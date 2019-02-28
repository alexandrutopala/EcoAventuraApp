package com.connection.simpleclient;

/**
 * Created by Alexandru on 17.10.2016.
 */
public class ConnectionChecker implements Runnable {
    private final static int WAIT = 5000;
    private boolean running;
    @Override
    public void run() {
        running = true;
        while (running && ConnectionController.getInstance().isAutoconectare()) {
            if (Controller.getInstance().getMainActivity() != null && !ConnectionController.getInstance().isExistsConnectTask() && !ConnectionController.getInstance().isDisconnected()) {
                try {
                    Object[] socket = Controller.getInstance().getSocket();
                    Controller.getInstance().getMainActivity().startConnectTask((String) socket[0], socket[1]+"");
                } catch (Exception e) {
                    try { Thread.sleep(WAIT);} catch (Exception e1) {}
                }
            } else {
                try { Thread.sleep(WAIT);} catch (Exception e1) {}
            }
            if (!Controller.getInstance().isOffline()) {
                stop();
            }
        }
    }

    public void stop () {
        running = false;
    }
}
