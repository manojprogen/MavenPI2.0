/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.timesetup;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class CalenSetUpAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(CalenSetUpAction.class);
    private final static String SUCCESS = "success";

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("getEndDate", "getEndDate");
        map.put("saveCalender", "saveCalender");
        map.put("duplicateCalenderName", "duplicateCalenderName");
        map.put("weekholidayskey", "weekHolidays");
        map.put("yearholidayskey", "yearHolidays");
        map.put("exceptionalHolidayskey", "exceptionalHolidays");
        map.put("valiDateCalnder", "valiDateCalnder");
        return map;
    }

    public ActionForward getEndDate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String startYear = request.getParameter("startYear");

//        String status = "";
//        int totaldaysofyer = 0;
        calendarInserting ci = new calendarInserting();
        String yrstrtarr[];
        yrstrtarr = startYear.split("/");
        int day = Integer.parseInt(yrstrtarr[1].toString());
        int mnth = Integer.parseInt(yrstrtarr[0].toString());
        int yer = Integer.parseInt(yrstrtarr[2].toString());
        String endyear = "";
        String endMonth = "";
        String endDay = "";
        boolean isLeapYear = ci.chkLeapyear(yer);
        String noofdays[] = {"31", "28", "31", "30", "31", "30", "31", "31", "30", "31", "30", "31"};

        if (mnth == 1 && day == 1) {
            endMonth = "12";
            endDay = "31";
            endyear = String.valueOf(yer);
        } else {
            if (isLeapYear && mnth <= 2) {
                noofdays[1] = "29";
            }
            endyear = String.valueOf(yer + 1);

            if (day == 1) {
                endMonth = String.valueOf(mnth - 1);
                endDay = noofdays[mnth - 2];
            } else {
                endMonth = String.valueOf(mnth);
                endDay = String.valueOf(day - 1);
            }


        }
        if (endMonth.length() < 2) {
            endMonth = "0" + endMonth;
        }
        if (endDay.length() < 2) {
            endDay = "0" + endDay;
        }


        /*
         * boolean isLeapYear = ci.chkLeapyear(yer); boolean isendLeapYear =
         * ci.chkLeapyear(yer+1); if (isLeapYear == true) { if(mnth>2){
         * if(mnth==3 && day==1){ totaldaysofyer = 361; }else{ totaldaysofyer =
         * 364; } }else if(mnth<=2) { if(mnth==2 && day>28){ totaldaysofyer =
         * 363; }else { totaldaysofyer = 363; } }
         *
         *
         * }
         *
         * else{ totaldaysofyer = 365-1;
         *
         * }
         *
         * Calendar calendar = Calendar.getInstance(); calendar.set(yer, mnth,
         * day); calendar.add(Calendar.DAY_OF_YEAR,(totaldaysofyer)); String
         * endyear=String.valueOf(calendar.get(1)); String
         * endMonth=String.valueOf(calendar.get(2)); String
         * endDay=String.valueOf(calendar.get(5)); if(calendar.get(2)<=9){
         * if(calendar.get(2)==0){ endMonth="12"; }else{
         * endMonth="0"+calendar.get(2); } } if(calendar.get(5)<=9){
         * endDay="0"+calendar.get(5); }
         *
         *
         * /*
         * //////////////////////////////////////////////////////.println("isendLeapYear--"+isendLeapYear);
         * if(isendLeapYear && calendar.get(2)>=2){ if(calendar.get(2)==2){
         * if(calendar.get(5)>28){ endDay=String.valueOf(calendar.get(5)+1);
         * }else if(calendar.get(2)==1 && calendar.get(5)>=28){ endDay="31"; }
         *
         * }
         * }
         */
//        String enddate = endMonth + "/" + endDay + "/" + endyear;

        response.getWriter().print(endMonth + "/" + endDay + "/" + endyear);
        return null;

    }

    public ActionForward saveCalender(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PbDb pbdb = new PbDb();
        TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
        String finalQuery = "";
        String calName = request.getParameter("calenderName");
        String startYear = request.getParameter("startYear");
        String endYear = request.getParameter("endYear");
        String connid = request.getParameter("conid");
        boolean fromStandTimeDim = Boolean.parseBoolean(request.getParameter("fromStandTimeDim"));
        ////.println("connid \t" + connid);
        calendarInserting ci = new calendarInserting();
        ProgenCalendarBuilder progenCalendarBuilder = new ProgenCalendarBuilder();
        //code to insert calender in metadata

        String addCalender = resBundle.getString("addCalender");
        int seqaddCalender = pbdb.getSequenceNumber("select PRG_CALENDER_SETUP_SEQ.nextval from dual");
        Object obj[] = new Object[7];
        obj[0] = seqaddCalender;
        obj[1] = "Enterprise";
        obj[2] = calName;
        obj[3] = "PR_DAY_DENOM_" + seqaddCalender;
        obj[4] = startYear;
        obj[5] = endYear;
        obj[6] = connid;
        finalQuery = pbdb.buildQuery(addCalender, obj);
        // pbdb.execModifySQL(finalQuery);
        //ends
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        DateFormat formatter;
        Date date;
        formatter = new SimpleDateFormat("MM/dd/yy");
        date = (Date) formatter.parse(startYear);
        calendar1.setTime(date);
        date = (Date) formatter.parse(endYear);
        calendar2.setTime(date);
        boolean returnResult = progenCalendarBuilder.buildCalendar(calendar1, calendar2, connid, calName, fromStandTimeDim);
        //ci.insertcal(startYear, endYear, seqaddCalender,connid);
        response.getWriter().print(returnResult);
        return null;

    }

    public ActionForward duplicateCalenderName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PbDb pbdb = new PbDb();
        String newCalenderName = request.getParameter("CalenderName");
        String connid = request.getParameter("conid");
        ////.println("connid" + connid);
        TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
        String getCalenderNames = resBundle.getString("getCalenderNames");
        Object[] claname = new Object[1];
        claname[0] = connid;
        String buildnamesqry = pbdb.buildQuery(getCalenderNames, claname);
        ////.println("buildnamesqry " + buildnamesqry);
        PbReturnObject pbro = pbdb.execSelectSQL(buildnamesqry);
        String CalenderName = "";
        String status = "NO";
        for (int i = 0; i < pbro.getRowCount(); i++) {
            CalenderName = pbro.getFieldValueString(i, 1);
            if (CalenderName.equalsIgnoreCase(newCalenderName)) {
                status = "YES";
                break;
            }
        }
        response.getWriter().print(status);
        return null;
    }

    public ActionForward weekHolidays(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableid = request.getParameter("tableid");
        String wekholiday = request.getParameter("wkday");
        String tablename = "PR_DAYS_" + tableid;
        Connection con = null;
        try {
            TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
            String updateholidays = resBundle.getString("updatewekholidays");
            Object[] updateobj = new Object[2];
            updateobj[0] = tablename;
            updateobj[1] = wekholiday;
            PbDb pbdb = new PbDb();
            String query = pbdb.buildQuery(updateholidays, updateobj);
            //////////////////////////////////////////////////////.println(query);
            con = ProgenConnection.getInstance().getConnection();
            Statement st = con.createStatement();
            st.execute(query);
            st.close();
            con.close();
        } catch (Exception e) {
            logger.error("Exception:", e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return null;
    }

    public ActionForward yearHolidays(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Connection con = null;
        try {
            String limit = request.getParameter("limit");
            //////////////////////////////////////////////////////.println("holidays" + limit);
            String tableid = request.getParameter("tableid");
            //////////////////////////////////////////////////////.println(tableid);
            int max = Integer.parseInt(limit);
            String[] holiday = new String[max];
            String[] reason = new String[max];
            String[] yerholidayquery = new String[max];

            PbDb pbdb = new PbDb();
            TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
            String updateholidays = resBundle.getString("updateyerholidays");
            Object[] updateobj = new Object[3];
            updateobj[0] = "PR_DAYS_" + tableid;
            con = ProgenConnection.getInstance().getConnection();
            Statement st = con.createStatement();
            for (int i = 0; i < max; i++) {
                if (!(request.getParameter("holiday" + i).equalsIgnoreCase(null))) {
                    String yrstrtarr[];
                    yrstrtarr = request.getParameter("holiday" + i).split("/");
                    int day = Integer.parseInt(yrstrtarr[1].toString());
                    int mnth = Integer.parseInt(yrstrtarr[0].toString());
                    int yer = Integer.parseInt(yrstrtarr[2].toString());
                    mnth = mnth - 1;
                    holiday[i] = day + "-" + monthName(mnth) + "-" + yer;
                    reason[i] = request.getParameter("reason" + i);
                    updateobj[1] = reason[i];
                    updateobj[2] = holiday[i];
                    yerholidayquery[i] = pbdb.buildQuery(updateholidays, updateobj);
                    st.execute(yerholidayquery[i]);
                }
            }
            st.close();
            con.close();
        } catch (Exception e) {
            logger.error("Exception:", e);
        } finally {
            if (con != null) {
                con = null;
            }
        }
        return null;
    }

    public ActionForward valiDateCalnder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            boolean returnResult = false;
            String startDateString = request.getParameter("startYear");
            String endDateString = request.getParameter("endYear");
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);
            String schedulerFlag = "";
            if (request.getParameter("scheduler") != null) {
                schedulerFlag = request.getParameter("scheduler");
            }

            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            ProgenCalendarBuilder progenCalendarBuilder = new ProgenCalendarBuilder();
            int calVal = endCalendar.compareTo(startCalendar);
            if (schedulerFlag.equalsIgnoreCase("")) {
                if (calVal == 1) {
                    returnResult = true;
                    long totalDays = progenCalendarBuilder.getTotalNumberOfDays((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
                    if (totalDays >= 365) {
                        startCalendar.add(Calendar.DATE, -1);
                        startCalendar.set(Calendar.YEAR, endCalendar.get(Calendar.YEAR));
                        int compVal = endCalendar.compareTo(startCalendar);
                        if (compVal == 0) {
                            returnResult = true;
                        } else {
                            returnResult = false;
                        }
                    } else {
                        returnResult = false;
                    }
                } else {
                    returnResult = false;

                }
            } else {
                if (calVal == -1) {
                    returnResult = false;
                } else {
                    returnResult = true;
                }

            }
            out.print(returnResult);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward exceptionalHolidays(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
            PbDb pbdb = new PbDb();
            String exceptionalholidays = resBundle.getString("updateexceptholidays");
            Connection con = ProgenConnection.getInstance().getConnection();
            Statement st = con.createStatement();
            String date = request.getParameter("date");
            String workstatus = request.getParameter("status");
            String reason = request.getParameter("reason");
            String tableid = request.getParameter("tableid");
            String yrstrtarr[];
            yrstrtarr = date.split("/");
            int day = Integer.parseInt(yrstrtarr[1].toString());
            int mnth = Integer.parseInt(yrstrtarr[0].toString());
            int yer = Integer.parseInt(yrstrtarr[2].toString());
            mnth = mnth - 1;
            date = day + "-" + monthName(mnth) + "-" + yer;
            Object[] updateobj = new Object[4];
            updateobj[0] = "PR_DAYS_" + tableid;
            updateobj[1] = workstatus;
            updateobj[2] = reason;
            updateobj[3] = date;
            String updtquery = pbdb.buildQuery(exceptionalholidays, updateobj);
            st.execute(updtquery);
            if (con != null) {
                con = null;
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return null;
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
}
