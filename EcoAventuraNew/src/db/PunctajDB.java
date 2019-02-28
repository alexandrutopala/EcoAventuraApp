/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

/**
 *
 * @author Alexandru
 */
public class PunctajDB implements java.io.Serializable{
    private JocDB joc;
    
    public PunctajDB (JocDB joc) {
        this.joc = joc;
    }
    
    public JocDB getJoc () {
        return joc;
    }
    
    public void setJoc (JocDB joc) {
        this.joc = joc;
    }
    
    @Override
    public String toString () {
        return joc != null ? joc.getPunctaj()+"" : "0";
    }
}
