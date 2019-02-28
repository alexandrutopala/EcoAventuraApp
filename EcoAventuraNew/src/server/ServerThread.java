/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import dto.Message;
import dto.ActivitateDTO;
import dto.EchipaDTO;
import dto.JocDTO;
import dto.JoinDTO;
import dto.UserDTO;
import gui.ActivitatiCompleteFrame;
import gui.SerieActivaFrame;
import gui.StartFrame;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import observer.Receiver;
import service.ControllerDB;
import service.SerializeController;

/**
 *
 * @author Alexandru
 */
public class ServerThread extends Thread{
    private Socket socket;
    private UserDTO user;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean running = false;
    private Receiver receiver;
    private boolean activeThread = true;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm : ");
    
    private final static int WAIT = 500;
    
    
    public ServerThread (Socket socket, Receiver receiver) {    
        this.socket = socket;
        this.receiver = receiver;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            
            ////System.out.println("output stream created");
            
            in = new ObjectInputStream (
                    socket.getInputStream()
            );
            
            ////System.out.println("input stream created");           
        } catch (Exception e) {
            System.out.println(sdf.format(Calendar.getInstance().getTime()) + "Eroare la conectarea unui dispozitiv : " + e.getMessage());
        }
        
        
        System.out.println(sdf.format(Calendar.getInstance().getTime()) + "Un nou dispozitiv s-a conectat! Adresa :" + socket.getInetAddress());
    }
    
    @Override
    public void run () {
        boolean commEstablished = false;
        boolean serieActuaizata = true;
        running = true;
        Message m = null;
        
        ////
        System.out.println(sdf.format(Calendar.getInstance().getTime()) + sdf.format(Calendar.getInstance().getTime()) + socket.getInetAddress() + " : Se stabileste o conexiune...");
        try {
            while (socket.isConnected() && running){
                if (!commEstablished) {               
                    do {
                        m = (Message) ServerFrame.getInstance().readObject(in, out); 
                    }while (m == null && running);  
                    
                    //// System.out.println("Data received!!");
                    
                    if (m == Message.ASK_CONNECTION){
                        out.writeObject(Message.CONFIRM_CONNECTION);
                        out.flush();
                        commEstablished = true;
                        
                        try { Thread.sleep(50); } catch (Exception e) {}    
                        
                        out.reset();
                        if (StartFrame.CRIPT == null) StartFrame.CRIPT = StartFrame.UNDEFINED_CRIPT;
                        out.writeObject(StartFrame.CRIPT);
                        out.flush();
                        ////
                        System.out.println(sdf.format(Calendar.getInstance().getTime()) + socket.getInetAddress() + " : Conexiune stabilita!");
                    }                    
                } else if (!serieActuaizata && user != null) {
                    out.writeObject(Message.CHECK_SERIE);
                    out.flush();
                    try {
                        Thread.sleep(WAIT);
                    } catch (Exception e) {}
                    out.writeObject(ControllerDB.getInstance().getLastSerie().getIdSerie());
                    out.flush();
                    serieActuaizata = true;
                } else {
                    
                    Object auxObj = ServerFrame.getInstance().readObject(in, out); 
                    if (!(auxObj instanceof Message) || auxObj == null) continue;
                    m = (Message) auxObj;           
                    
                    switch (m) {
                        case LOGIN_USER:{
                            UserDTO userdto = (UserDTO) ServerFrame.getInstance().readObject(in, out);
                            userdto.setAcces(UserDTO.ACCES_ANIMATOR);
                            int rez = ControllerDB.getInstance().logheazaAnimator(userdto);
                            
                            if (rez == ControllerDB.USER_OK) {
                                out.writeObject(Message.OK_USER);
                                out.flush();
                                ////
                                System.out.println(sdf.format(Calendar.getInstance().getTime()) + "Clientul " + userdto.getUsername() + "tocmai s-a logat.");
                            } else if (rez == ControllerDB.USER_EXISTENT){
                                out.writeObject(Message.EXISTENT_USER);
                                out.flush();
                            } else if (rez == ControllerDB.ANIMATOR_INEXISTENT) {
                                out.writeObject(Message.UNCREATED_USER);
                                out.flush();
                            } else {
                                out.writeObject(Message.EXISTENT_USER);
                                out.flush();
                            }
                            break;
                        }
                        case CONNECT_USER:{ 
                            Object o = ServerFrame.getInstance().readObject(in, out);
                            out.reset();
                            
                            if (!(o instanceof UserDTO)) {
                                distroy();
                            }
                            
                            UserDTO userdto = (UserDTO) o;
                            userdto = ControllerDB.getInstance().conecteazaAnimator(userdto);
                            
                            if (userdto != null) {
                                out.writeObject(Message.OK_USER);
                                out.flush();
                                if ((Message) ServerFrame.getInstance().readObject(in, out) == Message.CONNECT_USER){
                                    // adauga si id ul userului conectat
                                    userdto.setId(ControllerDB.getInstance().getUserIdByUsername(userdto.getUsername()));
                                    out.writeObject(userdto);
                                    out.flush();            
                                }
                                receiver.notifyListeners(userdto, true);
                                user = userdto;
                                ////
                                System.out.println(sdf.format(Calendar.getInstance().getTime()) + "Clientul : " + user.getUsername() + " este acum conectat.");
                            } else {
                                out.writeObject(Message.WRONG_PASSWORD);
                                out.flush();
                            }
                            break;
                        }
                        case IS_SOMETHING_TO_RECIEVE : {
                            if (user != null){
                            ////
                                System.out.println(sdf.format(Calendar.getInstance().getTime()) + "Clientul : " + user.getUsername() + " a trimis o cerere pentru primirea planului de activitati actual...");
                                share(ServerFrame.getInstance().getActivitati(), ServerFrame.getInstance().getJocuri());     
                                serieActuaizata = false;
            ////
                                System.out.println("Clientul : " + user.getUsername() + " : plan de activitati trimis!");
                              
                                
                            }
                            break;
                        }
                        case STOP_CONNECTION : {
                            ServerFrame.getInstance().removeClient(this);
                            distroy();
                            break;
                        } 
                        case DISCONNECT_USER : {
                            try {
                                receiver.notifyListeners(user, false);
                                ////
                                System.out.println(sdf.format(Calendar.getInstance().getTime()) + "Clientul : " + user.getUsername() + " tocmai s-a deconectat");
                            
                            } catch (Exception e) {}
                            finally {
                                user = null;
                            }
                            break;
                        }
                        case REQUEST_SERIE : {
                            ////
                            System.out.println(sdf.format(Calendar.getInstance().getTime()) + "Clientul : " + user.getUsername() + " a cerut seria activa...");
                            out.writeObject((List<EchipaDTO>) ControllerDB.getInstance().convert(ControllerDB.getInstance().getEchipeBySerie(ControllerDB.getInstance().getLastSerie())));
                            out.flush();
                            
                            break;
                        }
                        case IS_SOMETHING_TO_SEND : {
                            //// System.out.println(sdf.format(Calendar.getInstance().getTime()) + "Client : " + user.getUsername() + " just sent some new data! :D");
                            Object[] obj = (Object[]) ServerFrame.getInstance().readObject(in, out);
                            if (obj[0] instanceof List) {
                                List<JoinDTO> joins = (List<JoinDTO>) obj[0];
                                /// mai departe, informatiile urmeaza sa fie stocate in baza de date
                                ////
                                System.out.println(sdf.format(Calendar.getInstance().getTime()) + "Clientul : " + user.getUsername() + " tocmai a trimis noi informatii!");
                                ControllerDB.getInstance().storeJoins(joins, user); // am stocat datele
                                SerieActivaFrame.notifica(true);
                                ActivitatiCompleteFrame.getInstance().refresh();
                                out.writeObject(Message.ACTIVITIES_RECEIVED);
                                out.flush();
                            } else {
                                ////
                                System.err.println(sdf.format(Calendar.getInstance().getTime()) + "Clientul : " + user.getUsername() + " Pachet Invalid");
                            }
                            break;
                        }  
                        case CHECK_CONNECTION : {
                            activeThread = true;
                            break;
                        }
                        case SENDING_SERIE :
                            System.out.println("Client : " + user.getUsername() + " made a request for sending seria");                          
                            
                            //try { Thread.sleep(WAIT); ) catch (Exception e) {}
                            shareEchipe();
                            break;
                        case TEST_CONNECTION :
                            out.writeObject(Message.TEST_CONNECTION);
                            out.flush();
                            break;
                    }
                }
            }
            
            ////
            System.out.println(sdf.format(Calendar.getInstance().getTime()) + "Clientul"
                    + (user != null ? user.getUsername() : socket.getInetAddress()) +" tocmai s-a deconectat");
        } catch (Exception e) {
            System.err.println(sdf.format(Calendar.getInstance().getTime()) + "Client " + (user != null ? user.getUsername() : socket.getInetAddress()) + " : Eroare : " + e.getMessage());
            e.printStackTrace();
            distroy();
        }
    }
    
    public void distroy () {
        try {
            if (ServerFrame.isSingletonNull()) return;
            if (socket != null && ServerFrame.getInstance().getDevices() != null) {
                ServerFrame.getInstance().getDevices().removeDevice(socket);
            }
            out.writeObject(Message.STOP_CONNECTION);
            out.flush();
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException ex) {
            //Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            running = false;     
            if (user != null) {
                receiver.notifyListeners(user, false);
            }
        }
    }
    
    public void shareEchipe () {
        try {
            if (user == null) return;
            out.writeObject(Message.SENDING_SERIE);
            out.flush();
            
            Object [] objs = new Object[2];
            objs[0] = (List<EchipaDTO>) ControllerDB.getInstance().convert(ControllerDB.getInstance().getEchipeBySerie(ControllerDB.getInstance().getLastSerie()));
            objs[1] = SerieActivaFrame.getSerie().getNumarSerie();
            out.writeObject(objs);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
                            
    }
    
    public void share (List<ActivitateDTO> activitati, List<JocDTO> jocuri) throws IOException {
        if (ServerFrame.getInstance().isPackageToSend() && user != null){
            try {
                //activitati = ServerFrame.getInstance().getActivitati();
                //jocuri = ServerFrame.getInstance().getJocuri();
                
                //List<Object> sarcini = ServerFrame.getInstance().packData(activitati, jocuri);
                out.reset();
                out.writeObject(Message.IS_SOMETHING_TO_SEND);
                out.flush();
                Thread.sleep(WAIT);
                out.writeObject(SerializeController.getInstance().getProgramActivitatiID());
                out.flush();
                Thread.sleep(WAIT);
                out.writeObject(new ArrayList<>(activitati));
                out.flush();
                Thread.sleep(WAIT);
                out.writeObject(new ArrayList<>(jocuri));
                out.flush();
                Thread.sleep(WAIT);
                Date date = SerializeController.getInstance().getDataExpirare();
                if (date == null) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    date = cal.getTime();
                }
                out.writeObject(date);
                out.flush();
                ServerFrame.getInstance().userInformat(user);
            } catch (Exception e) {
                out.writeObject(null);
                e.printStackTrace();
                ////
                System.err.println(sdf.format(Calendar.getInstance().getTime()) + " Client : " + user.getUsername() + " - pachetul de date nu a putut fi trimis. Se deconecteaza...");
                distroy();
            }
        } else {
            out.writeObject(Message.NOTHING_TO_SEND);
            out.flush();
            ServerFrame.getInstance().userInformat(user);
            
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public UserDTO getUser() {
        return user;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public boolean isRunning() {
        return running;
    }

    public Receiver getReceiver() {
        return receiver;
    }
    
    public boolean getActiveThread () {
        return activeThread;
    }
    
    public void checkForConnection () {
        activeThread = false;
        try {
            out.writeObject(Message.CHECK_CONNECTION);
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
}

