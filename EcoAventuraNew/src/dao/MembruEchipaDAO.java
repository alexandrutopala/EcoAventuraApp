/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import db.EchipaDB;
import db.MembruEchipaDB;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alexandru
 */
public class MembruEchipaDAO implements Serializable {

    public MembruEchipaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MembruEchipaDB membruEchipaDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EchipaDB echipaidEchipa = membruEchipaDB.getEchipaidEchipa();
            if (echipaidEchipa != null) {
                echipaidEchipa = em.getReference(echipaidEchipa.getClass(), echipaidEchipa.getIdEchipa());
                membruEchipaDB.setEchipaidEchipa(echipaidEchipa);
            }
            em.persist(membruEchipaDB);
            if (echipaidEchipa != null) {
                echipaidEchipa.getMembruEchipaDBCollection().add(membruEchipaDB);
                echipaidEchipa = em.merge(echipaidEchipa);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MembruEchipaDB membruEchipaDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MembruEchipaDB persistentMembruEchipaDB = em.find(MembruEchipaDB.class, membruEchipaDB.getIdMembruEchipa());
            EchipaDB echipaidEchipaOld = persistentMembruEchipaDB.getEchipaidEchipa();
            EchipaDB echipaidEchipaNew = membruEchipaDB.getEchipaidEchipa();
            if (echipaidEchipaNew != null) {
                echipaidEchipaNew = em.getReference(echipaidEchipaNew.getClass(), echipaidEchipaNew.getIdEchipa());
                membruEchipaDB.setEchipaidEchipa(echipaidEchipaNew);
            }
            membruEchipaDB = em.merge(membruEchipaDB);
            if (echipaidEchipaOld != null && !echipaidEchipaOld.equals(echipaidEchipaNew)) {
                echipaidEchipaOld.getMembruEchipaDBCollection().remove(membruEchipaDB);
                echipaidEchipaOld = em.merge(echipaidEchipaOld);
            }
            if (echipaidEchipaNew != null && !echipaidEchipaNew.equals(echipaidEchipaOld)) {
                echipaidEchipaNew.getMembruEchipaDBCollection().add(membruEchipaDB);
                echipaidEchipaNew = em.merge(echipaidEchipaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = membruEchipaDB.getIdMembruEchipa();
                if (findMembruEchipaDB(id) == null) {
                    throw new NonexistentEntityException("The membruEchipaDB with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MembruEchipaDB membruEchipaDB;
            try {
                membruEchipaDB = em.getReference(MembruEchipaDB.class, id);
                membruEchipaDB.getIdMembruEchipa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The membruEchipaDB with id " + id + " no longer exists.", enfe);
            }
            EchipaDB echipaidEchipa = membruEchipaDB.getEchipaidEchipa();
            if (echipaidEchipa != null) {
                echipaidEchipa.getMembruEchipaDBCollection().remove(membruEchipaDB);
                echipaidEchipa = em.merge(echipaidEchipa);
            }
            em.remove(membruEchipaDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MembruEchipaDB> findMembruEchipaDBEntities() {
        return findMembruEchipaDBEntities(true, -1, -1);
    }

    public List<MembruEchipaDB> findMembruEchipaDBEntities(int maxResults, int firstResult) {
        return findMembruEchipaDBEntities(false, maxResults, firstResult);
    }

    private List<MembruEchipaDB> findMembruEchipaDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MembruEchipaDB.class));
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

    public MembruEchipaDB findMembruEchipaDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MembruEchipaDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getMembruEchipaDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MembruEchipaDB> rt = cq.from(MembruEchipaDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public MembruEchipaDB getSingleResult (String nume, EchipaDB echipa){
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("MembruEchipaDB.findByNumeMembruAndIdEchipa");
        q.setParameter("numeMembruEchipa", nume);
        q.setParameter("echipaidEchipa", echipa);
                        
        
        try {
            return (MembruEchipaDB) q.getResultList().get(0);
        } catch (Exception e){
            return null;
        }
    }
    
    public List<MembruEchipaDB> findMembriByEchipa (EchipaDB echipa) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("MembruEchipaDB.findByIdEchipa");
        q.setParameter("echipaidEchipa", echipa);
        
        return q.getResultList();
    }
    
    public int insertMembru (MembruEchipaDB m) {
        int res = -1;
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("INSERT INTO membruechipa (idMembruEchipa, numeMembruEchipa, Echipa_idEchipa) VALUES (?, ?, ?)");
        q.setParameter(1, m.getIdMembruEchipa());
        q.setParameter(2, m.getNumeMembruEchipa());
        q.setParameter(3, m.getEchipaidEchipa().getIdEchipa());
        
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
