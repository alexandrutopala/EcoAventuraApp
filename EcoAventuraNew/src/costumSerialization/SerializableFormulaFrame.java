/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package costumSerialization;

import java.util.ArrayList;

/**
 *
 * @author Alexandru
 */
public class SerializableFormulaFrame implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private int varId;
    private int constId;
    private int cronoId;
    private int membriId;
    private int varContor;
    private int constContor;
    private int cronoContor;
    private int membriContor;
    private ArrayList<SerializableLabel> formula;
    private int serie;
    
    public SerializableFormulaFrame () {}

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }
    
    public int getVarContor() {
        return varContor;
    }

    public void setVarContor(int varContor) {
        this.varContor = varContor;
    }

    public int getConstContor() {
        return constContor;
    }

    public void setConstContor(int constContor) {
        this.constContor = constContor;
    }

    public int getCronoContor() {
        return cronoContor;
    }

    public void setCronoContor(int cronoContor) {
        this.cronoContor = cronoContor;
    }

    public int getMembriContor() {
        return membriContor;
    }

    public void setMembriContor(int membriContor) {
        this.membriContor = membriContor;
    }
    
    public int getVarId() {
        return varId;
    }

    public void setVarId(int varId) {
        this.varId = varId;
    }

    public int getConstId() {
        return constId;
    }

    public void setConstId(int constId) {
        this.constId = constId;
    }

    public int getCronoId() {
        return cronoId;
    }

    public void setCronoId(int cronoId) {
        this.cronoId = cronoId;
    }

    public int getMembriId() {
        return membriId;
    }

    public void setMembriId(int membriId) {
        this.membriId = membriId;
    }
    
    public ArrayList<SerializableLabel> getFormula() {
        return formula;
    }

    public void setFormula(ArrayList<SerializableLabel> formula) {
        this.formula = formula;
    }
    
    
}
