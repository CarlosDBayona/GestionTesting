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
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
    , @NamedQuery(name = "Usuario.findByIdusuario", query = "SELECT u FROM Usuario u WHERE u.idusuario = :idusuario")
    , @NamedQuery(name = "Usuario.findByNombreusuario", query = "SELECT u FROM Usuario u WHERE u.nombreusuario = :nombreusuario")
    , @NamedQuery(name = "Usuario.findByApellidousuario", query = "SELECT u FROM Usuario u WHERE u.apellidousuario = :apellidousuario")
    , @NamedQuery(name = "Usuario.findBySemestreusuario", query = "SELECT u FROM Usuario u WHERE u.semestreusuario = :semestreusuario")
    , @NamedQuery(name = "Usuario.findByCarrerausuario", query = "SELECT u FROM Usuario u WHERE u.carrerausuario = :carrerausuario")
    , @NamedQuery(name = "Usuario.findByCargousuario", query = "SELECT u FROM Usuario u WHERE u.cargousuario = :cargousuario")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "idusuario")
    private String idusuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nombreusuario")
    private String nombreusuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "apellidousuario")
    private String apellidousuario;
    @Column(name = "semestreusuario")
    private Integer semestreusuario;
    @Size(max = 2147483647)
    @Column(name = "carrerausuario")
    private String carrerausuario;
    @Size(max = 2147483647)
    @Column(name = "cargousuario")
    private String cargousuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idusuario")
    private Collection<Prestamo> prestamoCollection;

    public Usuario() {
    }

    public Usuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public Usuario(String idusuario, String nombreusuario, String apellidousuario) {
        this.idusuario = idusuario;
        this.nombreusuario = nombreusuario;
        this.apellidousuario = apellidousuario;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getNombreusuario() {
        return nombreusuario;
    }

    public void setNombreusuario(String nombreusuario) {
        this.nombreusuario = nombreusuario;
    }

    public String getApellidousuario() {
        return apellidousuario;
    }

    public void setApellidousuario(String apellidousuario) {
        this.apellidousuario = apellidousuario;
    }

    public Integer getSemestreusuario() {
        return semestreusuario;
    }

    public void setSemestreusuario(Integer semestreusuario) {
        this.semestreusuario = semestreusuario;
    }

    public String getCarrerausuario() {
        return carrerausuario;
    }

    public void setCarrerausuario(String carrerausuario) {
        this.carrerausuario = carrerausuario;
    }

    public String getCargousuario() {
        return cargousuario;
    }

    public void setCargousuario(String cargousuario) {
        this.cargousuario = cargousuario;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Prestamo> getPrestamoCollection() {
        return prestamoCollection;
    }

    public void setPrestamoCollection(Collection<Prestamo> prestamoCollection) {
        this.prestamoCollection = prestamoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idusuario != null ? idusuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idusuario == null && other.idusuario != null) || (this.idusuario != null && !this.idusuario.equals(other.idusuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Datos.Usuario[ idusuario=" + idusuario + " ]";
    }
    
}
