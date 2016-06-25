package com.progen.report;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.Container;

@SuppressWarnings("MalformedRegexp")
public class XtendReportJson {
    public static Logger logger=Logger.getLogger(XtendReportJson.class);
    List<String> headers;
    ArrayList<String> rowViewbyNames = new ArrayList<String>();
    List<String> measure;
    List<String> dimensions;

    public String getReportJsonData(Container container, String filePath, String filterPath, String csvName) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            Map<String, List<Float>> masterMap = new HashMap<String, List<Float>>();
            Map<String, List<String>> filterMap = new HashMap<String, List<String>>();
            measure = container.getReportMeasureNames();
            rowViewbyNames = (ArrayList) container.getTableHashMap().get("REPNames");
            headers = Arrays.asList(br.readLine().split(","));
            dimensions = container.getViewByColNames();
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] cells = line.split(",");
                List<Float> list = new ArrayList<Float>(1);
                String viewByList = cells[headers.indexOf(rowViewbyNames.get(0))];
                if (masterMap.containsKey(viewByList)) {
                    list = masterMap.get(viewByList);
                    for (int j = 0; j < list.size(); j++) {
                        float temp = list.get(j);
                        list.remove(j);
                        list.add(j, temp + Float.parseFloat(cells[headers.indexOf(measure.get(j))]));
                    }
                } else {
                    for (int j = 0; j < measure.size(); j++) {
                        list.add(Float.parseFloat(cells[headers.indexOf(measure.get(j))]));
                    }
                    masterMap.put(viewByList, list);
                }
                for (int key = 0; key < rowViewbyNames.size(); key++) {
                    String val = cells[headers.indexOf(rowViewbyNames.get(key))];
                    if (filterMap.containsKey(rowViewbyNames.get(key))) {
                        if (filterMap.get(rowViewbyNames.get(key)).indexOf(val) == -1) {
                            filterMap.get(rowViewbyNames.get(key)).add(val);
                        }
                    } else {
                        List<String> filterList = new ArrayList<String>();
                        filterList.add(val);
                        filterMap.put(rowViewbyNames.get(key), filterList);
                    }
                }
            }
            String csvFile = "@@" + csvName.replace(".csv", "").replace(".json", "");
            writeFilters(filterMap, rowViewbyNames, filterPath, csvFile);
            return processJson(masterMap);
        } catch (IOException ex) {
            return "";
        }
    }

    String processJson(Map<String, List<Float>> masterMap) {
        DecimalFormat format = new DecimalFormat("0.00");
        List<Map<String, String>> datalist = new ArrayList<Map<String, String>>();
        String[] keys = (String[]) masterMap.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            String[] key = keys[i].split("__");
            Map<String, String> map = new HashMap<String, String>();
//            for(int num=0;num<rowViewbyNames.size();num++){
//                map.put(rowViewbyNames.get(num),key[num]);
//            }
            map.put(rowViewbyNames.get(0), key[0]);
            for (int num = 0; num < measure.size(); num++) {
                map.put(measure.get(num), format.format(Math.round(masterMap.get(keys[i]).get(num))));
            }
            datalist.add(map);
        }
        Gson gson = new Gson();
        Type tarType = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        return gson.toJson(datalist, tarType);
    }

    String generateMetaData(Container container) {
        List<String> dataType = container.getDataTypes();
        List<Map<String, String>> metaList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < headers.size(); i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("columnName", headers.get(i));
            map.put("displayName", headers.get(i));
            map.put("searchName", headers.get(i));
            map.put("rounding", "1d");
            map.put("aggregation", "sum");
            map.put("isFilter", "yes");
            map.put("dataType", "string");
            metaList.add(map);
        }
        Gson gson = new Gson();
        Type tarType = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        return gson.toJson(metaList, tarType);
    }

    String getChartMeta(Container container, String dataCsv, String jsonData) {
        String reportName = container.getReportName();
        List<String> tempList = (ArrayList) container.getTableHashMap().get("REPNames");
        rowViewbyNames.add(tempList.get(0));
        List<String> curAggregation = new ArrayList<String>();
        for (int i = 0; i < container.getReportMeasureNames().size(); i++) {
            curAggregation.add("sum");
        }
        XtendReportMeta xtendRepoMeta = new XtendReportMeta();
        xtendRepoMeta.setReportName(reportName);
        xtendRepoMeta.setViewbys(rowViewbyNames);
        xtendRepoMeta.setDataPath(dataCsv);
        xtendRepoMeta.setJsonPath(jsonData);
//        "jsonPath":"jsonData/ProGen WC 1.json"
        xtendRepoMeta.setMeasures(container.getReportMeasureNames());
        xtendRepoMeta.setDimensions(container.getViewByColNames());
        xtendRepoMeta.setAggregations(curAggregation);
//        xtendRepoMeta.addChart("Bar");
        xtendRepoMeta.setIsMap(false);
        xtendRepoMeta.setTempJson("VBar_XTend1_temp.json");
        xtendRepoMeta.setOnDimSort("true");
        xtendRepoMeta.setIsHirarchichal(false);
        xtendRepoMeta.setChartType("single");
        xtendRepoMeta.setIsDashboard(false);
        xtendRepoMeta.setIsTable(true);
        xtendRepoMeta.setFilterParams(null);
        xtendRepoMeta.setDrillIn(null);
        xtendRepoMeta.setCrossTab("false");
        Gson gson = new Gson();
        Type tarType = new TypeToken<XtendReportMeta>() {
        }.getType();
        return gson.toJson(xtendRepoMeta, tarType);
    }

    void writeFilters(Map<String, List<String>> filterMap, List<String> rowViewbyNames, String filterPath, String csvFile) {
        BufferedWriter bw = null;
        try {
            Gson gson = new Gson();
            Type tarType = new TypeToken<List<String>>() {
            }.getType();
            for (int i = 0; i < rowViewbyNames.size(); i++) {
                String filters = gson.toJson(filterMap.get(rowViewbyNames.get(i)), tarType);
                File file = new File(filterPath + "/" + rowViewbyNames.get(i).replace("/", "-+-") + csvFile + ".json");

                if (!file.exists()) {
                    file.createNewFile();
                }
                bw = new BufferedWriter(new FileWriter(file));
                bw.write(filters);
                bw.close();
            }
        } catch (IOException ex) {
            logger.error("Exception:",ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                logger.error("Exception:",ex);
            } catch (NullPointerException ex) {
                logger.error("Exception:",ex);
            }
        }
    }
}
