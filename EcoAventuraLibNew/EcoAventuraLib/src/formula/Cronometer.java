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
public class Cronometer extends FormulaElement implements java.io.Serializable{
    private int time = 0;
    private int maxTime = 0;
    
    public Cronometer(String nume, String descriere) {
        super(ElementType.CRONO, nume, descriere);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }
    
    @Override
    public int getValue () {
        return time;
    }
}
