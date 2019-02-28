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
import db.EchipaDB;
import db.SerieDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alexandru
 */
public class SerieDAO implements Serializable {

    public SerieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SerieDB serieDB) {
        if (serieDB.getEchipaDBCollection() == null) {
            serieDB.setEchipaDBCollection(new ArrayList<EchipaDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<EchipaDB> attachedEchipaDBCollection = new ArrayList<EchipaDB>();
            for (EchipaDB echipaDBCollectionEchipaDBToAttach : serieDB.getEchipaDBCollection()) {
                echipaDBCollectionEchipaDBToAttach = em.getReference(echipaDBCollectionEchipaDBToAttach.getClass(), echipaDBCollectionEchipaDBToAttach.getIdEchipa());
                attachedEchipaDBCollection.add(echipaDBCollectionEchipaDBToAttach);
            }
            serieDB.setEchipaDBCollection(attachedEchipaDBCollection);
            em.persist(serieDB);
            for (EchipaDB echipaDBCollectionEchipaDB : serieDB.getEchipaDBCollection()) {
                SerieDB oldSerieidSerieOfEchipaDBCollectionEchipaDB = echipaDBCollectionEchipaDB.getSerieidSerie();
                echipaDBCollectionEchipaDB.setSerieidSerie(serieDB);
                echipaDBCollectionEchipaDB = em.merge(echipaDBCollectionEchipaDB);
                if (oldSerieidSerieOfEchipaDBCollectionEchipaDB != null) {
                    oldSerieidSerieOfEchipaDBCollectionEchipaDB.getEchipaDBCollection().remove(echipaDBCollectionEchipaDB);
                    oldSerieidSerieOfEchipaDBCollectionEchipaDB = em.merge(oldSerieidSerieOfEchipaDBCollectionEchipaDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SerieDB serieDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SerieDB persistentSerieDB = em.find(SerieDB.class, serieDB.getIdSerie());
            Collection<EchipaDB> echipaDBCollectionOld = persistentSerieDB.getEchipaDBCollection();
            Collection<EchipaDB> echipaDBCollectionNew = serieDB.getEchipaDBCollection();
            List<String> illegalOrphanMessages = null;
            for (EchipaDB echipaDBCollectionOldEchipaDB : echipaDBCollectionOld) {
                if (!echipaDBCollectionNew.contains(echipaDBCollectionOldEchipaDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EchipaDB " + echipaDBCollectionOldEchipaDB + " since its serieidSerie field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<EchipaDB> attachedEchipaDBCollectionNew = new ArrayList<EchipaDB>();
            for (EchipaDB echipaDBCollectionNewEchipaDBToAttach : echipaDBCollectionNew) {
                echipaDBCollectionNewEchipaDBToAttach = em.getReference(echipaDBCollectionNewEchipaDBToAttach.getClass(), echipaDBCollectionNewEchipaDBToAttach.getIdEchipa());
                attachedEchipaDBCollectionNew.add(echipaDBCollectionNewEchipaDBToAttach);
            }
            echipaDBCollectionNew = attachedEchipaDBCollectionNew;
            serieDB.setEchipaDBCollection(echipaDBCollectionNew);
            serieDB = em.merge(serieDB);
            for (EchipaDB echipaDBCollectionNewEchipaDB : echipaDBCollectionNew) {
                if (!echipaDBCollectionOld.contains(echipaDBCollectionNewEchipaDB)) {
                    SerieDB oldSerieidSerieOfEchipaDBCollectionNewEchipaDB = echipaDBCollectionNewEchipaDB.getSerieidSerie();
                    echipaDBCollectionNewEchipaDB.setSerieidSerie(serieDB);
                    echipaDBCollectionNewEchipaDB = em.merge(echipaDBCollectionNewEchipaDB);
                    if (oldSerieidSerieOfEchipaDBCollectionNewEchipaDB != null && !oldSerieidSerieOfEchipaDBCollectionNewEchipaDB.equals(serieDB)) {
                        oldSerieidSerieOfEchipaDBCollectionNewEchipaDB.getEchipaDBCollection().remove(echipaDBCollectionNewEchipaDB);
                        oldSerieidSerieOfEchipaDBCollectionNewEchipaDB = em.merge(oldSerieidSerieOfEchipaDBCollectionNewEchipaDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = serieDB.getIdSerie();
                if (findSerieDB(id) == null) {
                    throw new NonexistentEntityException("The serieDB with id " + id + " no longer exists.");
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
            SerieDB serieDB;
            try {
                serieDB = em.getReference(SerieDB.class, id);
                serieDB.getIdSerie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The serieDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<EchipaDB> echipaDBCollectionOrphanCheck = serieDB.getEchipaDBCollection();
            for (EchipaDB echipaDBCollectionOrphanCheckEchipaDB : echipaDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SerieDB (" + serieDB + ") cannot be destroyed since the EchipaDB " + echipaDBCollectionOrphanCheckEchipaDB + " in its echipaDBCollection field has a non-nullable serieidSerie field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(serieDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SerieDB> findSerieDBEntities() {
        return findSerieDBEntities(true, -1, -1);
    }

    public List<SerieDB> findSerieDBEntities(int maxResults, int firstResult) {
        return findSerieDBEntities(false, maxResults, firstResult);
    }

    private List<SerieDB> findSerieDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SerieDB.class));
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

    public SerieDB findSerieDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SerieDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getSerieDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SerieDB> rt = cq.from(SerieDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public SerieDB getSingleResult (int nrSerie){
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("SerieDB.findByNumarSerie");
        q.setParameter("numarSerie", nrSerie);
        try{
            return (SerieDB) q.getSingleResult();
        } catch (Exception e){
            return null;
        }
    }
    
    public int insertSerie (SerieDB serie) {
        int res = -1;
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("INSERT INTO serie (idSerie, numarSerie, dataInceput) VALUES (?, ?, ?)");
        q.setParameter(1, serie.getIdSerie());
        q.setParameter(2, serie.getNumarSerie());
        q.setParameter(3, serie.getDataInceput());
        
        em.getTransaction().begin();
        
        try {
            res = q.executeUpdate();
        } catch (Exception e) {
             res = -1;
        } finally {
            em.getTransaction().commit();
            em.close();
        }
        return res;
    }
    
    public int findMaxSerieNumber (){
        Query q = getEntityManager().createNamedQuery("SerieDB.findMaxSerieNumber");
        try {
            return (Integer) q.getSingleResult();
        } catch (Exception e) {
            return 1;
        }
    }
}
