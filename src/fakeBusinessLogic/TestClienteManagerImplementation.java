/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakeBusinessLogic;

import businessLogic.BusinessLogicException;
import businessLogic.ClienteManager;
import exceptions.LoginNotFoundException;
import exceptions.PasswordWrongException;
import java.util.HashSet;
import java.util.Set;
import transferObjects.ClienteBean;
import transferObjects.UserPrivilege;

/**
 *
 * @author 2dam
 */
public class TestClienteManagerImplementation implements  ClienteManager{
    private Set<ClienteBean> clientes;
    public TestClienteManagerImplementation(){
        clientes=new HashSet<ClienteBean>();
        //Clientes
        ClienteBean cliente1=new ClienteBean();
        cliente1.setId(1);
        cliente1.setLogin("kevin");
        cliente1.setNombreCompleto("Kevin Garcia Goikoextxea");
        cliente1.setPrivilegio(UserPrivilege.USER);
        ClienteBean cliente2=new ClienteBean();
        cliente2.setId(2);
        cliente2.setLogin("nerea");
        cliente2.setNombreCompleto("Nerea Bujedo Fernandez");
        cliente2.setPrivilegio(UserPrivilege.USER);
        ClienteBean cliente3=new ClienteBean();
        cliente3.setId(3);
        cliente3.setLogin("adrian");
        cliente3.setNombreCompleto("Adrian Villanueva Ereida");
        cliente3.setPrivilegio(UserPrivilege.USER);
        clientes.add(cliente3);
        clientes.add(cliente2);
        clientes.add(cliente1);
    }

    @Override
    public void create(ClienteBean cliente) throws BusinessLogicException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(ClienteBean cliente) throws BusinessLogicException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Integer id) throws BusinessLogicException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ClienteBean find(Integer id) throws BusinessLogicException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<ClienteBean> findAll() throws BusinessLogicException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<ClienteBean> getVotantesId(Integer id) throws BusinessLogicException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actualizarContrasenia(ClienteBean cliente) throws BusinessLogicException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void comprarApunte(ClienteBean cliente, Integer idApunte) throws BusinessLogicException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean passwordForgot(String login) throws BusinessLogicException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ClienteBean iniciarSesion(String login, String contrasenia) throws BusinessLogicException, PasswordWrongException, LoginNotFoundException {
        ClienteBean resultado=null;
        for(ClienteBean cliente:clientes){
            if(cliente.getLogin()==login){
                resultado=cliente;
                break;
            }
        }
        if(resultado==null)
            throw new LoginNotFoundException("No existe ese usuario fake");
        return resultado;
    }
    
}
