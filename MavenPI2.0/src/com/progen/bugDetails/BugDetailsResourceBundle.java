/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.bugDetails;

import java.io.Serializable;
import java.util.ListResourceBundle;

class BugDetailsResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getbugstatus", "select DISTINCT STATUS_ID,STATUS_NAME from BUG_STATUS"},
        {"bugPriority", "select DISTINCT PRIORITY_NAME,BUG_PRIORITY_ID from bug_priority  "},
        {"savebugdetails", "INSERT INTO BUG_MASTER(BUG_ID,PI_ID,CUSTOMER_ID,CUST_USER_ID,USER_ID,SUBJECT,BUG_PRIORITY_ID,BUG_STATUS_ID,LOG_DATE,UPDATED_DATE,CREATED_BY,UPDATED_BY,PRODUCT_ID)"
            + "VALUES(&,(select DISTINCT TEXT_VERSION_ID from PI_VERSION where VERSION_NAME ='&'),(select DISTINCT CUSTOMER_ID from CUSTOMER_MASTER where CUSTOMER_NAME ='&'),"
            + "'','','&',&,&,sysdate, sysdate,'','','&')"},
        {"getDetailsList", " select subject, log_date, created_by, updated_by, updated_date, (select CUSTOMER_NAME FROM CUSTOMER_MASTER WHERE customer_id =1), "
            + "( select VERSION_NAME FROM PI_VERSION WHERE TEXT_VERSION_ID =pi_id),bug_id FROM bug_master"},
        {"bugDetailslist1", "INSERT INTO BUG_DETAILS (bug_det_id,BUG_DESC, bug_id,created_date) VALUES(BUG_DETAILS_SEQ.nextval,'&',&,sysdate)"},
        {"bugeditdetails", "select distinct bm.bug_id,bm.subject,bm.customer_id, bm.updated_date, bm.updated_by,bm.pi_id,bm.bug_status_id,bm.bug_priority_id,cm.customer_name,bp.priority_name,bs.status_name,pv.version_name from bug_master bm,"
            + "bug_details bd,customer_master cm,bug_priority bp, bug_status bs, pi_version pv where bm.bug_id=& and"
            + " bm.customer_id=cm.customer_id and bm.bug_priority_id=bp.bug_priority_id and bm.bug_status_id=bs.status_id"},
        {"saveEditBugdetails", "UPDATE BUG_MASTER SET CUSTOMER_ID=(select CUSTOMER_ID from CUSTOMER_MASTER WHERE CUSTOMER_NAME ='&'),"
            + "PI_ID=(select TEXT_VERSION_ID FROM pi_version where VERSION_NAME='&'),BUG_STATUS_ID   =&,BUG_PRIORITY_ID =&,"
            + "SUBJECT= '&'WHERE BUG_ID=&"}, {"saveEditBugdetails1", "INSERT INTO BUG_DETAILS (bug_det_id,BUG_DESC, bug_id,created_date) VALUES(BUG_DETAILS_SEQ.nextval,'&',&,sysdate)"}, {"deleteBugdetails", "delete FROM BUG_MASTER WHERE BUG_ID =&"}, {"saveUserDetails", "insert into BUG_USER_DETAILS (USER_ID,TEXT_USER_ID,COMPANY_NAME,COMPANY_ID,EMAIL_ID) values (BUG_USER_DETAILS_SEQ.nextval,'&',(select CUSTOMER_NAME FROM customer_master WHERE  CUSTOMER_ID=&),&,'&')"}, {"saveAssignDetails", "update BUG_MASTER SET ASSIGNED_USERS = '&' where BUG_ID  in (&)"}, {"saveAssignDetails1", "update BUG_DETAILS SET ASSIGNED_USERS = '&' where BUG_ID  in (&)"}, {"getBugDetailsList", ""}
    };
}
