/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.chating;

import java.io.Serializable;
import java.util.ListResourceBundle;

public class progechatResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"reviewMessages", "insert into PRG_AR_REPORT_VIEWER_MSG(MSG_ID,USER_ID,USER_NAME,REPORT_ID, MESSAGES, CREATED_ON, CREATED_BY,UPDATED_ON,UPDATED_BY) values (PRG_AR_REPORT_VIEWER_MSG_SEQ.nextval,&,'&',&,'&',sysdate,&,sysdate,&)"}, {"getreviewMessages", "select USER_NAME,MESSAGES,CREATED_ON from PRG_AR_REPORT_VIEWER_MSG WHERE report_id=& ORDER BY CREATED_ON DESC "}
    };
}
