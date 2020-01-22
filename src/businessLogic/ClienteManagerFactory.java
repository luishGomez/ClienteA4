/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import fakeBusinessLogic.TestClienteManagerImplementation;

/**
 *
 * @author Usuario
 */
public class ClienteManagerFactory {
     public static final String TEST="TEST";
    
    public static ClienteManager createClienteManager(String type) {
        //The object to be returned.
        ClienteManager clienteManager=null;
        //Evaluate type parameter.
        switch(type){
            case TEST:
                clienteManager=new TestClienteManagerImplementation();
                break;
            default:
                clienteManager=new ClienteManagerImplementation();
        }
        return clienteManager;
    }
}
