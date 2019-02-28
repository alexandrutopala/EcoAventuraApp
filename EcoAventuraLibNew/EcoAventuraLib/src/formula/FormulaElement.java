/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Alexandru
 */
public abstract class FormulaElement implements Serializable{
    public final static int MAX_VARS = 6;
    public final static int MAX_CONSTS = 5;
    public final static int MAX_CRONOS = 1;
    public final static int MAX_NR_MEMBRIS = 2;
    private ElementType type;
    private String nume; 
    private String descriere;
    
    public FormulaElement (ElementType type, String nume, String descriere) {
        this.type = type;
        this.nume = nume;
        this.descriere = descriere;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
    
    public abstract int getValue ();

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.type);
        hash = 71 * hash + Objects.hashCode(this.nume);
        hash = 71 * hash + Objects.hashCode(this.descriere);
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
        final FormulaElement other = (FormulaElement) obj;
        if (!this.nume.equalsIgnoreCase(other.nume)) {
            return false;
        }
        
        if (this.type != other.type) {
            return false;
        }
        return true;
    }
    
    
    
    @Override
    public String toString () {
        return type + " : " + descriere;
    }
}
