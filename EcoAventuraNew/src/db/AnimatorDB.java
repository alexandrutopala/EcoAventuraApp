/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alexandru
 */
@Entity
@Table(name = "animator")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnimatorDB.findAll", query = "SELECT a FROM AnimatorDB a"),
    @NamedQuery(name = "AnimatorDB.findByIdAnimator", query = "SELECT a FROM AnimatorDB a WHERE a.idAnimator = :idAnimator"),
    @NamedQuery(name = "AnimatorDB.findByNumeAnimator", query = "SELECT a FROM AnimatorDB a WHERE a.numeAnimator = :numeAnimator"),
    @NamedQuery(name = "AnimatorDB.findByDisponibilAnimator", query = "SELECT a FROM AnimatorDB a WHERE a.disponibilAnimator = :disponibilAnimator")})
public class AnimatorDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idAnimator")
    private Integer idAnimator;
    @Column(name = "numeAnimator")
    private String numeAnimator;
    @Column(name = "disponibilAnimator")
    private Boolean disponibilAnimator;

    public AnimatorDB() {
    }

    public AnimatorDB(Integer idAnimator) {
        this.idAnimator = idAnimator;
    }

    public Integer getIdAnimator() {
        return idAnimator;
    }

    public void setIdAnimator(Integer idAnimator) {
        this.idAnimator = idAnimator;
    }

    public String getNumeAnimator() {
        return numeAnimator;
    }

    public void setNumeAnimator(String numeAnimator) {
        this.numeAnimator = numeAnimator;
    }

    public Boolean getDisponibilAnimator() {
        return disponibilAnimator;
    }

    public void setDisponibilAnimator(Boolean disponibilAnimator) {
        this.disponibilAnimator = disponibilAnimator;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAnimator != null ? idAnimator.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnimatorDB)) {
            return false;
        }
        AnimatorDB other = (AnimatorDB) object;
        if ((this.idAnimator == null && other.idAnimator != null) || (this.idAnimator != null && !this.idAnimator.equals(other.idAnimator))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return numeAnimator;
    }
    
}
