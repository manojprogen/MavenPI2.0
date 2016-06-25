/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.userlayer.db;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.progen.metadata.*;
import com.progen.report.PbReportCollection;
import com.progen.report.display.DisplayParameters;
import com.progen.report.query.GetDimFactMapping;
import com.progen.reportview.db.CreateKPIFromReport;
import com.progen.superadmin.SuperAdminBd;
import com.progen.userlayer.action.GenerateDragAndDrophtml;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import oracle.jdbc.OraclePreparedStatement;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.SourceConn;
import prg.util.PbEncrypter;
import prg.util.PbMail;
import prg.util.PbMailParams;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

/**
 * @filename UserLayerDAO
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 5, 2009, 4:34:36 PM
 */
public class UserLayerDAO extends PbDb {

    public static Logger logger = Logger.getLogger(UserLayerDAO.class);
    PbEncrypter pswrdencrypter = new PbEncrypter();
//    PbUserLayerResourceBundle resBundle = new PbUserLayerResourceBundle();
    private boolean isCompanyValid = false;
    /*
     * This method is for creating new user folder and returns : true for
     * success and false for failure
     */
    ResourceBundle resourceBundle;
    private boolean isMemberUseInOtherLevel = false;
    private String accessLevel = "userLevel";
    //for SQL Server insertion into PRG_USER_SUB_FOLDER_TABLES MAINTAIN PRIMARY KEY SEPARATE
//    int subFolderTabId;
    //for SQL Server insertion into PRG_USER_SUB_FLDR_ELEMENTS MAINTAIN PRIMARY KEY SEPARATE
//    int subFolderEleId;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new PbUserLayerResourceBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new PbUserLayerResourceBundleMysql();
            } else {
                resourceBundle = new PbUserLayerResourceBundle();
            }
        }

        return resourceBundle;
    }
    public static int refId = 0;

    public boolean addUserFolder(String folderName, String folderDesc, String grpId) {
        ArrayList alist = new ArrayList();
        String addUserFolderQuery = "";
        String insertExtraElementsForFactsQuery = getResourceBundle().getString("insertExtraElementsForFacts");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            addUserFolderQuery = getResourceBundle().getString("addUserFolderMysql");
            PbReturnObject pbref1 = null;
            try {
                pbref1 = execSelectSQL("select LAST_INSERT_ID(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1");
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            refId = pbref1.getFieldValueInt(0, 0);

        } else {
            addUserFolderQuery = getResourceBundle().getString("addUserFolder");
        }
        String finalQuery = "";
        boolean result = false;

        Object[] Obj = new Object[3];
        Obj[0] = folderName;
        Obj[1] = folderDesc;
        Obj[2] = grpId;
        try {
            //new ProgenConnection().getConnection();
            finalQuery = buildQuery(addUserFolderQuery, Obj);
            ////.println("finalQuery addUserFolder\t" + finalQuery);
            alist.add(finalQuery);
            alist = addUserFolderDetails(alist, grpId);
            logger.info("alist\t" + alist);
//            for(int ialist=0;ialist<alist.size();ialist++){
//                
//            execModifySQL((String) alist.get(ialist));
//            }
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                finalQuery = " Update PRG_USER_SUB_FOLDER_ELEMENTS set ref_element_id = element_id where REF_ELEMENT_TYPE = 1  ";

                ////.println("finalQuery for update" + finalQuery);
                alist.add(finalQuery);

            }
            result = executeMultiple(alist);
            //code to change formula ids to element id

            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {

                logger.info("insertExtraElementsForFactsQuery----" + insertExtraElementsForFactsQuery);
                super.execModifySQL(insertExtraElementsForFactsQuery);
            }

            String refElements = "";
            String ActFormula = "";
            String mainEleId = "";
            ArrayList updateQueries = new ArrayList();

            String folderId;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                folderId = ((Integer) this.getCurrentSeqSqlServer("PRG_USER_FOLDER")).toString();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                String folderidQuery = "select LAST_INSERT_ID(folder_id) from PRG_USER_FOLDER order by 1 desc limit 1";
                PbReturnObject pbroFolderId = execSelectSQL(folderidQuery);
                folderId = String.valueOf(pbroFolderId.getFieldValueInt(0, 0));
            } else {
                String folderidQuery = "select PRG_USER_FOLDER_SEQ.CURRVAL FROM dual";
                // finalQuery=buildQuery(folderidQuery, Obj);
                //////////////////////////////////////////////////////////////////////////////.println.println("final folderidQuery---"+folderidQuery);
                PbReturnObject pbroFolderId = execSelectSQL(folderidQuery);
                folderId = String.valueOf(pbroFolderId.getFieldValueInt(0, 0));
            }

            String getCalculatedFormulasfacts = "SELECT DISTINCT sub_folder_tab_id, disp_name FROM PRG_USER_SUB_FOLDER_TABLES where is_fact='Y' "
                    + " and sub_folder_id in (SELECT distinct SUB_FOLDER_ID  FROM PRG_USER_FOLDER_DETAIL where FOLDER_ID=" + folderId + " and sub_folder_type in('Facts')) and (disp_name in('Calculated Facts'))order by disp_name ";
            // finalQuery=buildQuery(getCalculatedFormulasfacts, Obj);
            //////////////////////////////////////////////////////////////////////////////.println.println("final getCalculatedFormulas---"+getCalculatedFormulasfacts);
            PbReturnObject pbrofacts = execSelectSQL(getCalculatedFormulasfacts);

            for (int j1 = 0; j1 < pbrofacts.getRowCount(); j1++) {
                String getCalculatedFormulas = "SELECT ELEMENT_ID,USER_COL_NAME,REFFERED_ELEMENTS,ACTUAL_FORMULA,BUSS_TABLE_ID  FROM PRG_USER_SUB_FOLDER_ELEMENTS where SUB_FOLDER_TAB_ID=" + pbrofacts.getFieldValueInt(j1, 0) + " order by ref_element_id, ref_element_type";
                // finalQuery=buildQuery(getCalculatedFormulas, Obj);
                //////////////////////////////////////////////////////////////////////////////.println.println("final getCalculatedFormulas---"+getCalculatedFormulas);
                PbReturnObject pbro = execSelectSQL(getCalculatedFormulas);

                for (int i = 0; i < pbro.getRowCount(); i++) {
                    refElements = pbro.getFieldValueString(i, "REFFERED_ELEMENTS");
                    ActFormula = pbro.getFieldValueString(i, "ACTUAL_FORMULA").replace("'", "''");
                    mainEleId = String.valueOf(pbro.getFieldValueInt(i, "ELEMENT_ID"));
                    //////////////////////////////////////////////////////////////////////////////.println.println("refElements--"+refElements);
                    //////////////////////////////////////////////////////////////////////////////.println.println("ActFormula--"+ActFormula);
                    if (!refElements.equalsIgnoreCase("")) {
                        String refEleList[] = refElements.split(",");
                        for (int j = 0; j < refEleList.length; j++) {
                            if (refEleList[j].startsWith("M-")) {
                                String detlist[] = refEleList[j].substring(2).split("-");
                                if (detlist.length >= 3) {
                                    String bussTableId = detlist[0];
                                    String bussColId = detlist[1];
                                    String memId = detlist[2];
                                    String str = "select distinct sub_folder_tab_id FROM PRG_USER_SUB_FOLDER_TABLES where is_dimension='Y' and member_id=" + memId + " and sub_folder_id=(SELECT distinct SUB_FOLDER_ID  FROM PRG_USER_FOLDER_DETAIL where FOLDER_ID=" + folderId + "  and sub_folder_type in('Dimensions')) and  buss_table_id=" + bussTableId;
                                    PbReturnObject pbro1 = execSelectSQL(str);
                                    //////////////////////////////////////////////////////////////////////////////.println.println("str---"+str);
                                    String eleIdQuery = " select element_id from PRG_USER_SUB_FOLDER_ELEMENTS where  buss_table_id=" + bussTableId + " and buss_col_id=" + bussColId + " and SUB_FOLDER_TAB_ID=" + pbro1.getFieldValueInt(0, 0);
                                    //////////////////////////////////////////////////////////////////////////////.println.println("elementQuery---"+eleIdQuery);
                                    PbReturnObject elementIdpbro = execSelectSQL(eleIdQuery);
                                    //////////////////////////////////////////////////////////////////////////////.println.println("eleId--"+elementIdpbro.getFieldValueInt(0,0));
                                    ActFormula = ActFormula.replace(refEleList[j], String.valueOf(elementIdpbro.getFieldValueInt(0, 0)));
                                    refElements = refElements.replace(refEleList[j], String.valueOf(elementIdpbro.getFieldValueInt(0, 0)));
                                }

                            } else {
                                //////////////////////////////////////////////////////////////////////////////.println.println("IN ELSE PART");

                                String detlist[] = refEleList[j].split("-");
                                if (detlist.length >= 2) {
                                    String bussTableId = detlist[0];
                                    String bussColId = detlist[1];
                                    String str = "select distinct sub_folder_tab_id FROM PRG_USER_SUB_FOLDER_TABLES where is_fact='Y' and sub_folder_id=(SELECT distinct SUB_FOLDER_ID  FROM PRG_USER_FOLDER_DETAIL where FOLDER_ID=" + folderId + " and sub_folder_type in('Facts')) and  buss_table_id=" + bussTableId;
                                    PbReturnObject pbro1 = execSelectSQL(str);
                                    //////////////////////////////////////////////////////////////////////////////.println.println("str---"+str);
                                    String eleIdQuery = " select element_id from PRG_USER_SUB_FOLDER_ELEMENTS where  buss_table_id=" + bussTableId + " and buss_col_id=" + bussColId + "  and  SUB_FOLDER_TAB_ID=" + pbro1.getFieldValueInt(0, 0);
                                    //////////////////////////////////////////////////////////////////////////////.println.println("elementQuery---"+eleIdQuery);
                                    PbReturnObject elementIdpbro = execSelectSQL(eleIdQuery);
                                    //////////////////////////////////////////////////////////////////////////////.println.println("eleId--"+elementIdpbro.getFieldValueInt(0,0));
                                    refElements = refElements.replace(refEleList[j], String.valueOf(elementIdpbro.getFieldValueInt(0, 0)));
                                } else {
                                    String bussTableId = String.valueOf(pbro.getFieldValueInt(i, "BUSS_TABLE_ID"));
                                    String bussColId = detlist[0];
                                    String str = "select distinct sub_folder_tab_id FROM PRG_USER_SUB_FOLDER_TABLES where is_fact='Y' and sub_folder_id=(SELECT distinct SUB_FOLDER_ID  FROM PRG_USER_FOLDER_DETAIL where FOLDER_ID=" + folderId + " and sub_folder_type in('Facts')) and  buss_table_id=" + bussTableId;
                                    PbReturnObject pbro1 = execSelectSQL(str);
                                    //////////////////////////////////////.println.println("str---"+str);
                                    String eleIdQuery = " select element_id from PRG_USER_SUB_FOLDER_ELEMENTS where  buss_table_id=" + bussTableId + " and buss_col_id=" + bussColId + "  and  SUB_FOLDER_TAB_ID=" + pbro1.getFieldValueInt(0, 0);
                                    //////////////////////////////////////.println.println("elementQuery---"+eleIdQuery);
                                    PbReturnObject elementIdpbro = execSelectSQL(eleIdQuery);
                                    //////////////////////////////////////.println.println("eleId--"+elementIdpbro.getFieldValueInt(0,0));
                                    refElements = refElements.replace(refEleList[j], String.valueOf(elementIdpbro.getFieldValueInt(0, 0)));

                                }
                            }
                        }

                        String updateQuery = " update PRG_USER_SUB_FOLDER_ELEMENTS set REFFERED_ELEMENTS='" + refElements + "', ACTUAL_FORMULA='" + ActFormula + "' where element_id=" + mainEleId;
                        updateQueries.add(updateQuery);
                    }

                }
            }

            String sql = "SELECT DISTINCT sub_folder_tab_id, disp_name FROM PRG_USER_SUB_FOLDER_TABLES where is_fact='Y' and sub_folder_id=(SELECT distinct SUB_FOLDER_ID  FROM PRG_USER_FOLDER_DETAIL where FOLDER_ID=" + folderId + " and sub_folder_type in('Facts')) and disp_name not in('Calculated Facts','Formula') order by disp_name ";
            PbReturnObject pbro2 = execSelectSQL(sql);
            for (int i = 0; i < pbro2.getRowCount(); i++) {
                String getCalculatedFormulas1 = "SELECT ELEMENT_ID,USER_COL_NAME,REFFERED_ELEMENTS,ACTUAL_FORMULA,BUSS_TABLE_ID  FROM PRG_USER_SUB_FOLDER_ELEMENTS where SUB_FOLDER_TAB_ID=" + pbro2.getFieldValueInt(i, 0) + " order by ref_element_id, ref_element_type";
                // finalQuery=buildQuery(getCalculatedFormulas1, Obj);
                PbReturnObject pbro3 = execSelectSQL(getCalculatedFormulas1);

                for (int i1 = 0; i1 < pbro3.getRowCount(); i1++) {
                    refElements = pbro3.getFieldValueString(i1, "REFFERED_ELEMENTS");
                    ActFormula = pbro3.getFieldValueString(i1, "ACTUAL_FORMULA").replace("'", "''");
                    mainEleId = String.valueOf(pbro3.getFieldValueInt(i1, "ELEMENT_ID"));
                    if (!refElements.equalsIgnoreCase("")) {
                        String refEleList[] = refElements.split(",");
                        for (int j = 0; j < refEleList.length; j++) {
                            if (refEleList[j].startsWith("M-")) {
                                String detlist[] = refEleList[j].substring(2).split("-");
                                if (detlist.length >= 3) {
                                    String bussTableId = detlist[0];
                                    String bussColId = detlist[1];
                                    String memId = detlist[2];
                                    String str = "select distinct sub_folder_tab_id FROM PRG_USER_SUB_FOLDER_TABLES where is_dimension='Y' and member_id=" + memId + " and sub_folder_id=(SELECT distinct SUB_FOLDER_ID  FROM PRG_USER_FOLDER_DETAIL where FOLDER_ID=" + folderId + "  and sub_folder_type in('Dimensions')) and  buss_table_id=" + bussTableId;
                                    PbReturnObject pbro1 = execSelectSQL(str);
                                    String eleIdQuery = " select element_id from PRG_USER_SUB_FOLDER_ELEMENTS where  buss_table_id=" + bussTableId + " and buss_col_id=" + bussColId + " and SUB_FOLDER_TAB_ID=" + pbro1.getFieldValueInt(0, 0);
                                    PbReturnObject elementIdpbro = execSelectSQL(eleIdQuery);
                                    ActFormula = ActFormula.replace(refEleList[j], String.valueOf(elementIdpbro.getFieldValueInt(0, 0)));
                                    refElements = refElements.replace(refEleList[j], String.valueOf(elementIdpbro.getFieldValueInt(0, 0)));
                                }

                            } else {

                                String detlist[] = refEleList[j].split("-");
                                if (detlist.length >= 2) {
                                    String bussTableId = detlist[0];
                                    String bussColId = detlist[1];
                                    String str = "select distinct sub_folder_tab_id FROM PRG_USER_SUB_FOLDER_TABLES where is_fact='Y' and sub_folder_id=(SELECT distinct SUB_FOLDER_ID  FROM PRG_USER_FOLDER_DETAIL where FOLDER_ID=" + folderId + " and sub_folder_type in('Facts')) and  buss_table_id=" + bussTableId;
                                    PbReturnObject pbro1 = execSelectSQL(str);
                                    String eleIdQuery = " select element_id from  PRG_USER_SUB_FOLDER_ELEMENTS where  buss_table_id=" + bussTableId + " and buss_col_id=" + bussColId + "  and  SUB_FOLDER_TAB_ID=" + pbro1.getFieldValueInt(0, 0);
                                    PbReturnObject elementIdpbro = execSelectSQL(eleIdQuery);
                                    refElements = refElements.replace(refEleList[j], String.valueOf(elementIdpbro.getFieldValueInt(0, 0)));
                                } else {
                                    String bussTableId = String.valueOf(pbro3.getFieldValueInt(i, "BUSS_TABLE_ID"));
                                    String bussColId = detlist[0];
                                    String str = "select distinct sub_folder_tab_id FROM PRG_USER_SUB_FOLDER_TABLES where is_fact='Y' and sub_folder_id=(SELECT distinct SUB_FOLDER_ID  FROM PRG_USER_FOLDER_DETAIL where FOLDER_ID=" + folderId + " and sub_folder_type in('Facts')) and  buss_table_id=" + bussTableId;
                                    PbReturnObject pbro1 = execSelectSQL(str);
                                    String eleIdQuery = " select element_id from PRG_USER_SUB_FOLDER_ELEMENTS where  buss_table_id=" + bussTableId + " and buss_col_id=" + bussColId + "  and  SUB_FOLDER_TAB_ID=" + pbro1.getFieldValueInt(0, 0);
                                    PbReturnObject elementIdpbro = execSelectSQL(eleIdQuery);
                                    refElements = refElements.replace(refEleList[j], String.valueOf(elementIdpbro.getFieldValueInt(0, 0)));

                                }
                            }
                        }

                        String updateQuery = " update PRG_USER_SUB_FOLDER_ELEMENTS set REFFERED_ELEMENTS='" + refElements + "', ACTUAL_FORMULA='" + ActFormula + "' where element_id=" + mainEleId;

                        updateQueries.add(updateQuery);

                    }
                }
            }

            result = executeMultiple(updateQueries);

            return result;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return result;
        }
    }

    /*
     * This method is for inserting a records into user folder details and
     * returns : ArrayList which contains queries to be executed
     */
    public ArrayList addUserFolderDetails(ArrayList alist, String grpId) throws Exception {
        String addUserFolderDetailsQuery = getResourceBundle().getString("addUserFolderDetails");

        String[] folderDetailNames = {"Dimensions", "Facts", "Buckets"};
        Object[] Obj = null;
        int foldDtlId = 0;
        Obj = new Object[3];

        String finalQuery = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            foldDtlId = this.getCurrentSeqSqlServer("PRG_USER_FOLDER_DETAIL");
//            this.subFolderTabId = this.getCurrentSeqSqlServer("PRG_USER_SUB_FOLDER_TABLES");
//            this.subFolderEleId = this.getCurrentSeqSqlServer("PRG_USER_SUB_FLDR_ELEMENTS");
        }

        for (int index = 0; index < folderDetailNames.length; index++) {

            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                Obj[0] = "IDENT_CURRENT('PRG_USER_FOLDER')";
                Obj[1] = folderDetailNames[index];
                Obj[2] = folderDetailNames[index];
                foldDtlId += index + 1;
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                foldDtlId = getSequenceNumber("select LAST_INSERT_ID(sub_folder_id) from PRG_USER_FOLDER_DETAIL order by 1 desc limit 1");
                Obj[0] = "(select LAST_INSERT_ID(folder_id) from PRG_USER_FOLDER order by 1 desc limit 1)";
                Obj[1] = folderDetailNames[index];
                Obj[2] = folderDetailNames[index];
                foldDtlId += index + 1;
            } else {
                foldDtlId = getSequenceNumber("select PRG_USER_FOLDER_DETAIL_SEQ.NEXTVAL from dual");
                Obj[0] = String.valueOf(foldDtlId);
                Obj[1] = folderDetailNames[index];
                Obj[2] = folderDetailNames[index];
            }

            finalQuery = buildQuery(addUserFolderDetailsQuery, Obj);
            alist.add(finalQuery);
            alist = addUserSubFolderTables(alist, grpId, folderDetailNames[index], String.valueOf(foldDtlId), index);
        }

        return alist;

    }

    public ArrayList addUserSubFolderTables(ArrayList alist, String grpId, String folderType, String folderDtlId, int index) throws Exception {
        String addUserSubFolderTablesQuery = getResourceBundle().getString("addUserSubFolderTables");
        String addUserSubFolderTablesForDimensionsQuery = getResourceBundle().getString("addUserSubFolderTablesForDimensions");

        String getBussDimInfoByGrpIdQuery = getResourceBundle().getString("getBussDimInfoByGrpId");
        String getBussFactsInfoByGrpIdQuery = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            getBussFactsInfoByGrpIdQuery = getResourceBundle().getString("getBussFactsInfoByGrpIdMysql");
        } else {
            getBussFactsInfoByGrpIdQuery = getResourceBundle().getString("getBussFactsInfoByGrpId");
        }
        String getBussBucketInfoByGrpIdQuery = getResourceBundle().getString("getBussBucketInfoByGrpId");

        String finalQuery = "";
        PbReturnObject retObj = null;
        String[] tableColNames = null;

        int subFoldTabId = 0;

        //For Dimensions
        if (folderType.equalsIgnoreCase("Dimensions")) {
            Object[] Obj = new Object[1];
            Obj[0] = grpId;
            finalQuery = buildQuery(getBussDimInfoByGrpIdQuery, Obj);
            ////.println(" select  finalQuery\t  " + finalQuery);
            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {

                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                    this.subFolderTabId += i;
                    Obj = new Object[16];
                    Obj[0] = "IDENT_CURRENT('PRG_USER_FOLDER_DETAIL')";

                    Obj[1] = retObj.getFieldValueString(i, tableColNames[3]);
                    Obj[2] = "Y";
                    Obj[3] = "N";
                    Obj[4] = "N";
                    if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                        Obj[5] = "null";
                    } else {
                        Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[0]).equalsIgnoreCase("")) {
                        Obj[6] = "null";
                    } else {
                        Obj[6] = retObj.getFieldValueString(i, tableColNames[0]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[2]).equalsIgnoreCase("")) {
                        Obj[7] = "null";
                    } else {
                        Obj[7] = retObj.getFieldValueString(i, tableColNames[2]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[4]).equalsIgnoreCase("")) {
                        Obj[8] = "null";
                    } else {
                        Obj[8] = retObj.getFieldValueString(i, tableColNames[4]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[5]).equalsIgnoreCase("")) {
                        Obj[9] = "null";
                    } else {
                        Obj[9] = retObj.getFieldValueString(i, tableColNames[5]);
                    }

                    if (retObj.getFieldValueString(i, tableColNames[6]).equalsIgnoreCase("")) {
                        Obj[10] = "null";
                    } else {
                        Obj[10] = retObj.getFieldValueString(i, tableColNames[6]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[7]).equalsIgnoreCase("")) {
                        Obj[11] = "null";
                    } else {
                        Obj[11] = retObj.getFieldValueString(i, tableColNames[7]);
                    }

                    if (retObj.getFieldValueString(i, tableColNames[8]).equalsIgnoreCase("")) {
                        Obj[12] = "null";
                    } else {
                        Obj[12] = retObj.getFieldValueString(i, tableColNames[8]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[9]).equalsIgnoreCase("")) {
                        Obj[13] = "null";
                    } else {
                        Obj[13] = retObj.getFieldValueString(i, tableColNames[9]);
                    }

                    //modified for TABLE_DISP_NAME and TABLE_TOOLTIP_NAME
                    if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                        Obj[14] = "null";
                    } else {
                        Obj[14] = retObj.getFieldValueString(i, tableColNames[1]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                        Obj[15] = "null";
                    } else {
                        Obj[15] = retObj.getFieldValueString(i, tableColNames[1]);
                    }

                    //end
                    finalQuery = buildQuery(addUserSubFolderTablesForDimensionsQuery, Obj);
//                    subFoldTabId = this.subFolderTabId;

                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                    this.subFolderTabId += i;
                    Obj = new Object[16];
                    Obj[0] = "(select LAST_INSERT_ID(sub_folder_id) from PRG_USER_FOLDER_DETAIL order by 1 desc limit 1)";

                    Obj[1] = retObj.getFieldValueString(i, tableColNames[3]);
                    Obj[2] = "Y";
                    Obj[3] = "N";
                    Obj[4] = "N";
                    if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                        Obj[5] = "null";
                    } else {
                        Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[0]).equalsIgnoreCase("")) {
                        Obj[6] = "null";
                    } else {
                        Obj[6] = retObj.getFieldValueString(i, tableColNames[0]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[2]).equalsIgnoreCase("")) {
                        Obj[7] = "null";
                    } else {
                        Obj[7] = retObj.getFieldValueString(i, tableColNames[2]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[4]).equalsIgnoreCase("")) {
                        Obj[8] = "null";
                    } else {
                        Obj[8] = retObj.getFieldValueString(i, tableColNames[4]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[5]).equalsIgnoreCase("")) {
                        Obj[9] = "null";
                    } else {
                        Obj[9] = retObj.getFieldValueString(i, tableColNames[5]);
                    }

                    if (retObj.getFieldValueString(i, tableColNames[6]).equalsIgnoreCase("")) {
                        Obj[10] = "null";
                    } else {
                        Obj[10] = retObj.getFieldValueString(i, tableColNames[6]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[7]).equalsIgnoreCase("")) {
                        Obj[11] = "null";
                    } else {
                        Obj[11] = retObj.getFieldValueString(i, tableColNames[7]);
                    }

                    if (retObj.getFieldValueString(i, tableColNames[8]).equalsIgnoreCase("")) {
                        Obj[12] = "null";
                    } else {
                        Obj[12] = retObj.getFieldValueString(i, tableColNames[8]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[9]).equalsIgnoreCase("")) {
                        Obj[13] = "null";
                    } else {
                        Obj[13] = retObj.getFieldValueString(i, tableColNames[9]);
                    }

                    //modified for TABLE_DISP_NAME and TABLE_TOOLTIP_NAME
                    if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                        Obj[14] = "null";
                    } else {
                        Obj[14] = retObj.getFieldValueString(i, tableColNames[1]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                        Obj[15] = "null";
                    } else {
                        Obj[15] = retObj.getFieldValueString(i, tableColNames[1]);
                    }

                    //end
                    finalQuery = buildQuery(addUserSubFolderTablesForDimensionsQuery, Obj);
//                    subFoldTabId = this.subFolderTabId;

                } else {

                    subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");

                    Obj = new Object[17];
                    Obj[0] = String.valueOf(subFoldTabId);
                    Obj[1] = folderDtlId;

                    Obj[2] = retObj.getFieldValueString(i, tableColNames[3]);
                    Obj[3] = "Y";
                    Obj[4] = "N";
                    Obj[5] = "N";
                    Obj[6] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[7] = retObj.getFieldValueString(i, tableColNames[0]);
                    Obj[8] = retObj.getFieldValueString(i, tableColNames[2]);
                    Obj[9] = retObj.getFieldValueString(i, tableColNames[4]);
                    Obj[10] = retObj.getFieldValueString(i, tableColNames[5]);
                    Obj[11] = retObj.getFieldValueString(i, tableColNames[6]);
                    Obj[12] = retObj.getFieldValueString(i, tableColNames[7]);
                    Obj[13] = "";
                    Obj[13] = retObj.getFieldValueString(i, tableColNames[8]);
                    Obj[14] = retObj.getFieldValueString(i, tableColNames[9]);
                    //modified for TABLE_DISP_NAME and TABLE_TOOLTIP_NAME
                    Obj[15] = retObj.getFieldValueString(i, tableColNames[1]);

                    Obj[16] = retObj.getFieldValueString(i, tableColNames[1]);

                    //end
                    finalQuery = buildQuery(addUserSubFolderTablesForDimensionsQuery, Obj);
                }
                alist.add(finalQuery);

                alist = addUserSubFolderElements(alist, retObj.getFieldValueString(i, tableColNames[3]), folderDtlId, String.valueOf(subFoldTabId), grpId);
            }

            //added by susheela start. To insert in custom Drill
            String getAllMemRel = getResourceBundle().getString("getAllMemRel");
            Object grpObj[] = new Object[1];
            grpObj[0] = grpId;
            String fingetAllMemRel = buildQuery(getAllMemRel, grpObj);
            String addRoleCustomDrill = getResourceBundle().getString("getRoleCustomDrill");

            String insertRoleCustomDrillData = getResourceBundle().getString("addRoleCustomDrill");

            String finaddRoleCustomDrill = buildQuery(addRoleCustomDrill, grpObj);
            PbReturnObject roleDrillobj = execSelectSQL(finaddRoleCustomDrill);
            Vector insertable = new Vector();
            // for (int m = 0; m < roleDrillobj.getRowCount(); m++) {
            //    insertable.add(roleDrillobj.getFieldValueString(m, "MEMBER_ID"));
            // }
            String finALl = buildQuery(addRoleCustomDrill, grpObj);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" finALl --.."+finALl);
            PbReturnObject allDims = execSelectSQL(finALl);
            for (int m = 0; m < roleDrillobj.getRowCount(); m++) {
                insertable.add(roleDrillobj.getFieldValueString(m, "MEMBER_ID"));
            }
            ////.println("fingetAllMemRel\t" + fingetAllMemRel);
            PbReturnObject fingetAllMemRelOb = execSelectSQL(fingetAllMemRel);

            HashMap childs = new HashMap();
            HashMap dimMem = null;
            if (fingetAllMemRelOb.rowCount != 0) {
                String oldDimId = fingetAllMemRelOb.getFieldValueString(0, "DIM_ID");
                for (int n = 0; n < fingetAllMemRelOb.getRowCount(); n++) {

                    String memId = fingetAllMemRelOb.getFieldValueString(n, "MEM_ID");
                    String dimId = "";
                    String childId = "";
                    int next = n + 1;
                    String chElementId = fingetAllMemRelOb.getFieldValueString(n, "MEM_ID");
                    if (next < fingetAllMemRelOb.getRowCount()) {
                        dimId = fingetAllMemRelOb.getFieldValueString(next, "DIM_ID");
                         if (oldDimId.equalsIgnoreCase(dimId)) {
                             chElementId = fingetAllMemRelOb.getFieldValueString(next, "MEM_ID");
                          }
                    }
                    oldDimId = dimId;
                    childs.put(memId, chElementId);
                }
                dimMem = new HashMap();
                ArrayList det2 = new ArrayList();

                for (int n = 0; n < fingetAllMemRelOb.getRowCount(); n++) {

                    String memId = fingetAllMemRelOb.getFieldValueString(n, "MEM_ID");
                    String dimId = fingetAllMemRelOb.getFieldValueString(n, "DIM_ID");
                    String chElementId = fingetAllMemRelOb.getFieldValueString(n, "MEM_ID");
                    if (dimMem.containsKey(dimId)) {
                        det2 = (ArrayList) dimMem.get(dimId);
                        det2.add(memId);
                        dimMem.put(dimId, det2);
                    } else {
                        det2 = new ArrayList();
                        det2.add(memId);
                        dimMem.put(dimId, det2);
                    }
                }

            }

            for (int n = 0; n < roleDrillobj.getRowCount(); n++) {
                String dimId = roleDrillobj.getFieldValueString(n, "DIM_ID");
                String memId = roleDrillobj.getFieldValueString(n, "MEMBER_ID");
                if (childs.containsKey(memId)) {
                    String chId = childs.get(memId).toString();
                    if (dimMem.containsKey(dimId)) {
                        ArrayList det = (ArrayList) dimMem.get(dimId);
                        if (!det.contains(chId)) {
                            childs.put(memId, memId);
                        }
                    }
                }
            }
            ArrayList insertRoleDrillList = new ArrayList();
            for (int k = 0; k < insertable.size(); k++) {
                String memId = insertable.get(k).toString();
                String childId = memId;
                if (childs.containsKey(memId)) {
                    childId = childs.get(memId).toString();
                }
                Object insertOb[] = null;
                String finaddRoleCustomDrillData = "";
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    insertOb = new Object[4];
                    insertOb[0] = "null";
                    if (folderDtlId != null) {
                        insertOb[1] = folderDtlId;
                    } else {
                        insertOb[1] = "null";
                    }
                    if (memId != null) {
                        insertOb[2] = memId;
                    } else {
                        insertOb[2] = "null";
                    }
                    if (childId != null) {
                        insertOb[3] = childId;
                    } else {
                        insertOb[3] = "null";
                    }

                    finaddRoleCustomDrillData = buildQuery(insertRoleCustomDrillData, insertOb);
                } else {
                    insertOb = new Object[4];
                    insertOb[0] = "";
                    insertOb[1] = folderDtlId;
                    insertOb[2] = memId;
                    insertOb[3] = childId;
                    finaddRoleCustomDrillData = buildQuery(insertRoleCustomDrillData, insertOb);
                }

                insertRoleDrillList.add(finaddRoleCustomDrillData);
            }
            executeMultiple(insertRoleDrillList);
            //added by susheela over. To insert in custom Drill

        } //For Facts here
        else if (folderType.equalsIgnoreCase("Facts")) {
            Object[] Obj = new Object[1];
            Obj[0] = grpId;
            finalQuery = buildQuery(getBussFactsInfoByGrpIdQuery, Obj);
            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();

            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                    //int subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
//                    this.subFolderTabId += i;
                    Obj = new Object[11];
                    Obj[0] = "IDENT_CURRENT('PRG_USER_FOLDER_DETAIL')";
                    Obj[1] = retObj.getFieldValueString(i, tableColNames[0]);
                    Obj[2] = "N";
                    Obj[3] = "Y";
                    Obj[4] = "N";
                    Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[6] = "null";
                    Obj[7] = "null";
                    Obj[8] = "null";
                    //below 2 lines added for table_disp_name and table_tooltip_name
                    if (retObj.getFieldValueString(i, tableColNames[1]) == null) {
                        Obj[9] = "null";
                    } else {
                        Obj[9] = retObj.getFieldValueString(i, tableColNames[1]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[1]) == null) {
                        Obj[10] = "null";
                    } else {
                        Obj[10] = retObj.getFieldValueString(i, tableColNames[1]);
                    }

//                    subFoldTabId = this.subFolderTabId;
                } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                    //int subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
//                    this.subFolderTabId += i;
                    Obj = new Object[11];
                    Obj[0] = "(select LAST_INSERT_ID(sub_folder_id) from PRG_USER_FOLDER_DETAIL order by 1 desc limit 1)";
                    Obj[1] = retObj.getFieldValueString(i, tableColNames[0]);
                    Obj[2] = "N";
                    Obj[3] = "Y";
                    Obj[4] = "N";
                    Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[6] = "null";
                    Obj[7] = "null";
                    Obj[8] = "null";
                    //below 2 lines added for table_disp_name and table_tooltip_name
                    if (retObj.getFieldValueString(i, tableColNames[1]) == null) {
                        Obj[9] = "null";
                    } else {
                        Obj[9] = retObj.getFieldValueString(i, tableColNames[1]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[1]) == null) {
                        Obj[10] = "null";
                    } else {
                        Obj[10] = retObj.getFieldValueString(i, tableColNames[1]);
                    }

//                    subFoldTabId = this.subFolderTabId;
                } else {
                    subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
                    Obj = new Object[12];
                    Obj[0] = String.valueOf(subFoldTabId);
                    Obj[1] = folderDtlId;
                    Obj[2] = retObj.getFieldValueString(i, tableColNames[0]);
                    Obj[3] = "N";
                    Obj[4] = "Y";
                    Obj[5] = "N";
                    Obj[6] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[7] = "";
                    Obj[8] = "";
                    Obj[9] = "";
                    //below 2 lines added for table_disp_name and table_tooltip_name
                    Obj[10] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[11] = retObj.getFieldValueString(i, tableColNames[1]);
                }

                finalQuery = buildQuery(addUserSubFolderTablesQuery, Obj);
                ////.println("finalQuery3\t" + finalQuery);
                alist.add(finalQuery);
                alist = addUserSubFolderElementsForFacts(alist, retObj.getFieldValueString(i, tableColNames[0]), folderDtlId, String.valueOf(subFoldTabId));
            }

        } //in buckets only 9 values are bound but there are 11 binds in Query
        //will this condition be ever executed? - Arun
        else if (folderType.equalsIgnoreCase("Buckets")) {
            Object[] Obj = new Object[1];
            Obj[0] = grpId;
            finalQuery = buildQuery(getBussBucketInfoByGrpIdQuery, Obj);
            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {

                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                    subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
                    Obj = new Object[11];
                    Obj[0] = "IDENT_CURRENT('PRG_USER_FOLDER_DETAIL')";
                    Obj[1] = retObj.getFieldValueString(i, tableColNames[0]);
                    Obj[2] = "N";
                    Obj[3] = "N";
                    Obj[4] = "Y";
                    Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[6] = "null";
                    Obj[7] = "null";
                    Obj[8] = "null";
                    //below 2 lines added for table_disp_name and table_tooltip_name
                    if (retObj.getFieldValueString(i, tableColNames[1]) == null) {
                        Obj[9] = "null";
                    } else {
                        Obj[9] = retObj.getFieldValueString(i, tableColNames[1]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[1]) == null) {
                        Obj[10] = "null";
                    } else {
                        Obj[10] = retObj.getFieldValueString(i, tableColNames[1]);
                    }

                    finalQuery = buildQuery(addUserSubFolderTablesQuery, Obj);
                    alist.add(finalQuery);
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                    subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
                    Obj = new Object[11];
                    Obj[0] = "(select LAST_INSERT_ID(sub_folder_id) from PRG_USER_FOLDER_DETAIL order by 1 desc limit 1)";
                    Obj[1] = retObj.getFieldValueString(i, tableColNames[0]);
                    Obj[2] = "N";
                    Obj[3] = "N";
                    Obj[4] = "Y";
                    Obj[5] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[6] = "null";
                    Obj[7] = "null";
                    Obj[8] = "null";
                    //below 2 lines added for table_disp_name and table_tooltip_name
                    if (retObj.getFieldValueString(i, tableColNames[1]) == null) {
                        Obj[9] = "null";
                    } else {
                        Obj[9] = retObj.getFieldValueString(i, tableColNames[1]);
                    }
                    if (retObj.getFieldValueString(i, tableColNames[1]) == null) {
                        Obj[10] = "null";
                    } else {
                        Obj[10] = retObj.getFieldValueString(i, tableColNames[1]);
                    }

                    finalQuery = buildQuery(addUserSubFolderTablesQuery, Obj);
                   alist.add(finalQuery);
                } else {
                    subFoldTabId = getSequenceNumber("select PRG_USER_SUB_FOLDER_TABLES_SEQ.NEXTVAL from dual");
                    Obj = new Object[10];

                    Obj[0] = String.valueOf(subFoldTabId);
                    Obj[1] = folderDtlId;

                    Obj[2] = retObj.getFieldValueString(i, tableColNames[0]);
                    Obj[3] = "N";
                    Obj[4] = "N";
                    Obj[5] = "Y";
                    Obj[6] = retObj.getFieldValueString(i, tableColNames[1]);
                    Obj[7] = "";
                    Obj[8] = "";
                    Obj[9] = "";
                    finalQuery = buildQuery(addUserSubFolderTablesQuery, Obj);
                    alist.add(finalQuery);
                }

                alist = addUserSubFolderElements(alist, retObj.getFieldValueString(i, tableColNames[0]), folderDtlId, String.valueOf(subFoldTabId), grpId);
            }
        }

        return alist;

    }
    /*
     * This method is for inserting a records into user folder detasub folder
     * elementsils and returns : ArrayList which contains queries to be executed
     */

    public ArrayList addUserSubFolderElements(ArrayList alist, String bussTableId, String folderDtlId, String subFoldTabId, String grpId) throws Exception {
        String addUserSubFolderElementsQuery = getResourceBundle().getString("addUserSubFolderElements");
        String finalQuery;
        String getUserSubFolderElementsQuery = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            getUserSubFolderElementsQuery = getResourceBundle().getString("getUserSubFolderElements");
        }
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//            this.subFolderEleId = this.subFolderEleId + 1;
            Object[] Obj = new Object[4];
            Obj[0] = "IDENT_CURRENT('PRG_USER_FOLDER_DETAIL')";//
            Obj[1] = "IDENT_CURRENT('PRG_USER_SUB_FOLDER_TABLES')";
            Obj[2] = "IDENT_CURRENT('PRG_USER_SUB_FOLDER_ELEMENTS')";//
            Obj[3] = bussTableId;
            finalQuery = buildQuery(addUserSubFolderElementsQuery, Obj);
            alist.add(finalQuery);
        } //        LAST_INSERT_ID(sub_folder_id) from PRG_USER_FOLDER_DETAIL
        else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//            this.subFolderEleId = this.subFolderEleId + 1;
            //  PbReturnObject pbref = execSelectSQL("select LAST_INSERT_ID(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1");
            PbReturnObject pbref = null;
            Object[] Obj = new Object[1];
            // Obj[0] = "(select LAST_INSERT_ID(sub_folder_id) from PRG_USER_FOLDER_DETAIL order by 1 desc limit 1)";//
            //Obj[1] = "(select LAST_INSERT_ID(SUB_FOLDER_TAB_ID) from PRG_USER_SUB_FOLDER_TABLES order by 1 desc limit 1)";
            //Obj[2] = refid ;
            // "(select LAST_INSERT_ID(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1)";//
            Obj[0] = bussTableId;
            finalQuery = buildQuery(getUserSubFolderElementsQuery, Obj);

            pbref = execSelectSQL(finalQuery);

            for (int k = 0; k < pbref.getRowCount(); k++) {
                Obj = new Object[13];
                Obj[0] = "(select LAST_INSERT_ID(sub_folder_id) from PRG_USER_FOLDER_DETAIL order by 1 desc limit 1)";//
                Obj[1] = pbref.getFieldValue(k, 0);
                Obj[2] = pbref.getFieldValue(k, 1);
                Obj[3] = pbref.getFieldValue(k, 2);
                Obj[4] = pbref.getFieldValue(k, 3);
                Obj[5] = pbref.getFieldValue(k, 4);
                Obj[6] = pbref.getFieldValue(k, 5);
                Obj[7] = "(select LAST_INSERT_ID(SUB_FOLDER_TAB_ID) from PRG_USER_SUB_FOLDER_TABLES order by 1 desc limit 1)";
                Obj[8] = String.valueOf(refId + 1);
                Obj[9] = pbref.getFieldValue(k, 6);
                if (pbref.getFieldValue(k, 8) == null || pbref.getFieldValue(k, 7).equals("")) {
                    Obj[10] = null;
                } else {
                    Obj[10] = pbref.getFieldValue(k, 7);
                }
                Obj[11] = 'Y';
                if (pbref.getFieldValue(k, 8) == null) {
                    Obj[12] = null;
                } else {
                    Obj[12] = pbref.getFieldValue(k, 8);
                }
                finalQuery = buildQuery(addUserSubFolderElementsQuery, Obj);
                logger.info("addUserSubFolderElementsQuery---" + finalQuery);
                alist.add(finalQuery);
                refId++;
            }
        } else {
            Object[] Obj = new Object[3];
            Obj[0] = folderDtlId;
            Obj[1] = subFoldTabId;
            Obj[2] = bussTableId;
            finalQuery = buildQuery(addUserSubFolderElementsQuery, Obj);
            alist.add(finalQuery);
        }
        logger.info("finalQuery5\t" + finalQuery);

        //added by susheela to insert the table levels start
        // BusinessTablePaths bt = new BusinessTablePaths();
        // to insert in the business tables path maps
        // bt.getPathsToInsertForFact(bussTableId, grpIds);
        // HashMap hm = bt.getpathFromBusinessTableForTLevels(bussTableId);
        // to insert in the business tables levels
        // bt.AddParentChildLevels(hm);
        return alist;

    }

    public ArrayList addUserSubFolderElementsForFacts(ArrayList alist, String bussTableId, String folderDtlId, String subFoldTabId) throws Exception {
        PbReturnObject getdetspbro;
        String getElementsInfoQuery;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            getElementsInfoQuery = getResourceBundle().getString("getElementsInfoMysql");
        } else {
            getElementsInfoQuery = getResourceBundle().getString("getElementsInfo");
        }
        String addUserSubFolderElementsForFactsQuery = getResourceBundle().getString("addUserSubFolderElementsForFacts");
        String insertExtraElementsForFactsQuery = getResourceBundle().getString("insertExtraElementsForFacts");

        PbReturnObject retObj = null;
        String finalQuery = "";
        String[] tableColNames = null;

        String refid = "";
        int seq = 0;

        Object[] Obj = new Object[3];
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            Obj[0] = "IDENT_CURRENT('PRG_USER_FOLDER_DETAIL')";
            Obj[1] = "IDENT_CURRENT('PRG_USER_SUB_FOLDER_TABLES')";
            Obj[2] = bussTableId;
        } else {
            Obj[0] = folderDtlId;
            Obj[1] = subFoldTabId;
            Obj[2] = bussTableId;
        }

        finalQuery = buildQuery(getElementsInfoQuery, Obj);
        ////.println("addUserSubFolderElementsForFacts is " + finalQuery);
        retObj = execSelectSQL(finalQuery);

        tableColNames = retObj.getColumnNames();
        /*
         * finalQuery = " Update PRG_USER_SUB_FOLDER_ELEMENTS " + " set
         * ref_element_id = element_id " + " where SUB_FOLDER_ID =
         * IDENT_CURRENT('PRG_USER_FOLDER_DETAIL') " + " and SUB_FOLDER_TAB_ID =
         * IDENT_CURRENT('PRG_USER_SUB_FOLDER_TABLES') " + " and buss_table_id =
         * " +bussTableId;
         alist.add(finalQuery);
         */
        //for(int i=0;i<tableColNames.length;i++){
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(tableColNames[i]);
        //}

        //ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME, USER_COL_NAME, USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID,
        //REF_ELEMENT_ID, REF_ELEMENT_TYPE
        for (int i = 0; i < retObj.getRowCount(); i++) {
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Index is " + i);
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                seq = getCurrentSeqSqlServer("PRG_USER_SUB_FOLDER_ELEMENTS");
                refid = String.valueOf(seq + i);

                Obj = new Object[14];
//            Obj[0] = String.valueOf(seq);
                if (retObj.getFieldValueString(i, tableColNames[0]).equalsIgnoreCase("")) {
                    Obj[0] = "null";
                } else {
                    Obj[0] = "IDENT_CURRENT('PRG_USER_FOLDER_DETAIL') ";
                }
                if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                    Obj[1] = "null";
                } else {
                    Obj[1] = retObj.getFieldValueString(i, tableColNames[1]);
                }
                if (retObj.getFieldValueString(i, tableColNames[2]).equalsIgnoreCase("")) {
                    Obj[2] = "";
                } else {
                    Obj[2] = retObj.getFieldValueString(i, tableColNames[2]);
                }
                if (retObj.getFieldValueString(i, tableColNames[3]).equalsIgnoreCase("")) {
                    Obj[3] = "null";
                } else {
                    Obj[3] = retObj.getFieldValueString(i, tableColNames[3]);
                }
                if (retObj.getFieldValueString(i, tableColNames[4]).equalsIgnoreCase("")) {
                    Obj[4] = "null";
                } else {
                    Obj[4] = retObj.getFieldValueString(i, tableColNames[4]);
                }
                if (retObj.getFieldValueString(i, tableColNames[11]).equalsIgnoreCase("")) {
                    Obj[5] = "null";
                } else {
                    Obj[5] = retObj.getFieldValueString(i, tableColNames[11]);
                }

                if (retObj.getFieldValueString(i, tableColNames[6]).equalsIgnoreCase("")) {
                    Obj[6] = "null";
                } else {
                    if (retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("VARCHAR")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("VARCHAR2")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("NVARCHAR")) {
                        Obj[6] = "VARCHAR";
                    } else if (retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("NUMERIC")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("NUMBER")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("INT")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("DOUBLE")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("FLOAT")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("INTEGER")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("BIGINT")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("SMALLINT")) {
                        Obj[6] = "NUMBER";
                    } else {
                        Obj[6] = retObj.getFieldValueString(i, tableColNames[6]);
                    }
                }
                if (retObj.getFieldValueString(i, tableColNames[7]).equalsIgnoreCase("")) {
                    Obj[7] = "null";
                } else {
                    //modified by Nazneen
//                    Obj[7] = retObj.getFieldValueString(i, tableColNames[7]) ;
                    Obj[7] = "IDENT_CURRENT('PRG_USER_SUB_FOLDER_TABLES') ";
                }

                Obj[8] = refid;

                if (retObj.getFieldValueString(i, tableColNames[9]).equalsIgnoreCase(folderDtlId)) {
                    Obj[9] = "null";
                } else {
                    Obj[9] = retObj.getFieldValueString(i, tableColNames[9]);
                }
                if (retObj.getFieldValueString(i, tableColNames[8]).equalsIgnoreCase("")) {
                    Obj[10] = "null";
                } else {
                    Obj[10] = retObj.getFieldValueString(i, tableColNames[8]);
                }

                // Obj[12] = retObj.getFieldValueString(i, tableColNames[10]).replace(",","~");
                if (retObj.getFieldValueString(i, tableColNames[10]).equalsIgnoreCase("")) {
                    Obj[11] = "null";
                } else {
                    Obj[11] = retObj.getFieldValueString(i, tableColNames[10]);
                }
                if (retObj.getFieldValueString(i, tableColNames[12]).equalsIgnoreCase("")) {
                    Obj[12] = "null";
                } else {
                    Obj[12] = retObj.getFieldValueString(i, tableColNames[12]).replace("'", "''");
                }
                if (retObj.getFieldValueString(i, tableColNames[13]).equalsIgnoreCase("")) {
                    Obj[13] = "null";
                } else {
                    Obj[13] = retObj.getFieldValueString(i, tableColNames[13]);
                }

                finalQuery = buildQuery(addUserSubFolderElementsForFactsQuery, Obj);

            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                seq = getSequenceNumber("select Last_insert_id(ELEMENT_ID) from PRG_USER_SUB_FOLDER_ELEMENTS order by 1 desc limit 1");
                //refId = String.valueOf(seq + i);

                Obj = new Object[14];
//            Obj[0] = String.valueOf(seq);
                if (retObj.getFieldValueString(i, tableColNames[0]).equalsIgnoreCase("")) {
                    Obj[0] = "null";
                } else {
                    Obj[0] = "(select Last_Insert_Id(sub_folder_id) from PRG_USER_FOLDER_DETAIL order by 1 desc limit 1) ";
                }
                if (retObj.getFieldValueString(i, tableColNames[1]).equalsIgnoreCase("")) {
                    Obj[1] = "null";
                } else {
                    Obj[1] = retObj.getFieldValueString(i, tableColNames[1]);
                }
                if (retObj.getFieldValueString(i, tableColNames[2]).equalsIgnoreCase("")) {
                    Obj[2] = "";
                } else {
                    Obj[2] = retObj.getFieldValueString(i, tableColNames[2]);
                }
                if (retObj.getFieldValueString(i, tableColNames[3]).equalsIgnoreCase("")) {
                    Obj[3] = "null";
                } else {
                    Obj[3] = retObj.getFieldValueString(i, tableColNames[3]);
                }
                if (retObj.getFieldValueString(i, tableColNames[4]).equalsIgnoreCase("")) {
                    Obj[4] = "null";
                } else {
                    Obj[4] = retObj.getFieldValueString(i, tableColNames[4]);
                }
                if (retObj.getFieldValueString(i, tableColNames[11]).equalsIgnoreCase("")) {
                    Obj[5] = "null";
                } else {
                    Obj[5] = retObj.getFieldValueString(i, tableColNames[11]);
                }

                if (retObj.getFieldValueString(i, tableColNames[6]).equalsIgnoreCase("")) {
                    Obj[6] = "null";
                } else {
                    if (retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("VARCHAR")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("VARCHAR2")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("NVARCHAR")) {
                        Obj[6] = "VARCHAR";
                    } else if (retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("NUMERIC")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("NUMBER")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("INT")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("DOUBLE")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("FLOAT")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("INTEGER")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("BIGINT")
                            || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("SMALLINT")) {
                        Obj[6] = "NUMBER";
                    } else {
                        Obj[6] = retObj.getFieldValueString(i, tableColNames[6]);
                    }
                }
                if (retObj.getFieldValueString(i, tableColNames[7]).equalsIgnoreCase("")) {
                    Obj[7] = "null";
                } else {
                    //modified by Nazneen
//                    Obj[7] = retObj.getFieldValueString(i, tableColNames[7]) ;
                    Obj[7] = "(select Last_insert_id(SUB_FOLDER_TAB_ID) from PRG_USER_SUB_FOLDER_TABLES order by 1 desc limit 1) ";
                }

                Obj[8] = refId + 1;
                refId++;
                if (retObj.getFieldValueString(i, tableColNames[9]).equalsIgnoreCase(folderDtlId)) {
                    Obj[9] = "null";
                } else {
                    Obj[9] = retObj.getFieldValueString(i, tableColNames[9]);
                }
                if (retObj.getFieldValueString(i, tableColNames[8]).equalsIgnoreCase("")) {
                    Obj[10] = "null";
                } else {
                    Obj[10] = retObj.getFieldValueString(i, tableColNames[8]);
                }

                // Obj[12] = retObj.getFieldValueString(i, tableColNames[10]).replace(",","~");
                if (retObj.getFieldValueString(i, tableColNames[10]).equalsIgnoreCase("")) {
                    Obj[11] = "null";
                } else {
                    Obj[11] = retObj.getFieldValueString(i, tableColNames[10]);
                }
                if (retObj.getFieldValueString(i, tableColNames[12]).equalsIgnoreCase("")) {
                    Obj[12] = "null";
                } else {
                    Obj[12] = retObj.getFieldValueString(i, tableColNames[12]).replace("'", "''");
                }
                if (retObj.getFieldValueString(i, tableColNames[13]).equalsIgnoreCase("")) {
                    Obj[13] = "null";
                } else {
                    Obj[13] = retObj.getFieldValueString(i, tableColNames[13]);
                }

                finalQuery = buildQuery(addUserSubFolderElementsForFactsQuery, Obj);
            } else {
                seq = getSequenceNumber("select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL from dual");
                refid = String.valueOf(seq);

                Obj = new Object[15];
                Obj[0] = String.valueOf(seq);
                Obj[1] = retObj.getFieldValueString(i, tableColNames[0]);
                Obj[2] = retObj.getFieldValueString(i, tableColNames[1]);
                Obj[3] = retObj.getFieldValueString(i, tableColNames[2]);
                Obj[4] = retObj.getFieldValueString(i, tableColNames[3]);
                Obj[5] = retObj.getFieldValueString(i, tableColNames[4]);
                Obj[6] = retObj.getFieldValueString(i, tableColNames[11]);
                Obj[7] = retObj.getFieldValueString(i, tableColNames[6]);
                Obj[8] = retObj.getFieldValueString(i, tableColNames[7]);
                Obj[9] = refid;
                Obj[10] = retObj.getFieldValueString(i, tableColNames[9]);
                Obj[11] = retObj.getFieldValueString(i, tableColNames[8]);
                // Obj[12] = retObj.getFieldValueString(i, tableColNames[10]).replace(",","~");
                Obj[12] = retObj.getFieldValueString(i, tableColNames[10]);
                Obj[13] = retObj.getFieldValueString(i, tableColNames[12]).replace("'", "''");
                Obj[14] = retObj.getFieldValueString(i, tableColNames[13]).replace("'", "''");

                finalQuery = buildQuery(addUserSubFolderElementsForFactsQuery, Obj);
            }

            // if(!retObj.getFieldValueString(i, tableColNames[10]).equalsIgnoreCase("")){
            // String referredElements=retObj.getFieldValueString(i, tableColNames[10]);
            // }
            ////.println("finalQuery6\t" + finalQuery);
            alist.add(finalQuery);
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                finalQuery = " Update PRG_USER_SUB_FOLDER_ELEMENTS "
                        + " set ref_element_id = element_id  "
                        + " where REF_ELEMENT_TYPE = 1 and element_id = IDENT_CURRENT('PRG_USER_SUB_FOLDER_ELEMENTS') ";
                alist.add(finalQuery);

            }
            if (retObj.getFieldValueString(i, tableColNames[8]) != null && !("".equalsIgnoreCase(retObj.getFieldValueString(i, tableColNames[8]))) && (retObj.getFieldValueString(i, tableColNames[6]) != null && !("".equalsIgnoreCase(retObj.getFieldValueString(i, tableColNames[6]))))) {
                //if (retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("CALCULATED")
                //      || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("Number")
                //      || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("NUMERIC")
                //      || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("FLOAT")
                //      || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("DOUBLE")
                //     || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("INT")
                //     || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("INTEGER")
                //     || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("VARCHAR")
                //     || retObj.getFieldValueString(i, tableColNames[6]).trim().equalsIgnoreCase("VARCHAR2")) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    Obj = new Object[1];
                    //Obj[0] = String.valueOf(seq);
                    Obj[0] = "IDENT_CURRENT('PRG_USER_SUB_FOLDER_ELEMENTS')";
                    finalQuery = buildQuery(insertExtraElementsForFactsQuery, Obj);
                    logger.info("insertExtraElementsForFactsQuery finalQuery7" + finalQuery);
                    alist.add(finalQuery);

                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    Obj = new Object[1];
                    Obj[0] = "select max(element_id) from PRG_USER_SUB_FOLDER_ELEMENTS";

                } else {

                    seq = getSequenceNumber("select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL from dual");
                    Obj = new Object[1];
                    Obj[0] = String.valueOf(seq);
                    Obj[0] = refid;
                    finalQuery = buildQuery(insertExtraElementsForFactsQuery, Obj);

                    alist.add(finalQuery);
                }

                // }
            }
        }

        return alist;

    }

    public boolean copyUserFolder(String folderName, String folderDesc, String oldFolderId) throws Exception {
        ArrayList alist2 = new ArrayList();

        String addUserFolderQuery = getResourceBundle().getString("copyUserFolder");
        String getdets = "select FOLDER_CREATED_ON, FOLDER_CREATED_BY, FOLDER_UPDATED_ON, FOLDER_UPDATED_BY, GRP_ID from PRG_USER_FOLDER where folder_id=" + oldFolderId;
        PbReturnObject getdetspbro = execSelectSQL(getdets);
        String grpId = String.valueOf(getdetspbro.getFieldValueInt(0, 4));
        String folderidq = "select PRG_USER_FOLDER_SEQ.NEXTVAL from dual";
        PbReturnObject folderpbro = execSelectSQL(folderidq);
        String folderId = String.valueOf(folderpbro.getFieldValueInt(0, 0));
        String finalQuery = "";
        boolean result = false;
        Object[] Obj = new Object[4];
        Obj[0] = folderId;
        Obj[1] = folderName;
        Obj[2] = folderDesc;
        Obj[3] = grpId;
        try {

            finalQuery = buildQuery(addUserFolderQuery, Obj);
            ////////////////////////////////////////////////////////////////////////////////////.println.println("final Query==" + finalQuery);
            alist2.add(finalQuery);
            alist2 = copyUserFolderDetails(alist2, folderId, grpId, oldFolderId);
            result = executeMultiple(alist2);
            ////////////////////////////////////////////////////////////////////////////////////.println.println("result-" + result);
            copyUserFoldertabs(alist2, folderId, grpId, oldFolderId);
            copyUserFolderReports(folderId, oldFolderId);

            return result;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return result;
        }
    }

    public void copyUserFolderReports(String folderId, String oldFolderId) throws Exception {
        String getCommonFieldsFromTwoFolders = getResourceBundle().getString("getCommonFieldsFromTwoFolders");
        String finalQuery = "";
        String newReportId = "";
        PbReturnObject pbro = null;
        Object[] Obj = new Object[2];
        Obj[0] = oldFolderId;
        Obj[1] = folderId;
        finalQuery = buildQuery(getCommonFieldsFromTwoFolders, Obj);
        pbro = execSelectSQL(finalQuery);
        String repEleIds = "";
        int repElementIdsCount = 0;
        if (pbro != null) {
            if (pbro.getRowCount() > 0) {
                String getReportIdsForFolder = getResourceBundle().getString("getReportIdsForFolder");
                Obj = new Object[1];
                Obj[0] = oldFolderId;
                finalQuery = buildQuery(getReportIdsForFolder, Obj);
                //////.println("finalQuery==1111==="+finalQuery);
                PbReturnObject pbro1 = execSelectSQL(finalQuery);
                if (pbro1.getRowCount() > 0) {
                    String getReportElementIds = "";
                    String reportId = "";
                    for (int i = 0; i < pbro1.getRowCount(); i++) {
                        getReportElementIds = getResourceBundle().getString("getReportElementIds");
                        reportId = String.valueOf(pbro1.getFieldValueInt(i, 0));
                        Obj = new Object[3];
                        Obj[0] = reportId;
                        Obj[1] = reportId;
                        Obj[2] = reportId;
                        finalQuery = buildQuery(getReportElementIds, Obj);
                        //  //////.println("finalQuery==2221==="+finalQuery);
                        PbReturnObject pbroRepEleIds = execSelectSQL(finalQuery);

                        if (pbroRepEleIds.getRowCount() > 0) {
                            repElementIdsCount = pbroRepEleIds.getRowCount();

                            for (int j = 0; j < pbroRepEleIds.getRowCount(); j++) {
                                if (j == 0) {
                                    repEleIds = String.valueOf(pbroRepEleIds.getFieldValueInt(j, 0));
                                } else {
                                    repEleIds += "," + String.valueOf(pbroRepEleIds.getFieldValueInt(j, 0));
                                }

                            }

                        }

                        if (!repEleIds.equalsIgnoreCase("")) {
                            String getCommonelesFromRepFolder = getResourceBundle().getString("getCommonelesFromRepFolder");
                            Obj = new Object[3];
                            Obj[0] = oldFolderId;
                            Obj[1] = folderId;
                            Obj[2] = repEleIds;
                            finalQuery = buildQuery(getCommonelesFromRepFolder, Obj);
                            //////.println("finalQuery==333==="+finalQuery);
                            PbReturnObject pbroRepFolderEleIds = execSelectSQL(finalQuery);

                            if (pbroRepFolderEleIds.getRowCount() > 0) {
                                ArrayList list = new ArrayList();
                                PbReturnObject pbronew = null;

                                String colNames[] = null;
                                //////.println("repElementIdsCount=="+repElementIdsCount+"==== pbroRepFolderEleIds.getRowCount()=="+ pbroRepFolderEleIds.getRowCount());
                                if (repElementIdsCount == pbroRepFolderEleIds.getRowCount()) {
                                    String reportIdsql = "select PRG_AR_REPORT_MASTER_SEQ.nextval from dual";
                                    PbReturnObject pbroRepId = execSelectSQL(reportIdsql);
                                    newReportId = String.valueOf(pbroRepId.getFieldValueInt(0, 0));
                                    String addReportMaster = getResourceBundle().getString("addReportMaster");
                                    Obj = new Object[2];
                                    Obj[0] = newReportId;
                                    Obj[1] = reportId;
                                    finalQuery = buildQuery(addReportMaster, Obj);
                                    //////.println("finalQuery===="+finalQuery);
                                    list.add(finalQuery);
                                    String addReportDetails = getResourceBundle().getString("addReportDetails");
                                    Obj = new Object[3];
                                    Obj[0] = newReportId;
                                    Obj[1] = folderId;
                                    Obj[2] = reportId;
                                    finalQuery = buildQuery(addReportDetails, Obj);
                                    //////.println("finalQuery===="+finalQuery);
                                    list.add(finalQuery);
                                    String addReportParamDetails = getResourceBundle().getString("addReportParamDetails");
                                    Obj = new Object[2];
                                    Obj[0] = newReportId;
                                    Obj[1] = reportId;
                                    finalQuery = buildQuery(addReportParamDetails, Obj);
                                    //////.println("finalQuery===="+finalQuery);
                                    list.add(finalQuery);
                                    String addReportQryDetails = getResourceBundle().getString("addReportQryDetails");
                                    Obj = new Object[3];
                                    Obj[0] = folderId;
                                    Obj[1] = newReportId;
                                    Obj[2] = reportId;
                                    finalQuery = buildQuery(addReportQryDetails, Obj);
                                    //////.println("finalQuery===="+finalQuery);
                                    list.add(finalQuery);

                                    String getTimemaster = "select * from PRG_AR_REPORT_TIME where report_id=" + reportId;
                                    pbronew = execSelectSQL(getTimemaster);
                                    colNames = pbronew.getColumnNames();
                                    for (int k = 0; k < pbronew.getRowCount(); k++) {
                                        finalQuery = "select PRG_AR_REPORT_TIME_SEQ.nextval from dual";
                                        PbReturnObject pbroReptimeId = execSelectSQL(finalQuery);
                                        String newReportTimeId = String.valueOf(pbroReptimeId.getFieldValueInt(0, 0));
                                        String addReportTimeMaster = getResourceBundle().getString("addReportTimeMaster");
                                        Obj = new Object[4];
                                        Obj[0] = newReportTimeId;
                                        Obj[1] = pbronew.getFieldValueString(k, colNames[1]);
                                        Obj[2] = pbronew.getFieldValueString(k, colNames[2]);
                                        Obj[3] = newReportId;
                                        finalQuery = buildQuery(addReportTimeMaster, Obj);
                                        //////.println("finalQuery===="+finalQuery);
                                        list.add(finalQuery);
                                        finalQuery = "select * from PRG_AR_REPORT_TIME_DETAIL where REP_TIME_ID=" + pbronew.getFieldValueInt(k, colNames[0]);
                                        PbReturnObject timedetspbro = execSelectSQL(finalQuery);
                                        String addReportTimeMasterdetails = getResourceBundle().getString("addReportTimeMasterdetails");
                                        for (int k1 = 0; k1 < timedetspbro.getRowCount(); k1++) {
                                            Obj = new Object[5];
                                            Obj[0] = newReportTimeId;
                                            Obj[1] = timedetspbro.getFieldValueString(k1, 2);
                                            Obj[2] = timedetspbro.getFieldValueString(k1, 3);
                                            Obj[3] = timedetspbro.getFieldValueInt(k1, 4);
                                            Obj[4] = timedetspbro.getFieldValueInt(k1, 5);
                                            finalQuery = buildQuery(addReportTimeMasterdetails, Obj);
                                            //////.println("finalQuery===="+finalQuery);
                                            list.add(finalQuery);

                                        }
                                    }

                                    String getviewmaster = "select * from PRG_AR_REPORT_VIEW_BY_MASTER where report_id=" + reportId;
                                    pbronew = execSelectSQL(getviewmaster);
                                    colNames = pbronew.getColumnNames();
                                    for (int k = 0; k < pbronew.getRowCount(); k++) {
                                        finalQuery = "select PRG_AR_REP_VIEW_BY_MASTER_SEQ.nextval from dual";
                                        PbReturnObject pbroviewId = execSelectSQL(finalQuery);
                                        String newReportviewId = String.valueOf(pbroviewId.getFieldValueInt(0, 0));
                                        String addReportViewMaster = getResourceBundle().getString("addReportViewMaster");
                                        Obj = new Object[7];
                                        Obj[0] = newReportviewId;
                                        Obj[1] = newReportId;
                                        Obj[2] = pbronew.getFieldValueInt(k, colNames[2]);
                                        Obj[3] = pbronew.getFieldValueString(k, colNames[3]);
                                        Obj[4] = pbronew.getFieldValueInt(k, colNames[4]);
                                        Obj[5] = pbronew.getFieldValueInt(k, colNames[5]);
                                        Obj[6] = pbronew.getFieldValueString(k, colNames[6]);
                                        finalQuery = buildQuery(addReportViewMaster, Obj);
                                        //////.println("finalQuery===="+finalQuery);
                                        list.add(finalQuery);
                                        finalQuery = "select * from PRG_AR_REPORT_VIEW_BY_DETAILS where VIEW_BY_ID=" + pbronew.getFieldValueInt(k, colNames[0]);
                                        PbReturnObject timedetspbro = execSelectSQL(finalQuery);
                                        String addReportViewDets = getResourceBundle().getString("addReportViewDets");
                                        for (int k1 = 0; k1 < timedetspbro.getRowCount(); k1++) {
                                            Obj = new Object[3];
                                            Obj[0] = timedetspbro.getFieldValueInt(k1, 1);
                                            Obj[1] = newReportviewId;
                                            Obj[2] = timedetspbro.getFieldValueString(k1, 3);

                                            finalQuery = buildQuery(addReportViewDets, Obj);
                                            //////.println("finalQuery===="+finalQuery);
                                            list.add(finalQuery);

                                        }
                                    }

                                    String getgrpmaster = "select * from PRG_AR_GRAPH_MASTER where report_id=" + reportId;
                                    pbronew = execSelectSQL(getgrpmaster);
                                    colNames = pbronew.getColumnNames();
                                    for (int k = 0; k < pbronew.getRowCount(); k++) {
                                        finalQuery = "select PRG_AR_GRAPH_MASTER_SEQ.nextval from dual";
                                        PbReturnObject pbrogrpId = execSelectSQL(finalQuery);
                                        String newReportGrpId = String.valueOf(pbrogrpId.getFieldValueInt(0, 0));
                                        String addGraphmaster = getResourceBundle().getString("addGraphmaster");
                                        Obj = new Object[28];
                                        Obj[0] = newReportGrpId;
                                        Obj[1] = newReportId;
                                        Obj[2] = pbronew.getFieldValueString(k, colNames[2]);
                                        Obj[3] = pbronew.getFieldValueString(k, colNames[3]);
                                        Obj[4] = pbronew.getFieldValueInt(k, colNames[4]);
                                        Obj[5] = pbronew.getFieldValueInt(k, colNames[5]);
                                        Obj[6] = pbronew.getFieldValueInt(k, colNames[6]);
                                        Obj[7] = pbronew.getFieldValueString(k, colNames[7]);
                                        Obj[8] = pbronew.getFieldValueString(k, colNames[8]);
                                        Obj[9] = pbronew.getFieldValueString(k, colNames[9]);
                                        Obj[10] = pbronew.getFieldValueString(k, colNames[10]);
                                        Obj[11] = pbronew.getFieldValueString(k, colNames[11]);
                                        Obj[12] = pbronew.getFieldValueString(k, colNames[12]);
                                        Obj[13] = pbronew.getFieldValueInt(k, colNames[13]);
                                        Obj[14] = pbronew.getFieldValueString(k, colNames[14]);
                                        Obj[15] = pbronew.getFieldValueString(k, colNames[15]);
                                        Obj[16] = pbronew.getFieldValueString(k, colNames[16]);
                                        Obj[17] = pbronew.getFieldValueString(k, colNames[17]);
                                        Obj[18] = pbronew.getFieldValueInt(k, colNames[18]);
                                        Obj[19] = pbronew.getFieldValueString(k, colNames[19]);
                                        Obj[20] = pbronew.getFieldValueString(k, colNames[20]);
                                        Obj[21] = pbronew.getFieldValueString(k, colNames[21]);
                                        Obj[22] = pbronew.getFieldValueString(k, colNames[22]);
                                        Obj[23] = pbronew.getFieldValueString(k, colNames[23]);
                                        Obj[24] = pbronew.getFieldValueString(k, colNames[24]);
                                        Obj[25] = pbronew.getFieldValueString(k, colNames[25]);
                                        Obj[26] = pbronew.getFieldValueString(k, colNames[26]);
                                        Obj[27] = pbronew.getFieldValueString(k, colNames[27]);

                                        finalQuery = buildQuery(addGraphmaster, Obj);
                                        list.add(finalQuery);
                                        //////.println("finalQuery===="+finalQuery);
                                        finalQuery = "select * from PRG_AR_GRAPH_DETAILS where GRAPH_ID=" + pbronew.getFieldValueInt(k, colNames[0]);
                                        PbReturnObject grpdetspbro = execSelectSQL(finalQuery);
                                        String addGraphDets = getResourceBundle().getString("addGraphDets");
                                        for (int k1 = 0; k1 < grpdetspbro.getRowCount(); k1++) {
                                            Obj = new Object[6];
                                            Obj[0] = newReportGrpId;
                                            Obj[1] = grpdetspbro.getFieldValueString(k1, 2);
                                            Obj[2] = grpdetspbro.getFieldValueInt(k1, 3);
                                            Obj[3] = grpdetspbro.getFieldValueInt(k1, 4);
                                            Obj[4] = grpdetspbro.getFieldValueInt(k1, 5);
                                            Obj[5] = grpdetspbro.getFieldValueString(k1, 6);
                                            finalQuery = buildQuery(addGraphDets, Obj);
                                            list.add(finalQuery);

                                        }
                                    }

                                    String gettabpmaster = "select * from PRG_AR_REPORT_TABLE_MASTER where report_id=" + reportId;
                                    pbronew = execSelectSQL(gettabpmaster);
                                    colNames = pbronew.getColumnNames();
                                    for (int k = 0; k < pbronew.getRowCount(); k++) {
                                        finalQuery = "select PRG_AR_REPORT_TABLE_MASTER_SEQ.nextval from dual";
                                        PbReturnObject pbrogrpId = execSelectSQL(finalQuery);
                                        String newReporttabId = String.valueOf(pbrogrpId.getFieldValueInt(0, 0));
                                        String addGraphmaster = getResourceBundle().getString("addTableMaster");
                                        Obj = new Object[16];
                                        Obj[0] = newReporttabId;
                                        Obj[1] = newReportId;
                                        Obj[2] = pbronew.getFieldValueString(k, colNames[2]);
                                        Obj[3] = pbronew.getFieldValueString(k, colNames[3]);
                                        Obj[4] = pbronew.getFieldValueInt(k, colNames[4]);
                                        Obj[5] = pbronew.getFieldValueString(k, colNames[5]);
                                        Obj[6] = pbronew.getFieldValueString(k, colNames[6]);
                                        Obj[7] = pbronew.getFieldValueString(k, colNames[7]);
                                        Obj[8] = pbronew.getFieldValueString(k, colNames[8]);
                                        Obj[9] = pbronew.getFieldValueString(k, colNames[9]);
                                        Obj[10] = pbronew.getFieldValueString(k, colNames[10]);
                                        Obj[11] = pbronew.getFieldValueString(k, colNames[11]);
                                        Obj[12] = pbronew.getFieldValueString(k, colNames[12]);
                                        Obj[13] = pbronew.getFieldValueString(k, colNames[13]);
                                        Obj[14] = pbronew.getFieldValueString(k, colNames[14]);
                                        Obj[15] = pbronew.getFieldValueString(k, colNames[15]);

                                        finalQuery = buildQuery(addGraphmaster, Obj);
                                        //////.println("finalQuery===="+finalQuery);
                                        list.add(finalQuery);
                                        finalQuery = "select * from PRG_AR_REPORT_TABLE_DETAILS where REP_TAB_ID=" + pbronew.getFieldValueInt(k, colNames[0]);
                                        PbReturnObject grpdetspbro = execSelectSQL(finalQuery);
                                        String addTableDets = getResourceBundle().getString("addTableDets");
                                        for (int k1 = 0; k1 < grpdetspbro.getRowCount(); k1++) {
                                            Obj = new Object[20];
                                            Obj[0] = newReportId;
                                            Obj[1] = newReporttabId;
                                            Obj[2] = grpdetspbro.getFieldValueString(k1, 3);
                                            Obj[3] = grpdetspbro.getFieldValueString(k1, 4);
                                            Obj[4] = grpdetspbro.getFieldValueInt(k1, 5);
                                            Obj[5] = grpdetspbro.getFieldValueInt(k1, 6);
                                            Obj[6] = grpdetspbro.getFieldValueInt(k1, 7);
                                            Obj[7] = grpdetspbro.getFieldValueString(k1, 8);
                                            Obj[8] = grpdetspbro.getFieldValueString(k1, 9);
                                            Obj[9] = grpdetspbro.getFieldValueString(k1, 10);
                                            Obj[10] = grpdetspbro.getFieldValueString(k1, 11);
                                            Obj[11] = grpdetspbro.getFieldValueString(k1, 12);
                                            Obj[12] = grpdetspbro.getFieldValueString(k1, 13);
                                            Obj[13] = grpdetspbro.getFieldValueInt(k1, 14);
                                            Obj[14] = grpdetspbro.getFieldValueString(k1, 15);
                                            Obj[15] = grpdetspbro.getFieldValueString(k1, 16);
                                            Obj[16] = grpdetspbro.getFieldValueString(k1, 17);
                                            Obj[17] = grpdetspbro.getFieldValueString(k1, 18);
                                            Obj[18] = grpdetspbro.getFieldValueInt(k1, 19);
                                            Obj[19] = grpdetspbro.getFieldValueString(k1, 20);
                                            // Obj[20] = pbronew.getFieldValueString(k1,20 );
                                            finalQuery = buildQuery(addTableDets, Obj);
                                            //////.println("finalQuery===="+finalQuery);
                                            list.add(finalQuery);

                                        }
                                    }

                                }

                                //////.println("queries==listp----------=="+list);
                                executeMultiple(list);
                                ArrayList updateList = new ArrayList();
                                Obj = new Object[3];
                                Obj[0] = oldFolderId;
                                Obj[1] = folderId;
                                Obj[2] = repEleIds;
                                String oldInfo = getResourceBundle().getString("getRelatedOldInfo");
                                finalQuery = buildQuery(oldInfo, Obj);
                                PbReturnObject oldInfopbro = execSelectSQL(finalQuery);
                                for (int i1 = 0; i1 < 10; i1++) {
                                    String oldElementId = String.valueOf(oldInfopbro.getFieldValueInt(i1, "OLD_ELE_ID"));
                                    String newElementId = String.valueOf(oldInfopbro.getFieldValueInt(i1, "ELEMENT_ID"));
                                    String newDimId = String.valueOf(oldInfopbro.getFieldValueInt(i1, "DIM_ID"));
                                    String newDimTabId = String.valueOf(oldInfopbro.getFieldValueInt(i1, "DIM_TAB_ID"));
                                    String newFolderId = String.valueOf(oldInfopbro.getFieldValueInt(i1, "FOLDER_ID"));
                                    String newSubFolderId = String.valueOf(oldInfopbro.getFieldValueInt(i1, "SUB_FOLDER_ID"));
                                    updateList.add("update PRG_AR_REPORT_TABLE_DETAILS set QRY_COL_ID=(select distinct QRY_COL_ID from PRG_AR_QUERY_DETAIL where ELEMENT_ID=" + oldElementId + " and  report_id=" + newReportId + ")  where  report_id=" + newReportId + " and QRY_COL_ID=(select distinct QRY_COL_ID from PRG_AR_QUERY_DETAIL where ELEMENT_ID=" + oldElementId + " and  report_id=" + reportId + ")");
                                    updateList.add("update prg_ar_report_param_details set element_id=" + newElementId + " , dim_id=" + newDimId + "  ,dim_tab_id=" + newDimTabId + "  where element_id=" + oldElementId + "    and  report_id=" + newReportId);
                                    updateList.add("update PRG_AR_QUERY_DETAIL set element_id=" + newElementId + " , FOLDER_ID=" + newFolderId + " , SUB_FOLDER_ID=" + newSubFolderId + "  where element_id=" + oldElementId + "    and  report_id=" + newReportId);
                                    updateList.add("update PRG_AR_QUERY_DETAIL set ref_element_id=" + newElementId + "  where ref_element_id=" + oldElementId + "    and  report_id=" + newReportId);
                                    updateList.add("update PRG_AR_REPORT_VIEW_BY_MASTER set DEFAULT_VALUE='" + newElementId + "'  where DEFAULT_VALUE='" + oldElementId + "'    and  report_id=" + newReportId);
                                    updateList.add("update PRG_AR_GRAPH_DETAILS set ELEMENT_ID=" + newElementId + "  where ELEMENT_ID=" + oldElementId + "    and  GRAPH_ID in(select distinct GRAPH_ID from PRG_AR_GRAPH_MASTER where report_id=" + newReportId + ")");
                                    updateList.add("update PRG_AR_GRAPH_DETAILS set QUERY_COL_ID=(select distinct QRY_COL_ID from PRG_AR_QUERY_DETAIL where ELEMENT_ID=" + newElementId + " and  report_id=" + newReportId + ")  where ELEMENT_ID=" + newElementId + "    and  GRAPH_ID in(select distinct GRAPH_ID from PRG_AR_GRAPH_MASTER where report_id=" + newReportId + ")");
                                    updateList.add("  update PRG_AR_REPORT_VIEW_BY_DETAILS set PARAM_DISP_ID=(select  param_disp_id  from prg_ar_report_param_details where"
                                            + " report_id=" + newReportId + "  and element_id=" + newElementId + ") where PARAM_DISP_ID=(select  param_disp_id  from prg_ar_report_param_details where report_id=" + reportId
                                            + " and element_id=" + oldElementId + ")     and  view_by_id in(select distinct VIEW_BY_ID from PRG_AR_REPORT_VIEW_BY_MASTER where report_Id=" + newReportId + ")");

                                }
                                executeMultiple(updateList);
                                //////.println("updateList=="+updateList);

                            }

                        }

                    }

                }

            }
        }

    }

    public ArrayList copyUserFolderDetails(ArrayList alist, String folderId, String grpId, String oldFolderId) throws Exception {
        String addUserFolderDetailsQuery = getResourceBundle().getString("copyUserFolderDetails");
        String finalQuery = "";
        Object[] Obj = new Object[2];
        Obj[0] = folderId;
        Obj[1] = oldFolderId;
        finalQuery = buildQuery(addUserFolderDetailsQuery, Obj);
        ////////////////////////////////////////////////////////////////////////////////////.println.println("final Query==" + finalQuery);
        alist.add(finalQuery);
        return alist;

    }

    public void copyUserFoldertabs(ArrayList alist, String folderId, String grpId, String oldFolderId) throws Exception {
        ArrayList alist1 = new ArrayList();
        ArrayList alist2 = new ArrayList();
        ArrayList alist3 = new ArrayList();
        ArrayList alist4 = new ArrayList();
        boolean result = false;

        String subfolderdets = "select sub_folder_id,sub_folder_type from PRG_USER_FOLDER_DETAIL where folder_id=" + oldFolderId;
        ////////////////////////////////////////////////////////////////////////////////////.println.println("subfolderdets---" + subfolderdets);
        PbReturnObject pbro1 = execSelectSQL(subfolderdets);
        String subfolIds[] = new String[pbro1.getRowCount()];
        String subfolTypes[] = new String[pbro1.getRowCount()];
        String copyUserFolderTbales = getResourceBundle().getString("copyUserFolderTbales");
        String copyuserFolderElements = getResourceBundle().getString("copyuserFolderElements");
        String copyuserFolderElements1 = getResourceBundle().getString("copyuserFolderElements1");
        Object[] Obj1 = new Object[3];
        String finalQuery = "";
        for (int i = 0; i < pbro1.getRowCount(); i++) {
            subfolIds[i] = String.valueOf(pbro1.getFieldValueInt(i, 0));
            subfolTypes[i] = pbro1.getFieldValueString(i, 1);

            Obj1[0] = folderId;
            Obj1[1] = pbro1.getFieldValueString(i, 1);
            Obj1[2] = String.valueOf(pbro1.getFieldValueInt(i, 0));
            finalQuery = buildQuery(copyUserFolderTbales, Obj1);
            alist1.add(finalQuery);
            // execModifySQL(finalQuery);
            //  Connection con=ProgenConnection.getConnection();
            //  Statement st=con.createStatement();
            //  st.execute(finalQuery);
        }
        executeMultiple(alist1);
        Object[] Obj2 = new Object[5];

        for (int j = 0; j < subfolTypes.length; j++) {
            String elequ = "";
            //String type=subfolTypes[j];
            if (subfolTypes[j].equalsIgnoreCase("Dimensions")) {
                elequ = "SELECT sub_folder_tab_id , buss_table_id FROM PRG_USER_SUB_FOLDER_TABLES where SUB_FOLDER_ID=";
                elequ += "(select sub_folder_id from PRG_USER_FOLDER_DETAIL where folder_id= " + folderId + " and sub_folder_type='" + subfolTypes[j] + "') and USE_DENOM_TABLE!='Y' ";
            } else {
                elequ = "SELECT sub_folder_tab_id , buss_table_id FROM PRG_USER_SUB_FOLDER_TABLES where SUB_FOLDER_ID=";
                elequ += "(select sub_folder_id from PRG_USER_FOLDER_DETAIL where folder_id= " + folderId + " and sub_folder_type='" + subfolTypes[j] + "') ";
            }
            PbReturnObject pbro3 = execSelectSQL(elequ);
            String subtabId = "";
            String busstabId = "";
            for (int k = 0; k < pbro3.getRowCount(); k++) {
                subtabId = String.valueOf(pbro3.getFieldValueInt(k, 0));
                busstabId = String.valueOf(pbro3.getFieldValueInt(k, 1));
                Obj2[0] = folderId;
                Obj2[1] = subfolTypes[j];
                Obj2[2] = subtabId;
                Obj2[3] = subfolIds[j];
                Obj2[4] = busstabId;
                finalQuery = buildQuery(copyuserFolderElements, Obj2);
                alist2.add(finalQuery);
            }
            if (subfolTypes[j].equalsIgnoreCase("Dimensions")) {
                String elequ3 = "SELECT BUSS_TABLE_ID, DISP_NAME, SUB_FOLDER_TAB_ID FROM PRG_USER_SUB_FOLDER_TABLES where sub_folder_id=(select sub_folder_id from PRG_USER_FOLDER_DETAIL where folder_id= " + folderId + " and sub_folder_type='" + subfolTypes[j] + "') and use_denom_table='Y' order by buss_table_id";

                String dispName = "";
                PbReturnObject pbro4 = execSelectSQL(elequ3);
                Object[] Obj21 = new Object[6];
                for (int k = 0; k < pbro4.getRowCount(); k++) {
                    // subfolderId=String.valueOf(pbro4.getFieldValueInt(k, 0));
                    busstabId = String.valueOf(pbro4.getFieldValueInt(k, 0));
                    dispName = pbro4.getFieldValueString(k, 1);
                    subtabId = String.valueOf(pbro4.getFieldValueInt(k, 2));
                    String elequ4 = "SELECT Distinct SUB_FOLDER_TAB_ID FROM PRG_USER_SUB_FOLDER_TABLES where sub_folder_id=(select sub_folder_id from PRG_USER_FOLDER_DETAIL where folder_id= " + oldFolderId + " and sub_folder_type='" + subfolTypes[j] + "') and BUSS_TABLE_ID=" + busstabId + " and DISP_NAME='" + dispName + "' and use_denom_table='Y' and use_report_member='Y'";
                    PbReturnObject pbro5 = execSelectSQL(elequ4);
                    String currsubTabId = String.valueOf(pbro5.getFieldValueInt(0, 0));
                    Obj21[0] = folderId;
                    Obj21[1] = subfolTypes[j];
                    Obj21[2] = subtabId;
                    Obj21[3] = subfolIds[j];
                    Obj21[4] = busstabId;
                    Obj21[5] = currsubTabId;
                    finalQuery = buildQuery(copyuserFolderElements1, Obj21);
                    alist2.add(finalQuery);

                }
            }
            String refeleupdate = "update  PRG_USER_SUB_FOLDER_ELEMENTS set REF_ELEMENT_ID=ELEMENT_ID where SUB_FOLDER_ID=(select sub_folder_id from PRG_USER_FOLDER_DETAIL where folder_id= " + folderId + " and sub_folder_type='" + subfolTypes[j] + "')";
            alist2.add(refeleupdate);

        }
        result = executeMultiple(alist2);
        String sql = "";
        String updaterefelesql = "";
        ArrayList updatelist = new ArrayList();
        for (int j = 0; j < subfolTypes.length; j++) {
            if (subfolTypes[j].equalsIgnoreCase("Facts")) {
                sql = "SELECT ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, SUB_FOLDER_TAB_ID,"
                        + " REF_ELEMENT_TYPE FROM PRG_USER_SUB_FOLDER_ELEMENTS where REF_ELEMENT_TYPE='1' and SUB_FOLDER_ID=(select sub_folder_id from PRG_USER_FOLDER_DETAIL where folder_id= " + folderId + " and sub_folder_type='" + subfolTypes[j] + "')";
                PbReturnObject pbr = execSelectSQL(sql);
                for (int k = 0; k < pbr.getRowCount(); k++) {
                    updaterefelesql = "update  PRG_USER_SUB_FOLDER_ELEMENTS set REF_ELEMENT_ID=" + pbr.getFieldValueInt(k, 0) + " where SUB_FOLDER_ID=" + pbr.getFieldValueInt(k, 1) + " and BUSS_TABLE_ID=" + pbr.getFieldValueInt(k, 2) + " and BUSS_COL_ID=" + pbr.getFieldValueInt(k, 3) + " and SUB_FOLDER_TAB_ID=" + pbr.getFieldValueInt(k, 4) + " and REF_ELEMENT_TYPE in(2,3,4)";
                    updatelist.add(updaterefelesql);

                }

            }

        }
        result = executeMultiple(updatelist);

        String addUserAllInfoDetails = getResourceBundle().getString("addUserAllInfoDetails");
        String addUserAllDDIMDetails = getResourceBundle().getString("addUserAllDDIMDetails");
        String addUserAllDDIMMaster = getResourceBundle().getString("addUserAllDDIMMaster");
        String addUserAllDDIMKeyValEle = getResourceBundle().getString("addUserAllDDIMKeyValEle");
        String addUserAllADIMDetails = getResourceBundle().getString("addUserAllADIMDetails");
        String addUserAllAdimMaster = getResourceBundle().getString("addUserAllAdimMaster");
        String addUserAllADIMKeyValEle = getResourceBundle().getString("addUserAllADIMKeyValEle");
        Object Obj3[] = new Object[1];
        Obj3[0] = folderId;
        Object objtest1[] = new Object[2];
        objtest1[0] = folderId;
        objtest1[1] = folderId;
        finalQuery = buildQuery(addUserAllInfoDetails, Obj3);
        alist3.add(finalQuery);
        finalQuery = buildQuery(addUserAllDDIMDetails, Obj3);
        alist3.add(finalQuery);
        finalQuery = buildQuery(addUserAllDDIMMaster, Obj3);
        alist3.add(finalQuery);
        finalQuery = buildQuery(addUserAllDDIMKeyValEle, objtest1);

        alist3.add(finalQuery);
        finalQuery = buildQuery(addUserAllADIMDetails, Obj3);
        alist3.add(finalQuery);
        finalQuery = buildQuery(addUserAllAdimMaster, Obj3);
        alist3.add(finalQuery);
        finalQuery = buildQuery(addUserAllADIMKeyValEle, objtest1);
        alist3.add(finalQuery);

        Obj1[0] = "N";
        Obj1[1] = folderId;
        String addupdateFolderPublishment = getResourceBundle().getString("updateFolderPublishment");
        finalQuery = buildQuery(addupdateFolderPublishment, Obj1);
        alist3.add(finalQuery);
        result = executeMultiple(alist3);

        //  execModifySQL(finalQuery);
         /*
         * //for copy custom drill String copyCustomDrill =
         * resBundle.getString("copyCustomDrill"); Object Obj4[] = new
         * Object[3]; Obj4[0] = folderId; Obj4[1] = oldFolderId; Obj4[2] =
         * oldFolderId; finalQuery = buildQuery(copyCustomDrill, Obj4);
         *
         * alist3.add(finalQuery);
         * ////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery--"
         * + finalQuery); result = executeMultiple(alist3); String subdimfolIdq
         * = "select sub_folder_id FROM PRG_USER_FOLDER_DETAIL where FOLDER_ID="
         * + folderId + " and SUB_FOLDER_TYPE='Dimensions' "; PbReturnObject
         * pbrosubFolderid = execSelectSQL(subdimfolIdq); String subdimfolId =
         * String.valueOf(pbrosubFolderid.getFieldValueInt(0, 0)); String
         * childQuery = "select distinct child_member_id from
         * PRG_GRP_ROLE_CUSTOM_DRILL where member_id not in" + "(select
         * member_id from PRG_GRP_ROLE_CUSTOM_DRILL where sub_folder_id=" +
         * subdimfolId + " ) and sub_folder_id=" + subdimfolId; PbReturnObject
         * pbrochild = execSelectSQL(childQuery); String memlistquery = "select
         * member_id from PRG_GRP_ROLE_CUSTOM_DRILL where sub_folder_id=" +
         * subdimfolId; PbReturnObject memlistpbro =
         * execSelectSQL(memlistquery);
         *
         *
         * for (int i = 0; i < pbrochild.getRowCount(); i++) {
         *
         * String childchild = " select member_id, child_member_id from
         * PRG_GRP_ROLE_CUSTOM_DRILL where member_id=" +
         * pbrochild.getFieldValueInt(i, 0); PbReturnObject childchildpbro =
         * execSelectSQL(childchild); String memid =
         * String.valueOf(childchildpbro.getFieldValueInt(0, 0)); String
         * childmemId = String.valueOf(childchildpbro.getFieldValueInt(0, 1));
         * int count = 0; for (int j = 0; j < memlistpbro.getRowCount(); j++) {
         * if
         * (childmemId.equalsIgnoreCase(String.valueOf(memlistpbro.getFieldValueInt(j,
         * 0)))) { count = 1; break;
         *
         * }
         * }
         * if (count == 1) { if (childmemId.equalsIgnoreCase(memid)) { Object
         * Obj5[] = new Object[2]; Obj5[0] = memid; Obj5[1] =
         * pbrochild.getFieldValueInt(i, 0); String updatecustdrill =
         * resBundle.getString("updatecustdrill"); finalQuery =
         * buildQuery(updatecustdrill, Obj5);
         * ////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery--"
         * + finalQuery); alist4.add(finalQuery); } else {
         *
         * Object Obj5[] = new Object[2]; Obj5[0] = childmemId; Obj5[1] =
         * pbrochild.getFieldValueInt(i, 0); String updatecustdrill =
         * resBundle.getString("updatecustdrill"); finalQuery =
         * buildQuery(updatecustdrill, Obj5);
         * ////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery--"
         * + finalQuery); alist4.add(finalQuery); } } else { Object Obj5[] = new
         * Object[1]; // Obj5[0]=childmemId; Obj5[0] =
         * pbrochild.getFieldValueInt(i, 0); String updatecustdrill =
         * resBundle.getString("updatecustdrill2"); finalQuery =
         * buildQuery(updatecustdrill, Obj5);
         * ////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery--"
         * + finalQuery); alist4.add(finalQuery); } }
         */
    }

    //added on 12-12-09
    public void addUserMemberFilterForRole(String folderId, String allVal, String memId, String elementId, String subFolderId, String dimId) throws SQLException {
        if (!allVal.equalsIgnoreCase("")) {
            String addUserMemberFilter = "";
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                addUserMemberFilter = "insert into prg_role_member_filter(MEMBER_ID,FOLDER_ID,MEMBER_VALUE,ELEMENT_ID) values(?,?,?,?)";//resBundle.getString("addUserMemberFilter");

                PreparedStatement opstmt = null;
                Connection connection = ProgenConnection.getInstance().getConnection();
                opstmt = connection.prepareStatement(addUserMemberFilter);
                opstmt.setString(1, memId);
                opstmt.setString(2, folderId);
                opstmt.setString(3, allVal);
                opstmt.setString(4, elementId);
                opstmt.executeUpdate();
                if (connection != null) {
                    connection.close();
                }
            } else {
                addUserMemberFilter = "insert into prg_role_member_filter(ROLE_FILTER_ID,MEMBER_ID,FOLDER_ID,MEMBER_VALUE,ELEMENT_ID) values(ROLE_FILTER_ID_SEQ .nextval,?,?,?,?)";//resBundle.getString("addUserMemberFilter");

                OraclePreparedStatement opstmt = null;
                Connection connection = ProgenConnection.getInstance().getConnection();

                //////////////////////////////////////////////////////////////////////////////////System.out.println.println(" here - "+addUserMemberFilter);
                opstmt = (OraclePreparedStatement) connection.prepareStatement(addUserMemberFilter);
                opstmt.setString(1, memId);
                opstmt.setString(2, folderId);
                opstmt.setStringForClob(3, allVal);
                opstmt.setStringForClob(4, elementId);
                //////////////////////////////////////////////////////////////////////////////////System.out.println.println(" opstmt in "+opstmt.toString());
                int rows = opstmt.executeUpdate();
                try {
                    String filterIdQuery = "select ROLE_FILTER_ID from prg_role_member_filter order by ROLE_FILTER_ID desc";
                    PbReturnObject pbRet = null;
                    pbRet = new PbDb().execSelectSQL(filterIdQuery);
                    String filterId1 = pbRet.getFieldValueString(0, 0);
                    addSecMemberFilter("0", subFolderId, allVal, dimId, elementId, filterId1);
                } catch (Exception ex) {
                    logger.error("Exception:", ex);
                }
                if (connection != null) {
                    connection.close();
                }

            }
        }

    }
    /*
     * This method is for inserting a records into user sub folder tables and
     * returns : ArrayList which contains queries to be executed
     */

    private int getCurrentSeqSqlServer(String table) {
        int currSeq = 0;
        String sql = "Select IDENT_CURRENT('" + table + "')";

        PbReturnObject pbRet;
        try {
            pbRet = new PbDb().execSelectSQL(sql);
            currSeq = pbRet.getFieldValueInt(0, 0);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return currSeq;
    }

    //added 0n 12-12-09
    public HashMap checkMemberValueStatusForRole(String folderId, String memId) throws Exception {
        String status = "";
        HashMap details = new HashMap();
        String addedDataQ = "select * from prg_role_member_filter where member_id=" + memId + " and folder_id=" + folderId;
        PbReturnObject pbro = execSelectSQL(addedDataQ);
        String filterId = "";
        if (pbro.getRowCount() > 0) {
            status = "true";
            filterId = pbro.getFieldValueString(0, "ROLE_FILTER_ID");
        } else {
            status = "false";
        }

        details.put("status", status);
        details.put("filterId", filterId);
        return details;
    }

    public void deleteDimMemberValues(String userId, String subFolderIdUser, String userMemId) {
        String delQ = "delete from prg_user_role_member_filter where USER_ID=" + userId + " and MEMBER_ID=" + userMemId + " and "
                + " FOLDER_ID=(select folder_id from prg_user_folder_detail where sub_folder_id=" + subFolderIdUser + ")";
        ArrayList delList = new ArrayList();
        delList.add(delQ);
        try {
            executeMultiple(delList);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    //added on 12-12-09
    public void deleteDimMemberValuesForRole(String folderId, String memId) {
        String delQ = "delete from prg_role_member_filter where MEMBER_ID=" + memId + " and  FOLDER_ID=" + folderId;
        ArrayList delList = new ArrayList();
        delList.add(delQ);
        try {
            executeMultiple(delList);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }
//added on 12-12-09

    public void updateMemberValueStatusForRole(String folderId, String memId, String allVal, String filterId, String elementId, String subFolderId, String dimId) throws Exception {

        String deleteQ = "delete from prg_role_member_filter where role_filter_id=" + filterId;
        OraclePreparedStatement opstmt = null;
        Connection connection = ProgenConnection.getInstance().getConnection();

        String addUserMemberFilter = "insert into prg_role_member_filter(role_FILTER_ID,MEMBER_ID,FOLDER_ID,MEMBER_VALUE,ELEMENT_ID) values(ROLE_FILTER_ID_SEQ.nextval,?,?,?,?)";//resBundle.getString("addUserMemberFilter");
        if (!allVal.equalsIgnoreCase("")) {
            opstmt = (OraclePreparedStatement) connection.prepareStatement(addUserMemberFilter);
            opstmt.setString(1, memId);
            opstmt.setString(2, folderId);
            opstmt.setStringForClob(3, allVal);
            opstmt.setStringForClob(4, elementId);

//            int rows = opstmt.executeUpdate();
            opstmt.executeUpdate();
        }
        try {
            String filterIdQuery = "select ROLE_FILTER_ID from prg_role_member_filter order by ROLE_FILTER_ID desc";
            PbReturnObject pbRet = null;
            pbRet = new PbDb().execSelectSQL(filterIdQuery);
            String filterId1 = pbRet.getFieldValueString(0, 0);
            updateSecMemberValueStatus("0", subFolderId, allVal, dimId, elementId, filterId, filterId1);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (connection != null) {
            connection.close();
        }
        try {
            ArrayList del = new ArrayList();
            del.add(deleteQ);
            executeMultiple(del);
        } catch (Exception x) {
            logger.error("Exception:", x);
        }

    }

    public boolean deleteUserFolder(String folderId) {
        boolean result = false;
        String getSubFolderIdByFolderIdQuery = getResourceBundle().getString("getSubFolderIdByFolderId");
        String deleteUserFolderQuery = getResourceBundle().getString("deleteUserFolder");
        String deleteUserFolderDetailQuery = getResourceBundle().getString("deleteUserFolderDetails");
        String deleteUserSubFolderTablesQuery = getResourceBundle().getString("deleteUserSubFolderTables");
        String deleteUserSubFolderElementsQuery = getResourceBundle().getString("deleteUserSubFolderElements");

        ArrayList deleteUserFolderQueries = new ArrayList();
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String finalQuery = "";
        String subFolderId = "";
        String[] tableColNames = null;
        try {
            //new ProgenConnection().getConnection();

            Obj = new Object[1];
            Obj[0] = folderId;
            finalQuery = buildQuery(getSubFolderIdByFolderIdQuery, Obj);
            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();
            if (retObj != null && retObj.getRowCount() != 0) {
                for (int index = 0; index < retObj.getRowCount(); index++) {
                    subFolderId = retObj.getFieldValueString(index, tableColNames[0]);
                    Obj[0] = subFolderId;
                    finalQuery = buildQuery(deleteUserSubFolderElementsQuery, Obj);
                    deleteUserFolderQueries.add(finalQuery);//deleting from PRG_USER_SUB_FOLDER_ELEMENTS table

                    finalQuery = buildQuery(deleteUserSubFolderTablesQuery, Obj);
                    deleteUserFolderQueries.add(finalQuery);//deleting from PRG_USER_SUB_FOLDER_TABLES  table

                    Obj[0] = folderId;
                    finalQuery = buildQuery(deleteUserFolderDetailQuery, Obj);
                    deleteUserFolderQueries.add(finalQuery);//deleting from PRG_USER_FOLDER_DETAIL table

                    finalQuery = buildQuery(deleteUserFolderQuery, Obj);
                    deleteUserFolderQueries.add(finalQuery);//deleting from PRG_USER_FOLDER  table
                }
            }
            result = executeMultiple(deleteUserFolderQueries);
            return result;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return result;
        }

    }
    //by sunita

    public String getUserFolderList2(String connId, String folderid) {
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            PbReturnObject retObj = null;
            String[] colNames = null;
            String sql = getResourceBundle().getString("getUserFolderList1");
            Object obj[] = new Object[2];
            obj[1] = connId;
            obj[0] = folderid;
            String finalQuery = buildQuery(sql, obj);
            //
            retObj = execSelectSQL(finalQuery);
            //
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                String folderId = retObj.getFieldValueString(i, colNames[0]);
                String folderName = retObj.getFieldValueString(i, colNames[1]);
                String grpName = retObj.getFieldValueString(i, colNames[3]).toUpperCase();
                String grpId = retObj.getFieldValueString(i, colNames[4]).toUpperCase();
                outerBuffer.append("<li class='closed' id='" + folderId + "'>");
                outerBuffer.append("<img src='images/treeViewImages/folder-closed.gif' id='" + grpId + "' ></img>");
                outerBuffer.append("<span class='UserFolderMenu'><font size='1px' face='verdana'>&nbsp;" + folderName + "- </font><font size='2px' face='verdana'><b>" + grpName + "</b></font></span>");
                outerBuffer.append("<ul>");
//                outerBuffer.append(getUserSubFolderList(folderId));
                //commented by Nazneen
//                outerBuffer.append("<span class='UserFolderMenu' onclick='getRoleDetails(" + folderId + ")'><font size='1px' face='verdana'>&nbsp;" + folderName + "- </font><font size='2px' face='verdana'><b>" + grpName + "</b></font></span>");
//                outerBuffer.append("<ul id='ROLE_"+folderId+"'>");
                outerBuffer.append(getUserSubFolderList(folderId));
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return outerBuffer.toString();
    }

    public String getUserFolderList(String connId) {
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            PbReturnObject retObj = null;
            String[] colNames = null;
            String sql = getResourceBundle().getString("getUserFolderList");
            Object obj[] = new Object[1];
            obj[0] = connId;
            String finalQuery = buildQuery(sql, obj);
            retObj = execSelectSQL(finalQuery);

            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                String folderId = retObj.getFieldValueString(i, colNames[0]);
                String folderName = retObj.getFieldValueString(i, colNames[1]);
                String grpName = retObj.getFieldValueString(i, colNames[3]).toUpperCase();
                String grpId = retObj.getFieldValueString(i, colNames[4]).toUpperCase();
                outerBuffer.append("<li class='closed' id='" + folderId + "'>");
                outerBuffer.append("<img src='images/treeViewImages/folder-closed.gif' id='" + grpId + "' ></img>");
                outerBuffer.append("<span class='UserFolderMenu'><font size='1px' face='verdana'>&nbsp;" + folderName + "- </font><font size='2px' face='verdana'><b>" + grpName + "</b></font></span>");
                outerBuffer.append("<ul>");
//                outerBuffer.append(getUserSubFolderList(folderId));
                //commented by Nazneen
//                outerBuffer.append("<span class='UserFolderMenu' onclick='getRoleDetails(" + folderId + ")'><font size='1px' face='verdana'>&nbsp;" + folderName + "- </font><font size='2px' face='verdana'><b>" + grpName + "</b></font></span>");
//                outerBuffer.append("<ul id='ROLE_"+folderId+"'>");
                outerBuffer.append(getUserSubFolderList(folderId));
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return outerBuffer.toString();
    }

    public String getUserSubFolderList(String folderId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = folderId;
        String subFolderId = "";
        String subFolderName = "";
        String subFolderType = "";
        String sql = getResourceBundle().getString("getUserSubFolderList");
        //added by susheela start
        String subFolderIdDim = "";
        // added by susheela over

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            retObj = execSelectSQL(finalQuery);
            ////.println(" finalQuery 2 " + finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                subFolderId = retObj.getFieldValueString(i, colNames[0]);
                subFolderName = retObj.getFieldValueString(i, colNames[1]);
                subFolderType = retObj.getFieldValueString(i, colNames[2]);

                //added by susheela start
                if (subFolderType.equalsIgnoreCase("Dimensions")) {
                    subFolderIdDim = subFolderId;
                }
                // added by susheela over

                String subFolderTypeImg = "";

                if (subFolderType.equalsIgnoreCase("Dimensions")) {
                    subFolderTypeImg = "images/treeViewImages/Dim.gif";
                } else if (subFolderType.equalsIgnoreCase("Facts")) {
                    subFolderTypeImg = "images/treeViewImages/door.png";
                } else if (subFolderType.equalsIgnoreCase("Buckets")) {
                    subFolderTypeImg = "images/beaker-empty.png";
                }

                //subFolderTypeImg = "images/beaker-empty.png";
                outerBuffer.append("<li class='closed' id='" + subFolderId + "'>");
                outerBuffer.append("<img src='" + subFolderTypeImg + "'></img>");
                if (subFolderType.equalsIgnoreCase("Dimensions")) {
                    //modified by Nazneen
//                    outerBuffer.append("<span onclick='getsubRoleDetails(\""+subFolderType.toUpperCase()+"_"+subFolderId+"\")'><font size='1px' face='verdana'>&nbsp;" + subFolderName + "</font></span>");
                    outerBuffer.append("<span><font size='1px' face='verdana'>&nbsp;" + subFolderName + "</font></span>");

                } else if (subFolderType.equalsIgnoreCase("Facts")) {
                    //modified by Nazneen
//                    outerBuffer.append("<span id='" + subFolderId + "' class=\"formulaMenu\" onclick='getsubRoleDetails(\""+subFolderType.toUpperCase()+"_"+subFolderId+"\")'><font size='1px' face='verdana'>&nbsp;" + subFolderName + "</font></span>");
                    outerBuffer.append("<span id='" + subFolderId + "' class=\"formulaMenu\"><font size='1px' face='verdana'>&nbsp;" + subFolderName + "</font></span>");

                } else if (subFolderType.equalsIgnoreCase("Buckets")) {
                    //modified by Nazneen
//                    outerBuffer.append("<span onclick='getsubRoleDetails(\""+subFolderType.toUpperCase()+"_"+subFolderId+"\")'><font size='1px' face='verdana'>&nbsp;" + subFolderName + "</font></span>");
                    outerBuffer.append("<span><font size='1px' face='verdana'>&nbsp;" + subFolderName + "</font></span>");

                }

                outerBuffer.append("<ul>");

//                if (subFolderType.equalsIgnoreCase("Dimensions")) {
//
//                    outerBuffer.append(getUserFolderDims(subFolderId));
//
//                } else if (subFolderType.equalsIgnoreCase("Facts")) {
//
//                    outerBuffer.append(getUserFolderFacts(subFolderId));
//
//                } else if (subFolderType.equalsIgnoreCase("Buckets")) {
//
//                    outerBuffer.append(getUserFolderBuckets(subFolderId));
//
//                }
//                added by Nazneen
                if (subFolderType.equalsIgnoreCase("Dimensions")) {

                    outerBuffer.append(getUserFolderDims(subFolderId));

                } else if (subFolderType.equalsIgnoreCase("Facts")) {

                    outerBuffer.append(getUserFolderFacts(subFolderId));

                } else if (subFolderType.equalsIgnoreCase("Buckets")) {

                    outerBuffer.append(getUserFolderBuckets(subFolderId));

                }
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");

                //16-02-2010 added in order to show the target measures satrt
                if (i == retObj.getRowCount() - 1) {
                    //////////////////////////////.println(" j uuuu ");
                    outerBuffer.append("<li class='closed' id=" + subFolderId + ">");
                    outerBuffer.append("<img src='" + subFolderTypeImg + "'></img>");

                    outerBuffer.append("<span id=" + subFolderId + " class=\"targetMeasureRoleDiv\"><font size='1px' face='verdana'>&nbsp;Target Measures</font></span>");
                    // outerBuffer.append("<ul><li></li></ul>");
                    outerBuffer.append("<ul>");
                    outerBuffer.append(getTargetMeasuresForRole(folderId));
                    outerBuffer.append("</ul>");

                    outerBuffer.append("</li>");
                }

                if (i == retObj.getRowCount() - 1) {
                    //outerBuffer.append("<li class='closed' id='" + subFolderId + "'><ul>");
                    outerBuffer.append("<li class='closed' id=" + subFolderIdDim + ">");
                    outerBuffer.append("<img src='" + subFolderTypeImg + "'></img>");

                    outerBuffer.append("<span id=" + subFolderIdDim + " class=\"dimDiv\"><font size='1px' face='verdana'>&nbsp; Custom Drill</font></span>");
                    outerBuffer.append("<ul><li></li></ul>");
                    outerBuffer.append("</li>");
                    // added by susheela start

                    // modified by susheela 01-12-09 start
                    if (!isCompanyValid) {
                        String usersQuery = "select * from prg_grp_user_assignment where grp_id in(select grp_id from prg_user_folder where folder_id=" + folderId + ")";

                        outerBuffer.append("<li class='closed' id=" + subFolderIdDim + ">");
                        outerBuffer.append("<img src='" + subFolderTypeImg + "'></img>");

                        outerBuffer.append("<span id=" + subFolderIdDim + " class=\"userDrillDiv\"><font size='1px' face='verdana'>&nbsp; Users</font></span>");
                        outerBuffer.append("<ul>");
                        outerBuffer.append(getUsersForRole(folderId, subFolderIdDim));
                        outerBuffer.append("</ul>");
                        outerBuffer.append("</li>");
                    }
                    // modified by susheela 01-12-09 over

                    //// added by susheela for accounts
                    if (isCompanyValid) {
                        outerBuffer.append("<li class='closed' id=" + subFolderIdDim + ">");
                        outerBuffer.append("<img src='" + subFolderTypeImg + "'></img>");

                        outerBuffer.append("<span id=" + folderId + " class=\"accountDiv\"><font size='1px' face='verdana'>&nbsp; Company Name</font></span>");
                        //getAccountsForRole
                        outerBuffer.append("<ul>");
                        //outerBuffer.append(getUsersForRole(folderId, subFolderIdDim));
                        outerBuffer.append(getAccountsForRole(folderId, subFolderIdDim));
                        outerBuffer.append("</ul>");
                        outerBuffer.append("</li>");
                    }
                }

            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();
    }

    //added on 17feb 10 start
    public String getTargetMeasuresForRole(String roleId) throws Exception {
        StringBuffer outerBuffer = new StringBuffer("");
        String roleFact = "select * from prg_grp_buss_table where buss_table_id in(select buss_table_id from "
                + " prg_user_sub_folder_tables where sub_folder_id in(select sub_folder_id from prg_user_folder_detail "
                + " where folder_id=" + roleId + ") and is_fact='Y') and buss_type='Target Table'";        ////////////////////////////////////////////////////////////////////////////////////////////.println.println(" userRoleQ " + userRoleQ);
        // 
        PbReturnObject roleFactObj = execSelectSQL(roleFact);

        String busTabId = "";

        for (int p = 0; p < roleFactObj.getRowCount(); p++) {
            busTabId = roleFactObj.getFieldValueString(p, "BUSS_TABLE_ID");
            String factColQ = "Select distinct BUSS_COL_ID, BUSS_COL_NAME, BUSS_TABLE_ID, REF_ELEMENT_TYPE from "
                    + " prg_user_sub_folder_elements where buss_table_id =" + busTabId + " order by buss_col_id, ref_element_type ";
            PbReturnObject roleMeasureObj = execSelectSQL(factColQ);
            for (int k = 0; k < roleMeasureObj.getRowCount(); k++) {
                String colName = roleMeasureObj.getFieldValueString(k, "BUSS_COL_NAME");
                String refEleType = roleMeasureObj.getFieldValueString(k, "REF_ELEMENT_TYPE");
                String colId = roleMeasureObj.getFieldValueString(k, "BUSS_COL_ID");
                String isChecked = "";
                if (refEleType.equalsIgnoreCase("1")) {
                    if (colName.startsWith("TARGETM_")) {
                        String existQ = "select * from PRG_FOLDER_TARGET_MEASURE where folder_id=" + roleId + " and measure_id=" + colId;
                        PbReturnObject existObj = execSelectSQL(existQ);
                        if (existObj.getRowCount() == 0) {
                            isChecked = "";
                        } else {
                            isChecked = "checked";
                        }
                        outerBuffer.append("<li id='" + colId + "'>");
                        outerBuffer.append("<Input " + isChecked + " onclick=\"updateTargetMeasure('" + colId + ":" + roleId + ":" + busTabId + "')\" type=\"checkbox\">");

                        outerBuffer.append("<span id='" + colId + " ' class=\"measureTargetDiv\"><font size='1px' face='verdana'>&nbsp;" + colName + "</font></span>");
                        outerBuffer.append("</li>");
                    }
                }
            }
        }
        return outerBuffer.toString();
    }
    //added on 17-02-2010 over

    public String getRolePublishedStForAcc(String orgId) throws Exception {
        String status = "";
        String stQ = "select * from prg_user_all_adim_master where info_folder_id in(select buss_role from prg_org_master where org_id=" + orgId + ")";
        PbReturnObject pbro = execSelectSQL(stQ);
        if (pbro.getRowCount() > 0) {
            status = "published";
        } else {
            status = "unpublished";
        }

        return status;
    }

    public HashMap getMemberValuesForDimForAccount(String memId, String orgId, String subFolderId) throws Exception {
        ProgenParam pm = new ProgenParam();
        DisplayParameters dispParam = new DisplayParameters();
        HashMap details = new HashMap();
        String eleQuery = "select * from prg_user_sub_folder_elements where sub_folder_id in(" + subFolderId + ")  "
                + " and buss_col_id in( select distinct col_id from prg_grp_dim_member_details where "
                + " mem_id in(select member_id "
                + " from  prg_user_sub_folder_tables where member_id=" + memId + " and sub_folder_id in(" + subFolderId + ") )) order by 1 ";
        //////.println("eleQuery==" + eleQuery);
        /*
         * "select * from prg_user_sub_folder_elements where sub_folder_id
         * in(select sub_folder_id " + " from prg_user_folder_detail where
         * folder_id in(select buss_role from prg_org_master where org_id=" +
         * orgId + ")) " + " and buss_col_id in( select distinct col_id from
         * prg_grp_dim_member_details where mem_id in(select member_id " + "
         * from prg_user_sub_folder_tables where member_id=" + memId + " and
         * sub_folder_id in(select " + " sub_folder_id from
         * prg_user_folder_detail where folder_id in(select buss_role from " + "
         * prg_org_master where org_id=" + orgId + ") ) ))";
         */
        PbReturnObject eleObj = execSelectSQL(eleQuery);
        PbReportCollection prc = new PbReportCollection();
        for (int m = 0; m < eleObj.getRowCount(); m++) {
            String eleId = eleObj.getFieldValueString(m, "ELEMENT_ID");
            String busColName = eleObj.getFieldValueString(m, "BUSS_COL_NAME");
            String busColId = String.valueOf(eleObj.getFieldValueInt(m, "BUSS_COL_ID"));
            String query = dispParam.getParameterQuery(eleId);
            //PbReturnObject values=pm. execSelectSQL(query);
            pm.elementId = eleId;

            String sql = "select * from PRG_ACCOUNT_MEMBER_FILTER where MEMBER_ID=" + memId + " and ACCOUNT_NO in (" + orgId + " )";
            HashMap lMap = new HashMap();
            ArrayList lBusCols = new ArrayList();
            lBusCols.add(busColId);
            PbReturnObject pbro = execSelectSQL(sql);
            String whereClause = "";
            if (pbro.getRowCount() > 0) {
                lMap.put(eleId, pbro.getFieldValueClobString(0, "MEMBER_VALUE"));
                whereClause = prc.getWhereClause(lMap, lBusCols);
            } else {
                sql = "select * from prg_role_member_filter where member_id=" + memId + " and folder_id in (select distinct folder_id from prg_user_folder_detail where sub_folder_id=" + subFolderId + " )";
                pbro = execSelectSQL(sql);
                if (pbro.getRowCount() > 0) {
                    lMap.put(eleId, pbro.getFieldValueClobString(0, "MEMBER_VALUE"));
                    whereClause = prc.getWhereClause(lMap, lBusCols);
                }

            }
            query = query + whereClause + " order by 1";
            ArrayList al = pm.getRowEdges(query, eleId);

            details.put(busColName, al);
        }
        return details;
    }

    public String getAccountUsers(String roleId, String subFolderIdDim, String color) throws Exception {
        StringBuffer outerBuffer = new StringBuffer("");
        String accountUserQ = "select * from prg_ar_users where account_type=" + roleId;
        PbReturnObject userAccount = execSelectSQL(accountUserQ);
        for (int p = 0; p < userAccount.getRowCount(); p++) {
            String userId = "";
            String userName = "";
            userId = userAccount.getFieldValueString(p, "PU_ID");
            userName = userAccount.getFieldValueString(p, "PU_LOGIN_ID");
            outerBuffer.append("<li id='" + userId + "'>");
            outerBuffer.append("<span id='" + subFolderIdDim + ":" + userId + "' class=\"accountUserMenu\"  style=\"color:" + color + "\">&nbsp;" + userName + "</span>");
            outerBuffer.append("</li>");
        }
        return outerBuffer.toString();
    }

    public String getAccountsForRole(String folderId, String subFolderIdDim) throws Exception {
        StringBuffer outerBuffer = new StringBuffer("");
        String accountRoleQ = "select * from prg_org_master  order by  org_end_date desc";
        PbReturnObject userAccount = execSelectSQL(accountRoleQ);
        ArrayList orgIdarr = new ArrayList();
        ArrayList orgNamearr = new ArrayList();
        String roleIdsList[] = folderId.split(",");
        for (int i = 0; i < userAccount.getRowCount(); i++) {
            String busRoles = userAccount.getFieldValueString(i, 5);
            String comproleIdsList[] = busRoles.split(",");
            int count = 0;
            for (int j = 0; j < roleIdsList.length; j++) {
                for (int j1 = 0; j1 < comproleIdsList.length; j1++) {
                    if (roleIdsList[j].equalsIgnoreCase(comproleIdsList[j1])) {
                        count++;
                    }
                }
            }
            if (count >= roleIdsList.length) {
                orgIdarr.add(userAccount.getFieldValueString(i, "ORG_ID"));
                orgNamearr.add(userAccount.getFieldValueString(i, "ORGANISATION_NAME"));
            }
        }
        for (int p = 0; p < orgIdarr.size(); p++) {
            String orgId = String.valueOf(orgIdarr.get(p));
            String orgName = String.valueOf(orgNamearr.get(p));
            String actTimeDiffQ = "select sysdate-(select org_end_date from prg_org_master where org_Id in(" + orgId + ")) from dual";
            PbReturnObject timObj = execSelectSQL(actTimeDiffQ);
            float diff = 0;
            String color = "black";
            diff = Float.parseFloat(timObj.getFieldValueString(0, 0));
            if (diff > 0) {
                color = "red";
                orgName = orgName;

            }
            outerBuffer.append("<li id='li-" + orgId + "'>");
            outerBuffer.append("<span id='" + orgId + "' class=\"accountMenu\" style=\"color:" + color + "\">&nbsp;" + orgName + "</span>");
            outerBuffer.append("<span><font size='1px' face='verdana'>&nbsp;</font></span>");
            outerBuffer.append("<ul>");
            outerBuffer.append("<li class='closed' id='dim-" + orgId + "'>");
            outerBuffer.append("<span><font size='1px' face='verdana' style=\"color:" + color + "\">&nbsp;Dimensions</font></span>");
            outerBuffer.append("<ul>");
            outerBuffer.append(getUserFolderDimsForAccount(orgId, color, folderId));
            outerBuffer.append("</ul>");

            outerBuffer.append("</li>");

            outerBuffer.append("<li class='closed' id='user-" + orgId + "'>");
            outerBuffer.append("<span><font size='1px' face='verdana' style=\"color:" + color + "\">&nbsp;Users</font></span>");
            outerBuffer.append("<ul>");
            outerBuffer.append(getAccountUsers(orgId, subFolderIdDim, color));

            outerBuffer.append("</ul>");

            outerBuffer.append("</li>");
            outerBuffer.append("</ul>");

            outerBuffer.append("</li>");
        }
        return outerBuffer.toString();
    }

    public ArrayList getAddedMemberValuesForAccount(String orgId, String memberId) throws Exception {
        String addedDataQ = "select * from prg_account_member_filter where account_no=" + orgId + " and member_id=" + memberId;
        String dataValues = "";
        ArrayList allVals = new ArrayList();
        PbReturnObject pbro = execSelectSQL(addedDataQ);
        if (pbro.getRowCount() > 0) {
            dataValues = pbro.getFieldValueClobString(0, "MEMBER_VALUE");
        }
        String vals[] = (String[]) dataValues.split(",");
        for (int m = 0; m < vals.length; m++) {

            if (!vals[m].equalsIgnoreCase("")) {
                allVals.add(vals[m].replace("'||chr(38)||'", "&"));

            }
        }

        return allVals;
    }

    public String getUserFolderDimsForAccount(String orgId, String color, String folderId) throws Exception {
        StringBuffer outerBuffer = new StringBuffer("");
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = orgId;
        String sql = "select buss_role from prg_org_master where org_id=" + orgId;

        retObj = execSelectSQL(sql);
        if (retObj != null) {
            if (retObj.getRowCount() > 0) {
                String busRoleId = retObj.getFieldValueString(0, 0);
                /*
                 * if(!busRoleId.equalsIgnoreCase("")){ String exactRoleList="";
                 * String busRoleIdList[]=busRoleId.split(","); for(int
                 * i=0;i<10;i++){
                 * if(folderId.equalsIgnoreCase(busRoleIdList[i])){
                 * exactRoleList+=","+busRoleIdList[i]; } }
                 * if(exactRoleList.startsWith(",")){
                 *
                 * exactRoleList=exactRoleList.substring(1);
                 }
                 */

                sql = "SELECT DISTINCT DIM_ID, DIM_NAME,use_report_member,sub_folder_id FROM PRG_USER_SUB_FOLDER_TABLES where"
                        + " is_dimension='Y' and sub_folder_id in(select sub_folder_id from prg_user_folder_detail"
                        + " where folder_id in(" + folderId + ")) and dim_name not in('Time') order by dim_name";
                String dimId = "";
                String dimName = "";

                try {
                    // finalQuery = buildQuery(sql, obj);
                    retObj = execSelectSQL(sql);
                    if (retObj.getRowCount() > 0) {
                        colNames = retObj.getColumnNames();
                        for (int i = 0; i < retObj.getRowCount(); i++) {
                            dimId = retObj.getFieldValueString(i, colNames[0]);
                            dimName = retObj.getFieldValueString(i, colNames[1]);
                            String use_report_member = "";
                            String isChecked = "";
                            use_report_member = retObj.getFieldValueString(i, colNames[2]);
                            if (use_report_member.equalsIgnoreCase("Y")) {
                                isChecked = "checked";
                            } else {
                                isChecked = "";
                            }
                            String subFolderId = retObj.getFieldValueString(i, colNames[3]);
                            outerBuffer.append("<li class='closed' id='" + dimId + ":" + orgId + "'>");
                            outerBuffer.append("<span id=" + dimId + ":" + orgId + " class=\"\" style=\"color:" + color + "\">&nbsp;" + dimName + "</span>");
                            outerBuffer.append("<ul>");
                            outerBuffer.append(getUserFolderDimMbrsForAccount(subFolderId, dimId, orgId, color));

                            outerBuffer.append("</ul>");
                            outerBuffer.append("</li>");
                        }
                    }
                } catch (Exception exception) {
                    logger.error("Exception:", exception);
                }
            }
        }
        return outerBuffer.toString();
    }

    public String getUserFolderDimMbrsForAccount(String subFolderId, String dimId, String orgId, String color) throws Exception {
        StringBuffer outerBuffer = new StringBuffer("");
        String accountDimMemQ = "SELECT sub_folder_tab_id, member_id , nvl(disp_name, member_name) as mem_disp_name ,"
                + " dim_tab_id,use_report_member,use_report_dim_member FROM PRG_USER_SUB_FOLDER_TABLES WHERE"
                + " sub_folder_id ='" + subFolderId + "' AND is_dimension  ='Y' AND dim_id ='" + dimId + "' ORDER BY mem_disp_name";
        PbReturnObject userAccount = execSelectSQL(accountDimMemQ);
        for (int p = 0; p < userAccount.getRowCount(); p++) {
            String memId = "";
            String memberName = "";
            memId = userAccount.getFieldValueString(p, "MEMBER_ID");
            memberName = userAccount.getFieldValueString(p, "MEM_DISP_NAME");
            outerBuffer.append("<li id='" + memId + ":" + orgId + ":" + memberName.replace(" ", "~") + "'>");
            outerBuffer.append("<span id='" + subFolderId + ":" + memId + ":" + orgId + ":" + memberName.replace(" ", "~") + " ' class=\"accountDimMenu\" style=\"color:" + color + "\">&nbsp;" + memberName + "</span>");
            outerBuffer.append("</li>");
        }

        return outerBuffer.toString();
    }

    //added by susheela 01-12-09 start
    public HashMap getMemberValuesForDim(String memberID, String subFolderId, String elementID, String startVal, String searchText) throws Exception {
        String limit = "";
        String finalLimit = "";
        String endValue = "";
        int exceptionFlag = 0;
        int rowStart = 0;

        ProgenParam pm = new ProgenParam();
        PbReportCollection prc = new PbReportCollection();
        DisplayParameters dispParam = new DisplayParameters();
        HashMap details = new HashMap();
        String roleMemberFilter = "select * from prg_role_member_filter where folder_id in(select folder_id from "
                + " prg_user_folder_detail where sub_folder_id=" + subFolderId + ") and member_id in (select DISTINCT member_id"
                + " from prg_user_sub_folder_tables where dim_id in( SELECT  distinct dim_id FROM prg_user_sub_folder_tables"
                + " WHERE member_id=" + memberID + "))";
//        String getMemberForEle = " select * from  prg_user_all_info_details where element_id in( select key_element_id "
//                + " from prg_user_all_adim_key_val_ele ad where key_dim_id in(select dim_id from prg_user_sub_folder_tables"
//                + " where member_id in(" + memberID + "))  and key_folder_id in(select folder_id from prg_user_folder_detail where"
//                + " sub_folder_id=" + subFolderId + "))";
        String getMemberForEle = "select member_id , buss_col_id , element_id , buss_col_name from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID=" + elementID;
        PbReturnObject pbro = execSelectSQL(getMemberForEle);
        HashMap eleMembers = new HashMap();
        for (int g = 0; g < pbro.getRowCount(); g++) {
            ArrayList s = new ArrayList();

            s.add(pbro.getFieldValueString(g, 0));//3019
            s.add(pbro.getFieldValueString(g, 1));//16382
            eleMembers.put(pbro.getFieldValueString(g, 2), s);//111667
        }
        String userRoleFilterQ = "select * from PRG_USER_ROLE_MEMBER_FILTER where folder_id in(select folder_id from "
                + " prg_user_folder_detail  where sub_folder_id=" + subFolderId + ") and member_id in( select distinct member_id from "
                + " prg_user_sub_folder_tables where dim_id in(select dim_id from prg_user_sub_folder_tables where member_id=" + memberID + " ) ) ";
        PbReturnObject userObj = execSelectSQL(userRoleFilterQ);
        PbReturnObject roleFilterObj = execSelectSQL(roleMemberFilter);
        HashMap lMap = new HashMap();
        ArrayList lBusCols = new ArrayList();
        HashMap memFilterMap = new HashMap();
        HashMap det = new HashMap();
        ArrayList colIds = new ArrayList();
        boolean userSecurity = false;

        if (userObj.getRowCount() == 0) {
            userSecurity = true;
            for (int m = 0; m < roleFilterObj.getRowCount(); m++) {
                memFilterMap.put(roleFilterObj.getFieldValueString(m, "MEMBER_ID"), Joiner.on(",").join(fromXML(roleFilterObj.getFieldValueClobString(m, "MEMBER_VALUE"))));
            }
        } else {
            userSecurity = true;
            memFilterMap = new HashMap();
            for (int m = 0; m < roleFilterObj.getRowCount(); m++) {
                memFilterMap.put(roleFilterObj.getFieldValueString(m, "MEMBER_ID"), roleFilterObj.getFieldValueClobString(m, "MEMBER_VALUE"));
            }
            for (int m = 0; m < userObj.getRowCount(); m++) {
                if (memberID.equalsIgnoreCase(userObj.getFieldValueString(m, 2))) {
                    if (!memFilterMap.containsKey(userObj.getFieldValueString(m, 2))) {
                        memFilterMap.put(userObj.getFieldValueString(m, 2), Joiner.on(",").join(fromXML(userObj.getFieldValueClobString(m, "MEMBER_VALUE"))));
                    }
                }
            }
        }

        lMap = new HashMap();
        lBusCols = new ArrayList();
        String eleId = "";
        String query = "";
        String busColName = "";
        String memId = "";

        for (int m = 0; m < pbro.getRowCount(); m++) {
            eleId = pbro.getFieldValueString(m, 2);
            busColName = pbro.getFieldValueString(m, 3);
            query = dispParam.getParameterQuery(eleId);

            if (eleMembers.containsKey(eleId)) {
                ArrayList s = (ArrayList) eleMembers.get(eleId);

                memId = s.get(0).toString();
                if (memFilterMap.containsKey(memId)) {

                    if (userSecurity == true) {
                        String value = (String) memFilterMap.get(memId);
                        if ((value == null ? "" != null : !value.equals("")) || !value.equalsIgnoreCase("")) {
                            lMap.put(eleId, value);
                        }
                        lBusCols.add(s.get(1));
                    }
                }
            } else {
            }
        }
        String whereClause = "";
        whereClause = prc.getWhereClause(lMap, lBusCols);
        if (!(searchText == null || searchText == "" || searchText.equalsIgnoreCase(""))) {
            if (whereClause != null || whereClause != "") {
                whereClause = whereClause + " and " + busColName + " like '%" + searchText + "%'";
            } else {
                whereClause = " where" + busColName + " like '%" + searchText + "%'";
            }
        }
        if (startVal == null || startVal.equalsIgnoreCase("")) {
            startVal = "0";
            endValue = "100";
        } else {
            rowStart = Integer.parseInt(startVal);
//            startVal = "" + 0;
            startVal = "0";
            endValue = String.valueOf((rowStart - 1 + 20));
        }
        pm.elementId = eleId;

        Connection con = pm.getConnection(eleId);

        String connection = con.toString();
        if (connection.contains("mysql")) //             if (!(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE))) {
        {
            if (connection.contains("mysql")) {
                limit = " limit " + startVal + " ," + endValue + "";
            } else {
                limit = " and rownum between " + startVal + " and " + endValue + "";
            }
        }
        finalLimit = limit;

        query = query + whereClause + finalLimit;
        ArrayList al = pm.getRowEdges(query, eleId);
        al.remove(0);

        if (memberID.equalsIgnoreCase(memId)) {
            details.put(busColName, al);
        }
        logger.info("details in dao" + details);

        return details;
    }

    public HashMap getMemberValuesForDim(String userMemId, String subFolderIdUser, ArrayList<Cube> cubes, String userId) {
        DisplayParameters dispParam = new DisplayParameters();
        ProgenParam progenParam = new ProgenParam();
        MetadataDAO metadataDAO = new MetadataDAO();
        HashMap resultMap = new HashMap();
        ArrayList memberArrayList = new ArrayList();
        DimensionMembers dimensionMember = null;
        if (cubes != null && !cubes.isEmpty()) {
            Cube cube = null;
            Iterator<Cube> cubeIter = Iterables.filter(cubes, Cube.getAccessCubePredicate(Integer.parseInt(subFolderIdUser))).iterator();
            if (cubeIter.hasNext()) {
                cube = cubeIter.next();
            }
            ArrayList<Dimension> dimensionsList = cube.getDimensionsList();

            List<DimensionMembers> dimensionMemberslList = null;
            for (Dimension dimension : dimensionsList) {
                dimensionMemberslList = dimension.getDimensionMemberslList();
                Iterator<DimensionMembers> dimMemrsIterator = Iterables.filter(dimensionMemberslList, DimensionMembers.getAccessDimensionMemberPredicate(Integer.parseInt(userMemId))).iterator();
                if (dimMemrsIterator.hasNext()) {
                    dimensionMember = dimMemrsIterator.next();
                    break;
                }
            }
            String query = dispParam.getParameterQuery(Integer.toString(dimensionMember.getElementId().get(0)));
            StringBuilder whereClause = new StringBuilder();

            if (dimensionMemberslList != null) {

                for (DimensionMembers dimensiMember : dimensionMemberslList) {
                    List<MemberSecurity> memberSecurity = dimensiMember.getMemberSecurityList();
                    for (MemberSecurity security : memberSecurity) {
                        if (!security.getMemberValues().isEmpty()) {
                            //
                            if (userId.equalsIgnoreCase(String.valueOf(security.getUserId())) && dimensiMember.getMemberId() != Integer.parseInt(userMemId)) {
                                if (!getAccessLevel().equalsIgnoreCase("roleLevel")) {
                                    whereClause.append(" ").append(metadataDAO.bulidWhereCondition(dimensiMember.getBusTableName(), dimensiMember.getBusTableColumnName().get(0), security.getMemberValues()));
                                }

                            }
                            if (userId.equalsIgnoreCase(String.valueOf(security.getUserId())) && getAccessLevel().equalsIgnoreCase("roleLevel")) {
                                isMemberUseInOtherLevel = true;
                            }
                        }
                    }

                    if (!dimensiMember.getRoleSecurityList().get(0).getMemberValues().isEmpty()) {
                        if (dimensiMember.getMemberId() != Integer.parseInt(userMemId) || !getAccessLevel().equalsIgnoreCase("roleLevel")) {
                            whereClause.append(" ").append(metadataDAO.bulidWhereCondition(dimensiMember.getBusTableName(), dimensiMember.getBusTableColumnName().get(0), dimensiMember.getRoleSecurityList().get(0).getMemberValues()));
                        }
                    }

                }
            }
            try {

                Connection connection = ProgenConnection.getInstance().getConnectionForElement(Integer.toString(dimensionMember.getElementId().get(0)));
                PbReturnObject pbReturnObject = new PbReturnObject();
                query = query + whereClause.toString() + "order by 1 asc";
                pbReturnObject = super.execSelectSQL(query, connection);
                if (pbReturnObject.getRowCount() > 0) {
                    for (int i = 0; i < pbReturnObject.getRowCount(); i++) {

                        memberArrayList.add(pbReturnObject.getFieldValueString(i, 0));

                    }
                }

                resultMap.put(dimensionMember.getBusTableName(), memberArrayList);

            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }

        } else {
        }

        return resultMap;
    }

    public HashMap getMemberValuesForDimRole(String dimId, String dimTabId, String subFolderId) throws Exception {
        ProgenParam pm = new ProgenParam();
        ArrayList bussCols = new ArrayList();
        HashMap lMap = new HashMap();
        String getOtherDimFilterQ = "select * from PRG_ROLE_MEMBER_FILTER where folder_id in(select folder_id from"
                + " prg_user_folder_detail where sub_folder_id in( select sub_folder_id from prg_user_sub_folder_tables where "
                + " sub_folder_tab_id=" + subFolderId + " and dim_tab_id=" + dimTabId + ")) and member_id in (select member_id from "
                + " prg_user_sub_folder_tables where dim_id  in( SELECT  distinct dim_id FROM prg_user_sub_folder_tables"
                + " WHERE member_id=" + dimId + " ) )";

        PbReturnObject pbro = execSelectSQL(getOtherDimFilterQ);
        StringBuilder memIds = new StringBuilder();
        for (int y = 0; y < pbro.getRowCount(); y++) {
//            memIds = memIds + "," + pbro.getFieldValueString(y, "MEMBER_ID");
            memIds.append(",").append(pbro.getFieldValueString(y, "MEMBER_ID"));
        }
        if (memIds.length() > 0) {
            memIds = new StringBuilder(memIds.substring(1));
        }
        HashMap eleIds = new HashMap();
        HashMap eleCols = new HashMap();
        if (memIds.length() > 0) {
            String memElQ = "select * from prg_user_all_ddim_details where info_dim_id in(select dim_id from prg_user_sub_folder_tables where member_id in(" + memIds + ") )  and info_folder_id in(select folder_id from"
                    + " prg_user_folder_detail where sub_folder_id in( select sub_folder_id from prg_user_sub_folder_tables where "
                    + " sub_folder_tab_id=" + subFolderId + " and dim_tab_id=" + dimTabId + ")) and  info_element_id in ("
                    + " select val_element_id from prg_user_all_ddim_key_val_ele where key_sub_folder_id in("
                    + " select sub_folder_id from prg_user_sub_folder_tables where  sub_folder_tab_id=" + subFolderId + " and dim_tab_id=" + dimTabId + "))";
            PbReturnObject eleObj = execSelectSQL(memElQ);
            for (int m = 0; m < eleObj.getRowCount(); m++) {
                eleIds.put(eleObj.getFieldValueString(m, "INFO_MEMBER_ID"), eleObj.getFieldValueString(m, "INFO_ELEMENT_ID"));
                eleCols.put(eleObj.getFieldValueString(m, "INFO_MEMBER_ID"), eleObj.getFieldValueString(m, "INFO_BUSS_COL_ID"));
            }
        }
        String memId = "";
        for (int h = 0; h < pbro.getRowCount(); h++) {
            memId = pbro.getFieldValueString(h, "MEMBER_ID");
            if (!dimId.equalsIgnoreCase(memId)) {
                if (eleIds.containsKey(memId)) {
                    String eleId = eleIds.get(memId).toString();
                    String filterV = "";
                    filterV = pbro.getFieldValueClobString(h, "MEMBER_VALUE");
                    lMap.put(eleId, filterV);
                    String col = eleCols.get(memId).toString();
                    bussCols.add(col);
                }
            }
        }
        PbReportCollection prc = new PbReportCollection();
        String whereClause = prc.getWhereClause(lMap, bussCols);
        String insertedvals = "";
        DisplayParameters dispParam = new DisplayParameters();
        HashMap details = new HashMap();
        String eleQuery = "select * from prg_user_sub_folder_elements where sub_folder_id in(select sub_folder_id from prg_user_sub_folder_tables where sub_folder_tab_id=" + subFolderId + " and dim_tab_id=" + dimTabId + " and member_id=" + dimId + ") and buss_col_id in(select "
                + "distinct col_id from prg_grp_dim_member_details where mem_id in(select member_id from "
                + " prg_user_sub_folder_tables where member_id=" + dimId + " and sub_folder_tab_id=" + subFolderId + "))";

        PbReturnObject eleObj = execSelectSQL(eleQuery);

        for (int m = 0; m < eleObj.getRowCount(); m++) {
            String eleId = eleObj.getFieldValueString(m, "ELEMENT_ID");
            String busColName = eleObj.getFieldValueString(m, "BUSS_COL_NAME");
            String query = dispParam.getParameterQuery(eleId);
            query = query + whereClause + " order by 1";
            pm.elementId = eleId;
            ArrayList al = pm.getRowEdges(query, eleId);
            for (int j = 1; j < al.size(); j++) {

                if (j == 1) {
                    insertedvals = String.valueOf(al.get(j));
                } else {
                    insertedvals += "," + String.valueOf(al.get(j));
                }

            }

            details.put(busColName, al);
        }

//        if (!insertedvals.equalsIgnoreCase("")) {
//            String updatesql = "update PRG_ROLE_MEMBER_FILTER set MEMBER_VALUE='" + insertedvals + "'"
//                    + " where folder_id in(select folder_id from prg_user_folder_detail where sub_folder_id"
//                    + " in(select sub_folder_id from prg_user_sub_folder_tables where sub_folder_tab_id=" + subFolderId + ")) and MEMBER_ID in(" + memId + ")";
//            //////.println("updatesql===" + updatesql);
//            //execModifySQL(updatesql);
//        }
        return details;
    }

    public String getUserFolderDims(String subFolderId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = subFolderId;
        String sql = getResourceBundle().getString("getUserFolderDims");
        String dimId = "";
        String dimName = "";
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            ////.println(" finalQuery f Dims " + finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                dimId = retObj.getFieldValueString(i, colNames[0]);
                dimName = retObj.getFieldValueString(i, colNames[1]);

                // added by susheela start
                String use_report_member = "";
                String isChecked = "";
                use_report_member = retObj.getFieldValueString(i, colNames[2]);
                if (use_report_member.equalsIgnoreCase("Y")) {
                    isChecked = "checked";
                } else {
                    isChecked = "";
                }
                // added by susheela over


                /*
                 * outerBuffer.append("<li class='closed' id='" + dimId + "'>");
                 * outerBuffer.append("<img
                 * src='images/treeViewImages/Dim.gif'></img>");
                 * outerBuffer.append("<span><font size='1px'
                 * face='verdana'>&nbsp;" + dimName + "</font></span>");
                 * outerBuffer.append("<ul>");
                 */
                // modified by susheela start
                outerBuffer.append("<li class='closed'  id='" + dimId + ":" + subFolderId + "'>");
                // outerBuffer.append("<img src='images/treeViewImages/Dim.gif'></img>");
                outerBuffer.append("<Input " + isChecked + " onclick=\"updateDimSt('" + dimId + ":" + subFolderId + "')\" type=\"checkbox\">");
                //modified by Nazneen
//                outerBuffer.append("<span id=" + dimId + ":" + subFolderId + " onclick=\"getUserFolderDimMbrs("+subFolderId+", "+dimId+")\" class=\"dimensionDiv\"><font size='1px' face='verdana'>&nbsp;" + dimName + "</font></span>");
//                outerBuffer.append("<ul id='"+dimId+"_"+subFolderId+"_UL'>");
                outerBuffer.append("<span id=" + dimId + ":" + subFolderId + " class=\"dimensionDiv\"><font size='1px' face='verdana'>&nbsp;" + dimName + "</font></span>");
                outerBuffer.append("<ul>");
                // modified by susheela over

//                outerBuffer.append(getUserFolderDimMbrs(subFolderId, dimId));
                //added by Nazneen
                outerBuffer.append(getUserFolderDimMbrs(subFolderId, dimId));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");

            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();

    }
    //added on 12-12-09

    public ArrayList getAddedMemberValuesForRole(String memberId, String dimTabId, String subFolderTabId) throws Exception {
        String addedDataQ = "select * from prg_user_sub_folder_tables where member_id=" + memberId + " and dim_tab_id=" + dimTabId + " and sub_folder_tab_id=" + subFolderTabId;
        String dataValues = "";
        ArrayList allVals = new ArrayList();
        PbReturnObject pbro = execSelectSQL(addedDataQ);
        String subFolderId = "";
        if (pbro.getRowCount() > 0) {
            subFolderId = pbro.getFieldValueString(0, "SUB_FOLDER_ID");
        }
        String getValuesQ = "select * from prg_role_MEMBER_FILTER where folder_id=(select folder_id from prg_user_folder_detail where sub_folder_id=" + subFolderId + ") and member_id=" + memberId;
        PbReturnObject pbro2 = execSelectSQL(getValuesQ);
        if (pbro2.getRowCount() > 0) {
            dataValues = pbro2.getFieldValueClobString(0, "MEMBER_VALUE");
        }
        if (dataValues == null) {
            dataValues = "";
        }

        String vals[] = (String[]) dataValues.split(",");
        for (int m = 0; m < vals.length; m++) {
            if (!vals[m].equalsIgnoreCase("")) {
                allVals.add(vals[m].replace("'||chr(38)||'", "&"));
            }
        }
        return allVals;
    }

    public String getUserFolderDimsForUser(String subFolderId, String userId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = subFolderId;
        String sql = getResourceBundle().getString("getUserFolderDims");
        String dimId = "";
        String dimName = "";
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                dimId = retObj.getFieldValueString(i, colNames[0]);
                dimName = retObj.getFieldValueString(i, colNames[1]);
                String use_report_member = "";
                String isChecked = "";
                use_report_member = retObj.getFieldValueString(i, colNames[2]);
                if (use_report_member.equalsIgnoreCase("Y")) {
                    isChecked = "checked";
                } else {
                    isChecked = "";
                }
                outerBuffer.append("<li class='closed' id='" + dimId + ":" + subFolderId + "'>");
                outerBuffer.append("<span id=" + dimId + ":" + subFolderId + ":" + userId + " class=\"\"><font size='1px' face='verdana'>&nbsp;" + dimName + "</font></span>");
                outerBuffer.append("<ul>");
                outerBuffer.append(getUserFolderDimMbrsForUser(subFolderId, dimId, userId));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();

    }

    public HashMap getRoleFilterDetails(String memberId, String dimTabId, String subFolderTabId) throws Exception {
        HashMap Details = new HashMap();
        String dispName = "";
        String folderId = "";
        String addedDataQ = "select * from prg_user_sub_folder_tables where member_id=" + memberId + " and dim_tab_id=" + dimTabId + " and sub_folder_tab_id=" + subFolderTabId;
        String dataValues = "";
        ArrayList allVals = new ArrayList();
        PbReturnObject pbro = execSelectSQL(addedDataQ);
        pbro.writeString();
        String subFolderId = "";

        // if(pbro.getRowCount()>0){
        subFolderId = pbro.getFieldValueString(0, "SUB_FOLDER_ID");
        dispName = pbro.getFieldValueString(0, "DISP_NAME");
        // }

        String folderQ = "select FOLDER_ID from prg_user_folder_detail where sub_folder_id=" + subFolderId;
        PbReturnObject all = execSelectSQL(folderQ);
        folderId = all.getFieldValueString(0, "FOLDER_ID");
        Details.put("dispName", dispName);
        Details.put("folderId", folderId);
        return Details;
    }

    public String getUserFolderDimMbrsForUser(String subFolderId, String dimId, String userId) throws Exception {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;

        String sql;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            sql = getResourceBundle().getString("getUserFolderDimMbrsMysql");
        } else {
            sql = getResourceBundle().getString("getUserFolderDimMbrs");
        }
        Object obj[] = new Object[2];
        obj[0] = subFolderId;
        obj[1] = dimId;

        String subFolderTabId = "";
        String mbrId = "";
        String mbrName = "";
        String dimTabId = "";
        String eleId = "";

        String levelQ = "select * from prg_grp_dim_rel_details where rel_id in(select rel_id from prg_grp_dim_rel where dim_id=" + dimId + ") ORDER BY rel_level";
        StringBuffer outerBuffer = new StringBuffer("");
        PbReturnObject relObj = execSelectSQL(levelQ);

        HashMap det = new HashMap();
        try {
            finalQuery = buildQuery(sql, obj);
            retObj = execSelectSQL(finalQuery);
            ////////////////////////////////////////////////////////////////////////////////////.println.println(" finalQuery dim Mem-- " + finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {

                subFolderTabId = retObj.getFieldValueString(i, colNames[0]);
                mbrName = retObj.getFieldValueString(i, colNames[2]);
                dimTabId = retObj.getFieldValueString(i, colNames[3]);
                eleId = retObj.getFieldValueString(i, colNames[6]);

                mbrId = retObj.getFieldValueString(i, colNames[1]);
                String use_report_member = retObj.getFieldValueString(i, colNames[5]);
                String isChecked = "";
                if (use_report_member.equalsIgnoreCase("Y")) {
                    isChecked = "checked";
                } else {
                    isChecked = "";
                }
                ArrayList val = new ArrayList();
                val.add(0, mbrId);
                val.add(1, subFolderTabId);
                val.add(2, subFolderId);
                val.add(3, userId);
                val.add(4, mbrName);
                val.add(5, eleId);
                val.add(6, dimId);
                det.put(mbrId, val);

            }
            if (relObj.getRowCount() > 0) {
                for (int y = 0; y < relObj.getRowCount(); y++) {
                    String memId = relObj.getFieldValueString(y, "MEM_ID");
                    if (det.containsKey(memId)) {
                        ArrayList al = (ArrayList) det.get(memId);
                        subFolderId = (String) al.get(2);
                        mbrName = (String) al.get(4);
                        eleId = (String) al.get(5);
                        dimId = (String) al.get(6);
                        outerBuffer.append("<li class='closed' id='" + subFolderTabId + "'>");
                        outerBuffer.append("<span id=" + memId + ":" + subFolderId + ":" + userId + ":" + mbrName.replace(" ", "~") + ":" + eleId + ":" + dimId + " class=\"dimensionUserDiv\"><font size='1px' face='verdana'>&nbsp;" + mbrName + "</font></span>");
                        outerBuffer.append("</li>");
                    }
                }
            } else {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    mbrId = retObj.getFieldValueString(i, colNames[1]);
                    subFolderTabId = retObj.getFieldValueString(i, colNames[0]);
                    mbrName = retObj.getFieldValueString(i, colNames[2]);
                    dimTabId = retObj.getFieldValueString(i, colNames[3]);
                    outerBuffer.append("<li class='closed' id='" + subFolderTabId + "'>");
                    outerBuffer.append("<span id=" + mbrId + ":" + subFolderId + ":" + userId + ":" + mbrName.replace(" ", "~") + ":" + eleId + ":" + dimId + " class=\"dimensionUserDiv\"><font size='1px' face='verdana'>&nbsp;" + mbrName + "</font></span>");
                    outerBuffer.append("</li>");
                }
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();
    }

    public String getUsersForRole(String folderId, String subFolderIdDim) throws Exception {
        StringBuffer outerBuffer = new StringBuffer("");
//        String userRoleQ = "select * from prg_grp_user_assignment where grp_id in(select grp_id from prg_user_folder where folder_id=" + folderId + ")";
        String userRoleQ = "SELECT USER_ASSI_ID user_grp_id,USER_ID,GRP_ID ,START_DATE,END_DATE,"
                + "case when END_DATE is null then 'Y'  when END_DATE -START_DATE > 0 then 'Y' else 'N' end IS_ACTIVE"
                + ",b.pu_login_id USER_NAME,USER_FOLDER_ID folder_id FROM PRG_GRP_USER_FOLDER_ASSIGNMENT a,PRG_AR_USERS  b where a.user_id = b.PU_ID and USER_FOLDER_ID =" + folderId + "";
        PbReturnObject userData = execSelectSQL(userRoleQ);
        for (int p = 0; p < userData.getRowCount(); p++) {
            String userId = "";
            String userName = "";
            userId = userData.getFieldValueString(p, "USER_ID");
            userName = userData.getFieldValueString(p, "USER_NAME");
            outerBuffer.append("<li id='" + userId + "'>");
            //                outerBuffer.append("<ul>");
            //outerBuffer.append("<li class='closed' id='" + userId + "'>");
            outerBuffer.append("<span id='" + subFolderIdDim + ":" + userId + "' class=\"userDrillMenu\"><font size='1px' face='verdana'>&nbsp;" + userName + "</font></span>");

            // outerBuffer.append("<li class='closed' id=''>");
            outerBuffer.append("<span><font size='1px' face='verdana'>&nbsp;</font></span>");
            outerBuffer.append("<ul>");
            outerBuffer.append(getUserFolderDimsForUser(subFolderIdDim, userId));
            outerBuffer.append("</ul>");
            //outerBuffer.append("</li>");

            // outerBuffer.append("</li>");
            //outerBuffer.append("</ul>");
            outerBuffer.append("</li>");
        }
        return outerBuffer.toString();
    }

    public PbReturnObject getUserCustomDrill(String subFolderId, String userId) throws Exception {
        PbReturnObject all = new PbReturnObject();
        PbReturnObject members = new PbReturnObject();
        PbReturnObject details = new PbReturnObject();
        PbReturnObject relDetailsObj = new PbReturnObject();
        PbReturnObject dimDetails = new PbReturnObject();
        String drillQ = "Select * from PRG_USER_ROLE_DRILL where user_id=" + userId + " and sub_folder_id=" + subFolderId;
        PbReturnObject pbro = execSelectSQL(drillQ);
        if (pbro.getRowCount() == 0) {
            String dims = "select dim_id, buss_table_id, dim_name, member_id, member_name from PRG_USER_SUB_FOLDER_TABLES where sub_folder_id in(select sub_folder_id "
                    + " from PRG_USER_FOLDER_DETAIL where sub_folder_id='" + subFolderId + "' and sub_folder_name in('Dimensions')) order by dim_id";
            dimDetails = execSelectSQL(dims);

            String drillForRoleQ = "Select * from prg_grp_role_custom_drill where sub_folder_id=" + subFolderId;
            members = execSelectSQL(drillForRoleQ);
            String detailsQ = "select dim_id, buss_table_id, dim_name, member_id, member_name from PRG_USER_SUB_FOLDER_TABLES where sub_folder_id in(select sub_folder_id  from "
                    + "PRG_USER_FOLDER_DETAIL where sub_folder_id='" + subFolderId + "' and sub_folder_name in('Dimensions')) order by dim_id";
            details = execSelectSQL(detailsQ);
            String relDetails = "select d.mem_id,d.rel_level, d.rel_id, r.dim_id from prg_grp_dim_rel_details d, "
                    + " prg_grp_dim_rel r where d.rel_id in(select rel_id from prg_grp_dim_rel where dim_id "
                    + " in(select distinct dim_id from PRG_USER_SUB_FOLDER_TABLES  where sub_folder_id  "
                    + " in(select sub_folder_id from PRG_USER_FOLDER_DETAIL where sub_folder_id='" + subFolderId + "' and sub_folder_name "
                    + "  in('Dimensions'))) and is_default='Y') and d.rel_id= r.rel_id order by rel_id ,rel_level";
            relDetailsObj = execSelectSQL(relDetails);
        } else {
            String dims = "select dim_id, buss_table_id, dim_name, member_id, member_name from PRG_USER_SUB_FOLDER_TABLES where sub_folder_id in(select sub_folder_id "
                    + " from PRG_USER_FOLDER_DETAIL where sub_folder_id='" + subFolderId + "' and sub_folder_name in('Dimensions')) order by dim_id";
            dimDetails = execSelectSQL(dims);

            String drillForRoleQ = "select * from prg_grp_role_custom_drill where drill_id in(select drill_id from  PRG_USER_ROLE_DRILL where user_id=" + userId + " and sub_folder_id=" + subFolderId + ")";
            members = execSelectSQL(drillForRoleQ);
            String detailsQ = "select dim_id, buss_table_id, dim_name, member_id, member_name from PRG_USER_SUB_FOLDER_TABLES where sub_folder_id in(select sub_folder_id  from "
                    + "PRG_USER_FOLDER_DETAIL where sub_folder_id='" + subFolderId + "' and sub_folder_name in('Dimensions')) order by dim_id";
            details = execSelectSQL(detailsQ);

            String relDetails = "select d.mem_id,d.rel_level, d.rel_id, r.dim_id from prg_grp_dim_rel_details d, "
                    + " prg_grp_dim_rel r where d.rel_id in(select rel_id from prg_grp_dim_rel where dim_id "
                    + " in(select distinct dim_id from PRG_USER_SUB_FOLDER_TABLES  where sub_folder_id  "
                    + " in(select sub_folder_id from PRG_USER_FOLDER_DETAIL where sub_folder_id='" + subFolderId + "' and sub_folder_name "
                    + "  in('Dimensions'))) and is_default='Y') and d.rel_id= r.rel_id order by rel_id ,rel_level";
            relDetailsObj = execSelectSQL(relDetails);
        }
        all.setObject("Details", details);
        all.setObject("Members", members);
        all.setObject("RelDetails", relDetailsObj);
        all.setObject("DimDetails", dimDetails);
        return all;
    }

    public HashMap getUserDrilStatus(String subFolderId, String userId) throws Exception {
        String status = "";
        HashMap details = new HashMap();
        String userDrillId = "";
        String statusQ = "select * from PRG_USER_ROLE_DRILL where sub_folder_id=" + subFolderId + " and user_id=" + userId;
        PbReturnObject pbro = execSelectSQL(statusQ);
        if (pbro.getRowCount() == 0) {
            status = "insert";
        } else {
            status = "update";
            userDrillId = pbro.getFieldValueString(0, "DRILL_ID");
        }
        details.put("status", status);
        details.put("userDrillId", userDrillId);
        return details;
    }

    public void updateUserCustomDrill(HashMap members, String userId, String subFolderId, String userDrillId) throws Exception {
        String updateCustomDrill = "update prg_grp_role_custom_drill set child_member_id=& where sub_folder_id=& and member_id=& and drill_id=&";
        Set membersSet = members.keySet();
        ArrayList uList = new ArrayList();
        String[] membersSetK = (String[]) membersSet.toArray(new String[0]);
        for (int i = 0; i < membersSetK.length; i++) {
            String memId = membersSetK[i];
            String chId = "";
            if (members.containsKey(memId)) {
                chId = members.get(memId).toString();
            }
            Object updateObj[] = new Object[4];
            updateObj[0] = chId;
            updateObj[1] = subFolderId;
            updateObj[2] = memId;
            updateObj[3] = userDrillId;
            String finupdateCustomDrill = buildQuery(updateCustomDrill, updateObj);
            uList.add(finupdateCustomDrill);
        }
        try {
            executeMultiple(uList);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void insertUserCustomDrill(HashMap Vals, String userId, String subFolderId) throws Exception {
        String addRoleCustomDrill = getResourceBundle().getString("addRoleCustomDrill");
        ArrayList insertList = new ArrayList();
        String sequenceQuery = getResourceBundle().getString("getSequenceNumber");
        Object seqObj[] = new Object[1];
        seqObj[0] = "USER_DRILL_ID_SEQ";
        String seqQuery = buildQuery(sequenceQuery, seqObj);
        int drillId = getSequenceNumber(seqQuery);
        String allV[] = (String[]) Vals.keySet().toArray(new String[0]);
        for (int m = 0; m < allV.length; m++) {
            String memId = allV[m];
            String childMem = (String) Vals.get(memId);
            Object insertObj[] = new Object[5];
            insertObj[0] = "";
            insertObj[1] = subFolderId;
            insertObj[2] = memId;
            insertObj[3] = childMem;
            insertObj[4] = Integer.valueOf(drillId);
            String finaddRoleCustomDrill = buildQuery(addRoleCustomDrill, insertObj);
            insertList.add(finaddRoleCustomDrill);
        }

        String insertMasterDrill = "insert into PRG_USER_ROLE_DRILL(DRILL_SEQ_NUMBER,USER_ID,DRILL_ID,SUB_FOLDER_ID) values(USER_DRILL_ID_SEQ2.nextval,'&','&','&')";
        Object inOb[] = new Object[3];
        inOb[0] = userId;
        inOb[1] = Integer.valueOf(drillId);
        inOb[2] = subFolderId;
        String inQuery = buildQuery(insertMasterDrill, inOb);
        insertList.add(inQuery);
        try {
            executeMultiple(insertList);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

    }

    public String updateMemberValueStatus(String userId, String subFolderId, String memberId, String memberValue, String filterId, String elementId, String dimId) throws Exception {
        String status = "";
        String folderQ = "select * from prg_user_folder_detail where sub_folder_id=" + subFolderId;
        PbReturnObject folderObj = execSelectSQL(folderQ);
        String folderId = "";
        folderId = folderObj.getFieldValueString(0, "FOLDER_ID");
        String deleteQ = "delete from prg_user_role_member_filter where user_filter_id=" + filterId;
        OraclePreparedStatement opstmt = null;
        Connection connection = ProgenConnection.getInstance().getConnection();
//        String getElementIDAndDimid = "select UF.DIM_ID,UE.ELEMENT_ID from PRG_USER_SUB_FOLDER_TABLES UF ,prg_user_sub_folder_elements UE where UF.BUSS_TABLE_ID=UE.BUSS_TABLE_ID and ue.sub_folder_id=" + subFolderId + " and uf.member_id=" + memberId + " and uf.dim_id=(select dim_id from prg_grp_dim_member where member_id=" + memberId + ") and UF.sub_folder_id=ue.sub_folder_id";
//        PbReturnObject pbReturnObject = super.execSelectSQL(getElementIDAndDimid);
        String addUserMemberFilter = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            addUserMemberFilter = "insert into prg_user_role_member_filter(USER_ID,MEMBER_ID,FOLDER_ID,MEMBER_VALUE,DIM_ID,ELEMENT_ID) values(?,?,?,?,?,?)";//resBundle.getString("addUserMemberFilter");
        } else {
            addUserMemberFilter = "insert into prg_user_role_member_filter(USER_FILTER_ID,USER_ID,MEMBER_ID,FOLDER_ID,MEMBER_VALUE,DIM_ID,ELEMENT_ID) values(USER_FILTER_ID_SEQ.nextval,?,?,?,?,?,?)";//resBundle.getString("addUserMemberFilter");
        }
        if (!memberValue.equalsIgnoreCase("")) {
            opstmt = (OraclePreparedStatement) connection.prepareStatement(addUserMemberFilter);
            opstmt.setString(1, userId);
            opstmt.setString(2, memberId);
            opstmt.setString(3, folderId);
            opstmt.setStringForClob(4, memberValue.replace("'", "''"));
            opstmt.setStringForClob(5, dimId);
            opstmt.setStringForClob(6, elementId);
            int rows = opstmt.executeUpdate();
        }
        try {
            String filterIdQuery = "select USER_FILTER_ID from PRG_USER_ROLE_MEMBER_FILTER order by USER_FILTER_ID desc";
            PbReturnObject pbRet = null;
            pbRet = new PbDb().execSelectSQL(filterIdQuery);
            String filterId1 = pbRet.getFieldValueString(0, 0);
            updateSecMemberValueStatus(userId, subFolderId, memberValue, dimId, elementId, filterId, filterId1);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        if (connection != null) {
            connection.close();
        }
        try {
            ArrayList del = new ArrayList();
            del.add(deleteQ);
            executeMultiple(del);
        } catch (Exception x) {
            logger.error("Exception:", x);
        }

        return status;
    }

    public HashMap checkMemberValueStatus(String userId, String subFolderId, String memberId) throws Exception {
        String status = "";
        HashMap details = new HashMap();
        String addedDataQ = "select * from prg_user_role_member_filter where user_id=" + userId + " and folder_id in(select folder_id from prg_user_folder_detail where sub_folder_id=" + subFolderId + ") and member_id=" + memberId;
        PbReturnObject pbro = execSelectSQL(addedDataQ);
        String filterId = "";
        if (pbro.getRowCount() > 0) {
            status = "true";
            filterId = pbro.getFieldValueString(0, "USER_FILTER_ID");
        } else {
            status = "false";
        }
        details.put("status", status);
        details.put("filterId", filterId);
        return details;
    }

    public ArrayList getAddedMemberValues(String subFolderIdUser, String userMemId, String accessLevel, String userId) {
        String addedDataQ = "";

        if (accessLevel.equalsIgnoreCase("roleLevel")) {
            addedDataQ = "select * from prg_role_member_filter where folder_id in(select folder_id from prg_user_folder_detail where sub_folder_id=" + subFolderIdUser + ") and member_id=" + userMemId;
        } else {
            addedDataQ = "select * from prg_user_role_member_filter where  folder_id in(select folder_id from prg_user_folder_detail where sub_folder_id=" + subFolderIdUser + ") and member_id=" + userMemId + " and user_id=" + userId;
        }

        String dataValues = "";
        ArrayList allVals = new ArrayList();
        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = execSelectSQL(addedDataQ);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (pbro.getRowCount() > 0) {
            dataValues = pbro.getFieldValueClobString(0, "MEMBER_VALUE");
        }

        ArrayList<String> vals = fromXML(dataValues);
        for (String str : vals) {
            if (!str.equalsIgnoreCase("")) {
                allVals.add(str.replace("'||chr(38)||'", "&"));
            }
        }
        return allVals;
    }

    public void addUserMemberFilter(String userId, String subFolderId, String memberValue, String memberId, String elementId, String dimId) throws Exception {
        String folderQ = "select * from prg_user_folder_detail where sub_folder_id=" + subFolderId;
        PbReturnObject folderObj = execSelectSQL(folderQ);
        String folderId = "";
//        folderId = folderObj.getFieldValueString(0, "FOLDER_ID");
        folderId = folderObj.getFieldValueString(0, 0);
        String addUserMemberFilter = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            addUserMemberFilter = "insert into prg_user_role_member_filter(USER_ID,MEMBER_ID,FOLDER_ID,MEMBER_VALUE,DIM_ID,ELEMENT_ID) values(&,&,&,'&',&,&)";//resBundle.getString("addUserMemberFilter");
        } else {
            addUserMemberFilter = "insert into prg_user_role_member_filter(USER_FILTER_ID,USER_ID,MEMBER_ID,FOLDER_ID,MEMBER_VALUE,DIM_ID,ELEMENT_ID) values(USER_FILTER_ID_SEQ.nextval,?,?,?,?,?,?)";//resBundle.getString("addUserMemberFilter");
        }
        OraclePreparedStatement opstmt = null;
        Connection connection = ProgenConnection.getInstance().getConnection();
//        String getElementIDAndDimid = "select UF.DIM_ID,UE.ELEMENT_ID from PRG_USER_SUB_FOLDER_TABLES UF ,prg_user_sub_folder_elements UE where UF.BUSS_TABLE_ID=UE.BUSS_TABLE_ID and ue.sub_folder_id=" + subFolderId + " and uf.member_id=" + memberId + " and uf.dim_id=(select dim_id from prg_grp_dim_member where member_id=" + memberId + ") and UF.sub_folder_id=ue.sub_folder_id";
//
//        PbReturnObject pbReturnObject = super.execSelectSQL(getElementIDAndDimid);
        if (!memberValue.equalsIgnoreCase("")) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                Object insertObj[] = new Object[6];
                insertObj[0] = userId;
                insertObj[1] = memberId;
                insertObj[2] = folderId;
//            modified by Nazneen
//            insertObj[3] = memberValue;
                insertObj[3] = memberValue.replace("'", "''");
                insertObj[4] = dimId;
                insertObj[5] = elementId;
                String fininsertQ = buildQuery(addUserMemberFilter, insertObj);
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                    int rows = super.execUpdateSQL(fininsertQ);
                } else {
                    int rows = super.execUpdateSQL(fininsertQ, connection);
                }
            } else {
                opstmt = (OraclePreparedStatement) connection.prepareStatement(addUserMemberFilter);
                opstmt.setString(1, userId);
                opstmt.setString(2, memberId);
                opstmt.setString(3, folderId);
//             modified by Nazneen
//            opstmt.setStringForClob(4, memberValue);
                opstmt.setStringForClob(4, memberValue.replace("'", "''"));
                opstmt.setString(5, dimId);
                opstmt.setString(6, elementId);
                int rows = opstmt.executeUpdate();
            }
        }
        try {
            String filterIdQuery = "select USER_FILTER_ID from PRG_USER_ROLE_MEMBER_FILTER order by USER_FILTER_ID desc";
            PbReturnObject pbRet = null;
            pbRet = new PbDb().execSelectSQL(filterIdQuery);
            String filterId1 = pbRet.getFieldValueString(0, 0);
            addSecMemberFilter(userId, subFolderId, memberValue, dimId, elementId, filterId1);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (connection != null) {
            connection.close();
        }
    }
    // added by susheela 01-12-09 over

    /*
     * public String getUserFolderDims(String subFolderId) { PbReturnObject
     * retObj = null; String finalQuery = null; String[] colNames = null; Object
     * obj[] = new Object[1]; obj[0] = subFolderId; String sql =
     * resBundle.getString("getUserFolderDims"); String dimId = ""; String
     * dimName = ""; StringBuffer outerBuffer = new StringBuffer(""); try {
     * finalQuery = buildQuery(sql, obj); retObj = execSelectSQL(finalQuery);
     * colNames = retObj.getColumnNames(); for (int i = 0; i <
     * retObj.getRowCount(); i++) { dimId = retObj.getFieldValueString(i,
     * colNames[0]); dimName = retObj.getFieldValueString(i, colNames[1]);
     *
     * // added by susheela start String use_report_member = ""; String
     * isChecked = ""; use_report_member = retObj.getFieldValueString(i,
     * colNames[2]); if (use_report_member.equalsIgnoreCase("Y")) { isChecked =
     * "checked"; } else { isChecked = ""; } // added by susheela over
     *
     *
     *
     *
     * // modified by susheela start outerBuffer.append("<li class='closed'
     * id='" + dimId +":"+subFolderId+"'>"); // outerBuffer.append("<img
     * src='images/treeViewImages/Dim.gif'></img>"); outerBuffer.append("<Input
     * " + isChecked + " onclick=\"updateDimSt('" + dimId + ":" + subFolderId +
     * "')\" type=\"checkbox\">");
     *
     * outerBuffer.append("<span id="+dimId +":"+subFolderId+"
     * class=\"dimensionDiv\"><font size='1px' face='verdana'>&nbsp;" + dimName
     * + "</font></span>"); outerBuffer.append("<ul>"); // modified by susheela
     * over
     *
     * outerBuffer.append(getUserFolderDimMbrs(subFolderId, dimId));
     *
     * outerBuffer.append("</ul>"); outerBuffer.append("</li>");
     *
     * }
     * } catch (Exception exception) { logger.error("Exception:",exception); }
     * return outerBuffer.toString();
     *
     * }
     */
    public String getUserFolderDimMbrs(String subFolderId, String dimId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        String sql;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            sql = getResourceBundle().getString("getUserFolderDimMbrsMysql");
        } else {
            sql = getResourceBundle().getString("getUserFolderDimMbrs");
        }
        Object obj[] = new Object[2];
        obj[0] = subFolderId;
        obj[1] = dimId;

        String subFolderTabId = "";
        String mbrId = "";
        String mbrName = "";
        String dimTabId = "";
        String eleId = "";

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            ////.println(" finalQuery f dim Mbrs " + finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {

                subFolderTabId = retObj.getFieldValueString(i, colNames[0]);
                mbrName = retObj.getFieldValueString(i, colNames[2]);
                dimTabId = retObj.getFieldValueString(i, colNames[3]);
                eleId = retObj.getFieldValueString(i, colNames[6]);

                // modified by susheela start
                mbrId = retObj.getFieldValueString(i, colNames[1]);
                //added by susheela start 16thNov
                String use_report_member = retObj.getFieldValueString(i, colNames[5]);
                String isChecked = "";
                if (use_report_member.equalsIgnoreCase("Y")) {
                    isChecked = "checked";
                } else {
                    isChecked = "";
                }
                //added by susheela over 16thNov

                outerBuffer.append("<li class='closed' id='" + subFolderTabId + "'>");
                // outerBuffer.append("<img src='images/treeViewImages/Dim.gif'></img>");
                outerBuffer.append("<Input " + isChecked + " id='" + subFolderId + "' onclick=\"updateDimensionMemberSt('" + subFolderTabId + ":" + mbrId + ":" + dimTabId + ":" + eleId + ":" + subFolderId + ":" + dimId + "')\" type=\"checkbox\">");

                outerBuffer.append("<span id=" + subFolderTabId + ":" + mbrId + ":" + dimTabId + ":" + eleId + ":" + subFolderId + ":" + dimId + " class=\"dimensionMemberDiv\"><font size='1px' face='verdana'> " + mbrName + "</font></span>");
                // modified by susheela over

                /*
                 * outerBuffer.append("<li class='closed' id='" + subFolderTabId
                 * + "'>"); outerBuffer.append("<img
                 * src='images/treeViewImages/Dim.gif'></img>");
                 * outerBuffer.append("<span><font size='1px'
                 * face='verdana'>&nbsp;" + mbrName + "</font></span>");
                 */
                outerBuffer.append("<ul>");

                outerBuffer.append(getUserFolderDimMbrsColumns(subFolderTabId));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");

            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        //return dimensionList;
        return outerBuffer.toString();
    }

    private String getUserFolderDimMbrsColumns(String subFolderTabId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = subFolderTabId;
        String sql = getResourceBundle().getString("getUserFolderDimMbrsCols");

        String elementId = "";
        String mbrColName = "";
        String mbrColDispName = "";
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            //////.println(" finalQuery dim Cols " + finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                elementId = retObj.getFieldValueString(i, colNames[0]);
                mbrColName = retObj.getFieldValueString(i, colNames[1]);
                mbrColDispName = retObj.getFieldValueString(i, colNames[2]);

                outerBuffer.append("<li class='closed' id='" + elementId + "'>");
                outerBuffer.append("<img src='images/treeViewImages/Dim.gif'></img>");
                outerBuffer.append("<span><font size='1px' face='verdana'>&nbsp;" + mbrColName + "</font></span>");
                //outerBuffer.append("<ul>");

                //outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();
    }

    public String getUserFolderFacts(String subFolderId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = subFolderId;
        String sql = getResourceBundle().getString("getUserFolderFacts");
        String factId = "";
        String factName = "";
        String subFolderTabId = "";
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            ////.println(" finalQuery facts " + finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                factId = retObj.getFieldValueString(i, colNames[0]);
                factName = retObj.getFieldValueString(i, colNames[1]);
                subFolderTabId = factId;
                String use_report_member = retObj.getFieldValueString(i, colNames[2]);
                String toolTip = retObj.getFieldValueString(i, colNames[3]);
                String isChecked = "";
                if (use_report_member.equalsIgnoreCase("Y")) {
                    isChecked = "checked";
                } else {
                    isChecked = "";
                }
                outerBuffer.append("<li class='closed' id='" + factId + "'>");
                // outerBuffer.append("<img src='images/treeViewImages/database_table.png'></img>");
                outerBuffer.append("<Input id=" + subFolderTabId + "chk " + isChecked + " onclick=\"updateFactSt('" + subFolderTabId + "'),checkNunchkfacts('" + subFolderTabId + "chk')\" type=\"checkbox\">");
//                modified by Nazneen
//                outerBuffer.append("<span class='userFilterFormulaMenu'  onclick=\"getUserFolderFactsColumns("+subFolderTabId+","+subFolderId+")\"><font size='1px' face='verdana' title='" + toolTip + "'>&nbsp;" + factName + "</font></span>");
//                outerBuffer.append("<ul id='"+subFolderTabId+"_"+subFolderId+"_UL'>");
                outerBuffer.append("<span class='userFilterFormulaMenu'><font size='1px' face='verdana' title='" + toolTip + "'>&nbsp;" + factName + "</font></span>");
                outerBuffer.append("<ul>");
                //modified by Nazneen
//                outerBuffer.append(getUserFolderFactsColumns(subFolderTabId, subFolderId));
                outerBuffer.append(getUserFolderFactsColumns(subFolderTabId, subFolderId));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");

            }

        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();
    }

    public String getUserFolderFactsColumns(String subFolderTabId, String subFolderId) {
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = subFolderTabId;
        String sql = "SELECT ELEMENT_ID,USER_COL_NAME,USE_REPORT_FLAG,BUSS_COL_NAME,ref_element_type,user_col_desc FROM PRG_USER_SUB_FOLDER_ELEMENTS where SUB_FOLDER_TAB_ID='&' and ref_element_type=1 order by BUSS_COL_NAME, ref_element_type ";
        //resBundle.getString("getUserFolderFactsColumns");
        String factColName = "";
        String factColId = "";
        String factdispName = "";
        String formula = "";

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            ////.println(" finalQuery fact cols " + finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            int i = 0, total = 0, h = 0;
            for (i = 0; i < retObj.getRowCount(); i++) {

                factColId = retObj.getFieldValueString(i, colNames[0]);
                factColName = retObj.getFieldValueString(i, colNames[1]);
                factdispName = retObj.getFieldValueString(i, colNames[5]);
                String useReportFl = "";
                useReportFl = retObj.getFieldValueString(i, 2);
                String isSelected = "";
                if (useReportFl == null || useReportFl.equalsIgnoreCase("Y")) {
                    isSelected = "checked";
                } else if (useReportFl.equalsIgnoreCase("N")) {
                    isSelected = "";
                }

                outerBuffer.append("<li class='closed' id='" + factColId + "'>");
//                name=child-" + i + "-" + subFolderTabId + "
                outerBuffer.append("<Input id=child-" + total + "-" + subFolderTabId + " onclick=getElement('" + factColId + "','" + subFolderTabId + "') name=" + factColId + " " + isSelected + " type=\"checkbox\">");
                // outerBuffer.append("<Input onclick=getElement('" + factColId + "') name=" + factColId + " " + isSelected + " type=\"checkbox\">");
                outerBuffer.append("<span class='userdelete'><font size='1px' face='verdana'>&nbsp;" + factdispName + "</font></span>");
                sql = "SELECT ELEMENT_ID,USER_COL_NAME,USE_REPORT_FLAG,BUSS_COL_NAME,ref_element_type,user_col_desc,DISPLAY_FORMULA FROM PRG_USER_SUB_FOLDER_ELEMENTS where SUB_FOLDER_TAB_ID=" + subFolderTabId + " and ref_element_type in(2,3,4)and ref_element_id=" + factColId + " order by BUSS_COL_NAME, ref_element_type ";
                //  //////.println("====="+sql);
                retObj1 = execSelectSQL(sql);
                if (retObj1.getRowCount() > 0) {
                    outerBuffer.append("<ul>");
                    for (h = 0; h < retObj1.getRowCount(); h++) {
                        total++;
                        factColId = retObj1.getFieldValueString(h, colNames[0]);
                        factColName = retObj1.getFieldValueString(h, colNames[1]);
                        factdispName = retObj1.getFieldValueString(h, colNames[5]);
                        formula = retObj1.getFieldValueString(h, 6);
                        outerBuffer.append("<li class='closed' id='" + factColId + "'>");
                        //name=" + factColId + "
                        outerBuffer.append("<Input id=child-" + total + "-" + subFolderTabId + " onclick=getElement('" + factColId + "','" + subFolderTabId + "') name=" + factColId + " " + isSelected + " type=\"checkbox\">");
                        outerBuffer.append("<span class='userdelete' title='" + formula + "'><font size='1px' face='verdana'>&nbsp;" + factdispName + "</font></span>");
                        outerBuffer.append("</li>");
                    }
                    outerBuffer.append("</ul>");
                }
                total++;
                outerBuffer.append("</li>");
            }

            //code to add formula columns
            String getCalculatedFormulasfacts = "SELECT DISTINCT sub_folder_tab_id, disp_name FROM PRG_USER_SUB_FOLDER_TABLES where is_fact='Y' "
                    + " and sub_folder_id in (" + subFolderId + ") and (disp_name in('Calculated Facts'))order by disp_name ";
            //finalQuery=buildQuery(getCalculatedFormulasfacts, Obj);
            //////////////////////////////////////////////////////////////////////////////.println.println("final getCalculatedFormulas---"+getCalculatedFormulasfacts);
            PbReturnObject pbrofacts = execSelectSQL(getCalculatedFormulasfacts);
            sql = "SELECT ELEMENT_ID,USER_COL_NAME,USE_REPORT_FLAG,BUSS_COL_NAME,REF_ELEMENT_TYPE,REFFERED_ELEMENTS,USER_COL_DESC,DISPLAY_FORMULA FROM PRG_USER_SUB_FOLDER_ELEMENTS where SUB_FOLDER_TAB_ID=(" + pbrofacts.getFieldValueInt(0, 0)
                    + ")  and ref_element_type=1 order by BUSS_COL_NAME, ref_element_type";
            PbReturnObject pbroele = execSelectSQL(sql);
            for (i = 0; i < pbroele.getRowCount(); i++) {
                String testQuery = "";
                if (pbroele.getFieldValueString(i, 5) == null || pbroele.getFieldValueString(i, 5).trim().equalsIgnoreCase("")) {
                    testQuery = "select distinct sub_folder_tab_id  from PRG_USER_SUB_FOLDER_ELEMENTS where element_id in(null) and sub_folder_tab_id=" + subFolderTabId;
                } else {
                    testQuery = "select distinct sub_folder_tab_id  from PRG_USER_SUB_FOLDER_ELEMENTS where element_id in(" + pbroele.getFieldValueString(i, 5) + ") and sub_folder_tab_id=" + subFolderTabId;
                }
                //////////////////////////////////////////////////////////////////////////////.println.println("sql-----" + testQuery);
                PbReturnObject testpbro = execSelectSQL(testQuery);
                if (testpbro.getRowCount() > 0) {
                    factColId = pbroele.getFieldValueString(i, colNames[0]);
                    factColName = pbroele.getFieldValueString(i, colNames[1]);
                    factdispName = pbroele.getFieldValueString(i, "USER_COL_DESC");
                    formula = pbroele.getFieldValueString(i, "DISPLAY_FORMULA");
                    String useReportFl = "";
                    useReportFl = pbroele.getFieldValueString(i, 2);
                    String isSelected = "";
                    if (useReportFl == null || useReportFl.equalsIgnoreCase("Y")) {
                        isSelected = "checked";
                    } else if (useReportFl.equalsIgnoreCase("N")) {
                        isSelected = "";
                    }

                    outerBuffer.append("<li class='closed' id='" + factColId + "'>");
//                     name=child-" + i + "-" + subFolderTabId + "
                    outerBuffer.append("<Input id=child-" + total + "-" + subFolderTabId + " onclick=getElement('" + factColId + "','" + subFolderTabId + "') name=" + factColId + " " + isSelected + " type=\"checkbox\">");
                    //outerBuffer.append("<Input onclick=getElement('" + factColId + "') name=" + factColId + " " + isSelected + " type=\"checkbox\">");
                    outerBuffer.append("<span class='userdelete' title='" + formula + "'><font size='1px' face='verdana'>&nbsp;" + factdispName + "</font></span>");
                    sql = "SELECT ELEMENT_ID,USER_COL_NAME,USE_REPORT_FLAG,BUSS_COL_NAME,ref_element_type,REFFERED_ELEMENTS,user_col_desc,display_formula FROM PRG_USER_SUB_FOLDER_ELEMENTS where SUB_FOLDER_TAB_ID=(" + testpbro.getFieldValueInt(0, 0)
                            + ")  and ref_element_type in(2,3,4) and ref_element_id=" + factColId + " order by BUSS_COL_NAME, ref_element_type";
                    //  //////.println("====="+sql);
                    retObj1 = execSelectSQL(sql);
                    if (retObj1.getRowCount() > 0) {
                        outerBuffer.append("<ul>");
                        for (h = 0; h < retObj1.getRowCount(); h++) {
                            total++;
                            factColId = retObj1.getFieldValueString(h, colNames[0]);
                            factColName = retObj1.getFieldValueString(h, colNames[1]);
                            factdispName = retObj1.getFieldValueString(h, colNames[5]);
                            formula = retObj1.getFieldValueString(h, "DISPLAY_FORMULA");
                            outerBuffer.append("<li class='closed' id='" + factColId + "'>");
                            outerBuffer.append("<Input id=child-" + total + "-" + subFolderTabId + " onclick=getElement('" + factColId + "','" + subFolderTabId + "') name=" + factColId + " " + isSelected + " type=\"checkbox\">");
                            outerBuffer.append("<span class='userdelete' title='" + formula + "'><font size='1px' face='verdana'>&nbsp;" + factdispName + "</font></span>");
                            outerBuffer.append("</li>");
                        }
                        outerBuffer.append("</ul>");
                    }
                    total++;
                    outerBuffer.append("</li>");
                }
            }
            outerBuffer.append("<input type=hidden name=text" + subFolderTabId + " id=text" + subFolderTabId + " value=" + total + ">");

        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();
    }

    public String getUserFolderBuckets(String subFolderId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = subFolderId;
        String sql = getResourceBundle().getString("getUserFolderBuckets");
        String bucketId = "";
        String bucketName = "";
        String subFolderTabId = "";
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            ////.println("finalQuery\t" + finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                bucketId = retObj.getFieldValueString(i, colNames[0]);
                bucketName = retObj.getFieldValueString(i, colNames[1]);

                subFolderTabId = bucketId;

                outerBuffer.append("<li class='closed' id='" + bucketId + "'>");
                outerBuffer.append("<img src='images/treeViewImages/Dim.gif'></img>");
                outerBuffer.append("<span><font size='1px' face='verdana'>&nbsp;" + bucketName.replaceAll("_", " ") + "</font></span>");
                outerBuffer.append("<ul>");

                outerBuffer.append(getUserFolderBucketsColumns(subFolderTabId));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");

            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();
    }
    //added by susheela start

    public void updateElementId(String elementId) throws Exception {
        String getStatusQ = "SELECT ELEMENT_ID,USER_COL_NAME,use_report_flag  FROM PRG_USER_SUB_FOLDER_ELEMENTS where ELEMENT_ID=" + elementId;
        PbReturnObject pbro = execSelectSQL(getStatusQ);
        String status = pbro.getFieldValueString(0, 2);
        String newStatus = "";
        if (status.equalsIgnoreCase("Y")) {
            newStatus = "N";
        } else {
            newStatus = "Y";
        }
        String updateQ = "update  PRG_USER_SUB_FOLDER_ELEMENTS set use_report_flag='" + newStatus + "' where ELEMENT_ID=" + elementId;
        // String updateUserFolderId = "update  PRG_USER_all_info_details set use_report_flag='" + newStatus + "' where ELEMENT_ID=" + elementId;
        execModifySQL(updateQ);
        // execModifySQL(updateUserFolderId);
    }

    public void deleteRoleDimension(String dimId, String subFolderId) throws Exception {
        String deleteRoleDimQ = "delete from PRG_USER_SUB_FOLDER_TABLES where is_dimension='Y' and sub_folder_id='" + subFolderId + "' and DIM_ID=" + dimId;
        execModifySQL(deleteRoleDimQ);
    }

    public void deleteRoleDimensionMembers(String memberId, String subFolderTabId, String dimTabId) throws Exception {
        String deleteRoleDimQMem = "delete from PRG_USER_SUB_FOLDER_TABLES where is_dimension='Y' and sub_folder_tab_id='" + subFolderTabId + "'"
                + "  and DIM_tab_ID='" + dimTabId + "' and member_id='" + memberId + "'";

        execModifySQL(deleteRoleDimQMem);
    }

    public PbReturnObject getRelationForDrill(String folderId) throws Exception {
        PbReturnObject allData = new PbReturnObject();
//        String dims = "select dim_id, buss_table_id, dim_name, member_id, member_name from PRG_USER_SUB_FOLDER_TABLES where sub_folder_id in(select sub_folder_id "
//                + " from PRG_USER_FOLDER_DETAIL where sub_folder_id='" + folderId + "' and sub_folder_name in('Dimensions')) order by dim_id";

        String dims = "SELECT DIM_ID,BUSS_TABLE_ID,DIM_NAME,MEMBER_ID,MEMBER_NAME FROM PRG_USER_SUB_FOLDER_TABLES WHERE SUB_FOLDER_ID IN (SELECT SUB_FOLDER_ID "
                + " FROM PRG_USER_FOLDER_DETAIL WHERE SUB_FOLDER_ID ='" + folderId + "' AND SUB_FOLDER_NAME IN ('Dimensions'))ORDER BY DIM_ID ";

//        String relQ  = " select d.mem_id,d.rel_level, d.rel_id, r.dim_id from prg_grp_dim_rel_details d, prg_grp_dim_rel r"
//                + " where d.rel_id in(select rel_id from prg_grp_dim_rel where dim_id in(select distinct dim_id from PRG_USER_SUB_FOLDER_TABLES "
//                + " where sub_folder_id  in(select sub_folder_id from PRG_USER_FOLDER_DETAIL where sub_folder_id='" + folderId + "' and"
//                + " sub_folder_name  in('Dimensions'))) and is_default='Y') and d.rel_id= r.rel_id order by rel_id ,rel_level";
        String relQ = " SELECT D.MEM_ID,D.REL_LEVEL,D.REL_ID,R.DIM_ID FROM PRG_GRP_DIM_REL_DETAILS D,PRG_GRP_DIM_REL R"
                + " WHERE D.REL_ID IN (SELECT REL_ID FROM PRG_GRP_DIM_REL WHERE DIM_ID IN (SELECT DISTINCT DIM_ID FROM  PRG_USER_SUB_FOLDER_TABLES "
                + " WHERE SUB_FOLDER_ID IN (SELECT SUB_FOLDER_ID FROM PRG_USER_FOLDER_DETAIL WHERE SUB_FOLDER_ID ='" + folderId + "' AND"
                + " SUB_FOLDER_NAME IN ('Dimensions'))) AND IS_DEFAULT='Y') and D.REL_ID = R.REL_ID ORDER BY REL_ID,REL_LEVEL";
        ////.println(" relQ " + relQ);
        // String memQ = "select * from prg_grp_role_custom_drill where sub_folder_id="+folderId+" order by custom_drill_id";
//        String memQ = "select * from prg_grp_role_custom_drill where sub_folder_id=" + folderId + " and drill_id is null order by custom_drill_id";

        String memQ = "SELECT * FROM PRG_GRP_ROLE_CUSTOM_DRILL WHERE SUB_FOLDER_ID =" + folderId + " AND DRILL_ID IS NULL ORDER BY CUSTOM_DRILL_ID";
//        ProgenLog.log(ProgenLog.FINE, this, "getRelationForDrill", "memQ---" + memQ);
        logger.info("memQ---" + memQ);
        ////.println(" dims- " + dims);
        PbReturnObject relDetails = execSelectSQL(relQ);
        PbReturnObject dimDetails = execSelectSQL(dims);
        PbReturnObject allInfo = execSelectSQL(memQ);
//        ProgenLog.log(ProgenLog.FINE, this, " getRelationForDrill", "allInfo---" + allInfo);
        logger.info("allInfo---" + allInfo);
        allData.setObject("DimDetails", dimDetails);
        allData.setObject("RelationDetails", relDetails);
        allData.setObject("allInfo", allInfo);
        return allData;
    }

    public void updateCustomDrill(String subFolderId, HashMap members) throws Exception {
        String updateCustomDrill = getResourceBundle().getString("updateCustomDrill");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" members "+members+" updateCustomDrill "+updateCustomDrill);
        Set membersSet = members.keySet();
        ArrayList uList = new ArrayList();
        String[] membersSetK = (String[]) membersSet.toArray(new String[0]);
        for (int i = 0; i < membersSetK.length; i++) {
            String memId = membersSetK[i];
            String chId = "";
            if (members.containsKey(memId)) {
                chId = members.get(memId).toString();
            }
            Object updateObj[] = new Object[3];
            updateObj[0] = chId;
            updateObj[1] = subFolderId;
            updateObj[2] = memId;
            String finupdateCustomDrill = buildQuery(updateCustomDrill, updateObj);
            uList.add(finupdateCustomDrill);
        }
        try {
            executeMultiple(uList);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    //added on 16thNov
    public void updateDimensionStatus(String dimSubFolder) throws Exception {
        String dimSubFolderArr[] = dimSubFolder.split(":");
        String dimId = "";
        ArrayList uList = new ArrayList();
        String subFolderId = "";
        dimId = dimSubFolderArr[0];
        subFolderId = dimSubFolderArr[1];
        String currentStQ = "select USE_REPORT_MEMBER from prg_user_sub_folder_tables where dim_id=" + dimId + " and sub_folder_id=" + subFolderId;
        PbReturnObject pbro = execSelectSQL(currentStQ);
        String oldSt = "";
        oldSt = pbro.getFieldValueString(0, "USE_REPORT_MEMBER");
        String newSt = "";
        if (oldSt.equalsIgnoreCase("Y")) {
            newSt = "N";
        } else {
            newSt = "Y";
        }
        String updateQ = "update prg_user_sub_folder_tables set use_report_member='" + newSt + "', use_report_dim_member='" + newSt + "' where dim_id=" + dimId + " and sub_folder_id=" + subFolderId;
        uList.add(updateQ);
        try {
            //execModifySQL(updateQ);
            executeMultiple(uList);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void updateDimensionMemberStatus(String subMbrDimTab) throws Exception {
        String subMbrDimTabArr[] = subMbrDimTab.split(":");
        String dimTabId = "";
        ArrayList uList = new ArrayList();
        String memberId = "";
        String subFolderTabId = "";
        subFolderTabId = subMbrDimTabArr[0];
        memberId = subMbrDimTabArr[1];
        dimTabId = subMbrDimTabArr[2];
        String currentStQ = "select USE_REPORT_DIM_MEMBER from prg_user_sub_folder_tables where dim_tab_id=" + dimTabId + " and sub_folder_tab_id=" + subFolderTabId + " and member_id=" + memberId;
        PbReturnObject pbro = execSelectSQL(currentStQ);
        String oldSt = "";
        oldSt = pbro.getFieldValueString(0, "USE_REPORT_DIM_MEMBER");
        String newSt = "";
        if (oldSt.equalsIgnoreCase("Y")) {
            newSt = "N";
        } else {
            newSt = "Y";
        }
        String updateQ = "update prg_user_sub_folder_tables set use_report_dim_member='" + newSt + "' where dim_tab_id=" + dimTabId + " and sub_folder_tab_id=" + subFolderTabId + " and member_id=" + memberId;
        uList.add(updateQ);
        try {
            //execModifySQL(updateQ);
            executeMultiple(uList);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public HashMap checkBussRoleForTarget(String subTargetFolder) throws Exception {
        HashMap details = new HashMap();
        String status = "false";
        String roleName = "";
        String busGroup = "";
        String checkTargetFolderForGrp = getResourceBundle().getString("checkTargetFolderForGrp");
        Object folderObj[] = new Object[1];
        folderObj[0] = subTargetFolder;
        String fincheckTargetFolderForGrp = buildQuery(checkTargetFolderForGrp, folderObj);
        PbReturnObject pbro = execSelectSQL(fincheckTargetFolderForGrp);
        if (pbro.getRowCount() > 0) {
            status = "true";
            busGroup = pbro.getFieldValueString(0, "GRP_NAME");
            roleName = pbro.getFieldValueString(0, "FOLDER_NAME");
        }
        details.put("status", status);
        details.put("busGroup", busGroup);
        details.put("roleName", roleName);
        return details;
    }

    public void makeRoleForTarget(String subTargetFolder, String busGroup) throws Exception {
        ArrayList iList = new ArrayList();
        String insertTargetRole = getResourceBundle().getString("insertTargetRole");
        String grpIdQ = "select GRP_ID from prg_user_folder where folder_id=" + subTargetFolder;
        PbReturnObject pbro = execSelectSQL(grpIdQ);
        busGroup = pbro.getFieldValueString(0, "GRP_ID");
        Object obj[] = new Object[5];
        obj[0] = subTargetFolder;
        obj[1] = "Y";
        obj[2] = "null";
        obj[3] = "null";
        obj[4] = busGroup;
        String fininsertTargetRole = buildQuery(insertTargetRole, obj);
        iList.add(fininsertTargetRole);
        try {
            executeMultiple(iList);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

    }

    public String checkRoleForDelete(String folderId) throws Exception {
        String status = "";
        String roleQ = "Select * from target_measure_folder where bus_folder_id=" + folderId;
        PbReturnObject pbro = execSelectSQL(roleQ);
        if (pbro.getRowCount() > 0) {
            status = "true";
        } else {
            status = "false";
        }
        return status;
    }
    // added by susheela over

    private String getUserFolderBucketsColumns(String subFolderTabId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = subFolderTabId;
        String sql = getResourceBundle().getString("getUserFolderBucketsColumns");

        String bucketColName;
        String bucketColDispName;
        String bucketColId;

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            ////.println("finalQuery\t" + finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            for (int i = 0; i < retObj.getRowCount(); i++) {
                bucketColId = retObj.getFieldValueString(i, colNames[0]);
                bucketColName = retObj.getFieldValueString(i, colNames[1]);
                bucketColDispName = retObj.getFieldValueString(i, colNames[2]);

                outerBuffer.append("<li class='closed' id='" + bucketColId + "'>");
                outerBuffer.append("<img src='images/treeViewImages/Dim.gif'></img>");
                outerBuffer.append("<span><font size='1px' face='verdana'>&nbsp;" + bucketColDispName + "</font></span>");
                outerBuffer.append("</li>");
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();
    }

    public void insertClobData() {
        try {
            //new ProgenConnection().getConnection();

            String str = "java.sql.SQLException: Io exception: Broken pipe"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "         at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "java.sql.SQLException: Io exception: Broken pipe"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "         at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "java.sql.SQLException: Io exception: Broken pipe"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "         at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "java.sql.SQLException: Io exception: Broken pipe"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "         at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "java.sql.SQLException: Io exception: Broken pipe"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "         at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:134)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:179)"
                    + "        at oracle.jdbc.dbaccess.DBError.throwSqlException(DBError.java:333)"
                    + "        at oracle.jdbc.driver.OracleStatement.close(OracleStatement.java:648)"
                    + "                at oracle.jdbc.driver.OraclePreparedStatement.privateClose(OraclePreparedStatement.java:485)"
                    + "        at oracle.jdbc.driver.OraclePreparedStatement.close(OraclePreparedStatement.java:393)"
                    + "        at com.rawmart.itools.Import.main(Import.java:168)";

            String Query = "insert into TEST(ID,COLUMN2) VALUES(?,?)";
            Connection conn = ProgenConnection.getInstance().getConnection();
            PreparedStatement prepStmt = conn.prepareStatement(str);

            oracle.sql.CLOB newClob = oracle.sql.CLOB.createTemporary(conn, false, oracle.sql.CLOB.MAX_CHUNK_SIZE);
            newClob.setString(1, str);
            prepStmt.setString(1, "1");
            prepStmt.setClob(2, newClob);

            prepStmt.executeUpdate();

            //Object[] Obj = new Object[2];
            //Obj[0] = '1';
            //Obj[1] = str;
            //Query = buildQuery(Query, Obj);
            //execModifySQL(Query);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void updateFactStatus(String subFolderfactTabId) throws Exception {
        String dimSubFolderArr = subFolderfactTabId;
        String dimId = "";
        ArrayList uList = new ArrayList();
        String subFolderId = "";

        String currentStQ = "select distinct USE_REPORT_MEMBER from PRG_USER_SUB_FOLDER_TABLES where SUB_FOLDER_TAB_ID=" + subFolderfactTabId.replace("chk", "");
        ////.println(" currentSt " + currentStQ);
        try {
            PbReturnObject pbro = execSelectSQL(currentStQ);
            String oldSt = "";
            oldSt = pbro.getFieldValueString(0, 0);
            String newSt = "";
            if (oldSt.equalsIgnoreCase("Y")) {
                newSt = "N";
            } else {
                newSt = "Y";
            }
            String updateQ = "update prg_user_sub_folder_tables set use_report_member='" + newSt + "', use_report_dim_member='" + newSt + "' where  sub_folder_tab_id=" + subFolderfactTabId.replace("chk", "");
            ////.println(" updateQ " + updateQ);
            uList.add(updateQ);
            //=======
//        String currentStQ = "select distinct USE_REPORT_MEMBER from PRG_USER_SUB_FOLDER_TABLES where SUB_FOLDER_TAB_ID=" + subFolderfactTabId;
//        String oldSt = "";
//        String updateQ = "";
//            PbReturnObject pbro = execSelectSQL(currentStQ);
//
//            oldSt = pbro.getFieldValueString(0, "USE_REPORT_MEMBER");
//            String newSt = "";
//            if (oldSt.equalsIgnoreCase("Y")) {
//                newSt = "N";
//            } else {
//                newSt = "Y";
//            }
//            updateQ = "update prg_user_sub_folder_tables set use_report_member='" + newSt + "', use_report_dim_member='" + newSt + "' where  sub_folder_tab_id=" + subFolderfactTabId;
//            //////.println(" updateQ " + updateQ);
            //execModifySQL(updateQ);
            executeMultiple(uList);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public static void main(String[] a) {
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String folderName = "Marketing Folder";
        String folderDesc = "Marketing Folder";
        String grpId = "41";
        //userLayerDAO.addUserFolder(folderName, folderDesc, grpIds);
        //userLayerDAO.insertClobData();
        //userLayerDAO.deleteUserFolder("103");

        //String strUserFolderList= userLayerDAO.getUserFolderList();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(strUserFolderList);
    }

    //added by uday
    public String copyUser(String selectedUserId, String newUserName, String newUserPassword) {
        PbReturnObject pbro = new PbReturnObject();
        String newUserId = "";
        String encryptedPassword = "";
        String finalQuery = "";
        try {
            String newUserIdQuery = "select PRG_AR_USERS_SEQ.nextval as new_user_id from dual";
            finalQuery = getResourceBundle().getString("copyUser");
            pbro = execSelectSQL(newUserIdQuery);
            newUserId = pbro.getFieldValueString(0, 0);

            PbEncrypter pe = new PbEncrypter();
            encryptedPassword = pe.encrypt(newUserPassword);
            Object[] Obj = new Object[4];
            Obj[0] = newUserId;
            Obj[1] = newUserName;
            Obj[2] = encryptedPassword;
            Obj[3] = selectedUserId;

            finalQuery = buildQuery(finalQuery, Obj);
            execModifySQL(finalQuery);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return newUserId;
    }

    public void copyUserPrivileges(String selectedUserId, String newUserId) {
        PbReturnObject pbro = new PbReturnObject();
        ArrayList queries = new ArrayList();
        String Query = "";
        String newSeqId = "";
        int count = 0;
        String finalQuery = "";
        try {
            String qry = getResourceBundle().getString("copyUserPrivileges");
            Object[] Obj = new Object[2];
            Obj[0] = newUserId;
            Obj[1] = selectedUserId;
            finalQuery = buildQuery(qry, Obj);
            queries.add(finalQuery);
            executeMultiple(queries);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void copyReportPrivileges(String selectedUserId, String newUserId) {
        PbReturnObject pbro = new PbReturnObject();
        ArrayList queries = new ArrayList();
        String Query = "";
        String newSeqId = "";
        int count = 0;
        String finalQuery = "";
        try {
            String qry = getResourceBundle().getString("copyReportPrivileges");
            Object[] Obj = new Object[2];

            Obj[0] = newUserId;
            Obj[1] = selectedUserId;

            finalQuery = buildQuery(qry, Obj);
            queries.add(finalQuery);
            executeMultiple(queries);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void copyTablePrivileges(String selectedUserId, String newUserId) {
        PbReturnObject pbro = new PbReturnObject();
        ArrayList queries = new ArrayList();
        String Query = "";
        String newSeqId = "";
        int count = 0;
        String finalQuery = "";
        try {
            String qry = getResourceBundle().getString("copyTablePrivileges");
            Object[] Obj = new Object[2];

            Obj[0] = newUserId;
            Obj[1] = selectedUserId;

            finalQuery = buildQuery(qry, Obj);
            queries.add(finalQuery);
            executeMultiple(queries);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void copyGraphPrivileges(String selectedUserId, String newUserId) {
        PbReturnObject pbro = new PbReturnObject();
        ArrayList queries = new ArrayList();
        String Query = "";
        String newSeqId = "";
        int count = 0;
        String finalQuery = "";
        try {
            String qry = getResourceBundle().getString("copyGraphPrivileges");
            Object[] Obj = new Object[2];

            Obj[0] = newUserId;
            Obj[1] = selectedUserId;

            finalQuery = buildQuery(qry, Obj);
            queries.add(finalQuery);
            executeMultiple(queries);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void copyUserReports(String selectedUserId, String newUserId) {
        PbReturnObject pbro = new PbReturnObject();
        ArrayList queries = new ArrayList();
        String Query = "";
        String newSeqId = "";
        int count = 0;
        String finalQuery = "";
        try {
            String qry = getResourceBundle().getString("copyUserReports");
            Object[] Obj = new Object[2];

            Obj[0] = newUserId;
            Obj[1] = selectedUserId;

            finalQuery = buildQuery(qry, Obj);
            queries.add(finalQuery);
            executeMultiple(queries);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void copyUserAssignment(String selectedUserId, String newUserId) {
        PbReturnObject pbro = new PbReturnObject();
        ArrayList queries = new ArrayList();
        String Query = "";
        String newSeqId = "";
        int count = 0;
        String finalQuery = "";
        try {
            String qry = getResourceBundle().getString("copyUserAssignment");
            Object[] Obj = new Object[2];

            Obj[0] = newUserId;
            Obj[1] = selectedUserId;

            finalQuery = buildQuery(qry, Obj);
            queries.add(finalQuery);
            executeMultiple(queries);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void copyUserFolderAssignment(String selectedUserId, String newUserId) {
        PbReturnObject pbro = new PbReturnObject();
        ArrayList queries = new ArrayList();
        String Query = "";
        String newSeqId = "";
        int count = 0;
        String finalQuery = "";
        try {
            String qry = getResourceBundle().getString("copyUserFolderAssignment");
            Object[] Obj = new Object[2];

            Obj[0] = newUserId;
            Obj[1] = selectedUserId;

            finalQuery = buildQuery(qry, Obj);
            queries.add(finalQuery);
            executeMultiple(queries);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void copyUserRoleDrill(String selectedUserId, String newUserId) {
        PbReturnObject pbro = new PbReturnObject();
        ArrayList queries = new ArrayList();
        String Query = "";
        String newSeqId = "";
        int count = 0;
        String finalQuery = "";
        try {
            String qry = getResourceBundle().getString("copyUserRoleDrill");
            Object[] Obj = new Object[2];

            Obj[0] = newUserId;
            Obj[1] = selectedUserId;

            finalQuery = buildQuery(qry, Obj);
            queries.add(finalQuery);
            executeMultiple(queries);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void copyUserRoleMemberFilter(String selectedUserId, String newUserId) {
        PbReturnObject pbro = new PbReturnObject();
        ArrayList queries = new ArrayList();
        String Query = "";
        String newSeqId = "";
        int count = 0;
        String finalQuery = "";
        try {
            String qry = getResourceBundle().getString("copyUserRoleMemberFilter");
            Object[] Obj = new Object[2];

            Obj[0] = newUserId;
            Obj[1] = selectedUserId;

            finalQuery = buildQuery(qry, Obj);
            queries.add(finalQuery);
            executeMultiple(queries);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void copyUserStickyNote(String selectedUserId, String newUserId) {
        PbReturnObject pbro = new PbReturnObject();
        ArrayList queries = new ArrayList();
        String Query = "";
        String newSeqId = "";
        int count = 0;
        String finalQuery = "";
        try {
            String qry = getResourceBundle().getString("copyUserStickyNote");
            Object[] Obj = new Object[2];

            Obj[0] = newUserId;
            Obj[1] = selectedUserId;

            finalQuery = buildQuery(qry, Obj);
            queries.add(finalQuery);
            executeMultiple(queries);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void copyUserBussGroups(String selectedUserId, String newUserId) {
        PbReturnObject pbro = new PbReturnObject();
        ArrayList queries = new ArrayList();
        String Query = "";
        String newSeqId = "";
        int count = 0;
        String finalQuery = "";
        try {
            String qry = getResourceBundle().getString("copyUserBussGroups");
            Object[] Obj = new Object[2];

            Obj[0] = newUserId;
            Obj[1] = selectedUserId;

            finalQuery = buildQuery(qry, Obj);
            queries.add(finalQuery);
            executeMultiple(queries);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    //added by susheela on 18-02-2010
    public void updateTargetMeasureForRole(String roleId, String colId, String tabId) throws Exception {
        ArrayList al = new ArrayList();
        String query = "select * from PRG_FOLDER_TARGET_MEASURE where folder_id=" + roleId + " and measure_id=" + colId;
        PbReturnObject pbro = execSelectSQL(query);
        String insertQ = "";
        String updateQ = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            insertQ = "insert into PRG_FOLDER_TARGET_MEASURE(folder_id,measure_id,table_id) values(&,&,&)";
            updateQ = "delete from PRG_FOLDER_TARGET_MEASURE where folder_id=& and measure_id=& and table_id=&";

        } else {
            insertQ = "insert into PRG_FOLDER_TARGET_MEASURE(folder_id,measure_id,folder_measure_seq,table_id) values(&,&,FOLDER_TARGET_MEASURE_SEQ.nextval,&)";
            updateQ = "delete from PRG_FOLDER_TARGET_MEASURE where folder_id=& and measure_id=& and table_id=&";

        }

        if (pbro.getRowCount() == 0) {
            Object insertObj[] = new Object[3];
            insertObj[0] = roleId;
            insertObj[1] = colId;
            insertObj[2] = tabId;
            String fininsertQ = buildQuery(insertQ, insertObj);
            al.add(fininsertQ);
        } else {
            Object insertObj[] = new Object[3];
            insertObj[0] = roleId;
            insertObj[1] = colId;
            insertObj[2] = tabId;
            String finupdateQ = buildQuery(updateQ, insertObj);
            al.add(finupdateQ);
        }
        executeMultiple(al);
    }

    public void updateSelectedFactTable(String factTabId, String chkedStatus) {
        try {
            PbDb pbdb = new PbDb();
            Object[] updtobj = new Object[2];
            updtobj[0] = chkedStatus;
            updtobj[1] = factTabId;

            String updateqry = getResourceBundle().getString("updatefolderele");
            String updatefactqry = getResourceBundle().getString("updateusrallinfodtls");
            updateqry = buildQuery(updateqry, updtobj);
            //pbdb.execModifySQL(updateqry);
            //  updatefactqry = buildQuery(updatefactqry, updtobj);
            pbdb.execModifySQL(updatefactqry);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public void editUserFolderName(String folderName, String folderDesc, String folderId) throws Exception {
        ArrayList al = new ArrayList();
        String updateRoleNameUserFolderQuery = "update prg_user_folder set folder_name='" + folderName + "', folder_desc='" + folderDesc + "' where folder_id='" + folderId + "'";
        al.add(updateRoleNameUserFolderQuery);
        String updateRolNameUserallInfoQry = "update prg_user_all_info_details set folder_name='" + folderName + "' where folder_id='" + folderId + "'";
        al.add(updateRolNameUserallInfoQry);

        executeMultiple(al);
    }

    public String getPriorMeasure(String elementId) {

        String priorMeasQry = getResourceBundle().getString("getPriorMeasure");
        Object[] records = null;
        String finalQuery = "";
        String priorCol = null;

        records = new Object[1];
        records[0] = elementId;
        try {
            finalQuery = buildQuery(priorMeasQry, records);
            PbReturnObject retObj = execSelectSQL(finalQuery);
            if (retObj.getRowCount() > 0) {
                priorCol = retObj.getFieldValueString(0, "ELEMENT_ID");
            }
        } catch (SQLException exp) {
            logger.error("Exception:", exp);
        }
        return priorCol;

    }

    public HashMap<String, String> getPriorAndChangeMeasures(String measElementId) {
        PbReturnObject retObj = new PbReturnObject();
        PbDb pbDb = new PbDb();
        Object obj[] = new Object[1];
        String priorMeasQry = getResourceBundle().getString("getPriorAndChangeMeasures");
        obj[0] = measElementId;
        String measureId = "";
        int refElementType;
        String qry = pbDb.buildQuery(priorMeasQry, obj);
        HashMap<String, String> priorChangeMeas = new HashMap<String, String>();

        try {
            retObj = pbDb.execSelectSQL(qry);
            if (retObj.getRowCount() > 0) {
                for (int j = 0; j < retObj.getRowCount(); j++) {
                    measureId = retObj.getFieldValueString(j, "ELEMENT_ID");
                    refElementType = retObj.getFieldValueInt(j, "REF_ELEMENT_TYPE");
                    if (refElementType == 2) {
                        priorChangeMeas.put("Prior", measureId);
                    }
                    if (refElementType == 3) {
                        priorChangeMeas.put("Change", measureId);
                    }
                    if (refElementType == 4) {
                        priorChangeMeas.put("Change%", measureId);
                    }

                }
            }
        } catch (SQLException ex) {
        }
        return priorChangeMeas;

    }

    public String getMeasureType(String measElementid) {
        PbReturnObject retObj = null;
        String getRefElementType = getResourceBundle().getString("getrefElementType");
        String selectedMeas = "";
        int refElementType;
        Object[] elementIdValue = new Object[1];
        elementIdValue[0] = measElementid;
        String finalQuery = "";

        try {

            finalQuery = buildQuery(getRefElementType, elementIdValue);
            retObj = execSelectSQL(finalQuery);
            refElementType = Integer.parseInt(retObj.getFieldValueString(0, 0));
            selectedMeas = "Normal";
            if (refElementType == 2) {
                selectedMeas = "Prior";
            } else if (refElementType == 3) {
                selectedMeas = "Change";
            } else if (refElementType == 4) {
                selectedMeas = "Change%";
            }
        } catch (NumberFormatException ex) {
        } catch (SQLException ex) {
        }
        return selectedMeas;

    }

    public boolean isMeasureChangePercentSelected(String measElementid) {
        String selectedMeasure = getMeasureType(measElementid);
        if (selectedMeasure.equalsIgnoreCase("Change%")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean publishFact(String subFolderTabId) {
        String getSubFolderDetails = getResourceBundle().getString("getSubFolderDetails");
        String addUserSubFolderElements = getResourceBundle().getString("addUserSubFolderElements");
        String deleteElementDetails = getResourceBundle().getString("deleteElementDetails");
        String subFolderEleDetailsQuery = getResourceBundle().getString("subFolderEleDetailsQuery");
        String deleteFromAllInfo = getResourceBundle().getString("deleteFromAllInfo");
        String insertintoAllInfo = getResourceBundle().getString("insertintoAllInfo");
        String insertintoAllInfo1 = getResourceBundle().getString("insertintoAllInfo1");
        PbReturnObject folderDetailsObject = new PbReturnObject();
        PbReturnObject infoDetailsObject = new PbReturnObject();
        Object object[] = null;
        String finalQuery = "";
        String bussTabId = "";
        String subFolderId = "";
        String FolderId = "";
        ArrayList queryList = new ArrayList();
        String[] userColNames = {"Prior_", "Change_", "Change%"};

        boolean resultbo = false;
        try {
            finalQuery = "select * from prg_user_all_info_details  WHERE SUB_FOLDER_TAB_ID =" + subFolderTabId;
            infoDetailsObject = super.execSelectSQL(finalQuery);
            if (infoDetailsObject.getRowCount() > 0) {
                FolderId = infoDetailsObject.getFieldValueString(0, "FOLDER_ID");
                bussTabId = infoDetailsObject.getFieldValueString(0, "BUSS_TABLE_ID");
                subFolderId = infoDetailsObject.getFieldValueString(0, "SUB_FOLDER_ID");

                object = new Object[4];
                object[0] = FolderId;
                object[1] = bussTabId;
                object[2] = subFolderId;
                object[3] = subFolderTabId;
                finalQuery = super.buildQuery(insertintoAllInfo1, object);

                queryList.add(finalQuery);

            } else {
                finalQuery = super.buildQuery(getSubFolderDetails, object);
                folderDetailsObject = super.execSelectSQL(finalQuery);
                if (folderDetailsObject.getRowCount() > 0) {
                    bussTabId = folderDetailsObject.getFieldValueString(0, "BUSS_TABLE_ID");
                    subFolderId = folderDetailsObject.getFieldValueString(0, "SUB_FOLDER_ID");
                    object = new Object[2];
                    object[0] = bussTabId;
                    object[1] = subFolderTabId;
                    finalQuery = super.buildQuery(deleteElementDetails, object);
                    queryList.add(finalQuery);
                    object = new Object[3];
                    object[0] = subFolderId;
                    object[1] = subFolderTabId;
                    object[2] = bussTabId;
                    finalQuery = super.buildQuery(addUserSubFolderElements, object);
                    queryList.add(finalQuery);
                }
                object = new Object[5];
                for (int i = 0; i < userColNames.length; i++) {
                    object[0] = userColNames[i];
                    object[1] = userColNames[i].replaceAll("_", " ");
                    object[2] = i + 2;
                    object[3] = bussTabId;
                    object[4] = subFolderTabId;
                    finalQuery = super.buildQuery(subFolderEleDetailsQuery, object);
                    queryList.add(finalQuery);
                }
                object = new Object[3];
                object[0] = bussTabId;
                object[1] = subFolderId;
                object[2] = subFolderTabId;
                finalQuery = super.buildQuery(deleteFromAllInfo, object);
                queryList.add(finalQuery);
                finalQuery = super.buildQuery(insertintoAllInfo, object);
                queryList.add(finalQuery);
                //
            }
            resultbo = super.executeMultiple(queryList);
        } catch (NumberFormatException ex) {
            logger.error("Exception:", ex);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        return resultbo;
    }

    public PbReturnObject getTableDetails(String elementIds) {
        String selectQuery = getResourceBundle().getString("getTableDetails");
        String finalQuery = "";
        Object obj[] = new Object[1];
        obj[0] = elementIds;
        PbReturnObject retObj = new PbReturnObject();
        finalQuery = buildQuery(selectQuery, obj);
        try {
            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return retObj;
    }

    public void assignRoleToUser(String groupId, String[] users, String rolIds, String[] userName) {
        ArrayList<String> queryList = new ArrayList<String>();
        queryList.add("delete from prg_grp_user_assignment where grp_id in (" + groupId + ")  and folder_id in (" + rolIds + ") ");
        if (users != null) {
            queryList.add("delete from prg_grp_user_folder_assignment where grp_id in (" + groupId + ") and user_folder_id in(" + rolIds + ") and  user_id not in (" + Joiner.on(",").join(users) + ")");
            executeMultiple(queryList);
            queryList = new ArrayList<String>();
            for (int i = 0; i < users.length; i++) {
                assignGroup(users[i], groupId, rolIds, userName[i]);
                addToUserFolderAssignment(users[i], groupId, rolIds);
            }

        } else {
            queryList.add("delete from prg_grp_user_folder_assignment where grp_id in (" + groupId + ")  and user_folder_id in(" + rolIds + ")");
            executeMultiple(queryList);
            queryList = new ArrayList<String>();
        }

    }

    public void assignGroup(String userID, String grpId, String roleId, String userName) {
        String addUserAssignment;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            addUserAssignment = getResourceBundle().getString("addUserAssignmentMysql");
        } else {
            addUserAssignment = getResourceBundle().getString("addUserAssignment");
        }
        Object obj[] = null;
        String finalQuery = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            obj = new Object[7];
            obj[0] = userID;
            obj[1] = grpId;
            obj[2] = "getdate()";
            obj[3] = "";
            obj[4] = "Y";
            obj[5] = userName;
            obj[6] = roleId;

            finalQuery = super.buildQuery(addUserAssignment, obj);

        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            obj = new Object[6];
            obj[0] = userID;
            obj[1] = grpId;
            obj[2] = "CURDATE()";
            obj[3] = "Y";
            obj[4] = userName;
            obj[5] = roleId;

            finalQuery = super.buildQuery(addUserAssignment, obj);
        } else {
            obj = new Object[8];
            PbReturnObject pbro = new PbReturnObject();
            try {
                pbro = super.execSelectSQL("select PRG_GRP_USER_ASSIGNMENT_SEQ.nextval from dual");
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            String seqNum = String.valueOf(pbro.getFieldValueInt(0, 0));
            obj[0] = seqNum;
            obj[1] = userID;
            obj[2] = grpId;
            obj[3] = "sysdate";
            obj[4] = "";
            obj[5] = "Y";
            obj[6] = userName;
            obj[7] = roleId;
            finalQuery = super.buildQuery(addUserAssignment, obj);
        }
        try {
            super.execModifySQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

    }

    public String SubmitSetting(String repTitle, String hideDate, String fColor, String dateF, String sytm) throws SQLException {
        //added by Mohit for Custom setting
        Connection conn = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransfer;
        int flag = 0;

        String jsonString = "false";
        ResultSet resultSet = null;
        PbReturnObject retObj = null;
        String comb = "RepTitle~" + repTitle + ";HideDate~" + hideDate + ";FontColor~" + fColor + ";DateFor~" + dateF + ";sytm~" + sytm + ";";
        String addCalQry = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
            addCalQry = "insert into PRG_GBL_SETUP_VALUES(SETUP_ID,SETUP_KEY,SETUP_CHAR_VALUE)" + " values ('&','&','&')";
        } else {
            addCalQry = "insert into PRG_GBL_SETUP_VALUES(SETUP_ID,SETUP_KEY,SETUP_CHAR_VALUE)" + " values ('&','&','&')";
        }

        Object inOb[] = new Object[3];
        inOb[0] = 3;
        inOb[1] = "CUSTOM_SETTING";
        inOb[2] = comb;

        String Query = buildQuery(addCalQry, inOb);

        if (conn != null) {
            try {
                pstmtForTransfer = ProgenConnection.getInstance().getConnection().prepareStatement("select * from  PRG_GBL_SETUP_VALUES where SETUP_ID=" + 3);
                resultSet = pstmtForTransfer.executeQuery();
                if (resultSet.next()) {
                    pstmtForTransfer = ProgenConnection.getInstance().getConnection().prepareStatement("update PRG_GBL_SETUP_VALUES set SETUP_CHAR_VALUE='" + comb + "' where SETUP_ID=" + 3);
                    pstmtForTransfer.executeUpdate();
                    jsonString = "true";
                } else {
                    pstmtForTransfer = conn.prepareStatement(Query);
                    pstmtForTransfer.executeUpdate();
                    jsonString = "true";
                }
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
//        if(flag==0){
//            return false;
//        }
//        else {

        return jsonString;
//        }
    }

    public String getBuyer(String temp, String flag, String querytype) throws SQLException {
        //added by Mohit for drl
        Connection conn = ProgenConnection.getInstance().getConnectionByConId("66");
        PbReturnObject pbro = new PbReturnObject();
//        int flag = 0;
        PbDb pbdb = new PbDb();
        String jsonString = "";
        ResultSet resultSet = null;
//        PbReturnObject retObj = null;
        StringBuilder sb = new StringBuilder();
//        String comb="RepTitle~"+repTitle+";HideDate~"+hideDate+";FontColor~"+fColor+";DateFor~"+dateF+";sytm~"+sytm+";";
        String query = "";
        if (querytype.equalsIgnoreCase("select") && !(flag.contains("getbuyer"))) {
            if (temp.equalsIgnoreCase("bg")) {
                query = "select distinct buyer_group from  DRL_BUYER_MASTER_DIM ORDER BY buyer_group ASC ";
            } else if (temp.equalsIgnoreCase("bn")) {
                query = "select distinct name_of_buyer from  DRL_BUYER_MASTER_DIM ORDER BY name_of_buyer ASC ";
            } else if (flag.equalsIgnoreCase("bbg")) {
                query = "select distinct name_of_buyer from  DRL_BUYER_MASTER_DIM where buyer_group='" + temp + "' ORDER BY name_of_buyer ASC ";
            } else if (flag.equalsIgnoreCase("bbn")) {
                query = "select distinct buyer_group from  DRL_BUYER_MASTER_DIM where name_of_buyer='" + temp + "' ORDER BY buyer_group ASC ";
            }
            if (conn != null) {
                try {

                    pbro = pbdb.execSelectSQL(query, conn);

                    sb.append("<option> --SELECT-- </option>");
                    for (int i = 0; i < pbro.getRowCount(); i++) {
                        sb.append("<option>");
                        sb.append(pbro.getFieldValueString(i, 0));
                        sb.append("</option>");
                    }
//            resultSet = pstmtForTransfer.executeQuery();

                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                } catch (Exception ex) {
                }
            }
//

            jsonString = sb.toString();
//        return sb.toString();
        } else {
            if (flag.equalsIgnoreCase("getbuyerbbn")) {
                query = "select buyer_id,bu,buyer_group,name_of_buyer,team,DATE_FORMAT(start_date, '%d/%m/%Y') AS formatted_date1,DATE_FORMAT(end_date, '%d/%m/%Y') AS formatted_date2 from  DRL_BUYER_MASTER_DIM where name_of_buyer='" + temp + "'";
            } else if (flag.equalsIgnoreCase("getbuyerbbg")) {
                query = "select buyer_id,bu,buyer_group,name_of_buyer,team,DATE_FORMAT(start_date, '%d/%m/%Y') AS formatted_date1,DATE_FORMAT(end_date, '%d/%m/%Y') AS formatted_date2 from  DRL_BUYER_MASTER_DIM where buyer_group='" + temp + "'";
            } else if (flag.equalsIgnoreCase("getbuyerboth")) {

                String s[] = temp.split("MOHIT");
//
                query = "select buyer_id,bu,buyer_group,name_of_buyer,team,DATE_FORMAT(start_date, '%d/%m/%Y') AS formatted_date1,DATE_FORMAT(end_date, '%d/%m/%Y') AS formatted_date2 from  DRL_BUYER_MASTER_DIM where buyer_group='" + s[0] + "' and name_of_buyer='" + s[1] + "'";
            }
            if (conn != null) {
                try {

                    pbro = pbdb.execSelectSQL(query, conn);

//        sb.append("<option> --SELECT-- </option>");
                    for (int i = 0; i < pbro.getRowCount(); i++) {
                        int temp1 = pbro.getFieldValueInt(i, 0);
//           String Date1[]=(pbro.getFieldValueDateString(i, 5)).split("-");
//           String Date2[]=(pbro.getFieldValueDateString(i, 5)).split("-");
//           String startdate=date[2].
                        sb.append("<tr id='").append(temp1).append("' ><td style=' width: 1.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(i + 1).append("</td>");
                        sb.append("<td style='width: 5.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(temp1).append("</td>");
                        sb.append("<td style='width: 4.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='bUnit").append(temp1).append("' type='text' value='").append(pbro.getFieldValueString(i, 1)).append("'> </td>");
                        sb.append("<td style='width: 4.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='bGroup").append(temp1).append("' type='text' value='").append(pbro.getFieldValueString(i, 2)).append("'> </td>");
                        sb.append("<td style='width: 12.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='bName").append(temp1).append("' type='text' value='").append(pbro.getFieldValueString(i, 3)).append("'> </td>");
                        sb.append("<td style='width: 6.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='team").append(temp1).append("' type='text' value='").append(pbro.getFieldValueString(i, 4)).append("'> </td>");
                        sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='sddatepicker").append(temp1).append("' type='text' value='").append(pbro.getFieldValueString(i, 5)).append("' onclick=''> </td>");
                        sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='eddatepicker").append(temp1).append("' type='text' value='").append(pbro.getFieldValueString(i, 6)).append("' onclick=''> </td>");
                        sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='Update").append(temp1).append("' type='button' onclick='updateBuyer(").append(temp1).append(")' value='Update'> </td>");
                        sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='Delet").append(temp1).append("' type='button' onclick='DeleteBuyer(").append(temp1).append(")' value='Delete'> </td></tr>");

                    }
//            resultSet = pstmtForTransfer.executeQuery();

                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                } catch (Exception ex) {
                    logger.error("Exception:", ex);
                }
            }

            jsonString = sb.toString();
        }
//
        return jsonString;
    }

    //added by sruthi
    public String deleteBuyer(String Deleteid, String querytype) throws SQLException {
        Connection conn = ProgenConnection.getInstance().getConnectionByConId("65");
//        PbReturnObject pbro = new PbReturnObject();
        PbDb pbdb = new PbDb();
//        String jsonString = "";
        String query = "";
//        StringBuilder sb = new StringBuilder();
//        String jsonDelete = null;
        int k = 0;
        if (querytype.equalsIgnoreCase("Delete")) {
            query = "delete from  DRL_BUYER_MASTER_DIM where buyer_id=" + Deleteid;
        }
        if (conn != null) {
            try {
                k = pbdb.execUpdateSQL(query, conn);
            } catch (Exception ex) {
            }
//            jsonDelete = "" + k;
//            jsonDelete = String.valueOf( k);
        }
//        return jsonDelete;
        return String.valueOf(k);
    }
//ended by sruthi

    public String setBuyer(String bName, String bGroup, String bUnit, String team, String sddatepicker, String eddatepicker, String bid, String querytype) throws SQLException {
        //added by Mohit for Custom setting
        Connection conn = ProgenConnection.getInstance().getConnectionByConId("66");
        PbReturnObject pbro = new PbReturnObject();
        int j = 0;

        String jsonString = "";
//        ResultSet resultSet = null;
        PbDb pbdb = new PbDb();
//        PbReturnObject retObj = null;
        StringBuilder sb = new StringBuilder();
//        String comb="RepTitle~"+repTitle+";HideDate~"+hideDate+";FontColor~"+fColor+";DateFor~"+datesubmitF+";sytm~"+sytm+";";
        String query = "";

        String[] formysql = sddatepicker.split("/");
        String[] formysql1 = eddatepicker.split("/");

        String date1 = "date_format('" + formysql[2].trim() + "/" + formysql[1].trim() + "/" + formysql[0].trim() + "','%Y-%m-%d')";
        String date2 = "date_format('" + formysql1[2].trim() + "/" + formysql1[1].trim() + "/" + formysql1[0].trim() + "','%Y-%m-%d')";
        if (querytype.equalsIgnoreCase("insert")) {
            query = "insert into  DRL_BUYER_MASTER_DIM(BU,BUYER_GROUP,NAME_OF_BUYER,TEAM,start_date,end_date)" + " values ('&','&','&','&',&,&)";
            Object inOb[] = new Object[6];
            inOb[0] = bUnit;
            inOb[1] = bGroup;
            inOb[2] = bName;
            inOb[3] = team;
            inOb[4] = date1;
            inOb[5] = date2;
            query = buildQuery(query, inOb);
            try {

                j = pbdb.execUpdateSQL(query, conn);

            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            } catch (Exception ex) {
            }
            if (j == 1) {
//    query="select buyer_id,bu,buyer_group,name_of_buyer,team,DATE_FORMAT(start_date, '%d/%m/%Y') AS formatted_date1,DATE_FORMAT(end_date, '%d/%m/%Y') AS formatted_date2 from  DRL_BUYER_MASTER_DIM where bu='"+bUnit+"' and buyer_group='"+bGroup+"' and name_of_buyer='"+bName+"' and team='"+team+"' and start_date="+date1+" and end_date="+date2+" ";

                query = "select buyer_id,bu,buyer_group,name_of_buyer,team,DATE_FORMAT(start_date, '%d/%m/%Y') AS formatted_date1,DATE_FORMAT(end_date, '%d/%m/%Y') AS formatted_date2 from  DRL_BUYER_MASTER_DIM where buyer_id=(SELECT MAX(buyer_id)FROM drl_buyer_master_dim);";

                conn = ProgenConnection.getInstance().getConnectionByConId("66");

                if (conn != null) {
                    try {

                        pbro = pbdb.execSelectSQL(query, conn);

//        sb.append("<option> --SELECT-- </option>");
                        for (int i = 0; i < pbro.getRowCount(); i++) {
                            int temp1 = pbro.getFieldValueInt(i, 0);
//           String Date1[]=(pbro.getFieldValueDateString(i, 5)).split("-");
//           String Date2[]=(pbro.getFieldValueDateString(i, 5)).split("-");
//           String startdate=date[2].
                            sb.append("<tr id='").append(temp1).append("' ><td style=' width: 1.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(i + 1).append("</td>");
                            sb.append("<td style='width: 5.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(temp1).append("</td>");
                            sb.append("<td style='width: 4.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='bUnit").append(temp1).append("' type='text' value='").append(pbro.getFieldValueString(i, 1)).append("'> </td>");
                            sb.append("<td style='width: 4.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='bGroup").append(temp1).append("' type='text' value='").append(pbro.getFieldValueString(i, 2)).append("'> </td>");
                            sb.append("<td style='width: 12.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='bName").append(temp1).append("' type='text' value='").append(pbro.getFieldValueString(i, 3)).append("'> </td>");
                            sb.append("<td style='width: 6.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='team").append(temp1).append("' type='text' value='").append(pbro.getFieldValueString(i, 4)).append("'> </td>");
                            sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='sddatepicker").append(temp1).append("' type='text' value='").append(pbro.getFieldValueString(i, 5)).append("' onclick=''> </td>");
                            sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='eddatepicker").append(temp1).append("' type='text' value='").append(pbro.getFieldValueString(i, 6)).append("' onclick=''> </td>");
                            sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='Update").append(temp1).append("' type='button' onclick='updateBuyer(").append(temp1).append(")' value='Update'> </td>");
                            sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'><input id='Delet").append(temp1).append("' type='button' onclick='DeleteBuyer(").append(temp1).append(")' value='Delete'> </td></tr>");
                        }
//            resultSet = pstmtForTransfer.executeQuery();

                    } catch (SQLException ex) {
                        logger.error("Exception:", ex);
                    } catch (Exception ex) {
                    }
                }

                jsonString = sb.toString();
            } else {
//                 jsonString = String.valueOf( j);
                jsonString = String.valueOf(j);
            }

        } else if (querytype.equalsIgnoreCase("update")) {
            query = "update DRL_BUYER_MASTER_DIM set BU='" + bUnit + "' ,BUYER_GROUP='" + bGroup + "' ,NAME_OF_BUYER='" + bName + "' ,TEAM='" + team + "',start_date=" + date1 + ",end_date=" + date2 + " where buyer_id=" + bid;
//
            try {

                j = pbdb.execUpdateSQL(query, conn);
                jsonString = String.valueOf(j);
//            resultSet = pstmtForTransfer.executeQuery();

            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            } catch (Exception ex) {
            }

        }
//PbReturnObject pbro = new PbReturnObject();

        return jsonString;
    }

    public boolean isAssignedGroup(String user, String group, String roleID) {

        PbReturnObject pbReturnObject = new PbReturnObject();
        String isAssignedGroup = getResourceBundle().getString("isAssignedGroup");
        Object[] objects = new Object[3];
        objects[0] = group;
        objects[1] = user;
        objects[2] = roleID;
        try {
            pbReturnObject = super.execSelectSQL(super.buildQuery(isAssignedGroup, objects));
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (pbReturnObject.getRowCount() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public void addToUserFolderAssignment(String userID, String groupID, String roleID) {
        ArrayList<String> queryList = new ArrayList<String>();
        String finalQuery = "";
        String addUserFolderAssignment = getResourceBundle().getString("addUserFolderAssignment");
        String delfldr = getResourceBundle().getString("deleteFolder");
        Object delfold[] = new Object[2];
        delfold[0] = roleID;
        delfold[1] = userID;
//        String delfolders = super.buildQuery(delfldr, delfold);
//String deletQ = "delete from prg_ar_user_reports where USER_ID =" + userID;

        // queryList.add(delfolders);
        if ((ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER))) {
            Object obj[] = new Object[4];
            obj[0] = roleID;
            obj[1] = userID;
            obj[2] = groupID;
            obj[3] = "getdate()";
            finalQuery = super.buildQuery(addUserFolderAssignment, obj);
            queryList.add(finalQuery);
        } else if ((ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL))) {
            Object obj[] = new Object[4];
            obj[0] = roleID;
            obj[1] = userID;
            obj[2] = groupID;
            obj[3] = "CURDATE()";
            finalQuery = super.buildQuery(addUserFolderAssignment, obj);
            queryList.add(finalQuery);
        } else {
            Object obj[] = new Object[6];
            PbReturnObject pbro = new PbReturnObject();
            try {
                pbro = super.execSelectSQL("select PRG_GRP_USER_FOLDER_ASSI_SEQ.nextval from dual");
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            String seqNum = String.valueOf(pbro.getFieldValueInt(0, 0));
            obj[0] = seqNum;
            obj[1] = roleID;
            obj[2] = userID;
            obj[3] = groupID;
            obj[4] = "sysdate";
            obj[5] = "";
            finalQuery = super.buildQuery(addUserFolderAssignment, obj);
            queryList.add(finalQuery);

        }
        String getrepssqlqry = "";
        PbReturnObject pbr = null;
        if (isCompanyValid) {
            getrepssqlqry = getResourceBundle().getString("getRprt");
            Object user[] = new Object[1];
            user[0] = userID;
            String getrepssql = super.buildQuery(getrepssqlqry, user);
            try {
                pbr = super.execSelectSQL(getrepssql);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        } else {
            String sqlqry = getResourceBundle().getString("getRpid");
            Object objid[] = new Object[1];
            objid[0] = roleID;
            String sql = super.buildQuery(sqlqry, objid);
            try {
                pbr = super.execSelectSQL(sql);

            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        if (pbr != null) {
            if (pbr.getRowCount() > 0) {
                String repIdsdel = "";
                String repIdsdel1 = "";
                String repIdsdel2 = "";
                for (int n = 0; n < pbr.getRowCount(); n++) {
                    repIdsdel += "," + String.valueOf(pbr.getFieldValueInt(n, 0));
                }
                String repIdsdelSplit[] = repIdsdel.split(",");
                int length = repIdsdelSplit.length;
                if (length > 1000) {
                    int index = 0;
                    for (int i = 0; i < 500; i++) {
                        index = repIdsdel.indexOf(",", index + 1);
                    }
                    repIdsdel1 = repIdsdel.substring(0, index);
                    repIdsdel2 = repIdsdel.substring(index + 1);

//                }
                    if (!repIdsdel1.equalsIgnoreCase("") || !repIdsdel2.equalsIgnoreCase("")) {
                        repIdsdel1 = repIdsdel1.substring(1);
                        repIdsdel2 = repIdsdel1.substring(1);
                        String deluserreps1 = "DELETE  FROM prg_ar_user_reports where USER_ID=" + userID + " and Report_id in(" + repIdsdel1 + ")";
                        queryList.add(deluserreps1);
                        String deluserreps2 = "DELETE  FROM prg_ar_user_reports where USER_ID=" + userID + " and Report_id in(" + repIdsdel2 + ")";
                        queryList.add(deluserreps2);
                    }
                } else {
                    int size = repIdsdel.length();
                    String repIdsc = repIdsdel.substring(1, size);
                    String deluserreps1 = "DELETE  FROM prg_ar_user_reports where USER_ID=" + userID + " and Report_id in(" + repIdsc + ")";
                    queryList.add(deluserreps1);
                }
                if ((ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER))) {
                    // String insertQ = "insert into prg_ar_user_reports(USER_ID,REPORT_ID,PUR_FAV_REPORT) values(&,&,'N')";
                    String insertQ = getResourceBundle().getString("insrtreptqry");
                    for (int n = 0; n < pbr.getRowCount(); n++) {
                        String rep = String.valueOf(pbr.getFieldValueInt(n, 0));
                        Object inObj[] = new Object[2];
                        inObj[0] = userID;
                        inObj[1] = rep;
                        String insertQfin = super.buildQuery(insertQ, inObj);
                        queryList.add(insertQfin);
                    }
                } else {
                    String insertQ = getResourceBundle().getString("insrtreptqry");
                    for (int n = 0; n < pbr.getRowCount(); n++) {
                        String rep = String.valueOf(pbr.getFieldValueInt(n, 0));
                        Object inObj[] = new Object[2];
                        inObj[0] = userID;
                        inObj[1] = rep;
                        String insertQfin = super.buildQuery(insertQ, inObj);
                        queryList.add(insertQfin);
                    }
                }

            }
        }
        if ((ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL))) {
            super.executeMultiple(queryList);
        } else {
            super.execMultiple(queryList);
        }

    }

    public void assignFolderToUsers(String[] grpIds, String[] roleIds, String userId) {
        if (grpIds != null) {
            try {
                super.execModifySQL("delete from prg_ar_user_reports where USER_ID =" + userId);
                super.execModifySQL("delete from PRG_GRP_USER_FOLDER_ASSIGNMENT where USER_ID=" + userId);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            for (int i = 0; i < grpIds.length; i++) {

                if (isAssignedGroup(userId, grpIds[i], roleIds[i])) {
                    addToUserFolderAssignment(userId, grpIds[i], roleIds[i]);
                } else {
                    try {
                        PbReturnObject returnObject = execSelectSQL("select PU_LOGIN_ID from PRG_AR_USERS where PU_ID=" + userId);
                        assignGroup(userId, grpIds[i], roleIds[i], returnObject.getFieldValueString(0, 0));
                        addToUserFolderAssignment(userId, grpIds[i], roleIds[i]);
                    } catch (SQLException ex) {
                        logger.error("Exception:", ex);
                    }
                }
            }
        } else {
            ArrayList<String> queryList = new ArrayList<String>();
            queryList.add("delete from prg_grp_user_assignment where USER_ID=" + userId);
            queryList.add("delete from PRG_GRP_USER_FOLDER_ASSIGNMENT where USER_ID =" + userId);
            queryList.add("delete from prg_ar_user_reports where USER_ID =" + userId);
            super.executeMultiple(queryList);
        }
    }

    public PbReturnObject getElementIDDetails(String elementId) {
        PbReturnObject pbReturnObject = new PbReturnObject();
        try {
            pbReturnObject = super.execSelectSQL("SELECT GRP_ID, FOLDER_ID, FOLDER_NAME, SUB_FOLDER_ID, SUB_FOLDER_NAME, SUB_FOLDER_TYPE, SUB_FOLDER_TAB_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, DISP_NAME, QRY_DIM_ID, DIM_ID, DIM_TAB_ID, DIM_NAME, ELEMENT_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME, USER_COL_NAME, USER_COL_DESC, USER_COL_TYPE, REF_ELEMENT_ID, REF_ELEMENT_TYPE, MEMBER_ID, MEMBER_NAME, DENOM_QUERY, BUSS_TABLE_NAME, CONNECTION_ID, AGGREGATION_TYPE, ACTUAL_COL_FORMULA, USE_REPORT_FLAG,REFFERED_ELEMENTS,DISPLAY_FORMULA,TABLE_DISP_NAME,TABLE_TOOLTIP_NAME FROM PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID=" + elementId);

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return pbReturnObject;
    }

    public String toXML(String[] meemberValues) {
        StringBuilder builder = new StringBuilder();
        //modified by Nazneen
        builder.append("<memberDetails>");
        for (String member : meemberValues) {
            if (!member.equalsIgnoreCase("")) {
                builder.append("<member>").append(member.replace("~", ","));
                builder.append("</member>");
            }
        }
        builder.append("</memberDetails>");

//        for (String member : meemberValues) {
//            if (!member.equalsIgnoreCase("")) {
//                builder.append("'").append(member.replace("'''", ","));
//                builder.append("',");
//            }
//        }
        return builder.toString().trim();
    }
    //added by Nazneen

    public String toXMLSec(String[] meemberValues) {
        StringBuilder builder = new StringBuilder();
        //commented by Nazneen
//        builder.append("<memberDetails>");
//        for (String member : meemberValues) {
//            if (!member.equalsIgnoreCase("")) {
//                builder.append("<member>").append(member.replace("~", ","));
//                builder.append("</member>");
//            }
//        }
//        builder.append("</memberDetails>");
        for (String member : meemberValues) {
            if (!member.equalsIgnoreCase("")) {
                builder.append("'").append(member.replace("'''", ","));
                builder.append("',");
            }
        }
        return builder.toString().trim();
    }

    public ArrayList<String> fromXML(String dataValues) {
        SAXBuilder builder = new SAXBuilder();
        Document memberValDocument;
        Element root = null;
        ArrayList<String> memberValues = new ArrayList<String>();
        try {
            if (!dataValues.equalsIgnoreCase("")) {
                memberValDocument = builder.build(new ByteArrayInputStream(dataValues.getBytes()));
                root = memberValDocument.getRootElement();
                List row = root.getChildren("member");
                for (int i = 0; i < row.size(); i++) {
//                    Element member = (Element) row.get(i);
//                    List membersList = member.getChildren("measure");
                    Element memberValue = (Element) row.get(i);
                    memberValues.add(memberValue.getText());
                }
            }
        } catch (JDOMException ex) {
            logger.error("Exception:", ex);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return memberValues;
    }

    public String getFolderID(int subFolderId) {
        String query = "SELECT FOLDER_ID FROM PRG_USER_FOLDER_DETAIL where SUB_FOLDER_ID=" + subFolderId;
        PbReturnObject pbReturnObject = new PbReturnObject();
        String folderId = "0";
        try {
            pbReturnObject = super.execSelectSQL(query);
            folderId = pbReturnObject.getFieldValueString(0, 0);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        return folderId;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public boolean isIsMemberUseInOtherLevel() {
        return isMemberUseInOtherLevel;
    }

    public boolean assignRoleToUser(String[] users, String reportIDs, String refUserId) {
        String query = "";
        ArrayList<String> queryList = new ArrayList<String>();
        queryList.add("delete from PRG_AR_DATA_SNAPSHOTS where CREATED_BY in (" + Joiner.on(",").join(users) + ") and report_id in(" + reportIDs + ")");
        for (String user : users) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                query = "insert into PRG_AR_DATA_SNAPSHOTS (DATA_SNAPSHOT_NAME,REPORT_ID,LAST_REFRESHED,REFRESH_INTERVAL,DATA_SNAPSHOT,DATA_HTML_VIEW,CREATED_BY,CREATE_DATE,UPDATED_BY,UPDATED_DATE )"
                        + " select DATA_SNAPSHOT_NAME,REPORT_ID,LAST_REFRESHED,REFRESH_INTERVAL,DATA_SNAPSHOT,DATA_HTML_VIEW," + user + ",CREATE_DATE,UPDATED_BY,UPDATED_DATE from PRG_AR_DATA_SNAPSHOTS where created_by=" + refUserId + " and report_id in (" + reportIDs + ")";

            } else {
                query = "insert into PRG_AR_DATA_SNAPSHOTS (DATA_SNAPSHOT_ID,DATA_SNAPSHOT_NAME,REPORT_ID,LAST_REFRESHED,REFRESH_INTERVAL,DATA_SNAPSHOT,DATA_HTML_VIEW,CREATED_BY,CREATE_DATE,UPDATED_BY,UPDATED_DATE )"
                        + " select PRG_AR_DATA_SNAPSHOTS_S.NEXTVAL,DATA_SNAPSHOT_NAME,REPORT_ID,LAST_REFRESHED,REFRESH_INTERVAL,DATA_SNAPSHOT,DATA_HTML_VIEW," + user + ",CREATE_DATE,UPDATED_BY,UPDATED_DATE from PRG_AR_DATA_SNAPSHOTS where created_by=" + refUserId + " and report_id in (" + reportIDs + ")";

            }

            queryList.add(query);
        }

        return super.executeMultiple(queryList);
    }

    public String getAllConnectionForModifyMeasures() {
        String Query = " SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(Query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option>--SELECT--</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueInt(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 1));
            sb.append("</option>");
        }
        return sb.toString();
    }

    public String getAllMeasures(String userId, String connectionID, String foldersSelected, String tablesSelected) {
        String query = "";
        if (tablesSelected.equals("0000")) {
            query = "SELECT DISTINCT USER_COL_DESC, ELEMENT_ID,AGGREGATION_TYPE,ACTUAL_COL_FORMULA,BUSS_TABLE_NAME,BUSS_COL_NAME FROM PRG_USER_ALL_INFO_DETAILS   WHERE  CONNECTION_ID  =" + connectionID + " AND FOLDER_ID = " + foldersSelected + " AND BUSS_TABLE_NAME !='PROGEN_TIME' AND IS_FACT ='Y' and USE_REPORT_FLAG='Y' ORDER BY BUSS_COL_NAME,BUSS_TABLE_NAME asc";

        } else {
            query = "SELECT DISTINCT USER_COL_DESC, ELEMENT_ID,AGGREGATION_TYPE,ACTUAL_COL_FORMULA,BUSS_TABLE_NAME,BUSS_COL_NAME FROM PRG_USER_ALL_INFO_DETAILS   WHERE  CONNECTION_ID  =" + connectionID + " AND FOLDER_ID = " + foldersSelected + " AND BUSS_TABLE_ID=" + tablesSelected + " AND BUSS_TABLE_NAME !='PROGEN_TIME' AND IS_FACT ='Y' and USE_REPORT_FLAG='Y' and ref_element_type =1 ORDER BY BUSS_COL_NAME,BUSS_TABLE_NAME asc";

        }
        PbReturnObject pbro = new PbReturnObject();
        try {
            //
            pbro = new PbDb().execSelectSQL(query);
            Object[] memberNames = new Object[pbro.getRowCount()];
            Object[] elementIds = new Object[pbro.getRowCount()];
            Object[] aggregationTypes = new Object[pbro.getRowCount()];
            Object[] actualColFormulas = new Object[pbro.getRowCount()];
            Object[] bussTableNames = new Object[pbro.getRowCount()];
            Object[] bussColNames = new Object[pbro.getRowCount()];
//        Object[] grpNames = new Object[pbro.getRowCount()];
//        Object[] grpIds = new Object[pbro.getRowCount()];
            MeasureEditHelper med = new MeasureEditHelper();
            for (int i = 0; i < pbro.getRowCount(); i++) {
                memberNames[i] = pbro.getFieldValueString(i, "USER_COL_DESC");
                elementIds[i] = pbro.getFieldValue(i, 1);
                aggregationTypes[i] = pbro.getFieldValue(i, 2);
                actualColFormulas[i] = pbro.getFieldValue(i, 3);
                bussTableNames[i] = pbro.getFieldValue(i, 4);
                bussColNames[i] = pbro.getFieldValue(i, 5);
//         grpNames[i]=pbro.getFieldValue(i, 6);
//         grpIds[i]=pbro.getFieldValue(i, 7);
            }
            med.setMemberName(memberNames);
            med.setElementId(elementIds);
            med.setAggregationType(aggregationTypes);
            med.setActualColFormula(actualColFormulas);
            med.setBussTableName(bussTableNames);
            med.setBussColName(bussColNames);
//        med.setGrpName(grpNames);
//        med.setGrpId(grpIds);
            Gson gson = new Gson();
            gson.toJson(med);
            return gson.toJson(med);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public String getFolderDetails(String userId, String connectionID) {
        String Query = "SELECT FOLDER_ID,FOLDER_NAME FROM PRG_USER_FOLDER WHERE GRP_ID IN (SELECT GRP_ID FROM PRG_GRP_MASTER WHERE CONNECTION_ID =" + connectionID + ") ";
        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(Query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option>--SELECT--</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueInt(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 1));
            sb.append("</option>");
        }
        return sb.toString();
    }
    //written by sunita

    public String getTabDetails(String userId, String connectionID, String folderID) {
        String Query = "select distinct table_disp_name, buss_table_id FROM PRG_USER_ALL_INFO_DETAILS "
                + " where connection_id=" + connectionID + " and folder_id=" + folderID + " and SUB_FOLDER_NAME='Facts' order by table_disp_name";

        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(Query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='0000'>All</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueInt(i, 1) + "'>");
            sb.append(pbro.getFieldValueString(i, 0));
            sb.append("</option>");
        }
        return sb.toString();
    }

    public boolean deleteRow(String refID) {
        String Query = "update  PRG_USER_ALL_INFO_DETAILS set USE_REPORT_FLAG='N'  where REF_ELEMENT_ID=" + refID;
        ArrayList delList = new ArrayList();
        delList.add(Query);
        return super.executeMultiple(delList);

    }

    //started by Nazneen
    public String getDimDetails(String userId, String connectionID, String folderID) {
        String Query = "select distinct b.dim_id,b.dim_name FROM PRG_USER_ALL_INFO_DETAILS b INNER JOIN PRG_USER_ALL_ADIM_KEY_VAL_ELE p "
                + "ON (b.ELEMENT_ID = p.KEY_ELEMENT_ID) where connection_id=" + connectionID + " and folder_id=" + folderID + " order by b.dim_name";
        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(Query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='0000'>All</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueInt(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 1));
            sb.append("</option>");
        }
        return sb.toString();
    }

    public boolean updateDependentMeasure(ArrayListMultimap changeInDependentMultimap) {
        Set<String> elementKeys = changeInDependentMultimap.keySet();
        Object[] objects = new Object[3];
        Object[] objinUserInfo = new Object[3];
        Object[] objinUserInfoDetails = new Object[3];
        Object[] objinSubFold = new Object[3];
        String updateQuery = "UPDATE PRG_USER_ALL_INFO_DETAILS SET USER_COL_DESC = REPLACE( USER_COL_DESC,'&','&')  WHERE REF_ELEMENT_ID= &";
        String updateDepandentQuery = "UPDATE PRG_USER_ALL_INFO_DETAILS SET AGGREGATION_TYPE='&' ,ACTUAL_COL_FORMULA='&' WHERE ELEMENT_ID= &";
        //String updateDepndQ = "UPDATE PRG_USER_ALL_INFO_DETAILS SET USER_COL_DESC ='&',AGGREGATION_TYPE='&' WHERE ELEMENT_ID=&";
        String updateInSubFolderQuery = "UPDATE PRG_USER_SUB_FOLDER_ELEMENTS SET USER_COL_DESC = REPLACE( USER_COL_DESC,'&','&') WHERE REF_ELEMENT_ID=&";
        ArrayList queryList = new ArrayList();
        for (String key : elementKeys) {
            List tempList = (changeInDependentMultimap.get(key));
            //to get the values from updateQuery
            if ((tempList.get(0)).equals("--")) {
                objects[0] = null;
            } else {
                objects[0] = tempList.get(0);
            }
            if ((tempList.get(1)).equals("--")) {
                objects[1] = null;
            } else {
                objects[1] = tempList.get(1);
            }
//            if ((tempList.get(2)).equals("--")) {
//                objects[2] = null;
//            } else {
//                objects[2] = tempList.get(2);
//            }
//            if ((tempList.get(3)).equals("--")) {
//                objects[3] = null;
//            } else {
//                objects[3] = tempList.get(3);
//            }
            objects[2] = key;
            //to get the values from updateDepandentQuery
            if ((tempList.get(2)).equals("--")) {
                objinUserInfo[0] = null;
            } else {
                objinUserInfo[0] = tempList.get(2);
            }
            if ((tempList.get(3)).equals("--")) {
                objinUserInfo[1] = null;
            } else {
//                modified by Nazneen on 30Jan14
//                objinUserInfo[1] = tempList.get(3);
                objinUserInfo[1] = tempList.get(3).toString().replace("'", "''");
            }
//            if ((tempList.get(2)).equals("--")) {
//                objinUserInfo[2] = null;
//            } else {
//                objinUserInfo[2] = tempList.get(2);
//            }
//            if ((tempList.get(3)).equals("--")) {
//                objinUserInfo[3] = null;
//            } else {
//                objinUserInfo[3] = tempList.get(3);
//            }
            objinUserInfo[2] = key;
            //to get the values from updateDepndQ
            if ((tempList.get(0)).equals("--")) {
                objinUserInfoDetails[0] = null;
            } else {
                objinUserInfoDetails[0] = tempList.get(1);
            }
            if ((tempList.get(2)).equals("--")) {
                objinUserInfoDetails[1] = null;
            } else {
                objinUserInfoDetails[1] = tempList.get(2);
            }
//            if ((tempList.get(2)).equals("--")) {
//                objinUserInfoDetails[2] = null;
//            } else {
//                objinUserInfoDetails[2] = tempList.get(3);
//            }
            objinUserInfoDetails[2] = key;
            //to get values from  updateInSubFolderQuery
            if ((tempList.get(0)).equals("--")) {
                objinSubFold[0] = null;
            } else {
                objinSubFold[0] = tempList.get(0);
            }
            if ((tempList.get(1)).equals("--")) {
                objinSubFold[1] = null;
            } else {
                objinSubFold[1] = tempList.get(1);
            }
            objinSubFold[2] = key;
//            
//             
//            
            queryList.add(super.buildQuery(updateQuery, objects));
            queryList.add(super.buildQuery(updateDepandentQuery, objinUserInfo));
            // queryList.add(super.buildQuery(updateDepndQ, objinUserInfoDetails));
            queryList.add(super.buildQuery(updateInSubFolderQuery, objinSubFold));
        }
        return super.executeMultiple(queryList);
    }

    public boolean updateInGroup(ArrayListMultimap changeInGroupMultimap) {
        Set<String> elementKeys = changeInGroupMultimap.keySet();
        Object[] objects = new Object[3];
        Object[] objinUserInfo = new Object[4];
        String updateQuery = "UPDATE PRG_GRP_BUSS_TABLE_DETAILS SET COLUMN_DISPLAY_DESC = REPLACE( COLUMN_DISPLAY_DESC,'&','&') WHERE BUSS_COLUMN_ID = (SELECT MIN( BUSS_COL_ID)BUSS_COL_ID FROM PRG_USER_ALL_INFO_DETAILS WHERE ELEMENT_ID =& )";
        //String updateInUserAllInfoQuery = "UPDATE PRG_USER_ALL_INFO_DETAILS SET USER_COL_DESC = REPLACE( USER_COL_DESC,'&','&'),AGGREGATION_TYPE='&' WHERE ELEMENT_ID =&";
        ArrayList queryList = new ArrayList();
        for (String key : elementKeys) {
            //
            List tempList = (changeInGroupMultimap.get(key));
            //to get the value from updateQuery
            if ((tempList.get(0)).equals("--")) {
                objects[0] = null;
            } else {
                objects[0] = tempList.get(0);
            }
            if ((tempList.get(1)).equals("--")) {
                objects[1] = null;
            } else {
                objects[1] = tempList.get(1);
            }
            objects[2] = key;
            //to get values from updateInUserAllInfoQuery
            if ((tempList.get(0)).equals("--")) {
                objinUserInfo[0] = null;
            } else {
                objinUserInfo[0] = tempList.get(0);
            }
            if ((tempList.get(1)).equals("--")) {
                objinUserInfo[1] = null;
            } else {
                objinUserInfo[1] = tempList.get(1);
            }
            if ((tempList.get(2)).equals("--")) {
                objinUserInfo[2] = null;
            } else {
                objinUserInfo[2] = tempList.get(2);
            }
//            if ((tempList.get(3)).equals("--")) {
//                objinUserInfo[3] = null;
//            } else {
//                objinUserInfo[3] = tempList.get(3);
//            }
            objinUserInfo[3] = key;
            // 
            queryList.add(super.buildQuery(updateQuery, objects));
            //queryList.add(super.buildQuery(updateInUserAllInfoQuery, objinUserInfo));
        }
        return super.execMultiple(queryList);
    }

    public boolean updateInReport(ArrayListMultimap changeInReportMultimap) {
        Set<String> elementKeys = changeInReportMultimap.keySet();
        //
        Object[] objects = new Object[5];
        Object[] objinReport = new Object[3];
        Object[] objinQuery = new Object[3];
        //String updateQuery = "UPDATE PRG_USER_ALL_INFO_DETAILS SET USER_COL_DESC = REPLACE(USER_COL_DESC, '&','&'),AGGREGATION_TYPE='&',ACTUAL_COL_FORMULA='&' WHERE ELEMENT_ID = & ";
        String updateInReportQuery = "UPDATE PRG_AR_REPORT_TABLE_DETAILS SET COL_NAME= REPLACE( COL_NAME, '&','&') WHERE QRY_COL_ID IN ( SELECT QRY_COL_ID FROM PRG_AR_QUERY_DETAIL WHERE ELEMENT_ID in (select ELEMENT_ID from PRG_USER_ALL_INFO_DETAILS where REF_ELEMENT_ID='&'))";
        String updateInQueryDetailsQuery = "UPDATE PRG_AR_QUERY_DETAIL SET COL_DISP_NAME = REPLACE( COL_DISP_NAME,'&','&') WHERE ELEMENT_ID in (select ELEMENT_ID from PRG_USER_ALL_INFO_DETAILS where REF_ELEMENT_ID='&')";
        //String query="select REF_ELEMENT_ID from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID='&'";
        ArrayList queryList = new ArrayList();
        //PbReturnObject retobj=new PbReturnObject();

        for (String key : elementKeys) {
            List tempList = (changeInReportMultimap.get(key));
            //to get values from updateInReportQuery
            if ((tempList.get(0)).equals("--")) {
                objinReport[0] = null;
            } else {
                objinReport[0] = tempList.get(0);
            }
            if ((tempList.get(1)).equals("--")) {
                objinReport[1] = null;
            } else {
                objinReport[1] = tempList.get(1);
            }
            objinReport[2] = key;
            //to get values from updateInQueryDetailsQuery
            if ((tempList.get(0)).equals("--")) {
                objinQuery[0] = null;
            } else {
                objinQuery[0] = tempList.get(0);
            }
            if ((tempList.get(1)).equals("--")) {
                objinQuery[1] = null;
            } else {
                objinQuery[1] = tempList.get(1);
            }
            objinQuery[2] = key;
            // queryList.add(super.buildQuery(updateQuery, objects));
            queryList.add(super.buildQuery(updateInReportQuery, objinReport));
            queryList.add(super.buildQuery(updateInQueryDetailsQuery, objinQuery));

            updateInRole(changeInReportMultimap);
        }
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            return super.executeMultiple(queryList);
        } else {
            return super.execMultiple(queryList);
        }

    }

    public void updateInRole(ArrayListMultimap changeInReportMultimap) {
        Set<String> elementKeys = changeInReportMultimap.keySet();
        Object obj[] = new Object[1];
        Object[] objects = new Object[4];
        Object[] objRole = new Object[4];

        PbReturnObject retobj = new PbReturnObject();
        String refelementid = null;
        //String updateQuery = "UPDATE PRG_USER_ALL_INFO_DETAILS SET USER_COL_DESC = REPLACE(USER_COL_DESC, '&','&'),AGGREGATION_TYPE='&' WHERE REF_ELEMENT_ID = & ";
        String updateInRole = "UPDATE PRG_USER_SUB_FOLDER_ELEMENTS SET USER_COL_DESC   = REPLACE( USER_COL_DESC,'&','&'),DEFAULT_AGGREGATION  ='&' WHERE REF_ELEMENT_ID=& and REF_ELEMENT_TYPE in (2,3,4)";
        //String updateInReportQuery = "UPDATE PRG_AR_REPORT_TABLE_DETAILS SET COL_NAME= REPLACE( COL_NAME, '&','&') WHERE QRY_COL_ID IN ( SELECT QRY_COL_ID FROM PRG_AR_QUERY_DETAIL WHERE ELEMENT_ID = '&')";
        // String updateInQueryDetailsQuery = "UPDATE PRG_AR_QUERY_DETAIL SET COL_DISP_NAME = REPLACE( COL_DISP_NAME,'&','&') WHERE ELEMENT_ID='&'";
        String query = "select REF_ELEMENT_ID from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID=&";
        ArrayList queryList = new ArrayList();
        for (String key : elementKeys) {

            obj[0] = key;
            String finalquery = super.buildQuery(query, obj);
            try {
                retobj = super.execSelectSQL(finalquery);
                refelementid = retobj.getFieldValueString(0, 0);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }

            List tempList = (changeInReportMultimap.get(key));
            //to get values from updateQuery
            if ((tempList.get(0)).equals("--")) {
                objects[0] = null;
            } else {
                objects[0] = tempList.get(0);
            }
            if ((tempList.get(1)).equals("--")) {
                objects[1] = null;
            } else {
                objects[1] = tempList.get(1);
            }
            if ((tempList.get(2)).equals("--")) {
                objects[2] = null;
            } else {
                objects[2] = tempList.get(2);
            }
//            if ((tempList.get(3)).equals("--")) {
//                objects[3] = null;
//            } else {
//                objects[3] = tempList.get(3);
//            }
            objects[3] = refelementid;

            // to get updated in Role
            if ((tempList.get(0)).equals("--")) {
                objRole[0] = null;
            } else {
                objRole[0] = tempList.get(0);
            }
            if ((tempList.get(1)).equals("--")) {
                objRole[1] = null;
            } else {
                objRole[1] = tempList.get(1);
            }
            if ((tempList.get(2)).equals("--")) {
                objRole[2] = null;
            } else {
                objRole[2] = tempList.get(2);
            }
//            if ((tempList.get(3)).equals("--")) {
//                objects[3] = null;
//            } else {
//                objects[3] = tempList.get(3);
//            }
            objRole[3] = refelementid;

            //queryList.add(super.buildQuery(updateQuery, objects));
            queryList.add(super.buildQuery(updateInRole, objRole));
            //queryList.add(super.buildQuery(updateInReportQuery, objinReport));
            //queryList.add(super.buildQuery(updateInQueryDetailsQuery, objinQuery));
        }
        super.executeMultiple(queryList);
    }

    public void truncateDimValues() {
        String query = null;
        query = "truncate table PRG_GRP_DIM_MAP_KEY_VALUE";
        try {
            super.execModifySQL(query);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void insertNewDimValues() {
        String addExistingDimValues = null;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            addExistingDimValues = getResourceBundle().getString("addExistingDimValuesMysql");
        } else {
            addExistingDimValues = getResourceBundle().getString("addExistingDimValues");
        }

        try {
            super.execModifySQL(addExistingDimValues);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public String getAllConnectionForModifyMembers() {
        String Query = " SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(Query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option>--SELECT--</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueInt(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 1));
            sb.append("</option>");
        }
        return sb.toString();
    }

    public String getAllMembers(String connectionID, String folderId, String dimId) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        List<String> dimnamelist = new ArrayList<String>();
        List<String> buscolname = new ArrayList<String>();
        List<String> membernamelist = new ArrayList<String>();
        List<String> elementidlist = new ArrayList<String>();
        List<String> memberidlist = new ArrayList<String>();
        List<String> folderidlist = new ArrayList<String>();
        List<String> subfolderidlist = new ArrayList<String>();
        Map dimmap = new HashMap();
        String finalquery = "";
        if (dimId.equals("0000")) {
            String query = "SELECT b.GRP_ID,b.FOLDER_ID,b.SUB_FOLDER_ID,b.SUB_FOLDER_TAB_ID,b.ELEMENT_ID,b.BUSS_COL_ID,b.DIM_NAME,b.BUSS_COL_NAME,"
                    + "b.MEMBER_NAME,b.MEMBER_ID,b.CONNECTION_ID FROM PRG_USER_ALL_INFO_DETAILS  b inner join PRG_USER_ALL_ADIM_KEY_VAL_ELE  p "
                    + " on (b.ELEMENT_ID = p.KEY_ELEMENT_ID) where connection_id in ('&') and FOLDER_ID in ('&') and b.DIM_NAME not in('Time')  order by b.MEMBER_NAME,p.LEVEL1";
            Object obj[] = new Object[2];
            obj[0] = connectionID;
            obj[1] = folderId;
            finalquery = super.buildQuery(query, obj);
        } else {
            String query = "SELECT b.GRP_ID,b.FOLDER_ID,b.SUB_FOLDER_ID,b.SUB_FOLDER_TAB_ID,b.ELEMENT_ID,b.BUSS_COL_ID,b.DIM_NAME,b.BUSS_COL_NAME,"
                    + "b.MEMBER_NAME,b.MEMBER_ID,b.CONNECTION_ID FROM PRG_USER_ALL_INFO_DETAILS  b inner join PRG_USER_ALL_ADIM_KEY_VAL_ELE  p "
                    + " on (b.ELEMENT_ID = p.KEY_ELEMENT_ID) where connection_id in ('&') and FOLDER_ID in ('&') and DIM_ID in ('&') and b.DIM_NAME not in('Time')  order by b.MEMBER_NAME,p.LEVEL1";
            Object obj[] = new Object[3];
            obj[0] = connectionID;
            obj[1] = folderId;
            obj[2] = dimId;
            finalquery = super.buildQuery(query, obj);
        }
        try {
            returnObject = super.execSelectSQL(finalquery);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    dimnamelist.add(returnObject.getFieldValueString(i, 6));
                    buscolname.add(returnObject.getFieldValueString(i, 7));
                    membernamelist.add(returnObject.getFieldValueString(i, 8));
                    elementidlist.add(returnObject.getFieldValueString(i, 4));
                    memberidlist.add(returnObject.getFieldValueString(i, 9));
                    folderidlist.add(returnObject.getFieldValueString(i, 1));
                    subfolderidlist.add(returnObject.getFieldValueString(i, 2));
                }
            }
            dimmap.put("elementidlist", elementidlist);
            dimmap.put("dimnamelist", dimnamelist);
            dimmap.put("buscolname", buscolname);
            dimmap.put("membernamelist", membernamelist);
            dimmap.put("memberidlist", memberidlist);
            dimmap.put("folderidlist", folderidlist);
            dimmap.put("subfolderidlist", subfolderidlist);
            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;
    }

    public boolean getModifyMembers(String elementid, String dimnamelist, String oldmembernamelist, String newmembernamelist, String folderidlist, String subfolderidlist, String memberidlist) {
        Object[] obj = new Object[7];
        obj[0] = newmembernamelist;
        obj[1] = newmembernamelist;
        obj[2] = newmembernamelist;
        obj[3] = newmembernamelist;
        obj[4] = dimnamelist;
        obj[5] = oldmembernamelist;
        obj[6] = elementid;

        Object[] obj1 = new Object[5];
        obj1[0] = newmembernamelist;
        obj1[1] = dimnamelist;
        obj1[2] = oldmembernamelist;
        obj1[3] = folderidlist;
        obj1[4] = memberidlist;

        Object[] obj2 = new Object[5];
        obj2[0] = newmembernamelist;
        obj2[1] = newmembernamelist;
        obj2[2] = dimnamelist;
        obj2[3] = oldmembernamelist;
        obj2[4] = subfolderidlist;
        Object[] obj3 = new Object[5];
        obj3[0] = newmembernamelist;
        obj3[1] = newmembernamelist;
        obj3[2] = dimnamelist;
        obj3[3] = oldmembernamelist;
        obj3[4] = elementid;
        ArrayList queryList = new ArrayList();
        String updateQuery1 = "UPDATE PRG_USER_ALL_INFO_DETAILS "
                + " SET DISP_NAME     ='&' ,"
                + " MEMBER_NAME       ='&' "
                + " WHERE DIM_NAME    ='&' "
                + " AND DISP_NAME     ='&' "
                + " AND element_id    ='&'";

        String updateQuery2 = "UPDATE PRG_USER_ALL_DDIM_KEY_VAL_ELE "
                + " SET KEY_MEMBER_NAME         ='&' ,"
                + " VAL_MEMBER_NAME             ='&' ,"
                + " KEY_DISP_NAME               ='&' ,"
                + " VAL_DISP_NAME               ='&' "
                + " WHERE KEY_DIM_NAME          ='&' "
                + " AND KEY_MEMBER_NAME         ='&' "
                + " AND KEY_ELEMENT_ID          ='&' ";

        String updateQuery3 = "update PRG_USER_ALL_DDIM_MASTER "
                + " set INFO_MEMBER_NAME          ='&' "
                + " where INFO_DIM_NAME           ='&' "
                + " AND INFO_MEMBER_NAME          ='&' "
                + " AND INFO_FOLDER_ID            ='&' "
                + " AND INFO_MEMBER_ID            ='&' ";

        String updateQuery4 = "UPDATE PRG_USER_SUB_FOLDER_TABLES "
                + " SET DISP_NAME         ='&' ,"
                + " MEMBER_NAME           ='&' "
                + " WHERE DIM_NAME        ='&' "
                + " AND DISP_NAME         ='&' "
                + " AND SUB_FOLDER_ID     ='&' ";

        queryList.add(super.buildQuery(updateQuery2, obj));
        queryList.add(super.buildQuery(updateQuery3, obj1));
        queryList.add(super.buildQuery(updateQuery4, obj2));
        queryList.add(super.buildQuery(updateQuery1, obj3));

        boolean query = super.executeMultiple(queryList);

        return query;
    }

    public boolean getModifyMembersRep(String elementid, String oldmembernamelist, String newmembernamelist, String folderidlist) {
        Object[] obj1 = new Object[4];
        obj1[0] = newmembernamelist;
        obj1[1] = oldmembernamelist;
        obj1[2] = elementid;
        obj1[3] = folderidlist;

        ArrayList queryList = new ArrayList();

        String updateQuery1 = "UPDATE PRG_AR_REPORT_PARAM_DETAILS "
                + "SET param_disp_name   ='&' "
                + "WHERE param_disp_name ='&' "
                + "AND element_id        = '&' "
                + "and REPORT_ID IN"
                + "(select REPORT_ID from PRG_AR_REPORT_DETAILS where folder_id in ('&'))";

        queryList.add(super.buildQuery(updateQuery1, obj1));
        boolean query = super.executeMultiple(queryList);
        logger.info("Renamed in Reports");
        return query;

    }
    // added by sunita

    public boolean partialPublishDimentionsCheck(String dimid, String subfolderid, String folderid) {
        PbReturnObject returnObject = null;
//        String busstableid = null;
        boolean result = false;
        String query = "select * from prg_user_all_info_details where dim_id=" + dimid + " and sub_folder_id=" + subfolderid + " and folder_id=" + folderid;
        try {
            returnObject = super.execSelectSQL(query);
            if (returnObject.rowCount > 0) {
                result = true;
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        return result;
    }

    public boolean partialPublishDimentions(String dimid, String subfolderid, String folderid, String BucketType) {
        PbReturnObject returnObject = null;
        String busstableid = null;
        boolean result = false;
        String query = "select buss_table_id from PRG_USER_SUB_FOLDER_TABLES where dim_id=" + dimid;
        try {
            returnObject = super.execSelectSQL(query);
            busstableid = returnObject.getFieldValueString(0, 0);
            result = partialPublish(busstableid, folderid, BucketType);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return result;
    }

    public boolean partialPublish(String busstableid, String folderid, String BucketType) {
        String finalquery = null;
        String insertinUserAllInfoDetails = getResourceBundle().getString("insertinUserAllInfoDetails");
        String insertintoallAdimDetails = getResourceBundle().getString("insertintoallAdimDetails");
        String insertintoallAdimKeyValueElements = getResourceBundle().getString("insertintoallAdimKeyValueElements");
        String insertintoallAdimMasterdetails = getResourceBundle().getString("insertintoallAdimMasterdetails");
        String insertallDdimDetails;
        String insertallDdimkeyValueElement;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertallDdimDetails = getResourceBundle().getString("insertallDdimDetailsMysql");
            insertallDdimkeyValueElement = getResourceBundle().getString("insertallDdimkeyValueElementMysql");
        } else {
            insertallDdimDetails = getResourceBundle().getString("insertallDdimDetails");
            insertallDdimkeyValueElement = getResourceBundle().getString("insertallDdimkeyValueElement");
        }

        String insertallDdimMasterDetails = getResourceBundle().getString("insertallDdimMasterDetails");
        Object obj[] = new Object[2];
        obj[0] = folderid;
        obj[1] = busstableid;
        Object keyvalueeleobj[] = new Object[4];
        keyvalueeleobj[0] = folderid;
        keyvalueeleobj[1] = folderid;
        keyvalueeleobj[2] = busstableid;
        keyvalueeleobj[3] = busstableid;
        ArrayList partialpublishquerylist = new ArrayList();
        finalquery = super.buildQuery(insertinUserAllInfoDetails, obj);
        partialpublishquerylist.add(finalquery);
        finalquery = super.buildQuery(insertintoallAdimDetails, obj);
        //
        partialpublishquerylist.add(finalquery);
        finalquery = super.buildQuery(insertintoallAdimKeyValueElements, keyvalueeleobj);
        //
        partialpublishquerylist.add(finalquery);
        finalquery = super.buildQuery(insertintoallAdimMasterdetails, obj);
        //
        partialpublishquerylist.add(finalquery);
        finalquery = super.buildQuery(insertallDdimDetails, obj);
        //
        partialpublishquerylist.add(finalquery);
        finalquery = super.buildQuery(insertallDdimkeyValueElement, keyvalueeleobj);
        //
        partialpublishquerylist.add(finalquery);
        finalquery = super.buildQuery(insertallDdimMasterDetails, obj);
        //
        partialpublishquerylist.add(finalquery);
        if (BucketType.equalsIgnoreCase("SUMMARIZED")) {
            String updateUserAllInfo = "update prg_user_all_info_details set IS_SUMMARIZED_BUCKET = 'Y' where buss_table_id = " + busstableid;
            partialpublishquerylist.add(updateUserAllInfo);
        }
        boolean result = super.executeMultiple(partialpublishquerylist);

        return result;

    }

    public void SaveExtraMembers(String tableid, String dbtabName, String extrameber) {
        Connection conn = ProgenConnection.getInstance().getConnectionByTable(tableid);

//        String qry = "ALTER TABLE & add & VARCHAR(400 BYTE)";
        Object obj[] = new Object[2];
        obj[0] = dbtabName;
        obj[1] = extrameber;

    }
    //added by anil

    public String getAllTargetMeasures(String userId, String bsrSelectedID, String contextPath, boolean fromBPM) {
        ArrayList Parameters = null;
        StringBuilder strBuilder = new StringBuilder();
        if (!fromBPM) {
            strBuilder.append("<table style=\"width:100%;\"><tr><td width=\"50%\"><table><tr><td><h5>Element</h5></td><td><h5><input id=\"elementname\" type=\"text\" value=\"\" name=\"elementname\"/></h5></td></tr></table></td><td><table><tr><td><h5>MapppedTo</h5></td><td><h5><input id=\"mapto\" type=\"text\" value=\"\" name=\"mapto\"></h5></td></tr></table></td></tr></table>");
        }
        if (!fromBPM) {
            strBuilder.append("<table style=\"width:100%;height:220px\" border=\"solid black 1px\" >");
        } else {
            strBuilder.append("<table style=\"width:50%;height:220px\" border=\"solid black 1px\" >");
        }
        strBuilder.append("<tr><td width=\"50%\" valign=\"top\" class=\"draggedTable1\">");
        strBuilder.append("<div style=\"height:20px\" class=\"ui-state-default draggedDivs ui-corner-all\">");
        if (!fromBPM) {
            strBuilder.append("<font size=\"2\" style=\"font-weight:bold\">Data From BusinessRole</font></div><div class=\"masterDiv\" style=\"height:200px;overflow-y:auto\">");
        } else {
            strBuilder.append("<font size=\"2\" style=\"font-weight:bold\">BusinessRole FactTables</font></div><div class=\"masterDiv\" style=\"height:200px;overflow-y:auto\">");
        }
        strBuilder.append("<ul id=\"kpiTree\" class=\"filetree treeview-famfamfam\">");
        strBuilder.append("<span class='openmenu'>");
        strBuilder.append(getTargetMeasures(bsrSelectedID, Parameters, "1", contextPath, fromBPM));
        strBuilder.append("</span>");
        if (!fromBPM) {
            strBuilder.append("</ul></div></td>");
            strBuilder.append("<td width=\"50%\" valign=\"top\">");
            strBuilder.append("<div style=\"height:20px\" class=\"ui-state-default draggedDivs ui-corner-all\"><font size=\"2\" style=\"font-weight:bold\">Data From BusinessRole</font></div>");
            strBuilder.append("<div class=\"masterDiv1\" style=\"height:200px;overflow-y:auto\">");
            strBuilder.append("<ul id=\"kpiTree1\" class=\"filetree treeview-famfamfam\">");
            strBuilder.append("<span class='openmenu1'>");
            strBuilder.append(getTargetMeasures(bsrSelectedID, Parameters, "2", contextPath, fromBPM));
            strBuilder.append("</span>");
        }
        strBuilder.append("</ul></div></td></tr></table>");
        strBuilder.append("<br>");
        if (!fromBPM) {
            strBuilder.append("<table><tr width:100%><td align=\"center\"><input id=\"btn\" class=\"navtitle-hover\" type=\"button\" onclick=\"mappedElements()\" style=\"height: 25px; width: auto;\" value=\"Done\"></td></tr></table>");
        }
        return strBuilder.toString();
    }

    public String getTargetMeasures(String foldersIds, ArrayList Parameters, String divID, String contextPath, boolean fromBPM) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String factName = "";
        String subFolderTabid = "";
        String facttooltip = "";
        String userFolderIds = foldersIds;
        String[] colNames = null;
        Object obj[] = null;
        String sql = null;
        GetDimFactMapping getdimmap = new GetDimFactMapping();
        if (Parameters != null && !Parameters.isEmpty()) {
            obj = new Object[2];
            obj[0] = foldersIds;

            try {
                obj[1] = getdimmap.getFact(Parameters);
            } catch (SQLException ex) {
//            logger.error("Exception:",ex);
            }
            sql = getResourceBundle().getString("getFactsNew");
        } else {
            obj = new Object[1];
            obj[0] = foldersIds;
            sql = getResourceBundle().getString("getAllFactsNew");
        }
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);

            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                factName = retObj.getFieldValueString(i, colNames[1]);
                subFolderTabid = retObj.getFieldValueString(i, colNames[0]);
                facttooltip = retObj.getFieldValueString(i, colNames[2]);
                outerBuffer.append("<li class='closed' id=" + divID + "_'" + factName + i + "'>");
                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/table.png'></img>");
                if (!fromBPM) {
                    outerBuffer.append("<span style='font-family:verdana;' id='1~" + factName + " ' class=\"" + divID + "_span\" title='" + facttooltip + "'>" + factName + "</span>");
                } else {
                    outerBuffer.append("<span style='font-family:verdana;' id='1~" + factName + " '  title='" + facttooltip + "'>" + factName + "</span>");
                }
                outerBuffer.append("<ul id=" + divID + "_'factName-" + factName + "'>");
                outerBuffer.append(getTargetMeasureElements(userFolderIds, subFolderTabid, contextPath, divID));
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException exception) {
            logger.error("Exception:", exception);
        }

        return outerBuffer.toString();
    }

    public String getTargetMeasureElements(String userFolderIds, String subFolderTabid, String contextPath, String divID) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        //obj[0] = userFolderIds;
        obj[0] = subFolderTabid;
        String ElementId = "";
        String REFElementId = "";
        String ElementId1 = "";
        String REFElementId1 = "";
        String ElementName = "";
        String ElementName1 = "";
        String Formula = "";
//        String colId = "";
        String sql = getResourceBundle().getString("getFactElements");
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);

            ////////.println("finalQuery in getmeasureelements are : "+finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            ArrayList list = new ArrayList();
            String viewFormulaClass = "formulaViewMenu";
            for (int i = 0; i < retObj.getRowCount(); i++) {
                viewFormulaClass = "";
                ElementId = retObj.getFieldValueString(i, colNames[0]);
                ElementName = retObj.getFieldValueString(i, colNames[1]);
                REFElementId = retObj.getFieldValueString(i, colNames[2]);
                Formula = retObj.getFieldValueString(i, colNames[7]);
                if (ElementId.equalsIgnoreCase(REFElementId)) {
                    list.add(ElementId);
                    outerBuffer.append("<li class='closed'>");
                    if (!Formula.equalsIgnoreCase("")) {
                        outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/document-attribute-f.png'></img>");
                        viewFormulaClass = "formulaViewMenu";
                    } else {
                        outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/report.png'></img>");
                    }
                    outerBuffer.append("<span class=\"" + divID + "_span\" id='" + ElementId + "~" + ElementName + " '  title='" + Formula + "' style='font-family:verdana;'>" + ElementName + "</span>");

                    outerBuffer.append("<ul >");
                    for (int j = 0; j < retObj.getRowCount(); j++) {
                        ElementId1 = retObj.getFieldValueString(j, colNames[0]);
                        REFElementId1 = retObj.getFieldValueString(j, colNames[2]);
                        ElementName1 = retObj.getFieldValueString(j, colNames[1]);
                        Formula = retObj.getFieldValueString(j, colNames[7]);
                        if (ElementId.equalsIgnoreCase(REFElementId1) && !(ElementId.equalsIgnoreCase(ElementId1))) {
                            outerBuffer.append("<li class='closed' id='" + ElementId1 + "'>");
                            if (!Formula.equalsIgnoreCase("")) {
                                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/document-attribute-f.png'></img>");
                            } else {
                                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/report.png'></img>");
                            }
                            outerBuffer.append("<span id='" + ElementId1 + "~" + ElementName1 + "'  class=\"" + divID + "_span\" title='" + Formula + "' style='font-family:verdana;'>" + ElementName1 + "</span>");
                            outerBuffer.append("</li>");
                        }
                    }
                    outerBuffer.append("</ul>");
                    outerBuffer.append("</li>");
                }
            }

        } catch (SQLException exception) {
            logger.error("Exception:", exception);
        }
        return outerBuffer.toString();
    }

    public String getMappedElements(String eleID, String mappedEleID, String contextPath) {
//        PbReturnObject updated = new PbReturnObject();
        PbReturnObject elementID = new PbReturnObject();
//        String finalQuery = "";
        String query1 = "";
        String query2 = "";
        String[] colNames = null;
        int z = 0;
        Object[] obj = new Object[2];
        obj[0] = eleID;
        obj[1] = mappedEleID;
        PbReturnObject eleRefID = new PbReturnObject();
        query1 = "select REF_ELEMENT_ID from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID=" + eleID;
        try {
            eleRefID = execSelectSQL(query1);
            colNames = eleRefID.getColumnNames();
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        String elementRefId = "";
        for (int j = 0; j < eleRefID.getRowCount(); j++) {
            elementRefId = eleRefID.getFieldValueString(j, colNames[0]);
        }
        query2 = "select ELEMENT_ID from PRG_USER_ALL_INFO_DETAILS where REF_ELEMENT_ID=" + elementRefId;
        try {
            elementID = execSelectSQL(query2);
            colNames = elementID.getColumnNames();
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        ArrayList elementIds = new ArrayList();
        for (int j = 0; j < elementID.getRowCount(); j++) {
            elementIds.add(elementID.getFieldValueString(j, colNames[0]));
        }
        if ((elementIds.size() == 1) || (elementRefId != eleID)) {

            String query = "update PRG_USER_ALL_INFO_DETAILS set TARGET_MAP_ELEMENT=" + mappedEleID + " where ELEMENT_ID=" + eleID;
            try {
                z = execUpdateSQL(query);
            } catch (Exception e) {
                logger.error("Exception:", e);
            }
        }
        PbReturnObject mapRefID = new PbReturnObject();
        String query3 = "select REF_ELEMENT_ID from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID=" + mappedEleID;
        try {
            mapRefID = execSelectSQL(query3);
            colNames = mapRefID.getColumnNames();
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        String mappedRefId = "";
        for (int j = 0; j < mapRefID.getRowCount(); j++) {
            mappedRefId = mapRefID.getFieldValueString(j, colNames[0]);
        }
        PbReturnObject mapElementID = new PbReturnObject();
        String query4 = "select ELEMENT_ID from PRG_USER_ALL_INFO_DETAILS where REF_ELEMENT_ID=" + mappedRefId;
        try {
            mapElementID = execSelectSQL(query4);
            colNames = elementID.getColumnNames();
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        ArrayList mapElementIds = new ArrayList();
        for (int j = 0; j < mapElementID.getRowCount(); j++) {
            mapElementIds.add(mapElementID.getFieldValueString(j, colNames[0]));
        }
        if ((elementRefId.equals(eleID)) & (elementIds.size() >= 1) & (mapElementIds.size() == 1)) {
            for (int i = 0; i < elementIds.size(); i++) {
                String query = "update PRG_USER_ALL_INFO_DETAILS set TARGET_MAP_ELEMENT=" + mappedEleID + " where ELEMENT_ID=" + elementIds.get(i);
                try {
                    z = execUpdateSQL(query);
                } catch (Exception e) {
                    logger.error("Exception:", e);
                }
            }
        }
        if ((elementRefId.equals(eleID)) & (elementIds.size() >= 1) & (mapElementIds.size() >= 1)) {
            for (int i = 0; i < elementIds.size(); i++) {
                String query = "update PRG_USER_ALL_INFO_DETAILS set TARGET_MAP_ELEMENT=" + mapElementIds.get(i) + " where ELEMENT_ID=" + elementIds.get(i);
                try {
                    z = execUpdateSQL(query);
                } catch (Exception e) {
                    logger.error("Exception:", e);
                }
            }

        }
        if ((elementRefId.equals(eleID)) & (elementIds.size() >= 1) & (mappedRefId != mappedEleID)) {
            for (int i = 0; i < elementIds.size(); i++) {
                String query = "update PRG_USER_ALL_INFO_DETAILS set TARGET_MAP_ELEMENT=" + mappedEleID + " where ELEMENT_ID=" + elementIds.get(i);
                try {
                    z = execUpdateSQL(query);
                } catch (Exception e) {
                    logger.error("Exception:", e);
                }
            }
        }
        return Integer.toString(z);
    }
    //added by Nazneen

    public String getEmailConfigDetails() {

        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        List<String> hostName = new ArrayList<String>();
        List<String> portNo = new ArrayList<String>();
        List<String> fromAdd = new ArrayList<String>();
        List<String> debug = new ArrayList<String>();
        List<String> userId = new ArrayList<String>();
        List<String> password = new ArrayList<String>();
        List<String> sslStatus = new ArrayList<String>();
        Map dimmap = new HashMap();
//        String finalquery = "";

        String query = "SELECT HOST_NAME,PORT_NO,FROM_ADD,DEBUG,USER_ID,PWD,ssl_status from PRG_CONFIG_EMAIL";

        try {
            returnObject = super.execSelectSQL(query);
            if (returnObject != null) {
                hostName.add(returnObject.getFieldValueString(0, 0));
                portNo.add(returnObject.getFieldValueString(0, 1));
                fromAdd.add(returnObject.getFieldValueString(0, 2));
                debug.add(returnObject.getFieldValueString(0, 3));
                userId.add(returnObject.getFieldValueString(0, 4));
                String p1 = returnObject.getFieldValueString(0, 5);
                String paswd = DeEncrypter.getInstance().decrypt(p1);
                password.add(paswd);
                sslStatus.add(returnObject.getFieldValueString(0, 6));
            }
            dimmap.put("hostName", hostName);
            dimmap.put("portNo", portNo);
            dimmap.put("fromAdd", fromAdd);
            dimmap.put("debug", debug);
            dimmap.put("userId", userId);
            dimmap.put("password", password);
            dimmap.put("sslStatus", sslStatus);
            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;
    }

    public int saveEmailConfigDetails(String hostName, String portNo, String fromAdd, String checkDebug, String userId, String password, String sslStatus) {

        String passw = DeEncrypter.getInstance().encrypt(password);
        if (sslStatus.equalsIgnoreCase("null")) {
            sslStatus = "false";
        }
        String savedetails = "";
        int result = 0;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            savedetails = "UPDATE PRG_CONFIG_EMAIL"
                    + " SET ID = 1"
                    + " ,HOST_NAME = '" + hostName + "'"
                    + " ,PORT_NO = '" + portNo + "'"
                    + " ,FROM_ADD = '" + fromAdd + "'"
                    + " ,DEBUG = '" + checkDebug + "'"
                    + " ,USER_ID = '" + userId + "'"
                    + " ,PWD = '" + passw + "'"
                    + " ,ssl_status = '" + sslStatus + "'";
        } else {
            savedetails = "UPDATE PRG_CONFIG_EMAIL"
                    + " SET ID = PRG_CONFIG_EMAIL_SEQ.nextval"
                    + " ,HOST_NAME = '" + hostName + "'"
                    + " ,PORT_NO = '" + portNo + "'"
                    + " ,FROM_ADD = '" + fromAdd + "'"
                    + " ,DEBUG = '" + checkDebug + "'"
                    + " ,USER_ID = '" + userId + "'"
                    + " ,PWD = '" + passw + "'"
                    + " ,ssl_status = '" + sslStatus + "'";

        }
        try {
            result = execUpdateSQL(savedetails);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return (result);
    }
    //added by nazneen

    public void updateSecMemberValueStatus(String userId, String subFolderId, String memberValue, String dimId, String baseElementId, String oldFilterId, String newFilterId) throws Exception {
        String grpId = "";
        String folderId = "";
        String baseBussTableId = "";
        String baseBussColName = "";
//        String memberId = "";
//        String elementId = "";
//        String SubFolderTabId = "";
//        String dimTabId = "";
//        String bussTabId = "";
//        String bussColName = "";
//        String deleteSecMemberFilter = "";
//        String addSecMemberFilter = "";
        PbReturnObject baseEleObj = null;
//        PbReturnObject existeClauseObj = null;
        PbReturnObject pbReturnObject1 = null;
        ArrayList securityQueryList = new ArrayList();

        PbReturnObject pbro = null;
        String checkForFilterString = "Select * from PRG_USER_DENOM_SEC_INFO where USER_ID=" + userId + " and  clause_id =" + oldFilterId;
        pbro = execSelectSQL(checkForFilterString);
        if (pbro != null) {
            String deleteDataQ = "delete from PRG_USER_DENOM_SEC_INFO where USER_ID=" + userId + " and  clause_id =" + oldFilterId;
            securityQueryList.add(deleteDataQ);
        }

        String baseEleDetails = "select grp_id,folder_id,buss_table_id,buss_col_name from prg_user_all_info_details where element_id =" + baseElementId;
        String getAllElements = "SELECT A.sub_folder_tab_id,A.member_id ,A.dim_tab_id,B.element_id,b.buss_table_id,b.buss_col_name FROM PRG_USER_SUB_FOLDER_TABLES A,PRG_USER_SUB_FOLDER_ELEMENTS B,PRG_GRP_DIM_MAP_KEY_VALUE map WHERE map.key1 = B.buss_col_id and A.member_id = map.key_mem_id and A.sub_folder_tab_id = B.sub_folder_tab_id and A.sub_folder_id =" + subFolderId + " AND A.is_dimension ='Y' AND A.dim_id =" + dimId + " ORDER BY b.buss_col_name";

//        String status = "";
        String insertinuserDenomSecInfo = "";

        baseEleObj = execSelectSQL(baseEleDetails);
        grpId = baseEleObj.getFieldValueString(0, 0);
        folderId = baseEleObj.getFieldValueString(0, 1);
        baseBussTableId = baseEleObj.getFieldValueString(0, 2);
        baseBussColName = baseEleObj.getFieldValueString(0, 3);

        pbReturnObject1 = super.execSelectSQL(getAllElements);
        String finalquery = null;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            insertinuserDenomSecInfo = getResourceBundle().getString("insertinuserDenomSecInfoSql");
        } else {
            insertinuserDenomSecInfo = getResourceBundle().getString("insertinuserDenomSecInfo");
        }
        Object obj[] = new Object[16];
        finalquery = super.buildQuery(insertinuserDenomSecInfo, obj);

        if (pbReturnObject1 != null) {
            for (int i = 0; i < pbReturnObject1.getRowCount(); i++) {
                obj[0] = grpId;
                obj[1] = dimId;
                obj[2] = folderId;
                obj[3] = subFolderId;
                obj[4] = pbReturnObject1.getFieldValueString(i, 0);
                obj[5] = pbReturnObject1.getFieldValueString(i, 2);
                obj[6] = userId;
                obj[7] = pbReturnObject1.getFieldValueString(i, 1);
                obj[8] = pbReturnObject1.getFieldValueString(i, 3);
                obj[9] = newFilterId;
                obj[10] = baseElementId;
                obj[11] = baseBussTableId;
                obj[12] = baseBussColName;
                obj[13] = pbReturnObject1.getFieldValueString(i, 4);
                obj[14] = pbReturnObject1.getFieldValueString(i, 5);

                finalquery = super.buildQuery(insertinuserDenomSecInfo, obj);

                securityQueryList.add(finalquery);
            }

        }
        super.executeMultiple(securityQueryList);
    }

    public void addSecMemberFilter(String userId, String subFolderId, String memberValue, String dimId, String baseElementId, String filterId) throws Exception {
        String grpId = "";
        String folderId = "";
        String baseBussTableId = "";
        String baseBussColName = "";
//        String memberId = "";
//        String elementId = "";
//        String SubFolderTabId = "";
//        String dimTabId = "";
//        String bussTabId = "";
//        String bussColName = "";
//        String deleteSecMemberFilter = "";
//        String addSecMemberFilter = "";
        PbReturnObject baseEleObj = null;
//        PbReturnObject existeClauseObj = null;
        PbReturnObject pbReturnObject1 = null;

        String baseEleDetails = "select grp_id,folder_id,buss_table_id,buss_col_name from prg_user_all_info_details where element_id =" + baseElementId;
        String getAllElements = "SELECT A.sub_folder_tab_id,A.member_id ,A.dim_tab_id,B.element_id,b.buss_table_id,b.buss_col_name FROM PRG_USER_SUB_FOLDER_TABLES A,PRG_USER_SUB_FOLDER_ELEMENTS B,PRG_GRP_DIM_MAP_KEY_VALUE map WHERE map.key1 = B.buss_col_id and A.member_id = map.key_mem_id and A.sub_folder_tab_id = B.sub_folder_tab_id and A.sub_folder_id =" + subFolderId + " AND A.is_dimension ='Y' AND A.dim_id =" + dimId + " ORDER BY b.buss_col_name";

//        String status = "";
        String insertinuserDenomSecInfo = "";

        baseEleObj = execSelectSQL(baseEleDetails);
        grpId = baseEleObj.getFieldValueString(0, 0);
        folderId = baseEleObj.getFieldValueString(0, 1);
        baseBussTableId = baseEleObj.getFieldValueString(0, 2);
        baseBussColName = baseEleObj.getFieldValueString(0, 3);

        pbReturnObject1 = super.execSelectSQL(getAllElements);
        String finalquery = null;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            insertinuserDenomSecInfo = getResourceBundle().getString("insertinuserDenomSecInfoSql");
        } else {
            insertinuserDenomSecInfo = getResourceBundle().getString("insertinuserDenomSecInfo");
        }
        Object obj[] = new Object[15];
        ArrayList securityQueryList = new ArrayList();
        finalquery = super.buildQuery(insertinuserDenomSecInfo, obj);
        //added by Nazneen feb14
        if (filterId == null || filterId.equalsIgnoreCase("null") || filterId.equalsIgnoreCase("")) {
            filterId = "'null'";
        }
        if (pbReturnObject1 != null) {
            for (int i = 0; i < pbReturnObject1.getRowCount(); i++) {
                obj[0] = grpId;
                obj[1] = dimId;
                obj[2] = folderId;
                obj[3] = subFolderId;
                obj[4] = pbReturnObject1.getFieldValueString(i, 0);
                obj[5] = pbReturnObject1.getFieldValueString(i, 2);
                obj[6] = userId;
                obj[7] = pbReturnObject1.getFieldValueString(i, 1);
                obj[8] = pbReturnObject1.getFieldValueString(i, 3);
                obj[9] = filterId;
                obj[10] = baseElementId;
                obj[11] = baseBussTableId;
                obj[12] = baseBussColName;
                obj[13] = pbReturnObject1.getFieldValueString(i, 4);
                obj[14] = pbReturnObject1.getFieldValueString(i, 5);

                finalquery = super.buildQuery(insertinuserDenomSecInfo, obj);
                securityQueryList.add(finalquery);
            }
        }
        super.executeMultiple(securityQueryList);

    }
    //added by sunita

    public String getTableDetails1(String groupID) {
        String Query = "select  buss_table_id,buss_table_name FROM PRG_GRP_BUSS_TABLE   where grp_id=" + groupID;

        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(Query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='0000'>All</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueString(i, 1) + "'>");
            sb.append(pbro.getFieldValueString(i, 1));
            sb.append("</option>");
        }
        return sb.toString();
    }

    public String getAllColumns(String tablesSelected, String connId, String grpId) {

        String query = "";

        Connection con = null;
        query = "select * from " + tablesSelected;

        StringBuilder sb = new StringBuilder();
        PbReturnObject pbro = new PbReturnObject();

        try {
            con = ProgenConnection.getInstance().getConnectionByConId(connId);
            if (con != null) {

                ResultSet rs = null;
                Statement ps = null;
                try {
                    ps = con.createStatement();
                    rs = ps.executeQuery(query);
                    pbro = new PbReturnObject(rs);
                    rs.close();
                    rs = null;
                    ps.close();
                    ps = null;
                    con.close();
                    con = null;
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                } finally {
                    if (ps != null) {
                        ps.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    if (con != null) {
                        con.close();
                    }

                }
            }   //pbro = new PbDb().execSelectSQL(query);
            sb.append("<tr>");
            String ColNames[] = pbro.getColumnNames();
            String columns = "";
            sb.append("<td></td>");
            for (int i = 0; i < pbro.getColumnCount(); i++) {
                columns = columns + ColNames[i] + ",";
                sb.append(" <td class='navtitle-hover' valign='top' style='font-weight:normal'  id=" + ColNames[i] + " >" + ColNames[i] + "</td> ");
            }
            sb.append("</tr>");
            for (int i = 0; i < pbro.getRowCount(); i++) {
//                String rowid = "";
                StringBuilder rowid = new StringBuilder();
                for (int j = 0; j < pbro.getColumnCount(); j++) {
                    if (!pbro.getFieldValueString(i, j).equals("")) {
//                        rowid = rowid + pbro.getFieldValueString(i, j);
                        rowid.append(pbro.getFieldValueString(i, j));
                    }
                    if (!pbro.getFieldValueString(i, j + 1).equals("")) {
//                        rowid = rowid + ",";
                        rowid.append(",");
                    }

                }
                //
                sb.append("<tr id='" + rowid + "'>");
                sb.append(" <td  ><a class='ui-icon ui-icon-closethick'  title='Delete row' href='javascript:void(0);' style='background-color: white;' onclick=\"deleteRow('" + rowid + "')\" ></a></td> ");
                for (int j = 0; j < pbro.getColumnCount(); j++) {
                    sb.append(" <td  valign='top' style='font-weight:normal'  id=" + pbro.getFieldValueString(i, j) + " >" + pbro.getFieldValueString(i, j) + "</td> ");
                }
                sb.append("</tr>");
                sb.append("<input type='hidden' name='tabCol' id='tabCol' value=" + columns + ">");
            }

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return sb.toString();
    }

    public String getGroupDetails(String userId, String connectionID) {
        String Query = "SELECT GRP_ID,GRP_NAME FROM PRG_GRP_MASTER WHERE CONNECTION_ID =" + connectionID;
        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(Query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option>--SELECT--</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueInt(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 1));
            sb.append("</option>");
        }
        return sb.toString();
    }

    public void deleteRow1(String tableCol, String conId, String rowID, String tablesSelected) {
        String query = "";
        String colName[] = tableCol.split(",");
        String colVal[] = rowID.split(",");
//        String q1 = "";
        StringBuilder q1 = new StringBuilder();
        for (int i = 0; i < colVal.length; i++) {
            if (i != (colVal.length - 1)) {
                q1.append(colName[i]).append("= '").append(colVal[i]).append("' and ");
            } else {
//                q1 = q1 + colName[i] + "= '" + colVal[i] + "'";
                q1.append(colName[i]).append("= '").append(colVal[i]).append("'");
            }
        }
        Connection con = null;
        query = "delete from " + tablesSelected + " where " + q1;
        try {
            con = ProgenConnection.getInstance().getConnectionByConId(conId);
            if (con != null) {
                PreparedStatement ps = null;
                try {
                    ps = con.prepareStatement(query);
                    ps.execute();
                    ps.close();
                    ps = null;
                    con.close();
                    con = null;
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                } finally {
                    if (ps != null) {
                        ps.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                }
            }   //pbro = new PbDb().execSelectSQL(query);

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
    }

    public String getAllDefineMeasures(String userId, String bsrSelectedID, String contextPath) {
        ArrayList parameters = null;
//        ReportTemplateDAO templateDAO = new ReportTemplateDAO();
        StringBuilder measureBuilder = new StringBuilder();
        measureBuilder.append("<table style=\"width:100%;\"><tr><td width=\"50%\"><table><tr><td><h5>Element</h5></td><td><h5><input id=\"elementname\" type=\"text\" value=\"\" name=\"elementname\"/></h5></td></tr></table></td><td><table><tr><td></td><td></td></tr></table></td></tr></table>");
        // measureBuilder.append("<table style=\"width:100%;\"><tr align=\"left\"><td align=\"right\"><h5>Element</h5></td><td><h5><input id=\"elementname\" type=\"text\" value=\"\" name=\"elementname\"/></h5></td></tr></table>");
        measureBuilder.append("<table style=\"width:100%;height:400px\" border=\"solid black 1px\" >");
        measureBuilder.append("<tr><td class=\"draggedTable1\" width=\"50%\" valign=\"top\" >");
        measureBuilder.append("<div style=\"height:20px\" class=\"ui-state-default draggedDivs ui-corner-all\">");
        measureBuilder.append("<font size=\"2\" style=\"font-weight:bold\">Drag The Measures</font></div><div class=\"masterDiv\" style=\"height:350px;overflow-y:auto\">");
        measureBuilder.append("<ul id=\"kpiTree\" class=\"filetree treeview-famfamfam\">");
        //measureBuilder.append("<ul id=\"measure\"");
        // measureBuilder.append(templateDAO.getMeasures(bsrSelectedID,parameters,contextPath));
        //  measureBuilder.append("</ul>");
        measureBuilder.append(getTargetMeasures(bsrSelectedID, parameters, "1", contextPath, false));
        measureBuilder.append("</ul></div></td>");
        measureBuilder.append("<td id=\"dropTabs\" class=\"ui-droppable\" width=\"50%\" valign=\"top\">");
        //  measureBuilder.append("<table width=\"100%\">");
        //  measureBuilder.append("<tr style=\"height:50%\"><td id=\"dropTabs\">");
        measureBuilder.append("<div style=\"height:20px\" class=\"ui-state-default draggedDivs ui-corner-all\"><font size=\"2\" style=\"font-weight:bold\">Drop The Measures</font></div>");
        measureBuilder.append("<div class=\"masterDiv1\" style=\"height:350px;overflow-y:auto\">");
        measureBuilder.append("<ul id=\"sortable\" >");
        measureBuilder.append("</ul></div></td>");
        measureBuilder.append("</tr>");
        // measureBuilder.append("<tr style=\"height:50%\"><td id=\"\">");
        //   measureBuilder.append("<div style=\"height:20px\" class=\"ui-state-default draggedDivs ui-corner-all\">");
        //  measureBuilder.append("<font size=\"2\" style=\"font-weight:bold\">Relative Measures</font></div>");
        //  measureBuilder.append("<div class=\"masterDiv1\" style=\"height:175px;overflow-y:auto\">");
        //  measureBuilder.append("<ul id=\"sortable\" >");
        //  measureBuilder.append("</ul>");
        //  measureBuilder.append("</div>");
        // measureBuilder.append("</td></tr></table>");
        //   measureBuilder.append("</table>");
        measureBuilder.append("</table>");
        measureBuilder.append("<br>");
        measureBuilder.append("<table><tr width:100%><td align=\"center\"><input id=\"btn\" class=\"navtitle-hover\" type=\"button\" onclick=\"mappedElements()\" style=\"height: 25px; width: auto;\" value=\"Done\"></td></tr></table>");
        return measureBuilder.toString();
    }

    public String getRelatedMappedElements(String eleId, String mappedEleID, String query) {
        int y = 0;
        String z = "";
        try {
            y = execUpdateSQL(query);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (y > 0) {
            z = "Mapped Elements Sucessfully";
        } else {
        }
        return z;
    }

    public String getUsersForRoles(String grpID, String folderID) {
        PbReturnObject usrsObj = null;
        StringBuilder usersStrBuilder = new StringBuilder();
        //changed by Nazneen
//                String getUsersQuery="select pu_id,pu_firstname from prg_ar_users where pu_id in(select user_id from prg_grp_user_folder_assignment where grp_id="+grpID+")";
        String getUsersQuery = "select pu_id,PU_LOGIN_ID from prg_ar_users where pu_id in(select user_id from prg_grp_user_folder_assignment where grp_id=" + grpID + ")";
        try {
            usrsObj = execSelectSQL(getUsersQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        usersStrBuilder.append("<form name='myForm'  method='post'>");
        usersStrBuilder.append("<table align='center' id='tablesorterUserList' class='tablesorter'  cellpadding='0' cellspacing=1'>");
        usersStrBuilder.append("<thead><tr><th>&nbsp;</th><th>USERS</th></tr></thead>");
        usersStrBuilder.append("<tbody>");
        // int x=usrsObj.getRowCount();
        for (int i = 0; i < usrsObj.getRowCount(); i++) {
            usersStrBuilder.append("<tr>");
            usersStrBuilder.append("<td style='width:5%'><input type='checkbox' name='chkusers' id='chkusers' value=" + usrsObj.getFieldValueString(i, 0) + ">&nbsp;&nbsp;</td><td style='width:20%'>" + usrsObj.getFieldValueString(i, 1) + "</td>");
            usersStrBuilder.append("</td></tr>");
        }
        usersStrBuilder.append("</tbody>");
        usersStrBuilder.append("</table>");
        usersStrBuilder.append("<center>");
        usersStrBuilder.append("<input type='button' class='navtitle-hover' id='RAssignment' name='Assignment' value ='Assignment' onclick='Assign(" + folderID + ")'/>");
        usersStrBuilder.append("</center>");
        usersStrBuilder.append("</form>");
        return usersStrBuilder.toString();
    }

    public HashMap getFavReps(String userId) {
        ArrayList<String> favrepDbrdsNames = new ArrayList<String>();
        ArrayList<String> favrepDbrdsIDs = new ArrayList<String>();
        HashMap favrepDb = new HashMap();
        PbReturnObject favrepanddbdobj = null;
        String qry = getResourceBundle().getString("getFavReport");
        Object[] obj = new Object[2];
        obj[0] = userId;
        String finalFavReportQry = buildQuery(qry, obj);
        try {
            favrepanddbdobj = execSelectSQL(finalFavReportQry);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        for (int i = 0; i < favrepanddbdobj.getRowCount(); i++) {
            favrepDbrdsNames.add(favrepanddbdobj.getFieldValueString(i, 1));
            favrepDbrdsIDs.add(favrepanddbdobj.getFieldValueString(i, 0));

        }
        favrepDb.put("reportIds", favrepDbrdsIDs);
        favrepDb.put("reportNames", favrepDbrdsNames);
        return favrepDb;
    }
    //added by Nazneen

    public HashMap getFavRepsForAll(String userId) {
        ArrayList<String> favrepDbrdsNames = new ArrayList<String>();
        ArrayList<String> favrepDbrdsIDs = new ArrayList<String>();
        HashMap favrepDb = new HashMap();
        PbReturnObject favrepanddbdobj = null;
        String qry = getResourceBundle().getString("getFavReportForAll");
        String[] userIds = userId.split(",");
        int len = userIds.length;
        Object[] obj = new Object[3];
        obj[0] = userId;
        obj[1] = userId;
        obj[2] = len;
        String finalFavReportQry = buildQuery(qry, obj);
        try {
            favrepanddbdobj = execSelectSQL(finalFavReportQry);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        for (int i = 0; i < favrepanddbdobj.getRowCount(); i++) {
            favrepDbrdsNames.add(favrepanddbdobj.getFieldValueString(i, 1));
            favrepDbrdsIDs.add(favrepanddbdobj.getFieldValueString(i, 0));

        }
        favrepDb.put("reportIds", favrepDbrdsIDs);
        favrepDb.put("reportNames", favrepDbrdsNames);
        return favrepDb;
    }

    public HashMap getRepAndDbrdsForUser(String grpID) {
        ArrayList<String> repDbrdsNames = new ArrayList<String>();
        ArrayList<String> repDbrdsIDs = new ArrayList<String>();
        HashMap repDb = new HashMap();
        PbReturnObject repanddbdobj = null;
        String getRepQuery = "select report_id,report_name,report_type from prg_ar_report_master where  report_id in(select report_id from prg_ar_report_details where folder_id=" + grpID + ")";

        try {
            repanddbdobj = execSelectSQL(getRepQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        for (int i = 0; i < repanddbdobj.getRowCount(); i++) {
            repDbrdsIDs.add(repanddbdobj.getFieldValueString(i, 0));
            repDbrdsNames.add(repanddbdobj.getFieldValueString(i, 1));

        }
        repDb.put("reportIds", repDbrdsIDs);
        repDb.put("reportNames", repDbrdsNames);
        return repDb;

    }

    public boolean saveUsersForReports(String userIds, String reportIds) {
        String updateQry1 = getResourceBundle().getString("saveFavReport1");
        String updateQry2 = getResourceBundle().getString("saveFavReport2");
        boolean status = false;
        ArrayList X = new ArrayList();
        ArrayList Y = new ArrayList();
        String[] Z = userIds.split(",");
        // String[] ZB=reportIds.split(",");
        for (int i = 0; i < Z.length; i++) {
            Object obj[] = new Object[2];
            obj[0] = reportIds;
            obj[1] = Z[i];
            String fininserQ = buildQuery(updateQry1, obj);
            X.add(fininserQ);
            Object obj1[] = new Object[2];
            obj1[0] = reportIds;
            obj1[1] = Z[i];
            String fininserQ1 = buildQuery(updateQry2, obj1);
            X.add(fininserQ1);
        }
        try {
            status = executeMultiple(X);
        } catch (Exception n) {
            logger.error("Exception:", n);
        }
        return status;
    }

    public boolean saveFavLinks(ArrayList a) {
        boolean status = false;
        try {
            status = executeMultiple(a);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return status;
    }
    //added by Nazneen

    public boolean saveAllUsersForReports(String userIds, String reportIds) {
        String updateQry1 = getResourceBundle().getString("saveFavReportForAllUsers1");
        String updateQry2 = getResourceBundle().getString("saveFavReportForAllUsers2");
        boolean status = false;
        ArrayList X = new ArrayList();

        PbReturnObject favrepanddbdobj = null;
        String qry = getResourceBundle().getString("getFavReportForAll");
        String[] userId = userIds.split(",");
        StringBuilder reportId = new StringBuilder();
//        String reportId = "";
        int len = userId.length;
        Object[] obj1 = new Object[3];
        obj1[0] = userIds;
        obj1[1] = userIds;
        obj1[2] = len;
        String finalFavReportQry = buildQuery(qry, obj1);
        try {
            favrepanddbdobj = execSelectSQL(finalFavReportQry);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (favrepanddbdobj != null && favrepanddbdobj.getRowCount() > 0) {
            for (int i = 0; i < favrepanddbdobj.getRowCount(); i++) {
//                reportId = reportId + "," + favrepanddbdobj.getFieldValueString(i, 0);
                reportId.append(",").append(favrepanddbdobj.getFieldValueString(i, 0));
            }
            reportId = new StringBuilder(reportId.substring(1));

            Object obj[] = new Object[2];
            obj[0] = reportId;
            obj[1] = userIds;

            String fininserQ = buildQuery(updateQry1, obj);
            X.add(fininserQ);
        }
        Object obj2[] = new Object[2];
        obj2[0] = reportIds;
        obj2[1] = userIds;
        String fininserQ1 = buildQuery(updateQry2, obj2);
        X.add(fininserQ1);

        try {
            status = executeMultiple(X);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return status;
    }

    public String getOldRelatedMeasures(String busRoleId, HttpServletRequest request) {
        String sqlQuery = getResourceBundle().getString("getCreateKPIData");
        PbReturnObject retObj = new PbReturnObject();
        StringBuilder outerBuffer = new StringBuilder();
        HashMap hlist = new HashMap();
        String createKPIString = "";
        String jsonString = null;
        Gson gson = new Gson();
        CreateKPIFromReport kPIFromReport = new CreateKPIFromReport();
        try {
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (retObj.getRowCount() > 0) {
            //outerBuffer.append("<table>");
            for (int i = 0; i < retObj.getRowCount(); i++) {
                createKPIString = retObj.getFieldValueClobString(i, "CREATE_KPI_DATA");
                kPIFromReport = gson.fromJson(createKPIString, CreateKPIFromReport.class);
                if (kPIFromReport.getBizRoles() != null) {
                    createKPIString = kPIFromReport.getBizRoles()[0];
                    if (busRoleId != null && createKPIString != null && createKPIString.equalsIgnoreCase(busRoleId)) {
//                        outerBuffer.append("<tr id='"+retObj.getFieldValueString(i, "CREATE_KPI_NAME")+"'><td id='"+retObj.getFieldValueString(i, "CREATE_KPI_ID")+"'>");
//                        outerBuffer.append("<img align=\"middle\" SRC=\"" + request.getContextPath() + "/icons pinvoke/cross-small.png\" onclick=\"deleteColumn1('"+retObj.getFieldValueString(i, "CREATE_KPI_ID")+"','"+retObj.getFieldValueString(i, "CREATE_KPI_NAME")+"')\" BORDER=\"0\" title=\"Delete Related Measure\" />");
//                        outerBuffer.append(""+retObj.getFieldValueString(i, "CREATE_KPI_NAME")+"</td></tr>");
                        hlist.put(retObj.getFieldValueString(i, "CREATE_KPI_ID"), retObj.getFieldValueString(i, "CREATE_KPI_NAME"));
                    }
                }
            }
            jsonString = gson.toJson(hlist);
            //outerBuffer.append("</table>");
        }
        return jsonString;
    }

    public boolean deleteRelateMeasures(String idsNew) {
        String qry = "delete from PRG_AR_CREATE_KPI_FROM_REPORT where CREATE_KPI_ID in(" + idsNew + ")";
        boolean result = false;
        try {
            execModifySQL(qry);
            result = true;
        } catch (Exception ex) {
            result = false;
            logger.error("Exception:", ex);
        }
        return result;
    }

    public void insertTargetMeasures(String bussRoleId, String userId, String periodTypeId, String elementID, String elementName, String startValue, String endValue) {
        String query = "";
        PbReturnObject retObj = null;
        String selectStame = "SELECT * FROM PRG_AR_TARGET_MEASURES WHERE ELEMENT_ID='" + elementID + "'";
        try {
            retObj = super.execSelectSQL(selectStame);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (retObj.rowCount == 0) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                query = "insert into PRG_AR_TARGET_MEASURES(USER_ID,BUSS_ROLE,ELEMENT_ID,ELEMENT_NAME,PERIOD_TYPE,START_VALUE,END_VALUE) values('" + userId + "','" + bussRoleId + "','" + elementID + "','" + elementName + "','" + periodTypeId + "','" + startValue + "','" + endValue + "')";
            } else {
                query = "insert into PRG_AR_TARGET_MEASURES(TARGET_ID,USER_ID,BUSS_ROLE,ELEMENT_ID,ELEMENT_NAME,PERIOD_TYPE,START_VALUE,END_VALUE) values(PRR_AR_TARGET_MEAS_SEQ.NEXTVAL,'" + userId + "','" + bussRoleId + "','" + elementID + "','" + elementName + "','" + periodTypeId + "','" + startValue + "','" + endValue + "')";
            }
            try {
                super.execUpdateSQL(query);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        } else {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                query = "UPDATE  PRG_AR_TARGET_MEASURES SET PERIOD_TYPE='" + periodTypeId + "',START_VALUE='" + startValue + "',END_VALUE='" + endValue + "' WHERE ELEMENT_ID='" + elementID + "'";
            } else {
                query = "UPDATE  PRG_AR_TARGET_MEASURES SET PERIOD_TYPE='" + periodTypeId + "',START_VALUE='" + startValue + "',END_VALUE='" + endValue + "' WHERE ELEMENT_ID='" + elementID + "'";
            }
            try {
                super.execUpdateSQL(query);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public HashMap<String, List<String>> getTargetMeasuresDetails(String bussRoleId, String userId) {
        List<String> elementIds = new ArrayList<String>();
        List<String> elementNames = new ArrayList<String>();
        HashMap<String, List<String>> targetMeasure = new HashMap<String, List<String>>();
        PbReturnObject retOjb = null;
        String finalQuery = "SELECT TARGET_ID,ELEMENT_ID,ELEMENT_NAME FROM PRG_AR_TARGET_MEASURES WHERE USER_ID='" + userId + "' and BUSS_ROLE='" + bussRoleId + "' ORDER BY TARGET_ID ASC";
        try {
            retOjb = super.execSelectSQL(finalQuery);
            if (retOjb.rowCount != 0) {
                for (int i = 0; i < retOjb.rowCount; i++) {
                    elementIds.add(retOjb.getFieldValueString(i, "ELEMENT_ID"));
                    elementNames.add(retOjb.getFieldValueString(i, "ELEMENT_NAME"));
                }
                targetMeasure.put("ELEMENTIDS", elementIds);
                targetMeasure.put("ELEMENTNAMES", elementNames);
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return targetMeasure;
    }

    public List<String> testTargetsExistorNot(String bussRoleId, String userId, String elementID, String selectYear) throws Exception {
        List<String> targetPeriodDetails = new ArrayList<String>();
        PbReturnObject retOjb = null;
        if (selectYear != null && !selectYear.equalsIgnoreCase("")) {
            String finalQuery = "UPDATE PRG_AR_TARGET_MEASURES SET COMPARE_YEAR = '" + selectYear + "' WHERE USER_ID='" + userId + "' and BUSS_ROLE='" + bussRoleId + "' and ELEMENT_ID='" + elementID + "'";
            try {
                execUpdateSQL(finalQuery);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        String finalQuery = "SELECT PERIOD_TYPE,START_VALUE,END_VALUE,COMPARE_YEAR FROM PRG_AR_TARGET_MEASURES WHERE USER_ID='" + userId + "' and BUSS_ROLE='" + bussRoleId + "' and ELEMENT_ID='" + elementID + "'";
        try {
            retOjb = super.execSelectSQL(finalQuery);
            if (retOjb.rowCount != 0) {
                targetPeriodDetails.add(retOjb.getFieldValueString(0, "PERIOD_TYPE"));
                targetPeriodDetails.add(retOjb.getFieldValueString(0, "START_VALUE"));
                targetPeriodDetails.add(retOjb.getFieldValueString(0, "END_VALUE"));
                targetPeriodDetails.add(retOjb.getFieldValueString(0, "COMPARE_YEAR"));
                // targetPeriodDetails.add(retOjb.getFieldValueString(0, "ROLLING_TYPE"));
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return targetPeriodDetails;
    }

    public List<String> getTargetPeriodDetails(String startValue, String endValue, String elementID, String periodtype) {
        Connection con = null;
        List<String> targetPeriodDetails = new ArrayList<String>();
        PbReturnObject retOjb = null;
        String finalQuery = "";
        if (periodtype.equalsIgnoreCase("Month")) {
            finalQuery = "select distinct cm_cust_name,cm_st_date from pr_day_denom where cm_st_date between '" + startValue + "' and '" + endValue + "' order by cm_st_date asc";
        } else if (periodtype.equalsIgnoreCase("Quarter")) {
            finalQuery = "select distinct cq_cust_name ,CQ_ST_DATE from pr_day_denom  where CQ_ST_DATE between '" + startValue + "' and '" + endValue + "' order by CQ_ST_DATE asc";
        } else if (periodtype.equalsIgnoreCase("Year")) {
            finalQuery = "select distinct cy_cust_name from pr_day_denom   where DDATE between '" + startValue + "' and '" + endValue + "' order by cy_cust_name asc";
        }
        try {
            con = ProgenConnection.getInstance().getConnectionForElement(elementID);
            if (con != null) {
                retOjb = execSelectSQL(finalQuery, con);
                if (retOjb != null && retOjb.rowCount > 0) {
                    for (int i = 0; i < retOjb.rowCount; i++) {
                        targetPeriodDetails.add(retOjb.getFieldValueString(i, 0));
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return targetPeriodDetails;
    }

    public List<String> getTargetValues(String startValue, String endValue, String elementID, String periodtype) {
        List<String> targetPeriodvalues = new ArrayList<String>();
        PbReturnObject retOjb = null;
        String finalQuery = "SELECT TARGET_ID,TARGET_VALUE FROM PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='" + elementID + "' ORDER BY TARGET_ID ASC";
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(elementID);
        try {
            retOjb = execSelectSQL(finalQuery, con);
            if (retOjb != null && retOjb.rowCount > 0) {
                for (int i = 0; i < retOjb.rowCount; i++) {
                    targetPeriodvalues.add(retOjb.getFieldValueString(i, 1));
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return targetPeriodvalues;
    }

    public List<String> getPublishType(String startValue, String endValue, String elementID, String periodtype) {
        List<String> targetPeriodvalues = new ArrayList<String>();
        PbReturnObject retOjb = null;
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(elementID);
        String finalQuery = "SELECT TARGET_ID,PUBLISH_TYPE FROM PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='" + elementID + "' ORDER BY TARGET_ID ASC";

        try {
            retOjb = execSelectSQL(finalQuery, con);
            if (retOjb != null && retOjb.rowCount > 0) {
                for (int i = 0; i < retOjb.rowCount; i++) {
                    targetPeriodvalues.add(retOjb.getFieldValueString(i, 1));
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return targetPeriodvalues;
    }

    public void insertTargetMeasureValues(String elementId, String elemtName, String userId, String bussroleID, String StartVal, String endVal, String periodType, List<String> targetValuesList, List<String> cusomNames) {
        String insertTargetMeasures = getResourceBundle().getString("insertTargetMeasures");
        String getusernameQuery = "SELECT BUSS_TABLE_ID FROM prg_user_all_info_details where ELEMENT_ID=" + elementId + "";
        PbReturnObject retobj = null;
        Connection con = null;

        try {
            retobj = execSelectSQL(getusernameQuery);
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        String elementTableID = "";
        if (retobj.rowCount != 0) {
            elementTableID = retobj.getFieldValueString(0, 0);
        }
        String insertQuery = "";
        Object[] targetMeasures = null;
        boolean testForMeasure = false;
        ArrayList querys = new ArrayList();
        //String exstingQuery = "SELECT * FROM  PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='"+elementId+"' AND PERIOD_TYPE='"+periodType+"' AND START_DATE='"+StartVal+"' AND END_DATE='"+endVal+"'";
        String exstingQuery = "SELECT TARGET_ID FROM  PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='" + elementId + "' order by TARGET_ID ASC";
        String updateQuery = "UPDATE PRG_AR_TARGET_MEASURES_DETAILS SET TARGET_VALUE=& ,TARGET_DURATION='&' ,START_DATE='&' , END_DATE='&' ,CUSTOM_NAME='&',START_VALUE='&',END_VALUE='&' WHERE  TARGET_ID=&";
        List<String> targetIds = new ArrayList<String>();
        try {
            con = ProgenConnection.getInstance().getConnectionForElement(elementId);
            retobj = execSelectSQL(exstingQuery, con);
            if (retobj.rowCount > 0) {
                for (int i = 0; i < retobj.rowCount; i++) {
                    targetIds.add(retobj.getFieldValueString(i, 0));
                }
                testForMeasure = true;
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        List<String> startDate = new ArrayList<String>();
        List<String> endDate = new ArrayList<String>();
        try {
            for (int i = 0; i < cusomNames.size(); i++) {
                PbReturnObject ret = new PbReturnObject();
                if (periodType.equalsIgnoreCase("Month")) {
                    con = ProgenConnection.getInstance().getConnectionForElement(elementId);
                    ret = execSelectSQL("select min(ddate),max(ddate) from pr_day_denom where CM_CUST_NAME='" + cusomNames.get(i) + "'", con);
                }
                if (periodType.equalsIgnoreCase("Quarter")) {
                    con = ProgenConnection.getInstance().getConnectionForElement(elementId);
                    ret = execSelectSQL("select min(ddate),max(ddate) from pr_day_denom where CQ_CUST_NAME='" + cusomNames.get(i) + "'", con);
                }
                if (periodType.equalsIgnoreCase("Year")) {
                    con = ProgenConnection.getInstance().getConnectionForElement(elementId);
                    ret = execSelectSQL("select min(ddate),max(ddate) from pr_day_denom where CY_CUST_NAME='" + cusomNames.get(i) + "'", con);
                }
                startDate.add(ret.getFieldValueDateString(0, 0));
                endDate.add(ret.getFieldValueDateString(0, 1));
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        try {
            con = ProgenConnection.getInstance().getConnectionForElement(elementId);
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                if (testForMeasure) {
                    targetMeasures = new Object[8];
                    for (int i = 0; i < targetIds.size(); i++) {
                        // con = ProgenConnection.getInstance().getConnectionForElement(elementId);
                        targetMeasures[0] = targetValuesList.get(i);
                        targetMeasures[1] = periodType;
                        targetMeasures[2] = startDate.get(i);
                        targetMeasures[3] = endDate.get(i);
                        targetMeasures[4] = cusomNames.get(i);
                        targetMeasures[5] = StartVal;
                        targetMeasures[6] = endVal;
                        targetMeasures[7] = targetIds.get(i);
                        insertQuery = buildQuery(updateQuery, targetMeasures);
                        //execUpdateSQL(insertQuery,con);
                        querys.add(insertQuery);
                    }
                    executeMultiple(querys, con);
                } else {
                    targetMeasures = new Object[13];
                    for (int i = 0; i < cusomNames.size(); i++) {
                        // con = ProgenConnection.getInstance().getConnectionForElement(elementId);
                        targetMeasures[0] = "@@TIME@@";
                        targetMeasures[1] = bussroleID;
                        targetMeasures[2] = userId;
                        targetMeasures[3] = elementId;
                        targetMeasures[4] = elementTableID;
                        targetMeasures[5] = targetValuesList.get(i);
                        targetMeasures[6] = periodType;
                        targetMeasures[7] = startDate.get(i);
                        targetMeasures[8] = endDate.get(i);
                        targetMeasures[9] = cusomNames.get(i);
                        targetMeasures[10] = StartVal;
                        targetMeasures[11] = endVal;
                        targetMeasures[12] = elemtName;

                        insertQuery = buildQuery(insertTargetMeasures, targetMeasures);
                        //execUpdateSQL(insertQuery,con);
                        querys.add(insertQuery);
                    }
                    executeMultiple(querys, con);
                }
            } else {
                if (testForMeasure) {
                    targetMeasures = new Object[8];
                    for (int i = 0; i < cusomNames.size(); i++) {
                        //con = ProgenConnection.getInstance().getConnectionForElement(elementId);
                        targetMeasures[0] = targetValuesList.get(i);
                        targetMeasures[1] = periodType;
                        targetMeasures[2] = startDate.get(i);
                        targetMeasures[3] = endDate.get(i);
                        targetMeasures[4] = cusomNames.get(i);
                        targetMeasures[5] = StartVal;
                        targetMeasures[6] = endVal;
                        targetMeasures[7] = targetIds.get(i);
                        insertQuery = buildQuery(updateQuery, targetMeasures);
                        //execUpdateSQL(insertQuery,con);
                        querys.add(insertQuery);
                    }
                    executeMultiple(querys, con);
                } else {
                    targetMeasures = new Object[13];
                    for (int i = 0; i < cusomNames.size(); i++) {
                        //con = ProgenConnection.getInstance().getConnectionForElement(elementId);
                        targetMeasures[0] = "@@TIME@@";
                        targetMeasures[1] = bussroleID;
                        targetMeasures[2] = userId;
                        targetMeasures[3] = elementId;
                        targetMeasures[4] = elementTableID;
                        targetMeasures[5] = targetValuesList.get(i);
                        targetMeasures[6] = periodType;
                        targetMeasures[7] = startDate.get(i);
                        targetMeasures[8] = endDate.get(i);
                        targetMeasures[9] = cusomNames.get(i);
                        targetMeasures[10] = StartVal;
                        targetMeasures[11] = endVal;
                        targetMeasures[12] = elemtName;

                        insertQuery = buildQuery(insertTargetMeasures, targetMeasures);
                        // execUpdateSQL(insertQuery,con);
                        querys.add(insertQuery);
                    }
                    executeMultiple(querys, con);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public HashMap<String, List<String>> testForTargetValuesExits(String elementID, String periodtype, String startValue, String endValue) {
        List<String> targetPeriodDetails = new ArrayList<String>();
        List<String> customNames = new ArrayList<String>();
        HashMap<String, List<String>> targetMeasure = new HashMap<String, List<String>>();
        PbReturnObject retOjb = null;
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(elementID);
        String finalQuery = "SELECT * FROM PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='" + elementID + "' ORDER BY TARGET_ID ASC";
        try {
            retOjb = super.execSelectSQL(finalQuery, con);
            if (retOjb.rowCount > 0) {
                if (retOjb.getFieldValueString(0, "TARGET_DURATION").equalsIgnoreCase(periodtype) && retOjb.getFieldValueString(0, "START_VALUE").equalsIgnoreCase(startValue) && retOjb.getFieldValueString(0, "END_VALUE").equalsIgnoreCase(endValue)) {
                    for (int i = 0; i < retOjb.rowCount; i++) {
                        targetPeriodDetails.add(retOjb.getFieldValueString(i, "TARGET_VALUE"));
                        customNames.add(retOjb.getFieldValueString(i, "CUSTOM_NAME"));
                    }
                    targetMeasure.put("TARGETVALUES", targetPeriodDetails);
                    targetMeasure.put("CUSTOMNAMES", customNames);
                } else {
                    String deleteQuery = "DELETE  FROM PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='" + elementID + "'";
                    con = ProgenConnection.getInstance().getConnectionForElement(elementID);
                    super.execUpdateSQL(deleteQuery, con);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return targetMeasure;
    }

    public ArrayList getMeasureAggration(String eleId) {
        ArrayList aggrigation = new ArrayList();
        String finalQuer = "select AGGREGATION_TYPE from prg_user_all_info_details where element_id = " + eleId + "";
        PbReturnObject retOjb = null;
        try {
            retOjb = super.execSelectSQL(finalQuer);
            if (retOjb.rowCount > 0) {
//                for(int i=0;i<retOjb.rowCount;i++){
                aggrigation.add(retOjb.getFieldValueString(0, 0));
//                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return aggrigation;
    }

    public ArrayList getAllMeasuresViewbys(String bussId) {
        ArrayList aggrigation = new ArrayList();
        // String finalQuer = "SELECT DISTINCT USER_COL_DESC FROM PRG_USER_ALL_INFO_DETAILS WHERE FOLDER_ID="+bussId+" AND SUB_FOLDER_TYPE IN('Dimensions') AND USER_COL_DESC is not null";
        String finalQuer1 = "SELECT DISTINCT pusft.dim_name,MIN(pufd.sub_folder_id) over(partition BY pusft.dim_id) subFolder_id FROM Prg_User_Sub_Folder_Tables Pusft, Prg_User_Folder_Detail Pufd WHERE Pusft.Sub_Folder_Id =Pufd.Sub_Folder_Id AND Pufd.Sub_Folder_Type='Dimensions' AND pufd.folder_id IN (" + bussId + ") AND pusft.dim_name!='Time' ORDER BY DIM_NAME ASC";
        PbReturnObject retOjb = null;
        try {
            retOjb = super.execSelectSQL(finalQuer1);
            if (retOjb.rowCount > 0) {
                String finalQuer = "SELECT DISTINCT K.VAL_ELEMENT_ID, K.VAL_DISP_NAME,m.info_member_id, k.level1 FROM PRG_USER_ALL_DDIM_KEY_VAL_ELE K, Prg_User_All_Ddim_Master M Where K.Val_Sub_Folder_Id='" + retOjb.getFieldValueString(0, "SUBFOLDER_ID") + "' AND m.info_dim_id =K.KEY_DIM_ID AND K.VAL_DISP_NAME =m.info_member_name Order By 2";
                retOjb = super.execSelectSQL(finalQuer);
                if (retOjb.rowCount > 0) {
                    for (int i = 0; i < retOjb.rowCount; i++) {
                        if (!aggrigation.contains(retOjb.getFieldValueString(i, 1))) {
                            aggrigation.add(retOjb.getFieldValueString(i, 1));
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return aggrigation;
    }

    public ArrayList getViewbyValues(String bussId, String elemId) {
        ArrayList aggrigation = new ArrayList();
        String[] viewByvalues = null;
        PbReturnObject retobj = null;
        String exstingQuery = "SELECT TARGET_ID,VIEWBY_NAMES FROM  PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='" + elemId + "' order by TARGET_ID ASC";
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(elemId);
        try {
            retobj = execSelectSQL(exstingQuery, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (retobj.rowCount > 0) {
            if (!retobj.getFieldValueString(0, 1).equalsIgnoreCase("")) {
                viewByvalues = retobj.getFieldValueString(0, 1).split(",");
                for (int i = 0; i < viewByvalues.length; i++) {
                    aggrigation.add(viewByvalues[i]);
                }
            }

        }
        return aggrigation;
    }

    public void insertExcelFile(String bussRoleId, String userId, String elementID, String fileName) throws Exception {
        if (fileName != null && !fileName.equalsIgnoreCase("")) {
            String finalQuery = "UPDATE PRG_AR_TARGET_MEASURES SET EXCEL_FILE_NAME = '" + fileName + "' WHERE USER_ID='" + userId + "' and BUSS_ROLE='" + bussRoleId + "' and ELEMENT_ID='" + elementID + "'";
            try {
                execUpdateSQL(finalQuery);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public boolean getFileExistingOrNot(String bussId, String eleId) {
        boolean testforFile = false;
        PbReturnObject retOjb = null;
        String finalvalue = "SELECT EXCEL_FILE_NAME FROM PRG_AR_TARGET_MEASURES WHERE  BUSS_ROLE='" + bussId + "' and ELEMENT_ID='" + eleId + "'";
        try {
            retOjb = execSelectSQL(finalvalue);
            if (retOjb.rowCount > 0 && !retOjb.getFieldValueString(0, 0).equalsIgnoreCase("")) {
                testforFile = true;
            }
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        return testforFile;
    }

    public String getUiData(List<String> elementIds, List<String> elementNames, HttpServletRequest request) {
        StringBuilder uidesing = new StringBuilder();
        for (int i = 0; i < elementIds.size(); i++) {
            uidesing.append("<li class='closed' >");
            //uidesing.append("<img src='"+request.getContextPath()+"/icons pinvoke/document-attribute-f.png'></img>");
            uidesing.append("<span class=\"2_span\" id='" + elementIds.get(i) + "~" + elementNames.get(i) + " '  title='TargetMeasure' style='font-family:verdana;'>" + elementNames.get(i) + "</span>");
            uidesing.append("<ul>");
            uidesing.append(getTargetFieldValue(elementIds.get(i), request, elementNames.get(i)));
            uidesing.append("</ul>");
            uidesing.append("</li>");
        }
        return uidesing.toString();
    }

    public String getTargetFieldValue(String elementIds, HttpServletRequest request, String eleName) {
        StringBuilder fieldValues = new StringBuilder();
        PbReturnObject retobj = null;
        String finalQuery = "SELECT  TARGET_ID,CUSTOM_NAME from PRG_AR_TARGET_MEASURES_DETAILS where ELEMENT_ID='" + elementIds + "' order by TARGET_ID ASC";
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(elementIds);
        try {
            retobj = execSelectSQL(finalQuery, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        if (retobj.rowCount > 0) {
            for (int i = 0; i < retobj.rowCount; i++) {
                fieldValues.append("<li class='closed' >");
                //fieldValues.append("<img src='"+request.getContextPath()+"/icons pinvoke/document-attribute-f.png'></img>");
                fieldValues.append("<span class=\"3_span\" id='" + elementIds + "@" + retobj.getFieldValueString(i, 1) + "'  title='TargetMeasure' style='font-family:verdana;'>" + retobj.getFieldValueString(i, 1) + "</span>");
                fieldValues.append("<ul>");
                fieldValues.append(getViewByNames(elementIds, request, eleName, retobj.getFieldValueString(i, 1)));
                fieldValues.append("</ul>");
                fieldValues.append("</li>");
            }
        }
        return fieldValues.toString();
    }

    public String getViewByNames(String elementIds, HttpServletRequest request, String eleName, String timeType) {
        StringBuilder fieldValues = new StringBuilder();
        String[] viewByvalues = null;
        PbReturnObject retobj = null;
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(elementIds);
        String exstingQuery = "SELECT TARGET_ID,VIEWBY_NAMES FROM  PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='" + elementIds + "' order by TARGET_ID ASC";
        try {
            retobj = execSelectSQL(exstingQuery, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        if (retobj.rowCount > 0) {
            if (!retobj.getFieldValueString(0, 1).equalsIgnoreCase("")) {
                viewByvalues = retobj.getFieldValueString(0, 1).split(",");
                for (int i = 0; i < viewByvalues.length; i++) {
                    fieldValues.append("<li class='closed' >");
                    // fieldValues.append("<img src='"+request.getContextPath()+"/icons pinvoke/document-attribute-f.png'></img>");
                    fieldValues.append("<span class=\"4_span\" id='" + elementIds + "*" + viewByvalues[i] + "*" + eleName + "*" + timeType + "'  title='TargetMeasure' style='font-family:verdana;'>" + viewByvalues[i] + "</span>");
                    //fieldValues.append("<ul>");
                    //fieldValues.append(getViewByNames(elementIds));
                    //fieldValues.append("</ul>");
                    fieldValues.append("</li>");
                }
            }
        }
        return fieldValues.toString();
    }

    public void updateTargetParameters(String bussId, String elemtId, List<String> viewbys) {
        String exstingQuery = "SELECT TARGET_ID FROM  PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='" + elemtId + "' order by TARGET_ID ASC";
        PbReturnObject retobj = null;
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(elemtId);
        try {
            retobj = execSelectSQL(exstingQuery, con);
            if (retobj.rowCount > 0) {
                for (int i = 0; i < retobj.rowCount; i++) {
                    String updateString = "UPDATE PRG_AR_TARGET_MEASURES_DETAILS SET VIEWBY_NAMES = '" + Joiner.on(",").join(viewbys) + "' WHERE TARGET_ID='" + retobj.getFieldValueString(i, 0) + "'";
                    con = ProgenConnection.getInstance().getConnectionForElement(elemtId);
                    execUpdateSQL(updateString, con);
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public int getPrevieousYears(String elementID) {
        int val = 0;
        PbReturnObject retOjb = null;
        String finalValue = "SELECT COMPARE_YEAR FROM PRG_AR_TARGET_MEASURES WHERE ELEMENT_Id='" + elementID + "'";
        try {
            retOjb = execSelectSQL(finalValue);
            if (retOjb.rowCount > 0) {
                val = Integer.parseInt(retOjb.getFieldValueString(0, 0));
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return val;
    }

    public String getPrevieousRollingType(String elementID) {
        String val = "";
        PbReturnObject retOjb = null;
        String finalValue = "SELECT ROLLING_TYPE FROM PRG_AR_TARGET_MEASURES WHERE ELEMENT_Id='" + elementID + "'";
        try {
            retOjb = execSelectSQL(finalValue);
            if (retOjb.rowCount > 0) {
                val = retOjb.getFieldValueString(0, 0);
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return val;
    }

    public void updateExcelPublishType(String elementId, String userId, String bussroleID, String StartVal, String endVal, String periodType, List<String> targetValuesList, List<String> publishTypeList, List<String> customNames) {
        String exstingQuery = "SELECT TARGET_ID FROM  PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='" + elementId + "' order by TARGET_ID ASC";
        PbReturnObject retobj = null;
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(elementId);
        try {
            retobj = execSelectSQL(exstingQuery, con);
            if (retobj.rowCount > 0) {
                for (int i = 0; i < retobj.rowCount; i++) {
                    String updateString = "UPDATE PRG_AR_TARGET_MEASURES_DETAILS SET TARGET_VALUE = '" + targetValuesList.get(i) + "',PUBLISH_TYPE='" + publishTypeList.get(i).toUpperCase() + "' WHERE TARGET_ID='" + retobj.getFieldValueString(i, 0) + "'";
                    con = ProgenConnection.getInstance().getConnectionForElement(elementId);
                    execUpdateSQL(updateString, con);
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public ArrayList getTargetPeriodDetails(String elementIds) {
        Connection con = null;
        PbReturnObject retobj = null;
        con = ProgenConnection.getInstance().getConnectionForElement(elementIds);
        try {
            retobj = execSelectSQL("select distinct cm_cust_name from pr_day_denom order by cm_cust_name ASC", con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        ArrayList aggrigation = new ArrayList();
        // String finalQuery = "SELECT  TARGET_ID,CUSTOM_NAME from PRG_AR_TARGET_MEASURES_DETAILS where ELEMENT_ID='"+elementIds+"' order by TARGET_ID ASC";
//        try {
//            //retobj = execSelectSQL(finalQuery);
//        } catch (SQLException ex) {
//            logger.error("Exception:",ex);
//        }
        if (retobj.rowCount > 0) {
            for (int i = 0; i < retobj.rowCount; i++) {
                //aggrigation.add(retobj.getFieldValueString(i, 1));
                aggrigation.add(retobj.getFieldValueString(i, 0));
            }
        }
        return aggrigation;
    }

    public ArrayList getEndDateValue(String eleId, String monthName, String bussId, String regionName) {
        String finalQuery = "SELECT END_DATE FROM PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='" + eleId + "' AND CUSTOM_NAME='" + monthName + "'";
        PbReturnObject retOjb = null;
        String dateVal = "";
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(eleId);
        if (con != null) {
            try {
                retOjb = execSelectSQL(finalQuery, con);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            if (retOjb.rowCount > 0) {
                dateVal = retOjb.getFieldValueString(0, 0);
            }
        }

        ArrayList aggrigation = new ArrayList();
        // String finalQuer = "SELECT DISTINCT USER_COL_DESC FROM PRG_USER_ALL_INFO_DETAILS WHERE FOLDER_ID="+bussId+" AND SUB_FOLDER_TYPE IN('Dimensions') AND USER_COL_DESC is not null";
        String finalQuer1 = "SELECT DISTINCT pusft.dim_name,MIN(pufd.sub_folder_id) over(partition BY pusft.dim_id) subFolder_id FROM Prg_User_Sub_Folder_Tables Pusft, Prg_User_Folder_Detail Pufd WHERE Pusft.Sub_Folder_Id =Pufd.Sub_Folder_Id AND Pufd.Sub_Folder_Type='Dimensions' AND pufd.folder_id IN (" + bussId + ") AND pusft.dim_name!='Time' ORDER BY DIM_NAME ASC";
        try {
            retOjb = super.execSelectSQL(finalQuer1);
            if (retOjb.rowCount > 0) {
                String finalQuer = "SELECT DISTINCT K.VAL_ELEMENT_ID, K.VAL_DISP_NAME,m.info_member_id, k.level1 FROM PRG_USER_ALL_DDIM_KEY_VAL_ELE K, Prg_User_All_Ddim_Master M Where K.Val_Sub_Folder_Id='" + retOjb.getFieldValueString(0, "SUBFOLDER_ID") + "' AND m.info_dim_id =K.KEY_DIM_ID AND K.VAL_DISP_NAME =m.info_member_name AND K.VAL_DISP_NAME = '" + regionName + "' Order By 2";
                retOjb = super.execSelectSQL(finalQuer);
                if (retOjb.rowCount > 0) {
                    for (int i = 0; i < retOjb.rowCount; i++) {
                        if (!aggrigation.contains(retOjb.getFieldValueString(i, 0))) {
                            aggrigation.add(retOjb.getFieldValueString(i, 0));
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        aggrigation.add(dateVal);
        return aggrigation;
    }

    public void updateSupportiveMeasure(String elementId, String supportMeasure) {
        String exstingQuery = "SELECT ELEMENT_ID FROM  PRG_AR_TARGET_MEASURES WHERE ELEMENT_ID='" + elementId + "' ";
        PbReturnObject retobj = null;
        try {
            retobj = execSelectSQL(exstingQuery);
            if (retobj.rowCount > 0) {
                String updateString = "UPDATE PRG_AR_TARGET_MEASURES SET SUPPORT_MEASURES = '" + supportMeasure + "' WHERE ELEMENT_ID='" + elementId + "'";
                execUpdateSQL(updateString);
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public String getSupportiveMeasures(String elementId) {
        String exstingQuery = "SELECT SUPPORT_MEASURES FROM  PRG_AR_TARGET_MEASURES WHERE ELEMENT_ID='" + elementId + "' ";
        PbReturnObject retobj = null;
        String supportiveMeas = "";
        try {
            retobj = execSelectSQL(exstingQuery);
            if (retobj.rowCount > 0 && !retobj.getFieldValueString(0, 0).equalsIgnoreCase("")) {
                supportiveMeas = retobj.getFieldValueString(0, 0);
            }
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        return supportiveMeas;
    }

    public ArrayList getRollingDate(String elementId) {
        String exstingQuery = "SELECT PERIOD_TYPE,END_VALUE FROM  PRG_AR_TARGET_MEASURES WHERE ELEMENT_ID='" + elementId + "' ";
        PbReturnObject retobj = null;
        ArrayList supportiveMeas = new ArrayList();
        try {
            retobj = execSelectSQL(exstingQuery);
            if (retobj.rowCount > 0 && !retobj.getFieldValueString(0, 0).equalsIgnoreCase("") && !retobj.getFieldValueString(0, 1).equalsIgnoreCase("")) {
                supportiveMeas.add(retobj.getFieldValueString(0, 0));
                supportiveMeas.add(retobj.getFieldValueString(0, 1));
            }
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        return supportiveMeas;
    }

    public ArrayList getDiscreteMonths(String eleId, ArrayList viewBys) {

        ArrayList<String> startDate = new ArrayList<String>();
        ArrayList<String> endDate = new ArrayList<String>();
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(eleId);
        if (con != null) {
            for (int i = 0; i < viewBys.size(); i++) {
                PbReturnObject ret = new PbReturnObject();
                con = ProgenConnection.getInstance().getConnectionForElement(eleId);
                try {
                    ret = execSelectSQL("select min(ddate),max(ddate) from pr_day_denom where CM_CUST_NAME='" + viewBys.get(i) + "'", con);
                } catch (Exception ex) {
                    logger.error("Exception:", ex);
                }
                //startDate.add(ret.getFieldValueDateString(0,0));
                endDate.add(ret.getFieldValueDateString(0, 1));
            }
        }
        return endDate;
    }

    public ArrayList getDayLevelDataForAllocation(String eleId, String monthName) {
        ArrayList<String> dayDetails = new ArrayList<String>();
        Connection con = null;
        PbReturnObject ret = null;
        con = ProgenConnection.getInstance().getConnectionForElement(eleId);
        if (con != null) {

            con = ProgenConnection.getInstance().getConnectionForElement(eleId);
            try {
                ret = execSelectSQL("SELECT START_DATE,END_DATE,TARGET_VALUE FROM PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='" + eleId + "' AND CUSTOM_NAME='" + monthName + "'", con);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            //startDate.add(ret.getFieldValueDateString(0,0));
            dayDetails.add(ret.getFieldValueDateString(0, "START_DATE"));
            dayDetails.add(ret.getFieldValueDateString(0, "END_DATE"));
            dayDetails.add(Integer.toString(ret.getFieldValueInt(0, "TARGET_VALUE")));

        }
        return dayDetails;
    }

    public int getMonthWeeks(String eleId, String monthName) {
//        ArrayList<String> dayDetails = new ArrayList<String>();
        Connection con = null;
        PbReturnObject ret = null;
        con = ProgenConnection.getInstance().getConnectionForElement(eleId);
        if (con != null) {
            con = ProgenConnection.getInstance().getConnectionForElement(eleId);
            try {
                ret = execSelectSQL("SELECT DISTINCT CW_ST_MON_DATE,CW_END_MON_DATE FROM PR_DAY_DENOM WHERE CM_CUST_NAME='" + monthName + "' ORDER BY CW_ST_MON_DATE ASC", con);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }
        return ret.rowCount;
    }

    public ArrayList<String> getMonthWeeksValues(String eleId, String monthName) {
        ArrayList<String> dayDetails = new ArrayList<String>();
        ArrayList<String> dayDeta = new ArrayList<String>();
        ArrayList<String> weekTest = new ArrayList<String>();
        Connection con = null;
        PbReturnObject ret = null;
        con = ProgenConnection.getInstance().getConnectionForElement(eleId);
        if (con != null) {
            con = ProgenConnection.getInstance().getConnectionForElement(eleId);
            try {
                ret = execSelectSQL("SELECT DDATE,CW_ST_MON_DATE,CW_END_MON_DATE FROM PR_DAY_DENOM WHERE CM_CUST_NAME='" + monthName + "' ORDER BY DDATE ASC", con);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            for (int i = 0; i < ret.rowCount; i++) {
                weekTest.add(ret.getFieldValueString(i, "CW_ST_MON_DATE"));
            }
            con = ProgenConnection.getInstance().getConnectionForElement(eleId);
            try {
                ret = execSelectSQL("SELECT DISTINCT CW_ST_MON_DATE,CW_END_MON_DATE FROM PR_DAY_DENOM WHERE CM_CUST_NAME='" + monthName + "' ORDER BY CW_ST_MON_DATE ASC", con);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            for (int i = 0; i < ret.rowCount; i++) {
                dayDeta.add(ret.getFieldValueString(i, 0));
            }
            for (int i = 0; i < weekTest.size(); i++) {
                if (dayDeta.get(0).equalsIgnoreCase(weekTest.get(i).toString())) {
                    dayDetails.add("W1");
                } else if (dayDeta.get(1).equalsIgnoreCase(weekTest.get(i).toString())) {
                    dayDetails.add("W2");
                } else if (dayDeta.get(2).equalsIgnoreCase(weekTest.get(i).toString())) {
                    dayDetails.add("W3");
                } else if (dayDeta.get(3).equalsIgnoreCase(weekTest.get(i).toString())) {
                    dayDetails.add("W4");
                } else if (dayDeta.get(4).equalsIgnoreCase(weekTest.get(i).toString())) {
                    dayDetails.add("W5");
                }
                if (dayDeta.size() > 5) {
                    if (dayDeta.get(5).equalsIgnoreCase(weekTest.get(i).toString())) {
                        dayDetails.add("W6");
                    }
                }
            }
        }
        return dayDetails;
    }

    public void insertTargetDayLevelDetails(String eleId, String monthName, ArrayList weekNames, ArrayList monthDates, ArrayList monthDayNames, ArrayList targetValues, String dayallocationtype, String allocationtype) {
        String finalVale = "";
        String insertTargetMeasures = getResourceBundle().getString("insertTargetDayLevelData");

        PbReturnObject retobj = null;
        Connection con = null;
        ArrayList querys = new ArrayList();

        //DAYLEVEL_ID,ELEMENT_ID,MONTH_NAME,MONTH_WEEKS,MONTH_DATES,MONTH_DAYS,TARGET_LEVEL_VALUE,ALLOCATION_TYPE,DAY_ALLOCATION_TYPE
        boolean testForMeasure = false;

        //String exstingQuery = "SELECT * FROM  PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='"+elementId+"' AND PERIOD_TYPE='"+periodType+"' AND START_DATE='"+StartVal+"' AND END_DATE='"+endVal+"'";
        String exstingQuery = "SELECT DAYLEVEL_ID FROM  PRG_AR_TARGET_DAYLEVEL_DATA WHERE ELEMENT_ID='" + eleId + "' AND ALLOCATION_TYPE='" + allocationtype + "' order by DAYLEVEL_ID ASC";
        String updateQuery = "UPDATE PRG_AR_TARGET_DAYLEVEL_DATA SET ELEMENT_ID='&' ,MONTH_NAME='&' ,MONTH_WEEKS='&' , MONTH_DATES='&' ,MONTH_DAYS='&',TARGET_LEVEL_VALUE='&',ALLOCATION_TYPE='&',DAY_ALLOCATION_TYPE='&' WHERE  DAYLEVEL_ID=&";
        List<String> targetIds = new ArrayList<String>();
        try {
            con = ProgenConnection.getInstance().getConnectionForElement(eleId);
            retobj = execSelectSQL(exstingQuery, con);
            if (retobj.rowCount > 0) {
                for (int i = 0; i < retobj.rowCount; i++) {
                    targetIds.add(retobj.getFieldValueString(i, 0));
                }
                testForMeasure = true;
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        Object[] targetMeasures = null;
        try {
            con = ProgenConnection.getInstance().getConnectionForElement(eleId);
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                targetMeasures = new Object[9];
                if (testForMeasure) {
                    for (int i = 0; i < weekNames.size(); i++) {
                        // con = ProgenConnection.getInstance().getConnectionForElement(eleId);
                        targetMeasures[0] = eleId;
                        targetMeasures[1] = monthName;
                        targetMeasures[2] = weekNames.get(i);
                        targetMeasures[3] = monthDates.get(i);
                        targetMeasures[4] = monthDayNames.get(i);
                        targetMeasures[5] = targetValues.get(i);
                        targetMeasures[6] = allocationtype;
                        targetMeasures[7] = targetIds.get(i);
                        finalVale = buildQuery(updateQuery, targetMeasures);
                        //execUpdateSQL(finalVale,con);
                        querys.add(finalVale);
                    }
                    executeMultiple(querys, con);
                } else {
                    for (int i = 0; i < weekNames.size(); i++) {
                        //con = ProgenConnection.getInstance().getConnectionForElement(eleId);
                        targetMeasures[0] = eleId;
                        targetMeasures[1] = monthName;
                        targetMeasures[2] = weekNames.get(i);
                        targetMeasures[3] = monthDates.get(i);
                        targetMeasures[4] = monthDayNames.get(i);
                        targetMeasures[5] = targetValues.get(i);
                        targetMeasures[6] = allocationtype;
                        targetMeasures[7] = dayallocationtype;
                        finalVale = buildQuery(insertTargetMeasures, targetMeasures);
                        //execUpdateSQL(finalVale,con);
                        querys.add(finalVale);
                    }
                    executeMultiple(querys, con);
                }
            } else {
                targetMeasures = new Object[9];
                if (testForMeasure) {
                    for (int i = 0; i < weekNames.size(); i++) {
                        //con = ProgenConnection.getInstance().getConnectionForElement(eleId);
                        targetMeasures[0] = eleId;
                        targetMeasures[1] = monthName;
                        targetMeasures[2] = weekNames.get(i);
                        targetMeasures[3] = monthDates.get(i);
                        targetMeasures[4] = monthDayNames.get(i);
                        targetMeasures[5] = targetValues.get(i);
                        targetMeasures[6] = allocationtype;
                        targetMeasures[7] = dayallocationtype;
                        targetMeasures[8] = targetIds.get(i);
                        finalVale = buildQuery(updateQuery, targetMeasures);
                        //execUpdateSQL(finalVale,con);
                        querys.add(finalVale);
                    }
                    executeMultiple(querys, con);
                } else {
                    for (int i = 0; i < weekNames.size(); i++) {
                        //con = ProgenConnection.getInstance().getConnectionForElement(eleId);
                        targetMeasures[0] = eleId;
                        targetMeasures[1] = monthName;
                        targetMeasures[2] = weekNames.get(i);
                        targetMeasures[3] = monthDates.get(i);
                        targetMeasures[4] = monthDayNames.get(i);
                        targetMeasures[5] = targetValues.get(i);
                        targetMeasures[6] = allocationtype;
                        targetMeasures[7] = dayallocationtype;
                        finalVale = buildQuery(insertTargetMeasures, targetMeasures);
                        //execUpdateSQL(finalVale,con);
                        querys.add(finalVale);
                    }
                    executeMultiple(querys, con);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

    }

    public String getMonthTargetValue(String monthName, String elemId) {
        String targetValue = "";
        PbReturnObject retobj = null;
        String exstingQuery = "SELECT TARGET_VALUE FROM  PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='" + elemId + "' AND CUSTOM_NAME='" + monthName + "' order by TARGET_ID ASC";
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(elemId);
        try {
            retobj = execSelectSQL(exstingQuery, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (retobj.rowCount > 0) {
            targetValue = retobj.getFieldValueString(0, "TARGET_VALUE");
        }

        return targetValue;
    }

    public PbReturnObject getDayLevelDetalsData(String elemId, String monthName) {

        PbReturnObject retobj = null;
        String exstingQuery = "SELECT MONTH_DATES,TARGET_LEVEL_VALUE FROM  PRG_AR_TARGET_DAYLEVEL_DATA WHERE ELEMENT_ID='" + elemId + "' AND MONTH_NAME='" + monthName + "'  ORDER BY DAYLEVEL_ID";
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(elemId);
        try {
            retobj = execSelectSQL(exstingQuery, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return retobj;
    }

    public PbReturnObject getWeekLevelDetalsData(String elemId, String monthName) {

        PbReturnObject retobj = null;
        String exstingQuery = "SELECT MONTH_WEEKS,TARGET_WEEK_VALUE FROM  PRG_AR_TARGET_WEEK_ALLOCATION WHERE ELEMENT_ID='" + elemId + "' AND MONTH_NAME='" + monthName + "'  ORDER BY DAYLEVEL_ID";
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(elemId);
        try {
            retobj = execSelectSQL(exstingQuery, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return retobj;
    }

    public ArrayList getDayOrWeekandAllocationType(String elemId, String monthName, String levelTypeData) {
        ArrayList allocationType = new ArrayList();
        PbReturnObject retobj = null;
        String exstingQuery = "SELECT DISTINCT ALLOCATION_TYPE,DAY_ALLOCATION_TYPE FROM  PRG_AR_TARGET_DAYLEVEL_DATA WHERE ELEMENT_ID='" + elemId + "' AND MONTH_NAME='" + monthName + "'";
//            String exstingQuery = "SELECT DISTINCT ALLOCATION_TYPE,DAY_ALLOCATION_TYPE FROM  PRG_AR_TARGET_DAYLEVEL_DATA WHERE ELEMENT_ID='"+elemId+"' AND MONTH_NAME='"+monthName+"' AND ALLOCATION_TYPE='"+levelTypeData+"'";
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(elemId);
        try {

            retobj = execSelectSQL(exstingQuery, con);
            if (retobj.rowCount > 0) {
                allocationType.add(retobj.getFieldValueString(0, "ALLOCATION_TYPE"));
                allocationType.add(retobj.getFieldValueString(0, "DAY_ALLOCATION_TYPE"));
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return allocationType;
    }

    public ArrayList getCalculatedWeekData(String[] targetVals, String[] percentVals, String[] nameofWeek, ArrayList dayDetails, String targetValue) {
        ArrayList<String> targetValslist = new ArrayList<String>();
        ArrayList<String> percentValslist = new ArrayList<String>();
        ArrayList<String> nameofWeeklist = new ArrayList<String>();
        ArrayList<String> nofoValuesList = new ArrayList<String>();
        ArrayList<String> weekLevelDistValues = new ArrayList<String>();
        targetValslist.addAll(Arrays.asList(targetVals));
        percentValslist.addAll(Arrays.asList(percentVals));
        nameofWeeklist.addAll(Arrays.asList(nameofWeek));

        for (int i = 0; i < nameofWeeklist.size(); i++) {
            int k = 0;
            for (int j = 0; j < dayDetails.size(); j++) {
                if (nameofWeeklist.get(i).equalsIgnoreCase(dayDetails.get(j).toString())) {
                    k++;
                }
            }
            nofoValuesList.add(Integer.toString(k));
        }
        for (int i = 0; i < nofoValuesList.size(); i++) {
            int weekTargetVal = Integer.parseInt(targetValslist.get(i));
            int noofweeks = Integer.parseInt(nofoValuesList.get(i));

            int disrubutedVal = weekTargetVal / noofweeks;
            for (int j = 0; j < noofweeks; j++) {
                weekLevelDistValues.add(Integer.toString(disrubutedVal));
            }
        }
        int remaingValue = 0;
        for (int i = 0; i < weekLevelDistValues.size(); i++) {
            remaingValue = remaingValue + Integer.parseInt(weekLevelDistValues.get(i));
        }
        if (Integer.parseInt(targetValue) != remaingValue) {
            int val = Integer.parseInt(targetValue) - remaingValue;
            int lastVal = Integer.parseInt(weekLevelDistValues.get(weekLevelDistValues.size() - 1)) + val;
            weekLevelDistValues.remove(weekLevelDistValues.size() - 1);
            weekLevelDistValues.add(weekLevelDistValues.size(), Integer.toString(lastVal));
        }
        return weekLevelDistValues;
    }

    public void insertWeekLevelData(String eleId, String monthName, int noofWeeks, ArrayList targetValsArray, ArrayList percentValsArray, ArrayList nameofWeekArray) {
        String finalVale = "";
        String insertTargetMeasures = getResourceBundle().getString("insertTargetWeekLevelData");

        PbReturnObject retobj = null;
        Connection con = null;
        ArrayList querys = new ArrayList();

        boolean testForMeasure = false;

        //String exstingQuery = "SELECT * FROM  PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='"+elementId+"' AND PERIOD_TYPE='"+periodType+"' AND START_DATE='"+StartVal+"' AND END_DATE='"+endVal+"'";
        String exstingQuery = "SELECT DAYLEVEL_ID FROM  PRG_AR_TARGET_WEEK_ALLOCATION WHERE ELEMENT_ID='" + eleId + "'  AND MONTH_NAME='" + monthName + "' order by DAYLEVEL_ID ASC";
        String updateQuery = "UPDATE PRG_AR_TARGET_WEEK_ALLOCATION SET MONTH_NAME='&',MONTH_WEEKS='&',TARGET_WEEK_VALUE='&',TARGET_PERCENT_VALUE='&' WHERE  DAYLEVEL_ID=&";
        List<String> targetIds = new ArrayList<String>();
        try {
            con = ProgenConnection.getInstance().getConnectionForElement(eleId);
            retobj = execSelectSQL(exstingQuery, con);
            if (retobj.rowCount > 0) {
                for (int i = 0; i < retobj.rowCount; i++) {
                    targetIds.add(retobj.getFieldValueString(i, 0));
                }
                testForMeasure = true;
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        Object[] targetMeasures = null;
        try {
            con = ProgenConnection.getInstance().getConnectionForElement(eleId);
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                targetMeasures = new Object[5];
                if (testForMeasure) {
                    for (int i = 0; i < noofWeeks; i++) {
                        //con = ProgenConnection.getInstance().getConnectionForElement(eleId);
                        targetMeasures[0] = monthName;
                        targetMeasures[1] = nameofWeekArray.get(i);
                        targetMeasures[2] = targetValsArray.get(i);
                        targetMeasures[3] = percentValsArray.get(i);
                        targetMeasures[4] = targetIds.get(i);
                        finalVale = buildQuery(updateQuery, targetMeasures);
                        // execUpdateSQL(finalVale,con);
                        querys.add(finalVale);
                    }
                    executeMultiple(querys, con);
                } else {
                    for (int i = 0; i < noofWeeks; i++) {
                        // con = ProgenConnection.getInstance().getConnectionForElement(eleId);
                        targetMeasures[0] = eleId;
                        targetMeasures[1] = monthName;
                        targetMeasures[2] = nameofWeekArray.get(i);
                        targetMeasures[3] = targetValsArray.get(i);
                        targetMeasures[4] = percentValsArray.get(i);
                        finalVale = buildQuery(insertTargetMeasures, targetMeasures);
                        //execUpdateSQL(finalVale,con);
                        querys.add(finalVale);
                    }
                    executeMultiple(querys, con);
                }
            } else {
                targetMeasures = new Object[5];
                if (testForMeasure) {
                    for (int i = 0; i < noofWeeks; i++) {
                        //con = ProgenConnection.getInstance().getConnectionForElement(eleId);
                        targetMeasures[0] = monthName;
                        targetMeasures[1] = nameofWeekArray.get(i);
                        targetMeasures[2] = targetValsArray.get(i);
                        targetMeasures[3] = percentValsArray.get(i);
                        targetMeasures[4] = targetIds.get(i);
                        finalVale = buildQuery(updateQuery, targetMeasures);
                        // execUpdateSQL(finalVale,con);
                        querys.add(finalVale);
                    }
                    executeMultiple(querys, con);
                } else {
                    for (int i = 0; i < noofWeeks; i++) {
                        //con = ProgenConnection.getInstance().getConnectionForElement(eleId);
                        targetMeasures[0] = eleId;
                        targetMeasures[1] = monthName;
                        targetMeasures[2] = nameofWeekArray.get(i);
                        targetMeasures[3] = targetValsArray.get(i);
                        targetMeasures[4] = percentValsArray.get(i);
                        finalVale = buildQuery(insertTargetMeasures, targetMeasures);
                        // execUpdateSQL(finalVale,con);
                        querys.add(finalVale);
                    }
                    executeMultiple(querys, con);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void uploadWeekLevelData(String eleId, String monthName, String viewById, String viewByName, String eleName, List<String> totalValues, List<String> viewBynames, List<String> week1, List<String> week2, List<String> week3, List<String> week4, List<String> week5, List<String> week6) {
        String finalVale = "";
        String insertTargetMeasures = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            insertTargetMeasures = "insert into PRG_AR_WEEKLEVEL_UPLOADEDDATA(VIEWBY_ID,VIEWBY_NAME,ELEMENT_ID,ELEMENT_NAME,VIEWBY_MONTH,WEEK1,WEEK2,WEEK3,WEEK4,WEEK5,WEEK6,TOTAL_VALUE,VIEWBY_NAMES) values('&','&','&','&','&','&','&','&','&','&','&','&','&')";
        } else {
            insertTargetMeasures = "insert into PRG_AR_WEEKLEVEL_UPLOADEDDATA(DAYLEVEL_ID,VIEWBY_ID,VIEWBY_NAME,ELEMENT_ID,ELEMENT_NAME,VIEWBY_MONTH,WEEK1,WEEK2,WEEK3,WEEK4,WEEK5,WEEK6,TOTAL_VALUE,VIEWBY_NAMES) values(PRG_AR_WEEKDATA_SEQ.nextval,'&','&','&','&','&','&','&','&','&','&','&','&','&')";
        }

        PbReturnObject retobj = null;
        Connection con = null;
        ArrayList querys = new ArrayList();
        boolean testForMeasure = false;

        //String exstingQuery = "SELECT * FROM  PRG_AR_TARGET_MEASURES_DETAILS WHERE ELEMENT_ID='"+elementId+"' AND PERIOD_TYPE='"+periodType+"' AND START_DATE='"+StartVal+"' AND END_DATE='"+endVal+"'";
        String exstingQuery = "SELECT DAYLEVEL_ID FROM  PRG_AR_WEEKLEVEL_UPLOADEDDATA WHERE ELEMENT_ID='" + eleId + "'  AND VIEWBY_MONTH='" + monthName + "' AND VIEWBY_NAME ='" + viewByName + "'order by DAYLEVEL_ID ASC";
        String updateQuery = "UPDATE PRG_AR_WEEKLEVEL_UPLOADEDDATA SET WEEK1='&',WEEK2='&',WEEK3='&',WEEK4='&',WEEK5='&',WEEK6='&',TOTAL_VALUE='&',VIEWBY_NAMES='&' WHERE  DAYLEVEL_ID=&";
        List<String> targetIds = new ArrayList<String>();
        try {
            con = ProgenConnection.getInstance().getConnectionForElement(eleId);
            retobj = execSelectSQL(exstingQuery, con);
            if (retobj.rowCount > 0) {
                for (int i = 0; i < retobj.rowCount; i++) {
                    targetIds.add(retobj.getFieldValueString(i, 0));
                }
                testForMeasure = true;
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        Object[] targetMeasures = null;
        try {
            con = ProgenConnection.getInstance().getConnectionForElement(eleId);

            if (testForMeasure) {
                targetMeasures = new Object[9];
                for (int i = 0; i < viewBynames.size(); i++) {
                    //con = ProgenConnection.getInstance().getConnectionForElement(eleId);
                    targetMeasures[0] = week1.get(i);
                    targetMeasures[1] = week2.get(i);
                    targetMeasures[2] = week3.get(i);
                    targetMeasures[3] = week4.get(i);
                    targetMeasures[4] = week5.get(i);
                    if (!week6.isEmpty()) {
                        targetMeasures[5] = week6.get(i);
                    } else {
                        targetMeasures[5] = "";
                    }
                    targetMeasures[6] = totalValues.get(i);
                    targetMeasures[7] = viewBynames.get(i);
                    targetMeasures[8] = targetIds.get(i);
                    finalVale = buildQuery(updateQuery, targetMeasures);
                    querys.add(finalVale);
                }
                executeMultiple(querys, con);
            } else {
                targetMeasures = new Object[13];
                for (int i = 0; i < viewBynames.size(); i++) {
                    //con = ProgenConnection.getInstance().getConnectionForElement(eleId);
                    targetMeasures[0] = viewById;
                    targetMeasures[1] = viewByName;
                    targetMeasures[2] = eleId;
                    targetMeasures[3] = eleName;
                    targetMeasures[4] = monthName;
                    targetMeasures[5] = week1.get(i);
                    targetMeasures[6] = week2.get(i);
                    targetMeasures[7] = week3.get(i);
                    targetMeasures[8] = week4.get(i);
                    targetMeasures[9] = week5.get(i);
                    if (!week6.isEmpty()) {
                        targetMeasures[10] = week6.get(i);
                    } else {
                        targetMeasures[10] = "";
                    }
                    targetMeasures[11] = totalValues.get(i);
                    targetMeasures[12] = viewBynames.get(i);
                    finalVale = buildQuery(insertTargetMeasures, targetMeasures);
                    querys.add(finalVale);
                    //execUpdateSQL(finalVale,con);
                }
                executeMultiple(querys, con);
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }
    //started by Nazneen for CCC on Dec 2012

    public int saveCompanyDetails(String companyName, String companyDesc) throws SQLException {
        SourceConn sc = new SourceConn();
        Connection conSource = ProgenConnection.getInstance().getConnection();
        Connection conTarget = sc.getConnection("seaLinkdw", "", "", "", "", "", "", "", "");
        PreparedStatement pstmtForTransterSource;
        PreparedStatement pstmtForTransterTarget;
        PreparedStatement pstmtForTracCmp;

//        PreparedStatement pstmtForTempCmp;
        int flagSource = 0;
        int flagTarget = 0;
//        int flagTemp = 0;
        int flag = 0;
        ResultSet resultSet = null;
        PbReturnObject retObj = null;
        PbReturnObject returnObject = null;

        String qry = "select COMPANY_MASTER_SEQ.nextval from dual ";
        String insertCompanyDetailsSource = "insert into COMPANY_MASTER (COMPANY_ID,COMPANY_NAME,COMPANY_DESC) values (&,'&','&')";
//        String insertCompanyDetailsTarget = "insert into COMPANY_MASTER (COMPANY_ID,COMPANY_NAME,COMPANY_DESC) values (&,'&','&')";

        pstmtForTransterSource = conSource.prepareStatement(qry);
        resultSet = pstmtForTransterSource.executeQuery();
        retObj = new PbReturnObject(resultSet);
        int id = retObj.getFieldValueInt(0, 0);

        pstmtForTransterSource = null;
        Object inOb[] = new Object[3];
        inOb[0] = id;
        inOb[1] = companyName;
        inOb[2] = companyDesc;
        String inQuerySource = buildQuery(insertCompanyDetailsSource, inOb);
        String inQueryTarget = buildQuery(insertCompanyDetailsSource, inOb);
        if (conSource != null && conTarget != null) {
            try {
                pstmtForTransterSource = conSource.prepareStatement(inQuerySource);
                pstmtForTransterTarget = conTarget.prepareStatement(inQueryTarget);

                flagSource = pstmtForTransterSource.executeUpdate();
                flagTarget = pstmtForTransterTarget.executeUpdate();

            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        ArrayList alist = new ArrayList();
        String insertInTrackerLoadCmp = "insert into prg_load_tracker_master_cmp(ID,TAB_NAME,LAST_UPDATE_DATE,FACT_LAST_UPDATE_DATE,COMPANY_ID,SOURCE_TIMEZONE,TARGET_TIMEZONE) "
                + "values(&,'&','&','&',&,'&','&')";
        String selLoadTrackMaster = "select TAB_NAME,SOURCE_TIMEZONE,TARGET_TIMEZONE from prg_temp_load_tracker_master";
        pstmtForTracCmp = conSource.prepareStatement(selLoadTrackMaster);
        resultSet = pstmtForTracCmp.executeQuery();
        returnObject = new PbReturnObject(resultSet);
        String finalQuery = "";
//        boolean result = false;
        Object[] Obj = new Object[7];

        for (int i = 0; i < returnObject.getRowCount(); i++) {
            Obj[0] = "ETL_CONNECTIONS_DETAILS_SEQ.NEXTVAL";
            Obj[1] = returnObject.getFieldValueString(i, 0);
            Obj[2] = "01-JAN-06";
            Obj[3] = "01-JAN-06";
            Obj[4] = id;
            Obj[5] = returnObject.getFieldValueString(i, 1);
            Obj[6] = returnObject.getFieldValueString(i, 2);

            finalQuery = buildQuery(insertInTrackerLoadCmp, Obj);
            alist.add(finalQuery);
        }
        executeMultiple(alist);

        if (flagSource == 1 && flagTarget == 1) {
            flag = 1;
        }
        if (conSource != null) {
            conSource = null;
        }
        if (conTarget != null) {
            conSource = null;
        }
        return flag;
    }

    public String getAllCompanyDetails() throws SQLException {
//        SourceConn sc = new SourceConn();
        Connection conSource = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
        PbReturnObject retObj = null;

        String Query = " SELECT COMPANY_ID, COMPANY_NAME FROM COMPANY_MASTER order by  COMPANY_NAME ";
        pstmtForTransterSource = conSource.prepareStatement(Query);
        resultSet = pstmtForTransterSource.executeQuery();
        retObj = new PbReturnObject(resultSet);
        StringBuilder sb = new StringBuilder();

        sb.append("<option value = '0000'>--SELECT--</option>");
        int id = retObj.getFieldValueInt(0, 0);
        for (int i = 0; i < retObj.getRowCount(); i++) {
            sb.append("<option value='" + retObj.getFieldValueInt(i, 0) + "'>");
            sb.append(retObj.getFieldValueString(i, 1));
            sb.append("</option>");
        }
        if (conSource != null) {
            conSource = null;
        }
        return sb.toString();

    }

    public String getCompanyConnDetails(String companyId) throws SQLException {
//        SourceConn sc = new SourceConn();
//        Connection conSource = sc.getConnection("oracle1", "", "", "","","","","","");
        Connection conSource = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
        PbReturnObject returnObject = null;

        String jsonString = null;
        Gson json = new Gson();
        List<String> id = new ArrayList<String>();
        List<String> compId = new ArrayList<String>();
        List<String> compName = new ArrayList<String>();
        List<String> loadType = new ArrayList<String>();
        List<String> userName = new ArrayList<String>();
        List<String> pswd = new ArrayList<String>();
        List<String> server = new ArrayList<String>();
        List<String> serviceId = new ArrayList<String>();
        List<String> serviceName = new ArrayList<String>();
        List<String> portNumber = new ArrayList<String>();
        List<String> sourceTz = new ArrayList<String>();
        List<String> targetTz = new ArrayList<String>();
        List<String> dbConn = new ArrayList<String>();
        Map dimmap = new HashMap();
        String finalquery = "";

        String Query = "SELECT A.ID,A.COMPANY_ID,B.COMPANY_NAME,A.LOAD_TYPE,A.USER_NAME,A.PASSWORD,A.SERVER,A.SERVICE_ID,A.SERVICE_NAME,A.PORT,A.SOURCE_TIMEZONE,A.TARGET_TIMEZONE,A.DBNAME "
                + " FROM ETL_CONNECTIONS_DETAILS A,COMPANY_MASTER B  WHERE A.COMPANY_ID = B.COMPANY_ID  AND A.COMPANY_ID = " + companyId;
        try {
            pstmtForTransterSource = conSource.prepareStatement(Query);
            resultSet = pstmtForTransterSource.executeQuery();
            returnObject = new PbReturnObject(resultSet);
//            StringBuilder sb = new StringBuilder();

            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    id.add(returnObject.getFieldValueString(i, 0));
                    compId.add(returnObject.getFieldValueString(i, 1));
                    compName.add(returnObject.getFieldValueString(i, 2));
                    loadType.add(returnObject.getFieldValueString(i, 3));
                    userName.add(returnObject.getFieldValueString(i, 4));
                    pswd.add(returnObject.getFieldValueString(i, 5));
                    server.add(returnObject.getFieldValueString(i, 6));
                    serviceId.add(returnObject.getFieldValueString(i, 7));
                    serviceName.add(returnObject.getFieldValueString(i, 8));
                    portNumber.add(returnObject.getFieldValueString(i, 9));
                    sourceTz.add(returnObject.getFieldValueString(i, 10));
                    targetTz.add(returnObject.getFieldValueString(i, 11));
                    dbConn.add(returnObject.getFieldValueString(i, 12));
                }
            }
            dimmap.put("id", id);
            dimmap.put("compId", compId);
            dimmap.put("compName", compName);
            dimmap.put("loadType", loadType);
            dimmap.put("userName", userName);
            dimmap.put("pswd", pswd);
            dimmap.put("server", server);
            dimmap.put("serviceId", serviceId);
            dimmap.put("serviceName", serviceName);
            dimmap.put("portNumber", portNumber);
            dimmap.put("sourceTz", sourceTz);
            dimmap.put("targetTz", targetTz);
            dimmap.put("dbConn", dbConn);
            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;

    }

    public String getloadtypeDetails(String companyId) throws SQLException {
//        SourceConn sc = new SourceConn();
        Connection conSource = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
        PbReturnObject retObj = null;
        String[] loadTypesArray = {"quickTravel", "ctServer", "accpac"};

        String Query = "SELECT LOAD_TYPE FROM ETL_CONNECTIONS_DETAILS WHERE COMPANY_ID=?";
        pstmtForTransterSource = conSource.prepareStatement(Query);
        pstmtForTransterSource.setString(1, companyId);
        resultSet = pstmtForTransterSource.executeQuery();
        retObj = new PbReturnObject(resultSet);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < loadTypesArray.length; i++) {
            int flag = 0;
            for (int j = 0; j < retObj.getRowCount(); j++) {
                if (loadTypesArray[i].equalsIgnoreCase(retObj.getFieldValueString(j, 0))) {
                    flag = 1;
                }
            }
            if (flag == 0) {
                sb.append("<option value='" + loadTypesArray[i] + "'>");
                sb.append(loadTypesArray[i]);
                sb.append("</option>");
            }
        }
        if (conSource != null) {
            conSource = null;
        }
        return sb.toString();
    }

    public String getCompanyName(String companyId) throws SQLException {
//        SourceConn sc = new SourceConn();
//        Connection conSource = sc.getConnection("oracle1", "", "", "","","","","","");
        Connection conSource = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
        PbReturnObject returnObject = null;

        String jsonString = null;
        Gson json = new Gson();
        List<String> compName = new ArrayList<String>();
        Map dimmap = new HashMap();
//        String finalquery = "";

        String Query = "SELECT COMPANY_NAME "
                + " FROM COMPANY_MASTER "
                + " WHERE COMPANY_ID = " + companyId;
        try {
            pstmtForTransterSource = conSource.prepareStatement(Query);
            resultSet = pstmtForTransterSource.executeQuery();
            returnObject = new PbReturnObject(resultSet);
            StringBuilder sb = new StringBuilder();

            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    compName.add(returnObject.getFieldValueString(i, 0));
                }
            }
            dimmap.put("compName", compName);
            jsonString = json.toJson(dimmap);

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;
    }

    public int saveConnectionsDetails(String companyId, String loadType, String userName, String password, String server, String serviceId, String serviceName, String port, String SourceTimeZone, String TargetTimeZone, String dsnName, String dbConnType, String dbName, String runIndep) throws SQLException {
//        SourceConn sc = new SourceConn();
        Connection conn = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransfer;
        int flag = 0;
        ResultSet resultSet = null;
        PbReturnObject retObj = null;
        String paswd = DeEncrypter.getInstance().encrypt(password);

        String insertEtlConnDetails = "insert into ETL_CONNECTIONS_DETAILS (ID,COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,SERVICE_NAME,PORT,SOURCE_TIMEZONE,TARGET_TIMEZONE,DSN_NAME,DB_CONNECTION_TYPE,DBNAME,RUN_INDEPENDENTLY) "
                + " values (ETL_CONNECTIONS_DETAILS_SEQ.NEXTVAL,'&','&','&','&','&','&','&','&','&','&','&','&','&','&')";

        Object inOb[] = new Object[14];
        inOb[0] = companyId;
        inOb[1] = loadType;
        inOb[2] = userName;
        inOb[3] = paswd;
        inOb[4] = server;
        inOb[5] = serviceId;
        inOb[6] = serviceName;
        inOb[7] = port;
        inOb[8] = SourceTimeZone;
        inOb[9] = TargetTimeZone;
        inOb[10] = dsnName;
        inOb[11] = dbConnType;
        inOb[12] = dbName;
        inOb[13] = runIndep;

        String Query = buildQuery(insertEtlConnDetails, inOb);
        if (conn != null) {
            try {
                pstmtForTransfer = conn.prepareStatement(Query);
                flag = pstmtForTransfer.executeUpdate();
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        if (conn != null) {
            conn = null;
        }

        return flag;
    }

    public int deleteConnectionsDetails(String tabId) throws SQLException {
        SourceConn sc = new SourceConn();
        Connection conn = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransfer;
        int flag = 0;

        String insertEtlConnDetails = "delete from ETL_CONNECTIONS_DETAILS where id = &";
        Object inOb[] = new Object[1];
        inOb[0] = tabId;

        String Query = buildQuery(insertEtlConnDetails, inOb);
        if (conn != null) {
            try {
                pstmtForTransfer = conn.prepareStatement(Query);
                flag = pstmtForTransfer.executeUpdate();
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        if (conn != null) {
            conn = null;
        }

        return flag;
    }

    public String editCompanyConnDetails(String tabId, String companyId) throws SQLException {
//        SourceConn sc = new SourceConn();
        Connection conSource = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
        PbReturnObject returnObject = null;

        String jsonString = null;
        Gson json = new Gson();
        List<String> compName = new ArrayList<String>();
        List<String> loadType = new ArrayList<String>();
        List<String> userName = new ArrayList<String>();
        List<String> pswd = new ArrayList<String>();
        List<String> server = new ArrayList<String>();
        List<String> serviceId = new ArrayList<String>();
        List<String> serviceName = new ArrayList<String>();
        List<String> portNumber = new ArrayList<String>();
        List<String> dsnName = new ArrayList<String>();
        List<String> dbConnType = new ArrayList<String>();
        List<String> dbName = new ArrayList<String>();
        List<String> sourceTz = new ArrayList<String>();
        List<String> targetTz = new ArrayList<String>();
        List<String> runIndep = new ArrayList<String>();
        Map dimmap = new HashMap();
        String finalquery = "";

        String Query = "SELECT B.COMPANY_NAME,A.LOAD_TYPE,A.USER_NAME,A.PASSWORD,A.SERVER,A.SERVICE_ID,A.SERVICE_NAME,A.PORT,A.DSN_NAME,A.DB_CONNECTION_TYPE,"
                + "A.DBNAME,A.SOURCE_TIMEZONE,A.TARGET_TIMEZONE,A.RUN_INDEPENDENTLY FROM "
                + "ETL_CONNECTIONS_DETAILS A,COMPANY_MASTER B "
                + " WHERE A.COMPANY_ID = B.COMPANY_ID "
                + " AND A.COMPANY_ID = " + companyId + ""
                + " AND A.ID = " + tabId;
        try {
            pstmtForTransterSource = conSource.prepareStatement(Query);
            resultSet = pstmtForTransterSource.executeQuery();
            returnObject = new PbReturnObject(resultSet);
            StringBuilder sb = new StringBuilder();

            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    compName.add(returnObject.getFieldValueString(i, 0));
                    loadType.add(returnObject.getFieldValueString(i, 1));
                    userName.add(returnObject.getFieldValueString(i, 2));
                    String paswd = DeEncrypter.getInstance().decrypt(returnObject.getFieldValueString(i, 3));
                    pswd.add(paswd);
                    server.add(returnObject.getFieldValueString(i, 4));
                    serviceId.add(returnObject.getFieldValueString(i, 5));
                    serviceName.add(returnObject.getFieldValueString(i, 6));
                    portNumber.add(returnObject.getFieldValueString(i, 7));
                    dsnName.add(returnObject.getFieldValueString(i, 8));
                    dbConnType.add(returnObject.getFieldValueString(i, 9));
                    dbName.add(returnObject.getFieldValueString(i, 10));
                    sourceTz.add(returnObject.getFieldValueString(i, 11));
                    targetTz.add(returnObject.getFieldValueString(i, 12));
                    runIndep.add(returnObject.getFieldValueString(i, 13));
                }
            }
            dimmap.put("compName", compName);
            dimmap.put("loadType", loadType);
            dimmap.put("userName", userName);
            dimmap.put("pswd", pswd);
            dimmap.put("server", server);
            dimmap.put("serviceId", serviceId);
            dimmap.put("serviceName", serviceName);
            dimmap.put("portNumber", portNumber);
            dimmap.put("dsnName", dsnName);
            dimmap.put("dbConnType", dbConnType);
            dimmap.put("dbName", dbName);
            dimmap.put("sourceTz", sourceTz);
            dimmap.put("targetTz", targetTz);
            dimmap.put("runIndep", runIndep);
            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;

    }

    public int updateConnectionsDetails(String tabId, String loadType, String userName, String password, String server, String serviceId, String serviceName, String port, String SourceTimeZone, String TargetTimeZone, String dsnName, String dbConnType, String dbName, String runIndep) throws SQLException {
//        SourceConn sc = new SourceConn();
//        Connection conn = sc.getConnection("oracle1", "", "", "","","","","","");
        Connection conn = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransfer;
        int flag = 0;
        ResultSet resultSet = null;
        PbReturnObject retObj = null;
        String paswd = DeEncrypter.getInstance().encrypt(password);

        String updateEtlConnDetails = "update ETL_CONNECTIONS_DETAILS "
                + " set LOAD_TYPE = '&',"
                + " USER_NAME = '&',"
                + " PASSWORD = '&',"
                + " SERVER = '&',"
                + " SERVICE_ID = '&',"
                + " SERVICE_NAME = '&',"
                + " PORT = '&',"
                + " SOURCE_TIMEZONE = '&',"
                + " TARGET_TIMEZONE = '&',"
                + " DSN_NAME = '&',"
                + " DB_CONNECTION_TYPE = '&',"
                + " DBNAME = '&',"
                + " RUN_INDEPENDENTLY = '&' "
                + " WHERE ID = &";

        Object inOb[] = new Object[14];
        inOb[0] = loadType;
        inOb[1] = userName;
        inOb[2] = paswd;
        inOb[3] = server;
        inOb[4] = serviceId;
        inOb[5] = serviceName;
        inOb[6] = port;
        inOb[7] = SourceTimeZone;
        inOb[8] = TargetTimeZone;
        inOb[9] = dsnName;
        inOb[10] = dbConnType;
        inOb[11] = dbName;
        inOb[12] = runIndep;
        inOb[13] = tabId;

        String Query = buildQuery(updateEtlConnDetails, inOb);
        if (conn != null) {
            try {
                pstmtForTransfer = conn.prepareStatement(Query);
                flag = pstmtForTransfer.executeUpdate();
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        if (conn != null) {
            conn = null;
        }
        return flag;
    }

    public String getCompanyDetails() throws SQLException {
//        SourceConn sc = new SourceConn();
        //sc.getConnection("oracle1", "", "", "","","","","","");
        Connection conSource = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
        PbReturnObject returnObject = null;

        String jsonString = null;
        Gson json = new Gson();
        List<String> compId = new ArrayList<String>();
        List<String> compName = new ArrayList<String>();
        List<String> compDesc = new ArrayList<String>();
        Map dimmap = new HashMap();
        String finalquery = "";

        String Query = "SELECT COMPANY_ID,COMPANY_NAME,COMPANY_DESC FROM COMPANY_MASTER";
        try {
            pstmtForTransterSource = conSource.prepareStatement(Query);
            resultSet = pstmtForTransterSource.executeQuery();
            returnObject = new PbReturnObject(resultSet);
            StringBuilder sb = new StringBuilder();

            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    compId.add(returnObject.getFieldValueString(i, 0));
                    compName.add(returnObject.getFieldValueString(i, 1));
                    compDesc.add(returnObject.getFieldValueString(i, 2));
                }
            }
            dimmap.put("compId", compId);
            dimmap.put("compName", compName);
            dimmap.put("compDesc", compDesc);
            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;

    }

    public String editCompanyDetails(String companyId) throws SQLException {
        SourceConn sc = new SourceConn();
        Connection conSource = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
        PbReturnObject returnObject = null;

        String jsonString = null;
        Gson json = new Gson();
        List<String> compName = new ArrayList<String>();
        List<String> compDesc = new ArrayList<String>();
        Map dimmap = new HashMap();
        String finalquery = "";

        String Query = "SELECT COMPANY_NAME,COMPANY_DESC FROM COMPANY_MASTER WHERE COMPANY_ID=" + companyId;
        try {
            pstmtForTransterSource = conSource.prepareStatement(Query);
            resultSet = pstmtForTransterSource.executeQuery();
            returnObject = new PbReturnObject(resultSet);
            StringBuilder sb = new StringBuilder();
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    compName.add(returnObject.getFieldValueString(i, 0));
                    compDesc.add(returnObject.getFieldValueString(i, 1));
                }
            }
            dimmap.put("compName", compName);
            dimmap.put("compDesc", compDesc);
            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        return jsonString;

    }

    public int updateCompanyDetails(String compId, String companyName, String companyDesc) throws SQLException {
        SourceConn sc = new SourceConn();
        Connection conSource = ProgenConnection.getInstance().getConnection();
        Connection conTarget = sc.getConnection("seaLinkdw", "", "", "", "", "", "", "", "");
        PreparedStatement pstmtForTransterSource;
        PreparedStatement pstmtForTransterTarget;

        int flagSource = 0;
        int flagTarget = 0;
        int flag = 0;
//        ResultSet resultSet = null;
//        PbReturnObject retObj = null;

        String updateCompanyDetails = "update COMPANY_MASTER  set COMPANY_NAME = '&', COMPANY_DESC = '&'  WHERE COMPANY_ID = &";

        Object inOb[] = new Object[3];
        inOb[0] = companyName;
        inOb[1] = companyDesc;
        inOb[2] = compId;

        String Query = buildQuery(updateCompanyDetails, inOb);
        if (conSource != null && conTarget != null) {
            try {
                pstmtForTransterSource = conSource.prepareStatement(Query);
                pstmtForTransterTarget = conTarget.prepareStatement(Query);
                flagSource = pstmtForTransterSource.executeUpdate();
                flagTarget = pstmtForTransterTarget.executeUpdate();
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        if (flagSource == 1 && flagTarget == 1) {
            flag = 1;
        }
        if (conSource != null) {
            conSource = null;
        }
        if (conTarget != null) {
            conSource = null;
        }
        return flag;
    }
    //end of code by Nazneen for CCC
    //Start user assignment for company..by Nazneen

    public String getCompAssignedUsers(String companyId) throws SQLException {
        SourceConn sc = new SourceConn();
        Connection conSource = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
        PbReturnObject returnObject = null;

        String jsonString = null;
        Gson json = new Gson();
        List<String> compId = new ArrayList<String>();
        List<String> compName = new ArrayList<String>();

        Map dimmap = new HashMap();
//        String finalquery = "";

        String Query = "SELECT PROGEN_USER_ID,PRGOGEN_USER_NAME FROM PRG_SEC_USER_COMPANY where COMPANY_ID=" + companyId + " order by PRGOGEN_USER_NAME";
        try {
            pstmtForTransterSource = conSource.prepareStatement(Query);
            resultSet = pstmtForTransterSource.executeQuery();
            returnObject = new PbReturnObject(resultSet);
            StringBuilder sb = new StringBuilder();

            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    // 
                    compId.add(returnObject.getFieldValueString(i, 0));
                    compName.add(returnObject.getFieldValueString(i, 1));

                }
            }
            dimmap.put("compId", compId);
            dimmap.put("compName", compName);
            dimmap.put("rsLength", returnObject.getRowCount());

            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;

    }

    public int assignuserToComp(String[] users, String[] userName, String companyId, String connectionID) throws SQLException {
        Connection conTarget = ProgenConnection.getInstance().getConnectionByConId(connectionID);
        Connection conSource = ProgenConnection.getInstance().getConnection();
        int flag = 0;
        int flag1 = 0;
        SourceConn sc = new SourceConn();
        ArrayList alist = new ArrayList();
        PreparedStatement psGetComp = null;
        PreparedStatement psSetComp = null;
        String delqry = null;
        String query = null;

        delqry = "delete from prg_sec_user_company where COMPANY_ID=" + Integer.parseInt(companyId);
        alist.add(delqry);
        try {
            for (int i = 0; i < userName.length; i++) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    query = "insert into prg_sec_user_company(COMPANY_ID,PROGEN_USER_ID,PRGOGEN_USER_NAME,APP_USER_NAME) values(" + Integer.parseInt(companyId) + "," + Integer.parseInt(users[i]) + ",'" + userName[i] + "','" + userName[i] + "')";
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    query = "insert into prg_sec_user_company(COMPANY_ID,PROGEN_USER_ID,PRGOGEN_USER_NAME,APP_USER_NAME) values(" + Integer.parseInt(companyId) + "," + Integer.parseInt(users[i]) + ",'" + userName[i] + "','" + userName[i] + "')";
                } else {
                    query = "insert into prg_sec_user_company(ID,COMPANY_ID,PROGEN_USER_ID,PRGOGEN_USER_NAME,APP_USER_NAME) values(PRG_SEC_USER_COMPANY_SEQ.NEXTVAL," + Integer.parseInt(companyId) + "," + Integer.parseInt(users[i]) + ",'" + userName[i] + "','" + userName[i] + "')";
                }

                alist.add(query);
            }
            for (int i = 0; i < alist.size(); i++) {
                psGetComp = conSource.prepareStatement(String.valueOf(alist.get(i)));
                flag = psGetComp.executeUpdate();
            }
            psGetComp.close();
            psGetComp = null;
            conSource.close();
            conSource = null;
            alist.clear();
            delqry = "delete from prg_sec_user_company where COMPANY_ID=" + Integer.parseInt(companyId);
            alist.add(delqry);
            for (int i = 0; i < userName.length; i++) {
                if (conTarget.toString().contains("mysql")) {
                    query = "insert into prg_sec_user_company(COMPANY_ID,PROGEN_USER_ID,PRGOGEN_USER_NAME,APP_USER_NAME) values(" + Integer.parseInt(companyId) + "," + Integer.parseInt(users[i]) + ",'" + userName[i] + "','" + userName[i] + "')";
                } else if (conTarget.toString().contains("sqlserver")) {
                    query = "insert into prg_sec_user_company(COMPANY_ID,PROGEN_USER_ID,PRGOGEN_USER_NAME,APP_USER_NAME) values(" + Integer.parseInt(companyId) + "," + Integer.parseInt(users[i]) + ",'" + userName[i] + "','" + userName[i] + "')";
                } else {
                    query = "insert into prg_sec_user_company(ID,COMPANY_ID,PROGEN_USER_ID,PRGOGEN_USER_NAME,APP_USER_NAME) values(PRG_SEC_USER_COMPANY_SEQ.NEXTVAL," + Integer.parseInt(companyId) + "," + Integer.parseInt(users[i]) + ",'" + userName[i] + "','" + userName[i] + "')";
                }

                alist.add(query);
            }
            for (int i = 0; i < alist.size(); i++) {
                psSetComp = conTarget.prepareStatement(String.valueOf(alist.get(i)));
                flag1 = psSetComp.executeUpdate();
            }
            psSetComp.close();
            psSetComp = null;
            conTarget.close();
            conTarget = null;
            if (flag == 1 && flag1 == 1) {
                flag = 1;
            } else {
                flag = 0;
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return flag;
    }

    public String getCompAvailableUsers() throws SQLException {
        SourceConn sc = new SourceConn();
        Connection conSource = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
        PbReturnObject returnObject = null;
        String getusers = "select PU_ID, PU_LOGIN_ID from PRG_AR_USERS";
        String jsonString = null;
        Gson json = new Gson();
        List<String> compId = new ArrayList<String>();
        List<String> compName = new ArrayList<String>();

        Map dimmap = new HashMap();
        try {
            pstmtForTransterSource = conSource.prepareStatement(getusers);
            resultSet = pstmtForTransterSource.executeQuery();
            returnObject = new PbReturnObject(resultSet);

            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {

                    compId.add(returnObject.getFieldValueString(i, 0));
                    compName.add(returnObject.getFieldValueString(i, 1));
                }
            }
            dimmap.put("compId", compId);
            dimmap.put("compName", compName);
            dimmap.put("rsLength", returnObject.getRowCount());

            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;

    }
    //End user assignment for company..by Nazneen
    //Start DIm and Fact Sec..by Nazneen

    public String getFactTabDetails(String userId, String connectionID, String folderID) {
        String Query = "select distinct table_disp_name, buss_table_id FROM PRG_USER_ALL_INFO_DETAILS  where connection_id=" + connectionID + " and folder_id=" + folderID + " and SUB_FOLDER_NAME='Facts' order by table_disp_name";

        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(Query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='0000'>--SELECT--</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueInt(i, 1) + "'>");
            sb.append(pbro.getFieldValueString(i, 0));
            sb.append("</option>");
        }
        return sb.toString();
    }

    public String getDimMemberDetails(String connectionID, String folderId, String dimId) {
        String finalquery = "";
        StringBuilder sb = new StringBuilder();

        if (dimId.equals("0000")) {
            String query = "SELECT b.GRP_ID,b.FOLDER_ID,b.SUB_FOLDER_ID,b.SUB_FOLDER_TAB_ID,b.ELEMENT_ID,b.BUSS_COL_ID,b.DIM_NAME,"
                    + "b.MEMBER_NAME,b.MEMBER_ID,b.CONNECTION_ID FROM PRG_USER_ALL_INFO_DETAILS  b inner join PRG_USER_ALL_ADIM_KEY_VAL_ELE  p "
                    + " on (b.ELEMENT_ID = p.KEY_ELEMENT_ID) where connection_id in ('&') and FOLDER_ID in ('&') and b.DIM_NAME not in('Time')  order by b.MEMBER_NAME,p.LEVEL1";
            Object obj[] = new Object[2];
            obj[0] = connectionID;
            obj[1] = folderId;
            finalquery = super.buildQuery(query, obj);
        } else {
            String query = "SELECT b.GRP_ID,b.FOLDER_ID,b.SUB_FOLDER_ID,b.SUB_FOLDER_TAB_ID,b.ELEMENT_ID,b.BUSS_COL_ID,b.DIM_NAME,"
                    + "b.MEMBER_NAME,b.MEMBER_ID,b.CONNECTION_ID FROM PRG_USER_ALL_INFO_DETAILS  b inner join PRG_USER_ALL_ADIM_KEY_VAL_ELE  p "
                    + " on (b.ELEMENT_ID = p.KEY_ELEMENT_ID) where connection_id in ('&') and FOLDER_ID in ('&') and DIM_ID in ('&') and b.DIM_NAME not in('Time')  order by b.MEMBER_NAME,p.LEVEL1";
            Object obj[] = new Object[3];
            obj[0] = connectionID;
            obj[1] = folderId;
            obj[2] = dimId;
            finalquery = super.buildQuery(query, obj);
            PbReturnObject pbro = new PbReturnObject();
            try {
                pbro = new PbDb().execSelectSQL(finalquery);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }

            sb.append("<option value='0000'>--SELECT--</option>");
            for (int i = 0; i < pbro.getRowCount(); i++) {
                sb.append("<option value='" + pbro.getFieldValueInt(i, 4) + "'>");
                sb.append(pbro.getFieldValueString(i, 7));
                sb.append("</option>");
            }
        }
        return sb.toString();
    }

    public String getFactMeasureDetails(String userId, String connectionID, String foldersSelected, String tablesSelected) {
        String query = "";
        StringBuilder sb = new StringBuilder();
        if (tablesSelected.equals("0000")) {
            query = "SELECT DISTINCT USER_COL_DESC, ELEMENT_ID,AGGREGATION_TYPE,ACTUAL_COL_FORMULA,BUSS_TABLE_NAME,BUSS_COL_NAME FROM PRG_USER_ALL_INFO_DETAILS   WHERE  CONNECTION_ID  =" + connectionID + " AND FOLDER_ID = " + foldersSelected + " AND BUSS_TABLE_NAME !='PROGEN_TIME' AND IS_FACT ='Y' and USE_REPORT_FLAG='Y' ORDER BY BUSS_COL_NAME,BUSS_TABLE_NAME asc";
        } else {
            query = "SELECT DISTINCT USER_COL_DESC, ELEMENT_ID,AGGREGATION_TYPE,ACTUAL_COL_FORMULA,BUSS_TABLE_NAME,BUSS_COL_NAME FROM PRG_USER_ALL_INFO_DETAILS   WHERE  CONNECTION_ID  =" + connectionID + " AND FOLDER_ID = " + foldersSelected + " AND BUSS_TABLE_ID=" + tablesSelected + " AND BUSS_TABLE_NAME !='PROGEN_TIME' AND IS_FACT ='Y' and USE_REPORT_FLAG='Y' and ref_element_type =1 ORDER BY BUSS_COL_NAME,BUSS_TABLE_NAME asc";
        }

        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        sb.append("<option value='0000'>--SELECT--</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueInt(i, 1) + "'>");
            sb.append(pbro.getFieldValueString(i, 0));
            sb.append("</option>");
        }
        return sb.toString();
    }

    public int applySecurity(String elementId) throws SQLException {
        String query = "";
        int result = 0;
        String secKeyId = "";
        query = "select -1 FOLDER_ID,GRP_ID,BUSS_TABLE_ID,BUSS_COL_ID,BUSS_TABLE_NAME,BUSS_COL_NAME FROM PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID=" + elementId;
        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(query);

            String queryAddSec = null;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//            query = "select LAST_INSERT_ID(SEC_KEY_ID) from PRG_SEC_GRP_ROLE_USER_VAR order by 1 desc limit 1 ";
//            pbro = new PbDb().execSelectSQL(query);
//            int last_secKeyId = pbro.getFieldValueInt(0, 0);
                queryAddSec = "INSERT INTO PRG_SEC_GRP_ROLE_USER_VAR "
                        + "(sec_key_name,folder_id,grp_id,buss_table_id,buss_col_id,buss_table_name,buss_col_name,buss_tab_col_name,sec_clause_1)"
                        + " values ('COMPANY_ID',&,&,&,&,'&','&','&','&')";
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                queryAddSec = "INSERT INTO PRG_SEC_GRP_ROLE_USER_VAR "
                        + "(sec_key_name,folder_id,grp_id,buss_table_id,buss_col_id,buss_table_name,buss_col_name,buss_tab_col_name,sec_clause_1)"
                        + " values ('COMPANY_ID',&,&,&,&,'&','&','&','&')";
            } else {
                queryAddSec = "INSERT INTO PRG_SEC_GRP_ROLE_USER_VAR "
                        + "(sec_key_id,sec_key_name,folder_id,grp_id,buss_table_id,buss_col_id,buss_table_name,buss_col_name,buss_tab_col_name,sec_clause_1)"
                        + " values (PRG_SEC_GRP_ROLE_USER_VAR_SEQ.NEXTVAL,'COMPANY_ID',&,&,&,&,'&','&','&','&')";
            }
            String folder_id = pbro.getFieldValueString(0, 0);
            String GRP_ID = pbro.getFieldValueString(0, 1);
            String BUSS_TABLE_ID = pbro.getFieldValueString(0, 2);
            String BUSS_COL_ID = pbro.getFieldValueString(0, 3);
            String BUSS_TABLE_NAME = pbro.getFieldValueString(0, 4);
            String BUSS_COL_NAME = pbro.getFieldValueString(0, 5);
            String BUSS_TAB_COL_NAME = BUSS_TABLE_NAME + "." + BUSS_COL_NAME;
            String sec_clause_1 = "select distinct COMPANY_ID from PRG_SEC_USER_COMPANY where PROGEN_USER_ID =  @@PROGEN_GBL_VAR@@USER_ID";

            Object inOb[] = new Object[8];
            inOb[0] = folder_id;
            inOb[1] = GRP_ID;
            inOb[2] = BUSS_TABLE_ID;
            inOb[3] = BUSS_COL_ID;
            inOb[4] = BUSS_TABLE_NAME;
            inOb[5] = BUSS_COL_NAME;
            inOb[6] = BUSS_TAB_COL_NAME;
            inOb[7] = sec_clause_1;

            String Query = buildQuery(queryAddSec, inOb);
            Connection conn = ProgenConnection.getInstance().getConnection();
            PreparedStatement prepStmt = conn.prepareStatement(Query);
            result = prepStmt.executeUpdate();
        } catch (SQLException ex) {
            result = 0;
            logger.error("Exception:", ex);
        }
        return result;
    }
    //End DIm and Fact Sec..by Nazneen
    //Added by Nazneen for showing List of Process Running

    public String getProcessListDetails() throws SQLException {
        SourceConn sc = new SourceConn();
        Connection conTarget = sc.getConnection("seaLinkdw", "", "", "", "", "", "", "", "");
        PreparedStatement pstmtForTransterTarget;
        ResultSet resultSet = null;
        PbReturnObject returnObject = null;

        String jsonString = null;
        Gson json = new Gson();
        List<String> processId = new ArrayList<String>();
        List<String> user = new ArrayList<String>();
        List<String> host = new ArrayList<String>();
        List<String> database = new ArrayList<String>();
        List<String> command = new ArrayList<String>();
        List<String> time = new ArrayList<String>();
        List<String> state = new ArrayList<String>();
        List<String> info = new ArrayList<String>();

        Map dimmap = new HashMap();
        String finalquery = "";

        String Query = "SHOW FULL PROCESSLIST ";
        try {
            pstmtForTransterTarget = conTarget.prepareStatement(Query);
            resultSet = pstmtForTransterTarget.executeQuery();
            returnObject = new PbReturnObject(resultSet);
            StringBuilder sb = new StringBuilder();

            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    processId.add(returnObject.getFieldValueString(i, 0));
                    user.add(returnObject.getFieldValueString(i, 1));
                    host.add(returnObject.getFieldValueString(i, 2));
                    database.add(returnObject.getFieldValueString(i, 3));
                    command.add(returnObject.getFieldValueString(i, 4));
                    time.add(returnObject.getFieldValueString(i, 5));
                    state.add(returnObject.getFieldValueString(i, 6));
                    info.add(returnObject.getFieldValueString(i, 7));

                }
            }
            dimmap.put("processId", processId);
            dimmap.put("user", user);
            dimmap.put("host", host);
            dimmap.put("database", database);
            dimmap.put("command", command);
            dimmap.put("time", time);
            dimmap.put("state", state);
            dimmap.put("info", info);
            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        return jsonString;

    }

    public int killProcess(String processId) throws SQLException {
        SourceConn sc = new SourceConn();
        Connection conn = sc.getConnection("seaLinkdw", "", "", "", "", "", "", "", "");
        PreparedStatement pstmtForTransfer;
        int flag = 0;
//        PbDb pbdb = new PbDb();
        String queryKillProcess = "kill ";
        Object inOb[] = new Object[1];
        inOb[0] = processId;

        String Query = buildQuery(queryKillProcess, inOb);
        if (conn != null) {
            try {
//            flag = pbdb.execUpdateSQL(Query,conn);
                pstmtForTransfer = conn.prepareStatement(Query);
                flag = pstmtForTransfer.executeUpdate();

            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        if (conn != null) {
            conn = null;
        }

        return flag;
    }

    public String getUsersDetails() {
        String Query = "select PU_ID, PU_LOGIN_ID from PRG_AR_USERS";

        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(Query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='0000'>--All--</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueInt(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 1));
            sb.append("</option>");
        }
        return sb.toString();
    }

    public String getMemDetails(String elementId) throws SQLException {
        SourceConn sc = new SourceConn();
        Connection conSource = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
        PbReturnObject returnObject = null;
//        String query="SELECT b.SUB_FOLDER_ID,b.MEMBER_NAME,b.ELEMENT_ID FROM PRG_USER_ALL_INFO_DETAILS  b inner join PRG_USER_ALL_ADIM_KEY_VAL_ELE  p "
//              + " on (b.ELEMENT_ID = p.KEY_ELEMENT_ID) "
//              + " where connection_id = " + connectionID
//              + " and FOLDER_ID = " + folderId
//              + " and DIM_ID = " + dimId
//              + " AND MEMBER_ID = " + memId
//              + " and b.DIM_NAME not in('Time')";
        String query = "SELECT b.SUB_FOLDER_ID,b.MEMBER_NAME,b.MEMBER_ID FROM PRG_USER_ALL_INFO_DETAILS  b inner join PRG_USER_ALL_ADIM_KEY_VAL_ELE  p "
                + " on (b.ELEMENT_ID = p.KEY_ELEMENT_ID) "
                + " where ELEMENT_ID = " + elementId
                + " and b.DIM_NAME not in('Time')";

        String jsonString = null;
        Gson json = new Gson();
        List<String> subFolderId = new ArrayList<String>();
        List<String> memberName = new ArrayList<String>();
//       List<String>elementID=new ArrayList<String>();
        List<String> memberID = new ArrayList<String>();

        Map dimmap = new HashMap();
        try {
            pstmtForTransterSource = conSource.prepareStatement(query);
            resultSet = pstmtForTransterSource.executeQuery();
            returnObject = new PbReturnObject(resultSet);

            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    //
                    subFolderId.add(returnObject.getFieldValueString(i, 0));
                    memberName.add(returnObject.getFieldValueString(i, 1));
//                  elementID.add(returnObject.getFieldValueString(i,2));
                    memberID.add(returnObject.getFieldValueString(i, 2));

                }
            }
            dimmap.put("subFolderId", subFolderId);
            dimmap.put("memberName", memberName);
//            dimmap.put("elementID",elementID);
            dimmap.put("memberID", memberID);

            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;

    }

    public String getMembersForDimensionDragAndDrop(String roleID, String memberId, String contextPath, String userId, String subFolderIdUser) {
        DisplayParameters dispParam = new DisplayParameters();
        PbUserLayerResourceBundle resBundle = new PbUserLayerResourceBundle();
        ArrayList<String> dimensionMembers = new ArrayList<String>();
        ArrayList<String> assignedDimensionMembers = new ArrayList<String>();
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
//        PreparedStatement pstmtForTransterSource1;
//        ResultSet resultSet1 = null;

        String finalQuery = "";
        String getMemberDetails = resBundle.getString("getMemberDetails");
        Object memberDetails[] = new Object[3];
        memberDetails[0] = roleID;
        memberDetails[1] = roleID;
        memberDetails[2] = memberId;
        finalQuery = buildQuery(getMemberDetails, memberDetails);
        PbReturnObject memberDetailsObject = new PbReturnObject();
        try {
            memberDetailsObject = super.execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        String divhtml = "";
        if (memberDetailsObject.getRowCount() > 0) {
            String busTableName = memberDetailsObject.getFieldValueString(0, "BUSS_TABLE_NAME");
            String elementId = memberDetailsObject.getFieldValueString(0, "KEY_ELEMENT_ID");
            String query = dispParam.getParameterQuery(elementId);

            finalQuery = query + " order by 1 asc";
            Connection connection = ProgenConnection.getInstance().getConnectionForElement(elementId);
            PbReturnObject pbReturnObject = new PbReturnObject();
            try {
                pstmtForTransterSource = connection.prepareStatement(finalQuery);
                resultSet = pstmtForTransterSource.executeQuery();
                pbReturnObject = new PbReturnObject(resultSet);
//            pbReturnObject = execSelectSQL(finalQuery, connection);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            if (pbReturnObject.getRowCount() > 0) {
                for (int i = 0; i < pbReturnObject.getRowCount(); i++) {
                    dimensionMembers.add(pbReturnObject.getFieldValueString(i, 0));
                }
            }
        }
//         String finalQuery1 = "SELECT MEMBER_VALUE from prg_user_role_member_filter where user_id = " + userId + " and element_id="+elementID;
        String finalQuery1 = "select MEMBER_VALUE from prg_user_role_member_filter where user_id=" + userId + " and folder_id in(select folder_id from prg_user_folder_detail where sub_folder_id=" + subFolderIdUser + ") and member_id=" + memberId;;
        PbReturnObject pbReturnObject1 = new PbReturnObject();
        try {
            pbReturnObject1 = super.execSelectSQL(finalQuery1);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (pbReturnObject1 != null && pbReturnObject1.getRowCount() > 0) {
            String str = pbReturnObject1.getFieldUnknown(0, 0).replace("'", "");
            String assignedValStr[] = str.split(",");
            for (int i = 0; i < assignedValStr.length; i++) {
                assignedDimensionMembers.add(assignedValStr[i]);
            }
        }

        GenerateDragAndDrophtml html = new GenerateDragAndDrophtml("Select Columns from below", "Drag Columns to here", assignedDimensionMembers, dimensionMembers, contextPath);
        html.setDragableListNames(dimensionMembers);
        divhtml = html.getDragAndDropDiv();

        return divhtml;
    }
    //added for creating empty role

    public boolean addUserFolderEmpty(String folderName, String folderDesc, String grpId) {
        ArrayList alist = new ArrayList();
        String addUserFolderQuery = getResourceBundle().getString("addUserFolder");
        String finalQuery = "";
        boolean result = false;

        Object[] Obj = new Object[3];
        Obj[0] = folderName;
        Obj[1] = folderDesc;
        Obj[2] = grpId;
        try {
            //new ProgenConnection().getConnection();
            finalQuery = buildQuery(addUserFolderQuery, Obj);
            alist.add(finalQuery);
            alist = addUserFolderDetailsEmpty(alist, grpId);
//            
//            for(int ialist=0;ialist<alist.size();ialist++){
//                
//            execModifySQL((String) alist.get(ialist));
//            }
            result = executeMultiple(alist);
            //code to change formula ids to element id
            return result;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return result;
        }
    }

    /*
     * This method is for inserting a records into user folder details and
     * returns : ArrayList which contains queries to be executed
     */
    public ArrayList addUserFolderDetailsEmpty(ArrayList alist, String grpId) throws Exception {
        String addUserFolderDetailsQuery = getResourceBundle().getString("addUserFolderDetails");

        String[] folderDetailNames = {"Dimensions", "Facts", "Buckets"};
        Object[] Obj = null;
        int foldDtlId = 0;
        Obj = new Object[3];

        String finalQuery = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            foldDtlId = this.getCurrentSeqSqlServer("PRG_USER_FOLDER_DETAIL");
//            this.subFolderTabId = this.getCurrentSeqSqlServer("PRG_USER_SUB_FOLDER_TABLES");
//            this.subFolderEleId = this.getCurrentSeqSqlServer("PRG_USER_SUB_FLDR_ELEMENTS");
        }
        for (int index = 0; index < folderDetailNames.length; index++) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                Obj[0] = "IDENT_CURRENT('PRG_USER_FOLDER')";
                Obj[1] = folderDetailNames[index];
                Obj[2] = folderDetailNames[index];
                foldDtlId += index + 1;
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                Obj[0] = "(select Last_Insert_Id(folder_id) from PRG_USER_FOLDER order by 1 desc limit 1)";
                Obj[1] = folderDetailNames[index];
                Obj[2] = folderDetailNames[index];
                foldDtlId += index + 1;
            } else {
                foldDtlId = getSequenceNumber("select PRG_USER_FOLDER_DETAIL_SEQ.NEXTVAL from dual");
                Obj[0] = String.valueOf(foldDtlId);
                Obj[1] = folderDetailNames[index];
                Obj[2] = folderDetailNames[index];
            }

            finalQuery = buildQuery(addUserFolderDetailsQuery, Obj);
            alist.add(finalQuery);
        }
        return alist;

    }

    public String getGroupsDetails(String connectionID) {
        String Query = " select grp_id, grp_name  from prg_grp_master where connection_id in (" + connectionID + ") ";
        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(Query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value ='0000'>--SELECT--</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueInt(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 1));
            sb.append("</option>");
        }
        return sb.toString();
    }

    public String getRolesDetails(String groupId) throws SQLException {
        String Query = " select FOLDER_ID,FOLDER_NAME,ISPUBLISHED,FOLDER_DESC from PRG_USER_FOLDER where GRP_ID =" + groupId;
        PbReturnObject pbro = new PbReturnObject();
        String jsonString = null;
        Gson json = new Gson();
        List<String> folderId = new ArrayList<String>();
        List<String> folderName = new ArrayList<String>();
        List<String> isPublished = new ArrayList<String>();
        List<String> folderDesc = new ArrayList<String>();

        Map dimmap = new HashMap();
        try {
            pbro = new PbDb().execSelectSQL(Query);
            if (pbro != null) {
                for (int i = 0; i < pbro.getRowCount(); i++) {
                    folderId.add(pbro.getFieldValueString(i, 0));
                    folderName.add(pbro.getFieldValueString(i, 1));
                    isPublished.add(pbro.getFieldValueString(i, 2));
                    folderDesc.add(pbro.getFieldValueString(i, 3));
                }
            }
            dimmap.put("folderId", folderId);
            dimmap.put("folderName", folderName);
            dimmap.put("isPublished", isPublished);
            dimmap.put("folderDesc", folderDesc);
            jsonString = json.toJson(dimmap);

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;

    }

    public boolean publishBussinessRole(String folderId) {
        boolean result = false;
        String getinsertInUserAllInfoDetails = getResourceBundle().getString("insertPublishInUserAllInfoDetails");
        String getinsertInAllAdinDetails = getResourceBundle().getString("insertPublishInAllAdimDetails");
        String getinsertInAllAdimKeyValEle = getResourceBundle().getString("insertPublishInAllAdimKeyValEle");
        String getinsertInAllAdimMaster = getResourceBundle().getString("insertPublishInAllAdimMaster");
        String getinsertInAllDdimDetails = getResourceBundle().getString("insertPublishInAllDdimDetails");
        String getinsertInAllDdimKeyValEle = getResourceBundle().getString("insertPublishInAllDdimKeyValEle");
        String getinsertInUserAllDdimMaster = getResourceBundle().getString("insertPublishInUserAllDdimMaster");
        String getUpdateInUserFolder = getResourceBundle().getString("UpdateInUserFolder");

        ArrayList insertQueriesForPublish = new ArrayList();
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String finalQuery = "";
        try {
            //new ProgenConnection().getConnection();

            Obj = new Object[1];
            Obj[0] = folderId;
            finalQuery = buildQuery(getinsertInUserAllInfoDetails, Obj);
            insertQueriesForPublish.add(finalQuery);

            Obj[0] = folderId;
            finalQuery = buildQuery(getinsertInAllAdinDetails, Obj);
            insertQueriesForPublish.add(finalQuery);

            Obj[0] = folderId;
            finalQuery = buildQuery(getinsertInAllAdimKeyValEle, Obj);
            insertQueriesForPublish.add(finalQuery);

            Obj[0] = folderId;
            finalQuery = buildQuery(getinsertInAllAdimMaster, Obj);
            insertQueriesForPublish.add(finalQuery);

            Obj[0] = folderId;
            finalQuery = buildQuery(getinsertInAllDdimDetails, Obj);
            insertQueriesForPublish.add(finalQuery);

            Obj[0] = folderId;
            finalQuery = buildQuery(getinsertInAllDdimKeyValEle, Obj);
            insertQueriesForPublish.add(finalQuery);

            Obj[0] = folderId;
            finalQuery = buildQuery(getinsertInUserAllDdimMaster, Obj);
            insertQueriesForPublish.add(finalQuery);

            Obj[0] = folderId;
            finalQuery = buildQuery(getUpdateInUserFolder, Obj);
            insertQueriesForPublish.add(finalQuery);

            result = executeMultiple(insertQueriesForPublish);
            return result;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return result;
        }
    }

    public int updateRoleDetails(String roleName, String roleDesc, String folderId) throws SQLException {
        String getUpdateRoleDetails = getResourceBundle().getString("UpdateRoleDetails");
        ArrayList insertQueriesForRoleDetails = new ArrayList();
        PbReturnObject retObj = null;
        Object[] Obj = new Object[3];
        Obj[0] = roleName;
        Obj[1] = roleDesc;
        Obj[2] = folderId;
        String finalQuery = "";
        boolean result = false;
        PbReturnObject pbro = new PbReturnObject();

        finalQuery = buildQuery(getUpdateRoleDetails, Obj);
        insertQueriesForRoleDetails.add(finalQuery);
        result = executeMultiple(insertQueriesForRoleDetails);
        if (result == true) {
            return 1;
        } else {
            return 0;
        }

    }

    public boolean rePublishBussinessRole(String folderId) {
        boolean result = false;
        int rs = 0;
        String deleteUserAllInfoDetails = getResourceBundle().getString("deleteUserAllInfoDetails");
        String deleteUserAllDDIMKeyValEle = getResourceBundle().getString("deleteUserAllDDIMKeyValEle");
        String deleteUserAllADIMKeyValEle = getResourceBundle().getString("deleteUserAllADIMKeyValEle");
        String deleteUserAllDDIMDetails = getResourceBundle().getString("deleteUserAllDDIMDetails");
        String deleteUserAllDDIMMaster = getResourceBundle().getString("deleteUserAllDDIMMaster");
        String deleteUserAllADIMDetails = getResourceBundle().getString("deleteUserAllADIMDetails");
        String deleteUserAllADIMMaster = getResourceBundle().getString("deleteUserAllADIMMaster");
//       String addUserAllInfoDetails="";
//       if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)){
//            addUserAllInfoDetails=getResourceBundle().getString("addUserAllInfoDetailsMysql");
//       }else{
//            addUserAllInfoDetails=getResourceBundle().getString("addRoleUserAllInfoDetails");
//       }
//       String addUserAllInfoDetails=getResourceBundle().getString("insertPublishInUserAllInfoDetails");
//       String addUserAllDDIMDetails=getResourceBundle().getString("insertPublishInAllDdimDetails");
//       String addUserAllDDIMMaster=getResourceBundle().getString("insertPublishInUserAllDdimMaster");
//       String addUserAllDDIMKeyValEle=getResourceBundle().getString("insertPublishInAllDdimKeyValEle");
//       String addUserAllADIMDetails=getResourceBundle().getString("insertPublishInAllAdimDetails");
//       String addUserAllAdimMaster=getResourceBundle().getString("insertPublishInAllAdimMaster");
//       String addUserAllADIMKeyValEle=getResourceBundle().getString("insertPublishInAllAdimKeyValEle");
        String getinsertInUserAllInfoDetails = getResourceBundle().getString("insertPublishInUserAllInfoDetails");
        String getinsertInAllAdinDetails = getResourceBundle().getString("insertPublishInAllAdimDetails");
        String getinsertInAllAdimKeyValEle = getResourceBundle().getString("insertPublishInAllAdimKeyValEle");
        String getinsertInAllAdimMaster = getResourceBundle().getString("insertPublishInAllAdimMaster");
        String getinsertInAllDdimDetails = getResourceBundle().getString("insertPublishInAllDdimDetails");
        String getinsertInAllDdimKeyValEle = getResourceBundle().getString("insertPublishInAllDdimKeyValEle");
        String getinsertInUserAllDdimMaster = getResourceBundle().getString("insertPublishInUserAllDdimMaster");

        ArrayList deleteUserFolderQueries = new ArrayList();

        PbReturnObject retObj = null;
        Object[] Obj = null;
        Object[] Obj1 = null;
        String finalQuery = "";
        Obj = new Object[1];
        Obj[0] = folderId;
        try {
            finalQuery = buildQuery(deleteUserAllInfoDetails, Obj);
            deleteUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllDDIMKeyValEle, Obj);
            deleteUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllADIMKeyValEle, Obj);
            deleteUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllDDIMDetails, Obj);
            deleteUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllDDIMMaster, Obj);
            deleteUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllADIMDetails, Obj);
            deleteUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllADIMMaster, Obj);
            deleteUserFolderQueries.add(finalQuery);
            executeMultiple(deleteUserFolderQueries);

            Obj1 = new Object[2];
//            Obj1[0]=folderId;
//            Obj1[1]=folderId;

            ArrayList insertQueriesForPublish = new ArrayList();
            Obj[0] = folderId;
            finalQuery = buildQuery(getinsertInUserAllInfoDetails, Obj);
            insertQueriesForPublish.add(finalQuery);

            Obj[0] = folderId;
            finalQuery = buildQuery(getinsertInAllAdinDetails, Obj);
            insertQueriesForPublish.add(finalQuery);

            Obj[0] = folderId;
            finalQuery = buildQuery(getinsertInAllAdimKeyValEle, Obj);
            insertQueriesForPublish.add(finalQuery);

            Obj[0] = folderId;
            finalQuery = buildQuery(getinsertInAllAdimMaster, Obj);
            insertQueriesForPublish.add(finalQuery);

            Obj[0] = folderId;
            finalQuery = buildQuery(getinsertInAllDdimDetails, Obj);
            insertQueriesForPublish.add(finalQuery);

            Obj[0] = folderId;
            finalQuery = buildQuery(getinsertInAllDdimKeyValEle, Obj);
            insertQueriesForPublish.add(finalQuery);

            Obj[0] = folderId;
            finalQuery = buildQuery(getinsertInUserAllDdimMaster, Obj);
            insertQueriesForPublish.add(finalQuery);

            Obj1[0] = "Y";
            Obj1[1] = folderId;
            String addupdateFolderPublishment = getResourceBundle().getString("updateFolderPublishment");
            finalQuery = buildQuery(addupdateFolderPublishment, Obj1);
            insertQueriesForPublish.add(finalQuery);
            result = executeMultiple(insertQueriesForPublish);

//             String getCalculatedFormulas=getResourceBundle().getString("getCalculatedFormulas");
//             finalQuery=buildQuery(getCalculatedFormulas, Obj);
//             //////////////////////////////////////////////////////////////////////////////.println.println("final getCalculatedFormulas---"+finalQuery);
//             PbReturnObject pbro=execSelectSQL(finalQuery);
//             String refElements="";
//             String ActFormula="";
//             String mainEleId="";
//             String bussTableName="";
//           ArrayList updateQueries = new ArrayList();
//             for(int i=0;i<pbro.getRowCount();i++){
//                refElements=pbro.getFieldValueString(i,"REFFERED_ELEMENTS");
//                ActFormula=pbro.getFieldValueString(i, "ACTUAL_COL_FORMULA");
//                mainEleId=String.valueOf(pbro.getFieldValueInt(i,"ELEMENT_ID"));
//                bussTableName=String.valueOf(pbro.getFieldValueString(i,"BUSS_TABLE_NAME"));
//                 //////////////////////////////////////////////////////////////////////////.println.println("refElements--"+refElements);
//                 //////////////////////////////////////////////////////////////////////////.println.println("ActFormula--"+ActFormula);
//                 if(!refElements.equalsIgnoreCase("")){
//                 String refEleList[]=refElements.split(",");
//                 for(int j=0;j<refEleList.length;j++){
//                 if (refEleList[j].startsWith("M-")) {
//                     String detlist[]=refEleList[j].substring(2).split("-");
//                      if(detlist.length>=3){
//                     String bussTableId=detlist[0];
//                     String bussColId=detlist[1];
//                     String memId=detlist[2];
//                     String eleIdQuery=" select element_id from prg_user_all_info_details where  buss_table_id="+bussTableId+" and buss_col_id="+bussColId+" and  member_id="+memId +" and  folder_id="+folderId;
//                     //////////////////////////////////////////////////////////////////////////////.println.println("elementQuery---"+eleIdQuery);
//                     PbReturnObject elementIdpbro=execSelectSQL(eleIdQuery);
//                     //////////////////////////////////////////////////////////////////////////////.println.println("eleId--"+elementIdpbro.getFieldValueInt(0,0));
//                     ActFormula=ActFormula.replace(refEleList[j],String.valueOf(elementIdpbro.getFieldValueInt(0,0)));
//                     refElements=refElements.replace(refEleList[j],String.valueOf(elementIdpbro.getFieldValueInt(0,0)));
//                      }
//
//                } else {
//                      //////////////////////////////////////////////////////////////////////////////.println.println("IN ELSE PART");
//
//                     String detlist[]=refEleList[j].split("-");
//                     if(detlist.length>=2){
//                     String bussTableId=detlist[0];
//                     String bussColId=detlist[1];
//
//                     String eleIdQuery=" select element_id from prg_user_all_info_details where  buss_table_id="+bussTableId+" and buss_col_id="+bussColId+" and SUB_FOLDER_TYPE='Facts'  and  folder_id="+folderId;
//                     //////////////////////////////////////////////////////////////////////////////.println.println("elementQuery---"+eleIdQuery);
//                     PbReturnObject elementIdpbro=execSelectSQL(eleIdQuery);
//                     //////////////////////////////////////////////////////////////////////////////.println.println("eleId--"+elementIdpbro.getFieldValueInt(0,0));
//                     refElements=refElements.replace(refEleList[j],String.valueOf(elementIdpbro.getFieldValueInt(0,0)));
//                 }else{
//
//                     String bussTableId=String.valueOf(pbro.getFieldValueInt(i,"BUSS_TABLE_ID"));
//                     String bussColId=detlist[0];
//
//                     String eleIdQuery=" select element_id from prg_user_all_info_details where  buss_table_id="+bussTableId+" and buss_col_id="+bussColId+" and SUB_FOLDER_TYPE='Facts'  and  folder_id="+folderId;
//                     //////////////////////////////////////////////////////////////////////////////.println.println("elementQuery---"+eleIdQuery);
//                     PbReturnObject elementIdpbro=execSelectSQL(eleIdQuery);
//                     //////////////////////////////////////////////////////////////////////////////.println.println("eleId--"+elementIdpbro.getFieldValueInt(0,0));
//                     refElements=refElements.replace(refEleList[j],String.valueOf(elementIdpbro.getFieldValueInt(0,0)));
//                 }
//                 }
//                 }
//
//                   if(bussTableName.equalsIgnoreCase("Calculated Facts")){
//                       ////.println("vfbvnjhmjk,.lk/'");
//
//                   String updateQuery=" update PRG_USER_ALL_INFO_DETAILS set REFFERED_ELEMENTS='"+refElements+"', ACTUAL_COL_FORMULA='"+ActFormula +"',BUSS_TABLE_ID=0,BUSS_COL_ID=0,buss_table_name=null  where element_id="+mainEleId;
//                   updateQueries.add(updateQuery);
//                   }else{
//                     String updateQuery=" update PRG_USER_ALL_INFO_DETAILS set REFFERED_ELEMENTS='"+refElements+"', ACTUAL_COL_FORMULA='"+ActFormula +"'  where element_id="+mainEleId;
//                    updateQueries.add(updateQuery);
//                   }
//                   //////////////////////////////////////////////////////////////////////////.println.println("updateQuery--"+updateQuery);
//                     //////////////////////////////////////////////////////////////////////////.println.println("---------------------------------");
//
//
//
//             }
//             }
//            //////////////////////////////////////////////////////////////////////////////.println.println("------------------------------------------------------------------------------");
//           String getFolderLevelColFormulas="";
//           if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)){
//                getFolderLevelColFormulas=getResourceBundle().getString("getFolderLevelColFormulasMysql");
//           }else{
//               getFolderLevelColFormulas=getResourceBundle().getString("getFolderLevelColFormulas");
//           }
//           finalQuery=buildQuery(getFolderLevelColFormulas, Obj);
//             //////////////////////////////////////////////////////////////////////////////.println.println("---------getFolderLevelColFormulas------------->"+finalQuery);
//             //////////////////////////////////////////////////////////////////////////////.println.println("------------------------------------------------------------------------------");
//            updateQueries.add(finalQuery);
//
//             result=executeMultiple(updateQueries);
            return result;

        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return result;
        }
    }

    public String collectPlanData(String connId, String procName) throws SQLException {
//      boolean result = true;
        String result = "false";
        boolean finalResult = false;
        //
        Connection conn = null;
        PreparedStatement pstmtForTransterSource1 = null;
        PreparedStatement pstmtForTransterSource2 = null;
        ResultSet resultSet1 = null;
        ResultSet resultSet2 = null;
        try {
            conn = ProgenConnection.getInstance().getConnectionByConId(connId);
            String query1 = "select transaction_id from drl_transaction_load where table_name='drl_stg' ";
            String query2 = "select max(plant_id) from drl_cto_cmp_stg_prod ";
            PbReturnObject pbro1 = new PbReturnObject();
            PbReturnObject pbro2 = new PbReturnObject();
            try {
                pstmtForTransterSource1 = conn.prepareStatement(query1);
                resultSet1 = pstmtForTransterSource1.executeQuery();
                pbro1 = new PbReturnObject(resultSet1);
                pstmtForTransterSource2 = conn.prepareStatement(query2);
                resultSet2 = pstmtForTransterSource2.executeQuery();
                pbro2 = new PbReturnObject(resultSet2);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            String transId1 = pbro1.getFieldValueString(0, 0);
            String tempId = pbro2.getFieldValueString(0, 0);
            String transId2 = null;
            if (procName.equalsIgnoreCase("drl_month_load")) {
                CallableStatement proc = null;
                proc = conn.prepareCall("{ call " + procName + "() }");
                finalResult = proc.execute();
                result = "true";
                proc.close();
            } else {

                if (tempId.contains(".")) {
                    int val = tempId.indexOf(".");
                    transId2 = tempId.substring(0, val);
                } else {
                    transId2 = tempId;
                }
                if (transId1.equalsIgnoreCase(transId2)) {
                    result = "noRecords";
                } else {
                    CallableStatement proc = null;
                    proc = conn.prepareCall("{ call " + procName + "() }");
                    finalResult = proc.execute();
                    result = "true";
                    proc.close();
                }
            }

            resultSet1 = null;
            resultSet2 = null;
            conn.close();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return result;
    }

    public String getcheckIsCollected(String connId, String plantCode, String planScenario, String planRunDate, String procName) throws SQLException {

        String result = "false";
        Connection conn = null;
        CallableStatement callableStatement = null;
        String insertStoreProc = "{call drl_master_load(?,?,?)}";
        // 
        try {
            conn = ProgenConnection.getInstance().getConnectionByConId(connId);
            callableStatement = conn.prepareCall(insertStoreProc);
            callableStatement.setString(1, plantCode);
            callableStatement.setString(2, planScenario);
            callableStatement.setString(3, planRunDate);
            callableStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Exception", e);

        } finally {

            if (callableStatement != null) {
                callableStatement.close();
            }

            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    public String getPlantCodeDetails(String connId) {
        String Query = "";
        Connection conn = null;
        PreparedStatement pstmtForTransterSource = null;
        ResultSet resultSet = null;

        conn = ProgenConnection.getInstance().getConnectionByConId(connId);
//        String query = "SELECT DISTINCT PLANT FROM drl_planning_scenario_version_dim";
        String query = "SELECT DISTINCT PLANT FROM drl_plan_period_dim";
        PbReturnObject pbro = new PbReturnObject();
        try {
            pstmtForTransterSource = conn.prepareStatement(query);
            resultSet = pstmtForTransterSource.executeQuery();
            pbro = new PbReturnObject(resultSet);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='0000'>--- SELECT ---</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueString(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 0));
            sb.append("</option>");
        }
        return sb.toString();
    }

    public String getPlantCodeDetails11(String connId) {
        String Query = "";
        Connection conn = null;
        PreparedStatement pstmtForTransterSource = null;
        ResultSet resultSet = null;

        conn = ProgenConnection.getInstance().getConnectionByConId(connId);
//        String query = "SELECT DISTINCT PLANT FROM drl_planning_scenario_version_dim";
        String query = "SELECT DISTINCT PLANT FROM drl_cto_cmp_stg_master_prod";
        PbReturnObject pbro = new PbReturnObject();
        try {
            pstmtForTransterSource = conn.prepareStatement(query);
            resultSet = pstmtForTransterSource.executeQuery();
            pbro = new PbReturnObject(resultSet);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='0000'>--- SELECT ---</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueString(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 0));
            sb.append("</option>");
        }
        return sb.toString();
    }

    public String getPlantPeriodDetails(String connId, String plantCode, String planScenario) {
        String Query = "";
        Connection conn = null;
        PreparedStatement pstmtForTransterSource = null;
        ResultSet resultSet = null;

        conn = ProgenConnection.getInstance().getConnectionByConId(connId);
//        String query = "SELECT DISTINCT plan_period FROM drl_planning_scenario_version_dim where PLANT ="+plantCode;
        String query = "SELECT DISTINCT plan_period FROM drl_plan_period_dim where PLANT =" + plantCode + ""
                + " AND planning_scenario = " + planScenario;
        PbReturnObject pbro = new PbReturnObject();
        try {
            pstmtForTransterSource = conn.prepareStatement(query);
            resultSet = pstmtForTransterSource.executeQuery();
            pbro = new PbReturnObject(resultSet);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='0000'>--- SELECT ---</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueString(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 0));
            sb.append("</option>");
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return sb.toString();
    }

    public String getPlantPeriodDetails11(String connId, String plantCode, String planScenario) {
        String Query = "";
        Connection conn = null;
        PreparedStatement pstmtForTransterSource = null;
        ResultSet resultSet = null;

        conn = ProgenConnection.getInstance().getConnectionByConId(connId);
//        String query = "SELECT DISTINCT plan_period FROM drl_planning_scenario_version_dim where PLANT ="+plantCode;
        String query = "SELECT DISTINCT plan_run_date FROM drl_cto_cmp_stg_master_prod where PLANT =" + plantCode + ""
                + " AND planning_scenario = " + planScenario;

        // 
        PbReturnObject pbro = new PbReturnObject();
        try {
            pstmtForTransterSource = conn.prepareStatement(query);
            resultSet = pstmtForTransterSource.executeQuery();
            pbro = new PbReturnObject(resultSet);

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();

        sb.append("<option value='0000'>--- SELECT ---</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueString(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 0));
            sb.append("</option>");

        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return sb.toString();
    }

    public String getPlanVersionDetails(String connId, String plantCode, String plantPeriod, String planScenario) {
        String Query = "";
        Connection conn = null;
        PreparedStatement pstmtForTransterSource = null;
        ResultSet resultSet = null;

        conn = ProgenConnection.getInstance().getConnectionByConId(connId);
//        String query = "SELECT DISTINCT plan_version_name FROM drl_planning_scenario_version_dim where plant = " + plantCode + " AND plan_period ='" + plantPeriod+"'";
        String query = "SELECT DISTINCT plan_version FROM drl_plan_period_dim where plant = " + plantCode + ""
                + " AND plan_period ='" + plantPeriod + "' "
                + " AND planning_scenario = " + planScenario;
        PbReturnObject pbro = new PbReturnObject();
        try {
            pstmtForTransterSource = conn.prepareStatement(query);
            resultSet = pstmtForTransterSource.executeQuery();
            pbro = new PbReturnObject(resultSet);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='0000'>--- SELECT ---</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueString(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 0));
            sb.append("</option>");
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return sb.toString();
    }

    public String getcheckIsPublished(String connId, String plantCode, String plantPeriod, String planVersion, String planScenario) throws SQLException {
        SourceConn sc = new SourceConn();
        Connection conSource = null;
        conSource = ProgenConnection.getInstance().getConnectionByConId(connId);
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
        PbReturnObject returnObject = null;

        String jsonString = null;
        Gson json = new Gson();
        List<String> isPublished = new ArrayList<String>();
        Map dimmap = new HashMap();
        String finalquery = "";

//       String Query="SELECT status from drl_planning_scenario_version_dim where plant = " + plantCode + " AND plan_period ='" + plantPeriod + "' AND plan_version_name ='" +planVersion+"'";
        String Query = "SELECT plan_status from drl_plan_period_dim where plant = " + plantCode + " "
                + " AND plan_period ='" + plantPeriod + "' "
                + " AND plan_version ='" + planVersion + "' "
                + " AND planning_scenario = " + planScenario;
        try {
            pstmtForTransterSource = conSource.prepareStatement(Query);
            resultSet = pstmtForTransterSource.executeQuery();
            returnObject = new PbReturnObject(resultSet);
            StringBuilder sb = new StringBuilder();

            if (returnObject != null) {
                if (returnObject.getFieldValueString(0, 0).equals("") || returnObject.getFieldValueString(0, 0) == null) {
                    isPublished.add("N");
                } else {
                    isPublished.add(returnObject.getFieldValueString(0, 0).toUpperCase());
                }

                dimmap.put("isPublished", isPublished);
            }
            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        try {
            if (conSource != null) {
                conSource.close();
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;

    }

    public boolean publishPlan(String connId, String plantCode, String plantPeriod, String planVersion, String planScenario) throws SQLException {
        SourceConn sc = new SourceConn();
        Connection conSource = null;
        conSource = ProgenConnection.getInstance().getConnectionByConId(connId);
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
        PbReturnObject pbro = new PbReturnObject();
        PbReturnObject returnObject = null;

        String getPlanPropName = "select plan_property2 from drl_plan_period_dim where plant= ?" 
                + " AND plan_period ='?'  AND plan_version ='?'   AND planning_scenario = ?" ;
        pstmtForTransterSource = conSource.prepareStatement(getPlanPropName);
        pstmtForTransterSource.setString(1, plantCode);
        pstmtForTransterSource.setString(2, plantPeriod );
        pstmtForTransterSource.setString(3,  planVersion );
        pstmtForTransterSource.setString(4  , planScenario);
        resultSet = pstmtForTransterSource.executeQuery();
        pbro = new PbReturnObject(resultSet);
        String planProperty = pbro.getFieldValueString(0, 0);

        String Query = "update drl_plan_period_dim set   plan_status = 'Y'  ,plan_property2 = '?-P'"
                + " where plant=  ?  AND plan_period ='?' AND plan_version ='?'  AND planning_scenario = ?" ;

        PbReturnObject retObj = null;
        Object[] Obj = new Object[4];
        Obj[0] = plantCode;
        Obj[1] = plantPeriod;
        Obj[2] = planVersion;
        Obj[3] = planScenario;
//        String finalQuery = "";
        int flag = 0;
        boolean result = false;
        pstmtForTransterSource = conSource.prepareStatement(Query);
        pstmtForTransterSource.setString(1, planProperty);
        pstmtForTransterSource.setString(2, plantCode);
        pstmtForTransterSource.setString(3, plantPeriod);
        pstmtForTransterSource.setString(4, planVersion);
        pstmtForTransterSource.setString(5, planScenario);
        flag = pstmtForTransterSource.executeUpdate();
        if (flag == 1) {
            result = true;
        }
        return result;
    }

    //end of code by Nazneen
//added by Nazneen for segmentation
    public String getElementIdDetails(String elementId) throws SQLException {
        SourceConn sc = new SourceConn();
        Connection conSource = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransterSource;
        ResultSet resultSet = null;
        PbReturnObject returnObject = null;
        String str = "";
//        String getusers = "SELECT a.buss_col_id,a.grp_id,a.disp_name,a.buss_table_id,a.buss_col_name,a.user_col_type "
//                + " FROM prg_user_all_info_details a WHERE a.element_id      IN ("+elementId+")";
        String getusers = "SELECT a.buss_col_id,a.grp_id,a.buss_table_name,a.buss_table_id,a.buss_col_name,a.user_col_type,a.REFFERED_ELEMENTS "
                + " FROM prg_user_all_info_details a WHERE a.element_id   IN (" + elementId + ")";
        String jsonString = null;
        Gson json = new Gson();
        List<String> eleDetails = new ArrayList<String>();

        Map dimmap = new HashMap();
        try {
            pstmtForTransterSource = conSource.prepareStatement(getusers);
            resultSet = pstmtForTransterSource.executeQuery();
            returnObject = new PbReturnObject(resultSet);

            if (returnObject != null) {
                String buss_col_id = returnObject.getFieldValueString(0, 0);
                String db_column_id = "";
                String db_table_id = "";
                String grp_id = returnObject.getFieldValueString(0, 1);
                String disp_name = returnObject.getFieldValueString(0, 2);
                String buss_table_id = returnObject.getFieldValueString(0, 3);
                String buss_col_name = returnObject.getFieldValueString(0, 4);
                String user_col_type = returnObject.getFieldValueString(0, 5);
                String refferdEle = returnObject.getFieldValueString(0, 6);

                if (user_col_type.equalsIgnoreCase("SUMMARIZED")) {
                    if (refferdEle != null && !refferdEle.equalsIgnoreCase("") && !refferdEle.equalsIgnoreCase("null")) {
                        String getbussTabNames = "SELECT max(BUSS_TABLE_ID) FROM prg_user_all_info_details  where element_id IN (" + refferdEle + ")";
                        PbDb pbdb = new PbDb();
                        returnObject = null;
                        returnObject = pbdb.execSelectSQL(getbussTabNames);
                        if (returnObject != null) {
                            if (returnObject.getRowCount() > 0) {
                                buss_table_id = returnObject.getFieldValueString(0, 0);
                            }
                        }
                    }
                }

                str = buss_col_id + "," + db_column_id + "," + db_table_id + "," + grp_id + "~" + disp_name + "~" + buss_table_id + "~" + buss_col_name + "~" + user_col_type + "~" + elementId;
                eleDetails.add(str);
            }
            dimmap.put("eleDetails", eleDetails);
            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;

    }

    public String getElementColType(String elementId) throws SQLException {
        SourceConn sc = new SourceConn();
        PbDb pbdb = new PbDb();
        PbReturnObject returnObject = null;
        String str = "";
        String getusers = "SELECT USER_COL_TYPE,folder_id,REFFERED_ELEMENTS FROM prg_user_all_info_details  where element_id IN (" + elementId + ")";
        String jsonString = null;
        Gson json = new Gson();
        List<String> userColType = new ArrayList<String>();
        List<String> folderId = new ArrayList<String>();
        List<String> isSummValid = new ArrayList<String>();
        String isSummValidFlag = "true";

        Map dimmap = new HashMap();
        try {
            returnObject = pbdb.executeSelectSQL(getusers);
            if (returnObject != null) {
                String ColType = returnObject.getFieldValueString(0, 0);
                String folId = returnObject.getFieldValueString(0, 1);
                String refferdEle = returnObject.getFieldValueString(0, 2);
//                if(ColType.equalsIgnoreCase("SUMMARIZED")){
//                    if(refferdEle!=null && !refferdEle.equalsIgnoreCase("") && !refferdEle.equalsIgnoreCase("null")){
//                    String getbussTabNames = "SELECT BUSS_TABLE_ID FROM prg_user_all_info_details  where element_id IN ("+refferdEle+")";
//                    returnObject = null;
//                    returnObject = pbdb.execSelectSQL(getbussTabNames);
//                    if(returnObject!=null){
//                        if(returnObject.getRowCount()>0){
//                            for(int i=0;i<returnObject.getRowCount();i++){
//                            if(!returnObject.getFieldValueString(0,0).equalsIgnoreCase(returnObject.getFieldValueString(i,0))){
//                                isSummValidFlag = "false";
//                            }}
//                        }
//                    }
//                }
//                }
                userColType.add(ColType);
                folderId.add(folId);
                isSummValid.add(isSummValidFlag);
            }
            dimmap.put("userColType", userColType);
            dimmap.put("folderId", folderId);
            dimmap.put("isSummValid", isSummValid);
            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;

    }

    public String getPlanScenarioDetails(String connId, String plantCode) {
        String Query = "";
        Connection conn = null;
        PreparedStatement pstmtForTransterSource = null;
        ResultSet resultSet = null;

        conn = ProgenConnection.getInstance().getConnectionByConId(connId);
//        String query = "SELECT DISTINCT plan_period FROM drl_planning_scenario_version_dim where PLANT ="+plantCode;
        String query = "SELECT DISTINCT planning_scenario FROM drl_plan_period_dim where PLANT =?" ;
        PbReturnObject pbro = new PbReturnObject();
        try {
            pstmtForTransterSource = conn.prepareStatement(query);
            pstmtForTransterSource.setString(1, plantCode);
            resultSet = pstmtForTransterSource.executeQuery();
            pbro = new PbReturnObject(resultSet);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='0000'>--- SELECT ---</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueString(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 0));
            sb.append("</option>");
        }
        try {
            if (conn != null) {
                conn.close();
            }
            if (pstmtForTransterSource != null) {
                pstmtForTransterSource.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return sb.toString();
    }

    public String getPlanScenarioDetails11(String connId, String plantCode) {
        String Query = "";
        Connection conn = null;
        PreparedStatement pstmtForTransterSource = null;
        ResultSet resultSet = null;

        conn = ProgenConnection.getInstance().getConnectionByConId(connId);
//        String query = "SELECT DISTINCT plan_period FROM drl_planning_scenario_version_dim where PLANT ="+plantCode;
        String query = "SELECT DISTINCT planning_scenario FROM drl_cto_cmp_stg_master_prod where PLANT =?" ;
        PbReturnObject pbro = new PbReturnObject();
        try {
            pstmtForTransterSource = conn.prepareStatement(query);
             pstmtForTransterSource.setString(1, plantCode);
            resultSet = pstmtForTransterSource.executeQuery();
            pbro = new PbReturnObject(resultSet);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='0000'>--- SELECT ---</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueString(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 0));
            sb.append("</option>");
        }
        try {
            if (conn != null) {
                conn.close();
            }
             if (pstmtForTransterSource != null) {
                pstmtForTransterSource.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return sb.toString();
    }

    public String getElementBussTabName(String elementId) throws SQLException {
        String getbussColNameQry = "";
        String jsonString = null;
        Gson json = new Gson();
        List<String> tableDispName = new ArrayList<String>();
        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
            getbussColNameQry = "select isnull(table_disp_name,disp_name) from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID = " + elementId;
        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
            getbussColNameQry = "select ifnull(table_disp_name,disp_name) from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID = " + elementId;
        } else {
            getbussColNameQry = "select NVL(table_disp_name,disp_name) from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID = " + elementId;
        }
        String tabDispName = "";
        PbReturnObject returnObject = null;
        Map dimmap = new HashMap();
        try {
            returnObject = execSelectSQL(getbussColNameQry);
            if (returnObject != null && returnObject.getRowCount() != 0) {
                tabDispName = returnObject.getFieldValueString(0, 0);
                tableDispName.add(tabDispName);
            }
            dimmap.put("tableDispName", tableDispName);
            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;

    }

    public boolean saveMgmtCalender(String calenderName, String conid, String denomTable) throws SQLException {
        Connection conn = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransfer;
        int flag = 0;
        ResultSet resultSet = null;
        PbReturnObject retObj = null;
        String addCalQry = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
            addCalQry = "insert into PRG_CALENDER_SETUP (CALENDER_ID,CALENDER_NAME,CALENDER_TYPE,DENOM_TABLE,CONNECTION_ID)"
                    + " values (PRG_CALENDER_SETUP_SEQ.nextval,'&','&','&','&')";
        } else {
            addCalQry = "insert into PRG_CALENDER_SETUP (CALENDER_NAME,CALENDER_TYPE,DENOM_TABLE,CONNECTION_ID)"
                    + " values ('&','&','&','&')";
        }

        Object inOb[] = new Object[4];
        inOb[0] = calenderName;
        inOb[1] = "denomTable";
        inOb[2] = denomTable;
        inOb[3] = conid;

        String Query = buildQuery(addCalQry, inOb);

        if (conn != null) {
            try {
                pstmtForTransfer = conn.prepareStatement(Query);
                flag = pstmtForTransfer.executeUpdate();
                flag = 1;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        if (conn != null) {
            conn = null;
        }
        if (flag == 0) {
            return false;
        } else {
            return true;
        }
    }

    public String getCompanyDetailsForConn() {
        String Query = " SELECT COMPANY_ID, COMPANY_NAME FROM COMPANY_MASTER ";
        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(Query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='--SELECT--'>--SELECT--</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueInt(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 1));
            sb.append("</option>");
        }
        return sb.toString();
    }

    public String getCompanyCalDetails(String conid, String companyId) {
        String Query = "SELECT CALENDER_ID,CALENDER_NAME from PRG_CALENDER_SETUP where CONNECTION_ID = " + conid + ""
                + " AND CALENDER_ID not in (SELECT CALENDER_ID FROM PRG_COMPANY_CALANDER where COMPANY_ID = " + companyId + ")";
        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(Query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='--SELECT--'>--SELECT--</option>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            sb.append("<option value='" + pbro.getFieldValueInt(i, 0) + "'>");
            sb.append(pbro.getFieldValueString(i, 1));
            sb.append("</option>");
        }
        return sb.toString();
    }

    public boolean SaveCalenderToComp(String companyId, String calenderId) throws SQLException {
        Connection conn = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransfer;
        int flag = 0;
        ResultSet resultSet = null;
        PbReturnObject retObj = null;
        String addCalQry = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
            addCalQry = "insert into PRG_COMPANY_CALANDER (ID,COMPANY_ID,CALENDER_ID,DEFAULT_VAL)"
                    + " values (PRG_COMPANY_CALANDER_SEQ.nextval,'&','&','&')";
        } else {
            addCalQry = "insert into PRG_COMPANY_CALANDER (ID,COMPANY_ID,CALENDER_ID,DEFAULT_VAL)"
                    + " values (PRG_COMPANY_CALANDER_SEQ.nextval,'&','&','&')";
        }

        Object inOb[] = new Object[3];
        inOb[0] = companyId;
        inOb[1] = calenderId;
        inOb[2] = 'N';

        String Query = buildQuery(addCalQry, inOb);
        if (conn != null) {
            try {
                pstmtForTransfer = conn.prepareStatement(Query);
                flag = pstmtForTransfer.executeUpdate();
                flag = 1;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        if (conn != null) {
            conn = null;
        }
        if (flag == 0) {
            return false;
        } else {
            return true;
        }
    }

    public String getCompanyCalanders(String conid, String companyId) {
        String Query = "SELECT A.CALENDER_ID,A.DEFAULT_VAL,B.CALENDER_NAME from PRG_COMPANY_CALANDER A,PRG_CALENDER_SETUP B WHERE A.COMPANY_ID=" + companyId + " AND A.CALENDER_ID = B.CALENDER_ID";

        PbReturnObject pbro = new PbReturnObject();
        String jsonString = null;
        Gson json = new Gson();
        List<String> calenderId = new ArrayList<String>();
        List<String> defaulVal = new ArrayList<String>();
        List<String> calenderName = new ArrayList<String>();
        Map dimmap = new HashMap();
        String finalquery = "";

        try {
            StringBuilder sb = new StringBuilder();
            pbro = new PbDb().execSelectSQL(Query);
            if (pbro.getRowCount() > 0) {
                for (int i = 0; i < pbro.getRowCount(); i++) {
                    calenderId.add(pbro.getFieldValueString(i, 0));
                    defaulVal.add(pbro.getFieldValueString(i, 1));
                    calenderName.add(pbro.getFieldValueString(i, 2));
                }
                dimmap.put("calenderId", calenderId);
                dimmap.put("defaulVal", defaulVal);
                dimmap.put("calenderName", calenderName);
                jsonString = json.toJson(dimmap);
            }

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;
    }

    public boolean saveMarkCalender(String companyId, String calenderId) throws SQLException {
        Connection conn = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransfer;
        PreparedStatement pstmtForTransfer1;
        int flag = 0;
        ResultSet resultSet = null;
        PbReturnObject retObj = null;
        String addCalQry = "";
        String addCalQry1 = "";
        addCalQry = "update PRG_COMPANY_CALANDER "
                + " set DEFAULT_VAL = 'N' WHERE COMPANY_ID = &";
        addCalQry1 = "update PRG_COMPANY_CALANDER "
                + " set DEFAULT_VAL = 'Y' WHERE COMPANY_ID = & AND CALENDER_ID = &";

        Object inOb[] = new Object[1];
        inOb[0] = companyId;

        Object inOb1[] = new Object[2];
        inOb1[0] = companyId;
        inOb1[1] = calenderId;

        String q1 = buildQuery(addCalQry, inOb);
        String q2 = buildQuery(addCalQry1, inOb1);
        if (conn != null) {
            try {
                pstmtForTransfer = conn.prepareStatement(q1);
                pstmtForTransfer.executeUpdate();
                pstmtForTransfer1 = conn.prepareStatement(q2);
                pstmtForTransfer1.executeUpdate();
                flag = 1;
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        if (conn != null) {
            conn = null;
        }
        if (flag == 0) {
            return false;
        } else {
            return true;
        }
    }

    public String getPlanDataDetails(String connectionID) {
        Connection conn = null;

        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        List<String> plant = new ArrayList<String>();
        List<String> planningScenario = new ArrayList<String>();
        List<String> planRunDate = new ArrayList<String>();
        Map dimmap = new HashMap();
        String finalquery = "";
        PreparedStatement pstmtForTransterSource = null;
        ResultSet resultSet = null;
        conn = ProgenConnection.getInstance().getConnectionByConId(connectionID);
        if (conn != null) {
            try {
                String query = "select distinct plant,planning_scenario,custom_field1 from drl_month_fact order by custom_number1 desc limit 20";

                pstmtForTransterSource = conn.prepareStatement(query);
                resultSet = pstmtForTransterSource.executeQuery();
                returnObject = new PbReturnObject(resultSet);
                if (returnObject != null) {
                    for (int i = 0; i < returnObject.getRowCount(); i++) {
                        plant.add(returnObject.getFieldValueString(i, 0));
                        planningScenario.add(returnObject.getFieldValueString(i, 1));
                        planRunDate.add(returnObject.getFieldValueString(i, 2));
                    }
                }
                dimmap.put("plant", plant);
                dimmap.put("planningScenario", planningScenario);
                dimmap.put("planRunDate", planRunDate);
                jsonString = json.toJson(dimmap);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }

        return jsonString;
    }

    public String getSecurityVal(String elementId) throws SQLException {
        Connection conn = null;

        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        List<String> secKeyId = new ArrayList<String>();
        List<String> bussTabId = new ArrayList<String>();
        List<String> bussColId = new ArrayList<String>();
        List<String> bussTableName = new ArrayList<String>();
        List<String> bussColName = new ArrayList<String>();
        Map dimmap = new HashMap();
        String finalquery = "";
        PreparedStatement pstmtForTransterSource = null;
        ResultSet resultSet = null;
        String Query = "";

//       if(secCheck.equalsIgnoreCase("dim")){
//        Query = " SELECT distinct b.GRP_ID,b.BUSS_TABLE_ID FROM PRG_USER_ALL_INFO_DETAILS b where connection_id in ("+connectionID+") "
//                + " and FOLDER_ID in ("+folderID+") and DIM_ID in ("+dimId+") and b.DIM_NAME not in('Time')";
//       }
//     else {
//        Query = "SELECT DISTINCT GRP_ID,BUSS_TABLE_ID FROM PRG_USER_ALL_INFO_DETAILS WHERE CONNECTION_ID in ("+connectionID+") "
//                + " AND FOLDER_ID in ("+folderID+")  AND BUSS_TABLE_ID in ("+dimId+")";
//     }
        Query = "SELECT DISTINCT GRP_ID,BUSS_TABLE_ID,BUSS_COL_ID FROM PRG_USER_ALL_INFO_DETAILS WHERE ELEMENT_ID = " + elementId;

        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = new PbDb().execSelectSQL(Query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (pbro.getRowCount() > 0) {

            String grpId = pbro.getFieldValueString(0, 0);
            String bussTableId = pbro.getFieldValueString(0, 1);
            String bussColumnId = pbro.getFieldValueString(0, 2);
            conn = ProgenConnection.getInstance().getConnection();
            if (conn != null) {
                try {
                    String query = "select SEC_KEY_ID,BUSS_TABLE_ID,BUSS_COL_ID,BUSS_TABLE_NAME,BUSS_COL_NAME from PRG_SEC_GRP_ROLE_USER_VAR where GRP_ID =?  AND BUSS_TABLE_ID = ? AND BUSS_COL_ID=?";

                    pstmtForTransterSource = conn.prepareStatement(query);
                    pstmtForTransterSource.setString(1, grpId);
                    pstmtForTransterSource.setString(2, bussTableId);
                    pstmtForTransterSource.setString(3, bussColumnId);
                    resultSet = pstmtForTransterSource.executeQuery();
                    returnObject = new PbReturnObject(resultSet);
                    if (returnObject != null) {
                        for (int i = 0; i < returnObject.getRowCount(); i++) {
                            secKeyId.add(returnObject.getFieldValueString(i, 0));
                            bussTabId.add(returnObject.getFieldValueString(i, 1));
                            bussColId.add(returnObject.getFieldValueString(i, 2));
                            bussTableName.add(returnObject.getFieldValueString(i, 3));
                            bussColName.add(returnObject.getFieldValueString(i, 4));
                        }
                    }
                    dimmap.put("secKeyId", secKeyId);
                    dimmap.put("bussTabId", bussTabId);
                    dimmap.put("bussColId", bussColId);
                    dimmap.put("bussTableName", bussTableName);
                    dimmap.put("bussColName", bussColName);
                    jsonString = json.toJson(dimmap);
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
            }
        }

        return jsonString;
    }

    public String deleteSecurityVal(String filterId) {
        String status = "failure";
        String qry = "delete from PRG_SEC_GRP_ROLE_USER_VAR where SEC_KEY_ID in (&)";
        Object[] obj = new Object[2];
        obj[0] = filterId;
        String finalqry = buildQuery(qry, obj);

        try {
            execModifySQL(finalqry);
            status = "success";
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return status;
    }

    public String getMultiSecurityVal(String elementId, String userId) throws SQLException {
        Connection conn = null;

        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        List<String> secKeyId = new ArrayList<String>();
        List<String> memberName = new ArrayList<String>();
        List<String> MemberValue = new ArrayList<String>();

        Map dimmap = new HashMap();
        String finalquery = "";
        PreparedStatement pstmtForTransterSource = null;
        ResultSet resultSet = null;
        String query = "";

        conn = ProgenConnection.getInstance().getConnection();
        if (userId.equalsIgnoreCase("0000")) {
            query = "select a.ROLE_FILTER_ID,b.MEMBER_NAME,a.MEMBER_VALUE from PRG_ROLE_MEMBER_FILTER a, PRG_USER_ALL_INFO_DETAILS b "
                    + " where a.ELEMENT_ID = b.ELEMENT_ID "
                    + " and a.ELEMENT_ID = ?"  ;
                pstmtForTransterSource = conn.prepareStatement(query);
                pstmtForTransterSource.setString(1, elementId);
        } else {
            query = "select a.USER_FILTER_ID,b.MEMBER_NAME,a.MEMBER_VALUE "
                    + " from PRG_USER_ROLE_MEMBER_FILTER a, PRG_USER_ALL_INFO_DETAILS b "
                    + " where a.ELEMENT_ID = b.ELEMENT_ID "
                    + " and a.ELEMENT_ID = ?"
                    + " and a.USER_ID = ?" ;
                pstmtForTransterSource.setString(1, elementId);
                pstmtForTransterSource.setString(2, userId);
                pstmtForTransterSource = conn.prepareStatement(query);
        }
        if (conn != null) {
            try {
                resultSet = pstmtForTransterSource.executeQuery();
                returnObject = new PbReturnObject(resultSet);
                if (returnObject != null) {
                    for (int i = 0; i < returnObject.getRowCount(); i++) {
                        secKeyId.add(returnObject.getFieldValueString(i, 0));
                        memberName.add(returnObject.getFieldValueString(i, 1));
                        String memVal = returnObject.getFieldValueClobString(i, "MEMBER_VALUE");
                        memVal = memVal.replace("'||chr(38)||'", "&").replace("'", "");
                        MemberValue.add(memVal);

                    }
                }
                dimmap.put("secKeyId", secKeyId);
                dimmap.put("memberName", memberName);
                dimmap.put("MemberValue", MemberValue);
                jsonString = json.toJson(dimmap);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }

        return jsonString;
    }

    public String deleteMultiSecurityVal(String filterId, String userId) {

        String status = "failure";
        String qry = "";
        if (userId.equalsIgnoreCase("0000")) {
            qry = "delete from PRG_ROLE_MEMBER_FILTER where ROLE_FILTER_ID in (&)";
        } else {
            qry = "delete from PRG_USER_ROLE_MEMBER_FILTER where USER_FILTER_ID in (&)";
        }
        Object[] obj = new Object[2];
        obj[0] = filterId;
        String finalqry = buildQuery(qry, obj);

        try {
            execModifySQL(finalqry);
            status = "success";
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return status;
    }
    //end of code by Nazneen

    public String getUseridDetails() throws ParseException, SQLException {

        Connection conn = null;
        PreparedStatement pstmtForTransterSource = null;
        ResultSet resultSet = null;
        List<String> firstname11 = new ArrayList<String>();
        List<String> lastname11 = new ArrayList<String>();
        List<String> pu_email = new ArrayList<String>();
        List<String> puid = new ArrayList<String>();
        List<String> userid = new ArrayList<String>();
        List<String> logindatetime = new ArrayList<String>();
        List<String> logoutdatetime = new ArrayList<String>();
        List<String> loginDate = new ArrayList<String>();
        String jsonString = null;
        String datetime111;
        String datetime211;
        PreparedStatement pstmtForTransterSource11 = null;
        ResultSet resultSet11 = null;
        PbReturnObject pbro11 = new PbReturnObject();
        List<String> datetime11 = new ArrayList<String>();
        Map dimmap = new HashMap();
        Gson json = new Gson();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH : mm : ss");
        java.util.Date date = new java.util.Date();
        String datetime = dateFormat.format(date);

        Date date2 = dateFormat.parse(datetime);

        PbReturnObject pbro = new PbReturnObject();
        String query1 = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            query1 = "select user_id,(login_date + ' ' + login_time),(logout_date + ' ' + logout_time),login_date from hit_calc "
                    + " where logout_date is null and logout_time is null";
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            query1 = "select user_id,concat(login_date, ' ', login_time),concat(logout_date, ' ', logout_date),login_date from hit_calc "
                    + " where logout_date is null and logout_time is null";
        } else {

            query1 = "select user_id,login_date ||' '||login_time,logout_date ||' '||logout_time,login_date from hit_calc "
                    + " where logout_date is null and logout_time is null";
        }
        try {
            conn = ProgenConnection.getInstance().getConnection();
            pstmtForTransterSource = conn.prepareStatement(query1);
            resultSet = pstmtForTransterSource.executeQuery();
            pbro = new PbReturnObject(resultSet);

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        if (pbro != null) {
            for (int i = 0; i < pbro.getRowCount(); i++) {
                userid.add(pbro.getFieldValueString(i, 0));
                logindatetime.add(pbro.getFieldValueString(i, 1));
                logoutdatetime.add(pbro.getFieldValueString(i, 2));
                loginDate.add(pbro.getFieldValueString(i, 3));
            }
        }
//          datetime111 = logindatetime.toString();
//          datetime211 = logoutdatetime.toString();
//          if(datetime111.compareTo(datetime)>0 && datetime211!=null)
//           {
//             datetime11 = logindatetime;
//                for (int i = 0; i < userid.size(); i++) {
//                String query11 = "select PU_FIRSTNAME,PU_LASTNAME,pu_id,pu_email from prg_ar_users where pu_id="+userid.get(i);
//               pstmtForTransterSource11 = conn.prepareStatement(query11);
//               resultSet11 = pstmtForTransterSource11.executeQuery();
//               pbro11 = new PbReturnObject(resultSet11);
//                firstname11.add(pbro11.getFieldValueString(0, 0));
//                lastname11.add(pbro11.getFieldValueString(0, 1));
//                pu_email.add(pbro11.getFieldValueString(0, 3));
//                datetime11.add(datetime11.get(i));
//             }
//          }
        //changed by Nazneen
        DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String lt_date1 = newDateFormat.format(cal.getTime());
        String sysDate = lt_date1.substring(0, 10);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < userid.size(); i++) {
            String logDate1 = loginDate.get(i);
            String logDate = logDate1.substring(0, 10);
            Date newLoginDate = formatter.parse(logDate);
            Date newSysDate = formatter.parse(sysDate);
            if (newLoginDate.compareTo(newSysDate) == 0) {
//             datetime11 = logindatetime;
                String dateVal = logindatetime.get(i);
                String query11 = "select PU_FIRSTNAME,PU_LASTNAME,pu_id,pu_email from prg_ar_users where pu_id=?"  ;
                pstmtForTransterSource11 = conn.prepareStatement(query11);
                pstmtForTransterSource11.setString(1, userid.get(i));
                resultSet11 = pstmtForTransterSource11.executeQuery();
                pbro11 = new PbReturnObject(resultSet11);
                firstname11.add(pbro11.getFieldValueString(0, 0));
                lastname11.add(pbro11.getFieldValueString(0, 1));
                pu_email.add(pbro11.getFieldValueString(0, 3));
//                datetime11.add(datetime11.get(i));
                datetime11.add(dateVal);
            }
        }

        dimmap.put("firstname11", firstname11);
        dimmap.put("lastname11", lastname11);
        dimmap.put("pu_email", pu_email);
        dimmap.put("datetime11", datetime11);
        jsonString = json.toJson(dimmap);

        return jsonString;

    }

    public boolean rePublishDimension(String connID, String folderId, String dimId) throws SQLException {
        boolean result = false;
        int rs = 0;
        String bussTableId = "";
        Connection conn = null;
        String deleteUserAllInfoDetails = getResourceBundle().getString("deleteDimPublishUserAllInfoDetails");
        String deleteUserAllADIMDetails = getResourceBundle().getString("deleteDimPublishAllADIMDetails");
        String deleteUserAllADIMKeyValEle = getResourceBundle().getString("deleteDimPublishAllADIMKeyValEle");
        String deleteUserAllADIMMaster = getResourceBundle().getString("deleteDimPublishAllADIMMaster");
        String deleteUserAllDDIMDetails = getResourceBundle().getString("deleteDimPublishAllDDIMDetails");
        String deleteUserAllDDIMKeyValEle = getResourceBundle().getString("deleteDimPublishAllDDIMKeyValEle");
        String deleteUserAllDDIMMaster = getResourceBundle().getString("deleteDimPublishAllDDIMMaster");

        String insertUserAllInfoDetails = getResourceBundle().getString("insertDimPublishUserAllInfoDetails");
        String insertUserAllADIMDetails = getResourceBundle().getString("insertDimPublishAllADIMDetails");
        String insertUserAllADIMKeyValEle = getResourceBundle().getString("insertDimPublishAllADIMKeyValEle");
        String insertUserAllADIMMaster = getResourceBundle().getString("insertDimPublishAllADIMMaster");
        String insertUserAllDDIMDetails = getResourceBundle().getString("insertDimPublishAllDDIMDetails");
        String insertUserAllDDIMKeyValEle = getResourceBundle().getString("insertDimPublishAllDDIMKeyValEle");
        String insertUserAllDDIMMaster = getResourceBundle().getString("insertDimPublishAllDDIMMaster");

        String getDimId = "select BUSS_TABLE_ID from PRG_USER_ALL_INFO_DETAILS where DIM_ID = ?" ;
        PreparedStatement pstmtForTransterSource = null;
        ResultSet resultSet = null;
        PbReturnObject pbro = new PbReturnObject();
        conn = ProgenConnection.getInstance().getConnection();
        if (conn != null) {
            pstmtForTransterSource = conn.prepareStatement(getDimId);
            pstmtForTransterSource.setString(1,  dimId);
            resultSet = pstmtForTransterSource.executeQuery();
            pbro = new PbReturnObject(resultSet);
            bussTableId = pbro.getFieldValueString(0, 0);
        }
        ArrayList deleteDimMembersQueries = new ArrayList();

        PbReturnObject retObj = null;
        Object[] Obj = null;
        Object[] Obj1 = null;
        Object[] Obj2 = null;
        String finalQuery = "";
        Obj = new Object[1];
        Obj[0] = bussTableId;
        Obj2 = new Object[1];
        Obj2[0] = dimId;
        try {
            finalQuery = buildQuery(deleteUserAllInfoDetails, Obj);
            deleteDimMembersQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllADIMDetails, Obj2);
            deleteDimMembersQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllADIMKeyValEle, Obj);
            deleteDimMembersQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllADIMMaster, Obj2);
            deleteDimMembersQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllDDIMDetails, Obj2);
            deleteDimMembersQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllDDIMKeyValEle, Obj);
            deleteDimMembersQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllDDIMMaster, Obj2);
            deleteDimMembersQueries.add(finalQuery);

            executeMultiple(deleteDimMembersQueries);

            Obj1 = new Object[2];
            Obj1[0] = folderId;
            Obj1[1] = bussTableId;

            ArrayList insertQueriesForPublish = new ArrayList();

            finalQuery = buildQuery(insertUserAllInfoDetails, Obj1);
            insertQueriesForPublish.add(finalQuery);
            finalQuery = buildQuery(insertUserAllADIMDetails, Obj1);
            insertQueriesForPublish.add(finalQuery);
            finalQuery = buildQuery(insertUserAllADIMKeyValEle, Obj1);
            insertQueriesForPublish.add(finalQuery);
            finalQuery = buildQuery(insertUserAllADIMMaster, Obj1);
            insertQueriesForPublish.add(finalQuery);
            finalQuery = buildQuery(insertUserAllDDIMDetails, Obj1);
            insertQueriesForPublish.add(finalQuery);
            finalQuery = buildQuery(insertUserAllDDIMKeyValEle, Obj1);
            insertQueriesForPublish.add(finalQuery);
            finalQuery = buildQuery(insertUserAllDDIMMaster, Obj1);
            insertQueriesForPublish.add(finalQuery);

            Obj1[0] = "Y";
            Obj1[1] = folderId;
            String addupdateFolderPublishment = getResourceBundle().getString("updateFolderPublishment");
            finalQuery = buildQuery(addupdateFolderPublishment, Obj1);
            insertQueriesForPublish.add(finalQuery);
            result = executeMultiple(insertQueriesForPublish);

            return result;

        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return result;
        }
    }

    public boolean rePublishFact(String connID, String folderId, String bussTableId) throws SQLException {
        boolean result = false;
        int rs = 0;
        Connection conn = null;
        String insertRemFactPublishUserAllInfoDetails = getResourceBundle().getString("insertRemFactPublishUserAllInfoDetails");
        String insertAllFactPublishUserAllInfoDetails = getResourceBundle().getString("insertAllFactPublishUserAllInfoDetails");

        ArrayList insertQueriesForPublishFact = new ArrayList();
        PbReturnObject retObj = null;
        boolean fullPublish = true;
        String finalQuery = "";

        String getIsExistEleQry = "select * from PRG_USER_ALL_INFO_DETAILS where BUSS_TABLE_ID = ?" ;
        PreparedStatement pstmtForTransterSource = null;
        ResultSet resultSet = null;
        PbReturnObject pbro = new PbReturnObject();
        conn = ProgenConnection.getInstance().getConnection();
        if (conn != null) {
            pstmtForTransterSource = conn.prepareStatement(getIsExistEleQry);
            pstmtForTransterSource.setString(1, bussTableId);
            resultSet = pstmtForTransterSource.executeQuery();
            pbro = new PbReturnObject(resultSet);
            if (pbro.getRowCount() > 0) {
                fullPublish = false;
            }
        }
        try {
            if (fullPublish) {
                Object[] Obj = new Object[2];
                Obj[0] = folderId;
                Obj[1] = bussTableId;
                finalQuery = buildQuery(insertAllFactPublishUserAllInfoDetails, Obj);
                insertQueriesForPublishFact.add(finalQuery);
            } else {
//                String elementsId = "";
                StringBuilder elementsId = new StringBuilder();
                String getRemaingEleIds = " select A.ELEMENT_ID from PRG_USER_SUB_FOLDER_ELEMENTS A left outer join PRG_USER_ALL_INFO_DETAILS B on "
                        + "(A.ELEMENT_ID = B.ELEMENT_ID) where B.ELEMENT_ID is null AND A.BUSS_TABLE_ID =?";
                pstmtForTransterSource = null;
                resultSet = null;
                pstmtForTransterSource = conn.prepareStatement(getRemaingEleIds);
                pstmtForTransterSource.setString(1, bussTableId);
                resultSet = pstmtForTransterSource.executeQuery();
                pbro = new PbReturnObject(resultSet);
                if (pbro.getRowCount() > 0) {
                    for (int i = 0; i < pbro.getRowCount(); i++) {
//                        elementsId = elementsId + "," + pbro.getFieldValueString(i, 0);
                        elementsId.append(",").append(pbro.getFieldValueString(i, 0));
                    }
                    int len = elementsId.length();
                    elementsId = new StringBuilder(elementsId.substring(1, len));
                }
                Object[] Obj = new Object[3];
                Obj[0] = folderId;
                Obj[1] = bussTableId;
                Obj[2] = elementsId;
                finalQuery = buildQuery(insertRemFactPublishUserAllInfoDetails, Obj);
                insertQueriesForPublishFact.add(finalQuery);
            }
            result = executeMultiple(insertQueriesForPublishFact);
            return result;
        } catch (SQLException exception) {
            logger.error("Exception:", exception);
            return result;
        }
    }

    public String getAllFactsDetails(String connectionID, String folderId) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        List<String> bussTableId = new ArrayList<String>();
        List<String> bussTableName = new ArrayList<String>();
        List<String> tableDispName = new ArrayList<String>();
        Map dimmap = new HashMap();
        String finalquery = "";
        finalquery = "select distinct buss_table_id,buss_table_name,table_disp_name from prg_user_all_info_details "
                + " where is_fact = 'Y' "
                + " and buss_table_name is not null "
                + " and connection_id = " + connectionID + " and folder_id = " + folderId;

        try {
            returnObject = super.execSelectSQL(finalquery);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    bussTableId.add(returnObject.getFieldValueString(i, 0));
                    bussTableName.add(returnObject.getFieldValueString(i, 1));
                    tableDispName.add(returnObject.getFieldValueString(i, 2));
                }
            }
            dimmap.put("bussTableId", bussTableId);
            dimmap.put("bussTableName", bussTableName);
            dimmap.put("tableDispName", tableDispName);
            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;
    }

    public int modifyTableDispName(String conId, String folderID, String newTableDispName, String bussTableId) {
        Connection conn = null;
        try {
            conn = ProgenConnection.getInstance().getConnection();
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        PreparedStatement pstmtForTransfer;
        PreparedStatement pstmtForTransfer1;
        int flag = 0;
        ResultSet resultSet = null;
        PbReturnObject retObj = null;
        String qryModifyTableDispName = "";
        qryModifyTableDispName = "update prg_user_all_info_details "
                + " set table_disp_name = '&' "
                + " WHERE buss_table_id = & "
                + " and connection_id = & "
                + " and folder_id = & ";

        Object inOb[] = new Object[4];
        inOb[0] = newTableDispName;
        inOb[1] = bussTableId;
        inOb[2] = conId;
        inOb[3] = folderID;

        String q1 = buildQuery(qryModifyTableDispName, inOb);
        if (conn != null) {
            try {
                pstmtForTransfer = conn.prepareStatement(q1);
                pstmtForTransfer.executeUpdate();
                flag = 1;
            } catch (Exception ex) {
                logger.error("Exception:", ex);
                flag = 0;
            }
        }
        if (conn != null) {
            conn = null;
        }
        return flag;

    }
    //start of code  by Nazneen for Modify Custom Measures

    public String getAllCustomMeasures(String userId, String connectionID, String foldersSelected, String tablesSelected) {
        String query = "";
        if (tablesSelected.equals("0000")) {
            query = "SELECT DISTINCT A.USER_COL_DESC,A.ELEMENT_ID,A.AGGREGATION_TYPE,A.ACTUAL_COL_FORMULA,B.BUSS_TABLE_NAME,A.BUSS_COL_NAME"
                    + " FROM PRG_USER_ALL_INFO_DETAILS A, PRG_GRP_BUSS_TABLE B WHERE A.BUSS_TABLE_ID = B.BUSS_TABLE_ID  "
                    + " AND A.CONNECTION_ID  =" + connectionID + " AND A.FOLDER_ID = " + foldersSelected + " "
                    + " AND A.USER_COL_TYPE IN ('SUMMARIZED','SUMMARISED') AND A.IS_FACT ='Y' AND A.USE_REPORT_FLAG='Y' "
                    + " ORDER BY A.BUSS_COL_NAME,B.BUSS_TABLE_NAME asc";

        } else {
            query = "SELECT DISTINCT A.USER_COL_DESC,A.ELEMENT_ID,A.AGGREGATION_TYPE,A.ACTUAL_COL_FORMULA,B.BUSS_TABLE_NAME,A.BUSS_COL_NAME"
                    + " FROM PRG_USER_ALL_INFO_DETAILS A, PRG_GRP_BUSS_TABLE B WHERE A.BUSS_TABLE_ID = B.BUSS_TABLE_ID "
                    + " AND A.CONNECTION_ID  =" + connectionID + " AND A.FOLDER_ID = " + foldersSelected + " "
                    + " AND A.BUSS_TABLE_ID = " + tablesSelected + "  AND A.USER_COL_TYPE IN ('SUMMARIZED','SUMMARISED') AND IS_FACT ='Y' "
                    + " AND A.USE_REPORT_FLAG='Y' and A.ref_element_type =1 ORDER BY A.BUSS_COL_NAME,B.BUSS_TABLE_NAME asc";

        }
        PbReturnObject pbro = new PbReturnObject();
        try {
            //
            pbro = new PbDb().execSelectSQL(query);
            Object[] memberNames = new Object[pbro.getRowCount()];
            Object[] elementIds = new Object[pbro.getRowCount()];
            Object[] aggregationTypes = new Object[pbro.getRowCount()];
            Object[] actualColFormulas = new Object[pbro.getRowCount()];
            Object[] bussTableNames = new Object[pbro.getRowCount()];
            Object[] bussColNames = new Object[pbro.getRowCount()];
//        Object[] grpNames = new Object[pbro.getRowCount()];
//        Object[] grpIds = new Object[pbro.getRowCount()];
            MeasureEditHelper med = new MeasureEditHelper();
            for (int i = 0; i < pbro.getRowCount(); i++) {
                memberNames[i] = pbro.getFieldValueString(i, "USER_COL_DESC");
                elementIds[i] = pbro.getFieldValue(i, 1);
                aggregationTypes[i] = pbro.getFieldValue(i, 2);
                actualColFormulas[i] = pbro.getFieldValue(i, 3);
                bussTableNames[i] = pbro.getFieldValue(i, 4);
                bussColNames[i] = pbro.getFieldValue(i, 5);
//         grpNames[i]=pbro.getFieldValue(i, 6);
//         grpIds[i]=pbro.getFieldValue(i, 7);
            }
            med.setMemberName(memberNames);
            med.setElementId(elementIds);
            med.setAggregationType(aggregationTypes);
            med.setActualColFormula(actualColFormulas);
            med.setBussTableName(bussTableNames);
            med.setBussColName(bussColNames);
//        med.setGrpName(grpNames);
//        med.setGrpId(grpIds);
            Gson gson = new Gson();
            gson.toJson(med);
            return gson.toJson(med);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }
    //end of code by Nazneen for Modify Custom Measures
    //start of code  by Amar to gett all reportd fro a role

    public String getReportsForRoles(ArrayList<String> reportNames, ArrayList<Integer> reportIds, HttpServletRequest request) {
        PbReturnObject usrsObj = null;
        StringBuilder usersStrBuilder = new StringBuilder();
        int i = 0;
        //changed by Nazneen
//                String getUsersQuery="select pu_id,pu_firstname from prg_ar_users where pu_id in(select user_id from prg_grp_user_folder_assignment where grp_id="+grpID+")";
        // String getUsersQuery="select pu_id,PU_LOGIN_ID from prg_ar_users where pu_id in(select user_id from prg_grp_user_folder_assignment where grp_id="+grpID+")";
//        try {
//            usrsObj = execSelectSQL(getUsersQuery);
//        } catch (SQLException ex) {
//           logger.error("Exception:",ex);
//        }
        usersStrBuilder.append("<form name='myForm' id='myForm' action='reportViewer.do?reportBy=exportReportsIntoExcelsheets'  method='POST' enctype='application/x-www-form-urlencoded'>");
        usersStrBuilder.append("<table align='center' id='tablesorterUserList' class='tablesorter'   cellpadding='0' cellspacing=1'>");
        usersStrBuilder.append("<thead><tr><th>&nbsp;</th><th>Report Id</th><th>Report Name</th><th>Sheet_No.</th><th>Index_No.</th></tr></thead>");
        usersStrBuilder.append("<tbody>");
        // int x=usrsObj.getRowCount();

        for (i = 0; i < reportIds.size(); i++) {
            usersStrBuilder.append("<tr>");
            usersStrBuilder.append("<td style='width:5%'><input type='checkbox' name='chkusers" + i + "' id='chkIds'>&nbsp;&nbsp;</td><td name='report' style='width:20%'>" + reportIds.get(i) + "<input type='hidden' name='report" + i + "' value='" + reportIds.get(i) + "'></td>");
            usersStrBuilder.append("<td style='width:20%'>" + reportNames.get(i) + "</td>");
            usersStrBuilder.append("<td style='width:20%'><input type='number' style='width:100%' name='sheetName" + i + "' id='sheetNo' min='0' value=''></td>");
            usersStrBuilder.append("<td style='width:20%'><input type='number' style='width:100%' name='lineNo" + i + "' id='lineNo' min='0' value=''></td>");
            usersStrBuilder.append("</td></tr>");
        }
        usersStrBuilder.append("</tbody>");
        usersStrBuilder.append("</table>");
        usersStrBuilder.append("<center>");
        //usersStrBuilder.append("<tr>");
        // usersStrBuilder.append("<td align='left' colspan='1' class='migrate'>File Name: <label style='color:Red'>(.xls or .xlsx files only)</label></td>");
        //usersStrBuilder.append("<td align='left' colspan='1' class='migrate'><input type='file' id='filepath' name='filename' value='' style='background-color:lightgoldenrodyellow; color:black;'/></td>");
        //usersStrBuilder.append("</tr><br/><br/>");
        usersStrBuilder.append("<input type='hidden'   name='Counter' value ='").append(i).append("'/>");
        usersStrBuilder.append("<input type='submit' class='navtitle-hover'  name='Download' value ='DownloadE'/>");
        usersStrBuilder.append("<input type='button' class='navtitle-hover'  onclick='scheduleExcels()' name='Schedule' value ='ScheduleE'/>");
        //usersStrBuilder.append("<input type='button' class='navtitle-hover' id='scheduleE' name='Schedule' value ='Schedule' onclick='scheduleExcels()'/>");
        usersStrBuilder.append("</center>");
        usersStrBuilder.append("</form>");
        return usersStrBuilder.toString();
    }

    public HashMap getRepAndDbrdsForRole(String grpID) {
        ArrayList<String> repDbrdsNames = new ArrayList<String>();
        ArrayList<String> repDbrdsIDs = new ArrayList<String>();
        HashMap repDb = new HashMap();
        PbReturnObject repanddbdobj = null;
        String getRepQuery = "select report_id,report_name,report_type from prg_ar_report_master where report_type='R' and report_id in(select report_id from prg_ar_report_details where folder_id IN (select FOLDER_ID from prg_grp_user_assignment where grp_id=" + grpID + ") )";

        try {
            repanddbdobj = execSelectSQL(getRepQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        for (int i = 0; i < repanddbdobj.getRowCount(); i++) {
            repDbrdsIDs.add(repanddbdobj.getFieldValueString(i, 0));
            repDbrdsNames.add(repanddbdobj.getFieldValueString(i, 1));

        }
        repDb.put("reportIds", repDbrdsIDs);
        repDb.put("reportNames", repDbrdsNames);
        return repDb;

    }
    //added by Mohit for user Registration

    public String sendEmails(String subject, String BodyText, String emails) {
        PbMailParams params = null;
        PbMail mailer = null;
        String[] mailids = emails.split(",");

        for (int i = 0; i < mailids.length; i++) {
            params = new PbMailParams();
            params.setBodyText(BodyText);
            params.setToAddr(mailids[i]);
            params.setSubject(subject);
            params.setHasAttach(false);

            try {
                mailer = new PbMail(params);
                Boolean b = mailer.sendMail();
                int count = 0;

                while (!(b) && count < 5) {
                    b = mailer.sendMail();
                    count++;
//                              
                }
            } catch (Exception e) {
                logger.error("Exception:", e);
            }
        }
        return "send";

    }

    public String getPUid(String pmid) throws SQLException {
        PbReturnObject pbro = new PbReturnObject();
        Connection conn = ProgenConnection.getInstance().getConnection();
//        int flag = 0;
        PbDb pbdb = new PbDb();
        PreparedStatement pstmtForTransfer;

        String query = "select PU_ID from PRG_AR_USERS where PU_LOGIN_ID='" + pmid + "'";
        try {
            pbro = pbdb.execSelectSQL(query);
            if (pbro == null) {
                return "No data";
            }
        } catch (SQLException ex) {
//        
            logger.error("Exception:", ex);
        }
//                      String jsonString=pbro.getFieldValueString(0, 0);
        return pbro.getFieldValueString(0, 0);

    }

    //added by Mohit for user Registration
    public String dActUsers(HttpServletRequest request) throws SQLException {
//        PbReturnObject pbro = new PbReturnObject();
        String id = request.getParameter("id");
        String mailarr[] = id.split("::");
//        Connection conn = ProgenConnection.getInstance().getConnection();
        int flag = 0;
        PbDb pbdb = new PbDb();
//        PreparedStatement pstmtForTransfer;
//        String s = "'&'";
        StringBuilder s = new StringBuilder();
        s.append("'&'");
        Object obj[] = new Object[mailarr.length];
        for (int i = 0; i < mailarr.length; i++) {
            obj[i] = mailarr[i];
            if (i != 0) {
//                s = s + ",'&'";
                s.append(",'&'");
            }
        }

        String query = "update prg_ar_user_registration set activated='Deactivated' where EMAIL IN (" + s + ")";
        query = super.buildQuery(query, obj);
        try {
            flag = pbdb.execUpdateSQL(query);
            if (flag == 0) {
                return "Failed";
            }
        } catch (SQLException ex) {
//        
            logger.error("Exception:", ex);
        } catch (Exception ex) {
        }
        query = "delete PRG_AR_USERS where PU_LOGIN_ID IN (" + s + ")";
        query = super.buildQuery(query, obj);
        try {
            flag = pbdb.execUpdateSQL(query);
            if (flag == 0) {
                return "Failed";
            }
        } catch (SQLException ex) {
//        
            logger.error("Exception:", ex);
        } catch (Exception ex) {
        }
        query = "delete PRG_USER_ASSIGNMENTS where USER_NAME IN (" + s + ")";
        query = super.buildQuery(query, obj);
        try {
            flag = pbdb.execUpdateSQL(query);
            if (flag == 0) {
                return "Failed";
            }
        } catch (SQLException ex) {
//        
            logger.error("Exception:", ex);
        } catch (Exception ex) {
        }
//                      String jsonString="success";
        return "success";

        //    String query="select EMAIL,FIRST_NAME from prg_ar_user_registration where USER_ID="+id;
//                      String bodytext="<html><body>"
//                            + "<h3>Dear "+pbro.getFieldValueString(0, 1)+" ,</h3>"
//                            + "<h2>Your Request To Access PI has been Declined by Admin.....</h2>";
//                         String emails=pbro.getFieldValueString(0, 0)+",";
//                         sendEmails("Access Declined",bodytext,emails);
//                     query="update prg_ar_user_registration  set activated='Deactivated' where EMAIL="+id;
    }

    public String rejectUsers(HttpServletRequest request) throws SQLException {
        PbReturnObject pbro = new PbReturnObject();
        String id = request.getParameter("id");
        String mailarr[] = id.split("::");
//        Connection conn = ProgenConnection.getInstance().getConnection();
        int flag = 0;
        PbDb pbdb = new PbDb();
//        PreparedStatement pstmtForTransfer;
//        String s = "'&'";
        StringBuilder s = new StringBuilder();
        s.append("'&'");
        Object obj[] = new Object[mailarr.length];
        for (int i = 0; i < mailarr.length; i++) {
            obj[i] = mailarr[i];
            if (i != 0) {
//                s = s + ",'&'";
                s.append("'&'");
            }
        }

        String query = "update prg_ar_user_registration set activated='REJECTED' where EMAIL IN (" + s + ")";
        query = super.buildQuery(query, obj);
        try {
            flag = pbdb.execUpdateSQL(query);
            if (flag == 0) {
                return "Failed";
            }
        } catch (SQLException ex) {
//        
            logger.error("Exception:", ex);
        } catch (Exception ex) {
        }

//                      return "success";
        query = "select EMAIL,FIRST_NAME from prg_ar_user_registration where EMAIL IN (" + s + ")";
        query = super.buildQuery(query, obj);
        pbro = pbdb.execSelectSQL(query);

        String bodytext = "";
        for (int i = 0; i < mailarr.length; i++) {
            bodytext = "<html><body><h3>Dear " + pbro.getFieldValueString(i, 1) + " ,</h3><h2>Your Request To Access PI has been Declined by Admin.....</h2>";
            String emails = pbro.getFieldValueString(i, 0) + ",";
            sendEmails("Access Declined", bodytext, emails);
        }
        return "success";
//                     query="update prg_ar_user_registration  set activated='Deactivated' where EMAIL="+id;

    }

    //added by Mohit for user Registration
    public String getPendingUsers(String select, HttpServletRequest request) throws SQLException {
//         request.
        PbReturnObject pbro = new PbReturnObject();
        Connection conn = ProgenConnection.getInstance().getConnection();
//        int flag = 0;
        PbDb pbdb = new PbDb();
        PreparedStatement pstmtForTransfer;
        String status = request.getParameter("status");
        String jsonString = "";
        int i = 0;
        ResultSet resultSet = null;
        String query = "";
        StringBuilder sb = new StringBuilder();
        if (select.equalsIgnoreCase("all")) {

            if (status.equalsIgnoreCase("Pending")) {
                query = "select USER_ID,TITLE,FIRST_NAME,LAST_NAME,EMAIL,MOBILE,NATIONALITY,COUNTRY,ADDRESS,PROFESSION,PURPOSE,ACTIVATED,USER_TYPE from  prg_ar_user_registration WHERE activated='PENDING' order by ACTIVATED ";
            } else if (status.equalsIgnoreCase("Activated")) {
                query = "select USER_ID,TITLE,FIRST_NAME,LAST_NAME,EMAIL,MOBILE,NATIONALITY,COUNTRY,ADDRESS,PROFESSION,PURPOSE,ACTIVATED,USER_TYPE from  prg_ar_user_registration WHERE activated='ACTIVATED' order by ACTIVATED ";
            } else if (status.equalsIgnoreCase("Deactivated")) {
                query = "select USER_ID,TITLE,FIRST_NAME,LAST_NAME,EMAIL,MOBILE,NATIONALITY,COUNTRY,ADDRESS,PROFESSION,PURPOSE,ACTIVATED,USER_TYPE from  prg_ar_user_registration WHERE activated='Deactivated'  order by ACTIVATED";
            } else if (status.equalsIgnoreCase("Rejected")) {
                query = "select USER_ID,TITLE,FIRST_NAME,LAST_NAME,EMAIL,MOBILE,NATIONALITY,COUNTRY,ADDRESS,PROFESSION,PURPOSE,ACTIVATED,USER_TYPE from  prg_ar_user_registration WHERE activated='REJECTED' order by ACTIVATED";
            } else {
                query = "select USER_ID,TITLE,FIRST_NAME,LAST_NAME,EMAIL,MOBILE,NATIONALITY,COUNTRY,ADDRESS,PROFESSION,PURPOSE,ACTIVATED,USER_TYPE from  prg_ar_user_registration order by  ACTIVATED ";
            }

            try {

                pbro = pbdb.execSelectSQL(query);
                for (i = 0; i < pbro.getRowCount(); i++) {

                    int temp1 = pbro.getFieldValueInt(i, 0);
                    sb.append("<tr id='").append(temp1).append("'><td style='width: 1.0em; padding: 0.6em; border: 1px solid #CCC;'>").append("<input type='checkbox' id='").append(pbro.getFieldValueString(i, 4)).append("::").append(pbro.getFieldValueString(i, 0)).append("' name='chkRUser' value='").append(pbro.getFieldValueString(i, 0)).append("'></td>");

//                         sb.append("<tr id='").append(temp1).append("' ><td style=' width: 1.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(i + 1).append("</td>");
                    sb.append("<td style='width: 4.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(pbro.getFieldValueString(i, 1) + " ").append(pbro.getFieldValueString(i, 2) + " ").append(pbro.getFieldValueString(i, 3)).append("</td>");
                    sb.append("<td style='width: 4.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(pbro.getFieldValueString(i, 4)).append("</td>");
                    sb.append("<td style='width: 12.0em;padding: 0.6em; border: 1px solid #CCC;'>").append(pbro.getFieldValueString(i, 5)).append("</td>");
                    sb.append("<td style='width: 6.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(pbro.getFieldValueString(i, 6)).append("</td>");
                    sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(pbro.getFieldValueString(i, 7)).append("</td>");
                    sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(pbro.getFieldValueString(i, 9)).append("</td>");
                    sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(pbro.getFieldValueString(i, 10)).append("</td>");
                    sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(pbro.getFieldValueString(i, 11)).append("</td>");
                    sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(pbro.getFieldValueString(i, 12)).append("</td>");
                    sb.append("<td colspan='2' style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(pbro.getFieldValueString(i, 8)).append("</td></tr>");

//                         sb.append("<td title='Click on Yes to Activate User' style='width: 8.0em; border: 1px solid #CCC;'><input id='Update").append(temp1).append("' type='button' onclick='activateUser(").append(temp1).append(")' style='width:90%;' value='Yes'> </td>");
//                         sb.append("<td title='Click on  No to Decline User' style='width: 8.0em;  border: 1px solid #CCC;'><input id='Delet").append(temp1).append("' type='button' onclick='dactUser(").append(temp1).append(")'  style='width:90%;' value='No'> </td></tr>");
                }

//            resultSet = pstmtForTransfer.executeQuery();
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }

            jsonString = sb.toString();
        } else if (select.equalsIgnoreCase("one")) {
            String id = request.getParameter("id");
            query = "select EMAIL,FIRST_NAME,LAST_NAME,MOBILE,ADDRESS,COUNTRY from  prg_ar_user_registration where USER_ID=" + id;
            pbro = pbdb.execSelectSQL(query);

            String query11 = "select PU_LOGIN_ID from PRG_AR_USERS where PU_LOGIN_ID='" + pbro.getFieldValueString(0, 0) + "'";
            pstmtForTransfer = conn.prepareStatement(query11);
            resultSet = pstmtForTransfer.executeQuery();
            if (resultSet.next()) {
                jsonString = "AlreadyActivated" + pbro.getFieldValueString(0, 1);
                return jsonString;

            }
            String query2 = "insert into PRG_AR_USERS(PU_ID,PU_LOGIN_ID,PU_FIRSTNAME,PU_LASTNAME,PU_PASSWORD,PU_EMAIL,PU_CONTACTNO,PU_ADDRESS,PU_COUNTRY,PU_ACTIVE,PU_START_DATE,PU_END_DATE,USER_TYPE) VALUES(PRG_AR_USERS_SEQ.nextval,'&','&','&','&','&','&','&','&','&','&','&',&) ";
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
            String todaysDate = dateFormat.format(date);
//

            Object obj[] = new Object[12];
            obj[0] = pbro.getFieldValueString(0, 0);
            obj[1] = pbro.getFieldValueString(0, 1);
            obj[2] = pbro.getFieldValueString(0, 2);
            obj[3] = "p@q}V;V;h?z{f.j)K@y;";
            obj[4] = pbro.getFieldValueString(0, 0);
            obj[5] = pbro.getFieldValueString(0, 3);
            obj[6] = pbro.getFieldValueString(0, 4);
            obj[7] = pbro.getFieldValueString(0, 5);
            obj[8] = "Y";
            obj[9] = todaysDate;
            obj[10] = todaysDate;
            obj[11] = 10002;

            query2 = super.buildQuery(query2, obj);
            try {
                int i1 = pbdb.execUpdateSQL(query2);

                if (i1 == 1) {
                    jsonString = "inserted";
                }
                i1 = pbdb.execUpdateSQL("UPDATE prg_ar_user_registration SET ACTIVATED='ACTIVATED' where USER_ID=" + id);
            } catch (SQLException ex) {
//        
                logger.error("Exception:", ex);
            } catch (Exception ex) {
            }

            if (jsonString.equalsIgnoreCase("inserted")) {
                query = "select PU_ID,PU_LOGIN_ID from PRG_AR_USERS where PU_LOGIN_ID='" + pbro.getFieldValueString(0, 0) + "' and PU_FIRSTNAME='" + pbro.getFieldValueString(0, 1) + "' and USER_TYPE=" + 10002;
                PbReturnObject pbro1 = new PbReturnObject();
                pbro1 = pbdb.execSelectSQL(query);
                SuperAdminBd adminBd = new SuperAdminBd();
                adminBd.saveAssignedModules(pbro1.getFieldValueInt(0, 0), 10002, pbro1.getFieldValueString(0, 1), "Y");
                String bodytext = "<html><body>"
                        + "<h3>Dear " + pbro.getFieldValueString(0, 1) + " ,</h3>"
                        + "<h2>Your PI Login has been Activated Successfully, Followings are Login Details.....</h2>"
                        + "<br><table align=\"left\" style=\"width:40%;border-collapse: collapse;padding: 0.6em;\">\n"
                        + "<tr><td align=\"left\" style=\"width:30%; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; \"><label>User Name: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n"
                        + "<td align=\"left\" style=\"padding: 0.6em; border: 1px solid #CCC; \">" + pbro1.getFieldValueString(0, 1) + " </td></tr>\n"
                        + "<tr><td align=\"left\" style=\"width:100px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;\"><label>Password:&nbsp;&nbsp;</label></td>\n"
                        + "<td  style=\"padding: 0.6em; border: 1px solid #CCC; \">password</td>\n"
                        + "</tr></table>"
                        + "<br><br><br><br><br>"
                        + " <h3><a href=\"http://183.82.3.61:8088" + request.getContextPath() + "\">Click Here To Login </a></h3></body></html>";
                String emails = pbro1.getFieldValueString(0, 1) + ",";
                sendEmails("New Account Activated", bodytext, emails);
                jsonString = "activated" + pbro.getFieldValueString(0, 1);
            }

        }
//
        return jsonString;
    }

    //added by Mohit for user Registration
    public String userRegistration(String title, String fn, String ln, String email, String mob, String nationality, String country, String add, String prof, String purpose, HttpServletRequest request) throws SQLException {
        //added by Mohit for Custom setting
        Connection conn = ProgenConnection.getInstance().getConnection();
        PreparedStatement pstmtForTransfer;
//        int flag = 0;
        String ut = request.getParameter("ut");
        String state = request.getParameter("state");
        String jsonString = "false";
//        ResultSet resultSet = null;
//        PbReturnObject retObj = null;
        String addCalQry = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            addCalQry = "insert into PRG_AR_USER_REGISTRATION(TITLE,FIRST_NAME,LAST_NAME,EMAIL,MOBILE,NATIONALITY,COUNTRY,ADDRESS,PROFESSION,PURPOSE,ACTIVATED,USER_TYPE,STATE) values ('&','&','&','&','&','&','&','&','&','&','&','&','&')";
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            addCalQry = "insert into PRG_AR_USER_REGISTRATION(TITLE,FIRST_NAME,LAST_NAME,EMAIL,MOBILE,NATIONALITY,COUNTRY,ADDRESS,PROFESSION,PURPOSE,ACTIVATED,USER_TYPE,STATE) values ('&','&','&','&','&','&','&','&','&','&','&','&','&')";
        } else {
            addCalQry = "insert into PRG_AR_USER_REGISTRATION(USER_ID,TITLE,FIRST_NAME,LAST_NAME,EMAIL,MOBILE,NATIONALITY,COUNTRY,ADDRESS,PROFESSION,PURPOSE,ACTIVATED,USER_TYPE,STATE) values (USER_ID_REG.nextval,'&','&','&','&','&','&','&','&','&','&','&','&','&')";
        }

        Object inOb[] = new Object[13];
        inOb[0] = title;
        inOb[1] = fn;
        inOb[2] = ln;
        inOb[3] = email;
        inOb[4] = mob;
        inOb[5] = nationality;
        inOb[6] = country;
        inOb[7] = add;
        inOb[8] = prof;
        inOb[9] = purpose;
        inOb[10] = "PENDING";
        inOb[11] = ut;
        inOb[12] = state;

        String Query = buildQuery(addCalQry, inOb);

        if (conn != null) {
            try {

                pstmtForTransfer = conn.prepareStatement(Query);
                pstmtForTransfer.executeUpdate();
                jsonString = "true";

            } catch (SQLException ex) {
                logger.error("Exception:", ex);
                if (ex.getErrorCode() == 1) {
                    jsonString = "alreadyexists";
                    return jsonString;

                }
                logger.error("Exception:", ex);
            }
        }
        if (jsonString.equalsIgnoreCase("true")) {
            String emailIds = "bhavani.sankar@progenbusiness.com,amit@progenbusiness.com";
            String bodytext = "<html><body>"
                    + "<h3>Dear Mr. Amit,</h3><br><br>"
                    + "<div>\n"
                    + "<form id=\"regForm\" style=\"width:50%;border: 2px solid #B4D9EE;\">\n"
                    + "<table align=\"center\" style=\"width:90%;border-collapse: collapse;padding: 0.6em;\">\n"
                    + " <tr >\n"
                    + "<td align=\"center\" colspan=\"2\" >\n"
                    + "<h1>User Registration Details\n"
                    + " </h1></td>\n"
                    + "</tr>\n"
                    + "<tr><td align=\"left\" style=\"width:30%; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; \"><label>Full Name : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n"
                    + "<td align=\"left\" style=\"padding: 0.6em; border: 1px solid #CCC; \">" + title + " " + fn + " " + ln + " </td></tr>\n"
                    + "<tr><td align=\"left\" style=\"width:100px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;\"><label>Email Id:&nbsp;&nbsp;</label></td>\n"
                    + "<td  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + email + "</td>\n"
                    + "</tr>\n"
                    + "<tr><td align=\"left\" style=\"width:100px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; \"><label>Mobile No.:&nbsp;&nbsp;</label></td>\n"
                    + "<td  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + mob + "</td>\n"
                    + "</tr>\n"
                    + "<tr><td align=\"left\" style=\"width:100px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;\"><label>Nationality: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n"
                    + "<td align=\"left\"  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + nationality + "</td>\n"
                    + "</tr>\n"
                    + "<tr><td align=\"left\" style=\"width:100px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; \"><label>Country : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n"
                    + "<td align=\"left\"  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + country + "</td></tr>\n"
                    + "<tr><td align=\"left\" style=\"width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; \"><label>User Type:&nbsp;&nbsp;</label></td>\n"
                    + "<td  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + ut + "</td></tr>\n";
            if (ut.equalsIgnoreCase("State User")) {
                bodytext = bodytext + "<tr><td align=\"left\" style=\"width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; \"><label>State:&nbsp;&nbsp;</label></td>\n"
                        + "<td  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + state + "</td>\n"
                        + "</tr>\n";
            }

            bodytext = bodytext + "<tr><td align=\"left\" style=\"width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; \"><label>Address:&nbsp;&nbsp;</label></td>\n"
                    + "<td  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + add + "</td>\n"
                    + "</tr>\n"
                    + "<tr><td align=\"left\" style=\"width:100px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;\"><label>Profession:&nbsp;&nbsp;</label></td>\n"
                    + "<td  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + prof + "</td>\n"
                    + "</tr>\n"
                    + "<tr><td align=\"left\" style=\"width:100px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; \"><label>Purpose:&nbsp;&nbsp;</label></td>\n"
                    + "<td  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + purpose + "</td>\n"
                    + "</tr>\n"
                    + "<br>\n"
                    + "<tr></tr>\n"
                    + "<tr></tr>\n"
                    + "</table>\n"
                    + "</form></div>"
                    + ""
                    + "<br><h2>To Validate Above User Please Login in to Your PI Account and go to BI Manager>Users Request\n"
                    + " </h2>\n"
                    + " <h3><a href=\"http://183.82.3.61:8088" + request.getContextPath() + "\">Click Here To Login </a></h3>"
                    + "</body></html>";

            String usertext = "<html><body>"
                    + "<h3>Dear Mr." + fn + " ,</h3><br><br>"
                    + "<div>\n"
                    + "<form id=\"regForm\" style=\"width:50%;border: 2px solid #B4D9EE;\">\n"
                    + "<table align=\"center\" style=\"width:90%;border-collapse: collapse;padding: 0.6em;\">\n"
                    + " <tr >\n"
                    + "<td align=\"center\" colspan=\"2\" >\n"
                    + "<h1>User Registration Details\n"
                    + " </h1></td>\n"
                    + "</tr>\n"
                    + "<tr><td align=\"left\" style=\"width:30%; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; \"><label>Full Name : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n"
                    + "<td align=\"left\" style=\"padding: 0.6em; border: 1px solid #CCC; \">" + title + " " + fn + " " + ln + " </td></tr>\n"
                    + "<tr><td align=\"left\" style=\"width:100px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;\"><label>Email Id:&nbsp;&nbsp;</label></td>\n"
                    + "<td  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + email + "</td>\n"
                    + "</tr>\n"
                    + "<tr><td align=\"left\" style=\"width:100px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; \"><label>Mobile No.:&nbsp;&nbsp;</label></td>\n"
                    + "<td  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + mob + "</td>\n"
                    + "</tr>\n"
                    + "<tr><td align=\"left\" style=\"width:100px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;\"><label>Nationality: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n"
                    + "<td align=\"left\"  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + nationality + "</td>\n"
                    + "</tr>\n"
                    + "<tr><td align=\"left\" style=\"width:100px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; \"><label>Country : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n"
                    + "<td align=\"left\"  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + country + "</td></tr>\n"
                    + "<tr><td align=\"left\" style=\"width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; \"><label>User Type:&nbsp;&nbsp;</label></td>\n"
                    + "<td  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + ut + "</td></tr>\n";
            if (ut.equalsIgnoreCase("State User")) {
                usertext = usertext + "<tr><td align=\"left\" style=\"width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; \"><label>State:&nbsp;&nbsp;</label></td>\n"
                        + "<td  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + state + "</td>\n"
                        + "</tr>\n";
            }

            usertext = usertext + "<tr><td align=\"left\" style=\"width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; \"><label>Address:&nbsp;&nbsp;</label></td>\n"
                    + "<td  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + add + "</td>\n"
                    + "</tr>\n"
                    + "<tr><td align=\"left\" style=\"width:100px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC;\"><label>Profession:&nbsp;&nbsp;</label></td>\n"
                    + "<td  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + prof + "</td>\n"
                    + "</tr>\n"
                    + "<tr><td align=\"left\" style=\"width:100px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; \"><label>Purpose:&nbsp;&nbsp;</label></td>\n"
                    + "<td  style=\"padding: 0.6em; border: 1px solid #CCC; \">" + purpose + "</td>\n"
                    + "</tr>\n"
                    + "<br>\n"
                    + "<tr></tr>\n"
                    + "<tr></tr>\n"
                    + "</table>\n"
                    + "</form></div>"
                    + ""
                    + "<br><h2>You Will Receive an Email Shortly After Confirmation of Your Access</h2>"
                    + "</body></html>";

            sendEmails(" New User Registration Information ", bodytext, emailIds);
            sendEmails("Registered Successfully", usertext, email + ",");

        }

        return jsonString;

    }

    //added by krishan pratap 
    public String getAllDimsDetails(String connectionID, String folderId) {
        String jsonString = null;
        PbReturnObject returnObject = null;
        Gson json = new Gson();
        List<String> bussTableId = new ArrayList<>();
        List<String> bussTableName = new ArrayList<>();
        List<String> tableDispName = new ArrayList<>();
        Map dimmap = new HashMap();
        String finalquery = "";
        finalquery = "select DISTINCT  pusft.dim_id,pusft.dim_name  from PRG_USER_SUB_FOLDER_TABLES pusft, PRG_USER_FOLDER_DETAIL pufd "
                + " where pusft.sub_folder_id =pufd.sub_folder_id and pufd.sub_folder_type='Dimensions' "
                + " and pufd.folder_id in (" + folderId + ") and pusft.dim_name!='Time' "
                + " ORDER by DIM_NAME ASC";

        try {
            returnObject = super.execSelectSQL(finalquery);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    bussTableId.add(returnObject.getFieldValueString(i, 0));
                    bussTableName.add(returnObject.getFieldValueString(i, 1));
                    tableDispName.add(returnObject.getFieldValueString(i, 1));
                }
            }
            dimmap.put("bussTableId", bussTableId);
            dimmap.put("bussTableName", bussTableName);
            dimmap.put("tableDispName", tableDispName);
            jsonString = json.toJson(dimmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;
    }

    //added by krishan pratap
    public int getTableDispDimName(String dimdesc, String newDimName, String dimId) throws Exception {

        String finalquery1 = "";
        String dimidname = "";
        finalquery1 = "select qry_dim_id from prg_grp_dimensions where dim_id=" + dimId;
        PbReturnObject pbroFolderId = execSelectSQL(finalquery1);
        dimidname = String.valueOf(pbroFolderId.getFieldValueInt(0, 0));
        String renameQryDimension = getResourceBundle().getString("renameQryDim");
        String renameGrpDimension = getResourceBundle().getString("renameGrpDim");
        String renameRepDimension = getResourceBundle().getString("renameRepDim");
        int st = 0;;
        Object updateObj[] = new Object[3];
        updateObj[0] = newDimName;
        updateObj[1] = newDimName;
        updateObj[2] = dimidname;
        Object updateObj1[] = new Object[2];
        updateObj1[0] = newDimName;
        updateObj1[1] = dimidname;
        String finrenameQryDimension = buildQuery(renameQryDimension, updateObj);
        String finrenameGrpDimension = buildQuery(renameGrpDimension, updateObj);
        String finrenameRepDimension = buildQuery(renameRepDimension, updateObj1);
        try {
            execModifySQL(finrenameQryDimension);
            execModifySQL(finrenameGrpDimension);
            execModifySQL(finrenameRepDimension);
            st = 1;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return st;
    }

    //Added by Ashutosh for Restricted Power Analyzer 11-12-2015
    public String isReportCreator(String userId, String reportId) throws SQLException {
        HashMap<String, Boolean> map = new HashMap();
        PbReturnObject pbro = new PbReturnObject();
        String USERNAME;
//        Connection conn = ProgenConnection.getInstance().getConnection();
        PbDb pbdb = new PbDb();
        //String query = "SELECT * FROM PRG_AR_USER_REPORTS WHERE USER_ID='" + userId + "' AND REPORT_ID='" + reportId + "';";
        String u_name_qry = "select PU_LOGIN_ID from PRG_AR_USERS where PU_ID=" + userId;

        try {
            pbro = pbdb.execSelectSQL(u_name_qry);
            USERNAME = pbro.getFieldValueString(0, "PU_LOGIN_ID");
            String query = "select * from PRG_AR_REPORT_MASTER where  REPORT_ID=" + reportId + " AND created_by ='" + USERNAME + "'";
            pbro = pbdb.execSelectSQL(query);
            if (pbro.rowCount > 0) {
                map.put("status", true);
                return new Gson().toJson(map);
            }
        } catch (SQLException ex) {

            logger.error("Exception:", ex);
        } catch (Exception ex) {
        }
        map.put("status", false);
        return new Gson().toJson(map);

    }

    public String getUserRole(HttpSession session) {
        HashMap userInfo = new HashMap<>();
        boolean isCompanyAccessible;
        HashMap ClientCodesMap;
        try {
            if (session != null && !(session.getAttribute("insightsUserRole").toString()).equalsIgnoreCase("")) {
                String insightsUserRole = session.getAttribute("insightsUserRole").toString();
                if (!insightsUserRole.equalsIgnoreCase("") && insightsUserRole != null) {
                    if (insightsUserRole.equalsIgnoreCase("VPA")) {
                        userInfo.put("Admin", true);
                        userInfo.put("isPowerAnalyserEnableforUser", true);
                        userInfo.put("restrictedFlag", false);
                        userInfo.put("isCompanyAccessible", true);
                    } else if (insightsUserRole.equalsIgnoreCase("PA")) {
                        userInfo.put("Admin", true);
                        userInfo.put("isPowerAnalyserEnableforUser", true);
                        userInfo.put("restrictedFlag", false);
                        userInfo.put("isCompanyAccessible", true);
                    } else if (insightsUserRole.equalsIgnoreCase("RPA")) {
                        userInfo.put("Admin", false);
                        userInfo.put("isPowerAnalyserEnableforUser", true);
                        userInfo.put("restrictedFlag", true);
                        ClientCodesMap = (HashMap) session.getAttribute("ClientCodesMap");
                        isCompanyAccessible = ClientCodesMap.containsKey(session.getAttribute("clientCode").toString());
                        userInfo.put("isCompanyAccessible", isCompanyAccessible);
                    } else if (insightsUserRole.equalsIgnoreCase("A")) {
                        userInfo.put("Admin", false);
                        userInfo.put("isPowerAnalyserEnableforUser", false);
                        userInfo.put("restrictedFlag", false);
                        userInfo.put("isCompanyAccessible", false);
                    }
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return new Gson().toJson(userInfo);
    }
    //Code Ended by Ashutosh
}
