/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import DAO.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Datos.Administrativo;
import Datos.Prestamo;
import Datos.Usuario;
import Datos.Transaccion;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Carlos
 */
public class PrestamoJpaController implements Serializable {

    public PrestamoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Prestamo prestamo) throws PreexistingEntityException, Exception {
        if (prestamo.getTransaccionCollection() == null) {
            prestamo.setTransaccionCollection(new ArrayList<Transaccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Administrativo idadministrativo = prestamo.getIdadministrativo();
            if (idadministrativo != null) {
                idadministrativo = em.getReference(idadministrativo.getClass(), idadministrativo.getIdadministrativo());
                prestamo.setIdadministrativo(idadministrativo);
            }
            Usuario idusuario = prestamo.getIdusuario();
            if (idusuario != null) {
                idusuario = em.getReference(idusuario.getClass(), idusuario.getIdusuario());
                prestamo.setIdusuario(idusuario);
            }
            Collection<Transaccion> attachedTransaccionCollection = new ArrayList<Transaccion>();
            for (Transaccion transaccionCollectionTransaccionToAttach : prestamo.getTransaccionCollection()) {
                transaccionCollectionTransaccionToAttach = em.getReference(transaccionCollectionTransaccionToAttach.getClass(), transaccionCollectionTransaccionToAttach.getIdtransaccion());
                attachedTransaccionCollection.add(transaccionCollectionTransaccionToAttach);
            }
            prestamo.setTransaccionCollection(attachedTransaccionCollection);
            em.persist(prestamo);
            if (idadministrativo != null) {
                idadministrativo.getPrestamoCollection().add(prestamo);
                idadministrativo = em.merge(idadministrativo);
            }
            if (idusuario != null) {
                idusuario.getPrestamoCollection().add(prestamo);
                idusuario = em.merge(idusuario);
            }
            for (Transaccion transaccionCollectionTransaccion : prestamo.getTransaccionCollection()) {
                Prestamo oldCodprestamoOfTransaccionCollectionTransaccion = transaccionCollectionTransaccion.getCodprestamo();
                transaccionCollectionTransaccion.setCodprestamo(prestamo);
                transaccionCollectionTransaccion = em.merge(transaccionCollectionTransaccion);
                if (oldCodprestamoOfTransaccionCollectionTransaccion != null) {
                    oldCodprestamoOfTransaccionCollectionTransaccion.getTransaccionCollection().remove(transaccionCollectionTransaccion);
                    oldCodprestamoOfTransaccionCollectionTransaccion = em.merge(oldCodprestamoOfTransaccionCollectionTransaccion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPrestamo(prestamo.getCodprestamo()) != null) {
                throw new PreexistingEntityException("Prestamo " + prestamo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Prestamo prestamo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Prestamo persistentPrestamo = em.find(Prestamo.class, prestamo.getCodprestamo());
            Administrativo idadministrativoOld = persistentPrestamo.getIdadministrativo();
            Administrativo idadministrativoNew = prestamo.getIdadministrativo();
            Usuario idusuarioOld = persistentPrestamo.getIdusuario();
            Usuario idusuarioNew = prestamo.getIdusuario();
            Collection<Transaccion> transaccionCollectionOld = persistentPrestamo.getTransaccionCollection();
            Collection<Transaccion> transaccionCollectionNew = prestamo.getTransaccionCollection();
            List<String> illegalOrphanMessages = null;
            for (Transaccion transaccionCollectionOldTransaccion : transaccionCollectionOld) {
                if (!transaccionCollectionNew.contains(transaccionCollectionOldTransaccion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Transaccion " + transaccionCollectionOldTransaccion + " since its codprestamo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idadministrativoNew != null) {
                idadministrativoNew = em.getReference(idadministrativoNew.getClass(), idadministrativoNew.getIdadministrativo());
                prestamo.setIdadministrativo(idadministrativoNew);
            }
            if (idusuarioNew != null) {
                idusuarioNew = em.getReference(idusuarioNew.getClass(), idusuarioNew.getIdusuario());
                prestamo.setIdusuario(idusuarioNew);
            }
            Collection<Transaccion> attachedTransaccionCollectionNew = new ArrayList<Transaccion>();
            for (Transaccion transaccionCollectionNewTransaccionToAttach : transaccionCollectionNew) {
                transaccionCollectionNewTransaccionToAttach = em.getReference(transaccionCollectionNewTransaccionToAttach.getClass(), transaccionCollectionNewTransaccionToAttach.getIdtransaccion());
                attachedTransaccionCollectionNew.add(transaccionCollectionNewTransaccionToAttach);
            }
            transaccionCollectionNew = attachedTransaccionCollectionNew;
            prestamo.setTransaccionCollection(transaccionCollectionNew);
            prestamo = em.merge(prestamo);
            if (idadministrativoOld != null && !idadministrativoOld.equals(idadministrativoNew)) {
                idadministrativoOld.getPrestamoCollection().remove(prestamo);
                idadministrativoOld = em.merge(idadministrativoOld);
            }
            if (idadministrativoNew != null && !idadministrativoNew.equals(idadministrativoOld)) {
                idadministrativoNew.getPrestamoCollection().add(prestamo);
                idadministrativoNew = em.merge(idadministrativoNew);
            }
            if (idusuarioOld != null && !idusuarioOld.equals(idusuarioNew)) {
                idusuarioOld.getPrestamoCollection().remove(prestamo);
                idusuarioOld = em.merge(idusuarioOld);
            }
            if (idusuarioNew != null && !idusuarioNew.equals(idusuarioOld)) {
                idusuarioNew.getPrestamoCollection().add(prestamo);
                idusuarioNew = em.merge(idusuarioNew);
            }
            for (Transaccion transaccionCollectionNewTransaccion : transaccionCollectionNew) {
                if (!transaccionCollectionOld.contains(transaccionCollectionNewTransaccion)) {
                    Prestamo oldCodprestamoOfTransaccionCollectionNewTransaccion = transaccionCollectionNewTransaccion.getCodprestamo();
                    transaccionCollectionNewTransaccion.setCodprestamo(prestamo);
                    transaccionCollectionNewTransaccion = em.merge(transaccionCollectionNewTransaccion);
                    if (oldCodprestamoOfTransaccionCollectionNewTransaccion != null && !oldCodprestamoOfTransaccionCollectionNewTransaccion.equals(prestamo)) {
                        oldCodprestamoOfTransaccionCollectionNewTransaccion.getTransaccionCollection().remove(transaccionCollectionNewTransaccion);
                        oldCodprestamoOfTransaccionCollectionNewTransaccion = em.merge(oldCodprestamoOfTransaccionCollectionNewTransaccion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = prestamo.getCodprestamo();
                if (findPrestamo(id) == null) {
                    throw new NonexistentEntityException("The prestamo with id " + id + " no longer exists.");
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
            Prestamo prestamo;
            try {
                prestamo = em.getReference(Prestamo.class, id);
                prestamo.getCodprestamo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prestamo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Transaccion> transaccionCollectionOrphanCheck = prestamo.getTransaccionCollection();
            for (Transaccion transaccionCollectionOrphanCheckTransaccion : transaccionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Prestamo (" + prestamo + ") cannot be destroyed since the Transaccion " + transaccionCollectionOrphanCheckTransaccion + " in its transaccionCollection field has a non-nullable codprestamo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Administrativo idadministrativo = prestamo.getIdadministrativo();
            if (idadministrativo != null) {
                idadministrativo.getPrestamoCollection().remove(prestamo);
                idadministrativo = em.merge(idadministrativo);
            }
            Usuario idusuario = prestamo.getIdusuario();
            if (idusuario != null) {
                idusuario.getPrestamoCollection().remove(prestamo);
                idusuario = em.merge(idusuario);
            }
            em.remove(prestamo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Prestamo> findPrestamoEntities() {
        return findPrestamoEntities(true, -1, -1);
    }

    public List<Prestamo> findPrestamoEntities(int maxResults, int firstResult) {
        return findPrestamoEntities(false, maxResults, firstResult);
    }

    private List<Prestamo> findPrestamoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Prestamo.class));
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

    public Prestamo findPrestamo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Prestamo.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrestamoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Prestamo> rt = cq.from(Prestamo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
