package com.connection.simpleclient;

import java.io.IOException;

import dto.Message;
import dto.UserDTO;
import gui.Login;
import gui.MainContent;
import receiver.DataListener;
import receiver.Receiver;

/**
 * Created by Alexandru on 16.10.2016.
 */
public class ConnectionController  implements DataListener{
    private static ConnectionController singleton;
    private Controller controller = Controller.getInstance();
    private boolean existsConnectTask = false;
    private ConnectionChecker checker = null;
    private Thread checkerThread;
    private UserDTO sentUser;
    private boolean disconnected = false;
    private boolean autoconectare;

    private ConnectionController() {
        autoconectare = Controller.getInstance().deserializeAutoconectareButton();
    }

    public static ConnectionController getInstance() {
        if (singleton == null) {
            singleton = new ConnectionController();
        }
        return  singleton;
    }

    public boolean isAutoconectare() {
        return autoconectare;
    }

    public void setAutoconectare(boolean autoconectare) {
        this.autoconectare = autoconectare;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void addListener (Receiver r) {
        if (r != null) {
            r.addListener(this);
        }
    }

    private boolean isWaitingForUser;
    @Override
    public void update(Object... o) {
        if (o[0] instanceof Message) {
            Message m = (Message) o[0];

            switch (m) {
                case STOP_CONNECTION:
                    controller.setOffline(true);
                    controller.display("Esti Offline");
                    break;
                case KEEP_CONNECTION_ON:
                    controller.sendMessage(Message.KEEP_CONNECTION_ON);
                    break;
                case OK_USER:
                    if (Login.pushedButton == Login.LOG_BUTTON){
                        Login.pushedButton = Login.NO_BUTTON;
                        Controller.getInstance().display("Logat");
                        return;
                    }
                    Controller.getInstance().setUser(sentUser);
                    Controller.getInstance().getClient().sendData(Message.CONNECT_USER);

                    isWaitingForUser = true;
                    break;
                case CHECK_SERIE: {
                    break;
                }

            }
        }else if (isWaitingForUser && o[0] instanceof UserDTO) {
            isWaitingForUser = false;
            UserDTO user = (UserDTO) o[0];
            controller.serializeUser(user);
            controller.setUser(user);
            controller.setOffline(false);
            controller.sendMessage(Message.IS_SOMETHING_TO_RECIEVE);
        }

    }

    public void connectToServerManually () {
        if (disconnected) return;

        Object[] socket = Controller.getInstance().getSocket();
        Controller.getInstance().getMainActivity().startConnectTask((String) socket[0], socket[1]+"");
    }

    public void connectToServer () {
        if (disconnected || !autoconectare) return;

        if (checker != null) {
            checker.stop();
        }

        checker = new ConnectionChecker();
        checkerThread = new Thread(checker);
        checkerThread.start();
    }

    public void connect () {
        disconnected = false;
    }

    public void disconnect () {
        if (checker != null) checker.stop();
        disconnected = true;
        controller.sendMessage(Message.STOP_CONNECTION);
        controller.setOffline(true);
        if (controller.getClient() != null) {
            controller.getClient().shutdown();
        }
        existsConnectTask = false;
    }

    public boolean isExistsConnectTask() {
        return existsConnectTask;
    }

    public void setExistsConnectTask(boolean existsConnectTask) {
        this.existsConnectTask = existsConnectTask;
    }

    public void connectUser (String username, String parola) throws IOException {
        sentUser = Controller.getInstance().conectare(username, parola);
    }
}
