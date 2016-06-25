/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @author: Amar @Module: This file is created to perform all operation on AO
 */
package com.progen.reportview.bd;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.db.ProgenDataSet;
//import com.progen.log.ProgenLog;
import com.progen.query.RTMeasureElement;
import com.progen.report.FileReadWrite;
import com.progen.report.PbReportCollection;
import com.progen.report.ReportObjectMeta;
import com.progen.report.ReportObjectQuery;
import com.progen.report.query.PbReportQuery;
import com.progen.report.query.QueryExecutor;
import com.progen.reportdesigner.db.AOTemplateDAO;
import com.progen.reportview.db.PbAOViewerDAO;
import com.progen.reportview.db.PbReportViewerDAO;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

public class PbAOViewerBD extends PbDb{

    public static Logger logger = Logger.getLogger(PbAOViewerBD.class);
    private ArrayList<String> rowViewIds;
    private ArrayList<String> colViewIds;
    private ArrayList<String> timeDetails;
    private ArrayList<String> oneviewtimedetails;
    private ArrayList<String> tempTimeDetails;
    public ArrayList rowViewBys;
    public ArrayList ColViewBys;
    public ArrayList qryColumns;
    public int totalViewBys;
    public int rowViewCount;
    public int NoOfDays;
    public HashMap NonViewByMap;
    private String userId = null;
    boolean isAOEnable = true;
    AOTemplateDAO DAO = new AOTemplateDAO();
    public int NoOfSubAO = 0;
    public HashMap<String,String> aoAggNo=new HashMap<>();

    public boolean prepareAO(String action, Container container, String pbAOId, String PbUserId, HashMap requestParamValues) {

//        ProgenLog.log(ProgenLog.FINE, this, "prepareReport", "Entered " + System.currentTimeMillis());
        logger.info("Entered " + System.currentTimeMillis());
        PbReportCollection collect = null;
        QueryExecutor qryExec = new QueryExecutor();
        ProgenDataSet pbretObj = null;
        ArrayList originalColumns = new ArrayList();
        ArrayList displayColumns = new ArrayList();
        ArrayList displayLabels = new ArrayList();
        ArrayList displayLabelskpi = new ArrayList();
        ArrayList displayColumnskpi = new ArrayList();
        ArrayList dataTypes = new ArrayList();
        ArrayList displayTypes = new ArrayList();
        ArrayList links = new ArrayList();
        HashMap replinks = new HashMap();
        ArrayList alignments = new ArrayList();
//        ArrayList signs = new ArrayList();
        HashMap columnsVisibility = new HashMap();
        String dataType = "";
        String[] dbColumns = null;
        String[] columnTypes = null;

        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        HashMap GraphHashMap = null;
        HashMap ReportHashMap = null;
        ArrayList REP = null;
        ArrayList REPNames = null;
        ArrayList CEPNames = null;
        ArrayList Measures = null;
        ArrayList MeasuresNames = null;
        ArrayList Parameters = null;
        ArrayList ParametersNames = null;
        ArrayList ReportTimeParams = null;
        ArrayList ReportTimeParamsNames = null;
        String[] ParameterStr = null;
        String[] TimeParametersStr = null;
        HashMap DisplayNamesMap = null;
        ArrayList reportQryElementIds = null;
        ArrayList reportQryAggregations = null;
        ArrayList reportQryColNames = null;
        ArrayList reportQryColTypes = null;
        HashMap<String, ArrayList<String>> colProperties = new HashMap<String, ArrayList<String>>();
        Boolean isRTmeasExists = false;
        //String action = request.getParameter("action");
        StringBuffer bizRolesSb = new StringBuffer();

        HashMap TableProperties = null;
        boolean isMeasChangeEvent = false; //is Measure Change Event
        boolean isGrpMeasChgEvent = false; //graph column change event
        boolean ChangeViewByEvent = false; //Change View By event
        boolean isParmChngEvent = false; //parameter Change event
        boolean isOpenRepEvent = false; //report open Event
        boolean isExportExEvent = false;// export multiple excel Event
        boolean isGrpByAnlys = false; //Group By Analysis
        boolean isMapMeasChgEvent = false; //Map Measure Change
        boolean isDrillDown = false;
        PbReturnObject newCrossRetObj = null;
        PbReturnObject newImportExcelRetObj = null;

        HashMap<String, String> CrosstabMsrMap = new HashMap<String, String>();
//        ProgenLog.log(ProgenLog.FINE, this, "prepareReport", "Action " + action);
        logger.info("Action " + action);
        try {
            userId = PbUserId;
            container.setTableId(pbAOId);
            container.setAOId(pbAOId);
            PbReportViewerBD viewerBd = new PbReportViewerBD();
            ParametersHashMap = container.getParametersHashMap();
            TableHashMap = container.getTableHashMap();
            GraphHashMap = container.getGraphHashMap();
            ReportHashMap = container.getReportHashMap();
            ParametersHashMap = (ParametersHashMap == null) ? new HashMap() : ParametersHashMap;
            TableHashMap = (TableHashMap == null) ? new HashMap() : TableHashMap;
            GraphHashMap = (GraphHashMap == null) ? new HashMap() : GraphHashMap;
            ReportHashMap = (ReportHashMap == null) ? new HashMap() : ReportHashMap;
            collect = container.getReportCollect();
//            ProgenLog.log(ProgenLog.FINE, this, "prepareReport", "getParamMetadata in Collection " + System.currentTimeMillis());
            logger.info("getParamMetadata in Collection " + System.currentTimeMillis());
            if (collect == null) //open event
            {
                collect = new PbReportCollection();
            }
            collect.AOId = pbAOId;
            collect.isparent = container.isDependentReport;
            collect.parentRepId = container.parentid;
            collect.reportIncomingParameters = requestParamValues;
//            ProgenLog.log(ProgenLog.FINE, this, "prepareReport", "Prepared Collection " + System.currentTimeMillis());
            logger.info("Prepared Collection " + System.currentTimeMillis());
            PbReportViewerDAO daoonnsrattr = new PbReportViewerDAO();
            HashMap mapformsrattr = daoonnsrattr.modifyMeasureAttrreport(pbAOId, container);
            container.setmodifymeasureAttrChnge(mapformsrattr);
            /*added by Srikanth.p for initialize Report to check wether initilalize report Enabled*/
            if (isOpenRepEvent) {
                PbReportViewerDAO vDao = new PbReportViewerDAO();
                HashMap assignedMap = vDao.getAssignedInitialReports(pbAOId);
                vDao.modifyedMeasuresToReport(pbAOId, container);
            }/* Ended by Srikanth.p*/
            container.setTableMeasure(reportQryElementIds);
            container.setTableMeasureNames(reportQryColNames);
            collect.mapAggrigations();

            ReportHashMap.put("reportQryElementIds", reportQryElementIds);
            ReportHashMap.put("reportQryAggregations", reportQryAggregations);
            ReportHashMap.put("reportQryColNames", reportQryColNames);
            ReportHashMap.put("reportQryColTypes", reportQryColTypes);

            if (!collect.isTimeInitialized()) {
                collect.avoidProgenTime();
            }
            if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("year")) {
                    container.setTimeLevel("YEAR");
                } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Qtr")) {
                    container.setTimeLevel("QUARTER");
                } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Month")) {
                    container.setTimeLevel("MONTH");
                } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Week")) {
                    container.setTimeLevel("WEEK");
                } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Day")) {
                    container.setTimeLevel("DAY");
                }
            } else if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                container.setTimeLevel("MONTH");
            } else if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                container.setTimeLevel("QUARTER");
            } else if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_QTR_RANGE")) {
                container.setTimeLevel("YEAR");
            } else if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                container.setTimeLevel("YEAR");
            }
            container.setViewByColNames(collect.reportRowViewbyValues);
            container.setViewByElementIds(collect.reportRowViewbyValues);
            container.setTimeDetailsArray(collect.timeDetailsArray);
            PbReportViewerDAO dao = new PbReportViewerDAO();

//            String query = "";
//            // modified by krishan
//            PbReportQuery reportQuery = null;
//            reportQuery = qryExec.formulateQuery(collect, PbUserId);
//
//            if (container.isSplitBy()) {
//                reportQuery.setMultiViewBy(true);
//                reportQuery.addMultiViewBys(container.getSplitBy());
//            }
//            //added by Nazneen for Dimension Segment
//            if (container.getDepViewByConditionsmap() != null && !container.getDepViewByConditionsmap().isEmpty()) {
//                reportQuery.parameterType = container.getDepViewByConditionsmap();
//            }
//            if (container.getDependentviewbyIdQry() != null && !container.getDependentviewbyIdQry().isEmpty()) {
//                reportQuery.parameterQuery = container.getDependentviewbyIdQry();
//            }
//            reportQuery.resetParamHashmap = collect.resetParamHashmap;
//            if (collect.lockdatasetmap != null && !collect.lockdatasetmap.isEmpty()) {
//                for (int s = 0; s < collect.reportParamIds.size(); s++) {
//                    if (collect.lockdatasetmap.get(collect.reportParamIds.get(s)) != null) {
//                        reportQuery.parameterQuery.put(collect.reportParamIds.get(s), collect.dependentviewbyIdQry.get(collect.reportParamIds.get(s)));
//                    }
//                }
//            }
//            reportQuery.isAOEnable = this.isAOEnable;
//            String reportquery = "";
//            reportQuery.inMaps = (HashMap<String, List>) collect.operatorFilters.get("IN");
//            reportQuery.notInMaps = (HashMap<String, List>) collect.operatorFilters.get("NOTIN");
//            reportQuery.likeMaps = (HashMap<String, List>) collect.operatorFilters.get("LIKE");
//            reportQuery.notLikeMaps = (HashMap<String, List>) collect.operatorFilters.get("NOTLIKE");
//            reportQuery.reportQryElementIds = collect.reportQryElementIds;
//            //End of code by Nazneen on 18Jan14 for Analytical Object
//            query = reportQuery.generateViewByQry();
//            //added by sruthi to display the full query
//            reportquery = reportQuery.getfullQuery();
//            container.setReportQuery(reportquery);//ended by sruthi
//            collect.setRepQry(query);
//            Boolean isReportAccessible = reportQuery.isReportAccess();
//            container.setIsReportAccessible(isReportAccessible);
//            HashMap vals111 = new HashMap();
//            vals111 = reportQuery.getTimememdetails();
//            collect.setTimememdetails(vals111);
//            container.setTimememdetails(vals111);
//            container.setViewbyqry(reportQuery);
            if (collect.tableColNames != null && !collect.tableColNames.isEmpty()) {  // for generating crosstab msrnames while in renaming msr
                for (int i = container.getViewByCount(); i < collect.tableColNames.size(); i++) {
                    CrosstabMsrMap.put(collect.tableElementIds.get(i).toString(), collect.tableColNames.get(i).toString());
                }
            }
//            rowViewBys = reportQuery.getOrgRowViewbyCols();
//            ColViewBys = reportQuery.getColViewbyCols();
//            totalViewBys = reportQuery.getRowViewbyCols().size();
//            qryColumns = reportQuery.getQryColumns();
//            NoOfDays = reportQuery.getNoOfDays();
//            NonViewByMap = reportQuery.NonViewByMap;
boolean createAOForReports;

if(container.aoEditSummFlag){
            createAOForReports = this.createAOForReports(container, collect, pbAOId);
}else{
    createAOForReports=true;
}
             if(createAOForReports)
             {
//            if (pbretObj == null) {
//                pbretObj = qryExec.executeQuery(collect, query, false);
//                container.setDisplayColsfortimeDB(pbretObj.getColumnNames());
//                container.setRetObj(pbretObj);
//                container.setGrret(pbretObj);
//                container.setSqlStr(query);
//                container.setNoOfDays(NoOfDays);
//                collect.setNonViewByMap(NonViewByMap);
//                dbColumns = pbretObj.getColumnNames();
//                columnTypes = pbretObj.getColumnTypes();
//                if (isMeasChangeEvent == false && ChangeViewByEvent == false && isGrpMeasChgEvent == false && isParmChngEvent == false && isMapMeasChgEvent == false) {
//                    container.setReportMode("view");
//                    container.resetBackUpVariables();//newly added by santhosh.kumar@progenbusiness.com to reset back up variables;
//                    if ("".equals(collect.tableDisplayRows) || "All".equalsIgnoreCase(collect.tableDisplayRows)) {
////                        container.setTableDisplayRows(((Integer) pbretObj.getRowCount()).toString());
//                        container.setPagesPerSlide(((Integer) pbretObj.getRowCount()).toString());
//                    }
//                }
//                ProgenLog.log(ProgenLog.FINE, this, "prepareReport", "Query Fired " + System.currentTimeMillis());
//            }
            //container.setSqlStr(query);
            container.setNoOfDays(NoOfDays);

            container.setColumnViewByCount("0");
            //for normal report
            container.setReportCrosstab(false);
            originalColumns = container.getOriginalColumns();
            displayColumns = container.getDisplayColumns();
            displayLabels = container.getDisplayLabels();
            dataTypes = container.getDataTypes();
            displayTypes = container.getDisplayTypes();
            links = container.getLinks();
            replinks = container.getRepLinks();
            alignments = container.getAlignments();
            columnsVisibility = container.getColumnsVisibility();
            int viewByCount = collect.reportRowViewbyValues.size();
            //for change by view we will always reset view by count
            container.setViewByCount((collect.reportRowViewbyValues.size()));
            if (originalColumns == null || originalColumns.size() == 0) {
                originalColumns = originalColumns == null ? new ArrayList() : originalColumns;
                for (int i = 0; i < collect.reportRowViewbyValues.size(); i++) {
                    if (String.valueOf(collect.reportRowViewbyValues.get(i)).equalsIgnoreCase("Time")) {
                        if (!originalColumns.contains(String.valueOf(collect.reportRowViewbyValues.get(i)))) {
                            originalColumns.add(String.valueOf(collect.reportRowViewbyValues.get(i)));
                        }
                    } else {
                        if (String.valueOf(collect.reportRowViewbyValues.get(i)).contains("A_")) {
                            if (!originalColumns.contains(String.valueOf(collect.reportRowViewbyValues.get(i)))) {
                                originalColumns.add(String.valueOf(collect.reportRowViewbyValues.get(i)));
                            }
                        } else {
                            if (!originalColumns.contains("A_" + String.valueOf(collect.reportRowViewbyValues.get(i)))) {
                                originalColumns.add("A_" + String.valueOf(collect.reportRowViewbyValues.get(i)));
                            }
                        }
                    }
                }
                //then add %wise measure from collect.tableELementId which is pertaining to Table
                for (String tableElement : collect.tableElementIds) {
                    if (!originalColumns.contains(tableElement)) {
                        originalColumns.add(tableElement);
                    }
                }
                //code written by swathi for the purpose of rowViewby sorting in case of Number type
                if (dataTypes != null && !dataTypes.isEmpty()) {
                    dataTypes.clear();
                    alignments.clear();
                    displayTypes.clear();
                } else {
                    dataTypes = new ArrayList();
                    alignments = new ArrayList();
                    displayTypes = new ArrayList();
                }
                int colNum = 0;
                String dispCol;
                int count = originalColumns.size();
                if (container.getReportCollect().isIsExcelimportEnable() && newImportExcelRetObj != null) {
//                         count=(newImportExcelRetObj.cols.length-collect.reportRowViewbyValues.size());
                    count = newImportExcelRetObj.cols.length;
                }
            } else {
                viewByCount = collect.reportRowViewbyValues.size();
            }
            displayColumns = (displayColumns == null || displayColumns.isEmpty()) ? (ArrayList) collect.tableElementIds.clone() : displayColumns;
            dataTypes = (dataTypes == null || dataTypes.isEmpty()) ? collect.tableColTypes : dataTypes;
            displayTypes = (displayTypes == null || displayTypes.isEmpty()) ? collect.tableColDispTypes : displayTypes;
//                    signs = collect.columnSignType;
//            if (displayLabels == null || displayLabels.isEmpty()) {
//                displayLabels = (ArrayList) collect.tableColNames.clone();
//                if (container.getReportCollect().isIsExcelimportEnable() && newImportExcelRetObj != null) {
//                    int count = pbretObj.getColumnNames().length;
//                    for (int i = count; i < newImportExcelRetObj.cols.length; i++) {
//                        displayLabels.add(dbColumns[i]);
////                                container.getDisplayLabels().add(dbColumns[i]);
//                    }
//                }
//            } else {
            displayLabels = new ArrayList();
            for (int viewByIndex = 0; viewByIndex < collect.reportRowViewbyValues.size(); viewByIndex++) {
                displayLabels.add(collect.getElementName(String.valueOf(collect.reportRowViewbyValues.get(viewByIndex))));
            }
            HashMap tableHM = container.getTableHashMap();
            List measures = (List) tableHM.get("Measures");
            List measNames = (List) tableHM.get("MeasuresNames");
//                for (int viewByIndex = collect.reportRowViewbyValues.size(); viewByIndex < displayColumns.size(); viewByIndex++) {
//                    String columnName = String.valueOf(displayColumns.get(viewByIndex));
//                    if (RTMeasureElement.isRunTimeMeasure(columnName)) {
//                        if (measures.contains(columnName)) {
//                            int index = measures.indexOf(columnName);
//                            displayLabels.add(measNames.get(index));
//                        } else {
//                            String colSuffix = RTMeasureElement.getMeasureType(columnName).getColumnDisplay();
//                            columnName = RTMeasureElement.getOriginalColumn(columnName);
//                            displayLabels.add(NonViewByMap.get(columnName) + colSuffix);
//                        }
//                    } else {
//                        if (container.getMeasureName(columnName) != null) {
//                            displayLabels.add(container.getMeasureName(columnName));
//                        } else if (NonViewByMap.containsKey(columnName)) {
//                            displayLabels.add(NonViewByMap.get(columnName));
//                        } else {
//                            displayLabels.add(columnName);
//                        }
//                    }
//                    columnName = null;
//                }
            //}

            ArrayList viewByColumnsAlist = new ArrayList();
            ArrayList viewByElementdIdAlist = new ArrayList();
            for (int i = 0; i < viewByCount; i++) {
                if (String.valueOf(displayColumns.get(i)).equalsIgnoreCase("TIME")) {
                    viewByElementdIdAlist.add(String.valueOf(displayColumns.get(i)));

                } else {
                    viewByElementdIdAlist.add(String.valueOf(displayColumns.get(i)).substring(2));

                }
                viewByColumnsAlist.add(String.valueOf(displayLabels.get(i)));
            }
            container.setViewByColNames(viewByColumnsAlist);
            container.setViewByElementIds(viewByElementdIdAlist);
            REP = viewByElementdIdAlist;
            REPNames = viewByColumnsAlist;
            container.setOriginalColumns(originalColumns);
            container.setDisplayColumns(displayColumns);
            container.setDisplayLabels(displayLabels);
            container.setDisplayLabelskpi(displayLabelskpi);

            container.setDataTypes(dataTypes);
            container.setDisplayTypes(displayTypes);
            container.setLinks(links);
            container.setRepLinks(replinks);
            container.setSelected(new ArrayList());
            container.setAlignments(alignments);
            container.setReportCollect(collect);
            container.setAOName(collect.aoName);
            container.setReportDesc(collect.reportDesc);
            container.setSortRetObj(null);
//                container.setPagesPerSlide("All");

            ParameterStr = (String[]) (collect.reportParameters.keySet()).toArray(new String[0]);
            TimeParametersStr = (String[]) (collect.timeDetailsMap.keySet()).toArray(new String[0]);

            for (int paramIndex = 0; paramIndex < ParameterStr.length; paramIndex++) {
                ArrayList alist = (ArrayList) collect.reportParameters.get(ParameterStr[paramIndex]);
                if (Parameters == null) {
                    Parameters = new ArrayList();
                    ParametersNames = new ArrayList();
                }
                Parameters.add(ParameterStr[paramIndex]);
                ParametersNames.add(String.valueOf(alist.get(1)));
                alist = null;
            }
            for (int timeIndex = 0; timeIndex < TimeParametersStr.length; timeIndex++) {
                ArrayList alist = (ArrayList) collect.timeDetailsMap.get(TimeParametersStr[timeIndex]);
                if (ReportTimeParams == null) {
                    ReportTimeParams = new ArrayList();
                    ReportTimeParamsNames = new ArrayList();
                }
                ReportTimeParams.add(TimeParametersStr[timeIndex]);
                ReportTimeParamsNames.add(String.valueOf(alist.get(2)));
                alist = null;
            }
            ReportHashMap.put("ReportFolders", collect.reportBizRoles);
            ArrayList alist = new ArrayList();
            for (String str : collect.reportBizRoles) {
                alist.add(str);
            }
            ParametersHashMap.put("UserFolderIds", alist.toString().replace("[", "").replace("]", ""));
            if (collect.reportColViewbyValues == null || collect.reportColViewbyValues.size() == 0) {
                if (ReportHashMap.get("DisplayNamesMap") != null) {
                    DisplayNamesMap = (HashMap) ReportHashMap.get("DisplayNamesMap");
                } else {
                    DisplayNamesMap = new HashMap();
                }
                DisplayNamesMap = NonViewByMap;
                ReportHashMap.put("DisplayNamesMap", DisplayNamesMap);
            }

            ParametersHashMap.put("Parameters", Parameters);
            ParametersHashMap.put("ParametersNames", ParametersNames);
            ParametersHashMap.put("TimeParameters", ReportTimeParams);
            ParametersHashMap.put("TimeParametersNames", ReportTimeParamsNames);
            ParametersHashMap.put("TimeDimHashMap", collect.timeDetailsMap);
            ParametersHashMap.put("TimeDetailstList", collect.timeDetailsArray);

            TableHashMap.put("REP", REP);
            TableHashMap.put("REPNames", REPNames);
            TableHashMap.put("CEP", collect.reportColViewbyValues);
            TableHashMap.put("CEPNames", CEPNames);

            TableHashMap.put("Measures", Measures);
            TableHashMap.put("MeasuresNames", MeasuresNames);
            container.setFromColumn((container.getViewByCount())); //added by asnthosh.k on 06-03-2010 so as to start the displaying of columns in columns properties based on no of view bys

            String firstColumn = container.getDisplayColumns().get(0);
            //Function to save report
            this.saveReport(container, container.getAOName(), container.getReportDesc(), Integer.parseInt(pbAOId), dataType, userId, ParametersNames, action);
            //end of code

            if (tempTimeDetails != null) {
                collect.timeDetailsArray = tempTimeDetails;
                container.setTimeDetailsArray(tempTimeDetails);
                tempTimeDetails = null;
            }
            return true;
             }
             else
             {
                 return false;
             }
        } catch (Exception exp) {
            logger.error("Exception:", exp);
            return false;
        }
    }

    public boolean createAOForReports(Container container, PbReportCollection collect, String ReportId) {
//        if (isAOEnable) {

         String filesPath =container.getFilesPath();
         int status=0;
//        Gson gson = new Gson();

        String[] AggType = null;
         
       
//            filesPath = dao.getFilePath(session);
//       
//            filesPath = "/usr/local/cache";
        
        //
        container.setFilesPath(filesPath);
        
        QueryExecutor qryExec = new QueryExecutor();
        ReportObjectMeta roMeta = new ReportObjectMeta();
        ReportObjectQuery repObjQuery = new ReportObjectQuery();
        ArrayList<String> reportRowViewbyValues = collect.reportRowViewbyValues;
        ArrayList<String> reportColViewbyValues = collect.reportColViewbyValues;
        ArrayList<String> reportMeasureIdsList = collect.reportQryElementIds;
        PbReportQuery reportQuery;
        reportQuery = qryExec.formulateQuery(collect, userId);
        String[] elementArrList = repObjQuery.elementArrList;

        if (elementArrList != null) {
            for (String elementArrList1 : elementArrList) {
                if (reportRowViewbyValues != null && reportRowViewbyValues.size() > 0) {
                    if (!reportColViewbyValues.contains(elementArrList1)) {
                        if (!reportRowViewbyValues.contains(elementArrList1)) {
                            reportRowViewbyValues.add(elementArrList1);
                        }
                    }
                }
            }
        }

        reportQuery.setRowViewbyCols(reportRowViewbyValues);
        reportQuery.setColViewbyCols(reportColViewbyValues);
        reportQuery.isAOEnable = true;
        reportQuery.needZeroRowQuery = false;//added by mohit
        reportQuery.isQueryForAO = true;
        PbReturnObject rs = null;
        PbReturnObject retObj;
        PbAOViewerDAO dao = new PbAOViewerDAO();
        String qryforTimeInterval;
        Connection conn;
        ProgenParam connectionparam = new ProgenParam();
        PbDb pbdb = new PbDb();
         try {
        if (!container.tduration.equalsIgnoreCase("")) {
            Date date = new Date();
            Date date1;
            String qry = "";
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

            container.aoEdate = formatter.format(date);
            String noOfMonths;

            if (container.tduration.substring(2).equalsIgnoreCase("monthly")) {
            noOfMonths = "-" + (Integer.parseInt(container.tduration.substring(0, 1))-1);
                switch (ProgenConnection.getInstance().getDatabaseType()) {
                    case ProgenConnection.SQL_SERVER:
                        qry = "select CM_st_DATE from pr_day_denom where ddate = DateAdd(month," + noOfMonths + ",convert(datetime,'" + container.aoEdate + " ',120))";
                        break;
                    case ProgenConnection.MYSQL:
                        qry = "select CM_st_DATE from pr_day_denom where ddate = DATE_ADD(str_to_date('" + container.aoEdate + " ','%m/%d/%Y'), INTERVAL " + noOfMonths + " MONTH)";
                        break;
                    default:
                        qry = "select CM_st_DATE from pr_day_denom where ddate = ADD_MONTHS(to_date('" + container.aoEdate + " ','mm/dd/yyyy'), " + noOfMonths + ")";
                        break;
                }
            } else if (container.tduration.substring(2).equalsIgnoreCase("yearly")) {
                switch (ProgenConnection.getInstance().getDatabaseType()) {
                    case ProgenConnection.SQL_SERVER:
                        qry = "Select cy_st_date,ly_st_date from pr_day_denom where ddate= convert(datetime,'" + container.aoEdate + " ',120)";
                        break;
                    case ProgenConnection.MYSQL:
                        qry = "Select cy_st_date,ly_st_date from pr_day_denom where ddate= str_to_date('" + container.aoEdate + " ','%m/%d/%Y')";
                        break;
                    default:
                        qry = "Select cy_st_date,ly_st_date from pr_day_denom where ddate= to_date('" + container.aoEdate + " ','mm/dd/yyyy')";
                        break;
                }
            } else {
                dao.getCompleteDateReturnObject(container, reportMeasureIdsList);
            }
            if (!container.tduration.equalsIgnoreCase("Complete data")) {
//            try {
                    conn = connectionparam.getConnection(collect.reportQryElementIds.get(0).replace("A_", ""));
                    rs = pbdb.execSelectSQL(qry, conn);
//            } catch (Exception e) {
//            }
                if (container.tduration.subSequence(0, 1).equals("2")) {
                    date1 = rs.getFieldValueDate(0, 1);
                    container.aoSdate = formatter.format(date1);
                } else {
                    date1 = rs.getFieldValueDate(0, 0);
                    container.aoSdate = formatter.format(date1);
                }
            }
        }

        if (container.aoTimePeriod.equalsIgnoreCase("monthly")) {
            switch (ProgenConnection.getInstance().getDatabaseType()) {
                case ProgenConnection.SQL_SERVER:
                    qryforTimeInterval = "select distinct CM_END_DATE FROM PR_DAY_DENOM WHERE DDATE between  convert(datetime,'" + container.aoSdate + "',120) and  convert(datetime,'" + container.aoEdate + "',120) order by CM_END_DATE";
                    break;
                case ProgenConnection.MYSQL:
                    qryforTimeInterval = "select distinct CM_END_DATE FROM PR_DAY_DENOM WHERE DDATE between  str_to_date('" + container.aoSdate + "','%m/%d/%Y %H:%i:%s ') and  str_to_date('" + container.aoEdate + "','%m/%d/%Y %H:%i:%s ') order by CM_END_DATE";
                    break;
                default:
                    qryforTimeInterval = "select distinct CM_END_DATE FROM PR_DAY_DENOM WHERE DDATE between  to_date('" + container.aoSdate + "','mm/dd/yyyy Hh24:mi:ss ') and  to_date('" + container.aoEdate + "','mm/dd/yyyy Hh24:mi:ss ') order by CM_END_DATE";
                    break;
            }

        } else if (container.aoTimePeriod.equalsIgnoreCase("daily")) {
            switch (ProgenConnection.getInstance().getDatabaseType()) {
                case ProgenConnection.SQL_SERVER:
                    qryforTimeInterval = "select  distinct st_date FROM PR_DAY_DENOM WHERE DDATE between  convert(datetime,'" + container.aoSdate + "',120) and  convert(datetime,'" + container.aoEdate + "',120) order by st_date";
                    break;
                case ProgenConnection.MYSQL:
                    qryforTimeInterval = "select  distinct st_date FROM PR_DAY_DENOM WHERE DDATE between  str_to_date('" + container.aoSdate + "','%m/%d/%Y %H:%i:%s ') and  str_to_date('" + container.aoEdate + "','%m/%d/%Y %H:%i:%s ') order by st_date";
                    break;
                default:
                    qryforTimeInterval = "select  distinct st_date FROM PR_DAY_DENOM WHERE DDATE between  to_date('" + container.aoSdate + "','mm/dd/yyyy Hh24:mi:ss ') and  to_date('" + container.aoEdate + "','mm/dd/yyyy Hh24:mi:ss ') order by st_date";
                    break;
            }
        } else {
            switch (ProgenConnection.getInstance().getDatabaseType()) {
                case ProgenConnection.SQL_SERVER:
                    qryforTimeInterval = "select  distinct cy_end_date FROM PR_DAY_DENOM WHERE DDATE between  convert(datetime,'" + container.aoSdate + "',120) and  convert(datetime,'" + container.aoEdate + "',120) order by cy_end_date";
                    break;
                case ProgenConnection.MYSQL:
                    qryforTimeInterval = "select  distinct cy_end_date FROM PR_DAY_DENOM WHERE DDATE between  str_to_date('" + container.aoSdate + "','%m/%d/%Y %H:%i:%s ') and  str_to_date('" + container.aoEdate + "','%m/%d/%Y %H:%i:%s ') order by cy_end_date";
                    break;
                default:
                    qryforTimeInterval = "select  distinct cy_end_date FROM PR_DAY_DENOM WHERE DDATE between  to_date('" + container.aoSdate + "','mm/dd/yyyy Hh24:mi:ss ') and  to_date('" + container.aoEdate + "','mm/dd/yyyy Hh24:mi:ss ') order by cy_end_date";
                    break;
            }
        }
//        try {

            conn = connectionparam.getConnection(collect.reportQryElementIds.get(0).replace("A_", ""));
            rs = pbdb.execSelectSQL(qryforTimeInterval, conn);
//            } catch (Exception e) {
//            }

            File datafile = new File(filesPath+"/analyticalobject/M_AO_" + ReportId + ".json");

            for (int i = 0; i < rs.rowCount; i++) {

                String qryCreateTableAo = "";
                ArrayList timedetails1 = new ArrayList();
                timedetails1.add("Day");
                timedetails1.add("PRG_STD");
                Date date;
                date = rs.getFieldValueDate(i, 0);
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String todaysDate = dateFormat.format(date);
                timedetails1.add(todaysDate);

                if (container.aoTimePeriod.equalsIgnoreCase("Daily")) {
                    timedetails1.add("Day");
                } else if (container.aoTimePeriod.equalsIgnoreCase("monthly")) {
                    timedetails1.add("Month");

                } else {
                    timedetails1.add("Year");
                }

                timedetails1.add("Last Period");
                collect.timeDetailsArray = timedetails1;
                reportQuery.setTimeDetails(collect.timeDetailsArray);

                String reportQry = reportQuery.generateViewByQry();
                if (i == 0) {
                    String st_Date = reportQuery.startDate_AO;
                    String end_Date = reportQuery.endDate_AO;
                    String timeLevel_AO = reportQuery.timeLevel_AO;
                    String dateValues_AO = "stDate~" + st_Date + ";endDate~" + end_Date + ";timeLevel_AO~" + timeLevel_AO;
                    setReportObjectAO(collect, reportQuery, ReportId, dateValues_AO,container);

                    if (datafile.exists()) {
                        FileReadWrite readWrite = new FileReadWrite();
                        String metaString = readWrite.loadJSON(filesPath+"/analyticalobject/M_AO_" + ReportId + ".json");
                        Gson gson = new Gson();
                        Type tarType = new TypeToken<ReportObjectMeta>() {
                        }.getType();
                        roMeta = gson.fromJson(metaString, tarType);
                        String innerRepQry = roMeta.getFinalSqlNew_AO();
                        
                        if (innerRepQry != null && !innerRepQry.equalsIgnoreCase("")) {

                            String qryDeleteTableAO = "drop table M_AO_" + ReportId;
                            
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                            
                            innerRepQry=  innerRepQry.replaceAll(Pattern.quote("null*"), "").replaceAll(Pattern.quote("NULL*"), "").replaceAll(Pattern.quote("NULL                             *"), "")
                                    .replaceAll(Pattern.quote("null                             *"), "")
                                      .replaceAll(Pattern.quote("NULL *"), "") .replaceAll(Pattern.quote("null *"), "");
                                
                            qryCreateTableAo = "create table M_AO_" + ReportId + "  AS  " + innerRepQry;
                            
                            
//                    String qryUpdateReportMaster = "update prg_ar_report_master set AOValues='"+AOValues+"' where report_id="+ReportId;
                            }else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                qryCreateTableAo = "SELECT * into M_AO_" + ReportId + " from ( " + innerRepQry + " ) A";
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                qryCreateTableAo = "create table M_AO_" + ReportId + "  " + innerRepQry;
                            }
//                    try {
                                conn = connectionparam.getConnection(collect.reportQryElementIds.get(0).replace("A_", ""));
                                pbdb.execUpdateSQL(qryDeleteTableAO, conn);
//                    } catch (Exception e) {
//                    }

                        }
                    }
                } else if (datafile.exists()) {
                    String innerRepQry = reportQuery.finalSqlNew_AO;
                    if (innerRepQry != null && !innerRepQry.equalsIgnoreCase("")) {
                        qryCreateTableAo = " insert into  M_AO_" + ReportId + " " + innerRepQry;
//                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                                qryCreateTableAo = "";
//                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                                qryCreateTableAo = "";
//                            }

                    }
                }
//                    try {
                    conn = connectionparam.getConnection(collect.reportQryElementIds.get(0).replace("A_", ""));
                   status= pbdb.execUpdateSQLWithFlag(qryCreateTableAo, conn);
                   if(status < 0)
                   {
                        return false;
                   }
//                    } catch (Exception e) {
//                    }
                }
            reportQuery.isAOEnable = true;
            conn = connectionparam.getConnection(collect.reportQryElementIds.get(0).replace("A_", ""));
            PbReturnObject NoOfRows = pbdb.execSelectSQL("SELECT COUNT(*) FROM M_AO_" + ReportId, conn);
            if (NoOfRows != null && NoOfRows.getRowCount() > 0) {
                if (NoOfRows.getFieldValueInt(0, 0) > 500000) {
                    ArrayList<String> NewreportRowViewby = new ArrayList<String>();
                    for (int i = 0; i < collect.reportRowViewbyValues.size()-1; i++) {
                        NewreportRowViewby.add(collect.reportRowViewbyValues.get(i));
                        aoAggNo.put(collect.reportRowViewbyValues.get(i),"1");
                    }

                    insertIntoAggMaster(collect, ReportId);
                    boolean spilitAO = spilitAO(container, collect, ReportId, NewreportRowViewby);
                    if(!spilitAO)
                    {
                        return false;
                }
            }
            }
          return true;
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
            return false;
        }
    }
//added by mohit @ 24-March-2016 for split AO
    public boolean spilitAO(Container container, PbReportCollection collect, String ReportId, ArrayList<String> reportRowViewbys) {
//        if (isAOEnable) {
String filesPath =container.getFilesPath();
        NoOfSubAO++;
        int status=0;
        StringBuilder distinctq = new StringBuilder();
        Connection conn;
        ProgenParam connectionparam = new ProgenParam();
        PbDb pbdb = new PbDb();
        Integer maxRows = 0;
        ArrayList<String> NewreportRowViewby = new ArrayList<String>();
        distinctq = distinctq.append("SELECT ");

        for (int i = 0; i < reportRowViewbys.size(); i++) {
            if (!reportRowViewbys.get(i).equalsIgnoreCase("TIME")) {

                if (!(i == reportRowViewbys.size() - 1)) {
                    distinctq = distinctq.append("COUNT(DISTINCT A_").append(reportRowViewbys.get(i)).append(") AS  A_").append(reportRowViewbys.get(i)).append(",");

                } else {
                    distinctq = distinctq.append("COUNT(DISTINCT A_").append(reportRowViewbys.get(i)).append(") AS  A_").
                            append(reportRowViewbys.get(i)).
                            append(" FROM M_AO_").append(ReportId);
                }

            }
        }
        try {
            conn = connectionparam.getConnection(collect.reportQryElementIds.get(0).replace("A_", ""));
            PbReturnObject NoOfRows = pbdb.execSelectSQL(distinctq.toString(), conn);
            if (NoOfRows != null && NoOfRows.getRowCount() > 0) {
                HashMap<Integer, String> removeMap = new HashMap<>();
                for (int i = 0; i < reportRowViewbys.size(); i++) {
                    if (NoOfRows.getFieldValueInt(0, "A_" + reportRowViewbys.get(i)) > 0
                            && NoOfRows.getFieldValueInt(0, "A_" + reportRowViewbys.get(i)) > maxRows) {
                        maxRows = NoOfRows.getFieldValueInt(0, "A_" + reportRowViewbys.get(i));
                        removeMap.put(maxRows, reportRowViewbys.get(i));
                    }
                }

                if (maxRows > 10) {
                    reportRowViewbys.remove(removeMap.get(maxRows));
                     aoAggNo.put(removeMap.get(maxRows),"0");
                    QueryExecutor qryExec = new QueryExecutor();
                    ReportObjectMeta roMeta = new ReportObjectMeta();
                    ReportObjectQuery repObjQuery = new ReportObjectQuery();

                    ArrayList<String> reportRowViewbyValues = reportRowViewbys;
                    ArrayList<String> reportColViewbyValues = collect.reportColViewbyValues;
                    ArrayList<String> reportMeasureIdsList = collect.reportQryElementIds;
                    PbReportQuery reportQuery;
                    reportQuery = qryExec.formulateQuery(collect, userId);

                    reportQuery.setRowViewbyCols(reportRowViewbyValues);
                    reportQuery.setColViewbyCols(reportColViewbyValues);
                    reportQuery.isAOEnable = true;

                    reportQuery.isQueryForAO = true;
                    PbReturnObject rs = null;
                    PbReturnObject retObj;
                    PbAOViewerDAO dao = new PbAOViewerDAO();
                    String qryforTimeInterval;

                    if (!container.tduration.equalsIgnoreCase("")) {
                        Date date = new Date();
                        Date date1;
                        String qry = "";
                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

                        container.aoEdate = formatter.format(date);
                        String noOfMonths;

                        if (container.tduration.substring(2).equalsIgnoreCase("monthly")) {
                            noOfMonths = "-" + (Integer.parseInt(container.tduration.substring(0, 1))-1);
                            switch (ProgenConnection.getInstance().getDatabaseType()) {
                                case ProgenConnection.SQL_SERVER:
                                    qry = "select CM_st_DATE from pr_day_denom where ddate = DateAdd(month," + noOfMonths + ",convert(datetime,'" + container.aoEdate + " ',120))";
                                    break;
                                case ProgenConnection.MYSQL:
                                    qry = "select CM_st_DATE from pr_day_denom where ddate = DATE_ADD(str_to_date('" + container.aoEdate + " ','%m/%d/%Y'), INTERVAL " + noOfMonths + " MONTH)";
                                    break;
                                default:
                                    qry = "select CM_st_DATE from pr_day_denom where ddate = ADD_MONTHS(to_date('" + container.aoEdate + " ','mm/dd/yyyy'), " + noOfMonths + ")";
                                    break;
                            }
                        } else if (container.tduration.substring(2).equalsIgnoreCase("yearly")) {
                            switch (ProgenConnection.getInstance().getDatabaseType()) {
                                case ProgenConnection.SQL_SERVER:
                                    qry = "Select cy_st_date,ly_st_date from pr_day_denom where ddate= convert(datetime,'" + container.aoEdate + " ',120)";
                                    break;
                                case ProgenConnection.MYSQL:
                                    qry = "Select cy_st_date,ly_st_date from pr_day_denom where ddate= str_to_date('" + container.aoEdate + " ','%m/%d/%Y')";
                                    break;
                                default:
                                    qry = "Select cy_st_date,ly_st_date from pr_day_denom where ddate= to_date('" + container.aoEdate + " ','mm/dd/yyyy')";
                                    break;
                            }
                        } else {
                            dao.getCompleteDateReturnObject(container, reportMeasureIdsList);
                        }
                        if (!container.tduration.equalsIgnoreCase("Complete data")) {
//            try {
                            conn = connectionparam.getConnection(collect.reportQryElementIds.get(0).replace("A_", ""));
                            rs = pbdb.execSelectSQL(qry, conn);
//            } catch (Exception e) {
//            }
                            if (container.tduration.subSequence(0, 1).equals("2")) {
                                date1 = rs.getFieldValueDate(0, 1);
                                container.aoSdate = formatter.format(date1);
                            } else {
                                date1 = rs.getFieldValueDate(0, 0);
                                container.aoSdate = formatter.format(date1);
                            }
                        }
                    }

                    if (container.aoTimePeriod.equalsIgnoreCase("monthly")) {
                        switch (ProgenConnection.getInstance().getDatabaseType()) {
                            case ProgenConnection.SQL_SERVER:
                                qryforTimeInterval = "select distinct CM_END_DATE FROM PR_DAY_DENOM WHERE DDATE between  convert(datetime,'" + container.aoSdate + "',120) and  convert(datetime,'" + container.aoEdate + "',120)";
                                break;
                            case ProgenConnection.MYSQL:
                                qryforTimeInterval = "select distinct CM_END_DATE FROM PR_DAY_DENOM WHERE DDATE between  str_to_date('" + container.aoSdate + "','%m/%d/%Y %H:%i:%s ') and  str_to_date('" + container.aoEdate + "','%m/%d/%Y %H:%i:%s ')";
                                break;
                            default:
                                qryforTimeInterval = "select distinct CM_END_DATE FROM PR_DAY_DENOM WHERE DDATE between  to_date('" + container.aoSdate + "','mm/dd/yyyy Hh24:mi:ss ') and  to_date('" + container.aoEdate + "','mm/dd/yyyy Hh24:mi:ss ')";
                                break;
                        }

                    } else if (container.aoTimePeriod.equalsIgnoreCase("daily")) {
                        switch (ProgenConnection.getInstance().getDatabaseType()) {
                            case ProgenConnection.SQL_SERVER:
                                qryforTimeInterval = "select  distinct st_date FROM PR_DAY_DENOM WHERE DDATE between  convert(datetime,'" + container.aoSdate + "',120) and  convert(datetime,'" + container.aoEdate + "',120)";
                                break;
                            case ProgenConnection.MYSQL:
                                qryforTimeInterval = "select  distinct st_date FROM PR_DAY_DENOM WHERE DDATE between  str_to_date('" + container.aoSdate + "','%m/%d/%Y %H:%i:%s ') and  str_to_date('" + container.aoEdate + "','%m/%d/%Y %H:%i:%s ')";
                                break;
                            default:
                                qryforTimeInterval = "select  distinct st_date FROM PR_DAY_DENOM WHERE DDATE between  to_date('" + container.aoSdate + "','mm/dd/yyyy Hh24:mi:ss ') and  to_date('" + container.aoEdate + "','mm/dd/yyyy Hh24:mi:ss ')";
                                break;
                        }
                    } else {
                        switch (ProgenConnection.getInstance().getDatabaseType()) {
                            case ProgenConnection.SQL_SERVER:
                                qryforTimeInterval = "select  distinct cy_end_date FROM PR_DAY_DENOM WHERE DDATE between  convert(datetime,'" + container.aoSdate + "',120) and  convert(datetime,'" + container.aoEdate + "',120)";
                                break;
                            case ProgenConnection.MYSQL:
                                qryforTimeInterval = "select  distinct cy_end_date FROM PR_DAY_DENOM WHERE DDATE between  str_to_date('" + container.aoSdate + "','%m/%d/%Y %H:%i:%s ') and  str_to_date('" + container.aoEdate + "','%m/%d/%Y %H:%i:%s ')";
                                break;
                            default:
                                qryforTimeInterval = "select  distinct cy_end_date FROM PR_DAY_DENOM WHERE DDATE between  to_date('" + container.aoSdate + "','mm/dd/yyyy Hh24:mi:ss ') and  to_date('" + container.aoEdate + "','mm/dd/yyyy Hh24:mi:ss ')";
                                break;
                        }
                    }
//        try {

                    conn = connectionparam.getConnection(collect.reportQryElementIds.get(0).replace("A_", ""));
                    rs = pbdb.execSelectSQL(qryforTimeInterval, conn);
//            } catch (Exception e) {
//            }

                    File datafile = new File(filesPath+"/analyticalobject/M_AO_" + ReportId + "_" + NoOfSubAO + ".json");

                    for (int i = 0; i < rs.rowCount; i++) {

                        String qryCreateTableAo = "";
                        ArrayList timedetails1 = new ArrayList();
                        timedetails1.add("Day");
                        timedetails1.add("PRG_STD");
                        Date date;
                        date = rs.getFieldValueDate(i, 0);
                        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        String todaysDate = dateFormat.format(date);
                        timedetails1.add(todaysDate);

                        if (container.aoTimePeriod.equalsIgnoreCase("Daily")) {
                            timedetails1.add("Day");
                        } else if (container.aoTimePeriod.equalsIgnoreCase("monthly")) {
                            timedetails1.add("Month");

                        } else {
                            timedetails1.add("Year");
                        }

                        timedetails1.add("Last Period");
                        collect.timeDetailsArray = timedetails1;
                        reportQuery.setTimeDetails(collect.timeDetailsArray);

                        String reportQry = reportQuery.generateViewByQry();
                        if (i == 0) {
                            String st_Date = reportQuery.startDate_AO;
                            String end_Date = reportQuery.endDate_AO;
                            String timeLevel_AO = reportQuery.timeLevel_AO;
                            String dateValues_AO = "stDate~" + st_Date + ";endDate~" + end_Date + ";timeLevel_AO~" + timeLevel_AO;
                            setReportObjectAO(collect, reportQuery, ReportId + "_" + NoOfSubAO, dateValues_AO,container);

                            if (datafile.exists()) {
                                FileReadWrite readWrite = new FileReadWrite();
                                String metaString = readWrite.loadJSON(filesPath+"/analyticalobject/M_AO_" + ReportId + "_" + NoOfSubAO + ".json");
                                Gson gson = new Gson();
                                Type tarType = new TypeToken<ReportObjectMeta>() {
                                }.getType();
                                roMeta = gson.fromJson(metaString, tarType);
                                String innerRepQry = roMeta.getFinalSqlNew_AO();
                                if (innerRepQry != null && !innerRepQry.equalsIgnoreCase("")) {

                                    String qryDeleteTableAO = "drop table M_AO_" + ReportId + "_" + NoOfSubAO;
                                    qryCreateTableAo = "create table M_AO_" + ReportId + "_" + NoOfSubAO + "  AS  " + innerRepQry;
//                    String qryUpdateReportMaster = "update prg_ar_report_master set AOValues='"+AOValues+"' where report_id="+ReportId;
                                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                        qryCreateTableAo = "SELECT * into M_AO_" + ReportId + "_" + NoOfSubAO + " from ( " + innerRepQry + " ) A";
                                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                        qryCreateTableAo = "create table M_AO_" + ReportId + "_" + NoOfSubAO + "  " + innerRepQry;
                                    }
//                    try {
                                    conn = connectionparam.getConnection(collect.reportQryElementIds.get(0).replace("A_", ""));
                                    pbdb.execUpdateSQL(qryDeleteTableAO, conn);
//                    } catch (Exception e) {
//                    }

                                }
                            }
                        } else if (datafile.exists()) {
                            String innerRepQry = reportQuery.finalSqlNew_AO;
                            if (innerRepQry != null && !innerRepQry.equalsIgnoreCase("")) {
                                qryCreateTableAo = " insert into  M_AO_" + ReportId + "_" + NoOfSubAO + " " + innerRepQry;
//                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                                qryCreateTableAo = "";
//                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                                qryCreateTableAo = "";
//                            }

                            }
                        }
//                    try {
                        conn = connectionparam.getConnection(collect.reportQryElementIds.get(0).replace("A_", ""));
                        status= pbdb.execUpdateSQLWithFlag(qryCreateTableAo, conn);
                   if(status < 0)
                   {
                        return false;
                   }
//                    } catch (Exception e) {
//                    }
    }
                    reportQuery.isAOEnable = true;
                   insertIntoAggDetails(collect, ReportId,removeMap.get(maxRows));
                    conn = connectionparam.getConnection(collect.reportQryElementIds.get(0).replace("A_", ""));
                    NoOfRows = pbdb.execSelectSQL("SELECT COUNT(*) FROM M_AO_" + ReportId + "_" + NoOfSubAO, conn);

                    if (NoOfRows != null && NoOfRows.getRowCount() > 0) {
                        if (NoOfRows.getFieldValueInt(0, 0) > 500000) {
                            spilitAO(container, collect, ReportId, reportRowViewbys);
                        }
                    }
                }
            }

            return true;
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
            return false;
        }
    }

    public void setReportObjectAO(PbReportCollection collect, PbReportQuery reportQuery, String reportId, String dateValues_AO,Container container) {
        ReportObjectMeta roMeta = new ReportObjectMeta();
        String filesPath =container.getFilesPath();
        String OViewByCol_AO = "";
        String OmViewByCol_AO = "";
        String finalViewByCol_AO = "";
        roMeta.setOsql_AO(reportQuery.osql_AO);
        roMeta.setOViewByCol_AO(reportQuery.OViewByCol_AO);
//        roMeta.setOViewByCol_AO(OViewByCol_AO);
        roMeta.setOorderByCol_AO(reportQuery.OorderByCol_AO);
        roMeta.setOmsql_AO(reportQuery.omsql_AO);
        roMeta.setOmViewByCol_AO(reportQuery.OmViewByCol_AO);
//        roMeta.setOmViewByCol_AO(OmViewByCol_AO);
        roMeta.setOmorderByCol_AO(reportQuery.OmorderByCol_AO);
        roMeta.setColOrderByCol_AO(reportQuery.ColOrderByCol_AO);
        roMeta.setOsqlGroup_AO(reportQuery.osqlGroup_AO);
        roMeta.setFinalViewByCol_AO(reportQuery.finalViewByCol_AO);
//        roMeta.setFinalViewByCol_AO(finalViewByCol_AO);
        roMeta.setoWrapper_AO(reportQuery.oWrapper_AO);
        roMeta.setTableName_AO(reportQuery.tableName_AO);
        roMeta.setFilters_AO(reportQuery.filters_AO);
        roMeta.setIsAOEnable(reportQuery.isAOEnable);
        roMeta.setFinalSqlNew_AO(reportQuery.finalSqlNew_AO);
        roMeta.setDateValues_AO(dateValues_AO);
        roMeta.setViewIds(collect.reportParamIds);
        roMeta.setViewNames(collect.reportParamNames);
        roMeta.setMeasIds(collect.reportQryElementIds);
        roMeta.setMeasNames(collect.reportQryColNames);
        roMeta.setAggregations(collect.reportQryAggregations);
        Gson gson = new Gson();
        FileReadWrite fileReadWrite = new FileReadWrite();
        File file;
        file = new File("/usr/local/cache/analyticalobject");
        String path = file.getAbsolutePath();
        String fName = path + File.separator + "M_AO_" + reportId + ".json";
        File f = new File(path);
        File file1 = new File(fName);
        f.mkdirs();
        File datafile = new File(filesPath+"/analyticalobject/M_AO_" + reportId + ".json");
        if (!datafile.exists()) {
            try {
                datafile.createNewFile();
            } catch (IOException ex) {
                logger.error("Exception: ", ex);
            }
        }
        fileReadWrite.writeToFile(filesPath+"/analyticalobject/M_AO_" + reportId + ".json", gson.toJson(roMeta));
    }

    public String saveReport(Container container, String reportName, String reportDesc, int reportId, String date, String userId, List<String> createDateAndUser, String Gtregion) throws Exception {

        HashMap parametersMap = container.getParametersHashMap();
        HashMap TableHashMap = container.getTableHashMap();
        container.getTimeDetailsArray();
        container.getNoOfDays();
        // HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
        String UserFolderIds = (String) parametersMap.get("UserFolderIds");
        ArrayList queries = new ArrayList();

        ArrayList<String> displayColumns = container.getDisplayColumns();
        ArrayList<String> originnalColumns = container.getOriginalColumns();
        ArrayList<String> displayLabels = container.getDisplayLabels();
        ArrayList<String> timePeriodsList = container.getAllColumns();
        StringBuffer Parameters = new StringBuffer();
        StringBuffer ParametersOrder = new StringBuffer();
        StringBuffer QueryColumns = new StringBuffer();
        StringBuffer QueryColumnsOrder = new StringBuffer();

        ArrayList params = (ArrayList) parametersMap.get("Parameters");
        ArrayList timeDetails = (ArrayList) parametersMap.get("TimeDetailstList");
        HashMap timeDimHashMap = (HashMap) parametersMap.get("TimeDimHashMap");
        ArrayList timeParams = (ArrayList) parametersMap.get("timeParameters");
        ArrayList repExclude = (ArrayList) parametersMap.get("repExclude");
        ArrayList cepExclude = (ArrayList) parametersMap.get("cepExclude");
        ArrayList REP_Elements = (ArrayList) TableHashMap.get("REP");
        ArrayList CEP_Elements = (ArrayList) TableHashMap.get("CEP");
        LinkedHashMap currentTimeDetails = container.getCurrentTimeDetails();
        ArrayList<String> Measures = new ArrayList<String>();
        String mapEnabled;
        String timePeriods = "";
        for (int i = 0; i < REP_Elements.size(); i++) {
            if (REP_Elements.get(i).toString().lastIndexOf("_G") != -1) {
                REP_Elements.remove(i);
            }
        }
        if (!timePeriodsList.isEmpty()) {
            for (int i = 0; i < timePeriodsList.size(); i++) {
                timePeriods = timePeriods + "," + timePeriodsList.get(i).toString();
            }
        }
        int viewByCount = (container.getViewByCount());
        String QueryColumnsOrder1 = " case ";
        Measures = container.getTableDisplayMeasures();
        // added by ramesh janakuttu from line no 5199       
        String graphmeasId = null;
        ArrayList graphmeasures = null;
        String[] graphColumns = null;
        String graphId[] = null;
        HashMap singleGraphDetails = null;
        String[] PrevbarChartColumnNames = null;
        //
        //String showtable = (String) getShowGraphTable();
        ArrayList<String> excelMsrLst = new ArrayList<String>();

        for (int i = viewByCount; i < originnalColumns.size(); i++) {

            if (!RTMeasureElement.isRunTimeMeasure(originnalColumns.get(i).toString())) {
                QueryColumns.append("," + originnalColumns.get(i).toString().replace("A_", ""));
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    QueryColumnsOrder1 += " when element_id = " + originnalColumns.get(i).toString().replace("A_", "") + " then " + (i + 1) + " ";
                } else {
                    QueryColumnsOrder.append("," + originnalColumns.get(i).toString().replace("A_", "") + "," + (i + 1));
                }
            }
        }

        if (Integer.parseInt(container.getColumnViewByCount()) == 0) {
            //start of code by Nazneen %wise saving if %wise with absolute is not there
            for (int i = 0; i < displayColumns.size(); i++) {
                //if (originnalColumns.get(i).toString().lastIndexOf("_percentwise") == -1) {
                if (displayColumns.get(i).toString().contains("_percentwise") || displayColumns.get(i).toString().contains("_pwst") || displayColumns.get(i).toString().contains("_percentwise_rt")) {
                    String orgCol = displayColumns.get(i).toString().replace("_percentwise_rt", "").replace("_percentwise", "").replace("_pwst", "").replace("A_", "");
                    if (!QueryColumns.toString().contains(orgCol)) {
                        QueryColumns.append("," + orgCol.toString());
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            QueryColumnsOrder1 += " when element_id = " + orgCol.toString() + " then " + (i + 1) + " ";
                        } else {
                            QueryColumnsOrder.append("," + orgCol.toString() + "," + (i + 1));
                        }
                    }
                }
            }
            //end of code by Nazneen %wise saving if %wise with absolute is not there
        }
        QueryColumnsOrder1 += " else 10000 end ";
        String ParametersOrder1 = " case ";

        mapEnabled = container.isMapEnabled() ? "Y" : "N";

        //DAO.setParameterGroupMap(container.getParameterGroupAnalysisHashMap());//added by santhosh.k on 10-03-2010 for inserting parameter grouping hashmap into data base
        queries = DAO.insertAOMaster(reportId, reportName, reportDesc, UserFolderIds, mapEnabled, container.getReportCollect().getCustomSequence(), container.getReportCollect().getTransposeFormatMap(), container.getReportCollect().getGoalSeekBasicandAdhoc(), container.getReportCollect().getGoalandPercent(), userId, container.getReportCollect().getViewByValues(), container.getReportCollect().getGroupName(), container.getReportCollect().getGlobalValues(), container.getReportCollect().getGoalSeekTimeIndvidual(), container.getReportCollect().getProdAndViewName(), container, Gtregion);//inserting report master and details
        DAO.saveReport(queries);
//        if (this.isOverWriteExistingReport()) {
//            DAO.updateCurrentDateAnduserName(reportId, createDateAndUser, userId);
//        }

        String newReportId = null;
//        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//            String ReportIdQry = "";
//            if (!this.overWriteExistingReport) {
//                ReportIdQry = "select ident_current('PRG_AR_REPORT_MASTER')";
//                PbDb pbdb = new PbDb();
//                PbReturnObject retObj = pbdb.execSelectSQL(ReportIdQry);
//                newReportId = retObj.getFieldValueString(0, 0);
//                reportId = Integer.parseInt(newReportId);
//            } else {
//                newReportId = String.valueOf(reportId);
//            }
//        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//            if (!this.overWriteExistingReport) {
//                String qry = "SELECT LAST_INSERT_ID(REPORT_ID) from PRG_AR_REPORT_MASTER order by 1 desc limit 1";
//                PbReturnObject retobj = new PbReturnObject();
//                retobj = super.execSelectSQL(qry);
//                reportId = Integer.parseInt(retobj.getFieldValueString(0, 0));
//                newReportId = String.valueOf(reportId);
//            } else {
//                newReportId = String.valueOf(reportId);
//            }
//        } else {
        newReportId = String.valueOf(reportId);
        //}

        queries.clear();

        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                Parameters.append("," + String.valueOf(params.get(i)).replace("A_", ""));
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    ParametersOrder1 += " when element_id = " + String.valueOf(params.get(i)).replace("A_", "") + " then " + (i + 1) + " ";
                } else {
                    ParametersOrder.append("," + String.valueOf(params.get(i)).replace("A_", "") + "," + (i + 1));
                }
            }
            ParametersOrder1 += " else 10000 end ";
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                queries = DAO.insertAOParamDetails(reportId, Parameters.substring(1), ParametersOrder1, queries, container);//inserting report param details
            } else {
                queries = DAO.insertAOParamDetails(reportId, Parameters.substring(1), ParametersOrder.substring(1), queries, container);//inserting report param details
            }
        }
//        if (timeDetails != null && timeDetails.size() > 0) {
//            queries = DAO.insertReportTimeDimensions(timeDetails, timeDimHashMap, reportId, queries, timeParams, date, currentTimeDetails, this.isOverWriteExistingReport());//inserting time dimensions and time details
//        } else {
//            
//            queries = DAO.insertNonStdReportTimeDimensions(reportId, queries);//inserting non standard time dimensions
//        }
//
//        DAO.saveReport(queries);
//
//
//
//        queries.clear();
//        queries = DAO.insertReportViewByMaster(REP_Elements, CEP_Elements, reportId, queries, repExclude, cepExclude, this.isOverWriteExistingReport(), container);//inserting time dimensions and time details
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            queries = DAO.insertAOMeasuresDetails(reportId, QueryColumns.substring(1), QueryColumnsOrder1, queries, container);//inserting into query details

        } else {
            queries = DAO.insertAOMeasuresDetails(reportId, QueryColumns.substring(1), QueryColumnsOrder.substring(1), queries, container);//inserting into query details
        }
//
//        DAO.saveReport(queries);
//
//        queries = new ArrayList();
//        DAO.setTableHashMap(TableHashMap);
//        queries = DAO.insertReportTableMaster(reportId, reportName, container, queries, this.isOverWriteExistingReport());//inserting into report master and report master details        
//
//        DAO.saveReport(queries);
        return newReportId;

    }
    //added by mohit @ 24-March-2016 for split AO
     public void insertIntoAggMaster(PbReportCollection collect, String ReportId) {
         String distinctq= "";
        Connection conn;
        ProgenParam connectionparam = new ProgenParam();
        PbDb pbdb = new PbDb();
        Integer maxRows = 0;
        ArrayList<String> NewreportRowViewby = new ArrayList<>();
         try {
             Object ob[]=new Object[6];
             execUpdateSQL("DELETE FROM AO_AGG_MASTER WHERE  AO_NAME='"+"M_AO_"+ReportId+"'");
             execUpdateSQL("DELETE FROM AO_AGG_DETAILS WHERE  AO_NAME='"+"M_AO_"+ReportId+"'");
             
//        PbReturnObject AO_AGG_MASTER_ID = pbdb.execSelectSQL("SELECT MAX(AO_AGG_MASTER_ID)+1 FROM AO_AGG_MASTER");
//        if(AO_AGG_MASTER_ID!=null && AO_AGG_MASTER_ID.getRowCount()>0)
//        {
      distinctq = "INSERT INTO AO_AGG_MASTER(AO_NAME,\n" +
"AO_COLUMN_NAME,\n" +
"MULTIPLE_NO,\n" +
"DIVISION_NO,\n" +
"PLACE_VALUE,\n" +
"ROLE_ID) VALUES('&','&',&,&,&,&)";
            for (int i = 0; i < collect.reportRowViewbyValues.size()-1; i++) {
ob[0]="M_AO_"+ReportId;
ob[1]="A_"+collect.reportRowViewbyValues.get(i);
ob[2]=(int)Math.pow(10, i);
ob[3]=(int)Math.pow(10, i+1);
ob[4]=1;
ob[5]=collect.reportBizRoles[0];
          String buildQuery = buildQuery(distinctq, ob);
        execUpdateSQL(buildQuery);
}
            
//        }
         
         }catch (Exception ex) {
                logger.error("Exception: ", ex);
            }
        
     }
     public void insertIntoAggDetails(PbReportCollection collect, String ReportId,String colname) {
         String distinctq= "";
        Connection conn;
        ProgenParam connectionparam = new ProgenParam();
        PbDb pbdb = new PbDb();
        Integer maxRows = 0;
        ArrayList<String> NewreportRowViewby = new ArrayList<>();
          String NEW_AO_NAME="";
          String query1="";
           StringBuilder AO_AGG_NUMBER=new StringBuilder();
           for (int i = 0; i < collect.reportRowViewbyValues.size()-1; i++) {
               AO_AGG_NUMBER=AO_AGG_NUMBER.append(aoAggNo.get(collect.reportRowViewbyValues.get(i)));
           }
           AO_AGG_NUMBER=AO_AGG_NUMBER.reverse();
        if(NoOfSubAO==0)
        {
            NEW_AO_NAME="M_AO_"+ReportId;
            query1="SELECT AO_AGG_MASTER_ID FROM AO_AGG_MASTER WHERE AO_NAME='"+"M_AO_"+ReportId+"'";
        }else
        {
            NEW_AO_NAME="M_AO_"+ReportId+"_"+NoOfSubAO;
             query1="SELECT AO_AGG_MASTER_ID FROM AO_AGG_MASTER WHERE AO_NAME='"+"M_AO_"+ReportId+"' and AO_COLUMN_NAME='A_"+colname+"'";
        }
         try {
             Object ob[]=new Object[6];
        PbReturnObject AO_AGG_MASTER_ID = pbdb.execSelectSQL(query1);
        if(AO_AGG_MASTER_ID!=null && AO_AGG_MASTER_ID.getRowCount()>0)
        {
      distinctq = "INSERT INTO AO_AGG_DETAILS(AO_AGG_MASTER_ID,\n" +
"AO_NAME,\n" +
"AO_AGG_NUMBER,\n" +
"ROLE_ID,\n" +
"IS_TIME_AGG,\n" +
"NEW_AO_NAME\n" +
") VALUES(&,'&',&,&,'&','&')";
ob[0]=AO_AGG_MASTER_ID.getFieldValueInt(0, 0);
ob[1]="M_AO_"+ReportId;
ob[2]=AO_AGG_NUMBER.toString();
ob[3]=collect.reportBizRoles[0];
ob[4]="Y";
ob[5]=NEW_AO_NAME;
          String buildQuery = buildQuery(distinctq, ob);
        execUpdateSQL(buildQuery);
        } }catch (Exception ex) {
                logger.error("Exception: ", ex);
            }
        
     }
}
