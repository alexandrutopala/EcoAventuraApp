/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula;

import dto.EchipaDTO;

/**
 *
 * @author Alexandru
 */
public class MembriCount extends FormulaElement implements java.io.Serializable{
    private EchipaDTO echipa;
    
    public MembriCount(String nume, String descriere) {
        super(ElementType.NR_MEMBRI, nume, descriere);
    }
    
    @Override
    public int getValue () {
        try {
            return echipa.getMembriEchipa().size();
        } catch (Exception e) {
            return -1;
        }
    }

    public EchipaDTO getEchipa() {
        return echipa;
    }

    public void setEchipa(EchipaDTO echipa) {
        this.echipa = echipa;
    }
    
}
