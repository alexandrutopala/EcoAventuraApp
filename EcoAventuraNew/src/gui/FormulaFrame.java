/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import costumSerialization.SerializableArithmeticLabel;
import costumSerialization.SerializableFormulaFrame;
import costumSerialization.SerializableFormulaLabel;
import costumSerialization.SerializableLabel;
import db.JocGeneralDB;
import formula.Constant;
import formula.Cronometer;
import formula.ElementType;
import formula.Formula;
import formula.FormulaElement;
import formula.MembriCount;
import formula.Variable;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import service.ControllerDB;
import service.SerializeController;

/**
 *
 * @author Alexandru
 */
public class FormulaFrame extends javax.swing.JFrame {
    private final static Border STANDARD_BORDER = BorderFactory.createLineBorder(Color.black, 1);
    private final static Border SELECTED_BORDER = BorderFactory.createLineBorder(Color.yellow, 3);
    private final static Border EMPTY_BORDER = BorderFactory.createEmptyBorder();
    private final static String VARIABLE_TEXT = "     VAR     ";
    private final static String CONSTANT_TEXT = "   CONST   ";
    private final static String CRONOMETER_TEXT = "   CRONO   ";
    private final static String NR_MEMBRI_TEXT = "NR_MEMBRI";
    public final LabelFactory labelFactory = new LabelFactory();
    private int varId = 0;
    private int constId = 0;
    private int cronoId = 0;
    private int membriId = 0;
    private JocGeneralDB joc;
    private JLabel selectedElement;
    private JLabel selectedItemFormula;
    private JLabel selectedItemWait;
    private ArrayList<JLabel> formula;
    private HashMap<JLabel, FormulaElement> variables;
    private boolean onCreate;
    private int nrSerie = -1;
    /**
     * Creates new form FormulaFrame
     * @param joc
     * @param onCreate
     */
    public FormulaFrame(JocGeneralDB joc, boolean onCreate) {
        super ("Formula : " + joc.getNumeJocGeneral());
        SerializeController.getInstance().setLabelFactoryInstance(labelFactory);
        initComponents();
        
        this.onCreate = onCreate;
        this.joc = joc;
        incarcaFormula(joc);
        
        jTextArea1.setEditable(false);        
        jPanel2.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        
        
        if (formula.isEmpty()) {
            jPanel2.setSize(400, 60);
            JLabel l = new JLabel();
            l.setSize(1, 60);
            l.setText(" ");
            jPanel2.add(l);
        }
        
        //setResizable(false);
        setLocationRelativeTo(null);
        if (onCreate) {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        } else {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
        
    }
    
    
    // methods for knowing where the label is comeing from
    
    private boolean isLabelSelected() {
        return (selectedElement != null || selectedItemFormula != null || selectedItemWait != null);
    }
    
    private JLabel getSelectedLabel () {
        return selectedElement != null ? 
                    selectedElement : 
                    selectedItemFormula != null ? 
                        selectedItemFormula : 
                        selectedItemWait;
    }
    
    private JPanel getLabelParent () {
        return (JPanel) getSelectedLabel().getParent();
    }
    
    public void deselectAll () {
        if (selectedElement != null)
            selectedElement.setBorder(BorderFactory.createEmptyBorder());
        if (selectedItemFormula != null)
            selectedItemFormula.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        if (selectedItemWait != null)
            selectedItemWait.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        
        selectedElement = null;
        selectedItemFormula = null;
        selectedItemWait = null;
        
        for (JLabel l : formula) {
            l.setBorder(EMPTY_BORDER);
        }
    }
    
    private void selectLabel (JLabel l) {
        try {
            if (l.getParent() == jPanel1) {
                l.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
                selectedElement = l;
                
                if (selectedItemFormula != null)
                    selectedItemFormula.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                
                if (selectedItemWait != null)
                    selectedItemWait.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                
                selectedItemFormula = null;
                selectedItemWait = null;
            } else if (l.getParent() == jPanel2){
                l.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
                selectedItemFormula = l;
                
                if (selectedElement != null)
                    selectedElement.setBorder(BorderFactory.createEmptyBorder());
                
                if (selectedItemWait != null)
                    selectedItemWait.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                
                selectedElement = null;
                selectedItemWait = null;
            } 
        } catch (Exception e) {}
    }
    
    public void deselectNeighborhs (JLabel l, ArrayList<JLabel> list) {
        if (l.getParent() == jPanel1) return; // procedura se aplica doar pt jPanel2
        int index = list.indexOf(l);
        
        if (0 < index && index < list.size() - 1) {  // daca e la extrema deselectam ambii pretendenti
            list.get(index-1).setBorder(EMPTY_BORDER);
            list.get(index+1).setBorder(EMPTY_BORDER);
        } else if (index == 0 && index < list.size() - 1) {
            list.get(index+1).setBorder(EMPTY_BORDER);
        } else if (index == list.size() - 1 && index > 0){
            list.get(index-1).setBorder(EMPTY_BORDER);
        }        
    }
    
    public void selectNeighborhs (JLabel l, ArrayList<JLabel> list, boolean right) {
        if (l.getParent() == jPanel1) return; // procedura se aplica doar pt jPanel2
        int index = list.indexOf(l);
        
        if (index > 0 && !right) {
            list.get(index-1).setBorder(SELECTED_BORDER);
        }
        
        if (index < list.size() - 1 && right) {
            list.get(index+1).setBorder(SELECTED_BORDER);
        }        
    }
    
    public static boolean isArithmeticLabel (JLabel l) {
        return (l.getText().contains("+") ||
                l.getText().contains("-") ||
                l.getText().contains("*") ||
                l.getText().contains("/") ||
                l.getText().contains("(") ||
                l.getText().contains(")"));
    }
    
    public static boolean isArithmeticLabel (String l) {
        return (l.contains("+") ||
                l.contains("-") ||
                l.contains("*") ||
                l.contains("/") ||
                l.contains("(") ||
                l.contains(")"));
    }
    
    // add label on jPanel2 methods
    
    public void addLabel (JLabel nou, JLabel ref, boolean prev, boolean parenthesis) {
        if (nou == null) return;
        try {
            if (nou.getParent() == jPanel1) { // cream eticheta
                switch (nou.getText()) {
                    case "VARIABILA" : 
                        if (Integer.parseInt(varCount.getText()) <= 0) return;                        
                        nou = labelFactory.createLabel(jPanel2, ElementType.VAR); 
                        if (nou != null) {
                            varCount.setText((Integer.parseInt(varCount.getText())) -1 + "");
                        }
                        break;
                    case "CONSTANTA" : 
                        if (Integer.parseInt(constCount.getText()) <= 0) return;
                        nou = labelFactory.createLabel(jPanel2, ElementType.CONST);
                        if (nou != null) {
                            constCount.setText((Integer.parseInt(constCount.getText())) -1 + "");                            
                        }
                        break;
                    case "CRONOMETRU" :
                        if (Integer.parseInt(cronoCount.getText()) <= 0) return;                        
                        nou = labelFactory.createLabel(jPanel2, ElementType.CRONO);
                        if (nou != null) {
                            cronoCount.setText((Integer.parseInt(cronoCount.getText())) -1 + "");
                        }
                        break;
                    case "NR_MEMBRI" :
                        if (Integer.parseInt(membriCount.getText()) <= 0) return;
                        nou = labelFactory.createLabel(jPanel2, ElementType.NR_MEMBRI);
                        if (nou != null) {                            
                            membriCount.setText((Integer.parseInt(membriCount.getText())) -1 + "");
                        }
                        break;
                }
            }
            
            if (nou == null) return;
            
            if (ref == null) {
                if (formula.size() > 0) {                                           // daca in formula mai exista elemente,
                    formula.add(labelFactory.createArithmeticLabel(jPanel2, "+"));  // atunci adaugam si un operator
                } 
                formula.add(nou);
            } else if (parenthesis) {
                int index = formula.indexOf(ref);
                formula.add(index++, labelFactory.createArithmeticLabel(jPanel2, "("));
                formula.add(index+1, labelFactory.createArithmeticLabel(jPanel2, ")"));
                formula.add(index+1, nou);
                formula.add(index+1, labelFactory.createArithmeticLabel(jPanel2, "+"));
            } else if (prev) {
                int index = formula.indexOf(ref);
                formula.add(index++, nou);
                formula.add(index, labelFactory.createArithmeticLabel(jPanel2, "+"));
            } else {
                int index = formula.indexOf(ref);
                formula.add(index+1, nou);
                formula.add(index+1, labelFactory.createArithmeticLabel(jPanel2, "+"));
            }

            jPanel2Display(formula);
        } catch (Exception e){}
    }
    
    public void removeLabel (JLabel l, boolean isMoving) {
        if (l.getParent() != jPanel2) return;
        
        try {
            int index = formula.indexOf(l);

            if (index == 0) {
                formula.remove(index);
            } else if (index == formula.size() - 1) {
                formula.remove(index--);
                formula.remove(index);
            } else {
                JLabel left = formula.get(index-1);
                JLabel right = formula.get(index+1);
                formula.remove(l);
                if (left.getText().equals("+") || 
                    left.getText().equals("-") ||
                    left.getText().equals("*") ||
                    left.getText().equals("/")){

                    formula.remove(left);
                } else {
                    formula.remove(right);
                }
            }

            if (!isMoving){
                jPanel2Display(formula);
                variables.remove(l);
            }
        } catch (Exception e) {}
    }
    
    public void moveLabel (JLabel moved, JLabel ref, boolean prev, boolean parenthesis ) {
        removeLabel(moved, true);
        addLabel(moved, ref, prev, parenthesis);
    }
    
    // methods to display the labels 
    
    public void jPanel2Display (ArrayList<JLabel> list) {
        jPanel2.removeAll();
        
        for (int i = 0; i < list.size(); ++i) {
            jPanel2.add(list.get(i), i);
            jPanel2.validate();
        }
        
        deselectAll();
        
        if (formula.isEmpty()) {
            JLabel l = new JLabel();
            l.setSize(1, 60);
            l.setText(" ");
            jPanel2.add(l);
        }
    }   
    
    private void incarcaFormula (JocGeneralDB joc) {
        SerializableFormulaFrame sff = SerializeController.getInstance().deserializaFormulaFrame(joc);
        
        if (sff == null) {
            varCount.setText(FormulaElement.MAX_VARS + "");
            constCount.setText(FormulaElement.MAX_CONSTS + "");
            cronoCount.setText(FormulaElement.MAX_CRONOS + "");
            membriCount.setText(FormulaElement.MAX_NR_MEMBRIS + "");

            formula = new ArrayList<>();
            variables = new HashMap<>();
        } else {
            nrSerie = sff.getSerie();
            varId = sff.getVarId();
            constId = sff.getConstId();
            cronoId = sff.getCronoId();
            membriId = sff.getMembriId();
            varCount.setText(sff.getVarContor()+"");
            constCount.setText(sff.getConstContor()+"");
            cronoCount.setText(sff.getCronoContor()+"");
            membriCount.setText(sff.getMembriContor()+"");
            ArrayList<SerializableLabel> myFormula = sff.getFormula();
            formula = new ArrayList<>();
            variables = new HashMap<>();
            JLabel myLabel;
            
            for (SerializableLabel l : myFormula) {
                if (l.getClass() == SerializableFormulaLabel.class) {
                    myLabel = labelFactory.loadLabel((SerializableFormulaLabel) l, jPanel2);
                    formula.add(myLabel);
                    variables.put(myLabel, ((SerializableFormulaLabel) l).getFe());
                } else {
                    myLabel = labelFactory.loadArithmeticLabel((SerializableArithmeticLabel) l, jPanel2);
                    formula.add(myLabel);
                }
            }
            
            for (JLabel label : formula){
                for (MouseListener ml : label.getMouseListeners()) {
                    label.removeMouseListener(ml);
                }
                if (!isArithmeticLabel(label)) {
                    label.addMouseListener(new FormulaItemListener(label));
                    addToolTip(label, variables.get(label));
                } else {
                    label.addMouseListener(new FormulaOperatorListener(label));
                }
            }
            
            jPanel2Display(formula);
            
            System.out.println("FormulaFrame : salvare completa");
        }
    }
    
    public void addToolTip (JLabel label, FormulaElement e) {
        if (e == null) return;
        
        switch (e.getType()) {
            case CONST :
                label.setToolTipText("<html>Descriere: " + e.getDescriere() + "<br>Valoare: " + e.getValue() + "</html>");
                break;
            case CRONO :
                label.setToolTipText("<html>Descriere: " + e.getDescriere() + "<br>Valoare maxima:" + ((Cronometer) e).getMaxTime() + "</html>");
                break;
            case NR_MEMBRI :
                label.setToolTipText("<html>Descriere: " + e.getDescriere() + "</html>");
                break;
            case VAR :
         /////////       label.setToolTipText("<html>Descriere: " + e.getDescriere() + "<br>Valoare minima: " + ((Variable) e).getMinVal() + "<br>Valoare maxima: " + ((Variable) e).getMaxVal() + "</html>");
               break;
        }
    }   
        
    public static Formula calculeazaFormula (List<SerializableLabel> formula) {
        String formulaString = "";
        HashMap<FormulaElement, String> variablesMap = new HashMap<>();
        
        for (SerializableLabel l : formula) {
            if (l.getClass() == SerializableArithmeticLabel.class) {
                formulaString += ((SerializableArithmeticLabel) l).getOperator();
            } else {
                FormulaElement e = ((SerializableFormulaLabel) l).getFe();
                formulaString += e.getNume();
                variablesMap.put(e, e.getNume());
            }
        }
        Formula f = new Formula(variablesMap, formulaString);
        
        return f;        
    }
    
    private void salveazaFormula () {        
        SerializableFormulaFrame sff = new SerializableFormulaFrame();
        
        if (nrSerie != ControllerDB.getInstance().getLastSerie().getIdSerie() || SerializeController.getInstance().getFormula(joc) == null
                || !ControllerDB.getInstance().isPlayed(MainFrame.getSerieActiva(), joc)) {
            sff.setSerie(ControllerDB.getInstance().getLastSerie().getIdSerie());
            sff.setConstId(constId);
            sff.setCronoId(cronoId);
            sff.setMembriId(membriId);
            sff.setVarId(varId);
            
            ArrayList<SerializableLabel> f = new ArrayList<>();
            SerializableLabel sl;
            for (JLabel l : formula) {
                if (isArithmeticLabel(l)) {
                    sl = new SerializableArithmeticLabel(l.getText());
                    f.add(sl);
                } else {
                    sl = new SerializableFormulaLabel(l.getText(), variables.get(l), l.getToolTipText());
                    f.add(sl);
                }
            }
            
            sff.setFormula(f);
            
            try {
                sff.setConstContor(Integer.parseInt(constCount.getText()));
                sff.setCronoContor(Integer.parseInt(cronoCount.getText()));
                sff.setMembriContor(Integer.parseInt(membriCount.getText()));
                sff.setVarContor(Integer.parseInt(varCount.getText()));
            } catch (Exception e) {}

            if (SerializeController.getInstance().serializeFormulaFrame(joc, sff)) {
                System.out.println("FormulaFrame : salvare completa!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Noua formula a jocului nu se poate salva in timpul seriei active", "Eroare", JOptionPane.ERROR_MESSAGE);
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
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        variable = new javax.swing.JLabel();
        constant = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        cronometer = new javax.swing.JLabel();
        membri = new javax.swing.JLabel();
        varCount = new javax.swing.JLabel();
        constCount = new javax.swing.JLabel();
        cronoCount = new javax.swing.JLabel();
        membriCount = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        garbage = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

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

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel1MouseEntered(evt);
            }
        });

        jLabel1.setText("Elementele formulei :");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(4);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel2.setText("Descriere");

        variable.setText("VARIABILA");
        variable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                variableMouseClicked(evt);
            }
        });

        constant.setText("CONSTANTA");
        constant.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                constantMouseClicked(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        cronometer.setText("CRONOMETRU");
        cronometer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cronometerMouseClicked(evt);
            }
        });

        membri.setText("NR_MEMBRI");
        membri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                membriMouseClicked(evt);
            }
        });

        varCount.setText("jLabel7");

        constCount.setText("jLabel7");

        cronoCount.setText("jLabel7");

        membriCount.setText("jLabel7");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(constant, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                            .addComponent(variable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(varCount, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(constCount, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cronometer, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                            .addComponent(membri, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cronoCount, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(membriCount, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(287, 287, 287)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(310, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(variable)
                                    .addComponent(varCount))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(constant)
                                    .addComponent(constCount)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(cronometer)
                                            .addComponent(cronoCount))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(membri)
                                            .addComponent(membriCount)))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jSeparator2)
                    .addContainerGap()))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel2MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        jLabel11.setText("Formula :");

        garbage.setText("         Arunca");
        garbage.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        garbage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                garbageMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                garbageMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                garbageMouseExited(evt);
            }
        });

        jButton1.setText("Curata");
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

        jButton2.setText("Gata");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Deselecteaza");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(garbage, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(garbage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void variableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_variableMouseClicked
        variable.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
        constant.setBorder(BorderFactory.createEmptyBorder());
        cronometer.setBorder(BorderFactory.createEmptyBorder());
        membri.setBorder(BorderFactory.createEmptyBorder());
        jTextArea1.setText(Descriere.VAR_DESC);
        selectLabel(variable);
    }//GEN-LAST:event_variableMouseClicked

    private void constantMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_constantMouseClicked
        variable.setBorder(BorderFactory.createEmptyBorder());
        constant.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
        cronometer.setBorder(BorderFactory.createEmptyBorder());
        membri.setBorder(BorderFactory.createEmptyBorder());
        jTextArea1.setText(Descriere.CONST_DESC);
        selectLabel(constant);
    }//GEN-LAST:event_constantMouseClicked

    private void cronometerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cronometerMouseClicked
        variable.setBorder(BorderFactory.createEmptyBorder());
        constant.setBorder(BorderFactory.createEmptyBorder());
        cronometer.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
        membri.setBorder(BorderFactory.createEmptyBorder());
        jTextArea1.setText(Descriere.CRONO_DESC);
        selectLabel(cronometer);
    }//GEN-LAST:event_cronometerMouseClicked

    private void membriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_membriMouseClicked
        variable.setBorder(BorderFactory.createEmptyBorder());
        constant.setBorder(BorderFactory.createEmptyBorder());
        cronometer.setBorder(BorderFactory.createEmptyBorder());
        membri.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
        jTextArea1.setText(Descriere.MEMBRI_DESC);
        selectLabel(membri);
    }//GEN-LAST:event_membriMouseClicked

    private void jPanel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseEntered
        HelperFrame.getInstance().post(
                "Chenarul formulei",
                "Aici este reprezentata grafic formula de calcul a jocului selectat. Asupra formulei se pot efectua urmatoarele operatii: \n"
                        + " *adaugarea: avand un element selectat din panoul de mai sus, putem sa-l plasam in formula in raport cu un element deja "
                        + "existent : \n"
                        + "    -intre paranteze, cu un click in centrul elementului de referinta; \n"
                        + "    -in dreapta elementului de referinta, printr-un click in dreapta acestui; \n"
                        + "    -in stanga elementului de referinta, printr-un click in stanga acestuia;\n"
                        + " *modificarea: click dreapta pe elementul dorit pentru a-i modifica valorile; pentru a-i modifica pozitia, "
                        + "il selectam cu click stanga, si aplicam acelasi procedeu ca la adaugare;\n"
                        + " *vizuaizare: lasa cursorul mouse-ului deasupra unui element pentru a vizualiza informatii despre acesta;\n"
                        + " *stergere: selectarea elementului dorit prin click stanga, iar apoi click pe butonul 'Arunca'\n"
                        + "OBSERVATIE: pentru a modifica operatorii aritmetici, click stanga pe acesta pana apare operatorul dorit Acestia se misca prin "
                        + "rotatie in oridnea '+', '-', '*', '/' .");
        if (selectedElement != null) {
            jPanel2.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
        }
    }//GEN-LAST:event_jPanel2MouseEntered

    private void jPanel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseExited
        jPanel2.setBorder(BorderFactory.createLineBorder(Color.black, 2));
    }//GEN-LAST:event_jPanel2MouseExited

    private void garbageMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_garbageMouseEntered
        if (isLabelSelected()) {
            garbage.setBackground(Color.red);
        }
    }//GEN-LAST:event_garbageMouseEntered

    private void garbageMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_garbageMouseExited
        garbage.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_garbageMouseExited

    private void garbageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_garbageMouseClicked
        if (isLabelSelected()) {
            JLabel selected = getSelectedLabel();

            if (selected.getParent() == jPanel1) {
                selected.setBorder(BorderFactory.createEmptyBorder());
            } else if (selected.getParent() == jPanel2) {
            
                switch (selected.getText()) {
                    case VARIABLE_TEXT : 
                        varCount.setText((Integer.parseInt(varCount.getText()) + 1) + "");
                        break;
                    case CONSTANT_TEXT : 
                        constCount.setText((Integer.parseInt(constCount.getText()) + 1) + "");
                        break;
                    case CRONOMETER_TEXT :
                        cronoCount.setText((Integer.parseInt(cronoCount.getText()) + 1) + "");
                        break;
                    case NR_MEMBRI_TEXT :
                        membriCount.setText((Integer.parseInt(membriCount.getText()) + 1) + "");
                        break;
                }
                removeLabel(selected, false);
            }
            
            selected = null;
            selectedElement = null;
            selectedItemFormula = null;
            selectedItemWait = null;
            jTextArea1.setText("");
        }
        
        
    }//GEN-LAST:event_garbageMouseClicked

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        addLabel(getSelectedLabel(), null, false, false);
    }//GEN-LAST:event_jPanel2MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        formula.clear();
        variables.clear();
        jPanel2Display(formula);
        varCount.setText(FormulaElement.MAX_VARS + "");
        constCount.setText(FormulaElement.MAX_CONSTS + "");
        cronoCount.setText(FormulaElement.MAX_CRONOS + "");
        membriCount.setText(FormulaElement.MAX_NR_MEMBRIS + "");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //calculeazaFormula(formula, variables);
        int option = JOptionPane.showConfirmDialog(this, "Odata salvata, regula jocului nu se va mai schimba pana  "
                + "la sfasitul seriei. Continui?", "Salvare", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            salveazaFormula();
            dispose();
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int option = JOptionPane.showConfirmDialog(this, "Salvezi?", "Salvare", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            salveazaFormula();
            dispose();
        } 
        
    }//GEN-LAST:event_formWindowClosing

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        deselectAll();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        HelperFrame.getInstance().post(
                "Fereastra de concepere a formulei",
                "Daca matematica nu a fost materia ta favorita in scoala sau daca nu ai considerat-o vreodata utila la ceva, ei bine, acum "
                        + "este momentul sa trecem peste aceste granite. \n"
                        + "    Aceasta fereastra ofera facilitatea de a compune o formula dupa care sa se calculeze punctajele "
                        + "jocurilor organizate. \n"
                        + "ATENTIE: pentru o evaluare corecta, rezultatul formulei trebuie sa respecte urmatoarele conditii: \n"
                        + " *rezultatul trebuie sa fie intotdeauna pozitiv. Tot ce este mai mic ca 0, se va considera 0; \n"
                        + " *partea fractionara NU trebuie sa faca diferenta intre doua sau mai multe rezultate;\n"
                        + " *trebuie evitata posibilitatea impartirii la zero;\n"
                        + "DE RETINUT: daca animatorul care puncteaza va marca aceasta echipa ca 'absenta', atunci punctajul ei "
                        + "va fi alcatuit din valorile minime ale elementelor formulei.\n"
                        + "ATENTIE: daca cel putin o echipa a jucat acest joc dupa formula creata anterior, aceasta nu va mai putea fi modificata"
                        + " pana la finalul seriei.");
    }//GEN-LAST:event_formMouseEntered

    private void jPanel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseEntered
        HelperFrame.getInstance().post(
                "Panoul variabilelor",
                "De aici poti selecta tipul de element pe care doresti sa il plasezi in formula. Pentru aceasta, se va selecta cu un click "
                        + "(elementul va fi incadrat intr-un chenar galben) unul din cele patru elemente (VARIABILA, CRONOMETRU, CONSTANTA, NR_MEMBRI), "
                        + " si se va plasa in chenarul negru corespunzator 'Formulei' printr-un al doilea click.\n"
                        + "ATENTIE: numarul din dreapta elementului reprezinta de cate ori il vei mai putea folosi in formula.");
    }//GEN-LAST:event_jPanel1MouseEntered

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        HelperFrame.getInstance().post(
                "Curata formula",
                "Sterge toate elementele din panoul formulei.");
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        HelperFrame.getInstance().post(
                "Deselecteaza element",
                "Sterge selectia tuturor elementelor.");
    }//GEN-LAST:event_jButton3MouseEntered

  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel constCount;
    private javax.swing.JLabel constant;
    private javax.swing.JLabel cronoCount;
    private javax.swing.JLabel cronometer;
    private javax.swing.JLabel garbage;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel membri;
    private javax.swing.JLabel membriCount;
    private javax.swing.JLabel varCount;
    private javax.swing.JLabel variable;
    // End of variables declaration//GEN-END:variables
    
    private class Descriere {
        public final static String VAR_DESC =   "        Valoare numerica aleasa arbi-trar de catre utilizator, in confor- mitate cu rezultatele obtinute de    echipa la aceasta proba. ";
        public final static String CONST_DESC = "	Numarul membrilor echipei    care a finalizat jocul.";
        public final static String CRONO_DESC = "	Element folosit pentru     cronometrare. Timpul este de forma hh:mm:ss:f, unde : \n" +
                                                "h - ora\n" +
                                                "m - minut\n" +
                                                "s - secunda\n" +
                                                "f - fractiune de secunda\n" +
                                                "iar valoarea finala va fi data de numarul format prin alipirea celor 4 grupe.	Spre exemplu, pentru un    minut scurs (formatul - 00:01:00:0)se va forma numarul 1000.";
        public final static String MEMBRI_DESC = "	Numarul membrilor echipei    care a finalizat jocul.";
    }
    
    public class FormulaItemListener extends MouseMotionAdapter implements MouseListener {
        private final static int CENTER = 1;
        private final static int LEFT = 2;
        private final static int RIGHT = 3;
        private final static int NONE = 0;
        private final static int DIST_FROM_MARGIN = 20;
        private final JLabel listenedLabel;
        private int position;
        
        public FormulaItemListener (JLabel lb) {
            listenedLabel = lb;
            
        }
        
        @Override
        public void mouseClicked(MouseEvent me) {
            if (me.getButton() == MouseEvent.BUTTON1) {
                if (position != NONE && isLabelSelected()) {
                    if (getSelectedLabel().getParent() == jPanel1)
                        addLabel(getSelectedLabel(), listenedLabel, position == LEFT, position == CENTER);
                    else if (getSelectedLabel().getParent() == jPanel2) {
                        moveLabel(getSelectedLabel(), listenedLabel, position == LEFT, position == CENTER);
                    }
                } 
                selectLabel(listenedLabel);              
            } else if (me.getButton() == MouseEvent.BUTTON3) {
                FormulaElement fe = variables.get(listenedLabel);

                try {
                    switch (fe.getType()) {
                        case CONST : 
                            fe.setDescriere(JOptionPane.showInputDialog(FormulaFrame.this, "Descriere : ", fe.getDescriere()));
                            ((Constant) fe).setValue(Integer.parseInt(
                                                    JOptionPane.showInputDialog(FormulaFrame.this, "Noua valoare (numerica) a constantei:", fe.getValue()+"")));
                            listenedLabel.setToolTipText("<html>Descriere: " + fe.getDescriere() + "<br>Valoare: " + fe.getValue() + "</html>");
                            break;
                        case CRONO : 
                            fe.setDescriere(JOptionPane.showInputDialog(FormulaFrame.this, "Descriere : ", fe.getDescriere()));
                            ((Cronometer) fe).setMaxTime(Integer.parseInt(
                                                    JOptionPane.showInputDialog(FormulaFrame.this, "Timpul maxim (in minute) pana la care poate urca cronometrul :", ((Cronometer) fe).getMaxTime() +"")));
                            listenedLabel.setToolTipText("<html>Descriere: " + fe.getDescriere() + "<br>Valoare maxima:" + ((Cronometer) fe).getMaxTime() + "</html>");
                            break;
                        case NR_MEMBRI :
                            fe.setDescriere(JOptionPane.showInputDialog(FormulaFrame.this, "Descriere : ", fe.getDescriere()));
                            listenedLabel.setToolTipText("<html>Descriere: " + fe.getDescriere() + "</html>");
                            break;
                        case VAR :
                            String desc = JOptionPane.showInputDialog(FormulaFrame.this, "Descriere : ", fe.getDescriere());
                            fe.setDescriere(desc == null ? fe.getDescriere() : desc);
                            ((Variable) fe).setMinVal(Integer.parseInt(
                                                      JOptionPane.showInputDialog(FormulaFrame.this, "Valoare minima :", ((Variable) fe).getMinVal()+"")));
                            ((Variable) fe).setMaxVal(Integer.parseInt(
                                                      JOptionPane.showInputDialog(FormulaFrame.this, "Valoare maxima :", ((Variable) fe).getMaxVal()+"")));
                            listenedLabel.setToolTipText("<html>Descriere: " + 
                                    fe.getDescriere() + "<br>Valoare minima: " + 
                                    ((Variable) fe).getMinVal() + "<br>Valoare maxima: " + 
                                    ((Variable) fe).getMaxVal() + "</html>");
                            break;
                    }
                    variables.put(listenedLabel, fe);
                } catch (Exception e) {}
            }
            
        }

        @Override
        public void mousePressed(MouseEvent me) {}

        @Override
        public void mouseReleased(MouseEvent me) {}

        @Override
        public void mouseEntered(MouseEvent me) {
            if (isLabelSelected()) {
                if (me.getX() >= DIST_FROM_MARGIN &&                 // daca cursorul se afla pe centrul etichetei
                        me.getX() <= listenedLabel.getWidth() - DIST_FROM_MARGIN){
                    
                    deselectNeighborhs(listenedLabel, formula);
                    position = CENTER;
                } else if (me.getX() < DIST_FROM_MARGIN){ // cursorul se afla pe partea stanga a etichetei
                    selectNeighborhs(listenedLabel, formula, false); 
                    position = LEFT;
                } else {
                    selectNeighborhs(listenedLabel, formula, true);   
                    position = RIGHT;
                }
                listenedLabel.setBorder(SELECTED_BORDER);
            }               
                
        }

        @Override
        public void mouseExited(MouseEvent me) {
            deselectNeighborhs(listenedLabel, formula);
            if (getSelectedLabel() != listenedLabel)
                listenedLabel.setBorder(EMPTY_BORDER);
            position = NONE;
        }
        
        @Override
        public void mouseMoved (MouseEvent me) {
            mouseEntered(me);
        }       
    }
    
    public class FormulaOperatorListener extends MouseAdapter {
        private final JLabel listenedLabel;
        private final String [] operator = new String[]{" + ", " - ", " * ", " / "};
        private int index = 0;
        
        public FormulaOperatorListener (JLabel l) {
            listenedLabel = l;
        }
        
        @Override
        public void mouseClicked (MouseEvent me) {
            if (index == 3) {
                index = 0;
            } else {
                index++;
            }
            listenedLabel.setText(operator[index]);
        }
    }
    
    public class LabelFactory {
        private final static int STANDARD_WIDTH = 70;
        private final static int ARITHMETIC_WIDTH = 40;
        private final static String STANDARD_OPERATOR = " + ";
        
        private LabelFactory () {}  
        
        
        public JLabel createLabel (JPanel panel, ElementType type) {
            //
            JLabel l  = new JLabel();
            l.setSize(STANDARD_WIDTH, panel.getHeight());
            l.setBorder(EMPTY_BORDER);
            
            FormulaItemListener listener = new FormulaItemListener(l);
            
            l.addMouseListener(listener);
            l.addMouseMotionListener(listener);
            l.setToolTipText("Click-stanga pentru a edita proprietatile");
                        
            FormulaElement em = null;
            String nume;
            String descriere;  
                     
            descriere = JOptionPane.showInputDialog(FormulaFrame.this, "Descrie pe scurt rolul variabilei :", "Descriere", JOptionPane.PLAIN_MESSAGE);
            if (descriere == null) return null;
            
            switch (type) {
                case CONST :
                    nume = "const" + (constId++);
                    l.setText(CONSTANT_TEXT);
                    em = new Constant(nume, descriere); 
                    try {
                        int aux = Integer.parseInt(JOptionPane.showInputDialog(FormulaFrame.this, "Valoare constantei ", "Atribuire", JOptionPane.PLAIN_MESSAGE));
                        ((Constant) em).setValue(aux);
                        l.setToolTipText("<html>Descriere: " + descriere + "<br>Valoare: " + aux + "</html>");
                    } catch (Exception e) {return null;}
                    break;
                case CRONO : 
                    nume = "crono" + (cronoId++);
                    l.setText(CRONOMETER_TEXT);
                    em = new Cronometer(nume, descriere); 
                    try {
                        int aux = Integer.parseInt(JOptionPane.showInputDialog(FormulaFrame.this, "Timp limita (in minute) : ", "Atribuire", JOptionPane.PLAIN_MESSAGE));
                        ((Cronometer) em).setMaxTime(aux);
                        l.setToolTipText("<html>Descriere: " + descriere + "<br>Valoare maxima:" + aux + "</html>");
                    } catch (Exception e) {return null;}
                    
                    break;
                case NR_MEMBRI : 
                    nume = "membri" + (membriId++);
                    l.setText(NR_MEMBRI_TEXT);
                    em = new MembriCount(nume, descriere);
                    l.setToolTipText("<html>Descriere: " + descriere + "</html>");
                    break;
                case VAR : 
                    nume = "var" + (varId++);
                    l.setText(VARIABLE_TEXT);
                    em = new Variable(nume, descriere); 
                    try {
                        int aux1 = Integer.parseInt(JOptionPane.showInputDialog(FormulaFrame.this, "Valoare minima : ", "Atribuire", JOptionPane.PLAIN_MESSAGE));
                        int aux2 = Integer.parseInt(JOptionPane.showInputDialog(FormulaFrame.this, "Valoare Maxima : ", "Atribuire", JOptionPane.PLAIN_MESSAGE));
                        ((Variable) em).setMinVal(aux1);
                        ((Variable) em).setMaxVal(aux2);
                        l.setToolTipText("<html>Descriere: " + descriere + "<br>Valoare minima: " + aux1 + "<br>Valoare maxima: " + aux2 + "</html>");
                    } catch (Exception e) {return null;}
                    break;
                    
            }
            
            if (em != null && variables.containsValue(em)) {
                nume = JOptionPane.showInputDialog(FormulaFrame.this, "Introdu un nume al variabilei care sa nu fie folosit deja :", "Nume", JOptionPane.QUESTION_MESSAGE); 
                em.setNume(nume);
            }
            
            if (em != null && variables.containsValue(em)) {
                return null;
            }
            
            variables.put(l, em);
            return l;
        }
        
        public JLabel createArithmeticLabel (JPanel panel, String operator) {
            if (operator == null) operator = STANDARD_OPERATOR;
            JLabel l = new JLabel();
            l.setText(operator);
            l.setSize(ARITHMETIC_WIDTH, panel.getHeight());
            l.setBorder(EMPTY_BORDER);
            l.addMouseListener(new FormulaOperatorListener(l));
            return l;
        }
        
        public JLabel loadLabel (SerializableFormulaLabel sl, JPanel panel) {
            JLabel l = new JLabel();
            l.setText(sl.getLabelText());
            l.setToolTipText(sl.getToolTip());
            l.setSize(STANDARD_WIDTH, panel.getHeight());
            
            FormulaItemListener listener = new FormulaItemListener(l);
            l.addMouseListener(listener);
            l.addMouseMotionListener(listener);
            
            return l;
        }
        
        public JLabel loadArithmeticLabel (SerializableArithmeticLabel l, JPanel panel) {
            JLabel label = new JLabel();
            label.setText(l.getOperator());
            label.setSize(ARITHMETIC_WIDTH, panel.getHeight());
            label.setBorder(EMPTY_BORDER);
            
            FormulaOperatorListener fol = new FormulaOperatorListener(label);
            label.addMouseListener(fol);
            
            return label;
        }
        
    }
    
}
