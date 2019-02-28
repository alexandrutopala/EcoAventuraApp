/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import db.MembruEchipaDB;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/**
 *
 * @author Marius
 */
public class Inchiriere implements java.io.Serializable {
    private MembruEchipaDB responsabil;
    private String imprumut;
    private boolean returnat;
    private String dataInchiriere;
    private String dataReturnarii;
    
    public Inchiriere (MembruEchipaDB responsabil, String imprumut) {
        this.responsabil = responsabil;
        this.imprumut = imprumut;
        this.returnat = false;
        this.dataInchiriere = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
    }

    public MembruEchipaDB getResponsabil() {
        return responsabil;
    }

    public void setResponsabil(MembruEchipaDB responsabil) {
        this.responsabil = responsabil;
    }

    public String getImprumut() {
        return imprumut;
    }

    public void setImprumut(String imprumut) {
        this.imprumut = imprumut;
    }

    public boolean isReturnat() {
        return returnat;
    }

    public void setReturnat(boolean returnat) {
        this.returnat = returnat;
        if (returnat) {
            dataReturnarii = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        }
    }

    public String getDataInchiriere() {
        return dataInchiriere;
    }

    public void setDataInchiriere(String dataInchiriere) {
        this.dataInchiriere = dataInchiriere;
    }

    public String getDataReturnarii() {
        return dataReturnarii;
    }

    public void setDataReturnarii(String dataReturnarii) {
        this.dataReturnarii = dataReturnarii;
    }    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.responsabil);
        hash = 43 * hash + Objects.hashCode(this.imprumut);
        hash = 43 * hash + (this.returnat ? 1 : 0);
        hash = 43 * hash + Objects.hashCode(this.dataInchiriere);
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
        final Inchiriere other = (Inchiriere) obj;
        if (!Objects.equals(this.responsabil, other.responsabil)) {
            return false;
        }
        if (!Objects.equals(this.imprumut, other.imprumut)) {
            return false;
        }
        if (!Objects.equals(this.dataInchiriere, other.dataInchiriere)) {
            return false;
        }
        return true;
    }
    
}
