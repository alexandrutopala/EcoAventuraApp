/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.ibatis.common.jdbc.ScriptRunner;
import costumSerialization.SerializableFormulaFrame;
import dao.ActivitateDAO;
import dao.ActivitateGeneralaDAO;
import dao.AnimatorDAO;
import dao.EchipaDAO;
import dao.JocDAO;
import dao.JocGeneralDAO;
import dao.MembruEchipaDAO;
import dao.SerieDAO;
import dao.UserDAO;
import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import db.ActivitateDB;
import db.ActivitateGeneralaDB;
import db.AnimatorDB;
import db.EchipaDB;
import db.EchipaDataDB;
import db.JocDB;
import db.JocGeneralDB;
import db.MembruEchipaDB;
import db.SerieDB;
import db.UserDB;
import dialogs.XamppDialog;
import dto.ActivitateDTO;
import dto.ActivitateGeneralaDTO;
import dto.AnimatorDTO;
import dto.EchipaDTO;
import dto.JocDTO;
import dto.JocGeneralDTO;
import dto.JoinDTO;
import dto.MembruEchipaDTO;
import dto.SerieDTO;
import dto.UserDTO;
import gui.IstoricFrame;
import gui.MainFrame;
import gui.SerieActivaFrame;
import gui.StartFrame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;

/**
 *
 * @author Alexandru
 */
public class ControllerDB {
    public final static int USER_EXISTENT = 1;
    public final static int ANIMATOR_INEXISTENT = 2;
    public final static int PAROLA_GRESITA = 3;
    public final static int USER_OK = 4;
    
    public final static String DATE_SALVATE_PATH = "./db/date";
    public final static String REGISTRU_DATE_SALVATE_PATH = DATE_SALVATE_PATH + "/registru.srz";

    private SerieDB firstSerie;
    private SerieDB lastSerie; 
    private EntityManagerFactory emf;
    private ActivitateDAO activitateDao;
    private ActivitateGeneralaDAO activitateGeneralaDao;
    private AnimatorDAO animatorDao;
    private EchipaDAO echipaDao;
    private JocDAO jocDao;
    private JocGeneralDAO jocGeneralaDao;
    private MembruEchipaDAO membruEchipaDao;
    private SerieDAO serieDao;
    private UserDAO userDao;
    private Connection sqlConnection;
    private Map properties;
    
    private ControllerDB() { }
    
    public boolean connectToDB (Map properties) throws Exception {
        if (properties != null) {
            this.properties = properties;
        } else {
            properties = this.properties;
        }
        if (emf == null && properties != null) {
            emf = Persistence.createEntityManagerFactory("EcoAventuraPU", properties);
            activitateDao = new ActivitateDAO(emf);
            activitateGeneralaDao = new ActivitateGeneralaDAO(emf);
            animatorDao = new AnimatorDAO(emf);
            echipaDao = new EchipaDAO(emf);
            jocDao = new JocDAO(emf);
            jocGeneralaDao = new JocGeneralDAO(emf);
            membruEchipaDao = new MembruEchipaDAO(emf);
            serieDao = new SerieDAO(emf);
            userDao = new UserDAO(emf);
            return true;
        }
        
        return false;
    }
    
    public void disconnect () {
        emf.close();
        emf = null;
    }
    
    public static ControllerDB getInstance() {
        return ControllerDBHolder.INSTANCE;
    }
    
    private static class ControllerDBHolder {
        private static final ControllerDB INSTANCE = new ControllerDB();
    }

    public Connection getSqlConnection() {
        return sqlConnection;
    }

    public void setSqlConnection(Connection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }  
    
    public void setLastSerie (SerieDB serie) {
        this.lastSerie = serie;
    }
    
    public SerieDB getFirstSerie () {
        if (firstSerie == null) {
            calcFirstSerie();
        }
        return firstSerie;
    }
    
    
    
    public boolean adaugaUser(String username, String parola, int acces){
        UserDB user = userDao.getSingleResult(username);
        
        if (user == null){
            user = new UserDB();
            user.setUsername(username);
            user.setParola(parola);
            user.setAcces(acces);
            userDao.create(user);
            return true;
        }
        return false;
    }
    
    public int getUserIdByUsername (String username) {
        UserDB user = userDao.getSingleResult(username);
        
        if (user == null) return 0;
        return user.getIdUser();
    }
    
    public UserDB logheazaUser (String username, String parola){
        UserDB user = userDao.getSingleResult(username);
        
        if (user != null) {
            if (user.getParola().equals(parola) && user.getAcces() != UserDTO.ACCES_ANIMATOR){
                return user;
            }
        }
        return null;
    }
    
    public List<UserDB> getUsers () {
        return userDao.findUserDBEntities();
    }
    
    public boolean updateUser (UserDB user) {
        try {
            userDao.edit(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean stergeUser (UserDB user) {
        try {
            userDao.destroy(user.getIdUser());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public UserDB getUser (String username) {
        return userDao.getSingleResult(username);
    }
    
    public SerieDB adaugaSerie (int nrSerie, String data){
        SerieDB serie = serieDao.getSingleResult(nrSerie);
        
        if (serie == null){
            serie = new SerieDB();
            serie.setNumarSerie(nrSerie);
            serie.setDataInceput(data);
            serieDao.create(serie);

            lastSerie = serie;
            /*
            try {
                output.writeObject(serie);
            } catch (Exception e){
                System.out.println("A survenit o eroare la serializarea ultimei serii");
            }
            */
            return serie;
        }
        return null;
    }
    
    public int getSeriesNumber () {
        return serieDao.getSerieDBCount();
    }
    
    public int getMaxSeriesNumber () {
        return serieDao.findMaxSerieNumber();
    }
    
    public MembruEchipaDB adaugaMembru (String nume, EchipaDB echipa){
        MembruEchipaDB membru = new MembruEchipaDB();
        membru.setEchipaidEchipa(echipa);
        membru.setNumeMembruEchipa(nume);
        
        membruEchipaDao.create(membru);
        
        return membruEchipaDao.getSingleResult(nume, echipa);
    }
    
    public EchipaDB adaugaEchipa (SerieDB serie, String nume, String culoare, String scoala, String profesor){
        EchipaDB echipa = echipaDao.getSingleResult(nume, serie);
        
        if (echipa == null){
            echipa = new EchipaDB();
            echipa.setNumeEchipa(nume);
            echipa.setCuloareEchipa(culoare);
            echipa.setScoalaEchipa(scoala);
            echipa.setProfEchipa(profesor);
            echipa.setSerieidSerie(serie);
            echipaDao.create(echipa);
            echipa = echipaDao.getSingleResult(nume, serie);
            return echipa;
        }
        return null;
    }
    
    public boolean isTeamExists (EchipaDB echipa) {
        EchipaDB e = echipaDao.findEchipaDB(echipa.getIdEchipa());
        if (e != null) {
            return e.getNumeEchipa().equals(echipa.getNumeEchipa());
        }
        return false;
    }
    
    public boolean isPlayed (SerieDB serie, JocGeneralDB joc) {
        List<EchipaDB> echipe = getEchipeBySerie(serie);
        
        for (EchipaDB e : echipe) {
            List<JocDB> jocuri = getJocuriByEchipa(e);
            for (JocDB j : jocuri) {
                if (j.getJocGeneralidJocGeneral().equals(joc)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public List<ActivitateDB> getStoredActivitateByIdProgram (String perioada, ActivitateGeneralaDB actgdb, long idProgram) {
        return activitateDao.findActivitateByActivitateGeneralaAndIdProgramAndPerioada(actgdb, idProgram, perioada);
    }
    
    public List<JocDB> getStoredJocByIdProgram (String perioada, JocGeneralDB jdb, long idProgram) {
        return jocDao.findJocuriByJocGeneralAndIdProgramAndPerioada(jdb, perioada, idProgram);        
    }
    
    public List<ActivitateDB> getStoredActivitateByData (String data, String perioada, ActivitateGeneralaDB agdb) {
        return activitateDao.findActivitateByActivitateGeneralaAndDataAndPerioada(agdb, data, perioada);
    }
    
    public List<JocDB> getStoredJocByData (String data, String perioada, JocGeneralDB jgdb) {
        return jocDao.findJocuriByJocGeneralAndPerioadaAndData(jgdb, perioada, data);
    }
    
    // metoda imi spune daca echipa a absentat la jocul "jg"
    public boolean [] isTotalAbsent (EchipaDB e, JocGeneralDB jg) {
        List<JocDB> jocuri = jocDao.findByEchipaAndJocGeneral(e, jg);
        boolean [] res = new boolean[2];
        if (jocuri.isEmpty()) { // daca nu a fost niciun joc inregistrat, echipa nu are cum sa fi fost absenta            
            res[0] = true;
            res[1] = true;
            return res;
        } else {
            res[1] = false;
        }
        boolean totalAbsent = true;
        
        for (JocDB jdb : jocuri) {
            try {
                totalAbsent = (totalAbsent && jdb.getAbsent());
            } catch (Exception ex) {}
        }
        res[0] = totalAbsent;
        return res;
    }
    
    public SerieDB getLastSerie (){
        if (lastSerie == null){
            calcLastSerie();
        } 
        return lastSerie;
    }
    
    public List<SerieDB> getAllSerii (){
        return serieDao.findSerieDBEntities();
    }
    
    public void serializeFrame (int zile, String ora){
        try {
            OutputStreamWriter output = new OutputStreamWriter(
                    new FileOutputStream("./obj/date.txt")
            );
            
            output.write(zile + " ");
            output.write(ora);
            output.close();
            System.out.println("Serializare reusita! (MainFrame)");
        } catch (IOException ex) {
            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Eroare la serializarea ferestrei");
        }
    }
    
    public String [] deserializeFrame (){
        try {
            
            BufferedReader bf = new BufferedReader(
                    new InputStreamReader (
                            new FileInputStream("./obj/date.txt")
                    )
            );
            
            String [] s = bf.readLine().split(" ");
           
            bf.close();
            return s;
        } catch (Exception e){
            System.out.println("Eroare la deserializarea ferestrei");
            return null;
        }
    }
    
    public int getUsersCount () {
        return userDao.getUserDBCount();
    }
    
    public List<AnimatorDB> getAllAnimatori () {
        return animatorDao.findAnimatorDBEntities();
    }
    
    @Deprecated
    public void updateAnimator (AnimatorDB a) throws Exception{
        animatorDao.edit(a);
    }
    
    public AnimatorDB adaugaAnimator (String nume, boolean disponibil){
        AnimatorDB a = animatorDao.getSingleResult(nume);
        
        if (a == null){
            a = new AnimatorDB();
            a.setNumeAnimator(nume);
            a.setDisponibilAnimator(disponibil);
            animatorDao.create(a);
            return animatorDao.getSingleResult(nume);
        }
        
        return null;
    }
    
    public boolean editeazaAnimator (AnimatorDB a){
        try {
            animatorDao.edit(a);
            return true;
        } catch (Exception e){
            return false;
        }
    }
    
    public AnimatorDB getAnimator (String name) {
        return animatorDao.getSingleResult(name);
    } 
    
    public boolean stergeAnimator (AnimatorDB a) {
        try {
            animatorDao.destroy(a.getIdAnimator());
            return true;
        } catch (Exception e){
            return false;
        }
    }
    
    public List<EchipaDB> getEchipeBySerie (SerieDB serie){
        EntityManager em = echipaDao.getEntityManager();
        Query q = em.createNamedQuery("EchipaDB.findByIdSerie");
        q.setParameter("serieidSerie", serie);
        
        return q.getResultList();
    }
    
    public List<MembruEchipaDB> getMembriByEchipa (EchipaDB echipa){
        try{
            EntityManager em = membruEchipaDao.getEntityManager();
            Query q = em.createNamedQuery("MembruEchipaDB.findByIdEchipa");
            q.setParameter("echipaidEchipa", echipa);

            return q.getResultList();
        } catch (Exception e){
            return null;
        }
    }
    
    public void updateMembru (MembruEchipaDB membru) throws Exception {
        membruEchipaDao.edit(membru);
    }
    
    public void updateEchipa (EchipaDB echipa) throws NonexistentEntityException, Exception{
        echipaDao.edit(echipa);
    }

    public boolean stergeEchipa (EchipaDB echipa) {
        List<MembruEchipaDB> membri = membruEchipaDao.findMembriByEchipa(echipa);
        
        for (MembruEchipaDB m : membri) {
            if (!stergeMembru(m)) return false;
        }
        
        for (ActivitateDB a : activitateDao.findActivitiesByEchipa(echipa)){
            if (!stergeActivitate(a)) return false;
        }
        
        for (JocDB j : jocDao.findJocuriByEchiap(echipa)){
            if (!stergeJoc(j)) return false;
        }
        
        try {
            String oldColor = echipa.getCuloareEchipa();
            echipaDao.destroy(echipa.getIdEchipa());
            HashMap<String, EchipaDB> colors = SerializeController.getInstance().deserializeazaCulori();
            colors.put(oldColor, null);
            SerializeController.getInstance().serializareCulori(colors);
            return true;
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public boolean stergeMembru (MembruEchipaDB membru) {
        try {
            membruEchipaDao.destroy(membru.getIdMembruEchipa());
            return true;
        } catch (Exception e){}
        return false;
    }
    
    public boolean stergeActivitate (ActivitateDB a) {
        try {
            activitateDao.destroy(a.getIdActivitate());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean stergeJoc (JocDB j) {
        try {
            jocDao.destroy(j.getIdJoc());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public List<ActivitateGeneralaDB> getAllActivitatiGenerale(){
        return activitateGeneralaDao.findActivitateGeneralaDBEntities();
    }
    
    public List<JocGeneralDB> getAllJocuriGenerale () {
        return jocGeneralaDao.findJocGeneralDBEntities();
    }
    
    public List<AnimatorDB> getAnimatoriByDisponibilitate (boolean disponibil) {
        return animatorDao.getAniamtoriByDisponibilitate(disponibil);
    }
    
    public ActivitateGeneralaDB adaugaActivitateGenerala (String nume){
        ActivitateGeneralaDB ag = activitateGeneralaDao.getSingleResult(nume);
        
        if (ag == null){
            ag = new ActivitateGeneralaDB();
            ag.setNumeActivitateGenerala(nume);
            activitateGeneralaDao.create(ag);
            return activitateGeneralaDao.getSingleResult(nume);
        }
        return null;
    }
    
    public boolean editeazaActivitateGenerala (ActivitateGeneralaDB a) {
        ActivitateGeneralaDB ag = activitateGeneralaDao.getSingleResult(a.getNumeActivitateGenerala());
        
        if (ag == null) {
            try {
                activitateGeneralaDao.edit(a);
                return true;
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public boolean stergeActivitateGenerala (ActivitateGeneralaDB a) {
        try {
            for (ActivitateDB adb : activitateDao.findActivityByActivitateGenerala(a)) {
                if (!stergeActivitate(adb)) return false;
            }
            activitateGeneralaDao.destroy(a.getIdActivitateGenerala());
            return true;
        } catch (Exception e){
            return false;
        }
    }
    
    public JocGeneralDB adaugaJocGeneral (String nume, String descriere) {
        JocGeneralDB j = jocGeneralaDao.getSingleResult(nume);
        
        if (j == null) {
            j = new JocGeneralDB();
            j.setNumeJocGeneral(nume);
            j.setDescriereJoc(descriere);
            jocGeneralaDao.create(j);
            return jocGeneralaDao.getSingleResult(nume);
        }
        return null;
    }
    
    public JocGeneralDB findJocGeneral (String nume) {
        return jocGeneralaDao.getSingleResult(nume);
    }
    
    public boolean editeazaJocGeneral (JocGeneralDB j) {
        JocGeneralDB jg = jocGeneralaDao.getSingleResult(j.getNumeJocGeneral());
        
        if (jg == null) {
            try {
                jocGeneralaDao.edit(j);
                return true;
            } catch (Exception e) {
                
            }
        }
        return false;
    }
        
    public boolean stergeJocGeneral (JocGeneralDB j) {    
        try {
            for (JocDB joc : jocDao.findJocuriByJocGeneral(j)) {
                if (!stergeJoc(joc)) return false;
            }
            
            jocGeneralaDao.destroy(j.getIdJocGeneral());
            HashMap<JocGeneralDB, SerializableFormulaFrame> formulas = SerializeController.getInstance().deserializeFormulaFrames();
            formulas.remove(j);
            SerializeController.getInstance().serializeFormulaFrames(formulas);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public List<MembruEchipaDB> getAllMembri (SerieDB serie) {
        List<EchipaDB> echipe = echipaDao.findEchipeBySerie(serie);
        List<MembruEchipaDB> membri = new ArrayList<>();
        
        for (EchipaDB e : echipe) {
            List<MembruEchipaDB> aux = getMembriByEchipa(e);
            membri.addAll(aux);
        }
        
        return membri;
    }
    
    public int logheazaAnimator (UserDTO userdto) {
        UserDB userdb = userDao.getSingleResult(userdto.getUsername());
        
        if (userdb == null) {
            if (animatorDao.getSingleResult(userdto.getUsername()) != null) {
                if (adaugaUser(userdto.getUsername(), userdto.getParola(), userdto.getAcces())){
                    return USER_OK;
                } else {
                    return USER_EXISTENT;
                }
            } else {
                return ANIMATOR_INEXISTENT;
            }
        } else {
            return USER_EXISTENT;
        }       
    }
    
    public UserDTO conecteazaAnimator (UserDTO userdto) {
        UserDB userdb = userDao.getSingleResult(userdto.getUsername());
        
        if (userdb != null){
            if (userdb.getParola().equals(userdto.getParola())/* && 
                userdb.getAcces() == UserDTO.ACCES_ANIMATOR*/){
                return convert(userdb);
            }
        }
        return null;
    }
    
    public ActivitateDB findActivitate (long idProgram, String perioada, EchipaDB e, ActivitateGeneralaDB agdb) {
        ActivitateDB a = new ActivitateDB();
        a.setIdProgram(idProgram);
        a.setActivitateGeneralaidActivitateGenerala(agdb);
        a.setEchipaidEchipa(e);
        
        return activitateDao.isStored(a, perioada);
    }
    
    public JocDB findJoc (long idProgram, String perioada, EchipaDB e, JocGeneralDB jgdb) {
        JocDB j = new JocDB();
        j.setIdProgram(idProgram);
        j.setEchipaidEchipa(e);
        j.setJocGeneralidJocGeneral(jgdb);
        
        return jocDao.isStored(j, perioada);
    }
    
    public boolean wipeAllActivitiesByDate (String date) {
        return (activitateDao.wipeActivitati(date) && jocDao.wipeActivitati(date));
    }
    
    public ActivitateDB getUnicActivitate (ActivitateGeneralaDB agdb, EchipaDB e, String data /*nefolosit*/, String perioada) {
        ActivitateDB a = new ActivitateDB();
        a.setActivitateGeneralaidActivitateGenerala(agdb);
        a.setEchipaidEchipa(e);
        a.setData(data);
        a.setIdProgram(SerializeController.getInstance().getProgramActivitatiID());
        return activitateDao.isStored(a, perioada);
    }
    
    public JocDB getUnicJoc (JocGeneralDB jgdb, EchipaDB e, String data, String perioada) {
        JocDB j = new JocDB();
        j.setJocGeneralidJocGeneral(jgdb);
        j.setEchipaidEchipa(e);
        j.setData(data);
        j.setIdProgram(SerializeController.getInstance().getProgramActivitatiID());
        return jocDao.isStored(j, perioada);
    }
    
    public void storeJoins (final List<JoinDTO> joins, final UserDTO u) {
        ActivitateDB adb;
        JocDB jdb;
        List<ActivitateDTO> activitati = SerieActivaFrame.activitatiS;
        List<JocDTO> jocuri = SerieActivaFrame.jocuriS;
        boolean isPenalizare = false;
        boolean isExistingRunningProgram = SerieActivaFrame.isExistingRunningProgram();
        final long idProgram = SerializeController.getInstance().getProgramActivitatiID();
        
        // daca nu mai exista nici un program in desfasurare, inlatur toate activitatile in afara de penalizari
        for (int i = 0; i < joins.size(); ++i) {
            JoinDTO j = joins.get(i);
            if (j.getJoc() != null) {
                if (j.getJoc().getJocGeneral().toString().equalsIgnoreCase("penalizare")) {
                    isPenalizare = true;
                } else if (!isExistingRunningProgram) {
                    joins.remove(i);
                    i--;
                }                
            } else if (!isExistingRunningProgram) {
                joins.remove(i);
                i--;
            }
        }
        
        
        if (!SerieActivaFrame.isExistingRunningProgram() && !isPenalizare) return;
        
        final List<EchipaDB> echipe = getEchipeBySerie(MainFrame.getSerieActiva());      
        
        new Thread (
            new Runnable() {

                @Override
                public void run() {
                    IstoricFrame.getInstance().addRegisters(joins, u);
                }
            }
        ).start();
        
        for (JoinDTO join : joins) {
            if (join.getActivitate() != null) {                
                ActivitateDTO adto = join.getActivitate();
                
                //adto.setEchipe(activitati.get(activitati.indexOf(adto)).getEchipe()); // in urma upgrade-ului de defragmentare
                                                                                    // activitatile si jocurile sunt trimise cu echipele lor
                
                adb = convert(adto);
                adb.setIdProgram(idProgram);
                @SuppressWarnings("RedundantStringConstructorCall")
                final String ORGANIZATORI = new String(adb.getOrganizator());
                
                for (EchipaDTO e : adto.getEchipe()) {
                    EchipaDB edb = echipaDao.findEchipaDB(e.getId());
                    if (!echipe.contains(edb)){
                        System.out.println("Utilizatorul " + u.getUsername() + " a trimis activitatea " + adb + 
                                " in cadrul echipe " + e + ", care nu mai face parte din seria actuala.");
                        continue;
                    }
                    adb.setEchipaidEchipa(edb);
                    adb.getActivitateGeneralaidActivitateGenerala().setActivitateDBCollection(new ArrayList<ActivitateDB>()); //probabil, poate merge si fara
                    adb.setOrganizator(new String(ORGANIZATORI));
                    
                    ActivitateDB storedActivitate = activitateDao.isStored(adb, adto.getPerioada());
                    if (storedActivitate == null) {
                        adb.setOrganizator(
                                adb.getOrganizator()
                                .replace(u.getUsername(), "*" + u.getUsername())
                        );
                        activitateDao.create(adb);
                    } else {
                        if (storedActivitate.getOrganizator().contains("*" + u.getUsername())) { 
                            ////
                            System.out.println("Utilizatorul " + u.getUsername() + " a trimis un duplicat al activitatii " + adto + " pentru echipa " + edb);
                            continue;
                        }
                        adb.setIdActivitate(storedActivitate.getIdActivitate());
                        String [] ss = adb.getOrganizator().split(", ");
                        for (String s : ss) {
                            if (storedActivitate.getOrganizator().contains("*" + s) && !adb.getOrganizator().contains("*" + s)) { // evitam duplicarea stelutelor
                                adb.setOrganizator(adb.getOrganizator().replace(s, "*" + s));
                            }
                        }
                        
                        if (!adb.getOrganizator().contains("*" + u.getUsername())) {
                            adb.setOrganizator(adb.getOrganizator().replace(u.getUsername(), "*" + u.getUsername()));
                        }
                        try {
                            activitateDao.edit(adb);
                        } catch (Exception ex) {
                            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
            } else if (join.getJoc() != null) {               
                JocDTO jdto = join.getJoc();
                
                if (jdto.getJocGeneral().getNumeJocGeneral().equals("penalizare")){
                    JocDB jocdb = new JocDB();
                    jocdb.setJocGeneralidJocGeneral(jocGeneralaDao.getSingleResult("penalizare"));
                    jocdb.setEchipaidEchipa(echipaDao.findEchipaDB(jdto.getEchipe().get(0).getId()));
                    jocdb.setOrganizator(jdto.getAnimatori().get(0).getNumeAnimator() + ", ");
                    jocdb.setData(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
                    jocdb.setPunctaj(jdto.getPunctaj());
                    jocdb.setLocatie(jdto.getLocatie());
                    jocdb.setPost(jdto.getPost());
                    jocdb.setAbsent(false);
                    jocDao.create(jocdb);
                    continue;
                }
                
                //jdto.setEchipe(jocuri.get(jocuri.indexOf(jdto)).getEchipe());
                
                jdb = convert(jdto);
                jdb.setIdProgram(idProgram);
                final String ORGANIZATORI = new String(jdb.getOrganizator());
                
                for (EchipaDTO e : jdto.getEchipe()) {
                    int punctaj = 0;
                    EchipaDB edb = echipaDao.findEchipaDB(e.getId());
                    
                    if (!echipe.contains(edb)) {
                        System.out.println("Utilizatorul " + u.getUsername() + " a trimis activitatea " + jdb + 
                                    " in cadrul echipe " + e + ", care nu mai face parte din seria actuala.");
                        continue;
                    }
                    jdb.setEchipaidEchipa(edb);
                    jdb.getJocGeneralidJocGeneral().setJocDBCollection(new ArrayList<JocDB>());
                    jdb.setOrganizator(new String(ORGANIZATORI));
                    jdb.setPunctaj(0);
                    
                    jdb.setAbsent(jdto.getEchipeAbsente().contains(e));
                    
                    try {
                        punctaj = (int) jdto.getFormulas().get(e).evaluate();
                    } catch (Exception ex) {
                        Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        jdb.setPunctaj(punctaj < 0 ? 0 : punctaj);                        
                    }
                    
                    JocDB storedJoc = jocDao.isStored(jdb, jdto.getPerioada());
                    
                    if (storedJoc == null) {
                        jdb.setOrganizator(
                                jdb.getOrganizator().replace(u.getUsername(), "*" + u.getUsername())
                        );
                        jocDao.create(jdb);
                    } 
                    
                    if (storedJoc != null) {
                        if (storedJoc.getOrganizator().contains("*" + u.getUsername())) {
                            ////
                            System.out.println("Utilizatorul " + u.getUsername() + " a trimis un duplicat al jocului " + jdto + " pentru echipa " + edb);
                            continue;
                        }
                        jdb.setIdJoc(storedJoc.getIdJoc());
                        String [] ss = jdb.getOrganizator().split(", ");
                        for (String s : ss) {
                            if (storedJoc.getOrganizator().contains("*" + s) && !jdb.getOrganizator().contains("*" + s)) {
                                jdb.setOrganizator(jdb.getOrganizator().replace(s, "*" + s));
                            }
                        }
                        if (!jdb.getOrganizator().contains("*" + u.getUsername())) {
                            jdb.setOrganizator(jdb.getOrganizator().replace(u.getUsername(), "*" + u.getUsername()));
                        }
                        //ai grija cum si ce updatezi pt a nu pierde info
                        
                        if (!jdto.isAllowsAnimatorPrincipal()) {
                            jdb.setPunctaj(jdb.getPunctaj() + storedJoc.getPunctaj());
                            jdb.setAbsent(jdb.getAbsent() && storedJoc.getAbsent());
                        } else if (!jdto.getAnimatori().get(0).getNumeAnimator().equals(u.getUsername())){
                            jdb.setPunctaj(storedJoc.getPunctaj());
                            jdb.setAbsent(storedJoc.getAbsent());
                        }
                        
                        try {
                            jocDao.edit(jdb);
                        } catch (Exception ex) {
                            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        
        
    }
    
    public List<ActivitateDB> getActivittatiByEchipa (EchipaDB echipa) {
        return activitateDao.findActivitiesByEchipa(echipa);
    }
    
    public List<JocDB> getJocuriByEchipa (EchipaDB echipa) {
        return jocDao.findJocuriByEchiap(echipa);
    }
    
    public synchronized boolean dropDB () {
        try {
            EntityManager em = emf.createEntityManager();
            Query q = em.createNativeQuery("DROP DATABASE ecoaventuradb");
            em.getTransaction().begin();
            q.executeUpdate();
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public synchronized boolean uploadDB (String cannonicalPath) throws Exception {
//        int code = -1;
//        String [] data = StartFrame.deserializeaza();
//        Process p = null;
//        try {
//            p = Runtime.getRuntime().exec("./db/mysql ecoaventuradb -u" + data[0] + " -p" + data[1] + " -e source " + cannonicalPath);
//            code = p.waitFor();
//            
//            if (code != 0) throw new Exception();
//            return true;
//        } catch (Exception e) {
//            if (code != 0) {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//                throw new Exception (reader.readLine());
//            }
//        }
//        return false;
        Statement st = sqlConnection.createStatement();
        st.execute("CREATE DATABASE ecoaventuradb");
        Class.forName("com.mysql.jdbc.Driver");
        
        String data [] = StartFrame.deserializeaza();
                
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:" + data[2] + "/ecoaventuradb", data[0], data[1]);
        
        ScriptRunner runner = new ScriptRunner(conn, false, false);        
        runner.runScript(new BufferedReader(new FileReader(cannonicalPath)));
        
        return true;
    }
    
    public synchronized boolean downloadDB ()  {
        new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        try {
                            File f = new File(DATE_SALVATE_PATH);

                            if (!f.exists()) {
                                if (!f.mkdir()){
                                    throw new Exception("Fisierul nu poate fi scris");
                                }
                            }                           
                                                        
                            String mysqlDumpPath = SerializeController.getInstance().getXamppPath() + "\\mysql\\bin\\mysqldump";
                            SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy-HHmmss");
                            String fileName = sdf.format(Calendar.getInstance().getTime());
                            
                            
                            File dumped = new File(f.getPath() + fileName + ".sql"); 
                            dumped.createNewFile();
                            
                            try { Files.setAttribute(dumped.toPath(), "dos:hidden", true); } catch (Exception e) {}
                            
                            String [] data = StartFrame.deserializeaza();
                            int code;
                            Process p = null;
                            if (data[1] != "") {
                                p = Runtime.getRuntime().exec(mysqlDumpPath + " -u" + data[0] +
                                        " ecoaventuradb -r " + f.getCanonicalPath() + "\\" + fileName + ".sql");
                            } else {
                                p = Runtime.getRuntime().exec(mysqlDumpPath + " -u" + data[0] + 
                                        " -p" + data[1] + " ecoaventuradb -r " + f.getCanonicalPath() + "\\" + fileName + ".sql");
                            }
                            code = p.waitFor();


                            if (code != 0) {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                                throw new Exception (reader.readLine());
                            }
                            
                            try { Files.setAttribute(f.toPath(), "dos:hidden", false);
                            JOptionPane.showMessageDialog(null, "Baza de date a fost salvata", "Succes", JOptionPane.INFORMATION_MESSAGE);} 
                            catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "<html>Baza de date NU a fost salvata.<br>Asigura-te ca folderul aplicatiei se afla intr-o locatie<br>fara restrictii de citire/scriere. </html>", "Eroare de securitate", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Baza de date nu a putut fi salvata", "Eroare", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
        ).start();

        return true;
        
    }
    
    public synchronized void loadDefaultDB (Connection conn, String path) throws FileNotFoundException, IOException, SQLException {
        if (conn == null) conn = sqlConnection;
        
        ScriptRunner runner = new ScriptRunner(conn, false, false);
        runner.runScript(new BufferedReader(new FileReader(path)));
    }
    
    public synchronized void loadMagazinDB (Connection conn, String path) throws Exception{
        if (conn == null) conn = sqlConnection;
        
        ScriptRunner runner = new ScriptRunner(conn, false, false);
        runner.runScript(new BufferedReader(new FileReader(path)));
    }
    
    public long getActivitateMaxId () {
        return activitateDao.getMaxId();
    }
    
    public long getJocMaxId () {
        return jocDao.getMaxId();
    }
    
    // ================================ convertori ==================================================================
   
    
    public MembruEchipaDTO convert (MembruEchipaDB membrudb){
        MembruEchipaDTO membrudto = new MembruEchipaDTO(membrudb.getIdMembruEchipa());
        //membrudto.setEchipa(convert(membrudb.getEchipaidEchipa()));
        membrudto.setNumeMembruEchipa(membrudb.getNumeMembruEchipa());
        return membrudto;
    }
    
    public MembruEchipaDB convert (MembruEchipaDTO membrudto){
        MembruEchipaDB membrudb = new MembruEchipaDB();
        membrudb.setIdMembruEchipa(membrudto.getId());
        //membrudb.setEchipaidEchipa(convert(membrudto.getEchipa()));
        membrudb.setNumeMembruEchipa(membrudto.getNumeMembruEchipa());
        return membrudb;
    }
    
    public EchipaDTO convert (EchipaDB echipadb) {
        EchipaDTO echipadto = new EchipaDTO(echipadb.getIdEchipa());
        echipadto.setCuloareEchipa(echipadb.getCuloareEchipa());
        echipadto.setNumeEchipa(echipadb.getNumeEchipa());
        echipadto.setProfEhipa(echipadb.getProfEchipa());
        echipadto.setScoalaEchipa(echipadb.getScoalaEchipa());
        
        List<ActivitateDTO> activitati = new ArrayList<>();
        List<JocDTO> jocuri = new ArrayList<>();
        List<MembruEchipaDTO> membri = new ArrayList<>();
        
        try {
            for (ActivitateDB a : activitateDao.findActivitiesByEchipa(echipadb)){
                activitati.add(convert(a));
            }
        } catch (Exception e) {} 
        
        try {
            for (JocDB j : jocDao.findJocuriByEchiap(echipadb)){
                jocuri.add(convert(j));
            }
        }catch (Exception e){}
        
        try {
            for (MembruEchipaDB m : membruEchipaDao.findMembriByEchipa(echipadb)){
                membri.add(convert(m));
            }
        } catch (Exception e) {}
        
        echipadto.setActivitatiEchipa(activitati);
        echipadto.setJocuriEchipa(jocuri);
        echipadto.setMembriEchipa(membri);
        //echipadto.setSerie(convert(echipadb.getSerieidSerie()));
        
        for (MembruEchipaDTO m : echipadto.getMembriEchipa()){
            m.setEchipa(echipadto);
        }
        return echipadto;
    }
    
    public ActivitateDTO convert(ActivitateDB actdb){
        ActivitateDTO actdto = new ActivitateDTO();
        actdto.setId(actdb.getIdActivitate());
        actdto.setActivitateGenerala(convert(actdb.getActivitateGeneralaidActivitateGenerala()));
        actdto.setLocatie(actdb.getLoactie());
        actdto.setPost(actdb.getPost());
        
        List<AnimatorDTO> animatori = new ArrayList<>();
        String [] ss = actdb.getOrganizator().split(", ");
        
        for (String s : ss){
            try {
                AnimatorDB a = animatorDao.getSingleResult(s);
                if (a != null) {
                    animatori.add(convert(a));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        actdto.setAnimatori(animatori);
        return actdto;
    }
    
    public ActivitateDB convert(ActivitateDTO actdto){
        ActivitateDB actdb = new ActivitateDB(actdto.getId());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(Calendar.getInstance().getTime());
        actdb.setData(date);
        
        actdb.setLoactie(actdto.getLocatie() + " $" + actdto.getPerioada() + "$");
        actdb.setPost(actdto.getPost());
        actdb.setActivitateGeneralaidActivitateGenerala(convert(actdto.getActivitateGenerala()));
        
        String s = "";
        for (AnimatorDTO a : actdto.getAnimatori()){
            s = s.concat(a.getNumeAnimator() + ", ");
        }
        actdb.setOrganizator(s);
        
        return actdb;
    }
    
    public JocDTO convert (JocDB jocdb) {
        JocDTO jocdto = new JocDTO();
        jocdto.setId(jocdb.getIdJoc());
        jocdto.setPunctaj(jocdb.getPunctaj());
        jocdto.setJocGeneral(convert(jocdb.getJocGeneralidJocGeneral()));
        jocdto.setLocatie(jocdb.getLocatie());
        jocdto.setPost(jocdb.getPost());
        
        List<AnimatorDTO> animatori = new ArrayList<>();
        String [] ss = jocdb.getOrganizator().split(", ");
        
        for (String s : ss){
            try {
                AnimatorDB a = animatorDao.getSingleResult(s);
                if (a != null) {
                    animatori.add(convert(a));
                }
            } catch (Exception e) {
                
            }
        }
        
        jocdto.setAnimatori(animatori);
        return jocdto;
    }
    
    public JocDB convert (JocDTO jocdto) {
        JocDB jocdb = new JocDB();
        jocdb.setIdJoc(jocdto.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(Calendar.getInstance().getTime());
        jocdb.setData(date);
        jocdb.setPunctaj(jocdto.getPunctaj());
        jocdb.setPost(jocdto.getPost());
        jocdb.setLocatie(jocdto.getLocatie() + " $" + jocdto.getPerioada() + "$");
        jocdb.setJocGeneralidJocGeneral(convert(jocdto.getJocGeneral()));
        
        String s = "";
        for (AnimatorDTO a : jocdto.getAnimatori()){
            s = s.concat(a.getNumeAnimator() + ", ");
        }
        
        jocdb.setOrganizator(s);
        return jocdb;
    }
    
    public EchipaDB convert (EchipaDTO echipadto){
        EchipaDB echipadb = new EchipaDB();
        echipadb.setCuloareEchipa(echipadto.getCuloareEchipa());
        echipadb.setIdEchipa(echipadto.getId());
        echipadb.setNumeEchipa(echipadto.getNumeEchipa());
        echipadb.setProfEchipa(echipadto.getProfEhipa());
        echipadb.setScoalaEchipa(echipadto.getScoalaEchipa());
        //echipadb.setSerieidSerie(convert(echipadto.getSerie()));

        List<ActivitateDB> activitati = new ArrayList<>();
        List<JocDB> jocuri = new ArrayList<>();
        List<MembruEchipaDB> membri = new ArrayList<>();
        
        try {
            for (ActivitateDTO a : echipadto.getActivitatiEchipa()){
                activitati.add(convert(a));
            }
        } catch (Exception e) {}
        
        try {
            for (JocDTO j : echipadto.getJocuriEchipa()){
                jocuri.add(convert(j));
            }
        } catch (Exception e) {}
        
        try {
            for (MembruEchipaDTO m : echipadto.getMembriEchipa()){
                membri.add(convert(m));
            }
        } catch (Exception e){}
        
        echipadb.setActivitateDBCollection(activitati);
        echipadb.setJocDBCollection(jocuri);
        echipadb.setMembruEchipaDBCollection(membri);
        
        for (MembruEchipaDB m : echipadb.getMembruEchipaDBCollection()){
            m.setEchipaidEchipa(echipadb);
        }
        
        if (echipadb.getSerieidSerie() == null) {
            echipadb.setSerieidSerie(lastSerie);
        }
        return echipadb;
    }
    
    public SerieDTO convert (SerieDB seriedb) {
        SerieDTO seriedto = new SerieDTO();
        seriedto.setId(seriedb.getIdSerie());
        seriedto.setNumarSerie(seriedb.getNumarSerie());
        
        List<EchipaDTO> echipe = new ArrayList<>();
        
        for (EchipaDB e : seriedb.getEchipaDBCollection()){
            echipe.add(convert(e));
        }
        
        for (EchipaDTO e : seriedto.getEchipe()){
            e.setSerie(seriedto);
        }
        
        seriedto.setEchipe(echipe);
        seriedto.setDataInceput(seriedb.getDataInceput());
                
        return seriedto;
    }
    
    public SerieDB convert (SerieDTO seriedto) {
        SerieDB seriedb = new SerieDB();
        seriedb.setDataInceput(seriedto.getDataInceput());
        seriedb.setIdSerie(seriedto.getId());
        seriedb.setNumarSerie(seriedto.getNumarSerie());
        
        List<EchipaDB> echipe = new ArrayList<>();
        
        for (EchipaDTO e : seriedto.getEchipe()){
            echipe.add(convert(e));
        }
        
        for (EchipaDB e : seriedb.getEchipaDBCollection()){
            e.setSerieidSerie(seriedb);
        }            
        
        seriedb.setEchipaDBCollection(echipe);
        return seriedb;
    }
    
    public List<EchipaDTO> convert (List<EchipaDB> echipedb) {
        List<EchipaDTO> echipedto = new ArrayList<>();
        
        for (EchipaDB e : echipedb) {
            echipedto.add(convert(e));
        }
        return echipedto;
    }
    
    public UserDTO convert (UserDB userdb){
        UserDTO userdto = new UserDTO();
        userdto.setAcces(userdb.getAcces());
        userdto.setId(userdb.getIdUser());
        userdto.setParola(userdb.getParola());
        userdto.setUsername(userdb.getUsername());
        return userdto;
    }
    
    public UserDB convert (UserDTO userdto) {
        UserDB userdb = new UserDB();
        userdb.setAcces(userdto.getAcces());
        userdb.setIdUser(userdto.getId());
        userdb.setParola(userdto.getParola());
        userdb.setUsername(userdto.getUsername());
        
        return userdb;
    }
    
    public AnimatorDTO convert (AnimatorDB adb){
        AnimatorDTO adto = new AnimatorDTO();
        adto.setDisponibilAnimator(adb.getDisponibilAnimator());
        adto.setIdAnimator(adb.getIdAnimator());
        adto.setNumeAnimator(adb.getNumeAnimator());
        return adto;
    }
    
    public AnimatorDB convert (AnimatorDTO adto){
        AnimatorDB adb = new AnimatorDB();
        adb.setDisponibilAnimator(adto.isDisponibilAnimator());
        adb.setIdAnimator(adto.getIdAnimator());
        adb.setNumeAnimator(adto.getNumeAnimator());
        return adb;
    }
    
    public ActivitateGeneralaDTO convert (ActivitateGeneralaDB agdb){
        ActivitateGeneralaDTO agdto = new ActivitateGeneralaDTO();
        agdto.setIdActivitateGenerala(agdb.getIdActivitateGenerala());
        agdto.setNumeActivitateGenerala(agdb.getNumeActivitateGenerala());
        return agdto;
    }
    
    public ActivitateGeneralaDB convert (ActivitateGeneralaDTO agdto){
        ActivitateGeneralaDB agdb = activitateGeneralaDao.findActivitateGeneralaDB(agdto.getIdActivitateGenerala());
        return agdb;
    }
    
    public JocGeneralDTO convert (JocGeneralDB jdb) {
        JocGeneralDTO jdto = new JocGeneralDTO();
        jdto.setIdJocGeneral(jdb.getIdJocGeneral());
        jdto.setNumeJocGeneral(jdb.getNumeJocGeneral());
        jdto.setDescriereJoc(jdb.getDescriereJoc());
        return jdto;
    }
    
    public JocGeneralDB convert (JocGeneralDTO jdto) {
        JocGeneralDB jdb = jocGeneralaDao.findJocGeneralDB(jdto.getIdJocGeneral());
        return jdb;
    }
    
    
    
    //=====================================================
    
    private void calcLastSerie () {
        List<SerieDB> serii = serieDao.findSerieDBEntities();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
        try {
            SerieDB ultimaSerie = serii.get(0);
            Date ultimaData = df.parse(serii.get(0).getDataInceput());
            for (int i = 1; i < serii.size(); ++i){
                if (ultimaData.before(df.parse(serii.get(i).getDataInceput()))){
                    ultimaData = df.parse(serii.get(i).getDataInceput());
                    ultimaSerie = serii.get(i);
                }
            }
            lastSerie = ultimaSerie;
        } catch (ParseException ex) {
            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e){
            
        }
    }
    
    private void calcFirstSerie () {
        List<SerieDB> serii = serieDao.findSerieDBEntities();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
        try {
            SerieDB primaSerie = serii.get(0);
            Date primaData = df.parse(serii.get(0).getDataInceput());
            for (int i = 1; i < serii.size(); ++i){
                if (primaData.after(df.parse(serii.get(i).getDataInceput()))){
                    primaData = df.parse(serii.get(i).getDataInceput());
                    primaSerie = serii.get(i);
                }
            }
            firstSerie = primaSerie;
        } catch (ParseException ex) {
            Logger.getLogger(ControllerDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e){
            
        }
    }
    
    // dezarhivare 
    
    public void adaugaSerie (SerieDB serie, List<EchipaDataDB> data) {
        if (serie == null) return;
        try {                        
            serie.getEchipaDBCollection().size(); // lazy prevent
            serieDao.insertSerie(serie);
            adaugaEchipe((List<EchipaDB>) serie.getEchipaDBCollection(), data);            
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    public void adaugaEchipe (List<EchipaDB> echipe, List<EchipaDataDB> data) {
        if (echipe == null) return;
        EchipaDataDB ed = null;
        
        for (EchipaDB e : echipe) {
            echipaDao.insertEchipa(e);
            echipaDao.preventLazy(e.getIdEchipa());
            //e.getActivitateDBCollection().size();  // lazy prevent
            for (EchipaDataDB it : data) {
                if (it.getId() == e.getIdEchipa()){
                    ed = it;
                }
            }
            
            try {adaugaActivititati(ed.getActivitati()); } catch (Exception ex) {}
            //try {adaugaActivititati((List<ActivitateDB>) e.getActivitateDBCollection()); } catch (Exception ex) {}
            //e.getJocDBCollection().size(); // lazy prevent
            try {adaugaJocuri(ed.getJocuri()); } catch (Exception ex) {}
            //e.getMembruEchipaDBCollection().size(); //clazy prevent
            try {adaugaMembri(ed.getMembri());} catch (Exception ex) {}           
        }
    }
    
    public void adaugaActivititati (List<ActivitateDB> activitati) {
        if (activitati == null) return;
        for (ActivitateDB a : activitati) {
            //adaugaActivitateGenerala(a.getActivitateGeneralaidActivitateGenerala());
            activitateDao.insertActivitate(a);
            a.setActivitateGeneralaidActivitateGenerala(activitateGeneralaDao.getSingleResult(a.getActivitateGeneralaidActivitateGenerala().getNumeActivitateGenerala()));
            
        }
    }
    
    public void adaugaJocuri (List<JocDB> jocuri) {
        if (jocuri == null) return;
        for (JocDB j : jocuri) {
            //adaugaJocGeneral(j.getJocGeneralidJocGeneral());
            jocDao.insertJoc(j);
            j.setJocGeneralidJocGeneral(jocGeneralaDao.getSingleResult(j.getJocGeneralidJocGeneral().getNumeJocGeneral()));
            
        }
    }
    
    public void adaugaMembri (List<MembruEchipaDB> membri) {
        if (membri == null) return;
        for (MembruEchipaDB m : membri) {
            membruEchipaDao.insertMembru(m);
        }
    }
    
    public void adaugaActivitateGenerala (ActivitateGeneralaDB ag) {
        activitateGeneralaDao.insertActivitateGenerala(ag);
    }
    
    public void adaugaJocGeneral(JocGeneralDB jg) {
        jocGeneralaDao.insertJocGeneral(jg);
    }
    
    // arhivare 
    
    public SerieDB arhivSerie (SerieDB serie, HashMap<SerieDB, List<EchipaDataDB>> data) throws IllegalOrphanException, NonexistentEntityException, Exception {
        SerieDB s = serieDao.findSerieDB(serie.getIdSerie());
        if (s == null) throw new NullPointerException("Serie inexistenta");
        
        arhivEchipe(s, data);
        serieDao.destroy(s.getIdSerie());
        return s;
    }
    
    public List<EchipaDB> arhivEchipe (SerieDB serie, HashMap<SerieDB, List<EchipaDataDB>> data) throws Exception {
        List<EchipaDB> echipe = echipaDao.findEchipeBySerie(serie);
        List<EchipaDB> echipeSterse = new ArrayList<>();
        serie.setEchipaDBCollection(echipe);
        
        EchipaDataDB echipaData;
        List<EchipaDataDB> date = new ArrayList<>();
        
        for (EchipaDB e : echipe) {
            echipaData = new EchipaDataDB(e);
            try {
                echipaData.setActivitati(arhivActivitati(e));
                echipaData.setJocuri(arhivJocuri(e));
                echipaData.setMembri(arhivMembri(e));            
                
                date.add(echipaData);
                
                echipaDao.destroy(e.getIdEchipa());
                echipeSterse.add(e);
            } catch (Exception ex) {
                adaugaEchipe(echipeSterse, date);
                throw ex;
            }
        }
        data.put(serie, date);
        return echipeSterse;
    }
    
    public List<ActivitateDB> arhivActivitati (EchipaDB echipa) throws Exception{
        List<ActivitateDB> activitati = activitateDao.findActivitiesByEchipa(echipa);
        List<ActivitateDB> activitatiSterse = new ArrayList<>();
        
        //echipa.setActivitati(activitati);
        echipa.setActivitateDBCollection(activitati);
        
        for (ActivitateDB a : activitati){
            try {
                activitateDao.destroy(a.getIdActivitate());
                activitatiSterse.add(a);
            } catch (Exception e) {
                adaugaActivititati(activitatiSterse);
                throw e;
            }
        }
        return activitatiSterse;
    }
    
    public List<JocDB> arhivJocuri (EchipaDB echipa) throws Exception{
        List<JocDB> jocuri = jocDao.findJocuriByEchiap(echipa);
        List<JocDB> jocuriSterse = new ArrayList<>();
        
        //echipa.setJocuri(jocuri);
        echipa.setJocDBCollection(jocuri);
        
        for (JocDB j : jocuri) {
            try {
                jocDao.destroy(j.getIdJoc());
                jocuriSterse.add(j);
            } catch (Exception e) {
                adaugaJocuri(jocuriSterse);
                throw e;
            }
        }
        return jocuriSterse;
    }
    
    public List<MembruEchipaDB> arhivMembri (EchipaDB echipa) throws Exception{
        List<MembruEchipaDB> membri = membruEchipaDao.findMembriByEchipa(echipa);
        List<MembruEchipaDB> membriStersi = new ArrayList<>();
        
        //echipa.setMembri(membri);
        echipa.setMembruEchipaDBCollection(membri);
        
        for (MembruEchipaDB m : membri) {
            try {
                membruEchipaDao.destroy(m.getIdMembruEchipa());
                membriStersi.add(m);
            } catch (Exception e) {
                adaugaMembri(membriStersi);
                throw e;
            }
        }
        
        return membriStersi;
    }
}

