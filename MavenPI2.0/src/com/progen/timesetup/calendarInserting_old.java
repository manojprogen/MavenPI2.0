package com.progen.timesetup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import org.apache.log4j.Logger;
import prg.db.PbDb;

public class calendarInserting_old {
    //Hello this is an oldfile

    public static Logger logger = Logger.getLogger(calendarInserting_old.class);

    public static void main(String args[]) {
        try {
            calendarInserting_old clmthds = new calendarInserting_old();
            TimeSetUpResourceBundle tmsetup = new TimeSetUpResourceBundle();
            String strtdate, enddate;
            strtdate = "4-1-2004";//"4-1-2005";
            enddate = "3-31-2011";//"3-31-2006";
            Calendar cal = Calendar.getInstance();
            cal.set(2004, 3, 1);
            int totyrdays = cal.getActualMaximum(cal.DAY_OF_YEAR);
//            ////////////////////////////////////.println("totyrdays" + totyrdays);
            if (totyrdays == 366) {
            }
            int numofdays = clmthds.gettotalnumofDays("1-3-2004", "31-2-2011");//("1-3-2005", "31-2-2006");
//            ////////////////////////////////////.println("numofdays" + numofdays);
            strtdate = strtdate.replace('-', '/');
            enddate = enddate.replace('-', '/');
//            clmthds.insertcal(strtdate, enddate, 109);       //date format is mm/dd/yyyy
            ////////////////////////////////////.println("strtdate" + strtdate);
            ////////////////////////////////////.println("enddate" + enddate);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }
    TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
    PbDb pbdb = new PbDb();
    ArrayList list = new ArrayList();

    public void insertcal(String startyear, String endyear, int calenderId) throws Exception {
        //code to create tables
        // PbDb pbdb=new PbDb();
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


        //call to add PRYEAR
        String[] listholidays = {"26-JAN-2006", "14-FEB-2006", "1-MAY-2006", "15-AUG-2006", "22-AUG-2006", "18-JAN-2006", "11-JUN-2006", "26-DEC-2006", "17-JUN-2006", "9-AUG-2006"};
//        buildHolidayCalender(yer, mnth, day, endyer, endmnth, endday, monthName, calenderId,"SUNDAY","MONDAY",listholidays);
        addPrYear(yer, month, day, calenderId, endyer, endmonth, endday);
        addPrQtr(yer, mnth, day, calenderId, endyer, endmnth, endday, month, "yes");
        addPrMonth(yer, mnth, day, calenderId, endyer, endmnth, endday, month, "yes");
        addPrWeek(calenderId, yer, mnth, day, endyer, endmnth, endday, month);
        if (yer == endyer) {
            ////////////////////////// //////////////////////////////////////////////////////.println("in if");
            buildCalender(yer, mnth, day, endyer, endmnth, endday, monthName, calenderId);
        } else if (day - 1 == endday && yer < endyer && mnth == endmnth && endyer == yer + 1) {
            ////////////////////////// //////////////////////////////////////////////////////.println("in else");
            buildCalender(yer, mnth, day, endyer, endmnth, endday, monthName, calenderId);
        }
        addPrYearDenom(yer, mnth, day, calenderId, endyer, endmnth, endday, month, endmonth);
        addQrterDenom(yer, mnth, day, calenderId, endyer, endmnth, endday, month);
        addPrMonthDenom(yer, mnth, day, calenderId, endyer, endmnth, endday, month);
        addPrWeekDenom(yer, mnth, day, calenderId, endyer, endmnth, endday, month, endmonth);
//        String[] prirowekdetails = prweek(day, mnth, yer, endday, endmnth, endyer);
//        String getpriorwekarr[] = prioryearWek(day, mnth, yer, endday, endmnth, endyer);
//        //////////////////////////////////////////////////////.println(yer + "," + mnth + "," + day + "," + calenderId + "," + endyer + "," + endmnth + "," + endday + "," + month + "," + endmonth);
        addPrDayDenom(yer, mnth, day, calenderId, endyer, endmnth, endday, month, endmonth);
//         cWeek(yer, mnth, day, endday, endmnth, endyer, calenderId, "cweek");

//        for (int i = 0; i < getpriorwekarr.length; i++) {
//            ////////////////////////////////////////////////////////.println("at Last priro Week Query\t" + getpriorwekarr[i]);
//        }
//        prweektest(day, mnth, yer, endday, endmnth, endyer);


    }

    public void buildCalender(int yer, int mnth, int day, int endyer, int endmnth, int endday, String[] monthName, int calenderId) {
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
        obj1[0] = "PR_DAYS_" + calenderId;
        obj1[1] = "DDATE DATE,START_DATE DATE, END_DATE DATE , DAYOFYEAR NUMBER, DAYOFMONTH  NUMBER,WORKING_STATUS VARCHAR2(20 BYTE),DAY_NAME VARCHAR2(20 BYTE),HOLIDAY_REASON VARCHAR2(250 BYTE)";
        finalQuery = pbdb.buildQuery(createDenomTables, obj1);
        //////////////////////////////////////////////////////.println("PR DAY finalQuery---" + finalQuery);
        Createlist.add(finalQuery);
        String addPrDay = resBundle.getString("addPrDay");
        for (j = mnth; j <= forcount; j++) {
            int days = totaldays(yer, j + 1, day);
            for (int i = day; i <= days; i++) {
                count++;
                //code to insert date prdays
                cal.set(yer, j, i);
                obj1 = new Object[8];
                obj1[0] = calenderId;
                obj1[1] = i + "-" + monthName[j] + "-" + yer;
                obj1[2] = i + "-" + monthName[j] + "-" + yer;
                obj1[3] = i + "-" + monthName[j] + "-" + yer;
                obj1[4] = count;
                obj1[5] = i;
                obj1[6] = "W";
                obj1[7] = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                finalQuery = pbdb.buildQuery(addPrDay, obj1);
                //////////////////////////////////////////////////////.println("PR DAY finalQuery---" + finalQuery);
                Createlist.add(finalQuery);
                ////////////////////////////////////////////////////////.println("Dates Are---" + i + "-" + monthName[j] + "-" + yer);
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
//                           //////////////////////// //////////////////////////////////////////////////////.println("mnt" + mnth);
                            yer = yer + 1;
                            days = totaldays(yer, mnth + 1, day);
                        }
                    }
                }
            }
        }
        try {
            pbdb.executeMultipleCustomer(Createlist);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
//       //////////////////////// //////////////////////////////////////////////////////.println("count is" + count);
    }

    public void addPrYear(int year, String month, int day, int calenderId, int endyear, String endMonth, int endDay) {
        //////////////////////// //////////////////////////////////////////////////////.println("--------------------------------------------------------");
        try {
            String finalQuery = "";
            ArrayList Createlist = new ArrayList();
            String createDenomTables = resBundle.getString("createDenomTables");
            Object obj1[] = new Object[2];
            obj1[0] = "PR_YEAR_" + calenderId;
            obj1[1] = "PYEAR NUMBER, 	START_DATE DATE, 	END_DATE DATE, 	CUST_YEAR VARCHAR2(255 BYTE)";
            finalQuery = pbdb.buildQuery(createDenomTables, obj1);
            //////////////////////// //////////////////////////////////////////////////////.println("create table PR YEAR finalQuery---\n" + finalQuery);
            Createlist.add(finalQuery);
            String addPrYear = resBundle.getString("addPrYear");
            obj1 = new Object[5];
            obj1[0] = calenderId;
            obj1[1] = year;
            obj1[2] = day + "-" + month + "-" + year;
            obj1[3] = endDay + "-" + endMonth + "-" + endyear;
            obj1[4] = year;
            finalQuery = pbdb.buildQuery(addPrYear, obj1);
            //////////////////////////////////////////////////////.println("PR YEAR finalQuery---" + finalQuery);
            Createlist.add(finalQuery);
            pbdb.executeMultipleCustomer(Createlist);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public String[] addPrQtr(int yer, int month, int day, int calenderId, int endyear, int endMonth, int endDay, String monthName, String flag) {
        //////////////////////// //////////////////////////////////////////////////////.println("--------------------------------------------------------");
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
                //////////////////////// //////////////////////////////////////////////////////.println("create table" + finalQuery);
                Createlist.add(finalQuery);
            }
            //String addPrYear = resBundle.getString("addPrQtr");
            Calendar calendar = Calendar.getInstance();
            calendar.set(yer, month, day);
            String strdate = day + "-" + monthName + "-" + yer;
            String endDate = "";
            String dbyear = String.valueOf(yer);
            int extradays = totaldays(yer, (month + 1), day) - day;

            String addPrQtr = resBundle.getString("addPrQtr");
            obj1 = new Object[6];
            obj1[0] = calenderId;

            String qtrdets = "";
            for (int i = 1; i <= 4; i++) {
                //////////////////////// //////////////////////////////////////////////////////.println("----" + i);
                obj1[1] = i;
                calendar.add(Calendar.MONTH, 3);
                obj1[2] = dbyear;
                obj1[3] = strdate;
                qtrdets = i + "," + dbyear + "," + strdate;
                String sendstartdate = strdate;
                // if (day >= 28) {
                // endDate=(calendar.getActualMaximum(calendar.DAY_OF_MONTH))-extradays+ "-" + (calendar.get(2) + 1) + "-" + calendar.get(1);
                // //////////////////////// //////////////////////////////////////////////////////.println("strartdate==="+strdate+"   enddate----"+endDate);
                //  strdate=((calendar.getActualMaximum(calendar.DAY_OF_MONTH))-extradays+1)+ "-" + (calendar.get(2) + 1) + "-" + calendar.get(1);
                // endDate = (calendar.getActualMaximum(calendar.DAY_OF_MONTH)) - extradays + "-" + calendar.getDisplayName(2, calendar.SHORT, Locale.getDefault()) + "-" + calendar.get(1);
                ////////////////////////// //////////////////////////////////////////////////////.println("strartdate==="+strdate+"   enddate----"+endDate);
                //   strdate = ((calendar.getActualMaximum(calendar.DAY_OF_MONTH)) - extradays + 1) + "-" + calendar.getDisplayName(2, calendar.SHORT, Locale.getDefault()) + "-" + calendar.get(1);
                //  dbyear = String.valueOf(calendar.get(1));
                //  } else {
                //  endDate=calendar.get(5)+ "-" + (calendar.get(2)+1) + "-" + calendar.get(1);
                ////////////////////////// //////////////////////////////////////////////////////.println("strartdate==="+strdate+"   enddate----"+endDate);
                // calendar.add(Calendar.DATE,1);
                //  strdate=(calendar.get(5))+ "-" + (calendar.get(2)+1) + "-" + calendar.get(1);
                endDate = calendar.get(5) + "-" + calendar.getDisplayName(2, calendar.SHORT, Locale.getDefault()) + "-" + calendar.get(1);
                ////////////////////////// //////////////////////////////////////////////////////.println("strartdate==="+strdate+"   enddate----"+endDate);
                calendar.add(Calendar.DATE, 1);
                strdate = (calendar.get(5)) + "-" + calendar.getDisplayName(2, calendar.SHORT, Locale.getDefault()) + "-" + calendar.get(1);
                dbyear = String.valueOf(calendar.get(1));
                calendar.add(Calendar.DATE, -1);
                //}
                int totday = gettotalnumofDays(sendstartdate, endDate);
                obj1[4] = endDate;
                obj1[5] = "Q-" + i + "-" + dbyear;
                qtrdets += "," + endDate + "," + totday;
                QtrList[i - 1] = qtrdets;

                finalQuery = pbdb.buildQuery(addPrQtr, obj1);
//          /////////////////      //////////////////////////////////////////////////////.println("PR Qtr finalQuery---" + finalQuery);
                Createlist.add(finalQuery);
            }
            if (flag.equalsIgnoreCase("yes")) {

                pbdb.executeMultipleCustomer(Createlist);
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return QtrList;
    }

    public String[] addPrWeek(int calenderId, int year, int month, int day, int endyer, int endmnth, int endday, String monthName) {
        //////////////////////// //////////////////////////////////////////////////////.println("--------------------------------------------------------");
        String weekdets[] = null;
        try {

//           //////////////////////// //////////////////////////////////////////////////////.println("month---" + month);
            String strDate = "";
            String endDate = "";
            int totaldaysofyer = 0;
            String finalQuery = "";
            ArrayList Createlist = new ArrayList();
            String createDenomTables = resBundle.getString("createDenomTables");
            Object obj1[] = new Object[2];
            obj1[0] = "PR_WEEK_" + calenderId;
            obj1[1] = "WEEK_NO NUMBER, DYEAR NUMBER, START_DATE DATE, END_DATE DATE , CUST_NAME VARCHAR2(255 BYTE)";
            finalQuery = pbdb.buildQuery(createDenomTables, obj1);
            Createlist.add(finalQuery);
            ////////////////////// //////////////////////////////////////////////////////.println("PR week finalQuery---" + finalQuery);
            String addPrWeek = resBundle.getString("addPrWeek");


            String dets2[] = getpriorweeks(day, month, year, endday, endmnth, endyer, "No");

            //  Calendar calendar = Calendar.getInstance();

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
                //////////////// //////////////////////////////////////////////////////.println("addPrWeek finalQuery---" + finalQuery);
            }
            pbdb.executeMultipleCustomer(Createlist);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return weekdets;
    }

    public ArrayList addPrMonth(int yer, int mnth, int day, int calenderId, int endday, int endmnth, int endyer, String mnthName, String flag) throws Exception {
        ////////////////////// //////////////////////////////////////////////////////.println("---------------------------------------------------------------");
        String finalQuery = "";

        ArrayList Createlist = new ArrayList();
        ArrayList prmonthlist = new ArrayList();
        String createDenomTables = resBundle.getString("createDenomTables");
        Object obj1[] = new Object[2];
        if (flag.equalsIgnoreCase("yes")) {
            obj1[0] = "PR_MONTH_" + calenderId;
            obj1[1] = "MONTH_NO NUMBER, DYEAR NUMBER, START_DATE DATE, END_DATE DATE, MONTH_NAME VARCHAR2(255 BYTE), CUST_NAME VARCHAR2(255 BYTE)";
            finalQuery = pbdb.buildQuery(createDenomTables, obj1);
            ////////////////////// //////////////////////////////////////////////////////.println("create table" + finalQuery);
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
//            month1 = month1 + i;
            calendar.set(yer, i, dayex);
            int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1;
//           ////////////////////// //////////////////////////////////////////////////////.println("max days of month is " + maxDay);
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
            obj1[5] = monthName;
            prmonthdetails[2] = monthName + "-" + dbcustName;
            monthName = (calendar.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault()));
            obj1[6] = monthName + "-" + dbcustName;
            dbcustName = String.valueOf(calendar.get(Calendar.YEAR));
            finalQuery = pbdb.buildQuery(addPrMonth, obj1);
            //////////////////// //////////////////////////////////////////////////////.println("PR month finalQuery---" + finalQuery);
            Createlist.add(finalQuery);
            prmonthlist.add(prmonthdetails);
        }
        if (flag.equalsIgnoreCase("yes")) {

            pbdb.executeMultipleCustomer(Createlist);
        }
        return prmonthlist;
    }

    public void addPrDayDenom(int yer, int mnth, int day, int calenderId, int endyer, int endmnth, int endday, String mnthName, String endMnthName) throws Exception {

        ////////////////////// //////////////////////////////////////////////////////.println("------------------------------------------------------------------");
        String finalQuery = "";
        ArrayList Createlist = new ArrayList();
        String createDenomTables = resBundle.getString("createDenomTables");
        Object obj1[] = new Object[2];
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
        //////////////////////// //////////////////////////////////////////////////////.println("daydenomcreate table" + finalQuery);
        Createlist.add(finalQuery);
        String addPrDayDenom = resBundle.getString("addPrDayDenom");
        ////////////////////////////////////////////////////////.println("addPrDayDenom---" + addPrDayDenom);
        ArrayList cweklist = cWeek(yer, mnth, day, endday, endmnth, endyer, calenderId, "cweek");
        //////////////////////////////////////////////////////////////////////////////.println(cweklist.size());
        ArrayList cmonthlist = cWeek(yer, mnth, day, endday, endmnth, endyer, calenderId, "month");
        //////////////////////////////////////////////////////////////////////////////.println(cmonthlist.size());
        ArrayList prdayweklist = prDayweek(yer, mnth, day, endday, endmnth, endyer, calenderId);
        //////////////////////////////////////////////////////////////////////////////.println(prdayweklist.size());
        ArrayList lstyrwkdaylist = lastYearWeekDay(yer, mnth, day, endday, endmnth, endyer, calenderId);
        //////////////////////////////////////////////////////////////////////////////.println(lstyrwkdaylist.size());
        ArrayList pyrdaylist = pyearmDay(yer, mnth, day, endday, endmnth, endyer, calenderId);
        //////////////// //////////////////////////////////////////////////////.println("----size--" + pyrdaylist.size());
        ArrayList pmntdaylist = pmonthDay(yer, mnth, day, endday, endmnth, endyer, calenderId);
        //////////////////////////////////////////////////////////////////////////////.println(pmntdaylist.size());
        ArrayList cqerwdaylist = cqerweDay(yer, mnth, day, endday, endmnth, endyer, calenderId);
        //////////////////////////////////////////////////////////////////////////////.println(cqerwdaylist.size());
        ArrayList clastqerweDaylist = clastqerweDay(yer, mnth, day, endday, endmnth, endyer, calenderId);
        //////////////////////////////////////////////////////////////////////////////.println(clastqerweDaylist.size());
        // //////////////////////////////////////////////////////.println("------------------------lyer-----------------------");
        ArrayList clastyerqerDaylist = clastyerqerDay(yer, mnth, day, endday, endmnth, endyer, calenderId);
        // //////////////////////////////////////////////////////.println("-----------------------------------------------");
        //////////////////////////////////////////////////////////////////////////////.println(clastyerqerDaylist.size());
        ArrayList cyearDaylist = cyearDay(yer, mnth, day, endday, endmnth, endyer, calenderId);
        //////////////////////////////////////////////////////////////////////////////.println(cyearDaylist.size());
        ArrayList lyearDaylist = lyearDay(yer, mnth, day, endday, endmnth, endyer, calenderId);
        //////////////////////////////////////////////////////////////////////////////.println(lyearDaylist.size());
        int i = 0, stringlimit = 0, j = 0, count = -1, k = 0, totlength = 0, l = 1;
        String insertstrng = "";
        Object insrtobj[];
        //  ////////////////////// //////////////////////////////////////////////////////.println(" cweklist.size()---"+ cweklist.size());
        for (i = 0; i < cweklist.size(); i++) {
            String weklstrng[] = cweklist.get(i).toString().split(",");
            String prdaystrng[] = prdayweklist.get(i).toString().split(",");
            String lstyrwkdaystr[] = lstyrwkdaylist.get(i).toString().split(",");
            String cmonthstrng[] = cmonthlist.get(i).toString().split(",");
            String pyrdaystrng[] = pyrdaylist.get(i).toString().split(",");


            //    if (pmntdaylist.size() > cweklist.size()) {
            //        int min = i - 1;
            //       pyrdaystrng = pyrdaylist.get(min).toString().split(",");
            //    }
            String pmntdaystrng[] = pmntdaylist.get(i).toString().split(",");
            String cqerwdaystrng[] = cqerwdaylist.get(i).toString().split(",");
            String clastqerweDaystrng[] = clastqerweDaylist.get(i).toString().split(",");
            String clastyerqerDaystrng[] = clastyerqerDaylist.get(i).toString().split(",");
            String cyearDaystrng[] = cyearDaylist.get(i).toString().split(",");
            String lyearDaystrng[] = lyearDaylist.get(i).toString().split(",");

            stringlimit = weklstrng.length + prdaystrng.length + lstyrwkdaystr.length + cmonthstrng.length + pmntdaystrng.length + pyrdaystrng.length + cqerwdaystrng.length
                    + clastqerweDaystrng.length + clastyerqerDaystrng.length + cyearDaystrng.length + lyearDaystrng.length + 1;
            insrtobj = new Object[stringlimit];
            insrtobj[0] = calenderId;
            for (j = 1, k = 0; j < weklstrng.length + 1; j++, k++) {
                insrtobj[l] = weklstrng[k].toString();
                //  //////////////////////////////////////////////////////.println(j+"-"+insrtobj[j]);
                count++;
                insertstrng = insertstrng + "," + insrtobj[k];
                l++;

            }
            totlength = count;

            for (j = count + 2, k = 0; j < totlength + prdaystrng.length + 2; j++, k++) {
//               ////////////////////// //////////////////////////////////////////////////////.println("j"+j);
//               ////////////////////// //////////////////////////////////////////////////////.println("contu"+count);
//             //////////////////////////////////////////////////////.println(prdaystrng.length+2);
                insrtobj[l] = prdaystrng[k].toString();
                //   //////////////////////////////////////////////////////.println(j+"-"+insrtobj[j]);
                count++;
                insertstrng = insertstrng + "," + insrtobj[l];
                l++;

            }
            totlength = count;
            for (j = count + 2, k = 0; j < totlength + lstyrwkdaystr.length + 2; j++, k++) {
                insrtobj[l] = lstyrwkdaystr[k].toString();
                // //////////////////////////////////////////////////////.println(j+"-"+insrtobj[j]);
                count++;
                insertstrng = insertstrng + "," + insrtobj[l];
                l++;

            }
            totlength = count;
            for (j = count + 2, k = 0; j < totlength + cmonthstrng.length + 2; j++, k++) {
                insrtobj[l] = cmonthstrng[k].toString();
                //  //////////////////////////////////////////////////////.println(j+"-"+insrtobj[j]);
                count++;
                insertstrng = insertstrng + "," + insrtobj[l];
                l++;

            }

            totlength = count;
            for (j = count + 2, k = 0; j < totlength + pmntdaystrng.length + 2; j++, k++) {
                insrtobj[l] = pmntdaystrng[k].toString();
                ////////////////////////////////////////////////////////.println(j+"-"+insrtobj[j]);
                count++;
                insertstrng = insertstrng + "," + insrtobj[l];
                l++;

            }
            totlength = count;
            for (j = count + 2, k = 0; j < totlength + pyrdaystrng.length + 2; j++, k++) {
                insrtobj[l] = pyrdaystrng[k].toString();
                //  //////////////////////////////////////////////////////.println(j+"-"+insrtobj[j]);
                count++;
                insertstrng = insertstrng + "," + insrtobj[l];
                l++;

            }
            totlength = count;
            for (j = count + 2, k = 0; j < totlength + cqerwdaystrng.length + 2; j++, k++) {
                insrtobj[l] = cqerwdaystrng[k].toString();
                //  //////////////////////////////////////////////////////.println(j+"-"+insrtobj[j]);
                count++;
                insertstrng = insertstrng + "," + insrtobj[l];
                l++;

            }
            totlength = count;
            for (j = count + 2, k = 0; j < totlength + clastqerweDaystrng.length + 2; j++, k++) {
                insrtobj[l] = clastqerweDaystrng[k].toString();
                //  //////////////////////////////////////////////////////.println(j+"-"+insrtobj[j]);
                count++;
                insertstrng = insertstrng + "," + insrtobj[l];
                l++;

            }
            totlength = count;
            for (j = count + 2, k = 0; j < totlength + clastyerqerDaystrng.length + 2; j++, k++) {
                insrtobj[l] = clastyerqerDaystrng[k].toString();
                // //////////////////////////////////////////////////////.println(j+"-"+insrtobj[j]);
                count++;
                insertstrng = insertstrng + "," + insrtobj[l];
                l++;
            }
            totlength = count;
            for (j = count + 2, k = 0; j < totlength + cyearDaystrng.length + 2; j++, k++) {
                insrtobj[l] = cyearDaystrng[k].toString();
                //     //////////////////////////////////////////////////////.println(j+"-"+insrtobj[j]);
                count++;
                insertstrng = insertstrng + "," + insrtobj[l];
                l++;
            }
            totlength = count;
            for (j = count + 2, k = 0; j < totlength + lyearDaystrng.length + 2; j++, k++) {
                insrtobj[l] = lyearDaystrng[k].toString();
                //   //////////////////////////////////////////////////////.println(j+"-"+insrtobj[j]);
                count++;
                insertstrng = insertstrng + "," + insrtobj[l];
                l++;
            }
            finalQuery = pbdb.buildQuery(addPrDayDenom, insrtobj);
            //////////////////////////////////////////////////////////////////////////////.println("finalQuery---" + finalQuery);
//                   ////////////////////// //////////////////////////////////////////////////////.println("query"+addPrDayDenom);
//                   ////////////////////// //////////////////////////////////////////////////////.println("inser query"+finalquery);

//            finalquery = pbdb.buildQuery(finalquery, obj1);
//           ////////////////////// //////////////////////////////////////////////////////.println("prdaydeno "+finalQuery);
//           ////////////////////// //////////////////////////////////////////////////////.println("l" + l);
//             //////////////////////////////////////////////////////.println("prdaydeno" + finalquery);
            Createlist.add(finalQuery);
            l = 1;

        }
        pbdb.executeMultipleCustomer(Createlist);
        //////////////////////// //////////////////////////////////////////////////////.println(i);
    }

    public void addPrWeekDenom(int yer, int mnth, int day, int calenderId, int endyer, int endmnth, int endday, String mnthName, String endMnthName) throws Exception {

        ////////////////////// //////////////////////////////////////////////////////.println("-----------------------------------------------------------------");
        String finalQuery = "";
        ArrayList Createlist = new ArrayList();
        String createDenomTables = resBundle.getString("createDenomTables");
        Object obj1[] = new Object[2];
        obj1[0] = "PR_WEEK_DENOM_" + calenderId;
        obj1[1] = "CWEEK NUMBER,CYEAR NUMBER, C_ST_DATE DATE,C_END_DATE DATE,C_NO_DAYS NUMBER,PW_WEEK NUMBER,"
                + "PW_YEAR NUMBER,PW_ST_DATE DATE,PW_END_DATE DATE,PY_WEEK NUMBER,PY_YEAR NUMBER,PY_ST_DATE DATE,PY_END_DATE DATE";
        finalQuery = pbdb.buildQuery(createDenomTables, obj1);
        ////////////////////// //////////////////////////////////////////////////////.println("create table" + finalQuery);
        Createlist.add(finalQuery);
        String addPrWeekDenom = resBundle.getString("addPrWeekDenom");
        //String strDate=day+"-"+mnth+"-"+yer;
        //String endDate=endday+"-"+endmnth+"-"+
        String dets1[] = getpriorweeks(day, mnth, yer, endday, endmnth, endyer, "No");
        // String dets2[] = priorWeek(day, mnth, yer, endday, endmnth, endyer);
        String dets2[] = getpriorweeks(day, mnth, yer, endday, endmnth, endyer, "yes");
        String getstrtendyear[] = priorYear(day, mnth, yer, endday, endmnth, endyer);
        String priorstartyear = getstrtendyear[4];
        String priorendyear = getstrtendyear[5];
        ////////////////////// //////////////////////////////////////////////////////.println("priorstartyear---"+priorstartyear);
        ////////////////////// //////////////////////////////////////////////////////.println("priorendyear---"+priorendyear);

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

        //////////////////////// //////////////////////////////////////////////////////.println("dets1.length--" + dets1.length+"---"+dets1[0]);
        //////////////////////// //////////////////////////////////////////////////////.println("dets2.length--" + dets2.length+"---"+dets2[0]);
        //////////////////////// //////////////////////////////////////////////////////.println("dets3.length--" + dets3.length+"----"+dets3[0]);
        obj1 = new Object[14];
        obj1[0] = calenderId;
        for (int i = 0; i < dets1.length; i++) {
            String cdets[] = dets1[i].split(",");
            //////////////////////// //////////////////////////////////////////////////////.println("--1-"+cdets[0]);
            String pdets[] = dets2[i].split(",");
            ////////////////////////////////////////////////////////.println("--2-"+pdets[0]);
            String pydets[] = dets3[i].split(",");
            ////////////////////// //////////////////////////////////////////////////////.println("--3-"+dets3[i]);
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
            //////////////////////////////////////////////////////////////////////////////.println("finalQuery-add weekdenom-" + finalQuery);
        }
        //call to prior year details
        pbdb.executeMultipleCustomer(Createlist);

    }

    public void addPrMonthDenom(int yer, int mnth, int day, int calenderId, int endyer, int endmnth, int endday, String mnthName) throws Exception {
        ////////////////////// //////////////////////////////////////////////////////.println("------------------------------------------------------------------");
        String finalQuery = "";
        ArrayList Createlist = new ArrayList();
        String createDenomTables = resBundle.getString("createDenomTables");
        Object obj1[] = new Object[2];
        obj1[0] = "PR_MONTH_DENOM_" + calenderId;
        obj1[1] = "CMON NUMBER, 	CYEAR NUMBER, 	C_ST_DATE DATE, 	C_END_DATE DATE, 	C_NO_DAYS NUMBER,"
                + "PM_MON NUMBER, 	PM_YEAR NUMBER, 	PM_ST_DATE DATE, 	PM_END_DATE DATE, 	PM_DAYS NUMBER, 	PY_MON NUMBER, 	PY_YEAR NUMBER,"
                + "PY_ST_DATE DATE, 	PY_END_DATE DATE, 	PY_DAYS NUMBER";
        finalQuery = pbdb.buildQuery(createDenomTables, obj1);
        //////////////////////////////////////////////////////////////////////////////.println(finalQuery);
        Createlist.add(finalQuery);
        String addPrMonthDenom = resBundle.getString("addPrMonthDenom");
        // String dets1[]=addPrMonth(yer, mnth, day, calenderId, endyer, endmnth, endday, mnthName);
        String dets2[] = priorMonth(day, mnth, yer, endday, endmnth, endyer);

//       ////////////////////// //////////////////////////////////////////////////////.println("-------------month----------------");
        obj1 = new Object[16];
        obj1[0] = calenderId;
        for (int i = 0; i < dets2.length; i++) {
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
            //////////////////////////////////////////////////////.println("finalQuery-add monthdenom-" + finalQuery);
        }
        pbdb.executeMultipleCustomer(Createlist);
    }

    public void addQrterDenom(int yer, int month, int day, int calenderId, int endyear, int endMonth, int endDay, String monthName) {
        ////////////////////// //////////////////////////////////////////////////////.println("-----------------------------------------------------------------------");
        try {
            ArrayList list = new ArrayList();
            String finalQuery = "";
//            ArrayList Createlist = new ArrayList();
            String createDenomTables = resBundle.getString("createDenomTables");
            Object obj1[] = new Object[2];
            obj1[0] = "PR_QTR_DENOM_" + calenderId;
            obj1[1] = "CQTR NUMBER, CYEAR NUMBER,C_ST_DATE DATE, 	C_END_DATE DATE,	C_NO_DAYS NUMBER, 	PQ_QTR NUMBER,"
                    + "PQ_YEAR NUMBER, 	PQ_ST_DATE DATE,	PQ_END_DATE DATE, 	PQ_DAYS NUMBER, 	PY_QTR NUMBER, 	PY_YEAR NUMBER, 	PY_ST_DATE DATE,"
                    + "PY_END_DATE DATE, PY_DAYS NUMBER";
            finalQuery = pbdb.buildQuery(createDenomTables, obj1);
            //////////////////////////////////////////////////////.println(finalQuery);
            list.add(finalQuery);
            String addPrQtrDenom = resBundle.getString("addPrQtrDenom");
            String dets1[] = addPrQtr(yer, month, day, calenderId, endyear, endMonth, endDay, monthName, "No");

            //cal to prior details
            ////////////////////////////////////////////////////////.println("---------------------------------QTR------------------------------------------------");
            String dets2[] = addpriorQrter(yer, month, day, calenderId, endyear, endMonth, endDay, monthName, "YES");

            String getstrtendyear[] = priorYear(day, month, yer, day, month, endyear);
            String priorstartyear = getstrtendyear[4];
            String priorendyear = getstrtendyear[5];
            String[] priorstrtarr = new String[3];
            //////////////////////// //////////////////////////////////////////////////////.println("priorstartyear==========" + priorstartyear);
            //////////////////////// //////////////////////////////////////////////////////.println("priorendyear==========" + priorendyear);
            priorstrtarr = priorstartyear.split("-");
            month = Integer.parseInt(priorstrtarr[1].toString());
            day = Integer.parseInt(priorstrtarr[0].toString());
            yer = Integer.parseInt(priorstrtarr[2].toString());
            String[] priorendarr = new String[3];

            priorendarr = priorendyear.split("-");
            int endmnth = Integer.parseInt(priorendarr[1].toString());
            int endday = Integer.parseInt(priorendarr[0].toString());
            int endyer = Integer.parseInt(priorendarr[2].toString());
            // // //////////////////////////////////////////////////////.println("in qtr ------------"+yer+","+ month+","+  day+","+  calenderId+","+  endyer+","+  endmnth+","+  endday+","+  monthName);
            String dets3[] = addPrQtr(yer, month, day, calenderId, endyer, endmnth, endday, monthName, "NO");


            int i = 0;
            obj1 = new Object[16];
            obj1[0] = calenderId;
            for (i = 0; i < dets1.length; i++) {
                String det1slist[] = dets1[i].split(",");
                // //////////////////////////////////////////////////////.println("det2slist===" + dets2[i]);
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
                //////////////////////////////////////////////////////.println("finalQuery-add qtrdenom-" + finalQuery);
                ////// //////////////////////////////////////////////////////.println("queries \t" + dets1[i] + "," + dets2[i] + "," + dets3[i]);
            }
            //////////////////////// //////////////////////////////////////////////////////.println("---------------------------------------------------------------------------------");
            //call to prior year details
            pbdb.executeMultipleCustomer(list);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void addPrYearDenom(int yer, int mnth, int day, int calenderId, int endyer, int endmnth, int endday, String mnthName, String endMnthName) throws Exception {
        ////////////////////// //////////////////////////////////////////////////////.println("-----------------------------------------------------------------------");
        String finalQuery = "";
        ArrayList Createlist = new ArrayList();
        String createDenomTables = resBundle.getString("createDenomTables");
        Object obj1[] = new Object[2];
        obj1[0] = "PR_YEAR_DENOM_" + calenderId;
        obj1[1] = "CYEAR NUMBER,C_ST_DATE DATE,C_END_DATE DATE,C_NO_DAYS NUMBER,PYEAR NUMBER,P_ST_DATE DATE,P_END_DATE DATE,P_NO_DAYS NUMBER";
        finalQuery = pbdb.buildQuery(createDenomTables, obj1);
        Createlist.add(finalQuery);
        ////////////////////// //////////////////////////////////////////////////////.println("finalqUERY" + finalQuery);
        String addPrYeardenom = resBundle.getString("addPrYeardenom");
        String dets1[] = priorYear(day, mnth, yer, endday, endmnth, endyer);
        //  ////////////////////// //////////////////////////////////////////////////////.println("dets1[]--" + dets1[0]);
        //  ////////////////////// //////////////////////////////////////////////////////.println("dets1[]--" + dets1[1]);
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
        //////////////////////////////////////////////////////.println("finalQuery-add year denm-" + finalQuery);
        pbdb.executeMultipleCustomer(Createlist);
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

            String concat;

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
                concat = sno + "," + dbyear + "," + strdate;
                forcqtrcustname = "Q" + sno + "-" + dbyear;
                String sndstrtdate = strdate;
                // if (day >= 28) {
                //  endDate = (calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) - extradays + "-" + calendar.getDisplayName(2, Calendar.SHORT, Locale.getDefault()) + "-" + calendar.get(1);
                //   dbyear = calendar.get(1);
                //   strdate = ((calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) - extradays + 1) + "-" + calendar.getDisplayName(2, Calendar.SHORT, Locale.getDefault()) + "-" + calendar.get(1);

                //  } else {
                if (check.equalsIgnoreCase("yes")) {
                    if (i == 1) {
                        calendar.add(Calendar.DATE, -1);
                    }
                }
                endDate = calendar.get(5) + "-" + calendar.getDisplayName(2, Calendar.SHORT, Locale.getDefault()) + "-" + calendar.get(1);
                concat += "," + endDate;

                calendar.add(Calendar.DATE, 1);
                dbyear = calendar.get(1);
                strdate = (calendar.get(5)) + "-" + calendar.getDisplayName(2, Calendar.SHORT, Locale.getDefault()) + "-" + calendar.get(1);

                //}
                calendar.add(Calendar.DATE, -1);

                int totdays = gettotalnumofDays(sndstrtdate, endDate);
                if (check.equalsIgnoreCase("no")) {
                    totdays = gettotalnumofDays(day + "-" + monthNames[month] + "-" + yer, endDay + "-" + monthNames[endMonth] + "-" + endyear);
                }
                addlist[i - 1] = concat + "," + totdays + "," + forcqtrcustname;
                //////////////////////// //////////////////////////////////////////////////////.println("concat" + concat);

            }
//         pbdb.executeMultiple(Createlist);

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
        //////////////////////// //////////////////////////////////////////////////////.println("In This Year Total days: " + (diffDays) + " days.");
        cal1.set(yearpr, monthpr, daypr);
//       //////////////////////// //////////////////////////////////////////////////////.println("Start day of the Prior year is " + daypr + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR));
        cal2.set(eyear, emonth, eday);
//       //////////////////////// //////////////////////////////////////////////////////.println("End day of the year is " + cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR));
        cal1.add(Calendar.DATE, (int) -diffDays);
//       //////////////////////// //////////////////////////////////////////////////////.println("Start day of the Prior year is " + daypr + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR));
//        String startdate = daypr + "/" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "/" + cal1.get(Calendar.YEAR);
        String startdate = daypr + "-" + cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault()) + "-" + cal1.get(Calendar.YEAR);
        String startdate1 = daypr + "-" + cal1.get(Calendar.MONTH) + "-" + cal1.get(Calendar.YEAR);
        cal2.add(Calendar.DATE, (int) -diffDays);
//       //////////////////////// //////////////////////////////////////////////////////.println("End day of the Prior year is " + cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR));
//        String enddate = cal2.get(Calendar.DATE) + "/" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "/" + cal2.get(Calendar.YEAR);
        String enddate = cal2.get(Calendar.DATE) + "-" + cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault()) + "-" + cal2.get(Calendar.YEAR);
        String enddate1 = cal2.get(Calendar.DATE) + "-" + cal2.get(Calendar.MONTH) + "-" + cal2.get(Calendar.YEAR);
        cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DATE));
        cal2.set(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal1.get(Calendar.DATE));
        long milisp1 = cal1.getTimeInMillis();
        long milisp2 = cal2.getTimeInMillis();
        long diffp = milisp2 - milisp1;
        long diffDaysp = diffp / (24 * 60 * 60 * 1000);
        ////////////////////////////////////////////////////////.println("In Prior Year Total days: " + diffDaysp + " days.");

        strend[0] = String.valueOf(cal1.get(Calendar.YEAR));
        strend[1] = startdate;
        strend[2] = enddate;
        strend[3] = String.valueOf(gettotalnumofDays(startdate1, enddate1));
        strend[4] = startdate1;
        strend[5] = enddate1;
//       //////////////////////// //////////////////////////////////////////////////////.println("strends are000000000000000" + strend);
        return strend;
    }

    public String[] prweek(int strtday, int strtmonth, int strtyear, int eday, int emonth, int eyear) {

        String concat = "";

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(strtyear, strtmonth, strtday);
        cal2.set(eyear, emonth, eday);
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        long diff = milis2 - milis1;
        long diffDays = (diff / (7 * 24 * 60 * 60 * 1000));
//       //////////////////////// //////////////////////////////////////////////////////.println("In This Year Total Weeks: " + (diffDays));
        int weekno2 = (cal1.get(Calendar.WEEK_OF_YEAR)) - 1;
        ////////////////////////////////////////////////////////.println("Current week is befour loop " + weekno2);
        String prwekarr[] = new String[(int) diffDays];

        for (int w = 1; w <= diffDays; w++) {
            cal1.set(strtyear, strtmonth, strtday);
            int weekno = cal1.get(Calendar.WEEK_OF_YEAR) - weekno2;
//           //////////////////////// //////////////////////////////////////////////////////.println("Current week is " + weekno);
//           //////////////////////// //////////////////////////////////////////////////////.println("Start day of the Week is " + cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR));
            int strtwekyear = cal1.get(Calendar.YEAR);
            int day4 = strtday + 6;
            cal2.set(strtyear, strtmonth, day4);
//           //////////////////////// //////////////////////////////////////////////////////.println("End day of the Week is " + cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR));
            concat = weekno + "," + strtwekyear + "," + cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR)
                    + ",7" + cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR);
            cal1.add(Calendar.DATE, -7);
            int weekno1 = cal1.get(Calendar.WEEK_OF_YEAR) - weekno2;
            if (weekno1 == 0) {
                weekno1 = 52;
            }
//           //////////////////////// //////////////////////////////////////////////////////.println("Prior week is " + weekno1);
//           //////////////////////// //////////////////////////////////////////////////////.println("Start day of the Prior Week is " + cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR));
            int priorwekyear = cal2.get(Calendar.YEAR);
            cal2.add(Calendar.DATE, -7);
//           //////////////////////// //////////////////////////////////////////////////////.println("End day of the Prior Week is " + cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR));
            strtday = strtday + 7;
            concat += "," + weekno1 + "," + priorwekyear + "," + cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR)
                    + "," + cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR);
            prwekarr[w - 1] = concat;
        }
//        String getpriorwekarr[] = prioryearWek(strtday, strtmonth, strtyear, eday, emonth, eyear);
//        for (int i = 0; i < getpriorwekarr.length; i++) {
//           //////////////////////// //////////////////////////////////////////////////////.println("at Last priro Week Query\t" + prwekarr[i] + "," + getpriorwekarr[i]);
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
        ////////////////////////// //////////////////////////////////////////////////////.println("In This Year Total Weeks: " + (diffDays));
        int weekno2 = (cal1.get(Calendar.WEEK_OF_YEAR)) - 1;
        ////////////////////////////////////////////////////////.println("Current week is befour loop " + weekno2);
        String prorwekarr[] = new String[(int) diffDays];
        for (int w = 1; w <= diffDays; w++) {
            cal1.set(strtyear - 1, strtmonth, strtdate);
            int weekno = cal1.get(Calendar.WEEK_OF_YEAR) - weekno2;
            ////////////////////////// //////////////////////////////////////////////////////.println("Current week is " + weekno);
            // //////////////////////// //////////////////////////////////////////////////////.println("Start day of the Week is " + cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR));
            int calyear = cal1.get(Calendar.YEAR);
            int day4 = strtdate + 6;
            cal2.set(strtyear - 1, strtdate, day4);
            //  //////////////////////// //////////////////////////////////////////////////////.println("End day of the Week is " + cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR));
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
        ////////////////////////////////////////////////////////.println("In This Year Total Weeks: " + (diffDays));
        int weekno2 = cal1.get(3);

        String dets[] = new String[totweekno];

        cal1.set(year3, month3, day3);


        cal1.add(Calendar.DATE, -6);
        String strDate = cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR);
        cal1.add(Calendar.DATE, -1);
        for (int w = 1; w <= diffWeeks; w++) {
            ////////////////////////// //////////////////////////////////////////////////////.println("--"+w+"---");
            String detstr = "";
            detstr += w;
            detstr += "," + cal1.get(Calendar.YEAR);
            detstr += "," + strDate;

            //////////////////////// //////////////////////////////////////////////////////.println("w===" + w);

            cal1.add(Calendar.DATE, 6);

            //////////////////////// //////////////////////////////////////////////////////.println("in else");
            cal1.add(Calendar.DATE, 8);

            String enddate = cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR);
            cal1.add(Calendar.DATE, 1);
            strDate = cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR);
            cal1.add(Calendar.DATE, -1);


            detstr += "," + enddate;

            day3 = day3 + 7;
            dets[w - 1] = detstr;
        }
        // //////////////////////// //////////////////////////////////////////////////////.println("dets--return--"+dets);
        return dets;


    }

    //for prior month
    public String[] priorMonth(int day3, int month3, int year3, int eday, int emonth, int eyear) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(year3, month3, day3);
        cal2.set(year3, month3, day3);
        String dets1[] = priorYear(day3, month3, year3, eday, emonth, eyear);
        String pyearstrDate = dets1[1];
        String pyearenddate = dets1[2];
//       //////////////////////// //////////////////////////////////////////////////////.println("pyearstrDate---" + pyearstrDate + "-------pyearenddate----" + pyearenddate);
        String pyeardets[] = pyearstrDate.split("-");
        Calendar pycal = Calendar.getInstance();
        pycal.set(Integer.parseInt(pyeardets[2]), monthNumber(pyeardets[1]), Integer.parseInt(pyeardets[0]));


        ////////////////////////// //////////////////////////////////////////////////////.println("Start day of the month in the year " + cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR));
//       //////////////////////// //////////////////////////////////////////////////////.println("month " + cal1.get(Calendar.MONTH));
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
        int pytotaldays = gettotalnumofDays(pyearstrDate, pyearenddate);
        String pymonthyear = "";
        for (int i = 1; i <= 12; i++) {
            ////////////////////////// //////////////////////////////////////////////////////.println("calender----"+cal1);

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
            ////////////////////////////////////////////////////////.println("cnumofdays---"+pstrDate+"==="+pendDate);
            int pnumofdays = gettotalnumofDays(pstrDate, pendDate);
            ////////////////////////// //////////////////////////////////////////////////////.println("pnumofdays---"+pnumofdays);
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
//       //////////////////////// //////////////////////////////////////////////////////.println("days:" + days);
//       //////////////////////// //////////////////////////////////////////////////////.println("daysof year" + daysOfYear);
        for (i = day; i <= days; i++) {
            count++;
            ddate = i + "-" + monthName(month) + "-" + yer;
            cmonth = month + 1;
            cmstrtdate = 1 + "-" + monthName(month) + "-" + yer;
            cmenddate = days + "-" + monthName(month) + "-" + yer;
            cmcustname = monthName(month) + "-" + yer;
//           //////////////////////// //////////////////////////////////////////////////////.println("ddate is" + ddate);
            strtdate = ddate;
            enddate = ddate;
            cal.set(yer, month, i);
            daysofyear = cal.get(Calendar.DAY_OF_YEAR);
            cweek = cal.get(Calendar.WEEK_OF_YEAR);
            cwyear = cal.get(Calendar.YEAR);
            custname = "W-" + cweek + "-" + yer;
//        String dayis = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
//       //////////////////////// //////////////////////////////////////////////////////.println("dayis" + dayis);
            cal.setFirstDayOfWeek(Calendar.SUNDAY);  // Make sure that the calendar starts its week at Sunday
//            cal.setTime(new Date());
//           //////////////////////// //////////////////////////////////////////////////////.println("Current week is: " + cal.get(Calendar.WEEK_OF_YEAR));
//           //////////////////////// //////////////////////////////////////////////////////.println("Current week in this month is: " + cal.get(Calendar.WEEK_OF_MONTH));
//       //////////////////////// //////////////////////////////////////////////////////.println("Today is " + cal.getTime() );
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
//           //////////////////////// //////////////////////////////////////////////////////.println("Start of week: " + cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR));
            cwstrtdate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            cwstrtmntdate = cwstrtdate;
            cal.add(Calendar.DAY_OF_YEAR, 6);
//           //////////////////////// //////////////////////////////////////////////////////.println("End of week: " + cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR)); //+ cal.getTime() );
            cwenddate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            cwendmntdate = cwenddate;

            /*
             * ////////////////////////
             * //////////////////////////////////////////////////////.println("before
             * inserting");
             * //////////////////////////////////////////////////////.println(ddate);
             * //////////////////////////////////////////////////////.println(strtdate);
             * //////////////////////////////////////////////////////.println(enddate);
             * //////////////////////////////////////////////////////.println(daysofyear);
             * //////////////////////////////////////////////////////.println(cweek);
             * //////////////////////////////////////////////////////.println(cwyear);
             * //////////////////////////////////////////////////////.println(cwstrtdate);
             * //////////////////////////////////////////////////////.println(cwenddate);
             * //////////////////////////////////////////////////////.println(cwstrtmntdate);
             * //////////////////////////////////////////////////////.println(cwendmntdate);
             * //////////////////////////////////////////////////////.println(custname);
             */
            fullstring = ddate + "," + strtdate + "," + enddate + "," + daysofyear + "," + cweek + "," + cwyear + "," + cwstrtdate + "," + cwenddate + "," + cwstrtmntdate + "," + cwendmntdate + "," + custname;
            if (flag.equalsIgnoreCase("month")) {
                crntmnthstr = cmonth + "," + cwyear + "," + cmstrtdate + "," + cmenddate + "," + cmcustname;

//            //////////////////////////////////////////////////////.println(crntmnthstr);
                crnmnthlist.add(crntmnthstr);
            }
            if (flag.equalsIgnoreCase("cweek")) {
//            //////////////////////////////////////////////////////.println(fullstring);
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

    public ArrayList prDayweek(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {


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
//               //////////////////////// //////////////////////////////////////////////////////.println("days-----------" + days);
                day = days + day;
            }
        }
//        else
//        {
//            month = month-1;
//        }
        endday = endday - 7;
        if (endday <= 0) {
            if (endmonth == 0) {
                endmonth = 11;
                yer = yer - 1;
                days = totaldays(endyer, endmonth, endday);
//               //////////////////////// //////////////////////////////////////////////////////.println("endmonth-----------"+days);
                endday = days + endday;
            }
        }
        days = totaldays(yer, month + 1, day);
        daysOfYear = gettotalnumofDays(day + "-" + monthName(month) + "-" + yer, endday + "-" + monthName(endmonth) + "-" + endyer);
//       //////////////////////// //////////////////////////////////////////////////////.println("days:" + days);
//       //////////////////////// //////////////////////////////////////////////////////.println("daysof year" + daysOfYear);
        for (i = day; i <= days; i++) {
            count++;
            ddate = i + "-" + monthName(month) + "-" + yer;
//           //////////////////////// //////////////////////////////////////////////////////.println("ddate is" + ddate);
            strtdate = ddate;
            enddate = ddate;
            cal.set(yer, month, i);
            daysofyear = cal.get(Calendar.DAY_OF_YEAR);
            cweek = cal.get(Calendar.WEEK_OF_YEAR);
            cwyear = cal.get(Calendar.YEAR);
            custname = "W-" + cweek + "-" + yer;
//        String dayis = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
//       //////////////////////// //////////////////////////////////////////////////////.println("dayis" + dayis);
            cal.setFirstDayOfWeek(Calendar.SUNDAY);  // Make sure that the calendar starts its week at Sunday
//            cal.setTime(new Date());
//           //////////////////////// //////////////////////////////////////////////////////.println("Current week is: " + cal.get(Calendar.WEEK_OF_YEAR));
//           //////////////////////// //////////////////////////////////////////////////////.println("Current week in this month is: " + cal.get(Calendar.WEEK_OF_MONTH));
//       //////////////////////// //////////////////////////////////////////////////////.println("Today is " + cal.getTime() );
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
//           //////////////////////// //////////////////////////////////////////////////////.println("Start of week: " + cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR));
            cwstrtdate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            cwstrtmntdate = cwstrtdate;
            cal.add(Calendar.DAY_OF_YEAR, 6);
//           //////////////////////// //////////////////////////////////////////////////////.println("End of week: " + cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR)); //+ cal.getTime() );
            cwenddate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            cwendmntdate = cwenddate;

            prweekday = ddate + "," + cwstrtdate + "," + cwenddate + "," + cwstrtmntdate + "," + cwendmntdate + "," + custname;
//            //////////////////////////////////////////////////////.println(prweekday);
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
//               //////////////////////// //////////////////////////////////////////////////////.println("days-----------" + days);
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
//               //////////////////////// //////////////////////////////////////////////////////.println("endmonth-----------"+days);
                endday = 1;
            }
        }
        days = totaldays(yer, month + 1, day);
        daysOfYear = gettotalnumofDays(day + "-" + monthName(month) + "-" + yer, endday + "-" + monthName(endmonth) + "-" + endyer);
//       //////////////////////// //////////////////////////////////////////////////////.println("days:" + days);
//       //////////////////////// //////////////////////////////////////////////////////.println("daysof year" + daysOfYear);
        for (i = day; i <= days; i++) {
            count++;
            lyrwekdate = i + "-" + monthName(month) + "-" + yer;
//           //////////////////////// //////////////////////////////////////////////////////.println("ddate is" + ddate);
            cal.set(yer, month, i);
            daysofyear = cal.get(Calendar.DAY_OF_YEAR);
            cweek = cal.get(Calendar.WEEK_OF_YEAR);
            cwyear = cal.get(Calendar.YEAR);
            lstyrcustname = "W-" + cweek + "-" + yer;
//        String dayis = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
//       //////////////////////// //////////////////////////////////////////////////////.println("dayis" + dayis);
            cal.setFirstDayOfWeek(Calendar.SUNDAY);  // Make sure that the calendar starts its week at Sunday
//            cal.setTime(new Date());
//           //////////////////////// //////////////////////////////////////////////////////.println("Current week is: " + cal.get(Calendar.WEEK_OF_YEAR));
//           //////////////////////// //////////////////////////////////////////////////////.println("Current week in this month is: " + cal.get(Calendar.WEEK_OF_MONTH));
//       //////////////////////// //////////////////////////////////////////////////////.println("Today is " + cal.getTime() );
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
//           //////////////////////// //////////////////////////////////////////////////////.println("Start of week: " + cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR));
            lstyrstrtdate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            lstyrstrtmntdate = lstyrstrtdate;
            cal.add(Calendar.DAY_OF_YEAR, 6);
//           //////////////////////// //////////////////////////////////////////////////////.println("End of week: " + cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR)); //+ cal.getTime() );
            lstyrenddate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            lstyrendmntdate = lstyrenddate;

            lstyrweekday = lyrwekdate + "," + lstyrstrtdate + "," + lstyrenddate + "," + lstyrstrtmntdate + "," + lstyrendmntdate + "," + lstyrcustname;
//            //////////////////////////////////////////////////////.println(lstyrweekday);
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
//           //////////////////////// //////////////////////////////////////////////////////.println("startyear" + startyear);
//           //////////////////////// //////////////////////////////////////////////////////.println("endyear" + endyear);
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
            ////////////////////////// //////////////////////////////////////////////////////.println("============" + yer + "," + month + "," + day + "," + calenderId + "," + endday + "," + endmonth + "," + endyer);
            ArrayList prmnthlist = addPrMonth(yer, month, day, calenderId, endday, endmonth, endyer, monthName(month), "No");
            ////////////////////////// //////////////////////////////////////////////////////.println("prmnthlist-size--" + prmnthlist.size());
            String pyeardata;
            // month=month+1;
            int i = 0, j = 0, days = totaldays(yer, month + 1, day), count = 0, k = 0;
            // //////////////////////// //////////////////////////////////////////////////////.println("totalnumofdays" + days);
//           //////////////////////// //////////////////////////////////////////////////////.println("totalnumofdays" + totalnumofdays);
            for (i = day; i <= days; i++) {
                //  //////////////////////// //////////////////////////////////////////////////////.println("i==" + i + "count==" + count);
                k++;
                count++;

                pyeardata = i + "-" + monthName(month) + "-" + yer + ",";
                ////////////////////////// //////////////////////////////////////////////////////.println("pyeardata----" + pyeardata);
                if (k == totaldays(yer, month, day)) {
//                   //////////////////////// //////////////////////////////////////////////////////.println("k" + k);
                    j++;
                    k = 0;
                }


                yersarr = (String[]) prmnthlist.get(j);
                pyeardata = pyeardata + yersarr[0] + "," + yersarr[1] + "," + yersarr[2];
                // //////////////////////// //////////////////////////////////////////////////////.println("----" + pyeardata + "j====" + j);

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
                    // //////////////////////// //////////////////////////////////////////////////////.println("coutn"+count);

                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return pyearmdaylist;
    }

    public ArrayList pyearmDay(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {
        ArrayList pyearmdaylist = new ArrayList();
        String getpryear[] = priorYear(day, month, yer, endday, endmonth, endyer);
        String[] yersarr;
        try {

            String startyear = getpryear[1];
            String endyear = getpryear[2];
//           //////////////////////// //////////////////////////////////////////////////////.println("startyear" + startyear);
//           //////////////////////// //////////////////////////////////////////////////////.println("endyear" + endyear);
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
            ////////////////////////// //////////////////////////////////////////////////////.println("totanlumofdays" + totalnumofdays);
//            //////////////////////////////////////////////////////.println(yer + "," + month + "," + day + "," + calenderId + "," + endday + "," + endmonth + "," + endyer);
            ArrayList prmnthlist = addPrMonth(yer, month, day, calenderId, endday, endmonth, endyer, monthName(month), "No");
            String pyeardata;
//            month = month+1;
            int i = 0, j = 0, days = totaldays(yer, month + 1, day), count = 0, k = 0;
//           //////////////////////// //////////////////////////////////////////////////////.println("totalnumofdays" + days);
//           //////////////////////// //////////////////////////////////////////////////////.println("totalnumofdays" + totalnumofdays);
            for (i = day; i <= days; i++) {
                k++;
                count++;

                pyeardata = i + "-" + monthName(month) + "-" + yer + ",";

//                ////////////////////////////////////////////////////.print(i + "-" + monthName(month) + "-" + yer + ",");
//               //////////////////////// //////////////////////////////////////////////////////.println("j"+j);
                yersarr = (String[]) prmnthlist.get(j);
                pyeardata = pyeardata + yersarr[0] + "," + yersarr[1] + "," + yersarr[2];
                // //////////////////////////////////////////////////////.println(count + "-" + pyeardata);
                pyearmdaylist.add(pyeardata);
                if (k == totaldays(yer, month + 1, day)) {
//                   //////////////////////// //////////////////////////////////////////////////////.println("k" + k);
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
//                       //////////////////////// //////////////////////////////////////////////////////.println("days1"+days);
                    } else {
                        month = month + 1;
                        days = totaldays(yer, month + 1, day);
//                       //////////////////////// //////////////////////////////////////////////////////.println("days2"+days);
                        i = 0;
                    }
//                   //////////////////////// //////////////////////////////////////////////////////.println("coutn"+count);

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
////////////////////////////////////////////////////////.println(crntday + "-" + monthName(crntcal.get(Calendar.MONTH)) + "-" + crntcal.get(Calendar.YEAR));
// //////////////////////////////////////////////////////.println(crntcal.getActualMaximum(Calendar.DAY_OF_MONTH));
                if (l > pmontstrthcal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    l = pmontstrthcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                }

                pmstrtdate = 1 + "-" + monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR);
                pmenddate = pmontstrthcal.getActualMaximum(Calendar.DAY_OF_MONTH) + "-" + monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR);
                pmcustname = monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR);
                pmontdata = l + "-" + monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR) + "," + pmstrtdate + "," + pmenddate + "," + pmcustname;
                //////////////////////////////////////////////////////.println(pmontdata);
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

// //// //////////////////////////////////////////////////////.println("count " + count);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }


        /*
         * try { //
         * //////////////////////////////////////////////////////.println(yer +
         * "," + month + "," + day + "," + calenderId + "," + endday + "," +
         * endmonth + "," + endyer + "," + monthName(month));
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
         * //////////////////////////////////////////////////////.println(yer +
         * "," + month + "," + day + "," + calenderId + "," + endday + "," +
         * endmonth + "," + endyer + "," + monthName(month)); int i = 0, j = 0,
         * days = totaldays(yer, month + 1, day), count = 0, k = 0,
         * totalnumofdays; totalnumofdays = gettotalnumofDays(day + "-" +
         * monthName(month) + "-" + yer, endday + "-" + monthName(endmonth) +
         * "-" + endyer); String pmontdata; String[] yersarr; for (i = day; i <=
         * days; i++) {
         *
         * k++; count++;
         *
         * //////////////////////////////////////////////////////.print(i + "-"
         * + monthName(month) + "-" + yer + ","); yersarr = (String[])
         * prmnthlist.get(j);
         *
         * pmontdata = i + "-" + monthName(month) + "-" + yer + "," + yersarr[0]
         * + "," + yersarr[1] + "," + yersarr[2]; //////////////////
         * //////////////////////////////////////////////////////.println("--m--"
         * + pmontdata); pmonthdaylist.add(pmontdata); if (count ==
         * totalnumofdays) { break; } ////////////////////
         * //////////////////////////////////////////////////////.println("for k
         * " + totaldays(yer, month + 1, day) + "====for next month==" +
         * totaldays(yer, month, day));
         *
         * if (k == totaldays(yer, month + 1, day)) {
         *
         * j++; k = 0; } if (i == days) { if (month == 11) { month = 0; i = 0;
         * yer = yer + 1; days = totaldays(yer, month + 1, day); } else { month
         * = month + 1; days = totaldays(yer, month + 1, day); i = 0; } }
         *
         * }
         * // ////////////////////////
         * //////////////////////////////////////////////////////.println("count
         * " + count); } catch (Exception e) { logger.error("Exception:",e);
        }
         */
        return pmonthdaylist;
    }

    public ArrayList cqerweDay(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {
        ArrayList cqerwdaylist = new ArrayList();
        String cqerwedayconcat, splitqerwday[];
        String[] cqerwekday = addpriorQrter(yer, month, day, calenderId, endyer, endmonth, endday, monthName(month), "NO");
        int limit = 0, i = 0;
        for (i = 0; i < cqerwekday.length; i++) {
            splitqerwday = cqerwekday[i].split(",");
            limit = gettotalnumofDays(splitqerwday[2].toString(), splitqerwday[3].toString());
//           //////////////////////// //////////////////////////////////////////////////////.println("limit" + limit);
            for (int j = 0; j < limit; j++) {
                //Added two columns for month number and year number for enhancement
                cqerwedayconcat = splitqerwday[0] + "," + splitqerwday[1] + "," + splitqerwday[2] + "," + splitqerwday[3] + "," + splitqerwday[5];
                //  cqerwedayconcat =  splitqerwday[2] + "," + splitqerwday[3] + "," + splitqerwday[5];
                cqerwdaylist.add(cqerwedayconcat);
//                //////////////////////////////////////////////////////.println(cqerwedayconcat);
            }
        }
        return cqerwdaylist;
    }

    public ArrayList clastqerweDay(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {
        ArrayList clastqerwdaylist = new ArrayList();
        String clastqerwedayconcat, splitlastqerwday[];
        String[] clastqerwekday = addpriorQrter(yer, month, day, calenderId, endyer, endmonth, endday, monthName(month), "YES");
        int limit = 0, i = 0;
        Calendar calender = Calendar.getInstance();
        calender.set(yer, month, day);
        calender.add(calender.MONTH, -3);
        for (i = 0; i < clastqerwekday.length; i++) {
            splitlastqerwday = clastqerwekday[i].split(",");
            limit = gettotalnumofDays(splitlastqerwday[2].toString(), splitlastqerwday[3].toString());
//           //////////////////////// //////////////////////////////////////////////////////.println("limit" + limit);

            for (int j = 0; j < limit; j++) {
                String lqtrDate = calender.get(Calendar.DATE) + "-" + (calender.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + calender.get(Calendar.YEAR);
                calender.add(calender.DATE, 1);
                //               clastqerwedayconcat = splitlastqerwday[0] + "," + splitlastqerwday[1] + "," + splitlastqerwday[2] + "," + splitlastqerwday[3] + "," + splitlastqerwday[5];
                clastqerwedayconcat = lqtrDate + "," + splitlastqerwday[2] + "," + splitlastqerwday[3] + "," + splitlastqerwday[5];
                clastqerwdaylist.add(clastqerwedayconcat);
                //////////////////////////////////////////////////////.println(clastqerwedayconcat);
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
        //////////////////////// //////////////////////////////////////////////////////.println("---in clast--" + yer + "," + month + "," + day + "," + calenderId + "," + endyer + "," + endmonth + "," + endday + "," + monthName(month));
        //  String[] lstyerqterday = addpriorQrter(yer, month, day, calenderId, endyer, endmonth, endday, monthName(month), "NO");
        String[] lstyerqterday = addpriorQrter(yer, month, day, calenderId, endyer, endmonth, endday, monthName(month), "NO");
        int limit = 0, i = 0;
        //added by bharathi reddy
        Calendar calender = Calendar.getInstance();
        calender.set(yer, month, day);
        // calender.add(calender.YEAR,-1);
        // //////////////////////////////////////////////////////.println("-----------------000-----------------------------------"+lstyerqterday);
        for (i = 0; i < lstyerqterday.length; i++) {
            //////////////////////////////////////////////////////.println("splitlastqerday--" + lstyerqterday[i]);
            splitlastqerday = lstyerqterday[i].split(",");
            limit = gettotalnumofDays(splitlastqerday[2].toString(), splitlastqerday[3].toString());
//           //////////////////////// //////////////////////////////////////////////////////.println("limit" + limit);
            for (int j = 0; j < limit; j++) {
                //        lstyerconcat = splitlastqerday[0] + "," + splitlastqerday[1] + "," + splitlastqerday[2] + "," + splitlastqerday[3] + "," + splitlastqerday[5];

                String lqtrDate = calender.get(Calendar.DATE) + "-" + (calender.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + calender.get(Calendar.YEAR);
                lstyerconcat = lqtrDate + "," + splitlastqerday[2] + "," + splitlastqerday[3] + "," + splitlastqerday[5];
                calender.add(Calendar.DATE, 1);
                clastyerqerdaylist.add(lstyerconcat);
                // //////////////////////////////////////////////////////.println(lstyerconcat);
            }
        }
        return clastyerqerdaylist;
    }

    public ArrayList cyearDay(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {
        ArrayList cyeardaylist = new ArrayList();
        int loopedns = gettotalnumofDays(day + "-" + monthName(month) + "-" + yer, endday + "-" + monthName(endmonth) + "-" + endyer);
//        //////////////////////////////////////////////////////.println(loopedns);
        for (int i = 0; i < loopedns; i++) {
            cyeardaylist.add(yer + "," + day + "-" + monthName(month) + "-" + yer + "," + endday + "-" + monthName(endmonth) + "-" + endyer + "," + yer);
//            //////////////////////////////////////////////////////.println(yer + "," + day + "-" + monthName(month) + "-" + yer + "," + endday + "-" + monthName(endmonth) + "-" + endyer + "," + yer);
        }
        return cyeardaylist;
    }

    public ArrayList lyearDay(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId) {
        ArrayList lyeardaylist = new ArrayList();

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
//        //////////////////////////////////////////////////////.println(lstyrloopedns);
        for (int i = 0; i < lstyrloopedns; i++) {

            lyeardaylist.add(lstyerdaystrt.get(Calendar.DATE) + "-" + (lstyerdaystrt.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + lstyerdaystrt.get(Calendar.YEAR) + "," + day + "-" + monthName(month) + "-" + yer + "," + endday + "-" + monthName(endmonth) + "-" + endyer + "," + yer);
            lstyerdayend.add(Calendar.YEAR, 1);
            //   //////////////////////////////////////////////////////.println(yer + "," + day + "-" + monthName(month) + "-" + yer + "," + endday + "-" + monthName(endmonth) + "-" + endyer + "," + yer);
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
            ////////////////////////////////////////////////////////.println("Start day of the Prior Week is " + cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR));
            cal2.add(Calendar.DATE, -7);
            // //////////////////////// //////////////////////////////////////////////////////.println("End day of the Prior Week is " + cal2.get(Calendar.DATE) + "-" + (cal2.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal2.get(Calendar.YEAR));
            day3 = day3 + 7;
        }
    }

    public String[] getpriorweeks(int day, int month, int yer, int endday, int endmonth, int endyer, String flag) {
        // //////////////////////// //////////////////////////////////////////////////////.println("getinstance");

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        int numofweeks = getNumWeeksForYear(yer);
        //////////////////// //////////////////////////////////////////////////////.println("num of weeks---"+numofweeks);
        String detsstr = "";
        String detsList[] = new String[numofweeks - 1];
        String strDate = "";
        String endDate = "";
        int weekNo = 0;
        for (int w = 1; w < numofweeks; w++) {
//           //////////////////////// //////////////////////////////////////////////////////.println("Week No:-" + w);
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
            //////////////////// //////////////////////////////////////////////////////.println("detsstr---"+detsstr);
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
//       //////////////////////// //////////////////////////////////////////////////////.println("startdate" + startdate);
//       //////////////////////// //////////////////////////////////////////////////////.println("enddate" + enddate);
        String yrstrtarr[];
        yrstrtarr = startdate.split("-");
        int strtday = Integer.parseInt(yrstrtarr[0].toString());
        String strtmonth = yrstrtarr[1];
        int i = 0;
        for (i = 0; i < monthName.length; i++) {
            if (strtmonth.equalsIgnoreCase(monthName[i])) {
//               //////////////////////// //////////////////////////////////////////////////////.println("index is " + i);
                break;
            }
        }
        int strtyear = Integer.parseInt(yrstrtarr[2].toString());
//       //////////////////////// //////////////////////////////////////////////////////.println("strtmonth" + strtmonth);
        String yrendarr[];
        yrendarr = enddate.split("-");
        int endday = Integer.parseInt(yrendarr[0].toString());
        String endmnth = yrendarr[1];
        int j = 0;
        for (j = 0; j < monthName.length; j++) {
            if (endmnth.equalsIgnoreCase(monthName[j])) {
//               //////////////////////// //////////////////////////////////////////////////////.println("index is " + j);
                break;
            }
        }
        int endyer = Integer.parseInt(yrendarr[2].toString());
//       //////////////////////// //////////////////////////////////////////////////////.println("endmnth" + endmnth);
        cal1.set(strtyear, i, strtday);
        cal2.set(endyer, j, endday);
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        long diff = milis2 - milis1;
        long diffDays = (diff / (24 * 60 * 60 * 1000)) + 1;
//       //////////////////////// //////////////////////////////////////////////////////.println("In This Year Total Days: " + (diffDays));
        int days = (int) diffDays;
        return days;
    }

    public int getNumWeeksForYear(int year) {
        Calendar c = Calendar.getInstance();
        c.set(year, 0, 1);
//        //////////////////////////////////////////////////////.println(c.getMaximum(Calendar.WEEK_OF_YEAR));
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

        // //////////////////////////////////////////////////////.println(isLeapYear);
        return isLeapYear;
    }

    public int totaldays(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month - 1, date);
        int idays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//       //////////////////////// //////////////////////////////////////////////////////.println("Number of Days: " + idays);
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
     * //////////////////////////
     * //////////////////////////////////////////////////////.println("PR YEAR
     * finalQuery---" + finalQuery); Createlist.add(finalQuery); //
     * pbdb.executeMultiple(Createlist); } catch (Exception ex) {
     * logger.error("Exception:",ex); } }
     */

    public ArrayList cWeek(int yer, int month, int day, int endday, int endmonth, int endyer, int calenderId, String flag) {

        ArrayList crntweklist = new ArrayList();
        ArrayList crnmnthlist = new ArrayList();
        Calendar cal = Calendar.getInstance();
        String ddate, strtdate, enddate, cwstrtdate, cwenddate, custname, cwstrtmntdate, cwendmntdate, fullstring, cmstrtdate, cmenddate, cmcustname, crntmnthstr;
        int daysOfYear, daysofyear, cweek, cwyear, i = 0, days = 0, count = 0, cmonth;
        days = totaldays(yer, month + 1, day);
        daysOfYear = gettotalnumofDays(day + "-" + monthName(month) + "-" + yer, endday + "-" + monthName(endmonth) + "-" + endyer);
// ////////////////////// //////////////////////////////////////////////////////.println("days:" + days);
// ////////////////////// //////////////////////////////////////////////////////.println("daysof year" + daysOfYear);
        for (i = day; i <= days; i++) {
            count++;
            ddate = i + "-" + monthName(month) + "-" + yer;
            cmonth = month + 1;
            cmstrtdate = 1 + "-" + monthName(month) + "-" + yer;
            cmenddate = days + "-" + monthName(month) + "-" + yer;
            cmcustname = monthName(month) + "-" + yer;
// ////////////////////// //////////////////////////////////////////////////////.println("ddate is" + ddate);
            strtdate = ddate;
            enddate = ddate;
            cal.set(yer, month, i);
            daysofyear = cal.get(Calendar.DAY_OF_YEAR);
            cweek = cal.get(Calendar.WEEK_OF_YEAR);
            cwyear = cal.get(Calendar.YEAR);
            custname = "W-" + cweek + "-" + yer;
// String dayis = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
// ////////////////////// //////////////////////////////////////////////////////.println("dayis" + dayis);
            cal.setFirstDayOfWeek(Calendar.SUNDAY); // Make sure that the calendar starts its week at Sunday
// cal.setTime(new Date());
// ////////////////////// //////////////////////////////////////////////////////.println("Current week is: " + cal.get(Calendar.WEEK_OF_YEAR));
// ////////////////////// //////////////////////////////////////////////////////.println("Current week in this month is: " + cal.get(Calendar.WEEK_OF_MONTH));
// ////////////////////// //////////////////////////////////////////////////////.println("Today is " + cal.getTime() );
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
// ////////////////////// //////////////////////////////////////////////////////.println("Start of week: " + cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR));
// //////////////////////////////////////////////////////.println(cal.get(Calendar.MONTH));

            cwstrtdate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
// //////////////////////////////////////////////////////.println(month+"month");
            cwstrtmntdate = cwstrtdate;
            if (month != cal.get(Calendar.MONTH)) {
                cwstrtmntdate = 1 + "-" + monthName(month) + "-";
                if (yer != cal.get(Calendar.YEAR)) {
                    cwstrtmntdate = cwstrtmntdate + yer;
                } else {
                    cwstrtmntdate = cwstrtmntdate + cal.get(Calendar.YEAR);
                }
            }

// //////////////////////////////////////////////////////.println(cwstrtdate);
            cal.add(Calendar.DAY_OF_YEAR, 6);
// ////////////////////// //////////////////////////////////////////////////////.println("End of week: " + cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR)); //+ cal.getTime() );
            cwenddate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            cwendmntdate = cwenddate;
            if (month != cal.get(Calendar.MONTH)) {
                cal.add(Calendar.MONTH, -1);
                cwendmntdate = cal.getActualMaximum(Calendar.DAY_OF_MONTH) + "-" + monthName(month) + "-";
//                //////////////////////////////////////////////////////.println(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                cal.add(Calendar.MONTH, 1);
                if (yer != cal.get(Calendar.YEAR)) {
                    cwendmntdate = cwendmntdate + yer;
                } else {
                    cwendmntdate = cwendmntdate + cal.get(Calendar.YEAR);
                }
            }
//            //////////////////////////////////////////////////////.println(cwendmntdate);


            /*
             * //////////////////////
             * //////////////////////////////////////////////////////.println("before
             * inserting");
             * //////////////////////////////////////////////////////.println(ddate);
             * //////////////////////////////////////////////////////.println(strtdate);
             * //////////////////////////////////////////////////////.println(enddate);
             * //////////////////////////////////////////////////////.println(daysofyear);
             * //////////////////////////////////////////////////////.println(cweek);
             * //////////////////////////////////////////////////////.println(cwyear);
             * //////////////////////////////////////////////////////.println(cwstrtdate);
             * //////////////////////////////////////////////////////.println(cwenddate);
             * //////////////////////////////////////////////////////.println(cwstrtmntdate);
             * //////////////////////////////////////////////////////.println(cwendmntdate);
             * //////////////////////////////////////////////////////.println(custname);
             */
            fullstring = ddate + "," + strtdate + "," + enddate + "," + daysofyear + "," + cweek + "," + cwyear + "," + cwstrtdate + "," + cwenddate + "," + cwstrtmntdate + "," + cwendmntdate + "," + custname;
            if (flag.equalsIgnoreCase("month")) {
                crntmnthstr = cmonth + "," + cwyear + "," + cmstrtdate + "," + cmenddate + "," + cmcustname;

// //////////////////////////////////////////////////////.println(crntmnthstr);
                crnmnthlist.add(crntmnthstr);
            }
            if (flag.equalsIgnoreCase("cweek")) {
                ////////////////////////   //////////////////////////////////////////////////////.println(fullstring);
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
////////////////////////////////////////////////////////.println(crntday + "-" + monthName(crntcal.get(Calendar.MONTH)) + "-" + crntcal.get(Calendar.YEAR));
// //////////////////////////////////////////////////////.println(crntcal.getActualMaximum(Calendar.DAY_OF_MONTH));
                if (l > pmontstrthcal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    l = pmontstrthcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                }

                pmstrtdate = 1 + "-" + monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR);
                pmenddate = pmontstrthcal.getActualMaximum(Calendar.DAY_OF_MONTH) + "-" + monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR);
                pmcustname = monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR);
                pmontdata = l + "-" + monthName(pmontstrthcal.get(Calendar.MONTH)) + "-" + pmontstrthcal.get(Calendar.YEAR) + "," + pmstrtdate + "," + pmenddate + "," + pmcustname;
                //////////////////////// //////////////////////////////////////////////////////.println(pmontdata);
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

// //// //////////////////////////////////////////////////////.println("count " + count);
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
        //////////////////////// //////////////////////////////////////////////////////.println("PR DAY finalQuery---" + finalQuery);
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
//                //////////////////////////////////////////////////////.println("nameofday"+nameofday);
                if (nameofday.equalsIgnoreCase(wekholiday1) || nameofday.equalsIgnoreCase(wekholiday2)) {
//                    //////////////////////////////////////////////////////.println(wekholiday1);
//                    //////////////////////////////////////////////////////.println(wekholiday2);
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
                //////////////////////////////////////////////////////.println("PR DAY finalQuery---" + finalQuery);
                Createlist.add(finalQuery);
                ////////////////////////////////////////////////////////.println("Dates Are---" + i + "-" + monthName[j] + "-" + yer);
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
//                           //////////////////////// //////////////////////////////////////////////////////.println("mnt" + mnth);
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
//       //////////////////////// //////////////////////////////////////////////////////.println("count is" + count);
    }
}
