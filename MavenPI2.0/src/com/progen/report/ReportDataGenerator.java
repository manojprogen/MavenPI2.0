package com.progen.report;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.*;

public class ReportDataGenerator {

    XtendReportMeta reportMeta;
    Map<String, Map<String, HashMap<String, List<String>>>> dirJsonContent;
    Map<String, String> commonColList = new HashMap<String, String>();
//    Container container = new Container();
    FileReadWrite fileReadWrite = new FileReadWrite();

    public ReportDataGenerator() {
    }

    public ReportDataGenerator(XtendReportMeta reportMeta, Map<String, Map<String, HashMap<String, List<String>>>> dirJsonContent) {
        this.reportMeta = reportMeta;
        this.dirJsonContent = dirJsonContent;
    }

    public String generateReportData(boolean isCreateReport, String reportName, String reportId, String bizzRoleName) {
        String reportData;
        String temp = this.reportMeta.getDataPath();
//        String workDir = temp.split("/")[0];
//        String file = temp.split("/")[1];
//        if (reportMeta.getAggregations() == null) {
//            List<String> measureList=reportMeta.getMeasures();
//            List<String> aggregationList=new ArrayList<String>(measureList.size());
//            List<ColumnMetaData> columnMap=getColumnMetaData(workDir,file.replace(".csv",".json"));
//            for (ColumnMetaData columnMap1 : columnMap) {
//                int index=measureList.indexOf(columnMap1.getColumnName());
//                if (index >= 0) {
//                    if(columnMap1.getAggregation()!=null){
//                        aggregationList.add(index, columnMap1.getAggregation());
//                    }else{
//                        aggregationList.add(index, "SUM");
//                    }
//                }
//            }
//            reportMeta.setAggregations(aggregationList);
//        }
//        if (reportMeta.getIsDashboard() || reportMeta.getIsMap() || reportMeta.getIsMultiKpi() || reportMeta.getIsOverAllWordCloud()) {
        Map<String, List<String>> dataPath = new LinkedHashMap<String, List<String>>();

        Map<String, DashboardChartData> chartData = reportMeta.getChartData();
//            Set<String> charts = chartData.keySet();
        if (reportMeta.getIsMultipleCsv()) {
//                for (String chart : charts) {
//                    List<String> filePaths = new ArrayList<String>();
//                    DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
//                    List<String> fileNames = null;
//
//                    checkCommonColumns(chartDetails.getWorkDir(), chartDetails.getCsvName().replace(".csv", ".json"));
//                    if (isCreateReport) {
//                        reportMeta.setCommonColList(commonColList);
//                    }
//                    if (chartDetails.getWorkDir() != null && chartDetails.getCsvName() != null) {
//                        fileNames = dirJsonContent.get(chartDetails.getWorkDir()).get("data").get(chartDetails.getCsvName());
//                    }
//                    if (fileNames == null) {
//                        fileNames = new ArrayList<String>();
//                        fileNames.add(chartDetails.getCsvName());
//                    }
////                    for (String fileName : fileNames) {
////                        filePaths.add(container.privatePath + chartDetails.getWorkDir() + container.dataPath + fileName);
////                    }
//                    dataPath.put(chart, filePaths);
//                }
        } else {
//                List<String> fileNames = dirJsonContent.get(workDir).get("data").get(file);
            List<String> filePaths = new ArrayList<String>();
//                if (fileNames == null) {
//                    fileNames = new ArrayList<String>();
//                    fileNames.add(file);
//                }
//                for (String fileName : fileNames) {
//                    filePaths.add(container.privatePath + workDir + container.dataPath + fileName);
//                }

            //path 
            filePaths.add("/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ".csv");
//                dataPath.put("singleCsv", "/usr/local/cache/xtend/xtendtest/data/XTend_1.csv");
            dataPath.put("singleCsv", filePaths);
        }

        DashBoardDataGenerator dashBoardDataGenerator = new DashBoardDataGenerator(reportMeta, dataPath, reportMeta.getCommonColList());
        reportData = dashBoardDataGenerator.dashBoardData();
//        } 
//        else {
//            List<String> dataPath = new ArrayList<String>();
////            List<String> fileNames = dirJsonContent.get(workDir).get("data").get(file);
////            if (fileNames == null) {
////                fileNames = new ArrayList<String>();
////                fileNames.add(file);
////            }
//////            for (String fileName : fileNames) {
//////                dataPath.add(container.privatePath + workDir + container.dataPath + fileName);
//////            }
//            CsvToJson csvToJson = new CsvToJson(reportMeta, dataPath);
//            reportData = csvToJson.csvToJson();
//        }
        return reportData;
    }

    public List<ColumnMetaData> getColumnMetaData(String workDir, String sourceFile) {
        Gson gson = new Gson();
        Type tarType = new TypeToken<List<ColumnMetaData>>() {
        }.getType();
        List<ColumnMetaData> list = null;
//                list=gson.fromJson(fileReadWrite.loadJSON(container.privatePath + workDir + container.metaPath + sourceFile), tarType);
        return list;
    }

    public void checkCommonColumns(String workDir, String sourceFile) {
        List<ColumnMetaData> columnMetas = getColumnMetaData(workDir, sourceFile);
        boolean emptyFlag = commonColList.isEmpty();
        Map<String, String> columns = new HashMap<String, String>();
        for (ColumnMetaData columnMeta : columnMetas) {
            if (emptyFlag) {
                commonColList.put(columnMeta.getColumnName(), columnMeta.getColumnName());
            } else {
                if (commonColList.containsKey(columnMeta.getColumnName())) {
                    columns.put(columnMeta.getColumnName(), columnMeta.getColumnName());
                }
            }
            commonColList.clear();
            commonColList.putAll(columns);
        }
    }

    public String generateTrendData(boolean isCreateReport, String reportName, String reportId, String bizzRoleName) {
        String reportData;
        String temp = this.reportMeta.getDataPath();
//        String workDir = temp.split("/")[0];
//        String file = temp.split("/")[1];
//        if (reportMeta.getAggregations() == null) {
//            List<String> measureList=reportMeta.getMeasures();
//            List<String> aggregationList=new ArrayList<String>(measureList.size());
//            List<ColumnMetaData> columnMap=getColumnMetaData(workDir,file.replace(".csv",".json"));
//            for (ColumnMetaData columnMap1 : columnMap) {
//                int index=measureList.indexOf(columnMap1.getColumnName());
//                if (index >= 0) {
//                    if(columnMap1.getAggregation()!=null){
//                        aggregationList.add(index, columnMap1.getAggregation());
//                    }else{
//                        aggregationList.add(index, "SUM");
//                    }
//                }
//            }
//            reportMeta.setAggregations(aggregationList);
//        }
//        if (reportMeta.getIsDashboard() || reportMeta.getIsMap() || reportMeta.getIsMultiKpi() || reportMeta.getIsOverAllWordCloud()) {
        Map<String, List<String>> dataPath = new LinkedHashMap<String, List<String>>();

        Map<String, DashboardChartData> chartData = reportMeta.getChartData();
//            Set<String> charts = chartData.keySet();
        if (reportMeta.getIsMultipleCsv()) {
//                for (String chart : charts) {
//                    List<String> filePaths = new ArrayList<String>();
//                    DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
//                    List<String> fileNames = null;
//
//                    checkCommonColumns(chartDetails.getWorkDir(), chartDetails.getCsvName().replace(".csv", ".json"));
//                    if (isCreateReport) {
//                        reportMeta.setCommonColList(commonColList);
//                    }
//                    if (chartDetails.getWorkDir() != null && chartDetails.getCsvName() != null) {
//                        fileNames = dirJsonContent.get(chartDetails.getWorkDir()).get("data").get(chartDetails.getCsvName());
//                    }
//                    if (fileNames == null) {
//                        fileNames = new ArrayList<String>();
//                        fileNames.add(chartDetails.getCsvName());
//                    }
////                    for (String fileName : fileNames) {
////                        filePaths.add(container.privatePath + chartDetails.getWorkDir() + container.dataPath + fileName);
////                    }
//                    dataPath.put(chart, filePaths);
//                }
        } else {
//                List<String> fileNames = dirJsonContent.get(workDir).get("data").get(file);
            List<String> filePaths = new ArrayList<String>();
//                if (fileNames == null) {
//                    fileNames = new ArrayList<String>();
//                    fileNames.add(file);
//                }
//                for (String fileName : fileNames) {
//                    filePaths.add(container.privatePath + workDir + container.dataPath + fileName);
//                }

            //path
            filePaths.add("/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ".csv");
//                dataPath.put("singleCsv", "/usr/local/cache/xtend/xtendtest/data/XTend_1.csv");
            dataPath.put("singleCsv", filePaths);
        }

        DashBoardDataGenerator dashBoardDataGenerator = new DashBoardDataGenerator(reportMeta, dataPath, reportMeta.getCommonColList());
        reportData = dashBoardDataGenerator.dashBoardData();
//        }
//        else {
//            List<String> dataPath = new ArrayList<String>();
////            List<String> fileNames = dirJsonContent.get(workDir).get("data").get(file);
////            if (fileNames == null) {
////                fileNames = new ArrayList<String>();
////                fileNames.add(file);
////            }
//////            for (String fileName : fileNames) {
//////                dataPath.add(container.privatePath + workDir + container.dataPath + fileName);
//////            }
//            CsvToJson csvToJson = new CsvToJson(reportMeta, dataPath);
//            reportData = csvToJson.csvToJson();
//        }
        return reportData;
    }
}
