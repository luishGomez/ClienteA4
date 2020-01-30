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
 *
 * @author Usuario
 */
public class ClienteManagerImplementation implements ClienteManager {
    private ClienteRESTClient webClient;
    private static final Logger LOGGER = Logger.getLogger("BusinessLogic.ClienteManagerImplementation");
    
    public ClienteManagerImplementation() {
        webClient= new ClienteRESTClient();
    }
    @Override
    public void create(ClienteBean cliente) throws BusinessLogicException {
        try{
            webClient.create(cliente);
        }catch(Exception e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> CreateUser: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }

    @Override
    public void edit(ClienteBean cliente) throws BusinessLogicException {
        try{
            webClient.edit(cliente);
        }catch(Exception e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> edit: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }

    @Override
    public void remove(Integer id) throws BusinessLogicException {
        try{
           webClient.remove(id.toString());
        }catch(Exception e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> remove: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }

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

    @Override
    public void actualizarContrasenia(ClienteBean cliente) throws BusinessLogicException {
        try{
           webClient.actualizarContrasenia(cliente);
        }catch(Exception e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> actualizarContrasenia: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }

    @Override
    public void comprarApunte(ClienteBean cliente, Integer idApunte) throws BusinessLogicException {
        try{
           webClient.comprarApunte(cliente, idApunte.toString());
        }catch(Exception e){
            LOGGER.severe("ERROR! ClienteManagerImplementation -> comprarApunte: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }

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
