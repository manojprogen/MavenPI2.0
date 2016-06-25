/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.db;

/**
 *
 * @author arun
 */
public class InvalidDataException extends Exception {

    public InvalidDataException(String msg) {
        super(msg);
    }

    public InvalidDataException() {
    }
}
