/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.timesetup;

import java.util.Calendar;
import java.util.Locale;

/**
 *
 * @author progen
 */
public class YearPriorNextHelper extends PriorNextHelper {

    @Override
    public String[] findPrior(Calendar startCalendar, Calendar endCalendar) {
        //
        // 
        String priorYearValues[] = new String[6];
        int daypr = startCalendar.get(Calendar.DATE);
        long diffDays = getTotalNumberOfDays(startCalendar, endCalendar);
        startCalendar.add(Calendar.DATE, (int) -diffDays);
        String startdate = daypr + "-" + startCalendar.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault()) + "-" + startCalendar.get(Calendar.YEAR);
        String startdate1 = daypr + "-" + startCalendar.get(Calendar.MONTH) + "-" + startCalendar.get(Calendar.YEAR);
        endCalendar.add(Calendar.DATE, (int) -diffDays);
        String enddate = endCalendar.get(Calendar.DATE) + "-" + endCalendar.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault()) + "-" + endCalendar.get(Calendar.YEAR);
        String enddate1 = endCalendar.get(Calendar.DATE) + "-" + endCalendar.get(Calendar.MONTH) + "-" + endCalendar.get(Calendar.YEAR);
        startCalendar.set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DATE));
        endCalendar.set(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DATE));
        long milisp1 = startCalendar.getTimeInMillis();
        long milisp2 = endCalendar.getTimeInMillis();
        long diffp = milisp2 - milisp1;
        long diffDaysp = diffp / (24 * 60 * 60 * 1000);
        priorYearValues[0] = String.valueOf(startCalendar.get(Calendar.YEAR));
        priorYearValues[1] = startdate;
        priorYearValues[2] = enddate;
        priorYearValues[3] = String.valueOf(getTotalNumberOfDays(startCalendar, endCalendar));
        priorYearValues[4] = startdate1;
        priorYearValues[5] = enddate1;
        return priorYearValues;
    }

    @Override
    public String[] findNext(Calendar startCalendar, Calendar endCalendar) {
        String nextYearValues[] = new String[6];
        int daypr = startCalendar.get(Calendar.DATE);
        long diffDays = getTotalNumberOfDays(startCalendar, endCalendar);
        startCalendar.add(Calendar.YEAR, 1);
        String startdate = daypr + "-" + startCalendar.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault()) + "-" + startCalendar.get(Calendar.YEAR);
        String startdate1 = daypr + "-" + startCalendar.get(Calendar.MONTH) + "-" + startCalendar.get(Calendar.YEAR);
        endCalendar.add(Calendar.YEAR, 1);
        String enddate = endCalendar.get(Calendar.DATE) + "-" + endCalendar.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault()) + "-" + endCalendar.get(Calendar.YEAR);
        String enddate1 = endCalendar.get(Calendar.DATE) + "-" + endCalendar.get(Calendar.MONTH) + "-" + endCalendar.get(Calendar.YEAR);
        //startCalendar.set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DATE));
        // endCalendar.set(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DATE));
        long milisp1 = startCalendar.getTimeInMillis();
        long milisp2 = endCalendar.getTimeInMillis();
        long diffp = milisp2 - milisp1;
        long diffDaysp = diffp / (24 * 60 * 60 * 1000);
        nextYearValues[0] = String.valueOf(startCalendar.get(Calendar.YEAR));
        nextYearValues[1] = startdate;
        nextYearValues[2] = enddate;
        nextYearValues[3] = String.valueOf(getTotalNumberOfDays(startCalendar, endCalendar));
        nextYearValues[4] = startdate1;
        nextYearValues[5] = enddate1;
        return nextYearValues;
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
