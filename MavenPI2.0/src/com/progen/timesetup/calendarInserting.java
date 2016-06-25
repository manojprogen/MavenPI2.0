package com.progen.timesetup;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import utils.db.ProgenConnection;

public class calendarInserting {

    public static Logger logger = Logger.getLogger(calendarInserting.class);
    private String ConnectionId;
    private int noOfdaysinYear;

    public static void main(String args[]) {
        try {
            calendarInserting clmthds = new calendarInserting();
//            TimeSetUpResourceBundle tmsetup = new TimeSetUpResourceBundle();
            String strtdate, enddate;
            strtdate = "4-1-2004";//"4-1-2005";
            enddate = "3-31-2011";//"3-31-2006";
            Calendar cal = Calendar.getInstance();
            cal.set(2004, 3, 1);
            int totyrdays = cal.getActualMaximum(cal.DAY_OF_YEAR);
        if (totyrdays == 366) {
            }
            int numofdays = clmthds.gettotalnumofDays("1-3-2004", "31-2-2011");//("1-3-2005", "31-2-2006");
           strtdate = strtdate.replace('-', '/');
            enddate = enddate.replace('-', '/');
//            clmthds.insertcal(strtdate, enddate, 109);       //date format is mm/dd/yyyy
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }
    TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
    PbDb pbdb = new PbDb();
    ArrayList list = new ArrayList();

    public void insertcal(String startyear, String endyear, int calenderId, String connnID) throws Exception {

        this.ConnectionId = connnID;
        Calendar calender1 = Calendar.getInstance();
        Calendar calender2 = Calendar.getInstance();



        String finalQuery = "";
        String createtable = "";
        ArrayList Createlist = new ArrayList();
        String createDenomTables = resBundle.getString("createDenomTables");
        Object obj1[] = new Object[2];
        int totaldaysofyer = 0;
        String yearstrt = startyear;
        String yearends = endyear;
        String yrstrtarr[];
        yrstrtarr = yearstrt.split("/");
        int day = Integer.parseInt(yrstrtarr[1].toString());
        int mnth = Integer.parseInt(yrstrtarr[0].toString());
        int yer = Integer.parseInt(yrstrtarr[2].toString());

        boolean isLeapYear = chkLeapyear(yer);

        if (isLeapYear == true) {
            if (mnth >= 3) {
                totaldaysofyer = 365;
            } else if (mnth == 2 && day >= 29) {
                totaldaysofyer = 365;
            } else {
                totaldaysofyer = 366;
            }
        }

        String yrendarr[];
        yrendarr = yearends.split("/");
        int endday = Integer.parseInt(yrendarr[1].toString());
        int endmnth = Integer.parseInt(yrendarr[0].toString());
        int endyer = Integer.parseInt(yrendarr[2].toString());
        endmnth = endmnth - 1;

        boolean isendLeapYear = chkLeapyear(endyer);

        if (isendLeapYear == true) {
            if (endmnth >= 3) {
                totaldaysofyer = 365;
            } else if (endmnth == 2 && endday >= 29) {
                totaldaysofyer = 365;
            } else {
                totaldaysofyer = 366;
            }
        }

        if (isendLeapYear == false && isLeapYear == false) {
            totaldaysofyer = 365;
        }

        String[] monthName = {"JAN", "FEB",
            "MAR", "APR", "MAY", "JUN", "JUL",
            "AUG", "SEP", "OCT", "NOV",
            "DEC"};
        mnth = mnth - 1;
        String month = monthName[mnth];
        String endmonth = monthName[endmnth];
        calender1.set(yer, mnth, day);
        calender2.set(endyer, endmnth, endday);
        noOfdaysinYear = (int) getTotalNumberOfDays(calender1, calender2);
        String[] multyrs = multipleyears(yer, mnth, day, endyer, endmnth, endday);
        for (int i = 0; i < multyrs.length; i++) {
            String divyers = multyrs[i];
            startyear = divyers.substring(0, divyers.indexOf("@"));
            endyear = divyers.substring(divyers.indexOf("@") + 1, divyers.length());
            ////////////////////////.println("considering\t" + startyear);
            ////////////////////////.println("consideringendyear\t" + endyear);

            yrstrtarr = startyear.split("-");
            day = Integer.parseInt(yrstrtarr[0].toString());
            mnth = monthNumber(yrstrtarr[1].toString());
            yer = Integer.parseInt(yrstrtarr[2].toString());
            mnth = mnth - 1;

            yrendarr = endyear.split("-");
            endday = Integer.parseInt(yrendarr[0].toString());
            endmnth = monthNumber(yrendarr[1].toString());
            endyer = Integer.parseInt(yrendarr[2].toString());
            endmnth = endmnth - 1;
            if (i == 0) {
                createtable = "yes";
            } else {
                createtable = "no";
            }

            //upto Here is  the code for daydenom for inner methods of prdaydenom

            //addPrYear(yer, yrstrtarr[1].toString(), day, calenderId, endyer, yrendarr[1].toString(), endday, createtable);
            String forexec = "execute";
            // addPrQtr(yer, (mnth + 1), day, calenderId, endyer, endmnth, endday, yrstrtarr[1].toString(), createtable, forexec);
            //addPrMonth(yer, (mnth + 1), day, calenderId, endyer, endmnth, endday, monthName(mnth + 1), createtable, forexec);
            // addPrWeek(calenderId, yer, mnth, day, endyer, endmnth, endday, month, createtable);
            // buildCalender(yer, (mnth + 1), day, endyer, endmnth, endday, monthName, calenderId, createtable);
            //addPrYearDenom(yer, (mnth + 1), day, calenderId, endyer, endmnth, endday, month, endmonth, createtable);
            // addQrterDenom(yer, (mnth + 1), day, calenderId, endyer, endmnth, endday, month, createtable);
            addPrDayDenom(yer, mnth, day, calenderId, endyer, endmnth, endday, month, endmonth, createtable);
            //  addPrMonthDenom(yer, mnth, day, calenderId, endyer, endmnth, endday, month, createtable);
            // addPrWeekDenom(yer, mnth, day, calenderId, endyer, endmnth, endday, month, endmonth, createtable);
        }


    }

    public void buildCalender(int yer, int mnth, int day, int endyer, int endmnth, int endday, String[] monthName, int calenderId, String createtable) {
        int j = 0;
        int count = 0;
        int nxtmnt = 0;
        int check = 0;
        int forcount = 11;
        String finalQuery = "";
        Calendar cal = Calendar.getInstance();
        ArrayList Createlist = new ArrayList();
        String createDenomTables = resBundle.getString("createDenomTables");
        Object obj1[] = new Object[2];
        if (createtable.equalsIgnoreCase("yes")) {
            obj1[0] = "PR_DAYS_" + calenderId;
            obj1[1] = "DDATE DATE,START_DATE DATE, END_DATE DATE , DAYOFYEAR NUMBER, DAYOFMONTH  NUMBER,WORKING_STATUS VARCHAR2(20 BYTE),DAY_NAME VARCHAR2(20 BYTE),HOLIDAY_REASON VARCHAR2(250 BYTE)";
            finalQuery = pbdb.buildQuery(createDenomTables, obj1);
//            ////////////////////////.println("PR DAY finalQuery---" + finalQuery);
            Createlist.add(finalQuery);
        }
        String addPrDay = resBundle.getString("addPrDay");
        String dayname = null;
        for (j = mnth; j <= forcount; j++) {

            int days = totaldays(yer, j + 1, day);
            for (int i = day; i <= days; i++) {
                count++;
                dayname = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                //code to insert date prdays
                cal.set(yer, j, i);
                obj1 = new Object[8];
                obj1[0] = calenderId;
                obj1[1] = i + "-" + monthName[j] + "-" + yer;
                obj1[2] = i + "-" + monthName[j] + "-" + yer;
                obj1[3] = i + "-" + monthName[j] + "-" + yer;
                obj1[4] = count;
                obj1[5] = i;
                if (dayname.equalsIgnoreCase("Sunday")) {
                    obj1[6] = "L";
                } else {
                    obj1[6] = "W";
                }
                obj1[7] = dayname;
                finalQuery = pbdb.buildQuery(addPrDay, obj1);
//                ////////////////////////.println("PR DAY finalQuery---" + finalQuery);
                Createlist.add(finalQuery);
                //////////////////////////.println("Dates Are---" + i + "-" + monthName[j] + "-" + yer);
                if (yer == endyer && j == endmnth && i == endday) {
                    break;
                }
                if (i == days) {
                    day = 1;
                    days = totaldays(yer, j + 1, day);


                    if (j == monthName.length - 1) {
                        mnth = 0;

                        if (check == 0) {
                            j = -1;
                            check = 1;
                            forcount = endmnth;

                            nxtmnt = 1;
//                           //////////////////////// ////////////////////////.println("mnt" + mnth);
                            yer = yer + 1;
                            days = totaldays(yer, mnth + 1, day);
                        }
                    }
                }
            }
        }
        Connection connection = null;
        try {
            connection = (Connection) ProgenConnection.getInstance().getConnectionByConId(ConnectionId);
            boolean check1 = pbdb.executeMultiple(Createlist, connection);

            connection = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        //       //////////////////////// ////////////////////////.println("count is" + count);
    }

    public void addPrYear(int year, String month, int day, int calenderId, int endyear, String endMonth, int endDay, String createtable) {
        //////////////////////// ////////////////////////.println("--------------------------------------------------------");
        Connection connection = (Connection) ProgenConnection.getInstance().getConnectionByConId(ConnectionId);
        try {
            String finalQuery = "";
            ArrayList Createlist = new ArrayList();
            if (createtable.equalsIgnoreCase("yes")) {
                String createDenomTables = resBundle.getString("createDenomTables");
                Object obj1[] = new Object[2];
                obj1[0] = "PR_YEAR_" + calenderId;
                obj1[1] = "PYEAR NUMBER, 	START_DATE DATE, 	END_DATE DATE, 	CUST_YEAR VARCHAR2(255 BYTE)";
                finalQuery = pbdb.buildQuery(createDenomTables, obj1);
                //////////////////////// ////////////////////////.println("create table PR YEAR finalQuery---\n" + finalQuery);
                Createlist.add(finalQuery);
            }
            String addPrYear = resBundle.getString("addPrYear");
            Object[] obj1 = new Object[5];
            obj1[0] = calenderId;
            obj1[1] = year;
            obj1[2] = day + "-" + month + "-" + year;
            obj1[3] = endDay + "-" + endMonth + "-" + endyear;
            obj1[4] = year;
            finalQuery = pbdb.buildQuery(addPrYear, obj1);
//            ////////////////////////.println("PR YEAR finalQuery---" + finalQuery);
            Createlist.add(finalQuery);

            pbdb.executeMultiple(Createlist, connection);
            // connection.close();
            connection = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public String[] addPrQtr(int yer, int month, int day, int calenderId, int endyear, int endMonth, int endDay, String monthName, String flag, String execute) {
        //////////////////////// ////////////////////////.println("--------------------------------------------------------");
        Connection connection = null;
        String QtrList[] = new String[4];
        try {
            String finalQuery = "";
            ArrayList Createlist = new ArrayList();
            String createDenomTables = resBundle.getString("createDenomTables");

            Object obj1[] = new Object[2];
            if (flag.equalsIgnoreCase("yes")) {
                obj1[0] = "PR_QUARTER_" + calenderId;
                obj1[1] = "QUARTER_NO NUMBER, DYEAR NUMBER, START_DATE DATE, END_DATE DATE , CUST_NAME VARCHAR2(255 BYTE)";
                finalQuery = pbdb.buildQuery(createDenomTables, obj1);
                //////////////////////// ////////////////////////.println("create table" + finalQuery);
                Createlist.add(finalQuery);
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(yer, month, day);
            String strdate = day + "-" + monthName + "-" + yer;
            String endDate = "";
            String dbyear = String.valueOf(yer);
            int extradays = totaldays(yer, (month + 1), day) - day;

            String addPrQtr = resBundle.getString("addPrQtr");
            obj1 = new Object[6];
            obj1[0] = calenderId;

//            String qtrdets = "";
            StringBuilder qtrdets=new StringBuilder();
            for (int i = 1; i <= 4; i++) {

                obj1[1] = i;
                calendar.add(Calendar.MONTH, 3);
                obj1[2] = dbyear;
                obj1[3] = strdate;
                qtrdets.append(i).append(",").append(dbyear).append(",").append(strdate);
                String sendstartdate = strdate;
                endDate = calendar.get(5) + "-" + calendar.getDisplayName(2, Calendar.SHORT, Locale.getDefault()) + "-" + calendar.get(1);

                calendar.add(Calendar.DATE, 1);
                strdate = (calendar.get(5)) + "-" + calendar.getDisplayName(2, Calendar.SHORT, Locale.getDefault()) + "-" + calendar.get(1);
                dbyear = String.valueOf(calendar.get(1));
                calendar.add(Calendar.DATE, -1);

                int totday = gettotalnumofDays(sendstartdate, endDate);
                obj1[4] = endDate;
                obj1[5] = "Q-" + i + "-" + dbyear;
                qtrdets.append(",").append(endDate).append(",").append(totday);
                QtrList[i - 1] = qtrdets.toString();

                finalQuery = pbdb.buildQuery(addPrQtr, obj1);

                Createlist.add(finalQuery);
            }

            if (execute.equalsIgnoreCase("execute")) {
                connection = (Connection) ProgenConnection.getInstance().getConnectionByConId(ConnectionId);
                pbdb.executeMultiple(Createlist, connection);

                connection = null;
            }
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

        return QtrList;
    }

    public String[] addPrWeek(int calenderId, int year, int month, int day, int endyer, int endmnth, int endday, String monthName, String createtable) {

        String weekdets[] = null;
        Connection connection = null;
        try {


            String strDate = "";
            String endDate = "";
            int totaldaysofyer = 0;
            String finalQuery = "";
            ArrayList Createlist = new ArrayList();
            Object obj1[] = new Object[2];
            if (createtable.equalsIgnoreCase("yes")) {
                String createDenomTables = resBundle.getString("createDenomTables");
                obj1[0] = "PR_WEEK_" + calenderId;
                obj1[1] = "WEEK_NO NUMBER, DYEAR NUMBER, START_DATE DATE, END_DATE DATE , CUST_NAME VARCHAR2(255 BYTE)";
                finalQuery = pbdb.buildQuery(createDenomTables, obj1);
                Createlist.add(finalQuery);
            }

            String addPrWeek = resBundle.getString("addPrWeek");


            String dets2[] = getpriorweeks(day, month, year, endday, endmnth, endyer, "No");



            obj1 = new Object[6];
            for (int i = 0; i < dets2.length; i++) {

                String dets1[] = dets2[i].split(",");
                obj1[0] = calenderId;
                obj1[1] = dets1[0];
                obj1[2] = dets1[1];
                obj1[3] = dets1[2];
                obj1[4] = dets1[3];
                obj1[5] = "W-" + dets1[0] + "-" + dets1[1];
                finalQuery = pbdb.buildQuery(addPrWeek, obj1);
                Createlist.add(finalQuery);

            }
            connection = (Connection) ProgenConnection.getInstance().getConnectionByConId(ConnectionId);
            pbdb.executeMultiple(Createlist, connection);

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
        return weekdets;
    }

    public ArrayList addPrMonth(int yer, int mnth, int day, int calenderId, int endday, int endmnth, int endyer, String mnthName, String flag, String execute) throws Exception {

        String finalQuery = "";
        Connection connection = null;
        ArrayList Createlist = new ArrayList();
        ArrayList prmonthlist = new ArrayList();
        try {

            String createDenomTables = resBundle.getString("createDenomTables");
            Object obj1[] = new Object[2];
            if (flag.equalsIgnoreCase("yes")) {
                obj1[0] = "PR_MONTH_" + calenderId;
                obj1[1] = "MONTH_NO NUMBER, DYEAR NUMBER, START_DATE DATE, END_DATE DATE, MONTH_NAME VARCHAR2(255 BYTE), CUST_NAME VARCHAR2(255 BYTE)";
                finalQuery = pbdb.buildQuery(createDenomTables, obj1);

                Createlist.add(finalQuery);
            }
            String addPrMonth = resBundle.getString("addPrMonth");
            String strDate = day + "-" + mnthName + "-" + yer;
            String dbyear = String.valueOf(yer);
            String dbcustName = String.valueOf(yer);
            String monthName = mnthName;
            int monthex = mnth;
            int yearex = yer;
            int dayex = day;
            Calendar calendar = Calendar.getInstance();
            obj1 = new Object[7];
            int mnthCount = 0;
            for (int i = mnth; i <= 12 + monthex - 1; i++) {
                String prmonthdetails[] = new String[3];
                obj1[0] = calenderId;
                mnthCount = mnthCount + 1;
                obj1[1] = mnthCount;
                obj1[2] = dbyear;

                calendar.set(yer, i, dayex);
                int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1;

                if (dayex == 31 && monthex == 0) {
                    dayex = 1;
                }

                strDate = dayex + "-" + (calendar.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + calendar.get(Calendar.YEAR);
                obj1[3] = strDate;
                prmonthdetails[0] = strDate;
                calendar.add(Calendar.DATE, maxDay);
                String endDate = calendar.get(Calendar.DATE) + "-" + (calendar.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + calendar.get(Calendar.YEAR);
                prmonthdetails[1] = endDate;
                obj1[4] = endDate;
                prmonthdetails[2] = monthName + "-" + dbcustName;
                monthName = (calendar.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault()));
                obj1[5] = monthName;
                obj1[6] = monthName + "-" + dbcustName;
                dbcustName = String.valueOf(calendar.get(Calendar.YEAR));
                finalQuery = pbdb.buildQuery(addPrMonth, obj1);

                Createlist.add(finalQuery);
                prmonthlist.add(prmonthdetails);
            }

            if (execute.equalsIgnoreCase("execute")) {
                connection = (Connection) ProgenConnection.getInstance().getConnectionByConId(ConnectionId);
                pbdb.executeMultiple(Createlist, connection);

                connection = null;
            }
        } catch (Exception e) {

            logger.error("Exception:", e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return prmonthlist;
    }

    public void addPrDayDenom(int yer, int mnth, int day, int calenderId, int endyer, int endmnth, int endday, String mnthName, String endMnthName, String createtable) throws Exception {
        Connection connection = null;
        try {
            String finalQuery = "";

            int loopsize = 0;
            ArrayList Createlist = new ArrayList();
            String createDenomTables = resBundle.getString("createDenomTables");
            Object obj1[] = new Object[2];
            if (createtable.equalsIgnoreCase("yes")) {
                obj1[0] = "PR_DAY_DENOM_" + calenderId;
                obj1[1] = "DDATE DATE,ST_DATE DATE,END_DATE DATE,DAYSOFYEAR NUMBER,"
                        + " CWEEK NUMBER,CWYEAR NUMBER,CW_ST_DATE DATE,CW_END_DATE DATE,CW_ST_MON_DATE DATE,CW_END_MON_DATE DATE, "
                        + "CW_CUST_NAME VARCHAR2(100 BYTE),PW_DAY DATE,PW_ST_DATE DATE,PW_END_DATE DATE,PW_ST_MON_DATE DATE, "
                        + "PW_END_MON_DATE DATE,PW_CUST_NAME VARCHAR2(100 BYTE),LYW_DAY DATE,LYW_ST_DATE DATE,LYW_END_DATE DATE,"
                        + "LYW_ST_MON_DATE DATE,	LYW_END_MON_DATE DATE,LYW_CUST_NAME VARCHAR2(100 BYTE),CMONTH NUMBER,"
                        + "CM_YEAR NUMBER,CM_ST_DATE DATE,CM_END_DATE DATE,CM_CUST_NAME VARCHAR2(100 BYTE), "
                        + "PM_DAY DATE,PM_ST_DATE DATE,PM_END_DATE DATE,PM_CUST_NAME VARCHAR2(100 BYTE), "
                        + "PYM_DAY DATE,PYM_ST_DATE DATE,PYM_END_DATE DATE,PYM_CUST_NAME VARCHAR2(100 BYTE),"
                        + "CQTR NUMBER,CQ_YEAR NUMBER,CQ_ST_DATE DATE,CQ_END_DATE DATE,CQ_CUST_NAME VARCHAR2(100 BYTE),"
                        + "LQ_DAY DATE,LQ_ST_DATE DATE,LQ_END_DATE DATE,LQ_CUST_NAME VARCHAR2(100 BYTE),LYQ_DAY DATE,LYQ_ST_DATE DATE, "
                        + "LYQ_END_DATE DATE,	LYQ_CUST_NAME VARCHAR2(100 BYTE),CYEAR NUMBER,CY_ST_DATE DATE,CY_END_DATE DATE,"
                        + "CY_CUST_NAME VARCHAR2(100 BYTE),LY_DAY DATE,LY_ST_DATE DATE,LY_END_DATE DATE,LY_CUST_NAME VARCHAR2(100 BYTE)";
                finalQuery = pbdb.buildQuery(createDenomTables, obj1);

                Createlist.add(finalQuery);
            }
            String addPrDayDenom = resBundle.getString("addPrDayDenom");

            ArrayList cweklist = cWeek(yer, mnth + 1, day, endday, endmnth + 1, endyer, calenderId, "cweek");
            loopsize = cweklist.size();

            ArrayList cmonthlist = cWeek(yer, mnth + 1, day, endday, endmnth + 1, endyer, calenderId, "month");
            if (loopsize < cmonthlist.size()) {
                loopsize = cmonthlist.size();
            }


            ArrayList prdayweklist = prDayWeek_new(yer, mnth + 1, day, endday, endmnth + 1, endyer, calenderId);
            if (loopsize < prdayweklist.size()) {
                loopsize = prdayweklist.size();
            }

            ArrayList lstyrwkdaylist = lastYearWeekDay(yer, mnth + 1, day, endday, endmnth + 1, endyer, calenderId);
            if (loopsize < lstyrwkdaylist.size()) {
                loopsize = lstyrwkdaylist.size();
            }

            ArrayList pyrdaylist = pyearmDay(yer, mnth, day, endday, endmnth, endyer, calenderId);
            if (loopsize < pyrdaylist.size()) {
                loopsize = pyrdaylist.size();
            }

            ArrayList pmntdaylist = pmonthDay(yer, mnth + 1, day, endday, endmnth + 1, endyer, calenderId);
            if (loopsize < pmntdaylist.size()) {
                loopsize = pmntdaylist.size();
            }

            ArrayList cqerwdaylist = cqerweDay(yer, mnth + 1, day, endday, endmnth + 1, endyer, calenderId);
            if (loopsize < cqerwdaylist.size()) {
                loopsize = cqerwdaylist.size();
            }
            //////////////////////////////////////////////////////////.println("cqerwdaylist7---------\t" + cqerwdaylist.size());
            ArrayList clastqerweDaylist = clastqerweDay(yer, mnth, day, endday, endmnth, endyer, calenderId);
            if (loopsize < clastqerweDaylist.size()) {
                loopsize = clastqerweDaylist.size();
            }
            //////////////////////////////////////////////////////////.println("clastqerweDaylist8---------\t" + clastqerweDaylist.size());
            // ////////////////////////.println("------------------------lyer-----------------------");
            ArrayList clastyerqerDaylist = clastyerqerDay(yer, mnth, day, endday, endmnth, endyer, calenderId);
            if (loopsize < clastyerqerDaylist.size()) {
                loopsize = clastyerqerDaylist.size();
            }

            // ////////////////////////.println("-----------------------------------------------");
            //////////////////////////////////////////////////////////.println("clastyerqerDaylist9---------\t" + clastyerqerDaylist.size());
            ArrayList cyearDaylist = cyearDay(yer, mnth + 1, day, endday, endmnth + 1, endyer, calenderId);
            if (loopsize < cyearDaylist.size()) {
                loopsize = cyearDaylist.size();
            }
            //////////////////////////////////////////////////////////.println("cyearDaylist10---------\t" + cyearDaylist.size());
            ArrayList lyearDaylist = lyearDay(yer, mnth + 1, day, endday, endmnth + 1, endyer, calenderId);
            if (loopsize < lyearDaylist.size()) {
                loopsize = lyearDaylist.size();
            }
            //////////////////////////////////////////////////////////.println("lyearDaylist11---------\t" + lyearDaylist.size());

            //Upto here the code is to execute for multiple and single years



            int i = 0, stringlimit = 0, j = 0, count = -1, k = 0, totlength = 0, l = 1, iflarge;
//            String insertstrng = "";
            StringBuilder insertstrng=new StringBuilder();
            Object insrtobj[];
//        ////////////////////////.println(" cweklist.size()---" + cweklist.size());
            ////////////////////////.println("loopsize" + loopsize);
            for (i = 0; i < loopsize; i++) {
                iflarge = i;
                ////////////////////////.println("iflarge is i" + i);
                if (i == cweklist.size()) {
                    iflarge = cweklist.size() - 1;
                    ////////////////////////.println("iflarge" + iflarge);
                }
                String weklstrng[] = cweklist.get(iflarge).toString().split(",");
                if (i == prdayweklist.size()) {
                    iflarge = prdayweklist.size() - 1;
                }
                String prdaystrng[] = prdayweklist.get(iflarge).toString().split(",");
                if (i == lstyrwkdaylist.size()) {
                    iflarge = lstyrwkdaylist.size() - 1;
                }
                String lstyrwkdaystr[] = lstyrwkdaylist.get(iflarge).toString().split(",");
                if (i == cmonthlist.size()) {
                    iflarge = cmonthlist.size() - 1;
                }
                String cmonthstrng[] = cmonthlist.get(iflarge).toString().split(",");
                if (i == pyrdaylist.size()) {
                    iflarge = pyrdaylist.size() - 1;
                }
                String pyrdaystrng[] = pyrdaylist.get(iflarge).toString().split(",");


                //    if (pmntdaylist.size() > cweklist.size()) {
                //        int min = i - 1;
                //       pyrdaystrng = pyrdaylist.get(min).toString().split(",");
                //    }
                if (i == pmntdaylist.size()) {
                    iflarge = pmntdaylist.size() - 1;
                }
                String pmntdaystrng[] = pmntdaylist.get(iflarge).toString().split(",");
                if (i == cqerwdaylist.size()) {
                    iflarge = cqerwdaylist.size() - 1;
                }
                String cqerwdaystrng[] = cqerwdaylist.get(iflarge).toString().split(",");
                if (i == clastqerweDaylist.size()) {
                    iflarge = clastqerweDaylist.size() - 1;
                }
                String clastqerweDaystrng[] = clastqerweDaylist.get(iflarge).toString().split(",");
                if (i == clastyerqerDaylist.size()) {
                    iflarge = clastyerqerDaylist.size() - 1;
                }
                String clastyerqerDaystrng[] = clastyerqerDaylist.get(iflarge).toString().split(",");
                if (i == cyearDaylist.size()) {
                    iflarge = cyearDaylist.size() - 1;
                }
                String cyearDaystrng[] = cyearDaylist.get(iflarge).toString().split(",");
                if (i == lyearDaylist.size()) {
                    iflarge = lyearDaylist.size() - 1;
                }
                String lyearDaystrng[] = lyearDaylist.get(iflarge).toString().split(",");

                stringlimit = weklstrng.length + prdaystrng.length + lstyrwkdaystr.length + cmonthstrng.length + pmntdaystrng.length + pyrdaystrng.length + cqerwdaystrng.length
                        + clastqerweDaystrng.length + clastyerqerDaystrng.length + cyearDaystrng.length + lyearDaystrng.length + 1;
                insrtobj = new Object[stringlimit];
                insrtobj[0] = calenderId;
                for (j = 1, k = 0; j < weklstrng.length + 1; j++, k++) {
                    insrtobj[l] = weklstrng[k].toString();
                    //  ////////////////////////.println(j+"-"+insrtobj[j]);
                    count++;
                    insertstrng.append(insertstrng).append(",").append(insrtobj[k]);
                    l++;

                }
                totlength = count;

                for (j = count + 2, k = 0; j < totlength + prdaystrng.length + 2; j++, k++) {
//               ////////////////////// ////////////////////////.println("j"+j);
//               ////////////////////// ////////////////////////.println("contu"+count);
//             ////////////////////////.println(prdaystrng.length+2);
                    insrtobj[l] = prdaystrng[k].toString();
                    //   ////////////////////////.println(j+"-"+insrtobj[j]);
                    count++;
                    insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                    l++;

                }
                totlength = count;
                for (j = count + 2, k = 0; j < totlength + lstyrwkdaystr.length + 2; j++, k++) {
                    insrtobj[l] = lstyrwkdaystr[k].toString();
                    // ////////////////////////.println(j+"-"+insrtobj[j]);
                    count++;
                    insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                    l++;

                }
                totlength = count;
                for (j = count + 2, k = 0; j < totlength + cmonthstrng.length + 2; j++, k++) {
                    insrtobj[l] = cmonthstrng[k].toString();
                    //  ////////////////////////.println(j+"-"+insrtobj[j]);
                    count++;
                    insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                    l++;

                }

                totlength = count;
                for (j = count + 2, k = 0; j < totlength + pmntdaystrng.length + 2; j++, k++) {
                    insrtobj[l] = pmntdaystrng[k].toString();
                    //////////////////////////.println(j+"-"+insrtobj[j]);
                    count++;
                    insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                    l++;

                }
                totlength = count;
                for (j = count + 2, k = 0; j < totlength + pyrdaystrng.length + 2; j++, k++) {
                    insrtobj[l] = pyrdaystrng[k].toString();
                    //  ////////////////////////.println(j+"-"+insrtobj[j]);
                    count++;
                    insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                    l++;

                }
                totlength = count;
                for (j = count + 2, k = 0; j < totlength + cqerwdaystrng.length + 2; j++, k++) {
                    insrtobj[l] = cqerwdaystrng[k].toString();
                    //  ////////////////////////.println(j+"-"+insrtobj[j]);
                    count++;
                    insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                    l++;

                }
                totlength = count;
                for (j = count + 2, k = 0; j < totlength + clastqerweDaystrng.length + 2; j++, k++) {
                    insrtobj[l] = clastqerweDaystrng[k].toString();
                    //  ////////////////////////.println(j+"-"+insrtobj[j]);
                    count++;
                    insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                    l++;

                }
                totlength = count;
                for (j = count + 2, k = 0; j < totlength + clastyerqerDaystrng.length + 2; j++, k++) {
                    insrtobj[l] = clastyerqerDaystrng[k].toString();
                    // ////////////////////////.println(j+"-"+insrtobj[j]);
                    count++;
                    insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                    l++;
                }
                totlength = count;
                for (j = count + 2, k = 0; j < totlength + cyearDaystrng.length + 2; j++, k++) {
                    insrtobj[l] = cyearDaystrng[k].toString();
                    //     ////////////////////////.println(j+"-"+insrtobj[j]);
                    count++;
                    insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                    l++;
                }
                totlength = count;
                for (j = count + 2, k = 0; j < totlength + lyearDaystrng.length + 2; j++, k++) {
                    insrtobj[l] = lyearDaystrng[k].toString();
                    //   ////////////////////////.println(j+"-"+insrtobj[j]);
                    count++;
                    insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                    l++;
                }
                finalQuery = pbdb.buildQuery(addPrDayDenom, insrtobj);
                // 
                Createlist.add(finalQuery);
                l = 1;

            }
            connection = (Connection) ProgenConnection.getInstance().getConnectionByConId(ConnectionId);
            // pbdb.executeMultiple(Createlist,connection);
            // connection.close();
            connection = null;
        } catch (Exception e) {
            logger.error("Exception:", e);
        } finally {
            if (connection != null) {
                connection.close();
            }

        }
    }

    public void addPrWeekDenom(int yer, int mnth, int day, int calenderId, int endyer, int endmnth, int endday, String mnthName, String endMnthName, String createtable) throws Exception {

        Connection connection = null;
        try {
            String finalQuery = "";
            ArrayList Createlist = new ArrayList();
            String createDenomTables = resBundle.getString("createDenomTables");
            Object obj1[] = new Object[2];
            if (createtable.equalsIgnoreCase("yes")) {
                obj1[0] = "PR_WEEK_DENOM_" + calenderId;
                obj1[1] = "CWEEK NUMBER,CYEAR NUMBER, C_ST_DATE DATE,C_END_DATE DATE,C_NO_DAYS NUMBER,PW_WEEK NUMBER,"
                        + "PW_YEAR NUMBER,PW_ST_DATE DATE,PW_END_DATE DATE,PY_WEEK NUMBER,PY_YEAR NUMBER,PY_ST_DATE DATE,PY_END_DATE DATE";
                finalQuery = pbdb.buildQuery(createDenomTables, obj1);
                ////////////////////// ////////////////////////.println("create table" + finalQuery);
                Createlist.add(finalQuery);
            }
            String addPrWeekDenom = resBundle.getString("addPrWeekDenom");
            //String strDate=day+"-"+mnth+"-"+yer;
            //String endDate=endday+"-"+endmnth+"-"+
            String dets1[] = getpriorweeks(day, mnth, yer, endday, endmnth, endyer, "No");
            // String dets2[] = priorWeek(day, mnth, yer, endday, endmnth, endyer);
            String dets2[] = getpriorweeks(day, mnth, yer, endday, endmnth, endyer, "yes");
            String getstrtendyear[] = priorYear(day, mnth, yer, endday, endmnth, endyer);
            String priorstartyear = getstrtendyear[4];
            String priorendyear = getstrtendyear[5];
            ////////////////////// ////////////////////////.println("priorstartyear---"+priorstartyear);
            ////////////////////// ////////////////////////.println("priorendyear---"+priorendyear);

            String priorstrtarr[] = priorstartyear.split("-");
            int premonth = Integer.parseInt(priorstrtarr[1].toString());
            int preday = Integer.parseInt(priorstrtarr[0].toString());
            int preyer = Integer.parseInt(priorstrtarr[2].toString());
            String[] priorendarr = new String[3];

            priorendarr = priorendyear.split("-");
            int preendmnth = Integer.parseInt(priorendarr[1].toString());
            int preendday = Integer.parseInt(priorendarr[0].toString());
            int preendyer = Integer.parseInt(priorendarr[2].toString());


            String dets3[] = getpriorweeks(preday, premonth, preyer, preendday, preendmnth, preendyer, "No");

            //////////////////////// ////////////////////////.println("dets1.length--" + dets1.length+"---"+dets1[0]);
            //////////////////////// ////////////////////////.println("dets2.length--" + dets2.length+"---"+dets2[0]);
            //////////////////////// ////////////////////////.println("dets3.length--" + dets3.length+"----"+dets3[0]);
            obj1 = new Object[14];
            obj1[0] = calenderId;
            for (int i = 0; i < dets1.length; i++) {
                String cdets[] = dets1[i].split(",");
                //////////////////////// ////////////////////////.println("--1-"+cdets[0]);
                String pdets[] = dets2[i].split(",");
                //////////////////////////.println("--2-"+pdets[0]);
                String pydets[] = dets3[i].split(",");
                ////////////////////// ////////////////////////.println("--3-"+dets3[i]);
                obj1[1] = cdets[0];
                obj1[2] = cdets[1];
                obj1[3] = cdets[2];
                obj1[4] = cdets[3];
                obj1[5] = cdets[4];

                //if (cdets[0].equalsIgnoreCase("52")) {
                //     obj1[6] = 1;
                // } else
                if (cdets[0].equalsIgnoreCase("1")) {
                    obj1[6] = 52;
                } else {
                    obj1[6] = Integer.parseInt(cdets[0]) - 1;
                }
                obj1[7] = pdets[1];
                obj1[8] = pdets[2];
                obj1[9] = pdets[3];
                // obj1[9] = pydets[0];
                obj1[10] = cdets[0];
                obj1[11] = pydets[1];
                obj1[12] = pydets[2];
                obj1[13] = pydets[3];
                finalQuery = pbdb.buildQuery(addPrWeekDenom, obj1);
                Createlist.add(finalQuery);
//            ////////////////////////.println("finalQuery-add weekdenom-" + finalQuery);
            }
            //call to prior year details
            connection = (Connection) ProgenConnection.getInstance().getConnectionByConId(ConnectionId);
            pbdb.executeMultiple(Createlist, connection);
            // connection.close();
            connection = null;
        } catch (Exception e) {
            logger.error("Exception:", e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void addPrMonthDenom(int yer, int mnth, int day, int calenderId, int endyer, int endmnth, int endday, String mnthName, String createtable) throws Exception {

        Connection connection = null;
        try {
            String finalQuery = "";
            ArrayList Createlist = new ArrayList();
            String createDenomTables = resBundle.getString("createDenomTables");
            Object obj1[] = new Object[2];
            if (createtable.equalsIgnoreCase("yes")) {
                obj1[0] = "PR_MONTH_DENOM_" + calenderId;
                obj1[1] = "CMON NUMBER, 	CYEAR NUMBER, 	C_ST_DATE DATE, 	C_END_DATE DATE, 	C_NO_DAYS NUMBER,"
                        + "PM_MON NUMBER, 	PM_YEAR NUMBER, 	PM_ST_DATE DATE, 	PM_END_DATE DATE, 	PM_DAYS NUMBER, 	PY_MON NUMBER, 	PY_YEAR NUMBER,"
                        + "PY_ST_DATE DATE, 	PY_END_DATE DATE, 	PY_DAYS NUMBER";
                finalQuery = pbdb.buildQuery(createDenomTables, obj1);
                ////////////////////////////////////////////////.println(finalQuery);
                Createlist.add(finalQuery);
            }
            String addPrMonthDenom = resBundle.getString("addPrMonthDenom");
            // String dets1[]=addPrMonth(yer, mnth, day, calenderId, endyer, endmnth, endday, mnthName);
//        ////////////////////////.println(day+","+mnth+","+yer+","+endday+","+endmnth+","+endyer);
            String dets2[] = priorMonth(day, mnth - 1, yer, endday, endmnth - 1, endyer);

//       ////////////////////// ////////////////////////.println("-------------month----------------");
            obj1 = new Object[16];
            obj1[0] = calenderId;
            for (int i = 0; i < dets2.length; i++) {
//            ////////////////////////.println("dets2\t"+dets2[i]);
                String det1slist[] = dets2[i].split(",");
                obj1[1] = det1slist[0];
                obj1[2] = det1slist[1];
                obj1[3] = det1slist[2];
                obj1[4] = det1slist[3];
                obj1[5] = det1slist[4];
                obj1[6] = det1slist[5];
                obj1[7] = det1slist[6];
                obj1[8] = det1slist[7];
                obj1[9] = det1slist[8];
                obj1[10] = det1slist[9];
                obj1[11] = det1slist[10];
                obj1[12] = det1slist[11];
                obj1[13] = det1slist[12];
                obj1[14] = det1slist[13];
                obj1[15] = det1slist[14];
                finalQuery = pbdb.buildQuery(addPrMonthDenom, obj1);
                Createlist.add(finalQuery);
//            ////////////////////////.println("finalQuery-add monthdenom-" + finalQuery);
            }
            connection = (Connection) ProgenConnection.getInstance().getConnectionByConId(ConnectionId);
            pbdb.executeMultiple(Createlist, connection);
            // connection.close();
            connection = null;
        } catch (Exception e) {

            logger.error("Exception:", e);

        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void addQrterDenom(int yer, int month, int day, int calenderId, int endyear, int endMonth, int endDay, String monthName, String createtable) {
        Connection connection = null;
        try {
            ArrayList list = new ArrayList();
            String finalQuery = "";
//            ArrayList Createlist = new ArrayList();
            String createDenomTables = resBundle.getString("createDenomTables");
            Object obj1[] = new Object[2];
            if (createtable.equalsIgnoreCase("yes")) {
                obj1[0] = "PR_QTR_DENOM_" + calenderId;
                obj1[1] = "CQTR NUMBER, CYEAR NUMBER,C_ST_DATE DATE, 	C_END_DATE DATE,	C_NO_DAYS NUMBER, 	PQ_QTR NUMBER,"
                        + "PQ_YEAR NUMBER, 	PQ_ST_DATE DATE,	PQ_END_DATE DATE, 	PQ_DAYS NUMBER, 	PY_QTR NUMBER, 	PY_YEAR NUMBER, 	PY_ST_DATE DATE,"
                        + "PY_END_DATE DATE, PY_DAYS NUMBER";
                finalQuery = pbdb.buildQuery(createDenomTables, obj1);
//                ////////////////////////.println(finalQuery);
                list.add(finalQuery);
            }
            String addPrQtrDenom = resBundle.getString("addPrQtrDenom");
            String dets1[] = addPrQtr(yer, month, day, calenderId, endyear, endMonth, endDay, monthName, "No", "No");

            //cal to prior details
            //////////////////////////.println("---------------------------------QTR------------------------------------------------");
            String dets2[] = addpriorQrter(yer, month, day, calenderId, endyear, endMonth, endDay, monthName, "YES");

            String getstrtendyear[] = priorYear(day, month, yer, day, month, endyear);
            String priorstartyear = getstrtendyear[4];
            String priorendyear = getstrtendyear[5];
            String[] priorstrtarr = new String[3];
            //////////////////////// ////////////////////////.println("priorstartyear==========" + priorstartyear);
            //////////////////////// ////////////////////////.println("priorendyear==========" + priorendyear);
            priorstrtarr = priorstartyear.split("-");
            month = Integer.parseInt(priorstrtarr[1].toString());
            day = Integer.parseInt(priorstrtarr[0].toString());
            yer = Integer.parseInt(priorstrtarr[2].toString());
            String[] priorendarr = new String[3];

            priorendarr = priorendyear.split("-");
            int endmnth = Integer.parseInt(priorendarr[1].toString());
            int endday = Integer.parseInt(priorendarr[0].toString());
            int endyer = Integer.parseInt(priorendarr[2].toString());
            // // ////////////////////////.println("in qtr ------------"+yer+","+ month+","+  day+","+  calenderId+","+  endyer+","+  endmnth+","+  endday+","+  monthName);
            String dets3[] = addPrQtr(yer, month, day, calenderId, endyer, endmnth, endday, monthName, "NO", "No");


            int i = 0;
            obj1 = new Object[16];
            obj1[0] = calenderId;
            for (i = 0; i < dets1.length; i++) {
                String det1slist[] = dets1[i].split(",");
                // ////////////////////////.println("det2slist===" + dets2[i]);
                String det2slist[] = dets2[i].split(",");
                String det3slist[] = dets3[i].split(",");
                obj1[1] = det1slist[0];
                obj1[2] = det1slist[1];
                obj1[3] = det1slist[2];
                obj1[4] = det1slist[3];
                obj1[5] = det1slist[4];
                obj1[6] = det2slist[0];
                obj1[7] = det2slist[1];
                obj1[8] = det2slist[2];
                obj1[9] = det2slist[3];
                obj1[10] = det2slist[4];
                obj1[11] = det3slist[0];
                obj1[12] = det3slist[1];
                obj1[13] = det3slist[2];
                obj1[14] = det3slist[3];
                obj1[15] = det3slist[4];
                finalQuery = pbdb.buildQuery(addPrQtrDenom, obj1);
                list.add(finalQuery);
//                ////////////////////////.println("finalQuery-add qtrdenom-" + finalQuery);
                ////// ////////////////////////.println("queries \t" + dets1[i] + "," + dets2[i] + "," + dets3[i]);
            }
            //////////////////////// ////////////////////////.println("---------------------------------------------------------------------------------");
            //call to prior year details
            connection = (Connection) ProgenConnection.getInstance().getConnectionByConId(ConnectionId);
            pbdb.executeMultiple(list, connection);
            // connection.close();
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
    }

    public void addPrYearDenom(int yer, int mnth, int day, int calenderId, int endyer, int endmnth, int endday, String mnthName, String endMnthName, String createtable) throws Exception {
        Connection connection = null;
        try {
            String finalQuery = "";
            ArrayList Createlist = new ArrayList();
            String createDenomTables = resBundle.getString("createDenomTables");
            Object obj1[] = new Object[2];
            if (createtable.equalsIgnoreCase("yes")) {
                obj1[0] = "PR_YEAR_DENOM_" + calenderId;
                obj1[1] = "CYEAR NUMBER,C_ST_DATE DATE,C_END_DATE DATE,C_NO_DAYS NUMBER,PYEAR NUMBER,P_ST_DATE DATE,P_END_DATE DATE,P_NO_DAYS NUMBER";
                finalQuery = pbdb.buildQuery(createDenomTables, obj1);
                Createlist.add(finalQuery);
            }
            ////////////////////// ////////////////////////.println("finalqUERY" + finalQuery);
            String addPrYeardenom = resBundle.getString("addPrYeardenom");
            String dets1[] = priorYear(day, mnth, yer, endday, endmnth, endyer);
            //  ////////////////////// ////////////////////////.println("dets1[]--" + dets1[0]);
            //  ////////////////////// ////////////////////////.println("dets1[]--" + dets1[1]);
            obj1 = new Object[9];
            obj1[0] = calenderId;
            obj1[1] = yer;
            obj1[2] = day + "-" + mnthName + "-" + yer;
            obj1[3] = endday + "-" + endMnthName + "-" + endyer;
            obj1[4] = gettotalnumofDays(day + "-" + mnthName + "-" + yer, endday + "-" + endMnthName + "-" + endyer);
            obj1[5] = dets1[0];
            obj1[6] = dets1[1];
            obj1[7] = dets1[2];
            obj1[8] = dets1[3];
            finalQuery = pbdb.buildQuery(addPrYeardenom, obj1);
            Createlist.add(finalQuery);
            connection = (Connection) ProgenConnection.getInstance().getConnectionByConId(ConnectionId);
            pbdb.executeMultiple(Createlist, connection);
            // connection.close();
            connection = null;
        } catch (Exception e) {
            logger.error("Exception:", e);

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
            }
        }
    }

    //added by praveen
    public String[] addpriorQrter(int yer, int month, int day, int calenderId, int endyear, int endMonth, int endDay, String monthName, String check) {

        String addlist[] = new String[4];

        try {
            String[] monthNames = {"JAN", "FEB",
                "MAR", "APR", "MAY", "JUN", "JUL",
                "AUG", "SEP", "OCT", "NOV",
                "DEC"};
            int j[] = {4, 1, 2, 3};
            int sno = 0;
            if (check.equalsIgnoreCase("yes")) {
                if (month >= 3) {
                    month = month - 3;
                }
                if (month < 3) {
                    yer = yer - 1;
                    if (month == 2) {
                        month = 11;
                    }
                    if (month == 1) {
                        month = 10;
                    }
                    if (month == 0) {
                        month = 9;
                    }
                }
            }
            monthName = monthNames[month];

//            String concat;
            StringBuilder concat=new StringBuilder();

            Calendar calendar = Calendar.getInstance();
            calendar.set(yer, month, day);
            String strdate = day + "-" + monthName + "-" + yer;
            String endDate = "";
            String forcqtrcustname = "";
            int extradays = totaldays(yer, (month + 1), day) - day;
            int dbyear = yer;

            for (int i = 1; i <= 4; i++) {
                calendar.add(Calendar.MONTH, 3);

                if (check.equalsIgnoreCase("yes")) {
                    sno = j[i - 1];
                } else if (check.equalsIgnoreCase("no")) {
                    sno = i;
                }
                concat.append(sno).append(",").append(dbyear).append(",").append(strdate);
                forcqtrcustname = "Q" + sno + "-" + dbyear;
                String sndstrtdate = strdate;

                if (check.equalsIgnoreCase("yes")) {
                    if (i == 1) {
                        calendar.add(Calendar.DATE, -1);
                    }
                }

                endDate = calendar.get(5) + "-" + calendar.getDisplayName(2, Calendar.SHORT, Locale.getDefault()) + "-" + calendar.get(1);
                concat.append(",").append(endDate);

                calendar.add(Calendar.DATE, 1);
                dbyear = calendar.get(1);
                strdate = (calendar.get(5)) + "-" + calendar.getDisplayName(2, Calendar.SHORT, Locale.getDefault()) + "-" + calendar.get(1);


                calendar.add(Calendar.DATE, -1);
                int totdays = gettotalnumofDays(sndstrtdate, endDate);
                if (check.equalsIgnoreCase("no")) {
                    totdays = gettotalnumofDays(day + "-" + monthNames[month] + "-" + yer, endDay + "-" + monthNames[endMonth] + "-" + endyear);
                }
                addlist[i - 1] = concat + "," + totdays + "," + forcqtrcustname;

            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return addlist;
    }

    public String[] priorYear(int day3, int month3, int year3, int eday, int emonth, int eyear) {
        String strend[] = new String[6];
        int yearpr = year3;
        int monthpr = month3;
        int daypr = day3;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(year3, month3, day3);
        cal2.set(eyear, emonth, eday);
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        long diff = milis2 - milis1;
        long diffDays = (diff / (24 * 60 * 60 * 1000)) + 1;
        //////////////////////// ////////////////////////.println("In This Year Total days: " + (diffDays) + " days.");
        cal1.set(yearpr, monthpr, daypr);
//       //////////////////////// ////////////////////////.println("Start day of the Prior year is " + daypr + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR));
        cal2.set(eyear, emonth, eday);
//       //////////////////////// ////////////////////////.println("End day of the year is " + cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR));
        cal1.add(Calendar.DATE, (int) -diffDays);
//       //////////////////////// ////////////////////////.println("Start day of the Prior year is " + daypr + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR));
//        String startdate = daypr + "/" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "/" + cal1.get(Calendar.YEAR);
        String startdate = daypr + "-" + cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault()) + "-" + cal1.get(Calendar.YEAR);
        String startdate1 = daypr + "-" + cal1.get(Calendar.MONTH) + "-" + cal1.get(Calendar.YEAR);
        cal2.add(Calendar.DATE, (int) -diffDays);
//       //////////////////////// ////////////////////////.println("End day of the Prior year is " + cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR));
//        String enddate = cal2.get(Calendar.DATE) + "/" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "/" + cal2.get(Calendar.YEAR);
        String enddate = cal2.get(Calendar.DATE) + "-" + cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault()) + "-" + cal2.get(Calendar.YEAR);
        String enddate1 = cal2.get(Calendar.DATE) + "-" + cal2.get(Calendar.MONTH) + "-" + cal2.get(Calendar.YEAR);
        cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DATE));
        cal2.set(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal1.get(Calendar.DATE));
        long milisp1 = cal1.getTimeInMillis();
        long milisp2 = cal2.getTimeInMillis();
        long diffp = milisp2 - milisp1;
        long diffDaysp = diffp / (24 * 60 * 60 * 1000);
        //////////////////////////.println("In Prior Year Total days: " + diffDaysp + " days.");

        strend[0] = String.valueOf(cal1.get(Calendar.YEAR));
        strend[1] = startdate;
        strend[2] = enddate;
        strend[3] = String.valueOf(gettotalnumofDays(startdate1, enddate1));
        strend[4] = startdate1;
        strend[5] = enddate1;
//       //////////////////////// ////////////////////////.println("strends are000000000000000" + strend);
        return strend;
    }

    public String[] prweek(int strtday, int strtmonth, int strtyear, int eday, int emonth, int eyear) {

//        String concat = "";
       StringBuilder concat=new StringBuilder();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(strtyear, strtmonth, strtday);
        cal2.set(eyear, emonth, eday);
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        long diff = milis2 - milis1;
        long diffDays = (diff / (7 * 24 * 60 * 60 * 1000));
//       //////////////////////// ////////////////////////.println("In This Year Total Weeks: " + (diffDays));
        int weekno2 = (cal1.get(Calendar.WEEK_OF_YEAR)) - 1;
        //////////////////////////.println("Current week is befour loop " + weekno2);
        String prwekarr[] = new String[(int) diffDays];

        for (int w = 1; w <= diffDays; w++) {
            cal1.set(strtyear, strtmonth, strtday);
            int weekno = cal1.get(Calendar.WEEK_OF_YEAR) - weekno2;
//           //////////////////////// ////////////////////////.println("Current week is " + weekno);
//           //////////////////////// ////////////////////////.println("Start day of the Week is " + cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR));
            int strtwekyear = cal1.get(Calendar.YEAR);
            int day4 = strtday + 6;
            cal2.set(strtyear, strtmonth, day4);
//           //////////////////////// ////////////////////////.println("End day of the Week is " + cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR));
            concat.append(weekno).append(",").append(strtwekyear).append(",").append(cal1.get(Calendar.DATE)).append("-").append(cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())).append("-").append(cal1.get(Calendar.YEAR)).append(",7").append(cal2.get(Calendar.DATE)).append("-").append(cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())).append("-").append(cal2.get(Calendar.YEAR));
            cal1.add(Calendar.DATE, -7);
            int weekno1 = cal1.get(Calendar.WEEK_OF_YEAR) - weekno2;
            if (weekno1 == 0) {
                weekno1 = 52;
            }
//           //////////////////////// ////////////////////////.println("Prior week is " + weekno1);
//           //////////////////////// ////////////////////////.println("Start day of the Prior Week is " + cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR));
            int priorwekyear = cal2.get(Calendar.YEAR);
            cal2.add(Calendar.DATE, -7);
//           //////////////////////// ////////////////////////.println("End day of the Prior Week is " + cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR));
            strtday = strtday + 7;
            concat.append(",").append(weekno1).append(",").append(priorwekyear).append(",").append(cal1.get(Calendar.DATE)).append("-").append(cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())).append("-").append(cal1.get(Calendar.YEAR)).append(",").append(cal2.get(Calendar.DATE)).append("-").append(cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())).append("-").append(cal2.get(Calendar.YEAR));
            prwekarr[w - 1] = concat.toString();
        }
//        String getpriorwekarr[] = prioryearWek(strtday, strtmonth, strtyear, eday, emonth, eyear);
//        for (int i = 0; i < getpriorwekarr.length; i++) {
//           //////////////////////// ////////////////////////.println("at Last priro Week Query\t" + prwekarr[i] + "," + getpriorwekarr[i]);
//        }

        return prwekarr;
    }

    public String[] prioryearWek(int strtdate, int strtmonth, int strtyear, int eday, int emonth, int eyear) {

        String priorwekconcat;

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(strtyear - 1, strtmonth, strtdate);
        cal2.set(eyear - 1, emonth, eday);
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        long diff = milis2 - milis1;
        long diffDays = (diff / (7 * 24 * 60 * 60 * 1000));
        ////////////////////////// ////////////////////////.println("In This Year Total Weeks: " + (diffDays));
        int weekno2 = (cal1.get(Calendar.WEEK_OF_YEAR)) - 1;
        //////////////////////////.println("Current week is befour loop " + weekno2);
        String prorwekarr[] = new String[(int) diffDays];
        for (int w = 1; w <= diffDays; w++) {
            cal1.set(strtyear - 1, strtmonth, strtdate);
            int weekno = cal1.get(Calendar.WEEK_OF_YEAR) - weekno2;
            ////////////////////////// ////////////////////////.println("Current week is " + weekno);
            // //////////////////////// ////////////////////////.println("Start day of the Week is " + cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR));
            int calyear = cal1.get(Calendar.YEAR);
            int day4 = strtdate + 6;
            cal2.set(strtyear - 1, strtdate, day4);
            //  //////////////////////// ////////////////////////.println("End day of the Week is " + cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR));
            cal1.add(Calendar.DATE, -7);
            int weekno1 = cal1.get(Calendar.WEEK_OF_YEAR) - weekno2;
            if (weekno1 == 0) {
                weekno1 = 52;
            }
            strtdate = strtdate + 7;
            priorwekconcat = weekno + "," + calyear + "," + cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR)
                    + cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR);
            prorwekarr[w - 1] = priorwekconcat;
        }
        return prorwekarr;
    }

    //code for pr week denom
    public String[] priorWeek(int day3, int month3, int year3, int eday, int emonth, int eyear) {

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(year3, month3, day3);
        //  cal1.add(Calendar.YEAR,-1);
        // cal1.add(Calendar.MONTH,-cal1.getActualMaximum(Calendar.MONTH));
        cal2.set(eyear, emonth, eday);
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        long diff = milis2 - milis1;
        long diffWeeks = (diff / (7 * 24 * 60 * 60 * 1000));
        int totweekno = (int) (diff / (7 * 24 * 60 * 60 * 1000));
        int weekno2 = cal1.get(3);
        String dets[] = new String[totweekno];
        cal1.set(year3, month3, day3);
        cal1.add(Calendar.DATE, -6);
        String strDate = cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR);
        cal1.add(Calendar.DATE, -1);
        for (int w = 1; w <= diffWeeks; w++) {
//           String detstr = "";
           StringBuilder detstr=new StringBuilder();
            detstr.append( w);
            detstr.append(Integer.toString(cal1.get(Calendar.YEAR)));
            detstr.append(strDate);
            cal1.add(Calendar.DATE, 6);
            cal1.add(Calendar.DATE, 8);
            String enddate = cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR);
            cal1.add(Calendar.DATE, 1);
            strDate = cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR);
            cal1.add(Calendar.DATE, -1);
            detstr.append(",").append(enddate);
            day3 = day3 + 7;
            dets[w - 1] = detstr.toString();
        }
        return dets;
    }

    //for prior month
    public String[] priorMonth(int day3, int month3, int year3, int eday, int emonth, int eyear) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar endcal = Calendar.getInstance();
        cal1.set(year3, month3, day3);
        cal2.set(year3, month3, day3);
        endcal.set(eyear, emonth, eday);
        String dets1[] = priorYear(day3, month3, year3, eday, emonth, eyear);
        String pyearstrDate = dets1[1];
        String pyearenddate = dets1[2];
 String pyeardets[] = pyearstrDate.split("-");
        Calendar pycal = Calendar.getInstance();
        pycal.set(Integer.parseInt(pyeardets[2]), monthNumber(pyeardets[1]), Integer.parseInt(pyeardets[0]));
//        String pyerenddates[] = pyearenddate.split("-");
     int pyear = cal1.get(Calendar.YEAR);
        int pmonth = cal1.get(Calendar.MONTH);
        int pday = cal1.get(Calendar.DATE);
        String mnthdetstr = "";
        String mnthdets[] = new String[12];
        String strDate = "";
        String endDate = "";
        String pstrDate = "";
        String pendDate = "";
        String cyear = "";
        String pmyear = "";
        String cmonnum = "";
        String pmonnum = "";
        String pymonthnum = "";
        String pymonthstrDate = "";
        String pymonthenddate = "";
        int pytotaldays = daysBetween(cal1, endcal); //gettotalnumofDays(pyearstrDate, pyearenddate);// diffdates(pyear, pmonth, pday, eyear, emonth, eday);
        String pymonthyear = "";
        for (int i = 1; i <= 12; i++) {
            strDate = cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR);
            cyear = String.valueOf(cal1.get(Calendar.YEAR));
            cmonnum = String.valueOf(cal1.get(Calendar.MONTH) + 1);
            pymonthstrDate = pycal.get(Calendar.DATE) + "-" + (pycal.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + pycal.get(Calendar.YEAR);
            pymonthyear = String.valueOf(pycal.get(Calendar.YEAR));
            pymonthnum = String.valueOf(pycal.get(Calendar.MONTH) + 1);
            cal1.add(Calendar.MONTH, 1);
            cal1.add(Calendar.DATE, - 1);
            pycal.add(Calendar.MONTH, 1);
            pycal.add(Calendar.DATE, - 1);
            endDate = cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR);
            pymonthenddate = pycal.get(Calendar.DATE) + "-" + (pycal.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + pycal.get(Calendar.YEAR);
            cal1.add(Calendar.DATE, 1);
            pycal.add(Calendar.DATE, 1);
            cal2.add(Calendar.MONTH, -1);
            pstrDate = cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR);
            pmyear = String.valueOf(cal2.get(Calendar.YEAR));
            pmonnum = String.valueOf(cal2.get(Calendar.MONTH) + 1);
            cal2.add(Calendar.MONTH, 1);
            cal2.add(Calendar.DATE, - 1);
            pendDate = cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR);
            cal2.add(Calendar.DATE, 1);
            cal2.add(Calendar.MONTH, 1);
            int cnumofdays = gettotalnumofDays(strDate, endDate);
int pnumofdays = gettotalnumofDays(pstrDate, pendDate);
            mnthdetstr = cmonnum + "," + cyear + "," + strDate + "," + endDate + "," + cnumofdays + "," + pmonnum + "," + pmyear + "," + pstrDate + "," + pendDate + "," + pnumofdays + "," + pymonthnum + "," + pymonthyear + "," + pymonthstrDate + "," + pymonthenddate + "," + pytotaldays;
            mnthdets[i - 1] = mnthdetstr;
        }
        return mnthdets;
    }

    //added by praveen for day denom
    public ArrayList cWeekTest(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId, String flag) {

        ArrayList crntweklist = new ArrayList();
        ArrayList crnmnthlist = new ArrayList();
        Calendar cal = Calendar.getInstance();
        String ddate, strtdate, enddate, cwstrtdate, cwenddate, custname, cwstrtmntdate, cwendmntdate, fullstring, cmstrtdate, cmenddate, cmcustname, crntmnthstr;
        int daysOfYear, daysofyear, cweek, cwyear, i = 0, days = 0, count = 0, cmonth;
        days = totaldays(yer, month + 1, day);
        daysOfYear = gettotalnumofDays(day + "-" + monthName(month) + "-" + yer, endday + "-" + monthName(endmonth) + "-" + endyer);
        for (i = day; i <= days; i++) {
            count++;
            ddate = i + "-" + monthName(month) + "-" + yer;
            cmonth = month + 1;
            cmstrtdate = 1 + "-" + monthName(month) + "-" + yer;
            cmenddate = days + "-" + monthName(month) + "-" + yer;
            cmcustname = monthName(month) + "-" + yer;
          strtdate = ddate;
            enddate = ddate;
            cal.set(yer, month, i);
            daysofyear = cal.get(Calendar.DAY_OF_YEAR);
            cweek = cal.get(Calendar.WEEK_OF_YEAR);
            cwyear = cal.get(Calendar.YEAR);
            custname = "W-" + cweek + "-" + yer;
//        String dayis = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
  cal.setFirstDayOfWeek(Calendar.SUNDAY);  // Make sure that the calendar starts its week at Sunday
//            cal.setTime(new Date());
          cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
          cwstrtdate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            cwstrtmntdate = cwstrtdate;
            cal.add(Calendar.DAY_OF_YEAR, 6);
          cwenddate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            cwendmntdate = cwenddate;
            fullstring = ddate + "," + strtdate + "," + enddate + "," + daysofyear + "," + cweek + "," + cwyear + "," + cwstrtdate + "," + cwenddate + "," + cwstrtmntdate + "," + cwendmntdate + "," + custname;
            if (flag.equalsIgnoreCase("month")) {
                crntmnthstr = cmonth + "," + cwyear + "," + cmstrtdate + "," + cmenddate + "," + cmcustname;

//            ////////////////////////.println(crntmnthstr);
                crnmnthlist.add(crntmnthstr);
            }
            if (flag.equalsIgnoreCase("cweek")) {
//            ////////////////////////.println(fullstring);
                crntweklist.add(fullstring);
            }
            if (i == days) {
                i = 0;
                if (month == 11) {
                    month = 0;
                    yer = yer + 1;
                } else {
                    month = month + 1;
                }
                days = totaldays(yer, month + 1, day);
            }
            if (count == daysOfYear) {
                break;
            }
        }
        if (flag.equalsIgnoreCase("month")) {
            return crnmnthlist;
        } else {
            return crntweklist;
        }
    }

    public ArrayList prDayweek_old(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {


        ArrayList prDayweeklist = new ArrayList();
        Calendar cal = Calendar.getInstance();
        String ddate, strtdate, enddate, cwstrtdate, cwenddate, custname, cwstrtmntdate, cwendmntdate, prweekday;
        int daysOfYear, daysofyear, cweek, cwyear, i = 0, days = 0, count = 0;
       day = day - 7;
       if (day <= 0) {
            if (month == 0) {
                month = 11;
                yer = yer - 1;
                days = totaldays(yer, month, day);
             day = days + day;
         } else {
                month = month - 1;
                days = totaldays(yer, month, day);
         day = days + day;
            }
        }
        endday = endday - 7;
        if (endday <= 0) {
            if (endmonth == 0) {
                endmonth = 11;
                yer = yer - 1;
                days = totaldays(endyer, endmonth, endday);
       endday = days + endday;
            } else {
                endmonth = endmonth - 1;
                days = totaldays(endyer, endmonth, endday);
           endday = days + endday;
            }
        }
   days = totaldays(yer, month + 1, day);
        daysOfYear = gettotalnumofDays(day + "-" + monthName(month) + "-" + yer, endday + "-" + monthName(endmonth) + "-" + endyer);
    for (i = day; i <= days; i++) {
            count++;
            ddate = i + "-" + monthName(month) + "-" + yer;
        strtdate = ddate;
            enddate = ddate;
            cal.set(yer, month, i);
            daysofyear = cal.get(Calendar.DAY_OF_YEAR);
            cweek = cal.get(Calendar.WEEK_OF_YEAR);
            cwyear = cal.get(Calendar.YEAR);
            custname = "W-" + cweek + "-" + yer;
//        String dayis = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
           cal.setFirstDayOfWeek(Calendar.SUNDAY);  // Make sure that the calendar starts its week at Sunday
//            cal.setTime(new Date());
     cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
           cwstrtdate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            cwstrtmntdate = cwstrtdate;
            cal.add(Calendar.DAY_OF_YEAR, 6);
          cwenddate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            cwendmntdate = cwenddate;

            prweekday = ddate + "," + cwstrtdate + "," + cwenddate + "," + cwstrtmntdate + "," + cwendmntdate + "," + custname;
        prDayweeklist.add(prweekday);
            if (i == days) {
                i = 0;
                if (month == 11) {
                    month = 0;
                    yer = yer + 1;
                } else {
                    month = month + 1;
                }
                days = totaldays(yer, month + 1, day);
            }
            if (count == daysOfYear) {
            break;
            }
        }
        return prDayweeklist;
    }

    public ArrayList lastYearWeekDay(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {
        ArrayList lstyerweeklist = new ArrayList();
        Calendar cal = Calendar.getInstance();
        String lyrwekdate, lstyrstrtdate, lstyrenddate, lstyrcustname, lstyrstrtmntdate, lstyrendmntdate, lstyrweekday;
        int daysOfYear, daysofyear, cweek, cwyear, i = 0, days = 0, count = 0;

        day = day + 1;
        yer = yer - 1;
        days = totaldays(yer, month, day);
        if (day > days) {
            if (month == 11) {
                month = 0;
                yer = yer + 1;
                days = totaldays(yer, month, day);
      day = 1;
            } else {
                month = month + 1;
                day = 1;
            }
        }
        endday = endday + 1;
        endyer = endyer - 1;
        days = totaldays(endyer, endmonth, endday);
    if (endday > days) {
            if (endmonth == 11) {
                endmonth = 0;
                endyer = endyer + 1;
                days = totaldays(endyer, endmonth, endday);
         endday = 1;
            } else {
                endmonth = endmonth + 1;
                endday = 1;
            }
        }
        days = totaldays(yer, month + 1, day);
    daysOfYear = gettotalnumofDays(day + "-" + monthName(month) + "-" + yer, endday + "-" + monthName(endmonth) + "-" + endyer);
    for (i = day; i <= days; i++) {
            count++;
            lyrwekdate = i + "-" + monthName(month) + "-" + yer;
      cal.set(yer, month, i);
            daysofyear = cal.get(Calendar.DAY_OF_YEAR);
            cweek = cal.get(Calendar.WEEK_OF_YEAR);
            cwyear = cal.get(Calendar.YEAR);
            lstyrcustname = "W-" + cweek + "-" + yer;
//        String dayis = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
      cal.setFirstDayOfWeek(Calendar.SUNDAY);  // Make sure that the calendar starts its week at Sunday
//            cal.setTime(new Date());
   cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
   lstyrstrtdate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            lstyrstrtmntdate = lstyrstrtdate;
            cal.add(Calendar.DAY_OF_YEAR, 6);
     lstyrenddate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            lstyrendmntdate = lstyrenddate;

            lstyrweekday = lyrwekdate + "," + lstyrstrtdate + "," + lstyrenddate + "," + lstyrstrtmntdate + "," + lstyrendmntdate + "," + lstyrcustname;
   lstyerweeklist.add(lstyrweekday);
            if (i == days) {
                i = 0;
                if (month == 11) {
                    month = 0;
                    yer = yer + 1;
                } else {
                    month = month + 1;
                }
                days = totaldays(yer, month + 1, day);
            }
            if (count == daysOfYear) {
           break;
            }
        }
        return lstyerweeklist;
    }

    public ArrayList pyearmDay_old(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {
        ArrayList pyearmdaylist = new ArrayList();
        String getpryear[] = priorYear(day, month, yer, endday, endmonth, endyer);
        String[] yersarr;
        try {

            String startyear = getpryear[1];
            String endyear = getpryear[2];
 String yearstrt = startyear;
            String yearends = endyear;
            String yrstrtarr[];
            yrstrtarr = yearstrt.split("-");
            day = Integer.parseInt(yrstrtarr[0].toString());
            month = monthNumber(yrstrtarr[1].toString());
            yer = Integer.parseInt(yrstrtarr[2].toString());

            String yrendarr[];
            yrendarr = yearends.split("-");
            endday = Integer.parseInt(yrendarr[0].toString());
            endmonth = monthNumber(yrendarr[1].toString());
            endyer = Integer.parseInt(yrendarr[2].toString());
//            startyear = startyear.replace('/', '-');
//            endyear = endyear.replace('/', '-');
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.set(yer, month, day);
            cal2.set(endyer, endmonth, endday);
            long milis1 = cal1.getTimeInMillis();
            long milis2 = cal2.getTimeInMillis();
            long diff = milis2 - milis1;
            long diffDays = (diff / (24 * 60 * 60 * 1000)) + 1;
            int totalnumofdays = (int) diffDays;
      ArrayList prmnthlist = addPrMonth(yer, month, day, calenderId, endday, endmonth, endyer, monthName(month), "No", "No");
//            String pyeardata;
            StringBuilder pyeardata=new StringBuilder();
            // month=month+1;
            int i = 0, j = 0, days = totaldays(yer, month + 1, day), count = 0, k = 0;
         for (i = day; i <= days; i++) {
            k++;
                count++;

                pyeardata.append(i).append("-").append(monthName(month)).append("-").append(yer).append(",");
           if (k == totaldays(yer, month, day)) {
               j++;
                    k = 0;
                }


                yersarr = (String[]) prmnthlist.get(j);
                pyeardata.append(pyeardata).append(yersarr[0]).append(",").append(yersarr[1]).append(",").append(yersarr[2]);
      pyearmdaylist.add(pyeardata);
                if (j == 12) {
                    break;
                }
                if (count == totalnumofdays) {
                    break;
                }

                if (i == days) {
                    if (month == 11) {
                        month = 0;
                        i = 0;
                        yer = yer + 1;
                        days = totaldays(yer, month, day);
                    } else {
                        month = month + 1;
                        days = totaldays(yer, month, day);
                        i = 0;
                    }
    }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return pyearmdaylist;
    }

    public ArrayList pyearmDay(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {
        ArrayList pyearmdaylist = new ArrayList();
     String getpryear[] = priorYear(day, month + 1, yer, endday, endmonth + 1, endyer);
        String[] yersarr;
        try {

            String startyear = getpryear[1];
            String endyear = getpryear[2];
    String yearstrt = startyear;
            String yearends = endyear;
            String yrstrtarr[];
            yrstrtarr = yearstrt.split("-");
            day = Integer.parseInt(yrstrtarr[0].toString());
            month = monthNumber(yrstrtarr[1].toString());
            yer = Integer.parseInt(yrstrtarr[2].toString());

            String yrendarr[];
            yrendarr = yearends.split("-");
            endday = Integer.parseInt(yrendarr[0].toString());
            endmonth = monthNumber(yrendarr[1].toString());
            endyer = Integer.parseInt(yrendarr[2].toString());
//            startyear = startyear.replace('/', '-');
//            endyear = endyear.replace('/', '-');
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.set(yer, month, day);
            cal2.set(endyer, endmonth, endday);
            long milis1 = cal1.getTimeInMillis();
            long milis2 = cal2.getTimeInMillis();
            long diff = milis2 - milis1;
            long diffDays = (diff / (24 * 60 * 60 * 1000)) + 1;
            int totalnumofdays = (int) diffDays;
     ArrayList prmnthlist = addPrMonth(yer, month, day, calenderId, endday, endmonth, endyer, monthName(month), "No", "No");
  yersarr = new String[prmnthlist.size()];
//            String pyeardata;
            StringBuilder pyeardata=new StringBuilder();
//            month = month+1;
//            month = month+1;
            int i = 0, j = 0, days = totaldays(yer, month + 1, day), count = 0, k = 0;
     for (i = day; i <= days; i++) {
                k++;
                count++;
                pyeardata.append(i).append("-").append(monthName(month)).append("-").append(yer).append(",");
   if (j < prmnthlist.size()) {
                    yersarr = (String[]) prmnthlist.get(j);
                    pyeardata.append(pyeardata).append(yersarr[0]).append(",").append(yersarr[1]).append(",").append(monthName(month)).append("-").append(yer);
                 pyearmdaylist.add(pyeardata);
                }
                if (k == totaldays(yer, month + 1, day)) {
     j++;
                    k = 0;
                }

                if (count == totalnumofdays) {
                    break;
                }
                if (i == days) {
                    if (month == 11) {
                        month = 0;
                        i = 0;
                        yer = yer + 1;
                        days = totaldays(yer, month + 1, day);
           } else {
                        month = month + 1;
                        days = totaldays(yer, month + 1, day);
          i = 0;
                    }
            }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return pyearmdaylist;
    }

    public ArrayList pmonthDay(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {

        ArrayList pmonthdaylist = new ArrayList();
        try {
            int i = 0, j = 0, days = totaldays(yer, month + 1, day), count = 0, k = 0, totalnumofdays, pmonth = 0, crntdays = 0, l = 0;
            Calendar crntcal = Calendar.getInstance();
            crntcal.set(yer, month, day);
            Calendar pmontstrthcal = Calendar.getInstance();
            pmontstrthcal.set(yer, month, day);
            pmontstrthcal.add(Calendar.MONTH, -1);
            day = pmontstrthcal.get(Calendar.DAY_OF_MONTH);
            month = pmontstrthcal.get(Calendar.MONTH);
            yer = pmontstrthcal.get(Calendar.YEAR);
            Calendar pmontendhcal = Calendar.getInstance();
            pmontendhcal.set(endyer, endmonth, endday);
            //  totalnumofdays = gettotalnumofDays(day + "-" + monthName(month) + "-" + yer, endday + "-" + monthName(endmonth) + "-" + endyer);
//            String chktotalnumofdays = day + "-" + monthName(month) + "-" + yer + "," + endday + "-" + monthName(endmonth) + "-" + endyer;
        totalnumofdays = daysBetween(crntcal, pmontendhcal);
           pmontendhcal.add(Calendar.MONTH, -1);
            endday = pmontendhcal.get(Calendar.DAY_OF_MONTH);
            endmonth = pmontendhcal.get(Calendar.MONTH);
            endyer = pmontendhcal.get(Calendar.YEAR);
            String pmontdata, pmstrtdate, pmenddate, pmcustname, pmday;
            int crntyer = 0, crntmonth = 0, crntday = 0, crntmntdays = 0;
 for (i = day; i <= days; i++) {
                l++;
                k++;
                count++;
                crntyer = crntcal.get(Calendar.YEAR);
                crntmonth = crntcal.get(Calendar.MONTH);
                crntday = crntcal.get(Calendar.DAY_OF_MONTH);
          if (l > pmontstrthcal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    l = pmontstrthcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                }

                pmstrtdate = 1 + "-" + monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR);
                pmenddate = pmontstrthcal.getActualMaximum(Calendar.DAY_OF_MONTH) + "-" + monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR);
                pmcustname = monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR);
                pmontdata = l + "-" + monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR) + "," + pmstrtdate + "," + pmenddate + "," + pmcustname;
    pmonthdaylist.add(pmontdata);
                crntcal.add(Calendar.DATE, 1);
                crntdays = crntcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                if (count == totalnumofdays) {
                    break;
                }
                if (i == days) {
                    pmontstrthcal.add(Calendar.MONTH, 1);
                    l = 0;
                    days = crntcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    i = 0;
                }
            }

   } catch (Exception e) {
            logger.error("Exception:", e);
        }


        /*
         * try { // ////////////////////////.println(yer + "," + month + "," +
         * day + "," + calenderId + "," + endday + "," + endmonth + "," + endyer
         * + "," + monthName(month));
         *
         * Calendar pmontstrthcal = Calendar.getInstance();
         * pmontstrthcal.set(yer, month, day); pmontstrthcal.add(Calendar.MONTH,
         * -1);
         *
         * day = pmontstrthcal.get(Calendar.DAY_OF_MONTH); month =
         * pmontstrthcal.get(Calendar.MONTH); yer =
         * pmontstrthcal.get(Calendar.YEAR);
         *
         * Calendar pmontendhcal = Calendar.getInstance();
         * pmontendhcal.set(endyer, endmonth, endday);
         * pmontendhcal.add(Calendar.MONTH, -1); endday =
         * pmontendhcal.get(Calendar.DAY_OF_MONTH); endmonth =
         * pmontendhcal.get(Calendar.MONTH); endyer =
         * pmontendhcal.get(Calendar.YEAR);
         *
         * ArrayList prmnthlist = addPrMonth(yer, month, day, calenderId,
         * endday, endmonth, endyer, monthName(month), "No"); //
         * ////////////////////////.println(yer + "," + month + "," + day + ","
         * + calenderId + "," + endday + "," + endmonth + "," + endyer + "," +
         * monthName(month)); int i = 0, j = 0, days = totaldays(yer, month + 1,
         * day), count = 0, k = 0, totalnumofdays; totalnumofdays =
         * gettotalnumofDays(day + "-" + monthName(month) + "-" + yer, endday +
         * "-" + monthName(endmonth) + "-" + endyer); String pmontdata; String[]
         * yersarr; for (i = day; i <= days; i++) {
         *
         * k++; count++;
         *
         * ////////////////////////.print(i + "-" + monthName(month) + "-" + yer
         * + ","); yersarr = (String[]) prmnthlist.get(j);
         *
         * pmontdata = i + "-" + monthName(month) + "-" + yer + "," + yersarr[0]
         * + "," + yersarr[1] + "," + yersarr[2]; //////////////////
         * ////////////////////////.println("--m--" + pmontdata);
         * pmonthdaylist.add(pmontdata); if (count == totalnumofdays) { break; }
         * //////////////////// ////////////////////////.println("for k " +
         * totaldays(yer, month + 1, day) + "====for next month==" +
         * totaldays(yer, month, day));
         *
         * if (k == totaldays(yer, month + 1, day)) {
         *
         * j++; k = 0; } if (i == days) { if (month == 11) { month = 0; i = 0;
         * yer = yer + 1; days = totaldays(yer, month + 1, day); } else { month
         * = month + 1; days = totaldays(yer, month + 1, day); i = 0; } }
         *
         * }
         * // //////////////////////// ////////////////////////.println("count "
         * + count); } catch (Exception e) { logger.error("Exception:",e);
        }
         */
        return pmonthdaylist;
    }

    public ArrayList cqerweDay(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {
        ArrayList cqerwdaylist = new ArrayList();
        String cqerwedayconcat, splitqerwday[];
   String[] cqerwekday = quartersofgivendate(yer, month - 1, day, endday, endmonth - 1, endyer);//addpriorQrter(yer, month, day, calenderId, endyer, endmonth, endday, monthName(month), "NO");
        int limit = 0, i = 0;
        for (i = 0; i < cqerwekday.length; i++) {
            splitqerwday = cqerwekday[i].split(",");
      limit = gettotalnumofDays(splitqerwday[2].toString(), splitqerwday[3].toString());
      for (int j = 0; j < limit; j++) {
                //Added two columns for month number and year number for enhancement
                cqerwedayconcat = splitqerwday[0] + "," + splitqerwday[1] + "," + splitqerwday[2] + "," + splitqerwday[3] + "," + splitqerwday[4];
                //  cqerwedayconcat =  splitqerwday[2] + "," + splitqerwday[3] + "," + splitqerwday[5];
                cqerwdaylist.add(cqerwedayconcat);
          }
        }
        return cqerwdaylist;
    }

    public ArrayList clastqerweDay(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {
        ArrayList clastqerwdaylist = new ArrayList();
        String clastqerwedayconcat, splitlastqerwday[];
        Calendar calender = Calendar.getInstance();
        calender.set(yer, month, day);
        calender.add(Calendar.MONTH, -2);
        Calendar startdate = Calendar.getInstance();
        startdate.set(yer, month, day);
        startdate.add(Calendar.MONTH, -3);
        yer = startdate.get(Calendar.YEAR);
        month = startdate.get(Calendar.MONTH);
        day = startdate.get(Calendar.DATE);
        Calendar enddate = Calendar.getInstance();
        enddate.set(endyer, endmonth, endday);
        enddate.add(Calendar.MONTH, -3);
        endyer = enddate.get(Calendar.YEAR);
        endmonth = enddate.get(Calendar.MONTH);
        endday = enddate.get(Calendar.DATE);
        String[] clastqerwekday = quartersofgivendate(yer, month, day, endday, endmonth, endyer);//addpriorQrter(yer, month, day, calenderId, endyer, endmonth, endday, monthName(month), "YES");
        int limit = 0, i = 0;

        for (i = 0; i < clastqerwekday.length; i++) {
            splitlastqerwday = clastqerwekday[i].split(",");
   limit = gettotalnumofDays(splitlastqerwday[2].toString(), splitlastqerwday[3].toString());
            for (int j = 0; j < limit; j++) {
                String lqtrDate = calender.get(Calendar.DATE) + "-" + (calender.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + calender.get(Calendar.YEAR);
                calender.add(Calendar.DATE, 1);
                //               clastqerwedayconcat = splitlastqerwday[0] + "," + splitlastqerwday[1] + "," + splitlastqerwday[2] + "," + splitlastqerwday[3] + "," + splitlastqerwday[5];
                clastqerwedayconcat = lqtrDate + "," + splitlastqerwday[2] + "," + splitlastqerwday[3] + "," + splitlastqerwday[4];
                clastqerwdaylist.add(clastqerwedayconcat);
       }
        }
        return clastqerwdaylist;
    }

    public ArrayList clastyerqerDay(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {
        ArrayList clastyerqerdaylist = new ArrayList();
        Calendar lstyerqerdaystrt = Calendar.getInstance();
        lstyerqerdaystrt.set(yer, month, day);
        lstyerqerdaystrt.add(Calendar.YEAR, -1);

        day = lstyerqerdaystrt.get(Calendar.DATE);
        month = lstyerqerdaystrt.get(Calendar.MONTH);
        yer = lstyerqerdaystrt.get(Calendar.YEAR);

        Calendar lstyerqerdayend = Calendar.getInstance();
        lstyerqerdayend.set(endyer, endmonth, endday);
        lstyerqerdayend.add(Calendar.YEAR, -1);
        endday = lstyerqerdayend.get(Calendar.DATE);
        endmonth = lstyerqerdayend.get(Calendar.MONTH);
        endyer = lstyerqerdayend.get(Calendar.YEAR);

        String lstyerconcat, splitlastqerday[];
      //  String[] lstyerqterday = addpriorQrter(yer, month, day, calenderId, endyer, endmonth, endday, monthName(month), "NO");
        String[] lstyerqterday = quartersofgivendate(yer, month, day, endday, endmonth, endyer); //addpriorQrter(yer, month, day, calenderId, endyer, endmonth, endday, monthName(month), "NO");
        int limit = 0, i = 0;
        //added by bharathi reddy
        Calendar calender = Calendar.getInstance();
        calender.set(yer, month + 1, day);
        // calender.add(calender.YEAR,-1);
        for (i = 0; i < lstyerqterday.length; i++) {
         splitlastqerday = lstyerqterday[i].split(",");
        limit = gettotalnumofDays(splitlastqerday[2].toString(), splitlastqerday[3].toString());
         for (int j = 0; j < limit; j++) {
                //        lstyerconcat = splitlastqerday[0] + "," + splitlastqerday[1] + "," + splitlastqerday[2] + "," + splitlastqerday[3] + "," + splitlastqerday[5];

                String lqtrDate = calender.get(Calendar.DATE) + "-" + (calender.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + calender.get(Calendar.YEAR);
                lstyerconcat = lqtrDate + "," + splitlastqerday[2] + "," + splitlastqerday[3] + "," + splitlastqerday[4];
                calender.add(Calendar.DATE, 1);
                clastyerqerdaylist.add(lstyerconcat);
     }
        }

        return clastyerqerdaylist;
    }

    public ArrayList cyearDay(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {
        ArrayList cyeardaylist = new ArrayList();
 int loopedns = gettotalnumofDays(day + "-" + monthName(month) + "-" + yer, endday + "-" + monthName(endmonth) + "-" + endyer);
    for (int i = 0; i < loopedns; i++) {
            cyeardaylist.add(yer + "," + day + "-" + monthName(month) + "-" + yer + "," + endday + "-" + monthName(endmonth) + "-" + endyer + "," + yer);
       }
        return cyeardaylist;
    }

    public ArrayList lyearDay(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {
    ArrayList lyeardaylist = new ArrayList();

        Calendar lstyeardate = Calendar.getInstance();
        lstyeardate.set(yer, month, day);
        lstyeardate.add(Calendar.YEAR, -1);

        Calendar lstyerdaystrt = Calendar.getInstance();
        lstyerdaystrt.set(yer, month, day);
        lstyerdaystrt.add(Calendar.YEAR, -1);

        day = lstyerdaystrt.get(Calendar.DAY_OF_MONTH);
        month = lstyerdaystrt.get(Calendar.MONTH);
        yer = lstyerdaystrt.get(Calendar.YEAR);

        Calendar lstyerdayend = Calendar.getInstance();
        lstyerdayend.set(endyer, endmonth, endday);
        lstyerdayend.add(Calendar.YEAR, -1);

        endday = lstyerdayend.get(Calendar.DAY_OF_MONTH);
        endmonth = lstyerdayend.get(Calendar.MONTH);
        endyer = lstyerdayend.get(Calendar.YEAR);

        int lstyrloopedns = gettotalnumofDays(day + "-" + monthName(month) + "-" + yer, endday + "-" + monthName(endmonth) + "-" + endyer);
   for (int i = 0; i < lstyrloopedns; i++) {

            lyeardaylist.add(lstyeardate.get(Calendar.DATE) + "-" + (lstyeardate.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + lstyeardate.get(Calendar.YEAR) + "," + day + "-" + monthName(month) + "-" + yer + "," + endday + "-" + monthName(endmonth) + "-" + endyer + "," + yer);
            lstyerdayend.add(Calendar.YEAR, 1);
            lstyeardate.add(Calendar.DATE, 1);
    }
        return lyeardaylist;
    }

    //This method is not in use
    public static void prweektest(int day3, int month3, int year3, int eday, int emonth, int eyear) {

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(year3, month3, day3);
// cal1.add(Calendar.YEAR,-1);
// cal1.add(Calendar.MONTH,-cal1.getActualMaximum(Calendar.MONTH));
        cal2.set(eyear, emonth, eday);
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        long diff = milis2 - milis1;
        long diffDays = (diff / (7 * 24 * 60 * 60 * 1000));
        int weekno2 = (cal1.get(Calendar.WEEK_OF_YEAR)) - 1;
        for (int w = 1; w <= diffDays; w++) {
            cal1.set(year3, month3, day3);
//int weekno = cal1.get(Calendar.WEEK_OF_YEAR) - weekno2;
            int day4 = day3 + 6;
            cal2.set(year3, month3, day4);
            cal1.add(Calendar.DATE, -7);
            int weekno1 = cal1.get(Calendar.WEEK_OF_YEAR) - weekno2;
            if (weekno1 == 0) {
                weekno1 = 52;
            }
          cal2.add(Calendar.DATE, -7);
            day3 = day3 + 7;
        }
    }

    public String[] getpriorweeks(int day, int month, int yer, int endday, int endmonth, int endyer, String flag) {
     
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        int numofweeks = getNumWeeksForYear(yer);
        String detsstr = "";
        String detsList[] = new String[numofweeks - 1];
        String strDate = "";
        String endDate = "";
        int weekNo = 0;
        for (int w = 1; w < numofweeks; w++) {
    weekNo = w;
            cal1.set(yer, month, day);
            if (flag.equalsIgnoreCase("yes")) {
                cal1.add(Calendar.DATE, -7);
            }
            cal2.set(yer, month, day);
            if (flag.equalsIgnoreCase("yes")) {
                cal2.add(Calendar.DATE, -7);
            }
            strDate = cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR);
            cal2.add(Calendar.DATE, 6);
            endDate = cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR);
            day = day + 7;
            detsstr = weekNo + "," + cal1.get(Calendar.YEAR) + "," + strDate + "," + endDate + "," + gettotalnumofDays(strDate, endDate);
         detsList[w - 1] = detsstr;
        }
        return detsList;

    }

    private int gettotalnumofDays(String startdate, String enddate) {


        String[] monthName = {"JAN", "FEB",
            "MAR", "APR", "MAY", "JUN", "JUL",
            "AUG", "SEP", "OCT", "NOV",
            "DEC"};
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
 String yrstrtarr[];
        yrstrtarr = startdate.split("-");

        int strtday = Integer.parseInt(yrstrtarr[0].toString());
        String strtmonth = yrstrtarr[1];
        int i = 0;
        for (i = 0; i < monthName.length; i++) {
            if (strtmonth.equalsIgnoreCase(monthName[i])) {
          break;
            }
        }
        int strtyear = Integer.parseInt(yrstrtarr[2].toString());
 String yrendarr[];
        yrendarr = enddate.split("-");
        int endday = Integer.parseInt(yrendarr[0].toString());
        String endmnth = yrendarr[1];
        int j = 0;
        for (j = 0; j < monthName.length; j++) {
            if (endmnth.equalsIgnoreCase(monthName[j])) {
         break;
            }
        }
        int endyer = Integer.parseInt(yrendarr[2].toString());
   cal1.set(strtyear, i, strtday);
        cal2.set(endyer, j, endday);
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        long diff = milis2 - milis1;
        long diffDays = (diff / (24 * 60 * 60 * 1000)) + 1;
     int days = (int) diffDays;
       return days;
    }

    public int getNumWeeksForYear(int year) {
        Calendar c = Calendar.getInstance();
        c.set(year, 0, 1);
     return c.getMaximum(Calendar.WEEK_OF_YEAR);
    }

    public boolean chkLeapyear(int year) {
//        int year = Integer.parseInt(args[0]);
        boolean isLeapYear;

        // divisible by 4
        isLeapYear = (year % 4 == 0);

        // divisible by 4 and not 100
        isLeapYear = isLeapYear && (year % 100 != 0);

        // divisible by 4 and not 100 unless divisible by 400
        isLeapYear = isLeapYear || (year % 400 == 0);

      return isLeapYear;
    }

    public int totaldays(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month - 1, date);
        int idays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
  return idays;
    }

    public String monthName(int month) {
        String mnthname = "";
        String[] monthName = {"JAN", "FEB",
            "MAR", "APR", "MAY", "JUN", "JUL",
            "AUG", "SEP", "OCT", "NOV",
            "DEC"};
        mnthname = monthName[month];
        return mnthname;
    }

    public int monthNumber(String monthName) {
        int monthNumber = 0;
        String[] monthNamearr = {"JAN", "FEB",
            "MAR", "APR", "MAY", "JUN", "JUL",
            "AUG", "SEP", "OCT", "NOV",
            "DEC"};
        for (int i = 0; i < monthNamearr.length; i++) {
            if (monthNamearr[i].equalsIgnoreCase(monthName)) {
                monthNumber = i;
            }
        }
        return monthNumber;
    }
//This Method is not in use
       /*
     * public void addPrMonth(int year, String month, int day, int calenderId,
     * int endyear, String endMonth, int endDay) { try { String finalQuery = "";
     * ArrayList Createlist = new ArrayList(); String createDenomTables =
     * resBundle.getString("createDenomTables"); Object obj1[] = new Object[2];
     * obj1[0] = "PR_MONTH_" + calenderId; obj1[1] = "MONTH_NO NUMBER, DYEAR
     * NUMBER, START_DATE DATE, END_DATE DATE, MONTH_NAME VARCHAR2(255 BYTE),
     * CUST_NAME VARCHAR2(255 BYTE)"; finalQuery =
     * pbdb.buildQuery(createDenomTables, obj1); Createlist.add(finalQuery);
     * String addPrYear = resBundle.getString("addPrYear"); obj1 = new
     * Object[5]; obj1[0] = calenderId; obj1[1] = year; obj1[2] = day + "-" +
     * month + "-" + year; obj1[3] = endDay + "-" + endMonth + "-" + endyear;
     * obj1[4] = year; finalQuery = pbdb.buildQuery(addPrYear, obj1);
     * ////////////////////////// ////////////////////////.println("PR YEAR
     * finalQuery---" + finalQuery); Createlist.add(finalQuery); //
     * pbdb.executeMultiple(Createlist); } catch (Exception ex) {
     * logger.error("Exception:",ex); } }
     */

    public ArrayList cWeek(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId, String flag) {

        ArrayList crntweklist = new ArrayList();
        ArrayList crnmnthlist = new ArrayList();
        Calendar cal = Calendar.getInstance();
        cal.set(yer, month, day);
        int startDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        String ddate, strtdate, enddate, cwstrtdate, cwenddate, custname, cwstrtmntdate, cwendmntdate, fullstring, cmstrtdate, cmenddate, cmcustname, crntmnthstr;
        int dayOfYear, cweek, cwyear, i = 0, days = 0, count = 0, cmonth;
        days = totaldays(yer, month + 1, day);
        // daysOfYear = gettotalnumofDays(day + "-" + monthName(month) + "-" + yer, endday + "-" + monthName(endmonth) + "-" + endyer);
        int daysInMent = 0;
        for (i = day; i <= days; i++) {
            daysInMent++;
            count++;

            ddate = i + "-" + monthName(month) + "-" + yer;
            cmonth = month + 1;
            cmstrtdate = 1 + "-" + monthName(month) + "-" + yer;
            cmenddate = days + "-" + monthName(month) + "-" + yer;
            cmcustname = monthName(month) + "-" + yer;
            strtdate = ddate;
            enddate = ddate;
            cal.set(yer, month, i);
            dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
            if (dayOfYear >= startDayOfYear) {
                dayOfYear = dayOfYear - (startDayOfYear - 1);
            } else if (dayOfYear < startDayOfYear) {

                dayOfYear = noOfdaysinYear - (startDayOfYear + dayOfYear + 1);
            }
            cweek = cal.get(Calendar.WEEK_OF_YEAR);
            cwyear = cal.get(Calendar.YEAR);
            custname = "W-" + cweek + "-" + yer;
            cal.setFirstDayOfWeek(Calendar.SUNDAY); // Make sure that the calendar starts its week at Sunday
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

            cwstrtdate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            cwstrtmntdate = cwstrtdate;
            if (month != cal.get(Calendar.MONTH)) {
                cwstrtmntdate = 1 + "-" + monthName(month) + "-";
                if (yer != cal.get(Calendar.YEAR)) {
                    cwstrtmntdate = cwstrtmntdate + yer;
                } else {
                    cwstrtmntdate = cwstrtmntdate + cal.get(Calendar.YEAR);
                }
            }


            cal.add(Calendar.DAY_OF_YEAR, 6);

            cwenddate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            cwendmntdate = cwenddate;
            if (month != cal.get(Calendar.MONTH)) {
                cal.add(Calendar.MONTH, -1);
                cwendmntdate = cal.getActualMaximum(Calendar.DAY_OF_MONTH) + "-" + monthName(month) + "-";
  cal.add(Calendar.MONTH, 1);
                if (yer != cal.get(Calendar.YEAR)) {
                    cwendmntdate = cwendmntdate + yer;
                } else {
                    cwendmntdate = cwendmntdate + cal.get(Calendar.YEAR);
                }
            }

            fullstring = ddate + "," + strtdate + "," + enddate + "," + daysInMent + "," + cweek + "," + cwyear + "," + cwstrtdate + "," + cwenddate + "," + cwstrtmntdate + "," + cwendmntdate + "," + custname;
            //  
            if (flag.equalsIgnoreCase("month")) {
                crntmnthstr = cmonth + "," + cwyear + "," + cmstrtdate + "," + cmenddate + "," + cmcustname;
        crnmnthlist.add(crntmnthstr);
            }
            if (flag.equalsIgnoreCase("cweek")) {
      crntweklist.add(fullstring);
            }
            if (i == days) {
                i = 0;
                if (month == 11) {
                    month = 0;
                    yer = yer + 1;
                } else {
                    month = month + 1;
                }
                days = totaldays(yer, month + 1, day);
            }
            if (count == noOfdaysinYear) {
                break;
            }

        }
        if (flag.equalsIgnoreCase("month")) {
            return crnmnthlist;
        } else {
            return crntweklist;
        }
    }

    public void pm_Day(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {
        try {

            Calendar crntcal = Calendar.getInstance();
            crntcal.set(yer, month, day);

            Calendar pmontstrthcal = Calendar.getInstance();
            pmontstrthcal.set(yer, month, day);
            pmontstrthcal.add(Calendar.MONTH, -1);

            day = pmontstrthcal.get(Calendar.DAY_OF_MONTH);
            month = pmontstrthcal.get(Calendar.MONTH);
            yer = pmontstrthcal.get(Calendar.YEAR);

            Calendar pmontendhcal = Calendar.getInstance();
            pmontendhcal.set(endyer, endmonth, endday);
            pmontendhcal.add(Calendar.MONTH, -1);
            endday = pmontendhcal.get(Calendar.DAY_OF_MONTH);
            endmonth = pmontendhcal.get(Calendar.MONTH);
            endyer = pmontendhcal.get(Calendar.YEAR);

            int i = 0, j = 0, days = totaldays(yer, month + 1, day), count = 0, k = 0, totalnumofdays, pmonth = 0, crntdays = 0, l = 0;
            totalnumofdays = gettotalnumofDays(day + "-" + monthName(month) + "-" + yer, endday + "-" + monthName(endmonth) + "-" + endyer);
            String pmontdata, pmstrtdate, pmenddate, pmcustname, pmday;
            int crntyer, crntmonth, crntday, crntmntdays;

            for (i = day; i <= days; i++) {
                l++;
                k++;
                count++;
                crntyer = crntcal.get(Calendar.YEAR);
                crntmonth = crntcal.get(Calendar.MONTH);
                crntday = crntcal.get(Calendar.DAY_OF_MONTH);
 if (l > pmontstrthcal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    l = pmontstrthcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                }

                pmstrtdate = 1 + "-" + monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR);
                pmenddate = pmontstrthcal.getActualMaximum(Calendar.DAY_OF_MONTH) + "-" + monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR);
                pmcustname = monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR);
                pmontdata = l + "-" + monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR) + "," + pmstrtdate + "," + pmenddate + "," + pmcustname;
         crntcal.add(Calendar.DATE, 1);
                crntdays = crntcal.getActualMaximum(Calendar.DAY_OF_MONTH);


                if (count == totalnumofdays) {
                    break;
                }

                if (i == days) {
                    pmontstrthcal.add(Calendar.MONTH, 1);
                    l = 0;
                    days = crntcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    i = 0;
                }
            }
  } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public void buildHolidayCalender(int yer, int mnth, int day, int endyer, int endmnth, int endday, String[] monthName, int calenderId, String wekholiday1, String wekholiday2, String[] listofholidays) {
        int j = 0;
        int count = 0;
        int nxtmnt = 0;
        int check = 0;
        int forcount = 11;
        Calendar cal = Calendar.getInstance();
        String finalQuery = "", workstatus = "W";
        ArrayList Createlist = new ArrayList();
        String createDenomTables = resBundle.getString("createDenomTables");
        Object obj1[] = new Object[2];
        obj1[0] = "PR_DAYS_" + calenderId;
        obj1[1] = "DDATE DATE,START_DATE DATE, END_DATE DATE , DAYOFYEAR NUMBER, DAYOFMONTH  NUMBER,WORKING_STATUS";
        finalQuery = pbdb.buildQuery(createDenomTables, obj1);
        Createlist.add(finalQuery);
        String addPrDay = resBundle.getString("addPrDay");
        for (j = mnth; j <= forcount; j++) {
            int days = totaldays(yer, j + 1, day);
            for (int i = day; i <= days; i++) {
                count++;
                //code to insert date prdays
                cal.set(yer, j, i);
                obj1 = new Object[7];
                obj1[0] = calenderId;
                obj1[1] = i + "-" + monthName[j] + "-" + yer;
                obj1[2] = i + "-" + monthName[j] + "-" + yer;
                obj1[3] = i + "-" + monthName[j] + "-" + yer;
                obj1[4] = count;
                obj1[5] = i;
                String nameofday = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
             if (nameofday.equalsIgnoreCase(wekholiday1) || nameofday.equalsIgnoreCase(wekholiday2)) {
           workstatus = "L";
                } else {
                    workstatus = "W";
                }
                for (int k = 0; k < listofholidays.length; k++) {
                    if (listofholidays[k].equalsIgnoreCase(obj1[1].toString())) {
                        workstatus = "L";
                    }
                }
                obj1[6] = workstatus;
                finalQuery = pbdb.buildQuery(addPrDay, obj1);
       Createlist.add(finalQuery);
           if (yer == endyer && j == endmnth && i == endday) {
                    break;
                }
                if (i == days) {
                    day = 1;
                    days = totaldays(yer, j + 1, day);


                    if (j == monthName.length - 1) {
                        mnth = 0;

                        if (check == 0) {
                            j = -1;
                            check = 1;
                            forcount = endmnth;

                            nxtmnt = 1;
                yer = yer + 1;
                            days = totaldays(yer, mnth + 1, day);
                        }
                    }
                }
            }
        }
        try {
//            pbdb.executeMultipleCustomer(Createlist);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
 }

    public String[] multipleyears(int yer, int mnth, int day, int endyer, int endmnth, int endday) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(yer, mnth, day);
        cal2.set(endyer, endmnth, endday);
        int noofyrs = daysBetween(cal1, cal2) / 365;
       int limit = noofyrs;
        int noofdays = daysBetween(cal1, cal2);
       if (noofdays == 366 || noofdays == 365) {
            limit = 1;
        }
       String[] strtyear = new String[limit];
        String[] endyear = new String[limit];
        String[] totalyrs = new String[limit];

        for (int i = 0; i < limit; i++) {

            if (cal1.get(Calendar.YEAR) != endyer && cal1.get(Calendar.MONTH) != endmnth) {
              strtyear[i] = cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR);
                cal1.add(Calendar.MONTH, 12);
                cal1.add(Calendar.DATE, -1);
   endyear[i] = cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR);
                cal1.add(Calendar.DATE, 1);
                totalyrs[i] = strtyear[i] + "@" + endyear[i];
        } else if (cal1.get(Calendar.MONTH) != endmnth && noofdays == 366 || noofdays == 365) {
             strtyear[i] = cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR);
                cal1.add(Calendar.MONTH, 12);
                cal1.add(Calendar.DATE, -1);
        endyear[i] = cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR);
                cal1.add(Calendar.DATE, 1);
                totalyrs[i] = strtyear[i] + "@" + endyear[i];
           }
        }
        return totalyrs;
    }

    public int diffdates(int strtyer, int strtmnt, int strtday, int endyer, int endmnt, int endday) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.set(strtyer, strtmnt, strtday);
        calendar2.set(endyer, endmnt, endday);
        long milliseconds1 = calendar1.getTimeInMillis();
        long milliseconds2 = calendar2.getTimeInMillis();
        long diff = milliseconds2 - milliseconds1;
//        long diffSeconds = diff / 1000;
//        long diffMinutes = diff / (60 * 1000);
//        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);
      return (int) diffDays;
    }

    public static void prYear(int day3, int month3, int year3, int eday, int emonth, int eyear) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(year3, month3, day3);
        cal2.set(year3, month3, day3);
  int pyear = cal1.get(Calendar.YEAR);
        int pmonth = cal1.get(Calendar.MONTH);
        int pday = cal1.get(Calendar.DATE);

        for (int i = 1; i <= 12; i++) {
      cal1.add(Calendar.MONTH, 12);
            cal1.add(Calendar.DATE, -1);
           cal1.add(Calendar.DATE, 1);
            if (cal1.get(Calendar.MONTH) == emonth && cal1.get(Calendar.YEAR) == eyear) {
                break;
            }
        }
    }

    public int daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        int daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        daysBetween = daysBetween + 1;
    return daysBetween;
    }

    public String[] quartersofgivendate(int yer, int month, int day, int endday, int endmonth, int endyer) {
      String quarterarr[] = new String[4];
        month = month + 1;
        endmonth = endmonth + 1;
//        String concat = "";
        StringBuilder concat=new StringBuilder();
        int strtyer = 0;
        Calendar strtcal = Calendar.getInstance();
        Calendar endcal = Calendar.getInstance();
        strtcal.set(yer, month, day);
        endcal.set(yer, month, day);
        endcal.add(Calendar.MONTH, 3);
//        endcal.add(Calendar.DATE, -1);
        for (int i = 0; i < 4; i++) {
            concat.append(i + 1).append(",").append(strtcal.get(Calendar.YEAR)).append(",");
            String strtdate = strtcal.get(Calendar.DATE) + "-" + strtcal.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault()) + "-" + strtcal.get(Calendar.YEAR);
            concat.append(concat).append(strtdate);
            strtyer = strtcal.get(Calendar.YEAR);
       strtcal.add(Calendar.MONTH, 3);
            endcal.add(Calendar.DATE, -1);
            String enddate = endcal.get(Calendar.DATE) + "-" + endcal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + endcal.get(Calendar.YEAR);
            endcal.add(Calendar.DATE, 1);
            concat.append(concat).append(",").append(enddate).append("," + "Q").append(i + 1).append("-").append(strtyer);
      endcal.add(Calendar.MONTH, 3);
            quarterarr[i] = concat.toString();
 
        }
      return quarterarr;
    }

    public ArrayList prDayWeek_new(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {

        ArrayList prDayweeklist = new ArrayList();
        String ddate = null, custname = null, cwstrtdate = null, cwstrtmntdate = null, cwenddate = null, cwendmntdate = null, prDayWeek = null;
        Calendar strtcal = Calendar.getInstance();
        Calendar dayByDayCal = Calendar.getInstance();
        Calendar endcal = Calendar.getInstance();

        strtcal.set(yer, month, day);
        dayByDayCal.set(yer, month, day);
        endcal.set(endyer, endmonth, endday);
  
        int totalDays = daysBetween(strtcal, endcal), i = 0;
       strtcal.set(Calendar.DATE, -6);
        dayByDayCal.set(Calendar.DATE, -7);
        endcal.set(Calendar.DATE, -6);
     
        for (i = 0; i < totalDays; i++) {
            ddate = dayByDayCal.get(Calendar.DATE) + "-" + (dayByDayCal.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + dayByDayCal.get(Calendar.YEAR);
            custname = "W-" + strtcal.get(Calendar.WEEK_OF_YEAR) + "-" + strtcal.get(Calendar.YEAR);
            strtcal.setFirstDayOfWeek(Calendar.SUNDAY);
            strtcal.set(Calendar.DAY_OF_WEEK, strtcal.getFirstDayOfWeek());
            cwstrtdate = strtcal.get(Calendar.DATE) + "-" + monthName(strtcal.get(Calendar.MONTH)) + "-" + strtcal.get(Calendar.YEAR);
            cwstrtmntdate = cwstrtdate;
            strtcal.add(Calendar.DATE, 6);
            cwenddate = strtcal.get(Calendar.DATE) + "-" + monthName(strtcal.get(Calendar.MONTH)) + "-" + strtcal.get(Calendar.YEAR);
            cwendmntdate = cwenddate;
            prDayWeek = ddate + "," + cwstrtdate + "," + cwenddate + "," + cwstrtmntdate + "," + cwendmntdate + "," + custname;
//            prDayWeek =cwstrtdate + "," + cwenddate + "," + cwstrtmntdate + "," + cwendmntdate + "," + custname;
            prDayweeklist.add(prDayWeek);
          dayByDayCal.add(Calendar.DATE, 1);
            strtcal.add(Calendar.DATE, 1);
        }
       return prDayweeklist;
    }

    public long getTotalNumberOfDays(Calendar calendar1, Calendar calendar2) {

        long milis1 = calendar1.getTimeInMillis();
        long milis2 = calendar2.getTimeInMillis();
        long diff = milis2 - milis1;
//        long diffSeconds = diff / 1000;
//
//        long diffMinutes = diff / (60 * 1000);
//         long diffHours = diff / (60 * 60 * 1000);

        return (diff / (24 * 60 * 60 * 1000)) + 1;
    }
}
