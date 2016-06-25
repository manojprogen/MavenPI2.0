/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportdesigner.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.report.PbReportCollection;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.reportview.db.PbReportViewerDAO;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author ProGen
 */
public class AOTemplateDAO extends PbDb {

    private HashMap TableHashMap = null;
    ResourceBundle resourceBundle;// = new ReportTemplateResourceBundle();
    private ArrayList reportQryColNames = null;
    private ArrayList reportQryColTypes = null;
    private ArrayList originalReportQryColTypes = null;
    private HashMap ParameterGroupMap = null;
    private boolean overWriteExistingReport = false;
    public String dateEleId = "";
    String avgvaltype = "";
    HashMap avgcalmap = new HashMap();
    Set avgvalcount = new HashSet();
    public static Logger logger = Logger.getLogger(AOTemplateDAO.class);

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new ReportTemplateResBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                resourceBundle = new ReportTemplateResourceBundleMySql();
            } else {
                resourceBundle = new ReportTemplateResourceBundle();
            }
        }

        return resourceBundle;
    }

    public ArrayList insertAOMaster(int reportId, String reportName, String reportDesc, String UserFolderIds, String mapEnabled, List<String> customSeq, HashMap<String, ArrayList<String>> transposeFormatMap, HashMap<String, HashMap<String, ArrayList<String>>> targValue, HashMap<String, ArrayList<String>> goalPercent, String userId, List<String> rowViewByValues, String groupName, List<String> ElemeIds, HashMap<String, HashMap<String, ArrayList<String>>> goalTimeIndiv, List<String> newProdlist, Container container, String Gtregion) {
        String insertAOMasterQuery = "";
        boolean isFromAOEdit = container.getIsFromAOEdit();
        PbReportViewerBD bd = new PbReportViewerBD();
//        if(overwrite && ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)){
//             insertReportMasterQuery = getResourceBundle().getString("UpdateReportMaster");
//        }else if(overwrite && ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)){
//             insertReportMasterQuery = getResourceBundle().getString("UpdateReportMaster");
//        }else{
        if (isFromAOEdit) {
            insertAOMasterQuery = getResourceBundle().getString("updateAOMaster");
        } else {
            insertAOMasterQuery = getResourceBundle().getString("insertAOMaster");
        }
        //}


        String getusernameQuery = getResourceBundle().getString("getUserName");
        Object[] user = null;
        String query = "";
        PbReturnObject retobj = null;
        try {
            user = new Object[1];
            user[0] = userId;
            query = buildQuery(getusernameQuery, user);
            retobj = this.execSelectSQL(query);
        }
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }  catch (Exception ex) {
            logger.error("Exception: ", ex);
        }       logger.info("successful");
        String userName = "";
        if (retobj.rowCount != 0) {
            userName = retobj.getFieldValueString(0, 0);
        }
        //  System.out.println("userName"+userName);
        ArrayList queries = new ArrayList();
        Object[] reportMaster = null;
        String finalQuery = "";
        Gson gson = new Gson();
        //System.out.println("reportName in insert\t"+reportName);
        reportName = reportName.replace("'", "''");
        reportDesc = reportDesc.replace("'", "''");

        String tduration = container.tduration ;
        String  aoSdate =  container.aoSdate ; 
        String aoEdate =   container.aoEdate ;
        String  aoTimePeriod = container.aoTimePeriod ;

        StringBuilder Parameters = new StringBuilder();
        Parameters.append(",").append(tduration).append(",").append(aoSdate).append(",").append(aoEdate).append(",").append(aoTimePeriod);


        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                if(overwrite)
//                reportMaster = new Object[20];
//                else
                if (isFromAOEdit) {
                    reportMaster = new Object[12];
                } else {
                    reportMaster = new Object[11];
                }
                reportMaster[0] = reportName;
                reportMaster[1] = reportDesc;
                reportMaster[2] = "Y";
                reportMaster[3] = reportId;
                reportMaster[4] = "M_AO_" + reportId;
                reportMaster[5] = "";
//                reportMaster[6] = null;
                reportMaster[6] = "";
                reportMaster[7] = Integer.parseInt(UserFolderIds);
                reportMaster[8] = "";
                reportMaster[9] = userId;
                reportMaster[10] = Parameters;
                
                if (isFromAOEdit) {
                    reportMaster[11] = reportId;
                }

            } else {
                if (isFromAOEdit) {
                    reportMaster = new Object[12];
                    reportMaster[0] = reportName;
                    reportMaster[1] = reportDesc;
                    reportMaster[2] = "Y";
                    reportMaster[3] = reportId;
                    reportMaster[4] = "M_AO_" + reportId;
                    reportMaster[5] = "";
//                    reportMaster[6] = null;
                    reportMaster[6] = "";
                    reportMaster[7] = Integer.parseInt(UserFolderIds);
                    reportMaster[8] = "";
                    reportMaster[9] = userId;
                    reportMaster[10] = Parameters;
                    reportMaster[11] = reportId;
                } else {
                    reportMaster = new Object[12];
                    reportMaster[0] = reportId;
                    reportMaster[1] = reportName;
                    reportMaster[2] = reportDesc;
                    reportMaster[3] = "Y";
                    reportMaster[4] = reportId;
                    reportMaster[5] = "M_AO_" + reportId;
//                    reportMaster[6] = null;
                    reportMaster[6] = "";
                    reportMaster[7] = "";
                    reportMaster[8] = Integer.parseInt(UserFolderIds);
                    reportMaster[9] = "";
                    reportMaster[10] = userId;
                    reportMaster[11] = Parameters;
                }

            }

            finalQuery = buildQuery(insertAOMasterQuery, reportMaster);

            queries.add(finalQuery);//inserting into report master
            //queries = insertAODetails(reportId, UserFolderIds, queries);//inserting into report details

            //boolean status = executeMultiple(queries);


            return queries;

        } catch (Exception e) {
            logger.error("Exception: ", e);
            return queries;
        }

    }

//    public ArrayList insertAODetails(int reportId, String UserFolderIds, ArrayList queries) throws Exception {
//        String insertReportDetailsQuery = "";
////        if(overwrite && ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) )
////            insertReportDetailsQuery = getResourceBundle().getString("updateReportDetails");
////        else if(overwrite && ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL))
////            insertReportDetailsQuery = getResourceBundle().getString("updateReportDetails");
////        else
//        insertReportDetailsQuery = getResourceBundle().getString("insertReportDetails");
//        Object[] reportFolders = null;
//        Object[] reportDetails = null;
//        String finalQuery = "";
//
//        if (UserFolderIds != null) {
//            reportFolders = UserFolderIds.split(",");
//
//            for (int i = 0; i < reportFolders.length; i++) {
//                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                   // if(!overwrite){
//                    reportDetails = new Object[1];
//                    reportDetails[0] = reportFolders[i];
////                    }else{
////                    reportDetails = new Object[2];
////                    reportDetails[0] = reportFolders[i];
////                    reportDetails[1] = reportId;
////                    }
//                } else if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                    //if(!overwrite){
//                    reportDetails = new Object[2];
//                    String qry = "select LAST_INSERT_ID(REPORT_ID) from PRG_AR_REPORT_MASTER order by 1 desc limit 1";
//                    PbReturnObject retobj = new PbReturnObject();
//                    retobj = super.execSelectSQL(qry);
//                    reportDetails[0] = String.valueOf(Integer.parseInt(retobj.getFieldValueString(0, 0))+1);
//                    reportDetails[1] = reportFolders[i];
////                    }else{
////                    reportDetails = new Object[2];
////                    reportDetails[0] = reportFolders[i];
////                    reportDetails[1] = reportId;
////                    }
//
//                }else {
//                    reportDetails = new Object[2];
//                    reportDetails[0] = reportId;
//                    reportDetails[1] = reportFolders[i];
//                }
//
//                finalQuery = buildQuery(insertReportDetailsQuery, reportDetails);//inserting into report details
//
//                queries.add(finalQuery);
//            }
//        }
//        return queries;
//    }
    public boolean saveReport(ArrayList alist) {
        boolean result = false;
        try {

            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                result = executeMultiple(alist);
            } else {
                result = execMultiple(alist);
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);

        }
        logger.info("successful");
        return result;
    }

    public ArrayList insertAOParamDetails(int reportId, String paramsString, String paramString1, ArrayList queries, Container container) {

        //String insertReportParamDetailsQuery = resBundle.getString("insertReportParamDetails");
        String getAOParamDetailsQuery = getResourceBundle().getString("getAOParamDetails");
        PbReportCollection collect = container.getReportCollect();
        PbReturnObject retObj = null;
        String[] dbColumns = null;
        String finalQuery = "";
        Object[] Obj = null;
        String[] paramIds = paramsString.split(",");
        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
            Obj = new Object[3];
            Obj[0] = reportId;
            Obj[1] = paramsString.toString().replaceAll("elmnt-", "").trim();
            Obj[2] = paramString1.toString().replaceAll("elmnt-", "").trim();
        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
            Obj = new Object[3];
            Obj[0] = reportId;
            Obj[1] = paramsString.toString().replaceAll("elmnt-", "").trim();
            Obj[2] = paramString1.toString().replaceAll("elmnt-", "").trim();

        } else {
            Obj = new Object[3];
            Obj[0] = reportId;
            Obj[1] = paramsString.toString().replaceAll("elmnt-", "").trim();
            Obj[2] = paramString1.toString().replaceAll("elmnt-", "").trim();
        }
        String addParameterSecurity = "";

        try {

            finalQuery = buildQuery(getAOParamDetailsQuery, Obj);
            //////.println("finalquery getreportparamdetails are : " + finalQuery);
//            System.out.println("insertAOParamDetails:::"+finalQuery);
            logger.info("insertAOParamDetails:::" + finalQuery);
            retObj = execSelectSQL(finalQuery);
            dbColumns = retObj.getColumnNames();
            //modified by bharathi reddy default value clob insertion
            Connection connection = null;
            PreparedStatement opstmt = null;
            ArrayList<String> repqueries = new ArrayList<String>();
            Gson gson = new Gson();
            Type targetType = new TypeToken<List<String>>() {
            }.getType();
            PbReportViewerDAO vwBd = new PbReportViewerDAO();
            String aoDetailsId = "";
//            if((overwrite && ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) ||  (overwrite && ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL))){
//                for (int i = 0; i < retObj.getRowCount(); i++) {
//                    Object[] object = new Object[16];
//                    addParameterSecurity = "insert into PRG_AR_AO_DETAILS (AO_DETAILS_ID,AO_ID,AO_TABLE_NAME,AO_DISP_NAME,AO_TYPE,AO_AGGR,AO_FORMULA,AO_REFRENCE_ID,DIM_ID,ELEMENT_ID,AO_FORMULA_TYPE,IS_DEFAULT_DATE,IS_JOIN_DATE,BUSS_TABLE_ID) values('&','&','&','&','&','&','&','&','&','&','&','&','&','&')";
//                    object[0] = reportId;
//                    object[1] = retObj.getFieldValueString(i, dbColumns[1]);
//                    object[2] = retObj.getFieldValueString(i, dbColumns[2]);
//                    object[3] = retObj.getFieldValueString(i, dbColumns[3]);
//                    object[4] = retObj.getFieldValueString(i, dbColumns[4]);
//                    object[5] = retObj.getFieldValueString(i, dbColumns[5]);
//                    List<String> defultVlueList=collect.getInValList(paramIds[i]);
//                    String defaultVal= gson.toJson(defultVlueList, targetType);
//                    if (defultVlueList != null && !defultVlueList.isEmpty()) {
//                    object[6] =  defaultVal;
//                    } else {
//                    object[6]="[\"All\"]";
//                   }
//                    object[7] = collect.getParameterStatus(paramIds[i]);
//
//                   if(container.getDateandTimeOptions("A_"+paramIds[i])!=null && !container.getDateandTimeOptions("A_"+paramIds[i]).equalsIgnoreCase("null") && !"".equals(container.getDateandTimeOptions("A_"+paramIds[i])))
//                     object[8]=container.getDateandTimeOptions("A_"+paramIds[i]);
//                   else
//                    object[8]=null;
//
//                  if(container.getDateSubStringValues("A_"+paramIds[i])!=null && !container.getDateSubStringValues("A_"+paramIds[i]).equalsIgnoreCase("null") && !"".equals(container.getDateSubStringValues("A_"+paramIds[i])))
//                   object[9]=container.getDateSubStringValues("A_"+paramIds[i]);
//                  else
//                   object[9]=null;
//
//                  if(container.getDateFormatt("A_"+paramIds[i])!=null && !container.getDateFormatt("A_"+paramIds[i]).equalsIgnoreCase("null") && !"".equals(container.getDateFormatt("A_"+paramIds[i])))
//                     object[10]=container.getDateFormatt("A_"+paramIds[i]);
//                    else
//                    object[10]=null;
//
//
//                    List<String> notInValueList=collect.getNotInValList(paramIds[i]) != null ? collect.getNotInValList(paramIds[i]) : new ArrayList<String>();
//                    List<String> likeValueList=collect.getLikeValList(paramIds[i]) != null ? collect.getLikeValList(paramIds[i]) : new ArrayList<String>();
//                    List<String> notLikeValueList=collect.getNotLikeValList(paramIds[i]) != null ? collect.getNotLikeValList(paramIds[i]) : new ArrayList<String>();
//                    String notInVlue=gson.toJson(notInValueList, targetType);
//                    String likeVal=gson.toJson(likeValueList,targetType);
//                    String notLikeVal=gson.toJson(notLikeValueList,targetType);
//                    object[11]=notInVlue;
//                    object[12]=likeVal;
//                    object[13]=notLikeVal;
//
//            String rowviewbys;
//
//                if (collect.reportIncomingParameters.get("CBOVIEW_BY_1") != null){
//
//                    rowviewbys=(String)collect.reportIncomingParameters.get("CBOVIEW_BY_1");
//                    if( rowviewbys == null ? retObj.getFieldValueString(i, dbColumns[2]) == null : rowviewbys.equals(retObj.getFieldValueString(i, dbColumns[2]))){
//                     object[14]="true";
//                    }else{
//                     object[14]="false";
//                    }
//
//                }else{
//                 object[14]="false";
//                }
//                  String finalquery = "";
//                  finalquery = addParameterSecurity;
//                    System.out.println("");
//                  finalquery = buildQuery(finalquery, object);
//                  repqueries.add(finalquery);
//                }
//                String deleteReportParamDetailsQuery = "delete from PRG_AR_REPORT_PARAM_DETAILS where report_id='" + reportId + "'";
//                execModifySQL(deleteReportParamDetailsQuery);
//                this.saveReport(repqueries);
//            }
            connection = ProgenConnection.getInstance().getConnection();
            boolean isFromAOEdit = container.getIsFromAOEdit();
            if (isFromAOEdit) {
                String deleteSQL = "DELETE from PRG_AR_AO_DETAILS WHERE AO_ID = ?";
                PreparedStatement ps = connection.prepareStatement(deleteSQL);
                ps.setInt(1, reportId);
                ps.executeUpdate();
                if (ps != null) {
                    ps.close();
                }
            }
            for (int i = 0; i < retObj.getRowCount(); i++) {

//                aoDetailsId = vwBd.getAODetailsId();
                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                    addParameterSecurity = "insert into PRG_AR_AO_DETAILS (AO_ID,AO_TABLE_NAME,AO_DISP_NAME,AO_TYPE,IS_DIM,IS_FACT,DIM_ID,DIM_NAME,"
                            + "ELEMENT_ID,BUSS_TABLE_ID,BUSS_COL_ID,DISP_NAME,USER_COL_DESC,USER_COL_TYPE,AGGR_TYPE,REF_ELEMENT_ID,ACT_COL_FORMULA,REFERED_ELEMENTS,IS_DEFAULT_DATE,IS_JOIN_DATE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                    addParameterSecurity = "insert into PRG_AR_AO_DETAILS (AO_ID,AO_TABLE_NAME,AO_DISP_NAME,AO_TYPE,IS_DIM,IS_FACT,DIM_ID,DIM_NAME,"
                            + "ELEMENT_ID,BUSS_TABLE_ID,BUSS_COL_ID,DISP_NAME,USER_COL_DESC,USER_COL_TYPE,AGGR_TYPE,REF_ELEMENT_ID,ACT_COL_FORMULA,REFERED_ELEMENTS,IS_DEFAULT_DATE,IS_JOIN_DATE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                } else {
                    addParameterSecurity = "insert into PRG_AR_AO_DETAILS (AO_DETAILS_ID,AO_ID,AO_TABLE_NAME,AO_DISP_NAME,AO_TYPE,IS_DIM,IS_FACT,DIM_ID,DIM_NAME,"
                            + "ELEMENT_ID,BUSS_TABLE_ID,BUSS_COL_ID,DISP_NAME,USER_COL_DESC,USER_COL_TYPE,AGGR_TYPE,REF_ELEMENT_ID,ACT_COL_FORMULA,REFERED_ELEMENTS,IS_DEFAULT_DATE,IS_JOIN_DATE) values(PRG_AR_AO_DETAILS_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                }


                opstmt = connection.prepareStatement(addParameterSecurity);
                //opstmt.setInt(1, Integer.parseInt(aoDetailsId));
                opstmt.setInt(1, retObj.getFieldValueInt(i, dbColumns[0]));
                opstmt.setString(2, "M_AO_" + reportId);
                opstmt.setString(3, "");
                opstmt.setString(4, "");
                opstmt.setString(5, retObj.getFieldValueString(i, dbColumns[1]));
                opstmt.setString(6, retObj.getFieldValueString(i, dbColumns[2]));
                opstmt.setInt(7, retObj.getFieldValueInt(i, dbColumns[3]));
                opstmt.setString(8, retObj.getFieldValueString(i, dbColumns[4]));
                opstmt.setInt(9, retObj.getFieldValueInt(i, dbColumns[5]));
                opstmt.setInt(10, retObj.getFieldValueInt(i, dbColumns[6]));
                opstmt.setInt(11, retObj.getFieldValueInt(i, dbColumns[7]));
                opstmt.setString(12, retObj.getFieldValueString(i, dbColumns[8]));
                opstmt.setString(13, retObj.getFieldValueString(i, dbColumns[9]));
                opstmt.setString(14, retObj.getFieldValueString(i, dbColumns[10]));
                opstmt.setString(15, retObj.getFieldValueString(i, dbColumns[11]));
                opstmt.setInt(16, retObj.getFieldValueInt(i, dbColumns[12]));
                opstmt.setString(17, retObj.getFieldValueString(i, dbColumns[13]));
                opstmt.setString(18, retObj.getFieldValueString(i, dbColumns[14]));
                opstmt.setString(19, "");
                opstmt.setString(20, "");

                // List<String> defultVlueList=collect.getInValList(paramIds[i]);
                //if (defultVlueList != null && !defultVlueList.isEmpty()) {

//                    String defaultVal= gson.toJson(defultVlueList, targetType);
//                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
//                        opstmt.setString(7, defaultVal);
//                    } else if(ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                //opstmt.setString(7, "");
//                    }else {
//                        ((OraclePreparedStatement) opstmt).setStringForClob(7, defaultVal);
//                    }
//                } else {
//                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
//                        opstmt.setString(7, "[\"All\"]");
//                    } else if(ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)){
//                        opstmt.setString(7, "[\"All\"]");
//                    }   else {
//                        ((OraclePreparedStatement) opstmt).setStringForClob(7, "[\"All\"]");
//                    }
//                }


                //opstmt.setString(8, collect.getParameterStatus(paramIds[i]));
//
//                if(container.getDateandTimeOptions("A_"+paramIds[i])!=null && !container.getDateandTimeOptions("A_"+paramIds[i]).equalsIgnoreCase("null") && !"".equals(container.getDateandTimeOptions("A_"+paramIds[i])))
//                   opstmt.setString(9, container.getDateandTimeOptions("A_"+paramIds[i]));
//                else
//                  opstmt.setString(9,null);
//
//                 if(container.getDateSubStringValues("A_"+paramIds[i])!=null && !container.getDateSubStringValues("A_"+paramIds[i]).equalsIgnoreCase("null") && !"".equals(container.getDateSubStringValues("A_"+paramIds[i])))
//                 opstmt.setString(10, container.getDateSubStringValues("A_"+paramIds[i]));
//                else
//                  opstmt.setString(10,null);
//                 if(container.getDateFormatt("A_"+paramIds[i])!=null && !container.getDateFormatt("A_"+paramIds[i]).equalsIgnoreCase("null") && !"".equals(container.getDateFormatt("A_"+paramIds[i])))
//                     opstmt.setString(11,container.getDateFormatt("A_"+paramIds[i]));
//                    else
//                    opstmt.setString(11,null);
//                List<String> notInValueList=collect.getNotInValList(paramIds[i]) != null ? collect.getNotInValList(paramIds[i]) : new ArrayList<String>();
//                List<String> likeValueList=collect.getLikeValList(paramIds[i]) != null ? collect.getLikeValList(paramIds[i]) : new ArrayList<String>();
//                List<String> notLikeValueList=collect.getNotLikeValList(paramIds[i]) != null ? collect.getNotLikeValList(paramIds[i]) : new ArrayList<String>();
//                String notInVlue=gson.toJson(notInValueList, targetType);
//                String likeVal=gson.toJson(likeValueList,targetType);
//                String notLikeVal=gson.toJson(notLikeValueList,targetType);
//                opstmt.setString(12,notInVlue);
//                opstmt.setString(13,likeVal);
//                opstmt.setString(14,notLikeVal);
//                String rowviewbys;
//                if (collect.reportIncomingParameters.get("CBOVIEW_BY_1") != null){
//
//                    rowviewbys=(String)collect.reportIncomingParameters.get("CBOVIEW_BY_1");
//                    if( rowviewbys == null ? retObj.getFieldValueString(i, dbColumns[2]) == null : rowviewbys.equals(retObj.getFieldValueString(i, dbColumns[2]))){
//                    opstmt.setString(15,"true");
//                    }else{
//                         opstmt.setString(15,"false");
//                    }
//                }else{
//                 opstmt.setString(15,"false");
//                }
                int rows = opstmt.executeUpdate();
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    try {
                        connection.commit();
                    } 
                    catch (java.sql.SQLException e) {
                        logger.error("Exception: ", e);
                    } catch (Exception e) {
                        logger.error("Exception: ", e);
                    }
                    logger.info("successful");
                }
            }
            if (connection != null) {
                opstmt.close();
                connection.close();
                opstmt = null;
                connection = null;
                //System.out.println("is closed: "+ opstmt.isClosed());
                //System.out.println("Is Cloed Connection: "+ connection.isClosed());
//                System.out.println("Is Cloed Connection: ");
                logger.info("Is Closed Connection: ");
            }

        }
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        //catch (SQLException ex) { 
//            java.util.logging.Logger.getLogger(AOTemplateDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        logger.info("successful");
        return queries;
    }

    public ArrayList insertAOMeasuresDetails(int reportId, String measuress, String measuresOrder, ArrayList queries, Container container) throws Exception {
//        ProgenLog.log(ProgenLog.FINE, this, "insertReportQueryDetails", "Enter");
        logger.info("Enter ");
        String getAOQueryInfoQuery = getResourceBundle().getString("getAOMeasuresInfo");
        //String insertReportQueryDetailsQuery = getResourceBundle().getString("insertReportQueryDetails");
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String finalQuery = "";
        String[] dbColumns = null;
        Obj = new Object[2];
        Obj[0] = measuress;
        Obj[1] = measuresOrder;
        finalQuery = buildQuery(getAOQueryInfoQuery, Obj);
        retObj = execSelectSQL(finalQuery);
        dbColumns = retObj.getColumnNames();
        HashMap<String, ArrayList<String>> summerizedTableHashMap = container.getSummerizedTableHashMap();
        Connection connection = null;
        PreparedStatement opstmt = null;
        String addParameterSecurity = "";
        PbReportViewerDAO vwBd = new PbReportViewerDAO();
        String aoDetailsId = "";
        if (retObj != null && retObj.getRowCount() > 0) {
            connection = ProgenConnection.getInstance().getConnection();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                //aoDetailsId = vwBd.getAODetailsId();
                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {

                    addParameterSecurity = "insert into PRG_AR_AO_DETAILS (AO_ID,AO_TABLE_NAME,AO_DISP_NAME,AO_TYPE,IS_DIM,IS_FACT,"
                            + "ELEMENT_ID,BUSS_TABLE_ID,BUSS_COL_ID,DISP_NAME,USER_COL_DESC,USER_COL_TYPE,AGGR_TYPE,REF_ELEMENT_ID,ACT_COL_FORMULA,REFERED_ELEMENTS,IS_DEFAULT_DATE,IS_JOIN_DATE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                   addParameterSecurity = "insert into PRG_AR_AO_DETAILS (AO_ID,AO_TABLE_NAME,AO_DISP_NAME,AO_TYPE,IS_DIM,IS_FACT,"
                            + "ELEMENT_ID,BUSS_TABLE_ID,BUSS_COL_ID,DISP_NAME,USER_COL_DESC,USER_COL_TYPE,AGGR_TYPE,REF_ELEMENT_ID,ACT_COL_FORMULA,REFERED_ELEMENTS,IS_DEFAULT_DATE,IS_JOIN_DATE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                } else {
                    addParameterSecurity = "insert into PRG_AR_AO_DETAILS (AO_DETAILS_ID,AO_ID,AO_TABLE_NAME,AO_DISP_NAME,AO_TYPE,IS_DIM,IS_FACT,"
                            + "ELEMENT_ID,BUSS_TABLE_ID,BUSS_COL_ID,DISP_NAME,USER_COL_DESC,USER_COL_TYPE,AGGR_TYPE,REF_ELEMENT_ID,ACT_COL_FORMULA,REFERED_ELEMENTS,IS_DEFAULT_DATE,IS_JOIN_DATE) values(PRG_AR_AO_DETAILS_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                }


                opstmt = connection.prepareStatement(addParameterSecurity);
                //opstmt.setInt(1, Integer.parseInt(aoDetailsId));
                opstmt.setInt(1, reportId);
                opstmt.setString(2, "M_AO_" + reportId);
                opstmt.setString(3, "");
                opstmt.setString(4, "");
                opstmt.setString(5, retObj.getFieldValueString(i, dbColumns[0]));
                opstmt.setString(6, retObj.getFieldValueString(i, dbColumns[1]));
                opstmt.setInt(7, retObj.getFieldValueInt(i, dbColumns[2]));
                opstmt.setInt(8, retObj.getFieldValueInt(i, dbColumns[3]));
                opstmt.setInt(9, retObj.getFieldValueInt(i, dbColumns[4]));
                opstmt.setString(10, retObj.getFieldValueString(i, dbColumns[5]));
                opstmt.setString(11, retObj.getFieldValueString(i, dbColumns[6]));
                opstmt.setString(12, retObj.getFieldValueString(i, dbColumns[7]));
                opstmt.setString(13, retObj.getFieldValueString(i, dbColumns[8]));
                opstmt.setInt(14, retObj.getFieldValueInt(i, dbColumns[9]));
                opstmt.setString(15, retObj.getFieldValueString(i, dbColumns[10]));
                opstmt.setString(16, retObj.getFieldValueString(i, dbColumns[11]));
                opstmt.setString(17, "");
                opstmt.setString(18, "");
//                opstmt.setString(20, "");
//                opstmt.setString(21, "");

                int rows = opstmt.executeUpdate();
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    try {
                        connection.commit();
                    } catch (java.sql.SQLException e) {
                        logger.error("Exception: ", e);
                    }catch (Exception ex) {
            logger.error("Exception: ", ex);
                    }
                    logger.info("successful");
                }
            }
            if (connection != null) {
                opstmt.close();
                connection.close();
                opstmt = null;
                connection = null;
//                System.out.println("Is Cloed Connection: ");
                logger.info("Is Closed Connection: ");
            }
        }
//        ProgenLog.log(ProgenLog.FINE, this, "insertAOQueryDetails", "Exit");
        logger.info("Exit ");
        return queries;
    }
}
