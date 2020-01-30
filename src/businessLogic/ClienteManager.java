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
 *
 * @author Usuario
 */
public interface ClienteManager {
    public void create(ClienteBean cliente) throws BusinessLogicException;
    public void edit( ClienteBean cliente) throws BusinessLogicException;
    public void remove(Integer id) throws BusinessLogicException;
    public ClienteBean find( Integer id) throws BusinessLogicException;
    public Set<ClienteBean> findAll() throws BusinessLogicException;
    public Set <ClienteBean> getVotantesId( Integer id) throws BusinessLogicException;
    public void actualizarContrasenia(ClienteBean cliente) throws BusinessLogicException;
    public void comprarApunte(ClienteBean cliente, Integer idApunte) throws BusinessLogicException;
    public boolean passwordForgot( String login) throws BusinessLogicException,NotFoundException;
    public ClienteBean iniciarSesion(String login,String contrasenia)throws BusinessLogicException, PasswordWrongException, LoginNotFoundException;
    
}
