/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Alexandru
 */
public class AnimatorDTO implements Serializable{
    private int idAnimator;
    private String numeAnimator;
    private boolean disponibilAnimator;
    private List<Object> sarcini;

    public List<Object> getSarcini() {
        return sarcini;
    }

    public void setSarcini(List<Object> sarcini) {
        this.sarcini = sarcini;
    }    

    public int getIdAnimator() {
        return idAnimator;
    }

    public void setIdAnimator(int idAnimator) {
        this.idAnimator = idAnimator;
    }

    @Override
    public String toString() {
        return numeAnimator;
    }

    public String getNumeAnimator() {
        return numeAnimator;
    }

    public void setNumeAnimator(String numeAnimator) {
        this.numeAnimator = numeAnimator;
    }

    public boolean isDisponibilAnimator() {
        return disponibilAnimator;
    }

    public void setDisponibilAnimator(boolean disponibilAnimator) {
        this.disponibilAnimator = disponibilAnimator;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.idAnimator;
        hash = 97 * hash + Objects.hashCode(this.numeAnimator);
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
        final AnimatorDTO other = (AnimatorDTO) obj;
        if (!Objects.equals(this.numeAnimator, other.numeAnimator)) {
            return false;
        }
        if (this.disponibilAnimator != other.disponibilAnimator) {
            return false;
        }
        return true;
    }
    
    
}
