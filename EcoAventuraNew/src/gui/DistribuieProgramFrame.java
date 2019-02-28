/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import costumSerialization.SerializableDistribuieProgramFrame;
import db.AnimatorDB;
import dto.ActivitateDTO;
import dto.AnimatorDTO;
import dto.JocDTO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import service.ControllerDB;
import service.SerializeController;

/**
 *
 * @author Alexandru
 */
public class DistribuieProgramFrame extends javax.swing.JFrame {
    private final static int COMPRIMATED_WIDTH = 715;
    private final static int EXTENDED_WIDTH = 960;
    private final static int STANDARD_WIDTH = 200;
    private DefaultComboBoxModel<String> comboModel1;
    private DefaultComboBoxModel<AnimatorDB> comboModel2;
    private DefaultListModel<ActivitateDTO> activitati;
    private DefaultListModel<JocDTO> jocuri;
    private HashMap<AnimatorDB, DefaultListModel<ActivitateDTO> > sarcini;
    private HashMap<DefaultListModel<ActivitateDTO> , DefaultListModel<JocDTO> > perechi;
    private HashMap<ActivitateDTO, Integer> usedActivitati;
    private HashMap<JocDTO, Integer> usedJocuri;
    private boolean isChanged;
    private MyListCellRenderer cellRender;
    private HashMap<AnimatorDB, DefaultListModel<Object>> sarciniOrdonate; 
    private HashMap<AnimatorDB, Boolean> aplica;
    private int selectedIndex = -1;
    private boolean needSelect = false;
    
    private List<ActivitateDTO> paramActivitati;
    private List<JocDTO> paramJocuri; 
    /**
     * Creates new form DistribuieProgramFrame
     * @param activitati
     * @param jocuri
     * @throws java.lang.Exception
     */
    public DistribuieProgramFrame(List<ActivitateDTO> activitati, List<JocDTO> jocuri) throws Exception {
        super ("Distribuie Activitati");
        initComponents();
        
        this.paramActivitati = activitati;
        this.paramJocuri = jocuri;
        
        if (activitati == null && jocuri == null){
            JOptionPane.showMessageDialog(this, "Nici o activitate selectata");
            dispose(); 
            return;
        }
        
        comboModel1 = new DefaultComboBoxModel<>(new String [] {"Activitati", "Jocuri"});        
        
        jComboBox1.setModel(comboModel1);
        jComboBox1.setSelectedItem("Activitati");       
        
        
        List<AnimatorDB> animatori = ControllerDB.getInstance().getAnimatoriByDisponibilitate(true);       
        
        if (animatori == null) {
            JOptionPane.showMessageDialog(this, "Nu exista niciun animator disponibil");
            dispose();
            MainFrame.openFrame(new GestioneazaAnimatoriFrame(), false);
            throw new Exception();
        } else if (animatori.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nu exista niciun animator disponibil");
            dispose();
            MainFrame.openFrame(new GestioneazaAnimatoriFrame(), false);
            throw new Exception ();
        }  
        
        this.activitati = new DefaultListModel<>();
        this.jocuri = new DefaultListModel<>();
        
        for (ActivitateDTO a : activitati){
            this.activitati.addElement(a);
        }

        for (JocDTO j : jocuri){
            this.jocuri.addElement(j);
        }
        
        SerializableDistribuieProgramFrame sdpf = SerializeController.getInstance().deserializareDistribuieProgramFrame();
        
        // TODO: elimina din liste activitatile inexistente (care au fost sterse din program
        
        if (SerializeController.getInstance().isCanReadDistribuieFrame() && sdpf != null){
            ////
            System.out.println("Distribuie Program : date incarcate cu succes.");
            comboModel2 = sdpf.getComboModel2();
            //this.activitati = sdpf.getActivitati();
            //this.jocuri = sdpf.getJocuri();
            aplica = sdpf.getAplica();
            sarciniOrdonate = sdpf.getSarciniOrdonate();
            sarcini = sdpf.getSarcini();
            perechi = sdpf.getPerechi();
            usedActivitati = sdpf.getUsedActivitati();
            usedJocuri = sdpf.getUserJocuri();
            
            // se creaza liste de activitati pt animatorii recent adaugati
            DefaultListModel<ActivitateDTO> model;
            for (AnimatorDB a : animatori) {
                if (comboModel2.getIndexOf(a) == -1){
                    comboModel2.addElement(a);
                    model = new DefaultListModel<>();
                    sarcini.put(a,
                                model);
                    perechi.put(model,
                                new DefaultListModel<JocDTO>());                    
                    sarciniOrdonate.put(a, new DefaultListModel<>());
                    aplica.put(a, false);
                }
            }
            
            // se elimina listele animatorilor recent eliminati
            AnimatorDB animator;
            for (int i = 0; i < comboModel2.getSize(); ++i){
                animator = comboModel2.getElementAt(i);
                if (!animatori.contains(animator)){
                    DefaultListModel<JocDTO> jocuriAnimator = perechi.get(sarcini.get(animator));
                    perechi.remove(sarcini.get(animator));
                    DefaultListModel<ActivitateDTO> activitatiAnimator = sarcini.get(animator);
                    sarcini.remove(animator);
                    sarciniOrdonate.remove(animator);
                    aplica.remove(animator);
                    comboModel2.removeElement(animator);
                    
                    // fa ceva cu activitatile animatorului eliminat
                }
            }
            
            jComboBox2.setModel(comboModel2);
            jComboBox2.setSelectedItem(animatori.get(0));
            
            //se sterg din listele actuale activitatile care au fost eliminate la pasul anterior
            for (int i = 0; i < comboModel2.getSize(); ++i) {
                AnimatorDB anim = comboModel2.getElementAt(i);
                DefaultListModel<ActivitateDTO> ma = sarcini.get(anim);
                
                //eliminam activitatile
                for (int j = 0; j < ma.getSize(); ++j) {
                    ActivitateDTO activ = ma.get(j);
                    boolean exists = false;
                    for (int k = 0; k < activitati.size() && !exists; ++k) {
                        if (activ.equals(activitati.get(k))) exists = true;
                    }
                    if (!exists) {
                        ma.remove(j);
                        --j; // ?
                        sarciniOrdonate.get(anim).removeElement(activ);
                    }
                }
                
                DefaultListModel<JocDTO> mj = perechi.get(ma);
                
                //eliminam jocurile
                for (int j = 0; j < mj.getSize(); ++j) {
                    JocDTO joc = mj.get(j);
                    boolean exists = false;
                    for (int k = 0; k < jocuri.size() && !exists; ++k) {
                        if (joc.equals(jocuri.get(k))) exists = true;
                    }
                    
                    if (!exists) {
                        mj.remove(j);
                        j--;
                        sarciniOrdonate.get(anim).removeElement(joc);
                    }
                    
                }
            }
            
            // se adauga numarul de distribuiri pt activitatile si jocurile recent adaugate
            for (ActivitateDTO a : activitati) {
                if (!usedActivitati.containsKey(a)){
                    usedActivitati.put(a, 0);
                }
            }
            
            for (JocDTO j : jocuri) {
                if (!usedJocuri.containsKey(j)){
                    usedJocuri.put(j, 0);
                }
            }
            
            DefaultListModel<ActivitateDTO> am = sarcini.get((AnimatorDB) jComboBox2.getSelectedItem());
            DefaultListModel<JocDTO> jm = perechi.get(am);
            DefaultListModel<Object> os = sarciniOrdonate.get((AnimatorDB) jComboBox2.getSelectedItem());
            
            jList2.setModel(am);
            jList3.setModel(jm);
            jList4.setModel(os);
            jCheckBox1.setSelected(aplica.get((AnimatorDB) jComboBox2.getSelectedItem()));
            
            
            
            //updateListe(activitati, jocuri);
            salveaza();
        } else {            
            
            ////
            System.out.println("Distribuie Program : datele nu au putut fi incarcate. Se creeaza o noua fereastra...");
            comboModel2 = new DefaultComboBoxModel<>();
            
            sarcini = new HashMap<>();
            perechi = new HashMap<>();
            usedActivitati = new HashMap<>();
            usedJocuri = new HashMap<>();
            sarciniOrdonate = new HashMap<>();
            aplica = new HashMap<>();

            DefaultListModel<ActivitateDTO> model;
            for (AnimatorDB a : animatori){
                comboModel2.addElement(a);
                model = new DefaultListModel<>();
                sarcini.put(a,
                            model);
                perechi.put(model,
                            new DefaultListModel<JocDTO>());
                sarciniOrdonate.put(a, new DefaultListModel<Object>());
                aplica.put(a, false);
            }

            jComboBox2.setModel(comboModel2);
            try {
                jComboBox2.setSelectedItem(animatori.get(0));
            } catch (Exception e){
                JOptionPane.showMessageDialog(this, "Trebuie sa fie macar un animator disponibil");
                dispose();
                MainFrame.openFrame(new GestioneazaAnimatoriFrame(), false);
                throw new Exception();
            }
            
            for (ActivitateDTO a : activitati) {                
                usedActivitati.put(a, 0);                
            }
            
            for (JocDTO j : jocuri) {
                usedJocuri.put(j, 0);
            }
            
            model = sarcini.get((AnimatorDB) jComboBox2.getSelectedItem());
            DefaultListModel<JocDTO> aux = perechi.get(model);
            DefaultListModel<Object> os = sarciniOrdonate.get((AnimatorDB) jComboBox2.getSelectedItem());

            jList2.setModel(model);
            jList3.setModel(aux);
            jList4.setModel(os);
            jCheckBox1.setSelected(aplica.get((AnimatorDB) jComboBox2.getSelectedItem()));
            
            SerializeController.getInstance().setCanReadPosteazaFrame(true);
        }
        SerializeController.getInstance().setCanReadDistribuieFrame(true);
        
        jList1.setModel(this.activitati);
        cellRender = new MyListCellRenderer();
        jList1.setCellRenderer(cellRender);
        //jList1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        jList1.setFixedCellWidth(161);
        jList2.setFixedCellWidth(STANDARD_WIDTH);
        jList3.setFixedCellWidth(STANDARD_WIDTH);
        jList4.setFixedCellWidth(STANDARD_WIDTH);
        jComboBox2.setPrototypeDisplayValue("Nume_animator");
        
        jButton6.setText("<< Extinde >>");
        jPanel4.setVisible(false);
        jSeparator1.setVisible(false);
        jCheckBox1.setVisible(false);
        
        KeyListener [] listeners = jList4.getKeyListeners();
        for (int i = 0; i < listeners.length; ++i) {jList4.removeKeyListener(listeners[i]); }
        
        jList4.addKeyListener(new KeyAdapter () {

            @Override
            public void keyTyped(KeyEvent ke) {
                final int index = jList4.getSelectedIndex();
                final DefaultListModel<Object> model = (DefaultListModel<Object>) jList4.getModel();
        
                if (index == -1) return; 
                Object a = model.get(index);

                if (ke.getKeyChar() == KeyEvent.VK_W + 32) {
                    if (index > 0) {
                        if (model.removeElement(a)) {
                            model.add(index-1, a);
                            jList4.setSelectedIndex(index-1);
                            selectedIndex = index-1;
                            needSelect = true;
                        }
                    }
                } else if (ke.getKeyChar() == KeyEvent.VK_S + 32) {
                    if (index < model.size() - 1) {
                        if (model.removeElement(a)){
                            model.add(index+1, a);
                            jList4.setSelectedIndex(index+1);
                            selectedIndex = index+1;
                            needSelect = true;
                        }
                    }
                }

                jList4.setModel(model);
            }
            
        });
        
        jList4.addListSelectionListener(
                new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (needSelect) {
                    jList4.setSelectedIndex(selectedIndex);
                    needSelect = false;
                }
            }
        }
        );
        
        jButton7.setToolTipText("Distribuie activitatea tuturor animatorilor");
        setSize(COMPRIMATED_WIDTH, getHeight());
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void salveaza () {
        SerializableDistribuieProgramFrame sdpf = new SerializableDistribuieProgramFrame();
        sdpf.setActivitati(activitati);
        sdpf.setComboModel2(comboModel2);
        sdpf.setJocuri(jocuri);
        sdpf.setPerechi(perechi);
        sdpf.setSarcini(sarcini);
        sdpf.setUsedActivitati(usedActivitati);
        sdpf.setUserJocuri(usedJocuri);
        sdpf.setAplica(aplica);
        sdpf.setSarciniOrdonate(sarciniOrdonate);
        
        SerializeController.getInstance().serializaDistribuieProgramFrame(sdpf);
        SerializeController.getInstance().setCanReadDistribuieFrame(true);
        isChanged = false;
        
        ////
        System.out.println("Distribuie Program : salvare completa.");
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList();
        jSeparator1 = new javax.swing.JSeparator();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton7 = new javax.swing.JButton();

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
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboBox1MouseEntered(evt);
            }
        });
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setFixedCellWidth(159);
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList1MouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText(">");
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

        jButton2.setText("<");
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

        jButton5.setText("<< Inapoi");
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

        jLabel1.setText("Animator");

        jLabel2.setText("Activitati");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboBox2MouseEntered(evt);
            }
        });
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList2.setFixedCellWidth(200);
        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList2MouseEntered(evt);
            }
        });
        jScrollPane2.setViewportView(jList2);

        jButton4.setText("Mai departe >>");
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton4)))
        );

        jList3.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList3.setFixedCellWidth(222);
        jList3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList3MouseEntered(evt);
            }
        });
        jScrollPane3.setViewportView(jList3);

        jLabel3.setText("Jocuri");

        jButton6.setText("<< Extinde >>");
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

        jButton3.setText("Goleste");
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane3)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton3)))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Ordoneaza activitati"));

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        jList4.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList4MouseEntered(evt);
            }
        });
        jScrollPane4.setViewportView(jList4);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
        );

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jCheckBox1.setText("Aplica programul");
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseEntered(evt);
            }
        });
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton7.setText(">>");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(69, 69, 69)
                                    .addComponent(jButton7)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton1)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton2))
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(45, 45, 45)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(24, 24, 24)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (((String) jComboBox1.getSelectedItem()).equals("Activitati")){
            jList1.setModel(activitati);
        } else {
            jList1.setModel(jocuri);
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        MainFrame.openFrame(new PosteazaProgramFrame(), true);
        dispose();
        salveaza();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        DefaultListModel<ActivitateDTO> model;
        model = sarcini.get((AnimatorDB) jComboBox2.getSelectedItem());
        DefaultListModel<JocDTO> aux = perechi.get(model);
        DefaultListModel<Object> os = sarciniOrdonate.get((AnimatorDB) jComboBox2.getSelectedItem());
        
        jList2.setModel(model);
        jList3.setModel(aux);
        jList4.setModel(os);
        jCheckBox1.setSelected(aplica.get((AnimatorDB) jComboBox2.getSelectedItem()));
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jList1.getSelectedValue() != null) {  // adaugarea propriu-zisa a animatorilor se va face in metoda jButton4ActionPerformed
            //isChanged = true;
            if (((String) jComboBox1.getSelectedItem()).equals("Activitati")) {
                int indicies [] = jList1.getSelectedIndices();                
                
                for (int counter = 0; counter < indicies.length; ++counter) {
                    ActivitateDTO a = (ActivitateDTO) activitati.getElementAt(indicies[counter]);
                    AnimatorDB animator = (AnimatorDB) jComboBox2.getSelectedItem();                
                    DefaultListModel<ActivitateDTO> am = sarcini.get(animator);
                    DefaultListModel<Object> os = sarciniOrdonate.get(animator);
                    if (!am.contains(a)){
                        //a.adaugaAnimator(ControllerDB.getInstance().convert(animator));
                        int i = usedActivitati.get(a); // numaram distribuirea activitatii
                        usedActivitati.remove(a);
                        i++;
                        usedActivitati.put(a, i);
                        jList1.setModel(activitati);
                        jList1.setCellRenderer(cellRender);
                        am.addElement(a);
                        os.addElement(a);
                        jList2.setModel(am);
                        jList4.setModel(os);
                    }
                }
            } else {
                int indicies [] = jList1.getSelectedIndices();
                
                for (int counter = 0; counter < indicies.length; ++counter) {
                    JocDTO j = (JocDTO) jocuri.getElementAt(indicies[counter]);
                    AnimatorDB animator = (AnimatorDB) jComboBox2.getSelectedItem();
                    DefaultListModel<ActivitateDTO> am = sarcini.get(animator);
                    DefaultListModel<JocDTO> jm = perechi.get(am);
                    DefaultListModel<Object> os = sarciniOrdonate.get(animator);
                    if (!jm.contains(j)){
                        //j.adaugaAnimator(ControllerDB.getInstance().convert(animator));
                        int i = usedJocuri.get(j); // numaram distribuirea jocului
                        usedJocuri.remove(j);
                        i++;
                        usedJocuri.put(j, i);
                        jList1.setModel(jocuri);
                        jList1.setCellRenderer(cellRender);
                        jm.addElement(j);
                        os.addElement(j);
                        jList3.setModel(jm);
                        jList4.setModel(os);
                    }
                }
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {  // adaugarea propriu-zisa a animatorilor se va face in metoda jButton4ActionPerformed
            isChanged = true;
            if (((String) jComboBox1.getSelectedItem()).equals("Activitati")){
                AnimatorDB animator = (AnimatorDB) jComboBox2.getSelectedItem();
                DefaultListModel<ActivitateDTO> am = sarcini.get(animator);
                DefaultListModel<Object> os = sarciniOrdonate.get(animator);
                
                int indicies [] = jList2.getSelectedIndices();
                for (int counter = indicies.length - 1; counter >= 0 ; --counter) {
                    ActivitateDTO activitate = (ActivitateDTO) am.getElementAt(indicies[counter]);
                    am.removeElement(activitate);
                    os.removeElement(activitate);
                    int i = usedActivitati.get(activitate); // decrementam numarul de distribuiri ale acitivitatii
                    usedActivitati.remove(activitate);
                    i--;
                    usedActivitati.put(activitate, i);
                    jList1.setModel(activitati);
                    
                    
                }
                jList1.setSelectedIndices(indicies);
                jList2.setModel(am);
                jList4.setModel(os);
                //activitate.stergeAnimator(ControllerDB.getInstance().convert(animator));
            } else {
                AnimatorDB animator = (AnimatorDB) jComboBox2.getSelectedItem();
                DefaultListModel<ActivitateDTO> am = sarcini.get(animator);                
                DefaultListModel<JocDTO> jm = perechi.get(am);
                DefaultListModel<Object> os = sarciniOrdonate.get(animator);
                
                int indicies[] = jList3.getSelectedIndices();
                for (int counter = indicies.length - 1; counter >= 0; --counter) {
                    JocDTO joc = (JocDTO) jm.getElementAt(indicies[counter]);
                    jm.removeElement(joc);
                    os.removeElement(joc);
                    int i = usedJocuri.get(joc); // decrementam numarul de distribuiri ale jocului
                    usedJocuri.remove(joc);
                    i--;
                    usedJocuri.put(joc, i);
                    jList1.setModel(jocuri);                    
                }
                jList1.setSelectedIndices(indicies);
                jList3.setModel(jm);
                jList4.setModel(os);
                //joc.stergeAnimator(ControllerDB.getInstance().convert(animator));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        List<ActivitateDTO> list1 = new ArrayList<>();
        List<JocDTO> list2 = new ArrayList<>();
        
        DefaultListModel<ActivitateDTO> ma;
        DefaultListModel<JocDTO> mj;
        ActivitateDTO a;
        JocDTO j;
        AnimatorDB anim;
        
        for (int i = 0; i < comboModel2.getSize(); ++i) {
            anim = comboModel2.getElementAt(i);
            ma = sarcini.get(anim);
            
            
            for (int k = 0; k < ma.size(); ++k) {
                ma.get(k).getAnimatori().clear();
            }           
            
            mj = perechi.get(ma);
            
            for (int k = 0; k < mj.size(); ++k) {
                mj.get(k).getAnimatori().clear();
            }
        }
        
        for (int i = 0; i < comboModel2.getSize(); ++i) {
            anim = comboModel2.getElementAt(i);
            AnimatorDTO animdto = ControllerDB.getInstance().convert(anim);
            // adaugam sarcinile animatorului
            DefaultListModel<Object> os = sarciniOrdonate.get(anim);
            if (aplica.get(anim)) {
                List<Object> list = new ArrayList<>();
                for (int k = 0; k < os.size(); ++k) {
                    list.add(os.get(k));
                }
                animdto.setSarcini(list);
            }
            
            ma = sarcini.get(anim);
            
            
            for (int k = 0; k < ma.size(); ++k) {
                //if (!ma.get(k).getAnimatori().contains(ControllerDB.getInstance().convert(anim))) {
                //    ma.get(k).getAnimatori().add(ControllerDB.getInstance().convert(anim));
                //}
                ma.get(k).getAnimatori().add(animdto);
            }            
            
            mj = perechi.get(ma);
            
            for (int k = 0; k < mj.size(); ++k) {
                //if (!mj.get(k).getAnimatori().contains(ControllerDB.getInstance().convert(anim))) {
                //    mj.get(k).getAnimatori().add(ControllerDB.getInstance().convert(anim));
                //}
                mj.get(k).getAnimatori().add(animdto);
            } 
            
        }
        
        for (int i = 0; i < comboModel2.getSize(); ++i) {
            anim = comboModel2.getElementAt(i);
            ma = sarcini.get(anim);
            
            for (int k = 0; k < ma.size(); ++k) {
                boolean exists = false;
                a = ma.get(k);
                
                for (int l = 0; l < list1.size() && !exists; ++l){
                    ActivitateDTO a2 = list1.get(l);
                    if (a.getActivitateGenerala().equals(a2.getActivitateGenerala()) && a.getPerioada().equals(a2.getPerioada())){
                        exists = true;
                        /*if (a2.getAnimatori().size() < a.getAnimatori().size()) {
                            a2.setAnimatori(a.getAnimatori());
                        } else {
                            a.setAnimatori(a2.getAnimatori());
                        }*/
                        
                        for (AnimatorDTO animator : a.getAnimatori()){
                            if (!a2.getAnimatori().contains(animator)){
                                a2.adaugaAnimator(animator);
                            }
                        }
                    }
                }
                
                if (!exists) {
                    list1.add(a);
                }
            }
            
            mj = perechi.get(ma);
            
            for (int k = 0; k < mj.size(); ++k){
                boolean exists = false;
                j = mj.get(k);
                
                for (int l = 0; l < list2.size() && !exists; ++l){
                    JocDTO j2 = list2.get(l);
                    if (j.getJocGeneral().equals(j2.getJocGeneral()) && j.getPerioada().equals(j2.getPerioada())) {
                        exists = true;
                        /*if (j2.getAnimatori().size() < j.getAnimatori().size()) {
                            j2.setAnimatori(j.getAnimatori());
                        } else {
                            j.setAnimatori(j2.getAnimatori());
                        }*/
                        for (AnimatorDTO animator : j.getAnimatori()) {
                            if (!j2.getAnimatori().contains(animator)) {
                                j2.adaugaAnimator(animator);
                            }
                        }
                    }
                }
                
                if (!exists) {
                    list2.add(j);
                }
            }
        }
        
//        for (int i = 0; i < activitati.size(); ++i){
//            list1.add(activitati.get(i));
//        }
//        
//        for (int i = 0; i < jocuri.size(); ++i){
//            list2.add(jocuri.get(i));
//        }
         //verificam sa existe cel putin un animator pt fiecare joc
        for (JocDTO joc : list2) {
            if (joc.getAnimatori().isEmpty()) {
                JOptionPane.showMessageDialog(null, "<html>Fiecare joc trebuie sa fie distribuit cel putin unui animator. <br>"
                        + "Jocul " + joc + " trebuie distribuit", "Joc nedistribuit", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        new AdaugaInformatiiFrame(list1, list2).setVisible(true);
        dispose();
        salveaza();
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String extend = "<< Extinde >>";
        String comprim = ">> Comprima <<";
        
        if (jButton6.getText().equals(extend)){
            jButton6.setText(comprim);
            jPanel4.setVisible(true);
            jSeparator1.setVisible(true);
            jCheckBox1.setVisible(true);
            setSize(EXTENDED_WIDTH, getHeight());
        } else {
            jButton6.setText(extend);
            jPanel4.setVisible(false);
            jSeparator1.setVisible(false);
            jCheckBox1.setVisible(false);
            setSize(COMPRIMATED_WIDTH, getHeight());
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        aplica.put((AnimatorDB) jComboBox2.getSelectedItem(), jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        salveaza();
    }//GEN-LAST:event_formWindowClosing

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jComboBox1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox1MouseEntered
        HelperFrame.getInstance().post(
                "Tip de activitate",
                "Meniul din care se poate selecta tipul activitatilor care sa se afiseze in lista de mai jos.");
    }//GEN-LAST:event_jComboBox1MouseEntered

    private void jList1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseEntered
        HelperFrame.getInstance().post(
                "Lista activitatilor",
                "De aici se pot selecta diverse activitati pentru a le distribui animatorilor. Culoarea acestor activitati are urmatoarele semnificatii: \n"
                        + " *alb - activitatea nu a fost distribuita inca; \n"
                        + " *verde - activitatea a fost distribuita catre cel putin un animator; \n"
                        + " *albastru - ai selectat aceasta activitate;");
    }//GEN-LAST:event_jList1MouseEntered

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        HelperFrame.getInstance().post(
                "Fereastra de distribuire a activitatilor catre animatori",
                "In aceasta fereastra se poate decide care animatori vor fi raspunzatori de buna desfasurare "
                        + "a fiecarei activitati din program. Orice sarcina trebuie sa fie plasa in lista de sarcini "
                        + "a cel putin un animator. Numarul de organizatori ai unei activitati poate fi nelimitat (si nu este recomandat)."
                        + "Mai mult, in aceasta fereastra se poate stabili o ordine de desfasurare particulara a activitatilor pentru "
                        + "fiecare animator in parte, din meniul 'Extinde'->'Aplica Programul'. Daca animatorului selectat nu i se "
                        + "specifica nicio ordine particulara, acesta va rezolva activitatile in ordinea standard.");
    }//GEN-LAST:event_formMouseEntered

    private void jComboBox2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox2MouseEntered
        HelperFrame.getInstance().post(
                "Meniu animatori",
                "Din acest meniu putem selecta animatorul caruia vrem sa-i distribuim activitatea.");
    }//GEN-LAST:event_jComboBox2MouseEntered

    private void jList2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseEntered
        HelperFrame.getInstance().post(
                "Activitati animator",
                "Reprezinta lista activitatilor animatorului selectat. Se pot selecta elemente multiple folosind tastele 'Ctrl' si 'Shift'.");
    }//GEN-LAST:event_jList2MouseEntered

    private void jList3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList3MouseEntered
        HelperFrame.getInstance().post(
                "Jocuri animator",
                "Reprezinta lista jocurilor animatorului selectat. Se pot selecta elemente multiple folosind tastele 'Ctrl' si 'Shift'.");
    }//GEN-LAST:event_jList3MouseEntered

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        HelperFrame.getInstance().post(
                "Plasare activitate",
                "Plaseaza activitatea selectata in lista specifica a animatorului selectat.");
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        HelperFrame.getInstance().post(
                "Revoca activitate",
                "Anuleaza activitatea/activitatile plasate in lista de sarcini ale animatorului, plasandu-le inapoi in lista activitatilor "
                        + "de distribuit. \n ATENTIE: anularea nu va functiona daca tipul activitatii revocate nu coincide cu optiunea selectata "
                        + "din meniul tipurilor de activitati (cel din stanga-sus)");
    }//GEN-LAST:event_jButton2MouseEntered

    private void jButton6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseEntered
        HelperFrame.getInstance().post(
                "Extindere/Comprimare",
                "Arata sau ascunde meniul de ordonare particulara a activitatilor animatorului selectat.");
    }//GEN-LAST:event_jButton6MouseEntered

    private void jList4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList4MouseEntered
        HelperFrame.getInstance().post(
                "Lista activitatilor ordonate",
                "Aceasta lista permite reordonarea activitatilor animatorului selectat in mod arbitrar. Pentru a realiza aceasta,"
                        + " se selecteaza activitatea dorita, si folosind tastele 'W' (sus) si 'S' (jos), plasam activitatea "
                        + "pe pozitia dorita. \n"
                        + "DE RETINUT: pentru a se aplica aceasta ordine (valabila doar pentru animatorului selectat), trebuie bifata casuta"
                        + " 'Aplica programul'.");
    }//GEN-LAST:event_jList4MouseEntered

    private void jCheckBox1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseEntered
        HelperFrame.getInstance().post(
                "Aplicarea reordonarii",
                "In cazul in care este bifata, animatorul selectat va realiza activitatile distribuite dupa ordinea data in lista de mai sus.");
    }//GEN-LAST:event_jCheckBox1MouseEntered

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
        HelperFrame.getInstance().post(
                "Pasul urmator",
                "Ai terminat aici? Atunci este momentul sa treci la urmatorul pas!");
    }//GEN-LAST:event_jButton4MouseEntered

    private void jButton5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseEntered
        HelperFrame.getInstance().post(
                "Pasul anterior",
                "Vrei sa mai adaugi o activitate sau sa modifici una deja existenta? Sigur! Este necesar doar un click pe acest buton!");
    }//GEN-LAST:event_jButton5MouseEntered

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int input = JOptionPane.showConfirmDialog(this, "Sigur doresti sa stergi continutul acestei ferestre?", "Resetare", 
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (input != JOptionPane.YES_OPTION) return;
        
        SerializeController.getInstance().serializaDistribuieProgramFrame(null);
        dispose();
        try {
            MainFrame.openFrame(new DistribuieProgramFrame(paramActivitati, paramJocuri), true);
        } catch (Exception ex) {
            Logger.getLogger(DistribuieProgramFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        HelperFrame.getInstance().post(
                "Buton golire",
                "Curata intreg continutul creat de utilizator din aceasta fereastra.");
    }//GEN-LAST:event_jButton3MouseEntered

    private void jButton7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7MouseEntered

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        if (jList1.getSelectedIndex() == -1) return;
        final AnimatorDB selected = (AnimatorDB) comboModel2.getSelectedItem();
        
        int indicies [] = jList1.getSelectedIndices(); 
        
        for (int ind = 0; ind < comboModel2.getSize(); ++ind) {
            AnimatorDB animator = comboModel2.getElementAt(ind);
            if (((String) jComboBox1.getSelectedItem()).equals("Activitati")) {               
                
                for (int counter = 0; counter < indicies.length; ++counter) {
                    ActivitateDTO a = (ActivitateDTO) activitati.getElementAt(indicies[counter]);             
                    DefaultListModel<ActivitateDTO> am = sarcini.get(animator);
                    DefaultListModel<Object> os = sarciniOrdonate.get(animator);
                    if (!am.contains(a)){
                        //a.adaugaAnimator(ControllerDB.getInstance().convert(animator));
                        int i = usedActivitati.get(a); // numaram distribuirea activitatii
                        usedActivitati.remove(a);
                        i++;
                        usedActivitati.put(a, i);
                        jList1.setModel(activitati);
                        jList1.setCellRenderer(cellRender);
                        am.addElement(a);
                        os.addElement(a);
                    }
                }
            } else {
                
                for (int counter = 0; counter < indicies.length; ++counter) {
                    JocDTO j = (JocDTO) jocuri.getElementAt(indicies[counter]);
                    DefaultListModel<ActivitateDTO> am = sarcini.get(animator);
                    DefaultListModel<JocDTO> jm = perechi.get(am);
                    DefaultListModel<Object> os = sarciniOrdonate.get(animator);
                    if (!jm.contains(j)){
                        //j.adaugaAnimator(ControllerDB.getInstance().convert(animator));
                        int i = usedJocuri.get(j); // numaram distribuirea jocului
                        usedJocuri.remove(j);
                        i++;
                        usedJocuri.put(j, i);
                        jList1.setModel(jocuri);
                        jList1.setCellRenderer(cellRender);
                        jm.addElement(j);
                        os.addElement(j);
                    }
                }
            }
            
        }
//        DefaultListModel<ActivitateDTO> am = sarcini.get(selected);
//        DefaultListModel<JocDTO> jm = perechi.get(am);
//        DefaultListModel<Object> os = sarciniOrdonate.get(selected);
//        jList2.setModel(am);
//        jList3.setModel(jm);
//        jList4.setModel(os);
//        
        
    }//GEN-LAST:event_jButton7ActionPerformed

    private void updateListe (List<ActivitateDTO> activitati, List<JocDTO> jocuri){ // parametrii care sunt parsati prin constructor
        for (ActivitateDTO a : activitati) { // cautam elementele care au fost recent adaugate, si updatam listele
            if (this.activitati.indexOf(a) != -1){
                this.activitati.addElement(a);
            }
        }
        
        for (JocDTO j : jocuri) {
            if (this.jocuri.indexOf(j) == -1) {
                this.jocuri.addElement(j);
            }
        }
        
        for (int i = 0; i < this.activitati.size(); ++i) { // cautam elementele ce au fost recent eliminate, si updatam listele
            if (!activitati.contains(this.activitati.get(i))){
                removeActivitateFromEverywhere(this.activitati.get(i));
            }
        }
        
        for (int i = 0; i < this.jocuri.size(); ++i) {
            if (!jocuri.contains(this.jocuri.get(i))){
                removeJocFromEverywhere(this.jocuri.get(i));
            }
        }
    }
    
    // cauta si sterge orice aparitie a activitatii a
    private void removeActivitateFromEverywhere(ActivitateDTO a) {
        List<AnimatorDB> animatori = ControllerDB.getInstance().getAnimatoriByDisponibilitate(true);
        
        for (AnimatorDB anim : animatori){
            sarcini.get(anim).removeElement(a);
        }
        
        this.activitati.removeElement(a);
    }
    
    private void removeJocFromEverywhere (JocDTO j) {
        List<AnimatorDB> animatori = ControllerDB.getInstance().getAnimatoriByDisponibilitate(true);
        
        for (AnimatorDB anim : animatori) {
            perechi.get(sarcini.get(anim)).removeElement(j);
        }
        
        this.jocuri.removeElement(j);
    }
    
    private void clear () {
        
    }
    
    private class MyListCellRenderer extends DefaultListCellRenderer {
      private static final int PREF_W = 50;
      private static final int MAX_INT_VALUE = 10;
      private final Color HIGH_VALUE_FG = Color.white;
      private final Color HIGH_VALUE_BG = Color.getHSBColor((float) 0.375, (float) 0.711, (float) 0.882);
      private final Color STANDARD_VALUE_FG = Color.BLACK;
      private final Color STANDARD_VALUE_BG = Color.WHITE;
      private final Color SELECTED_VALUE_BG = Color.getHSBColor((float) 0.667,(float) 0.756, (float) 0.804);

      @Override
      public Dimension getPreferredSize() {
         Dimension superSize = super.getPreferredSize();
         return new Dimension(PREF_W, superSize.height);
      }

      @Override
      public Component getListCellRendererComponent(JList<?> list,
            Object value, int index, boolean isSelected, boolean cellHasFocus) {
         Component superRenderer = super.getListCellRendererComponent(list, value, index, isSelected,
               cellHasFocus);

         if (jComboBox1.getSelectedItem() == "Activitati") {
             if (usedActivitati.get((ActivitateDTO) value) > 0) {
                 setBackground(HIGH_VALUE_BG);
                 setForeground(HIGH_VALUE_FG);
             } else {
                 setBackground(STANDARD_VALUE_BG);
                 setForeground(STANDARD_VALUE_FG);
             }
         } else {
             if (usedJocuri.get((JocDTO) value) > 0) {
                 setBackground(HIGH_VALUE_BG);
                 setForeground(HIGH_VALUE_FG);
             } else {
                 setBackground(STANDARD_VALUE_BG);
                 setForeground(STANDARD_VALUE_FG);
             }
         }
         
         if (cellHasFocus) {
             setBackground(SELECTED_VALUE_BG);
             setForeground(HIGH_VALUE_FG);
         }

         return superRenderer;
      }
   }
    
    public void openPreviewMode () {
        String comprim = ">> Comprima <<";
        jButton5.setEnabled(false);
        jButton6.setText(comprim);
        jPanel4.setVisible(true);
        jSeparator1.setVisible(true);
        jCheckBox1.setVisible(true);
        setSize(EXTENDED_WIDTH, getHeight());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JList jList4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
