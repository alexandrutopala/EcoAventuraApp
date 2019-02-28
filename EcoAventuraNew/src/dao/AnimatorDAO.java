/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import db.AnimatorDB;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Alexandru
 */
public class AnimatorDAO implements Serializable {

    public AnimatorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AnimatorDB animatorDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(animatorDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AnimatorDB animatorDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            animatorDB = em.merge(animatorDB);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = animatorDB.getIdAnimator();
                if (findAnimatorDB(id) == null) {
                    throw new NonexistentEntityException("The animatorDB with id " + id + " no longer exists.");
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
            AnimatorDB animatorDB;
            try {
                animatorDB = em.getReference(AnimatorDB.class, id);
                animatorDB.getIdAnimator();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The animatorDB with id " + id + " no longer exists.", enfe);
            }
            em.remove(animatorDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AnimatorDB> findAnimatorDBEntities() {
        return findAnimatorDBEntities(true, -1, -1);
    }

    public List<AnimatorDB> findAnimatorDBEntities(int maxResults, int firstResult) {
        return findAnimatorDBEntities(false, maxResults, firstResult);
    }

    private List<AnimatorDB> findAnimatorDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AnimatorDB.class));
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

    public AnimatorDB findAnimatorDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AnimatorDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnimatorDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AnimatorDB> rt = cq.from(AnimatorDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public AnimatorDB getSingleResult (String nume){
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("AnimatorDB.findByNumeAnimator");
        q.setParameter("numeAnimator", nume);
        
        try{
            return (AnimatorDB) q.getSingleResult();
        } catch (Exception e){
            return null;
        }
    }
    
    public List<AnimatorDB> getAniamtoriByDisponibilitate (boolean av) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("AnimatorDB.findByDisponibilAnimator");
        q.setParameter("disponibilAnimator", av);
        
        return q.getResultList();
    }
}
