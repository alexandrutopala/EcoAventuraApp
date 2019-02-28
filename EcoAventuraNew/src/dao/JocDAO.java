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
import db.JocGeneralDB;
import db.EchipaDB;
import db.JocDB;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alexandru
 */
public class JocDAO implements Serializable {

    public JocDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(JocDB jocDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            JocGeneralDB jocGeneralidJocGeneral = jocDB.getJocGeneralidJocGeneral();
            if (jocGeneralidJocGeneral != null) {
                jocGeneralidJocGeneral = em.getReference(jocGeneralidJocGeneral.getClass(), jocGeneralidJocGeneral.getIdJocGeneral());
                jocDB.setJocGeneralidJocGeneral(jocGeneralidJocGeneral);
            }
            EchipaDB echipaidEchipa = jocDB.getEchipaidEchipa();
            if (echipaidEchipa != null) {
                echipaidEchipa = em.getReference(echipaidEchipa.getClass(), echipaidEchipa.getIdEchipa());
                jocDB.setEchipaidEchipa(echipaidEchipa);
            }
            em.persist(jocDB);
            if (jocGeneralidJocGeneral != null) {
                jocGeneralidJocGeneral.getJocDBCollection().add(jocDB);
                jocGeneralidJocGeneral = em.merge(jocGeneralidJocGeneral);
            }
            if (echipaidEchipa != null) {
                echipaidEchipa.getJocDBCollection().add(jocDB);
                echipaidEchipa = em.merge(echipaidEchipa);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(JocDB jocDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            JocDB persistentJocDB = em.find(JocDB.class, jocDB.getIdJoc());
            JocGeneralDB jocGeneralidJocGeneralOld = persistentJocDB.getJocGeneralidJocGeneral();
            JocGeneralDB jocGeneralidJocGeneralNew = jocDB.getJocGeneralidJocGeneral();
            EchipaDB echipaidEchipaOld = persistentJocDB.getEchipaidEchipa();
            EchipaDB echipaidEchipaNew = jocDB.getEchipaidEchipa();
            if (jocGeneralidJocGeneralNew != null) {
                jocGeneralidJocGeneralNew = em.getReference(jocGeneralidJocGeneralNew.getClass(), jocGeneralidJocGeneralNew.getIdJocGeneral());
                jocDB.setJocGeneralidJocGeneral(jocGeneralidJocGeneralNew);
            }
            if (echipaidEchipaNew != null) {
                echipaidEchipaNew = em.getReference(echipaidEchipaNew.getClass(), echipaidEchipaNew.getIdEchipa());
                jocDB.setEchipaidEchipa(echipaidEchipaNew);
            }
            jocDB = em.merge(jocDB);
            if (jocGeneralidJocGeneralOld != null && !jocGeneralidJocGeneralOld.equals(jocGeneralidJocGeneralNew)) {
                jocGeneralidJocGeneralOld.getJocDBCollection().remove(jocDB);
                jocGeneralidJocGeneralOld = em.merge(jocGeneralidJocGeneralOld);
            }
            if (jocGeneralidJocGeneralNew != null && !jocGeneralidJocGeneralNew.equals(jocGeneralidJocGeneralOld)) {
                jocGeneralidJocGeneralNew.getJocDBCollection().add(jocDB);
                jocGeneralidJocGeneralNew = em.merge(jocGeneralidJocGeneralNew);
            }
            if (echipaidEchipaOld != null && !echipaidEchipaOld.equals(echipaidEchipaNew)) {
                echipaidEchipaOld.getJocDBCollection().remove(jocDB);
                echipaidEchipaOld = em.merge(echipaidEchipaOld);
            }
            if (echipaidEchipaNew != null && !echipaidEchipaNew.equals(echipaidEchipaOld)) {
                echipaidEchipaNew.getJocDBCollection().add(jocDB);
                echipaidEchipaNew = em.merge(echipaidEchipaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = jocDB.getIdJoc();
                if (findJocDB(id) == null) {
                    throw new NonexistentEntityException("The jocDB with id " + id + " no longer exists.");
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
            JocDB jocDB;
            try {
                jocDB = em.getReference(JocDB.class, id);
                jocDB.getIdJoc();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jocDB with id " + id + " no longer exists.", enfe);
            }
            JocGeneralDB jocGeneralidJocGeneral = jocDB.getJocGeneralidJocGeneral();
            if (jocGeneralidJocGeneral != null) {
                jocGeneralidJocGeneral.getJocDBCollection().remove(jocDB);
                jocGeneralidJocGeneral = em.merge(jocGeneralidJocGeneral);
            }
            EchipaDB echipaidEchipa = jocDB.getEchipaidEchipa();
            if (echipaidEchipa != null) {
                echipaidEchipa.getJocDBCollection().remove(jocDB);
                echipaidEchipa = em.merge(echipaidEchipa);
            }
            em.remove(jocDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<JocDB> findJocDBEntities() {
        return findJocDBEntities(true, -1, -1);
    }

    public List<JocDB> findJocDBEntities(int maxResults, int firstResult) {
        return findJocDBEntities(false, maxResults, firstResult);
    }

    private List<JocDB> findJocDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(JocDB.class));
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

    public JocDB findJocDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(JocDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getJocDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<JocDB> rt = cq.from(JocDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public List<JocDB> findJocuriByEchiap (EchipaDB echipa) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("JocDB.findByEchipa");
        q.setParameter("echipa", echipa);
        
        return q.getResultList();
    }
    
    public List<JocDB> findJocuriByJocGeneral (JocGeneralDB joc) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("JocDB.findByJocGeneral");
        q.setParameter("jocGeneral", joc);
        
        return (List<JocDB>) q.getResultList();
    }
    
    public List<JocDB> findJocuriByJocGeneralAndIdProgramAndPerioada (JocGeneralDB j, String perioada, long idProgram) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("JocDB.findByIdProgramAndJocGeneralAndPerioada");
        q.setParameter("jocGeneral", j);
        q.setParameter("idProgram", idProgram);
        q.setParameter("perioada", "%"+perioada+"%");
        
        return (List<JocDB>) q.getResultList();
    }
    
    public List<JocDB> findJocuriByJocGeneralAndPerioadaAndData (JocGeneralDB jgdb, String perioada, String data) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("JocDB.findByJocGeneralAndDataAndPerioada");
        q.setParameter("jocGeneral", jgdb);
        q.setParameter("data", data);
        q.setParameter("perioada", "%"+perioada+"%");
        
        return q.getResultList();
    }
    
    public JocDB isStored (JocDB joc, String perioada) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("JocDB.findByJocGeneralAndEchipaAndIdProgramAndPerioada");
        q.setParameter("idProgram", joc.getIdProgram());
        q.setParameter("echipa", joc.getEchipaidEchipa());
        q.setParameter("jocGeneral", joc.getJocGeneralidJocGeneral());
        q.setParameter("perioada", "%"+perioada+"%");
        
        try {
            return (JocDB) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public int insertJoc (JocDB j) {
        int res = -1;
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("INSERT INTO joc (idJoc, organizator, punctaj, data, locatie, post, Echipa_idEchipa, JocGeneral_idJocGeneral, absent, idProgram) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        q.setParameter(1, j.getIdJoc());
        q.setParameter(2, j.getOrganizator());
        q.setParameter(3, j.getPunctaj());
        q.setParameter(4, j.getData());
        q.setParameter(5, j.getLocatie());
        q.setParameter(6, j.getPost());
        q.setParameter(7, j.getEchipaidEchipa().getIdEchipa());
        q.setParameter(8, j.getJocGeneralidJocGeneral().getIdJocGeneral());
        q.setParameter(9, j.getAbsent());
        q.setParameter(10, j.getIdProgram());
        
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
    
    @Deprecated
    public JocDB isStored (JocGeneralDB jgdb, EchipaDB e, long idProgram) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("JocDB.findByJocGeneralAndEchipaAndIdProgram");
        q.setParameter("jocGeneral", jgdb);
        q.setParameter("echipa", e);
        q.setParameter("idProgram", idProgram);
        
        try {
            return (JocDB) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
    
    public List<JocDB> findByEchipaAndJocGeneral (EchipaDB e, JocGeneralDB jg) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("JocDB.findByEchipaAndJocGeneral");
        q.setParameter("echipa", e);
        q.setParameter("jocGeneral", jg);
        
        return (List<JocDB>) q.getResultList();
    }
    
    public boolean wipeActivitati (String date) {
        try {
            EntityManager em = getEntityManager();
            Query q = em.createNamedQuery("JocDB.findByData");
            q.setParameter("data", date);

            List<JocDB> list = q.getResultList();

            for (JocDB a : list) {
                destroy(a.getIdJoc());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public long getMaxId() {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("JocDB.findMaxId");
        
        try {
            return (Long) q.getSingleResult();
        } catch (Exception e){
            return -1;
        }
    }
}
