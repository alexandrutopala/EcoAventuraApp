/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import costumSerialization.SerializablePosteazaProgramFrame;
import db.ActivitateGeneralaDB;
import db.JocGeneralDB;
import dto.ActivitateDTO;
import dto.JocDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import service.ControllerDB;
import service.SerializeController;

/**
 *
 * @author Alexandru
 */
public class PosteazaProgramFrame extends javax.swing.JFrame {
    private final static int STANDARD_MEDIU_WIDTH = 188;
    private final static int STANDARD_LARGE_WIDTH = 238;
    private DefaultListModel<JocGeneralDB> jocuri;
    private DefaultListModel<ActivitateGeneralaDB> activitati;
    private DefaultComboBoxModel<String> comboModel;
    private DefaultListModel<ActivitateGeneralaDB> activitateDimineata;
    private DefaultListModel<ActivitateGeneralaDB> activitateAmiaza;
    private DefaultListModel<ActivitateGeneralaDB> activitateSeara;
    private DefaultListModel<JocGeneralDB> jocDimineata;
    private DefaultListModel<JocGeneralDB> jocAmiaza;
    private DefaultListModel<JocGeneralDB> jocSeara;
    private boolean isChanged = false;
    
    
    /**
     * Creates new form PosteazaProgramFrame
     */
    public PosteazaProgramFrame() {
        super ("Planifica activitati");
        initComponents();
        isChanged = false;
        comboModel = new DefaultComboBoxModel<>(new String [] {"Activitati", "Jocuri"});
        jocuri = new DefaultListModel<>();
        activitati = new DefaultListModel<>();
        
        SerializablePosteazaProgramFrame sppf = SerializeController.getInstance().deserializeazaPostareProgramFrame();
        
        if (sppf == null || !SerializeController.getInstance().isCanReadPosteazaFrame()) {
            
            activitateDimineata = new DefaultListModel<>();
            activitateAmiaza = new DefaultListModel<>();
            activitateSeara = new DefaultListModel<>();

            jocDimineata = new DefaultListModel<>();
            jocAmiaza = new DefaultListModel<>();
            jocSeara = new DefaultListModel<>();            

            
            SerializeController.getInstance().setCanReadPosteazaFrame(true);
        } else {
            activitateDimineata = sppf.getActivitateDimineata();
            activitateAmiaza = sppf.getActivitateAmiaza();
            activitateSeara = sppf.getActivitateSeara();
            jocDimineata = sppf.getJocDimineata();
            jocAmiaza = sppf.getJocAmiaza();
            jocSeara = sppf.getJocSeara();
        }
        
        List<ActivitateGeneralaDB> aList = ControllerDB.getInstance().getAllActivitatiGenerale();
        List<JocGeneralDB> jList = ControllerDB.getInstance().getAllJocuriGenerale();

        for (ActivitateGeneralaDB a : aList){
            activitati.addElement(a);
        }

        for (JocGeneralDB j : jList) {
            if (j.getNumeJocGeneral().equals("penalizare")){
                continue;
            }
            jocuri.addElement(j);
        }
        
        jList7.setModel(jocDimineata);
        jList6.setModel(jocAmiaza);
        jList5.setModel(jocSeara);
        jList4.setModel(activitateSeara);
        jList3.setModel(activitateAmiaza);
        jList2.setModel(activitateDimineata);
        
        jList1.setFixedCellWidth(STANDARD_MEDIU_WIDTH);
        jList2.setFixedCellWidth(STANDARD_LARGE_WIDTH);
        jList3.setFixedCellWidth(STANDARD_LARGE_WIDTH);
        jList4.setFixedCellWidth(STANDARD_LARGE_WIDTH);
        jList5.setFixedCellWidth(STANDARD_LARGE_WIDTH);
        jList6.setFixedCellWidth(STANDARD_LARGE_WIDTH);
        jList7.setFixedCellWidth(STANDARD_LARGE_WIDTH);
        
        jComboBox1.setModel(comboModel);
        
        jComboBox1.setSelectedItem("Activitati");
        jList1.setModel(activitati);
        
        checkLists();
        
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void checkLists (){
        List<ActivitateGeneralaDB> activitatiGenerale = ControllerDB.getInstance().getAllActivitatiGenerale();
        List<JocGeneralDB> jocuriGenerale = ControllerDB.getInstance().getAllJocuriGenerale();
        
        for (int i = 0; i < activitateDimineata.size(); ++i) {
            ActivitateGeneralaDB a = activitateDimineata.get(i);
            if (!activitatiGenerale.contains(a)) {
                activitateDimineata.remove(i);
                i--;
            }
        }
        
        for (int i = 0; i < activitateAmiaza.size(); ++i) {
            ActivitateGeneralaDB a = activitateAmiaza.get(i);
            if (!activitatiGenerale.contains(a)) {
                activitateAmiaza.remove(i);
                i--;
            }
        }
        
        for (int i = 0; i < activitateSeara.size(); ++i) {
            ActivitateGeneralaDB a = activitateSeara.get(i);
            if (!activitatiGenerale.contains(a)) {
                activitateSeara.remove(i);
                i--;
            }
        }
        
        for (int i = 0; i < jocDimineata.size(); ++i) {
            JocGeneralDB j = jocDimineata.get(i);
            if (!jocuriGenerale.contains(j)) {
                jocDimineata.remove(i);
                i--;
            }
        }
        
        for (int i = 0; i < jocAmiaza.size(); ++i) {
            JocGeneralDB j = jocAmiaza.get(i);
            if (!jocuriGenerale.contains(j)) {
                jocAmiaza.remove(i);
                i--;
            }
        }
        
        for (int i = 0; i < jocSeara.size(); ++i) {
            JocGeneralDB j = jocSeara.get(i);
            if (!jocuriGenerale.contains(j)) {
                jocSeara.remove(i);
                i--;
            }
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList7 = new javax.swing.JList();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList6 = new javax.swing.JList();
        jScrollPane7 = new javax.swing.JScrollPane();
        jList5 = new javax.swing.JList();
        jSeparator1 = new javax.swing.JSeparator();
        jButton9 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jButton3.setText("<<");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton2MouseEntered(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText(">>");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("<<");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton2MouseEntered(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText(">>");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }
        });
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Anuleaza");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Mai departe >>");
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

        jLabel2.setText("Dupa-amiaza");

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList2);

        jList3.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList3);

        jLabel3.setText("Seara");

        jList4.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jList4);

        jLabel6.setText("Activitati");
        jLabel6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Dimineata");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane4))
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        jLabel7.setText("Jocuri");
        jLabel7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jList7.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane6.setViewportView(jList7);

        jList6.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(jList6);

        jList5.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane7.setViewportView(jList5);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 19, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(30, 30, 30)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton9.setText("Goleste");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(489, 489, 489))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jComboBox1, 0, 188, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(92, 92, 92)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jScrollPane1))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton7)
                            .addComponent(jButton8)
                            .addComponent(jButton9)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (((String) jComboBox1.getSelectedItem()).equals("Activitati")) {
            isChanged = true;
            int indicies [] = jList2.getSelectedIndices();
            if (indicies != null && indicies.length != 0) {
                for (int i = indicies.length - 1; i >= 0; --i) {
                    activitateDimineata.remove(indicies[i]);
                }
                jList2.setModel(activitateDimineata);
            }
        } else {
            int indicies [] = jList7.getSelectedIndices();
            if (indicies != null && indicies.length != 0){
                for (int i = indicies.length - 1; i >= 0; --i) {
                    jocDimineata.remove(indicies[i]);
                }
                jList7.setModel(jocDimineata);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (((String) jComboBox1.getSelectedItem()).equals("Activitati")) {
            isChanged = true;
            int indicies [] = jList3.getSelectedIndices();
            if (indicies != null && indicies.length != 0) {
                for (int i = indicies.length - 1; i >= 0; --i) {
                    activitateAmiaza.remove(indicies[i]);
                }
                jList3.setModel(activitateAmiaza);
            }
        } else {
           int indicies [] = jList6.getSelectedIndices();
            if (indicies != null && indicies.length != 0){
                for (int i = indicies.length - 1; i >= 0; --i) {
                    jocAmiaza.remove(indicies[i]);
                }
                jList6.setModel(jocAmiaza);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (((String) jComboBox1.getSelectedItem()).equals("Activitati")) {
            //isChanged = true;
            int indicies [] = jList4.getSelectedIndices();
            if (indicies != null  && indicies.length != 0) {
                for (int i = indicies.length - 1; i >= 0; --i) {
                    activitateSeara.remove(indicies[i]);
                }
                jList4.setModel(activitateSeara);
                jList1.setModel(activitati);
            }
        } else {
            int indicies [] = jList5.getSelectedIndices();
            if (indicies != null  && indicies.length != 0){
                for (int i = indicies.length - 1; i >= 0; --i) {
                    jocSeara.remove(indicies[i]);
                }
                jList5.setModel(jocSeara);
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (((String) jComboBox1.getSelectedItem()).equals("Activitati")){
            jList1.setModel(activitati);
        } else {
            jList1.setModel(jocuri);
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jList1.getSelectedValue() != null) {
            if (((String) jComboBox1.getSelectedItem()).equals("Activitati")){
                int indicies [] = jList1.getSelectedIndices();
                
                for (int i = 0; i < indicies.length; ++i) {
                    if (!activitateDimineata.contains((ActivitateGeneralaDB) jList1.getModel().getElementAt(indicies[i])))
                        activitateDimineata.addElement((ActivitateGeneralaDB) jList1.getModel().getElementAt(indicies[i]));
                }
                
                jList2.setModel(activitateDimineata);
            } else {
                int indicies[] = jList1.getSelectedIndices();
                
                for (int i = 0; i < indicies.length; ++i) {
                    if (!jocDimineata.contains((JocGeneralDB) jList1.getModel().getElementAt(indicies[i]))) {
                        if (SerializeController.getInstance().getFormula((JocGeneralDB) jList1.getModel().getElementAt(indicies[i])) == null) {
                            JOptionPane.showMessageDialog(this, "<html>Jocul " + ((JocGeneralDB) jList1.getModel().getElementAt(indicies[i])).getNumeJocGeneral() + ""
                                    + " nu a putut fi adaugat din cauza lipsei formulei de calcul. <br> Intrati in fereastra de Gestiune Activitati pentru a adauga formula. </html>", "Lipsa formula", JOptionPane.ERROR_MESSAGE);
                            continue;
                        } 
                        jocDimineata.addElement((JocGeneralDB) jList1.getModel().getElementAt(indicies[i]));
                    }
                }
                
                jList7.setModel(jocDimineata);                
            }
            //isChanged = true;
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (jList1.getSelectedValue() != null) {
            //isChanged = true;
            if (((String) jComboBox1.getSelectedItem()).equals("Activitati")){
                int indicies [] = jList1.getSelectedIndices();
                
                for (int i = 0; i < indicies.length; ++i) {
                    if (!activitateAmiaza.contains((ActivitateGeneralaDB) jList1.getModel().getElementAt(indicies[i])))
                        activitateAmiaza.addElement((ActivitateGeneralaDB) jList1.getModel().getElementAt(indicies[i]));
                }
                
                jList3.setModel(activitateAmiaza);
            } else {
                int indicies[] = jList1.getSelectedIndices();
                
                for (int i = 0; i < indicies.length; ++i) {
                    if (!jocAmiaza.contains((JocGeneralDB) jList1.getModel().getElementAt(indicies[i]))){
                        if (SerializeController.getInstance().getFormula((JocGeneralDB) jList1.getModel().getElementAt(indicies[i])) == null) {
                            JOptionPane.showMessageDialog(this, "<html>Jocul " + ((JocGeneralDB) jList1.getModel().getElementAt(indicies[i])).getNumeJocGeneral() + ""
                                    + " nu a putut fi adaugat din cauza lipsei formulei de calcul. <br> Intrati in fereastra de Gestiune Activitati pentru a adauga formula. </html>", "Lipsa formula", JOptionPane.ERROR_MESSAGE);
                            continue;
                        } 
                        jocAmiaza.addElement((JocGeneralDB) jList1.getModel().getElementAt(indicies[i]));
                    }
                }
                
                jList6.setModel(jocAmiaza);                
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (jList1.getSelectedValue() != null) {
            //isChanged = true;
            if (((String) jComboBox1.getSelectedItem()).equals("Activitati")){
                int indicies [] = jList1.getSelectedIndices();
                
                for (int i = 0; i < indicies.length; ++i) {
                    if (!activitateSeara.contains((ActivitateGeneralaDB) jList1.getModel().getElementAt(indicies[i])))    
                        activitateSeara.addElement((ActivitateGeneralaDB) jList1.getModel().getElementAt(indicies[i]));
                }
                
                jList4.setModel(activitateSeara);
            } else {
                int indicies[] = jList1.getSelectedIndices();
                
                for (int i = 0; i < indicies.length; ++i) {
                    if (!jocSeara.contains((JocGeneralDB) jList1.getModel().getElementAt(indicies[i]))){
                        if (SerializeController.getInstance().getFormula((JocGeneralDB) jList1.getModel().getElementAt(indicies[i])) == null) {
                            JOptionPane.showMessageDialog(this, "<html>Jocul " + ((JocGeneralDB) jList1.getModel().getElementAt(indicies[i])).getNumeJocGeneral() + ""
                                    + " nu a putut fi adaugat din cauza lipsei formulei de calcul. <br> Intrati in fereastra de Gestiune Activitati pentru a adauga formula. </html>", "Lipsa formula", JOptionPane.ERROR_MESSAGE);
                            continue;
                        } 
                        jocSeara.addElement((JocGeneralDB) jList1.getModel().getElementAt(indicies[i]));
                    }
                }
                jList5.setModel(jocSeara);                
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        dispose();        
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
//        if (isChanged) try {
//            boolean rez = SerializeController.getInstance().deleteFile(SerializeController.DISTRIBUIE_PROGRAM_PATH);
//            if (!rez) System.out.println("Fisierul nu a fost sters");
//            SerializeController.getInstance().setCanReadDistribuieFrame(false);
//        } catch (IOException ex) {
//            //Logger.getLogger(PosteazaProgramFrame.class.getName()).log(Level.SEVERE, null, ex);
//            ex.printStackTrace();                    
//        }
        ArrayList<ActivitateDTO> listaActivitati = new ArrayList<>();
        ArrayList<JocDTO> listaJocuri = new ArrayList<>();
        ActivitateDTO activitateDto;
        JocDTO jocDto;
        
        //for (ActivitateGeneralaDB a : ((ActivitateGeneralaDB [])activitateDimineata.toArray())){
        for (int i = 0; i < activitateDimineata.size(); ++i) {
            ActivitateGeneralaDB a = activitateDimineata.get(i);
            if (a.getNumeActivitateGenerala() == null) continue;
            activitateDto = new ActivitateDTO();
            activitateDto.setActivitateGenerala(ControllerDB.getInstance().convert(a));
            activitateDto.setPerioada("dimineata");
            listaActivitati.add(activitateDto);
        }
        
        for (int i = 0; i < activitateAmiaza.size(); ++i) {
            ActivitateGeneralaDB a = activitateAmiaza.get(i);
            if (a.getNumeActivitateGenerala() == null) continue;
            activitateDto = new ActivitateDTO();
            activitateDto.setActivitateGenerala(ControllerDB.getInstance().convert(a));
            activitateDto.setPerioada("amiaza");
            listaActivitati.add(activitateDto);
        }
        
        for (int i = 0; i < activitateSeara.size(); ++i) {
            ActivitateGeneralaDB a = activitateSeara.get(i);
            if (a.getNumeActivitateGenerala() == null) continue;
            activitateDto = new ActivitateDTO();
            activitateDto.setActivitateGenerala(ControllerDB.getInstance().convert(a));
            activitateDto.setPerioada("seara");
            listaActivitati.add(activitateDto);
        }
        
        for (int i = 0; i < jocDimineata.size(); ++i){
            JocGeneralDB j = jocDimineata.get(i);
            if (j.getNumeJocGeneral() == null) continue;
            jocDto = new JocDTO();
            jocDto.setJocGeneral(ControllerDB.getInstance().convert(j));
            jocDto.setPerioada("dimineata");
            jocDto.getJocGeneral().setFormula(SerializeController.getInstance().getFormula(j));
            listaJocuri.add(jocDto);
        }
        
        for (int i = 0; i < jocAmiaza.size(); ++i){
            JocGeneralDB j = jocAmiaza.get(i);
            if (j.getNumeJocGeneral() == null) continue;
            jocDto = new JocDTO();
            jocDto.setJocGeneral(ControllerDB.getInstance().convert(j));
            jocDto.setPerioada("amiaza");
            jocDto.getJocGeneral().setFormula(SerializeController.getInstance().getFormula(j));
            listaJocuri.add(jocDto);
        }
        
        for (int i = 0; i < jocSeara.size(); ++i){
            JocGeneralDB j = jocSeara.get(i);
            if (j.getNumeJocGeneral() == null) continue;
            jocDto = new JocDTO();
            jocDto.setJocGeneral(ControllerDB.getInstance().convert(j));
            jocDto.setPerioada("seara");
            jocDto.getJocGeneral().setFormula(SerializeController.getInstance().getFormula(j));
            listaJocuri.add(jocDto);
        }
        
        salveaza(false);
        try {
            MainFrame.openFrame(new DistribuieProgramFrame(listaActivitati, listaJocuri), true);
            dispose();
        } catch (Exception ex) {
            //Logger.getLogger(PosteazaProgramFrame.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jList1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseEntered
        HelperFrame.getInstance().post(
                "Lista activitati",
                "Sunt listate activitatile generale care au fost adaugate pana acum. Poti realiza selectii multiple folosind tastele 'Ctrl' sau 'Shift'");
    }//GEN-LAST:event_jList1MouseEntered

    private void jComboBox1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox1MouseEntered
        HelperFrame.getInstance().post(
                "Meniul tipurilor de activitati",
                "Cele doua tipuri sunt:  \n"
                        + " * activitati (standard); \n"
                        + " * jocuri; \n"
                        + "DE RETINUT: Diferenta majora intre o activitate standard si un joc este aceea ca jocul este punctat, si"
                        + " este pus un mai mare accent pe organizarea acestuia.");
    }//GEN-LAST:event_jComboBox1MouseEntered

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        String perioada = "";
        JButton source = (JButton) evt.getSource();
        if (source == jButton1) perioada = "dimineata";
        else if (source == jButton4) perioada = "amiaza";
        else if (source == jButton6) perioada = "seara";
        
        HelperFrame.getInstance().post(
                "Plaseaza activitati",
                "Activitatea selectata/Activitatile selectate vor fi plasate " + perioada + ".");
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        String perioada = "";
        JButton source = (JButton) evt.getSource();
        if (source == jButton2) perioada = "dimineata";
        else if (source == jButton3) perioada = "amiaza";
        else if (source == jButton5) perioada = "seara";
        
        HelperFrame.getInstance().post(
                "Anuleaza activitatea",
                "Activitatea selectata/Activitatile selectate din perioada de " + perioada + " vor fi anulate. \n"
                        + "DE RETINUT: daca tipul de activitate al listei din care revocam NU coincide cu tipul selectat din meniul "
                        + "listei principale, atunci anularea nu va functiona.");
    }//GEN-LAST:event_jButton2MouseEntered

    private void jButton8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseEntered
        HelperFrame.getInstance().post(
                "Pasul urmator",
                "Gata aici? Hai mai departe atunci!");
    }//GEN-LAST:event_jButton8MouseEntered

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        int input = JOptionPane.showConfirmDialog(this, "Sigur doresti sa stergi continutul acestei ferestre?", "Resetare", 
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (input != JOptionPane.YES_OPTION) return;
        
        SerializeController.getInstance().serializeazaPostareProgramFrame(null);
        dispose();
        MainFrame.openFrame(new PosteazaProgramFrame(), true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseEntered
        HelperFrame.getInstance().post(
                "Buton golire",
                "Curata intreg continutul creat de utilizator din aceasta fereastra.");
    }//GEN-LAST:event_jButton9MouseEntered

    public void salveaza (boolean ask) {
        if (ask) {
            int option = JOptionPane.showConfirmDialog(this, "Salvezi inainte de a iesi?", "Salveaza", JOptionPane.YES_NO_CANCEL_OPTION);

            if (option == JOptionPane.NO_OPTION){
                dispose();
                return;
            } else if (option == JOptionPane.CANCEL_OPTION){
                return;
            }
        }
        
        SerializablePosteazaProgramFrame sppf = new SerializablePosteazaProgramFrame();
        sppf.setActivitateAmiaza(activitateAmiaza);
        sppf.setActivitateDimineata(activitateDimineata);
        sppf.setActivitateSeara(activitateSeara);
        sppf.setActivitati(activitati);
        sppf.setJocAmiaza(jocAmiaza);
        sppf.setJocDimineata(jocDimineata);
        sppf.setJocSeara(jocSeara);
        sppf.setJocuri(jocuri);
        
        if (!SerializeController.getInstance().serializeazaPostareProgramFrame(sppf) && ask){
            JOptionPane.showMessageDialog(this, "Optiunile nu au putut fi salvate");
        }
        
        isChanged = false;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JList jList4;
    private javax.swing.JList jList5;
    private javax.swing.JList jList6;
    private javax.swing.JList jList7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
