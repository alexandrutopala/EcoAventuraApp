package adapters;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import com.connection.simpleclient.ConnectionController;
import com.connection.simpleclient.Controller;
import com.connection.simpleclient.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import receiver.DataListener;

/**
 * Created by Alexandru on 04.02.2017.
 */
public class SetariAdapter extends BaseAdapter{
    private final static int ITEM_AUTOCONECTARE = 1;
    private final static int ITEM_CONECTARE = 2;
    private final static int ITEM_RETEA = 3;
    private final static int ITEM_SERVER = 4;
    private final static int ITEM_REFRESH = 5;
    private Integer [] menu = new Integer[] {3, 4, 1, 2, 5};
    final private Context mContext;
    private Button butConectare;

    public SetariAdapter (final Context mContext) {
        this.mContext = mContext;
        Controller.getInstance().setSetariAdapter(this);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace(); // not all Android versions will print the stack trace automatically

                String message = getErrorMessage(e);
                String subject = "LOG: " +  new SimpleDateFormat("dd/MM/yyy - HH:mm:ss").format(Calendar.getInstance().getTime());

                Controller.getInstance().saveCrushLog(subject, message, mContext);
                Log.i("crush", "Crush report saved");

                Controller.getInstance().saveCrushRegister(
                        Controller.getInstance().getCrushRegister(mContext),
                        mContext
                );

                Log.i("crush", "Crush register saved");
            }
        });
    }

    @Override
    public int getCount() {
        return menu.length;
    }

    @Override
    public Object getItem(int i) {
        return menu[i];
    }

    @Override
    public long getItemId(int i) {
        return menu[i];
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = null;
        switch (menu[i]) {
            case ITEM_AUTOCONECTARE : {
                v = View.inflate(mContext, R.layout.item_autoconectare, null);
                Switch s = (Switch) v.findViewById(R.id.switch2);
                s.setChecked(Controller.getInstance().deserializeAutoconectareButton());

                s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        Controller.getInstance().serializeAutoconectareButton(b);
                       /* if (butConectare != null) {
                            butConectare.setEnabled(b);
                        }*/
                        ConnectionController.getInstance().setAutoconectare(b);
                        ConnectionController.getInstance().connectToServer();
                        notifyDataSetChanged();
                    }
                });
                break;
            }
            case ITEM_CONECTARE : {
                v = View.inflate(mContext, R.layout.item_conectare, null);
                final TextView text = (TextView) v.findViewById(R.id.textView52);
                text.setText(ConnectionController.getInstance().isDisconnected() ? "Conecteaza-te" : "Deconecteaza-te");
                v.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (ConnectionController.getInstance().isDisconnected()) {
                                    ConnectionController.getInstance().connect();
                                    ConnectionController.getInstance().connectToServer();
                                } else {
                                    ConnectionController.getInstance().disconnect();
                                }
                                text.setText(ConnectionController.getInstance().isDisconnected() ? "Conecteaza-te" : "Deconecteaza-te");
                            }
                        }
                );
                break;
            }
            case ITEM_RETEA : {
                v = View.inflate(mContext, R.layout.item_retea, null);
                TextView retea = (TextView) v.findViewById(R.id.textView43);
                TextView ip = (TextView) v.findViewById(R.id.textView45);

                ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (networkInfo.isConnected()) {
                    final WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                    if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                        retea.setText(connectionInfo.getSSID());
                        ip.setText(Formatter.formatIpAddress(connectionInfo.getIpAddress()));

                        break;
                    }
                }

                retea.setText("conexiune inexistenta");
                ip.setText("conexiune inexistenta");
                break;
            }
            case ITEM_SERVER : {
                v = View.inflate(mContext, R.layout.item_server, null);
                final Object [] socket = Controller.getInstance().getSocket();
                final Button bulina = (Button) v.findViewById(R.id.button14);
                final TextView status = (TextView) v.findViewById(R.id.textView47);
                final EditText adr = (EditText) v.findViewById(R.id.editText11);
                final EditText port = (EditText) v.findViewById(R.id.editText12);
                final Button modifica = (Button) v.findViewById(R.id.button15);
                final Button conectare = (Button) v.findViewById(R.id.button16);

                if (Controller.getInstance().isOffline()) {
                    bulina.setBackgroundColor(Color.LTGRAY);
                    status.setText("offline");
                } else {
                    bulina.setBackgroundColor(Color.GREEN);
                    status.setText("online");
                }

                adr.setText((String) socket[0]);
                if (socket[1] instanceof Integer) {
                    port.setText("" + socket[1]);
                } else {
                    port.setText((String) socket[1]);
                }


                modifica.setText("Salveaza");

                 modifica.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Controller.getInstance().saveSocket(adr.getText().toString(), Integer.parseInt(port.getText().toString()));
                                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                } catch (Exception e) {
                                    adr.setText((String) socket[0]);
                                    port.setText((String) socket[1]);
                                }
                            }
                        }
                );

                conectare.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ConnectionController.getInstance().connectToServerManually();
                            }
                        }
                );

                butConectare = conectare;
                conectare.setEnabled(!ConnectionController.getInstance().isAutoconectare());
                break;
            }
            case ITEM_REFRESH : {
                v = View.inflate(mContext, R.layout.item_refresh, null);
                TextView refresh = (TextView) v.findViewById(R.id.textView53);

                v.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                notifyDataSetChanged();
                            }
                        }
                );
                break;
            }
        }

        final View aux = v;

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN : {
                        view.setBackgroundColor(Color.LTGRAY);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        view.setBackgroundColor(Color.WHITE);
                        aux.performClick();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        view.setBackgroundColor(Color.WHITE);
                        break;
                    }
                }
                return true;
            }
        });

        return v;
    }

    public String getErrorMessage (Throwable e) {
        StackTraceElement[] stackTrackElementArray = e.getStackTrace();

        PackageManager manager = mContext.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo (mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
        }
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model;

        String crashLog;
        crashLog = ("Android version: " +  Build.VERSION.SDK_INT + "\n" +
                "Device: " + model + "\n" +
                "App version: " + (info == null ? "(null)" : info.versionCode) + "\n\n");

        crashLog += e.toString() + "\n\n";
        crashLog += "--------- Stack trace ---------\n\n";
        for (int i = 0; i < stackTrackElementArray.length; i++) {
            crashLog += "    " + stackTrackElementArray[i].toString() + "\n";
        }
        crashLog += "-------------------------------\n\n";

        return crashLog;
    }

}
