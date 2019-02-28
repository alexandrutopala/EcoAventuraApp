/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import costumSerialization.SerializableAdaugaInformatiiFrame;
import costumSerialization.SerializableDistribuieProgramFrame;
import dto.ActivitateDTO;
import dto.AnimatorDTO;
import dto.JocDTO;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import main.PulseThread;
import observer.KeyObserver;
import service.SerializeController;

/**
 *
 * @author Alexandru
 */
public class AdaugaInformatiiFrame extends javax.swing.JFrame {
    private final Color ORIGINAL_COLOR;
    private List<ActivitateDTO> activitati;
    private List<JocDTO> jocuri;
    private DefaultListModel<ActivitateDTO> modelActivitati;
    private DefaultListModel<JocDTO> modelJocuri;
    private ActivitateDTO curActivitate;
    private JocDTO curJoc;
    private DefaultComboBoxModel<AnimatorDTO> comboModel;
    private PulseThread pulse;
    private boolean invokeComboEvent = true;
    
    private List<ActivitateDTO> paramActivitati;
    private List<JocDTO> paramJocuri;
    /**
     * Creates new form AdaugaInformatiiFrame
     * @param activitati
     * @param jocuri
     */
    public AdaugaInformatiiFrame(List<ActivitateDTO> activitati, List<JocDTO> jocuri) {
        super ("Adauga informatii");
        initComponents();
        
        paramActivitati = new ArrayList(activitati);
        paramJocuri = new ArrayList<>(jocuri);
        
        ORIGINAL_COLOR = jButton3.getBackground();
        
        //!! revizuieste instructiunea de mai jos
        if (SerializeController.getInstance().isCandReadInforamtiiFrame()){
            SerializableAdaugaInformatiiFrame saif = SerializeController.getInstance().deserializeAdaugaInformatiiFrame();
            if (saif != null){
                ////
                System.out.println("Adauga Informatii : date incarcate cu succes");
                
                this.activitati = saif.getActivitati();
                this.jocuri = saif.getJocuri();
                
                for (ActivitateDTO a : this.activitati){
                    for (ActivitateDTO a2 : activitati) {
                        if (a.getActivitateGenerala().equals(a2.getActivitateGenerala()) && a.getPerioada().equals(a2.getPerioada())) {
                            a2.setDetalii(a.getDetalii());
                            a2.setLocatie(a.getLocatie());
                            a2.setPost(a.getPost());
                            //a.setAnimatori(a2.getAnimatori());
                            break;
                        }
                    }
                }
                
                for (JocDTO a : this.jocuri){
                    for (JocDTO a2 : jocuri) {
                        if (a.getJocGeneral().equals(a2.getJocGeneral()) && a.getPerioada().equals(a2.getPerioada())) {
                            a2.setDetalii(a.getDetalii());
                            a2.setLocatie(a.getLocatie());
                            a2.setPost(a.getPost());
                            a2.setAllowsAnimatorPrincipal(a.isAllowsAnimatorPrincipal());
                            //a.setAnimatori(a2.getAnimatori());
                            break;
                        }
                    }
                }                
                
                this.activitati = activitati;
                this.jocuri = jocuri;                
                SerializeController.getInstance().setCandReadInforamtiiFrame(true);
            }
        }
        this.activitati = activitati;
        this.jocuri = jocuri;
        SerializeController.getInstance().setCandReadInforamtiiFrame(true);
        
        
        comboModel = new DefaultComboBoxModel<>();
        modelActivitati = new DefaultListModel<>();
        modelJocuri = new DefaultListModel<>();
        
        for (ActivitateDTO a : activitati){
            modelActivitati.addElement(a);
        }
        
        for (JocDTO j : jocuri){
            modelJocuri.addElement(j);
        }
        
        jList1.setModel(modelActivitati);
        jList2.setModel(modelJocuri);
                        
        jLabel2.setText("");
        jLabel4.setText("");
        
        jCheckBox1.setVisible(false);
        jComboBox1.setVisible(false);
        
        // add events 
        KeyListener keyTypedListener = new KeyAdapter () {
            @Override
            public void keyTyped (KeyEvent ke) {
                if (pulse == null) {
                    pulse = new PulseThread(jButton3, ORIGINAL_COLOR);
                    pulse.start();
                }
            }            
        };
        
        jTextField1.addKeyListener(keyTypedListener);
        jTextField2.addKeyListener(keyTypedListener);
        jTextArea1.addKeyListener(keyTypedListener);   
        
        jTextField1.addKeyListener(new KeyObserver(KeyObserver.SHORT_LENGTH, jTextField1, "$"));
        jTextField2.addKeyListener(new KeyObserver(KeyObserver.STANDARD_LENGHT, jTextField2));
        jTextArea1.addKeyListener(new KeyObserver(KeyObserver.HUGE_LENGTH, jTextArea1));
        
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    public void enableButtons (boolean b){
        jButton1.setEnabled(b);
        //jButton2.setEnabled(b);
    }
    
    public void setSelectedItem (ActivitateDTO a, JocDTO j) {
        if (a != null) {
            jList1.setSelectedValue(a, true);
        } else if (j != null) {            
            jList2.setSelectedValue(j, true);
        }
    }
    
    public void showInfo (ActivitateDTO a, JocDTO j) {
        
        if (j != null) {
            jLabel2.setText(j.getJocGeneral().getNumeJocGeneral());
            jLabel4.setText("");
            jCheckBox1.setVisible(true);
            jCheckBox1.setSelected(j.isAllowsAnimatorPrincipal());
            
            invokeComboEvent = false;
            comboModel.removeAllElements(); 
            for (int i = 0; i < j.getAnimatori().size(); ++i){
                AnimatorDTO anim = j.getAnimatori().get(i);
                jLabel4.setText(jLabel4.getText() + anim.getNumeAnimator() + " ");
                comboModel.addElement(anim);
            }
            
            invokeComboEvent = true;
            comboModel.setSelectedItem(j.getAnimatori().get(0));
            jComboBox1.setModel(comboModel);
            
            jTextField1.setText(j.getLocatie());
            jTextField2.setText(j.getPost());
            jTextArea1.setText(j.getDetalii());
        } else if (a != null) {
            jLabel2.setText(a.getActivitateGenerala().getNumeActivitateGenerala());
            jLabel4.setText("");
            
            jCheckBox1.setVisible(false);
            jComboBox1.setVisible(false);
            
            for (int i = 0; i < a.getAnimatori().size(); ++i){
                AnimatorDTO anim = a.getAnimatori().get(i);
                jLabel4.setText(jLabel4.getText() + anim.getNumeAnimator() + " ");
            }
            
            jTextField1.setText(a.getLocatie());
            jTextField2.setText(a.getPost());
            jTextArea1.setText(a.getDetalii());
        }
        
    }
    
    public void disposeOnClose () {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jCheckBox1 = new javax.swing.JCheckBox();
        jComboBox1 = new javax.swing.JComboBox();
        jButton4 = new javax.swing.JButton();

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

        jLabel1.setText("Activitate :");

        jLabel2.setText("jLabel2");

        jLabel3.setText("Organizator(i) :");

        jLabel4.setText("jLabel4");

        jLabel5.setText("Unde?");

        jLabel6.setText("Post :");

        jLabel7.setText("Alte detalii :");

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextArea1);

        jButton2.setText("Mai departe >>");
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

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList2MouseEntered(evt);
            }
        });
        jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList2ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList2);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jList1MouseEntered(evt);
            }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(jList1);

        jLabel8.setText("Activitati");

        jLabel9.setText("Jocuri");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton1.setText("<< Inapoi");
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

        jButton3.setText("Salveaza");
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

        jCheckBox1.setText("Animator principal");
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseEntered(evt);
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

        jButton4.setText("Goleste");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 157, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(16, 16, 16)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jTextField1)
                            .addComponent(jTextField2)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jCheckBox1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox1, 0, 108, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1)))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3))
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton2)
                                        .addComponent(jButton4))
                                    .addComponent(jButton1)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 326, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton3)
                                    .addComponent(jCheckBox1)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jSeparator1))
                        .addGap(5, 5, 5)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       //dispose();
       setVisible(false);
        
       if (curActivitate != null){            
            modelActivitati.removeElement(curActivitate);
            curActivitate.setLocatie(jTextField1.getText());
            curActivitate.setPost(jTextField2.getText());
            curActivitate.setDetalii(jTextArea1.getText());
            modelActivitati.addElement(curActivitate);
            jList1.setModel(modelActivitati);
        } else if (curJoc != null) {            
            modelJocuri.removeElement(curJoc);
            curJoc.setLocatie(jTextField1.getText());
            curJoc.setPost(jTextField2.getText());
            curJoc.setDetalii(jTextArea1.getText());
            modelJocuri.addElement(curJoc);
            jList2.setModel(modelJocuri);
        }
        
        activitati.clear();
        jocuri.clear();
        
        for (int i = 0; i < modelActivitati.getSize(); ++i) {
            activitati.add(modelActivitati.get(i));
        }
        
        for (int i = 0; i < modelJocuri.getSize(); ++i) {
            jocuri.add(modelJocuri.get(i));
        }
        
        //ne asiguram ca nici un camp al vreunei activitati nu este null
        
        for (ActivitateDTO a : activitati) {
            if (a.getDetalii() == null) {
                a.setDetalii("");
            } 
            if (a.getLocatie() == null) {
                a.setLocatie("");
            }
            if (a.getPost() == null) {
                a.setPost("");
            }
        }
        
        for (JocDTO a : jocuri) {
            if (a.getDetalii() == null) {
                a.setDetalii("");
            } 
            if (a.getLocatie() == null) {
                a.setLocatie("");
            }
            if (a.getPost() == null) {
                a.setPost("");
            }
        }
        
        SerializableAdaugaInformatiiFrame saif = new SerializableAdaugaInformatiiFrame();
        saif.setActivitati(activitati);
        saif.setJocuri(jocuri);
        if (SerializeController.getInstance().serializeAdaugaInformatiiFrame(saif)) {
            System.out.println("Adauga Informatii : Salvare completa");
        } else {
            System.out.println("Adauga Informatii : Eroare la salvarea datelor");
        }
       
        new DistribuieEchipeFrame(activitati, jocuri, this).setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        jList2.setSelectedValue(null, false);
        
        //jButton3ActionPerformed(null);
        
        if (jList1.getSelectedValue() != null) {
            ActivitateDTO a = (ActivitateDTO) jList1.getSelectedValue();
            jCheckBox1.setVisible(false);
            jComboBox1.setVisible(false);
            curActivitate = a;
            curJoc = null;
            jLabel2.setText(a.getActivitateGenerala().getNumeActivitateGenerala());
            
            jLabel4.setText("");
            for (AnimatorDTO anim : a.getAnimatori()){
                jLabel4.setText(jLabel4.getText() + anim.getNumeAnimator() + " ");
            }
            
            jTextField1.setText(a.getLocatie());
            jTextField2.setText(a.getPost());
            jTextArea1.setText(a.getDetalii());
            
//            if (pulse != null) {
//                pulse.stopThread();
//                pulse = null;
//            }
            PulseThread.stopThread();
            pulse = null;
        }
    }//GEN-LAST:event_jList1MouseClicked

    private void jList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseClicked
        jList1.setSelectedValue(null, false);
        
        //jButton3ActionPerformed(null);
        
        if (jList2.getSelectedValue() != null) {            
            JocDTO a = (JocDTO) jList2.getSelectedValue();
            jCheckBox1.setVisible(true);
            jCheckBox1.setSelected(a.isAllowsAnimatorPrincipal());
            curJoc = a;
            curActivitate = null;
            jLabel2.setText(a.getJocGeneral().getNumeJocGeneral());
            
            jLabel4.setText("");
            
            invokeComboEvent = false;
            
            comboModel.removeAllElements();
            jComboBox1.setModel(new DefaultComboBoxModel());
            for (int i = 0; i < a.getAnimatori().size(); ++i) {
                AnimatorDTO anim = a.getAnimatori().get(i);
                jLabel4.setText(jLabel4.getText() + anim.getNumeAnimator() + " ");
                comboModel.addElement(anim);
            }
            
            invokeComboEvent = true;
            comboModel.setSelectedItem(a.getAnimatori().get(0));
            jComboBox1.setModel(comboModel);
            
            jComboBox1.setVisible(jCheckBox1.isSelected());
            
            jTextField1.setText(a.getLocatie());
            jTextField2.setText(a.getPost());
            jTextArea1.setText(a.getDetalii());
            
            PulseThread.stopThread();
            pulse = null;
//            if (pulse != null) {
//                pulse.stopThread();
//                pulse = null;
//            }
        }
    }//GEN-LAST:event_jList2MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (curActivitate != null){        
            int index = modelActivitati.indexOf(curActivitate);
            modelActivitati.removeElement(curActivitate);
            curActivitate.setLocatie(jTextField1.getText());
            curActivitate.setPost(jTextField2.getText());
            curActivitate.setDetalii(jTextArea1.getText());
            modelActivitati.add(index, curActivitate);  
        } else if (curJoc != null) {   
            int index = modelJocuri.indexOf(curJoc);
            modelJocuri.removeElement(curJoc);
            curJoc.setLocatie(jTextField1.getText());
            curJoc.setPost(jTextField2.getText());
            curJoc.setDetalii(jTextArea1.getText());
            modelJocuri.add(index, curJoc);
        }
        
        activitati.clear();
        jocuri.clear();
        
        for (int i = 0; i < modelActivitati.getSize(); ++i) {
            activitati.add(modelActivitati.get(i));
        }
        
        for (int i = 0; i < modelJocuri.getSize(); ++i) {
            jocuri.add(modelJocuri.get(i));
        }    
        
        
        
        SerializableAdaugaInformatiiFrame saif = new SerializableAdaugaInformatiiFrame();
        saif.setActivitati(activitati);
        saif.setJocuri(jocuri);
        SerializeController.getInstance().serializeAdaugaInformatiiFrame(saif);

        
        SerializeController.getInstance().setCanReadDistribuieFrame(true);
        SerializableDistribuieProgramFrame sdpf = SerializeController.getInstance().deserializareDistribuieProgramFrame();
        if (!(modelActivitati == null || modelActivitati.isEmpty())) {
            sdpf.setActivitati(modelActivitati);
        }
        if (!(modelJocuri == null || modelJocuri.isEmpty())) {
            sdpf.setJocuri(modelJocuri);
        }
        SerializeController.getInstance().serializaDistribuieProgramFrame(sdpf);
        try {
            MainFrame.openFrame(new DistribuieProgramFrame(activitati, jocuri), true);
            dispose();
        } catch (Exception ex) {
            
            //Logger.getLogger(AdaugaInformatiiFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (curActivitate != null) {
            int index = modelActivitati.indexOf(curActivitate);
            modelActivitati.remove(index);
            curActivitate.setLocatie(jTextField1.getText());
            curActivitate.setPost(jTextField2.getText());
            curActivitate.setDetalii(jTextArea1.getText());
            modelActivitati.add(index, curActivitate);
        } else if (curJoc != null) {
            int index = modelJocuri.indexOf(curJoc);
            modelJocuri.remove(index);
            curJoc.setLocatie(jTextField1.getText());
            curJoc.setPost(jTextField2.getText());
            curJoc.setDetalii(jTextArea1.getText());
            curJoc.setAllowsAnimatorPrincipal(jCheckBox1.isSelected());
            modelJocuri.add(index, curJoc);
        }
        
        jButton2.setEnabled(true);
//        if (pulse != null) {
//            pulse.stopThread();
//            pulse = null;
//        }
        PulseThread.stopThread();
        pulse = null;
    }//GEN-LAST:event_jButton3ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        SerializableAdaugaInformatiiFrame saif = new SerializableAdaugaInformatiiFrame();
        saif.setActivitati(activitati);
        saif.setJocuri(jocuri);
        SerializeController.getInstance().serializeAdaugaInformatiiFrame(saif);
        if (pulse != null) {
            pulse.stopThread();
            pulse = null;
        }
       
    }//GEN-LAST:event_formWindowClosing

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        if (jCheckBox1.isSelected()) {
            jComboBox1.setVisible(true);
        } else {
            jComboBox1.setVisible(false);
        }
        
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (!invokeComboEvent) return;
        
        AnimatorDTO a = (AnimatorDTO) jComboBox1.getSelectedItem();
        if (curJoc.getAnimatori().remove(a)) {
            curJoc.getAnimatori().add(0, a);
            jLabel4.setText("");
            for (AnimatorDTO anim : curJoc.getAnimatori()){
                jLabel4.setText(jLabel4.getText() + anim.getNumeAnimator() + " ");
            }
        }
        if (pulse == null) {
            pulse = new PulseThread(jButton3, ORIGINAL_COLOR);
            pulse.start();
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jList1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseEntered
        HelperFrame.getInstance().post(
                "Lista activitatilor planificate pentru ziua curenta",
                "Aici se afla toate activitatile planificate pentru aceasta zi. "
                        + "Selecteaz-o pe oricare dintre ele pentru a-i putea "
                        + "adauga detalii cu privinta la locatie, post si nu numai. \n"
                        + "Daca un detaliu anume nu este semnificativ (ex. : locatie, post etc.), acesta "
                        + "poate fi lasat gol. \n"
                        + "Nu uita sa SALVEZI inainte de a selecta alta activitate. Butonul 'Salveaza' "
                        + "va clipoci oricum.");
    }//GEN-LAST:event_jList1MouseEntered

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        jList1MouseClicked(null);
    }//GEN-LAST:event_jList1ValueChanged

    private void jList2ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList2ValueChanged
        jList2MouseClicked(null);
    }//GEN-LAST:event_jList2ValueChanged

    private void jList2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseEntered
        HelperFrame.getInstance().post(
                "Lista jocurilor planificate pentru ziua curenta",
                "Aici se afla toate jocurile planificate pentru aceasta zi. "
                        + "Selecteaza-l pe oricare pentru a-i putea "
                        + "adauga detalii cu privinta la locatie, post si nu numai. \n"
                        + "Daca un detaliu anume nu este semnificativ (ex. : locatie, post etc.), acesta "
                        + "poate fi lasat gol. \n"
                        + "Poti stabili daca numai un animator va putea puncta aceasta proba prin selectarea "
                        + "casutei 'Animator principal' (aceasta va aparea doar atunci cand un joc este selectat). \n"
                        + "Nu uita sa SALVEZI inainte de a selecta alta activitate. Butonul 'Salveaza' "
                        + "va clipoci oricum.");
    }//GEN-LAST:event_jList2MouseEntered

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        HelperFrame.getInstance().post("Fereastra de adauga a informatiilor", 
                "Folosindu-te de facilitatile oferite de aceasta fereastra, "
                        + "poti adauga pentru fiecare activitate sau joc in parte "
                        + "detalii privind locul in care se va desfasura, postul corespunzator, "
                        + "sau oricare alte detalii. \n"
                        + "De retinut: toate aceste campuri sunt optionale.");
    }//GEN-LAST:event_formMouseEntered

    private void jCheckBox1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseEntered
        HelperFrame.getInstance().post(
                "Optiunea de animator principal", 
                "Bifand aceasta optiune, selectezi un 'Animator principal' al acestui joc, "
                        + "fiind singurul dintre animatorii implicati care va putea puncta. "
                        + "Toti ceilalti animatori vor fi notificati doar ca sa se prezinte la joc. \n"
                        + "In cazul in care aceasta optiune este debifata, fiecare organizator al "
                        + "acestui joc va putea puncta, iar scorul final va fi reprezentat de suma "
                        + "tuturor punctajelor acordate de catre fiecare animator in parte.");
    }//GEN-LAST:event_jCheckBox1MouseEntered

    private void jComboBox1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox1MouseEntered
        HelperFrame.getInstance().post(
                "Meniul de selectare al animatorului principal", 
                "Acest meniu ofera posibilitatea de a numi unul dintre organizatorii acestei probe "
                        + "ca 'Animator principal'");
    }//GEN-LAST:event_jComboBox1MouseEntered

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        HelperFrame.getInstance().post(
                "Butonul de salvare", 
                "Pentru a nu pierde datele introduse pentru activitatea sau jocul selectat, "
                        + "fii sigur ca apesi pe acest buton pentru a salva tot ce ai scris. "
                        + "Conturul rosu intermitent te va avertiza cand este cazul sa salvezi.");
    }//GEN-LAST:event_jButton3MouseEntered

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        HelperFrame.getInstance().post("Butonul 'Inapoi'", 
                "Daca te-ai razgandit asupra unui detaliu anterior despre activitatile selectate, "
                        + "nu-i nicio problema, poti reveni oricand asupra lor, apasand acest buton.");
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        HelperFrame.getInstance().post(
                "Butonul 'Inainte'",
                "Cand ai terminat de pus la punct amanuntele privitoare la activitatile selectate, "
                        + "apasa acest buton pentru a merge in etapa urmatoare!");
    }//GEN-LAST:event_jButton2MouseEntered

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int input = JOptionPane.showConfirmDialog(this, "Sigur doresti sa stergi continutul acestei ferestre?", "Resetare", 
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (input != JOptionPane.YES_OPTION) return;
        
        for (ActivitateDTO a : paramActivitati) {
            a.setDetalii("");
            a.setLocatie("");
            a.setPost("");
        }
        
        for (JocDTO j : paramJocuri) {
            j.setDetalii("");
            j.setPost("");
            j.setLocatie("");
            j.setAllowsAnimatorPrincipal(true);
        }
        
        SerializeController.getInstance().serializeAdaugaInformatiiFrame(null);
        dispose();
        MainFrame.openFrame(new AdaugaInformatiiFrame(paramActivitati, paramJocuri), true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
        HelperFrame.getInstance().post(
                "Buton golire",
                "Curata intreg continutul creat de utilizator din aceasta fereastra.");
    }//GEN-LAST:event_jButton4MouseEntered

    private void jCheckBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseClicked
        if (pulse == null) {
            pulse = new PulseThread(jButton3, ORIGINAL_COLOR);
            pulse.start();
        }
    }//GEN-LAST:event_jCheckBox1MouseClicked

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
