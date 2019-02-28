/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Magazin.dao;

import Magazin.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Magazin.db.ProdusDB;
import Magazin.db.TranzactieDB;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Marius
 */
public class TranzactieDAO implements Serializable {

    public TranzactieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TranzactieDB tranzactieDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProdusDB idProdus = tranzactieDB.getIdProdus();
            if (idProdus != null) {
                idProdus = em.getReference(idProdus.getClass(), idProdus.getIdProdus());
                tranzactieDB.setIdProdus(idProdus);
            }
            em.persist(tranzactieDB);
            if (idProdus != null) {
                idProdus.getTranzactieDBCollection().add(tranzactieDB);
                idProdus = em.merge(idProdus);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TranzactieDB tranzactieDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TranzactieDB persistentTranzactieDB = em.find(TranzactieDB.class, tranzactieDB.getIdTranzactie());
            ProdusDB idProdusOld = persistentTranzactieDB.getIdProdus();
            ProdusDB idProdusNew = tranzactieDB.getIdProdus();
            if (idProdusNew != null) {
                idProdusNew = em.getReference(idProdusNew.getClass(), idProdusNew.getIdProdus());
                tranzactieDB.setIdProdus(idProdusNew);
            }
            tranzactieDB = em.merge(tranzactieDB);
            if (idProdusOld != null && !idProdusOld.equals(idProdusNew)) {
                idProdusOld.getTranzactieDBCollection().remove(tranzactieDB);
                idProdusOld = em.merge(idProdusOld);
            }
            if (idProdusNew != null && !idProdusNew.equals(idProdusOld)) {
                idProdusNew.getTranzactieDBCollection().add(tranzactieDB);
                idProdusNew = em.merge(idProdusNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tranzactieDB.getIdTranzactie();
                if (findTranzactieDB(id) == null) {
                    throw new NonexistentEntityException("The tranzactieDB with id " + id + " no longer exists.");
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
            TranzactieDB tranzactieDB;
            try {
                tranzactieDB = em.getReference(TranzactieDB.class, id);
                tranzactieDB.getIdTranzactie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tranzactieDB with id " + id + " no longer exists.", enfe);
            }
            ProdusDB idProdus = tranzactieDB.getIdProdus();
            if (idProdus != null) {
                idProdus.getTranzactieDBCollection().remove(tranzactieDB);
                idProdus = em.merge(idProdus);
            }
            em.remove(tranzactieDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TranzactieDB> findTranzactieDBEntities() {
        return findTranzactieDBEntities(true, -1, -1);
    }

    public List<TranzactieDB> findTranzactieDBEntities(int maxResults, int firstResult) {
        return findTranzactieDBEntities(false, maxResults, firstResult);
    }

    private List<TranzactieDB> findTranzactieDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TranzactieDB.class));
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

    public TranzactieDB findTranzactieDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TranzactieDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getTranzactieDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TranzactieDB> rt = cq.from(TranzactieDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
