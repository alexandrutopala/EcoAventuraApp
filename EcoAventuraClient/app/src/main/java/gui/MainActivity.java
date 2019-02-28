package gui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.connection.simpleclient.ClassIDs;
import com.connection.simpleclient.ConnectionController;
import com.connection.simpleclient.Controller;
import com.connection.simpleclient.MyApplication;
import com.connection.simpleclient.OnDataReceived;
import com.connection.simpleclient.R;

import dto.Message;
import pl.droidsonroids.gif.GifImageView;
import receiver.Receiver;
import com.connection.simpleclient.TCPClient;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static boolean EXIT_APP = false;

    private EditText ipText;
    private EditText portText;
    private Button connect;
    private Button goOffline;
    private GifImageView gif;

    private TCPClient client;
    private Receiver receiver;

    private boolean isConnected = false;
    private boolean isMakingTutorial = false;
    private boolean switchToNextActivity = false;

    // help
    private ShowcaseView showcase;
    private Target ipTarget, portTarget, connectTarget, goOfflineTarget;
    private int step = 0;
    private final static int TOTAL_STEPS = 5;
    private static boolean madeTutorial = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Controller.getInstance().setCurrentActivity(this);
        ipText = (EditText) findViewById(R.id.editText);
        portText = (EditText) findViewById(R.id.editText2);
        connect = (Button) findViewById(R.id.button);
        goOffline = (Button) findViewById(R.id.button5);
        gif= (GifImageView) findViewById(R.id.gif);

        Controller.getInstance().setApplicationContext(MyApplication.getMyApplicationContext());
        Controller.getInstance().setMainActivity(this);
        gif.setVisibility(View.INVISIBLE);


        try {
            Object [] objs = Controller.getInstance().getSocket();
            String ip = (String) objs[0];
            int port = (int) objs[1];
            ipText.setText(ip);
            portText.setText(port+"");
        } catch (Exception e) {}

        receiver = new Receiver();

        View.OnKeyListener keyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (gif.getVisibility() == View.INVISIBLE) {
                    connect.setEnabled(true);
                    connect.setBackgroundColor(Color.parseColor("#d24304"));
                }
                return false;
            }
        };

        ipText.setOnKeyListener(keyListener);
        portText.setOnKeyListener(keyListener);

        Controller.getInstance().setApplicationContext(MyApplication.getMyApplicationContext());

        connect.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            connect.setEnabled(false);
                            connect.setBackgroundColor(Color.parseColor("#d3d3d3"));
                            gif.setVisibility(View.VISIBLE);
                            String ip = ipText.getText().toString();
                            String port = portText.getText().toString();

                            try {
                                Controller.getInstance().saveSocket(ip, Integer.parseInt(port));
                            } catch (Exception e) {}
                            new ConnectTask().execute(ip, port, "");

                            //nextActivity();

                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //connect.setEnabled(true);
                        //Controller.getInstance().display("Se incearca conexiunea...");
                    }
                }
        );

        goOffline.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Controller.getInstance().setOffline(true);
                        try {
                            nextActivity();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }


    public class ConnectTask extends AsyncTask<String, Object, Void> {
        private boolean isShowingNepotrivireaCheilorDialog = false;

        protected void onPreExecute () {

        }

        @Override
        protected Void doInBackground (String... data){
            try {
                if (ConnectionController.getInstance().isExistsConnectTask()){
                    return null;
                }

                ConnectionController.getInstance().setExistsConnectTask(true);
                Controller.getInstance().setReceiver(receiver);

                client = new TCPClient(
                        new OnDataReceived() {
                            @Override
                            public void dataReceived(Object o) {
                                publishProgress(o);
                            }
                        },
                        data[0],
                        Integer.parseInt(data[1])
                );

                Controller.getInstance().setClient(client);

                //if (Controller.getInstance().isOfflineMode()) return null;
                int connCode = client.connect(MyApplication.getMyApplicationContext());

                if (connCode == TCPClient.CONEXIUNE_STABILTA) {
                    Log.i("Client thread", "Client conectat");
                    Controller.getInstance().setOffline(false);
                    isConnected = true;
                    receiver.notifyListeners(Message.CONFIRM_CONNECTION);

                    if (data.length > 2) {
                        publishProgress(null, true);
                    }

                    client.run();
                } else {
                    //throw new Exception();
                    publishProgress(connCode, false);
                }
            } catch (Exception e){
                Log.e("Client thread", "O eroare a aparut la conectarea clientului");
                publishProgress(null, false);
                Controller.getInstance().setOffline(true);
            }

            return null;
        }

        protected void onProgressUpdate(Object... objs){
            super.onProgressUpdate(objs);

            if (objs.length > 1 && objs[1] instanceof  Boolean ){
                if (((Boolean) objs[1])) {
                    try {
                        nextActivity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //Toast.makeText(MainActivity.this, "Conexiune esuata!", Toast.LENGTH_SHORT).show();
                    //connect.setEnabled(true);
                    gif.setVisibility(View.INVISIBLE);
                    if (!(objs[0] instanceof Integer) || objs[0] == null) return;
                    int connCode = (Integer) objs[0];


                    if (Controller.getInstance().getCurrentActivity() != null) {
                        if (Controller.getInstance().getCurrentActivity().getClass() == MainActivity.class) {
                            if (connCode == TCPClient.CONEXIUNE_NECONFIRMATA) {
                                Controller.getInstance().display("Conexiune esuata");
                            } else if (connCode == TCPClient.NEPOTRIVIREA_CHEILOR &&
                                    !isShowingNepotrivireaCheilorDialog) {

                                isShowingNepotrivireaCheilorDialog = true;
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Gazda nepotrivita")
                                        .setMessage("Dispozitivul actual nu este inregistrat in baza Serverului la care doresti sa te " +
                                                "conectezi. Doresti sa setezi acest Server ca gazda principala? \n" +
                                                "Atentie: resetarea gazdei va duce la pierderea tuturor datelor aplicatiei!")
                                        .setPositiveButton("RESETEAZA", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, null, "Se sterg datele...");
                                                progressDialog.setCanceledOnTouchOutside(false);
                                                progressDialog.setCancelable(false);
                                                Controller.getInstance().deleteApplicationFiles(MyApplication.getMyApplicationContext());
                                                progressDialog.dismiss();
                                                new AlertDialog.Builder(MainActivity.this)
                                                        .setTitle("Succes!")
                                                        .setMessage("Aplicatia a fost resetata. Conecteaza-te la noua gazda.")
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                connect.setEnabled(true);
                                                                gif.setVisibility(View.INVISIBLE);
                                                            }
                                                        })
                                                        .show();
                                            }
                                        })
                                        .setNegativeButton("ANULEAZA", null)
                                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                isShowingNepotrivireaCheilorDialog = false;
                                            }
                                        })
                                        .show();
                            }
                        }
                    }

                }
            }

            receiver.notifyListeners(objs);
            Log.i("client", "data received");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (ConnectionController.getInstance().isExistsConnectTask()) {
                ConnectionController.getInstance().setExistsConnectTask(false);
            }
        }
    }

    public void startConnectTask (String ip, String port) {
        new ConnectTask().execute(ip, port);
    }

    public void nextActivity () throws IOException {
        if (isMakingTutorial) {
            switchToNextActivity = true;
            return;
        }
        Intent i = new Intent(MainActivity.this, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    protected void onResume() {
        super.onResume();

        if (Login.EXIT_APP) {
            EXIT_APP = true;
            //finish();
            System.exit(0);
        }

        //ipText.setText("");
        //portText.setText("");

        try {
            if (client != null) {
                client.stop();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        client = null;
        isConnected = false;
        //Toast.makeText(MainActivity.this, "Deconectat", Toast.LENGTH_SHORT).show();
        Controller.getInstance().setCurrentActivity(this);

        if (!madeTutorial) {
            boolean madeTutorialBefore = Controller.getInstance().wasActivityUsedBefore(ClassIDs.MAIN_ACTIVITY, this);
            if (!madeTutorialBefore) {
                Controller.getInstance().setFirstUseActivity(ClassIDs.MAIN_ACTIVITY, true, this);
                makeTutorial();
            }
            madeTutorial = true;
        }
    }

    @Override
    public void onPause () {
        super.onPause();
        connect.setEnabled(true);
        connect.setBackgroundColor(Color.parseColor("#d24304"));
        gif.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EXIT_APP = true;
        //finish();
        //System.exit(0);
        finishAndRemoveTask();

    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        try {
            if (client != null) {
                client.stop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.raporteaza : {
                Intent intent = new Intent(this, Report.class);
                startActivity(intent);
                break;
            }
            case R.id.item1 : { // ajutor
                makeTutorial();
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void makeTutorial () {
        isMakingTutorial = true;
        step = 1;

        ipTarget = new ViewTarget(R.id.editText, this);
        portTarget = new ViewTarget(R.id.editText2, this);
        connectTarget = new ViewTarget(R.id.button, this);
        goOfflineTarget = new ViewTarget(R.id.button5, this);

        showcase = new ShowcaseView.Builder(this)
                .setTarget(Target.NONE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onTargetClick(view);
                    }
                })
                .setContentTitle("Conectare")
                .hideOnTouchOutside()
                .setContentText("Cu ajutorul adresei furnizata de coordonator (IP + PORT) te poti conecta la Serverul EcoAventura.")
                .setStyle(R.style.TransparentStyle)
                .build();
        showcase.show();
        showcase.setBlocksTouches(true);
        showcase.setButtonText("Urmatorul");
    }

    private void onTargetClick (View view) {
        switch (step) {
            case 0 :
                break;
            case 1 :
                showcase.setShowcase(ipTarget, true);
                showcase.setContentTitle("Adresa IP");
                showcase.setContentText("Adresa la care se gaseste serverul in retea.");
                break;
            case 2 :
                showcase.setShowcase(portTarget, true);
                showcase.setContentTitle("Port");
                showcase.setContentText("ID-ul procesului la care se va conecta aplicatia.");
                break;
            case 3 :
                showcase.setShowcase(connectTarget, true);
                showcase.setContentTitle("Conectare Online");
                showcase.setContentText("Conecteaza-te direct la Server.");
                break;
            case 4 :
                showcase.setShowcase(goOfflineTarget, true);
                showcase.setContentTitle("Conectare Offline");
                showcase.setContentText("Intra in modul Offline al aplicatiei.");
                break;
            case TOTAL_STEPS :
                showcase.setShowcase(Target.NONE, true);
                showcase.setContentTitle("Suntem gata aici");
                showcase.setContentText("Introdu adresa furnizata si apasa pe butonul 'Connect' pentru a merge mai departe.");
                showcase.setButtonText("Gata");
                break;
            default:
                showcase.hide();
                isMakingTutorial = false;
                if (switchToNextActivity) {
                    try {
                        nextActivity();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

        }
        step++;
    }


}
