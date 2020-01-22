/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package businessLogic;


import exceptions.LoginNotFoundException;
import exceptions.NoEsUserException;
import exceptions.PasswordWrongException;
import java.util.logging.Logger;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import service.UserRESTClient;
import transferObjects.ClienteBean;
import transferObjects.UserBean;

/**
 *
 * @author Usuario
 */
public class UserManagerImplementation implements UserManager {
    private UserRESTClient webClient;
    private static final Logger LOGGER = Logger.getLogger("BusinessLogic.UserManagerImplementation");
    
    public UserManagerImplementation() {
        webClient= new UserRESTClient();
    }
    
    @Override
    public void createUser(UserBean user) throws BusinessLogicException{
        try{
            webClient.createUser(user);
        }catch(Exception e){
            LOGGER.severe("ERROR! UserManagerImpl -> CreateUser: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
        
    }
    @Override
    public void updateUser(UserBean user) throws BusinessLogicException{
        try{
            webClient.updateUser(user);
        }catch(Exception e){
            LOGGER.severe("ERROR! UserManagerImpl -> updateUser: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
        
    }
    @Override
    public Object iniciarSesion(String login,String contrasenia)throws BusinessLogicException, PasswordWrongException, LoginNotFoundException, NoEsUserException{
        Object elUsuario=null;
        try{
            elUsuario=webClient.iniciarSesion(UserBean.class, login, contrasenia);
        }catch(NotAuthorizedException e){
            LOGGER.severe("ERROR! UserManagerImpl -> iniciarSesion: "+e.getMessage());
            throw new PasswordWrongException(e.getMessage());
        }catch(NotFoundException e){
            LOGGER.severe("ERROR! UserManagerImpl -> iniciarSesion: "+e.getMessage());
            throw new LoginNotFoundException(e.getMessage());
        }catch(BadRequestException e){
            LOGGER.severe("ERROR! UserManagerImpl -> iniciarSesion: "+e.getMessage());
            throw new NoEsUserException(e.getMessage());
        }catch(Exception e){
            LOGGER.severe("ERROR! UserManagerImpl -> iniciarSesion: "+e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
        return elUsuario;
    }
}
