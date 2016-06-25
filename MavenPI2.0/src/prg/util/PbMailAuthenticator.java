/*
 * PbMailAuthenticator.java
 *
 * Created on April 25, 2009, 5:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package prg.util;

/**
 *
 * @author Administrator
 */
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class PbMailAuthenticator extends Authenticator {

    public String myUserId;
    public String myPassword;

    /**
     * Creates a new instance of PbMailAuthenticator
     */
    public PbMailAuthenticator() {
        myUserId = null;
        myPassword = null;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(myUserId, myPassword);
    }
}
