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
public class UserManagerFactory {
    public static final String TEST="TEST";
    
    public static UserManager createUserManager(String type) {
        //The object to be returned.
        UserManager userManager=null;
        //Evaluate type parameter.
        switch(type){
            case TEST:
                break;
            default:
                userManager=new UserManagerImplementation();
        }
        return userManager;
    }
}
