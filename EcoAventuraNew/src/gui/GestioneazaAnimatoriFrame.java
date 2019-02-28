/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import db.AnimatorDB;
import db.UserDB;
import dialogs.AdaugaAnimatorFrame;
import dto.UserDTO;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import service.ControllerDB;

/**
 *
 * @author Alexandru
 */
public class GestioneazaAnimatoriFrame extends javax.swing.JFrame {
    private final static int STANDARD_LIST_WIDTH = 190;
    private DefaultListModel<AnimatorDB> modelDisponibili;
    private DefaultListModel<AnimatorDB> modelIndisponibili;
    /**
     * Creates new form GestioneazaAnimatoriFrame
     */
    public GestioneazaAnimatoriFrame() {
        super ("Gestiune Animatori");
        initComponents();
        
        // init models
        
        modelDisponibili = new DefaultListModel<>();
        modelIndisponibili = new DefaultListModel<>();
        
        modelDisponibili.clear();
        modelIndisponibili.clear();
        
        List<AnimatorDB> animatori = ControllerDB.getInstance().getAllAnimatori();
        
        for (AnimatorDB a : animatori){
            if (a.getDisponibilAnimator()){
                modelDisponibili.addElement(a);
            } else {
                modelIndisponibili.addElement(a);
            }
        }
        
        jList1.setModel(modelDisponibili);
        jList2.setModel(modelIndisponibili);
        
        // init menu items
        
        jMenuItem1.setText("Editeaza nume");
        jMenuItem2.setText("Sterge");
        
        jList1.setFixedCellWidth(STANDARD_LIST_WIDTH);
        jList2.setFixedCellWidth(STANDARD_LIST_WIDTH);
        
        setResizable(false);
        setLocationRelativeTo(null);
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

        jButton1 = new javax.swing.JButton();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();

        jButton1.setText("jButton1");

        jMenuItem1.setText("jMenuItem1");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem1MouseClicked(evt);
            }
        });
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("jMenuItem2");
        jMenuItem2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem2MouseClicked(evt);
            }
        });
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Disponibili");

        jButton2.setText(">>");
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
                jButton3MouseEntered(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel2.setText("Indisponibili");

        jButton4.setText("+ Adauga Animator");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

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
        jScrollPane3.setViewportView(jList1);

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
        jScrollPane4.setViewportView(jList2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(jScrollPane3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(jScrollPane4))))
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(16, 16, 16)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (jList1.getSelectedValue() != null){
            AnimatorDB a = (AnimatorDB) jList1.getSelectedValue();            
            a.setDisponibilAnimator(false);
            try {
                ControllerDB.getInstance().updateAnimator(a);
                a.setDisponibilAnimator(true);
                modelDisponibili.removeElement(a);
                a.setDisponibilAnimator(false);
                modelIndisponibili.addElement(a);
                jList1.setModel(modelDisponibili);
                jList2.setModel(modelIndisponibili);
            } catch (Exception ex) {
                Logger.getLogger(GestioneazaAnimatoriFrame.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Eroare la modificarea animatorului");
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (jList2.getSelectedValue() != null){
            AnimatorDB a = (AnimatorDB) jList2.getSelectedValue();
            a.setDisponibilAnimator(true);
            try {
                ControllerDB.getInstance().editeazaAnimator(a);
                a.setDisponibilAnimator(false);
                modelIndisponibili.removeElement(a);
                a.setDisponibilAnimator(true);
                modelDisponibili.addElement(a);
                jList1.setModel(modelDisponibili);
                jList2.setModel(modelIndisponibili);
            } catch (Exception e){
                JOptionPane.showMessageDialog(this, "Eroare la modificarea animatorului");
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        new AdaugaAnimatorFrame(this).setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jMenuItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MouseClicked
        
    }//GEN-LAST:event_jMenuItem1MouseClicked

    private void jMenuItem2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MouseClicked
        JList list = (JList) jPopupMenu1.getInvoker();
        AnimatorDB a  = (AnimatorDB) list.getSelectedValue();
        
        if (SerieActivaFrame.isExistingRunningProgram()) {
            int input = JOptionPane.showConfirmDialog(this, "Nu se pot face modificari asupra animatorului cat timp exista un program activ. <html><br></html>Doriti sa resetati programul pentru a realiza modificarile?", "Eroare", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);

            if (input == JOptionPane.YES_OPTION) {
                SerieActivaFrame.reseteazaSarcini(this, true);
            } else {
                return;
            }
        }
        
        int input = JOptionPane.showConfirmDialog(this, "Esti sigur ca vrei sa stergi animatorul?", "Atentionare", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (input != JOptionPane.YES_OPTION) return;
        
        String nume = a.getNumeAnimator();
        boolean rez = ControllerDB.getInstance().stergeAnimator(a);
        
        if (rez) {
            UserDB user = ControllerDB.getInstance().getUser(nume);
            
            if (user != null) {
                if (user.getAcces() == UserDTO.ACCES_ANIMATOR) {
                    ControllerDB.getInstance().stergeUser(user);
                }
            }
            if (a.getDisponibilAnimator()) {
                modelDisponibili.removeElement(a);
                jList1.setModel(modelDisponibili);
            } else {
                modelIndisponibili.removeElement(a);
                jList2.setModel(modelIndisponibili);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Animatorul " + a.getNumeAnimator() + " nu a putut fi eliminat");
        }
    }//GEN-LAST:event_jMenuItem2MouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (SerieActivaFrame.isExistingRunningProgram()) {
            int input = JOptionPane.showConfirmDialog(this, "<html>Nu se pot face modificari asupra animatorului cat timp exista un program activ.<br>Doriti sa resetati programul pentru a realiza modificarile?</html>", "Eroare", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);

            if (input == JOptionPane.YES_OPTION) {
                SerieActivaFrame.reseteazaSarcini(this, true);
            } else {
                return;
            }
        }
        
        JList list = (JList) jPopupMenu1.getInvoker();
        AnimatorDB a  = (AnimatorDB) list.getSelectedValue();
        String nume = JOptionPane.showInputDialog(this, "Introdu noul nume :", a.getNumeAnimator());
        if (nume.equals("")){
            return;
        }
        if (nume.contains(" ")) {
            JOptionPane.showMessageDialog(this, "Numele de utilizator nu are voie sa contina caractere albe (space, enter, etc.).", "Format gresit", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (a.getDisponibilAnimator()){
            modelDisponibili.removeElement(a);
        } else {
            modelIndisponibili.removeElement(a);
        }
        String oldName = a.getNumeAnimator();
        a.setNumeAnimator(nume);
        
        boolean rez = ControllerDB.getInstance().editeazaAnimator(a);
        if (!rez){
            JOptionPane.showMessageDialog(this, "Numele animatorului nu a putut fi modificat");
        } 
        
        UserDB user = ControllerDB.getInstance().getUser(oldName);
        if (user != null) {
            user.setUsername(nume);
            ControllerDB.getInstance().updateUser(user);
        }
        
        if (a.getDisponibilAnimator()){
            modelDisponibili.addElement(a);
        } else {
            modelIndisponibili.addElement(a);
        }
        
        jList1.setModel(modelDisponibili);
        jList2.setModel(modelIndisponibili);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JList list = (JList) jPopupMenu1.getInvoker();
        AnimatorDB a  = (AnimatorDB) list.getSelectedValue();
        
        if (SerieActivaFrame.isExistingRunningProgram()) {
            int input = JOptionPane.showConfirmDialog(this, "<html>Nu se pot face modificari asupra animatorului cat timp exista un program activ. <br>Doriti sa resetati programul pentru a realiza modificarile?</html>", "Eroare", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);

            if (input == JOptionPane.YES_OPTION) {
                SerieActivaFrame.reseteazaSarcini(this, true);
            } else {
                return;
            }
        }
        
        int input = JOptionPane.showConfirmDialog(this, "Esti sigur ca vrei sa stergi animatorul?", "Atentionare", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (input != JOptionPane.YES_OPTION) return;
        
        String nume = a.getNumeAnimator();
        boolean rez = ControllerDB.getInstance().stergeAnimator(a);
        
        if (rez) {
            UserDB user = ControllerDB.getInstance().getUser(nume);
            
            if (user != null) {
                if (user.getAcces() == UserDTO.ACCES_ANIMATOR) {
                    ControllerDB.getInstance().stergeUser(user);
                }
            }
            if (a.getDisponibilAnimator()) {
                modelDisponibili.removeElement(a);
                jList1.setModel(modelDisponibili);
            } else {
                modelIndisponibili.removeElement(a);
                jList2.setModel(modelIndisponibili);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Animatorul " + a.getNumeAnimator() + " nu a putut fi eliminat");
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

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

    private void jList1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseEntered
        HelperFrame.getInstance().post(
                "Lista animatorilor disponibili",
                "Doar animatori din aceasta lista vor putea fi vizualizati in planificarea programului de activitati. "
                        + "Click dreapta pentru mai multe optiuni.");
    }//GEN-LAST:event_jList1MouseEntered

    private void jList2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseEntered
        HelperFrame.getInstance().post(
                "Lista animatorilor indisponibili",
                "O evidenta a animatorilor care momentan nu pot participa la desfasurarea planului de activitati. "
                        + "Click dreapta pentru mai multe optiuni.");
    }//GEN-LAST:event_jList2MouseEntered

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        HelperFrame.getInstance().post(
                "",
                "Seteaza starea animatorului selectat in 'Indisponibil'.");
    }//GEN-LAST:event_jButton2MouseEntered

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        HelperFrame.getInstance().post(
                "",
                "Seteaza starea animatorului selectat in 'Disponibil'.");
    }//GEN-LAST:event_jButton3MouseEntered

    public void adaugaAnimator (AnimatorDB a){
        if (a.getDisponibilAnimator()){
            modelDisponibili.addElement(a);
            jList1.setModel(modelDisponibili);
        } else {
            modelIndisponibili.addElement(a);
            jList2.setModel(modelIndisponibili);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}