package com.progen.report;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.apache.log4j.Logger;

public class CsvToJson {

    public static Logger logger = Logger.getLogger(CsvToJson.class);
    List<Map<String, List<Double>>> masterList = new ArrayList<Map<String, List<Double>>>();
    XtendReportMeta reportMeta;
    List<String> filePaths;
    Map<String, Integer> indexMap = new HashMap<String, Integer>();

    public CsvToJson(XtendReportMeta reportMeta, List<String> filePaths) {
        this.reportMeta = reportMeta;
        this.filePaths = filePaths;
    }

    public String csvToJson() {
        BufferedReader br;
        String ab = "";
        int count = 0;
        String line;



        try {
            Date date = new Date();
            long startTime = date.getTime();
            int dataChunk = 50000;

            for (String filePath : filePaths) {
                br = new BufferedReader(new FileReader(filePath));
                int dataLineCount = 0;
                ArrayList<String> l1 = new ArrayList<String>();
                SequenceThread ft;
                String next;
                line = br.readLine();
                for (boolean first = true, last = (line == null); !last; first = false, line = next) {
                    last = ((next = br.readLine()) == null);
                    if (first) {
                        List<String> headers = Arrays.asList(line.split(",", -1));
                        reportMeta.setHeaders(headers);
                    } else {
//                        if(reportMeta.getDriverList()!=null && ){
//                            if(isValidateDriver(line)){
//                            l1.add(line);
//                            }
//                        }
//                        else{
                        if (isValidate(line)) {
                            l1.add(line);
                        }
//                        }
                        if (dataLineCount == (dataChunk - 1) || last) {
                            dataLineCount = 0;
                            if (l1.size() > 0) {
                                ft = new SequenceThread();
                                Thread t = new Thread(ft);
                                dataLineCount = 0;
                                ft.initFileThread(0, dataChunk - 1, (ArrayList) l1.clone(), reportMeta, masterList);
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
            Map<String, List<Double>> map = combineChunks(masterList);
            List<Map<String, String>> list;
            Gson gson = new Gson();
//            if (reportMeta.getIsHirarchichal()) {
            Map<String, List<Double>> map1 = processHirarchiJson(map);
            ab = gson.toJson(map1);
//            } 
//            else if (reportMeta.getIsOverLay()) {
//                Map map1 = processOverLayJson(map);
//                ab = gson.toJson(map1);
//            } 
//            else {
//                list = processJson(map);
//                JsonComperator jsonComperator = new JsonComperator(reportMeta.getMeasures().get(0), "desc");
//                Collections.sort(list, jsonComperator);
//                ab = gson.toJson(list);
//            }
            Date date1 = new Date();
            long endTime = date1.getTime();
//            logger.error("Error: time in mSec   "+ (endTime - startTime));
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
            }
        }
        return list;
    }

    public Map processHirarchiJson(Map<String, List<Double>> rawStr) {
        List<String> keyList = new ArrayList<String>();
        keyList.addAll(rawStr.keySet());
        Map map = new HashMap<String, Object>();
        for (int j = 0; j < rawStr.size(); j++) {
            String curKey = keyList.get(0);
            if (!(curKey.equalsIgnoreCase(""))) {
                keyList.remove(0);
                List<String> groupBys;
                try {
                    groupBys = new ArrayList<String>(Arrays.asList(curKey.split(",")));
                    if (!groupBys.get(0).equalsIgnoreCase("")) {
                        if (!map.containsKey(groupBys.get(0))) {
                            if (groupBys.size() == 1) {
                                map.put(groupBys.get(0), map.get(curKey));
                            } else {
                                map.put(groupBys.get(0), new HashMap<String, Object>());
                            }
                        }
                        Map hirarMap = (Map) map.get(groupBys.get(0));
                        for (int i = 1; i < groupBys.size(); i++) {
                            if (i == (groupBys.size() - 1)) {
                                hirarMap.put(groupBys.get(i), rawStr.get(curKey));
                            } else {
                                if (!hirarMap.containsKey(groupBys.get(i))) {
                                    hirarMap.put(groupBys.get(i), new HashMap());
                                }
                                hirarMap = (Map) hirarMap.get(groupBys.get(i));
                            }
                        }
                    }

                } catch (Exception e) {
                }
            }
        }
        return map;
    }

    public void recursiveFun1(Map<String, Object> map, Map<String, List<Double>> rawStr, List<String> keyList) {
        String curKey = keyList.get(0);
        keyList.remove(0);
        List<String> groupBys;
        try {
            groupBys = new ArrayList<String>(Arrays.asList(curKey.split(",")));
        } catch (Exception e) {
            groupBys = new ArrayList<String>();
            String e1 = "dfsd";

        }
        if (!map.containsKey(groupBys.get(0))) {
            if (groupBys.size() == 1) {
                map.put(groupBys.get(0), map.get(curKey));
            } else {
                map.put(groupBys.get(0), new HashMap<String, Object>());
            }
        }
        Map hirarMap = (Map) map.get(groupBys.get(0));
        for (int i = 1; i < groupBys.size(); i++) {
            if (i == (groupBys.size() - 1)) {
                hirarMap.put(groupBys.get(i), rawStr.get(curKey));
            } else {
                if (!hirarMap.containsKey(groupBys.get(i))) {
                    hirarMap.put(groupBys.get(i), new HashMap());
                }
                hirarMap = (Map) hirarMap.get(groupBys.get(i));
            }
        }
        if (keyList.size() > 0) {
            recursiveFun1(map, rawStr, keyList);
        }
    }

    public Map processOverLayJson(Map<String, List<Double>> rawMap) {
        Map<String, List<Map<String, String>>> cutOfMap = new HashMap<String, List<Map<String, String>>>();
        Map<String, List<Double>> overAllRawMap = new HashMap<String, List<Double>>();
        Map map = new HashMap();
        String[] keys = (String[]) rawMap.keySet().toArray(new String[0]);
        Map<String, Double> cutOfSorter = new HashMap<String, Double>();
        for (String key : keys) {
            Map<String, String> tempMap = new HashMap<String, String>();
            List<Double> measureValues = rawMap.get(key);
            String[] viewsArray = key.split(",");
            tempMap.put(reportMeta.getViewbys().get(1), viewsArray[1]);
            if (cutOfSorter.containsKey(viewsArray[0])) {
                cutOfSorter.put(viewsArray[0], cutOfSorter.get(viewsArray[0]) + measureValues.get(0));
            } else {
                cutOfSorter.put(viewsArray[0], measureValues.get(0));
            }

            if (!overAllRawMap.containsKey(viewsArray[1])) {
                overAllRawMap.put(viewsArray[1], new ArrayList<Double>());
            }

            for (int k = 0; k < reportMeta.getMeasures().size(); k++) {
                tempMap.put(reportMeta.getMeasures().get(k), String.format("%.2f", measureValues.get(k)));
                if (overAllRawMap.get(viewsArray[1]).size() < (reportMeta.getMeasures().size())) {
                    overAllRawMap.get(viewsArray[1]).add(measureValues.get(k));
                } else {
                    Double temp = overAllRawMap.get(viewsArray[1]).get(k);
                    overAllRawMap.get(viewsArray[1]).remove(k);
                    overAllRawMap.get(viewsArray[1]).add(k, temp + measureValues.get(k));
                }

                if (k == reportMeta.getMeasures().size() - 1) {
                    overAllRawMap.get(viewsArray[1]).add(measureValues.get(reportMeta.getMeasures().size()));
                }
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
        List<String> tempViewBy = new ArrayList<String>(reportMeta.getViewbys());
        reportMeta.getViewbys().remove(0);
        List<Map<String, String>> overAll = processJson(overAllRawMap);
//        JsonComperator jsonComperator = new JsonComperator(reportMeta.getMeasures().get(0), "desc");
//        Collections.sort(overAll, jsonComperator);
        reportMeta.setViewbys(tempViewBy);
        List list = new LinkedList(cutOfSorter.entrySet());
        Collections.sort(list, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                return -((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        Map<String, List<Map<String, String>>> sortedCutOfMap = new LinkedHashMap<String, List<Map<String, String>>>();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
//            Collections.sort(cutOfMap.get(entry.getKey()), jsonComperator);
            sortedCutOfMap.put((String) entry.getKey(), cutOfMap.get(entry.getKey()));
        }
        map.put("overAll", overAll);
        map.put("cuts", sortedCutOfMap);
        return map;
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

                        temp = list.get(reportMeta.getMeasures().size());
                        list.remove(reportMeta.getMeasures().size());
                        list.add(reportMeta.getMeasures().size(), temp + measureValues.get(reportMeta.getMeasures().size() + j));
                    }
                } else {
                    map.put(key, chunkMap.get(key));
                }
            }
        }
        return map;
    }

    public boolean isValidate(String line) {
        String[] splits = line.split(",");
        boolean status = true;
        if (reportMeta.getDrills() != null && !reportMeta.getDrills().isEmpty()) {
            Set<String> keys = reportMeta.getDrills().keySet();
            for (String key : keys) {
                List<String> drillValues = reportMeta.getDrills().get(key);
                if (reportMeta.getHeaders().indexOf(key) == -1 && key.equalsIgnoreCase("month year")) {
                    key = "MonthYear";
                }
                String val = splits[reportMeta.getHeaders().indexOf(key)];
                if ((val.isEmpty() || drillValues.indexOf(val.trim()) == -1)) {
                    status = false;
                    break;
                }
            }
        }

        if (reportMeta.getFilterMap() != null && !reportMeta.getFilterMap().isEmpty()) {
            Set<String> keys = reportMeta.getFilterMap().keySet();
            for (String key : keys) {
                List<String> drillValues = reportMeta.getFilterMap().get(key);
                if (reportMeta.getHeaders().indexOf(key) == -1
                        && key.equalsIgnoreCase("Month Year")) {
                    key = "MonthYear";
                }
                String val = splits[reportMeta.getHeaders().indexOf(key)];
                if ((val.isEmpty() || drillValues.indexOf(val.trim()) != -1)) {
                    status = false;
                    break;
                }
                if (status) {
//                    
                }
            }
        }
        return status;
    }

    public boolean isValidateDriver(String line) {
        String[] splits = line.split(",");
        boolean status = true;
        if (reportMeta.getDrills() != null && !reportMeta.getDrills().isEmpty()) {
            Set<String> keys = reportMeta.getDrills().keySet();
            for (String key : keys) {
                List<String> drillValues = reportMeta.getDrills().get(key);
                if (reportMeta.getHeaders().indexOf(key) == -1 && key.equalsIgnoreCase("month year")) {
                    key = "MonthYear";
                }
                String val = splits[reportMeta.getHeaders().indexOf(key)];
                if ((val.isEmpty() || drillValues.indexOf(val.trim()) == -1)) {
                    status = false;
                    break;
                }
            }
        }

//        if (reportMeta.getFilterMap() != null && !reportMeta.getFilterMap().isEmpty()) {
//            Set<String> keys = reportMeta.getFilterMap().keySet();
//            for (String key : keys) {
//                List<String> drillValues = reportMeta.getFilterMap().get(key);
//                if(reportMeta.getHeaders().indexOf(key)==-1 
//                        && key.equalsIgnoreCase("Month Year"))
//                {
//                    key = "MonthYear";
//                }
//                String val = splits[reportMeta.getHeaders().indexOf(key)];
//                if ((val.isEmpty() || drillValues.indexOf(val.trim()) != -1)) {
//                    status = false;
//                    break;
//                }
//                if (status) {
////                    
//                }
//            }
//        }
        return status;
    }
}
