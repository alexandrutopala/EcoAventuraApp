/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javax.swing.SwingWorker;

/**
 *
 * @author Marius
 */
public class AsyncTask extends SwingWorker<Void, Void>{
    final private Event doInBackgroundEvent;
    final private Event doneEvent;
    
    public AsyncTask (Event doInBackgroundEvent) throws NullPointerException{
        this.doInBackgroundEvent = doInBackgroundEvent;  
        this.doneEvent = null;
        if (doInBackgroundEvent == null) throw new NullPointerException("Background action cannot be null");
    }
    
    public AsyncTask (Event doInBackgroundEvent, Event doneEvent) throws NullPointerException {
        this.doInBackgroundEvent = doInBackgroundEvent;
        this.doneEvent = doneEvent;
        if (doInBackgroundEvent == null) throw new NullPointerException("Background action cannot be null");
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        if (doInBackgroundEvent != null) {
            doInBackgroundEvent.doAction();
        }
        return null;
    }

    @Override
    protected void done() {
        if (doneEvent != null){
            doneEvent.doAction();
        }
    }
    
    
}
