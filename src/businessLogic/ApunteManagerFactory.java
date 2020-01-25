/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import java.util.ResourceBundle;

/**
 *
 * @author 2dam
 */
public class ApunteManagerFactory {
    public static final String TEST="TEST";
    
    public static ApunteManager createApunteManager(String type) {
        //The object to be returned.
        ApunteManager apunteManager=null;
        //Evaluate type parameter.
        switch(type){
            case TEST:
                break;
            default:
                apunteManager=new ApunteManagerImplementation();
        }
        return apunteManager;
    }
}
