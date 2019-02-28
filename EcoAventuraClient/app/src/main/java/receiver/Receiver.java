package receiver;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alexandru on 8/29/2016.
 */
public class Receiver extends DataReceiver implements Serializable {
    private ArrayList<DataListener> listeners;

    public Receiver() {
        listeners = new ArrayList<>();
    }

    @Override
    public void addListener(DataListener dataListener) {
        if (!listeners.contains(dataListener)) {
            listeners.add(dataListener);
        }
    }

    @Override
    public void removeListener(DataListener dataListener) {
        listeners.remove(dataListener);
    }

    @Override
    public void notifyListeners(Object... o) {
        for (DataListener dl : listeners){
            dl.update(o);
        }
    }

    public ArrayList<DataListener> getListeners() {
        return listeners;
    }

    public void setListeners(ArrayList<DataListener> listeners) {
        if (listeners != null) this.listeners = listeners;
    }

}
