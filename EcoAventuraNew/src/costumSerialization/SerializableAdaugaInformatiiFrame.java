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

/**
 *
 * @author Alexandru
 */
public class SerializableAdaugaInformatiiFrame implements Serializable{
    private List<ActivitateDTO> activitati;
    private List<JocDTO> jocuri;
    private Date date;

    public SerializableAdaugaInformatiiFrame() {
        date = Calendar.getInstance().getTime();
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
    
    public Date getDate () {
        return this.date;
    }    
}
