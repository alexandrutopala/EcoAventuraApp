package db.retriev;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import temporizator.Event;

public class DataRetriever extends AsyncTask<Void, Void, List<DataModel>> {
    /*
        instanta care va filtra rezultatele, pastrandu-le numai pe cele care au aceleasi valori in campuri
        daca "filter" are o valoare a unui camp nula, inseamna ca acel camp nu va fi luat in calcul la diferentiere
    */
    private final DataModel filter = new DataModel() ;

    private Event startEvent;
    private Event doneEvent;
    private List<DataModel> lastResult;

    public DataRetriever (){
        filter.setModel(null);
        filter.setCode(null);
        filter.setTime(null);
        filter.setId(null);
    }

    public DataRetriever (DataModel m) {
        filter.setCode(m.getCode());
        filter.setId(m.getId());
        filter.setTime(m.getTime());
        filter.setModel(m.getModel());
    }

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

    @Nullable
    @Override
    protected List<DataModel> doInBackground(Void... params) {
        /**
         * Getting JSON Object from Web Using okHttp
         */
        JSONObject jsonObject = JSONParser.getDataFromWeb(JSONParser.USERS_URL);
        List<DataModel> models = new ArrayList<DataModel>();
        try {
            /**
             * Check Whether Its NULL???
             */
            if (jsonObject != null) {
                /**
                 * Check Length...
                 */
                if(jsonObject.length() > 0) {
                    /**
                     * Getting Array named "contacts" From MAIN Json Object
                     */
                    JSONArray array = jsonObject.getJSONArray(Key.KEY_SHEET);

                    /**
                     * Check Length of Array...
                     */

                    int lenArray = array.length();
                    if(lenArray > 0) {
                        for(int jIndex = 0 ; jIndex < lenArray; jIndex++) {

                            /**
                             * Creating Every time New Object
                             * and
                             * Adding into List
                             */
                            DataModel model = new DataModel();

                            /**
                             * Getting Inner Object from contacts array...
                             * and
                             * From that We will get Name of that Contact
                             *
                             */
                            JSONObject innerObject = array.getJSONObject(jIndex);
                            // get the fields using Key's class identifiers
                            model.setId(innerObject.getString(Key.KEY_FIELD_ID));
                            model.setTime(innerObject.getString(Key.KEY_FIELD_TIME));
                            model.setModel(innerObject.getString(Key.KEY_FIELD_MODEL));
                            model.setCode(innerObject.getString(Key.KEY_FIELD_CODE));

                            if (areDataModelsEqual(filter, model)) {
                                models.add(model);
                            }

                        }
                    }
                }
            }
            return models;
        } catch (JSONException je) {
            Log.i(JSONParser.TAG, "" + je.getLocalizedMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<DataModel> models) {
        lastResult = models;
        if (doneEvent != null) doneEvent.doAction(models);
    }

    public List<DataModel> getLastResult () {
        return lastResult;
    }

    public boolean areDataModelsEqual(@Nullable  DataModel m1, DataModel m2) {
        return ((m1.getId() == null ? true : m1.getId().equals(m2.getId())) &&
                (m1.getTime() == null ? true : m1.getTime().equals(m2.getTime())) &&
                (m1.getCode() == null ? true : m1.getCode().equals(m2.getCode())) &&
                (m1.getModel() == null ? true : m1.getModel().equals(m2.getModel())));
    }
}
