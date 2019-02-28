/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package observer;

/**
 *
 * @author Alexandru
 */
public interface DataReceiver {
    public void addListener (DataListener dataListener);
    public void removeListener (DataListener dataListener);
    public void notifyListeners(Object... objs);
}
