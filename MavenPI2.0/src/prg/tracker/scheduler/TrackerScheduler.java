package prg.tracker.scheduler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class TrackerScheduler {

    public static Logger logger = Logger.getLogger(TrackerScheduler.class);
    private String reportId, userId, trackerId, stdate, endate, stime1, smonth, sdate, frequency, EorM;
    private static Scheduler shed;
    private static SchedulerFactory sf;
    private JobDetail jd;
    //private HttpServletResponse response;

    public TrackerScheduler() throws Exception {
    }

    public TrackerScheduler(String reportId, String trackerId, String stdate, String endate, String stime1, String smonth, String sdate, String frequency, String EorM) {
        this.reportId = reportId;
        this.trackerId = trackerId;
        this.stdate = stdate;
        this.endate = endate;
        this.frequency = frequency;
        this.stime1 = stime1;
        this.smonth = smonth;
        this.sdate = sdate;
        this.EorM = EorM;
    }

    public void trackersendMail() throws Exception {
        try {
            /*
             * this is the code to trigger immediately sf = new
             * StdSchedulerFactory(); shed = sf.getScheduler(); jd = new
             * JobDetail("newJobtracker", "demogrouptracker", TrackerJob.class);
             * jd.getJobDataMap().put("reportId", reportId);
             * jd.getJobDataMap().put("userId", userId);
             * jd.getJobDataMap().put("trackerId", trackerId); SimpleTrigger st
             * = new SimpleTrigger("simple trigger", "simple group");
             * shed.start(); shed.scheduleJob(jd, st);
             */

            //Correct code
            //////////////////////////////////////////////////////.println.println("frequency----"+frequency);
            if (frequency.equalsIgnoreCase("1")) {
                shed = getScheduler();

                shed.start();
                String name = "newJobtracker" + trackerId;
                String group = "demogrouptracker" + trackerId;
                jd = new JobDetail(name, group, TrackerJob.class);
                jd.getJobDataMap().put("reportId", reportId);
                jd.getJobDataMap().put("userId", userId);
                jd.getJobDataMap().put("trackerId", trackerId);
                long startTime = System.currentTimeMillis() + (30L * 1000L);

                DateFormat formatter;
                Date startDate, endDate;
                formatter = new SimpleDateFormat("yyyy-MM-dd");

                String s[] = stdate.split("/");
                String e[] = endate.split("/");
                String sd = s[2] + "-" + s[0] + "-" + s[1];
                String ed = e[2] + "-" + e[0] + "-" + e[1];
                String stime = stime1;
                startDate = (Date) formatter.parse(sd);
                endDate = (Date) formatter.parse(ed);

                String[] time = stime.split(":");

                String expression = "0 " + time[1] + " " + time[0] + " * * ?";
                //            String expression = "0 0 0 * * ?";
                CronExpression cex = new CronExpression(expression);

                CronTrigger ct = new CronTrigger("simple trigger" + trackerId, "simple group" + trackerId, name, group, startDate, endDate, expression);

                shed.scheduleJob(jd, ct);
            } else if (frequency.equals("2")) {
                //This is a "Monthly Trigger" which Triggers every month on the specified date and time
                shed = getScheduler();

                shed.start();
                String name = "newJobtracker" + trackerId;
                String group = "demogrouptracker" + trackerId;
                jd = new JobDetail(name, group, TrackerJob.class);
                jd.getJobDataMap().put("reportId", reportId);
                jd.getJobDataMap().put("userId", userId);
                jd.getJobDataMap().put("trackerId", trackerId);

                long startTime = System.currentTimeMillis() + (30L * 1000L);
                DateFormat formatter;
                Date startDate, endDate;
                formatter = new SimpleDateFormat("yyyy-MM-dd");
                String s[] = stdate.split("/");
                String e[] = endate.split("/");
                String sd = s[2] + "-" + s[0] + "-" + s[1];
                String ed = e[2] + "-" + e[0] + "-" + e[1];
                String stime = stime1;
                startDate = (Date) formatter.parse(sd);
                endDate = (Date) formatter.parse(ed);


                String[] time = stime.split(":");
                String expression = "0 0 19 " + sdate + " * ?";

                CronExpression cex = new CronExpression(expression);
                CronTrigger ct = new CronTrigger("name" + trackerId, "group" + trackerId, name, group, startDate, endDate, expression);
                shed.scheduleJob(jd, ct);
            } else if (frequency.equals("3")) {
                shed = getScheduler();

                shed.start();
                String name = "newJobtracker" + trackerId;
                String group = "demogrouptracker" + trackerId;
                jd = new JobDetail(name, group, TrackerJob.class);
                jd.getJobDataMap().put("reportId", reportId);
                jd.getJobDataMap().put("trackerId", trackerId);
                long startTime = System.currentTimeMillis() + (30L * 1000L);
                DateFormat formatter;
                Date startDate, endDate;
                formatter = new SimpleDateFormat("yyyy-MM-dd");

                String s[] = stdate.split("/");
                String e[] = endate.split("/");
                String sd = s[2] + "-" + s[0] + "-" + s[1];
                String ed = e[2] + "-" + e[0] + "-" + e[1];
                String stime = stime1;
                startDate = (Date) formatter.parse(sd);
                endDate = (Date) formatter.parse(ed);


                String[] time = stime.split(":");
                String expression = "0 0 19 L 0/4 ?";
                CronExpression cex = new CronExpression(expression);
                //CronTrigger ct = new CronTrigger("name" + alertid, "group" + alertid,"newJob" + alertid, "demogroup" + alertid, startDate, endDate, expression);
                CronTrigger ct = new CronTrigger("name" + trackerId, "group" + trackerId, name, group, startDate, endDate, expression);

                shed.scheduleJob(jd, ct);
            } else if (frequency.equals("4")) {
                //This is an "Yearly Trigger" which Triggers every year on the specified month date and time
                shed = getScheduler();

                shed.start();
                String name = "newJobtracker" + trackerId;
                String group = "demogrouptracker" + trackerId;
                jd = new JobDetail(name, group, TrackerJob.class);
                jd.getJobDataMap().put("reportId", reportId);
                jd.getJobDataMap().put("trackerId", trackerId);
                long startTime = System.currentTimeMillis() + (30L * 1000L);
                DateFormat formatter;
                Date startDate, endDate;
                formatter = new SimpleDateFormat("yyyy-MM-dd");

                String s[] = stdate.split("/");
                String e[] = endate.split("/");
                String sd = s[2] + "-" + s[0] + "-" + s[1];
                String ed = e[2] + "-" + e[0] + "-" + e[1];
                String stime = stime1;
                startDate = (Date) formatter.parse(sd);
                endDate = (Date) formatter.parse(ed);

                String[] time = stime.split(":");
                String sdate1 = "1";
                String expression = "0 0 19 " + sdate1 + " " + smonth + " ?";
                CronExpression cex = new CronExpression(expression);
                CronTrigger ct = new CronTrigger("name" + trackerId, "group" + trackerId, name, group, startDate, endDate, expression);
                shed.scheduleJob(jd, ct);
            }

        } catch (Exception e) {//
            logger.error("Exception: ", e);
        }

    }

    public static Scheduler getScheduler() throws SchedulerException {
        try {
            if (shed == null) {
                shed = StdSchedulerFactory.getDefaultScheduler();
            }
            return shed;
        } catch (SchedulerException ex) {
            logger.error("Exception: ", ex);
            return shed;
        }
    }

    public void shutDown(String name, String group) {
        try {
            shed = StdSchedulerFactory.getDefaultScheduler();
            String[] jobGroupNames = shed.getJobGroupNames();
            for (int i = 0; i < jobGroupNames.length; i++) {
                String[] jobNames = shed.getJobNames(jobGroupNames[i]);
                for (int j = 0; j < jobNames.length; j++) {
                }
            }

            shed.deleteJob(name, group);
            String[] jobNames1 = shed.getJobGroupNames();
            for (int i = 0; i < jobNames1.length; i++) {
                String[] jobNames = shed.getJobNames(jobNames1[i]);
                for (int j = 0; j < jobNames.length; j++) {
                }
            }
        } catch (SchedulerException ex) {
            logger.error("Exception: ", ex);
        }
    }
}