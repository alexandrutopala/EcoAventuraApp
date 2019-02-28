/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "echipa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EchipaDB.findAll", query = "SELECT e FROM EchipaDB e"),
    @NamedQuery(name = "EchipaDB.findByIdEchipa", query = "SELECT e FROM EchipaDB e WHERE e.idEchipa = :idEchipa"),
    @NamedQuery(name = "EchipaDB.findByNumeEchipa", query = "SELECT e FROM EchipaDB e WHERE e.numeEchipa = :numeEchipa"),
    @NamedQuery(name = "EchipaDB.findByCuloareEchipa", query = "SELECT e FROM EchipaDB e WHERE e.culoareEchipa = :culoareEchipa"),
    @NamedQuery(name = "EchipaDB.findByScoalaEchipa", query = "SELECT e FROM EchipaDB e WHERE e.scoalaEchipa = :scoalaEchipa"),
    @NamedQuery(name = "EchipaDB.findByProfEchipa", query = "SELECT e FROM EchipaDB e WHERE e.profEchipa = :profEchipa"),
    @NamedQuery(name = "EchipaDB.findByIdSerie", query = "SELECT e FROM EchipaDB e WHERE e.serieidSerie = :serieidSerie"),
    @NamedQuery(name = "EchipaDB.findByNumeEchipaAndSerie", query = "SELECT e FROM EchipaDB e WHERE e.numeEchipa = :numeEchipa AND e.serieidSerie = :serieidSerie")})
public class EchipaDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idEchipa")
    private Integer idEchipa;
    @Column(name = "numeEchipa")
    private String numeEchipa;
    @Column(name = "culoareEchipa")
    private String culoareEchipa;
    @Column(name = "scoalaEchipa")
    private String scoalaEchipa;
    @Column(name = "profEchipa")
    private String profEchipa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "echipaidEchipa")
    private Collection<ActivitateDB> activitateDBCollection;
    @JoinColumn(name = "Serie_idSerie", referencedColumnName = "idSerie")
    @ManyToOne(optional = false)
    private SerieDB serieidSerie;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "echipaidEchipa")
    private Collection<MembruEchipaDB> membruEchipaDBCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "echipaidEchipa")
    private Collection<JocDB> jocDBCollection;    
    
    public EchipaDB() {
    }

    public EchipaDB(Integer idEchipa) {
        this.idEchipa = idEchipa;
    }

    public Integer getIdEchipa() {
        return idEchipa;
    }

    public void setIdEchipa(Integer idEchipa) {
        this.idEchipa = idEchipa;
    }

    public String getNumeEchipa() {
        return numeEchipa;
    }

    public void setNumeEchipa(String numeEchipa) {
        this.numeEchipa = numeEchipa;
    }

    public String getCuloareEchipa() {
        return culoareEchipa;
    }

    public void setCuloareEchipa(String culoareEchipa) {
        this.culoareEchipa = culoareEchipa;
    }

    public String getScoalaEchipa() {
        return scoalaEchipa;
    }

    public void setScoalaEchipa(String scoalaEchipa) {
        this.scoalaEchipa = scoalaEchipa;
    }

    public String getProfEchipa() {
        return profEchipa;
    }

    public void setProfEchipa(String profEchipa) {
        this.profEchipa = profEchipa;
    }

    @XmlTransient
    public Collection<ActivitateDB> getActivitateDBCollection() {
        return activitateDBCollection;
    }

    public void setActivitateDBCollection(Collection<ActivitateDB> activitateDBCollection) {
        this.activitateDBCollection = activitateDBCollection;
    }

    public SerieDB getSerieidSerie() {
        return serieidSerie;
    }

    public void setSerieidSerie(SerieDB serieidSerie) {
        this.serieidSerie = serieidSerie;
    }

    @XmlTransient
    public Collection<MembruEchipaDB> getMembruEchipaDBCollection() {
        return membruEchipaDBCollection;
    }

    public void setMembruEchipaDBCollection(Collection<MembruEchipaDB> membruEchipaDBCollection) {
        this.membruEchipaDBCollection = membruEchipaDBCollection;
    }

    @XmlTransient
    public Collection<JocDB> getJocDBCollection() {
        return jocDBCollection;
    }

    public void setJocDBCollection(Collection<JocDB> jocDBCollection) {
        this.jocDBCollection = jocDBCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEchipa != null ? idEchipa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EchipaDB)) {
            return false;
        }
        EchipaDB other = (EchipaDB) object;
        if ((this.idEchipa == null && other.idEchipa != null) || (this.idEchipa != null && !this.idEchipa.equals(other.idEchipa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return numeEchipa;
    }
    
}
