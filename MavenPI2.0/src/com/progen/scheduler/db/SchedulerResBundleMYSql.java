/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler.db;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author Administrator
 */

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author progen
 */
public class SchedulerResBundleMYSql extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"addReportSchedule", "insert into prg_report_scheduler(frequency,scheduler_time, mail_id, view_by, content_type,created_by,"
            + "updated_by,created_date,updated_date,START_DATE,END_DATE,report_id,SCHEDULER_NAME,PARAM_DETAIL,IS_AUTOSPLIT,VIEWBY_ID,FOLDER_ID,"
            + "PARTICULAR_DAY,CHECKED_VIEWBY,CHECKED_VIEWBY_NAME,FROM_REPORT,data_selection,data_multivalues) "
            + "values('&','&','&','&','&',&,&,getdate(),getdate(),convert(datetime,'&',120),convert(datetime,'&',120),&,'&','&','&','&','&','&','&','&','&','&','&')"},
        {"getDimMembers", "SELECT GRP_ID, FOLDER_ID, FOLDER_NAME, SUB_FOLDER_ID, SUB_FOLDER_NAME, SUB_FOLDER_TYPE, SUB_FOLDER_TAB_ID, IS_DIMENSION, IS_FACT, IS_BUCKET,"
            + " DISP_NAME," + " QRY_DIM_ID, DIM_ID, DIM_TAB_ID, DIM_NAME, ELEMENT_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,"
            + " USER_COL_NAME, USER_COL_DESC, USER_COL_TYPE, REF_ELEMENT_ID, REF_ELEMENT_TYPE, MEMBER_ID, MEMBER_NAME, DENOM_QUERY, BUSS_TABLE_NAME, "
            + "CONNECTION_ID, AGGREGATION_TYPE, ACTUAL_COL_FORMULA, USE_REPORT_FLAG, "
            + "REFFERED_ELEMENTS,DISPLAY_FORMULA,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME FROM PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID='&'"},
        {"getTrackerDetails", "SELECT a.TRACKER_NAME,a.FREQUENCY,a.SCHEDULED_TIME,a.START_DATE,a.END_DATE,a.FOLDER_ID,a.TRACKER_MODE,a.SUBSCRIBERS,"
            + "a.MEASURE_ID,a.VIEWBY_ID,a.CREATED_BY,b.USER_COL_DESC,a.SUPP_MEASURES,a.OPERATOR,a.FILTER_VALUE,a.IS_AUTO_IDENTIFY,a.SUPP_MEASURES_NAMES,a.IS_SENDANYWAY FROM PRG_TRACKER_MASTER a,PRG_USER_ALL_INFO_DETAILS b WHERE TRACKER_ID=& "
            + "AND a.MEASURE_ID=b.ELEMENT_ID"},
        {"getDimensionName", "select USER_COL_DESC from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID=& "},
        {"getTrckConditionDetails", "select VIEWBY_VALUE,CONDITION,MEASURE_START_VALUE,MEASURE_END_VALUE,MAIL_IDS,IS_SENDANYNWAY,TARGET_VALUE,DEVIATION_PER_VALUE,TRACKER_FLAG from PRG_TRACKER_CONDITIONS where TRACKER_ID=&"},
        {"deleteTrackerDetails", "delete from prg_tracker_master where tracker_id=&"},
        {"deleteTrackerConditions", "delete from prg_tracker_conditions where tracker_id=&"},
        {"addTrackerSchedule", "insert into PRG_TRACKER_MASTER(TRACKER_NAME,START_DATE,END_DATE,FREQUENCY,SCHEDULED_TIME,MEASURE_ID,"
            + "VIEWBY_ID,TRACKER_MODE,SUBSCRIBERS,FOLDER_ID,CREATION_DATE,CREATED_BY,UPDATION_DATE,UPDATED_BY,OPERATOR,FILTER_VALUE,SUPP_MEASURES,IS_AUTO_IDENTIFY,SUPP_MEASURES_NAMES,IS_SENDANYWAY,FROM_REPORT) values('&',convert(datetime,'&',120),convert(datetime,'&',120),'&','&','&'"
            + ",'&','&','&','&',getDate(),&,getDate(),&,'&',&,'&','&','&','&','&')"},
        {"addTrackerCondition", "insert into PRG_TRACKER_CONDITIONS(TRACKER_ID,VIEWBY_VALUE,CONDITION,MEASURE_START_VALUE,MEASURE_END_VALUE,MAIL_IDS,IS_SENDANYNWAY,TARGET_VALUE,DEVIATION_PER_VALUE,TRACKER_FLAG) values(ident_current('PRG_TRACKER_MASTER'),'&','&',&,&,'&','&',&,&,'&') "},
        //        {"addTrackerCondition","insert into PRG_TRACKER_CONDITIONS(TRACKER_ID,VIEWBY_VALUE,MAIL_IDS) values(ident_current('PRG_TRACKER_MASTER'),'&','&') "},
        {"getReportName", "select report_id,report_name from prg_ar_report_master where report_type='R' order by REPORT_NAME asc"},
        {"deleteReportDetails", "delete from PRG_REPORT_SCHEDULER where REPORT_SCHEDULE_ID=& "},
        {"getDimIdForReport", "SELECT a.DISP_NAME,a.ELEMENT_ID,b.IS_ROW_EDGE from prg_user_all_info_details a,prg_ar_report_view_by_master b where  a.ELEMENT_ID=b.DEFAULT_VALUE AND b.REPORT_ID=& order by view_by_seq"},
        // {"getDimIdForReport1","SELECT a.DISP_NAME,  a.ELEMENT_ID ,  b.is_row_edge FROM prg_user_all_info_details a,  prg_ar_report_view_by_master b WHERE b.DEFAULT_VALUE='TIME'AND b.REPORT_ID   =& ORDER BY view_by_seq"},
        {"getDimIdForReport1", "SELECT  b.DEFAULT_VALUE FROM   prg_ar_report_view_by_master b WHERE b.DEFAULT_VALUE='TIME' and b.REPORT_ID=& ORDER BY view_by_seq"},
        {"addReptSchdPreferences", "insert into PRG_REP_SCHD_PREFERENCES(SCHEDULER_ID,DIM_VALS,MAIL_IDS,DIM_ID) values(ident_current('PRG_REPORT_SCHEDULER'),'&','&','&')"},
        {"getSchdPreferenceDetails", "select DIM_VALS,MAIL_IDS,DIM_ID from PRG_REP_SCHD_PREFERENCES where SCHEDULER_ID=& "},
        {"getReportParams", "select * from prg_ar_report_param_details where REPORT_ID=&"},
        {"getReportMeasures", "select * from PRG_AR_QUERY_DETAIL where REPORT_ID=&"},
        {"getSchedulerId", "select LAST_INSERT_ID('PRG_PERSONALIZED_ID') from PRG_REPORT_SCHEDULER order by 1 desc limit 1"},
        {"addReptSchdSlices", "INSERT INTO PRG_REP_SCHD_SLICES(SCHEDULER_ID,SCHEDULER_PREFER_ID,ROW_VIEWBY_ID,ROW_VIEWBY_VAL)VALUES("
            + "ident_current('PRG_REPORT_SCHEDULER'),ident_current('PRG_REP_SCHD_PREFERENCES'),'&','&')"},
        {"getSchdSliceDetails", "select * from PRG_REP_SCHD_SLICES where SCHEDULER_ID=&"},
        {"deleteSchdPrfrDetails", "delete from PRG_REP_SCHD_PREFERENCES where SCHEDULER_ID in(&)"},
        {"deleteSchdSliceDetails", "delete from PRG_REP_SCHD_SLICES where SCHEDULER_ID in(&)"},
        {"getTrackerId", "select ident_current('PRG_TRACKER_MASTER')"},
        {"getReportScheduleDetails", "select REPORT_ID,FREQUENCY,SCHEDULER_TIME,MAIL_ID,VIEW_BY,CREATED_BY,CONTENT_TYPE,START_DATE,END_DATE,SCHEDULER_NAME,PARAM_DETAIL,"
            + "IS_AUTOSPLIT,VIEWBY_ID,FOLDER_ID,PARTICULAR_DAY,CHECKED_VIEWBY,CHECKED_VIEWBY_NAME,DATA_SELECTION,DATA_MULTIVALUES FROM PRG_REPORT_SCHEDULER WHERE REPORT_SCHEDULE_ID=&"},
        {"getUserDetails", "select * from prg_dim_user_mapping where dim_id=&"},
        {"getScoreCards", "select SCARD_ID ,SCARD_NAME FROM PRG_AR_SCARD_MASTER WHERE SCARD_ID IN (SELECT SCARD_ID FROM PRG_AR_SCARD_USERS WHERE USER_ID=&) AND FOLDER_ID=& ORDER BY SCARD_NAME ASC"},
        {"insertScorecardTracker", "insert into PRG_AR_SCORECARD_TRACKER"
            + "( SCORECARD_ID,SCHEDULER_NAME,CREATED_DATE ,"
            + "CREATED_BY ,UPDATED_DATE ,UPDATED_BY ,FREQUENCY ,SCHEDULER_TIME ,START_DATE ,END_DATE ,FOLDER_ID ,PARTICULAR_DAY,AS_OF_DATE,COMPARE_WITH,PERIOD) "
            + "  values(&,'&',&,&,&,&,'&','&','&','&','&','&','&','&','&')"},
        {"insertScardRulesQry", "insert into PRG_AR_SCARD_TRACKER_RULES(SCORECARD_SCHEDULE_ID,OPERATOR ,START_VALUE ,END_VALUE ,EMAIL_ID ,PHONE_NO ,CONTENT_TYPE ,SEND_TYPE ) "
            + " values(&,'&',&,&,'&',&,'&','&')"},
        {"editScorecardTracker", "SELECT  SCORECARD_ID , SCHEDULER_NAME, CREATED_DATE, CREATED_BY, UPDATED_DATE, UPDATED_BY, FREQUENCY, SCHEDULER_TIME, START_DATE, END_DATE, FOLDER_ID, PARTICULAR_DAY"
            + " FROM PRG_AR_SCORECARD_TRACKER WHERE scorecard_schedule_id=&"},
        {"editScorecardTrackerRules", "SELECT OPERATOR , START_VALUE, END_VALUE, EMAIL_ID, PHONE_NO, CONTENT_TYPE, SEND_TYPE  FROM PRG_AR_SCARD_TRACKER_RULES WHERE SCORECARD_SCHEDULE_ID = &"},
        {"deleteScorecardTracker", "delete from PRG_AR_SCORECARD_TRACKER where SCORECARD_SCHEDULE_ID=&"},
        {"deleteScorecardTrackerRules", "delete from PRG_AR_SCARD_TRACKER_RULES where SCORECARD_SCHEDULE_ID=&"}
    };
}