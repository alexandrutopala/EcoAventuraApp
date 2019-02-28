/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.util.List;

/**
 *
 * @author Alexandru
 */
public class EchipaDataDB implements java.io.Serializable{
    private int id;
    private List<ActivitateDB> activitati;
    private List<JocDB> jocuri;
    private List<MembruEchipaDB> membri;

    public EchipaDataDB(EchipaDB echipa) {
        this.id = echipa.getIdEchipa();
    }
    
    public int getId () {
        return this.id;
    }
    
    public List<ActivitateDB> getActivitati() {
        return activitati;
    }

    public void setActivitati(List<ActivitateDB> activitati) {
        this.activitati = activitati;
    }

    public List<JocDB> getJocuri() {
        return jocuri;
    }

    public void setJocuri(List<JocDB> jocuri) {
        this.jocuri = jocuri;
    }

    public List<MembruEchipaDB> getMembri() {
        return membri;
    }

    public void setMembri(List<MembruEchipaDB> membri) {
        this.membri = membri;
    }
    
    
}
