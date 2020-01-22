/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

/**
 *
 * @author Usuario
 */
public class OfertaManagerFactory {
     public static final String TEST="TEST";
    
    public static OfertaManager createOfertaManager(String type) {
        //The object to be returned.
        OfertaManager ofertaManager=null;
        //Evaluate type parameter.
        switch(type){
            case TEST:
                break;
            default:
                ofertaManager=new OfertaManagerImplementation();
        }
        return ofertaManager;
    }
}
