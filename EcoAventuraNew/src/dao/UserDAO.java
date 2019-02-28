/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import db.UserDB;
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
public class UserDAO implements Serializable {

    public UserDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserDB userDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(userDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserDB userDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            userDB = em.merge(userDB);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = userDB.getIdUser();
                if (findUserDB(id) == null) {
                    throw new NonexistentEntityException("The userDB with id " + id + " no longer exists.");
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
            UserDB userDB;
            try {
                userDB = em.getReference(UserDB.class, id);
                userDB.getIdUser();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userDB with id " + id + " no longer exists.", enfe);
            }
            em.remove(userDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserDB> findUserDBEntities() {
        return findUserDBEntities(true, -1, -1);
    }

    public List<UserDB> findUserDBEntities(int maxResults, int firstResult) {
        return findUserDBEntities(false, maxResults, firstResult);
    }

    private List<UserDB> findUserDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserDB.class));
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

    public UserDB findUserDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserDB> rt = cq.from(UserDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public UserDB getSingleResult (String username){
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("UserDB.findByUsername");
        q.setParameter("username", username);
        
        try {
            return (UserDB) q.getSingleResult();
        } catch (Exception e){
            return null;
        }
    }
    
    public int insertUser (UserDB user) {
        EntityManager em = getEntityManager();
        int res = -1;
        
        em.getTransaction().begin();
        
        Query q = em.createNativeQuery("INSERT INTO user (idUser, username, parola, acces) VALUES (?, ?, ?, ?)");
        q.setParameter(1, user.getIdUser());
        q.setParameter(2, user.getUsername());
        q.setParameter(3, user.getParola());
        q.setParameter(4, user.getAcces());
        
        try {
            res =  q.executeUpdate();
        } catch (Exception e) {
            res = -1;
        } finally {
            em.getTransaction().commit();
            em.close();
            return res;
        }
    }
}
