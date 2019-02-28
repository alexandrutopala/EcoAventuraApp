package gui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.connection.simpleclient.ClassIDs;
import com.connection.simpleclient.ConnectionController;
import com.connection.simpleclient.ConnectionKeeper;
import com.connection.simpleclient.Controller;

import floatingWindow.FloatingWindow;
import pl.droidsonroids.gif.GifImageView;
import receiver.DataListener;

import com.connection.simpleclient.MyApplication;
import com.connection.simpleclient.R;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import receiver.Receiver;

import java.io.IOException;

import dto.Message;
import dto.UserDTO;

public class Login extends AppCompatActivity implements DataListener {
    public static boolean EXIT_APP = false;
    public final static int CONNECT_BUTTON = 1;
    public final static int LOG_BUTTON = 2;
    public final static int NO_BUTTON = -1;
    private Receiver receiver;
    private Button con;
    private Button log;
    private Button exit;
    private EditText username;
    private EditText parola;
    private SwitchCompat mySwitch;
    private RelativeLayout layout;
    public static int pushedButton = NO_BUTTON;
    private boolean isWaitingForUser = false;
    private ConnectionKeeper keeper;
    private String confirmPass = "";
    private GifImageView gif;
    //private EditText notificationDisplay;

    // help
    private ShowcaseView showcase;
    private Target usernameTarget;
    private Target parolaTarget;
    private Target conTarget;
    private Target logTarget;
    private static boolean madeTutorial = false;

    private PopupWindow popup;
    private LayoutInflater inflater;

    public void createPopupWindow (){
        inflater = (LayoutInflater) MyApplication.getMyApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) inflater.inflate(R.layout.popup_window_layout, null);

        popup = new PopupWindow(container ,
                400,
                400,
                true);
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, 0, 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Controller.getInstance().setCurrentActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Bun venit!");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //debug
       //con.getText();
        layout = (RelativeLayout) findViewById(R.id.loginLayout);

        con = (Button) findViewById(R.id.button2);
        log = (Button) findViewById(R.id.button3);
        username = (EditText) findViewById(R.id.editText3);
        parola = (EditText) findViewById(R.id.editText4);
        mySwitch = (SwitchCompat) findViewById(R.id.switch1);
        exit = (Button) findViewById(R.id.button11);
        //gif = (GifImageView) findViewById(R.id.gif3);

        //gif.setVisibility(View.GONE);

        try {
        //notificationDisplay = (EditText) findViewById(R.id.editText5);
            if (!Controller.getInstance().isOffline()) {

                receiver = Controller.getInstance().getReceiver();

                receiver.addListener(this);

                keeper = new ConnectionKeeper();
                keeper.execute(Controller.getInstance().getClient().getOut());
            }

            Object [] o = Controller.getInstance().getLastUser();
            String username = (String) o[0];
            String parola = (String) o[1];
            boolean remember = (boolean) o[2];

            if (remember) {
                this.username.setText(username);
                this.parola.setText(parola);
                mySwitch.setChecked(remember);
            }

        } catch (Exception e) {
        }
        //events

        con.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!Controller.getInstance().isOffline()) {
                            try {
                                pushedButton = CONNECT_BUTTON;
                                waitingResponse(true);
                                String user = username.getText().toString().toLowerCase();
                                String pass = new String(parola.getText().toString());
                                if (mySwitch.isChecked()) Controller.getInstance().saveLastUser(user, pass, mySwitch.isChecked());
                                //Controller.getInstance().conectare(user, pass); //mutat in ConnectionController
                                ConnectionController.getInstance().connectUser(user, pass);
                            } catch (Exception e) {
                                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            // ce se intampla cand e offline
                            String user = username.getText().toString().toLowerCase();
                            String pass = new String(parola.getText().toString()).toLowerCase();
                            if (mySwitch.isChecked()) Controller.getInstance().saveLastUser(user, pass, mySwitch.isChecked());
                            UserDTO u = Controller.getInstance().conecteazaUserOffline(user, pass);
                            if (u != null) {
                                Toast.makeText(Login.this, "Bun venit!", Toast.LENGTH_SHORT);
                                //nextActivity(u); // activitatea este deja chemata din metoda Controller.getInstance().setUser(...)
                            } else {
                                Toast.makeText(Login.this, "Parola gresita sau user nelogat online", Toast.LENGTH_LONG).show();

                            }

                        }
                        waitingResponse(false);
                    }
                }
        );

        log.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Controller.getInstance().isOffline()) {
                            Toast.makeText(Login.this ,"Nu se poate crea un cont nou in modul offline", Toast.LENGTH_LONG).show();
                            return;
                        }
                        pushedButton = LOG_BUTTON;
                        waitingResponse(true);
                        final String user = username.getText().toString().toLowerCase();
                        final String pass = new String(parola.getText().toString());


                        final EditText input = new EditText(Login.this);
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        new AlertDialog.Builder(Login.this)
                                .setTitle("Verificare")
                                .setMessage("Confirma parola")
                                .setView(input)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    confirmPass = new String(input.getText().toString());
                                    if (!confirmPass.equals(pass)){
                                        new AlertDialog.Builder(Login.this)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setTitle("Verificare")
                                                .setMessage("Parolele nu coincid")
                                                .setPositiveButton("OK", null)
                                                .show();
                                        return;
                                    } else {
                                        try {
                                            Controller.getInstance().inregistrare(user, pass);
                                        } catch (IOException e) {
                                            Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            waitingResponse(false);
                                        }
                                    }
                                    }
                                })
                                .setNegativeButton("Inchide", null)
                                .show();

                        waitingResponse(false);
                    }
                }
        );

        exit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Controller.getInstance().getClient().stop();
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                        EXIT_APP = true;
                        //finish();
                        //System.exit(0);
                        finishAffinity();
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item2 : { // ajutor
                makeTutorial();
                break;
            }
            case R.id.setari : {
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;
            }

            case R.id.raporteaza1 : {
                Intent intent = new Intent(this, Report.class);
                startActivity(intent);
                break;
            }
            case android.R.id.home : {
                finish();
                break;
            }
            default: super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void update(Object... o) {
        try {
            Log.i("update", "update method is up");

            if (o[0] instanceof Message /*&& pushedButton != NO_BUTTON*/) {
                Message m = (Message) o[0];
                //notificationDisplay.setText("Message received!");
                waitingResponse(false);
                switch (m) {
                    case OK_USER:
                        if (pushedButton == CONNECT_BUTTON) {
                            //Controller.getInstance().getClient().sendData(Message.CONNECT_USER); mutate in ConnectionController
                            //isWaitingForUser = true;
                            waitingResponse(true);
                        } else {
                            //displayer("User logat!");
                        }
                        break;
                    case WRONG_PASSWORD:
                        displayer("Parola gresita sau user inexistent");
                        parola.setText("");
                        break;
                    case EXISTENT_USER:
                        displayer("Userul exista deja");
                        username.setText("");
                        parola.setText("");
                        break;
                    case UNCREATED_USER:
                        displayer("Userul n-a fost creat de administrator inca");
                        username.setText("");
                        parola.setText("");
                        break;

                    default: //displayer("Default reached");
                }
                pushedButton = NO_BUTTON;
            } else if (isWaitingForUser && o[0] instanceof UserDTO) {  // mutate in ConnectionController
                isWaitingForUser = false;
                UserDTO user = (UserDTO) o[0];
                /*Controller.getInstance().setUser(user);
                displayer("User conectat!");
                nextActivity(user);
                //the user is connected, can switch to next activity
                */
                waitingResponse(false);
            }
        } catch (Exception e) {
            displayer(e.getMessage());
            waitingResponse(false);
            e.printStackTrace();
        }
    }

    public void nextActivity (final UserDTO user) {
        if (Controller.getInstance().getCurrentActivity().getClass() == MainContent.class) return;
        //Controller.getInstance().showLoadingDialog(getApplicationContext());
        Controller.getInstance().showLoadingScreenDelayed(0);
        //new Handler().postDelayed(new Runnable() {
        //    @Override
        //    public void run() {
        con.setEnabled(false);

                Intent i = new Intent(Login.this, MainContent.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                i.putExtras(bundle);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
        //    }
        //}, 500);
        con.setEnabled(true);

    }

    protected void onDestroy() {
        super.onDestroy();
        try {
            if (!Controller.getInstance().isOffline()) {
                if (keeper != null) keeper.stop();
                Controller.getInstance().getClient().stop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayer (String message) {
        Controller.getInstance().display(message);
    }

    public void waitingResponse (boolean b) {
        con.setEnabled(!b);
        log.setEnabled(!b);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Controller.getInstance().setCurrentActivity(this);

        if (Controller.getInstance().getReceiver() != null) {
            if (!Controller.getInstance().getReceiver().getListeners().contains(this)) {
                Controller.getInstance().getReceiver().addListener(this);
            }
        }

        if (FloatingWindow.isLoadingScreenVisible()) {
            Controller.getInstance().hideLoadingScreenDelayed(700);
        }

        if (!madeTutorial) {
            boolean madeTutorialBefore = Controller.getInstance().wasActivityUsedBefore(ClassIDs.LOGIN, this);
            if (!madeTutorialBefore) {
                Controller.getInstance().setFirstUseActivity(ClassIDs.LOGIN, true, this);
                makeTutorial();
            }
            madeTutorial = true;
        }
    }

    private int step;
    private final static int TOTAL_STEPS = 6;

    private void makeTutorial () {
        step = 0;

        usernameTarget = new ViewTarget(R.id.editText3, this);
        conTarget = new ViewTarget(R.id.button2, this);
        logTarget = new ViewTarget(R.id.button3, this);

        showcase = new ShowcaseView.Builder(this)
                .setContentTitle("Meniul de Conectare/Logare")
                .setContentText("Aceasta este fereastra din care poate fi creat sau accesat un cont.")
                .hideOnTouchOutside()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickPerformed(view);
                    }
                })
                .setStyle(R.style.TransparentStyle)
                .setTarget(Target.NONE)
                .build();
        showcase.setBlocksTouches(true);
        showcase.setButtonText("Urmatorul");
        showcase.show();

        step++;
    }

    private void clickPerformed (View view) {
        switch (step) {
            case 0 : break;
            case 1 :
                showcase.setShowcase(usernameTarget, true);
                showcase.setContentTitle("Nume utilizator");
                showcase.setContentText("Coincide cu numele de animator asociat. Solicita coordonatorului acest nume.");
                break;
            case 2 :
                showcase.setShowcase(conTarget, true);
                showcase.setContentTitle("Conectare la cont");
                showcase.setContentText("Acceseaza contul tau.");
                break;
            case 3 :
                showcase.setContentText("Atentie! Iti poti accesa contul in modul Offline numai daca l-ai accesat macar o data in modul Online inainte.");
                break;
            case 4 :
                showcase.setShowcase(logTarget, true);
                showcase.setContentTitle("Creaza un cont");
                showcase.setContentText("Inregistreaza-ti contul in baza de date a aplicatiei. Asigura-te ca ai fost inregistrat ca animator intai.");
                break;
            case 5 :
                showcase.setContentText("Ai grija : Un cont se poate crea numai in modul Online.");
                break;
            case TOTAL_STEPS :
                showcase.setShowcase(Target.NONE, true);
                showcase.setContentTitle("Suntem aproape gata");
                showcase.setContentText("Creaza-ti un cont pentru a putea trece la pasul urmator.");
                break;
            default:
                showcase.hide();
        }
        step++;
    }

}
