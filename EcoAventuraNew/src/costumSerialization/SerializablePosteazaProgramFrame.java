/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package costumSerialization;

import db.ActivitateGeneralaDB;
import db.JocGeneralDB;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.swing.DefaultListModel;

/**
 *
 * @author Alexandru
 */
public class SerializablePosteazaProgramFrame implements Serializable{
    private DefaultListModel<ActivitateGeneralaDB> activitateDimineata;
    private DefaultListModel<ActivitateGeneralaDB> activitateAmiaza;
    private DefaultListModel<ActivitateGeneralaDB> activitateSeara;
    private DefaultListModel<JocGeneralDB> jocDimineata;
    private DefaultListModel<JocGeneralDB> jocAmiaza;
    private DefaultListModel<JocGeneralDB> jocSeara;
    private DefaultListModel<JocGeneralDB> jocuri;
    private DefaultListModel<ActivitateGeneralaDB> activitati;
    private Date date;

    public SerializablePosteazaProgramFrame() {
        date = Calendar.getInstance().getTime();
    }

    public DefaultListModel<ActivitateGeneralaDB> getActivitateDimineata() {
        return activitateDimineata;
    }

    public void setActivitateDimineata(DefaultListModel<ActivitateGeneralaDB> activitateDimineata) {
        this.activitateDimineata = activitateDimineata;
    }

    public DefaultListModel<ActivitateGeneralaDB> getActivitateAmiaza() {
        return activitateAmiaza;
    }

    public void setActivitateAmiaza(DefaultListModel<ActivitateGeneralaDB> activitateAmiaza) {
        this.activitateAmiaza = activitateAmiaza;
    }

    public DefaultListModel<ActivitateGeneralaDB> getActivitateSeara() {
        return activitateSeara;
    }

    public void setActivitateSeara(DefaultListModel<ActivitateGeneralaDB> activitateSeara) {
        this.activitateSeara = activitateSeara;
    }

    public DefaultListModel<JocGeneralDB> getJocDimineata() {
        return jocDimineata;
    }

    public void setJocDimineata(DefaultListModel<JocGeneralDB> jocDimineata) {
        this.jocDimineata = jocDimineata;
    }

    public DefaultListModel<JocGeneralDB> getJocAmiaza() {
        return jocAmiaza;
    }

    public void setJocAmiaza(DefaultListModel<JocGeneralDB> jocAmiaza) {
        this.jocAmiaza = jocAmiaza;
    }

    public DefaultListModel<JocGeneralDB> getJocSeara() {
        return jocSeara;
    }

    public void setJocSeara(DefaultListModel<JocGeneralDB> jocSeara) {
        this.jocSeara = jocSeara;
    }

    public DefaultListModel<JocGeneralDB> getJocuri() {
        return jocuri;
    }

    public void setJocuri(DefaultListModel<JocGeneralDB> jocuri) {
        this.jocuri = jocuri;
    }

    public DefaultListModel<ActivitateGeneralaDB> getActivitati() {
        return activitati;
    }

    public void setActivitati(DefaultListModel<ActivitateGeneralaDB> activitati) {
        this.activitati = activitati;
    }

    public Date getDate() {
        return date;
    }    
}
