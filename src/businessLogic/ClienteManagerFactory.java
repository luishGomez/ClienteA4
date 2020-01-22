/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import fakeBusinessLogic.TestClienteManagerImplementation;
import java.util.ResourceBundle;

/**
 *
 * @author Usuario
 */
public class ClienteManagerFactory {
    private static ResourceBundle configFile=ResourceBundle.getBundle("businessLogic.testConfig");
    private static final String tipo_ejecucion=configFile.getString("tipo_ejecucion");
     public static final String TEST="TEST";
    
    public static ClienteManager createClienteManager(String type) {
        //The object to be returned.
        ClienteManager clienteManager=null;
        //Evaluate type parameter.
        switch(tipo_ejecucion){
            case TEST:
                clienteManager=new TestClienteManagerImplementation();
                break;
            default:
                clienteManager=new ClienteManagerImplementation();
        }
        return clienteManager;
    }
}
