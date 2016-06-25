/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.timesetup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.log4j.Logger;

/**
 *
 * @author progen
 */
public class WeekPriorNextHelper extends PriorNextHelper {

    private String weekFlag;
    public static Logger logger = Logger.getLogger(WeekPriorNextHelper.class);

    @Override
    public String[] findPrior(Calendar startCalendar, Calendar endCalendar) {
        Calendar StartCal1 = (Calendar) startCalendar.clone();
        Calendar calendar = (Calendar) startCalendar.clone();
        int numofweeks = getNumWeeksForYear(StartCal1.get(Calendar.YEAR));
        //////////////////// ////////////////////////.println("num of weeks---"+numofweeks);
        String detsstr = "";
        String detsList[] = new String[numofweeks - 1];
        String strDate = "";
        String endDate = "";
        int weekNo = 0;
        for (int w = 1; w < numofweeks; w++) {
//           //////////////////////// ////////////////////////.println("Week No:-" + w);
            weekNo = w;

            if (weekFlag.equalsIgnoreCase("yes")) {
                StartCal1.add(Calendar.DATE, -7);
            }

            if (weekFlag.equalsIgnoreCase("yes")) {
                calendar.add(Calendar.DATE, -7);
            }
            strDate = StartCal1.get(Calendar.DATE) + "-" + (StartCal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + StartCal1.get(Calendar.YEAR);
            calendar.add(Calendar.DATE, 6);
            endDate = calendar.get(Calendar.DATE) + "-" + (calendar.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + calendar.get(Calendar.YEAR);

            detsstr = weekNo + "," + StartCal1.get(Calendar.YEAR) + "," + strDate + "," + endDate + "," + getTotalNumofDays(strDate, endDate);
            //////////////////// ////////////////////////.println("detsstr---"+detsstr);
            detsList[w - 1] = detsstr;
            StartCal1.add(Calendar.DATE, 7);
            calendar.add(Calendar.DATE, 1);
        }
        return detsList;
    }

    @Override
    public String[] findNext(Calendar startCalendar, Calendar endCalendar) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getWeekFlag() {
        return weekFlag;
    }

    public void setWeekFlag(String weekFlag) {
        this.weekFlag = weekFlag;
    }

    public int getNumWeeksForYear(int year) {
        Calendar c = Calendar.getInstance();
        c.set(year, 0, 1);
//        ////////////////////////.println(c.getMaximum(Calendar.WEEK_OF_YEAR));
        return c.getMaximum(Calendar.WEEK_OF_YEAR);
    }

    public int getTotalNumofDays(String StDate, String eDate) {
        int days = 0;

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date startDate = null, endDate = null;
        try {
            startDate = dateFormat.parse(StDate);
            endDate = dateFormat.parse(eDate);

        } catch (ParseException e) {
            logger.error("Exception:", e);
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(startDate);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(endDate);
        long milis1 = calendar1.getTimeInMillis();
        long milis2 = calendar2.getTimeInMillis();
        long diff = milis2 - milis1;
        return (int) ((diff / (24 * 60 * 60 * 1000)) + 1);
    }
}
