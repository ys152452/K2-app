package com.self.demo01;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.self.demo01.utils.LogUtil;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.self.demo01", appContext.getPackageName());
        try {
            PackageInfo packageInfo = appContext.getPackageManager().getPackageInfo(
                    appContext.getPackageName(),
                    PackageManager.GET_SIGNATURES
            );
            Signature[] signatures = packageInfo.signatures;
            if (signatures.length > 0) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(signatures[0].toByteArray());
                byte[] md5 = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b : md5) {
                    sb.append(String.format("%02X", b));
                }
                LogUtil.i(appContext.getPackageName().toString());
                LogUtil.i(sb.toString());
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}