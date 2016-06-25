package com.progen.reportview.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.db.ProgenDataSet;
import com.progen.report.*;
import com.progen.report.query.PbReportQuery;
import com.progen.report.query.PbTimeRanges;
import com.progen.report.query.ProgenAOQuery;
import com.progen.report.query.QueryExecutor;
import com.progen.report.template.TemplateMeta;
import com.progen.user.SessionListener;
import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.OneViewLetDetails;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.ProgenTimeDefinition;
import utils.db.ProgenConnection;

/**
 *
 * @author Gopesh
 */
public class JsonGenerator {

    public static Logger logger = Logger.getLogger(JsonGenerator.class);
    //Added by Ashutosh

    static {
        XtendAdapter.savePoint = true;
    }
    private XtendReportMeta reportMeta;
    Gson gson = new Gson();

    public String generateChartData(Container container, String userId) {
        //Gson gson = new Gson();
        List viewBys = container.getViewByColNames();
        List viewIds = container.getViewByElementIds();
//        List aggregation = new ArrayList<String>();
        List measureId = new ArrayList<String>();
        FileReadWrite fileReadWrite = new FileReadWrite();
        PbReportCollection collect = container.getReportCollect();
        for (int i = 0; i < container.getDisplayColumns().size(); i++) {
            measureId.add(container.getDisplayColumns().get(i).toString().replaceAll("A_", ""));
//            aggregation.add(container.getAggregationType(container.getDisplayColumns().get(i).replaceAll("A_", "")));
        }
        List measures = (List) nonOverLap(viewBys, container.getDisplayLabels());
        List measuresIds = (List) nonOverLap(viewIds, measureId);
        List<Map<String, String>> chartJson = generateChartJson(container);
        Map<String, String> nameMap = generateIDMap(container.getDisplayLabels(), container.getDisplayColumns());
        Map chartData = new HashMap();

        String filePath = "/usr/local/cache/" + collect.reportName + ".json";
        String datafilePath = "/usr/local/cache/" + collect.reportName + "_data.json";
        File file = new File(filePath);
        File datafile = new File(datafilePath);
        XtendReportMeta reportMeta = new XtendReportMeta();
        String meta = "";
        String data = "";
        Type tarType = new TypeToken<HashMap<String, List<Map<String, String>>>>() {
        }.getType();
        Type metaType = new TypeToken<XtendReportMeta>() {
        }.getType();
        if (file.exists()) {
            meta = fileReadWrite.loadJSON(filePath);
            chartData.put("meta", meta);
            reportMeta = gson.fromJson(meta, metaType);
        } else {
            reportMeta.setViewbys(viewBys);
            reportMeta.setViewbysIds(viewIds);
            reportMeta.setMeasures(measures);
            reportMeta.setMeasuresIds(measuresIds);
            chartData.put("meta", gson.toJson(reportMeta));
        }
        if (datafile.exists()) {
            data = fileReadWrite.loadJSON(datafilePath);
            HashMap<String, List<Map<String, String>>> chartData1 = gson.fromJson(data, tarType);
            container.setChartData(chartData1);
            chartData.put("chartData", data);
        } else {
            HashMap<String, List<Map<String, String>>> chartData1 = new HashMap<String, List<Map<String, String>>>();
            chartData1.put("chart1", chartJson);
            chartData.put("chartData", chartJson);
            container.setChartData(chartData1);
        }

//        chartData.put("chartData", chartJson);
        chartData.put("nameMap", nameMap);
        chartData.put("viewBys", viewBys);
        chartData.put("measures", measures);
        chartData.put("measuresIds", measuresIds);
        chartData.put("viewIds", viewIds);
        chartData.put("chartMeta", meta);

        return gson.toJson(chartData);
    }

    public String generateoneviewChartData(Container container, String userId, String regid) {
        //Gson gson = new Gson();
        List viewBys = container.getViewByColNames();
        FileReadWrite fileReadWrite = new FileReadWrite();
        PbReportCollection collect = container.getReportCollect();
// List<Map<String, String>> chartJson =new  List<Map<String, String>>;
        List<Map<String, String>> chartJson = new ArrayList<Map<String, String>>();
        List measures = (List) nonOverLap(viewBys, container.getDisplayLabels());

        chartJson = generateChartJson(container);

        Map<String, String> nameMap = generateIDMap(container.getDisplayLabels(), container.getDisplayColumns());
        Map chartData = new HashMap();

        String filePath = "/usr/local/cache/" + collect.reportName + ".json";
        String datafilePath = "/usr/local/cache/" + collect.reportName + "_data.json";
        File file = new File(filePath);
        File datafile = new File(datafilePath);
        String meta = "";
        String data = "";
        Type tarType = new TypeToken<HashMap<String, List<Map<String, String>>>>() {
        }.getType();
        if (file.exists()) {
            meta = fileReadWrite.loadJSON(filePath);
        }
        if (datafile.exists()) {
            data = fileReadWrite.loadJSON(datafilePath);
            HashMap<String, List<Map<String, String>>> chartData1 = gson.fromJson(data, tarType);
            container.setChartData(chartData1);
            chartData.put("chartData", data);
        } else {
            HashMap<String, List<Map<String, String>>> chartData1 = new HashMap<String, List<Map<String, String>>>();
            chartData1.put("chart" + regid, chartJson);
//            chartData.put("chartData", chartJson);
            chartData.put("Dashlets-" + regid, chartJson);
            container.setChartData(chartData1);
        }
        chartData.put("nameMap", nameMap);
        chartData.put("viewBys", viewBys);
        chartData.put("measures", measures);
        chartData.put("chartMeta", meta);





        return gson.toJson(chartData);
    }

    public String generatedataJson1(Container container, String userId, String reportId, String[] viewBys, String[] rowViewNamesArr, String[] measBys, String[] rowMeasNamesArr, String chartId, String[] AggType, String fromoneview, ReportObjectMeta goMeta, OneViewLetDetails detail, String InnerViewbyEleId, String fileLocation, String goUpdate) {
        PbReportCollection collect = container.getReportCollect();
//     ReportTemplateDAO rep = new ReportTemplateDAO();
//     String bizzRoleId = container.getReportCollect().reportBizRoles[0];
//        String bizzRoleName = collect.getReportBizRoleName(bizzRoleId);

//      String filePath = "/usr/local/cache/"+bizzRoleName.replaceAll("\\W", "").trim()+"/"+collect.reportName.replaceAll("\\W", "").trim()+"_"+collect.reportId+"/"+collect.reportName.replaceAll("\\W", "").trim() +"_"+collect.reportId+"_data.json";
//        String datafilePath = "/usr/local/cache/"+bizzRoleName.replaceAll("\\W", "").trim()+"/"+collect.reportName.replaceAll("\\W", "").trim()+"/"+collect.reportName.replaceAll("\\W", "").trim()+"_"+collect.reportId+".csv";

//     File file = new File(filePath);
//        File datafile = new File(datafilePath);

//        if (file.exists() && datafile.exists()) {
//
//        }
//        else{
        QueryExecutor qryExec = new QueryExecutor();
//        ProgenDataSet pbretObj = null;
        ProgenDataSet filterObj = null;
        PbReportQuery reportQuery = null;
//          if(fromoneview!=null && fromoneview.equalsIgnoreCase("true")){
////          reportQuery = qryExec.formulateQueryoneview(goMeta, userId,reportId);
//          }else{
        reportQuery = qryExec.formulateQuery(collect, userId);
//        }
        ArrayList viewBysList = new ArrayList();
        ArrayList viewBysNameList = new ArrayList();
        ArrayList queryCols = new ArrayList();
        ArrayList measBysList = new ArrayList();
        ArrayList measBysListNames = new ArrayList();
        ArrayList aggregationType = new ArrayList();
        for (int i = 0; i < viewBys.length; i++) {
            if (viewBys[i] != null && !viewBys[i].equalsIgnoreCase("undefined")) {
                viewBysList.add(viewBys[i]);
            }
        }
        for (int i = 0; i < measBys.length; i++) {
            measBysList.add(measBys[i].toString().replaceAll("A_", ""));
        }
        for (int i = 0; i < rowMeasNamesArr.length; i++) {
            measBysListNames.add(rowMeasNamesArr[i]);
        }
        for (int i = 0; i < AggType.length; i++) {
            aggregationType.add(AggType[i]);
        }
        reportQuery.setRowViewbyCols(viewBysList);


//        reportQuery.setQryColumns(queryCols);
        reportQuery.setColViewbyCols(queryCols);
        reportQuery.setmeasBys(new ArrayList());
        reportQuery.setmeasBysNames(new ArrayList());
        reportQuery.setmeasBys(measBysList);
        reportQuery.setmeasBysNames(measBysListNames);
        reportQuery.setQryColumns(measBysList); //by Nazneen on 30 April 2015
        reportQuery.setColAggration(aggregationType); //by Harsh
        reportQuery.setaggregationType(aggregationType);
        reportQuery.filePath = container.getFilesPath();
        reportQuery.reportQryElementIds = measBysList;
        reportQuery.isQueryForGO = true;
        reportQuery.setNeedZeroRowQuery(false);
        if ((container.checkGOflag.equalsIgnoreCase("true") && !container.checkGOflag.equalsIgnoreCase("")) || goUpdate.trim().equalsIgnoreCase("Complete Data Set")) {
            reportQuery.avoidProGenTime = true;
        }
        if (goUpdate != null && goUpdate.trim().equalsIgnoreCase("6_Month")) {
            reportQuery.goTimeType = "6month(";
        } else if (goUpdate != null && goUpdate.trim().equalsIgnoreCase("3_Month")) {
            reportQuery.goTimeType = "3month(";
        } else if (goUpdate != null && goUpdate.trim().equalsIgnoreCase("1_Year")) {
            reportQuery.goTimeType = "1year(";
        } else if (goUpdate != null && goUpdate.trim().equalsIgnoreCase("2_Year")) {
            reportQuery.goTimeType = "2year(";
        } else {
            reportQuery.goTimeType = "ReportTime";
        }

        if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
            // reportQuery.oneviewcustomtime = true;

            reportQuery.setTimeDetails(detail.getCustomTimeDetails());

        } else {

            reportQuery.setTimeDetails(container.getTimeDetailsArray());
        }
        reportQuery.InnerViewbyEleId = InnerViewbyEleId;

        String query = null;
        try {
            query = reportQuery.generateViewByQry();
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        PbReturnObject rs = null;
        Connection conn = null;
        PbDb pbdb = new PbDb();
//        boolean tableFlag = false;
        String tableName = "";
        if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
            tableName = "R_GO_" + detail.getOneviewId() + "_" + detail.getNoOfViewLets();
        } else {
            tableName = "R_GO_" + collect.reportId;
        }
        conn = ProgenConnection.getInstance().getConnectionForElement(measBys[0].toString().replace("A_", ""));
//
//       String qry = "Select COLUMN_NAME  from user_tab_columns where table_name='" + tableName + "'";
//            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                qry = "SELECT  COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='" + tableName + "'";
//            }
//        try {
//            rs = pbdb.execSelectSQL(qry, conn);
//            if (rs != null && rs.getRowCount() > 0) {
//                tableFlag=true;
//            }
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//        }


//        String finalQuery = "drop table R_GO_"+collect.reportId+" create table R_GO_"+collect.reportId+" as "+reportQuery.finalSql_AO;

//        pbretObj = qryExec.executeQuery(collect, filterQuery, false);

        //  generating filters
//       ArrayList<String> colViewIds = new ArrayList<String>();
//       ArrayList<String> rowViewIds = new ArrayList<String>();
//       for(int i=0;i<viewBys.length;i++){
//        viewBysList.add(viewBys[i]);
//        }
        setReportObject(collect, reportQuery, container, fromoneview, detail, fileLocation);
        //after check query
        String dropQuery = "";
        try {
            if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
                dropQuery = "drop table R_GO_" + detail.getOneviewId() + "_" + detail.getNoOfViewLets();
            } else {
                dropQuery = "drop table R_GO_" + collect.reportId;
            }
        } catch (Exception e) {
        }
//       ReportObjectQuery objQuery = new ReportObjectQuery();
//       ReportObjectMeta objectMeta = new ReportObjectMeta();
        String objectQuery = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            objectQuery = "select * into  " + tableName + " from (" + reportQuery.finalSqlNew_AO.replace("nvl(", "COALESCE(").replace("Nvl(", "COALESCE(").replace("NVL(", "COALESCE(") + " )a";
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
                objectQuery = "create table R_GO_" + detail.getOneviewId() + "_" + detail.getNoOfViewLets() + "   " + reportQuery.finalSqlNew_AO.replace("nvl(", "ifNULL(").replace("Nvl(", "ifNULL(").replace("NVL(", "ifNULL(");

            } else {
                objectQuery = "create table R_GO_" + collect.reportId + "   " + reportQuery.finalSqlNew_AO.replace("nvl(", "ifNULL(").replace("Nvl(", "ifNULL(").replace("NVL(", "ifNULL(");
            }
        } else {
            if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
                objectQuery = "create table R_GO_" + detail.getOneviewId() + "_" + detail.getNoOfViewLets() + " as " + reportQuery.finalSqlNew_AO;
            } else {
                objectQuery = "create table R_GO_" + collect.reportId + " as " + reportQuery.finalSqlNew_AO;
            }
        }
        try {
            pbdb.execUpdateSQL(dropQuery, conn);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        try {
            conn = ProgenConnection.getInstance().getConnectionForElement(measBys[0].toString().replace("A_", ""));
            String objectQuery1 = objectQuery.replaceAll("null[*]0", "0");
//            String objectQuery1=objectQuery.replaceAll("\"NULL*0\"", "0").replaceAll("\"null*0\"", "0");

            pbdb.execUpdateSQL(objectQuery1, conn);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
//        finally {
//            try {
//                if(conn!=null)
//                conn.close();
//            } catch (SQLException ex) {
//                logger.error("Exception:",ex);
//            }
//        }

//        String filterQuery = "select ";
        StringBuilder filterQuery = new StringBuilder();
        filterQuery.append("select  ");
        for (int k = 0; k < viewBys.length; k++) {
            if (viewBys[k].equalsIgnoreCase("TIME")) {
                if (k == 0) {
//        filterQuery +=viewBys[k];
                    filterQuery.append(viewBys[k]);
                } else {
//            filterQuery += " , "+viewBys[k];
                    filterQuery.append(" , ").append(viewBys[k]);
                }
            } else {
                if (k == 0) {
//                     filterQuery += "A_"+viewBys[k];
                    filterQuery.append("A_").append(viewBys[k]);
                } else {
//            filterQuery += " , A_"+viewBys[k];
                    filterQuery.append(" , A_").append(viewBys[k]);
                }
            }
        }
        if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
//             filterQuery += "  from R_GO_"+detail.getOneviewId()+"_"+detail.getNoOfViewLets();
            filterQuery.append("  from R_GO_").append(detail.getOneviewId()).append("_").append(detail.getNoOfViewLets());
        } else {
//        filterQuery += "  from R_GO_"+collect.reportId;
            filterQuery.append("  from R_GO_").append(collect.reportId).append(" where 1=2");
        }

        Map<String, List<String>> filterMap = new HashMap<String, List<String>>();
//          if(fromoneview!=null && fromoneview.equalsIgnoreCase("true")){
//               filterObj = qryExec.executeQuery1(goMeta, filterQuery, false);
//          }else{
        filterObj = qryExec.executeQuery(collect, filterQuery.toString(), false);
//        }
        for (int m = 0; m < viewBysList.size(); m++) {
            viewBysNameList.add(collect.getElementName(viewBysList.get(m).toString()));
        }
        for (int m = 0; m < filterObj.rowCount; m++) {
            for (int k = 0; k < viewBysList.size(); k++) {
                String val = filterObj.getFieldValueString(m, k).replace(",", "");

                if (filterMap.containsKey(viewBysList.get(k))) {
                    if (filterMap.get(viewBysList.get(k)).indexOf(val) == -1) {
                        filterMap.get(viewBysList.get(k)).add(val);
                    }
                } else {
                    List<String> filterList = new ArrayList<String>();
                    filterList.add(val);
                    filterMap.put((String) viewBysList.get(k), filterList);
                }
            }
        }
        XtendAdapter adapter = new XtendAdapter();
        String filterPath = fileLocation + "/analyticalobject/filters/R_GO_" + reportId;
        if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
            filterPath = fileLocation + "/analyticaloneviewobject/oneview_" + detail.getOneviewId() + "/filters/R_GO_" + detail.getOneviewId() + "_" + detail.getNoOfViewLets();
            if (!filterMap.isEmpty()) {
                adapter.writeFilters(filterMap, viewBysNameList, viewBysList, filterPath, ".json");
            }
        } else {
            adapter.writeFilters(filterMap, viewBysNameList, viewBysList, filterPath, ".json");
        }
//        setReportObject(collect,reportQuery);


//        XtendAdapter adapter = new XtendAdapter();
//        ArrayList nameList = new ArrayList();
//        ArrayList viewList = new ArrayList();
//        ArrayList nameList1 = (ArrayList)(container.getTableHashMap().get("MeasuresNames"));
//        for (String rowViewNamesArr1 : rowViewNamesArr) {
//            nameList.add(rowViewNamesArr1);
//            viewList.add(rowViewNamesArr1);
//        }
//        for (Object nameList11 : nameList1) {
//            nameList.add(nameList11);
//        }
//        XtendReportMeta reportMeta = new XtendReportMeta();
//        reportMeta.setViewbys(viewList);
//        reportMeta.setMeasures(nameList1);
//        reportMeta.setAggregations(collect.reportQryAggregations);
//FileReadWrite readWrite = new FileReadWrite();
//Gson gson = new Gson();
//File file;
//        file = new File("/usr/local/cache/"+bizzRoleName.replaceAll("\\W", "").trim()+"/"+collect.reportName.replaceAll("\\W", "").trim()+"_"+reportId.replaceAll("\\W", "").trim());
// String path = file.getAbsolutePath();
//        String fName = path+File.separator+collect.reportName.replaceAll("\\W", "").trim()+"_"+reportId+"_data.json";
//        File f = new File(path);
//        File file1 = new File(fName);
//        f.mkdirs();
//
//     try {
//                file1.createNewFile();
//     } catch (IOException ex) {
//                logger.error("Exception:",ex);
//     }
//readWrite.writeToFile(filePath, gson.toJson(reportMeta));
//        nameList.addAll(Arrays.asList(rowViewNamesArr));
//        nameList1.stream().forEach(nameList::add);

//       adapter.createCSVForReport(pbretObj,collect.reportId, collect.reportName, nameList,chartId, bizzRoleName);
//       for(int m=0;m<)
//       for (int key = 0; key < rowViewbyNames.size(); key++) {
//                    String val = cells[headers.indexOf(rowViewbyNames.get(key))];
//                    if (filterMap.containsKey(rowViewbyNames.get(key))) {
//                        if (filterMap.get(rowViewbyNames.get(key)).indexOf(val) == -1) {
//                            filterMap.get(rowViewbyNames.get(key)).add(val);
//                        }
//                    } else {
//                        List<String> filterList = new ArrayList<String>();
//                        filterList.add(val);
//                        filterMap.put(rowViewbyNames.get(key), filterList);
//                    }
//                }
//            }
//            String csvFile = "@@" + csvName.replace(".csv", "").replace(".json", "");
//            writeFilters(filterMap, rowViewbyNames, filterPath, csvFile);
//        List<Map<String, String>> chartJson = generateChartJsonForReport(nameList,pbretObj);
//        HashMap<String,List<Map<String, String>>> currChartData = container.getChartData();
//            currChartData.put(chartId,chartJson);
//            container.setChartData(currChartData);

//        }

        return "";
    }

    public List<Map<String, String>> generateChartJson(Container container) {
        List<String> paramNames = container.getDisplayLabels();
        ProgenDataSet pdata = container.getRetObj();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int m = 0; m < pdata.rowCount; m++) {
            Map<String, String> map = new HashMap<String, String>();
            for (int k = 0; k < paramNames.size(); k++) {
                map.put(paramNames.get(k), pdata.getFieldValueString(m, k));
            }
            list.add(map);
        }
        return list;
    }

    public List<Map<String, String>> generateChartJsonForReport(List<String> paramNames, ProgenDataSet pdata) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int m = 0; m < pdata.rowCount; m++) {
            Map<String, String> map = new HashMap<String, String>();
            for (int k = 0; k < paramNames.size(); k++) {
                map.put(paramNames.get(k), pdata.getFieldValueString(m, k));
            }
            list.add(map);
        }
        return list;
    }

    public Map<String, String> generateIDMap(List<String> DisplayNames, List<String> DisplayIDs) {
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < DisplayNames.size(); i++) {
            map.put(DisplayNames.get(i), DisplayIDs.get(i));
        }
        return map;
    }

    Collection intersect(Collection coll1, Collection coll2) {
        List intersection = new ArrayList(coll1);
        intersection.retainAll(new ArrayList(coll2));
        return intersection;
    }

    Collection nonOverLap(Collection coll1, Collection coll2) {
        Collection result = union(coll1, coll2);
        result.removeAll(intersect(coll1, coll2));
        return result;
    }

    Collection union(Collection coll1, Collection coll2) {
        List union = new ArrayList(coll1);
        union.addAll(new ArrayList(coll2));
        return union;
    }
    //sandeep

    public String geroneview(HttpServletRequest request, String reportId, String reportName, Container container, String fromoneview, String busrolename, String userId, OneViewLetDetails detail) {
        String filePath = "/usr/local/cache/" + busrolename.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ".json";
        String goPath = "/usr/local/cache/analyticalobject/R_GO_" + reportId + ".json";
        String metaFilePath = "/usr/local/cache/" + busrolename.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data.json";
//        String datafilePath = "/usr/local/cache/Sales/"+reportName+"_"+reportId+"/"+reportName+"_"+reportId+".csv";
        String metaFilePath1 = "/usr/local/cache/OneviewGO/oneview_" + detail.getOneviewId() + "/oneview_" + detail.getOneviewId() + "_" + detail.getNoOfViewLets() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data.json";
        File metafile1 = new File(metaFilePath);
        FileReadWrite fileReadWrite = new FileReadWrite();

        //Gson gson = new Gson();
        String data = "";
        String gimeta = "";
        String filePath1 = "/usr/local/cache";
        String chartname = detail.getchartname();

        ReportObjectMeta goMeta = new ReportObjectMeta();
        Type goType = new TypeToken<ReportObjectMeta>() {
        }.getType();
        Type goTypedash = new TypeToken<XtendReportMeta>() {
        }.getType();
        try {
            File file = new File(filePath);
            File metafile = new File(metaFilePath);
            if (file.exists() && metafile.exists()) {
                String goData = fileReadWrite.loadJSON(goPath);
                goMeta = gson.fromJson(goData, goType);
                List<String> viewbys = goMeta.getViewIds();
                List<String> viewbys1 = goMeta.getMeasIds();
                List<String> viewbys2 = goMeta.getMeasNames();
                List<String> viewbys3 = goMeta.getAggregations();
                String[] virebys = viewbys.toArray(new String[0]);
                String[] MeasIds = viewbys1.toArray(new String[0]);
                String[] MeasNames = viewbys2.toArray(new String[0]);
                String[] Aggregations = viewbys3.toArray(new String[0]);
                PbReportViewerDAO dao = new PbReportViewerDAO();
                String action = (String) request.getAttribute("savelocal");
                XtendReportMeta reportMeta1 = new XtendReportMeta();
                if (action != null && action.equalsIgnoreCase("savelocal")) {
                    if (metafile1.exists()) {
                        String meta = fileReadWrite.loadJSON(metaFilePath1);
                        reportMeta1 = gson.fromJson(meta, goTypedash);

                    }

                } else {
                    dao.addNewCharts(container, reportId, userId, virebys, null, MeasIds, MeasNames, null, Aggregations, fromoneview, goMeta, detail, "", filePath1, "");
                }
                ReportManagementDAO dao1 = new ReportManagementDAO();
                XtendReportMeta reportMeta = new XtendReportMeta();
                if (file.exists() && metafile.exists()) {
                    gimeta = fileReadWrite.loadJSON(metaFilePath);

                    reportMeta = gson.fromJson(gimeta, goTypedash);
                    if (action != null && action.equalsIgnoreCase("savelocal")) {
                        DashboardChartData reportMet = reportMeta1.getChartData().get(chartname);
                        reportMeta.getChartData().get(chartname).setSize_x(reportMet.getSize_x());
                        reportMeta.getChartData().get(chartname).setSize_y(reportMet.getSize_y());
                        reportMeta.getChartData().get(chartname).setRow(reportMet.getRow());
                        reportMeta.getChartData().get(chartname).setCol(reportMet.getCol());
                    }
                    request.setAttribute("reportMeta", reportMeta);
                    request.setAttribute("chartname", chartname);
                    request.setAttribute("oneviewid", detail.getOneviewId());
                    request.setAttribute("regid", detail.getNoOfViewLets());
                    request.setAttribute("reportId", reportId);

                }
                request.setAttribute("isoneview", "true");

                if (action != null && action.equalsIgnoreCase("savelocal")) {
                } else {
                    data = dao1.getChartsData(request, "/usr/local/cache");
                    Map map = new HashMap();
                    map.put("meta", gimeta);
                    map.put("data", data);
                    reportMeta.setViewbys(goMeta.getViewNames());
                    reportMeta.setViewbysIds(goMeta.getViewIds());
                    reportMeta.setMeasures(goMeta.getMeasNames());
                    reportMeta.setMeasuresIds(goMeta.getMeasIds());
                    reportMeta.setAggregations(goMeta.getAggregations());
                    map.put("meta", gson.toJson(reportMeta));
                    Map<String, Map<String, String>> mapData;
                    Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
                    }.getType();
                    data = gson.toJson(map);
                    Map<String, List<Map<String, String>>> dataMap = new HashMap<String, List<Map<String, String>>>();
                    mapData = gson.fromJson(data, tarType1);
                    dataMap = gson.fromJson(data, tarType1);
//        container.setDbData(mapData);
                    container.setDbrdData(dataMap);

                    container.setReportMeta(reportMeta);
                }
            }
        } catch (Exception e) {
            String report = "false";
        }
// data = gson.toJson(data);
        return data;
    }

    public String geroneviewcharts(String reportId, String reportName, Container container, String oneviewid, String regid) {
        String report = "false";
        String bizzRoleName = "";
//        if(fromoneview!=null && fromoneview.equalsIgnoreCase("true")){
//bizzRoleName=busrolename;
//        }else{
//         String bizzRoleId = container.getReportCollect().reportBizRoles[0];
//       bizzRoleName = container.getReportCollect().getReportBizRoleName(bizzRoleId);
//        }
        String filePath = "/usr/local/cache/OneviewGO/oneview_" + oneviewid + "/oneview_" + oneviewid + "_" + regid + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ".json";
//String goPath = "/usr/local/cache/analyticaloneviewobject/oneview_"+oneviewid+"/R_GO_"+regid+".json";
        String goPath = "/usr/local/cache/analyticalobject/R_GO_" + reportId + ".json";
        String metaFilePath = "/usr/local/cache/OneviewGO/oneview_" + oneviewid + "/oneview_" + oneviewid + "_" + regid + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data.json";
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
                XtendReportMeta reportMeta = new XtendReportMeta();
                ReportObjectMeta goMeta = new ReportObjectMeta();
                Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
                }.getType();
                Type metaType = new TypeToken<XtendReportMeta>() {
                }.getType();
                Type goType = new TypeToken<ReportObjectMeta>() {
                }.getType();
                Map<String, List<Map<String, String>>> dataMap = new HashMap<String, List<Map<String, String>>>();
                mapData = gson.fromJson(data, tarType1);
                dataMap = gson.fromJson(data, tarType1);
                reportMeta = gson.fromJson(meta, metaType);
                goMeta = gson.fromJson(goData, goType);
                reportMeta.setViewbys(goMeta.getViewNames());
                reportMeta.setViewbysIds(goMeta.getViewIds());
                reportMeta.setMeasures(goMeta.getMeasNames());
                reportMeta.setMeasuresIds(goMeta.getMeasIds());
                reportMeta.setAggregations(goMeta.getAggregations());
                map.put("meta", gson.toJson(reportMeta));
//         if(fromoneview!=null && fromoneview.equalsIgnoreCase("true")){
//
//        }else{
//      container.setDbData(mapData);
//      container.setDbrdData(dataMap);
//      container.setReportMeta(reportMeta);
//            }

                report = gson.toJson(map);
            } else {
                report = "false";
            }
        } catch (Exception e) {
            report = "false";
        }
        return report;
    }
    // end by sandeep
    //Edit charts

    public String gerReports(String reportId, String reportName, Container container, String fromoneview, String busrolename, String fileLocation, HttpSession session, HttpServletRequest request) {
        String report = "false";
        String bizzRoleName = "";
        /**
         * *Added by Ashutosh for graphs save points**
         */
//            HttpSession session = SessionListener.getSession(SessionListener.getSessionID());
        String firstPage = "";
        String userId = "";
        if (session != null) {
            if (session.getAttribute("USERID") != null) {
                userId = session.getAttribute("USERID").toString();
            } else {
                userId = XtendAdapter.userID;
            }
        }
        PbReturnObject retobj = null;
        List<String> pageSequqnce = new ArrayList<String>();
        Map<String, Map<String, Map<String, String>>> reportPageMapping = null;
        try {
                String pageMappingQuery = "select * from prg_report_page_mapping_local where report_id='" + reportId + "' and user_id='" + userId + "' order by PAGE_SEQUENCE";
            PbDb pbdb = new PbDb();
            retobj = pbdb.executeSelectSQL(pageMappingQuery);
            reportPageMapping = new HashMap<>();
            if (retobj == null || retobj.getRowCount() == 0) {
                pageMappingQuery = "select * from prg_report_page_mapping where report_id='" + reportId + "'" ;
                retobj = pbdb.executeSelectSQL(pageMappingQuery);
                }
            if (retobj != null && retobj.getRowCount() > 0) {
                Map<String, Map<String, String>> pageMapping = new HashMap<>();
                for (int c = 0; c < retobj.getRowCount(); c++) {
                    Map<String, String> pageInfo = new HashMap<>();
                    String pageId = retobj.getFieldValueString(c, 1);
                    String pageLabel = retobj.getFieldValueString(c, 2);
                    String pageSequence = retobj.getFieldValueString(c, 3);
                    pageSequqnce.add(pageSequence);
                    pageInfo.put("pageLabel", pageLabel);
                    pageInfo.put("pageSequence", pageSequence);
                    pageMapping.put(pageId, pageInfo);
                        if (c == 0) {
                            firstPage = pageId;
                }
                }
                reportPageMapping.put(reportId, pageMapping);
            }
        } catch (Exception ex) {
        }
        if (request.getParameter("pageChange") != null && request.getParameter("pageChange").isEmpty()!=true) {
            firstPage = request.getParameter("pageChange");
        }
        
//            HashMap roleMap = (HashMap) session.getAttribute("roleMap");
        String roleId = "";
//            if(roleMap!=null){
//            roleId = roleMap.get(reportId).toString();
//            }else{
        roleId = container.getReportCollect().reportBizRoles[0];
        String newPage=firstPage;
        if (firstPage.equalsIgnoreCase("default") ||!(new File(fileLocation + "/SavePoint/" + userId + "/" + roleId + "/" + reportId + "/" + reportId + "_data"+firstPage+".json").exists())) {
                firstPage = "";
            }
//            }
        if (XtendAdapter.savePoint && new File(fileLocation + "/SavePoint/" + userId + "/" + roleId + "/" + reportId + "/" + reportId + "_data"+firstPage+".json").exists()) {
            if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
                bizzRoleName = busrolename;
            } else {
                String bizzRoleId = container.getReportCollect().reportBizRoles[0];
                bizzRoleName = container.getReportCollect().getReportBizRoleName(bizzRoleId);
            }

            String filePath = fileLocation + "/SavePoint/" + userId + "/" + roleId + "/" + reportId + "/" + reportId + firstPage + ".json";
            String goPath = fileLocation + "/analyticalobject/R_GO_" + reportId + ".json";
            String metaFilePath = fileLocation + "/SavePoint/" + userId + "/" + roleId + "/" + reportId + "/" + reportId + "_data" + firstPage + ".json";
            String datafilePath = "/usr/local/cache/Sales/" + reportName + "_" + reportId + "/" + reportName + "_" + reportId + ".csv";
            //Gson gson = new Gson();
            FileReadWrite fileReadWrite = new FileReadWrite();
            try {
                File file = new File(filePath);
                File metafile = new File(metaFilePath);
                if (metafile.exists()) {
//        String data = fileReadWrite.loadJSON(filePath);
                    String meta = fileReadWrite.loadJSON(metaFilePath);
                    String goData = fileReadWrite.loadJSON(goPath);
                    Map map = new HashMap();
                    map.put("meta", meta);
                    map.put("data", "");
                    Map<String, Map<String, String>> mapData;
                    XtendReportMeta reportMeta = new XtendReportMeta();
                    ReportObjectMeta goMeta = new ReportObjectMeta();
                    Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
                    }.getType();
                    Type metaType = new TypeToken<XtendReportMeta>() {
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
                    if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
                    } else {
//      container.setDbData(mapData);
//      container.setDbrdData(dataMap);
                        container.setReportMeta(reportMeta);
                    }
                    map.put("pageSequence", pageSequqnce);
                    map.put("currentPage",newPage);
                    report = gson.toJson(map);
                }
            } catch (Exception e) {
                report = "false";
            }
            XtendAdapter.refreshlocal = true;
            return report;

        } else {
            if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
                bizzRoleName = busrolename;
            } else {
                String bizzRoleId = container.getReportCollect().reportBizRoles[0];
                bizzRoleName = container.getReportCollect().getReportBizRoleName(bizzRoleId);
            }
            newPage=firstPage;
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
                    XtendReportMeta reportMeta = new XtendReportMeta();
                    ReportObjectMeta goMeta = new ReportObjectMeta();
                    Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
                    }.getType();
                    Type metaType = new TypeToken<XtendReportMeta>() {
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
                    if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
                    } else {
                        container.setDbData(mapData);
                        container.setDbrdData(dataMap);
                        container.setReportMeta(reportMeta);
                    }
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
                    XtendReportMeta reportMeta = new XtendReportMeta();
                    ReportObjectMeta goMeta = new ReportObjectMeta();
                    Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
                    }.getType();
                    Type metaType = new TypeToken<XtendReportMeta>() {
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
                    if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
                } else {
//                        container.setDbData(mapData);
//                        container.setDbrdData(dataMap);
                        container.setReportMeta(reportMeta);
                    }
                    map.put("currentPage",newPage);
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

    public String getReportsT(String reportId, String reportName, Container container, String fileLocation) {
        String report = "false";
        String bizzRoleId = container.getReportCollect().reportBizRoles[0];
        String bizzRoleName = container.getReportCollect().getReportBizRoleName(bizzRoleId);
        String filePath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_trend.json";
        String goPath = fileLocation + "/analyticalobject/R_GO_" + reportId + ".json";
        String metaFilePath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data_trend.json";
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
                XtendReportMeta reportMeta = new XtendReportMeta();
                ReportObjectMeta goMeta = new ReportObjectMeta();
                Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
                }.getType();
                Type metaType = new TypeToken<XtendReportMeta>() {
                }.getType();
                Type goType = new TypeToken<ReportObjectMeta>() {
                }.getType();
                Map<String, List<Map<String, String>>> dataMap = new HashMap<String, List<Map<String, String>>>();
                mapData = gson.fromJson(data, tarType1);
                dataMap = gson.fromJson(data, tarType1);
                reportMeta = gson.fromJson(meta, metaType);
                goMeta = gson.fromJson(goData, goType);
                reportMeta.setViewbys(goMeta.getViewNames());
                reportMeta.setViewbysIds(goMeta.getViewIds());
                reportMeta.setMeasures(goMeta.getMeasNames());
                reportMeta.setMeasuresIds(goMeta.getMeasIds());
                reportMeta.setAggregations(goMeta.getAggregations());
                map.put("meta", gson.toJson(reportMeta));
                container.setDbData(mapData);
                container.setDbrdData(dataMap);
                container.setReportMeta(reportMeta);

                report = gson.toJson(map);
            } else {
                report = "false";
            }
        } catch (Exception e) {
            report = "false";
        }
        return report;
    }

    public String getVisuals(String reportId, String reportName, Container container, String fileLocation) {
        String report = "false";
        String bizzRoleId = container.getReportCollect().reportBizRoles[0];
        String bizzRoleName = container.getReportCollect().getReportBizRoleName(bizzRoleId);
        //Gson gson = new Gson();
        FileReadWrite fileReadWrite = new FileReadWrite();
        String key = "";
        String infoPath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_visualData.json";
        try {
            File file1 = new File(infoPath);
            if (file1.exists()) {
                String dataInfo = fileReadWrite.loadJSON(infoPath);
                Type tarInfo = new TypeToken<Map<String, String>>() {
                }.getType();
                Map<String, String> infoMap;
                infoMap = gson.fromJson(dataInfo, tarInfo);
                Set keySet = infoMap.keySet();
                Iterator itr = keySet.iterator();
                int count = 0;
                while (count < 1 && itr.hasNext()) {
                    key = itr.next().toString();
                    count++;
                }
                String goPath = fileLocation + "/analyticalobject/R_GO_" + reportId + ".json";
                String filePath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + key + ".json";
                String metaFilePath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_" + key + ".json";
//        String datafilePath = "/usr/local/cache/Sales/"+reportName+"_"+reportId+"/"+reportName+"_"+reportId+".csv";

//     try{
                File file = new File(filePath);
                File metafile = new File(metaFilePath);
                if (file.exists() && metafile.exists()) {
                    String data = fileReadWrite.loadJSON(filePath);
                    String meta = fileReadWrite.loadJSON(metaFilePath);
                    String goData = fileReadWrite.loadJSON(goPath);
                    Map map = new HashMap();
                    map.put("meta", meta);
                    map.put("data", data);
                    map.put("infoMap", infoMap);
                    Type metaType = new TypeToken<XtendReportMeta>() {
                    }.getType();
                    XtendReportMeta reportMeta = new XtendReportMeta();
                    reportMeta = gson.fromJson(meta, metaType);
                    if (reportMeta.getCurrType() != null && reportMeta.getVisualChartType().get(reportMeta.getCurrType()).equalsIgnoreCase("overlay")) {
                        Map<String, Map<String, String>> mapData;

                        ReportObjectMeta goMeta = new ReportObjectMeta();
                        Type tarType1 = new TypeToken<Map>() {
                        }.getType();

                        Type goType = new TypeToken<ReportObjectMeta>() {
                        }.getType();
//        OverlayData dataMap= new HashMap<OverlayData>();
                        Type type = new TypeToken<OverlayData>() {
                        }.getType();
//        mapData = gson.fromJson(data, tarType1);
                        OverlayData dataMap = gson.fromJson(data, type);

                        goMeta = gson.fromJson(goData, goType);
                        reportMeta.setViewbys(goMeta.getViewNames());
                        reportMeta.setViewbysIds(goMeta.getViewIds());
                        reportMeta.setMeasures(goMeta.getMeasNames());
                        reportMeta.setMeasuresIds(goMeta.getMeasIds());
                        reportMeta.setAggregations(goMeta.getAggregations());
                        map.put("meta", gson.toJson(reportMeta));
//            container.setDbData(mapData);
                        container.setOverlayData(dataMap);
                    } else {
                        Map<String, Map<String, String>> mapData;
                        ReportObjectMeta goMeta = new ReportObjectMeta();
                        Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
                        }.getType();
                        Type goType = new TypeToken<ReportObjectMeta>() {
                        }.getType();
                        Map<String, List<Map<String, String>>> dataMap = new HashMap<String, List<Map<String, String>>>();
                        if (reportMeta.getChartType().equalsIgnoreCase("tree-map") || reportMeta.getChartType().equalsIgnoreCase("CoffeeWheel") || reportMeta.getChartType().equalsIgnoreCase("fish-eye") || reportMeta.getChartType().equalsIgnoreCase("tree-map-single")) {
                            Map<String, Map<String, List<String>>> mapData1 = new HashMap<String, Map<String, List<String>>>();
                            Type tarType2 = new TypeToken<Map<String, Map<String, List<String>>>>() {
                            }.getType();
//          mapData1 = gson.fromJson(data, tarType2);
//         container.setTreeData(mapData1);
                        } else {
                            mapData = gson.fromJson(data, tarType1);
                            dataMap = gson.fromJson(data, tarType1);
                            container.setDbData(mapData);
                            container.setDbrdData(dataMap);
                        }
                        reportMeta = gson.fromJson(meta, metaType);
                        goMeta = gson.fromJson(goData, goType);
                        reportMeta.setViewbys(goMeta.getViewNames());
                        reportMeta.setViewbysIds(goMeta.getViewIds());
                        reportMeta.setMeasures(goMeta.getMeasNames());
                        reportMeta.setMeasuresIds(goMeta.getMeasIds());
                        reportMeta.setAggregations(goMeta.getAggregations());
                        map.put("meta", gson.toJson(reportMeta));

                    }
                    report = gson.toJson(map);
                } else {
                    report = "false";
                }
            }
        } catch (Exception e) {
            report = "false";
        }
        return report;
    }

    public String getVisualsChange(String reportId, String reportName, Container container, String visual, String fileLocation) {
        String report = "false";
        String bizzRoleId = container.getReportCollect().reportBizRoles[0];
        String bizzRoleName = container.getReportCollect().getReportBizRoleName(bizzRoleId);
        //Gson gson = new Gson();
        FileReadWrite fileReadWrite = new FileReadWrite();
        String key = visual;
        String infoPath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_visualData.json";
        try {
            File file1 = new File(infoPath);
            if (file1.exists()) {
                String dataInfo = fileReadWrite.loadJSON(infoPath);
                Type tarInfo = new TypeToken<Map<String, String>>() {
                }.getType();
                Map<String, String> infoMap;
                infoMap = gson.fromJson(dataInfo, tarInfo);
                Set keySet = infoMap.keySet();
//                         Iterator itr = keySet.iterator();
//                            int count=0;
//                            while(count<1 && itr.hasNext()){
//                                key = itr.next().toString();
//                    count++;
//                            }
                String goPath = fileLocation + "/analyticalobject/R_GO_" + reportId + ".json";
                String filePath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + key.trim() + ".json";//changed by sruthi for ie9
                String metaFilePath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_" + key.trim() + ".json";//changed by sruthi for ie9
//        String datafilePath = "/usr/local/cache/Sales/"+reportName+"_"+reportId+"/"+reportName+"_"+reportId+".csv";

//     try{
                File file = new File(filePath);
                File metafile = new File(metaFilePath);
                if (file.exists() && metafile.exists()) {
                    String data = fileReadWrite.loadJSON(filePath);
                    String meta = fileReadWrite.loadJSON(metaFilePath);
                    String goData = fileReadWrite.loadJSON(goPath);
                    Map map = new HashMap();
                    map.put("meta", meta);
                    map.put("data", data);
                    map.put("infoMap", infoMap);
                    Type metaType = new TypeToken<XtendReportMeta>() {
                    }.getType();
                    XtendReportMeta reportMeta = new XtendReportMeta();
                    reportMeta = gson.fromJson(meta, metaType);
                    if (reportMeta.getCurrType() != null && reportMeta.getVisualChartType().get(reportMeta.getCurrType()).equalsIgnoreCase("overlay")) {
                        Map<String, Map<String, String>> mapData;

                        ReportObjectMeta goMeta = new ReportObjectMeta();
                        Type tarType1 = new TypeToken<Map>() {
                        }.getType();

                        Type goType = new TypeToken<ReportObjectMeta>() {
                        }.getType();
//        OverlayData dataMap= new HashMap<OverlayData>();
                        Type type = new TypeToken<OverlayData>() {
                        }.getType();
//        mapData = gson.fromJson(data, tarType1);
                        OverlayData dataMap = gson.fromJson(data, type);

                        goMeta = gson.fromJson(goData, goType);
                        reportMeta.setViewbys(goMeta.getViewNames());
                        reportMeta.setViewbysIds(goMeta.getViewIds());
                        reportMeta.setMeasures(goMeta.getMeasNames());
                        reportMeta.setMeasuresIds(goMeta.getMeasIds());
                        reportMeta.setAggregations(goMeta.getAggregations());
                        map.put("meta", gson.toJson(reportMeta));
//            container.setDbData(mapData);
                        container.setOverlayData(dataMap);
                    } else {
                        Map<String, Map<String, String>> mapData;
                        ReportObjectMeta goMeta = new ReportObjectMeta();
                        Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
                        }.getType();
                        Type goType = new TypeToken<ReportObjectMeta>() {
                        }.getType();
                        Map<String, List<Map<String, String>>> dataMap = new HashMap<String, List<Map<String, String>>>();
                        if (reportMeta.getChartType().equalsIgnoreCase("tree-map") || reportMeta.getChartType().equalsIgnoreCase("CoffeeWheel") || reportMeta.getChartType().equalsIgnoreCase("fish-eye") || reportMeta.getChartType().equalsIgnoreCase("tree-map-single")) {
                            Map mapData1 = new HashMap();
                            Type tarType2 = new TypeToken<Map>() {
                            }.getType();
//          mapData1 = gson.fromJson(data, tarType2);
//         container.setTreeData(mapData1);
                        } else {
                            mapData = gson.fromJson(data, tarType1);
                            dataMap = gson.fromJson(data, tarType1);
                            container.setDbData(mapData);
                            container.setDbrdData(dataMap);
                        }
                        reportMeta = gson.fromJson(meta, metaType);
                        goMeta = gson.fromJson(goData, goType);
                        reportMeta.setViewbys(goMeta.getViewNames());
                        reportMeta.setViewbysIds(goMeta.getViewIds());
                        reportMeta.setMeasures(goMeta.getMeasNames());
                        reportMeta.setMeasuresIds(goMeta.getMeasIds());
                        reportMeta.setAggregations(goMeta.getAggregations());
                        map.put("meta", gson.toJson(reportMeta));

                    }

                    container.setReportMeta(reportMeta);

                    report = gson.toJson(map);
                } else {
                    report = "false";
                }
            }
        } catch (Exception e) {
            report = "false";
        }
        return report;
    }

    public String getLocalChart(String reportId, String reportName, String chartId, Container container, String type, String fileLocation, String userId1, HttpServletRequest request) {
        String report = "false";
        String bizzRoleId = container.getReportCollect().reportBizRoles[0];
        String bizzRoleName = container.getReportCollect().getReportBizRoleName(bizzRoleId);
        String filePath = "";
        String metaFilePath = "";
        String currentPage = "";
        if (request.getSession(false) != null && request.getSession(false).getAttribute("currentPage") != null) {
            currentPage = request.getSession(false).getAttribute("currentPage").toString();
        }
        if(currentPage !=null && currentPage.equalsIgnoreCase("")){
            currentPage = request.getParameter("currentPage");
        }
       
        if (type != null && type.equalsIgnoreCase("trend")) {
            filePath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_trend.json";
            metaFilePath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data_trend.json";
        } else {
            HttpSession session = SessionListener.getSession(SessionListener.getSessionID());
            String userId = userId1;
            String roleId = bizzRoleId;
             if(currentPage.equalsIgnoreCase("default")||!(new File(fileLocation + "/SavePoint/" + userId + "/" + roleId + "/" + reportId + "/" + reportId + "_data"+currentPage+".json").exists())){
            currentPage = "";
            }
            if (XtendAdapter.refreshlocal && new File(fileLocation + "/SavePoint/" + userId + "/" + roleId + "/" + reportId + "/" + reportId + "_data"+currentPage+".json").exists()) {
                filePath = fileLocation + "/SavePoint/" + userId + "/" + roleId + "/" + reportId + "/" + reportId + currentPage + ".json";

                metaFilePath = fileLocation + "/SavePoint/" + userId + "/" + roleId + "/" + reportId + "/" + reportId + "_data" + currentPage + ".json";
            } else {
                filePath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + currentPage + ".json";
                metaFilePath = fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data" + currentPage + ".json";
            }
        }
        //        String datafilePath = "/usr/local/cache/Sales/"+reportName+"_"+reportId+"/"+reportName+"_"+reportId+".csv";
        //Gson gson = new Gson();
        FileReadWrite fileReadWrite = new FileReadWrite();
        try {
            File file = new File(filePath);
            File metafile = new File(metaFilePath);
            if (metafile.exists()) {
//        String data = fileReadWrite.loadJSON(filePath);
                String meta = fileReadWrite.loadJSON(metaFilePath);
                Map<String, Map<String, String>> mapData;
//        Map<String, String> mapData1 = new HashMap<String,String>();
                XtendReportMeta reportMeta = new XtendReportMeta();
                Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
                }.getType();
                Type metaType = new TypeToken<XtendReportMeta>() {
                }.getType();
//        mapData = gson.fromJson(data, tarType1);
//              mapData1 =mapData.get(chartId);
                reportMeta = gson.fromJson(meta, metaType);
//        mapData.put(chartId,mapData.get(chartId));
//      DashboardChartData reportMet=container.getReportMeta().getChartData().get(chartId);
//        mapData.put(chartId,mapData1);
//      container.setDbData(mapData);
                container.getReportMeta().getChartData().put(chartId, reportMeta.getChartData().get(chartId));
//      container.setReportMeta(reportMeta);
                Map map = new HashMap();
                map.put("meta", container.getReportMeta());
//        map.put("data",container.getDbData());
                report = gson.toJson(map);
            } else {
                report = "false";
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
            report = "false";
        }
        return report;
    }

    public String trendAnalysisAction(String reportId, Container container, String PbUserId, String[] rowviewBy, String[] rowviewId) {
        PbReportCollection collect = container.getReportCollect();
        //Gson gson = new Gson();
        QueryExecutor qryExec = new QueryExecutor();
        ProgenDataSet pbretObj = null;
        PbReportQuery reportQuery = null;
        reportQuery = qryExec.formulateQuery(collect, PbUserId);
        ArrayList viewBysList = new ArrayList();
        ArrayList queryCols = new ArrayList();
        Map jsonData = new HashMap();
        for (int i = 0; i < rowviewBy.length; i++) {
            viewBysList.add(rowviewBy[i]);
        }
//     
        reportQuery.setColViewbyCols(queryCols);
        reportQuery.setRowViewbyCols(viewBysList);
        String query = null;
        try {
            query = reportQuery.generateViewByQry();

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        pbretObj = qryExec.executeQuery(collect, query, false);
        XtendAdapter adapter = new XtendAdapter();
        ArrayList nameList = new ArrayList();
        ArrayList nameList1 = (ArrayList) (container.getTableHashMap().get("MeasuresNames"));

        for (String rowviewId1 : rowviewId) {
            nameList.add(rowviewId1);
        }
        for (Object nameList11 : nameList1) {
            nameList.add(nameList11);
        }

        List<Map<String, String>> JsonList = generateTrendAnalysisJson(nameList, pbretObj);
        jsonData.put("JsonList", JsonList);
        jsonData.put("viewBysList", viewBysList);
        jsonData.put("measures", nameList1);
        return gson.toJson(jsonData);

    }

    public List<Map<String, String>> generateTrendAnalysisJson(ArrayList paramNames, ProgenDataSet pdata) {
//        List<String> paramNames = container.getDisplayLabels();
//        ProgenDataSet pdata = container.getRetObj();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int m = 0; m < pdata.rowCount; m++) {
            Map map = new HashMap();
            for (int k = 0; k < paramNames.size(); k++) {
                map.put(paramNames.get(k), pdata.getFieldValueString(m, k));
            }
            list.add(map);
        }
        return list;
    }

    public String getTrendReports(String reportId, String reportName, Container container) {
        String report = "false";
        String bizzRoleId = container.getReportCollect().reportBizRoles[0];
        String bizzRoleName = container.getReportCollect().getReportBizRoleName(bizzRoleId);
        String filePath = "/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_trend.json";
        String goPath = "/usr/local/cache/analyticalobject/R_GO_" + reportId + ".json";
        String metaFilePath = "/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data_trend.json";
//        String datafilePath = "/usr/local/cache/Sales/"+reportName+"_"+reportId+"/"+reportName+"_"+reportId+".csv";
        //Gson gson = new Gson();
        FileReadWrite fileReadWrite = new FileReadWrite();
        try {
            File file = new File(filePath);
            File metafile = new File(metaFilePath);
            if (file.exists() && metafile.exists()) {
                String data = fileReadWrite.loadJSON(filePath);
                String meta = fileReadWrite.loadJSON(metaFilePath);
                Map map = new HashMap();
                String goData = fileReadWrite.loadJSON(goPath);
                map.put("meta", meta);
                map.put("data", data);
                Map<String, Map<String, String>> mapData;
                XtendReportMeta reportMeta = new XtendReportMeta();
                ReportObjectMeta goMeta = new ReportObjectMeta();
                Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
                }.getType();
                Type metaType = new TypeToken<XtendReportMeta>() {
                }.getType();
                Type goType = new TypeToken<ReportObjectMeta>() {
                }.getType();
                Map<String, List<Map<String, String>>> dataMap = new HashMap<String, List<Map<String, String>>>();
                mapData = gson.fromJson(data, tarType1);
                dataMap = gson.fromJson(data, tarType1);
                reportMeta = gson.fromJson(meta, metaType);
                reportMeta = gson.fromJson(meta, metaType);
                goMeta = gson.fromJson(goData, goType);
                reportMeta.setViewbys(goMeta.getViewNames());
                reportMeta.setViewbysIds(goMeta.getViewIds());
                reportMeta.setMeasures(goMeta.getMeasNames());
                reportMeta.setMeasuresIds(goMeta.getMeasIds());
                reportMeta.setAggregations(goMeta.getAggregations());
                map.put("meta", gson.toJson(reportMeta));
                container.setDbData(mapData);
                container.setDbrdData(dataMap);
                container.setReportMeta(reportMeta);
                report = gson.toJson(map);
            } else {
                report = "false";
            }
        } catch (Exception e) {
            report = "false";
        }
        return report;
    }

    public String generateTrenddataJson1(Container container, String userId, String reportId, String[] viewBys, String[] rowViewNamesArr, String[] measBys, String[] rowMeasNamesArr, String chartId) {
        PbReportCollection collect = container.getReportCollect();
//     ReportTemplateDAO rep = new ReportTemplateDAO();
        String bizzRoleId = container.getReportCollect().reportBizRoles[0];
        String bizzRoleName = collect.getReportBizRoleName(bizzRoleId);

        String filePath = "/usr/local/cache/" + bizzRoleName + "/" + collect.reportName.replaceAll("\\W", "").trim() + "/" + collect.reportName.replaceAll("\\W", "").trim() + "_" + collect.reportId + "_trend.json";
        String datafilePath = "/usr/local/cache/" + bizzRoleName + "/" + collect.reportName.replaceAll("\\W", "").trim() + "/" + collect.reportName.replaceAll("\\W", "").trim() + "_" + collect.reportId + ".csv";

        File file = new File(filePath);
        File datafile = new File(datafilePath);

//        if (file.exists() && datafile.exists()) {
//
//        }
//        else{
        QueryExecutor qryExec = new QueryExecutor();
        ProgenDataSet pbretObj = null;
        PbReportQuery reportQuery = null;
        reportQuery = qryExec.formulateQuery(collect, userId);
        ArrayList viewBysList = new ArrayList();
        ArrayList queryCols = new ArrayList();
        for (int i = 0; i < viewBys.length; i++) {
            viewBysList.add(viewBys[i]);
        }
//        for(int i=0;i<measBys.length;i++){
//        queryCols.add(measBys[i]);
//        }
        reportQuery.setRowViewbyCols(viewBysList);
//        reportQuery.setQryColumns(queryCols);
        reportQuery.setColViewbyCols(queryCols);
        String query = null;
        try {
            query = reportQuery.generateViewByQry();
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        pbretObj = qryExec.executeQuery(collect, query, false);
        XtendAdapter adapter = new XtendAdapter();
        ArrayList nameList = new ArrayList();
        ArrayList nameList1 = (ArrayList) (container.getTableHashMap().get("MeasuresNames"));
        for (String rowViewNamesArr1 : rowViewNamesArr) {
            nameList.add(rowViewNamesArr1);
        }
        for (Object nameList11 : nameList1) {
            nameList.add(nameList11);
        }

//        nameList.addAll(Arrays.asList(rowViewNamesArr));
//        nameList1.stream().forEach(nameList::add);

        adapter.createCSVForTrend(pbretObj, collect.reportId, collect.reportName, nameList, chartId, bizzRoleName);
//        List<Map<String, String>> chartJson = generateChartJsonForReport(nameList,pbretObj);
//        HashMap<String,List<Map<String, String>>> currChartData = container.getChartData();
//            currChartData.put(chartId,chartJson);
//            container.setChartData(currChartData);

//        }

        return "";
    }

    public void setReportObject(PbReportCollection collect, PbReportQuery reportQuery, Container container, String fromoneview, OneViewLetDetails detail, String fileLocation) {
        ReportObjectMeta roMeta = new ReportObjectMeta();

        ArrayList colNames = new ArrayList();
        ArrayList measIDs = new ArrayList();
        ArrayList measNames = new ArrayList();
        ArrayList aggType = new ArrayList();
        //by nazneen for Dimension segment on summarized buckets
        boolean isDimSegOnSumm = reportQuery.isDimSegOnSumm;
        String finalSqlSummarized_AO = "";
        String finalSummarizedGrpBy_AO = "";
        if (isDimSegOnSumm) {
            finalSqlSummarized_AO = reportQuery.finalSqlSummarized_AO;
            finalSummarizedGrpBy_AO = reportQuery.finalSummarizedGrpBy_AO;
        }
        //ended by Nazneen
// if(fromoneview!=null && fromoneview.equalsIgnoreCase("true")){
// for(int i=0;i<reportQuery.getRowViewbyCols().size();i++){
//     String elementId=reportQuery.getRowViewbyCols().get(i).toString();
//      ArrayList paraInfo = new ArrayList();
//        String NextElementId = null;
//        if (elementId != null && elementId.equalsIgnoreCase("Time")) {
//            NextElementId = "Time";
//        } else {
////            paraInfo = (ArrayList) goMeta.getreportParameters().get(elementId);
//            if (paraInfo != null) {
//                if (paraInfo.get(1) != null) {
//                    NextElementId = paraInfo.get(1).toString();
//                }
//            }
//
//        }
//
//     colNames.add(NextElementId);
//}
//
// }else{
        for (int i = 0; i < reportQuery.getRowViewbyCols().size(); i++) {
            colNames.add(collect.getElementName(reportQuery.getRowViewbyCols().get(i).toString()));
        }
//     }
        int count = 0;
        ArrayList measureColNames1 = new ArrayList();
//[458239, 458242]
        ArrayList maeasureColNames = new ArrayList();
//  if(fromoneview!=null && fromoneview.equalsIgnoreCase("true")){
// for(int i=0;i<reportQuery.getmeasBys().size();i++){
//     if(goMeta.getReportQryElementsIds().contains(reportQuery.getmeasBys().get(i).toString().replace("A_", "").toString())){
//    measIDs.add(reportQuery.getmeasBys().get(i).toString().replace("A_", "").toString());
//     }
// else{
//          measIDs.add( goMeta.getReportQryElementsIds());
//}
////     if(goMeta.getMeasNames().get(i).toString().replace("\"", "").toString()){
//    measNames.add(reportQuery.getmeasBysNames().get(i).toString().replace("\"", "").toString());
////     }
//// else{
//// measNames.add(collect.reportQryColNames);
//// }
//     if(goMeta.getReportQryAggregations().contains(reportQuery.getaggregationType().get(i).toString().replace("\"", "").toString())){
//    aggType.add(reportQuery.getaggregationType().get(i).toString().replace("\"", "").toString());
//     }
// else{
// aggType.add(goMeta.getReportQryAggregations());
// }
// }
//  }else{
        for (int i = 0; i < reportQuery.getmeasBys().size(); i++) {
            if (collect.reportQryElementIds.contains(reportQuery.getmeasBys().get(i).toString().replace("A_", "").toString())) {
                measIDs.add(reportQuery.getmeasBys().get(i).toString().replace("A_", "").toString());
                measNames.add(reportQuery.getmeasBysNames().get(i).toString().replace("\"", "").toString());
            } else if (container.isSummarizedMeasuresEnabled() && !reportQuery.getmeasBys().get(i).toString().contains("A_")) {
                HashMap<String, ArrayList<String>> summarizedmMesMap = container.getSummerizedTableHashMap();

                if (summarizedmMesMap != null && !summarizedmMesMap.isEmpty()) {
                    measIDs.add((String) summarizedmMesMap.get("summerizedQryeIds").get(count));
                    measNames.add((String) summarizedmMesMap.get("summerizedQryColNames").get(count));
//                                    summerizedQryAggregations.addAll((List<String>) summarizedmMesMap.get("summerizedQryAggregations"));
//                                    summerizedQryColTypes.addAll((List<String>) summarizedmMesMap.get("summerizedQryColTypes"));
                }
                count++;
            } else {
                measIDs.add(collect.reportQryElementIds.get(i));
                measNames.add(collect.reportQryColNames.get(i));
            }
            if (collect.reportQryAggregations.contains(reportQuery.getaggregationType().get(i).toString().replace("\"", "").toString())) {
                aggType.add(reportQuery.getaggregationType().get(i).toString().replace("\"", "").toString());
            } else if (container.isSummarizedMeasuresEnabled()) {
                HashMap summarizedmMesMap = container.getSummerizedTableHashMap();
                if (summarizedmMesMap != null && !summarizedmMesMap.isEmpty()) {
                    aggType.addAll((List<String>) summarizedmMesMap.get("summerizedQryAggregations"));
                }
            } else {
                aggType.add(collect.reportQryAggregations.get(i));
            }
        }

//            roMeta.setFinalSql_AO(reportQuery.finalSql_AO);
        roMeta.setOsql_AO(reportQuery.osql_AO);
        roMeta.setOViewByCol_AO(reportQuery.OViewByCol_AO);
        roMeta.setOorderByCol_AO(reportQuery.OorderByCol_AO);
        roMeta.setOmsql_AO(reportQuery.omsql_AO);
        roMeta.setOmViewByCol_AO(reportQuery.OmViewByCol_AO);
        roMeta.setOmorderByCol_AO(reportQuery.OmorderByCol_AO);
        roMeta.setColOrderByCol_AO(reportQuery.ColOrderByCol_AO);
        roMeta.setOsqlGroup_AO(reportQuery.osqlGroup_AO);
        roMeta.setFinalViewByCol_AO(reportQuery.finalViewByCol_AO);
        roMeta.setoWrapper_AO(reportQuery.oWrapper_AO);
        roMeta.setTableName_AO(reportQuery.tableName_AO);
        roMeta.setFilters_AO(reportQuery.filters_AO);
        roMeta.setIsAOEnable(reportQuery.isAOEnable);
        roMeta.setViewIds(reportQuery.getRowViewbyCols());
        roMeta.setViewNames(colNames);
        roMeta.setMeasIds(measIDs);
        roMeta.setMeasNames(measNames);
//            roMeta.setMeasIds(collect.reportQryElementIds);
//            roMeta.setMeasNames(collect.reportQryColNames);
        roMeta.setAggregations(aggType);
        roMeta.setFinalSql_AO(reportQuery.finalSql_AO);
        roMeta.setFinalSqlNew_AO(reportQuery.finalSqlNew_AO);
        roMeta.setFinalSqlSummarized_AO(reportQuery.finalSqlSummarized_AO);
        roMeta.setFinalSummarizedGrpBy_AO(reportQuery.finalSummarizedGrpBy_AO);
        roMeta.setIsDimSegOnSumm(reportQuery.isDimSegOnSumm);
        roMeta.setOViewByColGrp_AO(reportQuery.OViewByColGrp_AO);

        //Gson gson = new Gson();
        FileReadWrite fileReadWrite = new FileReadWrite();

        File datafile;
        File file;
        String fName;
        String path;
        if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
            datafile = new File(fileLocation + "/analyticaloneviewobject/oneview_" + detail.getOneviewId() + "/R_GO_" + detail.getNoOfViewLets() + ".json");
            file = new File(fileLocation + "/analyticaloneviewobject/oneview_" + detail.getOneviewId() + "");

            path = file.getAbsolutePath();
            fName = path + File.separator + "R_GO_" + detail.getNoOfViewLets() + ".csv";
        } else {
            datafile = new File(fileLocation + "/analyticalobject/R_GO_" + collect.reportId + ".json");

            file = new File(fileLocation + "/analyticalobject");


            path = file.getAbsolutePath();
            fName = path + File.separator + "R_GO_" + collect.reportId + ".csv";
        }
        File f = new File(path);
        File file1 = new File(fName);
        f.mkdirs();
        if (!datafile.exists()) {
            try {
                datafile.createNewFile();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        //for read Report meta
        if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
            fileReadWrite.writeToFile(fileLocation + "/analyticaloneviewobject/oneview_" + detail.getOneviewId() + "/R_GO_" + detail.getNoOfViewLets() + ".json", gson.toJson(roMeta));
        } else {
            fileReadWrite.writeToFile(fileLocation + "/analyticalobject/R_GO_" + collect.reportId + ".json", gson.toJson(roMeta));
        }
//       ReportObjectQuery repQuery = new ReportObjectQuery();
//       String objquery = repQuery.getObjectQuery(collect.reportId);
//       
    }

    public void saveGraphJson(String newReportId, String newReportName, String oldReportId, String oldReportName, String bizzRoleName, String flag, String fileLocation) {

        File srcFolder = new File(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + oldReportName.replaceAll("\\W", "").trim() + "_" + oldReportId);
        File destFolder = new File(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + newReportName.replaceAll("\\W", "").trim() + "_" + newReportId);

        //make sure source exists
        if (!srcFolder.exists()) {


            //just exit
//         return;
            return;

        } else {

            try {
                copyFolder(srcFolder, destFolder, newReportName, newReportId, oldReportName, oldReportId, flag);
            } catch (IOException e) {
                logger.error("Exception:", e);
                //error, just exit
               return;
            }
        }



    }

    public void saveGOJson(String newReportId, String newReportName, String oldReportId, String oldReportName, String bizzRoleName, String flag, String fileLocation) {

        File srcFolder = new File(fileLocation + "/analyticalobject");
        File destFolder = new File(fileLocation + "/analyticalobject");

        //make sure source exists
        if (!srcFolder.exists()) {


            //just exit
          return;

        } else {

            try {
                copyFolder(srcFolder, destFolder, newReportName, newReportId, oldReportName, oldReportId, flag);
            } catch (IOException e) {
                logger.error("Exception:", e);
                //error, just exit
              return;
            }
        }


    }

    public void savefilterJson(String newReportId, String newReportName, String oldReportId, String oldReportName, String bizzRoleName, String flag, String fileLocation) {

        File srcFolder = new File(fileLocation + "/analyticalobject/filters/R_GO_" + oldReportId + "");
        File destFolder = new File(fileLocation + "/analyticalobject/filters/R_GO_" + newReportId + "");

        //make sure source exists
        if (!srcFolder.exists()) {


            //just exit
          return;

        } else {

            try {
                copyFolder(srcFolder, destFolder, newReportName, newReportId, oldReportName, oldReportId, flag);
            } catch (IOException e) {
                logger.error("Exception:", e);
                //error, just exit
              return;
            }
        }


    }

    public static void copyFolder(File src, File dest, String newReportName, String newReportId, String oldReportName, String oldReportId, String flag)
            throws IOException {

        if (src.isDirectory()) {

            //if directory not exists, create it
            if (!dest.exists()) {
                dest.mkdir();
                System.out.println("Directory copied from "
                        + src + "  to " + dest);
            }

            //list all the directory contents
            File srcFile;
            File destFile;
            String files[] = src.list();
            if (flag != null && !flag.isEmpty() && flag.equalsIgnoreCase("graph")) {
                for (String file : files) {

                    String fileTo = file.replaceAll(oldReportName.replaceAll("\\W", "").trim() + "_" + oldReportId, newReportName.replaceAll("\\W", "").trim() + "_" + newReportId);
//                     fileTo = newReportName.replaceAll("\\W","").trim()+"_"+newReportId+"_data.json";
                    //construct the src and dest file structure
                    srcFile = new File(src, file);
                    destFile = new File(dest, fileTo);
                    //recursive copy
                    copyFolder(srcFile, destFile, newReportName, newReportId, oldReportName, oldReportId, flag);
                }
                }else if(flag != null && !flag.isEmpty() && flag.equalsIgnoreCase("goTable")){
                 srcFile = new File(src, "R_GO_"+oldReportId+".json");
                 destFile = new File(dest, "R_GO_"+newReportId+".json");
                 copyFolder(srcFile,destFile,newReportName,newReportId,oldReportName, oldReportId,flag);
                } 
                else if(flag !=null && !flag.isEmpty() && flag.equalsIgnoreCase("filter")){

                for (String file : files) {
                    srcFile = new File(src, file);
                    destFile = new File(dest, file);

                    copyFolder(srcFile, destFile, newReportName, newReportId, oldReportName, oldReportId, flag);
                }

            }
                else if(flag !=null && !flag.isEmpty() && flag.equalsIgnoreCase("savepoint")){

                    for( String file : files){
                        srcFile = new File(src, file);
                        destFile = new File(dest, file.replaceAll(oldReportId, newReportId));

                        copyFolder(srcFile, destFile,newReportName,newReportId,oldReportName, oldReportId, flag);
                    }

                }

    	}
        
        else{
            //if file, then copy it
            //Use bytes stream to support all file types
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();

        }
    }

    public String updateSequence(String sequence, String roleName, String reportId, String reportName) {
        try {
            FileReadWrite fileReadWrite = new FileReadWrite();
            String infoPath = "/usr/local/cache/" + roleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_visualData.json";
            fileReadWrite.writeToFile(infoPath, sequence);
            return "success";
        } catch (Exception e) {
            return "failer";
        }
    }
// added by manik

    public String gerReportsLogin(String reportId, String reportName, String bizzRoleName, String fromLogin, String username, HttpSession session, String currentTab, String graphId, String chartId) {
        String report = "false";
//         String bizzRoleId = container.getReportCollect().reportBizRoles[0];
//        String bizzRoleName = container.getReportCollect().getReportBizRoleName(bizzRoleId);
//        String bizzRoleName = bizzRoleName;

        String filePath = "/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ".json";
        String goPath = "/usr/local/cache/analyticalobject/R_GO_" + reportId + ".json";
        String metaFilePath = "/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data.json";
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
                XtendReportMeta reportMeta = new XtendReportMeta();
                ReportObjectMeta goMeta = new ReportObjectMeta();
                Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
                }.getType();
                Type metaType = new TypeToken<XtendReportMeta>() {
                }.getType();
                Type goType = new TypeToken<ReportObjectMeta>() {
                }.getType();
                Map<String, List<Map<String, String>>> dataMap = new HashMap<String, List<Map<String, String>>>();
                mapData = gson.fromJson(data, tarType1);
                dataMap = gson.fromJson(data, tarType1);
                reportMeta = gson.fromJson(meta, metaType);
                goMeta = gson.fromJson(goData, goType);
                reportMeta.setViewbys(goMeta.getViewNames());
                reportMeta.setViewbysIds(goMeta.getViewIds());
                reportMeta.setMeasures(goMeta.getMeasNames());
                reportMeta.setMeasuresIds(goMeta.getMeasIds());
                reportMeta.setAggregations(goMeta.getAggregations());
                map.put("meta", gson.toJson(reportMeta));
//      container.setDbData(mapData);
//      container.setDbrdData(dataMap);
//      container.setReportMeta(reportMeta);
                Map<String, Map<String, Map<String, WallData>>> wallData = (Map<String, Map<String, Map<String, WallData>>>) session.getAttribute("wallData");
                WallData wall = new WallData();
//wall.setData(dataMap.get("chart1"));//chart selection manik
                wall.setData(dataMap.get(chartId));//chart selection manik
                wall.setReportMeta(reportMeta);
                if (session.getAttribute("wallData") != null) {
                    wallData = (Map<String, Map<String, Map<String, WallData>>>) session.getAttribute("wallData");
                }
                if (wallData.get(currentTab) != null) {
                    wallData.get(currentTab).get("graphs").put(graphId, wall);
                } else {
                    wallData.put(currentTab, new HashMap<String, Map<String, WallData>>());
                    wallData.get(currentTab).put("graphs", new HashMap<String, WallData>());
                    wallData.get(currentTab).get("graphs").put(graphId, wall);
                }
                session.setAttribute("wallData", wallData);
//         LoginReportMeta chartDetails = new LoginReportMeta();
//        Map<String,XtendReportMeta> metaDetails = new HashMap<String,XtendReportMeta>();
//        Map<String,Map<String,List<Map<String, String>>>> dataDetails = new HashMap<String,Map<String,List<Map<String, String>>>>();
//        metaDetails.put("reportMeta",reportMeta);
//        dataDetails.put("data",dataMap);
//        chartDetails.setReportMeta((XtendReportMeta) reportMeta);
//        chartDetails.setData((List<Map<String, String>>) dataMap.get("chart1"));
//
//        Map<String,LoginReportMeta> loginDataDetails = new HashMap<String,LoginReportMeta>();
//        if(session.getAttribute("loginDataDetails")!=null){
//            Type sessionDataType = new TypeToken<LoginReportMeta>(){ }.getType();
//            
//        loginDataDetails = gson.fromJson(String.valueOf(session.getAttribute("loginDataDetails")),sessionDataType);
//        }
//        loginDataDetails.put("chart1",chartDetails);
//
//        session.setAttribute("loginDataDetails",gson.toJson(loginDataDetails));


                report = gson.toJson(map);
            } else {
                report = "false";
            }
        } catch (Exception e) {
            report = "false";
        }

        return report;

    }

    public String getWallReports(String userName, HttpSession session) {
        String report = "";
        String data = "";
        String tagData = "";

        String filePath = "/usr/local/cache/loginPageCharts/tag_master.json";
        String tagfilePath = "/usr/local/cache/loginPageCharts/tag_details.json";
//     String filePath = "/usr/local/cache/loginPageCharts/"+userName+".json";

        //Gson gson = new Gson();
        FileReadWrite fileReadWrite = new FileReadWrite();
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Type wallType = new TypeToken<Map<String, Map<String, Map<String, WallData>>>>() {
                }.getType();
                data = fileReadWrite.loadJSON(filePath);
                tagData = fileReadWrite.loadJSON(tagfilePath);
                Map<String, Map<String, Map<String, WallData>>> wallData = gson.fromJson(data, wallType);
                session.setAttribute("wallData", wallData);
                Map map = new HashMap();
                map.put("data", data);
                map.put("tagData", tagData);
                report = gson.toJson(map);
            } else {
                report = "false";
            }
        } catch (Exception e) {
            report = "false";
        }

        return report;
    }

    public String generateFilter(Container container, List<String> viewBys, String reportName, String reportId, String bizzRoleName, String filePath) {
        //
        PbReportCollection collect = container.getReportCollect();
        QueryExecutor qryExec = new QueryExecutor();

        ProgenDataSet filterObj = null;
        ProgenAOQuery obj = new ProgenAOQuery();
        ArrayList viewBysList;
        ArrayList viewBysNameList;

//       for(int i=0;i<viewBys.size();i++){
//        viewBysList.add(viewBys.get(i));
//}
        for (int k = 0; k < viewBys.size(); k++) {
            String Ao_Name = "R_GO_" + collect.reportId;
            String filterQuery = "select ";
            ArrayList viewBysListFilter = new ArrayList();
            viewBysList = new ArrayList();
            viewBysNameList = new ArrayList();
            if (viewBys.get(k).equalsIgnoreCase("TIME")) {
                viewBysListFilter.add(viewBys.get(k));
            } else {
                viewBysListFilter.add("A_" + viewBys.get(k));

            }
            viewBysList.add(viewBys.get(k));
            try {
                Ao_Name = obj.aoReplace(Ao_Name, viewBysListFilter);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }

            if (viewBys.get(k).equalsIgnoreCase("TIME")) {
                if (k == 0) {
                    filterQuery += viewBys.get(k);
                } else {
                    filterQuery += " " + viewBys.get(k);
                }
            } else {
                if (k == 0) {
                    filterQuery += "A_" + viewBys.get(k);
                } else {
                    filterQuery += "A_" + viewBys.get(k);
                }
            }

//        if(fromoneview!=null && fromoneview.equalsIgnoreCase("true")){
//             filterQuery += "  from R_GO_"+detail.getOneviewId()+"_"+detail.getNoOfViewLets();
//        }else{
            filterQuery += "  from " + Ao_Name;
//        }

            Map<String, List<String>> filterMap = new HashMap<String, List<String>>();
//          if(fromoneview!=null && fromoneview.equalsIgnoreCase("true")){
//               filterObj = qryExec.executeQuery1(goMeta, filterQuery, false);
//          }else{
            filterObj = qryExec.executeQuery(collect, filterQuery, false);
//        }
            for (int m = 0; m < viewBysList.size(); m++) {
                viewBysNameList.add(collect.getElementName(viewBysList.get(m).toString()));
            }
            for (int m = 0; m < filterObj.rowCount; m++) {
                for (int z = 0; z < viewBysList.size(); z++) {
                    String val = filterObj.getFieldValueString(m, z).replace(",", "");

                    if (filterMap.containsKey(viewBysList.get(z))) {
                        if (filterMap.get(viewBysList.get(z)).indexOf(val) == -1) {
                            filterMap.get(viewBysList.get(z)).add(val);
                        }
                    } else {
                        List<String> filterList = new ArrayList<String>();
                        filterList.add(val);
                        filterMap.put((String) viewBysList.get(z), filterList);
                    }
                }
            }
            XtendAdapter adapter = new XtendAdapter();
            String filterPath = filePath + "/analyticalobject/filters/R_GO_" + reportId;
//           if(fromoneview!=null && fromoneview.equalsIgnoreCase("true")){fileLocation
//               filterPath = "/usr/local/cache/analyticaloneviewobject/oneview_"+detail.getOneviewId()+"/filters/R_GO_"+detail.getOneviewId()+"_"+detail.getNoOfViewLets();
//           }
            adapter.writeFilters(filterMap, viewBysNameList, viewBysList, filterPath, ".json");
        }
        return "";
    }

    public String GTKPICalculateFunction(HttpServletRequest request, String repId, String[] measId, String[] measName, String[] aggType, String chartIdRequest, String timeClauseGO, String dimSecurityClause, Container container) throws SQLException {
        Connection conn = null;
        //Gson gson = new Gson();
//        String query="";
//        String query1="";
//        String query2="";
//        String query3="";
//        String whereClause="";
//        String actualColFormula="";
//        String Aggregation="";
//        String refferedElement="";
        double result;
        String strresult = "";
        String aoAsGoId = request.getParameter("aoAsGoId");
        String timeClause1 = timeClauseGO;
        HttpSession session = request.getSession(false);
        String userid = String.valueOf(session.getAttribute("USERID"));
         PbReportViewerDAO dao = new PbReportViewerDAO();
        String fileLocation = "";
         if (session != null) {
                fileLocation = dao.getFilePath(session);
            } else {
                fileLocation = "/usr/local/cache";
            }
//        List<String> measNameList = new ArrayList<>();
//        List<String> measIdList = new ArrayList<>();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//        String userColName = "";
        String filterClause = "";
        ArrayList<String> timeDetailsGO = new ArrayList<String>();
        ArrayList<String> timeDetails = new ArrayList<String>();
        if (container != null) {
            timeDetails = container.getTimeDetailsArray();
            for (int i = 0; i < timeDetails.size(); i++) {
                timeDetailsGO.add(i, timeDetails.get(i));
            }

        }

        PbReturnObject retObj = new PbReturnObject();
        ProgenTimeDefinition timeDefObj = ProgenTimeDefinition.getInstance(repId, container,userid);
//        PbReturnObject retObj1=new PbReturnObject();
//        PbReturnObject retObj2=new PbReturnObject();
        PbDb pbdb = new PbDb();
        String AO_Name = "";
        if (aoAsGoId != null && !aoAsGoId.equalsIgnoreCase("null") && !aoAsGoId.equalsIgnoreCase("")) {

            AO_Name = "M_AO_" + aoAsGoId;
        } else {

            AO_Name = "R_GO_" + repId;
        }
        String[] elementId = measId;
        reportMeta = new XtendReportMeta();
        ReportManagementDAO repManagementDao = new ReportManagementDAO();
        reportMeta = repManagementDao.setReportForm(request);
        String action = request.getParameter("action");
        String fromoneview = request.getParameter("fromoneview");
        Map<String, List<String>> drillMap = new HashMap<String, List<String>>();
        HashMap<String, List> drillmapFromSet = new HashMap<String, List>();
        HashMap<String, List> drillmapFromSetAgg = new HashMap<String, List>();
        HashMap<String, List> filtermapFromSet = new HashMap<String, List>();
        HashMap<String, List> localfiltermapFromSet = new HashMap<String, List>();
//        HashMap<String, String> ResultSet = new HashMap<String, String>();
        List<String> ResultSet = new ArrayList<String>();
        String id = chartIdRequest;
        DashboardChartData chartDetails = reportMeta.getChartData().get(id);
        if ((reportMeta.getDrills() != null && !reportMeta.getDrills().toString().equalsIgnoreCase("")) || reportMeta.getFilterMap() != null || (reportMeta.getChartData() != null && reportMeta.getChartData().get(chartIdRequest) != null && reportMeta.getChartData().get(chartIdRequest).getFilters() != null) || reportMeta.getChartData().get(chartIdRequest).getLocalDrills() != null) {
            if (reportMeta.getDrills() != null) {
                drillMap = reportMeta.getDrills();
                Set<Entry<String, List<String>>> set = drillMap.entrySet();
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
                            String timeViewBy = repManagementDao.getTimeDimensionView(entry.getKey(), reportMeta);
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

                }
            } else if (reportMeta.getFilterMap() != null) {
                Set<Entry<String, List<String>>> filterset1 = reportMeta.getFilterMap().entrySet();
                for (Entry<String, List<String>> entry : filterset1) {
                    filtermapFromSet.put(entry.getKey(), entry.getValue());
                }
            }
//            if(fromoneview!=null && fromoneview.equalsIgnoreCase("true")){
//                filtermapFromSet = new HashMap<String, List>();
//            if(reportMeta.getFilterMap()!=null){
////            filtermap = reportMeta.getFilterMap();
//    Set<Entry<String, List<String>>> filterset = reportMeta.getFilterMap().entrySet();
//    for(Entry<String, List<String>> entry : filterset)
//    {
//
//            if(action!=null && action.equalsIgnoreCase("drillacross")){
//
//            }else{
//            List<String> viewbyids = new ArrayList(reportMeta.getViewbysIds());
//            for (String viewbyid : viewbyids) {
//                if(viewbyid.equalsIgnoreCase(entry.getKey())){
//             drillmapFromSet.put(entry.getKey(), entry.getValue());
//                }
//            }
//            }
//                }
//        }
//        }
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
                        filtermapFromSetLocal = new HashMap<String, List>();
//             HashMap<String, List> localfiltermapFromSet = new HashMap<String, List>();
                        List<String> listVal = new ArrayList<String>();
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
                                    List<String> listValNew = new ArrayList<String>();
                                    for (int m = 0; m < listVal.size(); m++) {
                                        for (int m1 = 0; m1 < entry1.getValue().size(); m1++) {
                                            if (entry1.getValue().get(m1).toString().equalsIgnoreCase(listVal.get(m).toString())) {
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

//                Map<String, List<String>> filtermapGO = new HashMap<String, List<String>>();
//                filtermapGO = reportMeta.getFilterMap();
//                List filterMap = null;
//                 filtermapGO=(Map<String, List<String>>) request.getAttribute("filterMap");
            HashMap<String, List> likeMap = new HashMap<String, List>();
            HashMap<String, List> notLikeMap = new HashMap<String, List>();
//                 HashMap<String, List> localfiltermapFromSet = new HashMap<String, List>();
            HashMap<String, List> localdrillmap = new HashMap<String, List>();
//                 Map<String, DashboardChartData> chartData;
            ReportObjectQuery objectQuery = new ReportObjectQuery();

            //  HashMap<String, List> filtermapFromSet = new HashMap<String, List>();
//                 chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());
//                 List<String> charts = new ArrayList(chartData.keySet());
//                   for (String chart : charts) {
//                  if(reportMeta.getChartData().get(chartIdRequest)!=null  && (reportMeta.getChartData().get(chartIdRequest).getChartType().equalsIgnoreCase("Table")||reportMeta.getChartData().get(chartIdRequest).getChartType().equalsIgnoreCase("TileChart")||reportMeta.getChartData().get(chartIdRequest).getChartType().equalsIgnoreCase("RadialProgress")||reportMeta.getChartData().get(chartIdRequest).getChartType().equalsIgnoreCase("LiquidFilledGauge")||reportMeta.getChartData().get(chartIdRequest).getChartType().equalsIgnoreCase("Dial-Gauge")||reportMeta.getChartData().get(chartIdRequest).getChartType().equalsIgnoreCase("KPIDash") ||reportMeta.getChartData().get(chartIdRequest).getChartType().equalsIgnoreCase("Emoji-Chart"))){
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
        if (reportMeta.getDrillFormat() != null && reportMeta.getDrillFormat().equalsIgnoreCase("time")) {

            timeClauseGO = "";
        } else {
            if ((reportMeta.getChartData().get(chartIdRequest).getCompleteChartData() != null && reportMeta.getChartData().get(chartIdRequest).getCompleteChartData().equalsIgnoreCase("Yes"))) {
                timeClauseGO = "";
            } else {
                timeClauseGO = timeClause1;
            }

        }
        String check_DDate = "";
        check_DDate = "select DDATE from R_GO_" + repId + " where 1=2";
        conn = (Connection) ProgenConnection.getInstance().getConnectionForElement(removeComparisonType(elementId[0].toString()));
        try {
            retObj = pbdb.execSelectSQL(check_DDate, conn);

        } catch (Exception e) {
        }
        if (retObj == null) {
            timeClauseGO = "";
        }


//                  }

//                   }


//         if(filterClause!=null && filterClause.equalsIgnoreCase(""))
//        whereClause += " where 1=1 " + timeClauseGO;
//        ProgenAOQuery aoQuery=new ProgenAOQuery();
//         if(reportMeta.getChartData().get(chartIdRequest).getCompleteChartData()!=null && reportMeta.getChartData().get(chartIdRequest).getCompleteChartData().equalsIgnoreCase("Yes")){
//               aoQuery.setAvoidProgenTime("Yes");
//           }else{
//               aoQuery.setAvoidProgenTime("No");
//           }
//          if(reportMeta.getChartData().get(chartIdRequest).getglobalEnable()!=null && reportMeta.getChartData().get(chartIdRequest).getglobalEnable().equalsIgnoreCase("Y") && (reportMeta.getFilterMap()!=null && !(reportMeta.getFilterMap().isEmpty())) ){
//            try {
//                AO_Name = aoQuery.aoReplace(AO_Name, new ArrayList(localfiltermapFromSet.keySet()));
//            } catch (SQLException ex) {
//                logger.error("Exception:",ex);
//            }
//                }else if(action!=null && action.equalsIgnoreCase("localfilterGraphs")){
//            try {
//                AO_Name = aoQuery.aoReplace(AO_Name, new ArrayList(localfiltermapFromSet.keySet()));
//            } catch (SQLException ex) {
//                logger.error("Exception:",ex);
//            }
//                }else{
//            try {
//                AO_Name = aoQuery.aoReplace(AO_Name, new ArrayList(filtermapFromSet.keySet()));
//            } catch (SQLException ex) {
//                logger.error("Exception:",ex);
//            }
//                }
        if (timeDefObj != null && chartDetails.getMeassureIds() != null) {
            List<String> measLabel = new ArrayList<>();
            List<String> compMeasLabel = new ArrayList<>();
            for (int i = 0; i < chartDetails.getMeassureIds().size(); i++) {
                String measId1 = chartDetails.getMeassureIds().get(i);
                String measName1 = chartDetails.getMeassures().get(i);
                if (measId != null && timeDefObj.getTimeDefinition() != null) {
                    measLabel.add(repManagementDao.getTimeBasedMeasure(timeDefObj, measName1, measId1, timeDetailsGO));
                } else {
                    measLabel.add(measName1);
                }
            }
            chartDetails.setMeassures(measLabel);
            if (chartDetails.getComparedMeasureId() != null && !chartDetails.getComparedMeasureId().isEmpty()) {
                for (int k = 0; k < chartDetails.getComparedMeasureId().size(); k++) {
                    String comId = chartDetails.getComparedMeasureId().get(k);
                    String comMeas = chartDetails.getComparedMeasure().get(k);
                    if (comId != null && timeDefObj.getTimeDefinition() != null) {
                        compMeasLabel.add(repManagementDao.getTimeBasedMeasure(timeDefObj, comMeas, comId, timeDetailsGO));
                    } else {
                        compMeasLabel.add(comMeas);
                    }
                }
                chartDetails.setComparedMeasure(compMeasLabel);
            }
        }
        ProgenAOQuery aoQuery = new ProgenAOQuery();
        if (reportMeta.getDrillFormat() != null && reportMeta.getDrillFormat().equalsIgnoreCase("time")) {
            timeDetailsGO = new ArrayList<String>();
            timeDetailsGO = repManagementDao.getTimeClauseForGoAgg(drillmapFromSet, drillmapFromSetAgg, container.getReportCollect(), reportMeta, chartDetails);
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
        HashMap<String, List> mapSetForGraphs = new HashMap<String, List>();
        String dontDrill1 = "N";
        if (chartDetails.getExcludeFromDrill() != null) {
            dontDrill1 = chartDetails.getExcludeFromDrill().toString();
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
          List<String> grandTotalAggType = new ArrayList<String>(chartDetails.getMeassureIds().size());
           grandTotalAggType = repManagementDao.addGrandTotalAggType(chartDetails);
        String queryGT = "";
        aoQuery.setAO_name(AO_Name);
//                aoQuery.setTimeDetails(container.getTimeDetailsArray());
       aoQuery.setGrandTotalAggType(grandTotalAggType);
        aoQuery.setViewIdList(null);
        aoQuery.setColViewIdList(null);
        aoQuery.setMeasureIdsList(chartDetails.getMeassureIds());
        aoQuery.setMeasureIdsListGO(reportMeta.getMeasuresIds());
        setTimeTypeForMeasure(chartDetails, aoQuery);
        aoQuery.setFilterClause(filterClause);
        if (chartDetails.getglobalEnable() != null && chartDetails.getglobalEnable().equalsIgnoreCase("Y") && (reportMeta.getFilterMap() != null && !(reportMeta.getFilterMap().isEmpty()))) {
            aoQuery.setFilterMap(mapSetForGraphs);
        } else if (action != null && action.equalsIgnoreCase("localfilterGraphs")) {
            aoQuery.setFilterMap(mapSetForGraphs);
        } else {

            aoQuery.setFilterMap(mapSetForGraphs);
        }
        aoQuery.setDimSecurityClause(dimSecurityClause);
        aoQuery.setAggregationType(chartDetails.getAggregation());
        aoQuery.setInnerViewByElementId("NONE");
        try {
            queryGT = aoQuery.generateAOQuery();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        PbReturnObject pbretObj = null;
            Connection  connect = null;
 pbretObj=repManagementDao.getReturnObjectBasedOnChart(repId, id, queryGT,fileLocation,userid);
            if(pbretObj==null)
              {
        connect = ProgenConnection.getInstance().getConnectionForElement(removeComparisonType(chartDetails.getMeassureIds().get(0)));
        if (connect != null) {
            try {

                pbretObj = pbdb.execSelectSQL(queryGT, connect);
                repManagementDao.setReturnObjectBasedOnChart(repId, id, queryGT,pbretObj,fileLocation,userid);
            }
                catch(Exception e){
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
                list = repManagementDao.generateChartJson(pbretObj, chartDetails.getMeassures(), chartDetails.getMeassureIds(), 0, null, timeDetailsGO);
                KPIMap.put("data", list);
                KPIMap.put("comparedMeasure", chartDetails.getComparedMeasure());
                return gson.toJson(KPIMap);
            } else {
                for (int i = 0; i < pbretObj.rowCount; i++) {
                    for (int k = 0; k < chartDetails.getMeassureIds().size(); k++) {
                        result = Double.parseDouble(pbretObj.getFieldValueString(i, "A_" + chartDetails.getMeassureIds().get(k)));
                        strresult = BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                        ResultSet.add(strresult);
                    }
                }

            }


        }

//                for(int z=0;z<elementId.length;z++){
//            whereClause = "";
//
//            try {
//                timeClauseGO = setTimeType(container.getTimeDetailsArray(), elementId[z]);
//            } catch (SQLException ex) {
//                logger.error("Exception:",ex);
//            }
//
//                  if(reportMeta.getChartData().get(chartIdRequest).getCompleteChartData()!=null && reportMeta.getChartData().get(chartIdRequest).getCompleteChartData().equalsIgnoreCase("N")){
//                      if(filterClause !=null && !filterClause.equalsIgnoreCase("")){
//                          if(reportMeta.getDrillFormat()!=null && reportMeta.getDrillFormat().equalsIgnoreCase("time") ){
//                              whereClause += " where 1=1 "+ filterClause  + dimSecurityClause;
//                          }else{
//
//                            whereClause += " where 1=1 "+ filterClause  + dimSecurityClause + timeClauseGO;
//                          }
//                      }else{
//
//                      whereClause += " where 1=1 "+ filterClause + dimSecurityClause + timeClauseGO;
//                      }
//                      }else if(reportMeta.getChartData().get(chartIdRequest).getCompleteChartData()!=null && reportMeta.getChartData().get(chartIdRequest).getCompleteChartData().equalsIgnoreCase("yes")){
//                          whereClause += " where 1=1 " + filterClause + dimSecurityClause;
//                      }
//                      else {
//                      whereClause += " where 1=1 "+filterClause + dimSecurityClause + timeClauseGO;
//                  }
//
//        if(reportMeta.getChartType()!=null && reportMeta.getChartType().equals("Time-Dashboard")){
//        List<String> timeDims=new ArrayList<String>();
//        timeDims.add("MOM");
//        timeDims.add("PMOM");
//        timeDims.add("QOQ");
//        timeDims.add("PQOQ");
//        timeDims.add("YOY");
//        String timeClause="";
////        Container container = Container.getContainerFromSession(request, repId);
//        ReportObjectQuery objectQuery = new ReportObjectQuery();
//        for(int l=0;l<timeDims.size();l++){
//        timeClause = " where 1=1 "+objectQuery.getCompareClause(container,reportMeta.getMeasuresIds().get(0).toString(),timeDims.get(l));
//        try
//        {
//
//        query="select ref_element_id as REF_ELEMENT_ID, ref_element_type as REF_ELEMENT_TYPE,USER_COL_TYPE as USER_COL_TYPE,REFFERED_ELEMENTS as REFFERED_ELEMENTS,ACTUAL_COL_FORMULA as ACTUAL_COL_FORMULA, USER_COL_NAME as USER_COL_NAME,AGGREGATION_TYPE AS AGGREGATION    from prg_user_all_info_details where element_id ="+aoQuery.removeComparisonType(elementId[z].toString());
//        }
//        catch(Exception ex)
//        {
//        logger.error("Exception:",ex);
//        }
//        try
//        {
//            retObj=pbdb.executeSelectSQL(query);
//            if((retObj.getFieldValueString(0, "REF_ELEMENT_TYPE").equalsIgnoreCase("4"))&&!(retObj.getFieldValueString(0, "USER_COL_TYPE").equalsIgnoreCase("summarized")))
//            {
//
//                query1 = "select element_id as ELEMENT_ID, user_col_name as USER_COL_NAME from prg_user_all_info_details where ref_element_id ="+retObj.getFieldValueString(0, "REF_ELEMENT_ID");
//                ArrayList changeelementId = new ArrayList();
//                query2 = "";
//                double current = 0;
//                double Prior = 0;
//                retObj1=pbdb.executeSelectSQL(query1);
//                query2 += "select ";
//                for(int i=0;i<2;i++){
//                query2 += retObj1.getFieldValueString(i, "USER_COL_NAME");
//                if(i==0){
//                query2 += " , ";
//                }
//               }
//                 query2 += " from "+AO_Name;
//                 query2 += " "+timeClause;
//                conn = (Connection) ProgenConnection.getInstance().getConnectionForElement(aoQuery.removeComparisonType(elementId[z].toString()));
//                retObj2 = pbdb.execSelectSQL(query2, conn);
//                String currentelementId = retObj1.getFieldValueString(0, 0);
//                String priorelementId = retObj1.getFieldValueString(1, 1);
//                for(int k=0;k<retObj2.getRowCount();k++){
//                    if (retObj2.getFieldValueString(k,0 )!=null ){
//                    current += Double.parseDouble(retObj2.getFieldValueString(k,0 ));
//                    }
//                    if (retObj2.getFieldValueString(k,1 )!=null && retObj2.getFieldValueString(k,1 )!=""  ){
//                    Prior += Double.parseDouble(retObj2.getFieldValueString(k, 1));
//                    }
//                 }
//                 result = 0.0;
//               result = (((current-Prior)/Prior)*100);
//               if(result>0.0){
//               strresult=BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
//            }else if(Double.isNaN(result)){
//            strresult = "0.0";
//            }
//                else{
//                strresult=String.valueOf(result);
//                }
//            }
//            else if((!retObj.getFieldValueString(0, "REF_ELEMENT_TYPE").equalsIgnoreCase("4")) && retObj.getFieldValueString(0, "USER_COL_TYPE").equalsIgnoreCase("summarized"))
//            {
//                query = "";
//                refferedElement= retObj.getFieldValueString(0, "REFFERED_ELEMENTS");
//                actualColFormula=retObj.getFieldValueString(0, "ACTUAL_COL_FORMULA");
//
//                query +="select "+actualColFormula+" AS  RESULT from "+AO_Name;
//                query += " "+timeClause;
//                conn = (Connection) ProgenConnection.getInstance().getConnectionForElement(aoQuery.removeComparisonType(elementId[z].toString()));
//                retObj1 = pbdb.execSelectSQL(query, conn);
//                result = 0.0;
//                result=Double.parseDouble(retObj1.getFieldValueString(0, "RESULT"));
//                if(result>0.0){
//                  strresult=BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
//            }else if(Double.isNaN(result)){
//            strresult = "0.0";
//            }
//                else{
//                strresult=String.valueOf(result);
//                }
//            }
//            else if((retObj.getFieldValueString(0, "REF_ELEMENT_TYPE").equalsIgnoreCase("3")) && !(retObj.getFieldValueString(0, "USER_COL_TYPE").equalsIgnoreCase("summarized")))
//            {
//
//                query1 = "select element_id as ELEMENT_ID, user_col_name as USER_COL_NAME from prg_user_all_info_details where ref_element_id ="+retObj.getFieldValueString(0, "REF_ELEMENT_ID");
//                ArrayList changeelementId = new ArrayList();
//                query2 = "";
//                double current = 0;
//                double Prior = 0;
//                retObj1=pbdb.executeSelectSQL(query1);
//                query2 += "select ";
//                for(int i=0;i<2;i++){
//                query2 += retObj1.getFieldValueString(i, "USER_COL_NAME");
//                if(i==0){
//                query2 += " , ";
//                }
//               }
//                 query2 += " from "+AO_Name;
//                 query2 += " "+timeClause;
//                conn = (Connection) ProgenConnection.getInstance().getConnectionForElement(aoQuery.removeComparisonType(elementId[z].toString()));
//                retObj2 = pbdb.execSelectSQL(query2, conn);
//                String currentelementId = retObj1.getFieldValueString(0, 0);
//                String priorelementId = retObj1.getFieldValueString(1, 1);
//                for(int k=0;k<retObj2.getRowCount();k++){
//                    if (retObj2.getFieldValueString(k,0 )!=null ){
//                    current += Double.parseDouble(retObj2.getFieldValueString(k,0 ));
//                    }
//                    if (retObj2.getFieldValueString(k,1 )!=null && retObj2.getFieldValueString(k,1 )!=""  ){
//                    Prior += Double.parseDouble(retObj2.getFieldValueString(k, 1));
//                    }
//                 }
//                 result = 0.0;
//               result = (current-Prior);
//               if(result>0.0){
//               strresult=BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
//            }else if(Double.isNaN(result)){
//            strresult = "0.0";
//            }
//                else{
//                strresult=String.valueOf(result);
//                }
//            }
//            else if((!retObj.getFieldValueString(0, "REF_ELEMENT_TYPE").equalsIgnoreCase("3")) && retObj.getFieldValueString(0, "USER_COL_TYPE").equalsIgnoreCase("summarized"))
//            {
//                query = "";
//                refferedElement= retObj.getFieldValueString(0, "REFFERED_ELEMENTS");
//                actualColFormula=retObj.getFieldValueString(0, "ACTUAL_COL_FORMULA");
//
//                query +="select "+actualColFormula+" AS  RESULT from "+AO_Name;
//                query += " "+timeClause;
//                conn = (Connection) ProgenConnection.getInstance().getConnectionForElement(aoQuery.removeComparisonType(elementId[z].toString()));
//                retObj1 = pbdb.execSelectSQL(query, conn);
//                result = 0.0;
//                result=Double.parseDouble(retObj1.getFieldValueString(0, "RESULT"));
//                if(result>0.0){
//                  strresult=BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
//            }else if(Double.isNaN(result)){
//            strresult = "0.0";
//            }
//                else{
//                strresult=String.valueOf(result);
//                }
//            }
//            else
//            {
//                query = "";
//                conn = (Connection) ProgenConnection.getInstance().getConnectionForElement(aoQuery.removeComparisonType(elementId[z].toString()));
//                userColName=retObj.getFieldValueString(0, "USER_COL_NAME");
//                Aggregation=retObj.getFieldValueString(0, "AGGREGATION");
//                if(Aggregation.toUpperCase().equalsIgnoreCase("COUNTDISTINCT") || Aggregation.toUpperCase().equalsIgnoreCase("COUNT") || Aggregation.contains("#")){
//                Aggregation = "SUM";
//                }
//                query +="select "+Aggregation+"("+userColName+") "+userColName+" from "+AO_Name;
//                query += " "+timeClause;
//                retObj1 = pbdb.execSelectSQL(query, conn);
//                result = 0.0;
//                if(!retObj1.getFieldValueString(0, userColName).equalsIgnoreCase("") && !retObj1.getFieldValueString(0, userColName).isEmpty()){
//                result=Double.parseDouble(retObj1.getFieldValueString(0, userColName));
//                }else {
//                result=0.0;
//                }
//
//                if(result>0.0){
//                strresult=BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
//            }else if(Double.isNaN(result)){
//            strresult = "0.0";
//            }
//                else{
//                strresult=String.valueOf(result);
//        }
//
//            }
////            ResultSet.put(measName[z], strresult);
//            ResultSet.add(strresult);
////           return String.valueOf(strresult);
//        }
//        catch(Exception ex)
//        {
//             logger.error("Exception:",ex);
//        }
//        }
//        }
//        else{
//        try
//        {
//        query="select ref_element_id as REF_ELEMENT_ID, ref_element_type as REF_ELEMENT_TYPE,USER_COL_TYPE as USER_COL_TYPE,REFFERED_ELEMENTS as REFFERED_ELEMENTS,ACTUAL_COL_FORMULA as ACTUAL_COL_FORMULA, USER_COL_NAME as USER_COL_NAME,AGGREGATION_TYPE AS AGGREGATION    from prg_user_all_info_details where element_id ="+aoQuery.removeComparisonType(elementId[z].toString());
//        }
//        catch(Exception ex)
//        {
//        logger.error("Exception:",ex);
//        }
//        try
//        {
//            retObj=pbdb.executeSelectSQL(query);
//            if((retObj.getFieldValueString(0, "REF_ELEMENT_TYPE").equalsIgnoreCase("4"))&&!(retObj.getFieldValueString(0, "USER_COL_TYPE").equalsIgnoreCase("summarized")))
//            {
//
//                query1 = "select element_id as ELEMENT_ID, user_col_name as USER_COL_NAME from prg_user_all_info_details where ref_element_id ="+retObj.getFieldValueString(0, "REF_ELEMENT_ID");
//                ArrayList changeelementId = new ArrayList();
//                query2 = "";
//                double current = 0;
//                double Prior = 0;
//                retObj1=pbdb.executeSelectSQL(query1);
//                query2 += "select ";
//                for(int i=0;i<2;i++){
//                query2 += retObj1.getFieldValueString(i, "USER_COL_NAME");
//                if(i==0){
//                query2 += " , ";
//                }
//               }
//                 query2 += " from "+AO_Name;
//                 query2 += " "+whereClause;
//                conn = (Connection) ProgenConnection.getInstance().getConnectionForElement(aoQuery.removeComparisonType(elementId[z].toString()));
//                retObj2 = pbdb.execSelectSQL(query2, conn);
//                String currentelementId = retObj1.getFieldValueString(0, 0);
//                String priorelementId = retObj1.getFieldValueString(1, 1);
//                for(int k=0;k<retObj2.getRowCount();k++){
//                    if (retObj2.getFieldValueString(k,0 )!=null ){
//                    current += Double.parseDouble(retObj2.getFieldValueString(k,0 ));
//                    }
//                    if (retObj2.getFieldValueString(k,1 )!=null && retObj2.getFieldValueString(k,1 )!=""  ){
//                    Prior += Double.parseDouble(retObj2.getFieldValueString(k, 1));
//                    }
//                 }
//                 result = 0.0;
//               result = (((current-Prior)/Prior)*100);
//               if(result>0.0){
//               strresult=BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
//            }else if(Double.isNaN(result)){
//            strresult = "0.0";
//            }
//                else{
//                strresult=String.valueOf(result);
//                }
//            }
//            else if((!retObj.getFieldValueString(0, "REF_ELEMENT_TYPE").equalsIgnoreCase("4")) && retObj.getFieldValueString(0, "USER_COL_TYPE").equalsIgnoreCase("summarized"))
//            {
//                query = "";
//                refferedElement= retObj.getFieldValueString(0, "REFFERED_ELEMENTS");
//                actualColFormula=retObj.getFieldValueString(0, "ACTUAL_COL_FORMULA");
//
//                query +="select "+actualColFormula+" AS  RESULT from "+AO_Name;
//                query += " "+whereClause;
//                conn = (Connection) ProgenConnection.getInstance().getConnectionForElement(aoQuery.removeComparisonType(elementId[z].toString()));
//                retObj1 = pbdb.execSelectSQL(query, conn);
//                result = 0.0;
//                result=Double.parseDouble(retObj1.getFieldValueString(0, "RESULT"));
//                if(result>0.0){
//                  strresult=BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
//            }else if(Double.isNaN(result)){
//            strresult = "0.0";
//            }
//                else{
//                strresult=String.valueOf(result);
//                }
//            }
//            else if((retObj.getFieldValueString(0, "REF_ELEMENT_TYPE").equalsIgnoreCase("3")) && !(retObj.getFieldValueString(0, "USER_COL_TYPE").equalsIgnoreCase("summarized")))
//            {
//
//                query1 = "select element_id as ELEMENT_ID, user_col_name as USER_COL_NAME from prg_user_all_info_details where ref_element_id ="+retObj.getFieldValueString(0, "REF_ELEMENT_ID");
//                ArrayList changeelementId = new ArrayList();
//                query2 = "";
//                double current = 0;
//                double Prior = 0;
//                retObj1=pbdb.executeSelectSQL(query1);
//                query2 += "select ";
//                for(int i=0;i<2;i++){
//                query2 += retObj1.getFieldValueString(i, "USER_COL_NAME");
//                if(i==0){
//                query2 += " , ";
//                }
//               }
//                 query2 += " from "+AO_Name;
//                 query2 += " "+whereClause;
//                conn = (Connection) ProgenConnection.getInstance().getConnectionForElement(aoQuery.removeComparisonType(elementId[z].toString()));
//                retObj2 = pbdb.execSelectSQL(query2, conn);
//                String currentelementId = retObj1.getFieldValueString(0, 0);
//                String priorelementId = retObj1.getFieldValueString(1, 1);
//                for(int k=0;k<retObj2.getRowCount();k++){
//                    if (retObj2.getFieldValueString(k,0 )!=null ){
//                    current += Double.parseDouble(retObj2.getFieldValueString(k,0 ));
//                    }
//                    if (retObj2.getFieldValueString(k,1 )!=null && retObj2.getFieldValueString(k,1 )!=""  ){
//                    Prior += Double.parseDouble(retObj2.getFieldValueString(k, 1));
//                    }
//                 }
//                 result = 0.0;
//               result = (current-Prior);
//               if(result>0.0){
//               strresult=BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
//            }else if(Double.isNaN(result)){
//            strresult = "0.0";
//            }
//                else{
//                strresult=String.valueOf(result);
//                }
//            }
//            else if((!retObj.getFieldValueString(0, "REF_ELEMENT_TYPE").equalsIgnoreCase("3")) && retObj.getFieldValueString(0, "USER_COL_TYPE").equalsIgnoreCase("summarized"))
//            {
//                query = "";
//                refferedElement= retObj.getFieldValueString(0, "REFFERED_ELEMENTS");
//                actualColFormula=retObj.getFieldValueString(0, "ACTUAL_COL_FORMULA");
//
//                query +="select "+actualColFormula+" AS  RESULT from "+AO_Name;
//                query += " "+whereClause;
//                conn = (Connection) ProgenConnection.getInstance().getConnectionForElement(aoQuery.removeComparisonType(elementId[z].toString()));
//                retObj1 = pbdb.execSelectSQL(query, conn);
//                result = 0.0;
//                result=Double.parseDouble(retObj1.getFieldValueString(0, "RESULT"));
//                if(result>0.0){
//                  strresult=BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
//            }else if(Double.isNaN(result)){
//            strresult = "0.0";
//            }
//                else{
//                strresult=String.valueOf(result);
//                }
//            }
//            else
//            {
//
//                query = "";
//                conn = (Connection) ProgenConnection.getInstance().getConnectionForElement(aoQuery.removeComparisonType(elementId[z].toString()));
//                userColName=retObj.getFieldValueString(0, "USER_COL_NAME");
//                Aggregation=retObj.getFieldValueString(0, "AGGREGATION");
//                if(Aggregation.toUpperCase().equalsIgnoreCase("COUNTDISTINCT") || Aggregation.toUpperCase().equalsIgnoreCase("COUNT") || Aggregation.contains("#")){
//                Aggregation = "SUM";
//                }
//                query +="select "+Aggregation+"("+userColName+") "+userColName+" from "+AO_Name;
//                query += " "+whereClause;
//                
//                retObj1 = pbdb.execSelectSQL(query, conn);
//                result = 0.0;
//                if(!retObj1.getFieldValueString(0, userColName).equalsIgnoreCase("") && !retObj1.getFieldValueString(0, userColName).isEmpty()){
//                result=Double.parseDouble(retObj1.getFieldValueString(0, userColName));
//                }else {
//                result=0.0;
//                }
//
//                if(result>0.0){
//                strresult=BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
//            }else if(Double.isNaN(result)){
//            strresult = "0.0";
//            }
//                else{
//                strresult=String.valueOf(result);
//        }
//
//            }
////            ResultSet.put(measName[z], strresult);
//            ResultSet.add(strresult);
////           return String.valueOf(strresult);
//        }
//        catch(Exception ex)
//        {
//             logger.error("Exception:",ex);
//        }
//    }
//        }
        return gson.toJson(ResultSet);
    }

    public String setTimeType(ArrayList timeDetailArray, String timeType) throws SQLException {
        PbTimeRanges pbTime = new PbTimeRanges();
        String[] elementId = timeType.split("_");
        String id = "";
        String type = "";
        try {
            id = elementId[0];
            type = elementId[1];
        } catch (Exception ex) {
        }
        pbTime.elementID = id;

        if (type.equalsIgnoreCase("")) {
            pbTime.setRange(timeDetailArray.get(3).toString(), timeDetailArray.get(4).toString(), timeDetailArray.get(2).toString());
            return " and DDATE " + pbTime.d_clu;
        } else if (type.equalsIgnoreCase("PRIOR")) {
            pbTime.setRange(timeDetailArray.get(3).toString(), timeDetailArray.get(4).toString(), timeDetailArray.get(2).toString());
            return " and DDATE " + pbTime.pd_clu;
        } else {
            return " and DDATE " + pbTime.setRange(timeDetailArray, type);

        }

    }

    public String removeComparisonType(String qryWhereId) {
        String[] split = qryWhereId.split(",");
//        qryWhereId= "";
        StringBuilder qryWhereId1 = new StringBuilder(300);
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
            }
        }


        return qryWhereId1.toString();
    }

    private void setTimeTypeForMeasure(DashboardChartData chartDetails, ProgenAOQuery objQuery) {
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

    void saveGraphSavePointFile(String newReportId, String oldReportId, String savepoint, String fileLocation, String userId, String bizzRoleId) {
        File srcFolder = new File(fileLocation+"/SavePoint/"+userId+"/"+bizzRoleId+"/"+"/"+oldReportId);
File destFolder = new File(fileLocation+"/SavePoint/"+userId+"/"+bizzRoleId+"/"+"/"+newReportId);

    	//make sure source exists
    	if(!srcFolder.exists()){

           
           //just exit
         return;

        }else{

           try{
        	copyFolder(srcFolder,destFolder,"",newReportId,"",oldReportId,savepoint);
           }catch(IOException e){
        	e.printStackTrace();
        	//error, just exit
              return;
}
        }

    }
    private void setTimeTypeForMeasureTemplate(TemplateMeta chartDetails, ProgenAOQuery objQuery,String measureId) {
        ArrayList<String> measureType = new ArrayList<String>();
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
        objQuery.setTimeDetailsArray(measureType);
    }

    String GTKPIForTemplate(HttpServletRequest request, String repId, Container container) {
        Connection conn = null;
        Map<String, String> resultMap = new HashMap<String, String>();
        double result;
        String strresult = "";
        String aoAsGoId = request.getParameter("aoAsGoId");
        String timeClauseGO = "";
        HttpSession session = request.getSession(false);
        String userid = String.valueOf(session.getAttribute("USERID"));
        PbReportViewerDAO dao = new PbReportViewerDAO();
        List<String> ResultSet = new ArrayList<String>();
//        String fileLocation = "";
//         if (session != null) {
//                fileLocation = dao.getFilePath(session);
//            } else {
//                fileLocation = "/usr/local/cache";
//            }
//        List<String> measNameList = new ArrayList<>();
//        List<String> measIdList = new ArrayList<>();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//        String userColName = "";
        String filterClause = "";
        ArrayList<String> timeDetailsGO = new ArrayList<String>();
        ArrayList<String> timeDetails = new ArrayList<String>();
        if (container != null) {
            timeDetails = container.getTimeDetailsArray();
            for (int i = 0; i < timeDetails.size(); i++) {
                timeDetailsGO.add(i, timeDetails.get(i));
            }

        }

        PbReturnObject retObj = new PbReturnObject();
        ProgenTimeDefinition timeDefObj = ProgenTimeDefinition.getInstance(repId, container, userid);
//        PbReturnObject retObj1=new PbReturnObject();
//        PbReturnObject retObj2=new PbReturnObject();
        PbDb pbdb = new PbDb();
        String AO_Name = "";
        if (aoAsGoId != null && !aoAsGoId.equalsIgnoreCase("null") && !aoAsGoId.equalsIgnoreCase("")) {

            AO_Name = "M_AO_" + aoAsGoId;
        } else {

            AO_Name = "R_GO_" + repId;
        }

        reportMeta = new XtendReportMeta();
        ReportManagementDAO repManagementDao = new ReportManagementDAO();
        reportMeta = repManagementDao.setReportForm(request);
        Map<String, TemplateMeta> templateMeta = reportMeta.getTemplateMeta();
        TemplateMeta templateDetails = templateMeta.get("template1");
        List<String> measureIds = templateDetails.getMeasureIds();

        String action = request.getParameter("action");
        Map<String, List<String>> drillMap = new HashMap<String, List<String>>();
        HashMap<String, List> drillmapFromSet = new HashMap<String, List>();
        HashMap<String, List> drillmapFromSetAgg = new HashMap<String, List>();
        HashMap<String, List> filtermapFromSet = new HashMap<String, List>();
        HashMap<String, List> localfiltermapFromSet = new HashMap<String, List>();
//        HashMap<String, String> ResultSet = new HashMap<String, String>();
        String check_DDate = "";
        check_DDate = "select DDATE from R_GO_" + repId + " where 1=2";
        conn = (Connection) ProgenConnection.getInstance().getConnectionForElement(removeComparisonType(measureIds.get(0).toString()));
        try {
            retObj = pbdb.execSelectSQL(check_DDate, conn);

        } catch (Exception e) {
        }
        if (retObj == null) {
            timeClauseGO = "";
        }

//        if (timeDefObj != null && templateDetails.getMeasureIds() != null) {
        List<String> measLabel = new ArrayList<>();
        List<String> compMeasLabel = new ArrayList<>();
        for (int i = 0; i < templateDetails.getMeasureIds().size(); i++) {
            String measId1 = templateDetails.getMeasureIds().get(i);
            String measName1 = templateDetails.getMeasureNames().get(i);
            if (measId1 != null && timeDefObj.getTimeDefinition() != null) {
                measLabel.add(repManagementDao.getTimeBasedMeasure(timeDefObj, measName1, measId1, timeDetailsGO));
            } else {
                measLabel.add(measName1);
            }
        }
        templateDetails.setMeasureNames(measLabel);
        for (int i = 0; i < templateDetails.getMeasureIds().size(); i++) {
            ArrayList<String> measureTimeDetails=new ArrayList();
            Map<String, List<String>> timeDetails1 = templateDetails.getTimeDetails();
            if(timeDetails1!=null){
                    List<String> value=timeDetails1.get(removeComparisonType(templateDetails.getMeasureIds().get(i))+"_date");
                    if(value!=null){
                        for(String t:value){
                            measureTimeDetails.add(t);
                        }
                    }
                    else{
                        measureTimeDetails=timeDetailsGO;                        
                    }
            }
            else{
                measureTimeDetails=timeDetailsGO;
            }
            ProgenAOQuery aoQuery = new ProgenAOQuery();
            aoQuery.setTimeDetails(measureTimeDetails);
            aoQuery.setAvoidProgenTime("No");
//        }
            aoQuery.setDrillFormat(reportMeta.getDrillFormat());
            List<String> grandTotalAggType = new ArrayList<String>(1);
            grandTotalAggType.add("N");
            String queryGT = "";
            aoQuery.setAO_name(AO_Name);
//                aoQuery.setTimeDetails(container.getTimeDetailsArray());
            aoQuery.setGrandTotalAggType(grandTotalAggType);
            aoQuery.setViewIdList(null);
            aoQuery.setColViewIdList(null);
            List<String> measureId=new ArrayList<String>();
            measureId.add(templateDetails.getMeasureIds().get(i));
            aoQuery.setMeasureIdsList(measureId);
            aoQuery.setMeasureIdsListGO(measureId);
            setTimeTypeForMeasureTemplate(templateDetails, aoQuery,templateDetails.getMeasureIds().get(i));
            aoQuery.setFilterClause(filterClause);
            List<String> aggType=new ArrayList<String>();
            aggType.add(templateDetails.getAggregation().get(i));
            
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
                connect = ProgenConnection.getInstance().getConnectionForElement(removeComparisonType(templateDetails.getMeasureIds().get(0)));
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
                        result = Double.parseDouble(pbretObj.getFieldValueString(j, "A_" + measureId.get(k)));
                        strresult = BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                        ResultSet.add(strresult);
                    }
                }

//            }


            }
        }
        resultMap.put("meta", gson.toJson(templateMeta));
        resultMap.put("data", gson.toJson(ResultSet));
        return gson.toJson(resultMap);
    }

}
