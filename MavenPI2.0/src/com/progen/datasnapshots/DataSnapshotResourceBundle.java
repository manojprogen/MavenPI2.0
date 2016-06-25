/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.datasnapshots;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author Administrator
 */
public class DataSnapshotResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getDataSnapShotId", "select PRG_AR_DATA_SNAPSHOTS_S.NEXTVAL NEXTVAL FROM DUAL"},
        {"insertDataSnapShots", "insert into PRG_AR_DATA_SNAPSHOTS(DATA_SNAPSHOT_ID,DATA_SNAPSHOT_NAME,REPORT_ID,REFRESH_INTERVAL,CREATED_BY,UPDATED_BY,DATA_HTML_VIEW,CREATE_DATE,UPDATED_DATE,LAST_REFRESHED,HTML_REPORT_TYPE) values(&,'&',&,&,&,&,'&',SYSDATE,SYSDATE,SYSDATE,'&')"},
        {"updateDataSnapShots", "update PRG_AR_DATA_SNAPSHOTS set DATA_SNAPSHOT_NAME='&',REPORT_ID=&,LAST_REFRESHED=&,REFRESH_INTERVAL=&,DATA_SNAPSHOT='&',DATA_HTML_VIEW='&',CREATED_BY&,CREATED_DATE=&,UPDATED_BY=&,UPDATED_DATE=& where DATA_SNAPSHOT_ID= "},
        {"deleteDataSnapShots", "delete from PRG_AR_DATA_SNAPSHOTS where DATA_SNAPSHOT_ID=&"},
        {"getAllSnapShots", "select DATA_SNAPSHOT_NAME,REPORT_ID,LAST_REFRESHED,CREATE_DATE from PRG_AR_DATA_SNAPSHOTS"},
        {"deleteSnapShots", "delete from PRG_AR_DATA_SNAPSHOTS where DATA_SNAPSHOT_ID in(&)"},
        {"getHtmlViewClob", "select DATA_HTML_VIEW from PRG_AR_DATA_SNAPSHOTS where DATA_SNAPSHOT_ID=& for update"},
        {"updateHtmlView", "update PRG_AR_DATA_SNAPSHOTS set DATA_HTML_VIEW=?,LAST_REFRESHED=SYSDATE WHERE DATA_SNAPSHOT_ID=?"},
        {"getSysdate", "select SYSDATE from dual"},
        {"openDataSnapshotHtml", "select DATA_SNAPSHOT_ID, DATA_SNAPSHOT_NAME, REPORT_ID, LAST_REFRESHED,REFRESH_INTERVAL, DATA_HTML_VIEW FROM PRG_AR_DATA_SNAPSHOTS WHERE DATA_SNAPSHOT_ID=&"},
        {"updateHtmlView1", "update PRG_AR_DATA_SNAPSHOTS set DATA_HTML_VIEW='na',LAST_REFRESHED=SYSDATE WHERE DATA_SNAPSHOT_ID=&"},
        {"getHeaderTitle", "select SETUP_CHAR_VALUE from PRG_GBL_SETUP_VALUES where SETUP_KEY='COMPANY_NAME'"},
        {"updateAdvSnapshotFileName", "update PRG_AR_DATA_SNAPSHOTS set ADVANCED_HTML_FILE_NAME='&'  where DATA_SNAPSHOT_ID=&"},
        {"getAdvSnapshotFileName", "select ADVANCED_HTML_FILE_NAME from PRG_AR_DATA_SNAPSHOTS where DATA_SNAPSHOT_ID=&"},
        {"insertHeadlines", "insert into PRG_AR_HEADLINES(HEADLINE_ID,HEADLINE_DATA,HEADLINE_NAME,REPORT_ID,DATA_STATUS) values(&,'&','&',&,'&')"},
        {"getHeadlineId", "select PRG_AR_HEADLINES_SEQ.NEXTVAL NEXTVAL FROM DUAL"},
        {"updateHeadlineView1", "update PRG_AR_HEADLINES set HEADLINE_DATA='na' WHERE HEADLINE_ID=&"},
        {"getHeadlineClob", "select HEADLINE_DATA from PRG_AR_HEADLINES where HEADLINE_ID=& for update"},
        {"updateHeadlineView", "update PRG_AR_HEADLINES set HEADLINE_DATA=? WHERE HEADLINE_ID=?"},
        {"getHeadlines", "SELECT  H.HEADLINE_ID,H.HEADLINE_DATA,H.HEADLINE_NAME,H.REPORT_ID From PRG_AR_HEADLINES H,PRG_HEADLINES_ASSIGNMENT P where H.HEADLINE_ID=P.HEADLINE_ID AND P.USER_ID=&"},
        {"getHeadlineData", "select HEADLINE_DATA from PRG_AR_HEADLINES where HEADLINE_ID=&"},
        {"getHeadlineName", "select HEADLINE_NAME from PRG_AR_HEADLINES where HEADLINE_ID=&"},
        {"deleteHeadline", "delete from PRG_AR_HEADLINES where HEADLINE_ID=&"},
        {"getEmailHeadlines", "select HEADLINE_ID,HEADLINE_NAME from PRG_AR_HEADLINES where HEADLINE_ID in(&)"},
        {"getDataStatus", "select DATA_STATUS from PRG_AR_HEADLINES where HEADLINE_ID='&'"},
        {"updateMailData", "update PRG_AR_HEADLINES set HEADLINE_MAIL_DATA='&' WHERE HEADLINE_ID='&'"},
        {"getAssignId", "select PRG_HEADLINES_ASSIGNMENT_SEQ.NEXTVAL NEXTVAL FROM DUAL"},
        {"insertHeadlinesAssignment", "insert into PRG_HEADLINES_ASSIGNMENT(ASSIGN_ID,HEADLINE_ID,USER_ID) values(&,&,&)"},
        {"getHeadlinesbasedonUser", "select a.HEADLINE_ID,a.HEADLINE_DATA,a.HEADLINE_NAME,a.REPORT_ID from PRG_AR_HEADLINES a,PRG_HEADLINES_ASSIGNMENT b where a.HEADLINE_ID=b.HEADLINE_ID and b.user_id=&"},
        {"getCheckedHeadlines", "select HEADLINE_ID,HEADLINE_DATA,HEADLINE_NAME,REPORT_ID From PRG_AR_HEADLINES where HEADLINE_ID in(&)"},
        {"getDynamicHeadlines", "select a.PRG_PERSONALIZED_ID,a.PRG_REPORT_ID,a.PRG_REPORT_CUST_NAME,SNAP_URL from PRG_AR_PERSONALIZED_REPORTS a,PRG_HEADLINES_ASSIGNMENT b where a.MODULE_TYPE='dynamicHeadline' and a.PRG_PERSONALIZED_ID=b.HEADLINE_ID and b.user_id=&"}
    };
}
