/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import costumSerialization.SerializableAdaugaInformatiiFrame;
import costumSerialization.SerializableDistribuieEchipeFrame;
import costumSerialization.SerializableDistribuieProgramFrame;
import costumSerialization.SerializableFormulaFrame;
import costumSerialization.SerializablePosteazaProgramFrame;
import costumSerialization.SerializableSerieActivaFrame;
import db.EchipaDB;
import db.EchipaDataDB;
import db.JocGeneralDB;
import db.SerieDB;
import dto.UserDTO;
import formula.Formula;
import gui.FormulaFrame;
import gui.FormulaFrame.LabelFactory;
import gui.MainFrame;
import gui.StartFrame;
import gui.ZippingFrame;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import main.Event;
import main.Inchiriere;

/**
 *
 * @author Alexandru
 */
public class SerializeController {
    public final static String POSTEAZA_PROGRAM_PATH = "./obj/posteazaProgram.srz";
    public final static String DISTRIBUIE_PROGRAM_PATH = "./obj/distribuieProgram.srz";
    public final static String ADAUGA_INFORMATII_PATH = "./obj/adaugaInformatii.srz";
    public final static String SERIE_ACTIVA_PATH = "./obj/serieActiva.srz";
    public final static String DISTRIBUIE_ECHIPE_PATH = "./obj/distribuieEchipe.srz";
    public final static String PORT_PATH = "./obj/port.srz";
    public final static String FORMULAS_PATH = "./obj/formulas.srz";
    public final static String ARHIVA_SERII_PATH = "./obj/serii.srz";
    public final static String CULORI_PATH = "./obj/culori.srz";    
    public final static String XAMPP_PATH = "./xampp/xampp_path.txt";
    public final static String DATA_EXPIRARE_PATH = "./obj/data_expirare.txt";
    public final static String LAST_SERIE_ACTIVA_FRAME_PATH = "./obj/lastSerieActivaFrame.srz";
    public final static String ID_PROGRAM_PATH = "./obj/id.srz";
    public final static String DATA_INCEPUT_PROGRAM_PATH = "./obj/start.srz";
    public final static String LISTA_UTILIZATORI_SERVER = "./obj/serverUsers.srz";
    public final static String CONFIG_PATH = "./obj/config.srz";
    public final static String INCHIRIERI_PATH = "./obj/inchirieri.srz";
    private boolean canReadDistribuieFrame = true;
    private boolean candReadInforamtiiFrame = true;
    private boolean canReadPosteazaFrame = true;
    private boolean canReadSerieActivaFrame = true;
    private boolean canReadDistribuieEchipeFrame = true;
    private HashMap<JocGeneralDB, SerializableFormulaFrame> formulas;
    private HashMap<String, Object> configs;
    private LabelFactory labelFactoryInstance;
    
    private SerializeController() {
    }
    
    public static SerializeController getInstance() {
        return SerializeControllerHolder.INSTANCE;
    }
    
    private static class SerializeControllerHolder {

        private static final SerializeController INSTANCE = new SerializeController();
    }

    public LabelFactory getLabelFactoryInstance() {
        return labelFactoryInstance;
    }

    public void setLabelFactoryInstance(LabelFactory labelFactoryInstance) {
        this.labelFactoryInstance = labelFactoryInstance;
    }   
    
    private HashMap<String, Object> loadConfigs () {
        if (configs != null) return configs;
        
        ObjectInputStream in;
        FileInputStream fin;
        
        try {
            fin = new FileInputStream(CONFIG_PATH);
            in = new ObjectInputStream(fin);
            
            configs = (HashMap<String, Object>) in.readObject();
            
            in.close();
            fin.close();            
            
        } catch (Exception e) {
            configs = new HashMap<>();
            saveConfigs(configs);
        }
        return configs;
    }
    
    private boolean saveConfigs (HashMap<String, Object> configs) {
        ObjectOutputStream out;
        FileOutputStream fout;
        
        try {
            fout = new FileOutputStream(CONFIG_PATH);
            out = new ObjectOutputStream(fout);
            
            out.writeObject(configs);
            out.flush();
            
            out.close();
            fout.close();
                        
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean saveConfigs () {
        ObjectOutputStream out;
        FileOutputStream fout;
        
        try {
            fout = new FileOutputStream(CONFIG_PATH);
            out = new ObjectOutputStream(fout);
            
            out.writeObject(configs);
            out.flush();
            
            out.close();
            fout.close();
                        
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void writeConfig (String key, Object val) {
        HashMap<String, Object> map = loadConfigs();
        map.put(key, val);
    }
    
    public Object readCongif (String key) {
        HashMap<String, Object> map = loadConfigs();
        return map.get(key);
    }
    
    public boolean serializeazaPostareProgramFrame (SerializablePosteazaProgramFrame sppf){
        try {
            ObjectOutputStream output = new ObjectOutputStream(
                    new FileOutputStream(POSTEAZA_PROGRAM_PATH, false)
            );
            
            output.writeObject(sppf);
            output.close();
            return true;
        } catch (Exception e){
            return false;
        }
    }
    
    public boolean existSerializedPPF () {
        File f = new File(POSTEAZA_PROGRAM_PATH);        
        return (f.exists() && f.isFile());       
    }
    
    public boolean existSerializedDPF () {
        File f = new File(DISTRIBUIE_PROGRAM_PATH);
        return (f.exists() && f.isFile());         
    }
    
    public boolean existSerializedSAF () {
        File f = new File(SERIE_ACTIVA_PATH);
        return (f.exists() && f.isFile());
    }
    
    public boolean serializeSerieActivaFrame( SerializableSerieActivaFrame ssaf){
        try {
            ObjectOutputStream output = new ObjectOutputStream(
                    new FileOutputStream(SERIE_ACTIVA_PATH)
            );
            
            output.writeObject(ssaf);
            output.close();
            
            return true;
        } catch (Exception e){
            return false;
        }
    }
    
    public boolean serializeLastSerieActivaFrame (SerializableSerieActivaFrame ssaf) {
        FileOutputStream fout;
        ObjectOutputStream out;
        
        try {
            fout = new FileOutputStream(LAST_SERIE_ACTIVA_FRAME_PATH);
            out = new ObjectOutputStream(fout);
            
            out.writeObject(ssaf);
            out.flush();
            
            out.close();
            fout.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public SerializableSerieActivaFrame deserializeSerieActivaFrame () {
        try {
            ObjectInputStream input = new ObjectInputStream(
                    new FileInputStream(SERIE_ACTIVA_PATH)
            );
            
            SerializableSerieActivaFrame ssaf = (SerializableSerieActivaFrame) input.readObject();
            input.close();
            
            // testam sa vedem daca ssaf, activitatiS sau jocuriS sunt nule
            // daca da, va fi aruncata o exceptie, si funtia va returna null
            ssaf.getJocuriS().size();
            ssaf.getActivitatiS().size();
            
            if (hasExpired(Calendar.getInstance().getTime())) {
                try {
                    deleteFile(SERIE_ACTIVA_PATH);
                    return null;
                } catch (Exception e){
                    return null;
                }
            }
            return ssaf;
            
        } catch (Exception e) {
            return null;
        }
    }
    
    public SerializableSerieActivaFrame deserializeLastSerieActivaFrame () {
        ObjectInputStream input;
        FileInputStream fin;
        try{
            fin = new FileInputStream(LAST_SERIE_ACTIVA_FRAME_PATH);
            input = new ObjectInputStream(fin);
            
            SerializableSerieActivaFrame ssaf = (SerializableSerieActivaFrame) input.readObject();
            input.close();
            fin.close();
            
            return ssaf;
        } catch (Exception e) {
            return null;
        }
    }
    public SerializablePosteazaProgramFrame deserializeazaPostareProgramFrame () {
        try {
            ObjectInputStream input = new ObjectInputStream(
                    new FileInputStream(POSTEAZA_PROGRAM_PATH)
            );
            
            SerializablePosteazaProgramFrame sppf = (SerializablePosteazaProgramFrame) input.readObject();
            input.close();            
            
//            if (hasExpired(Calendar.getInstance().getTime())){
//                try {
//                    deleteFile(POSTEAZA_PROGRAM_PATH);
//                    return null;
//                } catch (Exception e){
//                    return null;
//                }
//            }
            return sppf;
        } catch (Exception e){
            return null;
        }
    }
    
    public boolean serializaDistribuieProgramFrame (SerializableDistribuieProgramFrame sdpf){
        try {
            ObjectOutputStream output = new ObjectOutputStream (
                    new FileOutputStream (DISTRIBUIE_PROGRAM_PATH)
            );
            
            output.writeObject(sdpf);
            output.close();
            return true;
        } catch (Exception e){
            return false;
        }
    }
    
    public SerializableDistribuieProgramFrame deserializareDistribuieProgramFrame () {
        try {
            ObjectInputStream input = new ObjectInputStream(
                    new FileInputStream(DISTRIBUIE_PROGRAM_PATH)
            );
            
            SerializableDistribuieProgramFrame sdpf = (SerializableDistribuieProgramFrame) input.readObject();
            input.close();
            
//            if (hasExpired(Calendar.getInstance().getTime()) || !canReadDistribuieFrame){
//                canReadDistribuieFrame = true;
//                try {
//                    deleteFile(DISTRIBUIE_PROGRAM_PATH);
//                    return null;
//                } catch (Exception e){
//                    return null;
//                }
//            }
            return sdpf;
        } catch (Exception e){
            System.out.println("Deserializare DistribuieProgramFrame : " + e.getMessage());
            return null;
        } 
    }
    
    public boolean serializeAdaugaInformatiiFrame (SerializableAdaugaInformatiiFrame saif) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(
                    new FileOutputStream(ADAUGA_INFORMATII_PATH)
            );
                        
            output.writeObject(saif);
            output.close();
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public SerializableAdaugaInformatiiFrame deserializeAdaugaInformatiiFrame () {
        try {
            ObjectInputStream input = new ObjectInputStream (
                    new FileInputStream(ADAUGA_INFORMATII_PATH)
            );            
            
            SerializableAdaugaInformatiiFrame saif = (SerializableAdaugaInformatiiFrame) input.readObject();
            input.close();           
            
//            if (hasExpired(Calendar.getInstance().getTime()) || !candReadInforamtiiFrame){
//                canReadDistribuieFrame = true;
//                try {
//                    deleteFile(ADAUGA_INFORMATII_PATH);
//                    return null;
//                } catch (Exception e){
//                    return null;
//                }
//            }
            return saif;
        } catch (Exception e){
            return null;
        }
    }
    
    public boolean serializeDistribuieEchipeFrame (SerializableDistribuieEchipeFrame sdef) {
        try {
            ObjectOutputStream output = new ObjectOutputStream (
                    new FileOutputStream(DISTRIBUIE_ECHIPE_PATH)
            );
            
            output.writeObject(sdef);
            output.close();
            return true;
        } catch (Exception e) {
            System.out.println("Eroare la serializarea DistribuieEchipeFrame : " + e.getMessage());
            return false;
        }
    }
    
    public SerializableDistribuieEchipeFrame deserializeDistribuieEchipeFrame () {
        try {
            ObjectInputStream input = new ObjectInputStream(
                    new FileInputStream(DISTRIBUIE_ECHIPE_PATH)
            );
            
            SerializableDistribuieEchipeFrame sdef = (SerializableDistribuieEchipeFrame) input.readObject();
            
//            if (hasExpired(Calendar.getInstance().getTime()) || !candReadInforamtiiFrame){
//                canReadDistribuieFrame = true;
//                try {
//                    deleteFile(DISTRIBUIE_ECHIPE_PATH);
//                    return null;
//                } catch (Exception e){
//                    return null;
//                }
//            }
            return sdef;
        } catch (Exception e) {
            System.out.println("Eroare la deserializarea DistribuieEchipeFrame : " + e.getMessage());
            return null;
        }
    }

    public boolean isCanReadDistribuieEchipeFrame() {
        return canReadDistribuieEchipeFrame;
    }

    public void setCanReadDistribuieEchipeFrame(boolean canReadDistribuieEchipeFrame) {
        this.canReadDistribuieEchipeFrame = canReadDistribuieEchipeFrame;
    }
    
    public boolean isCanReadSerieActivaFrame() {
        return canReadSerieActivaFrame;
    }

    public void setCanReadSerieActivaFrame(boolean canReadSerieActivaFrame) {
        this.canReadSerieActivaFrame = canReadSerieActivaFrame;
    }
    
    public boolean isCanReadPosteazaFrame() {
        return canReadPosteazaFrame;
    }

    public void setCanReadPosteazaFrame(boolean canReadPosteazaFrame) {
        this.canReadPosteazaFrame = canReadPosteazaFrame;
        if (!canReadPosteazaFrame){
            canReadDistribuieFrame = false;
            candReadInforamtiiFrame = false;
            canReadDistribuieEchipeFrame = false;
        }
    }
    
    
    public boolean isCandReadInforamtiiFrame() {
        return candReadInforamtiiFrame;
    }

    public void setCandReadInforamtiiFrame(boolean candReadInforamtiiFrame) {
        this.candReadInforamtiiFrame = candReadInforamtiiFrame;
        if (!candReadInforamtiiFrame) {
            canReadDistribuieEchipeFrame = false;
        }
    }

    public boolean isCanReadDistribuieFrame() {
        return canReadDistribuieFrame;
    }

    public void setCanReadDistribuieFrame(boolean canReadDistribuieFrame) {
        this.canReadDistribuieFrame = canReadDistribuieFrame;
        if (!canReadDistribuieFrame){
            candReadInforamtiiFrame = false;
            canReadDistribuieEchipeFrame = false;
        }
    }
    
    public boolean savePort (int port) {
        try {
            FileOutputStream fout = new FileOutputStream(PORT_PATH);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            
            out.writeObject(port);
            out.flush();
            out.close();
            fout.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public int getPort () {
        try {
            FileInputStream fin = new FileInputStream(PORT_PATH);
            ObjectInputStream in = new ObjectInputStream(fin);
            
            int port = (Integer) in.readObject();
            in.close();
            fin.close();
            return port;
        } catch (Exception e) {
            return -1;
        }
    }
    
    public boolean serializeFormulaFrames (HashMap<JocGeneralDB, SerializableFormulaFrame> formulas) {
        try {
            FileOutputStream fout = new FileOutputStream(FORMULAS_PATH);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            
            this.formulas = formulas;
            out.writeObject(formulas);
            out.flush();
            
            out.close();
            fout.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public HashMap<JocGeneralDB, SerializableFormulaFrame> deserializeFormulaFrames () {
        try {
            if (this.formulas != null) return formulas; // daca e deja retinuta tabela de formule, n-o mai incarcam o data
            
            FileInputStream fin = new FileInputStream(FORMULAS_PATH);
            ObjectInputStream in = new ObjectInputStream(fin);
            
            HashMap<JocGeneralDB, SerializableFormulaFrame> formulas = (HashMap<JocGeneralDB, SerializableFormulaFrame>) in.readObject();
            
            in.close();
            fin.close();
            
            if (formulas == null) throw new Exception ("null");
            return formulas;
        } catch (Exception e) {
            if (serializeFormulaFrames(new HashMap<JocGeneralDB, SerializableFormulaFrame>())) {
                System.out.println("FormulaFrame : first serialization complete");
            }
            return null;
        }
    }
    
    public SerializableFormulaFrame deserializaFormulaFrame (JocGeneralDB joc) {
        if (joc == null) return null;
        try {
            HashMap<JocGeneralDB, SerializableFormulaFrame> map = deserializeFormulaFrames();
            for (JocGeneralDB j : map.keySet()) {
                if (j.equals(joc)) {
                    return map.get(j);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public boolean serializeFormulaFrame (JocGeneralDB joc, SerializableFormulaFrame sff) {
        if (joc == null || sff == null) return false;
        
        HashMap<JocGeneralDB, SerializableFormulaFrame> formulas = deserializeFormulaFrames();
        
        try {
            boolean found = false;
            for (JocGeneralDB j : formulas.keySet()) {
                if (j.equals(joc)) {
                    formulas.put(j, sff);
                    found = true;
                }
            }
            if (!found) {
                formulas.put(joc, sff);
            }
            return serializeFormulaFrames(formulas);
        } catch (Exception e) {return false;}
    }
    
    public Formula getFormula (JocGeneralDB joc) {
        SerializableFormulaFrame sff = deserializaFormulaFrame(joc);
        if (sff == null) return null;
        
        return FormulaFrame.calculeazaFormula(sff.getFormula());
    }
    
    public boolean updateFormula (JocGeneralDB newJoc, JocGeneralDB oldJoc) {
        try {
            HashMap<JocGeneralDB, SerializableFormulaFrame> formulas = deserializeFormulaFrames();
            
            if (formulas.containsKey(oldJoc)) {
                SerializableFormulaFrame sff = formulas.get(oldJoc);
                formulas.remove(oldJoc);
                formulas.put(newJoc, sff);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    
    //=========================================
    
    public boolean deleteFile (String path) throws IOException {
        File f = new File(path);
        
        return f.delete();
    }
    
    public HashMap<SerieDB, List<EchipaDataDB> > incarcaSerii () {
        FileInputStream fin = null;
        ObjectInputStream in = null;
        try {
            fin = new FileInputStream(ARHIVA_SERII_PATH);
            in = new ObjectInputStream(fin);
            
            HashMap<SerieDB, List<EchipaDataDB> > serii = (HashMap<SerieDB, List<EchipaDataDB> >) in.readObject();
            
            in.close();
            fin.close();
            
            if (serii == null) serii = new HashMap<>();
            
            return serii;
            
        } catch (Exception ex) {
            try {
                fin.close();
            } catch (Exception e) {}
            
            try {
                in.close();
            } catch (Exception e) {}
            
            return new HashMap<>();
        }
        
    }
    
    public boolean salveazaSerii (HashMap<SerieDB, List<EchipaDataDB> > serii) {
        FileOutputStream fout = null;
        ObjectOutputStream out = null;       
        
        try {
            File f = new File(ARHIVA_SERII_PATH);
            f.createNewFile();            
        } catch (Exception e) {}
        
        try {
            fout = new FileOutputStream(ARHIVA_SERII_PATH);
            out = new ObjectOutputStream(fout);
            
            out.writeObject(serii);
            
            out.close();
            fout.close();
            
            return true;
        } catch (Exception e) {
            try { out.close(); } catch (Exception e1) {}
            try { fout.close(); } catch (Exception e2) {}
            
            return false;
        }                
    }
    
    public boolean salveazaSerie (SerieDB serie, List<EchipaDataDB> data) {
        HashMap<SerieDB, List<EchipaDataDB> > serii = incarcaSerii();
        serii.put(serie, data);
        return salveazaSerii(serii);        
    }
    
    
    public Object[] incarcaSerie (SerieDB serie) {
        HashMap<SerieDB, List<EchipaDataDB> > serii = incarcaSerii();
        SerieDB s2 = null;
        List<EchipaDataDB> data = null;
        for (SerieDB s : serii.keySet()) {
            if (s.getIdSerie().equals(serie.getIdSerie())) {
                s2 = s;
                data = serii.get(s);
            }
        }
        
        if (s2 == null) return null;
        
        Object [] objs = new Object[2];
        objs[0] = s2;
        objs[1] = data;
        serii.remove(s2);
        if (!salveazaSerii(serii)) return null;
        return objs;
    }
    
    public boolean serializareCulori (HashMap<String, EchipaDB> colors) {
        FileOutputStream fout;
        ObjectOutputStream out;
        
        try {
            fout = new FileOutputStream(CULORI_PATH);
            out = new ObjectOutputStream(fout);
            
            out.writeObject(colors);
            out.flush();
            out.close();
            fout.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public HashMap<String, EchipaDB> deserializeazaCulori () {
        FileInputStream fin;
        ObjectInputStream in;
        
        try {
            fin = new FileInputStream(CULORI_PATH);
            in = new ObjectInputStream(fin);
            
            HashMap<String, EchipaDB> map = (HashMap<String, EchipaDB>) in.readObject();            

            boolean changed = false;
            
            try {
                for (String s : map.keySet()) {
                    if (!ControllerDB.getInstance().isTeamExists(map.get(s))) {
                        map.put(s, null);
                        changed = true;
                    }
                }
            } catch (Exception e) {}
            
            try {
                for (EchipaDB e : ControllerDB.getInstance().getEchipeBySerie(MainFrame.getSerieActiva())) {
                    if (!map.keySet().contains(e.getCuloareEchipa())) {
                        map.put(e.getCuloareEchipa(), e);
                        changed = true;
                    }
                }
            } catch (Exception e) {}
            
            if (changed) {
                serializareCulori(map);
            }         
                        
            in.close();
            fin.close();
            return map;
        } catch (Exception ex) {
            HashMap<String, EchipaDB> map = new HashMap<>();
            
            boolean changed = false;
            try {
                for (EchipaDB e : ControllerDB.getInstance().getEchipeBySerie(MainFrame.getSerieActiva())) {
                    if (!map.keySet().contains(e.getCuloareEchipa())) {
                        map.put(e.getCuloareEchipa(), e);
                        changed = true;
                    }
                }
            } catch (Exception e) {}
            
            if (changed) {
                serializareCulori(map);
            }
            serializareCulori(map);
            
            return map;
            
        }
    }
    
    public void zipApplication (String outPath, String source, Frame frame, Event event) throws Exception {
        Zipper zipper = new Zipper(outPath, source);
        File f = new File (source);
        
        for (File file : f.listFiles()) {
            if (!StartFrame.folders.contains(file.getName())) continue;
            
            if (file.isDirectory()) {
                queueAdder(zipper, file.getPath());
            } else {
                zipper.add(file);
            }
        }
        //zipper.zip();
        ZippingFrame ui = new ZippingFrame(zipper, frame, false);
        ui.setDoneEvent(event);
        ui.start();
    }
    
    private void queueAdder (Zipper zip, String path) {
        File f = new File (path);
        
        for (File file : f.listFiles()) {
            
            if (file.isDirectory()) {
                queueAdder(zip, file.getPath());
            } else {
                zip.add(file);
            }
        }
    }
    
    public String getXamppPath () {
       BufferedReader bf = null;
       InputStreamReader in = null;
       FileInputStream fis = null;
       
       try {
           fis = new FileInputStream(XAMPP_PATH);
           in = new InputStreamReader(fis);
           bf = new BufferedReader(in);
           
           String path = bf.readLine();
           
           bf.close();
           in.close();
           fis.close();
           return path;
       } catch (Exception e) {
           try { bf.close(); } catch (Exception ex) {}
           try { in.close(); } catch (Exception ex) {}
           try { fis.close(); } catch (Exception ex) {}
           return "";
       }
    }
    
    public void storeXamppPath (String path) {
        FileOutputStream fos = null;
        OutputStreamWriter writer = null;
        try {
            fos = new FileOutputStream(XAMPP_PATH);
            writer = new OutputStreamWriter(fos);
            
            writer.write(path);
            
            writer.close();
            fos.close();
            
        } catch (Exception e) {
            try { writer.close(); } catch (Exception ex) {}
            try { fos.close(); } catch (Exception ex) {}
            e.printStackTrace();
        }
    }
    
    public boolean close (FileInputStream in) {
        try {
            in.close();
            in = null;
            System.gc();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean close (FileOutputStream out) {
        try {
            out.flush();
            out.close();
            out = null;
            System.gc();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean close (ObjectInputStream in) {
        try {
            in.close();
            in = null;
            System.gc();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean close (ObjectOutputStream out) {
        try {
            out.flush();
            out.close();
            out = null;
            System.gc();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean setDataExpirare (Date date) {
        FileOutputStream fout;
        ObjectOutputStream out;
        
        try {
            fout = new FileOutputStream(new File(DATA_EXPIRARE_PATH));
            out = new ObjectOutputStream(fout);
            
            out.writeObject(date);
            out.flush();
            
            out.close();
            fout.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean setDataExpirare (int year, int month, int day, int hour, int minutes) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date;
        try {
            date = sdf.parse(year + "/" + month + "/" + day + " " + hour + ":" + minutes);
        } catch (ParseException ex) {
            Logger.getLogger(SerializeController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        FileOutputStream fout;
        ObjectOutputStream out;
        
        try {
            fout = new FileOutputStream(new File(DATA_EXPIRARE_PATH));
            out = new ObjectOutputStream(fout);
            
            out.writeObject(date);
            out.flush();
            
            out.close();
            fout.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public Date getDataExpirare () {
        FileInputStream fin;
        ObjectInputStream in;
        
        try {
            fin = new FileInputStream(new File(DATA_EXPIRARE_PATH));
            in = new ObjectInputStream(fin);
            
            return (Date) in.readObject();
        } catch (Exception e) {
            return null;
        }
    }
    
    public int compareToExpireDate (Date date) {
        Date expireDate = getDataExpirare(); 
        if (expireDate == null) {
            return 1;
        }
        return date.compareTo(expireDate);
    }
    
    public boolean hasExpired (Date date) {
        return (compareToExpireDate(date) > 0);
    }
    
    public long getProgramActivitatiID () {
        FileInputStream fin;
        ObjectInputStream in;
        
        try {
            fin = new FileInputStream(ID_PROGRAM_PATH);
            in = new ObjectInputStream(fin);
            
            Long ID = (Long) in.readObject();
            
            in.close();
            fin.close();
                        
            return ID;
        } catch (Exception e) {
            long idAct = ControllerDB.getInstance().getActivitateMaxId();
            long idJoc = ControllerDB.getInstance().getJocMaxId();
            long id = (idAct > idJoc ? idAct : idJoc) + 1;
            setProgramActivitatiID(id);
            setDataInceputProgram(Calendar.getInstance().getTime());
            return id;
        }
    }
    
    public long assignProgramActivitatiID () {
        FileInputStream fin;
        ObjectInputStream in;
        
        try {
            fin = new FileInputStream(ID_PROGRAM_PATH);
            in = new ObjectInputStream(fin);
            
            Long ID = (Long) in.readObject();
            
            ID++;
            in.close();
            fin.close();
            setProgramActivitatiID(ID);
            setDataInceputProgram(Calendar.getInstance().getTime());
                        
            return ID;
        } catch (Exception e) {
            long idAct = ControllerDB.getInstance().getActivitateMaxId();
            long idJoc = ControllerDB.getInstance().getJocMaxId();
            long id = (idAct > idJoc ? idAct : idJoc) + 1;
            setProgramActivitatiID(id);
            setDataInceputProgram(Calendar.getInstance().getTime());
            return id;
        }
    }
    
    private boolean setProgramActivitatiID (long id) {
        FileOutputStream fout;
        ObjectOutputStream out;
        
        try {
            fout = new FileOutputStream(ID_PROGRAM_PATH);
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
    
    public Date getDataInceputProgram () {
        FileInputStream fin;
        ObjectInputStream in;
        
        try {
            fin = new FileInputStream(DATA_INCEPUT_PROGRAM_PATH);
            in = new ObjectInputStream(fin);
            
            Date data = (Date) in.readObject();
            
            in.close();
            fin.close();
            
            return data;
        } catch (Exception e) {
            return Calendar.getInstance().getTime();
        }
    }
    
    private boolean setDataInceputProgram (Date date) {
        FileOutputStream fout;
        ObjectOutputStream out;
        
        try {
            fout = new FileOutputStream(DATA_INCEPUT_PROGRAM_PATH);
            out = new ObjectOutputStream(fout);
            
            out.writeObject(date);
            
            out.close();
            fout.close();
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean serializeServerList (DefaultListModel<UserDTO> model) {
        FileOutputStream fout;
        ObjectOutputStream out;
        
        try {
            fout = new FileOutputStream(LISTA_UTILIZATORI_SERVER);
            out = new ObjectOutputStream(fout);
            
            out.writeObject(model);
            out.flush();
            
            out.close();
            fout.close();
            return true;
        }catch (Exception e) {
            return false;
        }
    }  
    
    public DefaultListModel<UserDTO> deserializeServerList () {
        FileInputStream fin;
        ObjectInputStream in;
        
        try {
            fin = new FileInputStream(LISTA_UTILIZATORI_SERVER);
            in = new ObjectInputStream(fin);
            
            DefaultListModel<UserDTO> model = (DefaultListModel<UserDTO>) in.readObject();
            
            in.close();
            fin.close();
            
            return model;
        } catch (Exception e) {
            return null;
        }
    }
    
    public boolean serializeInchirieri (List<Inchiriere> inchirieri) {
        ObjectOutputStream out;
        FileOutputStream fout;
        
        try {
            fout = new FileOutputStream(INCHIRIERI_PATH);
            out = new ObjectOutputStream(fout);
            
            out.writeObject(inchirieri);
            out.flush();
            
            out.close();
            fout.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public List<Inchiriere> deserializeInchirieri () {
        ObjectInputStream in;
        FileInputStream fin;
        
        try {
            fin = new FileInputStream(INCHIRIERI_PATH);
            in = new ObjectInputStream(fin);
            
            List<Inchiriere> inchirieri = (List<Inchiriere>) in.readObject();
            
            in.close();
            fin.close();
            
            return inchirieri;
        } catch (Exception e) {
            List<Inchiriere> inchirieri = new ArrayList<>();
            serializeInchirieri(inchirieri);
            return inchirieri;
        }
    }
}
