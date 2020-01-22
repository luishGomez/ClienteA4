/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakeBusinessLogic;

import businessLogic.BusinessLogicException;
import businessLogic.UserManager;
import exceptions.LoginNotFoundException;
import exceptions.NoEsUserException;
import exceptions.PasswordWrongException;
import transferObjects.UserBean;
import transferObjects.UserPrivilege;

/**
 *
 * @author 2dam
 */
public class TestUserManagerImplementation implements UserManager {
    private UserBean admin;

    public TestUserManagerImplementation() {
        admin=new UserBean();
        admin.setId(0);
        admin.setLogin("admin");
        admin.setPrivilegio(UserPrivilege.ADMIN);
        
    }
    

    @Override
    public void createUser(UserBean user) throws BusinessLogicException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateUser(UserBean user) throws BusinessLogicException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object iniciarSesion(String login, String contrasenia) throws BusinessLogicException, PasswordWrongException, LoginNotFoundException, NoEsUserException {
        Object resultado=null;
        if(login.equals("admin")){
            resultado=admin;
        }else{
            throw new NoEsUserException("No es usuario");
        }
        return resultado;
    }
    
}
