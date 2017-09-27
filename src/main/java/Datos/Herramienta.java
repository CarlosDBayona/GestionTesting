/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Carlos
 */
@Entity
@Table(name = "herramienta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Herramienta.findAll", query = "SELECT h FROM Herramienta h")
    , @NamedQuery(name = "Herramienta.findByIdherramienta", query = "SELECT h FROM Herramienta h WHERE h.idherramienta = :idherramienta")
    , @NamedQuery(name = "Herramienta.findByNombreherramienta", query = "SELECT h FROM Herramienta h WHERE h.nombreherramienta = :nombreherramienta")
    , @NamedQuery(name = "Herramienta.findByNoserial", query = "SELECT h FROM Herramienta h WHERE h.noserial = :noserial")
    , @NamedQuery(name = "Herramienta.findByDescripcionherramienta", query = "SELECT h FROM Herramienta h WHERE h.descripcionherramienta = :descripcionherramienta")
    , @NamedQuery(name = "Herramienta.findByEstado", query = "SELECT h FROM Herramienta h WHERE h.estado = :estado")})
public class Herramienta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "idherramienta")
    private String idherramienta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nombreherramienta")
    private String nombreherramienta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "noserial")
    private String noserial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "descripcionherramienta")
    private String descripcionherramienta;
    @Size(max = 2147483647)
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "idadministrativo", referencedColumnName = "idadministrativo")
    @ManyToOne(optional = false)
    private Administrativo idadministrativo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idherramienta")
    private Collection<Transaccion> transaccionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idherramienta")
    private Collection<Mantenimiento> mantenimientoCollection;

    public Herramienta() {
    }

    public Herramienta(String idherramienta) {
        this.idherramienta = idherramienta;
    }

    public Herramienta(String idherramienta, String nombreherramienta, String noserial, String descripcionherramienta) {
        this.idherramienta = idherramienta;
        this.nombreherramienta = nombreherramienta;
        this.noserial = noserial;
        this.descripcionherramienta = descripcionherramienta;
    }

    public String getIdherramienta() {
        return idherramienta;
    }

    public void setIdherramienta(String idherramienta) {
        this.idherramienta = idherramienta;
    }

    public String getNombreherramienta() {
        return nombreherramienta;
    }

    public void setNombreherramienta(String nombreherramienta) {
        this.nombreherramienta = nombreherramienta;
    }

    public String getNoserial() {
        return noserial;
    }

    public void setNoserial(String noserial) {
        this.noserial = noserial;
    }

    public String getDescripcionherramienta() {
        return descripcionherramienta;
    }

    public void setDescripcionherramienta(String descripcionherramienta) {
        this.descripcionherramienta = descripcionherramienta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Administrativo getIdadministrativo() {
        return idadministrativo;
    }

    public void setIdadministrativo(Administrativo idadministrativo) {
        this.idadministrativo = idadministrativo;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Transaccion> getTransaccionCollection() {
        return transaccionCollection;
    }

    public void setTransaccionCollection(Collection<Transaccion> transaccionCollection) {
        this.transaccionCollection = transaccionCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Mantenimiento> getMantenimientoCollection() {
        return mantenimientoCollection;
    }

    public void setMantenimientoCollection(Collection<Mantenimiento> mantenimientoCollection) {
        this.mantenimientoCollection = mantenimientoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idherramienta != null ? idherramienta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Herramienta)) {
            return false;
        }
        Herramienta other = (Herramienta) object;
        if ((this.idherramienta == null && other.idherramienta != null) || (this.idherramienta != null && !this.idherramienta.equals(other.idherramienta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Datos.Herramienta[ idherramienta=" + idherramienta + " ]";
    }
    
}
