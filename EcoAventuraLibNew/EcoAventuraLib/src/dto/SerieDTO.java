/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Alexandru
 */
public class SerieDTO implements Serializable{
    private int id;
    private int numarSerie;
    private String dataInceput;
    private List<EchipaDTO> echipe;

    public String getDataInceput() {
        return dataInceput;
    }

    public void setDataInceput(String dataInceput) {
        this.dataInceput = dataInceput;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumarSerie() {
        return numarSerie;
    }

    public void setNumarSerie(int numarSerie) {
        this.numarSerie = numarSerie;
    }

    public List<EchipaDTO> getEchipe() {
        return echipe;
    }

    public void setEchipe(List<EchipaDTO> echipe) {
        this.echipe = echipe;
    }


    @Override
    public String toString() {
        return "Seria : " + numarSerie;
    }

    
    
    
}
