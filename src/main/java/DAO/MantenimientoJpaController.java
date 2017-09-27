/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.NonexistentEntityException;
import DAO.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Datos.Herramienta;
import Datos.Mantenimiento;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Carlos
 */
public class MantenimientoJpaController implements Serializable {

    public MantenimientoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Mantenimiento mantenimiento) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Herramienta idherramienta = mantenimiento.getIdherramienta();
            if (idherramienta != null) {
                idherramienta = em.getReference(idherramienta.getClass(), idherramienta.getIdherramienta());
                mantenimiento.setIdherramienta(idherramienta);
            }
            em.persist(mantenimiento);
            if (idherramienta != null) {
                idherramienta.getMantenimientoCollection().add(mantenimiento);
                idherramienta = em.merge(idherramienta);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMantenimiento(mantenimiento.getIdmantenimiento()) != null) {
                throw new PreexistingEntityException("Mantenimiento " + mantenimiento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Mantenimiento mantenimiento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mantenimiento persistentMantenimiento = em.find(Mantenimiento.class, mantenimiento.getIdmantenimiento());
            Herramienta idherramientaOld = persistentMantenimiento.getIdherramienta();
            Herramienta idherramientaNew = mantenimiento.getIdherramienta();
            if (idherramientaNew != null) {
                idherramientaNew = em.getReference(idherramientaNew.getClass(), idherramientaNew.getIdherramienta());
                mantenimiento.setIdherramienta(idherramientaNew);
            }
            mantenimiento = em.merge(mantenimiento);
            if (idherramientaOld != null && !idherramientaOld.equals(idherramientaNew)) {
                idherramientaOld.getMantenimientoCollection().remove(mantenimiento);
                idherramientaOld = em.merge(idherramientaOld);
            }
            if (idherramientaNew != null && !idherramientaNew.equals(idherramientaOld)) {
                idherramientaNew.getMantenimientoCollection().add(mantenimiento);
                idherramientaNew = em.merge(idherramientaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = mantenimiento.getIdmantenimiento();
                if (findMantenimiento(id) == null) {
                    throw new NonexistentEntityException("The mantenimiento with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mantenimiento mantenimiento;
            try {
                mantenimiento = em.getReference(Mantenimiento.class, id);
                mantenimiento.getIdmantenimiento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mantenimiento with id " + id + " no longer exists.", enfe);
            }
            Herramienta idherramienta = mantenimiento.getIdherramienta();
            if (idherramienta != null) {
                idherramienta.getMantenimientoCollection().remove(mantenimiento);
                idherramienta = em.merge(idherramienta);
            }
            em.remove(mantenimiento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Mantenimiento> findMantenimientoEntities() {
        return findMantenimientoEntities(true, -1, -1);
    }

    public List<Mantenimiento> findMantenimientoEntities(int maxResults, int firstResult) {
        return findMantenimientoEntities(false, maxResults, firstResult);
    }

    private List<Mantenimiento> findMantenimientoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Mantenimiento.class));
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

    public Mantenimiento findMantenimiento(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mantenimiento.class, id);
        } finally {
            em.close();
        }
    }

    public int getMantenimientoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Mantenimiento> rt = cq.from(Mantenimiento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
