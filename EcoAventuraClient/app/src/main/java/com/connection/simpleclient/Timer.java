package com.connection.simpleclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Alexandru on 14.02.2017.
 */

public class Timer extends AsyncTask<Void, String, Void> {
    private TextView timpRamas;
    private TextView dataExpirare;
    private Date expireDate;
    private boolean expired = false;
    private boolean running;
    private Context context;
    private boolean executing = false;
    public boolean avertizat = false;

    public Timer(TextView timpRamas, TextView dataExpirare, Context context) {
        this.timpRamas = timpRamas;
        this.dataExpirare = dataExpirare;
        this.context = context;
    }

    public Timer(TextView timpRamas, TextView dataExpirare, Date expireDate, Context context) {
        this.timpRamas = timpRamas;
        this.dataExpirare = dataExpirare;
        this.expireDate = expireDate;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        running = true;
        refresh();

        while (running) {
            if (!expired) {
                long diff = expireDate.getTime() - Calendar.getInstance().getTimeInMillis();
                long hours = TimeUnit.MILLISECONDS.toHours(diff);
                diff %= (1000 * 60 * 60);
                long mins = TimeUnit.MILLISECONDS.toMinutes(diff);
                diff %= (1000 * 60);
                long secs = TimeUnit.MILLISECONDS.toSeconds(diff);
                publishProgress("Timp ramas: " + (hours < 10 ? "0" : "") + hours + ":" + (mins < 10 ? "0" : "") + mins + ":" + (secs < 10 ? "0" : "") + secs, null);

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {}
                if (diff < 0) {
                    expired = true;
                }
            }
            //else {
            //    running = false;
            //    vibrate(500);
            //}
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (values[0] != null) {
            timpRamas.setText(values[0]);
        }
        if (values[1] != null) {
            dataExpirare.setText(values[1]);
        }
        if (!expired) {
            if (TimeUnit.MILLISECONDS.toMinutes(expireDate.getTime() - Calendar.getInstance().getTimeInMillis()) > 60) {
                timpRamas.setTextColor(Color.WHITE);

            } else if (TimeUnit.MILLISECONDS.toMinutes(expireDate.getTime() - Calendar.getInstance().getTimeInMillis()) > 0 && !avertizat) {
                try {
                    timpRamas.setTextColor(Color.parseColor("#d24304"));
                    showAlert("Nu e panica, man", "Mai ai mai putin de o ora pentru a trimite activitatile realizate");
                    vibrate(500);
                    avertizat = true;
                } catch (Exception e) {}
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        executing = true;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        executing = false;
    }

    public void setDataExpirare (Date date) {
        this.expireDate = date;
        avertizat = false;
        refresh();
    }

    public Date getExpireDate () {
        return expireDate;
    }

    public void refresh () {
        if (expireDate == null) {
            expired = true;
            publishProgress("Timp ramas: Infinit", "Data inchiderii programului: Nedefinita");
            return;
        }
        publishProgress(null, "Data inchiderii programului: " + new SimpleDateFormat("HH:mm (dd.MM.yyyy)").format(expireDate));
        expired = Calendar.getInstance().getTime().after(expireDate);
        if (expired) {
            publishProgress("Timp ramas: plan incheiat", null);
        }
    }

    public void stop () {
        running = false;
    }

    public boolean isRunning () {
        return running;
    }

    private void showAlert (String title, String msg) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Ok", null)
                .show();
    }

    private void vibrate (int d) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(d);
    }

    public boolean isExecuting () {
        return executing;
    }

    public boolean hasExpired () {
        return expired;
    }

    public void setTimpRamas (TextView timpRamas) {
        this.timpRamas = timpRamas;
    }

    public void setDataExpirare (TextView dataExpirare) {
        this.dataExpirare = dataExpirare;
    }

}
