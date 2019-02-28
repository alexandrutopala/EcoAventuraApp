/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import dto.ActivitateDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexandru
 */
public class FakeActivitateDB extends ActivitateDB{
    String data;
    List<ActivitateDB> activitati = new ArrayList<>();
    
    public FakeActivitateDB(String data) {
        this.data = data;
    }
    
    public void addActivitate (ActivitateDB a) {
        activitati.add(a);
    }
    
    public void removeActivitate (ActivitateDB a) {
        activitati.remove(a);
    }
    
    public List<ActivitateDB> getActivitati () {
        return (List<ActivitateDB>) activitati;
    }
    
    @Override
    public String toString () {
        return "  " + data;
    }
}
