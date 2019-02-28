/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import db.SerieDB;
import db.ActivitateDB;
import db.EchipaDB;
import java.util.ArrayList;
import java.util.Collection;
import db.MembruEchipaDB;
import db.JocDB;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alexandru
 */
public class EchipaDAO implements Serializable {

    public EchipaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EchipaDB echipaDB) {
        if (echipaDB.getActivitateDBCollection() == null) {
            echipaDB.setActivitateDBCollection(new ArrayList<ActivitateDB>());
        }
        if (echipaDB.getMembruEchipaDBCollection() == null) {
            echipaDB.setMembruEchipaDBCollection(new ArrayList<MembruEchipaDB>());
        }
        if (echipaDB.getJocDBCollection() == null) {
            echipaDB.setJocDBCollection(new ArrayList<JocDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SerieDB serieidSerie = echipaDB.getSerieidSerie();
            if (serieidSerie != null) {
                serieidSerie = em.getReference(serieidSerie.getClass(), serieidSerie.getIdSerie());
                echipaDB.setSerieidSerie(serieidSerie);
            }
            Collection<ActivitateDB> attachedActivitateDBCollection = new ArrayList<ActivitateDB>();
            for (ActivitateDB activitateDBCollectionActivitateDBToAttach : echipaDB.getActivitateDBCollection()) {
                activitateDBCollectionActivitateDBToAttach = em.getReference(activitateDBCollectionActivitateDBToAttach.getClass(), activitateDBCollectionActivitateDBToAttach.getIdActivitate());
                attachedActivitateDBCollection.add(activitateDBCollectionActivitateDBToAttach);
            }
            echipaDB.setActivitateDBCollection(attachedActivitateDBCollection);
            Collection<MembruEchipaDB> attachedMembruEchipaDBCollection = new ArrayList<MembruEchipaDB>();
            for (MembruEchipaDB membruEchipaDBCollectionMembruEchipaDBToAttach : echipaDB.getMembruEchipaDBCollection()) {
                membruEchipaDBCollectionMembruEchipaDBToAttach = em.getReference(membruEchipaDBCollectionMembruEchipaDBToAttach.getClass(), membruEchipaDBCollectionMembruEchipaDBToAttach.getIdMembruEchipa());
                attachedMembruEchipaDBCollection.add(membruEchipaDBCollectionMembruEchipaDBToAttach);
            }
            echipaDB.setMembruEchipaDBCollection(attachedMembruEchipaDBCollection);
            Collection<JocDB> attachedJocDBCollection = new ArrayList<JocDB>();
            for (JocDB jocDBCollectionJocDBToAttach : echipaDB.getJocDBCollection()) {
                jocDBCollectionJocDBToAttach = em.getReference(jocDBCollectionJocDBToAttach.getClass(), jocDBCollectionJocDBToAttach.getIdJoc());
                attachedJocDBCollection.add(jocDBCollectionJocDBToAttach);
            }
            echipaDB.setJocDBCollection(attachedJocDBCollection);
            em.persist(echipaDB);
            if (serieidSerie != null) {
                serieidSerie.getEchipaDBCollection().add(echipaDB);
                serieidSerie = em.merge(serieidSerie);
            }
            for (ActivitateDB activitateDBCollectionActivitateDB : echipaDB.getActivitateDBCollection()) {
                EchipaDB oldEchipaidEchipaOfActivitateDBCollectionActivitateDB = activitateDBCollectionActivitateDB.getEchipaidEchipa();
                activitateDBCollectionActivitateDB.setEchipaidEchipa(echipaDB);
                activitateDBCollectionActivitateDB = em.merge(activitateDBCollectionActivitateDB);
                if (oldEchipaidEchipaOfActivitateDBCollectionActivitateDB != null) {
                    oldEchipaidEchipaOfActivitateDBCollectionActivitateDB.getActivitateDBCollection().remove(activitateDBCollectionActivitateDB);
                    oldEchipaidEchipaOfActivitateDBCollectionActivitateDB = em.merge(oldEchipaidEchipaOfActivitateDBCollectionActivitateDB);
                }
            }
            for (MembruEchipaDB membruEchipaDBCollectionMembruEchipaDB : echipaDB.getMembruEchipaDBCollection()) {
                EchipaDB oldEchipaidEchipaOfMembruEchipaDBCollectionMembruEchipaDB = membruEchipaDBCollectionMembruEchipaDB.getEchipaidEchipa();
                membruEchipaDBCollectionMembruEchipaDB.setEchipaidEchipa(echipaDB);
                membruEchipaDBCollectionMembruEchipaDB = em.merge(membruEchipaDBCollectionMembruEchipaDB);
                if (oldEchipaidEchipaOfMembruEchipaDBCollectionMembruEchipaDB != null) {
                    oldEchipaidEchipaOfMembruEchipaDBCollectionMembruEchipaDB.getMembruEchipaDBCollection().remove(membruEchipaDBCollectionMembruEchipaDB);
                    oldEchipaidEchipaOfMembruEchipaDBCollectionMembruEchipaDB = em.merge(oldEchipaidEchipaOfMembruEchipaDBCollectionMembruEchipaDB);
                }
            }
            for (JocDB jocDBCollectionJocDB : echipaDB.getJocDBCollection()) {
                EchipaDB oldEchipaidEchipaOfJocDBCollectionJocDB = jocDBCollectionJocDB.getEchipaidEchipa();
                jocDBCollectionJocDB.setEchipaidEchipa(echipaDB);
                jocDBCollectionJocDB = em.merge(jocDBCollectionJocDB);
                if (oldEchipaidEchipaOfJocDBCollectionJocDB != null) {
                    oldEchipaidEchipaOfJocDBCollectionJocDB.getJocDBCollection().remove(jocDBCollectionJocDB);
                    oldEchipaidEchipaOfJocDBCollectionJocDB = em.merge(oldEchipaidEchipaOfJocDBCollectionJocDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EchipaDB echipaDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EchipaDB persistentEchipaDB = em.find(EchipaDB.class, echipaDB.getIdEchipa());
            SerieDB serieidSerieOld = persistentEchipaDB.getSerieidSerie();
            SerieDB serieidSerieNew = echipaDB.getSerieidSerie();
            Collection<ActivitateDB> activitateDBCollectionOld = persistentEchipaDB.getActivitateDBCollection();
            Collection<ActivitateDB> activitateDBCollectionNew = echipaDB.getActivitateDBCollection();
            Collection<MembruEchipaDB> membruEchipaDBCollectionOld = persistentEchipaDB.getMembruEchipaDBCollection();
            Collection<MembruEchipaDB> membruEchipaDBCollectionNew = echipaDB.getMembruEchipaDBCollection();
            Collection<JocDB> jocDBCollectionOld = persistentEchipaDB.getJocDBCollection();
            Collection<JocDB> jocDBCollectionNew = echipaDB.getJocDBCollection();
            List<String> illegalOrphanMessages = null;
            for (ActivitateDB activitateDBCollectionOldActivitateDB : activitateDBCollectionOld) {
                if (!activitateDBCollectionNew.contains(activitateDBCollectionOldActivitateDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ActivitateDB " + activitateDBCollectionOldActivitateDB + " since its echipaidEchipa field is not nullable.");
                }
            }
            for (MembruEchipaDB membruEchipaDBCollectionOldMembruEchipaDB : membruEchipaDBCollectionOld) {
                if (!membruEchipaDBCollectionNew.contains(membruEchipaDBCollectionOldMembruEchipaDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MembruEchipaDB " + membruEchipaDBCollectionOldMembruEchipaDB + " since its echipaidEchipa field is not nullable.");
                }
            }
            for (JocDB jocDBCollectionOldJocDB : jocDBCollectionOld) {
                if (!jocDBCollectionNew.contains(jocDBCollectionOldJocDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain JocDB " + jocDBCollectionOldJocDB + " since its echipaidEchipa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (serieidSerieNew != null) {
                serieidSerieNew = em.getReference(serieidSerieNew.getClass(), serieidSerieNew.getIdSerie());
                echipaDB.setSerieidSerie(serieidSerieNew);
            }
            Collection<ActivitateDB> attachedActivitateDBCollectionNew = new ArrayList<ActivitateDB>();
            for (ActivitateDB activitateDBCollectionNewActivitateDBToAttach : activitateDBCollectionNew) {
                activitateDBCollectionNewActivitateDBToAttach = em.getReference(activitateDBCollectionNewActivitateDBToAttach.getClass(), activitateDBCollectionNewActivitateDBToAttach.getIdActivitate());
                attachedActivitateDBCollectionNew.add(activitateDBCollectionNewActivitateDBToAttach);
            }
            activitateDBCollectionNew = attachedActivitateDBCollectionNew;
            echipaDB.setActivitateDBCollection(activitateDBCollectionNew);
            Collection<MembruEchipaDB> attachedMembruEchipaDBCollectionNew = new ArrayList<MembruEchipaDB>();
            for (MembruEchipaDB membruEchipaDBCollectionNewMembruEchipaDBToAttach : membruEchipaDBCollectionNew) {
                membruEchipaDBCollectionNewMembruEchipaDBToAttach = em.getReference(membruEchipaDBCollectionNewMembruEchipaDBToAttach.getClass(), membruEchipaDBCollectionNewMembruEchipaDBToAttach.getIdMembruEchipa());
                attachedMembruEchipaDBCollectionNew.add(membruEchipaDBCollectionNewMembruEchipaDBToAttach);
            }
            membruEchipaDBCollectionNew = attachedMembruEchipaDBCollectionNew;
            echipaDB.setMembruEchipaDBCollection(membruEchipaDBCollectionNew);
            Collection<JocDB> attachedJocDBCollectionNew = new ArrayList<JocDB>();
            for (JocDB jocDBCollectionNewJocDBToAttach : jocDBCollectionNew) {
                jocDBCollectionNewJocDBToAttach = em.getReference(jocDBCollectionNewJocDBToAttach.getClass(), jocDBCollectionNewJocDBToAttach.getIdJoc());
                attachedJocDBCollectionNew.add(jocDBCollectionNewJocDBToAttach);
            }
            jocDBCollectionNew = attachedJocDBCollectionNew;
            echipaDB.setJocDBCollection(jocDBCollectionNew);
            echipaDB = em.merge(echipaDB);
            if (serieidSerieOld != null && !serieidSerieOld.equals(serieidSerieNew)) {
                serieidSerieOld.getEchipaDBCollection().remove(echipaDB);
                serieidSerieOld = em.merge(serieidSerieOld);
            }
            if (serieidSerieNew != null && !serieidSerieNew.equals(serieidSerieOld)) {
                serieidSerieNew.getEchipaDBCollection().add(echipaDB);
                serieidSerieNew = em.merge(serieidSerieNew);
            }
            for (ActivitateDB activitateDBCollectionNewActivitateDB : activitateDBCollectionNew) {
                if (!activitateDBCollectionOld.contains(activitateDBCollectionNewActivitateDB)) {
                    EchipaDB oldEchipaidEchipaOfActivitateDBCollectionNewActivitateDB = activitateDBCollectionNewActivitateDB.getEchipaidEchipa();
                    activitateDBCollectionNewActivitateDB.setEchipaidEchipa(echipaDB);
                    activitateDBCollectionNewActivitateDB = em.merge(activitateDBCollectionNewActivitateDB);
                    if (oldEchipaidEchipaOfActivitateDBCollectionNewActivitateDB != null && !oldEchipaidEchipaOfActivitateDBCollectionNewActivitateDB.equals(echipaDB)) {
                        oldEchipaidEchipaOfActivitateDBCollectionNewActivitateDB.getActivitateDBCollection().remove(activitateDBCollectionNewActivitateDB);
                        oldEchipaidEchipaOfActivitateDBCollectionNewActivitateDB = em.merge(oldEchipaidEchipaOfActivitateDBCollectionNewActivitateDB);
                    }
                }
            }
            for (MembruEchipaDB membruEchipaDBCollectionNewMembruEchipaDB : membruEchipaDBCollectionNew) {
                if (!membruEchipaDBCollectionOld.contains(membruEchipaDBCollectionNewMembruEchipaDB)) {
                    EchipaDB oldEchipaidEchipaOfMembruEchipaDBCollectionNewMembruEchipaDB = membruEchipaDBCollectionNewMembruEchipaDB.getEchipaidEchipa();
                    membruEchipaDBCollectionNewMembruEchipaDB.setEchipaidEchipa(echipaDB);
                    membruEchipaDBCollectionNewMembruEchipaDB = em.merge(membruEchipaDBCollectionNewMembruEchipaDB);
                    if (oldEchipaidEchipaOfMembruEchipaDBCollectionNewMembruEchipaDB != null && !oldEchipaidEchipaOfMembruEchipaDBCollectionNewMembruEchipaDB.equals(echipaDB)) {
                        oldEchipaidEchipaOfMembruEchipaDBCollectionNewMembruEchipaDB.getMembruEchipaDBCollection().remove(membruEchipaDBCollectionNewMembruEchipaDB);
                        oldEchipaidEchipaOfMembruEchipaDBCollectionNewMembruEchipaDB = em.merge(oldEchipaidEchipaOfMembruEchipaDBCollectionNewMembruEchipaDB);
                    }
                }
            }
            for (JocDB jocDBCollectionNewJocDB : jocDBCollectionNew) {
                if (!jocDBCollectionOld.contains(jocDBCollectionNewJocDB)) {
                    EchipaDB oldEchipaidEchipaOfJocDBCollectionNewJocDB = jocDBCollectionNewJocDB.getEchipaidEchipa();
                    jocDBCollectionNewJocDB.setEchipaidEchipa(echipaDB);
                    jocDBCollectionNewJocDB = em.merge(jocDBCollectionNewJocDB);
                    if (oldEchipaidEchipaOfJocDBCollectionNewJocDB != null && !oldEchipaidEchipaOfJocDBCollectionNewJocDB.equals(echipaDB)) {
                        oldEchipaidEchipaOfJocDBCollectionNewJocDB.getJocDBCollection().remove(jocDBCollectionNewJocDB);
                        oldEchipaidEchipaOfJocDBCollectionNewJocDB = em.merge(oldEchipaidEchipaOfJocDBCollectionNewJocDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = echipaDB.getIdEchipa();
                if (findEchipaDB(id) == null) {
                    throw new NonexistentEntityException("The echipaDB with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EchipaDB echipaDB;
            try {
                echipaDB = em.getReference(EchipaDB.class, id);
                echipaDB.getIdEchipa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The echipaDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ActivitateDB> activitateDBCollectionOrphanCheck = echipaDB.getActivitateDBCollection();
            for (ActivitateDB activitateDBCollectionOrphanCheckActivitateDB : activitateDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EchipaDB (" + echipaDB + ") cannot be destroyed since the ActivitateDB " + activitateDBCollectionOrphanCheckActivitateDB + " in its activitateDBCollection field has a non-nullable echipaidEchipa field.");
            }
            Collection<MembruEchipaDB> membruEchipaDBCollectionOrphanCheck = echipaDB.getMembruEchipaDBCollection();
            for (MembruEchipaDB membruEchipaDBCollectionOrphanCheckMembruEchipaDB : membruEchipaDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EchipaDB (" + echipaDB + ") cannot be destroyed since the MembruEchipaDB " + membruEchipaDBCollectionOrphanCheckMembruEchipaDB + " in its membruEchipaDBCollection field has a non-nullable echipaidEchipa field.");
            }
            Collection<JocDB> jocDBCollectionOrphanCheck = echipaDB.getJocDBCollection();
            for (JocDB jocDBCollectionOrphanCheckJocDB : jocDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EchipaDB (" + echipaDB + ") cannot be destroyed since the JocDB " + jocDBCollectionOrphanCheckJocDB + " in its jocDBCollection field has a non-nullable echipaidEchipa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            SerieDB serieidSerie = echipaDB.getSerieidSerie();
            if (serieidSerie != null) {
                serieidSerie.getEchipaDBCollection().remove(echipaDB);
                serieidSerie = em.merge(serieidSerie);
            }
            em.remove(echipaDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EchipaDB> findEchipaDBEntities() {
        return findEchipaDBEntities(true, -1, -1);
    }

    public List<EchipaDB> findEchipaDBEntities(int maxResults, int firstResult) {
        return findEchipaDBEntities(false, maxResults, firstResult);
    }

    private List<EchipaDB> findEchipaDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EchipaDB.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public EchipaDB findEchipaDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EchipaDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getEchipaDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EchipaDB> rt = cq.from(EchipaDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public EchipaDB getSingleResult (String nume, SerieDB serie){
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("EchipaDB.findByNumeEchipaAndSerie");
        q.setParameter("numeEchipa", nume);
        q.setParameter("serieidSerie", serie);
        
        try {
            return (EchipaDB) q.getSingleResult();
        } catch (Exception e){
            return null;
        }
    }
    
    public List<EchipaDB> findEchipeBySerie (SerieDB serie) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("EchipaDB.findByIdSerie");
        q.setParameter("serieidSerie", serie);
        
        return q.getResultList();
    }
    
    public int insertEchipa (EchipaDB e) {
        int res = -1;
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("INSERT INTO echipa (idEchipa, numeEchipa, culoareEchipa, scoalaEchipa, profEchipa, Serie_idSerie) VALUES (?, ?, ?, ?, ?, ?)");
        q.setParameter(1, e.getIdEchipa());
        q.setParameter(2, e.getNumeEchipa());
        q.setParameter(3, e.getCuloareEchipa());
        q.setParameter(4, e.getScoalaEchipa());
        q.setParameter(5, e.getProfEchipa());
        q.setParameter(6, e.getSerieidSerie().getIdSerie());
        
        em.getTransaction().begin();
        
        try {
            res = q.executeUpdate();
        } catch (Exception ex) {
            res = -1;
        } finally {
            em.getTransaction().commit();
            em.close();
        }
        return res;
    }
    
    public void preventLazy (int id) {
        EchipaDB e = getEntityManager().find(EchipaDB.class, id);
        
        e.getActivitateDBCollection().size();
        e.getJocDBCollection().size();
        e.getMembruEchipaDBCollection().size();
    }
}
