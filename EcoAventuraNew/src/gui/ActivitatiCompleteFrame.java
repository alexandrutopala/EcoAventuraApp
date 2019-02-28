/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import db.ActivitateDB;
import db.JocDB;
import dto.ActivitateDTO;
import dto.AnimatorDTO;
import dto.JocDTO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import service.ControllerDB;
import service.SerializeController;

/**
 *
 * @author Alexandru
 */
public class ActivitatiCompleteFrame extends javax.swing.JFrame {
    private final static int STANDARD_WIDTH = 197;
    private static ActivitatiCompleteFrame singleton = null;
    private DefaultListModel<AnimatorDTO> modelAnimatori;
    private DefaultListModel<Object> modelSarcini;
    private HashMap<AnimatorDTO, List<Object> > sarciniMap;
    private HashMap<AnimatorDTO, List<Boolean> > completedMap;
    public boolean raportMode = false;
    private Date date;
    /**
     * Creates new form ActivitatiCompleteFrame
     */
    private ActivitatiCompleteFrame() throws Exception {
        super ("Activitati in desfasurare");
        initComponents();
        
        sarciniMap = new HashMap<>();
        completedMap = new HashMap<>();
        modelAnimatori = new DefaultListModel<>();
        modelSarcini = new DefaultListModel<>();
        
        try {
            List<ActivitateDTO> activitati = SerieActivaFrame.activitatiS;
            List<JocDTO> jocuri = SerieActivaFrame.jocuriS;

            for (ActivitateDTO a : activitati) {
                for (AnimatorDTO anim : a.getAnimatori()) {
                    if (!sarciniMap.containsKey(anim)) {
                        sarciniMap.put(anim, new ArrayList<>());
                        completedMap.put(anim, new ArrayList<Boolean>());
                    }
                    sarciniMap.get(anim).add(a);
                    completedMap.get(anim).add(false);
                }
            }

            for (JocDTO j : jocuri) {
                for (AnimatorDTO anim : j.getAnimatori()) {
                    if (!sarciniMap.containsKey(anim)) {
                        sarciniMap.put(anim, new ArrayList<>());
                        completedMap.put(anim, new ArrayList<Boolean>());
                    }
                    sarciniMap.get(anim).add(j);
                    completedMap.get(anim).add(false);
                }
            }

            for (AnimatorDTO anim : sarciniMap.keySet()) {
                modelAnimatori.addElement(anim);
            }
        } catch (Exception e) {}
        
        jList2.setFixedCellWidth(STANDARD_WIDTH);
        jList2.setCellRenderer(new ListCellRender());
        jList1.setFixedCellWidth(STANDARD_WIDTH);
        jList1.setModel(modelAnimatori);
        jList2.setModel(modelSarcini);
        jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        jLabel2.setText("");
        jLabel4.setText("");
        jLabel5.setText("");
        jButton3.setVisible(false);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static ActivitatiCompleteFrame getInstance() {
        if (singleton == null) {
            try {
                singleton = new ActivitatiCompleteFrame();
            } catch (Exception ex) {
                Logger.getLogger(ActivitatiCompleteFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        return singleton;
    }
    
    public void buildFrame (List<ActivitateDTO> activitati, List<JocDTO> jocuri) {
        boolean NULL = false;
        sarciniMap = new HashMap<>();
        completedMap = new HashMap<>();
        modelAnimatori.clear();
    
        try {
            for (ActivitateDTO a : activitati) {
                for (AnimatorDTO anim : a.getAnimatori()) {
                    if (!sarciniMap.containsKey(anim)) {
                        sarciniMap.put(anim, new ArrayList<>());
                        completedMap.put(anim, new ArrayList<Boolean>());
                    }
                    sarciniMap.get(anim).add(a);
                    completedMap.get(anim).add(false);
                }
            }
        } catch (Exception ex) { NULL = true; }
        
        try {
            for (JocDTO j : jocuri) {
                for (AnimatorDTO anim : j.getAnimatori()) {
                    if (!sarciniMap.containsKey(anim)) {
                        sarciniMap.put(anim, new ArrayList<>());
                        completedMap.put(anim, new ArrayList<Boolean>());
                    }
                    sarciniMap.get(anim).add(j);
                    completedMap.get(anim).add(false);
                }
            }
        } catch (Exception ex) { 
            NULL = (NULL && true); 
        }
        
        for (AnimatorDTO anim : sarciniMap.keySet()) {
            modelAnimatori.addElement(anim);
        }
        
        jList1.setModel(modelAnimatori);
        jList2.setModel(modelSarcini);
        
        
    }
    
    public void buildRaport (List<ActivitateDTO> activitati, List<JocDTO> jocuri, Date date) {
        try {
            buildFrame(activitati, jocuri);
            raportMode = true;
            this.date = date;
            setVisible(true);
            jButton2.setText("Am terminat");
            jButton3.setVisible(true);
            if (SerializeController.getInstance().deserializeLastSerieActivaFrame() == null) {
                jButton3.setEnabled(false);
            } else {
                jButton3.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Nimic de vizualizat", "Program null", JOptionPane.INFORMATION_MESSAGE);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Animatori"));

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
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Activitati"));

        jLabel1.setText("Activitati complete:");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("/0");

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
        jScrollPane2.setViewportView(jList2);

        jLabel3.setText("Animator :");

        jLabel4.setText("jLabel4");
        jLabel4.setMaximumSize(new java.awt.Dimension(150, 14));
        jLabel4.setMinimumSize(new java.awt.Dimension(150, 14));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("10");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton2.setText("Refresh");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("<< Iesire");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("Prelungeste program");
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

        jButton4.setText("Istoric");
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        final AnimatorDTO selected = (AnimatorDTO) jList1.getSelectedValue();
        if (selected == null) return;
        
        modelSarcini.clear();
        jLabel4.setText(selected.getNumeAnimator());
        
        for (Object o : sarciniMap.get(selected)){
            modelSarcini.addElement(o);
        }
        
        jList2.setModel(modelSarcini);
        
        jLabel2.setText("/" + sarciniMap.get(selected).size());
        jLabel5.setText("0");
    }//GEN-LAST:event_jList1ValueChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose(); 
        if (raportMode) {       
            raportMode = false;
            singleton = null;
            setAlwaysOnTop(false);
            if (SerializeController.getInstance().hasExpired(Calendar.getInstance().getTime())) {
                SerializeController.getInstance().serializeSerieActivaFrame(null);
                SerializeController.getInstance().serializeLastSerieActivaFrame(null);
            }
            
            MainFrame.openFrame(new SerieActivaFrame(MainFrame.getSerieActiva()), true);
            return;
        }
        try {
            singleton = new ActivitatiCompleteFrame();
            singleton.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "<html>Ops, s-a produs o schimbare neprevazuta o programului in desfasurare...</html>", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseClicked
        if (jList2.getSelectedValue() != null && 
            evt.getButton() == MouseEvent.BUTTON1 &&
            evt.getClickCount() == 2) {
            
            try {
                new EchipeCompleteFrame(jList2.getSelectedValue(), (AnimatorDTO) jList1.getSelectedValue(), 
                    raportMode == true ? date : Calendar.getInstance().getTime()).setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(ActivitatiCompleteFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jList2MouseClicked

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        SerieActivaFrame.notifica(false);
    }//GEN-LAST:event_formFocusGained

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (raportMode) {
            raportMode = false;
            singleton = null;
            setAlwaysOnTop(false);
            jButton3.setVisible(false);
            if (SerializeController.getInstance().hasExpired(Calendar.getInstance().getTime())) {
                SerializeController.getInstance().serializeSerieActivaFrame(null);
            }
            MainFrame.openFrame(new SerieActivaFrame(MainFrame.getSerieActiva()), true);
        }
    }//GEN-LAST:event_formWindowClosing

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        HelperFrame.getInstance().post("Fereastra activitatilor complete",
                "Aceasta este fereastra in care se poate urmari progresul " +
                 "facut de animatori in realizarea activitatilor curente.");
    }//GEN-LAST:event_formMouseEntered

    private void jList1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseEntered
        HelperFrame.getInstance().post("Lista animatorilor implicati in activitatile actuale",
                "Aici este afisat fiecare animator care este implicat in cel putin o activitate in desfaurare."
              + " Selecteaza animatorul dorit pentru a vedea ce activitati a indeplinit. "
                        + "Te poti folosi de sagetelele 'sus' si 'jos' pentru a naviga mai usor.");
    }//GEN-LAST:event_jList1MouseEntered

    private void jList2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseEntered
        HelperFrame.getInstance().post("Lista activitatilor animatorului selectat", 
                "Aici sunt afisate toate activitatile in care animatorul este implicat. "
                        + "In functie de culoarea activitatii, putem afla urmatoarele informatii: \n"
                        + " *alb - activitatea nu a fost inca finalizata de toate echipele; \n"
                        + " *verde - toate echipele au realizat aceasta activitate; \n"
                        + " *rosu - exista cel putin o echipa notata ca 'absenta' la aceasta activitate; \n"
                        + "Dublu-click pe activitate pentru a vedea mai multe detalii despre echipele participante la aceasta.");
    }//GEN-LAST:event_jList2MouseEntered

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        SerieActivaFrame.prelungesteSesiuneDialog(singleton, false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        HelperFrame.getInstance().post("Prelungire a programului",
                "In cazul in care unul sau mai multi animatori nu si-au incheiat programul, poti "
                        + "prelungi programul de activitati folosind aceasta optiune. \n"
                        + "In cazul in care programul NU va fi prelungit, acesta va fi sters definitiv.");
    }//GEN-LAST:event_jButton3MouseEntered

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        IstoricFrame.getInstance().setFramePosition(getLocation().x + getWidth() + 5, getLocation().y);
    }//GEN-LAST:event_formComponentMoved

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        IstoricFrame.getInstance().setFramePosition(getLocation().x + getWidth() + 5, getLocation().y);
    }//GEN-LAST:event_formComponentResized

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        IstoricFrame.getInstance().setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    public void refresh () {       
        jList2.setModel(new DefaultListModel());
        jList2.setModel(modelSarcini);
        toFront();
        
        if (isVisible()) {
            SerieActivaFrame.notifica(false);
        }
    }
    
    public void refreshFrame() {
        try {
            if (singleton != null) {
                singleton.dispose();
            }
            singleton = new ActivitatiCompleteFrame();
            singleton.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "<html>Ops, s-a produs o schimbare neprevazuta o programului in desfasurare...</html>", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private class ListCellRender extends DefaultListCellRenderer {
        private final Color HIGH_VALUE_FG = Color.white;
        private final Color HIGH_VALUE_BG = Color.getHSBColor((float) 0.375, (float) 0.711, (float) 0.882);
        private final Color STANDARD_VALUE_FG = Color.BLACK;
        private final Color STANDARD_VALUE_BG = Color.WHITE;
        float [] aux = Color.RGBtoHSB(249, 56, 34, null);                
        private final Color ABSENT_VALUE_BG = Color.getHSBColor(aux[0], aux[1], aux[2]);
        private final Color ABSENT_VALUE_FG = Color.WHITE;
        
    
        @Override
        public Component getListCellRendererComponent(JList<?> list,
            Object value, int index, boolean isSelected, boolean cellHasFocus) {
            
            Component superRenderer = super.getListCellRendererComponent(list, value, index, isSelected,
               cellHasFocus);
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String data = sdf.format(raportMode == true ? date : Calendar.getInstance().getTime());
            
            if (value.getClass() == ActivitateDTO.class) {
                ActivitateDTO adto = (ActivitateDTO) value;
                // vom primi o lista cu aceeasi activitate, de atatea ori cate echipe au facut-o
                //EFICIENTA DE TIMP
                List<ActivitateDB> adb = ControllerDB.getInstance().getStoredActivitateByIdProgram(adto.getPerioada(), 
                                                                                        ControllerDB.getInstance().convert(adto.getActivitateGenerala()),
                                                                                        SerializeController.getInstance().getProgramActivitatiID());
                int activitatatiCompleteCount = 0;
                AnimatorDTO anim = (AnimatorDTO) jList1.getSelectedValue();
                
                for (ActivitateDB a : adb) {
                    if (a.getOrganizator().contains("*" + anim.getNumeAnimator())) {
                        activitatatiCompleteCount++;
                    }
                }
                
                setBackground(STANDARD_VALUE_BG);
                setForeground(STANDARD_VALUE_FG);                
                
                if (activitatatiCompleteCount == adto.getEchipe().size()) {
                    setBackground(HIGH_VALUE_BG);
                    setForeground(HIGH_VALUE_FG);
                    List<Boolean> bools = completedMap.get((AnimatorDTO) jList1.getSelectedValue());
                    bools.remove(index);
                    bools.add(index, true);
                    completedMap.put((AnimatorDTO) jList1.getSelectedValue(), bools);
                }
            } else if (value.getClass() == JocDTO.class) {
                JocDTO jdto = (JocDTO) value;
                List<JocDB> jdb = ControllerDB.getInstance().getStoredJocByIdProgram(jdto.getPerioada(), 
                                                                          ControllerDB.getInstance().convert(jdto.getJocGeneral()),
                                                                          SerializeController.getInstance().getProgramActivitatiID());
                int jocuriCompleteCount = 0;
                AnimatorDTO anim = (AnimatorDTO) jList1.getSelectedValue();
                boolean absent = false;
                
                for (JocDB j : jdb) {
                    if (j.getOrganizator().contains("*" + anim.getNumeAnimator())) {
                        jocuriCompleteCount++;
                    }
                    if (j.getAbsent()) absent = true;
                }
                
                setBackground(STANDARD_VALUE_BG);
                setForeground(STANDARD_VALUE_FG);
                
                if (jocuriCompleteCount == jdto.getEchipe().size()) {
                    setBackground(HIGH_VALUE_BG);
                    setForeground(HIGH_VALUE_FG);
                    List<Boolean> bools = completedMap.get((AnimatorDTO) jList1.getSelectedValue());
                    bools.remove(index);
                    bools.add(index, true);
                    completedMap.put((AnimatorDTO) jList1.getSelectedValue(), bools);
                }
                
                if (absent) {
                    setBackground(ABSENT_VALUE_BG);
                    setForeground(ABSENT_VALUE_FG);
                }
            }
            
            if (modelSarcini.lastElement().equals(value)) {
                int counter = 0;
                for (Boolean b : completedMap.get((AnimatorDTO) jList1.getSelectedValue())) {
                    if (b) {
                        counter++;
                    }
                }
                jLabel5.setText(counter + "");
            }
            return superRenderer;
        }
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
