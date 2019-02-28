package adapters;

import android.widget.BaseAdapter;

import com.connection.simpleclient.Activitate;

/**
 * Created by Alexandru on 09.03.2017.
 */

public abstract class MyBaseAdapter extends BaseAdapter {
    public abstract boolean removeElement (Activitate a);
}
