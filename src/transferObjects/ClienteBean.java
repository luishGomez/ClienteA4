package transferObjects;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * El POJO que almacena los datos de un cliente
 * @author Ricardo Peinado Lastra
 */
@XmlRootElement(name="cliente")
public class ClienteBean extends UserBean implements Serializable{
   
    private float saldo;
    private byte[] foto;

    public ClienteBean() {
    }

    public ClienteBean(float saldo, byte[] foto, Integer id, String login, String email, String nombreCompleto, UserStatus status, UserPrivilege privilegio, String contrasenia, Date ultimoAcceso, Date ultimoCambioContrasenia) {
        super(id, login, email, nombreCompleto, status, privilegio, contrasenia, ultimoAcceso, ultimoCambioContrasenia);
        this.saldo = saldo;
        this.foto = foto;
    }

    /**
     * @return the saldo
     */
    public float getSaldo() {
        return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    /**
     * @return the foto
     */
    public byte[] getFoto() {
        return foto;
    }

    /**
     * @param foto the foto to set
     */
    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
    
    
    
    
    
}
