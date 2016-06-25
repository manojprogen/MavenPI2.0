/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.reportscheduler;

import com.progen.scheduler.AOSchedule;
import java.util.Arrays;
import java.util.Calendar;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import prg.util.PbMail;
import prg.util.PbMailParams;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import com.progen.report.query.PbReportQuery;
import com.progen.reportview.db.ProgenReportViewerDAO;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

/**
 *
 * @author Parsi Bhargavi
 */
public class AOSchedulerJob implements Job {

    public static Logger logger = Logger.getLogger(AOSchedulerJob.class);
    PbMailParams params = null;
    PbMail mailer = null;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDataMap dataMap = jec.getJobDetail().getJobDataMap();
        AOSchedule aoSchedule = (AOSchedule) dataMap.get("scheduleAO");
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("H:m");
        String dateNow = formatter.format(currentDate.getTime());
        if (aoSchedule.isIsAO() && aoSchedule.getScheduledTime().equalsIgnoreCase(dateNow)) {
            try {
                sendAOSchedulerMail(aoSchedule);
                System.out.println("its calling");
            } catch (Exception e) {
                logger.error("Scheduler Error", e);
            }
        }
    }

    public void sendAOSchedulerMail(AOSchedule aoSchedule) throws SQLException, Exception {
        logger.info("Executing AO scheduler");
        String schedulerName = aoSchedule.getAOSchedName();
        String schedulerType = aoSchedule.getLoadType();
        String schedulerId = aoSchedule.getAOSchedId();
        String dateType = aoSchedule.getDateType();
        String loadDate = aoSchedule.getloadStartDate();
        String loadEndDate = aoSchedule.getloadEndDate();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();
        String[] loadDateArray = null;
        String qryCreateTableAo = "";
        String cleanData = "";
        String mailIdsList[] = aoSchedule.getMailIds().split(",");
        HashMap<String, ArrayList> dataMap = new HashMap<>();
        PbReportQuery query = new PbReportQuery();
        Connection conn;
        ProgenParam connectionparam = new ProgenParam();
        PbDb pbdb = new PbDb();

        ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
        dao.getAOdetails(schedulerId, aoSchedule);
        dataMap = aoSchedule.getAOdetails();

        query.setRowViewbyCols(dataMap.get("viewIds"));
        query.setColViewbyCols(dataMap.get("colviewIds"));
        query.reportQryElementIds = dataMap.get("measId");
        query.setQryColumns(dataMap.get("measId"));
        query.isScheduleAO = true;
        query.setColAggration(dataMap.get("aggType"));
        ArrayList timedetails1 = new ArrayList();

        if (dateType.equalsIgnoreCase("Normal_Date")) {
            timedetails1.add("Day");
            timedetails1.add("PRG_STD");
            if (loadDate.contains("yestrday")) {

                cal.add(Calendar.DATE, -1);
                loadDate = formatter.format(cal.getTime());
            } else if (loadDate.contains("tomorow")) {

                cal.add(Calendar.DATE, 1);
                loadDate = formatter.format(cal.getTime());
            }
            if (loadDate.contains("newSysDate")) {
                loadDateArray = loadDate.split(",");
                cal.add(Calendar.DATE, Integer.parseInt(loadDateArray[1]));
                loadDate = formatter.format(cal.getTime());

            } else if (loadDate.contains("globalDate")) {

                PbDb pbDB = new PbDb();
                PbReturnObject PbRetObj;
                String query1 = "select setup_date_value from prg_gbl_setup_values where setup_key='SYSTEM_DATE'";
                PbRetObj = pbDB.execSelectSQL(query1);
                if (PbRetObj.rowCount > 0) {
                    cal.setTime(PbRetObj.getFieldValueDate(0, 0));
                }
                loadDateArray = loadDate.split(",");
                cal.add(Calendar.DATE, Integer.parseInt(loadDateArray[1]));
                loadDate = formatter.format(cal.getTime());
            }
            timedetails1.add(loadDate);
            timedetails1.add("DAY");
            timedetails1.add("LAST PERIOD");
            aoSchedule.setloadStartDate(loadDate);
            aoSchedule.setloadEndDate(loadDate);
        } else {
            timedetails1.add("Day");
            timedetails1.add("PRG_DATE_RANGE");
            for (int i = 0; i < 2; i++) {
//                if (i == 0) {
//                    loadDate = loadStartDate;
//                } else {
//                    loadDate = loadEndDate;
//                }
                if (loadDate.contains("yestrday")) {

                    cal.add(Calendar.DATE, -1);
                    loadDate = formatter.format(cal.getTime());
                } else if (loadDate.contains("tomorow")) {

                    cal.add(Calendar.DATE, 1);
                    loadDate = formatter.format(cal.getTime());
                }
                if (loadDate.contains("newSysDate")) {
                    loadDateArray = loadDate.split(",");
                    cal.add(Calendar.DATE, Integer.parseInt(loadDateArray[1]));
                    loadDate = formatter.format(cal.getTime());

                } else if (loadDate.contains("globalDate")) {

                    PbDb pbDB = new PbDb();
                    PbReturnObject PbRetObj;
                    String query1 = "select setup_date_value from prg_gbl_setup_values where setup_key='SYSTEM_DATE'";
                    PbRetObj = pbDB.execSelectSQL(query1);
                    if (PbRetObj.rowCount > 0) {
                        cal.setTime(PbRetObj.getFieldValueDate(0, 0));
                    }
                    loadDateArray = loadDate.split(",");
                    cal.add(Calendar.DATE, Integer.parseInt(loadDateArray[1]));
                    loadDate = formatter.format(cal.getTime());
                }
                if (i == 0) {
                    aoSchedule.setStartDateString(loadDate);
                    loadDate = loadEndDate;
                } else {
                    aoSchedule.setEndDateString(loadDate);
                }
            }
            timedetails1.add(aoSchedule.getloadStartDate());
            timedetails1.add(aoSchedule.getloadEndDate());
            timedetails1.add(aoSchedule.getloadStartDate());
            timedetails1.add(aoSchedule.getloadEndDate());
        }
        query.setTimeDetails(timedetails1);

        String finalQuery = query.generateViewByQry();

        String innerRepQry = query.finalSqlNew_AO;

        if (schedulerType.equalsIgnoreCase("Truncate and Load")) {

            cleanData = "TRUNCATE TABLE M_AO_" + schedulerId;
            if (innerRepQry != null && !innerRepQry.equalsIgnoreCase("")) {
                qryCreateTableAo = " insert into  M_AO_" + schedulerId + " " + innerRepQry;

            }

        } else if (schedulerType.equalsIgnoreCase("Delete and Load")) {
            switch (ProgenConnection.getInstance().getDatabaseType()) {
                case ProgenConnection.SQL_SERVER:
                    cleanData = "delete from M_AO_" + schedulerId + " where ddate between  convert(datetime,'" + aoSchedule.getStartDateString() + "',120) and  convert(datetime,'" + aoSchedule.getEndDateString() + "',120)";
                    break;
                case ProgenConnection.MYSQL:
                    cleanData = "delete from M_AO_" + schedulerId + " WHERE DDATE between  str_to_date('" + aoSchedule.getStartDateString() + "','%m/%d/%Y %H:%i:%s ') and  str_to_date('" + aoSchedule.getEndDateString() + "','%m/%d/%Y %H:%i:%s ')";
                    break;
                default:
                    cleanData = "delete from M_AO_" + schedulerId + " DDATE between  to_date('" + aoSchedule.getStartDateString() + "','mm/dd/yyyy Hh24:mi:ss ') and  to_date('" + aoSchedule.getEndDateString() + "','mm/dd/yyyy Hh24:mi:ss ')";
                    break;
            }

            if (innerRepQry != null && !innerRepQry.equalsIgnoreCase("")) {
                qryCreateTableAo = " insert into  M_AO_" + schedulerId + " " + innerRepQry;

            }

        } else if (innerRepQry != null && !innerRepQry.equalsIgnoreCase("")) {
            qryCreateTableAo = " insert into  M_AO_" + schedulerId + " " + innerRepQry;

        }

        if (!cleanData.equalsIgnoreCase("") || !cleanData.isEmpty()) {
            conn = connectionparam.getConnection(dataMap.get("measId").get(0).toString().replace("A_", ""));
            pbdb.execUpdateSQL(cleanData, conn);
        }

        qryCreateTableAo = getProgenTimeReplaces(aoSchedule.getloadStartDate(), aoSchedule.getloadEndDate(), qryCreateTableAo);
        qryCreateTableAo = getProgenTimeReplaces1(aoSchedule.getloadStartDate(), aoSchedule.getloadEndDate(), qryCreateTableAo);

        conn = connectionparam.getConnection(dataMap.get("measId").get(0).toString().replace("A_", ""));
        int count = pbdb.execUpdateSQL(qryCreateTableAo, conn);
        System.out.println("no of rows inserted" + count);
        StringBuilder completeContent = new StringBuilder();
        
completeContent.append("<html lang=\"en\"><body><div><span style=\"padding: 5px; background-color: #f2f2f2; color: green; font-style: normal;\">Dear Mr./Mrs.</span><br/><br/>" +
                            "<span style=\"padding: 5px; background-color: rgb(76, 175, 80); color: white; font-style: normal;\"><br/>");
completeContent.append("Number of record insert is").append(count).append("</div></body></html>");
        params = new PbMailParams();
        params.setBodyText(completeContent.toString());
        //params.setToAddr(mailIds);
        params.setSubject(schedulerName + " Scheduler is Completed successfully");
        params.setHasAttach(false);
        mailer = new PbMail(params);
        try {
            ArrayList<String> al = new ArrayList<String>();
            al.addAll(Arrays.asList(mailIdsList));
            params.setToAddr(al.toString().replace("[", "").replace("]", ""));
            mailer.sendMail();
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

        logger.info("Finished Executing AO scheduler");
    }

    public String getProgenTimeReplaces(String startDate, String endDate, String finalquery) {

        switch (ProgenConnection.getInstance().getDatabaseType()) {
            case ProgenConnection.SQL_SERVER:
                finalquery = finalquery.replace("convert(datetime,'" + startDate + "',120)", "@PROGENTIME@@ST_DATE");
                finalquery = finalquery.replace("convert(datetime,'" + endDate + "',120)", "@PROGENTIME@@ED_DATE");
                break;
            case ProgenConnection.MYSQL:
                finalquery = finalquery.replace("str_to_date('" + startDate + "','%m/%d/%Y %H:%i:%s ')", "@PROGENTIME@@ST_DATE");
                finalquery = finalquery.replace("str_to_date('" + endDate + "','%m/%d/%Y %H:%i:%s ')", "@PROGENTIME@@ED_DATE");
                break;
            default:
                finalquery = finalquery.replace("to_date('" + startDate + "','mm/dd/yyyy Hh24:mi:ss ')", "@PROGENTIME@@ST_DATE");
                finalquery = finalquery.replace("to_date('" + endDate + "','mm/dd/yyyy Hh24:mi:ss ')", "@PROGENTIME@@ED_DATE");
                break;
        }

        return finalquery;
    }

    public String getProgenTimeReplaces1(String startDate, String endDate, String finalquery) {

        switch (ProgenConnection.getInstance().getDatabaseType()) {
            case ProgenConnection.SQL_SERVER:
                finalquery = finalquery.replace("@PROGENTIME@@ST_DATE", "convert(datetime,'" + startDate + "',120)");
                finalquery = finalquery.replace("@PROGENTIME@@ED_DATE", "convert(datetime,'" + endDate + "',120)");
                break;
            case ProgenConnection.MYSQL:
                finalquery = finalquery.replace("@PROGENTIME@@ST_DATE", "str_to_date('" + startDate + "','%m/%d/%Y %H:%i:%s ')");
                finalquery = finalquery.replace("@PROGENTIME@@ED_DATE", "str_to_date('" + endDate + "','%m/%d/%Y %H:%i:%s ')");
                break;
            default:
                finalquery = finalquery.replace("@PROGENTIME@@ST_DATE", "to_date('" + startDate + "','mm/dd/yyyy Hh24:mi:ss ')");
                finalquery = finalquery.replace("@PROGENTIME@@ED_DATE", "to_date('" + endDate + "','mm/dd/yyyy Hh24:mi:ss ')");
                break;
        }

        return finalquery;
    }
}
