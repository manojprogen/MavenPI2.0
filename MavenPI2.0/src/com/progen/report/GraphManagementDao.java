/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.contypes.GetConnectionType;
import com.progen.report.query.ProgenAOQuery;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.db.OverlayData;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.reportview.db.ProgenReportViewerDAO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.PbExcelGenerator;
import utils.db.ProgenConnection;

/**
 *
 * @author Mayank
 */
public class GraphManagementDao {
  public static Logger logger = Logger.getLogger(ReportManagementDAO.class);
//    Container container = new Container();
    XtendReportMeta reportMeta = new XtendReportMeta();
    FileReadWrite fileReadWrite = new FileReadWrite();
    HashMap filterLookupOriginalToNew = null;
    ReportManagementDAO repManDAO = new ReportManagementDAO();
    PbDb pbdb = new PbDb();
    
    
    public String getComboGraphData(HttpServletRequest request, String fileLocation) {
        String chartId = request.getParameter("chartId");
        String reportId = request.getParameter("graphsId");
        String actionGO = request.getParameter("actionGo");
        if (actionGO != null && actionGO.equalsIgnoreCase("paramChange")) {
            return "false";
        }
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        String filePath = fileLocation + "/" + "Combo_Data/" + userId + "/" + reportId + "/" + chartId + "/" + chartId + "First.json";
        String filePath1 = fileLocation + "/" + "Combo_Data/" + userId + "/" + reportId + "/" + chartId + "/" + chartId + "Second.json";
        FileReadWrite readWrite = new FileReadWrite();
        Gson gson = new Gson();
        File file = new File(filePath);
        File file1 = new File(filePath1);

        if (file.exists() && file1.exists()) {

            String data1 = readWrite.loadJSON(filePath);
            String data2 = readWrite.loadJSON(filePath1);
            Map map = new HashMap();
            map.put("data1", data1);
            map.put("data2", data2);
            return gson.toJson(map);
        }


        return "false";
    }
    
    public String getChartsData(HttpServletRequest request, String fileLocation) {
        Gson gson = new Gson();
        PbDb pbdb = new PbDb();
        reportMeta = new XtendReportMeta();
        String oneviewid = "";
        String regid = "";
        String dimSecurityClause = "";
        String reportId = request.getParameter("reportId");
        String chartFlag = request.getParameter("chartFlag");
        String aoAsGoId = request.getParameter("aoAsGoId");
        String divId = request.getParameter("chartID");
        String isoneview = (String) request.getAttribute("isoneview");
        HttpSession session = request.getSession(false);
        String userid = String.valueOf(session.getAttribute("USERID"));
        String xlsxfilename=reportId+divId+userid;
       
        setGraphForm(request);
        
        ReportObjectQuery objectQuery = new ReportObjectQuery();

        Container container = Container.getContainerFromSession(request, reportId);
        
        ArrayList<String> timeDetails = new ArrayList<String>();
        if (container != null) {
            timeDetails = container.getTimeDetailsArray();
            String connType = null;
            String st_date = timeDetails.get(2).toString();
            GetConnectionType gettypeofconn = new GetConnectionType();
            connType = gettypeofconn.getConTypeByElementId(reportMeta.getMeasuresIds().get(0));
            if (connType != null && connType.equalsIgnoreCase("SqlServer")) {
                st_date = "   convert(datetime,'" + st_date + "',120) ";

            } else if (connType != null && connType.equalsIgnoreCase("Oracle")) {
                st_date = "   to_date('" + st_date + "','mm/dd/yyyy hh24:mi:ss ') ";

            } else if (connType != null && connType.equalsIgnoreCase("Postgres")) {
                st_date = "   to_timestamp('" + st_date + "','mm/dd/yyyy hh24:mi:ss ') ";

            }
            String timeAggQuery = "";
            if (timeDetails != null) {
                if (timeDetails.get(3).toString().equalsIgnoreCase("QTR")) {
                    timeAggQuery = "select QTR_NAME AGG_NAME from pr_day_info where ddate=" + st_date + "";
                } else if (timeDetails.get(3).toString().equalsIgnoreCase("Month")) {
                    timeAggQuery = "select MONTH_NAME AGG_NAME from pr_day_info where ddate=" + st_date + "";
                } else if (timeDetails.get(3).toString().equalsIgnoreCase("Year")) {
                    timeAggQuery = "select YEAR_NAME AGG_NAME from pr_day_info where ddate=" + st_date + "";
                }
                PbReturnObject retObj2 = null;
                Connection conn = null;

                if (reportMeta.getViewbysIds().get(0).trim().equalsIgnoreCase("TIME")) {
                    conn = ProgenConnection.getInstance().getConnectionForElement(reportMeta.getMeasuresIds().get(0));
                } else {
                    conn = ProgenConnection.getInstance().getConnectionForElement(reportMeta.getViewbysIds().get(0));
                }
                if (conn != null) {
                    try {
                        retObj2 = pbdb.execSelectSQL(timeAggQuery, conn);
//                             timeDetails.remove(4);
                        timeDetails.add(retObj2.getFieldValueString(0, "AGG_NAME"));

                    } catch (Exception ex) {
                        logger.error("Exception:", ex);
                    }
                }

            }

            reportMeta.setTimedetails(timeDetails);
        }
        String timeClause = objectQuery.getCalenderTime(container, reportMeta.getMeasuresIds().get(0).toString());
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        HashMap<String, List> drillmapFromSet = new HashMap<String, List>();
        if (reportMeta.getDrills() != null) {
            map = reportMeta.getDrills();
            Set<Map.Entry<String, List<String>>> set = map.entrySet();


            for (Map.Entry<String, List<String>> entry : set) {
                drillmapFromSet.put(entry.getKey(), entry.getValue());
            }
        }
        Map<String, List<String>> filtermap = new HashMap<String, List<String>>();
        HashMap<String, List> filtermapFromSet = new HashMap<String, List>();
        if (reportMeta.getFilterMap() != null) {
            filtermap = reportMeta.getFilterMap();
            Set<Map.Entry<String, List<String>>> filterset = filtermap.entrySet();
            for (Map.Entry<String, List<String>> entry : filterset) {
                filtermapFromSet.put(entry.getKey(), entry.getValue());
            }
        }


//         HashMap<String, List> inMap  = new HashMap<String, List>();
//                            HashMap<String, List> notInMap = new HashMap<String, List>();//
        HashMap<String, List> likeMap = new HashMap<String, List>();
        HashMap<String, List> notLikeMap = new HashMap<String, List>();
        String filterClause = objectQuery.getFilterClause(drillmapFromSet, filtermapFromSet, likeMap, notLikeMap);
        Map<String, String> dataMapgblsave = new HashMap<String, String>();//sandeep
        if (request.getSession(false).getAttribute("dataMapgblsave") != null) {
            dataMapgblsave = (Map<String, String>) request.getSession(false).getAttribute("dataMapgblsave");
        }
        Map<String, List<Map<String, String>>> dataMap = new HashMap<String, List<Map<String, String>>>();
        Map cutOfmap = new HashMap();
        Map cutOfmap1 = new HashMap();

        Map<String, DashboardChartMeta> chartData = new LinkedHashMap<String, DashboardChartMeta>(reportMeta.getChartMeta());
        List<String> charts = new ArrayList(chartData.keySet());

        for (String chart : charts) {
            //
            if (chartFlag != null && !chartFlag.equalsIgnoreCase("") && divId != null && !divId.equalsIgnoreCase("")) {

                chart = divId;
            }
            if (isoneview != null && isoneview.equalsIgnoreCase("true")) {
                String chartname = (String) request.getAttribute("chartname");
                chart = chartname;
                oneviewid = (String) request.getAttribute("oneviewid");
                regid = (String) request.getAttribute("regid");
                reportId = (String) request.getAttribute("reportId");
            }
            DashboardChartMeta chartDetails = reportMeta.getChartMeta().get(chart);
            ArrayList<String> colViewIds = new ArrayList<String>();
            ArrayList<String> rowIds = new ArrayList<String>();
            for (int j = 0; j < chartDetails.getViewIds().size(); j++) {
                if (reportMeta.getVisualChartType() != null && reportMeta.getChartType() != null && reportMeta.getChartType().equalsIgnoreCase("tree-map")) {
                    rowIds.add(chartDetails.getViewIds().get(j));
                    if (j == 1) {
                        break;
                    }
                } else {
                    rowIds.add(chartDetails.getViewIds().get(j));
                }
            }

            String measureclause = "";
            int filterCount = 0;
            //for measure filters
            if (reportMeta.getChartMeta().get(chart).getMeasureFilters() != null) {
                Map<String, Map<String, String>> measureMap = reportMeta.getChartMeta().get(chart).getMeasureFilters();
                Set<String> measuresFilter = measureMap.keySet();
                for (String measureVal : measuresFilter) {
                    try {
                        String queryForMeasure = "select user_col_name,AGGREGATION_TYPE from prg_user_all_info_details where element_id='" + measureVal + "'";
                        PbReturnObject measureObj = pbdb.execSelectSQL(queryForMeasure);
                        Set<String> measuresFiltersMap = measureMap.get(measureVal).keySet();
                        for (String measureSymbol : measuresFiltersMap) {
                            if (filterCount == 0) {
                                measureclause += " having ";
                            } else {
                                measureclause += " and ";
                            }
                            filterCount++;
                            if (measureSymbol.equalsIgnoreCase("<>")) {
                                String vals =
                                        measureclause += "SUM(" + measureObj.getFieldValueString(0, 0) + ") between  " + measureMap.get(measureVal).get(measureSymbol).split("__")[0] + " and " + measureMap.get(measureVal).get(measureSymbol).split("__")[1];
                            } else {
                                measureclause += "SUM(" + measureObj.getFieldValueString(0, 0) + ") " + measureSymbol + "  " + measureMap.get(measureVal).get(measureSymbol) + " ";
                            }
//                           measureclause += measureObj.getFieldValueString(0, 1)+"("+measureObj.getFieldValueString(0, 0)+") "+measureSymbol+"  "+measureMap.get(measureVal).get(measureSymbol)+" ";
                        }
                    } //     filterClause += " and A_"+reportMeta.getChartMeta().get(chart).getMeassureIds().get(0)+" > "+reportMeta.getChartMeta().get(chart).getMeasureProperties().get("A_"+reportMeta.getChartMeta().get(chart).getMeassureIds().get(0))+" ";
                    catch (SQLException ex) {
                        logger.error("Exception:", ex);
                    }
                }
            }
              List<String> grandTotalAggType = new ArrayList<String>(chartDetails.getMeassureIds().size());
      for(int i=0;i<chartDetails.getMeassureIds().size();i++){
          String measId = chartDetails.getMeassureIds().get(i);
          
          if(measId.contains("PT")){
              grandTotalAggType.add(i, "PT");
}
          else if(measId.contains("GT")){
             grandTotalAggType.add(i, "GT"); 
          }else{
              grandTotalAggType.add(i, "N"); 
          }
      }
            ProgenAOQuery objQuery = new ProgenAOQuery();
            String query = "";
            if (aoAsGoId != null && !aoAsGoId.equalsIgnoreCase("")) {
                objQuery.setAO_name("M_AO_" + aoAsGoId);
            } else {
                objQuery.setAO_name("R_GO_" + reportId);
            }
            if (userid != null && !userid.equalsIgnoreCase("")) {
            dimSecurityClause = objectQuery.getDimSecClause(reportMeta.getViewbysIds(), userid,filtermapFromSet);
        }
            objQuery.setGrandTotalAggType(grandTotalAggType);
            objQuery.setTimeDetails(timeDetails);
            objQuery.setViewIdList(rowIds);
            objQuery.setColViewIdList(colViewIds);
            objQuery.setMeasureIdsList(chartDetails.getMeassureIds());
            objQuery.setMeasureIdsListGO(reportMeta.getMeasuresIds());
            setTimeTypeForMeasure(chartDetails, objQuery);
            objQuery.setFilterClause(filterClause);
            objQuery.setFilterMap(filtermapFromSet);
            objQuery.setAggregationType(chartDetails.getAggregation());
            objQuery.setInnerViewByElementId("NONE");
//                objQuery.setTimeDetailsArray(measureType);
            try {
                query = objQuery.generateAOQuery();
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
//            String query = objectQuery.getObjectQueryGO(reportId,filterClause,rowIds,colViewIds,chartDetails.getMeassureIds().get(0),measureclause,isoneview,oneviewid,regid,fileLocation,timeClause,dimSecurityClause);
//          String query = "select A_"+chartDetails.getViewBys().get(0)+",B_"+chartDetails.getMeassures().get(0)+" from R_AO_"+reportId+"  ";

            PbReturnObject pbretObj = null;
            Connection conn = null;

            if (chartDetails.getViewIds().get(0).trim().equalsIgnoreCase("TIME")) {
                conn = ProgenConnection.getInstance().getConnectionForElement(chartDetails.getMeassureIds().get(0));
            } else {
                conn = ProgenConnection.getInstance().getConnectionForElement(chartDetails.getViewIds().get(0));
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
                   
                 writeGraphDatainXLSX(pbretObj);
                 writeGraphDatainCSV(pbretObj);
                    //end of code by anitha
                    List<String> colNames = new ArrayList();
                    List<String> colNames1 = new ArrayList();
                    List<String> colIds = new ArrayList();
                    List<String> colIds1 = new ArrayList();
                    List<String> meaIds = new ArrayList();
                    List<String> meaName1 = new ArrayList();
                    colIds.add(chartDetails.getViewIds().get(0));
                    colNames.add(chartDetails.getViewBys().get(0));
                    if ((chartDetails.getChartType().equalsIgnoreCase("multi-view-bubble") || chartDetails.getChartType().equalsIgnoreCase("Multi-View-Tree") || chartDetails.getChartType().equalsIgnoreCase("grouped-bar") || chartDetails.getChartType().equalsIgnoreCase("GroupedHorizontal-Bar") || chartDetails.getChartType().equalsIgnoreCase("Scatter-Bubble") || chartDetails.getChartType().equalsIgnoreCase("Split-Bubble") || chartDetails.getChartType().equalsIgnoreCase("world-map") || chartDetails.getChartType().equalsIgnoreCase("Trend-Combo") || chartDetails.getChartType().equalsIgnoreCase("grouped-table") || chartDetails.getChartType().equalsIgnoreCase("grouped-line") || chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar") || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar") || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-BarLine") || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar%") || chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar%") || chartDetails.getChartType().equalsIgnoreCase("Grouped-MultiMeasureBar") || (reportMeta.getChartType() != null && (reportMeta.getChartType().equalsIgnoreCase("Multi-View-Bubble") || reportMeta.getChartType().equalsIgnoreCase("Split-Graph") || reportMeta.getChartType().equalsIgnoreCase("India-Map-Dashboard") || reportMeta.getChartType().equalsIgnoreCase("world-map-animation")))) && chartDetails.getViewIds().size() > 1) {
                        colIds.add(chartDetails.getViewIds().get(1));
                        colNames.add(chartDetails.getViewBys().get(1));
                    }
                    if (chart.equalsIgnoreCase("chart1") && reportMeta.getVisualChartType() != null && request.getParameter("currType") != null && reportMeta.getVisualChartType().get(request.getParameter("currType")).equalsIgnoreCase("Overlay")) {
                        colIds.add(chartDetails.getViewIds().get(1));
                        colNames.add(chartDetails.getViewBys().get(1));
                    }

                    for (int m = 0; m < chartDetails.getViewBys().size(); m++) {
                        if (reportMeta.getVisualChartType() != null && reportMeta.getChartType() != null && reportMeta.getChartType().equalsIgnoreCase("tree-map")) {
                            colIds1.add(chartDetails.getViewIds().get(m));
                            colNames1.add(chartDetails.getViewBys().get(m));
                            if (m == 1) {
                                break;
                            }
                        } else {
                            colIds1.add(chartDetails.getViewIds().get(m));
                            colNames1.add(chartDetails.getViewBys().get(m));
                        }
                    }
                    for (int m = 0; m < chartDetails.getMeassureIds().size(); m++) {
                        colIds.add(chartDetails.getMeassureIds().get(m));
                        colNames.add(chartDetails.getMeassures().get(m));
                        meaIds.add(chartDetails.getMeassureIds().get(m));
                        meaName1.add(chartDetails.getMeassures().get(m));
                    }
//                    if (reportMeta.getIsHirarchichal()) {
//                    colIds.add(chartDetails.getViewIds().get(0));
//                    }

                    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                 writeGraphDatainXLSX1(pbretObj,colNames,xlsxfilename);

                    if (chart.equalsIgnoreCase("chart1") && reportMeta.getVisualChartType() != null && request.getParameter("currType") != null && reportMeta.getVisualChartType().get(request.getParameter("currType")).equalsIgnoreCase("Overlay")) {
                        cutOfmap1 = repManDAO.processOverLayJson(repManDAO.generateChartJsonForOverLay(pbretObj, chartDetails.getViewIds(), chartDetails.getMeassureIds(), reportMeta.getChartMeta().get(chart).getViewIds().size()));
                    } else {
                        list = repManDAO.generateChartJson(pbretObj, colNames, colIds, reportMeta.getChartMeta().get(chart).getViewIds().size(), session, timeDetails);
                    }
//                    List<Map<String, String>> list = generateChartJson(pbretObj,colNames,colIds);
                    if (reportMeta.getChartType() != null && (reportMeta.getChartType().equalsIgnoreCase("Fish-Eye") || reportMeta.getChartType().equalsIgnoreCase("tree-map") || reportMeta.getChartType().equalsIgnoreCase("CoffeeWheel") || reportMeta.getChartType().equalsIgnoreCase("Tree-map-single"))) {
                        List<Map<String, List<Double>>> listData = repManDAO.generateChartJsonHie(pbretObj, colNames, colIds, colIds1, colNames1, meaIds, meaName1);
                        Map<String, List<String>> combineList = repManDAO.combineChunks(listData, meaName1);
                        Map mapData = new HashMap();
                        if (reportMeta.getChartType().equalsIgnoreCase("Tree-map-single")) {
                            mapData = combineList;
                        } else {
                            mapData = repManDAO.processHirarchiJson(combineList);
                        }
                        container.setTreeData(mapData);
                        return gson.toJson(mapData);
                    }
                    if (chartDetails.getViewBys().get(0).toLowerCase().contains("week") || chartDetails.getViewBys().get(0).equalsIgnoreCase("Year") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("month") || (chartDetails.getViewBys().get(0).toString().trim().toLowerCase().contains("time") && !chartDetails.getViewBys().get(0).toString().replaceAll("\\W", "").trim().toLowerCase().equalsIgnoreCase("timeslot"))) {
                        DateComperator dateComperator = new DateComperator(chartDetails.getViewBys().get(0).toString(), "asc", timeDetails);
                        Collections.sort(list, dateComperator);
                    } else {

                        if (chart.equalsIgnoreCase("chart1") && reportMeta.getVisualChartType() != null && request.getParameter("currType") != null && reportMeta.getVisualChartType().get(request.getParameter("currType")).equalsIgnoreCase("Overlay")) {
                        } else {
                            JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(0), "desc", "");
                            Collections.sort(list, jsonComperator);
                        }
                    }
                    List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
                    if (reportMeta.getVisualChartType() != null && reportMeta.getChartType() != null && (reportMeta.getChartType().equalsIgnoreCase("bubble-dashboard") || reportMeta.getChartType().equalsIgnoreCase("multi-view-bubble") || reportMeta.getChartType().equalsIgnoreCase("Pie-Dashboard"))) {
                        for (int f = 0; f < (list.size() < 200 ? list.size() : 200); f++) {
                            dataList.add(list.get(f));
                        }
                    }
                    if (reportMeta.getVisualChartType() != null && reportMeta.getVisualChartType() != null && request.getParameter("currType") != null && reportMeta.getVisualChartType().get(request.getParameter("currType")).equalsIgnoreCase("Overlay")) {
                        if (chart.equalsIgnoreCase("chart1")) {
                            cutOfmap.put(chart, cutOfmap1);
                        } else {
                            cutOfmap.put(chart, list);
                        }
                    } else {
                        if (reportMeta.getChartType() != null && (reportMeta.getChartType().equalsIgnoreCase("bubble-dashboard") || reportMeta.getChartType().equalsIgnoreCase("multi-view-bubble")) || reportMeta.getChartType().equalsIgnoreCase("Pie-Dashboard")) {
                            dataMap.put(chart, dataList);
                            dataMapgblsave.put(regid, gson.toJson(dataMap));
                        } else {
                            if (chartDetails.getRecords() != null && !chartDetails.getRecords().toString().equalsIgnoreCase("") && !chartDetails.getChartType().equalsIgnoreCase("grouped-bar") && !chartDetails.getChartType().equalsIgnoreCase("GroupedHorizontal-Bar") && !chartDetails.getChartType().equalsIgnoreCase("Scatter-Bubble") && !chartDetails.getChartType().equalsIgnoreCase("Split-Bubble") && !chartDetails.getChartType().equalsIgnoreCase("world-map") && chartDetails.getChartType().equalsIgnoreCase("Trend-Combo") && !chartDetails.getChartType().equalsIgnoreCase("Grouped-Table") && !chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar") && !chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar") && !chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar%") && !chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar%") && !chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-BarLine") && !chartDetails.getChartType().equalsIgnoreCase("grouped-line") && !chartDetails.getChartType().equalsIgnoreCase("multi-view-bubble") && !chartDetails.getChartType().equalsIgnoreCase("Multi-View-Tree") && !chartDetails.getChartType().equalsIgnoreCase("Grouped-MultiMeasureBar")) {
                                for (int f = 0; f < (list.size() < Integer.parseInt(chartDetails.getRecords()) ? list.size() : Integer.parseInt(chartDetails.getRecords())); f++) {
                                    dataList.add(list.get(f));
                                }
                            } else {
                                dataList = list;
                            }
                            dataMap.put(chart, dataList);
                            dataMapgblsave.put(regid, gson.toJson(dataMap));
                        }
                    }
                } catch (Exception ex) {
                    logger.error("Exception:", ex);
                }
            }
            if (isoneview != null && isoneview.equalsIgnoreCase("true")) {
                request.getSession(false).setAttribute("dataMapgblsave", dataMapgblsave);
                break;
            }
            if (chartFlag != null && !chartFlag.equalsIgnoreCase("") && divId != null && !divId.equalsIgnoreCase("")) {
                break;
            }
        }



        if (reportMeta.getVisualChartType() != null && reportMeta.getVisualChartType().get(request.getParameter("currType")) != null && reportMeta.getVisualChartType().get(request.getParameter("currType")).equalsIgnoreCase("Overlay")) {
            Type dataType = new TypeToken<OverlayData>() {
            }.getType();
            OverlayData cutData = gson.fromJson(gson.toJson(cutOfmap), dataType);
            container.setOverlayData(cutData);
            return gson.toJson(cutOfmap);
        } else {
            //g
            Map<String, List<Map<String, String>>> newDataMap = new HashMap<String, List<Map<String, String>>>();
            if (chartFlag != null && !chartFlag.equalsIgnoreCase("") && divId != null && !divId.equalsIgnoreCase("")) {
                newDataMap = container.getDbrdData();
                newDataMap.put(divId, dataMap.get(divId));
                container.setDbrdData(newDataMap);
            } else {
                newDataMap = dataMap;
                container.setDbrdData(dataMap);
            }
            return gson.toJson(newDataMap);
        }
    }
    
    
     public XtendReportMeta setGraphForm(HttpServletRequest request) {
        Gson gson = new Gson();
        XtendReportMeta mapTest;
        Type tarType = new TypeToken<List<String>>() {
        }.getType();
        List<String> list;
        if (request.getParameter("reportName") != null) {
            reportMeta.setReportName(request.getParameter("reportName"));
        }
        if (request.getParameter("viewby") != null) {
            list = gson.fromJson(request.getParameter("viewby"), tarType);
            reportMeta.setViewbys(list);
        }
        if (request.getParameter("viewbyIds") != null) {
            list = gson.fromJson(request.getParameter("viewbyIds"), tarType);
            reportMeta.setViewbysIds(list);
        }
        if (request.getParameter("draggableViewBys") != null) {
            list = gson.fromJson(request.getParameter("draggableViewBys"), tarType);
            reportMeta.setDraggableViewBys(list);
        }

        if (request.getParameter("measure") != null) {
            list = gson.fromJson(request.getParameter("measure"), tarType);
            reportMeta.setMeasures(list);
        }
        if (request.getParameter("measureIds") != null) {
            list = gson.fromJson(request.getParameter("measureIds"), tarType);
            reportMeta.setMeasuresIds(list);
        }

        if (request.getParameter("aggregation") != null) {
            list = gson.fromJson(request.getParameter("aggregation"), tarType);
            reportMeta.setAggregations(list);
        }

        if (request.getParameter("groupbys") != null) {
            list = gson.fromJson(request.getParameter("groupbys"), tarType);
            reportMeta.setDimensions(list);
        }
        if (request.getParameter("nameMap") != null) {
            list = gson.fromJson(request.getParameter("nameMap"), tarType);
            reportMeta.setNameMap(list);
        }

        if (request.getParameter("filters1") != null) {
            boolean flag = false;
            if ((request.getParameter("isNewReport") != null && request.getParameter("isNewReport").equalsIgnoreCase("true"))) {
                flag = true;
            } else {
                flag = false;
            }
            if (!flag) {
                Type tarType1 = new TypeToken<Map<String, List<String>>>() {
                }.getType();
                Map<String, List<String>> map = gson.fromJson(request.getParameter("filters1"), tarType1);
                reportMeta.setFilterMap(map);
            }
        }
        if (request.getParameter("filtersmapgraph") != null) {

            Type tarType1 = new TypeToken<Map<String, List<String>>>() {
            }.getType();
            Map<String, List<String>> map = gson.fromJson(request.getParameter("filtersmapgraph"), tarType1);
            reportMeta.setFilterMapgraph(map);

        }

        if (request.getParameter("drills") != null) {
            boolean flag = false;
            if ((request.getParameter("isNewReport") != null && request.getParameter("isNewReport").equalsIgnoreCase("true"))) {
                flag = true;
            } else {
                flag = false;
            }
            if (!flag) {
                Type tarType1 = new TypeToken<Map<String, List<String>>>() {
                }.getType();
                Map<String, List<String>> map = gson.fromJson(request.getParameter("drills"), tarType1);
                reportMeta.setDrills(map);
            }

        } else {
            reportMeta.setDrills(null);
        }

        if (request.getParameter("driverList") != null && reportMeta.getDrills() != null) {
            list = gson.fromJson(request.getParameter("driverList"), tarType);
            reportMeta.setDriverList(list);
        } else {
            reportMeta.setDriverList(null);
        }

//        if (request.getParameter("filterType") != null) {
//            reportMeta.setFilterType(request.getParameter("filterType"));
//        }

        if (request.getParameter("dataPath") != null) {
            reportMeta.setDataPath(request.getParameter("dataPath"));
        }

        if ((request.getParameter("isMap") != null && !request.getParameter("isMap").equalsIgnoreCase("no")) || (reportMeta != null && reportMeta.getIsMap() && !request.getParameter("isMap").equalsIgnoreCase("no"))) {
            reportMeta.setIsMap(true);
        } else {
            reportMeta.setIsMap(false);
        }

//        if (request.getParameter("tarPath") != null) {
//            reportMeta.setTarPath(request.getParameter("tarPath"));
//        }

        if (request.getParameter("onDimSort") != null) {
            reportMeta.setOnDimSort(request.getParameter("onDimSort"));
        }

        if ((request.getParameter("isHirarchichal") != null && request.getParameter("isHirarchichal").equalsIgnoreCase("true")) || (reportMeta != null && reportMeta.getIsHirarchichal() && (request.getParameter("isHirarchichal") == null || !request.getParameter("isHirarchichal").equals("false")))) {
            reportMeta.setIsHirarchichal(true);
        } else {
            reportMeta.setIsHirarchichal(false);
        }

        if (request.getParameter("chartType") != null) {
            reportMeta.setChartType(request.getParameter("chartType"));
        }

        if (request.getParameter("dashBoardType") != null) {
            reportMeta.setDashboardType(request.getParameter("dashBoardType"));
        }

        if (request.getParameter("drillStatus") != null) {
            reportMeta.setDrillStatus(request.getParameter("drillStatus"));
        }

        if (request.getParameter("Others") != null) {
            reportMeta.setOthers(request.getParameter("Others"));
        }

        if (request.getParameter("drilltype") != null) {
            reportMeta.setDrillType(request.getParameter("drilltype"));
        }
        if (request.getParameter("drillFormat") != null) {
            reportMeta.setDrillFormat(request.getParameter("drillFormat"));
        }

        if (request.getParameter("ismeasureBubble") != null) {
            reportMeta.setIsmeasureBubble(request.getParameter("ismeasureBubble"));
        }
        try {
            if ((request.getParameter("isDashboard") != null && request.getParameter("isDashboard").equals("true")) || ((reportMeta != null && reportMeta.getIsDashboard()) && (request.getParameter("isDashboard") == null || !request.getParameter("isDashboard").equals("false")))) {
                reportMeta.setIsDashboard(true);
            } else {
                reportMeta.setIsDashboard(false);
            }
        } catch (NullPointerException npe) {
            if (reportMeta != null && reportMeta.getIsDashboard()) {
                reportMeta.setIsDashboard(true);
            } else {
                reportMeta.setIsDashboard(false);
            }
        }

        if ((request.getParameter("isTable") != null && request.getParameter("isTable").equals("true")) || (reportMeta != null && reportMeta.getIsTable())) {
            reportMeta.setIsTable(true);
        } else {
            reportMeta.setIsTable(false);
        }

//        if (request.getParameter("tableAttribute") != null) {
//            reportMeta.setTableAttribute(request.getParameter("tableAttribute"));
//        }

        if (request.getParameter("crossTab") != null) {
            reportMeta.setCrossTab(request.getParameter("crossTab"));
        }

        if (request.getParameter("drillIn") != null) {
            reportMeta.setDrillIn(request.getParameter("drillIn"));
        }

//        if (request.getParameter("mapBy") != null) {
//            reportMeta.setMapBy(request.getParameter("mapBy"));
//        }

        if (request.getParameter("legends") != null) {
            reportMeta.setLegends(request.getParameter("legends"));
        }

        if (request.getParameter("drillIn") != null) {
            reportMeta.setDrillIn(request.getParameter("drillIn"));
        }

//        if (request.getParameter("orderMeasure") != null) {
//            list = gson.fromJson(request.getParameter("orderMeasure"), tarType);
//            reportMeta.setOrderMeasure(list);
//        }
//
//        if (request.getParameter("orderViewBy") != null) {
//            list = gson.fromJson(request.getParameter("orderViewBy"), tarType);
//            reportMeta.setOrderMeasure(list);
//        }
        if (request.getParameter("filterParams") != null) {
            list = gson.fromJson(request.getParameter("filterParams"), tarType);
            reportMeta.setFilterParams(list);
        }

        if ((request.getParameter("isOverlay") != null && request.getParameter("isOverlay").equalsIgnoreCase("yes")) || (reportMeta != null && reportMeta.getIsTable())) {
            reportMeta.setIsOverLay(true);
        } else {
            reportMeta.setIsOverLay(false);
        }

        if (request.getParameter("chartList") != null) {
            list = gson.fromJson(request.getParameter("chartList"), tarType);
            reportMeta.setChartList(list);
        }
        if (request.getParameter("currType") != null) {
            reportMeta.setCurrType(request.getParameter("currType"));
        }

        if (request.getParameter("chartMeta") != null) {
            Type tarType1 = new TypeToken<Map<String, DashboardChartMeta>>() {
            }.getType();
            Map<String, DashboardChartMeta> chartData = gson.fromJson(request.getParameter("chartMeta"), tarType1);
//            Map<String, DashboardChartData> chartData = gson.fromJson("", tarType1); //Comment
            reportMeta.setChartMeta(chartData);
        }
        if (request.getParameter("measureColor") != null) {
            Type tarType1 = new TypeToken<Map<String, String>>() {
            }.getType();
            Map map = gson.fromJson(request.getParameter("measureColor"), tarType1);
//            Map<String, DashboardChartData> chartData = gson.fromJson("", tarType1); //Comment
            reportMeta.setMeasureColor(map);
        }
        if (request.getParameter("numberFormatMap") != null) {
            Type tarType1 = new TypeToken<Map<String, String>>() {
            }.getType();
            Map map = gson.fromJson(request.getParameter("numberFormatMap"), tarType1);
//            Map<String, DashboardChartData> chartData = gson.fromJson("", tarType1); //Comment
            reportMeta.setNumberFormatMap(map);
        }
        if (request.getParameter("visualChartType") != null) {
            Type tarType1 = new TypeToken<Map<String, String>>() {
            }.getType();

            Map map = gson.fromJson(request.getParameter("visualChartType"), tarType1);
            //        HashMap<String,String> MapData = (HashMap<String, String>) map;

//                Set keySet = map.keySet();
//                            Iterator itr = keySet.iterator();
//                            String key;
//                            while(itr.hasNext()){
//                                key = itr.next().toString();
//                          String       map.get(key);
//                                collect.operatorFilters.get("IN").put(key,inMap.get(key));

//                            }
//                list   = gson.fromJson(request.getParameter("visualChartType"), tarType);
            reportMeta.setVisualChartType(map);
        }
//        if ((request.getParameter("isMultipleCsv") != null && "true".equals(request.getParameter("isMultipleCsv"))) || ((reportMeta != null && reportMeta.getIsMultipleCsv()) && (request.getParameter("isMultipleCsv") == null || !request.getParameter("isMultipleCsv").equals("false")))) {
//            reportMeta.setIsMultipleCsv(true);
//            Type tarType1 = new TypeToken<Map<String, String>>() {
//            }.getType();
//            if (request.getParameter("commonColumns") != null) {
//                Map<String, String> map = gson.fromJson(request.getParameter("commonColumns"), tarType1);
//                reportMeta.setCommonColList(map);
//            }
//        } else {
//            reportMeta.setIsMultipleCsv(false);
//        }

//        if ((request.getParameter("isOverAllWordCloud") != null && request.getParameter("isOverAllWordCloud").equals("true")) || (reportMeta != null && reportMeta.getIsOverAllWordCloud() && (request.getParameter("isOverAllWordCloud") == null || !request.getParameter("isOverAllWordCloud").equals("false")))) {
//            reportMeta.setIsOverAllWordCloud(true);
//        } else {
//            reportMeta.setIsOverAllWordCloud(false);
//        }

//        if (request.getParameter("colorAttributMap") != null && !request.getParameter("colorAttributMap").equals("")) {
//            Type type = new TypeToken<Map<String, String>>() {
//            }.getType();
//            Map<String, String> map = gson.fromJson(request.getParameter("colorAttributMap"), type);
//            reportMeta.setColorMap(map);
//        }
//
//        if (request.getParameter("isSection") != null) {
//            reportMeta.setIsSection(request.getParameter("isSection"));
//        } else {
//            reportMeta.setIsSection("");
//        }

        if (reportMeta.getChartList() != null && !reportMeta.getChartList().isEmpty() && reportMeta.getChartList().get(0).equalsIgnoreCase("Multi-KPI")) {
            reportMeta.setIsMultiKpi(true);
        } else {
            reportMeta.setIsMultiKpi(false);
        }

        if (request.getParameter("shadeType") != null) {
            reportMeta.setShadeType(request.getParameter("shadeType"));
        }
        if (request.getParameter("currencyType") != null) {
            reportMeta.setShadeType(request.getParameter("currencyType"));
        }
        if (request.getParameter("isShaded") != null) {
            reportMeta.setIsShaded(request.getParameter("isShaded"));
        }

        if (request.getParameter("conditionalMap") != null) {
            Type tarType1 = new TypeToken<Map<String, Map<String, String>>>() {
            }.getType();
            Map map = gson.fromJson(request.getParameter("conditionalMap"), tarType1);
            reportMeta.setConditionalMap(map);
        }
        if (request.getParameter("conditionalMeasure") != null) {
            reportMeta.setConditionalMeasure(request.getParameter("conditionalMeasure"));
        }

        if (request.getParameter("timeMap") != null) {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> map = gson.fromJson(request.getParameter("timeMap"), type);
            reportMeta.setTimeMap(map);
        }
        //added by shobhit for multi dashboard on 22/19/15
        if (request.getParameter("parentViewBy") != null) {
            reportMeta.setParentViewBy(request.getParameter("parentViewBy"));
        }

        if (request.getParameter("childViewBys") != null) {
            list = gson.fromJson(request.getParameter("childViewBys"), tarType);
            reportMeta.setChildViewBys(list);
        }

        if (request.getParameter("childMeasBys") != null) {
            list = gson.fromJson(request.getParameter("childMeasBys"), tarType);
            reportMeta.setChildMeasBys(list);
        }

        if (request.getParameter("selectedViewBys") != null) {
            list = gson.fromJson(request.getParameter("selectedViewBys"), tarType);
            reportMeta.setSelectedViewBys(list);
        }

        if (request.getParameter("selectedMeasBys") != null) {
            list = gson.fromJson(request.getParameter("selectedMeasBys"), tarType);
            reportMeta.setSelectedMeasBys(list);
        }
        if (request.getParameter("timeDetailsArray") != null) {
            list = gson.fromJson(request.getParameter("timeDetailsArray"), tarType);
            reportMeta.setTimedetails(list);
        }
        //end
        return reportMeta;
    }
     
     public void setTimeTypeForMeasure(DashboardChartMeta chartDetails, ProgenAOQuery objQuery) {
        ArrayList<String> measureType = new ArrayList<String>();
        for (int i = 0; i < chartDetails.getMeassureIds().size(); i++) {
            String measureId = chartDetails.getMeassureIds().get(i);
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
      //for graph data dowload in XLSX
    public String writeGraphDatainXLSX(PbReturnObject retobj){
    int rowCount=retobj.getRowCount();
    int columnCountt=retobj.getColumnCount();
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet1 = wb.createSheet("Sheet1");
         for(int i=0;i<rowCount;i++){
          Row rowData = sheet1.createRow(i);
          for(int j=0;j<columnCountt;j++){
              Cell rowCell = rowData.createCell(j);
              if(j==0){
                rowCell.setCellValue(retobj.getFieldValueString(i, j));  
              }else{

              rowCell.setCellValue(String.valueOf(Double.valueOf(retobj.getFieldValueString(i, j)).longValue()));
              }
          }
         }
        try {
            FileOutputStream fileOut = new FileOutputStream("/home/progen/Downloads/Exlfolder/MB_sample.xlsx");
            wb.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return null;
    }
    
      //for graph data dowload in XLSX by Shivam
    public String writeGraphDatainXLSX1(PbReturnObject retobj,List<String> colnames,String xlsxfilename){
    int rowCount=retobj.getRowCount();
    int columnCountt=retobj.getColumnCount();
    
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFFont font = wb.createFont();
      font.setFontHeightInPoints((short) 8);
      font.setFontName("IMPACT");
      font.setColor(HSSFColor.BLACK.index);
      font.setBold(true);
       XSSFColor grey =new XSSFColor(new java.awt.Color(192,192,192));
//      XSSFColor color=new XSSFColor();
//      color.setRgb(rgb);
      //Set font into style
      XSSFCellStyle style = wb.createCellStyle();
     
 style.setFillForegroundColor(grey);
      style.setFont(font);
        Sheet sheet1 = wb.createSheet("Sheet1");
          Row headData = sheet1.createRow(0);
          for(int i1=0;i1<colnames.size();i1++){
           Cell rowCell1 = headData.createCell(i1);
            rowCell1.setCellValue(colnames.get(i1));
            rowCell1.setCellStyle(style);
          }
          
         for(int i=1;i<=rowCount;i++){
          Row rowData = sheet1.createRow(i);
          for(int j=0;j<columnCountt;j++){
              Cell rowCell = rowData.createCell(j);
              if(j==0){
                rowCell.setCellValue(retobj.getFieldValueString(i-1, j));  
              }else{
                  
              rowCell.setCellValue(String.valueOf(Double.valueOf(retobj.getFieldValueString(i-1, j)).longValue()));
              }
          }
         }
        try {
            File tempFile = null;
            String filePath = File.separator + "/home/progen/Downloads" + File.separator ;
//            String filePath = File.separator + "home" + File.separator + "progen" + File.separator + "Downloads" + File.separator;
//            String filePath = ".."+File.separator+"Downloads" + File.separator;
            tempFile = new File(filePath);
            if (tempFile.exists()) {
            } else {
                tempFile.mkdirs();
            }
            boolean isit=isFilenameExist( filePath,xlsxfilename+"MB_sample.xlsx");
             if(isFilenameExist( filePath,xlsxfilename+"MB_sample.xlsx")){
               String name=  isFilenameExistGetAnotherName( filePath, xlsxfilename, "MB_sample", ".xlsx");
                 tempFile = new File(filePath+name);
             }else{
                 
                 tempFile = new File(filePath+xlsxfilename+"MB_sample.xlsx");
             }
            
            FileOutputStream fileOut = new FileOutputStream(tempFile);
//            FileOutputStream fileOut = new FileOutputStream("/home/progen/Downloads/Exlfolder/MB_sample.xlsx");
            wb.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return null;
    }
    
    
    private static boolean isFilenameExist(String filePath,String FileName){
         File dir = new File(filePath);
	    File[] dir_contents = dir.listFiles();
            boolean flag=false;
	    for (int i = 0; i < dir_contents.length; i++) 
            {
	        if (dir_contents[i].getName().equals(FileName))
                {
                    flag= true;
                }else{
                    
                }
	    }
            return flag;
    }
    
    private static String isFilenameExistGetAnotherName(String filePath,String FileName1,String FileName2,String ext){
         File dir = new File(filePath);
	    File[] dir_contents = dir.listFiles();
            String flag="";
            String FileName=FileName1+FileName2+ext;
	    for (int i = 0; i < dir_contents.length; i++) 
            {
	        if (dir_contents[i].getName().equals(FileName))
                {
                    flag= FileName1+FileName2+i+ext;
                }else{
                    
                }
	    }
            return flag;
    }
    
    //for graph data dowload in CSV
    public String writeGraphDatainCSV(PbReturnObject retobj){
    int rowCount=retobj.getRowCount();
    int columnCountt=retobj.getColumnCount();
        try
	{
	    FileWriter writer = new FileWriter("d:/test/CsvTest.csv");
        for(int i=0;i<rowCount;i++){
            if(i !=0){
            writer.append('\n');
            }
          for(int j=0;j<columnCountt;j++){
          if(j==0){
              writer.append(retobj.getFieldValueString(i, j));  
              writer.append(',');
              }else{
              writer.append(String.valueOf(Double.valueOf(retobj.getFieldValueString(i, j)).longValue()));
              writer.append(',');
              }
          }
          }

	    writer.flush();
	    writer.close();
	}
	catch(IOException e)
	{
	     e.printStackTrace();
	} 
    return null;
    }
    public void saveGlobalDateGraph(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String userId = request.getParameter("usersId");
        String reportId = request.getParameter("graphsId");
        String fileLocation="";
        try {
            String qry2 = "select savepoint_id,savepoint_name, collect_filename,container_filename from prg_report_savepoint_details where user_id='&' and report_id='&' and is_enabled='&'";
            Object[] obj = new Object[3];
            obj[0] = userId;
            obj[1] = reportId;
            obj[2] = "true";
            String finalqry = pbdb.buildQuery(qry2, obj);
            PbReturnObject result = pbdb.execSelectSQL(finalqry);
            String savepointId = "";
            String savepointName = "";
            String collectFileName = "";
            String containerFileName = "";
            if (result != null && result.rowCount > 0) {

                savepointId = result.getFieldValueString(0, 0);
                savepointName = result.getFieldValueString(0, 1);
                collectFileName = result.getFieldValueString(0, 2);
                containerFileName = result.getFieldValueString(0, 3);
}

            if (savepointId != null && !savepointId.isEmpty()) {
                String usrSavepointName = null;
                HashMap map1 = new HashMap();
                Container container2 = null;
                map1 = (HashMap) session.getAttribute("PROGENTABLES");
                container2 = (Container) map1.get(reportId);
                //for serializing object into file
                ProgenReportViewerDAO dao = null;
                PbReportCollection collect2 = container2.getReportCollect();
                ObjectOutputStream out = null;
                String filePath = null;
                FileOutputStream fileOut = null;
                String overriteFlag = "false";
                String currenCollectSavepointPath = null;
                String collectfilename = null;
                String status1[] = null;
                String container_filename = null;
                String savepoint_id = null;
                String owriteSavepointName = null;
                boolean exceptionFlag = false;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
                SimpleDateFormat formatter2db = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date2 = new Date();
                String createdDate = formatter.format(date2);
                createdDate = createdDate.replace(" ", "_");
//                String fileLocation = null;
                PbReportViewerDAO pbDao = new PbReportViewerDAO();
                if (session != null) {
                    fileLocation = pbDao.getFilePath(session);
                } else {
                    fileLocation = "/usr/local/cache";
                }
                try {
                    File tempFile = null;
                    filePath = fileLocation + File.separator + userId + File.separator + reportId;
                    tempFile = new File(filePath);
                    if (tempFile.exists()) {
                    } else {
                        tempFile.mkdirs();
                    }
                collectfilename = collectFileName;
                container_filename = containerFileName;
                savepoint_id = savepointId;
                owriteSavepointName = savepointName;
                currenCollectSavepointPath = filePath + File.separator + collectfilename;
                fileOut = new FileOutputStream(currenCollectSavepointPath);
                out = new ObjectOutputStream(fileOut);
                out.writeObject(collect2);
                overriteFlag = "true";

                } catch (IOException i) {
                    logger.error("Exception:", i);
                    exceptionFlag = true;
                    overriteFlag = "false";
                } finally {
                    if (out != null && fileOut != null) {
                        try {
                            out.reset();
                            out.close();
                            fileOut.flush();
                            fileOut.close();
                        } catch (IOException ex) {
                            logger.error("Exception:", ex);
                        }
                    }
                }
                String currentContainerObjectPath = null;
                try {
                    FileOutputStream fileOut2 = null;
                    ObjectOutputStream out2 = null;
                    filePath = fileLocation + File.separator + userId + File.separator + reportId;
                        currentContainerObjectPath = filePath + File.separator + container_filename;
                        fileOut2 = new FileOutputStream(currentContainerObjectPath);
                        out2 = new ObjectOutputStream(fileOut2);
                        out2.writeObject(container2);
                    out2.reset();
                    out2.close();
                    fileOut2.flush();
                    fileOut2.close();
//                    session.setAttribute("CONTAINER_OBJPATH_" + userId + "_" + ReportId, currentContainerObjectPath);
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                    exceptionFlag = true;
                    overriteFlag = "false";
                }
                createdDate = formatter2db.format(date2);
                String savepntname = null;

        if (overriteFlag.equalsIgnoreCase("true")) {
            dao = new ProgenReportViewerDAO();
            if (usrSavepointName != null && !usrSavepointName.isEmpty()) {
                savepntname = usrSavepointName;
            } else {
                savepntname = owriteSavepointName;
            }
            dao.updateCurrentLocalSavePointPath(userId, reportId, savepntname, currenCollectSavepointPath, collectfilename, currentContainerObjectPath, container_filename, "true", createdDate, savepoint_id);
        }
            }//if retunrObject==null
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }
 
}
