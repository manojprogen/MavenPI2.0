/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler;

import com.progen.portal.PortalPdfGenerator;
import com.progen.report.ReportParameter;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.query.TrendDataSet;
import com.progen.report.query.TrendDataSetHelper;
import com.progen.report.scorecard.tracker.ScorecardTracker;
import com.progen.report.scorecard.tracker.ScorecardTrackerJob;
import com.progen.scheduler.db.SchedulerDAO;
import com.progen.scheduler.tracker.Tracker;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.*;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import prg.db.OneViewLetDetails;
import prg.reportscheduler.AOSchedulerJob;
import prg.reportscheduler.HeadLinesSchedulerJob;
import prg.reportscheduler.KpiChartSchedulerJob;
import prg.reportscheduler.LoadSchedulerJob;
import prg.reportscheduler.ReportSchedulerJob;
import prg.tracker.scheduler.TrackerJob;

/**
 *
 * @author progen
 */
public class SchedulerBD {

    public static Logger logger = Logger.getLogger(SchedulerBD.class);

    public void scheduleReport(ReportSchedule schedule, boolean isEdit) {
        try {
            Scheduler shed = getScheduler();
            String reportId = "";
            if (shed != null) {
                if (!isEdit) {
                    reportId = String.valueOf(schedule.getReportScheduledId());
                }

                String jobName = "";
                String groupName = "";
                String triggerName = "";

                if (!schedule.isFromOneview()) {
                    jobName = "ReportScheduler" + schedule.getReportScheduledId();
                    groupName = "Reports";
                    triggerName = "Trigger-" + jobName;
                } else {
                    jobName = "OneviewMeasureAlert" + schedule.getReportScheduledId();
                    groupName = "OneviewMeasure";
                    triggerName = "Trigger-" + jobName;
                }

                //Check if the job already exists in the scheduler. If present, delete the job
                try {
                    JobDetail tempJd = shed.getJobDetail(jobName, groupName);
                    if (tempJd != null) {
                        shed.deleteJob(jobName, groupName);
                    }
                } catch (SchedulerException e) {
                }

                JobDetail jd = new JobDetail(jobName, groupName, ReportSchedulerJob.class);

                jd.getJobDataMap().put("scheduleReport", schedule);
                String frequency = schedule.getFrequency();
                Date startDate = schedule.getStartDate();
                Date endDate = schedule.getEndDate();

                Date today = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
                // 
                if (today.after(startDate) && today.before(new Date(formatter.format(endDate.getTime() + MILLIS_IN_DAY)))) {
                    String expression = "";
                    String[] t = schedule.getScheduledTime().split(":");
                    if (frequency.equalsIgnoreCase("Daily")) {
                        expression = "0 " + t[1] + " " + t[0] + " * * ?";
                    } else if (frequency.equalsIgnoreCase("Weekly")) {
                        String particularDay = schedule.getParticularDay();
                        // String particularDay="B";
                        if (particularDay != null) {
                            expression = "0 " + t[1] + " " + t[0] + " ? * " + Integer.parseInt(particularDay);
                        } else if ("B".equalsIgnoreCase(particularDay)) {
                            expression = "0 " + t[1] + " " + t[0] + " ? * 1";
                        } else {
                            expression = "0 " + t[1] + " " + t[0] + " ? * 2";
                        }
                    } else if (frequency.equalsIgnoreCase("Hourly")) {
                        String particularDate = schedule.getParticularDay();
                        if (particularDate != null) {
                            expression = "0 " + t[1] + " " + t[0] + "/" + Integer.parseInt(particularDate) + " * * ? *";
                        }
                    } else {
//                        
                        String particularDate = schedule.getParticularDay();
//                        
                        if ("L".equalsIgnoreCase(particularDate)) {
                            expression = "0 " + t[1] + " " + t[0] + " ? * 6L";  //0 0 0 L * ? is not supported by the scheduler for last day of the month.
                        } //So providing a hack by using last friday of the month.
                        else if ("B".equalsIgnoreCase(particularDate)) {
                            expression = "0 " + t[1] + " " + t[0] + " 1 * ?";
                        } else if (particularDate != null) {
                            expression = "0 " + t[1] + " " + t[0] + " " + Integer.parseInt(particularDate) + " * ?";
                        } else {
                            expression = "0 " + t[1] + " " + t[0] + " 1 * ?";
                        }
                        //expression = "0 " + t[1] + " " + t[0] + " " +particularDate+" * ?";
                    }

//                            
                    //                shed.unscheduleJob(triggerName, groupName);
                    if ("PortalPDF".equalsIgnoreCase(schedule.getContentType()) && schedule.getPortalFileName() == null) {
                        String headerTitle = "Progen Business Solutions";
                        PortalPdfGenerator pdf = new PortalPdfGenerator();
                        pdf.setHeaderTitle(headerTitle);
                        pdf.setReportName("Pdf Report");
                        pdf.setRequest(schedule.getRequest());
                        pdf.setResponse(schedule.getResponse());
                        pdf.setContentType(schedule.getContentType());
                        pdf.portalPDF();
                        String pdfFilename = pdf.getFileName();
                        schedule.setPortalFileName(pdfFilename);
                    }

                    CronExpression cex = new CronExpression(expression);
                    CronTrigger ct = new CronTrigger(triggerName, groupName, jobName, groupName, startDate, endDate, expression);
                    shed.scheduleJob(jd, ct);
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public void scheduleTracker(Tracker tracker, boolean isEdit) {
        try {
            Scheduler shed = getScheduler();
            String reportId = "";
            if (shed != null) {
                if (!isEdit) {
                    reportId = String.valueOf(tracker.getTrackerId());
                }

                String jobName = "Tracker" + tracker.getTrackerId();
                String groupName = "Trackers";
                String triggerName = "Trigger-" + jobName;

                //Check if the job already exists in the scheduler. If present, delete the job
                try {
                    JobDetail tempJd = shed.getJobDetail(jobName, groupName);
                    if (tempJd != null) {
                        shed.deleteJob(jobName, groupName);
                    }
                } catch (SchedulerException e) {
                }

                JobDetail jd = new JobDetail(jobName, groupName, TrackerJob.class);
                jd.getJobDataMap().put("Tracker", tracker);

                String time = tracker.getScheduledTime();
                Date startDate = tracker.getStartDate();
                Date endDate = tracker.getEndDate();

                Date today = new Date();
                if (today.after(startDate) && today.before(endDate)) {
                    String[] t = time.split(":");
                    String frequency = tracker.getFrequency();
                    String expression = "";
                    if ("Daily".equals(frequency)) { //Daily
                        expression = "0 " + t[1] + " " + t[0] + " * * ?";
                    } else if ("Monthly".equals(frequency)) { //Monthly
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(startDate);
                        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                        expression = "0 " + t[1] + " " + t[0] + " " + dayOfMonth + " * ?";
                    } else if ("Quarterly".equals(frequency)) { //Quarterly
                        expression = "0 " + t[1] + " " + t[0] + " * * ?";
                    } else if ("Yearly".equals(frequency)) { //Yearly
                        expression = "0 " + t[1] + " " + t[0] + " * * ?";
                    }
                    CronExpression cex = new CronExpression(expression);
                    CronTrigger ct = new CronTrigger(triggerName, groupName, jobName, groupName, startDate, endDate, expression);
                    shed.scheduleJob(jd, ct);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void runTracker(int trackerId, boolean fromStudio) {
        SchedulerDAO dao = new SchedulerDAO();
        Tracker tracker = null;
        if (fromStudio) {
            tracker = dao.getTracker(trackerId, false);
        } else {
            tracker = dao.getTracker(trackerId, true);
        }
        TrackerJob job = new TrackerJob();
        job.sendTrackerHtml(tracker);
    }

    public Scheduler getScheduler() {
        Scheduler sched = null;
        try {
            sched = StdSchedulerFactory.getDefaultScheduler();
            if (!sched.isStarted()) {
                sched.start();
            }
        } catch (SchedulerException ex) {
            logger.error("Exception:", ex);
        }
        return sched;
    }

    public String buildSchedulerHistory(String timeLevel, String measureId, String dimId, List<String> memberId, String userId, String folderId, String dimName, String msrName, String frmMsr) {
        ReportParameter repParam = new ReportParameter();
        List<String> measIds = new ArrayList<String>();
        measIds.add(measureId);
        repParam.setParameter(dimId, memberId, false);
        ArrayList<String> rowViewBys = new ArrayList<String>();
        Iterator<String> dimValuesIter = null;
        BigDecimal maxInPeriod = null;
        BigDecimal minInPeriod = null;
        BigDecimal threeMonthsAvg = null;
        BigDecimal sixMonthsAvg = null;
        BigDecimal nineMonthsAvg = null;
        BigDecimal twelveMonthsAvg = null;
        BigDecimal currValue = null;

        String[] strOprtrs = {"<", ">", "<=", ">=", "="};
        TrendDataSet consolidateDataSet = null;
        HashMap<String, TrendDataSet> datasets = new HashMap<String, TrendDataSet>();
        StringBuilder history = new StringBuilder();
        rowViewBys.add(dimId);
        repParam.setViewBys(rowViewBys, new ArrayList<String>());
        if (!(measureId.startsWith("A_"))) {
            measureId = "A_" + measureId;
        }

        if (memberId.contains("All")) {
            consolidateDataSet = TrendDataSetHelper.buildTrendDataSet(measIds, timeLevel, userId, folderId);
            maxInPeriod = consolidateDataSet.findMaxInPeriod(measureId);
            minInPeriod = consolidateDataSet.findMinInPeriod(measureId);
            threeMonthsAvg = consolidateDataSet.findLastNPeriodAverage(measureId, 3);
            sixMonthsAvg = consolidateDataSet.findLastNPeriodAverage(measureId, 6);
            nineMonthsAvg = consolidateDataSet.findLastNPeriodAverage(measureId, 9);
            twelveMonthsAvg = consolidateDataSet.findLastNPeriodAverage(measureId, 12);
            currValue = consolidateDataSet.getLastPeriodValue(measureId);
        } else {
            datasets = TrendDataSetHelper.buildTrendDataSet(measIds, repParam, timeLevel, userId, folderId);
            Set<String> dimValuesSet = datasets.keySet();
            dimValuesIter = dimValuesSet.iterator();
            while (dimValuesIter.hasNext()) {
                String dimValue = dimValuesIter.next();
                TrendDataSet trendDataSet = datasets.get(dimValue);
                maxInPeriod = trendDataSet.findMaxInPeriod(measureId);
                minInPeriod = trendDataSet.findMinInPeriod(measureId);
                threeMonthsAvg = trendDataSet.findLastNPeriodAverage(measureId, 3);
                sixMonthsAvg = trendDataSet.findLastNPeriodAverage(measureId, 6);
                nineMonthsAvg = trendDataSet.findLastNPeriodAverage(measureId, 9);
                twelveMonthsAvg = trendDataSet.findLastNPeriodAverage(measureId, 12);
                currValue = trendDataSet.getLastPeriodValue(measureId);
            }
        }
        if (frmMsr.equals("false")) {
            history.append("<tr>");
            history.append("</tr><tr>");
            history.append("<font style='font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>View History</font>");
            history.append("</tr><tr>");
            history.append("<tr><td style='height:15px'></td></tr>");
            history.append("<td valign=\"top\" class=\"myHead\">").append(dimName).append("</td>");

            history.append("<td><input type=\"text\" style=\"text-align:right;width: 50%;\" readonly value=\"").append(memberId).append("\" ></td>");
            history.append("<td valign=\"top\"  class=\"myHead\">Current Value </td>");
            history.append("<td><input  type=\"text\" style=\"text-align:right;width: 50%;\" readonly   value=\"").append(NumberFormatter.getModifiedNumber(currValue)).append("\" ></td>");  //Target Basis
            history.append("<td><input type=\"hidden\" style=\"text-align:right;width: 50%;\" readonly  id=\"currVal\" value=\"").append(NumberFormatter.getModifidNumber(currValue)).append("\" ></td>");  //Target Basis
            history.append("</tr><tr>");
            history.append("<td valign=\"top\"  class=\"myHead\">Maximum in period</td>");
            history.append("<td><input type=\"text\" style=\"text-align:right;width: 50%;\" readonly value=\"").append(NumberFormatter.getModifiedNumber(maxInPeriod)).append("\" ></td>");

            history.append("<td valign=\"top\"  class=\"myHead\">Minimum in period</td>");
            history.append("<td><input type=\"text\" style=\"text-align:right;width: 50%;\" readonly value=\"").append(NumberFormatter.getModifiedNumber(minInPeriod)).append("\"></td>");
            history.append("</tr><tr>");
            history.append("<td valign=\"top\" class=\"myHead\">Average in 3 ").append(timeLevel).append("</td>");
            history.append("<td><input type=\"text\" style=\"text-align:right;width: 50%;\" readonly value=\"").append(NumberFormatter.getModifiedNumber(threeMonthsAvg)).append("\"></td>");


            history.append("<td valign=\"top\"  class=\"myHead\">Average in 6 ").append(timeLevel).append("</td>");
            history.append("<td><input type=\"text\" style=\"text-align:right;width: 50%;\" readonly value=\"").append(NumberFormatter.getModifiedNumber(sixMonthsAvg)).append("\"></td>");
            history.append("</tr><tr>");
            history.append("<td valign=\"top\" class=\"myHead\">Average in 9 ").append(timeLevel).append("</td>");
            history.append("<td><input type=\"text\" style=\"text-align:right;width: 50%;\" readonly value=\"").append(NumberFormatter.getModifiedNumber(nineMonthsAvg)).append("\"></td>");

            history.append("<td valign=\"top\"  class=\"myHead\">Average in 12 ").append(timeLevel).append("</td>");
            history.append("<td><input type=\"text\" style=\"text-align:right;width: 50%;\" readonly value=\"").append(NumberFormatter.getModifiedNumber(twelveMonthsAvg)).append("\"></td>");
            history.append("</tr>");
        } else {
            history.append("<table><tr>");
            history.append("</tr><tr>");
            history.append("<font style='font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>View History</font>");
            history.append("</tr><tr>");
            history.append("<tr><td style='height:15px'></td></tr>");
//            history.append("<td valign=\"top\" class=\"myHead\">").append(dimName).append("</td>") ;
//            history.append("<td><input type=\"text\" style=\"text-align:right;width: 50%;\" readonly value=\"").append(memberId).append("\" ></td>");
            history.append("<td valign=\"top\"  class=\"myHead\">Current Value </td>");
            history.append("<td><input type=\"text\" style=\"text-align:right;width: 50%;\" readonly value=\"").append(NumberFormatter.getModifiedNumber(currValue)).append("\" ></td>");

            history.append("<td valign=\"top\"  class=\"myHead\">Maximum in period</td>");
            history.append("<td><input type=\"text\" style=\"text-align:right;width: 50%;\" readonly value=\"").append(NumberFormatter.getModifiedNumber(maxInPeriod)).append("\" ></td>");
            history.append("</tr><tr>");
            history.append("<td valign=\"top\"  class=\"myHead\">Minimum in period</td>");
            history.append("<td><input type=\"text\" style=\"text-align:right;width: 50%;\" readonly value=\"").append(NumberFormatter.getModifiedNumber(minInPeriod)).append("\"></td>");

            history.append("<td valign=\"top\" class=\"myHead\">Average in 3 ").append(timeLevel).append("</td>");
            history.append("<td><input type=\"text\" style=\"text-align:right;width: 50%;\" readonly value=\"").append(NumberFormatter.getModifiedNumber(threeMonthsAvg)).append("\"></td>");
            history.append("</tr><tr>");

            history.append("<td valign=\"top\"  class=\"myHead\">Average in 6 ").append(timeLevel).append("</td>");
            history.append("<td><input type=\"text\" style=\"text-align:right;width: 50%;\" readonly value=\"").append(NumberFormatter.getModifiedNumber(sixMonthsAvg)).append("\"></td>");

            history.append("<td valign=\"top\" class=\"myHead\">Average in 9 ").append(timeLevel).append("</td>");
            history.append("<td><input type=\"text\" style=\"text-align:right;width: 50%;\" readonly value=\"").append(NumberFormatter.getModifiedNumber(nineMonthsAvg)).append("\"></td>");
            history.append("</tr><tr>");
            history.append("<td valign=\"top\"  class=\"myHead\">Average in 12 ").append(timeLevel).append("</td>");
            history.append("<td><input type=\"text\" style=\"text-align:right;width: 50%;\" readonly value=\"").append(NumberFormatter.getModifiedNumber(twelveMonthsAvg)).append("\"></td>");
            history.append("</tr>");
            history.append("</table>");
            history.append("<table style='width:100%' align='center'><tr><td colspan='2' style='height:15px'></td></tr> </table>");
            history.append("<table><tr><font style='font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>Apply Filter</font></tr>");
            history.append("<tr><td style='height:15px'></td></tr></table>");
            history.append("<table style='width:100%'><tr><td width='2%'style='font-weight: bolder';font-size: 11px;> ").append(msrName).append("</td>");
            history.append("<td width='1%' align='left'> When</td>");
            history.append("<td width='1%'><select name='operators' id='condOperator'><option value='none'>-Select-</option>");
            for (String Str : strOprtrs) {
                history.append("<option  value='").append(Str).append(">'").append(Str).append("</option>");

            }
            history.append("</select></td><td width='5%'>");
            history.append("<Input type='text' class='myTextbox3' name='sValues' id='condVal' style='width:90px' ></td></tr></table>");
            history.append("<table style='width:100%'><tr><td style='height:15px'></td></tr><tr><td colspan='2' align='center'><input type='button' class='navtitle-hover' style='width:auto' value='Done' onclick='closeHistory()'> </td></tr></table>");
        }
        return history.toString();

    }

    public void runScheduler(String schedulerId, boolean fromStudio) {
        SchedulerDAO dao = new SchedulerDAO();
        ReportSchedule schedule = null;
        if (fromStudio) {
            schedule = dao.getReportScheduleDetails(schedulerId, false);
        } else {
            schedule = dao.getReportScheduleDetails(schedulerId, true);
        }
        boolean isAutoSplit = schedule.isIsAutoSplited();
        ReportSchedulerJob job = new ReportSchedulerJob();
        if (schedule.getReportSchedulePrefrences() != null && !(schedule.getReportSchedulePrefrences().isEmpty()) || isAutoSplit) {
            try {
                job.sendSchedulerMail(schedule);
            } catch (ParseException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void scheduleScorecardTracker(ScorecardTracker tracker) {
        try {
            Scheduler shed = getScheduler();
            int reportId = tracker.getsCardShedId();
            if (shed != null) {

                String groupName = "ScorecardTrackers";
                String jobName = "Scorecard" + tracker.getsCardShedId();
                String triggerName = "Trigger-" + jobName;

                //Check if the job already exists in the scheduler. If present, delete the job
                try {
                    JobDetail tempJd = shed.getJobDetail(jobName, groupName);
                    if (tempJd != null) {
                        shed.deleteJob(jobName, groupName);
                    }
                } catch (SchedulerException e) {
                }

                JobDetail jd = new JobDetail(jobName, groupName, ScorecardTrackerJob.class);
                jd.getJobDataMap().put("ScorecardTracker", tracker);

                String time = tracker.getScheduleTime();
                Date startDate = tracker.getStartDate();
                Date endDate = tracker.getEndDate();

                Date today = new Date();
                if (today.after(startDate) && today.before(endDate)) {
                    String[] t = time.split(":");
                    String frequency = tracker.getFrequency();
                    String expression = "";
                    if ("1".equals(frequency)) { //Daily
                        expression = "0 " + t[1] + " " + t[0] + " ? * *";
                    } else if ("2".equals(frequency)) { //Monthly
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(startDate);
                        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                        expression = "0 " + t[1] + " " + t[0] + " " + dayOfMonth + " * ?";
                    } else if ("3".equals(frequency)) { //Quarterly
                        expression = "0 " + t[1] + " " + t[0] + " * * ?";
                    } else if ("4".equals(frequency)) { //Yearly
                        expression = "0 " + t[1] + " " + t[0] + " * * ?";
                    }
                    CronExpression cex = new CronExpression(expression);
                    CronTrigger ct = new CronTrigger(triggerName, groupName, jobName, groupName, startDate, endDate, expression);
                    shed.scheduleJob(jd, ct);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void secheduleHeadLines(String[] headlineId, String[] headlinesNames, String[] toAddress, String[] scheduleTime, String timeVal, int totalMins, String hourlyTime, String description) {


        try {
            Scheduler shed = getScheduler();
            String jobName = "";
            String groupName = "";
            String triggerName = "";
            totalMins = totalMins * 1000;

            //Check if the job already exists in the scheduler. If present, delete the job

            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);

            for (int i = 0; i < headlineId.length; i++) {
                jobName = "HeadLinesScheduler" + headlineId[i];
                groupName = "HeadLines";
                triggerName = "Trigger-" + jobName;
                try {
                    JobDetail tempJd = shed.getJobDetail(jobName, groupName);
                    if (tempJd != null) {
                        shed.deleteJob(jobName, groupName);
                    }
                } catch (SchedulerException e) {
                }

                JobDetail jd = new JobDetail(jobName, groupName, HeadLinesSchedulerJob.class);

                jd.getJobDataMap().put("headlineId", headlineId[i]);
                jd.getJobDataMap().put("toAddress", toAddress[i]);
                jd.getJobDataMap().put("scheduleTime", scheduleTime[0]);
                jd.getJobDataMap().put("headlinesNames", headlinesNames[i]);
                jd.getJobDataMap().put("description", description);
                String expression = "";
                // String expression = "0 "+scheduleTime[i]+" * * ?";
                //String expression = "0 "+scheduleTime[i]+" "+day+" "+month+" ? "+year;
//                    CronExpression cex = new CronExpression(expression);
//                    CronTrigger ct = new CronTrigger(triggerName, groupName, jobName, groupName, expression);
//                    SimpleTrigger trigger=new SimpleTrigger("triggername", null, cal.getTime(), null, SimpleTrigger.REPEAT_INDEFINITELY,(long)totalMins );
//                    if(!timeVal.equalsIgnoreCase("single")&&totalMins!=0)
//                    shed.scheduleJob(jd, trigger);
//                    else
//                    shed.scheduleJob(jd, ct);
                if (timeVal.equalsIgnoreCase("onlyonce")) {
                    expression = "0 " + scheduleTime[0] + " * * ?";

                } else if (timeVal.equalsIgnoreCase("daily")) {
                    expression = "0 " + scheduleTime[0] + " * * ? *";
                } else if (timeVal.equalsIgnoreCase("hourly")) {
                    String time[] = hourlyTime.split(" ");
                    expression = "0 " + "0/" + time[0] + " " + "*/" + time[1] + " * * ? * ";
                } else {
                    HeadLinesSchedulerJob job = new HeadLinesSchedulerJob();
                    job.sendSchedulerMail(headlineId[i], headlinesNames[i], toAddress[i], scheduleTime[0], description);
                    // expression = "0 "+sysMins+" "+sysHrs+" * * ?";
                }
                // 
                if (!timeVal.equalsIgnoreCase("frequent")) {
                    CronExpression cex = new CronExpression(expression);
                    CronTrigger ct = new CronTrigger(triggerName, groupName, jobName, groupName, expression);
                    shed.scheduleJob(jd, ct);
                    shed.start();
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public StringBuilder getOneviewNoCurrMeasureHeasder(OneViewLetDetails oneviewlet, String valu, double currVal, double priorVal, String curval) {

        StringBuilder finalStringVal = new StringBuilder();
        String fontcolor;
//         if(oneviewlet.getFontColor()!=null && !oneviewlet.getFontColor().isEmpty()){
//             fontcolor=oneviewlet.getFontColor();
//         }
//         else
        fontcolor = "#000000";
        finalStringVal.append("<table style='margin-left: 10px;width:100%;'>");//kruthika
        finalStringVal.append("<tr >");
//            finalStringVal.append("<td width='2%'></td>");
        String reportName = oneviewlet.getRepName();
        String title = reportName;
        int strlength = (oneviewlet.getWidth() / 11);
        if (reportName.length() >= strlength) {
            reportName = reportName.substring(0, strlength - 3);
            reportName += "..";
        }
        if (valu != null) {
            finalStringVal.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;white-space:nowrap'>");
            finalStringVal.append("<a style=\"\" href=\"javascript:submiturls12('" + valu + "')\">");
            finalStringVal.append("<strong id='forDillDown" + oneviewlet.getNoOfViewLets() + "' style=\"font-size: 12pt;white-space:nowrap;font-weight: normal\" title=\"" + title + "\">" + reportName + "</strong></a>");//oneviewlet.getRepName()
            finalStringVal.append("</td>");
        } else {
            finalStringVal.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;color:" + fontcolor + ";white-space:nowrap' title=\"" + title + "\" >" + reportName + "</td>"); //oneviewlet.getRepName()
        }
        finalStringVal.append("<td id=\"refreshTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-arrowrefresh-1-w\" title=\"Refresh Region\" onclick=\"refreshOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"saveTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-disk\" title=\"Save\" onclick=\"saveEachOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
        }
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"optionId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'>");
            finalStringVal.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-triangle-2-n-s\" onclick=\"selectforReadd(" + oneviewlet.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Region Options\"></a>");

            finalStringVal.append("<div id=\"reigonOptionsDivId" + oneviewlet.getNoOfViewLets() + "\" style='display:none;width:120px;height:auto;background-color:white;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;' calss=\"overlapDiv\">");
            finalStringVal.append("<table border='0' align='left' >");
            finalStringVal.append("<tr><td>");


            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"renameRegion('Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Rename</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"drillToReport('" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getReptype() + "')\"  >Drill</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"drillToReport('dashboard','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Drill To Dashboard</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"measureOptions('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + curval + "','','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','')\" title='MeasuresOptions' href='javascript:void(0)'>Measure Option</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"customTimeAggregation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" title='customTimeAggregation' href='javascript:void(0)'>CustomTimeAggregation</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"taggleNewMeasures('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + curval + "','','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "')\" title='NewMeasureType' href='javascript:void(0)'>Toggle Display</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"measureTrendGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getHeight() + "','" + oneviewlet.getWidth() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" title='MeasureTrend' href='javascript:void(0)'>Trend</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"saveEachOneVIewReg("+oneviewlet.getNoOfViewLets()+")\"  >Save</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a  style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;'  onclick=\"measureComments('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" href='javascript:void(0)' title='Add/View Comments' >Comments</a></td></tr></table>");

            finalStringVal.append("</td></tr>");
            finalStringVal.append("</table>");
            finalStringVal.append("</div>");
            finalStringVal.append("</td>");
        }

        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"optionId1" + oneviewlet.getNoOfViewLets() + "\" style='width:0px;align:right;'>");
            finalStringVal.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-plusthick\" onclick=\"selectReadd(" + oneviewlet.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Re Add Onevielet\"></a>");

            finalStringVal.append("<div id=\"readdDivId" + oneviewlet.getNoOfViewLets() + "\" style='display:none; width:90px; height:100px; background-color:#ffffff; overflow:auto;  position:absolute; text-align:left; border:1px solid #000000; border-top-width: 0px; z-index:1002;'>");
            finalStringVal.append("<table border='0' align='left' >");
            finalStringVal.append("<tr><td>");
            //  }
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('report','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Reports</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('measures','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Measures</a></td></tr></table>");

            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('dashboard','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >KPIs</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('headline','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Headlines</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('complexkpi','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Custom KPI</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('Date','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Date</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('notes','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Notes</a></td></tr></table>");
            finalStringVal.append("</td></tr>");
            finalStringVal.append("</table>");
            finalStringVal.append("</div>");
            finalStringVal.append("</td>");
        }
        finalStringVal.append("</tr></table>");
        return finalStringVal;
    }

    public void scheduleLoad(ReportSchedule schedule, boolean isEdit) {
        try {
            Scheduler shed = getScheduler();
            if (shed != null) {
                // changed by mohit for multiple schedulers
                String jobName = "LoadScheduler" + schedule.getSchedulerLoadId();
                String groupName = "Loads" + schedule.getSchedulerLoadId();
                String triggerName = "Trigger-" + jobName;
                try {
                    JobDetail tempJd = shed.getJobDetail(jobName, groupName);
                    if (tempJd != null) {
                        shed.deleteJob(jobName, groupName);
                    }
                } catch (SchedulerException e) {
                }
                System.out.println("Load Scheduled-->" + jobName);
                JobDetail jd = new JobDetail(jobName, groupName, LoadSchedulerJob.class);

                jd.getJobDataMap().put("scheduleLoad", schedule);
                String frequency = schedule.getSchedulerLoadFrequency();
                Date startDate = schedule.getSchedulerLoadStartDate();
                Date endDate = schedule.getSchedulerLoadEndDate();
                String particularDay = schedule.getParticularDay();
                Date today = new Date();
                //added by mohit
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
                if (today.after(startDate) && today.before(new Date(formatter.format(endDate.getTime() + MILLIS_IN_DAY)))) {
                    String expression = "";
                    String[] t = schedule.getSchedulerLoadTime().split(":");
                    if (frequency.equalsIgnoreCase("Daily")) {
                        expression = "0 " + t[1] + " " + t[0] + " * * ?";
                    } else if (frequency.equalsIgnoreCase("Weekly")) {

                        // String particularDay="B";
                        if (particularDay != null) {
                            expression = "0 " + t[1] + " " + t[0] + " ? * " + Integer.parseInt(particularDay);
                        } else if ("B".equalsIgnoreCase(particularDay)) {
                            expression = "0 " + t[1] + " " + t[0] + " ? * 1";
                        } else {
                            expression = "0 " + t[1] + " " + t[0] + " ? * 2";
                        }
                    } else if (frequency.equalsIgnoreCase("Monthly")) { //Monthly
                        expression = "0 " + t[1] + " " + t[0] + " " + Integer.parseInt(particularDay) + " * ?";
                    } else if (frequency.equalsIgnoreCase("Hourly")) { //Monthly //added by mohit

                        expression = "0 " + t[1] + " * 1/1 * ? *";
                    }
                    CronExpression cex = new CronExpression(expression);
                    CronTrigger ct = new CronTrigger(triggerName, groupName, jobName, groupName, startDate, endDate, expression);
                    shed.addJob(jd, true);
                    shed.scheduleJob(ct);
//                    shed.scheduleJob(jd, ct);
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }
    //Code Added by Amar

    public void scheduleReportNow(ReportSchedule schedule, boolean isEdit) {
        try {
            Scheduler shed = getScheduler();
            String reportId = "";
            if (shed != null) {
                if (!isEdit) {
                    reportId = String.valueOf(schedule.getReportScheduledId());
                }

                String jobName = "";
                String groupName = "";
                String triggerName = "";

                if (!schedule.isFromOneview()) {
                    jobName = "ReportScheduler" + schedule.getReportScheduledId();
                    groupName = "Reports";
                    triggerName = "Trigger-" + jobName;
                } else {
                    jobName = "OneviewMeasureAlert" + schedule.getReportScheduledId();
                    groupName = "OneviewMeasure";
                    triggerName = "Trigger-" + jobName;
                }

                //Check if the job already exists in the scheduler. If present, delete the job
                try {
                    JobDetail tempJd = shed.getJobDetail(jobName, groupName);
                    if (tempJd != null) {
                        shed.deleteJob(jobName, groupName);
                    }
                } catch (SchedulerException e) {
                }

                JobDetail jd = new JobDetail(jobName, groupName, ReportSchedulerJob.class);

                jd.getJobDataMap().put("scheduleReport", schedule);
                String frequency = schedule.getFrequency();
                Date startDate = schedule.getStartDate();
                Date endDate = schedule.getEndDate();

                Date today = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
                // 
                if (today.after(startDate) && today.before(new Date(formatter.format(endDate.getTime() + MILLIS_IN_DAY)))) {
                    String expression = "";
                    String[] t = schedule.getScheduledTime().split(":");
//                    if(frequency.equalsIgnoreCase("Daily"))
//                    {
                    expression = "0 " + t[1] + " " + t[0] + " * * ?";
//                    }
//                    else if(frequency.equalsIgnoreCase("Weekly"))
//                    {
//                        String particularDay=schedule.getParticularDay();
//                       // String particularDay="B";
//                        if(particularDay!=null)
//                            expression = "0 "+t[1]+" "+t[0]+" ? * "+Integer.parseInt(particularDay);
//                        else if("B".equalsIgnoreCase(particularDay))
//                            expression = "0 "+t[1]+" "+t[0]+" ? * 1";
//                        else
//                            expression = "0 "+t[1]+" "+t[0]+" ? * 2";
//                    }
//                    else if(frequency.equalsIgnoreCase("Hourly")){
//                        String particularDate=schedule.getParticularDay();
//                        if(particularDate!=null)
//                        expression="0 "+t[1]+" "+t[0]+"/"+Integer.parseInt(particularDate)+" * * ? *";
//                    }
//                    else
//                    {
////                        
//                        String particularDate=schedule.getParticularDay();
////                        
//                        if ("L".equalsIgnoreCase(particularDate))
//                            expression = "0 " + t[1] + " " + t[0] + " ? * 6L";  //0 0 0 L * ? is not supported by the scheduler for last day of the month.
//                                                                                //So providing a hack by using last friday of the month.
//                        else if ("B".equalsIgnoreCase(particularDate))
//                            expression = "0 " + t[1] + " " + t[0] + " 1 * ?";
//                        else if(particularDate!=null)
//                            expression = "0 " + t[1] + " " + t[0] + " " +Integer.parseInt(particularDate)+" * ?";
//                        else
//                           expression = "0 " + t[1] + " " + t[0] + " 1 * ?";
//                            //expression = "0 " + t[1] + " " + t[0] + " " +particularDate+" * ?";
//                    }

//                            
                    //                shed.unscheduleJob(triggerName, groupName);
                    if ("PortalPDF".equalsIgnoreCase(schedule.getContentType()) && schedule.getPortalFileName() == null) {
                        String headerTitle = "Progen Business Solutions";
                        PortalPdfGenerator pdf = new PortalPdfGenerator();
                        pdf.setHeaderTitle(headerTitle);
                        pdf.setReportName("Pdf Report");
                        pdf.setRequest(schedule.getRequest());
                        pdf.setResponse(schedule.getResponse());
                        pdf.setContentType(schedule.getContentType());
                        pdf.portalPDF();
                        String pdfFilename = pdf.getFileName();
                        schedule.setPortalFileName(pdfFilename);
                    }

                    CronExpression cex = new CronExpression(expression);
                    CronTrigger ct = new CronTrigger(triggerName, groupName, jobName, groupName, startDate, endDate, expression);
                    shed.scheduleJob(jd, ct);
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }
    //added by Dinanath for kpi chart alert
    public void scheduleKPIChart(KPIAlertSchedule schedule, boolean isEdit) {
        try {
            Scheduler shed = getScheduler();
            String reportId = "";
            if (shed != null) {
                if (!isEdit) {
                    reportId = String.valueOf(schedule.getKpiAlertSchedId());
}

                String jobName = "";
                String groupName = "";
                String triggerName = "";

                    if(schedule.isIsFromKPIChart()){
                    jobName = "KPIScheduler" + schedule.getKpiAlertSchedId();
                    groupName = "KPI";
                    triggerName = "Trigger-" + jobName;
                    }else{
                    jobName = "KPIScheduler" + schedule.getKpiAlertSchedId();
                    groupName = "KPI";
                    triggerName = "Trigger-" + jobName;
                    }
               

                //Check if the job already exists in the scheduler. If present, delete the job
                try {
                    JobDetail tempJd = shed.getJobDetail(jobName, groupName);
                    if (tempJd != null) {
                        shed.deleteJob(jobName, groupName);
                    }
                } catch (SchedulerException e) {
                }

                JobDetail jd = new JobDetail(jobName, groupName, KpiChartSchedulerJob.class);

                jd.getJobDataMap().put("scheduleReport", schedule);
                String frequency = schedule.getFrequency();
                Date startDate = schedule.getStartDate();
                Date endDate = schedule.getEndDate();

                Date today = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
                // 
                if (today.after(startDate) && today.before(new Date(formatter.format(endDate.getTime() + MILLIS_IN_DAY)))) {
                    String expression = "";
                    String[] t = schedule.getScheduledTime().split(":");
                    if (frequency.equalsIgnoreCase("Daily")) {
                        expression = "0 " + t[1] + " " + t[0] + " * * ?";
                    } else if (frequency.equalsIgnoreCase("Weekly")) {
                        String particularDay = schedule.getParticularDay();
                        // String particularDay="B";
                        if (particularDay != null) {
                            expression = "0 " + t[1] + " " + t[0] + " ? * " + Integer.parseInt(particularDay);
                        } else if ("B".equalsIgnoreCase(particularDay)) {
                            expression = "0 " + t[1] + " " + t[0] + " ? * 1";
                        } else {
                            expression = "0 " + t[1] + " " + t[0] + " ? * 2";
                        }
                    } else if (frequency.equalsIgnoreCase("Hourly")) {
                        String particularDate = schedule.getParticularDay();
                        if (particularDate != null) {
                            expression = "0 " + t[1] + " " + t[0] + "/" + Integer.parseInt(particularDate) + " * * ? *";
                        }
                    } else {
//                        
                        String particularDate = schedule.getParticularDay();
//                        
                        if ("L".equalsIgnoreCase(particularDate)) {
                            expression = "0 " + t[1] + " " + t[0] + " ? * 6L";  //0 0 0 L * ? is not supported by the scheduler for last day of the month.
                        } //So providing a hack by using last friday of the month.
                        else if ("B".equalsIgnoreCase(particularDate)) {
                            expression = "0 " + t[1] + " " + t[0] + " 1 * ?";
                        } else if (particularDate != null) {
                            expression = "0 " + t[1] + " " + t[0] + " " + Integer.parseInt(particularDate) + " * ?";
                        } else {
                            expression = "0 " + t[1] + " " + t[0] + " 1 * ?";
                        }
                        //expression = "0 " + t[1] + " " + t[0] + " " +particularDate+" * ?";
                    }


                    CronExpression cex = new CronExpression(expression);
                    CronTrigger ct = new CronTrigger(triggerName, groupName, jobName, groupName, startDate, endDate, expression);
                    shed.scheduleJob(jd, ct);
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }
    public void scheduleAO(AOSchedule schedule, boolean isEdit) {
        try {
            Scheduler shed = getScheduler();
          //  String reportId = "";
            if (shed != null) {
//                if (!isEdit) {
//                    reportId = String.valueOf(schedule.getAOSchedId());
//                }

                String jobName = "";
                String groupName = "";
                String triggerName = "";

                    if(schedule.isIsAO()){
                    jobName = "AOScheduler" + schedule.getAOSchedId();
                    groupName = "AO";
                    triggerName = "Trigger-" + jobName;
                    }else{
                    jobName = "AOScheduler" + schedule.getAOSchedId();
                    groupName = "AO";
                    triggerName = "Trigger-" + jobName;
}
               

                //Check if the job already exists in the scheduler. If present, delete the job
                try {
                    JobDetail tempJd = shed.getJobDetail(jobName, groupName);
                    if (tempJd != null) {
                        shed.deleteJob(jobName, groupName);
                    }
                } catch (SchedulerException e) {
                }

                JobDetail jd = new JobDetail(jobName, groupName, AOSchedulerJob.class);

                jd.getJobDataMap().put("scheduleAO", schedule);
                String frequency = schedule.getFrequency();
                Date startDate = schedule.getStartDate();
                Date endDate = schedule.getEndDate();

                Date today = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
                // 
                if (today.after(startDate) && today.before(new Date(formatter.format(endDate.getTime() + MILLIS_IN_DAY)))) {
                    String expression = "";
                    String[] t = schedule.getScheduledTime().split(":");
                    if (frequency.equalsIgnoreCase("Daily")) {
                        expression = "0 " + t[1] + " " + t[0] + " * * ?";
                    } else if (frequency.equalsIgnoreCase("Weekly")) {
                        String particularDay = schedule.getParticularDay();
                        // String particularDay="B";
                        if (particularDay != null) {
                            expression = "0 " + t[1] + " " + t[0] + " ? * " + Integer.parseInt(particularDay);
                        } else if ("B".equalsIgnoreCase(particularDay)) {
                            expression = "0 " + t[1] + " " + t[0] + " ? * 1";
                        } else {
                            expression = "0 " + t[1] + " " + t[0] + " ? * 2";
                        }
                    } else if (frequency.equalsIgnoreCase("Hourly")) {
                        String particularDate = schedule.getParticularDay();
                        if (particularDate != null) {
                            expression = "0 " + t[1] + " " + t[0] + "/" + Integer.parseInt(particularDate) + " * * ? *";
                        }
                    } else {
//                        
                        String particularDate = schedule.getParticularDay();
//                        
                        if ("L".equalsIgnoreCase(particularDate)) {
                            expression = "0 " + t[1] + " " + t[0] + " ? * 6L";  //0 0 0 L * ? is not supported by the scheduler for last day of the month.
                        } //So providing a hack by using last friday of the month.
                        else if ("B".equalsIgnoreCase(particularDate)) {
                            expression = "0 " + t[1] + " " + t[0] + " 1 * ?";
                        } else if (particularDate != null) {
                            expression = "0 " + t[1] + " " + t[0] + " " + Integer.parseInt(particularDate) + " * ?";
                        } else {
                            expression = "0 " + t[1] + " " + t[0] + " 1 * ?";
                        }
                        //expression = "0 " + t[1] + " " + t[0] + " " +particularDate+" * ?";
                    }


                    CronExpression cex = new CronExpression(expression);
                    CronTrigger ct = new CronTrigger(triggerName, groupName, jobName, groupName, startDate, endDate, expression);
                    shed.scheduleJob(jd, ct);
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }
}
