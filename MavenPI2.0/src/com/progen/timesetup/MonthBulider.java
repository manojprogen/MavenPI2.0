/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.timesetup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import prg.db.PbDb;

/**
 *
 * @author progen
 */
class MonthBulider extends CalendarBuilder {

    private String execute = "yes";
    TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
    PbDb pbDb = new PbDb();

    public MonthBulider() {
    }

    @Override
    protected ArrayList buildCalendarData(Date startDate, Date endDate, String calendarId) {
        ArrayList queryList = new ArrayList();
        ArrayList prmonthlist = new ArrayList();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        Calendar calForStartDe = Calendar.getInstance();
        Calendar calForEndDe = Calendar.getInstance();
        calForStartDe.setTime(startDate);
        calForEndDe.setTime(endDate);
        startCalendar.setTime(startDate);
        endCalendar.setTime(endDate);
        String addPrMonth = null;
        if (calendarId == null) {
            addPrMonth = resBundle.getString("addPrMonth1");
        } else {
            addPrMonth = resBundle.getString("addPrMonth");
        }
        Object[] obj1 = null;
        String strDate = "";
//        String strDate = startCalendar.get(Calendar.DATE) + "-" + startCalendar.getDisplayName(2, Calendar.SHORT, Locale.getDefault()) + "-" + startCalendar.get(Calendar.YEAR);

        String dbyear = String.valueOf(startCalendar.get(Calendar.YEAR));
        String dbcustName = String.valueOf(startCalendar.get(Calendar.YEAR));
        String monthName = startCalendar.getDisplayName(2, Calendar.SHORT, Locale.getDefault());
        int monthex = startCalendar.get(Calendar.MONTH) + 1;

        int dayex = startCalendar.get(Calendar.DATE);
        String finalQuery = "";
        obj1 = new Object[7];
        int mnthCount = 0;
        for (int i = (startCalendar.get(Calendar.MONTH) + 1); i <= 12 + monthex - 1; i++) {
            String prmonthdetails[] = new String[3];
            obj1[0] = calendarId;
            mnthCount = mnthCount + 1;
            obj1[1] = mnthCount;
            obj1[2] = dbyear;

            // calendar.set(yer, i, dayex);
            int maxDay = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            if (dayex == 31 && monthex == 0) {
                dayex = 1;
            }

            strDate = dayex + "-" + (startCalendar.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + startCalendar.get(Calendar.YEAR);
            obj1[3] = strDate;
            prmonthdetails[0] = strDate;
            startCalendar.add(Calendar.DATE, maxDay);
            startCalendar.add(Calendar.DATE, -1);
            String endDatestr = startCalendar.get(Calendar.DATE) + "-" + (startCalendar.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + startCalendar.get(Calendar.YEAR);

            prmonthdetails[1] = endDatestr;
            obj1[4] = endDatestr;
            prmonthdetails[2] = monthName + "-" + dbcustName;
            monthName = (startCalendar.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault()));
            startCalendar.add(Calendar.DATE, 1);
            obj1[5] = monthName;
            obj1[6] = monthName + "-" + dbcustName;
            dbcustName = String.valueOf(startCalendar.get(Calendar.YEAR));
            finalQuery = pbDb.buildQuery(addPrMonth, obj1);
            prmonthlist.add(prmonthdetails);
            if (execute.equalsIgnoreCase("yes")) {
                queryList.add(finalQuery);

            }
        }
        if (execute.equalsIgnoreCase("yes")) {
            //  
            //
            ArrayList tempList = monthBuliderForDenom(calForStartDe, calForEndDe, calendarId);

            queryList.addAll(tempList);

        }


        if (execute.equalsIgnoreCase("yes")) {
            //    
            return queryList;
        } else {
            return prmonthlist;
        }

    }

    public ArrayList monthBuliderForDenom(Calendar StCalendar, Calendar endCalendar, String calendarId) {
        this.execute = "no";
        ArrayList queryList = new ArrayList();
        String addPrMonthDenom = null;
        if (calendarId == null) {
            addPrMonthDenom = resBundle.getString("addPrMonthDenom1");
        } else {
            addPrMonthDenom = resBundle.getString("addPrMonthDenom");
        }
        MonthPriorNextHelper monthPriorNextHelper = new MonthPriorNextHelper();
        String dets2[] = monthPriorNextHelper.findPrior(StCalendar, endCalendar);
        String finalQuery = "";

        Object obj1[] = new Object[16];
        obj1[0] = calendarId;
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
            finalQuery = pbDb.buildQuery(addPrMonthDenom, obj1);
            queryList.add(finalQuery);

        }
        // 
        this.execute = "yes";
        return queryList;
    }
}
