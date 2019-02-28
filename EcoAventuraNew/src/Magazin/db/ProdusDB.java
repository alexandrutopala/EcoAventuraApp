/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Magazin.db;

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
 * @author Marius
 */
@Entity
@Table(name = "produs")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProdusDB.findAll", query = "SELECT p FROM ProdusDB p"),
    @NamedQuery(name = "ProdusDB.findByIdProdus", query = "SELECT p FROM ProdusDB p WHERE p.idProdus = :idProdus"),
    @NamedQuery(name = "ProdusDB.findByDenumire", query = "SELECT p FROM ProdusDB p WHERE p.denumire = :denumire"),
    @NamedQuery(name = "ProdusDB.findByPret", query = "SELECT p FROM ProdusDB p WHERE p.pret = :pret"),
    @NamedQuery(name = "ProdusDB.findByStoc", query = "SELECT p FROM ProdusDB p WHERE p.stoc = :stoc"),
    @NamedQuery(name = "ProdusDB.findByObservatii", query = "SELECT p FROM ProdusDB p WHERE p.observatii = :observatii")})
public class ProdusDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idProdus")
    private Integer idProdus;
    @Basic(optional = false)
    @Column(name = "Denumire")
    private String denumire;
    @Basic(optional = false)
    @Column(name = "Pret")
    private float pret;
    @Column(name = "Stoc")
    private Integer stoc;
    @Column(name = "Observatii")
    private String observatii;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProdus")
    private Collection<TranzactieDB> tranzactieDBCollection;

    public ProdusDB() {
    }

    public ProdusDB(Integer idProdus) {
        this.idProdus = idProdus;
    }

    public ProdusDB(Integer idProdus, String denumire, float pret) {
        this.idProdus = idProdus;
        this.denumire = denumire;
        this.pret = pret;
    }

    public Integer getIdProdus() {
        return idProdus;
    }

    public void setIdProdus(Integer idProdus) {
        this.idProdus = idProdus;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public float getPret() {
        return pret;
    }

    public void setPret(float pret) {
        this.pret = pret;
    }

    public Integer getStoc() {
        return stoc;
    }

    public void setStoc(Integer stoc) {
        this.stoc = stoc;
    }

    public String getObservatii() {
        return observatii;
    }

    public void setObservatii(String observatii) {
        this.observatii = observatii;
    }

    @XmlTransient
    public Collection<TranzactieDB> getTranzactieDBCollection() {
        return tranzactieDBCollection;
    }

    public void setTranzactieDBCollection(Collection<TranzactieDB> tranzactieDBCollection) {
        this.tranzactieDBCollection = tranzactieDBCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProdus != null ? idProdus.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProdusDB)) {
            return false;
        }
        ProdusDB other = (ProdusDB) object;
        if ((this.idProdus == null && other.idProdus != null) || (this.idProdus != null && !this.idProdus.equals(other.idProdus))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Magazin.db.ProdusDB[ idProdus=" + idProdus + " ]";
    }
    
}
