/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Magazin.service;

import Magazin.dao.ProdusDAO;
import Magazin.dao.TranzactieDAO;
import Magazin.dao.exceptions.IllegalOrphanException;
import Magazin.dao.exceptions.NonexistentEntityException;
import Magazin.db.ProdusDB;
import Magazin.db.TranzactieDB;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Marius
 */
public class MagazinController {
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static MagazinController singleton = null;
    private final ProdusDAO produsDao;
    private final TranzactieDAO tranzactieDao;
    private EntityManagerFactory emf;
    
    private MagazinController () {
        emf = Persistence.createEntityManagerFactory("MagazinSE");
        produsDao = new ProdusDAO(emf);
        tranzactieDao = new TranzactieDAO(emf);
    }
    
    public static MagazinController getInstance () {
        if (singleton == null) {
            singleton = new MagazinController();
        }
        return singleton;
    }
    
    public List<ProdusDB> findAllProduse () {
        return produsDao.findProdusDBEntities();
    }
    
    public ProdusDB adaugaProdus (String denumire, float pret, int stoc) {
        ProdusDB p = new ProdusDB();
        p.setDenumire(denumire);
        p.setPret(pret);
        p.setStoc(stoc);
        
        produsDao.create(p);
        return p;
    }
    
    public boolean updateProdus (ProdusDB p) {
        try {
            produsDao.edit(p);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean stergeProdus (ProdusDB p) {
        try {
            for (TranzactieDB t : p.getTranzactieDBCollection()) {
                if (!stergeTranzactie(t)) {
                    throw new Exception();
                }
            }
            produsDao.destroy(p.getIdProdus());
            return true;
        } catch (Exception ex) {
            return false;
        } 
    }
    
    public TranzactieDB createTranzactie (boolean adaugate, int bucati, ProdusDB p) {
        TranzactieDB t = new TranzactieDB();
        t.setAdaugate(adaugate);
        t.setBucati(bucati);
        t.setData(sdf.format(Calendar.getInstance().getTime()));
        t.setIdProdus(p);
        t.setPret(p.getPret() * ((float) bucati));
        
        tranzactieDao.create(t);
        return t;
    }
    
    public TranzactieDB createTranzactie (TranzactieDB t) {
        tranzactieDao.create(t);
        return t;
    }
    
    public boolean stergeTranzactie (TranzactieDB t) {
        try {
            tranzactieDao.destroy(t.getIdTranzactie());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public List<TranzactieDB> findAllTranzactii () {
        return tranzactieDao.findTranzactieDBEntities();
    }
    
    public boolean stergeToateProdusele () {
        List<ProdusDB> produse = produsDao.findProdusDBEntities();
        
        for (ProdusDB p : produse) {
            if (!stergeProdus(p)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean stergeToateTranzactiile () {
        List<TranzactieDB> tranzactii = tranzactieDao.findTranzactieDBEntities();
        
        for (TranzactieDB t : tranzactii) {
            if (!stergeTranzactie(t)) {
                return false;
            }
        }
        return true;
    }
    
    public void disconnect () {
        emf.close();
        emf = null;
    }
}
