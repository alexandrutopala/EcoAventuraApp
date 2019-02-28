/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import dialogs.AdressFrame;
import dto.ActivitateDTO;
import dto.JocDTO;
import dto.Message;
import dto.UserDTO;
import gui.HelperFrame;
import gui.SerieActivaFrame;
import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import main.AsyncTask;
import main.Event;
import main.Main;
import main.StreamGobbler;
import observer.DataListener;
import observer.Receiver;
import service.SerializeController;

/**
 *
 * @author Alexandru
 */
public class ServerFrame extends javax.swing.JFrame implements DataListener{
    private static int PORT = 16969;
    private static ServerFrame singleton;
    private static boolean IS_HOTSPOT_ON = false;
    private ServerSocket socket;
    private List<ActivitateDTO> activitati;
    private List<JocDTO> jocuri;
    private ArrayList<ServerThread> clients;
    private boolean running = false;
    private boolean runCheckConnectionThread = false;
    private boolean runAutoVerificareThread = true;
    private DefaultListModel<UserDTO> useriLogati;
    private DefaultListModel<UserDTO> useriInformati;
    private Receiver receiver;
    public static boolean isOn = false;
    public DevicesFrame devices;
    /**
     * Creates new form ServerFrame
     */
    private ServerFrame() {
        super ("Server Frame");
        initComponents();

        int auxPort = SerializeController.getInstance().getPort();
        if (auxPort != -1) PORT = auxPort;
        
        DefaultListModel<UserDTO> savedModel = SerializeController.getInstance().deserializeServerList();

        receiver = new Receiver();
        clients = new ArrayList<>();
        useriLogati = new DefaultListModel<>();
        useriInformati =  new DefaultListModel<>();      
        
        
        receiver.addListener(this);
        //isPackageToSend();
        this.activitati = SerieActivaFrame.activitatiS;
        this.jocuri = SerieActivaFrame.jocuriS;

        try {
            socket = new ServerSocket(PORT);
            running = true;
            new Thread () {
                @Override
                public void run () {
                    try {
                        try {
                            SerieActivaFrame.jButton7.setBackground(Color.GREEN);
                        } catch (Exception e) {
                        }
                        while (running) {
                            ServerThread st = new ServerThread(socket.accept(), receiver);
                            clients.add(st);
                            st.start();
                            if (devices != null) devices.addDevice(st.getSocket());
                        }
                        socket.close();
                        try {SerieActivaFrame.jButton7.setBackground(null);} catch (Exception e) {}
                    } catch (Exception e) {
                        ////
                        System.out.println("Socket inchis");
                    }
                }
            }.start();
        } catch (IOException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        useriLogati.addListDataListener(new ListDataListener() {

            @Override
            public void intervalAdded(ListDataEvent lde) {
                try {
                    UserDTO user = (UserDTO) useriLogati.get(lde.getIndex0());
                    boolean exists = false;
                    if (user == null) return;



                    try {
                        for (int i = 0; i < useriLogati.size() && !exists; ++i) {
                            if (((UserDTO) useriLogati.get(i)).getUsername().equals(user.getUsername()) && i != lde.getIndex0()) {
                                exists = true;
                            }
                        }
                    } catch (Exception e) {exists = true;}
                    if (exists) useriLogati.remove(lde.getIndex0());
                } catch (Exception e) {return;}
            }

            @Override
            public void intervalRemoved(ListDataEvent lde) {}

            @Override
            public void contentsChanged(ListDataEvent lde) {}
        });

        useriInformati.addListDataListener(new ListDataListener() {

            @Override
            public void intervalAdded(ListDataEvent lde) {
                UserDTO user = (UserDTO) useriInformati.get(lde.getIndex0());
                boolean exists = false;

                for (int i = 0; i < useriInformati.size() && !exists; ++i) {
                    if (((UserDTO) useriInformati.get(i)).getUsername().equals(user.getUsername()) && i != lde.getIndex0()) {
                        exists = true;
                    }
                }

                if (exists) useriInformati.remove(lde.getIndex0());
            }

            @Override
            public void intervalRemoved(ListDataEvent lde) {}

            @Override
            public void contentsChanged(ListDataEvent lde) {}
        });

        new Thread () {
            private static final int TIME_INTERVAL = 5000 * 60;
            @Override
            public void run() {
                runAutoVerificareThread = true;
                while (runAutoVerificareThread && jCheckBox1.isSelected()) {
                    try {
                        Thread.sleep(TIME_INTERVAL);
                        new StatusThread().start();
                    } catch (Exception e) {}
                }
                ////
                System.out.println("Auto checker stopped");
            }
        }.start();
        
        useriLogati.clear();
        useriInformati.clear();
        
        if (savedModel != null) {
            for (int i = 0; i < savedModel.getSize(); ++i) {
                UserDTO u = savedModel.get(i);
                useriInformati.addElement(u);
            }
        }
        
        jMenuItem4.setText("Porneste hotspot");
        
        jList1.setModel(useriLogati);
        jList2.setModel(useriInformati);
        jLabel5.setText("");
        ImageIcon icon = new ImageIcon("./res/loading.gif-c200");
        Image img = icon.getImage();
        img = img.getScaledInstance(15, 15, Image.SCALE_FAST);
        jLabel5.setIcon(new ImageIcon(img));
        jLabel5.setVisible(false);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
                       

        System.out.println("Serverul a pornit cu succes pe portul : " + PORT);
        isOn = true;
        try {
            SerieActivaFrame.jButton7.setBackground(Color.GREEN);
        } catch (Exception e) {
        }
        toFront();
    }

    public boolean isServerRunning () {
        return running; 
    }

    public static ServerFrame getInstance () {
        if (singleton == null) {
            singleton = new ServerFrame();
        }
        return singleton;
    }

    public static boolean isSingletonNull () {
        return (singleton == null);
    }

    public ServerSocket getServerSocket () {
        return socket;
    }

    public void removeClient (ServerThread client) {
        clients.remove(client);
    }

    public List<ServerThread> getClients () {
        return this.clients;
    }

    public DevicesFrame getDevices() {
        return devices;
    }

    public void setDevices(DevicesFrame devices) {
        this.devices = devices;
    }

    public boolean isPackageToSend () {
        if (this.activitati == null || this.jocuri == null) {
            this.activitati = SerieActivaFrame.getOrderedActivitati();
            this.jocuri = SerieActivaFrame.getOrderedJocuri();
        }

        return (activitati != null || jocuri != null);
    }

    public Object readObject (ObjectInputStream in, ObjectOutputStream out) {
        Object obj;
        try {
            do {
                obj = in.readObject();
                if (obj instanceof Message && (Message) obj == Message.KEEP_CONNECTION_ON) {
                    out.writeObject(Message.KEEP_CONNECTION_ON);
                    out.flush();
                }
            } while (obj instanceof Message && (Message) obj == Message.KEEP_CONNECTION_ON);

            return obj;
        }catch (Exception e) {
            return null;
        }
    }

    public boolean opresteServerul () {
        try {
            SerializeController.getInstance().serializeServerList(useriInformati);
            for (int i = 0; i < clients.size(); ++i) {
                ServerThread st = clients.get(i);
                st.distroy();
            }
            socket.close();
            running = false;
            singleton = null;
            System.out.println("Serverul a fost oprit cu succes");
            if (!Main.isClosingApp()) dispose();
            isOn = false;
            runAutoVerificareThread = false;
            runCheckConnectionThread = false;
            try {
                SerieActivaFrame.jButton7.setBackground(null);
            } catch (Exception e) {}
            try {
                if (IS_HOTSPOT_ON) {
                    jMenuItem4ActionPerformed(null);
                }
            } catch (Exception e) {}
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private void restartServer () {
        opresteServerul();
        ServerFrame.getInstance();
    }

    public void setPackage (List<ActivitateDTO> activitati, List<JocDTO> jocuri ){
        this.activitati = activitati;
        this.jocuri = jocuri;
    }

    public void shareActivities () {
        try { SerieActivaFrame.jButton7.setBackground(Color.GREEN); } catch (Exception e) {}
        
        useriInformati.clear();
        SerializeController.getInstance().serializeServerList(null);

        for (int i = 0; i < useriInformati.getSize(); ++i){
            useriLogati.addElement(useriInformati.get(i));
        }
        
        jList1.setModel(useriLogati);
        jList2.setModel(useriInformati);

        activitati = SerieActivaFrame.getOrderedActivitati();
        jocuri = SerieActivaFrame.getOrderedJocuri();

        for (ServerThread st : clients){
            if (st.getUser() != null){
                try {
                    st.share(activitati, jocuri);
                    UserDTO user = st.getUser();
                    useriLogati.removeElement(user);
                    useriInformati.addElement(user);
                    jList1.setModel(useriLogati);
                    jList2.setModel(useriInformati);
                } catch (IOException ex) {
                    Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void shareSerie () {
        useriInformati.clear();
        SerializeController.getInstance().serializeServerList(null);
        
        for (int i = 0; i < useriInformati.getSize(); ++i){
            useriLogati.addElement(useriInformati.get(i));
        }        

        jList1.setModel(useriLogati);
        jList2.setModel(useriInformati);

        for (ServerThread st : clients){
            if (st.getUser() != null){
                try {
                    st.shareEchipe();
                    UserDTO user = st.getUser();
                    useriLogati.removeElement(user);
                    useriInformati.addElement(user);
                    jList1.setModel(useriLogati);
                    jList2.setModel(useriInformati);
                } catch (Exception ex) {
                    Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void userInformat (UserDTO user) {
        boolean exists = false;
        for (int i = 0; i < useriInformati.size() && !exists; ++i) {
            if (useriInformati.get(i).getUsername().equals(user.getUsername())) {
                exists = true;
            }
        }

        if (exists) return;

        useriInformati.addElement(user);
        jList2.setModel(useriInformati);
    }

    public List<ActivitateDTO> getActivitati () {
        if (activitati == null) activitati = new ArrayList<>();
        return (List<ActivitateDTO>) activitati;
    }

    public List<JocDTO> getJocuri () {
        if (jocuri == null) jocuri = new ArrayList<>();
        return (List<JocDTO>) jocuri;
    }

    public List<Object> packData (List<ActivitateDTO> activitati, List<JocDTO> jocuri) {
        List<Object> sarcini = new ArrayList<>();

        for (ActivitateDTO a : activitati) {
            sarcini.add(a);
        }

        for (JocDTO j : jocuri) {
            sarcini.add(j);
        }

        Collections.sort(sarcini, new Comparator<Object> () {

            @Override
            public int compare(Object t, Object t1) {
                if (t instanceof ActivitateDTO && t1 instanceof JocDTO) {
                    return -1;
                } else if (t instanceof JocDTO && t1 instanceof ActivitateDTO) {
                    return 1;
                } else {
                    if (t instanceof ActivitateDTO) {
                        if (((ActivitateDTO) t).getPerioada().equals("seara")) {
                            return 1;
                        } else if (((ActivitateDTO) t).getPerioada().equals("amiaza") && ((ActivitateDTO) t1).getPerioada().equals("dimineata")){
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        if (((JocDTO) t).getPerioada().equals("seara")) {
                            return 1;
                        } else if (((JocDTO) t).getPerioada().equals("amiaza") && ((JocDTO) t1).getPerioada().equals("dimineata")){
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                }
            }
        });

        return sarcini;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel2.setText("Useri Logati");

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList1MouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jLabel3.setText("Receptionat Activitati");

        jList2.setModel(new DefaultListModel());
        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList2MouseEntered(evt);
            }
        });
        jScrollPane2.setViewportView(jList2);

        jButton2.setText("Inchide Serverul");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Status"));

        jButton1.setText("Verifica conexiuni");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(2, 2, 2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel5.setText("jLabel5");

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Verifica automat conexiunile");
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseEntered(evt);
            }
        });

        jMenu1.setText("Optiuni");

        jMenuItem1.setText("Dispozitive conectate");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem1MouseEntered(evt);
            }
        });
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Schimba portul");
        jMenuItem2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem2MouseEntered(evt);
            }
        });
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Adresa");
        jMenuItem3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem3MouseEntered(evt);
            }
        });
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("HotSpot");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenu2MouseEntered(evt);
            }
        });

        jMenuItem4.setText("jMenuItem4");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(88, 88, 88)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jCheckBox1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        ArrayList<Socket> sockets = new ArrayList<>();
        for (ServerThread st : clients){
            sockets.add(st.getSocket());
        }
        new DevicesFrame(sockets).setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        opresteServerul();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        opresteServerul();
    }//GEN-LAST:event_formWindowClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new StatusThread().start();
    }//GEN-LAST:event_jButton1ActionPerformed
    // aristotel - sociabilitatea umana
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        String s = JOptionPane.showInputDialog(this, "Introdu un nou port (1024 - 65535) :");
        try {
            int auxPort = Integer.parseInt(s);
            if (auxPort > 1024 && auxPort < 65535) {
                PORT = auxPort;
                SerializeController.getInstance().savePort(PORT);
                restartServer();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Portul nu inteplineste conditiile cerute.");
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        new AdressFrame(socket).setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        HelperFrame.getInstance().post(
                "Modulul Server",
                "In aceasta fereastra este reprezentat modulul server al aplicatiei. Acesta faciliteaza conectarea animatorilor ("
                        + "prin intermediul aplicatiei mobile) la aplicatia desktop, pentru a putea prelua programul actual si "
                        + "echipele seriei actuale. Cat timp aceasta fereastra este deschisa, orice animator va putea obtine informatiile "
                        + "mentionate mai sus.\n"
                        + "ATENTIE: acest modul consuma un numar de resurse considerabil. Evita sa folosesti o alta aplicatie costisitoare "
                        + "cat timp este deschis acest modul.");
    }//GEN-LAST:event_formMouseEntered

    private void jList1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseEntered
        HelperFrame.getInstance().post(
                "Utilizatori logati",
                "Aici vor fi afisati toti animatorii care sunt conectati la aplicatie. \n"
                        + "DE RETINUT: un animator se poate conecta numai daca acesta are cont. Un animator nu se va putea loga "
                        + "daca nu a fost inregistrat mai intai.\n"
                        + "RECOMANDAT: pentru o buna functionare a intregului sistem, este cat se poate de indicat ca numele animatorului "
                        + "sa ramana exact acelasi cu cel al utilizatorului asociat.");
    }//GEN-LAST:event_jList1MouseEntered

    private void jList2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseEntered
        HelperFrame.getInstance().post(
                "Utilizatori informati",
                "Este lista in care apar toti animatorii care au fost sau inca mai sunt conectati la aplicatia, si care au aflat programul de "
                        + "astazi, implicit ce au de facut.\n"
                        + "DE RETINUT: daca serverul va fi inchis, lista aceasta va fi resetata, deoarece se considera ca au aparut schimbari "
                        + "neinregistrate inca.");
    }//GEN-LAST:event_jList2MouseEntered

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        HelperFrame.getInstance().post(
                "Verificare",
                "Deoarece pot exista erori ale retelei sau ale aplicatiei mobile, serverul se poate sa nu fie notificat daca un client s-a deconectat."
                        + " Se recomanda a se folosi aceasta optiune pentru a se curata utilizatorii cu care a fost pierduta conexiunea."
                        + "\nDE RETINUT: verificand frecvent conexiunile, se previne functionarea lenta a aplicatiei, cauzata de valorile reziduale.");
    }//GEN-LAST:event_jButton1MouseEntered

    private void jCheckBox1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseEntered
        HelperFrame.getInstance().post(
                "Verificare autoamata",
                "Bifand aceasta optiune, serverul va face automat verificarile de rigoare ale conexiunilor.");
    }//GEN-LAST:event_jCheckBox1MouseEntered

    private void jMenuItem3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem3MouseEntered
        HelperFrame.getInstance().post(
                "Adresa serverului",
                "Este adresa socket(formata din IP si PORT) la care se gaseste serverul. Aceasta adresa trebuie comunicata"
                        + "animatorilor pentru a se putea conecta cu aplicatia mobila."
                        + "Gandeste-te ca Serverul e gazda, iar aplicatiile mobile care se conecteaza la server sunt prietenii gazdei. "
                        + "Cum pot pritenii sa vina in vizita la gazda daca acestia nu stiu la ce adresa locuieste acesta?");
    }//GEN-LAST:event_jMenuItem3MouseEntered

    private void jMenuItem2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MouseEntered
        HelperFrame.getInstance().post(
                "Portul serverului",
                "Portul este o adresa a procesului pornit de server."
                        + "Nu se recomanda schimbarea portului decat daca serverul nu poate porni.\n"
                        + "DE RETINUT: valoarea portului este cuprinsa intre 0 si 65535, dar nu se recomanda atribuirea unei valori din intervalul "
                        + "[0, 1024].");
    }//GEN-LAST:event_jMenuItem2MouseEntered

    private void jMenuItem1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MouseEntered
        HelperFrame.getInstance().post(
                "Dispozitive coenctate",
                "La server se pot conecta dispozitive fara a se loga cu un cont anume. Contul faciliteaza accesul la echipe si la planul de activitati."
                        + "Dispozitivele conectate nu vor aparea in fereastra serverului deoarece nu prezinta importanta, dar le poti vedea "
                        + "dand click pe aceasta optiune.");
    }//GEN-LAST:event_jMenuItem1MouseEntered

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            SerieActivaFrame.jButton7.setBackground(Color.GREEN);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_formWindowOpened

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        if (Main.isClosingApp()) return;
        jLabel5.setVisible(true);
        if (!IS_HOTSPOT_ON) {            
            // verificam compatibilitatea placii de retea cu optiunea HotSpot
            new AsyncTask(
                    new Event(){
                        @Override
                        public void doAction() {
                        //TODO: verifica posibilitatea pornirii hotspot ului
                            jLabel4.setText("Verific compatibilitatea hotspot");
                            try {
                                Process p = Runtime.getRuntime().exec("netsh wlan show drivers");
                                BufferedReader buf = new BufferedReader(
                                        new InputStreamReader(p.getInputStream())
                                );
                                p.waitFor();
                                
                                String line = "";
                                
                                do {
                                    line = buf.readLine();
                                } while (!line.contains("Hosted network supported"));
                                
                                if (line.contains("No")) {
                                    JOptionPane.showMessageDialog(ServerFrame.this, "Tehnologia HotSpot nu este suportata", "HotSpot", JOptionPane.ERROR_MESSAGE);
                                    jLabel4.setText("");
                                    return;
                                }
                                
                                    //TODO: seteaza hotspot
                                jLabel4.setText("Setez hotspot");
                                File f = new File("./lib/start_hotspot.bat");

                                try {
                                    String command = "cmd /c start " + f.getCanonicalPath();
                                    p = Runtime.getRuntime().exec(command);
                                    StreamGobbler output = new StreamGobbler(p.getInputStream());
                                    StreamGobbler error = new StreamGobbler(p.getErrorStream());
                                    output.start();
                                    error.start();
                                    p.waitFor();

                                    if (p.exitValue() != 0) throw new Exception("Eroare neasteptata la setarea hotspot");
                                } catch (Exception ex) {
                                    Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
                                    JOptionPane.showMessageDialog(ServerFrame.this, "A aparut o eroare la setarea HotSpotului", "HotSpot", JOptionPane.ERROR_MESSAGE);
                                    jLabel4.setText("");
                                    jLabel5.setVisible(false);
                                    return;
                                }

    //                            
                                jLabel4.setText("");
                                jLabel5.setVisible(false);
                                jMenuItem4.setText("Opreste hotspot");
                                IS_HOTSPOT_ON = true;
                                 
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(ServerFrame.this, "A aparut o eroare la pornirea HotSpotului", "HotSpot", JOptionPane.ERROR_MESSAGE);
                                jLabel4.setText("");
                                jLabel5.setVisible(false);
                                //return;
                            } 
                        }
                        
                    }
            ).execute();
        } else {
            new AsyncTask( 
                    new Event () {
                        @Override
                        public void doAction() {
                            jLabel5.setVisible(true);
                            jLabel4.setText("Se opreste hotspot");
                            
                            try {
                                File f = new File("./lib/stop_hotspot.bat");
                                String command = "cmd /c start " + f.getCanonicalPath();
                                Process p = Runtime.getRuntime().exec(command);
                                StreamGobbler output = new StreamGobbler(p.getInputStream());
                                StreamGobbler error = new StreamGobbler(p.getErrorStream());
                                output.start();
                                error.start();
                                p.waitFor();
                                
                                if (p.exitValue() != 0) throw new Exception ();
                            } catch (Exception e) {
                                Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, e);
                                JOptionPane.showMessageDialog(ServerFrame.this, "A aparut o eroare la oprirea HotSpotului", "HotSpot", JOptionPane.ERROR_MESSAGE);
                                jLabel4.setText("");
                                jLabel5.setVisible(false);
                                return;
                            }
                            
                            jLabel4.setText("");
                            jLabel5.setVisible(false);
                            IS_HOTSPOT_ON = false;
                            jMenuItem4.setText("Porneste hotspot");
                        }                        
                    }
            ).execute();
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenu2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseEntered
        HelperFrame.getInstance().post("Statie Hotspot", "In cazul inexistentei unei retele Wireless, folosind aceasta optiune, statia de pe care "
                + "ruleaza programul poate fi transformat intr-un router wireless, la care se pot conecta dispozitivele clent.");
    }//GEN-LAST:event_jMenu2MouseEntered

    private class StatusThread extends Thread {
        private static final int WAIT = 5000;
        @Override
        public void run() {
            jButton1.setEnabled(false);
            jLabel5.setVisible(true);
            jLabel4.setText("Se verifica conexiunile...");

            for (ServerThread st : clients) {
                st.checkForConnection();
            }

            try {
                Thread.sleep(WAIT);

                int i = 0;

                while (i < clients.size()) {
                    if (clients.get(i).getActiveThread()) {
                        i++;
                    } else {
                        clients.get(i).distroy();
                        clients.remove(i);
                    }
                }

                useriLogati.clear();

                for (ServerThread st : clients) {
                    useriLogati.addElement(st.getUser());
                }

                jList1.setModel(useriLogati);

            } catch (Exception e) {
                System.out.println("Eroare la verificarea conexiunii : " + e.getMessage());

            } finally {
                jLabel4.setText("");
                jButton1.setEnabled(true);
                jLabel5.setVisible(false);
            }
        }
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Object... objs) {
        ////
        //System.out.println("");
        try {
            if (objs != null && objs[0] instanceof UserDTO) {
                if ((boolean) objs[1]) {
                    useriLogati.addElement((UserDTO) objs[0]);
                    jList1.setModel(useriLogati);
                } else {
                    UserDTO u = (UserDTO) objs[0];
                    for (int i = 0; i < useriLogati.size(); ++i) {
                        try {
                            if (useriLogati.get(i).getId() == u.getId()){
                                useriLogati.removeElement(useriLogati.get(i));
                            }
                        } catch (Exception e) {}
                    }

                    jList1.setModel(useriLogati);
                }
            }
        } catch (Exception e) {
            System.out.println("Exceptie in metoda ServerFrame.update : " + e.getMessage());
        }
    }
}
