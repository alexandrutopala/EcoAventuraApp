/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Alexandru
 */
public class ActivitateGeneralaDTO implements Serializable{
    private int idActivitateGenerala;
    private String numeActivitateGenerala;

    public int getIdActivitateGenerala() {
        return idActivitateGenerala;
    }

    public void setIdActivitateGenerala(int idActivitateGenerala) {
        this.idActivitateGenerala = idActivitateGenerala;
    }

    public String getNumeActivitateGenerala() {
        return numeActivitateGenerala;
    }

    public void setNumeActivitateGenerala(String numeActivitateGenerala) {
        this.numeActivitateGenerala = numeActivitateGenerala;
    }

    @Override
    public String toString() {
        return numeActivitateGenerala;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.idActivitateGenerala;
        hash = 23 * hash + Objects.hashCode(this.numeActivitateGenerala);
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
        final ActivitateGeneralaDTO other = (ActivitateGeneralaDTO) obj;
        if (!Objects.equals(this.numeActivitateGenerala, other.numeActivitateGenerala)) {
            return false;
        }
        return true;
    }
    
    
}
