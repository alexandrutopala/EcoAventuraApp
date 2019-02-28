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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alexandru
 */
@Entity
@Table(name = "membruechipa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MembruEchipaDB.findAll", query = "SELECT m FROM MembruEchipaDB m"),
    @NamedQuery(name = "MembruEchipaDB.findByIdMembruEchipa", query = "SELECT m FROM MembruEchipaDB m WHERE m.idMembruEchipa = :idMembruEchipa"),
    @NamedQuery(name = "MembruEchipaDB.findByNumeMembruEchipa", query = "SELECT m FROM MembruEchipaDB m WHERE m.numeMembruEchipa = :numeMembruEchipa"),
    @NamedQuery(name = "MembruEchipaDB.findByNumeMembruAndIdEchipa", query = "SELECT m FROM MembruEchipaDB m WHERE m.numeMembruEchipa = :numeMembruEchipa AND m.echipaidEchipa = :echipaidEchipa"),
    @NamedQuery(name = "MembruEchipaDB.findByIdEchipa", query = "SELECT m FROM MembruEchipaDB m WHERE m.echipaidEchipa = :echipaidEchipa")})
public class MembruEchipaDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idMembruEchipa")
    private Integer idMembruEchipa;
    @Column(name = "numeMembruEchipa")
    private String numeMembruEchipa;
    @JoinColumn(name = "Echipa_idEchipa", referencedColumnName = "idEchipa")
    @ManyToOne(optional = false)
    private EchipaDB echipaidEchipa;

    public MembruEchipaDB() {
    }

    public MembruEchipaDB(Integer idMembruEchipa) {
        this.idMembruEchipa = idMembruEchipa;
    }

    public Integer getIdMembruEchipa() {
        return idMembruEchipa;
    }

    public void setIdMembruEchipa(Integer idMembruEchipa) {
        this.idMembruEchipa = idMembruEchipa;
    }

    public String getNumeMembruEchipa() {
        return numeMembruEchipa;
    }

    public void setNumeMembruEchipa(String numeMembruEchipa) {
        this.numeMembruEchipa = numeMembruEchipa;
    }

    public EchipaDB getEchipaidEchipa() {
        return echipaidEchipa;
    }

    public void setEchipaidEchipa(EchipaDB echipaidEchipa) {
        this.echipaidEchipa = echipaidEchipa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMembruEchipa != null ? idMembruEchipa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MembruEchipaDB)) {
            return false;
        }
        MembruEchipaDB other = (MembruEchipaDB) object;
        if ((this.idMembruEchipa == null && other.idMembruEchipa != null) || (this.idMembruEchipa != null && !this.idMembruEchipa.equals(other.idMembruEchipa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return numeMembruEchipa;
    }
    
}
