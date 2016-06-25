package com.progen.timesetup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class DaypriorNexthelper extends PriorNextHelper {

    @Override
    public String[] findPrior(Calendar startCalendar, Calendar endCalendar) {
        int numberOfDays = (int) getTotalNumberOfDays(startCalendar, endCalendar);
        String[] resultArray = new String[numberOfDays];

        for (int i = numberOfDays, k = 1; i > 0; i--, k++) {
            endCalendar.add(Calendar.DATE, -1);
            String tempStr = endCalendar.get(Calendar.DATE) + "-" + endCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + endCalendar.get(Calendar.YEAR);

            if (i == numberOfDays) {
                tempStr = tempStr + "," + numberOfDays;
            } else {
                tempStr = tempStr + "," + k;
            }
            resultArray[i - 1] = tempStr;
        }
        return resultArray;
    }

    @Override
    public String[] findNext(Calendar startCalendar, Calendar endCalendar) {
        int numberOfDays = (int) getTotalNumberOfDays(startCalendar, endCalendar);
        String[] resultArray = new String[numberOfDays];
        for (int i = 1; i <= numberOfDays; i++) {
            startCalendar.add(Calendar.DATE, 1);
            String tempStr = startCalendar.get(Calendar.DATE) + "-" + startCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + startCalendar.get(Calendar.YEAR);
            if (i == numberOfDays) {
                tempStr = tempStr + "," + 1;
            } else {
                int tempI = i + 1;
                tempStr = tempStr + "," + tempI;
            }
            resultArray[i - 1] = tempStr;
        }


        return resultArray;
    }

    public ArrayList currentMonth(Calendar startCalendar, Calendar endCalendar) {//cWeek
        ArrayList monthDetails = new ArrayList();
//        String tempStr = "";
        StringBuilder tempStr=new StringBuilder();
        int totalNumberOfDaysinYear = (int) getTotalNumberOfDays(startCalendar, endCalendar);
        int monthcount = 1;
        int monthhelperInt = 0;
        int monthMax = 0;
        Calendar endmonthCalendar = (Calendar) startCalendar.clone();
        endmonthCalendar.add(Calendar.MONTH, 1);
        endmonthCalendar.set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        for (int i = 1; i <= totalNumberOfDaysinYear; i++) {
            monthMax = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            tempStr.append(Integer.toString(monthcount));
            tempStr.append(",").append(Integer.toString(startCalendar.get(Calendar.YEAR)));
            tempStr.append(",").append(startCalendar.get(Calendar.DATE)).append("-").append(startCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())).append("-").append(startCalendar.get(Calendar.YEAR));
            tempStr.append(",").append(endmonthCalendar.get(Calendar.DATE)).append("-").append(endmonthCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())).append("-").append(endmonthCalendar.get(Calendar.YEAR));
            tempStr.append(",").append(startCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())).append("-").append(startCalendar.get(Calendar.YEAR));
            monthhelperInt++;
            if (monthhelperInt == monthMax) {
                startCalendar.add(Calendar.MONTH, 1);
                endmonthCalendar.set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                monthcount++;
                monthhelperInt = 0;
            }
            monthDetails.add(tempStr);
            tempStr.append("");

        }
        return monthDetails;
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

    public long getTotalNumberOfDays(Calendar calendar1, Calendar calendar2) {

        long milis1 = calendar1.getTimeInMillis();
        long milis2 = calendar2.getTimeInMillis();
        long diff = milis2 - milis1;


        return (diff / (24 * 60 * 60 * 1000)) + 1;
    }

    public ArrayList priorDayWeek(Calendar startCalendar, Calendar endCalendar) {//prDayWeek_new

        ArrayList prDayweeklist = new ArrayList();
        String ddate = null, custname = null, cwstrtdate = null, cwstrtmntdate = null, cwenddate = null, cwendmntdate = null, prDayWeek = null;
        Calendar strtcal = Calendar.getInstance();
        Calendar dayByDayCal = Calendar.getInstance();
        Calendar endcal = Calendar.getInstance();

        strtcal = (Calendar) startCalendar.clone();
        dayByDayCal = (Calendar) startCalendar.clone();
        endcal = (Calendar) endCalendar.clone();

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
            prDayweeklist.add(prDayWeek);
            dayByDayCal.add(Calendar.DATE, 1);
            strtcal.add(Calendar.DATE, 1);
        }

        return prDayweeklist;
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

    public ArrayList lastYearWeekDay(Calendar startCalendar, Calendar endCalendar) {
        ArrayList lstyerweeklist = new ArrayList();
        Calendar cal = Calendar.getInstance();
        String lyrwekdate, lstyrstrtdate, lstyrenddate, lstyrcustname, lstyrstrtmntdate, lstyrendmntdate, lstyrweekday;
        int daysOfYear, daysofyear, cweek, cwyear, i = 0, days = 0, count = 0;
        int day = startCalendar.get(Calendar.DATE);
        int yer = startCalendar.get(Calendar.YEAR);
        int month = startCalendar.get(Calendar.MONTH);
        int endday = endCalendar.get(Calendar.DATE);
        int endyer = endCalendar.get(Calendar.YEAR);
        int endmonth = endCalendar.get(Calendar.MONTH);

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

        daysOfYear = (int) getTotalNumberOfDays(startCalendar, endCalendar);

        for (i = day; i <= days; i++) {
            count++;
            lyrwekdate = i + "-" + monthName(month) + "-" + yer;

            cal.set(yer, month, i);
            daysofyear = cal.get(Calendar.DAY_OF_YEAR);
            cweek = cal.get(Calendar.WEEK_OF_YEAR);
            cwyear = cal.get(Calendar.YEAR);
            lstyrcustname = "W-" + cweek + "-" + yer;

            cal.setFirstDayOfWeek(Calendar.SUNDAY);  // Make sure that the calendar starts its week at Sunday

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

    public ArrayList curentQtrWeekDay(Calendar startCalendar, Calendar endCalendar) {//cqerweDay
        //int yer, int month, int day, int endday, int endmonth, int endyer
        int yer = startCalendar.get(Calendar.YEAR);
        int month = startCalendar.get(Calendar.MONTH);
        int day = startCalendar.get(Calendar.DATE);
        int endyer = endCalendar.get(Calendar.YEAR);
        int endmonth = endCalendar.get(Calendar.MONTH);
        int endday = endCalendar.get(Calendar.DATE);
        ArrayList cqerwdaylist = new ArrayList();
        String cqerwedayconcat, splitqerwday[];

        String[] cqerwekday = quartersOfGivendate(yer, month, day, endday, endmonth, endyer);//addpriorQrter(yer, month, day, calenderId, endyer, endmonth, endday, monthName(month), "NO");
        int limit = 0, i = 0;
        for (i = 0; i < cqerwekday.length; i++) {
            splitqerwday = cqerwekday[i].split(",");

            limit = getTotalNumofDays(splitqerwday[2].toString(), splitqerwday[3].toString());

            for (int j = 0; j < limit; j++) {

                cqerwedayconcat = splitqerwday[0] + "," + splitqerwday[1] + "," + splitqerwday[2] + "," + splitqerwday[3] + "," + splitqerwday[4];

                cqerwdaylist.add(cqerwedayconcat);

            }
        }
        return cqerwdaylist;
    }

    public String[] quartersOfGivendate(int yer, int month, int day, int endday, int endmonth, int endyer) {

        String quarterarr[] = new String[4];

//        String concat = "";
        StringBuilder concat=new StringBuilder();
        int strtyer = 0;
        Calendar strtcal = Calendar.getInstance();
        Calendar endcal = Calendar.getInstance();
        strtcal.set(yer, month, day);
        endcal.set(yer, month, day);
        endcal.add(Calendar.MONTH, 3);

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

    public int getTotalNumofDays(String startdate, String enddate) {


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

    public ArrayList clastyerqerDay(Calendar startCalendar, Calendar endCalendar) {

        int yer = startCalendar.get(Calendar.YEAR);
        int month = startCalendar.get(Calendar.MONTH);
        int day = startCalendar.get(Calendar.DATE);
        int endday = endCalendar.get(Calendar.DATE);
        int endmonth = endCalendar.get(Calendar.MONTH);
        int endyer = endCalendar.get(Calendar.YEAR);
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

        String[] lstyerqterday = quartersOfGivendate(yer, month, day, endday, endmonth, endyer);
        int limit = 0, i = 0;

        Calendar calender = Calendar.getInstance();
        calender.set(yer, month + 1, day);

        for (i = 0; i < lstyerqterday.length; i++) {

            splitlastqerday = lstyerqterday[i].split(",");

            limit = getTotalNumofDays(splitlastqerday[2].toString(), splitlastqerday[3].toString());

            for (int j = 0; j < limit; j++) {
                String lqtrDate = calender.get(Calendar.DATE) + "-" + (calender.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + calender.get(Calendar.YEAR);
                lstyerconcat = lqtrDate + "," + splitlastqerday[2] + "," + splitlastqerday[3] + "," + splitlastqerday[4];
                calender.add(Calendar.DATE, 1);
                clastyerqerdaylist.add(lstyerconcat);
            }
        }

        return clastyerqerdaylist;
    }

    public ArrayList currentYearDay(Calendar startCalendar, Calendar endCalendar) {//cyearDay
        int yer = startCalendar.get(Calendar.YEAR);
        int month = startCalendar.get(Calendar.MONTH);
        int day = startCalendar.get(Calendar.DATE);
        int endday = endCalendar.get(Calendar.DATE);
        int endmonth = endCalendar.get(Calendar.MONTH);
        int endyer = endCalendar.get(Calendar.YEAR);
        ArrayList cyeardaylist = new ArrayList();
        int loopedns = getTotalNumofDays(day + "-" + monthName(month) + "-" + yer, endday + "-" + monthName(endmonth) + "-" + endyer);
        for (int i = 0; i < loopedns; i++) {
            cyeardaylist.add(yer + "," + day + "-" + monthName(month) + "-" + yer + "," + endday + "-" + monthName(endmonth) + "-" + endyer + "," + yer);
        }
        return cyeardaylist;
    }

    public ArrayList nextYearQtr(Calendar startCalendar, Calendar endCalendar) {

        int yer = startCalendar.get(Calendar.YEAR);
        int month = startCalendar.get(Calendar.MONTH);
        int day = startCalendar.get(Calendar.DATE);
        int endday = endCalendar.get(Calendar.DATE);
        int endmonth = endCalendar.get(Calendar.MONTH);
        int endyer = endCalendar.get(Calendar.YEAR);
        ArrayList clastyerqerdaylist = new ArrayList();
        Calendar nextyerqerdaystrt = Calendar.getInstance();
        nextyerqerdaystrt.set(yer, month, day);
        nextyerqerdaystrt.add(Calendar.YEAR, 1);

        day = nextyerqerdaystrt.get(Calendar.DATE);
        month = nextyerqerdaystrt.get(Calendar.MONTH);
        yer = nextyerqerdaystrt.get(Calendar.YEAR);

        Calendar nextyerqerdayend = Calendar.getInstance();
        nextyerqerdayend.set(endyer, endmonth, endday);
        nextyerqerdayend.add(Calendar.YEAR, 1);
        endday = nextyerqerdayend.get(Calendar.DATE);
        endmonth = nextyerqerdayend.get(Calendar.MONTH);
        endyer = nextyerqerdayend.get(Calendar.YEAR);

        String nextyerconcat, splitnextqerday[];

        String[] nextyerqterday = quartersOfGivendate(yer, month, day, endday, endmonth, endyer);
        int limit = 0, i = 0;

        Calendar calender = Calendar.getInstance();
        calender.set(yer, month + 1, day);

        for (i = 0; i < nextyerqterday.length; i++) {

            splitnextqerday = nextyerqterday[i].split(",");

            limit = getTotalNumofDays(splitnextqerday[2].toString(), splitnextqerday[3].toString());

            for (int j = 0; j < limit; j++) {
                String nextQtrDate = calender.get(Calendar.DATE) + "-" + (calender.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + calender.get(Calendar.YEAR);
                nextyerconcat = nextQtrDate + "," + splitnextqerday[2] + "," + splitnextqerday[3] + "," + splitnextqerday[4];
                calender.add(Calendar.DATE, 1);
                clastyerqerdaylist.add(nextyerconcat);
            }
        }

        return clastyerqerdaylist;

    }

    public ArrayList nextYearWeekDay(Calendar startCalendar, Calendar endCalendar) {
        ArrayList nextyerweeklist = new ArrayList();
        Calendar cal = Calendar.getInstance();
        String lyrwekdate, nextyrstrtdate, nextyrenddate, nextyrcustname, nextyrstrtmntdate, nextyrendmntdate, nextyrweekday;
        int daysOfYear, daysofyear, cweek, cwyear, i = 0, days = 0, count = 0;
        int day = startCalendar.get(Calendar.DATE);
        int yer = startCalendar.get(Calendar.YEAR);
        int month = startCalendar.get(Calendar.MONTH);
        int endday = endCalendar.get(Calendar.DATE);
        int endyer = endCalendar.get(Calendar.YEAR);
        int endmonth = endCalendar.get(Calendar.MONTH);

        day = day + 1;
        yer = yer + 1;
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

        daysOfYear = (int) getTotalNumberOfDays(startCalendar, endCalendar);

        for (i = day; i <= days; i++) {
            count++;
            lyrwekdate = i + "-" + monthName(month) + "-" + yer;

            cal.set(yer, month, i);
            daysofyear = cal.get(Calendar.DAY_OF_YEAR);
            cweek = cal.get(Calendar.WEEK_OF_YEAR);
            cwyear = cal.get(Calendar.YEAR);
            nextyrcustname = "W-" + cweek + "-" + yer;

            cal.setFirstDayOfWeek(Calendar.SUNDAY);  // Make sure that the calendar starts its week at Sunday

            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

            nextyrstrtdate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            nextyrstrtmntdate = nextyrstrtdate;
            cal.add(Calendar.DAY_OF_YEAR, 6);

            nextyrenddate = cal.get(Calendar.DATE) + "-" + monthName(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR);
            nextyrendmntdate = nextyrenddate;

            nextyrweekday = lyrwekdate + "," + nextyrstrtdate + "," + nextyrenddate + "," + nextyrstrtmntdate + "," + nextyrendmntdate + "," + nextyrcustname;

            nextyerweeklist.add(nextyrweekday);
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
        return nextyerweeklist;
    }

    public ArrayList nextDayWeek(Calendar startCalendar, Calendar endCalendar) {//prDayWeek_new

        ArrayList nextDayweeklist = new ArrayList();
        String ddate = null, custname = null, cwstrtdate = null, cwstrtmntdate = null, cwenddate = null, cwendmntdate = null, nextDayWeek = null;
//        Calendar strtcal = Calendar.getInstance();
//        Calendar nextByDayCal = Calendar.getInstance();
//        Calendar endcal = Calendar.getInstance();

        Calendar strtcal = (Calendar) startCalendar.clone();
        Calendar nextByDayCal = (Calendar) startCalendar.clone();
        Calendar endcal = (Calendar) endCalendar.clone();

        int totalDays = daysBetween(strtcal, endcal), i = 0;

        strtcal.add(Calendar.DATE, 7);
        nextByDayCal.add(Calendar.DATE, 7);
        endcal.add(Calendar.DATE, 6);

        for (i = 0; i < totalDays; i++) {
            ddate = nextByDayCal.get(Calendar.DATE) + "-" + (nextByDayCal.getDisplayName((Calendar.MONTH), Calendar.SHORT, Locale.getDefault())) + "-" + nextByDayCal.get(Calendar.YEAR);
            custname = "W-" + strtcal.get(Calendar.WEEK_OF_YEAR) + "-" + strtcal.get(Calendar.YEAR);
            strtcal.setFirstDayOfWeek(Calendar.SUNDAY);
            strtcal.set(Calendar.DAY_OF_WEEK, strtcal.getFirstDayOfWeek());
            cwstrtdate = strtcal.get(Calendar.DATE) + "-" + monthName(strtcal.get(Calendar.MONTH)) + "-" + strtcal.get(Calendar.YEAR);
            cwstrtmntdate = cwstrtdate;
            strtcal.add(Calendar.DATE, 6);
            cwenddate = strtcal.get(Calendar.DATE) + "-" + monthName(strtcal.get(Calendar.MONTH)) + "-" + strtcal.get(Calendar.YEAR);
            cwendmntdate = cwenddate;
            nextDayWeek = ddate + "," + cwstrtdate + "," + cwenddate + "," + cwstrtmntdate + "," + cwendmntdate + "," + custname;
            nextDayweeklist.add(nextDayWeek);
            nextByDayCal.add(Calendar.DATE, 1);
            strtcal.add(Calendar.DATE, 1);
        }

        return nextDayweeklist;
    }

    ArrayList currentWeekdays(Calendar startCalendar, Calendar endCalendar) {
        ArrayList weeklist = new ArrayList();
//        String tempWeekstr = "";
        StringBuilder tempWeekstr=new StringBuilder();
        int totalNumberOfDaysinYear = (int) getTotalNumberOfDays(startCalendar, endCalendar);
        int numberOfDaysinmonth = 0;
        Calendar dayCalendar = (Calendar) startCalendar.clone();
        Calendar weekCalendar = (Calendar) startCalendar.clone();
        Calendar monthCalendar = (Calendar) startCalendar.clone();
        int dayofweek = weekCalendar.get(Calendar.DAY_OF_WEEK);
        weekCalendar.set(Calendar.DAY_OF_WEEK, dayCalendar.getFirstDayOfWeek());
        int weekCount = 1;
        for (int i = 1; i <= totalNumberOfDaysinYear; i++) {

            numberOfDaysinmonth = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            tempWeekstr.append(dayCalendar.get(Calendar.DATE)).append("-").append(dayCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())).append("-").append(dayCalendar.get(Calendar.YEAR));
            tempWeekstr.append(",").append(dayCalendar.get(Calendar.DATE)).append("-").append(dayCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())).append("-").append(dayCalendar.get(Calendar.YEAR));
            tempWeekstr.append(",").append(dayCalendar.get(Calendar.DATE)).append("-").append(dayCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())).append("-").append(dayCalendar.get(Calendar.YEAR));
            tempWeekstr.append(",").append(i);

            tempWeekstr.append(",").append(weekCount);
            tempWeekstr.append(",").append(weekCalendar.get(Calendar.YEAR));
            tempWeekstr.append(",").append(weekCalendar.get(Calendar.DATE)).append("-").append(weekCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())).append("-").append(weekCalendar.get(Calendar.YEAR));
            if (weekCalendar.get(Calendar.MONTH) == dayCalendar.get(Calendar.MONTH)) {
                tempWeekstr.append(",").append(weekCalendar.get(Calendar.DATE)).append("-").append(weekCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())).append("-").append(weekCalendar.get(Calendar.YEAR));
            } else {
                tempWeekstr.append(",").append(1).append("-").append(dayCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())).append("-").append(weekCalendar.get(Calendar.YEAR));

            }

            weekCalendar.add(Calendar.DATE, 6);
            tempWeekstr.append(",").append(weekCalendar.get(Calendar.DATE)).append("-").append(weekCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())).append("-").append(weekCalendar.get(Calendar.YEAR));
            tempWeekstr.append(",").append(weekCalendar.get(Calendar.DATE)).append("-").append(weekCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())).append("-").append(weekCalendar.get(Calendar.YEAR));

            tempWeekstr.append("," + "W_").append(weekCount).append("_").append(dayCalendar.get(Calendar.YEAR));
            weekCalendar.add(Calendar.DATE, -6);

            dayCalendar.add(Calendar.DATE, 1);
            if (i % 7 == 0) {
                weekCount++;

            }

            if (dayofweek % 7 == 0) {
                weekCalendar.setTime(dayCalendar.getTime());
                //  weekCalendar.add(Calendar.DATE,1);
            }

            dayofweek++;
            weeklist.add(tempWeekstr);
        }

        return weeklist;
    }

    public String priorMonthDay(Calendar startCalendar, Calendar endCalendar) {
        String prmontStr = "";
        Calendar tempCalendar = (Calendar) startCalendar.clone();
        tempCalendar.add(Calendar.MONTH, -1);
        int monthMax = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (startCalendar.get(Calendar.DATE) == monthMax) {

            tempCalendar.set(tempCalendar.get(Calendar.YEAR), tempCalendar.get(Calendar.MONTH), tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            prmontStr = tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            tempCalendar.add(Calendar.DATE, -(tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1));
            prmontStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            tempCalendar.add(Calendar.DATE, (tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1));
            prmontStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            prmontStr += "," + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
        } else {
            prmontStr = tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            tempCalendar.add(Calendar.DATE, -(tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1));
            prmontStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            tempCalendar.add(Calendar.DATE, (tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1));
            prmontStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            prmontStr += "," + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);


        }

        return prmontStr;
    }

    public String nextMonthDay(Calendar startCalendar, Calendar endCalendar) {
        String nMmontStr = "";
        Calendar tempCalendar = (Calendar) startCalendar.clone();
        tempCalendar.add(Calendar.MONTH, 1);
        int monthMax = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (startCalendar.get(Calendar.DATE) == monthMax) {

            tempCalendar.set(tempCalendar.get(Calendar.YEAR), tempCalendar.get(Calendar.MONTH), tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            nMmontStr = tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            tempCalendar.add(Calendar.DATE, -(tempCalendar.get(Calendar.DATE)) + 1);
            nMmontStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            tempCalendar.add(Calendar.DATE, (tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1));
            nMmontStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            nMmontStr += "," + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
        } else {
            nMmontStr = tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            tempCalendar.add(Calendar.DATE, -(tempCalendar.get(Calendar.DATE)) + 1);
            nMmontStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            tempCalendar.add(Calendar.DATE, (tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1));
            nMmontStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            nMmontStr += "," + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);


        }

        return nMmontStr;
    }

    public String lastQtrDays(Calendar startCalendar, Calendar qtrStartCalendar, int count) {
        HashMap qtrHashMap = new HashMap();
        qtrHashMap.put(1, 4);
        qtrHashMap.put(2, 1);
        qtrHashMap.put(3, 2);
        qtrHashMap.put(4, 3);
        String qtrStr = "";
        Calendar tempCalendar = (Calendar) startCalendar.clone();
        tempCalendar.add(Calendar.MONTH, -3);
        qtrStartCalendar.add(Calendar.MONTH, -3);
        int monthMax = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (startCalendar.get(Calendar.DATE) == monthMax) {

            tempCalendar.set(tempCalendar.get(Calendar.YEAR), tempCalendar.get(Calendar.MONTH), tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            qtrStr = tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            qtrStartCalendar.add(Calendar.DATE, -(qtrStartCalendar.get(Calendar.DATE) - 1));
            qtrStr += "," + qtrStartCalendar.get(Calendar.DATE) + "-" + qtrStartCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + qtrStartCalendar.get(Calendar.YEAR);
            qtrStartCalendar.add(Calendar.MONTH, 2);
            qtrStr += "," + qtrStartCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) + "-" + qtrStartCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + qtrStartCalendar.get(Calendar.YEAR);
            qtrStr += ",Q" + qtrHashMap.get(count) + "-" + tempCalendar.get(Calendar.YEAR);
        } else {
            qtrStr = tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            qtrStartCalendar.add(Calendar.DATE, -(qtrStartCalendar.get(Calendar.DATE) - 1));
            qtrStr += "," + qtrStartCalendar.get(Calendar.DATE) + "-" + qtrStartCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + qtrStartCalendar.get(Calendar.YEAR);
            qtrStartCalendar.add(Calendar.MONTH, 2);
            qtrStr += "," + qtrStartCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) + "-" + qtrStartCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + qtrStartCalendar.get(Calendar.YEAR);
            qtrStr += ",Q" + qtrHashMap.get(count) + "-" + tempCalendar.get(Calendar.YEAR);

        }

        return qtrStr;
    }

    String nextQtrDays(Calendar startCalendar, Calendar qtrStartCalendar, int qtrCheckCount) {
        HashMap qtrHashMap = new HashMap();
        qtrHashMap.put(1, 2);
        qtrHashMap.put(2, 3);
        qtrHashMap.put(3, 4);
        qtrHashMap.put(4, 1);
        String qtrStr = "";
        Calendar tempCalendar = (Calendar) startCalendar.clone();
        tempCalendar.add(Calendar.MONTH, 3);
        qtrStartCalendar.add(Calendar.MONTH, 3);
        int monthMax = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (startCalendar.get(Calendar.DATE) == monthMax) {

            tempCalendar.set(tempCalendar.get(Calendar.YEAR), tempCalendar.get(Calendar.MONTH), tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            qtrStr = tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            qtrStartCalendar.add(Calendar.DATE, -(qtrStartCalendar.get(Calendar.DATE) - 1));
            qtrStr += "," + qtrStartCalendar.get(Calendar.DATE) + "-" + qtrStartCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + qtrStartCalendar.get(Calendar.YEAR);
            qtrStartCalendar.add(Calendar.MONTH, 2);
            qtrStr += "," + qtrStartCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) + "-" + qtrStartCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + qtrStartCalendar.get(Calendar.YEAR);
            qtrStr += ",Q" + qtrHashMap.get(qtrCheckCount) + "-" + tempCalendar.get(Calendar.YEAR);
        } else {
            qtrStr = tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            qtrStartCalendar.add(Calendar.DATE, -(qtrStartCalendar.get(Calendar.DATE) - 1));
            qtrStr += "," + qtrStartCalendar.get(Calendar.DATE) + "-" + qtrStartCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + qtrStartCalendar.get(Calendar.YEAR);
            qtrStartCalendar.add(Calendar.MONTH, 2);
            qtrStr += "," + qtrStartCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) + "-" + qtrStartCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + qtrStartCalendar.get(Calendar.YEAR);
            qtrStr += ",Q" + qtrHashMap.get(qtrCheckCount) + "-" + tempCalendar.get(Calendar.YEAR);

        }

        return qtrStr;
    }

    String nextYearMonthDay(Calendar startCalendar, Calendar calendar0, String yearMonthCeecker) {
        String nYeartStr = "";
        Calendar tempCalendar = (Calendar) startCalendar.clone();
        tempCalendar.add(Calendar.YEAR, 1);
        int monthMax = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (startCalendar.get(Calendar.DATE) == monthMax) {
            tempCalendar.set(tempCalendar.get(Calendar.YEAR), tempCalendar.get(Calendar.MONTH), tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            if (yearMonthCeecker.equalsIgnoreCase("month")) {
                nYeartStr = tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            } else {
                nYeartStr = Integer.toString(tempCalendar.get(Calendar.YEAR));
                nYeartStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            }

            tempCalendar.add(Calendar.DATE, -(tempCalendar.get(Calendar.DATE)) + 1);
            nYeartStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            tempCalendar.add(Calendar.DATE, (tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1));
            nYeartStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);

            if (yearMonthCeecker.equalsIgnoreCase("month")) {

                nYeartStr += "," + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            } else {
                nYeartStr += ",N_" + tempCalendar.get(Calendar.YEAR);
            }

        } else {

            if (yearMonthCeecker.equalsIgnoreCase("month")) {
                nYeartStr = tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            } else {
                nYeartStr = Integer.toString(tempCalendar.get(Calendar.YEAR));
                nYeartStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            }

            tempCalendar.add(Calendar.DATE, -(tempCalendar.get(Calendar.DATE)) + 1);
            nYeartStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            tempCalendar.add(Calendar.DATE, (tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1));
            nYeartStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            if (yearMonthCeecker.equalsIgnoreCase("month")) {

                nYeartStr += "," + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            } else {
                nYeartStr += ",L" + tempCalendar.get(Calendar.YEAR);
            }




        }

        return nYeartStr;
    }

    String priorYearMonthDay(Calendar startCalendar, Calendar endCalendar, String yearMonthCeecker) {
        String pYeartStr = "";
        Calendar tempCalendar = (Calendar) startCalendar.clone();
        tempCalendar.add(Calendar.YEAR, -1);
        int monthMax = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (startCalendar.get(Calendar.DATE) == monthMax) {

            tempCalendar.set(tempCalendar.get(Calendar.YEAR), tempCalendar.get(Calendar.MONTH), tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            if (yearMonthCeecker.equalsIgnoreCase("month")) {
                pYeartStr = tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            } else {
                pYeartStr = Integer.toString(tempCalendar.get(Calendar.YEAR));
                pYeartStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            }

            tempCalendar.add(Calendar.DATE, -(tempCalendar.get(Calendar.DATE)) + 1);
            pYeartStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            tempCalendar.add(Calendar.DATE, (tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1));
            pYeartStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            if (yearMonthCeecker.equalsIgnoreCase("month")) {
                pYeartStr += "," + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            } else {
                pYeartStr += "," + tempCalendar.get(Calendar.YEAR);
            }

        } else {
            if (yearMonthCeecker.equalsIgnoreCase("month")) {
                pYeartStr = tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            } else {
                pYeartStr = Integer.toString(tempCalendar.get(Calendar.YEAR));
                pYeartStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            }

            tempCalendar.add(Calendar.DATE, -(tempCalendar.get(Calendar.DATE)) + 1);
            pYeartStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            tempCalendar.add(Calendar.DATE, (tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1));
            pYeartStr += "," + tempCalendar.get(Calendar.DATE) + "-" + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);

            if (yearMonthCeecker.equalsIgnoreCase("month")) {
                pYeartStr += "," + tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + tempCalendar.get(Calendar.YEAR);
            } else {
                pYeartStr += "," + tempCalendar.get(Calendar.YEAR);
            }




        }

        return pYeartStr;
    }
}
