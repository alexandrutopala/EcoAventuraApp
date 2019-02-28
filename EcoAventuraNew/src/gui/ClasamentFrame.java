/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import db.EchipaDB;
import db.JocDB;
import db.JocGeneralDB;
import db.PunctajDB;
import db.SerieDB;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import main.AsyncTask;
import main.Event;
import main.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import service.ControllerDB;

/**
 *
 * @author Alexandru
 */
public class ClasamentFrame extends javax.swing.JFrame {
    private final static String [] columns = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AT", "AU", "AV", "AW", "AX", "AY", "AZ"};
    private final static int N_MAX = 100;
    private final static int TABLE_HEIGHT = 40;
    private final static int MAX_TABLE_HEIGHT = 400;
    private final static int MIN_TABLE_WIDTH = 400;
    private final static int TABLE_MIN_WIDTH = 60;
    private final static int TABLE_MAX_WIDTH = 130;
    private final static int ADAOS = 10;
    private final ImageIcon ICON;
    private static int AMP = 200;
    private static int STARTING_SCORE;
    private SerieDB serie;
    private DefaultTableModel model;
    private List<JocGeneralDB> headers;
    private List<EchipaDB> echipe;
    private HashMap<EchipaDB, List<PunctajDB> > punctaje;
    private String [] header;
    private int [][] matrix;
    private boolean absente [][];
    private boolean noRegistry[][];
    private boolean showAbsenti = true;
    
    /**
     * Creates new form ClasamentFrame
     */
    public ClasamentFrame(SerieDB serie, List<EchipaDB> echipe) {
        super ("Clasament seria " + serie.getNumarSerie());
        initComponents();
                     
        absente = new boolean[N_MAX][N_MAX];
        noRegistry = new boolean[N_MAX][N_MAX];
        headers = new ArrayList<>();
        this.echipe = echipe;
        STARTING_SCORE = echipe.size() * AMP;
        
        for (EchipaDB e : echipe) {
            List<JocDB> jocuri = ControllerDB.getInstance().getJocuriByEchipa(e);
            for (JocDB j : jocuri){
                if (!headers.contains(j.getJocGeneralidJocGeneral())){
                    headers.add(j.getJocGeneralidJocGeneral());
                }
            }
            JocGeneralDB penalizare = ControllerDB.getInstance().findJocGeneral("penalizare");
            if (!headers.contains(penalizare)) {
                headers.add(penalizare);
            }
        } 
        
        this.serie = serie;
        model = new DefaultTableModel() {
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return EchipaDB.class;
                    default:
                        return Integer.class;
                }
            }
        };  
        
        
        
        model.setColumnCount(headers.size() + 2); // + 2, adica totals si echipe
        model.setRowCount(echipe.size());
        
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable1.setModel(model);
        
        header = setHeaders(headers);
        setEchipe(echipe);
        resizeColumnWidth(jTable1);       
        
        
        jTable1.setDefaultRenderer(Integer.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, 
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component c = super.getTableCellRendererComponent(table, 
                    value, isSelected, hasFocus, row, column);
                c.setBackground(row%2==0 ? Color.white : Color.getHSBColor(0.6f, 0214f, 0.002f)); 
                
                
                if (absente[row][column] && showAbsenti && !noRegistry[row][column]) {
                    c.setForeground(Color.RED);
                } else {
                    c.setForeground(Color.BLACK);
                }
                return c;
            };
        });
        
        jTable1.getTableHeader().addMouseMotionListener(
            new MouseMotionAdapter() {

                @Override
                public void mouseDragged(MouseEvent e) {
                    showAbsenti = false;
                    super.mouseDragged(e); //To change body of generated methods, choose Tools | Templates.
                }
                    
            }
        );
       
        jTable1.getTableHeader().addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent me) {
                        showAbsenti = false;
                        super.mouseClicked(me);                       
                    }                    
             }
        );
        
        calculeaza();
        
        jLabel1.setText("");
        jLabel2.setText("");
        ImageIcon icon = new ImageIcon("./res/loading.gif-c200");
        Image img = icon.getImage();
        img = img.getScaledInstance(15, 15, Image.SCALE_FAST);
        ICON = new ImageIcon(img);
        jLabel1.setIcon(ICON);
        jLabel1.setVisible(false);
        //setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    public final String [] setHeaders (List<JocGeneralDB> headers) {
        jTable1.repaint();
        String [] h = new String [headers.size()+2];
        
        h[0] = "Echipe";
        JocGeneralDB fake1 = new JocGeneralDB(-1);
        fake1.setNumeJocGeneral(h[0]);
        jTable1.getColumnModel().getColumn(0).setHeaderValue(fake1);
        
        JocGeneralDB penalizare;
        for (int i = 0; i < headers.size(); ++i){
            if (headers.get(i).getNumeJocGeneral().equals("penalizare")) {
                penalizare = headers.remove(i);
                headers.add(penalizare);
            }
            h[i+1] = headers.get(i).getNumeJocGeneral();
            jTable1.getColumnModel().getColumn(i+1).setHeaderValue(headers.get(i));
        }     
        
        h[headers.size()+1] = "Total";
        JocGeneralDB fake2 = new JocGeneralDB(-1);
        fake2.setNumeJocGeneral(h[headers.size()+1]);
        jTable1.getColumnModel().getColumn(headers.size()+1).setHeaderValue(fake2);
        
        return h;
    }
    
    public final void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        int totalWidth = 0;
        for (int column = 0; column < table.getColumnCount(); column++) {
            int maxWidth = table.getGraphics().getFontMetrics().stringWidth(columnModel.getColumn(column).getHeaderValue().toString()) + ADAOS; // Min width
            int width;
            for (int row = 0; row < table.getRowCount(); row++) {
                String s = table.getValueAt(row, column) != null ? table.getValueAt(row, column).toString() : "";
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);            
                width = table.getGraphics().getFontMetrics().stringWidth(s) + ADAOS;//Math.max(comp.getPreferredSize().width +1 , width);
                //width += (TABLE_MIN_WIDTH - width) + 10; 
                comp.setPreferredSize(new Dimension(width, TABLE_HEIGHT));
                
                if (columnModel.getColumn(column).getHeaderValue().equals("Echipe")) {                   
                    comp.setBackground(row%2==0 ? Color.white : Color.getHSBColor(0.6f, 0214f, 0.002f));       
                }
                
                //comp.setBackground(column%2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
                maxWidth = Math.max(width, maxWidth);

            }
            if (maxWidth < TABLE_MIN_WIDTH) {
                maxWidth += (TABLE_MIN_WIDTH - maxWidth);
            } 
            
            if(maxWidth > TABLE_MAX_WIDTH){
                maxWidth=TABLE_MAX_WIDTH;
            }            
            
            columnModel.getColumn(column).setPreferredWidth(maxWidth); 
            totalWidth += columnModel.getColumn(column).getWidth();
        }
        //totalWidth += 2 * TABLE_MAX_WIDTH - TABLE_MIN_WIDTH;
        //totalWidth *= 2;
        int height = TABLE_HEIGHT * (model.getRowCount() + 1);
        totalWidth = totalWidth < MIN_TABLE_WIDTH ? MIN_TABLE_WIDTH : totalWidth;
        //if (height > MAX_TABLE_HEIGHT) {
        //    height = MAX_TABLE_HEIGHT;
        //}
        jScrollPane1.setPreferredSize(new Dimension(totalWidth, height));
        jScrollPane1.setSize(totalWidth, TABLE_HEIGHT * height);
        table.setPreferredScrollableViewportSize(new Dimension(totalWidth, height));
        table.setSize(totalWidth, height);        
        setSize(totalWidth, height + 150);
        setPreferredSize(new Dimension(totalWidth, height + 150));
//        Dimension d = new Dimension(totalWidth, height);
//        table.setMinimumSize(d);
//        table.setPreferredSize(d);
//        table.setMaximumSize(d);
//        
//        jScrollPane1.setMinimumSize(d);
//        jScrollPane1.setPreferredSize(d);
//        jScrollPane1.setMaximumSize(d);
//        
//        setMinimumSize(d);
//        setPreferredSize(d);
//        setMaximumSize(d);
        
        jButton1.setLocation(jButton1.getLocation().x, height + ADAOS);
        validate();
        repaint();
        pack();
    }

    final public  void setEchipe (List<EchipaDB> echipe) {
        for (int row = 0; row < model.getRowCount(); ++row){
            model.setValueAt(echipe.get(row), row, 0);
        }
    }
    
    final public void calculeaza () {
        jTable1.repaint();
        if (punctaje == null) {
            punctaje = new HashMap<>();
            List<PunctajDB> p;
            for (EchipaDB e : echipe){
                List<JocDB> jocuri = ControllerDB.getInstance().getJocuriByEchipa(e);
                p = new ArrayList<>();
                for (int i = 0; i < headers.size(); ++i) p.add(null);
                for (JocDB j : jocuri){
                    int index = headers.indexOf(j.getJocGeneralidJocGeneral()); // calculez indexul pe care ar trebui sa se afle j in lista p
                    if (p.get(index) == null) { // daca nu exista nimic acolo, atunci il punem
                        p.remove(index);
                        p.add(index, new PunctajDB(j));
                    } else {                    // altfel, adunam punctajul jocuui actual la cel deja existant
                        p.get(index).getJoc().setPunctaj(p.get(index).getJoc().getPunctaj() + j.getPunctaj());
                    }
                }
                punctaje.put(e, p);
            }
        }
        
        matrix = new int[echipe.size()][headers.size()];
        int n = echipe.size();
        int m = headers.size();
        
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (punctaje.get(echipe.get(i)).get(j) != null) {
                    matrix[i][j] = punctaje.get(echipe.get(i)).get(j).getJoc().getPunctaj();
                } else {
                    matrix[i][j] = 0;
                }
            }
        }
        
        List<Pair> vect = new ArrayList<>();
        for (int j = 0; j < m-1; ++j) { // ultima coloana reprezinta INTOTDEAUNA penalizarile, si trebuie luate ca atare
            vect.clear();
            for (int i = 0; i < n; ++i) { // preluam coloana cu coloana
                Pair pair = new Pair(matrix[i][j], i);
                vect.add(pair);
            }
                                          // prelucram coloana
            Collections.sort(vect, new Comparator<Pair>() {
                @Override
                public int compare(Pair t, Pair t1) {
                    if (t.getFirstValue() < t1.getFirstValue()) return -1;
                    else if (t.getFirstValue() > t1.getFirstValue()) return 1;
                    else return (t.getSecondValue() < t1.getSecondValue()) == true ? -1 : 
                            (t.getSecondValue() > t1.getSecondValue()) == true ? 1 : 0;
                }
                
            });
            
            int restanta = 1;
            int last = vect.get(n-1).getFirstValue();
            vect.get(n-1).setFirstValue(STARTING_SCORE);  
            for (int i = n-2; i >= 0; --i){
                if (vect.get(i).getFirstValue() != last) {
                    last = vect.get(i).getFirstValue();
                    vect.get(i).setFirstValue(vect.get(i+1).getFirstValue() - AMP * restanta);
                    restanta = 1;
                } else {
                    vect.get(i).setFirstValue(vect.get(i+1).getFirstValue());
                    restanta++;
                }
            }
            
            for (int i = 0; i < n; ++i) {  // si o punem inapoi
                matrix[vect.get(i).getSecondValue()][j] = vect.get(i).getFirstValue();
            }
        }
        
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                absente[i][j] = false;
                if (j == m-1) {
                    model.setValueAt(-matrix[i][j], i, j+1);
                    //matrix[i][j] *= -1;
                } else {
                    model.setValueAt(matrix[i][j], i, j+1);
                }
            }
        }
        
        for (int i = 0; i < model.getRowCount(); ++i) {
            for (int j = 0; j < headers.size() - 1; ++j) {
                boolean [] res = ControllerDB.getInstance().isTotalAbsent(
                        (EchipaDB) model.getValueAt(i, 0), 
                        headers.get(j));
                if (res[0]) {
                    if (jCheckBox1.isSelected()) {
                        model.setValueAt(0, i, j+1);
                    }
                    absente[i][j+1] = true;
                    noRegistry[i][j+1]= res[1];
                }
            }
        }
        
        
        jTable1.setModel(model);
        jMenuItem1.setText("Modifica ratia (" + AMP + ")");
        jMenuItem2.setText("Modifica punctaj de pornire (" + STARTING_SCORE + ")");
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
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        jTable1.setAutoscrolls(false);
        jTable1.setColumnSelectionAllowed(true);
        jTable1.setRowHeight(35);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTable1MouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jButton1.setText("Calculeaza total");
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

        jButton2.setText("Descarca Excel");
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

        jButton3.setText("Recalculeaza punctaje");
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

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("<html>    Acorda punctajul minim 0 pentru echipele<br>care nu au realizat jocul respectiv<br> sau care au fost marcate ca absente.</html>");
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseEntered(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jMenu1.setText("Optiuni punctare");

        jMenuItem1.setText("Modifica ratia");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Modifica punctaj de pornire");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 929, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addGap(14, 14, 14)
                                .addComponent(jButton1))
                            .addComponent(jCheckBox1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jLabel2))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jCheckBox1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jTable1.repaint();
        for (int row = 0; row < jTable1.getRowCount(); ++row) {
            int total = 0;
            for (int column = 1; column < jTable1.getColumnCount() - 1; ++column){
                total += (Integer) jTable1.getValueAt(row, column);
            }
            jTable1.setValueAt(total, row, jTable1.getColumnCount() - 1);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            setEnabled(false);
            calculeaza();            
        } catch (Exception e) {
            
        } finally {
            setEnabled(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jLabel1.setVisible(true);
        if (matrix == null) {
            calculeaza();
        }
        JFileChooser chooser = new JFileChooser(); 
        chooser.setCurrentDirectory(new java.io.File("./serii"));
        chooser.setDialogTitle("Selecteaza destinatia");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        //chooser.setAcceptAllFileFilterUsed(false);
        //   
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            // add loading animation 
            //setEnabled(false);
            int m = headers.size();
            int n = echipe.size();
            
            // punem semnul minus la punctajele corespunzatoare penalizarilor, revenind la semnul normal la finalul procedurii
            for (int i = 0; i < n; ++i) { 
                matrix[i][m-1] *= -1;
            }
            
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet ws = wb.createSheet();
            
            XSSFDataFormat format =  wb.createDataFormat();
            
            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
            headerStyle.setBorderRight(CellStyle.BORDER_THIN);
            
            CellStyle numericStyle; 
            numericStyle = wb.createCellStyle();
            numericStyle.setBorderBottom(CellStyle.BORDER_THIN);
            //numericStyle.setDataFormat(format.getFormat("0"));
            
            CellStyle numericBorderedStyle;
            numericBorderedStyle = wb.createCellStyle();
            numericBorderedStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
            numericBorderedStyle.setBorderBottom(CellStyle.BORDER_THIN);
            //numericBorderedStyle.setDataFormat(format.getFormat("0"));
            
            // load data
            
            int rowId = 0;
            int cellId = 0;
            XSSFRow row;
            
            row = ws.createRow(rowId++);
            Cell crt = row.createCell(cellId++);
            CellStyle style = wb.createCellStyle();
            style.setBorderBottom(CellStyle.BORDER_MEDIUM);
            style.setBorderRight(CellStyle.BORDER_MEDIUM);
            crt.setCellValue("Nr. Crt.");
            crt.setCellStyle(style);
            
            for (int i = 0; i < header.length; ++i) {
                Cell cell = row.createCell(cellId++);
                cell.setCellValue(header[i]);
                cell.setCellStyle(headerStyle);
                
                if (i == header.length - 1) {
                    cell.setCellStyle(style);
                }
            }
            
            int i = 1;
            for (EchipaDB e : punctaje.keySet()){
                cellId = 0;
                row = ws.createRow(rowId++);
                Cell count = row.createCell(cellId++);
                
                count.setCellValue(i);
                count.setCellStyle(numericBorderedStyle);
                
                Cell team = row.createCell(cellId++);
                team.setCellValue(e.toString());
                team.setCellStyle(numericBorderedStyle);
                                
                for (int j = 0; j < matrix[i-1].length; ++j) {
                    Cell cell = row.createCell(cellId++);
                    cell.setCellValue(matrix[i-1][j]);
                    
                    if (j == matrix[i-1].length - 1) {
                        cell.setCellStyle(numericBorderedStyle);
                    } else {
                        cell.setCellStyle(numericStyle);
                    }
                }
                
                Cell total = row.createCell(cellId++);
                total.setCellType(CellType.FORMULA);
                total.setCellFormula("SUM(" + columns[2] + (rowId) + ":" + columns[punctaje.get(e).size()+1] + (rowId) + ")");
                total.setCellStyle(style);
                
                ++i;
            }
            
            for (int k = 0; k < n; ++k) {
                matrix[k][m-1] *= -1;
            }
            
            try {
                String fisName = "clasament seria " + serie.getNumarSerie() + "_" + new SimpleDateFormat("ddMMyyyy_HHss").format(Calendar.getInstance().getTime());
                fisName = JOptionPane.showInputDialog(this, "Denumeste foaia de calcul", fisName);
                
                File f = new File(chooser.getSelectedFile().getAbsolutePath() + File.separator + fisName + ".xlsx");
                if (f.exists()) {
                    if (JOptionPane.showConfirmDialog(this, "Exista deja un fisier cu acelasi nume. Suprascrieti?", "Duplicat", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                FileOutputStream fout = new FileOutputStream(f);
                wb.write(fout);
                fout.close();
                Desktop.getDesktop().open(chooser.getSelectedFile());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Fisierul nu a putut fi creat");                 
            }
            
            // enable frame
            
        }
        setEnabled(true);
        jLabel1.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        jLabel1.setIcon(ICON);
        setEnabled(false);
        new AsyncTask(new Event(){
            @Override
            public void doAction() {
                calculeaza();
            }
        }, new Event() {
            @Override
            public void doAction() {
                jLabel1.setIcon(null);
                setEnabled(true);
            }
        }).execute();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jTable1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseEntered
        HelperFrame.getInstance().post(
                "Tabel punctaje",
                "Aici sunt afisate punctajele procesate ale fiecarei echipe, obtinute la fiecare proba origanizata pana acum. \n"
                        + "Fiecare punctaj scris cu rosu semnifica absenta echipei determinata de linia celulei punctajului, la jocul determinat de coloana acesteia.\n"
                        + "Poti ordona crescator sau descrescator punctajele dand click pe marginea alba de sus a coloanei de referinta. \n"
                        + "Daca vrei, poti reordona coloanele tabelului tragand cu mouse-ul de marginea alba de sus a oricarei coloane "
                        + "la stanga sau la dreapta...doar daca vrei... ATENTIE insa, coloana echipelor, a penalizarilor si a punctajelor finale "
                        + " trebuie sa ramana mereu in pozitia lor INITIALA.");
    }//GEN-LAST:event_jTable1MouseEntered

    private void jCheckBox1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseEntered
        HelperFrame.getInstance().post(
                "Optiunea de punctare in funtie de absentaj",
                "Deoarece orice echipa poate lipsi de la o proba, sta la decizia coordonatorului daca "
                        + "aceasta va fi punctata ca si cum ar fi jucat, dar ar fi iesit pe ultimul loc, "
                        + "sau daca nu va fi punctata deloc. Dar cum toate echipele sunt egale in drepturi "
                        + "si indatoriri, tratamentul va fi acelasi pentru oricare echipa absenta.");
    }//GEN-LAST:event_jCheckBox1MouseEntered

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        HelperFrame.getInstance().post(
                "Fereastra clasamentului",
                "Aici se centralizeaza punctajele acordate de catre animatori la diversele jocuri organizate"
                        + " pana in momentul de fata, se proceseaza si, pentru fiecare joc in parte, se stabileste un top "
                        + "dupa urmatorul procedeu : \n"
                        + "- punctajul maxim se stabile prin inmultirea numarului de echipe din seria actuala cu 200, si este acordat echipei de pe locul I; \n"
                        + "- echipa de pe locul II va primi cu 200 mai putin decat cea de pe locul intai, s.a.m.d. \n"
                        + "- in caz de egalitate, se acorda acelasi punctaj tuturor echipelor care se afla la egalitate, iar "
                        + "urmatoarea echipa va primi cu 200, inmultit cu cate echipe au avut acelasi punctaj, mai putin;");
    }//GEN-LAST:event_formMouseEntered

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        HelperFrame.getInstance().post(
                "Calcularea punctajelor",
                "Calculeaza punctajul final al echipei, insumand toate scorurile de pe linia echipei respective. "
                        + "Rezultatul va fi afisat in coloana 'Total'");
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        HelperFrame.getInstance().post(
                "Descarca Excel",
                "Pentru o eficientizare a timpului, aplicatia vine cu optiunea de a descarca tabelul din fereastra "
                        + "intr-un fisier de format Excel, gata sa fie printat si afisat.");
    }//GEN-LAST:event_jButton2MouseEntered

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        HelperFrame.getInstance().post(
                "Optiunea de recalculare a punctajelor",
                "Daca ceva nu este la locul lui din punctajele afisate, sau au fost primite noi scoruri de catre server "
                        + "si doresti sa reimprospatati datele, poti folosi aceasta optiune pentru a actualiza punctajele.");
    }//GEN-LAST:event_jButton3MouseEntered

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            AMP = Integer.parseInt(JOptionPane.showInputDialog(this, "Modifica diferenta dintre punctaje: ", AMP));
            jMenuItem1.setText("Modifica ratia (" + AMP + ")");
            calculeaza();
        } catch (Exception e) {}
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            STARTING_SCORE = Integer.parseInt(JOptionPane.showInputDialog(this, "Modifica diferenta dintre punctaje: ", STARTING_SCORE));
            jMenuItem2.setText("Modifica punctaj de pornire (" + STARTING_SCORE + ")");
            calculeaza();
        } catch (Exception e) {}
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
