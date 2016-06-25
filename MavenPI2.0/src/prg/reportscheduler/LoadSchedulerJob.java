package prg.reportscheduler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.execution.EtlLoadForJKP;
import com.progen.execution.ProgramExecusion;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.reportview.db.ProgenReportViewerDAO;
import com.progen.scheduler.ReportSchedule;
import com.progen.scheduler.ScheduleLogger;
import com.progen.scheduler.SchedulerBD;
import com.progen.studio.StudioAction;
import com.progen.userlayer.db.DeEncrypter;
import com.progen.userlayer.db.LogReadWriter;
import com.progen.xml.UploadingXmlIntoDatabase;
import com.uploadfile.excel.FTPReadCSVFile;
import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.SourceConn;
import prg.util.PbMail;
import prg.util.PbMailParams;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import utils.db.ProgenConnection;
//import com.progen.report.params.PrgReportParams;
//import com.progen.metadata.*;

public class LoadSchedulerJob implements Job {

    public static Logger logger = Logger.getLogger(LoadSchedulerJob.class);
    String connName = "";
    String userName = "";
    String password = "";
    String option = "false";
    PbMailParams params = null;
    PbMail mailer = null;

    @Override
    public void execute(JobExecutionContext jec) {

        JobDataMap dataMap = jec.getJobDetail().getJobDataMap();
        ReportSchedule schedule = (ReportSchedule) dataMap.get("scheduleLoad");
        String loadType = schedule.getSchedulerLoadType();
        String truncType = schedule.getSchedulerLoadTruncType();
        String loadtime = schedule.getSchedulerLoadTime();
        String[] temp;
//          System.out.println("loadType "+loadType+" truncType "+" loadtime "+loadtime);
        Calendar currentDate = Calendar.getInstance();
        int sysHr = currentDate.get(Calendar.HOUR_OF_DAY);
        int sysMin = currentDate.get(Calendar.MINUTE);

        temp = loadtime.split(":");
        String loadHr = temp[0];
        String loadMin = temp[1];

        if (temp[0].startsWith("0")) {
            loadHr = temp[0].substring(1);
        } else {
            loadHr = temp[0];
        }
        if (temp[1].startsWith("0")) {
            loadMin = temp[1].substring(1);
        } else {
            loadMin = temp[1];
        }

        loadtime = loadHr + ":" + loadMin;
        String sysTime = sysHr + ":" + sysMin;

        if ((!schedule.isRunFlag() && sysTime.equalsIgnoreCase(loadtime))
                || (schedule.getSchedulerLoadFrequency().equalsIgnoreCase("Hourly") && loadMin.equalsIgnoreCase(sysMin + ""))) {
//            
            sendSchedulerStartMail(schedule);
            runSchedulerLoad(schedule, loadType, truncType);
//            sendSchedulerFinishMail(schedule);
        }
    }

    public void runSchedulerLoad(ReportSchedule schedule, String loadType, String truncType) {
        final LogReadWriter logrw = new LogReadWriter();
        final ProgramExecusion proExe = new ProgramExecusion();
        final SourceConn sc = new SourceConn();
        final EtlLoadForJKP eltJkp = new EtlLoadForJKP();
        String idValue = loadType;
        final ReportSchedule s1 = schedule;
//        final Connection connectionSC = sc.getConnection("seaLinkdw", "", "", "","","","","","");
        if (truncType.equalsIgnoreCase("y")) {
            option = "true";
        }
        connName = "";
        userName = "";
        password = "";
        PbReturnObject retObj = null;
        PbDb pbdb = new PbDb();
        String query = "";
        String loadVal = "";

        if (idValue.equals("QuickInit")) {
            logrw.setLogFileName("QuickInit");
            final String option = "true";
            logger.info("truncate QT-->" + option);
            try {
                logrw.fileWriter("truncate QT-->" + option);
            } catch (IOException ex) {
                logger.error("Exception: ", ex);
            }
            final String forceInit = "INIT";
            final String forceReq = "Quick Travel";
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            //changed for CCC
                            PbReturnObject retObj = null;
                            PbDb pbdb = new PbDb();

                            String loadVal = "";
                            String query = "";

                            proExe.truncateQuickTravelStg(option);
                            Connection con = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "quickTravel";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                logrw.fileWriter("query-------------->" + query);
                                logrw.fileWriter("truncate QT-->" + option);
                            } catch (IOException ex) {
                                logger.error("Exception: ", ex);
                            }
                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC
                                    logger.info("compId------>" + compId);
                                    try {
                                        logrw.fileWriter("compId------>" + compId);
                                    } catch (IOException ex) {
                                        logger.error("Exception: ", ex);
                                    }
                                    proExe.runLoad(forceInit, forceReq, compId);
//                                proExe.loadQuickTravel(forceInit, connName, userName, password, option);
                                    proExe.loadQuickTravel(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadQuickTravelInit();
                            sendSchedulerFinishMail(s1, "");
                            logger.info("-----------------------LOAD COMPLETED FOR QT INIT--------------------------");
                            try {
                                logrw.fileWriter("-----------------------LOAD COMPLETED FOR QT INIT--------------------------");
                            } catch (IOException ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (con != null) {
                                try {
                                    con.close();
                                    con = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception: ", ex);
                                }
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("QuickIncr")) {
            logrw.setLogFileName("QuickIncr");
            final String option = "true";

            try {
                logrw.fileWriter("truncate QT-->" + option);
            } catch (IOException ex) {
                logger.error("Exception: ", ex);
            }
            final String forceInit = "INCR";
            final String forceReq = "Quick Travel";
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            //changed for CCC
                            PbReturnObject retObj = null;
                            PbDb pbdb = new PbDb();
                            String loadVal = "";
                            String query = "";
                            proExe.truncateQuickTravelStg(option);
                            Connection con = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "quickTravel";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";
                            logger.info("query-------->" + query);
                            try {
                                logrw.fileWriter("query-------------->" + query);
                                logrw.fileWriter("truncate QT-->" + option);
                            } catch (IOException ex) {
                                logger.error("Exception: ", ex);
                            }
                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC
                                    logger.info("compId------>" + compId);
                                    try {
                                        logrw.fileWriter("compId------>" + compId);
                                    } catch (IOException ex) {
                                        logger.error("Exception: ", ex);
                                    }
                                    proExe.runLoad(forceInit, forceReq, compId);
//                                proExe.loadQuickTravel(forceInit,connName,userName,password,option);
                                    proExe.loadQuickTravel(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadQuickTravelIncr();
//                             logger.info("Running ETL Procedure");
//                            try {
//                                logrw.fileWriter("Running ETL Procedure");
//                            } catch (IOException ex) {
//                                logger.error("Exception: ",ex);
//                            }
//                            CallableStatement proc = null;
//                            try
//                            {
//                                try {
//                                    proc = connectionSC.prepareCall("{ call procForCCCETL1() }");
//                                    proc.execute();
//                                } catch (SQLException ex) {
//                                    logger.error("Exception: ",ex);
//                                }
//                            }
//                            finally
//                            {
//                                try {
//                                    proc.close();
//                                    connectionSC.close();
//                                } catch (SQLException ex) {
//                                    logger.error("Exception: ",ex);
//                                }
//                            }
                            sendSchedulerFinishMail(s1, "");

                            try {
                                logrw.fileWriter("-----------------------LOAD COMPLETED FOR QT INIT--------------------------");
                            } catch (IOException ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (con != null) {
                                try {
                                    con.close();
                                    con = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception: ", ex);
                                }
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("CtInit")) {
            logrw.setLogFileName("CtInit");
            logger.info("truncate CT Server-->" + option);
            try {
                logrw.fileWriter("truncate CT Server-->" + option);
            } catch (IOException ex) {
                logger.error("Exception: ", ex);
            }
            final String forceInit = "INIT";
            final String forceReq = "CT Server";
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            //changed for CCC
                            PbReturnObject retObj = null;
                            PbDb pbdb = new PbDb();
                            String loadVal = "";
                            String query = "";

                            proExe.truncateCtServerStg(option);
                            Connection con = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "ctServer";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                logrw.fileWriter("query-------------->" + query);
                                logrw.fileWriter("truncate QT-->" + option);
                            } catch (IOException ex) {
                                logger.error("Exception: ", ex);
                            }
                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC
                                    logger.info("compId------>" + compId);
                                    try {
                                        logrw.fileWriter("compId------>" + compId);
                                    } catch (IOException ex) {
                                        logger.error("Exception: ", ex);
                                    }
                                    proExe.runLoad(forceInit, forceReq, compId);
//                                proExe.loadCtServer(forceInit,connName,userName,password,option);
                                    proExe.loadCtServer(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadCTServerInit();
                            sendSchedulerFinishMail(s1, "");

                            try {
                                logrw.fileWriter("-----------------------LOAD COMPLETED FOR QT INIT--------------------------");
                            } catch (IOException ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (con != null) {
                                try {
                                    con.close();
                                    con = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception: ", ex);
                                }
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("AccInit")) {
            logrw.setLogFileName("AccInit");
            logger.info("truncate Accpac-->" + option);
            try {
                logrw.fileWriter("truncate Accpac-->" + option);
            } catch (IOException ex) {
                logger.error("Exception: ", ex);
            }
            final String forceInit = "INIT";
            final String forceReq = "Accpac";
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            //changed for CCC
                            PbReturnObject retObj = null;
                            PbDb pbdb = new PbDb();
                            String loadVal = "";
                            String query = "";

                            proExe.truncateAccpacStg(option);
                            Connection con = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "accpac";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                logrw.fileWriter("query-------------->" + query);
                                logrw.fileWriter("truncate QT-->" + option);
                            } catch (IOException ex) {
                                logger.error("Exception: ", ex);
                            }
                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC
                                    logger.info("compId------>" + compId);
                                    proExe.runLoad(forceInit, forceReq, compId);
//                                proExe.loadAccpac(forceInit,connName,userName,password,option);
                                    proExe.loadAccpac(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadAccpacInit();
                            sendSchedulerFinishMail(s1, "");
                            logger.info("-----------------------LOAD COMPLETED FOR ACCPAC INIT--------------------------");
                            try {
                                logrw.fileWriter("-----------------------LOAD COMPLETED FOR ACCPAC INIT--------------------------");
                            } catch (IOException ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (con != null) {
                                try {
                                    con.close();
                                    con = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception: ", ex);
                                }
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("ComInit")) {
            logrw.setLogFileName("ComInit");
            final String optionAcc = option;
            final String optionCt = option;
            final String forceInit = "INIT";
            final String option = "true";
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            //changed for CCC
                            PbReturnObject retObj = null;
                            PbDb pbdb = new PbDb();
                            String loadVal = "";
                            String query = "";
                            String forceReq = "";

                            proExe.truncateQuickTravelStg(option);
                            Connection con = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "quickTravel";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                logrw.fileWriter("query-------------->" + query);
                                logrw.fileWriter("truncate QT-->" + option);
                            } catch (IOException ex) {
                                logger.error("Exception: ", ex);
                            }
                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC
                                    forceReq = "Quick Travel";

                                    logger.info("compId-------------->" + compId);
                                    logger.info("truncate QT-->" + option);
                                    try {
                                        logrw.fileWriter("truncate QT-->" + option);
                                    } catch (IOException ex) {
                                        logger.error("Exception: ", ex);
                                    }
                                    proExe.runLoad(forceInit, forceReq, compId);
//                                proExe.loadQuickTravel(forceInit,connName,userName,password,option);
                                    proExe.loadQuickTravel(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadQuickTravelInit();
                            if (con != null) {
                                try {
                                    con.close();
                                    con = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception: ", ex);
                                }
                            }
                            //CT server INIT Load
                            proExe.truncateCtServerStg(option);
                            Connection con1 = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "ctServer";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                retObj = pbdb.execSelectSQL(query, con1);
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }
                            for (int j = 0; j < retObj.getRowCount(); j++) {
                                final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                final String password = retObj.getFieldValueString(j, "PASSWORD");
                                final String server = retObj.getFieldValueString(j, "SERVER");
                                final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                final String port = retObj.getFieldValueString(j, "PORT");
                                final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                String password1 = DeEncrypter.getInstance().decrypt(password);
                                //end for CCC


                                try {
                                    logrw.fileWriter("truncate CT Server-->" + optionCt);
                                } catch (IOException ex) {
                                    logger.error("Exception: ", ex);
                                }
                                forceReq = "CT Server";
                                proExe.runLoad(forceInit, forceReq, compId);
//                                proExe.loadCtServer(forceInit,connName,userName,password,optionCt);
                                proExe.loadCtServer(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                            }
                            proExe.loadCTServerInit();
                            if (con1 != null) {
                                try {
                                    con1.close();
                                    con1 = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception: ", ex);
                                }
                            }
                            //changed for CCC
                            //Accpac INIT Load
                            proExe.truncateAccpacStg(option);
                            Connection con2 = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "accpac";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                logrw.fileWriter("query-------------->" + query);
                                logrw.fileWriter("truncate QT-->" + option);
                            } catch (IOException ex) {
                                logger.error("Exception: ", ex);
                            }
                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC


                                    try {
                                        logrw.fileWriter("compId-------------->" + compId);
                                        logrw.fileWriter("truncate Accpac-->" + optionAcc);
                                    } catch (IOException ex) {
                                        logger.error("Exception: ", ex);
                                    }
                                    forceReq = "Accpac";
                                    proExe.runLoad(forceInit, forceReq, compId);
//                                proExe.loadAccpac(forceInit,connName,userName,password,optionAcc);
                                    proExe.loadAccpac(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadAccpacInit();
                            if (con2 != null) {
                                try {
                                    con2.close();
                                    con2 = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception: ", ex);
                                }
                            }
                            sendSchedulerFinishMail(s1, "");

                            try {
                                logrw.fileWriter("-----------------------LOAD COMPLETED FOR COMBINED INIT--------------------------");
                            } catch (IOException ex) {
                                logger.error("Exception: ", ex);
                            }

                            try {
                                con.close();
                                con = null;
                            } catch (SQLException ex) {
                                logger.error("Exception: ", ex);
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("ComIncr")) {
            logrw.setLogFileName("ComIncr");
            final String optionAcc = option;
            final String optionCt = option;
            final String option = "true";

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            String forceInit = "";
                            String forceReq = "";
                            //changed for CCC
                            PbReturnObject retObj = null;
                            PbDb pbdb = new PbDb();
                            String loadVal = "";
                            String query = "";

                            //QT INCR Load
                            proExe.truncateQuickTravelStg(option);
                            loadVal = "quickTravel";
                            Connection con = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                logrw.fileWriter("query-------------->" + query);
                            } catch (IOException ex) {
                                logger.error("Exception: ", ex);
                            }
                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC


                                    try {
                                        logrw.fileWriter("compId-------------->" + compId);
                                        logrw.fileWriter("truncate QT-->" + option);
                                    } catch (IOException ex) {
                                        logger.error("Exception: ", ex);
                                    }
                                    forceInit = "INCR";
                                    forceReq = "Quick Travel";
                                    proExe.runLoad(forceInit, forceReq, compId);
//                                proExe.loadQuickTravel(forceInit,connName,userName,password,option);
                                    proExe.loadQuickTravel(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadQuickTravelIncr();
                            if (con != null) {
                                try {
                                    con.close();
                                    con = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception: ", ex);
                                }
                            }
//                             
//                            try {
//                                logrw.fileWriter("Running ETL Procedure");
//                            } catch (IOException ex) {
//                                logger.error("Exception: ",ex);
//                            }
//                            CallableStatement proc = null;
//                            try
//                            {
//                                try {
//                                    proc = connectionSC.prepareCall("{ call procForCCCETL1() }");
//                                    proc.execute();
//                                } catch (SQLException ex) {
//                                    logger.error("Exception: ",ex);
//                                }
//                            }
//                            finally
//                            {
//                                try {
//                                    proc.close();
//                                    connectionSC.close();
//                                } catch (SQLException ex) {
//                                    logger.error("Exception: ",ex);
//                                }
//                            }
                            //changed for CCC
                            //CT Server INIT Load
                            proExe.truncateCtServerStg(option);
                            Connection con1 = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "ctServer";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                logrw.fileWriter("query-------------->" + query);
                                logrw.fileWriter("truncate QT-->" + option);
                            } catch (IOException ex) {
                                logger.error("Exception: ", ex);
                            }
                            try {
                                retObj = pbdb.execSelectSQL(query, con1);
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC


                                    try {
                                        logrw.fileWriter("compId-------------->" + compId);
                                        logrw.fileWriter("truncate CT Server-->" + optionCt);
                                    } catch (IOException ex) {
                                        logger.error("Exception: ", ex);
                                    }
                                    forceInit = "INIT";
                                    forceReq = "CT Server";
                                    proExe.runLoad(forceInit, forceReq, compId);
//                                proExe.loadCtServer(forceInit,connName,userName,password,optionCt);
                                    proExe.loadCtServer(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadCTServerInit();
                            if (con1 != null) {
                                try {
                                    con1.close();
                                    con1 = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception: ", ex);
                                }
                            }

                            //changed for CCC
                            //Accpac INIT Load
                            proExe.truncateAccpacStg(option);
                            Connection con2 = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "accpac";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                logrw.fileWriter("query-------------->" + query);
                                logrw.fileWriter("truncate QT-->" + option);
                            } catch (IOException ex) {
                                logger.error("Exception: ", ex);
                            }
                            try {
                                retObj = pbdb.execSelectSQL(query, con2);
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC


                                    try {
                                        logrw.fileWriter("compId-------------->" + compId);
                                        logrw.fileWriter("truncate Accpac-->" + optionAcc);
                                    } catch (IOException ex) {
                                        logger.error("Exception: ", ex);
                                    }
                                    forceReq = "Accpac";
                                    proExe.runLoad(forceInit, forceReq, compId);
//                                proExe.loadAccpac(forceInit,connName,userName,password,optionAcc);
                                    proExe.loadAccpac(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadAccpacInit();
                            if (con2 != null) {
                                try {
                                    con2.close();
                                    con2 = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception: ", ex);
                                }
                            }
                            sendSchedulerFinishMail(s1, "");

                            try {
                                logrw.fileWriter("-----------------------LOAD COMPLETED FOR COMBINED INCR--------------------------");
                            } catch (IOException ex) {
                                logger.error("Exception: ", ex);
                            }
                        }
                    },
                    10000);
        } //Start of code by Nazneen For JK Papers
        else if (idValue.equals("JKPLoad")) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            String todaysDate = dateFormat.format(date);
                            Date d1 = null;
                            Date d2 = null;
                            Date d3 = null;
                            Date d4 = null;
                            int yearDiffd1 = 0;
                            int yearDiffd2 = 0;
                            String finalStartDate = "";
                            String finalEndDate = "";
                            final SourceConn sc = new SourceConn();
                            final EtlLoadForJKP eltJkp = new EtlLoadForJKP();
                            CallableStatement proc = null;
                            //call of truncate procedure
                            Connection con = null;
                            con = sc.getConnection("jkdwh", "", "", "", "", "", "", "", "");
                            PbReturnObject retObj = null;
                            PbDb pbdb = new PbDb();

                            String query = "SELECT TAB_NAME,LAST_UPDATE_DATE from PRG_LOAD_TRACKER_MASTER";

                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                                if (retObj != null && retObj.getRowCount() > 0) {

                                    Connection con1 = null;
                                    con1 = sc.getConnection("jkdwh", "", "", "", "", "", "", "", "");
                                    proc = con1.prepareCall("{ call truncate_table_proc() }");
                                    proc.execute();
                                    for (int i = 0; i < retObj.getRowCount(); i++) {
                                        String tableName = retObj.getFieldValueString(i, 0);
                                        String startDate = retObj.getFieldValueString(i, 1);


                                        Date FromDate = dateFormat1.parse(startDate);


                                        startDate = dateFormat.format(FromDate);
                                        d1 = dateFormat.parse(todaysDate);
                                        yearDiffd2 = d1.getYear();
                                        int yrFinal = yearDiffd2 + 1900;
                                        String finalTodayDate = String.valueOf(yrFinal) + "-01-01"; //years start date

                                        d2 = dateFormat.parse(finalTodayDate);
                                        d3 = dateFormat.parse(todaysDate);
                                        d4 = dateFormat.parse(startDate);
                                        long diff1 = d3.getTime() - d2.getTime();
                                        long diff2 = d4.getTime() - d2.getTime();
                                        long diffDays1 = diff1 / (24 * 60 * 60 * 1000);
                                        long diffDays2 = diff2 / (24 * 60 * 60 * 1000);

                                        yearDiffd1 = d3.getYear();
                                        yearDiffd2 = d4.getYear();

                                        String daysDiff1 = String.valueOf(diffDays1 + 1);
                                        String daysDiff2 = String.valueOf(diffDays2 + 1);
                                        if (daysDiff1.length() == 2) {
                                            daysDiff1 = "0" + daysDiff1;
                                        } else if (daysDiff1.length() == 1) {
                                            daysDiff1 = "00" + daysDiff1;
                                        }
                                        if (daysDiff2.length() == 2) {
                                            daysDiff2 = "0" + daysDiff2;
                                        } else if (daysDiff2.length() == 1) {
                                            daysDiff2 = "00" + daysDiff2;
                                        }
                                        finalStartDate = String.valueOf(yearDiffd2) + "" + daysDiff2;
                                        finalEndDate = String.valueOf(yearDiffd1) + "" + daysDiff1;



                                        eltJkp.runLoadForJKP(tableName, finalStartDate, finalEndDate, todaysDate);

                                    }
                                    con = null;
                                    con = sc.getConnection("jkdwh", "", "", "", "", "", "", "", "");

                                    //calling ending procedures


                                    proc = con.prepareCall("{ call Date_Conversion_proc() }");
                                    proc.execute();
                                    proc = null;


                                    proc = con.prepareCall("{ call Merge_Load() }");
                                    proc.execute();
                                    proc = null;


                                    proc = con.prepareCall("{ call PRG_BUSINESS_UNIT_DIM_proc() }");
                                    proc.execute();
                                    proc = null;


                                    proc = con.prepareCall("{ call prg_item_dim_proc() }");
                                    proc.execute();
                                    proc = null;


                                    proc = con.prepareCall("{ call prg_parent_to_dim_proc() }");
                                    proc.execute();
                                    proc = null;


                                    proc = con.prepareCall("{ call prg_sales_delivery_fact_f4211() }");
                                    proc.execute();
                                    proc = null;
                                    //parametirized procedure


                                    String insertStoreProc = "{call prg_sales_delivery_fact_f42119(?,?)}";
                                    proc = con.prepareCall(insertStoreProc);
                                    proc.setInt(1, Integer.parseInt(finalStartDate));
                                    proc.setInt(2, Integer.parseInt(finalEndDate));
                                    proc.execute();
                                    proc = null;


                                    proc = con.prepareCall("{ call prg_outstanding_final_fact() }");
                                    proc.execute();
                                    proc = null;


                                    proc = con.prepareCall("{ call Inventory_LOAD() }");
                                    proc.execute();
                                    proc = null;


                                }
                                if (con != null) {
                                    con.close();
                                }
                                sendSchedulerFinishMail(s1, "");

                            } catch (Exception ex) {
                                sendSchedulerConnError("JKP LOAD ERROR");

                                logger.error("Exception: ", ex);
                            }
                        }
                    },
                    10000);
        } //End of code by Nazneen For JK Papers
        //Start of code by Mohit for Dataflow
        else if (idValue.equals("DFLoad")) {
//   logrw.setLogFileName("DataFlow_Load");
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
//                            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-dd-MM");
                            Date date = new Date();
                            String todaysDate = dateFormat.format(date);

                            final SourceConn sc = new SourceConn();
                            final EtlLoadForJKP eltJkp = new EtlLoadForJKP();
                            Connection con = null;
                            con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                            CallableStatement proc = null;
                            PbReturnObject retObj = null;
                            String tableName = "", startDate = "";
                            Format formatter = new SimpleDateFormat("dd-MMM-yy");
                            date = new Date();
                            String s = formatter.format(date);
                            String Filename = "DataFlow_Load" + s;
                            PbDb pbdb = new PbDb();
//                              logger.info("con----------" + con.toString());
                            String query = "SELECT TAB_NAME,LAST_UPDATE_DATE from prg_load_tracker_master where  tab_name not in ('TICKET_CRMUSER_LOCATION','Ticket_Data','dvs_personal','dvs_modeofverification')";
                            String truntable[] = {"dvs_stg", "workitem_stg", "dvs_employment_stg", "documentverficationsystem_stg", "dvs_health_license_stg", "dvs_education_stg", "dvs_case_stg", "dvs_user_stg", "dvs_issuing_authority_stg", "processinstance_stg"};

//                            logger.info("query-----------" + query);
                            try {
                                eltJkp.setEtlStartedDate();//added by Dinanath

                                logrw.fileWriterWithFileName("---------------------------------------------Data Flow ETL Scheduler " + s + " --------------------------------------------------", Filename);
//
//                                  date = new Date();
                                retObj = pbdb.execSelectSQL(query, con);
                                if (retObj != null && retObj.getRowCount() > 0) {
                                    logger.info(":::::RUNNING TRUNCATES::::");
                                    date = new Date();
                                    logrw.fileWriterWithFileName("---------------------Truncation Started-----------------------@" + date + "\n", Filename);
                                    for (int i = 0; i < truntable.length; i++) {
                                        con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                                        logger.info("TRUNCATE TABLE " + truntable[i] + ";");
                                        int j = pbdb.execUpdateSQL("TRUNCATE TABLE " + truntable[i] + ";", con);
                                        logrw.fileWriterWithFileName("(" + (i + 1) + ") TRUNCATE TABLE " + truntable[i], Filename);
                                    }
                                    logrw.fileWriterWithFileName("---------------------Truncation Completed-----------------------\n", Filename);
                                    for (int i = 0; i < retObj.getRowCount(); i++) {
                                        tableName = retObj.getFieldValueString(i, 0);
                                        startDate = retObj.getFieldValueString(i, 1);
                                        date = new Date();
                                        logger.info("eltJkp.runLoadForDataflow...start.." + tableName + "...." + date);
                                        eltJkp.runLoadForDataflow(tableName, startDate, todaysDate, false, Filename);
                                        //updates in fact
                                    }
                                    logger.info(":::::COMPLETED INSERTING IN TABLES::::");
                                    logrw.fileWriterWithFileName("---------------------Insertion Completed -----------------------\n", Filename);
                                    //calling ending procedures
                                    date = new Date();
                                    logger.info(":::::CALLING ENDING PROCEDURES::::" + date);
                                    logrw.fileWriterWithFileName("---------------------Calling Procedures-----------------------@" + date + "\n", Filename);
                                    con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                                    proc = con.prepareCall("{ call dataflow.neeraj_all() }");
                                    proc.execute();
                                    if (con != null) {
                                        con.close();

                                    }
                                }
                                date = new Date();
                                logrw.fileWriterWithFileName(":::::LOAD COMPLETED SUCCESSFULLY::::@" + date.toString() + "\n", Filename);
                                logger.info(":::::ETL LOAD COMPLETED SUCCESSFULLY::::@" + date.toString());
                                eltJkp.setEtlCompletedDate();//added by Dinanath
                                sendSchedulerFinishMail(s1, Filename);

                                scheduleJobAfterEtlCompleted(); //Added by Amar
                            } catch (Exception ex) {
                                try {
                                    logrw.fileWriterWithFileName("DATA FLOW LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                                } catch (IOException ex1) {
                                    logger.error("Exception: ", ex1);
                                }
                                sendSchedulerEtlFailedMail(s1, "DATA FLOW LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                                delayReportSchedulerMailIfEtlFailed();
                                eltJkp.setEtlFailedDate();//added by Dinanath
                                logger.error(":::::ETL ERROOOOOORRRRRRRRRRRRR::::: ", ex);
                            }
                        }
                    },
                    10000);
        }
        
//Start of code by Dinanath for Dataflow
        else if (idValue.equals("documentverficationsystem")) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                public void run() {
                    DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
//                            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-dd-MM");
                    Date date = new Date();
                    String todaysDate = dateFormat.format(date);

                    final SourceConn sc = new SourceConn();
                    final EtlLoadForJKP eltJkp = new EtlLoadForJKP();
                    Connection con = null;
                    con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                    CallableStatement proc = null;
                    PbReturnObject retObj = null;
                    String tableName = "documentverficationsystem", startDate = "";
                    Format formatter = new SimpleDateFormat("dd-MMM-yy");
                    date = new Date();
                    String s = formatter.format(date);
                    String Filename = "DataFlow_Load" + s;
                    PbDb pbdb = new PbDb();
//                    String query = "SELECT TAB_NAME,LAST_UPDATE_DATE from prg_load_tracker_master where  tab_name in ('documentverficationsystem')";
                    String truntable[] = {"DOCUMENTVERFICATIONSYSTEM_1_stg"};

                    try {

                        logrw.fileWriterWithFileName("---------------------------------------------Data Flow ETL Scheduler " + s + " --------------------------------------------------", Filename);

                            System.out.println(":::::RUNNING TRUNCATES::::");
                            date = new Date();
                            logrw.fileWriterWithFileName("---------------------Truncation Started-----------------------@" + date + "\n", Filename);
                                con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                                System.out.println("TRUNCATE TABLE " + truntable[0] + ";");
                                int j = pbdb.execUpdateSQL("TRUNCATE TABLE " + truntable[0] + ";", con);
                                logrw.fileWriterWithFileName("(" + ( 0 + 1) + ") TRUNCATE TABLE " + truntable[0], Filename);
                            logrw.fileWriterWithFileName("---------------------Truncation Completed-----------------------\n", Filename);

                                System.out.println("eltJkp.runLoadForDataflow...start.." + tableName + "...." + date);
                                eltJkp.runLoadForDataflow("DOCUMENTVERFICATIONSYSTEM_1", "No Data", "No Data", false, Filename);

                            System.out.println(":::::COMPLETED INSERTING IN TABLES::::");
                            logrw.fileWriterWithFileName("---------------------Insertion Completed -----------------------\n", Filename);
                            date = new Date();
                            System.out.println(":::::CALLING ENDING PROCEDURES::::" + date);
                            logrw.fileWriterWithFileName("---------------------Calling Procedures-----------------------@" + date + "\n", Filename);
                            con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                            proc = con.prepareCall("{ call dataflow.DOCUMENTVERFICATIONSYSTEM_initial() }");
                            proc.execute();
                            if (con != null) {
                                con.close();

                            }
//                        }
                        date = new Date();
                        logrw.fileWriterWithFileName(":::::LOAD COMPLETED SUCCESSFULLY::::@" + date.toString() + "\n", Filename);
                        System.out.println(":::::ETL LOAD COMPLETED SUCCESSFULLY::::@" + date.toString());
                        sendSchedulerFinishMail(s1, Filename);

                    } catch (Exception ex) {
                        try {
                            logrw.fileWriterWithFileName("DATA FLOW LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                        } catch (IOException ex1) {
                            logger.error("Exception: ", ex1);
                        }
                        sendSchedulerEtlFailedMail(s1, "DATA FLOW LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                        System.out.println(":::::ETL ERROOOOOORRRRRRRRRRRRR::::" + ex);
                        logger.error("Exception: ", ex);
                    }
                }
            },
                    10000);
        } 
        else if (idValue.equals("Dataflow_Auto_Etl")) {//added by Dinanath
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            try {
                                String status = eltJkp.runDailyDFLoadAutoETL(s1);
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }

                        }
                    },
                    10000);
        } else if (idValue.equals("DF_Philippines_Auto_Etl")) {//added by Dinanath
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            try {
                                String status = eltJkp.runDailyPhilippines_Auto_ETL(s1);
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }

                        }
                    },
                    10000);
        } else if (idValue.equals("TcktLoad")) {
//            logger.info("hello"Tct+idValue);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
                            Date date = new Date();
                            String todaysDate = dateFormat.format(date);

                            final SourceConn sc = new SourceConn();
                            final EtlLoadForJKP eltJkp = new EtlLoadForJKP();

                            Connection con = null;
                            con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");

                            CallableStatement proc = null;
                            PbReturnObject retObj = null;
                            PbReturnObject retObj2 = null;
                            String tableName = "", startDate = "";
                            PbDb pbdb = new PbDb();
//                            
                            String query = "SELECT TAB_NAME,LAST_UPDATE_DATE from prg_load_tracker_master where  tab_name in ('TICKET_CRMUSER_LOCATION','Ticket_Data')";
                            String truntable[] = {"TICKET_CRMUSER_LOCATION_stg", "Ticket_Data_stg"};

//                            
                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                                if (retObj != null && retObj.getRowCount() > 0) {

                                    for (int i = 0; i < truntable.length; i++) {
                                        con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");

                                        int j = pbdb.execUpdateSQL("TRUNCATE TABLE " + truntable[i] + ";", con);
                                    }
                                    for (int i = 0; i < retObj.getRowCount(); i++) {
                                        tableName = retObj.getFieldValueString(i, 0);
                                        startDate = retObj.getFieldValueString(i, 1);
                                        Date date1 = new Date();

                                        eltJkp.runLoadForDataflow(tableName, startDate, todaysDate, false, "");
                                        //updates in fact


                                    }

                                    //calling ending procedures
                                    Date date1 = new Date();

                                    con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                                    proc = con.prepareCall("{ call dataflow.Ticket_Applicant_Proc() }");
                                    proc.execute();
                                    if (con != null) {

                                        con.close();

                                    }
                                }
                                sendSchedulerFinishMail(s1, "");

                            } catch (Exception ex) {
                                sendSchedulerEtlFailedMail(s1, "DATA FLOW TICKET_LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", "");

                                logger.error("Exception: ", ex);
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("LIVE_CASE")) {

//            
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            final SourceConn sc = new SourceConn();
                            final EtlLoadForJKP eltJkp = new EtlLoadForJKP();
                            // CallableStatement proc = null;
                            //call of truncate procedure
                            Connection con = null;
                            con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                            //Connection con1 = null;
                            //con1 = sc.getConnection("Df_oracle", "", "", "", "", "", "", "", "");
                            CallableStatement proc = null;
                            PbReturnObject retObj = null;
                            PbReturnObject retObj2 = null;
                            //   String tableName="",startDate="";
                            String Last_date = "";
                            PbDb pbdb = new PbDb();
//
                            try {
                                retObj = pbdb.execSelectSQL("select last_live_case_update from staging.prg_load_tracker_master where id=100", con);
//                                if (retObj != null && retObj.getRowCount() > 0) {

                                if (retObj != null && retObj.getRowCount() > 0) {
                                    Last_date = retObj.getFieldValueString(0, 0);
                                }

//
                                con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
//                                        


                                int i = pbdb.execUpdateSQL("TRUNCATE TABLE DVS_LIVE_CASE_COUNT_STG;", con);

                                eltJkp.Live_Case_Update("DVS_LIVE_CASE_COUNT", Last_date);
                                //updates in fact


//                                    }

                                //calling ending procedures
                                Date date1 = new Date();

                                con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                                proc = con.prepareCall("{ call dataflow.live_Case_Proc() }");
                                proc.execute();
                                if (con != null) {

                                    con.close();

                                }
//                                }

                                sendSchedulerFinishMail(s1, "");

                            } catch (Exception ex) {
                                sendSchedulerEtlFailedMail(s1, "DATA FLOW LOAD ERROR FOR TABLE DVS_LIVE_CASE_COUNT And Exception is ****" + ex + "****", "");

                                logger.error("Exception: ", ex);
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("DvsUserLoad")) {
//            
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
//                         DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
//                            Date date = new Date();
//                         String todaysDate = dateFormat.format(date);

                            final SourceConn sc = new SourceConn();
                            final EtlLoadForJKP eltJkp = new EtlLoadForJKP();

                            Connection con = null;
                            con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");

                            CallableStatement proc = null;
                            PbReturnObject retObj = null;
                            PbReturnObject retObj2 = null;
                            String tableName = "", startDate = "";
                            PbDb pbdb = new PbDb();
//                             String Last_date="";
//                            
//                            String query = "SELECT TAB_NAME,LAST_UPDATE_DATE from prg_load_tracker_master where  tab_name in ('TICKET_CRMUSER_LOCATION','Ticket_Data')";
//                            String truntable[]={"dvs_user_stg"};

//                            
                            try {
//                               retObj = pbdb.execSelectSQL("select last_dvs_user_update from staging.prg_load_tracker_master where id=100", con);
//                                if (retObj != null && retObj.getRowCount() > 0) {

//                                    if (retObj != null && retObj.getRowCount() > 0) {
//                                       startDate= retObj.getFieldValueString(0, 0);
//    }

//
//                                        con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
//                                        


                                int i = pbdb.execUpdateSQL("TRUNCATE TABLE dvs_user_stg;", con);

                                eltJkp.runLoadForDataflow("dvs_user", "No Need", "No Need", false, "");
                                //updates in fact


//                                    }

                                //calling ending procedures
                                Date date1 = new Date();

                                con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                                proc = con.prepareCall("{ call dataflow.User_Performer_Proc() }");
                                proc.execute();
                                if (con != null) {

                                    con.close();

                                }
//                                }

                                sendSchedulerFinishMail(s1, "");

                            } catch (Exception ex) {
                                sendSchedulerEtlFailedMail(s1, "DATA FLOW DvsUserLoad ERROR FOR TABLE DVS_USER And Exception is ****" + ex + "****", "");

                                logger.error("Exception: ", ex);
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("DvsPersonal")) {
//            
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
                            Date date = new Date();
                            String todaysDate = dateFormat.format(date);

                            final SourceConn sc = new SourceConn();
                            final EtlLoadForJKP eltJkp = new EtlLoadForJKP();

                            Connection con = null;
                            con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");

                            CallableStatement proc = null;
                            PbReturnObject retObj = null;
                            PbReturnObject retObj2 = null;
                            String tableName = "", startDate = "";
                            PbDb pbdb = new PbDb();
//                             String Last_date="";
//                            
//                            String query = "SELECT TAB_NAME,LAST_UPDATE_DATE from prg_load_tracker_master where  tab_name in ('TICKET_CRMUSER_LOCATION','Ticket_Data')";
//                            String truntable[]={"dvs_user_stg"};

//                            
                            try {
//                               retObj = pbdb.execSelectSQL("select last_dvs_user_update from staging.prg_load_tracker_master where id=100", con);
//                                if (retObj != null && retObj.getRowCount() > 0) {

//                                    if (retObj != null && retObj.getRowCount() > 0) {
//                                       startDate= retObj.getFieldValueString(0, 0);
//    }

//
//                                        con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
//                                        


                                int i = pbdb.execUpdateSQL("TRUNCATE TABLE dvs_personal_stg;", con);
                                eltJkp.runLoadForDataflow("dvs_personal", "No Need", todaysDate, false, "");
                                //updates in fact


//                                    }

                                //calling ending procedures
                                Date date1 = new Date();

                                con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                                proc = con.prepareCall("{ call dataflow.personal_staging_proc() }");
                                proc.execute();
                                proc = con.prepareCall("{ call dataflow.personal_dim_proc() }");
                                proc.execute();
                                if (con != null) {

                                    con.close();

                                }
//                                }

                                sendSchedulerFinishMail(s1, "");

                            } catch (Exception ex) {
                                sendSchedulerEtlFailedMail(s1, "DATA FLOW DvsPersonal ERROR FOR TABLE dvs_personal And Exception is ****" + ex + "****", "");

                                logger.error("Exception: ", ex);
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("DvsModeofVerification")) {
//            
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
                            Date date = new Date();
                            String todaysDate = dateFormat.format(date);

                            final SourceConn sc = new SourceConn();
                            final EtlLoadForJKP eltJkp = new EtlLoadForJKP();

                            Connection con = null;
                            con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");

                            CallableStatement proc = null;
                            PbReturnObject retObj = null;
                            PbReturnObject retObj2 = null;
                            String tableName = "", startDate = "";
                            PbDb pbdb = new PbDb();
//                             String Last_date="";
//                            
//                            String query = "SELECT TAB_NAME,LAST_UPDATE_DATE from prg_load_tracker_master where  tab_name in ('TICKET_CRMUSER_LOCATION','Ticket_Data')";
//                            String truntable[]={"dvs_user_stg"};

//                            
                            try {
//                               retObj = pbdb.execSelectSQL("select last_dvs_user_update from staging.prg_load_tracker_master where id=100", con);
//                                if (retObj != null && retObj.getRowCount() > 0) {

//                                    if (retObj != null && retObj.getRowCount() > 0) {
//                                       startDate= retObj.getFieldValueString(0, 0);
//    }

//
//                                        con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
//                                        


                                int i = pbdb.execUpdateSQL("TRUNCATE TABLE dvs_modeofverification_stg;", con);
                                eltJkp.runLoadForDataflow("dvs_modeofverification", "No Need", todaysDate, false, "");
                                //updates in fact


//                                    }

                                //calling ending procedures
                                Date date1 = new Date();

                                con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                                proc = con.prepareCall("{ call dataflow.Mode_of_verification() }");
                                proc.execute();

                                if (con != null) {

                                    con.close();

                                }
//                                }

                                sendSchedulerFinishMail(s1, "");

                            } catch (Exception ex) {
                                sendSchedulerEtlFailedMail(s1, "DATA FLOW dvs_modeofverification ERROR FOR TABLE dvs  And Exception is ****" + ex + "****", "");

                                logger.error("Exception: ", ex);
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("Mail_Delivery")) {
//            
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            final SourceConn sc = new SourceConn();
                            final EtlLoadForJKP eltJkp = new EtlLoadForJKP();
                            Connection con = null;
                            CallableStatement proc = null;
                            try {
                                con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                                proc = con.prepareCall("{ call dataflow.Mail_Delivery_Status() }");
                                proc.execute();

                                if (con != null) {
                                    con.close();
                                }
                                sendSchedulerFinishMail(s1, "");

                            } catch (Exception ex) {
                                sendSchedulerEtlFailedMail(s1, "DATA FLOW Mail_Delivery_Status ERROR ---" + ex, "");

                                logger.error("Exception: ", ex);
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("XmlLoad")) {
            logrw.setLogFileName("XmlLoad");
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            UploadingXmlIntoDatabase UpXml = new UploadingXmlIntoDatabase();
                            mailer = new PbMail();
                            String emailId = String.valueOf(s1.getSchedulerLoadEmailId());
                            CallableStatement proc = null;
                            Connection con = null;
                            try {
                                Format formatter = new SimpleDateFormat("dd-MMM-yy");
                                con = ProgenConnection.getInstance().getConnection();
                                Date date = new Date();
                                String s = formatter.format(date);
                                logrw.fileWriter("---------------------------------------------Leela Xml Scheduler " + s + " --------------------------------------------------");
                                UpXml.TruncateLoadTracker("ALL");
                                mailer.downloadEmailAttachments(emailId);
                                UpXml.UpdateSchedulerlogs(s1.getSchedulerLoadId(), s1.getSchedulerLoadType(), "Succeed");
                                proc = con.prepareCall("{ call test_Proc.dbo.Leela_Final_Proc() }");
                                Date date1 = new Date();

                                logrw.fileWriter("---------------------------------------------Calling Ending Procedure  " + date1.toString() + "--------------------------------------------------");
                                proc.execute();
                                Date date2 = new Date();

                                logrw.fileWriter("---------------------------------------------ETL LOAD COMPLETED  " + date2.toString() + "--------------------------------------------------");
                                sendSchedulerFinishMail(s1, "");
                            } catch (Exception ex) {
                                UpXml.UpdateSchedulerlogs(s1.getSchedulerLoadId(), s1.getSchedulerLoadType(), "failed");
                                sendSchedulerEtlFailedMail(s1, ex.toString(), "");
                                logger.error("Exception: ", ex);
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("PgmasEtl")) {
//            
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {

                            final SourceConn sc = new SourceConn();
                            final EtlLoadForJKP eltJkp = new EtlLoadForJKP();
                            // CallableStatement proc = null;
                            //call of truncate procedure
                            Connection con = null;
                            Connection con1 = null;
                            try {
                                con = sc.getConnection("pgmas", "", "", "", "", "", "", "", "");
                                con1 = sc.getConnection("metapgmas", "", "", "", "", "", "", "", "");
                                if (con == null) {
                                    Date date1 = new Date();
                                    sendSchedulerEtlFailedMail(s1, "PGMAS LOAD ERROR **pgmas CONNECTION ERROR** Date- " + date1, "");
                                } else if (con1 == null) {
                                    Date date1 = new Date();
                                    sendSchedulerEtlFailedMail(s1, "PGMAS LOAD ERROR **metapgmas CONNECTION ERROR** Date- " + date1, "");
                                } else {

//                            con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                                    //Connection con1 = null;
                                    //con1 = sc.getConnection("Df_oracle", "", "", "", "", "", "", "", "");
                                    CallableStatement proc = null;
                                    PbReturnObject retObj = null;
                                    PbReturnObject retObj2 = null;
                                    String tableName = "", lastdate = "";
                                    PbDb pbdb = new PbDb();
//                            
//                              
                                    String query = "SELECT LAST_UPDATE_DATE from PRG_LOAD_TRACKER_MASTER";
                                    retObj = pbdb.execSelectSQL(query, con1);
                                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                                    DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                                    Date date = new Date();
                                    Date ludate = null;
                                    java.sql.Date sqlDate1 = null;
                                    java.sql.Date sqlDate2 = null;
                                    String todaysDate = dateFormat.format(date);


                                    if (retObj != null && retObj.getRowCount() > 0) {

                                        lastdate = retObj.getFieldValueString(0, 0);
                                        ludate = dateFormat1.parse(lastdate);
//
                                        String todaysDate1 = dateFormat1.format(ludate);
//                      
//                          
                                        sqlDate1 = new java.sql.Date(ludate.getTime());
                                        sqlDate2 = new java.sql.Date(date.getTime());

                                        proc = con.prepareCall("{ call pgmas_bi.DIM_PROC() }");
                                        proc.execute();

                                        proc = con.prepareCall("{ call pgmas_bi.ORDER_NEW_fact_proc(?,?) }");
                                        proc.setDate(1, sqlDate1);
                                        proc.setDate(2, sqlDate2);
                                        proc.execute();

                                        proc = con.prepareCall("{ call pgmas_bi.reservation_fact_proc(?,?) }");
                                        proc.setDate(1, sqlDate1);
                                        proc.setDate(2, sqlDate2);
                                        proc.execute();

                                        proc = con.prepareCall("{ call pgmas_bi.prg_Contact_fact_PROC(?,?) }");
                                        proc.setDate(1, sqlDate1);
                                        proc.setDate(2, sqlDate2);
                                        proc.execute();

                                        proc = con.prepareCall("{ call pgmas_bi.prg_guestlist_fact_proc(?,?) }");
                                        proc.setDate(1, sqlDate1);
                                        proc.setDate(2, sqlDate2);
                                        proc.execute();

                                        proc = con.prepareCall("{ call pgmas_bi.time_analysis_fact_proc(?,?) }");
                                        proc.setDate(1, sqlDate1);
                                        proc.setDate(2, sqlDate2);
                                        proc.execute();

                                        proc = con.prepareCall("{ call pgmas_bi.Star_Rating_Proc() }");
                                        proc.execute();

                                    }
                                    con1 = sc.getConnection("metapgmas", "", "", "", "", "", "", "", "");

                                    if (con1 == null) {
                                        Date date1 = new Date();
                                        sendSchedulerEtlFailedMail(s1, "PGMAS LOAD ERROR **metapgmas CONNECTION ERROR** Date- " + date1, "");
                                    }
                                    query = "UPDATE PRG_LOAD_TRACKER_MASTER SET LAST_UPDATE_DATE='" + todaysDate + "'";
//                                    int c = pbdb.execUpdateSQL(query, con1);
                                    pbdb.execUpdateSQL(query, con1);
//                                
//                                    if (c == 1) {
//                                        
//                                    } else {
//                                        
//                                    }

                                    sendSchedulerFinishMail(s1, "");
                                }
                            } catch (Exception ex) {
                                Date date1 = new Date();
                                sendSchedulerEtlFailedMail(s1, "PGMAS LOAD ERROR  Date- " + date1, "");
//                                
                                logger.error("Exception: ", ex);
                            }
                        }
                    },
                    10000);
        } else if (idValue.equalsIgnoreCase("UndeliveredMail")) {//added by Dinanath for calling to load inbox message for undelivered status
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            try {
                                PbMail pbmail = new PbMail();
                                pbmail.downloadFailedEmailInbox("dinanath.parit@progenbusiness.com");
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }

                        }
                    },
                    10000);
        } else if (idValue.contains("CampBell_Load")) {

//           logrw.setLogFileName(idValue);
            final String NewidValue = idValue;
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {

                            UploadingXmlIntoDatabase UpXml = new UploadingXmlIntoDatabase();
                            Format formatter = new SimpleDateFormat("dd-MMM-yy");
                            Date date = new Date();
                            String s = formatter.format(date);
                            String filename = NewidValue + s;
                            try {
                                logrw.fileWriterWithFileName("---------------------------------------------CampBell ETL Scheduler " + s + " --------------------------------------------------", filename);
                                final SourceConn sc = new SourceConn();
                                final EtlLoadForJKP eltJkp = new EtlLoadForJKP();
                                Connection con = null;
                                Connection Biserver = null;
                                Connection Accpac = null;
                                Connection NS5 = null;
                                CallableStatement proc = null;
                                PbReturnObject retObj = null;
                                PbReturnObject Checkdate1 = null;
                                PbReturnObject Checkdate2 = null;
                                String Subject = "";
                                String text = "Please check connections and restart the services to again run scheduler";
                                String tableName = "";
                                String LOCATION_NAME = "";
                                String columnNames="";
                                String Message = "";
                                String adate = "";
                                String sdate = "";
                                String startDate = "";
                                PbDb pbdb = new PbDb();
                                Biserver = ProgenConnection.getInstance().getConnection();
                                Accpac = sc.getConnection("ACCPAC_CAMPBELL", "", "", "", "", "", "", "", "");
                                NS5 = sc.getConnection("NS5", "", "", "", "", "", "", "", "");
                                if (Biserver == null) {
                                    Subject = "BiServer's MYSQL Service is down, ETL Load terminated";

                                    sendSchedulerCustomMail(s1, Subject, text, filename);
                                } else if (Accpac == null) {
                                    Subject = "Accpac Service is down, ETL Load terminated";
                                    sendSchedulerCustomMail(s1, Subject, text, filename);
                                } else if (NS5 == null) {
                                    Subject = "NS5 Service is down, ETL Load terminated";
                                    sendSchedulerCustomMail(s1, Subject, text, filename);
                                } else {
                                    String query = "SELECT TAB_NAME,LOCATION_NAME,DISPLAY_COLUMN_NAME from prg_load_tracker_master where  LOCATION_NAME in ('ACCPAC_CAMPBELL','NS5')";
                                    retObj = pbdb.execSelectSQL(query);
                                    if (retObj != null && retObj.getRowCount() > 0) {
                                        for (int i = 0; i < retObj.getRowCount(); i++) {
                                            try {
                                                Message = "";
                                                LOCATION_NAME = retObj.getFieldValueString(i, "LOCATION_NAME");
                                                tableName = retObj.getFieldValueString(i, "TAB_NAME");
                                                columnNames = retObj.getFieldValueString(i, "DISPLAY_COLUMN_NAME");
                                                logrw.fileWriterWithFileName("---------------------Truncation Started-----------------------\n", filename);
                                                if (LOCATION_NAME.equalsIgnoreCase("ACCPAC_CAMPBELL")) {
                                                    Accpac = sc.getConnection(LOCATION_NAME, "", "", "", "", "", "", "", "");
                                                    Checkdate1 = pbdb.execSelectSQL(" SELECT MAX(AUDTDATE) FROM ERP_CS_COMPANY_DATA_DBO." + tableName);
                                                    Checkdate2 = pbdb.execSelectSQL(" SELECT MAX(AUDTDATE) FROM " + tableName, Accpac);
//                                          if(Checkdate2.getFieldValueInt(i, "AUDTDATE")!=0 && Checkdate1.getFieldValueInt(i, "AUDTDATE")!=0 && Checkdate2.getFieldValueInt(i, "AUDTDATE")<=Checkdate1.getFieldValueInt(i, "AUDTDATE"))
//                                          {
                                                    adate = Checkdate2.getFieldValueInt(0, 0) + "";
                                                    sdate = Checkdate1.getFieldValueInt(0, 0) + "";
//                                                     if(adate!=null && !adate.equalsIgnoreCase("") )
//                                                    adate = adate.substring(0, 4) + "-" + adate.substring(4, 6) + "-" + adate.substring(6);
//                                                   if(sdate!=null && !sdate.equalsIgnoreCase("") )
//                                                    sdate = sdate.substring(0, 4) + "-" + sdate.substring(4, 6) + "-" + sdate.substring(6);
//                                                 Subject="Latest data is not present in AccPac Server, ETL Load running as usual ";
//                                                 text= "Table Name:"+retObj.getFieldValueString(i, "TAB_NAME")+"\n"
//                                                        +"ACCPAC-AUDTDATE:"+adate+" and BI-AUDTDATE:"+sdate;
//                                                 sendSchedulerCustomMail(s1, Subject, text, filename);
//                                                 Message="The data in AP Payables Header table ("+retObj.getFieldValueString(i, "TAB_NAME")+") is same in Data Warehouse "+retObj.getFieldValueString(i, "TAB_NAME")+" Table"
//                                                         + "ACCPAC-AUDTDATE:"+adate+" and BI-AUDTDATE:"+sdate;
//                                          }else
//                                          {
                                                    Message = "ACCPAC-AUDTDATE:" + adate + " and BI-AUDTDATE:" + sdate;
//                                          }
                                                    pbdb.execUpdateSQL("TRUNCATE TABLE  ERP_CS_COMPANY_DATA_DBO." + tableName);
//                                        stmt.execute("TRUNCATE TABLE  ERP_CS_COMPANY_DATA_DBO." + retObj.getFieldValueString(i, "TAB_NAME"));
                                                    logrw.fileWriterWithFileName("TRUNCATE TABLE  ERP_CS_COMPANY_DATA_DBO." + tableName, filename);
                                                    System.out.println("TRUNCATE TABLE " + tableName);
                                                    pbdb.execUpdateSQL("insert into cs_dw.csdw_etl_log (process_name, source_schema, target_Schema, load_date, start_date, end_date, source_count, target_count, processed_flag, message)"
                                                            + "values ('" + tableName + "','erp_cs_company_data','erp_cs_company_data_dbo',date(sysdate()), sysdate(),'','','','','" + Message + "')");
                                                } else if (LOCATION_NAME.equalsIgnoreCase("NS5")) {
                                                    pbdb.execUpdateSQL("TRUNCATE TABLE  SAFENET." + tableName);
//                                         stmt.execute("TRUNCATE TABLE  SAFENET." + retObj.getFieldValueString(i, "TAB_NAME"));
                                                    logrw.fileWriterWithFileName("TRUNCATE TABLE  SAFENET." + tableName, filename);

                                                    System.out.println("TRUNCATE TABLE " + tableName);
                                                }


                                                int totalrows = eltJkp.LoadForCampBell(tableName, LOCATION_NAME, filename,columnNames);
                                                PbReturnObject load_id = pbdb.execSelectSQL(" select max(load_id) from cs_dw.csdw_etl_log where load_date = date(sysdate()) and process_name = '" + tableName + "'");
                                                if (LOCATION_NAME.equalsIgnoreCase("ACCPAC_CAMPBELL") && load_id != null) {
                                                    Message += " " + tableName + " Inserted and ETL Load Successful";
                                                    String query1 = "update cs_dw.csdw_etl_log set end_date=sysdate(),source_count=" + totalrows + ","
                                                            + "target_count=" + totalrows + ",processed_flag='Y',message='" + Message + "' where load_id=" + load_id.getFieldValueString(0, 0) + " and process_name='" + tableName + "'";

                                                    pbdb.execUpdateSQL(query1);

                                                }
                                            } catch (Exception ex) {
                                                UpXml.InsertIntoTrackerMaster(tableName, "Scheduler", " 0 and Exception is " + ex, "Failed");
                                                logrw.fileWriterWithFileName("Exception for " + tableName + " is :::::" + ex.toString() + "::::", filename);
                                                sendSchedulerEtlFailedMail(s1, ex.toString(), filename);

                                                logger.error("Exception: ", ex);
                                            }
                                        }

                                    }
                                    logrw.fileWriterWithFileName(":::::::::::::::::::Insertion Completed:::::::::::::::::::::\n", filename);
                                    Date d1 = new Date();
                                    logrw.fileWriterWithFileName(":::::::::::::::::::Calling Ending Procedures:::::::::::::::::::::@" + d1.toString() + "\n", filename);

                                    con = ProgenConnection.getInstance().getConnection();
                                    if (NewidValue.equalsIgnoreCase("CampBell_Load")) {
                                        Thread myThreads[] = new Thread[RunMultiProcedure.totalproc];
                                        logrw.fileWriterWithFileName(":::::::::::::::::::Calling Independent Procedures:::::::::::::::::::::@\n", filename);

                                        RunMultiProcedure.filename = filename;
                                        for (int x = 0; x < RunMultiProcedure.totalproc; x++) {
                                            myThreads[x] = new Thread(new RunMultiProcedure());
                                            myThreads[x].start();
                                        }
                                        for (int x = 0; x < RunMultiProcedure.totalproc; x++) {
                                            myThreads[x].join();
                                        }
                                        d1 = new Date();
                                        logrw.fileWriterWithFileName(":::::::::::::::::::Calling Dependent Procedures:::::::::::::::::::::@" + d1.toString() + "\n", filename);

                                        for (int x = 0; x < RunMultiProcedure.dependentProcs.length; x++) {
                                            proc = con.prepareCall("{ call cs_dw." + RunMultiProcedure.dependentProcs[x] + "() }");

                                            logrw.fileWriterWithFileName("{ call cs_dw." + RunMultiProcedure.dependentProcs[x] + "() }", filename);
                                            proc.execute();
                                        }

                                    } else if (NewidValue.equalsIgnoreCase("CampBell_Load_Snapshot")) {
                                        proc = con.prepareCall("{ call cs_dw.prg_cs_snapshot_mon_end_inc() }");

                                        logrw.fileWriterWithFileName("{ call cs_dw.prg_cs_snapshot_mon_end_inc() }", filename);
                                        proc.execute();
                                    }
                                    d1 = new Date();
                                    logrw.fileWriterWithFileName(":::::LOAD COMPLETED SUCCESSFULLY::::@" + d1.toString() + "\n", filename);

                                    UpXml.UpdateSchedulerlogs(s1.getSchedulerLoadId(), s1.getSchedulerLoadType(), "Succeed");
                                    sendSchedulerFinishMail(s1, filename);
                                }
                                if (con != null || Biserver != null || Accpac != null || NS5 != null) {
                                    con.close();
                                    Biserver.close();
                                    Accpac.close();
                                    NS5.close();
                                }

                            } catch (Exception ex) {
                                UpXml.UpdateSchedulerlogs(s1.getSchedulerLoadId(), s1.getSchedulerLoadType(), "Failed");
                                try {
                                    logrw.fileWriterWithFileName("Exception is :::::" + ex.toString() + "::::", filename);
                                } catch (IOException ex1) {
                                    logger.error("Exception: ", ex1);
                                }
                                sendSchedulerEtlFailedMail(s1, ex.toString(), filename);
                                logger.error("Exception: ", ex);
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("DFLoad_ PHILIPPINES")) {
//            
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {

                            DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
//                            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-dd-MM");
                            Date date = new Date();
                            String todaysDate = dateFormat.format(date);

                            final SourceConn sc = new SourceConn();
                            final EtlLoadForJKP eltJkp = new EtlLoadForJKP();
                            Connection con = null;
                            con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                            CallableStatement proc = null;
                            PbReturnObject retObj = null;
                            PbReturnObject retObj2 = null;
                            String tableName = "", startDate = "";
                            Format formatter = new SimpleDateFormat("dd-MMM-yy");
                            date = new Date();
                            String s = formatter.format(date);
                            String Filename = "DataFlow_PHILIPPINES_Load" + s;
                            PbDb pbdb = new PbDb();
//                            logger.info("con----------" + con.toString());
                            String query = "SELECT TAB_NAME,LAST_UPDATE_DATE from prg_load_tracker_master where  tab_name not in ('TICKET_CRMUSER_LOCATION','Ticket_Data','dvs_personal','dvs_modeofverification')";

//                            String query = "SELECT TAB_NAME,LAST_UPDATE_DATE from prg_load_tracker_master where  tab_name  in ('dvs_health_license','dvs_employment','dvs_education')";
                            String truntable[] = {"dvs_stg", "workitem_stg", "dvs_employment_stg", "documentverficationsystem_stg", "dvs_health_license_stg", "dvs_education_stg", "dvs_case_stg", "dvs_user_stg", "dvs_issuing_authority_stg", "processinstance_stg"};
//                             String truntable[] = {"dvs_health_license_stg","dvs_employment_stg","dvs_education_stg"};
//

//                            logger.info("query-----------" + query);
                            try {
                                logrw.fileWriterWithFileName("---------------------------------------------DATA FLOW PHILIPPINES ETL SCHEDULER " + s + " --------------------------------------------------", Filename);

                                retObj = pbdb.execSelectSQL(query, con);
                                if (retObj != null && retObj.getRowCount() > 0) {
                                    logger.info(":::::RUNNING TRUNCATES::::");
                                    date = new Date();
                                    logrw.fileWriterWithFileName("---------------------Truncation Started-----------------------@" + date + "\n", Filename);

                                    for (int i = 0; i < truntable.length; i++) {
                                        con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                                        logger.info("TRUNCATE TABLE " + truntable[i] + ";");
                                        int j = pbdb.execUpdateSQL("TRUNCATE TABLE " + truntable[i] + ";", con);
                                        logrw.fileWriterWithFileName("(" + (i + 1) + ") TRUNCATE TABLE " + truntable[i], Filename);
                                    }
                                    logrw.fileWriterWithFileName("---------------------Truncation Completed-----------------------\n", Filename);
                                    for (int i = 0; i < retObj.getRowCount(); i++) {
                                        tableName = retObj.getFieldValueString(i, 0);
                                        startDate = retObj.getFieldValueString(i, 1);
                                        Date date1 = new Date();
                                        logger.info("eltJkp.runLoadForDataflow...start.." + tableName + "...." + date1);
                                        eltJkp.runLoadForDataflow(tableName, startDate, todaysDate, true, Filename);
                                        //updates in fact

                                    }
                                    logger.info(":::::COMPLETED INSERTING IN TABLES::::");
                                    logrw.fileWriterWithFileName("---------------------Insertion Completed -----------------------\n", Filename);
                                    //calling ending procedures
                                    Date date1 = new Date();
                                    logger.info(":::::CALLING ENDING PROCEDURES::::" + date1);
                                    date = new Date();
                                    logrw.fileWriterWithFileName("---------------------Calling Procedures-----------------------@" + date + "\n", Filename);
                                    con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                                    proc = con.prepareCall("{ call dataflow.philippines_proc() }");
                                    proc.execute();
                                    if (con != null) {
                                        con.close();

                                    }
                                }
                                logger.info(":::::LOAD COMPLETED SUCCESSFULLY::::");
                                logrw.fileWriterWithFileName(":::::LOAD COMPLETED SUCCESSFULLY::::@" + date.toString() + "\n", Filename);
                                sendSchedulerFinishMail(s1, Filename);

                                date = new Date();

                            } catch (Exception ex) {
                                try {
                                    logrw.fileWriterWithFileName("DATA FLOW LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                                } catch (IOException ex1) {
                                    logger.error("Exception: ", ex1);
                                }

                                sendSchedulerEtlFailedMail(s1, "DFLoad_ PHILIPPINES ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                                logger.error(":::::ERROOOOOORRRRRRRRRRRRR::::: ", ex);
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("Hourly_Ping")) {
//            logger.info("hello"+idValue);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            PbDb db = new PbDb();
                            String query = "";
                            int execUpdateSQL = 0;
                            try {
                                Connection con = ProgenConnection.getInstance().getConnection();
                                switch (ProgenConnection.getInstance().getDatabaseType()) {
                                    case ProgenConnection.SQL_SERVER:
                                        query = "select getdate()";
                                        break;
                                    case ProgenConnection.MYSQL:
                                        query = "SELECT NOW()";
                                        break;
                                    default:
                                        query = "select sysdate from dual";
                                        break;
                                }
                                PbReturnObject execSelectSQL = db.execSelectSQL(query, con);
                                if (execSelectSQL != null) {
                                    logger.info("Its Hourly Ping..Current Time=" + execSelectSQL.getFieldValueString(0, 0));
                                }
                            } catch (Exception ex) {
                                logger.error(":::::ERROOOOOORRRRRRRRRRRRR::::", ex);
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("Deleted_Barcodes")) {
//            logger.info("hello"Tct+idValue);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
                            Date date = new Date();
                            String todaysDate = dateFormat.format(date);

                            final SourceConn sc = new SourceConn();
                            final EtlLoadForJKP eltJkp = new EtlLoadForJKP();

                            Connection con = null;
                            con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");

                            CallableStatement proc = null;
                            String tableName = "", startDate = "";
                            PbDb pbdb = new PbDb();
                            try {

                                int i = pbdb.execUpdateSQL("TRUNCATE TABLE dvs_deleted_barcodes_stg;", con);
                                eltJkp.runLoadForDataflow("dvs_deleted_barcodes", "No Need", todaysDate, false, "");

                                Date date1 = new Date();

                                con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                                proc = con.prepareCall("{ call dataflow.deleted_barcodes() }");
                                proc.execute();

                                if (con != null) {
                                    con.close();
                                }
                                sendSchedulerFinishMail(s1, "");
                                logger.info(":::::LOAD COMPLETED SUCCESSFULLY::::");
                            } catch (Exception ex) {
                                sendSchedulerEtlFailedMail(s1, "DATA FLOW dvs_modeofverification ERROR FOR TABLE dvs_deleted_barcodes  And Exception is ****" + ex + "****", "");
                                logger.error(":::::ERROOOOOORRRRRRRRRRRRR::::", ex);
                            }
                        }
                    },
                    10000);
        } else if (idValue.equalsIgnoreCase("ETL_Running_Slow")) {//added by Dinanath for calling to load inbox message for undelivered status
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            try {
                                ETL_Running_Slow();
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }

                        }
                    },
                    10000);
        } else if (idValue.equals("Update_Workitem")) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
                            Date date = new Date();
                            String todaysDate = dateFormat.format(date);
                            final SourceConn sc = new SourceConn();
                            final EtlLoadForJKP eltJkp = new EtlLoadForJKP();
                            Connection con = null;
                            CallableStatement proc = null;
                            PbDb pbdb = new PbDb();
                            try {
                                logger.info(":::::RUNNING TRUNCATE FOR TABLE end_date_missing_log ::::");
                                int i = pbdb.execUpdateSQL("TRUNCATE TABLE staging.end_date_missing_log_stg;");
                                eltJkp.runLoadForDataflow("end_date_missing_log", "No Need", todaysDate, false, "");
                                logger.info(":::::COMPLETED INSERTING IN TABLE end_date_missing_log_stg::::");
                                Date date1 = new Date();
                                logger.info(":::::CALLING ENDING PROCEDURES update_workitem()::::" + date1);
                                con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                                proc = con.prepareCall("{ call dataflow.update_workitem() }");
                                proc.execute();
                                if (con != null) {
                                    con.close();
                                }
                                sendSchedulerFinishMail(s1, "");
                                logger.info(":::::LOAD COMPLETED SUCCESSFULLY::::");
                            } catch (Exception ex) {
                                sendSchedulerEtlFailedMail(s1, "DATA FLOW Update_Workitem ERROR FOR TABLE end_date_missing_log  And Exception is ****" + ex + "****", "");
                                logger.error(":::::ERROOOOOORRRRRRRRRRRRR::::", ex);
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("Rebuild_Cache_Scheduler")) {
            final String NewidValue = idValue;
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {

                            Format formatter = new SimpleDateFormat("dd-MMM-yy");
                            Date date = new Date();
                            String s = formatter.format(date);


                            String filename = NewidValue + s;
                            try {
                                logrw.fileWriterWithFileName("#######################################################  Rebuild Cache Scheduler " + s + " ###########################################", filename);
                                logrw.fileWriterWithFileName("REPORT_ID       REPORT_NAME                                      START_TIME              END_TIME            STATUS", filename);
                                logrw.fileWriterWithFileName("---------------------------------------------------------------------------------------------------------------------------------------", filename);


                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }
                            String fileLocation = "/usr/local/cache";
                            PbReportViewerDAO PbR = new PbReportViewerDAO();
                            if (!(PbR.FilePath.equalsIgnoreCase(""))) {
                                fileLocation = PbR.FilePath;
                            }
                            File tempFile = new File(fileLocation + "/AO_Data");
                            File[] allusers = tempFile.listFiles();
                            if (allusers != null) {
                                for (File f1 : allusers) {
                                    try {
                                        tempFile = new File(fileLocation + "/AO_Data/" + f1.getName());
                                        String[] allreports = tempFile.list();
                                        if (allreports != null && allreports.length > 0) {
                                            StringBuilder filterQuery = new StringBuilder();
                                            for (int loop = 0; loop < allreports.length; loop++) {
                                                if (loop == 0) {
                                                    filterQuery.append(allreports[loop]);
                                                } else {
                                                    filterQuery.append(",").append(allreports[loop]);
                                                }
                                            }
                                            ProgenReportViewerDAO prvdao = new ProgenReportViewerDAO();
                                            String rebuildCache = prvdao.rebuildCache(f1.getName(), filterQuery.toString(), fileLocation, true, filename);

                                        }


                                    } catch (Exception ex) {
                                        logger.error("Exception: ", ex);
                                    }



                                }

                                try {
                                    logrw.fileWriterWithFileName("################################################# Scheduler Finished ################################", filename);
                                } catch (Exception ex) {
//            logger.error("Exception: ",ex);
                                }

                                sendSchedulerFinishMail(s1, filename);



                            }
                        }
                    },
                    10000);
        }  else if (idValue.contains("VERACTION_LOAD")) {
            final String NewidValue = idValue;
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            UploadingXmlIntoDatabase UpXml = new UploadingXmlIntoDatabase();
                            Format formatter = new SimpleDateFormat("dd-MMM-yy");
                            Date date = new Date();
                            final SourceConn sc = new SourceConn();
                             Connection con = null;
                            String s = formatter.format(date);
                            String filename = NewidValue + s;
                            PbDb pbdb = new PbDb();
                             RunMultiLoad.filename = filename;
                            try {
                               PbReturnObject client = null;
                                PbReturnObject accPeriod = null;
                                  PbReturnObject client_id = null;
                             logger.info("Veraction ETL Scheduler started   "+date);
                                logrw.fileWriterWithFileName("---------------------------------------------Veraction ETL Scheduler " + s + " --------------------------------------------------", filename);
                                String query = "select distinct client_code from Acc_List";
                                con = sc.getConnection("VR_PROD", "", "", "", "", "", "", "", "");
                                    client = pbdb.execSelectSQL(query,con);
                                    
                                    if (client!=null && client.getRowCount()>0) {
                                       
                                           for (int i = 0; i < client.getRowCount(); i++) {
                                           
                                               con = sc.getConnection("VR_PROD", "", "", "", "", "", "", "", "");
                                               client_id= pbdb.execSelectSQL("select client_id from client where client_key='"+client.getFieldValueString(i, 0)+"'",con);
                                               
                                                if (client_id!=null && client_id.getRowCount()>0) {
                                               RunMultiLoad.allClientCodes.add(client_id.getFieldValueString(0, 0));
                                               query = "select distinct accounting_period_key from Acc_List where client_code='"+client.getFieldValueString(i, 0)+"'";
                                    con = sc.getConnection("VR_PROD", "", "", "", "", "", "", "", "");
                                    accPeriod = pbdb.execSelectSQL(query,con);
                                    
                                    if (accPeriod!=null && accPeriod.getRowCount()>0) {
                                    
                                          ArrayList<String> allAccPeriod=new ArrayList<>();
                                        
                                        for (int j= 0; j < accPeriod.getRowCount(); j++) {
                                        
                                        allAccPeriod.add(accPeriod.getFieldValueString(j, 0));
                                       
                                               
        }
                                        logger.info("client_id "+client_id.getFieldValueString(0, 0)+" allAccPeriod @"+allAccPeriod.toString());
                                                  RunMultiLoad.clientInfo.put(client_id.getFieldValueString(0, 0), allAccPeriod);
                                                  
    }

                                           }
                                           }
                                             RunMultiLoad.myThreads = new Thread[RunMultiLoad.allClientCodes.size()];
//                                              Thread myThreads=null;
                                              for (int x = 0; x < 2; x++) {
                                          RunMultiLoad.myThreads[x] = new Thread(new RunMultiLoad());
                                               RunMultiLoad.myThreads[x].start();
                                        }
                                        for (int x = 0; x < RunMultiLoad.allClientCodes.size(); x++) {
                                            RunMultiLoad.myThreads[x].join();
                                        }
                                    }

                                    logger.info(":::::LOAD COMPLETED SUCCESSFULLY::::@"+new Date());
                                    logrw.fileWriterWithFileName(":::::LOAD COMPLETED SUCCESSFULLY::::@"+new Date()+"\n", filename);
                                    UpXml.UpdateSchedulerlogs(s1.getSchedulerLoadId(), s1.getSchedulerLoadType(), "Succeed");
                                    sendSchedulerFinishMail(s1, filename);
                                
                              
                        
                            } catch (Exception ex) {
                                UpXml.UpdateSchedulerlogs(s1.getSchedulerLoadId(), s1.getSchedulerLoadType(), "Failed");
                                try {
                                    logrw.fileWriterWithFileName("Exception is :::::" + ex.toString() + "::::", filename);
                                } catch (IOException ex1) {
                                    logger.error("Exception: ", ex1);
                                }
                                sendSchedulerEtlFailedMail(s1, ex.toString(), filename);
                                logger.error("Exception: ", ex);
                            }
                        }
                    },
                    10000);
        }else if (idValue.equals("CSVLoad")) {//added by Dinanath
//            System.out.println("hello"+idValue);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        public void run() {

                    try {
                        System.out.println("CSVFile is calling from Schedular");
                        FTPReadCSVFile readcsv=new FTPReadCSVFile();
                        readcsv.csvfile();
                        System.out.println("CSV File loading is finished from Schedular");
                    } catch (Exception ex) {
                        System.out.println("CSV File loading has thrown error.");
                        ex.printStackTrace();
                         }
                       }
                    },
                    10000);
        }else if (idValue.equals("DeleteFileFromCache")) {//added by Dinanath
//            System.out.println("hello"+idValue);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        public void run() {

//                            mailer=new PbMail();
//                            String emailId = String.valueOf(s1.getSchedulerLoadEmailId());
                    try {
//                        mailer.downloadEmailAttachments(emailId);
                        System.out.println("All Files from c://usr/local/cache/ReadWriteCSVFile is being delete. please wait.........");
                       File tempFiles = null;
                         String filePaths = null;
                        filePaths = "c://usr/local/cache/ReadWriteCSVFile";
                        tempFiles = new File(filePaths);
                        if (tempFiles.exists()) {

                        } else {
                        tempFiles.mkdirs();
                        }
                        File[] allFilesAndDirs = tempFiles.listFiles();
                        for(int i=0;i<allFilesAndDirs.length;i++){
                            allFilesAndDirs[i].delete();
                            System.out.println("Deleted File Name: "+allFilesAndDirs[i].toString());
                        }
                        System.out.println("All Files from c://usr/local/cache/ReadWriteCSVFile have deleted successfully.");
//                     sendSchedulerFinishMail(s1);
                    } catch (Exception ex) {
//                         sendSchedulerEtlFailedMail(s1,"DATA FLOW DvsUserLoad ERROR FOR TABLE DVS_USER");
                        ex.printStackTrace();
                    }
                        }
                    },
                    10000);
        }
        }
    

    public void sendSchedulerConnError(String connDetails) {
        String emailId = "";
        if (connDetails.contains("DATA FLOW")) {
            emailId = "mohit.jain@progenbusiness.com,amit@progenbusiness.com,diwakarvats@gmail.com,"
                    + "rahul.singh@dataflowgroup.com,monika.agrawal@progenbusiness.com,amit.gupta@dataflowgroup.com";
        } else if (connDetails.contains("PGMAS")) {
            emailId = "mohit.jain@progenbusiness.com,amit@progenbusiness.com,nitesh.dugar@progenbusiness.com,ankit.gupta@progenbusiness.com";
        } else {
            emailId = "amit@progenbusiness.com,nazneen.khan@progenbusiness.com";
        }
        if (emailId != null && !emailId.equalsIgnoreCase("")) {
            String[] mailids = emailId.split(",");
            for (int i = 0; i < mailids.length; i++) {
                params = new PbMailParams();
                params.setBodyText("ETL FAILED !!! CONNECTION ERROR FOR......" + connDetails);
                params.setToAddr(mailids[i]);
                params.setSubject(" ETL FAILED ");
                params.setHasAttach(false);

                try {
                    mailer = new PbMail(params);
                    Boolean b = mailer.sendMail();
//                       
                    int count = 0;

                    while (!(b) && count < 5) {
                        b = mailer.sendMail();
                        count++;
//                              
                    }
                } catch (Exception e) {
                    logger.error("Exception: ", e);
                }
            }
        }
    }

    public void sendSchedulerStartMail(ReportSchedule schedule) {
        String loadId = String.valueOf(schedule.getSchedulerLoadId());
        String loadType = String.valueOf(schedule.getSchedulerLoadType());
        String loadDate = String.valueOf(schedule.getSchedulerLoadStartDate());
        String loadTime = String.valueOf(schedule.getSchedulerLoadTime());
        String emailId = String.valueOf(schedule.getSchedulerLoadEmailId());
        if (emailId != null && !emailId.equalsIgnoreCase("")) {
            String[] mailids = emailId.split(",");

            for (int i = 0; i < mailids.length; i++) {
                params = new PbMailParams();
                params.setBodyText("Load Started For---------------------->LoadId:" + loadId + "       loadType:" + loadType + "       LoadDate:" + loadDate + "   LoadStartTime:" + loadTime);
                params.setToAddr(mailids[i]);
                params.setSubject("LoadScheduler " + loadType + " " + loadId);
                params.setHasAttach(false);

                try {
                    mailer = new PbMail(params);
                    Boolean b = mailer.sendMail();
//                       
                    int count = 0;

                    while (!(b) && count < 5) {
                        b = mailer.sendMail();
                        count++;
//                              
                    }
                    if (("JKPLoad".equalsIgnoreCase(loadType)) || ("DFLoad".equalsIgnoreCase(loadType))) {
                    }
//                        else
//                        {ScheduleLogger.addLogEntry(schedule.getSchedulerLoadId(), "Scheduler", "Success");}

                } catch (Exception e) {
                    logger.error("Exception: ", e);
                    ScheduleLogger.addLogEntry(schedule.getSchedulerLoadId(), "Scheduler", "Failed");
                }
            }
        }
    }

    public void sendSchedulerFinishMail(ReportSchedule schedule, String file_name) {
        String loadId = String.valueOf(schedule.getSchedulerLoadId());
        String loadType = String.valueOf(schedule.getSchedulerLoadType());
        String loadDate = String.valueOf(schedule.getSchedulerLoadStartDate());
        String loadTime = String.valueOf(schedule.getSchedulerLoadTime());
        String emailId = String.valueOf(schedule.getSchedulerLoadEmailId());
        if (emailId != null && !emailId.equalsIgnoreCase("")) {
            String[] mailids = emailId.split(",");
            String str = "";
            if (file_name.equalsIgnoreCase("")) {
                str = System.getProperty("java.io.tmpdir") + "/" + LogReadWriter.file_name + ".txt";
            } else {
                str = System.getProperty("java.io.tmpdir") + "/" + file_name + ".txt";
            }
            ArrayList<String> attch = new ArrayList<String>();
            attch.add(str);

            for (int i = 0; i < mailids.length; i++) {
                params = new PbMailParams();
                params.setBodyText("Load Completed For---------------------->LoadId:" + loadId + "       loadType:" + loadType + "       LoadDate:" + loadDate + "   LoadStartTime:" + loadTime);
                params.setToAddr(mailids[i]);
                params.setSubject("LoadScheduler " + loadType + " " + loadId);
                params.setHasAttach(true);
                params.setAttachFile(attch);

                try {
                    mailer = new PbMail(params);
                    Boolean b = mailer.sendMail();
//                     added by mohit  
                    int count = 0;

                    while (!(b) && count < 5) {
                        b = mailer.sendMail();
                        count++;
//                              
                    }
                    if (("JKPLoad".equalsIgnoreCase(loadType)) || ("DFLoad".equalsIgnoreCase(loadType))) {
                    }
//                        else
//                        {
//                        ScheduleLogger.addLogEntry(schedule.getSchedulerLoadId(), "Scheduler", "Success");}
                } catch (Exception e) {
                    logger.error("Exception: ", e);
                    ScheduleLogger.addLogEntry(schedule.getSchedulerLoadId(), "Scheduler", "Failed");
                }
            }
        }
    }
    //Function added by Amar to reschedule Scheduler after ETL completed

    public void scheduleJobAfterEtlCompleted() {
        logger.info("==================scheduleJobAfterEtlCompleted start==================");
        PbReportViewerDAO dao = new PbReportViewerDAO();
        try {
            SchedulerBD objPlugin = new SchedulerBD();
//            PbReturnObject retobj = null;//bcz of dataflow merging
            PbReturnObject retobj = dao.getAllSchedulerAfetrEtldetails();
            ReportSchedule schedule = null;
            Date today = new Date();
            Calendar currentDate = Calendar.getInstance();
            int sysHr = currentDate.get(Calendar.HOUR_OF_DAY);
            int sysMin = currentDate.get(Calendar.MINUTE);
            int tmpMin = 0;
            int tmpHr = 0;
            String scheduleTime = "";
            String[] splitScheduleTime = null;
            if (retobj != null && retobj.getRowCount() > 0) {

                for (int i = 0; i < retobj.getRowCount(); i++) {
                    Gson json = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<ReportSchedule>>() {
                    }.getType();
                    List<ReportSchedule> scheduleList = json.fromJson(retobj.getFieldValueString(i, "SCHEDULER_DETAILS"), type);
                    Date sdate = retobj.getFieldValueDate(i, "SCHEDULE_START_DATE");
                    Date edate = retobj.getFieldValueDate(i, "SCHEDULE_END_DATE");
                    //Date lastSentdate = retobj.getFieldValueDate(i, "LAST_SENT_DATE");

                    int aftrEtlTime = retobj.getFieldValueAfterTime(i, "AFTER_ETL_TIME");
                    if (scheduleList != null && !scheduleList.isEmpty()) {
                        for (ReportSchedule schedule1 : scheduleList) {
                            schedule = schedule1;
                            if (today.before(edate)) {
                                scheduleTime = schedule.getScheduledTime();
                                splitScheduleTime = scheduleTime.split(":");
                                //sysMin = sysMin + aftrEtlTime;
                                tmpMin = sysMin + aftrEtlTime;
                                if (tmpMin >= 60) {
                                    tmpHr = sysHr + tmpMin / 60;
                                    tmpMin = tmpMin % 60;
                                } else {
                                    tmpHr = sysHr;
                                }
                                scheduleTime = String.valueOf(tmpHr).concat(":").concat(String.valueOf(tmpMin));
                                schedule.setScheduledTime(scheduleTime);
                            }

                            schedule.setStartDate(sdate);
                            schedule.setEndDate(edate);

                        }
                    }

                    if (today.before(edate)) {
                        objPlugin.scheduleReport(schedule, true);
                    }
                    logger.info("****Current Time:" + new Date() + "==== ScheduleJob AfterEtl is " + i + ". " + schedule.getSchedulerName() + " ====scheduled at====: " + scheduleTime);
                }
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    public void sendSchedulerEtlFailedMail(ReportSchedule schedule, String connDetails, String file_name) {
        String loadId = String.valueOf(schedule.getSchedulerLoadId());
        String loadType = String.valueOf(schedule.getSchedulerLoadType());
        String loadDate = String.valueOf(schedule.getSchedulerLoadStartDate());
        String loadTime = String.valueOf(schedule.getSchedulerLoadTime());
        String emailId = String.valueOf(schedule.getSchedulerLoadEmailId());
        if (emailId != null && !emailId.equalsIgnoreCase("")) {
            String[] mailids = emailId.split(",");
            String str = "";
            if (file_name.equalsIgnoreCase("")) {
                str = System.getProperty("java.io.tmpdir") + "/" + LogReadWriter.file_name + ".txt";
            } else {
                str = System.getProperty("java.io.tmpdir") + "/" + file_name + ".txt";
            }
            ArrayList<String> attch = new ArrayList<String>();
            attch.add(str);
            for (int i = 0; i < mailids.length; i++) {
                params = new PbMailParams();
                params.setBodyText("Load Failed For---------------------->LoadId:" + loadId + "       loadType:" + loadType + "       LoadDate:" + loadDate + "   LoadStartTime:" + loadTime
                        + " ..<html><body><br> " + connDetails + " </body></html>");
                params.setToAddr(mailids[i]);
                params.setSubject("ETL FAILED FOR " + loadType + " " + loadId);
                params.setHasAttach(true);
                params.setAttachFile(attch);

                try {
                    mailer = new PbMail(params);
                    Boolean b = mailer.sendMail();
//                       logger.info("hello.........."+b);
                    int count = 0;

                    while (!(b) && count < 5) {
                        b = mailer.sendMail();
                        count++;
//                              logger.info("hello.........."+count);
                    }
                    if (("JKPLoad".equalsIgnoreCase(loadType)) || ("DFLoad".equalsIgnoreCase(loadType))) {
                    }
//                        else
//                        {ScheduleLogger.addLogEntry(schedule.getSchedulerLoadId(), "Scheduler", "Success");}

                } catch (Exception e) {
                    logger.error("Exception: ", e);
                    ScheduleLogger.addLogEntry(schedule.getSchedulerLoadId(), "Scheduler", "Failed");
                }
            }
        }
    }

    public void sendSchedulerCustomMail(ReportSchedule schedule, String subject, String text, String file_name) {
        String loadId = String.valueOf(schedule.getSchedulerLoadId());
        String loadType = String.valueOf(schedule.getSchedulerLoadType());
        String loadDate = String.valueOf(schedule.getSchedulerLoadStartDate());
        String loadTime = String.valueOf(schedule.getSchedulerLoadTime());
        String emailId = String.valueOf(schedule.getSchedulerLoadEmailId());
        if (emailId != null && !emailId.equalsIgnoreCase("")) {
            String[] mailids = emailId.split(",");
            String str = "";
            if (file_name.equalsIgnoreCase("")) {
                str = System.getProperty("java.io.tmpdir") + "/" + LogReadWriter.file_name + ".txt";
            } else {
                str = System.getProperty("java.io.tmpdir") + "/" + file_name + ".txt";
            }
            ArrayList<String> attch = new ArrayList<String>();
            attch.add(str);
            for (int i = 0; i < mailids.length; i++) {
                params = new PbMailParams();
                params.setBodyText("Load Failed For---------------------->LoadId:" + loadId + "       loadType:" + loadType + "       LoadDate:" + loadDate + "   LoadStartTime:" + loadTime
                        + " ..<html><body><br> " + text + " </body></html>");
                params.setToAddr(mailids[i]);
                params.setSubject(subject + " for " + loadType + " " + loadId);
                params.setHasAttach(true);
                params.setAttachFile(attch);

                try {
                    mailer = new PbMail(params);
                    Boolean b = mailer.sendMail();
                    int count = 0;
                    while (!(b) && count < 5) {
                        b = mailer.sendMail();
                        count++;
                    }
                } catch (Exception e) {
                    logger.error("Exception: ", e);
                    ScheduleLogger.addLogEntry(schedule.getSchedulerLoadId(), "Scheduler", "Failed");
                }
            }
        }
    }

    public void delayReportSchedulerMailIfEtlFailed() {

        String emailId = "vishal.upreti@dataflowgroup.com,shubhonita@dataflowgroup.com,gauri.sharma@dataflowgroup.com,divya.vaish@dataflowgroup.com,rajinder.sejwal@dataflowgroup.com,issam.hattar@dataflowgroup.com,aali@dataflowgroup.com,dima@dataflowgroup.com,suja.kiran@dataflowgroup.com,suniel.nambiar@dataflowservices.com,bandi.sunder@dataflowservices.com,abhinav.singh@dataflowgroup.com,vikas.sharma@dataflowgroup.com,mkumar@dataflowgroup.com,nasim.ahmed@dataflowgroup.com,pmattas@dataflowgroup.com,preeti.bhardwaj@dataflowgroup.com,alaa@dataflowgroup.com,michelle.ibarra@dataflowgroup.com,michelle.espiritu@dataflowgroup.com,emenichetti@dataflowgroup.com,skumar@dataflowgroup.com,amit.naik@dataflowgroup.com,shankho.mitra@dataflowgroup.com,sangeeta.saini@dataflowgroup.com,schiang@dataflowgroup.com,vikas.mishra@dataflowgroup.com,brajpal.shishodia@dataflowgroup.com,dhiraj.kumar@dataflowgroup.com,sanjeev.rathore@dataflowgroup.com,monika.agrawal@progenbusiness.com,amit@progenbusiness.com,mohit.jain@progenbusiness.com,dinanath.parit@progenbusiness.com";
//        String emailId = "dinanath.parit@progenbusiness.com,mohit.jain@progenbusiness.com";
        String bodyTextMessage = "Good Morning All,"
                + "Kindly except some delay in reports from Insight as due to some technical issue, ETL has Failed.<br/>\n"
                + "Team has restarted the activity and reports would be rolled as soon as possible.<br/>\n"
                + "Kindly let us know for any further issue/information.<br/>\n"
                + "<br/>\n"
                + "<br/>\n"
                + "Thanks <br/>\n"
                + "Insight Team<br/> ";
        params = new PbMailParams();
        params.setBodyText(bodyTextMessage);
        params.setToAddr(emailId);
        params.setSubject("ETL Failed. Insight Reports might be delayed.");
        params.setHasAttach(false);
//                params.setAttachFile(attch);
        try {
            mailer = new PbMail(params);
            Boolean b = mailer.sendMail();
        } catch (Exception e) {
            logger.error("Exception: ", e);

        }
    }

    public void ETL_Running_Slow() {
        try {
            EtlLoadForJKP jkp = new EtlLoadForJKP();
            Date etl_completed_date = jkp.getEtlCompletedDate();
            Date Etl_Failed_Date = jkp.getEtlFailedDate();
            Date Etl_Started_Date = jkp.getEtlStartedDate();
            if (etl_completed_date != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String todaydt = sdf.format(date);
                String etl_completed_date2 = sdf.format(etl_completed_date);
                String Etl_Failed_Date2 = null;
                if (Etl_Failed_Date != null) {
                    Etl_Failed_Date2 = sdf.format(Etl_Failed_Date);
                }
                String Etl_Started_Date2 = null;
                if (Etl_Started_Date != null) {
                    Etl_Started_Date2 = sdf.format(Etl_Started_Date);
                }

                if (Etl_Failed_Date2 != null && Etl_Started_Date2 != null && Etl_Failed_Date2.equalsIgnoreCase(todaydt) && Etl_Started_Date2.equalsIgnoreCase(todaydt)) {
                    logger.info("DFLoad ETL has failed so etl running slow mail has avoided.");
                } else if (etl_completed_date2 != null && etl_completed_date2.equalsIgnoreCase(todaydt)) {
                    logger.info("Fine! Etl Completed before 9 am.");
                } else if (Etl_Started_Date2 != null && (Etl_Started_Date2.equalsIgnoreCase(todaydt) && !etl_completed_date2.equalsIgnoreCase(todaydt) && !Etl_Failed_Date2.equalsIgnoreCase(todaydt))) {
                    String emailId = "vishal.upreti@dataflowgroup.com,shubhonita@dataflowgroup.com,gauri.sharma@dataflowgroup.com,divya.vaish@dataflowgroup.com,rajinder.sejwal@dataflowgroup.com,issam.hattar@dataflowgroup.com,aali@dataflowgroup.com,dima@dataflowgroup.com,suja.kiran@dataflowgroup.com,suniel.nambiar@dataflowservices.com,bandi.sunder@dataflowservices.com,abhinav.singh@dataflowgroup.com,vikas.sharma@dataflowgroup.com,mkumar@dataflowgroup.com,nasim.ahmed@dataflowgroup.com,pmattas@dataflowgroup.com,preeti.bhardwaj@dataflowgroup.com,alaa@dataflowgroup.com,michelle.ibarra@dataflowgroup.com,michelle.espiritu@dataflowgroup.com,emenichetti@dataflowgroup.com,skumar@dataflowgroup.com,amit.naik@dataflowgroup.com,shankho.mitra@dataflowgroup.com,sangeeta.saini@dataflowgroup.com,schiang@dataflowgroup.com,vikas.mishra@dataflowgroup.com,brajpal.shishodia@dataflowgroup.com,dhiraj.kumar@dataflowgroup.com,sanjeev.rathore@dataflowgroup.com,monika.agrawal@progenbusiness.com,amit@progenbusiness.com,mohit.jain@progenbusiness.com,dinanath.parit@progenbusiness.com";
//        String emailId = "dinanath.parit@progenbusiness.com,mohit.jain@progenbusiness.com";
                    String bodyTextMessage = "Good Morning All,"
                            + "Insight Reports would be delayed as due to some technical issue, ETL is running slow.<br/>\n"
                            + "Team is looking into the issue and as soon as issue resolves, reports would be rolled out.<br/>\n"
                            + "Kindly let us know for any further issue/information.<br/>\n"
                            + "<br/>\n"
                            + "<br/>\n"
                            + "Thanks <br/>\n"
                            + "Insight Team<br/> ";
                    params = new PbMailParams();
                    params.setBodyText(bodyTextMessage);
                    params.setToAddr(emailId);
                    params.setSubject("ETL Running Slow. Insight Reports are delayed.");
                    params.setHasAttach(false);
//                params.setAttachFile(attch);
                    try {
                        mailer = new PbMail(params);
                        Boolean b = mailer.sendMail();
                    } catch (Exception e) {
                        logger.error("Exception: ", e);

                    }
                }

            }
        } catch (Exception e) {
        }
    }
    public void commuLinkFailureOrDeadLockMail(int minuteDelay,String mainException,String exceptionType) {

        String emailId = "dataflow@progenbusiness.com,dinanath.parit@progenbusiness.com,mohit.jain@progenbusiness.com";
        String bodyTextMessage = "Good Morning All,"
                + "Kindly except some delay in reports from Insight as due to some technical issue,<br/>\n"
                + "Program will auto restart after " + minuteDelay + " minutes<br/>\n"
                + "The Main cause of Exception was :" + mainException + "<br/>\n"
                + "Kindly let us know for any further issue/information.<br/>\n"
                + "<br/>\n"
                + "<br/>\n"
                + "Thanks <br/>\n"
                + "Insight Team<br/> ";
        params = new PbMailParams();
        params.setBodyText(bodyTextMessage);
        params.setToAddr(emailId);
        params.setSubject("ETL " + exceptionType + " . Insight Reports might be delayed.");
        params.setHasAttach(false);
//                params.setAttachFile(attch);
        try {
            mailer = new PbMail(params);
            Boolean b = mailer.sendMail();
        } catch (Exception e) {
            logger.error("Exception: ", e);

        }
    }
}
