/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler;

/**
 *
 * @author ProGen
 */
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.connection.ConnectionMetadata;
import com.progen.db.PbBaseDAO;
import com.progen.reportview.db.PbReportViewerDAO;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import prg.db.PbReturnObject;
import prg.util.PbEmailConfig;
import utils.db.ProgenConnection;

public class reportScheduleLoading implements ServletContextListener {

    public static Logger logger = Logger.getLogger(reportScheduleLoading.class);

    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        // 
        SchedulerBD scheduleObj = new SchedulerBD();
        Scheduler scheduler = scheduleObj.getScheduler();
//        scheduler.clear();
        PbBaseDAO dao = new PbBaseDAO();
        dao.killAllsessions();
        // 
    }

    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        //  

        Properties connProps = new Properties();
        java.io.InputStream servletStream = contextEvent.getServletContext().getResourceAsStream("/WEB-INF/MetadataConn.xml");
        ConnectionMetadata metadata = null;
        PbReportViewerDAO pbrv = new PbReportViewerDAO();
        PbReturnObject returnObj = new PbReturnObject();
        //ProgenConnection.connProps = ;//
        try {
            if (servletStream != null) {
                connProps.loadFromXML(servletStream);
                metadata = new ConnectionMetadata(connProps);
                ProgenConnection.setConnectionMetadata(metadata);
            }

            //added by Mohit
            servletStream = contextEvent.getServletContext().getResourceAsStream("/WEB-INF/classes/cache.ccf");
            Properties fileProps = new Properties();
            if (servletStream != null) {
                try {
                    fileProps.load(servletStream);
                    pbrv.FilePath = fileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath");
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }

            }
            //added by Nazneen

            returnObj = pbrv.getEmailConfigDetails();
            if (returnObj.getRowCount() > 0) {
                PbEmailConfig emailConfigObj = PbEmailConfig.getPbEmailConfig();

                if (emailConfigObj == null) {
                    PbEmailConfig.createEmailConfigfrmDB(returnObj);
                }
            }
//               PbEmailConfig emailConfig = PbEmailConfig.getPbEmailConfig();
//               if (emailConfig == null){
//                    Properties emailProps=new Properties();
//                    servletStream = contextEvent.getServletContext().getResourceAsStream("/WEB-INF/EmailConfig.xml");
//
//                    if( servletStream != null )
//                    {
//                        try {
//                        emailProps.loadFromXML(servletStream);
//                        PbEmailConfig.createEmailConfig(emailProps);
//                        }
//                        catch(Exception e)
//                        {
//                            logger.error("Exception:",e);
//                        }
//                    }
//                }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        PbReportViewerDAO dao = new PbReportViewerDAO();
        try {
            SchedulerBD objPlugin = new SchedulerBD();
            // code modified by Amar
            //PbReturnObject retobj=dao.getAllSchedulerdetails();
            PbReturnObject retobj = dao.getAllSchedulerdetailsWithoutETL();
            //end of code mofification by Amar
            ReportSchedule schedule = null;
            Date today = new Date();
            if (retobj != null && retobj.getRowCount() > 0) {
                for (int i = 0; i < retobj.getRowCount(); i++) {
                    Gson json = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<ReportSchedule>>() {
                    }.getType();
                    List<ReportSchedule> scheduleList = json.fromJson(retobj.getFieldValueString(i, "SCHEDULER_DETAILS"), type);
                    Date sdate = retobj.getFieldValueDate(i, "SCHEDULE_START_DATE");
                    Date edate = retobj.getFieldValueDate(i, "SCHEDULE_END_DATE");
                    if (scheduleList != null && !scheduleList.isEmpty()) {
                        for (ReportSchedule schedule1 : scheduleList) {
                            schedule = schedule1;
                            schedule.setStartDate(sdate);
                            schedule.setEndDate(edate);
                        }
                    }
                    //  schedule=json.fromJson(retobj.getFieldValueString(i,14), ReportSchedule.class);

                    if (today.before(edate)) {
                        objPlugin.scheduleReport(schedule, true);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        try {
            //Started By Nazneen
            System.out.println("Schedulling ETL Load");
            SchedulerBD objPlug = new SchedulerBD();
            PbReportViewerDAO rvDAO = new PbReportViewerDAO();
            PbReturnObject retObj1 = new PbReturnObject();
            retObj1 = rvDAO.getAllSchedulerLoadDetails();
            ReportSchedule scheduleLoad;// = new ReportSchedule();changed by mohit for multiple schedulers
            Date todays = new Date();
            if (retObj1 != null && retObj1.getRowCount() > 0) {
                for (int i = 0; i < retObj1.getRowCount(); i++) {
                    scheduleLoad = new ReportSchedule();
                    int loadId = retObj1.getFieldValueInt(i, 0);
                    Date loadStDate = retObj1.getFieldValueDate(i, 4);
                    Date loadEndDate = retObj1.getFieldValueDate(i, 5);
                    String truncTable = retObj1.getFieldValueString(i, 3);
                    String loadName = retObj1.getFieldValueString(i, 1);
                    String loadType = retObj1.getFieldValueString(i, 2);
                    String loadTime = retObj1.getFieldValueString(i, 6);
                    String frequency = retObj1.getFieldValueString(i, 7);
                    String emailId = retObj1.getFieldValueString(i, 8);
                    String PARTICULAR_DAY = retObj1.getFieldValueString(i, 9);//added by mohit
                    scheduleLoad.setSchedulerLoadId(loadId);
                    scheduleLoad.setSchedulerLoadName(loadName);
                    scheduleLoad.setSchedulerLoadType(loadType);
                    scheduleLoad.setSchedulerLoadTruncType(truncTable);
                    scheduleLoad.setSchedulerLoadTime(loadTime);
                    scheduleLoad.setSchedulerLoadFrequency(frequency);
                    scheduleLoad.setSchedulerLoadStartDate(loadStDate);
                    scheduleLoad.setSchedulerLoadEndDate(loadEndDate);
                    scheduleLoad.setSchedulerLoadEmailId(emailId);
                    scheduleLoad.setParticularDay(PARTICULAR_DAY);
                    if (todays.before(loadEndDate)) {
                        objPlug.scheduleLoad(scheduleLoad, true);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        //For Oneview Measure Alerts

        try {
            SchedulerBD onewSchebd = new SchedulerBD();
            PbReportViewerDAO repOnewDao = new PbReportViewerDAO();
            PbReturnObject oneviewRepot = repOnewDao.getAllOneviewMeasuresAlerts();
            ReportSchedule oneviewReSche = null;
            Date today = new Date();
            if (oneviewRepot != null && oneviewRepot.getRowCount() > 0) {
                for (int i = 0; i < oneviewRepot.getRowCount(); i++) {
                    Gson json = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<ReportSchedule>>() {
                    }.getType();
                    List<ReportSchedule> scheduleList = json.fromJson(oneviewRepot.getFieldValueString(i, "ALERTS_DETAILS"), type);
                    Date sdate = oneviewRepot.getFieldValueDate(i, "ALERT_START_DATE");
                    Date edate = oneviewRepot.getFieldValueDate(i, "ALERT_END_DATE");
                    if (scheduleList != null && !scheduleList.isEmpty()) {
                        for (ReportSchedule schedule12 : scheduleList) {
                            oneviewReSche = schedule12;
                            oneviewReSche.setStartDate(sdate);
                            oneviewReSche.setEndDate(edate);
                        }
                    }
                    if (today.before(edate)) {
                        onewSchebd.scheduleReport(oneviewReSche, true);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        try {
            SchedulerBD refreshSchebd = new SchedulerBD();
            PbReturnObject refreshretobj = dao.getAllQuickRefreshReports();
            ReportSchedule refreshSchedule = null;
            Date today = new Date();
            if (refreshretobj != null && refreshretobj.getRowCount() > 0) {
                for (int i = 0; i < refreshretobj.getRowCount(); i++) {
                    Gson json = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<ReportSchedule>>() {
                    }.getType();
                    List<ReportSchedule> scheduleList = json.fromJson(refreshretobj.getFieldValueString(i, "SCHEDULER_DETAILS"), type);
                    Date sdate = refreshretobj.getFieldValueDate(i, "SCHEDULE_START_DATE");
                    Date edate = refreshretobj.getFieldValueDate(i, "SCHEDULE_END_DATE");
                    if (scheduleList != null && !scheduleList.isEmpty()) {
                        for (ReportSchedule schedule12 : scheduleList) {
                            refreshSchedule = schedule12;
                            refreshSchedule.setStartDate(sdate);
                            refreshSchedule.setEndDate(edate);
                        }
                    }
                    if (today.before(edate)) {
                        refreshSchebd.scheduleReport(refreshSchedule, true);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        //added by Dinanath
        try {
            PbReportViewerDAO every5minute = new PbReportViewerDAO();
            String status = every5minute.callingThisAtEveryXminute(5);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        //added by bhargavi
        try {
            SchedulerBD SchedulerBDobj = new SchedulerBD();
            PbReturnObject retobj = dao.getAllAOSchedulerdetails();
            AOSchedule aoSchedule = new AOSchedule();
            Date today = new Date();
            if (retobj != null && retobj.getRowCount() > 0) {

                for (int i = 0; i < retobj.getRowCount(); i++) {
                    Date sdate = retobj.getFieldValueDate(i, "SCHEDULE_START_DATE");
                    Date edate = retobj.getFieldValueDate(i, "SCHEDULE_END_DATE");
                    aoSchedule.setStartDate(sdate);
                    aoSchedule.setEndDate(edate);
                    aoSchedule.setAOSchedName(retobj.getFieldValueString(i, "AO_NAME"));
                    aoSchedule.setLoadType(retobj.getFieldValueString(i, "SCHEDULE_LOAD_TYPE"));
                    aoSchedule.setAOSchedId(retobj.getFieldValueString(i, "AO_ID"));
                    aoSchedule.setDateType(retobj.getFieldValueString(i, "LOAD_DATE_TYPE"));
                    aoSchedule.setloadStartDate(retobj.getFieldValueString(i, "LOAD_START_DATE"));
                    aoSchedule.setloadEndDate(retobj.getFieldValueString(i, "LOAD_END_DATE"));
                    aoSchedule.setMailIds(retobj.getFieldValueString(i, "EMAIL_IDS"));
                    aoSchedule.setFrequency(retobj.getFieldValueString(i, "SCHEDULE_FREQUENCY"));
                    aoSchedule.setScheduledTime(retobj.getFieldValueString(i, "SCHEDULE_TIME"));
                    if (today.before(edate)) {
                        SchedulerBDobj.scheduleAO(aoSchedule, true);
                    }
                }

            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        //code added by Dinanath Parit
        try {
            SchedulerBD objPlugin = new SchedulerBD();
            // code modified by Amar
            //PbReturnObject retobj=dao.getAllSchedulerdetails();
            PbReturnObject retobj = dao.getDailyKPIChartScheduler();
            //end of code mofification by Amar
            KPIAlertSchedule schedule = null;
            Date today = new Date();
            if (retobj != null && retobj.getRowCount() > 0) {
                for (int i = 0; i < retobj.getRowCount(); i++) {
                    Gson json = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<KPIAlertSchedule>>() {
                    }.getType();
                    List<KPIAlertSchedule> scheduleList = json.fromJson(retobj.getFieldValueString(i, "SCHEDULER_DETAILS"), type);
                    Date sdate = retobj.getFieldValueDate(i, "SCHEDULE_START_DATE");
                    Date edate = retobj.getFieldValueDate(i, "SCHEDULE_END_DATE");
                    if (scheduleList != null && !scheduleList.isEmpty()) {
                        for (KPIAlertSchedule schedule1 : scheduleList) {
                            schedule = schedule1;
                            schedule.setStartDate(sdate);
                            schedule.setEndDate(edate);
    }
}
                    //  schedule=json.fromJson(retobj.getFieldValueString(i,14), ReportSchedule.class);

                    if (today.before(edate)) {
                        objPlugin.scheduleKPIChart(schedule, true);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }
}
