/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import exceptions.LoginNotFoundException;
import exceptions.PasswordWrongException;
import java.util.Set;
import javax.ws.rs.NotFoundException;
import transferObjects.ClienteBean;

/**
 * La interfaz que controla los clientes.
 * @author Ricardo Peinado Lastra.
 */
public interface ClienteManager {
    /**
     * Crea un cliente
     * @param cliente Los datos del cliente
     * @throws BusinessLogicException  Salta si ocurre algun error.
     */
    public void create(ClienteBean cliente) throws BusinessLogicException;
    /**
     * Edita un cliente
     * @param cliente Los datos del cliente
     * @throws BusinessLogicException  Salta si ocurre algun error.
     */
    public void edit( ClienteBean cliente) throws BusinessLogicException;
    /**
     * Borra un cliente por su identificador.
     * @param id El identificador del cliente.
     * @throws BusinessLogicException Salta si ocurre algun error.
     */
    public void remove(Integer id) throws BusinessLogicException;
    /**
     * Busca un cliente por su identificador.
     * @param id El identificador del cliente.
     * @return Los datos del cliente.
     * @throws BusinessLogicException Salta si ocurre algun error. 
     */
    public ClienteBean find( Integer id) throws BusinessLogicException;
    /**
     * Devuelve todos los clientes.
     * @return Una lista de todos los clientes.
     * @throws BusinessLogicException  Salta si ocurre algun error.
     */
    public Set<ClienteBean> findAll() throws BusinessLogicException;
    /**
     * Devuelve una lista de clientes que hallan votado un determinado apunte.
     * @param id El identificador del apunte.
     * @return La lista de clientes que halla votado el apunte.
     * @throws BusinessLogicException  Salta si ocurre algun error.
     */
    public Set <ClienteBean> getVotantesId( Integer id) throws BusinessLogicException;
    /**
     * Actualiza la contrase de un cliente.
     * @param cliente Los datos de un cliente.
     * @throws BusinessLogicException  Salta si ocurre algun error.
     */
    public void actualizarContrasenia(ClienteBean cliente) throws BusinessLogicException;
    /**
     * Permite comprar un apunte.
     * @param cliente Los datos del cliente.
     * @param idApunte El identificador del apunte.
     * @throws BusinessLogicException  Salta si courre algun error.
     */
    public void comprarApunte(ClienteBean cliente, Integer idApunte) throws BusinessLogicException;
    /**
     * Permite cambiar la contraseña del cliente.
     * @param login El login del cliente.
     * @return Retorna un TRUE si va todo bien FALSE en todo lo contrario
     * @throws BusinessLogicException Salta si ocurre algunn error.
     * @throws NotFoundException Salta si no se encutra el login.
     */
    public boolean passwordForgot( String login) throws BusinessLogicException,NotFoundException;
    /**
     * Permite iniciar sesion a un cliente.
     * @param login El login del cliente.
     * @param contrasenia La contraseña del cliente
     * @return Retorna los datos de un cliente.
     * @throws BusinessLogicException Salta si ocurre algun error.
     * @throws PasswordWrongException Salta si la contraseña no es correcta.
     * @throws LoginNotFoundException  Salta si el login no existe.
     */
    public ClienteBean iniciarSesion(String login,String contrasenia)throws BusinessLogicException, PasswordWrongException, LoginNotFoundException;
    
}
