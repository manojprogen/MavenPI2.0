package com.progen.report;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.progen.contypes.GetConnectionType;
import java.sql.Statement;
import com.progen.reportview.db.OverlayData;
import java.io.File;
import com.progen.db.ProgenDataSet;
import com.progen.report.query.ProgenAOQuery;
import com.progen.report.template.TemplateMeta;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.db.JsonGenerator;
import com.progen.reportview.db.OverlayData;
import java.io.*;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map.Entry;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.*;
import prg.util.PbDisplayLabel;
import prg.util.ProgenTimeDefinition;
import utils.db.ProgenConnection;

public class ReportManagementDAO {

    public static Logger logger = Logger.getLogger(ReportManagementDAO.class);
//    Container container = new Container();
    XtendReportMeta reportMeta = new XtendReportMeta();
    FileReadWrite fileReadWrite = new FileReadWrite();
    HashMap filterLookupOriginalToNew = null;

    //for open saved charts
    public String getChartDetails(String chartName, String workDir) {

        //later
//        String chartPath = container.privatePath + workDir + container.chartMetaPath + chartName + ".json";
//        reportMeta = readChartJson(chartPath);
        Gson gson = new Gson();
//        String reportData = fileReadWrite.loadJSON(container.privatePath + workDir + "/" + container.chartDataPath + chartName + ".json");

//        model.addAttribute("reportMeta", gson.toJson(reportMeta));
//        model.addAttribute("reportData", reportData);
        //for dashboard
//        if(reportMeta != null && reportMeta.getIsDashboard()){
//        Map<String, Map<String, String>> mapData;
//        Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
//        }.getType();
//        mapData = gson.fromJson(reportData, tarType1);
//        container.setDbData(mapData);
//        }
//        reportMeta.setAggregations(aggregations);
//        reportMeta.setUpdateFlag(updateFlag);
        Map<String, Object> responseMap = new HashMap<String, Object>();
//        responseMap.put("reportMeta", reportMeta);
//        responseMap.put("reportData", reportData);
        return gson.toJson(responseMap);
    }

    public String chartRequestHandler(HttpServletRequest request, String bizzRoleName) {
        Gson gson = new Gson();
        Type tarType = new TypeToken<XtendReportMeta>() {
        }.getType();
        boolean isCreateReport = true;
//        if (oldReportMeta != null) {
//            reportMeta = gson.fromJson(oldReportMeta, tarType);
//            isCreateReport = false;
//        }
//        if (reportMeta == null) {
        reportMeta = new XtendReportMeta();
//        }
        setReportForm(request);
        List<String> driverList = null;
        boolean flag = true;
        String reportName = request.getParameter("reportName");
        String reportId = request.getParameter("reportId");
        if (request.getParameter("bin") != null) {
            flag = false;
            driverList = gson.fromJson(request.getParameter("bin"), new TypeToken<List<String>>() {
            }.getType());
        }
//        if (reportMeta.getIsMultipleCsv() && driverList != null) {
//
//            Map<String, DashboardChartData> chartData = reportMeta.getChartData();
//            Map<String, DashboardChartData> newChartData = new LinkedHashMap<String, DashboardChartData>();
//            Set<String> charts = chartData.keySet();
//            newChartData.put(driverList.get(0), chartData.get(driverList.get(0)));
//            charts.remove(driverList.get(0));
//            for (String chart : charts) {
//                newChartData.put(chart, chartData.get(chart));
//            }
//            reportMeta.getChartData().clear();
//            reportMeta.setChartData(newChartData);
//        }
        Map<String, Map<String, HashMap<String, List<String>>>> dirJsonContent = null;//readDirJson();
        ReportDataGenerator dataGenerator = new ReportDataGenerator(reportMeta, dirJsonContent);
        String reportData = dataGenerator.generateReportData(isCreateReport, reportName, reportId, bizzRoleName);

        File file;
        file = new File("/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId);
        String path = file.getAbsolutePath();
        String fName = path + File.separator + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ".json";
        File f = new File(path);
        File file1 = new File(fName);
        f.mkdirs();

        try {
            file1.createNewFile();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

//        fileReadWrite.writeToFile("C:/usr/local/cache/Sales/"+reportName+"_"+reportId+"/"+reportName+"_"+reportId+".json", reportData);
        fileReadWrite.writeToFile(fName, reportData);
        Map<String, Map<String, String>> mapData;
        Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
        }.getType();

//            if (!isCreateReport) {
//                mapData = gson.fromJson(oldReportData, tarType1);
//            } else {
        mapData = null;
//            }
        Map<String, Map<String, String>> newMapData = gson.fromJson(reportData, tarType1);
//        model.addAttribute("reportMeta", gson.toJson(reportMeta));
        if ((request.getParameter("isDashlet") != null && request.getParameter("isDashlet").equalsIgnoreCase("true")) || (reportMeta != null && reportMeta.getIsDashboard())) {

//            newMapData.keySet();
//            if(flag && mapData==null){
            if (mapData == null) {
                mapData = new HashMap<String, Map<String, String>>();
            }
            for (String key : newMapData.keySet()) {
                if ((driverList != null && driverList.indexOf(key) == -1) || flag) {
                    mapData.put(key, newMapData.get(key));
                }
            }
            reportData = gson.toJson(mapData);

        }
        Container container = Container.getContainerFromSession(request, reportId);
        container.setDbData(newMapData);
        return reportData;
    }

    public String singleChartRequestHandler(HttpServletRequest request, String bizzRoleName) {
        Gson gson = new Gson();
        Type tarType = new TypeToken<XtendReportMeta>() {
        }.getType();
        boolean isCreateReport = true;

        reportMeta = new XtendReportMeta();

        setReportForm(request);
        Container container = Container.getContainerFromSession(request, request.getParameter("reportId"));
        container.setReportMeta(reportMeta);
        List<String> driverList = null;
        boolean flag = true;
        String reportName = request.getParameter("reportName");
        String reportId = request.getParameter("reportId");
        if (request.getParameter("bin") != null) {
            flag = false;
            driverList = gson.fromJson(request.getParameter("bin"), new TypeToken<List<String>>() {
            }.getType());
        }
//        if (reportMeta.getIsMultipleCsv() && driverList != null) {
//
//            Map<String, DashboardChartData> chartData = reportMeta.getChartData();
//            Map<String, DashboardChartData> newChartData = new LinkedHashMap<String, DashboardChartData>();
//            Set<String> charts = chartData.keySet();
//            newChartData.put(driverList.get(0), chartData.get(driverList.get(0)));
//            charts.remove(driverList.get(0));
//            for (String chart : charts) {
//                newChartData.put(chart, chartData.get(chart));
//            }
//            reportMeta.getChartData().clear();
//            reportMeta.setChartData(newChartData);
//        }
        Map<String, Map<String, HashMap<String, List<String>>>> dirJsonContent = null;//readDirJson();
        ReportDataGenerator dataGenerator = new ReportDataGenerator(reportMeta, dirJsonContent);
        String reportData = dataGenerator.generateReportData(isCreateReport, reportName, reportId, bizzRoleName);

        File file;
        file = new File("/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId);

        String path = file.getAbsolutePath();
//        String fName = path+File.separator+reportName.replaceAll("\\W", "").trim()+"_"+reportId+".json";
//        File f = new File(path);
//        File file1 = new File(fName);
//        f.mkdirs();
//
//     try {
//                file1.createNewFile();
//     } catch (IOException ex) {
//                logger.error("Exception:",ex);
//     }

//        fileReadWrite.writeToFile("C:/usr/local/cache/Sales/"+reportName+"_"+reportId+"/"+reportName+"_"+reportId+".json", reportData);
//        fileReadWrite.writeToFile(fName,reportData);
//        model.addAttribute("reportMeta", gson.toJson(reportMeta));
        Map<String, Map<String, String>> mapData;
//            if (!isCreateReport) {
//                mapData = gson.fromJson(oldReportData, tarType1);
//            } else {
        mapData = null;
//            }
        Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
        }.getType();
        Map<String, Map<String, String>> newMapData = gson.fromJson(reportData, tarType1);
        if ((request.getParameter("isDashlet") != null && request.getParameter("isDashlet").equalsIgnoreCase("true")) || (reportMeta != null && reportMeta.getIsDashboard())) {

//            newMapData.keySet();
//            if(flag && mapData==null){
            if (mapData == null) {
                mapData = new HashMap<String, Map<String, String>>();
            }
            for (String key : newMapData.keySet()) {
                if ((driverList != null && driverList.indexOf(key) == -1) || flag) {
                    mapData.put(key, newMapData.get(key));
                }
            }

            reportData = gson.toJson(mapData);
        }
        Map<String, Map<String, String>> mapData1 = container.getDbData();
        if (mapData1 == null) {
            mapData1 = new HashMap<String, Map<String, String>>();
        }
        for (String key : newMapData.keySet()) {
//                if ((driverList != null && driverList.indexOf(key) == -1) || flag) {
            mapData1.put(key, newMapData.get(key));
//                }
        }
        container.setDbData(mapData1);
        return reportData;
    }

    public String chartRequestHandlerDrills(HttpServletRequest request, String bizzRoleName) {
        Gson gson = new Gson();
        Type tarType = new TypeToken<XtendReportMeta>() {
        }.getType();
        boolean isCreateReport = true;
//        if (oldReportMeta != null) {
//            reportMeta = gson.fromJson(oldReportMeta, tarType);
//            isCreateReport = false;
//        }
//        if (reportMeta == null) {
        reportMeta = new XtendReportMeta();
//        }
        setReportForm(request);
        List<String> driverList = null;
        boolean flag = true;
        String reportName = request.getParameter("reportName");
        String reportId = request.getParameter("reportId");
        if (request.getParameter("bin") != null) {
            flag = false;
            driverList = gson.fromJson(request.getParameter("bin"), new TypeToken<List<String>>() {
            }.getType());
        }
//        if (reportMeta.getIsMultipleCsv() && driverList != null) {
//
//            Map<String, DashboardChartData> chartData = reportMeta.getChartData();
//            Map<String, DashboardChartData> newChartData = new LinkedHashMap<String, DashboardChartData>();
//            Set<String> charts = chartData.keySet();
//            newChartData.put(driverList.get(0), chartData.get(driverList.get(0)));
//            charts.remove(driverList.get(0));
//            for (String chart : charts) {
//                newChartData.put(chart, chartData.get(chart));
//            }
//            reportMeta.getChartData().clear();
//            reportMeta.setChartData(newChartData);
//        }
        Map<String, Map<String, HashMap<String, List<String>>>> dirJsonContent = new HashMap<String, Map<String, HashMap<String, List<String>>>>();//readDirJson();
        ReportDataGenerator dataGenerator = new ReportDataGenerator(reportMeta, dirJsonContent);
        String reportData = dataGenerator.generateReportData(isCreateReport, reportName, reportId, bizzRoleName);
//         File file = new File("C:/usr/local/cache/Sales/"+reportName+"_"+reportId+"/"+reportName+"_"+reportId+".json");
//         File file1 = new File("C:/usr/local/cache/Sales/"+reportName+"_"+reportId);
//         file1.mkdirs();
//       if (!file.exists()) {
//     try {
//         file.createNewFile();
//     } catch (IOException ex) {
//         logger.error("Exception:",ex);
//     }
//                }
//
//        fileReadWrite.writeToFile("C:/usr/local/cache/Sales/"+reportName+"_"+reportId+"/"+reportName+"_"+reportId+".json", reportData);
//        model.addAttribute("reportMeta", gson.toJson(reportMeta));
//        if ((request.getParameter("isDashlet") != null && request.getParameter("isDashlet").equalsIgnoreCase("true")) || (reportMeta != null && reportMeta.getIsDashboard())) {
        Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
        }.getType();
        Map<String, Map<String, String>> mapData;
//            if (!isCreateReport) {
//                mapData = gson.fromJson(oldReportData, tarType1);
//            } else {
        mapData = null;
//            }
        Map<String, Map<String, String>> newMapData = gson.fromJson(reportData, tarType1);
//            newMapData.keySet();
//            if(flag && mapData==null){
        if (mapData == null) {
            mapData = new HashMap<String, Map<String, String>>();
        }
//            for (String key : newMapData.keySet()) {
//                if ((driverList != null && driverList.indexOf(key) == -1) || flag) {
//                    mapData.put(key, newMapData.get(key));
//                }
//            }

        Container container = Container.getContainerFromSession(request, reportId);
        Map<String, Map<String, String>> DbData = container.getDbData();
        String driver = request.getParameter("driver");
        String drillType = request.getParameter("drilltype");
//            driver = "chart1";
        if (drillType != null && drillType.equalsIgnoreCase("within")) {
            for (String key : newMapData.keySet()) {
                if (key.equalsIgnoreCase(driver)) {
                    mapData.put(key, newMapData.get(key));
                } else {
                    mapData.put(key, DbData.get(key));
                }
            }
        } else {
            mapData.put(driver, DbData.get(driver));
            if (driver.equalsIgnoreCase("")) {
                mapData = new HashMap<String, Map<String, String>>();
            }
            for (String key : newMapData.keySet()) {
                if (!key.equalsIgnoreCase(driver)) {
                    mapData.put(key, newMapData.get(key));
                }
            }
        }
//            }
        reportData = gson.toJson(mapData);
//        }
        container.setDbData(mapData);
        return reportData;
    }

    public String chartRequestHandlerDrillfd(HttpServletRequest request) {
        Gson gson = new Gson();
        Type tarType = new TypeToken<XtendReportMeta>() {
        }.getType();
        boolean isCreateReport = true;
//        if(oldReportMeta!=null){
//        reportMeta = gson.fromJson(oldReportMeta, tarType);
        isCreateReport = false;
//        }
//        if (reportMeta == null) {
        reportMeta = new XtendReportMeta();
//        }
        setReportForm(request);
        List<String> driverList = null;
//        boolean flag = true;
//            if(request.getParameter("bin")!=null){
//        flag = false;
        driverList = gson.fromJson(request.getParameter("bin"), new TypeToken<List<String>>() {
        }.getType());
//        }

        if (reportMeta.getIsMultipleCsv() && driverList != null) {

            Map<String, DashboardChartData> chartData = reportMeta.getChartData();
            Map<String, DashboardChartData> newChartData = new LinkedHashMap<String, DashboardChartData>();
            Set<String> charts = chartData.keySet();
            newChartData.put(driverList.get(0), chartData.get(driverList.get(0)));
            charts.remove(driverList.get(0));
            for (String chart : charts) {
                newChartData.put(chart, chartData.get(chart));
            }
            reportMeta.getChartData().clear();
            reportMeta.setChartData(newChartData);
        }
        Map<String, Map<String, HashMap<String, List<String>>>> dirJsonContent = null; //readDirJson();
        ReportDataGenerator dataGenerator = new ReportDataGenerator(reportMeta, dirJsonContent);
        String reportData = dataGenerator.generateReportData(isCreateReport, "", "", "");
//        model.addAttribute("reportMeta", gson.toJson(reportMeta));
        if ((request.getParameter("isDashlet") != null && request.getParameter("isDashlet").equalsIgnoreCase("true")) || (reportMeta != null && reportMeta.getIsDashboard())) {
            Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
            }.getType();
            Map<String, Map<String, String>> mapData;
            if (!isCreateReport) {
//                mapData = gson.fromJson(oldReportData, tarType1);
            } else {
                mapData = null;
            }
            Map<String, Map<String, String>> newMapData = gson.fromJson(reportData, tarType1);
            mapData = new HashMap<String, Map<String, String>>();
            mapData = newMapData;
            Map<String, Map<String, String>> mapData1;
            mapData1 = new HashMap<String, Map<String, String>>();
//            mapData1 = container.getDbData();
//            if (request.getParameter("drilltype") != null && request.getParameter("drilltype").equalsIgnoreCase("singleDrill")) {
//                for (String driverList1 : driverList) {
//                    mapData.put(driverList1, container.getDbData().get(driverList1));
//                }
//            } else {
//                if(request.getParameter("drilltype") != null && !(request.getParameter("drilltype").equalsIgnoreCase("withinDrill"))){
//                for (String driverList1 : driverList) {
//                    mapData.put(driverList1, mapData1.get(driverList1));
//                }
//                }
//                container.setDbData(mapData);
//            }

            reportData = gson.toJson(mapData);
        }
        return reportData;
    }
    //sandeep

    public XtendReportMeta setOneviewForm(HttpServletRequest request) {
        Gson gson = new Gson();

        XtendReportMeta reportMeta1 = new XtendReportMeta();
        String refrehreporttime = (String) request.getAttribute("refrehreporttime");
        if (refrehreporttime != null && refrehreporttime.equalsIgnoreCase("true")) {
            Map map = new HashMap();
            map = (Map) request.getAttribute("mapdata");
            String data = (String) map.get("data");
            String meta = (String) map.get("meta");
// Gson gson = new Gson();
            Type metaType = new TypeToken<XtendReportMeta>() {
            }.getType();
            reportMeta1 = gson.fromJson(meta, metaType);
        } else {
            reportMeta1 = (XtendReportMeta) request.getAttribute("reportMeta");
        }
        Type tarType = new TypeToken<List<String>>() {
        }.getType();
        List<String> list;
        String chartname = (String) request.getAttribute("chartname");
        if (reportMeta1.getReportName() != null) {
            reportMeta.setReportName(reportMeta1.getReportName());
        }
        if (reportMeta1.getChartData().get(chartname).getViewBys() != null) {
            list = reportMeta1.getViewbys();
            reportMeta.setViewbys(list);
        }
        if (reportMeta1.getChartData().get(chartname).getViewIds() != null) {
            list = reportMeta1.getViewbysIds();
            reportMeta.setViewbysIds(list);
        }
        if (reportMeta1.getChartData().get(chartname).getMeassures() != null) {
            list = reportMeta1.getChartData().get(chartname).getMeassures();
            reportMeta.setMeasures(list);
        }
        if (reportMeta1.getChartData().get(chartname).getMeassureIds() != null) {
            list = reportMeta1.getChartData().get(chartname).getMeassureIds();
            reportMeta.setMeasuresIds(list);
        }
        if (reportMeta1.getChartData().get(chartname).getAggregation() != null) {
            list = reportMeta1.getChartData().get(chartname).getAggregation();
            reportMeta.setAggregations(list);
        }
        if (reportMeta1.getChartData().get(chartname).getChartType() != null) {
            reportMeta.setChartType(reportMeta1.getChartData().get(chartname).getChartType());
        }
        if (reportMeta1.getChartData() != null) {
            Type tarType1 = new TypeToken<Map<String, DashboardChartData>>() {
            }.getType();
            Map<String, DashboardChartData> chartData = reportMeta1.getChartData();
//            Map<String, DashboardChartData> chartData = gson.fromJson("", tarType1); //Comment
            reportMeta.setChartData(chartData);
        }
        if (reportMeta1.getTimeMap() != null) {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> map = reportMeta1.getTimeMap();
            reportMeta.setTimeMap(map);
        }
        return reportMeta;

    }
//end of sandeep code for set request values in chartdata

    public XtendReportMeta setReportForm(HttpServletRequest request) {
        Gson gson = new Gson();
        XtendReportMeta mapTest;
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

    public XtendReportMeta readChartJson(String chartPath) {
        Gson gson = new Gson();
        Type tarType = new TypeToken<XtendReportMeta>() {
        }.getType();
        XtendReportMeta reportData = gson.fromJson(fileReadWrite.loadJSON(chartPath), tarType);
        return reportData;
    }

    public String trendRequestHandler(HttpServletRequest request, String bizzRoleName) {
        Gson gson = new Gson();
        Type tarType = new TypeToken<XtendReportMeta>() {
        }.getType();
        boolean isCreateReport = true;
//        if (oldReportMeta != null) {
//            reportMeta = gson.fromJson(oldReportMeta, tarType);
//            isCreateReport = false;
//        }
//        if (reportMeta == null) {
        reportMeta = new XtendReportMeta();
//        }
        setReportForm(request);

        Container container = Container.getContainerFromSession(request, request.getParameter("reportId"));
        container.setTrendReportMeta(reportMeta);

        List<String> driverList = null;
        boolean flag = true;
        String reportName = request.getParameter("reportName");
        String reportId = request.getParameter("reportId");
        if (request.getParameter("bin") != null) {
            flag = false;
            driverList = gson.fromJson(request.getParameter("bin"), new TypeToken<List<String>>() {
            }.getType());
        }
//        if (reportMeta.getIsMultipleCsv() && driverList != null) {
//
//            Map<String, DashboardChartData> chartData = reportMeta.getChartData();
//            Map<String, DashboardChartData> newChartData = new LinkedHashMap<String, DashboardChartData>();
//            Set<String> charts = chartData.keySet();
//            newChartData.put(driverList.get(0), chartData.get(driverList.get(0)));
//            charts.remove(driverList.get(0));
//            for (String chart : charts) {
//                newChartData.put(chart, chartData.get(chart));
//            }
//            reportMeta.getChartData().clear();
//            reportMeta.setChartData(newChartData);
//        }
        Map<String, Map<String, HashMap<String, List<String>>>> dirJsonContent = null;//readDirJson();
        ReportDataGenerator dataGenerator = new ReportDataGenerator(reportMeta, dirJsonContent);
        String reportData = dataGenerator.generateTrendData(isCreateReport, reportName, reportId, bizzRoleName);

        File file;
        file = new File("/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId);

        String path = file.getAbsolutePath();
        String fName = path + File.separator + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_trend.json";
        File f = new File(path);
        File file1 = new File(fName);
        f.mkdirs();

        try {
            file1.createNewFile();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

//        fileReadWrite.writeToFile("C:/usr/local/cache/Sales/"+reportName+"_"+reportId+"/"+reportName+"_"+reportId+".json", reportData);
        fileReadWrite.writeToFile(fName, reportData);
        Map<String, Map<String, String>> mapData;
        Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
        }.getType();

//            if (!isCreateReport) {
//                mapData = gson.fromJson(oldReportData, tarType1);
//            } else {
        mapData = null;
//            }
        Map<String, Map<String, String>> newMapData = gson.fromJson(reportData, tarType1);
//        model.addAttribute("reportMeta", gson.toJson(reportMeta));
        if ((request.getParameter("isDashlet") != null && request.getParameter("isDashlet").equalsIgnoreCase("true")) || (reportMeta != null && reportMeta.getIsDashboard())) {

//            newMapData.keySet();
//            if(flag && mapData==null){
            if (mapData == null) {
                mapData = new HashMap<String, Map<String, String>>();
            }
            for (String key : newMapData.keySet()) {
                if ((driverList != null && driverList.indexOf(key) == -1) || flag) {
                    mapData.put(key, newMapData.get(key));
                }
            }
            reportData = gson.toJson(mapData);

        }
//       Container container = Container.getContainerFromSession(request, reportId);
        Map<String, Map<String, String>> mapData1 = container.getTrendData();
        if (mapData1 == null) {
            mapData1 = new HashMap<String, Map<String, String>>();
        }
        for (String key : newMapData.keySet()) {
//                if ((driverList != null && driverList.indexOf(key) == -1) || flag) {
            mapData1.put(key, newMapData.get(key));
//                }
        }
        container.setTrendData(mapData1);
        return reportData;
    }

    public String trendRequestHandlerDrills(HttpServletRequest request, String bizzRoleName) {
        Gson gson = new Gson();
        Type tarType = new TypeToken<XtendReportMeta>() {
        }.getType();
        boolean isCreateReport = true;
//        if (oldReportMeta != null) {
//            reportMeta = gson.fromJson(oldReportMeta, tarType);
//            isCreateReport = false;
//        }
//        if (reportMeta == null) {
        reportMeta = new XtendReportMeta();
//        }
        setReportForm(request);
        List<String> driverList = null;
        boolean flag = true;
        String reportName = request.getParameter("reportName");
        String reportId = request.getParameter("reportId");
        if (request.getParameter("bin") != null) {
            flag = false;
            driverList = gson.fromJson(request.getParameter("bin"), new TypeToken<List<String>>() {
            }.getType());
        }
//        if (reportMeta.getIsMultipleCsv() && driverList != null) {
//
//            Map<String, DashboardChartData> chartData = reportMeta.getChartData();
//            Map<String, DashboardChartData> newChartData = new LinkedHashMap<String, DashboardChartData>();
//            Set<String> charts = chartData.keySet();
//            newChartData.put(driverList.get(0), chartData.get(driverList.get(0)));
//            charts.remove(driverList.get(0));
//            for (String chart : charts) {
//                newChartData.put(chart, chartData.get(chart));
//            }
//            reportMeta.getChartData().clear();
//            reportMeta.setChartData(newChartData);
//        }
        Map<String, Map<String, HashMap<String, List<String>>>> dirJsonContent = null;//readDirJson();
        ReportDataGenerator dataGenerator = new ReportDataGenerator(reportMeta, dirJsonContent);
        String reportData = dataGenerator.generateTrendData(isCreateReport, reportName, reportId, bizzRoleName);
//         File file = new File("C:/usr/local/cache/Sales/"+reportName+"_"+reportId+"/"+reportName+"_"+reportId+".json");
//         File file1 = new File("C:/usr/local/cache/Sales/"+reportName+"_"+reportId);
//         file1.mkdirs();
//       if (!file.exists()) {
//     try {
//         file.createNewFile();
//     } catch (IOException ex) {
//         logger.error("Exception:",ex);
//     }
//                }
//
//        fileReadWrite.writeToFile("C:/usr/local/cache/Sales/"+reportName+"_"+reportId+"/"+reportName+"_"+reportId+".json", reportData);
//        model.addAttribute("reportMeta", gson.toJson(reportMeta));
//        if ((request.getParameter("isDashlet") != null && request.getParameter("isDashlet").equalsIgnoreCase("true")) || (reportMeta != null && reportMeta.getIsDashboard())) {
        Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
        }.getType();
        Map<String, Map<String, String>> mapData;
//            if (!isCreateReport) {
//                mapData = gson.fromJson(oldReportData, tarType1);
//            } else {
        mapData = null;
//            }
        Map<String, Map<String, String>> newMapData = gson.fromJson(reportData, tarType1);
//            newMapData.keySet();
//            if(flag && mapData==null){
        if (mapData == null) {
            mapData = new HashMap<String, Map<String, String>>();
        }
//            for (String key : newMapData.keySet()) {
//                if ((driverList != null && driverList.indexOf(key) == -1) || flag) {
//                    mapData.put(key, newMapData.get(key));
//                }
//            }

        Container container = Container.getContainerFromSession(request, reportId);
        Map<String, Map<String, String>> DbData = container.getTrendData();
        String driver = request.getParameter("driver");
        String drillType = request.getParameter("drilltype");
//            driver = "chart1";
        if ((drillType != null && drillType.equalsIgnoreCase("within")) || drillType.equalsIgnoreCase("")) {
            for (String key : newMapData.keySet()) {
                if (key.equalsIgnoreCase(driver)) {
                    mapData.put(key, newMapData.get(key));
                } else {
                    mapData.put(key, DbData.get(key));
                }
            }
        } else {
            if (driver.equalsIgnoreCase("")) {
                mapData = new HashMap<String, Map<String, String>>();
            }
            mapData.put(driver, DbData.get(driver));

            for (String key : newMapData.keySet()) {
                if (!key.equalsIgnoreCase(driver)) {
                    mapData.put(key, newMapData.get(key));
                }
            }
        }
//            }
        reportData = gson.toJson(mapData);
//        }
        container.setTrendData(mapData);
        return reportData;
    }

//    public List<ColumnMetaData> readMetaJson(String metaPath) {
//        Gson gson = new Gson();
//        Type tarType = new TypeToken<List<ColumnMetaData>>() {
//        }.getType();
//        List<ColumnMetaData> chartMeta = gson.fromJson(fileReadWrite.loadJSON(metaPath), tarType);
//        return chartMeta;
//    }
//    public String getAllFiltersData(String reportMeta1, String groupByList) {
//        Map<String, List<String>> allFilters = new HashMap<String, List<String>>();
//        Gson gson = new Gson();
//        Type type = new TypeToken<XtendReportMeta>() {
//        }.getType();
//        reportMeta = gson.fromJson(reportMeta1, type);
//        if (reportMeta != null) {
//            String dataPath = reportMeta.getDataPath();
//            type = new TypeToken<List<String>>() {
//            }.getType();
//            List<String> groupBys = gson.fromJson(groupByList, type);
//            String[] dirs = dataPath.split("/");
//
//            String workDir = dirs[0];
//            String csvFile = dirs[1].replace(".csv", "");
//
//            String filterPath;
//                int count=0;
//            for (String groupBy : groupBys) {
//                count++;
////                if(reportMeta.getIsMultipleCsv() && reportMeta.getIsDashboard()){
////                workDir = reportMeta.getChartData().get("chart"+count).getWorkDir();
////                csvFile = reportMeta.getChartData().get("chart"+count).getCsvName().replace(".csv","");
////                }
//                filterPath = container.privatePath + workDir + container.filterPath + groupBy.replace("/", "-+-") + "@@" + csvFile + ".json";
//                String cont = fileReadWrite.loadJSON(filterPath);
//                if (cont == "{}") {
//                    cont = "[]";
//                }
//                List<String> list = gson.fromJson(cont, type);
//                allFilters.put(groupBy.replace(" ", ""), list);
//            }
//
//        }
//        return gson.toJson(allFilters);
//    }
//    public void saveAsNewReport(HttpServletRequest request, String reportData) {
//        Gson gson = new Gson();
//        String dataPath = request.getParameter("dataPath");
//        String workDir = dataPath.split("/")[0];
//        String dataCSV = dataPath.split("/")[1];
//        String reportName = request.getParameter("reportName");
//        reportMeta = setReportForm(request);
//        Type tarType = new TypeToken<Map<String, Map<String, Map<String, String>>>>() {
//        }.getType();
//        String st = fileReadWrite.loadJSON(container.privatePath + container.reportMaster);
//        if (st == null || st.equalsIgnoreCase("")) {
//            st = "{}";
//        }
//        Map<String, Map<String, Map<String, String>>> reportMaster = gson.fromJson(st, tarType);
//        Map<String, Map<String, String>> reportListMap = reportMaster.get(workDir);
//        Map<String, String> reportMap = new HashMap<String, String>();
//        if (!reportMaster.containsKey(workDir)) {
//            reportListMap = new HashMap<String, Map<String, String>>();
//            reportMaster.put(workDir, reportListMap);
//        }
//        if (!reportListMap.containsKey(reportName)) {
//            reportMap.put("Created_on", new Date().toString());
//            reportMap.put("reportMetaPath", workDir + container.chartDataPath + reportName + ".json");
//            reportMap.put("dataCSV", dataCSV);
//            reportListMap.put(reportName, reportMap);
//        }
//        reportMeta.setReportName(reportName);
//        fileReadWrite.writeToFile(container.privatePath + container.reportMaster, gson.toJson(reportMaster));
//        fileReadWrite.writeToFile(container.privatePath + workDir + container.chartMetaPath + reportName + ".json", gson.toJson(reportMeta));
//        fileReadWrite.writeToFile(container.privatePath + workDir + container.chartDataPath + reportName + ".json", reportData);
//    }
//
//    public void overWriteReport(HttpServletRequest request, String reportData) {
//        Gson gson = new Gson();
//        String dataPath = request.getParameter("dataPath");
//        String workDir = dataPath.split("/")[0];
//        String reportName = request.getParameter("reportName");
//        reportMeta = setReportForm(request);
//        fileReadWrite.writeToFile(container.privatePath + workDir + container.chartMetaPath + reportName + ".json", gson.toJson(reportMeta));
//        fileReadWrite.writeToFile(container.privatePath + workDir + container.chartDataPath + reportName + ".json", reportData);
//    }
//    public String deleteReport(String workDir, String reportName) {
//        Gson gson = new Gson();
//        String res = "";
//        Type type = new TypeToken<Map<String, Map<String, Map<String, String>>>>() {
//        }.getRawType();
//        boolean status = fileReadWrite.deleteFile(container.privatePath + workDir + container.chartDataPath + reportName + ".json");
//        status = fileReadWrite.deleteFile(container.privatePath + workDir + container.chartMetaPath + reportName + ".json");
//        if (status) {
//            Map<String, Map<String, Map<String, String>>> reportMaster = gson.fromJson(fileReadWrite.loadJSON(container.privatePath + container.reportMaster), type);
//            reportMaster.remove(reportName);
//            res = "success";
//        }
//        return res;
//    }
//    public void changeChartSequence(String workDir, String sequence) {
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<String>>() {
//        }.getType();
//        List<String> newSequence = gson.fromJson(sequence, type);
//        type = new TypeToken<Map<String, Map<String, Map<String, String>>>>() {
//        }.getType();
//        Map<String, Map<String, Map<String, String>>> reportMaster = gson.fromJson(fileReadWrite.loadJSON(container.privatePath + container.reportMaster), type);
//        Map<String, Map<String, String>> tempMap = new HashMap<String, Map<String, String>>(reportMaster.get(workDir));
//        reportMaster.get(workDir).clear();
//        for (int i = (newSequence.size() - 1); i >= 0; i--) {
//            reportMaster.get(workDir).put(newSequence.get(i), tempMap.get(newSequence.get(i)));
//        }
//        fileReadWrite.writeJson(reportMaster, container.privatePath + container.reportMaster);
//    }
//    public String refreshDashChart(Model model, String workDir, String chartId, String reportName, String reportData) {
//        Gson gson = new Gson();
//        XtendReportMeta xtendReportMeta = gson.fromJson(fileReadWrite.loadJSON(container.privatePath + workDir + container.chartMetaPath + reportName + ".json"), new TypeToken<XtendReportMeta>() {
//        }.getType());
//
//        Map<String, DashboardChartData> chartData = xtendReportMeta.getChartData();
//        DashboardChartData chartDetails = chartData.get(chartId);
//
//        Type type = new TypeToken<Map<String, List<Map<String, String>>>>() {
//        }.getType();
//        Map<String, List<Map<String, String>>> oldReportData = gson.fromJson(fileReadWrite.loadJSON(container.privatePath + workDir + container.chartDataPath + reportName + ".json"), type);
//        Map<String, List<Map<String, String>>> currReportData = gson.fromJson(reportData, type);
//        currReportData.put(chartId, oldReportData.get(chartId));
//        model.addAttribute("reportData", gson.toJson(currReportData));
//
//        Map<String, Object> responseMap = new HashMap<String, Object>();
//        responseMap.put("chartDetails", chartDetails);
//        responseMap.put("reportData", currReportData);
//        return gson.toJson(responseMap);
//    }
//
//    public void saveDashlet(String workDir, String chartId, String reportName, String chartDetails, String reportData) {
//        Gson gson = new Gson();
//        DashboardChartData chartDetail = gson.fromJson(chartDetails, new TypeToken<DashboardChartData>() {
//        }.getType());
//        XtendReportMeta xtendReportMeta = gson.fromJson(fileReadWrite.loadJSON(container.privatePath + workDir + container.chartMetaPath + reportName + ".json"), new TypeToken<XtendReportMeta>() {
//        }.getType());
//        Map<String, DashboardChartData> chartData = xtendReportMeta.getChartData();
//        chartData.put(chartId, chartDetail);
//        fileReadWrite.writeToFile(container.privatePath + workDir + container.chartMetaPath + reportName + ".json", gson.toJson(xtendReportMeta));
//        fileReadWrite.writeToFile(container.privatePath + workDir + container.chartDataPath + reportName + ".json", reportData);
//    }
//
//    public Map<String, Map<String, HashMap<String, List<String>>>> readDirJson() {
//        Gson gson = new Gson();
//        Type tarType = new TypeToken<Map<String, Map<String, HashMap<String, List<String>>>>>() {
//        }.getType();
//        String dirJson = fileReadWrite.loadJSON(container.privatePath + container.dirJson);
//        dirJson = dirJson.replace("[]", "{}");
//        Map<String, Map<String, HashMap<String, List<String>>>> map = gson.fromJson(dirJson, tarType);
//        return map;
//    }
    public String advanceChartRequestHandler(HttpServletRequest request, String bizzRoleName) {
        Gson gson = new Gson();
        Type tarType = new TypeToken<XtendReportMeta>() {
        }.getType();
        Map visualData = new HashMap();
        boolean isCreateReport = true;
        reportMeta = new XtendReportMeta();
        Container container = Container.getContainerFromSession(request, request.getParameter("reportId"));
        setReportForm(request);

        container.setReportMeta(reportMeta);
        List<String> driverList = null;
        String reportData = "";
        boolean flag = true;
        String reportName = request.getParameter("reportName");
        String reportId = request.getParameter("reportId");
        if (request.getParameter("bin") != null) {
            flag = false;
            driverList = gson.fromJson(request.getParameter("bin"), new TypeToken<List<String>>() {
            }.getType());
        }
        Map<String, Map<String, HashMap<String, List<String>>>> dirJsonContent = null;//readDirJson();

        List<String> dataPath = new ArrayList<String>();
        dataPath.add("/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ".csv");
        String charttype = container.getReportMeta().getChartType();
        if (charttype.equalsIgnoreCase("India-map") || charttype.equalsIgnoreCase("India-map-with-chart")) {
            ReportDataGenerator dataGenerator = new ReportDataGenerator(reportMeta, dirJsonContent);
            reportData = dataGenerator.generateTrendData(isCreateReport, reportName, reportId, bizzRoleName);
            File file;
            file = new File("/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId);

            String path = file.getAbsolutePath();
            Map<String, Map<String, String>> mapData;
            mapData = null;
            Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
            }.getType();
            visualData = gson.fromJson(reportData, tarType1);
        } else {
            CsvToJson csvToJson = new CsvToJson(reportMeta, dataPath);
            reportData = csvToJson.csvToJson();

            File file;
            file = new File("/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId);

            String path = file.getAbsolutePath();
            Map<String, Map<String, String>> mapData;
            mapData = null;
            Type tarType1 = new TypeToken<Map<String, Map<String, List<Double>>>>() {
            }.getType();
            visualData = gson.fromJson(reportData, tarType1);
        }
//        if ((request.getParameter("isDashlet") != null && request.getParameter("isDashlet").equalsIgnoreCase("true")) || (reportMeta != null && reportMeta.getIsDashboard())) {
//            if (mapData == null) {
//                mapData = new HashMap<String, Map<String, String>>();
//            }
////            for (String key : newMapData.keySet()) {
////                if ((driverList != null && driverList.indexOf(key) == -1) || flag) {
////                    mapData.put(key, newMapData.get(key));
////                }
////            }
//
//            reportData = gson.toJson(mapData);
//        }
//        Map<String, Map<String, String>> mapData1 = container.getDbData();
//        if(mapData1==null){
//        mapData1 = new HashMap<String, Map<String, String>>();
//        }
//        for (String key : newMapData.keySet()) {
//                    mapData1.put(key, newMapData.get(key));
//            }

        container.setVisualData(visualData);
        return reportData;
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
        if (isoneview != null && isoneview.equalsIgnoreCase("true")) {
//
            setOneviewForm(request);
        } else {
            setReportForm(request);
        }
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

            }else{
                st_date = "str_to_date('" + st_date + " ','%m/%d/%Y')";
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
            Set<Entry<String, List<String>>> set = map.entrySet();

            for (Entry<String, List<String>> entry : set) {
                drillmapFromSet.put(entry.getKey(), entry.getValue());
            }
        }
        Map<String, List<String>> filtermap = new HashMap<String, List<String>>();
        HashMap<String, List> filtermapFromSet = new HashMap<String, List>();
        if (reportMeta.getFilterMap() != null) {
            filtermap = reportMeta.getFilterMap();
            Set<Entry<String, List<String>>> filterset = filtermap.entrySet();
            for (Entry<String, List<String>> entry : filterset) {
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

        Map<String, DashboardChartData> chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());
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
            DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
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

//            String measureclause = "";
            StringBuilder measureclause = new StringBuilder();
            int filterCount = 0;
            //for measure filters
            if (reportMeta.getChartData().get(chart).getMeasureFilters() != null) {
                Map<String, Map<String, String>> measureMap = reportMeta.getChartData().get(chart).getMeasureFilters();
                Set<String> measuresFilter = measureMap.keySet();
                for (String measureVal : measuresFilter) {
                    try {
                        String queryForMeasure = "select user_col_name,AGGREGATION_TYPE from prg_user_all_info_details where element_id='" + measureVal + "'";
                        PbReturnObject measureObj = pbdb.execSelectSQL(queryForMeasure);
                        Set<String> measuresFiltersMap = measureMap.get(measureVal).keySet();
                        for (String measureSymbol : measuresFiltersMap) {
                            if (filterCount == 0) {
//                                measureclause += " having ";
                                measureclause.append(" having ");
                            } else {
//                                measureclause += " and ";
                                measureclause.append(" and ");
                            }
                            filterCount++;
                            if (measureSymbol.equalsIgnoreCase("<>")) {
//                              measureclause += "SUM(" + measureObj.getFieldValueString(0, 0) + ") between  " + measureMap.get(measureVal).get(measureSymbol).split("__")[0] + " and " + measureMap.get(measureVal).get(measureSymbol).split("__")[1];
                                measureclause.append("SUM(").append(measureObj.getFieldValueString(0, 0)).append(") between  ").append(measureMap.get(measureVal).get(measureSymbol).split("__")[0]).append(" and ").append(measureMap.get(measureVal).get(measureSymbol).split("__")[1]);
                            } else {
//                                measureclause += "SUM(" + measureObj.getFieldValueString(0, 0) + ") " + measureSymbol + "  " + measureMap.get(measureVal).get(measureSymbol) + " ";
                                measureclause.append("SUM(").append(measureObj.getFieldValueString(0, 0)).append(") ").append(measureSymbol).append("  ").append(measureMap.get(measureVal).get(measureSymbol)).append(" ");
                            }
//                           measureclause += measureObj.getFieldValueString(0, 1)+"("+measureObj.getFieldValueString(0, 0)+") "+measureSymbol+"  "+measureMap.get(measureVal).get(measureSymbol)+" ";
                        }
//                        filterClause += " and A_"+reportMeta.getChartData().get(chart).getMeassureIds().get(0)+" > "+reportMeta.getChartData().get(chart).getMeasureProperties().get("A_"+reportMeta.getChartData().get(chart).getMeassureIds().get(0))+" ";
                    } catch (SQLException ex) {
                        logger.error("Exception:", ex);
                    }
                }
            }
             List<String> grandTotalAggType = new ArrayList<>(chartDetails.getMeassureIds().size());
           grandTotalAggType = addGrandTotalAggType(chartDetails);
            ProgenAOQuery objQuery = new ProgenAOQuery();
             if (userid != null && !userid.equalsIgnoreCase("")) {
            dimSecurityClause = objectQuery.getDimSecClause(reportMeta.getViewbysIds(), userid,filtermapFromSet);
        }
            String query = "";
            if (aoAsGoId != null && !aoAsGoId.equalsIgnoreCase("null")&& !aoAsGoId.equalsIgnoreCase("")) {
                objQuery.setAO_name("M_AO_" + aoAsGoId);
            } else {
                objQuery.setAO_name("R_GO_" + reportId);
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
            objQuery.collectMulticalender=container.getReportCollect().getcollectMulticalender(); //added by Mohit for change calendar
            
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
                    //end of code by anitha
                    List<String> colNames = new ArrayList();
                    List<String> colNames1 = new ArrayList();
                    List<String> colIds = new ArrayList();
                    List<String> colIds1 = new ArrayList();
                    List<String> meaIds = new ArrayList();
                    List<String> meaName1 = new ArrayList();
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
                            || chartDetails.getChartType().equalsIgnoreCase("grouped-table") 
                            || chartDetails.getChartType().equalsIgnoreCase("Grouped-Map") 
                            || chartDetails.getChartType().equalsIgnoreCase("grouped-line") 
                            || chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar") 
                            || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar") 
                            || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-BarLine") 
                            || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar%") 
                            || chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar%") 
                            || chartDetails.getChartType().equalsIgnoreCase("Grouped-MultiMeasureBar") 
                            || (reportMeta.getChartType() != null && (reportMeta.getChartType().equalsIgnoreCase("Multi-View-Bubble") || reportMeta.getChartType().equalsIgnoreCase("Split-Graph") || reportMeta.getChartType().equalsIgnoreCase("India-Map-Dashboard") || reportMeta.getChartType().equalsIgnoreCase("world-map-animation")))) && chartDetails.getViewIds().size() > 1) {
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

                    if (chart.equalsIgnoreCase("chart1") && reportMeta.getVisualChartType() != null && request.getParameter("currType") != null && reportMeta.getVisualChartType().get(request.getParameter("currType")).equalsIgnoreCase("Overlay")) {
                        cutOfmap1 = processOverLayJson(generateChartJsonForOverLay(pbretObj, chartDetails.getViewIds(), chartDetails.getMeassureIds(), reportMeta.getChartData().get(chart).getViewIds().size()));
                    } else {
                        list = generateChartJson(pbretObj, colNames, colIds, reportMeta.getChartData().get(chart).getViewIds().size(), session, timeDetails);
                        if(list!=null && !list.isEmpty()){
                        getSortMeasureValue(chartDetails,list);
                    }
                    }
//                    List<Map<String, String>> list = generateChartJson(pbretObj,colNames,colIds);
                    if (reportMeta.getChartType() != null && (reportMeta.getChartType().equalsIgnoreCase("Fish-Eye") || reportMeta.getChartType().equalsIgnoreCase("tree-map") || reportMeta.getChartType().equalsIgnoreCase("CoffeeWheel") || reportMeta.getChartType().equalsIgnoreCase("Tree-map-single"))) {
                        List<Map<String, List<Double>>> listData = generateChartJsonHie(pbretObj, colNames, colIds, colIds1, colNames1, meaIds, meaName1);
                        Map<String, List<String>> combineList = combineChunks(listData, meaName1);
                        Map mapData = new HashMap();
                        if (reportMeta.getChartType().equalsIgnoreCase("Tree-map-single")) {
                            mapData = combineList;
                        } else {
                            mapData = processHirarchiJson(combineList);
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
                            if (chartDetails.getRecords() != null && !chartDetails.getRecords().toString().equalsIgnoreCase("") && !chartDetails.getChartType().equalsIgnoreCase("grouped-bar") && !chartDetails.getChartType().equalsIgnoreCase("GroupedHorizontal-Bar") && !chartDetails.getChartType().equalsIgnoreCase("Scatter-Bubble") && !chartDetails.getChartType().equalsIgnoreCase("Split-Bubble") && !chartDetails.getChartType().equalsIgnoreCase("world-map") && chartDetails.getChartType().equalsIgnoreCase("Trend-Combo") && !chartDetails.getChartType().equalsIgnoreCase("Grouped-Table") && !chartDetails.getChartType().equalsIgnoreCase("Grouped-Map") && !chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar") && !chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar") && !chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar%") && !chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar%") && !chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-BarLine") && !chartDetails.getChartType().equalsIgnoreCase("grouped-line") && !chartDetails.getChartType().equalsIgnoreCase("multi-view-bubble") && !chartDetails.getChartType().equalsIgnoreCase("Multi-View-Tree") && !chartDetails.getChartType().equalsIgnoreCase("Grouped-MultiMeasureBar")) {
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

    public String setgblfilter(HttpServletRequest request) throws FileNotFoundException {

        PbReturnObject securityfilters = null;
        ArrayList<String> a = new ArrayList<String>();
        Map map2 = new HashMap();
        PbDb pbdb = new PbDb();
        securityfilters = (PbReturnObject) request.getSession(false).getAttribute("securityfilters");
        for (int i1 = 0; i1 < securityfilters.getRowCount(); i1++) {
            try {
                PbReturnObject retObj1 = null;
                String ELEMENT_ID = securityfilters.getFieldValueString(i1, "ELEMENT_ID");
                String MEMBER_VALUE = securityfilters.getFieldValueClobString(i1, "MEMBER_VALUE").replace("'", "");
                String query1 = "select buss_col_name,BUSS_TABLE_NAME from prg_user_all_info_details where ELEMENT_ID= " + ELEMENT_ID;
                retObj1 = pbdb.execSelectSQL(query1);
                Object[] ObjArray = new Object[3];
                ObjArray[0] = retObj1.getFieldUnknown(0, 0);
                ObjArray[1] = retObj1.getFieldUnknown(0, 1);
                ObjArray[2] = retObj1.getFieldUnknown(0, 0);
                String query = "select distinct  &   from  &  where & is not null ";

                Statement st = null;
                ResultSet rs = null;
                Connection conn = ProgenConnection.getInstance().getConnectionForElement(ELEMENT_ID);
                st = conn.createStatement();
                String finalQuery = pbdb.buildQuery(query, ObjArray);
                rs = st.executeQuery(finalQuery);
                // a.add("All");
                while (rs.next()) {
                    if (MEMBER_VALUE != null && MEMBER_VALUE.equalsIgnoreCase(rs.getString(1).replace("'", ""))) {
                    } else {
                        a.add(rs.getString(1).replace("'", ""));
                    }
                }
                map2.put(ELEMENT_ID, a);
                rs.close();
                rs = null;
                st.close();
                st = null;
                conn.close();
                conn = null;

            } catch (SQLException ex) {

                logger.error("Exception:", ex);

            }
            request.setAttribute("secrfilter", map2);
        }
        return null;
    }

    public String getChartsDataDrills(HttpServletRequest request, String fileLocation) throws FileNotFoundException, SQLException {
        String reportData = "";
        String sortColumns = "";
        String sortType = "";
        String innersortColumns = "";
        String innersortType = "";
        String reportId = "";
        String filterClause = "";
        String timeClause = "";
        String timeClause1 = "";
        String dimSecurityClause = "";
        String isoneview = "";
        String oneviewid = "";
        String regid = "";
        String viewByChange = "";
          ArrayList rowViewbyCols = null;
        HashMap NonViewByMap = new HashMap();
        HashMap<String, String> CrosstabMsrMap = new HashMap<String, String>();
        PbReturnObject newCrossRetObj;
        newCrossRetObj = null;

        String viewChartId = "";
        String nooneviewfilter = "";
        Map returnObjectMap = new HashMap();
        List<OneViewLetDetails> oneviewletDetails;

        OneViewLetDetails detail = null;
        String fromoneview = request.getParameter("fromoneview");
        String chartname = request.getParameter("chartname");
        String chartFlag = request.getParameter("chartFlag");
        String divId = request.getParameter("chartID");
        String action = request.getParameter("action");
        String actionGo = request.getParameter("actionGo");
        viewByChange = request.getParameter("changeView");
        viewChartId = request.getParameter("viewChartId");
        String trendType = request.getParameter("trendType");
        String isseurity = (String) request.getAttribute("isseurity");
        String filtertype = (String) request.getAttribute("filtertype");
        String refrehreporttime = (String) request.getAttribute("refrehreporttime");
        reportId = request.getParameter("reportId");
        String aoAsGoId = request.getParameter("aoAsGoId");
        HttpSession session = request.getSession(false);
        String userid = String.valueOf(session.getAttribute("USERID"));
        ArrayList<String> timeDetails = new ArrayList<String>();
        ArrayList<String> timeDetailsGO = new ArrayList<String>();
//         = new PbReportCollection();

        List<String> ReturnGTResultList = new ArrayList<String>();
        if (fromoneview != null && fromoneview.equalsIgnoreCase("true") && isseurity != null && isseurity.equalsIgnoreCase("true")) {
            setgblfilter(request);
        }
        Gson gson = new Gson();
        PbDb pbdb = new PbDb();
        
        if (reportId == null) {
            reportId = request.getParameter("graphsId");
        }
        Type tarType = new TypeToken<XtendReportMeta>() {
        }.getType();
        reportMeta = new XtendReportMeta();
        setReportForm(request);
        ReportObjectQuery objectQuery = new ReportObjectQuery();
        Container container = Container.getContainerFromSession(request, reportId);
        PbReportCollection collect = container.getReportCollect();
        if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {

            if (refrehreporttime != null && refrehreporttime.equalsIgnoreCase("true")) {
                setOneviewForm(request);

            }
        } else {
            if (container != null) {

                timeDetails = container.getTimeDetailsArray();
                for (int z = 0; z < timeDetails.size(); z++) {
                    timeDetailsGO.add(z, timeDetails.get(z));
                }
//                   String connType = null;
//                 String st_date  =  timeDetailsGO.get(2).toString();
//               GetConnectionType gettypeofconn = new GetConnectionType();
//        connType = gettypeofconn.getConTypeByElementId(reportMeta.getMeasuresIds().get(0));
//        if (connType!=null && connType.equalsIgnoreCase("SqlServer")) {
//            st_date = "   convert(datetime,'" + st_date + "',120) ";
//
//        }else if (connType!=null && connType.equalsIgnoreCase("Oracle")) {
//            st_date = "   to_date('" + st_date + "','mm/dd/yyyy hh24:mi:ss ') ";
//
//        }else if (connType!=null && connType.equalsIgnoreCase("Postgres")) {
//            st_date = "   to_timestamp('" + st_date + "','mm/dd/yyyy hh24:mi:ss ') ";
//
//        }
//                 String timeAggQuery = "";
//                 if(timeDetailsGO !=null){
//                     if(timeDetailsGO.get(3).toString().equalsIgnoreCase("QTR")){
//                         timeAggQuery = "select QTR_NAME AGG_NAME from pr_day_info where ddate="+st_date+"";
//                     }else if(timeDetailsGO.get(3).toString().equalsIgnoreCase("Month")){
//                          timeAggQuery = "select MONTH_NAME AGG_NAME from pr_day_info where ddate="+st_date+"";
//                     }else if(timeDetailsGO.get(3).toString().equalsIgnoreCase("Year")){
//                          timeAggQuery = "select YEAR_NAME AGG_NAME from pr_day_info where ddate="+st_date+"";
//                     }
//                     PbReturnObject retObj2 = null;
//            Connection  conn = null;
//
//            if(reportMeta.getViewbysIds().get(0).trim().equalsIgnoreCase("TIME")){
//            conn = ProgenConnection.getInstance().getConnectionForElement(reportMeta.getMeasuresIds().get(0));
//            }
//            else{
//            conn = ProgenConnection.getInstance().getConnectionForElement(reportMeta.getViewbysIds().get(0));
//            }
//            if(conn !=null){
//                         try {
//                             if(!timeAggQuery.equalsIgnoreCase("")){
//                             retObj2 = pbdb.execSelectSQL(timeAggQuery, conn);
////                             timeDetails.remove(4);
//                             timeDetailsGO.add(retObj2.getFieldValueString(0, "AGG_NAME"));
//                             }
//                         } catch (Exception ex) {
//                             logger.error("Exception:",ex);
//                         }
//            }

//                 }
                reportMeta.setTimedetails(timeDetailsGO);
            }
            timeClause = objectQuery.getCalenderTime(container, reportMeta.getMeasuresIds().get(0).toString());

            timeClause1 = timeClause;
            if (trendType != null) {
                timeClause1 = "";
                if (trendType.trim().equalsIgnoreCase("Month Till Date")) {
                    timeClause1 = objectQuery.getCompareClause(container, reportMeta.getMeasuresIds().get(0), "MOM");
                } else if (trendType.trim().equalsIgnoreCase("PRIOR MTD")) {
                    timeClause1 = objectQuery.getCompareClause(container, reportMeta.getMeasuresIds().get(0), "PMOM");
                } else if (trendType.trim().equalsIgnoreCase("Qtr Till Date")) {
                    timeClause1 = objectQuery.getCompareClause(container, reportMeta.getMeasuresIds().get(0), "QOQ");
                } else if (trendType.trim().equalsIgnoreCase("PRIOR QTD")) {
                    timeClause1 = objectQuery.getCompareClause(container, reportMeta.getMeasuresIds().get(0), "PQOQ");
                } else if (trendType.trim().equalsIgnoreCase("Year Till Date")) {
                    timeClause1 = objectQuery.getCompareClause(container, reportMeta.getMeasuresIds().get(0), "YOY");
                }
            }

        }

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        HashMap<String, List> drillmapFromSet = new HashMap<String, List>();
        HashMap<String, List> drillmapFromSetAgg = new HashMap<String, List>();
        List<String> driverList = new ArrayList<String>();
        if (request.getParameter("driverList") != null) {
            driverList = gson.fromJson(request.getParameter("driverList"), new TypeToken<List<String>>() {
            }.getType());
        }
//        setGlobalValuetoReportMeta(session,reportMeta);//

        if (reportMeta.getDrills() != null) {
            map = reportMeta.getDrills();
            Set<Entry<String, List<String>>> set = map.entrySet();

            for (Entry<String, List<String>> entry : set) {
                //sandeep
                if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
                    List<String> viewbyids = new ArrayList(reportMeta.getViewbysIds());
                    for (String viewbyid : viewbyids) {
                        if (viewbyid.equalsIgnoreCase(entry.getKey())) {
                            drillmapFromSet.put(entry.getKey(), entry.getValue());
                        }
                    }
                } else {
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
            if (filterMap.size() > 0 && !filterMap.isEmpty()) {
                nooneviewfilter = "true";
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
        HashMap<String, List> likeMap = new HashMap<String, List>();
        HashMap<String, List> notLikeMap = new HashMap<String, List>();
//                     filterClause = objectQuery.getFilterClause(drillmapFromSet,filtermapFromSet,likeMap,notLikeMap);
        Map<String, DashboardChartData> chartData;
        Map<String, List<Map<String, String>>> dataMap = new HashMap<String, List<Map<String, String>>>();
        Map<String, String> dataMapgblsave = new HashMap<String, String>();//sandeep
        if (request.getSession(false).getAttribute("dataMapgblsave") != null) {
            dataMapgblsave = (Map<String, String>) request.getSession(false).getAttribute("dataMapgblsave");
        }

        if (fromoneview != null && fromoneview.equalsIgnoreCase("true") && isseurity != null && isseurity.equalsIgnoreCase("true")) {
            Map<String, DashboardChartData> chartData1 = (Map<String, DashboardChartData>) request.getAttribute("chartData");
// chartData = gson.fromJson(request.getAttribute("chartData"), new TypeToken<LinkedHashMap<String, DashboardChartData>>() {
//            }.getType());
            chartData = chartData1;
            reportMeta.setChartData(chartData1);
        } else {
            chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());
        }
// added for display label by mayank
        String def_id = "";
        PbDisplayLabel dispLabel = PbDisplayLabel.getPbDisplayLabel();
        if (dispLabel != null) {
            def_id = dispLabel.getDefaultCompanyId();
            if (dispLabel != null && reportMeta.getViewbysIds() != null) {
                List<String> viewLabel = new ArrayList<String>();
                for (int i = 0; i < reportMeta.getViewbysIds().size(); i++) {
                    String id = reportMeta.getViewbysIds().get(i);
                    String viewName = reportMeta.getViewbys().get(i);
                    if (id != null && dispLabel.getColDisplayWithCompany(def_id, id) != null) {
                        viewLabel.add(dispLabel.getColDisplayWithCompany(def_id, id));
                    } else {
                        viewLabel.add(viewName);
                    }
                }
                reportMeta.setViewbys(viewLabel);
            }
            if (dispLabel != null && reportMeta.getMeasuresIds() != null) {
                List<String> measLabel = new ArrayList<String>();
                for (int i = 0; i < reportMeta.getMeasuresIds().size(); i++) {
                    String measId = reportMeta.getMeasuresIds().get(i);
                    String measName = reportMeta.getMeasures().get(i);
                    if (measId != null && dispLabel.getColDisplayWithCompany(def_id, measId) != null) {
                        measLabel.add(dispLabel.getColDisplayWithCompany(def_id, measId));
                    } else {
                        measLabel.add(measName);
                    }
                }
                reportMeta.setMeasures(measLabel);
            }
        }
        ProgenTimeDefinition timeDefObj = ProgenTimeDefinition.getInstance(reportId, container,userid);
        if (timeDefObj != null) {
            if (timeDefObj != null && reportMeta.getMeasuresIds() != null) {
                List<String> measLabel = new ArrayList<String>();
                for (int i = 0; i < reportMeta.getMeasuresIds().size(); i++) {
                    String measId = reportMeta.getMeasuresIds().get(i);
                    String measName = reportMeta.getMeasures().get(i);
                    if (measId != null && timeDefObj.getTimeDefinition() != null) {
                        measLabel.add(getTimeBasedMeasure(timeDefObj, measName, measId, timeDetailsGO));
                    } else {
                        measLabel.add(measName);
                    }
                }
                reportMeta.setMeasures(measLabel);
            }
        }

        List<String> charts = new ArrayList(chartData.keySet());
        Map cutOfmap = new HashMap();
        Map cutOfmap1 = new HashMap();
        List<String> kpiChartId = new ArrayList<>();
        for (String chart : charts) {

            //sandeep
            if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
                chart = chartname;
//                isoneview=fromoneview;
                oneviewid = request.getParameter("oneviewID");
                regid = request.getParameter("regid");
                String userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
                String oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
                String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
                String fileName = reportTemplateDAO.getOneviewFileName(oneviewid);
                File file = null;
                String bizzRoleName = "";
                OnceViewContainer onecontainer1 = null;
                file = new File(oldAdvHtmlFileProps + "/" + fileName);
                if (file.exists()) {
                    ObjectInputStream ois = null;
                    try {
                        FileInputStream fis = new FileInputStream(oldAdvHtmlFileProps + "/" + fileName);
                        ois = new ObjectInputStream(fis);
                        onecontainer1 = (OnceViewContainer) ois.readObject();
                        ois.close();
                    } catch (ClassNotFoundException ex) {
                        logger.error("Exception:", ex);
                    } catch (IOException ex) {
                        logger.error("Exception:", ex);
                    } finally {
                        try {
                            ois.close();
                        } catch (IOException ex) {
                            logger.error("Exception:", ex);
                        }
                    }
                }

                oneviewletDetails = onecontainer1.onviewLetdetails;
//               OneViewLetDetails detail=null;
                for (int i = 0; i < oneviewletDetails.size(); i++) {
                    detail = oneviewletDetails.get(i);
                    String regionid = detail.getNoOfViewLets();
                    if (regionid != null && regionid.equalsIgnoreCase(regid)) {
                        if (!detail.isOneviewReportTimeDetails()) {
                            isoneview = fromoneview;
                            break;
                        }
                        ArrayList Timedetails = new ArrayList();
                        if (session.getAttribute("timedetails") != null) {
                            Timedetails = (ArrayList) session.getAttribute("timedetails");
                            session.removeAttribute("timedetails");
                        } else if (request.getParameter("timedetails") != null) {
                            String[] timearraylist = request.getParameter("timedetails").split(",");
                            for (int i1 = 0; i1 < timearraylist.length; i1++) {
                                Timedetails.add(timearraylist[i1]);
                            }
                        }

                        timeClause = objectQuery.getOneviewCalTime(Timedetails, reportMeta.getChartData().get(chart).getMeassureIds().get(0).toString());
                        timeClause1 = timeClause;

                    }

                }
            }
            // end sandeep
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
            if (dispLabel != null && chartDetails.getViewIds() != null) {
                List<String> viewLabel = new ArrayList<>();
                for (int i = 0; i < chartDetails.getViewIds().size(); i++) {
                    String id = chartDetails.getViewIds().get(i);
                    String viewName = chartDetails.getViewBys().get(i);
                    if (id != null && dispLabel.getColDisplayWithCompany(def_id, id) != null) {
                        viewLabel.add(dispLabel.getColDisplayWithCompany(def_id, id));
                    } else {
                        viewLabel.add(viewName);
                    }
                }
                chartDetails.setViewBys(viewLabel);
            }
            if (dispLabel != null && chartDetails.getMeassureIds() != null) {
                List<String> measLabel = new ArrayList<>();
                for (int i = 0; i < chartDetails.getMeassureIds().size(); i++) {
                    String id = chartDetails.getMeassureIds().get(i);
                    String measName = chartDetails.getMeassures().get(i);
                    if (id != null && dispLabel.getColDisplayWithCompany(def_id, id) != null) {
                        measLabel.add(dispLabel.getColDisplayWithCompany(def_id, id));
                    } else {
                        measLabel.add(measName);
                    }
                }
                chartDetails.setMeassures(measLabel);
            }
            if (reportMeta.getDrillFormat() != null && (reportMeta.getDrillFormat().equalsIgnoreCase("time") || !drillmapFromSetAgg.keySet().isEmpty())) {
                timeDetailsGO = new ArrayList<String>();
                timeDetailsGO = getTimeClauseForGoAgg(drillmapFromSet, drillmapFromSetAgg, collect, reportMeta, chartDetails);
                objQuery.setTimeDetails(timeDetailsGO);
            } else {
                if (chartDetails.getTimeEnable() != null && chartDetails.getTimeEnable().equalsIgnoreCase("Yes")) {
                    objQuery.setIsChartTimeEnable("Yes");
                    objQuery.setChartTimeClause(chartDetails.getTimeBasedData().get(0));
                }
//                   else{
                System.out.println("***********************************************timeDetails**************" + timeDetailsGO.toString());
               if(chartDetails.getCustomtimeType()!=null && chartDetails.getCustomtimeType().equalsIgnoreCase("Yes")){  // add by mayank sharma for custom time on chart
                timeDetailsGO=customTimechart(chartDetails);
               }
                objQuery.setTimeDetails(timeDetailsGO);
//                   }   
            }
            if (timeDefObj != null && chartDetails.getMeassureIds() != null) {
                List<String> measLabel = new ArrayList<>();
                List<String> compMeasLabel = new ArrayList<>();
                for (int i = 0; i < chartDetails.getMeassureIds().size(); i++) {
                    String measId = chartDetails.getMeassureIds().get(i);
                    String measName = chartDetails.getMeassures().get(i);
                    if (measId != null && timeDefObj.getTimeDefinition() != null) {
                        measLabel.add(getTimeBasedMeasure(timeDefObj, measName, measId, timeDetailsGO));
                    } else {
                        measLabel.add(measName);
                    }
                }
                chartDetails.setMeassures(measLabel);
                if (chartDetails.getComparedMeasureId() != null && !chartDetails.getComparedMeasureId().isEmpty()) {
                    for (int k = 0; k < chartDetails.getComparedMeasureId().size(); k++) {
                        String comId = chartDetails.getComparedMeasureId().get(k);
                        String comMeas = chartDetails.getComparedMeasure().get(k);
                        if (comId != null && timeDefObj.getTimeDefinition() != null) {
                            compMeasLabel.add(getTimeBasedMeasure(timeDefObj, comMeas, comId, timeDetailsGO));
                        } else {
                            compMeasLabel.add(comMeas);
                        }
                    }
                    chartDetails.setComparedMeasure(compMeasLabel);
                }
            }

            if ((chartDetails.getCompleteChartData() != null && chartDetails.getCompleteChartData().equalsIgnoreCase("Yes")) || (request.getParameter("type") != null && request.getParameter("type").equalsIgnoreCase("trend")) || (filterClause != null && !filterClause.equalsIgnoreCase(""))) {
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
                    Map<String, List<String>> map2 = gson.fromJson(request.getParameter("filters1"), tarType1);
//Added By Ram
                    HashMap lookupFilter = container.getFilterLookupData();
                    filterLookupOriginalToNew = container.getFilterLookupOriginalToNew();
                    for (int t = 0; t < reportMeta.getViewbysIds().size(); t++) {
                        String id = reportMeta.getViewbysIds().get(t);
                        if (map2 != null) {
                            if (map2.containsKey(id)) {
                                if (!map2.get(id).isEmpty()) {
                                    List list = map2.get(id);
                                    for (int g = 0; g < list.size(); g++) {
                                        if (lookupFilter.containsKey(list.get(g))) {
                                            list.set(g, lookupFilter.get(list.get(g)));
                                        }
                                    }
                                    map2.put(id, list);
                                }
                            }
                        }
                    }
// End Ram Code
                    reportMeta.setFilterMap(map2);
                }
            }
            if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {

                if (isseurity != null && isseurity.equalsIgnoreCase("true")) {
                    Type tarType1 = new TypeToken<Map<String, List<String>>>() {
                    }.getType();
                    Map<String, List<String>> map11 = (Map<String, List<String>>) request.getAttribute("secrfilter");
//                map2=map11;
                    reportMeta.setFilterMap(map11);
                }
            }
            if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
// String filtertype=(String) request.getAttribute("filtertype");
                if (filtertype != null && (filtertype.equalsIgnoreCase("disablefilter") || filtertype.equalsIgnoreCase("enablefilter"))) {
                    Map<String, List<String>> filtermap = (Map<String, List<String>>) request.getAttribute("FilterMap");
                    reportMeta.setFilterMap(filtermap);
                }
            }
            if (reportMeta.getFilterMap() != null) {
//            filtermap = reportMeta.getFilterMap();
                Set<Entry<String, List<String>>> filterset = reportMeta.getFilterMap().entrySet();
                for (Entry<String, List<String>> entry : filterset) {
                    if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
                        if (action != null && action.equalsIgnoreCase("drillacross")) {
                        } else {
                            List<String> viewbyids = new ArrayList(reportMeta.getViewbysIds());
                            for (String viewbyid : viewbyids) {
                                if (viewbyid.equalsIgnoreCase(entry.getKey())) {
                                    drillmapFromSet.put(entry.getKey(), entry.getValue());
                                }
                            }
                        }
                    } else {
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
            }
            if (action != null && action.equalsIgnoreCase("localfilter")) {

                if (reportMeta.getChartData().get(chart).getFilters() != null) {
                    List<String> listVal = new ArrayList<String>();
                    Set<Entry<String, List<String>>> filterset1 = reportMeta.getChartData().get(chart).getFilters().entrySet();
                    if (filterset1.size() > 0) {
                        drillmapFromSet = new HashMap<String, List>();
                        for (Entry<String, List<String>> entry1 : filterset1) {
                            listVal = entry1.getValue();
                            if (reportMeta.getChartData().get(chart).getglobalEnable() != null && reportMeta.getChartData().get(chart).getglobalEnable().equalsIgnoreCase("Y") && reportMeta.getFilterMap() != null) {
                                Set<Entry<String, List<String>>> filterset = reportMeta.getFilterMap().entrySet();
                                for (Entry<String, List<String>> entry : filterset) {
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
            if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
                filterClause = "";
                filterClause = objectQuery.getFilterClause(drillmapFromSet, filtermapFromSet, likeMap, notLikeMap);
            } else if (action != null && action.equalsIgnoreCase("localfilterGraphs")) {

                if (reportMeta.getChartData().get(chart).getFilters() != null) {
//                  HashMap<String, List> filtermapFromSetLocal = new HashMap<String, List>();
                    List<String> listVal = new ArrayList<String>();
//                  filtermapFromSetLocal=filtermapFromSet;
                    Set<Entry<String, List<String>>> localfilterset = reportMeta.getChartData().get(chart).getFilters().entrySet();
                    for (Entry<String, List<String>> entry1 : localfilterset) {
                        listVal = entry1.getValue();
                        if (reportMeta.getChartData().get(chart).getglobalEnable() != null && reportMeta.getChartData().get(chart).getglobalEnable().equalsIgnoreCase("Y") && (reportMeta.getFilterMap() != null && !(reportMeta.getFilterMap().isEmpty()))) {
                            Set<Entry<String, List<String>>> filterset = reportMeta.getFilterMap().entrySet();
                            for (Entry<String, List<String>> entry : filterset) {
                                List<String> listValNew = new ArrayList<String>();
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
                    HashMap<String, List> filtermapFromSetLocal = new HashMap<String, List>();
                    filtermapFromSetLocal = filtermapFromSet;
                    Set<Entry<String, List<String>>> filterset = reportMeta.getChartData().get(chart).getFilters().entrySet();
                    for (Entry<String, List<String>> entry : filterset) {
                        if ((reportMeta.getChartData().get(chart).getOthersL() == null || reportMeta.getChartData().get(chart).getOthersL().equalsIgnoreCase("Y")) && !entry.getKey().equalsIgnoreCase(chartDetails.getViewIds().get(0))) {
                            List<String> listVal = new ArrayList<String>();
                            if (filtermapFromSetLocal.get(entry.getKey()) != null) {
                                listVal = filtermapFromSetLocal.get(entry.getKey());
                                listVal.addAll(listVal.size(), entry.getValue());
                            } else {
                                listVal = entry.getValue();
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
                                Set<Entry<String, List<String>>> filtersetGlobal = reportMeta.getFilterMap().entrySet();
                                for (Entry<String, List<String>> entry1 : filtersetGlobal) {
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
//                           measureVal = objQuery1.removeComparisonType(measureVal);
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
                                 measureclause += "A_"+measureVal+" "+measureSymbol+"  "+measureMap.get(measureVal).get(measureSymbol)+" ";
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
                    Set<Entry<String, List<String>>> localDrills = reportMeta.getChartData().get(chart).getLocalDrills().entrySet();
                    for (Entry<String, List<String>> entry1 : localDrills) {
                        listVal = entry1.getValue();
                        //Added by Ram
                        HashMap lookupFilter = container.getFilterLookupData();
                        for (int g = 0; g < listVal.size(); g++) {
                            if (lookupFilter.containsKey(listVal.get(g))) {
                                listVal.set(g, lookupFilter.get(listVal.get(g)).toString());
                            }
                        }
                        //ended by ram
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
                        Set<Entry<String, List<String>>> localfilterset = reportMeta.getChartData().get(chart).getFilters().entrySet();
                        for (Entry<String, List<String>> entry1 : localfilterset) {
                            listVal = entry1.getValue();
                            if (reportMeta.getChartData().get(chart).getglobalEnable() != null && reportMeta.getChartData().get(chart).getglobalEnable().equalsIgnoreCase("Y") && (reportMeta.getFilterMap() != null && !(reportMeta.getFilterMap().isEmpty()))) {
                                Set<Entry<String, List<String>>> filterset = reportMeta.getFilterMap().entrySet();
                                for (Entry<String, List<String>> entry : filterset) {
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
                Set<Entry<String, List<String>>> filterset = reportMeta.getNotfilters().entrySet();
                for (Entry<String, List<String>> entry : filterset) {
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
                if (reportMeta.getChartType() != null && reportMeta.getChartType().equals("Time-Dashboard")) {
                    String firstMeasure[] = new String[]{measId1[0]};
                    GTResult = jsongenerator.GTKPICalculateFunction(request, reportId, firstMeasure, measBys1, aggregation1, chart, timeClause, dimSecurityClause, container);
                } else {
                    GTResult = jsongenerator.GTKPICalculateFunction(request, reportId, measId1, measBys1, aggregation1, chart, timeClause, dimSecurityClause, container);
                }
//          Object GTResultObject = GTResult;
//          for(int u=0;u<GTResultObject.s;u++){
//          }

//          String[] GTResultStringArray =  GTResult.replace("{", "").replace("}", "").replace("\"", "").split(",");
                List<String> GTResultMap = new ArrayList<String>(Arrays.asList(GTResult.replace("[", "").replace("]", "").replace("\"", "").split(",")));
//          GTResultMap.add(GTResultStringArray[0]);
//          GTResultMap.add(GTResult .replace("{", "").replace("}", ""));
//          Map<String,String> GTResultMap = new HashMap<String, String>();
                for (String pairs : GTResultMap) {
                    String[] entry = pairs.split(":");
////          GTResultMap.put(entry[0].trim(), entry[1].trim());
                    ReturnGTResultList.add(entry[0].trim());
                }
                reportMeta.getChartData().get(chart).setGTValueList(GTResultMap);
                if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
                    kpiChartId.add(chart);

                } else {
                    if (!reportMeta.getChartType().equals("Time-Dashboard") && !reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Table") && !reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Scatter-Analysis")) {
                        kpiChartId.add(chart);
//                        continue;
                    }

                }

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
            objQuery.collectMulticalender=container.getReportCollect().getcollectMulticalender(); //added by Mohit for change calendar
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
                List<Map<String, String>> list = new ArrayList<Map<String, String>>();
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
                    pbretObj.MeasurePos = container.getMeasurePosition();
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
                    colIds = new ArrayList<String>();
                    colNames = new ArrayList<String>();
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
                    cutOfmap1 = processOverLayJson(generateChartJsonForOverLay(pbretObj, chartDetails.getViewIds(), chartDetails.getMeassureIds(), reportMeta.getChartData().get(chart).getViewIds().size()));
                } else {
//                         if(reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("TileChart")||reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("RadialProgress")||reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("LiquidFilledGauge")||reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Dial-Gauge")||reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("KPIDash") ||reportMeta.getChartData().get(chart).getChartType().equalsIgnoreCase("Emoji-Chart")){
//                   HashMap<String,String> map1 = new HashMap<String,String>();
//                   ArrayList arrayMap = new ArrayList();
////                   map1.put("GTResult",reportMeta.getChartData().get(chart).getGTValue());
//                              arrayMap.add(map1);
//                   list= (List<Map<String, String>>) arrayMap;
//
//                        }else{

//                     if(pbretObj==null)
//                       {
//                       list= generateChartJson(pbretObj,colNames,colIds,reportMeta.getChartData().get(chart).getViewIds().size(),session,timeDetailsGO);
//                  setReturnObjectBasedOnChart(reportId, chart, query,pbretObj,fileLocation,userid);
//                    }
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
                    if(list!=null && !list.isEmpty()){
                    getSortMeasureValue(chartDetails,list);
                    }
                    }
//                     if(chartDetails.getChartType().equalsIgnoreCase("Combo-Analysis") && !chartDetails.isComboData())
//                       setComboData(reportId,chart,list,fileLocation,userid,chartDetails.getComboType());
                }

//                     List<Map<String, String>> list = generateChartJson(pbretObj,colNames,colIds);

                Map mapData = new HashMap();
                if (reportMeta.getChartType() != null && (reportMeta.getChartType().equalsIgnoreCase("Fish-Eye") || reportMeta.getChartType().equalsIgnoreCase("tree-map") || reportMeta.getChartType().equalsIgnoreCase("CoffeeWheel") || reportMeta.getChartType().equalsIgnoreCase("tree-map-single") || chartDetails.getChartType().equalsIgnoreCase("Multi-view-tree"))) {
                    List<Map<String, List<Double>>> listData = generateChartJsonHie(pbretObj, colNames, colIds, colIds1, colNames1, meaIds, meaName1);
                    Map<String, List<String>> combineList = combineChunks(listData, meaName1);
//list = new HashMap<String, List<Double>>();
                    if (reportMeta.getChartType().equalsIgnoreCase("Tree-map-single")) {
                        mapData = combineList;
                    } else {
                        mapData = processHirarchiJson(combineList);
                    }
// container = Container.getContainerFromSession(request, reportId);
// container.setTreeData(mapData);
//    return gson.toJson(mapData);
//
                }

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
                        if (chartDetails.getRecords() != null && !chartDetails.getRecords().toString().equalsIgnoreCase("") && !chartDetails.getRecords().equalsIgnoreCase("All") && chartDetails.getOthers() != null 
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
                                list = new ArrayList<Map<String, String>>();
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

//                       if(fromoneview!=null && fromoneview.equalsIgnoreCase("true")){
//                 break;
//        }
//        }
//if(fromoneview!=null && fromoneview.equalsIgnoreCase("true")){
//     return gson.toJson(dataMap);
//}else{
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
//            }
            if (fromoneview != null && fromoneview.equalsIgnoreCase("true") || viewByChange != null && viewByChange.equalsIgnoreCase("viewByChange")) {
                break;
            }
            if (chartFlag != null && chartFlag.equalsIgnoreCase("true") && divId != null && !divId.equalsIgnoreCase("")) {
                break;
            }
        }
        for (String chart : charts) {
            Map<String, List<String>> localDrills = reportMeta.getChartData().get(chart).getLocalDrills();
            Set<Entry<String, List<String>>> localDrillEntrySet = null;
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
                    Set<Entry<String, List<String>>> anchorChartEntrySet = anchorChart.entrySet();
                    Iterator<Entry<String, List<String>>> iterator = anchorChartEntrySet.iterator();
                    if (iterator.hasNext()) {
                        driverChart = iterator.next().getKey();
                    }
                    List<String> viewBys = reportMeta.getChartData().get(driverChart).getViewBys();
                    String driverViewBy = viewBys.get(0);
                    if (!driverViewBy.equalsIgnoreCase(dependentViewBy)) {
                        continue;
                    }
                    iterator = anchorChartEntrySet.iterator();
                    Set<Entry<String, List<Map<String, String>>>> dataMapEntries = dataMap.entrySet();
                    Iterator<Entry<String, List<Map<String, String>>>> dataMapIterator = dataMapEntries.iterator();
                    List<Map<String, String>> driverChartData = null;
                    List<Map<String, String>> dependentChartData = null;
                    while (dataMapIterator.hasNext()) {
                        Entry<String, List<Map<String, String>>> dataMapData = dataMapIterator.next();
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
        if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
//      if((reportMeta.getFilterMap()!=null && !(reportMeta.getFilterMap()).isEmpty() && !(reportMeta.getChartData().get(chartname).getChartType().equalsIgnoreCase("Table")) )) {
            returnObjectMap.put("meta", gson.toJson(reportMeta));
            returnObjectMap.put("data", gson.toJson(dataMap));
//       break;
//       }
            if (action != null && (action.equalsIgnoreCase("olap") || action.equalsIgnoreCase("disablefilter") || action.equalsIgnoreCase("enablefilter"))) {
                if (action != null && action.equalsIgnoreCase("disablefilter") || action.equalsIgnoreCase("enablefilter")) {
                    request.getSession(false).setAttribute("dataMapgblsave", dataMapgblsave);
                }
                return gson.toJson(dataMap);
            } else if (drillmapFromSet.isEmpty()) {
                String flag = "false";
                return flag;

            } else {
                if (action != null && action.equalsIgnoreCase("drillacross")) {
                } else {
                    request.getSession(false).setAttribute("dataMapgblsave", dataMapgblsave);
                }
                if ((filtermapGO != null) && detail.isOneviewReportTimeDetails()) {
                    if (refrehreporttime != null && refrehreporttime.equalsIgnoreCase("true")) {
                        return gson.toJson(dataMap);
                    } else {
                        return null;
                    }

                } else {

                    return gson.toJson(returnObjectMap);
                }
            }
        } else {

            container = Container.getContainerFromSession(request, reportId);
            if(kpiChartId!=null){
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
                        Type metaType = new TypeToken<XtendReportMeta>() {
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
                if (reportMeta.getVisualChartType() != null && request.getParameter("currType") != null && reportMeta.getVisualChartType().get(request.getParameter("currType")).equalsIgnoreCase("Overlay")) {
                    Type dataType = new TypeToken<OverlayData>() {
                    }.getType();
                    OverlayData cutData = gson.fromJson(gson.toJson(cutOfmap), dataType);
                    container.setOverlayData(cutData);
                    return gson.toJson(cutOfmap);
                } else {
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
                }
            }
        }
    }

    //generate data json//
    public List<Map<String, String>> generateChartJson(PbReturnObject pbretObj, List<String> paramNames, List<String> paramIds, int size, HttpSession session, ArrayList<String> timeDetails) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//        ProgenAOQuery objAO = new ProgenAOQuery();
//         Map<String, ArrayList> elementCurrency;
//             elementCurrency = new HashMap<String, ArrayList>();
//             if(session !=null)
//             elementCurrency = (Map<String, ArrayList>) session.getAttribute("elementCurrency");
//             String currencySymbol = "";
//             String elementId = "";
        for (int m = 0; m < pbretObj.rowCount; m++) {
            Map<String, String> map = new LinkedHashMap<String, String>();
            for (int k = 0; k < paramNames.size(); k++) {
//                if(elementCurrency !=null && !elementCurrency.isEmpty()){
//                    elementId = objAO.removeComparisonType(paramIds.get(k).replaceAll("A_", ""));
//
//                if(elementCurrency.get(elementId)!=null && !elementCurrency.get(elementId).isEmpty() && elementCurrency.get(elementId).get(1)!=null && !elementCurrency.get(elementId).get(1).toString().equalsIgnoreCase("")){
//                    currencySymbol = elementCurrency.get(elementId).get(1).toString();
//                }else {
//                    currencySymbol = "";
//                }
////                currencySymbol = elementCurrency.get(elementId).get(1);
//                }
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
//            for (int k = 0; k < measures.size(); k++) {
//                map.put(measures.get(k), pbretObj.getFieldValueString(m, k));
//            }
            list.add(map);
        }
//        JsonComperator jsonComperator = new JsonComperator(reportMeta.getMeasures().get(0), "desc");
//        Collections.sort(list, jsonComperator);
        return list;
    }

    public List<Map<String, String>> generateChartJsonChange(PbReturnObject pbretObj, PbReturnObject pbretObj1, List<String> paramNames, List<String> paramIds, int size) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int m = 0; m < pbretObj.rowCount; m++) {
            Map<String, String> map = new LinkedHashMap<String, String>();
            for (int k = 0; k < paramNames.size(); k++) {
                if (paramNames.get(k).trim().equalsIgnoreCase("TIME")) {
                    map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, paramIds.get(k).trim()));
                } else {
                    if (k >= size && (pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k)).contains("e") || pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k)).contains("E"))) {
                        map.put(paramNames.get(k), String.valueOf(Double.valueOf(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k))).longValue()));
                    } else {
                        if (k >= size && pbretObj1.rowCount >= 1) {
                            map.put(paramNames.get(k), String.valueOf(Double.valueOf(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k))).longValue() - Double.valueOf(pbretObj1.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k))).longValue()));
                        } else {
                            map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k)));
                        }
                    }
                }

            }
//            for (int k = 0; k < measures.size(); k++) {
//                map.put(measures.get(k), pbretObj.getFieldValueString(m, k));
//            }
            list.add(map);
        }
//        JsonComperator jsonComperator = new JsonComperator(reportMeta.getMeasures().get(0), "desc");
//        Collections.sort(list, jsonComperator);
        return list;
    }

    public List<Map<String, String>> generateChartJsonChangePercent(PbReturnObject pbretObj, PbReturnObject pbretObj1, List<String> paramNames, List<String> paramIds, int size) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int m = 0; m < pbretObj.rowCount; m++) {
            Map<String, String> map = new LinkedHashMap<String, String>();
            for (int k = 0; k < paramNames.size(); k++) {
                if (paramNames.get(k).trim().equalsIgnoreCase("TIME")) {
                    map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, paramIds.get(k).trim()));
                } else {
                    if (k >= size && (pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k)).contains("e") || pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k)).contains("E"))) {
                        double prior = 0.0d;
                        double current = Double.valueOf(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k))).longValue();
                        try {

                            prior = Double.valueOf(pbretObj1.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k))).longValue();
                        } catch (NumberFormatException e) {
                            prior = Double.valueOf(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k))).longValue();
//                            logger.error("Exception:", e);
                        }
                        double changePercent = ((current - prior) / prior) * 100;
                        map.put(paramNames.get(k), String.valueOf(changePercent));
                    } else {
                        if (k >= size && pbretObj1.rowCount >= 1) {
                            double prior = 0.0d;
                            double current = Double.valueOf(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k))).longValue();
                            try {
                                prior = Double.valueOf(pbretObj1.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k))).longValue();
                            } catch (NumberFormatException e) {
                                prior = Double.valueOf(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k))).longValue();
//                                logger.error("Exception:", e);
                            }
//                            double prior = Double.valueOf(pbretObj1.getFieldValueStringBasedOnViewSeq(m, "A_"+paramIds.get(k))).longValue();
                            double changePercent = ((current - prior) / prior) * 100;
                            map.put(paramNames.get(k), String.valueOf(changePercent));
                        } else {
                            map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k)));
                        }
                    }
                }

            }
//            for (int k = 0; k < measures.size(); k++) {
//                map.put(measures.get(k), pbretObj.getFieldValueString(m, k));
//            }
            list.add(map);
        }
//        JsonComperator jsonComperator = new JsonComperator(reportMeta.getMeasures().get(0), "desc");
//        Collections.sort(list, jsonComperator);
        return list;
    }

    public List<Map<String, String>> generateChartJsonForOverLay(PbReturnObject pbretObj, List<String> paramNames, List<String> measureNames, int size) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        for (int m = 0; m < pbretObj.rowCount; m++) {
            Map<String, String> map = new HashMap<String, String>();
            List<Double> newList = new ArrayList<Double>();
            newList.add(Double.valueOf(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + measureNames.get(0).trim())));
//            for (int k = 0; k < paramNames.size(); k++) {
//                if(paramNames.get(k).trim().equalsIgnoreCase("TIME")){
            map.put(pbretObj.getFieldValueString(m, "A_" + paramNames.get(0)) + "," + pbretObj.getFieldValueString(m, "A_" + paramNames.get(1)), pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + measureNames.get(0).trim()));
//                }else{
//                    if(k>=size && (pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_"+paramIds.get(k)).contains("e") || pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_"+paramIds.get(k)).contains("E"))){
//                         map.put(paramNames.get(k), String.valueOf(Double.valueOf(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_"+paramIds.get(k))).longValue()));
//                        }
//                    else{
//                map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_"+paramIds.get(k)));
//                }
//                }
            list.add(map);
        }
        return list;
//            for (int k = 0; k < measures.size(); k++) {
//                map.put(measures.get(k), pbretObj.getFieldValueString(m, k));
//            }
//            list.add(map);
    }
//        JsonComperator jsonComperator = new JsonComperator(reportMeta.getMeasures().get(0), "desc");
//        Collections.sort(list, jsonComperator);
//        return list;
//    }

    public Map processOverLayJson(List<Map<String, String>> rawMapOverLay) {
        Map<String, List<Map<String, String>>> cutOfMap = new HashMap<String, List<Map<String, String>>>();
        Map map = new HashMap();
        Map<String, String> rawMap = new HashMap<String, String>();
        Map<String, String> cutOfSorter = new HashMap<String, String>();
        for (int k = 0; k < rawMapOverLay.size(); k++) {
//        rawMap.put(rawMap.get(k));
//        }
            rawMap = rawMapOverLay.get(k);
            String[] keys = (String[]) rawMap.keySet().toArray(new String[0]);

            for (String key : keys) {
                Map<String, String> tempMap = new HashMap<String, String>();
                String measureValues = rawMap.get(key);
                String[] viewsArray = key.split(",");
                tempMap.put(reportMeta.getChartData().get("chart1").getViewBys().get(1), viewsArray[1]);
                tempMap.put(reportMeta.getChartData().get("chart1").getMeassures().get(0), measureValues);
                if (cutOfSorter.containsKey(viewsArray[0])) {
                    cutOfSorter.put(viewsArray[0], cutOfSorter.get(viewsArray[0]) + measureValues);
                } else {
                    cutOfSorter.put(viewsArray[0], measureValues);
                }
                List<Map<String, String>> innerList;
                if (cutOfMap.containsKey(viewsArray[0])) {
                    innerList = cutOfMap.get(viewsArray[0]);
                    innerList.add(tempMap);
                    cutOfMap.put(viewsArray[0], innerList);
                } else {
                    innerList = new ArrayList<Map<String, String>>();
                    innerList.add(tempMap);
                    cutOfMap.put(viewsArray[0], innerList);
                }
            }
        }
        List list = new LinkedList(cutOfSorter.entrySet());
        Map<String, List<Map<String, String>>> sortedCutOfMap = new LinkedHashMap<String, List<Map<String, String>>>();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedCutOfMap.put((String) entry.getKey(), cutOfMap.get(entry.getKey()));
        }
        map.put("cuts", sortedCutOfMap);
        return map;
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

    public List<Map<String, List<Double>>> generateChartJsonHie(PbReturnObject pbretObj, List<String> paramNames, List<String> paramIds, List<String> viewName, List<String> viewId, List<String> measId, List<String> measName) {
        List<Map<String, List<Double>>> list = new ArrayList<Map<String, List<Double>>>();
        for (int m = 0; m < pbretObj.rowCount; m++) {
            Map<String, List<Double>> map = new HashMap<String, List<Double>>();
            StringBuilder groupBys = new StringBuilder();
            for (int k = 0; k < viewName.size(); k++) {
//                if(paramNames.get(k).trim().equalsIgnoreCase("TIME")){
                if (k == 0) {
//                    groupBys = pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + viewName.get(k).trim());
                    groupBys.append(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + viewName.get(k).trim()));
                } else {
//                    groupBys += "," + pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + viewName.get(k).trim());
                    groupBys.append(",").append(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + viewName.get(k).trim()));
                }
//                }
                List<Double> list1 = new ArrayList<Double>();
                for (int i = 0; i < measName.size(); i++) {
//                if(paramNames.get(k).trim().equalsIgnoreCase("TIME")){
                    list1.add(Double.parseDouble(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + measId.get(i))));
                }
                map.put(groupBys.toString(), list1);
//                }else{
//                    if(k!=0 && (pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_"+paramIds.get(k)).contains("e") || pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_"+paramIds.get(k)).contains("E"))){
//                         map.put(paramNames.get(k), String.valueOf(Double.valueOf(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_"+paramIds.get(k))).intValue()));
//                        }
//                    else{
//                map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_"+paramIds.get(k)));
//                    }
//                }

            }
//            for (int k = 0; k < measures.size(); k++) {
//                map.put(measures.get(k), pbretObj.getFieldValueString(m, k));
//            }
            list.add(map);
        }
//        JsonComperator jsonComperator = new JsonComperator(reportMeta.getMeasures().get(0), "desc");
//        Collections.sort(list, jsonComperator);
        return list;
    }

    public Map<String, List<String>> combineChunks(List<Map<String, List<Double>>> listMap, List<String> params) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        List<String> list;
        for (Map<String, List<Double>> chunkMap : listMap) {
            Set<String> keys = chunkMap.keySet();
            for (String key : keys) {
                if (map.containsKey(key)) {
                    list = map.get(key);
//                    List<Double> measureValues = chunkMap.get(key);
                    for (int j = 0; j < params.size(); j++) {
                        String temp = list.get(j);
//                        list.remove(j);
//                            list.add(j, temp + measureValues.get(j));
//                        temp = list.get(params.size()-1);
//                        list.remove(params.size()-1);
//                        list.add(params.size(), temp + measureValues.get(params.size()-1));
                        list.add(j, String.valueOf(temp));
                    }
                } else {
                    List<String> mapList = new ArrayList<String>();
                    for (int k = 0; k < chunkMap.get(key).size(); k++) {
                        mapList.add(String.valueOf(chunkMap.get(key).get(k)));
                    }
                    map.put(key, mapList);
                }
            }
        }
        return map;
    }

    public List<Map<String, String>> processJson(Map<String, List<Double>> rawStr) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        String[] keys = (String[]) rawStr.keySet().toArray(new String[0]);
        for (String key : keys) {
            Map<String, String> tempMap = new HashMap<String, String>();
            List<Double> abc = rawStr.get(key);
            String[] a = key.split(",");
            try {
                for (int j = 0; j < reportMeta.getViewbys().size(); j++) {
                    tempMap.put(reportMeta.getViewbys().get(j), a[j]);
                }
                for (int k = 0; k < reportMeta.getMeasures().size(); k++) {
                    if (reportMeta.getAggregations().get(k).equalsIgnoreCase("AVG")) {
                        tempMap.put(reportMeta.getMeasures().get(k), String.format("%.2f", abc.get(k) / abc.get(reportMeta.getMeasures().size() + k)));
                    } else {
                        tempMap.put(reportMeta.getMeasures().get(k), String.format("%.2f", abc.get(k)));
                    }
                }
                list.add(tempMap);
            } catch (Exception e) {
                logger.error("Exception:", e);
            }
        }
        return list;
    }

    //Code added by Amar on 26th march,2015
    public String getChartsDataForGO(XtendReportMeta reportMeta, String reportId) {
        Gson gson = new Gson();
        PbDb pbdb = new PbDb();
        ReportObjectQuery objectQuery = new ReportObjectQuery();

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        HashMap<String, List> drillmapFromSet = new HashMap<String, List>();
        if (reportMeta.getDrills() != null) {
            map = reportMeta.getDrills();
            Set<Entry<String, List<String>>> set = map.entrySet();

            for (Entry<String, List<String>> entry : set) {
                drillmapFromSet.put(entry.getKey(), entry.getValue());
            }
        }
        Map<String, List<String>> filtermap = new HashMap<String, List<String>>();
        HashMap<String, List> filtermapFromSet = new HashMap<String, List>();
        if (reportMeta.getFilterMap() != null) {
            filtermap = reportMeta.getFilterMap();
            Set<Entry<String, List<String>>> filterset = filtermap.entrySet();
            for (Entry<String, List<String>> entry : filterset) {
                filtermapFromSet.put(entry.getKey(), entry.getValue());
            }
        }

//         HashMap<String, List> inMap  = new HashMap<String, List>();
//                            HashMap<String, List> notInMap = new HashMap<String, List>();
        HashMap<String, List> likeMap = new HashMap<String, List>();
        HashMap<String, List> notLikeMap = new HashMap<String, List>();
        String filterClause = objectQuery.getFilterClause(drillmapFromSet, filtermapFromSet, likeMap, notLikeMap);

        Map<String, List<Map<String, String>>> dataMap = new HashMap<String, List<Map<String, String>>>();
        Map cutOfmap = new HashMap();
        Map cutOfmap1 = new HashMap();
//        String reportId = reportMeta.
        Map<String, DashboardChartData> chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());
        List<String> charts = new ArrayList(chartData.keySet());

        for (String chart : charts) {

            DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
            ArrayList<String> colViewIds = new ArrayList<String>();
            ArrayList<String> rowIds = new ArrayList<String>();
            for (int j = 0; j < chartDetails.getViewIds().size(); j++) {
                rowIds.add(chartDetails.getViewIds().get(j));
            }

//              String measureclause = "";
            StringBuilder measureclause = new StringBuilder();
            int filterCount = 0;
            //for measure filters
            if (reportMeta.getChartData().get(chart).getMeasureFilters() != null) {
                Map<String, Map<String, String>> measureMap = reportMeta.getChartData().get(chart).getMeasureFilters();
                Set<String> measuresFilter = measureMap.keySet();
                for (String measureVal : measuresFilter) {
                    try {
                        String queryForMeasure = "select user_col_name,AGGREGATION_TYPE from prg_user_all_info_details where element_id='" + measureVal + "'";
                        PbReturnObject measureObj = pbdb.execSelectSQL(queryForMeasure);
                        Set<String> measuresFiltersMap = measureMap.get(measureVal).keySet();
                        for (String measureSymbol : measuresFiltersMap) {
                            if (filterCount == 0) {
//                           measureclause+=" having ";
                                measureclause.append(" having ");
                            } else {
//                               measureclause+=" and ";
                                measureclause.append(" and ");

                            }
                            filterCount++;
                            measureclause.append("SUM(").append(measureObj.getFieldValueString(0, 0)).append(") ").append(measureSymbol).append("  ").append(measureMap.get(measureVal).get(measureSymbol) + " ");
//                           measureclause += "SUM("+measureObj.getFieldValueString(0, 0)+") "+measureSymbol+"  "+measureMap.get(measureVal).get(measureSymbol)+" ";
//                           measureclause += measureObj.getFieldValueString(0, 1)+"("+measureObj.getFieldValueString(0, 0)+") "+measureSymbol+"  "+measureMap.get(measureVal).get(measureSymbol)+" ";
                        }
                    } //     filterClause += " and A_"+reportMeta.getChartData().get(chart).getMeassureIds().get(0)+" > "+reportMeta.getChartData().get(chart).getMeasureProperties().get("A_"+reportMeta.getChartData().get(chart).getMeassureIds().get(0))+" ";
                    catch (SQLException ex) {
                        logger.error("Exception:", ex);
                    }
                }
            }

            String query = objectQuery.getObjectQueryGO(reportId, filterClause, rowIds, colViewIds, chartDetails.getMeassureIds().get(0), measureclause.toString(), "", "", "", "/usr/local/cache", "", "");
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
                    pbretObj = pbdb.execSelectSQL(query, conn);
                    List<String> colNames = new ArrayList();
                    List<String> colNames1 = new ArrayList();
                    List<String> colIds = new ArrayList();
                    List<String> colIds1 = new ArrayList();
                    List<String> meaIds = new ArrayList();
                    List<String> meaName1 = new ArrayList();
                    colIds.add(chartDetails.getViewIds().get(0));
                    colNames.add(chartDetails.getViewBys().get(0));
                    if ((chartDetails.getChartType().equalsIgnoreCase("multi-view-bubble") || chartDetails.getChartType().equalsIgnoreCase("Multi-View-Tree") || chartDetails.getChartType().equalsIgnoreCase("grouped-bar") || chartDetails.getChartType().equalsIgnoreCase("GroupedHorizontal-Bar") || chartDetails.getChartType().equalsIgnoreCase("Scatter-Bubble") || chartDetails.getChartType().equalsIgnoreCase("Split-Bubble") || chartDetails.getChartType().equalsIgnoreCase("world-map") || chartDetails.getChartType().equalsIgnoreCase("Trend-Combo") || chartDetails.getChartType().equalsIgnoreCase("grouped-table") || chartDetails.getChartType().equalsIgnoreCase("Grouped-Map") || chartDetails.getChartType().equalsIgnoreCase("grouped-line") || chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar") || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar") || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-BarLine") || chartDetails.getChartType().equalsIgnoreCase("GroupedStacked-Bar%") || chartDetails.getChartType().equalsIgnoreCase("GroupedStackedH-Bar%") || chartDetails.getChartType().equalsIgnoreCase("Grouped-MultiMeasureBar") || (reportMeta.getChartType() != null && (reportMeta.getChartType().equalsIgnoreCase("Multi-View-Bubble") || reportMeta.getChartType().equalsIgnoreCase("Split-Graph") || reportMeta.getChartType().equalsIgnoreCase("world-map-animation")))) && chartDetails.getViewIds().size() > 1) {
                        colIds.add(chartDetails.getViewIds().get(1));
                        colNames.add(chartDetails.getViewBys().get(1));
                    }
                    if (chart.equalsIgnoreCase("chart1") && reportMeta.getVisualChartType() != null && reportMeta.getCurrType() != null && reportMeta.getVisualChartType().get(reportMeta.getCurrType()).equalsIgnoreCase("Overlay")) {
                        colIds.add(chartDetails.getViewIds().get(1));
                        colNames.add(chartDetails.getViewBys().get(1));
                    }

                    for (int m = 0; m < chartDetails.getViewBys().size(); m++) {
                        colIds1.add(chartDetails.getViewIds().get(m));
                        colNames1.add(chartDetails.getViewBys().get(m));
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

                    if (chart.equalsIgnoreCase("chart1") && reportMeta.getVisualChartType() != null && reportMeta.getCurrType() != null && reportMeta.getVisualChartType().get(reportMeta.getCurrType()).equalsIgnoreCase("Overlay")) {
                        cutOfmap1 = processOverLayJson(generateChartJsonForOverLay(pbretObj, chartDetails.getViewIds(), chartDetails.getMeassureIds(), reportMeta.getChartData().get(chart).getViewIds().size()));
                    } else {
//                    list = generateChartJson(pbretObj,colNames,colIds,reportMeta.getChartData().get(chart).getViewIds().size(),session);
                    }
//                    List<Map<String, String>> list = generateChartJson(pbretObj,colNames,colIds);
                    if (reportMeta.getChartType() != null && (reportMeta.getChartType().equalsIgnoreCase("Fish-Eye") || reportMeta.getChartType().equalsIgnoreCase("tree-map") || reportMeta.getChartType().equalsIgnoreCase("CoffeeWheel"))) {
                        List<Map<String, List<Double>>> listData = generateChartJsonHie(pbretObj, colNames, colIds, colIds1, colNames1, meaIds, meaName1);
                        Map<String, List<String>> combineList = combineChunks(listData, meaName1);
                        Map mapData = processHirarchiJson(combineList);
                        return gson.toJson(mapData);
                    }
                    if (chartDetails.getViewBys().get(0).equalsIgnoreCase("Year") || chartDetails.getViewBys().get(0).toString().toLowerCase().contains("month") || chartDetails.getViewBys().get(0).toString().trim().toLowerCase().contains("time")) {
                        DateComperator dateComperator = new DateComperator(chartDetails.getViewBys().get(0).toString(), "asc", null);
                        Collections.sort(list, dateComperator);
                    } else {
                        JsonComperator jsonComperator = new JsonComperator(chartDetails.getMeassures().get(0), "desc", "");
                        if (chart.equalsIgnoreCase("chart1") && reportMeta.getVisualChartType() != null && reportMeta.getCurrType() != null && reportMeta.getVisualChartType().get(reportMeta.getCurrType()).equalsIgnoreCase("Overlay")) {
                        } else {
                            Collections.sort(list, jsonComperator);
                        }
                    }
                    List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
                    if (reportMeta.getVisualChartType() != null && reportMeta.getChartType() != null && (reportMeta.getChartType().equalsIgnoreCase("bubble-dashboard") || reportMeta.getChartType().equalsIgnoreCase("multi-view-bubble"))) {
                        for (int f = 0; f < (list.size() < 200 ? list.size() : 200); f++) {
                            dataList.add(list.get(f));
                        }
                    }
                    if (reportMeta.getVisualChartType() != null && reportMeta.getVisualChartType() != null && reportMeta.getCurrType() != null && reportMeta.getVisualChartType().get(reportMeta.getCurrType()).equalsIgnoreCase("Overlay")) {
                        if (chart.equalsIgnoreCase("chart1")) {
                            cutOfmap.put(chart, cutOfmap1);
                        } else {
                            cutOfmap.put(chart, list);
                        }
                    } else {
                        if (reportMeta.getChartType() != null && (reportMeta.getChartType().equalsIgnoreCase("bubble-dashboard") || reportMeta.getChartType().equalsIgnoreCase("multi-view-bubble"))) {
                            dataMap.put(chart, dataList);
                        } else {
                            dataMap.put(chart, list);
                        }
                    }
                } catch (Exception ex) {
                    logger.error("Exception:", ex);
                }
            }

        }

        if (reportMeta.getVisualChartType() != null && reportMeta.getVisualChartType().get(reportMeta.getCurrType()) != null && reportMeta.getVisualChartType().get(reportMeta.getCurrType()).equalsIgnoreCase("Overlay")) {
            Type dataType = new TypeToken<OverlayData>() {
            }.getType();
            OverlayData cutData = gson.fromJson(gson.toJson(cutOfmap), dataType);
            return gson.toJson(cutOfmap);
        } else {
            return gson.toJson(dataMap);
        }
    }

    //end of code
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

    public String GroupedMeasureCheck(String measureId) throws SQLException {
        String query = "";
        Gson gson = new Gson();
        PbDb pbDb = new PbDb();
        ProgenAOQuery obj = new ProgenAOQuery();
        measureId = obj.removeComparisonType(measureId);
        PbReturnObject retObj = new PbReturnObject();
query +="select CHILD_ELEMENT_ID,SEQUENCE_NUMBER from PRG_USER_ELEMENTS_HIERARCHY where PERENT_ELEMENT_ID ="+measureId+" ORDER BY SEQUENCE_NUMBER " ;
        retObj = pbDb.execSelectSQL(query);
        List<String> childMeasures = new ArrayList<>();
        if (retObj != null && retObj.getRowCount() >= 1) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                childMeasures.add(retObj.getFieldValueString(i, "CHILD_ELEMENT_ID"));
            }
        }
        return gson.toJson(childMeasures);

    }
public String ParentGroupedMeasuresCheck() throws SQLException{
String query = "";
Gson gson = new Gson();
PbDb pbDb = new PbDb();
ProgenAOQuery obj = new ProgenAOQuery();
//measureId = obj.removeComparisonType(measureId);
PbReturnObject retObj = new PbReturnObject();
query +="select distinct(PERENT_ELEMENT_ID) from PRG_USER_ELEMENTS_HIERARCHY";
retObj = pbDb.execSelectSQL(query);
List<String> groupMeasures = new ArrayList<>();
if(retObj !=null && retObj.getRowCount()>=1){
for(int i=0;i<retObj.getRowCount();i++){
groupMeasures.add(retObj.getFieldValueString(i, "PERENT_ELEMENT_ID"));
}
}
return gson.toJson(groupMeasures);

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

    private void setGlobalValuetoReportMeta(HttpSession session, XtendReportMeta reportMeta, DashboardChartData chartDetails) {
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

    public ArrayList getTimeClauseForGoAgg(HashMap<String, List> drillmapFromSet, HashMap<String, List> drillmapFromSetAgg, PbReportCollection collect, XtendReportMeta reportMeta, DashboardChartData chartDetails) {
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
        System.out.println("===========================================================drillmapfromSettttttttttttt========" + drillmapFromSetAgg.toString());

//        if(collect !=null){
        String measureId = removeComparisonType(chartDetails.getMeassureIds().get(0));
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++timeVIewBY" + timeViewBy);
        if (timeViewBy.equalsIgnoreCase("Month")) {

            end_date = objQuery.getMonthDrill(drillValue.replace(" ", "").trim(), reportMeta, measureId);
        } else if (timeViewBy.equalsIgnoreCase("Qtr")) {
            end_date = objQuery.getQtrDrill(drillValue.replace(" ", "").trim(), reportMeta, measureId);
        } else if (timeViewBy.equalsIgnoreCase("Year")) {
            end_date = objQuery.getYearDrill(drillValue.replace(" ", "").trim(), reportMeta, measureId);
        }
        clause = objQuery.getClauseForDrill(collect, measureId, end_date);
        System.out.println("********Clause******************");
        System.out.println(clause);
//        }
        return clause;
    }

    public String getTimeDimensionView(String keyAggId, XtendReportMeta reportMeta) {
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

    //changed by Mohit for go query
    public PbReturnObject getReturnObjectBasedOnChart(String reportid, String chartid, String query, String fileLocation, String userid) {
        PbReturnObject pbretObj = null;
        List<Object> pbretObjlist = null;
        String oldquery = "";
        Map<String, String> allqueries = new LinkedHashMap<>();
        List<Map<String, String>> list = null;
        File file1 = null;
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
                    if (allqueries.get("QUERY" + i).toString().equalsIgnoreCase(query)) {
                        datafile = new File(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + chartid + "/" + "QUERY" + i + ".txt");
                        FileInputStream fis = new FileInputStream(datafile);
                        ObjectInputStream in = new ObjectInputStream(fis);
                        pbretObj = (PbReturnObject) in.readObject();
// metaString = readWrite.loadJSON(fileLocation+"/AO_Data/"+userid+"/"+reportid+"/"+chartid +"/"+"QUERY"+i+".json");
//           list = gson.fromJson(metaString, new TypeToken<List<Map<String, String>>>(){}.getType());
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
    
     public PbReturnObject getReturnObjectBasedOnLov(String reportid,String elmntid,String query,String fileLocation,String userid) {
        PbReturnObject pbretObj = null;
         List<Object> pbretObjlist=null;
         String oldquery="";
         Map<String,String> allqueries=new LinkedHashMap<>();
         List<Map<String, String>> list=null;
                   File  file1 = null;    
try {
   File datafile = new File(fileLocation+"/Lov_Data/"+userid+"/"+reportid+"/"+elmntid + "/"+elmntid+".json");
        if (datafile.exists()) {
            FileReadWrite readWrite = new FileReadWrite();
            String metaString = readWrite.loadJSON(fileLocation+"/Lov_Data/"+userid+"/"+reportid+"/"+elmntid + "/"+elmntid+".json");
            Gson gson = new Gson();
            Type tarType = new TypeToken<Map<String,String>>() {
            }.getType();
           allqueries = gson.fromJson(metaString, tarType);
           for(int i=1;i<=allqueries.size();i++)
           {
                if(allqueries.get("QUERY"+i).toString().replaceAll("\\W", "").equalsIgnoreCase(query.replaceAll("\\W", ""))){
                   datafile=new File(fileLocation+"/Lov_Data/"+userid+"/"+reportid+"/"+elmntid +"/"+"QUERY"+i+".txt");  
                    FileInputStream fis = new FileInputStream(datafile);
                ObjectInputStream in=new ObjectInputStream(fis);  
  pbretObj =(PbReturnObject)in.readObject();  
// metaString = readWrite.loadJSON(fileLocation+"/AO_Data/"+userid+"/"+reportid+"/"+chartid +"/"+"QUERY"+i+".json");
//           list = gson.fromJson(metaString, new TypeToken<List<Map<String, String>>>(){}.getType());
           break;
}
           }
               
            }
} catch (Exception ex) {
//                Logger.getLogger(PbReportViewerBD.class.getName()).log(Level.SEVERE, null, ex);
           ex.printStackTrace();
            }
        return pbretObj;
    }
    
//changed by Mohit for go query

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

      public boolean setReturnObjectBasedOnLov(String reportid,String elmntid,String query,PbReturnObject resultSet,String fileLocation,String userid) throws FileNotFoundException{
        FileReadWrite fileReadWrite = new FileReadWrite();
        File file;
        File datafile;
        Gson gson = new Gson();
        Map<String,String> allqueries=new LinkedHashMap<>();
        file = new File(fileLocation+"/Lov_Data/"+userid+"/"+reportid+"/"+elmntid);
        String path = file.getAbsolutePath();
        String fName = path + File.separator + elmntid + ".json";
        File f1 = new File(path);
        try{
        File file1 = new File(fName);
        f1.mkdirs();
         datafile = new File(fileLocation+"/Lov_Data/"+userid+"/"+reportid+"/"+elmntid +"/"+elmntid+ ".json");
        if (!datafile.exists()) {
            try {
                datafile.createNewFile();
            } catch (IOException ex) {
//                Logger.getLogger(PbReportViewerBD.class.getName()).log(Level.SEVERE, null, ex);

            }
            allqueries.put("QUERY1",query);
             fileReadWrite.writeToFile(fileLocation+"/Lov_Data/"+userid+"/"+reportid+"/"+elmntid +"/"+elmntid+ ".json", gson.toJson(allqueries));
//fileReadWrite.writeToFile(fileLocation+"/AO_Data/"+userid+"/"+reportid+"/"+chartid +"/"+"QUERY"+(allqueries.size())+ ".json", gson.toJson(list));
file1 = new File(fileLocation+"/Lov_Data/"+userid+"/"+reportid+"/"+elmntid +"/"+"QUERY"+(allqueries.size())+ ".txt");
        file1.createNewFile();
        FileOutputStream fout=new FileOutputStream(file1);  
  ObjectOutputStream out=new ObjectOutputStream(fout);  
  out.writeObject(resultSet);  
  out.flush();        
        }else
        {
            FileReadWrite readWrite = new FileReadWrite();
            String metaString = readWrite.loadJSON(fileLocation+"/Lov_Data/"+userid+"/"+reportid+"/"+elmntid +"/"+elmntid+ ".json");
            Type tarType = new TypeToken<Map<String,String>>() {
            }.getType();
           allqueries = gson.fromJson(metaString, tarType);
            if(allqueries.size()<50)
           {
               allqueries.put("QUERY"+(allqueries.size()+1),query);
                fileReadWrite.writeToFile(fileLocation+"/Lov_Data/"+userid+"/"+reportid+"/"+elmntid +"/"+elmntid+ ".json", gson.toJson(allqueries));
//fileReadWrite.writeToFile(fileLocation+"/AO_Data/"+userid+"/"+reportid+"/"+chartid +"/"+"QUERY"+(allqueries.size())+ ".json", gson.toJson(list));
         file1 = new File(fileLocation+"/Lov_Data/"+userid+"/"+reportid+"/"+elmntid +"/"+"QUERY"+(allqueries.size())+ ".txt");
        file1.createNewFile();
        FileOutputStream fout=new FileOutputStream(file1);  
  ObjectOutputStream out=new ObjectOutputStream(fout);  
  out.writeObject(resultSet);  
  out.flush();
           }
        }
        }
        catch(Exception e){
            e.getMessage();
            e.printStackTrace();
            return false;
        }
   return true;

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

    private List<Map<String, String>> generateCrossTabData(PbReturnObject pbretObj, List<String> paramNames, List<String> paramIds, int size, HttpSession session, ArrayList<String> timeDetails) {
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
        /*Ashutosh*/
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

    private ArrayList<String> customTimechart(DashboardChartData chartDetails) { // add by mayank sharma for custom time on chart
        ArrayList<String> customdatetype = new ArrayList<String>();
        customdatetype.add("Day");
        customdatetype.add("PRG_STD");
        customdatetype.add(chartDetails.getCustomTimeDate());
        customdatetype.add(chartDetails.getCustomTimeflag());
//        if (chartDetails != null) {
        if (chartDetails.getCustomTimeflag() != null && chartDetails.getCustomTimeflag().equalsIgnoreCase("Month")) {
            customdatetype.add("Last Month");
        } else if (chartDetails.getCustomTimeflag() != null && chartDetails.getCustomTimeflag().equalsIgnoreCase("Qtr")) {
            customdatetype.add("Last Qtr");
        } else if (chartDetails.getCustomTimeflag() != null && chartDetails.getCustomTimeflag().equalsIgnoreCase("Year")) {
            customdatetype.add("Last Year");
}
          return customdatetype;
       } 
     }
