/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula;

/**
 *
 * @author Alexandru
 */
public class Variable extends FormulaElement implements java.io.Serializable{
    private int value = 0;
    private int minVal = 0;
    private int maxVal = 0;
    
    public Variable(String nume, String descriere) {
        super(ElementType.VAR, nume, descriere);
    }

    @Override
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMinVal() {
        return minVal;
    }

    public void setMinVal(int minVal) {
        this.minVal = minVal;
    }

    public int getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(int maxVal) {
        this.maxVal = maxVal;
    }
    
    
}
