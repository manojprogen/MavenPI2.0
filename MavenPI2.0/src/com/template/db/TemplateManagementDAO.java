/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.template.db;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.progen.report.DashboardChartData;
import com.progen.report.DashboardChartMeta;
import com.progen.report.DateComperator;
import com.progen.report.FileReadWrite;
import com.progen.report.JsonComperator;
import com.progen.report.ReportObjectQuery;
import com.progen.report.query.ProgenAOQuery;
import com.progen.report.template.TemplateMeta;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.db.JsonGenerator;
import com.template.action.ManagementResourceBundleMySql;
import com.template.action.ManagementResourceBundleOracle;
import com.template.action.ManagementResourceBundleSqlServer;
import com.template.db.TemplatePageGenerator;
import com.template.meta.ManagementTemplateMeta;
import com.template.meta.TemplateInfoMeta;
import com.template.util.TemplateContainer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.ProgenTimeDefinition;
import utils.db.ProgenConnection;

/**
 *
 * @author mayank
 */
public class TemplateManagementDAO {

  public static Logger logger = Logger.getLogger(TemplateManagementDAO.class);
//    Container container = new Container();
    ManagementTemplateMeta reportMeta = new ManagementTemplateMeta();
    TemplateInfoMeta templateMeta=new TemplateInfoMeta();
    FileReadWrite fileReadWrite = new FileReadWrite();
    HashMap filterLookupOriginalToNew = null;   
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
    
public String getChartsDataDrills(HttpServletRequest request, String fileLocation) throws FileNotFoundException, SQLException {
        String sortColumns = "";
        String sortType = "";
        String innersortColumns = "";
        String innersortType = "";
        String reportId = "";
        String filterClause = "";
        String timeClause = "";
        String timeClause1 = "";
        String dimSecurityClause = "";
        String regid = "";
        String viewByChange = "";
          ArrayList rowViewbyCols = null;
        HashMap NonViewByMap = new HashMap();
        HashMap<String, String> CrosstabMsrMap = new HashMap<>();
        PbReturnObject newCrossRetObj;
        newCrossRetObj = null;

        String viewChartId = "";
        Map returnObjectMap = new HashMap();
      
        HttpSession session = request.getSession(false);
        String chartname = request.getParameter("chartname");
        String chartFlag = request.getParameter("chartFlag");
        String divId = request.getParameter("chartID");
        String action = request.getParameter("action");
        String actionGo = request.getParameter("actionGo");
        viewByChange = request.getParameter("changeView");
        viewChartId = request.getParameter("viewChartId");
       
//        reportId = request.getParameter("reportId");
//        reportId = "4243";
        String aoAsGoId = null;
        String userid = String.valueOf(session.getAttribute("USERID"));
        List<String> timeDetails = new ArrayList<String>();
        ArrayList<String> timeDetailsGO = new ArrayList<>();
//         = new PbReportCollection();
//        session.getAttribute("tempPages");
        Gson gson = new Gson();
        PbDb pbdb = new PbDb();
        
        if (reportId == null) {
            reportId = request.getParameter("graphsId");
        }
//        Type tarType = new TypeToken<ManagementTemplateMeta>() {
//        }.getType();
        reportMeta = new ManagementTemplateMeta();
       TemplatePageGenerator pageGenerator=new TemplatePageGenerator();
        templateMeta = pageGenerator.setTemplateInfoForm(request);
        setTemplateForm(request);
        
        ReportObjectQuery objectQuery = new ReportObjectQuery();
        TemplateContainer container = new TemplateContainer();

        timeDetails =  reportMeta.getTimedetails();
            for(String timeString : timeDetails){
                timeDetailsGO.add(timeString);
            }
          Set<String> reportKeys = templateMeta.getPageMap().keySet();
            for(String id : reportKeys){
                reportId = id;
                break;
            }
            if(request.getParameter("pagesId")!=null){
                String pageReportId = request.getParameter("pagesId");
                reportId = pageReportId.split(":")[1];
            }
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        HashMap<String, List> drillmapFromSet = new HashMap<String, List>();
        HashMap<String, List> drillmapFromSetAgg = new HashMap<String, List>();
        List<String> driverList = new ArrayList<String>();
        if (request.getParameter("driverList") != null) {
            driverList = gson.fromJson(request.getParameter("driverList"), new TypeToken<List<String>>() {
            }.getType());
        }

        if (reportMeta.getDrills() != null) {
            map = reportMeta.getDrills();
            Set<Map.Entry<String, List<String>>> set = map.entrySet();

            for (Map.Entry<String, List<String>> entry : set) {
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
                        String timeViewBy = getTimeDimensionView(entry.getKey(), reportMeta);
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

            
                //end of drillacross
            }
        }
        Map<String, List<String>> filtermapGO = new HashMap<String, List<String>>();
        filtermapGO = reportMeta.getFilterMap();
        List filterMap = null;
//       if(request.getAttribute("filterMap")!=null){
        filtermapGO = (Map<String, List<String>>) request.getAttribute("filterMap");
//        filterMap=filtermapGO.keySet();
//        for(key val:filterMap)
        if (filtermapGO != null) {
            Set keySet = filtermapGO.keySet();
            Iterator itr = keySet.iterator();
            String key;
            while (itr.hasNext()) {
                key = itr.next().toString();
                filterMap.add(filtermapGO.get(key));
            }
        }
//        HashMap<String, List> filtermapFromSet = new HashMap<String, List>();
//
//        if(reportMeta.getFilterMap()!=null){
//            filtermap = reportMeta.getFilterMap();
//    Set<Entry<String, List<String>>> filterset = filtermap.entrySet();
//    for(Entry<String, List<String>> entry : filterset)
//    {
//        filtermapFromSet.put(entry.getKey(), entry.getValue());
//    }
//        }

//         HashMap<String, List> inMap  = new HashMap<String, List>();
//                            HashMap<String, List> notInMap = new HashMap<String, List>();
        HashMap<String, List> likeMap = new HashMap<>();
        HashMap<String, List> notLikeMap = new HashMap<>();
//                     filterClause = objectQuery.getFilterClause(drillmapFromSet,filtermapFromSet,likeMap,notLikeMap);
        Map<String, DashboardChartData> chartData;
        Map<String, List<Map<String, String>>> dataMap = new HashMap<>();
        Map<String, String> dataMapgblsave = new HashMap<>();//sandeep
        if (request.getSession(false).getAttribute("dataMapgblsave") != null) {
            dataMapgblsave = (Map<String, String>) request.getSession(false).getAttribute("dataMapgblsave");
        }

            chartData = new LinkedHashMap<>(reportMeta.getChartData());
// added for display label by mayank
//        String def_id = "";
//        PbDisplayLabel dispLabel = PbDisplayLabel.getPbDisplayLabel();
//        if (dispLabel != null) {
//            def_id = dispLabel.getDefaultCompanyId();
//            if (dispLabel != null && reportMeta.getViewbysIds() != null) {
//                List<String> viewLabel = new ArrayList<String>();
//                for (int i = 0; i < reportMeta.getViewbysIds().size(); i++) {
//                    String id = reportMeta.getViewbysIds().get(i);
//                    String viewName = reportMeta.getViewbys().get(i);
//                    if (id != null && dispLabel.getColDisplayWithCompany(def_id, id) != null) {
//                        viewLabel.add(dispLabel.getColDisplayWithCompany(def_id, id));
//                    } else {
//                        viewLabel.add(viewName);
//                    }
//                }
//                reportMeta.setViewbys(viewLabel);
//            }
//            if (dispLabel != null && reportMeta.getMeasuresIds() != null) {
//                List<String> measLabel = new ArrayList<String>();
//                for (int i = 0; i < reportMeta.getMeasuresIds().size(); i++) {
//                    String measId = reportMeta.getMeasuresIds().get(i);
//                    String measName = reportMeta.getMeasures().get(i);
//                    if (measId != null && dispLabel.getColDisplayWithCompany(def_id, measId) != null) {
//                        measLabel.add(dispLabel.getColDisplayWithCompany(def_id, measId));
//                    } else {
//                        measLabel.add(measName);
//                    }
//                }
//                reportMeta.setMeasures(measLabel);
//            }
//        }
//        ProgenTimeDefinition timeDefObj = ProgenTimeDefinition.getInstance(reportId, container,userid);
//        if (timeDefObj != null) {
//            if (timeDefObj != null && reportMeta.getMeasuresIds() != null) {
//                List<String> measLabel = new ArrayList<String>();
//                for (int i = 0; i < reportMeta.getMeasuresIds().size(); i++) {
//                    String measId = reportMeta.getMeasuresIds().get(i);
//                    String measName = reportMeta.getMeasures().get(i);
//                    if (measId != null && timeDefObj.getTimeDefinition() != null) {
//                        measLabel.add(getTimeBasedMeasure(timeDefObj, measName, measId, timeDetailsGO));
//                    } else {
//                        measLabel.add(measName);
//                    }
//                }
//                reportMeta.setMeasures(measLabel);
//            }
//        }

        List<String> charts = new ArrayList(chartData.keySet());
        Map cutOfmap = new HashMap();
        Map cutOfmap1 = new HashMap();
        List<String> kpiChartId = new ArrayList<>();
        for (String chart : charts) {

           
            //added by mayank
            if (chartFlag != null && chartFlag.equalsIgnoreCase("true") && divId != null && !divId.equalsIgnoreCase("")) {

                chart = divId;
            }
            if (viewByChange != null && viewByChange.equalsIgnoreCase("viewByChange")) {
                chart = viewChartId;
            }
            DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
            /*Added by Ashutosh*/
            Map<String, String> sortMeasure = chartDetails.getSortMeasure();    // add for sort table 
            String index = "0";
            try {
                Set<String> keySet = sortMeasure.keySet();
                for (String i : keySet) {
                    index = i;
                }
            } catch (Exception ex) {
                index = "0";
//                logger.error("Exception:", ex);
            }
            int measureIndex = Integer.parseInt(index);
            ProgenAOQuery objQuery = new ProgenAOQuery();
            setGlobalValuetoReportMeta(session, reportMeta, chartDetails);
//            if (dispLabel != null && chartDetails.getViewIds() != null) {
//                List<String> viewLabel = new ArrayList<>();
//                for (int i = 0; i < chartDetails.getViewIds().size(); i++) {
//                    String id = chartDetails.getViewIds().get(i);
//                    String viewName = chartDetails.getViewBys().get(i);
//                    if (id != null && dispLabel.getColDisplayWithCompany(def_id, id) != null) {
//                        viewLabel.add(dispLabel.getColDisplayWithCompany(def_id, id));
//                    } else {
//                        viewLabel.add(viewName);
//                    }
//                }
//                chartDetails.setViewBys(viewLabel);
//            }
//            if (dispLabel != null && chartDetails.getMeassureIds() != null) {
//                List<String> measLabel = new ArrayList<>();
//                for (int i = 0; i < chartDetails.getMeassureIds().size(); i++) {
//                    String id = chartDetails.getMeassureIds().get(i);
//                    String measName = chartDetails.getMeassures().get(i);
//                    if (id != null && dispLabel.getColDisplayWithCompany(def_id, id) != null) {
//                        measLabel.add(dispLabel.getColDisplayWithCompany(def_id, id));
//                    } else {
//                        measLabel.add(measName);
//                    }
//                }
//                chartDetails.setMeassures(measLabel);
//            }
            if (reportMeta.getDrillFormat() != null && (reportMeta.getDrillFormat().equalsIgnoreCase("time") || !drillmapFromSetAgg.keySet().isEmpty())) {
                timeDetailsGO = new ArrayList<String>();
                timeDetailsGO = getTimeClauseForGoAgg(drillmapFromSet, drillmapFromSetAgg, timeDetailsGO, reportMeta, chartDetails);
                objQuery.setTimeDetails(timeDetailsGO);
            } else {
                if (chartDetails.getTimeEnable() != null && chartDetails.getTimeEnable().equalsIgnoreCase("Yes")) {
                    objQuery.setIsChartTimeEnable("Yes");
                    objQuery.setChartTimeClause(chartDetails.getTimeBasedData().get(0));
                }
//                   else{
                System.out.println("***********************************************timeDetails**************" + timeDetailsGO.toString());
                objQuery.setTimeDetails(timeDetailsGO);
//                   }   
            }
//            if (timeDefObj != null && chartDetails.getMeassureIds() != null) {
//                List<String> measLabel = new ArrayList<>();
//                List<String> compMeasLabel = new ArrayList<>();
//                for (int i = 0; i < chartDetails.getMeassureIds().size(); i++) {
//                    String measId = chartDetails.getMeassureIds().get(i);
//                    String measName = chartDetails.getMeassures().get(i);
//                    if (measId != null && timeDefObj.getTimeDefinition() != null) {
//                        measLabel.add(getTimeBasedMeasure(timeDefObj, measName, measId, timeDetailsGO));
//                    } else {
//                        measLabel.add(measName);
//                    }
//                }
//                chartDetails.setMeassures(measLabel);
//                if (chartDetails.getComparedMeasureId() != null && !chartDetails.getComparedMeasureId().isEmpty()) {
//                    for (int k = 0; k < chartDetails.getComparedMeasureId().size(); k++) {
//                        String comId = chartDetails.getComparedMeasureId().get(k);
//                        String comMeas = chartDetails.getComparedMeasure().get(k);
//                        if (comId != null && timeDefObj.getTimeDefinition() != null) {
//                            compMeasLabel.add(getTimeBasedMeasure(timeDefObj, comMeas, comId, timeDetailsGO));
//                        } else {
//                            compMeasLabel.add(comMeas);
//                        }
//                    }
//                    chartDetails.setComparedMeasure(compMeasLabel);
//                }
//            }

            if ((chartDetails.getCompleteChartData() != null && chartDetails.getCompleteChartData().equalsIgnoreCase("Yes"))
                    || (request.getParameter("type") != null && request.getParameter("type").equalsIgnoreCase("trend"))
                    || (filterClause != null && !filterClause.equalsIgnoreCase(""))) {
                timeClause = "";
            } else {
                timeClause = timeClause1;
            }
            if (reportMeta.getDrillFormat() != null && reportMeta.getDrillFormat().equalsIgnoreCase("time")) {

                timeClause = "";
            } else {
                if (chartDetails.getCompleteChartData() != null && chartDetails.getCompleteChartData().equalsIgnoreCase("Yes")) {
                    timeClause = "";
                } else {
                    timeClause = timeClause1;
                }

            }
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
            if(chartDetails.getChartType().equalsIgnoreCase("Cross-Table") && chartDetails.getColViewIds()!=null){
                for(int k=0;k<chartDetails.getColViewIds().size();k++){
                    colViewIds.add(chartDetails.getColViewIds().get(k));
                }
            }
            HashMap<String, List> filtermapFromSet = new HashMap<String, List>();
            HashMap<String, List> localfiltermapFromSet = new HashMap<String, List>();
            HashMap<String, List> localdrillmap = new HashMap<String, List>();
//            if (request.getParameter("filters1") != null) {
//                boolean flag = false;
//                if ((request.getParameter("isNewReport") != null && request.getParameter("isNewReport").equalsIgnoreCase("true"))) {
//                    flag = true;
//                } else {
//                    flag = false;
//                }
//                if (!flag) {
//                    Type tarType1 = new TypeToken<Map<String, List<String>>>() {
//                    }.getType();
//                    Map<String, List<String>> map2 = gson.fromJson(request.getParameter("filters1"), tarType1);
////Added By Ram
//                    HashMap lookupFilter = container.getFilterLookupData();
//                    filterLookupOriginalToNew = container.getFilterLookupOriginalToNew();
//                    for (int t = 0; t < reportMeta.getViewbysIds().size(); t++) {
//                        String id = reportMeta.getViewbysIds().get(t);
//                        if (map2 != null) {
//                            if (map2.containsKey(id)) {
//                                if (!map2.get(id).isEmpty()) {
//                                    List list = map2.get(id);
//                                    for (int g = 0; g < list.size(); g++) {
//                                        if (lookupFilter.containsKey(list.get(g))) {
//                                            list.set(g, lookupFilter.get(list.get(g)));
//                                        }
//                                    }
//                                    map2.put(id, list);
//                                }
//                            }
//                        }
//                    }
// End Ram Code
//                    reportMeta.setFilterMap(map2);
//                }
//            }
            if (reportMeta.getFilterMap() != null) {
//            filtermap = reportMeta.getFilterMap();
                Set<Map.Entry<String, List<String>>> filterset = reportMeta.getFilterMap().entrySet();
                for (Map.Entry<String, List<String>> entry : filterset) {
                        //  List<String> viewbyids = new ArrayList(reportMeta.getViewbysIds());
                        if (reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("time") || reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("TIME")) {
                            if (entry.getKey().equalsIgnoreCase("TIME") || entry.getKey().equalsIgnoreCase("time")) {
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
                                filtermapFromSet.put(keyValue, entry.getValue());
                            } else {
                                filtermapFromSet.put(entry.getKey(), entry.getValue());
                            }
                        } else {
                            filtermapFromSet.put(entry.getKey(), entry.getValue());
                        }
                }
            }
            if (action != null && action.equalsIgnoreCase("localfilter")) {

                if (reportMeta.getChartData().get(chart).getFilters() != null) {
                    List<String> listVal = new ArrayList<String>();
                    Set<Map.Entry<String, List<String>>> filterset1 = reportMeta.getChartData().get(chart).getFilters().entrySet();
                    if (filterset1.size() > 0) {
                        drillmapFromSet = new HashMap<String, List>();
                        for (Map.Entry<String, List<String>> entry1 : filterset1) {
                            listVal = entry1.getValue();
                            if (reportMeta.getChartData().get(chart).getglobalEnable() != null && reportMeta.getChartData().get(chart).getglobalEnable().equalsIgnoreCase("Y") && reportMeta.getFilterMap() != null) {
                                Set<Map.Entry<String, List<String>>> filterset = reportMeta.getFilterMap().entrySet();
                                for (Map.Entry<String, List<String>> entry : filterset) {
                                    if (entry.getKey().equalsIgnoreCase(entry1.getKey())) {
                                        for (int m = 0; m < listVal.size(); m++) {
                                            if (entry.getValue().contains(listVal.get(m).toString())) {
                                            } else {
                                                entry.getValue().add(listVal.get(m));
                                            }
                                        }
                                        if (reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("time") || reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("TIME")) {
                                            if (entry.getKey().equalsIgnoreCase("TIME") || entry.getKey().equalsIgnoreCase("time")) {
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
                                                localfiltermapFromSet.put(keyValue, entry.getValue());
                                            } else {
                                                localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                            }
                                        } else {
                                            localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                        }
//localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                    } else {
                                        if (reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("time") || reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("TIME")) {
                                            if (entry.getKey().equalsIgnoreCase("TIME") || entry.getKey().equalsIgnoreCase("time")) {
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
                                                localfiltermapFromSet.put(keyValue, entry.getValue());
                                            } else {
                                                localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                            }
                                        } else {
                                            localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                        }
                                    }

                                }
                            } else {
                                if (reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("time") || reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("TIME")) {
                                    if (entry1.getKey().equalsIgnoreCase("TIME") || entry1.getKey().equalsIgnoreCase("time")) {
                                        String timeDeatailsVar = timeDetailsGO.get(3).toString();
                                        String keyValue = "";
                                        if (timeDeatailsVar.equalsIgnoreCase("Month")) {
                                            keyValue = "MONTH_YEAR";
                                        } else if (timeDeatailsVar.equalsIgnoreCase("Qtr")) {
                                            keyValue = "QTR_YEAR";
                                        } else if (timeDeatailsVar.equalsIgnoreCase("Year")) {
                                            keyValue = "YEAR_NAME";
                                        } else {
                                            keyValue = entry1.getKey();
                                        }
                                        localfiltermapFromSet.put(keyValue, listVal);
                                    } else {
                                        localfiltermapFromSet.put(entry1.getKey(), listVal);
                                    }
                                } else {
                                    localfiltermapFromSet.put(entry1.getKey(), listVal);
                                }
                                // localfiltermapFromSet.put(entry1.getKey(), listVal);
                            }
                        }
                        drillmapFromSet = localfiltermapFromSet;
                    } else {
                    }
                }

            }
            if (action != null && action.equalsIgnoreCase("localfilterGraphs")) {

                if (reportMeta.getChartData().get(chart).getFilters() != null) {
//                  HashMap<String, List> filtermapFromSetLocal = new HashMap<String, List>();
                    List<String> listVal = new ArrayList<>();
//                  filtermapFromSetLocal=filtermapFromSet;
                    Set<Map.Entry<String, List<String>>> localfilterset = reportMeta.getChartData().get(chart).getFilters().entrySet();
                    for (Map.Entry<String, List<String>> entry1 : localfilterset) {
                        listVal = entry1.getValue();
                        if (reportMeta.getChartData().get(chart).getglobalEnable() != null && reportMeta.getChartData().get(chart).getglobalEnable().equalsIgnoreCase("Y") && (reportMeta.getFilterMap() != null && !(reportMeta.getFilterMap().isEmpty()))) {
                            Set<Map.Entry<String, List<String>>> filterset = reportMeta.getFilterMap().entrySet();
                            for (Map.Entry<String, List<String>> entry : filterset) {
                                List<String> listValNew = new ArrayList<>();
                                listValNew = entry.getValue();
                                if (entry.getKey().equalsIgnoreCase(entry1.getKey())) {

                                    for (int m = 0; m < listVal.size(); m++) {
                                        if (listValNew.contains(listVal.get(m).toString())) {
                                        } else {
                                            listValNew.add(listVal.get(m).toString());
                                        }
//    for(int m1=0;m1<entry.getValue().size();m1++){
//        if(!entry.getValue().get(m1).toString().equalsIgnoreCase(listVal.get(m).toString())){
//            listVal.add(entry.getValue().get(m1));
//        }else{
//            
//        }
//    }

                                    }
                                    if (reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("time") || reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("TIME")) {
                                        if (entry.getKey().equalsIgnoreCase("TIME") || entry.getKey().equalsIgnoreCase("time")) {
                                            String timeDeatailsVar = timeDetailsGO.get(3);
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
                                            localfiltermapFromSet.put(keyValue, listValNew);
                                        } else {
                                            localfiltermapFromSet.put(entry.getKey(), listValNew);
                                        }
                                    } else {
                                        localfiltermapFromSet.put(entry.getKey(), listValNew);
                                    }
//localfiltermapFromSet.put(entry.getKey(), listValNew);
                                } else {
                                    if (reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("time") || reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("TIME")) {
                                        if (entry.getKey().equalsIgnoreCase("TIME") || entry.getKey().equalsIgnoreCase("time")) {
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
                                            localfiltermapFromSet.put(keyValue, entry.getValue());
                                            localfiltermapFromSet.put(keyValue, entry1.getValue());
                                        } else {
                                            localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                            localfiltermapFromSet.put(entry1.getKey(), entry1.getValue());
                                        }
                                    } else {
                                        localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                        localfiltermapFromSet.put(entry1.getKey(), entry1.getValue());
                                    }
//            localfiltermapFromSet.put(entry.getKey(), entry.getValue());
//            localfiltermapFromSet.put(entry1.getKey(), entry1.getValue());
                                }

                            }
                        } else {
                            localfiltermapFromSet.put(entry1.getKey(), listVal);
                        }

                    }

                    filterClause = "";
                    filterClause = objectQuery.getFilterClause(drillmapFromSet, localfiltermapFromSet, likeMap, notLikeMap);
                } else {
                    filterClause = "";
                    filterClause = objectQuery.getFilterClause(drillmapFromSet, filtermapFromSet, likeMap, notLikeMap);
                }

            } else {
                if (reportMeta.getChartData().get(chart).getFilters() != null) {
                    HashMap<String, List> filtermapFromSetLocal = new HashMap<>();
                    filtermapFromSetLocal = filtermapFromSet;
                    Set<Map.Entry<String, List<String>>> filterset = reportMeta.getChartData().get(chart).getFilters().entrySet();
                    for (Map.Entry<String, List<String>> entry : filterset) {
                        if ((reportMeta.getChartData().get(chart).getOthersL() == null || reportMeta.getChartData().get(chart).getOthersL().equalsIgnoreCase("Y")) && !entry.getKey().equalsIgnoreCase(chartDetails.getViewIds().get(0))) {
                            List<String> listVal = new ArrayList<>();
                            if (filtermapFromSetLocal.get(entry.getKey()) != null) {
                                listVal = filtermapFromSetLocal.get(entry.getKey());
                                listVal.addAll(listVal.size(), entry.getValue());
                            } else {
                                listVal = entry.getValue();
                            }
                            if (reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("time") || reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("TIME")) {
                                if (entry.getKey().equalsIgnoreCase("TIME") || entry.getKey().equalsIgnoreCase("time")) {
                                    String timeDeatailsVar = timeDetailsGO.get(3);
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
                                    //  localfiltermapFromSet.put(keyValue, entry.getValue());
                                    filtermapFromSetLocal.put(keyValue, entry.getValue());
                                } else {
                                    //   localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                    filtermapFromSetLocal.put(entry.getKey(), entry.getValue());
                                }
                            } else {
                                //   localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                filtermapFromSetLocal.put(entry.getKey(), entry.getValue());
                            }
                            //  filtermapFromSetLocal.put(entry.getKey(), listVal);
                        } else if (reportMeta.getChartData().get(chart).getOthersL() == null || reportMeta.getChartData().get(chart).getOthersL().equalsIgnoreCase("N")) {
                            filtermapFromSetLocal = new HashMap<String, List>();
                            List<String> listVal = new ArrayList<String>();
                            if (filtermapFromSetLocal.get(entry.getKey()) != null) {
                                listVal = filtermapFromSetLocal.get(entry.getKey());
                                listVal.addAll(listVal.size(), entry.getValue());
                            } else {
                                listVal = entry.getValue();
                            }

                            if (reportMeta.getChartData().get(chart).getglobalEnable() != null && reportMeta.getChartData().get(chart).getglobalEnable().equalsIgnoreCase("Y") && reportMeta.getFilterMap() != null) {
                                Set<Map.Entry<String, List<String>>> filtersetGlobal = reportMeta.getFilterMap().entrySet();
                                for (Map.Entry<String, List<String>> entry1 : filtersetGlobal) {
                                    if (entry1.getKey().equalsIgnoreCase(entry.getKey())) {
                                        List<String> listValNew = new ArrayList<String>();
                                        for (int m = 0; m < listVal.size(); m++) {
                                            for (int m1 = 0; m1 < entry1.getValue().size(); m1++) {
                                                if (entry1.getValue().get(m1).toString().equalsIgnoreCase(listVal.get(m).toString())) {
                                                    listValNew.add(entry1.getValue().get(m1));
                                                }
                                            }
                                        }
                                        if (reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("time") || reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("TIME")) {
                                            if (entry.getKey().equalsIgnoreCase("TIME") || entry.getKey().equalsIgnoreCase("time")) {
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
                                                //  localfiltermapFromSet.put(keyValue, entry.getValue());
                                                localfiltermapFromSet.put(keyValue, listValNew);
                                            } else {
                                                //   localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                                localfiltermapFromSet.put(entry1.getKey(), listValNew);
                                            }
                                        } else {
                                            //   localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                            localfiltermapFromSet.put(entry1.getKey(), listValNew);
                                        }

                                    } else {
                                        if (reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("time") || reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("TIME")) {
                                            if (entry.getKey().equalsIgnoreCase("TIME") || entry.getKey().equalsIgnoreCase("time")) {
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
                                                //  localfiltermapFromSet.put(keyValue, entry.getValue());
                                                localfiltermapFromSet.put(keyValue, entry1.getValue());
                                            } else {
                                                //   localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                                localfiltermapFromSet.put(entry1.getKey(), entry1.getValue());
                                            }
                                        } else {
                                            //   localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                            localfiltermapFromSet.put(entry1.getKey(), entry1.getValue());
                                        }

                                    }

                                }
                            } else {
                                if (reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("time") || reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("TIME")) {
                                    if (entry.getKey().equalsIgnoreCase("TIME") || entry.getKey().equalsIgnoreCase("time")) {
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
                                        //  localfiltermapFromSet.put(keyValue, entry.getValue());
                                        localfiltermapFromSet.put(keyValue, listVal);
                                    } else {
                                        //   localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                        localfiltermapFromSet.put(entry.getKey(), listVal);
                                    }
                                } else {
                                    //   localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                    localfiltermapFromSet.put(entry.getKey(), listVal);
                                }
                                //  localfiltermapFromSet.put(entry.getKey(), listVal);
                            }

                            filtermapFromSetLocal = localfiltermapFromSet;

                        }

                    }
                    filterClause = "";
                    filterClause = objectQuery.getFilterClause(drillmapFromSet, filtermapFromSetLocal, likeMap, notLikeMap);
                } else {
                    filterClause = "";
                    filterClause = objectQuery.getFilterClause(drillmapFromSet, filtermapFromSet, likeMap, notLikeMap);
                }
            }

            //for measure filters
              String measureclause = "";
            int filterCount = 0;
               if(reportMeta.getChartData().get(chart).getMeasureFilters()!=null){
                    Map<String,Map<String,String>> measureMap = reportMeta.getChartData().get(chart).getMeasureFilters();
                   Set<String> measuresFilter = measureMap.keySet();
                    ProgenAOQuery objQuery1 = new ProgenAOQuery();
                   for(String measureVal : measuresFilter){
                       try {
                           measureVal = objQuery1.removeComparisonType(measureVal);
//                           String queryForMeasure = "select user_col_name,AGGREGATION_TYPE from prg_user_all_info_details where element_id='"+measureVal+"'";
//                           PbReturnObject measureObj  = pbdb.execSelectSQL(queryForMeasure);
                           Set<String> measuresFiltersMap = measureMap.get(measureVal).keySet();
                           for(String measureSymbol : measuresFiltersMap){
                               if(filterCount==0){
                           measureclause+=" where ";
                               }else{
                               measureclause+=" and ";
                               }
                               filterCount++;
//                               if(measureObj.getFieldValueString(0, 1).equalsIgnoreCase("COUNTDISTINCT")){
                                   if(measureSymbol.equalsIgnoreCase("<>")){
                               measureclause += "A_"+measureVal+ " between "+measureMap.get(measureVal).get(measureSymbol).split("__")[0]+" and "+measureMap.get(measureVal).get(measureSymbol).split("__")[1];
                               }else{
                                 measureclause += "A_"+measureVal+" between "+measureSymbol+"  "+measureMap.get(measureVal).get(measureSymbol)+" ";
                               }
//                               }else{
//                           measureclause += measureObj.getFieldValueString(0, 1)+"("+measureObj.getFieldValueString(0, 0)+") "+measureSymbol+"  "+measureMap.get(measureVal).get(measureSymbol)+" ";
//                               }
                              }
                           }
                       catch (Exception ex) {
                           logger.error("Exception:",ex);
                       }
                   }
     }
            String action1 = request.getParameter("initializeFlag");
            if (action1 != null && action1.equalsIgnoreCase("true")) {
                if (reportMeta.getChartData().get(chart).getLocalDrills() != null && !reportMeta.getChartData().get(chart).getLocalDrills().isEmpty()) {
                    List<String> listVal = new ArrayList<String>();
                    Set<Map.Entry<String, List<String>>> localDrills = reportMeta.getChartData().get(chart).getLocalDrills().entrySet();
                    for (Map.Entry<String, List<String>> entry1 : localDrills) {
                        listVal = entry1.getValue();
                        //Added by Ram
//                        HashMap lookupFilter = container.getFilterLookupData();
//                        for (int g = 0; g < listVal.size(); g++) {
//                            if (lookupFilter.containsKey(listVal.get(g))) {
//                                listVal.set(g, lookupFilter.get(listVal.get(g)).toString());
//                            }
//                        }
                        //ended by ram
                        if (reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("time") || reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("TIME")) {
                            if (entry1.getKey().equalsIgnoreCase("TIME") || entry1.getKey().equalsIgnoreCase("time")) {
                                String timeDeatailsVar = timeDetailsGO.get(3);
                                String keyValue = "";
                                if (timeDeatailsVar.equalsIgnoreCase("Month")) {
                                    keyValue = "MONTH_YEAR";
                                } else if (timeDeatailsVar.equalsIgnoreCase("Qtr")) {
                                    keyValue = "QTR_YEAR";
                                } else if (timeDeatailsVar.equalsIgnoreCase("Year")) {
                                    keyValue = "YEAR_NAME";
                                } else {
                                    keyValue = entry1.getKey();
                                }
                                //  localfiltermapFromSet.put(keyValue, entry.getValue());
                                localdrillmap.put(keyValue, listVal);
                            } else {
                                //   localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                localdrillmap.put(entry1.getKey(), listVal);
                            }
                        } else {
                            //   localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                            localdrillmap.put(entry1.getKey(), listVal);
                        }
                        //       localdrillmap.put(entry1.getKey(), listVal);
                    }

                    filterClause = "";
                    filterClause = objectQuery.getFilterClause(localdrillmap, localfiltermapFromSet, likeMap, notLikeMap);
                } else if (action != null && action.equalsIgnoreCase("localfilterGraphs")) {
                    if (reportMeta.getChartData().get(chart).getFilters() != null) {
//                  HashMap<String, List> filtermapFromSetLocal = new HashMap<String, List>();
                        List<String> listVal = new ArrayList<String>();
//                  filtermapFromSetLocal=filtermapFromSet;
                        Set<Map.Entry<String, List<String>>> localfilterset = reportMeta.getChartData().get(chart).getFilters().entrySet();
                        for (Map.Entry<String, List<String>> entry1 : localfilterset) {
                            listVal = entry1.getValue();
                            if (reportMeta.getChartData().get(chart).getglobalEnable() != null && reportMeta.getChartData().get(chart).getglobalEnable().equalsIgnoreCase("Y") && (reportMeta.getFilterMap() != null && !(reportMeta.getFilterMap().isEmpty()))) {
                                Set<Map.Entry<String, List<String>>> filterset = reportMeta.getFilterMap().entrySet();
                                for (Map.Entry<String, List<String>> entry : filterset) {
                                    if (entry.getKey().equalsIgnoreCase(entry1.getKey())) {
                                        List<String> listValNew = new ArrayList<String>();
                                        for (int m = 0; m < listVal.size(); m++) {
                                            for (int m1 = 0; m1 < entry.getValue().size(); m1++) {
                                                if (entry.getValue().get(m1).toString().equalsIgnoreCase(listVal.get(m).toString())) {
                                                    listValNew.add(entry.getValue().get(m1));
                                                }
                                            }
                                        }
                                        if (reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("time") || reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("TIME")) {
                                            if (entry1.getKey().equalsIgnoreCase("TIME") || entry1.getKey().equalsIgnoreCase("time")) {
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
                                                //  localfiltermapFromSet.put(keyValue, entry.getValue());
                                                localfiltermapFromSet.put(keyValue, listValNew);
                                            } else {
                                                //   localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                                localfiltermapFromSet.put(entry.getKey(), listValNew);
                                            }
                                        } else {
                                            //   localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                            localfiltermapFromSet.put(entry.getKey(), listValNew);
                                        }

                                    } else {
                                        if (reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("time") || reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("TIME")) {
                                            if (entry1.getKey().equalsIgnoreCase("TIME") || entry1.getKey().equalsIgnoreCase("time")) {
                                                String timeDeatailsVar = timeDetailsGO.get(3).toString();
                                                String keyValue = "";
                                                if (timeDeatailsVar.equalsIgnoreCase("Month")) {
                                                    keyValue = "MONTH_YEAR";
                                                } else if (timeDeatailsVar.equalsIgnoreCase("Qtr")) {
                                                    keyValue = "QTR_YEAR";
                                                } else if (timeDeatailsVar.equalsIgnoreCase("Year")) {
                                                    keyValue = "YEAR_NAME";
                                                } else {
                                                    keyValue = entry1.getKey();
                                                }
                                                localfiltermapFromSet.put(keyValue, entry.getValue());
                                                localfiltermapFromSet.put(keyValue, entry1.getValue());
                                            } else {
                                                localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                                localfiltermapFromSet.put(entry1.getKey(), entry1.getValue());
                                            }
                                        } else {
                                            localfiltermapFromSet.put(entry.getKey(), entry.getValue());
                                            localfiltermapFromSet.put(entry1.getKey(), entry1.getValue());
                                        }

                                    }

                                }
                            } else {
                                if (reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("time") || reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("TIME")) {
                                    if (entry1.getKey().equalsIgnoreCase("TIME") || entry1.getKey().equalsIgnoreCase("time")) {
                                        String timeDeatailsVar = timeDetailsGO.get(3).toString();
                                        String keyValue = "";
                                        if (timeDeatailsVar.equalsIgnoreCase("Month")) {
                                            keyValue = "MONTH_YEAR";
                                        } else if (timeDeatailsVar.equalsIgnoreCase("Qtr")) {
                                            keyValue = "QTR_YEAR";
                                        } else if (timeDeatailsVar.equalsIgnoreCase("Year")) {
                                            keyValue = "YEAR_NAME";
                                        } else {
                                            keyValue = entry1.getKey();
                                        }
                                        localfiltermapFromSet.put(keyValue, listVal);
                                    } else {
                                        localfiltermapFromSet.put(entry1.getKey(), listVal);
                                    }
                                } else {
                                    localfiltermapFromSet.put(entry1.getKey(), listVal);
                                }
                                // localfiltermapFromSet.put(entry1.getKey(), listVal);
                            }

                        }

                        filterClause = "";
                        filterClause = objectQuery.getFilterClause(drillmapFromSet, localfiltermapFromSet, likeMap, notLikeMap);
                    } else {
                        filterClause = "";
                        filterClause = objectQuery.getFilterClause(drillmapFromSet, filtermapFromSet, likeMap, notLikeMap);
                    }
                } else {
                    filterClause = "";
                    filterClause = objectQuery.getFilterClause(drillmapFromSet, filtermapFromSet, likeMap, notLikeMap);
                }
            }
if (reportMeta.getNotfilters() != null) {
boolean notinfilters=false;
                Set<Map.Entry<String, List<String>>> filterset = reportMeta.getNotfilters().entrySet();
                for (Map.Entry<String, List<String>> entry : filterset) {
                if(entry.getValue().size()>0){notinfilters=true;
                        //  List<String> viewbyids = new ArrayList(reportMeta.getViewbysIds());
                        if (reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("time") || reportMeta.getChartData().get(chart).getViewIds().get(0).equalsIgnoreCase("TIME")) {
                            if (entry.getKey().equalsIgnoreCase("TIME") || entry.getKey().equalsIgnoreCase("time")) {
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
                                filtermapFromSet.put(keyValue, entry.getValue());
                            } else {
                                filtermapFromSet.put(entry.getKey(), entry.getValue());
                            }
                        } else {
                            filtermapFromSet.put(entry.getKey(), entry.getValue());
                        }
                }
                }
                if(notinfilters){
                filterClause = "";
                    filterClause = objectQuery.getFilterClauseNotin(drillmapFromSet, filtermapFromSet, likeMap, notLikeMap);
 }
            }
            if (reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Column-Pie") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Column-Donut") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Stacked-KPI") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Bullet-Horizontal") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Expression-Table") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("KPI-Table") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Table") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Scatter-Analysis") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Standard-KPI") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Standard-KPI1")  || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Radial-Chart") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("LiquidFilled-KPI") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Dial-Gauge") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("KPIDash") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Emoji-Chart") || (reportMeta.getChartType() != null && reportMeta.getChartType().equals("Time-Dashboard"))) {
                JsonGenerator jsongenerator = new JsonGenerator();
                if (reportId == null) {
                    reportId = request.getParameter("reportId");
                }
                ArrayList<String> measId = new ArrayList<String>();
                ArrayList<String> measBys = new ArrayList<String>();
                ArrayList<String> aggregation = new ArrayList<String>();
                for (int z = 0; z < reportMeta.getChartData().get(chart).getMeassureIds().size(); z++) {
                    measId.add(reportMeta.getChartData().get(chart).getMeassureIds().get(z).toString());
                    measBys.add(reportMeta.getChartData().get(chart).getMeassures().get(z).toString());
                    if (reportMeta.getChartData().get(chart).getAggregation() != null && !reportMeta.getChartData().get(chart).getAggregation().isEmpty()) {
                        aggregation.add(reportMeta.getChartData().get(chart).getAggregation().get(z).toString());
                    }
                }
                String[] measId1 = new String[measId.size()];
                String[] measBys1 = new String[measBys.size()];
                String[] aggregation1 = new String[aggregation.size()];
                for (int i = 0; i < measId.size(); i++) {
                    measId1[i] = measId.get(i);
                    measBys1[i] = measBys.get(i);
                    aggregation1[i] = aggregation.get(i);
                }

                // DashboardChartData chartData1 = new DashboardChartData();
                String GTResult = "";
//                if (reportMeta.getChartType() != null && reportMeta.getChartType().equals("Time-Dashboard")) {
//                    String firstMeasure[] = new String[]{measId1[0]};
//                    GTResult = jsongenerator.GTKPICalculateFunction(request, reportId, firstMeasure, measBys1, aggregation1, chart, timeClause, dimSecurityClause, container);
//                } else {
//                    GTResult = jsongenerator.GTKPICalculateFunction(request, reportId, measId1, measBys1, aggregation1, chart, timeClause, dimSecurityClause, container);
//                }
//          Object GTResultObject = GTResult;
//          for(int u=0;u<GTResultObject.s;u++){
//          }

//          String[] GTResultStringArray =  GTResult.replace("{", "").replace("}", "").replace("\"", "").split(",");
                List<String> GTResultMap = new ArrayList<>(Arrays.asList(GTResult.replace("[", "").replace("]", "").replace("\"", "").split(",")));
//          GTResultMap.add(GTResultStringArray[0]);
//          GTResultMap.add(GTResult .replace("{", "").replace("}", ""));
//          Map<String,String> GTResultMap = new HashMap<String, String>();
                for (String pairs : GTResultMap) {
                    String[] entry = pairs.split(":");
////          GTResultMap.put(entry[0].trim(), entry[1].trim());
                }
                reportMeta.getChartData().get(chart).setGTValueList(GTResultMap);
                    if (!reportMeta.getChartType().equals("Time-Dashboard") && !reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Table") && !reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Scatter-Analysis")) {
                        kpiChartId.add(chart);
//                        continue;
                    }

//                }

            }
            HashMap<String, List> mapSetForGraphs = new HashMap<String, List>();
            String dontDrill1 = "N";
            if (reportMeta.getChartData().get(chart).getExcludeFromDrill() != null) {
                dontDrill1 = reportMeta.getChartData().get(chart).getExcludeFromDrill().toString();
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
            List<String> grandTotalAggType = new ArrayList<String>(chartDetails.getMeassureIds().size());
            grandTotalAggType = addGrandTotalAggType(chartDetails);
//            for(int k=0;k<chartDetails.getMeassureIds().size();k++){
//                grandTotalAggType.add(k, "N");
//            }
            //  
             if (userid != null && !userid.equalsIgnoreCase("")) {

            dimSecurityClause = objectQuery.getDimSecClause(reportMeta.getViewbysIds(), userid,mapSetForGraphs);
        }
            if (chartDetails.getCompleteChartData() != null && chartDetails.getCompleteChartData().equalsIgnoreCase("Yes")) {
                objQuery.setAvoidProgenTime("Yes");
            } else {
                objQuery.setAvoidProgenTime("No");
            }
            objQuery.setDrillFormat(reportMeta.getDrillFormat());
            String query = "";
              String aoNameQuery = getResourceBundle().getString("getAOName");
              PbReturnObject retAoObj = null;
           Object[] obj = new Object[1];
           obj[0] = reportId;
           aoNameQuery = pbdb.buildQuery(aoNameQuery, obj);
            try {
                retAoObj = pbdb.execSelectSQL(aoNameQuery);
            } catch (SQLException ex) {
               logger.error(ex);
            }
           if(retAoObj!=null && retAoObj.rowCount>0){
               aoAsGoId = retAoObj.getFieldValueString(0, "AO_ID");
           }
            if (aoAsGoId != null && !aoAsGoId.equalsIgnoreCase("null") && !aoAsGoId.equalsIgnoreCase("")) {
                objQuery.setAO_name("M_AO_" + aoAsGoId);                //added by mohit for AOasGO
            }else {
                objQuery.setAO_name("R_GO_" + reportId);
            }
            
            objQuery.setDateFlag_New(chartDetails.getDateFlag_New()); //added by Ashutosh for dataslider
            objQuery.setMeasureFilter(measureclause); //added by Ashutosh for dataslider
            objQuery.setGrandTotalAggType(grandTotalAggType);
            objQuery.setViewIdList(rowIds);
            objQuery.setColViewIdList(colViewIds);
            objQuery.setMeasureIdsList(chartDetails.getMeassureIds());
            objQuery.setMeasureIdsListGO(reportMeta.getMeasuresIds());
            setTimeTypeForMeasure(chartDetails, objQuery);
            objQuery.setFilterClause(filterClause);
            if(chartDetails.getChartType().equalsIgnoreCase("Cross-Table")){
            rowViewbyCols=(ArrayList) rowIds.clone();
             if (colViewIds != null && !colViewIds.isEmpty()) {
            for (int i = 0; i < colViewIds.size(); i++) {
                rowViewbyCols.add(colViewIds.get(i));
            }
        }
            }
            if (reportMeta.getChartData().get(chart).getglobalEnable() != null && reportMeta.getChartData().get(chart).getglobalEnable().equalsIgnoreCase("Y") && (reportMeta.getFilterMap() != null && !(reportMeta.getFilterMap().isEmpty()))) {
                objQuery.setFilterMap(mapSetForGraphs);
            } else if (action != null && action.equalsIgnoreCase("localfilters")) {
                objQuery.setFilterMap(mapSetForGraphs);
            } else {

                objQuery.setFilterMap(mapSetForGraphs);
            }
            objQuery.setDimSecurityClause(dimSecurityClause);
            objQuery.setAggregationType(chartDetails.getAggregation());
            objQuery.setInnerViewByElementId("NONE");
            objQuery.setChartType(chartDetails.getChartType());
            if (chartDetails.getSortBasis() == null) {
                chartDetails.setSortBasis("Value");
            }
            objQuery.setSortingType(chartDetails.getSortBasis());
            if (chartDetails.getRecords() == null) {
                chartDetails.setSortBasis("12");
            }
            objQuery.setChartRecords(chartDetails.getRecords());
//            objQuery.collectMulticalender=container.getReportCollect().getcollectMulticalender(); //added by Mohit for change calendar
            try {
                query = objQuery.generateAOQuery();
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
//            String query = objectQuery.getObjectQueryGO(reportId,filterClause,rowIds,colViewIds,chartDetails.getMeassureIds().get(0),measureclause,isoneview,oneviewid,regid,fileLocation,timeClause,dimSecurityClause);
            PbReturnObject pbretObj = null;

//            if (conn != null) {
            try {
//   //                    if(fromoneview!=null && fromoneview.equalsIgnoreCase("true")){
//                     if ((filtermapGO!=null)||(refrehreporttime!=null && refrehreporttime.equalsIgnoreCase("true"))  &&  detail.isOneviewReportTimeDetails()) {
////                          if(refrehreporttime!=null && refrehreporttime.equalsIgnoreCase("true")){
//                pbretObj = pbdb.execSelectSQL(query, conn);
//
//                     }else if(!drillmapFromSet.isEmpty()){
//                          pbretObj = pbdb.execSelectSQL(query, conn);
//                     }
//                       } else {

                //modified by anitha
                List<Map<String, String>> list = new ArrayList<>();
//              list= getReturnObjectBasedOnChart(reportId, chart, query,fileLocation,userid);
                pbretObj = getReturnObjectBasedOnChart(reportId, chart, query, fileLocation, userid);
                List<String> colNames = new ArrayList();
                List<String> colNames1 = new ArrayList();
                List<String> colIds = new ArrayList();
                List<String> colIds1 = new ArrayList();
                List<String> meaIds = new ArrayList();
                List<String> meaName1 = new ArrayList();
                if (pbretObj == null) {
                    Connection conn = null;

                    if (chartDetails.getViewIds().get(0).trim().equalsIgnoreCase("TIME")) {
                        conn = ProgenConnection.getInstance().getConnectionForElement(removeComparisonType(chartDetails.getMeassureIds().get(0)));
                    } else {
                        conn = ProgenConnection.getInstance().getConnectionForElement(chartDetails.getViewIds().get(0));
                    }
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
                        ArrayList<String> sessionQueryLst = new ArrayList<String>();
                        try {
                            sessionQueryLst = reportTemplateDAO.getSessionQueriesForOracle();
                        } catch (Exception e) {
                            logger.error("Exception:", e);
                        }
                        //changed by Mohit for go query

                        pbretObj = pbdb.execSelectORCL(query, conn, sessionQueryLst);
//                    setReturnObjectBasedOnChart(reportId, chart, query,pbretObj,fileLocation,userid);
                    } else {
                        pbretObj = pbdb.execSelectSQL(query, conn);
//                    setReturnObjectBasedOnChart(reportId, chart, query,pbretObj,fileLocation,userid);

                    }
                    setReturnObjectBasedOnChart(reportId, chart, query, pbretObj, fileLocation, userid);
                }
                //end of code by anitha
//                    }
                if(chartDetails.getChartType().equalsIgnoreCase("Cross-Table")){
                        for (int i = 0; i < chartDetails.getMeassureIds().size(); i++) {
                            CrosstabMsrMap.put("A_"+chartDetails.getMeassureIds().get(i).toString(), chartDetails.getMeassures().get(i).toString());
                }
List<String> queryColumnsId= new ArrayList<>(chartDetails.getMeassureIds());
ArrayList crossRowId = new ArrayList();
for(int k=0;k<chartDetails.getViewIds().size();k++){
    crossRowId.add(k, chartDetails.getViewIds().get(k));
}
                    pbretObj.totalViewBys = rowViewbyCols.size();
                    pbretObj.totalOrderbys = rowViewbyCols.size();
                    pbretObj.nonViewInput = NonViewByMap;
                    pbretObj.rowViewBys =crossRowId;
                    pbretObj.ColViewBys = colViewIds;
                    pbretObj.rowViewCount = crossRowId.size();
                    pbretObj.colViewCount = colViewIds.size();
                    pbretObj.Qrycolumns = (ArrayList) queryColumnsId;
                    pbretObj.crosstabMsrMap = CrosstabMsrMap;
                    pbretObj.meausreOnCol = true;
//                    pbretObj.MeasurePos = container.getMeasurePosition();
                    pbretObj.MeasurePos = colViewIds.size();

                newCrossRetObj = pbretObj.transposeReturnObject();
                }
                colIds.add(chartDetails.getViewIds().get(0));
                colNames.add(chartDetails.getViewBys().get(0));
                if ((chartDetails.getChartType().equalsIgnoreCase("multi-view-bubble") 
                        || chartDetails.getChartType().equalsIgnoreCase("Multi-View-Tree") 
                        || chartDetails.getChartType().equalsIgnoreCase("grouped-bar") 
                        || chartDetails.getChartType().equalsIgnoreCase("GroupedHorizontal-Bar")
                        || chartDetails.getChartType().equalsIgnoreCase("Scatter-Bubble")
                        || chartDetails.getChartType().equalsIgnoreCase("Split-Bubble")
                        || chartDetails.getChartType().equalsIgnoreCase("world-map") 
                        || chartDetails.getChartType().equalsIgnoreCase("Trend-Combo")
                        || chartDetails.getChartType().equalsIgnoreCase("Grouped-Table")
                        || chartDetails.getChartType().equalsIgnoreCase("Grouped-Map")
                        || chartDetails.getChartType().equalsIgnoreCase("grouped-line")
                        || chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar")
                        || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar")
                        || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-BarLine") 
                        || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar%")
                        || chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar%")
                        || chartDetails.getChartType().equalsIgnoreCase("Multi-View-Bubble")
                        || chartDetails.getChartType().equalsIgnoreCase("Multi-View-Tree")
                        || chartDetails.getChartType().equalsIgnoreCase("Grouped-MultiMeasureBar")
                        || (reportMeta.getChartType() != null 
                        && (reportMeta.getChartType().equalsIgnoreCase("Multi-View-Bubble") 
                        || reportMeta.getChartType().equalsIgnoreCase("Split-Graph")
                        || reportMeta.getChartType().equalsIgnoreCase("India-Map-Dashboard") 
                        || reportMeta.getChartType().equalsIgnoreCase("world-map-animation")))) 
                        && chartDetails.getViewIds().size() > 1) {
                    colIds.add(chartDetails.getViewIds().get(1));
                    colNames.add(chartDetails.getViewBys().get(1));
                }
                if ((chartDetails.getChartType().equalsIgnoreCase("Table") || chartDetails.getChartType().equalsIgnoreCase("Scatter-Analysis") || chartDetails.getChartType().equalsIgnoreCase("Bar-Table") || chartDetails.getChartType().equalsIgnoreCase("Transpose-Table") || (chartDetails.getChartType().equalsIgnoreCase("Horizontal-Bar") && chartDetails.getTableWithSymbol() != null && chartDetails.getTableWithSymbol().equalsIgnoreCase("Y"))) && chartDetails.getViewIds().size() > 1) {
                    colIds = new ArrayList<>();
                    colNames = new ArrayList<>();
                    for (int k = 0; k < chartDetails.getViewIds().size(); k++) {
                        colIds.add(chartDetails.getViewIds().get(k));
                        colNames.add(chartDetails.getViewBys().get(k));
                    }

                }
                for (int m = 0; m < chartDetails.getMeassureIds().size(); m++) {
                    colIds.add(chartDetails.getMeassureIds().get(m));
                    colNames.add(chartDetails.getMeassures().get(m));
                    meaIds.add(chartDetails.getMeassureIds().get(m));
                    meaName1.add(chartDetails.getMeassures().get(m));
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
                if (chart.equalsIgnoreCase("chart1") && reportMeta.getVisualChartType() != null && request.getParameter("currType") != null && reportMeta.getVisualChartType().get(request.getParameter("currType")).equalsIgnoreCase("Overlay")) {
                    colIds.add(chartDetails.getViewIds().get(1));
                    colNames.add(chartDetails.getViewBys().get(1));
                }
                if (reportMeta.getChartData().get(chart).getSortBasis() != null) {
                    sortColumns = reportMeta.getChartData().get(chart).getSortBasis().substring(0, 1);
                } else {
                    sortColumns = "V";
                }
                if (reportMeta.getChartData().get(chart).getSorting() != null) {
                    sortType = reportMeta.getChartData().get(chart).getSorting().substring(0, 1);
                } else {
                    sortType = "D";
                }
//                     List<Map<String, String>> list = new  ArrayList<Map<String, String>>();
                if (chart.equalsIgnoreCase("chart1") && reportMeta.getVisualChartType() != null && request.getParameter("currType") != null && reportMeta.getVisualChartType().get(request.getParameter("currType")).equalsIgnoreCase("Overlay")) {
                } else {

                    if(chartDetails.getChartType().equalsIgnoreCase("Cross-Table")){
                        int size = chartDetails.getViewIds().size();
                        chartDetails.setCrossTabFinalOrder(newCrossRetObj.CrossTabfinalOrder);
                        Set<String> keys = newCrossRetObj.nonViewByMapNew.keySet();
                        Map<String,List<String>> headerMap = new HashMap<>();
                        for(String key:keys){
                            headerMap.put(key,newCrossRetObj.nonViewByMapNew.get(key));
                        }
                        chartDetails.setcrossTableHeaderMap(headerMap);
//                        int size = chartDetails.getViewIds().size() + chartDetails.getColViewIds().size();
                    list = generateCrossTabData(newCrossRetObj, colNames, colIds, size, session, timeDetailsGO);
                    }else{
                        
                    list = generateChartJson(pbretObj, colNames, colIds, reportMeta.getChartData().get(chart).getViewIds().size(), session, timeDetailsGO);
                   
                    getSortMeasureValue(chartDetails,list);
                    }
//                     if(chartDetails.getChartType().equalsIgnoreCase("Combo-Analysis") && !chartDetails.isComboData())
//                       setComboData(reportId,chart,list,fileLocation,userid,chartDetails.getComboType());
                }

//                     List<Map<String, String>> list = generateChartJson(pbretObj,colNames,colIds);

                Map mapData = new HashMap();
//                if (reportMeta.getChartType() != null && (reportMeta.getChartType().equalsIgnoreCase("Fish-Eye") || reportMeta.getChartType().equalsIgnoreCase("tree-map") || reportMeta.getChartType().equalsIgnoreCase("CoffeeWheel") || reportMeta.getChartType().equalsIgnoreCase("tree-map-single") || chartDetails.getChartType().equalsIgnoreCase("Multi-view-tree"))) {
//                    List<Map<String, List<Double>>> listData = generateChartJsonHie(pbretObj, colNames, colIds, colIds1, colNames1, meaIds, meaName1);
//                    Map<String, List<String>> combineList = combineChunks(listData, meaName1);
////list = new HashMap<String, List<Double>>();
//                    if (reportMeta.getChartType().equalsIgnoreCase("Tree-map-single")) {
//                        mapData = combineList;
//                    } else {
//                        mapData = processHirarchiJson(combineList);
//                    }
//// container = Container.getContainerFromSession(request, reportId);
//// container.setTreeData(mapData);
////    return gson.toJson(mapData);
////
//                }

                if (request.getParameter("type") != null && request.getParameter("type").equalsIgnoreCase("graph") && (chartDetails.getChartType().equalsIgnoreCase("multi-view-bubble") || chartDetails.getChartType().equalsIgnoreCase("Multi-View-Tree") || chartDetails.getChartType().equalsIgnoreCase("grouped-bar") || chartDetails.getChartType().equalsIgnoreCase("GroupedHorizontal-Bar") || chartDetails.getChartType().equalsIgnoreCase("Scatter-Bubble") || chartDetails.getChartType().equalsIgnoreCase("Split-Bubble") || chartDetails.getChartType().equalsIgnoreCase("world-map") || chartDetails.getChartType().equalsIgnoreCase("Trend-Combo") || chartDetails.getChartType().equalsIgnoreCase("Grouped-Table") || chartDetails.getChartType().equalsIgnoreCase("Grouped-Map") || chartDetails.getChartType().equalsIgnoreCase("grouped-line") || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar") || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-BarLine") || chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar") || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar%") || chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar%") || chartDetails.getChartType().equalsIgnoreCase("Grouped-MultiMeasureBar"))) {

                    if (sortColumns != null && "A".equals(sortColumns)) {
                        if (sortType != null && "D".equals(sortType)) {
                            JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(measureIndex), "desc", "");
                            Collections.sort(list, jsonComperator);

                            if (chartDetails.getViewBys().get(0).equalsIgnoreCase("Year") || chartDetails.getViewBys().get(0).replaceAll("\\W", "").trim().toLowerCase().equalsIgnoreCase("qtryear") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("month") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("week") || (chartDetails.getViewBys().get(0).toString().trim().toLowerCase().contains("time") && !chartDetails.getViewBys().get(0).toString().replaceAll("\\W", "").trim().toLowerCase().equalsIgnoreCase("timeslot"))) {
                                DateComperator dateComperator = new DateComperator(chartDetails.getViewBys().get(0).toString(), "asc", timeDetailsGO);
                                Collections.sort(list, dateComperator);
                            } else {
                                JsonComperator jsonComperator1 = new JsonComperator(chartDetails.getViewBys().get(0), "desc", sortColumns);
                                Collections.sort(list, jsonComperator1);
                            }
                        } else {
                            JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(measureIndex), "asc", "");
                            Collections.sort(list, jsonComperator);

                            if (chartDetails.getViewBys().get(0).equalsIgnoreCase("Year") || chartDetails.getViewBys().get(0).replaceAll("\\W", "").trim().toLowerCase().equalsIgnoreCase("qtryear") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("month") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("week") || (chartDetails.getViewBys().get(0).toString().trim().toLowerCase().contains("time") && !chartDetails.getViewBys().get(0).toString().replaceAll("\\W", "").trim().toLowerCase().equalsIgnoreCase("timeslot"))) {
                                DateComperator dateComperator = new DateComperator(chartDetails.getViewBys().get(0).toString(), "desc", timeDetailsGO);
                                Collections.sort(list, dateComperator);
                            } else {
                                JsonComperator jsonComperator1 = new JsonComperator(chartDetails.getViewBys().get(0), "asc", sortColumns);
                                Collections.sort(list, jsonComperator1);
                            }
                        }
//}else {
//       if(sortType !=null && "D".equals(sortType)){
//JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(0), "desc","");
//Collections.sort(list, jsonComperator);
//}else {
//         JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(0), "asc","");
//Collections.sort(list, jsonComperator);
//       }
//}
                    } else {
                        if (sortType != null && "D".equals(sortType)) {
                            JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(measureIndex), "desc", "");
                            Collections.sort(list, jsonComperator);

                            if (chartDetails.getViewBys().get(0).equalsIgnoreCase("Year") || chartDetails.getViewBys().get(0).replaceAll("\\W", "").trim().toLowerCase().equalsIgnoreCase("qtryear") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("month") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("week") || (chartDetails.getViewBys().get(0).toString().trim().toLowerCase().contains("time") && !chartDetails.getViewBys().get(0).toString().replaceAll("\\W", "").trim().toLowerCase().equalsIgnoreCase("timeslot"))) {
                                DateComperator dateComperator = new DateComperator(chartDetails.getViewBys().get(0).toString(), "desc", timeDetailsGO);
                                Collections.sort(list, dateComperator);
                            }
                        } else {
                            JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(measureIndex), "asc", "");
                            Collections.sort(list, jsonComperator);

                            if (chartDetails.getViewBys().get(0).equalsIgnoreCase("Year") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("month") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("week") || (chartDetails.getViewBys().get(0).toString().trim().toLowerCase().contains("time") && !chartDetails.getViewBys().get(0).toString().replaceAll("\\W", "").trim().toLowerCase().equalsIgnoreCase("timeslot"))) {
                                DateComperator dateComperator = new DateComperator(chartDetails.getViewBys().get(0).toString(), "asc", timeDetailsGO);
                                Collections.sort(list, dateComperator);
                            }
                        }
                    }
                } else {

                    if (sortColumns != null && "A".equals(sortColumns)) {
                        if (sortType != null && "A".equals(sortType)) {

                            if (chartDetails.getViewBys().get(0).equalsIgnoreCase("Year") || chartDetails.getViewBys().get(0).replaceAll("\\W", "").trim().toLowerCase().equalsIgnoreCase("qtryear") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("month") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("week") || (chartDetails.getViewBys().get(0).toString().trim().toLowerCase().contains("time") && !chartDetails.getViewBys().get(0).toString().replaceAll("\\W", "").trim().toLowerCase().equalsIgnoreCase("timeslot"))) {
                                DateComperator dateComperator = new DateComperator(chartDetails.getViewBys().get(0).toString(), "asc", timeDetailsGO);
                                Collections.sort(list, dateComperator);
                            } else {
                                JsonComperator jsonComperator = new JsonComperator(chartDetails.getViewBys().get(0), "desc", sortColumns);
                                Collections.sort(list, jsonComperator);
                            }

                        } else {
                            if (chartDetails.getViewBys().get(0).equalsIgnoreCase("Year") || chartDetails.getViewBys().get(0).replaceAll("\\W", "").trim().toLowerCase().equalsIgnoreCase("qtryear") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("month") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("week") || (chartDetails.getViewBys().get(0).toString().trim().toLowerCase().contains("time") && !chartDetails.getViewBys().get(0).toString().replaceAll("\\W", "").trim().toLowerCase().equalsIgnoreCase("timeslot"))) {
                                DateComperator dateComperator = new DateComperator(chartDetails.getViewBys().get(0).toString(), "desc", timeDetailsGO);
                                Collections.sort(list, dateComperator);
                            } else {
                                JsonComperator jsonComperator = new JsonComperator(chartDetails.getViewBys().get(0), "asc", sortColumns);
                                Collections.sort(list, jsonComperator);
                            }
                        }

                    } else {

                        if (sortType != null && "D".equals(sortType)) {
                            if (chartDetails.getChartType().equalsIgnoreCase("multi-view-bubble") || chartDetails.getChartType().equalsIgnoreCase("Multi-View-Tree") || chartDetails.getChartType().equalsIgnoreCase("grouped-bar") || chartDetails.getChartType().equalsIgnoreCase("GroupedHorizontal-Bar") || chartDetails.getChartType().equalsIgnoreCase("Scatter-Bubble") || chartDetails.getChartType().equalsIgnoreCase("Split-Bubble") || chartDetails.getChartType().equalsIgnoreCase("world-map") || chartDetails.getChartType().equalsIgnoreCase("Trend-Combo") || chartDetails.getChartType().equalsIgnoreCase("Grouped-Table") || chartDetails.getChartType().equalsIgnoreCase("grouped-line") || chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar") || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar") || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-BarLine") || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar%") || chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar%") || chartDetails.getChartType().equalsIgnoreCase("Grouped-MultiMeasureBar")) {
                                JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(measureIndex), "desc", sortColumns);
                                Collections.sort(list, jsonComperator);
                            } else {
                                if (chartDetails.getViewBys().get(0).equalsIgnoreCase("Year") || chartDetails.getViewBys().get(0).equalsIgnoreCase("Qtr Year") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("month") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("week") || (chartDetails.getViewBys().get(0).toString().trim().toLowerCase().contains("time") && !chartDetails.getViewBys().get(0).toString().replaceAll("\\W", "").trim().toLowerCase().equalsIgnoreCase("timeslot"))) {
                                    DateComperator dateComperator = new DateComperator(chartDetails.getViewBys().get(0).toString(), "asc", timeDetailsGO);
                                    Collections.sort(list, dateComperator);
                                } else {
                                    JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(measureIndex), "desc", sortColumns);
                                    Collections.sort(list, jsonComperator);
                                }
                            }
                        } else {

                            if (chartDetails.getViewBys().get(0).equalsIgnoreCase("Year") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("month") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("week") || (chartDetails.getViewBys().get(0).toString().trim().toLowerCase().contains("time") && !chartDetails.getViewBys().get(0).toString().replaceAll("\\W", "").trim().toLowerCase().equalsIgnoreCase("timeslot"))) {
                                DateComperator dateComperator = new DateComperator(chartDetails.getViewBys().get(0).toString(), "desc", timeDetailsGO);
                                Collections.sort(list, dateComperator);
                            } else {
                                JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(measureIndex), "asc", "");
                                Collections.sort(list, jsonComperator);
                            }
                        }

                    }
                }
                // grouped bar

                if (chartDetails.getChartType().equalsIgnoreCase("multi-view-bubble") || chartDetails.getChartType().equalsIgnoreCase("Multi-View-Tree") || chartDetails.getChartType().equalsIgnoreCase("grouped-bar") || chartDetails.getChartType().equalsIgnoreCase("GroupedHorizontal-Bar") || chartDetails.getChartType().equalsIgnoreCase("Scatter-Bubble") || chartDetails.getChartType().equalsIgnoreCase("Split-Bubble") || chartDetails.getChartType().equalsIgnoreCase("world-map") || chartDetails.getChartType().equalsIgnoreCase("Trend-Combo") || chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar") || chartDetails.getChartType().equalsIgnoreCase("grouped-table") || chartDetails.getChartType().equalsIgnoreCase("Grouped-Map")|| chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar") || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-BarLine") || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar%") || chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar%") || chartDetails.getChartType().equalsIgnoreCase("Grouped-MultiMeasureBar")) {
                    if (reportMeta.getChartData().get(chart).getInnerSortBasis() != null) {
                        innersortColumns = reportMeta.getChartData().get(chart).getInnerSortBasis().substring(0, 1);
                    } else {
                        innersortColumns = "V";
                    }
                    if (reportMeta.getChartData().get(chart).getInnerSorting() != null) {
                        innersortType = reportMeta.getChartData().get(chart).getInnerSorting().substring(0, 1);
                    } else {
                        innersortType = "D";
                    }

                    if (innersortColumns != null && "A".equals(innersortColumns)) {
                        if (innersortType != null && "D".equals(innersortType)) {
//JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(0), "desc","");
//Collections.sort(list, jsonComperator);

                            if (chartDetails.getViewBys().get(1).equalsIgnoreCase("Year") || chartDetails.getViewBys().get(1).toString().toLowerCase().contains("month") || chartDetails.getViewBys().get(1).toString().toLowerCase().contains("week") || chartDetails.getViewBys().get(1).toString().trim().toLowerCase().contains("time")) {
                                DateComperator dateComperator = new DateComperator(chartDetails.getViewBys().get(1).toString(), "asc", timeDetailsGO);
                                Collections.sort(list, dateComperator);
                            } else {
                                JsonComperator jsonComperator1 = new JsonComperator(chartDetails.getViewBys().get(1), "desc", innersortColumns);
                                Collections.sort(list, jsonComperator1);
                            }
                        } else {
//        JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(0), "asc","");
//Collections.sort(list, jsonComperator);

                            if (chartDetails.getViewBys().get(1).equalsIgnoreCase("Year") || chartDetails.getViewBys().get(1).toString().toLowerCase().contains("month") || chartDetails.getViewBys().get(1).toString().toLowerCase().contains("week") || chartDetails.getViewBys().get(1).toString().trim().toLowerCase().contains("time")) {
                                DateComperator dateComperator = new DateComperator(chartDetails.getViewBys().get(1).toString(), "desc", timeDetailsGO);
                                Collections.sort(list, dateComperator);
                            } else {
                                JsonComperator jsonComperator1 = new JsonComperator(chartDetails.getViewBys().get(1), "asc", innersortColumns);
                                Collections.sort(list, jsonComperator1);
                            }
                        }
//}else {
//       if(sortType !=null && "D".equals(sortType)){
//JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(0), "desc","");
//Collections.sort(list, jsonComperator);
//}else {
//         JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(0), "asc","");
//Collections.sort(list, jsonComperator);
//       }
//}
                    } else {
                        if (innersortType != null && "D".equals(innersortType)) {
//JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(0), "desc","");
//Collections.sort(list, jsonComperator);

                            if (chartDetails.getViewBys().get(0).equalsIgnoreCase("Year") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("month") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("week") || chartDetails.getViewBys().get(0).toString().trim().toLowerCase().contains("time")) {
                                DateComperator dateComperator = new DateComperator(chartDetails.getViewBys().get(0).toString(), "desc", timeDetailsGO);
                                Collections.sort(list, dateComperator);
                            }
                        } else {
                            JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(measureIndex), "asc", "");
                            Collections.sort(list, jsonComperator);

                            if (chartDetails.getViewBys().get(0).equalsIgnoreCase("Year") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("month") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("week") || chartDetails.getViewBys().get(0).toString().trim().toLowerCase().contains("time")) {
                                DateComperator dateComperator = new DateComperator(chartDetails.getViewBys().get(0).toString(), "asc", timeDetailsGO);
                                Collections.sort(list, dateComperator);
                            }
                        }
                    }
                } //end of group sorting

                if (chartDetails.getChartType().equalsIgnoreCase("Combo-Analysis") && !chartDetails.isComboData() && actionGo != null ) {

                    setComboData(reportId, chart, list, fileLocation, userid, chartDetails.getComboType());
                }
                List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
                if (chartDetails.getOthersL() == null) {
                    chartDetails.setOthersL("N");
                }
                if (chartDetails.getOthers() == null) {
                    chartDetails.setOthers("N");
                }
                if (reportMeta.getVisualChartType() != null && reportMeta.getChartType() != null && (reportMeta.getChartType().equalsIgnoreCase("bubble-dashboard") || reportMeta.getChartType().equalsIgnoreCase("multi-view-bubble") || reportMeta.getChartType().equalsIgnoreCase("Pie-Dashboard") || reportMeta.getChartType().equalsIgnoreCase("world-map") || reportMeta.getChartType().equalsIgnoreCase("Trend-Combo") || reportMeta.getChartType().equalsIgnoreCase("Split-Graph"))) {
                    for (int f = 0; f < (list.size() < 300 ? list.size() : 300); f++) {
                        dataList.add(list.get(f));
                    }
                }
                if (reportMeta.getVisualChartType() != null && request.getParameter("currType") != null && reportMeta.getVisualChartType().get(request.getParameter("currType")).equalsIgnoreCase("Overlay")) {
                    if (chart.equalsIgnoreCase("chart1")) {
                        cutOfmap.put(chart, cutOfmap1);
                    } else {
                        cutOfmap.put(chart, list);
                    }
                } else {
                    if (reportMeta.getChartType() != null && (reportMeta.getChartType().equalsIgnoreCase("bubble-dashboard") || reportMeta.getChartType().equalsIgnoreCase("multi-view-bubble") || reportMeta.getChartType().equalsIgnoreCase("Split-Graph") || reportMeta.getChartType().equalsIgnoreCase("Pie-Dashboard"))) {
                        dataMap.put(chart, dataList);
                        dataMapgblsave.put(regid, gson.toJson(dataMap));
                    } else {
                        if (chartDetails.getRecords() != null && !chartDetails.getRecords().equalsIgnoreCase("") && !chartDetails.getRecords().equalsIgnoreCase("All") && chartDetails.getOthers() != null 
                                && chartDetails.getOthersL() != null && !chartDetails.getOthersL().equalsIgnoreCase("Y") 
                                && !chartDetails.getOthers().equalsIgnoreCase("Y") 
                                && !chartDetails.getChartType().equalsIgnoreCase("grouped-bar") 
                                && !chartDetails.getChartType().equalsIgnoreCase("GroupedHorizontal-Bar") 
                                && !chartDetails.getChartType().equalsIgnoreCase("Split-Bubble") 
                                && !chartDetails.getChartType().equalsIgnoreCase("world-map") 
                                && !(chartDetails.getChartType().equalsIgnoreCase("Trend-Combo") 
                                && chartDetails.getTrendViewMeasures() != null 
                                && (chartDetails.getTrendViewMeasures() != null 
                                && !(chartDetails.getTrendViewMeasures().equalsIgnoreCase("Measures")))) 
                                && !chartDetails.getChartType().equalsIgnoreCase("grouped-table") 
                                && !chartDetails.getChartType().equalsIgnoreCase("Grouped-Map") 
                                && !chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar") 
                                && !chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-BarLine") 
                                && !chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar%") 
                                && !chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar%") 
                                && !chartDetails.getChartType().equalsIgnoreCase("grouped-line") 
                                && !chartDetails.getChartType().equalsIgnoreCase("multi-view-bubble") 
                                && !chartDetails.getChartType().equalsIgnoreCase("Multi-View-Tree") 
                                && !chartDetails.getChartType().equalsIgnoreCase("Grouped-MultiMeasureBar")) {
                            try {
                                for (int f = Integer.parseInt(chartDetails.getStartRecords()); f < (list.size() < Integer.parseInt(chartDetails.getEndRecords()) ? list.size() : Integer.parseInt(chartDetails.getEndRecords())); f++) {
                                    dataList.add(list.get(f));
                                }
                            } catch (Exception e) {

                                for (int f = 0; f < (list.size() < Integer.parseInt(chartDetails.getRecords()) ? list.size() : Integer.parseInt(chartDetails.getRecords())); f++) {
                                    dataList.add(list.get(f));
                                }
//                                logger.error("Exception:", e);
                            }
                        } else {
                            if (chartDetails.getChartType().equalsIgnoreCase("multi-view-tree")) {
                                list = new ArrayList<>();
                                list.add(mapData);
                                dataList = list;
                            } else {

                                dataList = list;
                            }
                        }
                        dataMap.put(chart, dataList);
                        dataMapgblsave.put(regid, gson.toJson(dataMap));
                    }
                }
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
//            }
            if (chartFlag != null && chartFlag.equalsIgnoreCase("true") && divId != null && !divId.equalsIgnoreCase("")) {
                break;
            }
        }
        for (String chart : charts) {
            Map<String, List<String>> localDrills = reportMeta.getChartData().get(chart).getLocalDrills();
            Set<Map.Entry<String, List<String>>> localDrillEntrySet = null;
            if (localDrills != null) {
                localDrillEntrySet = localDrills.entrySet();
            }
            if (localDrillEntrySet != null && !localDrillEntrySet.isEmpty()) {
                String dependentViewBy = "";
                List<String> dependentViewByList = reportMeta.getChartData().get(chart).getViewBys();
                dependentViewBy = dependentViewByList.get(0);
                Map<String, List<String>> anchorChart = reportMeta.getChartData().get(charts.get(0)).getAnchorChart();
                String driverChart = "";
                if (anchorChart != null) {
                    List<Map<String, String>> orderedData = new ArrayList<Map<String, String>>();
                    Set<Map.Entry<String, List<String>>> anchorChartEntrySet = anchorChart.entrySet();
                    Iterator<Map.Entry<String, List<String>>> iterator = anchorChartEntrySet.iterator();
                    if (iterator.hasNext()) {
                        driverChart = iterator.next().getKey();
                    }
                    List<String> viewBys = reportMeta.getChartData().get(driverChart).getViewBys();
                    String driverViewBy = viewBys.get(0);
                    if (!driverViewBy.equalsIgnoreCase(dependentViewBy)) {
                        continue;
                    }
                    iterator = anchorChartEntrySet.iterator();
                    Set<Map.Entry<String, List<Map<String, String>>>> dataMapEntries = dataMap.entrySet();
                    Iterator<Map.Entry<String, List<Map<String, String>>>> dataMapIterator = dataMapEntries.iterator();
                    List<Map<String, String>> driverChartData = null;
                    List<Map<String, String>> dependentChartData = null;
                    while (dataMapIterator.hasNext()) {
                        Map.Entry<String, List<Map<String, String>>> dataMapData = dataMapIterator.next();
                        if (dataMapData.getKey().equalsIgnoreCase(driverChart)) {
                            driverChartData = dataMapData.getValue();
                        }
                        if (dataMapData.getKey().equalsIgnoreCase(chart)) {
                            dependentChartData = dataMapData.getValue();
                        }
                    }
                    if (driverChartData != null && dependentChartData != null) {
                        for (int i = 0; i < driverChartData.size(); i++) {
                            Map<String, String> dataSet = driverChartData.get(i);
                            String driverValue = dataSet.get(driverViewBy);
                            for (int j = 0; j < dependentChartData.size(); j++) {
                                Map<String, String> dependentMap = dependentChartData.get(j);
                                String dependentValue = dependentMap.get(dependentViewBy);
                                if (driverValue.equalsIgnoreCase(dependentValue)) {
                                    orderedData.add(dependentMap);
                                    break;
                                }
                            }
                        }
                    }
                    dataMap.put(chart, orderedData);
                }
            }
        }
       

//            container = Container.getContainerFromSession(request, reportId);
            if(!kpiChartId.isEmpty()){
                if(container.getDbrdData()==null){
                container.setDbrdData(dataMap);
            }
                for(byte z=0;z<kpiChartId.size();z++){
                if(container.getDbrdData().get(kpiChartId.get(z)) ==null)
                container.setDbrdData(dataMap);
                }
            }
            String driver = request.getParameter("driver");
            String drillType = "";
            if (request.getParameter("driver") != null && !request.getParameter("driver").equalsIgnoreCase("")) {
                if (reportMeta.getChartData().get(request.getParameter("driver")) != null) {
                    drillType = reportMeta.getChartData().get(request.getParameter("driver")).getDrillType();
                }
            }
            if ((request.getParameter("driver") != null && !request.getParameter("driver").equalsIgnoreCase("")) || (reportMeta.getVisualChartType() != null && request.getParameter("currType") != null && reportMeta.getVisualChartType().get(request.getParameter("currType")).equalsIgnoreCase("multi-kpi") && request.getParameter("type").equalsIgnoreCase("advance"))) {
                Map<String, List<Map<String, String>>> newDataMap = new HashMap<String, List<Map<String, String>>>();
                newDataMap = container.getDbrdData();
                Set<String> chartsIds = dataMap.keySet();
//        Set<String> drillChartsIds = container.getDbrdData().keySet();
                if (request.getParameter("driver") != null && drillType != null && drillType.equalsIgnoreCase("within")) {
                    for (String chartsId : chartsIds) {
//           if(chartsIds.size()== drillChartsIds.size())
                        if (chartsId.equalsIgnoreCase(driver)) {
                            newDataMap.put(chartsId, dataMap.get(chartsId));
                        } else {
                            newDataMap.put(chartsId, container.getDbrdData().get(chartsId));
                        }

                    }
                }
      
        else if(drillType!=null && drillType.equalsIgnoreCase("multi")){
            for(String chartsId : chartsIds){
                System.out.println("........................driverList...................."+driverList+".........................."+dataMap+"......................"+reportMeta.getChartData().get(chartsId));
         if(!driverList.contains(chartsId)){
         newDataMap.put(chartsId,dataMap.get(chartsId));
         }
         else if(!reportMeta.getChartData().get(chartsId).getChartType().equalsIgnoreCase("world-map")){
              
          newDataMap.put(chartsId,container.getDbrdData().get(chartsId));
              
         }else{
             System.out.println(".............................World Map............................ "+dataMap.get(chartsId));
         newDataMap.put(chartsId,dataMap.get(chartsId));
         }
         }

        }
                else {
                    DashboardChartData chartDetails = reportMeta.getChartData().get("chart1");
                    if (reportMeta.getVisualChartType() != null && request.getParameter("currType") != null && reportMeta.getVisualChartType().get(request.getParameter("currType")).equalsIgnoreCase("multi-kpi") && request.getParameter("type").equalsIgnoreCase("advance") || (chartDetails != null && chartDetails.getChartType().equalsIgnoreCase("quickAnalysis"))) {
                        for (String chartsId : chartsIds) {
                            if (driverList != null && !driverList.contains(chartsId)) {
                                newDataMap.put(chartsId, dataMap.get(chartsId));
                            } else {
                                newDataMap.put(chartsId, container.getDbrdData().get(chartsId));
                            }
                        }
                    } else {
                        for (String chartsId : chartsIds) {
                            String dontDrill = "";
                            if (reportMeta.getChartData().get(chartsId).getExcludeFromDrill() != null) {
                                dontDrill = reportMeta.getChartData().get(chartsId).getExcludeFromDrill().toString();
                            }
                            if (!chartsId.equalsIgnoreCase(driver) && !dontDrill.equalsIgnoreCase("Y")) {
                                newDataMap.put(chartsId, dataMap.get(chartsId));
                            }
                        }
                        if (reportMeta.getChartData().get(driver).getChartType().equalsIgnoreCase("Trend-Table-Combo") ||reportMeta.getChartData().get(driver).getChartType().equalsIgnoreCase("world-map") ||reportMeta.getChartData().get(driver).getChartType().equalsIgnoreCase("Trend-Analysis-Chart")) {
                            newDataMap.put(driver, dataMap.get(driver));
                        } else {
                            newDataMap.put(driver, container.getDbrdData().get(driver));
                        }
                    }
                }
                container.setDbrdData(newDataMap);
                returnObjectMap.put("data", gson.toJson(newDataMap));
                if (drillType != null && drillType.equalsIgnoreCase("single")) {
                    returnObjectMap.put("meta", gson.toJson(reportMeta));
                }

                for (String chart : charts) {
                    if ((reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Column-Pie") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Column-Donut") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Stacked-KPI") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Bullet-Horizontal") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Table") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Scatter-Analysis") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Transpose-Table") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Expression-Table") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("KPI-Table") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Standard-KPI") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Standard-KPI1") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Radial-Chart") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("LiquidFilled-KPI") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Dial-Gauge") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("KPIDash") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Emoji-Chart")) && ((reportMeta.getDrills() != null) || reportMeta.getFilterMap() != null)) {
                        Type metaType = new TypeToken<ManagementTemplateMeta>() {
                        }.getType();
                        returnObjectMap.put("meta", gson.toJson(reportMeta));
                        break;
                    }
                }
                if (request.getParameter("type").equalsIgnoreCase("advance")) {
                    return gson.toJson(newDataMap);
                } else {
                    return gson.toJson(returnObjectMap);
                }
//     return gson.toJson(returnObjectMap);
            } else if (request.getParameter("isEdit") != null && request.getParameter("isEdit").equalsIgnoreCase("Y")) {
                Map<String, List<Map<String, String>>> newDataMap = new HashMap<String, List<Map<String, String>>>();
                newDataMap = container.getDbrdData();
                driver = request.getParameter("editId");
                Set<String> chartsIds = dataMap.keySet();
                for (String chartsId : chartsIds) {
                    if (chartsId.equalsIgnoreCase(driver)) {
                        newDataMap.put(chartsId, dataMap.get(chartsId));
                    }
                }
                newDataMap.put(driver, container.getDbrdData().get(driver));
                container.setDbrdData(newDataMap);
//       returnObjectMap.put("data", gson.toJson(newDataMap));
                for (String chart : charts) {
                    if ((reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Column-Pie") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Column-Donut") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Stacked-KPI") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Combo-Analysis") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Expression-Table") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("KPI-Table") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Standard-KPI")|| reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Standard-KPI1") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Radial-Chart") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("LiquidFilled-KPI") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Dial-Gauge") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("KPIDash") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Emoji-Chart")) && ((reportMeta.getDrills() != null) || reportMeta.getFilterMap() != null)) {
                        returnObjectMap.put("data", gson.toJson(newDataMap));
                        returnObjectMap.put("meta", gson.toJson(reportMeta));

                        break;
                    } else {
                        returnObjectMap.put("data", gson.toJson(newDataMap));
                        returnObjectMap.put("meta", gson.toJson(reportMeta));
                        break;
                    }
                }
                return gson.toJson(returnObjectMap);
            } else {
//                if (reportMeta.getVisualChartType() != null && request.getParameter("currType") != null && reportMeta.getVisualChartType().get(request.getParameter("currType")).equalsIgnoreCase("Overlay")) {
//                    Type dataType = new TypeToken<OverlayData>() {
//                    }.getType();
//                    OverlayData cutData = gson.fromJson(gson.toJson(cutOfmap), dataType);
//                    container.setOverlayData(cutData);
//                    return gson.toJson(cutOfmap);
//                } else {
                    if (kpiChartId != null && !kpiChartId.isEmpty()) {
                        for (int j = 0; j < kpiChartId.size(); j++) {
                            dataMap.put(kpiChartId.get(j), container.getDbrdData().get(kpiChartId.get(j)));
                        }
                    } else if (viewByChange != null && viewByChange.equalsIgnoreCase("viewByChange") && !reportMeta.getChartType().equalsIgnoreCase("Pie-Dashboard")) {
                        for (String chart : charts) {
                            if (!viewChartId.equalsIgnoreCase(chart)) {
                                dataMap.put(chart, container.getDbrdData().get(chart));
                            }
                        }
                    }
                    container.setDbrdData(dataMap);

                    for (String chart : charts) {
                        if ((reportMeta.getFilterMap() != null && !(reportMeta.getFilterMap()).isEmpty() && !(reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Table") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Scatter-Analysis")))) {
                            returnObjectMap.put("meta", gson.toJson(reportMeta));
                            returnObjectMap.put("data", gson.toJson(dataMap));
//       break;
                        } else if ((reportMeta.getChartData() != null && reportMeta.getChartData().get(chart) != null && reportMeta.getChartData().get(chart).getFilters() != null && !(reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Table") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Scatter-Analysis")))) {
                            returnObjectMap.put("meta", gson.toJson(reportMeta));
                            returnObjectMap.put("data", gson.toJson(dataMap));
//        break;
                        } else if ((reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Table") || reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Scatter-Analysis")) && !(reportMeta.getFilterMap() != null && !(reportMeta.getFilterMap()).isEmpty()) && !(reportMeta.getChartData() != null && reportMeta.getChartData().get(chart) != null && reportMeta.getChartData().get(chart).getFilters() != null)) {
                            returnObjectMap.put("meta", gson.toJson(reportMeta));
                            returnObjectMap.put("data", gson.toJson(dataMap));
                            break;
                        } // else if(reportMeta.getFilterMap()!=null){
                        // returnObjectMap.put("data", gson.toJson(dataMap));
                        // }
                        else {
                            returnObjectMap.put("meta", gson.toJson(reportMeta));
                            returnObjectMap.put("data", gson.toJson(dataMap));
//        break;
                        }
                    }
                    if (request.getParameter("type").equalsIgnoreCase("advance")) {
                        return gson.toJson(dataMap);
                    } else {
                        return gson.toJson(returnObjectMap);
                    }
//                }
            }
    }

  public ManagementTemplateMeta setTemplateForm(HttpServletRequest request) {
        Gson gson = new Gson();
        Type tarType = new TypeToken<List<String>>() {
        }.getType();
        List<String> list;
        if (request.getParameter("reportName") != null) {
            reportMeta.setReportName(request.getParameter("reportName"));
        }
        if (request.getParameter("advanceChartType") != null) {
            reportMeta.setAdvanceChartType(request.getParameter("advanceChartType"));
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
if (request.getParameter("notfilters") != null) {

                Type tarType1 = new TypeToken<Map<String, List<String>>>() {
                }.getType();
                Map<String, List<String>> map = gson.fromJson(request.getParameter("notfilters"), tarType1);
                reportMeta.setNotfilters(map);
           

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
            logger.error("Exception:", npe);
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

        if (request.getParameter("chartData") != null) {
            Type tarType1 = new TypeToken<Map<String, DashboardChartData>>() {
            }.getType();
            Map<String, DashboardChartData> chartData = gson.fromJson(request.getParameter("chartData"), tarType1);
//            Map<String, DashboardChartData> chartData = gson.fromJson("", tarType1); //Comment
            reportMeta.setChartData(chartData);
        }
        if (request.getParameter("advanceChartData") != null) {
            Type tarType1 = new TypeToken<Map<String,Map<String,DashboardChartMeta>>>() {
            }.getType();
            Map<String,Map<String,DashboardChartMeta>> advanceChartData = gson.fromJson(request.getParameter("advanceChartData"), tarType1);
//            Map<String, DashboardChartData> chartData = gson.fromJson("", tarType1); //Comment
            reportMeta.setAdvanceChartData(advanceChartData);
        }
        if (request.getParameter("templateMeta") != null) {
            Type tarType1 = new TypeToken<Map<String, TemplateMeta>>() {
            }.getType();
            Map<String, TemplateMeta> templateMeta = gson.fromJson(request.getParameter("templateMeta"), tarType1);
//            Map<String, DashboardChartData> chartData = gson.fromJson("", tarType1); //Comment
            reportMeta.setTemplateMeta(templateMeta);
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
     
            reportMeta.setVisualChartType(map);
        }

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
public Map<String, Map<String, List<String>>> processHirarchiJson(Map<String, List<String>> rawStr) {
        List<String> keyList = new ArrayList<String>();
        keyList.addAll(rawStr.keySet());
        Map<String, Map<String, List<String>>> map = new HashMap<String, Map<String, List<String>>>();
        for (int j = 0; j < rawStr.size(); j++) {
            String curKey = keyList.get(0);
            keyList.remove(0);
            if (!(curKey.equalsIgnoreCase(""))) {
//            keyList.remove(0);
                List<String> groupBys;
                try {
                    groupBys = new ArrayList<String>(Arrays.asList(curKey.split(",")));
                    if (!groupBys.get(0).equalsIgnoreCase("")) {
                        if (!map.containsKey(groupBys.get(0))) {
                            if (groupBys.size() == 1) {
                                map.put(groupBys.get(0), map.get(curKey));
                            } else {
                                map.put(groupBys.get(0), new HashMap<String, List<String>>());
                            }
                        }
                        Map hirarMap = (Map) map.get(groupBys.get(0));

                        for (int i = 1; i < groupBys.size(); i++) {
                            try {
                                if (i == (groupBys.size() - 1)) {
                                    hirarMap.put(groupBys.get(i), rawStr.get(curKey));
                                } else {
                                    if (!hirarMap.containsKey(groupBys.get(i))) {
                                        hirarMap.put(groupBys.get(i), new HashMap());
                                    }
                                    hirarMap = (Map) hirarMap.get(groupBys.get(i));
                                }
                            } catch (Exception e) {
                                hirarMap.put(groupBys.get(i), new HashMap());
                                if (hirarMap != null) {
                                    hirarMap.put(groupBys.get(i), hirarMap.get(groupBys.get(i)));
                                }
                                logger.error("Exception:", e);
                            }
                        }

                    }

                } catch (Exception e) {
                    logger.error("Exception:", e);
                }
            }
        }
        return map;
    }

 public List<Map<String, String>> generateCrossTabData(PbReturnObject pbretObj, List<String> paramNames, List<String> paramIds, int size, HttpSession session, ArrayList<String> timeDetails) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//pbretObj.nonViewByMapNew
        for (int m = 0; m < pbretObj.rowCount; m++) {
            Map<String, String> map = new LinkedHashMap<String, String>();
                for(int j =0; j<pbretObj.CrossTabfinalOrder.size(); ){
            for (int k = 0; k < paramNames.size(); k++) {
                    
                if (paramNames.get(k).trim().equalsIgnoreCase("TIME")) {
                    if (timeDetails != null && timeDetails.get(3).equalsIgnoreCase("Month")) {
                        map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, "MONTH_YEAR"));
                    } else if (timeDetails != null && timeDetails.get(3).equalsIgnoreCase("Qtr")) {
                        map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, "QTR_YEAR"));
                    } else if (timeDetails != null && timeDetails.get(3).equalsIgnoreCase("Year")) {
                        map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, "YEAR_NAME"));
                    } else {

                        map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, paramIds.get(k).trim()));
}
                } else {
                    if (k >= size && (pbretObj.getFieldValueStringBasedOnViewSeq(m, pbretObj.CrossTabfinalOrder.get(j).toString()).contains("e") || pbretObj.getFieldValueStringBasedOnViewSeq(m, pbretObj.CrossTabfinalOrder.get(j).toString()).contains("E"))) {
                        map.put(paramNames.get(k), String.valueOf(Double.valueOf(pbretObj.CrossTabfinalOrder.get(j).toString()).longValue()));
                    } else {
                        //Added by Ram
//                        if (filterLookupOriginalToNew != null && filterLookupOriginalToNew.containsKey(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k)))) {
//                            map.put(paramNames.get(k), filterLookupOriginalToNew.get(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k))).toString());
//                        } else //Endded by Ram
//                        {
                            if(k==0){
                                map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k)));
                            }else{
                                
                            map.put(paramNames.get(k)+"_"+pbretObj.CrossTabfinalOrder.get(j).toString(), pbretObj.getFieldValueStringBasedOnViewSeq(m, pbretObj.CrossTabfinalOrder.get(j).toString()));
                            j++;
                            }
//                        }
                    }
                }
                }
            }

            list.add(map);
        }

        return list;
    }
 
 public List<Map<String, String>> generateChartJson(PbReturnObject pbretObj, List<String> paramNames, List<String> paramIds, int size, HttpSession session, ArrayList<String> timeDetails) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        for (int m = 0; m < pbretObj.rowCount; m++) {
            Map<String, String> map = new LinkedHashMap<String, String>();
            for (int k = 0; k < paramNames.size(); k++) {

                if (paramNames.get(k).trim().equalsIgnoreCase("TIME")) {
                    if (timeDetails != null && timeDetails.get(3).equalsIgnoreCase("Month")) {
                        map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, "MONTH_YEAR"));
                    } else if (timeDetails != null && timeDetails.get(3).equalsIgnoreCase("Qtr")) {
                        map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, "QTR_YEAR"));
                    } else if (timeDetails != null && timeDetails.get(3).equalsIgnoreCase("Year")) {
                        map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, "YEAR_NAME"));
                    } else {

                        map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, paramIds.get(k).trim()));
                    }
                } else {
                    if (k >= size && (pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k)).contains("e") || pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k)).contains("E"))) {
                        map.put(paramNames.get(k), String.valueOf(Double.valueOf(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k))).longValue()));
                    } else {
                        //Added by Ram
                        if (filterLookupOriginalToNew != null && filterLookupOriginalToNew.containsKey(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k)))) {
                            map.put(paramNames.get(k), filterLookupOriginalToNew.get(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k))).toString());
                        } else //Endded by Ram
                        {
                            map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k)));
                        }
                    }
                }

            }

            list.add(map);
        }

        return list;
    }
 
 public void setTimeTypeForMeasure(DashboardChartData chartDetails, ProgenAOQuery objQuery) {
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
            }else if (measureId.contains("_WTD")) {
                measureType.add("WTD");
            }else if (measureId.contains("_PWTD")) {
                measureType.add("PWTD");
            }else if (measureId.contains("_PYWTD")) {
                measureType.add("PYWTD");
            }  
            else if (measureId.contains("_MOMPer")) {
                measureType.add("MOMPer");
            } else if (measureId.contains("_MOYMPer")) {
                measureType.add("MOYMPer");
            } else if (measureId.contains("_QOQPer")) {
                measureType.add("QOQPer");
            } else if (measureId.contains("_QOYQPer")) {
                measureType.add("QOYQPer");
            } else if (measureId.contains("_YOYPer")) {
                measureType.add("YOYPer");
            }else if (measureId.contains("_WOWPer")) {
                measureType.add("WOWPer");
            }
            else if (measureId.contains("_WOYWPer")) {
                measureType.add("WOYWPer");
            }
            else if (measureId.contains("_MOM")) {
                measureType.add("MOM");
            } else if (measureId.contains("_MOYM")) {
                measureType.add("MOYM");
            } else if (measureId.contains("_QOQ")) {
                measureType.add("QOQ");
            } else if (measureId.contains("_QOYQ")) {
                measureType.add("QOYQ");
            } else if (measureId.contains("_YOY")) {
                measureType.add("YOY");
            }else if (measureId.contains("_WOW")) {
                measureType.add("WOW");
            }else if (measureId.contains("_WOYW")) {
                measureType.add("WOYW");
            } 
            else {
                measureType.add("PERIOD");
            }
        }
        objQuery.setTimeDetailsArray(measureType);
    }

    public String getTimeBasedMeasure(ProgenTimeDefinition timeDefObj, String measName, String measId, ArrayList<String> timeDetailsGO) {
        Map<String, String> timeMap = timeDefObj.getTimeDefinition();
        ReportObjectQuery objQuery = new ReportObjectQuery();
        if (measName.contains("(PMTD")) {
            return measName.replaceAll("PMTD", timeMap.get("PMTD"));
        } else if (measName.contains("(PYMTD")) {
            return measName.replaceAll("PYMTD", timeMap.get("PYMTD"));
        } else if (measName.contains("MTD")) {
            return measName.replaceAll("MTD", timeMap.get("MTD"));
        } else if (measName.contains("(PQTD")) {
            return measName.replaceAll("PQTD", timeMap.get("PQTD"));
        } else if (measName.contains("(PYQTD")) {
            return measName.replaceAll("PYQTD", timeMap.get("PYQTD"));
        } else if (measName.contains("(QTD")) {
            return measName.replaceAll("QTD", timeMap.get("QTD"));
        }else if (measName.contains("(PWTD")) {
            return measName.replaceAll("PWTD", timeMap.get("PWTD"));
        } else if (measName.contains("(PYWTD")) {
            return measName.replaceAll("PYWTD", timeMap.get("PYWTD"));
        } else if (measName.contains("(WTD")) {
            return measName.replaceAll("WTD", timeMap.get("WTD"));
        }  
        else if (measName.contains("(PYTD")) {
            return measName.replaceAll("PYTD", timeMap.get("PYTD"));
        } else if (measName.contains("(YTD")) {
            return measName.replaceAll("YTD", timeMap.get("YTD"));
        } else if (measName.contains("PRIOR")) {
            return measName.replaceAll("PRIOR", timeMap.get("PRIOR"));
        } else if (measId.contains("PRIOR")) {
            try {
                String priorValue = objQuery.getPriorValue(timeDetailsGO, removeComparisonType(measId));

                return measName.substring(0, measName.indexOf("(") + 1) + priorValue + ")";
            } catch (Exception e) {
//                logger.error("Exception:", e);
                return measName;
            }
        } else if (measId.contains("PYTD")) {
            try {

                return measName.substring(0, measName.indexOf("(") + 1) + timeMap.get("PYTD") + ")";
            } catch (Exception e) {
//                logger.error("Exception:", e);
                return measName;
            }
        } else if (measId.contains("YTD")) {
            try {

                return measName.substring(0, measName.indexOf("(") + 1) + timeMap.get("YTD") + ")";
            } catch (Exception e) {
//                logger.error("Exception:", e);
                return measName;
            }
        }
        return measName;
    }
    
    public String removeComparisonType(String qryWhereId) {
        String[] split = qryWhereId.split(",");
//        qryWhereId="";
        StringBuilder qryWhereId1 = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            try {
                split[i] = split[i].substring(0, split[i].indexOf("_"));
                if (i == split.length - 1) {
//                qryWhereId += split[i];
                    qryWhereId1.append(split[i]);
                } else {
//                    qryWhereId += split[i]+" ,";
                    qryWhereId1.append(split[i]).append(" ,");
                }
            } catch (Exception e) {
                if (i == split.length - 1) {
//                qryWhereId += split[i];
                    qryWhereId1.append(split[i]);
                } else {
//                    qryWhereId += split[i]+" ,";
                    qryWhereId1.append(split[i]).append(" ,");
                }
//                logger.error("Exception:", e);
            }
        }

        return qryWhereId1.toString();
    }
    
     public List addGrandTotalAggType(DashboardChartData chartDetails) {
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
      return grandTotalAggType;
    }
     
       public PbReturnObject getReturnObjectBasedOnChart(String reportid, String chartid, String query, String fileLocation, String userid) {
        PbReturnObject pbretObj = null;
        Map<String, String> allqueries = new LinkedHashMap<>();
        try {
            File datafile = new File(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + chartid + "/" + chartid + ".json");
            if (datafile.exists()) {
                FileReadWrite readWrite = new FileReadWrite();
                String metaString = readWrite.loadJSON(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + chartid + "/" + chartid + ".json");
                Gson gson = new Gson();
                Type tarType = new TypeToken<Map<String, String>>() {
                }.getType();
                allqueries = gson.fromJson(metaString, tarType);
                for (int i = 1; i <= allqueries.size(); i++) {
                    if (allqueries.get("QUERY" + i).equalsIgnoreCase(query)) {
                        datafile = new File(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + chartid + "/" + "QUERY" + i + ".txt");
                        FileInputStream fis = new FileInputStream(datafile);
                        ObjectInputStream in = new ObjectInputStream(fis);
                        pbretObj = (PbReturnObject) in.readObject();

                        break;
                    }
                }

            }
        } catch (JsonParseException ex) {
            logger.error("Exception:", ex);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } catch (ClassNotFoundException ex) {
            logger.error("Exception:", ex);
        }
        return pbretObj;
    }
       
       public void getSortMeasureValue(DashboardChartData chartDetails,List<Map<String, String>> list){
        Map<String,List<String>> valueMap=new HashMap<>();
        try{
        for(int i=0; i<chartDetails.getMeassureIds().size(); i++){
        List<String> minMaxList= new ArrayList<>();    
        JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(i), "desc", "");
        Collections.sort(list, jsonComperator);
        minMaxList.add(list.get(0).get(chartDetails.getMeassures().get(i)));
        minMaxList.add(list.get(list.size()-1).get(chartDetails.getMeassures().get(i)));
        valueMap.put(chartDetails.getMeassureIds().get(i),minMaxList );
        }
        chartDetails.setDataSliderMinMaxValue(valueMap);
        }catch(Exception ex){
        logger.error("Exception:", ex);
        }
    }
       
       
        private void setGlobalValuetoReportMeta(HttpSession session, ManagementTemplateMeta reportMeta, DashboardChartData chartDetails) {
        Map<String, ArrayList> elementCurrency;
        elementCurrency = new HashMap<String, ArrayList>();
        Map<String, String> numberFormat = new HashMap<String, String>();
        String currencySymbol = "";
        String elementId = "";
        if (session != null) {
            elementCurrency = (Map<String, ArrayList>) session.getAttribute("elementCurrency");
            numberFormat = (Map<String, String>) session.getAttribute("CurrencyShownAsMap");
        }
        Map<String, String> currencyMap = new HashMap<String, String>();
        for (int k = 0; k < chartDetails.getMeassureIds().size(); k++) {
            elementId = removeComparisonType(chartDetails.getMeassureIds().get(k));
            if (elementCurrency != null && !elementCurrency.isEmpty()) {

                if (elementCurrency.get(elementId) != null && !elementCurrency.get(elementId).isEmpty() && elementCurrency.get(elementId).get(1) != null && !elementCurrency.get(elementId).get(1).toString().equalsIgnoreCase("")) {
                    currencySymbol = elementCurrency.get(elementId).get(1).toString();
//                    break;
                } else {
                    currencySymbol = "";
                }
            }
            currencyMap.put(elementId, currencySymbol);
        }
        chartDetails.setCurrencySymbol(currencyMap);
        reportMeta.setCurrencyType(currencySymbol);

        // For Number Format from gbl
        if (numberFormat != null && !numberFormat.isEmpty()) {
            reportMeta.setNumberFormatMap(numberFormat);
        }

    }
        
         public void setComboData(String reportId, String chartId, List<Map<String, String>> list, String fileLocation, String userId, String comboType) {
        FileReadWrite fileReadWrite = new FileReadWrite();
//     File file = new File(fileLocation + "/"+ "Combo_Data/"+userId+File.separator+reportId+File.separator+chartId+File.separator+chartId+".json");
        Gson gson = new Gson();
        File f = new File(fileLocation + "/" + "Combo_Data/" + userId + File.separator + reportId + File.separator + chartId);
        String path = f.getAbsolutePath();
        File datafile = new File(fileLocation + "/" + "Combo_Data/" + userId + File.separator + reportId + File.separator + chartId + File.separator + chartId + "First.json");
        File f1 = new File(path);
        try {

            f1.mkdirs();
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        if (comboType != null) {
            if (!datafile.exists()) {
                if (comboType.equalsIgnoreCase("drill1")) {
                    try {
                        datafile.createNewFile();
                    } catch (IOException ex) {
                        logger.error("Exception:", ex);
                    }
//            allqueries.put("QUERY1",query);
                    fileReadWrite.writeToFile(fileLocation + "/" + "Combo_Data/" + userId + File.separator + reportId + File.separator + chartId + File.separator + chartId + "First.json", gson.toJson(list));
                }
            } else {
                if (comboType.equalsIgnoreCase("drill2")) {
                    datafile = new File(fileLocation + "/" + "Combo_Data/" + userId + File.separator + reportId + File.separator + chartId + File.separator + chartId + "Second.json");
                    try {
                        datafile.createNewFile();
                    } catch (IOException ex) {
                        logger.error("Exception:", ex);
                    }

                    fileReadWrite.writeToFile(fileLocation + "/" + "Combo_Data/" + userId + File.separator + reportId + File.separator + chartId + File.separator + chartId + "Second.json", gson.toJson(list));
//fileR
                }
            }
        }
    }
         
         public boolean setReturnObjectBasedOnChart(String reportid, String chartid, String query, PbReturnObject pbretObj, String fileLocation, String userid) throws FileNotFoundException {
        FileReadWrite fileReadWrite = new FileReadWrite();
        File file;
        File datafile;
        Gson gson = new Gson();
        Map<String, String> allqueries = new LinkedHashMap<>();
        file = new File(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + chartid);
        String path = file.getAbsolutePath();
        String fName = path + File.separator + chartid + ".json";
        File f1 = new File(path);
        try {
            File file1 = new File(fName);
            f1.mkdirs();
            datafile = new File(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + chartid + "/" + chartid + ".json");
            if (!datafile.exists()) {
                try {
                    datafile.createNewFile();
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }
                allqueries.put("QUERY1", query);
                fileReadWrite.writeToFile(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + chartid + "/" + chartid + ".json", gson.toJson(allqueries));
//fileReadWrite.writeToFile(fileLocation+"/AO_Data/"+userid+"/"+reportid+"/"+chartid +"/"+"QUERY"+(allqueries.size())+ ".json", gson.toJson(list));
                file1 = new File(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + chartid + "/" + "QUERY" + (allqueries.size()) + ".txt");
                file1.createNewFile();
                FileOutputStream fout = new FileOutputStream(file1);
                ObjectOutputStream out = new ObjectOutputStream(fout);
                out.writeObject(pbretObj);
                out.flush();
            } else {
                FileReadWrite readWrite = new FileReadWrite();
                String metaString = readWrite.loadJSON(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + chartid + "/" + chartid + ".json");
                Type tarType = new TypeToken<Map<String, String>>() {
                }.getType();
                allqueries = gson.fromJson(metaString, tarType);
                if (allqueries.size() < 50) {
                    allqueries.put("QUERY" + (allqueries.size() + 1), query);
                    fileReadWrite.writeToFile(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + chartid + "/" + chartid + ".json", gson.toJson(allqueries));
//fileReadWrite.writeToFile(fileLocation+"/AO_Data/"+userid+"/"+reportid+"/"+chartid +"/"+"QUERY"+(allqueries.size())+ ".json", gson.toJson(list));
                    file1 = new File(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + chartid + "/" + "QUERY" + (allqueries.size()) + ".txt");
                    file1.createNewFile();
                    FileOutputStream fout = new FileOutputStream(file1);
                    ObjectOutputStream out = new ObjectOutputStream(fout);
                    out.writeObject(pbretObj);
                    out.flush();
                }
            }
        } catch (JsonParseException e) {
            logger.error("Exception:", e);
            return false;
        } catch (IOException e) {
            logger.error("Exception:", e);
            return false;
        }
        return true;

    }
         
         
         public ArrayList getTimeClauseForGoAgg(HashMap<String, List> drillmapFromSet, HashMap<String, List> drillmapFromSetAgg, ArrayList timeDetails, ManagementTemplateMeta reportMeta, DashboardChartData chartDetails) {
        String end_date = "";
        String timeViewBy = "";
        String drillValue = "";
        ArrayList clause = new ArrayList();
        ReportObjectQuery objQuery = new ReportObjectQuery();

        if (!drillmapFromSetAgg.keySet().isEmpty()) {
            for (String keyAgg : drillmapFromSetAgg.keySet()) {
                timeViewBy = getTimeDimensionView(keyAgg, reportMeta);
                drillValue = drillmapFromSetAgg.get(keyAgg).get(0).toString();
            }
        }

        String measureId = removeComparisonType(chartDetails.getMeassureIds().get(0));
        if (timeViewBy.equalsIgnoreCase("Month")) {

            end_date = objQuery.getMonthDrill(drillValue.replace(" ", "").trim(), reportMeta, measureId);
        } else if (timeViewBy.equalsIgnoreCase("Qtr")) {
            end_date = objQuery.getQtrDrill(drillValue.replace(" ", "").trim(), reportMeta, measureId);
        } else if (timeViewBy.equalsIgnoreCase("Year")) {
            end_date = objQuery.getYearDrill(drillValue.replace(" ", "").trim(), reportMeta, measureId);
        }
        clause = getClauseForDrill(timeDetails, measureId, end_date);
//        }d
        return clause;
    }
         
         public String getTimeDimensionView(String keyAggId, ManagementTemplateMeta reportMeta) {
        List<String> viewIds = reportMeta.getViewbysIds();
        List<String> viewName = reportMeta.getViewbys();
        List<String> newViewIds = new ArrayList<String>();
        List<String> newViewName = new ArrayList<String>();
        for (int k = 0; k < viewIds.size(); k++) {
            newViewIds.add(k, viewIds.get(k));
            newViewName.add(k, viewName.get(k));
        }
        newViewIds.add("QTR_YEAR");
        newViewIds.add("MONTH_YEAR");
        newViewIds.add("YEAR_NAME");
        newViewName.add("QTR_YEAR");
        newViewName.add("MONTH_YEAR");
        newViewName.add("YEAR_NAME");
        String keyAgg = "";
        if (newViewIds.indexOf(keyAggId) != -1) {
            keyAgg = newViewName.get(newViewIds.indexOf(keyAggId));
        }

        if (keyAgg.trim().equalsIgnoreCase("Month")
                || keyAgg.replace(" ", "").trim().equalsIgnoreCase("Month-Year")
                || keyAgg.replace(" ", "").trim().equalsIgnoreCase("Month_Year")
                || keyAgg.replace(" ", "").trim().equalsIgnoreCase("MonthYear")) {
            keyAgg = "Month";
        } else if (keyAgg.trim().equalsIgnoreCase("Qtr")
                || keyAgg.replace(" ", "").trim().equalsIgnoreCase("Qtr-Year")
                || keyAgg.replace(" ", "").trim().equalsIgnoreCase("Qtr_Year")
                || keyAgg.replace(" ", "").trim().equalsIgnoreCase("QtrYear")) {
            keyAgg = "Qtr";
        } else if (keyAgg.trim().equalsIgnoreCase("Year") || keyAgg.replace(" ", "").trim().equalsIgnoreCase("Year_Name")) {
            keyAgg = "Year";
        }
        return keyAgg;
    }
         
         public ArrayList getClauseForDrill(ArrayList timeDetails, String element_Id, String end_date) {
        ArrayList tempTimeDetails = new ArrayList();
//        if (collect != null) {
//            ArrayList timeDetails = collect.timeDetailsArray;
            for (int i = 0; i < timeDetails.size(); i++) {
                tempTimeDetails.add(i, timeDetails.get(i));
            }
            tempTimeDetails.remove(2);
            tempTimeDetails.add(2, end_date);

//        }
        System.out.println("**************************************************************timeDEtails**************:" + tempTimeDetails.toString());
        return tempTimeDetails;
    }

    public void saveTemplate(HttpServletRequest request) {
        String templateName = request.getParameter("templateName");
        String templateId = request.getParameter("templateId");
        String templateDesc = request.getParameter("templateDesc");
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        Date currentDate = new Date();
        String currentDateStr = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(currentDate);
        String query = "insert into prg_template_details values('" + templateName + "','" + templateDesc + "','" + userId + "','" + currentDateStr + "','" + currentDateStr + "','" + templateId + "')";
        PbDb pbdb = new PbDb();
//        try {
////            pbdb.execInsert(query);
//        } catch (SQLException ex) {
//            logger.error(ex);
//        } catch (Exception ex) {
//            logger.error(ex);
//        }
        TemplatePageGenerator pageGenerator=new TemplatePageGenerator();
        templateMeta = pageGenerator.setTemplateInfoForm(request);
        File file1;
        String fileLocation = pageGenerator.getFilePath(session);
        file1 = new File(fileLocation + "/template/" + userId + "/" + templateId + "/");
        String path = file1.getAbsolutePath();
        File f = new File(path);
        f.mkdirs();
        File file = new File(fileLocation + "/template/" + userId + "/" + templateId + "/" + templateId + "_meta.json");
        if (!file.exists()) {
            try {

                file.createNewFile();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
}
        }
        Gson gson=new Gson();
        fileReadWrite.writeToFile(fileLocation + "/template/" + userId + "/" + templateId + "/" + templateId + "_meta.json",gson.toJson(templateMeta));
        System.out.println("");
    }
}
