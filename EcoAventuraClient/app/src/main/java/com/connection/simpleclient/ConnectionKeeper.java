package com.connection.simpleclient;

import android.os.AsyncTask;
import android.os.SystemClock;

import java.io.ObjectOutputStream;

import dto.Message;

/**
 * Created by Alexandru on 8/31/2016.
 */
public class ConnectionKeeper extends AsyncTask<ObjectOutputStream, Void, Void>{
    public final static  int DEFAULT_SLEEP_TIME = 5000;
    private boolean running = false;
    private int sleepTime = DEFAULT_SLEEP_TIME;

    @Override
    protected Void doInBackground (ObjectOutputStream... objs){
        running = false;
        ObjectOutputStream out = objs[0];

        try {
            while (running) {
                out.writeObject(Message.KEEP_CONNECTION_ON);
                SystemClock.sleep(sleepTime);
            }
        } catch (Exception e) {
            e.getMessage();
            Controller.getInstance().setOffline(true);
        }

        return null;
    }

    public void stop () {
        running = false;
    }

    public int getSleepTime () {
        return sleepTime;
    }

    public boolean setSleepTime (int sleepTime){
        if (sleepTime >= DEFAULT_SLEEP_TIME){
            this.sleepTime = sleepTime;
            return true;
        }
        return false;
    }
}
