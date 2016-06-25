/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.timesetup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import prg.db.PbDb;

/**
 *
 * @author progen
 */
class WeekBuilder extends CalendarBuilder {

    TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
    WeekPriorNextHelper weekPriorNextHelper = new WeekPriorNextHelper();
    PbDb PbDb = new PbDb();

    @Override
    protected ArrayList buildCalendarData(Date startDate, Date endDate, String calendarId) {
        ArrayList queryList = new ArrayList();
        String finalQuery = "";
        String addPrWeek = null;
        if (calendarId == null) {
            addPrWeek = resBundle.getString("addPrWeek1");
        } else {
            addPrWeek = resBundle.getString("addPrWeek");
        }
        Object[] obj1 = new Object[6];

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        endCalendar.setTime(endDate);
        Calendar calendar1 = (Calendar) startCalendar.clone();
        Calendar calendar2 = (Calendar) endCalendar.clone();


        weekPriorNextHelper.setWeekFlag("No");
        // 
        //
        String dets2[] = weekPriorNextHelper.findPrior(startCalendar, endCalendar);
        for (int i = 0; i < dets2.length; i++) {

            String dets1[] = dets2[i].split(",");
            obj1[0] = calendarId;
            obj1[1] = dets1[0];
            obj1[2] = dets1[1];
            obj1[3] = dets1[2];
            obj1[4] = dets1[3];
            obj1[5] = "W-" + dets1[0] + "-" + dets1[1];
            finalQuery = PbDb.buildQuery(addPrWeek, obj1);
            queryList.add(finalQuery);

        }
        ArrayList denomQuerys = weekDenombuilder(calendar1, calendar2, calendarId);

        queryList.addAll(denomQuerys);


        //   
        return queryList;
    }

    public ArrayList weekDenombuilder(Calendar startCalendar, Calendar endCalendar, String calendarId) {
        ArrayList queryList = new ArrayList();
        String finalQuery = "";
        YearPriorNextHelper yearPriorNextHelper = new YearPriorNextHelper();
        String addPrWeekDenom = null;
        if (calendarId == null) {
            addPrWeekDenom = resBundle.getString("addPrWeekDenom1");
        } else {
            addPrWeekDenom = resBundle.getString("addPrWeekDenom");
        }
        weekPriorNextHelper.setWeekFlag("No");
        String dets1[] = weekPriorNextHelper.findPrior(startCalendar, endCalendar);
        weekPriorNextHelper.setWeekFlag("Yes");
        String dets2[] = weekPriorNextHelper.findPrior(startCalendar, endCalendar);
        String getstrtendyear[] = yearPriorNextHelper.findPrior(startCalendar, endCalendar);
        String priorstartyear = getstrtendyear[4];
        String priorendyear = getstrtendyear[5];
        String priorstrtarr[] = priorstartyear.split("-");
        int premonth = Integer.parseInt(priorstrtarr[1].toString());
        int preday = Integer.parseInt(priorstrtarr[0].toString());
        int preyer = Integer.parseInt(priorstrtarr[2].toString());
        String[] priorendarr = new String[3];
        startCalendar.set(preyer, premonth, preday);
        priorendarr = priorendyear.split("-");
        int preendmnth = Integer.parseInt(priorendarr[1].toString());
        int preendday = Integer.parseInt(priorendarr[0].toString());
        int preendyer = Integer.parseInt(priorendarr[2].toString());
        endCalendar.set(preendyer, preendmnth, preendday);
        weekPriorNextHelper.setWeekFlag("No");
        String dets3[] = weekPriorNextHelper.findPrior(startCalendar, endCalendar);

        Object[] obj1 = new Object[14];
        obj1[0] = calendarId;
        for (int i = 0; i < dets1.length; i++) {
            String cdets[] = dets1[i].split(",");
            //////////////////////// ////////////////////////.println("--1-"+cdets[0]);
            String pdets[] = dets2[i].split(",");
            //////////////////////////.println("--2-"+pdets[0]);
            String pydets[] = dets3[i].split(",");
            ////////////////////// ////////////////////////.println("--3-"+dets3[i]);
            obj1[1] = cdets[0];
            obj1[2] = cdets[1];
            obj1[3] = cdets[2];
            obj1[4] = cdets[3];
            obj1[5] = cdets[4];

            //if (cdets[0].equalsIgnoreCase("52")) {
            //     obj1[6] = 1;
            // } else
            if (cdets[0].equalsIgnoreCase("1")) {
                obj1[6] = 52;
            } else {
                obj1[6] = Integer.parseInt(cdets[0]) - 1;
            }
            obj1[7] = pdets[1];
            obj1[8] = pdets[2];
            obj1[9] = pdets[3];
            // obj1[9] = pydets[0];
            obj1[10] = cdets[0];
            obj1[11] = pydets[1];
            obj1[12] = pydets[2];
            obj1[13] = pydets[3];
            finalQuery = PbDb.buildQuery(addPrWeekDenom, obj1);
            queryList.add(finalQuery);

        }

        //
        return queryList;
    }
}
