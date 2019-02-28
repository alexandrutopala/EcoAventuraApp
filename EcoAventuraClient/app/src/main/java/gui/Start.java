package gui;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.Settings;

import com.connection.simpleclient.Controller;
import com.connection.simpleclient.MyApplication;
import com.connection.simpleclient.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import db.DBController;
import db.insert.SendRequest;
import db.retriev.DataModel;
import db.retriev.DataRetriever;
import db.retriev.KeyRetreiver;
import floatingWindow.FloatingWindow;
import temporizator.Event;

public class Start extends AppCompatActivity {
    private AppCompatImageView image;
    private TextView motto;
    private TextView copyright;
    private Button start;
    private Button help;
    private final static String [] PERMISSIONS = new String [] {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.VIBRATE
    };
    private final static int PERMISSION_ALL = 1;
    private final static int CODE_LENGTH = 6;
    private boolean GRANTED = false;
    private final static int MAX_SDK_VERSION = Build.VERSION_CODES.M;

    private static boolean animationDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        try {
            if (!requestWindowFeature(Window.FEATURE_NO_TITLE)){
                setTitle("");
            }
        } catch (Exception e) {
            setTitle("EcoAventura");
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        image = (AppCompatImageView) findViewById(R.id.imageView);
        motto = (TextView) findViewById(R.id.textView39);
        copyright = (TextView) findViewById(R.id.textView40);
        start = (Button) findViewById(R.id.button9);
        help = (Button) findViewById(R.id.button19);

        if (!animationDisplayed) {
            motto.setVisibility(View.INVISIBLE);
            image.setVisibility(View.INVISIBLE);
            copyright.setVisibility(View.INVISIBLE);
            start.setVisibility(View.INVISIBLE);
            start.setEnabled(false);
            help.setVisibility(View.INVISIBLE);
        }


        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final Animation fadeIn2 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final Animation fadeIn3 = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        fadeIn2.setDuration(1500);

        fadeIn.setAnimationListener(
                new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        image.setVisibility(View.VISIBLE);
                        motto.startAnimation(fadeIn2);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                }
        );

        fadeIn2.setAnimationListener(
                new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {      }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        motto.setVisibility(View.VISIBLE);
                        fadeIn3.setDuration(500);
                        start.startAnimation(fadeIn3);
                        copyright.startAnimation(fadeIn3);
                        help.startAnimation(fadeIn3);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                }
        );

        fadeIn3.setAnimationListener(
                new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        start.setVisibility(View.VISIBLE);
                        copyright.setVisibility(View.VISIBLE);
                        help.setVisibility(View.VISIBLE);
                        animationDisplayed = true;
                        if (Build.VERSION.SDK_INT > MAX_SDK_VERSION) {
                            start.setEnabled(true);
                            start.setText("Versiune incompatibila");
                            return;
                        }
                        checkPermission();
                        sendCrushReports();
                        checkFloatingWindowPermission();
                        checkRegistrationCode();
                        //insertRow();
                        //DBController.getInstance().registerThisPhone(Start.this, "123456");
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                }
        );
        if (start.getVisibility() == View.INVISIBLE) {
            image.startAnimation(fadeIn); //Set animation to your ImageView
        }

        start.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT > MAX_SDK_VERSION) {
                            new AlertDialog.Builder(Start.this)
                                    .setCancelable(false)
                                    .setTitle("Aplicatie neactualizata")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setMessage("Aplicatia EcoAventura Mobile a fost creata pentru a rula pe versiunile Android " +
                                            "5 si Android 6. \nPentru actualizarea aplicatiei, consultati coordonatorii taberei." +
                                            "\n\nVa multumim!")
                                    .setPositiveButton("AM INTELES", null)
                                    .show();
                            return;
                        }

                        if (!GRANTED) {
                            new AlertDialog.Builder(Start.this)
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .setTitle("Permisiune refuzata")
                                    .setMessage("Aplicatia nu poate rula fara permisiunile cerute. Te rog sa restartezi aplicatia si sa-i acorzi " +
                                            "toate permisiunile necesare")
                                    .setPositiveButton("AM INTELES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            //finishAndRemoveTask();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                            //return;
                        }



                        Intent intent = new Intent(Start.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        );

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                try {
                    PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);


                    message += "Versiune aplicatie: " + info.versionName + "\n";
                    message += "Cod versiune: " + info.versionCode + "\n";
                    message += "Data compilarii: " + MyApplication.BUILD_DATE + "\n\n";

                    message += "Platforme suportate:\n";
                    for (int i = 0; i < MyApplication.PLATFORMS.length; ++i) {
                        message += ("  - " + MyApplication.PLATFORMS[i] + "\n");
                    }
                    message += "\n";

                    message += "Contact administrator aplicatie: \n" + MyApplication.EMAIL_CONTACT + "\n";


                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    message = "Datele aplicatiei nu au putut fi citite.\n\n";
                    message += "Contact administrator aplicatie: \n" + MyApplication.EMAIL_CONTACT + "\n";
                }

                new AlertDialog.Builder(Start.this)
                        .setTitle("Informatii aplicatie")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setMessage(message)
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

    }

    public void sendCrushReports () {
        HashMap<String, String> register = Controller.getInstance().getCrushRegister(this);
        ProgressDialog dialog = ProgressDialog.show(this, "Se trimite raportul aplicatiei", "");
        dialog.show();
        for (String subject : register.keySet()) {
            String message = register.get(subject);
            try {
                Controller.getInstance().getMailSender().sendMail(
                        subject,
                        message,
                        Controller.getInstance().getMailSender().getUserMailAdress(),
                        Controller.getInstance().getMailSender().getUserMailAdress(),
                        this,//getApplicationContext(),
                        "",
                        "Se trimite raportul aplicatiei...",
                        false
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dialog.dismiss();
    }

    public void createFloatingWindow () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!android.provider.Settings.canDrawOverlays(MyApplication.getMyApplicationContext())) {
                return;
            }
        }

        Intent intent = new Intent(this, FloatingWindow.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);
    }

    @Override
    public void onResume () {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;

        for (String permission : permissions) {
            // verificam daca au fost acordate permisiunile necesare (exceptie poate face numai SYSTEM_ALERT_WINDOW
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED && permission != Manifest.permission.SYSTEM_ALERT_WINDOW) {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Permisiune lipsa")
                        .setMessage("Permisiunea " + permission + " nu este aprobata. E posibil ca aplicatia sa aiba un comortament defectuos. Dar nu e panica, man! ")
                        .setPositiveButton("AM INTELES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //finishAndRemoveTask();
                            }
                        })
                        .setCancelable(false)
                        .show();
                //return;
            }
        }

        GRANTED = true;
    }

    public void checkPermission () {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            GRANTED = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    createFloatingWindow();
                }
            }, 500);
            return;
        } else {
            List<String> ungrantedPerms = new ArrayList<>();

            for (int i = 0; i < PERMISSIONS.length; ++i) {
                if (checkSelfPermission(PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED) {
                    ungrantedPerms.add(PERMISSIONS[i]);
                }
            }

            if (!ungrantedPerms.isEmpty()) {
                String [] ss = new String[ungrantedPerms.size()];
                int ind = 0;
                for (String s : ungrantedPerms) {
                    ss[ind++] = s;
                }
                requestPermissions(ss, PERMISSION_ALL);
            } else {
                GRANTED = true;

            }
        }
    }

    public void checkFloatingWindowPermission () {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    createFloatingWindow();
                }
            }, 500);
            return;
        }else if (Settings.canDrawOverlays(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    createFloatingWindow();
                }
            }, 500);
            return;
        }

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Cerere")
                .setMessage("Acordati aplicatiei permisiunea folosirii elementelor grafice suprapuse aplicatiei, in scopul unei experiente si mai placute?")
                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new AlertDialog.Builder(Start.this)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setTitle("Instructiuni")
                                .setMessage("In fereastra urmatoare care se va deschide, glisati comutatorul corespunzator aplicatiei EcoAventura, dupa care reporniti aplicatia.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        final String packageName = Start.this.getPackageName();
                                        final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName));
                                        Start.this.startActivity(intent);
                                        finish();
                                    }
                                })
                                .show();
                    }
                })
                .setNegativeButton("Nu, multumesc", null)
                .show();
    }


    private boolean registred = false;

    public void checkRegistrationCode () {
        // prima etapa, verificam daca exista deja un fisier in memoria telefonului cu codul de activare
        if (Controller.getInstance().existsRegistrationCodeFile(MyApplication.getMyApplicationContext())) {
            //exista totusi riscul ca aplcatia sa fie fost stearsa din lista de licentiate, trebuie verificat
            if (checkInternetConnection()) {
                Event doneEvent = new Event() {
                    @Override
                    public void doAction(Object... obj) {
                        if (obj[0] == null || !(obj[0] instanceof List)) return;
                        List<DataModel> models = (List<DataModel>) obj[0];
                        if (models.size() == 1) {
                            // asta inseamna ca a fost selectata o singura inregistrare, cea actuala, deci totul este bine aici
                            return;
                        }
                        // secventa defectuasa
                        //if (!Controller.getInstance().deleteRegistrationCode(getApplicationContext())) {
                        //    Controller.getInstance().saveRegistrationCode("", getApplicationContext());
                        //}

                    }
                };

                DataModel myDataModel = new DataModel(getDeviceId(), null, Build.MODEL, Controller.getInstance().getRegistrationCode(MyApplication.getMyApplicationContext()));
                DataRetriever retriever = new DataRetriever(myDataModel);
                retriever.setDoneEvent(doneEvent);
                retriever.execute();
            }
            start.setEnabled(true);
            return;
        }

        // a doua etapa, verificam daca exista conexiune la internet, pt a putea accesa baza de date
        if (!checkInternetConnection()) {
            showBeforeCloseDialog("Inregistrare aplicatie", "Inregistrarea aplicatieieste imposibila din cauza lipsei conexiunii la internet. Asigura-te ca esti conectat la internet si revino");
            return;
        }

        final ProgressDialog dialog = ProgressDialog.show(this, "Se verifica inregistrarea aplicatiei", "Verific inregistrarile anterioare");
        dialog.setCancelable(false);
        // pasul trei, dupa verificarea conexiunii la internet, verificam daca aplicatiaeste deja inregistrata in baza de date online
        verifyPastRegistration(dialog);

        //daca nici dupa acest pas nu s-a incheiat inregistrarea, inseamna de fapt ca aplicatia nu este inregistrata

    }

    public void verifyPastRegistration (final ProgressDialog dialog) {
        if (dialog == null) return;
        Event startEvent = new Event() {
            @Override
            public void doAction(Object... obj) {
                dialog.show();
            }
        };

        Event doneEvent = new Event() {
            @Override
            public void doAction(Object... obj) {
                if (obj[0] == null || !(obj[0] instanceof  List)) {
                    dialog.dismiss();
                    showBeforeCloseDialog("Ops, ceva nu a mers bine...", "Eroare de parasare. Reincercati.");
                    return;
                }

                List<DataModel> models = (List) obj[0];
                if (models.size() == 1) { // exista un singur user cu id-ul si modelul acesta, perfect!
                    registred = true;
                    dialog.dismiss();
                    Controller.getInstance().saveRegistrationCode(models.get(0).getCode(), MyApplication.getMyApplicationContext());
                    start.setEnabled(true);
                    return;
                }

                if (models.size() > 1) {
                    showBeforeCloseDialog("Eroare de inregistrare", "Acest dispozitiv prezinta o activitate suspicioasa. Contactati administratorul aplicatiei pentru mai multe detalii.");
                    return;
                }

                prepareRegistry(dialog);
            }
        };
        String deviceId = getDeviceId();
        DataModel filter = new DataModel(deviceId, null, Build.MODEL, null);

        DataRetriever retriever = new DataRetriever(filter);
        retriever.setStartEvent(startEvent);
        retriever.setDoneEvent(doneEvent);
        retriever.execute();
    }

    public void prepareRegistry (final ProgressDialog dialog) {
        if (dialog != null) {
            dialog.setMessage("Se pregateste inregistrarea. Va dura putin...");
        }

        Event doneParsingUsersEvent = new Event() {
            List<DataModel> models;
            List<String> codes;
            @Override
            public void doAction(Object... obj) {
                if (obj[0] == null || !(obj[0] instanceof List)) {
                    if (dialog != null) dialog.dismiss();
                    showBeforeCloseDialog("Eroare de parsare", "Restartati aplicatia. Daca aceeasi problema survine din nou, contactati administratorul aplicatiei.");
                    return;
                }
                models = (List<DataModel>) obj[0];

                if (dialog != null) dialog.setMessage("Se verifica inregistrarile anterioare");

                Event doneParsingKeysEvent = new Event() {
                    @Override
                    public void doAction(Object... obj) {
                        if (obj[0] == null || !(obj[0] instanceof List)) {
                            if (dialog != null) dialog.dismiss();
                            showBeforeCloseDialog("Eroare de parsare", "Restartati aplicatia. Daca aceeasi problema survine din nou, contactati administratorul aplicatiei.");
                            return;
                        }

                        codes = (List<String>) obj[0];
                        if (dialog != null) dialog.dismiss();
                        registratePhone(dialog, models, codes);
                    }
                };

                KeyRetreiver keyRetreiver = new KeyRetreiver();
                keyRetreiver.setDoneEvent(doneParsingKeysEvent);
                keyRetreiver.execute();
            }
        };

        DataRetriever dataRetriever = new DataRetriever();
        dataRetriever.setDoneEvent(doneParsingUsersEvent);
        dataRetriever.execute();
    }

    public void registratePhone (final ProgressDialog dialog, final List<DataModel> models, final List<String> codes) {
        if (dialog != null) dialog.dismiss();

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        input.setHint("Cod inregistrare");

        final AlertDialog alert = new AlertDialog.Builder(this)
                .setView(input)
                .setTitle("Inregistreaza aplicatia")
                .setMessage("")
                .setPositiveButton("CORECT", null)
                .setNegativeButton("RENUNT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finishAndRemoveTask();
                    }
                })
                .setCancelable(false)
                .create();

        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String code = input.getText().toString();
                //verificarea 1 : numarul de caractere corespunzator
                if (code.length() != CODE_LENGTH) {
                    input.setText("");
                    alert.setMessage("Numar nepotrivit de caractere");
                    return;
                }

                // verificarea 2 : sa existe codul de inregistrare
                if (!codes.contains(code)) {
                    input.setText("");
                    alert.setMessage("Cod invalid");
                    return;
                }

                // verificarea 3 : sa nu fie un cod deja luat de alta aplicatie
                boolean exists = false;
                for (DataModel m : models) {
                    if (m.getCode().equals(code)) {
                        exists = true; break;
                    }
                }

                if (exists) {
                    input.setText("");
                    alert.setMessage("Cod invalid");
                    return;
                }

                dialog.show();
                dialog.setTitle("Se inregistreaza aplcatia...");
                dialog.setMessage("");

                ConnectivityManager con = (ConnectivityManager) Start.this.getSystemService(CONNECTIVITY_SERVICE);
                Event doneEvent = new Event() {
                    @Override
                    public void doAction(Object... obj) {
                        dialog.dismiss();
                        alert.dismiss();
                        Controller.getInstance().saveRegistrationCode(code, MyApplication.getMyApplicationContext());
                        start.setEnabled(true);
                    }
                };

                SendRequest request = new SendRequest(con, doneEvent);
                String [] params = new String[] {
                        Start.this.getDeviceId(),
                        new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()),
                        Build.MODEL,
                        code
                };
                request.execute(params);
            }
        });
    }

    public void showBeforeCloseDialog (String title, String message) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("AM INTELES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finishAndRemoveTask();
                    }
                })
                .show();
    }

    public boolean checkInternetConnection () {
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        return netInfo != null;
    }

    public String getDeviceId () {
        return Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
}
