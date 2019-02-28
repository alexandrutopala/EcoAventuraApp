/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import dialogs.LicenceDialog;
import dialogs.XamppDialog;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import service.ControllerDB;
import service.SerializeController;

/**
 *
 * @author Alexandru
 */
public class StartFrame extends javax.swing.JFrame {
    public final static String [] libs = new String [] {
        "AbsoluteLayout.jar",
        "commons-codec-1.10.jar",
        "commons-collections4-4.1.jar",
        "commons-logging-1.2.jar",
        "curvesapi-1.04.jar",
        "eclipselink.jar",
        "EcoAventuraLib.jar",
        "javax.persistence_2.1.0.v201304241213.jar",
        "jcalendar-1.4.jar",
        "junit-4.12.jar",
        "log4j-1.2.17.jar",
        "mysql-connector-java-5.1.23-bin.jar",
        "org.eclipse.persistence.jpa.jpql_2.5.2.v20140319-9ad6abd.jar",
        "poi-3.15.jar",
        "poi-examples-3.15.jar",
        "poi-excelant-3.15.jar",
        "poi-ooxml-3.15.jar",
        "poi-ooxml-schemas-3.15.jar",
        "poi-scratchpad-3.15.jar",
        "xmlbeans-2.6.0.jar",
        "ibatis-2.3.0.677.jar",
        "start_hotspot.bat",
        "stop_hotspot.bat",
        "glazedlists_java16-1.10.0.jar"
    };
    
    public final static String [] resources = new String [] {
        "arrow.png",
        "description.png",
        "close.png",
        "move.png",
        "ico.png",
        "question_mark.png",
        "eco.jpg",
        "loading.gif-c200",
        "exclamation-mark.png"
    };
    
    public final static String [] database = new String [] {
        "EcoAventuraDB.mwb",
        //"ecoaventuradb.sql",
        "script.sql",
        "magazinScript.sql",
        "MagazinDB.mwb"
        //"user.txt" // nu e neaparat necesar de fisierul userului
    };
    
    public final static String [] xampp = new String [] {
        "xampp-win32-7.0.2-1-VC14-installer.exe"
    };
    
    public final static List<String> folders = new ArrayList<>(); 
    // actions 
    private final static String CHECKING_FILES = "Se verifica : ";
    private final static String CREATING_FILES = "Se creeaza : ";
    public final static String DEFAULT_DB_PATH = "./db/script.sql";
    public final static String DEFAULT_MAGAZIN_DB_PATH = "./db/magazinScript.sql";
    public final static String XAMPP_PATH = "./xampp";
    public final static String MYSQL_START = "mysql_start.bat";
    public final static String MYSQL_STOP = "mysql_stop.bat";
    public final static String NUME_GHID_UTILIZATOR = "Ghidul Utilizatorului Standard.pdf";
    public final static String USER_GUIDE_PATH = "./" + NUME_GHID_UTILIZATOR;
    public final static String NUME_EXECUTABIL = "EcoAventura.jar";
    public final static String NUME_FOLDER_ARHIVE = "arhiv";
    public final static String NUME_FOLDER_BAZA_DE_DATE = "db";
    public final static String LICENCE_PATH = "licence.srz";
    public final static String LICENCE_DATA_PATH = "licence.txt";
    
    public static String UNDEFINED_CRIPT = "undefined";
    public static String CRIPT = UNDEFINED_CRIPT;
    
    
    public final static int LICENSE_ELEMENTS_COUNT = 4;
    private final static int START_POS = 0;
    private final static int END_POS = 3;
    private Thread checker;
    
    /**
     * Creates new form StartFrame
     */
    public StartFrame() {
        initComponents();
        
        folders.add("db");
        folders.add("lib");
        folders.add("obj");
        folders.add("res");
        folders.add("serii");
        folders.add("xampp");
        folders.add("Logs");
        folders.add("EcoAventura.jar");
        folders.add("Ghidul Utilizatorului Standard.pdf");
        
        
        jPanel1.setBackground(Color.WHITE);
        jLabel2.setBackground(Color.WHITE);
        jLabel3.setBackground(Color.WHITE);
        jLabel4.setBackground(Color.WHITE);
        
        jLabel1.setText("");
        jLabel2.setText("");
        jLabel3.setText("");
        jLabel3.setMinimumSize(new Dimension(291, 23));
        jLabel3.setPreferredSize(new Dimension(291, 23));
        jLabel3.setMaximumSize(new Dimension(291, 23));
        
        // set icons
        ImageIcon ico = new ImageIcon("./res/eco.jpg");
        Image img1 = ico.getImage();
        img1 = img1.getScaledInstance(336, 176, Image.SCALE_SMOOTH);
        jLabel1.setIcon(new ImageIcon(img1));
        
        ImageIcon icon = new ImageIcon("./res/loading.gif-c200");
        Image img = icon.getImage();
        img = img.getScaledInstance(15, 15, Image.SCALE_FAST);
        jLabel2.setIcon(new ImageIcon(img));
        
        //ConsoleFrame.getInstance().setVisible(true);
        
        checker = new Checker();
        checker.start();
        
        setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);       
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(java.awt.Color.white);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("jLabel5");

        jLabel2.setText("jLabel6");

        jLabel3.setText("jLabel7");
        jLabel3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLabel3PropertyChange(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Copyright © 2016 Topala Alexandru-Nicolae. All Rights Reserved.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
    }//GEN-LAST:event_formWindowClosing

    private void jLabel3PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLabel3PropertyChange
        
    }//GEN-LAST:event_jLabel3PropertyChange
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    public static boolean checkCriptedLicence(String [] data, final String cript) {
        //return true;
        try {
            //            System.out.println("Digesting: \n" + data[0].toLowerCase() + "\n" + data[1].toLowerCase() + "\n" + data[2].toLowerCase() + "\n" + data[3].toLowerCase());
            
            byte[] data1 = data[0].toLowerCase().getBytes("UTF-8");
            byte[] data2 = data[1].toLowerCase().getBytes("UTF-8");
            byte[] data3 = data[2].toLowerCase().getBytes("UTF-8");
            byte[] data4 = data[3].toLowerCase().getBytes("UTF-8");
            
            //             System.out.println("Digested: \n" + data1 + "\n" + data2 + "\n" + data3 + "\n" + data4);
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            String d1 = new String(md.digest(data1));
            String d2 = new String(md.digest(data2));
            String d3 = new String(md.digest(data3));
            String d4 = new String(md.digest(data4));
            
            String newCript = d1 + //.substring(START_POS, END_POS) + 
                    d3 + //.substring(START_POS, END_POS) + 
                    d2 + //.substring(START_POS, END_POS) + 
                    d4 + //.substring(START_POS, END_POS);
                    d3.substring(START_POS, END_POS) + 
                    d1.substring(START_POS, END_POS) + 
                    d2.substring(START_POS, END_POS) + 
                    d4.substring(START_POS, END_POS);
            byte [] data5 = newCript.getBytes("UTF-8");
            //String d5 = md.digest(data5).toString();
            String d5 = new BigInteger(1, data5).toString(16);
            //            System.out.println(d5);
            //            System.out.println("Cript from data: " + d5);
            //            System.out.println("Cript from file: " + cript);
            
            if (cript.equals(d5)){
                CRIPT = cript;
                return true;
            }
            return false;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean saveData (String data[]) {
        FileOutputStream fout;
        PrintWriter writer;
        try {
            fout = new FileOutputStream("./" + LICENCE_DATA_PATH);
            writer = new PrintWriter(fout);
            
            writer.println(data[0]);
            writer.flush();
            writer.println(data[1]);
            writer.flush();
            writer.println(data[2]);
            writer.flush();
            writer.println(data[3]);
            writer.flush();
            
            writer.close();
            fout.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void checkLicence () {
        File f = new File("./" + LICENCE_PATH);
        File f2 = new File("./" + LICENCE_DATA_PATH);
        
        if (!f.exists()) {
            JOptionPane.showMessageDialog(this, "Programul nu este licentiat. Te rog sa contactezi administratorul aplicatiei pentru mai multe detalii.", "Licenta inexistenta", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return;
        }
        
        if (!f.isHidden()) {
            try { Files.setAttribute(f.toPath(), "dos:hidden", true); } catch (Exception e) {}
        }
        
        if (!f2.exists()) {
            LicenceDialog dialog = new LicenceDialog(this, true, new String[LICENSE_ELEMENTS_COUNT]);
            dialog.setVisible(true);
            
            if (dialog.isLicenced()) return;
            //TODO: daca userul s-a licentia in dialogul anterior, stari peste, daca nu, inchide aplicatia
        }
        
        FileInputStream input;
        InputStreamReader in;
        BufferedReader buf;
        
        FileInputStream input2;
        InputStreamReader in2;
        BufferedReader buf2;
        
        try {
            input = new FileInputStream(f2);
            in = new InputStreamReader(input);
            buf = new BufferedReader(in);
            String [] dataSet = new String[LICENSE_ELEMENTS_COUNT];
            dataSet[0] = buf.readLine();
            dataSet[1] = buf.readLine();
            dataSet[2] = buf.readLine();
            dataSet[3] = buf.readLine();
            
            buf.close();
            in.close();
            input.close();
            
            input2 = new FileInputStream(f);
//            objInput = new ObjectInputStream(input2);            
//            
//            String cript = (String) objInput.readObject();
//            
//            objInput.close();
//            input2.close();
            in2 = new InputStreamReader(input2);
            buf2 = new BufferedReader(in2);
            
            String cript = buf2.readLine();
            
            buf2.close();
            in2.close();
            input2.close();
            
            if (!checkCriptedLicence(dataSet, cript)) {
                if (JOptionPane.showConfirmDialog(this, "<html>Licenta existenta este corupta.<br>Reintroduci datele de licentiere?</html>", "Licenta corupta", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    LicenceDialog dialog = new LicenceDialog(this, true, dataSet);
                    dialog.setVisible(true);
                    
                    if (dialog.isLicenced()) {
                        return;
                    } else {
                        System.exit(0);
                    }
                } else {
                    System.exit(0);
                }
            } 
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "<html>O eroare a survenit in timpul verificarii licentei. Te rog sa contactezi administratorul aplicatiei.<br>Mesaj:" + e.getLocalizedMessage() + " </html>", "Licenta inexistenta", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return;
        }
        
    }
    
    public final class Checker extends Thread {
        private final static int WAIT = 50;
        boolean canStart = false;
        @Override
        public void run() {
            try { Thread.sleep(1000); } catch (Exception e) {}
            
            try {
                jLabel3.setText("Se verifica licenta");
                //checkLicence();
                CRIPT = "147674e280a07be2809d4cc3bac3916622c3afc393c397c2b724c2b60a45c38a47c38ec393e2809ec2b8356d6bcb9cc3bb66c3b03f5de284a2c3ac4631c5bdc3b643c5a1c2b2c3bbcb9c6f7cc692c2a51105c2b1c391c5b8c5b8c3a538053a6646c2a14d5cc2b60a451476743f5de284a2c2a51105";
                
                File curDir = new File(".");

                jLabel3.setText(CHECKING_FILES + curDir.getPath());

                // se verifica librariile

                File libDir = new File("./lib");

                jLabel3.setText(CHECKING_FILES + libDir.getPath());

                String [] files = libDir.list();

                for (int i = 0; i < libs.length; ++i) {
                    try { Thread.sleep(WAIT); } catch (Exception e) {}                
                    jLabel3.setText(CHECKING_FILES + libDir.getPath() + "\\" + libs[i]);
                    boolean exists = false;
                    for (int j = 0; j < files.length && !exists; ++j) {
                        if (libs[i].equals(files[j])) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        JOptionPane.showMessageDialog(StartFrame.this, "Eroare : " + libs[i] + " lipseste", "Fisier inexistent", JOptionPane.ERROR_MESSAGE);
                        StartFrame.this.dispose();
                        throw new Exception ("Fisier inexistent");
                    }
                }

                // verificam folderul obiectelor serializabile 

                File objDir = new File("./obj");

                jLabel3.setText(CHECKING_FILES + objDir.getPath());

                try { Thread.sleep(WAIT); } catch (Exception e) {}  

                if (!objDir.exists()) {
                    try {
                        try { Thread.sleep(WAIT); } catch (Exception e) {}  
                        jLabel3.setText(CREATING_FILES + objDir.getPath());
                        if (!objDir.mkdir()) {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(StartFrame.this, "Eroare : " + objDir.getPath() + " nu poate fi scris.", "Acces restrictionat", JOptionPane.ERROR_MESSAGE);
                        StartFrame.this.dispose();
                        throw new Exception ("Fisier inexistent");
                    }
                }

                // verificam folderul xampp

                File xamppDir = new File("./xampp");

                jLabel3.setText(CHECKING_FILES + xamppDir.getPath());

                String [] files1 = xamppDir.list();

                for (int i = 0; i < xampp.length; ++i) {
                    try { Thread.sleep(WAIT); } catch (Exception e) {}                
                    jLabel3.setText(CHECKING_FILES + xamppDir.getPath() + "\\" + xampp[i]);
                    boolean exists = false;
                    for (int j = 0; j < files1.length && !exists; ++j) {
                        if (xampp[i].equals(files1[j])) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        JOptionPane.showMessageDialog(StartFrame.this, "Eroare : " + xampp[i] + " lipseste", "Fisier inexistent", JOptionPane.ERROR_MESSAGE);
                        StartFrame.this.dispose();
                        throw new Exception ("Fisier inexistent");
                    }
                }

                // verificam folderul seriilor

                File seriiDir = new File("./serii");

                jLabel3.setText(CHECKING_FILES + seriiDir.getPath());

                try { Thread.sleep(WAIT); } catch (Exception e) {}  

                if (!seriiDir.exists()) {
                    try {
                        try { Thread.sleep(WAIT); } catch (Exception e) {}  
                        jLabel3.setText(CREATING_FILES + seriiDir.getPath());
                        if (!seriiDir.mkdir()) {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(StartFrame.this, "Eroare : " + seriiDir.getPath() + " nu poate fi scris.", "Acces restrictionat", JOptionPane.ERROR_MESSAGE);
                        StartFrame.this.dispose();
                        throw new Exception ("Fisier inexistent");
                    }
                }

                // verificam resursele

                File resDir = new File ("./res");
                jLabel3.setText(CHECKING_FILES + resDir.getPath());

                if (resDir.exists()) {
                    String res [] = resDir.list();

                    for (int i = 0; i < resources.length; ++i) {
                        boolean exists = false;
                        jLabel3.setText(CHECKING_FILES + resDir.getPath() + "\\" + resources[i]);
                        for (int j = 0; j < res.length && !exists; ++j) {
                            if (resources[i].equals(res[j])) {
                                exists = true;
                            }
                        }

                        if (!exists) {
                            JOptionPane.showMessageDialog(StartFrame.this, "Eroare : " + resources[i] + " lipseste", "Fisier inexistent", JOptionPane.ERROR_MESSAGE);
                            StartFrame.this.dispose();
                            throw new Exception("Fisier inexistent");
                        }
                    }
                }

                // verificam folderul db

                File dbDir = new File("./db");
                jLabel3.setText(CHECKING_FILES + dbDir.getPath());

                if (dbDir.exists()) {
                    String db [] = dbDir.list();

                    for (int i = 0; i < database.length; ++i) {
                        boolean exists = false;
                        jLabel3.setText(CHECKING_FILES + dbDir.getPath() + "\\" + database[i]);
                        for (int j = 0; j < db.length && !exists; ++j) {
                            if (database[i].equals(db[j])) {
                                exists = true;
                            }
                        }

                        if (!exists) {
                            JOptionPane.showMessageDialog(StartFrame.this, "Eroare : " + database[i] + " lipseste", "Fisier inexistent", JOptionPane.ERROR_MESSAGE);
                            StartFrame.this.dispose();
                            throw new Exception ("Fisier inexistent");
                        }
                    }
                }


                // verificam conexiunea cu baza de date

                jLabel3.setText("Verificare conexiune la baza de date...");
                String username;
                String password;
                String port;             
                

                String [] date = deserializeaza();

                if (date == null) {
                    username = JOptionPane.showInputDialog(StartFrame.this, "Username : ", "Conectare la baza de date", JOptionPane.QUESTION_MESSAGE);
                    password = JOptionPane.showInputDialog(StartFrame.this, "Parola : ", "Conectare la baza de date", JOptionPane.QUESTION_MESSAGE);
                    port = JOptionPane.showInputDialog(StartFrame.this, "Port : ", "Conectare la baza de date", JOptionPane.QUESTION_MESSAGE);
                    serializeaza(username, password, port);
                } else if (date[0].equals("") || date[2].equals("")) {
                    username = JOptionPane.showInputDialog(StartFrame.this, "Username : ", "Conectare la baza de date", JOptionPane.QUESTION_MESSAGE);
                    password = JOptionPane.showInputDialog(StartFrame.this, "Parola : ", "Conectare la baza de date", JOptionPane.QUESTION_MESSAGE);
                    port = JOptionPane.showInputDialog(StartFrame.this, "Port : ", "Conectare la baza de date", JOptionPane.QUESTION_MESSAGE);
                    serializeaza(username, password, port);
                } else {
                    username = date[0];
                    password = date[1];
                    port = date[2];
                }
                Connection conn = null;

//                jLabel3.setText("Starting MySql driver...");
//                if (startMySqlDriver(StartFrame.this)) {
//                    jLabel3.setText("MySql driver started successfully");
//                } else {
//                    dispose();
//                    throw new Exception("MySql could not start");
//                }
                
                
                Class.forName("com.mysql.jdbc.Driver"); //Register JDBC Driver

                System.out.println("Creating a connection...");
                checkXampp(StartFrame.this);
                try {
                    Thread.sleep(500);
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:" + port, username, password); //Open a connection
                } catch (CommunicationsException e) {
                    jLabel3.setText("Se reincearca legatura...");
                    new Thread ( new Runnable() {
                            @Override
                            public void run() {
                                startXampp(StartFrame.this);
                            }
                        }).start();
                    
                        for (int i = 0; i < 3 && conn == null; ++i) {
                            jLabel3.setText("Incercarea " + (i+1));
                            DriverManager.setLoginTimeout(10);
                            try {
                                Thread.sleep(1000);
                                conn = DriverManager.getConnection("jdbc:mysql://localhost:" + port, username, password); //Open a connection
                            } catch (CommunicationsException ex) { }
                        }                    
                }
                
                if (conn == null) {
                    throw new Exception ("Conexiune nula");
                }
                
                ControllerDB.getInstance().setSqlConnection(conn);
                ResultSet catalog = conn.getMetaData().getCatalogs();

                boolean exists = false;
                boolean exists2 = false;
                while (catalog.next() && (!exists || !exists2)) {
                    if (catalog.getString(1).equalsIgnoreCase("ecoaventuradb")) {
                        exists = true;
                    }
                    if (catalog.getString(1).equalsIgnoreCase("magazindb")){
                        exists2 = true;
                    }
                }

                if (!exists) {
                    jLabel3.setText("Se creeaza baza de date...");
                    // o cream
                    ControllerDB.getInstance().loadDefaultDB(null, DEFAULT_DB_PATH);
                    
                    //jLabel3.setText("Se conecteaza...");
                } 
                
                if (!exists2) {
                    jLabel3.setText("Se creeaza baza de date...");
                    
                    ControllerDB.getInstance().loadMagazinDB(null, DEFAULT_MAGAZIN_DB_PATH);
                    
                    jLabel3.setText("Se conecteaza...");
                }

                Map properties = new HashMap();
                properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/ecoaventuradb?zeroDateTimeBehavior=convertToNull");
                properties.put("javax.persistence.jdbc.password", password);
                properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
                properties.put("javax.persistence.jdbc.user", username);
                boolean res = ControllerDB.getInstance().connectToDB(properties);

                if (!res){
                    throw new Exception();
                }

                jLabel3.setText("Conectat!");
                try { Thread.sleep(1000); } catch (Exception e) {}
                dispose();
                
                new LoginFrame().setVisible(true);
                canStart = true;

            } catch (Exception e) {
                JOptionPane.showMessageDialog(StartFrame.this, "Eroare : " + " Conexiune esuata la baza de date", "Connection weak", JOptionPane.ERROR_MESSAGE);
                StartFrame.this.dispose();
                //showDialog();
                showControlPanel();
            }
            
//            if (!canStart) {
//                System.exit(0);
//            }
        }
        
    }
         
    public void showControlPanel () {
        if (JOptionPane.showConfirmDialog(null, "Doriti sa deschideti panoul de control al bazei de date?", "Setari", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION){
            System.exit(0);
            return;
        }
        
        new DBControlFrame().setVisible(true);
    }
    
    @Deprecated /* use showControlPanel instead*/
    public void showDialog () {
        if (JOptionPane.showConfirmDialog(null, "Doriti sa modificati setarile de conectare la baza de date?", "Setari", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION){
            return;
        }
        String username;
        String password;
        String port;
        username = JOptionPane.showInputDialog(StartFrame.this, "Username : ", "Conectare la baza de date", JOptionPane.QUESTION_MESSAGE);
        password = JOptionPane.showInputDialog(StartFrame.this, "Parola : ", "Conectare la baza de date", JOptionPane.QUESTION_MESSAGE);
        port = JOptionPane.showInputDialog(StartFrame.this, "Port : ", "Conectare la baza de date", JOptionPane.QUESTION_MESSAGE);
        serializeaza(username, password, port);
    }
    
    public static  String [] deserializeaza () {
        BufferedReader bf;
        InputStreamReader reader;
        FileInputStream input;
        
        try {    
            input = new FileInputStream("./db/user.txt");
            reader = new InputStreamReader (input);
            bf = new BufferedReader(reader);
            
            String [] s = bf.readLine().split(" ");
            
            bf.close();
            reader.close();
            input.close();
            return s;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            
        }
        return null;
    }
    
    public static boolean serializeaza (String username, String password, String port) {
        OutputStreamWriter out;
        FileOutputStream fout;
        
        try {
            fout = new FileOutputStream(new File("./db/user.txt"));
            out = new OutputStreamWriter(fout);
            
            out.write(username + " ");
            out.write(password + " ");
            out.write(port);
            out.close();
            fout.close();
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {}
        return false;
    }
    
    public static boolean checkXampp (Frame frame) {
        try {
           String installedXamppPath = SerializeController.getInstance().getXamppPath();
           File xampp = new File(installedXamppPath + File.separator + MYSQL_START);
           
           if (!xampp.exists()) {
               // instalam
               int input = JOptionPane.showConfirmDialog(frame, "Configurati acum serverul MySQL?", "Configurare", 
                       JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
               
               if (input == JOptionPane.YES_OPTION){
                   XamppDialog dialog = new XamppDialog(frame, true);
                   if (dialog.isCanRun()) {
                       xampp = new File(SerializeController.getInstance().getXamppPath() + File.separator + MYSQL_START);
                   } else {
                       JOptionPane.showMessageDialog(frame, "Serverul MySQL nu a putut fi pornit", "MySQl Error", JOptionPane.ERROR_MESSAGE);
                       System.exit(0);
                   }
               } else {
                   System.exit(0);
               }
           }
           return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean startXampp (Frame frame) {
        try {
           String installedXamppPath = SerializeController.getInstance().getXamppPath();
           File xampp = new File(installedXamppPath + File.separator + MYSQL_START);
           
           if (!xampp.exists()) {
               // instalam
               int input = JOptionPane.showConfirmDialog(frame, "Configurati acum serverul MySQL?", "Configurare", 
                       JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
               
               if (input == JOptionPane.YES_OPTION){
                   XamppDialog dialog = new XamppDialog(frame, true);
                   if (dialog.isCanRun()) {
                       xampp = new File(SerializeController.getInstance().getXamppPath() + File.separator + MYSQL_START);
                   } else {
                       JOptionPane.showMessageDialog(frame, "Serverul MySQL nu a putut fi pornit", "MySQl Error", JOptionPane.ERROR_MESSAGE);
                       System.exit(0);
                   }
               } else {
                   System.exit(0);
               }
           }
           
           Process p = Runtime.getRuntime().exec(xampp.getCanonicalPath());
           p.waitFor();
           return true;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
    
    public static synchronized boolean stopXampp () {
        try {
           String installedXamppPath = SerializeController.getInstance().getXamppPath();
           File xampp = new File(installedXamppPath + File.separator + MYSQL_STOP);
           Process p = Runtime.getRuntime().exec(xampp.getCanonicalPath());
           return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public static boolean intallXampp () {
        try {
            File f = new File (XAMPP_PATH + File.separator + xampp[0]);
            Runtime.getRuntime().exec("cmd /c " + f.getCanonicalPath());
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "XAMPP nu a putut fi instalat", "Eroare de instalare", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
