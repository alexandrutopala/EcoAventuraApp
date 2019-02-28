package db.insert;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import temporizator.Event;

public class SendRequest extends AsyncTask<String, Void, String> {
    private final static String SHEET_ID = "1xia_kwITpRr-0OAc6hwchxaQqlKGbz8DjRrmfrUze8U";
    private final static String SCRIPT_ID = "https://script.google.com/macros/s/AKfycbwKWUwRMGjX9aFQLzTT8M1atrcgTRa0P6wsK5usnCiOSrJC0JU/exec";
    public final static int ELEMENTS_COUNT = 4;
    private Context mContext;
    private Event doneEvent;
    private Event startEvent;
    private ConnectivityManager conManager;

    public SendRequest (ConnectivityManager conManager) {
        this.conManager = conManager;
    }

    public SendRequest (ConnectivityManager conManager, Event doneEvent) {
        this.doneEvent = doneEvent;
        this.conManager = conManager;
    }

    public SendRequest (ConnectivityManager conManager, Event startEvent, Event doneEvent) {
        this.startEvent = startEvent;
        this.doneEvent = doneEvent;
        this.conManager = conManager;
    }

    protected void onPreExecute(){
        if (startEvent != null) startEvent.doAction();
    }

    protected String doInBackground(String... arg0) {

        try{
            // trbuie primit exact setul de date corespunzator
            if (arg0.length != ELEMENTS_COUNT) return "Numar necorespunzator de parametri";
            if (!checkInternetConnection()) return "Nu exista conexiune la internet";

            URL url = new URL(SCRIPT_ID);
            // https://script.google.com/macros/s/AKfycbyuAu6jWNYMiWt9X5yp63-hypxQPlg5JS8NimN6GEGmdKZcIFh0/exec
            JSONObject postDataParams = new JSONObject();

            //int i;
            //for(i=1;i<=70;i++)


            //    String usn = Integer.toString(i);


            postDataParams.put("id", arg0[0]);
            postDataParams.put("time", arg0[1]);
            postDataParams.put("model", arg0[2]);
            postDataParams.put("code", arg0[3]);
            postDataParams.put("SHEET_ID", SHEET_ID);


            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();

            }
            else {
                return new String("false : "+responseCode);
            }
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (doneEvent != null) doneEvent.doAction(result);
    }


    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public boolean checkInternetConnection () {
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        return netInfo != null;
    }

}