package com.self.demo01;
import android.content.pm.PackageManager;
import android.os.RemoteException;

import com.self.demo01.utils.LogUtil;
import com.self.demo01.utils.ThreadPoolUtil;
import com.sunmi.iotauthorizelib.IotAuthorizeSDK;
import com.sunmi.iotauthorizelib.IotAuthorizer;
import com.sunmi.iotauthorizelib.LicenseResult;
import com.sunmi.iotdependsdk.IOTDependConfig;
import com.sunmi.iotdependsdk.IOTDependSDK;
import com.sunmi.iotdependsdk.ResultCode;
import com.sunmi.thingservice.sdk.ThingDevice;
import com.sunmi.thingservice.sdk.ThingSDK;
import com.sunmi.thingservice.sdk.ThingService;

import java.util.List;

public class Application extends android.app.Application {
    private static final String TAG = "Application";
    public static String license;
    public static List<ThingService> printerList;
    public static List<ThingService> scannerList;
    public static List<ThingService> msrList;
    public static List<ThingDevice> deviceList;
    private String appId = "自己的";
    private String appKey = "自己的";
    public static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        init();
    }

    private void init() {
        ThreadPoolUtil.getInstance().execute(()->{
            LicenseResult result = IotAuthorizeSDK.syncGetLicense(getApplicationContext(), appId, appKey, "");
            LogUtil.i(result.toString());
            LogUtil.i(getApplicationContext().toString());
            if (result.code == IotAuthorizer.CODE_SUCCESS){
                //success , do something
                license = result.data;
                LogUtil.d(TAG, "license======"+license);

                IOTDependConfig config = IOTDependConfig
                        .builder()
                        .strategyType(IOTDependConfig.STRATEGY_CACHE_FIRST)
                        .cacheExpiry(60 * 60)
                        .showErrorDialog(true)
                        .license(license)
                        .build();
                int initResult = IOTDependSDK.getInstance().init(getApplicationContext(), config);
                if (initResult == ResultCode.CODE_SUCCESS){
                    //success, do something

                    IOTDependSDK.getInstance().asyncDepend(new IOTDependSDK.ResultCallback() {
                        @Override
                        public void onSuccess() {
                            //成功回调，走后续流程
                            LogUtil.d(TAG,"IOTDependSDK======onSuccess");
                            getService();
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            LogUtil.d(TAG,"IOTDependSDK======onFail"+msg);
                            LogUtil.d(TAG,"IOTDependSDK======onFail"+code);
                            //失败
                            if (code == ResultCode.CODE_PERMISSION_ERROR) {
                                //请求 READ_PHONE_STATE 权限
//                                requestPerm();
                            }
                        }

                        @Override
                        public void onStart() {
                            LogUtil.d(TAG,"IOTDependSDK======onStart");
                        }
                    });

                } else {
                    //error
                }

            } else {
                //fail
                String errorMdg = result.msg;
                LogUtil.d(TAG, "license======errorMdg::"+errorMdg+"code:"+result.code);
            }
        });
    }
    private void initView() {
    }
    private void getService() {
        int initResult = ThingSDK.getInstance().init(getApplicationContext(), license, new ThingSDK.StatusListener() {
            @Override
            public void connected() {
                try {
                    deviceList = ThingSDK.getInstance().getDevices();
                    printerList = ThingSDK.getInstance().getService("printer");
                    msrList = ThingSDK.getInstance().getService("id_mifare_card");
                    scannerList = ThingSDK.getInstance().getService("scanner");
                    LogUtil.e(TAG+"  print: ", deviceList + " ------------- " + deviceList.size());
                    LogUtil.e(TAG+"  print: ", printerList + " ------------- " + printerList.size());
                    LogUtil.e(TAG+"  msrList: ", msrList + " ------------- " + msrList.size());
                    LogUtil.i(TAG + "  scan: ", scannerList + " ------------- " + scannerList.size());

//                    if(type.equals("scanner")){
//                        ThingSDK.getInstance().registerServiceEvent(printerList.get(0).deviceId, printerList.get(0).serviceId, iServiceEventListener);
//                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void disconnected() {
                LogUtil.e(TAG, "disconnected");
            }
        });
        LogUtil.i(TAG, "ThingSDK init result: " + initResult);
    }
}
