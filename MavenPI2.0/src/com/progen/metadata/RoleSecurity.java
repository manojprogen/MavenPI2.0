/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.metadata;

import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class RoleSecurity {

    private boolean exClude = false;
    private int securityId;
    private ArrayList<String> memberValues;

    public RoleSecurity(int securityId, ArrayList<String> memberValues) {
        this.memberValues = memberValues;
        this.securityId = securityId;

    }

    public ArrayList<String> getMemberValues() {
        return memberValues;
    }
}
