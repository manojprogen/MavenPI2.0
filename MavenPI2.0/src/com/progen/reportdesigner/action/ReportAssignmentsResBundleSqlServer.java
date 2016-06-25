/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportdesigner.action;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author Saurabh
 */
public class ReportAssignmentsResBundleSqlServer extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"addUserFolderAssignment", "INSERT  INTO PRG_GRP_USER_FOLDER_ASSIGNMENT(USER_FOLDER_ID, USER_ID, GRP_ID, START_DATE) values(&,&,&,&)"}, {"deleteUserFolderAssignment", " delete from PRG_GRP_USER_FOLDER_ASSIGNMENT where USER_ID=&"}, {"addUserReports", "INSERT INTO  PRG_AR_USER_REPORTS(USER_ID, REPORT_ID,IS_WHAT_IF_REPORT) values(&,&,'&')"}, {"addReportParents", "INSERT INTO PRG_AR_REPORT_PARENTS( REPORT_ID, PARENT_ID) values(ident_current('PRG_AR_REPORT_MASTER'),&)"}, {"getUserFolderExistance", "SELECT  distinct USER_ID FROM PRG_GRP_USER_FOLDER_ASSIGNMENT where USER_FOLDER_ID in (&)"}, {"getUserFolderusers", "SELECT  distinct USER_ID FROM PRG_GRP_USER_FOLDER_ASSIGNMENT where user_id not in(&) and USER_FOLDER_ID in (&) "}, {"getBusGrpUsers", "SELECT distinct USER_ID FROM PRG_GRP_USER_ASSIGNMENT where user_id not in(&) and grp_id in (&)"}, {"getAllUsers", "SELECT * from prg_ar_users"}, {"deleteFolder", "DELETE  FROM PRG_GRP_USER_FOLDER_ASSIGNMENT where user_folder_id in (&) and user_id=&"}, {"getRprt", "select ar.report_id, pm.report_name from account_report ar,prg_ar_users pu,prg_ar_report_master pm where pu.pu_id= & and "
            + "ar.org_id=pu.account_type and pm.report_id= ar.report_id"}, {"insrtreptqry", "insert into prg_ar_user_reports(USER_ID,REPORT_ID,PUR_FAV_REPORT) values(&,&,'N')"}, {"getRpid", " SELECT REPORT_ID,FOLDER_ID FROM PRG_AR_REPORT_DETAILS where FOLDER_ID in(&)"}, {"getOneviewIdName", "select IDENT_CURRENT('ONEVIEW_NAME_ID') "}
    };
}
