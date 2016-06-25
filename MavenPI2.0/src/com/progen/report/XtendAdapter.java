package com.progen.report;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.db.ProgenDataSet;
import com.progen.report.data.DataFacade;
import com.progen.report.data.RowViewTableBuilder;
import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableHeaderRow;
import com.progen.report.display.CSVTableBodyDisplay;
import com.progen.report.display.CSVTableHeaderDisplay;
import com.progen.report.display.table.TableDisplay;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.servlet.ServletUtilities;
import com.progen.servlet.ServletWriterTransferObject;
import java.io.*;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.PbDisplayLabel;

public class XtendAdapter extends PbDb {

    public static Logger logger = Logger.getLogger(XtendAdapter.class);
    private int fromRow;
    private int toRow;
    public static String folderName;
    public static String privatePath;
    public static String dirJsonPath;
    public static String reportMasterPath;
    public static String dataPath;
    public static String metaDataPath;
    public static String jsonDataPath;
    public static String chartPath;
    public static String filterPath;
    public static String reportName;
    public static String collect;
    XtendReportMeta globalreportMeta;
    public static boolean savePoint = false;
    public static boolean refreshlocal = false;
    public static String userID = "";
    public static String roleId = "";
    List<String> headers = new ArrayList<String>();
    Map<String, Map<String, List<String>>> dirJson = new HashMap<String, Map<String, List<String>>>();

    static {

        Properties properties = getProperties("xtend.properties");
        privatePath = properties.getProperty("privatePath");
        reportMasterPath = properties.getProperty("reportMaster");
        dirJsonPath = properties.getProperty("dirJson");
        dataPath = properties.getProperty("dataPath");
        metaDataPath = properties.getProperty("metaPath");
        jsonDataPath = properties.getProperty("chartDataPath");
        chartPath = properties.getProperty("chartMetaPath");
        filterPath = properties.getProperty("filterPath");
        collect = properties.getProperty("collect");
    }

    public void generateAndStoreCSV(Container container, String userId, String csvNtopar, String fileName) {
        String filePath = System.getProperty("java.io.tmpdir") + "/" + fileName;
        PbReportCollection collect = container.getReportCollect();
        folderName = collect.getReportBizRoleName(collect.reportBizRoles[0]);
        reportName = collect.reportName;
        collect.tableColNames = container.getDisplayLabels();
        String dataCsv = reportName + "_" + folderName + ".csv";
        String metaDataJson = reportName + "_" + folderName + ".json";
        createBussFolder();
        XtendReportJson xtReport = new XtendReportJson();
        upLoadData(dataCsv, metaDataJson, filePath);
        uploadRepoJsonData(container, filePath, xtReport, dataCsv);
        uploadMetaData(container, metaDataJson, xtReport);
        String objectPath = privatePath + folderName + this.collect;
        File file = new File(objectPath + reportName + ".ser");
        FileOutputStream fileOut;
        try {
            if (!new File(objectPath.substring(0, objectPath.length() - 1)).exists()) {
                new File(objectPath.substring(0, objectPath.length() - 1)).mkdir();
            }
            fileOut = new FileOutputStream(file);
            ObjectOutputStream out;
            out = new ObjectOutputStream(fileOut);
            out.writeObject(collect);
            out.close();
            fileOut.close();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        uploadrepoMeta(container, dataCsv, collect.reportId, userId);
        new File(filePath).delete();
    }

    public String getFileName(String reportId, String userId) {
        return ServletUtilities.prefix + reportId + "_" + userId + "_DS";
    }

    public int getFromRow() {
        return fromRow;
    }

    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }

    public int getToRow() {
        return toRow;
    }

    public void setToRow(int toRow) {
        this.toRow = toRow;
    }

    public void createBussFolder() {

        if (!(new File(privatePath + folderName).exists())) {
            new File(privatePath + folderName).mkdir();
        }

        if (!(new File(privatePath + folderName + dataPath).exists())) {
            new File(privatePath + folderName + dataPath).mkdir();
        }

        if (!(new File(privatePath + folderName + metaDataPath).exists())) {
            new File(privatePath + folderName + metaDataPath).mkdir();
        }

        if (!(new File(privatePath + folderName + filterPath).exists())) {
            new File(privatePath + folderName + filterPath).mkdir();
        }

        if (!(new File(privatePath + folderName + chartPath).exists())) {
            new File(privatePath + folderName + chartPath).mkdir();
        }

        if (!(new File(privatePath + folderName + jsonDataPath).exists())) {
            new File(privatePath + folderName + jsonDataPath).mkdir();
        }

    }

    String generateCsv(Container container, String userId, String csvNtopar, PbReportCollection collect) {

        DataFacade facade = new DataFacade(container);
        facade.setUserId(userId);
        facade.setCtxPath(collect.ctxPath);
        TableBuilder tableBldr = new RowViewTableBuilder(facade);
        TableHeaderRow[] headerRows;
        headerRows = tableBldr.getHeaderRowData();
        int colCount = tableBldr.getColumnCount();
        StringBuilder header = new StringBuilder();
        String heading;

        if (fromRow == 0 && toRow == 0) {
            tableBldr.setFromAndToRow(0, container.getRetObj().getViewSequence().size());
        } else {
            tableBldr.setFromAndToRow(fromRow, toRow);
        }
        TableDisplay displayHelper = null;
        TableDisplay bodyHelper = null;
        ArrayList<String> timedetails = new ArrayList<String>();
        ArrayList<String> parameterDertails = new ArrayList<String>();
        if (!csvNtopar.equalsIgnoreCase("CSN")) {
            timedetails = container.getTimeDetailsArray();
            if (!timedetails.contains(container.getReportDesc())) {
                timedetails.add(container.getReportDesc());
                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.applyPattern("MM/dd/yyyy");
                timedetails.add(sdf.format(Calendar.getInstance().getTime()));
            }
            String viewbyvalues = "";
            String[] values = new String[collect.paramValueList.size()];
            if (collect.paramValueList != null) {
                for (int k = 0; k < collect.paramValueList.size(); k++) {
                    values = collect.paramValueList.get(k).toString().split(":");

                    viewbyvalues = values[1];

                    if (!viewbyvalues.equalsIgnoreCase("[All]")) {
                        String value = (String) collect.paramValueList.get(k);
                        if (!parameterDertails.contains(value)) {
                            parameterDertails.add(value);
                        }
                    }
                }
            }
        } else {
            timedetails.add("wihtoutParam");
            if (!timedetails.contains(container.getReportDesc())) {
                timedetails.add(container.getReportDesc());
                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.applyPattern("MM/dd/yyyy");
                timedetails.add(sdf.format(Calendar.getInstance().getTime()));
            }
        }
        displayHelper = new CSVTableHeaderDisplay(tableBldr, timedetails, parameterDertails);
        bodyHelper = new CSVTableBodyDisplay(tableBldr);
        ServletWriterTransferObject swt = null;
        for (TableHeaderRow headerRow : headerRows) {

            for (int i = tableBldr.getFromColumn(); i < colCount; i++) {
                heading = headerRow.getRowData(i);

                header.append(heading);
                headers.add(heading.toString());
                if (i != colCount - 1) {
                    header.append(",");
                }

            }
            header.append("\n");
        }
        StringBuilder tableHtml = new StringBuilder();
        tableHtml.append(header);
        tableHtml.append(bodyHelper.generateOutputHTML());

        return tableHtml.toString();
    }

    void upLoadData(String dataCsv, String metaDataJson, String filePath) {
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            updateDirJson(dataCsv, metaDataJson);
            File file = new File(privatePath + folderName + dataPath + "/" + dataCsv);
            File tempFile = new File(filePath);

            inStream = new FileInputStream(tempFile);
            outStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }

        } catch (IOException ioe) {
            logger.error("Exception:", ioe);
        } finally {
            try {
                inStream.close();
                outStream.close();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            } catch (NullPointerException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    void updateDirJson(String dataCsv, String metaDataJson) {
        BufferedWriter bw;
        BufferedReader br;
        String dirJsonTemp = "";
        try {
            File dirJsonFile = new File(privatePath + dirJsonPath);
            br = new BufferedReader(new FileReader(dirJsonFile));
            Gson gson = new Gson();
            Type tarType = new TypeToken<Map<String, Map<String, List<String>>>>() {
            }.getType();
            String st = br.readLine();
            if (st == null || st.equalsIgnoreCase("")) {
                st = "{}";
            }
            dirJsonTemp = st;
            dirJson = gson.fromJson(st, tarType);

//            Map<String, List<String>> map = new HashMap<>();
//            List<String> fileList = new ArrayList<>();
//            if (dirJson.containsKey(folderName)) {
//                map = dirJson.get(folderName);
//                map.get("data").add(dataCsv);
//                map.get("metadata").add(metaDataJson);
//            } else {
//                fileList.add(dataCsv);
//                map.put("data", fileList);
//                fileList.clear();
//                fileList.add(metaDataJson);
//                map.put("metadata", fileList);
//            }
//            dirJson.put(folderName, map);
            bw = new BufferedWriter(new FileWriter(dirJsonFile));
            bw.write(gson.toJson(dirJson));
            bw.close();
        } catch (IOException ioe) {
            logger.error("Exception:", ioe);
        } catch (Exception ioe) {
            logger.error("Exception:", ioe);
        }
    }

    void uploadMetaData(Container container, String metaDataJson, XtendReportJson xtReport) {
        String metaData = xtReport.generateMetaData(container);
        writeToFile(privatePath + folderName + metaDataPath + "/" + metaDataJson, metaData);
    }

    void uploadrepoMeta(Container container, String dataCsv, String reportID, String userID) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            XtendReportJson xtReportJson = new XtendReportJson();
            File reportMasterFile = new File(privatePath + reportMasterPath);
            br = new BufferedReader(new FileReader(reportMasterFile));
            Gson gson = new Gson();
            Type tarType = new TypeToken<Map<String, Map<String, Map<String, String>>>>() {
            }.getType();
            Map<String, Map<String, Map<String, String>>> reportMaster = new HashMap<String, Map<String, Map<String, String>>>();
            String st = br.readLine();
            if (st.equalsIgnoreCase("") || st == null) {
                st = "{}";
            }
            reportMaster = gson.fromJson(st, tarType);
            Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
            bw = new BufferedWriter(new FileWriter(reportMasterFile));
            if (!reportMaster.containsKey(reportName)) {

                Map<String, String> innerMap = new HashMap<String, String>();

                if (reportMaster.containsKey(folderName)) {
                    map = reportMaster.get(folderName);
                }
                innerMap.put("Created_on", new Date().toString());
                innerMap.put("reportMetaPath", folderName + chartPath + "/" + reportName + ".json");
                innerMap.put("dataCSV", dataCsv);
                innerMap.put("reportID", reportID);
                innerMap.put("userID", userID);
                map.put(reportName, innerMap);
                reportMaster.put(folderName, map);

            }
            bw.write(gson.toJson(reportMaster, tarType));
            String metaData = xtReportJson.getChartMeta(container, folderName + "/" + dataCsv, jsonDataPath.replace("/", "") + "/" + reportName + ".json");
            writeToFile(privatePath + folderName + chartPath + "/" + reportName + ".json", metaData);
        } catch (IOException ioe) {
            logger.error("Exception:", ioe);
        } finally {
            try {
                br.close();
                bw.close();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            } catch (NullPointerException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    void uploadRepoJsonData(Container container, String filePath, XtendReportJson xtReport, String dataCsv) {
        String filter = privatePath + folderName + filterPath + "/";
        String jsonData = xtReport.getReportJsonData(container, filePath, filter, dataCsv);
        writeToFile(privatePath + folderName + jsonDataPath + "/" + reportName + ".json", jsonData);
    }

    void writeToFile(String path, String data) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(new File(path)));
            bw.write(data);
        } catch (IOException ioe) {
            logger.error("Exception:", ioe);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public static Properties getProperties(String resource) {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream inputStrem = loader.getResourceAsStream(resource);
        if (inputStrem != null) {
            try {
                properties.load(inputStrem);
            } catch (IOException e) {
            }
        }
        return properties;
    }

    public void createCSV(ProgenDataSet pbretObj, String reportId, String reportName, List<String> paramNames) {
        String folderName1 = getFolderName(reportId);
        File file = new File(privatePath + folderName1 + dataPath + reportName + "_" + folderName1 + ".csv");
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            for (int m = 0; m < pbretObj.rowCount; m++) {
//                String br = "";
                StringBuilder br = new StringBuilder();
                if (m == 0) {
                    for (String paramName : paramNames) {
                        if (paramNames.indexOf(paramName) > 0) {
//                            br += ",";
                            br.append(",");
                        }
//                        br += paramName;
                        br.append(paramName);
                    }
                    // br += System.lineSeparator();
                }
                for (int k = 0; k < paramNames.size(); k++) {
                    if (k != 0) {
//                        br += ",";
                        br.append(",");
                    }
//                    br += pbretObj.getFieldValueString(m, k);
                    br.append(pbretObj.getFieldValueString(m, k));
//                    if (k == pbretObj.getColumnNames().length - 1) {
//                  //     br += System.lineSeparator();
//                    }
                }
                fw.append(br);
            }
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
    }

    public void createCSVForReport(ProgenDataSet pbretObj, String reportId, String reportName, List<String> paramNames, String chartId, String bizzRole) {
//        String folderName1 = getFolderName(reportId);
//        File file = new File(privatePath + folderName1 + dataPath + reportName +"_"+folderName1+ ".csv");

//        if(chartId.equalsIgnoreCase("Local")){
//        file = new File( "/usr/local/cache/"+reportName+"_"+reportId+".csv");
//        }
//        else{
//        String path = "C:"+File.separator+"hello";
//String fname= path+File.separator+"abc.txt";
//    File f = new File(path);
//    File f1 = new File(fname);
//
//    f.mkdirs() ;
//    try {
//        f1.createNewFile();
//    } catch (IOException e) {
//        // TODO Auto-generated catch block
//        logger.error("Exception:",e);
//    }

        Map<String, List<String>> filterMap = new HashMap<String, List<String>>();
        File file;
        file = new File("/usr/local/cache/" + bizzRole.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId);

        String path = file.getAbsolutePath();
        String fName = path + File.separator + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ".csv";
        File f = new File(path);
        File file1 = new File(fName);
        f.mkdirs();

        try {
            file1.createNewFile();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

//        }
        FileWriter fw;

        try {
            fw = new FileWriter(file1);
            for (int m = 0; m < pbretObj.rowCount; m++) {
//                String br = "";
                StringBuilder br = new StringBuilder();
                if (m == 0) {
                    for (String paramName : paramNames) {
                        if (paramNames.indexOf(paramName) > 0) {
//                            br += ",";
                            br.append(",");
                        }
//                        br += paramName.replace(",","");
                        br.append(paramName.replace(",", ""));
                    }
//                    br += "\n";
                    br.append("\n");
//                    br += System.lineSeparator();
                }
                for (int k = 0; k < paramNames.size(); k++) {
                    if (k != 0) {
//                        br += ",";
                        br.append(",");
                    }
//                    br += pbretObj.getFieldValueString(m, k).replace(",","");
                    br.append(pbretObj.getFieldValueString(m, k).replace(",", ""));
                    if (k == paramNames.size() - 1) {
//                        br += "\n";
                        br.append("\n");
//                    br += System.lineSeparator();
                    }


                    String val = pbretObj.getFieldValueString(m, k).replace(",", "");
                    if (filterMap.containsKey(paramNames.get(k))) {
                        if (filterMap.get(paramNames.get(k)).indexOf(val) == -1) {
                            filterMap.get(paramNames.get(k)).add(val);
                        }
                    } else {
                        List<String> filterList = new ArrayList<String>();
                        filterList.add(val);
                        filterMap.put(paramNames.get(k), filterList);
                    }

                }
                fw.append(br);
            }
            fw.flush();
            fw.close();

            String csvFile = reportName + "_" + reportId + ".json";
            String filterPath = "/usr/local/cache/" + bizzRole.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/filters";
            writeFilters(filterMap, paramNames, paramNames, filterPath, csvFile);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
    }

    public void createCSVForoneview(ProgenDataSet pbretObj, String reportId, String reportName, List<String> paramNames, String chartId, String bizzRole) {
//        String folderName1 = getFolderName(reportId);
//        File file = new File(privatePath + folderName1 + dataPath + reportName +"_"+folderName1+ ".csv");

//        if(chartId.equalsIgnoreCase("Local")){
//        file = new File( "/usr/local/cache/"+reportName+"_"+reportId+".csv");
//        }
//        else{
//        String path = "C:"+File.separator+"hello";
//String fname= path+File.separator+"abc.txt";
//    File f = new File(path);
//    File f1 = new File(fname);
//
//    f.mkdirs() ;
//    try {
//        f1.createNewFile();
//    } catch (IOException e) {
//        // TODO Auto-generated catch block
//        logger.error("Exception:",e);
//    }

        Map<String, List<String>> filterMap = new HashMap<String, List<String>>();
        File file;
        file = new File("/usr/local/cache/" + bizzRole.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId);

        String path = file.getAbsolutePath();
        String fName = path + File.separator + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ".csv";
        File f = new File(path);
        File file1 = new File(fName);
        f.mkdirs();

        try {
            file1.createNewFile();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

//        }
        FileWriter fw;

        try {
            fw = new FileWriter(file1);
            for (int m = 0; m < pbretObj.rowCount; m++) {
//                String br = "";
                StringBuilder br = new StringBuilder();
                if (m == 0) {
                    for (String paramName : paramNames) {
                        if (paramNames.indexOf(paramName) > 0) {
//                            br += ",";
                            br.append(",");
                        }
//                        br += paramName.replace(",","");
                        br.append(paramName.replace(",", ""));
                    }
//                    br += "\n";
                    br.append("\n");
//                    br += System.lineSeparator();
                }
                for (int k = 0; k < paramNames.size(); k++) {
                    if (k != 0) {
//                        br += ",";
                        br.append(",");
                    }
//                    br += pbretObj.getFieldValueString(m, k).replace(",","");
                    br.append(pbretObj.getFieldValueString(m, k).replace(",", ""));
                    if (k == paramNames.size() - 1) {
//                        br += "\n";
                        br.append("\n");
//                    br += System.lineSeparator();
                    }


                    String val = pbretObj.getFieldValueString(m, k).replace(",", "");
                    if (filterMap.containsKey(paramNames.get(k))) {
                        if (filterMap.get(paramNames.get(k)).indexOf(val) == -1) {
                            filterMap.get(paramNames.get(k)).add(val);
                        }
                    } else {
                        List<String> filterList = new ArrayList<String>();
                        filterList.add(val);
                        filterMap.put(paramNames.get(k), filterList);
                    }

                }
                fw.append(br.toString());
            }
            fw.flush();
            fw.close();

            String csvFile = reportName + "_" + reportId + ".json";
            String filterPath = "/usr/local/cache/" + bizzRole.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/filters";
            writeFilters(filterMap, paramNames, paramNames, filterPath, csvFile);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
    }

    public void createCSVForTrend(ProgenDataSet pbretObj, String reportId, String reportName, List<String> paramNames, String chartId, String bizzRole) {
//        String folderName1 = getFolderName(reportId);
//        File file = new File(privatePath + folderName1 + dataPath + reportName +"_"+folderName1+ ".csv");

//        if(chartId.equalsIgnoreCase("Local")){
//        file = new File( "/usr/local/cache/"+reportName+"_"+reportId+".csv");
//        }
//        else{
//        String path = "C:"+File.separator+"hello";
//String fname= path+File.separator+"abc.txt";
//    File f = new File(path);
//    File f1 = new File(fname);
//
//    f.mkdirs() ;
//    try {
//        f1.createNewFile();
//    } catch (IOException e) {
//        // TODO Auto-generated catch block
//        logger.error("Exception:",e);
//    }
        File file;
        file = new File("/usr/local/cache/" + bizzRole.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_trend");

        String path = file.getAbsolutePath();
        String fName = path + File.separator + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_trend.csv";
        File f = new File(path);
        File file1 = new File(fName);
        f.mkdirs();

        try {
            file1.createNewFile();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

//        }
        FileWriter fw;

        try {
            fw = new FileWriter(file1);
            for (int m = 0; m < pbretObj.rowCount; m++) {
//                String br = "";
                StringBuilder br = new StringBuilder();
                if (m == 0) {
                    for (String paramName : paramNames) {
                        if (paramNames.indexOf(paramName) > 0) {
//                            br += ",";
                            br.append(",");
                        }
//                        br += paramName.replace(",","");
                        br.append(paramName.replace(",", ""));
                    }
//                    br += "\n";
                    br.append("\n");
//                    br += System.lineSeparator();
                }
                for (int k = 0; k < paramNames.size(); k++) {
                    if (k != 0) {
//                        br += ",";
                        br.append(",");
                    }
//                    br += pbretObj.getFieldValueString(m, k).replace(",","");
                    br.append(pbretObj.getFieldValueString(m, k).replace(",", ""));
                    if (k == paramNames.size() - 1) {
//                        br += "\n";
                        br.append("\n");
//                    br += System.lineSeparator();
                    }
                }
                fw.append(br.toString());
            }
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
    }

    public String getFolderName(String reportId) {
        try {
            String qry = "select b.folder_name from PRG_AR_REPORT_DETAILS a ,PRG_USER_FOLDER b where a.folder_id= b.folder_id and a.report_id=" + reportId;
            PbReturnObject pbro = executeSelectSQL(qry);
            String folderName1 = pbro.getFieldValueString(0, 0);
            return folderName1;
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
            return "";
        }
    }

    public String saveoneviewChartMeta(String reportName, String reportId, String chartMeta, String data, String oneviewid, String regid, String chartType, String key, String visualChartType) {
//       if(type.equalsIgnoreCase("chart")){
        FileReadWrite fileReadWrite = new FileReadWrite();
        File file1;
        file1 = new File("/usr/local/cache/OneviewGO/oneview_" + oneviewid + "/oneview_" + oneviewid + "_" + regid);
        String path = file1.getAbsolutePath();
//        String fName = path+File.separator+reportName.replaceAll("\\W", "").trim()+"_"+reportId+".json";
        File f = new File(path);
        f.mkdirs();
        File file = new File("/usr/local/cache/OneviewGO/oneview_" + oneviewid + "/oneview_" + oneviewid + "_" + regid + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data.json");
//       File datafile = new File("/usr/local/cache/OneviewGO/oneview_"+oneviewid+"/oneview_"+oneviewid+"_"+regid+"/"+reportName.replaceAll("\\W", "").trim()+"_"+reportId+".json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        fileReadWrite.writeToFile("/usr/local/cache/OneviewGO/oneview_" + oneviewid + "/oneview_" + oneviewid + "_" + regid + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data.json", chartMeta);
        fileReadWrite.writeToFile("/usr/local/cache/OneviewGO/oneview_" + oneviewid + "/oneview_" + oneviewid + "_" + regid + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ".json", data);
        return "";
//   }
    }

    public String saveChartMeta(String reportName, String reportId, String chartMeta, String data, String bizzRoleName, String type, String chartType, String key, String visualChartType, String fileLocation,boolean publish,HttpSession session,String currentPage) {
        if (type.equalsIgnoreCase("chart")) {
            /***Added by Ashutosh for graphs save points***/
           if(currentPage.equalsIgnoreCase("default")){
               currentPage="";
           }
            if (XtendAdapter.savePoint) {
//               HttpSession session = SessionListener.getSession(SessionListener.getSessionID());
                String userId = session.getAttribute("USERID").toString();
//                HashMap roleMap = (HashMap)session.getAttribute("roleMap");
//                String roleId = roleMap.get(reportId).toString();
                String roleId = XtendAdapter.roleId;
                FileReadWrite fileReadWrite = new FileReadWrite();
                File file1;
                file1 = new File(fileLocation + "/SavePoint/" + userId + "/" + roleId + "/" + reportId + "/");
                String path = file1.getAbsolutePath();
                //String fName = path + File.separator + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ".json";
                File f = new File(path);
                f.mkdirs();
                File file = new File(fileLocation + "/SavePoint/" + userId + "/"+ roleId + "/" + reportId + "/" + reportId + "_data"+currentPage+".json");
                File datafile = new File(fileLocation + "/SavePoint/" + userId + "/"+ roleId + "/" + reportId + "/" + reportId + ""+currentPage+".json");
                if (!file.exists()) {
                    try {

                        file.createNewFile();
                    } catch (IOException ex) {
                        logger.error("Exception:", ex);
                    }
                }
                fileReadWrite.writeToFile(fileLocation + "/SavePoint/" + userId + "/"+ roleId + "/" + reportId + "/"  + reportId + "_data"+currentPage+".json", chartMeta);
                fileReadWrite.writeToFile(fileLocation + "/SavePoint/" + userId + "/"+ roleId + "/" + reportId + "/" + reportId + ""+currentPage+".json", data);
                if(publish){
                    file1 = new File(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId);
                    path = file1.getAbsolutePath();
                String fName = path + File.separator + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ""+currentPage+".json";
                    f = new File(path);
                    f.mkdirs();
                 file = new File(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data"+currentPage+".json");
                 datafile = new File(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ""+currentPage+".json");
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException ex) {
                            logger.error("Exception:", ex);
                        }
                    }
                fileReadWrite.writeToFile(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data"+currentPage+".json", chartMeta);
                fileReadWrite.writeToFile(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ""+currentPage+".json", data);
                }

                String goPath = fileLocation + "/analyticalobject/R_GO_" + reportId + ".json";
                String goData = fileReadWrite.loadJSON(goPath);
                Type goType = new TypeToken<ReportObjectMeta>() {
                }.getType();
                ReportObjectMeta goMeta = new ReportObjectMeta();
                Gson gson = new Gson();
                goMeta = gson.fromJson(goData, goType);

                Type metaType = new TypeToken<XtendReportMeta>() {
                }.getType();
                XtendReportMeta reportMeta = new XtendReportMeta();
                reportMeta = gson.fromJson(chartMeta, metaType);
                goMeta.setViewIds(reportMeta.getViewbysIds());
                goMeta.setViewNames(reportMeta.getViewbys());
                goMeta.setMeasIds(reportMeta.getMeasuresIds());
                goMeta.setMeasNames(reportMeta.getMeasures());
                goMeta.setAggregations(reportMeta.getAggregations());
                fileReadWrite.writeToFile(goPath, gson.toJson(goMeta));
//                XtendAdapter.savePoint = false;
                return "";

            } else {
                FileReadWrite fileReadWrite = new FileReadWrite();
                File file1;
                file1 = new File(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId);
                String path = file1.getAbsolutePath();
                String fName = path + File.separator + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ".json";
                File f = new File(path);
                f.mkdirs();
                File file = new File(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data"+currentPage+".json");
                File datafile = new File(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ""+currentPage+".json");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException ex) {
                        logger.error("Exception:", ex);
                    }
                }
                fileReadWrite.writeToFile(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data"+currentPage+".json", chartMeta);
                fileReadWrite.writeToFile(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ""+currentPage+".json", data);
                String goPath = fileLocation + "/analyticalobject/R_GO_" + reportId + ".json";
                String goData = fileReadWrite.loadJSON(goPath);
                Type goType = new TypeToken<ReportObjectMeta>() {
                }.getType();
                ReportObjectMeta goMeta = new ReportObjectMeta();
                Gson gson = new Gson();
                goMeta = gson.fromJson(goData, goType);

                Type metaType = new TypeToken<XtendReportMeta>() {
                }.getType();
                XtendReportMeta reportMeta = new XtendReportMeta();
                reportMeta = gson.fromJson(chartMeta, metaType);
                goMeta.setViewIds(reportMeta.getViewbysIds());
                goMeta.setViewNames(reportMeta.getViewbys());
                goMeta.setMeasIds(reportMeta.getMeasuresIds());
                goMeta.setMeasNames(reportMeta.getMeasures());
                goMeta.setAggregations(reportMeta.getAggregations());
                fileReadWrite.writeToFile(goPath, gson.toJson(goMeta));
                XtendAdapter.savePoint = true;
                return "";
            }
        } else if (type.equalsIgnoreCase("trend")) {

//          String filePath = "/usr/local/cache/"+bizzRoleName.replaceAll("\\W","").trim()+"/"+reportName.replaceAll("\\W","").trim()+"_"+reportId+"/"+reportName.replaceAll("\\W","").trim()+"_"+reportId+"_trend.json";
//String goPath = "/usr/local/cache/analyticalobject/R_GO_"+reportId+".json";
//String metaFilePath = "/usr/local/cache/"+bizzRoleName.replaceAll("\\W","").trim()+"/"+reportName.replaceAll("\\W","").trim()+"_"+reportId+"/"+reportName.replaceAll("\\W","").trim()+"_"+reportId+"_data_trend.json";

            FileReadWrite fileReadWrite = new FileReadWrite();
            File file1;
            file1 = new File(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId);
            String path = file1.getAbsolutePath();
            String fName = path + File.separator + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_trend.json";
            File f = new File(path);
            f.mkdirs();
            File file = new File(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data_trend.json");
            File datafile = new File(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_trend.json");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }
            }
            fileReadWrite.writeToFile(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data_trend.json", chartMeta);
            fileReadWrite.writeToFile(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_trend.json", data);
            return "";
        } else {
            FileReadWrite fileReadWrite = new FileReadWrite();
            File file = new File(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_" + key + ".json");
            File infofile = new File(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_visualData.json");
            File datafile = new File(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + key + ".json");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }
            }
            fileReadWrite.writeToFile(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_" + key + ".json", chartMeta);
            fileReadWrite.writeToFile(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_visualData.json", visualChartType);
            fileReadWrite.writeToFile(fileLocation + "/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + key + ".json", data);
            return "";
        }
    }

    public String readReportMaster() {
        BufferedReader br = null;
        BufferedReader br1 = null;
        BufferedWriter bw = null;
        String st = "";
        String st1 = "";
        Map<String, String> reportMap = new HashMap<String, String>();
        File reportMasterFile = new File(privatePath + reportMasterPath);
        try {
            br = new BufferedReader(new FileReader(reportMasterFile));
        } catch (FileNotFoundException ex) {
            logger.error("Exception:", ex);
        }
        Gson gson = new Gson();
        Type tarType = new TypeToken<Map<String, Map<String, Map<String, String>>>>() {
        }.getType();
        Map<String, Map<String, Map<String, String>>> reportMaster = new HashMap<String, Map<String, Map<String, String>>>();

        try {
            st = br.readLine();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        if (st.equalsIgnoreCase("") || st == null) {
            st = "{}";
        }
        reportMaster = gson.fromJson(st, tarType);
        Set s1 = reportMaster.keySet();
        Iterator iterator = s1.iterator();
        while (iterator.hasNext()) {
            XtendReportMeta reportMeta = new XtendReportMeta();
            String dir = iterator.next().toString();
            Set s2 = reportMaster.get(dir).keySet();
            Iterator iterator1 = s2.iterator();
            while (iterator1.hasNext()) {
                String report = iterator1.next().toString();
                File metaFile = new File(privatePath + dir + "/my_charts/" + report + ".json");
                if (metaFile.isFile()) {
                    Type tarType1 = new TypeToken<XtendReportMeta>() {
                    }.getType();


                    try {
                        br1 = new BufferedReader(new FileReader(metaFile));
                    } catch (FileNotFoundException ex) {
                        logger.error("Exception:", ex);
                    }
                    try {
                        st1 = br1.readLine();
                    } catch (IOException ex) {
                        logger.error("Exception:", ex);
                    }

                    reportMeta = gson.fromJson(st1, tarType1);
                    if (reportMeta.getIsMap()) {
                        reportMap.put(report, dir);
                    }
                }
            }
        }
        return gson.toJson(reportMap);
    }

    public String saveTrendMeta(String reportName, String reportId, String chartMeta, String data, String bizzRoleName) {
        FileReadWrite fileReadWrite = new FileReadWrite();
        File file = new File("/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data_trend.json");
        File datafile = new File("/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_trend.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        fileReadWrite.writeToFile("/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_data_trend.json", chartMeta);
        fileReadWrite.writeToFile("/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_trend.json", data);
        return "";
    }

    public void writeFilters(Map<String, List<String>> filterMap, List<String> rowViewbys, List<String> rowViewbyNames, String filterPath, String csvFile) {
        BufferedWriter bw = null;
        try {
            String filters = "";
            Gson gson = new Gson();
            Type tarType = new TypeToken<List<String>>() {
            }.getType();
            for (int i = 0; i < rowViewbyNames.size(); i++) {
                List<String> list = filterMap.get(rowViewbyNames.get(i));
                if (list != null) {
                    Collections.sort(list);
                    filters = gson.toJson(list, tarType);
                }
                if (rowViewbys.get(i) != null) {
                    File file = new File(filterPath + "/" + rowViewbys.get(i).replace("/", "-+-") + csvFile);
                    String path = filterPath;
                    String fName = path + File.separator + rowViewbyNames.get(i) + csvFile;
                    File f = new File(path);
                    File file1 = new File(fName);
                    f.mkdirs();
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    bw = new BufferedWriter(new FileWriter(file));
                    bw.write(filters);
                    bw.close();
                }
            }
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            } catch (NullPointerException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public String getFilters(String reportId, String fileLocation, HttpServletRequest request) {
        Map<String, List<String>> allFilters = new HashMap<String, List<String>>();
        DashBoardDataGenerator dashBoardDataGenerator = new DashBoardDataGenerator();
        Gson gson = new Gson();
        Map<String, List<String>> allFilters1 = new HashMap<String, List<String>>();
        List<String> parameterlist = new ArrayList<String>();
        List<String> viewbynames = new ArrayList<String>();
        HttpSession session = request.getSession(false);
        String typegbl = request.getParameter("typegbl");
        String loadtime = (String) request.getAttribute("typegbl");
        Type type1 = new TypeToken<ReportObjectMeta>() {
        }.getType();
        ReportObjectMeta reportMeta = new ReportObjectMeta();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> groupByList = new ArrayList<String>();
        List<String> groupByNameList = new ArrayList<String>();

        String[] dirs = dataPath.split("/");


        String filterPath;
        filterPath = fileLocation + "/analyticalobject/R_GO_" + reportId + ".json";
        FileReadWrite fileReadWrite = new FileReadWrite();

        File file = new File(filterPath);
//                String cont;
        if (file.exists()) {
            reportMeta = gson.fromJson(fileReadWrite.loadJSON(filterPath), type1);


            PbDisplayLabel dispLabel = PbDisplayLabel.getPbDisplayLabel();
            if (dispLabel != null) {
                String def_id = dispLabel.getDefaultCompanyId();

                if (dispLabel != null && reportMeta.getViewIds() != null) {
                    List<String> viewLabel = new ArrayList<>();
                    for (int i = 0; i < reportMeta.getViewIds().size(); i++) {
                        String id = reportMeta.getViewIds().get(i);
                        String viewName = reportMeta.getViewNames().get(i);
                        if (id != null && dispLabel.getColDisplayWithCompany(def_id, id) != null) {
                            viewLabel.add(dispLabel.getColDisplayWithCompany(def_id, id));
                        } else {
                            viewLabel.add(viewName);
                        }
                    }
                    reportMeta.setViewNames(viewLabel);
                }
            }
            groupByList = reportMeta.getViewIds();
            groupByNameList = reportMeta.getViewNames();
            int flag = 0;
            for (String groupBy : groupByNameList) {
                filterPath = fileLocation + "/analyticalobject/filters/R_GO_" + reportId + "/" + groupBy + ".json";
                String cont = "{}";
                if (typegbl != null && typegbl.equalsIgnoreCase("graph")) {
                    cont = fileReadWrite.loadJSON(filterPath);
                }
                if (cont == "{}") {
                    cont = "[]";
                }
                String timeformat = "";
                List<String> list = gson.fromJson(cont, type);
                List<Double> list1 = gson.fromJson(cont, type);
                if (groupBy != null) {
                    if (groupBy != null && (groupBy.equalsIgnoreCase("Year") || groupBy.toString().toLowerCase().contains("month") || groupBy.toString().toLowerCase().contains("time"))) {
                        DateComperator dateComperator = new DateComperator(groupBy.toString(), "asc", null);
                        List<Map<String, String>> list3 = new ArrayList<Map<String, String>>();
                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                Map<String, String> mapList1 = new HashMap<String, String>();
                                if (list.get(i) != "" && !list.get(i).equalsIgnoreCase("")) {
                                    mapList1.put(groupBy, list.get(i));
                                    list3.add(mapList1);
                                    if (groupBy.toString().toLowerCase().contains("time") && list.get(i).contains("Q")) {
                                        timeformat = "Qtr";
                                    }
                                }
                            }
                        }
                        List<String> list4 = new ArrayList<String>();
                        if (groupBy.toString().toLowerCase().contains("time") && (timeformat == null ? "" != null : !timeformat.equals("")) && timeformat.equalsIgnoreCase("Qtr")) {
                            list4 = list;
                        } else {
                            if (list3 != null) {
                                Collections.sort(list3, dateComperator);
                                for (int i = 0; i < list3.size(); i++) {
                                    Set<String> keys = list3.get(i).keySet();
                                    for (String key : keys) {
                                        list4.add(list3.get(i).get(key));
                                    }
                                }
                            }
                        }
                        allFilters.put(groupBy, list4);
                        allFilters1.put(groupByList.get(flag), list4);
                        parameterlist.add(groupByList.get(flag));
                        viewbynames.add(groupBy);
                        flag++;
                        //  List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
//                     Map<String, List<Double>> map = new HashMap<String,List<Double>>();
//                     map.put(groupByList.get(0),list1);
//                     List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
//                      List<Map<String, String>> mapList = new HashMap<String, String>();
//                      mapList.add(map);
//                      List<Map<String, String>> mapList = dashBoardDataGenerator.processJson1(map,reportMeta);

                    } else {
                        allFilters.put(groupBy, list);
                        allFilters1.put(groupByList.get(flag), list);
                        parameterlist.add(groupByList.get(flag));
                        viewbynames.add(groupBy);
                        flag++;
                    }
                }
            }
            request.setAttribute("allFiltersnames", allFilters);
            request.setAttribute("allFilters", allFilters1);
            request.setAttribute("parameterlist", parameterlist);
            request.setAttribute("viewbynames", viewbynames);
            session.setAttribute("reportMeta", reportMeta);
            //added by sruthi to show filters
            if (request.getParameter("action") != null && request.getParameter("action").equalsIgnoreCase("open")) {
                session.setAttribute("graphselectdata", reportMeta.getgraphfiltersize());//ended by sruthi
            }
            PbReportViewerDAO dao = new PbReportViewerDAO();
            String reportName = request.getParameter("reportName");
            String fromoneview = request.getParameter("fromoneview");
            String busrolename = request.getParameter("busrolename");
            String filePath = "";
            Container container = Container.getContainerFromSession(request, reportId);
            if (session != null) {
                filePath = dao.getFilePath(session);
            } else {
                filePath = "/usr/local/cache";
            }
            if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
                container = null;
            } else {
//reportName=container.getReportName();
////        Gson gson = new Gson();
//          HashMap map1 = new HashMap();
//          Map<String, List<String>> FilterMap = new HashMap<String, List<String>>();
//          Map<String, List<String>> advFilterMap = new HashMap<String, List<String>>();
//          Map<String, List<String>> TrendFilterMap = new HashMap<String, List<String>>();
//        Type tarType1 = new TypeToken<Map<String, String>>() {
//        }.getType();
//
//         Type metaType = new TypeToken<XtendReportMeta>() {
//        }.getType();
//                  XtendReportMeta reportMeta1 = new XtendReportMeta();
//                   if(typegbl!=null && typegbl.equalsIgnoreCase("graph") || loadtime!=null && loadtime.equalsIgnoreCase("true")){
// String report = dao.getReports(reportId,reportName,container,fromoneview,busrolename,filePath);
//                    
//       if(report!=null && (report == null ? "" != null : !report.equals(""))  &&  (!report.equalsIgnoreCase("false"))){
//          map1 = gson.fromJson(report, tarType1);
//          String metadata=(String) map1.get("meta");
//reportMeta1 = gson.fromJson(metadata, metaType);
//FilterMap=reportMeta1.getFilterMap();
//session.setAttribute("FilterMap", FilterMap);
//
//        }
//           }
//                   if(typegbl!=null && typegbl.equalsIgnoreCase("advance") || loadtime!=null && loadtime.equalsIgnoreCase("true")){
// String reportadv = dao.Visualsfiltermap(reportId,reportName,container,filePath);
//if(reportadv!=null && (reportadv == null ? "" != null : !reportadv.equals("")) &&  (!reportadv.equalsIgnoreCase("false"))){
//map1 = new HashMap();
//          map1 = gson.fromJson(reportadv, tarType1);
//          String metadata=(String) map1.get("meta");
//          reportMeta1 = new XtendReportMeta();
//reportMeta1 = gson.fromJson(metadata, metaType);
//
//advFilterMap=reportMeta1.getFilterMap();
//session.setAttribute("advFilterMap", advFilterMap);
//
//        }
//                       }
//                  if(typegbl!=null && typegbl.equalsIgnoreCase("trend") || loadtime!=null && loadtime.equalsIgnoreCase("true")){
// String reporttrend = dao.getReportsT(reportId,reportName,container,filePath);
// if(reporttrend!=null && (reporttrend == null ? "" != null : !reporttrend.equals("")) &&  (!reporttrend.equalsIgnoreCase("false"))){
//map1 = new HashMap();
//          map1 = gson.fromJson(reporttrend, tarType1);
//          String metadata=(String) map1.get("meta");
//          reportMeta1 = new XtendReportMeta();
//reportMeta1 = gson.fromJson(metadata, metaType);
//
//TrendFilterMap=reportMeta1.getFilterMap();
//session.setAttribute("TrendFilterMap", TrendFilterMap);
//
//        }
//           }
            }
        }

        return gson.toJson(allFilters);
    }
//sandeep

    public String getAjaxFilters(String reportId, String fileLocation, HttpServletRequest request, String startValue1) {
        Map<String, List<String>> allFilters = new HashMap<String, List<String>>();
        Map<String, List<String>> allFilters1 = new HashMap<String, List<String>>();
        List<String> parameterlist = new ArrayList<String>();
        List<String> viewbynames = new ArrayList<String>();
        Gson gson = new Gson();
        Type type1 = new TypeToken<ReportObjectMeta>() {
        }.getType();
        HttpSession session = request.getSession(false);
        ReportObjectMeta reportMeta = new ReportObjectMeta();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> groupByList = new ArrayList<String>();
        List<String> groupByNameList = new ArrayList<String>();
        String fromajaxtype = request.getParameter("fromajaxtype");
        String elementid = request.getParameter("elementid");
        String startValue = startValue1;
        String[] dirs = dataPath.split("/");
        ArrayList a = new ArrayList();

        String filterPath;
        filterPath = fileLocation + "/analyticalobject/R_GO_" + reportId + ".json";
        FileReadWrite fileReadWrite = new FileReadWrite();


//                String cont;
//                reportMeta = gson.fromJson(fileReadWrite.loadJSON(filterPath),type1);
        reportMeta = (ReportObjectMeta) session.getAttribute("reportMeta");
        groupByList = reportMeta.getViewIds();
        groupByNameList = reportMeta.getViewNames();
        int flag = 0;
        for (String groupBy : groupByNameList) {
            filterPath = fileLocation + "/analyticalobject/filters/R_GO_" + reportId + "/" + groupBy + ".json";
            String cont;
            cont = fileReadWrite.loadJSON(filterPath);
            if (cont == "{}") {
                cont = "[]";
            }
            if (groupByList.get(flag).equalsIgnoreCase(elementid)) {
                List<String> list = gson.fromJson(cont, type);

                if (startValue != null && (startValue == null ? "" != null : !startValue.equals(""))) {
                    int size = Integer.parseInt(startValue) + 20;
                    for (int i = Integer.parseInt(startValue); i < size; i++) {
                        if (list.size() >= i) {
                            a.add(list.get(i - 1));
                        }
                    }
                } else {
                    for (int i = 0; i < 20; i++) {
                        if (list.size() > i) {
                            a.add(list.get(i));
                        }
                    }
                }

                break;
            }

            flag++;
        }
        String s[] = new String[a.size()];
        a.toArray(s);
//                    String value = "";
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
//                        value = value + s[i] + "\n";
            value.append(s[i]).append("\n");

        }
        return value.toString();
    }

    public void createFilter(List<String> filters, String reportName, String reportId, String bussName) {
        String line = "";
        int count = 0;
        Date dat = new Date();
        long st = dat.getTime();
        try {
            BufferedReader br = new BufferedReader(new FileReader("/usr/local/cache/" + bussName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + ".csv"));
            Map<String, List<String>> filterMap = new HashMap<String, List<String>>();
            String next;
            line = br.readLine();
            for (boolean first = true, last = (line == null); !last; first = false, line = next) {
                last = ((next = br.readLine()) == null);
                count++;
                if (!first) {
                    String[] cells = line.split(",", -1);
                    for (String filter : filters) {
                        try {
                            String val;
                            val = cells[filters.indexOf(filter)];
                            if (filterMap.containsKey(filter)) {
                                if (filterMap.get(filter).indexOf(val) == -1) {
                                    filterMap.get(filter).add(val);
                                }
                            } else {
                                List<String> filterList = new ArrayList<String>();
                                filterList.add(val);
                                filterMap.put(filter, filterList);
                            }
                        } catch (IndexOutOfBoundsException iob) {
                            logger.error("Exception:", iob);
                        }
                    }
                }
            }
            Date dat1 = new Date();
            long Diff = dat1.getTime() - st;

            String csvFile = reportName + "_" + reportId + ".json";
            String filterPath = "/usr/local/cache/" + bussName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/filters";
            writeFilters(filterMap, filters, filters, filterPath, csvFile);
        } catch (FileNotFoundException ex) {
            logger.error("Exception:", ex);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } catch (IndexOutOfBoundsException iob) {
            String[] aa = line.split(",");

        }
    }
//       added by manik
    public String saveLoginChartMeta(String username, String data) {
        FileReadWrite fileReadWrite = new FileReadWrite();
        File file1;
        file1 = new File("/usr/local/cache/loginPageCharts");
        String path = file1.getAbsolutePath();
//        String fName = path+File.separator+reportName.replaceAll("\\W", "").trim()+"_"+reportId+".json";
        File f = new File(path);
        f.mkdirs();
//       File datafile = new File("/usr/local/cache/"+bizzRoleName.replaceAll("\\W", "").trim()+"/"+reportName.replaceAll("\\W", "").trim()+"_"+reportId+"/"+reportName.replaceAll("\\W", "").trim()+"_"+reportId+".json");
        if (!file1.exists()) {
            try {
                file1.createNewFile();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        fileReadWrite.writeToFile("/usr/local/cache/loginPageCharts/tag_master.json", data);
        return "";
    }
}
