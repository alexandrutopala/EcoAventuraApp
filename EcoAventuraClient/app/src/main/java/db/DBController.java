package db;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import db.insert.SendRequest;
import db.retriev.DataModel;
import gui.Start;
import temporizator.Event;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Marius on 20-Apr-17.
 */
public class DBController {
    private static DBController ourInstance = new DBController();

    public static DBController getInstance() {
        return ourInstance;
    }

    private DBController() {
    }

    public void registerThisPhone (final Context mContext, String code) {
        String id = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String [] params = new String [] {
                id,
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()),
                Build.MODEL,
                code
        };

        final ProgressDialog dialog;
        ProgressDialog aux;
        try {
            aux = ProgressDialog.show(mContext, "Asteapta...", "Se insereaza o coloana");
        }catch (Exception e) {aux = null;}
        dialog = aux;

        Event startEvent = new Event() {
            @Override
            public void doAction(Object... obj) {
                try {
                    dialog.show();
                } catch (Exception e) {}
            }
        };

        Event doneEvent = new Event() {
            @Override
            public void doAction(Object... obj) {
                try {dialog.dismiss();}catch (Exception e){}
            }
        };

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        new SendRequest(connectivityManager, startEvent, doneEvent).execute(params);
    }

}
