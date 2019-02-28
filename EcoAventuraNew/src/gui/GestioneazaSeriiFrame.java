/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import db.EchipaDataDB;
import db.SerieDB;
import dialogs.PermissionFrame;
import dto.UserDTO;
import java.awt.Image;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import main.AsyncTask;
import main.Event;
import service.ControllerDB;
import service.SerializeController;

/**
 *
 * @author Alexandru
 */
public class GestioneazaSeriiFrame extends javax.swing.JFrame {
    public static final int MAX_SERII_INACTIVE = 4;
    private final static int SERIE_ARHIVATA_ID = 1;
    private final static int SERIE_TERMINATA_ID = 2;
    private final static int SERIE_ACTIVA_ID = 3;
    private final static int FAKE_SERIE_ID = -4;
    
    private DefaultListModel<SerieDB> model;
    private HashMap <SerieDB, Integer> status;
    
    private MainFrame main;
    private int seriiIncarcate;
    
    private SerieDB fake1; // arhivate
    private SerieDB fake2; // terminate
    private SerieDB fake3; // active
    
    /**
     * Creates new form GestioneazaSeriiFrame
     * @param main
     */
    public GestioneazaSeriiFrame(MainFrame main) {
        super ("Gestioneaza serii");
        initComponents();
        
        this.main = main;
        
        model = new DefaultListModel<>();
        status = new HashMap<>();
        
        fake1 = new SerieDB() {
            @Override 
            public String toString () {
                return "   " + this.getDataInceput();
            }
        };
        fake1.setNumarSerie(FAKE_SERIE_ID);
        fake1.setDataInceput("Serii arhivate");
        
        fake2 = new SerieDB() {
            @Override 
            public String toString () {
                return "   " + this.getDataInceput();
            }
        };
        fake2.setNumarSerie(FAKE_SERIE_ID);
        fake2.setDataInceput("Serii terminate");
        
        fake3 = new SerieDB() {
            @Override 
            public String toString () {
                return "   " + this.getDataInceput();
            }
        };
        fake3.setNumarSerie(FAKE_SERIE_ID);
        fake3.setDataInceput("Serie activa");
        
        model.addElement(fake1);
        
        try {
            for (SerieDB s : SerializeController.getInstance().incarcaSerii().keySet()) {
                model.addElement(s);
                status.put(s, SERIE_ARHIVATA_ID);
            }
        } catch (Exception e) {}
        
        model.addElement(fake2);
        
        try {
            for (SerieDB s : ControllerDB.getInstance().getAllSerii()) {
                if (s != MainFrame.getSerieActiva()){
                    model.addElement(s);
                    status.put(s, SERIE_TERMINATA_ID);
                }
            }
        } catch (Exception e) {}
        
        seriiIncarcate = ControllerDB.getInstance().getAllSerii().size();
        
        model.addElement(fake3);
        
        if (MainFrame.getSerieActiva() != null) {
            model.removeElement(MainFrame.getSerieActiva());
            model.addElement(MainFrame.getSerieActiva());
            status.put(MainFrame.getSerieActiva(), SERIE_ACTIVA_ID);
            seriiIncarcate--;
        }
        
        jList1.setModel(model);
        jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        jLabel6.setText("");
        jLabel7.setText("");
        jLabel8.setText("");
        jLabel9.setText("");
        jButton2.setText("Unknown");
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        jLabel10.setText("");
        ImageIcon icon = new ImageIcon("./res/loading.gif-c200");
        Image img = icon.getImage();
        img = img.getScaledInstance(15, 15, Image.SCALE_FAST);
        jLabel10.setIcon(new ImageIcon(img));
        jLabel10.setVisible(false);
        
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void refreshList () {
        model.clear();
        model.addElement(fake1);
        
        for (SerieDB s : SerializeController.getInstance().incarcaSerii().keySet()) {
            model.addElement(s);
            status.put(s, SERIE_ARHIVATA_ID);
            
        }
        
        model.addElement(fake2);
        
        for (SerieDB s : ControllerDB.getInstance().getAllSerii()) {
            if (s != MainFrame.getSerieActiva()){
                model.addElement(s);
                status.put(s, SERIE_TERMINATA_ID);
            }
        }
        
        seriiIncarcate = ControllerDB.getInstance().getAllSerii().size();
        
        model.addElement(fake3);
        
        if (MainFrame.getSerieActiva() != null) {
            model.removeElement(MainFrame.getSerieActiva());
            model.addElement(MainFrame.getSerieActiva());
            status.put(MainFrame.getSerieActiva(), SERIE_ACTIVA_ID);
            seriiIncarcate--;
        }
        
        jList1.setModel(model);
        //jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        jLabel6.setText("");
        jLabel7.setText("");
        jLabel8.setText("");
        jLabel9.setText("");
        jButton2.setText("Unknown");
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });

        jLabel1.setText("Serii :");

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
        jScrollPane1.setViewportView(jList1);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informatii", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jLabel2.setText("Numar serie :");

        jLabel3.setText("Data inceput :");

        jLabel4.setText("Numar echipe :");

        jLabel5.setText("Status :");

        jLabel6.setText("jLabel6");

        jLabel7.setText("jLabel6");

        jLabel8.setText("jLabel6");

        jLabel9.setText("jLabel6");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("Iesire");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Actiuni"));

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Sterge");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel10.setText("jLabel10");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jLabel10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        SerieDB selected = (SerieDB) jList1.getSelectedValue();
        
        if (selected == null) return;
        if (selected.getNumarSerie() == FAKE_SERIE_ID) {
            jLabel6.setText("");
            jLabel7.setText("");
            jLabel8.setText("");
            jLabel9.setText("");
            jButton2.setEnabled(false);
            return;
        }
        
        jLabel6.setText(selected.getNumarSerie() + "");
        jLabel7.setText(selected.getDataInceput());
        jLabel8.setText(selected.getEchipaDBCollection().size() + "");
        
        switch (status.get(selected)) {
            case SERIE_ACTIVA_ID : 
                jLabel9.setText("Activa");
                jButton2.setEnabled(false);
                jButton3.setEnabled(false);
                break;
            case SERIE_ARHIVATA_ID :
                jLabel9.setText ("Arhivata");
                jButton2.setEnabled(true);
                jButton3.setEnabled(true);
                jButton2.setText("Dezarhiveaza");
                break;
            case SERIE_TERMINATA_ID :
                jLabel9.setText("Terminata (nearhivata)");
                jButton2.setEnabled(true);
                jButton3.setEnabled(true);
                jButton2.setText("Arhiveaza");
        }
    }//GEN-LAST:event_jList1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        final SerieDB selected = (SerieDB) jList1.getSelectedValue();
        
        if (selected == null) return;
        if (selected.getNumarSerie() == FAKE_SERIE_ID) return;
        jLabel10.setVisible(true);
        setEnabled(false);
        
        new AsyncTask(new Event () {
            @Override
            public void doAction() {
                arhivOrDezarhivSerie(selected);
            }            
        }, new Event() {
            @Override
            public void doAction() {
                jLabel10.setVisible(false);
                setEnabled(true);
            }
        }).execute();
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void arhivOrDezarhivSerie (final SerieDB selected) {
        if (jButton2.getText().equals("Arhiveaza")) {
            //GestioneazaSeriiFrame.this.setEnabled(false); // blocam fereastra, pt a preveni actiuni nedorite din partea userului
            jButton2.setEnabled(false);
            jButton3.setEnabled(false);
            
            try {
                HashMap<SerieDB, List<EchipaDataDB>> data = new HashMap<>();
                SerieDB serieArhivata = ControllerDB.getInstance().arhivSerie(selected, data);
                if (serieArhivata == null) throw new Exception();
                
                if (!SerializeController.getInstance().salveazaSerie(selected, data.get(serieArhivata))) throw new Exception();
                
                // seria a fost arhivata cu succes
                
                refreshList();
                seriiIncarcate--;
                status.put(selected, SERIE_ARHIVATA_ID);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Seria nu a putut fi arhivata. Eroare : " + e.getMessage());
            } finally {
                //jButton3.setEnabled(true);
                //jButton2.setEnabled(true);
                jLabel10.setVisible(false);
            }
            
        } else if (jButton2.getText().equals("Dezarhiveaza")){
            //GestioneazaSeriiFrame.this.setEnabled(false);
            jButton3.setEnabled(false);
            jButton2.setEnabled(false);
            jLabel10.setVisible(true);
            
//            if (seriiIncarcate >= MAX_SERII_INACTIVE) {
//                JOptionPane.showMessageDialog(this, "Numarul maxim de serii incarcate a fost atins. Arhivati seriile terminate pentru a putea incarca");
//                return;
//            }
            try {
                List<EchipaDataDB> data;
                SerieDB aux;
                Object[] objs = SerializeController.getInstance().incarcaSerie(selected);
                
                aux = (SerieDB) objs[0];
                data = (List<EchipaDataDB>) objs[1];
                
                if (aux == null) {
                    throw new Exception("Seria arhivata nu a fost gasita");
                }
                ControllerDB.getInstance().adaugaSerie(aux, data);
                
                // dezarhivat cu succes
                
                refreshList();
                seriiIncarcate++;
                status.put(selected, SERIE_TERMINATA_ID);
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(GestioneazaSeriiFrame.this, "Seria nu a putut fi dezarhivata.");
            } finally {
                //GestioneazaSeriiFrame.this.setEnabled(true);
                //jButton3.setEnabled(true);
                //jButton2.setEnabled(true);
            }
        }
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        final SerieDB selected = (SerieDB) jList1.getSelectedValue();
        
        PermissionFrame p = new PermissionFrame(this, true, "Confirmati stergerea seriei " + selected.getNumarSerie() + ", din data " + selected.getDataInceput(), UserDTO.ACCES_COORDONATOR);
        if (!p.isApproved()) {
            JOptionPane.showMessageDialog(this, "Acces refuzat", "Respins", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (selected == null || selected.getNumarSerie() == FAKE_SERIE_ID || status.get(selected) == SERIE_ACTIVA_ID){            
            return;
        }
        
        int input = JOptionPane.showConfirmDialog(this, "Sigur doresti sa stergi aceasta serie?", "Atentie", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (input != JOptionPane.YES_OPTION) return;
        
        jButton3.setEnabled(false);
        jButton2.setEnabled(false);
        jLabel10.setVisible(true);
        
        if (jButton2.getText().equals("Arhiveaza")) { // serie terminata
            HashMap<SerieDB, List<EchipaDataDB> > date = new HashMap<>();
            try {
                SerieDB serieArhivata = ControllerDB.getInstance().arhivSerie(selected, date);
                if (serieArhivata == null) throw new Exception();
                
                
                //succes
                status.remove(selected);
                seriiIncarcate--;
                refreshList();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Eroare la stergerea seriei");
                System.out.println("Gestioneaza Serii : Eroare : " + e.getMessage());
            }
        } else if (jButton2.getText().equals("Dezarhiveaza")) {
            try {
                List<EchipaDataDB> data;
                SerieDB aux;
                Object[] objs = SerializeController.getInstance().incarcaSerie(selected);
                
                aux = (SerieDB) objs[0];
                data = (List<EchipaDataDB>) objs[1];
                
                if (aux == null) {
                    throw new Exception("Seria arhivata nu a fost gasita");
                }
                
                // succes
                status.remove(selected);
                refreshList();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Eroare la stergerea seriei");
                System.out.println("Gestioneaza Serii : Eroare : " + e.getMessage());
            }
        }
        
        jButton3.setEnabled(true);
        jButton2.setEnabled(true);
        jLabel10.setVisible(false);
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        HelperFrame.getInstance().post(
                "Fereastra de gestionare a seriilor",
                "Orice serie se poate gasi in una dintre urmatoarele trei stari: \n"
                        + " *activa: seria care in desfasurare acum si care este stocata in baza de date;\n"
                        + " *terminata(nearhivata): serie finalizata care inca este stocata in baza de date; \n"
                        + " *terminata(arhivata): serie finalizata care a fost stocata intr-un fisier separat in acest pc;\n"
                        + "RECOMANDARE: pentru a eficentiza viteza de lucru a aplicatie, se recomanda a nu se pastra mai mult de trei serii "
                        + "nearhivate si terminate la un moment de timp stocate in baza de date;\n"
                        + "DE RETINUT: o serie arhivata nu poate fi vizualizata;");
    }//GEN-LAST:event_formMouseEntered

    private void jList1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseEntered
        HelperFrame.getInstance().post(
                "Lista seriilor existente",
                "O evidenta a seriilor desfasurate pana acum, pe cele trei stari. Pentru a putea gestiona o serie anume, click stanga pe aceasta.");
    }//GEN-LAST:event_jList1MouseEntered

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
