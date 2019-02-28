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
import javax.persistence.FetchType;
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
@Table(name = "activitate")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActivitateDB.findAll", query = "SELECT a FROM ActivitateDB a"),
    @NamedQuery(name = "ActivitateDB.findByIdActivitate", query = "SELECT a FROM ActivitateDB a WHERE a.idActivitate = :idActivitate"),
    @NamedQuery(name = "ActivitateDB.findByOrganizator", query = "SELECT a FROM ActivitateDB a WHERE a.organizator = :organizator"),
    @NamedQuery(name = "ActivitateDB.findByData", query = "SELECT a FROM ActivitateDB a WHERE a.data = :data"),
    @NamedQuery(name = "ActivitateDB.findByLoactie", query = "SELECT a FROM ActivitateDB a WHERE a.loactie = :loactie"),
    @NamedQuery(name = "ActivitateDB.findByPost", query = "SELECT a FROM ActivitateDB a WHERE a.post = :post"),
    @NamedQuery(name = "ActivitateDB.findByEchipa", query = "SELECT a FROM ActivitateDB a WHERE a.echipaidEchipa = :echipa"),
    @NamedQuery(name = "ActivitateDB.findByActivitateGenerala", query = "SELECT a FROM ActivitateDB a WHERE a.activitateGeneralaidActivitateGenerala = :activitateGenerala"),
    @NamedQuery(name = "ActivitateDB.findByDataAndActivitateGeneralaAndEchipa", query = "SELECT a FROM ActivitateDB a WHERE a.activitateGeneralaidActivitateGenerala = :activitateGenerala AND a.echipaidEchipa = :echipaId AND a.data = :data"),
    @NamedQuery(name = "ActivitateDB.findByIdProgramAndActivitateGeneralaAndPerioada", query = "SELECT a FROM ActivitateDB a WHERE a.activitateGeneralaidActivitateGenerala = :activitateGenerala AND a.idProgram = :idProgram AND a.loactie LIKE :perioada"),
    @NamedQuery(name = "ActivitateDB.findByActivitateGeneralaAndEchipaAndIdProgram", query = "SELECT a FROM ActivitateDB a WHERE a.activitateGeneralaidActivitateGenerala = :activitateGenerala AND a.echipaidEchipa = :echipa AND a.idProgram = :idProgram"),
    @NamedQuery(name = "ActivitateDB.findByActivitateGeneralaAndDataAndPerioada", query = "SELECT a FROM ActivitateDB a WHERE a.activitateGeneralaidActivitateGenerala = :activitateGenerala AND a.data = :data AND a.loactie LIKE :perioada"),
    @NamedQuery(name = "ActivitateDB.findByActivitateGeneralaAndEchipaAndIdProgramAndPerioada", query = "SELECT a FROM ActivitateDB a WHERE a.activitateGeneralaidActivitateGenerala = :activitateGenerala AND a.echipaidEchipa = :echipa AND a.idProgram = :idProgram AND a.loactie LIKE :perioada"), 
    @NamedQuery(name = "ActivitateDB.findMaxId", query = "SELECT MAX(a.idProgram) FROM ActivitateDB a")})
public class ActivitateDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idActivitate")
    private Integer idActivitate;
    @Column(name = "organizator")
    private String organizator;
    @Column(name = "data")
    private String data;
    @Basic(optional = false)
    @Column(name = "loactie")
    private String loactie;
    @Basic(optional = false)
    @Column(name = "post")
    private String post;
    @Basic(optional= false)
    @Column(name = "idProgram")
    private long idProgram;
    @JoinColumn(name = "Echipa_idEchipa",referencedColumnName = "idEchipa")
    @ManyToOne(optional = false)
    private EchipaDB echipaidEchipa;
    @JoinColumn(name = "ActivitateGenerala_idActivitateGenerala", referencedColumnName = "idActivitateGenerala")
    @ManyToOne(optional = false)
    private ActivitateGeneralaDB activitateGeneralaidActivitateGenerala;

    public long getIdProgram() {
        return idProgram;
    }

    public void setIdProgram(long idProgram) {
        this.idProgram = idProgram;
    }    
    
    public ActivitateDB() {
    }

    public ActivitateDB(Integer idActivitate) {
        this.idActivitate = idActivitate;
    }

    public ActivitateDB(Integer idActivitate, String loactie, String post) {
        this.idActivitate = idActivitate;
        this.loactie = loactie;
        this.post = post;
    }

    public Integer getIdActivitate() {
        return idActivitate;
    }

    public void setIdActivitate(Integer idActivitate) {
        this.idActivitate = idActivitate;
    }

    public String getOrganizator() {
        return organizator;
    }

    public void setOrganizator(String organizator) {
        this.organizator = organizator;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLoactie() {
        return loactie;
    }

    public void setLoactie(String loactie) {
        this.loactie = loactie;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public EchipaDB getEchipaidEchipa() {
        return echipaidEchipa;
    }

    public void setEchipaidEchipa(EchipaDB echipaidEchipa) {
        this.echipaidEchipa = echipaidEchipa;
    }

    public ActivitateGeneralaDB getActivitateGeneralaidActivitateGenerala() {
        return activitateGeneralaidActivitateGenerala;
    }

    public void setActivitateGeneralaidActivitateGenerala(ActivitateGeneralaDB activitateGeneralaidActivitateGenerala) {
        this.activitateGeneralaidActivitateGenerala = activitateGeneralaidActivitateGenerala;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idActivitate != null ? idActivitate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActivitateDB)) {
            return false;
        }
        ActivitateDB other = (ActivitateDB) object;
        if ((this.idActivitate == null && other.idActivitate != null) || (this.idActivitate != null && !this.idActivitate.equals(other.idActivitate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return activitateGeneralaidActivitateGenerala.toString();
    }
    
}
