/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import db.ActivitateDB;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import db.EchipaDB;
import db.ActivitateGeneralaDB;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alexandru
 */
public class ActivitateDAO implements Serializable {

    public ActivitateDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ActivitateDB activitateDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EchipaDB echipaidEchipa = activitateDB.getEchipaidEchipa();
            if (echipaidEchipa != null) {
                echipaidEchipa = em.getReference(echipaidEchipa.getClass(), echipaidEchipa.getIdEchipa());
                activitateDB.setEchipaidEchipa(echipaidEchipa);
            }
            ActivitateGeneralaDB activitateGeneralaidActivitateGenerala = activitateDB.getActivitateGeneralaidActivitateGenerala();
            if (activitateGeneralaidActivitateGenerala != null) {
                activitateGeneralaidActivitateGenerala = em.getReference(activitateGeneralaidActivitateGenerala.getClass(), activitateGeneralaidActivitateGenerala.getIdActivitateGenerala());
                activitateDB.setActivitateGeneralaidActivitateGenerala(activitateGeneralaidActivitateGenerala);
            }
            em.persist(activitateDB);
            if (echipaidEchipa != null) {
                echipaidEchipa.getActivitateDBCollection().add(activitateDB);
                echipaidEchipa = em.merge(echipaidEchipa);
            }
            if (activitateGeneralaidActivitateGenerala != null) {
                activitateGeneralaidActivitateGenerala.getActivitateDBCollection().add(activitateDB);
                activitateGeneralaidActivitateGenerala = em.merge(activitateGeneralaidActivitateGenerala);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ActivitateDB activitateDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ActivitateDB persistentActivitateDB = em.find(ActivitateDB.class, activitateDB.getIdActivitate());
            EchipaDB echipaidEchipaOld = persistentActivitateDB.getEchipaidEchipa();
            EchipaDB echipaidEchipaNew = activitateDB.getEchipaidEchipa();
            ActivitateGeneralaDB activitateGeneralaidActivitateGeneralaOld = persistentActivitateDB.getActivitateGeneralaidActivitateGenerala();
            ActivitateGeneralaDB activitateGeneralaidActivitateGeneralaNew = activitateDB.getActivitateGeneralaidActivitateGenerala();
            if (echipaidEchipaNew != null) {
                echipaidEchipaNew = em.getReference(echipaidEchipaNew.getClass(), echipaidEchipaNew.getIdEchipa());
                activitateDB.setEchipaidEchipa(echipaidEchipaNew);
            }
            if (activitateGeneralaidActivitateGeneralaNew != null) {
                activitateGeneralaidActivitateGeneralaNew = em.getReference(activitateGeneralaidActivitateGeneralaNew.getClass(), activitateGeneralaidActivitateGeneralaNew.getIdActivitateGenerala());
                activitateDB.setActivitateGeneralaidActivitateGenerala(activitateGeneralaidActivitateGeneralaNew);
            }
            activitateDB = em.merge(activitateDB);
            if (echipaidEchipaOld != null && !echipaidEchipaOld.equals(echipaidEchipaNew)) {
                echipaidEchipaOld.getActivitateDBCollection().remove(activitateDB);
                echipaidEchipaOld = em.merge(echipaidEchipaOld);
            }
            if (echipaidEchipaNew != null && !echipaidEchipaNew.equals(echipaidEchipaOld)) {
                echipaidEchipaNew.getActivitateDBCollection().add(activitateDB);
                echipaidEchipaNew = em.merge(echipaidEchipaNew);
            }
            if (activitateGeneralaidActivitateGeneralaOld != null && !activitateGeneralaidActivitateGeneralaOld.equals(activitateGeneralaidActivitateGeneralaNew)) {
                activitateGeneralaidActivitateGeneralaOld.getActivitateDBCollection().remove(activitateDB);
                activitateGeneralaidActivitateGeneralaOld = em.merge(activitateGeneralaidActivitateGeneralaOld);
            }
            if (activitateGeneralaidActivitateGeneralaNew != null && !activitateGeneralaidActivitateGeneralaNew.equals(activitateGeneralaidActivitateGeneralaOld)) {
                activitateGeneralaidActivitateGeneralaNew.getActivitateDBCollection().add(activitateDB);
                activitateGeneralaidActivitateGeneralaNew = em.merge(activitateGeneralaidActivitateGeneralaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = activitateDB.getIdActivitate();
                if (findActivitateDB(id) == null) {
                    throw new NonexistentEntityException("The activitateDB with id " + id + " no longer exists.");
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
            ActivitateDB activitateDB;
            try {
                activitateDB = em.getReference(ActivitateDB.class, id);
                activitateDB.getIdActivitate();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The activitateDB with id " + id + " no longer exists.", enfe);
            }
            EchipaDB echipaidEchipa = activitateDB.getEchipaidEchipa();
            if (echipaidEchipa != null) {
                echipaidEchipa.getActivitateDBCollection().remove(activitateDB);
                echipaidEchipa = em.merge(echipaidEchipa);
            }
            ActivitateGeneralaDB activitateGeneralaidActivitateGenerala = activitateDB.getActivitateGeneralaidActivitateGenerala();
            if (activitateGeneralaidActivitateGenerala != null) {
                activitateGeneralaidActivitateGenerala.getActivitateDBCollection().remove(activitateDB);
                activitateGeneralaidActivitateGenerala = em.merge(activitateGeneralaidActivitateGenerala);
            }
            em.remove(activitateDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ActivitateDB> findActivitateDBEntities() {
        return findActivitateDBEntities(true, -1, -1);
    }

    public List<ActivitateDB> findActivitateDBEntities(int maxResults, int firstResult) {
        return findActivitateDBEntities(false, maxResults, firstResult);
    }

    private List<ActivitateDB> findActivitateDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ActivitateDB.class));
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

    public ActivitateDB findActivitateDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ActivitateDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getActivitateDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ActivitateDB> rt = cq.from(ActivitateDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<ActivitateDB> findActivitiesByEchipa (EchipaDB echipa) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("ActivitateDB.findByEchipa");
        q.setParameter("echipa", echipa);
        
        return q.getResultList();
    }
    
    public List<ActivitateDB> findActivityByActivitateGenerala (ActivitateGeneralaDB a) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("ActivitateDB.findByActivitateGenerala");
        q.setParameter("activitateGenerala", a);
        
        return (List<ActivitateDB>) q.getResultList();
    }
    
    public List<ActivitateDB> findActivitateByActivitateGeneralaAndIdProgramAndPerioada (ActivitateGeneralaDB a, long idProgram, String perioada) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("ActivitateDB.findByIdProgramAndActivitateGeneralaAndPerioada");
        q.setParameter("activitateGenerala", a);
        q.setParameter("idProgram", idProgram);
        q.setParameter("perioada", "%"+perioada+"%");
        
        return (List<ActivitateDB>) q.getResultList();
    }
    
    public List<ActivitateDB> findActivitateByActivitateGeneralaAndDataAndPerioada (ActivitateGeneralaDB agdb, String data, String perioada) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("ActivitateDB.findByActivitateGeneralaAndDataAndPerioada");
        q.setParameter("activitateGenerala", agdb);
        q.setParameter("data", data);
        q.setParameter("perioada", "%"+perioada+"%");
        
        return q.getResultList();
    }
    
    public ActivitateDB isStored (ActivitateDB a, String perioada) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("ActivitateDB.findByActivitateGeneralaAndEchipaAndIdProgramAndPerioada");
        
        q.setParameter("activitateGenerala", a.getActivitateGeneralaidActivitateGenerala());
        q.setParameter("echipa", a.getEchipaidEchipa());
        q.setParameter("idProgram", a.getIdProgram());
        q.setParameter("perioada", "%"+perioada+"%");
        
        try {
            return (ActivitateDB) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Deprecated
    public ActivitateDB isStored (ActivitateGeneralaDB agdb, EchipaDB e, long idProgram) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("ActivitateDB.findByActivitateGeneralaAndEchipaAndIdProgram");
        q.setParameter("activitateGenerala", agdb);
        q.setParameter("echipa", e);
        q.setParameter("idProgram", idProgram);
        
        try {
            return (ActivitateDB) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
    
    public int insertActivitate (ActivitateDB a) {
        EntityManager em = getEntityManager(); 
        Query q = em.createNativeQuery("INSERT INTO activitate (idActivitate, organizator, data, loactie, post, Echipa_idEchipa, ActivitateGenerala_idActivitateGenerala, idProgram) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        q.setParameter(1, a.getIdActivitate());
        q.setParameter(2, a.getOrganizator());
        q.setParameter(3, a.getData());
        q.setParameter(4, a.getLoactie());
        q.setParameter(5, a.getPost());
        q.setParameter(6, a.getEchipaidEchipa().getIdEchipa());
        q.setParameter(7, a.getActivitateGeneralaidActivitateGenerala().getIdActivitateGenerala());
        q.setParameter(8, a.getIdProgram());
        
        em.getTransaction().begin();
        
        int res = -1;
        
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
    
    public boolean wipeActivitati (String date) {
        try {
            EntityManager em = getEntityManager();
            Query q = em.createNamedQuery("ActivitateDB.findByData");
            q.setParameter("data", date);

            List<ActivitateDB> list = q.getResultList();

            for (ActivitateDB a : list) {
                destroy(a.getIdActivitate());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public long getMaxId () {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("ActivitateDB.findMaxId");
        
        try {
            return (Long) q.getSingleResult();
        } catch (Exception e) {
            return -1;
        }
    }
}
