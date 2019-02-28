/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package costumSerialization;

import db.AnimatorDB;
import dto.ActivitateDTO;
import dto.JocDTO;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

/**
 *
 * @author Alexandru
 */
public class SerializableDistribuieProgramFrame  implements Serializable{
    private DefaultComboBoxModel<AnimatorDB> comboModel2;
    private DefaultListModel<ActivitateDTO> activitati;
    private DefaultListModel<JocDTO> jocuri;
    private HashMap<AnimatorDB, DefaultListModel<ActivitateDTO> > sarcini;
    private HashMap<DefaultListModel<ActivitateDTO> , DefaultListModel<JocDTO> > perechi;    
    private HashMap<ActivitateDTO, Integer> usedActivitati;
    private HashMap<JocDTO, Integer> userJocuri;
    private HashMap<AnimatorDB, DefaultListModel<Object>> sarciniOrdonate;
    private HashMap<AnimatorDB, Boolean> aplica;
    private Date date;

    public SerializableDistribuieProgramFrame() {
         date = Calendar.getInstance().getTime();
    }  

    public HashMap<AnimatorDB, Boolean> getAplica() {
        return aplica;
    }

    public void setAplica(HashMap<AnimatorDB, Boolean> aplica) {
        this.aplica = aplica;
    }

    public HashMap<AnimatorDB, DefaultListModel<Object>> getSarciniOrdonate() {
        return sarciniOrdonate;
    }

    public void setSarciniOrdonate(HashMap<AnimatorDB, DefaultListModel<Object>> sarciniOrdonate) {
        this.sarciniOrdonate = sarciniOrdonate;
    }

    public DefaultComboBoxModel<AnimatorDB> getComboModel2() {
        return comboModel2;
    }

    public void setComboModel2(DefaultComboBoxModel<AnimatorDB> comboModel2) {
        this.comboModel2 = comboModel2;
    }

    public DefaultListModel<ActivitateDTO> getActivitati() {
        return activitati;
    }

    public void setActivitati(DefaultListModel<ActivitateDTO> activitati) {
        this.activitati = activitati;
    }

    public DefaultListModel<JocDTO> getJocuri() {
        return jocuri;
    }

    public void setJocuri(DefaultListModel<JocDTO> jocuri) {
        this.jocuri = jocuri;
    }

    public HashMap<AnimatorDB, DefaultListModel<ActivitateDTO>> getSarcini() {
        return sarcini;
    }

    public void setSarcini(HashMap<AnimatorDB, DefaultListModel<ActivitateDTO>> sarcini) {
        this.sarcini = sarcini;
    }

    public HashMap<DefaultListModel<ActivitateDTO>, DefaultListModel<JocDTO>> getPerechi() {
        return perechi;
    }

    public void setPerechi(HashMap<DefaultListModel<ActivitateDTO>, DefaultListModel<JocDTO>> perechi) {
        this.perechi = perechi;
    }

    public HashMap<ActivitateDTO, Integer> getUsedActivitati() {
        return usedActivitati;
    }

    public void setUsedActivitati(HashMap<ActivitateDTO, Integer> usedActivitati) {
        this.usedActivitati = usedActivitati;
    }

    public HashMap<JocDTO, Integer> getUserJocuri() {
        return userJocuri;
    }

    public void setUserJocuri(HashMap<JocDTO, Integer> userJocuri) {
        this.userJocuri = userJocuri;
    }
    
    

    public Date getDate() {
        return date;
    }
    
    
}
