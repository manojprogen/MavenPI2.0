/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.db;

/**
 *
 * @author arun
 */
public class PGConnectionException extends Exception {

    public PGConnectionException(String msg) {
        super(msg);
    }

    public PGConnectionException() {
    }
}
