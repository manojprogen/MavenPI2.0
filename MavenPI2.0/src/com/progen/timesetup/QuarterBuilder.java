/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.timesetup;

import java.util.*;
import prg.db.PbDb;

/**
 *
 * @author progen
 */
class QuarterBuilder extends CalendarBuilder {

    String execute = "yes";
    TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
    PbDb pbDb = new PbDb();

    @Override
    protected ArrayList buildCalendarData(Date startDate, Date endDate, String calendarId) {
        Date stforDenom = (Date) startDate.clone();
        Date edforDenom = (Date) endDate.clone();
        ArrayList<String> queryList = new ArrayList();
        ArrayList<String> qtrDetailsList = new ArrayList();
        ArrayList querysOfQtrDenom = new ArrayList();

        String finalQuery = "";
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        endCalendar.setTime(endDate);
        String insertPrQuarter = null;
        if (calendarId == null) {
            insertPrQuarter = resBundle.getString("addPrQtr1");
        } else {
            insertPrQuarter = resBundle.getString("addPrQtr");
        }
        String strdate = startCalendar.get(Calendar.DATE) + "-" + startCalendar.getDisplayName(2, Calendar.SHORT, Locale.getDefault()) + "-" + startCalendar.get(Calendar.YEAR);
        String dbyear = String.valueOf(startCalendar.get(Calendar.YEAR));
        String endDatestr = "";
        String QtrList[] = new String[4];
        Object[] prQuarter = new Object[6];
        prQuarter[0] = calendarId;
//        String qtrdates = "";
        StringBuilder qtrdates=new StringBuilder();
        QuarterPriorNextHelper quarterPriorNextHelper = new QuarterPriorNextHelper();
        for (int i = 1; i <= 4; i++) {
            Calendar calendar = (Calendar) startCalendar.clone();
            prQuarter[1] = i;
            startCalendar.add(Calendar.MONTH, 3);
            prQuarter[2] = startCalendar.get(Calendar.YEAR);
            prQuarter[3] = strdate;
            qtrdates.append(i).append(",").append(dbyear).append(",").append(strdate);
            String sendstartdate = strdate;
            startCalendar.add(Calendar.DATE, -1);
            endDatestr = startCalendar.get(5) + "-" + startCalendar.getDisplayName(2, Calendar.SHORT, Locale.getDefault()) + "-" + startCalendar.get(1);

            startCalendar.add(Calendar.DATE, 1);
            strdate = (startCalendar.get(5)) + "-" + startCalendar.getDisplayName(2, Calendar.SHORT, Locale.getDefault()) + "-" + startCalendar.get(1);
            dbyear = String.valueOf(startCalendar.get(1));
            //startCalendar.add(Calendar.DATE, -1);

            int totday = (int) quarterPriorNextHelper.getTotalNumberOfDays(calendar, startCalendar);
            prQuarter[4] = endDatestr;
            prQuarter[5] = "Q-" + i + "-" + dbyear;
            qtrdates.append(",").append(endDatestr).append(",").append(totday);
            QtrList[i - 1] = qtrdates.toString();

            finalQuery = pbDb.buildQuery(insertPrQuarter, prQuarter);
            if (execute.equalsIgnoreCase("yes")) {
                queryList.add(finalQuery);
            }

        }
        if (execute.equalsIgnoreCase("no")) {
            qtrDetailsList.addAll(Arrays.asList(QtrList));
        }

        if (execute.equalsIgnoreCase("yes")) {
            querysOfQtrDenom = quarterdenomBuilder(stforDenom, edforDenom, calendarId);
        }

        if (execute.equalsIgnoreCase("yes")) {

            queryList.addAll(querysOfQtrDenom);

            //
            return queryList;
        } else {
            return qtrDetailsList;
        }

    }

    public ArrayList quarterdenomBuilder(Date startDate, Date endDate, String calendarId) {
        ArrayList queryList = new ArrayList();
        String finalQuery = "";
        this.execute = "no";
        QuarterPriorNextHelper quarterPriorNextHelper = new QuarterPriorNextHelper();
        YearPriorNextHelper yearPriorNextHelper = new YearPriorNextHelper();
        String addPrQtrDenom = null;
        if (calendarId == null) {
            addPrQtrDenom = resBundle.getString("addPrQtrDenom1");
        } else {
            addPrQtrDenom = resBundle.getString("addPrQtrDenom");
        }
        Object[] quarterObj = new Object[16];
        ArrayList qtrArrayList1 = buildCalendarData((Date) startDate.clone(), (Date) endDate.clone(), calendarId);
        String[] qtrArray1 = new String[qtrArrayList1.size()];
        for (int i = 0; i < qtrArrayList1.size(); i++) {
            qtrArray1[i] = (String) qtrArrayList1.get(i);
        }

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime((Date) startDate.clone());
        Calendar calendar1 = (Calendar) startCalendar.clone();
        Calendar calendar2 = (Calendar) endCalendar.clone();
        // 
        //
        endCalendar.setTime((Date) endDate.clone());
        String priorQuarterDetails[] = quarterPriorNextHelper.findPrior(startCalendar, endCalendar);
//        
//        
        String getstrtendyear[] = yearPriorNextHelper.findPrior(calendar1, endCalendar);
        String priorstartyear = getstrtendyear[4];
        String priorendyear = getstrtendyear[5];
        String[] priorstrtarr = new String[3];
        priorstrtarr = priorstartyear.split("-");
        int month = Integer.parseInt(priorstrtarr[1].toString());
        int day = Integer.parseInt(priorstrtarr[0].toString());
        int year = Integer.parseInt(priorstrtarr[2].toString());
        String[] priorendarr = new String[3];
        priorendarr = priorendyear.split("-");
        int endmnth = Integer.parseInt(priorendarr[1].toString());
        int endday = Integer.parseInt(priorendarr[0].toString());
        int endyer = Integer.parseInt(priorendarr[2].toString());
        startCalendar.set(year, month, day);
        endCalendar.set(endyer, endmnth, endday);
        ArrayList qtrArrayList2 = buildCalendarData(startCalendar.getTime(), endCalendar.getTime(), calendarId);
        String[] qtrArray2 = new String[qtrArrayList2.size()];
        for (int i = 0; i < qtrArrayList2.size(); i++) {
            qtrArray2[i] = (String) qtrArrayList2.get(i);
        }
        int i = 0;
        for (i = 0; i < qtrArray1.length; i++) {
            String det1slist[] = qtrArray1[i].split(",");
            // ////////////////////////.println("det2slist===" + dets2[i]);
            String det2slist[] = priorQuarterDetails[i].split(",");
            String det3slist[] = qtrArray2[i].split(",");
            quarterObj[1] = det1slist[0];
            quarterObj[2] = det1slist[1];
            quarterObj[3] = det1slist[2];
            quarterObj[4] = det1slist[3];
            quarterObj[5] = det1slist[4];
            quarterObj[6] = det2slist[4];
            quarterObj[7] = det2slist[0];
            quarterObj[8] = det2slist[1];
            quarterObj[9] = det2slist[2];
            quarterObj[10] = det2slist[3];
            quarterObj[11] = det3slist[0];
            quarterObj[12] = det3slist[1];
            quarterObj[13] = det3slist[2];
            quarterObj[14] = det3slist[3];
            quarterObj[15] = det3slist[4];
            finalQuery = pbDb.buildQuery(addPrQtrDenom, quarterObj);
            queryList.add(finalQuery);

        }
        this.execute = "yes";
//        
        return queryList;
    }
}
