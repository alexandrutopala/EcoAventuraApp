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
import db.JocDB;
import db.JocGeneralDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alexandru
 */
public class JocGeneralDAO implements Serializable {

    public JocGeneralDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(JocGeneralDB jocGeneralDB) {
        if (jocGeneralDB.getJocDBCollection() == null) {
            jocGeneralDB.setJocDBCollection(new ArrayList<JocDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<JocDB> attachedJocDBCollection = new ArrayList<JocDB>();
            for (JocDB jocDBCollectionJocDBToAttach : jocGeneralDB.getJocDBCollection()) {
                jocDBCollectionJocDBToAttach = em.getReference(jocDBCollectionJocDBToAttach.getClass(), jocDBCollectionJocDBToAttach.getIdJoc());
                attachedJocDBCollection.add(jocDBCollectionJocDBToAttach);
            }
            jocGeneralDB.setJocDBCollection(attachedJocDBCollection);
            em.persist(jocGeneralDB);
            for (JocDB jocDBCollectionJocDB : jocGeneralDB.getJocDBCollection()) {
                JocGeneralDB oldJocGeneralidJocGeneralOfJocDBCollectionJocDB = jocDBCollectionJocDB.getJocGeneralidJocGeneral();
                jocDBCollectionJocDB.setJocGeneralidJocGeneral(jocGeneralDB);
                jocDBCollectionJocDB = em.merge(jocDBCollectionJocDB);
                if (oldJocGeneralidJocGeneralOfJocDBCollectionJocDB != null) {
                    oldJocGeneralidJocGeneralOfJocDBCollectionJocDB.getJocDBCollection().remove(jocDBCollectionJocDB);
                    oldJocGeneralidJocGeneralOfJocDBCollectionJocDB = em.merge(oldJocGeneralidJocGeneralOfJocDBCollectionJocDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(JocGeneralDB jocGeneralDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            JocGeneralDB persistentJocGeneralDB = em.find(JocGeneralDB.class, jocGeneralDB.getIdJocGeneral());
            Collection<JocDB> jocDBCollectionOld = persistentJocGeneralDB.getJocDBCollection();
            Collection<JocDB> jocDBCollectionNew = jocGeneralDB.getJocDBCollection();
            List<String> illegalOrphanMessages = null;
            for (JocDB jocDBCollectionOldJocDB : jocDBCollectionOld) {
                if (!jocDBCollectionNew.contains(jocDBCollectionOldJocDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain JocDB " + jocDBCollectionOldJocDB + " since its jocGeneralidJocGeneral field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<JocDB> attachedJocDBCollectionNew = new ArrayList<JocDB>();
            for (JocDB jocDBCollectionNewJocDBToAttach : jocDBCollectionNew) {
                jocDBCollectionNewJocDBToAttach = em.getReference(jocDBCollectionNewJocDBToAttach.getClass(), jocDBCollectionNewJocDBToAttach.getIdJoc());
                attachedJocDBCollectionNew.add(jocDBCollectionNewJocDBToAttach);
            }
            jocDBCollectionNew = attachedJocDBCollectionNew;
            jocGeneralDB.setJocDBCollection(jocDBCollectionNew);
            jocGeneralDB = em.merge(jocGeneralDB);
            for (JocDB jocDBCollectionNewJocDB : jocDBCollectionNew) {
                if (!jocDBCollectionOld.contains(jocDBCollectionNewJocDB)) {
                    JocGeneralDB oldJocGeneralidJocGeneralOfJocDBCollectionNewJocDB = jocDBCollectionNewJocDB.getJocGeneralidJocGeneral();
                    jocDBCollectionNewJocDB.setJocGeneralidJocGeneral(jocGeneralDB);
                    jocDBCollectionNewJocDB = em.merge(jocDBCollectionNewJocDB);
                    if (oldJocGeneralidJocGeneralOfJocDBCollectionNewJocDB != null && !oldJocGeneralidJocGeneralOfJocDBCollectionNewJocDB.equals(jocGeneralDB)) {
                        oldJocGeneralidJocGeneralOfJocDBCollectionNewJocDB.getJocDBCollection().remove(jocDBCollectionNewJocDB);
                        oldJocGeneralidJocGeneralOfJocDBCollectionNewJocDB = em.merge(oldJocGeneralidJocGeneralOfJocDBCollectionNewJocDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = jocGeneralDB.getIdJocGeneral();
                if (findJocGeneralDB(id) == null) {
                    throw new NonexistentEntityException("The jocGeneralDB with id " + id + " no longer exists.");
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
            JocGeneralDB jocGeneralDB;
            try {
                jocGeneralDB = em.getReference(JocGeneralDB.class, id);
                jocGeneralDB.getIdJocGeneral();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jocGeneralDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<JocDB> jocDBCollectionOrphanCheck = jocGeneralDB.getJocDBCollection();
            for (JocDB jocDBCollectionOrphanCheckJocDB : jocDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This JocGeneralDB (" + jocGeneralDB + ") cannot be destroyed since the JocDB " + jocDBCollectionOrphanCheckJocDB + " in its jocDBCollection field has a non-nullable jocGeneralidJocGeneral field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(jocGeneralDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<JocGeneralDB> findJocGeneralDBEntities() {
        return findJocGeneralDBEntities(true, -1, -1);
    }

    public List<JocGeneralDB> findJocGeneralDBEntities(int maxResults, int firstResult) {
        return findJocGeneralDBEntities(false, maxResults, firstResult);
    }

    private List<JocGeneralDB> findJocGeneralDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(JocGeneralDB.class));
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

    public JocGeneralDB findJocGeneralDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(JocGeneralDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getJocGeneralDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<JocGeneralDB> rt = cq.from(JocGeneralDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public JocGeneralDB getSingleResult (String nume) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("JocGeneralDB.findByNumeJocGeneral");
        q.setParameter("numeJocGeneral", nume);
        
        try {
            return (JocGeneralDB) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public int insertJocGeneral (JocGeneralDB j) {
        int res = -1;
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("INSERT INTO jocgeneral (idJocGeneral, numeJocGeneral, descriereJoc) VALUES (?, ?, ?)");
        q.setParameter(1, j.getIdJocGeneral());
        q.setParameter(2, j.getNumeJocGeneral());
        q.setParameter(3, j.getDescriereJoc());
        
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
