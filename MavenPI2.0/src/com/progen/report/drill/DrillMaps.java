/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.drill;

import com.progen.validate.checkValues;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class DrillMaps {

    private StringBuffer elementId = new StringBuffer();
    private StringBuffer userId = new StringBuffer();
    private StringBuffer reportId = new StringBuffer();
    private ArrayList ParamList = new ArrayList();
    private HashMap ParamMap = new HashMap();
    private checkValues checkValid = new checkValues();
    public HashMap MemberDrillMap = new HashMap();
    public HashMap RepDrillMap = new HashMap();
    public static Logger logger = Logger.getLogger(DrillMaps.class);

    public HashMap<String, String> getDrillForReport(String reportId, String userId, String elementId, HashMap reportParameterValues) {
        this.reportId.append(reportId);
        this.userId.append(userId);
        this.elementId.append(elementId);
        this.ParamMap = reportParameterValues;
        return this.getDrillForReport();
    }

    public HashMap<String, String> getDrillForReport() {
        HashMap<String, String> ChildMap = new LinkedHashMap<String, String>();
        MemberDrillMap = new HashMap();
        PbDb pbdb = new PbDb();
        PbReturnObject retObj = new PbReturnObject();
        if (!checkValid.checkNonZeroEmpty(reportId)) {
            return getDrillForUser();
        }

        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                String truncatetable = " truncate table PRG_USER_DIM_RPT_MST_V ";
                StringBuffer insertQuery = new StringBuffer(" insert into PRG_USER_DIM_RPT_MST_V SELECT M.USER_ID,"
                        + " D1.INFO_FOLDER_ID INFO_FOLDER_ID, M.REPORT_ID , M.FOLDER_ID, m.member_id P_member_ID, D1.Key_DIM_ID,D1.Key_DIM_NAME,"
                        + " d1.info_member_name P_member_name,D1.INFO_ELEMENT_ID P_element_id,D1.VAL_ELEMENT_ID PVAL_element_id,M.CHILD_MEMBER_ID,"
                        + " d2.info_member_name C_member_name,D2.INFO_ELEMENT_ID C_element_id,D2.VAL_ELEMENT_ID CVAL_element_id,M.ASSIGNED_TYPE "
                        + " FROM "
                        + " (SELECT R.USER_ID,R.REPORT_ID,R.MEMBER_ID, R.CHILD_MEMBER_ID,R.ASSIGNED_TYPE,RD.FOLDER_ID FROM PRG_REPORT_CUSTOM_DRILL R,"
                        + " PRG_AR_REPORT_DETAILS RD WHERE r.report_id = rd.report_id "
                        + "  ) M"
                        + " INNER JOIN PRG_USER_DIM_REL_MST_V D1 ON (D1.INFO_MEMBER_ID = M.MEMBER_ID AND D1.keY_folder_id  = M.folder_id)  LEFT JOIN PRG_USER_DIM_REL_MST_V D2"
                        + " ON ( D2.INFO_MEMBER_ID = M.CHILD_MEMBER_ID AND d2.keY_folder_id   =m.folder_id) WHERE D1.Key_DIM_NAME !='Time' ");
                pbdb.execUpdateSQL(truncatetable);
                pbdb.execUpdateSQL(insertQuery.toString());
                StringBuffer sqlstr = new StringBuffer(" Select  FOLDER_ID,  INFO_FOLDER_ID,  p_MEMBER_ID, "
                        + "  KEY_DIM_ID,   KEY_DIM_NAME,   P_MEMBER_NAME,"
                        + " P_ELEMENT_ID,   PVAL_ELEMENT_ID,      C_MEMBER_NAME, "
                        + "  C_ELEMENT_ID,   CVAL_ELEMENT_ID , REPORT_ID,C_ASSIGNED_TYPE ,CHILD_MEMBER_ID"
                        + "  from PRG_USER_DIM_RPT_MST_V "
                        + " where folder_id in (select folder_id from PRG_USER_ALL_INFO_DETAILS where"
                        + " report_id in (" + reportId + ")) and "
                        + " USER_ID = (" + userId + ") ");
                //
                retObj = pbdb.execSelectSQL(sqlstr.toString());
            } else {
                StringBuffer sqlstr = new StringBuffer(" Select  FOLDER_ID,  INFO_FOLDER_ID,  p_MEMBER_ID, "
                        + "  KEY_DIM_ID,   KEY_DIM_NAME,   P_MEMBER_NAME,"
                        + " P_ELEMENT_ID,   PVAL_ELEMENT_ID,      C_MEMBER_NAME, "
                        + "  C_ELEMENT_ID,   CVAL_ELEMENT_ID , REPORT_ID,C_ASSIGNED_TYPE ,CHILD_MEMBER_ID"
                        + "  from PRG_USER_DIM_RPT_MST_V "
                        + " where folder_id in (select folder_id from PRG_USER_ALL_INFO_DETAILS where"
                        + " report_id in (" + reportId + ")) and "
                        + " USER_ID = (" + userId + ") ");
                //.out.println("sqlstr  11 "+sqlstr);
                retObj = pbdb.execSelectSQL(sqlstr.toString());
            }
            if (retObj != null && retObj.rowCount > 0) {
                for (int loop = 0; loop < retObj.rowCount; loop++) {
                    if (retObj.getFieldValue(loop, "C_ASSIGNED_TYPE").toString().equalsIgnoreCase("R")) {
                        RepDrillMap.put(retObj.getFieldValueString(loop, "P_ELEMENT_ID"), retObj.getFieldValueString(loop, "CHILD_MEMBER_ID"));
                    } else {
                        ChildMap.put(retObj.getFieldValueString(loop, "P_ELEMENT_ID"), retObj.getFieldValueString(loop, "C_ELEMENT_ID"));
                        MemberDrillMap.put(retObj.getFieldValue(loop, "P_MEMBER_NAME"), retObj.getFieldValue(loop, "C_MEMBER_NAME"));
                    }
                }

            } else {
                ChildMap = getDrillForUser();
            }


        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }




        return (ChildMap);
    }

    public HashMap<String, String> getDrillForUser() {
        HashMap<String, String> ChildMap = new LinkedHashMap<String, String>();
        MemberDrillMap = new HashMap();
        PbDb pbdb = new PbDb();
        PbReturnObject retObj = new PbReturnObject();
        if (!checkValid.checkNonZeroEmpty(userId)) {
            return getDrillForRole();
        }

        try {
            if (!elementId.toString().equalsIgnoreCase("TIME") && !elementId.toString().equalsIgnoreCase("KPI") && !elementId.toString().equalsIgnoreCase("None")) {
                StringBuffer sqlstr = new StringBuffer(" Select  FOLDER_ID,  SUB_FOLDER_ID,  MEMBER_ID, "
                        + "  KEY_DIM_ID,   KEY_DIM_NAME,   P_MEMBER_NAME,"
                        + " P_ELEMENT_ID,   PVAL_ELEMENT_ID,      C_MEMBER_NAME, "
                        + "  C_ELEMENT_ID,   CVAL_ELEMENT_ID"
                        + "  from PRG_USER_DIM_USR_MST_V "
                        + " where folder_id in (select folder_id from PRG_USER_ALL_INFO_DETAILS where"
                        + " element_id in (" + elementId + ")) and "
                        + " USER_ID = (" + userId + ") ");


                retObj = pbdb.execSelectSQL(sqlstr.toString());
            }

            if (retObj != null && retObj.rowCount > 0) {
                for (int loop = 0; loop < retObj.rowCount; loop++) {
                    ChildMap.put(retObj.getFieldValueString(loop, "P_ELEMENT_ID"), retObj.getFieldValueString(loop, "C_ELEMENT_ID"));
                    MemberDrillMap.put(retObj.getFieldValue(loop, "P_MEMBER_NAME"), retObj.getFieldValue(loop, "C_MEMBER_NAME"));
                }

            } else {
                ChildMap = getDrillForRole();
            }


        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }




        return (ChildMap);
    }

    public HashMap<String, String> getDrillForRole() {
        HashMap<String, String> ChildMap = new LinkedHashMap<String, String>();
        MemberDrillMap = new HashMap();
        PbDb pbdb = new PbDb();
        PbReturnObject retObj = new PbReturnObject();
        if (!checkValid.checkNonZeroEmpty(elementId)) {
            return ChildMap;
        }


        try {
            if (!elementId.toString().equalsIgnoreCase("TIME") && !elementId.toString().equalsIgnoreCase("KPI") && !elementId.toString().equalsIgnoreCase("None")) {
                StringBuffer sqlstr = new StringBuffer(" Select  FOLDER_ID,  SUB_FOLDER_ID,  MEMBER_ID, "
                        + "  KEY_DIM_ID,   KEY_DIM_NAME,   P_MEMBER_NAME,"
                        + " P_ELEMENT_ID,   PVAL_ELEMENT_ID,   CHILD_MEMBER_ID,   C_MEMBER_NAME, "
                        + "  C_ELEMENT_ID,   CVAL_ELEMENT_ID"
                        + "  from PRG_USER_DIM_ROLE_MST_V "
                        + " where folder_id in (select folder_id from PRG_USER_ALL_INFO_DETAILS where"
                        + " element_id in (" + elementId + ")) ");


//            
                retObj = pbdb.execSelectSQL(sqlstr.toString());
            }

            if (retObj != null && retObj.rowCount > 0) {
                for (int loop = 0; loop < retObj.rowCount; loop++) {
                    ChildMap.put(retObj.getFieldValueString(loop, "P_ELEMENT_ID"), retObj.getFieldValueString(loop, "C_ELEMENT_ID"));
                    MemberDrillMap.put(retObj.getFieldValue(loop, "P_MEMBER_NAME"), retObj.getFieldValue(loop, "C_MEMBER_NAME"));
                }

            } else {
                ChildMap = getDrillForDimension();
            }


        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }




        return (ChildMap);
    }

    public HashMap<String, String> getDrillForDimension() {
        HashMap<String, String> ChildMap = new LinkedHashMap<String, String>();
        MemberDrillMap = new HashMap();
        PbDb pbdb = new PbDb();
        PbReturnObject retObj = new PbReturnObject();
        if (!checkValid.checkNonZeroEmpty(elementId)) {
            return ChildMap;
        }



        try {
            if (!elementId.toString().equalsIgnoreCase("Time") && !elementId.toString().equalsIgnoreCase("KPI") && !elementId.toString().equalsIgnoreCase("None")) {
                StringBuffer sqlstr = new StringBuffer(" SELECT   REL_LEVEL,  INFO_MEMBER_ID,   INFO_ELEMENT_ID,   INFO_MEMBER_NAME,   LEVEL1,   KEY_FOLDER_ID, "
                        + "    KEY_FOLDER_NAME,  REL_NAME,   IS_DEFAULT, KEY_DIM_ID "
                        + " FROM PRG_USER_DIM_REL_MST_V M , PRG_USER_ALL_INFO_DETAILS D  "
                        + "  where M.KEY_FOLDER_ID =d.folder_id "
                        + "  and d.element_id =" + elementId + "  "
                        + "   order by KEY_DIM_ID, REL_LEVEL desc ");


//            
                retObj = pbdb.execSelectSQL(sqlstr.toString());
            }

            if (retObj != null && retObj.rowCount > 0) {


                if (retObj.rowCount > 1) {
                    for (int loop = 1; loop < retObj.rowCount; loop++) {
                        if (retObj.getFieldValueString(loop - 1, "KEY_DIM_ID").equalsIgnoreCase(retObj.getFieldValueString(loop, "KEY_DIM_ID"))) {
                            ChildMap.put(retObj.getFieldValueString(loop, "INFO_MEMBER_ID"), retObj.getFieldValueString(loop - 1, "INFO_MEMBER_ID"));
                            MemberDrillMap.put(retObj.getFieldValue(loop, "INFO_MEMBER_NAME"), retObj.getFieldValue(loop - 1, "INFO_MEMBER_NAME"));
                        } else {
                            ChildMap.put(retObj.getFieldValueString(loop, "INFO_MEMBER_ID"), retObj.getFieldValueString(loop, "INFO_MEMBER_ID"));
                            MemberDrillMap.put(retObj.getFieldValue(loop, "INFO_MEMBER_NAME"), retObj.getFieldValue(loop, "INFO_MEMBER_NAME"));
                        }

                    }

                } else {
                    ChildMap.put(retObj.getFieldValueString(0, "INFO_MEMBER_ID"), retObj.getFieldValueString(0, "INFO_MEMBER_ID"));
                    MemberDrillMap.put(retObj.getFieldValue(0, "INFO_MEMBER_NAME"), retObj.getFieldValue(0, "INFO_MEMBER_NAME"));

                }


            } else {
            }
            //  
            // 


        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }


        return (ChildMap);

    }

    public ArrayList getParamList() {
        return ParamList;
    }

    public void setParamList(ArrayList ParamList) {
        this.ParamList = ParamList;
    }

    public HashMap getParamMap() {
        return ParamMap;
    }

    public void setParamMap(HashMap ParamMap) {
        this.ParamMap = ParamMap;
    }

    public StringBuffer getElementId() {
        return elementId;
    }

    public void setElementId(StringBuffer elementId) {
        this.elementId = elementId;
    }

    public StringBuffer getReportId() {
        return reportId;
    }

    public void setReportId(StringBuffer reportId) {
        this.reportId = reportId;
    }

    public StringBuffer getUserId() {
        return userId;
    }

    public void setUserId(StringBuffer userId) {
        this.userId = userId;
    }

    public HashMap<String, String> getRepDrillForReport(String reportId, String userId, String elementId, HashMap reportParameterValues) {
        this.reportId.append(reportId);
        this.userId.append(userId);
        this.elementId.append(elementId);
        this.ParamMap = reportParameterValues;
        return this.getRepDrillMap();
    }

    public HashMap<String, String> getRepDrillMap() {
        HashMap<String, String> ChildMap = new LinkedHashMap<String, String>();
        MemberDrillMap = new HashMap();
        PbDb pbdb = new PbDb();
        PbReturnObject retObj = new PbReturnObject();
        if (!checkValid.checkNonZeroEmpty(reportId)) {
            return getDrillForUser();
        }

        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                String truncatetable = " truncate table PRG_USER_DIM_RPT_MST_V ";
                StringBuffer insertQuery = new StringBuffer(" insert into PRG_USER_DIM_RPT_MST_V SELECT M.USER_ID,"
                        + " D1.INFO_FOLDER_ID INFO_FOLDER_ID, M.REPORT_ID , M.FOLDER_ID, m.member_id P_member_ID, D1.Key_DIM_ID,D1.Key_DIM_NAME,"
                        + " d1.info_member_name P_member_name,D1.INFO_ELEMENT_ID P_element_id,D1.VAL_ELEMENT_ID PVAL_element_id,M.CHILD_MEMBER_ID,"
                        + " d2.info_member_name C_member_name,D2.INFO_ELEMENT_ID C_element_id,D2.VAL_ELEMENT_ID CVAL_element_id,M.ASSIGNED_TYPE "
                        + " FROM "
                        + " (SELECT R.USER_ID,R.REPORT_ID,R.MEMBER_ID, R.CHILD_MEMBER_ID,R.ASSIGNED_TYPE,RD.FOLDER_ID FROM PRG_REPORT_CUSTOM_DRILL R,"
                        + " PRG_AR_REPORT_DETAILS RD WHERE r.report_id = rd.report_id "
                        + "  ) M"
                        + " INNER JOIN PRG_USER_DIM_REL_MST_V D1 ON (D1.INFO_MEMBER_ID = M.MEMBER_ID AND D1.keY_folder_id  = M.folder_id)  LEFT JOIN PRG_USER_DIM_REL_MST_V D2"
                        + " ON ( D2.INFO_MEMBER_ID = M.CHILD_MEMBER_ID AND d2.keY_folder_id   =m.folder_id) WHERE D1.Key_DIM_NAME !='Time' ");
                pbdb.execUpdateSQL(truncatetable);
                pbdb.execUpdateSQL(insertQuery.toString());
                StringBuffer sqlstr = new StringBuffer(" Select  FOLDER_ID,  INFO_FOLDER_ID,  p_MEMBER_ID, "
                        + "  KEY_DIM_ID,   KEY_DIM_NAME,   P_MEMBER_NAME,"
                        + " P_ELEMENT_ID,   PVAL_ELEMENT_ID,      C_MEMBER_NAME, "
                        + "  C_ELEMENT_ID,   CVAL_ELEMENT_ID , REPORT_ID,C_ASSIGNED_TYPE ,CHILD_MEMBER_ID"
                        + "  from PRG_USER_DIM_RPT_MST_V "
                        + " where folder_id in (select folder_id from PRG_USER_ALL_INFO_DETAILS where"
                        + " report_id in (" + reportId + ")) and "
                        + " USER_ID = (" + userId + ") ");
                //   
                retObj = pbdb.execSelectSQL(sqlstr.toString());
            } else {
                StringBuffer sqlstr = new StringBuffer(" Select  FOLDER_ID,  INFO_FOLDER_ID,  p_MEMBER_ID, "
                        + "  KEY_DIM_ID,   KEY_DIM_NAME,   P_MEMBER_NAME,"
                        + " P_ELEMENT_ID,   PVAL_ELEMENT_ID,      C_MEMBER_NAME, "
                        + "  C_ELEMENT_ID,   CVAL_ELEMENT_ID , REPORT_ID,C_ASSIGNED_TYPE ,CHILD_MEMBER_ID"
                        + "  from PRG_USER_DIM_RPT_MST_V "
                        + " where folder_id in (select folder_id from PRG_USER_ALL_INFO_DETAILS where"
                        + " report_id in (" + reportId + ")) and "
                        + " USER_ID = (" + userId + ") ");


                //  
                retObj = pbdb.execSelectSQL(sqlstr.toString());
            }
            if (retObj != null && retObj.rowCount > 0) {
                for (int loop = 0; loop < retObj.rowCount; loop++) {
                    if (retObj.getFieldValue(loop, "C_ASSIGNED_TYPE").toString().equalsIgnoreCase("R")) {
                        RepDrillMap.put(retObj.getFieldValueString(loop, "P_ELEMENT_ID"), retObj.getFieldValueString(loop, "CHILD_MEMBER_ID"));
                    } else {
                        ChildMap.put(retObj.getFieldValueString(loop, "P_ELEMENT_ID"), retObj.getFieldValueString(loop, "C_ELEMENT_ID"));
                        MemberDrillMap.put(retObj.getFieldValue(loop, "P_MEMBER_NAME"), retObj.getFieldValue(loop, "C_MEMBER_NAME"));
                    }
                }

            } else {
                ChildMap = getDrillForUser();
            }


        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }




        return (RepDrillMap);
    }
}
