/*D
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import db.EchipaDB;
import db.MembruEchipaDB;
import db.SerieDB;
import dialogs.InfoMembruFrame;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import service.ControllerDB;
import service.SerializeController;

/**
 *
 * @author Alexandru
 */
public class GestioneazaEchipeFrame extends javax.swing.JFrame {
    private final static int MAX_WIDTH = 250;
    private SerieDB serie;
    private DefaultListModel<MembruEchipaDB> model1;
    private DefaultListModel<MembruEchipaDB> model2;
    private DefaultComboBoxModel<EchipaDB> comboModel1;
    private DefaultComboBoxModel<EchipaDB> comboModel2;
    private EchipaDB selectedItem1;
    private EchipaDB selectedItem2;
    private boolean isDone;
    private boolean inactiv;
    /**
     * Creates new form GestioneazaEchipeFrame
     */
    public GestioneazaEchipeFrame(SerieDB serie, boolean inactiv) {
        super ("Gestioneaza echipele seriei " + serie.getNumarSerie());
        initComponents();
        
        this.serie = serie;       
        this.inactiv = inactiv;
        
        model1 = new DefaultListModel<>();
        model2 = new DefaultListModel<>();
        comboModel1 = new DefaultComboBoxModel<>();
        comboModel2= new DefaultComboBoxModel<>();
        
        refreshEchipe();
        
        jMenuItem1.setText("Info");
        jMenuItem2.setText("Editeaza nume");
        jMenuItem3.setText("Sterge");
        
        // inactiv fragment
        jButton5.setEnabled(!inactiv);
        jButton6.setEnabled(!inactiv);
        jButton1.setEnabled(!inactiv);
        jButton2.setEnabled(!inactiv);
        jButton7.setEnabled(!inactiv);
        jButton8.setEnabled(!inactiv);
        jMenuItem2.setEnabled(!inactiv);
        jMenuItem3.setEnabled(!inactiv);
        
        jList1.setFixedCellWidth(MAX_WIDTH);
        jList2.setFixedCellWidth(MAX_WIDTH);
        jComboBox1.setPrototypeDisplayValue("Echipe");
        jComboBox2.setPrototypeDisplayValue("Echipe");
        
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
    }
    
    public void refreshEchipe() {
       try {
           isDone = false;
           
           comboModel1.removeAllElements();
           comboModel2.removeAllElements();
           model1.clear();
           model2.clear();
           
           List<EchipaDB> echipe = ControllerDB.getInstance().getEchipeBySerie(serie);
           
           if (echipe.size() < 2){
               jButton1.setEnabled(false);
               jButton2.setEnabled(false);
           } else {
               jButton1.setEnabled(true && !inactiv);
               jButton2.setEnabled(true && !inactiv);
           }        
           
           
           if (selectedItem1 == null && echipe.size() > 0){
               selectedItem1 = echipe.get(0);
           }  
           echipe.remove(selectedItem1);
           
           if (selectedItem2 == null && echipe.size() > 1) {
               selectedItem2 = echipe.get(1);
           }
           echipe.remove(selectedItem2);
           
           if (selectedItem1 == null) {
               jButton3.setEnabled(false);
               jButton7.setEnabled(false);
           } else {
               jButton3.setEnabled(true);
               jButton7.setEnabled(true && !inactiv);
           }
           
           if (selectedItem2 == null) {
               jButton4.setEnabled(false);
               jButton8.setEnabled(false);
           } else {
               jButton4.setEnabled(true);
               jButton8.setEnabled(true && !inactiv);
           }
           
           comboModel1.addElement(selectedItem1);
           comboModel2.addElement(selectedItem2);
           
           for (int i = 0; i < echipe.size(); ++i){
               comboModel1.addElement(echipe.get(i));
               comboModel2.addElement(echipe.get(i));
           }
           
           jComboBox1.setModel(comboModel1);
           jComboBox2.setModel(comboModel2);
           
           if (selectedItem1 != null){
               List<MembruEchipaDB> membri= ControllerDB.getInstance().getMembriByEchipa(selectedItem1);
               for (MembruEchipaDB m : membri){
                   model1.addElement(m);
               }
           }
           
           if (selectedItem2 != null){
               List<MembruEchipaDB> membri = ControllerDB.getInstance().getMembriByEchipa(selectedItem2);
               for (MembruEchipaDB m : membri){
                   model2.addElement(m);
               }
           }
           
           jList1.setModel(model1);
           jList2.setModel(model2);
           
           isDone = true;
       } catch (Exception e){
           
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();

        jMenuItem1.setText("jMenuItem1");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("jMenuItem2");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        jMenuItem3.setText("jMenuItem3");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem3);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Echipa ");

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

        jLabel2.setText("Echipa ");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboBox1MouseEntered(evt);
            }
        });
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setToolTipText("");

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jList2);

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

        jButton3.setText("Info");
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

        jButton4.setText("Info");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton3MouseEntered(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("+ Adauga Membru");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("+ Adauga Echipa");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Elimina");
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

        jButton8.setText("Elimina");
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton7MouseEntered(evt);
            }
        });
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
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
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton6)
                            .addComponent(jButton5))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addContainerGap(192, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (jList2.getSelectedValue() != null){
            MembruEchipaDB membru = (MembruEchipaDB) jList2.getSelectedValue();
            membru.setEchipaidEchipa((EchipaDB) jComboBox1.getSelectedItem());
            
            try {
                ControllerDB.getInstance().updateMembru(membru);
                refreshEchipe();
            } catch (Exception e){
                JOptionPane.showMessageDialog(this, "Eroare la modificarea membrului");
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (!isDone) return;
        selectedItem1 = (EchipaDB) jComboBox1.getSelectedItem();
        refreshEchipe();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        if (!isDone) return;
        selectedItem2 = (EchipaDB) jComboBox2.getSelectedItem();
        refreshEchipe();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        
        HashMap<String, EchipaDB> culori = SerializeController.getInstance().deserializeazaCulori();
        int counter = 0;
        for (String str : culori.keySet()) {
            if (culori.get(str) == null) {
                counter++;
            }
        }
        if (counter < 1) {
            int input = JOptionPane.showConfirmDialog(this, "<html>In momentul de fata nu exista suficiente culori disponibile pentru fiecare echipa.<br> Adaugati acum?</html>", "Atentie", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (input == JOptionPane.YES_OPTION) {
                new ColorsFrame(this, true).setVisible(true);
                culori = SerializeController.getInstance().deserializeazaCulori();
                counter = 0;
                for (String str : culori.keySet()) {
                    if (culori.get(str) == null) {
                        counter++;
                    }
                }
                if (counter < 1) { 
                    JOptionPane.showMessageDialog(this, "Culori insuficiente", null, JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else {
                return;
            }
        }
        AdaugaEchipaFrame aef = new AdaugaEchipaFrame(serie, 1, this);
        aef.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        MainFrame.openFrame(aef, true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jList1.getSelectedValue() != null) {
            MembruEchipaDB membru = (MembruEchipaDB) jList1.getSelectedValue();
            membru.setEchipaidEchipa((EchipaDB) jComboBox2.getSelectedItem());
            
            try {
                ControllerDB.getInstance().updateMembru(membru);
                refreshEchipe();
            } catch (Exception e){
                JOptionPane.showMessageDialog(this, "Eroare la modificarea membrului");
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        String nume = JOptionPane.showInputDialog(this, "Introdu numele membrului : ");
        MembruEchipaDB m = ControllerDB.getInstance().adaugaMembru(nume, selectedItem1);
        
        if (m != null){
            refreshEchipe();
        } else {
            JOptionPane.showMessageDialog(this, "Membrul nu a putut fi adaugat");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new EditeazaEchipaFrame(selectedItem1, this, inactiv).setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        new EditeazaEchipaFrame(selectedItem2, this, inactiv).setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        if (SerieActivaFrame.isExistingRunningProgram()) {
            JOptionPane.showMessageDialog(this, "Echipa nu poate fi eliminata in timpul desfasurarii activitatilor", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (selectedItem1 != null){
            if (JOptionPane.showConfirmDialog(this, "Esti sigur ca vrei sa elimini echipa " + selectedItem1.getNumeEchipa()) == JOptionPane.OK_OPTION){
                if (ControllerDB.getInstance().stergeEchipa(selectedItem1)){
                    selectedItem1 = null;
                    refreshEchipe();
                } else {
                    JOptionPane.showMessageDialog(this, "Echipa nu a putut fi stearsa");
                    refreshEchipe();
                }
            }
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (SerieActivaFrame.isExistingRunningProgram()) {
            JOptionPane.showMessageDialog(this, "Echipa nu poate fi eliminata in timpul desfasurarii activitatilor", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (selectedItem2 != null){
            if (JOptionPane.showConfirmDialog(this, "Esti sigur ca vrei sa elimini echipa " + selectedItem2.getNumeEchipa()) == JOptionPane.OK_OPTION){
                if (ControllerDB.getInstance().stergeEchipa(selectedItem2)){
                    selectedItem2 = null;
                    refreshEchipe();
                } else {
                    JOptionPane.showMessageDialog(this, "Echipa nu a putut fi stearsa");
                    refreshEchipe();
                }
            }
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        new InfoMembruFrame( ( (MembruEchipaDB) ((JList) jPopupMenu1.getInvoker()).getSelectedValue() )).setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        if (jList1.getSelectedValue() != null && evt.getButton() == MouseEvent.BUTTON3){
            jPopupMenu1.show(jList1, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jList1MouseClicked

    private void jList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseClicked
        if (jList2.getSelectedValue() != null && evt.getButton() == MouseEvent.BUTTON3){
            jPopupMenu1.show(jList2, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jList2MouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        MembruEchipaDB m = ( (MembruEchipaDB) ((JList) jPopupMenu1.getInvoker()).getSelectedValue() );
        String nume = JOptionPane.showInputDialog(this, "Introdu noul nume : ", m.getNumeMembruEchipa());
        
        if (nume.equals("")) return;
        
        m.setNumeMembruEchipa(nume);
        
        try {
            ControllerDB.getInstance().updateMembru(m);
            refreshEchipe();
        } catch (Exception e){
            JOptionPane.showMessageDialog(this, "Membrul nu a putut fi modificat");
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        try {
            MembruEchipaDB m = ( (MembruEchipaDB) ((JList) jPopupMenu1.getInvoker()).getSelectedValue() );      
        
            if (ControllerDB.getInstance().stergeMembru(m)) {
                refreshEchipe();
            } else {
                JOptionPane.showMessageDialog(this, "Membrul nu a putut fi sters");
            }
            
        } catch (Exception e){
            JOptionPane.showMessageDialog(this, "Membrul nu a putut fi sters");
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jComboBox1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox1MouseEntered
        HelperFrame.getInstance().post(
                "Meniu echipe",
                "Reprezinta meniul din care se poate alege echipa ale carei informatii sa fie afisate in fereastra.");
    }//GEN-LAST:event_jComboBox1MouseEntered

    private void jButton7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseEntered
        HelperFrame.getInstance().post(
                "Eliminare echipa",
                "Optiunea de stergere a echipe selectate in meniul de mai sus.\n");
    }//GEN-LAST:event_jButton7MouseEntered

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        HelperFrame.getInstance().post(
                "Informatii echipa",
                "Lanseaza o noua fereastra in care se pot viuzaliza sau modifica informatiile privitoare la echipa "
                        + "selectata in meniul de mai sus.");
    }//GEN-LAST:event_jButton3MouseEntered

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        HelperFrame.getInstance().post(
                "Mutare membru",
                "Folosind aceasta optiune, putem transfera membrul selectat/membrii selectati din echipa aleasa din partea stanga, "
                        + "in echipa aleasa in partea dreapta. \n");
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        HelperFrame.getInstance().post(
                "Mutare membru",
                "Folosind aceasta optiune, putem transfera membrul selectat/membrii selectati din echipa aleasa din partea dreapta, "
                        + "in echipa aleasa in partea stanga. \n");
    }//GEN-LAST:event_jButton2MouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
