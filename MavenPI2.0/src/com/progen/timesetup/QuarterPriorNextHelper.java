package com.progen.timesetup;

import java.util.Calendar;
import java.util.Locale;

public class QuarterPriorNextHelper extends PriorNextHelper {

    String[] monthNames = {"JAN", "FEB",
        "MAR", "APR", "MAY", "JUN", "JUL",
        "AUG", "SEP", "OCT", "NOV",
        "DEC"};

    @Override
    public String[] findPrior(Calendar startCalendar, Calendar endCalendar) {
        // 
        String[] priorQtrDetails = new String[4];

//        String tempstr = "";
        StringBuilder tempstr=new StringBuilder();
        int qtrCount = 4;
        for (int i = 1; i <= 4; i++) {

            Calendar calendar = (Calendar) startCalendar.clone();
            calendar.add(Calendar.MONTH, -3);
            long numberOfDays = getTotalNumberOfDays(calendar, startCalendar);
            tempstr.append(calendar.get(Calendar.YEAR)).append(",").append(calendar.get(Calendar.DATE)).append("-").append(calendar.getDisplayName(2, Calendar.SHORT, Locale.getDefault())).append("-").append(calendar.get(Calendar.YEAR));
            startCalendar.add(Calendar.DATE, -1);
            tempstr.append(tempstr).append(",").append(startCalendar.get(Calendar.DATE)).append("-").append(startCalendar.getDisplayName(2, Calendar.SHORT, Locale.getDefault())).append("-").append(startCalendar.get(Calendar.YEAR)).append(",").append(numberOfDays).append(",").append(qtrCount);
            startCalendar.add(Calendar.DATE, 1);
            startCalendar.add(Calendar.MONTH, 3);

            //  
            priorQtrDetails[i - 1] = tempstr.toString();
            tempstr.append("");
            if (i == 1) {
                qtrCount = 0;
            }
            qtrCount++;
        }

        return priorQtrDetails;
    }

    @Override
    public String[] findNext(Calendar startCalendar, Calendar endCalendar) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int gettotalnumofDays(String startdate, String enddate) {



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

    public long getTotalNumberOfDays(Calendar calendar1, Calendar calendar2) {

        long milis1 = calendar1.getTimeInMillis();
        long milis2 = calendar2.getTimeInMillis();
        long diff = milis2 - milis1;
//        long diffSeconds = diff / 1000;
//
//        long diffMinutes = diff / (60 * 1000);
//         long diffHours = diff / (60 * 60 * 1000);

        return (diff / (24 * 60 * 60 * 1000));
    }
}
