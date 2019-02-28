package gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.connection.simpleclient.Chronometer;
import com.connection.simpleclient.ClassIDs;
import com.connection.simpleclient.Controller;
import com.connection.simpleclient.MyApplication;
import com.connection.simpleclient.NotificationID;
import com.connection.simpleclient.R;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.ikovac.timepickerwithseconds.TimePicker;

import java.util.Calendar;
import java.util.HashMap;

import dto.EchipaDTO;
import dto.JocDTO;
import dto.MembruEchipaDTO;
import formula.Cronometer;
import formula.ElementType;
import formula.Formula;
import formula.FormulaElement;
import formula.MembriCount;
import formula.Variable;

import static android.R.id.message;

public class GameActivity extends AppCompatActivity {
    private static final int MAX_MEMBRI_DIF = 5;
    private TextView crono;
    private TextView cronoLimit;
    private TextView var1;
    private TextView interval1;
    private EditText input1;
    private TextView var2;
    private TextView interval2;
    private EditText input2;
    private TextView var3;
    private TextView interval3;
    private EditText input3;
    private TextView var4;
    private TextView interval4;
    private EditText input4;
    private TextView var5;
    private TextView interval5;
    private EditText input5;
    private TextView var6;
    private TextView interval6;
    private EditText input6;
    private TextView crono1;
    private Button bStart;
    private Button bStop;
    private Button bReset;
    private long startTime = -1;
    private Formula formula;
    private EchipaDTO echipa;
    private HashMap<View, FormulaElement> fields;
    private Button bDone;
    private Button absent;
    private JocDTO joc;
    private boolean isSaved = false;
    private boolean isAbsent = false;
    private NotificationCompat.Builder mBuilder;
    private Handler mHandler;
    private static Intent result;

    private long mPauseTime;
    private Chronometer chronometer;
    private Thread threadCrono;

    //help
    private ShowcaseView showcase;
    private Target absentTarget;
    private Target doneTarget;
    private static boolean madeTutorial = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Controller.getInstance().setCurrentActivity(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (result != null) {
            setResult(RESULT_OK, result);
            result = null;
            //finish();
        }

        // get formula & echipa
        formula = (Formula) getIntent().getExtras().getSerializable("formula");
        echipa = (EchipaDTO) getIntent().getExtras().getSerializable("echipa");
        joc = (JocDTO) getIntent().getExtras().getSerializable("joc");

        setTitle(joc.getJocGeneral().getNumeJocGeneral() + " : " + echipa.getMembriEchipa().size() + " participanti");
        buildNotification(joc.getJocGeneral().getNumeJocGeneral() + " : in desfasurare", "");

        fields = new HashMap<>();

        // crono elements

        crono1 = (TextView) findViewById(R.id.textView37);
        crono = (TextView) findViewById(R.id.crono);
        cronoLimit = (TextView) findViewById(R.id.textView58);
        bStart = (Button) findViewById(R.id.button10);
        bStop = (Button) findViewById(R.id.stop);
        bReset = (Button) findViewById(R.id.reset);
        bDone = (Button) findViewById(R.id.done);
        absent = (Button) findViewById(R.id.button12);

        crono.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                bStop.performClick();
                String time = crono.getText().toString();
                String [] ss = time.split(":");

                MyTimePickerDialog mTimePicker = new MyTimePickerDialog(GameActivity.this, new MyTimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                        // TODO Auto-generated method stub
                        crono.setText(String.format(
                                "%02d:%02d:%02d:%03d", hourOfDay, minute, seconds, 0
                        ));
                    }
                }, Integer.parseInt(ss[0]), Integer.parseInt(ss[1]), Integer.parseInt(ss[2]), true);
                mTimePicker.show();

                return false;
            }
        });

        bStart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (chronometer == null) {
                            if (startTime != -1) {
                                mPauseTime = System.currentTimeMillis() - mPauseTime;
                                startTime += mPauseTime;
                            }
                            chronometer = new Chronometer(GameActivity.this, startTime, ((Cronometer) fields.get(crono)).getMaxTime() );
                            threadCrono = new Thread(chronometer);
                            threadCrono.start();
                            chronometer.start();
                        }
                    }
                }
        );

        bStop.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (chronometer != null) {
                            chronometer.stop();
                            threadCrono.interrupt();
                            startTime = chronometer.getStartTime();
                            threadCrono = null;
                            chronometer = null;
                            mPauseTime = System.currentTimeMillis();
                        }
                    }
                }
        );

        bReset.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        crono.setText("00:00:00:000");
                        if (chronometer != null) {
                            chronometer.stop();
                            threadCrono.interrupt();
                        }
                        startTime = -1;
                        threadCrono = null;
                        chronometer = null;
                        crono.setText("00:00:00:000");

                    }
                }
        );


        bDone.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //onBackPressed();
                        //finishActivity(RESULT_OK);
                        new AlertDialog.Builder(GameActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Terminare joc")
                                .setMessage("Confirma terminarea jocului")
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!salveaza()){
                                            new AlertDialog.Builder(GameActivity.this)
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .setTitle("Abandonare")
                                                    .setMessage("Se pare ca exista cateva campuri necompletate. Jocul nu se va salva. Continui?")
                                                    .setPositiveButton("Da", new DialogInterface.OnClickListener()
                                                    {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            finish();
                                                        }

                                                    })
                                                    .setNegativeButton("NoNo", null)
                                                    .show();
                                        } else {
                                            finish();
                                        }
                                    }

                                })
                                .setNegativeButton("NU", null)
                                .show();
                    }
                }
        );

        absent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(GameActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Confirma marcarea echipei " + echipa.getNumeEchipa() + " ca absenta")
                                .setMessage("Echipa va primi punctajul minim si nu va mai putea reface aceasta proba.")
                                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        for (View v : fields.keySet()){
                                            if (v instanceof EditText) {
                                                ((EditText) v).setText(((Variable)fields.get(v)).getMinVal() + "");
                                            } else if (v instanceof TextView) {
                                                ((TextView) v).setText("00:00:00:000");
                                            }
                                        }
                                        isAbsent = true;
                                        salveaza();
                                        finish();
                                    }
                                })
                                .setNegativeButton("TOTUSI NU", null)
                                .show();

                    }
                }
        );


        // //group 1

        var1 = (TextView) findViewById(R.id.textView29);
        input1 = (EditText) findViewById(R.id.editText6);
        interval1 = (TextView) findViewById(R.id.textView30);

        // group 2

        var2 = (TextView) findViewById(R.id.textView31);
        input2 = (EditText) findViewById(R.id.editText7);
        interval2 = (TextView) findViewById(R.id.textView32);

        // group 3

        var3 = (TextView) findViewById(R.id.textView33);
        input3 = (EditText) findViewById(R.id.editText8);
        interval3 = (TextView) findViewById(R.id.textView34);

        // group 4

        var4 = (TextView) findViewById(R.id.textView35);
        input4 = (EditText) findViewById(R.id.editText9);
        interval4 = (TextView) findViewById(R.id.textView36);

        // group 5

        var5 = (TextView) findViewById(R.id.textView62);
        input5 = (EditText) findViewById(R.id.editText10);
        interval5 = (TextView) findViewById(R.id.textView63);

        // group 6

        var6 = (TextView) findViewById(R.id.textView64);
        input6 = (EditText) findViewById(R.id.editText11);
        interval6 = (TextView) findViewById(R.id.textView65);

        // setting up the views
        setViews();

        mHandler = new Handler(
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message message) {
                        stopChronometer();
                        return true;
                    }
                }
        );

    }



    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        menu.getItem(0).setTitle(echipa.toString());
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.echipa_menu_item : {
                Intent intent = new Intent(GameActivity.this, InformatiiMembru.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("echipa", echipa);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case R.id.help_menu_item : {
                makeTutorial();
                break;
            }
            case R.id.numar_membri_menu_item : {
                final EditText input = new EditText(GameActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                boolean hasMembri = false;
                FormulaElement auxElement = null;
                for (FormulaElement fe : formula.getVariables().keySet()) {
                    if (fe.getType() == ElementType.NR_MEMBRI) {
                        hasMembri = true;
                        auxElement = fe;
                    }
                }

                final FormulaElement membriElement = auxElement;

                AlertDialog.Builder alert = new AlertDialog.Builder(GameActivity.this);
                alert.setTitle("Schimbare numar membri");

                if (hasMembri) {
                    alert.setMessage("Introduceti numarul membrilor participanti:");
                    input.setText(membriElement.getValue() != -1 ?
                            membriElement.getValue() + "" :
                            echipa.getMembriEchipa().size() + "");
                    input.selectAll();
                    alert.setView(input);
                    alert.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int nr = -1;
                                    try {
                                        nr = Integer.parseInt(input.getText().toString());
                                        if (nr > echipa.getMembriEchipa().size() + MAX_MEMBRI_DIF) throw new Exception("Prea multi membri adaugati. Sunt acceptati cel mult " + MAX_MEMBRI_DIF + " in plus.");
                                        if (nr <= 0) throw new Exception("Numarul membrilor trebuie sa fie mai mare strict decat 0.");

                                        int dif = (membriElement.getValue() != -1 ?
                                                membriElement.getValue() :
                                                echipa.getMembriEchipa().size() ) - nr;

                                        if (dif < 0) { // mai trebuie adaugati membri (fictivi)
                                            dif *= -1;
                                            for (int j = 0; j < dif; ++j) {
                                                MembruEchipaDTO membru = new MembruEchipaDTO(-1);
                                                membru.setNumeMembruEchipa("fictiv");
                                                ((MembriCount) membriElement).getEchipa().getMembriEchipa().add(membru);
                                            }
                                        } else { // altfel, trebuie eliminati "nr" membri
                                            for (int j = 0; j < dif; ++j){
                                                ((MembriCount) membriElement).getEchipa().getMembriEchipa().remove(j);
                                            }
                                        }
                                        GameActivity.this.setTitle(joc.getJocGeneral().getNumeJocGeneral() + " : " + ((MembriCount) membriElement).getEchipa().getMembriEchipa().size() + " participanti");
                                    } catch (Exception e) {
                                        new AlertDialog.Builder(GameActivity.this)
                                                .setTitle("Eroare")
                                                .setMessage(e.getMessage())
                                                .setPositiveButton("AM INTELES", null)
                                                .show();
                                    }
                                }
                            });
                    alert.setNegativeButton("RENUNTA", null);
                } else {
                    alert.setMessage("Punctajul jocului nu depinde de numarul de membri");
                    alert.setPositiveButton("AM INTELES", null);
                }
                alert.show();

                break;
            }
            case R.id.descriere_item : {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder (GameActivity.this)
                        .setTitle("Descriere")
                        .setMessage(joc.getJocGeneral().getDescriereJoc())
                        .setPositiveButton("AM INTELES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                builder.create().show();
                break;
            }
            case android.R.id.home : {
                onBackPressed();
                break;
            }
            default: super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void updateTimerText (final String time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                crono.setText(time);
            }
        });
    }

    public void setViews () {
        HashMap<FormulaElement, String> variables = formula.getVariables();
        for (FormulaElement e : variables.keySet()){
            if (e.getClass() == Variable.class) { // daca pt [a, b], a>b, interschimbam
                Variable v = (Variable) e;
                if (v.getMaxVal() < v.getMinVal()) {
                    int aux = v.getMaxVal();
                    v.setMaxVal(v.getMinVal());
                    v.setMinVal(aux);
                }
            }

            switch (e.getType()) {
                case VAR :
                    if (!fields.containsKey(input1)){
                        var1.setText(e.getDescriere());
                        interval1.setText(((Variable) e).getMinVal() + " - " + ((Variable) e).getMaxVal());
                        fields.put(input1, e);
                    } else if (!fields.containsKey(input2)) {
                        var2.setText(e.getDescriere());
                        interval2.setText(((Variable) e).getMinVal() + " - " + ((Variable) e).getMaxVal());
                        fields.put(input2, e);
                    } else if (!fields.containsKey(input3)) {
                        var3.setText(e.getDescriere());
                        interval3.setText(((Variable) e).getMinVal() + " - " + ((Variable) e).getMaxVal());
                        fields.put(input3, e);
                    } else if (!fields.containsKey(input4)) {
                        var4.setText(e.getDescriere());
                        interval4.setText(((Variable) e).getMinVal() + " - " + ((Variable) e).getMaxVal());
                        fields.put(input4, e);
                    } else if (!fields.containsKey(input5)) {
                        var5.setText(e.getDescriere());
                        interval5.setText(((Variable) e).getMinVal() + " - " + ((Variable) e).getMaxVal());
                        fields.put(input5, e);
                    } else if (!fields.containsKey(input6)) {
                        var6.setText(e.getDescriere());
                        interval6.setText(((Variable) e).getMinVal() + " - " + ((Variable) e).getMaxVal());
                        fields.put(input6, e);
                    }
                    break;
                case CRONO:
                    crono1.setText(e.getDescriere());
                    cronoLimit.setText(((Cronometer) e).getMaxTime() + " min.");
                    fields.put(crono, e);
                    break;
                case NR_MEMBRI:
                    ((MembriCount) e).setEchipa(Controller.getInstance().makeCopy(echipa));
                    break;
            }
        }

        // ascundem fiecare camp care nu a fost asociat cu un element din formula
        if (!fields.containsKey(input1)) {
            var1.setVisibility(View.GONE);
            input1.setVisibility(View.GONE);
            interval1.setVisibility(View.GONE);
        }

        if (!fields.containsKey(input2)) {
            var2.setVisibility(View.GONE);
            input2.setVisibility(View.GONE);
            interval2.setVisibility(View.GONE);
        }

        if (!fields.containsKey(input3)) {
            var3.setVisibility(View.GONE);
            input3.setVisibility(View.GONE);
            interval3.setVisibility(View.GONE);
        }

        if (!fields.containsKey(input4)) {
            var4.setVisibility(View.GONE);
            input4.setVisibility(View.GONE);
            interval4.setVisibility(View.GONE);
        }

        if (!fields.containsKey(input5)) {
            var5.setVisibility(View.GONE);
            input5.setVisibility(View.GONE);
            interval5.setVisibility(View.GONE);
        }

        if (!fields.containsKey(input6)) {
            var6.setVisibility(View.GONE);
            input6.setVisibility(View.GONE);
            interval6.setVisibility(View.GONE);
        }

        if (!fields.containsKey(crono)) {
            crono.setVisibility(View.GONE);
            crono1.setVisibility(View.GONE);
            cronoLimit.setVisibility(View.GONE);
            bReset.setVisibility(View.GONE);
            bStart.setVisibility(View.GONE);
            bStop.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed () {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Sfarsit joc")
                .setMessage("Jocul nu va fi salvat. Continui?")
                .setPositiveButton("Da", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }

                })
                .setNegativeButton("NoNo", null)
                .show();
    }

    /*
    @Override
    protected void onUserLeaveHint () {
        //super.onNewIntent(intent);
        showNotification(NotificationID.JOC_ACTIV_NOTIFICATION_ID);
    }
    */

    @Override
    protected void onPause () {
        super.onPause();
        showNotification(NotificationID.JOC_ACTIV_NOTIFICATION_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(RESULT_CANCELED);
        stopChronometer();
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(3); // JOC_ACTIV_NOTIFICATION_ID
    }

    public boolean salveaza () {
        if (isSaved) return true;
        try {
            HashMap<FormulaElement, String> variables = new HashMap<>();
            for (View v : fields.keySet()) {
                int value = 0;
                FormulaElement e = fields.get(v);

                if (v instanceof EditText) {
                    value = Integer.parseInt(String.valueOf(((EditText) v).getText()));
                } else {
                    //value = Integer.parseInt(String.valueOf(((TextView) v).getText()));
                    String s = String.valueOf(((TextView) v).getText());
                    String ss[] = s.split(":");
                    StringBuilder sb = new StringBuilder(ss[ss.length - 1]);
                    ss[ss.length - 1] = sb.substring(0, 1);

                    if (Integer.parseInt(ss[1]) > ((Cronometer) e).getMaxTime()) {
                        ((Cronometer) e).setTime(((Cronometer) e).getMaxTime() * 1000);
                    } else {
                        s = "";
                        for (String aux : ss) {
                            s += aux;
                        }

                        ((Cronometer) e).setTime(Integer.parseInt(s));
                    }
                }

                switch (e.getType()) {
                    case VAR:
                        if (((Variable) e).getMaxVal() >= value && value >= ((Variable) e).getMinVal()) {
                            ((Variable) e).setValue(value);
                        } else if (((Variable) e).getMaxVal() < value) {
                            ((Variable) e).setValue(((Variable) e).getMaxVal());
                        } else {
                            ((Variable) e).setValue(((Variable) e).getMinVal());
                        }
                        break;
                    case CRONO:
                        break;
                }

                variables.put(e, e.getNume());
            }

            for (FormulaElement fe : formula.getVariables().keySet()){
                switch (fe.getType()) {
                    case NR_MEMBRI:
                        if (((MembriCount) fe).getEchipa() == null) ((MembriCount) fe).setEchipa(echipa);
                        variables.put(fe, formula.getVariables().get(fe));
                        break;
                    case CONST:
                        variables.put(fe, formula.getVariables().get(fe));
                        break;
                }
            }

            //salveaza undeva valorile


            Intent results = new Intent();
            Bundle bundle = new Bundle();
            Formula f = new Formula(variables, formula.getFormula());
            bundle.putSerializable("formula", f);
            bundle.putSerializable("echipa", echipa);
            bundle.putSerializable("absent", isAbsent);
            results.putExtras(bundle);
            result = results;
            setResult(RESULT_OK, results);
            isSaved = true;
            return true;
        } catch (Exception e) {
            setResult(Activity.RESULT_CANCELED);
            return false;
        }
    }

    @Override
    protected void onResume () {
        super.onResume();
        Controller.getInstance().setCurrentActivity(this);
        Controller.getInstance().hideLoadingScreenDelayed(500);
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(3); // JOC_ACTIV_NOTIFICATION_ID
        if (!madeTutorial) {
            boolean madeTutorialBefore = Controller.getInstance().wasActivityUsedBefore(ClassIDs.GAME_ACTIVITY, this);
            if (!madeTutorialBefore) {
                Controller.getInstance().setFirstUseActivity(ClassIDs.GAME_ACTIVITY, true, this);
                makeTutorial();
            }
            madeTutorial = true;
        }
    }

    private void buildNotification (String title, String text) {
        Intent intent = new Intent (MyApplication.getMyApplicationContext(), GameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("formula", formula);
        bundle.putSerializable("echipa", echipa);
        bundle.putSerializable("joc", joc);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.getMyApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent);

        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);
        mBuilder.mNotification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    private void showNotification (NotificationID id) {
        int ID = id == NotificationID.ACTIVITATI_NOTIFICATION_ID ? 1 :
                id == NotificationID.JOC_ACTIV_NOTIFICATION_ID ? 3 : 2;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID, mBuilder.build());
    }

    public void stopChronometer () {
        if (chronometer != null) {
            chronometer.stop();
            threadCrono.interrupt();
            startTime = chronometer.getStartTime();
            threadCrono = null;
            chronometer = null;
            mPauseTime = System.currentTimeMillis();
        }
        if (this.isFinishing()) return;
        try {
            new VibrationAlert().start();
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("Timpul a expirat")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GameActivity.this.stopVibrator();
                        }

                    })
                    .show();
            Controller.getInstance().display("Timpul jocului a expirat");
        } catch (Exception e) {
            try {GameActivity.this.stopVibrator();} catch (Exception e1) {}
        }
    }

    public void stopVibrator () {
        vibrationRunning = false;
    }

    public Handler getHandler () {
        return mHandler;
    }

    private boolean vibrationRunning;

    class VibrationAlert extends Thread {
        @Override
        public void run () {
            Vibrator v = (Vibrator) GameActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
            vibrationRunning = true;

            while (vibrationRunning) {
                v.vibrate(200);
                try { Thread.sleep(1000); } catch (Exception e) {}
            }
        }
    }

    private int step = 0;
    private final static int TOTAL_STEPS = 3;

    private void makeTutorial () {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(bDone.getWindowToken(), 0);
        step = 0;
        absentTarget = new ViewTarget(R.id.button12, this);
        doneTarget = new ViewTarget(R.id.done, this);

        showcase = new ShowcaseView.Builder(this)
                .setTarget(Target.NONE)
                .setContentTitle("Desfasurarea Jocului")
                .setContentText("Aici vei puncta echipa selectata. Toate punctajele acordate separat in fiecare camp " +
                        "se vor cumula intr-un total final.")
                .setStyle(R.style.TransparentStyle)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickperformed(view);
                    }
                })
                .build();
        showcase.setButtonText("Urmatorul");
        showcase.setBlocksTouches(true);
        showcase.show();

        step++;
    }

    private void clickperformed (View view) {
        switch (step) {
            case 0 : break;
            case 1 :
                showcase.setShowcase(absentTarget, true);
                showcase.setContentTitle("Absent");
                showcase.setContentText("Marcheaza echipa selectata ca 'absenta'. In clasamentul final, echipa respectiva " +
                        "va fi trecuta cu punctajul '0' la aceasta proba (daca nu exista cel putin un animator care s-o puncteze)");
                break;
            case 2 :
                showcase.setShowcase(doneTarget, true);
                showcase.setContentTitle("Finalizeaza Joc");
                showcase.setContentText("Dupa ce ai completat toate campurile cu scorurile echipei, inchieie jocul folosing acest buton.");
                showcase.setButtonText("Gata");
                break;
            default: showcase.hide();
        }
        step++;
    }
}
