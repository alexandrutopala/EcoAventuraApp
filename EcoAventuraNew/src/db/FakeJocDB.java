/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import dto.JocDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexandru
 */
public class FakeJocDB extends JocDB{
    String data;
    List<JocDB> jocuri = new ArrayList<>();
    
    public FakeJocDB (String data) {
        this.data = data;
    }
    
    public void addJoc (JocDB j) {
        jocuri.add(j);
    }
    
    public void removeJoc (JocDB j) {
        jocuri.remove(j);
    }
    
    public List<JocDB> getJocuri () {
        return jocuri;
    }
    
    @Override
    public String toString () {
        return "  " + data;
    }
}
