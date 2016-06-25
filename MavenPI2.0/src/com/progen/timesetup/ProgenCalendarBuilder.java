package com.progen.timesetup;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class ProgenCalendarBuilder {

    public static Logger logger = Logger.getLogger(ProgenCalendarBuilder.class);
    private String connectionID;
    private Calendar startCalendar;
    private Calendar endCalendar;
    private String calenderName;
    private boolean fromStandTimeDim = false;
    TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
    PbDb pbDb = new PbDb();

    public boolean buildCalendar(Calendar startCal, Calendar endCal, String connectionId, String calenderName, boolean fromStandTimeDim) {
        if (fromStandTimeDim == true) {
            this.fromStandTimeDim = fromStandTimeDim;
        }

        this.startCalendar = startCal;
        this.endCalendar = endCal;
        this.connectionID = connectionId;
        this.calenderName = calenderName;
        boolean checkCreateTables = false;
        if (fromStandTimeDim == false) {
            String calenderId = insertCalendarMetaData();
            checkCreateTables = createCalenderTables(calenderId, connectionId);
        } else {
            checkCreateTables = createCalenderTables(connectionId);
        }

        return checkCreateTables;
    }

    public String insertCalendarMetaData() {

        String calenderId = null;
        if (fromStandTimeDim == true) {
            String addCalender = resBundle.getString("addCalender");
            Object obj[] = new Object[7];
            String finalQuery = "";
            try {
                int seqaddCalender = pbDb.getSequenceNumber("select PRG_CALENDER_SETUP_SEQ.nextval from dual");
                obj[0] = seqaddCalender;
                obj[1] = "Enterprise";
                obj[2] = calenderName;
                obj[3] = "PR_DAY_DENOM_" + seqaddCalender;
                obj[4] = startCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + startCalendar.get(Calendar.DATE) + "-" + startCalendar.get(Calendar.YEAR);
                obj[5] = endCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + endCalendar.get(Calendar.DATE) + "-" + endCalendar.get(Calendar.YEAR);
                obj[6] = connectionID;
                finalQuery = pbDb.buildQuery(addCalender, obj);
                // 
                pbDb.execModifySQL(finalQuery);
                calenderId = Integer.toString(seqaddCalender);

            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }
        return calenderId;
    }

    public boolean createCalenderTables(String calenderId, String connectionId) {


        ArrayList createQuerys = new ArrayList();
        String finalQuery = "";
        boolean checkCreateTables = false;
        String createDenomTables = resBundle.getString("createDenomTables");
        //Create Table for YEAR
        Object obj1[] = new Object[2];
        obj1[0] = "PR_YEAR_" + calenderId;
        obj1[1] = "PYEAR NUMBER, 	START_DATE DATE, 	END_DATE DATE, 	CUST_YEAR VARCHAR2(255 BYTE)";
        finalQuery = pbDb.buildQuery(createDenomTables, obj1);
        createQuerys.add(finalQuery);
        //Create Table for YEAR_DENOM
        obj1[0] = "PR_YEAR_DENOM_" + calenderId;
        obj1[1] = "CYEAR NUMBER,C_ST_DATE DATE,C_END_DATE DATE,C_NO_DAYS NUMBER,PYEAR NUMBER,P_ST_DATE DATE,P_END_DATE DATE,P_NO_DAYS NUMBER";
        finalQuery = pbDb.buildQuery(createDenomTables, obj1);
        createQuerys.add(finalQuery);
        //Create Table for Quarter
        obj1[0] = "PR_QUARTER_" + calenderId;
        obj1[1] = "QUARTER_NO NUMBER, DYEAR NUMBER, START_DATE DATE, END_DATE DATE , CUST_NAME VARCHAR2(255 BYTE)";
        finalQuery = pbDb.buildQuery(createDenomTables, obj1);
        createQuerys.add(finalQuery);
        //Create Table for Quarter Denom
        obj1[0] = "PR_QTR_DENOM_" + calenderId;
        obj1[1] = "CQTR NUMBER, CYEAR NUMBER,C_ST_DATE DATE, 	C_END_DATE DATE,	C_NO_DAYS NUMBER, 	PQ_QTR NUMBER,"
                + "PQ_YEAR NUMBER, 	PQ_ST_DATE DATE,	PQ_END_DATE DATE, 	PQ_DAYS NUMBER, 	PY_QTR NUMBER, 	PY_YEAR NUMBER, 	PY_ST_DATE DATE,"
                + "PY_END_DATE DATE, PY_DAYS NUMBER";
        finalQuery = pbDb.buildQuery(createDenomTables, obj1);
        createQuerys.add(finalQuery);
        //Create Table for Month
        obj1[0] = "PR_MONTH_" + calenderId;
        obj1[1] = "MONTH_NO NUMBER, DYEAR NUMBER, START_DATE DATE, END_DATE DATE, MONTH_NAME VARCHAR2(255 BYTE), CUST_NAME VARCHAR2(255 BYTE)";
        finalQuery = pbDb.buildQuery(createDenomTables, obj1);
        createQuerys.add(finalQuery);
        //Create Table for MonthDenom
        obj1[0] = "PR_MONTH_DENOM_" + calenderId;
        obj1[1] = "CMON NUMBER, 	CYEAR NUMBER, 	C_ST_DATE DATE, 	C_END_DATE DATE, 	C_NO_DAYS NUMBER,"
                + "PM_MON NUMBER, 	PM_YEAR NUMBER, 	PM_ST_DATE DATE, 	PM_END_DATE DATE, 	PM_DAYS NUMBER, 	PY_MON NUMBER, 	PY_YEAR NUMBER,"
                + "PY_ST_DATE DATE, 	PY_END_DATE DATE, 	PY_DAYS NUMBER";
        finalQuery = pbDb.buildQuery(createDenomTables, obj1);
        createQuerys.add(finalQuery);
        //create Table for Week
        obj1[0] = "PR_WEEK_" + calenderId;
        obj1[1] = "WEEK_NO NUMBER, DYEAR NUMBER, START_DATE DATE, END_DATE DATE , CUST_NAME VARCHAR2(255 BYTE)";
        finalQuery = pbDb.buildQuery(createDenomTables, obj1);
        createQuerys.add(finalQuery);
        //create Table for Week Denom
        obj1[0] = "PR_WEEK_DENOM_" + calenderId;
        obj1[1] = "CWEEK NUMBER,CYEAR NUMBER, C_ST_DATE DATE,C_END_DATE DATE,C_NO_DAYS NUMBER,PW_WEEK NUMBER,"
                + "PW_YEAR NUMBER,PW_ST_DATE DATE,PW_END_DATE DATE,PY_WEEK NUMBER,PY_YEAR NUMBER,PY_ST_DATE DATE,PY_END_DATE DATE";
        finalQuery = pbDb.buildQuery(createDenomTables, obj1);
        createQuerys.add(finalQuery);
        //Create Table for Day
        obj1[0] = "PR_DAYS_" + calenderId;
        obj1[1] = "DDATE DATE,START_DATE DATE, END_DATE DATE , DAYOFYEAR NUMBER, DAYOFMONTH  NUMBER,WORKING_STATUS VARCHAR2(20 BYTE),DAY_NAME VARCHAR2(20 BYTE),HOLIDAY_REASON VARCHAR2(250 BYTE)";
        finalQuery = pbDb.buildQuery(createDenomTables, obj1);
        createQuerys.add(finalQuery);
        //create Table for Day Denom
        obj1[0] = "PR_DAY_DENOM_" + calenderId;
        obj1[1] = "DDATE DATE,ST_DATE DATE,END_DATE DATE,P_ST_DATE DATE,N_ST_DATE DATE,DAYSOFYEAR NUMBER,PDAYSOFYEAR NUMBER,NDAYSOFYEAR NUMBER,"
                + " CWEEK NUMBER,CWYEAR NUMBER,CW_ST_DATE DATE,CW_END_DATE DATE,CW_ST_MON_DATE DATE,CW_END_MON_DATE DATE, "
                + "CW_CUST_NAME VARCHAR2(100 BYTE),PW_DAY DATE,PW_ST_DATE DATE,PW_END_DATE DATE,PW_ST_MON_DATE DATE, "
                + "PW_END_MON_DATE DATE,PW_CUST_NAME VARCHAR2(100 BYTE),NW_DAY DATE,NW_ST_DATE DATE,NW_END_DATE DATE,"
                + "NW_ST_MON_DATE DATE,NW_END_MON_DATE DATE,NW_CUST_NAME VARCHAR2(100 BYTE),LYW_DAY DATE,LYW_ST_DATE DATE,LYW_END_DATE DATE,"
                + "LYW_ST_MON_DATE DATE,	LYW_END_MON_DATE DATE,LYW_CUST_NAME VARCHAR2(100 BYTE),NYW_DAY DATE,NYW_ST_DATE DATE,NYW_END_DATE DATE,"
                + "NYW_ST_MON_DATE DATE,NYW_END_MON_DATE DATE,NYW_CUST_NAME VARCHAR2(100 BYTE),CMONTH NUMBER,"
                + "CM_YEAR NUMBER,CM_ST_DATE DATE,CM_END_DATE DATE,CM_CUST_NAME VARCHAR2(100 BYTE), "
                + "PM_DAY DATE,PM_ST_DATE DATE,PM_END_DATE DATE,PM_CUST_NAME VARCHAR2(100 BYTE),NM_DAY DATE,NM_ST_DATE DATE,NM_END_DATE DATE,NM_CUST_NAME VARCHAR2(100 BYTE), "
                + "PYM_DAY DATE,PYM_ST_DATE DATE,PYM_END_DATE DATE,PYM_CUST_NAME VARCHAR2(100 BYTE),NYM_DAY DATE,NYM_ST_DATE DATE,NYM_END_DATE DATE,NYM_CUST_NAME VARCHAR2(100 BYTE),"
                + "CQTR NUMBER,CQ_YEAR NUMBER,CQ_ST_DATE DATE,CQ_END_DATE DATE,CQ_CUST_NAME VARCHAR2(100 BYTE),"
                + "LQ_DAY DATE,LQ_ST_DATE DATE,LQ_END_DATE DATE,LQ_CUST_NAME VARCHAR2(100 BYTE),NQ_DAY DATE,NQ_ST_DATE DATE,NQ_END_DATE DATE,NQ_CUST_NAME VARCHAR2(100 BYTE),"
                + "LYQ_DAY DATE,LYQ_ST_DATE DATE,LYQ_END_DATE DATE,LYQ_CUST_NAME VARCHAR2(100 BYTE),NYQ_DAY DATE,NYQ_ST_DATE DATE,NYQ_END_DATE DATE,NYQ_CUST_NAME VARCHAR2(100 BYTE),"
                + "CYEAR NUMBER,CY_ST_DATE DATE,CY_END_DATE DATE,CY_CUST_NAME VARCHAR2(100 BYTE),"
                + "LYEAR NUMBER,LY_DAY DATE,LY_ST_DATE DATE,LY_END_DATE DATE,LY_CUST_NAME VARCHAR2(100 BYTE),NYEAR NUMBER,NY_DAY DATE,NY_ST_DATE DATE,NY_END_DATE DATE,NY_CUST_NAME VARCHAR2(100 BYTE)";
        finalQuery = pbDb.buildQuery(createDenomTables, obj1);
        createQuerys.add(finalQuery);
        //  
        Connection connection = null;
        try {
            connection = ProgenConnection.getInstance().getConnectionByConId(connectionId);
            checkCreateTables = pbDb.executeMultiple(createQuerys, connection);
            connection = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }

            }
        }
        if (checkCreateTables) {
            boolean checkResult = buildCalnder(startCalendar.getTime(), endCalendar.getTime(), calenderId, connectionId);
            if (!checkResult) {
                checkCreateTables = false;
            }
        }
        return checkCreateTables;
    }

    public boolean createCalenderTables(String connectionId) {

        Connection con = null;
        boolean checkCreateTables = false;
        String createDenomTables = resBundle.getString("createDenomTables");
        ArrayList<String> tablenames = new ArrayList<String>();
        ArrayList<String> queryList = new ArrayList<String>();
        int sourceId = Integer.parseInt(connectionId);

        PbReturnObject rsObject = new PbReturnObject();
        //con= new ProgenConnection().getConnection();

        con = ProgenConnection.getInstance().getConnectionByConId(Integer.toString(sourceId));
        String sql = "select tname from tab";
        try {
            rsObject = pbDb.execSelectSQL(sql, con);
            for (int i = 0; i < rsObject.getRowCount(); i++) {

                tablenames.add(rsObject.getFieldValueString(i, 0));
            }
            Object obj1[] = new Object[2];


            if (!tablenames.contains("PR_YEAR")) {
                obj1[0] = "PR_YEAR";
                obj1[1] = "PYEAR NUMBER,START_DATE DATE,END_DATE DATE,CUST_YEAR VARCHAR2(255 BYTE)";
                sql = pbDb.buildQuery(createDenomTables, obj1);
                queryList.add(sql);
            } else {
                sql = "truncate table PR_YEAR";
                queryList.add(sql);
            }

            if (!tablenames.contains("PR_YEAR_DENOM")) {
                obj1[0] = "PR_YEAR_DENOM";
                obj1[1] = "CYEAR NUMBER,C_ST_DATE DATE,C_END_DATE DATE,C_NO_DAYS NUMBER,PYEAR NUMBER,P_ST_DATE DATE,P_END_DATE DATE,P_NO_DAYS NUMBER";
                sql = pbDb.buildQuery(createDenomTables, obj1);
                queryList.add(sql);
            } else {
                sql = "truncate table PR_YEAR_DENOM";
                queryList.add(sql);
            }

            if (!tablenames.contains("PR_QUARTER")) {
                obj1[0] = "PR_QUARTER";
                obj1[1] = "QUARTER_NO NUMBER, DYEAR NUMBER, START_DATE DATE, END_DATE DATE , CUST_NAME VARCHAR2(255 BYTE)";
                sql = pbDb.buildQuery(createDenomTables, obj1);
                queryList.add(sql);
            } else {
                sql = "truncate table PR_QUARTER";
                queryList.add(sql);
            }

            if (!tablenames.contains("PR_QTR_DENOM")) {
                obj1[0] = "PR_QTR_DENOM";
                obj1[1] = "CQTR NUMBER, CYEAR NUMBER,C_ST_DATE DATE, 	C_END_DATE DATE,	C_NO_DAYS NUMBER, 	PQ_QTR NUMBER,"
                        + "PQ_YEAR NUMBER, 	PQ_ST_DATE DATE,	PQ_END_DATE DATE, 	PQ_DAYS NUMBER, 	PY_QTR NUMBER, 	PY_YEAR NUMBER, 	PY_ST_DATE DATE,"
                        + "PY_END_DATE DATE, PY_DAYS NUMBER";
                sql = pbDb.buildQuery(createDenomTables, obj1);
                queryList.add(sql);
            } else {
                sql = "truncate table PR_QTR_DENOM";
                queryList.add(sql);
            }
            if (!tablenames.contains("PR_MONTH")) {
                obj1[0] = "PR_MONTH";
                obj1[1] = "MONTH_NO NUMBER, DYEAR NUMBER, START_DATE DATE, END_DATE DATE, MONTH_NAME VARCHAR2(255 BYTE), CUST_NAME VARCHAR2(255 BYTE)";
                sql = pbDb.buildQuery(createDenomTables, obj1);
                queryList.add(sql);

            } else {
                sql = "truncate table PR_MONTH";
                queryList.add(sql);
            }

            if (!tablenames.contains("PR_MONTH_DENOM")) {
                obj1[0] = "PR_MONTH_DENOM";
                obj1[1] = "CMON NUMBER, 	CYEAR NUMBER, 	C_ST_DATE DATE, 	C_END_DATE DATE, 	C_NO_DAYS NUMBER,"
                        + "PM_MON NUMBER, 	PM_YEAR NUMBER, 	PM_ST_DATE DATE, 	PM_END_DATE DATE, 	PM_DAYS NUMBER, 	PY_MON NUMBER, 	PY_YEAR NUMBER,"
                        + "PY_ST_DATE DATE, 	PY_END_DATE DATE, 	PY_DAYS NUMBER";
                sql = pbDb.buildQuery(createDenomTables, obj1);
                queryList.add(sql);
            } else {
                sql = "truncate table PR_MONTH_DENOM";
                queryList.add(sql);

            }

            if (!tablenames.contains("PR_WEEK")) {
                obj1[0] = "PR_WEEK";
                obj1[1] = "WEEK_NO NUMBER, DYEAR NUMBER, START_DATE DATE, END_DATE DATE , CUST_NAME VARCHAR2(255 BYTE)";
                sql = pbDb.buildQuery(createDenomTables, obj1);
                queryList.add(sql);
            } else {
                sql = "truncate table PR_WEEK";
                queryList.add(sql);
            }

            if (!tablenames.contains("PR_WEEK_DENOM")) {
                obj1[0] = "PR_WEEK_DENOM";
                obj1[1] = "CWEEK NUMBER,CYEAR NUMBER, C_ST_DATE DATE,C_END_DATE DATE,C_NO_DAYS NUMBER,PW_WEEK NUMBER,"
                        + "PW_YEAR NUMBER,PW_ST_DATE DATE,PW_END_DATE DATE,PY_WEEK NUMBER,PY_YEAR NUMBER,PY_ST_DATE DATE,PY_END_DATE DATE";
                sql = pbDb.buildQuery(createDenomTables, obj1);
                queryList.add(sql);
            } else {
                sql = "truncate table PR_WEEK_DENOM";
                queryList.add(sql);
            }


            if (!tablenames.contains("PR_DAYS")) {
                obj1[0] = "PR_DAYS";
                obj1[1] = "DDATE DATE,START_DATE DATE, END_DATE DATE , DAYOFYEAR NUMBER, DAYOFMONTH  NUMBER,WORKING_STATUS VARCHAR2(20 BYTE),DAY_NAME VARCHAR2(20 BYTE),HOLIDAY_REASON VARCHAR2(250 BYTE)";
                sql = pbDb.buildQuery(createDenomTables, obj1);
                queryList.add(sql);
            } else {
                sql = "truncate table PR_DAYS";
                queryList.add(sql);
            }

            if (!tablenames.contains("PR_DAY_DENOM")) {
                obj1[0] = "PR_DAY_DENOM";
                obj1[1] = "DDATE DATE,"
                        + "ST_DATE DATE,"
                        + "END_DATE DATE,"
                        + "P_ST_DATE DATE,"
                        + "N_ST_DATE DATE,"
                        + "DAYSOFYEAR NUMBER,"
                        + "PDAYSOFYEAR NUMBER,"
                        + "NDAYSOFYEAR NUMBER,"
                        + " CWEEK NUMBER,"
                        + "CWYEAR NUMBER,"
                        + "CW_ST_DATE DATE,"
                        + "CW_END_DATE DATE,"
                        + "CW_ST_MON_DATE DATE,"
                        + "CW_END_MON_DATE DATE, "
                        + "CW_CUST_NAME VARCHAR2(100 BYTE),PW_DAY DATE,PW_ST_DATE DATE,PW_END_DATE DATE,PW_ST_MON_DATE DATE, "
                        + "PW_END_MON_DATE DATE,PW_CUST_NAME VARCHAR2(100 BYTE),NW_DAY DATE,NW_ST_DATE DATE,NW_END_DATE DATE,"
                        + "NW_ST_MON_DATE DATE,NW_END_MON_DATE DATE,NW_CUST_NAME VARCHAR2(100 BYTE),LYW_DAY DATE,LYW_ST_DATE DATE,LYW_END_DATE DATE,"
                        + "LYW_ST_MON_DATE DATE,	LYW_END_MON_DATE DATE,LYW_CUST_NAME VARCHAR2(100 BYTE),NYW_DAY DATE,NYW_ST_DATE DATE,NYW_END_DATE DATE,"
                        + "NYW_ST_MON_DATE DATE,NYW_END_MON_DATE DATE,NYW_CUST_NAME VARCHAR2(100 BYTE),CMONTH NUMBER,"
                        + "CM_YEAR NUMBER,CM_ST_DATE DATE,CM_END_DATE DATE,CM_CUST_NAME VARCHAR2(100 BYTE), "
                        + "PM_DAY DATE,PM_ST_DATE DATE,PM_END_DATE DATE,PM_CUST_NAME VARCHAR2(100 BYTE),NM_DAY DATE,NM_ST_DATE DATE,NM_END_DATE DATE,NM_CUST_NAME VARCHAR2(100 BYTE), "
                        + "PYM_DAY DATE,PYM_ST_DATE DATE,PYM_END_DATE DATE,PYM_CUST_NAME VARCHAR2(100 BYTE),NYM_DAY DATE,NYM_ST_DATE DATE,NYM_END_DATE DATE,NYM_CUST_NAME VARCHAR2(100 BYTE),"
                        + "CQTR NUMBER,CQ_YEAR NUMBER,CQ_ST_DATE DATE,CQ_END_DATE DATE,CQ_CUST_NAME VARCHAR2(100 BYTE),"
                        + "LQ_DAY DATE,LQ_ST_DATE DATE,LQ_END_DATE DATE,LQ_CUST_NAME VARCHAR2(100 BYTE),NQ_DAY DATE,NQ_ST_DATE DATE,NQ_END_DATE DATE,NQ_CUST_NAME VARCHAR2(100 BYTE),"
                        + "LYQ_DAY DATE,LYQ_ST_DATE DATE,LYQ_END_DATE DATE,LYQ_CUST_NAME VARCHAR2(100 BYTE),NYQ_DAY DATE,NYQ_ST_DATE DATE,NYQ_END_DATE DATE,NYQ_CUST_NAME VARCHAR2(100 BYTE),"
                        + "CYEAR NUMBER,CY_ST_DATE DATE,CY_END_DATE DATE,CY_CUST_NAME VARCHAR2(100 BYTE),"
                        + "LYEAR NUMBER,LY_DAY DATE,LY_ST_DATE DATE,LY_END_DATE DATE,LY_CUST_NAME VARCHAR2(100 BYTE),NYEAR NUMBER,NY_DAY DATE,NY_ST_DATE DATE,NY_END_DATE DATE,NY_CUST_NAME VARCHAR2(100 BYTE)";
                sql = pbDb.buildQuery(createDenomTables, obj1);
                queryList.add(sql);

            } else {
                sql = "truncate table PR_DAY_DENOM";
                queryList.add(sql);
            }

            con = ProgenConnection.getInstance().getConnectionByConId(Integer.toString(sourceId));
            checkCreateTables = pbDb.executeMultiple(queryList, con);
            con = null;


        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }

            }
        }
        if (checkCreateTables) {
            boolean checkResult = buildCalnder(startCalendar.getTime(), endCalendar.getTime(), null, connectionId);
            if (!checkResult) {
                checkCreateTables = false;
            }
        }
        return checkCreateTables;

    }

    public boolean buildCalnder(Date startDate, Date endDate, String calendarId, String connectionId) {
        boolean result = false;
        CalendarBuilder yearBuilder = new YearBuilder();
        CalendarBuilder quaterBuilder = new QuarterBuilder();
        CalendarBuilder monthbulider = new MonthBulider();
        CalendarBuilder weekbuilder = new WeekBuilder();
        CalendarBuilder daybuilder = new DayBuilder();
//        yearBuilder.buildCalendarData(startDate, endDate);
//        monthbulider.buildCalendarData(startDate, endDate);
//        quaterBuilder.buildCalendarData(startDate, endDate);
//        weekbuilder.buildCalendarData(startDate, endDate);
//        daybuilder.buildCalendarData(startDate, endDate);
        yearBuilder.setNext(quaterBuilder).setNext(monthbulider).setNext(weekbuilder).setNext(daybuilder);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        for (int i = startCalendar.get((Calendar.YEAR)); i < endCalendar.get(Calendar.YEAR); i++) {
            Calendar tempCalendar = Calendar.getInstance();
            tempCalendar = (Calendar) startCalendar.clone();
            tempCalendar.add(Calendar.DATE, -1);
            tempCalendar.set(Calendar.YEAR, i + 1);
            startDate = startCalendar.getTime();
            endDate = tempCalendar.getTime();


            ArrayList<String> queryList = yearBuilder.generateCalendarData(startDate, endDate, calendarId);
//        

            Connection connection = null;

            try {
                connection = ProgenConnection.getInstance().getConnectionByConId(connectionId);
                Statement statement = connection.createStatement();
                for (String string : queryList) {
                    //

                    statement.addBatch(string);
                }
                int count[] = statement.executeBatch();
                connection.close();
                connection = null;
                if (count.length > 1) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            } finally {

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        logger.error("Exception:", ex);
                    }
                }
            }
            startCalendar.set(Calendar.YEAR, i + 1);
        }
        return result;
    }

    public long getTotalNumberOfDays(Calendar calendar1, Calendar calendar2) {

        long milis1 = calendar1.getTimeInMillis();
        long milis2 = calendar2.getTimeInMillis();
        long diff = milis2 - milis1;

        return (diff / (24 * 60 * 60 * 1000)) + 1;
    }
}
