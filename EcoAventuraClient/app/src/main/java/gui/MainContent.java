package gui;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.connection.simpleclient.Activitate;
import com.connection.simpleclient.ClassIDs;
import com.connection.simpleclient.ConnectionController;
import com.connection.simpleclient.Controller;

import adapters.ActivitatiCompleteListAdaptor;
import adapters.AnimatoriAdapter;
import dto.AnimatorDTO;
import dto.MembruEchipaDTO;
import floatingWindow.FloatingWindow;
import pl.droidsonroids.gif.GifImageView;
import receiver.DataListener;

import com.connection.simpleclient.MyApplication;
import com.connection.simpleclient.NotificationID;
import com.connection.simpleclient.R;
import com.connection.simpleclient.SecondTouchListener;
import com.connection.simpleclient.Timer;
import com.connection.simpleclient.TouchListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;


import adapters.ActivitatiListAdapter;
import dto.ActivitateDTO;
import dto.EchipaDTO;
import dto.JocDTO;
import dto.Message;
import dto.UserDTO;
import temporizator.Event;
import temporizator.Temporizator;

public class MainContent extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,
        DataListener {

    private final static String SERIAZLIZED_LIST_FILE_SUF = "activitati.srz";
    private final static int SUCCES = 1;
    private final static int FAIL = 2;
    private final static int SEND = 3;

    private Toolbar toolbar;
    private ListView lvActivitate;
    private ActivitatiListAdapter adapter;
    private ActivitatiCompleteListAdaptor activitatiCompleteAdaptor;
    private AnimatoriAdapter animatoriAdapter;
    private List<Activitate> mActivitateList;
    private List<Activitate> mActivitateCompletaList;
    private List<ActivitateDTO> activitati;
    private List<JocDTO> jocuri;

    private boolean isWaitingPackage = false;
    private boolean isCheckingSerie = false;
    private boolean isRefreshingSerie = false;
    private boolean isSendingSerie = false;

    private UserDTO user;
    private FloatingActionButton fab;
    private NavigationView navigationView;
    private boolean readActivitati = false;
    private boolean isWaitingConfirm;
    private boolean checkerActive = false;
    private boolean isWaitingReceivedConfirm = false;
    private boolean deserialized = false;

    private Handler handler;
    private Handler waitingHandler;
    private TouchListener mTouchListener;

    private NotificationCompat.Builder mBuilder;
    private static final String ACTION = "close_notification";
    private ProgressDialog progressDialog;

    private static boolean isPaused = false;

    private SecondTouchListener mSecondTouchListener;

    //listeners
    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;

    private View emptyView = null;
    private GifImageView loadingGif;

    private Date expDate;
    private Timer timer;

    //ajutor
    private ShowcaseView showcase;
    private Target navigationTarget;
    private Target fabTarget;
    private static boolean madeTutorial = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        emptyView = View.inflate(this, R.layout.empty_view, null);
        loadingGif = (GifImageView) findViewById(R.id.gif2);
        loadingGif.setVisibility(View.VISIBLE);

        isPaused = false;
        Controller.getInstance().setCurrentActivity(this);

        user = (UserDTO) getIntent().getExtras().getSerializable("user");

        if (user == null) {
            finish();

        }
        if (!Controller.getInstance().isOffline()) {
            Controller.getInstance().serializeUser(user);
            if (Controller.getInstance().getReceiver() != null) {
                Controller.getInstance().getReceiver().addListener(this);
            }
        }

        if (Controller.getInstance().getUser() == null) {
            try {
                ConnectionController.getInstance().connectUser(user.getUsername(), user.getParola());
                Controller.getInstance().setUser(user);
            } catch (Exception e) {}
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        ((TextView) header.findViewById(R.id.userName)).setText("Bine ai venit, " + user.getUsername() + "!");
        timer = new Timer(
                (TextView) header.findViewById(R.id.timpRamas),
                (TextView) header.findViewById(R.id.dataExpirare),
                this
        );

        navigationView.setCheckedItem(R.id.nav_gallery);


        setTitle("Activitatile mele de azi");
        handler = new Handler(
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(android.os.Message message) {
                        switch (message.what){
                            case SEND :
                                Controller.getInstance().sendData(Controller.getInstance().pack(mActivitateCompletaList));
                                Snackbar.make(fab, "Se asteapta confirmarea serverului...", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("SendInfo", null).show();
                                isWaitingReceivedConfirm = true;
                                new Thread(new gui.MainContent.Temporizator(handler, 7000)).start();
                                waitingHandler = handler;
                                break;
                            case SUCCES :
                                fab.setEnabled(true);
                                //lvActivitate.requestDisallowInterceptTouchEvent(false);
                                enableDisableView(lvActivitate, true);
                                activitatiCompleteAdaptor.markAllAsSent();
                                activitatiCompleteAdaptor.notifyDataSetChanged();
                                Snackbar.make(fab, "Informatiile au fost trimise!", Snackbar.LENGTH_SHORT)
                                        .setAction("SendInfo", null).show();
                                break;
                            case FAIL :
                                //lvActivitate.requestDisallowInterceptTouchEvent(false);
                                enableDisableView(lvActivitate, true);
                                fab.setEnabled(true);
                                Snackbar.make(fab, "Conexiune instabila", Snackbar.LENGTH_SHORT)
                                        .setAction("SendInfo", null).show();
                                break;
                            default:
                        }
                        return true;
                    }
                }
        );

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Controller.getInstance().isOffline()){
                    Snackbar.make(view, "Offline...", Snackbar.LENGTH_SHORT)
                            .setAction("offline", null).show();
                    navigationView = (NavigationView) findViewById(R.id.nav_view);
                    View header = navigationView.getHeaderView(0);
                    timer.setDataExpirare((TextView) header.findViewById(R.id.dataExpirare));
                    timer.setTimpRamas((TextView) header.findViewById(R.id.timpRamas));
                    timer.refresh();
                    return;
                }
                if (lvActivitate.getAdapter() instanceof ActivitatiListAdapter || lvActivitate.getAdapter() instanceof AnimatoriAdapter) {
                    Snackbar.make(view, "Se reimprospateaza...", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Action", null).show();
                    Controller.getInstance().sendMessage(Message.IS_SOMETHING_TO_RECIEVE);
                    navigationView = (NavigationView) findViewById(R.id.nav_view);
                    View header = navigationView.getHeaderView(0);
                    timer.setDataExpirare((TextView) header.findViewById(R.id.dataExpirare));
                    timer.setTimpRamas((TextView) header.findViewById(R.id.timpRamas));
                    timer.refresh();
                } else if (lvActivitate.getAdapter() instanceof ActivitatiCompleteListAdaptor && !checkerActive) {

                    if (timer != null && timer.hasExpired()) {
                        // verificam daca exista penalizari
                        boolean existsPenalizari = false;
                        for (Activitate a : activitatiCompleteAdaptor.getmActivitatiCompleteList()) {
                            if (isPenalizare(a) && !a.isSent()) {
                                existsPenalizari = true;
                                break;
                            }
                        }
                        Snackbar.make(view, "Programul de activitati a expirat", Snackbar.LENGTH_SHORT)
                                .setAction("notify", null).show();

                        if (existsPenalizari) {
                            Snackbar.make(view, "Trimite penalizarile", Snackbar.LENGTH_LONG)
                                    .setAction("Trimite", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ArrayList<Activitate> penalizari = new ArrayList<Activitate>();
                                            for (Activitate a : activitatiCompleteAdaptor.getmActivitatiCompleteList()) {
                                                if (isPenalizare(a) && !a.isSent()) {
                                                    //sendActivitate(a);
                                                    penalizari.add(a);
                                                }
                                            }
                                            Controller.getInstance().sendMessage(Message.IS_SOMETHING_TO_SEND);
                                            Controller.getInstance().sendData(Controller.getInstance().pack(penalizari));
                                            Snackbar.make(fab, "Se asteapta confirmarea serverului...", Snackbar.LENGTH_INDEFINITE)
                                                    .setAction("SendInfo", null).show();
                                            isWaitingReceivedConfirm = true;
                                            new Thread(new gui.MainContent.Temporizator(handler, 7000)).start();
                                            waitingHandler = handler;

                                        }
                                    }).show();
                        }
                        return;
                    }

                    if (activitatiCompleteAdaptor.getToBeSendItemsCount() != 0) {
                        Snackbar.make(view, "Se asteapta raspuns de la server...", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Wait", null).show();
                        fab.setEnabled(false);
                        //lvActivitate.requestDisallowInterceptTouchEvent(true);
                        enableDisableView(lvActivitate, false);

                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            checkerActive = true;
                                            isWaitingConfirm = true;
                                            Controller.getInstance().sendMessage(Message.TEST_CONNECTION);

                                            if (Controller.getInstance().isOffline())  {
                                                android.os.Message msg = handler.obtainMessage();
                                                msg.what = FAIL;
                                                handler.sendMessage(msg);
                                                throw new Exception();
                                            }

                                            long start = System.currentTimeMillis();
                                            long end = start + 10*1000; // 10 seconds * 1000 ms/sec
                                            while (isWaitingConfirm && !Controller.getInstance().isOffline() && System.currentTimeMillis() < end){}

                                            if (System.currentTimeMillis() > end){
                                                Controller.getInstance().setOffline(true);
                                                throw new Exception();
                                            }
                                            Controller.getInstance().sendMessage(Message.IS_SOMETHING_TO_SEND);
                                            android.os.Message msg = handler.obtainMessage();
                                            msg.what = SEND;
                                            handler.sendMessage(msg);
                                        } catch (Exception e) {
                                            android.os.Message msg = handler.obtainMessage();
                                            msg.what = FAIL;
                                            handler.sendMessage(msg);
                                        }
                                        checkerActive = false;
                                    }
                                }
                        ).start();

                    } else {
                        Snackbar.make(view, "Nimic de trimis...", Snackbar.LENGTH_SHORT)
                                .setAction("SendInfo", null).show();
                    }
                }
            }
        });

        fab.setImageDrawable(ContextCompat.getDrawable(MyApplication.getMyApplicationContext(), R.drawable.refresh));
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        //Controller.getInstance().sendMessage(Message.IS_SOMETHING_TO_RECIEVE);

        lvActivitate = (ListView) findViewById(R.id.list);
        lvActivitate.setAdapter(adapter);

        emptyView.setVisibility(View.GONE);
        emptyView.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.FILL_PARENT));
        ((ViewGroup) lvActivitate.getParent()).addView(emptyView);
        lvActivitate.setEmptyView(emptyView);

        //while (activitati == null && jocuri == null);

        //createActivitati();


        longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //displayer("Ai selectat a " + i + "-a activitate");

                int i;
                try {
                    i = lvActivitate.getPositionForView(view);
                } catch (Exception e) { i = ListView.INVALID_POSITION; }
                if (i == ListView.INVALID_POSITION) return false;

                if (!(lvActivitate.getAdapter() instanceof ActivitatiListAdapter)){
                    return false;
                }
                Activitate a = (Activitate) adapter.getItem(i);
                if (a.isHighPiority()){
                    a.setHighPiority(false);
                    adapter.pushDown(a);
                } else {
                    a.setHighPiority(true);
                    adapter.bringToFront(a);
                }
                adapter.notifyListTouch(true);

                adapter.notifyDataSetChanged();
                return true;
            }
        };


        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyListTouch(true);Activitate a;

                boolean locked = false;
                boolean echipeIntegrale = false;
                final int i = lvActivitate.getPositionForView(view);

                if (i == ListView.INVALID_POSITION) return;

                if (lvActivitate.getAdapter() instanceof ActivitatiCompleteListAdaptor){
                    a = (Activitate) activitatiCompleteAdaptor.getItem(i);
                    locked = true;
                    echipeIntegrale = true;
                } else if (lvActivitate.getAdapter() instanceof ActivitatiListAdapter) {
                    a = (Activitate) adapter.getItem(i);
                    locked = false;
                } else if (lvActivitate.getAdapter() instanceof AnimatoriAdapter) {
                    // deschide o noua activitate
                    Controller.getInstance().showLoadingScreenDelayed(0);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AnimatorDTO animatorDTO = (AnimatorDTO) animatoriAdapter.getItem(i);
                            Intent intent = new Intent(MainContent.this, AnimatorContent.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("animator", animatorDTO);
                            bundle.putSerializable("activitati", (ArrayList<Activitate>) getActivitatiByAnimator(animatorDTO));
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }, 500);

                    return;
                } else {
                    a = null;
                }


                if (a == null) return;
                // start new activity
                Controller.getInstance().showLoadingScreenDelayed(0);

                final boolean finalLocked = locked;
                final boolean finalEchipeIntegrale = echipeIntegrale;
                final Activitate finalA = a;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finalA.setListItem(i);
                        Intent intent = new Intent(MainContent.this, ViewActivitate.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("activitate", finalA);
                        bundle.putSerializable("lock", finalLocked);
                        bundle.putSerializable("modVizualizare", finalEchipeIntegrale);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }, 500);

            }
        };


        Controller.getInstance().setClickListener(clickListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        Controller.getInstance().setSaveEvent(new Event() {
            @Override
            public void doAction(Object ... obj) {
                try {
                    serializare();
                } catch (Exception e) {}
            }
        });

        //se buseste fisierul de intrare pt a se forta citirea datelor din server
        //busesteFisier();

        // se incarca datele salvate
        deserializare();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Delogare")
                    .setMessage("Sigur doresti sa te deloghezi?")
                    .setPositiveButton("Da", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Controller.getInstance().showLoadingScreenDelayed(0);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Controller.getInstance().deconectare();
                                    finish();
                                }
                            }, 500);

                        }

                    })
                    .setNegativeButton("NoNo", null)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case R.id.raporteaza : {
                Intent intent = new Intent(this, Report.class);
                startActivity(intent);
                break;
            }
            case R.id.item1 : { // ajutor
                makeTutorial();
                break;
            }
            case R.id.idProgram : {
                displayer("ID: " + Controller.getInstance().getProgramID(this));
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        //noinspection SimplifiableIfStatement

        return true;
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) { // activitatea actuala
            try {
                if (!mActivitateList.isEmpty()) {
                    final Activitate a = (Activitate) adapter.getItem(0);
                    if (a == null) return true;
                    // start new activity
                    Controller.getInstance().showLoadingScreenDelayed(0);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            a.setListItem(0);
                            Intent intent = new Intent(MainContent.this, ViewActivitate.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("activitate", a);
                            bundle.putSerializable("modVizualizare", false);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }, 500);

                } else throw new Exception();
            } catch (Exception e) {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Activitate")
                        .setMessage("Nimic de facut...deocamdata")
                        .setPositiveButton("Yuhu", null)
                        .show();
            }
        } else if (id == R.id.nav_gallery) { // activitatile mele
            setTitle("Activitatile mele");
            try {adapter.refresh(); } catch (Exception e) {}
            lvActivitate.setAdapter(adapter);
            fab.setImageDrawable(ContextCompat.getDrawable(MyApplication.getMyApplicationContext(), R.drawable.refresh));
        } else if (id == R.id.nav_slideshow) { // toate activitatile
            setTitle("Toate activitatile");
            lvActivitate.setAdapter(animatoriAdapter);
            fab.setImageDrawable(ContextCompat.getDrawable(MyApplication.getMyApplicationContext(), R.drawable.refresh));
        } else if (id == R.id.nav_manage) { // activitati complete
            setTitle("Activitati complete");
            lvActivitate.setAdapter(activitatiCompleteAdaptor);
            fab.setImageDrawable(ContextCompat.getDrawable(MyApplication.getMyApplicationContext(), R.drawable.ic_menu_send));
        } else if (id == R.id.nav_share) { // copii
            final Intent intent = new Intent(MainContent.this, MembriContent.class);
            final Bundle bundle = new Bundle();
            Controller.getInstance().showLoadingScreenDelayed(0);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ArrayList<MembruEchipaDTO> aux = (ArrayList<MembruEchipaDTO>) Controller.getInstance().getMembri();
                            aux.size(); // test for not null
                            bundle.putSerializable("membri", aux);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } catch (Exception e) {
                            Controller.getInstance().sendMessage(Message.SENDING_SERIE);
                            displayer("Serie inexistenta");
                        }
                    }
                }, 500);

        } else if (id == R.id.nav_send) { // echipe
            final Intent intent = new Intent(MainContent.this, EchipeContent.class);
            final Bundle bundle = new Bundle();
            Controller.getInstance().showLoadingScreenDelayed(0);
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<EchipaDTO> aux = (ArrayList<EchipaDTO>) Controller.getInstance().getEchipe();
                        aux.size();
                        bundle.putSerializable("echipe", aux);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }, 500);

            } catch (Exception e) {
                displayer("Serie inexistenta");
                Controller.getInstance().sendMessage(Message.SENDING_SERIE);
            }
        } else if (id == R.id.nav_setari) {
            Controller.getInstance().showLoadingScreenDelayed(0);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainContent.this, Settings.class);
                    startActivity(intent);
                }
            }, 500);

        } else if (id == R.id.nav_deconectare) {
            Controller.getInstance().showLoadingScreenDelayed(0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Controller.getInstance().deconectare();
                    finish();
                }
            }, 500);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void update(Object... o) {
        if (o[0] instanceof Message) {
            Message m = (Message) o[0];

            switch (m) {

                case IS_SOMETHING_TO_SEND:
                    loadingGif.setVisibility(View.VISIBLE);
                    isWaitingPackage = true;
                    activitati = null;
                    jocuri = null;
                    expDate = null;
                    break;
                case NOTHING_TO_SEND:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingGif.setVisibility(View.INVISIBLE);
                        }
                    }, 500);
                    activitati = new ArrayList<>();
                    jocuri = new ArrayList<>();
                    break;
                case CHECK_SERIE:
                    isCheckingSerie = true;
                    Snackbar.make(fab, "Se verifica seria...", Snackbar.LENGTH_SHORT).setAction("Reimprospatare", null).show();
                    break;
                case DISCONNECT_USER:
                    displayer("Delogat de catre server");
                    finish();
                    break;
                case SENDING_SERIE:
                    isSendingSerie = true;
                    break;
                case TEST_CONNECTION: {
                    if (isWaitingConfirm) {
                        isWaitingConfirm = false;
                    }
                    break;
                }
                case ACTIVITIES_RECEIVED: {
                    if (isWaitingReceivedConfirm) {
                        isWaitingReceivedConfirm = false;
                        if (waitingHandler != null) {
                            android.os.Message message = waitingHandler.obtainMessage();
                            message.what = SUCCES;
                            waitingHandler.sendMessage(message);
                            waitingHandler = null;
                        }
                    }
                }
            }
        } else if (isWaitingPackage && o[0] instanceof Long) { // daca a fost trimis un nou plan de activitati, si cel vechi trebuie resetat
            if ((Long) o[0] != Controller.getInstance().getProgramID(MainContent.this)) {
                Controller.getInstance().setProgramID((Long) o[0], MainContent.this);
                mActivitateList = null;

                if (mActivitateCompletaList != null) {
                    Iterator<Activitate> it = mActivitateCompletaList.iterator();
                    while (it.hasNext()) {
                        Activitate a = it.next();
                        if (!isPenalizare(a)) {
                            it.remove();
                        } else if (a.isSent()){
                            it.remove();
                        }
                    }
                }
                if (activitatiCompleteAdaptor != null) {
                    if (activitatiCompleteAdaptor.getmActivitatiCompleteList() != null) {
                        Iterator<Activitate> it = activitatiCompleteAdaptor.getmActivitatiCompleteList().iterator();
                        while (it.hasNext()) {
                            Activitate a = it.next();
                            if (!isPenalizare(a)) {
                                it.remove();
                            } else if (a.isSent()) {
                                it.remove();
                            }
                        }
                    }
                }
                serializare();
            }
        } else if (isWaitingPackage && o[0] instanceof ArrayList) {
            if (!readActivitati) {
                activitati = (List<ActivitateDTO>) o[0];
                readActivitati = true;
            } else {
                jocuri = (List<JocDTO>) o[0];
                readActivitati = false;
            }
        } else if (isWaitingPackage && o[0] instanceof Date) {
            expDate = (Date) o[0];

        } else if (isCheckingSerie && o[0] instanceof Integer) {
            isCheckingSerie = false;

            if ((Integer) o[0] != Controller.getInstance().getNrSerie() || Controller.getInstance().getEchipe() == null){
                Controller.getInstance().sendMessage(Message.REQUEST_SERIE);
                Controller.getInstance().setNrSerie((Integer) o[0]);
                isRefreshingSerie = true;
                reset();
            } else {
                Snackbar.make(fab, "Actualizezi seria?", Snackbar.LENGTH_LONG).setAction("Actualizeaza", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Controller.getInstance().sendMessage(Message.REQUEST_SERIE);
                        isRefreshingSerie = true;
                    }
                }).show();
            }
        } else if (isRefreshingSerie && o[0] instanceof List) {
            Controller.getInstance().salveazaEchipe((List<EchipaDTO>) o[0], Controller.getInstance().getNrSerie());
            isRefreshingSerie = false;
            Snackbar.make(fab, "Serie actualizata!", Snackbar.LENGTH_SHORT).setAction("Reimprospatare", null).show();
        } else if (isSendingSerie && ((Object [])o[0])[0] instanceof List && ((Object[]) o[0])[1] instanceof Integer) {
            Controller.getInstance().salveazaEchipe((List<EchipaDTO>) ((Object [])o[0])[0], (Integer) ((Object[]) o[0])[1]);
            isSendingSerie = false;
            Snackbar.make(fab, "Serie actualizata!", Snackbar.LENGTH_SHORT).setAction("Reimprospatare", null).show();
        }

        if (activitati != null && jocuri != null && expDate != null && isWaitingPackage) {
            isWaitingPackage = false;
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            View header = navigationView.getHeaderView(0);
            timer.setDataExpirare((TextView) header.findViewById(R.id.dataExpirare));
            timer.setTimpRamas((TextView) header.findViewById(R.id.timpRamas));
            timer.setDataExpirare(expDate);
            Controller.getInstance().dateWasUpdated();

            createActivitati();
            // afisam notificarea
            if (isPaused) {
                buildNotification("Activitati primite!");
                showNotification(NotificationID.ACTIVITATI_NOTIFICATION_ID);
            }
        }
    }

    public void reset () {
        mActivitateList = null;
        mActivitateCompletaList = null;
        activitati = new ArrayList<>();
        jocuri = new ArrayList<>();
        //busesteFisier();
        serializare();
    }


    public void displayer (String msg) {
        Controller.getInstance().display(msg);
    }

    public void removePreviousProgramActivities () {
        Iterator<Activitate> iterator;
        final long ID = Controller.getInstance().getProgramID(this);
        if (mActivitateList != null) {
            iterator = mActivitateList.iterator();

            while (iterator.hasNext()) {
                Activitate next = iterator.next();

                if (next.getIdProgram() != ID && !isPenalizare(next)){
                    iterator.remove();
                }
            }
        }

        if (mActivitateCompletaList != null) {
            iterator = mActivitateCompletaList.iterator();

            while (iterator.hasNext()) {
                Activitate a = iterator.next();
                if (a.getIdProgram() != ID && !isPenalizare(a)) {
                    iterator.remove();
                }
            }
        }

        if (activitatiCompleteAdaptor != null) {
            if (activitatiCompleteAdaptor.getmActivitatiCompleteList() != null) {
                iterator = activitatiCompleteAdaptor.getmActivitatiCompleteList().iterator();

                while (iterator.hasNext()) {
                    Activitate a = iterator.next();
                    if (a.getIdProgram() != ID && ! isPenalizare(a)) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    public void createActivitati () {

        if (!user.equals(Controller.getInstance().getUser())) return;

        // inlaturam activitatile din alte programe anterioare
        removePreviousProgramActivities();

        ArrayList<Activitate> auxActivitateList = (ArrayList<Activitate>) mActivitateList; // vechile activitati
        mActivitateList = new ArrayList<>();                                               // noile activitati

        if (mActivitateCompletaList == null) {
            if (activitatiCompleteAdaptor != null) {
                mActivitateCompletaList = activitatiCompleteAdaptor.getmActivitatiCompleteList();
            } else {
                mActivitateCompletaList = new ArrayList<>();
            }
        }


        List<ActivitateDTO> activitatiCopy = copyActivitati(activitati);
        List<JocDTO> jocuriCopy = copyJocuri(jocuri);

        AnimatorDTO animatorLogat = null;
        List<AnimatorDTO> animatori;
        try {
            animatori = Controller.getInstance().getAnimatori(activitati, jocuri, true);
        } catch (Exception e) {
            animatori = new ArrayList<>();
        }

        for (AnimatorDTO a : animatori) {
            if (a.getNumeAnimator().equalsIgnoreCase(user.getUsername())) {
                animatorLogat = a;
            }
        }

        if (animatorLogat != null) {

            if (animatorLogat.getSarcini() != null) { // daca activitatile animatorului a fost repartizate separat
                for (Object o : animatorLogat.getSarcini()) {
                    if (o.getClass() == ActivitateDTO.class) {
                        ((ArrayList<Activitate>) mActivitateList).add(new Activitate((ActivitateDTO) o, null, ((ActivitateDTO) o).getEchipe()));
                    } else {
                        ((ArrayList<Activitate>) mActivitateList).add(new Activitate(null, (JocDTO) o, ((JocDTO) o).getEchipe()));
                    }
                }
            } else {

                // se incarca doar activitatile pt animatorul logat
                for (ActivitateDTO a : activitatiCopy) {
                    for (int i = 0; i < a.getAnimatori().size(); ++i) {
                        if (a.getAnimatori().get(i).getNumeAnimator().equalsIgnoreCase(user.getUsername())) { // daca userul contribuie la activitatea a
                            mActivitateList.add(new Activitate(a, null, new ArrayList<>(a.getEchipe()))); // ii adaugam activitatea in lista
                            i = a.getAnimatori().size(); // incheiem loop ul
                        }
                    }

                }


                for (JocDTO j : jocuriCopy) {
                    for (int i = 0; i < j.getAnimatori().size(); ++i) {
                        if (j.getAnimatori().get(i).getNumeAnimator().equalsIgnoreCase(user.getUsername())) {
                            mActivitateList.add(new Activitate(null, j, new ArrayList<>(j.getEchipe())));
                            i = j.getAnimatori().size();
                        }
                    }
                }

                Collections.sort(mActivitateList, new Comparator<Activitate>() {
                    @Override
                    public int compare(Activitate activitate, Activitate t1) {
                        int ord1 = activitate.getActivitateDTO() != null ? activitate.getActivitateDTO().getOrdin() : activitate.getJocDTO().getOrdin();
                        int ord2 = t1.getActivitateDTO() != null ? t1.getActivitateDTO().getOrdin() : t1.getJocDTO().getOrdin();
                        return (ord1 - ord2);
                    }
                });
            }
        } else {

            // se incarca doar activitatile pt animatorul logat
            for (ActivitateDTO a : activitatiCopy) {
                for (int i = 0; i < a.getAnimatori().size(); ++i) {
                    if (a.getAnimatori().get(i).getNumeAnimator().equalsIgnoreCase(user.getUsername())) { // daca userul contribuie la activitatea a
                        mActivitateList.add(new Activitate(a, null, new ArrayList<>(a.getEchipe()))); // ii adaugam activitatea in lista
                        i = a.getAnimatori().size(); // incheiem loop ul
                    }
                }

            }


            for (JocDTO j : jocuriCopy) {
                for (int i = 0; i < j.getAnimatori().size(); ++i) {
                    if (j.getAnimatori().get(i).getNumeAnimator().equalsIgnoreCase(user.getUsername())) {
                        mActivitateList.add(new Activitate(null, j, new ArrayList<>(j.getEchipe())));
                        i = j.getAnimatori().size();
                    }
                }
            }

            Collections.sort(mActivitateList, new Comparator<Activitate>() {
                @Override
                public int compare(Activitate activitate, Activitate t1) {
                    int ord1 = activitate.getActivitateDTO() != null ? activitate.getActivitateDTO().getOrdin() : activitate.getJocDTO().getOrdin();
                    int ord2 = t1.getActivitateDTO() != null ? t1.getActivitateDTO().getOrdin() : t1.getJocDTO().getOrdin();
                    return (ord1 - ord2);
                }
            });
        }

        // se pastreaza activitatile pioritare dupa improspatare
        if (auxActivitateList != null) {
            for (Activitate a : mActivitateList) {
                for (Activitate a2 : auxActivitateList) {
                    if (a.getActivitateDTO() != null && a2.getActivitateDTO() != null &&
                        a.getActivitateDTO().getPerioada().equalsIgnoreCase(a2.getActivitateDTO().getPerioada()) &&
                        a.getActivitateDTO().getActivitateGenerala().getNumeActivitateGenerala().equalsIgnoreCase(a2.getActivitateDTO().getActivitateGenerala().getNumeActivitateGenerala())){


                        a.setDone(a2.isDone());
                        a.setHighPiority(a2.isHighPiority());
                        a.setSent(a2.isSent());
                        //a.setEchipe(a2.getEchipe());

                        boolean theSame = true;

                        if (a.getECHIPE().size() != a2.getECHIPE().size()) {
                            theSame = false;
                        }

                        // parcurgem toate echipele din distributia anterioara
                        for (EchipaDTO e : a2.getECHIPE()){
                            if (!a2.getEchipe().contains(e)){ // daca echipa a fost distribuita, dar a terminat jocul, este stearsa din noua distributie
                                a.removeEchipa(e);
                                theSame = false;
                            }
                        }

                        if (!theSame) {
                            a.setDone(false);
                            a.setSent(false);
                            //mActivitateCompletaList.remove(a);
                        }

                    } else if (a.getJocDTO() != null && a2.getJocDTO() != null &&
                               a.getJocDTO().getPerioada().equalsIgnoreCase(a.getJocDTO().getPerioada()) &&
                               a.getJocDTO().getJocGeneral().getNumeJocGeneral().equalsIgnoreCase(a2.getJocDTO().getJocGeneral().getNumeJocGeneral())) {
                        a.setDone(a2.isDone());
                        a.setHighPiority(a2.isHighPiority());
                        a.setSent(a2.isSent());
                        //a.setEchipe(a2.getEchipe());

                        boolean theSame = true;

                        if (a.getECHIPE().size() != a2.getECHIPE().size()) {
                            theSame = false;
                        }
                        // parcurgem toate echipele din distributia anterioara
                        for (EchipaDTO e : a2.getECHIPE()){
                            if (!a2.getEchipe().contains(e)){ // daca echipa a fost distribuita, dar a terminat jocul, este stearsa din noua distributie
                                a.removeEchipa(e);
                                theSame = false;
                            }
                        }


                        if (!theSame) {
                            a.setDone(false);
                            a.setSent(false);
                            //mActivitateCompletaList.remove(a);
                        }

                    }
                }
            }
        }


        if (mActivitateCompletaList != null) {
            // daca au venit echipe suplimentare la activitatile terminate ulterior, va trebui sa le eliminam
            List<Activitate> mActivitateCompletaListDeEliminat = new ArrayList<>();
            for (Activitate a : mActivitateCompletaList) {
                int delete = -1;

                for (int i = 0; i < mActivitateList.size() && delete == -1; ++i) {
                    Activitate a2 = mActivitateList.get(i);
                    if (a.getActivitateDTO() != null && a2.getActivitateDTO() != null &&
                        a.getActivitateDTO().getPerioada().equalsIgnoreCase(a2.getActivitateDTO().getPerioada()) &&
                        a.getActivitateDTO().getActivitateGenerala().getNumeActivitateGenerala().equalsIgnoreCase(a2.getActivitateDTO().getActivitateGenerala().getNumeActivitateGenerala())){

                        a.getActivitateDTO().setLocatie(a2.getActivitateDTO().getLocatie());
                        a.getActivitateDTO().setPost(a2.getActivitateDTO().getPost());
                        a.getActivitateDTO().setDetalii(a2.getActivitateDTO().getDetalii());

                        for (EchipaDTO e : a.getECHIPE()) {
                            a2.removeEchipa(e);
                        }
                        if (a2.getEchipe().size() == 0) {
                            delete = i;
                        } else {
                            a2.setDone(false);
                            a2.setSent(false);
                            mActivitateCompletaListDeEliminat.add(a); // activitatea a s-a prelungit prin a2, prin urmare trebuie eliminata
                                                                      // din lista activitatilor complete
                        }

                    } else if (a.getJocDTO() != null && a2.getJocDTO() != null &&
                            a.getJocDTO().getPerioada().equalsIgnoreCase(a.getJocDTO().getPerioada()) &&
                            a.getJocDTO().getJocGeneral().getNumeJocGeneral().equalsIgnoreCase(a2.getJocDTO().getJocGeneral().getNumeJocGeneral())) {

                        a.getJocDTO().setLocatie(a2.getJocDTO().getLocatie());
                        a.getJocDTO().setPost(a2.getJocDTO().getPost());
                        a.getJocDTO().setDetalii(a2.getJocDTO().getDetalii());

                        for (EchipaDTO e : a.getECHIPE()) {
                            a2.removeEchipa(e);
                        }
                        if (a2.getEchipe().size() == 0) {
                            delete = i;
                        } else {
                            a2.setDone(false);
                            a2.setSent(false);
                            mActivitateCompletaListDeEliminat.add(a); // activitatea a s-a prelungit prin a2, prin urmare trebuie eliminata
                                                                      // din lista activitatilor complete
                        }

                    }

                }

                if (delete != -1) {
                    mActivitateList.remove(delete);
                }
            }

        }


        mTouchListener = new TouchListener(this, lvActivitate);
        mSecondTouchListener = new SecondTouchListener(this, lvActivitate);
        Controller.getInstance().setmTouchListener(mTouchListener);

        activitatiCompleteAdaptor = new ActivitatiCompleteListAdaptor(this, mActivitateCompletaList, clickListener, mSecondTouchListener, lvActivitate);
        adapter = new ActivitatiListAdapter(this, mActivitateList, activitatiCompleteAdaptor, mTouchListener,
                clickListener, longClickListener, lvActivitate, null);
        animatoriAdapter = new AnimatoriAdapter(this,
                                                animatori,
                                                clickListener);
        Controller.getInstance().setAdapterActivitati(adapter);
        Controller.getInstance().setAdapterActivitatiComplete(activitatiCompleteAdaptor);

        adapter.refresh();
        lvActivitate.setAdapter(adapter);
        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.refresh));


        adapter.notifyDataSetChanged();
        adapter.setFab(fab);
        navigationView.setCheckedItem(R.id.nav_gallery);

        onNavigationItemSelected(navigationView.getMenu().getItem(1));

        if (!timer.isExecuting()) {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB)
                timer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                timer.execute();
        }

        serializare();

        Snackbar.make(fab, "Reimprospatat", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingGif.setVisibility(View.INVISIBLE);
            }
        }, 1500);
        //return (ArrayList<Activitate>) mActivitateList;
    }

    @Override
    protected void onResume () {
        super.onResume();

        // daca nu exista toate resursele necesare, este cel mai indicat sa oprim activitatea
        if (!checkResources()) {
            finish();
            return;
        }

        try { adapter.refresh(); } catch (Exception e) {}
        isPaused = false;
        if (lvActivitate.getAdapter() instanceof ActivitatiListAdapter) {
            navigationView.setCheckedItem(R.id.nav_gallery);
        } else if (lvActivitate.getAdapter() instanceof ActivitatiCompleteListAdaptor) {
            navigationView.setCheckedItem(R.id.nav_manage);
        }

        Controller.getInstance().setCurrentActivity(this);
        //Controller.getInstance().sendMessage(Message.IS_SOMETHING_TO_RECIEVE);
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1); // 1 = ACTIVITATI_NOTIFICATION_ID

        if (Controller.getInstance().getReceiver() != null){
            if (!Controller.getInstance().getReceiver().getListeners().contains(this)) {
                Controller.getInstance().getReceiver().addListener(this);
            }
        }

        if (!madeTutorial) {
            boolean madeTutorialBefore = Controller.getInstance().wasActivityUsedBefore(ClassIDs.MAIN_CONTENT, this);
            if (!madeTutorialBefore) {
                Controller.getInstance().setFirstUseActivity(ClassIDs.MAIN_CONTENT, true, this);
                makeTutorial();
            }
            madeTutorial = true;
        }

    }

    @Override
    protected void onDestroy () {
        super.onDestroy();

        Controller.getInstance().dateNeedUpdate();
        if (Controller.getInstance().getReceiver() != null){
            Controller.getInstance().getReceiver().removeListener(this);
        }
        if (!Controller.getInstance().isOffline()) {
            try { Controller.getInstance().getClient().sendData(Message.DISCONNECT_USER); } catch (Exception e) {}
            Controller.getInstance().setUser(null); // indicam ca userul este deconectat
        }
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1); // ACTIVITATI_NOTIFICATION_ID
        //finish();
        //serializare(); // cat timp exista evenimentul onPause, serializarea in onDestroy nu-si mai are rostul
    }

    @Override
    protected void onPause () {
        super.onPause();
        isPaused = true;
        serializare();
    }

    /*
    *  Prin apelarea metodei, se verifica toate resursele necesare inceperii activitatii
    *  Daca toate datele sunt prezente, metoda intoarce valoarea true, care indica posibilitatea
    *  rularii activitatii.
    *  Incaz contrar, metoda intoarcevaloarea false, si se recomanda iesirea din activitate
    *
     */
    private boolean checkResources () {
        if (user == null || Controller.getInstance().getUser() == null) {
            return false;
        } else if (user == null) {
            user = Controller.getInstance().getUser();
        }

        if (mActivitateList == null) {
            if (Controller.getInstance().isOffline()) {
                return false;
            }
        }

        if (mActivitateCompletaList == null) {
            if (Controller.getInstance().isOffline()) {
                return false;
            }
        }

        return true;
    }

    public void serializare () {
        if (user == null) user = Controller.getInstance().getUser();
        if (user == null) {
            Log.i("serializare", "Salvarea a esuat: utilizator null");
            return;
        }
        try {
            FileOutputStream fout = MyApplication.getMyApplicationContext().openFileOutput(user.getUsername() + "_" + SERIAZLIZED_LIST_FILE_SUF, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(timer.getExpireDate());
            out.flush();
            out.writeObject(activitati);
            out.flush();
            out.writeObject(jocuri);
            out.flush();
            out.writeObject((ArrayList<Activitate>) mActivitateList);
            out.flush();
            out.writeObject(mActivitateCompletaList);
            out.flush();

            out.close();
            fout.close();

            Log.i("serialization", "Serialization completed");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deserializare () {
        // read list from file
        user = Controller.getInstance().getUser(); //////////////////////////////////////////////////////////////// avertizare user
        if (user == null) {
            Log.i("deserializare", "Deserializarea a esuat a esuat: utilizator null");
            return;
        }
        try {
            /*File [] files = getApplicationContext().getFilesDir().listFiles();
            for (int i = 0; i < 52; ++i) {
                files[i].delete();
            }*/
            FileInputStream fin = MyApplication.getMyApplicationContext().openFileInput(user.getUsername() + "_" + SERIAZLIZED_LIST_FILE_SUF);

            ObjectInputStream in = new ObjectInputStream(fin);
            Date expDate = (Date) in.readObject();
            Date curDate = Calendar.getInstance().getTime();

            if (expDate == null) { // in cazul in care nu avem o data de expirare, presupunem ca programul
                                  // a expirat astazi, la ora 00:00
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                expDate = calendar.getTime();
            }

            this.expDate = expDate;
            timer.setDataExpirare(expDate);

            if (curDate.before(expDate) || !Controller.getInstance().hasUpdatedExpireDate()) {
                activitati = (List<ActivitateDTO>) in.readObject();
                jocuri = (List<JocDTO>) in.readObject();
                mActivitateList = (ArrayList<Activitate>) in.readObject();
                mActivitateCompletaList = (ArrayList<Activitate>) in.readObject();
                Log.i("serialization", "Deserialization completed");

                if (Controller.getInstance().isOffline()) {
                    if (activitati == null) activitati = new ArrayList<>();
                    if (jocuri == null) jocuri = new ArrayList<>();
                    createActivitati();
                } else {
                    activitati = null;
                    jocuri = null;
                    expDate = null;
                }
            } else {

                if (Controller.getInstance().isOffline()) {
                    activitati = new ArrayList<>();
                    jocuri = new ArrayList<>();
                    createActivitati();
                }
            }

            in.close();
            fin.close();


        } catch (Exception e) {
            e.printStackTrace();
            if (Controller.getInstance().isOffline()) {
                activitati = new ArrayList<>();
                jocuri = new ArrayList<>();
                createActivitati();
            }
        }
        if (FloatingWindow.isLoadingScreenVisible()) {
            Controller.getInstance().hideLoadingScreenDelayed(500);
        }
    }

    public void golesteProgramul () {
        mActivitateList = null;
        Iterator<Activitate> it = mActivitateCompletaList.iterator();
        while (it.hasNext()) {
            Activitate a = it.next();
            if (!isPenalizare(a)) {
                it.remove();
            } else if (a.isSent()) {
                it.remove();
            }
        }
        serializare();
    }

    ///////////////////////////////////
    private void busesteFisier () {
        try {
            FileOutputStream fout = MyApplication.getMyApplicationContext().openFileOutput(user.getUsername() + "_" + SERIAZLIZED_LIST_FILE_SUF, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(1.4325);
            out.flush();

            out.close();
            fout.close();
        }catch (Exception e) {

        }
    }

    private List<Activitate> getActivitatiByAnimator (AnimatorDTO animatorDTO) {
        List<Activitate> mActivitateList = new ArrayList<>();

        for (ActivitateDTO a : activitati){
            for (int i = 0; i < a.getAnimatori().size(); ++i) {
                if (a.getAnimatori().get(i).getNumeAnimator().equalsIgnoreCase(animatorDTO.getNumeAnimator())){ // daca userul contribuie la activitatea a
                    mActivitateList.add(new Activitate(a, null, a.getEchipe())); // ii adaugam activitatea in lista
                    i = a.getAnimatori().size(); // incheiem loop ul
                }
            }

        }


        for (JocDTO j : jocuri) {
            for (int i = 0; i < j.getAnimatori().size(); ++i) {
                if (j.getAnimatori().get(i).getNumeAnimator().equalsIgnoreCase(animatorDTO.getNumeAnimator())) {
                    mActivitateList.add(new Activitate(null, j, j.getEchipe()));
                    i = j.getAnimatori().size();
                }
            }
        }

        return mActivitateList;
    }

    private void buildNotification (String text) {
        Intent intent = new Intent(MyApplication.getMyApplicationContext(), Controller.getInstance().getCurrentActivity().getClass());
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        intent.putExtras(bundle);

        //
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.getMyApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Eco Aventura")
                        .setContentText(text)
                        .setContentIntent(pendingIntent);

        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setAutoCancel(true);
    }

    private void showNotification (NotificationID id) {
        int ID = id == NotificationID.ACTIVITATI_NOTIFICATION_ID ? 1 : 2;
        Notification notification = mBuilder.build();
        //notification.flags = Notification.FLAG_ONGOING_EVENT;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID, notification);
    }

    public static List<ActivitateDTO> copyActivitati (List<ActivitateDTO> activitati) {
        List<ActivitateDTO> copy = new ArrayList<>();
        if (activitati == null) return copy;

        for (ActivitateDTO a : activitati) {
            ActivitateDTO aCopy = new ActivitateDTO();
            aCopy.setActivitateGenerala(a.getActivitateGenerala());
            aCopy.setAnimatori(a.getAnimatori());
            aCopy.setDetalii(a.getDetalii());
            aCopy.setEchipe(new ArrayList<>(a.getEchipe()));
            aCopy.setId(a.getId());
            aCopy.setLocatie(a.getLocatie());
            aCopy.setOrdin(a.getOrdin());
            aCopy.setPerioada(a.getPerioada());
            aCopy.setPost(a.getPost());
            aCopy.setIdProgram(a.getIdProgram());
            copy.add(aCopy);
        }

        return copy;
    }

    public static List<JocDTO> copyJocuri (List<JocDTO> jocuri) {
        List<JocDTO> copy = new ArrayList<>();
        if (jocuri == null) return copy;

        for (JocDTO j : jocuri) {
            JocDTO jCopy = new JocDTO();
            jCopy.setPost(j.getPost());
            jCopy.setPerioada(j.getPerioada());
            jCopy.setLocatie(j.getLocatie());
            jCopy.setOrdin(j.getOrdin());
            jCopy.setAllowsAnimatorPrincipal(j.isAllowsAnimatorPrincipal());
            jCopy.setAnimatori(j.getAnimatori());
            jCopy.setDetalii(j.getDetalii());
            jCopy.setEchipe(new ArrayList<>(j.getEchipe()));
            jCopy.setEchipeAbsente(j.getEchipeAbsente());
            jCopy.setFormulas(j.getFormulas());
            jCopy.setId(j.getId());
            jCopy.setPunctaj(j.getPunctaj());
            jCopy.setJocGeneral(j.getJocGeneral());
            jCopy.setIdProgram(j.getIdProgram());
            copy.add(jCopy);
        }
        return copy;
    }

    public static ActivitateDTO copyActivitate (ActivitateDTO a) {
        if (a == null) return null;
        ActivitateDTO aCopy = new ActivitateDTO();
        aCopy.setActivitateGenerala(a.getActivitateGenerala());
        aCopy.setAnimatori(a.getAnimatori());
        aCopy.setDetalii(a.getDetalii());
        aCopy.setEchipe(new ArrayList<>(a.getEchipe()));
        aCopy.setId(a.getId());
        aCopy.setLocatie(a.getLocatie());
        aCopy.setOrdin(a.getOrdin());
        aCopy.setPerioada(a.getPerioada());
        aCopy.setPost(a.getPost());
        aCopy.setIdProgram(a.getIdProgram());
        return aCopy;
    }

    public static JocDTO copyJoc (JocDTO j){
        if (j == null) return j;
        JocDTO jCopy = new JocDTO();
        jCopy.setPost(j.getPost());
        jCopy.setPerioada(j.getPerioada());
        jCopy.setLocatie(j.getLocatie());
        jCopy.setOrdin(j.getOrdin());
        jCopy.setAllowsAnimatorPrincipal(j.isAllowsAnimatorPrincipal());
        jCopy.setAnimatori(j.getAnimatori());
        jCopy.setDetalii(j.getDetalii());
        jCopy.setEchipe(new ArrayList<>(j.getEchipe()));
        jCopy.setEchipeAbsente(j.getEchipeAbsente());
        jCopy.setFormulas(j.getFormulas());
        jCopy.setId(j.getId());
        jCopy.setPunctaj(j.getPunctaj());
        jCopy.setJocGeneral(j.getJocGeneral());
        jCopy.setIdProgram(j.getIdProgram());
        return jCopy;
    }

    public boolean isPenalizare (Activitate a ) {
        if (a.getJocDTO() == null) return false;
        return (a.getJocDTO().getJocGeneral().toString().equalsIgnoreCase("penalizare"));
    }

    private Activitate toSend;
    final Handler myHandler = new Handler(
            new Handler.Callback() {
                @Override
                public boolean handleMessage(android.os.Message message) {
                    switch (message.what) {
                        case SEND :
                            List<Activitate> list = new ArrayList<>();
                            list.add(toSend);
                            Controller.getInstance().sendData(Controller.getInstance().pack(list));

                            new Thread(new gui.MainContent.Temporizator(myHandler, 7000)).start();
                            waitingHandler = myHandler;
                            isWaitingReceivedConfirm = true;

                            Snackbar.make(fab, "Se asteapta confirmarea serverului...", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("SendInfo", null).show();
                            break;
                        case SUCCES :
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                                if (toSend.getJocDTO() != null) {
                                    if (toSend.getJocDTO().getJocGeneral().toString().equalsIgnoreCase("penalizare")) {
                                        try {
                                            int p = toSend.getJocDTO().getPunctaj();
                                            toSend.getJocDTO().setPunctaj(p*2);
                                            toSend.getJocDTO().setLocatie(" " + p*2);
                                            toSend.getJocDTO().setDetalii("Penalizare : " + p*2);
                                        } catch (Exception e) {}
                                    }
                                }
                            }
                            progressDialog = null;
                            fab.setEnabled(true);
                            //lvActivitate.requestDisallowInterceptTouchEvent(false);
                            enableDisableView(lvActivitate, true);
                            toSend.setSent(true);
                            activitatiCompleteAdaptor.notifySendOneActivitate();
                            activitatiCompleteAdaptor.notifyDataSetChanged();
                            Snackbar.make(fab, "Trimis!", Snackbar.LENGTH_SHORT)
                                    .setAction("SendInfo", null).show();
                            break;
                        case FAIL :
                            if (progressDialog != null) progressDialog.dismiss();
                            progressDialog = null;
                            fab.setEnabled(true);
                            //lvActivitate.requestDisallowInterceptTouchEvent(false);
                            enableDisableView(lvActivitate, true);
                            Snackbar.make(fab, "Esuat: Conexiune instabila", Snackbar.LENGTH_SHORT)
                                    .setAction("SendInfo", null).show();
                            break;
                    }
                    return true;
                }
            }
    );
    public void sendActivitate (final Activitate activitate) {
        toSend = activitate;
        if (activitate.isSent()) {
            Snackbar.make(fab, "Activitate deja trimisa", Snackbar.LENGTH_SHORT)
                    .setAction("Retrimite", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (timer.hasExpired() && !isPenalizare(activitate)) {
                                Snackbar.make(fab, "Esuat: program incheiat.", Snackbar.LENGTH_SHORT)
                                        .show();
                                return;
                            }

                            if (Controller.getInstance().isOffline()) {
                                Snackbar.make(fab, "Esuat: esti Offline.", Snackbar.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                            activitate.setSent(false);
                            if (activitate.getActivitateDTO() != null) {
                                activitate.getActivitateDTO().setEchipe(new ArrayList<>(activitate.getECHIPE()));
                            } else {
                                activitate.getJocDTO().setEchipe(new ArrayList<>(activitate.getECHIPE()));
                            }
                            activitatiCompleteAdaptor.notifyDataSetChanged();
                            sendActivitate(activitate);
                            progressDialog = ProgressDialog.show(MainContent.this, "", "Se retrimite activitatea...");
                            new temporizator.Temporizator(new Event() {
                                @Override
                                public void doAction(Object... obj) {
                                    try {
                                        progressDialog.setCanceledOnTouchOutside(true);
                                    } catch (Exception e){}
                                }
                            }, 8, temporizator.Temporizator.SECONDS).doScheduledEvent();
                        }
                    }).show();
            return;
        }


        if (timer.hasExpired() && !isPenalizare(activitate)) {
            Snackbar.make(fab, "Esuat: program incheiat.", Snackbar.LENGTH_SHORT)
                    .setAction("Notify", null).show();
            if (progressDialog != null) progressDialog.dismiss();
            return;
        }


        Snackbar.make(fab, "Se asteapta raspuns de la server...", Snackbar.LENGTH_INDEFINITE)
                .setAction("Wait", null).show();

        //lvActivitate.requestDisallowInterceptTouchEvent(true);
        enableDisableView(lvActivitate, false);

        fab.setEnabled(false);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            checkerActive = true;
                            isWaitingConfirm = true;
                            Controller.getInstance().sendMessage(Message.TEST_CONNECTION);

                            if (Controller.getInstance().isOffline())  {
                                android.os.Message msg = myHandler.obtainMessage();
                                msg.what = FAIL;
                                myHandler.sendMessage(msg);
                                throw new Exception();
                            }

                            long start = System.currentTimeMillis();
                            long end = start + 10*1000; // 10 seconds * 1000 ms/sec
                            while (isWaitingConfirm && !Controller.getInstance().isOffline() && System.currentTimeMillis() < end){}

                            if (System.currentTimeMillis() > end){
                                Controller.getInstance().setOffline(true);
                                throw new Exception();
                            }
                            Controller.getInstance().sendMessage(Message.IS_SOMETHING_TO_SEND);
                            android.os.Message msg = handler.obtainMessage();
                            msg.what = SEND;
                            myHandler.sendMessage(msg);
                        } catch (Exception e) {
                            android.os.Message msg = handler.obtainMessage();
                            msg.what = FAIL;
                            myHandler.sendMessage(msg);
                        }
                        checkerActive = false;
                    }
                }
        ).start();
    }

    private int steps;
    private final static int TOTAL_STEPS = 2;

    private void makeTutorial () {
        steps = 0;

        navigationTarget = new Target() {
            @Override
            public Point getPoint() {
                return new Point((int)toolbar.getX(), (int)toolbar.getY());
            }
        };

        fabTarget = new ViewTarget(fab);

        showcase = new ShowcaseView.Builder(this)
                .setContentTitle("Panou activitati")
                .setContentText("Aici poti vedea toate activitatile pe care le ai de indeplinit.")
                .setStyle(R.style.TransparentStyle)
                .setTarget(Target.NONE)
                .hideOnTouchOutside()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickPerformed(view);
                    }
                })
                .build();


        showcase.setBlocksTouches(true);
        showcase.setButtonText("Urmatorul");
        showcase.show();
    }

    private void clickPerformed (View view) {
        switch (steps) {
            case 0 :

                showcase.setShowcase(fabTarget, true);
                if (lvActivitate.getAdapter() != null)
                    if (lvActivitate.getAdapter().getClass() == ActivitatiCompleteListAdaptor.class) {
                        showcase.setContentTitle("Trimite activitati");
                        showcase.setContentText("Trimite pachetul de activitati finalizate catre Server.");
                        break;
                    }
                showcase.setContentTitle("Reimprospateaza");
                showcase.setContentText("Actualizeaza seria si planul de activitati actual.");
                break;
            case 1 :
                showcase.setShowcase(navigationTarget, true);
                showcase.setContentTitle("Meniu optiuni");
                showcase.setContentText("Deschide meniul in care sunt afisate alte optiuni utile ale aplicatiei.");
                showcase.setButtonText("Gata");
                break;
            default :
                showcase.hide();
                if (lvActivitate.getAdapter() != null) {
                    if (lvActivitate.getAdapter().getCount() != 0) {
                        if (lvActivitate.getAdapter().getClass() == ActivitatiListAdapter.class) {
                            adapter.showTutorial(this);
                        } else if (lvActivitate.getAdapter().getClass() == ActivitatiCompleteListAdaptor.class) {
                            activitatiCompleteAdaptor.showTutorial(this);
                        } else if (lvActivitate.getAdapter().getClass() == AnimatoriAdapter.class) {
                            animatoriAdapter.showTutorial(this, lvActivitate);
                        }
                    }
                }
        }
        steps++;
    }

    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if ( view instanceof ViewGroup ) {
            ViewGroup group = (ViewGroup)view;

            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

    private class Temporizator implements Runnable {
        final private Handler handler;
        final private int timeOut;

        public Temporizator (Handler handler, int timeOut) {
            this.handler = handler;
            this.timeOut = timeOut;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(timeOut);
            } catch (InterruptedException e) {
                e.printStackTrace();
                android.os.Message message = handler.obtainMessage();
                message.what = FAIL;
                handler.sendMessage(message);
            }
            if (isWaitingReceivedConfirm) {
                isWaitingReceivedConfirm = false;
                android.os.Message message = handler.obtainMessage();
                message.what = FAIL;
                handler.sendMessage(message);
            }
        }
    }
}
