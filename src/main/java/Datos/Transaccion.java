/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Carlos
 */
@Entity
@Table(name = "transaccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transaccion.findAll", query = "SELECT t FROM Transaccion t")
    , @NamedQuery(name = "Transaccion.findByIdtransaccion", query = "SELECT t FROM Transaccion t WHERE t.idtransaccion = :idtransaccion")
    , @NamedQuery(name = "Transaccion.findByObservaciones", query = "SELECT t FROM Transaccion t WHERE t.observaciones = :observaciones")})
public class Transaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "idtransaccion")
    private String idtransaccion;
    @Size(max = 2147483647)
    @Column(name = "observaciones")
    private String observaciones;
    @JoinColumn(name = "idherramienta", referencedColumnName = "idherramienta")
    @ManyToOne(optional = false)
    private Herramienta idherramienta;
    @JoinColumn(name = "codprestamo", referencedColumnName = "codprestamo")
    @ManyToOne(optional = false)
    private Prestamo codprestamo;

    public Transaccion() {
    }

    public Transaccion(String idtransaccion) {
        this.idtransaccion = idtransaccion;
    }

    public String getIdtransaccion() {
        return idtransaccion;
    }

    public void setIdtransaccion(String idtransaccion) {
        this.idtransaccion = idtransaccion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Herramienta getIdherramienta() {
        return idherramienta;
    }

    public void setIdherramienta(Herramienta idherramienta) {
        this.idherramienta = idherramienta;
    }

    public Prestamo getCodprestamo() {
        return codprestamo;
    }

    public void setCodprestamo(Prestamo codprestamo) {
        this.codprestamo = codprestamo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtransaccion != null ? idtransaccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaccion)) {
            return false;
        }
        Transaccion other = (Transaccion) object;
        if ((this.idtransaccion == null && other.idtransaccion != null) || (this.idtransaccion != null && !this.idtransaccion.equals(other.idtransaccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Datos.Transaccion[ idtransaccion=" + idtransaccion + " ]";
    }
    
}
