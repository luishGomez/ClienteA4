/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import fakeBusinessLogic.TestApunteManagerImplementation;
import java.util.ResourceBundle;

/**
 *
 * @author 2dam
 */
public class ApunteManagerFactory {
    private static ResourceBundle configFile=ResourceBundle.getBundle("businessLogic.testConfig");
    private static final String tipo_ejecucion=configFile.getString("tipo_ejecucion");
    public static final String TEST="TEST";
    
    public static ApunteManager createApunteManager(String type) {
        //The object to be returned.
        ApunteManager apunteManager=null;
        //Evaluate type parameter.
        switch(tipo_ejecucion){
            case TEST:
                apunteManager= new TestApunteManagerImplementation();
                break;
            default:
                apunteManager=new ApunteManagerImplementation();
        }
        return apunteManager;
    }
}
