package receiver;

import java.io.Serializable;

/**
 * Created by Alexandru on 8/29/2016.
 */
public abstract class DataReceiver implements Serializable {
    public abstract void addListener(DataListener dataListener);
    public abstract void removeListener(DataListener dataListener);
    public abstract void notifyListeners(Object... o);
}
