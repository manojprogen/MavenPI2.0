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
public class MemberSecurity {

    private boolean exClude = false;
    private int securityId;
    private int userId;
    private ArrayList<String> memberValues;

    public MemberSecurity(ArrayList<String> memberValues, int securityId, int userId) {
        this.memberValues = memberValues;
        this.securityId = securityId;
        this.userId = userId;

    }

    public ArrayList<String> getMemberValues() {
        return memberValues;
    }

    public int getUserId() {
        return userId;
    }
}
