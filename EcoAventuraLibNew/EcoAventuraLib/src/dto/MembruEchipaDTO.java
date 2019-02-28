/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;

/**
 *
 * @author Alexandru
 */
public class MembruEchipaDTO implements Serializable {
    private int id;
    private String numeMembruEchipa;
    private EchipaDTO echipa;

    public MembruEchipaDTO(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNumeMembruEchipa() {
        return numeMembruEchipa;
    }

    public void setNumeMembruEchipa(String numeMembruEchipa) {
        this.numeMembruEchipa = numeMembruEchipa;
    }

    public EchipaDTO getEchipa() {
        return echipa;
    }

    public void setEchipa(EchipaDTO echipa) {
        this.echipa = echipa;
    }

    @Override
    public String toString() {
        return numeMembruEchipa;
    }
    
    
}
