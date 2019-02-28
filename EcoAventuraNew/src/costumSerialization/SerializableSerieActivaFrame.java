/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package costumSerialization;

import dto.ActivitateDTO;
import dto.JocDTO;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author Alexandru
 */
public class SerializableSerieActivaFrame implements Serializable{
    private DefaultListModel<ActivitateDTO> activitatiDimineata;
    private DefaultListModel<ActivitateDTO> activitatiAmiaza;
    private DefaultListModel<ActivitateDTO> activitatiSeara;
    private DefaultListModel<JocDTO> jocuriDimineata;
    private DefaultListModel<JocDTO> jocuriAmiaza;
    private DefaultListModel<JocDTO> jocuriSeara;
    private List<ActivitateDTO> activitatiS;
    private List<JocDTO> jocuriS;
    private Date date;
    private boolean wereActivitatiReordered;
    private boolean wereJocuriReordered;

    public boolean isWereActivitatiReordered() {
        return wereActivitatiReordered;
    }

    public void setWereActivitatiReordered(boolean wereActivitatiReordered) {
        this.wereActivitatiReordered = wereActivitatiReordered;
    }

    public boolean isWereJocuriReordered() {
        return wereJocuriReordered;
    }

    public void setWereJocuriReordered(boolean wereJocuriReordered) {
        this.wereJocuriReordered = wereJocuriReordered;
    }   
    
    public SerializableSerieActivaFrame() {
        date = Calendar.getInstance().getTime();
    }
    
    public DefaultListModel<ActivitateDTO> getActivitatiDimineata() {
        return activitatiDimineata;
    }

    public void setActivitatiDimineata(DefaultListModel<ActivitateDTO> activitatiDimineata) {
        this.activitatiDimineata = activitatiDimineata;
    }

    public DefaultListModel<ActivitateDTO> getActivitatiAmiaza() {
        return activitatiAmiaza;
    }

    public void setActivitatiAmiaza(DefaultListModel<ActivitateDTO> activitatiAmiaza) {
        this.activitatiAmiaza = activitatiAmiaza;
    }

    public DefaultListModel<ActivitateDTO> getActivitatiSeara() {
        return activitatiSeara;
    }

    public void setActivitatiSeara(DefaultListModel<ActivitateDTO> activitatiSeara) {
        this.activitatiSeara = activitatiSeara;
    }

    public DefaultListModel<JocDTO> getJocuriDimineata() {
        return jocuriDimineata;
    }

    public void setJocuriDimineata(DefaultListModel<JocDTO> jocuriDimineata) {
        this.jocuriDimineata = jocuriDimineata;
    }

    public DefaultListModel<JocDTO> getJocuriAmiaza() {
        return jocuriAmiaza;
    }

    public void setJocuriAmiaza(DefaultListModel<JocDTO> jocuriAmiaza) {
        this.jocuriAmiaza = jocuriAmiaza;
    }

    public DefaultListModel<JocDTO> getJocuriSeara() {
        return jocuriSeara;
    }

    public void setJocuriSeara(DefaultListModel<JocDTO> jocuriSeara) {
        this.jocuriSeara = jocuriSeara;
    }

    public List<ActivitateDTO> getActivitatiS() {
        return activitatiS;
    }

    public void setActivitatiS(List<ActivitateDTO> activitatiS) {
        this.activitatiS = activitatiS;
    }

    public List<JocDTO> getJocuriS() {
        return jocuriS;
    }

    public void setJocuriS(List<JocDTO> jocuriS) {
        this.jocuriS = jocuriS;
    }

    public Date getDate() {
        return date;
    }
        
}
