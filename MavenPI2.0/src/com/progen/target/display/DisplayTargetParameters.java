package com.progen.target.display;

import com.progen.target.PbTargetCollection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;
import org.apache.log4j.Logger;
import prg.business.group.BusinessGroupDAO;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

public class DisplayTargetParameters {

    public static Logger logger = Logger.getLogger(DisplayTargetParameters.class);
    public String tarId;
    PbTargetCollection collect = new PbTargetCollection();
    //PbTargetCollection collect = (PbTargetCollection)getCollectionObj();
    ProgenParam f1 = new ProgenParam();
    public int totalParam = 0;
    String result = "";
    PbDb pbDb = new PbDb();
    DisplayTargetParametersResourceBundle resundle = new DisplayTargetParametersResourceBundle();
    BusinessGroupDAO bgdao = new BusinessGroupDAO();
    String tempPass = "";
    int j = 0;

    public String getParameterQuery(String elementID) {
        PbReturnObject retObj = null;
        String[] colNames;
        String temp;
        String sqlstr = "";
        String finalQuery = "";

        Object[] Obj = null;
        try {
            sqlstr = resundle.getString("getParameterQuery1");
            Obj = new Object[1];
            Obj[0] = elementID;

            finalQuery = pbDb.buildQuery(sqlstr, Obj);
            retObj = pbDb.execSelectSQL(finalQuery);

            colNames = retObj.getColumnNames();
            int psize = retObj.getRowCount();
            if (psize > 0) {
                sqlstr = "";
                sqlstr += "select distinct  " + retObj.getFieldValue(0, colNames[0]);
                sqlstr += " A1 ,  " + retObj.getFieldValue(0, colNames[0]);
                sqlstr += "  A2   " + bgdao.viewBussDataWithColSingle(retObj.getFieldValue(0, colNames[1]).toString());
                sqlstr += " ";
                tempPass = retObj.getFieldValue(0, colNames[2]).toString();
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        // sqlstr +=sqlstr+" where 1=1";
        return (sqlstr);
    }

    public String getParameterInfo(String elementID) {
        PbReturnObject retObj = null;
        String[] colNames;
//        String temp;
        String sqlstr = "";
        String finalQuery = "";

        Object[] Obj = null;
        try {
            sqlstr = resundle.getString("getParameterQuery1");
            Obj = new Object[1];
            Obj[0] = elementID;

            finalQuery = pbDb.buildQuery(sqlstr, Obj);
            retObj = pbDb.execSelectSQL(finalQuery);

            colNames = retObj.getColumnNames();
            int psize = retObj.getRowCount();
            if (psize > 0) {

                tempPass = retObj.getFieldValue(0, colNames[2]).toString();
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return ("");
    }

    public PbReturnObject getParameterColQuery(HashMap hm, String targetId, String colElementId) throws Exception {
        String sqlStr = "";
        PbReturnObject pbro2 = new PbReturnObject();
        String childsQuery = "select * from prg_target_param_details where target_id=" + targetId + " order by dim_id, rel_level";
        String busColsQ = "select DISTINCT element_id, buss_col_name from prg_user_all_info_details where folder_id "
                + " in(select bus_folder_id from target_measure_folder where "
                + " bus_group_id=(select business_group from prg_target_master where prg_measure_id "
                + " in(select measure_id from target_master WHERE target_master.target_id=" + targetId + ")))";
        PbReturnObject finbusColsObj = pbDb.execSelectSQL(busColsQ);
        HashMap busCols = new HashMap();
        for (int l = 0; l < finbusColsObj.getRowCount(); l++) {
            busCols.put(finbusColsObj.getFieldValueString(l, "ELEMENT_ID"), finbusColsObj.getFieldValueString(l, "BUSS_COL_NAME"));
        }

        PbReturnObject pbro = pbDb.execSelectSQL(childsQuery);
        HashMap dimChilds = new HashMap();
        HashMap elementsDetails = new HashMap();
        ArrayList busTabIds = new ArrayList();

        for (int m = 0; m < pbro.getRowCount(); m++) {
            String dimId = pbro.getFieldValueString(m, "DIM_ID");
            String childElementId = pbro.getFieldValueString(m, "ELEMENT_ID");
            if (dimChilds.containsKey(dimId)) {
                ArrayList det = (ArrayList) dimChilds.get(dimId);
                if (!childElementId.equalsIgnoreCase("-1")) {
                    det.add(childElementId);
                }
                dimChilds.put(dimId, det);

            } else {
                ArrayList det = new ArrayList();
                if (!childElementId.equalsIgnoreCase("-1")) {
                    det.add(childElementId);
                }
                dimChilds.put(dimId, det);
            }
            ArrayList eleDets = new ArrayList();
            eleDets.add(0, pbro.getFieldValueString(m, "DIM_ID"));
            eleDets.add(1, pbro.getFieldValueString(m, "CHILD_ELEMENT_ID"));
            eleDets.add(2, pbro.getFieldValueString(m, "BUSS_TABLE"));
            elementsDetails.put(pbro.getFieldValueString(m, "ELEMENT_ID"), eleDets);
            if (!busTabIds.contains(pbro.getFieldValueString(m, "BUSS_TABLE"))) {
                busTabIds.add(pbro.getFieldValueString(m, "BUSS_TABLE"));
            }
        }

        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);
        Connection con = null;
        this.totalParam = a1.length;

        HashMap dimsChilds = new HashMap();
        for (int i = 0; i < a1.length; i++) {
            HashMap childValues = new HashMap();

            ArrayList a = (ArrayList) hm.get(a1[i]);
            if (hm.containsKey(a.get(0).toString())) {
                ArrayList detVals = (ArrayList) hm.get(a.get(0).toString());

                if (dimsChilds.containsKey(a.get(3).toString())) {
                    childValues = (HashMap) dimsChilds.get(a.get(3).toString());
                    childValues.put(a.get(0).toString(), detVals.get(8).toString());
                    dimsChilds.put(a.get(3).toString(), childValues);
                } else {
                    childValues.put(a.get(0).toString(), detVals.get(8).toString());
                    dimsChilds.put(a.get(3).toString(), childValues);
                }

            }
        }

        for (int i = 0; i < a1.length; i++) {
            ArrayList a = (ArrayList) hm.get(a1[i]);
            String elementId = a.get(0).toString();
            if (colElementId.equalsIgnoreCase(elementId)) {
                f1.elementId = elementId;
                ArrayList childElements = new ArrayList();
                ArrayList ColViewbyCols = new ArrayList();
                if (dimChilds.containsKey(a.get(3).toString())) {
                    childElements = (ArrayList) dimChilds.get(a.get(3).toString());
                }

                HashMap newHm2 = new HashMap();
                boolean flag = false;
                HashMap newHm = new HashMap();
                if (dimsChilds.containsKey(a.get(3).toString())) {
                    newHm = (HashMap) dimsChilds.get(a.get(3).toString());
                    if (newHm.containsKey(a.get(3).toString())) {
                        newHm.remove(a.get(3).toString());
                    }
                }

                for (int p = childElements.size() - 1; p >= 0; p--) {
                    String eleId = childElements.get(p).toString();
                    if (flag == true) {
                        ArrayList eleDet = (ArrayList) elementsDetails.get(eleId);
                        String busCol = "";
                        if (busCols.containsKey(eleId)) {
                            busCol = (String) busCols.get(eleId);
                            ColViewbyCols.add(busCol);
                            if (newHm.containsKey(eleId)) {
                                String a2 = (String) newHm.get(eleId);
                                newHm2.put(eleId, a2);
                            }
                        }

                    }
                    if (eleId.equalsIgnoreCase(a.get(0).toString())) {
                        flag = true;
                    }

                }

//                String whereClause = getWhereClause(newHm2, ColViewbyCols);
                StringBuilder whereClause = new StringBuilder(getWhereClause(newHm2, ColViewbyCols));
//                String finalWhereClause = "";
                if (whereClause.indexOf("and") == 1) {
//                    whereClause = whereClause.substring(4);
//                    whereClause = " where " + whereClause;
                    whereClause.append(whereClause.substring(4));
                    whereClause.append(" where ").append(whereClause);
                }
//                finalWhereClause = whereClause;
//               sqlStr = getParameterQuery(a1[i]) + finalWhereClause;
                sqlStr = getParameterQuery(a1[i]) + whereClause;
            }
        }
        pbro2 = f1.getRowEdgesRetObj(sqlStr, colElementId);
        return pbro2;
    }

    public String getParameterRowQuery(HashMap hm, String targetId, String colElementId) throws Exception {
        String sqlStr = "";
        PbReturnObject pbro2 = new PbReturnObject();
        String childsQuery = "select * from prg_target_param_details where target_id=" + targetId + " order by dim_id, rel_level";
        String busColsQ = "select DISTINCT element_id, buss_col_name from prg_user_all_info_details where folder_id "
                + " in(select bus_folder_id from target_measure_folder where "
                + " bus_group_id=(select business_group from prg_target_master where prg_measure_id "
                + " in(select measure_id from target_master WHERE target_master.target_id=" + targetId + ")))";
        PbReturnObject finbusColsObj = pbDb.execSelectSQL(busColsQ);
        HashMap busCols = new HashMap();
        for (int l = 0; l < finbusColsObj.getRowCount(); l++) {
            busCols.put(finbusColsObj.getFieldValueString(l, "ELEMENT_ID"), finbusColsObj.getFieldValueString(l, "BUSS_COL_NAME"));
        }

        PbReturnObject pbro = pbDb.execSelectSQL(childsQuery);
        HashMap dimChilds = new HashMap();
        HashMap elementsDetails = new HashMap();
        ArrayList busTabIds = new ArrayList();

        for (int m = 0; m < pbro.getRowCount(); m++) {
            String dimId = pbro.getFieldValueString(m, "DIM_ID");
            String childElementId = pbro.getFieldValueString(m, "ELEMENT_ID");
            if (dimChilds.containsKey(dimId)) {
                ArrayList det = (ArrayList) dimChilds.get(dimId);
                if (!childElementId.equalsIgnoreCase("-1")) {
                    det.add(childElementId);
                }
                dimChilds.put(dimId, det);

            } else {
                ArrayList det = new ArrayList();
                if (!childElementId.equalsIgnoreCase("-1")) {
                    det.add(childElementId);
                }
                dimChilds.put(dimId, det);
            }
            ArrayList eleDets = new ArrayList();
            eleDets.add(0, pbro.getFieldValueString(m, "DIM_ID"));
            eleDets.add(1, pbro.getFieldValueString(m, "CHILD_ELEMENT_ID"));
            eleDets.add(2, pbro.getFieldValueString(m, "BUSS_TABLE"));
            elementsDetails.put(pbro.getFieldValueString(m, "ELEMENT_ID"), eleDets);
            if (!busTabIds.contains(pbro.getFieldValueString(m, "BUSS_TABLE"))) {
                busTabIds.add(pbro.getFieldValueString(m, "BUSS_TABLE"));
            }
        }

        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);
        Connection con = null;
        this.totalParam = a1.length;

        HashMap dimsChilds = new HashMap();
        for (int i = 0; i < a1.length; i++) {
            HashMap childValues = new HashMap();
            ArrayList a = (ArrayList) hm.get(a1[i]);
            if (hm.containsKey(a.get(0).toString())) {
                ArrayList detVals = (ArrayList) hm.get(a.get(0).toString());

                if (dimsChilds.containsKey(a.get(3).toString())) {
                    childValues = (HashMap) dimsChilds.get(a.get(3).toString());
                    childValues.put(a.get(0).toString(), detVals.get(8).toString());
                    dimsChilds.put(a.get(3).toString(), childValues);
                } else {
                    childValues.put(a.get(0).toString(), detVals.get(8).toString());
                    dimsChilds.put(a.get(3).toString(), childValues);
                }

            }
        }

        // ////////////////////////////////////////////////////////.println(" dimsChilds -- "+dimsChilds);
        for (int i = 0; i < a1.length; i++) {
            ArrayList a = (ArrayList) hm.get(a1[i]);
            String elementId = a.get(0).toString();
            if (colElementId.equalsIgnoreCase(elementId)) {
                f1.elementId = elementId;
                //  ////////////////////////////////////////////////////////.println(a.get(0).toString()+" ....////// a --- "+a);
                ArrayList childElements = new ArrayList();
                ArrayList ColViewbyCols = new ArrayList();
                if (dimChilds.containsKey(a.get(3).toString())) {
                    childElements = (ArrayList) dimChilds.get(a.get(3).toString());
                }
                HashMap newHm2 = new HashMap();
                boolean flag = false;
                HashMap newHm = new HashMap();
                if (dimsChilds.containsKey(a.get(3).toString())) {
                    newHm = (HashMap) dimsChilds.get(a.get(3).toString());
                    if (newHm.containsKey(a.get(3).toString())) {
                        newHm.remove(a.get(3).toString());
                    }
                }

                for (int p = childElements.size() - 1; p >= 0; p--) {
                    String eleId = childElements.get(p).toString();
                    if (flag == true) {
                        ArrayList eleDet = (ArrayList) elementsDetails.get(eleId);
                        String busCol = "";
                        if (busCols.containsKey(eleId)) {
                            busCol = (String) busCols.get(eleId);
                            ColViewbyCols.add(busCol);
                            if (newHm.containsKey(eleId)) {
                                String a2 = (String) newHm.get(eleId);
                                newHm2.put(eleId, a2);
                            }
                        }

                    }
                    if (eleId.equalsIgnoreCase(a.get(0).toString())) {
                        flag = true;
                    }

                }
//                 String whereClause = getWhereClause(newHm2, ColViewbyCols);
                StringBuilder whereClause = new StringBuilder(getWhereClause(newHm2, ColViewbyCols));
//                String finalWhereClause = "";
                if (whereClause.indexOf("and") == 1) {
//                    whereClause = whereClause.substring(4);
//                    whereClause = " where " + whereClause;
                    whereClause.append(whereClause.substring(4));
                    whereClause.append(" where ").append(whereClause);
                }
//                finalWhereClause = whereClause;
//                sqlStr = getParameterQuery(a1[i]) + finalWhereClause;
                sqlStr = getParameterQuery(a1[i]) + whereClause;
            }
        }
        return sqlStr;
    }

    public String displayParams(HashMap hm, String targetId) throws SQLException, Exception {
        String childsQuery = "select * from prg_target_param_details where target_id=" + targetId + " order by dim_id, rel_level";
        String busColsQ = "select DISTINCT element_id, buss_col_name from prg_user_all_info_details where folder_id "
                + " in(select bus_folder_id from target_measure_folder where "
                + " bus_group_id=(select business_group from prg_target_master where prg_measure_id "
                + " in(select measure_id from target_master WHERE target_master.target_id=" + targetId + ")))";
        PbReturnObject finbusColsObj = pbDb.execSelectSQL(busColsQ);
        HashMap busCols = new HashMap();
        for (int l = 0; l < finbusColsObj.getRowCount(); l++) {
            busCols.put(finbusColsObj.getFieldValueString(l, "ELEMENT_ID"), finbusColsObj.getFieldValueString(l, "BUSS_COL_NAME"));
        }

        PbReturnObject pbro = pbDb.execSelectSQL(childsQuery);
        HashMap dimChilds = new HashMap();
        HashMap elementsDetails = new HashMap();
        ArrayList busTabIds = new ArrayList();

        for (int m = 0; m < pbro.getRowCount(); m++) {
            String dimId = pbro.getFieldValueString(m, "DIM_ID");
            String childElementId = pbro.getFieldValueString(m, "ELEMENT_ID");
            if (dimChilds.containsKey(dimId)) {
                ArrayList det = (ArrayList) dimChilds.get(dimId);
                if (!childElementId.equalsIgnoreCase("-1")) {
                    det.add(childElementId);
                }
                dimChilds.put(dimId, det);

            } else {
                ArrayList det = new ArrayList();
                if (!childElementId.equalsIgnoreCase("-1")) {
                    det.add(childElementId);
                }
                dimChilds.put(dimId, det);
            }
            ArrayList eleDets = new ArrayList();
            eleDets.add(0, pbro.getFieldValueString(m, "DIM_ID"));
            eleDets.add(1, pbro.getFieldValueString(m, "CHILD_ELEMENT_ID"));
            eleDets.add(2, pbro.getFieldValueString(m, "BUSS_TABLE"));
            elementsDetails.put(pbro.getFieldValueString(m, "ELEMENT_ID"), eleDets);
            if (!busTabIds.contains(pbro.getFieldValueString(m, "BUSS_TABLE"))) {
                busTabIds.add(pbro.getFieldValueString(m, "BUSS_TABLE"));
            }
        }

        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);
//        Connection con = null;
        this.totalParam = a1.length;
        HashMap dimsChilds = new HashMap();
        for (int i = 0; i < a1.length; i++) {
            HashMap childValues = new HashMap();
            ArrayList a = (ArrayList) hm.get(a1[i]);
            if (hm.containsKey(a.get(0).toString())) {
                ArrayList detVals = (ArrayList) hm.get(a.get(0).toString());
                // if(!detVals.get(8).toString().equalsIgnoreCase("All"))
                //  {

                if (dimsChilds.containsKey(a.get(3).toString())) {
                    childValues = (HashMap) dimsChilds.get(a.get(3).toString());
                    childValues.put(a.get(0).toString(), detVals.get(8).toString());
                    dimsChilds.put(a.get(3).toString(), childValues);
                } else {
                    childValues.put(a.get(0).toString(), detVals.get(8).toString());
                    dimsChilds.put(a.get(3).toString(), childValues);
                }
                //  }
            }
        }
        for (int i = 0; i < a1.length; i++) {
            ArrayList a = (ArrayList) hm.get(a1[i]);
            String elementId = a.get(0).toString();

            f1.elementId = elementId;
            ArrayList childElements = new ArrayList();
            ArrayList ColViewbyCols = new ArrayList();
            if (dimChilds.containsKey(a.get(3).toString())) {
                childElements = (ArrayList) dimChilds.get(a.get(3).toString());
            }

            HashMap newHm2 = new HashMap();
            boolean flag = false;
            HashMap newHm = new HashMap();
            if (dimsChilds.containsKey(a.get(3).toString())) {
                newHm = (HashMap) dimsChilds.get(a.get(3).toString());
                if (newHm.containsKey(a.get(3).toString())) {
                    newHm.remove(a.get(3).toString());
                }
            }

            for (int p = childElements.size() - 1; p >= 0; p--) {
                String eleId = childElements.get(p).toString();
                if (flag == true) {
                    ArrayList eleDet = (ArrayList) elementsDetails.get(eleId);
                    String busCol = "";
                    if (busCols.containsKey(eleId)) {
                        busCol = (String) busCols.get(eleId);
                        ColViewbyCols.add(busCol);
                        if (newHm.containsKey(eleId)) {
                            String a2 = (String) newHm.get(eleId);
                            newHm2.put(eleId, a2);
                        }
                    }

                }
                if (eleId.equalsIgnoreCase(a.get(0).toString())) {
                    flag = true;
                }

            }
            String whereClause = getWhereClause(newHm2, ColViewbyCols);
            String finalWhereClause = "";
            if (whereClause.indexOf("and") == 1) {
                whereClause = whereClause.substring(4);
                whereClause = " where " + whereClause;
            }
            finalWhereClause = whereClause;

            if (j % 4 == 0) {
                result += "</Tr><Tr>";
            }

            if (a.get(5).toString().equalsIgnoreCase("SingleSelectBox(With All)")) {
                result += " <Td id=column" + i + " align=\"right\"> " + f1.getQueryAllCombotb(a.get(9).toString(), (String) a.get(1), getParameterQuery(a1[i]) + finalWhereClause, (String) a.get(8)) + " </Td> ";
            } else {
                //result += " <Td align=\"right\"> " + f1.getMultiTextBoxNew (a.get(9).toString(),(String)a.get(1),factQry.setQuery(a1[i],hm), (String)a.get(8)) + " </Td> ";
                result += " <Td id=column" + i + " align=\"right\"> " + f1.getMultiTextBoxNew(a.get(9).toString(), (String) a.get(1), (String) a.get(8), a1[i], (String) a.get(10)) + " </Td> ";

            }
            j++;

        }

        return result;
    }

    public String getPeriodTypeQuery(String key, String val, String targetId) {
        String value = val;
        String str = "";
        if (key.equalsIgnoreCase("PRG_PERIOD")) {
            if (value.equalsIgnoreCase("Day")) {
                // str = "Select VALUE, VALUE from PRG_VALUE_LOOKUP where KEY_VALUE = 'Daily' order by 2";
                str = "select level_name,level_name from targettimelevels where target_id=" + targetId + " order by level_id";
            } else if (value.equalsIgnoreCase("Month")) {
                //str = "Select VALUE, VALUE from PRG_VALUE_LOOKUP where KEY_VALUE = 'Monthly' order by 2";
                str = "select level_name,level_name from targettimelevels where target_id=" + targetId + " order by level_id";
            } else if (value.equalsIgnoreCase("Quarter") || value.equalsIgnoreCase("Qtr")) {
                //str = "Select VALUE, VALUE from PRG_VALUE_LOOKUP where KEY_VALUE = 'Quarterly' order by 2";
                str = "select level_name,level_name from targettimelevels where target_id=" + targetId + " order by level_id";
            } else if (value.equalsIgnoreCase("Year")) {
                // str = "Select VALUE, VALUE from PRG_VALUE_LOOKUP where KEY_VALUE = 'Yearly' order by 2";
                str = "select level_name,level_name from targettimelevels where target_id=" + targetId + " order by level_id";
            }

        }
        return str;
    }

    public String getMonthsQry(String key) {
        collect.targetId = tarId;
        ArrayList getMonths = collect.getSelectedMonths();
        String startMonth = (String) getMonths.get(0);
        String endMonth = (String) getMonths.get(1);
        String str = "";
        if (key.equalsIgnoreCase("AS_OF_MONTH") || key.equalsIgnoreCase("AS_OF_MONTH1")) {
            str = "select MON_NAME as view_by, MON_NAME as view_by1  from PRG_ACN_MON_DENOM "
                    + "where cm_st_date between (select distinct cm_st_date from prg_acn_mon_denom where mon_name='" + startMonth + "') and "
                    + "(select distinct cm_end_date from prg_acn_mon_denom where mon_name='" + endMonth + "') order by cyear,cmon";
        }
        return str;
    }

    public String getMonths(String key) {
        collect.targetId = tarId;
        ArrayList getMonths = collect.getSelectedMonths();
        String startMonth = (String) getMonths.get(0);
        String endMonth = (String) getMonths.get(1);
        String str = "";
        if (key.equalsIgnoreCase("AS_OF_MONTH") || key.equalsIgnoreCase("AS_OF_MONTH1")) {
            str = "select MON_NAME as view_by, MON_NAME as view_by1  from PRG_ACN_MON_DENOM "
                    + "where cm_st_date between (select distinct cm_st_date from prg_acn_mon_denom where mon_name='" + startMonth + "') and "
                    + "(select distinct cm_end_date from prg_acn_mon_denom where mon_name='" + endMonth + "') order by cyear,cmon";
        }
        return str;
    }

    public String getQtrsQry(String key) {
        collect.targetId = tarId;
        ArrayList getQtrs = collect.getSelectedQtrs();
        String targetStartQtr = (String) getQtrs.get(0);
        String targetEndQtr = (String) getQtrs.get(1);
        if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_QTR")) {
            targetStartQtr = (String) collect.targetIncomingParameters.get("CBO_AS_OF_QTR");
        }
        if (collect.targetIncomingParameters.containsKey("CBO_AS_OF_QTR1")) {
            targetEndQtr = (String) collect.targetIncomingParameters.get("CBO_AS_OF_QTR1");
        }
        String str = "";
        if (key.equalsIgnoreCase("AS_OF_QTR") || key.equalsIgnoreCase("AS_OF_QTR1")) {
            str = "select distinct cmqtr_code as view_by,cmqtr_code as view_by1,cyear from prg_acn_mon_denom "
                    + "where cmq_st_date between (select distinct cmq_st_date from prg_acn_mon_denom where cmqtr_code='" + targetStartQtr + "') and "
                    + " (select distinct cmq_st_date from prg_acn_mon_denom where cmqtr_code='" + targetEndQtr + "') order by cyear,cmqtr_code";
        }
        return str;
    }

    public String getYearsQry(String key) {
        collect.targetId = tarId;
        ArrayList getYears = collect.getSelectedYears();
        String targetStartYear = (String) getYears.get(0);
        String targetEndYear = (String) getYears.get(1);
        String str = "";
        if (key.equalsIgnoreCase("AS_OF_YEAR") || key.equalsIgnoreCase("AS_OF_YEAR1")) {
            str = "select distinct cyear as view_by,cyear as view_by1 from prg_acn_mon_denom "
                    + "where cmy_start_date between (select distinct cmy_start_date from prg_acn_mon_denom where cyear='" + targetStartYear + "') and "
                    + "(select distinct cmy_start_date from prg_acn_mon_denom where cyear='" + targetEndYear + "') order by cyear";
        }
        return str;
    }

    public String displayTimeParams(LinkedHashMap hm, ArrayList timeDetailsArray, HashMap targetIncomingParameters) throws SQLException, Exception {
        // String result1 = "<Table width=\"80%\">";
        collect.targetIncomingParameters = targetIncomingParameters;
        String result1 = "";
        String periodType = timeDetailsArray.get(4).toString();
        ArrayList newTimeArray = timeDetailsArray;
        LinkedHashMap newTimeMap = hm;
        collect.targetId = tarId;

        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);

        this.totalParam += a1.length;
        int i = 0;

        for (i = 0; i < a1.length; i++) {

            if (j == 0) {
                result1 += "<Tr>";
            } else if (j % 4 == 0) {
                result1 += "</Tr><Tr>";
            }
            /*
             * if ((i + 1) % 4 == 1) { result1 += "<Tr>";
             }
             */
            ArrayList a = (ArrayList) hm.get(a1[i]);
            if (a1[i].equalsIgnoreCase("AS_OF_DATE")) {
                result1 += " <Td id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker") + " </Td> ";

            } else if (a1[i].equalsIgnoreCase("AS_OF_DATE1")) {
                result1 += " <Td id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker1") + " </Td> ";

            } else if (a1[i].equalsIgnoreCase("AS_OF_MONTH") || a1[i].equalsIgnoreCase("AS_OF_MONTH1")) {
                result1 += " <td align=\"right\"> " + f1.getQueryCombotb2(a.get(1).toString(), (String) a.get(2), getMonthsQry(a1[i]), (String) a.get(0)) + " </td> ";
            } else if (a1[i].equalsIgnoreCase("AS_OF_QTR") || a1[i].equalsIgnoreCase("AS_OF_QTR1")) {
                result1 += " <td align=\"right\"> " + f1.getQueryCombotb2(a.get(1).toString(), (String) a.get(2), getQtrsQry(a1[i]), (String) a.get(0)) + " </td> ";
            } else if (a1[i].equalsIgnoreCase("AS_OF_YEAR") || a1[i].equalsIgnoreCase("AS_OF_YEAR1")) {
                result1 += " <td align=\"right\"> " + f1.getQueryCombotb2(a.get(1).toString(), (String) a.get(2), getYearsQry(a1[i]), (String) a.get(0)) + " </td> ";
            } else if (a1[i].equalsIgnoreCase("PRG_PERIOD")) {
                result1 += " <Td id=column" + i + " align=\"right\"> " + f1.getQueryCombotb2(a.get(1).toString(), (String) a.get(2), getPeriodTypeQuery(a1[i], (String) timeDetailsArray.get(0), tarId), (String) a.get(0)) + " </Td> ";
            }

            j++;
        }

        result1 += "";
        return result1;
    }

    public String displayParam(ArrayList elementList) {
//        String result1 = "<Table>";
        StringBuilder result1 = new StringBuilder();
        result1.append("<Table>");
//        String sqlstr = "";
        int i;
        for (i = 0; i < elementList.size(); i++) {
            //String sqlstr = this.getParameterQuery((String)elementList.get(i));
            if ((i + 1) % 4 == 1) {
//                result1 += "<Tr>";
                result1.append("<Tr>");
            }
            getParameterInfo(elementList.get(i).toString());
            //         result1 += "<Td>" + f1.getMultiTextBoxNew("CBOAR" + elementList.get(i).toString(), this.tempPass, "All", elementList.get(i).toString(),(String)//a.get(10)) + "</Td>";
            //result += " <Td id=column" + i + " align=\"right\"> " + f1.getQueryCombotb("CBOAR" + elementList.get(i).toString(), this.tempPass, sqlstr, elementList.get(i).toString()) + " </Td> ";

            if ((i + 1) % 4 == 0) {
//                result1 += "</Tr>";
                result1.append("</Tr>");
            }
        }
        if ((i) % 4 != 0) {
            result1.append("</Tr>");
        }
        result1.append("</Table>");
        return result1.toString();
    }

    public String displayViewBys(HashMap hm, HashMap ParameterMap) {
        // String result1 = "<Table width=\"80%\">";
        collect.targetId = tarId;
//        String result1 = "";
        StringBuilder result1 = new StringBuilder();
        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);
        this.totalParam += a1.length;
        String eleIdViewBy1 = "";
        String eleIdViewBy2 = "";
        int i = 0;
        for (i = 0; i < a1.length; i++) {

            if (j % 4 == 0) {
//                result1 += "</Tr><Tr>";
                result1.append("</Tr><Tr>");
            }
            ArrayList a = (ArrayList) hm.get(a1[i]);

            if (((String) a.get(0)).equalsIgnoreCase("CBOVIEW_BY_NAME")) {
//                result1 += " <Td id=column" + i + " align=\"right\"> " + f1.getQueryCombotb4(a.get(1).toString(), (String) a.get(2), collect.setTargetViewByQuery((String) a.get(0), hm, (String) a.get(3)), (String) a.get(3)) + " </Td> ";
                result1.append(" <Td id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb4(a.get(1).toString(), (String) a.get(2), collect.setTargetViewByQuery((String) a.get(0), hm, (String) a.get(3)), (String) a.get(3))).append(" </Td> ");
                eleIdViewBy1 = a.get(3).toString();
            }
            if (((String) a.get(0)).equalsIgnoreCase("CBOVIEW_BY_NAME1")) {
                eleIdViewBy2 = a.get(3).toString();
//                result1 += " <Td id=column" + i + " align=\"right\"> " + f1.getQueryCombotb3(a.get(1).toString(), (String) a.get(2), collect.setTargetViewByQuery((String) a.get(0), hm, (String) a.get(3)), (String) a.get(3)) + " </Td> ";
                result1.append(" <Td id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb3(a.get(1).toString(), (String) a.get(2), collect.setTargetViewByQuery((String) a.get(0), hm, (String) a.get(3)), (String) a.get(3))).append(" </Td> ");
                // result1 += "<Td><input name=\"Submit\" type=\"Button\"  value=\"    Go    \"  onclick=\"submitform()\"></Td>";
            }
            if (((String) a.get(0)).equalsIgnoreCase("CBOVIEW_BY_NAME2")) {
                eleIdViewBy2 = a.get(3).toString();
//                result1 += " <Td id=column" + i + " align=\"right\"> " + f1.getQueryCombotbBasis(a.get(1).toString(), (String) a.get(2), collect.setTargetViewByQuery((String) a.get(0), hm, (String) a.get(3)), (String) a.get(3)) + " </Td> ";
                result1.append(" <Td id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotbBasis(a.get(1).toString(), (String) a.get(2), collect.setTargetViewByQuery((String) a.get(0), hm, (String) a.get(3)), (String) a.get(3))).append(" </Td> ");
//                result1 += "<Td><input name=\"Submit\" type=\"Button\"  class=\"navtitle-hover\"    value=\"    Go    \"  onclick=\"submitform()\"></Td>";
                result1.append("<Td><input name=\"Submit\" type=\"Button\"  class=\"navtitle-hover\"    value=\"    Go    \"  onclick=\"submitform()\"></Td>");
            }

            j++;
        }
        if (j % 4 != 0) {
//            result1 += "</Tr>";
            result1.append("</Tr>");
        }
//       result1 += "";
        result1.append("");
        return result1.toString();

    }

    public String displayViewBysForView(HashMap hm, HashMap ParameterMap) {
        // String result1 = "<Table width=\"80%\">";
        collect.targetId = tarId;
//        String result1 = "";
        StringBuilder result1 = new StringBuilder();
        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);
        this.totalParam += a1.length;
        String eleIdViewBy1 = "";
        String eleIdViewBy2 = "";
        int i = 0;
        for (i = 0; i < a1.length; i++) {

            if (j % 4 == 0) {
//                result1 += "</Tr><Tr>";
                result1.append("</Tr><Tr>");
            }
            ArrayList a = (ArrayList) hm.get(a1[i]);

            if (((String) a.get(0)).equalsIgnoreCase("CBOVIEW_BY_NAME")) {
//                result1 += " <Td id=column" + i + " align=\"right\"> " + f1.getQueryCombotb4(a.get(1).toString(), (String) a.get(2), collect.setTargetViewByQuery((String) a.get(0), hm, (String) a.get(3)), (String) a.get(3)) + " </Td> ";
                result1.append(" <Td id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb4(a.get(1).toString(), (String) a.get(2), collect.setTargetViewByQuery((String) a.get(0), hm, (String) a.get(3)), (String) a.get(3))).append(" </Td> ");
                eleIdViewBy1 = a.get(3).toString();
            }
            if (((String) a.get(0)).equalsIgnoreCase("CBOVIEW_BY_NAME1")) {
                eleIdViewBy2 = a.get(3).toString();
//                result1 += " <Td id=column" + i + " align=\"right\"> " + f1.getQueryCombotb3(a.get(1).toString(), (String) a.get(2), collect.setTargetViewByQuery((String) a.get(0), hm, (String) a.get(3)), (String) a.get(3)) + " </Td> ";
//                result1 += "<Td><input name=\"Submit\" type=\"Button\"  value=\"    Go    \"  onclick=\"submitformView()\"></Td>";
                result1.append(" <Td id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb3(a.get(1).toString(), (String) a.get(2), collect.setTargetViewByQuery((String) a.get(0), hm, (String) a.get(3)), (String) a.get(3))).append(" </Td> ");
                result1.append("<Td><input name=\"Submit\" type=\"Button\"  value=\"    Go    \"  onclick=\"submitformView()\"></Td>");
            }

            j++;
        }
        if (j % 4 != 0) {
//            result1 += "</Tr>";
            result1.append("</tr>");
        }
//        result1 += "";
        return result1.toString();

    }

    public String getElementName(String elementId, HashMap reportParameters) {
        ArrayList paraInfo = new ArrayList();
        String NextElementId = null;
        paraInfo = (ArrayList) reportParameters.get(elementId);
        if (paraInfo != null) {
            if (paraInfo.get(1) != null) {
                NextElementId = paraInfo.get(1).toString();
            }
        }
        return (NextElementId);
    }

    public String RowEdgeQuery(HashMap hm) {
        String sqlstr = "";
        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);
        //ArrayList rowEdgeValues = new ArrayList();

        for (int i = 0; i < a1.length; i++) {
            ArrayList a = (ArrayList) hm.get(a1[i]);
            if (a.get(0).toString().equalsIgnoreCase("CBOVIEW_BY_NAME")) {
                sqlstr = getParameterQuery((String) a.get(3));
            }

        }
        return sqlstr;
    }

    public String ColumnEdgeQuery(HashMap hm, ArrayList timeDetails) {
        String sqlstr = "";
        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);
        for (int i = 0; i < a1.length; i++) {
            ArrayList a = (ArrayList) hm.get(a1[i]);
            if (a.get(0).toString().equalsIgnoreCase("CBOVIEW_BY_NAME1")) {
                if (a.get(4).toString().equalsIgnoreCase("Time")) {
                    sqlstr = getTimeQuery((String) timeDetails.get(0), (String) timeDetails.get(2), (String) timeDetails.get(3), (String) timeDetails.get(4));
                } else {
                    sqlstr = getParameterQuery((String) a.get(3));
                }
            }
        }
        return sqlstr;
    }

    public String getTimeQuery(String minTimeLEvel, String from, String to, String periodType) {
        String sqlstr = "";
        if (minTimeLEvel.equalsIgnoreCase("Day")) {
            if (periodType.equalsIgnoreCase("Day")) {
                sqlstr = "select view_by , view_by as view_by1 from ( select distinct to_char(T.ST_DATE,'dd-mm-yy') as view_by , "
                        + "trunc(T.ST_DATE) as orderbycol from pr_day_denom T where ddate between to_date('" + from + "','mm/dd/yyyy') "
                        + "and to_date('" + to + "','mm/dd/yyyy') ) order by orderbycol ";
            } /*
             * else if (periodType.equalsIgnoreCase("WEEK")) { sqlstr = "select
             * view_by , view_by as view_by1 from ( select distinct T.CWEEK ||
             * '-' || T.CWYEAR as view_by ," + "T.CWYEAR || '-' || T.CWEEK as
             * orderbycol from pr_day_denom T where ddate between
             * to_date('"+from+"','mm/dd/yyyy') " + "and
             * to_date('"+to+"','mm/dd/yyyy') ) order by orderbycol "; }
             */ else if (periodType.equalsIgnoreCase("MONTH")) {
                sqlstr = "select view_by , view_by as view_by1 from ( select distinct TO_CHAR(ST_DATE,'MON-RR') as view_by , "
                        + "TO_CHAR(ST_DATE,'YYYY-MM') as orderbycol from pr_day_denom T where ddate between to_date('" + from + "','mm/dd/yyyy') "
                        + "and to_date('" + to + "','mm/dd/yyyy') ) order by orderbycol ";
            } else if (periodType.equalsIgnoreCase("QTR") || periodType.equalsIgnoreCase("QUARTER")) {
                sqlstr = "select view_by , view_by as view_by1 from ( select distinct T.CQTR || '-' || T.CQ_YEAR as view_by ,"
                        + "T.CQ_YEAR || '-' || T.CQTR as orderbycol from pr_day_denom T where ddate between to_date('" + from + "','mm/dd/yyyy') "
                        + "and to_date('" + to + "','mm/dd/yyyy') ) order by orderbycol ";
            } else {
                sqlstr = "select view_by , view_by as view_by1 from ( select distinct T.CYEAR as view_by ,T.CYEAR as orderbycol "
                        + "from pr_day_denom T where ddate between to_date('" + from + "','mm/dd/yyyy') "
                        + "and to_date('" + to + "','mm/dd/yyyy') ) order by orderbycol ";
            }

        } else if (minTimeLEvel.equalsIgnoreCase("Month")) {
            if (periodType.equalsIgnoreCase("MONTH")) {
                sqlstr = "select distinct mon_name,cmon, cyear from prg_acn_mon_denom where cm_st_date between "
                        + "(select distinct cm_st_date from prg_acn_mon_denom where mon_name='" + from + "') and "
                        + "(select distinct cm_end_date from prg_acn_mon_denom where mon_name='" + to + "') order by 3,2";
            } else if (periodType.equalsIgnoreCase("QTR") || periodType.equalsIgnoreCase("QUARTER")) {
                sqlstr = "select view_by,view_by as view_by1 from ( select distinct T.CMQTR || '-' || T.CYEAR as view_by , T.CYEAR || '-' || T.CMQTR as "
                        + "orderbycol FROM prg_acn_mon_denom T where cm_st_date between "
                        + "(select DISTINCT cm_st_date from prg_acn_mon_denom where mon_name='" + from + "') and "
                        + "(select distinct cm_end_date from prg_acn_mon_denom where mon_name='" + to + "')) order by orderbycol";
            } else {
                sqlstr = "select distinct cyear from prg_acn_mon_denom where cm_st_date between "
                        + "(select DISTINCT cm_st_date from prg_acn_mon_denom where mon_name='" + from + "') and "
                        + "(select distinct cm_end_date from prg_acn_mon_denom where mon_name='" + to + "') order by cyear";
            }
        } else if (minTimeLEvel.equalsIgnoreCase("Quarter") || minTimeLEvel.equalsIgnoreCase("QTR")) {
            if (periodType.equalsIgnoreCase("QTR") || periodType.equalsIgnoreCase("QUARTER")) {
                sqlstr = "select view_by,view_by as view_by1 from "
                        + "( select distinct T.CQTR || '-' || T.CQ_YEAR as view_by , T.CQ_YEAR || '-' || T.CQTR as orderbycol FROM "
                        + " pr_day_denom T where ddate between (select DISTINCT cmq_st_date from prg_acn_mon_denom where cmqtr_code='" + from + "') "
                        + "and (select distinct cmq_end_date from prg_acn_mon_denom where cmqtr_code='" + to + "')) order by orderbycol";

            } else {
                sqlstr = "select distinct cyear from prg_acn_mon_denom where cmq_st_date between "
                        + "(select DISTINCT cmq_st_date from prg_acn_mon_denom where cmqtr_code='" + from + "') "
                        + "and (select distinct cmq_end_date from prg_acn_mon_denom where cmqtr_code='" + to + "') order by cyear";
            }
        } else if (minTimeLEvel.equalsIgnoreCase("Year")) {
            sqlstr = "select distinct cyear from prg_acn_mon_denom where cmy_start_date between "
                    + "(select distinct cmy_start_date from prg_acn_mon_denom where cyear='" + from + "') "
                    + "and (select distinct cmy_end_date from prg_acn_mon_denom where cyear='" + to + "') order by cyear";
        }
        return sqlstr;
    }

    public HashMap displayDataRegion(HashMap viewBy, ArrayList timeDetails, HashMap targetParametersValues, String drllUrl, String colDrillUrl, HashMap targetParameters, ArrayList basisValues) throws Exception {
        String freezeButton = "";
        String parentDataQ = "";
        String parentDataQ2 = "";
        String dataQuery2 = "";

        HashMap RestrictingTotal = new HashMap();
        String basisVal = basisValues.get(0).toString();
        HashMap paramValues = collect.targetParametersValues;
        Connection con = ProgenConnection.getInstance().getConnection();
        PreparedStatement ps, psP, psO;
        ResultSet rs, rsP, rsO;
        rs = null;
        ps = null;
        psP = null;
        rsP = null;
        ps = null;
        psO = null;
        rsO = null;
        String startRange = "";
        String endRange = "";
        String minTimeLevel = "";
        startRange = timeDetails.get(2).toString();
        endRange = timeDetails.get(3).toString();
        minTimeLevel = timeDetails.get(0).toString();
        String endPeriod = "";
        String startPeriod = "";
//        String result = "";
        StringBuilder result = new StringBuilder();
        HashMap totalResult = new HashMap();
        HashMap originalResult = new HashMap();
        String rowQry = "";
        String colQry = "";
        String secAnalyze = "";
        String primaryAnalyze = "";
        String primParamEleId = "";
        String dateMeassage = "";
        boolean disableSaveButtonFlag = false;
        boolean showRestrictingTotal = true;
        ArrayList viewByParentsList = new ArrayList();
        boolean periodFlag = false;
        boolean displayPeriodFlagMessage = false;
        String targetStartDate = "";
        String targetEndDate = "";

        boolean showRowTotal = true;
        boolean showColTotal = true;
        boolean overAllFlag = false;
        String targetId = tarId;

        String timeLevelQ = "select level_id,level_name from targettimelevels where target_id=" + targetId + " order by level_id";
        PbReturnObject timeObj = pbDb.execSelectSQL(timeLevelQ);
        String maxTimeLevel = timeObj.getFieldValueString(0, "LEVEL_NAME");
        rowQry = RowEdgeQuery(viewBy);
        colQry = ColumnEdgeQuery(viewBy, timeDetails);

        String targetTimeQ = resundle.getString("targetTimeQ");
        Object tObj[] = new Object[1];
        tObj[0] = targetId;
        String fintargetTimeQ = pbDb.buildQuery(targetTimeQ, tObj);
        PbReturnObject TargetTimeOb = pbDb.execSelectSQL(fintargetTimeQ);
        if (minTimeLevel.equalsIgnoreCase("Day")) {
            targetStartDate = TargetTimeOb.getFieldValueString(0, "START_DATE");
            targetEndDate = TargetTimeOb.getFieldValueString(0, "END_DATE");
        } else if (minTimeLevel.equalsIgnoreCase("Month")) {
            targetStartDate = TargetTimeOb.getFieldValueString(0, "TARGET_START_MONTH");
            targetEndDate = TargetTimeOb.getFieldValueString(0, "TARGET_END_MONTH");
        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
            targetStartDate = TargetTimeOb.getFieldValueString(0, "TARGET_START_QTR");
            targetEndDate = TargetTimeOb.getFieldValueString(0, "TARGET_END_QTR");
        } else if (minTimeLevel.equalsIgnoreCase("Year")) {
            targetStartDate = TargetTimeOb.getFieldValueString(0, "TARGET_START_YEAR");
            targetEndDate = TargetTimeOb.getFieldValueString(0, "TARGET_END_YEAR");
        }

        // to get the business column names and display names for the element ids
        String getBussColNames = " SELECT DISTINCT element_id,buss_col_name FROM prg_user_all_info_details"
                + " where element_id in(select  element_id from prg_target_param_details where target_id=&)";
        // resundle.getString("getBussColNames");
        String getDispNames = resundle.getString("getDispNames");

        Object tarOb[] = new Object[1];
        tarOb[0] = targetId;
        String fingetBussColNames = pbDb.buildQuery(getBussColNames, tarOb);
        PbReturnObject pbro = pbDb.execSelectSQL(fingetBussColNames);
        HashMap elementCols = new HashMap();
        HashMap viewByNames = new HashMap();

        String fingetDispNames = pbDb.buildQuery(getDispNames, tarOb);
        //.println(" fingetDispNames . " + fingetDispNames);
        PbReturnObject pbroDispNames = pbDb.execSelectSQL(fingetDispNames);

        // store the business column names for element ids
        for (int i = 0; i < pbro.getRowCount(); i++) {
            elementCols.put(pbro.getFieldValueString(i, "ELEMENT_ID"), pbro.getFieldValueString(i, "BUSS_COL_NAME"));
            // viewByNames.put(pbro.getFieldValueString(i,"ELEMENT_ID"),pbro.getFieldValueString(i,"PARAM_DISP_NAME"));
        }
        for (int m = 0; m < pbroDispNames.getRowCount(); m++) {
            viewByNames.put(pbroDispNames.getFieldValueString(m, "ELEMENT_ID"), pbroDispNames.getFieldValueString(m, "PARAM_DISP_NAME"));
        }
        // to get the target table and measure name
        String getTargetTable = resundle.getString("getTargetTable");
        String fingetTargetTable = pbDb.buildQuery(getTargetTable, tarOb);
        // ////////////////////////////////////////////////////////.println(" fingetTargetTable "+fingetTargetTable);
        PbReturnObject pbroTab = pbDb.execSelectSQL(fingetTargetTable);
        String measureName2 = pbroTab.getFieldValueString(0, "MEASURE_NAME");
        String measureName = measureName2.trim().replace(" ", "_");
        String targetTable = pbroTab.getFieldValueString(0, "BUSS_TABLE_NAME");

        String[] a1 = (String[]) (viewBy.keySet()).toArray(new String[0]);
        //String columnEdgeValues = "";
        String rowViewByElement = "";
        String viewByElement = "";
        String colViewByElement = "";
        String colCurrVal = "";
        String periodType = "";
        String minimumTimeLevel = "";
        String currVal = "";
        String dataQuery = "";
        String overAllMessage = "";
        String errorMessage = "";
        String childperiodType = "";

        // set the primary anakyse by and secondary analyse by element ids and display names
        for (int i = 0; i < a1.length; i++) {
            ArrayList a = (ArrayList) viewBy.get(a1[i]);
            if (a.get(0).toString().equalsIgnoreCase("CBOVIEW_BY_NAME")) {
                viewByElement = a.get(3).toString();
                ////.println(" a.... "+a);
                if (!a.get(3).toString().equalsIgnoreCase("0")) {
                    currVal = a.get(4).toString();
                } else {
                    currVal = "Overall Target";
                }
            }

            if (a.get(0).toString().equalsIgnoreCase("CBOVIEW_BY_NAME1")) {
                if (a.get(4).toString().equalsIgnoreCase("Time")) {
                    periodType = (String) timeDetails.get(4);
                    minimumTimeLevel = (String) timeDetails.get(0);
                    colViewByElement = a.get(3).toString();
                    colCurrVal = a.get(4).toString();
                } else {
                    periodType = (String) timeDetails.get(4);
                    minimumTimeLevel = (String) timeDetails.get(0);
                    colViewByElement = a.get(3).toString();
                    colCurrVal = a.get(4).toString();
                }
            }
            if (a.get(0).toString().equalsIgnoreCase("CBOVIEW_BY_NAME2")) {
                basisVal = a.get(3).toString();
            }
            //basisVal
        }
        rowViewByElement = viewByElement;
        // flag to show message if no data is available at minimum time level
        if (maxTimeLevel.equalsIgnoreCase(periodType)) {
            showRowTotal = false;
            showColTotal = false;
            overAllFlag = true;
        }

        if (!periodType.equalsIgnoreCase(minimumTimeLevel)) {
            periodFlag = true;
        }
        HashMap AllV = new HashMap();
        HashMap allVa = new HashMap();
        HashMap primH2 = new HashMap();
        String oldPrimV = "";
        String newPrimV = "";
        ArrayList paramVals = new ArrayList();

        ArrayList colEdgeVals = new ArrayList();
        primaryAnalyze = viewByElement.trim();
        f1.elementId = viewByElement.trim();
        if (!currVal.equalsIgnoreCase("Overall Target")) {
            paramVals = f1.getRowEdges(rowQry, currVal);
        }
        HashMap allV = new HashMap();
        String oldPv = "";
        String newPv = "";
        HashMap primH = new HashMap();
        //self row clause
        String rowCurrVal = "";
        HashMap allParamHashMap = targetParametersValues;
//        String allParametrsFilterClause = "";
        StringBuilder allParametrsFilterClause = new StringBuilder();
        String[] keys = (String[]) (allParamHashMap.keySet()).toArray(new String[0]);
        String a = "";
        // paramVals = new ArrayList();
        // on the basis of request parameters get the row header values
        for (int i = 0; i < keys.length; i++) {
            a = (String) allParamHashMap.get(keys[i]);

            if (rowViewByElement.equalsIgnoreCase(keys[i]) && a.equalsIgnoreCase("All")) {
                String primQuery = getParameterRowQuery(targetParameters, targetId, viewByElement);
                if (!currVal.equalsIgnoreCase("Overall Target")) {
                    paramVals = f1.getRowEdges(primQuery, currVal);
                }
            } else if (rowViewByElement.equalsIgnoreCase(keys[i]) && !(a.equalsIgnoreCase("All"))) {
                paramVals = new ArrayList();
                if (!paramVals.contains(currVal)) {
                    paramVals.add(currVal);
                    paramVals.add(a);
                }
            }
            if (!a.equalsIgnoreCase("All")) {
//                allParametrsFilterClause = allParametrsFilterClause + " and " + elementCols.get(keys[i]) + "='" + a + "'";
                allParametrsFilterClause.append(allParametrsFilterClause).append(" and ").append(elementCols.get(keys[i])).append("='").append(a).append("'");
            }
        }

        PbReturnObject secViewBy = new PbReturnObject();
        boolean flagToDisplayCrossTab = false;

        // on the basis of request parameters get the column header values
        for (int i = 0; i < a1.length; i++) {
            ArrayList arr = (ArrayList) viewBy.get(a1[i]);
            if (arr.get(0).toString().equalsIgnoreCase("CBOVIEW_BY_NAME1")) {
                if (arr.get(4).toString().equalsIgnoreCase("Time")) {
                    secViewBy = pbDb.execSelectSQL(colQry);
                    colEdgeVals = getArrayListFromPbReturnObject(secViewBy);

                } else {
                    secViewBy = f1.getColumnEdges(colQry);
                    secViewBy = getParameterColQuery(targetParameters, targetId, colViewByElement);
                    colEdgeVals = getArrayListFromPbReturnObject(secViewBy);//secViewBy);
                    colEdgeVals = new ArrayList();
                    //self column clause
                    for (int g = 0; g < keys.length; g++) {
                        a = (String) allParamHashMap.get(keys[g]);
                        if (colViewByElement.equalsIgnoreCase(keys[g]) && a.equalsIgnoreCase("All")) {
                            colEdgeVals = getArrayListFromPbReturnObject(secViewBy);
                        } else if (colViewByElement.equalsIgnoreCase(keys[g]) && !(a.equalsIgnoreCase("All"))) {
                            colEdgeVals.add(a);
                        }
                    }
                    flagToDisplayCrossTab = true;
                }
            }
        }

        boolean flagForRestrictingTotal = false;
        // when column header is time
        String overallT = "";
        if (elementCols.containsKey(viewByElement.trim())) {
            overallT = elementCols.get(viewByElement.trim()).toString();
        }
        String viewName = "";
        if (viewByNames.containsKey(viewByElement.trim())) {
            viewName = viewByNames.get(viewByElement.trim()).toString();
        }

        if (flagToDisplayCrossTab == false) {
            if (!viewByElement.equalsIgnoreCase("0")) {
                if (periodType.equalsIgnoreCase("Day")) {
                    dataQuery = "select " + overallT + ",sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewName + "' " + allParametrsFilterClause + " and secviewby is null group by " + overallT + ",to_char(t_date,'dd-mm-yy') order by " + overallT + ",to_char(t_date,'dd-mm-yy')";
                } else if (periodType.equalsIgnoreCase("Month")) {
                    dataQuery = "select " + overallT + ",sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewName + "' " + allParametrsFilterClause + " and secviewby is null group by " + overallT + ",t_month order by " + overallT + ",t_month";
                } //dataQuery ="select "+elementCols.get(viewByElement.trim()).toString()+",sum("+measureName.trim()+"),t_month from "+targetTable.trim()+" where target_id='"+targetId+"' and viewby='"+viewByNames.get(viewByElement.trim()).toString()+"'"+allParametrsFilterClause+" group by "+elementCols.get(viewByElement.trim()).toString()+",t_month order by "+elementCols.get(viewByElement.trim()).toString()+",t_month";
                else if (periodType.equalsIgnoreCase("Quarter") || periodType.equalsIgnoreCase("Qtr")) {
                    dataQuery = "select " + overallT + ",sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id='" + targetId + "' " + allParametrsFilterClause + " "
                            + " and viewby='" + viewName + "' and secviewby is null group by " + overallT + ", t_qtr order by " + overallT + ",t_qtr";
                } else if (periodType.equalsIgnoreCase("Year")) {
                    dataQuery = "select " + overallT + ",sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewName + "' " + allParametrsFilterClause + " and secviewby is null group by " + overallT + ",t_year order by " + overallT + ",t_year";
                }

            } else {
                if (viewByElement.equalsIgnoreCase("0")) {
                    paramVals = new ArrayList();
                    paramVals.add("OverAll Target");
                    paramVals.add("OverAll");
                }

                if (periodType.equalsIgnoreCase("Day")) {
                    dataQuery = "select sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='OverAll'  group by to_char(t_date,'dd-mm-yy') order by to_char(t_date,'dd-mm-yy')";
                } else if (periodType.equalsIgnoreCase("Month")) {
                    dataQuery = "select sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='OverAll'  group by t_month order by t_month";
                } else if (periodType.equalsIgnoreCase("Quarter") || periodType.equalsIgnoreCase("QTR")) {
                    dataQuery = "select sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='OverAll' group by t_qtr order by t_qtr";
                } else if (periodType.equalsIgnoreCase("Year")) {
                    dataQuery = "select sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='OverAll' group by t_year order by t_year";
                }

            }
            //.println(" dataQuery..---.. " + dataQuery);
            // PbReturnObject allD = pbDb.execSelectSQL(dataQuery);

            con = ProgenConnection.getInstance().getConnection();
            // if(elementCols.containsKey(viewByElement.trim()))
            // {
            ps = con.prepareStatement(dataQuery);
            rs = ps.executeQuery();
            //  }
            PbReturnObject allD = new PbReturnObject();
            if (rs != null) {
                allD = new PbReturnObject(rs);
            }

            // data obtained from the target table
            if (!viewByElement.equalsIgnoreCase("0")) {
                for (int n = 0; n < allD.getRowCount(); n++) {
                    oldPv = allD.getFieldValueString(n, 0);
                    if (newPv.equalsIgnoreCase("")) {
                        newPv = oldPv;
                    } else if (!newPv.trim().equalsIgnoreCase(oldPv.trim()) && newPv.length() > 0) {
                        allV.put(newPv, primH);
                        primH = new HashMap();
                        newPv = oldPv;
                    }
                    String dat = allD.getFieldValueString(n, 2);
                    long mValue = Long.parseLong(allD.getFieldValueString(n, 1));
                    primH.put(dat, new Long(mValue));
                    if (n == (allD.getRowCount() - 1)) {
                        allV.put(oldPv, primH);
                    }
                }
            } else {
                oldPv = "OverAll";
                primH = new HashMap();
                for (int n = 0; n < allD.getRowCount(); n++) {
                    if (newPv.equalsIgnoreCase("")) {
                        newPv = oldPv;
                    }

                    /*
                     * else
                     * if(!newPv.trim().equalsIgnoreCase(oldPv.trim())&&newPv.length()>0)
                     * { allV.put(newPv,primH); primH = new HashMap(); newPv =
                     * oldPv; }
                     */
                    String dat = allD.getFieldValueString(n, 1);
                    long mValue = Long.parseLong(allD.getFieldValueString(n, 0));
                    primH.put(dat, new Long(mValue));
                    allV.put(oldPv, primH);

                }
            }
            PbReturnObject time = pbDb.execSelectSQL(colQry);
            colEdgeVals = new ArrayList();

            // when column header is time
            for (int y = 0; y < time.getRowCount(); y++) {
                colEdgeVals.add(time.getFieldValueString(y, 0));
                if (y == 0) {
                    startPeriod = time.getFieldValueString(y, 0);
                }
                if (y == time.getRowCount() - 1) {
                    endPeriod = time.getFieldValueString(y, 0);
                }
            }

            // to get originalResult which is there in the database
            for (int x = 1; x < paramVals.size(); x++) {
                String pVal = paramVals.get(x).toString();
                HashMap dt = new HashMap();
                if (allV.containsKey(pVal)) {
                    dt = (HashMap) allV.get(pVal);
                }
                for (int y = 0; y < time.getRowCount(); y++) {
                    String timeV = time.getFieldValueString(y, 0);
                    long mV = 0;
                    if (dt.containsKey(timeV)) {
                        mV = ((Long) dt.get(timeV)).longValue();
                    }
                    String value = "";
                    if (mV != 0) {
//                        value = "" + mV + "";
                        value = String.valueOf(mV);
                    }
                    originalResult.put(paramVals.get(x) + ":" + timeV, new Long(mV));
                }
            }

            // to get the column and row total hashMaps
            HashMap rowTotal = new HashMap();
            HashMap colTotal = new HashMap();
            String[] originalValsKeys = (String[]) (originalResult.keySet()).toArray(new String[0]);
            for (int m = 0; m < originalValsKeys.length; m++) {
                String totalKey = originalValsKeys[m];
                String rowColKey = "";
                if (originalResult.containsKey(totalKey)) {
                    rowColKey = totalKey;//(String)originalResult.get(totalKey);
                    long oriVal = 0;
                    oriVal = ((Long) originalResult.get(totalKey)).longValue();
                    String totKey[] = rowColKey.split(":");
                    String rowKey = "";
                    String colKey = "";
                    for (int n = 0; n < totKey.length; n++) {
                        if (n == 0) {
                            rowKey = totKey[n];
                        }
                        if (n == 1) {
                            colKey = totKey[n];
                        }
                    }
                    if (rowTotal.containsKey(rowKey)) {
                        long al = ((Long) rowTotal.get(rowKey)).longValue();
                        al = oriVal + al;
                        rowTotal.put(rowKey, new Long(al));
                    } else {
                        rowTotal.put(rowKey, new Long(oriVal));
                    }

                    if (colTotal.containsKey(colKey)) {
                        long colAl = ((Long) colTotal.get(colKey)).longValue();
                        colAl = oriVal + colAl;
                        colTotal.put(colKey, new Long(colAl));
                    } else {
                        colTotal.put(colKey, new Long(oriVal));
                    }

                }
            }

            String childElement = collect.getChildElementId(viewByElement, targetId);

            //column Header display
            String pType = "";
            pType = periodType;
            //if(pType.e)

            childperiodType = collect.getChildTimeId(pType, targetId);
            if (childperiodType != null) {
                if (childperiodType.equalsIgnoreCase("")) {
                    childperiodType = null;
                }
            }
//            result = result + "<Tr><Td style=\"width:auto;font-weight:bold;font-size:11px;background-color:#E6E6E6\">" + currVal + "</Td>";
            result.append("<Tr><Td style=\"width:auto;font-weight:bold;font-size:11px;background-color:#E6E6E6\">").append(currVal).append("</Td>");
            String newColDrillUrl = colDrillUrl;
            for (int j = 0; j < time.getRowCount(); j++) {

                newColDrillUrl = colDrillUrl.replace("~", time.getFieldValueString(j, 0));
                if (viewByElement.equalsIgnoreCase("0")) {
                    if (overAllFlag == false) {
                        if (childperiodType != null) {
//                            result = result + "<Td align=\"center\" colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\"><a href=\"javascript:submitTimeDrill('" + newColDrillUrl + "')\">" + time.getFieldValueString(j, 0) + "</Td>";
                            result.append("<Td align=\"center\" colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\"><a href=\"javascript:submitTimeDrill('").append(newColDrillUrl).append("')\">").append(time.getFieldValueString(j, 0)).append("</Td>");
                        } else {
//                            result = result + "<Td align=\"center\" colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">" + time.getFieldValueString(j, 0) + "</Td>";
                            result.append("<Td align=\"center\" colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">").append(time.getFieldValueString(j, 0)).append("</Td>");
                        }
                    } else if (overAllFlag == true) {
                        if (childperiodType != null) {
//                            result = result + "<Td align=\"center\"  style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\"><a href=\"javascript:submitTimeDrill('" + newColDrillUrl + "')\">" + time.getFieldValueString(j, 0) + "</Td>";
                            result.append("<Td align=\"center\"  style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\"><a href=\"javascript:submitTimeDrill('").append(newColDrillUrl).append("')\">").append(time.getFieldValueString(j, 0)).append("</Td>");
                        } else {
//                            result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">" + time.getFieldValueString(j, 0) + "</Td>";
                            result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">").append(time.getFieldValueString(j, 0)).append("</Td>");
                        }

                    }
                } else {
//                    result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">" + time.getFieldValueString(j, 0) + "</Td>";
                    result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">").append(time.getFieldValueString(j, 0)).append("</Td>");
                }
            }

            if (viewByElement.equalsIgnoreCase("0") && overAllFlag == false) {
//                result = result + "<Td align=\"center\" colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Row Total</Td>";
                result.append("<Td align=\"center\" colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Row Total</Td>");
            } else {
//                result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Row Total</Td>";
                result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Row Total</Td>");
            }

            if (viewByElement.equalsIgnoreCase("0") && overAllFlag == false) {
//                result = result + "<Td align=\"center\"  colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Restricting Total</Td>";
//                result = result + "<Td align=\"center\"  colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Difference</Td>";
                result.append("<Td align=\"center\"  colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Restricting Total</Td>");
                result.append("<Td align=\"center\"  colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Difference</Td>");

            }
            result.append("</tr>");
//            result = result + "</Tr>";

            /// to show percent and absolute in the header
            if (viewByElement.equalsIgnoreCase("0")) {
                String basisHeader1 = "";
                String basisHeader2 = "";
                if (basisVal.equalsIgnoreCase("absolute")) {
                    basisHeader1 = "Absolute";
                    basisHeader2 = "Percentage";
                } else if (basisVal.equalsIgnoreCase("Percentage")) {
                    basisHeader1 = "Percentage";
                    basisHeader2 = "absolute";
                }
                if (overAllFlag == false) {
//                    result = result + "<Tr><Td></Td>";
                    result.append("<Tr><Td></Td>");
                    for (int j = 0; j < time.getRowCount(); j++) {
//                        result = result + "<Td>" + basisHeader1 + "</Td>";
//                        result = result + "<Td>" + basisHeader2 + "</Td>";
                        result.append("<Td>").append(basisHeader1).append("</Td>");
                        result.append("<Td>").append(basisHeader2).append("</Td>");
                    }
//                    result = result + "<Td>" + basisHeader1 + "</Td><Td>" + basisHeader2 + "</Td>";
//                    result = result + "<Td>" + basisHeader1 + "</Td><Td>" + basisHeader2 + "</Td><Td>" + basisHeader1 + "</Td><Td>" + basisHeader2 + "</Td></Tr>";
                    result.append("<Td>").append(basisHeader1).append("</Td><Td>").append(basisHeader2).append("</Td>");
                    result.append("<Td>").append(basisHeader1).append("</Td><Td>").append(basisHeader2).append("</Td><Td>").append(basisHeader1).append("</Td><Td>").append(basisHeader2).append("</Td></Tr>");
                }
            }

            // display the rowedgevalues
            if (!viewByElement.equalsIgnoreCase("0")) {
                for (int x = 1; x < paramVals.size(); x++) {
                    String pVal = paramVals.get(x).toString();
                    long rowTotalVal = 0;
                    if (rowTotal.containsKey(pVal)) {
                        rowTotalVal = ((Long) rowTotal.get(pVal)).longValue();
                    }
                    // when no child is there then no drill otherwise give drill
                    if (!childElement.equalsIgnoreCase("-1")) {
//                        result = result + "<Tr>" + "<Td style=\"width:auto\"><a href=\"javascript:submitDrill('" + drllUrl + paramVals.get(x).toString() + "')\">" + paramVals.get(x).toString() + "</Td>";
                        result.append("<Tr><Td style=\"width:auto\"><a href=\"javascript:submitDrill('").append(drllUrl).append(paramVals.get(x).toString()).append("')\">").append(paramVals.get(x).toString()).append("</Td>");
                    } else if (childElement.equalsIgnoreCase("-1")) {
//                        result = result + "<Tr>" + "<Td style=\"width:auto\">" + paramVals.get(x).toString() + "</Td>";
                        result.append("<Tr><Td style=\"width:auto\">").append(paramVals.get(x).toString()).append("</Td>");
                    }

                    HashMap dt = new HashMap();
                    if (allV.containsKey(pVal)) {
                        dt = (HashMap) allV.get(pVal);
                    }
                    for (int y = 0; y < time.getRowCount(); y++) {
                        String timeV = time.getFieldValueString(y, 0);
                        long mV = 0;
                        if (dt.containsKey(timeV)) {
                            mV = ((Long) dt.get(timeV)).longValue();
                        }

                        String value = "";
                        if (mV != 0) {
//                            value = "" + mV + "";
                            value = String.valueOf(mV);
                        }
                        // originalResult.put(paramVals.get(x)+":"+timeV,Integer.valueOf(mV));
                        String freezeCell = "";
                        if (periodFlag == true) {
                            if (value.equalsIgnoreCase("0") || value.equalsIgnoreCase("")) {
                                freezeCell = "readonly";
                            }
                        }

                        if (periodFlag == true) {
                            if (displayPeriodFlagMessage == false) {
                                if (value.equalsIgnoreCase("0") || value.equalsIgnoreCase("")) {
                                    displayPeriodFlagMessage = true;
                                }
                            }
                        }
//                        result = result + "<Td><input style=\"width:99%\" onkeypress=\"return isNumberKey(event)\" type=\"text\" id=\"" + paramVals.get(x) + ":" + timeV + "\" onkeyup=\"autoSum('" + paramVals.get(x) + ":" + timeV + "')\" name=\"" + paramVals.get(x) + ":" + timeV + "\"  " + freezeCell + " value=\"" + value + "\"></Td>";
                        result.append("<Td><input style=\"width:99%\" onkeypress=\"return isNumberKey(event)\" type=\"text\" id=\"").append(paramVals.get(x)).append(":").append(timeV).append("\" onkeyup=\"autoSum('").append(paramVals.get(x)).append(":").append(timeV).append("')\" name=\"").append(paramVals.get(x)).append(":").append(timeV).append("\"  ").append(freezeCell).append(" value=\"").append(value).append("\"></Td>");

                    }
                    //if(showRowTotal==true)
                    //{
//                    result = result + "<Td><input style=\"width:99%\" class=\"displayTotal\" readonly value=\"" + rowTotalVal + "\"></Td>";
                    result.append("<Td><input style=\"width:99%\" class=\"displayTotal\" readonly value=\"").append(rowTotalVal).append("\"></Td>");
                    // }
//                    result = result + "</Tr>";
                    result.append("</Tr>");
                }
            }
            String parentPeriodType = collect.getParentTimeId(periodType, targetId);
            if (viewByElement.equalsIgnoreCase("0")) {
                if (overAllFlag == true) {

                    // paramVals.add("OverAll Target");
                    // paramVals.add("OverAll");
                    HashMap dt = new HashMap();
//                    result = result + "<Tr><Td>" + paramVals.get(0).toString() + "</Td>";
                    result.append("<Tr><Td>").append(paramVals.get(0).toString()).append("</Td>");
                    long rTotal = 0;

                    for (int x = 1; x < paramVals.size(); x++) {
                        String pVal = paramVals.get(x).toString();
                        if (allV.containsKey(pVal)) {
                            dt = (HashMap) allV.get(pVal);
                        }
                        for (int y = 0; y < time.getRowCount(); y++) {
                            String timeV = time.getFieldValueString(y, 0);
                            long mV = 0;
                            String value = "";
                            if (dt.containsKey(timeV)) {
                                mV = ((Long) dt.get(timeV)).longValue();
                                if (mV != 0) {
//                                    value = "" + mV + "";
                                    value = Long.toString(mV);
                                }
                            }
                            rTotal = rTotal + mV;
                            result.append("<Td><input style=\"width:99%\" onkeypress=\"return isNumberKey(event)\" type=\"text\" id=\"").append(paramVals.get(x)).append(":").append(timeV).append("\" onkeyup=\"autoSum('").append(paramVals.get(x)).append(":").append(timeV).append("')\" name=\"").append(paramVals.get(x)).append(":").append(timeV).append("\"   value=\"").append(value).append("\"></Td>");
//                            result = result + "<Td><input style=\"width:99%\" onkeypress=\"return isNumberKey(event)\" type=\"text\" id=\"" + paramVals.get(x) + ":" + timeV + "\" onkeyup=\"autoSum('" + paramVals.get(x) + ":" + timeV + "')\" name=\"" + paramVals.get(x) + ":" + timeV + "\"   value=\"" + value + "\"></Td>";

                        }
                    }
//                    result = result + "<Td><input type=\"text\" style=\"width:99%\" class=\"displayTotal\" readonly value=" + rTotal + "></Td></Tr>";
                    result.append("<Td><input type=\"text\" style=\"width:99%\" class=\"displayTotal\" readonly value=").append(rTotal).append("></Td></Tr>");
                } else {
                    ArrayList upperTimeList = new ArrayList();
                    if (periodType.equalsIgnoreCase("Qtr")) {
                        if (parentPeriodType.equalsIgnoreCase("Year")) {
                            for (int y = 0; y < time.getRowCount(); y++) {
                                String timeV = time.getFieldValueString(y, 0);
                                if (!upperTimeList.contains(timeV.substring(2))) {
                                    upperTimeList.add(timeV.substring(2));
                                }
                            }
                            //upperTimeList=
                        }
                    } else if (periodType.equalsIgnoreCase("Month")) {
                        Vector qtrVal = new Vector();
                        if (parentPeriodType.equalsIgnoreCase("Qtr")) {
                            for (int y = 0; y < time.getRowCount(); y++) {
                                String timeV = time.getFieldValueString(y, 0);
                                String QtrForMonQ = "select DISTINCT cmqtr_code, pmyqtr from prg_acn_mon_denom where mon_name='" + timeV + "'";
                                PbReturnObject pb = pbDb.execSelectSQL(QtrForMonQ);
                                if (!upperTimeList.contains(pb.getFieldValueString(0, 0).substring(1))) {
                                    upperTimeList.add(pb.getFieldValueString(0, 0).substring(1));
                                }
                            }
                            //upperTimeList=
                        }
                        if (parentPeriodType.equalsIgnoreCase("Year")) {
                            for (int y = 0; y < time.getRowCount(); y++) {
                                String timeV = time.getFieldValueString(y, 0);
                                String QtrForMonQ = "select distinct cyear  from prg_acn_mon_denom where mon_name='" + timeV + "'";
                                ////////////////////////////.println(" QtrForMonQ "+ QtrForMonQ);
                                PbReturnObject pb = pbDb.execSelectSQL(QtrForMonQ);
                                if (!upperTimeList.contains(pb.getFieldValueString(0, 0))) {
                                    upperTimeList.add(pb.getFieldValueString(0, 0));
                                }
                            }
                            //upperTimeList=
                        }
                    } else if (periodType.equalsIgnoreCase("Day")) {
                        Vector qtrVal = new Vector();
                        ////////////////////////////.println(" in if qtr month ");
                        if (parentPeriodType.equalsIgnoreCase("Month")) {
                            for (int y = 0; y < time.getRowCount(); y++) {
                                String timeV = time.getFieldValueString(y, 0);
                                String DaysForMonQ = "select to_char(ddate,'MON-yy') from pr_day_denom where ddate=to_date('" + timeV + "','dd-mm-yy')";
                                ////////////////////////////.println(" DaysForMonQ "+ DaysForMonQ);
                                PbReturnObject pb = pbDb.execSelectSQL(DaysForMonQ);
                                if (!upperTimeList.contains(pb.getFieldValueString(0, 0))) {
                                    upperTimeList.add(pb.getFieldValueString(0, 0));
                                }
                            }
                            //upperTimeList=
                        }
                    }
                    ////////////////////////////.println(" upperTimeList "+upperTimeList);
                    String upperPerioQ = "";
                    if (parentPeriodType.equalsIgnoreCase("year")) {
                        upperPerioQ = " select sum(" + measureName.trim().replace(" ", "_") + "),t_year from " + targetTable.trim() + " where target_id='?' and viewby='OverAll' group by t_year order by t_year";
                    } else if (parentPeriodType.equalsIgnoreCase("Qtr")) {
                        upperPerioQ = " select sum(" + measureName.trim().replace(" ", "_") + "),t_qtr from " + targetTable.trim() + " where target_id='?' and viewby='OverAll' group by t_qtr order by t_qtr";
                    } else if (parentPeriodType.equalsIgnoreCase("Month")) {
                        upperPerioQ = " select sum(" + measureName.trim().replace(" ", "_") + "),t_month from " + targetTable.trim() + " where target_id='?' and viewby='OverAll' group by t_month order by t_month";
                    }

                    ps = con.prepareStatement(upperPerioQ);
                    ps.setString(1, targetId);
                    rs = ps.executeQuery();
                    PbReturnObject timeRestrictionD = new PbReturnObject();
                    if (rs != null) {
                        timeRestrictionD = new PbReturnObject(rs);
                    }
                    HashMap timeParentMap = new HashMap();
                    long lVal = 0;
                    for (int m = 0; m < timeRestrictionD.getRowCount(); m++) {
                        lVal = Long.parseLong(timeRestrictionD.getFieldValueString(m, 0));
                        timeParentMap.put(timeRestrictionD.getFieldValueString(m, 1), new Long(lVal));
                    }
                    //paramVals.add("OverAll Target");
                    // paramVals.add("OverAll");
                    HashMap dt = new HashMap();
//                    result = result + "<Tr><Td>" + paramVals.get(0).toString() + "</Td>";
                    result.append("<Tr><Td>").append(paramVals.get(0).toString()).append("</Td>");
                    long timeResTotal = 0;
                    long rTotalForOverall = 0;
                    for (int x = 1; x < paramVals.size(); x++) {

                        String pVal = paramVals.get(x).toString();
                        if (allV.containsKey(pVal)) {
                            dt = (HashMap) allV.get(pVal);
                        }
                        for (int y = 0; y < upperTimeList.size(); y++) {
                            if (timeParentMap.containsKey(upperTimeList.get(y).toString())) {
                                Long lv = (Long) timeParentMap.get(upperTimeList.get(y).toString());
                                timeResTotal = timeResTotal + lv;
                            }
                        }
                        for (int y = 0; y < time.getRowCount(); y++) {
                            String timeV = time.getFieldValueString(y, 0);
                            long mV = 0;
                            String value = "";
                            if (dt.containsKey(timeV)) {
                                mV = ((Long) dt.get(timeV)).longValue();
                                if (mV != 0) {
//                                    value = "" + mV + "";
                                    value = String.valueOf(mV);
                                }
                            }
                            long perc = 0;
                            if (timeResTotal != 0) {
                                perc = (mV * 100) / timeResTotal;
                            }
                            rTotalForOverall = rTotalForOverall + mV;
                            String pervalue = "";
//                            pervalue = "" + perc + "";
                            pervalue = String.valueOf(perc);
                            if (basisVal.equalsIgnoreCase("absolute")) {
//                                result = result + "<Td><input style=\"width:99%\" onkeypress=\"return isNumberKey(event)\" type=\"text\" onfocus=\"onFocusValue('" + paramVals.get(x) + ":" + timeV + "')\" id=\"" + paramVals.get(x) + ":" + timeV + "\" onkeyup=\"autoSum('" + paramVals.get(x) + ":" + timeV + "')\" name=\"" + paramVals.get(x) + ":" + timeV + "\"   value=\"" + value + "\"></Td>";
//                                result = result + "<Td><input style=\"background-color:silver;width:99%\" id=\"" + paramVals.get(x) + ":" + timeV + ":d" + "\" readonly onkeypress=\"return isNumberKey(event)\" type=\"text\" id=\"" + paramVals.get(x) + ":" + timeV + ":d\" name=\"" + paramVals.get(x) + ":" + timeV + ":d\"   value=\"" + perc + "\"></Td>";
                                result.append("<Td><input style=\"width:99%\" onkeypress=\"return isNumberKey(event)\" type=\"text\" onfocus=\"onFocusValue('").append(paramVals.get(x)).append(":").append(timeV).append("')\" id=\"").append(paramVals.get(x)).append(":").append(timeV).append("\" onkeyup=\"autoSum('").append(paramVals.get(x)).append(":").append(timeV).append("')\" name=\"").append(paramVals.get(x)).append(":").append(timeV).append("\"   value=\"").append(value).append("\"></Td>");
                                result.append("<Td><input style=\"background-color:silver;width:99%\" id=\"").append(paramVals.get(x) + ":" + timeV + ":d" + "\" readonly onkeypress=\"return isNumberKey(event)\" type=\"text\" id=\"").append(paramVals.get(x)).append(":").append(timeV).append(":d\" name=\"").append(paramVals.get(x)).append(":").append(timeV).append(":d\"   value=\"").append(perc).append("\"></Td>");
                            }
                            if (basisVal.equalsIgnoreCase("percentage")) {
//                                result = result + "<Td><input style=\"width:99%\" onkeypress=\"return isNumberKey(event)\" type=\"text\" onfocus=\"onFocusValue('" + paramVals.get(x) + ":" + timeV + "')\" id=\"" + paramVals.get(x) + ":" + timeV + "\" onkeyup=\"autoSum('" + paramVals.get(x) + ":" + timeV + "')\" name=\"" + paramVals.get(x) + ":" + timeV + "\"   value=\"" + perc + "\"></Td>";
//                                result = result + "<Td><input style=\"background-color:silver;width:99%\" id=\"" + paramVals.get(x) + ":" + timeV + ":d" + "\" readonly name=\"" + paramVals.get(x) + ":" + timeV + ":d" + "\"   value=\"" + value + "\"></Td>";
                                result.append("<Td><input style=\"width:99%\" onkeypress=\"return isNumberKey(event)\" type=\"text\" onfocus=\"onFocusValue('").append(paramVals.get(x)).append(":").append(timeV).append("')\" id=\"").append(paramVals.get(x)).append(":").append(timeV).append("\" onkeyup=\"autoSum('").append(paramVals.get(x)).append(":").append(timeV).append("')\" name=\"").append(paramVals.get(x)).append(":").append(timeV).append("\"   value=\"").append(perc).append("\"></Td>");
                                result.append("<Td><input style=\"background-color:silver;width:99%\" id=\"").append(paramVals.get(x)).append(":").append(timeV).append(":d").append("\" readonly name=\"").append(paramVals.get(x)).append(":").append(timeV).append(":d").append("\"   value=\"").append(value).append("\"></Td>");

                            }
                            // result = result+"<Td><input onkeypress=\"return isNumberKey(event)\" type=\"text\" id=\""+paramVals.get(x)+":"+timeV+"\" onkeyup=\"autoSum('"+paramVals.get(x)+":"+timeV+"')\" name=\""+paramVals.get(x)+":"+timeV+"\"   value=\""+value+"\"></Td>";

                            //for restricting upper time level total
                        }
                        long percRow = 0;
                        if (rTotalForOverall != 0) {
                            percRow = (rTotalForOverall / timeResTotal) * 100;
                        }

                        // for row total
                        if (basisVal.equalsIgnoreCase("absolute")) {
//                            result = result + "<Td><input class=\"displayTotal\" style=\"width:99%\" readonly value=\"" + rTotalForOverall + "\"></Td><Td><input type=\"text\" style=\"width:99%\" class=\"displayTotal\" readonly value=" + percRow + "></Td>";
                            result.append("<Td><input class=\"displayTotal\" style=\"width:99%\" readonly value=\"").append(rTotalForOverall).append("\"></Td><Td><input type=\"text\" style=\"width:99%\" class=\"displayTotal\" readonly value=").append(percRow).append("></Td>");
                        }
                        if (basisVal.equalsIgnoreCase("percentage")) {
//                            result = result + "<Td><input class=\"displayTotal\" style=\"width:99%\" readonly value=\"" + percRow + "\"></Td><Td><input type=\"text\" style=\"width:99%\" class=\"displayTotal\" readonly value=" + rTotalForOverall + "></Td>";
                            result.append("<Td><input class=\"displayTotal\" style=\"width:99%\" readonly value=\"").append(percRow).append("\"></Td><Td><input type=\"text\" style=\"width:99%\" class=\"displayTotal\" readonly value=").append(rTotalForOverall).append("></Td>");
                        }

                        RestrictingTotal.put("OverAll", new Long(timeResTotal));
                        if (timeResTotal == 0) {
                            freezeButton = "disabled";
                        }
                        // for restricting total
                        long timeRestTotPer = 0;
                        timeRestTotPer = 100;
                        if (basisVal.equalsIgnoreCase("absolute")) {
//                            result = result + "<Td><input style=\"width:99%\" type=\"text\" class=\"displayTotal\" readonly value=\"" + timeResTotal + "\"></Td>";
//                            result = result + "<Td><input style=\"width:99%\" type=\"text\" class=\"displayTotal\" readonly value=\"" + timeRestTotPer + "\"></Td>";
                            result.append("<Td><input style=\"width:99%\" type=\"text\" class=\"displayTotal\" readonly value=\"").append(timeResTotal).append("\"></Td>");
                            result.append("<Td><input style=\"width:99%\" type=\"text\" class=\"displayTotal\" readonly value=\"").append(timeRestTotPer).append("\"></Td>");
                        }
                        if (basisVal.equalsIgnoreCase("percentage")) {
//                            result = result + "<Td><input style=\"width:99%\" type=\"text\" class=\"displayTotal\" readonly value=\"" + timeRestTotPer + "\"></Td>";
//                            result = result + "<Td><input style=\"width:99%\" type=\"text\" class=\"displayTotal\" readonly value=\"" + timeResTotal + "\"></Td>";
                            result.append("<Td><input style=\"width:99%\" type=\"text\" class=\"displayTotal\" readonly value=\"").append(timeRestTotPer).append("\"></Td>");
                            result.append("<Td><input style=\"width:99%\" type=\"text\" class=\"displayTotal\" readonly value=\"").append(timeResTotal).append("\"></Td>");
                        }

                        //for difference
                        long difVal = 0;
                        difVal = timeResTotal - rTotalForOverall;
                        long difValPerc = 0;
                        difValPerc = 100 - percRow;
                        if (basisVal.equalsIgnoreCase("absolute")) {
//                            result = result + "<Td><input type=\"text\" style=\"width:99%\" class=\"displayTotal\" readonly value=\"" + difVal + "\"></Td>";
//                            result = result + "<Td><input type=\"text\" style=\"width:99%\" class=\"displayTotal\" readonly value=\"" + difValPerc + "\"></Td>";
                            result.append("<Td><input type=\"text\" style=\"width:99%\" class=\"displayTotal\" readonly value=\"").append(difVal).append("\"></Td>");
                            result.append("<Td><input type=\"text\" style=\"width:99%\" class=\"displayTotal\" readonly value=\"").append(difValPerc).append("\"></Td>");
                        }
                        if (basisVal.equalsIgnoreCase("percentage")) {
                            result.append("<Td><input type=\"text\" style=\"width:99%\" class=\"displayTotal\" readonly value=\"").append(difValPerc).append("\"></Td>");
                            result.append("<Td><input type=\"text\" style=\"width:99%\" class=\"displayTotal\" readonly value=\"").append(difVal).append("\"></Td>");

                        }
                    }
//                    result = result + "</Tr>";
                    result.append("</Tr>");
                }
            }

            if (!viewByElement.equalsIgnoreCase("0")) {
                long gTot = 0;
//                result = result + "<Tr><Td style=\"font-weight:bold;font-size:11px\">Column Total</Td>";
                result.append("<Tr><Td style=\"font-weight:bold;font-size:11px\">Column Total</Td>");
                long colTotVal = 0;
                for (int p = 0; p < colEdgeVals.size(); p++) {
                    String colKey = colEdgeVals.get(p).toString();
                    colTotVal = 0;
                    if (colTotal.containsKey(colKey)) {
                        colTotVal = ((Long) colTotal.get(colKey)).longValue();
                    }
                    result.append("<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"").append(colTotVal).append("\"></Td>");
                    gTot = gTot + colTotVal;
                    //  RestrictingTotal.put(colEdgeVals.get(p).toString(),new Long(colTotVal));
                }
                result.append("<Td><input type=\"text\" class=\"displayTotal\" readonly id=\"GrandTotal\" value=").append(gTot).append("></Td></Tr>");
//                result = result + "<Td><input type=\"text\" class=\"displayTotal\" readonly id=\"GrandTotal\" value=" + gTot + "></Td></Tr>";
            }
            // to find the parent total
            Object tarObj1[] = new Object[2];
            tarObj1[0] = targetId;
            tarObj1[1] = viewByElement;
            String getParents = resundle.getString("getParents");
            String fingetParents = pbDb.buildQuery(getParents, tarObj1);
            PbReturnObject viewByElementInfoOb = pbDb.execSelectSQL(fingetParents);

            // get all the parent element ids of the view by
            boolean addParents = false;
            for (int y = 0; y < viewByElementInfoOb.getRowCount(); y++) {
                String localEle = viewByElementInfoOb.getFieldValueString(y, "ELEMENT_ID");
                if (addParents == true) {
                    viewByParentsList.add(localEle);
                }
                if (localEle.equalsIgnoreCase(viewByElement)) {
                    addParents = true;
                }
            }

//             for appending the non ALl values of any element ids from the request parameters
            StringBuilder parentsFilter = new StringBuilder();
            for (int i = 0; i < keys.length; i++) {
                a = (String) allParamHashMap.get(keys[i]);
                if (viewByParentsList.contains(keys[i])) {
                    if (!a.equalsIgnoreCase("All")) {
//                        parentsFilter = parentsFilter + " and " + elementCols.get(keys[i]) + "='" + a + "'";
                        parentsFilter.append(" and ").append(elementCols.get(keys[i])).append("='").append(a).append("'");
                    }
                }
            }

            String getParentElementId = resundle.getString("getParentElementId");
            String fingetParentElementId = pbDb.buildQuery(getParentElementId, tarObj1);

            ArrayList parentElementsList = new ArrayList();
            PbReturnObject allElementInfoOb = pbDb.execSelectSQL(fingetParentElementId);
            boolean skipChilds = false;
            for (int h = 0; h < allElementInfoOb.getRowCount(); h++) {
                if (allElementInfoOb.getFieldValueString(h, "ELEMENT_ID").equalsIgnoreCase(viewByElement)) {
                    skipChilds = true;
                }
                if (skipChilds == false) {
                    parentElementsList.add(allElementInfoOb.getFieldValueString(h, "ELEMENT_ID"));
                }

            }

            String parentEleId = "-1";
            for (int g = 0; g < allElementInfoOb.getRowCount(); g++) {
                int previous = g - 1;
                String localElementId = allElementInfoOb.getFieldValueString(g, "ELEMENT_ID");
                if (viewByElement.equalsIgnoreCase(localElementId)) {
                    if (previous >= 0) {
                        parentEleId = allElementInfoOb.getFieldValueString(previous, "ELEMENT_ID");
                    }
                }
            }
            String parentBussCol = "";
            if (elementCols.containsKey(parentEleId)) {
                parentBussCol = (String) elementCols.get(parentEleId);
                parentBussCol = "," + parentBussCol;
            }

            ArrayList bussTable = new ArrayList();

            // to get the business table ids and get the data from the customer database
            for (int g = 0; g < allElementInfoOb.getRowCount(); g++) {
                String localElementId = allElementInfoOb.getFieldValueString(g, "ELEMENT_ID");
                if (viewByElement.equalsIgnoreCase(localElementId)) {
                    bussTable.add(allElementInfoOb.getFieldValueString(g, "BUSS_TABLE"));
                }
                if (parentEleId.equalsIgnoreCase(localElementId)) {
                    bussTable.add(allElementInfoOb.getFieldValueString(g, "BUSS_TABLE"));
                }
            }

            String getTargetPrimParam = resundle.getString("getTargetPrimParam");
            String fingetTargetPrimParam = pbDb.buildQuery(getTargetPrimParam, tarOb);
            PbReturnObject targetInfoOb = pbDb.execSelectSQL(fingetTargetPrimParam);

            primParamEleId = targetInfoOb.getFieldValueString(0, "TARGET_PRIM_PARAM");

            String getAllElementsInfo = resundle.getString("getAllElementsInfo");
            String fingetAllElementsInfo = pbDb.buildQuery(getAllElementsInfo, tarOb);
            PbReturnObject targetElementsInfoOb = pbDb.execSelectSQL(fingetAllElementsInfo);
            HashMap elementDet = new HashMap();
            int localDim = targetElementsInfoOb.getFieldValueInt(0, "ELEMENT_ID");
            int dimId = localDim;
            String parentId = "-1";
            HashMap parentsMap = new HashMap();
            for (int m = 0; m < targetElementsInfoOb.getRowCount(); m++) {
                parentsMap.put(targetElementsInfoOb.getFieldValueString(m, "CHILD_ELEMENT_ID"), targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"));
            }
            for (int m = 0; m < targetElementsInfoOb.getRowCount(); m++) {
                ArrayList det = new ArrayList();
                det.add(0, targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"));
                parentId = "-1";
                if (parentsMap.containsKey(targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"))) {
                    parentId = parentsMap.get(targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID")).toString();
                }
                //parentId=targetElementsInfoOb.getFieldValueString(m,"PARENT_ID");
                if (parentId.equalsIgnoreCase("")) {
                    parentId = "-1";
                }
                det.add(1, parentId);
                det.add(2, targetElementsInfoOb.getFieldValueString(m, "PARAM_DISP_NAME"));
                // det.add(3,targetElementsInfoOb.getFieldValueString(m,"PARAM_DISP_NAME"));
                elementDet.put(targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"), det);
            }
            //elementCols
            //viewByElement

            parentBussCol = "";
            HashMap parentTotals = new HashMap();
            String parentDispName = "";
            String parentEle = "";
            parentDataQ = "";

            // when primary parameter dont show restricting total else show
            if (primParamEleId.equalsIgnoreCase(viewByElement)) {
                showRestrictingTotal = false;
            }

            if (showRestrictingTotal == true) {
                if (elementDet.containsKey(viewByElement)) {
                    ArrayList eleDet = new ArrayList();
                    eleDet = (ArrayList) elementDet.get(viewByElement);
                    parentEle = eleDet.get(1).toString();
                    parentDispName = eleDet.get(2).toString();
                }
                if (elementCols.containsKey(parentEle)) {
                    parentBussCol = elementCols.get(parentEle).toString();
                }

                // to put the parent filters on row view by
                ArrayList allParents = new ArrayList();
                boolean messageShow = false;

                String parentParamFilter = "";
                String[] reqParams = (String[]) (targetParametersValues.keySet()).toArray(new String[0]);
                for (int o = 0; o < reqParams.length; o++) {
                    String key = reqParams[o];
                    String value = "-1";
                    String val = targetParametersValues.get(key).toString();
                    if (key.equalsIgnoreCase(parentEle)) {
                        if (targetParametersValues.containsKey(key)) {
                            if (!val.equalsIgnoreCase("All")) {
                                parentParamFilter = " and " + elementCols.get(key).toString() + "='" + val + "'";
                            }
                            if (val.equalsIgnoreCase("All")) {
                                if (errorMessage.length() == 0) {
                                    errorMessage = "As " + viewByNames.get(parentEle.trim()).toString() + " is not selected for " + viewByNames.get(viewByElement.trim()).toString() + ".So no data entry is allowed.";
                                }
                            }
                        }
                    }
                    if (viewByParentsList.contains(key)) {
                        if (!parentEle.equalsIgnoreCase(key)) {
                            if (val.equalsIgnoreCase("All")) {
                                errorMessage = errorMessage + "Select " + viewByNames.get(key).toString() + " for data entry.";
                                disableSaveButtonFlag = true;
                            }
                        }
                    }
                }
                if (periodType.equalsIgnoreCase("Day")) {
                    if (!parentEle.equalsIgnoreCase("-1")) {
                        parentDataQ = "select " + elementCols.get(parentEle.trim()).toString() + ",sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + " group by " + elementCols.get(parentEle.trim()).toString() + ",to_char(t_date,'dd-mm-yy') order by " + elementCols.get(parentEle.trim()).toString() + ",to_char(t_date,'dd-mm-yy')";
                    } else {
                        parentDataQ = "select sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' and t_day is not null " + parentParamFilter + " " + parentsFilter + " group by to_char(t_date,'dd-mm-yy') order by to_char(t_date,'dd-mm-yy')";
                    }
                } // get the data for parent parameter after putting its filters for parents
                else if (periodType.equalsIgnoreCase("Month")) {
                    if (!parentEle.equalsIgnoreCase("-1")) {
                        parentDataQ = "select " + elementCols.get(parentEle.trim()).toString() + ",sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + " group by " + elementCols.get(parentEle.trim()).toString() + ",t_month order by " + elementCols.get(parentEle.trim()).toString() + ",t_month ";
                    } else {
                        parentDataQ = "select sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' and t_month is not null  " + parentParamFilter + " " + parentsFilter + " group by t_month order by t_month";
                    }
                } else if (periodType.equalsIgnoreCase("Quarter") || periodType.equalsIgnoreCase("Qtr")) {
                    if (!parentEle.equalsIgnoreCase("-1")) {
                        parentDataQ = "select " + elementCols.get(parentEle.trim()).toString() + ",sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + " group by " + elementCols.get(parentEle.trim()).toString() + ",t_qtr order by " + elementCols.get(parentEle.trim()).toString() + ",t_qtr ";
                    } else {
                        parentDataQ = "select sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' and t_qtr is not null  " + parentParamFilter + " " + parentsFilter + "group by t_qtr order by t_qtr ";
                    }
                } else if (periodType.equalsIgnoreCase("Year")) {
                    if (!parentEle.equalsIgnoreCase("-1")) {
                        parentDataQ = "select " + elementCols.get(parentEle.trim()).toString() + ",sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + " group by " + elementCols.get(parentEle.trim()).toString() + ",t_year order by " + elementCols.get(parentEle.trim()).toString() + ",t_year";
                    } else {
                        parentDataQ = "select sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' and t_year is not null " + parentParamFilter + " " + parentsFilter + " group by t_year order by t_year ";
                    }
                }
                ////////////////////////////.println(" parentDataQ "+parentDataQ);
                //PbReturnObject parentTotObj = pbDb.execSelectSQL(parentDataQ);
                // PbReturnObject parentTotObj = pbDb.execSelectSQL(parentDataQ);
                psP = con.prepareStatement(parentDataQ);
                rsP = psP.executeQuery();
                PbReturnObject parentTotObj = new PbReturnObject(rsP);

                primH2 = new HashMap();
                if (!parentEle.equalsIgnoreCase("-1")) {
                    for (int i = 0; i < parentTotObj.getRowCount(); i++) {
                        oldPrimV = parentTotObj.getFieldValueString(i, 0);
                        if (newPrimV.equalsIgnoreCase("")) {
                            newPrimV = oldPrimV;
                        }
                        oldPrimV = parentTotObj.getFieldValueString(i, 0);
                        String dat = parentTotObj.getFieldValueString(i, 2);
                        long mValue = parentTotObj.getFieldValueInt(i, 1);
                        long old = 0;
                        if (primH2.containsKey(dat)) {
                            old = ((Long) primH2.get(dat)).longValue();
                        }
                        mValue = mValue + old;
                        primH2.put(dat, new Long(mValue));
                    }
                } else {
                    for (int i = 0; i < parentTotObj.getRowCount(); i++) {
                        String dat = parentTotObj.getFieldValueString(i, 1);
                        long mValue = Long.parseLong(parentTotObj.getFieldValueString(i, 0));
                        long old = 0;
                        if (primH2.containsKey(dat)) {
                            old = ((Long) primH2.get(dat)).longValue();
                        }
                        mValue = mValue + old;
                        primH2.put(dat, new Long(mValue));
                    }
                }
                result.append("<Tr><Td style=\"font-weight:bold;font-size:11px\">Restricting Total</Td>");
//                result = result + "<Tr><Td style=\"font-weight:bold;font-size:11px\">Restricting Total</Td>";
                long restrictGTotal = 0;
                for (int k = 0; k < colEdgeVals.size(); k++) {
                    long rTotal = 0;
                    if (primH2.containsKey(colEdgeVals.get(k).toString())) {
                        rTotal = ((Long) primH2.get(colEdgeVals.get(k).toString())).longValue();
                    }
                    restrictGTotal = restrictGTotal + rTotal;
//                    result = result + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"" + rTotal + "\"></Td>";
                    result.append("<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"").append(rTotal).append("\"></Td>");
                    if (disableSaveButtonFlag == false) {
                        if (primH2.containsKey(colEdgeVals.get(k).toString())) {
                            if (rTotal != 0) {
                                disableSaveButtonFlag = true;
                            }
                        }
                    }
                    RestrictingTotal.put(colEdgeVals.get(k).toString(), new Long(rTotal));
                }
//                result = result + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=" + restrictGTotal + "></Td></Tr>";
//                result = result + "<Tr><Td style=\"font-weight:bold;font-size:11px\">Difference</Td>";
                result.append("<Td><input type=\"text\" readonly class=\"displayTotal\" value=").append(restrictGTotal).append("></Td></Tr>");
                result.append("<Tr><Td style=\"font-weight:bold;font-size:11px\">Difference</Td>");
                for (int k = 0; k < colEdgeVals.size(); k++) {
//                    result = result + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>";
                    result.append("<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>");
                }
//                result = result + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td></Tr>";
                result.append("<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td></Tr>");
            }

            // to show the overall target
            String primParamFilter = "";
            String overAllTotQ = "";
            // to put the primary paramteres hierarchy filters
            String[] reqParams = (String[]) (targetParametersValues.keySet()).toArray(new String[0]);
            for (int o = 0; o < reqParams.length; o++) {
                String key = reqParams[o];
                String value = "-1";
                if (key.equalsIgnoreCase(primParamEleId)) {
                    if (targetParametersValues.containsKey(key)) {
                        String val = targetParametersValues.get(key).toString();
                        if (!val.equalsIgnoreCase("All")) {
                            primParamFilter = " and " + elementCols.get(key).toString() + "='" + val + "'";
                        }

                    }
                }
            }
            String prim = "";
            String primName = "";
            if (viewByNames.containsKey(primParamEleId.trim())) {
                primName = viewByNames.get(primParamEleId.trim()).toString();
            }
            if (elementCols.containsKey(primParamEleId.trim())) {
                prim = elementCols.get(primParamEleId.trim()).toString();
            }
            if (periodType.equalsIgnoreCase("Day")) {
                overAllTotQ = "Select " + prim + ",sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + primName + "' " + primParamFilter + " group by " + prim + ",to_char(t_date,'dd-mm-yy') order by " + prim + ",to_char(t_date,'dd-mm-yy')";
            } else if (periodType.equalsIgnoreCase("Month")) {
                overAllTotQ = "Select " + prim + ",sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + primName + "' " + primParamFilter + " group by " + prim + ",t_month order by " + prim + ",t_month";
            } // overAllTotQ = "Select "+elementCols.get(primParamEleId.trim()).toString()+",sum("+measureName.trim()+"),t_month from "+targetTable.trim()+" where target_id="+targetId+" and viewby='"+viewByNames.get(primParamEleId.trim()).toString()+"' "+primParamFilter+" group by "+elementCols.get(primParamEleId.trim()).toString()+",t_month order by "+elementCols.get(primParamEleId.trim()).toString()+",t_month";
            else if (periodType.equalsIgnoreCase("Quarter")) {
                overAllTotQ = "Select " + prim + ",sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + primName + "' " + primParamFilter + " group by " + prim + ",t_qtr order by " + prim + ",t_qtr";
            } else if (periodType.equalsIgnoreCase("Year")) {
                overAllTotQ = "Select " + prim + ",sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + primName + "' " + primParamFilter + " group by " + prim + ",t_year order by " + prim + ",t_year";
            }

            ////////////////////////////.println(" overAllTotQ "+overAllTotQ);
            //  PbReturnObject overAllObj = pbDb.execSelectSQL(overAllTotQ);
            if (!overAllTotQ.equalsIgnoreCase("")) {
                psO = con.prepareStatement(overAllTotQ);
            }
            if (viewByNames.containsKey(primParamEleId.trim())) {
                rsO = psO.executeQuery();
            }
            PbReturnObject overAllObj = new PbReturnObject();
            if (rsO != null) {
                overAllObj = new PbReturnObject(rsO);
            }

            primH2 = new HashMap();
            for (int i = 0; i < overAllObj.getRowCount(); i++) {
                oldPrimV = overAllObj.getFieldValueString(i, 0);
                if (newPrimV.equalsIgnoreCase("")) {
                    newPrimV = oldPrimV;
                }
                oldPrimV = overAllObj.getFieldValueString(i, 0);
                String dat = overAllObj.getFieldValueString(i, 2);
                int mValue = overAllObj.getFieldValueInt(i, 1);
                int old = 0;
                if (primH2.containsKey(dat)) {
                    old = ((Integer) primH2.get(dat)).intValue();
                }
                mValue = mValue + old;
                primH2.put(dat, Integer.valueOf(mValue));
            }
            int grandTotal = 0;
            for (int k = 0; k < colEdgeVals.size(); k++) {
                int rTotal = 0;
                if (primH2.containsKey(colEdgeVals.get(k).toString())) {
                    rTotal = ((Integer) primH2.get(colEdgeVals.get(k).toString())).intValue();
                }
                grandTotal = grandTotal + rTotal;
            }
            ////////////////////////////////////////////////////////.println(" grandTotal "+grandTotal);
            secAnalyze = "Time";

            // overAllMessage=" The Overall Target Set By Primary Parameter '"+viewByNames.get(primParamEleId.trim()).toString()+"' is "+grandTotal;
        } else {
            dataQuery2 = "";
            String displayDate = startRange;
            dateMeassage = "The Data is shown for " + minTimeLevel + "  '" + startRange + "'";
            if (minTimeLevel.equalsIgnoreCase("Day")) {
                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(colViewByElement.trim()).toString() + "'  and t_date=to_date(to_char('" + startRange + "'),'mm-dd-yy') " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
                dataQuery2 = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(viewByElement.trim()).toString() + "'  and t_date=to_date(to_char('" + startRange + "'),'mm-dd-yy') " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
            } //dataQuery ="select "+elementCols.get(viewByElement.trim()).toString()+",sum("+measureName.trim()+"),"+elementCols.get(colViewByElement.trim()).toString()+" from "+targetTable.trim()+" where target_id='"+targetId+"' and viewby='"+viewByNames.get(colViewByElement.trim()).toString()+"' "+allParametrsFilterClause+" and t_date=to_date(to_char('"+startRange+"'),'mm-dd-yy') group by "+elementCols.get(viewByElement.trim()).toString()+","+elementCols.get(colViewByElement.trim()).toString()+" order by "+elementCols.get(viewByElement.trim()).toString()+","+elementCols.get(colViewByElement.trim()).toString();
            else if (minTimeLevel.equalsIgnoreCase("Month")) {
                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(colViewByElement.trim()).toString() + "'  and t_month='" + startRange + "' " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
                dataQuery2 = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(viewByElement.trim()).toString() + "'  and t_month='" + startRange + "' " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
            } // dataQuery ="select "+elementCols.get(viewByElement.trim()).toString()+",sum("+measureName.trim()+"),"+elementCols.get(colViewByElement.trim()).toString()+" from "+targetTable.trim()+" where target_id='"+targetId+"' and viewby='"+viewByNames.get(colViewByElement.trim()).toString()+"'  and t_month='"+startRange+"' "+allParametrsFilterClause+" group by "+elementCols.get(viewByElement.trim()).toString()+","+elementCols.get(colViewByElement.trim()).toString()+" order by "+elementCols.get(viewByElement.trim()).toString()+","+elementCols.get(colViewByElement.trim()).toString();
            else if (minTimeLevel.equalsIgnoreCase("Quarter") || minTimeLevel.equalsIgnoreCase("Qtr")) {
                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(colViewByElement.trim()).toString() + "'  and t_qtr='" + startRange + "' " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
                dataQuery2 = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(viewByElement.trim()).toString() + "'  and t_qtr='" + startRange + "' " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();

            } // dataQuery ="select "+elementCols.get(viewByElement.trim()).toString()+",sum("+measureName.trim()+"),"+elementCols.get(colViewByElement.trim()).toString()+" from "+targetTable.trim()+" where target_id='"+targetId+"' and viewby='"+viewByNames.get(colViewByElement.trim()).toString()+"'  and t_qtr='"+startRange.substring(1)+"' "+allParametrsFilterClause+" group by "+elementCols.get(viewByElement.trim()).toString()+","+elementCols.get(colViewByElement.trim()).toString()+" order by "+elementCols.get(viewByElement.trim()).toString()+","+elementCols.get(colViewByElement.trim()).toString();
            else if (minTimeLevel.equalsIgnoreCase("Year")) {
                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(colViewByElement.trim()).toString() + "'  and t_year='" + startRange + "' " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
                dataQuery2 = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(viewByElement.trim()).toString() + "'  and t_year='" + startRange + "' " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();

            }

            // PbReturnObject allD = pbDb.execSelectSQL(dataQuery);
            ps = con.prepareStatement(dataQuery);
            rs = ps.executeQuery();
            PbReturnObject allD = new PbReturnObject(rs);

            ps = con.prepareStatement(dataQuery2);
            rs = ps.executeQuery();
            PbReturnObject allD2 = new PbReturnObject(rs);

            allV = new HashMap();
            newPv = "";
            oldPv = "";
            // loop throuth data and make hashMap for row and column headers
            for (int n = 0; n < allD.getRowCount(); n++) {
                oldPv = allD.getFieldValueString(n, 0);
                if (newPv.equalsIgnoreCase("")) {
                    newPv = oldPv;
                } else if (!newPv.trim().equalsIgnoreCase(oldPv.trim()) && newPv.length() > 0) {
                    allV.put(newPv, primH);
                    primH = new HashMap();
                    newPv = oldPv;
                }
                String dat = allD.getFieldValueString(n, 2);
                Long mValue = Long.parseLong(allD.getFieldValueString(n, 1));
                primH.put(dat, new Long(mValue));
                if (n == (allD.getRowCount() - 1)) {
                    allV.put(oldPv, primH);
                }
            }

            for (int n = 0; n < allD2.getRowCount(); n++) {
                oldPv = allD2.getFieldValueString(n, 0);
                if (newPv.equalsIgnoreCase("")) {
                    newPv = oldPv;
                } else if (!newPv.trim().equalsIgnoreCase(oldPv.trim()) && newPv.length() > 0) {
                    allV.put(newPv, primH);
                    primH = new HashMap();
                    newPv = oldPv;
                }
                String dat = allD2.getFieldValueString(n, 2);
                Long mValue = Long.parseLong(allD2.getFieldValueString(n, 1));
                primH.put(dat, new Long(mValue));
                if (n == (allD2.getRowCount() - 1)) {
                    allV.put(oldPv, primH);
                }
            }

            PbReturnObject time = new PbReturnObject();//pbDb.execSelectSQL(colQry);
            // paramVals = f1.getRowEdges(rowQry,currVal);
            time = f1.getRowEdgesRetObj(colQry, colCurrVal);

            // to get originalResult
            for (int x = 1; x < paramVals.size(); x++) {
                String pVal = paramVals.get(x).toString();
                HashMap dt = new HashMap();
                if (allV.containsKey(pVal)) {
                    dt = (HashMap) allV.get(pVal);
                }
                for (int y = 0; y < time.getRowCount(); y++) {
                    String timeV = time.getFieldValueString(y, 0);
                    long mV = 0;
                    if (dt.containsKey(timeV)) {
                        mV = ((Long) dt.get(timeV)).intValue();
                    }
                    String value = "";
                    if (mV != 0) {
//                        value = "" + mV + "";
                        value = String.valueOf(mV);
                    }
                    originalResult.put(paramVals.get(x) + ":" + timeV, new Long(mV));
                }
            }
            // to get the column and row total
            HashMap rowTotal = new HashMap();
            HashMap colTotal = new HashMap();
            String[] originalValsKeys = (String[]) (originalResult.keySet()).toArray(new String[0]);
            for (int m = 0; m < originalValsKeys.length; m++) {
                String totalKey = originalValsKeys[m];
                String rowColKey = "";
                if (originalResult.containsKey(totalKey)) {
                    rowColKey = totalKey;//(String)originalResult.get(totalKey);
                    long oriVal = 0;
                    oriVal = ((Long) originalResult.get(totalKey)).longValue();
                    String totKey[] = rowColKey.split(":");
                    String rowKey = "";
                    String colKey = "";
                    for (int n = 0; n < totKey.length; n++) {
                        if (n == 0) {
                            rowKey = totKey[n];
                        }
                        if (n == 1) {
                            colKey = totKey[n];
                        }
                    }
                    if (rowTotal.containsKey(rowKey)) {
                        long al = ((Long) rowTotal.get(rowKey)).longValue();
                        al = oriVal + al;
                        rowTotal.put(rowKey, new Long(al));
                    } else {
                        rowTotal.put(rowKey, new Long(oriVal));
                    }

                    if (colTotal.containsKey(colKey)) {
                        long colAl = ((Long) colTotal.get(colKey)).longValue();
                        colAl = oriVal + colAl;
                        colTotal.put(colKey, new Long(colAl));
                    } else {
                        colTotal.put(colKey, new Long(oriVal));
                    }

                }
            }
            // to get the column and row total

            // to get the restricting total
            String colChildElement = "";
            colChildElement = collect.getChildElementId(colViewByElement, targetId);

            // to find the parent total
            String getParents = resundle.getString("getParents");
            Object tarObP[] = new Object[2];
            tarObP[0] = targetId;
            tarObP[1] = colViewByElement;
            String parentEle2 = "";

            String getParentElementId = resundle.getString("getParentElementId");
            Object tarObCol[] = new Object[2];
            tarObCol[0] = targetId;
            tarObCol[1] = colViewByElement;
            String fingetParentElementId = pbDb.buildQuery(getParentElementId, tarObCol);
            PbReturnObject allElementInfoOb = pbDb.execSelectSQL(fingetParentElementId);
            String parentEleId = "-1";
            for (int g = 0; g < allElementInfoOb.getRowCount(); g++) {
                int previous = g - 1;
                String localElementId = allElementInfoOb.getFieldValueString(g, "ELEMENT_ID");
                if (colViewByElement.equalsIgnoreCase(localElementId)) {
                    if (previous >= 0) {
                        parentEleId = allElementInfoOb.getFieldValueString(previous, "ELEMENT_ID");
                    }
                }
            }
            String parentBussCol = "";
            if (elementCols.containsKey(parentEleId)) {
                parentBussCol = (String) elementCols.get(parentEleId);
                parentBussCol = "," + parentBussCol;
            }
            ArrayList bussTable = new ArrayList();
            // to get the business table ids
            for (int g = 0; g < allElementInfoOb.getRowCount(); g++) {
                String localElementId = allElementInfoOb.getFieldValueString(g, "ELEMENT_ID");
                if (colViewByElement.equalsIgnoreCase(localElementId)) {
                    bussTable.add(allElementInfoOb.getFieldValueString(g, "BUSS_TABLE"));
                }
                if (parentEleId.equalsIgnoreCase(localElementId)) {
                    bussTable.add(allElementInfoOb.getFieldValueString(g, "BUSS_TABLE"));
                }
            }

            // to make the parent query for tables
            String startQuery = "select distinct " + elementCols.get(colViewByElement.trim()).toString() + " " + parentBussCol;
            String middleClause = "";
            String endClause = "";
            String parentQuery = "";
            BusinessGroupDAO bgDao = new BusinessGroupDAO();
            middleClause = bgDao.viewBussDataWithouCol(bussTable);
            parentQuery = startQuery + " " + middleClause;
            f1.elementId = colViewByElement;
            PbReturnObject parentDataOb = f1.getParentData(parentQuery);
            String getTargetPrimParam = resundle.getString("getTargetPrimParam");
            Object tarObCol2[] = new Object[1];
            tarObCol2[0] = targetId;
            String fingetTargetPrimParam = pbDb.buildQuery(getTargetPrimParam, tarObCol2);
            PbReturnObject targetInfoOb = pbDb.execSelectSQL(fingetTargetPrimParam);

            primParamEleId = targetInfoOb.getFieldValueString(0, "TARGET_PRIM_PARAM");
            String getAllElementsInfo = resundle.getString("getAllElementsInfo");
            String fingetAllElementsInfo = pbDb.buildQuery(getAllElementsInfo, tarObCol2);
            PbReturnObject targetElementsInfoOb = pbDb.execSelectSQL(fingetAllElementsInfo);
            HashMap elementDet = new HashMap();
            int localDim = targetElementsInfoOb.getFieldValueInt(0, "ELEMENT_ID");
            int dimId = localDim;
            String parentId = "-1";

            HashMap parentsMap = new HashMap();

            for (int m = 0; m < targetElementsInfoOb.getRowCount(); m++) {
                parentsMap.put(targetElementsInfoOb.getFieldValueString(m, "CHILD_ELEMENT_ID"), targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"));
            }
            for (int m = 0; m < targetElementsInfoOb.getRowCount(); m++) {
                ArrayList det = new ArrayList();
                det.add(0, targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"));
                parentId = "-1";
                if (parentsMap.containsKey(targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"))) {
                    parentId = parentsMap.get(targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID")).toString();
                }
                //parentId=targetElementsInfoOb.getFieldValueString(m,"PARENT_ID");
                if (parentId.equalsIgnoreCase("")) {
                    parentId = "-1";
                }
                det.add(1, parentId);
                det.add(2, targetElementsInfoOb.getFieldValueString(m, "PARAM_DISP_NAME"));
                elementDet.put(targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"), det);
            }

            parentBussCol = "";
            HashMap parentTotals = new HashMap();
            String parentDispName = "";
            String parentEle = "";
            parentDataQ = "";
            parentDataQ2 = "";
            String parentDispName2 = "";

            //parentEle2=collect.getChildElementId(colViewByElement,targetId);
            if (elementDet.containsKey(viewByElement)) {
                ArrayList eleDet = new ArrayList();
                eleDet = (ArrayList) elementDet.get(viewByElement);
                parentEle = eleDet.get(1).toString();
                parentDispName = eleDet.get(2).toString();
            }
            if (elementDet.containsKey(colViewByElement)) {
                ArrayList eleDet = new ArrayList();
                eleDet = (ArrayList) elementDet.get(colViewByElement);
                parentEle2 = eleDet.get(1).toString();
                parentDispName2 = eleDet.get(2).toString();
            }
            if (elementCols.containsKey(parentEle)) {
                parentBussCol = elementCols.get(parentEle).toString();
            }

            // to put the parent filters on columns
            String parentParamFilter = "";
            String[] reqParams = (String[]) (targetParametersValues.keySet()).toArray(new String[0]);
            for (int o = 0; o < reqParams.length; o++) {
                String key = reqParams[o];
                String value = "-1";
                if (key.equalsIgnoreCase(parentEle)) {
                    if (targetParametersValues.containsKey(key)) {
                        String val = targetParametersValues.get(key).toString();
                        if (!val.equalsIgnoreCase("All")) {
                            parentParamFilter = " and " + elementCols.get(key).toString() + "='" + val + "'";
                        }
                        if (val.equalsIgnoreCase("All")) {
                            if (errorMessage.length() == 0) {
                                errorMessage = "As " + viewByNames.get(parentEle.trim()).toString() + " is not selected for " + viewByNames.get(viewByElement.trim()).toString() + ".So no data entry is allowed.";
                            }
                        }
                    }
                }
            }

            String parentParamFilter2 = "";
            for (int o = 0; o < reqParams.length; o++) {
                String key = reqParams[o];
                String value = "-1";
                if (key.equalsIgnoreCase(parentEle2)) {
                    if (targetParametersValues.containsKey(key)) {
                        String val = targetParametersValues.get(key).toString();
                        if (!val.equalsIgnoreCase("All")) {
                            parentParamFilter2 = " and " + elementCols.get(key).toString() + "='" + val + "'";
                        }
                        if (val.equalsIgnoreCase("All")) {
                            //if(errorMessage.length()==0)
                            //errorMessage = "As "+viewByNames.get(parentEle.trim()).toString()+" is not selected for "+viewByNames.get(viewByElement.trim()).toString()+".So no data entry is allowed.";
                        }
                    }
                }
            }

            if (minTimeLevel.equalsIgnoreCase("Month")) {
                if (parentEle.trim().equalsIgnoreCase("-1")) {
                    parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby is null and  t_month='" + startRange + "' " + parentParamFilter + " group by " + elementCols.get(viewByElement.trim()).toString() + ",t_month order by " + elementCols.get(viewByElement.trim()).toString() + ",t_month";
                } else {
                    parentDataQ = "select " + elementCols.get(parentEle.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()) + " from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' and secviewby is null " + parentParamFilter + " and t_month='" + startRange + "' group by " + elementCols.get(parentEle.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString();
                }

                if (parentEle2.trim().equalsIgnoreCase("-1")) {
                    parentDataQ2 = "select " + elementCols.get(colViewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and  t_month='" + startRange + "' and secviewby is null " + parentParamFilter + " group by " + elementCols.get(colViewByElement.trim()).toString() + ",t_month order by " + elementCols.get(colViewByElement.trim()).toString() + ",t_month";
                } else {
                    parentDataQ2 = "select " + elementCols.get(parentEle2.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(parentEle2.trim()).toString() + " from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle2.trim()).toString() + "' " + parentParamFilter + " and t_month='" + startRange + "' and secviewby='" + viewByNames.get(parentEle2).toString() + "' group by " + elementCols.get(colViewByElement.trim()).toString() + "," + elementCols.get(parentEle2.trim()).toString() + " order by " + elementCols.get(colViewByElement.trim()).toString() + "," + elementCols.get(parentEle2.trim()).toString();
                }
            } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                if (parentEle.trim().equalsIgnoreCase("-1")) {
                    parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby is null and  t_date=to_char('" + startRange + "','dd-mm-yy') " + parentParamFilter + " group by " + elementCols.get(viewByElement.trim()).toString() + ",to_char(t_date,'dd-mm-yy') order by " + elementCols.get(viewByElement.trim()).toString() + ",to_char(t_date,'dd-mm-yy')";
                } else {
                    parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(parentEle.trim()).toString() + " from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' and secviewby is null " + parentParamFilter + " and t_date=to_char('" + startRange + "','dd-mm-yy') group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString();
                }

                if (parentEle2.trim().equalsIgnoreCase("-1")) {
                    parentDataQ2 = "select " + elementCols.get(colViewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and  t_date=to_char('" + startRange + "','dd-mm-yy') and secviewby is null " + parentParamFilter + " group by " + elementCols.get(colViewByElement.trim()).toString() + ",to_char(t_date,'dd-mm-yy') order by " + elementCols.get(colViewByElement.trim()).toString() + ",to_char(t_date,'dd-mm-yy')";
                } else {
                    parentDataQ2 = "select " + elementCols.get(parentEle2.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(parentEle2.trim()).toString() + " from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle2.trim()).toString() + "' " + parentParamFilter + " and t_date=to_char('" + startRange + "','dd-mm-yy') and secviewby='" + viewByNames.get(parentEle2).toString() + "' group by " + elementCols.get(colViewByElement.trim()).toString() + "," + elementCols.get(parentEle2.trim()).toString() + " order by " + elementCols.get(colViewByElement.trim()).toString() + "," + elementCols.get(parentEle2.trim()).toString();
                }

            } else if (minTimeLevel.equalsIgnoreCase("Quarter") || minTimeLevel.equalsIgnoreCase("Qtr")) {
                if (parentEle.trim().equalsIgnoreCase("-1")) {
                    parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby is null and  t_qtr='" + startRange + "' " + parentParamFilter + " group by " + elementCols.get(viewByElement.trim()).toString() + ",t_qtr order by " + elementCols.get(viewByElement.trim()).toString() + ",t_qtr";
                } else {
                    parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(parentEle.trim()).toString() + " from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' and secviewby is null " + parentParamFilter + " and t_qtr='" + startRange + "' group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString();
                }

                if (parentEle2.trim().equalsIgnoreCase("-1")) {
                    parentDataQ2 = "select " + elementCols.get(colViewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and  t_qtr='" + startRange + "' and secviewby is null " + parentParamFilter + " group by " + elementCols.get(colViewByElement.trim()).toString() + ",t_qtr order by " + elementCols.get(colViewByElement.trim()).toString() + ",t_qtr";
                } else {
                    parentDataQ2 = "select " + elementCols.get(parentEle2.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(parentEle2.trim()).toString() + " from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle2.trim()).toString() + "' " + parentParamFilter + " and t_qtr='" + startRange + "' and secviewby='" + viewByNames.get(parentEle2).toString() + "' group by " + elementCols.get(colViewByElement.trim()).toString() + "," + elementCols.get(parentEle2.trim()).toString() + " order by " + elementCols.get(colViewByElement.trim()).toString() + "," + elementCols.get(parentEle2.trim()).toString();
                }
            } else if (minTimeLevel.equalsIgnoreCase("Year")) {
                if (parentEle.trim().equalsIgnoreCase("-1")) {
                    parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby is null and  t_year='" + startRange + "' " + parentParamFilter + " group by " + elementCols.get(viewByElement.trim()).toString() + ",t_year order by " + elementCols.get(viewByElement.trim()).toString() + ",t_year";
                    if (!parentEle2.trim().equalsIgnoreCase("-1")) {
                        parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(parentEle2.trim()).toString() + "' and  t_year='" + startRange + "' " + parentParamFilter + parentParamFilter2 + " group by " + elementCols.get(viewByElement.trim()).toString() + ",t_year order by " + elementCols.get(viewByElement.trim()).toString() + ",t_year";
                    }
                } else {
                    parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ") from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' " + parentParamFilter + parentParamFilter2 + " and t_year='" + startRange + "' group by " + elementCols.get(viewByElement.trim()).toString() + ",t_year order by " + elementCols.get(viewByElement.trim()).toString() + ",t_year";
                }

                if (parentEle2.trim().equalsIgnoreCase("-1")) {
                    parentDataQ2 = "select " + elementCols.get(colViewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and  t_year='" + startRange + "' and secviewby is null group by " + elementCols.get(colViewByElement.trim()).toString() + ",t_year order by " + elementCols.get(colViewByElement.trim()).toString() + ",t_year";
                } else {
                    parentDataQ2 = "select " + elementCols.get(parentEle2.trim()).toString() + ",sum(" + measureName.trim() + ") ,t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle2.trim()).toString() + "' " + parentParamFilter + parentParamFilter2 + " and t_year='" + startRange + "'  group by " + elementCols.get(parentEle2.trim()).toString() + ",t_year order by " + elementCols.get(parentEle2.trim()).toString() + ",t_year";
                }

                // else if(parentEle.equalsIgnoreCase("-1"))//if(!parentEle.equalsIgnoreCase("-1"))
            }
            // PbReturnObject parentData=pbDb.execSelectSQL(parentDataQ);           

            psP = con.prepareStatement(parentDataQ);
            rsP = psP.executeQuery();
            PbReturnObject parentData = new PbReturnObject(rsP);
            parentTotals = new HashMap();
            for (int t = 0; t < parentData.getRowCount(); t++) {
                parentTotals.put(parentData.getFieldValueString(t, 0), new Long(Long.parseLong(parentData.getFieldValueString(t, 1))));
            }

            psP = con.prepareStatement(parentDataQ2);
            rsP = psP.executeQuery();
            parentData = new PbReturnObject(rsP);

            for (int t = 0; t < parentData.getRowCount(); t++) {
                parentTotals.put(parentData.getFieldValueString(t, 0), new Long(Long.parseLong(parentData.getFieldValueString(t, 1))));
            }
            ////////////////////////////.println(" parentTotals --=.,., "+parentTotals);
            // to get the restricting total over

            // to display the data
            //to display header
            //colChildElement = collect.getChildElementId(colChildElement,targetId);
//             result = result + "<Tr><Td style=\"width:200px;font-weight:bold;font-size:11px;background-color:#E6E6E6\">" + currVal + "</Td>";
            result.append("<Tr><Td style=\"width:200px;font-weight:bold;font-size:11px;background-color:#E6E6E6\">").append(currVal).append("</Td>");
            if (!colChildElement.equalsIgnoreCase("-1")) {
                for (int z = 0; z < colEdgeVals.size(); z++) {
//                    result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">";
//                    result += "<a href=\"javascript:submitColumnDrillUrl('" + colDrillUrl + (String) colEdgeVals.get(z) + "')\">" + (String) colEdgeVals.get(z)+" </Td>";
                    result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">");
                    result.append("<a href=\"javascript:submitColumnDrillUrl('").append(colDrillUrl).append((String) colEdgeVals.get(z)).append("')\">").append((String) colEdgeVals.get(z)).append(" </Td>");

                }
            } else if (colChildElement.equalsIgnoreCase("-1")) {
                for (int z = 0; z < colEdgeVals.size(); z++) {
//                    result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">";
//                    result += (String) colEdgeVals.get(z)+"</Td>";
                    result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">");
                    result.append((String) colEdgeVals.get(z)).append("</Td>");

                }
            }
            // result = result+"<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Row Total</Td>";
//            result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Restricting Total</Td>";
            result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Restricting Total</Td>");
            // result = result+"<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Difference</Td>";
            result.append("</Tr>");
//            result = result + "</Tr>";

            // display the data table
            disableSaveButtonFlag = true;
            HashMap Combo = new HashMap();
            for (int x = 1; x < paramVals.size(); x++) {
                String pVal = paramVals.get(x).toString();
                long parentTotal = 0;
                if (parentTotals.containsKey(pVal)) {
                    parentTotal = ((Long) parentTotals.get(pVal)).longValue();
                }
                if (disableSaveButtonFlag == false) {
                    if (parentTotal != 0) {
                        disableSaveButtonFlag = true;
                    }
                }

                long rowTotalVal = 0;
                String childElement = collect.getChildElementId(viewByElement, targetId);
                if (rowTotal.containsKey(pVal)) {
                    rowTotalVal = ((Long) rowTotal.get(pVal)).longValue();
                }
//                result = result + "<Tr>" + "<Td>";
                result.append("<Tr><Td>");
                if (!childElement.equalsIgnoreCase("-1")) {
//                    result = result + "<a href=\"javascript:submitDrill('" + drllUrl + paramVals.get(x).toString() + "')\">" + paramVals.get(x).toString();
                    result.append("<a href=\"javascript:submitDrill('").append(drllUrl).append(paramVals.get(x).toString()).append("')\">").append(paramVals.get(x).toString());
                    //result=result+paramVals.get(x).toString();
                } else {
//                    result = result + paramVals.get(x).toString();
                    result.append(paramVals.get(x).toString());
                }
//                result = result + "</Td>";
                result.append("</Td>");

                HashMap dt = new HashMap();
                if (allV.containsKey(pVal)) {
                    dt = (HashMap) allV.get(pVal);
                }
                for (int y = 0; y < colEdgeVals.size(); y++) {
                    String timeV = colEdgeVals.get(y).toString();
                    long mV = 0;
                    if (dt.containsKey(timeV)) {
                        mV = ((Long) dt.get(timeV)).longValue();
                    }
                    String value = "";
                    if (mV != 0) {
//                        value = "" + mV + "";
                        value = String.valueOf(mV);
                    }
//                    result = result + "<Td><input style=\"width:99%\" onkeypress=\"return isNumberKey(event)\" type=\"text\" id=\"" + paramVals.get(x) + ":" + timeV + "\" name=\"" + paramVals.get(x) + ":" + timeV + "\" value=\"" + value + "\"></Td>";
                    result.append("<Td><input style=\"width:99%\" onkeypress=\"return isNumberKey(event)\" type=\"text\" id=\"").append(paramVals.get(x)).append(":").append(timeV).append("\" name=\"").append(paramVals.get(x)).append(":").append(timeV).append("\" value=\"").append(value).append("\"></Td>");

                }
                // result = result+"<Td><input type=\"text\" readonly class=\"displayTotal\" value=\""+rowTotalVal+"\"></Td>" +
//                result = result + "<Td><input style=\"width:99%\" type=\"text\" readonly value=\"" + parentTotal + "\"></Td>";
                result.append("<Td><input style=\"width:99%\" type=\"text\" readonly value=\"").append(parentTotal).append("\"></Td>");
                //         "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>";
//                result = result + "</Tr>";
                result.append("</Tr>");
            }

            // result = result+"<Tr><Td style=\"width:200px\">Column Total</Td>";
//            result = result + "<Tr><Td style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Restricting Total</Td>";
            result.append("<Tr><Td style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Restricting Total</Td>");
            long gTotal = 0;

            for (int p = 0; p < colEdgeVals.size(); p++) {
                String colKey = colEdgeVals.get(p).toString();
                long colTotVal = 0;
                if (parentTotals.containsKey(colKey)) {
                    colTotVal = ((Long) parentTotals.get(colKey)).longValue();
                }
//                result = result + "<Td><input type=\"text\" style=\"width:99%\" name='2' readonly value=\"" + colTotVal + "\"></Td>";
                result.append("<Td><input type=\"text\" style=\"width:99%\" name='2' readonly value=\"").append(colTotVal).append("\"></Td>");
                gTotal = gTotal + colTotVal;
            }
//            result = result + "<Td style=\"width:200px\"><input style=\"width:99%\" type=\"text\" readonly value=" + gTotal + "></Td>";
            result.append("<Td style=\"width:200px\"><input style=\"width:99%\" type=\"text\" readonly value=").append(gTotal).append("></Td>");
            //  result=result+"<Td style=\"width:200px\"><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>";
            //  result=result+"<Td style=\"width:200px\"><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>";
//            result = result + "</Tr>";
            result.append("</Tr>");
            // to show the overall target
            //primParamEleId
//            String primParamFilter = "";
//            String overAllTotQ = "";
//            // to put the primary paramteres hierarchy filters
// Commented By Prabal As Not usable
//            for (int o = 0; o < reqParams.length; o++) {
//                String key = reqParams[o];
//                String value = "-1";
//                if (key.equalsIgnoreCase(primParamEleId)) {
//                    if (targetParametersValues.containsKey(key)) {
//                        String val = targetParametersValues.get(key).toString();
//                        if (!val.equalsIgnoreCase("All")) {
//                            primParamFilter = " and " + elementCols.get(key).toString() + "='" + val + "'";
//                        }
//
//                    }
//                }
//            }
//            if (minTimeLevel.equalsIgnoreCase("Day")) // overAllTotQ = "Select "+elementCols.get(primParamEleId.trim()).toString()+",sum("+measureName.trim()+"),to_char(t_date,'dd-mm-yy') from "+targetTable.trim()+" where target_id="+targetId+" and viewby='"+viewByNames.get(primParamEleId.trim()).toString()+"' "+primParamFilter+" group by "+elementCols.get(primParamEleId.trim()).toString()+",to_char(t_date,'dd-mm-yy') order by "+elementCols.get(primParamEleId.trim()).toString()+",to_char(t_date,'dd-mm-yy')";
//            {
//                overAllTotQ = "Select sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' " + primParamFilter + " group by to_char(t_date,'dd-mm-yy') order by to_char(t_date,'dd-mm-yy')";
//            } else if (minTimeLevel.equalsIgnoreCase("Month")) {
//                overAllTotQ = "Select sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' " + primParamFilter + " group by t_month order by t_month";
//            } else if (minTimeLevel.equalsIgnoreCase("Quarter") || minTimeLevel.equalsIgnoreCase("Qtr")) {
//                overAllTotQ = "Select sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' " + primParamFilter + " group by t_qtr order by t_qtr";
//            } else if (minTimeLevel.equalsIgnoreCase("Year")) {
//                overAllTotQ = "Select sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' " + primParamFilter + " group by t_year order by t_year";
//            }
//
//            // PbReturnObject overAllObj = pbDb.execSelectSQL(overAllTotQ);
//            RestrictingTotal = parentTotals;
//            psO = con.prepareStatement(overAllTotQ);
            // rsO=psO.executeQuery();
            PbReturnObject overAllObj = new PbReturnObject();

            primH2 = new HashMap();
            for (int i = 0; i < overAllObj.getRowCount(); i++) {
                oldPrimV = overAllObj.getFieldValueString(i, 0);
                if (newPrimV.equalsIgnoreCase("")) {
                    newPrimV = oldPrimV;
                }
                oldPrimV = overAllObj.getFieldValueString(i, 0);
                String dat = overAllObj.getFieldValueString(i, 0);
                int mValue = overAllObj.getFieldValueInt(i, 1);
                int old = 0;
                if (primH2.containsKey(dat)) {
                    old = ((Integer) primH2.get(dat)).intValue();
                }
                mValue = mValue + old;
                primH2.put(dat, Integer.valueOf(mValue));
            }
            int grandTotal = 0;
            for (int k = 0; k < paramVals.size(); k++) {
                int rTotal = 0;
                if (primH2.containsKey(paramVals.get(k).toString())) {
                    rTotal = ((Integer) primH2.get(paramVals.get(k).toString())).intValue();
                }
                grandTotal = grandTotal + rTotal;
            }

            // overAllMessage=" The Overall Target Set By Primary Parameter '"+viewByNames.get(primParamEleId.trim()).toString()+"' is "+grandTotal;
            secAnalyze = colViewByElement;

        }

        if (secAnalyze.equalsIgnoreCase("Time")) {
            if (showRestrictingTotal == true) {
                if (disableSaveButtonFlag == false) {
                    freezeButton = "disabled";
                }
            }
        } else if (disableSaveButtonFlag == false) {
            freezeButton = "disabled";
        }

        if (errorMessage.length() > 5) {
            freezeButton = "disabled";
        }
        String periodMsg = "";
        if (displayPeriodFlagMessage == true) {
            periodMsg = "As no data is available at '" + minTimeLevel + "'. So no data entry is allowed at '" + periodType + "'. Only modification is allowed";
        }

        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (psP != null) {
            psP.close();
        }
        if (rsP != null) {
            rsP.close();
        }
        if (psO != null) {
            psO.close();
        }
        if (rsO != null) {
            rsO.close();
        }
        if (con != null) {
            con.close();
        }
        totalResult.put("displayDataRegion", result);
        totalResult.put("rowEdgeValues", paramVals);
        totalResult.put("colEdgeValues", colEdgeVals);
        totalResult.put("originalResult", originalResult);
        totalResult.put("overAllMessage", overAllMessage);
        totalResult.put("errorMessage", errorMessage);
        totalResult.put("secAnalyze", secAnalyze);
        totalResult.put("primaryAnalyze", primaryAnalyze);
        totalResult.put("startRange", startRange);
        totalResult.put("primParamEleId", primParamEleId);
        totalResult.put("periodType", periodType);
        totalResult.put("minTimeLevel", minTimeLevel);
        totalResult.put("endRange", endRange);
        totalResult.put("endPeriod", endPeriod);
        totalResult.put("startPeriod", startPeriod);
        totalResult.put("dateMeassage", dateMeassage);
        totalResult.put("measureName", measureName);
        totalResult.put("freezeButton", freezeButton);//periodMsg
        totalResult.put("periodMsg", periodMsg);
        totalResult.put("targetStartDate", targetStartDate);
        totalResult.put("targetEndDate", targetEndDate);
        totalResult.put("colDrillUrl", colDrillUrl);
        totalResult.put("basisVal", basisVal);
        totalResult.put("RestrictingTotal", RestrictingTotal);
        totalResult.put("maxTimeLevel", maxTimeLevel);
        totalResult.put("dataQuery", dataQuery);
        totalResult.put("dataQuery2", dataQuery2);
        totalResult.put("parentDataQ", parentDataQ);
        totalResult.put("parentDataQ2", parentDataQ2);

        return totalResult;
    }

    public HashMap displayDataRegionForView(HashMap viewBy, ArrayList timeDetails, HashMap targetParametersValues, String drllUrl, String colDrillUrl, HashMap targetParameters, ArrayList basisValues) throws Exception {
        String freezeButton = "";

        HashMap RestrictingTotal = new HashMap();
        String basisVal = basisValues.get(0).toString();
        HashMap paramValues = collect.targetParametersValues;
        Connection con = ProgenConnection.getInstance().getConnection();
        PreparedStatement ps, psP, psO;
        ResultSet rs, rsP, rsO;
        rs = null;
        ps = null;
        psP = null;
        rsP = null;
        ps = null;
        psO = null;
        rsO = null;
        String startRange = "";
        String endRange = "";
        String minTimeLevel = "";
        startRange = timeDetails.get(2).toString();
        endRange = timeDetails.get(3).toString();
        minTimeLevel = timeDetails.get(0).toString();
        String endPeriod = "";
        String startPeriod = "";
//        String result = "";
        StringBuilder result = new StringBuilder();
        HashMap totalResult = new HashMap();
        HashMap originalResult = new HashMap();
        String rowQry = "";
        String colQry = "";
        String secAnalyze = "";
        String primaryAnalyze = "";
        String primParamEleId = "";
        String dateMeassage = "";
        boolean disableSaveButtonFlag = false;
        boolean showRestrictingTotal = true;
        ArrayList viewByParentsList = new ArrayList();
        boolean periodFlag = false;
        boolean displayPeriodFlagMessage = false;
        String targetStartDate = "";
        String targetEndDate = "";

        boolean showRowTotal = true;
        boolean showColTotal = true;
        boolean overAllFlag = false;
        String targetId = tarId;

        String timeLevelQ = "select level_id,level_name from targettimelevels where target_id=" + targetId + " order by level_id";
        PbReturnObject timeObj = pbDb.execSelectSQL(timeLevelQ);
        String maxTimeLevel = timeObj.getFieldValueString(0, "LEVEL_NAME");

        rowQry = RowEdgeQuery(viewBy);
        colQry = ColumnEdgeQuery(viewBy, timeDetails);

        String targetTimeQ = resundle.getString("targetTimeQ");
        Object tObj[] = new Object[1];
        tObj[0] = targetId;
        String fintargetTimeQ = pbDb.buildQuery(targetTimeQ, tObj);
        PbReturnObject TargetTimeOb = pbDb.execSelectSQL(fintargetTimeQ);
        if (minTimeLevel.equalsIgnoreCase("Day")) {
            targetStartDate = TargetTimeOb.getFieldValueString(0, "START_DATE");
            targetEndDate = TargetTimeOb.getFieldValueString(0, "END_DATE");
        } else if (minTimeLevel.equalsIgnoreCase("Month")) {
            targetStartDate = TargetTimeOb.getFieldValueString(0, "TARGET_START_MONTH");
            targetEndDate = TargetTimeOb.getFieldValueString(0, "TARGET_END_MONTH");
        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
            targetStartDate = TargetTimeOb.getFieldValueString(0, "TARGET_START_QTR");
            targetEndDate = TargetTimeOb.getFieldValueString(0, "TARGET_END_QTR");
        } else if (minTimeLevel.equalsIgnoreCase("Year")) {
            targetStartDate = TargetTimeOb.getFieldValueString(0, "TARGET_START_YEAR");
            targetEndDate = TargetTimeOb.getFieldValueString(0, "TARGET_END_YEAR");
        }

        // to get the business column names and display names for the element ids
        String getBussColNames = resundle.getString("getBussColNames");
        String getDispNames = resundle.getString("getDispNames");

        Object tarOb[] = new Object[1];
        tarOb[0] = targetId;
        String fingetBussColNames = pbDb.buildQuery(getBussColNames, tarOb);
        PbReturnObject pbro = pbDb.execSelectSQL(fingetBussColNames);
        HashMap elementCols = new HashMap();
        HashMap viewByNames = new HashMap();

        String fingetDispNames = pbDb.buildQuery(getDispNames, tarOb);
        PbReturnObject pbroDispNames = pbDb.execSelectSQL(fingetDispNames);

        // store the business column names for element ids
        for (int i = 0; i < pbro.getRowCount(); i++) {
            elementCols.put(pbro.getFieldValueString(i, "ELEMENT_ID"), pbro.getFieldValueString(i, "BUSS_COL_NAME"));
            // viewByNames.put(pbro.getFieldValueString(i,"ELEMENT_ID"),pbro.getFieldValueString(i,"PARAM_DISP_NAME"));
        }
        for (int m = 0; m < pbroDispNames.getRowCount(); m++) {
            viewByNames.put(pbroDispNames.getFieldValueString(m, "ELEMENT_ID"), pbroDispNames.getFieldValueString(m, "PARAM_DISP_NAME"));
        }
        // to get the target table and measure name
        String getTargetTable = resundle.getString("getTargetTable");
        String fingetTargetTable = pbDb.buildQuery(getTargetTable, tarOb);
        PbReturnObject pbroTab = pbDb.execSelectSQL(fingetTargetTable);
        String measureName2 = pbroTab.getFieldValueString(0, "MEASURE_NAME");
        String measureName = measureName2.trim().replace(" ", "_");
        String targetTable = pbroTab.getFieldValueString(0, "BUSS_TABLE_NAME");

        String[] a1 = (String[]) (viewBy.keySet()).toArray(new String[0]);
        //String columnEdgeValues = "";
        String rowViewByElement = "";
        String viewByElement = "";
        String colViewByElement = "";
        String colCurrVal = "";
        String periodType = "";
        String minimumTimeLevel = "";
        String currVal = "";
        String dataQuery = "";
        String overAllMessage = "";
        String errorMessage = "";
        String childperiodType = "";

        // set the primary anakyse by and secondary analyse by element ids and display names
        for (int i = 0; i < a1.length; i++) {
            ArrayList a = (ArrayList) viewBy.get(a1[i]);
            if (a.get(0).toString().equalsIgnoreCase("CBOVIEW_BY_NAME")) {
                viewByElement = a.get(3).toString();
                ////////////////////////////.println(" a.... "+a);
                if (!a.get(3).toString().equalsIgnoreCase("0")) {
                    currVal = a.get(4).toString();
                } else {
                    currVal = "Overall Target";
                }
            }

            if (a.get(0).toString().equalsIgnoreCase("CBOVIEW_BY_NAME1")) {
                if (a.get(4).toString().equalsIgnoreCase("Time")) {
                    periodType = (String) timeDetails.get(4);
                    minimumTimeLevel = (String) timeDetails.get(0);
                    colViewByElement = a.get(3).toString();
                    colCurrVal = a.get(4).toString();
                } else {
                    periodType = (String) timeDetails.get(4);
                    minimumTimeLevel = (String) timeDetails.get(0);
                    colViewByElement = a.get(3).toString();
                    colCurrVal = a.get(4).toString();
                }
            }
            if (a.get(0).toString().equalsIgnoreCase("CBOVIEW_BY_NAME2")) {
                basisVal = a.get(3).toString();
            }

        }
        rowViewByElement = viewByElement;
        // flag to show message if no data is available at minimum time level
        if (maxTimeLevel.equalsIgnoreCase(periodType)) {
            showRowTotal = false;
            showColTotal = false;
            overAllFlag = true;
        }

        if (!periodType.equalsIgnoreCase(minimumTimeLevel)) {
            periodFlag = true;
        }
        HashMap AllV = new HashMap();
        HashMap allVa = new HashMap();
        HashMap primH2 = new HashMap();
        String oldPrimV = "";
        String newPrimV = "";
        ArrayList paramVals = new ArrayList();

        ArrayList colEdgeVals = new ArrayList();
        primaryAnalyze = viewByElement.trim();
        f1.elementId = viewByElement.trim();
        if (!currVal.equalsIgnoreCase("Overall Target")) {
            paramVals = f1.getRowEdges(rowQry, currVal);
        }
        HashMap allV = new HashMap();
        String oldPv = "";
        String newPv = "";
        HashMap primH = new HashMap();
        //self row clause
        String rowCurrVal = "";
        HashMap allParamHashMap = targetParametersValues;
//        String allParametrsFilterClause = "";
        StringBuilder allParametrsFilterClause = new StringBuilder();
        String[] keys = (String[]) (allParamHashMap.keySet()).toArray(new String[0]);
        String a = "";
        // paramVals = new ArrayList();

        // on the basis of request parameters get the row header values
        for (int i = 0; i < keys.length; i++) {
            a = (String) allParamHashMap.get(keys[i]);

            if (rowViewByElement.equalsIgnoreCase(keys[i]) && a.equalsIgnoreCase("All")) {
                String primQuery = getParameterRowQuery(targetParameters, targetId, viewByElement);
                if (!currVal.equalsIgnoreCase("Overall Target")) {
                    paramVals = f1.getRowEdges(primQuery, currVal);
                }

            } else if (rowViewByElement.equalsIgnoreCase(keys[i]) && !(a.equalsIgnoreCase("All"))) {
                paramVals = new ArrayList();
                if (!paramVals.contains(currVal)) {
                    paramVals.add(currVal);
                    paramVals.add(a);
                }
            }
            if (!a.equalsIgnoreCase("All")) {
//                allParametrsFilterClause = allParametrsFilterClause + " and " + elementCols.get(keys[i]) + "='" + a + "'";
                allParametrsFilterClause.append(" and ").append(elementCols.get(keys[i])).append("='").append(a).append("'");
            }
        }

        PbReturnObject secViewBy = new PbReturnObject();
        boolean flagToDisplayCrossTab = false;

        // on the basis of request parameters get the column header values
        for (int i = 0; i < a1.length; i++) {
            ArrayList arr = (ArrayList) viewBy.get(a1[i]);
            if (arr.get(0).toString().equalsIgnoreCase("CBOVIEW_BY_NAME1")) {
                if (arr.get(4).toString().equalsIgnoreCase("Time")) {
                    secViewBy = pbDb.execSelectSQL(colQry);
                    colEdgeVals = getArrayListFromPbReturnObject(secViewBy);

                } else {
                    secViewBy = f1.getColumnEdges(colQry);
                    secViewBy = getParameterColQuery(targetParameters, targetId, colViewByElement);
                    colEdgeVals = getArrayListFromPbReturnObject(secViewBy);//secViewBy);
                    colEdgeVals = new ArrayList();
                    //self column clause
                    for (int g = 0; g < keys.length; g++) {
                        a = (String) allParamHashMap.get(keys[g]);
                        if (colViewByElement.equalsIgnoreCase(keys[g]) && a.equalsIgnoreCase("All")) {
                            colEdgeVals = getArrayListFromPbReturnObject(secViewBy);
                        } else if (colViewByElement.equalsIgnoreCase(keys[g]) && !(a.equalsIgnoreCase("All"))) {
                            colEdgeVals.add(a);
                        }
                    }
                    flagToDisplayCrossTab = true;
                }
            }
        }

        boolean flagForRestrictingTotal = false;

        // when column header is time
        String overallT = "";
        if (elementCols.containsKey(viewByElement.trim())) {
            overallT = elementCols.get(viewByElement.trim()).toString();
        }
        String viewName = "";
        if (viewByNames.containsKey(viewByElement.trim())) {
            viewName = viewByNames.get(viewByElement.trim()).toString();
        }

        if (flagToDisplayCrossTab == false) {
            if (!viewByElement.equalsIgnoreCase("0")) {
                if (periodType.equalsIgnoreCase("Day")) {
                    dataQuery = "select " + overallT + ",sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewName + "' " + allParametrsFilterClause + " and secviewby is null group by " + overallT + ",to_char(t_date,'dd-mm-yy') order by " + overallT + ",to_char(t_date,'dd-mm-yy')";
                } else if (periodType.equalsIgnoreCase("Month")) {
                    dataQuery = "select " + overallT + ",sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewName + "' " + allParametrsFilterClause + " and secviewby is null group by " + overallT + ",t_month order by " + overallT + ",t_month";
                } //dataQuery ="select "+elementCols.get(viewByElement.trim()).toString()+",sum("+measureName.trim()+"),t_month from "+targetTable.trim()+" where target_id='"+targetId+"' and viewby='"+viewByNames.get(viewByElement.trim()).toString()+"'"+allParametrsFilterClause+" group by "+elementCols.get(viewByElement.trim()).toString()+",t_month order by "+elementCols.get(viewByElement.trim()).toString()+",t_month";
                else if (periodType.equalsIgnoreCase("Quarter") || periodType.equalsIgnoreCase("Qtr")) {
                    dataQuery = "select " + overallT + ",sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id='" + targetId + "' " + allParametrsFilterClause + " "
                            + " and viewby='" + viewName + "' and secviewby is null group by " + overallT + ", t_qtr order by " + overallT + ",t_qtr";
                } else if (periodType.equalsIgnoreCase("Year")) {
                    dataQuery = "select " + overallT + ",sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewName + "' " + allParametrsFilterClause + " and secviewby is null group by " + overallT + ",t_year order by " + overallT + ",t_year";
                }

            } else {
                if (viewByElement.equalsIgnoreCase("0")) {
                    paramVals = new ArrayList();
                    paramVals.add("OverAll Target");
                    paramVals.add("OverAll");
                }

                if (periodType.equalsIgnoreCase("Day")) {
                    dataQuery = "select sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='OverAll'  group by to_char(t_date,'dd-mm-yy') order by to_char(t_date,'dd-mm-yy')";
                } else if (periodType.equalsIgnoreCase("Month")) {
                    dataQuery = "select sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='OverAll'  group by t_month order by t_month";
                } else if (periodType.equalsIgnoreCase("Quarter") || periodType.equalsIgnoreCase("QTR")) {
                    dataQuery = "select sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='OverAll' group by t_qtr order by t_qtr";
                } else if (periodType.equalsIgnoreCase("Year")) {
                    dataQuery = "select sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='OverAll' group by t_year order by t_year";
                }

            }
            // PbReturnObject allD = pbDb.execSelectSQL(dataQuery);

            con = ProgenConnection.getInstance().getConnection();
            // if(elementCols.containsKey(viewByElement.trim()))
            // {
            ps = con.prepareStatement(dataQuery);
            rs = ps.executeQuery();
            //  }
            PbReturnObject allD = new PbReturnObject();
            if (rs != null) {
                allD = new PbReturnObject(rs);
            }

            // data obtained from the target table
            if (!viewByElement.equalsIgnoreCase("0")) {
                for (int n = 0; n < allD.getRowCount(); n++) {
                    oldPv = allD.getFieldValueString(n, 0);
                    if (newPv.equalsIgnoreCase("")) {
                        newPv = oldPv;
                    } else if (!newPv.trim().equalsIgnoreCase(oldPv.trim()) && newPv.length() > 0) {
                        allV.put(newPv, primH);
                        primH = new HashMap();
                        newPv = oldPv;
                    }
                    String dat = allD.getFieldValueString(n, 2);
                    long mValue = Long.parseLong(allD.getFieldValueString(n, 1));
                    primH.put(dat, new Long(mValue));
                    if (n == (allD.getRowCount() - 1)) {
                        allV.put(oldPv, primH);
                    }
                }
            } else {
                oldPv = "OverAll";
                primH = new HashMap();
                for (int n = 0; n < allD.getRowCount(); n++) {
                    if (newPv.equalsIgnoreCase("")) {
                        newPv = oldPv;
                    }

                    /*
                     * else
                     * if(!newPv.trim().equalsIgnoreCase(oldPv.trim())&&newPv.length()>0)
                     * { allV.put(newPv,primH); primH = new HashMap(); newPv =
                     * oldPv; }
                     */
                    String dat = allD.getFieldValueString(n, 1);
                    long mValue = Long.parseLong(allD.getFieldValueString(n, 0));
                    primH.put(dat, new Long(mValue));
                    allV.put(oldPv, primH);

                }
            }
            PbReturnObject time = pbDb.execSelectSQL(colQry);
            colEdgeVals = new ArrayList();

            // when column header is time
            for (int y = 0; y < time.getRowCount(); y++) {
                colEdgeVals.add(time.getFieldValueString(y, 0));
                if (y == 0) {
                    startPeriod = time.getFieldValueString(y, 0);
                }
                if (y == time.getRowCount() - 1) {
                    endPeriod = time.getFieldValueString(y, 0);
                }
            }

            // to get originalResult which is there in the database
            for (int x = 1; x < paramVals.size(); x++) {
                String pVal = paramVals.get(x).toString();
                HashMap dt = new HashMap();
                if (allV.containsKey(pVal)) {
                    dt = (HashMap) allV.get(pVal);
                }
                for (int y = 0; y < time.getRowCount(); y++) {
                    String timeV = time.getFieldValueString(y, 0);
                    long mV = 0;
                    if (dt.containsKey(timeV)) {
                        mV = ((Long) dt.get(timeV)).longValue();
                    }
                    String value = "";
                    if (mV != 0) {
//                        value = "" + mV + "";
                        value = String.valueOf(mV);
                    }
                    originalResult.put(paramVals.get(x) + ":" + timeV, new Long(mV));
                }
            }

            // to get the column and row total hashMaps
            HashMap rowTotal = new HashMap();
            HashMap colTotal = new HashMap();
            String[] originalValsKeys = (String[]) (originalResult.keySet()).toArray(new String[0]);
            for (int m = 0; m < originalValsKeys.length; m++) {
                String totalKey = originalValsKeys[m];
                String rowColKey = "";
                if (originalResult.containsKey(totalKey)) {
                    rowColKey = totalKey;//(String)originalResult.get(totalKey);
                    long oriVal = 0;
                    oriVal = ((Long) originalResult.get(totalKey)).longValue();
                    String totKey[] = rowColKey.split(":");
                    String rowKey = "";
                    String colKey = "";
                    for (int n = 0; n < totKey.length; n++) {
                        if (n == 0) {
                            rowKey = totKey[n];
                        }
                        if (n == 1) {
                            colKey = totKey[n];
                        }
                    }
                    if (rowTotal.containsKey(rowKey)) {
                        long al = ((Long) rowTotal.get(rowKey)).longValue();
                        al = oriVal + al;
                        rowTotal.put(rowKey, new Long(al));
                    } else {
                        rowTotal.put(rowKey, new Long(oriVal));
                    }

                    if (colTotal.containsKey(colKey)) {
                        long colAl = ((Long) colTotal.get(colKey)).longValue();
                        colAl = oriVal + colAl;
                        colTotal.put(colKey, new Long(colAl));
                    } else {
                        colTotal.put(colKey, new Long(oriVal));
                    }

                }
            }

            String childElement = collect.getChildElementId(viewByElement, targetId);

            //column Header display
            String pType = "";
            pType = periodType;
            //if(pType.e)

            childperiodType = collect.getChildTimeId(pType, targetId);
            if (childperiodType != null) {
                if (childperiodType.equalsIgnoreCase("")) {
                    childperiodType = null;
                }
            }
//            result = result + "<Tr><Td style=\"width:auto;font-weight:bold;font-size:11px;background-color:#E6E6E6\">" + currVal + "</Td>";
            result.append("<Tr><Td style=\"width:auto;font-weight:bold;font-size:11px;background-color:#E6E6E6\">").append(currVal).append("</Td>");
            String newColDrillUrl = colDrillUrl;
            for (int j = 0; j < time.getRowCount(); j++) {

                newColDrillUrl = colDrillUrl.replace("~", time.getFieldValueString(j, 0));
                if (viewByElement.equalsIgnoreCase("0")) {
                    if (overAllFlag == false) {
                        if (childperiodType != null) {
//                            result = result + "<Td align=\"center\" colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\"><a href=\"javascript:submitTimeDrill('" + newColDrillUrl + "')\">" + time.getFieldValueString(j, 0) + "</Td>";
                            result.append("<Td align=\"center\" colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\"><a href=\"javascript:submitTimeDrill('").append(newColDrillUrl).append("')\">").append(time.getFieldValueString(j, 0)).append("</Td>");
                        } else {
//                            result = result + "<Td align=\"center\" colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">" + time.getFieldValueString(j, 0) + "</Td>";
                            result.append("<Td align=\"center\" colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">").append(time.getFieldValueString(j, 0)).append("</Td>");
                        }
                    } else if (overAllFlag == true) {
                        if (childperiodType != null) {
//                            result = result + "<Td align=\"center\"  style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\"><a href=\"javascript:submitTimeDrill('" + newColDrillUrl + "')\">" + time.getFieldValueString(j, 0) + "</Td>";
                            result.append("<Td align=\"center\"  style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\"><a href=\"javascript:submitTimeDrill('").append(newColDrillUrl).append("')\">").append(time.getFieldValueString(j, 0)).append("</Td>");
                        } else {
//                            result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">" + time.getFieldValueString(j, 0) + "</Td>";
                            result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">").append(time.getFieldValueString(j, 0)).append("</Td>");
                        }

                    }
                } else {
//                    result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">" + time.getFieldValueString(j, 0) + "</Td>";
                    result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">").append(time.getFieldValueString(j, 0)).append("</Td>");

                }
            }

            if (viewByElement.equalsIgnoreCase("0") && overAllFlag == false) {
//                result = result + "<Td align=\"center\" colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Row Total</Td>";
                result.append("<Td align=\"center\" colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Row Total</Td>");
            } else {
//                result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Row Total</Td>";
                result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Row Total</Td>");
            }

            if (viewByElement.equalsIgnoreCase("0") && overAllFlag == false) {
//                result = result + "<Td align=\"center\"  colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Restricting Total</Td>";
                result.append("<Td align=\"center\"  colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Restricting Total</Td>");
                result.append("<Td align=\"center\"  colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Difference</Td>");
//                result = result + "<Td align=\"center\"  colspan='2' style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Difference</Td>";
            }

//            result = result + "</Tr>";
            result.append("</Tr>");

            /// to show percent and absolute in the header
            if (viewByElement.equalsIgnoreCase("0")) {
                String basisHeader1 = "";
                String basisHeader2 = "";
                if (basisVal.equalsIgnoreCase("absolute")) {
                    basisHeader1 = "Absolute";
                    basisHeader2 = "Percentage";
                } else if (basisVal.equalsIgnoreCase("Percentage")) {
                    basisHeader1 = "Percentage";
                    basisHeader2 = "absolute";
                }

                if (overAllFlag == false) {
//                    result = result + "<Tr><Td></Td>";
                    result.append("<Tr><Td></Td>");
                    for (int j = 0; j < time.getRowCount(); j++) {
//                        result = result + "<Td>" + basisHeader1 + "</Td>";
                        result.append("<Td>").append(basisHeader1).append("</Td>");
//                        result = result + "<Td>" + basisHeader2 + "</Td>";
                        result.append("<Td>").append(basisHeader2).append("</Td>");
                    }
//                    result = result + "<Td>" + basisHeader1 + "</Td><Td>" + basisHeader2 + "</Td>";
                    result.append("<Td>").append(basisHeader1).append("</Td><Td>").append(basisHeader2).append("</Td>");
//                    result = result + "<Td>" + basisHeader1 + "</Td><Td>" + basisHeader2 + "</Td><Td>" + basisHeader1 + "</Td><Td>" + basisHeader2 + "</Td></Tr>";
                    result.append("<Td>").append(basisHeader1).append("</Td><Td>").append(basisHeader2).append("</Td><Td>").append(basisHeader1).append("</Td><Td>").append(basisHeader2).append("</Td></Tr>");
                }
            }

            // display the rowedgevalues
            if (!viewByElement.equalsIgnoreCase("0")) {
                for (int x = 1; x < paramVals.size(); x++) {
                    String pVal = paramVals.get(x).toString();
                    long rowTotalVal = 0;
                    if (rowTotal.containsKey(pVal)) {
                        rowTotalVal = ((Long) rowTotal.get(pVal)).longValue();
                    }
                    // when no child is there then no drill otherwise give drill
                    if (!childElement.equalsIgnoreCase("-1")) {
//                        result = result + "<Tr>" + "<Td style=\"width:auto\"><a href=\"javascript:submitDrill('" + drllUrl + paramVals.get(x).toString() + "')\">" + paramVals.get(x).toString() + "</Td>";
                        result.append("<Tr><Td style=\"width:auto\"><a href=\"javascript:submitDrill('").append(drllUrl).append(paramVals.get(x).toString()).append("')\">").append(paramVals.get(x).toString()).append("</Td>");
                    } else if (childElement.equalsIgnoreCase("-1")) {
//                        result = result + "<Tr>" + "<Td style=\"width:auto\">" + paramVals.get(x).toString() + "</Td>";
                        result.append("<Tr><Td style=\"width:auto\">").append(paramVals.get(x).toString()).append("</Td>");
                    }

                    HashMap dt = new HashMap();
                    if (allV.containsKey(pVal)) {
                        dt = (HashMap) allV.get(pVal);
                    }
                    for (int y = 0; y < time.getRowCount(); y++) {
                        String timeV = time.getFieldValueString(y, 0);
                        long mV = 0;
                        if (dt.containsKey(timeV)) {
                            mV = ((Long) dt.get(timeV)).longValue();
                        }

                        String value = "";
                        if (mV != 0) {
//                            value = "" + mV + "";
                            value = String.valueOf(mV);
                        }
                        // originalResult.put(paramVals.get(x)+":"+timeV,Integer.valueOf(mV));
                        String freezeCell = "";
                        if (periodFlag == true) {
                            if (value.equalsIgnoreCase("0") || value.equalsIgnoreCase("")) {
                                freezeCell = "readonly";
                            }
                        }

                        if (periodFlag == true) {
                            if (displayPeriodFlagMessage == false) {
                                if (value.equalsIgnoreCase("0") || value.equalsIgnoreCase("")) {
                                    displayPeriodFlagMessage = true;
                                }
                            }
                        }
//                        result = result + "<Td><input style=\"width:99%\" readonly type=\"text\" id=\"" + paramVals.get(x) + ":" + timeV + "\" onkeyup=\"autoSum('" + paramVals.get(x) + ":" + timeV + "')\" name=\"" + paramVals.get(x) + ":" + timeV + "\"  " + freezeCell + " value=\"" + value + "\"></Td>";
                        result.append("<Td><input style=\"width:99%\" readonly type=\"text\" id=\"").append(paramVals.get(x)).append(":").append(timeV).append("\" onkeyup=\"autoSum('").append(paramVals.get(x)).append(":").append(timeV).append("')\" name=\"").append(paramVals.get(x)).append(":").append(timeV).append("\"  ").append(freezeCell).append(" value=\"").append(value).append("\"></Td>");

                    }
                    //if(showRowTotal==true)
                    //{
//                    result = result + "<Td><input  style=\"width:99%\" class=\"displayTotal\" readonly value=\"" + rowTotalVal + "\"></Td>";
                    result.append("<Td><input  style=\"width:99%\" class=\"displayTotal\" readonly value=\"").append(rowTotalVal).append("\"></Td>");
                    // }
//                    result = result + "</Tr>";
                    result.append("</Tr>");
                }
            }
            String parentPeriodType = collect.getParentTimeId(periodType, targetId);
            if (viewByElement.equalsIgnoreCase("0")) {
                if (overAllFlag == true) {

                    HashMap dt = new HashMap();
//                    result = result + "<Tr><Td>" + paramVals.get(0).toString() + "</Td>";
                    result.append("<Tr><Td>").append(paramVals.get(0).toString()).append("</Td>");
                    long rTotal = 0;

                    for (int x = 1; x < paramVals.size(); x++) {
                        String pVal = paramVals.get(x).toString();
                        if (allV.containsKey(pVal)) {
                            dt = (HashMap) allV.get(pVal);
                        }
                        for (int y = 0; y < time.getRowCount(); y++) {
                            String timeV = time.getFieldValueString(y, 0);
                            long mV = 0;
                            String value = "";
                            if (dt.containsKey(timeV)) {
                                mV = ((Long) dt.get(timeV)).longValue();
                                if (mV != 0) {
//                                    value = "" + mV + "";
                                    value = String.valueOf(mV);
                                }
                            }
                            rTotal = rTotal + mV;

//                            result = result + "<Td><input  style=\"width:99%\" readonly type=\"text\" id=\"" + paramVals.get(x) + ":" + timeV + "\" onkeyup=\"autoSum('" + paramVals.get(x) + ":" + timeV + "')\" name=\"" + paramVals.get(x) + ":" + timeV + "\"   value=\"" + value + "\"></Td>";
                            result.append("<Td><input  style=\"width:99%\" readonly type=\"text\" id=\"").append(paramVals.get(x)).append(":").append(timeV).append("\" onkeyup=\"autoSum('").append(paramVals.get(x)).append(":").append(timeV).append("')\" name=\"").append(paramVals.get(x)).append(":").append(timeV).append("\"   value=\"").append(value).append("\"></Td>");

                        }
                    }
//                    result = result + "<Td><input type=\"text\"  style=\"width:99%\" class=\"displayTotal\" readonly value=" + rTotal + "></Td></Tr>";
                    result.append("<Td><input type=\"text\"  style=\"width:99%\" class=\"displayTotal\" readonly value=").append(rTotal).append("></Td></Tr>");
                } else {
                    ArrayList upperTimeList = new ArrayList();
                    if (periodType.equalsIgnoreCase("Qtr")) {
                        if (parentPeriodType.equalsIgnoreCase("Year")) {
                            for (int y = 0; y < time.getRowCount(); y++) {
                                String timeV = time.getFieldValueString(y, 0);
                                if (!upperTimeList.contains(timeV.substring(2))) {
                                    upperTimeList.add(timeV.substring(2));
                                }
                            }
                            //upperTimeList=
                        }
                    } else if (periodType.equalsIgnoreCase("Month")) {
                        Vector qtrVal = new Vector();
                        if (parentPeriodType.equalsIgnoreCase("Qtr")) {
                            for (int y = 0; y < time.getRowCount(); y++) {
                                String timeV = time.getFieldValueString(y, 0);
                                String QtrForMonQ = "select DISTINCT cmqtr_code, pmyqtr from prg_acn_mon_denom where mon_name='" + timeV + "'";
                                PbReturnObject pb = pbDb.execSelectSQL(QtrForMonQ);
                                if (!upperTimeList.contains(pb.getFieldValueString(0, 0).substring(1))) {
                                    upperTimeList.add(pb.getFieldValueString(0, 0).substring(1));
                                }
                            }
                            //upperTimeList=
                        }
                        if (parentPeriodType.equalsIgnoreCase("Year")) {
                            for (int y = 0; y < time.getRowCount(); y++) {
                                String timeV = time.getFieldValueString(y, 0);
                                String QtrForMonQ = "select distinct cyear  from prg_acn_mon_denom where mon_name='" + timeV + "'";
                                PbReturnObject pb = pbDb.execSelectSQL(QtrForMonQ);
                                if (!upperTimeList.contains(pb.getFieldValueString(0, 0))) {
                                    upperTimeList.add(pb.getFieldValueString(0, 0));
                                }
                            }
                            //upperTimeList=
                        }
                    } else if (periodType.equalsIgnoreCase("Day")) {
                        Vector qtrVal = new Vector();
                        if (parentPeriodType.equalsIgnoreCase("Month")) {
                            for (int y = 0; y < time.getRowCount(); y++) {
                                String timeV = time.getFieldValueString(y, 0);
                                String DaysForMonQ = "select to_char(ddate,'MON-yy') from pr_day_denom where ddate=to_date('" + timeV + "','dd-mm-yy')";
                                PbReturnObject pb = pbDb.execSelectSQL(DaysForMonQ);
                                if (!upperTimeList.contains(pb.getFieldValueString(0, 0))) {
                                    upperTimeList.add(pb.getFieldValueString(0, 0));
                                }
                            }
                            //upperTimeList=
                        }
                    }
                    String upperPerioQ = "";
                    if (parentPeriodType.equalsIgnoreCase("year")) {
                        upperPerioQ = " select sum(" + measureName.trim().replace(" ", "_") + "),t_year from " + targetTable.trim() + " where target_id='?' and viewby='OverAll' group by t_year order by t_year";
                    } else if (parentPeriodType.equalsIgnoreCase("Qtr")) {
                        upperPerioQ = " select sum(" + measureName.trim().replace(" ", "_") + "),t_qtr from " + targetTable.trim() + " where target_id='?' and viewby='OverAll' group by t_qtr order by t_qtr";
                    } else if (parentPeriodType.equalsIgnoreCase("Month")) {
                        upperPerioQ = " select sum(" + measureName.trim().replace(" ", "_") + "),t_month from " + targetTable.trim() + " where target_id='?' and viewby='OverAll' group by t_month order by t_month";
                    }

                    ps = con.prepareStatement(upperPerioQ);
                    ps.setString(1, targetId);
                    rs = ps.executeQuery();
                    PbReturnObject timeRestrictionD = new PbReturnObject();
                    if (rs != null) {
                        timeRestrictionD = new PbReturnObject(rs);
                    }
                    HashMap timeParentMap = new HashMap();
                    long lVal = 0;
                    for (int m = 0; m < timeRestrictionD.getRowCount(); m++) {
                        lVal = Long.parseLong(timeRestrictionD.getFieldValueString(m, 0));
                        timeParentMap.put(timeRestrictionD.getFieldValueString(m, 1), new Long(lVal));
                    }
                    //paramVals.add("OverAll Target");
                    // paramVals.add("OverAll");
                    HashMap dt = new HashMap();
//                    result = result + "<Tr><Td>" + paramVals.get(0).toString() + "</Td>";
                    result.append("<Tr><Td>").append(paramVals.get(0).toString()).append("</Td>");
                    long timeResTotal = 0;
                    long rTotalForOverall = 0;
                    for (int x = 1; x < paramVals.size(); x++) {

                        String pVal = paramVals.get(x).toString();
                        if (allV.containsKey(pVal)) {
                            dt = (HashMap) allV.get(pVal);
                        }
                        for (int y = 0; y < upperTimeList.size(); y++) {
                            if (timeParentMap.containsKey(upperTimeList.get(y).toString())) {
                                Long lv = (Long) timeParentMap.get(upperTimeList.get(y).toString());
                                timeResTotal = timeResTotal + lv;
                            }
                        }
                        for (int y = 0; y < time.getRowCount(); y++) {
                            String timeV = time.getFieldValueString(y, 0);
                            long mV = 0;
                            String value = "";
                            if (dt.containsKey(timeV)) {
                                mV = ((Long) dt.get(timeV)).longValue();
                                if (mV != 0) {
//                                    value = "" + mV + "";
                                    value = String.valueOf(mV);
                                }
                            }
                            long perc = 0;
                            if (timeResTotal != 0) {
                                perc = (mV * 100) / timeResTotal;
                            }
                            rTotalForOverall = rTotalForOverall + mV;
                            String pervalue = "";
//                            pervalue = "" + perc + "";
                            pervalue = String.valueOf(perc);
                            if (basisVal.equalsIgnoreCase("absolute")) {
//                                result = result + "<Td><input readonly  style=\"width:99%\" type=\"text\" id=\"" + paramVals.get(x) + ":" + timeV + "\" onkeyup=\"autoSum('" + paramVals.get(x) + ":" + timeV + "')\" name=\"" + paramVals.get(x) + ":" + timeV + "\"   value=\"" + value + "\"></Td>";
                                result.append("<Td><input readonly  style=\"width:99%\" type=\"text\" id=\"").append(paramVals.get(x)).append(":").append(timeV).append("\" onkeyup=\"autoSum('").append(paramVals.get(x)).append(":").append(timeV).append("')\" name=\"").append(paramVals.get(x)).append(":").append(timeV).append("\"   value=\"").append(value).append("\"></Td>");
//                                result = result + "<Td><input style=\"background-color:silver;width:99%\" id=\"" + paramVals.get(x) + ":" + timeV + ":d" + "\" readonly onkeypress=\"return isNumberKey(event)\" type=\"text\" id=\"" + paramVals.get(x) + ":" + timeV + ":d\" name=\"" + paramVals.get(x) + ":" + timeV + ":d\"   value=\"" + perc + "\"></Td>";
                                result.append("<Td><input style=\"background-color:silver;width:99%\" id=\"").append(paramVals.get(x)).append(":").append(timeV).append(":d").append("\" readonly onkeypress=\"return isNumberKey(event)\" type=\"text\" id=\"").append(paramVals.get(x)).append(":").append(timeV).append(":d\" name=\"").append(paramVals.get(x)).append(":").append(timeV).append(":d\"   value=\"").append(perc).append("\"></Td>");
                            }
                            if (basisVal.equalsIgnoreCase("percentage")) {
//                                result = result + "<Td><input readonly type=\"text\"  style=\"width:99%\" id=\"" + paramVals.get(x) + ":" + timeV + "\" onkeyup=\"autoSum('" + paramVals.get(x) + ":" + timeV + "')\" name=\"" + paramVals.get(x) + ":" + timeV + "\"   value=\"" + perc + "\"></Td>";
                                result.append("<Td><input readonly type=\"text\"  style=\"width:99%\" id=\"").append(paramVals.get(x)).append(":").append(timeV).append("\" onkeyup=\"autoSum('").append(paramVals.get(x)).append(":").append(timeV).append("')\" name=\"").append(paramVals.get(x)).append(":").append(timeV).append("\"   value=\"").append(perc).append("\"></Td>");
//                                result = result + "<Td><input style=\"background-color:silver;width:99%\" id=\"" + paramVals.get(x) + ":" + timeV + ":d" + "\" readonly name=\"" + paramVals.get(x) + ":" + timeV + ":d" + "\"   value=\"" + value + "\"></Td>";
                                result.append("<Td><input style=\"background-color:silver;width:99%\" id=\"").append(paramVals.get(x)).append(":").append(timeV).append(":d").append("\" readonly name=\"").append(paramVals.get(x)).append(":").append(timeV).append(":d").append("\"   value=\"").append(value).append("\"></Td>");
                            }
                            // result = result+"<Td><input onkeypress=\"return isNumberKey(event)\" type=\"text\" id=\""+paramVals.get(x)+":"+timeV+"\" onkeyup=\"autoSum('"+paramVals.get(x)+":"+timeV+"')\" name=\""+paramVals.get(x)+":"+timeV+"\"   value=\""+value+"\"></Td>";

                            //for restricting upper time level total
                        }
                        long percRow = 0;
                        if (rTotalForOverall != 0) {
                            percRow = (rTotalForOverall / timeResTotal) * 100;
                        }

                        // for row total
                        if (basisVal.equalsIgnoreCase("absolute")) {
//                            result = result + "<Td><input class=\"displayTotal\" readonly  value=\"" + rTotalForOverall + "\"></Td><Td><input type=\"text\" class=\"displayTotal\" readonly value=" + percRow + "></Td>";
                            result.append("<Td><input class=\"displayTotal\" readonly  value=\"").append(rTotalForOverall).append("\"></Td><Td><input type=\"text\" class=\"displayTotal\" readonly value=").append(percRow).append("></Td>");
                        }
                        if (basisVal.equalsIgnoreCase("percentage")) {
                            result.append("<Td><input class=\"displayTotal\" readonly value=\"").append(percRow).append("\"></Td><Td><input type=\"text\" class=\"displayTotal\" readonly value=").append(rTotalForOverall).append("></Td>");
                        }

                        RestrictingTotal.put("OverAll", new Long(timeResTotal));
                        if (timeResTotal == 0) {
                            freezeButton = "disabled";
                        }
                        // for restricting total
                        long timeRestTotPer = 0;
                        timeRestTotPer = 100;
                        if (basisVal.equalsIgnoreCase("absolute")) {
//                            result = result + "<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"" + timeResTotal + "\"></Td>";
                            result.append("<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"").append(timeResTotal).append("\"></Td>");
//                            result = result + "<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"" + timeRestTotPer + "\"></Td>";
                            result.append("<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"").append(timeRestTotPer).append("\"></Td>");
                        }
                        if (basisVal.equalsIgnoreCase("percentage")) {
//                            result = result + "<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"" + timeRestTotPer + "\"></Td>";
                            result.append("<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"").append(timeRestTotPer).append("\"></Td>");
//                            result = result + "<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"" + timeResTotal + "\"></Td>";
                            result.append("<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"").append(timeResTotal).append("\"></Td>");

                        }

                        //for difference
                        long difVal = 0;
                        difVal = timeResTotal - rTotalForOverall;
                        long difValPerc = 0;
                        difValPerc = 100 - percRow;
                        if (basisVal.equalsIgnoreCase("absolute")) {
//                            result = result + "<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"" + difVal + "\"></Td>";
                            result.append("<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"").append(difVal).append("\"></Td>");
//                            result = result + "<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"" + difValPerc + "\"></Td>";
                            result.append("<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"").append(difValPerc).append("\"></Td>");
                        }
                        if (basisVal.equalsIgnoreCase("percentage")) {
//                            result = result + "<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"" + difValPerc + "\"></Td>";
                            result.append("<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"").append(difValPerc).append("\"></Td>");
//                            result = result + "<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"" + difVal + "\"></Td>";
                            result.append("<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"").append(difVal).append("\"></Td>");

                        }
                    }
//                    result = result + "</Tr>";
                    result.append("</Tr>");

                }
            }

            if (!viewByElement.equalsIgnoreCase("0")) {
                long gTot = 0;
//                result = result + "<Tr><Td style=\"font-weight:bold;font-size:11px\">Column Total</Td>";
                result.append("<Tr><Td style=\"font-weight:bold;font-size:11px\">Column Total</Td>");
                long colTotVal = 0;
                for (int p = 0; p < colEdgeVals.size(); p++) {
                    String colKey = colEdgeVals.get(p).toString();
                    colTotVal = 0;
                    if (colTotal.containsKey(colKey)) {
                        colTotVal = ((Long) colTotal.get(colKey)).longValue();
                    }
//                    result = result + "<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"" + colTotVal + "\"></Td>";
                    result.append("<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"").append(colTotVal).append("\"></Td>");
                    gTot = gTot + colTotVal;
                    //  RestrictingTotal.put(colEdgeVals.get(p).toString(),new Long(colTotVal));
                }

//                result = result + "<Td><input type=\"text\" class=\"displayTotal\" readonly id=\"GrandTotal\" value=" + gTot + "></Td></Tr>";
                result.append("<Td><input type=\"text\" class=\"displayTotal\" readonly id=\"GrandTotal\" value=").append(gTot).append("></Td></Tr>");
            }// to find the parent total
            Object tarObj1[] = new Object[2];
            tarObj1[0] = targetId;
            tarObj1[1] = viewByElement;
            String getParents = resundle.getString("getParents");
            String fingetParents = pbDb.buildQuery(getParents, tarObj1);
            PbReturnObject viewByElementInfoOb = pbDb.execSelectSQL(fingetParents);

            // get all the parent element ids of the view by
            boolean addParents = false;
            for (int y = 0; y < viewByElementInfoOb.getRowCount(); y++) {
                String localEle = viewByElementInfoOb.getFieldValueString(y, "ELEMENT_ID");
                if (addParents == true) {
                    viewByParentsList.add(localEle);
                }
                if (localEle.equalsIgnoreCase(viewByElement)) {
                    addParents = true;
                }
            }

            // for appending the non ALl values of any element ids from the request parameters
//            String parentsFilter = "";
            StringBuilder parentsFilter = new StringBuilder();
            for (int i = 0; i < keys.length; i++) {
                a = (String) allParamHashMap.get(keys[i]);
                if (viewByParentsList.contains(keys[i])) {
                    if (!a.equalsIgnoreCase("All")) {
//                        parentsFilter = parentsFilter + " and " + elementCols.get(keys[i]) + "='" + a + "'";
                        parentsFilter.append(" and ").append(elementCols.get(keys[i])).append("='").append(a).append("'");
                    }
                }
            }

            String getParentElementId = resundle.getString("getParentElementId");
            String fingetParentElementId = pbDb.buildQuery(getParentElementId, tarObj1);

            ArrayList parentElementsList = new ArrayList();
            PbReturnObject allElementInfoOb = pbDb.execSelectSQL(fingetParentElementId);
            boolean skipChilds = false;
            for (int h = 0; h < allElementInfoOb.getRowCount(); h++) {
                if (allElementInfoOb.getFieldValueString(h, "ELEMENT_ID").equalsIgnoreCase(viewByElement)) {
                    skipChilds = true;
                }
                if (skipChilds == false) {
                    parentElementsList.add(allElementInfoOb.getFieldValueString(h, "ELEMENT_ID"));
                }

            }

            String parentEleId = "-1";
            for (int g = 0; g < allElementInfoOb.getRowCount(); g++) {
                int previous = g - 1;
                String localElementId = allElementInfoOb.getFieldValueString(g, "ELEMENT_ID");
                if (viewByElement.equalsIgnoreCase(localElementId)) {
                    if (previous >= 0) {
                        parentEleId = allElementInfoOb.getFieldValueString(previous, "ELEMENT_ID");
                    }
                }
            }
            String parentBussCol = "";
            if (elementCols.containsKey(parentEleId)) {
                parentBussCol = (String) elementCols.get(parentEleId);
                parentBussCol = "," + parentBussCol;
            }

            ArrayList bussTable = new ArrayList();

            // to get the business table ids and get the data from the customer database
            for (int g = 0; g < allElementInfoOb.getRowCount(); g++) {
                String localElementId = allElementInfoOb.getFieldValueString(g, "ELEMENT_ID");
                if (viewByElement.equalsIgnoreCase(localElementId)) {
                    bussTable.add(allElementInfoOb.getFieldValueString(g, "BUSS_TABLE"));
                }
                if (parentEleId.equalsIgnoreCase(localElementId)) {
                    bussTable.add(allElementInfoOb.getFieldValueString(g, "BUSS_TABLE"));
                }
            }

            String getTargetPrimParam = resundle.getString("getTargetPrimParam");
            String fingetTargetPrimParam = pbDb.buildQuery(getTargetPrimParam, tarOb);
            PbReturnObject targetInfoOb = pbDb.execSelectSQL(fingetTargetPrimParam);

            primParamEleId = targetInfoOb.getFieldValueString(0, "TARGET_PRIM_PARAM");

            String getAllElementsInfo = resundle.getString("getAllElementsInfo");
            String fingetAllElementsInfo = pbDb.buildQuery(getAllElementsInfo, tarOb);
            PbReturnObject targetElementsInfoOb = pbDb.execSelectSQL(fingetAllElementsInfo);
            HashMap elementDet = new HashMap();
            int localDim = targetElementsInfoOb.getFieldValueInt(0, "ELEMENT_ID");
            int dimId = localDim;
            String parentId = "-1";
            HashMap parentsMap = new HashMap();
            for (int m = 0; m < targetElementsInfoOb.getRowCount(); m++) {
                parentsMap.put(targetElementsInfoOb.getFieldValueString(m, "CHILD_ELEMENT_ID"), targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"));
            }
            for (int m = 0; m < targetElementsInfoOb.getRowCount(); m++) {
                ArrayList det = new ArrayList();
                det.add(0, targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"));
                parentId = "-1";
                if (parentsMap.containsKey(targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"))) {
                    parentId = parentsMap.get(targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID")).toString();
                }
                //parentId=targetElementsInfoOb.getFieldValueString(m,"PARENT_ID");
                if (parentId.equalsIgnoreCase("")) {
                    parentId = "-1";
                }
                det.add(1, parentId);
                det.add(2, targetElementsInfoOb.getFieldValueString(m, "PARAM_DISP_NAME"));
                // det.add(3,targetElementsInfoOb.getFieldValueString(m,"PARAM_DISP_NAME"));
                elementDet.put(targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"), det);
            }
            //elementCols
            //viewByElement

            parentBussCol = "";
            HashMap parentTotals = new HashMap();
            String parentDispName = "";
            String parentEle = "";
            String parentDataQ = "";

            // when primary parameter dont show restricting total else show
            if (primParamEleId.equalsIgnoreCase(viewByElement)) {
                showRestrictingTotal = false;
            }

            if (showRestrictingTotal == true) {
                if (elementDet.containsKey(viewByElement)) {
                    ArrayList eleDet = new ArrayList();
                    eleDet = (ArrayList) elementDet.get(viewByElement);
                    parentEle = eleDet.get(1).toString();
                    parentDispName = eleDet.get(2).toString();
                }
                if (elementCols.containsKey(parentEle)) {
                    parentBussCol = elementCols.get(parentEle).toString();
                }
                // to put the parent filters on row view by
//                ArrayList allParents = new ArrayList();
//                boolean messageShow = false;

                String parentParamFilter = "";
                String[] reqParams = (String[]) (targetParametersValues.keySet()).toArray(new String[0]);
                for (int o = 0; o < reqParams.length; o++) {
                    String key = reqParams[o];
                    String value = "-1";
                    String val = targetParametersValues.get(key).toString();
                    if (key.equalsIgnoreCase(parentEle)) {
                        if (targetParametersValues.containsKey(key)) {

                            if (!val.equalsIgnoreCase("All")) {
                                parentParamFilter = " and " + elementCols.get(key).toString() + "='" + val + "'";
                            }
                            if (val.equalsIgnoreCase("All")) {
                                if (errorMessage.length() == 0) {
                                    errorMessage = "As " + viewByNames.get(parentEle.trim()).toString() + " is not selected for " + viewByNames.get(viewByElement.trim()).toString() + ".So no data entry is allowed.";
                                }
                            }
                        }
                    }
                    if (viewByParentsList.contains(key)) {
                        if (!parentEle.equalsIgnoreCase(key)) {
                            if (val.equalsIgnoreCase("All")) {
                                errorMessage = errorMessage + "Select " + viewByNames.get(key).toString() + " for data entry.";
                                disableSaveButtonFlag = true;
                            }
                        }
                    }
                }
                if (periodType.equalsIgnoreCase("Day")) {
                    if (!parentEle.equalsIgnoreCase("-1")) {
                        parentDataQ = "select " + elementCols.get(parentEle.trim()).toString() + ",sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + " group by " + elementCols.get(parentEle.trim()).toString() + ",to_char(t_date,'dd-mm-yy') order by " + elementCols.get(parentEle.trim()).toString() + ",to_char(t_date,'dd-mm-yy')";
                    } else {
                        parentDataQ = "select sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' and t_day is not null " + parentParamFilter + " " + parentsFilter + " group by to_char(t_date,'dd-mm-yy') order by to_char(t_date,'dd-mm-yy')";
                    }
                } // get the data for parent parameter after putting its filters for parents
                else if (periodType.equalsIgnoreCase("Month")) {
                    if (!parentEle.equalsIgnoreCase("-1")) {
                        parentDataQ = "select " + elementCols.get(parentEle.trim()).toString() + ",sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + " group by " + elementCols.get(parentEle.trim()).toString() + ",t_month order by " + elementCols.get(parentEle.trim()).toString() + ",t_month ";
                    } else {
                        parentDataQ = "select sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' and t_month is not null  " + parentParamFilter + " " + parentsFilter + " group by t_month order by t_month";
                    }
                } else if (periodType.equalsIgnoreCase("Quarter") || periodType.equalsIgnoreCase("Qtr")) {
                    if (!parentEle.equalsIgnoreCase("-1")) {
                        parentDataQ = "select " + elementCols.get(parentEle.trim()).toString() + ",sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + " group by " + elementCols.get(parentEle.trim()).toString() + ",t_qtr order by " + elementCols.get(parentEle.trim()).toString() + ",t_qtr ";
                    } else {
                        parentDataQ = "select sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' and t_qtr is not null  " + parentParamFilter + " " + parentsFilter + "group by t_qtr order by t_qtr ";
                    }
                } else if (periodType.equalsIgnoreCase("Year")) {
                    if (!parentEle.equalsIgnoreCase("-1")) {
                        parentDataQ = "select " + elementCols.get(parentEle.trim()).toString() + ",sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + " group by " + elementCols.get(parentEle.trim()).toString() + ",t_year order by " + elementCols.get(parentEle.trim()).toString() + ",t_year";
                    } else {
                        parentDataQ = "select sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='OverAll' and t_year is not null " + parentParamFilter + " " + parentsFilter + " group by t_year order by t_year ";
                    }
                }
                //PbReturnObject parentTotObj = pbDb.execSelectSQL(parentDataQ);
                // PbReturnObject parentTotObj = pbDb.execSelectSQL(parentDataQ);
                psP = con.prepareStatement(parentDataQ);
                rsP = psP.executeQuery();
                PbReturnObject parentTotObj = new PbReturnObject(rsP);

                primH2 = new HashMap();
                if (!parentEle.equalsIgnoreCase("-1")) {
                    for (int i = 0; i < parentTotObj.getRowCount(); i++) {
                        oldPrimV = parentTotObj.getFieldValueString(i, 0);
                        if (newPrimV.equalsIgnoreCase("")) {
                            newPrimV = oldPrimV;
                        }
                        oldPrimV = parentTotObj.getFieldValueString(i, 0);
                        String dat = parentTotObj.getFieldValueString(i, 2);
                        long mValue = parentTotObj.getFieldValueInt(i, 1);
                        long old = 0;
                        if (primH2.containsKey(dat)) {
                            old = ((Long) primH2.get(dat)).longValue();
                        }
                        mValue = mValue + old;
                        primH2.put(dat, new Long(mValue));
                    }
                } else {
                    for (int i = 0; i < parentTotObj.getRowCount(); i++) {
                        String dat = parentTotObj.getFieldValueString(i, 1);
                        long mValue = Long.parseLong(parentTotObj.getFieldValueString(i, 0));
                        long old = 0;
                        if (primH2.containsKey(dat)) {
                            old = ((Long) primH2.get(dat)).longValue();
                        }
                        mValue = mValue + old;
                        primH2.put(dat, new Long(mValue));
                    }
                }

//                result = result + "<Tr><Td style=\"font-weight:bold;font-size:11px\">Restricting Total</Td>";
                result.append("<Tr><Td style=\"font-weight:bold;font-size:11px\">Restricting Total</Td>");
                long restrictGTotal = 0;
                for (int k = 0; k < colEdgeVals.size(); k++) {
                    long rTotal = 0;
                    if (primH2.containsKey(colEdgeVals.get(k).toString())) {
                        rTotal = ((Long) primH2.get(colEdgeVals.get(k).toString())).longValue();
                    }
                    restrictGTotal = restrictGTotal + rTotal;
//                    result = result + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"" + rTotal + "\"></Td>";
                    result.append("<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"").append(rTotal).append("\"></Td>");
                    if (disableSaveButtonFlag == false) {
                        if (primH2.containsKey(colEdgeVals.get(k).toString())) {
                            if (rTotal != 0) {
                                disableSaveButtonFlag = true;
                            }
                        }
                    }
                    RestrictingTotal.put(colEdgeVals.get(k).toString(), new Long(rTotal));
                }
//                result = result + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=" + restrictGTotal + "></Td></Tr>";
                result.append("<Td><input type=\"text\" readonly class=\"displayTotal\" value=").append(restrictGTotal).append("></Td></Tr>");
//                result = result + "<Tr><Td style=\"font-weight:bold;font-size:11px\">Difference</Td>";
                result.append("<Tr><Td style=\"font-weight:bold;font-size:11px\">Difference</Td>");
                for (int k = 0; k < colEdgeVals.size(); k++) {
//                    result = result + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>";
                    result.append("<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>");
                }
//                result = result + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td></Tr>";
                result.append("<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td></Tr>");
            }

            // to show the overall target
            String primParamFilter = "";
            String overAllTotQ = "";
            // to put the primary paramteres hierarchy filters
            String[] reqParams = (String[]) (targetParametersValues.keySet()).toArray(new String[0]);
            for (int o = 0; o < reqParams.length; o++) {
                String key = reqParams[o];
                String value = "-1";
                if (key.equalsIgnoreCase(primParamEleId)) {
                    if (targetParametersValues.containsKey(key)) {
                        String val = targetParametersValues.get(key).toString();
                        if (!val.equalsIgnoreCase("All")) {
                            primParamFilter = " and " + elementCols.get(key).toString() + "='" + val + "'";
                        }

                    }
                }
            }
            String prim = "";
            String primName = "";
            if (viewByNames.containsKey(primParamEleId.trim())) {
                primName = viewByNames.get(primParamEleId.trim()).toString();
            }
            if (elementCols.containsKey(primParamEleId.trim())) {
                prim = elementCols.get(primParamEleId.trim()).toString();
            }
            if (periodType.equalsIgnoreCase("Day")) {
                overAllTotQ = "Select " + prim + ",sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + primName + "' " + primParamFilter + " group by " + prim + ",to_char(t_date,'dd-mm-yy') order by " + prim + ",to_char(t_date,'dd-mm-yy')";
            } else if (periodType.equalsIgnoreCase("Month")) {
                overAllTotQ = "Select " + prim + ",sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + primName + "' " + primParamFilter + " group by " + prim + ",t_month order by " + prim + ",t_month";
            } // overAllTotQ = "Select "+elementCols.get(primParamEleId.trim()).toString()+",sum("+measureName.trim()+"),t_month from "+targetTable.trim()+" where target_id="+targetId+" and viewby='"+viewByNames.get(primParamEleId.trim()).toString()+"' "+primParamFilter+" group by "+elementCols.get(primParamEleId.trim()).toString()+",t_month order by "+elementCols.get(primParamEleId.trim()).toString()+",t_month";
            else if (periodType.equalsIgnoreCase("Quarter")) {
                overAllTotQ = "Select " + prim + ",sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + primName + "' " + primParamFilter + " group by " + prim + ",t_qtr order by " + prim + ",t_qtr";
            } else if (periodType.equalsIgnoreCase("Year")) {
                overAllTotQ = "Select " + prim + ",sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + primName + "' " + primParamFilter + " group by " + prim + ",t_year order by " + prim + ",t_year";
            }

            //  PbReturnObject overAllObj = pbDb.execSelectSQL(overAllTotQ);
            if (!overAllTotQ.equalsIgnoreCase("")) {
                psO = con.prepareStatement(overAllTotQ);
            }
            if (viewByNames.containsKey(primParamEleId.trim())) {
                rsO = psO.executeQuery();
            }
            PbReturnObject overAllObj = new PbReturnObject();
            if (rsO != null) {
                overAllObj = new PbReturnObject(rsO);
            }

            primH2 = new HashMap();
            for (int i = 0; i < overAllObj.getRowCount(); i++) {
                oldPrimV = overAllObj.getFieldValueString(i, 0);
                if (newPrimV.equalsIgnoreCase("")) {
                    newPrimV = oldPrimV;
                }
                oldPrimV = overAllObj.getFieldValueString(i, 0);
                String dat = overAllObj.getFieldValueString(i, 2);
                int mValue = overAllObj.getFieldValueInt(i, 1);
                int old = 0;
                if (primH2.containsKey(dat)) {
                    old = ((Integer) primH2.get(dat)).intValue();
                }
                mValue = mValue + old;
                primH2.put(dat, Integer.valueOf(mValue));
            }
            int grandTotal = 0;
            for (int k = 0; k < colEdgeVals.size(); k++) {
                int rTotal = 0;
                if (primH2.containsKey(colEdgeVals.get(k).toString())) {
                    rTotal = ((Integer) primH2.get(colEdgeVals.get(k).toString())).intValue();
                }
                grandTotal = grandTotal + rTotal;
            }
            secAnalyze = "Time";

            // overAllMessage=" The Overall Target Set By Primary Parameter '"+viewByNames.get(primParamEleId.trim()).toString()+"' is "+grandTotal;
        } else {
            String dataQuery2 = "";
//            String displayDate = startRange;
            dateMeassage = "The Data is shown for " + minTimeLevel + "  '" + startRange + "'";
            if (minTimeLevel.equalsIgnoreCase("Day")) {
                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='?' and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(colViewByElement.trim()).toString() + "'  and t_date=to_date(to_char('?'),'mm-dd-yy')  ?   group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
                dataQuery2 = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='?' and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(viewByElement.trim()).toString() + "'  and t_date=to_date(to_char('?'),'mm-dd-yy') ?   group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
            } //dataQuery ="select "+elementCols.get(viewByElement.trim()).toString()+",sum("+measureName.trim()+"),"+elementCols.get(colViewByElement.trim()).toString()+" from "+targetTable.trim()+" where target_id='"+targetId+"' and viewby='"+viewByNames.get(colViewByElement.trim()).toString()+"' "+allParametrsFilterClause+" and t_date=to_date(to_char('"+startRange+"'),'mm-dd-yy') group by "+elementCols.get(viewByElement.trim()).toString()+","+elementCols.get(colViewByElement.trim()).toString()+" order by "+elementCols.get(viewByElement.trim()).toString()+","+elementCols.get(colViewByElement.trim()).toString();
            else if (minTimeLevel.equalsIgnoreCase("Month")) {
                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='?' and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(colViewByElement.trim()).toString() + "'  and t_month='?'  ?  group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
                dataQuery2 = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='?' and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(viewByElement.trim()).toString() + "'  and t_month='?'  ?  group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
            } // dataQuery ="select "+elementCols.get(viewByElement.trim()).toString()+",sum("+measureName.trim()+"),"+elementCols.get(colViewByElement.trim()).toString()+" from "+targetTable.trim()+" where target_id='"+targetId+"' and viewby='"+viewByNames.get(colViewByElement.trim()).toString()+"'  and t_month='"+startRange+"' "+allParametrsFilterClause+" group by "+elementCols.get(viewByElement.trim()).toString()+","+elementCols.get(colViewByElement.trim()).toString()+" order by "+elementCols.get(viewByElement.trim()).toString()+","+elementCols.get(colViewByElement.trim()).toString();
            else if (minTimeLevel.equalsIgnoreCase("Quarter") || minTimeLevel.equalsIgnoreCase("Qtr")) {
                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='?' and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(colViewByElement.trim()).toString() + "'  and t_qtr='?'  ?  group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
                dataQuery2 = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='?' and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(viewByElement.trim()).toString() + "'  and t_qtr='?'  ?   group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();

            } // dataQuery ="select "+elementCols.get(viewByElement.trim()).toString()+",sum("+measureName.trim()+"),"+elementCols.get(colViewByElement.trim()).toString()+" from "+targetTable.trim()+" where target_id='"+targetId+"' and viewby='"+viewByNames.get(colViewByElement.trim()).toString()+"'  and t_qtr='"+startRange.substring(1)+"' "+allParametrsFilterClause+" group by "+elementCols.get(viewByElement.trim()).toString()+","+elementCols.get(colViewByElement.trim()).toString()+" order by "+elementCols.get(viewByElement.trim()).toString()+","+elementCols.get(colViewByElement.trim()).toString();
            else if (minTimeLevel.equalsIgnoreCase("Year")) {
                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='?' and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(colViewByElement.trim()).toString() + "'  and t_year='?'  ?  group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
                dataQuery2 = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='?' and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(viewByElement.trim()).toString() + "'  and t_year='?'  ?   group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();

            }

            // PbReturnObject allD = pbDb.execSelectSQL(dataQuery);
            ps = con.prepareStatement(dataQuery);
            ps.setString(1, targetId);
            ps.setString(2,startRange  );
            ps.setString(3, allParametrsFilterClause.toString());
            rs = ps.executeQuery();
            PbReturnObject allD = new PbReturnObject(rs);

            ps = con.prepareStatement(dataQuery2);
            ps.setString(1, targetId);
            ps.setString(2,startRange  );
            ps.setString(3, allParametrsFilterClause.toString());
            rs = ps.executeQuery();
            PbReturnObject allD2 = new PbReturnObject(rs);

            allV = new HashMap();
            newPv = "";
            oldPv = "";
            // loop throuth data and make hashMap for row and column headers
            for (int n = 0; n < allD.getRowCount(); n++) {
                oldPv = allD.getFieldValueString(n, 0);
                if (newPv.equalsIgnoreCase("")) {
                    newPv = oldPv;
                } else if (!newPv.trim().equalsIgnoreCase(oldPv.trim()) && newPv.length() > 0) {
                    allV.put(newPv, primH);
                    primH = new HashMap();
                    newPv = oldPv;
                }
                String dat = allD.getFieldValueString(n, 2);
                Long mValue = Long.parseLong(allD.getFieldValueString(n, 1));
                primH.put(dat, new Long(mValue));
                if (n == (allD.getRowCount() - 1)) {
                    allV.put(oldPv, primH);
                }
            }

            for (int n = 0; n < allD2.getRowCount(); n++) {
                oldPv = allD2.getFieldValueString(n, 0);
                if (newPv.equalsIgnoreCase("")) {
                    newPv = oldPv;
                } else if (!newPv.trim().equalsIgnoreCase(oldPv.trim()) && newPv.length() > 0) {
                    allV.put(newPv, primH);
                    primH = new HashMap();
                    newPv = oldPv;
                }
                String dat = allD2.getFieldValueString(n, 2);
                Long mValue = Long.parseLong(allD2.getFieldValueString(n, 1));
                primH.put(dat, new Long(mValue));
                if (n == (allD2.getRowCount() - 1)) {
                    allV.put(oldPv, primH);
                }
            }
            PbReturnObject time = new PbReturnObject();//pbDb.execSelectSQL(colQry);
            // paramVals = f1.getRowEdges(rowQry,currVal);
            time = f1.getRowEdgesRetObj(colQry, colCurrVal);

            // to get originalResult
            for (int x = 1; x < paramVals.size(); x++) {
                String pVal = paramVals.get(x).toString();
                HashMap dt = new HashMap();
                if (allV.containsKey(pVal)) {
                    dt = (HashMap) allV.get(pVal);
                }
                for (int y = 0; y < time.getRowCount(); y++) {
                    String timeV = time.getFieldValueString(y, 0);
                    long mV = 0;
                    if (dt.containsKey(timeV)) {
                        mV = ((Long) dt.get(timeV)).intValue();
                    }
                    String value = "";
                    if (mV != 0) {
//                        value = "" + mV + "";
                        value = String.valueOf(mV);
                    }
                    originalResult.put(paramVals.get(x) + ":" + timeV, new Long(mV));
                }
            }
            // to get the column and row total
            HashMap rowTotal = new HashMap();
            HashMap colTotal = new HashMap();
            String[] originalValsKeys = (String[]) (originalResult.keySet()).toArray(new String[0]);
            for (int m = 0; m < originalValsKeys.length; m++) {
                String totalKey = originalValsKeys[m];
                String rowColKey = "";
                if (originalResult.containsKey(totalKey)) {
                    rowColKey = totalKey;//(String)originalResult.get(totalKey);
                    long oriVal = 0;
                    oriVal = ((Long) originalResult.get(totalKey)).longValue();
                    String totKey[] = rowColKey.split(":");
                    String rowKey = "";
                    String colKey = "";
                    for (int n = 0; n < totKey.length; n++) {
                        if (n == 0) {
                            rowKey = totKey[n];
                        }
                        if (n == 1) {
                            colKey = totKey[n];
                        }
                    }
                    if (rowTotal.containsKey(rowKey)) {
                        long al = ((Long) rowTotal.get(rowKey)).longValue();
                        al = oriVal + al;
                        rowTotal.put(rowKey, new Long(al));
                    } else {
                        rowTotal.put(rowKey, new Long(oriVal));
                    }

                    if (colTotal.containsKey(colKey)) {
                        long colAl = ((Long) colTotal.get(colKey)).longValue();
                        colAl = oriVal + colAl;
                        colTotal.put(colKey, new Long(colAl));
                    } else {
                        colTotal.put(colKey, new Long(oriVal));
                    }

                }
            }
            // to get the column and row total

            // to get the restricting total
            String colChildElement = "";
            colChildElement = collect.getChildElementId(colViewByElement, targetId);
            // to find the parent total
            String getParents = resundle.getString("getParents");
            Object tarObP[] = new Object[2];
            tarObP[0] = targetId;
            tarObP[1] = colViewByElement;
            String parentEle2 = "";

            String getParentElementId = resundle.getString("getParentElementId");
            Object tarObCol[] = new Object[2];
            tarObCol[0] = targetId;
            tarObCol[1] = colViewByElement;
            String fingetParentElementId = pbDb.buildQuery(getParentElementId, tarObCol);
            PbReturnObject allElementInfoOb = pbDb.execSelectSQL(fingetParentElementId);
            String parentEleId = "-1";
            for (int g = 0; g < allElementInfoOb.getRowCount(); g++) {
                int previous = g - 1;
                String localElementId = allElementInfoOb.getFieldValueString(g, "ELEMENT_ID");
                if (colViewByElement.equalsIgnoreCase(localElementId)) {
                    if (previous >= 0) {
                        parentEleId = allElementInfoOb.getFieldValueString(previous, "ELEMENT_ID");
                    }
                }
            }
            String parentBussCol = "";
            if (elementCols.containsKey(parentEleId)) {
                parentBussCol = (String) elementCols.get(parentEleId);
                parentBussCol = "," + parentBussCol;
            }
            ArrayList bussTable = new ArrayList();
            // to get the business table ids
            for (int g = 0; g < allElementInfoOb.getRowCount(); g++) {
                String localElementId = allElementInfoOb.getFieldValueString(g, "ELEMENT_ID");
                if (colViewByElement.equalsIgnoreCase(localElementId)) {
                    bussTable.add(allElementInfoOb.getFieldValueString(g, "BUSS_TABLE"));
                }
                if (parentEleId.equalsIgnoreCase(localElementId)) {
                    bussTable.add(allElementInfoOb.getFieldValueString(g, "BUSS_TABLE"));
                }
            }

            // to make the parent query for tables
            String startQuery = "select distinct " + elementCols.get(colViewByElement.trim()).toString() + " " + parentBussCol;
            String middleClause = "";
            String endClause = "";
            String parentQuery = "";
            BusinessGroupDAO bgDao = new BusinessGroupDAO();
            middleClause = bgDao.viewBussDataWithouCol(bussTable);
            parentQuery = startQuery + " " + middleClause;
            f1.elementId = colViewByElement;
            PbReturnObject parentDataOb = f1.getParentData(parentQuery);
            String getTargetPrimParam = resundle.getString("getTargetPrimParam");
            Object tarObCol2[] = new Object[1];
            tarObCol2[0] = targetId;
            String fingetTargetPrimParam = pbDb.buildQuery(getTargetPrimParam, tarObCol2);
            PbReturnObject targetInfoOb = pbDb.execSelectSQL(fingetTargetPrimParam);

            primParamEleId = targetInfoOb.getFieldValueString(0, "TARGET_PRIM_PARAM");
            String getAllElementsInfo = resundle.getString("getAllElementsInfo");
            String fingetAllElementsInfo = pbDb.buildQuery(getAllElementsInfo, tarObCol2);
            PbReturnObject targetElementsInfoOb = pbDb.execSelectSQL(fingetAllElementsInfo);
            HashMap elementDet = new HashMap();
            int localDim = targetElementsInfoOb.getFieldValueInt(0, "ELEMENT_ID");
            int dimId = localDim;
            String parentId = "-1";

            HashMap parentsMap = new HashMap();

            for (int m = 0; m < targetElementsInfoOb.getRowCount(); m++) {
                parentsMap.put(targetElementsInfoOb.getFieldValueString(m, "CHILD_ELEMENT_ID"), targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"));
            }
            for (int m = 0; m < targetElementsInfoOb.getRowCount(); m++) {
                ArrayList det = new ArrayList();
                det.add(0, targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"));
                parentId = "-1";
                if (parentsMap.containsKey(targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"))) {
                    parentId = parentsMap.get(targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID")).toString();
                }
                //parentId=targetElementsInfoOb.getFieldValueString(m,"PARENT_ID");
                if (parentId.equalsIgnoreCase("")) {
                    parentId = "-1";
                }
                det.add(1, parentId);
                det.add(2, targetElementsInfoOb.getFieldValueString(m, "PARAM_DISP_NAME"));
                elementDet.put(targetElementsInfoOb.getFieldValueString(m, "ELEMENT_ID"), det);
            }

            parentBussCol = "";
            HashMap parentTotals = new HashMap();
            String parentDispName = "";
            String parentEle = "";
            String parentDataQ = "";
            String parentDataQ2 = "";
            String parentDispName2 = "";

            //parentEle2=collect.getChildElementId(colViewByElement,targetId);
            if (elementDet.containsKey(viewByElement)) {
                ArrayList eleDet = new ArrayList();
                eleDet = (ArrayList) elementDet.get(viewByElement);
                parentEle = eleDet.get(1).toString();
                parentDispName = eleDet.get(2).toString();
            }
            if (elementDet.containsKey(colViewByElement)) {
                ArrayList eleDet = new ArrayList();
                eleDet = (ArrayList) elementDet.get(colViewByElement);
                parentEle2 = eleDet.get(1).toString();
                parentDispName2 = eleDet.get(2).toString();
            }
            if (elementCols.containsKey(parentEle)) {
                parentBussCol = elementCols.get(parentEle).toString();
            }

            // to put the parent filters on columns
            String parentParamFilter = "";
            String[] reqParams = (String[]) (targetParametersValues.keySet()).toArray(new String[0]);
            for (int o = 0; o < reqParams.length; o++) {
                String key = reqParams[o];
                String value = "-1";
                if (key.equalsIgnoreCase(parentEle)) {
                    if (targetParametersValues.containsKey(key)) {
                        String val = targetParametersValues.get(key).toString();
                        if (!val.equalsIgnoreCase("All")) {
                            parentParamFilter = " and " + elementCols.get(key).toString() + "='" + val + "'";
                        }
                        if (val.equalsIgnoreCase("All")) {
                            if (errorMessage.length() == 0) {
                                errorMessage = "As " + viewByNames.get(parentEle.trim()).toString() + " is not selected for " + viewByNames.get(viewByElement.trim()).toString() + ".So no data entry is allowed.";
                            }
                        }
                    }
                }
            }

            String parentParamFilter2 = "";
            for (int o = 0; o < reqParams.length; o++) {
                String key = reqParams[o];
                String value = "-1";
                if (key.equalsIgnoreCase(parentEle2)) {
                    if (targetParametersValues.containsKey(key)) {
                        String val = targetParametersValues.get(key).toString();
                        if (!val.equalsIgnoreCase("All")) {
                            parentParamFilter2 = " and " + elementCols.get(key).toString() + "='" + val + "'";
                        }
                        if (val.equalsIgnoreCase("All")) {
                            if (errorMessage.length() == 0) {
                                errorMessage = "As " + viewByNames.get(parentEle.trim()).toString() + " is not selected for " + viewByNames.get(viewByElement.trim()).toString() + ".So no data entry is allowed.";
                            }
                        }
                    }
                }
            }
            if (minTimeLevel.equalsIgnoreCase("Month")) {
                if (parentEle.trim().equalsIgnoreCase("-1")) {
                    parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby is null and  t_month='?' " + parentParamFilter + " group by " + elementCols.get(viewByElement.trim()).toString() + ",t_month order by " + elementCols.get(viewByElement.trim()).toString() + ",t_qtr";
                } else {
                    parentDataQ = "select " + elementCols.get(parentEle.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()) + " from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' and secviewby is null " + parentParamFilter + " and t_month='?' group by " + elementCols.get(parentEle.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString();
                }

                if (parentEle2.trim().equalsIgnoreCase("-1")) {
                    parentDataQ2 = "select " + elementCols.get(colViewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and  t_month='?' and secviewby is null " + parentParamFilter + " group by " + elementCols.get(colViewByElement.trim()).toString() + ",t_month order by " + elementCols.get(colViewByElement.trim()).toString() + ",t_month";
                } else {
                    parentDataQ2 = "select " + elementCols.get(parentEle2.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(parentEle2.trim()).toString() + " from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle2.trim()).toString() + "' " + parentParamFilter + " and t_month='?' and secviewby='" + viewByNames.get(parentEle2).toString() + "' group by " + elementCols.get(colViewByElement.trim()).toString() + "," + elementCols.get(parentEle2.trim()).toString() + " order by " + elementCols.get(colViewByElement.trim()).toString() + "," + elementCols.get(parentEle2.trim()).toString();
                }
            } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                if (parentEle.trim().equalsIgnoreCase("-1")) {
                    parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby is null and  t_date=to_char('?','dd-mm-yy') " + parentParamFilter + " group by " + elementCols.get(viewByElement.trim()).toString() + ",to_char(t_date,'dd-mm-yy') order by " + elementCols.get(viewByElement.trim()).toString() + ",to_char(t_date,'dd-mm-yy')";
                } else {
                    parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(parentEle.trim()).toString() + " from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' and secviewby is null " + parentParamFilter + " and t_date=to_char('?','dd-mm-yy') group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString();
                }

                if (parentEle2.trim().equalsIgnoreCase("-1")) {
                    parentDataQ2 = "select " + elementCols.get(colViewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and  t_date=to_char('?','dd-mm-yy') and secviewby is null " + parentParamFilter + " group by " + elementCols.get(colViewByElement.trim()).toString() + ",to_char(t_date,'dd-mm-yy') order by " + elementCols.get(colViewByElement.trim()).toString() + ",to_char(t_date,'dd-mm-yy')";
                } else {
                    parentDataQ2 = "select " + elementCols.get(parentEle2.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(parentEle2.trim()).toString() + " from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle2.trim()).toString() + "' " + parentParamFilter + " and t_date=to_char('?','dd-mm-yy') and secviewby='" + viewByNames.get(parentEle2).toString() + "' group by " + elementCols.get(colViewByElement.trim()).toString() + "," + elementCols.get(parentEle2.trim()).toString() + " order by " + elementCols.get(colViewByElement.trim()).toString() + "," + elementCols.get(parentEle2.trim()).toString();
                }

            } else if (minTimeLevel.equalsIgnoreCase("Quarter") || minTimeLevel.equalsIgnoreCase("Qtr")) {
                if (parentEle.trim().equalsIgnoreCase("-1")) {
                    parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby is null and  t_qtr='?' " + parentParamFilter + " group by " + elementCols.get(viewByElement.trim()).toString() + ",t_qtr order by " + elementCols.get(viewByElement.trim()).toString() + ",t_qtr";
                } else {
                    parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(parentEle.trim()).toString() + " from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' and secviewby is null " + parentParamFilter + " and t_qtr='?' group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString();
                }

                if (parentEle2.trim().equalsIgnoreCase("-1")) {
                    parentDataQ2 = "select " + elementCols.get(colViewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and  t_qtr='?' and secviewby is null " + parentParamFilter + " group by " + elementCols.get(colViewByElement.trim()).toString() + ",t_qtr order by " + elementCols.get(colViewByElement.trim()).toString() + ",t_qtr";
                } else {
                    parentDataQ2 = "select " + elementCols.get(parentEle2.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(parentEle2.trim()).toString() + " from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle2.trim()).toString() + "' " + parentParamFilter + " and t_qtr='?' and secviewby='" + viewByNames.get(parentEle2).toString() + "' group by " + elementCols.get(colViewByElement.trim()).toString() + "," + elementCols.get(parentEle2.trim()).toString() + " order by " + elementCols.get(colViewByElement.trim()).toString() + "," + elementCols.get(parentEle2.trim()).toString();
                }
            } else if (minTimeLevel.equalsIgnoreCase("Year")) {
                if (parentEle.trim().equalsIgnoreCase("-1")) {
                    parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby is null and  t_year='?' " + parentParamFilter + " group by " + elementCols.get(viewByElement.trim()).toString() + ",t_year order by " + elementCols.get(viewByElement.trim()).toString() + ",t_year";
                    if (!parentEle2.trim().equalsIgnoreCase("-1")) {
                        parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(parentEle2.trim()).toString() + "' and  t_year='?' " + parentParamFilter + parentParamFilter2 + " group by " + elementCols.get(viewByElement.trim()).toString() + ",t_year order by " + elementCols.get(viewByElement.trim()).toString() + ",t_year";
                    }
                } else {
                    parentDataQ = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ") from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' " + parentParamFilter + parentParamFilter2 + " and t_year='?' group by " + elementCols.get(viewByElement.trim()).toString() + ",t_year order by " + elementCols.get(viewByElement.trim()).toString() + ",t_year";
                }

                if (parentEle2.trim().equalsIgnoreCase("-1")) {
                    parentDataQ2 = "select " + elementCols.get(colViewByElement.trim()).toString() + ", sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and  t_year='?' and secviewby is null group by " + elementCols.get(colViewByElement.trim()).toString() + ",t_year order by " + elementCols.get(colViewByElement.trim()).toString() + ",t_year";
                } else {
                    parentDataQ2 = "select " + elementCols.get(parentEle2.trim()).toString() + ",sum(" + measureName.trim() + ") ,t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle2.trim()).toString() + "' " + parentParamFilter + parentParamFilter2 + " and t_year='?'  group by " + elementCols.get(parentEle2.trim()).toString() + ",t_year order by " + elementCols.get(parentEle2.trim()).toString() + ",t_year";
                }

                // else if(parentEle.equalsIgnoreCase("-1"))//if(!parentEle.equalsIgnoreCase("-1"))
            }
            // PbReturnObject parentData=pbDb.execSelectSQL(parentDataQ);
            psP = con.prepareStatement(parentDataQ);
            psP.setString(1, startRange );
            rsP = psP.executeQuery();
            PbReturnObject parentData = new PbReturnObject(rsP);
            parentTotals = new HashMap();
            for (int t = 0; t < parentData.getRowCount(); t++) {
                parentTotals.put(parentData.getFieldValueString(t, 0), new Long(Long.parseLong(parentData.getFieldValueString(t, 1))));
            }

            psP = con.prepareStatement(parentDataQ2);
            psP.setString(1, startRange );
            rsP = psP.executeQuery();
            parentData = new PbReturnObject(rsP);

            for (int t = 0; t < parentData.getRowCount(); t++) {
                parentTotals.put(parentData.getFieldValueString(t, 0), new Long(Long.parseLong(parentData.getFieldValueString(t, 1))));
            }
            // to get the restricting total over
            // to display the data
            //to display header
            //colChildElement = collect.getChildElementId(colChildElement,targetId);
//            result = result + "<Tr><Td style=\"width:auto;font-weight:bold;font-size:11px;background-color:#E6E6E6\">" + currVal + "</Td>";
            result.append("<Tr><Td style=\"width:auto;font-weight:bold;font-size:11px;background-color:#E6E6E6\">").append(currVal).append("</Td>");
            if (!colChildElement.equalsIgnoreCase("-1")) {
                for (int z = 0; z < colEdgeVals.size(); z++) {
//                    result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">";
                    result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">");
//                    result += "<a href=\"javascript:submitColumnDrillUrl('" + colDrillUrl + (String) colEdgeVals.get(z) + "')\">" + (String) colEdgeVals.get(z) + "";
                    result.append("<a href=\"javascript:submitColumnDrillUrl('").append(colDrillUrl).append((String) colEdgeVals.get(z)).append("')\">").append((String) colEdgeVals.get(z)).append("");
//                    result += "</Td>";
                    result.append("</Td>");

                }
            } else if (colChildElement.equalsIgnoreCase("-1")) {
                for (int z = 0; z < colEdgeVals.size(); z++) {
//                    result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">";
                    result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">").append((String) colEdgeVals.get(z)).append("</tr>");
//                    result += (String) colEdgeVals.get(z);
//                    result += "</Td>";

                }
            }
            // result = result+"<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Row Total</Td>";
//            result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Restricting Total</Td>";
            result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Restricting Total</Td>");
            // result = result+"<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Difference</Td>";

//            result = result + "</Tr>";
            result.append("</Tr>");

            // display the data table
            disableSaveButtonFlag = true;
            HashMap Combo = new HashMap();
            for (int x = 1; x < paramVals.size(); x++) {
                String pVal = paramVals.get(x).toString();
                long parentTotal = 0;
                if (parentTotals.containsKey(pVal)) {
                    parentTotal = ((Long) parentTotals.get(pVal)).longValue();
                }

                if (disableSaveButtonFlag == false) {
                    if (parentTotal != 0) {
                        disableSaveButtonFlag = true;
                    }
                }

                long rowTotalVal = 0;
                String childElement = collect.getChildElementId(viewByElement, targetId);
                if (rowTotal.containsKey(pVal)) {
                    rowTotalVal = ((Long) rowTotal.get(pVal)).longValue();
                }
//                result = result + "<Tr><Td>";
                result.append("<Tr><Td>");
                if (!childElement.equalsIgnoreCase("-1")) {
//                    result = result + "<a href=\"javascript:submitDrill('" + drllUrl + paramVals.get(x).toString() + "')\">" + paramVals.get(x).toString();
                    result.append("<a href=\"javascript:submitDrill('").append(drllUrl).append(paramVals.get(x).toString()).append("')\">").append(paramVals.get(x).toString());
                    //result=result+paramVals.get(x).toString();
                } else {
//                    result = result + paramVals.get(x).toString();
                    result.append(paramVals.get(x).toString());
                }
//                result = result + "</Td>";
                result.append("</Td>");

                HashMap dt = new HashMap();
                if (allV.containsKey(pVal)) {
                    dt = (HashMap) allV.get(pVal);
                }
                for (int y = 0; y < colEdgeVals.size(); y++) {
                    String timeV = colEdgeVals.get(y).toString();
                    long mV = 0;
                    if (dt.containsKey(timeV)) {
                        mV = ((Long) dt.get(timeV)).longValue();
                    }
                    String value = "";
                    if (mV != 0) {
//                        value = "" + mV + "";
                        value = String.valueOf(mV);
                    }
//                    result = result + "<Td><input readonly  style=\"width:99%\" type=\"text\" id=\"" + paramVals.get(x) + ":" + timeV + "\" name=\"" + paramVals.get(x) + ":" + timeV + "\" value=\"" + value + "\"></Td>";
                    result.append("<Td><input readonly  style=\"width:99%\" type=\"text\" id=\"").append(paramVals.get(x)).append(":").append(timeV).append("\" name=\"").append(paramVals.get(x)).append(":").append(timeV).append("\" value=\"").append(value).append("\"></Td>");

                }
                // result = result+"<Td><input type=\"text\" readonly class=\"displayTotal\" value=\""+rowTotalVal+"\"></Td>" +
                result.append("<Td><input type=\"text\"  style=\"width:99%\" readonly value=\"").append(parentTotal).append("\"></Td>");
                //         "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>";
                result.append("</Tr>");
            }

            // result = result+"<Tr><Td style=\"width:200px\">Column Total</Td>";
//            result = result + "<Tr><Td style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Restricting Total</Td>";
            result.append("<Tr><Td style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Restricting Total</Td>");
            long gTotal = 0;

            for (int p = 0; p < colEdgeVals.size(); p++) {
                String colKey = colEdgeVals.get(p).toString();
                long colTotVal = 0;
                if (parentTotals.containsKey(colKey)) {
                    colTotVal = ((Long) parentTotals.get(colKey)).longValue();
                }
//                result = result + "<Td><input type=\"text\" name='2'  style=\"width:99%\" readonly value=\"" + colTotVal + "\"></Td>";
                result.append("<Td><input type=\"text\" name='2'  style=\"width:99%\" readonly value=\"").append(colTotVal).append("\"></Td>");
                gTotal = gTotal + colTotVal;
            }
            result.append("<Td style=\"width:200px\"><input  style=\"width:99%\" type=\"text\" readonly value=").append(gTotal).append("></Td>");
            //  result=result+"<Td style=\"width:200px\"><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>";
            //  result=result+"<Td style=\"width:200px\"><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>";
//            result = result + "</Tr>";
            result.append("</Tr>");
            // to show the overall target primParamEleId
            String primParamFilter = "";
            String overAllTotQ = "";
            // to put the primary paramteres hierarchy filters

            for (int o = 0; o < reqParams.length; o++) {
                String key = reqParams[o];
                String value = "-1";
                if (key.equalsIgnoreCase(primParamEleId)) {
                    if (targetParametersValues.containsKey(key)) {
                        String val = targetParametersValues.get(key).toString();
                        if (!val.equalsIgnoreCase("All")) {
                            primParamFilter = " and " + elementCols.get(key).toString() + "='" + val + "'";
                        }

                    }
                }
            }
            if (minTimeLevel.equalsIgnoreCase("Day")) // overAllTotQ = "Select "+elementCols.get(primParamEleId.trim()).toString()+",sum("+measureName.trim()+"),to_char(t_date,'dd-mm-yy') from "+targetTable.trim()+" where target_id="+targetId+" and viewby='"+viewByNames.get(primParamEleId.trim()).toString()+"' "+primParamFilter+" group by "+elementCols.get(primParamEleId.trim()).toString()+",to_char(t_date,'dd-mm-yy') order by "+elementCols.get(primParamEleId.trim()).toString()+",to_char(t_date,'dd-mm-yy')";
            {
                overAllTotQ = "Select sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=? and viewby='OverAll' " + primParamFilter + " group by to_char(t_date,'dd-mm-yy') order by to_char(t_date,'dd-mm-yy')";
            } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                overAllTotQ = "Select sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=? and viewby='OverAll' " + primParamFilter + " group by t_month order by t_month";
            } else if (minTimeLevel.equalsIgnoreCase("Quarter") || minTimeLevel.equalsIgnoreCase("Qtr")) {
                overAllTotQ = "Select sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=? and viewby='OverAll' " + primParamFilter + " group by t_qtr order by t_qtr";
            } else if (minTimeLevel.equalsIgnoreCase("Year")) {
                overAllTotQ = "Select sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=?  and viewby='OverAll' " + primParamFilter + " group by t_year order by t_year";
            }

            // PbReturnObject overAllObj = pbDb.execSelectSQL(overAllTotQ);
            RestrictingTotal = parentTotals;
            psO = con.prepareStatement(overAllTotQ);
            psO.setString(2, targetId);
            rsO = psO.executeQuery();
            PbReturnObject overAllObj = new PbReturnObject(rsO);

            primH2 = new HashMap();
            for (int i = 0; i < overAllObj.getRowCount(); i++) {
                oldPrimV = overAllObj.getFieldValueString(i, 0);
                if (newPrimV.equalsIgnoreCase("")) {
                    newPrimV = oldPrimV;
                }
                oldPrimV = overAllObj.getFieldValueString(i, 0);
                String dat = overAllObj.getFieldValueString(i, 0);
                int mValue = overAllObj.getFieldValueInt(i, 1);
                int old = 0;
                if (primH2.containsKey(dat)) {
                    old = ((Integer) primH2.get(dat)).intValue();
                }
                mValue = mValue + old;
                primH2.put(dat, Integer.valueOf(mValue));
            }
            int grandTotal = 0;
            for (int k = 0; k < paramVals.size(); k++) {
                int rTotal = 0;
                if (primH2.containsKey(paramVals.get(k).toString())) {
                    rTotal = ((Integer) primH2.get(paramVals.get(k).toString())).intValue();
                }
                grandTotal = grandTotal + rTotal;
            }

            // overAllMessage=" The Overall Target Set By Primary Parameter '"+viewByNames.get(primParamEleId.trim()).toString()+"' is "+grandTotal;
            secAnalyze = colViewByElement;

        }

        if (secAnalyze.equalsIgnoreCase("Time")) {
            if (showRestrictingTotal == true) {
                if (disableSaveButtonFlag == false) {
                    freezeButton = "disabled";
                }
            }
        } else if (disableSaveButtonFlag == false) {
            freezeButton = "disabled";
        }

        if (errorMessage.length() > 5) {
            freezeButton = "disabled";
        }
        String periodMsg = "";
        if (displayPeriodFlagMessage == true) {
            periodMsg = "As no data is available at '" + minTimeLevel + "'. So no data entry is allowed at '" + periodType + "'. Only modification is allowed";
        }
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (psP != null) {
            psP.close();
        }
        if (rsP != null) {
            rsP.close();
        }
        if (psO != null) {
            psO.close();
        }
        if (rsO != null) {
            rsO.close();
        }
        if (con != null) {
            con.close();
        }
        totalResult.put("displayDataRegion", result);
        totalResult.put("rowEdgeValues", paramVals);
        totalResult.put("colEdgeValues", colEdgeVals);
        totalResult.put("originalResult", originalResult);
        totalResult.put("overAllMessage", overAllMessage);
        totalResult.put("errorMessage", errorMessage);
        totalResult.put("secAnalyze", secAnalyze);
        totalResult.put("primaryAnalyze", primaryAnalyze);
        totalResult.put("startRange", startRange);
        totalResult.put("primParamEleId", primParamEleId);
        totalResult.put("periodType", periodType);
        totalResult.put("minTimeLevel", minTimeLevel);
        totalResult.put("endRange", endRange);
        totalResult.put("endPeriod", endPeriod);
        totalResult.put("startPeriod", startPeriod);
        totalResult.put("dateMeassage", dateMeassage);
        totalResult.put("measureName", measureName);
        totalResult.put("freezeButton", freezeButton);//periodMsg
        totalResult.put("periodMsg", periodMsg);
        totalResult.put("targetStartDate", targetStartDate);
        totalResult.put("targetEndDate", targetEndDate);
        totalResult.put("colDrillUrl", colDrillUrl);
        totalResult.put("basisVal", basisVal);
        totalResult.put("RestrictingTotal", RestrictingTotal);
        totalResult.put("maxTimeLevel", maxTimeLevel);
        return totalResult;
    }

    public ArrayList getDefaultTargetDatesList(String targetId, String minTimeLevel) throws Exception {
        ArrayList defDates = new ArrayList();
        ArrayList allDates = new ArrayList();
        String tDates = "";
        if (minTimeLevel.equalsIgnoreCase("Day")) {
            tDates = "select to_char(target_start_date,'dd/mm/yyyy') s_date, to_char(target_end_date,'dd/mm/yyyy') e_date from target_master where target_id='" + targetId + "'";
        } else if (minTimeLevel.equalsIgnoreCase("Month")) {
            tDates = "select target_start_month s_date, target_end_month e_date from target_master where target_id='" + targetId + "'";
        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
            tDates = "select target_start_qtr s_date, target_end_qtr e_date from target_master where target_id='" + targetId + "'";
        } else if (minTimeLevel.equalsIgnoreCase("Year")) {
            tDates = "select target_start_year s_date, target_end_year e_date from target_master where target_id='" + targetId + "'";
        }

        PbReturnObject dateOb = pbDb.execSelectSQL(tDates);
        String s_date = dateOb.getFieldValueString(0, "S_DATE");
        String e_date = dateOb.getFieldValueString(0, "E_DATE");
        String defaultTimeQuery = "";
        String time2Query = "";
        if (minTimeLevel.equalsIgnoreCase("Day")) {
            defaultTimeQuery = "select  view_by , view_by as view_by1 from ( select distinct to_Char(T.ST_DATE,'dd-MON-yy')  as view_by ,  trunc(T.ST_DATE)  as orderbycol  from  pr_day_denom T  where  ddate between to_date('" + s_date + "','dd/mm/yyyy') and to_date('" + e_date + "','dd/mm/yyyy') ) order by orderbycol";
            time2Query = "select  view_by , view_by as view_by1 from ( select distinct to_Char(T.ST_DATE,'dd-mm-yy')  as view_by ,  trunc(T.ST_DATE)  as orderbycol  from  pr_day_denom T  where  ddate between to_date('" + s_date + "','dd/mm/yyyy') and to_date('" + e_date + "','dd/mm/yyyy') ) order by orderbycol";
            PbReturnObject timeObj = pbDb.execSelectSQL(time2Query);
            for (int t = 0; t < timeObj.getRowCount(); t++) {
                allDates.add(timeObj.getFieldValueString(t, 0));
            }
        } else if (minTimeLevel.equalsIgnoreCase("Month")) {
            defaultTimeQuery = "select mon_name,cmon, cyear from prg_acn_mon_denom where cm_st_date between (select cm_st_date from prg_acn_mon_denom where mon_name='" + s_date + "') and (select cm_end_date from prg_acn_mon_denom where mon_name='" + e_date + "') order by 3,2";
        } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
            defaultTimeQuery = "select DISTINCT cmqtr, cyear from prg_acn_mon_denom where cm_st_date between (select DISTINCT pmq_st_date from prg_acn_mon_denom where pmqtr_code='" + s_date + "') and (select DISTINCT pmq_st_date from prg_acn_mon_denom where pmqtr_code='" + e_date + "') order by 2,1";
        } else if (minTimeLevel.equalsIgnoreCase("Year")) {
            defaultTimeQuery = "select DISTINCT cyear from prg_acn_mon_denom where pyy_start_date between (select DISTINCT pyy_start_date from prg_acn_mon_denom where cyear='" + s_date + "') and (select DISTINCT pyy_start_date from prg_acn_mon_denom where cyear='" + e_date + "') order by 1";
        }

        PbReturnObject defaultTimeOb = pbDb.execSelectSQL(defaultTimeQuery);
        for (int y = 0; y < defaultTimeOb.getRowCount(); y++) {
            if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                defDates.add(defaultTimeOb.getFieldValueString(y, 0) + "-" + defaultTimeOb.getFieldValueString(y, 1));
            } else {
                defDates.add(defaultTimeOb.getFieldValueString(y, 0));
            }
        }

        ////////////////////////////////////////////////////////.println(" defDates "+defDates);
        return defDates;
    }

    public ArrayList getArrayListFromPbReturnObject(PbReturnObject retObj) {
        ArrayList arrayList = new ArrayList();
        String[] columnNames = retObj.getColumnNames();
        for (int k = 0; k < retObj.getRowCount(); k++) {
            arrayList.add(retObj.getFieldValueString(k, columnNames[0]));
        }

        return arrayList;
    }

    //added by susheela
    public HashMap getParentTotal(HashMap viewBy, ArrayList timeDetails) {
        HashMap parentTotal = new HashMap();
        String targetId = tarId;
        String[] a1 = (String[]) (viewBy.keySet()).toArray(new String[0]);
        //String columnEdgeValues = "";
        String viewByElement = "";
        String periodType = "";
        String minimumTimeLevel = "";
        String currVal = "";

        for (int i = 0; i < a1.length; i++) {
            ArrayList a = (ArrayList) viewBy.get(a1[i]);
            if (a.get(0).toString().equalsIgnoreCase("CBOVIEW_BY_NAME")) {
                viewByElement = a.get(3).toString();
                currVal = a.get(4).toString();
            }

            if (a.get(0).toString().equalsIgnoreCase("CBOVIEW_BY_NAME1")) {
                if (a.get(4).toString().equalsIgnoreCase("Time")) {
                    periodType = (String) timeDetails.get(4);
                    minimumTimeLevel = (String) timeDetails.get(0);
                    //sqlstr = getTimeQuery((String)timeDetails.get(0),(String)timeDetails.get(2), (String)timeDetails.get(3));
                }

            }
        }
        return parentTotal;
    }

    boolean ValidateParamValues(Object o) {
        boolean isValid = true;
        if (o == null || o.toString().equals("") || o.toString().equalsIgnoreCase("All")) {
            isValid = false;
        }

        return (isValid);
    }

    String getOracleInClause(Object o) {
        return ("'" + o.toString().replace(",", "','") + "'");
    }

    public String getWhereClause(HashMap hm, ArrayList viewByCols) throws Exception {

        Vector parameterTable = new Vector();
        String ParameterId = null;
        String paramWhereClause = "";
        String selfParamWhereClause = "";
        String sqlstr = "";
        String finalQuery = "";
        {//where Clause Columns
            String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);
            for (int i = 0; i < a1.length; i++) {
                if (ValidateParamValues(hm.get(a1[i]))) {
                    if (ParameterId == null) {
                        ParameterId = (a1[i]).toString();
                    } else {
                        ParameterId += "," + (a1[i]).toString();
                    }
                    /*
                     * if(parameterTable.isEmpty() || (
                     * !parameterTable.contains(a1[i]))){
                     * parameterTable.add(a1[i]);
                     }
                     */
                }
            }
            if (ParameterId != null) {

                sqlstr = resundle.getString("generateViewByQry2");
                Object Obj[] = new Object[1];
                Obj[0] = ParameterId;
                finalQuery = pbDb.buildQuery(sqlstr, Obj);
                PbReturnObject retObj = pbDb.execSelectSQL(finalQuery);
                String colNames[] = retObj.getColumnNames();
                int psize = 0;
                psize = retObj.getRowCount();
                if (psize > 0) {
                    //Looping twice
                    //Loop 1 find the fact and current and prior cols
                    //loop 2 build query

                    for (int i = 0; i < psize; i++) {
                        //ViewByMap.put(retObj.getFieldValue(i, colNames[5]), retObj.getFieldValue(i, colNames[1]) + "." + retObj.getFieldValue(i, colNames[2]));
                        //ViewByMapCol.put(retObj.getFieldValue(i, colNames[5]), retObj.getFieldValue(i, colNames[2]));
                        paramWhereClause += " and " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                        paramWhereClause += " in (" + getOracleInClause(hm.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";
                        if (!(viewByCols == null || viewByCols.size() == 0)) {
                            if (retObj.getFieldValueString(i, colNames[5]).equalsIgnoreCase(viewByCols.get(0).toString())) {

                                selfParamWhereClause += " and " + retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
                                selfParamWhereClause += " in (" + getOracleInClause(hm.get(retObj.getFieldValueString(i, colNames[5]))) + ") ";

                            }
                        }
                        if (parameterTable.isEmpty()) {
                            parameterTable.add(retObj.getFieldValueString(i, colNames[0]));
                        } else if (!parameterTable.contains(retObj.getFieldValueString(i, colNames[0]))) {
                            parameterTable.add(retObj.getFieldValueString(i, colNames[0]));
                        }
                    }
                }
            }
        }
        return paramWhereClause;

    }

    public HashMap getDataViewByStatus(String periodType, String dateValue) throws SQLException, Exception {
        String targetTable = "";
        String tableQ = "select * from prg_grp_buss_table where buss_table_id in( select target_table_id from prg_target_master where"
                + " prg_measure_id in(select measure_id from target_master where target_id=" + tarId + "))";
        PbReturnObject pbro = pbDb.execSelectSQL(tableQ);
        targetTable = pbro.getFieldValueString(0, "BUSS_TABLE_NAME");
        Connection con = ProgenConnection.getInstance().getConnection();
        PreparedStatement ps = null;
        if (periodType.equalsIgnoreCase("year")) {
            ps = con.prepareStatement("select distinct viewby from  ?  where   t_year='?'  and target_id=?");
        } else if (periodType.equalsIgnoreCase("Qtr") || periodType.equalsIgnoreCase("Quarter")) {
            ps = con.prepareStatement("select distinct viewby from  ?    where t_qtr='?' and target_id=?");
        } else if (periodType.equalsIgnoreCase("Month")) {
            ps = con.prepareStatement("select distinct viewby from  ?   where t_month='?' and target_id=?");
        } else if (periodType.equalsIgnoreCase("day")) {
            ps = con.prepareStatement("select distinct viewby from   ?  where t_date='?' and target_id=?");
        }

        ResultSet rs = null;
        HashMap viewByDataMap = new HashMap();
        ps.setString(1, targetTable );
        ps.setString(2, dateValue);
        ps.setString(3, tarId);
        rs = ps.executeQuery();
        PbReturnObject viewByObj = new PbReturnObject(rs);
        ArrayList viewList = new ArrayList();
        for (int m = 0; m < viewByObj.getRowCount(); m++) {
            viewList.add(viewByObj.getFieldValueString(m, "VIEWBY"));
        }
        viewByObj.writeString();

        String eleQ = "select * from prg_target_param_details where target_id=" + tarId;
        PbReturnObject eleObj = pbDb.execSelectSQL(eleQ);
        for (int j = 0; j < eleObj.getRowCount(); j++) {
            if (viewList.contains(eleObj.getFieldValueString(j, "PARAM_DISP_NAME"))) {
                viewByDataMap.put(eleObj.getFieldValueString(j, "ELEMENT_ID"), eleObj.getFieldValueString(j, "PARAM_DISP_NAME"));
            }
        }
        if(ps!=null){
            ps.close();
        }
        if(rs!=null){
            rs.close();
        }
        return viewByDataMap;
    }
}
