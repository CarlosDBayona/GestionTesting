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
import Datos.Prestamo;
import Datos.Transaccion;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Carlos
 */
public class TransaccionDAO implements Serializable {

    private EntityManager em=null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Transaccion transaccion) throws PreexistingEntityException, Exception {
       startOperation();
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Herramienta idherramienta = transaccion.getIdherramienta();
            if (idherramienta != null) {
                idherramienta = em.getReference(idherramienta.getClass(), idherramienta.getIdherramienta());
                transaccion.setIdherramienta(idherramienta);
            }
            Prestamo codprestamo = transaccion.getCodprestamo();
            if (codprestamo != null) {
                codprestamo = em.getReference(codprestamo.getClass(), codprestamo.getCodprestamo());
                transaccion.setCodprestamo(codprestamo);
            }
            em.persist(transaccion);
            if (idherramienta != null) {
                idherramienta.getTransaccionCollection().add(transaccion);
                idherramienta = em.merge(idherramienta);
            }
            if (codprestamo != null) {
                codprestamo.getTransaccionCollection().add(transaccion);
                codprestamo = em.merge(codprestamo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTransaccion(transaccion.getIdtransaccion()) != null) {
                throw new PreexistingEntityException("Transaccion " + transaccion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
                emf.close();
            }
        }
    }

    public void edit(Transaccion transaccion) throws NonexistentEntityException, Exception {
        startOperation();
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaccion persistentTransaccion = em.find(Transaccion.class, transaccion.getIdtransaccion());
            Herramienta idherramientaOld = persistentTransaccion.getIdherramienta();
            Herramienta idherramientaNew = transaccion.getIdherramienta();
            Prestamo codprestamoOld = persistentTransaccion.getCodprestamo();
            Prestamo codprestamoNew = transaccion.getCodprestamo();
            if (idherramientaNew != null) {
                idherramientaNew = em.getReference(idherramientaNew.getClass(), idherramientaNew.getIdherramienta());
                transaccion.setIdherramienta(idherramientaNew);
            }
            if (codprestamoNew != null) {
                codprestamoNew = em.getReference(codprestamoNew.getClass(), codprestamoNew.getCodprestamo());
                transaccion.setCodprestamo(codprestamoNew);
            }
            transaccion = em.merge(transaccion);
            if (idherramientaOld != null && !idherramientaOld.equals(idherramientaNew)) {
                idherramientaOld.getTransaccionCollection().remove(transaccion);
                idherramientaOld = em.merge(idherramientaOld);
            }
            if (idherramientaNew != null && !idherramientaNew.equals(idherramientaOld)) {
                idherramientaNew.getTransaccionCollection().add(transaccion);
                idherramientaNew = em.merge(idherramientaNew);
            }
            if (codprestamoOld != null && !codprestamoOld.equals(codprestamoNew)) {
                codprestamoOld.getTransaccionCollection().remove(transaccion);
                codprestamoOld = em.merge(codprestamoOld);
            }
            if (codprestamoNew != null && !codprestamoNew.equals(codprestamoOld)) {
                codprestamoNew.getTransaccionCollection().add(transaccion);
                codprestamoNew = em.merge(codprestamoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = transaccion.getIdtransaccion();
                if (findTransaccion(id) == null) {
                    throw new NonexistentEntityException("The transaccion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
                emf.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        startOperation();
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaccion transaccion;
            try {
                transaccion = em.getReference(Transaccion.class, id);
                transaccion.getIdtransaccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transaccion with id " + id + " no longer exists.", enfe);
            }
            Herramienta idherramienta = transaccion.getIdherramienta();
            if (idherramienta != null) {
                idherramienta.getTransaccionCollection().remove(transaccion);
                idherramienta = em.merge(idherramienta);
            }
            Prestamo codprestamo = transaccion.getCodprestamo();
            if (codprestamo != null) {
                codprestamo.getTransaccionCollection().remove(transaccion);
                codprestamo = em.merge(codprestamo);
            }
            em.remove(transaccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
                emf.close();
            }
        }
    }

    public List<Transaccion> findTransaccionEntities() {
        return findTransaccionEntities(true, -1, -1);
    }

    public List<Transaccion> findTransaccionEntities(int maxResults, int firstResult) {
        return findTransaccionEntities(false, maxResults, firstResult);
    }

    private List<Transaccion> findTransaccionEntities(boolean all, int maxResults, int firstResult) {
        startOperation();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Transaccion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
            emf.close();
        }
    }

    public Transaccion findTransaccion(String id) {
        startOperation();
        try {
            return em.find(Transaccion.class, id);
        } finally {
            em.close();
            emf.close();
        }
    }

    public int getTransaccionCount() {
        startOperation();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Transaccion> rt = cq.from(Transaccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
            emf.close();
        }
    }
    protected void startOperation() { 
        URI dbUri = null;
        try {
            dbUri = new URI(System.getenv("DATABASE_URL")); 
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

            Map<String, String> properties = new HashMap<String, String>();
            properties.put("javax.persistence.jdbc.url", dbUrl);
            properties.put("javax.persistence.jdbc.user", username );
            properties.put("javax.persistence.jdbc.password", password );
            properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            this.emf = Persistence.createEntityManagerFactory("GestionInventario",properties);
            this.em = emf.createEntityManager();
        } catch (URISyntaxException ex) {
            Logger.getLogger(TransaccionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
