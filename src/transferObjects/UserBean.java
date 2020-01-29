/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package transferObjects;

import java.io.Serializable;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sergio
 */
@XmlRootElement(name="user")
public class UserBean implements Serializable{
    private static final long serialVersionUID=1L;
    
    private Integer id;
    private String login;
    private String email;
    private String nombreCompleto;
    //private final SimpleStringProperty nombreCompleto;
    private UserStatus status;
    private UserPrivilege privilegio;
    private String contrasenia;
    private Date ultimoAcceso;
    private Date ultimoCambioContrasenia;
     public UserBean() {
    }

    public UserBean(Integer id, String login, String email, String nombreCompleto, UserStatus status, UserPrivilege privilegio, String contrasenia, Date ultimoAcceso, Date ultimoCambioContrasenia) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.status = status;
        this.privilegio = privilegio;
        this.contrasenia = contrasenia;
        this.ultimoAcceso = ultimoAcceso;
        this.ultimoCambioContrasenia = ultimoCambioContrasenia;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the nombreCompleto
     */
    public String getNombreCompleto() {
        return nombreCompleto;
        //return nombreCompleto.get();
    }

    /**
     * @param nombreCompleto the nombreCompleto to set
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
        //this.nombreCompleto.set(nombreCompleto);
    }

    /**
     * @return the status
     */
    public UserStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    /**
     * @return the privilegio
     */
    public UserPrivilege getPrivilegio() {
        return privilegio;
    }

    /**
     * @param privilegio the privilegio to set
     */
    public void setPrivilegio(UserPrivilege privilegio) {
        this.privilegio = privilegio;
    }

    /**
     * @return the contrasenia
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * @param contrasenia the contrasenia to set
     */
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    /**
     * @return the ultimoAcceso
     */
    public Date getUltimoAcceso() {
        return ultimoAcceso;
    }

    /**
     * @param ultimoAcceso the ultimoAcceso to set
     */
    public void setUltimoAcceso(Date ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    /**
     * @return the ultimoCambioContrasenia
     */
    public Date getUltimoCambioContrasenia() {
        return ultimoCambioContrasenia;
    }

    /**
     * @param ultimoCambioContrasenia the ultimoCambioContrasenia to set
     */
    public void setUltimoCambioContrasenia(Date ultimoCambioContrasenia) {
        this.ultimoCambioContrasenia = ultimoCambioContrasenia;
    }

   

    @Override
    public String toString() {
        return nombreCompleto;
    }
    
    
    
    
}
