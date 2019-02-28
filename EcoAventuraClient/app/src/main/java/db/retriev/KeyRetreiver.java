package db.retriev;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import temporizator.Event;

/**
 * Created by Marius on 20-Apr-17.
 */

public class KeyRetreiver extends AsyncTask<Void, Void, List<String>> {
    private Event startEvent;
    private Event doneEvent;
    private List<String> lastResult;

    public void setStartEvent(Event startEvent) {
        this.startEvent = startEvent;
    }

    public void setDoneEvent(Event doneEvent) {
        this.doneEvent = doneEvent;
    }

    @Override
    protected void onPreExecute() {
        if (startEvent != null) startEvent.doAction();
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        List<String> keys = new ArrayList<>();

        JSONObject json = JSONParser.getDataFromWeb(JSONParser.KEYS_URL);

        try {
            if (json != null) {
                JSONArray array = json.getJSONArray(Key.KEY_KEY_SHEET);

                for (int i = 0; i < array.length(); ++i) {
                    JSONObject j = array.getJSONObject(i);
                    keys.add(j.getString(Key.KEY_KEY));
                }
            }
        } catch (Exception e) {
            return null;
        }

        return keys;
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        lastResult = strings;
        if (doneEvent != null) doneEvent.doAction(strings);
    }

    public List<String> getLastResult () {
        return lastResult;
    }
}
