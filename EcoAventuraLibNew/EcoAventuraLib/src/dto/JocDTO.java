/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import formula.Formula;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Alexandru
 */
public class JocDTO implements Serializable{
    private int id;
    private String perioada; // 'dimineata', 'pranz', 'seara'
    private List<AnimatorDTO> animatori = new ArrayList<>();
    private List<EchipaDTO> echipe;
    private String locatie;
    private String post;
    private String detalii;
    private JocGeneralDTO jocGeneral;
    private Integer punctaj;
    private int ordin;
    private long idProgram;
    private List<EchipaDTO> echipeAbsente = new ArrayList<>();
    private HashMap<EchipaDTO, Formula> formulas;
    private boolean allowsAnimatorPrincipal = true;

    public long getIdProgram() {
        return idProgram;
    }

    public void setIdProgram(long idProgram) {
        this.idProgram = idProgram;
    }
    
    public int getOrdin() {
        return ordin;
    }
    
    public void setOrdin(int ordin) {
        this.ordin = ordin;
    } 
    
    public boolean isAllowsAnimatorPrincipal() {
        return allowsAnimatorPrincipal;
    }

    public void setAllowsAnimatorPrincipal(boolean allowsAnimatorPrincipal) {
        this.allowsAnimatorPrincipal = allowsAnimatorPrincipal;
    }
    
    public HashMap<EchipaDTO, Formula> getFormulas() {
        return formulas;
    }

    public void setFormulas(HashMap<EchipaDTO, Formula> formulas) {
        this.formulas = formulas;
    }

    public List<EchipaDTO> getEchipe() {
        return echipe;
    }

    public void setEchipe(List<EchipaDTO> echipe) {
        this.echipe = echipe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<EchipaDTO> getEchipeAbsente() {
        return echipeAbsente;
    }

    public void setEchipeAbsente(List<EchipaDTO> echipeAbsente) {
        if (echipeAbsente != null) {
            this.echipeAbsente = echipeAbsente;
        }
    }
    
    public boolean addEchipaAbsenta (EchipaDTO e) {
        return echipeAbsente.add(e);
    }
    
    @Override
    public String toString() {
        return jocGeneral.getNumeJocGeneral() + "(" + perioada + ")";
    }

    public String getPerioada() {
        return perioada;
    }

    public void setPerioada(String perioada) {
        this.perioada = perioada;
    }

    public List<AnimatorDTO> getAnimatori() {
        return animatori;
    }

    public void setAnimatori(List<AnimatorDTO> animatori) {
        this.animatori = animatori;
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

    public String getDetalii() {
        return detalii;
    }

    public void setDetalii(String detalii) {
        this.detalii = detalii;
    }

    public JocGeneralDTO getJocGeneral() {
        return jocGeneral;
    }

    public void setJocGeneral(JocGeneralDTO jocGeneral) {
        this.jocGeneral = jocGeneral;
    }

    public Integer getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(Integer punctaj) {
        this.punctaj = punctaj;
    }
    
    public void adaugaAnimator (AnimatorDTO a){
        animatori.add(a);
    }
    
    public void stergeAnimator (AnimatorDTO a) {
        animatori.remove(a);
    }    

    @Override
    public int hashCode() {
        int hash = 3;
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
        final JocDTO other = (JocDTO) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.perioada, other.perioada)) {
            return false;
        }
        if (!Objects.equals(this.jocGeneral, other.jocGeneral)) {
            return false;
        }
        return true;
    }
    
    
}
