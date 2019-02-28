/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Magazin.gui;

import Magazin.db.ProdusDB;
import Magazin.db.TranzactieDB;
import Magazin.dialogs.AdaugaProdusDialog;
import Magazin.service.MagazinController;
import dialogs.PermissionFrame;
import dto.UserDTO;
import gui.HelperFrame;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import main.AsyncTask;
import main.Event;

/**
 *
 * @author Marius
 */
public class MagazinFrame extends javax.swing.JFrame {
    private final static int MAX_LENGTH = 199;
    private final static int MIN_WIDTH = 5;
    private final static int MAX_WIDTH = 300;
    private final static String [] HEADERS = new String [] {"Nr. crt.", "Denumire", "Pret", "Stoc"};
    private String [][] tableContent;
    private DefaultTableModel model;
    private List<ProdusDB> produse;
    
    /**
     * Creates new form MagazinFrame
     */
    public MagazinFrame() {
        super("Magazin");
        initComponents();
        
        produse = MagazinController.getInstance().findAllProduse();
        parseContent();
        model = new DefaultTableModel(tableContent, HEADERS){
            @Override
            public boolean isCellEditable(int row, int column) {
               //all cells false
               return false;
            }
        };
        
        // prepare pop-up menu
        jMenuItem4.setText("Vinde o bucata");
        jMenuItem2.setText("Vinde bucati");
        jMenuItem3.setText("Adauga bucati");
        jMenuItem5.setText("Sterge produsul");
        jMenuItem6.setText("Modifica");
        jMenuItem11.setText("Returneaza bucati");
        
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.setModel(model);        
        
        jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, 
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component c = super.getTableCellRendererComponent(table, 
                    value, isSelected, hasFocus, row, column);
                
                ProdusDB p = produse.get(row);
                
                if (isSelected) {
                    c.setBackground(Color.BLUE);
                    c.setForeground(Color.WHITE);
                } else if (p.getStoc() == 0) {
                    c.setBackground(Color.RED);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                
                return c;
            }
           
        });
        
        jLabel1.setText("");
        jLabel2.setText("Caut \"\"");
        createKeybindings(jTable1);
                
        resizeColumnWidth(jTable1);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void createKeybindings(JTable table) {
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
            table.getActionMap().put("Enter", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {}
            });
    }
    
    private void refreshContent () {
        produse = MagazinController.getInstance().findAllProduse();
        parseContent();
        model.setDataVector(tableContent, HEADERS);
        jTable1.setModel(model);
        resizeColumnWidth(jTable1);
    }

    private void parseContent () {        
        tableContent = new String[produse.size()][4];
        
        int i = 0;
        for (ProdusDB p : produse) {
            tableContent[i][0] = (i + 1) + "";
            tableContent[i][1] = p.getDenumire();
            tableContent[i][2] = p.getPret() + "";
            tableContent[i][3] = p.getStoc() + "";
            i++;
        }
    }
    
    
    public final void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = MIN_WIDTH; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > MAX_WIDTH)
                width = MAX_WIDTH;
            columnModel.getColumn(column).setPreferredWidth(width);
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
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();

        jMenuItem7.setText("jMenuItem7");
        jPopupMenu1.add(jMenuItem7);

        jMenuItem4.setText("jMenuItem4");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem4);

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

        jMenuItem6.setText("jMenuItem6");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem6);

        jMenuItem11.setText("jMenuItem11");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem11);

        jMenuItem5.setText("jMenuItem5");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem5);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTable1MouseEntered(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTable1KeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        jMenu1.setText("Optiuni");

        jMenuItem1.setText("Adauga produs");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem8.setText("Statistica");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenuItem9.setText("Sterge toate produsele");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem9);

        jMenuItem10.setText("Sterge toate tranzactiile");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem10);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Instructiuni");

        jMenuItem12.setText("Afiseaza instructiuni");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem12);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        AdaugaProdusDialog dialog = new AdaugaProdusDialog(this, true, null);
        dialog.setVisible(true);
        ProdusDB p = dialog.getProdus();
        
        if (p == null) return;
        
        p = MagazinController.getInstance().adaugaProdus(p.getDenumire(), p.getPret(), p.getStoc());
        MagazinController.getInstance().createTranzactie(true, p.getStoc(), p);
        refreshContent();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        
        int prodIndex = jTable1.getSelectedRow();
        if (prodIndex == -1) return;
        
        ProdusDB p = produse.get(prodIndex);
        int bucati; 
        try {
            bucati = Integer.parseInt(JOptionPane.showInputDialog(this, "Numar bucati vandute: "));
            if (bucati > p.getStoc()) {
                throw new Exception("Stoc insuficient");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Date invalide", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }       
        
        p.setStoc(p.getStoc() - bucati);
        
        if (MagazinController.getInstance().updateProdus(p)) {
            model.setValueAt(p.getStoc(), prodIndex, 3); 
            jTable1.repaint();
            TranzactieDB t = MagazinController.getInstance().createTranzactie(false, bucati, p);
            p.getTranzactieDBCollection().add(t);
            jLabel1.setText("S-au vandut : " + bucati + " bucati de " + p.getDenumire());
        } 
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        int prodIndex = jTable1.getSelectedRow();
        if (prodIndex == -1) return;
        
        ProdusDB p = produse.get(prodIndex);
        if (p.getStoc() < 1) {
            JOptionPane.showMessageDialog(this, "Stoc insuficient", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int bucati = 1;
        
        p.setStoc(p.getStoc() - bucati);
        
        if (MagazinController.getInstance().updateProdus(p)) {
            model.setValueAt(p.getStoc(), prodIndex, 3);                 
            jTable1.repaint();
            TranzactieDB t = MagazinController.getInstance().createTranzactie(false, bucati, p);
            p.getTranzactieDBCollection().add(t);
            jLabel1.setText("S-au vandut : " + bucati + " bucati de " + p.getDenumire());
        } 
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        int prodIndex = jTable1.getSelectedRow();
        if (prodIndex == -1) return;
        
        ProdusDB p = produse.get(prodIndex);
        
        int bucati;
        try {
            bucati = Integer.parseInt(JOptionPane.showInputDialog(this, "Numar bucati adaugate: "));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Date invalide", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        p.setStoc(p.getStoc() + bucati);
        if (MagazinController.getInstance().updateProdus(p)) {
            model.setValueAt(p.getStoc(), prodIndex, 3);   
            jTable1.repaint();
            TranzactieDB t = MagazinController.getInstance().createTranzactie(true, bucati, p);
            p.getTranzactieDBCollection().add(t);
        } 
        
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        int prodIndex = jTable1.getSelectedRow();
        if (prodIndex == -1) return;
        
        ProdusDB p = produse.get(prodIndex);
        
        int rez = JOptionPane.showConfirmDialog(this, "Esti sigur ca doresti sa stergi produsul " + p.getDenumire() + "?", "Atentie", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (rez == JOptionPane.YES_OPTION) {
            if (MagazinController.getInstance().stergeProdus(p)) {
                refreshContent();
            }
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private boolean thirdClick = false;
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (jTable1.getSelectedRow() == -1) return;
        
        if (evt.getButton() == MouseEvent.BUTTON3) {
            ProdusDB p = produse.get(jTable1.getSelectedRow());
            jMenuItem7.setText(p.getDenumire());
            jPopupMenu1.show(jTable1, evt.getX(), evt.getY());
        } else if (evt.getButton() == MouseEvent.BUTTON1) {
            if (evt.getClickCount() == 2) {
                new AsyncTask(
                        new Event () {
                            @Override
                            public void doAction() {
                                try { Thread.sleep(500); } catch (Exception e) {}
                                if (thirdClick) {
                                    thirdClick = false;
                                    return;
                                }
                                jMenuItem4ActionPerformed(null);
                            }                            
                        }
                ).execute();
                
            } else if (evt.getClickCount() == 3) {
                thirdClick = true;
                jMenuItem2ActionPerformed(null);
            } 
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        int prodIndex = jTable1.getSelectedRow();
        if (prodIndex == -1) return;
        
        ProdusDB p = produse.get(prodIndex);
        AdaugaProdusDialog dialog = new AdaugaProdusDialog(this, true, p);
        dialog.setVisible(true);
        p = dialog.getProdus();
        
        if (p == null) return;
        
        if (MagazinController.getInstance().updateProdus(p)) {
            refreshContent();
        }
        
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        new StatisticaFrame().setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        int rez = JOptionPane.showConfirmDialog(this, "<html>Sigur doresti sa stergi toate produsele?<br>Tranzactiile vor fi sterse de asemenea</html>", "Atentie", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (rez != JOptionPane.YES_OPTION) return;
        
        PermissionFrame perm = new PermissionFrame(this, true, "Aproba stergerea produselor si tranzactiilor efectuate la magazin", UserDTO.ACCES_COORDONATOR);
        //perm.setVisible(true);
        if (!perm.isApproved()) return;
        
        MagazinController.getInstance().stergeToateProdusele();
        refreshContent();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        int rez = JOptionPane.showConfirmDialog(this, "<html>Sigur doresti sa stergi toate tranzactiile?</html>", "Atentie", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (rez != JOptionPane.YES_OPTION) return;
        
        PermissionFrame perm = new PermissionFrame(this, true, "Aproba stergerea tranzactiilor efectuate la magazin", UserDTO.ACCES_COORDONATOR);
        //perm.setVisible(true);
        if (!perm.isApproved()) return;
        
        MagazinController.getInstance().stergeToateTranzactiile();
        refreshContent();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        int prodIndex = jTable1.getSelectedRow();
        if (prodIndex == -1) return;
        
        ProdusDB p = produse.get(prodIndex);
        
        int bucati;
        try {
            bucati = Integer.parseInt(JOptionPane.showInputDialog(this, "Numar bucati returnate: "));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Date invalide", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        p.setStoc(p.getStoc() + bucati);
        if (MagazinController.getInstance().updateProdus(p)) {
            model.setValueAt(p.getStoc(), prodIndex, 3);
            jTable1.repaint();
            //TODO: creaza tranzaactie in minus
            TranzactieDB t = new TranzactieDB();
            t.setAdaugate(false);
            t.setBucati(-bucati);
            t.setData(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
            t.setIdProdus(p);
            t.setPret(-bucati * p.getPret());
            t.setObservatii(
                    JOptionPane.showInputDialog(this, "Motivul returnarii :")
            );
            
            if (t.getObservatii().length() > MAX_LENGTH) t.setObservatii(t.getObservatii().substring(0, MAX_LENGTH));
            MagazinController.getInstance().createTranzactie(t);
        }
        
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private final static long TIME_INTERVAL = 500;
    private final static long TYPE_INTERVAL = 1500;
    private long lastEventRised = 0;
    private long lastKeyTyped = 0;
    private int enterCounts = 0;
    private boolean threadRunning = false;
    private boolean threadRunning2 = false;
    private String keyWord = "";
    private int index = -1;
    
    private void jTable1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyTyped
        if (jTable1.getSelectedRow() == -1) return;        
        
        if (evt.getKeyChar() == '\n') {
            if (evt.getWhen() - lastEventRised > TIME_INTERVAL) {
                enterCounts = 0;
            }
            enterCounts++;                        
            
            if (!threadRunning) {
                new AsyncTask(
                    new Event () {
                        @Override
                        public void doAction () {
                            threadRunning = true;
                            try { Thread.sleep(500); } catch (Exception e) {}
                            
                            if (enterCounts == 1) {
                                jMenuItem4ActionPerformed(null);
                            } else if (enterCounts == 2) {
                                jMenuItem2ActionPerformed(null);
                            }
                            
                            threadRunning = false;
                        }
                    }
                ).execute();
            }
        } else if (evt.getKeyChar() == '\b') {
            keyWord = "";
            jLabel2.setText("Caut \"\"");
        } else {
            keyWord += evt.getKeyChar();
            jLabel2.setText("Caut \"" + keyWord + "\"");
            
            for (int i = 0; i < produse.size(); ++i) {
                if (produse.get(i).getDenumire().toLowerCase().contains(keyWord.toLowerCase())) {
                    index = i;
                    i = produse.size();
                }
            }
            
            jTable1.setRowSelectionInterval(index, index);
            
            if (!threadRunning2) {
                new AsyncTask(
                    new Event () {
                        @Override
                        public void doAction() {
                            threadRunning2 = true;
                            
                            do {
                                try { Thread.sleep(TYPE_INTERVAL); } catch (Exception e) {}
                            } while (System.currentTimeMillis() - lastKeyTyped <= TYPE_INTERVAL);
                            //TODO: sterge sirul de caractere format dupa o inactivitate de 1,5 sec
                            keyWord = "";
                            jLabel2.setText("Caut \"\"");
                            threadRunning2 = false;
                        }                          
                    }
                ).execute();
            }
            
            lastKeyTyped = System.currentTimeMillis();
        }
        
        evt.consume();
        lastEventRised = System.currentTimeMillis();
    }//GEN-LAST:event_jTable1KeyTyped

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        if (threadRunning) {
            evt.consume();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            int i = (index == -1 ? 0 : index);
            String keyWord = this.keyWord;
            if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
               
                do {
                    ++i;
                    if (i >= produse.size()) {
                        i = 0;
                    }
                }while (!produse.get(i).getDenumire().toLowerCase().contains(keyWord.toLowerCase()));
                
                index = i;
                jTable1.setRowSelectionInterval(index, index);
                
            } else {
                
                do {
                    --i;
                    if (i < 0) {
                        i = produse.size() - 1;
                    }
                }while (!produse.get(i).getDenumire().toLowerCase().contains(keyWord.toLowerCase()));
                
                index = i;
                jTable1.setRowSelectionInterval(index, index);
            }
            lastKeyTyped = System.currentTimeMillis();
            evt.consume();
        }
        
    }//GEN-LAST:event_jTable1KeyPressed

    private void jTable1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseEntered
        HelperFrame.getInstance().post(
                "Tabel produse", 
                "Scurtaturi pentru inregistrarea mai rapidaa vanzarii produselor : \n"
                        + "- click-dreapta pe produsul dorit pentru a afisa meniul de optiuni disponibile;\n"
                        + "- dublub-click sau Enter pe un produs pentru a vinde o bucata;\n"
                        + "- triplu-click sau dublu-Enter pe un produs pentru a vinde un numar specificat de bucati;\n"
                        + "- tasteaza numele produsului cautat pentru a fi selectat direct. Navigheaza cu sagetelele sus si jos printre rezultatele cautarii."
                        + " Foloseste tasta Backspace pentru a reseta cautarea.");
    }//GEN-LAST:event_jTable1MouseEntered

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        HelperFrame.getInstance().showFrame();
        HelperFrame.getInstance().setLocation(getX() + getWidth() + 5, getY());
        HelperFrame.getInstance().setCanReadInfo(true);
        jTable1MouseEntered(null);
        HelperFrame.getInstance().setCanReadInfo(false);
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
