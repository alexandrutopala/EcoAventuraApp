/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import costumSerialization.SerializableDistribuieEchipeFrame;
import db.EchipaDB;
import db.JocDB;
import dialogs.PermissionFrame;
import dto.ActivitateDTO;
import dto.AnimatorDTO;
import dto.EchipaDTO;
import dto.JocDTO;
import dto.JocGeneralDTO;
import dto.UserDTO;
import formula.Formula;
import static gui.SerieActivaFrame.timer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import server.ServerFrame;
import service.ControllerDB;
import service.SerializeController;

/**
 *
 * @author Alexandru
 */
public class DistribuieEchipeFrame extends javax.swing.JFrame {
    private final static DefaultListModel<EchipaDB> EMPTY_MODEL = new DefaultListModel<>();
    private final static int MAX_WIDTH = 213;
    private List<ActivitateDTO> activitati;
    private List<JocDTO> jocuri;
    private List<EchipaDB> echipe;
    private DefaultListModel<EchipaDB> modelEchipe;
    private DefaultComboBoxModel<String> model;
    private DefaultComboBoxModel<ActivitateDTO> comboActivitati;
    private DefaultComboBoxModel<JocDTO> comboJocuri;
    private HashMap<ActivitateDTO, DefaultListModel<EchipaDB> > activitatiMap;
    private HashMap<JocDTO, DefaultListModel<EchipaDB> > jocuriMap;
    private boolean isEmpty = true;
    private AdaugaInformatiiFrame aif;
    
    private List<ActivitateDTO> paramActivitati;
    private List<JocDTO> paramJocuri;
    
    /**
     * Creates new form DistribuieEchipeFrame
     * @param activitati
     * @param jocuri
     * @param aif
     */
    public DistribuieEchipeFrame(List<ActivitateDTO> activitati, List<JocDTO> jocuri, AdaugaInformatiiFrame aif) {
        super ("Distribuie echipe");
        initComponents();
        isEmpty = false;
        
        paramActivitati = new ArrayList<>(activitati);
        paramJocuri = new ArrayList<>(jocuri);
        
        model = new DefaultComboBoxModel<>(new String[] {"Activitati", "Jocuri"});
        
        modelEchipe = new DefaultListModel<>();
                
        this.activitati = activitati;
        this.jocuri = jocuri;
        this.aif = aif;
        
        echipe = ControllerDB.getInstance().getEchipeBySerie(SerieActivaFrame.getSerie());
        
        for (EchipaDB e : echipe) {
            modelEchipe.addElement(e);
        }
        
        SerializableDistribuieEchipeFrame sdef = SerializeController.getInstance().deserializeDistribuieEchipeFrame();
        
        // TODO: sterge echipele care au realizat deja activitatea selectata pe ziua de azi, desfasurata in aceeasi perioada (elimina duplicatele)
        // TODO: elimina activitatile inexistente
        
        if (sdef != null && SerializeController.getInstance().isCanReadDistribuieEchipeFrame()) {
            comboActivitati = sdef.getComboActivitati();
            comboJocuri = sdef.getComboJocuri();
            activitatiMap = sdef.getActivitatiMap();
            jocuriMap = sdef.getJocuriMap();
            
            //updatam modelele combo
            
            comboActivitati.removeAllElements();
            comboJocuri.removeAllElements();
            
            for (ActivitateDTO a : activitati) {
                comboActivitati.addElement(a);
            }
            
            for (JocDTO j : jocuri) {
                comboJocuri.addElement(j);
            }
            
            // updatam activitatile, distribuind echipele din salvarea anterioara
            HashMap<ActivitateDTO, DefaultListModel<EchipaDB>> auxActivitateMap = new HashMap<>();
            HashMap<JocDTO, DefaultListModel<EchipaDB>> auxJocMap = new HashMap<>();
            
            for (ActivitateDTO a : activitati) {
                if (activitatiMap.containsKey(a)) {
                    auxActivitateMap.put(a, activitatiMap.get(a));
                } else {
                    auxActivitateMap.put(a, new DefaultListModel<EchipaDB>());
                }
            }
            
            for (JocDTO j : jocuri) {
                if (jocuriMap.containsKey(j)) {
                    auxJocMap.put(j, jocuriMap.get(j));
                } else {
                    auxJocMap.put(j, new DefaultListModel<EchipaDB>());
                }
            }
            
            activitatiMap = auxActivitateMap;
            jocuriMap = auxJocMap;
            
            // stergem echipele care au realizat activitatea selectata o data, in aceeasi zi si in aceeasi perioada
            String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
            
            //TODO: sterge activitatile duplicate numai daca fac parte din planuri de activitati diferite
            if (isNewProgram() && false) {        
                for (ActivitateDTO a : activitatiMap.keySet()) {
                    DefaultListModel<EchipaDB> me = activitatiMap.get(a);
                    for (int i = 0; i < me.getSize(); ++i) {
                        boolean playedToday = ControllerDB.getInstance().getUnicActivitate(
                                ControllerDB.getInstance().convert(a.getActivitateGenerala()),
                                me.get(i), 
                                date,
                                a.getPerioada()) != null;
                        if (playedToday) {
                            me.remove(i);
                            i--;
                        }
                    }
                }


                for (JocDTO a : jocuriMap.keySet()) {
                    DefaultListModel<EchipaDB> me = jocuriMap.get(a);
                    for (int i = 0; i < me.getSize(); ++i) {
                        boolean playedToday = ControllerDB.getInstance().getUnicJoc(
                                ControllerDB.getInstance().convert(a.getJocGeneral()),
                                me.get(i), 
                                date,
                                a.getPerioada()) != null;
                        if (playedToday) {
                            me.remove(i);
                            i--;
                        }
                    }
                }
            }
        } else {
            comboActivitati = new DefaultComboBoxModel<>();
            comboJocuri = new DefaultComboBoxModel<>();
            activitatiMap = new HashMap<>();
            jocuriMap = new HashMap<>();

            for (ActivitateDTO a : activitati) {
                comboActivitati.addElement(a);
                activitatiMap.put(a, new DefaultListModel<EchipaDB>());
            }

            for (JocDTO j : jocuri){
                comboJocuri.addElement(j);
                jocuriMap.put(j, new DefaultListModel<EchipaDB>());
            }
            
            SerializeController.getInstance().setCanReadDistribuieEchipeFrame(true);
        }
        
        
        jCheckBox1.setEnabled(false);
        jCheckBox1.setSelected(false);
        if (comboActivitati.getSize() > 0) {
            model.setSelectedItem("Activitati");
            comboActivitati.setSelectedItem(this.activitati.get(0));
            jComboBox1.setModel(model);
            jComboBox2.setModel(comboActivitati);
            jList2.setModel(activitatiMap.get(this.activitati.get(0)));
        } else if (comboJocuri.getSize() > 0) {
            model.setSelectedItem("Jocuri");
            jButton5.setEnabled(true);
            jCheckBox1.setEnabled(true);
            comboJocuri.setSelectedItem(this.jocuri.get(0));
            jComboBox1.setModel(model);
            jComboBox2.setModel(comboJocuri);
            jList2.setModel(jocuriMap.get(this.jocuri.get(0)));
        } else {
            jComboBox1.setModel(model);
            jComboBox2.setModel(new DefaultComboBoxModel());
            jList2.setModel(new DefaultListModel());
            isEmpty = true;
            jButton1.setEnabled(false);
            jButton2.setEnabled(false);
            jButton5.setEnabled(false);
        }
        
        jList1.setFixedCellWidth(MAX_WIDTH);
        jList1.setModel(modelEchipe);
        jList1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jList2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        
        jButton5.setToolTipText("Adauga doar echipele care nu au mai facut jocul");
        jCheckBox1.setToolTipText("Avertizeaza-ma de fiecare data cand distribui unei echipe un joc pe care l-a realizat deja");
        jCheckBox1.setSelected(true);
        
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
    }

    public boolean salveaza () {
        SerializableDistribuieEchipeFrame sdef = new SerializableDistribuieEchipeFrame();
        sdef.setActivitatiMap(activitatiMap);
        sdef.setComboActivitati(comboActivitati);
        sdef.setComboJocuri(comboJocuri);
        sdef.setJocuriMap(jocuriMap);
        sdef.setActivitati(activitati);
        sdef.setJocuri(jocuri);
        return SerializeController.getInstance().serializeDistribuieEchipeFrame(sdef);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Echipe");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList1MouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

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
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList2MouseEntered(evt);
            }
        });
        jScrollPane2.setViewportView(jList2);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox2, 0, 168, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton1.setText(">>");
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

        jButton2.setText("<<");
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

        jButton3.setText("<< Inapoi");
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

        jButton4.setText("Posteaza>>");
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

        jButton5.setText(">>>");
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

        jCheckBox1.setText("Preventie duplicate");
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseEntered(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton6.setText("Goleste");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton6)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(jButton5)
                        .addGap(36, 36, 36)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jCheckBox1)
                    .addComponent(jButton6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (jComboBox1.getSelectedItem().equals("Activitati")){
            jComboBox2.setModel(comboActivitati);
            
            jCheckBox1.setEnabled(false);
            if (comboActivitati.getSize() == 0) {
                jButton1.setEnabled(false);
                jButton2.setEnabled(false);
                jButton5.setEnabled(false);
                jList2.setModel(EMPTY_MODEL);
            } else {
                jButton1.setEnabled(true);
                jButton2.setEnabled(true);
                jButton5.setEnabled(true);
                jComboBox2ItemStateChanged(evt);
            }
        } else if (jComboBox1.getSelectedItem().equals("Jocuri")) {
            jComboBox2.setModel(comboJocuri);
            jButton5.setEnabled(true);
            jCheckBox1.setEnabled(true);
            if (comboJocuri.getSize() == 0) {
                jButton1.setEnabled(false);
                jButton2.setEnabled(false);
                jButton5.setEnabled(false);
                jList2.setModel(EMPTY_MODEL);
            } else {
                jButton1.setEnabled(true);
                jButton2.setEnabled(true);
                jButton5.setEnabled(true);
                jComboBox2ItemStateChanged(evt);
            }
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        if (!isEmpty){
            if (jComboBox1.getSelectedItem().equals("Activitati")){
                jList2.setModel(activitatiMap.get((ActivitateDTO) comboActivitati.getSelectedItem()));
            } else if (jComboBox1.getSelectedItem().equals("Jocuri")){
                jList2.setModel(jocuriMap.get((JocDTO) comboJocuri.getSelectedItem()));
            }
        }
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int [] indicies = jList1.getSelectedIndices();
        DefaultListModel<EchipaDB> aux = (DefaultListModel<EchipaDB>) jList2.getModel();
        String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        final boolean isNewProgram = isNewProgram();
        final long ID = SerializeController.getInstance().getProgramActivitatiID();
        
        for (int i = 0; i < indicies.length; ++i){
            boolean playedToday = false;
            String nume;
            if (!isNewProgram()) { 
                if (model.getSelectedItem().equals("Activitati")) {
                    playedToday = (ControllerDB.getInstance().getUnicActivitate(
                            ControllerDB.getInstance().convert(((ActivitateDTO)comboActivitati.getSelectedItem()).getActivitateGenerala()),
                            (EchipaDB) modelEchipe.get(indicies[i]),
                            date,
                            ((ActivitateDTO) comboActivitati.getSelectedItem()).getPerioada()) != null);
                    nume = ((ActivitateDTO) comboActivitati.getSelectedItem()).toString();
                } else {
                    playedToday = (ControllerDB.getInstance().getUnicJoc(
                            ControllerDB.getInstance().convert(((JocDTO)comboJocuri.getSelectedItem()).getJocGeneral()),
                            (EchipaDB) modelEchipe.get(indicies[i]),
                            date,
                            ((JocDTO) comboJocuri.getSelectedItem()).getPerioada()) != null);
                    nume = ((JocDTO) comboJocuri.getSelectedItem()).toString();
                }

                if (playedToday) {
                    JOptionPane.showMessageDialog(this, "Echipa " + ((EchipaDB) modelEchipe.get(indicies[i])) +
                            " a realizat deja activitatea " + nume + " in programul de activitati actual",
                            "Activitate duplicata", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
            }
            
            if (jCheckBox1.isSelected()) {
                EchipaDB e = (EchipaDB) modelEchipe.get(indicies[i]);
                if (playedBefore(e)) {
                    int input = JOptionPane.showConfirmDialog(this, "Jocul " + ((JocDTO) jComboBox2.getSelectedItem()) + " a fost deja jucat de echipa " + e + " in aceasta serie. Continui?", "Atentie", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (input != JOptionPane.YES_OPTION) {
                        continue;
                    }
                }
            }
            if (!aux.contains((EchipaDB) modelEchipe.get(indicies[i]))){
                aux.addElement((EchipaDB) modelEchipe.get(indicies[i]));
            }
        }
        
        jList2.setModel(aux);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        DefaultListModel<EchipaDB> aux = (DefaultListModel<EchipaDB>) jList2.getModel();
        int indicies[] = jList2.getSelectedIndices();
        
        for (int i = indicies.length - 1; i >= 0; --i) {
            aux.remove(indicies[i]);
        }
        jList2.setModel(aux);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        PermissionFrame perm = new PermissionFrame(this, true, "Confirmare distribuire sarcini", UserDTO.ACCES_COORDONATOR);
        
        
        if (!perm.isApproved()) {
            JOptionPane.showMessageDialog(this, "Acces interzis", "Eroare de autentificare", JOptionPane.ERROR_MESSAGE);
            return;
        } 
        dispose();
        
        List<EchipaDTO> auxEchipe;
        DefaultListModel<EchipaDB> auxEchipeDb;
        for (ActivitateDTO a : activitati) {
            auxEchipe = new ArrayList<>();
            auxEchipeDb = activitatiMap.get(a);
            for (int i = 0; i < auxEchipeDb.getSize(); ++i) {
                auxEchipe.add(ControllerDB.getInstance().convert(auxEchipeDb.get(i)));
            }
            // adaugam echipele si la sarcinile animatorului
            for (AnimatorDTO anim : a.getAnimatori()) {
                if (anim.getSarcini() != null) { // doar daca s-a facut o impartire separata
                    int index = anim.getSarcini().indexOf(a);
                    ActivitateDTO animActivitate = ((ActivitateDTO) anim.getSarcini().get(index));
                    animActivitate.setEchipe(auxEchipe);
                    animActivitate.setDetalii(a.getDetalii());
                    animActivitate.setLocatie(a.getLocatie());
                    animActivitate.setPost(a.getPost());
                }
            }
            a.setEchipe(auxEchipe);
        }
        
        for (JocDTO j : jocuri) {
            auxEchipe = new ArrayList<>();
            auxEchipeDb = jocuriMap.get(j);
            if (j.getFormulas() == null){
                j.setFormulas(new HashMap<EchipaDTO, Formula>());
            }
            for (int i = 0; i < auxEchipeDb.getSize(); ++i) {
                EchipaDTO e = ControllerDB.getInstance().convert(auxEchipeDb.get(i));
                auxEchipe.add(e);
                if (j.getFormulas().get(e) == null) {
                    Formula formula = SerializeController.getInstance().getFormula(ControllerDB.getInstance().convert(j.getJocGeneral()));
                    j.getFormulas().put(e, formula);
                }
            }
            
            // adaugam echipele si la sarcinile animatorului
            for (AnimatorDTO anim : j.getAnimatori()) {
                if (anim.getSarcini() != null) { // doar daca s-a facut o impartire separata
                    int index = anim.getSarcini().indexOf(j);
                    JocDTO animJoc = ((JocDTO) anim.getSarcini().get(index));
                    animJoc.setEchipe(auxEchipe);
                    animJoc.setFormulas(j.getFormulas());
                    animJoc.setAllowsAnimatorPrincipal(j.isAllowsAnimatorPrincipal());
                    animJoc.setDetalii(j.getDetalii());
                    animJoc.setLocatie(j.getLocatie());
                    animJoc.setPost(j.getPost());
                }
            }
            j.setEchipe(auxEchipe);
        }
        
        for (ActivitateDTO a : activitati) {
            if (a.getEchipe() == null) {
                a.setEchipe(new ArrayList<EchipaDTO>());
            }
        }
            
        for (JocDTO j : jocuri) {
            if (j.getEchipe() == null) {
                j.setEchipe(new ArrayList<EchipaDTO>());
            }
        }
        
        salveaza();
        
        if (isNewProgram()) {
            SerializeController.getInstance().assignProgramActivitatiID();            
            timer.setDefaultDataExpirare();
        } 
        
        final long ID = SerializeController.getInstance().getProgramActivitatiID();
        
        for (ActivitateDTO a : activitati) {
            a.setIdProgram(ID);
        }
        
        for (JocDTO j : jocuri) {
            j.setIdProgram(ID);
        }
        
        SerieActivaFrame.posteazaProgram(activitati, jocuri, true);
        ServerFrame.getInstance().setPackage(activitati, jocuri);
        ServerFrame.getInstance().shareActivities();
        // trimite catre server
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        aif.setVisible(true);        
        dispose();
        salveaza();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        //if (!isNewProgram()) return; // daca e vorba de un program nou, atunci e imposibil ca activitatile sa fi fost realizate deja
        boolean isNewProg = isNewProgram();
        
        if (jComboBox1.getSelectedItem().equals("Jocuri")) {
            for (EchipaDB e : echipe){
                boolean playedToday = false;
                String nume;
                
                playedToday = !isNewProg && (ControllerDB.getInstance().getUnicJoc(
                        ControllerDB.getInstance().convert(((JocDTO)comboJocuri.getSelectedItem()).getJocGeneral()),
                        e,
                        date,
                        ((JocDTO) comboJocuri.getSelectedItem()).getPerioada()) != null);
                nume = ((JocDTO) comboJocuri.getSelectedItem()).toString();
                
                if (playedToday) {
                    continue;
                }
                if (!playedBefore(e)) {
                    DefaultListModel<EchipaDB> aux = (DefaultListModel<EchipaDB>) jList2.getModel();

                    if (!aux.contains(e)){
                        aux.addElement(e);
                    }
                }
            }
        } else {
            for (EchipaDB e : echipe){
                DefaultListModel<EchipaDB> aux = (DefaultListModel<EchipaDB>) jList2.getModel();
                String nume;
                boolean playedToday = false;
                playedToday = !isNewProg && (ControllerDB.getInstance().getUnicActivitate(
                            ControllerDB.getInstance().convert(((ActivitateDTO)comboActivitati.getSelectedItem()).getActivitateGenerala()),
                            e,
                            date,
                            ((ActivitateDTO) comboActivitati.getSelectedItem()).getPerioada()) != null);
                nume = ((ActivitateDTO) comboActivitati.getSelectedItem()).toString();
                
                if (playedToday) {
                    continue;
                }
                
                if (!aux.contains(e)){
                    aux.addElement(e);
                }                
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        HelperFrame.getInstance().post(
                "Fereastra de distribuire a echipelor",
                "Acesta este ultimul pas in planificarea programului de activitati, si presupune distribuirea echipelor "
                        + "pe activitati.");
    }//GEN-LAST:event_formMouseEntered

    private void jList1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseEntered
        HelperFrame.getInstance().post(
                "Lista echilor",
                "Contine toate echipele participante la seria actuala. Poti selecta echipe multiple tinand apasat pe "
                        + "tasta 'Ctrl' sau 'Shift'.");
    }//GEN-LAST:event_jList1MouseEntered

    private void jButton5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseEntered
        HelperFrame.getInstance().post(
                "Inserare rapida",
                "Aceasta optiune isi schimba semnificatia in functie de tipul de activitate: \n"
                        + "- pentru activitatea standard, aceasta plaseaza toate echipele in lista participantilor la activitatea selectata; \n"
                        + "- pentru jocuri, optiunea plaseaza in lista participantilor numai echipele care nu au realizat jocul pana acum;");
    }//GEN-LAST:event_jButton5MouseEntered

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        HelperFrame.getInstance().post(
                "Plasare echipa",
                "Plaseaza echipa/echipele selectate in lista participantilor la activitatea selectata.");
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        HelperFrame.getInstance().post(
                "Sterge echipa",
                "Sterge echipa/echipele selectate din lista participantilor la activitatea selectata.");
    }//GEN-LAST:event_jButton2MouseEntered

    private void jCheckBox1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseEntered
        HelperFrame.getInstance().post(
                "Prevenire duplica",
                "Este o optiune care se aplica doar la jocuri, si care te avertizeaza daca incerci sa plasezi in lista "
                        + "participantilor la un joc, o echipa care a realizat deja jocul respectiv.");
    }//GEN-LAST:event_jCheckBox1MouseEntered

    private void jComboBox1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox1MouseEntered
        HelperFrame.getInstance().post(
                "Tipuri de activitate",
                "Este meniul din care alegi tipul de activitate (standard sau joc) din care vor face parte activitatile din meniul din dreapta.");
    }//GEN-LAST:event_jComboBox1MouseEntered

    private void jComboBox2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox2MouseEntered
        HelperFrame.getInstance().post(
                "Meniul activitatilor",
                "De aici alegi carei activitati doresti sa-i plasezi echipele.");
    }//GEN-LAST:event_jComboBox2MouseEntered

    private void jList2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseEntered
        HelperFrame.getInstance().post(
                "Lista participantilor",
                "Aceasta este lista unde sunt plasate echipele care urmeaza sa participe la activitatea selectata.");
    }//GEN-LAST:event_jList2MouseEntered

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
        HelperFrame.getInstance().post(
                "Distribuire activitati",
                "Acesta este momentul adevarului. Daca ai terminat de pus la punct toate detaliile cu privire la programul de azi, "
                        + "este momentul sa le comunici si celorlalti ce ai planuit. Apasand acest buton, vei face "
                        + "public animatorilor planul de astazi, si fiecare va sti ce are de facut!");
    }//GEN-LAST:event_jButton4MouseEntered

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        HelperFrame.getInstance().post(
                "Revenire la pasul anterior",
                "Daca ceva nu a mers bine, sau doar ai uitat un mic detaliu, nu-i nimic, oricand poti reveni la pasii anteriori. "
                        + "Nu-ti face griji, tot ce ai facut pana acum va fi salvat automat!");
    }//GEN-LAST:event_jButton3MouseEntered

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int input = JOptionPane.showConfirmDialog(this, "Sigur doresti sa stergi continutul acestei ferestre?", "Resetare", 
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (input != JOptionPane.YES_OPTION) return;
        
        SerializeController.getInstance().serializeDistribuieEchipeFrame(null);
        dispose();
        MainFrame.openFrame(new DistribuieEchipeFrame(paramActivitati, paramJocuri, aif), true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseEntered
        HelperFrame.getInstance().post(
                "Buton golire",
                "Curata intreg continutul creat de utilizator din aceasta fereastra.");
    }//GEN-LAST:event_jButton6MouseEntered

    
    public boolean playedBefore (EchipaDB e) {
        boolean exist = false;
        try {              
            JocGeneralDTO jg = ((JocDTO) jComboBox2.getSelectedItem()).getJocGeneral();
            List<JocDB> jocuri = ControllerDB.getInstance().getJocuriByEchipa(e);
            for (int i = 0; i < jocuri.size() && !exist; ++i) {
                if (jocuri.get(i).getJocGeneralidJocGeneral().getNumeJocGeneral().equals(jg.getNumeJocGeneral())){
                    exist = true;
                }
            }
        } catch (Exception ex) {}
        return exist;
    }

    public final boolean isNewProgram () {
        return ((SerieActivaFrame.activitatiS == null ? true : SerieActivaFrame.activitatiS.isEmpty())
                && (SerieActivaFrame.jocuriS == null ? true : SerieActivaFrame.jocuriS.isEmpty())
                && (SerieActivaFrame.timer != null ? SerieActivaFrame.timer.hasExpired() : true));
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
