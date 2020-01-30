/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import exceptions.LoginNotFoundException;
import exceptions.PasswordWrongException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.GenericType;
import service.ClienteRESTClient;
import transferObjects.ClienteBean;

/**
 *  La clase que implementa el manager de cliente.
 * @author Ricardo Peinado Lastra
 */
public class ClienteManagerImplementation implements ClienteManager {
    private ClienteRESTClient webClient;
    private static final Logger LOGGER = Logger.getLogger("BusinessLogic.ClienteManagerImplementation");
    
    public ClienteManagerImplementation() {
        webClient= new ClienteRESTClient();
    }
    /**
     * Crea un cliente.
     * @param cliente Los datos del cliente
     * @throws BusinessLogicException  Si ocurre algun error al crear el cliente.
     */
    @Override
    public void create(ClienteBean cliente) throws BusinessLogicException {
        try{
            webClient.create(cliente);
        }catch(Exception e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> CreateUser: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }
    /**
     * Permite modificar un cliente
     * @param cliente Los datos de un cliente.
     * @throws BusinessLogicException Salta si ocurre algun error.
     */
    @Override
    public void edit(ClienteBean cliente) throws BusinessLogicException {
        try{
            webClient.edit(cliente);
        }catch(Exception e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> edit: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }
    /**
     * Permite borrar un cliente.
     * @param id El identificador del cliente.
     * @throws BusinessLogicException Salta si ocurre algun error
     */
    @Override
    public void remove(Integer id) throws BusinessLogicException {
        try{
           webClient.remove(id.toString());
        }catch(Exception e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> remove: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }
    /**
     * Busca un cliente por su identificador.
     * @param id El identificador del cliente.
     * @return Retorna los datos del cliente.
     * @throws BusinessLogicException Salta si ocurre algun error.
     */
    @Override
    public ClienteBean find(Integer id) throws BusinessLogicException {
        ClienteBean resultado=null;
        try{
            resultado=webClient.find(ClienteBean.class, id.toString());
        }catch(Exception e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> find: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
        return resultado;
    }
    /**
     * Busca todos los clientes.
     * @return Retorna todos los datos del clientes.
     * @throws BusinessLogicException Salta si ocurre algun error.
     */
    @Override
    public Set<ClienteBean> findAll() throws BusinessLogicException {
        Set<ClienteBean> resultado=null;
        try{
           resultado=webClient.findAll(new GenericType<Set<ClienteBean>>() {});
        }catch(Exception e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> findAll: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
        return resultado;
    }
    /**
     * Devuelve la lista de clientes que han votado un apunte en especifico.
     * @param id El identificador del apunte.
     * @return La lista de clientes de votantes.
     * @throws BusinessLogicException Salta si ocurre algun error.
     */
    @Override
    public Set<ClienteBean> getVotantesId(Integer id) throws BusinessLogicException {
        Set<ClienteBean> resultado=null;
        try{
           resultado=webClient.getVotantesId(new GenericType<Set<ClienteBean>>() {}, id.toString());
        }catch(Exception e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> getVotantesId: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
        return resultado;
    }
    /**
     * Permite al cliente actualizar su contraseña.
     * @param cliente Los datos del cliente.
     * @throws BusinessLogicException Salta si ocurre algun error.
     */
    @Override
    public void actualizarContrasenia(ClienteBean cliente) throws BusinessLogicException {
        try{
           webClient.actualizarContrasenia(cliente);
        }catch(Exception e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> actualizarContrasenia: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }
    /**
     * Permite comprar un apunte.
     * @param cliente Los datos del cliente.
     * @param idApunte El identificador del apunte.
     * @throws BusinessLogicException  Salta si ocurre algun error.
     */
    @Override
    public void comprarApunte(ClienteBean cliente, Integer idApunte) throws BusinessLogicException {
        try{
           webClient.comprarApunte(cliente, idApunte.toString());
        }catch(Exception e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> comprarApunte: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }
    /**
     * Permite enviar una nueva contraseña al email de la cuenta.
     * @param login El login del usuario.
     * @return Retorna si todo a ido bien.
     * @throws BusinessLogicException Salta si ocurre un error.
     * @throws NotFoundException  Salta si ese login no existe.
     */
    @Override
    public boolean passwordForgot(String login) throws BusinessLogicException,NotFoundException {
        boolean resultado=false;
        try{
           resultado=webClient.passwordForgot(Boolean.class, login);
           
        }catch(NotFoundException e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> passwordForgot: "+e.getMessage());
            throw new NotFoundException(e.getMessage());
        }catch(Exception e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> passwordForgot: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
        return resultado;
    }
    /**
     * Permite iniciar sesion a un cliente.
     * @param login El login
     * @param contrasenia La contraseña
     * @return Los datos del cliente
     * @throws BusinessLogicException Salta si ocurre algun error.
     * @throws PasswordWrongException Salta si la contraseña es incorrecta.
     * @throws LoginNotFoundException  Salta si el login no existe.
     */
    @Override
    public ClienteBean iniciarSesion(String login,String contrasenia)throws BusinessLogicException, PasswordWrongException, LoginNotFoundException{
        ClienteBean resultado=null;
        try{
           resultado=webClient.iniciarSesion(ClienteBean.class, login, contrasenia);
        }catch(NotAuthorizedException e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> iniciarSesion: "+e.getMessage()+" "+login+" "+contrasenia);
            throw new PasswordWrongException(e.getMessage());
        }catch(NotFoundException e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> iniciarSesion: "+e.getMessage()+" "+login+" "+contrasenia);
            throw new LoginNotFoundException(e.getMessage());
        }catch(Exception e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> iniciarSesion: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
        return resultado;
    }
    
}
