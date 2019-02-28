/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package costumSerialization;

import db.EchipaDB;
import dto.ActivitateDTO;
import dto.JocDTO;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

/**
 *
 * @author Alexandru
 */
public class SerializableDistribuieEchipeFrame implements Serializable{
    private Date date;
    private List<ActivitateDTO> activitati;
    private List<JocDTO> jocuri;
    private DefaultComboBoxModel<ActivitateDTO> comboActivitati;
    private DefaultComboBoxModel<JocDTO> comboJocuri;
    private HashMap<ActivitateDTO, DefaultListModel<EchipaDB> > activitatiMap;
    private HashMap<JocDTO, DefaultListModel<EchipaDB> > jocuriMap;
    
    public SerializableDistribuieEchipeFrame () {
        date = Calendar.getInstance().getTime();
    }

    public Date getDate() {
        return date;
    }

    public List<ActivitateDTO> getActivitati() {
        return activitati;
    }

    public void setActivitati(List<ActivitateDTO> activitati) {
        this.activitati = activitati;
    }

    public List<JocDTO> getJocuri() {
        return jocuri;
    }

    public void setJocuri(List<JocDTO> jocuri) {
        this.jocuri = jocuri;
    }
    
    public DefaultComboBoxModel<ActivitateDTO> getComboActivitati() {
        return comboActivitati;
    }

    public void setComboActivitati(DefaultComboBoxModel<ActivitateDTO> comboActivitati) {
        this.comboActivitati = comboActivitati;
    }

    public DefaultComboBoxModel<JocDTO> getComboJocuri() {
        return comboJocuri;
    }

    public void setComboJocuri(DefaultComboBoxModel<JocDTO> comboJocuri) {
        this.comboJocuri = comboJocuri;
    }

    public HashMap<ActivitateDTO, DefaultListModel<EchipaDB>> getActivitatiMap() {
        return activitatiMap;
    }

    public void setActivitatiMap(HashMap<ActivitateDTO, DefaultListModel<EchipaDB>> activitatiMap) {
        this.activitatiMap = activitatiMap;
    }

    public HashMap<JocDTO, DefaultListModel<EchipaDB>> getJocuriMap() {
        return jocuriMap;
    }

    public void setJocuriMap(HashMap<JocDTO, DefaultListModel<EchipaDB>> jocuriMap) {
        this.jocuriMap = jocuriMap;
    }
    
    
}
