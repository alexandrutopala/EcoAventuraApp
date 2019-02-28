/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Magazin.dao;

import Magazin.dao.exceptions.IllegalOrphanException;
import Magazin.dao.exceptions.NonexistentEntityException;
import Magazin.db.ProdusDB;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Magazin.db.TranzactieDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Marius
 */
public class ProdusDAO implements Serializable {

    public ProdusDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProdusDB produsDB) {
        if (produsDB.getTranzactieDBCollection() == null) {
            produsDB.setTranzactieDBCollection(new ArrayList<TranzactieDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<TranzactieDB> attachedTranzactieDBCollection = new ArrayList<TranzactieDB>();
            for (TranzactieDB tranzactieDBCollectionTranzactieDBToAttach : produsDB.getTranzactieDBCollection()) {
                tranzactieDBCollectionTranzactieDBToAttach = em.getReference(tranzactieDBCollectionTranzactieDBToAttach.getClass(), tranzactieDBCollectionTranzactieDBToAttach.getIdTranzactie());
                attachedTranzactieDBCollection.add(tranzactieDBCollectionTranzactieDBToAttach);
            }
            produsDB.setTranzactieDBCollection(attachedTranzactieDBCollection);
            em.persist(produsDB);
            for (TranzactieDB tranzactieDBCollectionTranzactieDB : produsDB.getTranzactieDBCollection()) {
                ProdusDB oldIdProdusOfTranzactieDBCollectionTranzactieDB = tranzactieDBCollectionTranzactieDB.getIdProdus();
                tranzactieDBCollectionTranzactieDB.setIdProdus(produsDB);
                tranzactieDBCollectionTranzactieDB = em.merge(tranzactieDBCollectionTranzactieDB);
                if (oldIdProdusOfTranzactieDBCollectionTranzactieDB != null) {
                    oldIdProdusOfTranzactieDBCollectionTranzactieDB.getTranzactieDBCollection().remove(tranzactieDBCollectionTranzactieDB);
                    oldIdProdusOfTranzactieDBCollectionTranzactieDB = em.merge(oldIdProdusOfTranzactieDBCollectionTranzactieDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProdusDB produsDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProdusDB persistentProdusDB = em.find(ProdusDB.class, produsDB.getIdProdus());
            Collection<TranzactieDB> tranzactieDBCollectionOld = persistentProdusDB.getTranzactieDBCollection();
            Collection<TranzactieDB> tranzactieDBCollectionNew = produsDB.getTranzactieDBCollection();
            List<String> illegalOrphanMessages = null;
            for (TranzactieDB tranzactieDBCollectionOldTranzactieDB : tranzactieDBCollectionOld) {
                if (!tranzactieDBCollectionNew.contains(tranzactieDBCollectionOldTranzactieDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TranzactieDB " + tranzactieDBCollectionOldTranzactieDB + " since its idProdus field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<TranzactieDB> attachedTranzactieDBCollectionNew = new ArrayList<TranzactieDB>();
            for (TranzactieDB tranzactieDBCollectionNewTranzactieDBToAttach : tranzactieDBCollectionNew) {
                tranzactieDBCollectionNewTranzactieDBToAttach = em.getReference(tranzactieDBCollectionNewTranzactieDBToAttach.getClass(), tranzactieDBCollectionNewTranzactieDBToAttach.getIdTranzactie());
                attachedTranzactieDBCollectionNew.add(tranzactieDBCollectionNewTranzactieDBToAttach);
            }
            tranzactieDBCollectionNew = attachedTranzactieDBCollectionNew;
            produsDB.setTranzactieDBCollection(tranzactieDBCollectionNew);
            produsDB = em.merge(produsDB);
            for (TranzactieDB tranzactieDBCollectionNewTranzactieDB : tranzactieDBCollectionNew) {
                if (!tranzactieDBCollectionOld.contains(tranzactieDBCollectionNewTranzactieDB)) {
                    ProdusDB oldIdProdusOfTranzactieDBCollectionNewTranzactieDB = tranzactieDBCollectionNewTranzactieDB.getIdProdus();
                    tranzactieDBCollectionNewTranzactieDB.setIdProdus(produsDB);
                    tranzactieDBCollectionNewTranzactieDB = em.merge(tranzactieDBCollectionNewTranzactieDB);
                    if (oldIdProdusOfTranzactieDBCollectionNewTranzactieDB != null && !oldIdProdusOfTranzactieDBCollectionNewTranzactieDB.equals(produsDB)) {
                        oldIdProdusOfTranzactieDBCollectionNewTranzactieDB.getTranzactieDBCollection().remove(tranzactieDBCollectionNewTranzactieDB);
                        oldIdProdusOfTranzactieDBCollectionNewTranzactieDB = em.merge(oldIdProdusOfTranzactieDBCollectionNewTranzactieDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = produsDB.getIdProdus();
                if (findProdusDB(id) == null) {
                    throw new NonexistentEntityException("The produsDB with id " + id + " no longer exists.");
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
            ProdusDB produsDB;
            try {
                produsDB = em.getReference(ProdusDB.class, id);
                produsDB.getIdProdus();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produsDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TranzactieDB> tranzactieDBCollectionOrphanCheck = produsDB.getTranzactieDBCollection();
            for (TranzactieDB tranzactieDBCollectionOrphanCheckTranzactieDB : tranzactieDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ProdusDB (" + produsDB + ") cannot be destroyed since the TranzactieDB " + tranzactieDBCollectionOrphanCheckTranzactieDB + " in its tranzactieDBCollection field has a non-nullable idProdus field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(produsDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProdusDB> findProdusDBEntities() {
        return findProdusDBEntities(true, -1, -1);
    }

    public List<ProdusDB> findProdusDBEntities(int maxResults, int firstResult) {
        return findProdusDBEntities(false, maxResults, firstResult);
    }

    private List<ProdusDB> findProdusDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProdusDB.class));
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

    public ProdusDB findProdusDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProdusDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdusDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProdusDB> rt = cq.from(ProdusDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
