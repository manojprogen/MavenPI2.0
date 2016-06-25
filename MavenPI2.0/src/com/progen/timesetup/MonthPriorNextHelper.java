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
public class MonthPriorNextHelper extends PriorNextHelper {

    private String[] monthNames = {"JAN", "FEB",
        "MAR", "APR", "MAY", "JUN", "JUL",
        "AUG", "SEP", "OCT", "NOV",
        "DEC"};

    @Override
    public String[] findPrior(Calendar startCalendar, Calendar endCalendar) {
        //
        // 
        Calendar cal1 = (Calendar) startCalendar.clone();
        Calendar cal2 = (Calendar) startCalendar.clone();
        Calendar endcal = (Calendar) endCalendar.clone();
        YearPriorNextHelper yearPriorNextHelper = new YearPriorNextHelper();
        String dets1[] = yearPriorNextHelper.findPrior(startCalendar, endCalendar);

        String pyearstrDate = dets1[1];
        String pyearenddate = dets1[2];
//       //////////////////////// ////////////////////////.println("pyearstrDate---" + pyearstrDate + "-------pyearenddate----" + pyearenddate);
        String pyeardets[] = pyearstrDate.split("-");
        Calendar prirYearcal = Calendar.getInstance();
        prirYearcal.set(Integer.parseInt(pyeardets[2]), monthNumber(pyeardets[1]), Integer.parseInt(pyeardets[0]));
//        String pyerenddates[] = pyearenddate.split("-");

        ////////////////////////// ////////////////////////.println("Start day of the month in the year " + cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR));
//       //////////////////////// ////////////////////////.println("month " + cal1.get(Calendar.MONTH));
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
            pymonthstrDate = prirYearcal.get(Calendar.DATE) + "-" + (prirYearcal.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + prirYearcal.get(Calendar.YEAR);
            pymonthyear = String.valueOf(prirYearcal.get(Calendar.YEAR));
            pymonthnum = String.valueOf(prirYearcal.get(Calendar.MONTH) + 1);
            cal1.add(Calendar.MONTH, 1);
            cal1.add(Calendar.DATE, - 1);
            prirYearcal.add(Calendar.MONTH, 1);
            prirYearcal.add(Calendar.DATE, - 1);
            endDate = cal1.get(Calendar.DATE) + "-" + (cal1.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + cal1.get(Calendar.YEAR);
            pymonthenddate = prirYearcal.get(Calendar.DATE) + "-" + (prirYearcal.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + prirYearcal.get(Calendar.YEAR);
            cal1.add(Calendar.DATE, 1);
            prirYearcal.add(Calendar.DATE, 1);
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

    public int monthNumber(String monthName) {
        int monthNumber = 0;

        for (int i = 0; i < monthNames.length; i++) {
            if (monthNames[i].equalsIgnoreCase(monthName)) {
                monthNumber = i;
            }
        }
        return monthNumber;
    }

    public int daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        int daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        daysBetween = daysBetween + 1;
//        ////////////////////////.println("daysbetween" + daysBetween);
        return daysBetween;
    }

    @Override
    public String[] findNext(Calendar startCalendar, Calendar endCalendar) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private int gettotalnumofDays(String startdate, String enddate) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        String yrstrtarr[];
        yrstrtarr = startdate.split("-");

        int strtday = Integer.parseInt(yrstrtarr[0].toString());
        String strtmonth = yrstrtarr[1];
        int i = 0;
        for (i = 0; i < monthNames.length; i++) {
            if (strtmonth.equalsIgnoreCase(monthNames[i])) {

                break;
            }
        }
        int strtyear = Integer.parseInt(yrstrtarr[2].toString());

        String yrendarr[];
        yrendarr = enddate.split("-");
        int endday = Integer.parseInt(yrendarr[0].toString());
        String endmnth = yrendarr[1];
        int j = 0;
        for (j = 0; j < monthNames.length; j++) {
            if (endmnth.equalsIgnoreCase(monthNames[j])) {

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
}
