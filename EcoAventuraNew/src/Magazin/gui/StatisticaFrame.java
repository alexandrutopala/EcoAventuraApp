/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Magazin.gui;

import Magazin.db.ProdusDB;
import Magazin.db.TranzactieDB;
import Magazin.service.MagazinController;
import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Marius
 */
public class StatisticaFrame extends javax.swing.JFrame {
    private final static int MIN_WIDTH = 5;
    private final static int MAX_WIDTH = 300;
    private static final String [] HEADERS = new String [] {"Nr. Crt.", "Denumire", "Bucati", "Suma Obtinuta"};
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final List<TranzactieDB> tranzactii;
    private final List<ProdusDB> produse;
    private DefaultTableModel model;
    private String [][] tableContent;
    private HashMap<ProdusDB, Integer> map;
    private boolean isConstructing = false;
    /**
     * Creates new form StatisticaFrame
     */
    public StatisticaFrame() {
        super("Statistica");
        isConstructing = true;
        initComponents();
        
        produse = MagazinController.getInstance().findAllProduse();
        tranzactii = MagazinController.getInstance().findAllTranzactii();
        map = new HashMap<>();
        model = new DefaultTableModel();
        
        //if (tranzactii == null) tranzactii = new ArrayList<>();
        
        if (tranzactii.isEmpty()) {
            jDateChooser1.setDate(Calendar.getInstance().getTime());
            jDateChooser2.setDate(Calendar.getInstance().getTime());
        } else {
            try {
                jDateChooser1.setDate(sdf.parse(tranzactii.get(0).getData()));
                jDateChooser2.setDate(sdf.parse(tranzactii.get(tranzactii.size() - 1).getData()));
            } catch (ParseException ex) {
                Logger.getLogger(StatisticaFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        jLabel4.setText("");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        isConstructing = false;
        calculeaza();
        resizeColumnWidth(jTable1);
    }
    
    private void calculeaza () {
        parseContent();
        int infBound = -1;
        int supBound = -1;
        float total = 0;
        
        Date date1 = jDateChooser1.getDate();
        Date date2 = jDateChooser2.getDate();
        date1.setHours(0);
        date1.setMinutes(0);
        date1.setSeconds(0);
        date2.setHours(0);
        date2.setMinutes(0);
        date2.setSeconds(0);
        int i = 0;
        
        for (TranzactieDB t : tranzactii) {
            try {
                Date date = sdf.parse(t.getData());
                date.setHours(0);
                date.setMinutes(0);
                date.setSeconds(0);
                
                if ((date.after(date1) || date.equals(date1)) && infBound == -1) {
                    infBound = i;
                }
                
                if (date.before(date2) || date.equals(date2)) {
                    supBound = i;
                }
                
                i++;
            } catch (ParseException ex) {
                Logger.getLogger(StatisticaFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (infBound == -1 || supBound == -1) return;
        
        int index; 
        int bucati = 0;
        float suma = 0;
        
        for (i = infBound; i <= supBound; ++i) {
            TranzactieDB t = tranzactii.get(i);
            
            if (!t.getAdaugate()) {// daca produsul a fost vandut
                index = map.get(t.getIdProdus());
                try { bucati = Integer.parseInt(tableContent[index][2]); } catch (Exception e) {}
                try { suma = Float.parseFloat(tableContent[index][3]); } catch (Exception e) {}
                
                bucati += t.getBucati();
                suma += t.getPret();
                
                tableContent[index][2] = bucati + "";
                tableContent[index][3] = suma + "";
                total += t.getPret();
            }
        }
        
        model.setDataVector(tableContent, HEADERS);
        jTable1.setModel(model);
        jLabel4.setText(total + "");
        resizeColumnWidth(jTable1);
    }
    
    private void parseContent () {
        tableContent = new String[produse.size()][4];
        
        int i = 0;
        
        for (ProdusDB p : produse) {
            tableContent[i][0] = (i + 1) + "";
            tableContent[i][1] = p.getDenumire();
            tableContent[i][2] = "0";
            tableContent[i][3] = "0";
            map.put(p, i);
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

        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jLabel1.setText("de la");

        jLabel2.setText("pana la");

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
        jScrollPane1.setViewportView(jTable1);

        jLabel3.setText("Total vanzari:");

        jLabel4.setText("jLabel4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        if (isConstructing) return;
        calculeaza();
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        if (isConstructing) return;
        calculeaza();
    }//GEN-LAST:event_jDateChooser2PropertyChange


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
