/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.reportscheduler;

import com.progen.report.DashboardChartData;
import com.progen.report.DateComperator;
import com.progen.report.JsonComperator;

import com.progen.report.PbReportCollectionResBunSqlServer;
import com.progen.report.PbReportCollectionResourceBundle;
import com.progen.report.PbReportCollectionResourceBundleMySql;
import com.progen.report.ReportManagementDAO;
import static com.progen.report.ReportManagementDAO.logger;
import com.progen.report.ReportObjectQuery;
import com.progen.report.XtendReportMeta;
import com.progen.report.query.ProgenAOQuery;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.scheduler.KPIAlertSchedule;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.PbMail;
import prg.util.PbMailParams;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

/**
 *
 * @author Dinanath Parit
 */
public class KpiChartSchedulerJob implements Job {

    public static Logger logger = Logger.getLogger(KpiChartSchedulerJob.class);
    HashMap<String, ArrayList<String>> timeDetailsMap;
    ArrayList timeDetailsArray;
    PbMailParams params = null;
    PbMail mailer = null;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDataMap dataMap = jec.getJobDetail().getJobDataMap();
        KPIAlertSchedule kpiSchedule = (KPIAlertSchedule) dataMap.get("scheduleReport");
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("H:m");
        String dateNow = formatter.format(currentDate.getTime());

        if (kpiSchedule.isIsFromKPIChart() && kpiSchedule.getScheduledTime().equalsIgnoreCase(dateNow)) {
            try {
                logger.info("calling  sendKPISchedulerMail(kpiSchedule) ");
                sendKPISchedulerMail(kpiSchedule);
            } catch (Exception e) {
                logger.error("KPIScheduler Error ", e);
            }
        }

    }
//code written by Dinanath
    public void sendKPISchedulerMail(KPIAlertSchedule kpiSchedule) {
        logger.info("Executing kpi scheduler");
        String reportId = kpiSchedule.getReportId();
        getTimeDetailsArray(reportId);//get timedetailsArray
        ArrayList<String> timeDetails = new ArrayList<String>();
        timeDetails = timeDetailsArray;
        String schedulerName = kpiSchedule.getKpiAlertSchedName();
        String schedulerId = kpiSchedule.getKpiAlertSchedId();
        String reportName = kpiSchedule.getReportName();
        String applyAsMeasureType = kpiSchedule.getMeasureType();
        String operatorList[] = kpiSchedule.getOperatorValuesArr();
        String sValuesList[] = kpiSchedule.getStartValuesArr();
        String eValuesList[] = kpiSchedule.getEndValuesArr();
        String mailIdsList[] = kpiSchedule.getMailIdsArr();

        String chartName = kpiSchedule.getChartName();

        String userId = kpiSchedule.getUserId();
        String filePath = kpiSchedule.getGlobalFilePath();
        String ctxpath = kpiSchedule.getContextPath();
        ////////////////////////////////////////////////
        Map<String, String> mapData = new HashMap<String, String>();
        Map<String, Map<String, String>> mainMapData = new HashMap<String, Map<String, String>>();
        mainMapData = kpiSchedule.getChartMapData();
        mapData = mainMapData.get(chartName);
        String viewBys = mapData.get("viewBys");
        String viewIds = mapData.get("viewIds");
        String dimensions = mapData.get("dimensions");
        String measName = mapData.get("measName");
        String measId = mapData.get("measId");
        String aggType = mapData.get("aggType");
        String defaultMeasures = mapData.get("defaultMeasures");
        String defaultMeasureIds = mapData.get("defaultMeasureIds");
        String chartType = mapData.get("chartType");
        String KPIName = mapData.get("KPIName");

        List<String> viewIdsList = new ArrayList();
        String viewIdsArr[] = viewIds.split(",");
        for (String vid : viewIdsArr) {
            viewIdsList.add(vid);
        }
        List<String> viewBysList = new ArrayList();
        String viewBysArr[] = viewBys.split(",");
        for (String viewbys : viewBysArr) {
            viewBysList.add(viewbys);
        }
        List<String> grandTotalAggType = new ArrayList();
        String percentType="";
        List<String> measureIdsList = new ArrayList();
        String measIds[] = measId.split(",");
        for (String msid : measIds) {
            if (applyAsMeasureType.equalsIgnoreCase("Change %")) {
                if (timeDetails.get(1).equalsIgnoreCase("PRG_STD") && timeDetails.get(3).equalsIgnoreCase("Month")) {
                    percentType="_MOMPer";
                    measureIdsList.add(msid + "_MOMPer");
                    grandTotalAggType.add("N");
                } else if (timeDetails.get(1).equalsIgnoreCase("PRG_STD") && timeDetails.get(3).equalsIgnoreCase("Year")) {
                    percentType="_YOYPer";
                    measureIdsList.add(msid + percentType);
                    grandTotalAggType.add("N");
                } else if (timeDetails.get(1).equalsIgnoreCase("PRG_STD") && timeDetails.get(3).equalsIgnoreCase("Quarter")) {
                    percentType="_QOQPer";
                    measureIdsList.add(msid + percentType);
                    grandTotalAggType.add("N");
                }

            } else {
                measureIdsList.add(msid);
                grandTotalAggType.add("N");
            }
        }

        List<String> measureNameList = new ArrayList();
        String measNames[] = measName.split(",");
        for (String measN : measNames) {
            measureNameList.add(measN);
        }

        List<String> aggTypeValuesList = new ArrayList();
        String aggTypeIds[] = aggType.split(",");
        for (String aggTypeN : aggTypeIds) {
            aggTypeValuesList.add(aggTypeN);
        }

        ReportTemplateDAO rtdao = new ReportTemplateDAO();
        String aoAsGoId = rtdao.GetaoAsGoId(reportId);

//        XtendReportMeta reportMeta = new XtendReportMeta();
//        Map<String, DashboardChartData> chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());
//        List<String> charts = new ArrayList(chartData.keySet());
        String chart = "";
//        for (String chart2 : charts) {
//            chart = chart2;
//        }
//        DashboardChartData chartDetails = reportMeta.getChartData().get(chart);

        ReportObjectQuery objectQuery2 = new ReportObjectQuery();
        HashMap<String, List> drillmapFromSet = new HashMap<String, List>();
        HashMap<String, List> filtermapFromSet = new HashMap<String, List>();
        HashMap<String, List> likeMap = new HashMap<String, List>();
        HashMap<String, List> notLikeMap = new HashMap<String, List>();
        String filterClause = objectQuery2.getFilterClause(drillmapFromSet, filtermapFromSet, likeMap, notLikeMap);
        //quering for gt
        ProgenAOQuery objQuery = new ProgenAOQuery();
        String query = "";
        if (aoAsGoId != null && !aoAsGoId.equalsIgnoreCase("")) {
            objQuery.setAO_name("M_AO_" + aoAsGoId);
        } else {
            objQuery.setAO_name("R_GO_" + reportId);
        }
        objQuery.setTimeDetails(timeDetails);
//        objQuery.setViewIdList(viewIdsList);
        objQuery.setViewIdList(null);
        objQuery.setColViewIdList(null);

        objQuery.setMeasureIdsList(measureIdsList);
        objQuery.setMeasureIdsListGO(measureIdsList);

        setTimeTypeForMeasure(measureIdsList, objQuery);

        objQuery.setFilterClause(filterClause);
        objQuery.setFilterMap(filtermapFromSet);
        objQuery.setAggregationType(aggTypeValuesList);
        objQuery.setInnerViewByElementId("NONE");
        objQuery.setGrandTotalAggType(grandTotalAggType);
//                objQuery.setTimeDetailsArray(measureType);
        try {
            query = objQuery.generateAOQuery();
        } catch (SQLException ex) {
            logger.error("Exception ss: ", ex);
        }
        PbReturnObject pbretObj = null;
        PbReturnObject pbretObjMsr = null;
        Connection conn = null;
        PbDb pbdb = new PbDb();
        if (viewIdsList.get(0).trim().equalsIgnoreCase("TIME")) {
            conn = ProgenConnection.getInstance().getConnectionForElement(measureIdsList.get(0));
        } else {
            conn = ProgenConnection.getInstance().getConnectionForElement(viewIdsList.get(0));
        }
        if (conn != null) {
            try {
                //modified by anitha
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                    ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
                    ArrayList<String> sessionQueryLst = new ArrayList<String>();
                    try {
                        sessionQueryLst = reportTemplateDAO.getSessionQueriesForOracle();
                    } catch (Exception e) {
                        logger.error("Exception:", e);
                    }
                    pbretObj = pbdb.execSelectORCL(query, conn, sessionQueryLst);
                } else {
                    pbretObj = pbdb.execSelectSQL(query, conn);
                }
                //end of code by anitha

            } catch (Exception ex) {
                logger.error("Exception while getting GTValue:", ex);
            }
        }

        //quering for gt
        Map<String, Map> mainRowWiseVal = new HashMap<String, Map>();
        for (int n = 0; n < viewIdsList.size(); n++) {
            Map<Integer, List<String>> rowWiseVal = new HashMap<Integer, List<String>>();
            ProgenAOQuery objQueryForMsrVal = new ProgenAOQuery();
            String query2ForMsrVal = "";
            if (aoAsGoId != null && !aoAsGoId.equalsIgnoreCase("")) {
                objQueryForMsrVal.setAO_name("M_AO_" + aoAsGoId);
            } else {
                objQueryForMsrVal.setAO_name("R_GO_" + reportId);
            }
            objQueryForMsrVal.setTimeDetails(timeDetails);
//        objQuery.setViewIdList(viewIdsList);
            List<String> viewIdsListSingle = new ArrayList();
            viewIdsListSingle.add(viewIdsList.get(n));
            objQueryForMsrVal.setViewIdList(viewIdsListSingle);
            objQueryForMsrVal.setColViewIdList(null);
            objQueryForMsrVal.setMeasureIdsList(measureIdsList);
            objQueryForMsrVal.setMeasureIdsListGO(measureIdsList);
            setTimeTypeForMeasure(measureIdsList, objQueryForMsrVal);

            objQueryForMsrVal.setFilterClause(filterClause);
            objQueryForMsrVal.setFilterMap(filtermapFromSet);
            objQueryForMsrVal.setAggregationType(aggTypeValuesList);
            objQueryForMsrVal.setInnerViewByElementId("NONE");
            objQueryForMsrVal.setGrandTotalAggType(grandTotalAggType);
//                objQuery.setTimeDetailsArray(measureType);
            try {
                query2ForMsrVal = objQueryForMsrVal.generateAOQuery();
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }

            if (viewIdsList.get(0).trim().equalsIgnoreCase("TIME")) {
                conn = ProgenConnection.getInstance().getConnectionForElement(measureIdsList.get(0));
            } else {
                conn = ProgenConnection.getInstance().getConnectionForElement(viewIdsList.get(0));
            }
            if (conn != null) {
                try {
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
                        ArrayList<String> sessionQueryLst = new ArrayList<String>();
                        try {
                            sessionQueryLst = reportTemplateDAO.getSessionQueriesForOracle();
                        } catch (Exception e) {
                            logger.error("Exception:", e);
                        }
                        pbretObjMsr = pbdb.execSelectORCL(query2ForMsrVal, conn, sessionQueryLst);
                    } else {
                        pbretObjMsr = pbdb.execSelectSQL(query2ForMsrVal, conn);
                    }
                    String colNamesMsr[] = pbretObjMsr.getColumnNames();

                    List<String> viewByValFirst = new ArrayList();
                    viewByValFirst.add(viewBysList.get(n));
                    viewByValFirst.add(measureNameList.get(0));
                    rowWiseVal.put(0, viewByValFirst);

                    for (int i = 1; i <= pbretObjMsr.getRowCount(); i++) {
                        List<String> viewByVal = new ArrayList();
//                        for (int m = 0; m < colNamesMsr.length; m++) {

                        viewByVal.add(pbretObjMsr.getFieldValueString((i - 1), "A_" + viewIdsList.get(n)));
                        if(applyAsMeasureType.equalsIgnoreCase("Change %")){
                        viewByVal.add(pbretObjMsr.getFieldValueString((i - 1), "A_" + measureIdsList.get(0)));
                        }else{
                            viewByVal.add(pbretObjMsr.getFieldValueString((i - 1), "A_" + measureIdsList.get(0)));
                        }
//                        }
                        rowWiseVal.put(i, viewByVal);
                    }
                    mainRowWiseVal.put(viewIdsList.get(n), rowWiseVal);

                } catch (Exception ex) {
                    logger.error("Exception while executing query and process data:", ex);
                }
            }
        }//viewBy for
        ////////////////////////////////////

        ///////////
        String colNames[] = pbretObj.getColumnNames();
        ArrayList<String> avgGT = new ArrayList();
        if (pbretObj.getRowCount()>0) {
            if(applyAsMeasureType.equalsIgnoreCase("Change %")){
                avgGT.add(pbretObj.getFieldValueString(0, "A_" + measureIdsList.get(0)));
            }else{
                avgGT.add(pbretObj.getFieldValueString(0, 1));
        }
        }
        //////////

        /////////
        BigDecimal averageGTDecimal = new BigDecimal(avgGT.get(0));
        String averageGT = getModifidNumber(averageGTDecimal);
        Double averageGTotal = Double.valueOf(averageGT);
        StringBuilder completeContent = new StringBuilder();
        completeContent.append("<html lang=\"en\">\n"
                + "<head>\n"
                + "<style>\n"
                + "table {\n"
                + "    border-collapse: collapse;\n"
                + "    width: 100%;\n"
                + "}\n"
                + "\n"
                + "th, td {\n"
                + "    text-align: left;\n"
                + "    padding: 8px;\n"
                + "}\n"
                + "table tr th{background-color: #ff5722;color:#fff}\n"
                + "tr:nth-child(even){background-color: #f2f2f2}\n"
                + "</style>\n"
                + "</head>"
                + "<body>");
        boolean isSendMailFlag = false;
        for (int i = 0; i < operatorList.length; i++) {
            Double compGTWithStartValue = Double.valueOf(sValuesList[i]);

            if (operatorList[i].equalsIgnoreCase("<")) {
                if (averageGTotal < compGTWithStartValue) {
                     isSendMailFlag = true;
                    completeContent.append("<div><span style=\"padding: 5px; background-color: #f2f2f2; color: green; font-style: normal;\">Dear Sir/Madam,</span><br/><br/>"
                            + "<span style=\"padding: 5px; background-color: #42a5f5; color: white; font-style: normal;\">Report: <b>").append(reportName).append("</b></span><br/><br/>"
                            + "<span style=\"padding: 5px; background-color: #b71c1c; color: white; font-style: normal;\">Alert for <b>").append(measureNameList.get(0)).append("</b></span>"
                            + "<br/><br/>" + "<table>" + "<thead><tr><th>Target</th><th>Achieved</th><th>Alert Type</th></thead></tr>" + "<tr style='background-color:#ffccbc;'><td>").append("Value should be ").append(operatorList[i]).append(" ").append(compGTWithStartValue).append("</td><td>").append(getModifidNumber(averageGTDecimal)).append("</td><td></td></tr>"
                            + "</table><br/>");
                    for (String key : mainRowWiseVal.keySet()) {
                        Map<String, Double> sortedMap = new HashMap<String, Double>();
                        Map<Integer, List<String>> rowWiseVal2 = new HashMap<Integer, List<String>>();
                        logger.info("viewById table:"+key);
                        rowWiseVal2 = mainRowWiseVal.get(key);
                                                ///
                         for (int s = 1; s < rowWiseVal2.size(); s++) {
                           
                            List<String> allRowV = rowWiseVal2.get(s);
                            for (int k = 0; k < allRowV.size(); k++) {
                                sortedMap.put(allRowV.get(0),Double.parseDouble(allRowV.get(1).isEmpty()? "0" : allRowV.get(1)));
                            }
                        }
                         Map<String, Double> finalMapRes = new LinkedHashMap<String, Double>();
                         finalMapRes=sortMapValueAscAndDescByValue(sortedMap, compGTWithStartValue);
             
                        //
                        completeContent.append("<div id='").append(key).append("' style=\"min-height: 20px;max-height: 300px;overflow:auto;\"><table>");
                        completeContent.append("<thead><tr>");
                        for (int s = 0; s < 1; s++) {
                            List<String> allRowV = rowWiseVal2.get(s);
                            for (int k = 0; k < allRowV.size(); k++) {
                                completeContent.append("<th>").append(allRowV.get(k)).append("</th>");
                            }
                        }
                        completeContent.append("</tr></thead><tbody>");
//                        for (int s = 1; s < rowWiseVal2.size(); s++) {
//                          if (s % 2 == 0) {
//                                completeContent.append("<tr style='background-color:#fbe9e7;'>");
//                            } else {
//                                completeContent.append("<tr style='background-color:#ffccbc;'>");
//                            }
//                            List<String> allRowV = rowWiseVal2.get(s);
//                            for (int k = 0; k < allRowV.size(); k++) {
//                                completeContent.append("<td>").append(allRowV.get(k)).append("</td>");
//                            }
//                            completeContent.append("</tr>");
//                        }
                         int iii=0;
                         for (Map.Entry<String, Double> entry : finalMapRes.entrySet()) {
                            System.out.println("finalMapRes Key = " + entry.getKey() + ", finalMapRes Value = " + entry.getValue());
                                if (iii % 2 == 0) {
                                completeContent.append("<tr style='background-color:#fbe9e7;'>");
                            } else {
                                completeContent.append("<tr style='background-color:#ffccbc;'>");
                            }
                           
                                completeContent.append("<td>").append(entry.getKey()).append("</td>");
                                completeContent.append("<td>").append(entry.getValue()).append("</td>");
                          
                            completeContent.append("</tr>");
                            iii++;
                         }
                        completeContent.append("</tbody></table></div><br/><br/>");
                    }
                    completeContent.append("</div>");
                } else {
                    isSendMailFlag = false;
                }
            } else if (operatorList[i].equalsIgnoreCase(">")) {
                if (averageGTotal > compGTWithStartValue) {
                    isSendMailFlag = true;
                    completeContent.append("<div><span style=\"padding: 5px; background-color: #f2f2f2; color: green; font-style: normal;\">Dear Sir/Madam,</span><br/><br/>"
                            + "<span style=\"padding: 5px; background-color: #42a5f5; color: white; font-style: normal;\">Report: <b>").append(reportName).append("</b></span><br/><br/>"
                            + "<span style=\"padding: 5px; background-color: #b71c1c; color: white; font-style: normal;\">Alert for <b>").append(measureNameList.get(0)).append("</b></span>"
                            + "<br/><br/>" + "<table>" + "<thead><tr><th>Target</th><th>Achieved</th><th>Alert Type</th></thead></tr>" + "<tr style='background-color:#ffccbc;'><td>").append("Value should be ").append(operatorList[i]).append(" ").append(compGTWithStartValue).append("</td><td>").append(getModifidNumber(averageGTDecimal)).append("</td><td></td></tr>"
                            + "</table><br/>");
                    for (String key : mainRowWiseVal.keySet()) {
                        Map<String, Double> sortedMap = new HashMap<String, Double>();
                        Map<Integer, List<String>> rowWiseVal2 = new HashMap<Integer, List<String>>();
                        logger.info("viewById table:"+key);
                        rowWiseVal2 = mainRowWiseVal.get(key);
                                                ///
                         for (int s = 1; s < rowWiseVal2.size(); s++) {
                           
                            List<String> allRowV = rowWiseVal2.get(s);
                            for (int k = 0; k < allRowV.size(); k++) {
                                sortedMap.put(allRowV.get(0),Double.parseDouble(allRowV.get(1).isEmpty()? "0" : allRowV.get(1)));
                            }
                        }
                         Map<String, Double> finalMapRes = new LinkedHashMap<String, Double>();
                         finalMapRes=sortMapValueAscAndDescByValue(sortedMap, compGTWithStartValue);
             
                        //
                        completeContent.append("<div id='").append(key).append("' style=\"min-height: 20px;max-height: 300px;overflow:auto;\"><table>");
                        completeContent.append("<thead><tr>");
                        for (int s = 0; s < 1; s++) {
                            List<String> allRowV = rowWiseVal2.get(s);
                            for (int k = 0; k < allRowV.size(); k++) {
                                completeContent.append("<th>").append(allRowV.get(k)).append("</th>");
                            }
                        }
                        completeContent.append("</tr></thead><tbody>");
//                        for (int s = 1; s < rowWiseVal2.size(); s++) {
//                          if (s % 2 == 0) {
//                                completeContent.append("<tr style='background-color:#fbe9e7;'>");
//                            } else {
//                                completeContent.append("<tr style='background-color:#ffccbc;'>");
//                            }
//                            List<String> allRowV = rowWiseVal2.get(s);
//                            for (int k = 0; k < allRowV.size(); k++) {
//                                completeContent.append("<td>").append(allRowV.get(k)).append("</td>");
//                            }
//                            completeContent.append("</tr>");
//                        }
                         int iii=0;
                         for (Map.Entry<String, Double> entry : finalMapRes.entrySet()) {
                            System.out.println("finalMapRes Key = " + entry.getKey() + ", finalMapRes Value = " + entry.getValue());
                                if (iii % 2 == 0) {
                                completeContent.append("<tr style='background-color:#fbe9e7;'>");
                            } else {
                                completeContent.append("<tr style='background-color:#ffccbc;'>");
                            }
                           
                                completeContent.append("<td>").append(entry.getKey()).append("</td>");
                                completeContent.append("<td>").append(entry.getValue()).append("</td>");
                          
                            completeContent.append("</tr>");
                            iii++;
                         }
                        completeContent.append("</tbody></table></div><br/><br/>");
                    }
                    completeContent.append("</div>");
                } else {
                    isSendMailFlag = false;
                }
            } else if (operatorList[i].equalsIgnoreCase("<=")) {
                if (averageGTotal <= compGTWithStartValue) {
                   isSendMailFlag = true;
                    completeContent.append("<div><span style=\"padding: 5px; background-color: #f2f2f2; color: green; font-style: normal;\">Dear Sir/Madam,</span><br/><br/>"
                           + "<span style=\"padding: 5px; background-color: #42a5f5; color: white; font-style: normal;\">Report: <b>").append(reportName).append("</b></span><br/><br/>"
                                   + "<span style=\"padding: 5px; background-color: #b71c1c; color: white; font-style: normal;\">Alert for <b>").append(measureNameList.get(0)).append("</b></span>"
                                           + "<br/><br/>" + "<table>" + "<thead><tr><th>Target</th><th>Achieved</th><th>Alert Type</th></thead></tr>" + "<tr style='background-color:#ffccbc;'><td>").append("Value should be ").append(operatorList[i]).append(" ").append(compGTWithStartValue).append("</td><td>").append(getModifidNumber(averageGTDecimal)).append("</td><td></td></tr>"
                            + "</table><br/>");
                    for (String key : mainRowWiseVal.keySet()) {
                        Map<String, Double> sortedMap = new HashMap<String, Double>();
                        Map<Integer, List<String>> rowWiseVal2 = new HashMap<Integer, List<String>>();
                        logger.info("viewById table:"+key);
                        rowWiseVal2 = mainRowWiseVal.get(key);
                                                ///
                         for (int s = 1; s < rowWiseVal2.size(); s++) {
                           
                            List<String> allRowV = rowWiseVal2.get(s);
                            for (int k = 0; k < allRowV.size(); k++) {
                                sortedMap.put(allRowV.get(0),Double.parseDouble(allRowV.get(1).isEmpty()? "0" : allRowV.get(1)));
                            }
                        }
                         Map<String, Double> finalMapRes = new LinkedHashMap<String, Double>();
                         finalMapRes=sortMapValueAscAndDescByValue(sortedMap, compGTWithStartValue);
             
                        //
                        completeContent.append("<div id='").append(key).append("' style=\"min-height: 20px;max-height: 300px;overflow:auto;\"><table>");
                        completeContent.append("<thead><tr>");
                        for (int s = 0; s < 1; s++) {
                            List<String> allRowV = rowWiseVal2.get(s);
                            for (int k = 0; k < allRowV.size(); k++) {
                                completeContent.append("<th>").append(allRowV.get(k)).append("</th>");
                            }
                        }
                        completeContent.append("</tr></thead><tbody>");
//                        for (int s = 1; s < rowWiseVal2.size(); s++) {
//                          if (s % 2 == 0) {
//                                completeContent.append("<tr style='background-color:#fbe9e7;'>");
//                            } else {
//                                completeContent.append("<tr style='background-color:#ffccbc;'>");
//                            }
//                            List<String> allRowV = rowWiseVal2.get(s);
//                            for (int k = 0; k < allRowV.size(); k++) {
//                                completeContent.append("<td>").append(allRowV.get(k)).append("</td>");
//                            }
//                            completeContent.append("</tr>");
//                        }
                         int iii=0;
                         for (Map.Entry<String, Double> entry : finalMapRes.entrySet()) {
                            System.out.println("finalMapRes Key = " + entry.getKey() + ", finalMapRes Value = " + entry.getValue());
                                if (iii % 2 == 0) {
                                completeContent.append("<tr style='background-color:#fbe9e7;'>");
                            } else {
                                completeContent.append("<tr style='background-color:#ffccbc;'>");
                            }
                           
                                completeContent.append("<td>").append(entry.getKey()).append("</td>");
                                completeContent.append("<td>").append(entry.getValue()).append("</td>");
                          
                            completeContent.append("</tr>");
                            iii++;
                         }
                        completeContent.append("</tbody></table></div><br/><br/>");
                    }
                    completeContent.append("</div>");
                } else {
                    isSendMailFlag = false;
                }
            } else if (operatorList[i].equalsIgnoreCase(">=")) {
                if (averageGTotal >= compGTWithStartValue) {
                     isSendMailFlag = true;
                    completeContent.append("<div><span style=\"padding: 5px; background-color: #f2f2f2; color: green; font-style: normal;\">Dear Sir/Madam,</span><br/><br/>"
                            + "<span style=\"padding: 5px; background-color: #42a5f5; color: white; font-style: normal;\">Report: <b>").append(reportName).append("</b></span><br/><br/>"
                            + "<span style=\"padding: 5px; background-color: #b71c1c; color: white; font-style: normal;\">Alert for <b>").append(measureNameList.get(0)).append("</b></span>"
                            + "<br/><br/>" + "<table>" + "<thead><tr><th>Target</th><th>Achieved</th><th>Alert Type</th></thead></tr>" + "<tr style='background-color:#ffccbc;'><td>").append("Value should be ").append(operatorList[i]).append(" ").append(compGTWithStartValue).append("</td><td>").append(getModifidNumber(averageGTDecimal)).append("</td><td></td></tr>"
                            + "</table><br/>");
                    for (String key : mainRowWiseVal.keySet()) {
                        Map<String, Double> sortedMap = new HashMap<String, Double>();
                        Map<Integer, List<String>> rowWiseVal2 = new HashMap<Integer, List<String>>();
                        logger.info("viewById table:"+key);
                        rowWiseVal2 = mainRowWiseVal.get(key);
                                                ///
                         for (int s = 1; s < rowWiseVal2.size(); s++) {
                           
                            List<String> allRowV = rowWiseVal2.get(s);
                            for (int k = 0; k < allRowV.size(); k++) {
                                sortedMap.put(allRowV.get(0),Double.parseDouble(allRowV.get(1).isEmpty()? "0" : allRowV.get(1)));
                            }
                        }
                         Map<String, Double> finalMapRes = new LinkedHashMap<String, Double>();
                         finalMapRes=sortMapValueAscAndDescByValue(sortedMap, compGTWithStartValue);
             
                        //
                        completeContent.append("<div id='").append(key).append("' style=\"min-height: 20px;max-height: 300px;overflow:auto;\"><table>");
                        completeContent.append("<thead><tr>");
                        for (int s = 0; s < 1; s++) {
                            List<String> allRowV = rowWiseVal2.get(s);
                            for (int k = 0; k < allRowV.size(); k++) {
                                completeContent.append("<th>").append(allRowV.get(k)).append("</th>");
                            }
                        }
                        completeContent.append("</tr></thead><tbody>");
//                        for (int s = 1; s < rowWiseVal2.size(); s++) {
//                          if (s % 2 == 0) {
//                                completeContent.append("<tr style='background-color:#fbe9e7;'>");
//                            } else {
//                                completeContent.append("<tr style='background-color:#ffccbc;'>");
//                            }
//                            List<String> allRowV = rowWiseVal2.get(s);
//                            for (int k = 0; k < allRowV.size(); k++) {
//                                completeContent.append("<td>").append(allRowV.get(k)).append("</td>");
//                            }
//                            completeContent.append("</tr>");
//                        }
                         int iii=0;
                         for (Map.Entry<String, Double> entry : finalMapRes.entrySet()) {
                            System.out.println("finalMapRes Key = " + entry.getKey() + ", finalMapRes Value = " + entry.getValue());
                                if (iii % 2 == 0) {
                                completeContent.append("<tr style='background-color:#fbe9e7;'>");
                            } else {
                                completeContent.append("<tr style='background-color:#ffccbc;'>");
                            }
                           
                                completeContent.append("<td>").append(entry.getKey()).append("</td>");
                                completeContent.append("<td>").append(entry.getValue()).append("</td>");
                          
                            completeContent.append("</tr>");
                            iii++;
                         }
                        completeContent.append("</tbody></table></div><br/><br/>");
                    }
                    completeContent.append("</div>");
                } else {
                    isSendMailFlag = false;
                }
            } else if (operatorList[i].equalsIgnoreCase("==")) {
                if (averageGTotal == compGTWithStartValue) {
                     isSendMailFlag = true;
                    completeContent.append("<div><span style=\"padding: 5px; background-color: #f2f2f2; color: green; font-style: normal;\">Dear Sir/Madam,</span><br/><br/>"
                            + "<span style=\"padding: 5px; background-color: #42a5f5; color: white; font-style: normal;\">Report: <b>").append(reportName).append("</b></span><br/><br/>"
                            + "<span style=\"padding: 5px; background-color: #b71c1c; color: white; font-style: normal;\">Alert for <b>").append(measureNameList.get(0)).append("</b></span>"
                            + "<br/><br/>" + "<table>" + "<thead><tr><th>Target</th><th>Achieved</th><th>Alert Type</th></thead></tr>" + "<tr style='background-color:#ffccbc;'><td>").append("Value should be be ").append(operatorList[i]).append(" ").append(compGTWithStartValue).append("</td><td>").append(getModifidNumber(averageGTDecimal)).append("</td><td></td></tr>"
                            + "</table><br/>");
                    for (String key : mainRowWiseVal.keySet()) {
                        Map<String, Double> sortedMap = new HashMap<String, Double>();
                        Map<Integer, List<String>> rowWiseVal2 = new HashMap<Integer, List<String>>();
                        logger.info("viewById table:"+key);
                        rowWiseVal2 = mainRowWiseVal.get(key);
                                                ///
                         for (int s = 1; s < rowWiseVal2.size(); s++) {
                           
                            List<String> allRowV = rowWiseVal2.get(s);
                            for (int k = 0; k < allRowV.size(); k++) {
                                sortedMap.put(allRowV.get(0),Double.parseDouble(allRowV.get(1).isEmpty()? "0" : allRowV.get(1)));
                            }
                        }
                         Map<String, Double> finalMapRes = new LinkedHashMap<String, Double>();
                         finalMapRes=sortMapValueAscAndDescByValue(sortedMap, compGTWithStartValue);
             
                        //
                        completeContent.append("<div id='").append(key).append("' style=\"min-height: 20px;max-height: 300px;overflow:auto;\"><table>");
                        completeContent.append("<thead><tr>");
                        for (int s = 0; s < 1; s++) {
                            List<String> allRowV = rowWiseVal2.get(s);
                            for (int k = 0; k < allRowV.size(); k++) {
                                completeContent.append("<th>").append(allRowV.get(k)).append("</th>");
                            }
                        }
                        completeContent.append("</tr></thead><tbody>");
//                        for (int s = 1; s < rowWiseVal2.size(); s++) {
//                          if (s % 2 == 0) {
//                                completeContent.append("<tr style='background-color:#fbe9e7;'>");
//                            } else {
//                                completeContent.append("<tr style='background-color:#ffccbc;'>");
//                            }
//                            List<String> allRowV = rowWiseVal2.get(s);
//                            for (int k = 0; k < allRowV.size(); k++) {
//                                completeContent.append("<td>").append(allRowV.get(k)).append("</td>");
//                            }
//                            completeContent.append("</tr>");
//                        }
                         int iii=0;
                         for (Map.Entry<String, Double> entry : finalMapRes.entrySet()) {
                            System.out.println("finalMapRes Key = " + entry.getKey() + ", finalMapRes Value = " + entry.getValue());
                                if (iii % 2 == 0) {
                                completeContent.append("<tr style='background-color:#fbe9e7;'>");
                            } else {
                                completeContent.append("<tr style='background-color:#ffccbc;'>");
                            }
                           
                                completeContent.append("<td>").append(entry.getKey()).append("</td>");
                                completeContent.append("<td>").append(entry.getValue()).append("</td>");
                          
                            completeContent.append("</tr>");
                            iii++;
                         }
                        completeContent.append("</tbody></table></div><br/><br/>");
                    }
                    completeContent.append("</div>");
                } else {
                    isSendMailFlag = false;
                }
            } else if (operatorList[i].equalsIgnoreCase("<>")) {
                Double compGTWithEndValue = Double.valueOf(eValuesList[i]);
                if (averageGTotal > compGTWithStartValue && averageGTotal < compGTWithEndValue) {
                     isSendMailFlag = true;
                    completeContent.append("<div><span style=\"padding: 5px; background-color: #f2f2f2; color: green; font-style: normal;\">Dear Sir/Madam,</span><br/><br/>"
                            + "<span style=\"padding: 5px; background-color: #42a5f5; color: white; font-style: normal;\">Report: <b>").append(reportName).append("</b></span><br/><br/>"
                            + "<span style=\"padding: 5px; background-color: #b71c1c; color: white; font-style: normal;\">Alert for <b>").append(measureNameList.get(0)).append("</b></span>"
                            + "<br/><br/>" + "<table>" + "<thead><tr><th>Target</th><th>Achieved</th><th>Alert Type</th></thead></tr>" + "<tr style='background-color:#ffccbc;'><td>").append("Value should be ").append(operatorList[i]).append(" ").append(compGTWithStartValue).append("</td><td>").append(getModifidNumber(averageGTDecimal)).append("</td><td></td></tr>"
                            + "</table><br/>");
                    for (String key : mainRowWiseVal.keySet()) {
                        Map<String, Double> sortedMap = new HashMap<String, Double>();
                        Map<Integer, List<String>> rowWiseVal2 = new HashMap<Integer, List<String>>();
                        logger.info("viewById table:"+key);
                        rowWiseVal2 = mainRowWiseVal.get(key);
                                                ///
                         for (int s = 1; s < rowWiseVal2.size(); s++) {
                           
                            List<String> allRowV = rowWiseVal2.get(s);
                            for (int k = 0; k < allRowV.size(); k++) {
                                sortedMap.put(allRowV.get(0),Double.parseDouble(allRowV.get(1).isEmpty()? "0" : allRowV.get(1)));
                            }
                        }
                         Map<String, Double> finalMapRes = new LinkedHashMap<String, Double>();
                         finalMapRes=sortMapValueAscAndDescByValue(sortedMap, compGTWithStartValue);
             
                        //
                        completeContent.append("<div id='").append(key).append("' style=\"min-height: 20px;max-height: 300px;overflow:auto;\"><table>");
                        completeContent.append("<thead><tr>");
                        for (int s = 0; s < 1; s++) {
                            List<String> allRowV = rowWiseVal2.get(s);
                            for (int k = 0; k < allRowV.size(); k++) {
                                completeContent.append("<th>").append(allRowV.get(k)).append("</th>");
                            }
                        }
                        completeContent.append("</tr></thead><tbody>");
//                        for (int s = 1; s < rowWiseVal2.size(); s++) {
//                          if (s % 2 == 0) {
//                                completeContent.append("<tr style='background-color:#fbe9e7;'>");
//                            } else {
//                                completeContent.append("<tr style='background-color:#ffccbc;'>");
//                            }
//                            List<String> allRowV = rowWiseVal2.get(s);
//                            for (int k = 0; k < allRowV.size(); k++) {
//                                completeContent.append("<td>").append(allRowV.get(k)).append("</td>");
//                            }
//                            completeContent.append("</tr>");
//                        }
                         int iii=0;
                         for (Map.Entry<String, Double> entry : finalMapRes.entrySet()) {
                            System.out.println("finalMapRes Key = " + entry.getKey() + ", finalMapRes Value = " + entry.getValue());
                                if (iii % 2 == 0) {
                                completeContent.append("<tr style='background-color:#fbe9e7;'>");
                            } else {
                                completeContent.append("<tr style='background-color:#ffccbc;'>");
                            }
                           
                                completeContent.append("<td>").append(entry.getKey()).append("</td>");
                                completeContent.append("<td>").append(entry.getValue()).append("</td>");
                          
                            completeContent.append("</tr>");
                            iii++;
                         }
                        completeContent.append("</tbody></table></div><br/><br/>");
                    }
                    completeContent.append("</div>");
                } else {
                    isSendMailFlag = false;
                }
            }
            completeContent.append("</body> </html>");
            logger.info("Need to send mail ========="+isSendMailFlag);
            //for sending mail need condition check
            if (isSendMailFlag) {
                params = new PbMailParams();
                params.setBodyText(completeContent.toString());
                // params.setToAddr(mailIds);
                params.setSubject(schedulerName);
                params.setHasAttach(false);
                mailer = new PbMail(params);
                try {
                    ArrayList<String> al = new ArrayList<String>();
                    al.addAll(Arrays.asList(mailIdsList));
                    params.setToAddr(al.toString().replace("[", "").replace("]", ""));
                    mailer.sendMail();
                } catch (Exception e) {
                    logger.error("Exception while sending KPI chart scheduler mail : ", e);
                }
            }
        }
        logger.info("Finished Executing KPI scheduler");
    }

    public static String getModifidNumber(BigDecimal bd) {       //target Basis
        String str = "";
        String temp = "";
        str = bd.toString();

        double num = Double.parseDouble(str);
        BigDecimal decimal = new BigDecimal(num);
        bd = decimal.setScale(2, RoundingMode.DOWN);
        temp = bd.toString();
        return temp;
    }

    private ResourceBundle getResourceBundle() {
        ResourceBundle resBndle = null;
        if (resBndle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resBndle = new PbReportCollectionResBunSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resBndle = new PbReportCollectionResourceBundleMySql();
            } else {
                resBndle = new PbReportCollectionResourceBundle();
            }
        }
        return resBndle;
    }

    public void getTimeDetailsArray(String reportId) {
        String sqlstr = "";
        String finalQuery = "";
        Object Obj[] = null;
        String temp = "";
        PbDb db = new PbDb();
        PbReturnObject retObj = null;
        try {
            sqlstr = getResourceBundle().getString("getParameterTimeInfo");
            Obj = new Object[1];
            Obj[0] = reportId;

            finalQuery = db.buildQuery(sqlstr, Obj);
            //////.println("finalquery of getparamtimeinfo is : "+finalQuery);
            retObj = db.execSelectSQL(finalQuery);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        String[] colNames = retObj.getColumnNames();
        int psize = retObj.getRowCount();
        ProgenParam pParam = new ProgenParam();

        timeDetailsMap = new LinkedHashMap<String, ArrayList<String>>();
        timeDetailsArray = new ArrayList();

        if (psize > 0) {
            timeDetailsArray.add(retObj.getFieldValueString(0, colNames[1]));
            timeDetailsArray.add(retObj.getFieldValueString(0, colNames[2]));
            for (int looper = 0; looper < psize; looper++) {
                ArrayList<String> timeInfo = new ArrayList<String>();
                temp = (retObj.getFieldValueString(looper, colNames[3])).trim();

                if (temp.equalsIgnoreCase("AS_OF_DATE")) {
                    if ("".equals(retObj.getFieldValueString(looper, 7))) {
//                            if (null==(retObj.getFieldValueDateString(0, 7))) {
                        if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                            if (!retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Month")) {
                                if (!retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Qtr")) {
                                    if (!retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Year")) {
                                        if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Yesterday")) {
                                            timeInfo.add(pParam.getcurrentdateforpage(1));
                                        } else if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Tomorrow")) {
                                            timeInfo.add(pParam.getcurrentdateforpage(-1));
                                        } else if (retObj.getFieldValueString(looper, colNames[8]).contains("newSysDate")) {
                                            timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                                        } else if (retObj.getFieldValueString(looper, colNames[8]).contains("globalDate")) {
                                            timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                                        } else if (retObj.getFieldValueString(looper, colNames[8]).contains("systemDate")) {
                                            timeInfo.add(pParam.getdateforpage());
                                        } else {
                                            timeInfo.add(pParam.getdateforpage());
                                        }
                                    } else {
                                        timeInfo.add(pParam.getdateforpage());
                                    }
                                } else {
                                    timeInfo.add(pParam.getdateforpage());
                                }
                            } else {
                                timeInfo.add(pParam.getdateforpage());
                            }
                        } else {
                            timeInfo.add(pParam.getdateforpage());
                        }

                    } else {
                        String date = retObj.getFieldValueString(looper, 7);
                        timeInfo.add(date);
                    }
                } else if (temp.equalsIgnoreCase("AS_OF_DATE1")) {
                    if ("".equals(retObj.getFieldValueString(looper, 7))) {
                        if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                            if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("fromyestrday")) {
                                timeInfo.add(pParam.getcurrentdateforpage(1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("fromtomorow")) {
                                timeInfo.add(pParam.getcurrentdateforpage(-1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("fromSysDate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("fromglobalDate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else {
                                timeInfo.add(pParam.getdateforpage(30));
                            }
                        }
//                            if (null==(retObj.getFieldValueDateString(0, 7))) {
                    } else {
                        String date = retObj.getFieldValueString(looper, 7);
                        timeInfo.add(date);
                    }

                } else if (temp.equalsIgnoreCase("AS_OF_DATE2")) {
                    if ("".equals(retObj.getFieldValueString(looper, 7))) {
                        if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                            if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("toyestrday")) {
                                timeInfo.add(pParam.getcurrentdateforpage(1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("totomorow")) {
                                timeInfo.add(pParam.getcurrentdateforpage(-1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("toSystDate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("toglobalDdate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else {
                                timeInfo.add(pParam.getdateforpage(1));
                            }
                        }
//                            if (null==(retObj.getFieldValueDateString(0, 7))) {
                    } else {
                        String date = retObj.getFieldValueString(looper, 7);
                        timeInfo.add(date);
                    }

                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DATE1")) {
                    if (retObj.getFieldValueString(looper, colNames[2]) == null || (!retObj.getFieldValueString(looper, colNames[2]).equalsIgnoreCase("PRG_COHORT"))) {
                        String date = "";
                        date = retObj.getFieldValueString(looper, 7);
                        if (!date.equalsIgnoreCase("") && date != null) {
                            timeInfo.add(date);
                        } else if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                            if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("CmpFrmyestrday")) {
                                timeInfo.add(pParam.getcurrentdateforpage(1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("CmpFrmtomorow")) {
                                timeInfo.add(pParam.getcurrentdateforpage(-1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("CmpFrmSysDate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("CmpFrmglobalDate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else {
                                timeInfo.add(pParam.getdateforpage(60));
                            }
                        }
                    } else {
                        timeInfo.add(pParam.getdateforpage(30));
                    }
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                    if (retObj.getFieldValueString(looper, colNames[2]) == null || (!retObj.getFieldValueString(looper, colNames[2]).equalsIgnoreCase("PRG_COHORT"))) {
                        String date = "";
                        date = retObj.getFieldValueString(looper, 7);
                        if (!date.equalsIgnoreCase("") && date != null) {
                            timeInfo.add(date);
                        } else if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                            if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("cmptoyestrday")) {
                                timeInfo.add(pParam.getcurrentdateforpage(1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("cmptotomorow")) {
                                timeInfo.add(pParam.getcurrentdateforpage(-1));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("cmptoSysDate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else if (retObj.getFieldValueString(looper, colNames[8]).contains("cmptoglobalDate")) {
                                timeInfo.add(pParam.getcurrentdateforpage(retObj.getFieldValueString(looper, colNames[8])));
                            } else {
                                timeInfo.add(pParam.getdateforpage(60));
                            }
                        }
                    } else {
                        timeInfo.add(pParam.getdateforpage(-365));
                    }
                } else if (temp.equalsIgnoreCase("AS_OF_DMONTH1")) {
                    timeInfo.add(pParam.getmonthforpage("30"));
                } else if (temp.equalsIgnoreCase("AS_OF_DMONTH2")) {
                    timeInfo.add(pParam.getmonthforpage("0"));
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DMONTH1") || temp.equalsIgnoreCase("CMP_AS_OF_DMONTH2")) {
                    timeInfo.add(pParam.getmonthforpage("62"));
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DMONTH2")) {
                    timeInfo.add(pParam.getmonthforpage("31"));
                } else if (temp.equalsIgnoreCase("AS_OF_DQUARTER1")) {
                    timeInfo.add(pParam.getQtrforpage());
                } else if (temp.equalsIgnoreCase("AS_OF_DQUARTER2")) {
                    timeInfo.add(pParam.getQtrforpage());
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DQUARTER1") || temp.equalsIgnoreCase("CMP_AS_OF_DQUARTER2")) {
                    timeInfo.add(pParam.getQtrforpage());
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DQUARTER2")) {
                    timeInfo.add(pParam.getQtrforpage());
                } else if (temp.equalsIgnoreCase("AS_OF_DYEAR1")) {
                    timeInfo.add(pParam.getYearforpage("366"));
                } else if (temp.equalsIgnoreCase("AS_OF_DYEAR2")) {
                    timeInfo.add(pParam.getYearforpage());
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DYEAR1")) {
                    timeInfo.add(pParam.getYearforpage("734"));
                } else if (temp.equalsIgnoreCase("CMP_AS_OF_DYEAR2")) {
                    timeInfo.add(pParam.getYearforpage("367"));
                } else if (temp.equalsIgnoreCase("AS_OF_MONTH") || temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                    timeInfo.add(pParam.getmonthforpage());
                } else if (temp.equalsIgnoreCase("AS_OF_YEAR") || temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                    timeInfo.add(pParam.getYearforpage());
                } else if (temp.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                        timeInfo.add(retObj.getFieldValueString(looper, colNames[8]));
                    } else {
                        timeInfo.add("Month");
                    }
                } else if (temp.equalsIgnoreCase("PRG_DAY_ROLLING")) {
                    timeInfo.add("Last 30 Days");
                } else if (temp.equalsIgnoreCase("PRG_COMPARE")) {
                    if ((retObj.getFieldValueString(looper, colNames[8]) == null ? "" != null : !retObj.getFieldValueString(looper, colNames[8]).equals(""))) {
                        if (retObj.getFieldValueString(looper, colNames[8]).equalsIgnoreCase("Month")) {
                            timeInfo.add("Last Period");
                        } else {
                            timeInfo.add(retObj.getFieldValueString(looper, colNames[8]));
                        }
                    } else {
                        timeInfo.add("Last Period");
                    }
                }

                timeInfo.add("CBO_" + temp);
                timeInfo.add(retObj.getFieldValueString(looper, colNames[4]));
                timeInfo.add(retObj.getFieldValueString(looper, colNames[5]));
                timeInfo.add(retObj.getFieldValueString(looper, colNames[6]));
                timeInfo.add(timeInfo.get(0));

                timeInfo.add(temp);
                timeDetailsMap.put(timeInfo.get(6), timeInfo);
            }
        }
        //////.println("timeDetailsArray is : "+timeDetailsArray);
        ArrayList timeInfo = new ArrayList();
        //////////////Time Calc
        if (!timeDetailsArray.isEmpty()) {
            if (timeDetailsArray.get(0).toString().equalsIgnoreCase("Year") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_CMP")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_YEAR");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_YEAR1");
                if (timeInfo != null) {
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                timeInfo = timeDetailsMap.get("AS_OF_DATE1");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = timeDetailsMap.get("AS_OF_DATE2");
                timeDetailsArray.add(timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DATE1") != null) {
                    timeInfo = timeDetailsMap.get("CMP_AS_OF_DATE1");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DATE2") != null) {
                    timeInfo = timeDetailsMap.get("CMP_AS_OF_DATE2");
                    timeDetailsArray.add(timeInfo.get(0));

                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("PRG_PERIOD_TYPE") != null) {
                    timeInfo = timeDetailsMap.get("PRG_PERIOD_TYPE");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add("PERIOD");
                }
            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE1");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE2");
                timeDetailsArray.add(timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DATE1") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DATE1");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DATE2") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DATE2");
                    timeDetailsArray.add(timeInfo.get(0));

                } else {
                    timeDetailsArray.add(null);
                }
            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DMONTH1");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DMONTH2");
                timeDetailsArray.add(timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DMONTH1") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DMONTH1");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DMONTH2") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DMONTH2");
                    timeDetailsArray.add(timeInfo.get(0));

                } else {
                    timeDetailsArray.add(null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_QUARTER_RANGE")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DQUARTER1");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DQUARTER2");
                timeDetailsArray.add(timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DQUARTER1") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DQUARTER1");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DQUARTER2") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DQUARTER2");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DYEAR1");
                timeDetailsArray.add(timeInfo.get(0));
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DYEAR2");
                timeDetailsArray.add(timeInfo.get(0));
                if (timeDetailsMap.get("CMP_AS_OF_DYEAR1") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DYEAR1");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add(null);
                }
                if (timeDetailsMap.get("CMP_AS_OF_DYEAR2") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("CMP_AS_OF_DYEAR2");
                    timeDetailsArray.add(timeInfo.get(0));

                } else {
                    timeDetailsArray.add(null);
                }

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DAY_ROLLING")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = (ArrayList) timeDetailsMap.get("PRG_DAY_ROLLING");
                timeDetailsArray.add(timeInfo.get(0));

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = (ArrayList) timeDetailsMap.get("PRG_PERIOD_TYPE");
                timeDetailsArray.add(timeInfo.get(0));

                if (timeDetailsMap.get("PRG_COMPARE") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("PRG_COMPARE");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add("Last Period");
                }
            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("Month") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_CMP")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_MONTH");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_MONTH1");
                timeDetailsArray.add(timeInfo.get(0));

            } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("MONTH") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_MONTH");
                timeDetailsArray.add(timeInfo.get(0));

                timeInfo = (ArrayList) timeDetailsMap.get("PRG_PERIOD_TYPE");
                timeDetailsArray.add(timeInfo.get(0));

                if (timeDetailsMap.get("PRG_COMPARE") != null) {
                    timeInfo = (ArrayList) timeDetailsMap.get("PRG_COMPARE");
                    timeDetailsArray.add(timeInfo.get(0));
                } else {
                    timeDetailsArray.add("Last Period");
                }
            }

        } else {////Time Query Failed as of now only because of prgorn non Standard time
            //ie Proge Time is not used .
            //We will set day level time , but we should find the time levele from time master table
//            avoidProgenTime();
        }
    }

    public void setTimeTypeForMeasure(List<String> measureIds, ProgenAOQuery objQuery) {
        ArrayList<String> measureType = new ArrayList<String>();
        for (int i = 0; i < measureIds.size(); i++) {
            String measureId = measureIds.get(i);
            if (measureId.contains("_PRIOR")) {
                measureType.add("PRIORPERIOD");
            } else if (measureId.contains("_MTD")) {
                measureType.add("MTD");
            } else if (measureId.contains("_PMTD")) {
                measureType.add("PMTD");
            } else if (measureId.contains("_PYMTD")) {
                measureType.add("PYMTD");
            } else if (measureId.contains("_QTD")) {
                measureType.add("QTD");
            } else if (measureId.contains("_PQTD")) {
                measureType.add("PQTD");
            } else if (measureId.contains("_PYQTD")) {
                measureType.add("PYQTD");
            } else if (measureId.contains("_YTD")) {
                measureType.add("YTD");
            } else if (measureId.contains("_PYTD")) {
                measureType.add("PYTD");
            } else if (measureId.contains("_MOMPer")) {
                measureType.add("MOMPer");
            } else if (measureId.contains("_MOYMPer")) {
                measureType.add("MOYMPer");
            } else if (measureId.contains("_QOQPer")) {
                measureType.add("QOQPer");
            } else if (measureId.contains("_QOYQPer")) {
                measureType.add("QOYQPer");
            } else if (measureId.contains("_YOYPer")) {
                measureType.add("YOYPer");
            } else if (measureId.contains("_MOM")) {
                measureType.add("MOM");
            } else if (measureId.contains("_MOYM")) {
                measureType.add("MOYM");
            } else if (measureId.contains("_QOQ")) {
                measureType.add("QOQ");
            } else if (measureId.contains("_QOYQ")) {
                measureType.add("QOYQ");
            } else if (measureId.contains("_YOY")) {
                measureType.add("YOY");
            } else {
                measureType.add("PERIOD");
            }
        }
        objQuery.setTimeDetailsArray(measureType);
    }
    public Map<String, Double> sortMapValueAscAndDescByValue(Map<String, Double> map, double compVal){
          Map<String, Double> finalMap = new LinkedHashMap<String, Double>();
//        Map<String, Integer> map = new HashMap<String, Integer>();
//        map.put("java", 20.0);
//        map.put("C++", 45);
//        map.put("Java2Novice", 2);
//        map.put("Unix", 67);
//        map.put("MAC", 26);
//        map.put("Why this kolavari", 93);
//        map.put("jar", 10);
//        map.put("kent", 12);
        Set<Entry<String, Double>> set = map.entrySet();
        List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(set);
        List<Entry<String, Double>> list2 = new ArrayList<Entry<String, Double>>(set);
        Collections.sort( list, new Comparator<Map.Entry<String, Double>>()
        {
            @Override
            public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        Collections.sort( list2, new Comparator<Map.Entry<String, Double>>()
        {
            @Override
            public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
            {
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );

        for(Map.Entry<String, Double> entry:list){
            Double allv=entry.getValue();
            if(allv>=compVal){
                finalMap.put(entry.getKey(),entry.getValue());
//            System.out.println(entry.getKey()+" ==== "+entry.getValue());
            }else{
                
            }
            
        }
        for(Map.Entry<String, Double> entry:list2){
            Double allv=entry.getValue();
            if(allv<compVal){
                finalMap.put(entry.getKey(),entry.getValue());
//            System.out.println(entry.getKey()+" ==== "+entry.getValue());
            }else{
                
            }
            
        }
//        for (Map.Entry<String, Double> entry : finalMap.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//        }

        return finalMap;
    }
}
