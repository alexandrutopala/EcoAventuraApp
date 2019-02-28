/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import formula.Formula;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Alexandru
 */
public class JocGeneralDTO implements Serializable{
    private int idJocGeneral;
    private String numeJocGeneral;
    private String descriereJoc;
    private Formula formula;

    public int getIdJocGeneral() {
        return idJocGeneral;
    }

    public String getDescriereJoc() {
        return descriereJoc;
    }

    public void setDescriereJoc(String descriereJoc) {
        this.descriereJoc = descriereJoc;
    }   
   
    public void setIdJocGeneral(int idJocGeneral) {
        this.idJocGeneral = idJocGeneral;
    }

    public String getNumeJocGeneral() {
        return numeJocGeneral;
    }

    public void setNumeJocGeneral(String numeJocGeneral) {
        this.numeJocGeneral = numeJocGeneral;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }      
    
    @Override
    public String toString () {
        return numeJocGeneral;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JocGeneralDTO other = (JocGeneralDTO) obj;
        if (!Objects.equals(this.numeJocGeneral, other.numeJocGeneral)) {
            return false;
        }
        return true;
    }
    
    
}
