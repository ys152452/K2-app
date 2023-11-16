package com.self.demo01;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.self.demo01.print.CommandRequest;
import com.self.demo01.print.PrintUtil;
import com.self.demo01.utils.LogUtil;
import com.self.demo01.utils.AidlUtil;
import com.sunmi.thingservice.sdk.IResponseCallback;
import com.sunmi.thingservice.sdk.IServiceEventListener;
import com.sunmi.thingservice.sdk.ThingSDK;
import com.sunmi.thingservice.sdk.ThingService;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static MainActivity app;
    private static final String TAG = "MyActivity";
    private IntentFilter filter;
    private SMSReceiver sms;
    boolean scanTag = true;
    private static final String ACTION_DATA_CODE_RECEIVED = "com.sunmi.scanner.ACTION_DATA_CODE_RECEIVED";

    private static final String DATA = "data";
    private static final String SOURCE = "source_byte";

    String types = "";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = this;
        setContentView(R.layout.activity_main);
        filter = new IntentFilter(ACTION_DATA_CODE_RECEIVED);
        sms = new SMSReceiver();
        registerReceiver(sms, filter);
        AidlUtil.getInstance().connectPrinterService(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(sms);
    }
    class SMSReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String code = intent.getStringExtra(DATA);
            String arr = Arrays.toString(intent.getByteArrayExtra(SOURCE));

            Log.d("TTTT", "读手牌：" + code + "---" + arr);
            Log.d("获取传参","---------"+types+"----------");
            WebView webView = (WebView) findViewById(R.id.web_view);
            if(code.indexOf("000026") != -1){
                code =  code.split("000026")[1];
            }
            String finalCode = code;
            runOnUiThread(() -> {
                webView.loadUrl("javascript:"+types+"('" + finalCode + "')");
                webView.loadUrl("javascript:handClick()");
            });
        }


    }
    private class MyWebViewClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
    private void initView() {
        WebView webView = (WebView) findViewById(R.id.web_view);
//        String url = "file:///android_asset/index.html";
//        String url = "http://172.16.8.254:8080/";
        String url = "https://trade-app.zkctd.cn";


        WebSettings webSettings = webView.getSettings();
        //可以访问https
        webSettings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //开启JavaScript
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
        webView.addJavascriptInterface(new WebAppInterface(this),"K2");

        webView.loadUrl(url);
        webView.setWebViewClient(new MyWebViewClient(){
            @Override
            @SuppressWarnings("deprecation")
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();// 接受所有网站的证书
                super.onReceivedSslError(view, handler, error);
            }
        });
    }
    public class WebAppInterface {
        Context mContext;
        private int ret;

        WebAppInterface(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public void doPrint(String b64) {
            print(PrintUtil.getPicRequest(b64));
        }
        @JavascriptInterface
        public void doToast(String msg) {
            LogUtil.e(TAG, "webview send-----:" + msg);
            Toast.makeText(app, "webview打印：" + msg, Toast.LENGTH_SHORT).show();
        }
        @JavascriptInterface
        public void typesClick(String type){
            types = type;
        }

    }
    private synchronized void print(CommandRequest request) {
        if (Application.printerList == null || Application.printerList.size() == 0) {
            Toast.makeText(this, "获取打印机失败", Toast.LENGTH_SHORT).show();
            return;
        } else {
            LogUtil.e(TAG, "sass send------:" + PrintUtil.gson.toJson(Application.printerList.get(0)));
        }
        String callUuid = null;
        ThingService service = Application.printerList.get(0);

        final long time = SystemClock.elapsedRealtime();
        LogUtil.e(TAG, "sass send:" + SystemClock.elapsedRealtime());
        try {
            callUuid = ThingSDK.getInstance().execute(service, ThingSDK.ACTION_TYPE_COMMAND, ThingSDK.ACTION_EXECUTE, PrintUtil.gson.toJson(request), new IResponseCallback.Stub() {
                @Override
                public void response(String uuid, Map data) throws RemoteException {
                    LogUtil.e(TAG, "sass receive:" + SystemClock.elapsedRealtime());
                    LogUtil.e(TAG, uuid + ";" + time);
                    long t1 = SystemClock.elapsedRealtime();
                    LogUtil.e(TAG, time + ";" + t1 + "  ; " + (t1 - time));
                    if (data != null) {
                        StringBuilder builder = new StringBuilder("print-->");
                        for (Object s : data.keySet()) {
                            builder.append(s + "  :  " + data.get(s) + "；");
                        }
                        LogUtil.e(TAG, "res:" + builder.toString() + "    ;" + t1);
                        ThingSDK.getInstance().executeCommand(service, "{command: 'cutPaper'}", null);
                    }
                    AidlUtil.getInstance().cutPaper();
                }
            });

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, " print call :" + callUuid + ";" + PrintUtil.gson.toJson(request));
    }

    IServiceEventListener iServiceEventListener = new IServiceEventListener.Stub() {
        @Override
        public void onEvent(String deviceId, String serviceId, String eventParam) throws RemoteException {
            LogUtil.e("ricardo2   ", " registerEvent call :" + deviceId + "  " + serviceId + "  " + eventParam);
            WebView webView = (WebView) findViewById(R.id.web_view);
            runOnUiThread(() -> {
                webView.loadUrl("javascript:recK2Msg('" + eventParam + "')");
            });
            iServiceEventListener = null;
        }
    };

}
