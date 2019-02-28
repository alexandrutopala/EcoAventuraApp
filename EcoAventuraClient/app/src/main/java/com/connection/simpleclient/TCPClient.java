package com.connection.simpleclient;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketImplFactory;

import dto.Message;
import receiver.Receiver;

/**
 * Created by Alexandru on 8/21/2016.
 */
public class TCPClient {
    private final static int TIME_OUT = 15000;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private OnDataReceived receiver;
    private Receiver dataNotifier;
    private boolean running = false;

    public static final int CONEXIUNE_STABILTA = 0;
    public static final int NEPOTRIVIREA_CHEILOR = 2;
    public static final int CONEXIUNE_NECONFIRMATA = 1;

    private Socket socket;
    private String server;
    private int port;

    public TCPClient(OnDataReceived receiver, String server, int port) {
        this.receiver = receiver;
        this.server = server;
        this.port = port;
        this.dataNotifier = Controller.getInstance().getReceiver();
    }

    public int getPort () { return port; }

    public String getIp () { return server; }

    public void sendData(Object o) {
        if (out != null) {
            try {
                out.writeObject(o);
                out.flush();
            } catch (IOException e) {
                Controller.getInstance().setOffline(true);
                shutdown();
                e.printStackTrace();
            }
        }
    }

    public void stop() throws IOException {
        sendData(Message.STOP_CONNECTION);
        running = false;
        shutdown();
    }

    public int connect (Context context) throws Exception{

        //if (Controller.getInstance().isConnected()) return true;
        try {

            InetAddress serverAddress = InetAddress.getByName(server);
            InetSocketAddress address = new InetSocketAddress(serverAddress, port);
            //socket = new Socket(serverAddress, port);
            socket = new Socket();
            //socket.setSoTimeout(TIME_OUT);
            socket.connect(address, TIME_OUT);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            //out.writeObject(Message.ASK_CONNECTION);
            sendData(Message.ASK_CONNECTION);

            Message m;

            do {
                m = (Message) in.readObject();
            } while (m == null);

            if (m == Message.CONFIRM_CONNECTION) {
                //Controller.getInstance().setOffline(false); aceasta instructiune trebuie pusa in connectionChecker,
                // pt ca atunci cand se seteaza listenerii, receiverul sa nu fie null
                //sendData(Message.KEEP_CONNECTION_ON);
                //socket.setSoTimeout(0);
                String key;
                do {
                    key = (String) in.readObject();
                } while (key == null);

                String storedKey = Controller.getInstance().getServerKey(context);
                if (storedKey == null) {
                    Controller.getInstance().saveServerKey(context, key);
                    return CONEXIUNE_STABILTA;
                }

                if (storedKey.equals(key)) {
                    return CONEXIUNE_STABILTA;
                } else {
                    return NEPOTRIVIREA_CHEILOR;
                }
            }

            return CONEXIUNE_NECONFIRMATA;
        } catch (Exception e) {
            shutdown();
            throw e;
        }

    }


    public void run() {
        running = true;

        try {
            Object serverMessage = null;

            while (running && !Controller.getInstance().isOffline()) {
                serverMessage = in.readObject();
                //in.reset();

                if (serverMessage instanceof Message) {
                    if (serverMessage == Message.KEEP_CONNECTION_ON){
                        continue;
                    }

                    if (serverMessage == Message.CHECK_CONNECTION) {
                        sendData(Message.CHECK_CONNECTION);
                        continue;
                    }
                }

                if (serverMessage != null && receiver != null) {
                    receiver.dataReceived(serverMessage);
                }

            }

        } catch (Exception e) {
            running = false;
        } finally {
            shutdown();
        }
    }

    public void shutdown () {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (out != null) {
            try {
                out.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public ObjectOutputStream getOut () {return out;}
    public ObjectInputStream getIn () {return in;}

}
