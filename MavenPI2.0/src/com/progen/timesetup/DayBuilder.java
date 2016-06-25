/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.timesetup;

import java.util.*;
import prg.db.PbDb;

class DayBuilder extends CalendarBuilder {

    TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
    WeekPriorNextHelper weekPriorNextHelper = new WeekPriorNextHelper();
    PbDb PbDb = new PbDb();

    @Override
    protected ArrayList buildCalendarData(Date startDate, Date endDate, String calendarId) {
        ArrayList queryList = new ArrayList();
        String addPrDay = null;
        if (calendarId == null) {
            addPrDay = resBundle.getString("addPrDay1");
        } else {
            addPrDay = resBundle.getString("addPrDay");
        }

        Calendar startCalnder = Calendar.getInstance();
        startCalnder.setTime(startDate);
        String[] monthName = {"JAN", "FEB",
            "MAR", "APR", "MAY", "JUN", "JUL",
            "AUG", "SEP", "OCT", "NOV",
            "DEC"};
        String finalQuery = "";
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        String dayname = null;
        int j = 0;
        int check = 0;
        int count = 0;
        int forcount = 11;
        int mnth = startCalnder.get(Calendar.MONTH);
        Object[] obj1 = new Object[8];
        for (j = startCalnder.get(Calendar.MONTH); j <= forcount; j++) {
            startCalnder.set(startCalnder.get(Calendar.YEAR), j, startCalnder.get(Calendar.DATE));
            int days = startCalnder.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int i = startCalnder.get(Calendar.DATE); i <= days; i++) {
                count++;
                dayname = startCalnder.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                //code to insert date prdays


                obj1[0] = calendarId;
                obj1[1] = i + "-" + monthName[j] + "-" + startCalnder.get(Calendar.YEAR);
                obj1[2] = i + "-" + monthName[j] + "-" + startCalnder.get(Calendar.YEAR);
                obj1[3] = i + "-" + monthName[j] + "-" + startCalnder.get(Calendar.YEAR);
                obj1[4] = count;
                obj1[5] = i;
                if (dayname.equalsIgnoreCase("Sunday")) {
                    obj1[6] = "L";
                } else {
                    obj1[6] = "W";
                }
                obj1[7] = dayname;
                finalQuery = PbDb.buildQuery(addPrDay, obj1);
                queryList.add(finalQuery);

                if (startCalnder.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) && j == endCalendar.get(Calendar.MONTH) && i == endCalendar.get(Calendar.DATE)) {
                    break;
                }
                if (i == days) {

                    days = totaldays(startCalnder.get(Calendar.YEAR), j + 1, 1);

                    if (j == monthName.length - 1) {
                        mnth = 0;

                        if (check == 0) {
                            j = -1;
                            check = 1;
                            forcount = endCalendar.get(Calendar.MONTH);

                            //startCalnder.add(Calendar.YEAR, 1);
                            days = totaldays(startCalnder.get(Calendar.YEAR), mnth + 1, startCalnder.get(Calendar.DATE));
                        }
                    }
                }
                startCalnder.add(Calendar.DATE, 1);
            }
        }
        // 
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(startDate);
        calendar2.setTime(endDate);
        ArrayList denomQueryList = dayDenomBuild(calendar1, calendar2, calendarId);
        queryList.addAll(denomQueryList);
        return queryList;

    }

    public int totaldays(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, date);
        int idays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return idays;
    }

    public ArrayList dayDenomBuild(Calendar startCalendar, Calendar endCalendar, String calendarId) {
        ArrayList queryList = new ArrayList();
        String addPrDayDenom = null;
        if (calendarId == null) {
            addPrDayDenom = resBundle.getString("addPrDayDenom1");
        } else {
            addPrDayDenom = resBundle.getString("addPrDayDenom");
        }
        String finalQuery = "";
        int loopsize = 0;
        DaypriorNexthelper daypriorNexthelper = new DaypriorNexthelper();

        long totalDayinyear = daypriorNexthelper.getTotalNumberOfDays((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        int totalYdays = (int) totalDayinyear;
        ArrayList cweklist = daypriorNexthelper.currentWeekdays((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        loopsize = cweklist.size();


        ArrayList cmonthlist = daypriorNexthelper.currentMonth((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        if (loopsize < cmonthlist.size()) {
            loopsize = cmonthlist.size();
        }

        //priorDays
        String[] priorDayArray = daypriorNexthelper.findPrior((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        ArrayList priorDayArrayList = new ArrayList();
        priorDayArrayList.addAll(Arrays.asList(priorDayArray));
        if (loopsize < priorDayArrayList.size()) {
            loopsize = priorDayArrayList.size();
        }
        //nextDays
        String[] nextDayArray = daypriorNexthelper.findNext((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        ArrayList nextDayArrayList = new ArrayList();
        nextDayArrayList.addAll(Arrays.asList(nextDayArray));
        if (loopsize < nextDayArrayList.size()) {
            loopsize = nextDayArrayList.size();
        }


        ArrayList prdayweklist = daypriorNexthelper.priorDayWeek((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        if (loopsize < prdayweklist.size()) {
            loopsize = prdayweklist.size();
        }


        ArrayList nextDayWeekList = daypriorNexthelper.nextDayWeek((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        if (loopsize < nextDayWeekList.size()) {
            loopsize = nextDayWeekList.size();
        }

        ArrayList lstyrwkdaylist = daypriorNexthelper.lastYearWeekDay((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        if (loopsize < lstyrwkdaylist.size()) {
            loopsize = lstyrwkdaylist.size();
        }

        ArrayList nextYerWdaysList = daypriorNexthelper.nextYearWeekDay((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        if (loopsize < nextYerWdaysList.size()) {
            loopsize = nextYerWdaysList.size();
        }

        ArrayList pyrdaylist = new ArrayList();
        //daypriorNexthelper.priorYearMonthDay((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        Calendar stCalForPYear = (Calendar) startCalendar.clone();
        Calendar endClaForPYear = (Calendar) endCalendar.clone();
        for (int py = 0; py < totalYdays; py++) {
            String tmpStr = daypriorNexthelper.priorYearMonthDay((Calendar) stCalForPYear.clone(), (Calendar) endClaForPYear.clone(), "month");
            pyrdaylist.add(tmpStr);
            stCalForPYear.add(Calendar.DATE, 1);
        }
        if (loopsize < pyrdaylist.size()) {
            loopsize = pyrdaylist.size();
        }

        ArrayList nextYearmonthDaysList = new ArrayList();
        //daypriorNexthelper.nextYearMonthDay((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());

        Calendar stCalForYear = (Calendar) startCalendar.clone();
        Calendar endClaForYear = (Calendar) endCalendar.clone();
        for (int ny = 0; ny < totalYdays; ny++) {
            String tempyear = daypriorNexthelper.nextYearMonthDay((Calendar) stCalForYear.clone(), (Calendar) endClaForYear.clone(), "month");
            nextYearmonthDaysList.add(tempyear);
            stCalForYear.add(Calendar.DATE, 1);
        }
        if (loopsize < nextYearmonthDaysList.size()) {
            loopsize = nextYearmonthDaysList.size();
        }

        ArrayList pmntdaylist = new ArrayList();
        //daypriorNexthelper.priorMonthDay((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());

        Calendar startCalendar1 = (Calendar) startCalendar.clone();
        Calendar endCalendar1 = (Calendar) endCalendar.clone();
        for (int ki = 0; ki < totalYdays; ki++) {
            String tempStr = daypriorNexthelper.priorMonthDay((Calendar) startCalendar1.clone(), (Calendar) endCalendar1.clone());
            pmntdaylist.add(tempStr);
            startCalendar1.add(Calendar.DATE, 1);
        }
        if (loopsize < pmntdaylist.size()) {
            loopsize = pmntdaylist.size();
        }

        ArrayList nextMonthDayList = new ArrayList();
        //daypriorNexthelper.nextMonthDay((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        Calendar startCalendarNM = (Calendar) startCalendar.clone();
        Calendar endCalendarNM = (Calendar) endCalendar.clone();
        for (int ni = 0; ni < totalYdays; ni++) {
            String tempNMStr = daypriorNexthelper.nextMonthDay((Calendar) startCalendarNM.clone(), (Calendar) endCalendarNM.clone());
            nextMonthDayList.add(tempNMStr);
            startCalendarNM.add(Calendar.DATE, 1);
        }

        if (loopsize < nextMonthDayList.size()) {
            loopsize = nextMonthDayList.size();
        }

        ArrayList cqerwdaylist = daypriorNexthelper.curentQtrWeekDay((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        if (loopsize < cqerwdaylist.size()) {
            loopsize = cqerwdaylist.size();
        }

        ArrayList nextQtrList = new ArrayList();
        //daypriorNexthelper.nextQtrWeekDay((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        Calendar sQtrCalendar = (Calendar) startCalendar.clone();
        Calendar eQtrCalendar = (Calendar) startCalendar.clone();
        Calendar monthCalendar = (Calendar) startCalendar.clone();
        int qtrCheckCount = 1;
        int monthCeckCount = 1;
        for (int ni = 0; ni < totalYdays; ni++) {
            String tempQtrStr = daypriorNexthelper.nextQtrDays((Calendar) sQtrCalendar.clone(), (Calendar) eQtrCalendar.clone(), qtrCheckCount);
            nextQtrList.add(tempQtrStr);
            sQtrCalendar.add(Calendar.DATE, 1);
            if (monthCalendar.get(Calendar.MONTH) != sQtrCalendar.get(Calendar.MONTH)) {
                monthCeckCount++;
            }
            if (monthCeckCount == 4) {
                qtrCheckCount++;
                eQtrCalendar.add(Calendar.MONTH, 3);
                monthCeckCount = 1;
            }
            monthCalendar.add(Calendar.DATE, 1);

        }
        if (loopsize < nextQtrList.size()) {
            loopsize = nextQtrList.size();
        }

        ArrayList clastqerweDaylist = new ArrayList();
        //daypriorNexthelper.clastqerweDay((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        Calendar startCforQtr = (Calendar) startCalendar.clone();
        Calendar endCforQtr = (Calendar) startCalendar.clone();
        Calendar monthCheckCalendar = (Calendar) startCalendar.clone();
        int qtrCount = 1;
        int monthCount = 1;
        for (int qi = 0; qi < totalYdays; qi++) {

            String tempQtrStr = daypriorNexthelper.lastQtrDays((Calendar) startCforQtr.clone(), (Calendar) endCforQtr.clone(), qtrCount);
            clastqerweDaylist.add(tempQtrStr);
            startCforQtr.add(Calendar.DATE, 1);
            if (monthCheckCalendar.get(Calendar.MONTH) != startCforQtr.get(Calendar.MONTH)) {
                monthCount++;
            }
            if (monthCount == 4) {
                qtrCount++;
                endCforQtr.add(Calendar.MONTH, 3);
                monthCount = 1;
            }
            monthCheckCalendar.add(Calendar.DATE, 1);
        }

        if (loopsize < clastqerweDaylist.size()) {
            loopsize = clastqerweDaylist.size();
        }

        //nextQtr
        ArrayList nextQtrDaysList = daypriorNexthelper.nextYearQtr((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        if (loopsize < nextQtrDaysList.size()) {
            loopsize = nextQtrDaysList.size();
        }



        ArrayList clastyerqerDaylist = daypriorNexthelper.clastyerqerDay((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        if (loopsize < clastyerqerDaylist.size()) {
            loopsize = clastyerqerDaylist.size();
        }
        ArrayList cyearDaylist = daypriorNexthelper.currentYearDay((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        if (loopsize < cyearDaylist.size()) {
            loopsize = cyearDaylist.size();
        }
        ArrayList lyearDaylist = new ArrayList();
        //daypriorNexthelper.lyearDay((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        Calendar stYearCalendar = (Calendar) startCalendar.clone();
        Calendar endYearCalendar = (Calendar) endCalendar.clone();
        for (int ly = 0; ly < totalYdays; ly++) {
            String tempYearStr = daypriorNexthelper.priorYearMonthDay(stYearCalendar, endYearCalendar, "year");
            lyearDaylist.add(tempYearStr);
            stYearCalendar.add(Calendar.DATE, 1);
        }
        if (loopsize < lyearDaylist.size()) {
            loopsize = lyearDaylist.size();
        }
        ArrayList nextYearDaylist = new ArrayList();
        //daypriorNexthelper.nextYearDay((Calendar) startCalendar.clone(), (Calendar) endCalendar.clone());
        Calendar stNextYearCalendar = (Calendar) startCalendar.clone();
        Calendar endNextYearCalendar = (Calendar) endCalendar.clone();
        for (int ny = 0; ny < totalYdays; ny++) {
            String tempYearStr = daypriorNexthelper.nextYearMonthDay(stNextYearCalendar, endNextYearCalendar, "year");
            nextYearDaylist.add(tempYearStr);
            stNextYearCalendar.add(Calendar.DATE, 1);
        }
        if (loopsize < nextYearDaylist.size()) {
            loopsize = nextYearDaylist.size();
        }

        int i = 0, stringlimit = 0, j = 0, count = -1, k = 0, totlength = 0, l = 1, iflarge;
//        String insertstrng = "";
        StringBuilder insertstrng =new StringBuilder();
        Object insrtobj[];

        for (i = 0; i < loopsize; i++) {
            iflarge = i;
            ////////////////////////.println("iflarge is i" + i);
            if (i == cweklist.size()) {
                iflarge = cweklist.size() - 1;
                ////////////////////////.println("iflarge" + iflarge);
            }
            String weklstrng[] = cweklist.get(iflarge).toString().split(",");

            if (i == priorDayArrayList.size()) {
                iflarge = priorDayArrayList.size() - 1;
            }
            String priDayArray[] = priorDayArrayList.get(iflarge).toString().split(",");//2 ele

            if (i == nextDayArrayList.size()) {
                iflarge = nextDayArrayList.size() - 1;
            }
            String[] neDayArray = nextDayArrayList.get(iflarge).toString().split(",");//2 ele


            if (i == prdayweklist.size()) {
                iflarge = prdayweklist.size() - 1;
            }
            String prdaystrng[] = prdayweklist.get(iflarge).toString().split(",");


            if (i == nextDayWeekList.size()) {
                iflarge = nextDayWeekList.size() - 1;
            }

            String nextDayWeekAString[] = nextDayWeekList.get(iflarge).toString().split(",");//6 ele


            if (i == lstyrwkdaylist.size()) {
                iflarge = lstyrwkdaylist.size() - 1;
            }
            String lstyrwkdaystr[] = lstyrwkdaylist.get(iflarge).toString().split(",");

            if (i == nextYerWdaysList.size()) {
                iflarge = nextYerWdaysList.size() - 1;
            }
            String nextYearWeekDaysArray[] = nextYerWdaysList.get(iflarge).toString().split(",");//6 ele

            if (i == cmonthlist.size()) {
                iflarge = cmonthlist.size() - 1;
            }
            String cmonthstrng[] = cmonthlist.get(iflarge).toString().split(",");
            if (i == pyrdaylist.size()) {
                iflarge = pyrdaylist.size() - 1;
            }
            String pyrdaystrng[] = pyrdaylist.get(iflarge).toString().split(",");


            if (i == nextYearmonthDaysList.size()) {
                iflarge = nextYearmonthDaysList.size() - 1;
            }
            String nYerMonthDayString[] = nextYearmonthDaysList.get(iflarge).toString().split(",");//4ele

            //    if (pmntdaylist.size() > cweklist.size()) {
            //        int min = i - 1;
            //       pyrdaystrng = pyrdaylist.get(min).toString().split(",");
            //    }
            if (i == pmntdaylist.size()) {
                iflarge = pmntdaylist.size() - 1;
            }
            String pmntdaystrng[] = pmntdaylist.get(iflarge).toString().split(",");

            if (i == nextMonthDayList.size()) {

                iflarge = nextMonthDayList.size() - 1;
            }
            String nextMonthString[] = nextMonthDayList.get(iflarge).toString().split(",");//4 ele
            if (i == cqerwdaylist.size()) {
                iflarge = cqerwdaylist.size() - 1;
            }
            String cqerwdaystrng[] = cqerwdaylist.get(iflarge).toString().split(",");

            if (i == nextQtrList.size()) {
                iflarge = nextQtrList.size() - 1;
            }
            String nextQTrString[] = nextQtrList.get(iflarge).toString().split(","); //4 ele

            if (i == clastqerweDaylist.size()) {
                iflarge = clastqerweDaylist.size() - 1;
            }

            String clastqerweDaystrng[] = clastqerweDaylist.get(iflarge).toString().split(",");

            if (i == nextQtrDaysList.size()) {
                iflarge = nextQtrDaysList.size() - 1;
            }
            String nextQtrDaString[] = nextQtrDaysList.get(iflarge).toString().split(",");
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

            if (i == nextYearDaylist.size()) {

                iflarge = nextYearDaylist.size() - 1;
            }
            String nextYearDayArString[] = nextYearDaylist.get(iflarge).toString().split(",");//4ele

            stringlimit = weklstrng.length + prdaystrng.length + lstyrwkdaystr.length + cmonthstrng.length + pmntdaystrng.length + pyrdaystrng.length + cqerwdaystrng.length
                    + clastqerweDaystrng.length + clastyerqerDaystrng.length + cyearDaystrng.length + lyearDaystrng.length + 1
                    + priDayArray.length + neDayArray.length + nextYearWeekDaysArray.length + nYerMonthDayString.length + nextMonthString.length
                    + nextQTrString.length + nextQtrDaString.length + nextYearDayArString.length + nextDayWeekAString.length;
            insrtobj = new Object[stringlimit];
            insrtobj[0] = calendarId;
            // 
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
                insertstrng.append(insertstrng + "," + insrtobj[l]);
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
            totlength = count;
            for (j = count + 2, k = 0; j < totlength + priDayArray.length + 2; j++, k++) {
                insrtobj[l] = priDayArray[k].toString();
                count++;
                insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                l++;

            }
            totlength = count;
            for (j = count + 2, k = 0; j < totlength + neDayArray.length + 2; j++, k++) {
                insrtobj[l] = neDayArray[k].toString();
                count++;
                insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                l++;

            }
            totlength = count;
            for (j = count + 2, k = 0; j < totlength + nextDayWeekAString.length + 2; j++, k++) {
                insrtobj[l] = nextDayWeekAString[k].toString();
                count++;
                insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                l++;

            }
            totlength = count;


            for (j = count + 2, k = 0; j < totlength + nextYearWeekDaysArray.length + 2; j++, k++) {
                insrtobj[l] = nextYearWeekDaysArray[k].toString();
                count++;
                insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                l++;

            }
            totlength = count;

            for (j = count + 2, k = 0; j < totlength + nextMonthString.length + 2; j++, k++) {
                insrtobj[l] = nextMonthString[k].toString();
                count++;
                insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                l++;

            }
            totlength = count;

            for (j = count + 2, k = 0; j < totlength + nYerMonthDayString.length + 2; j++, k++) {
                insrtobj[l] = nYerMonthDayString[k].toString();
                count++;
                insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                l++;

            }
            totlength = count;

            for (j = count + 2, k = 0; j < totlength + nextQTrString.length + 2; j++, k++) {
                insrtobj[l] = nextQTrString[k].toString();
                count++;
                insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                l++;

            }
            totlength = count;
            for (j = count + 2, k = 0; j < totlength + nextQtrDaString.length + 2; j++, k++) {
                insrtobj[l] = nextQtrDaString[k].toString();
                count++;
                insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                l++;

            }
            totlength = count;
            for (j = count + 2, k = 0; j < totlength + nextYearDayArString.length + 2; j++, k++) {
                insrtobj[l] = nextYearDayArString[k].toString();
                count++;
                insertstrng.append(insertstrng).append(",").append(insrtobj[l]);
                l++;

            }

            finalQuery = PbDb.buildQuery(addPrDayDenom, insrtobj);
            // 
            queryList.add(finalQuery);
            l = 1;

        }

        return queryList;
    }
}
