/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
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
import org.apache.poi.hslf.record.OEPlaceholderAtom;

/**
 *
 * @author Alexandru
 */
@Entity
@Table(name = "jocgeneral")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JocGeneralDB.findAll", query = "SELECT j FROM JocGeneralDB j"),
    @NamedQuery(name = "JocGeneralDB.findByIdJocGeneral", query = "SELECT j FROM JocGeneralDB j WHERE j.idJocGeneral = :idJocGeneral"),
    @NamedQuery(name = "JocGeneralDB.findByNumeJocGeneral", query = "SELECT j FROM JocGeneralDB j WHERE j.numeJocGeneral = :numeJocGeneral")})
public class JocGeneralDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idJocGeneral")
    private Integer idJocGeneral;
    @Column(name = "numeJocGeneral")
    private String numeJocGeneral;
    @Column(name = "descriereJoc")
    private String descriereJoc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jocGeneralidJocGeneral")
    private Collection<JocDB> jocDBCollection;

    public JocGeneralDB() {
    }

    public String getDescriereJoc() {
        return descriereJoc;
    }

    public void setDescriereJoc(String descriereJoc) {
        this.descriereJoc = descriereJoc;
    }    
    
    public JocGeneralDB(Integer idJocGeneral) {
        this.idJocGeneral = idJocGeneral;
    }

    public Integer getIdJocGeneral() {
        return idJocGeneral;
    }

    public void setIdJocGeneral(Integer idJocGeneral) {
        this.idJocGeneral = idJocGeneral;
    }

    public String getNumeJocGeneral() {
        return numeJocGeneral;
    }

    public void setNumeJocGeneral(String numeJocGeneral) {
        this.numeJocGeneral = numeJocGeneral;
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
        hash += (idJocGeneral != null ? idJocGeneral.hashCode() : 0);
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
        final JocGeneralDB other = (JocGeneralDB) obj;
//        if (!Objects.equals(this.idJocGeneral, other.idJocGeneral)){
//            return false;
//        }
//        if (!Objects.equals(this.numeJocGeneral, other.numeJocGeneral)) {
//            return false;
//        }
        if (!this.numeJocGeneral.equalsIgnoreCase(other.numeJocGeneral)){
            return false;
        }
            
        return true;
    }
    
    @Override
    public String toString() {
        return numeJocGeneral;
    }
    
}
