/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package observer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Alexandru
 */
public class Receiver implements DataReceiver, Serializable {
    private ArrayList<DataListener> listeners;
    
    public Receiver () {
        listeners = new ArrayList<>();
    }
    
    @Override
    public void addListener(DataListener dataListener) {
        listeners.add(dataListener);
    }

    @Override
    public void removeListener(DataListener dataListener) {
        listeners.remove(dataListener);
    }

    @Override
    public void notifyListeners(Object... objs) {
        for (DataListener dl : listeners) {
            dl.update(objs);
        }
    }
    
}
