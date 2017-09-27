/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "prestamo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prestamo.findAll", query = "SELECT p FROM Prestamo p")
    , @NamedQuery(name = "Prestamo.findByCodprestamo", query = "SELECT p FROM Prestamo p WHERE p.codprestamo = :codprestamo")
    , @NamedQuery(name = "Prestamo.findByTipopractica", query = "SELECT p FROM Prestamo p WHERE p.tipopractica = :tipopractica")
    , @NamedQuery(name = "Prestamo.findByFechaentrada", query = "SELECT p FROM Prestamo p WHERE p.fechaentrada = :fechaentrada")
    , @NamedQuery(name = "Prestamo.findByFechasalida", query = "SELECT p FROM Prestamo p WHERE p.fechasalida = :fechasalida")
    , @NamedQuery(name = "Prestamo.findByTipoprestamo", query = "SELECT p FROM Prestamo p WHERE p.tipoprestamo = :tipoprestamo")})
public class Prestamo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "codprestamo")
    private String codprestamo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "tipopractica")
    private String tipopractica;
    @Column(name = "fechaentrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaentrada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechasalida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechasalida;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "tipoprestamo")
    private String tipoprestamo;
    @JoinColumn(name = "idadministrativo", referencedColumnName = "idadministrativo")
    @ManyToOne(optional = false)
    private Administrativo idadministrativo;
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idusuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codprestamo")
    private Collection<Transaccion> transaccionCollection;

    public Prestamo() {
    }

    public Prestamo(String codprestamo) {
        this.codprestamo = codprestamo;
    }

    public Prestamo(String codprestamo, String tipopractica, Date fechasalida, String tipoprestamo) {
        this.codprestamo = codprestamo;
        this.tipopractica = tipopractica;
        this.fechasalida = fechasalida;
        this.tipoprestamo = tipoprestamo;
    }

    public String getCodprestamo() {
        return codprestamo;
    }

    public void setCodprestamo(String codprestamo) {
        this.codprestamo = codprestamo;
    }

    public String getTipopractica() {
        return tipopractica;
    }

    public void setTipopractica(String tipopractica) {
        this.tipopractica = tipopractica;
    }

    public Date getFechaentrada() {
        return fechaentrada;
    }

    public void setFechaentrada(Date fechaentrada) {
        this.fechaentrada = fechaentrada;
    }

    public Date getFechasalida() {
        return fechasalida;
    }

    public void setFechasalida(Date fechasalida) {
        this.fechasalida = fechasalida;
    }

    public String getTipoprestamo() {
        return tipoprestamo;
    }

    public void setTipoprestamo(String tipoprestamo) {
        this.tipoprestamo = tipoprestamo;
    }

    public Administrativo getIdadministrativo() {
        return idadministrativo;
    }

    public void setIdadministrativo(Administrativo idadministrativo) {
        this.idadministrativo = idadministrativo;
    }

    public Usuario getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Usuario idusuario) {
        this.idusuario = idusuario;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Transaccion> getTransaccionCollection() {
        return transaccionCollection;
    }

    public void setTransaccionCollection(Collection<Transaccion> transaccionCollection) {
        this.transaccionCollection = transaccionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codprestamo != null ? codprestamo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prestamo)) {
            return false;
        }
        Prestamo other = (Prestamo) object;
        if ((this.codprestamo == null && other.codprestamo != null) || (this.codprestamo != null && !this.codprestamo.equals(other.codprestamo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Datos.Prestamo[ codprestamo=" + codprestamo + " ]";
    }
    
}
