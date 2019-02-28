/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Magazin.gui.MagazinFrame;
import Magazin.service.MagazinController;
import costumSerialization.SerializableSerieActivaFrame;
import db.EchipaDB;
import db.SerieDB;
import db.UserDB;
import dialogs.DBDialog;
import dialogs.LoadingDialog;
import dialogs.PermissionFrame;
import dialogs.SetDayFrame;
import dialogs.SetOraFrame;
import dto.UserDTO;
import static gui.SerieActivaFrame.activitatiS;
import static gui.SerieActivaFrame.jocuriS;
import static gui.SerieActivaFrame.timer;
import java.awt.Desktop;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import main.AsyncTask;
import main.Event;
import server.ServerFrame;
import service.ControllerDB;
import service.SerializeController;

/**
 *
 * @author Alexandru
 */
public class MainFrame extends javax.swing.JFrame implements Serializable {
    private final static int DEFAULT_ZILE_PER_SERIE = 6;
    private final static String DEFAULT_DISPOSE_HOUR = "17:00";
    public static List<Frame> frames = null; 
    private DefaultComboBoxModel<SerieDB> model; 
    private UserDB user;
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
    private static SerieDB serieActiva;
    public static int zilePerSerie = DEFAULT_ZILE_PER_SERIE;
    public static String oraDezactivare = DEFAULT_DISPOSE_HOUR;
    public static boolean isLocked = false;
    public List<Frame> invisibleFrames;
    public static boolean raportViewed = false;
    private static MainFrame instance;
    
    /**
     * Creates new form MainFrame
     * @param user
     */
    public MainFrame(UserDB user) {
        super("Utilizator logat : " + user.getUsername());
        initComponents();
        
        this.user = user;
        
        model = new DefaultComboBoxModel<>();
        model.removeAllElements();
        jComboBox1.setModel(model);
        
        String [] aux = ControllerDB.getInstance().deserializeFrame();
        if (aux != null){
            try {
                zilePerSerie = Integer.parseInt(aux[0]);
            } catch (Exception e) {
                zilePerSerie = DEFAULT_ZILE_PER_SERIE;
            }
            oraDezactivare = aux[1];            
        }
        
        jMenuItem2.setText("Numar zile per serie" + " - " + zilePerSerie);
        jMenuItem3.setText("Ora dezactivare serie" + " - " + oraDezactivare);
        
        if (user.getAcces() < UserDTO.ACCES_ADMINISTRATOR) jMenuItem17.setVisible(false);
        
        
        refreshSerii();
        //SerializeController.getInstance().salveazaSerii(new ArrayList<SerieDB>());
        instance = this;
        
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    public UserDB getUser () {
        return user;
    }
    
    public static MainFrame getInstance () {
        return instance;
    }
    
    public void restartAppWithoutLogin () {
        jMenuItem7ActionPerformed(null);
    }
    
    public static void unlock () {
        for (Frame f : frames) {
            f.setVisible(true);
        }
        frames = null;
        isLocked = false;
    }
    
    private int getDays (Date d1, Date d2){
        long days = d2.getTime() - d1.getTime();
        return (int) TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS);
    }
    
    private void refreshSerii () {
        if (ControllerDB.getInstance().getSeriesNumber() != 0){
            try {
                int campDay = getDays(df.parse(ControllerDB.getInstance().getLastSerie().getDataInceput()),
                                      Calendar.getInstance().getTime()) + 1;

                List<SerieDB> serii = ControllerDB.getInstance().getAllSerii();

                if (campDay > MainFrame.zilePerSerie
                        || (campDay == MainFrame.zilePerSerie 
                        && oraDezactivare.compareToIgnoreCase(hourFormat.format(Calendar.getInstance().getTime())) < 0)){
                    jLabel2.setText("N/A");
                    jLabel4.setText("N/A");
                    jButton2.setEnabled(false);
                    HashMap<String, EchipaDB> colors = SerializeController.getInstance().deserializeazaCulori();
                    List<EchipaDB> echipe = ControllerDB.getInstance().getEchipeBySerie(ControllerDB.getInstance().getLastSerie());
                    
                    for (EchipaDB e : echipe) {
                        colors.put(e.getCuloareEchipa(), null);
                    }
                    SerializeController.getInstance().serializareCulori(colors);
                    serieActiva = null;
                    SerieActivaFrame.activitatiS = null;
                    SerieActivaFrame.jocuriS = null;
                    SerializeController.getInstance().serializaDistribuieProgramFrame(null);
                    SerializeController.getInstance().serializeAdaugaInformatiiFrame(null);
                    SerializeController.getInstance().serializeDistribuieEchipeFrame(null);
                    SerializeController.getInstance().serializeSerieActivaFrame(null);
                    SerializeController.getInstance().serializeazaPostareProgramFrame(null);
                    ControllerDB.getInstance().setLastSerie(null);
                    if (!ServerFrame.isSingletonNull()) {
                        ServerFrame.getInstance().opresteServerul();
                    }
                    
                    
                } else {
                    serieActiva = ControllerDB.getInstance().getLastSerie();  
                    jLabel2.setText(serieActiva.getNumarSerie() + "");
                    jLabel4.setText("Ziua " + campDay);
                    serii.remove(ControllerDB.getInstance().getLastSerie());                    
                    jButton2.setEnabled(true);
                    
                    if (campDay - 1 < 0) {
                        jLabel4.setText(((campDay - 1) == -1 ? "O zi ramasa" : (campDay - 1) * (-1) + " zile ramase") + (" (" + serieActiva.getDataInceput() + ")"));
                        if (user.getAcces() < UserDTO.ACCES_ADMINISTRATOR) 
                            jButton2.setEnabled(false); 
                        //TODO deblocheaza instructiunea de mai sus
                    }
                    
                    HashMap<String, EchipaDB> colors = SerializeController.getInstance().deserializeazaCulori();
                    List<EchipaDB> echipe = ControllerDB.getInstance().getEchipeBySerie(ControllerDB.getInstance().getLastSerie());
                    
                    for (EchipaDB e : echipe) {                       
                        colors.put(e.getCuloareEchipa(), e); 
                    }
                    SerializeController.getInstance().serializareCulori(colors);
                }

                model.removeAllElements();

                for (SerieDB s : serii){
                    model.addElement(s);
                }
                
                if (serii.isEmpty()){
                    jButton3.setEnabled(false);
                } else {
                    jButton3.setEnabled(true);
                }

                jComboBox1.setModel(model);
            } catch (ParseException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            jLabel2.setText("N/A");
            jLabel4.setText("N/A");
            jButton2.setEnabled(false);
            jButton3.setEnabled(false);
        }
    }
    
    public void changeDaysNumber (int val){
        zilePerSerie = val;    
        setVisible(false);
        ControllerDB.getInstance().serializeFrame(zilePerSerie, oraDezactivare);
        setVisible(true);
        jMenuItem2.setText("Numar zile per serie" + " - " + zilePerSerie);
        jMenuItem3.setText("Ora dezactivare serie" + " - " + oraDezactivare);
        refreshSerii();
    }
    
    public void changeHour (int h, int m){
        Calendar cal = Calendar.getInstance();
            
        cal.set(Calendar.HOUR_OF_DAY, h);
        cal.set(Calendar.MINUTE, m);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        oraDezactivare = hourFormat.format(cal.getTime());
        setVisible(false);
        ControllerDB.getInstance().serializeFrame(zilePerSerie, oraDezactivare);
        setVisible(true);
        jMenuItem2.setText("Numar zile per serie" + " - " + zilePerSerie);
        jMenuItem3.setText("Ora dezactivare serie" + " - " + oraDezactivare);
        refreshSerii();
    }
    
    public static SerieDB getSerieActiva () {
        return serieActiva;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jButton1.setText("Adauga Serie");
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

        jButton4.setText("Gestioneaza serii");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Serii"));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel1MouseEntered(evt);
            }
        });

        jLabel1.setText("Serie activa :");

        jLabel2.setText("Seria x");

        jButton2.setText("Deschide");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Arhiva :");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboBox1MouseEntered(evt);
            }
        });

        jButton3.setText("Deschide");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel4.setText("Ziua x");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator1)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(11, 11, 11)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addContainerGap())
        );

        jMenu4.setText("Date");

        jMenuItem10.setText("Gestioneaza date");
        jMenuItem10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem10MouseEntered(evt);
            }
        });
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenuItem9.setText("Salveaza datele actuale");
        jMenuItem9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem9MouseEntered(evt);
            }
        });
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuItem11.setText("Reseteaza baza de date");
        jMenuItem11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem11MouseEntered(evt);
            }
        });
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem11);

        jMenuItem17.setText("Panou de control ");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem17);

        jMenuBar1.add(jMenu4);

        jMenu2.setText("Utilizatori");

        jMenuItem8.setText("Gestioneaza utilizatori");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        jMenu6.setText("Magazin");

        jMenuItem18.setText("Deschide magazin");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem18);

        jMenuBar1.add(jMenu6);

        jMenu7.setText("Inchirieri");

        jMenuItem19.setText("Inchiriaza joc");
        jMenuItem19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem19MouseEntered(evt);
            }
        });
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem19);

        jMenuBar1.add(jMenu7);

        jMenu1.setText("Optiuni");

        jMenuItem2.setText("Numar zile per serie");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Ora dezactivare serie");
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

        jMenuItem1.setText("Gestioneaza culori");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem6.setText("Blocheaza aplicatia");
        jMenuItem6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem6MouseEntered(evt);
            }
        });
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem7.setText("Restarteaza aplicatia");
        jMenuItem7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem7MouseEntered(evt);
            }
        });
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem12.setText("Arhiveaza");
        jMenuItem12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem12MouseEntered(evt);
            }
        });
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem12);

        jMenuItem16.setText("Reseteaza intreaga aplicatie");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem16);

        jMenuBar1.add(jMenu1);

        jMenu5.setText("Ajutor");

        jMenuItem13.setText("Lanseaza indrumator");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem13);

        jMenuItem14.setText("Consola");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem14);

        jMenuItem15.setText("Ghidul utilizatorului");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem15);

        jMenuBar1.add(jMenu5);

        jMenu3.setText("Iesire");

        jMenuItem4.setText("Deconectare");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem5.setText("Iesire");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jCalendar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (serieActiva != null){
            JOptionPane.showMessageDialog(this, "Nu poti adauga serii noi cat timp exista o serie activa");
            return;
        }
        
        SerializeController.getInstance().setCanReadSerieActivaFrame(false);
        SerializeController.getInstance().serializeLastSerieActivaFrame(null);
        SerializeController.getInstance().serializeSerieActivaFrame(null);
        activitatiS = new ArrayList<>();
        jocuriS = new ArrayList<>();
        if (SerieActivaFrame.instance != null) {
            ServerFrame.getInstance().toBack();
            ServerFrame.getInstance().setPackage(activitatiS, jocuriS);
            ServerFrame.getInstance().shareActivities();
            ServerFrame.getInstance().setState(Frame.ICONIFIED);
            try { timer.setDataExpirare(null); } catch (Exception e) {}
        
        }
        new AdaugaSerieFrame().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        new SetDayFrame("Modifica perioada", "Introdu noua perioada (in zile) :", -1, this, zilePerSerie).setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        new SetOraFrame("Modifica ora", "Ora dezactivare serie :", this, oraDezactivare).setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        dispose();
        Frame [] myFrames = getFrames();
        
        for (Frame f : myFrames) {
            if (f.getClass() == ServerFrame.class) {
                if (!ServerFrame.isSingletonNull()) {
                    ServerFrame.getInstance().opresteServerul();
                    continue;
                }
            }
            f.dispose();
        }
        new LoginFrame().setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        dispose();
        System.exit(0);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        SerializableSerieActivaFrame ssaf = SerializeController.getInstance().deserializeLastSerieActivaFrame();
        if (SerializeController.getInstance().hasExpired(Calendar.getInstance().getTime()) && ssaf != null && !raportViewed) {
            SerieActivaFrame.vizualizeazaRaport(ssaf);
            raportViewed = true;
            return;
        }
        MainFrame.openFrame(new SerieActivaFrame(serieActiva), true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        refreshSerii();
    }//GEN-LAST:event_formWindowGainedFocus

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        ControllerDB.getInstance().serializeFrame(zilePerSerie, oraDezactivare);      
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        MainFrame.openFrame(new GestioneazaSeriiFrame(this), false);
    }//GEN-LAST:event_jButton4MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        final SerieDB selected = (SerieDB) jComboBox1.getSelectedItem();
        MainFrame.openFrame(new SerieTerminataFrame(selected), false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        new ColorsFrame(this, false).setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        frames = new ArrayList<>();
        
        for (Frame f : Frame.getFrames()) {
            if (f.isVisible()) {
                f.setVisible(false);
                frames.add(f);
            }
        }
        
        isLocked = true;
        LoginFrame login = new LoginFrame();
        login.lock(user);
        login.setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        Frame [] visibleFrames = Frame.getFrames();
        
        for (Frame f : visibleFrames) {
            f.dispose();
            if (f.getClass() == ServerFrame.class){
                ServerFrame.getInstance().opresteServerul();
            }
        }
        
        new MainFrame(user).setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

   
    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        MainFrame.openFrame(new GestioneazaUseriFrame(this, user), false);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        try {
            invisible();
            final LoadingDialog loading = new LoadingDialog(this, true);
            loading.setVisible(true);
            loading.setProgressText("Se salveaza baza de date...");
            new AsyncTask(new Event () {

                @Override
                public void doAction() {
                    ControllerDB.getInstance().downloadDB();
                }
            }, new Event() {

                @Override
                public void doAction() {
                    loading.setVisible(false);
                    visible();
                }
            }).execute();

        } catch (Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }             
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        DBDialog dialog = new DBDialog(this, true);
        dialog.setVisible(true);
        
        if (dialog.isChanged()) {
            dispose();
            ControllerDB.getInstance().disconnect();
            try {
                ControllerDB.getInstance().connectToDB(null);
                new MainFrame(user).setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Eroare la conectarea la baza de date. Va rugam sa reporniti aplicatia");
                System.exit(0);
            }
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        int input = JOptionPane.showConfirmDialog(this, "<html>In cazul in care baza de date actuala nu este salvata, <br>"
                + "datele din ea se vor pierde definitiv. Continui? </html>", "Atentionare", 
                JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION);
        
        
        if (input == JOptionPane.YES_OPTION) {
            PermissionFrame perm = new PermissionFrame(this, true, "Confirma resetarea bazei de date", UserDTO.ACCES_COORDONATOR);
            if (!perm.isApproved()) return;
            try {
                dispose();
                SerializeController.getInstance().salveazaSerii(null); // se sterg seriile arhivate
                SerializeController.getInstance().serializaDistribuieProgramFrame(null);
                SerializeController.getInstance().serializeAdaugaInformatiiFrame(null);
                SerializeController.getInstance().serializeDistribuieEchipeFrame(null);
                SerializeController.getInstance().serializeSerieActivaFrame(null);
                SerializeController.getInstance().serializeazaPostareProgramFrame(null);
                //SerializeController.getInstance().serializeFormulaFrames(null);
                ControllerDB.getInstance().setLastSerie(null);                
                SerieActivaFrame.activitatiS = null;
                SerieActivaFrame.jocuriS = null;
                serieActiva = null;
                SerieActivaFrame.setSerie(null);
                
                ControllerDB.getInstance().dropDB();
                ControllerDB.getInstance().disconnect();
                ControllerDB.getInstance().loadDefaultDB(null, StartFrame.DEFAULT_DB_PATH);
                ControllerDB.getInstance().connectToDB(null);
                new MainFrame(user).setVisible(true);
                ControllerDB.getInstance().adaugaUser(user.getUsername(), user.getParola(), user.getAcces());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Baza de date nu a putut fi resetata");
            }        
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        File f = new File("./" + StartFrame.NUME_FOLDER_ARHIVE);
        f.mkdir();
        JFileChooser chooser = new JFileChooser(); 
        chooser.setCurrentDirectory(new java.io.File("./" + StartFrame.NUME_FOLDER_ARHIVE));
        chooser.setDialogTitle("Selecteaza destinatia");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);        
        
        int input = JOptionPane.showConfirmDialog(this, "Salvati baza de date inainte?", "BackUp", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (input == JOptionPane.CANCEL_OPTION) return;
        if (input == JOptionPane.YES_OPTION) {
            try {
                //invisible();
                final LoadingDialog loading = new LoadingDialog(this, true);
                loading.setVisible(true);
                loading.setProgressText("Se salveaza baza de date...");
                new AsyncTask(new Event () {

                    @Override
                    public void doAction() {
                        ControllerDB.getInstance().downloadDB();
                    }
                }, new Event() {

                    @Override
                    public void doAction() {
                        loading.setVisible(false);
                        visible();
                    }
                }).execute();
                
            } catch (Exception ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
           try {
               setEnabled(false);
               invisible();
               int option;
               String time = new SimpleDateFormat("_ddMMyyyy_HHmm").format(Calendar.getInstance().getTime());
               String nume = "EcoAventuraServer" + time;
               nume = JOptionPane.showInputDialog(this, "Introdu un nume convenabil ", nume);
               String outPath = chooser.getSelectedFile().getCanonicalPath()+ File.separator +  nume + ".zip";
               
               File f1 = new File(outPath);
               if (f1.exists()) {
                   option = JOptionPane.showConfirmDialog(this, "Se pare ca exista deja un fisier cu numele selectat. Suprascrieti??", "", JOptionPane.YES_NO_OPTION);
                   if (option == JOptionPane.NO_OPTION) {
                       setEnabled(true);
                       visible();
                       return;
                   }
               }
               
               SerializeController.getInstance().zipApplication(outPath, new File(".").getCanonicalPath(), this, myEvent);
               
            } catch (Exception ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "A aparut o eroare la arhivare aplicatiei");
                
                visible();
            } finally {
               setEnabled(true);
            }
           
        } else {
            visible();
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        HelperFrame.getInstance().showFrame();
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jPanel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseEntered
        HelperFrame.getInstance().post(
                "Meniul seriilor",
                "Ofera calea de acces catre seria activa sau catre o serie finalizata si nearhivata.");
    }//GEN-LAST:event_jPanel1MouseEntered

    private void jComboBox1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox1MouseEntered
        HelperFrame.getInstance().post(
                "Meniu serii terminate",
                "Selecteaza o serie terminata ale carei informatii vrei sa le revezi.");
    }//GEN-LAST:event_jComboBox1MouseEntered

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        HelperFrame.getInstance().post(
                "Butonul de adaugat serie",
                "Seria va putea fi adaugata numai daca nu se suprapune peste una finalizata si daca nu exista nicio serie activa.");
    }//GEN-LAST:event_jButton1MouseEntered

    private void jMenuItem10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem10MouseEntered
        HelperFrame.getInstance().post(
                "Baze de date salvate",
                "Folosind aceasta optiune vei putea vizualiza configuratiile bazelor de date salvate. O baza de date cuprinde "
                        + "toate informatiile legate de seriile desfasurate, echipele acestora, activitatile intreprinse, animatori...adica tot."
                        + "Din fereastra lansata de aceasta optiune vei putea incarca o configuratie salvata anterior, sau vei putea face alte modificari.\n"
                        + "ATENTIE: asigura-te ca salvezi baza de date actuala inainte de a inarca o alta, folosind optiunea din acelasi meniu 'Salveaza datele actuale'."
                        + "Altfel, toate datele actuale vor fi pierdute.");
    }//GEN-LAST:event_jMenuItem10MouseEntered

    private void jMenuItem9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem9MouseEntered
        HelperFrame.getInstance().post(
                "Salveaza configuratia",
                "Salveaza baza de date actuala, cu toate informatiile din aceasta. Poti vedea fisierul salvat in meniul lansat de optiunea "
                        + "'Gestioneaza date'. Fisierul va avea denumirea data de data si ora la care a fost creat.");
    }//GEN-LAST:event_jMenuItem9MouseEntered

    private void jMenuItem11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem11MouseEntered
        HelperFrame.getInstance().post(
                "Resetare totala",
                "Aceasta optiune sterge in intregime datele actuale ale aplicatiei. Totul va fi luat de la zero.\n"
                        + "ATENTIE: asigura-te ca ai salvat configuratia actuala, folosind optiunea 'Salveaza date actual'. "
                        + "Altfel, toate datele actuale vor fi pierdute.");
    }//GEN-LAST:event_jMenuItem11MouseEntered

    private void jMenuItem6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem6MouseEntered
        HelperFrame.getInstance().post(
                "Seurizare aplicatie",
                "Aceasta optiune ofera facilitatea de a lasa aplicatia sa ruleze in fundal, fiind afisata doar fereastra de logare."
                        + "Astfel, este prevenita eventualitatea ca cineva sa modifice datele aplicatiei in cazul in care aceasta ramane nesupravegheata."
                        + "\nDE RETINUT: doar utilizatorul care a blocat aplicatia va putea s-o deblocheze.");
    }//GEN-LAST:event_jMenuItem6MouseEntered

    private void jMenuItem7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem7MouseEntered
        HelperFrame.getInstance().post(
                "Restart aplicatie",
                "In cazul unui comportament anormal al aplicatiei, se recomanda restartarea acesteia folosind aceasta optiune.");
    }//GEN-LAST:event_jMenuItem7MouseEntered

    private void jMenuItem12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem12MouseEntered
        HelperFrame.getInstance().post(
                "Arhiveaza aplicatia",
                "Aceasta optiune este importanta deoarece ofera aplicatiei facilitatea de a putea fi transportata. Dupa apasarea acestui buton, "
                        + "aplicatia, alaturi de toate datele acesteia vor fi incluse intr-o arhiva care poate fi transportata pe orice alta statie"
                        + " care ruleaza sistemul de operare Windows. \n"
                        + "INSTRUCTIUNI DE TRANSFERARE:\n"
                        + "1. Se salveaza baza de date actuala (meniul 'Date'->'Salveaza datele actuale');\n"
                        + "2. Se arhiveaza aplicatia (folosind aceasta optiune);\n"
                        + "3. Se muta arhiva creata pe noua statie;\n"
                        + "4. Se dezarhiveaza si se porneste aplicatia;\n"
                        + "5. Din meniul 'Date'->'Gestioneaza data' este incarcata baza de date salvata inainte de arhivare;\n"
                        + "Si este gata, aplicatia va arata exact ca inainte!");
    }//GEN-LAST:event_jMenuItem12MouseEntered

    private void jMenuItem3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem3MouseEntered
        HelperFrame.getInstance().post(
                "Dezactivare serie",
                "Reprezinta ora din ultima zi a seriei in care seria actuala este dezactivata, si se contorizeaza ca terminata.");
    }//GEN-LAST:event_jMenuItem3MouseEntered

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        ConsoleFrame.getInstance().setVisible(true);
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        if (Desktop.isDesktopSupported()) {
            try {
                File f = new File(StartFrame.USER_GUIDE_PATH);
                Desktop.getDesktop().open(f);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Not supported");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Not supported");
        }
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        int input = JOptionPane.showConfirmDialog(this, "<html>Resetarea apicatiei la setarile din fabrica va duce la pierderea tuturor datelor.<br> Sigur continui? </html>", "Resetare Totala", JOptionPane.YES_NO_OPTION);
        
        if (input == JOptionPane.NO_OPTION) return;
        
        input = JOptionPane.showConfirmDialog(this, "<html>Doresti sa salvezi configuratia actuala a aplicatiei inainte? </html>", "Salvare date", JOptionPane.YES_NO_OPTION);
        
        
        myEvent = new Event() {

            @Override
            public void doAction() {
                invisible();
        
                myDialog = new LoadingDialog(MainFrame.this, true);
                myDialog.setVisible(true);

                new AsyncTask(new Event () {

                    @Override
                    public void doAction() {
                        ((LoadingDialog) myDialog).setProgressText("Se sterge baza de date...");
                        ControllerDB.getInstance().dropDB();
                        ((LoadingDialog) myDialog).setProgressText("Se sterg fisierele aplicatiei...");
                        deleteFiles(new File("."));
                    }
                }, new Event() {

                    @Override
                    public void doAction() {
                        myDialog.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Aplicatia a fost readusa cu succes la setarile initiale!", "Proces complet", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                }).execute();
                myEvent = null;
            }            
        };
        
        if (input == JOptionPane.YES_OPTION) {
            jMenuItem12ActionPerformed(evt);
            return;
        }
        
        myEvent.doAction();
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        DBControlFrame db = new DBControlFrame();
        db.setVisible(true);
        db.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        final LoadingDialog loading = new LoadingDialog(this, false);
        loading.setProgressText("Se deschide magazinul...");
        loading.setVisible(true);
        
        new AsyncTask(
                new Event(){
                    @Override
                    public void doAction () {
                        MagazinController.getInstance();
                        new MagazinFrame().setVisible(true);
                    }
                }, 
                new Event() {
                    @Override
                    public void doAction () {
                        loading.dispose();
                    }
                }
        ).execute();
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        new InchiriereFrame().setVisible(true);
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem19MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem19MouseEntered
        HelperFrame.getInstance().post(
                "Tabel inchirieri", 
                "reprezinta o mini baza de date care indosariaza obiectele imprumutate de copii cu scop educativ sau recreativ, "
                        + "tinand evidenta inchirierilor si returnarilor acestora."
        );
    }//GEN-LAST:event_jMenuItem19MouseEntered
    
    Event myEvent;
    LoadingDialog myDialog;
    private void deleteFiles (File f) {
        ((LoadingDialog) myDialog).setProgressText(f.getPath());
        
        if (f.isFile()) {
            if (!isImportantFile(f.getName())) {
                f.delete();                
            }
            return;
        }
        
        if (f.isDirectory()) {
            if (f.getName().equals(StartFrame.NUME_FOLDER_ARHIVE)) return;
            if (f.getName().equals(StartFrame.NUME_FOLDER_BAZA_DE_DATE)) return;
            File [] toBeDeteled = f.listFiles();
            for (File myFile : toBeDeteled) {
                deleteFiles(myFile);
            }
            if (f.listFiles().length == 0) {
                f.delete();
            }
        }
    }
    
    private boolean isImportantFile (String s) {
        for (String s1 : StartFrame.resources) {
            if (s.equals(s1)) return true;
        }
        
        for (String s1 : StartFrame.database) {
            if (s.equals(s1)) return true;
        }
        
        for (String s1 : StartFrame.libs) {
            if (s.equals(s1)) return true;
        }
        
        for (String s1 : StartFrame.xampp) {
            if (s.equals(s1)) return true;
        }
        
        if (s.equals(StartFrame.NUME_GHID_UTILIZATOR)) return true;
        if (s.equals(StartFrame.NUME_EXECUTABIL)) return true;
        if (s.equals(StartFrame.LICENCE_PATH)) return true;
        if (s.equals(StartFrame.LICENCE_DATA_PATH)) return true;
        if (s.equals(SerializeController.FORMULAS_PATH)) return true;
        
        return false;
    }
    
    public void invisible () {
        invisibleFrames = new ArrayList<>();
        
        for (Frame f : Frame.getFrames()) {
            if (f.getClass() == ServerFrame.class){
                ServerFrame.getInstance().opresteServerul();
            }
            
//            if (f.getClass() == ZippingFrame.class) {
//                continue;
//            }
            
            if (f.isVisible()){
                f.setVisible(false);
                invisibleFrames.add(f);
            }
        }
    }
    
    public void visible () {
        for (Frame f : invisibleFrames) {
            f.setVisible(true);
        }
        invisibleFrames = null;
    }
    
    public void doneZipping (String path) {
        visible();
        JOptionPane.showMessageDialog(this, "Succes!");
        try {
            Desktop.getDesktop().open(new File(path).getParentFile());
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void restartApp (){
        Frame [] visibleFrames = Frame.getFrames();
        
        for (Frame f : visibleFrames) {
            f.dispose();
            if (f.getClass() == ServerFrame.class){
                ServerFrame.getInstance().opresteServerul();
            }
        }
        
        new LoginFrame().setVisible(true);
    }
    
    public static void openFrame (Frame frame, boolean closePrev) {
        Frame [] frames = Frame.getFrames();
        
        for (Frame f : frames) {
            if (f.getClass() == frame.getClass()) {
                f.dispose();
            }
        }
        frame.setVisible(true);
        frame.toFront();
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private transient com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
