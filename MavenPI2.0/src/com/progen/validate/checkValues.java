/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.validate;

/**
 *
 * @author Administrator
 */
public class checkValues {

    public boolean checkNonZeroEmpty(String checkVal) {
        if (checkVal == null) {
            return (false);
        }
        if (checkVal.equals("")) {
            return (false);
        }
        if (checkVal.equalsIgnoreCase("NULL")) {
            return (false);
        }

        return (true);
    }

    public boolean checkNonZeroEmpty(StringBuffer checkVal) {
        if (checkVal == null) {
            return (false);
        }
        if (checkVal.toString().equals("")) {
            return (false);
        }
        if (checkVal.toString().equalsIgnoreCase("NULL")) {
            return (false);
        }

        return (true);
    }
}
