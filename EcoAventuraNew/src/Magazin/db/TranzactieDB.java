/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Magazin.db;

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
 * @author Marius
 */
@Entity
@Table(name = "tranzactie")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TranzactieDB.findAll", query = "SELECT t FROM TranzactieDB t"),
    @NamedQuery(name = "TranzactieDB.findByIdTranzactie", query = "SELECT t FROM TranzactieDB t WHERE t.idTranzactie = :idTranzactie"),
    @NamedQuery(name = "TranzactieDB.findByBucati", query = "SELECT t FROM TranzactieDB t WHERE t.bucati = :bucati"),
    @NamedQuery(name = "TranzactieDB.findByAdaugate", query = "SELECT t FROM TranzactieDB t WHERE t.adaugate = :adaugate"),
    @NamedQuery(name = "TranzactieDB.findByData", query = "SELECT t FROM TranzactieDB t WHERE t.data = :data"),
    @NamedQuery(name = "TranzactieDB.findByObservatii", query = "SELECT t FROM TranzactieDB t WHERE t.observatii = :observatii"),
    @NamedQuery(name = "TranzactieDB.findByPret", query = "SELECT t FROM TranzactieDB t WHERE t.pret = :pret")})
public class TranzactieDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idTranzactie")
    private Integer idTranzactie;
    @Basic(optional = false)
    @Column(name = "Bucati")
    private int bucati;
    @Basic(optional = false)
    @Column(name = "Adaugate")
    private boolean adaugate;
    @Basic(optional = false)
    @Column(name = "Data")
    private String data;
    @Column(name = "Observatii")
    private String observatii;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Pret")
    private Float pret;
    @JoinColumn(name = "idProdus", referencedColumnName = "idProdus")
    @ManyToOne(optional = false)
    private ProdusDB idProdus;

    public TranzactieDB() {
    }

    public TranzactieDB(Integer idTranzactie) {
        this.idTranzactie = idTranzactie;
    }

    public TranzactieDB(Integer idTranzactie, int bucati, boolean adaugate, String data) {
        this.idTranzactie = idTranzactie;
        this.bucati = bucati;
        this.adaugate = adaugate;
        this.data = data;
    }

    public Integer getIdTranzactie() {
        return idTranzactie;
    }

    public void setIdTranzactie(Integer idTranzactie) {
        this.idTranzactie = idTranzactie;
    }

    public int getBucati() {
        return bucati;
    }

    public void setBucati(int bucati) {
        this.bucati = bucati;
    }

    public boolean getAdaugate() {
        return adaugate;
    }

    public void setAdaugate(boolean adaugate) {
        this.adaugate = adaugate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getObservatii() {
        return observatii;
    }

    public void setObservatii(String observatii) {
        this.observatii = observatii;
    }

    public Float getPret() {
        return pret;
    }

    public void setPret(Float pret) {
        this.pret = pret;
    }

    public ProdusDB getIdProdus() {
        return idProdus;
    }

    public void setIdProdus(ProdusDB idProdus) {
        this.idProdus = idProdus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTranzactie != null ? idTranzactie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TranzactieDB)) {
            return false;
        }
        TranzactieDB other = (TranzactieDB) object;
        if ((this.idTranzactie == null && other.idTranzactie != null) || (this.idTranzactie != null && !this.idTranzactie.equals(other.idTranzactie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Magazin.db.TranzactieDB[ idTranzactie=" + idTranzactie + " ]";
    }
    
}
