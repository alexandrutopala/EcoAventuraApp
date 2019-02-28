/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import gui.SerieActivaFrame;
import java.awt.Color;
import java.awt.Frame;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;

/**
 *
 * @author Alexandru
 */
public class Timer implements Runnable {
    private JLabel dataExpirare;
    private JLabel timpRamas;
    private boolean running;
    private Date expireDate = null;
    private boolean expired = false;
    private boolean notifyWhenExpired = false;
    private boolean asked = true;
    private boolean frozen = false;
    final private Frame parent;
    
    public Timer (JLabel dataExpirare, JLabel timpRamas, Frame parent) {
        this.dataExpirare = dataExpirare;
        this.timpRamas = timpRamas;
        this.parent = parent;
        // luam ultima data retinuta
        expireDate = SerializeController.getInstance().getDataExpirare();
        
        // daca data expirarii este in urma datei actuale, inseamna ca programul a expirat deja, deci 
        // vom reseta datele
        if (expireDate != null) {
            if (Calendar.getInstance().getTime().after(expireDate)) {
                expireDate = null;
                SerializeController.getInstance().setDataExpirare(expireDate);
                expired = true;
            }
        } else { 
            expired = true;
        }
    }

    @Override
    public void run() {
        running = true;
        refresh();
        while (running) {  
            if (!frozen) {
                if (!expired) {
                    asked = false;
                    if (expireDate != null ) {
                        long diff = expireDate.getTime() - Calendar.getInstance().getTimeInMillis();
                        long hours = TimeUnit.MILLISECONDS.toHours(diff);
                        diff %= (1000 * 60 * 60);
                        long mins = TimeUnit.MILLISECONDS.toMinutes(diff);
                        diff %= (1000 * 60);
                        long secs = TimeUnit.MILLISECONDS.toSeconds(diff);
                        timpRamas.setText((hours < 10 ? "0" : "") + hours + ":" + (mins < 10 ? "0" : "") + mins + ":" + (secs < 10 ? "0" : "") + secs);

                        if (TimeUnit.MILLISECONDS.toMinutes(expireDate.getTime() - Calendar.getInstance().getTimeInMillis()) > 60) {
                            timpRamas.setForeground(Color.BLACK);
                        } else {
                            timpRamas.setForeground(Color.RED);
                        }

                        if (diff < 0) {
                            expired = true;
                        }

                    } else {
                        timpRamas.setText("infinit");
                        timpRamas.setForeground(Color.BLACK);
                        expired = true;
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        dataExpirare.setText("nedefinit");
                        timpRamas.setText("infinit");
                    }


                } else if (!asked) {
                    SerieActivaFrame.prelungesteSesiuneDialog(parent, true);
                    asked = true;
                    try {Thread.sleep(3000);} catch (Exception e) {}
                }
            } else {
                try {Thread.sleep(3000);} catch (Exception e) {}
            }
        }
    }
    
    public void setDataExpirare (Date expireDate) {
        this.expireDate = expireDate;
        refresh();
    }
    
    public Date getDataExpirare () {
        return expireDate;
    }
    
    public void setDefaultDataExpirare () {  
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        this.expireDate = cal.getTime();
        if (Calendar.getInstance().getTime().before(expireDate)) {
            expired = false;
        } else {
            expired = true;
        }
        refresh();
    }
    
    public void refresh () {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm (dd/MM/yyyy)");
        dataExpirare.setText(expireDate != null ? sdf.format(expireDate) : "nedefinit");     
        SerializeController.getInstance().setDataExpirare(expireDate);
        if (expireDate == null){
            expired = true;
            timpRamas.setText("infinit");
        } else if (Calendar.getInstance().getTime().before(expireDate)) {
            expired = false;
            timpRamas.setText("calculeaza...");
        } else {
            expired = true;
            timpRamas.setText("infinit");
        }
    }
    
    public void stop () {
        running = false;
    }
    
    public boolean isRunning () {
        return running;
    }
    
    public boolean hasExpired () {
        return expired;
    }
    
    public void freeze () {
        frozen = true;
    }
    
    public void melt () {
        frozen = false;
    }
    
    public void setTimpRamasLabel (JLabel timpRamas) {
        this.timpRamas = timpRamas;
    }
    
    public void setDataExpirareLabel (JLabel dataExpirare) {
        this.dataExpirare = dataExpirare;
    }
}
