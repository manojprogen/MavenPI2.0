/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler.db;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author progen
 */
public class SchedulerResBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"addReportSchedule", "insert into prg_report_scheduler(REPORT_SCHEDULE_ID,frequency,scheduler_time, mail_id, view_by, content_type,created_by,"
            + "updated_by,created_date,updated_date,START_DATE,END_DATE,report_id,SCHEDULER_NAME,PARAM_DETAIL,IS_AUTOSPLIT,VIEWBY_ID,FOLDER_ID,"
            + "PARTICULAR_DAY,CHECKED_VIEWBY,CHECKED_VIEWBY_NAME,FROM_REPORT,data_selection,data_multivalues) "
            + "values(PRG_REPORT_SCHEDULER_SEQ.nextval,'&','&','&','&','&',&,&,sysdate,sysdate,to_date('&','dd-mm-yyyy'),to_date('&','dd-mm-yyyy'),&,'&','&','&','&','&','&','&','&','&','&','&')"},
        {"getDimMembers", "SELECT GRP_ID, FOLDER_ID, FOLDER_NAME, SUB_FOLDER_ID, SUB_FOLDER_NAME, SUB_FOLDER_TYPE, SUB_FOLDER_TAB_ID, IS_DIMENSION, IS_FACT, IS_BUCKET,"
            + " DISP_NAME," + " QRY_DIM_ID, DIM_ID, DIM_TAB_ID, DIM_NAME, ELEMENT_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,"
            + " USER_COL_NAME, USER_COL_DESC, USER_COL_TYPE, REF_ELEMENT_ID, REF_ELEMENT_TYPE, MEMBER_ID, MEMBER_NAME, DENOM_QUERY, BUSS_TABLE_NAME, "
            + "CONNECTION_ID, AGGREGATION_TYPE, ACTUAL_COL_FORMULA, USE_REPORT_FLAG, "
            + "REFFERED_ELEMENTS,DISPLAY_FORMULA,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME FROM PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID='&'"},
        {"addTrackerSchedule", "insert into PRG_TRACKER_MASTER(TRACKER_ID,TRACKER_NAME,START_DATE,END_DATE,FREQUENCY,SCHEDULED_TIME,MEASURE_ID,"
            + "VIEWBY_ID,TRACKER_MODE,SUBSCRIBERS,FOLDER_ID,CREATION_DATE,CREATED_BY,UPDATION_DATE,UPDATED_BY,OPERATOR,FILTER_VALUE,SUPP_MEASURES,IS_AUTO_IDENTIFY,SUPP_MEASURES_NAMES,IS_SENDANYWAY, FROM_REPORT) values(PRG_TRACKER_MASTER_SEQ.nextval,'&',to_date('&','dd-mm-yyyy'),to_date('&','dd-mm-yyyy'),'&','&','&'"
            + ",'&','&','&','&',sysdate,&,sysdate,&,'&',&,'&','&','&','&','&')"},
        //        {"addTrackerCondition","insert into PRG_TRACKER_CONDITIONS(TRACKER_ID,VIEWBY_VALUE,MAIL_IDS) values(PRG_TRACKER_MASTER_SEQ.currval,'&','&') "},
        {"addTrackerCondition", "insert into PRG_TRACKER_CONDITIONS(TRACKER_ID,VIEWBY_VALUE,CONDITION,MEASURE_START_VALUE,MEASURE_END_VALUE,MAIL_IDS,IS_SENDANYNWAY,TARGET_VALUE,DEVIATION_PER_VALUE,TRACKER_FLAG) values(PRG_TRACKER_MASTER_SEQ.currval,'&','&',&,&,'&','&',&,&,'&') "},
        {"getTrackerDetails", "SELECT a.TRACKER_NAME,a.FREQUENCY,a.SCHEDULED_TIME,a.START_DATE,a.END_DATE,a.FOLDER_ID,a.TRACKER_MODE,a.SUBSCRIBERS,"
            + "a.MEASURE_ID,a.VIEWBY_ID,a.CREATED_BY,b.USER_COL_DESC,a.SUPP_MEASURES,a.OPERATOR,a.FILTER_VALUE,a.IS_AUTO_IDENTIFY,a.SUPP_MEASURES_NAMES,a.IS_SENDANYWAY FROM PRG_TRACKER_MASTER a,PRG_USER_ALL_INFO_DETAILS b WHERE TRACKER_ID=& "
            + "AND a.MEASURE_ID=b.ELEMENT_ID"},
        {"getDimensionName", "select USER_COL_DESC from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID=& "},
        {"getTrckConditionDetails", "select VIEWBY_VALUE,CONDITION,MEASURE_START_VALUE,MEASURE_END_VALUE,MAIL_IDS,IS_SENDANYNWAY,TARGET_VALUE,DEVIATION_PER_VALUE,TRACKER_FLAG from PRG_TRACKER_CONDITIONS where TRACKER_ID=&"},
        {"deleteTrackerDetails", "delete from prg_tracker_master where tracker_id in(&)"},
        {"deleteTrackerConditions", "delete from prg_tracker_conditions where tracker_id in(&)"},
        {"getReportName", "select report_id,report_name from prg_ar_report_master where report_type='R' order by REPORT_NAME asc"},
        {"getReportScheduleDetails", "select REPORT_ID,FREQUENCY,SCHEDULER_TIME,MAIL_ID,VIEW_BY,CREATED_BY,CONTENT_TYPE,START_DATE,END_DATE,SCHEDULER_NAME,PARAM_DETAIL,IS_AUTOSPLIT,VIEWBY_ID,FOLDER_ID,PARTICULAR_DAY,CHECKED_VIEWBY,CHECKED_VIEWBY_NAME,DATA_SELECTION,DATA_MULTIVALUES FROM PRG_REPORT_SCHEDULER WHERE REPORT_SCHEDULE_ID=&"},
        {"deleteReportDetails", "delete from PRG_REPORT_SCHEDULER where REPORT_SCHEDULE_ID in(&) "},
        {"getDimIdForReport", "SELECT a.DISP_NAME,a.ELEMENT_ID ,b.is_row_edge from prg_user_all_info_details a,prg_ar_report_view_by_master b where  a.ELEMENT_ID=b.DEFAULT_VALUE AND b.REPORT_ID=& order by view_by_seq"},
        //{"getDimIdForReport1","SELECT a.DISP_NAME,  a.ELEMENT_ID ,  b.is_row_edge FROM prg_user_all_info_details a,  prg_ar_report_view_by_master b WHERE b.DEFAULT_VALUE='TIME'AND b.REPORT_ID   =& ORDER BY view_by_seq"},
        {"getDimIdForReport1", "SELECT  b.DEFAULT_VALUE FROM   prg_ar_report_view_by_master b WHERE b.DEFAULT_VALUE='TIME' and b.REPORT_ID=& ORDER BY view_by_seq"},
        {"addReptSchdPreferences", "insert into PRG_REP_SCHD_PREFERENCES(REP_SCHD_PREFER_ID,SCHEDULER_ID,DIM_VALS,MAIL_IDS,DIM_ID) values(PRG_REP_SCHD_PREFERENCES_SEQ.nextval,PRG_REPORT_SCHEDULER_SEQ.currval,'&','&','&')"},
        {"getSchdPreferenceDetails", "select DIM_VALS,MAIL_IDS,DIM_ID from PRG_REP_SCHD_PREFERENCES where SCHEDULER_ID=& "},
        {"getReportParams", "select * from prg_ar_report_param_details where REPORT_ID=&"},
        {"getReportMeasures", "select * from PRG_AR_QUERY_DETAIL where REPORT_ID=&"},
        {"getSchedulerId", "select PRG_REPORT_SCHEDULER_SEQ.currval from dual"},
        {"addReptSchdSlices", "INSERT INTO PRG_REP_SCHD_SLICES(REP_SCHD_SLICE_ID,SCHEDULER_ID,SCHEDULER_PREFER_ID,ROW_VIEWBY_ID,ROW_VIEWBY_VAL)VALUES(PRG_REP_SCHD_SLICES_SEQ.nextval,"
            + "PRG_REPORT_SCHEDULER_SEQ.currval,PRG_REP_SCHD_PREFERENCES_SEQ.currval,'&','&')"},
        {"getSchdSliceDetails", "select * from PRG_REP_SCHD_SLICES where SCHEDULER_ID=&"},
        {"deleteSchdPrfrDetails", "delete from PRG_REP_SCHD_PREFERENCES where SCHEDULER_ID in(&)"},
        {"deleteSchdSliceDetails", "delete from PRG_REP_SCHD_SLICES where SCHEDULER_ID in(&)"}, {"getUserDetails", "select * from prg_dim_user_mapping where dim_id=&"}, {"getTrackerId", "select PRG_TRACKER_MASTER_SEQ.currval from dual"},
        {"getScoreCards", "select SCARD_ID ,SCARD_NAME FROM PRG_AR_SCARD_MASTER WHERE SCARD_ID IN (SELECT SCARD_ID FROM PRG_AR_SCARD_USERS WHERE USER_ID=&) AND FOLDER_ID=& ORDER BY SCARD_NAME ASC"},
        {"PRG_AR_SCORECARD_TRACKER_SEQ", "select PRG_AR_SCORECARD_TRACKER_SEQ.nextval from dual"},
        {"insertScorecardTracker", "insert into PRG_AR_SCORECARD_TRACKER"
            + "(SCORECARD_SCHEDULE_ID, SCORECARD_ID,SCHEDULER_NAME,CREATED_DATE ,"
            + "CREATED_BY ,UPDATED_DATE ,UPDATED_BY ,FREQUENCY ,SCHEDULER_TIME ,START_DATE ,END_DATE ,FOLDER_ID ,PARTICULAR_DAY,AS_OF_DATE,COMPARE_WITH,PERIOD) "
            + "  values(&,&,'&',&,&,&,&,'&','&','&','&','&','&','&','&','&')"},
        {"insertScardRulesQry", "insert into PRG_AR_SCARD_TRACKER_RULES(SCORECARD_SCHEDULE_ID,OPERATOR ,START_VALUE ,END_VALUE ,EMAIL_ID ,PHONE_NO ,CONTENT_TYPE ,SEND_TYPE ) "
            + " values(&,'&',&,&,'&',&,'&','&')"},
        {"editScorecardTracker", "SELECT  SCORECARD_ID , SCHEDULER_NAME, CREATED_DATE, CREATED_BY, UPDATED_DATE, UPDATED_BY, FREQUENCY, SCHEDULER_TIME, START_DATE, END_DATE, FOLDER_ID, PARTICULAR_DAY,AS_OF_DATE,COMPARE_WITH,PERIOD"
            + " FROM PRG_AR_SCORECARD_TRACKER WHERE scorecard_schedule_id=&"},
        {"editScorecardTrackerRules", "SELECT OPERATOR , START_VALUE, END_VALUE, EMAIL_ID, PHONE_NO, CONTENT_TYPE, SEND_TYPE  FROM PRG_AR_SCARD_TRACKER_RULES WHERE SCORECARD_SCHEDULE_ID = &"},
        {"deleteScorecardTracker", "delete from PRG_AR_SCORECARD_TRACKER where SCORECARD_SCHEDULE_ID in (&)"},
        {"deleteScorecardTrackerRules", "delete from PRG_AR_SCARD_TRACKER_RULES where SCORECARD_SCHEDULE_ID in (&)"}
    };
}