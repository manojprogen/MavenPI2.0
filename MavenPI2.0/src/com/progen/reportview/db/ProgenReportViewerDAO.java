/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportview.db;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.progen.cacheLayer.CacheLayer;
import com.progen.contypes.GetConnectionType;
import com.progen.db.ProgenDataSet;
import com.progen.i18n.TranslaterHelper;
import com.progen.report.FileReadWrite;
import com.progen.report.PbReportCollection;
import com.progen.report.ReportManagementDAO;
import static com.progen.report.ReportManagementDAO.logger;
import com.progen.report.ReportObjectMeta;
import com.progen.report.XtendAdapter;
import com.progen.report.query.PbReportQuery;
import com.progen.report.query.PbTimeRanges;
import com.progen.report.query.ProgenAOQuery;
import com.progen.report.query.QueryExecutor;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.scheduler.AOSchedule;
import com.progen.scheduler.KPIAlertSchedule;
import com.progen.scheduler.ReportSchedule;
import com.progen.user.SessionListener;
import com.progen.userlayer.db.LogReadWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author DINANATH
 */
public class ProgenReportViewerDAO extends PbDb {

    public static Logger logger = Logger.getLogger(ProgenReportViewerDAO.class);
    private ResourceBundle resourceBundle;
    public int memberId = 0;

    private ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                resourceBundle = new PbReportViewerResourceBundle();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new PbReportViewerResourceBundleMySql();
            } else {
                resourceBundle = new PbReportViewerResBunSqlServer();
            }
        }
        return resourceBundle;
    }
// this method added by prabal

    public String getReportDefinitionDate(String reportId) throws SQLException {
        String queryForSelectingDates = "SELECT REPORT_ID,REPORT_NAME,CREATED_DATE,created_by,last_update_on  ,last_update_by FROM PRG_AR_REPORT_MASTER where REPORT_ID IN(" + reportId + ")";
        StringBuilder result = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        PbDb pbdb = new PbDb();
        PbReturnObject returnObj = null;
        java.sql.Date lastUpdate = null;
        Gson gson = new Gson();
        CacheLayer cacheLayer = CacheLayer.getCacheInstance();// ByPrabal  
        Map<String, String> dateMap = new HashMap<String, String>();
        try {
            if (cacheLayer.getdata("getReportDefinitionDate" + reportId) == null) {
                returnObj = pbdb.execSelectSQL(queryForSelectingDates);
                cacheLayer.setdata("getReportDefinitionDate" + reportId, returnObj);
            } else {
                returnObj = (PbReturnObject) cacheLayer.getdata("getReportDefinitionDate" + reportId);
            }
            if (returnObj != null && returnObj.rowCount > 0) {
                for (int i = 0; i < returnObj.rowCount; i++) {
                    result.setLength(0);
//                Timestamp st = returnObj.getFieldValueTimestamp(i, 2);
//                new Date(returnObj.getFieldValueDateString(i, 2)).getTime();
//                
                    java.sql.Date d = new java.sql.Date(new Date(returnObj.getFieldValueDateString(i, 2)).getTime());
                    result.append(df.format(d)).append("~");
                    String dateStr = returnObj.getFieldValueString(i, 4);
                    if (dateStr == null || dateStr.equalsIgnoreCase("")) {
                        result.append("");
                    } else {
                        lastUpdate = new java.sql.Date(formatter.parse(dateStr).getTime());
                        result.append(df.format(lastUpdate));
                    }
                    dateMap.put(returnObj.getFieldValueString(i, 0), result.toString());
                }
            }
        } catch (Exception ex) {
        } finally {
            return gson.toJson(dateMap);
        }
    }

    public String getElementId(String reportId) throws SQLException {
        String query = "select element_id,qry_col_id from prg_ar_query_detail where report_id='" + reportId + "'";
        PbDb pbdb = new PbDb();
        PbReturnObject returnObj = null;
        try {
            returnObj = pbdb.execSelectSQL(query);
        } catch (Exception ex) {
            return "";
        }
        String elementId = "";
        if (returnObj != null && returnObj.rowCount > 0) {
            elementId = returnObj.getFieldValueString(0, 0);
        }
        return elementId;
    }

    public PbReturnObject getTimeDefinationReturnObject(Container container, PbReturnObject retObj, String element_id) {
        String query = "";
        PbDb pbdb = new PbDb();
        Connection conn = null;
        PbReportCollection collect = new PbReportCollection();
        PbTimeRanges pbTime = new PbTimeRanges();
        String connType = null;
        String st_date = "";
        conn = ProgenConnection.getInstance().getConnectionForElement(element_id);
        ArrayList<String> timeDetails = new ArrayList<String>();
        if (container != null) {
            collect = container.getReportCollect();
            timeDetails = collect.timeDetailsArray;
            if (timeDetails != null) {
                if (timeDetails.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {
                    try {
                        pbTime.elementID = element_id;
                        pbTime.setRange(timeDetails);
                    } catch (SQLException ex) {
                        java.util.logging.Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    st_date = pbTime.d_clu;

                    query = "select CM_CUST_NAME,PM_CUST_NAME,PYM_CUST_NAME, CQ_CUST_NAME,LQ_CUST_NAME,LYQ_CUST_NAME,CY_CUST_NAME,LY_CUST_NAME,CW_CUST_NAME,PW_CUST_NAME,LYW_CUST_NAME from pr_day_denom where ddate" + st_date + "";
                } else {
                    st_date = timeDetails.get(2).toString();
                    GetConnectionType gettypeofconn = new GetConnectionType();
                    connType = gettypeofconn.getConTypeByElementId(element_id);
                    if (connType != null && connType.equalsIgnoreCase("SqlServer")) {
                        st_date = "   convert(datetime,'" + st_date + "',120) ";

                    } else if (connType != null && connType.equalsIgnoreCase("Oracle")) {
                        st_date = "   to_date('" + st_date + "','mm/dd/yyyy hh24:mi:ss ') ";

                    } else if (connType != null && connType.equalsIgnoreCase("Postgres")) {
                        st_date = "   to_timestamp('" + st_date + "','mm/dd/yyyy hh24:mi:ss ') ";

                    }else {
                        st_date = "str_to_date('" + st_date + " ','%m/%d/%Y')";
                    }
                    query = "select CM_CUST_NAME,PM_CUST_NAME,PYM_CUST_NAME, CQ_CUST_NAME,LQ_CUST_NAME,LYQ_CUST_NAME,CY_CUST_NAME,LY_CUST_NAME,CW_CUST_NAME,PW_CUST_NAME,LYW_CUST_NAME from pr_day_denom where ddate=" + st_date + "";

                }

            }
        }
        if (conn != null) {
            try {
                retObj = pbdb.execSelectSQL(query, conn);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }
        return retObj;
    }

    public String getStartPAgeUrl(String userId) {
        String url = "landingPage.jsp";
        PbDb pbdb = new PbDb();
        PbReturnObject retObj = null;
        String query = "select start_page from prg_ar_users where pu_id=" + userId;
        try {
            retObj = pbdb.execSelectSQL(query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (retObj != null && retObj.getRowCount() > 0) {
            url = retObj.getFieldValueString(0, 0);
        }

        return url;
    }
    //added by Dinanath for multiple savepoint

    public void saveCurrentLocalSavePointPath(String userId, String reportId, String fileName, String currentsavepointpath, String collect_filename, String currentContainerObjectPath, String container_filename, String atrue, String createdDate, String savepointId) {
        String finalqry;
        String qry2 = "update prg_report_savepoint_details set is_enabled='&' where user_id='&' and report_id='&'";
        Object[] obj = new Object[3];
        obj[0] = "false";
        obj[1] = userId;
        obj[2] = reportId;
        finalqry = buildQuery(qry2, obj);
        try {
            execUpdateSQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        String qry1 = getResourceBundle().getString("insertQueryofReportSavepoint");
//        String qry1 = "insert into prg_report_savepoint_details(user_id,report_id,savepoint_name,collect_object_filepath,collect_filename,container_object_filepath,container_filename,is_enabled,created_date)values('&','&','&','&','&','&','&','&','&')";
        obj = new Object[9];
        obj[0] = userId;
        obj[1] = reportId;
        obj[2] = fileName;
        obj[3] = currentsavepointpath;
        obj[4] = collect_filename;
        obj[5] = currentContainerObjectPath;
        obj[6] = container_filename;
        obj[7] = atrue;
        obj[8] = createdDate;
        finalqry = buildQuery(qry1, obj);
        try {
            execUpdateSQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return;
    }
    //added by Dinanath for multiple savepoint

    public void updateCurrentLocalSavePointPath(String userId, String reportId, String savepointname, String currentsavepointpath, String collect_filename, String currentContainerObjectPath, String container_filename, String atrue, String createdDate, String savepointId) {
        String finalqry;
        String qry2 = "update prg_report_savepoint_details set is_enabled='&' where user_id='&' and report_id='&'";
        Object[] obj = new Object[3];
        obj[0] = "false";
        obj[1] = userId;
        obj[2] = reportId;
        finalqry = buildQuery(qry2, obj);
        try {
            execUpdateSQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        String qry1 = "update prg_report_savepoint_details set collect_object_filepath='&' ,collect_filename='&',container_object_filepath='&', container_filename='&' ,is_enabled='&' , created_date='&' , savepoint_name='&' where user_id='&' and report_id='&' and savepoint_id='&'";
        obj = new Object[10];
        obj[0] = currentsavepointpath;
        obj[1] = collect_filename;
        obj[2] = currentContainerObjectPath;
        obj[3] = container_filename;
        obj[4] = atrue;
        obj[5] = createdDate;
        obj[6] = savepointname;
        obj[7] = userId;
        obj[8] = reportId;
        obj[9] = savepointId;
        finalqry = buildQuery(qry1, obj);
        try {
            execUpdateSQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return;
    }
    //added by Dinanath for multiple savepoint

    public String getLocalCollectSavePointPath(String userId, String reportId, String action, HttpSession session) {
        //edited By Mohit Gupta for language translation
        Locale locale = (Locale) session.getAttribute("UserLocaleFormat");
        String finalqry;
        String result[] = null;
        Object obj[] = null;
        File tempFile = null;
        File tempFile1 = null;
        String filePath = null;
        String fileLocation = null;
        PbReportViewerDAO pbDao = new PbReportViewerDAO();
        if (session != null) {
            fileLocation = pbDao.getFilePath(session);
        } else {
            fileLocation = "/usr/local/cache";
        }
        filePath = fileLocation + File.separator + userId + File.separator + reportId;
        tempFile = new File(filePath);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        StringBuilder htmlContent = new StringBuilder();
        String qry1 = null;
        if (action.equalsIgnoreCase("change")) {
            htmlContent.append("<table style='width: 100%;' id='noOfCollectObject'><tr>"
                    + "<th style=\"background-color: #8BC34A;color:#eaf5f7; width: 20%;  padding: 0.3em; border: 1px solid #CCC; \">" + TranslaterHelper.getTranslatedInLocale("SavePoint_Name", locale) + "</th>"
                    + "<th style=\"background-color: #8BC34A;color:#eaf5f7; width: 20%;  padding: 0.3em; border: 1px solid #CCC; \">" + TranslaterHelper.getTranslatedInLocale("Availability_of_File", locale) + "</th>"
                    + "<th style=\"background-color: #8BC34A;color:#eaf5f7; width: 20%;  padding: 0.3em; border: 1px solid #CCC; \">" + TranslaterHelper.getTranslatedInLocale("Default_Enabled", locale) + "</th>"
                    + "<th style=\"background-color: #8BC34A;color:#eaf5f7; width: 20%;  padding: 0.3em; border: 1px solid #CCC; \">" + TranslaterHelper.getTranslatedInLocale("Created_Date", locale) + "</th>"
                    + "<th style=\"background-color: #8BC34A;color:#eaf5f7; width: 15%;  padding: 0.3em; border: 1px solid #CCC; \">" + TranslaterHelper.getTranslatedInLocale("Change_Default_Savepoint", locale) + "</th>"
                    + "</tr>");
            qry1 = "select savepoint_name, collect_filename, container_filename, is_enabled, created_date , savepoint_id from prg_report_savepoint_details where user_id='&' and report_id='&'";
        } else if (action.equalsIgnoreCase("show")) {
            htmlContent.append("<table style='width: 100%;' id='noOfCollectObject'><tr>"
                    + "<th style=\"background-color: #8BC34A;color:#eaf5f7; width: 20%;  padding: 0.3em; border: 1px solid #CCC; \">" + TranslaterHelper.getTranslatedInLocale("SavePoint_Name", locale) + "</th>"
                    + "<th style=\"background-color: #8BC34A;color:#eaf5f7; width: 20%;  padding: 0.3em; border: 1px solid #CCC; \">" + TranslaterHelper.getTranslatedInLocale("OverWrite_New_Status", locale) + "</th>"
                    + "<th style=\"background-color: #8BC34A;color:#eaf5f7; width: 20%;  padding: 0.3em; border: 1px solid #CCC; \">" + TranslaterHelper.getTranslatedInLocale("Availability_of_File", locale) + "</th>"
                    + "<th style=\"background-color: #8BC34A;color:#eaf5f7; width: 20%;  padding: 0.3em; border: 1px solid #CCC; \">" + TranslaterHelper.getTranslatedInLocale("Created_Date", locale) + "</th>"
                    + "<th style=\"background-color: #8BC34A;color:#eaf5f7; width: 15%;  padding: 0.3em; border: 1px solid #CCC; \">" + TranslaterHelper.getTranslatedInLocale("Default_Enabled", locale) + "</th>"
                    + "</tr>");
            qry1 = "select savepoint_name, collect_filename, container_filename, is_enabled ,created_date, savepoint_id from prg_report_savepoint_details where user_id='&' and report_id='&'";
        }
        obj = new Object[2];
        obj[0] = userId;
        obj[1] = reportId;
        finalqry = buildQuery(qry1, obj);
        PbReturnObject returnObject = null;
        String isReportSavepoint="";
        try {
            returnObject = execSelectSQL(finalqry);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    tempFile = new File(filePath + File.separator + returnObject.getFieldValueString(i, 1));
                    tempFile1 = new File(filePath + File.separator + returnObject.getFieldValueString(i, 2));
                    String flagMessage = "Not Available";
                    if (tempFile.isFile() && tempFile1.isFile()) {
                        flagMessage = "Available";
                    }
                    isReportSavepoint+=returnObject.getFieldValueString(i, 3)+",";
                   
                    if (action.equalsIgnoreCase("change")) {
                        htmlContent.append("<tr ><td style=\"padding: 0.3em;border: 1px solid #CCC;\">" + returnObject.getFieldValueString(i, 0) + "</td>"
                                + "<td style=\"padding: 0.3em;border: 1px solid #CCC;\">" + flagMessage + "</td>"
                                + "<td style=\"padding: 0.3em;border: 1px solid #CCC;\">" + returnObject.getFieldValueString(i, 3) + "</td>"
                                + "<td style=\"padding: 0.3em;border: 1px solid #CCC;\">" + returnObject.getFieldValueString(i, 4) + "</td>"
                                + "<td style=\"padding: 0.3em;border: 1px solid #CCC;\"><input type='radio' name='setDefaultCollect' value='" + returnObject.getFieldValueString(i, 0) + "::" + returnObject.getFieldValueString(i, 1) + "::" + returnObject.getFieldValueString(i, 2) + "::" + returnObject.getFieldValueString(i, 5) + "'> Set Default</td>"
                                + "</tr>");
                    } else if (action.equalsIgnoreCase("show")) {
                        htmlContent.append("<tr ><td style=\"padding: 0.3em;border: 1px solid #CCC;\">" + returnObject.getFieldValueString(i, 0) + "</td>"
                                + "<td style=\"padding: 0.3em;border: 1px solid #CCC;\"><input type='radio' id='" + returnObject.getFieldValueString(i, 1) + "::" + returnObject.getFieldValueString(i, 2) + "::" + returnObject.getFieldValueString(i, 5) + "::" + returnObject.getFieldValueString(i, 0) + "' name='savepointfilename' value='Overrite_" + (i + 1) + "'> Overrite</td>"
                                + "<td style=\"padding: 0.3em;border: 1px solid #CCC;\">" + flagMessage + "</td>"
                                + "<td style=\"padding: 0.3em;border: 1px solid #CCC;\">" + returnObject.getFieldValueString(i, 4) + "</td>"
                                + "<td style=\"padding: 0.3em;border: 1px solid #CCC;\">" + returnObject.getFieldValueString(i, 3) + "</td>"
                                + "</tr>");
                    }
                }
            }
            if (action.equalsIgnoreCase("change")) {
                htmlContent.append("<tr ><td style=\"padding: 0.3em;border: 1px solid #CCC;\">Global Report</td>"
                        + "<td style=\"padding: 0.3em;border: 1px solid #CCC;\">Not Available</td>");
                        if(isReportSavepoint.contains("true")){
                htmlContent.append("<td style=\"padding: 0.3em;border: 1px solid #CCC;\">false</td>");
                                }else{
                htmlContent.append("<td style=\"padding: 0.3em;border: 1px solid #CCC;\">true</td>");
            }
                htmlContent.append("<td style=\"padding: 0.3em;border: 1px solid #CCC;\">None</td>");
                htmlContent.append("<td style=\"padding: 0.3em;border: 1px solid #CCC;\"><input type='radio' name='setDefaultCollect' value='Global Report'> Set Default Global</td></tr>");
            }
            if (action.equalsIgnoreCase("show")) {
                htmlContent.append("<tr>"
                        + "<td style=\"padding: 0.3em;\">Create SavePoint" + (returnObject.getRowCount() + 1) + "</td>"
                        + "<td style=\"padding: 0.3em;\"><input type='radio' id='createNewSavePoint' name='savepointfilename' value='New_" + (returnObject.getRowCount() + 1) + "'>" + TranslaterHelper.getTranslatedInLocale("New", locale) + " </td>"
                        + "<td colspan=2 style=\"padding: 0.3em;\"><span id='noOfCollectObjectMessage' style='color:red;'></span></td>"
                        + "<td style=\"padding: 0.3em;\"></td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td style=\"padding: 0.3em;\"><span id='ysn'>" + TranslaterHelper.getTranslatedInLocale("SavePoint_Name", locale) + ":</span></td>"
                        + "<td style=\"padding: 0.3em;\"><input type='text' id='yourSavepointName' name='yourSavepointName' value='' placeholder='" + TranslaterHelper.getTranslatedInLocale("Optional", locale) + "'> </td>"
                        + "<td colspan=2 style=\"padding: 0.3em;\"></td>"
                        + "<td style=\"padding: 0.3em;\"></td>"
                        + "</tr>");
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        htmlContent.append("<tr ><td colspan='5' ><br/></td></tr>");
        if (action.equalsIgnoreCase("change")) {
            htmlContent.append("<tr ><td colspan='5' align='center'><input type='button' class='navtitle-hover' value='" + TranslaterHelper.getTranslatedInLocale("Change", locale) + "' onclick='setDefaultCollectSavepoint()'></td></tr>");
        } else if (action.equalsIgnoreCase("show")) {
            htmlContent.append("<tr ><td colspan='5' align='center'><input type='button' class='navtitle-hover' value='" + TranslaterHelper.getTranslatedInLocale("save", locale) + "' onclick='saveCollectObjectInLocalFile2()'></td></tr>");
        }
        htmlContent.append("</table>");
        return htmlContent.toString();
    }
    //added by Dinanath for multiple savepoint

    public String setDefaultCollectSavepoint(String userId, String reportId, String savepointName, String savepointId) {
        String finalqry;
        try {
            String qry2 = "update prg_report_savepoint_details set is_enabled='&' where user_id='&' and report_id='&'";
            Object[] obj = new Object[3];
            obj[0] = "false";
            obj[1] = userId;
            obj[2] = reportId;
            finalqry = buildQuery(qry2, obj);
            int result = execUpdateSQL(finalqry);
            if (!savepointName.equalsIgnoreCase("Global Report")) {
                qry2 = "update prg_report_savepoint_details set is_enabled='&' where user_id='&' and report_id='&' and savepoint_id='&'";
                obj = new Object[4];
                obj[0] = "true";
                obj[1] = userId;
                obj[2] = reportId;
                obj[3] = savepointId;
                finalqry = buildQuery(qry2, obj);

                result = execUpdateSQL(finalqry);
//                if(result==1)
//                return "Default Report savepoint has changed successfully";
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return "Default Report savepoint has changed successfully";
    }
//Added By Ram 18Desc2015

    public boolean insertMeasuresAsViewBys(String reportid, String[] rowNameArray, String[] rowIdArray, int dispSeqNo) throws Exception {
        ArrayList al = new ArrayList();
        boolean result = false;

        String insertQuery = getResourceBundle().getString("insertMeasuresAsViewBys");

        for (int m = 0; m < rowIdArray.length; m++) {

            Object insertObj[] = new Object[7];
            insertObj[0] = reportid;
            insertObj[1] = rowNameArray[m];
            insertObj[2] = rowIdArray[m];
            insertObj[3] = dispSeqNo;
            insertObj[4] = "All";
            insertObj[5] = "false";
            insertObj[6] = "Y";
            dispSeqNo++;
            String fininserQ = buildQuery(insertQuery, insertObj);
            al.add(fininserQ);

        }
        try {
            result = executeMultiple(al);
        } catch (Exception n) {
            logger.error("Exception:", n);
        }
        return result;
    }
    //added by Dinanath for getting system cpu info
    public boolean flag = false;

    public String callingThisAtEveryXminute() {

        try {
            if (!flag) {
                String filepath = File.separator + "usr" + File.separator + "local" + File.separator + "cache";
                File executionPath = new File(filepath);

                if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                    if (System.getProperty("os.arch").toLowerCase().compareTo("i386") == 0) {
                        filepath = File.separator + "usr" + File.separator + "local" + File.separator + "apps";
                        executionPath = new File(filepath);
                        System.load(executionPath.getAbsolutePath() + "/libsigar-x86-linux.so");
                    } else if (System.getProperty("os.arch").toLowerCase().compareTo("amd64") == 0) {
                        filepath = File.separator + "usr" + File.separator + "local" + File.separator + "apps";
                        executionPath = new File(filepath);
                        System.load(executionPath.getAbsolutePath() + "/libsigar-amd64-linux.so");
                    }
                }
                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    if (System.getProperty("os.arch").toLowerCase().compareTo("x86") == 0) {
                        filepath = File.separator + "usr" + File.separator + "local" + File.separator + "cache";
                        executionPath = new File(filepath);
                        System.load(executionPath.getAbsolutePath() + "/sigar-x86-winnt.dll");
                    } else if (System.getProperty("os.arch").toLowerCase().compareTo("x64") == 0) {
                        filepath = File.separator + "usr" + File.separator + "local" + File.separator + "cache";
                        executionPath = new File(filepath);
                        System.load(executionPath.getAbsolutePath() + "/sigar-amd64-winnt.dll");
                    }
                }

                flag = true;

            }
        } catch (java.lang.UnsatisfiedLinkError usfdle) {
//            logger.error("Exception:",usfdle);
        } catch (Exception ex) {
//            logger.error("Exception:",ex);
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                Date date = new Date();

                int cpuLoadFinal = 0;
                try {
                    if (!flag) {
                        String filepath = File.separator + "usr" + File.separator + "local" + File.separator + "cache";
                        File executionPath = new File(filepath);

                        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                            if (System.getProperty("os.arch").toLowerCase().compareTo("i386") == 0) {
                                filepath = File.separator + "usr" + File.separator + "local" + File.separator + "apps";
                                executionPath = new File(filepath);
                                System.load(executionPath.getAbsolutePath() + "/libsigar-x86-linux.so");
                            } else if (System.getProperty("os.arch").toLowerCase().compareTo("amd64") == 0) {
                                filepath = File.separator + "usr" + File.separator + "local" + File.separator + "apps";
                                executionPath = new File(filepath);
                                System.load(executionPath.getAbsolutePath() + "/libsigar-amd64-linux.so");
                            }
                        }
                        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                            if (System.getProperty("os.arch").toLowerCase().compareTo("x86") == 0) {
                                filepath = File.separator + "usr" + File.separator + "local" + File.separator + "cache";
                                executionPath = new File(filepath);
                                System.load(executionPath.getAbsolutePath() + "/sigar-x86-winnt.dll");
                            } else if (System.getProperty("os.arch").toLowerCase().compareTo("x64") == 0) {
                                filepath = File.separator + "usr" + File.separator + "local" + File.separator + "cache";
                                executionPath = new File(filepath);
                                System.load(executionPath.getAbsolutePath() + "/sigar-amd64-winnt.dll");
                            }
                        }

                        flag = true;

                    }
                } catch (java.lang.UnsatisfiedLinkError usfdle) {
//            logger.error("Exception:",usfdle);
                } catch (Exception ex) {
//            logger.error("Exception:",ex);
                }
                try {
                    Calendar currentDate = Calendar.getInstance();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
                    String dateNow = formatter.format(currentDate.getTime());

                    Sigar sigar = new Sigar();
                    Mem mem = null;
                    CpuPerc cpuPerc = null;
                    mem = sigar.getMem();
                    cpuPerc = sigar.getCpuPerc();

                    Object insertObj[] = new Object[12];

                    insertObj[0] = mem.getActualFree() / 1024 / 1024 + " MB";

                    insertObj[1] = mem.getActualUsed() / 1024 / 1024 + " MB";

                    insertObj[2] = mem.getFree() / 1024 / 1024 + " MB";

                    insertObj[3] = mem.getRam() + " MB";

                    insertObj[4] = mem.getTotal() / 1024 / 1024 + " MB";

                    insertObj[5] = mem.getUsed() / 1024 / 1024 + " MB";
                    //puPerc cpuPerc = cpu.getCpuPerc();

                    insertObj[6] = cpuPerc.getCombined() * 100;

                    insertObj[7] = cpuPerc.getSys() * 100;

                    insertObj[8] = cpuPerc.getUser() * 100;

                    insertObj[9] = cpuPerc.getIdle() * 100;
                    cpuLoadFinal = (int) (cpuPerc.getCombined() * 100);

                    insertObj[10] = cpuLoadFinal;

                    insertObj[11] = dateNow;
                    String insertQuery = getResourceBundle().getString("insertQueryofSystemMemoryInfo");
//                    String insertQuery = "insert into prg_system_memory_info(ID,Actual_total_free_sys_memory,Actual_total_used_sys_memory,Total_free_sys_memory,sys_memory,total_sys_memory,total_used_sys_memory,"
//                    + "cpu_perce_usage_combined,cpu_perce_usage_sys,cpu_perce_usage_user,free_cpu_percetange,cpu_load_final,current_system_time"
//                    + ")values(memory_id.nextval,'&','&','&','&','&','&','&','&','&','&','&','&')";

                    insertQuery = buildQuery(insertQuery, insertObj);

                    int result = execUpdateSQL(insertQuery);

                } catch (java.lang.UnsatisfiedLinkError usfdle) {
//            logger.error("Exception:",usfdle);
                } catch (SigarException se) {
//            logger.error("Exception:",ex);
                } catch (Exception ex) {
//            logger.error("Exception:",ex);
                }
            }
        }, 0, 10 * 60 * 1000);

        return "success";
    }

    public HashMap<String, String> createCustomMeasureForGraphs(String elementId, String reportId) {
        String query = "select ELEMENT_ID,BUSS_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID='" + elementId + "'";
        PbDb pbdb = new PbDb();
//        Connection conn = null;
//        conn = ProgenConnection.getInstance().getConnectionForElement(elementId);
        PbReturnObject retObj;
        try {
            retObj = pbdb.execSelectSQL(query);
            String bussColName = retObj.getFieldValueString(0, 1);
            String aggType = retObj.getFieldValueString(0, 2);
            HashMap<String, String> measureInfo = new HashMap<String, String>();
            measureInfo.put("measureName", bussColName);
            measureInfo.put("aggType", aggType);
            measureInfo.put("elementId", elementId);
            return measureInfo;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
            return null;
        }
    }

    public void createDefaultRepSavepoint(HttpServletRequest request, String userId, String reportId) throws Exception {
        HttpSession session = request.getSession(false);
        String checkLocalOrGlobal="";
        try {
            String qry2 = "select setup_char_value from prg_gbl_setup_values where setup_key='IS_OPENING_REPORT'";
            PbReturnObject result = execSelectSQL(qry2);
            if (result != null && result.rowCount > 0) {

                checkLocalOrGlobal = result.getFieldValueString(0, 0);
            }
            if(checkLocalOrGlobal!=null && checkLocalOrGlobal.equalsIgnoreCase("LOCAL")){
            qry2 = "select * from prg_report_savepoint_details where user_id='&' and report_id='&'";
            Object[] obj = new Object[2];
            obj[0] = userId;
            obj[1] = reportId;
            String finalqry = buildQuery(qry2, obj);
            result = execSelectSQL(finalqry);
            String savepointId = "";
            if (result != null && result.rowCount > 0) {

                savepointId = result.getFieldValueString(0, 0);
            }

            if (savepointId != null && savepointId.isEmpty()) {
                String fileName = "createNewSavePoint";
                String status = "New_1";
                String usrSavepointName = null;
                HashMap map = new HashMap();
                Container container = null;
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportId);
                PbReportCollection collection2 = null;
                //for serializing object into file
                ProgenReportViewerDAO dao = null;
                PbReportCollection collect = container.getReportCollect();
                ObjectOutputStream out = null;
                String filePath = null;
                FileOutputStream fileOut = null;
                String newFlag = "false";
//                String overriteFlag = "false";
                String currenCollectSavepointPath = null;
                String collectfilename = null;
                String status1[] = null;
                String container_filename = null;
                String savepoint_id = null;
//                String owriteSavepointName = null;
                boolean exceptionFlag = false;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
                SimpleDateFormat formatter2db = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = new Date();
                String createdDate = formatter.format(date);
                createdDate = createdDate.replace(" ", "_");
                String fileLocation = null;
                PbReportViewerDAO pbDao = new PbReportViewerDAO();
                if (session != null) {
                    fileLocation = pbDao.getFilePath(session);
                } else {
                    fileLocation = "/usr/local/cache";
                }
                try {
                    File tempFile = null;
                    //filePath = "c://usr/local/cache";
                    filePath = fileLocation + File.separator + userId + File.separator + reportId;
                    tempFile = new File(filePath);
                    if (tempFile.exists()) {
                    } else {
                        tempFile.mkdirs();
                    }
                    status1 = status.split("_");
                    if (fileName.equalsIgnoreCase("createNewSavePoint") && status1[0].equalsIgnoreCase("New")) {
                        collectfilename = userId + "_" + reportId + "_" + createdDate + "_collect.ser";
                        currenCollectSavepointPath = filePath + File.separator + collectfilename;
                        fileOut = new FileOutputStream(currenCollectSavepointPath);
                        out = new ObjectOutputStream(fileOut);
                        out.writeObject(collect);
                        //session.setAttribute("COLLECT_OBJPATH_" + userId + "_" + reportId, currenCollectSavepointPath);
                        newFlag = "true";
                    }

                } catch (IOException i) {
                    logger.error("Exception:", i);
                    exceptionFlag = true;
                } finally {
                    if (out != null && fileOut != null) {
                        try {
                            out.reset();
                            out.close();
                            fileOut.flush();
                            fileOut.close();
                        } catch (IOException ex) {
                            logger.error("Exception:", ex);
                        }
                    }
                }
                String currentContainerObjectPath = null;
                try {
                    FileOutputStream fileOut2 = null;
                    ObjectOutputStream out2 = null;
                    filePath = fileLocation + File.separator + userId + File.separator + reportId;
                    status1 = status.split("_");
                    if (fileName.equalsIgnoreCase("createNewSavePoint") && status1[0].equalsIgnoreCase("New")) {
                        container_filename = userId + "_" + reportId + "_" + createdDate + "_container.ser";
                        currentContainerObjectPath = filePath + File.separator + container_filename;
                        fileOut2 = new FileOutputStream(currentContainerObjectPath);
                        out2 = new ObjectOutputStream(fileOut2);
                        out2.writeObject(container);
                    }
                    out2.reset();
                    out2.close();
                    fileOut2.flush();
                    fileOut2.close();
                    //session.setAttribute("CONTAINER_OBJPATH_" + userId + "_" + reportId, currentContainerObjectPath);
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                    exceptionFlag = true;
                }
                createdDate = formatter2db.format(date);
                String savepntname = null;

                if (newFlag.equalsIgnoreCase("true")) {
                    dao = new ProgenReportViewerDAO();
                    if (usrSavepointName != null && !usrSavepointName.isEmpty()) {
                        savepntname = usrSavepointName;
                    } else {
                        savepntname = "SavePoint" + status1[1];
                    }
                    dao.saveCurrentLocalSavePointPath(userId, reportId, savepntname, currenCollectSavepointPath, collectfilename, currentContainerObjectPath, container_filename, "true", createdDate, savepoint_id);
                }

                if (exceptionFlag) {
//        response.getWriter().print("Your Report Savepoint has not saved. Please try Again!!!");
                } else {
//        response.getWriter().print("Your Report Savepoint has saved successfully");
                }
            }//if retunrObject==null
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    //added by mohit for ao
    public String getAOBasedOnReport(String reportBizRoles, String fileLocation) throws Exception {
        String listOfAOQuery = "select AO_ID,AO_NAME from prg_ar_ao_master where folder_id='" + reportBizRoles + "'";
        String AOId = "";
        String AOName = "";
//        String result = "";
        StringBuilder result = new StringBuilder(400);
//        StringBuffer outerBuffer = new StringBuffer("");
        StringBuilder outerBuffer = new StringBuilder(300);
        File datafile = null;

        PbReturnObject returnObject = new PbReturnObject();
        returnObject = new PbDb().execSelectSQL(listOfAOQuery);
        if (returnObject != null && returnObject.getRowCount() > 0 && returnObject.getFieldValueString(0, 0) != null) {
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                AOId = returnObject.getFieldValueString(i, 0);
                AOName = returnObject.getFieldValueString(i, 1);

                datafile = new File(fileLocation + "/analyticalobject/M_AO_" + AOId + ".json");
                if (datafile.exists()) {
                    outerBuffer.append("<li class='closed' id='" + AOId + "'>");
                    outerBuffer.append("<input type='radio' name='AOList' id='" + AOId + "'><span><font size='1px' face='verdana'><b>" + AOName + "</b></font></span>");
                    outerBuffer.append("</li>");
                }
                if (i == returnObject.getRowCount() - 1) {
                    result.append(outerBuffer.toString());
//                                            result+=outerBuffer.toString();
                    result.append("</ul></td></tr><tr><td><input type=\"button\" value=\"Done\" onclick=\"createAOForGraph()\" class=\"navtitle-hover\" name=\"Done\"></td></tr></table>");
//                                            result += "</ul></td></tr><tr><td><input type=\"button\" value=\"Done\" onclick=\"createAOForGraph()\" class=\"navtitle-hover\" name=\"Done\"></td></tr></table>";
                }
            }
        } else {
            result = new StringBuilder("Failed");
        }
        return result.toString();
    }
    //added by mohit for ao

    public String createAOForGraph(String aoId, String REPORTID, HttpServletRequest request) throws Exception {
String filesPath = "";
 HttpSession session = request.getSession(false);
    PbReportViewerDAO dao = new PbReportViewerDAO();
        Gson gson = new Gson();

        String[] AggType = null;
         Container container = Container.getContainerFromSession(request, REPORTID);
        if (session != null) {
            filesPath = dao.getFilePath(session);
        } else {
            filesPath = "/usr/local/cache";
        }
        //
        container.setFilesPath(filesPath);
        
        
//          String reportId = request.getParameter("reportId");
// String listOfAOQuery="select * from prg_ar_ao_details where ao_id='"+aoId+"'";
        String result = "";

     
        HashMap<String, List> aoMasterMap = new HashMap<>();
        List<String> viewBysList = new ArrayList<>();
        List<String> viewBysNameList = new ArrayList<>();
        List<String> measBys1 = new ArrayList<>();
        List<String> rowMeasNamesArr1 = new ArrayList<>();
        List<String> AggType1 = new ArrayList<>();
        ReportObjectMeta roMeta = new ReportObjectMeta();
        File datafile = new File(filesPath+"/analyticalobject/M_AO_" + aoId + ".json");
        if (datafile.exists()) {
            FileReadWrite readWrite = new FileReadWrite();
            String metaString = readWrite.loadJSON(filesPath+"/analyticalobject/M_AO_" + aoId + ".json");
            Type tarType = new TypeToken<ReportObjectMeta>() {
            }.getType();
            roMeta = gson.fromJson(metaString, tarType);
            viewBysList = roMeta.getViewIds();
            viewBysNameList = roMeta.getViewNames();
            measBys1 = roMeta.getMeasIds();
            rowMeasNamesArr1 = roMeta.getMeasNames();
            AggType1 = roMeta.getAggregations();
        } else {

//                             PbReturnObject returnObject = new PbReturnObject();
//                             returnObject= new PbDb().execSelectSQL(listOfAOQuery);
//                              if(returnObject!=null && returnObject.getRowCount() > 0 && returnObject.getFieldValueString(0, 0)!=null)
//                              {
//                                  int d=0,f=0;
//                                  for (int i=0; i<returnObject.getRowCount(); i++)  {
//                                    if(returnObject.getFieldValueString(i, "IS_DIM").equalsIgnoreCase("Y")){
//                                        viewBysList.add(returnObject.getFieldValueString(i, "ELEMENT_ID"));
//                                        viewBysNameList.add(returnObject.getFieldValueString(i, "USER_COL_DESC"));
////                                        viewBys[d]=returnObject.getFieldValueString(i, "ELEMENT_ID");
////                                        rowViewNamesArr[d]=returnObject.getFieldValueString(i, "ELEMENT_ID");
////                                        d++;
//                                    }
//                                 else if(returnObject.getFieldValueString(i, "IS_FACT").equalsIgnoreCase("Y"))
//                                    {
//                                      measBys1.add(returnObject.getFieldValueString(i, "ELEMENT_ID"));
//                                        rowMeasNamesArr1.add(returnObject.getFieldValueString(i, "USER_COL_DESC"));
////                                     measBys[f]=returnObject.getFieldValueString(i, "ELEMENT_ID");
////                                        rowMeasNamesArr[f]=returnObject.getFieldValueString(i, "ELEMENT_ID");
//                                        AggType1.add(returnObject.getFieldValueString(i, "AGGR_TYPE"));
////                                        AggType[f]=returnObject.getFieldValueString(i, "AGGR_TYPE");
////                                        f++;
//                                    }
//
//                                  }}
            result = "Failed";
            return result;
        }

        viewBysList.add("TIME");
        viewBysNameList.add("Time");
        aoMasterMap.put("viewby", viewBysNameList);
        aoMasterMap.put("viewbyIds", viewBysList);
        aoMasterMap.put("measure", rowMeasNamesArr1);
        aoMasterMap.put("measureIds", measBys1);
        aoMasterMap.put("aggregation", AggType1);
//          ReportObjectMeta roMeta = new ReportObjectMeta();
        roMeta.setViewIds(viewBysList);
        roMeta.setViewNames(viewBysNameList);
        roMeta.setMeasIds(measBys1);
        roMeta.setMeasNames(rowMeasNamesArr1);
        roMeta.setAggregations(AggType1);
        FileReadWrite fileReadWrite = new FileReadWrite();
        File file;
        file = new File(filesPath+"/analyticalobject");
        String path = file.getAbsolutePath();
        //        String fName = path + File.separator +"R_GO_"+ REPORTID + ".json";
        File f1 = new File(path);
        //        File file1 = new File(fName);
        f1.mkdirs();
        datafile = new File(filesPath+"/analyticalobject/R_GO_" + REPORTID + ".json");
        if (!datafile.exists()) {
            try {
                datafile.createNewFile();
            } catch (IOException ex) {
                //                logger.error("Exception:",ex);
            }
        }
        fileReadWrite.writeToFile(filesPath+"/analyticalobject/R_GO_" + REPORTID + ".json", gson.toJson(roMeta));


       
//        PbReportCollection collect = container.getReportCollect();
//        QueryExecutor qryExec = new QueryExecutor();
//        ProgenDataSet filterObj = null;
//
//        StringBuilder filterQuery = new StringBuilder(500);
//        filterQuery.append("select ");
//
//        for (int l = 0; l < viewBysList.size(); l++) {
//            if (viewBysList.get(l).equalsIgnoreCase("TIME")) {
//                if (l == 0) {
//                    filterQuery.append(viewBysList.get(l));
//                    //                    filterQuery +=viewBysList.get(l);
//                } else {
//                    //                    filterQuery += " , "+viewBysList.get(l);
//                    filterQuery.append(" , ").append(viewBysList.get(l));
//                }
//            } else if (l == 0) {
//                //                        filterQuery += "A_"+viewBysList.get(l);
//                filterQuery.append("A_").append(viewBysList.get(l));
//            } else {
//                //                         filterQuery += " , A_"+viewBysList.get(l);
//                filterQuery.append(" , A_").append(viewBysList.get(l));
//            }
        //        }
//
//        filterQuery.append("  from M_AO_").append(aoId);
//        //        }
//
//        Map<String, List<String>> filterMap = new HashMap<>();
//
//        filterObj = qryExec.executeQuery(collect, filterQuery.toString(), false);
//
//        for (int m = 0; m < filterObj.rowCount; m++) {
//            for (int k = 0; k < viewBysList.size(); k++) {
//                String val = filterObj.getFieldValueString(m, k).replace(",", "");
//
//                if (filterMap.containsKey(viewBysList.get(k))) {
//                    if (filterMap.get(viewBysList.get(k)).indexOf(val) == -1) {
//                        filterMap.get(viewBysList.get(k)).add(val);
//                    }
//                } else {
//                    List<String> filterList = new ArrayList<String>();
//                    filterList.add(val);
//                    filterMap.put((String) viewBysList.get(k), filterList);
//                }
//            }
        //        }
//        XtendAdapter adapter = new XtendAdapter();
//        String filterPath = filesPath + "/analyticalobject/filters/R_GO_" + REPORTID;
//
//        adapter.writeFilters(filterMap, viewBysNameList, viewBysList, filterPath, ".json");

        result = gson.toJson(aoMasterMap);
        return result;
    }

    public String updateMasterForGraph(String aoId, String reportId) {
        String finalqry;
        try {
            String qry2 = "update prg_ar_report_master set AO_ID_FOR_GRAPH='&' where report_id=&";
            Object[] obj = new Object[2];
            obj[0] = aoId;
            obj[1] = reportId;
            finalqry = buildQuery(qry2, obj);
            execUpdateSQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
            return "Failed";
        }
        return "Succeed";
    }

    public String createGOJson(String aoId, String reportId) {
        String finalqry;
        try {
            String qry2 = "update prg_ar_report_master set AO_ID_FOR_GRAPH='&' where report_id=&";
            Object[] obj = new Object[2];
            obj[0] = aoId;
            obj[1] = reportId;
            finalqry = buildQuery(qry2, obj);
            int result = execUpdateSQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
            return "Failed";
        }
        return "Succeed";
    }

    public String invalidateCache(String userId, String deleterepids, String fileLocation) {
//        String finalqry;
        try {
            String[] delRepIds = deleterepids.split(",");
           
            for (int i = 0; i < delRepIds.length; i++) {
                File tempFile = new File(fileLocation + "/AO_Data/" + userId + "/" + delRepIds[i]);
                File[] allFilesAndDirs = tempFile.listFiles();
                if (allFilesAndDirs != null) {
                    for (File f1 : allFilesAndDirs) {
                        tempFile = new File(fileLocation + "/AO_Data/" + userId + "/" + delRepIds[i] + "/" + f1.getName());
                        File[] allFiles = tempFile.listFiles();
                        if (allFiles != null) {
                            for ( File f2 : allFiles) {
                                if (!f1.getName().equalsIgnoreCase(f2.getName().replaceAll(".json", "").replaceAll(".JSON", ""))) {
                                    new File(fileLocation + "/AO_Data/" + userId + "/" + delRepIds[i] + "/" + f1.getName() + "/" + f2.getName()).delete();
                                }
                            }
                        }else
                        {
                              if (!delRepIds[i].equalsIgnoreCase(f1.getName().replaceAll(".json", "").replaceAll(".JSON", ""))) {
                               new File(fileLocation + "/AO_Data/" + userId + "/" + delRepIds[i] + "/" + f1.getName()).delete();
                        }
                              }

                    }
                }

            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
            return "Failed";
        }
        return "Succeed";
    }
    //added by Mohit 

    public String rebuildCache(String userId, String deleterepids, String fileLocation, boolean islog, String filename) {

        LogReadWriter logrw = new LogReadWriter();
        Gson gson = new Gson();
        PbReturnObject pbretObj = null;
        String query = "";
        String elementid = "";
        String currentquery = "";
        String reportid = "";
        String reportname = "";
        String st = "";
        String et = "";
        Date d1 = null;
        HashMap<String, List<Object>> allchartqueries = new HashMap<>();
        String currentPath=fileLocation + "/AO_Data/" + userId + "/" ;
        String[] delRepIds = deleterepids.split(",");
        for (int i = 0; i < delRepIds.length; i++) {

            String jsonPath="";
            String queryPath="";
            reportid = delRepIds[i];
            try {
                d1 = new Date();
                st = d1.toString();
//                 if(islog)
//                 logrw.fileWriterWithFileName("################################################# Scheduler Finished ################################", filename);

                Map<String, String> allqueries = new LinkedHashMap<>();
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    query = getResourceBundle().getString("selectElementIdBasedOnReport");
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                    query = getResourceBundle().getString("selectElementIdBasedOnReport");
                } else {
                    query = getResourceBundle().getString("selectElementIdBasedOnReport");
                }
                Object[] obj = new Object[2];
                obj[0] = reportid;
                obj[1] = reportid;
                query = buildQuery(query, obj);
                PbReturnObject returnobject = execSelectSQL(query);
                if (returnobject != null) {
                    elementid = returnobject.getFieldValueString(0, 0);
                    reportname = returnobject.getFieldValueString(0, 1);
                }
                File tempFile = new File(currentPath + reportid);
                File[] allFilesAndDirs = tempFile.listFiles();
                if (allFilesAndDirs != null) {
                    for (File f1 : allFilesAndDirs) {
                       File datafile=null;
                        if(reportid.equalsIgnoreCase(f1.getName().replaceAll(".json", "").replaceAll(".JSON", "")))
                        {
                            jsonPath=currentPath + reportid +"/" +reportid + ".json";
                            queryPath=currentPath + reportid +"/";
                             datafile = new File(jsonPath);
                        }
                        else
                        {    
                             jsonPath=currentPath + reportid + "/" + f1.getName() + "/" +f1.getName() + ".json";
                            queryPath=currentPath + reportid + "/" + f1.getName() + "/";
                             datafile = new File(jsonPath);
                        }

                        
                        if (datafile.exists()) {
                            
                            FileReadWrite readWrite = new FileReadWrite();
                            String metaString = readWrite.loadJSON(jsonPath);
                            Type tarType = new TypeToken<Map<String, String>>() {
                            }.getType();
                            allqueries = gson.fromJson(metaString, tarType);
                            if (allqueries != null && !allqueries.isEmpty()) {
                                for (int j = 1; j <= allqueries.size(); j++) {
                                    pbretObj = null;
                                    currentquery = allqueries.get("QUERY" + j).toString();
                                    if (!allchartqueries.isEmpty()) {
                                        for (int allq = 1; allq <= allchartqueries.size(); allq++) {
                                            if ((allchartqueries.get("QUERY" + allq).get(0).toString().equalsIgnoreCase(currentquery))) {
                                                pbretObj = (PbReturnObject) allchartqueries.get("QUERY" + allq).get(1);
                                                break;
                                            }

                                        }
                                    }
                                    if (pbretObj == null) {
                                        List<Object> rtobject = new ArrayList<>();
                                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                                            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
                                            ArrayList<String> sessionQueryLst = new ArrayList<String>();
                                            try {
                                                sessionQueryLst = reportTemplateDAO.getSessionQueriesForOracle();
                                            } catch (Exception e) {
                                                logger.error("Exception:", e);
                                            }
                                            pbretObj = execSelectORCL(currentquery, ProgenConnection.getInstance().getConnectionForElement(elementid), sessionQueryLst);
                                        } else {
                                            pbretObj = execSelectSQL(currentquery, ProgenConnection.getInstance().getConnectionForElement(elementid));

                                        }
                                        rtobject.add(currentquery);
                                        rtobject.add(pbretObj);
                                        allchartqueries.put("QUERY" + (allchartqueries.size() + 1), rtobject);

                                    }
                                    tempFile = new File(queryPath + "QUERY" + (j) + ".txt");
                                    tempFile.createNewFile();
                                    FileOutputStream fout = new FileOutputStream(tempFile);
                                    ObjectOutputStream out = new ObjectOutputStream(fout);
                                    out.writeObject(pbretObj);
                                    out.flush();

                                }
                            }

                        }
                    }
                }
                d1 = new Date();
                et = d1.toString();
                if (islog) {
                    logrw.fileWriterWithFileName(reportid + "       " + reportname + "      " + st + "     " + et + "           Succeed", filename);
                }
                logrw.fileWriterWithFileName("---------------------------------------------------------------------------------------------------------------------------------------", filename);

            } catch (Exception ex) {
//            logger.error("Exception:",ex);

                logger.error("Exception:", ex);
                d1 = new Date();
                et = d1.toString();
                if (islog) {
                    try {
                        logrw.fileWriterWithFileName(reportid + "       " + reportname + "       " + st + "     " + et + "           Failed", filename);
                        logrw.fileWriterWithFileName("---------------------------------------------------------------------------------------------------------------------------------------", filename);

                    } catch (IOException ex1) {
                        logger.error("Exception:", ex1);
                    }
                }

            }

        }

        return "Succeed";

//               Object[] obj = new Object[1];
//            obj[0] = deleterepids;
//            query = buildQuery(query, obj);
//            PbReturnObject returnobject = execSelectSQL(query);
//            if(returnobject!=null)
//            {
//                
//                for(int i=0;i<returnobject.getRowCount();i++)
//                {
//                    REPORT_ID=returnobject.getFieldValueString(i, "REPORT_ID");
//             REPORT_NAME=returnobject.getFieldValueString(i, "REPORT_NAME");
//             FOLDER_ID=returnobject.getFieldValueString(i, "FOLDER_ID");
//             FOLDER_NAME=returnobject.getFieldValueString(i, "FOLDER_NAME");     
//                    File datafile = new File(fileLocation+"/"+FOLDER_NAME.replaceAll("\\W", "").trim()
//                            +"/"+REPORT_NAME.replaceAll("\\W", "").trim()+"_"+REPORT_ID+"/"+REPORT_NAME.replaceAll("\\W", "").trim()+"_"+REPORT_ID+"_data.json");
////        if (datafile.exists()) {
////            FileReadWrite readWrite = new FileReadWrite();
////            String metaString = readWrite.loadJSON(fileLocation+"/"+FOLDER_NAME.replaceAll("\\W", "").trim()
////                            +"/"+REPORT_NAME.replaceAll("\\W", "").trim()+"_"+REPORT_ID+"/"+REPORT_NAME.replaceAll("\\W", "").trim()+"_"+REPORT_ID+"_data.json");
//////           
////            Type tarType = new TypeToken<XtendReportMeta>() {}.getType();
////            reportMeta = gson.fromJson(metaString, tarType);
//// viewBysList= reportMeta.getViewIds();
//// viewBysNameList= reportMeta.getViewNames();
//// measBys1=   reportMeta.getMeasIds();
////rowMeasNamesArr1=  reportMeta.getMeasNames();
//// AggType1=  reportMeta.getAggregations();
////        }
//                    
//                    
//                    
//                }
//            }
    }

    public PbReturnObject selectprgpbtalent() throws SQLException {
        String query = getResourceBundle().getString("selectprgpbtalent");
        return this.execSelectSQL(query);
    }

    public PbReturnObject selectprgUserAssingnment(String userId) throws SQLException {
        String query = getResourceBundle().getString("selectprgUserAssingnment");
        Object params[] = new Object[1];
        params[0] = userId;
        String finalQuery = this.buildQuery(query, params);
        return this.execSelectSQL(finalQuery);
    }

    public int insertSaveDiemension(String name, String desc) throws Exception {
        String query = getResourceBundle().getString("insertSaveDiemension");
        Object params[] = new Object[1];
        params[0] = name;
        params[1] = desc;
        String finalQuery = this.buildQuery(query, params);
        return this.execUpdateSQL(finalQuery);
    }
    //added by Dinanath for showing existing dim and measure of AO

    public String getExistingDimOrFactForAO(String aoId, String REPORTID, HttpServletRequest request) throws Exception {
        String result = null;
         HttpSession session = request.getSession(false);
        PbReportViewerDAO dao = new PbReportViewerDAO();
        String userId = String.valueOf(session.getAttribute("USERID"));
        Gson gson = new Gson();
        HashMap<String, List> aoMasterMap = new HashMap<>();
        List<String> viewBysList = new ArrayList<>();
        List<String> viewBysNameList = new ArrayList<>();
        List<String> measBys1 = new ArrayList<>();
        List<String> rowMeasNamesArr1 = new ArrayList<>();
        ReportObjectMeta roMeta = new ReportObjectMeta();
         String fileLocation = "";  //added by Mohit
          if (session != null) {
            fileLocation = new PbReportViewerDAO().getFilePath(session);
          }
        try {
            File datafile = new File(fileLocation+"/analyticalobject/M_AO_" + aoId + ".json");
            if (datafile.exists()) {
                FileReadWrite readWrite = new FileReadWrite();
                String metaString = readWrite.loadJSON(fileLocation+"/analyticalobject/M_AO_" + aoId + ".json");
//            Gson gson = new Gson();
                Type tarType = new TypeToken<ReportObjectMeta>() {
                }.getType();
                roMeta = gson.fromJson(metaString, tarType);
                viewBysList = roMeta.getViewIds();
                viewBysNameList = roMeta.getViewNames();
                measBys1 = roMeta.getMeasIds();
                rowMeasNamesArr1 = roMeta.getMeasNames();
                aoMasterMap.put("viewbyIds", viewBysList);
                aoMasterMap.put("viewbyName", viewBysNameList);
                aoMasterMap.put("measureIds", measBys1);
                aoMasterMap.put("measure", rowMeasNamesArr1);
            }
            Gson json = new Gson();
            String jsonString = json.toJson(aoMasterMap);
            result = jsonString;
            return result;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
            return "Failed";
        }
    }

    public String getDateDetails(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);

        String query;
        String query1;
        String[] colTypeList = null;
        String createdDate = "";
        PbReportQuery repQry = new PbReportQuery();
        PbDb pbdb = new PbDb();
        PbReturnObject pbro;
        PbReturnObject pbro1;
        PbReturnObject pbro2;
        PbReturnObject pbro3;
        Connection conn;
        HashMap map = new HashMap();
        String elementId = request.getParameter("Msrs");
        String AOEdit = request.getParameter("isFromAOEdit");
        String AOId = request.getParameter("reportid");
        String MsrsList = request.getParameter("MsrsList");
        String viewbys = request.getParameter("viewbys");

        HashMap<String, String> measurType = new HashMap<>();
        HashMap<String, String> refferedEle = new HashMap<>();

        Container container = null;
        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(AOId);

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            if (!AOEdit.equalsIgnoreCase("true")) {
                Date date = new Date();
                createdDate = formatter.format(date);

                conn = repQry.getConnection(elementId);

                switch (ProgenConnection.getInstance().getDatabaseType()) {
                    case ProgenConnection.SQL_SERVER:
                        //     query=" SELECT cm_cust_name,pm_cust_name,pym_cust_name,cq_cust_name,lq_cust_name,lyq_cust_name,cy_cust_name,ly_cust_name from pr_day_denom  WHERE ddate = convert(datetime,'"+date+" ',120)";
                        query = "select cy_st_date from pr_day_denom  WHERE ddate = convert(datetime,'" + createdDate + " ',120)";
                        break;
                    case ProgenConnection.MYSQL:
                        //query=" SELECT cm_cust_name,pm_cust_name,pym_cust_name,cq_cust_name,lq_cust_name,lyq_cust_name,cy_cust_name,ly_cust_name from pr_day_denom  WHERE ddate = str_to_date('"+date+" ','%m/%d/%Y')";
                        query = "select cy_st_date from pr_day_denom  WHERE ddate = str_to_date('" + createdDate + " ','%m/%d/%Y')";
                        break;
                    default:
                        //query=" SELECT cm_cust_name,pm_cust_name,pym_cust_name,cq_cust_name,lq_cust_name,lyq_cust_name,cy_cust_name,ly_cust_name from pr_day_denom  WHERE ddate = to_date('"+date+" ','mm/dd/yyyy')";
                        query = "select cy_st_date from pr_day_denom  WHERE ddate = to_date('" + createdDate + " ','mm/dd/yyyy')";
                        break;
                }
                pbro = pbdb.execSelectSQL(query, conn);
                if (pbro != null) {
                    Date tdate = pbro.getFieldValueDate(0, 0);
                    createdDate = createdDate + "," + formatter.format(tdate);
                }

            } else {

                String measuresList = MsrsList.substring(1);
                String[] newMeasuresList = measuresList.split(",");
                List<String> newViewbysList = Arrays.asList(viewbys.substring(1).split(","));
                ArrayList<String> oldMeasuresList = new ArrayList<>();
                ArrayList<String> oldViewbysList = new ArrayList<>();

                query1 = "select element_id,user_col_type from PRG_AR_AO_DETAILS  WHERE AO_ID=" + AOId + " and is_fact='Y'";
//                query1 = "select user_col_type from PRG_user_all_info_details  WHERE element_ID in('"+msrsList.substring(1)+"')";
////                query1 = "select * from PRG_AR_AO_DETAILS  WHERE AO_ID='"+AOId+"'";
                pbro1 = pbdb.execSelectSQL(query1);
                if (pbro1 != null) {
                    for (int i = 0; i < pbro1.rowCount; i++) {
                        oldMeasuresList.add(pbro1.getFieldValueString(i, 0));
                        //   measurType.put(pbro1.getFieldValueString(i, 0), pbro1.getFieldValueString(i, 1));
                    }
                }
                query1 = "select element_id, user_col_type,REFFERED_ELEMENTS from PRG_user_all_info_details  WHERE element_ID in(" + MsrsList.substring(1) + ")";
                pbro2 = pbdb.execSelectSQL(query1);
                if (pbro2 != null) {
                    for (int i = 0; i < pbro2.rowCount; i++) {
                        refferedEle.put(pbro2.getFieldValueString(i, 0), pbro2.getFieldValueString(i, 2));
                        measurType.put(pbro2.getFieldValueString(i, 0), pbro2.getFieldValueString(i, 1));
                    }
                }
                query1 = "select element_id from PRG_AR_AO_DETAILS  WHERE AO_ID=" + AOId + " and is_fact='N'";
                pbro3 = pbdb.execSelectSQL(query1);
                if (pbro3 != null) {
                    for (int i = 0; i < pbro3.rowCount; i++) {
                        oldViewbysList.add(pbro3.getFieldValueString(i, 0));
                    }
                }
                if (oldViewbysList.size() == newViewbysList.size() || oldViewbysList.containsAll(newViewbysList)) {

                    if (newMeasuresList.length < oldMeasuresList.size()) {
                        container.aoEditSummFlag = true;
                    } else {
                        for (int i = 0; i < newMeasuresList.length; i++) {
                            if (!oldMeasuresList.contains(newMeasuresList[i])) {
                                if (!measurType.get(newMeasuresList[i]).equalsIgnoreCase("SUMMARIZED")) {
                                    container.aoEditSummFlag = true;
                                    break;
                                } else {
                                    String[] reffelements = refferedEle.get(newMeasuresList[i]).split(",");
                                    for (int j = 0; j < reffelements.length; j++) {
                                        if (!oldMeasuresList.contains(reffelements[j])) {
                                            container.aoEditSummFlag = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    container.aoEditSummFlag = true;
                }
                query = "select TIME_DETAILS from PRG_AR_AO_MASTER  WHERE AO_ID='" + AOId + "'";
                pbro = pbdb.execSelectSQL(query);
                if (pbro != null) {
                   if(!pbro.getFieldValueString(0, 0).equalsIgnoreCase(""))
                   {
                    createdDate = pbro.getFieldValueString(0, 0).substring(1);
                }
            }
            }
        } catch (IOException e) {
        }
        return createdDate;
    }

    public String addNewPage(HttpServletRequest request) {
        PbReturnObject retobj = null;
        String reportId = request.getParameter("reportId");
        String userId = request.getParameter("usersId");
        if(userId==null){
            userId=XtendAdapter.userID;
        }
        String query = "select max(page_sequence) from prg_report_page_mapping_local where user_id='"+userId+"' group by report_id having report_id='" + reportId + "'";
        PbDb pbdb = new PbDb();
        String pageLabel = request.getParameter("pageLabel");

        try {
            retobj = pbdb.execSelectSQL(query);
        } catch (SQLException ex) {
//            Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        int nextSequence = 0;
        String maxSequence = "0";
        if (retobj != null && retobj.getRowCount() > 0) {
            maxSequence = retobj.getFieldValueString(0, 0);
        }
        try {
            nextSequence = Integer.parseInt(maxSequence) + 1;
        } catch (Exception ex) {
            nextSequence = 1;
        }
        String nextPageId = "_page" + nextSequence;
        query = "insert into prg_report_page_mapping_local values(" + reportId + ",'" + nextPageId + "','" + pageLabel + "'," + nextSequence + ",'"+userId+"')";
        try {
            pbdb.execInsert(query);
        } catch (SQLException ex) {
//            Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
//            Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        query = "select max(page_sequence) from prg_report_page_mapping group by report_id having report_id='" + reportId + "'";

        try {
            retobj = pbdb.execSelectSQL(query);
        } catch (SQLException ex) {
//            Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        nextSequence = 0;
        maxSequence = "0";
        if (retobj != null && retobj.getRowCount() > 0) {
            maxSequence = retobj.getFieldValueString(0, 0);
        }
        try {
            nextSequence = Integer.parseInt(maxSequence) + 1;
        } catch (Exception ex) {
            nextSequence = 1;
        }
        String nextPageId1 = "_page" + nextSequence;
        query = "insert into prg_report_page_mapping values(" + reportId + ",'" + nextPageId1 + "','" + pageLabel + "'," + nextSequence + ")";
        try {
            pbdb.execInsert(query);
        } catch (SQLException ex) {
//            Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
//            Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nextPageId + ":" + nextSequence;
    }

    public String createFirstPage(HttpServletRequest request) {
        PbReturnObject retobj = null;
        PbDb pbdb = new PbDb();
        String reportId = request.getParameter("reportId");
        String pageLabel = request.getParameter("pageLabel");
        String userId = request.getParameter("usersId");
        if(userId == null){
          userId=  XtendAdapter.userID;
        }
        String query = "select * from prg_report_page_mapping_local where report_id=" + reportId+" and user_id='"+userId+"'";
        try {
            retobj = pbdb.execSelectSQL(query);
        } catch (SQLException ex) {
//            Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        String pageId = "default";
        if (retobj != null && retobj.getRowCount() == 0) {
            query = "insert into prg_report_page_mapping_local values(" + reportId + ",'" + pageId + "','" + pageLabel + "',1,'"+userId+"')";
            try {
                pbdb.execInsert(query);

            } catch (SQLException ex) {
//                Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
//                Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        query = "select * from prg_report_page_mapping where report_id=" + reportId;
        try {
            retobj = pbdb.execSelectSQL(query);
        } catch (SQLException ex) {
//            Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (retobj != null && retobj.getRowCount() == 0) {
            query = "insert into prg_report_page_mapping values(" + reportId + ",'" + pageId + "','" + pageLabel + "',1)";
            try {
                pbdb.execInsert(query);

            } catch (SQLException ex) {
//                Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
//                Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pageId;
    }

    public void updatePageSequence(String reportId, String pageId, String userId) {
        PbDb pbdb = new PbDb();
        if(userId==null){
            userId=XtendAdapter.userID;
        }
        String query = "update prg_report_page_mapping_local set page_sequence = page_sequence+1 where report_id=" + reportId +" and user_id="+userId;
        try {
            pbdb.execUpdateSQL(query);
            query = "update prg_report_page_mapping_local set page_sequence=1 where report_id=" + reportId + " and page_id='" + pageId + "'" + " and user_id="+userId;
            pbdb.execUpdateSQL(query);
        } catch (Exception ex) {
//            Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updatePageLabels(HttpServletRequest request, String reportId, String pageLabel, String currentPage) {
      PbDb pbdb = new PbDb();
      String userId=request.getParameter("usersId");
      if(userId==null){
            userId=XtendAdapter.userID;
        }
      String query = " update prg_report_page_mapping_local set page_label='"+pageLabel+"' where report_id="+reportId+" and page_id='"+currentPage+"'" + " and user_id="+userId;
        try {
            pbdb.execUpdateSQL(query); 
        } catch (Exception ex) {
            logger.error("Exception:", ex);
}
    }
    //added by Dinanath as on 22 march 2016 for defining kpi alert scheduelrs
    public void updateKPISchedulerDetails(String schedulerId, KPIAlertSchedule kpiSchedule) throws Exception {
        String qry = getResourceBundle().getString("updateKPISchedulerdetails");
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(kpiSchedule.getStartDate());
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(kpiSchedule.getEndDate());
        Date sdate = kpiSchedule.getStartDate();
        Date edate = kpiSchedule.getEndDate();
        kpiSchedule.setStartDate(null);
        kpiSchedule.setEndDate(null);
        List<KPIAlertSchedule> scheduleList = new ArrayList<KPIAlertSchedule>();
        scheduleList.add(kpiSchedule);
        Gson gson = new Gson();
        String gsonString = gson.toJson(scheduleList);
        Object[] obj = new Object[4];
        obj[0] = gsonString;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            obj[1] = startCalendar.get(Calendar.YEAR) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.DATE);
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            obj[1] = startCalendar.get(Calendar.YEAR) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.DATE);
        } else {
            obj[1] = startCalendar.get(Calendar.DATE) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.YEAR);
}

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            obj[2] = endCalendar.get(Calendar.YEAR) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.DATE);
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            obj[2] = endCalendar.get(Calendar.YEAR) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.DATE);
        } else {
            obj[2] = endCalendar.get(Calendar.DATE) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.YEAR);
        }

        obj[3] = schedulerId;
        String finalqry = buildQuery(qry, obj);
            execUpdateSQL(finalqry);
       
        kpiSchedule.setStartDate(sdate);
        kpiSchedule.setEndDate(edate);
    }
//added by Dinanath as on 22 march 2016 for defining kpi alert scheduelrs
    public String getKPISchedulerID(int reportId, String sName, String userId, String ctxPath, String createdby, Date sDate, Date eDate) throws Exception {
        PbReturnObject retobj = new PbReturnObject();
        String insertQuery = getResourceBundle().getString("insertKPISchedulerdetails");
        Object obj2[] = new Object[6];
        obj2[0] = userId;
        obj2[1] = reportId;
        obj2[2] = sName;
        obj2[3] = ctxPath;
        obj2[4] = createdby;
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String todayDate = formatter.format(date);
        obj2[5] = todayDate;
        
        String finalqry2 = buildQuery(insertQuery, obj2);
        int i = execUpdateSQL(finalqry2);
        if (i > 0) {
            String qry = "select PRG_PERSONALIZED_ID from prg_ar_personalized_kpi where PRG_REPORT_ID='&' and PRG_REPORT_CUST_NAME='&'";
            Object[] obj = new Object[2];
            obj[0] = reportId;
            obj[1] = sName;
            String finalqry = buildQuery(qry, obj);
            retobj = execSelectSQL(finalqry);
        String schedulerId = retobj.getFieldValueString(0, 0).toString();
        return schedulerId;
        }
        return null;
    }

    public String getDataFlag(String reportId) {
       PbDb pbdb = new PbDb();
       PbReturnObject retObj= null;
       String dataFlag = "";
       String query = "select DATA_FLAG from prg_ar_report_table_master where REPORT_ID="+reportId;
        try {
            retObj = pbdb.execSelectSQL(query);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
}
       if(retObj!=null && retObj.rowCount>0){
           dataFlag = retObj.getFieldValueString(0, "DATA_FLAG");
       }
        return dataFlag;
    }
    
    
    //Added by Mohit for Cache management
    public ProgenDataSet getReturnObjectBasedOnReport(String reportid, String query, String fileLocation, String userid) {
        ProgenDataSet pbretObj = null;
        List<Object> pbretObjlist = null;
        String oldquery = "";
        Map<String, String> allqueries = new LinkedHashMap<>();
        List<Map<String, String>> list = null;
        File file1 = null;
        try {
            File datafile = new File(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + reportid + ".json");
            if (datafile.exists()) {
                FileReadWrite readWrite = new FileReadWrite();
                String metaString = readWrite.loadJSON(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + reportid + ".json");
                Gson gson = new Gson();
                Type tarType = new TypeToken<Map<String, String>>() {
                }.getType();
                allqueries = gson.fromJson(metaString, tarType);
                for (int i = 1; i <= allqueries.size(); i++) {
                    if (allqueries.get("QUERY" + i).toString().equalsIgnoreCase(query)) {
                        datafile = new File(fileLocation + "/AO_Data/" + userid + "/" + reportid  + "/" + "QUERY" + i + ".txt");
                        FileInputStream fis = new FileInputStream(datafile);
                        ObjectInputStream in = new ObjectInputStream(fis);
                        pbretObj = (ProgenDataSet) in.readObject();
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
//Added by Mohit for Cache management

    public boolean setReturnObjectBasedOnReport(String reportid,String query, ProgenDataSet pbretObj, String fileLocation, String userid) throws FileNotFoundException {
        FileReadWrite fileReadWrite = new FileReadWrite();
        File file;
        File datafile;
        Gson gson = new Gson();
        Map<String, String> allqueries = new LinkedHashMap<>();
        file = new File(fileLocation + "/AO_Data/" + userid + "/" + reportid);
        String path = file.getAbsolutePath();
        String fName = path + File.separator + reportid + ".json";
        File f1 = new File(path);
        try {
            File file1 = new File(fName);
            f1.mkdirs();
            datafile = new File(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + reportid + ".json");
            if (!datafile.exists()) {
                try {
                    datafile.createNewFile();
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }
                allqueries.put("QUERY1", query);
                fileReadWrite.writeToFile(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + reportid + ".json", gson.toJson(allqueries));
//fileReadWrite.writeToFile(fileLocation+"/AO_Data/"+userid+"/"+reportid+"/"+chartid +"/"+"QUERY"+(allqueries.size())+ ".json", gson.toJson(list));
                file1 = new File(fileLocation + "/AO_Data/" + userid + "/" + reportid  + "/" + "QUERY" + (allqueries.size()) + ".txt");
                file1.createNewFile();
                FileOutputStream fout = new FileOutputStream(file1);
                ObjectOutputStream out = new ObjectOutputStream(fout);
                out.writeObject(pbretObj);
                out.flush();
            } else {
                FileReadWrite readWrite = new FileReadWrite();
                String metaString = readWrite.loadJSON(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + reportid + ".json");
                Type tarType = new TypeToken<Map<String, String>>() {
                }.getType();
                allqueries = gson.fromJson(metaString, tarType);
                if (allqueries.size() < 50) {
                    allqueries.put("QUERY" + (allqueries.size() + 1), query);
                    fileReadWrite.writeToFile(fileLocation + "/AO_Data/" + userid + "/" + reportid + "/" + reportid + ".json", gson.toJson(allqueries));
//fileReadWrite.writeToFile(fileLocation+"/AO_Data/"+userid+"/"+reportid+"/"+chartid +"/"+"QUERY"+(allqueries.size())+ ".json", gson.toJson(list));
                    file1 = new File(fileLocation + "/AO_Data/" + userid + "/" + reportid  + "/" + "QUERY" + (allqueries.size()) + ".txt");
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
    
    
      public void setGraphFilterData(Container container, String reportId, String elementId,String userId) throws SQLException, FileNotFoundException {
       String fileLocation="";
       Gson gson = new Gson();
       String ao_name="R_GO_"+reportId;
       if((container.getReportCollect().AOId!=null && !container.getReportCollect().AOId.equalsIgnoreCase(""))){
           ao_name ="M_AO_"+ container.getReportCollect().AOId;
       }
        String metaConnectionType= ProgenConnection.getInstance().getDatabaseType();
       List<String> viewIds = new ArrayList<String>();
       HttpSession session = SessionListener.getSession(SessionListener.getSessionID());
        PbReportViewerDAO pbDao = new PbReportViewerDAO();
        File file = null;
                if (session != null) {
                    fileLocation = pbDao.getFilePath(session);
                } else {
                    fileLocation = "/usr/local/cache";
                }
         String goPath = fileLocation + "/analyticalobject/R_GO_" + reportId + ".json";
          FileReadWrite fileReadWrite = new FileReadWrite();
            try {
                file = new File(goPath);
                if (file.exists()) {
                    String goData = fileReadWrite.loadJSON(goPath);
                    ReportObjectMeta goMeta = new ReportObjectMeta();
                    Type goType = new TypeToken<ReportObjectMeta>() {
                    }.getType();
                    goMeta = gson.fromJson(goData, goType);
                viewIds = goMeta.getViewIds();
                }
            } catch (Exception e) {
            }
        ProgenAOQuery pbAoQuery = new ProgenAOQuery();
        Connection con = null; 
        ReportManagementDAO dao1 =new ReportManagementDAO();
         PbDb pbDb =new PbDb();
          if (file.exists()) {
        for(int i=0;i<viewIds.size();i++){
            ArrayList viewIdList = new ArrayList();
           
            if(!viewIds.get(i).equalsIgnoreCase("TIME")){
                
            viewIdList.add("A_"+viewIds.get(i));
            }else{
                continue;
            }
            
              ao_name = pbAoQuery.aoReplace(ao_name, viewIdList);
          
               String qry = "";
          if(metaConnectionType!=null && !metaConnectionType.equalsIgnoreCase("MySql")){
              
               qry = "select /*+ CURSOR_SHARING_EXACT RESULT_CACHE */ A1 "
                  + "from (select RANK() over(order by A1) AS num1,A1 "
                  + "from (select distinct("+viewIdList.get(0)+") A1 from "+ao_name+" ) O1 ) O2 "
                  + "where num1 between 1 and 20 order by 1 asc";   
         
           PbReturnObject roleObj=null;
              roleObj =   (PbReturnObject) dao1.getReturnObjectBasedOnLov(reportId,viewIdList.get(0).toString(),qry,fileLocation,userId);
              if(roleObj==null){
                 System.out.println(".....................................Filter query Fired  ......................."+qry);
//                 rs = st.executeQuery(qry1);
                   con=ProgenConnection.getInstance().getConnectionForElement(elementId);
                try {
                    roleObj = pbDb.execSelectSQL(qry,con);
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
//                System.out.println("ddd"+roleObj.getFieldValueString(0, 0));
//               roleObj.prepareObject(rs);
//                   roleObj =  st.executeQuery(qry1);
              dao1.setReturnObjectBasedOnLov(reportId,viewIdList.get(0).toString(),qry,roleObj,fileLocation,userId);
                    }
        }    
         
          }
          }
            
    }
    public String removePage(HttpServletRequest request, String reportId, String currentPage) {
        String flag=request.getParameter("flag");
        PbDb pbdb = new PbDb();
        String userId=request.getParameter("usersId");
        if(userId==null){
            userId=XtendAdapter.userID;
        }
        String newPage="";
        if(flag.equals("local")){
            String query = " select * from prg_report_page_mapping_local where report_id=" + reportId+" and user_id='"+userId+"'";
            PbReturnObject retobj = null;
            try {
                retobj=pbdb.executeSelectSQL(query);
                if (retobj != null && retobj.getRowCount() < 2) {
                    return "failure";
                }
                else{
                    newPage=retobj.getFieldValueString(retobj.getRowCount()-2, "page_id");
                    query = "delete from prg_report_page_mapping_local where report_id=" + reportId +" and page_id='"+currentPage+"' and user_id='"+userId+"'";
                    pbdb.execUpdateSQL(query);
                    query = " select * from prg_report_page_mapping_local where report_id=" + reportId+" and user_id='"+userId+"'";
                    retobj=pbdb.executeSelectSQL(query);
                    newPage=retobj.getFieldValueString(0, "page_id");
                }
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }
        else{
        String query = " select * from prg_report_page_mapping where report_id=" + reportId;
        PbReturnObject retobj = null;
        try {
            retobj=pbdb.executeSelectSQL(query);
            if (retobj != null && retobj.getRowCount() < 2) {
                return "failure";
            }
            else{
                newPage=retobj.getFieldValueString(retobj.getRowCount()-2, "page_id");
                query = "delete from prg_report_page_mapping where report_id=" + reportId +" and page_id='"+currentPage+"'";
                pbdb.execUpdateSQL(query);
                query = " select * from prg_report_page_mapping where report_id=" + reportId;
                retobj=pbdb.executeSelectSQL(query);
                newPage=retobj.getFieldValueString(0, "page_id");
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        }
        return newPage;
    }
       //Added by Bhargavi to save AO scheduler details on 5th April 2016

    public void updateAOSchedulerDetails(String schedulerId, AOSchedule aoSchedule) throws Exception {
        String qry = getResourceBundle().getString("updateAOSchedulerdetails");
Connection con = null;
        PreparedStatement ps = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//        String sdate = formatter.format(aoSchedule.getStartDate());
//        String edate = formatter.format(aoSchedule.getEndDate());

       

//        Object[] obj = new Object[10];
//        obj[0] = aoSchedule.getStartDate();
//        obj[1] = aoSchedule.getEndDate();
//        obj[2] = aoSchedule.getMailIds();
//        obj[3] = aoSchedule.getLoadType();
//        obj[4] = aoSchedule.getScheduledTime();
//        obj[5] = aoSchedule.getFrequency();
//        obj[6] = schedulerId;
//        obj[7] = aoSchedule.getloadStartDate();
//        obj[8] = aoSchedule.getloadStartDate();
//        obj[9] = aoSchedule.getDateType();

con = ProgenConnection.getInstance().getConnection();
           
            ps = con.prepareStatement(qry);
            try {
                                Date parseS = dateFormat.parse(aoSchedule.getStartDateString());
                                Date parseE = dateFormat.parse(aoSchedule.getEndDateString());

                                 ps.setDate(1, new java.sql.Date(parseS.getTime()));
                                 ps.setDate(2, new java.sql.Date(parseE.getTime()));
                            } catch (ParseException ex) {
                                logger.error("Exception: ",ex);
                            }
          ps.setString(3, aoSchedule.getMailIds());
          ps.setString(4, aoSchedule.getLoadType());
          ps.setString(5, aoSchedule.getScheduledTime());
          ps.setString(6, aoSchedule.getFrequency());
          ps.setString(7, aoSchedule.getloadStartDate());
          ps.setString(8, aoSchedule.getloadEndDate());
          ps.setString(9, aoSchedule.getDateType());
          ps.setString(10, schedulerId);
            
             
            ps.executeUpdate();

            ps.close();
            if (con != null) {
                con.close();
            }
//        String finalqry = buildQuery(qry, obj);
//        execUpdateSQL(finalqry);

    }
    public void getAOdetails(String AO_id, AOSchedule aoSchedule)throws Exception{
        
        PbDb pbDB = new PbDb();
                ArrayList<String> viewBysList = new ArrayList();
                ArrayList<String> colviewBysList = new ArrayList();
                ArrayList<String> viewIdsList = new ArrayList();
                ArrayList<String> measureNameList = new ArrayList();
                ArrayList<String> measureIdsList = new ArrayList();
                ArrayList<String> aggTypeValuesList = new ArrayList();
            PbReturnObject PbRetObj;
            PbReturnObject PbRetObj1;
            String sql = "select disp_name, element_id from prg_ar_ao_details where ao_id='" + AO_id + "' and is_dim='y'";
            PbRetObj = pbDB.execSelectSQL(sql);
            if (PbRetObj.rowCount > 0) {
                for(int i=0; i< PbRetObj.rowCount; i++){
                viewBysList.add(PbRetObj.getFieldValueString(i, 0));
                viewIdsList.add(PbRetObj.getFieldValueString(i, 1));
            }
            }
            viewIdsList.add("TIME");
            String sql1 = "select disp_name, element_id,aggr_type from prg_ar_ao_details where ao_id='" + AO_id + "' and is_fact='y'";
            PbRetObj1 = pbDB.execSelectSQL(sql1);
            if (PbRetObj1.rowCount > 0) {
             for(int i=0; i< PbRetObj1.rowCount; i++){
                measureNameList.add(PbRetObj1.getFieldValueString(i, 0));
                measureIdsList.add(PbRetObj1.getFieldValueString(i, 1));
                aggTypeValuesList.add(PbRetObj1.getFieldValueString(i, 2));
            }
            }
              HashMap<String, ArrayList<String>> dataMap = new HashMap<>();
            dataMap.put("viewBys", viewBysList);
            dataMap.put("viewIds", viewIdsList);
            dataMap.put("colviewIds", colviewBysList);
            dataMap.put("measName", measureNameList);
            dataMap.put("measId", measureIdsList);
            dataMap.put("aggType", aggTypeValuesList);
            
           aoSchedule.setAOdetails(dataMap); 
    }
    public String getManagementDashboardFlag(String reportId){
        String query="select MGMT_TEMP_FLAG from prg_ar_report_master where report_id="+reportId;
        PbDb pbDB = new PbDb();
        PbReturnObject retObj=null;
        try {
            retObj=pbDB.execSelectSQL(query);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
}
        return retObj.getFieldValueString(0, 0);
    }

    public void setMgtmlTemplateFlag(String reportId,String flag) {
        PbDb pbDB = new PbDb();
        flag="true";
        String query="update prg_ar_report_master set MGMT_TEMP_FLAG='"+flag+"' where report_id="+reportId;
        try {
            pbDB.execUpdateSQL(query);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ProgenReportViewerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
