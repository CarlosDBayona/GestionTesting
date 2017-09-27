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
@Table(name = "administrativo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Administrativo.findAll", query = "SELECT a FROM Administrativo a")
    , @NamedQuery(name = "Administrativo.findByIdadministrativo", query = "SELECT a FROM Administrativo a WHERE a.idadministrativo = :idadministrativo")
    , @NamedQuery(name = "Administrativo.findByNombreadmin", query = "SELECT a FROM Administrativo a WHERE a.nombreadmin = :nombreadmin")
    , @NamedQuery(name = "Administrativo.findByContrasena", query = "SELECT a FROM Administrativo a WHERE a.contrasena = :contrasena")
    , @NamedQuery(name = "Administrativo.findByTipopermiso", query = "SELECT a FROM Administrativo a WHERE a.tipopermiso = :tipopermiso")})
public class Administrativo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "idadministrativo")
    private String idadministrativo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nombreadmin")
    private String nombreadmin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "contrasena")
    private String contrasena;
    @Size(max = 2147483647)
    @Column(name = "tipopermiso")
    private String tipopermiso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idadministrativo")
    private Collection<Prestamo> prestamoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idadministrativo")
    private Collection<Herramienta> herramientaCollection;

    public Administrativo() {
    }

    public Administrativo(String idadministrativo) {
        this.idadministrativo = idadministrativo;
    }

    public Administrativo(String idadministrativo, String nombreadmin, String contrasena) {
        this.idadministrativo = idadministrativo;
        this.nombreadmin = nombreadmin;
        this.contrasena = contrasena;
    }

    public String getIdadministrativo() {
        return idadministrativo;
    }

    public void setIdadministrativo(String idadministrativo) {
        this.idadministrativo = idadministrativo;
    }

    public String getNombreadmin() {
        return nombreadmin;
    }

    public void setNombreadmin(String nombreadmin) {
        this.nombreadmin = nombreadmin;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTipopermiso() {
        return tipopermiso;
    }

    public void setTipopermiso(String tipopermiso) {
        this.tipopermiso = tipopermiso;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Prestamo> getPrestamoCollection() {
        return prestamoCollection;
    }

    public void setPrestamoCollection(Collection<Prestamo> prestamoCollection) {
        this.prestamoCollection = prestamoCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Herramienta> getHerramientaCollection() {
        return herramientaCollection;
    }

    public void setHerramientaCollection(Collection<Herramienta> herramientaCollection) {
        this.herramientaCollection = herramientaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idadministrativo != null ? idadministrativo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Administrativo)) {
            return false;
        }
        Administrativo other = (Administrativo) object;
        if ((this.idadministrativo == null && other.idadministrativo != null) || (this.idadministrativo != null && !this.idadministrativo.equals(other.idadministrativo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Datos.Administrativo[ idadministrativo=" + idadministrativo + " ]";
    }
    
}
