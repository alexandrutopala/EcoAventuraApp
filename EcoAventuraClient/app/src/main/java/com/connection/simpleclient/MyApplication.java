package com.connection.simpleclient;


import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import floatingWindow.FloatingWindow;

/**
 * Created by Alexandru on 16.02.2017.
 */

public class MyApplication extends Application {
    public static final String [] PLATFORMS = new String[] {"Android 5", "Android 6"};
    public static final String EMAIL_CONTACT = "alexandru.top98@gmail.com";
    public static final String BUILD_DATE = "13.08.2017";
    private static Context applicationContext;

    public void onCreate() {
        super.onCreate();
        // Setup handler for uncaught exceptions.

        applicationContext = getApplicationContext();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                Controller.getInstance().saveCrushRegister(
                        Controller.getInstance().getCrushRegister(getApplicationContext()),
                        getApplicationContext()
                );
            }
        }));



    }

    public static Context getMyApplicationContext () {
        return applicationContext;
    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically
        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Toast.makeText(getApplicationContext(), "Versiune incompatibila", Toast.LENGTH_SHORT);
        }

        String message = getErrorMessage(e);
        String subject = "LOG: " +  new SimpleDateFormat("dd/MM/yyy - HH:mm:ss").format(Calendar.getInstance().getTime());

        Controller.getInstance().saveCrushLog(subject, message, this);
        Log.i("crush", "Crush report saved");

        Controller.getInstance().saveCrushRegister(
                Controller.getInstance().getCrushRegister(getApplicationContext()),
                getApplicationContext()
        );

        Log.i("crush", "Crush register saved");

        System.exit(1); // kill off the crashed app
    }

    public String getErrorMessage (Throwable e) {
        StackTraceElement[] stackTrackElementArray = e.getStackTrace();

        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo (this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
        }
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model;

        String crashLog;
        crashLog = ("Android version: " +  Build.VERSION.SDK_INT + "\n" +
                "Device: " + model + "\n" +
                "App version: " + (info == null ? "(null)" : info.versionCode) + "\n\n");

        crashLog += e.toString() + "\n\n";
        crashLog += "--------- Stack trace ---------\n\n";
        for (int i = 0; i < stackTrackElementArray.length; i++) {
            crashLog += "    " + stackTrackElementArray[i].toString() + "\n";
        }
        crashLog += "-------------------------------\n\n";

        return crashLog;
    }

    public String convertLogToString () {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo (this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
        }
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model;

        try {
            String cmd = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) ?
                    "logcat -d -v time MyApp:v dalvikvm:v System.err:v *:s" :
                    "logcat -d -v time";

            Process process = Runtime.getRuntime().exec(cmd);

            String message = "Android version: " +  Build.VERSION.SDK_INT + "\n" +
                    "Device: " + model + "\n" +
                    "App version: " + (info == null ? "(null)" : info.versionCode) + "\n";

            StringWriter writer = new StringWriter();
            IOUtils.copy(process.getErrorStream(), writer, "UTF-8");
            message += writer.toString();

            return message;

        }catch (Exception e){
            return "";
        }
    }
}
