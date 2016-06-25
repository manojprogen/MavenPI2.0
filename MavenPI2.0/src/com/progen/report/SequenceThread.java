package com.progen.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class SequenceThread implements Runnable {

    public static Logger logger = Logger.getLogger(SequenceThread.class);
    Thread t;
    int startIndex;
    int endIndex;
    XtendReportMeta reportMeta;
    Map<String, List<Double>> map = new HashMap<String, List<Double>>();
    List<String> list;
    List<Map<String, List<Double>>> mapList;
    Map<String, List<String>> countDistinctMap;
    Map<String, Map<String, Double>> countDis = new HashMap<String, Map<String, Double>>();

    void initFileThread(int startIndex, int endIndex, List<String> list, XtendReportMeta reportMeta, List<Map<String, List<Double>>> masterList) {
        this.list = list;
        this.reportMeta = reportMeta;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.mapList = masterList;
    }

    @Override
    public void run() {
        try {
            for (String l1 : list) {
                createJson(l1.split(",", -1));
            }
            mapList.add(map);
            list.clear();
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public void createJson(String[] splits) {
//        String groupbys = "";
        StringBuilder groupbys = new StringBuilder();
        try {
            for (int i = 0; i < reportMeta.getViewbys().size(); i++) {
                if (i == 0) {
//                    groupbys += splits[reportMeta.getHeaders().indexOf(reportMeta.getViewbys().get(i))];
                    groupbys.append(splits[reportMeta.getHeaders().indexOf(reportMeta.getViewbys().get(i))]);
                } else {
//                    groupbys += "," + splits[reportMeta.getHeaders().indexOf(reportMeta.getViewbys().get(i))];
                    groupbys.append(",").append(splits[reportMeta.getHeaders().indexOf(reportMeta.getViewbys().get(i))]);
                }
            }
            List<Double> list1 = new ArrayList<Double>(reportMeta.getMeasures().size() * 2);
            for (int i = 0; i < reportMeta.getMeasures().size() * 2; i++) {  //this loop use to allocate memory to size of List
                list1.add(0d);
            }
            Map<String, Double> groupByMap = new HashMap<String, Double>();
            if (map.containsKey(groupbys.toString())) {
                list1 = map.get(groupbys.toString());
                for (int j = 0; j < reportMeta.getMeasures().size(); j++) {
                    boolean rowOmmitFlag = false;
                    double temp = 0;
                    if (!reportMeta.getAggregations().get(j).equalsIgnoreCase("COUNT_DISTINCT")) {
                        temp = list1.get(j);
                        if (new Float(temp).isNaN()) {
                            temp = (float) 0.0;
                        }
                        list1.remove(j);
                    }
                    Double measureVal;
                    String tmp;
                    if (!reportMeta.getAggregations().get(j).equalsIgnoreCase("COUNT_DISTINCT")) {
                        tmp = splits[reportMeta.getHeaders().indexOf(reportMeta.getMeasures().get(j))].replace(" ", "");
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
                        measureVal = 0d;
                    }
                    if (reportMeta.getAggregations().get(j).equalsIgnoreCase("COUNT_DISTINCT")) {
                        if (countDis.get(groupbys.toString()) != null) {
                            groupByMap = countDis.get(groupbys.toString());
                        }
                        groupByMap.put(splits[reportMeta.getHeaders().indexOf(reportMeta.getMeasures().get(j))], 2.0);
                        countDis.put(groupbys.toString(), groupByMap);
                        int len = 0;
                        if (countDis.get(groupbys.toString()) != null) {
                            len = countDis.get(groupbys.toString()).size();
                        }
                        reportMeta.setCountDistinctMap(countDis);
                        measureVal = (double) len;
                        list1.add(j, measureVal);
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
                        tmp = tmp.replaceAll("[^.\\ \\d]", "");
                        if (tmp.length() != 0) {
                            groupByMap.put(sign + tmp, Double.parseDouble("2"));
                            countDis.put(groupbys.toString(), groupByMap);
                        }
                        measureVal = 1d;
                    }

                    if (reportMeta.getAggregations().get(j).equalsIgnoreCase("COUNT")) {
                        list1.add(j, 1d);
                    } else {
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
                map.put(groupbys.toString(), list1);
            }
        } catch (NumberFormatException e) {
            logger.error("Exception:", e);
        } catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
    }
}