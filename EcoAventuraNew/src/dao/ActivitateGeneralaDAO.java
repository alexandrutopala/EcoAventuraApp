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
import db.ActivitateDB;
import db.ActivitateGeneralaDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alexandru
 */
public class ActivitateGeneralaDAO implements Serializable {

    public ActivitateGeneralaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ActivitateGeneralaDB activitateGeneralaDB) {
        if (activitateGeneralaDB.getActivitateDBCollection() == null) {
            activitateGeneralaDB.setActivitateDBCollection(new ArrayList<ActivitateDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ActivitateDB> attachedActivitateDBCollection = new ArrayList<ActivitateDB>();
            for (ActivitateDB activitateDBCollectionActivitateDBToAttach : activitateGeneralaDB.getActivitateDBCollection()) {
                activitateDBCollectionActivitateDBToAttach = em.getReference(activitateDBCollectionActivitateDBToAttach.getClass(), activitateDBCollectionActivitateDBToAttach.getIdActivitate());
                attachedActivitateDBCollection.add(activitateDBCollectionActivitateDBToAttach);
            }
            activitateGeneralaDB.setActivitateDBCollection(attachedActivitateDBCollection);
            em.persist(activitateGeneralaDB);
            for (ActivitateDB activitateDBCollectionActivitateDB : activitateGeneralaDB.getActivitateDBCollection()) {
                ActivitateGeneralaDB oldActivitateGeneralaidActivitateGeneralaOfActivitateDBCollectionActivitateDB = activitateDBCollectionActivitateDB.getActivitateGeneralaidActivitateGenerala();
                activitateDBCollectionActivitateDB.setActivitateGeneralaidActivitateGenerala(activitateGeneralaDB);
                activitateDBCollectionActivitateDB = em.merge(activitateDBCollectionActivitateDB);
                if (oldActivitateGeneralaidActivitateGeneralaOfActivitateDBCollectionActivitateDB != null) {
                    oldActivitateGeneralaidActivitateGeneralaOfActivitateDBCollectionActivitateDB.getActivitateDBCollection().remove(activitateDBCollectionActivitateDB);
                    oldActivitateGeneralaidActivitateGeneralaOfActivitateDBCollectionActivitateDB = em.merge(oldActivitateGeneralaidActivitateGeneralaOfActivitateDBCollectionActivitateDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ActivitateGeneralaDB activitateGeneralaDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ActivitateGeneralaDB persistentActivitateGeneralaDB = em.find(ActivitateGeneralaDB.class, activitateGeneralaDB.getIdActivitateGenerala());
            Collection<ActivitateDB> activitateDBCollectionOld = persistentActivitateGeneralaDB.getActivitateDBCollection();
            Collection<ActivitateDB> activitateDBCollectionNew = activitateGeneralaDB.getActivitateDBCollection();
            List<String> illegalOrphanMessages = null;
            for (ActivitateDB activitateDBCollectionOldActivitateDB : activitateDBCollectionOld) {
                if (!activitateDBCollectionNew.contains(activitateDBCollectionOldActivitateDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ActivitateDB " + activitateDBCollectionOldActivitateDB + " since its activitateGeneralaidActivitateGenerala field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ActivitateDB> attachedActivitateDBCollectionNew = new ArrayList<ActivitateDB>();
            for (ActivitateDB activitateDBCollectionNewActivitateDBToAttach : activitateDBCollectionNew) {
                activitateDBCollectionNewActivitateDBToAttach = em.getReference(activitateDBCollectionNewActivitateDBToAttach.getClass(), activitateDBCollectionNewActivitateDBToAttach.getIdActivitate());
                attachedActivitateDBCollectionNew.add(activitateDBCollectionNewActivitateDBToAttach);
            }
            activitateDBCollectionNew = attachedActivitateDBCollectionNew;
            activitateGeneralaDB.setActivitateDBCollection(activitateDBCollectionNew);
            activitateGeneralaDB = em.merge(activitateGeneralaDB);
            for (ActivitateDB activitateDBCollectionNewActivitateDB : activitateDBCollectionNew) {
                if (!activitateDBCollectionOld.contains(activitateDBCollectionNewActivitateDB)) {
                    ActivitateGeneralaDB oldActivitateGeneralaidActivitateGeneralaOfActivitateDBCollectionNewActivitateDB = activitateDBCollectionNewActivitateDB.getActivitateGeneralaidActivitateGenerala();
                    activitateDBCollectionNewActivitateDB.setActivitateGeneralaidActivitateGenerala(activitateGeneralaDB);
                    activitateDBCollectionNewActivitateDB = em.merge(activitateDBCollectionNewActivitateDB);
                    if (oldActivitateGeneralaidActivitateGeneralaOfActivitateDBCollectionNewActivitateDB != null && !oldActivitateGeneralaidActivitateGeneralaOfActivitateDBCollectionNewActivitateDB.equals(activitateGeneralaDB)) {
                        oldActivitateGeneralaidActivitateGeneralaOfActivitateDBCollectionNewActivitateDB.getActivitateDBCollection().remove(activitateDBCollectionNewActivitateDB);
                        oldActivitateGeneralaidActivitateGeneralaOfActivitateDBCollectionNewActivitateDB = em.merge(oldActivitateGeneralaidActivitateGeneralaOfActivitateDBCollectionNewActivitateDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = activitateGeneralaDB.getIdActivitateGenerala();
                if (findActivitateGeneralaDB(id) == null) {
                    throw new NonexistentEntityException("The activitateGeneralaDB with id " + id + " no longer exists.");
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
            ActivitateGeneralaDB activitateGeneralaDB;
            try {
                activitateGeneralaDB = em.getReference(ActivitateGeneralaDB.class, id);
                activitateGeneralaDB.getIdActivitateGenerala();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The activitateGeneralaDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ActivitateDB> activitateDBCollectionOrphanCheck = activitateGeneralaDB.getActivitateDBCollection();
            for (ActivitateDB activitateDBCollectionOrphanCheckActivitateDB : activitateDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ActivitateGeneralaDB (" + activitateGeneralaDB + ") cannot be destroyed since the ActivitateDB " + activitateDBCollectionOrphanCheckActivitateDB + " in its activitateDBCollection field has a non-nullable activitateGeneralaidActivitateGenerala field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(activitateGeneralaDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ActivitateGeneralaDB> findActivitateGeneralaDBEntities() {
        return findActivitateGeneralaDBEntities(true, -1, -1);
    }

    public List<ActivitateGeneralaDB> findActivitateGeneralaDBEntities(int maxResults, int firstResult) {
        return findActivitateGeneralaDBEntities(false, maxResults, firstResult);
    }

    private List<ActivitateGeneralaDB> findActivitateGeneralaDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ActivitateGeneralaDB.class));
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

    public ActivitateGeneralaDB findActivitateGeneralaDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ActivitateGeneralaDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getActivitateGeneralaDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ActivitateGeneralaDB> rt = cq.from(ActivitateGeneralaDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public ActivitateGeneralaDB getSingleResult (String nume) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("ActivitateGeneralaDB.findByNumeActivitateGenerala");
        q.setParameter("numeActivitateGenerala", nume);
        
        try {
            return (ActivitateGeneralaDB) q.getSingleResult();
        } catch (Exception e){
            return null;
        }
    }
    
    public int insertActivitateGenerala (ActivitateGeneralaDB ag) {
        int res = -1;
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("INSERT INTO activitategenerala (idActivitateGenerala, numeActivitateGenerala) VALUES (?, ?)");
        q.setParameter(1, ag.getIdActivitateGenerala());
        q.setParameter(2, ag.getNumeActivitateGenerala());
        
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
}
