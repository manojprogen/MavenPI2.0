/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.db;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author Administrator
 */
public class PbBaseResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"checkReportMessages", "SELECT MESSAGE FROM PR_AR_REPORT_MESSAGE where report_id='&' and user_id='&'"}, {"insertReportMessages", "insert into PR_AR_REPORT_MESSAGE(MSG_ID, USER_ID, REPORT_ID, MESSAGE, CREATED_BY, CREATED_ON, UPDATE_BY, UPDATE_ON) "
            + "  values (PR_AR_REPORT_MESSAGE_SEQ.nextval,'&','&','&','&',sysdate,'&',sysdate)"}, {"updateReportMessages", " update PR_AR_REPORT_MESSAGE set message='&' where report_id='&' and user_id='&'"}, {"indicuslogin", "select * FROM prg_ar_users WHERE PU_LOGIN_ID = '&'and PU_PASSWORD ='&' and ACCOUNT_TYPE =(select ORG_ID from PRG_ORG_MASTER where upper(ORGANISATION_NAME) = '&')"}, {"getFilePath", "select SETUP_CHAR_VALUE from PRG_GBL_SETUP_VALUES where SETUP_KEY='TAGGER_KEY'"}, {"getUserDetails", "select * from PRG_USER_ASSIGNMENTS where USER_ID='&'"}, {"getUserStatus", "select PU_ACTIVE from PRG_AR_USERS where PU_ID='&'"}, {"updateLogOutInformation", "Update Hit_Calc Set Status='N',LOGOUT_DATE =sysdate,LOGOUT_TIME ='&' WHERE SESSION_ID='&' AND user_id=&"}, {"getAdminCount", "SELECT COUNT(*) AS COUNT FROM PRG_USER_ASSIGNMENTS WHERE (admin='Y' OR QUERY_STUDIO='Y' OR POWER_ANALYZER='Y') AND USER_ID=&"}, {"direct_insert_prg_tag_master", "insert into PRG_TAG_MASTER\n"
            + "(tag_id ,\n"
            + "TAG_NAME\n"
            + ",TAG_DESC\n"
            + ", USER_ID\n"
            + ",SEQUENCE_ID\n"
            + ",FONT_SIZE)\n"
            + "select\n"
            + "--use Seq_no tag_id ,\n"
            + "A.tag_id,\n"
            + "A.TAG_NAME\n"
            + ",A.TAG_DESC\n"
            + ", & \n"
            + ",A.SEQUENCE_ID\n"
            + ",A.FONT_SIZE\n"
            + "from\n"
            + "(\n"
            + "SELECT * \n"
            + "FROM PRG_TAG_MASTER where user_id = & ) A \n"
            + "left join\n"
            + "(\n"
            + "SELECT * \n"
            + "FROM PRG_TAG_MASTER where user_id = & ) B \n"
            + "on ( A.tag_name = b.TAG_NAME)\n"
            + "where b.tag_id is null"}, {"direct_insert_prg_tag_report_assignment", "insert into PRG_TAG_REPORT_ASSIGNMENT (TAG_ASSIGN_ID ,      \n"
            + "TAG_ID    ,            \n"
            + "TAG_SHORT_DESC   ,  \n"
            + "TAG_LONG_DESC ,    \n"
            + "REPORT_ID  ,       \n"
            + "USER_ID   ,           \n"
            + "TAG_TYPE  ,         \n"
            + "SEQUENCE_ID)\n"
            + "Select\n"
            + "--use Seq_no TAG_ASSIGN_ID ,\n"
            + "TAG_REPORT_ASSIGNMENT_SEQ.nextval,\n"
            + "b.n_tag_id TAG_ID    ,            \n"
            + "A.TAG_SHORT_DESC   ,  \n"
            + "A.TAG_LONG_DESC ,    \n"
            + "A.REPORT_ID  ,       \n"
            + "& ,           \n"
            + "TAG_TYPE  ,         \n"
            + "SEQUENCE_ID  from PRG_TAG_REPORT_ASSIGNMENT A\n"
            + "inner join (\n"
            + "select distinct\n"
            + "A.tag_id S_tag_id, b.tag_id n_tag_id\n"
            + "from\n"
            + "(\n"
            + "SELECT *\n"
            + "FROM PRG_TAG_MASTER where user_id = & ) A\n"
            + "left join\n"
            + "(\n"
            + "SELECT *\n"
            + "FROM PRG_TAG_MASTER where user_id = & ) B\n"
            + "on ( A.tag_name = b.TAG_NAME)\n"
            + "where b.tag_name is not null)  B\n"
            + "on (b.S_tag_id = a.tag_id) \n"
            + "where A.user_id = &  \n"
            + "and report_id not in (Select A.report_id  from PRG_TAG_REPORT_ASSIGNMENT A \n"
            + "inner join (\n"
            + "select distinct\n"
            + "A.tag_id S_tag_id, b.tag_id n_tag_id \n"
            + "from\n"
            + "(\n"
            + "SELECT *\n"
            + "FROM PRG_TAG_MASTER where user_id = & ) A \n"
            + "left join\n"
            + "(\n"
            + "SELECT *\n"
            + "FROM PRG_TAG_MASTER where user_id = & ) B \n"
            + "on ( A.tag_name = b.TAG_NAME)\n"
            + "where b.tag_name is not null)  B\n"
            + "on (b.n_tag_id = a.tag_id)\n"
            + "where A.user_id = & \n"
            + ")"}
    };
}
