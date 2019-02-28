package com.connection.simpleclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.BoolRes;
import android.support.design.widget.Snackbar;
import android.app.AlertDialog;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import adapters.ActivitatiCompleteListAdaptor;
import adapters.ActivitatiListAdapter;
import adapters.SetariAdapter;
import dto.ActivitateDTO;
import dto.AnimatorDTO;
import dto.EchipaDTO;
import dto.JocDTO;
import dto.JoinDTO;
import dto.MembruEchipaDTO;
import dto.Message;
import dto.UserDTO;
import email.GMailSender;
import floatingWindow.FloatingWindow;
import gui.Login;
import gui.MainActivity;
import gui.MainContent;
import receiver.DataListener;
import receiver.Receiver;
import temporizator.Event;

/**
 * Created by Alexandru on 8/29/2016.
 */
public class Controller {
    private final static String [] messages = new String [] {
            "Plantez copacei...",
            "La cules de fragi si mure...",
            "Ma uit atent cum creste iarba...",
            "Shht, vanez strumfi...",
            "Numar punctele negre de pe tavan...",
            "Calculez volumul soarelui...",
            "Se stabileste o legatura...cu extraterestrii..."
    };
    private final static String SERIALIZED_USERS_PATH = "users.srz";
    private final static String SERIALIZED_SERIE_PATH = "serie.srz";
    private final static String SERIALIZED_SOCKET_PATH = "socket.srz";
    private final static String SERIALIZED_USER_PATH = "user.srz";
    private final static String CRUSH_REPORTS = "crushes.srz";
    private final static String ID_PROGRAM = "id.srz";
    private final static String FIRST_USE = "first.srz";
    private final static String REGISTRATION_CODE = "registration.srz";
    public final static String AUTOCONECTARE = "autoconectare.srz";
    public final static String SERVER_KEY = "key.txt";
    private static Controller singleton = null;
    private Context applicationContext;
    private Receiver receiver;
    private TCPClient client;
    private MainActivity mainActivity;
    private UserDTO user;
    private ActivitatiListAdapter adapterActivitati;
    private ActivitatiCompleteListAdaptor adapterActivitatiComplete;
    private int nrSerie = -1;
    private int lastColor = -1;
    private List<AnimatorDTO> animatori;
    private Random r = new Random();
    private Activity currentActivity;
    private SetariAdapter setariAdapter;
    private TouchListener mTouchListener;
    private View.OnClickListener clickListener;
    private int [] colors;
    private GMailSender gMailSender;
    private HashMap<ClassIDs, Boolean> firstUsedMap;
    private static boolean expiredDateUpdated = false;
    private AlertDialog.Builder loadingDialog;
    private Event saveEvent;
    private HashMap<String, String> crushRegister;

    private boolean isOffline;

    private Controller () {
        colors = new int[9];
        colors[0] = Color.rgb(198, 157, 75);
        colors[1] = Color.rgb(207, 133, 44);
        colors[2] = Color.rgb(184, 60, 54);
        colors[3] = Color.rgb(142,142,141);
        colors[4] = Color.rgb(146,143,115);
        colors[5] = Color.rgb(69,88,134);
        colors[6] = Color.rgb(106,40,96);
        colors[7] = Color.rgb(111,0,17);
        colors[8] = Color.rgb(80, 105, 66);

        gMailSender = new GMailSender("ecoaventuracrushlogger@gmail.com", "Wanted2016");
    }

    public static Controller getInstance () {
        if (singleton == null) {
            singleton = new Controller();
        }
        return singleton;
    }

    public GMailSender getMailSender () {
        return gMailSender;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public TouchListener getmTouchListener() {
        return mTouchListener;
    }

    public void setmTouchListener(TouchListener mTouchListener) {
        this.mTouchListener = mTouchListener;
    }

    public SetariAdapter getSetariAdapter() {
        return setariAdapter;
    }

    public void setSetariAdapter(SetariAdapter setariAdapter) {
        this.setariAdapter = setariAdapter;
    }

    public Context getApplicationContext() {
        return MyApplication.getMyApplicationContext();
    }

    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }


    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setReceiver(Receiver receiver){
        this.receiver = receiver;
        ConnectionController.getInstance().addListener(receiver);
    }

    public void setClient (TCPClient client){
        this.client = client;
    }

    public Receiver getReceiver(){
        return receiver;
    }

    public TCPClient getClient () {
        return client;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean offline) {
        if (offline == isOffline) return;

        this.isOffline = offline;
        if (isOffline) {
            display("Offline");
            if (ConnectionController.getInstance().isAutoconectare()) {
                ConnectionController.getInstance().connectToServer();
            } else {
                ConnectionController.getInstance().connectToServerManually();
            }
        } else {
            display("Online");
            if (currentActivity instanceof MainActivity) { // daca ne aflam in activitate principala, trebuie sa trecem la urmatoarea activitate
                try {
                    ((MainActivity) currentActivity).nextActivity();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (user != null) {
                try {
                    ConnectionController.getInstance().connectUser(user.getUsername(), user.getParola());
                } catch (IOException e) {
                    user = null;
                    e.printStackTrace();
                }
            }
        }
    }

    public void setUser (UserDTO user) {
        this.user = user;
        if (currentActivity instanceof Login && user != null) { // trebuie sa comutam pe urmatoarea activitate
            ((Login) currentActivity).nextActivity(user);
        }
        display("Online");
    }

    public UserDTO getUser () { return this.user; }

    public void inregistrare (String username, String parola) throws IOException {
        if (isOffline()) return;

        ObjectOutputStream out = client.getOut();
        //out.writeObject(Message.LOGIN_USER);
        client.sendData(Message.LOGIN_USER);
        UserDTO user = new UserDTO();
        user.setUsername(username);
        user.setParola(parola);
        user.setAcces(UserDTO.ACCES_ANIMATOR);

        //out.writeObject(user);
        client.sendData(user);
    }

    public UserDTO conectare (String username, String parola) throws  IOException {
        if (isOffline()) return null;

        ObjectOutputStream out = client.getOut();
        //out.writeObject(Message.CONNECT_USER);
        client.sendData(Message.CONNECT_USER);
        UserDTO user = new UserDTO();
        user.setUsername(username);
        user.setParola(parola);
        user.setAcces(UserDTO.ACCES_ANIMATOR);

        //out.writeObject(user);
        client.sendData(user);
        return user;
    }



    public void deconectare () {
        if (!isOffline) {
            try {
                sendMessage(Message.DISCONNECT_USER);
            } catch (Exception e) {}
        }
    }

    public void sendMessage (Message m) {
        if (!isOffline())
            client.sendData(m);
    }

    public void sendData (Object... objs){
        if (!isOffline() && client != null) {
            client.sendData(objs);
        }
    }

    private void serializeUsers (List<UserDTO> users) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(
                    applicationContext.openFileOutput(SERIALIZED_USERS_PATH, Context.MODE_PRIVATE)
            );
            output.writeObject(users);
            output.flush();
            output.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<UserDTO> deserializeUsers () {
        try {
            ObjectInputStream input = new ObjectInputStream(
                    applicationContext.openFileInput(SERIALIZED_USERS_PATH)
            ) ;

            return (List<UserDTO>) input.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public void serializeUser (UserDTO user) {
        List<UserDTO> users = deserializeUsers();
        if (users == null) {
            users = new ArrayList<>();
        }
        if (!users.contains(user)) {
            users.add(user);
            serializeUsers(users);
        } else {
            UserDTO u = users.get(users.indexOf(user));
            if (!u.getParola().equals(user.getParola()) || u.getAcces() != user.getAcces() ||
                    u.getId() != user.getId()){
                users.remove(u);
                users.add(user);
                serializeUsers(users);
            }
        }

    }

    public UserDTO conecteazaUserOffline (String username, String parola) {
        List<UserDTO> users = deserializeUsers();
        try {
            for (int i = 0; i < users.size(); ++i) {
                if (users.get(i) == null) {
                    users.remove(i);
                    serializeUsers(users);
                }
                if (users.get(i).getUsername().equalsIgnoreCase(username)) {
                    if (users.get(i).getParola().equals(parola)) {
                        setUser(users.get(i));
                        return users.get(i);
                    }
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }



    public void salveazaEchipe (List<EchipaDTO> echipe, int nrSerie) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    applicationContext.openFileOutput(SERIALIZED_SERIE_PATH, Context.MODE_PRIVATE)
            );

            out.writeObject(nrSerie);
            out.flush();
            out.writeObject(echipe);
            out.flush();

            out.close();

            Log.i("Serialization", "Echipele au fost serializate");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<EchipaDTO> getEchipe () {
        try {
            ObjectInputStream in = new ObjectInputStream(
                    applicationContext.openFileInput(SERIALIZED_SERIE_PATH)
            );

            int nrSerie = (Integer) in.readObject();
            List<EchipaDTO> echipe = (List<EchipaDTO>) in.readObject();
            in.close();
            return echipe;
        } catch (Exception e) {
            e.printStackTrace();
            //sendMessage(Message.SENDING_SERIE);
            return null;
        }
    }

    public List<MembruEchipaDTO> getMembri () throws Exception{
        List<EchipaDTO> echipe = getEchipe();
        List<MembruEchipaDTO> membri = new ArrayList<>();

        for (EchipaDTO e : echipe) {
            membri.addAll(e.getMembriEchipa());
        }
        return membri;
    }

    public int getNrSerie () {
        //just for debug
        //nrSerie = 1;
        //

        if (nrSerie > 0) return nrSerie;

        try {
            ObjectInputStream in = new ObjectInputStream(
                    applicationContext.openFileInput(SERIALIZED_SERIE_PATH)
            );

            int nrSerie = (Integer) in.readObject();
            in.close();
            return nrSerie;
        } catch (Exception e) {
            return -1;
        }
    }

    public void setNrSerie (int nrSerie) {
        this.nrSerie = nrSerie;
    }

    public ActivitatiListAdapter getAdapterActivitati() {
        return adapterActivitati;
    }

    public void setAdapterActivitati(ActivitatiListAdapter adapterActivitati) {
        this.adapterActivitati = adapterActivitati;
    }

    public ActivitatiCompleteListAdaptor getAdapterActivitatiComplete() {
        return adapterActivitatiComplete;
    }

    public void setAdapterActivitatiComplete(ActivitatiCompleteListAdaptor adapterActivitatiComplete) {
        this.adapterActivitatiComplete = adapterActivitatiComplete;
    }

    public List<JoinDTO> pack (List<Activitate> actComplete) {
        List<JoinDTO> joins = new ArrayList<>();

        for (Activitate a : actComplete) {
            if (!a.isSent()) {
                joins.add(new JoinDTO(
                        MainContent.copyActivitate(a.getActivitateDTO()),
                        MainContent.copyJoc(a.getJocDTO())));

                if (a.getJocDTO() != null && a.getJocDTO().getJocGeneral().toString().equals("penalizare")) continue;

                a.getEchipe().clear();
            }
        }

        return joins;
    }

    public int getRandomColor () {
        int[] colors = new int[]{Color.CYAN, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.RED};

        int x;
        do {
            x = r.nextInt(colors.length);
        } while (x == lastColor);

        lastColor = x;
        return colors[x];

    }

    public List<AnimatorDTO> getAnimatori (List<ActivitateDTO> activitati, List<JocDTO> jocuri, boolean newDataSet){
        if (animatori != null && ! newDataSet) {
            return animatori;
        }

        List<AnimatorDTO> animtori = new ArrayList<>();

        for (ActivitateDTO a : activitati) {
            for (AnimatorDTO anim : a.getAnimatori()) {
                if (!animtori.contains(anim)){
                    animtori.add(anim);
                }
            }
        }

        for (JocDTO j : jocuri) {
            for (AnimatorDTO anim : j.getAnimatori()) {
                if (!animtori.contains(anim)) {
                    animtori.add(anim);
                }
            }
        }

        this.animatori = animtori;
        return animtori;
    }

    public List<AnimatorDTO> getAnimatori () {
        return this.animatori;
    }

    public boolean saveSocket (String ip, int port) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(applicationContext.openFileOutput(SERIALIZED_SOCKET_PATH, Context.MODE_PRIVATE));
            out.writeObject(ip);
            out.flush();
            out.writeObject((Integer) port);
            //out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Object [] getSocket () {
        try {
            ObjectInputStream in = new ObjectInputStream(applicationContext.openFileInput(SERIALIZED_SOCKET_PATH));
            Object [] objs = new Object[2];
            objs[0] = in.readObject();
            objs[1] = in.readObject();

            return objs;
        } catch (Exception e){
            return new Object[] {"", ""};
        }
    }

    public boolean saveLastUser (String username, String parola, boolean remember) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    applicationContext.openFileOutput(SERIALIZED_USER_PATH, Context.MODE_PRIVATE)
            );

            out.writeObject(username);
            out.flush();
            out.writeObject(parola);
            out.flush();
            out.writeObject(remember);
            out.flush();
            //out.close();
            return true;
        } catch (Exception e) {
            Log.i("serializare", e.getMessage());
            return false;
        }
    }

    public Object [] getLastUser () {
        try {
            ObjectInputStream in = new ObjectInputStream(applicationContext.openFileInput(SERIALIZED_USER_PATH));
            Object [] objs = new Object[3];

            objs[0] = in.readObject();
            objs[1] = in.readObject();
            objs[2] = in.readObject();
            return objs;
        } catch (Exception e) {
            return null;
        }
    }

    private String lastMsg = "";
    private long when = -1;
    private static final long MIN_TIME = 5000; // 5 sec
    public void display (String msg) {
        try {
            if (msg.equals(lastMsg)) {
                if (when != -1 && System.currentTimeMillis() - when < MIN_TIME) {
                    return;
                } else {
                    when = System.currentTimeMillis();
                }
            } else {
                lastMsg = msg;
                when = System.currentTimeMillis();
            }

            if (msg.equalsIgnoreCase("online") && isOffline) return;
            if (msg.equalsIgnoreCase("offline") && isOffline && lastMsg.equalsIgnoreCase("offline")) return;

            if (currentActivity != null) {
                Toast.makeText(MyApplication.getMyApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {}
    }

    public EchipaDTO makeCopy (EchipaDTO e) {
        EchipaDTO newE = new EchipaDTO(e.getId());
        newE.setActivitatiEchipa(e.getActivitatiEchipa());
        newE.setCuloareEchipa(e.getCuloareEchipa());
        newE.setJocuriEchipa(e.getJocuriEchipa());
        newE.setMembriEchipa(e.getMembriEchipa());
        newE.setNumeEchipa(e.getNumeEchipa());
        newE.setProfEhipa(e.getProfEhipa());
        newE.setScoalaEchipa(e.getScoalaEchipa());
        newE.setSerie(e.getSerie());

        return newE;
    }

    public void serializeAutoconectareButton (boolean b) {
        try {
            FileOutputStream fout = MyApplication.getMyApplicationContext().openFileOutput(AUTOCONECTARE, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(b);
            out.flush();
            out.close();
            fout.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean deserializeAutoconectareButton () {
        try {
            FileInputStream fin = MyApplication.getMyApplicationContext().openFileInput(AUTOCONECTARE);
            ObjectInputStream in = new ObjectInputStream(fin);
            boolean b = (Boolean) in.readObject();
            in.close();
            fin.close();
            return b;
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    public int getColor (int i) {
        return colors[i % colors.length];
    }

    public boolean saveCrushLog (String subject, String content, Context context) {
        try {
            HashMap<String, String> register = getCrushRegister(context);

            if (register.containsValue(content)) return true; // daca avem deja un mesaj de roare exact la fel, e destul

            if (!register.containsKey(subject)) {
                register.put(subject, content);
                //saveCrushRegister(register, context); nu salvam chiar de fiecare data
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public HashMap<String, String> getCrushRegister (Context context) {
        ObjectInputStream in;
        FileInputStream fin;

        if (crushRegister != null) {
            return crushRegister;
        }

        try {
            fin = MyApplication.getMyApplicationContext().openFileInput(CRUSH_REPORTS);
            in = new ObjectInputStream(fin);

            HashMap<String, String> register = (HashMap) in.readObject();

            in.close();
            fin.close();

            return register;
        } catch (Exception e){
            crushRegister = new HashMap<String, String>();
            return crushRegister;
        }
    }

    public boolean saveCrushRegister (HashMap<String, String> register, Context context) {
        //return true;
        FileOutputStream fout;
        ObjectOutputStream out;

        try {
            fout = MyApplication.getMyApplicationContext().openFileOutput(CRUSH_REPORTS, Context.MODE_PRIVATE);
            out = new ObjectOutputStream(fout);

            out.writeObject(register);
            out.flush();

            out.close();
            fout.close();

            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean setProgramID (long id, Context context) {
        FileOutputStream fout;
        ObjectOutputStream out;
        if (getUser() == null) return false;

        try {
            fout = MyApplication.getMyApplicationContext().openFileOutput(getUser().getUsername() + "_" + ID_PROGRAM, Context.MODE_PRIVATE);
            out = new ObjectOutputStream(fout);

            out.writeObject(id);
            out.flush();

            out.close();
            fout.close();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long getProgramID (Context context) {
        ObjectInputStream in;
        FileInputStream fin;
        if (getUser() == null) return -1;

        try {
            fin = MyApplication.getMyApplicationContext().openFileInput(getUser().getUsername() + "_" + ID_PROGRAM);
            in = new ObjectInputStream(fin);

            Long id = (Long) in.readObject();

            in.close();
            fin.close();

            return id;
        } catch (Exception e){
            return -1;
        }
    }

    private HashMap<ClassIDs, Boolean> getFirstUsedActivities  (Context context) {
        ObjectInputStream in;
        FileInputStream fin;

        if (firstUsedMap != null) return firstUsedMap;

        try {
            fin = MyApplication.getMyApplicationContext().openFileInput(FIRST_USE);
            in = new ObjectInputStream(fin);

            HashMap<ClassIDs, Boolean> map = (HashMap<ClassIDs, Boolean>) in.readObject();
            firstUsedMap = map;

            in.close();
            fin.close();

            return map;
        } catch (Exception e ) {
            HashMap<ClassIDs, Boolean> map = new HashMap<>();
            if (saveActivitiesUsedBefore(map, context)) {
                return map;
            }
            return null;
        }
    }

    private boolean saveActivitiesUsedBefore (HashMap<ClassIDs, Boolean> map, Context context) {
        ObjectOutputStream out;
        FileOutputStream fout;

        try {
            fout = MyApplication.getMyApplicationContext().openFileOutput(FIRST_USE, Context.MODE_PRIVATE);
            out = new ObjectOutputStream(fout);

            out.writeObject(map);
            firstUsedMap = map;

            out.close();
            fout.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean wasActivityUsedBefore(ClassIDs activityId, Context context) {
        HashMap<ClassIDs, Boolean> map = getFirstUsedActivities(context);
        if (map == null) return false;

        try {
            return map.get(activityId);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean setFirstUseActivity (ClassIDs activityId, boolean firstUsed, Context context) {
        HashMap<ClassIDs, Boolean> map = getFirstUsedActivities(context);
        if (map == null) map = new HashMap<>();
        map.put(activityId, firstUsed);
        return saveActivitiesUsedBefore(map, context);
    }

    public boolean hasUpdatedExpireDate () {
        return expiredDateUpdated;
    }

    public void dateWasUpdated() {
        expiredDateUpdated = true;
    }

    public void dateNeedUpdate () { expiredDateUpdated = false; }

    private PopupWindow popupWindow;

    public void createPopupwindow (Context context, View parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.popup_window_layout, null);

        popupWindow = new PopupWindow(
            customView,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

        popupWindow.showAtLocation(parent, Gravity.TOP | Gravity.LEFT, 0, 0);
    }

    public String getRandomMessage () {
        Random r = new Random();
        return messages[r.nextInt(messages.length)];
    }

    public void showLoadingScreenDelayed (long delay) {
        if (delay == 0) {
            FloatingWindow.setMessage(getRandomMessage());
            FloatingWindow.makeLoadingScreenAppear();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FloatingWindow.setMessage(getRandomMessage());
                FloatingWindow.makeLoadingScreenAppear();
            }
        }, delay);
    }

    public void hideLoadingScreenDelayed (long delay) {
        if (delay == 0) {
            FloatingWindow.makeLoadingScreenDisappear();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FloatingWindow.makeLoadingScreenDisappear();
            }
        }, delay);
    }

    public void salveazaActivitati () {
        if (saveEvent != null) {
            saveEvent.doAction();
        }
    }

    public void setSaveEvent (Event saveEvent) {
        this.saveEvent = saveEvent;
    }

    public boolean saveRegistrationCode (String code,Context mContext) {
        FileOutputStream fout;
        ObjectOutputStream out;

        try {
            fout = mContext.openFileOutput(REGISTRATION_CODE, Context.MODE_PRIVATE);
            out = new ObjectOutputStream(fout);

            out.writeObject(code);
            out.flush();

            out.close();
            fout.close();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean existsRegistrationCodeFile (Context mContext) {
        try {
            File f = new File(mContext.getFilesDir().getPath() + File.separator + REGISTRATION_CODE);
            return f.exists();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteRegistrationCode (Context mContext) {
        try {
            File f = new File(mContext.getFilesDir().getPath() + File.separator + REGISTRATION_CODE);
            return f.delete();
        } catch (Exception e){
            return false;
        }
    }

    public String getRegistrationCode (Context mContext) {
        FileInputStream fin;
        ObjectInputStream in;

        try {
            fin = mContext.openFileInput(REGISTRATION_CODE);
            in = new ObjectInputStream(fin);

            String code = (String) in.readObject();

            in.close();
            fin.close();

            return code;
        } catch (Exception e) {
            return "";
        }
    }

    public String getServerKey (Context context) {
        FileInputStream in;
        InputStreamReader reader;
        BufferedReader buf;

        try {
            in = context.openFileInput(SERVER_KEY);
            reader = new InputStreamReader(in);
            buf = new BufferedReader(reader);

            String key = buf.readLine();

            buf.close();
            reader.close();
            in.close();

            return key;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean saveServerKey (Context context, String key) {
        FileOutputStream out;
        PrintWriter writer;

        try {
            out = context.openFileOutput(SERVER_KEY, Context.MODE_PRIVATE);
            writer = new PrintWriter(out);

            writer.println(key);
            writer.flush();

            writer.close();
            out.close();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int deleteApplicationFiles (Context context) {
        int fails = 0;

        File [] files = context.getFilesDir().listFiles();

        for (File f : files) {
            if (f.getName().equals(REGISTRATION_CODE)) continue;
            if (f.getName().equals(SERIALIZED_SOCKET_PATH)) continue;
            if (f.getName().equals(FIRST_USE)) continue;
            try {
                if (!f.delete()) {
                    f.deleteOnExit();
                    fails++;
                }
            } catch (SecurityException s) {
                fails++;
            }
        }
        return fails;
    }

    public int getWifiSignalStrength (int levels) {
        @SuppressLint("WifiManagerLeak") WifiManager manager = (WifiManager) MyApplication.getMyApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return WifiManager.calculateSignalLevel(info.getRssi(), levels);
    }
}
