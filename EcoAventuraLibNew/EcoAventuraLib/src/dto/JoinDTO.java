/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.Objects;

/**
 *
 * @author Alexandru
 */
public class JoinDTO implements java.io.Serializable{
    private ActivitateDTO activitate;
    private JocDTO joc;
    
    public JoinDTO (ActivitateDTO activitate, JocDTO joc) {
        this.activitate = activitate;
        this.joc = joc;
    }

    public ActivitateDTO getActivitate() {
        return activitate;
    }

    public void setActivitate(ActivitateDTO activitate) {
        this.activitate = activitate;
    }

    public JocDTO getJoc() {
        return joc;
    }

    public void setJoc(JocDTO joc) {
        this.joc = joc;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final JoinDTO other = (JoinDTO) obj;
        if (!Objects.equals(this.activitate, other.activitate)) {
            return false;
        }
        if (!Objects.equals(this.joc, other.joc)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JoinDTO : " + (activitate != null ? activitate.toString() : joc.toString());
    }
    
    
}
