/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author 2dam
 */
public class LoginNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>loginNotFoundException</code> without
     * detail message.
     */
    public LoginNotFoundException() {
    }

    /**
     * Constructs an instance of <code>loginNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public LoginNotFoundException(String msg) {
        super(msg);
    }
}
