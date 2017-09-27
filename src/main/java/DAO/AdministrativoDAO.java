/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import DAO.exceptions.PreexistingEntityException;
import Datos.Administrativo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Datos.Prestamo;
import java.util.ArrayList;
import java.util.Collection;
import Datos.Herramienta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Carlos
 */
public class AdministrativoDAO implements Serializable {

    public AdministrativoDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Administrativo administrativo) throws PreexistingEntityException, Exception {
        if (administrativo.getPrestamoCollection() == null) {
            administrativo.setPrestamoCollection(new ArrayList<Prestamo>());
        }
        if (administrativo.getHerramientaCollection() == null) {
            administrativo.setHerramientaCollection(new ArrayList<Herramienta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Prestamo> attachedPrestamoCollection = new ArrayList<Prestamo>();
            for (Prestamo prestamoCollectionPrestamoToAttach : administrativo.getPrestamoCollection()) {
                prestamoCollectionPrestamoToAttach = em.getReference(prestamoCollectionPrestamoToAttach.getClass(), prestamoCollectionPrestamoToAttach.getCodprestamo());
                attachedPrestamoCollection.add(prestamoCollectionPrestamoToAttach);
            }
            administrativo.setPrestamoCollection(attachedPrestamoCollection);
            Collection<Herramienta> attachedHerramientaCollection = new ArrayList<Herramienta>();
            for (Herramienta herramientaCollectionHerramientaToAttach : administrativo.getHerramientaCollection()) {
                herramientaCollectionHerramientaToAttach = em.getReference(herramientaCollectionHerramientaToAttach.getClass(), herramientaCollectionHerramientaToAttach.getIdherramienta());
                attachedHerramientaCollection.add(herramientaCollectionHerramientaToAttach);
            }
            administrativo.setHerramientaCollection(attachedHerramientaCollection);
            em.persist(administrativo);
            for (Prestamo prestamoCollectionPrestamo : administrativo.getPrestamoCollection()) {
                Administrativo oldIdadministrativoOfPrestamoCollectionPrestamo = prestamoCollectionPrestamo.getIdadministrativo();
                prestamoCollectionPrestamo.setIdadministrativo(administrativo);
                prestamoCollectionPrestamo = em.merge(prestamoCollectionPrestamo);
                if (oldIdadministrativoOfPrestamoCollectionPrestamo != null) {
                    oldIdadministrativoOfPrestamoCollectionPrestamo.getPrestamoCollection().remove(prestamoCollectionPrestamo);
                    oldIdadministrativoOfPrestamoCollectionPrestamo = em.merge(oldIdadministrativoOfPrestamoCollectionPrestamo);
                }
            }
            for (Herramienta herramientaCollectionHerramienta : administrativo.getHerramientaCollection()) {
                Administrativo oldIdadministrativoOfHerramientaCollectionHerramienta = herramientaCollectionHerramienta.getIdadministrativo();
                herramientaCollectionHerramienta.setIdadministrativo(administrativo);
                herramientaCollectionHerramienta = em.merge(herramientaCollectionHerramienta);
                if (oldIdadministrativoOfHerramientaCollectionHerramienta != null) {
                    oldIdadministrativoOfHerramientaCollectionHerramienta.getHerramientaCollection().remove(herramientaCollectionHerramienta);
                    oldIdadministrativoOfHerramientaCollectionHerramienta = em.merge(oldIdadministrativoOfHerramientaCollectionHerramienta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAdministrativo(administrativo.getIdadministrativo()) != null) {
                throw new PreexistingEntityException("Administrativo " + administrativo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Administrativo administrativo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Administrativo persistentAdministrativo = em.find(Administrativo.class, administrativo.getIdadministrativo());
            Collection<Prestamo> prestamoCollectionOld = persistentAdministrativo.getPrestamoCollection();
            Collection<Prestamo> prestamoCollectionNew = administrativo.getPrestamoCollection();
            Collection<Herramienta> herramientaCollectionOld = persistentAdministrativo.getHerramientaCollection();
            Collection<Herramienta> herramientaCollectionNew = administrativo.getHerramientaCollection();
            List<String> illegalOrphanMessages = null;
            for (Prestamo prestamoCollectionOldPrestamo : prestamoCollectionOld) {
                if (!prestamoCollectionNew.contains(prestamoCollectionOldPrestamo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Prestamo " + prestamoCollectionOldPrestamo + " since its idadministrativo field is not nullable.");
                }
            }
            for (Herramienta herramientaCollectionOldHerramienta : herramientaCollectionOld) {
                if (!herramientaCollectionNew.contains(herramientaCollectionOldHerramienta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Herramienta " + herramientaCollectionOldHerramienta + " since its idadministrativo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Prestamo> attachedPrestamoCollectionNew = new ArrayList<Prestamo>();
            for (Prestamo prestamoCollectionNewPrestamoToAttach : prestamoCollectionNew) {
                prestamoCollectionNewPrestamoToAttach = em.getReference(prestamoCollectionNewPrestamoToAttach.getClass(), prestamoCollectionNewPrestamoToAttach.getCodprestamo());
                attachedPrestamoCollectionNew.add(prestamoCollectionNewPrestamoToAttach);
            }
            prestamoCollectionNew = attachedPrestamoCollectionNew;
            administrativo.setPrestamoCollection(prestamoCollectionNew);
            Collection<Herramienta> attachedHerramientaCollectionNew = new ArrayList<Herramienta>();
            for (Herramienta herramientaCollectionNewHerramientaToAttach : herramientaCollectionNew) {
                herramientaCollectionNewHerramientaToAttach = em.getReference(herramientaCollectionNewHerramientaToAttach.getClass(), herramientaCollectionNewHerramientaToAttach.getIdherramienta());
                attachedHerramientaCollectionNew.add(herramientaCollectionNewHerramientaToAttach);
            }
            herramientaCollectionNew = attachedHerramientaCollectionNew;
            administrativo.setHerramientaCollection(herramientaCollectionNew);
            administrativo = em.merge(administrativo);
            for (Prestamo prestamoCollectionNewPrestamo : prestamoCollectionNew) {
                if (!prestamoCollectionOld.contains(prestamoCollectionNewPrestamo)) {
                    Administrativo oldIdadministrativoOfPrestamoCollectionNewPrestamo = prestamoCollectionNewPrestamo.getIdadministrativo();
                    prestamoCollectionNewPrestamo.setIdadministrativo(administrativo);
                    prestamoCollectionNewPrestamo = em.merge(prestamoCollectionNewPrestamo);
                    if (oldIdadministrativoOfPrestamoCollectionNewPrestamo != null && !oldIdadministrativoOfPrestamoCollectionNewPrestamo.equals(administrativo)) {
                        oldIdadministrativoOfPrestamoCollectionNewPrestamo.getPrestamoCollection().remove(prestamoCollectionNewPrestamo);
                        oldIdadministrativoOfPrestamoCollectionNewPrestamo = em.merge(oldIdadministrativoOfPrestamoCollectionNewPrestamo);
                    }
                }
            }
            for (Herramienta herramientaCollectionNewHerramienta : herramientaCollectionNew) {
                if (!herramientaCollectionOld.contains(herramientaCollectionNewHerramienta)) {
                    Administrativo oldIdadministrativoOfHerramientaCollectionNewHerramienta = herramientaCollectionNewHerramienta.getIdadministrativo();
                    herramientaCollectionNewHerramienta.setIdadministrativo(administrativo);
                    herramientaCollectionNewHerramienta = em.merge(herramientaCollectionNewHerramienta);
                    if (oldIdadministrativoOfHerramientaCollectionNewHerramienta != null && !oldIdadministrativoOfHerramientaCollectionNewHerramienta.equals(administrativo)) {
                        oldIdadministrativoOfHerramientaCollectionNewHerramienta.getHerramientaCollection().remove(herramientaCollectionNewHerramienta);
                        oldIdadministrativoOfHerramientaCollectionNewHerramienta = em.merge(oldIdadministrativoOfHerramientaCollectionNewHerramienta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = administrativo.getIdadministrativo();
                if (findAdministrativo(id) == null) {
                    throw new NonexistentEntityException("The administrativo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Administrativo administrativo;
            try {
                administrativo = em.getReference(Administrativo.class, id);
                administrativo.getIdadministrativo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The administrativo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Prestamo> prestamoCollectionOrphanCheck = administrativo.getPrestamoCollection();
            for (Prestamo prestamoCollectionOrphanCheckPrestamo : prestamoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Administrativo (" + administrativo + ") cannot be destroyed since the Prestamo " + prestamoCollectionOrphanCheckPrestamo + " in its prestamoCollection field has a non-nullable idadministrativo field.");
            }
            Collection<Herramienta> herramientaCollectionOrphanCheck = administrativo.getHerramientaCollection();
            for (Herramienta herramientaCollectionOrphanCheckHerramienta : herramientaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Administrativo (" + administrativo + ") cannot be destroyed since the Herramienta " + herramientaCollectionOrphanCheckHerramienta + " in its herramientaCollection field has a non-nullable idadministrativo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(administrativo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Administrativo> findAdministrativoEntities() {
        return findAdministrativoEntities(true, -1, -1);
    }

    public List<Administrativo> findAdministrativoEntities(int maxResults, int firstResult) {
        return findAdministrativoEntities(false, maxResults, firstResult);
    }

    private List<Administrativo> findAdministrativoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Administrativo.class));
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

    public Administrativo findAdministrativo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Administrativo.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdministrativoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Administrativo> rt = cq.from(Administrativo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
