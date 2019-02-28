/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alexandru
 */
@Entity
@Table(name = "activitategenerala")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActivitateGeneralaDB.findAll", query = "SELECT a FROM ActivitateGeneralaDB a"),
    @NamedQuery(name = "ActivitateGeneralaDB.findByIdActivitateGenerala", query = "SELECT a FROM ActivitateGeneralaDB a WHERE a.idActivitateGenerala = :idActivitateGenerala"),
    @NamedQuery(name = "ActivitateGeneralaDB.findByNumeActivitateGenerala", query = "SELECT a FROM ActivitateGeneralaDB a WHERE a.numeActivitateGenerala = :numeActivitateGenerala")})
public class ActivitateGeneralaDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idActivitateGenerala")
    private Integer idActivitateGenerala;
    @Column(name = "numeActivitateGenerala")
    private String numeActivitateGenerala;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activitateGeneralaidActivitateGenerala")
    private Collection<ActivitateDB> activitateDBCollection;

    public ActivitateGeneralaDB() {
    }

    public ActivitateGeneralaDB(Integer idActivitateGenerala) {
        this.idActivitateGenerala = idActivitateGenerala;
    }

    public Integer getIdActivitateGenerala() {
        return idActivitateGenerala;
    }

    public void setIdActivitateGenerala(Integer idActivitateGenerala) {
        this.idActivitateGenerala = idActivitateGenerala;
    }

    public String getNumeActivitateGenerala() {
        return numeActivitateGenerala;
    }

    public void setNumeActivitateGenerala(String numeActivitateGenerala) {
        this.numeActivitateGenerala = numeActivitateGenerala;
    }

    @XmlTransient
    public Collection<ActivitateDB> getActivitateDBCollection() {
        return activitateDBCollection;
    }

    public void setActivitateDBCollection(Collection<ActivitateDB> activitateDBCollection) {
        this.activitateDBCollection = activitateDBCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idActivitateGenerala != null ? idActivitateGenerala.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActivitateGeneralaDB)) {
            return false;
        }
        ActivitateGeneralaDB other = (ActivitateGeneralaDB) object;
        if ((this.idActivitateGenerala == null && other.idActivitateGenerala != null) || (this.idActivitateGenerala != null && !this.idActivitateGenerala.equals(other.idActivitateGenerala))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return numeActivitateGenerala;
    }
    
}
