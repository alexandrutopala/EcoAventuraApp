/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import db.ActivitateDB;
import db.EchipaDB;
import db.JocDB;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import service.ControllerDB;

/**
 *
 * @author Alexandru
 */
public class ViewActivitateInternalFrame extends javax.swing.JInternalFrame {
    private EchipaDB echipa;
    private DefaultListModel<EchipaDB> model;
    private JDesktopPane desktop;
    private List<JocDB> storedJocuri;
    private List<ActivitateDB> storedActivitati;
    private ActivitateDB curActivitate = null;
    private JocDB curJoc = null;
    /**
     * Creates new form ViewActivitateInternalFrame
     * @param echipa
     * @param desktop
     */
    public ViewActivitateInternalFrame(EchipaDB echipa, JDesktopPane desktop) {
        super ("Echipa " + echipa.getNumeEchipa());
        initComponents();
        this.echipa = echipa;
        this.desktop = desktop;
        
        model = new DefaultListModel<>();
        
        clearFields();
        buildTree();
        
        jMenuItem1.setText("Sterge");
        jMenuItem1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (JOptionPane.showConfirmDialog(
                        jList1,
                        "Sarcinile selectate vor fi sterse definitiv. Continuati?",
                        "Confirmare",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    
                    stergeNoduri();
                    //buildTree();
                }
            }
        });
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
    }
    
    private void clearFields () {
        jLabel2.setText("");
        jLabel3.setText("");
        jLabel5.setText("");
        jLabel5.setToolTipText(null);
        jLabel7.setText("");
        jLabel9.setText("");
        jLabel11.setText("");
        jLabel13.setText("");
        jLabel16.setText("");
        jLabel14.setVisible(false);
        model.clear();
        jList1.setModel(model);
        curActivitate = null;
        curJoc = null;
    }

    private void populateFields (ActivitateDB a) {
        clearFields();
        curActivitate = a;
        curJoc = null;
        jLabel2.setText(a.getActivitateGeneralaidActivitateGenerala().getNumeActivitateGenerala());
        jLabel3.setText(a.getData());
        jLabel5.setText(a.getOrganizator());
        jLabel5.setToolTipText(a.getOrganizator());
        String perioada = a.getLoactie().subSequence(a.getLoactie().indexOf("$") + 1, a.getLoactie().lastIndexOf("$")).toString();
        jLabel7.setText(a.getLoactie().replace("$" + perioada + "$", ""));
        jLabel9.setText(perioada);
        jLabel11.setText(a.getPost()); 
        jLabel16.setText(a.getIdProgram() + "");
        
        storedActivitati = ControllerDB.getInstance().getStoredActivitateByIdProgram(perioada, a.getActivitateGeneralaidActivitateGenerala(), a.getIdProgram());
        for (ActivitateDB adb : storedActivitati) {
            if (!adb.getEchipaidEchipa().equals(echipa)) {
                model.addElement(adb.getEchipaidEchipa());
            }
        }
        jList1.setModel(model);
    }
    
    private void populateFields (JocDB j) {
        clearFields();
        curActivitate = null;
        curJoc = j;
        jLabel2.setText(j.getJocGeneralidJocGeneral().getNumeJocGeneral());
        jLabel3.setText(j.getData());
        jLabel5.setText(j.getOrganizator());
        jLabel5.setToolTipText(j.getOrganizator());
        jLabel16.setText(j.getIdProgram() + "");
        
        if (!j.getJocGeneralidJocGeneral().toString().equals("penalizare")) {
            String perioada = j.getLocatie().subSequence(j.getLocatie().indexOf("$") + 1, j.getLocatie().lastIndexOf("$")).toString();
            jLabel7.setText(j.getLocatie().replace("$" + perioada + "$", ""));
            jLabel9.setText(perioada);
            
            storedJocuri = ControllerDB.getInstance().getStoredJocByIdProgram(perioada, j.getJocGeneralidJocGeneral(), j.getIdProgram());
            
            for (JocDB jdb : storedJocuri) {
                if (!jdb.getEchipaidEchipa().equals(echipa)) {
                    model.addElement(jdb.getEchipaidEchipa());
                }
            }
            jLabel13.setText(j.getPunctaj() + ""  + (j.getAbsent() == true ? " (absent)" : ""));
        } else {
            jLabel13.setText(j.getPunctaj() + "");
        }
        
        jLabel11.setText(j.getPost());
        jLabel14.setVisible(true);     
                
        jList1.setModel(model);
    }
    
    private void buildTree () {
        List<ActivitateDB> activitati = ControllerDB.getInstance().getActivittatiByEchipa(echipa);
        List<JocDB> jocuri = ControllerDB.getInstance().getJocuriByEchipa(echipa);
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Sarcini");
        DefaultMutableTreeNode activitatiNode = new DefaultMutableTreeNode("Activitati");
        DefaultMutableTreeNode jocuriNode = new DefaultMutableTreeNode("Jocuri");
        
        root.add(activitatiNode);
        root.add(jocuriNode);
        
        String lastDate = "";
        DefaultMutableTreeNode node = null;
        
        for (ActivitateDB adb : activitati) {
            if (!adb.getData().equals(lastDate)) {
                if (node != null) {
                    activitatiNode.add(node);
                }
                node = new DefaultMutableTreeNode(adb.getData());
                lastDate = adb.getData();
            }
            
            node.add(new DefaultMutableTreeNode(adb, false));
        }
        if (node != null) {
            if (node.getChildCount() != 0) activitatiNode.add(node);
        }
        
        
        lastDate = "";
        node = null;
        
        for (JocDB jdb : jocuri) {
            if (!jdb.getData().equals(lastDate)) {
                if (node != null) {
                    jocuriNode.add(node);
                }
                node = new DefaultMutableTreeNode(jdb.getData());
                lastDate = jdb.getData();
            }
            
            node.add(new DefaultMutableTreeNode(jdb, false));
        }
        
        if (node != null) {
            if (node.getChildCount() != 0) jocuriNode.add(node);
        }
        jTree1 = new JTree(root);
        jTree1.setRootVisible(false);        
        jScrollPane1.setViewportView(jTree1);
        jTree1.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent tse) {
                jTree1SelectionChanged(tse);
            }
        });
        jTree1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent me) {
                jTree1MouseClicked(me);
            }
        });
    }
    
    private void stergeNoduri () {
        TreePath [] paths = jTree1.getSelectionPaths();
        DefaultTreeModel treeModel = (DefaultTreeModel) jTree1.getModel();
        DefaultMutableTreeNode node;
        
        if (paths == null) {
            JOptionPane.showMessageDialog(jList1, "Nicio sarcina selectata", "", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        for (TreePath th : paths) {
            node = (DefaultMutableTreeNode) th.getLastPathComponent();
            node.setParent((DefaultMutableTreeNode) th.getParentPath().getLastPathComponent());
            if (node.getUserObject().getClass() == String.class) {
                if (node.getUserObject().equals("Activitati") || node.getUserObject().equals("Jocuri")) {
                    //JOptionPane.showMessageDialog(jList1, "", "", JOptionPane.INFORMATION_MESSAGE);
                    continue;
                }
            }
            
            if (node.isRoot()) continue;
            
            if (node.isLeaf()) {
                if (node.getUserObject().getClass() == ActivitateDB.class) {
                    ControllerDB.getInstance().stergeActivitate((ActivitateDB) node.getUserObject());
                } else if (node.getUserObject().getClass() == JocDB.class) {
                    ControllerDB.getInstance().stergeJoc((JocDB) node.getUserObject());
                }
                try { treeModel.removeNodeFromParent(node); } catch (Exception e) {}
                try {
                    if (node.getParent().getChildCount() == 0) {
                        treeModel.removeNodeFromParent((DefaultMutableTreeNode) node.getParent()); 
                    }
                } catch (Exception e) {}
            } else {
                DefaultMutableTreeNode child;
                for (int i = 0; i < treeModel.getChildCount(node); ++i) {
                    child = (DefaultMutableTreeNode) treeModel.getChild(node, i);
                    
                    if (child.getUserObject().getClass() == ActivitateDB.class) {
                        ControllerDB.getInstance().stergeActivitate((ActivitateDB) child.getUserObject());
                    } else if (child.getUserObject().getClass() == JocDB.class) {
                        ControllerDB.getInstance().stergeJoc((JocDB) child.getUserObject());
                    }
                }
                node.removeAllChildren();
                try {treeModel.removeNodeFromParent(node); } catch (Exception e) {}
            }
            //treeModel.reload();
        }
        clearFields();
    }
 
    
    TreePath newPath = null;
    public void expandPath (TreePath path, int index) { 
        if (index == path.getPathCount()) {
            jTree1.setExpandsSelectedPaths(true);
            jTree1.setSelectionPath(newPath);
        }
        
        if (index == 0) {
            newPath = new TreePath(((DefaultTreeModel) jTree1.getModel()).getRoot());
            expandPath(path, index+1);
            return;
        }
        
        Enumeration children = ((DefaultMutableTreeNode) newPath.getLastPathComponent()).children();
        DefaultMutableTreeNode searchedNode = (DefaultMutableTreeNode) path.getPathComponent(index);
        DefaultMutableTreeNode node = null;
        try {
            while (children.hasMoreElements()) {
                node = (DefaultMutableTreeNode) children.nextElement();
                if (egale(searchedNode, node)) {
                    newPath = newPath.pathByAddingChild(node);
                    expandPath(path, index+1);
                    return;
                }
            }
        } catch (Exception e) {}
    }
    
    public boolean egale (DefaultMutableTreeNode o1, DefaultMutableTreeNode o2){
        if (o1.getUserObject().getClass() == String.class && o2.getUserObject().getClass() == String.class) {
            return o1.getUserObject().equals(o2.getUserObject());
        } else if (o1.getUserObject().getClass() == JocDB.class && o2.getUserObject().getClass() == JocDB.class) {
            if ((o1.getUserObject()).toString().equals(o2.getUserObject().toString()) && ((JocDB) o1.getUserObject()).getLocatie().equals(((JocDB) o2.getUserObject()).getLocatie())) {
                return true;
            }
        } else if (o1.getUserObject().getClass() == ActivitateDB.class && o2.getUserObject().getClass() == ActivitateDB.class) {
            if ((o1.getUserObject().toString().equals(o2.getUserObject().toString()) && ((ActivitateDB) o1.getUserObject()).getLoactie().equals(((ActivitateDB) o2.getUserObject()).getLoactie()))){
                return true;
            }
        }
        return false;
    }
    
    public EchipaDB getEchipa () {
        return echipa;
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
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");
        jPopupMenu1.add(jMenuItem1);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jSplitPane1.setDividerLocation(250);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("sarcini");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("activitati");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("jocuri");
        treeNode1.add(treeNode2);
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.setRootVisible(false);
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTree1MouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jButton1.setText("<<Iesire");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
        );

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel2MouseEntered(evt);
            }
        });

        jLabel1.setText("Activitatea :");

        jLabel2.setText("jLabel2");

        jLabel3.setText("jLabel2");

        jLabel4.setText("Data :");

        jLabel5.setText("jLabel2");

        jLabel6.setText("Organizatori :");

        jLabel7.setText("jLabel2");

        jLabel8.setText("Locatie :");

        jLabel9.setText("jLabel2");

        jLabel10.setText("Perioada :");

        jLabel11.setText("jLabel2");

        jLabel12.setText("Post :");

        jLabel13.setText("jLabel2");

        jLabel14.setText("Punctaj :");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Alte echipe care au realizat activitatea"));

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
        jScrollPane2.setViewportView(jList1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Nota"));

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setText("<html>\"*\" - marcheaza un animator care si-a finalizat activitatea</html>");
        jLabel15.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel15.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jLabel16.setText("jLabel2");

        jLabel17.setText("ID Program :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel16))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3) {
            jPopupMenu1.show(jTree1, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTree1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        if (jList1.getSelectedValue() != null && evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
            JInternalFrame [] frames = desktop.getAllFrames();
            boolean found = false;
            final EchipaDB selected = (EchipaDB) jList1.getSelectedValue();
            final TreePath path = jTree1.getSelectionPath();
            String newData;
            String oldData = "sh#t";
            
            newData = getData(selected, (curActivitate != null));
            if (!newData.equals("")) {
                oldData = (String) ((DefaultMutableTreeNode) path.getParentPath().getLastPathComponent()).getUserObject();
                ((DefaultMutableTreeNode) path.getParentPath().getLastPathComponent()).setUserObject(newData);
            }
            
            for (int i = 0; i < frames.length && !found; ++i) {
                if (((ViewActivitateInternalFrame) frames[i]).getEchipa().equals(selected)) {                    
                    frames[i].toFront();
                    ((ViewActivitateInternalFrame) frames[i]).expandPath(path, 0);
                    if (!"sh#t".equals(oldData)) {
                        ((DefaultMutableTreeNode) path.getParentPath().getLastPathComponent()).setUserObject(oldData);
                    }
                    found = true;
                }
            }
            
            if (found) return;
            
            ViewActivitateInternalFrame iFrame = new ViewActivitateInternalFrame(selected, desktop);
            iFrame.setVisible(true);            
            desktop.add(iFrame);
            iFrame.toFront();
            iFrame.setLocation(this.getX() + 20, this.getY() + 20);
            iFrame.expandPath(path, 0);
            if (!"sh#t".equals(oldData)) {
                ((DefaultMutableTreeNode) path.getParentPath().getLastPathComponent()).setUserObject(oldData);
            }
        }
    }//GEN-LAST:event_jList1MouseClicked

    private String getData (EchipaDB e, boolean cautaInActivitati) {
        
        if (cautaInActivitati) {
            for (ActivitateDB a : storedActivitati) {
                if (a.getEchipaidEchipa().equals(e)) {
                    return a.getData();
                }
            }
        } else {
            for (JocDB j : storedJocuri) {
                if (j.getEchipaidEchipa().equals(e)) {
                    return j.getData();
                }
            }
        }
        return "";
    }
    
    
    private void jTree1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseEntered
        HelperFrame.getInstance().post(
                "Istoricul activitatilor",
                "Pentru a vizualiza informatii privind o activitate anume, o putem cauta in arborele actual, in functie de data si tip. ("
                        + "Activitate standard sau Joc). Pentru a sterge activitatea selectata se apasa click-dreapta pe aceasta si dupa, "
                        + "optiunea 'Stergere'. Poti selecta mai multe activitati deodata folosind tastele 'Ctrl' si 'Shift'");
    }//GEN-LAST:event_jTree1MouseEntered

    private void jList1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseEntered
        HelperFrame.getInstance().post(
                "Lista echipe",
                "In aceasta lista sunt afisate toate celelalte echipe care au realizat activitatea selectata. Dublu-click pe o echipa "
                        + "pentru a mai multe detalii.");
    }//GEN-LAST:event_jList1MouseEntered

    private void jPanel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel2MouseEntered

    private void jTree1SelectionChanged (TreeSelectionEvent tse) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        if (node.getUserObject().getClass() == ActivitateDB.class || node.getUserObject().getClass() == JocDB.class) {
            if (ActivitateDB.class == node.getUserObject().getClass()) {
                populateFields((ActivitateDB) node.getUserObject());
            } else {
                populateFields((JocDB) node.getUserObject());
            }
        }else {
            clearFields();
        }
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
