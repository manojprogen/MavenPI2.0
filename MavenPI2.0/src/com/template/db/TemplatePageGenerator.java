/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.template.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.report.FileReadWrite;
import com.progen.report.ReportObjectMeta;
import com.progen.report.XtendAdapter;
import com.progen.report.DashboardChartData;
import com.progen.report.ReportObjectQuery;
import com.progen.report.query.ProgenAOQuery;
import com.template.action.ManagementResourceBundleMySql;
import com.template.action.ManagementResourceBundleOracle;
import com.template.action.ManagementResourceBundleSqlServer;
import com.template.meta.ManagementTemplateMeta;
import com.template.meta.TemplateInfoMeta;
import com.template.util.TemplateContainer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author mayank
 */
public class TemplatePageGenerator {

    public static Logger logger = Logger.getLogger(TemplatePageGenerator.class);
    //Added by Ashutosh

    static {
        XtendAdapter.savePoint = true;
    }
    private ManagementTemplateMeta reportMeta;
    private TemplateInfoMeta templateMeta;
    Gson gson = new Gson();

       private ResourceBundle getResourceBundle(){
        ResourceBundle resourceBundle;
        switch (ProgenConnection.getInstance().getDatabaseType()) {
            case ProgenConnection.SQL_SERVER:
                resourceBundle =  new ManagementResourceBundleSqlServer();
                break;
            case ProgenConnection.ORACLE:
                resourceBundle = new ManagementResourceBundleOracle();
                break;
            default:
                resourceBundle = new ManagementResourceBundleMySql();
                break;
        }
        return resourceBundle;
    }
    
    public String getTemplatePage(HttpServletRequest request, String reportId, String reportName,String firstTempPage) {
        String report = "false";
        String bizzRoleName = "";
        HttpSession session = request.getSession(false);
        String fileLocation = getFilePath(session);
        TemplateContainer container = new TemplateContainer();
         Type tarType = new TypeToken<List<String>>(){}.getType();
//            HttpSession session = SessionListener.getSession(SessionListener.getSessionID());
        String firstPage = "";
        if(request.getParameter("reportId")!=null){
            reportId = request.getParameter("reportId");
        }
        PbReturnObject retobj = null;
        String userId = "";
         String roleId = "";
        if (session != null) {
            if (session.getAttribute("USERID") != null) {
                userId = session.getAttribute("USERID").toString();
            } else {
                userId = XtendAdapter.userID;
            }
        }
            PbDb pbdb = new PbDb();
        String roleQuery = getResourceBundle().getString("getRoleId");
        try {
            Object[] obj = new Object[1];
            obj[0]= reportId;
        roleQuery = pbdb.buildQuery(roleQuery, obj);
            retobj=pbdb.execSelectSQL(roleQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if(retobj!=null && retobj.rowCount>0){
            roleId = retobj.getFieldValueString(0, "FOLDER_ID");
            bizzRoleName = retobj.getFieldValueString(0, "FOLDER_NAME");
        }
        List<String> templatePage = new ArrayList<>();
       String templateId = (String)session.getAttribute("templateId");
        templatePage = gson.fromJson(session.getAttribute("tempPages").toString(),tarType);
        List<String> pageSequqnce = new ArrayList<>();
        Map<String, Map<String, Map<String, String>>> reportPageMapping = null;
        try {
//            String pageMappingQuery = "select * from prg_report_page_mapping where PAGE_ID='"+firstTempPage+"' and report_id='" + reportId + "' and USER_ID='"+userId+"'";
            String pageMappingQuery = "select * from prg_report_page_mapping where  report_id='" + reportId + "'";
            retobj = pbdb.executeSelectSQL(pageMappingQuery);
            reportPageMapping = new HashMap<>();
            if (templatePage != null && templatePage.size()>0) {
                Map<String, Map<String, String>> pageMapping = new HashMap<>();
                for (int c = 0; c < templatePage.size(); c++) {
                    Map<String, String> pageInfo = new HashMap<>();
                    String[] pageInfoArray = templatePage.get(c).split(":");
      String reportAOId = pageInfoArray[0].replace("m_", "");
       String pageId = pageInfoArray[2];
//                     String pageId = "default";
//                    String pageId = retobj.getFieldValueString(c, 1);
                    String pageLabel = pageInfoArray[1];
                    String pageSequence = String.valueOf(c+1);
                    pageSequqnce.add(pageSequence);

                    pageInfo.put("pageLabel", pageLabel);
                    pageInfo.put("pageSequence", pageSequence);
                    pageInfo.put("reportId", reportAOId);
                    pageMapping.put(pageId, pageInfo);
                    if (c == 0) {
                        firstPage = pageId;
                    }
                }
                reportPageMapping.put(templateId, pageMapping);
            }
        } catch (Exception ex) {
        }
        if (request.getParameter("pageChange") != null && request.getParameter("pageChange").isEmpty() != true) {
            firstPage = request.getParameter("pageChange");
        }
        
//            HashMap roleMap = (HashMap) session.getAttribute("roleMap");
       
//firstPage="default";
        String newPage = firstPage;
        if (firstPage.equalsIgnoreCase("default") || !(new File(fileLocation + "/SavePoint/" + userId + "/" + roleId + "/" + reportId + "/" + reportId + "_data" + firstPage + ".json").exists())) {
            firstPage = "";
        }
//        firstPage = "_page3";
//            }
//roleId = "51";
        if (XtendAdapter.savePoint && new File(fileLocation + "/SavePoint/" + userId + "/" + roleId + "/" + reportId + "/" + reportId + "_data" + firstPage + ".json").exists()) {

            String goPath = fileLocation + "/analyticalobject/R_GO_" + reportId + ".json";
            String metaFilePath = fileLocation + "/SavePoint/" + userId + "/" + roleId + "/" + reportId + "/" + reportId + "_data" + firstPage + ".json";
            //Gson gson = new Gson();
            FileReadWrite fileReadWrite = new FileReadWrite();
            try {
                File metafile = new File(metaFilePath);
                if (metafile.exists()) {
//        String data = fileReadWrite.loadJSON(filePath);
                    String meta = fileReadWrite.loadJSON(metaFilePath);
                    String goData = fileReadWrite.loadJSON(goPath);
                    Map map = new HashMap();
                    map.put("meta", meta);
                    map.put("data", "");
                    Map<String, Map<String, String>> mapData;
                    reportMeta = new ManagementTemplateMeta();
                    ReportObjectMeta goMeta = new ReportObjectMeta();
//                    Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
//                    }.getType();
                    Type metaType = new TypeToken<ManagementTemplateMeta>() {
                    }.getType();
                    Type goType = new TypeToken<ReportObjectMeta>() {
                    }.getType();
                    Map<String, List<Map<String, String>>> dataMap = new HashMap<String, List<Map<String, String>>>();
//        mapData = gson.fromJson(data, tarType1);
//        dataMap = gson.fromJson(data, tarType1);
                    reportMeta = gson.fromJson(meta, metaType);
                    goMeta = gson.fromJson(goData, goType);
//        if(reportMeta.getViewbys()==null){

                    reportMeta.setViewbys(goMeta.getViewNames());
                    reportMeta.setViewbysIds(goMeta.getViewIds());
//        }
                    reportMeta.setMeasures(goMeta.getMeasNames());
                    reportMeta.setMeasuresIds(goMeta.getMeasIds());
                    reportMeta.setAggregations(goMeta.getAggregations());
                    reportMeta.setReportPageMapping(reportPageMapping);
                    map.put("meta", gson.toJson(reportMeta));
//      container.setDbData(mapData);
                    container.setDbrdData(dataMap);
                    container.setReportMeta(reportMeta);
                    map.put("pageSequence", pageSequqnce);
                    map.put("currentPage", newPage);
                    report = gson.toJson(map);
                }
            } catch (Exception e) {
                report = "false";
            }
            XtendAdapter.refreshlocal = true;
            return report;

        } else {
            newPage = firstPage;
            if (firstPage.equalsIgnoreCase("default")) {
                firstPage = "";
            }
            String filePath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + firstPage + ".json";
            String goPath = fileLocation + "/analyticalobject/R_GO_" + reportId + ".json";
            String metaFilePath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data" + firstPage + ".json";
//        String datafilePath = "/usr/local/cache/Sales/"+reportName+"_"+reportId+"/"+reportName+"_"+reportId+".csv";
            //Gson gson = new Gson();
            FileReadWrite fileReadWrite = new FileReadWrite();
            try {
                File file = new File(filePath);
                File metafile = new File(metaFilePath);
                if (file.exists() && metafile.exists()) {
                    String data = fileReadWrite.loadJSON(filePath);
                    String meta = fileReadWrite.loadJSON(metaFilePath);
                    String goData = fileReadWrite.loadJSON(goPath);
                    Map map = new HashMap();
                    map.put("meta", meta);
                    map.put("data", data);
                    Map<String, Map<String, String>> mapData;
                    ManagementTemplateMeta reportMeta = new ManagementTemplateMeta();
                    ReportObjectMeta goMeta = new ReportObjectMeta();
                    Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
                    }.getType();
                    Type metaType = new TypeToken<ManagementTemplateMeta>() {
                    }.getType();
                    Type goType = new TypeToken<ReportObjectMeta>() {
                    }.getType();
                    Map<String, List<Map<String, String>>> dataMap = new HashMap<String, List<Map<String, String>>>();
                    mapData = gson.fromJson(data, tarType1);
                    dataMap = gson.fromJson(data, tarType1);
                    reportMeta = gson.fromJson(meta, metaType);
                    goMeta = gson.fromJson(goData, goType);
//        if(reportMeta.getViewbys()==null){

                    reportMeta.setViewbys(goMeta.getViewNames());
                    reportMeta.setViewbysIds(goMeta.getViewIds());
//        }
                    reportMeta.setMeasures(goMeta.getMeasNames());
                    reportMeta.setMeasuresIds(goMeta.getMeasIds());
                    reportMeta.setAggregations(goMeta.getAggregations());
                    reportMeta.setReportPageMapping(reportPageMapping);
                    map.put("meta", gson.toJson(reportMeta));
//                        container.setDbrdData(dataMap);
                    container.setReportMeta(reportMeta);
                    map.put("pageSequence", pageSequqnce);
                    report = gson.toJson(map);
                } else if (metafile.exists()) {
//                String data = fileReadWrite.loadJSON(filePath);
                    String meta = fileReadWrite.loadJSON(metaFilePath);
                    String goData = fileReadWrite.loadJSON(goPath);
                    Map map = new HashMap();
                    map.put("meta", meta);
//                    map.put("data", "");
                    Map<String, Map<String, String>> mapData;
                    ManagementTemplateMeta reportMeta = new ManagementTemplateMeta();
                    ReportObjectMeta goMeta = new ReportObjectMeta();
                    Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
                    }.getType();
                    Type metaType = new TypeToken<ManagementTemplateMeta>() {
                    }.getType();
                    Type goType = new TypeToken<ReportObjectMeta>() {
                    }.getType();
                    Map<String, List<Map<String, String>>> dataMap = new HashMap<String, List<Map<String, String>>>();
//                    mapData = gson.fromJson(data, tarType1);
//                    dataMap = gson.fromJson(data, tarType1);
                    reportMeta = gson.fromJson(meta, metaType);
                    goMeta = gson.fromJson(goData, goType);
//        if(reportMeta.getViewbys()==null){

                    reportMeta.setViewbys(goMeta.getViewNames());
                    reportMeta.setViewbysIds(goMeta.getViewIds());
//        }
                    reportMeta.setMeasures(goMeta.getMeasNames());
                    reportMeta.setMeasuresIds(goMeta.getMeasIds());
                    reportMeta.setAggregations(goMeta.getAggregations());
                    map.put("meta", gson.toJson(reportMeta));
//                        container.setDbData(mapData);
//                        container.setDbrdData(dataMap);
                    container.setReportMeta(reportMeta);
                    map.put("currentPage", newPage);
                    report = gson.toJson(map);

                } else {
                    report = "false";
                }
            } catch (Exception e) {
                report = "false";
            }
            XtendAdapter.savePoint = true;
            XtendAdapter.refreshlocal = false;
            return report;
        }
    }

    public String getKPIValue(HttpServletRequest request) throws SQLException {
        Connection conn = null;

        double result;
        String strresult = "";
        String aoAsGoId = request.getParameter("aoAsGoId");
        HttpSession session = request.getSession(false);
        String userid = String.valueOf(session.getAttribute("USERID"));
        String reportId = "4243";
        String fileLocation;
        if (session != null) {
            fileLocation = getFilePath(session);
        } else {
            fileLocation = "/usr/local/cache";
        }
//        List<String> measNameList = new ArrayList<>();
//        List<String> measIdList = new ArrayList<>();

        List<Map<String, String>> list = new ArrayList<>();
//        String userColName = "";
        String filterClause = "";
        List<String> timeDetails = new ArrayList<>();
        ArrayList<String> timeDetailsGO = new ArrayList<>();

//        PbReturnObject retObj = new PbReturnObject();
        PbDb pbdb = new PbDb();
        String AO_Name = "";
        aoAsGoId = "473";
        if (aoAsGoId != null && !aoAsGoId.equalsIgnoreCase("null") && !aoAsGoId.equalsIgnoreCase("")) {

            AO_Name = "M_AO_" + aoAsGoId;
        } else {

            AO_Name = "R_GO_4243";
        }

        reportMeta = new ManagementTemplateMeta();
        TemplateManagementDAO dao = new TemplateManagementDAO();
        reportMeta = dao.setTemplateForm(request);
        String action = request.getParameter("action");
        Map<String, List<String>> drillMap = new HashMap<>();
        HashMap<String, List> drillmapFromSet = new HashMap<>();
        HashMap<String, List> drillmapFromSetAgg = new HashMap<>();
        HashMap<String, List> filtermapFromSet = new HashMap<>();
        HashMap<String, List> localfiltermapFromSet = new HashMap<>();
//        HashMap<String, String> ResultSet = new HashMap<String, String>();
        timeDetails = reportMeta.getTimedetails();
        for (String timeString : timeDetails) {
            timeDetailsGO.add(timeString);
        }
        List<String> resultSet = new ArrayList<>();

        String chartIdRequest = request.getParameter("chartId");
        DashboardChartData chartDetails = reportMeta.getChartData().get(chartIdRequest);
        if ((reportMeta.getDrills() != null && !reportMeta.getDrills().toString().equalsIgnoreCase("")) || reportMeta.getFilterMap() != null || (reportMeta.getChartData() != null && reportMeta.getChartData().get(chartIdRequest) != null && reportMeta.getChartData().get(chartIdRequest).getFilters() != null) || reportMeta.getChartData().get(chartIdRequest).getLocalDrills() != null) {
            if (reportMeta.getDrills() != null) {
                drillMap = reportMeta.getDrills();
                Set<Entry<String, List<String>>> set = drillMap.entrySet();
                for (Entry<String, List<String>> entry : set) {
                    //sandeep
                    if (entry.getKey().equalsIgnoreCase("TIME")) {
                        String timeDeatailsVar = timeDetailsGO.get(3).toString();
                        String keyValue = "";
                        if (timeDeatailsVar.equalsIgnoreCase("Month")) {
                            keyValue = "MONTH_YEAR";
                        } else if (timeDeatailsVar.equalsIgnoreCase("Qtr")) {
                            keyValue = "QTR_YEAR";
                        } else if (timeDeatailsVar.equalsIgnoreCase("Year")) {
                            keyValue = "YEAR_NAME";
                        } else {
                            keyValue = entry.getKey();
                        }
                        drillmapFromSetAgg.put(keyValue, entry.getValue());
//        drillmapFromSet.put(keyValue, entry.getValue());
                    } else {
                        String timeViewBy = dao.getTimeDimensionView(entry.getKey(), reportMeta);
                        if (timeViewBy.equalsIgnoreCase("Month")) {

                            drillmapFromSetAgg.put(entry.getKey(), entry.getValue());
                        } else if (timeViewBy.equalsIgnoreCase("Qtr")) {
                            drillmapFromSetAgg.put(entry.getKey(), entry.getValue());
                        } else if (timeViewBy.equalsIgnoreCase("Year")) {
                            drillmapFromSetAgg.put(entry.getKey(), entry.getValue());
                        } else {
                            drillmapFromSet.put(entry.getKey(), entry.getValue());

                        }
                    }

                }
            } else if (reportMeta.getFilterMap() != null) {
                Set<Entry<String, List<String>>> filterset1 = reportMeta.getFilterMap().entrySet();
                for (Entry<String, List<String>> entry : filterset1) {
                    filtermapFromSet.put(entry.getKey(), entry.getValue());
                }
            }

            if (reportMeta.getChartData() != null && reportMeta.getChartData().get(chartIdRequest) != null && reportMeta.getChartData().get(chartIdRequest).getFilters() != null) {
                HashMap<String, List> filtermapFromSetLocal = new HashMap<String, List>();
                filtermapFromSetLocal = filtermapFromSet;
                Set<Entry<String, List<String>>> filterset = reportMeta.getChartData().get(chartIdRequest).getFilters().entrySet();
                for (Entry<String, List<String>> entry : filterset) {
                    if ((reportMeta.getChartData().get(chartIdRequest).getOthersL() == null || reportMeta.getChartData().get(chartIdRequest).getOthersL().equalsIgnoreCase("N"))) {
                        List<String> listVal = new ArrayList<String>();
                        if (filtermapFromSetLocal.get(entry.getKey()) != null) {
                            listVal = filtermapFromSetLocal.get(entry.getKey());
                            listVal.addAll(listVal.size(), entry.getValue());
                        } else {
                            listVal = entry.getValue();
                        }
                        filtermapFromSetLocal.put(entry.getKey(), listVal);
                    }
                    if (reportMeta.getChartData().get(chartIdRequest).getOthersL() == null || reportMeta.getChartData().get(chartIdRequest).getOthersL().equalsIgnoreCase("N")) {
                        filtermapFromSetLocal = new HashMap<>();
//             HashMap<String, List> localfiltermapFromSet = new HashMap<String, List>();
                        List<String> listVal = new ArrayList<>();
                        if (filtermapFromSetLocal.get(entry.getKey()) != null) {
                            listVal = filtermapFromSetLocal.get(entry.getKey());
                            listVal.addAll(listVal.size(), entry.getValue());
                        } else {
                            listVal = entry.getValue();
                        }
                        if (reportMeta.getChartData().get(chartIdRequest).getglobalEnable() != null && reportMeta.getChartData().get(chartIdRequest).getglobalEnable().equalsIgnoreCase("Y") && reportMeta.getFilterMap() != null) {

                            Set<Entry<String, List<String>>> filtersetGlobal = reportMeta.getFilterMap().entrySet();
                            for (Entry<String, List<String>> entry1 : filtersetGlobal) {
                                if (entry1.getKey().equalsIgnoreCase(entry.getKey())) {
                                    List<String> listValNew = new ArrayList<>();
                                    for (int m = 0; m < listVal.size(); m++) {
                                        for (int m1 = 0; m1 < entry1.getValue().size(); m1++) {
                                            if (entry1.getValue().get(m1).equalsIgnoreCase(listVal.get(m))) {
                                                listValNew.add(entry1.getValue().get(m1));
                                            }
                                        }
                                    }
                                    localfiltermapFromSet.put(entry1.getKey(), listValNew);
                                } else {
                                    localfiltermapFromSet.put(entry1.getKey(), entry1.getValue());
                                }

                            }
                        } else {
                            localfiltermapFromSet.put(entry.getKey(), listVal);
                        }

                        filtermapFromSetLocal = localfiltermapFromSet;

                        filtermapFromSet = filtermapFromSetLocal;
                    }
                }
            }

            HashMap<String, List> likeMap = new HashMap<String, List>();
            HashMap<String, List> notLikeMap = new HashMap<String, List>();
            HashMap<String, List> localdrillmap = new HashMap<String, List>();
            ReportObjectQuery objectQuery = new ReportObjectQuery();
            String action1 = request.getParameter("initializeFlag");
            if (action1 != null && action1.equalsIgnoreCase("true")) {
                if (reportMeta.getChartData().get(chartIdRequest).getLocalDrills() != null && !reportMeta.getChartData().get(chartIdRequest).getLocalDrills().isEmpty()) {
                    List<String> listVal = new ArrayList<String>();
                    Set<Entry<String, List<String>>> localDrills = reportMeta.getChartData().get(chartIdRequest).getLocalDrills().entrySet();
                    for (Entry<String, List<String>> entry1 : localDrills) {
                        listVal = entry1.getValue();
                        localdrillmap.put(entry1.getKey(), listVal);
                    }

                    filterClause = "";
                    filterClause = objectQuery.getFilterClause(localdrillmap, localfiltermapFromSet, likeMap, notLikeMap);
                } else {
                    filterClause = "";
                    filterClause = objectQuery.getFilterClause(drillmapFromSet, filtermapFromSet, likeMap, notLikeMap);
                }
            } else {

                filterClause = objectQuery.getFilterClause(drillmapFromSet, filtermapFromSet, likeMap, notLikeMap);
            }
        }
        if (reportMeta.getChartData().get(chartIdRequest).getCompleteChartData() == null) {
            reportMeta.getChartData().get(chartIdRequest).setCompleteChartData("N");
        }

//        String check_DDate;
//        check_DDate = "select DDATE from R_GO_" + reportId + " where 1=2";
//        conn = (Connection) ProgenConnection.getInstance().getConnectionForElement(dao.removeComparisonType(chartDetails.getMeassureIds().get(0)));
//        try {
//            retObj = pbdb.execSelectSQL(check_DDate, conn);
//
//        } catch (Exception e) {
//        }


        ProgenAOQuery aoQuery = new ProgenAOQuery();
        if (reportMeta.getDrillFormat() != null && reportMeta.getDrillFormat().equalsIgnoreCase("time")) {
            timeDetailsGO = new ArrayList<>();
            timeDetailsGO = dao.getTimeClauseForGoAgg(drillmapFromSet, drillmapFromSetAgg, timeDetailsGO, reportMeta, chartDetails);
            aoQuery.setTimeDetails(timeDetailsGO);
        } else {
            if (chartDetails.getTimeEnable() != null && chartDetails.getTimeEnable().equalsIgnoreCase("Yes")) {
                aoQuery.setIsChartTimeEnable("Yes");
                aoQuery.setChartTimeClause(chartDetails.getTimeBasedData().get(0));
            }
//                   else{
            // System.out.println("***********************************************timeDetails**************"+timeDetailsGO.toString()); 
            aoQuery.setTimeDetails(timeDetailsGO);
//                   }   
        }
        HashMap<String, List> mapSetForGraphs = new HashMap<>();
        String dontDrill1 = "N";
        if (chartDetails.getExcludeFromDrill() != null) {
            dontDrill1 = chartDetails.getExcludeFromDrill();
        }
        if (dontDrill1.equalsIgnoreCase("N")) {
            for (String keySet1 : drillmapFromSet.keySet()) {
                mapSetForGraphs.put(keySet1, drillmapFromSet.get(keySet1));
            }
        }
        for (String keySet1 : filtermapFromSet.keySet()) {
            if (filtermapFromSet.get(keySet1) != null) {
                mapSetForGraphs.put(keySet1, filtermapFromSet.get(keySet1));
            }
        }
        for (String keySet1 : localfiltermapFromSet.keySet()) {
            if (localfiltermapFromSet.get(keySet1) != null) {
                mapSetForGraphs.put(keySet1, localfiltermapFromSet.get(keySet1));
            }
        }

        if (chartDetails.getCompleteChartData() != null && chartDetails.getCompleteChartData().equalsIgnoreCase("Yes")) {
            aoQuery.setAvoidProgenTime("Yes");
        } else {
            aoQuery.setAvoidProgenTime("No");
        }
        aoQuery.setDrillFormat(reportMeta.getDrillFormat());
        List<String> grandTotalAggType = new ArrayList<>(chartDetails.getMeassureIds().size());
        grandTotalAggType = dao.addGrandTotalAggType(chartDetails);
        String queryGT = "";
        aoQuery.setAO_name(AO_Name);
//                aoQuery.setTimeDetails(container.getTimeDetailsArray());
        aoQuery.setGrandTotalAggType(grandTotalAggType);
        aoQuery.setViewIdList(null);
        aoQuery.setColViewIdList(null);
        aoQuery.setMeasureIdsList(chartDetails.getMeassureIds());
        aoQuery.setMeasureIdsListGO(reportMeta.getMeasuresIds());
        dao.setTimeTypeForMeasure(chartDetails, aoQuery);
        aoQuery.setFilterClause(filterClause);
        if (chartDetails.getglobalEnable() != null && chartDetails.getglobalEnable().equalsIgnoreCase("Y") && (reportMeta.getFilterMap() != null && !(reportMeta.getFilterMap().isEmpty()))) {
            aoQuery.setFilterMap(mapSetForGraphs);
        } else if (action != null && action.equalsIgnoreCase("localfilterGraphs")) {
            aoQuery.setFilterMap(mapSetForGraphs);
        } else {

            aoQuery.setFilterMap(mapSetForGraphs);
        }
//        aoQuery.setDimSecurityClause(dimSecurityClause);
        aoQuery.setAggregationType(chartDetails.getAggregation());
        aoQuery.setInnerViewByElementId("NONE");
        try {
            queryGT = aoQuery.generateAOQuery();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        PbReturnObject pbretObj = null;
        Connection connect = null;
        pbretObj = dao.getReturnObjectBasedOnChart(reportId, chartIdRequest, queryGT, fileLocation, userid);
        if (pbretObj == null) {
            connect = ProgenConnection.getInstance().getConnectionForElement(dao.removeComparisonType(chartDetails.getMeassureIds().get(0)));
            if (connect != null) {
                try {

                    pbretObj = pbdb.execSelectSQL(queryGT, connect);
                    dao.setReturnObjectBasedOnChart(reportId, chartIdRequest, queryGT, pbretObj, fileLocation, userid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
//            if(connect !=null){
//                connect.close();
//            }
        if (pbretObj != null && pbretObj.rowCount > 0) {
            if (chartDetails.getChartType().equalsIgnoreCase("KPI-Dashboard")) {
                Map KPIMap = new HashMap();
                list = dao.generateChartJson(pbretObj, chartDetails.getMeassures(), chartDetails.getMeassureIds(), 0, null, timeDetailsGO);
                KPIMap.put("data", list);
                KPIMap.put("comparedMeasure", chartDetails.getComparedMeasure());
                return gson.toJson(KPIMap);
            } else {
                for (int i = 0; i < pbretObj.rowCount; i++) {
                    for (int k = 0; k < chartDetails.getMeassureIds().size(); k++) {
                        result = Double.parseDouble(pbretObj.getFieldValueString(i, "A_" + chartDetails.getMeassureIds().get(k)));
                        strresult = BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                        resultSet.add(strresult);
                    }
                }

            }

        }

        return gson.toJson(resultSet);
    }

    public String getFilePath(HttpSession session) {
        String advHtmlFileProps = "";
        if (session != null) {
            InputStream servletStream = session.getServletContext().getResourceAsStream("/WEB-INF/classes/cache.ccf");
            Properties fileProps = new Properties();
            if (servletStream != null) {
                try {
                    fileProps.load(servletStream);
                    advHtmlFileProps = fileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath");
                } catch (IOException ex) {

                    logger.error("Exception:", ex);
                }
            }
        }
        return advHtmlFileProps;
    }

    public String getTemplateId(HttpServletRequest request) {
        PbDb pbdb = new PbDb();
        String query = getResourceBundle().getString("getTemplateId");
               String templateId;
               int value=1;
               PbReturnObject retObj = null;
        try {
            retObj = pbdb.execSelectSQL(query);
        } catch (SQLException ex) {
             logger.error("Exception:", ex);
            }
      if(retObj!=null && retObj.rowCount>0){
       templateId = retObj.getFieldValueString(0, 0);
      value = value + Integer.parseInt(templateId);
      templateId = String.valueOf(value);
      }else {
          templateId="1";
      }         
               return templateId;
    }

    public String getTemplateKPIData(HttpServletRequest request) {
     Type tarType = new TypeToken<List<String>>(){}.getType();
        HttpSession session = request.getSession(false);
        List<String> measureList = gson.fromJson(session.getAttribute("tempMeasures").toString(), tarType);
        Map<String,List<String>> measureNameMap = new HashMap<>();
        List<String> pageList = gson.fromJson(session.getAttribute("tempPages").toString(),tarType);
        Map<String,List<String>> measureIdMap = new HashMap<>();
        Map<String,List<String>> pageMap = new HashMap<>();
        Map<String,List<String>> pageIdMap = new HashMap<>();
//        String [] pageArray = pageList.split(":");
//        String key = measureList.get(0).split(":")[0];
        String[][] measureMatrix=new String[measureList.size()][measureList.get(0).split(":").length];
        String[][] pageMatrix=new String[pageList.size()][pageList.get(0).split(":").length];
        for(int i=0;i<measureList.size();i++){
            measureMatrix[i] = measureList.get(i).split(":");
        }
        for(int i=0;i<pageList.size();i++){
            pageMatrix[i] = pageList.get(i).split(":");
        }
        for(int i=0;i<measureMatrix.length;i++){
            String repId=measureMatrix[i][0];
            if(!measureNameMap.containsKey(repId)){
                List<String> measures= new ArrayList<>();
                List<String> measuresId= new ArrayList<>();
                for(int j=0;j<measureMatrix.length;j++){
                    if(measureMatrix[j][0].equals(repId)){
                        measures.add(measureMatrix[j][1]);
                        measuresId.add(measureMatrix[j][2]);
                    }
                }
                measureNameMap.put(repId,measures);
                measureIdMap.put(repId,measuresId);
            }
        }
        for(int i=0;i<pageMatrix.length;i++){
            String repId=pageMatrix[i][0];
            if(!pageMap.containsKey(repId)){
                List<String> pages= new ArrayList<>();
                List<String> pagesId= new ArrayList<>();
                for(int j=0;j<pageMatrix.length;j++){
                    if(pageMatrix[j][0].equals(repId)){
                        pages.add(pageMatrix[j][1]);
                        pagesId.add(pageMatrix[j][2]);
                    }
                }
                pageMap.put(repId.replace("m_", ""),pages);
                pageIdMap.put(repId.replace("m_", ""),pagesId);
//                measureIdMap.put(repId,measuresId);
            }
        }
        request.setAttribute("measureNameMap", measureNameMap);
        request.setAttribute("pageMap", pageMap);
        request.setAttribute("pageIdMap", pageIdMap);
        request.setAttribute("measureIdMap", measureIdMap);
        templateMeta = new TemplateInfoMeta();
      templateMeta=  setTemplateInfoForm(request);
        
        Map map = new HashMap();
        map.put("meta",gson.toJson(templateMeta));
    return gson.toJson(map);
    }

    public TemplateInfoMeta setTemplateInfoForm(HttpServletRequest request) {
        if(templateMeta ==null){
            templateMeta = new TemplateInfoMeta();
        }
        Type tarType = new TypeToken<Map<String,List<String>>>(){}.getType();
//            templateMeta=new TemplateInfoMeta();
            if(request.getParameter("templateName")!=null){
                templateMeta.setTemplateName(request.getParameter("templateName"));
            }
            if(request.getParameter("templateDesc")!=null){
                templateMeta.setTemplateDesc(request.getParameter("templateDesc"));
            }
            if(request.getParameter("templateId")!=null){
                templateMeta.setTemplateId(request.getParameter("templateId"));
            }
            if(request.getAttribute("measureNameMap")!=null){
//                Map<String,List<String>> map = gson.fromJson(request.getAttribute("measureNameMap").toString(), tarType);
                templateMeta.setMeasureNameMap((Map)request.getAttribute("measureNameMap"));
                
            }
            else if(request.getParameter("measureNameMap")!=null){
                Map<String,List<String>> map = gson.fromJson(request.getParameter("measureNameMap").toString(), tarType);
                templateMeta.setMeasureNameMap(map);
            }
            if(request.getAttribute("measureIdMap")!=null){
//                Map<String,List<String>> map = gson.fromJson(request.getAttribute("measureIdMap").toString(), tarType);
                templateMeta.setMeasureIdMap((Map)request.getAttribute("measureIdMap"));
            }
            else if(request.getParameter("measureIdMap")!=null){
                Map<String,List<String>> map = gson.fromJson(request.getParameter("measureIdMap").toString(), tarType);
                templateMeta.setMeasureIdMap(map);
            }
            if(request.getAttribute("pageMap")!=null){
                templateMeta.setPageMap((Map)request.getAttribute("pageMap"));
            }else if(request.getParameter("pageMap")!=null){
                Map<String,List<String>> map = gson.fromJson(request.getParameter("pageMap").toString(), tarType); 
                templateMeta.setPageMap(map);
            }
            if(request.getAttribute("pageIdMap")!=null){
                templateMeta.setPageIdMap((Map)request.getAttribute("pageIdMap"));
            }else if(request.getParameter("pageIdMap")!=null){
                Map<String,List<String>> map = gson.fromJson(request.getParameter("pageIdMap").toString(), tarType); 
                templateMeta.setPageIdMap(map);
            }
            return templateMeta;
            
    }

    public String getMeasureTotal(HttpServletRequest request) {
        Connection conn = null;
        Map<String, String> resultMap = new HashMap<String, String>();
        double result;
        String strresult = "";
        HttpSession session = request.getSession(false);
        List<String> ResultSet = new ArrayList<String>();
String aoAsGoId = null;
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//        String userColName = "";
        String filterClause = "";
        ArrayList<String> timeDetailsGO = new ArrayList<String>();

        PbReturnObject retObj = null;
        PbDb pbdb = new PbDb();
        String AO_Name = "";
       TemplateManagementDAO templateDao = new TemplateManagementDAO();

        templateMeta = new TemplateInfoMeta();
        templateMeta = setTemplateInfoForm(request);
        Map<String,List<String>> measureIdMap = templateMeta.getMeasureIdMap();
         Set<String> measureIdKey = measureIdMap.keySet();
        for(String measureKey : measureIdKey)
        {
        measureKey = measureKey.replace("m_", "");
           String aoNameQuery = getResourceBundle().getString("getAOName");
           Object[] obj = new Object[1];
           obj[0] = measureKey;
           aoNameQuery = pbdb.buildQuery(aoNameQuery, obj);
            try {
                retObj = pbdb.execSelectSQL(aoNameQuery);
            } catch (SQLException ex) {
               logger.error(ex);
            }
           if(retObj!=null && retObj.rowCount>0){
               aoAsGoId = retObj.getFieldValueString(0, "AO_ID");
           }
         if (aoAsGoId != null && !aoAsGoId.equalsIgnoreCase("")) {

            AO_Name = "M_AO_" + aoAsGoId;
        } else {

            AO_Name = "R_GO_" + measureKey;
        }
        List<String> measureIds = measureIdMap.get("m_"+measureKey);

     
        List<String> measLabel = new ArrayList<>();
        
        for (int i = 0;measureIds!=null && i < measureIds.size(); i++) {
            ArrayList<String> measureTimeDetails=new ArrayList<>();
//            Map<String, List<String>> timeDetails1 = templateDetails.getTimeDetails();
//            if(timeDetails1!=null){
//                    List<String> value=timeDetails1.get(removeComparisonType(templateDetails.getMeasureIds().get(i))+"_date");
//                    if(value!=null){
//                        for(String t:value){
//                            measureTimeDetails.add(t);
//                        }
//                    }
//                    else{
//                        measureTimeDetails=timeDetailsGO;                        
//                    }
//            }
//            else{
//                measureTimeDetails=timeDetailsGO;
//            }
            ProgenAOQuery aoQuery = new ProgenAOQuery();
            aoQuery.setTimeDetails(new ArrayList<String>());
            aoQuery.setAvoidProgenTime("No");
//        }
//            aoQuery.setDrillFormat(reportMeta.getDrillFormat());
            List<String> grandTotalAggType = new ArrayList<>(1);
            grandTotalAggType.add("N");
            String queryGT = "";
            aoQuery.setAO_name(AO_Name);
//                aoQuery.setTimeDetails(container.getTimeDetailsArray());
            aoQuery.setGrandTotalAggType(grandTotalAggType);
            aoQuery.setAvoidProgenTime("Yes");
            aoQuery.setViewIdList(null);
            aoQuery.setColViewIdList(null);
            List<String> measureId=new ArrayList<>();
            measureId.add(measureIds.get(i));
            aoQuery.setMeasureIdsList(measureId);
            aoQuery.setMeasureIdsListGO(measureId);
            setTimeTypeForMeasureTemplate(measureId, aoQuery);
            aoQuery.setFilterClause(filterClause);
            List<String> aggType=new ArrayList<String>();
//            aggType.add(templateDetails.getAggregation().get(i));
            
            aoQuery.setAggregationType(aggType);
            aoQuery.setInnerViewByElementId("NONE");
            try {
                queryGT = aoQuery.generateAOQuery();
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            PbReturnObject pbretObj = null;
            Connection connect = null;
// pbretObj=repManagementDao.getReturnObjectBasedOnChart(repId, id, queryGT,fileLocation,userid);
            if (pbretObj == null) {
                connect = ProgenConnection.getInstance().getConnectionForElement(templateDao.removeComparisonType(measureId.get(0)));
                if (connect != null) {
                    try {

                        pbretObj = pbdb.execSelectSQL(queryGT, connect);
//                repManagementDao.setReturnObjectBasedOnChart(repId, id, queryGT,pbretObj,fileLocation,userid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (pbretObj != null && pbretObj.rowCount > 0) {
                for (int j = 0; j < pbretObj.rowCount; j++) {
                    for (int k = 0; k < measureId.size(); k++) {
                        result = Double.parseDouble(pbretObj.getFieldValueString(j, "A_" + templateDao.removeComparisonType(measureId.get(k))));
                        strresult = BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                        ResultSet.add(strresult);
                    }
                }

//            }


            }
        }
        
        }//end of for loop idmap
        resultMap.put("meta", gson.toJson(templateMeta));
        resultMap.put("data", gson.toJson(ResultSet));
        return gson.toJson(resultMap);
    }
    
     private void setTimeTypeForMeasureTemplate(List<String> measureIdList, ProgenAOQuery objQuery) {
        ArrayList<String> measureType = new ArrayList<String>();
        for (int i = 0; i < measureIdList.size(); i++) {
            String measureId = measureIdList.get(i);
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
}
