/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import dto.EchipaDTO;
import dto.JoinDTO;
import dto.UserDTO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import service.SerializeController;

/**
 *
 * @author Marius
 */
public class IstoricFrame extends javax.swing.JFrame {
    private static final String CLASS_ID = "IstoricFrame";
    private static final String SAVE_HISTORY_CONFIG = "historyCongif";
    public static final String HISTORY_PATH = "./obj/history.srz";
    private static IstoricFrame singleton = null;
    private final DefaultMutableTreeNode root;
    private DefaultTreeModel model;
    private boolean moved = false;
    private HashMap<String, Object> classConfigs;
    
    /**
     * Creates new form IstoricFrame
     */
    private IstoricFrame() {
        super ("Istoric activitati primite");
        initComponents();
        
        JTree savedTree = loadHistory();
        model = (DefaultTreeModel) (savedTree != null ? savedTree.getModel() : null);
        
        if (savedTree == null || model == null) {            
            root = new DefaultMutableTreeNode("Istoric");        
            model = new DefaultTreeModel(root);
            jTree1 = new JTree();
            jTree1.setModel(model);
        } else {
            jTree1 = savedTree;
            model = (DefaultTreeModel) jTree1.getModel();
            root = (DefaultMutableTreeNode) model.getRoot();
        }
        
        // read configs
        try {
            classConfigs = (HashMap<String, Object>) SerializeController.getInstance().readCongif(CLASS_ID);
            boolean checked = (Boolean) classConfigs.get(SAVE_HISTORY_CONFIG);
            jCheckBoxMenuItem1.setSelected(checked);
        } catch (Exception e) {}
        
        jScrollPane1.setViewportView(jTree1);
        jTree1.setRootVisible(true);
        
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public static IstoricFrame getInstance () {
        if (singleton == null) {
            singleton = new IstoricFrame();
        }
        return singleton;
    }
    
    public static boolean isExistingInstance () {
        return (singleton != null);
    }
    
    public void setFramePosition (int x, int y) {
        if (moved) return;
        this.setLocation(x, y);
    }
    
    public void addNode (DefaultMutableTreeNode node) {
        model.insertNodeInto(node, root, root.getChildCount());
    }
    
    public void addRegisters (List<JoinDTO> joins, UserDTO user) {
        
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(
                new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(
                        Calendar.getInstance().getTime()
                ) + " - Animatorul " + user.getUsername() + " a trimis noi activitati");
        
        for (JoinDTO j : joins) {
            if (j.getActivitate() != null) {
                DefaultMutableTreeNode activ = new DefaultMutableTreeNode(j.getActivitate().toString());
                DefaultMutableTreeNode echipa = null;
                
                for (EchipaDTO e : j.getActivitate().getEchipe()) {
                    echipa = new DefaultMutableTreeNode(e.toString());
                    activ.add(echipa);
                }
                
                node.add(activ);
            } else if (j.getJoc() != null) {
                DefaultMutableTreeNode joc = new DefaultMutableTreeNode(j.getJoc().toString());
                DefaultMutableTreeNode echipa = null;
                
                for (EchipaDTO e : j.getJoc().getEchipe()) {
                    echipa = new DefaultMutableTreeNode(e.toString());
                    joc.add(echipa);
                }
                
                node.add(joc);
            }
        }
        
        addNode(node);
    }
    
    public final JTree loadHistory () {
        ObjectInputStream in;
        FileInputStream fin;
        JTree tree = new JTree();
        try {
           fin = new FileInputStream(HISTORY_PATH);
           in = new ObjectInputStream(fin);
           
           DefaultTreeModel model = (DefaultTreeModel) in.readObject();
           this.model = model;           
           tree.setModel(model);
           
           in.close();
           fin.close();
           
           return tree;
        } catch (Exception e) {
            return null;
        }
    }
    
    public final boolean saveHistory (JTree tree) {
        if (!jCheckBoxMenuItem1.isSelected()) {
            File f = new File(HISTORY_PATH);            
            if (!f.delete()) f.deleteOnExit();
            return true;
        }
        ObjectOutputStream out;
        FileOutputStream fout;
        
        try {
            fout = new FileOutputStream(HISTORY_PATH);
            out = new ObjectOutputStream(fout);
            
            out.writeObject(tree.getModel());
            out.flush();
            
            out.close();
            fout.close();
            
            return true;
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(this, "Historicul nu a putut fi salvat.", "Eroare de scriere", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
       
    public final boolean saveHistory () {
        if (!jCheckBoxMenuItem1.isSelected()) {
            File f = new File(HISTORY_PATH);            
            if (!f.delete()) f.deleteOnExit();
            return true;
        }
        
        ObjectOutputStream out;
        FileOutputStream fout;
        
        try {
            fout = new FileOutputStream(HISTORY_PATH);
            out = new ObjectOutputStream(fout);
                        
            out.writeObject(jTree1.getModel());
            out.flush();
            
            out.close();
            fout.close();
            
            return true;
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(this, "Historicul nu a putut fi salvat.", "Eroare de scriere", JOptionPane.ERROR_MESSAGE);
            return false;
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
        jTree1 = new javax.swing.JTree();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jScrollPane1.setViewportView(jTree1);

        jMenu1.setText("Optiuni");

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("Pastreaza istoricul");
        jMenu1.add(jCheckBoxMenuItem1);

        jMenuItem1.setText("Sterge istoricul");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        //moved = true;
    }//GEN-LAST:event_formComponentMoved

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        saveHistory(null);
        singleton = null;
        dispose();
        IstoricFrame.getInstance().setLocation(getLocation());
        IstoricFrame.getInstance().setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (classConfigs == null) {
            classConfigs = new HashMap<>();
        }
        
        classConfigs.put(SAVE_HISTORY_CONFIG, jCheckBoxMenuItem1.isSelected());
        SerializeController.getInstance().writeConfig(CLASS_ID, classConfigs);
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
