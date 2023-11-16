package com.self.demo01.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;



import com.sunmi.extprinterservice.ExtPrinterService;

public class AidlUtil {
    private static final String SERVICE＿PACKAGE = "com.sunmi.extprinterservice";
    private static final String SERVICE＿ACTION = "com.sunmi.extprinterservice.PrinterService";

    private ExtPrinterService printerService;
    private static AidlUtil mAidlUtil = new AidlUtil();
    private Context context;

    private AidlUtil() {
    }

    public static AidlUtil getInstance() {
        return mAidlUtil;
    }

    public void connectPrinterService(Context context) {
        this.context = context.getApplicationContext();
        Intent intent = new Intent();
        intent.setPackage(SERVICE＿PACKAGE);
        intent.setAction(SERVICE＿ACTION);
        context.getApplicationContext().startService(intent);
        context.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    public void disconnectPrinterService(Context context) {
        if (printerService != null) {
            context.getApplicationContext().unbindService(connService);
            printerService = null;
        }
    }

    public boolean isConnect() {
        return printerService != null;
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("tryhard","aidl service is disconnected");
            printerService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("tryhard","aidl service is connected");
            printerService = ExtPrinterService.Stub.asInterface(service);
        }
    };

    public void initPrinter() {
        if (printerService == null) {
            Toast.makeText(context,"Init",Toast.LENGTH_LONG).show();
            return;
        }

        try {
            printerService.printerInit();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cutPaper() {
        if (printerService == null) {
            Toast.makeText(context,"Cut",Toast.LENGTH_LONG).show();
            return;
        }

        try {
            printerService.cutPaper(0, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}