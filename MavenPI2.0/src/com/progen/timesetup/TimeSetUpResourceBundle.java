/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.timesetup;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author Saurabh
 */
public class TimeSetUpResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"updateTimeSetUp", "update PRG_CALENDER_SETUP set CALENDER_NAME='&' where CALENDER_ID=&"}, {"getCalList", "SELECT CALENDER_ID,  CALENDER_NAME FROM PRG_CALENDER_SETUP   where connection_id in (&) order by CALENDER_ID "}, {"addCalender", "INSERT INTO PRG_CALENDER_SETUP(CALENDER_ID, CALENDER_TYPE, CALENDER_NAME, DENOM_TABLE,START_DATE,END_DATE,CONNECTION_ID) values(&,'&','&','&',TO_DATE('&','mm-dd-yyyy'),TO_DATE('&','mm-dd-yyyy'),&)"}, {"updatePrYear", "update PR_YEAR_& set CUST_YEAR='&' where rowid='&'"}, {"updatePrQuarter", "update PR_QUARTER_& set CUST_NAME='&' where rowid='&'"}, {"updatePrMonth", "update PR_MONTH_& set CUST_NAME='&' where rowid='&'"}, {"updatePrWeek", "update PR_WEEK_& set CUST_NAME='&' where rowid='&'"}, {"updatestandPrYear", "update PR_YEAR set CUST_YEAR='&' where rowid='&'"}, {"updatestandPrQuarter", "update PR_QUARTER set CUST_NAME='&' where rowid='&'"}, {"updatestandPrMonth", "update PR_MONTH set CUST_NAME='&' where rowid='&'"}, {"updatestandPrWeek", "update PR_WEEK set CUST_NAME='&' where rowid='&'"}, {"createDenomTables", " CREATE TABLE & (&) "}, {"addPrYear", "INSERT INTO PR_YEAR_&(PYEAR, START_DATE, END_DATE, CUST_YEAR)  values(&,'&','&','&')"}, {"addPrYear1", "INSERT INTO PR_YEAR &(PYEAR, START_DATE, END_DATE, CUST_YEAR)  values(&,'&','&','&')"}, {"addPrQtr", "INSERT INTO PR_QUARTER_&(QUARTER_NO, DYEAR, START_DATE, END_DATE, CUST_NAME)  values(&,&,'&','&','&')"}, {"addPrQtr1", "INSERT INTO PR_QUARTER &(QUARTER_NO, DYEAR, START_DATE, END_DATE, CUST_NAME)  values(&,&,'&','&','&')"}, {"addPrMonth", "INSERT INTO PR_MONTH_&(MONTH_NO, DYEAR, START_DATE, END_DATE, MONTH_NAME, CUST_NAME)  values(&,&,'&','&','&','&')"}, {"addPrMonth1", "INSERT INTO PR_MONTH &(MONTH_NO, DYEAR, START_DATE, END_DATE, MONTH_NAME, CUST_NAME)  values(&,&,'&','&','&','&')"}, {"addPrWeek", "INSERT INTO PR_WEEK_&(WEEK_NO, DYEAR, START_DATE, END_DATE, CUST_NAME)  values(&,&,'&','&','&')"}, {"addPrWeek1", "INSERT INTO PR_WEEK &(WEEK_NO, DYEAR, START_DATE, END_DATE, CUST_NAME)  values(&,&,'&','&','&')"}, {"addPrDay", "INSERT INTO PR_DAYS_&(DDATE, START_DATE, END_DATE, DAYOFYEAR, DAYOFMONTH,WORKING_STATUS,DAY_NAME)  values('&','&','&',&,&,'&','&')"}, {"addPrDay1", "INSERT INTO PR_DAYS &(DDATE, START_DATE, END_DATE, DAYOFYEAR, DAYOFMONTH,WORKING_STATUS,DAY_NAME)  values('&','&','&',&,&,'&','&')"}, {"addPrMonDenom", "INSERT INTO PR_MONTH_DENOM_&(	CMON, 	CYEAR, 	C_ST_DATE, 	C_END_DATE, 	C_NO_DAYS,"
            + "PM_MON, 	PM_YEAR, 	PM_ST_DATE, 	PM_END_DATE, 	PM_DAYS, 	PY_MON, 	PY_YEAR,"
            + "PY_ST_DATE, 	PY_END_DATE, 	PY_DAYS) values(&,&,'&','&',&,&,&,'&','&',&,&,&,'&','&',&)"}, {"addPrQtrDenom", "Insert into PR_QTR_DENOM_&(CQTR, CYEAR, C_ST_DATE, C_END_DATE, C_NO_DAYS, PQ_QTR, PQ_YEAR, PQ_ST_DATE, PQ_END_DATE, PQ_DAYS,"
            + " PY_QTR, PY_YEAR, PY_ST_DATE, PY_END_DATE, PY_DAYS) values(&,&,'&','&',&,'&','&','&','&',&,&,&,'&','&',&)"}, {"addPrQtrDenom1", "Insert into PR_QTR_DENOM &(CQTR, CYEAR, C_ST_DATE, C_END_DATE, C_NO_DAYS, PQ_QTR, PQ_YEAR, PQ_ST_DATE, PQ_END_DATE, PQ_DAYS,"
            + " PY_QTR, PY_YEAR, PY_ST_DATE, PY_END_DATE, PY_DAYS) values(&,&,'&','&',&,'&','&','&','&',&,&,&,'&','&',&)"}, {"addPrYeardenom", "INSERT INTO PR_YEAR_DENOM_& (CYEAR, C_ST_DATE, C_END_DATE, C_NO_DAYS, PYEAR, P_ST_DATE, P_END_DATE, P_NO_DAYS)values(&,'&','&',&,&,'&','&',&)"}, {"addPrYeardenom1", "INSERT INTO PR_YEAR_DENOM &(CYEAR, C_ST_DATE, C_END_DATE, C_NO_DAYS, PYEAR, P_ST_DATE, P_END_DATE, P_NO_DAYS)values(&,'&','&',&,&,'&','&',&)"}, {"addPrWeekDenom", "INSERT INTO PR_WEEK_DENOM_&(CWEEK, CYEAR, C_ST_DATE, C_END_DATE, C_NO_DAYS, PW_WEEK, PW_YEAR,"
            + "PW_ST_DATE, PW_END_DATE, PY_WEEK, PY_YEAR, PY_ST_DATE, PY_END_DATE) values(&,&,'&','&',&,&,&,'&','&',&,&,'&','&')"}, {"addPrWeekDenom1", "INSERT INTO PR_WEEK_DENOM &(CWEEK, CYEAR, C_ST_DATE, C_END_DATE, C_NO_DAYS, PW_WEEK, PW_YEAR,"
            + "PW_ST_DATE, PW_END_DATE, PY_WEEK, PY_YEAR, PY_ST_DATE, PY_END_DATE) values(&,&,'&','&',&,&,&,'&','&',&,&,'&','&')"}, {"addPrMonthDenom", "INSERT INTO PR_MONTH_DENOM_& (CMON, CYEAR, C_ST_DATE, C_END_DATE, C_NO_DAYS, PM_MON, PM_YEAR, PM_ST_DATE, PM_END_DATE, PM_DAYS, PY_MON,"
            + " PY_YEAR, PY_ST_DATE, PY_END_DATE, PY_DAYS) values(&,&,'&','&',&,&,&,'&','&',&,&,&,'&','&',&)"}, {"addPrMonthDenom1", "INSERT INTO PR_MONTH_DENOM & (CMON, CYEAR, C_ST_DATE, C_END_DATE, C_NO_DAYS, PM_MON, PM_YEAR, PM_ST_DATE, PM_END_DATE, PM_DAYS, PY_MON,"
            + " PY_YEAR, PY_ST_DATE, PY_END_DATE, PY_DAYS) values(&,&,'&','&',&,&,&,'&','&',&,&,&,'&','&',&)"}, {"addPrDayDenom", "INSERT INTO PR_DAY_DENOM_&(DDATE, ST_DATE, END_DATE, DAYSOFYEAR, CWEEK, CWYEAR, CW_ST_DATE,CW_ST_MON_DATE,CW_END_DATE, "
            + " CW_END_MON_DATE, CW_CUST_NAME, PW_DAY, PW_ST_DATE, PW_END_DATE, PW_ST_MON_DATE, PW_END_MON_DATE,"
            + " PW_CUST_NAME, LYW_DAY, LYW_ST_DATE, LYW_END_DATE, LYW_ST_MON_DATE, LYW_END_MON_DATE, LYW_CUST_NAME,"
            + " CMONTH, CM_YEAR, CM_ST_DATE, CM_END_DATE, CM_CUST_NAME, PM_DAY, PM_ST_DATE, PM_END_DATE, PM_CUST_NAME,"
            + " PYM_DAY, PYM_ST_DATE, PYM_END_DATE, PYM_CUST_NAME, CQTR, CQ_YEAR, CQ_ST_DATE, CQ_END_DATE, CQ_CUST_NAME,"
            + " LQ_DAY, LQ_ST_DATE, LQ_END_DATE, LQ_CUST_NAME, LYQ_DAY, LYQ_ST_DATE, LYQ_END_DATE, LYQ_CUST_NAME, CYEAR, CY_ST_DATE,"
            + " CY_END_DATE, CY_CUST_NAME,LYEAR,LY_DAY, LY_ST_DATE, LY_END_DATE, LY_CUST_NAME,P_ST_DATE,PDAYSOFYEAR,N_ST_DATE,NDAYSOFYEAR,NW_DAY,NW_ST_DATE,NW_END_DATE,"
            + "NW_ST_MON_DATE,NW_END_MON_DATE,NW_CUST_NAME,NYW_DAY,NYW_ST_DATE,NYW_END_DATE,NYW_ST_MON_DATE,NYW_END_MON_DATE,NYW_CUST_NAME,"
            + "NM_DAY,NM_ST_DATE,NM_END_DATE,NM_CUST_NAME,NYM_DAY,NYM_ST_DATE,NYM_END_DATE,NYM_CUST_NAME,NQ_DAY,NQ_ST_DATE,NQ_END_DATE,NQ_CUST_NAME,"
            + "NYQ_DAY,NYQ_ST_DATE,NYQ_END_DATE,NYQ_CUST_NAME,NYEAR,NY_DAY,NY_ST_DATE,NY_END_DATE,NY_CUST_NAME) values('&','&','&',&,&,&,'&','&','&'  ,"
            + " '&','&','&','&','&','&','&','&','&','&','&','&','&','&',&,&,'&','&','&','&','&','&','&','&','&','&','&',&,&,'&','&','&','&','&','&','&','&','&','&','&',"
            + " &,'&','&','&','&','&','&','&','&','&',"
            + "'&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&')"}, {"addPrDayDenom1", "INSERT INTO PR_DAY_DENOM &(DDATE, ST_DATE, END_DATE, DAYSOFYEAR, CWEEK, CWYEAR, CW_ST_DATE,CW_ST_MON_DATE,CW_END_DATE, "
            + " CW_END_MON_DATE, CW_CUST_NAME, PW_DAY, PW_ST_DATE, PW_END_DATE, PW_ST_MON_DATE, PW_END_MON_DATE,"
            + " PW_CUST_NAME, LYW_DAY, LYW_ST_DATE, LYW_END_DATE, LYW_ST_MON_DATE, LYW_END_MON_DATE, LYW_CUST_NAME,"
            + " CMONTH, CM_YEAR, CM_ST_DATE, CM_END_DATE, CM_CUST_NAME, PM_DAY, PM_ST_DATE, PM_END_DATE, PM_CUST_NAME,"
            + " PYM_DAY, PYM_ST_DATE, PYM_END_DATE, PYM_CUST_NAME, CQTR, CQ_YEAR, CQ_ST_DATE, CQ_END_DATE, CQ_CUST_NAME,"
            + " LQ_DAY, LQ_ST_DATE, LQ_END_DATE, LQ_CUST_NAME, LYQ_DAY, LYQ_ST_DATE, LYQ_END_DATE, LYQ_CUST_NAME, CYEAR, CY_ST_DATE,"
            + " CY_END_DATE, CY_CUST_NAME,LYEAR,LY_DAY, LY_ST_DATE, LY_END_DATE, LY_CUST_NAME,P_ST_DATE,PDAYSOFYEAR,N_ST_DATE,NDAYSOFYEAR,NW_DAY,NW_ST_DATE,NW_END_DATE,"
            + "NW_ST_MON_DATE,NW_END_MON_DATE,NW_CUST_NAME,NYW_DAY,NYW_ST_DATE,NYW_END_DATE,NYW_ST_MON_DATE,NYW_END_MON_DATE,NYW_CUST_NAME,"
            + "NM_DAY,NM_ST_DATE,NM_END_DATE,NM_CUST_NAME,NYM_DAY,NYM_ST_DATE,NYM_END_DATE,NYM_CUST_NAME,NQ_DAY,NQ_ST_DATE,NQ_END_DATE,NQ_CUST_NAME,"
            + "NYQ_DAY,NYQ_ST_DATE,NYQ_END_DATE,NYQ_CUST_NAME,NYEAR,NY_DAY,NY_ST_DATE,NY_END_DATE,NY_CUST_NAME) values('&','&','&',&,&,&,'&','&','&'  ,"
            + " '&','&','&','&','&','&','&','&','&','&','&','&','&','&',&,&,'&','&','&','&','&','&','&','&','&','&','&',&,&,'&','&','&','&','&','&','&','&','&','&','&',"
            + " &,'&','&','&','&','&','&','&','&','&',"
            + "'&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&')"}, {"getCalenderNames", "SELECT CALENDER_ID, CALENDER_NAME FROM PRG_CALENDER_SETUP where CONNECTION_ID=&"} //added by praveen.kumar on 29/12/2009
        , {"updatewekholidays", "update & set working_status='L',HOLIDAY_REASON='WeekHoliday' WHERE day_name='&'"}, {"updateyerholidays", "update & set holiday_reason='&' WHERE ddate = '&'"}, {"updateexceptholidays", "update & set working_status='&',holiday_reason='&' where ddate='&'"}
    };
}
