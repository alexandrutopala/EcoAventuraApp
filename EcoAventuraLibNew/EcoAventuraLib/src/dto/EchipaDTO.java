/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Alexandru
 */
public class EchipaDTO implements Serializable{
    private int id;
    private String numeEchipa;
    private String culoareEchipa;
    private String scoalaEchipa;
    private String profEhipa;
    private SerieDTO serie;
    private List<ActivitateDTO> activitatiEchipa;
    private List<JocDTO> jocuriEchipa;
    private List<MembruEchipaDTO> membriEchipa;

    public SerieDTO getSerie() {
        return serie;
    }

    public void setSerie(SerieDTO serie) {
        this.serie = serie;
    }
    
    public EchipaDTO(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getProfEhipa() {
        return profEhipa;
    }

    public void setProfEhipa(String profEhipa) {
        this.profEhipa = profEhipa;
    }

    public List<ActivitateDTO> getActivitatiEchipa() {
        return activitatiEchipa;
    }

    public void setActivitatiEchipa(List<ActivitateDTO> activitatiEchipa) {
        this.activitatiEchipa = activitatiEchipa;
    }

    public List<JocDTO> getJocuriEchipa() {
        return jocuriEchipa;
    }

    public void setJocuriEchipa(List<JocDTO> jocuriEchipa) {
        this.jocuriEchipa = jocuriEchipa;
    }

    public List<MembruEchipaDTO> getMembriEchipa() {
        return membriEchipa;
    }

    public void setMembriEchipa(List<MembruEchipaDTO> membriEchipa) {
        this.membriEchipa = membriEchipa;
    }

    @Override
    public String toString() {
        return numeEchipa;
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
        final EchipaDTO other = (EchipaDTO) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
}
