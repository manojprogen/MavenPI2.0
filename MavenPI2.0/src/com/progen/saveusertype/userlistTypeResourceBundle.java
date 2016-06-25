/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.saveusertype;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author Saurabh
 */
class userlistTypeResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"saveUsertype", "insert into prg_user_type(USER_TYPE_ID,USER_TYPE_NAME,USER_TYPE_DESC) values(USER_TYPE_SEQ.nextval,'&','&')"},};
}
