/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.timesetup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import prg.db.PbDb;

/**
 *
 * @author progen
 */
class YearBuilder extends CalendarBuilder {

    TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();

    public ArrayList buildCalendarData(Date startDate, Date endDate, String calendarId) {
        String addPrYear = null;
        if (calendarId == null) {
            addPrYear = resBundle.getString("addPrYear1");
        } else {
            addPrYear = resBundle.getString("addPrYear");
        }

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        PbDb pbDb = new PbDb();
        String finalQuery = "";
        YearPriorNextHelper yearPriorNextHelper = new YearPriorNextHelper();
        ArrayList<String> queryList = new ArrayList();
        endCalendar.setTime(endDate);
        Object[] yearObj = new Object[5];
//        int month=startCalendar.get(Calendar.MONTH);
//        int endmonth=endCalendar.get(Calendar.MONTH);
//        month=month+1;
//        endmonth=endmonth+1;
        String month = new SimpleDateFormat("MMM").format(startDate);
        String endmonth = new SimpleDateFormat("MMM").format(endDate);
        yearObj[0] = calendarId;
        yearObj[1] = startCalendar.get(Calendar.YEAR);
        yearObj[2] = startCalendar.get(Calendar.DATE) + "-" + month + "-" + startCalendar.get(Calendar.YEAR);
        yearObj[3] = endCalendar.get(Calendar.DATE) + "-" + endmonth + "-" + endCalendar.get(Calendar.YEAR);
        yearObj[4] = startCalendar.get(Calendar.YEAR);
        finalQuery = pbDb.buildQuery(addPrYear, yearObj);
        queryList.add(finalQuery);

        String insertInPRYearDenom = null;
        if (calendarId == null) {
            insertInPRYearDenom = resBundle.getString("addPrYeardenom1");
        } else {
            insertInPRYearDenom = resBundle.getString("addPrYeardenom");
        }

        String priorYearValues[] = yearPriorNextHelper.findPrior((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        Object[] yearDenomObj = new Object[9];
        yearDenomObj[0] = calendarId;
        yearDenomObj[1] = startCalendar.get(Calendar.YEAR);
        yearDenomObj[2] = startCalendar.get(Calendar.DATE) + "-" + month + "-" + startCalendar.get(Calendar.YEAR);
        yearDenomObj[3] = endCalendar.get(Calendar.DATE) + "-" + endmonth + "-" + endCalendar.get(Calendar.YEAR);
        yearDenomObj[4] = yearPriorNextHelper.getTotalNumberOfDays(startCalendar, endCalendar);
        yearDenomObj[5] = priorYearValues[0];
        yearDenomObj[6] = priorYearValues[1];
        yearDenomObj[7] = priorYearValues[2];
        yearDenomObj[8] = priorYearValues[3];
        finalQuery = pbDb.buildQuery(insertInPRYearDenom, yearDenomObj);
        queryList.add(finalQuery);
        // 
        //  
        return queryList;

    }
}
