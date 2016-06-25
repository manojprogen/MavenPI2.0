package com.progen.report;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.apache.log4j.Logger;

public class DashBoardDataGenerator {

    public static Logger logger = Logger.getLogger(DashBoardDataGenerator.class);
    List<Map<String, List<Double>>> masterList = new ArrayList<Map<String, List<Double>>>();
    XtendReportMeta reportMeta;
    Map<String, List<String>> dataPaths;
    Map<String, List<Map<String, String>>> dashBoardMaster = new HashMap<String, List<Map<String, String>>>();
    Map<String, List<Map<String, List<Double>>>> threadMasterMap = new HashMap<String, List<Map<String, List<Double>>>>();
    List<String> commonColList = new ArrayList<String>();
    Map<String, String> commonCols;
    Map<String, List<String>> commonColDrill = new HashMap<String, List<String>>();
    String firstChart;
    Set<String> keySet;

    public DashBoardDataGenerator() {
    }

    public DashBoardDataGenerator(XtendReportMeta reportMeta, Map<String, List<String>> filePaths, Map<String, String> commonColList) {
        this.reportMeta = reportMeta;
        this.dataPaths = filePaths;
        if (commonColList != null && !commonColList.isEmpty()) {
            Set<String> comCol = commonColList.keySet();
            this.commonColList.addAll(comCol);
        }
    }

    public String dashBoardData() {
        BufferedReader br;
        String ab = "";
        int count = 0;
        String line;
        try {
            Date date = new Date();
            long startTime = date.getTime();
            Map<String, DashboardChartData> chartData = new LinkedHashMap<String, DashboardChartData>(reportMeta.getChartData());
            List<String> charts = new ArrayList(chartData.keySet());
            firstChart = charts.get(0);
            int dataChunk = 50000;
//            logger.error("chunkSize:--" + dataChunk);
            Set<String> dataPathKeys = dataPaths.keySet();
            for (String dataPathKey : dataPathKeys) {
                List<String> filePaths = dataPaths.get(dataPathKey);
                Map<String, DashboardChartData> chartData1 = new HashMap<String, DashboardChartData>();
                chartData1.put(dataPathKey, chartData.get(dataPathKey));
                if (!dataPathKey.equalsIgnoreCase("singleCsv")) {
                    reportMeta.setChartData(chartData1);
                }
                for (String filePath : filePaths) {
                    ArrayList<String> l1 = new ArrayList<String>();
                    int dataLineCount = 0;
                    DashBoardSequenceThread ft;
                    String next;
                    br = new BufferedReader(new FileReader(filePath));
                    line = br.readLine();
                    for (boolean first = true, last = (line == null); !last; first = false, line = next) {
                        last = ((next = br.readLine()) == null);
                        if (first) {
                            br.mark(dataChunk);
                            List<String> headers = Arrays.asList(line.split(",", -1));
                            reportMeta.setHeaders(headers);
                        } else {
                            if (isValidate(line, dataPathKey)) {
                                l1.add(line);
                            }
                            if (dataLineCount == (dataChunk - 1) || last) {
                                dataLineCount = 0;
                                if (l1.size() > 0) {
                                    ft = new DashBoardSequenceThread();
                                    Thread t = new Thread(ft);
                                    ft.initFileThread((ArrayList) l1.clone(), reportMeta, threadMasterMap);
                                    l1.clear();
                                    t.start();
                                    try {
                                        t.join();
                                    } catch (InterruptedException ex) {
                                    }
                                }
                            } else {
                                dataLineCount++;
                            }

                        }
                        count++;
                    }
                    br.close();
                }
                if (firstChart.equals(dataPathKey) && reportMeta.getIsMultipleCsv()) {
                    keySet = commonColDrill.keySet();
                }
            }
            reportMeta.setChartData(chartData);
            if (!threadMasterMap.isEmpty()) {
                for (String chart : charts) {
                    DashboardChartData chartDetails = reportMeta.getChartData().get(chart);
//                    if (chartDetails.getViewByLevel() == null || chartDetails.getViewByLevel().equalsIgnoreCase("single")) {
////                        reportMeta.getViewbys().clear();
//                        reportMeta.setViewbys(chartDetails.getViewBys());
//                    } else if (chartDetails.getViewByLevel().equalsIgnoreCase("double")) {
////                        reportMeta.getViewbys().clear();
////                        reportMeta.getViewbys().add(chartDetails.getViewBys().get(0));
////                        if (chartDetails.getViewBys().size() > 1) {
////                            reportMeta.getViewbys().add(chartDetails.getViewBys().get(1));
////                        }
//                    } else { //hirarchical
                    reportMeta.setViewbys(chartDetails.getViewBys());
//                    }
                    reportMeta.setMeasures(chartDetails.getMeassures());
                    reportMeta.setAggregations(chartDetails.getAggregation());
                    List<Map<String, List<Double>>> listMap = threadMasterMap.get(chart);
                    Map<String, List<Double>> map;
                    if (listMap == null || listMap.get(0) == null) {
                        map = new HashMap<String, List<Double>>();
                    } else {
                        map = combineChunks(threadMasterMap.get(chart));
                    }

                    List<Map<String, String>> list = processJson(map);
                    if (reportMeta.getViewbys().get(0).equalsIgnoreCase("Year") || reportMeta.getViewbys().get(0).toString().toLowerCase().contains("month") || reportMeta.getViewbys().get(0).toString().toLowerCase().contains("time")) {
                        DateComperator dateComperator = new DateComperator(reportMeta.getViewbys().get(0).toString(), "asc", null);
                        Collections.sort(list, dateComperator);
                    } else {
                        JsonComperator jsonComperator = new JsonComperator(reportMeta.getMeasures().get(0), "desc", "");
                        Collections.sort(list, jsonComperator);
                    }
                    dashBoardMaster.put(chart, list);
                    masterList = new ArrayList<Map<String, List<Double>>>();
                }
            }
            Gson gson = new Gson();
            ab = gson.toJson(dashBoardMaster);
            Date date1 = new Date();
            long endTime = date1.getTime();
//            logger.error("time in mSec  " + (endTime - startTime));
        } catch (IOException ioe) {
            logger.error("Exception:", ioe);
        }
        return ab;
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
                    tempMap.put(reportMeta.getViewbys().get(j), a[j].trim());
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
            }

        }
        return list;
    }

    public Map<String, List<Double>> combineChunks(List<Map<String, List<Double>>> listMap) {
        Map<String, List<Double>> map = new HashMap<String, List<Double>>();
        List<Double> list;
        for (Map<String, List<Double>> chunkMap : listMap) {
            Set<String> keys = chunkMap.keySet();
            for (String key : keys) {

                if (map.containsKey(key)) {
                    list = map.get(key);
                    List<Double> measureValues = chunkMap.get(key);
                    for (int j = 0; j < reportMeta.getMeasures().size(); j++) {
                        double temp = list.get(j);
                        list.remove(j);
                        if (reportMeta.getAggregations().get(j).equalsIgnoreCase("COUNT_DISTINCT")) {
                            Map<String, Map<String, Double>> countDis = reportMeta.getCountDistinctMap();
                            list.add(j, (double) countDis.get(key).size());
                        } else if (reportMeta.getAggregations().get(j).equalsIgnoreCase("AVG")) {
                            list.add(j, temp + measureValues.get(j));
                        } else if (reportMeta.getAggregations().get(j).equalsIgnoreCase("COUNT")) {
                            list.add(j, temp + measureValues.get(j));
                        } else if (reportMeta.getAggregations().get(j).equalsIgnoreCase("MAX")) {
                            if (measureValues.get(j) > temp) {
                                list.add(j, measureValues.get(j));
                            }
                        } else if (reportMeta.getAggregations().get(j).equalsIgnoreCase("MIN")) {
                            if (measureValues.get(j) < temp) {
                                list.add(j, measureValues.get(j));
                            }
                        } else {
                            list.add(j, temp + measureValues.get(j));
                        }
                        temp = list.get(reportMeta.getMeasures().size() + j);
                        list.remove(reportMeta.getMeasures().size() + j);
                        list.add(reportMeta.getMeasures().size() + j, temp + measureValues.get(reportMeta.getMeasures().size() + j));
                    }
                } else {
                    map.put(key, chunkMap.get(key));
                }
            }
        }
        return map;
    }

    public boolean isValidate(String line, String chart) {
        String[] splits = line.split(",");
        boolean status = true;
        if (reportMeta.getDrills() != null && !reportMeta.getDrills().isEmpty()) {
            Set<String> keys = reportMeta.getDrills().keySet();
            for (String key : keys) {
                String val;
                if (reportMeta.getHeaders().indexOf(key) != -1) {
                    List<String> drillValues = reportMeta.getDrills().get(key);
                    val = splits[reportMeta.getHeaders().indexOf(key)];
                    if ((val.isEmpty() || drillValues.indexOf(val.trim()) == -1)) {
                        status = false;
                        break;
                    }
                } else {
                    if (reportMeta.getIsMultipleCsv()) {
                        if (commonColList.size() == 0) {
                            status = false;
                        } else {
                            for (String key1 : keySet) {
                                List<String> drillValues = commonColDrill.get(key1);
                                val = splits[reportMeta.getHeaders().indexOf(key1)];
                                if ((val.isEmpty() || drillValues.indexOf(val.trim()) == -1)) {
                                    status = false;
                                    break;
                                }
                            }
                        }

                    }
                }
            }
        }



        if (reportMeta.getFilterMap() != null && !reportMeta.getFilterMap().isEmpty()) {
            Set<String> keys = reportMeta.getFilterMap().keySet();
            for (String key : keys) {

                String val;
                if (reportMeta.getHeaders().indexOf(key) != -1) {
                    List<String> drillValues = reportMeta.getFilterMap().get(key);
                    val = splits[reportMeta.getHeaders().indexOf(key)];
                    if ((val.isEmpty() || drillValues.indexOf(val.trim()) != -1)) {
                        status = false;
                        break;
                    }
                } else {
                    if (reportMeta.getIsMultipleCsv()) {
                        if (commonColList.size() == 0) {
                            status = false;
                        } else {
                            for (String key1 : keySet) {
                                List<String> drillValues = commonColDrill.get(key1);
                                val = splits[reportMeta.getHeaders().indexOf(key1)];
                                if ((val.isEmpty() || drillValues.indexOf(val.trim()) != -1)) {
                                    status = false;
                                    break;
                                }
                            }
                        }

                    }
                }
            }
        }

        if (status) {
            if (firstChart.equals(chart)) {
//                        if (commonColList.indexOf(key) == -1) {
                for (String commonCol : commonColList) {
                    String val1 = splits[reportMeta.getHeaders().indexOf(commonCol)];
                    if (!commonColDrill.containsKey(commonCol)) {
                        commonColDrill.put(commonCol, new ArrayList<String>());
                    }
                    if (commonColDrill.get(commonCol).indexOf(val1) == -1) {
                        commonColDrill.get(commonCol).add(val1);
                    }
                }
            }
        }
        return status;
    }
}
