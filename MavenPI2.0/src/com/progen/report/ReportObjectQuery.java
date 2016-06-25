/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.report.query.PbTimeRanges;
import com.template.meta.ManagementTemplateMeta;
import java.io.File;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import prg.business.group.SaveBucketsBD;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

/**
 *
 * @author progen
 */
public class ReportObjectQuery {

    public static Logger logger = Logger.getLogger(ReportObjectQuery.class);
    public String[] elementArrList = null;
    public ArrayList reportQryElementIds = new ArrayList();
     public static String filePath = "";

    public String getObjectQuery(String reportId) {
        ReportObjectMeta roMeta = new ReportObjectMeta();
        String finalSql = "";
        File datafile = new File("/usr/local/cache/analyticalobject/R_GO_" + reportId + ".json");
        if (datafile.exists()) {
            FileReadWrite readWrite = new FileReadWrite();

            String metaString = readWrite.loadJSON("/usr/local/cache/analyticalobject/R_GO_" + reportId + ".json");
            Gson gson = new Gson();
            Type tarType = new TypeToken<ReportObjectMeta>() {
            }.getType();

            roMeta = gson.fromJson(metaString, tarType);
            String filterClause = "";
            String finalSql_AO = "select * from R_GO_" + reportId + "  where 1=1 " + filterClause + "  ";
            finalSql = " select " + roMeta.getOmViewByCol_AO() + " , " + roMeta.getOmorderByCol_AO() + roMeta.getOmsql_AO() + " from ( " + finalSql_AO + " ) O7_1 ";
            return finalSql;
        } else {
            return "noData";
        }
    }

    public String modifyObjectQuery(Map<String, List<String>> map) {

        return null;
    }
    //added By Nazneen on 19 Jan 2015 for Analytical object final query

    public String getObjectQueryAO(String reportId, String filterClause, ArrayList<String> rowViewIds, ArrayList<String> colViewIds, String startDate, String endDate, String timeLevel) {
        ReportObjectMeta roMeta = new ReportObjectMeta();
//        String finalSql = "";
        File datafile = new File(filePath+"/analyticalobject/R_AO_" + reportId + ".json");
        if (datafile.exists()) {
            FileReadWrite readWrite = new FileReadWrite();
            String metaString = readWrite.loadJSON(filePath+"/analyticalobject/R_AO_" + reportId + ".json");
            Gson gson = new Gson();
            Type tarType = new TypeToken<ReportObjectMeta>() {
            }.getType();
            roMeta = gson.fromJson(metaString, tarType);
//            String query;
            String osql_AO = roMeta.getOsql_AO();
            String OViewByCol_AO = roMeta.getOViewByCol_AO();
//            String OorderByCol_AO = roMeta.getOorderByCol_AO();
            String OmViewByCol_AO = roMeta.getOmViewByCol_AO();
            String OmorderByCol_AO = roMeta.getOmorderByCol_AO();
            String ColOrderByCol_AO = roMeta.getColOrderByCol_AO();
            String omsql_AO = roMeta.getOmsql_AO();
            String osqlGroup_AO = roMeta.getOsqlGroup_AO();
            String finalViewByCol_AO = roMeta.getFinalViewByCol_AO();
            String oWrapper_AO = roMeta.getoWrapper_AO();
            String dateValues_AO = roMeta.getDateValues_AO();
            String stDateAO = "";
            String edDateAO = "";
            String timeLevel_AO = "";
            OViewByCol_AO = "";
            OmViewByCol_AO = "";
            finalViewByCol_AO = "";
            OmorderByCol_AO = "";
            boolean isValidDate = true;
            if (dateValues_AO != null && !dateValues_AO.equalsIgnoreCase("")) {
                String[] dateVals = dateValues_AO.split(";");

                for (int i = 0; i < dateVals.length; i++) {
                    String temp = dateVals[i];
                    if (temp.contains("stDate")) {
                        stDateAO = temp.substring(temp.indexOf("~") + 1).replace("00:00:00", "").replace(" ", "");
                    } else if (temp.contains("endDate")) {
                        edDateAO = temp.substring(temp.indexOf("~") + 1).replace("00:00:00", "").replace(" ", "");
                    } else if (temp.contains("timeLevel_AO")) {
                        timeLevel_AO = temp.substring(temp.indexOf("~") + 1);
                    }
                }
                boolean isDate = false;
                SimpleDateFormat formatter1;
                String datePattern = "\\d{4}-\\d{1,2}-\\d{1,2}";
                isDate = stDateAO.matches(datePattern);
                if (isDate) {
                    formatter1 = new SimpleDateFormat("yyyy-mm-dd");
                } else {
                    formatter1 = new SimpleDateFormat("mm/dd/yyyy");
                }
                // SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                try {
                    Date sdateAO = formatter1.parse(stDateAO);
                    Date edateAO = formatter1.parse(edDateAO);
                    Date sdate = formatter1.parse(startDate);
                    Date edate = formatter1.parse(endDate);
                    stDateAO = sdf.format(sdateAO);
                    edDateAO = sdf.format(edateAO);
                    if (!(sdate.equals(sdateAO) || sdate.after(sdateAO) && sdate.before(edateAO)) && !(edate.equals(edateAO) || edate.before(edateAO) && edate.after(sdateAO))) {
                        isValidDate = false;
                    }
                } catch (ParseException ex) {
                    logger.error("Exception:", ex);
                }
                if (!timeLevel_AO.equalsIgnoreCase(timeLevel)) {
                    isValidDate = false;
                }
            }

            if (isValidDate) {
                ArrayList<String> totalViewIds = new ArrayList<String>();
                for (int i = 0; i < rowViewIds.size(); i++) {
                    if (!totalViewIds.contains(rowViewIds.get(i))) {
                        totalViewIds.add(rowViewIds.get(i).toString());
                    }
                }
                for (int i = 0; i < colViewIds.size(); i++) {
                    if (!totalViewIds.contains(colViewIds.get(i))) {
                        totalViewIds.add(colViewIds.get(i).toString());
                    }
                }
                for (int i = 0; i < totalViewIds.size(); i++) {
                    if (totalViewIds.get(i).toString().equalsIgnoreCase("TIME") || totalViewIds.get(i).toString().trim().equalsIgnoreCase("TIME")) {
                        OViewByCol_AO += ", " + totalViewIds.get(i).toString();
                        OmViewByCol_AO += ", O_" + totalViewIds.get(i).toString() + " AS " + totalViewIds.get(i).toString();
                        finalViewByCol_AO += ", " + totalViewIds.get(i).toString() + " AS " + totalViewIds.get(i).toString();
//                    OmorderByCol_AO += ", " + totalViewIds.get(i).toString() + " AS ORDER" + (i + 1);
                        OmorderByCol_AO += ", " + totalViewIds.get(i).toString() + " AS O_" + totalViewIds.get(i).toString();
                    } else {
                        OViewByCol_AO += ", " + "A_" + totalViewIds.get(i).toString();
                        OmViewByCol_AO += ", " + "A_" + totalViewIds.get(i).toString() + " AS " + "A_" + totalViewIds.get(i).toString();
                        finalViewByCol_AO += ", " + "A_" + totalViewIds.get(i).toString() + " AS " + "A_" + totalViewIds.get(i).toString();
//                    OmorderByCol_AO += ", " + "A_" + totalViewIds.get(i).toString() + " AS ORDER" + (i + 1);
                        OmorderByCol_AO += ", " + "A_" + totalViewIds.get(i).toString() + " AS O_" + totalViewIds.get(i).toString();
                    }
                }
                OViewByCol_AO = OViewByCol_AO.substring(1);
                OmViewByCol_AO = OmViewByCol_AO.substring(1);
                finalViewByCol_AO = finalViewByCol_AO.substring(1);
                OmorderByCol_AO = OmorderByCol_AO.substring(1);
                String finalSql_AO = "noData";
                String tableName = "R_AO_" + reportId;
                String qry = "Select COLUMN_NAME  from user_tab_columns where table_name='" + tableName + "'";
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    qry = "SELECT  COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='" + tableName + "'";
                }
                //check whether table exists in data and contains column equal to view by's and filter elements are there in table
                Connection conn = null;
                boolean tableFlag = true;
                PbDb pbdb = new PbDb();
                PbReturnObject rs = null;
                ProgenParam connectionparam = new ProgenParam();
//                String checkClause = "";
                StringBuilder checkClause = new StringBuilder(400);
                if (totalViewIds != null && !totalViewIds.isEmpty() && totalViewIds.size() > 0) {
                    conn = connectionparam.getConnection(reportQryElementIds.get(0).toString().replace("A_", ""));
                    try {
                        rs = pbdb.execSelectSQL(qry, conn);
                        if (rs != null && rs.getRowCount() > 0) {
                            for (int i = 0; i < rs.getRowCount(); i++) {
                                checkClause.append(",").append(rs.getFieldValueString(i, 0).toString().replace("A_", "").replace("E_", ""));
//                                checkClause = checkClause + "," + rs.getFieldValueString(i, 0).toString().replace("A_", "").replace("E_", "");
                            }
                            for (int i = 0; i < totalViewIds.size(); i++) {
                                if (!checkClause.toString().contains(totalViewIds.get(i).toString().replace("A_", "").replace("E_", ""))) {
                                    tableFlag = false;
                                }
                            }
                            if (elementArrList != null) {
                                for (int i = 0; i < elementArrList.length; i++) {
                                    if (!checkClause.toString().contains(elementArrList[i].toString().replace("A_", "").replace("E_", ""))) {
                                        tableFlag = false;
                                    }
                                }
                            }
                            if (reportQryElementIds != null) {
                                PbReturnObject rs1 = null;
//                                String reportQryEleArr = "";
                                StringBuilder reportQryEleArr = new StringBuilder(400);
                                for (int i = 0; i < reportQryElementIds.size(); i++) {
                                    reportQryEleArr.append(",").append(reportQryElementIds.get(i).toString());
//                                    reportQryEleArr = reportQryEleArr + "," + reportQryElementIds.get(i).toString();
                                }
                                reportQryEleArr = new StringBuilder(reportQryEleArr.substring(1));
//                                reportQryEleArr = reportQryEleArr.substring(1);
                                String getBussColName = "select user_col_name,element_id from prg_user_all_info_details where element_id in (" + reportQryEleArr + ")";
                                rs1 = pbdb.execSelectSQL(getBussColName);
                                if (rs1 != null && rs1.getRowCount() > 0) {
                                    for (int i = 0; i < rs1.getRowCount(); i++) {
                                        String bussColName = rs1.getFieldValueString(i, 0).toString();
                                        String elementId = rs1.getFieldValueString(i, 1).toString();
                                        if (!checkClause.toString().contains(bussColName)) {
                                            if (!checkClause.toString().contains(elementId)) {
                                                tableFlag = false;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            tableFlag = true;
                        }
                    } catch (Exception ex) {
                        tableFlag = false;
                        logger.error("Exception:", ex);
                    }
                }
//                boolean isAOEnable = true;
//                String check = oWrapper_AO + " " + finalViewByCol_AO;
//                if (rowViewIds != null) {
//                    for (int i = 0; i < rowViewIds.size(); i++) {
//                        if (!check.contains(rowViewIds.get(i).toString())) {
//                            isAOEnable = false;
//                        }
//                    }
//                }
//                if (colViewIds != null) {
//                    for (int i = 0; i < colViewIds.size(); i++) {
//                        if (!check.contains(colViewIds.get(i).toString())) {
//                            isAOEnable = false;
//                        }
//                    }
//                }

                OViewByCol_AO = OViewByCol_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");
                osql_AO = osql_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");
                finalSql_AO = finalSql_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");
                OViewByCol_AO = OViewByCol_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");
                osqlGroup_AO = osqlGroup_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");
                osqlGroup_AO = osqlGroup_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");


                if (tableFlag) {

                    if (OViewByCol_AO.toLowerCase().trim().contains("time")) {
                        finalSql_AO = "select * from R_AO_" + reportId + " where 1=1 " + filterClause;
                    } else {
                        finalSql_AO = "select * from R_AO_" + reportId + " where 1=1 " + filterClause + "and TIME =" + stDateAO;
                    }
                    if (colViewIds != null && colViewIds.size() > 0) {
                        if (colViewIds != null && colViewIds.size() > 0) {
//                    finalSql_AO = " select " + OViewByCol_AO +" "+  osql_AO + " from ( " + finalSql_AO + " ) O5 group by " + OViewByCol_AO  + " " + osqlGroup_AO + " ";//
//                    finalSql_AO = " select " + OViewByCol_AO + " , " + OorderByCol_AO + osql_AO + " from ( " + finalSql_AO + " ) O5 group by " + OViewByCol_AO + " , " + OorderByCol_AO + " " + osqlGroup_AO + " ";//
                            finalSql_AO = " select " + OViewByCol_AO + " " + osql_AO + " from ( " + finalSql_AO + " ) O5 group by " + OViewByCol_AO + " " + osqlGroup_AO + " ";//

//                    finalSql_AO = " select " + OmViewByCol_AO  +" "+ omsql_AO + " from ( " + finalSql_AO + " ) O6  ";
//                    finalSql_AO = " select " + OmViewByCol_AO + " , " + OmorderByCol_AO + " , " + ColOrderByCol_AO + omsql_AO + " from ( " + finalSql_AO + " ) O6  ";
                            finalSql_AO = " select " + OmViewByCol_AO + " , " + OmorderByCol_AO + omsql_AO + " from ( " + finalSql_AO + " ) O6  ";
                        } else {
//                    finalSql_AO = " select " + OViewByCol_AO + " , " + OorderByCol_AO + osql_AO + " from ( " + finalSql_AO + " ) O7  group by " + OViewByCol_AO + " , " + OorderByCol_AO + " " + osqlGroup_AO + "  ";// //+ " order by " + OmorderByCol;
                            finalSql_AO = " select " + OViewByCol_AO + "  " + osql_AO + " from ( " + finalSql_AO + " ) O7  group by " + OViewByCol_AO + " " + osqlGroup_AO + "  ";// //+ " order by " + OmorderByCol;
//                    finalSql_AO = " select " + OViewByCol_AO  +" "+ osql_AO + " from ( " + finalSql_AO + " ) O7  group by " + OViewByCol_AO + " " + osqlGroup_AO + "  ";// //+ " order by " + OmorderByCol;

                            finalSql_AO = " select " + OmViewByCol_AO + " , " + OmorderByCol_AO + omsql_AO + " from ( " + finalSql_AO + " ) O7_1 ";
//                    finalSql_AO = " select " + OmViewByCol_AO +" " + omsql_AO + " from ( " + finalSql_AO + " ) O7_1 ";
                        }
                        if (!(colViewIds != null && colViewIds.size() > 0)) {
                            if (rowViewIds != null && rowViewIds.size() > 0) {
                                finalSql_AO = " select " + finalViewByCol_AO + oWrapper_AO + " from ( " + finalSql_AO + " ) OT1 order by  " + OmorderByCol_AO;
//                        finalSql_AO = " select " + finalViewByCol_AO + oWrapper_AO + " from ( " + finalSql_AO + " ) OT1 ";
                            }
                        }
                        finalSql_AO = finalSql_AO + " order by " + ColOrderByCol_AO;
                    } else {
                        if (colViewIds != null && colViewIds.size() > 0) {
                            finalSql_AO = " select " + OViewByCol_AO + " " + osql_AO + " from ( " + finalSql_AO + " ) O5 group by " + OViewByCol_AO + " " + osqlGroup_AO + " ";//
//                    finalSql_AO = " select " + OViewByCol_AO + " , " + OorderByCol_AO + osql_AO + " from ( " + finalSql_AO + " ) O5 group by " + OViewByCol_AO + " , " + OorderByCol_AO + " " + osqlGroup_AO + " ";//

                            finalSql_AO = " select " + OmViewByCol_AO + " " + omsql_AO + " from ( " + finalSql_AO + " ) O6  ";
//                    finalSql_AO = " select " + OmViewByCol_AO + " , " + OmorderByCol_AO + " , " + ColOrderByCol_AO + omsql_AO + " from ( " + finalSql_AO + " ) O6  ";
                        } else {
//                    finalSql_AO = " select " + OViewByCol_AO + " , " + OorderByCol_AO + osql_AO + " from ( " + finalSql_AO + " ) O7  group by " + OViewByCol_AO + " , " + OorderByCol_AO + " " + osqlGroup_AO + "  ";// //+ " order by " + OmorderByCol;
                            finalSql_AO = " select " + OViewByCol_AO + " " + osql_AO + " from ( " + finalSql_AO + " ) O7  group by " + OViewByCol_AO + " " + osqlGroup_AO + "  ";// //+ " order by " + OmorderByCol;

//                    finalSql_AO = " select " + OmViewByCol_AO + " , " + OmorderByCol_AO + omsql_AO + " from ( " + finalSql_AO + " ) O7_1 ";
                            finalSql_AO = " select " + OmViewByCol_AO + " " + omsql_AO + " from ( " + finalSql_AO + " ) O7_1 ";
                        }
                        if (!(colViewIds != null && colViewIds.size() > 0)) {
                            if (rowViewIds != null && rowViewIds.size() > 0) {
//                        finalSql_AO = " select " + finalViewByCol_AO + oWrapper_AO + " from ( " + finalSql_AO + " ) OT1 order by  " + OmorderByCol_AO;
                                finalSql_AO = " select " + finalViewByCol_AO + oWrapper_AO + " from ( " + finalSql_AO + " ) OT1 ";
                            }
                        }
                    }
                }
                conn = connectionparam.getConnection(reportQryElementIds.get(0).toString().replace("A_", ""));

                if (conn.toString().contains("sqlserver")) {
                    finalSql_AO = finalSql_AO.replace("nvl(", "COALESCE(");//temporary fix --Remove this code by defining variable for nvl at top
                    finalSql_AO = finalSql_AO.replace("Nvl(", "COALESCE(");//temporary fix
                    finalSql_AO = finalSql_AO.replace("NVL(", "COALESCE(");//temporary fix
                }
                if (conn.toString().contains("PostgreSQL")) {//
                    finalSql_AO = finalSql_AO.replace("nvl(", "COALESCE(");//temporary fix --Remove this code by defining variable for nvl at top
                    finalSql_AO = finalSql_AO.replace("Nvl(", "COALESCE(");//temporary fix
                    finalSql_AO = finalSql_AO.replace("NVL(", "COALESCE(");//temporary fix
                }
                if (conn.toString().toUpperCase().contains("MYSQL")) {
                    finalSql_AO = finalSql_AO.replace("nvl(", "ifNULL(");//temporary fix --Remove this code by defining variable for nvl at top
                    finalSql_AO = finalSql_AO.replace("Nvl(", "ifNULL(");//temporary fix
                    finalSql_AO = finalSql_AO.replace("NVL(", "ifNULL(");//temporary fix
                }

//                finalSql_AO = finalSql_AO.replace("O_TIME", "TIME");
//                finalSql_AO = finalSql_AO.replace("TIME", "O_TIME");

                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        logger.error("Exception:", ex);
                    }
                }

                return finalSql_AO;
            } else {
                return "noData";
            }

        } else {
            return "noData";
        }
    }

    public String getObjectQueryGO(String reportId, String filterClause, ArrayList<String> rowViewIds, ArrayList<String> colViewIds, String eleId, String measurefilter, String isoneview, String oneviewid, String regid, String fileLocation, String timeClause, String dimSecurityClause) {
        ReportObjectMeta roMeta = new ReportObjectMeta();
//        String finalSql = "";
        File datafile = new File(fileLocation + "/analyticalobject/R_GO_" + reportId + ".json");
        if (isoneview != null && isoneview.equalsIgnoreCase("true")) {
            datafile = new File(fileLocation + "/analyticaloneviewobject/oneview_" + oneviewid + "/R_GO_" + regid + ".json");
        }
        if (datafile.exists()) {
            FileReadWrite readWrite = new FileReadWrite();
            String metaString = readWrite.loadJSON(fileLocation + "/analyticalobject/R_GO_" + reportId + ".json");
            if (isoneview != null && isoneview.equalsIgnoreCase("true")) {
                metaString = readWrite.loadJSON(fileLocation + "/analyticaloneviewobject/oneview_" + oneviewid + "/R_GO_" + regid + ".json");
            }
            PbDb pbdb = new PbDb();
            Connection conn1 = null;
            PbReturnObject newGOObject = null;
            conn1 = ProgenConnection.getInstance().getConnectionForElement(eleId.toString().replace("A_", ""));
            Gson gson = new Gson();
            Type tarType = new TypeToken<ReportObjectMeta>() {
            }.getType();

            roMeta = gson.fromJson(metaString, tarType);
//            String query;
            String osql_AO = roMeta.getOsql_AO();
            String OViewByCol_AO = roMeta.getOViewByCol_AO();
//            String OorderByCol_AO = roMeta.getOorderByCol_AO();
            String finalSql_AO = "";
            String check_DDate = "";
            check_DDate = "select DDATE from R_GO_" + reportId + " where 1=2";
            if (isoneview != null && isoneview.equalsIgnoreCase("true")) {
                finalSql_AO = "select * from R_GO_" + oneviewid + "_" + regid + " where 1=1 " + filterClause + dimSecurityClause + timeClause;
            } else {
                finalSql_AO = "select * from R_GO_" + reportId + " where 1=1 " + filterClause + dimSecurityClause + timeClause;
            }
            try {
                newGOObject = pbdb.execSelectSQL(check_DDate, conn1);
                if (newGOObject == null) {
                    if (isoneview != null && isoneview.equalsIgnoreCase("true")) {
                        finalSql_AO = "select * from R_GO_" + oneviewid + "_" + regid + " where 1=1 " + filterClause;
                    } else {
                        finalSql_AO = "select * from R_GO_" + reportId + " where 1=1 " + filterClause + dimSecurityClause;
                    }
                }
            } catch (Exception e) {
            }
            String OmViewByCol_AO = roMeta.getOmViewByCol_AO();
//            String OmorderByCol_AO = roMeta.getOmorderByCol_AO();
//            String ColOrderByCol_AO = roMeta.getColOrderByCol_AO();
            String omsql_AO = roMeta.getOmsql_AO();
            String osqlGroup_AO = roMeta.getOsqlGroup_AO();
            String finalViewByCol_AO = roMeta.getFinalViewByCol_AO();
            String oWrapper_AO = roMeta.getoWrapper_AO();
            boolean isDimSegOnSumm = roMeta.getIsDimSegOnSumm();
            String finalSummarizedGrpBy_AO = roMeta.getFinalSummarizedGrpBy_AO();
            String finalSqlSummarized_AO = roMeta.getFinalSqlSummarized_AO();
            String OViewByColBuck_AO = roMeta.getOViewByCol_AO();
            String OViewByColBuckGrp_AO = roMeta.getOViewByColGrp_AO();

//            String filePath = roMeta.getFilePath();
            String filePath = fileLocation;
            OViewByCol_AO = "";
            OmViewByCol_AO = "";
            finalViewByCol_AO = "";
//            String ids = "";
            StringBuilder ids = new StringBuilder();
//            String isDimSeg = "false";
//            String grpByQry = "";
            StringBuilder grpByQry = new StringBuilder(1000);
            for (int i = 0; i < rowViewIds.size(); i++) {
                if (!rowViewIds.get(i).toString().equalsIgnoreCase("TIME")) {
//                    ids = ids + "," + rowViewIds.get(i).toString();
                    ids.append(",").append(rowViewIds.get(i).toString());
                }
            }
//            if (!ids.equalsIgnoreCase("")) {
            if (ids.length() > 0) {
//                ids = ids.substring(1);
                ids = new StringBuilder(ids.substring(1));
            }
            isDimSegOnSumm = false;

            String queryForMeasure = "select IS_SUMMARIZED_BUCKET,DIM_ID,ELEMENT_ID from prg_user_all_info_details where element_id in(" + ids + ")";
            try {

                if (!ids.toString().trim().equalsIgnoreCase("Time") && !ids.toString().trim().equalsIgnoreCase("")) {

                    PbReturnObject measureObj = pbdb.execSelectSQL(queryForMeasure);
                    if (measureObj != null && measureObj.getRowCount() > 0) {
                        for (int i = 0; i < measureObj.getRowCount(); i++) {
                            String dimId = measureObj.getFieldValueString(i, 1);
                            if (measureObj.getFieldValueString(i, 0).equalsIgnoreCase("Y")) {
                                isDimSegOnSumm = true;
                                SaveBucketsBD bd = new SaveBucketsBD();
                                String SummBucketFormula = bd.readFormulaFromFile(dimId, filePath);
//                                grpByQry = grpByQry + ",( " + SummBucketFormula + " )";
                                grpByQry.append(",( ").append(SummBucketFormula).append(" )");
                            } else {
                                grpByQry.append(",A_").append(measureObj.getFieldValueString(i, 2));
//                                grpByQry = grpByQry + ",A_" + measureObj.getFieldValueString(i, 2);
                            }
                        }
                    }
                } else {
                    grpByQry.append(",TIME");
//                    grpByQry = grpByQry + ",TIME";
                }

                grpByQry = new StringBuilder(grpByQry.substring(1));
//                grpByQry = grpByQry.substring(1);

                grpByQry = new StringBuilder(replaceUnwantedCharsOViewByCol(grpByQry.toString()));
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }

            for (int i = 0; i < rowViewIds.size(); i++) {
//                if(rowViewIds.get(i).toString().equalsIgnoreCase("TIME")){
                if (rowViewIds.get(i).toString().trim().equalsIgnoreCase("TIME")) {
                    OViewByCol_AO += ", " + rowViewIds.get(i).toString();
                    OmViewByCol_AO += ", " + rowViewIds.get(i).toString() + " AS " + rowViewIds.get(i).toString();
                    finalViewByCol_AO += ", " + rowViewIds.get(i).toString() + " AS " + rowViewIds.get(i).toString();
                } else {
                    OViewByCol_AO += ", " + "A_" + rowViewIds.get(i).toString();
                    OmViewByCol_AO += ", " + "A_" + rowViewIds.get(i).toString() + " AS " + "A_" + rowViewIds.get(i).toString();
                    finalViewByCol_AO += ", " + "A_" + rowViewIds.get(i).toString() + " AS " + "A_" + rowViewIds.get(i).toString();
                }

            }
            for (int i = 0; i < colViewIds.size(); i++) {
                if (colViewIds.get(i).toString().trim().equalsIgnoreCase("TIME")) {
                    OViewByCol_AO += ", " + colViewIds.get(i).toString();
                    OmViewByCol_AO += ", " + colViewIds.get(i).toString() + " AS " + colViewIds.get(i).toString();
                    finalViewByCol_AO += ", " + colViewIds.get(i).toString() + " AS " + colViewIds.get(i).toString();
                } else {
                    OViewByCol_AO += ", " + "A_" + colViewIds.get(i).toString();
                    OmViewByCol_AO += ", " + "A_" + colViewIds.get(i).toString() + " AS " + "A_" + colViewIds.get(i).toString();
                    finalViewByCol_AO += ", " + "A_" + colViewIds.get(i).toString() + " AS " + "A_" + colViewIds.get(i).toString();
                }

            }
            OViewByCol_AO = OViewByCol_AO.substring(1);
            OmViewByCol_AO = OmViewByCol_AO.substring(1);
            finalViewByCol_AO = finalViewByCol_AO.substring(1);

            boolean isAOEnable = true;
            String check = oWrapper_AO + " " + finalViewByCol_AO;
            if (rowViewIds != null) {
                for (int i = 0; i < rowViewIds.size(); i++) {
                    if (!check.contains(rowViewIds.get(i).toString())) {
                        isAOEnable = false;
                    }
                }
            }
            if (colViewIds != null) {
                for (int i = 0; i < colViewIds.size(); i++) {
                    if (!check.contains(colViewIds.get(i).toString())) {
                        isAOEnable = false;
                    }
                }
            }
            String OViewByColNew1 = "";
//            String OorderByColNew1 = "";
            if (isDimSegOnSumm) {
                OViewByColNew1 = OViewByColBuckGrp_AO;
                OViewByColNew1 = replaceUnwantedCharsOViewByCol(OViewByColBuckGrp_AO);
            }

//              OViewByCol_AO = OViewByCol_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");
//            osql_AO = osql_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");
//            finalSql_AO = finalSql_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");
//            OViewByCol_AO = OViewByCol_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");
//            osqlGroup_AO = osqlGroup_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");
//            osqlGroup_AO = osqlGroup_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");


            if (isAOEnable) {
                if (colViewIds != null && colViewIds.size() > 0) {
                    if (isDimSegOnSumm) {
                        finalSql_AO = " select " + finalSqlSummarized_AO + " FROM ( " + finalSql_AO + " ) SGP group by " + finalSummarizedGrpBy_AO + " ";
//                          finalSql_AO = " select " + OViewByColBuck_AO + " " + osql_AO + " from ( " + finalSql_AO + " ) O5 group by " + OViewByColNew1 + " " + osqlGroup_AO + " ";//
                        finalSql_AO = " select " + OViewByColBuck_AO + " " + osql_AO + " from ( " + finalSql_AO + " ) O5 group by " + grpByQry + " " + osqlGroup_AO + " ";//
                    } else {
                        finalSql_AO = " select " + OViewByCol_AO + " " + osql_AO + " from ( " + finalSql_AO + " ) O5 group by " + OViewByCol_AO + " " + osqlGroup_AO + " ";//
                    }
                    finalSql_AO = " select " + OmViewByCol_AO + " " + omsql_AO + " from ( " + finalSql_AO + " ) O6  ";
                } else {
                    if (isDimSegOnSumm) {
                        finalSql_AO = " select " + finalSqlSummarized_AO + "  FROM ( " + finalSql_AO + " ) SGP group by " + finalSummarizedGrpBy_AO + " ";
//                          finalSql_AO = " select " + OViewByColBuck_AO + " " + osql_AO + " from ( " + finalSql_AO + " ) O7  group by " + OViewByColNew1 + " " + osqlGroup_AO + "  "+measurefilter+ " ";// //+ " order by " + OmorderByCol;
                        finalSql_AO = " select " + OViewByColBuck_AO + " " + osql_AO + " from ( " + finalSql_AO + " ) O7  group by " + grpByQry + " " + osqlGroup_AO + "  " + measurefilter + " ";// //+ " order by " + OmorderByCol;
                    } else {
                        finalSql_AO = " select " + OViewByCol_AO + " " + osql_AO + " from ( " + finalSql_AO + " ) O7  group by " + OViewByCol_AO + " " + osqlGroup_AO + "  " + measurefilter + " ";// //+ " order by " + OmorderByCol;
                    }
                    finalSql_AO = " select " + OmViewByCol_AO + " " + omsql_AO + " from ( " + finalSql_AO + " ) O7_1 ";
                }
                if (!(colViewIds != null && colViewIds.size() > 0)) {
                    if (rowViewIds != null && rowViewIds.size() > 0) {
                        finalSql_AO = " select " + finalViewByCol_AO + oWrapper_AO + " from ( " + finalSql_AO + " ) OT1 ";
                    }
                }
            }
            Connection conn = null;
            ProgenParam connectionparam = new ProgenParam();

            conn = connectionparam.getConnection(eleId.toString().replace("A_", ""));

            if (conn.toString().contains("sqlserver")) {
                finalSql_AO = finalSql_AO.replace("nvl(", "COALESCE(");//temporary fix --Remove this code by defining variable for nvl at top
                finalSql_AO = finalSql_AO.replace("Nvl(", "COALESCE(");//temporary fix
                finalSql_AO = finalSql_AO.replace("NVL(", "COALESCE(");//temporary fix
            }
            if (conn.toString().contains("PostgreSQL")) {//
                finalSql_AO = finalSql_AO.replace("nvl(", "COALESCE(");//temporary fix --Remove this code by defining variable for nvl at top
                finalSql_AO = finalSql_AO.replace("Nvl(", "COALESCE(");//temporary fix
                finalSql_AO = finalSql_AO.replace("NVL(", "COALESCE(");//temporary fix
            }
            if (conn.toString().contains("mysql")) {
                finalSql_AO = finalSql_AO.replace("nvl(", "ifNULL(");//temporary fix --Remove this code by defining variable for nvl at top
                finalSql_AO = finalSql_AO.replace("Nvl(", "ifNULL(");//temporary fix
                finalSql_AO = finalSql_AO.replace("NVL(", "ifNULL(");//temporary fix
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
            }

            return finalSql_AO;
        } else {
            return "noData";
        }
    }
public String getFilterClauseNotin(HashMap<String, List> inMap, HashMap<String, List> notInMap, HashMap<String, List> likeMap, HashMap<String, List> notLikeMap) {
        String selfParamWhereClause = "";
        String elementList = null;

        if (notInMap != null && notInMap.size() > 0) {
            String eleIds[] = (String[]) notInMap.keySet().toArray(new String[0]);
            for (int m = 0; m < eleIds.length; m++) {
//                if (!eleIds[m].equalsIgnoreCase("Time")) {
                if (ValidateParamValues(notInMap.get(eleIds[m]))) {
//                        if (!(elementList.contains(notInMap.get(eleIds[m]).toString()))) {
//                            elementList += "," + notInMap.get(eleIds[m]).toString();
//                        }
                    if (elementList == null) {
                        elementList = (eleIds[m]).toString();
                    } else {
                        elementList += "," + (eleIds[m]).toString();
                    }
                }
//            }
            }
        }
        if (elementList != null && !elementList.equalsIgnoreCase("")) {
//        String sqlstr = "select distinct BUSS_TABLE_ID,BUSS_TABLE_NAME,BUSS_COL_NAME,USER_COL_NAME,denom_query,ELEMENT_ID,"
//                + "REF_ELEMENT_ID, REF_ELEMENT_TYPE , ACTUAL_COL_FORMULA, USER_COL_DESC,USER_COL_TYPE,member_name , DIM_ID,  "
//                + "DIM_TAB_ID,  DIM_NAME,   MEMBER_ID from prg_user_all_info_details  where ELEMENT_ID in (" + elementList + ")  order by BUSS_TABLE_ID,"
//                + "REF_ELEMENT_ID, REF_ELEMENT_TYPE ";
//        PbDb pbdb = new PbDb();
//        PbReturnObject retObj;
            elementArrList = elementList.split(",");

//                retObj = pbdb.execSelectSQL(sqlstr);

            String paramCond = "";
            String parameterAndOr = " and ";

//            for (int i = 0; i < retObj.rowCount; i++) {
            for (int i = 0; i < elementArrList.length; i++) {
//                String userColType = retObj.getFieldValueString(i,10);
               
                if (notInMap != null && notInMap.size() > 0 && notInMap.get(elementArrList[i]) != null) {
                    {

                        if (ValidateParamValues(notInMap.get(elementArrList[i]))) {
                            String val = notInMap.get(elementArrList[i]).toString();
                            if (val != null && !val.equalsIgnoreCase("[]")) {
                                paramCond = " not in ";
//                        selfParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, 1) + "." + retObj.getFieldValueString(i, 2);
//                        selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(retObj.getFieldValueString(i, 5))) + ") ";
                                if (elementArrList[i].toString().equalsIgnoreCase("Time")) {
                                    selfParamWhereClause += parameterAndOr + " " + elementArrList[i];
                                } else {
                                    selfParamWhereClause += parameterAndOr + " A_" + elementArrList[i];
                                }
                                selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(elementArrList[i])) + ") ";
                            }
                        }
                    }
                }
              
            }


        }
        return selfParamWhereClause;

    }
    public String getFilterClause(HashMap<String, List> inMap, HashMap<String, List> notInMap, HashMap<String, List> likeMap, HashMap<String, List> notLikeMap) {
        String selfParamWhereClause = "";
        String elementList = null;
        if (inMap != null && inMap.size() > 0) {
            String eleIds[] = (String[]) inMap.keySet().toArray(new String[0]);
            for (int m = 0; m < eleIds.length; m++) {
//                if (!eleIds[m].equalsIgnoreCase("Time")) {
                if (ValidateParamValues(inMap.get(eleIds[m]))) {
//                        if (!(elementList.contains(inMap.get(eleIds[m]).toString()))) {
//                            elementList += "," + inMap.get(eleIds[m]).toString();
//                        }
                    if (elementList == null) {
                        elementList = (eleIds[m]).toString();
                    } else {
                        elementList += "," + (eleIds[m]).toString();
                    }
                }
//                }
            }
        }
        if (likeMap != null && likeMap.size() > 0) {
            String eleIds[] = (String[]) likeMap.keySet().toArray(new String[0]);
            for (int m = 0; m < eleIds.length; m++) {
//                if (!eleIds[m].equalsIgnoreCase("Time")) {
                if (ValidateParamValues(likeMap.get(eleIds[m]))) {
//                        if (!(elementList.contains(likeMap.get(eleIds[m]).toString()))) {
//                            elementList += "," + likeMap.get(eleIds[m]).toString();
//                        }
                    if (elementList == null) {
                        elementList = (eleIds[m]).toString();
                    } else {
                        elementList += "," + (eleIds[m]).toString();
                    }
                }
//                }
            }
        }
        if (notLikeMap != null && notLikeMap.size() > 0) {
            String eleIds[] = (String[]) notLikeMap.keySet().toArray(new String[0]);
            for (int m = 0; m < eleIds.length; m++) {
//                if (!eleIds[m].equalsIgnoreCase("Time")) {
                if (ValidateParamValues(notLikeMap.get(eleIds[m]))) {
//                        if (!(elementList.contains(notLikeMap.get(eleIds[m]).toString()))) {
//                            elementList += "," + notLikeMap.get(eleIds[m]).toString();
//                        }
                    if (elementList == null) {
                        elementList = (eleIds[m]).toString();
                    } else {
                        elementList += "," + (eleIds[m]).toString();
                    }
                }
//                }
            }
        }
        if (notInMap != null && notInMap.size() > 0) {
            String eleIds[] = (String[]) notInMap.keySet().toArray(new String[0]);
            for (int m = 0; m < eleIds.length; m++) {
//                if (!eleIds[m].equalsIgnoreCase("Time")) {
                if (ValidateParamValues(notInMap.get(eleIds[m]))) {
//                        if (!(elementList.contains(notInMap.get(eleIds[m]).toString()))) {
//                            elementList += "," + notInMap.get(eleIds[m]).toString();
//                        }
                    if (elementList == null) {
                        elementList = (eleIds[m]).toString();
                    } else {
                        elementList += "," + (eleIds[m]).toString();
                    }
                }
//            }
            }
        }
        if (elementList != null && !elementList.equalsIgnoreCase("")) {
//        String sqlstr = "select distinct BUSS_TABLE_ID,BUSS_TABLE_NAME,BUSS_COL_NAME,USER_COL_NAME,denom_query,ELEMENT_ID,"
//                + "REF_ELEMENT_ID, REF_ELEMENT_TYPE , ACTUAL_COL_FORMULA, USER_COL_DESC,USER_COL_TYPE,member_name , DIM_ID,  "
//                + "DIM_TAB_ID,  DIM_NAME,   MEMBER_ID from prg_user_all_info_details  where ELEMENT_ID in (" + elementList + ")  order by BUSS_TABLE_ID,"
//                + "REF_ELEMENT_ID, REF_ELEMENT_TYPE ";
//        PbDb pbdb = new PbDb();
//        PbReturnObject retObj;
            elementArrList = elementList.split(",");

//                retObj = pbdb.execSelectSQL(sqlstr);

            String paramCond = "";
            String parameterAndOr = " and ";

//            for (int i = 0; i < retObj.rowCount; i++) {
            for (int i = 0; i < elementArrList.length; i++) {
//                String userColType = retObj.getFieldValueString(i,10);
                if (inMap != null && inMap.size() > 0 && inMap.get(elementArrList[i]) != null) {
                    {
                        boolean flag = false;
                        for(int m=0;m<inMap.get(elementArrList[i]).size();m++){
                           if(inMap.get(elementArrList[i]).get(m)==null){
                           flag=true;
                               break;
                        } 
                           
                        }
                        if(flag){
                            continue;
                        }
                        if (ValidateParamValues(inMap.get(elementArrList[i]))) {
                            String val = inMap.get(elementArrList[i]).toString();
                            if (val != null && !val.equalsIgnoreCase("[]")) {
                                paramCond = " in ";
                                if (elementArrList[i].toString().equalsIgnoreCase("Time") || elementArrList[i].toString().equalsIgnoreCase("MONTH_YEAR")
                                        || elementArrList[i].toString().equalsIgnoreCase("QTR_YEAR")
                                        || elementArrList[i].toString().equalsIgnoreCase("YEAR_NAME")) {
                                    selfParamWhereClause += parameterAndOr + " " + elementArrList[i];
                                } else {
                                    selfParamWhereClause += parameterAndOr + " A_" + elementArrList[i];
                                }
//                        selfParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, 1) + "." + retObj.getFieldValueString(i, 2);
//                        selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(inMap.get(retObj.getFieldValueString(i, 5))) + ") ";

                                selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(inMap.get(elementArrList[i])) + ") ";
                            }
                        }
                    }
                }
                if (notInMap != null && notInMap.size() > 0 && notInMap.get(elementArrList[i]) != null) {
                    {

                        if (ValidateParamValues(notInMap.get(elementArrList[i]))) {
                            String val = notInMap.get(elementArrList[i]).toString();
                            if (val != null && !val.equalsIgnoreCase("[]")) {
                                paramCond = " in ";
//                        selfParamWhereClause += parameterAndOr + retObj.getFieldValueString(i, 1) + "." + retObj.getFieldValueString(i, 2);
//                        selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(retObj.getFieldValueString(i, 5))) + ") ";
                                if (elementArrList[i].toString().equalsIgnoreCase("Time")) {
                                    selfParamWhereClause += parameterAndOr + " " + elementArrList[i];
                                } else {
                                    selfParamWhereClause += parameterAndOr + " A_" + elementArrList[i];
                                }
                                selfParamWhereClause += " " + paramCond + " (" + getOracleInClause(notInMap.get(elementArrList[i])) + ") ";
                            }
                        }
                    }
                }
                if (likeMap != null && likeMap.size() > 0 && likeMap.get(elementArrList[i]) != null) {
                    {
                        if (ValidateParamValues(likeMap.get(elementArrList[i]))) {
                            String val = likeMap.get(elementArrList[i]).toString();
                            if (val != null && !val.equalsIgnoreCase("[]")) {
                                paramCond = " like ";
                                if (elementArrList[i].toString().equalsIgnoreCase("Time")) {
                                    selfParamWhereClause += parameterAndOr + " ( " + elementArrList[i];
                                } else {
                                    selfParamWhereClause += parameterAndOr + " ( A_" + elementArrList[i];
                                }
//                        selfParamWhereClause += parameterAndOr+" ( "  + retObj.getFieldValueString(i, 1) + "." + retObj.getFieldValueString(i, 2);
//                        selfParamWhereClause += " "+paramCond+" (" + getOracleLikeNotlikeClause("like", retObj.getFieldValueString(i, 1) + "." + retObj.getFieldValueString(i, 2),likeMap.get(retObj.getFieldValueString(i, 5))) + ") ) ";
                                selfParamWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("like", elementArrList[i], likeMap.get(elementArrList[i])) + ") ) ";
                            }
                        }
                    }
                }

                if (notLikeMap != null && notLikeMap.size() > 0 && notLikeMap.get(elementArrList[i]) != null) {
                    {
                        if (ValidateParamValues(notLikeMap.get(elementArrList[i]))) {
                            String val = notLikeMap.get(elementArrList[i]).toString();
                            if (val != null && !val.equalsIgnoreCase("[]")) {
                                paramCond = " not like ";
                                if (elementArrList[i].toString().equalsIgnoreCase("Time")) {
                                    selfParamWhereClause += parameterAndOr + " ( " + elementArrList[i];
                                } else {
                                    selfParamWhereClause += parameterAndOr + " ( A_" + elementArrList[i];
                                }

//                        selfParamWhereClause += parameterAndOr+" ( "  + retObj.getFieldValueString(i, 1) + "." + retObj.getFieldValueString(i, 2);
//                        selfParamWhereClause += " "+paramCond+" (" + getOracleLikeNotlikeClause("not like", retObj.getFieldValueString(i, 1) + "." + retObj.getFieldValueString(i, 2),notLikeMap.get(retObj.getFieldValueString(i, 5))) + ") ) ";

                                selfParamWhereClause += " " + paramCond + " (" + getOracleLikeNotlikeClause("not like", elementArrList[i], notLikeMap.get(elementArrList[i])) + ") ) ";
                            }
                        }
                    }
                }
            }


        }
        return selfParamWhereClause;

    }

    public boolean ValidateParamValues(Object o) {
        boolean isValid = true;
        if (o instanceof List) {
            if (o == null || ((List<String>) o).contains("All") || ((List<String>) o).contains("ALL")
                    || ((List<String>) o).isEmpty()) {
                isValid = false;
            }
        } else {
            if (o == null || o.toString().equalsIgnoreCase("All") || o.toString().equalsIgnoreCase("null")
                    || o.toString().equalsIgnoreCase("")) {
                isValid = false;
            }
        }
        return (isValid);
    }

    String getOracleInClause(Object o) {

        if (o instanceof List) {
            StringBuilder result = new StringBuilder();
            for (Object obj : (List) o) {
                if(obj==null){
                    
                }else{
                result.append(",'").append(obj.toString()).append("'");
            }
            }
            return (result.length() > 0 ? result.toString().substring(1) : "");
        } else {
            return ("'" + o.toString().replace(",", "','") + "'");
        }

    }

    String getOracleLikeNotlikeClause(String isLike, String tableColumn, Object o) {
        if (o instanceof List) {
            int i = 0;
            StringBuilder result = new StringBuilder();
            for (Object obj : (List) o) {
                if (i == 0) {
                    result.append("  ").append("'").append(obj.toString()).append("'");
                } else {
                    result.append(") OR ").append(tableColumn).append(" ").append(isLike).append(" ( ").append("'").append(obj.toString()).append("'");
                }

                i++;
            }
            return (result.length() > 0 ? result.toString().substring(1) : "");
        } else {
            return ("'" + o.toString().replace(",", "','") + "'");
        }

    }

    String getOracleInClauseTarget(Object o) {
        if (o instanceof List) {
            StringBuilder result = new StringBuilder();
            for (Object obj : (List) o) {
                result.append(",'").append(obj.toString()).append("'");
            }
            return (result.length() > 0 ? result.toString().substring(1) : "");
        } else {
            return ("'" + o.toString().replace(",", "','") + "'");
        }
    }

    public String replaceUnwantedCharsOViewByCol(String OViewByColNew1) {
        OViewByColNew1 = OViewByColNew1.replace("SUM(", "(").replace("AVG(", "(").replace("MIN(", "(").replace("MAX(", "(").replace("COUNT(", "(").replace("COUNTDISTINCT(", "(");
        OViewByColNew1 = OViewByColNew1.replace("AS VIEWBY1", "").replace("AS VIEWBY2", "").replace("AS VIEWBY3", "").replace("AS VIEWBY4", "").replace("AS VIEWBY5", "").replace("AS VIEWBY6", "");
        OViewByColNew1 = OViewByColNew1.replace("AS ORDER1", "").replace("AS ORDER2", "").replace("AS ORDER3", "").replace("AS ORDER4", "").replace("AS ORDER5", "").replace("AS ORDER6", "");
        return OViewByColNew1;
    }

    public String getCalenderTime(Container container, String element_Id) {
//        PbTimeRanges pbTime = new PbTimeRanges();
        String timeClause = "";
//        if (container != null) {
//            ArrayList timeDetails = container.getTimeDetailsArray();
//            String PRG_PERIOD_TYPE = "";
//            String PRG_COMPARE = "";
//            String date = "";
//            if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")) {
//                PRG_PERIOD_TYPE = timeDetails.get(3).toString();
//                PRG_COMPARE = timeDetails.get(4).toString();
//                date = timeDetails.get(2).toString();
//                pbTime.elementID = element_Id;
//                try {
//                    pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
//                } catch (Exception e) {
//                }
//            } else if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
//                pbTime.elementID = element_Id;
//                try {
//                    pbTime.setRange(timeDetails);
//                } catch (Exception e) {
//                }
//            }
//            timeClause = "and DDATE" + pbTime.d_clu;
//        }
        return timeClause;
    }

    public String getOneviewCalTime(ArrayList Timedetails, String element_Id) {
        PbTimeRanges pbTime = new PbTimeRanges();
        String timeClause = "";
        if (!Timedetails.isEmpty()) {
            ArrayList timeDetails = Timedetails;
            String PRG_PERIOD_TYPE = "";
            String PRG_COMPARE = "";
            String date = "";
            if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                PRG_PERIOD_TYPE = timeDetails.get(3).toString();
                PRG_COMPARE = timeDetails.get(4).toString();
                date = timeDetails.get(2).toString();
                pbTime.elementID = element_Id;
                try {
                    pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                } catch (Exception e) {
                }
            } else if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                pbTime.elementID = element_Id;
                try {
                    pbTime.setRange(timeDetails);
                } catch (Exception e) {
                }
            }
            timeClause = "and DDATE" + pbTime.d_clu;
        }
        return timeClause;
    }

    public String getCompareClause(Container container, String element_Id, String timeType) {
        PbTimeRanges pbTime = new PbTimeRanges();
        String timeClause = "";
        String timeClause1 = "";
        if (container != null) {

            ArrayList timeDetails = container.getTimeDetailsArray();
            String PRG_PERIOD_TYPE = "";
            String PRG_COMPARE = "";
            String date = "";
            if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD") && !container.isChangeFlag()) {
                if (timeType != null && (timeType.equalsIgnoreCase("MOM") || timeType.equalsIgnoreCase("MTD"))) {
                    PRG_PERIOD_TYPE = "Month";
                    PRG_COMPARE = "Last Month";
                    date = timeDetails.get(2).toString();
                    pbTime.elementID = element_Id;
                    try {
                        pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                    } catch (Exception e) {
                    }
                    timeClause = "and DDATE " + pbTime.d_clu;
                } else if (timeType != null && (timeType.equalsIgnoreCase("PMOM") || timeType.equalsIgnoreCase("PMTD"))) {
                    PRG_PERIOD_TYPE = "Month";
                    PRG_COMPARE = "Last Month";
                    date = timeDetails.get(2).toString();
                    pbTime.elementID = element_Id;
                    try {
                        pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                    } catch (Exception e) {
                    }
                    timeClause = "and DDATE " + pbTime.pd_clu;
                } else if (timeType != null && (timeType.equalsIgnoreCase("PQOQ") || timeType.equalsIgnoreCase("PQTD"))) {
                    PRG_PERIOD_TYPE = "Qtr";
                    PRG_COMPARE = "Last Qtr";
                    date = timeDetails.get(2).toString();
                    pbTime.elementID = element_Id;
                    try {
                        pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                    } catch (Exception e) {
                    }
                    timeClause = "and DDATE " + pbTime.pd_clu;
                } else if (timeType != null && (timeType.equalsIgnoreCase("QOQ") || timeType.equalsIgnoreCase("QTD"))) {
                    PRG_PERIOD_TYPE = "Qtr";
                    PRG_COMPARE = "Last Qtr";
                    date = timeDetails.get(2).toString();
                    pbTime.elementID = element_Id;
                    try {
                        pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                    } catch (Exception e) {
                    }
                    timeClause = "and DDATE " + pbTime.d_clu;
                } else if (timeType != null && (timeType.equalsIgnoreCase("MOY") || timeType.equalsIgnoreCase("PYMTD"))) {
                    PRG_PERIOD_TYPE = "Month";
                    PRG_COMPARE = "Same Month Last Year";
                    date = timeDetails.get(2).toString();
                    pbTime.elementID = element_Id;
                    try {
                        pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                    } catch (Exception e) {
                    }
                    timeClause = "and DDATE " + pbTime.pd_clu;
                } else if (timeType != null && (timeType.equalsIgnoreCase("QOY") || timeType.equalsIgnoreCase("PYQTD"))) {
                    PRG_PERIOD_TYPE = "Qtr";
                    PRG_COMPARE = "Same Qtr Last Year";
                    date = timeDetails.get(2).toString();
                    pbTime.elementID = element_Id;
                    try {
                        pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                    } catch (Exception e) {
                    }
                    timeClause = "and DDATE " + pbTime.pd_clu;
                } else if (timeType != null && (timeType.equalsIgnoreCase("YOY") || timeType.equalsIgnoreCase("YTD"))) {
                    PRG_PERIOD_TYPE = "Year";
                    PRG_COMPARE = "Last Year";
                    date = timeDetails.get(2).toString();
                    pbTime.elementID = element_Id;
                    try {
                        pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                    } catch (Exception e) {
                    }
                    timeClause = "and DDATE " + pbTime.d_clu;
                } else if (timeType != null && (timeType.equalsIgnoreCase("PYOY") || timeType.equalsIgnoreCase("PYTD"))) {
                    PRG_PERIOD_TYPE = "Year";
                    PRG_COMPARE = "Last Year";
                    date = timeDetails.get(2).toString();
                    pbTime.elementID = element_Id;
                    try {
                        pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                    } catch (Exception e) {
                    }
                    timeClause = "and DDATE " + pbTime.pd_clu;
                }
            } /**
             * Compare Clause For Change Begin *
             */
            else if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD") && container.isChangeFlag()) {

                if (timeType != null && (timeType.equalsIgnoreCase("MOM") || timeType.equalsIgnoreCase("MOMPer"))) {
                    PRG_PERIOD_TYPE = "Month";
                    PRG_COMPARE = "Last Month";
                    date = timeDetails.get(2).toString();
                    pbTime.elementID = element_Id;
                    try {
                        pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                    } catch (Exception e) {
                    }
                    timeClause = "and DDATE " + pbTime.d_clu;
                    timeClause1 = "and DDATE " + pbTime.pd_clu;
                } else if (timeType != null && (timeType.equalsIgnoreCase("MOYM") || timeType.equalsIgnoreCase("MOYMPer"))) {
                    PRG_PERIOD_TYPE = "Month";
                    PRG_COMPARE = "Same Month Last Year";
                    date = timeDetails.get(2).toString();
                    pbTime.elementID = element_Id;
                    try {
                        pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                    } catch (Exception e) {
                    }
                    timeClause = "and DDATE " + pbTime.d_clu;
                    timeClause1 = "and DDATE " + pbTime.pd_clu;

                } else if (timeType != null && (timeType.equalsIgnoreCase("QOQ") || timeType.equalsIgnoreCase("QOQPer"))) {
                    PRG_PERIOD_TYPE = "Qtr";
                    PRG_COMPARE = "Last Qtr";
                    date = timeDetails.get(2).toString();
                    pbTime.elementID = element_Id;
                    try {
                        pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                    } catch (Exception e) {
                    }
                    timeClause = "and DDATE " + pbTime.d_clu;
                    timeClause1 = "and DDATE " + pbTime.pd_clu;

                } else if (timeType != null && (timeType.equalsIgnoreCase("QOYQ") || timeType.equalsIgnoreCase("QOYQPer"))) {
                    PRG_PERIOD_TYPE = "Qtr";
                    PRG_COMPARE = "Same Qtr Last Year";
                    date = timeDetails.get(2).toString();
                    pbTime.elementID = element_Id;
                    try {
                        pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                    } catch (Exception e) {
                    }
                    timeClause = "and DDATE " + pbTime.d_clu;
                    timeClause1 = "and DDATE " + pbTime.pd_clu;

                } else if (timeType != null && (timeType.equalsIgnoreCase("YOY") || timeType.equalsIgnoreCase("YOYPer"))) {
                    PRG_PERIOD_TYPE = "Year";
                    PRG_COMPARE = "Last Year";
                    date = timeDetails.get(2).toString();
                    pbTime.elementID = element_Id;
                    try {
                        pbTime.setRange(PRG_PERIOD_TYPE, PRG_COMPARE, date);
                    } catch (Exception e) {
                    }
                    timeClause = "and DDATE " + pbTime.d_clu;
                    timeClause1 = "and DDATE " + pbTime.pd_clu;

                }

            }

        }
        if (container.isChangeFlag()) {

            return timeClause + " :: " + timeClause1;
        } else {
            return timeClause;
        }
    }

    public String getDimSecClause(List<String> viewbysIds, String userId, HashMap<String, List> mapSetForGraphs)  {
        String query = "";
//        String viewIds = "";
        StringBuilder viewIds = new StringBuilder(300);
        String id = "";
        String secClause = "";
        for (int i = 0; i < viewbysIds.size(); i++) {
            if (!viewbysIds.get(i).toString().replace("[", "").replace("]", "").trim().equalsIgnoreCase("TIME")) {
//                if (viewIds.equalsIgnoreCase("")) {
                if (viewIds.length() == 0) {
//                    viewIds += viewbysIds.get(i).replace("A_", "").toString();
                    viewIds.append(viewbysIds.get(i).replace("A_", "").toString());
                } else {
                    viewIds.append(",").append(viewbysIds.get(i).replace("A_", "").toString());
//                    viewIds += "," + viewbysIds.get(i).replace("A_", "").toString();
                }
            }
        }
        PbDb pbdb = new PbDb();
        query = "select A.*, b.* from PRG_SEC_GRP_ROLE_USER_VAR a, PRG_USER_ALL_ADIM_KEY_VAL_ELE b where a.buss_col_id in ( select key_buss_col_id from PRG_USER_ALL_ADIM_KEY_VAL_ELE where key_element_id in (&) )   and a.buss_col_id =b.key_buss_col_id  and b.key_folder_id = (select distinct key_folder_id from PRG_USER_ALL_ADIM_KEY_VAL_ELE where key_element_id in (&))";
        Object o[] = new Object[2];
        o[0] = viewIds;
        o[1] = viewIds;
        query = pbdb.buildQuery(query, o);
        PbReturnObject retObj = new PbReturnObject();
        try {
            retObj = pbdb.execSelectSQL(query);
        } catch (Exception e) {
        }
        String gblSetupQuery  = " select SETUP_KEY,SETUP_CHAR_VALUE from prg_gbl_setup_values where SETUP_ID in(46,47,48) ";
        PbReturnObject retObjGbl = null;
        try {
            retObjGbl = pbdb.execSelectSQL(gblSetupQuery);
        } catch (SQLException ex) {
             logger.error("Exception:", ex);
        }
        String securityElementId = "";
        String securityElementName = "";
        String securityMapFlag="";
        String inClause =" 1=1";
        if(retObjGbl!=null && retObjGbl.rowCount>0){
            securityElementId = retObjGbl.getFieldValueString(0, "SETUP_CHAR_VALUE");
            securityElementName = retObjGbl.getFieldValueString(1, "SETUP_CHAR_VALUE");
            securityMapFlag = retObjGbl.getFieldValueString(2, "SETUP_CHAR_VALUE");
        }
        if(securityMapFlag.equalsIgnoreCase("y")){
        if(mapSetForGraphs!=null && !mapSetForGraphs.isEmpty())
        {
            Set<Entry<String, List>> set = mapSetForGraphs.entrySet();
          for (Entry<String, List> entry : set) {
            
              if(securityElementId.equalsIgnoreCase(entry.getKey())){
                  if(entry.getValue()!=null && entry.getValue().size()>0)
               inClause = securityElementName + " in ( " + ArrayToCommaString(entry.getValue()) +" )";
              }
          }
        }
        }
        if (retObj != null && retObj.getRowCount() > 0) {
            id = "A_" + retObj.getFieldValueString(0, "KEY_ELEMENT_ID");
            secClause = " AND " + id + " IN ( select distinct COMPANY_ID from PRG_SEC_USER_COMPANY  where PROGEN_USER_ID = " + userId + "  AND "+ inClause +" ) ";
//            secClause = " AND 1 IN ( select distinct COMPANY_ID from PRG_SEC_USER_COMPANY  where PROGEN_USER_ID = " + userId + "  AND "+ inClause +" ) ";
        }
        return secClause;

        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public String getYearDrill(String getYear, XtendReportMeta reportMeta, String connID) {
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String result = null;
        Connection conn = null;
        String[] colNames;
        String finalQuery = "";
        PbDb db = new PbDb();
        String connectionType = ProgenConnection.getInstance().getDatabaseType();
        getYear = getYear.replace("]", "").replace("[", "").replace("\"", "").toUpperCase();
//        getConnectionType gettypeofconn = new getConnectionType();
//        String connType = gettypeofconn.getConTypeByElementId(elementId);
        if (ProgenConnection.SQL_SERVER.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select convert(varchar,cy_end_date,110) ed_date from pr_day_denom where upper(CY_CUST_NAME) = '" + getYear + "' ";

        } else if (ProgenConnection.MYSQL.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select date_format(cy_end_date,'%m/%d/%Y') ed_date from pr_day_denom where upper(CY_CUST_NAME) = '" + getYear + "' ";
        } else {
            finalQuery = " Select to_char(cy_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where upper(CY_CUST_NAME) = '" + getYear + "' ";

        }
        try {


            conn = ProgenConnection.getInstance().getConnectionForElement(connID);
            retObj = db.execSelectSQL(finalQuery, conn);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (retObj != null) {
            colNames = retObj.getColumnNames();
            int psize = retObj.getRowCount();

            if (psize > 0) {
                result = retObj.getFieldValueString(0, colNames[0]);
            }

        }

        return result;
    }

    public String getQtrDrill(String getYear, XtendReportMeta reportMeta, String connID) {
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String result = null;
        String[] colNames;
        Connection conn = null;
        String finalQuery = "";
        PbDb db = new PbDb();
        String connectionType = ProgenConnection.getInstance().getDatabaseType();
        getYear = getYear.replace("]", "").replace("[", "").replace("\"", "").toUpperCase();
//        getConnectionType gettypeofconn = new getConnectionType();
//        String connType = gettypeofconn.getConTypeByElementId(elementId);
        if (ProgenConnection.SQL_SERVER.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select convert(varchar,cq_end_date,110) ed_date from pr_day_denom where upper(CQ_CUST_NAME)= '" + getYear + "' ";

        } else if (ProgenConnection.MYSQL.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select date_format(cq_end_date,'%m/%d/%Y') ed_date from pr_day_denom where upper(CQ_CUST_NAME) = '" + getYear + "' ";
        } else {
            finalQuery = " Select to_char(cq_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where upper(CQ_CUST_NAME) = '" + getYear + "' ";

        }

        //String finalQuery = " Select to_char(cq_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where CQ_CUST_NAME = '" + getYear + "' ";
        try {


            conn = ProgenConnection.getInstance().getConnectionForElement(connID);
            retObj = db.execSelectSQL(finalQuery, conn);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (retObj != null) {
            colNames = retObj.getColumnNames();
            int psize = retObj.getRowCount();

            if (psize > 0) {
                result = retObj.getFieldValueString(0, colNames[0]);
            }
        }


        return result;
    }

    public String getMonthDrill(String getYear, XtendReportMeta reportMeta, String connID) {
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String result = null;
        String[] colNames = null;
        Connection conn = null;
        String finalQuery = "";
        PbDb db = new PbDb();
        String connectionType = ProgenConnection.getInstance().getDatabaseType();
        int psize = 0;
        getYear = getYear.replace("]", "").replace("[", "").replace("\"", "").toUpperCase();


        if (ProgenConnection.SQL_SERVER.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select convert(varchar,cm_end_date,110) ed_date from pr_day_denom where upper(CM_CUST_NAME) = '" + getYear + "' ";

        } else if (ProgenConnection.MYSQL.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select date_format(cm_end_date,'%m/%d/%Y') ed_date from pr_day_denom where upper(CM_CUST_NAME) = '" + getYear + "' ";
        } else {
            finalQuery = " Select to_char(cm_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where upper(CM_CUST_NAME) = '" + getYear + "' ";

        }
        //String finalQuery = " Select to_char(cm_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where CM_CUST_NAME = '" + getYear + "' ";
//        
        try {


            conn = ProgenConnection.getInstance().getConnectionForElement(connID);
            retObj = db.execSelectSQL(finalQuery, conn);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        //Modified By Mayank
        if (retObj != null) {
            colNames = retObj.getColumnNames();

            psize = retObj.getRowCount();
            result = retObj.getFieldValueString(0, colNames[0]);
        }



        return result;
    }

    
      public String getYearDrill(String getYear, ManagementTemplateMeta reportMeta, String connID) {
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String result = null;
        Connection conn = null;
        String[] colNames;
        String finalQuery = "";
        PbDb db = new PbDb();
        String connectionType = ProgenConnection.getInstance().getDatabaseType();
        getYear = getYear.replace("]", "").replace("[", "").replace("\"", "").toUpperCase();
//        getConnectionType gettypeofconn = new getConnectionType();
//        String connType = gettypeofconn.getConTypeByElementId(elementId);
        if (ProgenConnection.SQL_SERVER.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select convert(varchar,cy_end_date,110) ed_date from pr_day_denom where upper(CY_CUST_NAME) = '" + getYear + "' ";

        } else if (ProgenConnection.MYSQL.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select date_format(cy_end_date,'%m/%d/%Y') ed_date from pr_day_denom where upper(CY_CUST_NAME) = '" + getYear + "' ";
        } else {
            finalQuery = " Select to_char(cy_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where upper(CY_CUST_NAME) = '" + getYear + "' ";

        }
        try {


            conn = ProgenConnection.getInstance().getConnectionForElement(connID);
            retObj = db.execSelectSQL(finalQuery, conn);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (retObj != null) {
            colNames = retObj.getColumnNames();
            int psize = retObj.getRowCount();

            if (psize > 0) {
                result = retObj.getFieldValueString(0, colNames[0]);
            }

        }

        return result;
    }

    public String getQtrDrill(String getYear, ManagementTemplateMeta reportMeta, String connID) {
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String result = null;
        String[] colNames;
        Connection conn = null;
        String finalQuery = "";
        PbDb db = new PbDb();
        String connectionType = ProgenConnection.getInstance().getDatabaseType();
        getYear = getYear.replace("]", "").replace("[", "").replace("\"", "").toUpperCase();
//        getConnectionType gettypeofconn = new getConnectionType();
//        String connType = gettypeofconn.getConTypeByElementId(elementId);
        if (ProgenConnection.SQL_SERVER.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select convert(varchar,cq_end_date,110) ed_date from pr_day_denom where upper(CQ_CUST_NAME)= '" + getYear + "' ";

        } else if (ProgenConnection.MYSQL.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select date_format(cq_end_date,'%m/%d/%Y') ed_date from pr_day_denom where upper(CQ_CUST_NAME) = '" + getYear + "' ";
        } else {
            finalQuery = " Select to_char(cq_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where upper(CQ_CUST_NAME) = '" + getYear + "' ";

        }

        //String finalQuery = " Select to_char(cq_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where CQ_CUST_NAME = '" + getYear + "' ";
        try {


            conn = ProgenConnection.getInstance().getConnectionForElement(connID);
            retObj = db.execSelectSQL(finalQuery, conn);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (retObj != null) {
            colNames = retObj.getColumnNames();
            int psize = retObj.getRowCount();

            if (psize > 0) {
                result = retObj.getFieldValueString(0, colNames[0]);
            }
        }


        return result;
    }

    public String getMonthDrill(String getYear, ManagementTemplateMeta reportMeta, String connID) {
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String result = null;
        String[] colNames = null;
        Connection conn = null;
        String finalQuery = "";
        PbDb db = new PbDb();
        String connectionType = ProgenConnection.getInstance().getDatabaseType();
        int psize = 0;
        getYear = getYear.replace("]", "").replace("[", "").replace("\"", "").toUpperCase();


        if (ProgenConnection.SQL_SERVER.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select convert(varchar,cm_end_date,110) ed_date from pr_day_denom where upper(CM_CUST_NAME) = '" + getYear + "' ";

        } else if (ProgenConnection.MYSQL.equalsIgnoreCase(connectionType)) {
            finalQuery = " Select date_format(cm_end_date,'%m/%d/%Y') ed_date from pr_day_denom where upper(CM_CUST_NAME) = '" + getYear + "' ";
        } else {
            finalQuery = " Select to_char(cm_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where upper(CM_CUST_NAME) = '" + getYear + "' ";

        }
        //String finalQuery = " Select to_char(cm_end_date,'MM/DD/YYYY') ed_date from pr_day_denom where CM_CUST_NAME = '" + getYear + "' ";
//        
        try {


            conn = ProgenConnection.getInstance().getConnectionForElement(connID);
            retObj = db.execSelectSQL(finalQuery, conn);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        //Modified By Mayank
        if (retObj != null) {
            colNames = retObj.getColumnNames();

            psize = retObj.getRowCount();
            result = retObj.getFieldValueString(0, colNames[0]);
        }



        return result;
    }

    public ArrayList getClauseForDrill(PbReportCollection collect, String element_Id, String end_date) {
        ArrayList tempTimeDetails = new ArrayList();
        if (collect != null) {
            ArrayList timeDetails = collect.timeDetailsArray;
            for (int i = 0; i < timeDetails.size(); i++) {
                tempTimeDetails.add(i, timeDetails.get(i));
            }
            tempTimeDetails.remove(2);
            tempTimeDetails.add(2, end_date);

        }
        System.out.println("**************************************************************timeDEtails**************:" + tempTimeDetails.toString());
        return tempTimeDetails;
    }

    public String getPriorValue(ArrayList<String> timeDetails, String connID) {
        Connection conn = null;
        PbReturnObject retObj = null;
        String getYear = timeDetails.get(2).toString();

        String finalQuery = "";
        PbDb db = new PbDb();
        String connectionType = ProgenConnection.getInstance().getDatabaseType();
        if (connectionType != null && connectionType.equalsIgnoreCase("SqlServer")) {
            getYear = "   convert(datetime,'" + getYear + "',120) ";

        } else if (connectionType != null && connectionType.equalsIgnoreCase("Oracle")) {
            getYear = "   to_date('" + getYear + "','mm/dd/yyyy hh24:mi:ss ') ";

        } else if (connectionType != null && connectionType.equalsIgnoreCase("Postgres")) {
            getYear = "   to_timestamp('" + getYear + "','mm/dd/yyyy hh24:mi:ss ') ";

        }

//        if (ProgenConnection.SQL_SERVER.equalsIgnoreCase(connectionType)) {
//            finalQuery = " select PM_CUST_NAME,PYM_CUST_NAME,LQ_CUST_NAME,LYQ_CUST_NAME,LY_CUST_NAME from pr_day_denom where ddate = " + getYear + " ";

//        } else if (ProgenConnection.MYSQL.equalsIgnoreCase(connectionType)) {
//            finalQuery = " select PM_CUST_NAME,PYM_CUST_NAME,LQ_CUST_NAME,LYQ_CUST_NAME,LY_CUST_NAME from pr_day_denom where ddate = " + getYear + " ";
//        } else {
            finalQuery = " select PM_CUST_NAME,PYM_CUST_NAME,LQ_CUST_NAME,LYQ_CUST_NAME,LY_CUST_NAME,PW_CUST_NAME,LYW_CUST_NAME from pr_day_denom where ddate = " + getYear + " ";

//        }


        try {


            conn = ProgenConnection.getInstance().getConnectionForElement(connID);
            retObj = db.execSelectSQL(finalQuery, conn);

            if (retObj != null && retObj.rowCount > 0) {
                if (timeDetails.get(3).toString().equalsIgnoreCase("QTR") && (timeDetails.get(4).toString().equalsIgnoreCase("Last Qtr")
                        || timeDetails.get(4).toString().equalsIgnoreCase("Last Month"))) {
                    return retObj.getFieldValueString(0, "LQ_CUST_NAME");
                }else if(timeDetails.get(3).toString().equalsIgnoreCase("week") && (timeDetails.get(4).toString().equalsIgnoreCase("Last Week") ||
                		timeDetails.get(4).toString().equalsIgnoreCase("Last Qtr") || timeDetails.get(4).toString().equalsIgnoreCase("Last PERIOD")
                        || timeDetails.get(4).toString().equalsIgnoreCase("Last Month"))){
                	 return retObj.getFieldValueString(0, "PW_CUST_NAME");
                }
                else if (timeDetails.get(3).toString().equalsIgnoreCase("WEEK") && (timeDetails.get(4).toString().equalsIgnoreCase("Same Week Last Year")
                        || timeDetails.get(4).toString().equalsIgnoreCase("Last Year")
                        || timeDetails.get(4).toString().equalsIgnoreCase("Same Month Last Year"))) {
                    return retObj.getFieldValueString(0, "LYW_CUST_NAME");
                }
                else if (timeDetails.get(3).toString().equalsIgnoreCase("QTR") && (timeDetails.get(4).toString().equalsIgnoreCase("Same Qtr Last Year")
                        || timeDetails.get(4).toString().equalsIgnoreCase("Last Year")
                        || timeDetails.get(4).toString().equalsIgnoreCase("Same Month Last Year"))) {
                    return retObj.getFieldValueString(0, "LYQ_CUST_NAME");
                } else if (timeDetails.get(3).toString().equalsIgnoreCase("Month") && (timeDetails.get(4).toString().equalsIgnoreCase("Last Month")
                        || timeDetails.get(4).toString().equalsIgnoreCase("Last Qtr") || timeDetails.get(4).toString().equalsIgnoreCase("Last Period"))) {
                    return retObj.getFieldValueString(0, "PM_CUST_NAME");
                } else if (timeDetails.get(3).toString().equalsIgnoreCase("Month") && timeDetails.get(4).toString().equalsIgnoreCase("Last Period")) {
                    return retObj.getFieldValueString(0, "PM_CUST_NAME");
                } else if (timeDetails.get(3).toString().equalsIgnoreCase("Month") && (timeDetails.get(4).toString().equalsIgnoreCase("Same Month Last Year")
                        || timeDetails.get(4).toString().equalsIgnoreCase("Last Year") || timeDetails.get(4).toString().equalsIgnoreCase("Same Qtr Last Year"))) {
                    return retObj.getFieldValueString(0, "PYM_CUST_NAME");
                } else if (timeDetails.get(3).toString().equalsIgnoreCase("Year")) {
                    return retObj.getFieldValueString(0, "LY_CUST_NAME");
                }

            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return "";
    }

    public String getObjectQueryForAOReport(String aoTableId, String reportId, String filterClause, ArrayList<String> rowViewIds, ArrayList<String> colViewIds, String startDate, String endDate, String timeLevel) {
        ReportObjectMeta roMeta = new ReportObjectMeta();
        String finalSql = "";
        File datafile = new File(filePath+"/analyticalobject/R_AO_" + reportId + ".json");
        if (datafile.exists()) {
            FileReadWrite readWrite = new FileReadWrite();
            String metaString = readWrite.loadJSON(filePath+"/analyticalobject/R_AO_" + reportId + ".json");
            Gson gson = new Gson();
            Type tarType = new TypeToken<ReportObjectMeta>() {
            }.getType();
            roMeta = gson.fromJson(metaString, tarType);
            //  String query;
            String osql_AO = roMeta.getOsql_AO();
            String OViewByCol_AO = roMeta.getOViewByCol_AO();
            // String OorderByCol_AO = roMeta.getOorderByCol_AO();
            String OmViewByCol_AO = roMeta.getOmViewByCol_AO();
            String OmorderByCol_AO = roMeta.getOmorderByCol_AO();
            String ColOrderByCol_AO = roMeta.getColOrderByCol_AO();
            String omsql_AO = roMeta.getOmsql_AO();
            String osqlGroup_AO = roMeta.getOsqlGroup_AO();
            String finalViewByCol_AO = roMeta.getFinalViewByCol_AO();
            String oWrapper_AO = roMeta.getoWrapper_AO();
            String dateValues_AO = roMeta.getDateValues_AO();
            String stDateAO = "";
            String edDateAO = "";
            String timeLevel_AO = "";
            OViewByCol_AO = "";
            OmViewByCol_AO = "";
            finalViewByCol_AO = "";
            OmorderByCol_AO = "";
            boolean isValidDate = true;
            if (dateValues_AO != null && !dateValues_AO.equalsIgnoreCase("")) {
                String[] dateVals = dateValues_AO.split(";");

                for (int i = 0; i < dateVals.length; i++) {
                    String temp = dateVals[i];
                    if (temp.contains("stDate")) {
                        stDateAO = temp.substring(temp.indexOf("~") + 1).replace("00:00:00", "").replace(" ", "");
                    } else if (temp.contains("endDate")) {
                        edDateAO = temp.substring(temp.indexOf("~") + 1).replace("00:00:00", "").replace(" ", "");
                    } else if (temp.contains("timeLevel_AO")) {
                        timeLevel_AO = temp.substring(temp.indexOf("~") + 1);
                    }
                }

                boolean isDate = false;
                SimpleDateFormat formatter1;
                String datePattern = "\\d{4}-\\d{1,2}-\\d{1,2}";
                isDate = stDateAO.matches(datePattern);
                if (isDate) {
                    formatter1 = new SimpleDateFormat("yyyy-mm-dd");
                } else {
                    formatter1 = new SimpleDateFormat("mm/dd/yyyy");
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                try {

                    Date sdateAO = formatter1.parse(stDateAO);
                    Date edateAO = formatter1.parse(edDateAO);
                    Date sdate = formatter1.parse(startDate);
                    Date edate = formatter1.parse(endDate);
                    stDateAO = sdf.format(sdateAO);
                    edDateAO = sdf.format(edateAO);
                    if (!(sdate.equals(sdateAO) || sdate.after(sdateAO) && sdate.before(edateAO)) && !(edate.equals(edateAO) || edate.before(edateAO) && edate.after(sdateAO))) {
                        isValidDate = false;
                    }
                } catch (ParseException ex) {
                    logger.error("Exception:", ex);
                }
                if (!timeLevel_AO.equalsIgnoreCase(timeLevel)) {
                    isValidDate = false;
                }
            }
            System.out.println("isValidDate:" + isValidDate);
            if (isValidDate) {
                ArrayList<String> totalViewIds = new ArrayList<String>();
                for (int i = 0; i < rowViewIds.size(); i++) {
                    if (!totalViewIds.contains(rowViewIds.get(i))) {
                        totalViewIds.add(rowViewIds.get(i).toString());
                    }
                }
                for (int i = 0; i < colViewIds.size(); i++) {
                    if (!totalViewIds.contains(colViewIds.get(i))) {
                        totalViewIds.add(colViewIds.get(i).toString());
                    }
                }
                for (int i = 0; i < totalViewIds.size(); i++) {
                    if (totalViewIds.get(i).toString().equalsIgnoreCase("TIME") || totalViewIds.get(i).toString().trim().equalsIgnoreCase("TIME")) {
                        OViewByCol_AO += ", " + totalViewIds.get(i).toString();
                        OmViewByCol_AO += ", O_" + totalViewIds.get(i).toString() + " AS " + totalViewIds.get(i).toString();
                        finalViewByCol_AO += ", " + totalViewIds.get(i).toString() + " AS " + totalViewIds.get(i).toString();
//                    OmorderByCol_AO += ", " + totalViewIds.get(i).toString() + " AS ORDER" + (i + 1);
                        OmorderByCol_AO += ", " + totalViewIds.get(i).toString() + " AS O_" + totalViewIds.get(i).toString();
                    } else {
                        OViewByCol_AO += ", " + "A_" + totalViewIds.get(i).toString();
                        OmViewByCol_AO += ", " + "A_" + totalViewIds.get(i).toString() + " AS " + "A_" + totalViewIds.get(i).toString();
                        finalViewByCol_AO += ", " + "A_" + totalViewIds.get(i).toString() + " AS " + "A_" + totalViewIds.get(i).toString();
//                    OmorderByCol_AO += ", " + "A_" + totalViewIds.get(i).toString() + " AS ORDER" + (i + 1);
                        OmorderByCol_AO += ", " + "A_" + totalViewIds.get(i).toString() + " AS O_" + totalViewIds.get(i).toString();
                    }
                }
                OViewByCol_AO = OViewByCol_AO.substring(1);
                OmViewByCol_AO = OmViewByCol_AO.substring(1);
                finalViewByCol_AO = finalViewByCol_AO.substring(1);
                OmorderByCol_AO = OmorderByCol_AO.substring(1);
                String finalSql_AO = "noData";
//                String tableName = "M_AO_" + aoTableId;
//                String qry = "Select COLUMN_NAME  from user_tab_columns where table_name='" + tableName + "'";
//                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                    qry = "SELECT  COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='" + tableName + "'";
//                }
                //check whether table exists in data and contains column equal to view by's and filter elements are there in table
                Connection conn = null;
                boolean tableFlag = true;
//                PbDb pbdb = new PbDb();
//                PbReturnObject rs = null;
                ProgenParam connectionparam = new ProgenParam();
//                String checkClause = "";
//                if (totalViewIds != null && !totalViewIds.isEmpty() && totalViewIds.size() > 0) {
//                    conn = connectionparam.getConnection(reportQryElementIds.get(0).toString().replace("A_", ""));
//                    try {
//                        rs = pbdb.execSelectSQL(qry, conn);
//                        if (rs != null && rs.getRowCount() > 0) {
//                            for (int i = 0; i < rs.getRowCount(); i++) {
//                                checkClause = checkClause + "," + rs.getFieldValueString(i, 0).toString().replace("A_", "").replace("E_", "");
//                            }
//                            for (int i = 0; i < totalViewIds.size(); i++) {
//                                if (!checkClause.contains(totalViewIds.get(i).toString().replace("A_", "").replace("E_", ""))) {
//                                    tableFlag = false;
//                                }
//                            }
//                            if (elementArrList != null) {
//                                for (int i = 0; i < elementArrList.length; i++) {
//                                    if (!checkClause.contains(elementArrList[i].toString().replace("A_", "").replace("E_", ""))) {
//                                        tableFlag = false;
//                                    }
//                                }
//                            }
//                            if (reportQryElementIds != null) {
//                                PbReturnObject rs1 = null;
////                                String reportQryEleArr = "";
//                                 StringBuilder reportQryEleArr = new StringBuilder();
//                                for (int i = 0; i < reportQryElementIds.size(); i++) {
//                                    reportQryEleArr.append(",").append(reportQryElementIds.get(i).toString());
////                                    reportQryEleArr = reportQryEleArr + "," + reportQryElementIds.get(i).toString();
//                                }
//                                 reportQryEleArr=new StringBuilder( reportQryEleArr.substring(1));
////                                reportQryEleArr = reportQryEleArr.substring(1);
//                            }
//                                String getBussColName = "select user_col_name,element_id from prg_user_all_info_details where element_id in (" + reportQryEleArr + ")";
//                                rs1 = pbdb.execSelectSQL(getBussColName);
//                                if (rs1 != null && rs1.getRowCount() > 0) {
//                                    for (int i = 0; i < rs1.getRowCount(); i++) {
//                                        String bussColName = rs1.getFieldValueString(i, 0).toString();
//                                        String elementId = rs1.getFieldValueString(i, 1).toString();
//                                        if (!checkClause.contains(bussColName)) {
//                                            if (!checkClause.contains(elementId)) {
//                                                tableFlag = false;
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        } else {
//                            tableFlag = true;
//                        }
//                    } catch (Exception ex) {
//                        tableFlag = false;
//                        logger.error("Exception:",ex);
//                    }
//                }
//                boolean isAOEnable = true;
//                String check = oWrapper_AO + " " + finalViewByCol_AO;
//                if (rowViewIds != null) {
//                    for (int i = 0; i < rowViewIds.size(); i++) {
//                        if (!check.contains(rowViewIds.get(i).toString())) {
//                            isAOEnable = false;
//                        }
//                    }
//                }
//                if (colViewIds != null) {
//                    for (int i = 0; i < colViewIds.size(); i++) {
//                        if (!check.contains(colViewIds.get(i).toString())) {
//                            isAOEnable = false;
//                        }
//                    }
//                }

                OViewByCol_AO = OViewByCol_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");
                osql_AO = osql_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");
                finalSql_AO = finalSql_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");
                OViewByCol_AO = OViewByCol_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");
                osqlGroup_AO = osqlGroup_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");
                osqlGroup_AO = osqlGroup_AO.replace("O_TIME", "TIME").replace("TIME", "O_TIME");


                if (tableFlag) {
                    System.out.println(OViewByCol_AO + "<------AODateFilt:-----> " + stDateAO);
                    //  if (OViewByCol_AO.toLowerCase().trim().contains("time")) {
                    finalSql_AO = "select * from M_AO_" + aoTableId + " where 1=1 " + filterClause;
//                    } else {
//                        finalSql_AO = "select * from R_AO_" + reportId + " where 1=1 " + filterClause + "and TIME =" + stDateAO;
//                    }
                    if (colViewIds != null && colViewIds.size() > 0) {
                        if (colViewIds != null && colViewIds.size() > 0) {
//                    finalSql_AO = " select " + OViewByCol_AO +" "+  osql_AO + " from ( " + finalSql_AO + " ) O5 group by " + OViewByCol_AO  + " " + osqlGroup_AO + " ";//
//                    finalSql_AO = " select " + OViewByCol_AO + " , " + OorderByCol_AO + osql_AO + " from ( " + finalSql_AO + " ) O5 group by " + OViewByCol_AO + " , " + OorderByCol_AO + " " + osqlGroup_AO + " ";//
                            finalSql_AO = " select " + OViewByCol_AO + " " + osql_AO + " from ( " + finalSql_AO + " ) O5 group by " + OViewByCol_AO + " " + osqlGroup_AO + " ";//

//                    finalSql_AO = " select " + OmViewByCol_AO  +" "+ omsql_AO + " from ( " + finalSql_AO + " ) O6  ";
//                    finalSql_AO = " select " + OmViewByCol_AO + " , " + OmorderByCol_AO + " , " + ColOrderByCol_AO + omsql_AO + " from ( " + finalSql_AO + " ) O6  ";
                            finalSql_AO = " select " + OmViewByCol_AO + " , " + OmorderByCol_AO + omsql_AO + " from ( " + finalSql_AO + " ) O6  ";
                        } else {
//                    finalSql_AO = " select " + OViewByCol_AO + " , " + OorderByCol_AO + osql_AO + " from ( " + finalSql_AO + " ) O7  group by " + OViewByCol_AO + " , " + OorderByCol_AO + " " + osqlGroup_AO + "  ";// //+ " order by " + OmorderByCol;
                            finalSql_AO = " select " + OViewByCol_AO + "  " + osql_AO + " from ( " + finalSql_AO + " ) O7  group by " + OViewByCol_AO + " " + osqlGroup_AO + "  ";// //+ " order by " + OmorderByCol;
//                    finalSql_AO = " select " + OViewByCol_AO  +" "+ osql_AO + " from ( " + finalSql_AO + " ) O7  group by " + OViewByCol_AO + " " + osqlGroup_AO + "  ";// //+ " order by " + OmorderByCol;

                            finalSql_AO = " select " + OmViewByCol_AO + " , " + OmorderByCol_AO + omsql_AO + " from ( " + finalSql_AO + " ) O7_1 ";
//                    finalSql_AO = " select " + OmViewByCol_AO +" " + omsql_AO + " from ( " + finalSql_AO + " ) O7_1 ";
                        }
                        if (!(colViewIds != null && colViewIds.size() > 0)) {
                            if (rowViewIds != null && rowViewIds.size() > 0) {
                                finalSql_AO = " select " + finalViewByCol_AO + oWrapper_AO + " from ( " + finalSql_AO + " ) OT1 order by  " + OmorderByCol_AO;
//                        finalSql_AO = " select " + finalViewByCol_AO + oWrapper_AO + " from ( " + finalSql_AO + " ) OT1 ";
                            }
                        }
                        finalSql_AO = finalSql_AO + " order by " + ColOrderByCol_AO;
                    } else {
                        if (colViewIds != null && colViewIds.size() > 0) {
                            finalSql_AO = " select " + OViewByCol_AO + " " + osql_AO + " from ( " + finalSql_AO + " ) O5 group by " + OViewByCol_AO + " " + osqlGroup_AO + " ";//
//                    finalSql_AO = " select " + OViewByCol_AO + " , " + OorderByCol_AO + osql_AO + " from ( " + finalSql_AO + " ) O5 group by " + OViewByCol_AO + " , " + OorderByCol_AO + " " + osqlGroup_AO + " ";//

                            finalSql_AO = " select " + OmViewByCol_AO + " " + omsql_AO + " from ( " + finalSql_AO + " ) O6  ";
//                    finalSql_AO = " select " + OmViewByCol_AO + " , " + OmorderByCol_AO + " , " + ColOrderByCol_AO + omsql_AO + " from ( " + finalSql_AO + " ) O6  ";
                        } else {
//                    finalSql_AO = " select " + OViewByCol_AO + " , " + OorderByCol_AO + osql_AO + " from ( " + finalSql_AO + " ) O7  group by " + OViewByCol_AO + " , " + OorderByCol_AO + " " + osqlGroup_AO + "  ";// //+ " order by " + OmorderByCol;
                            finalSql_AO = " select " + OViewByCol_AO + " " + osql_AO + " from ( " + finalSql_AO + " ) O7  group by " + OViewByCol_AO + " " + osqlGroup_AO + "  ";// //+ " order by " + OmorderByCol;

//                    finalSql_AO = " select " + OmViewByCol_AO + " , " + OmorderByCol_AO + omsql_AO + " from ( " + finalSql_AO + " ) O7_1 ";
                            finalSql_AO = " select " + OmViewByCol_AO + " " + omsql_AO + " from ( " + finalSql_AO + " ) O7_1 ";
                        }
                        if (!(colViewIds != null && colViewIds.size() > 0)) {
                            if (rowViewIds != null && rowViewIds.size() > 0) {
//                        finalSql_AO = " select " + finalViewByCol_AO + oWrapper_AO + " from ( " + finalSql_AO + " ) OT1 order by  " + OmorderByCol_AO;
                                finalSql_AO = " select " + finalViewByCol_AO + oWrapper_AO + " from ( " + finalSql_AO + " ) OT1 ";
                            }
                        }
                    }
                }
                conn = connectionparam.getConnection(reportQryElementIds.get(0).toString().replace("A_", ""));
                System.out.println("conn:::0" + conn.toString());

                if (conn.toString().contains("sqlserver")) {
                    finalSql_AO = finalSql_AO.replace("nvl(", "COALESCE(");//temporary fix --Remove this code by defining variable for nvl at top
                    finalSql_AO = finalSql_AO.replace("Nvl(", "COALESCE(");//temporary fix
                    finalSql_AO = finalSql_AO.replace("NVL(", "COALESCE(");//temporary fix
                }
                if (conn.toString().contains("PostgreSQL")) {//
                    finalSql_AO = finalSql_AO.replace("nvl(", "COALESCE(");//temporary fix --Remove this code by defining variable for nvl at top
                    finalSql_AO = finalSql_AO.replace("Nvl(", "COALESCE(");//temporary fix
                    finalSql_AO = finalSql_AO.replace("NVL(", "COALESCE(");//temporary fix
                }
                if (conn.toString().toUpperCase().contains("MYSQL")) {
                    finalSql_AO = finalSql_AO.replace("nvl(", "ifNULL(");//temporary fix --Remove this code by defining variable for nvl at top
                    finalSql_AO = finalSql_AO.replace("Nvl(", "ifNULL(");//temporary fix
                    finalSql_AO = finalSql_AO.replace("NVL(", "ifNULL(");//temporary fix
                }

//                finalSql_AO = finalSql_AO.replace("O_TIME", "TIME");
//                finalSql_AO = finalSql_AO.replace("TIME", "O_TIME");

                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        logger.error("Exception:", ex);
                    }
                }
                logger.info("PROGEBN ReportObjectQuery:::::::::::::::::::::::::::::" + finalSql_AO);
                return finalSql_AO;
            } else {
                return "noData";
            }

        } else {
            return "noData";
        }
    }
    
        public String ArrayToCommaString(List<String> measureIdsList) {
      //  String collecror = "";
        StringBuilder collecror =new StringBuilder();
        if (measureIdsList == null || measureIdsList.isEmpty()) {
        } else {
            for (int loop = 0; loop < measureIdsList.size(); loop++) {
                if (loop == 0) {
                   // collecror += measureIdsList.get(loop);
                    collecror.append("'").append(measureIdsList.get(loop)).append("'");
                } else {
                    collecror.append(", '").append(measureIdsList.get(loop)).append("'");
}
            }
        }

        return collecror.toString();
    }
}
