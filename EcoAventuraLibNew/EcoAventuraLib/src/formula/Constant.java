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
public class Constant extends FormulaElement implements java.io.Serializable{
    private int value = 0;
    
    public Constant(String nume, String descriere) {
        super(ElementType.CONST, nume, descriere);
    }

    @Override
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    
}
