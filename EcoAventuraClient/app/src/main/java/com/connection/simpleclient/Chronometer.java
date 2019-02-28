package com.connection.simpleclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.os.Vibrator;

import gui.GameActivity;

/**
 * Created by Alexandru on 16.10.2016.
 */
public class Chronometer implements Runnable {
    public static final long MILLIS_TO_MINUTES = 60000;
    public static final long MILLIS_TO_HOURS = 3600000;
    public static final long MILLIS_TO_SECONDS = 1000;

    private Context mContext;
    private long mStartTime = 0;
    private long mTimeLimit = -1;

    private boolean isRunning;

    /*
    * mTimeLimit - timpul pana la care merge cronometrul
    *               -1 pt infinit
    */
    public Chronometer (Context context, long startTime, long mTimeLimit){
        this.mContext = context;
        this.mStartTime = startTime;
        this.mTimeLimit = mTimeLimit;
    }

    public void start () {
        if (mStartTime == -1) {
            mStartTime = System.currentTimeMillis();
        }

        isRunning = true;
    }

    public void stop () {
        isRunning = false;
    }


    @Override
    public void run() {
        while (isRunning) {
            long since = System.currentTimeMillis() - mStartTime;
            int seonds = (int) ((since / MILLIS_TO_SECONDS) % 60);
            int minutes = (int) ((since / MILLIS_TO_MINUTES) % 60);
            int hours = (int) ((since / MILLIS_TO_HOURS) % 24);
            int millis = (int) (since % 1000);
            long time = hours;
            time = time * 100 + minutes;
            time = time * 100 + seonds;
            time = time * 1000 + millis;
            ((GameActivity) mContext).updateTimerText(String.format(
                    "%02d:%02d:%02d:%03d", hours, minutes, seonds, millis
            ));

            if (mTimeLimit != -1) {
                if (time > mTimeLimit * 100000){
                    isRunning = false;
                    Message m = ((GameActivity) mContext).getHandler().obtainMessage();
                    m.what = 1;
                    ((GameActivity) mContext).getHandler().sendMessage(m);
                }
            }

            try {Thread.sleep(10);} catch (Exception e) {}
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public long getStartTime ()
    {
        return mStartTime;
    }

    public void setmStartTime(long mStartTime) { this.mStartTime = mStartTime; }
}
