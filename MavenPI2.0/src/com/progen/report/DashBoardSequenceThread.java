package com.progen.report;

//import com.progen.services.DashboardChartData;
//import com.progen.services.XtendReportMeta;
import java.util.*;
import org.apache.log4j.Logger;

public class DashBoardSequenceThread implements Runnable {

    public static Logger logger = Logger.getLogger(DashBoardSequenceThread.class);
    Thread t;
    XtendReportMeta reportMeta;
    DashboardChartData chartDetails;
    Map<String, Map<String, List<Double>>> map = new HashMap<String, Map<String, List<Double>>>();
    List<String> list;
    Map<String, List<Map<String, List<Double>>>> mapList;
    Map<String, Map<String, Double>> countDis = new HashMap<String, Map<String, Double>>();

    void initFileThread(List<String> list, XtendReportMeta reportMeta, Map<String, List<Map<String, List<Double>>>> masterList) {
        this.list = list;
        this.reportMeta = reportMeta;
        this.mapList = masterList;
    }

    @Override
    public void run() {
        try {
            Map<String, DashboardChartData> chartData = reportMeta.getChartData();
            Set<String> charts = chartData.keySet();
            int count = 0;
            for (String l1 : list) {
                for (String chart : charts) {
                    chartDetails = reportMeta.getChartData().get(chart);
                    if (chartDetails.getViewByLevel() == null || chartDetails.getViewByLevel().equalsIgnoreCase("single")) {
//                        if(reportMeta.getViewbys()!=null){
//                        reportMeta.getViewbys().clear();
//                        }
                        reportMeta.setViewbys(chartDetails.getViewBys());
                        reportMeta.setMeasures(chartDetails.getMeassures());
                        reportMeta.setAggregations(chartDetails.getAggregation());
//                        reportMeta.getViewbys().add(chartDetails.getViewBys().get(0));
                    } else if (chartDetails.getViewByLevel().equalsIgnoreCase("double")) {
//                        if(reportMeta.getViewbys()!=null){
//                        reportMeta.getViewbys().clear();
//                        }
                        reportMeta.setViewbys(chartDetails.getViewBys());
//                        reportMeta.getViewbys().add(chartDetails.getViewBys().get(0));
                        reportMeta.setMeasures(chartDetails.getMeassures());
                        reportMeta.setAggregations(chartDetails.getAggregation());
                        if (chartDetails.getViewBys().size() > 1) {
                            reportMeta.getViewbys().add(chartDetails.getViewBys().get(1));
                        }
                    } else { //hirarchical
//                        if(reportMeta.getViewbys()!=null){
//                        reportMeta.getViewbys().clear();
//                        }
                        reportMeta.setViewbys(chartDetails.getViewBys());
                    }
//                    List<String> aggregations = new ArrayList<String>();
//                    for(String measure:reportMeta.getMeasures()){
//                    aggregations.add("SUM");
//                    }
//                    reportMeta.setAggregations(aggregations);

                    String[] splits = l1.split(",", -1);
                    if (isValidate(splits)) {

                        createJson(splits, chart);
                        count++;
                    }
                }
            }
            for (String chart : charts) {
                if (!mapList.containsKey(chart)) {
                    mapList.put(chart, new ArrayList<Map<String, List<Double>>>());
                }
                mapList.get(chart).add(map.get(chart));
            }

            list.clear();
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public void createJson(String[] splits, String chart) {
        String groupbys = "";
        try {
            boolean firstFlag = true;
            for (int i = 0; i < reportMeta.getViewbys().size(); i++) {
                if (reportMeta.getHeaders().indexOf(reportMeta.getViewbys().get(i)) != -1) {
                    if (firstFlag) {
                        firstFlag = false;
                        if (reportMeta.getViewbys().get(i).equalsIgnoreCase("XtendGT")) {
                            groupbys += "XtendGT";
                        } else {
                            groupbys += splits[reportMeta.getHeaders().indexOf(reportMeta.getViewbys().get(i))];
                        }
                    } else {
                        if (reportMeta.getViewbys().get(i).equalsIgnoreCase("XtendGT")) {
                            groupbys += "XtendGT";
                        } else {
                            groupbys += "," + splits[reportMeta.getHeaders().indexOf(reportMeta.getViewbys().get(i))];
                        }
                    }
                }
            }
            List<Double> list1 = new ArrayList<Double>(reportMeta.getMeasures().size() * 2);
            for (int i = 0; i < reportMeta.getMeasures().size() * 2; i++) {    //this loop use to allocate memory to reAllocated size of List
                list1.add(0d);
            }
            Map<String, Double> groupByMap = new HashMap<String, Double>();
            if (!map.containsKey(chart)) {
                map.put(chart, new HashMap<String, List<Double>>());
            }

            if (map.get(chart).containsKey(groupbys)) {
                list1 = map.get(chart).get(groupbys);
                for (int j = 0; j < reportMeta.getMeasures().size(); j++) {
                    boolean rowOmmitFlag = false;
                    double temp = 0;
                    if (!reportMeta.getAggregations().get(j).equalsIgnoreCase("COUNT_DISTINCT")) {
                        try {
                            temp = list1.get(j);
                            if (new Float(temp).isNaN()) {
                                temp = (float) 0.0;
                            }
                            list1.remove(j);
                        } catch (Exception e) {
                            logger.error("Exception:", e);
                        }
                    }
                    Double measureVal;
                    if (!reportMeta.getAggregations().get(j).equalsIgnoreCase("COUNT_DISTINCT")) {
                        String tmp = splits[reportMeta.getHeaders().indexOf(reportMeta.getMeasures().get(j))].replace(" ", "");
                        String sign = tmp.replaceAll("[^-]", "");
                        if (tmp.contains("e") || tmp.contains("E")) {
                            tmp = String.valueOf(Double.valueOf(tmp).intValue());
                        }
                        tmp = tmp.replaceAll("[^.\\ \\d]", "");
                        if (tmp == null || tmp.equals("") || tmp.equals("null")) {
                            rowOmmitFlag = true;
                        }
                        if (tmp == null || tmp.equals("") || new Double(Double.parseDouble(tmp)).isNaN()) {
                            measureVal = 0d;
                        } else {
                            measureVal = Double.parseDouble(sign + tmp);
                        }
                    } else {
                        measureVal = 0d;
                    }
                    if (reportMeta.getAggregations().get(j).equalsIgnoreCase("COUNT_DISTINCT")) {
                        if (countDis.get(groupbys) != null) {
                            groupByMap = countDis.get(groupbys);
                        }
                        groupByMap.put(splits[reportMeta.getHeaders().indexOf(reportMeta.getMeasures().get(j))], 2.0);
                        countDis.put(groupbys, groupByMap);
                        int len = 0;
                        if (countDis.get(groupbys) != null) {
                            len = countDis.get(groupbys).size();
                        }
//                        reportMeta.setCountDistinctMap(countDis);
                        double d = len;
                        list1.add(j, d);
                    } else if (reportMeta.getAggregations().get(j).equalsIgnoreCase("AVG")) {
                        list1.add(j, temp + measureVal);
                    } else if (reportMeta.getAggregations().get(j).equalsIgnoreCase("COUNT")) {
                        list1.add(j, ++temp);
                    } else if (reportMeta.getAggregations().get(j).equalsIgnoreCase("MAX")) {
                        if (measureVal > temp) {
                            list1.add(j, measureVal);
                        }
                    } else if (reportMeta.getAggregations().get(j).equalsIgnoreCase("MIN")) {
                        if (measureVal < temp) {
                            list1.add(j, measureVal);
                        }
                    } else {
                        list1.add(j, temp + measureVal);
                    }
                    temp = list1.get(reportMeta.getMeasures().size() + j);
                    list1.remove(reportMeta.getMeasures().size() + j);
                    if (rowOmmitFlag) {
                        list1.add(reportMeta.getMeasures().size() + j, temp + 0d);
                    } else {
                        list1.add(reportMeta.getMeasures().size() + j, temp + 1d);
                    }
                }
            } else {
                for (int j = 0; j < reportMeta.getMeasures().size(); j++) {
                    boolean rowOmmitFlag = false;
                    Double measureVal;
                    if (!reportMeta.getAggregations().get(j).equalsIgnoreCase("COUNT_DISTINCT")) {
//                    String mes=reportMeta.getMeasures().get(j);
//                    int i=reportMeta.getHeaders().indexOf(mes);
//                    String tmp=splits[i];
//                    tmp=tmp.replaceAll("[^.\\ \\d]","");
                        String tmp = splits[reportMeta.getHeaders().indexOf(reportMeta.getMeasures().get(j))].replace(" ", "");
                        String sign = tmp.replaceAll("[^-]", "");
                        tmp = tmp.replaceAll("[^.\\ \\d]", "");
                        if (tmp == null || tmp.equals("") || tmp.equals("null")) {
                            rowOmmitFlag = true;
                        }
                        if (tmp == null || tmp.equals("") || new Double(Double.parseDouble(tmp)).isNaN()) {
                            measureVal = 0d;
                        } else {
                            measureVal = Double.parseDouble(sign + tmp);
                        }
                    } else {
                        String tmp = splits[reportMeta.getHeaders().indexOf(reportMeta.getMeasures().get(j))].replace(" ", "");
                        String sign = tmp.replaceAll("[^-]", "");
                        if (tmp.contains("e") || tmp.contains("E")) {
                            tmp = String.valueOf(Double.valueOf(tmp).intValue());
                        }
                        tmp = tmp.replaceAll("[^.\\ \\d]", "");
                        if (tmp.length() != 0) {
                            groupByMap.put(sign + tmp, Double.parseDouble("2"));
                            countDis.put(groupbys, groupByMap);
                        }
                        measureVal = 1d;
                    }
                    if (reportMeta.getAggregations().get(j).equalsIgnoreCase("COUNT")) {
                        list1.add(j, 1d);
                    } else {
                        if (!new Float(measureVal).isNaN()) {
                            list1.remove(j);
                            list1.add(j, measureVal);
                            list1.remove(reportMeta.getMeasures().size() + j);
                            if (rowOmmitFlag) {
                                list1.add(reportMeta.getMeasures().size() + j, 0d);
                            } else {
                                list1.add(reportMeta.getMeasures().size() + j, 1d);
                            }
                        }
                    }
                }
                map.get(chart).put(groupbys, list1);
            }
        } catch (NumberFormatException e) {
            logger.error("Exception:", e);
        } catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
    }

    public boolean isValidate(String[] splits) {
        boolean status = true;
        if (chartDetails.getFilters() != null && !chartDetails.getFilters().isEmpty()) {
            Set<String> keys = chartDetails.getFilters().keySet();
            for (String key : keys) {
                List<String> drillValues = chartDetails.getFilters().get(key);
                String val = splits[reportMeta.getHeaders().indexOf(key)];
                if ((val.isEmpty() || drillValues.indexOf(val.trim()) != -1)) {
                    status = false;
                    break;
                }
            }
        }
        return status;
    }
}
