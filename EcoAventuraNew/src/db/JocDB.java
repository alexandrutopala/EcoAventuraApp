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
@Table(name = "joc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JocDB.findAll", query = "SELECT j FROM JocDB j"),
    @NamedQuery(name = "JocDB.findByIdJoc", query = "SELECT j FROM JocDB j WHERE j.idJoc = :idJoc"),
    @NamedQuery(name = "JocDB.findByOrganizator", query = "SELECT j FROM JocDB j WHERE j.organizator = :organizator"),
    @NamedQuery(name = "JocDB.findByPunctaj", query = "SELECT j FROM JocDB j WHERE j.punctaj = :punctaj"),
    @NamedQuery(name = "JocDB.findByData", query = "SELECT j FROM JocDB j WHERE j.data = :data"),
    @NamedQuery(name = "JocDB.findByLocatie", query = "SELECT j FROM JocDB j WHERE j.locatie = :locatie"),
    @NamedQuery(name = "JocDB.findByPost", query = "SELECT j FROM JocDB j WHERE j.post = :post"),
    @NamedQuery(name = "JocDB.findByEchipa", query = "SELECT j FROM JocDB j WHERE j.echipaidEchipa = :echipa"),
    @NamedQuery(name = "JocDB.findByJocGeneral", query = "SELECT j FROM JocDB j WHERE j.jocGeneralidJocGeneral = :jocGeneral"),
    @NamedQuery(name = "JocDB.findByJocGeneralAndEchipaIdAndData", query = "SELECT j FROM JocDB j WHERE j.data = :data AND j.jocGeneralidJocGeneral = :jocGeneral AND j.echipaidEchipa = :echipaId"),
    @NamedQuery(name = "JocDB.findByEchipaAndJocGeneral", query = "SELECT j FROM JocDB j WHERE j.echipaidEchipa = :echipa AND j.jocGeneralidJocGeneral = :jocGeneral"),
    @NamedQuery(name = "JocDB.findByIdProgramAndJocGeneralAndPerioada", query = "SELECT j FROM JocDB j WHERE j.idProgram = :idProgram AND j.jocGeneralidJocGeneral = :jocGeneral AND j.locatie LIKE :perioada"),
    @NamedQuery(name = "JocDB.findByJocGeneralAndEchipaAndIdProgram", query = "SELECT j FROM JocDB j WHERE j.jocGeneralidJocGeneral = :jocGeneral AND j.echipaidEchipa = :echipa AND j.idProgram = :idProgram"),
    @NamedQuery(name = "JocDB.findByJocGeneralAndDataAndPerioada", query = "SELECT j FROM JocDB j WHERE j.jocGeneralidJocGeneral = :jocGeneral AND j.data = :data AND j.locatie LIKE :perioada"),
    @NamedQuery(name = "JocDB.findByJocGeneralAndEchipaAndIdProgramAndPerioada", query = "SELECT j FROM JocDB j WHERE j.jocGeneralidJocGeneral = :jocGeneral AND j.echipaidEchipa = :echipa AND j.idProgram = :idProgram AND j.locatie LIKE :perioada"),
    @NamedQuery(name = "JocDB.findMaxId", query = "SELECT MAX(j.idProgram) FROM JocDB j")})
public class JocDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idJoc")
    private Integer idJoc;
    @Column(name = "organizator")
    private String organizator;
    @Column(name = "punctaj")
    private Integer punctaj;
    @Column(name = "data")
    private String data;
    @Basic(optional = false)
    @Column(name = "locatie")
    private String locatie;
    @Basic(optional = false)
    @Column(name = "post")
    private String post;
    @Basic(optional = false)
    @Column(name = "absent")
    private boolean absent;
    @Basic(optional = false)
    @Column(name = "idProgram")
    private long idProgram;
    @JoinColumn(name = "JocGeneral_idJocGeneral", referencedColumnName = "idJocGeneral")
    @ManyToOne(optional = false)
    private JocGeneralDB jocGeneralidJocGeneral;
    @JoinColumn(name = "Echipa_idEchipa", referencedColumnName = "idEchipa")
    @ManyToOne(optional = false)
    private EchipaDB echipaidEchipa;

    public JocDB() {
    }

    public JocDB(Integer idJoc) {
        this.idJoc = idJoc;
    }

    public boolean getAbsent() {
        return absent;
    }

    public void setAbsent(boolean absent) {
        this.absent = absent;
    }

    public long getIdProgram() {
        return idProgram;
    }

    public void setIdProgram(long idProgram) {
        this.idProgram = idProgram;
    }

    public JocDB(Integer idJoc, String locatie, String post) {
        this.idJoc = idJoc;
        this.locatie = locatie;
        this.post = post;
    }

    public Integer getIdJoc() {
        return idJoc;
    }

    public void setIdJoc(Integer idJoc) {
        this.idJoc = idJoc;
    }

    public String getOrganizator() {
        return organizator;
    }

    public void setOrganizator(String organizator) {
        this.organizator = organizator;
    }

    public Integer getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(Integer punctaj) {
        this.punctaj = punctaj;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public JocGeneralDB getJocGeneralidJocGeneral() {
        return jocGeneralidJocGeneral;
    }

    public void setJocGeneralidJocGeneral(JocGeneralDB jocGeneralidJocGeneral) {
        this.jocGeneralidJocGeneral = jocGeneralidJocGeneral;
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
        hash += (idJoc != null ? idJoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JocDB)) {
            return false;
        }
        JocDB other = (JocDB) object;
        if ((this.idJoc == null && other.idJoc != null) || (this.idJoc != null && !this.idJoc.equals(other.idJoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return jocGeneralidJocGeneral.toString();
    }
    
}
