/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.passwordAction;

import java.io.Serializable;
import java.util.ListResourceBundle;

public class passwordResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"checkoldpassword", "select PU_PASSWORD from prg_ar_users where upper(PU_LOGIN_ID)=upper('&')"}, {"updatenewpassword", "update PRG_AR_USERS set PU_PASSWORD='&' where upper(PU_LOGIN_ID)=upper('&')"}, {"getpassword", "select pu_password from prg_ar_users where upper(PU_LOGIN_ID)=upper('&') and PU_EMAIL = '&'"}, {"resetpassword", "update prg_ar_users set pu_password ='&' where pu_id='&'"}, {"verifyrstpswrd", "select user_type_name from prg_user_type where user_type_id in (select user_type from prg_ar_users where pu_id='&')"}, {"checkoldName", "select PU_LOGIN_ID from prg_ar_users where upper(PU_ID)=('&')"}, {"updatenewname", "update PRG_AR_USERS set PU_LOGIN_ID='&' where upper(PU_LOGIN_ID)=upper('&') AND PU_ID=('&')"}
    };
}
