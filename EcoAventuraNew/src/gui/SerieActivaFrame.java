/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import costumSerialization.SerializableSerieActivaFrame;
import db.SerieDB;
import dialogs.PermissionFrame;
import dialogs.SetDateAndTimeDialog;
import dialogs.TeamSelectorDialog;
import dto.ActivitateDTO;
import dto.JocDTO;
import dto.UserDTO;
import static gui.MainFrame.raportViewed;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import main.AsyncTask;
import main.Event;
import server.ServerFrame;
import service.ControllerDB;
import service.SerializeController;
import service.Timer;

/**
 *
 * @author Alexandru
 */
public class SerieActivaFrame extends javax.swing.JFrame {
    private final static int STANDARD_WIDTH = 80;
    private final static int STANDARD_BUTTON_WIDTH = 30;
    private final static int STANDARD_BUTTON_HEIGHT = 30;
    
    private static SerieDB serie;
    private static DefaultListModel<ActivitateDTO> activitatiDimineata;
    private static DefaultListModel<ActivitateDTO> activitatiAmiaza;
    private static DefaultListModel<ActivitateDTO> activitatiSeara;
    private static DefaultListModel<JocDTO> jocuriDimineata;
    private static DefaultListModel<JocDTO> jocuriAmiaza;
    private static DefaultListModel<JocDTO> jocuriSeara;
    public static List<ActivitateDTO> activitatiS;
    public static List<JocDTO> jocuriS;
    public static ImageIcon exclamation = new ImageIcon("res/exclamation-mark.png");
    public static SerieActivaFrame instance;
    public static Timer timer;
    public static Thread timerThread;
    public static boolean shareOnStartUp = false;
    
    
    private int selectedIndex1;
    private boolean needSelect1;
    private int selectedIndex2;
    private boolean needSelect2;
    private int selectedIndex3;
    private boolean needSelect3;
    private int selectedIndex4;
    private boolean needSelect4;
    private int selectedIndex5;
    private boolean needSelect5;
    private int selectedIndex6;
    private boolean needSelect6;
    
    private static int id = 0;
    
    private final ImageIcon ICON;
   // private 
    /**
     * Creates new form SerieActivaFrame
     * @param serie
     */
    public SerieActivaFrame(SerieDB serie) {
        super ("Serie activa : Seria " + serie.getNumarSerie());
        initComponents();
        
        ImageIcon icon = new ImageIcon("./res/loading.gif-c200");
        Image img = icon.getImage();
        img = img.getScaledInstance(15, 15, Image.SCALE_FAST);
        this.ICON = new ImageIcon(img);
        
        this.serie = serie;
        instance = this;
        
        if (!(timer != null && timer.isRunning())) {
            timer = new Timer(jLabel8, jLabel9, this);
            timerThread = new Thread(timer);
        }
        
        timer.setTimpRamasLabel(jLabel9);
        timer.setDataExpirareLabel(jLabel8);
        
        if (!timer.hasExpired()) {
            SerializeController.getInstance().serializeSerieActivaFrame(
                    SerializeController.getInstance().deserializeLastSerieActivaFrame()
            );
        }
        
        timer.refresh();
        
        //exclamation = new ImageIcon("res/exclamation-mark.png");
        //exclamation.setImage(exclamation.getImage().getScaledInstance(jButton15.getWidth(), jButton15.getHeight(), Image.SCALE_SMOOTH));
        
        SerializableSerieActivaFrame ssaf = SerializeController.getInstance().deserializeSerieActivaFrame();
        if (ssaf != null && SerializeController.getInstance().isCanReadSerieActivaFrame()){            
            activitatiDimineata = ssaf.getActivitatiDimineata();
            activitatiAmiaza = ssaf.getActivitatiAmiaza();
            activitatiSeara = ssaf.getActivitatiSeara();
            jocuriDimineata = ssaf.getJocuriDimineata();
            jocuriAmiaza = ssaf.getJocuriAmiaza();
            jocuriSeara = ssaf.getJocuriSeara();
            jocuriS = ssaf.getJocuriS();
            activitatiS = ssaf.getActivitatiS();
            if (ssaf.isWereActivitatiReordered()) {
                jLabel4.setIcon(exclamation);
                jLabel4.setToolTipText("<html>Ordinea activitatilor a fost modificata.<br>Click aici pentru retrimiterea programului.</html>");
            }
            
            if (ssaf.isWereJocuriReordered()) {
                jLabel5.setIcon(exclamation);
                jLabel5.setToolTipText("<html>Ordinea jocurilor a fost modificata.<br>Click aici pentru retrimiterea programului.</html>");
            }
        } else {
            activitatiDimineata = new DefaultListModel<>();
            activitatiAmiaza = new DefaultListModel<>();
            activitatiSeara = new DefaultListModel<>();
            jocuriDimineata = new DefaultListModel<>();
            jocuriAmiaza = new DefaultListModel<>();
            jocuriSeara = new DefaultListModel<>();
            SerializeController.getInstance().setCanReadSerieActivaFrame(true);
         }
        
        
        
        jList1.setModel(activitatiDimineata);
        jList2.setModel(activitatiAmiaza);
        jList3.setModel(activitatiSeara);
        jList4.setModel(jocuriDimineata);
        jList5.setModel(jocuriAmiaza);
        jList6.setModel(jocuriSeara);
        
        jButton1.setText("<html> Posteaza<br>Program</html>");
        jButton2.setText("<html> Calculeaza<br>Clasament</html>");
        jButton3.setText("<html> Gestioneaza<br>Activitati</html>");
        jButton4.setText("<html> Gestioneaza <br>Echipele</html>");
        jButton5.setText("<html> Gestioneaza <br>Animatori</html>");
        jButton6.setText("<html> Program <br>Animatori</html>");
        jButton7.setText("<html> Porneste <br>Serverul</html>");
        jButton8.setText("<html> Program <br> Echipe</html>");
        jButton9.setText("<html> Cauta <br>Membru </html>");
        jButton16.setText("<html>Seteaza<br>data<br>expirare</html>");
               
        jList1.setFixedCellWidth(STANDARD_WIDTH);
        jList2.setFixedCellWidth(STANDARD_WIDTH);
        jList3.setFixedCellWidth(STANDARD_WIDTH);
        jList4.setFixedCellWidth(STANDARD_WIDTH);
        jList5.setFixedCellWidth(STANDARD_WIDTH);
        jList6.setFixedCellWidth(STANDARD_WIDTH);
        
        
        if (!timer.isRunning()) {
            timerThread.start();
        }
        
        if (shareOnStartUp) {            
            ServerFrame.getInstance().shareActivities();
            ServerFrame.getInstance().toBack();
            shareOnStartUp = false;
        }
        
        jLabel10.setText("ID PROGRAM: " + SerializeController.getInstance().getProgramActivitatiID());
        
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private static void cleanReorderNotification () {
        jLabel4.setIcon(null);
        jLabel4.setToolTipText(null);
        jLabel5.setIcon(null);
        jLabel5.setToolTipText(null);
    }
    
    @SuppressWarnings("empty-statement")
    public static void posteazaProgram (List<ActivitateDTO> activitati, List<JocDTO> jocuri, boolean order){
        activitatiS = activitati;
        jocuriS = jocuri;
        
        cleanReorderNotification();
        
        activitatiDimineata.clear();
        activitatiAmiaza.clear();
        activitatiSeara.clear();
        jocuriDimineata.clear();
        jocuriAmiaza.clear();
        jocuriSeara.clear();
        
        for (ActivitateDTO a : activitati){
            switch (a.getPerioada()){
                case "dimineata" :
                    if (!order) {
                        int index;
                        for (index = 0; index < activitatiDimineata.size() && a.getOrdin() > activitatiDimineata.get(index).getOrdin(); ++index);
                        if (index != activitatiDimineata.size()) {
                           activitatiDimineata.add(index, a);
                        } else {
                            activitatiDimineata.addElement(a);
                        }
                    } else {
                        activitatiDimineata.addElement(a);
                    }
                    break;
                case "amiaza" :
                    if (!order) {
                        int index;
                        for (index = 0; index < activitatiAmiaza.size() && a.getOrdin() > activitatiAmiaza.get(index).getOrdin(); ++index);
                        if (index != activitatiAmiaza.size()) {
                           activitatiAmiaza.add(index, a);
                        } else {
                            activitatiAmiaza.addElement(a);
                        }
                    } else {
                        activitatiAmiaza.addElement(a);
                    }
                    break;
                case "seara" :
                    if (!order) {
                        int index;
                        for (index = 0; index < activitatiSeara.size() && a.getOrdin() > activitatiSeara.get(index).getOrdin(); ++index);
                        if (index != activitatiSeara.size()) {
                           activitatiSeara.add(index, a);
                        } else {
                            activitatiSeara.addElement(a);
                        }
                    } else {
                        activitatiSeara.addElement(a);
                    }
                    break;
                default :
            }
        }
        
        for (JocDTO j : jocuri) {
            switch (j.getPerioada()){
                case "dimineata" :
                    if (!order) {
                        int index;
                        for (index = 0; index < jocuriDimineata.size() && j.getOrdin() > jocuriDimineata.get(index).getOrdin(); ++index);
                        if (index != jocuriDimineata.size()) {
                           jocuriDimineata.add(index, j);
                        } else {
                            jocuriDimineata.addElement(j);
                        }
                    } else {
                        jocuriDimineata.addElement(j);
                    }
                    break;
                case "amiaza" :
                    if (!order) {
                        int index;
                        for (index = 0; index < jocuriAmiaza.size() && j.getOrdin() > jocuriAmiaza.get(index).getOrdin(); ++index);
                        if (index != jocuriAmiaza.size()) {
                           jocuriAmiaza.add(index, j);
                        } else {
                            jocuriAmiaza.addElement(j);
                        }
                    } else {
                        jocuriAmiaza.addElement(j);
                    }
                    break;
                case "seara" :
                    if (!order) {
                        int index;
                        for (index = 0; index < jocuriSeara.size() && j.getOrdin() > jocuriSeara.get(index).getOrdin(); ++index);
                        if (index != jocuriSeara.size()) {
                           jocuriSeara.add(index, j);
                        } else {
                            jocuriSeara.addElement(j);
                        }
                    } else {
                        jocuriSeara.addElement(j);
                    }
                    break;                    
                default :
            }
        }
        
        if (order) {
            int ordin = 0;
            for (int i = 0; i < activitatiDimineata.size(); ++i) {activitatiDimineata.get(i).setOrdin(ordin++);}
            for (int i = 0; i < activitatiAmiaza.size(); ++i) { activitatiAmiaza.get(i).setOrdin(ordin++); }
            for (int i = 0; i < activitatiSeara.size(); ++i) { activitatiSeara.get(i).setOrdin(ordin++); }
            for (int i = 0; i < jocuriDimineata.size(); ++i) { jocuriDimineata.get(i).setOrdin(ordin++); }
            for (int i = 0; i < jocuriAmiaza.size(); ++i) { jocuriAmiaza.get(i).setOrdin(ordin++); }
            for (int i = 0; i < jocuriSeara.size(); ++i) {jocuriSeara.get(i).setOrdin(ordin++); }
        }
        
        jList1.setModel(activitatiDimineata);
        jList2.setModel(activitatiAmiaza);
        jList3.setModel(activitatiSeara);
        jList4.setModel(jocuriDimineata);
        jList5.setModel(jocuriAmiaza);
        jList6.setModel(jocuriSeara);
        activitatiS = activitati;
        jocuriS = jocuri;
        salveaza();
        MainFrame.raportViewed = false;
//        SerializableSerieActivaFrame ssaf = new SerializableSerieActivaFrame(); //toate aceste operatii se efectueaza 
//        ssaf.setActivitatiAmiaza(activitatiAmiaza);                           // deja in procedura salveaza()
//        ssaf.setActivitatiDimineata(activitatiDimineata);
//        ssaf.setActivitatiS(activitatiS);
//        ssaf.setActivitatiSeara(activitatiSeara);
//        ssaf.setJocuriAmiaza(jocuriAmiaza);
//        ssaf.setJocuriDimineata(jocuriDimineata);
//        ssaf.setJocuriS(jocuriS);
//        ssaf.setJocuriSeara(jocuriSeara);
//        SerializeController.getInstance().serializeLastSerieActivaFrame(ssaf);
        jLabel10.setText("ID PROGRAM: " + SerializeController.getInstance().getProgramActivitatiID());
    }
    
    public static void refreshFrame () {
        activitatiDimineata = new DefaultListModel<>();
        activitatiAmiaza = new DefaultListModel<>();
        activitatiSeara = new DefaultListModel<>();
        jocuriDimineata = new DefaultListModel<>();
        jocuriAmiaza = new DefaultListModel<>();
        jocuriSeara = new DefaultListModel<>();
        SerializeController.getInstance().setCanReadSerieActivaFrame(true);
        cleanReorderNotification();
        jList1.setModel(activitatiDimineata);
        jList2.setModel(activitatiAmiaza);
        jList3.setModel(activitatiSeara);
        jList4.setModel(jocuriDimineata);
        jList5.setModel(jocuriAmiaza);
        jList6.setModel(jocuriSeara);
        
    }

    public static SerieDB getSerie() {
        return serie;
    }

    public static void setSerie(SerieDB serie) {
        SerieActivaFrame.serie = serie;
    }
    
    public static List<ActivitateDTO> getOrderedActivitati () {
        List<ActivitateDTO> orderedActivitati = new ArrayList<>();
        boolean reset = false;
        if (id != 0) {
            reset = true;
        }
        ActivitateDTO a;
        for (int i = 0; i < activitatiDimineata.size(); ++i) {
            a = activitatiDimineata.get(i);
            //a.setOrdin(id++);
            orderedActivitati.add(a);
        }
        
        for (int i = 0; i < activitatiAmiaza.size(); ++i) {
            a = activitatiAmiaza.get(i);
            //a.setOrdin(id++);
            orderedActivitati.add(a);
        }
        
        for (int i = 0; i < activitatiSeara.size(); ++i) {
            a = activitatiSeara.get(i);
            //a.setOrdin(id++);
            orderedActivitati.add(a);
        }
       
        activitatiS = orderedActivitati;
        if (reset){
            id = 0;
        }            
        
        return orderedActivitati;
    }
    
    public static List<JocDTO> getOrderedJocuri () {
        List<JocDTO> orderedJouri = new ArrayList<>();
        boolean reset = false;
        if (id != 0) {
            reset = true;
        }
        
        JocDTO j;
        for (int i = 0; i < jocuriDimineata.size(); ++i) {
            j = jocuriDimineata.get(i);
            //j.setOrdin(id++);
            orderedJouri.add(j);
        }
        
        for (int i = 0; i < jocuriAmiaza.size(); ++i) {
            j = jocuriAmiaza.get(i);
            //j.setOrdin(id++);
            orderedJouri.add(j);
        }
        
        for (int i = 0; i < jocuriSeara.size(); ++i) {
            j = jocuriSeara.get(i);
            //j.setOrdin(id++);
            orderedJouri.add(j);
        }
        if (reset) {
            id = 0;
        }
        
        jocuriS = orderedJouri;
        return orderedJouri;
    }
    
    public static boolean isExistingRunningProgram () { // ne spune daca exista activitati in desfasurare
        boolean exists = false;
        if (activitatiS != null) {
            if (!activitatiS.isEmpty()){                
                exists = true;
            } 
        }
        
        if (jocuriS != null) {
            if (!jocuriS.isEmpty()) {
                exists = true;
            }
        }
        return exists;
    } 
    
    public static void reseteazaSarcini (JFrame parent, boolean ask) {
        if (ask) {
            int choice = JOptionPane.showConfirmDialog(parent, "Sigur doresti sa stergi planul de activitati din aceasta zi?", "Atentie", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) return;
        }
        
        cleanReorderNotification();
        
        SerializableSerieActivaFrame ssaf = new SerializableSerieActivaFrame();
        ssaf.setActivitatiAmiaza(activitatiAmiaza);
        ssaf.setActivitatiDimineata(activitatiDimineata);
        ssaf.setActivitatiS(activitatiS);
        ssaf.setActivitatiSeara(activitatiSeara);
        ssaf.setJocuriAmiaza(jocuriAmiaza);
        ssaf.setJocuriDimineata(jocuriDimineata);
        ssaf.setJocuriS(jocuriS);
        ssaf.setJocuriSeara(jocuriSeara);
        SerializeController.getInstance().serializeLastSerieActivaFrame(ssaf);
        
        SerializeController.getInstance().setCanReadPosteazaFrame(false);
        SerializeController.getInstance().setCanReadSerieActivaFrame(false);
        refreshFrame();
        activitatiS = new ArrayList<>();
        jocuriS = new ArrayList<>();
        ServerFrame.getInstance().toBack();
        ServerFrame.getInstance().setPackage(activitatiS, jocuriS);
        ServerFrame.getInstance().shareActivities();
        timer.setDataExpirare(null);
    }
    
    public static void salveaza () {
        if (!isExistingRunningProgram()) return;
        SerializableSerieActivaFrame ssaf = new SerializableSerieActivaFrame();
        ssaf.setActivitatiAmiaza(activitatiAmiaza);
        ssaf.setActivitatiDimineata(activitatiDimineata);
        ssaf.setActivitatiS(activitatiS);
        ssaf.setActivitatiSeara(activitatiSeara);
        ssaf.setJocuriAmiaza(jocuriAmiaza);
        ssaf.setJocuriDimineata(jocuriDimineata);
        ssaf.setJocuriS(jocuriS);
        ssaf.setJocuriSeara(jocuriSeara);
        ssaf.setWereActivitatiReordered(jLabel4.getIcon() != null);
        ssaf.setWereJocuriReordered(jLabel5.getIcon() != null);
        SerializeController.getInstance().serializeSerieActivaFrame(ssaf);
        SerializeController.getInstance().serializeLastSerieActivaFrame(ssaf);
        ///
        System.out.println("Serializare completa (SerieActivaFrame)");
    }
    
    public static void notifica (boolean b) {
        try {
            if (b) {
                jButton14.setIcon(exclamation);
            } else {
                jButton14.setIcon(null);
            }
        } catch (Exception e) {}
    }
    
    public static void vizualizeazaRaport (SerializableSerieActivaFrame ssaf) {
        int opt = JOptionPane.showConfirmDialog(null, "Vizualizati raportul activitatilor din ziua anterioara?", "Raport activitati", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) {
            ActivitatiCompleteFrame.getInstance().buildRaport(ssaf.getActivitatiS(), ssaf.getJocuriS(), ssaf.getDate());
        } else {
             SerializeController.getInstance().serializeLastSerieActivaFrame(null);
             MainFrame.openFrame(new SerieActivaFrame(MainFrame.getSerieActiva()), true);
        }       
    }
    
    /*
    * @expired - argumentul spune functiei daca programul a expirat
    *            sau este doar prelungit
    */
    public static void prelungesteSesiuneDialog (Frame parent, boolean expired) {
        if (!expired) {
            SetDateAndTimeDialog dialog = new SetDateAndTimeDialog(parent, true, SerializeController.getInstance().getDataExpirare());
            if (!dialog.isCanceled()) {
                if (timer != null) timer.setDataExpirare(dialog.getNewDate());
                else SerializeController.getInstance().setDataExpirare(dialog.getNewDate());
            }
            return;
        }
        if (!ServerFrame.isSingletonNull()) ServerFrame.getInstance().opresteServerul(); // serverul trebuie oprit in timpul stabilirii datei de expirare a programului
        int input = JOptionPane.showConfirmDialog(null,
                "Planul de activitati a expirat. Doriti sa-l prelungiti?",
                "Plan de activitati",
                JOptionPane.YES_NO_OPTION);
        
        if (input == JOptionPane.NO_OPTION) {
            reseteazaSarcini(null, false);
            if (timer != null) timer.setDataExpirare(null);
            else SerializeController.getInstance().setDataExpirare(null);
            
            timer.stop();
            instance.dispose();
            instance = null;            
            
            SerializableSerieActivaFrame ssaf = SerializeController.getInstance().deserializeLastSerieActivaFrame();
            SerializeController.getInstance().serializeLastSerieActivaFrame(null);
            if (SerializeController.getInstance().hasExpired(Calendar.getInstance().getTime()) && ssaf != null && !raportViewed) {
                SerieActivaFrame.vizualizeazaRaport(ssaf);
                return;
            }
        } else {
            SetDateAndTimeDialog dialog = new SetDateAndTimeDialog(parent, true, timer.getDataExpirare());
            if (!dialog.isCanceled()) {
                if (timer != null) timer.setDataExpirare(dialog.getNewDate());
                else SerializeController.getInstance().setDataExpirare(dialog.getNewDate());
                
            } else if (SerializeController.getInstance().hasExpired(Calendar.getInstance().getTime())) {
                if (timer != null) timer.setDataExpirare(null);
                else SerializeController.getInstance().setDataExpirare(null);
                
                reseteazaSarcini(null, false);
            }
        }
    }
    
    public void closeFrames () {
        Frame [] frames = Frame.getFrames();
        
        for (Frame f : frames) {
            if (f.getClass() == MainFrame.class) {
                continue;
            } else if (f.getClass() == ServerFrame.class) {
                ServerFrame.getInstance().opresteServerul();
                continue;
            }
            f.dispose();
        }
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList5 = new javax.swing.JList();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList6 = new javax.swing.JList();
        jButton10 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Comenzi"));

        jButton1.setText("jButton1");
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

        jButton2.setText("jButton2");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton2MouseEntered(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("jButton3");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton3MouseEntered(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setText("jButton5");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton5MouseEntered(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("jButton6");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton6MouseEntered(evt);
            }
        });
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton8.setText("jButton8");
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton8MouseEntered(evt);
            }
        });
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("jButton9");
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton9MouseEntered(evt);
            }
        });
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton4.setText("jButton4");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton4MouseEntered(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton7.setText("jButton7");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton7MouseEntered(evt);
            }
        });
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton15.setText("Vezi activitatile complete");
        jButton15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton15MouseEntered(evt);
            }
        });
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton15)
                .addGap(2, 2, 2))
        );

        jLabel4.setText("Activitati");
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList1MouseEntered(evt);
            }
        });
        jList1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jList1KeyTyped(evt);
            }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jLabel1.setText("Dimineata");

        jLabel2.setText("Pranz");

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList1MouseEntered(evt);
            }
        });
        jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList2ValueChanged(evt);
            }
        });
        jList2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jList2KeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jList2);

        jLabel3.setText("Seara");

        jList3.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList1MouseEntered(evt);
            }
        });
        jList3.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList3ValueChanged(evt);
            }
        });
        jList3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jList3KeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(jList3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel5.setText("Jocuri");
        jLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        jList4.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList1MouseEntered(evt);
            }
        });
        jList4.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList4ValueChanged(evt);
            }
        });
        jList4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jList4KeyTyped(evt);
            }
        });
        jScrollPane4.setViewportView(jList4);

        jList5.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList5.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList1MouseEntered(evt);
            }
        });
        jList5.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList5ValueChanged(evt);
            }
        });
        jList5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jList5KeyTyped(evt);
            }
        });
        jScrollPane5.setViewportView(jList5);

        jScrollPane6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList1MouseEntered(evt);
            }
        });

        jList6.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList6.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList6MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList1MouseEntered(evt);
            }
        });
        jList6.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList6ValueChanged(evt);
            }
        });
        jList6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jList6KeyTyped(evt);
            }
        });
        jScrollPane6.setViewportView(jList6);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(jScrollPane5)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton10.setText("Ordoneaza Activitati");
        jButton10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton10MouseEntered(evt);
            }
        });
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Catre animatori"));

        jButton11.setText("Retrimite activitati >> ");
        jButton11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton11MouseEntered(evt);
            }
        });
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText("Redistribuie echipe >>");
        jButton12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton12MouseEntered(evt);
            }
        });
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton14.setText("Evalueaza Activitatile in Desfasurare");
        jButton14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton14MouseEntered(evt);
            }
        });
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
            .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton14))
        );

        jButton13.setText("Reseteaza activitati");
        jButton13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton13MouseEntered(evt);
            }
        });
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(0));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel5MouseEntered(evt);
            }
        });

        jLabel6.setText("Data expirare activitati :");

        jLabel7.setText("Timp ramas :");

        jLabel8.setText("jLabel8");

        jLabel9.setText("jLabel9");
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });

        jButton16.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton16.setText("jButton16");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton17.setText("Ia ziua de la inceput");
        jButton17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton17MouseEntered(evt);
            }
        });
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jLabel10.setText("jLabel10");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton10)
                                .addGap(8, 8, 8)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 52, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jButton13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton17)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        MainFrame.openFrame(new GestioneazaAnimatoriFrame(), false);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        MainFrame.openFrame(new GestioneazaEchipeFrame(serie, false), false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        MainFrame.openFrame(new PosteazaProgramFrame(), true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        if (jList1.getSelectedValue() != null && 
            evt.getButton() == MouseEvent.BUTTON1 &&
            evt.getClickCount() == 2){
            AdaugaInformatiiFrame f = new AdaugaInformatiiFrame(activitatiS, jocuriS);
            f.enableButtons(false);
            f.setSelectedItem((ActivitateDTO) jList1.getSelectedValue(), null);
            f.showInfo((ActivitateDTO) jList1.getSelectedValue(), null);
            f.disposeOnClose(); 
            f.setVisible(true);
        }
    }//GEN-LAST:event_jList1MouseClicked

    private void jList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseClicked
        if (jList2.getSelectedValue() != null && 
            evt.getButton() == MouseEvent.BUTTON1 &&
            evt.getClickCount() == 2){
            AdaugaInformatiiFrame f = new AdaugaInformatiiFrame(activitatiS, jocuriS);
            f.enableButtons(false);
            f.setSelectedItem((ActivitateDTO) jList2.getSelectedValue(), null);
            f.showInfo((ActivitateDTO) jList2.getSelectedValue(), null);
            f.disposeOnClose();
            f.setVisible(true);
        }
    }//GEN-LAST:event_jList2MouseClicked

    private void jList3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList3MouseClicked
        if (jList3.getSelectedValue() != null && 
            evt.getButton() == MouseEvent.BUTTON1 &&
            evt.getClickCount() == 2){
            AdaugaInformatiiFrame f = new AdaugaInformatiiFrame(activitatiS, jocuriS);
            f.enableButtons(false);
            f.setSelectedItem((ActivitateDTO) jList3.getSelectedValue(), null);
            f.showInfo((ActivitateDTO) jList3.getSelectedValue(), null);
            f.disposeOnClose();
            f.setVisible(true);
        }
    }//GEN-LAST:event_jList3MouseClicked

    private void jList4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList4MouseClicked
        if (jList4.getSelectedValue() != null && 
            evt.getButton() == MouseEvent.BUTTON1 &&
            evt.getClickCount() == 2){
            AdaugaInformatiiFrame f = new AdaugaInformatiiFrame(activitatiS, jocuriS);
            f.enableButtons(false);
            f.setSelectedItem(null, (JocDTO) jList4.getSelectedValue());
            f.showInfo(null, (JocDTO) jList4.getSelectedValue());
            f.disposeOnClose();
            f.setVisible(true);
        }
    }//GEN-LAST:event_jList4MouseClicked

    private void jList5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList5MouseClicked
        if (jList5.getSelectedValue() != null && 
            evt.getButton() == MouseEvent.BUTTON1 &&
            evt.getClickCount() == 2){
            AdaugaInformatiiFrame f = new AdaugaInformatiiFrame(activitatiS, jocuriS);
            f.enableButtons(false);
            f.setSelectedItem(null, (JocDTO) jList5.getSelectedValue());
            f.showInfo(null, (JocDTO) jList5.getSelectedValue());
            f.disposeOnClose();
            f.setVisible(true);
        }
    }//GEN-LAST:event_jList5MouseClicked

    private void jList6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList6MouseClicked
        if (jList6.getSelectedValue() != null && 
            evt.getButton() == MouseEvent.BUTTON1 &&
            evt.getClickCount() == 2){
            AdaugaInformatiiFrame f = new AdaugaInformatiiFrame(activitatiS, jocuriS);
            f.enableButtons(false);
            f.setSelectedItem(null, (JocDTO) jList6.getSelectedValue());
            f.showInfo(null, (JocDTO) jList6.getSelectedValue());
            f.disposeOnClose();
            f.setVisible(true);
        }
    }//GEN-LAST:event_jList6MouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        salveaza();
        timer.stop();
    }//GEN-LAST:event_formWindowClosing

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        ServerFrame.getInstance().toFront();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (activitatiS == null || jocuriS == null) {
            JOptionPane.showMessageDialog(this, "Nimic de afisat.");
            return;
        }
        new ProgramEchipeFrame(activitatiS, jocuriS).setVisible(true);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        MainFrame.openFrame(new GestioneazaActivitatiFrame(), true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        MainFrame.openFrame(new CautaMembruFrame(serie), false);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jList1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList1KeyTyped
        int index = jList1.getSelectedIndex();
        
        if (index == -1) return; 
        ActivitateDTO a = activitatiDimineata.get(index);
        jLabel4.setIcon(exclamation);
        jLabel4.setToolTipText("<html>Ordinea activitatilor a fost modificata.<br>Click aici pentru retrimiterea programului.</html>");
        
        if (evt.getKeyChar() == KeyEvent.VK_W + 32) {
            if (index > 0) {
                int newOrder = activitatiDimineata.get(index-1).getOrdin();
                if (activitatiDimineata.removeElement(a)) {
                    // interschimba numarele de ordine
                    int oldOrder = a.getOrdin();                    
                    activitatiDimineata.get(index-1).setOrdin(oldOrder);
                    a.setOrdin(newOrder);
                    
                    activitatiDimineata.add(index-1, a);
                    jList1.setSelectedIndex(index-1);
                    selectedIndex1 = index-1;
                    needSelect1 = true;
                }
            }
        } else if (evt.getKeyChar() == KeyEvent.VK_S + 32) {
            if (index < activitatiDimineata.size() - 1) {
                int newOrder = activitatiDimineata.get(index+1).getOrdin();
                if (activitatiDimineata.removeElement(a)){
                    // interschimbam numerele de ordine
                    int oldOrder = a.getOrdin();                    
                    activitatiDimineata.get(index).setOrdin(oldOrder);
                    a.setOrdin(newOrder);
                    
                    activitatiDimineata.add(index+1, a);
                    jList1.setSelectedIndex(index+1);
                    selectedIndex1 = index+1;
                    needSelect1 = true;
                }
            }
        }
        
        jList1.setModel(activitatiDimineata);
    }//GEN-LAST:event_jList1KeyTyped

    private void jList2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList2KeyTyped
        int index = jList2.getSelectedIndex();
        
        if (index == -1) return; 
        ActivitateDTO a = activitatiAmiaza.get(index);
        jLabel4.setIcon(exclamation);
        jLabel4.setToolTipText("<html>Ordinea activitatilor a fost modificata.<br>Click aici pentru retrimiterea programului.</html>");
        
        if (evt.getKeyChar() == KeyEvent.VK_W + 32) {            
            if (index > 0) {
                int newOrder = activitatiAmiaza.get(index-1).getOrdin();
                if (activitatiAmiaza.removeElement(a)) {
                    int oldOrder = a.getOrdin();                    
                    activitatiAmiaza.get(index-1).setOrdin(oldOrder);
                    a.setOrdin(newOrder);
                    
                    activitatiAmiaza.add(index-1, a);
                    jList2.setSelectedIndex(index-1);
                    selectedIndex2 = index-1;
                    needSelect2 = true;
                }
            }
        } else if (evt.getKeyChar() == KeyEvent.VK_S + 32) {
            if (index < activitatiAmiaza.size() - 1) {
                int newOrder = activitatiAmiaza.get(index+1).getOrdin();
                if (activitatiAmiaza.removeElement(a)){
                    int oldOrder = a.getOrdin();                    
                    activitatiAmiaza.get(index).setOrdin(oldOrder);
                    a.setOrdin(newOrder);
                    
                    activitatiAmiaza.add(index+1, a);
                    jList2.setSelectedIndex(index+1);
                    selectedIndex2 = index+1;
                    needSelect2 = true;
                }
            }
        }
        
        jList2.setModel(activitatiAmiaza);
    }//GEN-LAST:event_jList2KeyTyped

    private void jList3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList3KeyTyped
        int index = jList3.getSelectedIndex();
        
        if (index == -1) return; 
        ActivitateDTO a = activitatiSeara.get(index);
        jLabel4.setIcon(exclamation);
        jLabel4.setToolTipText("<html>Ordinea activitatilor a fost modificata.<br>Click aici pentru retrimiterea programului.</html>");
        
        if (evt.getKeyChar() == KeyEvent.VK_W + 32) {
            if (index > 0) {
                int newOrder = activitatiSeara.get(index-1).getOrdin();
                if (activitatiSeara.removeElement(a)) {
                    int oldOrder = a.getOrdin();                    
                    activitatiSeara.get(index-1).setOrdin(oldOrder);
                    a.setOrdin(newOrder);
                    
                    activitatiSeara.add(index-1, a);
                    jList3.setSelectedIndex(index-1);
                    selectedIndex3 = index-1;
                    needSelect3 = true;
                }
            }
        } else if (evt.getKeyChar() == KeyEvent.VK_S + 32) {
            if (index < activitatiSeara.size() - 1) {
                int newOrder = activitatiSeara.get(index+1).getOrdin();
                if (activitatiSeara.removeElement(a)){
                    int oldOrder = a.getOrdin();                    
                    activitatiSeara.get(index).setOrdin(oldOrder);
                    a.setOrdin(newOrder);
                    
                    activitatiSeara.add(index+1, a);
                    jList3.setSelectedIndex(index+1);
                    selectedIndex3 = index+1;
                    needSelect3 = true;
                }
            }
        }
        
        jList3.setModel(activitatiSeara);
    }//GEN-LAST:event_jList3KeyTyped

    private void jList4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList4KeyTyped
        int index = jList4.getSelectedIndex();
        
        if (index == -1) return; 
        JocDTO a = jocuriDimineata.get(index);
        jLabel5.setIcon(exclamation);
        jLabel5.setToolTipText("<html>Ordinea jocurilor a fost modificata.<br>Click aici pentru retrimiterea programului.</html>");
        
        if (evt.getKeyChar() == KeyEvent.VK_W + 32) {
            if (index > 0) {
                int newOrder = jocuriDimineata.get(index-1).getOrdin();
                if (jocuriDimineata.removeElement(a)) {
                    int oldOrder = a.getOrdin();                    
                    jocuriDimineata.get(index-1).setOrdin(oldOrder);
                    a.setOrdin(newOrder);
                    
                    jocuriDimineata.add(index-1, a);
                    jList4.setSelectedIndex(index-1);
                    selectedIndex4 = index-1;
                    needSelect4 = true;
                }
            }
        } else if (evt.getKeyChar() == KeyEvent.VK_S + 32) {
            if (index < jocuriDimineata.size() - 1) {
                int newOrder = jocuriDimineata.get(index+1).getOrdin();
                if (jocuriDimineata.removeElement(a)){
                    int oldOrder = a.getOrdin();                    
                    jocuriDimineata.get(index).setOrdin(oldOrder);
                    a.setOrdin(newOrder);
                    
                    jocuriDimineata.add(index+1, a);
                    jList4.setSelectedIndex(index+1);
                    selectedIndex4 = index+1;
                    needSelect4 = true;
                }
            }
        }
        
        jList4.setModel(jocuriDimineata);
    }//GEN-LAST:event_jList4KeyTyped

    private void jList5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList5KeyTyped
        int index = jList5.getSelectedIndex();
        
        if (index == -1) return; 
        JocDTO a = jocuriAmiaza.get(index);
        jLabel5.setIcon(exclamation);
        jLabel5.setToolTipText("<html>Ordinea jocurilor a fost modificata.<br>Click aici pentru retrimiterea programului.</html>");
        
        if (evt.getKeyChar() == KeyEvent.VK_W + 32) {
            if (index > 0) {
                int newOrder = jocuriAmiaza.get(index-1).getOrdin();
                if (jocuriAmiaza.removeElement(a)) {
                    int oldOrder = a.getOrdin();                    
                    jocuriAmiaza.get(index-1).setOrdin(oldOrder);
                    a.setOrdin(newOrder);
                    
                    jocuriAmiaza.add(index-1, a);
                    jList5.setSelectedIndex(index-1);
                    selectedIndex5 = index-1;
                    needSelect5 = true;
                }
            }
        } else if (evt.getKeyChar() == KeyEvent.VK_S + 32) {
            if (index < jocuriAmiaza.size() - 1) {
                int newOrder = jocuriAmiaza.get(index+1).getOrdin();
                if (jocuriAmiaza.removeElement(a)){
                    int oldOrder = a.getOrdin();                    
                    jocuriAmiaza.get(index).setOrdin(oldOrder);
                    a.setOrdin(newOrder);
                    
                    jocuriAmiaza.add(index+1, a);
                    jList5.setSelectedIndex(index+1);
                    selectedIndex5 = index+1;
                    needSelect5 = true;
                }
            }
        }
        
        jList5.setModel(jocuriAmiaza);
    }//GEN-LAST:event_jList5KeyTyped

    private void jList6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList6KeyTyped
        int index = jList6.getSelectedIndex();
        
        if (index == -1) return; 
        JocDTO a = jocuriSeara.get(index);
        jLabel5.setIcon(exclamation);
        jLabel5.setToolTipText("<html>Ordinea jocurilor a fost modificata.<br>Click aici pentru retrimiterea programului.</html>");
        
        if (evt.getKeyChar() == KeyEvent.VK_W + 32) {
            if (index > 0) {
                int newOrder = jocuriSeara.get(index-1).getOrdin();
                if (jocuriSeara.removeElement(a)) {
                    int oldOrder = a.getOrdin();                    
                    jocuriSeara.get(index-1).setOrdin(oldOrder);
                    a.setOrdin(newOrder);
                    
                    jocuriSeara.add(index-1, a);
                    jList6.setSelectedIndex(index-1);
                    selectedIndex6 = index-1;
                    needSelect6 = true;
                }
            }
        } else if (evt.getKeyChar() == KeyEvent.VK_S + 32) {
            if (index < jocuriSeara.size() - 1) {
                int newOrder = jocuriSeara.get(index+1).getOrdin();
                if (jocuriSeara.removeElement(a)){
                    int oldOrder = a.getOrdin();                    
                    jocuriSeara.get(index).setOrdin(oldOrder);
                    a.setOrdin(newOrder);
                    
                    jocuriSeara.add(index+1, a);
                    jList6.setSelectedIndex(index+1);
                    selectedIndex6 = index+1;
                    needSelect6 = true;
                }
            }
        }
        
        jList6.setModel(jocuriSeara);
    }//GEN-LAST:event_jList6KeyTyped

    
    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        //System.out.println("Selection changed");
        if (needSelect1){
            jList1.setSelectedIndex(selectedIndex1);
            needSelect1 = false;
        }
    }//GEN-LAST:event_jList1ValueChanged

    private void jList2ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList2ValueChanged
        if (needSelect2){
            jList2.setSelectedIndex(selectedIndex2);
            needSelect2 = false;
        }
    }//GEN-LAST:event_jList2ValueChanged

    private void jList3ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList3ValueChanged
        if (needSelect3){
            jList3.setSelectedIndex(selectedIndex3);
            needSelect3 = false;
        }
    }//GEN-LAST:event_jList3ValueChanged

    private void jList4ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList4ValueChanged
        if (needSelect4){
            jList4.setSelectedIndex(selectedIndex4);
            needSelect4 = false;
        }
    }//GEN-LAST:event_jList4ValueChanged

    private void jList5ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList5ValueChanged
        if (needSelect5){
            jList5.setSelectedIndex(selectedIndex5);
            needSelect5 = false;
        }
    }//GEN-LAST:event_jList5ValueChanged

    private void jList6ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList6ValueChanged
        if (needSelect6){
            jList6.setSelectedIndex(selectedIndex6);
            needSelect6 = false;
        }
    }//GEN-LAST:event_jList6ValueChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jLabel11.setIcon(ICON);
        new AsyncTask(new Event () {
            @Override
            public void doAction() {
                final TeamSelectorDialog selector = new TeamSelectorDialog(SerieActivaFrame.this, true, serie);
                selector.setOnDoneEvent(new Event () {
                    @Override
                    public void doAction() {
                        MainFrame.openFrame(new ClasamentFrame(serie, selector.getSelectedTeams()), true);
                    }
                });
                selector.setVisible(true);
            }
        }, new Event() {
            @Override
            public void doAction() {
                jLabel11.setIcon(null);
            }
        }).execute();
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        if (activitatiS == null && jocuriS == null) {
            JOptionPane.showMessageDialog(this, "Nimic de ordonat");
            return;
        }
        MainFrame.openFrame(new OrderFrame(activitatiS, jocuriS), false);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            DistribuieProgramFrame dpf = new DistribuieProgramFrame(new ArrayList<>(activitatiS), new ArrayList<>(jocuriS));
            //dpf.setVisible(true);
            MainFrame.openFrame(dpf, false);
            dpf.openPreviewMode();
        } catch (Exception ex) {
            //Logger.getLogger(SerieActivaFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Nici un program de afisat", "Chill", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        PermissionFrame perm = new PermissionFrame(this, true, "Aprobare distribuire activitati : ", UserDTO.ACCES_COORDONATOR);
        if (!perm.isApproved()) {
            JOptionPane.showMessageDialog(this, "Respins", "Acces refuzat", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ServerFrame.getInstance().setPackage(activitatiS, jocuriS);
        ServerFrame.getInstance().shareActivities();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        PermissionFrame perm = new PermissionFrame(this, true, "Aprobare distribuire activitati : ", UserDTO.ACCES_COORDONATOR);
        if (!perm.isApproved()) {
            JOptionPane.showMessageDialog(this, "Respins", "Acces refuzat", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ServerFrame.getInstance().shareSerie();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        int choice = JOptionPane.showConfirmDialog(this, "Sigur doresti sa stergi planul de activitati din aceasta zi?", "Atentie", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) return;
        
        PermissionFrame perm = new PermissionFrame(this, true, "Aprobare stergere activitati : ", UserDTO.ACCES_COORDONATOR);
        if (!perm.isApproved()) {
            JOptionPane.showMessageDialog(this, "Respins", "Acces refuzat", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        SerializeController.getInstance().setCanReadPosteazaFrame(false);
        SerializeController.getInstance().setCanReadSerieActivaFrame(false);
        dispose();
        
        try {
            File f = new File("./obj/adaugaInformatii.srz");
            f.delete(); 
            
            f = new File(".obj/distribuieEchipe.srz");
            f.delete();
            f = new File(".obj/distribuieProgram.srz");
            f.delete();
            f = new File(".obj/posteazaProgram.srz");
            f.delete();
            f = new File(".obj/serieActiva.srz");
            f.delete();
        } catch (Exception e) {
            
        }
        
        SerieActivaFrame.activitatiS = null;
        SerieActivaFrame.jocuriS = null;
        SerializeController.getInstance().serializaDistribuieProgramFrame(null);
        SerializeController.getInstance().serializeAdaugaInformatiiFrame(null);
        SerializeController.getInstance().serializeDistribuieEchipeFrame(null);
        SerializeController.getInstance().serializeSerieActivaFrame(null);
        SerializeController.getInstance().serializeLastSerieActivaFrame(null);
        SerializeController.getInstance().serializeazaPostareProgramFrame(null);
        //SerializeController.getInstance().setDataExpirare(null);
        timer.setDataExpirare(null);
        closeFrames();
        MainFrame.openFrame(new SerieActivaFrame(serie), true);
        
        
        choice = JOptionPane.showConfirmDialog(this, "Doresti sa notifici animatorii?", "Notificare", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION){
            ServerFrame.getInstance().setVisible(true);
            ServerFrame.getInstance().toFront();
            ServerFrame.getInstance().shareActivities();
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        notifica(false);
        try {
            ActivitatiCompleteFrame.getInstance().refreshFrame();
            ActivitatiCompleteFrame.getInstance().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Nici o activitate in desfasurare...", "Chill", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        MainFrame.openFrame(new DesktopActivitatiFrame(serie), false);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if (ActivitatiCompleteFrame.getInstance().raportMode) {
            instance.setState(Frame.ICONIFIED);
        }
        if (!ServerFrame.isSingletonNull()) {
            if (ServerFrame.getInstance().isServerRunning()) {
                jButton7.setBackground(Color.GREEN);
            }
        }
    }//GEN-LAST:event_formWindowOpened

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        HelperFrame.getInstance().post(
                "Fereastra seriei active",
                "Aceasta fereastra reprezinta nucleul seriei in desfasurare. De aici se pot distribui activitati, se pot gestiona echipele "
                        + "participante sau activitatile/jocurile generale, se poate vizualiza progresul planului de activitati actual si "
                        + "se poate consulta istoricul de activitate al oricarei echipe. Totodata, fereastra ofera facilitatea de a gestiona "
                        + "si animatorii taberei.");
    }//GEN-LAST:event_formMouseEntered

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        HelperFrame.getInstance().post(
                "Distribuie activitati",
                "De aici se porneste conceperea planului de activitati pentru ziua actuala. Ce activitati, care animatori le organizeaza, "
                        + "detalii despre activitate si care echipe le vor realiza, toate acestea se stabilesc in procesul inceput prin "
                        + "apasarea acestui buton.");
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        HelperFrame.getInstance().post(
                "Clasament",
                "Lanseaza o fereastra in care este reprezentat grafic clasamentul actual al echipelor, in functie de jocurile realizate "
                        + "pana in momentul de fata.");
    }//GEN-LAST:event_jButton2MouseEntered

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        HelperFrame.getInstance().post(
                "Gestionarea si administrarea activitatilor",
                "In fereastra ce se ascunde in spatele acestui buton se pot adauga, modifica, sterge sau doar vizualiza activitatile "
                        + "care pot face parte din programul oricarei zile.");
    }//GEN-LAST:event_jButton3MouseEntered

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
        HelperFrame.getInstance().post(
                "Gestionarea si administrarea echipelor",
                "Lanseaza o noua fereastra care cuprinde facilitatile necesare pentru a vizualiza, adauga, modifica sau sterge oricare dintre echipele "
                        + "seriei actuale, alaturi de membri acesteia.");
    }//GEN-LAST:event_jButton4MouseEntered

    private void jButton5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseEntered
        HelperFrame.getInstance().post(
                "Gestionarea si administrarea animatorilor",
                "Folosindu-te de acest meniu, poti adauga noi animatori, sau sa modifici, chiar sa stergi, un animator deja existent. "
                        + "Tot din meniul oferit, vei putea seta disponibilitatea oricarui animator.");
    }//GEN-LAST:event_jButton5MouseEntered

    private void jButton6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseEntered
        HelperFrame.getInstance().post(
                "Program animatori",
                "Acest buton reprezinta o scurtatura catre pasul 2 al procestului de 'Postare a programului'. In fereastra care "
                        + "se va deschide poate fi vizualizat sau modificat programul de activitati al oricarui animator. \n"
                        + "DE RETINUT: ordinea activitatilor din lista ordonata este aceeasi cu ordinea in care vor aparea sarcinile "
                        + "pe aplicatia mobila a animatorului. Acest fapt conteaza, deoarece animatorul va rezolva sarcinile in ordinea in "
                        + "care ii sunt date. ");
    }//GEN-LAST:event_jButton6MouseEntered

    private void jButton7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseEntered
        HelperFrame.getInstance().post(
                "Start server",
                "Apasand acest buton, poti porni modulul 'Server' al acestei aplicatii, care permite conectarea animatorilor ("
                        + "prin intermediul aplicatiei mobile) la aplicatie desktop. Pornind acest modul, vei face vizibil "
                        + "planul de activitati actual si vei putea urmari animatorii care au fost informati in privinta acestuia.\n"
                        + "Conturul verde al butonului semnifica faptul ca Serverul este pornit.\n"
                        + "ATENTIE: acest modul poate consuma destule resurse ale calculatorului pe care ruleaza. Este recomandat "
                        + "ca in timpul folosirii acestui modul sa nu se foloseasca si alte aplicatii la fel de costisitoare.");
    }//GEN-LAST:event_jButton7MouseEntered

    private void jButton8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseEntered
        HelperFrame.getInstance().post(
                "Programul echipelor",
                "In fereastra care se va deschide dupa apasarea acestui buton, vei putea vedea activitatile pe care "
                        + "le are de indeplinit fiecare echipa.");
    }//GEN-LAST:event_jButton8MouseEntered

    private void jButton9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseEntered
        HelperFrame.getInstance().post(
                "Cautare membru",
                "Daca doresti sa afli mai multe informatii despre un copil din seria actuala si stii cum il cheama, poti "
                        + "folosi acest meniu.");
    }//GEN-LAST:event_jButton9MouseEntered

    private void jButton15MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton15MouseEntered
        HelperFrame.getInstance().post(
                "Gestionarea si vizualizarea istoricului de activitati al echipelor",
                "Click pentru a vedea ce activitati a indeplinit fiecare echipa pana in momentul de fata, si detalii despre acestea.");
    }//GEN-LAST:event_jButton15MouseEntered

    private void jButton11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton11MouseEntered
        HelperFrame.getInstance().post(
                "Update activitati",
                "Daca ai facut anumite modificari asupra uneia sau mai multor activitati din programul actual, "
                        + "te poti asigura ca toti animatorii vor fi informati apasand acest buton. Aplicatia "
                        + "va retrimite activitatile cu ultimele modificari facute.");
    }//GEN-LAST:event_jButton11MouseEntered

    private void jButton12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton12MouseEntered
        HelperFrame.getInstance().post(
                "Update echipe",
                "Daca ai facut vreo modificare asupra echipelor sau asupra membrilor acestora, e bine sa notifici si animatorii prin "
                        + "apasarea acestui buton.");
    }//GEN-LAST:event_jButton12MouseEntered

    private void jButton14MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton14MouseEntered
        HelperFrame.getInstance().post(
                "Monitorul planului de activitati",
                "In fereastra care va fi deschisa vei putea vizualiza progresul animatorilor, sarcinile pe care acestia le au de facut, "
                        + "stadiul lor, si care echipe si-au incheiat activitatea.\n"
                        + "Fii pe faza! Cand observi o mica pictograma cu un semn al exclamarii pe acest buton, "
                        + "inseamna ca au sosit noi informatii de la cel putin un animator.");
    }//GEN-LAST:event_jButton14MouseEntered

    private void jList1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseEntered
        if (evt.getSource().getClass() != JList.class) return;
        JList list = (JList) evt.getSource();
        String perioada = "";
        if (list == jList1 || list == jList4) perioada = "dimineata";
        else if (list == jList2 || list == jList5) perioada = "amiaza";
        else if (list == jList3 || list == jList6) perioada = "seara";
        
        HelperFrame.getInstance().post(
                "Programul activitatilor de " + perioada,
                "Aici sunt afisate toate activitatile care vor avea loc " + perioada + ", in ordinea in care trebuie sa fie realizate. "
                        + "Pentru a reordona activitatile, selecteaz-o pe cea dorita, si foloseste tastele 'W' si 'S' pentru "
                        + "a o deplasa pe pozitia dorita. Pentru reordonarea tuturor activitatilor, foloseste optiunea 'Ordoneaza activitati'. \n"
                        + "ATENTIE: dupa ce ai terminat de reordonat, asigura-te ca folosesti optiunea de 'Retrimitere activitati' "
                        + "pentru a instiinta animatorii asupra modificarilor facute. \n"
                        + "DE RETINUT: ordinea activitatilor conteaza deoarece, in funtie de asta, animatorul isi va realiza sarcinile. "
                        + "Nu se recomanda nerespectarea oridinii perioadelor zilei. (adica plasarea unei activitati de seara inaintea uneia de dimineata, spre exemplu)");
    }//GEN-LAST:event_jList1MouseEntered

    private void jButton10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MouseEntered
        HelperFrame.getInstance().post(
                "Meniu de reordonare",
                "Folosind aceasta facilitate, vei putea ordona toate activitatile din program in mod arbitrar.");
    }//GEN-LAST:event_jButton10MouseEntered

    private void jButton13MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton13MouseEntered
        HelperFrame.getInstance().post(
                "Resetare",
                "Folosind aceasta optiune, vei reseta intreg planul de activitati. Acest fapt va duce la stergerea inlusiv a "
                        + "detaliilor despre activitatile din program. Gandeste-te bine inainte s-o folosesti.");
    }//GEN-LAST:event_jButton13MouseEntered

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        jButton13ActionPerformed(evt);
        if (ControllerDB.getInstance().wipeAllActivitiesByDate(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()))) {
            
            return;
        }
        
        JOptionPane.showMessageDialog(this, "O eroare a aparut la stergea activitatilor din baza de date", "Eroare", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jPanel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseEntered
        HelperFrame.getInstance().post(
            "Control ora inchidere",
            "Fiecare program de activitati postat vine implicit cu o ora de dezactivare, la care aplicatia "
            + "salveaza datele folosite si face loc pentru planificarea unui nou program. "
            + "\nOra standard de dezactivare este 23:59. Poti prelungi sau scurta programul folosind optiunea "
            + "'Seteaza data expirare'.");
    }//GEN-LAST:event_jPanel5MouseEntered

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        prelungesteSesiuneDialog(instance, false);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        dispose();
        formWindowClosing(null);
        MainFrame.openFrame(new SerieActivaFrame(serie), true);
    }//GEN-LAST:event_jLabel9MouseClicked

    private void jButton17MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseEntered
        HelperFrame.getInstance().post(
                "Buton resetare",
                "In cazul unui inconvenient major al programului de azi, care nu mai poate fi reparat, "
                        + "se poate folosi aceasta optiune pentru a sterge TOATE datele inregistrate "
                        + "in aceasta zi, ca si cum nimic nu s-ar fi intamplat.");
    }//GEN-LAST:event_jButton17MouseEntered

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {
            jButton11ActionPerformed(null);
            jLabel4.setIcon(null);
            jLabel4.setToolTipText(null);
            jLabel5.setIcon(null);
            jLabel5.setToolTipText(null);
        }
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {
            jButton11ActionPerformed(null);
            jLabel4.setIcon(null);
            jLabel4.setToolTipText(null);
            jLabel5.setIcon(null);
            jLabel5.setToolTipText(null);
        }
    }//GEN-LAST:event_jLabel5MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private static javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    public static javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private static javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private static javax.swing.JLabel jLabel4;
    private static javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private static javax.swing.JList jList1;
    private static javax.swing.JList jList2;
    private static javax.swing.JList jList3;
    private static javax.swing.JList jList4;
    private static javax.swing.JList jList5;
    private static javax.swing.JList jList6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    // End of variables declaration//GEN-END:variables
}
