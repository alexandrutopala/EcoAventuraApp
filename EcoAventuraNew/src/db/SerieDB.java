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
@Table(name = "serie")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SerieDB.findAll", query = "SELECT s FROM SerieDB s"),
    @NamedQuery(name = "SerieDB.findByIdSerie", query = "SELECT s FROM SerieDB s WHERE s.idSerie = :idSerie"),
    @NamedQuery(name = "SerieDB.findByNumarSerie", query = "SELECT s FROM SerieDB s WHERE s.numarSerie = :numarSerie"),
    @NamedQuery(name = "SerieDB.findByDataInceput", query = "SELECT s FROM SerieDB s WHERE s.dataInceput = :dataInceput"),
    @NamedQuery(name = "SerieDB.findMaxSerieNumber", query = "SELECT MAX(s.numarSerie) FROM SerieDB s")})
public class SerieDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idSerie")
    private Integer idSerie;
    @Column(name = "numarSerie")
    private Integer numarSerie;
    @Column(name = "dataInceput")
    private String dataInceput;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serieidSerie")
    private Collection<EchipaDB> echipaDBCollection;

    public SerieDB() {
    }

    public SerieDB(Integer idSerie) {
        this.idSerie = idSerie;
    }

    public Integer getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(Integer idSerie) {
        this.idSerie = idSerie;
    }

    public Integer getNumarSerie() {
        return numarSerie;
    }

    public void setNumarSerie(Integer numarSerie) {
        this.numarSerie = numarSerie;
    }

    public String getDataInceput() {
        return dataInceput;
    }

    public void setDataInceput(String dataInceput) {
        this.dataInceput = dataInceput;
    }

    @XmlTransient
    public Collection<EchipaDB> getEchipaDBCollection() {
        return echipaDBCollection;
    }

    public void setEchipaDBCollection(Collection<EchipaDB> echipaDBCollection) {
        this.echipaDBCollection = echipaDBCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSerie != null ? idSerie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SerieDB)) {
            return false;
        }
        SerieDB other = (SerieDB) object;
        if ((this.idSerie == null && other.idSerie != null) || (this.idSerie != null && !this.idSerie.equals(other.idSerie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return numarSerie+ " (" + dataInceput + ") ";
    }
    
}
