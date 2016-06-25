package com.progen.scenario.display;

import com.progen.charts.ProGenStandardCategoryToolTipGenerator;
import com.progen.report.query.PbReportQuery;
import com.progen.scenariodesigner.db.ScenarioTemplateDAO;
import com.progen.scenarion.PbScenarioCollection;
import com.progen.scenarioview.db.PbScenarioParamVals;
import com.progen.scenarioview.db.ScenarioDb;
import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
import prg.ScenarioCal.ScenarioC;
import prg.business.group.BusinessGroupDAO;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.Session;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

public class DisplayScenarioParameters {

    public static Logger logger = Logger.getLogger(DisplayScenarioParameters.class);

    public static HttpServletResponse getResponse() {
        return response;
    }

    public static void setResponse(HttpServletResponse aResponse) {
        response = aResponse;
    }

    public static HttpSession getSession() {
        return session;
    }

    public static void setSession(HttpSession aSession) {
        session = aSession;
    }
    public String ScenarioId;
    public PbScenarioCollection viewerCollect = new PbScenarioCollection();
    public static HashMap normalHM = new HashMap();
    public static HashMap NonViewByMap = new HashMap();
    public static ArrayList NonViewByList = new ArrayList();
    public static PbReturnObject normalRetObj = new PbReturnObject();
    private static HttpServletResponse response;
    private static HttpSession session;
    private Vector Dataset = new Vector();
    private Vector ViewBy = new Vector();
    PbScenarioCollection collect = new PbScenarioCollection();
    ProgenParam f1 = new ProgenParam();
    public int totalParam = 0;
    StringBuilder result =new  StringBuilder();
    PbDb pbDb = new PbDb();
    DisplayScenarioResourceBundle resundle = new DisplayScenarioResourceBundle();
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
            // ////("finalQuery in param is" + finalQuery);
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
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return (sqlstr);
    }

    public String getParameterInfo(String elementID) {
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

                tempPass = retObj.getFieldValue(0, colNames[2]).toString();
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return ("");
    }

    public PbReturnObject getParameterColQuery(HashMap hm, String targetId, String colElementId)  {
            String sqlStr = "";
            PbReturnObject pbro2 = new PbReturnObject();
        try {
            // ////(targetId + " HashMap in displayParamsis getParameterColQuery --" + hm);
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
                    String whereClause = getWhereClause(newHm2, ColViewbyCols);
                    String finalWhereClause = "";
                    if (whereClause.indexOf("and") == 1) {
                        whereClause = whereClause.substring(4);
                        whereClause = " where " + whereClause;
                    }
                    finalWhereClause = whereClause;
                    sqlStr = getParameterQuery(a1[i]) + finalWhereClause;
                }
            }
            pbro2 = f1.getRowEdgesRetObj(sqlStr, colElementId);
            return pbro2;
        } catch (SQLException ex) {
           logger.error("Exception :", ex);
            return pbro2;
        } catch (Exception ex) {
           logger.error("Exception :", ex);
            return pbro2;
        }
    }

    public String getParameterRowQuery(HashMap hm, String targetId, String colElementId)  {
            String sqlStr = "";
//        PbReturnObject pbro2 = new PbReturnObject();
            PbReturnObject finbusColsObj=null;
            PbReturnObject pbro =null;
        try {
            String childsQuery = "select * from prg_target_param_details where target_id=" + targetId + " order by dim_id, rel_level";
            String busColsQ = "select DISTINCT element_id, buss_col_name from prg_user_all_info_details where folder_id "
                    + " in(select bus_folder_id from target_measure_folder where "
                    + " bus_group_id=(select business_group from prg_target_master where prg_measure_id "
                    + " in(select measure_id from target_master WHERE target_master.target_id=" + targetId + ")))";
            finbusColsObj = pbDb.execSelectSQL(busColsQ);
            HashMap busCols = new HashMap();
            for (int l = 0; l < finbusColsObj.getRowCount(); l++) {
                busCols.put(finbusColsObj.getFieldValueString(l, "ELEMENT_ID"), finbusColsObj.getFieldValueString(l, "BUSS_COL_NAME"));
            }
            
            pbro = pbDb.execSelectSQL(childsQuery);
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
                    
                    String whereClause = getWhereClause(newHm2, ColViewbyCols);
                    String finalWhereClause = "";
                    if (whereClause.indexOf("and") == 1) {
                        whereClause = whereClause.substring(4);
                        whereClause = " where " + whereClause;
                    }
                    finalWhereClause = whereClause;
                    sqlStr = getParameterQuery(a1[i]) + finalWhereClause;
                }
            }
            return sqlStr;
        } catch (Exception ex) {
           return sqlStr;
        }
    }

    public String getPeriodTypeQuery(String key, String val) {
        String value = val;
        String str = "";
        if (key.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
            if (value.equalsIgnoreCase("Day")) {
                str = "Select VALUE, VALUE from PRG_VALUE_LOOKUP where KEY_VALUE = 'Daily' order by 2";
            } else if (value.equalsIgnoreCase("Month")) {
                //str = "Select VALUE, VALUE from PRG_VALUE_LOOKUP where KEY_VALUE = 'Monthly' order by 2";
                str = "Select 'Month' VALUE, 'Month' VALUE from dual order by 2";
            } else if (value.equalsIgnoreCase("Quarter")) {
                str = "Select VALUE, VALUE from PRG_VALUE_LOOKUP where KEY_VALUE = 'Quarterly' order by 2";
            } else if (value.equalsIgnoreCase("Year")) {
                str = "Select VALUE, VALUE from PRG_VALUE_LOOKUP where KEY_VALUE = 'Yearly' order by 2";
            }
        }
        return str;
    }

    public String getMonthsQry(String key, ArrayList timeDetailsArray, String flag) {

        String startMonth = "";
        String endMonth = "";
        if (flag.equalsIgnoreCase("compareScenario")) {
            startMonth = (String) timeDetailsArray.get(2);
            endMonth = (String) timeDetailsArray.get(3);
        } else {
            startMonth = (String) timeDetailsArray.get(0);
            endMonth = (String) timeDetailsArray.get(1);
        }

        String str = "";
        if (key.equalsIgnoreCase("AS_OF_MONTH") || key.equalsIgnoreCase("AS_OF_MONTH1")) {
            str = "select MON_NAME as view_by, MON_NAME as view_by1  from PRG_ACN_MON_DENOM "
                    + "where cm_st_date between (select distinct cm_st_date from prg_acn_mon_denom where mon_name='" + startMonth + "') and "
                    + "(select distinct cm_end_date from prg_acn_mon_denom where mon_name='" + endMonth + "') order by cyear,cmon";
        }
        return str;
    }

    public String getAllMonthsQry(String key) {
        String str = "";
        if (key.equalsIgnoreCase("AS_OF_MONTH") || key.equalsIgnoreCase("AS_OF_MONTH1")) {
            str = "select MON_NAME as view_by, MON_NAME as view_by1  from PRG_ACN_MON_DENOM order by cyear,cmon";
        }
        return str;
    }

    public String getQtrsQry(String key) {
        collect.scenarioId = ScenarioId;
        ArrayList getQtrs = collect.getSelectedQtrs();
        String targetStartQtr = (String) getQtrs.get(0);
        String targetEndQtr = (String) getQtrs.get(1);
        String str = "";
        if (key.equalsIgnoreCase("AS_OF_QTR") || key.equalsIgnoreCase("AS_OF_QTR1")) {
            str = "select distinct cmqtr_code as view_by,cmqtr_code as view_by1,cyear from prg_acn_mon_denom "
                    + "where cmq_st_date between (select distinct cmq_st_date from prg_acn_mon_denom where cmqtr_code='" + targetStartQtr + "') and "
                    + " (select distinct cmq_st_date from prg_acn_mon_denom where cmqtr_code='" + targetEndQtr + "') order by cyear,cmqtr_code";
        }
        return str;
    }

    public String getYearsQry(String key) {
        collect.scenarioId = ScenarioId;
        ArrayList getYears = collect.getSelectedYears();
        String targetStartYear = (String) getYears.get(0);
        String targetEndYear = (String) getYears.get(1);
        String str = "";
        if (key.equalsIgnoreCase("AS_OF_YEAR") || key.equalsIgnoreCase("AS_OF_YEAR1")) {
            str = "select distinct cyear as view_by,cyear as view_by1  from PR_DAY_DENOM where cyear between " + targetStartYear + "  "
                    + " and " + targetEndYear + "  order by cyear";
        }
        return str;
    }

    public String getYearsQry(String key, ArrayList timeDetailsArray, String flag) {
        String targetStartYear = "";
        String targetEndYear = "";
        if (flag.equalsIgnoreCase("compareScenario")) {
            targetStartYear = (String) timeDetailsArray.get(2);
            targetEndYear = (String) timeDetailsArray.get(3);
        } else {
            targetStartYear = (String) timeDetailsArray.get(0);
            targetEndYear = (String) timeDetailsArray.get(1);
        }
        String str = "";
        if (key.equalsIgnoreCase("AS_OF_YEAR") || key.equalsIgnoreCase("AS_OF_YEAR1")) {
            str = "select distinct cyear as view_by,cyear as view_by1  from PR_DAY_DENOM where cyear between " + targetStartYear + "  "
                    + " and " + targetEndYear + "  order by cyear";
        }
        return str;
    }

    public String getYearsQry2(String key, String scenarioId) {
        collect.scenarioId = scenarioId;
        ArrayList getYears = collect.getSelectedYears2(scenarioId);
        String targetStartYear = (String) getYears.get(0);
        String targetEndYear = (String) getYears.get(1);
        String str = "";
        if (key.equalsIgnoreCase("AS_OF_YEAR") || key.equalsIgnoreCase("AS_OF_YEAR1")) {
            str = "select distinct cyear as view_by,cyear as view_by1  from PR_DAY_DENOM where cyear between " + targetStartYear + "  "
                    + " and " + targetEndYear + "  order by cyear";
        }
        return str;
    }

    public String displayTimeParams(HashMap hm, ArrayList timeDetailsArray, String flag) throws SQLException, Exception {
        StringBuilder result2 = new StringBuilder();
        result2.append("<Table width=\"100%\">");
        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);
        this.totalParam += a1.length;
        int i = 0;

        for (i = 0; i < a1.length; i++) {

            if (j == 0) {
                result2.append("<Tr>");
            } else if (j % 4 == 0) {
                result2.append("</Tr><Tr>");
            }

            ArrayList a = (ArrayList) hm.get(a1[i]);
            if (a1[i].equalsIgnoreCase("AS_OF_MONTH") || a1[i].equalsIgnoreCase("AS_OF_MONTH1")) {
                result2.append(" <Td id=\"column").append(i).append("\" align=\"right\"> ").append(f1.getQueryCombotb2(a.get(1).toString(), (String) a.get(7), getMonthsQry(a1[i], timeDetailsArray, flag), (String) a.get(0))).append(" </Td> ");
            } else if (a1[i].equalsIgnoreCase("AS_OF_YEAR") || a1[i].equalsIgnoreCase("AS_OF_YEAR1")) {
                result2.append(" <Td id=\"column").append(i).append("\" align=\"right\"> ").append(f1.getQueryCombotb2(a.get(1).toString(), a.get(2).toString(), getYearsQry(a1[i], timeDetailsArray, flag), (String) a.get(0))).append(" </Td> ");
            } else if (a1[i].equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                result2.append(" <Td id=\"column").append(i).append("\" align=\"right\"> ").append(f1.getQueryCombotb2(a.get(1).toString(), (String) a.get(7), getPeriodTypeQuery(a1[i], "Month"), (String) a.get(0))).append(" </Td> ");
            }
            j++;
        }
        return result2.toString();
    }

    public String displayTimeParams2(HashMap hm, ArrayList timeDetailsArray, String flag, String scenarioId) throws SQLException, Exception {
        
         StringBuilder result2 = new StringBuilder();
        result2 .append("<Table width=\"100%\">");
        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);
        this.totalParam += a1.length;
        int i = 0;

        for (i = 0; i < a1.length; i++) {

            if (j == 0) {
                result2.append("<Tr>");
            } else if (j % 4 == 0) {
                result2.append("</Tr><Tr>");
            }

            ArrayList a = (ArrayList) hm.get(a1[i]);
            if (a1[i].equalsIgnoreCase("AS_OF_MONTH") || a1[i].equalsIgnoreCase("AS_OF_MONTH1")) {
                result2.append("<Td id=\"column").append(i).append("\" align=\"right\"> ").append(f1.getQueryCombotb2(a.get(1).toString(), (String) a.get(7), getMonthsQry(a1[i], timeDetailsArray, flag), (String) a.get(0))).append(" </Td> ");
            } else if (a1[i].equalsIgnoreCase("AS_OF_YEAR") || a1[i].equalsIgnoreCase("AS_OF_YEAR1")) {
                result2.append(" <Td id=\"column").append(i).append("\" align=\"right\"> ").append(f1.getQueryCombotb2(a.get(1).toString(), a.get(2).toString(), getYearsQry2(a1[i], scenarioId), (String) a.get(0))).append(" </Td> ");
            } else if (a1[i].equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                result2.append(" <Td id=\"column").append(i).append("\" align=\"right\"> ").append(f1.getQueryCombotb2(a.get(1).toString(), (String) a.get(7), getPeriodTypeQuery(a1[i], "Month"), (String) a.get(0))).append(" </Td> ");
            }
            j++;
        }
        return result2.toString();
    }

    public String displayParams(HashMap hm, String scenarioId) throws SQLException, Exception {
        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);

StringBuilder result2 = new StringBuilder();
        for (int i = 0; i < a1.length; i++) {
            ArrayList a = (ArrayList) hm.get(a1[i]);
            String elementId = a.get(0).toString();

            f1.elementId = elementId;
//            ArrayList childElements = new ArrayList();
//            ArrayList ColViewbyCols = new ArrayList();

            String finalWhereClause = "";

            if (j % 4 == 0) {
                result2.append("</Tr><Tr>");
            }

            if (a.get(5).toString().equalsIgnoreCase("SingleSelectBox(With All)")) {
                result2.append(" <Td id=\"column").append(i).append("\" align=\"right\"> ").append(f1.getQueryAllCombotb(a.get(9).toString(), (String) a.get(1), getParameterQuery(a1[i]) + finalWhereClause, (String) a.get(8))).append(" </Td> ");
            } else {
                result2.append(" <Td id=\"column").append(i).append("\" align=\"right\"> ").append(f1.getMultiTextBoxNew(a.get(9).toString(), (String) a.get(1), (String) a.get(8), a1[i], (String) a.get(10))).append(" </Td> ");
            }
            j++;
        }
        return result2.toString();
    }

    public String displayViewBys(HashMap hm, HashMap ParameterMap, LinkedHashMap scenarioModels, String flag) throws SQLException {
        collect.scenarioId = ScenarioId;
      
        StringBuilder result2 = new StringBuilder();
        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);
        this.totalParam += a1.length;
        String eleIdViewBy1 = "";
        String eleIdViewBy2 = "";
        int i = 0;
        for (i = 0; i < a1.length; i++) {
            if (j % 4 == 0) {
                result2.append("</Tr><Tr>");
            }
            ArrayList a = (ArrayList) hm.get(a1[i]);
            if (((String) a.get(0)).equalsIgnoreCase("CBOVIEW_BY_NAME")) {
                result2.append( " <Td id=\"column" + i + "\" align=\"right\"> " + f1.getQueryCombotb4ForScenario(a.get(1).toString(), (String) a.get(2), collect.setScenarioViewByQuery((String) a.get(0), hm, (String) a.get(3), ParameterMap, flag), (String) a.get(3), ParameterMap) + " </Td> ");
                eleIdViewBy1 = a.get(3).toString();
            }
            if (((String) a.get(0)).equalsIgnoreCase("CBOVIEW_BY_NAME1")) {
                eleIdViewBy2 = a.get(3).toString();
                result2.append(" <Td id=\"column").append(i).append("\" align=\"right\"> ").append(f1.getQueryCombotb3ForScenario(a.get(1).toString(), (String) a.get(2), collect.setScenarioViewByQuery((String) a.get(0), hm, (String) a.get(3), scenarioModels, flag), (String) a.get(3))).append( " </Td> ");
            }
            j++;
        }

        if (flag.equalsIgnoreCase("analyzeScenario")) {
            result2.append("<Td><input class=\"navtitle-hover\" name=\"Submit\" id=\"goButton\" type=\"Button\"  value=\"    Go    \"  onclick=\"submitform2()\"></Td>");
            result2.append("<Td><input class=\"navtitle-hover\" name=\"budgetingModels\" id=\"budgetingModels\" type=\"Button\"  value=\"  Budgeting Models  \"  onclick=\"return createCustomModel()\"></Td>");
            result2.append("<Td><input class=\"navtitle-hover\" name=\"saveForNow\" id=\"saveForNow\" type=\"Button\"  value=\"   Save For Now  \"  onclick=\"saveForNowFun()\"></Td>");
        } else if (flag.equalsIgnoreCase("compareScenario")) {
            result2.append( "<Td><input class=\"navtitle-hover\" name=\"Submit\" type=\"Button\"  value=\"    Go    \"  onclick=\"submitform3()\"></Td>");
        } else {
            result2.append( "<Td><input class=\"navtitle-hover\" name=\"Submit\" type=\"Button\"  value=\"    Go    \"  onclick=\"submitform()\"></Td>");
        }
        result2.append("</Tr></Table>");

        return result2.toString();

    }

    public String displayParam(ArrayList elementList) {
//        String result1 = "<Table>";
        StringBuilder result1 = new StringBuilder(400);
        result1.append("<Table>");
        int i;
        for (i = 0; i < elementList.size(); i++) {
            if ((i + 1) % 4 == 1) {
//                result1 += "<Tr>";
                result1.append("<Tr>");
            }
            getParameterInfo(elementList.get(i).toString());
            // result1 += "<Td>" + f1.getMultiTextBoxNew("CBOAR" + elementList.get(i).toString(), this.tempPass, "All", elementList.get(i).toString(),(String)a.get(10)) + "</Td>";

            if ((i + 1) % 4 == 0) {
//                result1 += "</Tr>";
                result1.append("</Tr>");
            }
        }
        if ((i) % 4 != 0) {
//            result1 += "</Tr>";
            result1.append("</Tr>");
        }
//        result1 += "</Table>";
        result1.append("</Table>");
        return (result1.toString());
    }

    public String displayViewBysForView(HashMap hm, HashMap ParameterMap) {
        collect.setScenarioId(ScenarioId);
//        String result1 = "";
        StringBuilder result1 = new StringBuilder(400);
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
                eleIdViewBy1 = a.get(3).toString();
            }
            if (((String) a.get(0)).equalsIgnoreCase("CBOVIEW_BY_NAME1")) {
                eleIdViewBy2 = a.get(3).toString();
//              result1 += "<Td><input name=\"Submit\" type=\"Button\"  value=\"    Go    \"  onclick=\"submitformView()\"></Td>";
                result1.append("<Td><input name='Submit' type='Button'  value='Go'  onclick='submitformView()'></Td>");
            }

            j++;
        }
        if (j % 4 != 0) {
//            result1 += "</Tr>";
            result1.append("</Tr>");
        }
//        result1 += "";
        result1.append("");
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
            } else if (periodType.equalsIgnoreCase("MONTH")) {
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

            //  ////////////////////////////////////////////////////////////////////.println("Inside day time range "+sqlstr);
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
            // ////////////////////////////////////////////////////////////////////.println("Inside month time range "+sqlstr);
        } else if (minTimeLEvel.equalsIgnoreCase("Quarter")) {
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

    public HashMap displayDataRegionForView(HashMap viewBy, ArrayList timeDetails, HashMap targetParametersValues, String drllUrl, String colDrillUrl, HashMap targetParameters) throws Exception {
//        HashMap paramValues = collect.scenarioParametersValues;
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

        String targetId = ScenarioId;
        rowQry = RowEdgeQuery(viewBy);
        colQry = ColumnEdgeQuery(viewBy, timeDetails);
        String targetStartDate = "";
        String targetEndDate = "";
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

        for (int i = 0; i < pbro.getRowCount(); i++) {
            elementCols.put(pbro.getFieldValueString(i, "ELEMENT_ID"), pbro.getFieldValueString(i, "BUSS_COL_NAME"));
        }
        for (int m = 0; m < pbroDispNames.getRowCount(); m++) {
            viewByNames.put(pbroDispNames.getFieldValueString(m, "ELEMENT_ID"), pbroDispNames.getFieldValueString(m, "PARAM_DISP_NAME"));
        }
        String getTargetTable = resundle.getString("getTargetTable");
        String fingetTargetTable = pbDb.buildQuery(getTargetTable, tarOb);
        PbReturnObject pbroTab = pbDb.execSelectSQL(fingetTargetTable);
        String measureName2 = pbroTab.getFieldValueString(0, "MEASURE_NAME");
        String measureName = measureName2.trim().replace(" ", "_");
        String targetTable = pbroTab.getFieldValueString(0, "BUSS_TABLE_NAME");

        String[] a1 = (String[]) (viewBy.keySet()).toArray(new String[0]);
        String rowViewByElement = "";
        String viewByElement = "";
        String colViewByElement = "";
        String colCurrVal = "";
        String periodType = "";
        String minimumTimeLevel = "";
        String currVal = "";
//        String dataQuery = "";
        StringBuilder dataQuery = new StringBuilder(300);
        String overAllMessage = "";
        String errorMessage = "";


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
                    colViewByElement = a.get(3).toString();
                    colCurrVal = a.get(4).toString();
                } else {
                    colViewByElement = a.get(3).toString();
                    colCurrVal = a.get(4).toString();
                }
            }
        }
        rowViewByElement = viewByElement;
//        HashMap AllV = new HashMap();
//        HashMap allVa = new HashMap();
        HashMap primH2 = new HashMap();
        String oldPrimV = "";
        String newPrimV = "";
        ArrayList paramVals = new ArrayList();

        ArrayList colEdgeVals = new ArrayList();
        primaryAnalyze = viewByElement.trim();
        f1.elementId = viewByElement.trim();
        paramVals = f1.getRowEdges(rowQry, currVal);
        HashMap allV = new HashMap();
        String oldPv = "";
        String newPv = "";
        HashMap primH = new HashMap();
//        String rowCurrVal = "";
        HashMap allParamHashMap = targetParametersValues;
//        String allParametrsFilterClause = "";
        StringBuilder allParametrsFilterClause = new StringBuilder();
        String[] keys = (String[]) (allParamHashMap.keySet()).toArray(new String[0]);
        String a = "";
        paramVals = new ArrayList();
        for (int i = 0; i < keys.length; i++) {
            a = (String) allParamHashMap.get(keys[i]);

            if (rowViewByElement.equalsIgnoreCase(keys[i]) && a.equalsIgnoreCase("All")) {
                String primQuery = getParameterRowQuery(targetParameters, targetId, viewByElement);
                paramVals = f1.getRowEdges(primQuery, currVal);
            } else if (rowViewByElement.equalsIgnoreCase(keys[i]) && !(a.equalsIgnoreCase("All"))) {
                paramVals.add(currVal);
                paramVals.add(a);
            }
            if (!a.equalsIgnoreCase("All")) {
                allParametrsFilterClause.append(" and ").append(elementCols.get(keys[i])).append("='").append(a).append("'");
//                allParametrsFilterClause = allParametrsFilterClause + " and " + elementCols.get(keys[i]) + "='" + a + "'";
            }
        }

        PbReturnObject secViewBy = new PbReturnObject();
        boolean flagToDisplayCrossTab = false;
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

//        boolean flagForRestrictingTotal = false;
        if (flagToDisplayCrossTab == false) {
            if (periodType.equalsIgnoreCase("Day")) {
                dataQuery.append("select ").append(elementCols.get(viewByElement.trim()).toString()).append(",sum(").append(measureName.trim()).append("),to_char(t_date,'dd-mm-yy') from ").append(targetTable.trim()).append(" where target_id='").append(targetId).append("' and viewby='").append(viewByNames.get(viewByElement.trim()).toString()).append("' ").append(allParametrsFilterClause).append(" group by ").append(elementCols.get(viewByElement.trim()).toString()).append(",to_char(t_date,'dd-mm-yy') order by ").append(elementCols.get(viewByElement.trim()).toString()).append(",to_char(t_date,'dd-mm-yy')");
//                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + ",to_char(t_date,'dd-mm-yy') order by " + elementCols.get(viewByElement.trim()).toString() + ",to_char(t_date,'dd-mm-yy')";
            } else if (periodType.equalsIgnoreCase("Month")) {
//                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "'" + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + ",t_month order by " + elementCols.get(viewByElement.trim()).toString() + ",t_month";
            } else if (periodType.equalsIgnoreCase("Quarter")) {
                dataQuery.append("select ").append(elementCols.get(viewByElement.trim()).toString()).append(",sum(").append(measureName.trim()).append("),t_qtr from ").append(targetTable.trim()).append(" where target_id='").append(targetId).append("' ").append(allParametrsFilterClause).append(" ");
//                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id='" + targetId + "' " + allParametrsFilterClause + " "
                dataQuery.append(" and viewby='").append(viewByNames.get(viewByElement.trim()).toString()).append("' group by ").append(elementCols.get(viewByElement.trim()).toString()).append(" ,");
//                        + " and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' group by " + elementCols.get(viewByElement.trim()).toString() + ","
                dataQuery.append(" t_qtr order by ").append(elementCols.get(viewByElement.trim()).toString()).append(",t_qtr");
//                 + " t_qtr order by " + elementCols.get(viewByElement.trim()).toString() + ",t_qtr";
            } else if (periodType.equalsIgnoreCase("Year")) {
                dataQuery.append("select ").append(elementCols.get(viewByElement.trim()).toString()).append(",sum(").append(measureName.trim()).append("),t_year from ").append(targetTable.trim()).append(" where target_id='").append(targetId).append("' and viewby='").append(viewByNames.get(viewByElement.trim()).toString()).append("' ").append(allParametrsFilterClause).append(" group by ").append(elementCols.get(viewByElement.trim()).toString()).append(",t_year order by ").append(elementCols.get(viewByElement.trim()).toString()).append(",t_year");
//                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + ",t_year order by " + elementCols.get(viewByElement.trim()).toString() + ",t_year";
            }

            ps = con.prepareStatement(dataQuery.toString());
            rs = ps.executeQuery();
            PbReturnObject allD = new PbReturnObject(rs);

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
                int mValue = allD.getFieldValueInt(n, 1);
                primH.put(dat, Integer.valueOf(mValue));
                if (n == (allD.getRowCount() - 1)) {
                    allV.put(oldPv, primH);
                }
            }
            PbReturnObject time = pbDb.execSelectSQL(colQry);
            colEdgeVals = new ArrayList();
            for (int y = 0; y < time.getRowCount(); y++) {
                colEdgeVals.add(time.getFieldValueString(y, 0));
                if (y == 0) {
                    startPeriod = time.getFieldValueString(y, 0);
                }
                if (y == time.getRowCount() - 1) {
                    endPeriod = time.getFieldValueString(y, 0);
                }
            }

            for (int x = 1; x < paramVals.size(); x++) {
                String pVal = paramVals.get(x).toString();
                HashMap dt = new HashMap();
                if (allV.containsKey(pVal)) {
                    dt = (HashMap) allV.get(pVal);
                }
                for (int y = 0; y < time.getRowCount(); y++) {
                    String timeV = time.getFieldValueString(y, 0);
                    int mV = 0;
                    if (dt.containsKey(timeV)) {
                        mV = ((Integer) dt.get(timeV)).intValue();
                    }
                    String value = "";
                    if (mV != 0) {
//                        value = "" + mV + "";
                        value = String.valueOf( mV );
                    }
                    originalResult.put(paramVals.get(x) + ":" + timeV, Integer.valueOf(mV));
                }
            }

            HashMap rowTotal = new HashMap();
            HashMap colTotal = new HashMap();
            String[] originalValsKeys = (String[]) (originalResult.keySet()).toArray(new String[0]);
            for (int m = 0; m < originalValsKeys.length; m++) {
                String totalKey = originalValsKeys[m];
                String rowColKey = "";
                if (originalResult.containsKey(totalKey)) {
                    rowColKey = totalKey;//(String)originalResult.get(totalKey);
                    int oriVal = 0;
                    oriVal = ((Integer) originalResult.get(totalKey)).intValue();
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
                        int al = ((Integer) rowTotal.get(rowKey)).intValue();
                        al = oriVal + al;
                        rowTotal.put(rowKey, Integer.valueOf(al));
                    } else {
                        rowTotal.put(rowKey, Integer.valueOf(oriVal));
                    }

                    if (colTotal.containsKey(colKey)) {
                        int colAl = ((Integer) colTotal.get(colKey)).intValue();
                        colAl = oriVal + colAl;
                        colTotal.put(colKey, Integer.valueOf(colAl));
                    } else {
                        colTotal.put(colKey, Integer.valueOf(oriVal));
                    }

                }
            }

            String childElement = "";//collect.getChildElementId(viewByElement,targetId);

            //column Header display
//            result = result + "<Tr><Td style=\"width:200px;font-weight:bold;font-size:11px;background-color:#E6E6E6\">" + currVal + "</Td>";
            result.append( "<Tr><Td style=\"width:200px;font-weight:bold;font-size:11px;background-color:#E6E6E6\">").append(currVal ).append("</Td>");
            for (int j = 0; j < time.getRowCount(); j++) {
//                result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">").a + time.getFieldValueString(j, 0) + "</Td>";
                result.append( "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">").append(time.getFieldValueString(j, 0) ).append( "</Td>");
            }
//            result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Row Total</Td>";
            result.append( "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Row Total</Td></tr>");
//            result = result + "</Tr>";

            // display the rowedgevalues
            for (int x = 1; x < paramVals.size(); x++) {
                String pVal = paramVals.get(x).toString();
                int rowTotalVal = 0;
                if (rowTotal.containsKey(pVal)) {
                    rowTotalVal = ((Integer) rowTotal.get(pVal)).intValue();
                }
                if (!childElement.equalsIgnoreCase("-1")) {
//                    result = result + "<Tr>" + "<Td style=\"width:200px\"><a href=\"javascript:submitDrill('" + drllUrl + paramVals.get(x).toString() + "')\">" + paramVals.get(x).toString() + "</Td>";
                    result.append("<Tr>" + "<Td style=\"width:200px\"><a href=\"javascript:submitDrill('").append( drllUrl).append( paramVals.get(x).toString() ).append( "')\">").append( paramVals.get(x).toString() ).append("</Td>");
                } else if (childElement.equalsIgnoreCase("-1")) {
//                    result = result + "<Tr>" + "<Td style=\"width:200px\">" + paramVals.get(x).toString() + "</Td>";
                    result.append("<Tr><Td style=\"width:200px\">").append(paramVals.get(x).toString() ).append("</Td>");
                }

                HashMap dt = new HashMap();
                if (allV.containsKey(pVal)) {
                    dt = (HashMap) allV.get(pVal);
                }
                for (int y = 0; y < time.getRowCount(); y++) {
                    String timeV = time.getFieldValueString(y, 0);
                    int mV = 0;
                    if (dt.containsKey(timeV)) {
                        mV = ((Integer) dt.get(timeV)).intValue();

                    }

                    String value = "";
                    if (mV != 0) {
                      value = String.valueOf( mV );
                    }
                    // originalResult.put(paramVals.get(x)+":"+timeV,Integer.valueOf(mV));
//                    result = result + "<Td><input onkeypress=\"return isNumberKey(event)\" type=\"text\" id=\"" + paramVals.get(x) + ":" + timeV + "\" onkeyup=\"autoSum('" + paramVals.get(x) + ":" + timeV + "')\" readonly name=\"" + paramVals.get(x) + ":" + timeV + "\" value=\"" + value + "\"></Td>";
                    result.append("<Td><input onkeypress=\"return isNumberKey(event)\" type=\"text\" id=\"" ).append( paramVals.get(x) ).append( ":"  ).append( timeV  ).append("\" onkeyup=\"autoSum('"  ).append( paramVals.get(x)  ).append( ":"  ).append(timeV  ).append( "')\" readonly name=\""  ).append( paramVals.get(x)  ).append( ":"  ).append( timeV  ).append( "\" value=\""  ).append( value  ).append( "\"></Td>");

                }
//                result = result + "<Td><input class=\"displayTotal\" readonly value=\"" + rowTotalVal + "\"></Td>";
                result.append( "<Td><input class=\"displayTotal\" readonly value=\""  ).append( rowTotalVal  ).append( "\"></Td></tr>");
//                result = result + "</Tr>";
            }
//            result = result + "<Tr><Td style=\"width:200px\">Column Total</Td>";
            result.append("<Tr><Td style=\"width:200px\">Column Total</Td>");
            // to display the column total

            for (int p = 0; p < colEdgeVals.size(); p++) {
                String colKey = colEdgeVals.get(p).toString();
                int colTotVal = 0;
                if (colTotal.containsKey(colKey)) {
                    colTotVal = ((Integer) colTotal.get(colKey)).intValue();
                }
//                result = result + "<Td><input type=\"text\" class=\"displayTotal\" readonly value=\"" + colTotVal + "\"></Td>";
                result.append("<Td><input type=\"text\" class=\"displayTotal\" readonly value=\""  ).append( colTotVal  ).append( "\"></Td>");
            }
//            result = result + "<Td><input type=\"text\" class=\"displayTotal\" readonly id=\"GrandTotal\" value=\"0\"></Td></Tr>";
//            result.append("<Td><input type=\"text\" class=\"displayTotal\" readonly id=\"GrandTotal\" value=\"0\"></Td></Tr>");

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

            // for appending the non ALl values of any element ids from the request parameters
//            String parentsFilter = "";
            StringBuilder parentsFilter = new StringBuilder();
            for (int i = 0; i < keys.length; i++) {
                a = (String) allParamHashMap.get(keys[i]);
                if (viewByParentsList.contains(keys[i])) {
                    if (!a.equalsIgnoreCase("All")) {
//                        parentsFilter = parentsFilter + " and " + elementCols.get(keys[i]) + "='" + a + "'";
                        parentsFilter.append( " and " ).append( elementCols.get(keys[i]) ).append( "='" ).append( a ).append( "'");
                    }
                }
            }
            String getParentElementId = resundle.getString("getParentElementId");
            String fingetParentElementId = pbDb.buildQuery(getParentElementId, tarObj1);
            PbReturnObject allElementInfoOb = pbDb.execSelectSQL(fingetParentElementId);
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
            // to make the parent query for tables
            String startQuery = "select distinct " + elementCols.get(viewByElement.trim()).toString() + " " + parentBussCol;
            String middleClause = "";
//            String endClause = "";
            String parentQuery = "";
            BusinessGroupDAO bgDao = new BusinessGroupDAO();
            middleClause = bgDao.viewBussDataWithouCol(bussTable);
            parentQuery = startQuery + " " + middleClause;
            f1.elementId = viewByElement;
//            PbReturnObject parentDataOb = f1.getParentData(parentQuery);

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
                ArrayList allParents = new ArrayList();

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
                if (periodType.equalsIgnoreCase("Day")) {
                    if (!parentEle.equalsIgnoreCase("-1")) {
                        parentDataQ = "select " + elementCols.get(parentEle.trim()).toString() + ",sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + " group by " + elementCols.get(parentEle.trim()).toString() + ",to_char(t_date,'dd-mm-yy') order by " + elementCols.get(parentEle.trim()).toString() + ",to_char(t_date,'dd-mm-yy')";
                    } else {
                        parentDataQ = "select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + " group by " + elementCols.get(primParamEleId.trim()).toString() + ",to_char(t_date,'dd-mm-yy') order by " + elementCols.get(primParamEleId.trim()).toString() + ",to_char(t_date,'dd-mm-yy')";
                    }
                } // get the data for parent parameter
                else if (periodType.equalsIgnoreCase("Month")) {
                    if (!parentEle.equalsIgnoreCase("-1")) {
                        parentDataQ = "select " + elementCols.get(parentEle.trim()).toString() + ",sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + " group by " + elementCols.get(parentEle.trim()).toString() + ",t_month order by " + elementCols.get(parentEle.trim()).toString() + ",t_month ";
                    } else {
                        parentDataQ = "select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + " group by " + elementCols.get(primParamEleId.trim()).toString() + ",t_month order by " + elementCols.get(primParamEleId.trim()).toString() + ",month";
                    }
                } else if (periodType.equalsIgnoreCase("Quarter")) {
                    if (!parentEle.equalsIgnoreCase("-1")) {
                        parentDataQ = "select " + elementCols.get(parentEle.trim()).toString() + ",sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + " group by " + elementCols.get(parentEle.trim()).toString() + ",t_qtr order by " + elementCols.get(parentEle.trim()).toString() + ",t_qtr ";
                    } else {
                        parentDataQ = "select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + "group by " + elementCols.get(primParamEleId.trim()).toString() + ",t_qtr order by " + elementCols.get(primParamEleId.trim()).toString() + ",t_qtr ";
                    }
                } else if (periodType.equalsIgnoreCase("Year")) {
                    if (!parentEle.equalsIgnoreCase("-1")) {
                        parentDataQ = "select " + elementCols.get(parentEle.trim()).toString() + ",sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + " group by " + elementCols.get(parentEle.trim()).toString() + ",t_year order by " + elementCols.get(parentEle.trim()).toString() + ",t_year";
                    } else {
                        parentDataQ = "select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=" + targetId + " and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' " + parentParamFilter + " " + parentsFilter + " group by " + elementCols.get(primParamEleId.trim()).toString() + ",t_year order by " + elementCols.get(primParamEleId.trim()).toString() + ",t_year ";
                    }
                }
                //PbReturnObject parentTotObj = pbDb.execSelectSQL(parentDataQ);
                psP = con.prepareStatement(parentDataQ);
                rsP = psP.executeQuery();
                PbReturnObject parentTotObj = new PbReturnObject(rsP);
                // PreparedStatement psP = con.

                primH2 = new HashMap();
                for (int i = 0; i < parentTotObj.getRowCount(); i++) {
                    oldPrimV = parentTotObj.getFieldValueString(i, 0);
                    if (newPrimV.equalsIgnoreCase("")) {
                        newPrimV = oldPrimV;
                    }
                    oldPrimV = parentTotObj.getFieldValueString(i, 0);
                    String dat = parentTotObj.getFieldValueString(i, 2);
                    int mValue = parentTotObj.getFieldValueInt(i, 1);
                    int old = 0;
                    if (primH2.containsKey(dat)) {
                        old = ((Integer) primH2.get(dat)).intValue();
                    }
                    mValue = mValue + old;
                    primH2.put(dat, Integer.valueOf(mValue));
                }
               
//                 result = result + "<Tr><Td>Restricting Total</Td>";
                 result.append("<Tr><Td>Restricting Total</Td>");
                for (int k = 0; k < colEdgeVals.size(); k++) {
                    int rTotal = 0;
                    if (primH2.containsKey(colEdgeVals.get(k).toString())) {
                        rTotal = ((Integer) primH2.get(colEdgeVals.get(k).toString())).intValue();
                    }
//                    result = result + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"" + rTotal + "\"></Td>";
                    result.append("<Td><input type=\"text\" readonly class=\"displayTotal\" value=\""  ).append( rTotal  ).append("\"></Td>");
                    if (disableSaveButtonFlag == false) {

                        if (primH2.containsKey(colEdgeVals.get(k).toString())) {
                            if (rTotal != 0) {
                                disableSaveButtonFlag = true;
                            }
                        }
                    }

                }
//                result = result + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td></Tr>";
                result.append("<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td></Tr>");
//                result = result + "<Tr><Td>Difference</Td>";
                result.append("<Tr><Td>Difference</Td>");
                for (int k = 0; k < colEdgeVals.size(); k++) {
//                    result = result + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>";
                    result.append( "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>");
                }
//                result = result + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td></Tr>";
                result.append("<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td></Tr>");
            }
            // result = result+"<Tr><Td>"+errorMessage+"</Td></Tr>";


            // to show the overall target
            //primParamEleId
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
            if (periodType.equalsIgnoreCase("Day")) {
                overAllTotQ = "Select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' " + primParamFilter + " group by " + elementCols.get(primParamEleId.trim()).toString() + ",to_char(t_date,'dd-mm-yy') order by " + elementCols.get(primParamEleId.trim()).toString() + ",to_char(t_date,'dd-mm-yy')";
            } else if (periodType.equalsIgnoreCase("Month")) {
                overAllTotQ = "Select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' " + primParamFilter + " group by " + elementCols.get(primParamEleId.trim()).toString() + ",t_month order by " + elementCols.get(primParamEleId.trim()).toString() + ",t_month";
            } else if (periodType.equalsIgnoreCase("Quarter")) {
                overAllTotQ = "Select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' " + primParamFilter + " group by " + elementCols.get(primParamEleId.trim()).toString() + ",t_qtr order by " + elementCols.get(primParamEleId.trim()).toString() + ",t_qtr";
            } else if (periodType.equalsIgnoreCase("Year")) {
                overAllTotQ = "Select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' " + primParamFilter + " group by " + elementCols.get(primParamEleId.trim()).toString() + ",t_year order by " + elementCols.get(primParamEleId.trim()).toString() + ",t_year";
            }

            //PbReturnObject overAllObj = pbDb.execSelectSQL(overAllTotQ);
            psO = con.prepareStatement(overAllTotQ);
            psO.setString(1, targetId);
            rsO = psO.executeQuery();
            PbReturnObject overAllObj = new PbReturnObject(rsO);

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
            overAllMessage = " The Overall Target Set By Primary Parameter '" + viewByNames.get(primParamEleId.trim()).toString() + "' is " + grandTotal;
            //  result = result+"<Tr><Td>"+overAllMessage+"</Td></Tr>";
        } else {
            String displayDate = startRange;
            dateMeassage = "The Data is shown for " + minTimeLevel + "  '" + startRange + "'";
            if (minTimeLevel.equalsIgnoreCase("Day")) {
                dataQuery.append("select ").append(elementCols.get(viewByElement.trim()).toString()).append(",sum(").append(measureName.trim()).append("),").append(elementCols.get(colViewByElement.trim()).toString()).append(" from ").append(targetTable.trim()).append(" where target_id='").append(targetId).append("' and viewby='").append(viewByNames.get(colViewByElement.trim()).toString()).append("' ").append(allParametrsFilterClause).append(" and t_date=to_date(to_char('").append(startRange).append("'),'mm-dd-yy') group by ").append(elementCols.get(viewByElement.trim()).toString()).append(",").append(elementCols.get(colViewByElement.trim()).toString()).append(" order by ").append(elementCols.get(viewByElement.trim()).toString()).append(",").append(elementCols.get(colViewByElement.trim()).toString());
//                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' " + allParametrsFilterClause + " and t_date=to_date(to_char('" + startRange + "'),'mm-dd-yy') group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
            } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                dataQuery.append("select ").append(elementCols.get(viewByElement.trim()).toString()).append(",sum(").append(measureName.trim()).append("),").append(elementCols.get(colViewByElement.trim()).toString()).append(" from ").append(targetTable.trim()).append(" where target_id='").append(targetId).append("' and viewby='").append(viewByNames.get(colViewByElement.trim()).toString()).append("'  and t_month='").append(startRange).append("' ").append(allParametrsFilterClause).append(" group by ").append(elementCols.get(viewByElement.trim()).toString()).append(",").append(elementCols.get(colViewByElement.trim()).toString()).append(" order by ").append(elementCols.get(viewByElement.trim()).toString()).append(",").append(elementCols.get(colViewByElement.trim()).toString());
//                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "'  and t_month='" + startRange + "' " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
            } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                dataQuery.append("select ").append(elementCols.get(viewByElement.trim()).toString()).append(",sum(").append(measureName.trim()).append("),").append(elementCols.get(colViewByElement.trim()).toString()).append(" from ").append(targetTable.trim()).append(" where target_id='").append(targetId).append("' and viewby='").append(viewByNames.get(colViewByElement.trim()).toString()).append("'  and t_qtr='").append(startRange).append("' ").append(allParametrsFilterClause).append(" group by ").append(elementCols.get(viewByElement.trim()).toString()).append(",").append(elementCols.get(colViewByElement.trim()).toString()).append(" order by ").append(elementCols.get(viewByElement.trim()).toString()).append(",").append(elementCols.get(colViewByElement.trim()).toString());
//                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "'  and t_qtr='" + startRange + "' " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
            } else if (minTimeLevel.equalsIgnoreCase("Year")) {
                dataQuery.append("select ").append(elementCols.get(viewByElement.trim()).toString()).append(",sum(").append(measureName.trim()).append("),").append(elementCols.get(colViewByElement.trim()).toString()).append(" from ").append(targetTable.trim()).append(" where target_id='").append(targetId).append("' and viewby='").append(viewByNames.get(colViewByElement.trim()).toString()).append("'  and t_year='").append(startRange).append("' ").append(allParametrsFilterClause).append(" group by ").append(elementCols.get(viewByElement.trim()).toString()).append(",").append(elementCols.get(colViewByElement.trim()).toString()).append(" order by ").append(elementCols.get(viewByElement.trim()).toString()).append(",").append(elementCols.get(colViewByElement.trim()).toString());
//                dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "'  and t_year='" + startRange + "' " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
            }

            // PbReturnObject allD = pbDb.execSelectSQL(dataQuery);
            ps = con.prepareStatement(dataQuery.toString());
            rs = ps.executeQuery();
            PbReturnObject allD = new PbReturnObject(rs);

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
                int mValue = allD.getFieldValueInt(n, 1);
                primH.put(dat, Integer.valueOf(mValue));
                if (n == (allD.getRowCount() - 1)) {
                    allV.put(oldPv, primH);
                }
            }

            PbReturnObject time = new PbReturnObject();//pbDb.execSelectSQL(colQry);
            //  paramVals = f1.getRowEdges(rowQry,currVal);
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
                    int mV = 0;
                    if (dt.containsKey(timeV)) {
                        mV = ((Integer) dt.get(timeV)).intValue();
                    }
                    String value = "";
                    if (mV != 0) {
                      value = String.valueOf( mV );
                    }
                    originalResult.put(paramVals.get(x) + ":" + timeV, Integer.valueOf(mV));
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
                    int oriVal = 0;
                    oriVal = ((Integer) originalResult.get(totalKey)).intValue();
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
                        int al = ((Integer) rowTotal.get(rowKey)).intValue();
                        al = oriVal + al;
                        rowTotal.put(rowKey, Integer.valueOf(al));
                    } else {
                        rowTotal.put(rowKey, Integer.valueOf(oriVal));
                    }

                    if (colTotal.containsKey(colKey)) {
                        int colAl = ((Integer) colTotal.get(colKey)).intValue();
                        colAl = oriVal + colAl;
                        colTotal.put(colKey, Integer.valueOf(colAl));
                    } else {
                        colTotal.put(colKey, Integer.valueOf(oriVal));
                    }

                }
            }
            // to get the column and row total

            // to get the restricting total
            String colChildElement = "";
            colChildElement = "";//collect.getChildElementId(colViewByElement,targetId);

            // to find the parent total
            String getParents = resundle.getString("getParents");
            Object tarObP[] = new Object[2];
            tarObP[0] = targetId;
            tarObP[1] = colViewByElement;

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

            if (elementDet.containsKey(colViewByElement)) {
                ArrayList eleDet = new ArrayList();
                eleDet = (ArrayList) elementDet.get(colViewByElement);
                parentEle = eleDet.get(1).toString();
                parentDispName = eleDet.get(2).toString();
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
            if (minTimeLevel.equalsIgnoreCase("Month")) {
                if (parentEle.trim().equalsIgnoreCase("-1")) {
                    parentDataQ = "select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' " + parentParamFilter + " and t_month='?' group by " + elementCols.get(primParamEleId.trim()).toString() + ",t_month order by " + elementCols.get(primParamEleId.trim()).toString() + ",t_month ";
                } else {
                    parentDataQ = "select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(parentEle.trim()).toString() + " from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " and t_month='?' group by " + elementCols.get(primParamEleId.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString() + " order by " + elementCols.get(primParamEleId.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString();
                }
                // else if(parentEle.equalsIgnoreCase("-1"))//if(!parentEle.equalsIgnoreCase("-1"))
            } else if (minTimeLevel.equalsIgnoreCase("Day")) {
                if (parentEle.trim().equalsIgnoreCase("-1")) {
                    parentDataQ = "select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' and t_date=to_date(to_char('?'),'mm-dd-yy') " + parentParamFilter + " group by " + elementCols.get(primParamEleId.trim()).toString() + ",to_char(t_date,'dd-mm-yy') order by " + elementCols.get(primParamEleId.trim()).toString() + ",to_char(t_date,'dd-mm-yy')";
                } else {
                    parentDataQ = "select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + ")" + elementCols.get(parentEle.trim()).toString() + " from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " and t_date=to_date(to_char('?'),'mm-dd-yy') group by " + elementCols.get(primParamEleId.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString() + " order by " + elementCols.get(primParamEleId.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString();
                }
                // else if(parentEle.equalsIgnoreCase("-1"))//if(!parentEle.equalsIgnoreCase("-1"))
            } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                if (parentEle.trim().equalsIgnoreCase("-1")) {
                    parentDataQ = "select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' and t_qtr='?' " + parentParamFilter + " group by " + elementCols.get(primParamEleId.trim()).toString() + ",t_qtr order by " + elementCols.get(primParamEleId.trim()).toString() + ",t_qtr ";
                } else {
                    parentDataQ = "select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + ")" + elementCols.get(parentEle.trim()).toString() + " from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " and t_qtr='?' group by " + elementCols.get(primParamEleId.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString() + " order by " + elementCols.get(primParamEleId.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString();
                }
                // else if(parentEle.equalsIgnoreCase("-1"))//if(!parentEle.equalsIgnoreCase("-1"))
            } else if (minTimeLevel.equalsIgnoreCase("Year")) {
                if (parentEle.trim().equalsIgnoreCase("-1")) {
                    parentDataQ = "select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' " + parentParamFilter + " and t_year='" + startRange + "' group by " + elementCols.get(primParamEleId.trim()).toString() + ",t_year order by " + elementCols.get(primParamEleId.trim()).toString() + ",t_year ";
                } else {
                    parentDataQ = "select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(parentEle.trim()).toString() + " from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(parentEle.trim()).toString() + "' " + parentParamFilter + " and t_year='?' group by " + elementCols.get(primParamEleId.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString() + " order by " + elementCols.get(primParamEleId.trim()).toString() + "," + elementCols.get(parentEle.trim()).toString();
                }
                // else if(parentEle.equalsIgnoreCase("-1"))//if(!parentEle.equalsIgnoreCase("-1"))
            }
            psP = con.prepareStatement(parentDataQ);
            psP.setString(1, targetId);
            psP.setString(2, startRange);
            rsP = psP.executeQuery();
            PbReturnObject parentData = new PbReturnObject(rsP);
            // PbReturnObject parentData=pbDb.execSelectSQL(parentDataQ);
            parentTotals = new HashMap();
            for (int t = 0; t < parentData.getRowCount(); t++) {
                parentTotals.put(parentData.getFieldValueString(t, 0), Integer.valueOf(parentData.getFieldValueInt(t, 1)));
            }
            // to get the restricting total over
            // to display the data
            //to display header
            //colChildElement = collect.getChildElementId(colChildElement,targetId);
//            result = result + "<Tr><Td style=\"width:200px;font-weight:bold;font-size:11px;background-color:#E6E6E6\">" + currVal + "</Td>";
            result.append("<Tr><Td style=\"width:200px;font-weight:bold;font-size:11px;background-color:#E6E6E6\">" ).append( currVal  ).append( "</Td>");
            if (!colChildElement.equalsIgnoreCase("-1")) {
                for (int z = 0; z < colEdgeVals.size(); z++) {
//                    result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">";
                    result.append( "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">");
//                    result += "<a href=\"javascript:submitColumnDrillUrl('" + colDrillUrl + (String) colEdgeVals.get(z) + "')\">" + (String) colEdgeVals.get(z) + "";
                    result.append( "<a href=\"javascript:submitColumnDrillUrl('"  ).append( colDrillUrl  ).append( (String) colEdgeVals.get(z)  ).append( "')\">"  ).append( (String) colEdgeVals.get(z)  ).append( "</tr> ");
//                    result += "</Td>";

                }
            } else if (colChildElement.equalsIgnoreCase("-1")) {
                for (int z = 0; z < colEdgeVals.size(); z++) {
//                    result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">";
                    result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">");
//                    result += (String) colEdgeVals.get(z);
                    result.append((String) colEdgeVals.get(z)).append("</td>");
//                    result += "</Td>";

                }
            }
//            result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Row Total</Td>";
            result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Row Total</Td>");
//            result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Restricting Total</Td>";
            result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Restricting Total</Td>");
//            result = result + "<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Difference</Td>";
            result.append("<Td align=\"center\" style=\"font-weight:bold;font-size:11px;background-color:#E6E6E6\">Difference</Td></tr>");

//            result = result + "</Tr>";
            // display the data table
            for (int x = 1; x < paramVals.size(); x++) {
                String pVal = paramVals.get(x).toString();
                int parentTotal = 0;
                if (parentTotals.containsKey(pVal)) {
                    parentTotal = ((Integer) parentTotals.get(pVal)).intValue();
                }

                if (disableSaveButtonFlag == false) {
                    if (parentTotal != 0) {
                        disableSaveButtonFlag = true;
                    }
                }

                int rowTotalVal = 0;
                if (rowTotal.containsKey(pVal)) {
                    rowTotalVal = ((Integer) rowTotal.get(pVal)).intValue();
                }
                
//                result = result + "<Tr>" + "<Td>" + paramVals.get(x).toString() + "</Td>";
                result.append( "<Tr><Td>").append( paramVals.get(x).toString()).append( "</Td>");
                /*
                 * else if(colChildElement.equalsIgnoreCase("-1")){ result =
                 * result+"<Tr>"+"<Td
                 * style=\"width:200px\">"+paramVals.get(x).toString()+"</Td>";
                 * }
                 */

//                result = result + "<Tr>" + "<Td>" + paramVals.get(x).toString() + "</Td>";
                result.append( "<Tr><Td>"  ).append( paramVals.get(x).toString()  ).append( "</Td>");
                /* else if(colChildElement.equalsIgnoreCase("-1")){
                result = result+"<Tr>"+"<Td style=\"width:200px\">"+paramVals.get(x).toString()+"</Td>";
                } */

                HashMap dt = new HashMap();
                if (allV.containsKey(pVal)) {
                    dt = (HashMap) allV.get(pVal);
                }
                for (int y = 0; y < colEdgeVals.size(); y++) {
                    String timeV = colEdgeVals.get(y).toString();
                    int mV = 0;
                    if (dt.containsKey(timeV)) {
                        mV = ((Integer) dt.get(timeV)).intValue();
                    }
                    String value = "";
                    if (mV != 0) {
                      value = String.valueOf( mV );
                    }
//                    result = result + "<Td><input readonly onkeypress=\"return isNumberKey(event)\" onkeyup=\"autoRowSum('" + paramVals.get(x) + ":" + timeV + "')\" type=\"text\" id=\"" + paramVals.get(x) + ":" + timeV + "\" name=\"" + paramVals.get(x) + ":" + timeV + "\"  value=\"" + value + "\"></Td>";
                    result.append( "<Td><input readonly onkeypress=\"return isNumberKey(event)\" onkeyup=\"autoRowSum('" ).append( paramVals.get(x)  ).append( ":" ).append( timeV  ).append( "')\" type=\"text\" id=\""  ).append( paramVals.get(x)  ).append( ":"  ).append( timeV  ).append("\" name=\""  ).append( paramVals.get(x)  ).append( ":"  ).append( timeV  ).append( "\"  value=\""  ).append( value  ).append( "\"></Td>");

                }
//                result = result + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"" + rowTotalVal + "\"></Td>"
//                        + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"" + parentTotal + "\"></Td>"
//                        + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>";
                result.append( "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"" ).append( rowTotalVal ).append( "\"></Td>").append( "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"" ).append( parentTotal ).append( "\"></Td>").append( "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td></tr>");

//                result = result + "</Tr>";
            }

//            result = result + "<Tr><Td style=\"width:200px\">Column Total</Td>";
            result.append( "<Tr><Td style=\"width:200px\">Column Total</Td>");
            for (int p = 0; p < colEdgeVals.size(); p++) {
                String colKey = colEdgeVals.get(p).toString();
                int colTotVal = 0;
                if (colTotal.containsKey(colKey)) {
                    colTotVal = ((Integer) colTotal.get(colKey)).intValue();
                }
//                result = result + "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"" + colTotVal + "\"></Td>";
                result.append( "<Td><input type=\"text\" readonly class=\"displayTotal\" value=\"" ).append( colTotVal ).append( "\"></Td>");
            }
//            result = result + "<Td style=\"width:200px\"><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>";
//            result = result + "<Td style=\"width:200px\"><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>";
//            result = result + "<Td style=\"width:200px\"><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>";
//            result = result + "</Tr>";
            // to show the overall target

            result.append("<Td style=\"width:200px\"><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>");
            result.append( "<Td style=\"width:200px\"><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>");
            result.append( "<Td style=\"width:200px\"><input type=\"text\" readonly class=\"displayTotal\" value=\"0\"></Td>");
            result.append( "</Tr>");
             // to show the overall target
            //primParamEleId
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
            if (minTimeLevel.equalsIgnoreCase("Day")) {
                overAllTotQ = "Select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' " + primParamFilter + " group by " + elementCols.get(primParamEleId.trim()).toString() + ",to_char(t_date,'dd-mm-yy') order by " + elementCols.get(primParamEleId.trim()).toString() + ",to_char(t_date,'dd-mm-yy')";
            } else if (minTimeLevel.equalsIgnoreCase("Month")) {
                overAllTotQ = "Select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' " + primParamFilter + " and t_month='" + startRange + "' group by " + elementCols.get(primParamEleId.trim()).toString() + ",t_month order by " + elementCols.get(primParamEleId.trim()).toString() + ",t_month";
            } else if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                overAllTotQ = "Select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' " + primParamFilter + " group by " + elementCols.get(primParamEleId.trim()).toString() + ",t_qtr order by " + elementCols.get(primParamEleId.trim()).toString() + ",t_qtr";
            } else if (minTimeLevel.equalsIgnoreCase("Year")) {
                overAllTotQ = "Select " + elementCols.get(primParamEleId.trim()).toString() + ",sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id=?  and viewby='" + viewByNames.get(primParamEleId.trim()).toString() + "' " + primParamFilter + " group by " + elementCols.get(primParamEleId.trim()).toString() + ",t_year order by " + elementCols.get(primParamEleId.trim()).toString() + ",t_year";
            }

            psO = con.prepareStatement(overAllTotQ);
            psO.setString(1, targetId);
            rsO = psO.executeQuery();
            PbReturnObject overAllObj = new PbReturnObject(rsO);
            // PbReturnObject overAllObj = pbDb.execSelectSQL(overAllTotQ);
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

            overAllMessage = " The Overall Target Set By Primary Parameter '" + viewByNames.get(primParamEleId.trim()).toString() + "' is " + grandTotal;
            secAnalyze = colViewByElement;

        }
        String freezeButton = "";
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
        totalResult.put("freezeButton", freezeButton);
        totalResult.put("targetStartDate", targetStartDate);
        totalResult.put("targetEndDate", targetEndDate);

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
            //////////////////////////////////////////////////////////////////////////.println("time2Query -"+time2Query);
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

        //////////////////////////////////////////////////////////////////////////.println("defaultTimeQuery - "+defaultTimeQuery);
        PbReturnObject defaultTimeOb = pbDb.execSelectSQL(defaultTimeQuery);
        for (int y = 0; y < defaultTimeOb.getRowCount(); y++) {
            if (minTimeLevel.equalsIgnoreCase("Quarter")) {
                defDates.add(defaultTimeOb.getFieldValueString(y, 0) + "-" + defaultTimeOb.getFieldValueString(y, 1));
            } else {
                defDates.add(defaultTimeOb.getFieldValueString(y, 0));
            }
        }

        ////////////////////////////////////////////////////////////////////.println(" defDates "+defDates);
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
//        String targetId = ScenarioId;
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
//        String ParameterId = null;
        StringBuilder ParameterId=new StringBuilder();
//        String paramWhereClause = "";
               StringBuilder paramWhereClause=new StringBuilder();
//        String selfParamWhereClause = "";
         StringBuilder selfParamWhereClause=new StringBuilder();
        String sqlstr = "";
        String finalQuery = "";
        {//where Clause Columns
            String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);
            for (int i = 0; i < a1.length; i++) {
                if (ValidateParamValues(hm.get(a1[i]))) {
                    if (ParameterId == null) {
                        ParameterId.append(a1[i]).toString();
                    } else {
                        ParameterId.append(",").append((a1[i]).toString());
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
                //
                Object Obj[] = new Object[1];
                Obj[0] = ParameterId;
                finalQuery = pbDb.buildQuery(sqlstr, Obj);
                //  ////////////////////////////////////////////////////////////////////.println(" finalQuery --"+finalQuery);
                //
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
                        paramWhereClause.append(" and ").append(retObj.getFieldValueString(i, colNames[1])).append(".").append(retObj.getFieldValueString(i, colNames[2]));
                        paramWhereClause.append(" in (").append(getOracleInClause(hm.get(retObj.getFieldValueString(i, colNames[5])))).append(") ");
                        if (!(viewByCols == null || viewByCols.size() == 0)) {
                            if (retObj.getFieldValueString(i, colNames[5]).equalsIgnoreCase(viewByCols.get(0).toString())) {

                                selfParamWhereClause.append(" and ").append(retObj.getFieldValueString(i, colNames[1])).append(".").append(retObj.getFieldValueString(i, colNames[2]));
                                selfParamWhereClause.append(" in (").append(getOracleInClause(hm.get(retObj.getFieldValueString(i, colNames[5])))).append(") ");

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
        return paramWhereClause.toString();

    }

    //added by uday on 19-jan-2010
    public String displayParamwithTime(ArrayList elementList, HashMap timehm) {
//        String result1 = "";
        StringBuilder result1=new StringBuilder();
        String sqlstr = "";
        int j = 1;
        result1.append("<Table width=\"70%\"> <tr><td width=\"80%\"> <table width=\"100%\">");
        f1.elementId = elementList.get(0).toString();
        this.totalParam = 0;
        String[] a1 = (String[]) (timehm.keySet()).toArray(new String[0]);
        this.totalParam += a1.length;
        int i = 0;
        for (i = 0; i < a1.length; i++) {
            if ((j) % 4 == 1) {
                result1.append("<Tr>");
            }
            ArrayList a = (ArrayList) timehm.get(a1[i]);

            if (a1[i].equalsIgnoreCase("AS_OF_MONTH") || a1[i].equalsIgnoreCase("AS_OF_MONTH1")) {
                result1.append(" <Td id=\"column").append(i).append("\" align=\"right\"> ").append(f1.getQueryCombotb2(a.get(1).toString(), (String) a.get(7), getAllMonthsQry(a1[i]), (String) a.get(2))).append(" </Td> ");
            } else if (a1[i].equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                result1.append(" <Td id=\"column").append(i).append("\" align=\"right\"> ").append(f1.getQueryCombotb2(a.get(1).toString(), (String) a.get(7), getPeriodTypeQuery(a1[i], "Month"), (String) a.get(0))).append(" </Td> ");
            } else if (a1[i].equalsIgnoreCase("AS_OF_YEAR") || a1[i].equalsIgnoreCase("AS_OF_YEAR1")) {
                result1.append(" <Td id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");
            }
            if ((j) % 4 == 0) {
                result1.append( "</Tr>");
            }
            j++;
        }

        result1.append( "");

        for (i = 0; i < elementList.size(); i++) {
            try {
                if (j % 4 == 1) {
                    result1.append("<Tr>");
                }

                sqlstr = "select A1 , A2 from ( " + getParameterQuery(elementList.get(i).toString()) + " ) where rownum between 1 and 100 ";

                f1.elementId = elementList.get(i).toString();

                result1.append(" <Td align=\"right\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryAllCombotb("CBOAR" + elementList.get(i).toString(), this.tempPass, sqlstr, elementList.get(i).toString())) .append(" </Td> ");
                if (j % 4 == 0) {
                    result1.append( "</Tr>");
                }
                j++;
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }
        if ((j) % 4 != 0) {
            result1.append( "</Tr>");
        }

        result1.append("</table>");
        return (result1.toString());
    }

    public ArrayList getProjectedMonths(String scenarioId, LinkedHashMap timeDetailsMap) throws Exception {

        ArrayList newMonths = new ArrayList();
        String query = "select * from scenario_master where scenario_id=" + scenarioId;
        String endMonth = "";
        String startMonth = "";
        PbReturnObject scMonths = pbDb.execSelectSQL(query);
        startMonth = scMonths.getFieldValueString(0, "HISTORICAL_ST_MONTH");
        endMonth = scMonths.getFieldValueString(0, "HISTORICAL_END_MONTH");

        if (timeDetailsMap.containsKey("AS_OF_MONTH")) {
            ArrayList al = (ArrayList) timeDetailsMap.get("AS_OF_MONTH");
            startMonth = al.get(0).toString();
        }
        if (timeDetailsMap.containsKey("AS_OF_MONTH1")) {
            ArrayList al = (ArrayList) timeDetailsMap.get("AS_OF_MONTH1");
            endMonth = al.get(0).toString();
        }
        if (timeDetailsMap.containsKey("AS_OF_YEAR")) {
            ArrayList al = (ArrayList) timeDetailsMap.get("AS_OF_YEAR");
            startMonth = al.get(0).toString();
        }
        if (timeDetailsMap.containsKey("AS_OF_YEAR1")) {
            ArrayList al = (ArrayList) timeDetailsMap.get("AS_OF_YEAR1");
            endMonth = al.get(0).toString();
        }
//        String monthQ = "select MON_NAME as view_by, MON_NAME as view_by1 from PRG_ACN_MON_DENOM where cm_st_date between " +
//                " (select distinct cm_st_date from prg_acn_mon_denom where mon_name='" + startMonth + "') and " +
//                " (select distinct cm_end_date from prg_acn_mon_denom where mon_name='" + endMonth + "') order by " +
//                "cyear,cmon";

        String monthQ = "select distinct CWYEAR as view_by,CWYEAR as view_by1 from PR_DAY_DENOM where CWYEAR between '" + startMonth + "' and '" + endMonth + "' order by cwyear";
        PbReturnObject scAllMonths = pbDb.execSelectSQL(monthQ);
        for (int i = 0; i < scAllMonths.getRowCount(); i++) {
            newMonths.add(scAllMonths.getFieldValueString(i, 1));
        }

        return newMonths;
    }

    public ArrayList getHistMonths(String scenarioId) throws Exception {

        ArrayList newMonths = new ArrayList();
        String query = "select * from scenario_master where scenario_id=" + scenarioId;
        String endMonth = "";
        String startMonth = "";
        PbReturnObject scMonths = pbDb.execSelectSQL(query);
        startMonth = scMonths.getFieldValueString(0, "SCENARIO_START_MONTH");
        endMonth = scMonths.getFieldValueString(0, "SCENARIO_END_MONTH");

//        String monthQ = "select MON_NAME as view_by, MON_NAME as view_by1  from PRG_ACN_MON_DENOM where cm_st_date between " +
//                " (select distinct cm_st_date from prg_acn_mon_denom where mon_name='" + startMonth + "') and " +
//                " (Stringselect distinct cm_end_date from prg_acn_mon_denom where mon_name='" + endMonth + "') order by " +
//                "cyear,cmon";

        String monthQ = "select distinct CWYEAR as view_by,CWYEAR as view_by1 from PR_DAY_DENOM where CWYEAR between '" + startMonth + "' and '" + endMonth + "'";
        PbReturnObject scAllMonths = pbDb.execSelectSQL(monthQ);
        for (int i = 0; i < scAllMonths.getRowCount(); i++) {
            if (i == 0) {
                newMonths.add(scAllMonths.getFieldValueString(0, 1));
            }
        }
        return newMonths;
    }

    public HashMap getScenarioTable(ArrayList colViewByVal, PbReturnObject dataObj, String scenarioId, ArrayList scenarioViewByMonths, String viewBy, HashMap crossTabNonViewByMap, LinkedHashMap scenarioViewByMain, LinkedHashMap timeDetailsMap, HashMap scenarioParameters, String drillUrl) throws Exception {
        String dimensionId = "";
        String viewByEle = viewBy;
        HashMap normalHM = new HashMap();
        double secondTrend = 0.00;
        double thirdTrend = 0.00;
        double fourthTrend = 0.00;
        double sixthTrend = 0.00;

        double secondTrend1 = 0.00;
        double thirdTrend2 = 0.00;
        double fourthTrend3 = 0.00;
        double sixthTrend4 = 0.00;

        double secondTrendg = 0.00;
        double thirdTrendg = 0.00;
        double sixthTrendg = 0.00;
        double ninthTrendg = 0.00;
        double twelveTrendg = 0.00;
        double lastMonthg = 0.00;
        double selectmonthg = 0.00;
        double grandTotal = 0.00;
        double msrGrandTotal = 0.00;
        double projGrandTotal = 0.00;
        double fieldGrandTotal = 0.00;
        BigDecimal bd = null;

        double y1growth = 0.00;
        double y2growth = 0.00;

        double sumdouble1 = 0.00;
        double sumdoubledouB2 = 0.00;
        double sumdouble3 = 0.00;
        double sumdoubledouB4 = 0.00;
        double sumExpGrowth = 0.00;
        double sumExpFinalGrowth = 0.00;
        double sumforCastGrowth = 0.00;
        double sumforCastFinalGrowth = 0.00;

        ArrayList douArrayList = new ArrayList();
        ArrayList doubArrayList = new ArrayList();

        LinkedHashMap GraphMap = new LinkedHashMap();
        Vector GraphVector = new Vector();
        Vector ViewbyVector = new Vector();

//        normalHM = collect.getNormalHm();
        //    ////("normalHM-in getScenarioTable--" + normalHM);
        if (scenarioParameters.containsKey(viewBy)) {
            ArrayList det = new ArrayList();
            det = (ArrayList) scenarioParameters.get(viewBy);
            dimensionId = det.get(3).toString();
        }

        HashMap all = new HashMap();
        ArrayList histMonths = new ArrayList();
        histMonths = getHistMonths(scenarioId);
        //    ////("histMonths--" + histMonths);
        String nextMonth = "";
        nextMonth = histMonths.get(0).toString();
        ArrayList param = new ArrayList();
        String viewByName = "";
        String mainVal[] = (String[]) scenarioViewByMain.keySet().toArray(new String[0]);
        String modelId = "";
        String modelName = "";
        for (int t = 0; t < mainVal.length; t++) {
            ArrayList al = (ArrayList) scenarioViewByMain.get(mainVal[t]);
            if (al.get(2).toString().equalsIgnoreCase("Model")) {
                modelName = al.get(3).toString();
                modelId = al.get(4).toString();
            }
        }

        String dispName = "select * from prg_user_all_info_details where element_id=" + viewBy;
        PbReturnObject vNameObj = pbDb.execSelectSQL(dispName);
        viewBy = vNameObj.getFieldValueString(0, "DISP_NAME");
        ArrayList colNew = new ArrayList();
        HashMap colRevMap = new HashMap();
        for (int y = 0; y < colViewByVal.size(); y++) {
            String qName = colViewByVal.get(y).toString();
            String val = "";
            if (crossTabNonViewByMap.containsKey(qName)) {
                colNew.add(crossTabNonViewByMap.get(qName).toString());
            }
            colRevMap.put(crossTabNonViewByMap.get(qName).toString(), qName);
        }
        PbReturnObject pbro = new PbReturnObject();
        ArrayList newMonths = new ArrayList();
        newMonths = getProjectedMonths(scenarioId, timeDetailsMap);

        String colNames[] = new String[newMonths.size() + 1];
        colNames[0] = viewBy.toUpperCase().trim();
        for (int y = 1; y < colNames.length; y++) {
            colNames[y] = (String) newMonths.get(y - 1);
        }
        pbro.setColumnNames(colNames); //uday

        for (int m = 0; m < dataObj.getRowCount(); m++) {
            pbro.setFieldValueString(viewBy.toUpperCase().trim(), dataObj.getFieldValueString(m, 0));
            param.add(dataObj.getFieldValueString(m, 0));
            for (int y = 0; y < newMonths.size(); y++) {
                String scMon = newMonths.get(y).toString();
                //    ////("scMon---" + scMon);
                String dataValue = "0";
                if (colNew.contains(scMon)) {
                    if (colRevMap.containsKey(scMon)) {
                        String qName = "";
                        qName = (String) colRevMap.get(scMon);
                        dataValue = dataObj.getFieldValueString(m, qName);
                        //      ////("dataValue--" + dataValue);
                    }
                }
                pbro.setFieldValueString(scMon, dataValue);
            }
            pbro.addRow();
        }



        pbro.writeString();

        ArrayList datesVa = new ArrayList();
        String cols[] = pbro.getColumnNames();
        String fColV = "";
        int t = 0;
        for (int p = 0; p < cols.length; p++) {
            if (p == 0) {
                fColV = cols[p];
            }

        }
        ArrayList al2 = new ArrayList();
        al2 = newMonths;
        datesVa = al2;
        //  ////(" datesVa " + datesVa);
        viewByName = viewBy;

        ArrayList alRev = new ArrayList();
        for (int y = al2.size() - 1; y >= 0; y--) {
            alRev.add(al2.get(y));
        }


        LinkedHashMap totIssue = new LinkedHashMap();

        //Calculation For the custom Model
        PbScenarioParamVals customP = new PbScenarioParamVals();

        Session customSession = new Session();
        ScenarioDb scDb = new ScenarioDb();
        customP.setScenarioId(scenarioId);
        customSession.setObject(customP);
        PbReturnObject modelpbro = scDb.getCustModelForScenario(customSession);
        PbReturnObject custompbro = scDb.getCustModelDet(customSession);
        String custModName[] = new String[modelpbro.getRowCount()];
        String custModBasis[] = new String[modelpbro.getRowCount()];
        String custModWeightType[] = new String[modelpbro.getRowCount()];
        String custModBasis2 = "";
        String custModName2 = "";//modelId;
        if (modelId.equalsIgnoreCase("") || (modelId.equalsIgnoreCase("--Select--"))) {
            custModName2 = modelId;
        }
        String custModWeightType2 = "";
        HashMap custMapMod = new HashMap();
        ArrayList cMonths = new ArrayList();

        // to check the weights
        HashMap custWeights = new HashMap();
        if (modelpbro.getRowCount() > 0) {
            for (int y = 0; y < modelpbro.getRowCount(); y++) {
                custModName[y] = modelpbro.getFieldValueString(y, "CUSTOM_MODEL_NAME");
                custModBasis[y] = modelpbro.getFieldValueString(y, "MODEL_BASIS");
                // custModWeightType[y] = modelpbro.getFieldValueString(y, "WEIGHT_TYPE");
                if (modelId.equalsIgnoreCase(modelpbro.getFieldValueString(y, "CUSTOM_MODEL_NAME"))) {
                    custModName2 = modelpbro.getFieldValueString(y, "CUSTOM_MODEL_NAME");
                    // custModWeightType2 = modelpbro.getFieldValueString(y,"WEIGHT_TYPE");
                    custModBasis2 = modelpbro.getFieldValueString(y, "MODEL_BASIS");
                }
            }
            ArrayList custMonths = new ArrayList();
            // if(!custModName2.equalsIgnoreCase(""))
            // modelId = custModName2;

            for (int x = 0; x < custompbro.getRowCount(); x++) {


                String mName = custompbro.getFieldValueString(x, "CUSTOM_MODEL_NAME");
                String monName = custompbro.getFieldValueString(x, "CUSTOM_MODEL_MONTHS");
                //monName = monName.substring(0, 3) + monName.substring(4); 
                String weight = custompbro.getFieldValueString(x, "CUSTOM_MODEL_WEIGHTS");
                if (modelId.equalsIgnoreCase(mName)) {
                    cMonths.add(monName);
                }

                if (custMapMod.containsKey(mName)) {
                    custMonths = (ArrayList) custMapMod.get(mName);
                    HashMap neMap = new HashMap();
                    neMap.put(monName, weight);
                    custMonths.add(neMap);
                    // custMapMod.put(mName,custMonths);
                } else {
                    HashMap neMap = new HashMap();
                    custMonths = new ArrayList();
                    neMap.put(monName, weight);
                    custMonths.add(neMap);
                    // custMapMod.put(mName,custMonths);

                }
                custMapMod.put(mName, custMonths);
            }
        }
        ////////////////////////////////////////.println(" custMapMod " + custMapMod);

        ArrayList graphArray = new ArrayList();
        for (int m = 0; m < pbro.getRowCount(); m++) {
            LinkedHashMap hm2 = new LinkedHashMap();
            String oldpVal = param.get(m).toString();
            //////////////////////////////////////////.println("oldpVal...- " + oldpVal);
            hm2.put(viewByName, oldpVal);
            for (int k = 0; k < datesVa.size(); k++) {
                String b = datesVa.get(k).toString();
                float newV = Float.parseFloat(pbro.getFieldValueString(m, b.trim().toUpperCase()));
                //////////////////////////////////////////.println(m + " b -- " + b);
                hm2.put(b.trim().toUpperCase(), new Float(newV));
                // graphArray.add(hm2);
            }
        }

        // to stotre the custom calculated values
        HashMap custHm = new HashMap();
        //////////////////////////////////////////.println("graphArray . // " + graphArray);
        String nextMonth2 = nextMonth;//.substring(0, 3) + nextMonth.substring(4);
        Vector allPa = new Vector();
        boolean addFlag = false;
        LinkedHashMap custMapIssue = new LinkedHashMap();
        ////////////////////////////////////////.println(" alRev " + alRev);
        for (int m = 0; m < pbro.getRowCount(); m++) {

            LinkedHashMap hm2 = new LinkedHashMap();
            String oldpVal = param.get(m).toString();

            if (!allPa.contains(oldpVal)) {
                hm2.put(viewByName, oldpVal);
                //graphArray.add(hm2);
                addFlag = true;
            }
            // hm2.put(viewByName, oldpVal);
            // graphArray.add(hm2);

            ////////////////////////////////////////.println("oldpVal - " + oldpVal);
            double rTot = 0;
            LinkedHashMap hMapIssue = new LinkedHashMap();


            double custTotal = 0; //  Set key5 = (Set)hm.keySet();
            double custTotal2 = 0;
            Set keys = (Set) custMapMod.keySet();
            String keysV[] = (String[]) keys.toArray(new String[0]);
            String mName = "";
            //if (keysV.length > 0) {
            mName = modelId;//keysV[0];
            // }
            HashMap values = new HashMap();
            ArrayList oldMon = new ArrayList();
            if (custMapMod.containsKey(mName)) {
                oldMon = (ArrayList) custMapMod.get(mName);
                //////////////////////////////////////////.println("oldMon - "+oldMon);
                for (int l = 0; l < oldMon.size(); l++) {
                    HashMap old = (HashMap) oldMon.get(l);
                    Set keys2 = (Set) old.keySet();
                    String keysV2[] = (String[]) keys2.toArray(new String[0]);
                    for (int o = 0; o < keysV2.length; o++) {
                        String mm = keysV2[o];
                        if (old.containsKey(mm)) {
                            String wt = old.get(mm).toString();
                            values.put(mm, wt);
                        }
                    }
                }
            }
            double lastMon = 0.00;
            double secondLastMon = 0.00;
            double thirdLastMon = 0.00;
            double perChangeFirst = 0.00; //perChangeFirst = (secondLastMon-lastMon)/100;
            double fourthLastMon = 0.00;
            double fifthLastMon = 0.00;
            double sixthLastMon = 0.00;
            double seventhLastMon = 0.00;

            double sumFirstMonth = 0.00;
            double sumTwoMonths = 0.00;
            double sumThreeMonths = 0.00;
            double sumFourMonths = 0.00;
            double sumFiveMonths = 0.00;
            double sumSixMonths = 0.00;

            double sumTot = 0.00;

            // for minimum and maximum
            double minValue = 0.00;
            double maxValue = 0.00;
            boolean minFlag = false;
            for (int k = 0; k < alRev.size(); k++) {

                //////////////////////////////////////////.println("custModWeightType2 ... " + custModWeightType2);
                String b = alRev.get(k).toString();
                ////////////////////////////////////////.println(" pbro.getFieldValueString(m,b.trim().-=-=-=-=- " + pbro.getFieldValueString(m, b.trim().toUpperCase()));
                double newV = Double.parseDouble(pbro.getFieldValueString(m, b.trim().toUpperCase()));
                ////////////////////////////////////////.println(b + " newV " + newV);
                sumTot = sumTot + newV;

                if (k == 1) {
                    sumTwoMonths = sumTot;
                }
                if (k == 2) {
                    sumThreeMonths = sumTot;
                }
                if (k == 3) {
                    sumFourMonths = sumTot;
                }
                if (k == 5) {
                    sumSixMonths = sumTot;
                }


                if (k == 0) {
                    lastMon = newV;
                }
                if (k == 1) {
                    secondLastMon = newV;
                }
                if (k == 2) {
                    thirdLastMon = newV;
                }
                if (k == 3) {
                    fourthLastMon = newV;
                }
                if (k == 4) {
                    fifthLastMon = newV;
                }
                if (k == 5) {
                    sixthLastMon = newV;
                }
                if (k == 6) {
                    seventhLastMon = newV;
                }



                // to calculate the weight. If month is there get its weight and multiply with total of that month and add to custTotal
                if (cMonths.contains(b)) {
                    if (values.containsKey(b)) {
                        if (custModBasis2.equalsIgnoreCase("Weighted Average")) {
                            //if (custModWeightType2.equalsIgnoreCase("Not Equal")) {
                            //////////////////////////////////////////.println(" in Not  equal--");
                            double wt = Double.parseDouble((String) values.get(b));
                            // float nW = wt/100;
                            double newW = newV * wt;// newV*(1+nW);
                            custTotal = custTotal + newW;

                            //////////////////////////////////////////.println(wt + " wt------custTotal.. " + custTotal + " newV== " + newV + " paper " + b + " newW== " + newW + " nW============");
                        } else if (custModBasis2.equalsIgnoreCase("Simple Average")) {
                            ////////////////////////////////////////.println(" in  Simple Average---------");
                            custTotal = custTotal + newV;
                            ////////////////////////////////////////.println(" custTotal ,./ " + custTotal);
                        } else if (custModBasis2.equalsIgnoreCase("Sum")) {
                            //////////////////////////////////////////.println(" in  Sum---------");
                            double sumV = 0.00;
                            ScenarioC sc2 = new ScenarioC();
                            sumV = newV;
                            custTotal2 = custTotal2 + newV;
                            custTotal = custTotal + newV;//sumV;//newV;
                            ////////////////////////////////////////.println("custTotal is:: sum " + custTotal);
                        } else if (custModBasis2.equalsIgnoreCase("Minimum")) {
                            //////////////////////////////////////////.println(" in  Minimum---------");
                            if (minFlag == false) {
                                if (minValue > newV || minFlag == false) {
                                    minValue = newV;
                                }
                                minFlag = true;
                            } else if (minFlag == true) {
                                if (minValue > newV) {
                                    minValue = newV;
                                }
                            }
                        } else if (custModBasis2.equalsIgnoreCase("Maximum")) {
                            //////////////////////////////////////////.println(" in  Maximum---------");
                            if (maxValue < newV) {
                                maxValue = newV;
                            }
                        }
                    }
                }
                //// for minimum and maximum


                hm2.put(b.trim().toUpperCase(), new Double(newV));
                graphArray.add(hm2);

                rTot = rTot + newV;
                if (k == 0) {
                    hMapIssue.put("Sce1", String.valueOf(rTot));
                    //////////////////////////////////////////.println("Sce1 --  .... " + rTot);
                    if (modelId.equalsIgnoreCase("Last Month")) {
                        hm2.put(nextMonth2, String.valueOf(rTot));
                        // graphArray.add(hm2);
                    }
                }
                if (k == 1) {
                    double av = (double) rTot / 2;
                    //////////////////////////////////////////.println("Sce2 -- " + av + " .... " + rTot);
                    hMapIssue.put("Sce2", String.valueOf(av));
//                    if (modelId.equalsIgnoreCase("Last Two Months Average")) {
//                        hm2.put(nextMonth2, String.valueOf(av));
//                        // graphArray.add(hm2);
//                    }
                    if (modelId.equalsIgnoreCase("Last Two Years Average")) {
                        hm2.put(nextMonth2, String.valueOf(av));
                        // graphArray.add(hm2);
                    }
                }
                if (k == 2) {
                    double av = (double) rTot / 3;
                    //////////////////////////////////////////.println("Sce3 -- " + av + " .... " + rTot);
                    hMapIssue.put("Sce3", String.valueOf(av));
//                    if (modelId.equalsIgnoreCase("Last Three Months Average")) {
//                        hm2.put(nextMonth2, String.valueOf(av));
//                        //graphArray.add(hm2);
//                    }
                    if (modelId.equalsIgnoreCase("Last Three Years Average")) {
                        hm2.put(nextMonth2, String.valueOf(av));
                        //graphArray.add(hm2);
                    }
                }
                if (k == 5) {
                    double av = (double) rTot / 6;
                    //////////////////////////////////////////.println("Sce6 -- " + av + " .... " + rTot);
                    hMapIssue.put("Sce6", String.valueOf(av));
//                    if (modelId.equalsIgnoreCase("Last Six Months Average")) {
//                        hm2.put(nextMonth2, String.valueOf(av));
//                        //graphArray.add(hm2);
//                    }
                    if (modelId.equalsIgnoreCase("Last Six Years Average")) {
                        hm2.put(nextMonth2, String.valueOf(av));
                        //graphArray.add(hm2);
                    }
                }
                if (k == 8) {
                    double av = (double) rTot / 9;
                    //////////////////////////////////////////.println("Sce9 -- " + av + " .... " + rTot);
                    hMapIssue.put("Sce9", String.valueOf(av));
//                    if (modelId.equalsIgnoreCase("Last Nine Months Average")) {
//                        hm2.put(nextMonth2, String.valueOf(av));
//                        graphArray.add(hm2);
//                    }
                    if (modelId.equalsIgnoreCase("Last Nine Years Average")) {
                        hm2.put(nextMonth2, String.valueOf(av));
                        graphArray.add(hm2);
                    }
                }
                if (k == 11) {
                    double av = (double) rTot / 12;
                    //////////////////////////////////////////.println("Sce12 -- " + av + " .... " + rTot);
                    hMapIssue.put("Sce12", String.valueOf(av));
//                    if (modelId.equalsIgnoreCase("Last Twelve Months Average")) {
//                        hm2.put(nextMonth2, String.valueOf(av));
//                        // graphArray.add(hm2);
//                    }
                    if (modelId.equalsIgnoreCase("Last Twelve Years Average")) {
                        hm2.put(nextMonth2, String.valueOf(av));
                        // graphArray.add(hm2);
                    }
                }
                if (custModBasis2.equalsIgnoreCase("Weighted Average")) {
                    //////////////////////////////////////////.println(custModBasis2+" in Not  equal");
                    double bb = (double) custTotal / 100;
                    ScenarioC sc = new ScenarioC();
                    String bb2 = sc.getModifiedNumber(bb, "");
                    custMapIssue.put(oldpVal, bb2);
                    if (modelId.equalsIgnoreCase(custModName2)) { //custModName2
                        hMapIssue.put(custModName2, bb2); // hMapIssue.put("Sce9", new Float(av));
                        if (!modelId.equalsIgnoreCase("--Select--")) {
                            hm2.put(nextMonth2, bb2);
                            //graphArray.add(hm2);
                        }
                    }
                } else if (custModBasis2.equalsIgnoreCase("Simple Average")) {
                    //////////////////////////////////////////.println(custModBasis2+" in equal");
                    int tVal = 1;
                    tVal = cMonths.size();
                    if (tVal == 0) {
                        tVal = 1;
                    }
                    double bb = (double) custTotal / tVal;
                    ScenarioC sc = new ScenarioC();
                    String bb2 = sc.getModifiedNumber(bb, "");
                    custMapIssue.put(oldpVal, bb2);
                    if (modelId.equalsIgnoreCase(custModName2)) { //custModName2
                        hMapIssue.put(custModName2, bb2); // hMapIssue.put("Sce9", new Float(av));
                        if (!modelId.equalsIgnoreCase("--Select--")) {
                            hm2.put(nextMonth2, bb2);
                            //graphArray.add(hm2);
                        }
                    }
                } else if (custModBasis2.equalsIgnoreCase("Sum")) {

                    // float bb = (float)custTotal;
                    double bb = (double) custTotal2;
                    ScenarioC sc = new ScenarioC();
                    String bb2 = sc.getModifiedNumber(bb, "");
                    //////////////////////////////////////////.println(custModBasis2+" Sum--./ "+custTotal+" bb2 "+bb2+" bb = "+bb);
                    custMapIssue.put(oldpVal, new Double(bb2));
                    if (modelId.equalsIgnoreCase(custModName2)) { //custModName2
                        hMapIssue.put(custModName2, bb2); // hMapIssue.put("Sce9", new Float(av));
                        if (!modelId.equalsIgnoreCase("--Select--")) {
                            hm2.put(nextMonth2, bb2);
                            //graphArray.add(hm2);
                        }
                    }
                }

            }
            // to put for the maximum and minimum

            if (custModBasis2.equalsIgnoreCase("Maximum")) {
                //////////////////////////////////////////.println(custModBasis2+" Maximum");
                double bb = (double) maxValue;
                ScenarioC sc = new ScenarioC();
                String bb2 = sc.getModifiedNumber(bb, "");
                custMapIssue.put(oldpVal, new Double(bb2));
                if (modelId.equalsIgnoreCase(custModName2)) { //custModName2
                    hMapIssue.put(custModName2, bb2); // hMapIssue.put("Sce9", new Float(av));
                    if (!modelId.equalsIgnoreCase("--Select--")) {
                        hm2.put(nextMonth2, bb2);
                        //graphArray.add(hm2);
                    }
                }
            } else if (custModBasis2.equalsIgnoreCase("Minimum")) {
                //////////////////////////////////////////.println(custModBasis2+" Minimum "+custModName2);
                double bb = (double) minValue;
                ScenarioC sc = new ScenarioC();
                String bb2 = sc.getModifiedNumber(bb, "");
                custMapIssue.put(oldpVal, bb2);
                if (modelId.equalsIgnoreCase(custModName2)) { //custModName2
                    hMapIssue.put(custModName2, bb2); // hMapIssue.put("Sce9", new Float(av));
                    if (!modelId.equalsIgnoreCase("--Select--")) {
                        hm2.put(nextMonth2, bb2);
                        //graphArray.add(hm2);
                    }
                }
            }


            // to find the average of historical data
            double avgSumTwo = 0.00;
            avgSumTwo = sumTwoMonths / 2;

            double avgSumThree = 0.00;
            avgSumThree = sumThreeMonths / 3;

            double avgSumFour = 0.00;
            avgSumFour = sumFourMonths / 4;

            double avgSumSix = 0.00;
            avgSumSix = sumSixMonths / 6;

            // to get the change in percentage
            ScenarioC c = new ScenarioC();
            if (secondLastMon != 0) {
                perChangeFirst = (double) (100 * ((lastMon - secondLastMon) / secondLastMon));//(100*((lastMon-secondLastMon)/lastMon));//(float)((secondLastMon-lastMon)/lastMon);
            }
            double perChangeSecond = 0.00;
            if (thirdLastMon != 0) {
                perChangeSecond = (double) (100 * ((secondLastMon - thirdLastMon) / thirdLastMon));//(100*((secondLastMon-thirdLastMon)/secondLastMon));
            }
            double perChangeThird = 0.00;
            if (fourthLastMon != 0) {
                perChangeThird = (double) (100 * ((thirdLastMon - fourthLastMon) / fourthLastMon));//(100*((thirdLastMon-fourthLastMon)/thirdLastMon));
            }
            double perChangeFourth = 0.00;
            if (fifthLastMon != 0) {
                perChangeFourth = (double) (100 * ((fourthLastMon - fifthLastMon) / fifthLastMon));//(100*((thirdLastMon-fourthLastMon)/thirdLastMon));
            }
            double perChangeFifth = 0.00;
            if (sixthLastMon != 0) {
                perChangeFifth = (double) (100 * ((fifthLastMon - sixthLastMon) / sixthLastMon));//(100*((thirdLastMon-fourthLastMon)/thirdLastMon));
            }
            double perChangeSixth = 0.00;
            if (seventhLastMon != 0) {
                perChangeSixth = (double) (100 * ((sixthLastMon - seventhLastMon) / seventhLastMon));//(100*((thirdLastMon-fourthLastMon)/thirdLastMon));
            }

            // to get the average change in percentage
            double firstAvg = 0.00;
            firstAvg = (perChangeFirst + perChangeSecond) / 2;
            double firstTrend = ((firstAvg) * avgSumTwo) / 100;//(1+perChangeFirst)*lastMon; //thirdLastMon

            double secondAvg = 0.00;
            secondAvg = (perChangeFirst + perChangeSecond) / 2;

            double thirdAvg = 0.00;
            thirdAvg = (perChangeFirst + perChangeSecond + perChangeThird) / 3;

            double fourthAvg = 0.00;
            fourthAvg = (perChangeFirst + perChangeSecond + perChangeThird + perChangeFourth) / 4;

            double sixthAvg = 0.00;
            sixthAvg = (perChangeFirst + perChangeSecond + perChangeThird + perChangeFourth + perChangeFifth + perChangeSixth) / 6;

            double avgsec = (perChangeFirst + perChangeSecond) / 2;

            secondTrend = ((100 + secondAvg) * avgSumTwo) / 100;//(perChangeFirst)*lastMon;//thirdLastMon;

            thirdTrend = ((100 + thirdAvg) * avgSumThree) / 100;
            fourthTrend = ((100 + fourthAvg) * avgSumFour) / 100;
            sixthTrend = ((100 + sixthAvg) * avgSumSix) / 100;

//            if (modelId.equalsIgnoreCase("Last Two Months Average Growth")) {
//                String ss = c.getScenarioValDouble(secondTrend);
//                hMapIssue.put("Last Two Months Average Growth", ss);
//                hm2.put(nextMonth2, ss);
//                //graphArray.add(hm2);
//            }
            if (modelId.equalsIgnoreCase("Last Two Years Average Growth")) {
                String ss = c.getModifiedNumber(secondTrend, "");
                hMapIssue.put("Last Two Years Average Growth", ss);
                hm2.put(nextMonth2, ss);

                //graphArray.add(hm2);
            }

//            if (modelId.equalsIgnoreCase("Last Three Months Average Growth")) {
//                String ss2 = c.getScenarioValDouble(thirdTrend);
//                hMapIssue.put("Last Three Months Average Growth", ss2);
//                hm2.put(nextMonth2, ss2);
//                // graphArray.add(hm2);
//            }
            if (modelId.equalsIgnoreCase("Last Three Years Average Growth")) {
                String ss2 = c.getModifiedNumber(thirdTrend, "");
                hMapIssue.put("Last Three Years Average Growth", ss2);
                hm2.put(nextMonth2, ss2);
                // graphArray.add(hm2);
            }
//            if (modelId.equalsIgnoreCase("Last Four Months Average Growth")) {
//                String ss4 = c.getScenarioValDouble(fourthTrend);
//                hMapIssue.put("Last Four Months Average Growth", ss4);
//                hm2.put(nextMonth2, ss4);
//                // graphArray.add(hm2);
//            }
            if (modelId.equalsIgnoreCase("Last Four Years Average Growth")) {
                String ss4 = c.getModifiedNumber(fourthTrend, "");
                hMapIssue.put("Last Four Years Average Growth", ss4);
                hm2.put(nextMonth2, ss4);
                // graphArray.add(hm2);
            }
//            if (modelId.equalsIgnoreCase("Last Six Months Average Growth")) {
//                String ss6 = c.getScenarioValDouble(sixthTrend);
//                hMapIssue.put("Last Six Months Average Growth", ss6);
//                hm2.put(nextMonth2, ss6);
//                //  graphArray.add(hm2);
//            }
            if (modelId.equalsIgnoreCase("Last Six Years Average Growth")) {
                String ss6 = c.getModifiedNumber(sixthTrend, "");
                hMapIssue.put("Last Six Years Average Growth", ss6);
                hm2.put(nextMonth2, ss6);
                //  graphArray.add(hm2);
            }

            graphArray.add(hm2);
            totIssue.put(oldpVal, hMapIssue);
            //////("Amit here is hm2" + hm2);
        }


        datesVa.add(nextMonth2);
//        ////(" totIssue " + totIssue);
//        ////(" graphArray " + graphArray);
//        ////("datesVa" + datesVa);
        result.append("<table id=\"tablesorter\" class=\"tablesorter\" width=\"100%\">");
        result.append("<thead>");
        result.append(result).append("<tr>");
        result.append(result).append("<th style=\"font-weight:bold\">").append(viewByName).append("</th>");

        for (int k = 0; k < datesVa.size(); k++) {
            String b = datesVa.get(k).toString();
            if (k == datesVa.size() - 1) {
//                result = result + "<th style=\"background-color:silver;font-weight:bold\">" + nextMonth + "</th>";
                result.append(result).append("<th style=\"font-weight:bold\">").append(nextMonth).append( "</th>");
            } else {
                result.append(result).append("<th style=\"font-weight:bold\">").append(b).append("</th>");
            }
        }
        //  ////("getNormalHM().size() in displayscnrparms is : "+getNormalHM().size());
        if (getNormalHM().size() > 0) {
            if (getNormalRetObj().getColumnCount() > 0) {
                //  ////("collect.nonviewbymap is : "+getNonViewByMap());
                //   ////("collect.nonviewbylist is : "+getNonViewByList());
                for (int i = 0; i < getNormalRetObj().getColumnCount() - 1; i++) {
                    //    ////("collect.getNonViewByMap().get(collect.getNonViewByList().get(i))---"+getNonViewByMap().get("A_"+getNonViewByList().get(i)));
                    result.append(result).append("<th style=\"font-weight:bold\">").append(getNonViewByMap().get("A_" + getNonViewByList().get(i))).append("</th>");
                }
            }
            result.append(result).append("<th style=\"font-weight:bold\">Projected Value</th>");
        }

        result.append(result).append("<th style=\"font-weight:bold\">First Yr Growth</th>");
        result.append(result).append("<th style=\"font-weight:bold\">Second Yr Growth</th>");

        result.append(result).append( "</tr>");
        result.append("</thead>");
       result.append("<tbody>");

        int rowSp = 0;
        int newRowSp = 0;
        Vector vvVal = new Vector();
        String temp = "";
        ////////////////////////////////////////.println(viewBy + " fColV " + fColV + " viewByName " + viewByName);

        String coNames[] = pbro.getColumnNames();
        ////////////////////////////////////////.println("coNames length is:: " + coNames.length);
        ////////////////////////////////////////.println(" pbro " + pbro.getRowCount());

        String childEleId = null;
        childEleId = collect.getChildElementId(viewByEle, scenarioId, scenarioParameters);

        if (childEleId == null) {
            childEleId = "-1";
        }

        for (int i = 0; i < pbro.getRowCount(); i++) {

            String oldP = pbro.getFieldValueString(i, viewByName.toUpperCase().trim());
            String par2 = pbro.getFieldValueString(i, viewByName.toUpperCase().trim());

            ////////////////////////////////////////.println(" oldP=-= " + oldP);
//            ////("par2 string is : " + par2);

            if ((i % 2) == 0) {
                temp = "even";
            } else {
                temp = "odd";
            }
            result.append(result + "<tr class=\"" + temp + "\">");
            if (!childEleId.equalsIgnoreCase("-1")) {
                result.append(result + "<td><a href=\"javascript:submitDrill('" + drillUrl + par2.toString() + "')\">" + par2 + "</td>");

            } else {
                result.append( result + "<td>" + par2 + "</td>");
            }
            ViewbyVector.add(par2);
            //////("ViewbyVector is : " + ViewbyVector);
            for (int k = 0; k < datesVa.size(); k++) {
                String b = datesVa.get(k).toString();
                //////(" b is : " + b);
                //////////////////////////////////////////.println(k + " datesVa " + datesVa.size());
                String vv = "";
                if (k < datesVa.size() - 1) {
                    vv = pbro.getFieldValueString(i, b.toUpperCase().trim());
                }
                if (k == datesVa.size() - 1) {
                    //   ////(" in if--");
                    String nsce1Value = "0";
                    String nsce2Value = "0";
                    String nsce3Value = "0";
                    String nsce6Value = "0";
                    String nsce9Value = "0";
                    String nsce12Value = "0";

                    String par = oldP;
                    //   ////("par - " + par);
                    HashMap vals = (HashMap) totIssue.get(par);
                    //   ////("vals---" + vals);
                    ArrayList newV = new ArrayList();
                    LinkedHashMap hhV = new LinkedHashMap();

                    String sce1V = "0";
                    ScenarioC sc = new ScenarioC();
                    ////////////////////////////////////////.println(" vals " + vals);
                    if (vals.containsKey("Sce1")) {
                        sce1V = ((String) vals.get("Sce1"));
                        nsce1Value = sc.getModifiedNumber(Double.parseDouble(sce1V), "");
                        //   ////("nsce1Value--" + nsce1Value);
                        hhV.put("Sce1", nsce1Value);
                        lastMonthg = Double.parseDouble(sce1V);
                    }


                    //////////////////////////////////////////.println(par + " par- sce1V //..-- " + sce1V);

                    //newV.add(hhV);

                    String sce2Value = "0";
                    if (vals.containsKey("Sce2")) {
                        String sce2V = ((String) vals.get("Sce2"));
                        nsce2Value = sc.getModifiedNumber(Double.parseDouble(sce2V), "");
                        hhV.put("Sce2", nsce2Value);
                        secondTrendg = Double.parseDouble(sce2V);
                        //////////////////////////////////////////.println("nsce2Value .- " + nsce2Value);
                        //newV.add(hhV);
                    }

                    String sce3Value = "0";
                    if (vals.containsKey("Sce3")) {
                        String sce3V = ((String) vals.get("Sce3"));
                        nsce3Value = sc.getModifiedNumber(Double.parseDouble(sce3V), "");
                        hhV.put("Sce3", nsce3Value);
                        thirdTrendg = Double.parseDouble(sce3V);
                        //newV.add(hhV);
                    }

                    String sce6Value = "0";
                    if (vals.containsKey("Sce6")) {
                        String sce6V = ((String) vals.get("Sce6"));
                        nsce6Value = sc.getModifiedNumber(Double.parseDouble(sce6V), "");
                        hhV.put("Sce6", nsce6Value);
                        sixthTrendg = Double.parseDouble(sce6V);
                        //newV.add(hhV);
                    }

                    String sce9Value = "0";
                    if (vals.containsKey("Sce9")) {
                        String sce9V = ((String) vals.get("Sce9"));
                        nsce9Value = sc.getModifiedNumber(Double.parseDouble(sce9V), "");
                        hhV.put("Sce9", nsce9Value);
                        ninthTrendg = Double.parseDouble(sce9V);
                        //newV.add(hhV);
                    }

                    String sce12Value = "0";
                    if (vals.containsKey("Sce12")) {
                        String sce12V = ((String) vals.get("Sce12"));
                        nsce12Value = sce12V;// sc.getScenarioValDouble(sce12V);
                        hhV.put("Sce12", nsce12Value);
                        twelveTrendg = Double.parseDouble(sce12V);

                    }
                    String sceTrend2 = "0";
//                    if (vals.containsKey("Last Two Months Average Growth")) {
//                        String sceT2V = ((String) vals.get("Last Two Months Average Growth"));//.doubleValue();
//                        sceTrend2 = sc.getScenarioValDouble(sceT2V);
//                        hhV.put("Last Two Months Average Growth", sceTrend2);
//
//                    }
                    if (vals.containsKey("Last Two Years Average Growth")) {
                        String sceT2V = ((String) vals.get("Last Two Years Average Growth"));//.doubleValue();
                        sceTrend2 = sceT2V;
                        hhV.put("Last Two Years Average Growth", sceTrend2);
                        secondTrend1 = Double.parseDouble(sceT2V);
                    }

                    String sceTrend3 = "0";
//                    if (vals.containsKey("Last Three Months Average Growth")) {
//                        String sceT3V = ((String) vals.get("Last Three Months Average Growth"));
//                        sceTrend3 = sc.getScenarioValDouble(sceT3V);
//                        hhV.put("Last Three Months Average Growth", sceTrend3);
//
//                    }
                    if (vals.containsKey("Last Three Years Average Growth")) {
                        String sceT3V = ((String) vals.get("Last Three Years Average Growth"));
                        sceTrend3 = sc.getModifiedNumber(Double.parseDouble(sceT3V), "");
                        hhV.put("Last Three Years Average Growth", sceTrend3);
                        thirdTrend2 = Double.parseDouble(sceT3V);
                    }
                    String sceTrend4 = "0";
//                    if (vals.containsKey("Last Four Months Average Growth")) {
//                        String sceT4V = ((String) vals.get("Last Four Months Average Growth"));
//                        sceTrend4 = sc.getScenarioValDouble(sceT4V);
//                        hhV.put("Last Four Months Average Growth", sceTrend4);
//
//                    }
                    if (vals.containsKey("Last Four Years Average Growth")) {
                        String sceT4V = ((String) vals.get("Last Four Years Average Growth"));
                        sceTrend4 = sc.getModifiedNumber(Double.parseDouble(sceT4V), "");
                        hhV.put("Last Four Years Average Growth", sceTrend4);
                        fourthTrend3 = Double.parseDouble(sceT4V);

                    }

                    String sceTrend6 = "0";
//                    if (vals.containsKey("Last Six Months Average Growth")) {
//                        String sceT6V = ((String) vals.get("Last Six Months Average Growth"));
//                        sceTrend6 = sc.getScenarioValDouble(sceT6V);
//                        hhV.put("Last Six Months Average Growth", sceTrend6);
//
//                    }
                    if (vals.containsKey("Last Six Years Average Growth")) {
                        String sceT6V = ((String) vals.get("Last Six Years Average Growth"));
                        sceTrend6 = sc.getModifiedNumber(Double.parseDouble(sceT6V), "");
                        hhV.put("Last Six Years Average Growth", sceTrend6);
                        sixthTrend4 = Double.parseDouble(sceT6V);

                    }

                    //  ////("sceTrend4" + sceTrend4);
                    //  ////("hhV" + hhV);

                    String scustValue = "0";
                    /*
                     * if (vals.containsKey("Custom Model")) { float custV =
                     * ((Float) vals.get("Custom Model")).floatValue();
                     * scustValue = sc.getScenarioVal(custV); hhV.put("Custom
                     * Model", new Float(scustValue));
                     *
                     * }
                     */
                    if (vals.containsKey(modelId)) {
                        String custV = ((String) vals.get(modelId));
                        if (custModBasis2.equalsIgnoreCase("Sum")) {
                            scustValue = sc.getModifiedNumber(Double.parseDouble(custV), "");//custV;

                        } else {
                            scustValue = sc.getModifiedNumber(Double.parseDouble(custV), "");
                        }
                        selectmonthg = Double.parseDouble(custV);
                        //////////////////////////////////////////.println(custV+" custV "+b + " b------------ in if nsce2Value " + scustValue);
                        //   hhV.put("Custom Model", new Float(scustValue));

                    }
                    // }
                    //////////////////////////////////////////.println(modelId+" modelId - "+b + " b in if nsce2Value " + nsce2Value+ " custModName2" +custModName2);
                    //////////////////////////////////////////.println("CustomS -- "+CustomS);
                    double multiplyValue = 0.00;

                    if (modelId.equalsIgnoreCase("--Select--") || modelId.equalsIgnoreCase("")) {
                        result.append(result).append("<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>");
                    }
                    if (modelId.equalsIgnoreCase("Last Month")) {
                        result.append(result).append("<td >").append(nsce1Value).append("</td>");
                        grandTotal = grandTotal + lastMonthg;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = lastMonthg * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
                        GraphMap.put(nextMonth, String.valueOf(lastMonthg));

                    }
                    if (modelId.equalsIgnoreCase("Last Two Years Average")) {
                        result.append(result).append("<td >").append(nsce2Value).append("</td>");
                        grandTotal = grandTotal + secondTrendg;
                        if (getNormalHM().size() > 0) {
                            //   ////("getNormalHM() is : "+getNormalHM());
                            //   ////("pbro.getFieldValueString(i, 0) is : "+pbro.getFieldValueString(i, 0));
                            multiplyValue = secondTrendg * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
                        GraphMap.put(nextMonth, String.valueOf(secondTrendg));
                    }
                    if (modelId.equalsIgnoreCase("Last Three Years Average")) {
                        result.append(result).append("<td >").append(nsce3Value).append("</td>");
                        grandTotal = grandTotal + thirdTrendg;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = thirdTrendg * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
                        GraphMap.put(nextMonth, String.valueOf(thirdTrendg));
                    }
                    if (modelId.equalsIgnoreCase("Last Six Years Average")) {
                        result.append(result).append("<td >").append(nsce6Value).append("</td>");
                        grandTotal = grandTotal + sixthTrendg;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = sixthTrendg * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
                        GraphMap.put(nextMonth, String.valueOf(sixthTrendg));
                    }
                    if (modelId.equalsIgnoreCase("Last Nine Years Average")) {
                        result.append(result).append("<td >").append(nsce9Value).append("</td>");
                        grandTotal = grandTotal + ninthTrendg;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = ninthTrendg * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
                        GraphMap.put(nextMonth, String.valueOf(ninthTrendg));
                    }
                    if (modelId.equalsIgnoreCase("Last Twelve Years Average")) {
                        result.append(result).append("<td >").append(nsce12Value).append( "</td>");
                        grandTotal = grandTotal + twelveTrendg;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = twelveTrendg * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
                        GraphMap.put(nextMonth, String.valueOf(twelveTrendg));
                    }
                    if (modelId.equalsIgnoreCase("Last Two Years Average Growth")) {
                        result.append(result).append("<td >").append(sceTrend2).append("</td>");
                        grandTotal = grandTotal + secondTrend1;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = secondTrend1 * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
                        GraphMap.put(nextMonth, String.valueOf(secondTrend1));
                    }
                    if (modelId.equalsIgnoreCase("Last Three Years Average Growth")) {
                        result.append(result).append("<td >").append(sceTrend3).append( "</td>");
                        grandTotal = grandTotal + thirdTrend2;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = thirdTrend2 * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
                        GraphMap.put(nextMonth, String.valueOf(thirdTrend2));
                    }
                    if (modelId.equalsIgnoreCase("Last Four Years Average Growth")) {
                        result.append(result).append("<td >").append(sceTrend4).append("</td>");
                        grandTotal = grandTotal + fourthTrend3;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = fourthTrend3 * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
                        GraphMap.put(nextMonth, String.valueOf(fourthTrend3));
                    }
                    if (modelId.equalsIgnoreCase("Last Six Years Average Growth")) {
                        result.append(result).append("<td >").append(sceTrend6).append( "</td>");
                        grandTotal = grandTotal + sixthTrend4;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = sixthTrend4 * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
                        GraphMap.put(nextMonth, String.valueOf(sixthTrend4));
                    }
                    String model = "17";
                    Double dou = y1growth;
                    Double doub = y2growth;
                    bd = new BigDecimal(dou);
                    bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                    dou = bd.doubleValue();
                    bd = new BigDecimal(doub);
                    bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                    doub = bd.doubleValue();
                    douArrayList.add(dou);
                    doubArrayList.add(doub);
                    result.append(result).append("<td >").append(dou).append( "</td>");
                    result.append(result).append("<td >").append(doub).append( "</td>");

                    if (modelId.equalsIgnoreCase(custModName2) && (!custModName2.equalsIgnoreCase("--Select--"))) {
                        result.append(result).append("<td >").append(scustValue).append( "</td>");
                        grandTotal = grandTotal + selectmonthg;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = selectmonthg * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
                        GraphMap.put(nextMonth, String.valueOf(selectmonthg));
                    }


                    if (getNormalHM().size() > 0) {
//                        ScenarioC sc = new ScenarioC();
                        String finalMsrVal = sc.getModifiedNumber(Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", "")), "");
                        String finalValue = sc.getModifiedNumber(multiplyValue, "");
                        msrGrandTotal = msrGrandTotal + Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        projGrandTotal = projGrandTotal + Double.parseDouble(String.valueOf(multiplyValue));
//                        ////("finalMsrVal is : "+finalMsrVal);
//                        ////("multiplyvalue is : "+multiplyValue);
//                        ////("finalvalue is : "+finalValue);
                        result.append(result).append("<td style=\"").append(temp).append("\">").append(finalMsrVal).append(" </td>");
                        result.append(result).append("<td >").append(finalValue).append( "</td>");
                        for (int v = 0; v < getNormalRetObj().getColumnCount() - 1; v++) {
                            GraphMap.put(getNonViewByMap().get("A_" + getNonViewByList().get(v)), getNormalHM().get(pbro.getFieldValueString(v, 0)).toString().replace("[", "").replace("]", ""));
                        }
                        GraphMap.put("Projected Value", String.valueOf(multiplyValue));
                    }

                    result.append(result).append("</tr>");
//                    ////("i before is : " + i);
//                    ////("pbro.getrowcount before is : " + pbro.getRowCount());
//                    ////("pbro.getcolcount before is : " + pbro.getColumnCount());
//                    if (i == pbro.getRowCount() - 1) {
//                        result = result + "<tr>";
//                        result = result + "<td style=\"background-color:silver\">Grand Total</td>";
////                        for (int gr = 0; gr < pbro.getColumnCount(); gr++) {
////                            if (k != datesVa.size() - 1) {
////                        ////("in if of datesvasize");
////                        if (k != datesVa.size() - 1) {
////                            for (int fgt = 1; fgt < pbro.getColumnCount(); fgt++) {
////                                for (int frgt = 0; frgt < pbro.getRowCount(); frgt++) {
////                                    String[] colname = pbro.getColumnNames();
////                                    ////("pbro.getFieldValueString(frgt, colname[fgt]) is : " + pbro.getFieldValueString(frgt, colname[fgt]));
////                                    fieldGrandTotal = fieldGrandTotal + Double.parseDouble(pbro.getFieldValueString(frgt, colname[fgt]));
////                                    ////("fieldGrandTotal is : " + fieldGrandTotal);
//////                            grandTotal = grandTotal + Double.parseDouble(pbro.getFieldValueString(frgt, colname[fgt]));
//////                            ////("grandTotal is : " + grandTotal);
////                                }
////                                result = result + "<td style=\"background-color:silver\">feeeeee" + sc.getScenarioValDouble(String.valueOf(fieldGrandTotal)) + "</td>";
////                                fieldGrandTotal = 0;
////                            }
////                        } else {
//                        result = result + "<td style=\"background-color:silver\">grrrrrr" + sc.getScenarioValDouble(String.valueOf(grandTotal)) + "</td>";
////                        }
////                            }
////                        }
//                        if (getNormalHM().size() > 0) {
//                            result = result + "<td style=\"background-color:silver\">grr" + sc.getScenarioValDouble(String.valueOf(msrGrandTotal)) + "</td>";
//                            result = result + "<td style=\"background-color:silver\">grrr" + sc.getScenarioValDouble(String.valueOf(projGrandTotal)) + "</td>";
//                        }
//                        result = result + "</tr>";
//                    }
                } else {
                    ////////////////////////////////////////.println(" in else " + b);
                    ScenarioC sc = new ScenarioC();
                    String nVal = pbro.getFieldValueString(i, b.toUpperCase().trim());
                    String v = sc.getModifiedNumber(Double.parseDouble(nVal), "");
//                    ////("v is : " + v);
                    result.append(result).append("<td>").append(v).append( "</td>");
                    GraphMap.put(b.toUpperCase().trim(), nVal);
//                    String b = datesVa.get(k).toString();
                    if (k == 1) {
                        String nVal1 = pbro.getFieldValueString(i, datesVa.get(k - 1).toString().trim());
                        if (nVal1.equalsIgnoreCase("0")) {
                            y1growth = 0.00;
                        } else {
                            y1growth = (Double.parseDouble(nVal) - Double.parseDouble(nVal1)) * 100 / Double.parseDouble(nVal1);

                        }

                    }
                    if (k == 2) {
                        String nVal1 = pbro.getFieldValueString(i, datesVa.get(k - 1).toString().trim());
                        if (nVal1.equalsIgnoreCase("0")) {
                            y2growth = 0.00;
                        } else {
                            y2growth = (Double.parseDouble(nVal) - Double.parseDouble(nVal1)) * 100 / Double.parseDouble(nVal1);
                        }


                    }


                }

            }
            GraphVector.add(GraphMap);
            GraphMap = new LinkedHashMap();
//            ////.println("GraphVector is : " + GraphVector);
            sumdouble1 = sumdouble1 + y1growth;
            sumdoubledouB2 = sumdoubledouB2 + y2growth;
            sumdouble3 = sumdouble1 / douArrayList.size();
            sumdoubledouB4 = sumdoubledouB2 / doubArrayList.size();
            if (i == pbro.getRowCount() - 1) {
                ScenarioC sc = new ScenarioC();
                result.append(result).append("<tr class=\"even\">");
                result.append(result).append( "<td style=\"font-weight:bold;font-color:blue\">Grand Total</td>");
                for (int fgt = 1; fgt < pbro.getColumnCount(); fgt++) {
                    for (int frgt = 0; frgt < pbro.getRowCount(); frgt++) {
                        String[] colname = pbro.getColumnNames();
//                        ////("pbro.getFieldValueString(frgt, colname[fgt]) is : " + pbro.getFieldValueString(frgt, colname[fgt]));
                        fieldGrandTotal = fieldGrandTotal + Double.parseDouble(pbro.getFieldValueString(frgt, colname[fgt]));
//                        ////("fieldGrandTotal is : " + fieldGrandTotal);
                    }
                    result.append(result).append("<td style=\"font-weight:bold\">").append(sc.getModifiedNumber(fieldGrandTotal, "")).append("</td>");
                    fieldGrandTotal = 0.00;
                }
                result.append(result).append("<td style=\"font-weight:bold\">").append(sc.getModifiedNumber(grandTotal, "")).append("</td>");
                if (getNormalHM().size() > 0) {
                    result.append(result).append("<td style=\"font-weight:bold\">").append(sc.getModifiedNumber(msrGrandTotal, "")).append("</td>");
                    result.append(result).append("<td style=\"font-weight:bold\">").append(sc.getModifiedNumber(projGrandTotal, "")).append( "</td>");
                }

                bd = new BigDecimal(sumdouble3);
                bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                sumdouble3 = bd.doubleValue();
                bd = new BigDecimal(sumdoubledouB4);
                bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                sumdoubledouB4 = bd.doubleValue();

                result.append(result).append("<td style=\"font-weight:bold\">").append(sumdouble3).append( "</td>");
                result.append(result).append("<td style=\"font-weight:bold\">").append(sumdoubledouB4).append( "</td>");

                result.append(result).append( "</tr>");
            }
        }

        result.append(result).append("</table>");

        result.append(result).append("<br>");

        result.append(result).append("<table>");
        result.append(result).append( "<tr>");
        result.append(result).append("<td>T=Trillion</td>");
        result.append(result).append( "<td>B=Billion</td>");
        result.append(result).append( "<td>M=Million</td>");
        result.append(result).append("<td>K=Thousand</td>");
        result.append(result).append("</tr>");
        result.append(result).append( "</table>");

        ////////////////////////////////////////.println(" graphArray--= -" + graphArray);
        //  ////(" result-========== " + result);
        all.put("graphArray", graphArray);
        all.put("result", result);
        all.put("datesVa", datesVa);


        all.put("viewByName", viewByName);
        all.put("modelId", modelId);
        all.put("histMonths", histMonths);
        all.put("dimensionId", dimensionId);

        // ////("all in getscenariotable method is : " + all);

        setViewBy(ViewbyVector);
        setDataset(GraphVector);
//        //("GraphVector is : " + GraphVector);
//        //("getSession is : " + getSession());
//        //("getresponse is : " + getResponse());
//        String GraphPath = getGraph(GraphVector, ViewbyVector, getSession(), getResponse(), getResponse().getWriter());
//        //("GraphPath=" + GraphPath);

        return all;
    }

    public String displayTable(HashMap ParametersHashMap, HashMap TableHashMap) throws Exception {

        ArrayList parameterElementIds = new ArrayList();
        ArrayList ParametersNames = new ArrayList();
        ArrayList timeRangeDetails = new ArrayList();
        LinkedHashMap TimeDimHashMap = null;
        ArrayList TimeDimList = new ArrayList();
        String UserFolderIds = "";
        ArrayList rowEdgeParamIds = new ArrayList();
        ArrayList rowEdgeParamNames = new ArrayList();
        ArrayList colEdgeParamNames = new ArrayList();
        ArrayList reportQryElementIds = new ArrayList();
        ArrayList reportQryAggregations = new ArrayList();
        ScenarioTemplateDAO tempDAO = new ScenarioTemplateDAO();
        ArrayList timeA = new ArrayList();
        String lastDate = "";
        ArrayList qryCols = new ArrayList();
        HashMap ParametersValues = new HashMap();
        ArrayList monthsArray = new ArrayList();
        ArrayList param = new ArrayList();
        HashMap colViewMap = new HashMap();
        ArrayList colNamesArray = new ArrayList();
        ArrayList colNew = new ArrayList();
        HashMap colRevMap = new HashMap();
        String tempStr = "";

        //  ////("TableHashMap in displayTable ??????????" + TableHashMap);
        parameterElementIds = (ArrayList) ParametersHashMap.get("Parameters");
        ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
        //  ////("ParametersHashMap-in displayTable---" + ParametersHashMap);
        timeRangeDetails = (ArrayList) ParametersHashMap.get("TimeRangeDetails");
        TimeDimHashMap = (LinkedHashMap) ParametersHashMap.get("TimeDimHashMap");
        TimeDimList = (ArrayList) ParametersHashMap.get("timeParameters");

        UserFolderIds = (String) ParametersHashMap.get("UserFolderIds");
        rowEdgeParamIds = (ArrayList) TableHashMap.get("REP");
        rowEdgeParamNames = (ArrayList) TableHashMap.get("REPNames");
        reportQryElementIds = (ArrayList) TableHashMap.get("Measures");
        colEdgeParamNames.add("Time");
        reportQryAggregations.add("SUM");
        lastDate = tempDAO.getMonthLastDate((String) timeRangeDetails.get(1));
//        timeA.add(0, "Day");
//        timeA.add(1, "PRG_STD");
//        timeA.add(2, lastDate);
//        timeA.add(3, "Month");
//        timeA.add(4, "Year");
        timeA = new ArrayList();
        timeA.add(0, "Day");
        timeA.add(1, "PRG_YEAR_RANGE");
        timeA.add(2, timeRangeDetails.get(0).toString());
        timeA.add(3, timeRangeDetails.get(1).toString());
        timeA.add(4, timeRangeDetails.get(2).toString());
        timeA.add(5, timeRangeDetails.get(3).toString());
        qryCols.add(reportQryElementIds.get(0).toString());


        PbReportQuery repQuery = new PbReportQuery();
        repQuery.setRowViewbyCols(rowEdgeParamIds);
        repQuery.setColViewbyCols(colEdgeParamNames);
        repQuery.setTimeDetails(timeA);
        repQuery.setQryColumns(qryCols);
        repQuery.setColAggration(reportQryAggregations);
        repQuery.setDefaultMeasure(reportQryElementIds.get(0).toString());
        repQuery.setDefaultMeasureSumm(reportQryAggregations.get(0).toString());
        repQuery.setParamValue(ParametersValues);


        PbReturnObject dataObj = repQuery.getPbReturnObject((String) rowEdgeParamIds.get(0));
        ////////////////////////////////////.println("dataObj.getRoeCount() is:: " + dataObj.getRowCount());

        //String qry = getMonthsQry("AS_OF_YEAR", timeRangeDetails, "analyzeScenario");
        String qry = getYearsQry("AS_OF_YEAR", timeRangeDetails, "analyzeScenario");
        PbReturnObject pbro2 = pbDb.execSelectSQL(qry);
        for (int i = 0; i < pbro2.getRowCount(); i++) {
            monthsArray.add(pbro2.getFieldValueString(i, 0));
        }

        String colNames[] = new String[monthsArray.size() + 1];
        colNames[0] = (String) rowEdgeParamNames.get(0);
        for (int y = 1; y < colNames.length; y++) {
            colNames[y] = (String) monthsArray.get(y - 1);
        }

        colViewMap = repQuery.crossTabNonViewByMap;
        colNamesArray = repQuery.crossTabNonViewBy;

        ////////////////////////////////////////.println("colViewMap in displayTTable is:: " + colViewMap);
        ////////////////////////////////////////.println("colNamesArray in displayTTable is:: " + colNamesArray);

        for (int y = 0; y < colNamesArray.size(); y++) {
            String qName = colNamesArray.get(y).toString();
            if (colViewMap.containsKey(qName)) {
                colNew.add(colViewMap.get(qName).toString());
            }
            colRevMap.put(colViewMap.get(qName).toString(), qName);
        }

        PbReturnObject pbro = new PbReturnObject();
        pbro.setColumnNames(colNames);
        for (int m = 0; m < dataObj.getRowCount(); m++) {
            pbro.setFieldValueString((String) rowEdgeParamNames.get(0), dataObj.getFieldValueString(m, 0));
            param.add(dataObj.getFieldValueString(m, 0));
            for (int y = 0; y < monthsArray.size(); y++) {
                String scMon = monthsArray.get(y).toString();
                String dataValue = "0";
                if (colNew.contains(scMon)) {
                    ////////////////////////////////////////.println("in if 1");
                    if (colRevMap.containsKey(scMon)) {
                        ////////////////////////////////////////.println("in if 2");
                        String qName = "";
                        qName = (String) colRevMap.get(scMon);
                        dataValue = dataObj.getFieldValueString(m, qName);
                        ////////////////////////////////////////.println("dataValue is:: " + dataValue);
                    }
                }
                pbro.setFieldValueString(scMon, dataValue);
            }
            pbro.addRow();
        }

        pbro.writeString();

        StringBuilder dispTableResult = new StringBuilder(500);
        dispTableResult.append("<table id=\"tablesorter\" class=\"tablesorter\" width=\"100%\"><thead><tr>");
//        String dispTableResult = "<table id=\"tablesorter\" class=\"tablesorter\" width=\"100%\">";
//        dispTableResult += "<thead><tr>";
//        dispTableResult += "<tr>";
        for (int i = 0; i < rowEdgeParamNames.size(); i++) {
            dispTableResult.append("<th style=\"font-weight:bold\">").append((String) rowEdgeParamNames.get(i)).append("</th>");
//            dispTableResult += "<th style=\"font-weight:bold\">";
//            dispTableResult += (String) rowEdgeParamNames.get(i);
//            dispTableResult += "</th>";
        }

        for (int i = 0; i < pbro2.getRowCount(); i++) {
            dispTableResult.append("<th style=\"font-weight:bold\">").append((String) pbro2.getFieldValueString(i, 0)).append("</th>");
//            dispTableResult += "<th style=\"font-weight:bold\">";
//            dispTableResult += (String) pbro2.getFieldValueString(i, 0);
//            dispTableResult += "</th>";
        }
//        dispTableResult += "</tr>";
//        dispTableResult += "</thead>";
//        dispTableResult += "<tbody>";
        dispTableResult.append("</tr></thead><tbody>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            if ((i % 2) == 0) {
                tempStr = "even";
            } else {
                tempStr = "odd";
            }
            dispTableResult.append("<tr class='").append(tempStr).append("'><td>").append((String) pbro.getFieldValueString(i, 0)).append("</td>");
//            dispTableResult += "<tr class=\"" + tempStr + "\">";
//            dispTableResult += "<td>";
//            dispTableResult += (String) pbro.getFieldValueString(i, 0);
//            dispTableResult += "</td>";

            for (int h = 0; h < monthsArray.size(); h++) {
                dispTableResult.append("<td>").append((String) pbro.getFieldValueString(i, (String) monthsArray.get(h))).append("</td>");
//                dispTableResult += "<td>";
//                dispTableResult += (String) pbro.getFieldValueString(i, (String) monthsArray.get(h));
//                dispTableResult += "</td>";
            }
            dispTableResult.append("</tr>");
//            dispTableResult += "</tr>";
        }
        dispTableResult.append("</tbody></table>");
//        dispTableResult += "</tbody>";
//        dispTableResult += "</table>";

        return dispTableResult.toString();
    }

    public HashMap getCompareScenarioTable(ArrayList colViewByVal, PbReturnObject dataObj, String scenarioId, ArrayList timeRangeDetails, String viewBy, HashMap crossTabNonViewByMap, LinkedHashMap scenarioViewByMain, LinkedHashMap ModelsMap, HashMap scenarioParameters, String drillUrl) throws Exception {
        ScenarioC sc = new ScenarioC();
        StringBuffer compareTableResult = new StringBuffer("");
        HashMap all = new HashMap();
        String tempStr = "";
        ArrayList param = new ArrayList();
        String scnMon = "";
        String dataValue = "";
        String columnName = "";
        ArrayList colNew = new ArrayList();
        HashMap colRevMap = new HashMap();
        ArrayList monthsArray = new ArrayList();
        String finalValue = "";
        String value = "";
        String value1 = "";
        String value2 = "";
        String value3 = "";
        double doubleValue = 0.00;
        String maximumValue = "";
        double maximumDoubleValue = 0.00;
        String minimumValue = "";
        double minimumDoubleValue = 0.00;
        HashMap customModelDetailsMap = null;
        HashMap calcMeasureQueryResult = new LinkedHashMap();
        ArrayList reportQryElementIds = new ArrayList();
        ArrayList reportQryAggregations = new ArrayList();
        PbReportQuery eleRepQuery = new PbReportQuery();
        ArrayList timeA = new ArrayList();
        double fieldGrandTotal = 0.00;
        double calcGrandTotal = 0.00;
        ArrayList fieldGrandTotalList = new ArrayList();
        ArrayList calcGrandTotalList = new ArrayList();
        double fieldGrandTotal1 = 0.00;
        double calcGrandTotal1 = 0.00;
        double fieldGrandTotal2 = 0.00;
        double calcGrandTotal2 = 0.00;
        ArrayList finalField = new ArrayList();
        ArrayList finalCalc = new ArrayList();


        //Move Query to reource bundle //Change inner Queru support more than one measure

        //  ////("collect.timeDetailsArray is:: "+viewerCollect.timeDetailsArray);


        String calcMeasureQuery = "SELECT   ELEMENT_ID,   AGGREGATION_TYPE, "
                + " BUSS_TABLE_ID,  REF_ELEMENT_TYPE,  ACTUAL_COL_FORMULA,  REFFERED_ELEMENTS "
                + " FROM PRG_USER_ALL_INFO_DETAILS where element_id in ("
                + " select case when  "
                + " INSTR(CALC_ELEMENT_ID,',',1,1) >0 and  INSTR(CALC_ELEMENT_ID,',',1,1) is not null "
                + " then    SUBSTR( CALC_ELEMENT_ID,1,INSTR(CALC_ELEMENT_ID,',',1,1)-1)    "
                + " else  CALC_ELEMENT_ID end CALC_ELEMENT_ID  "
                + " FROM SCENARIO_MASTER where SCENARIO_ID =" + viewerCollect.scenarioId + " ) ";
        //  ////("calcMeasureQuery" + calcMeasureQuery);
        //  ////("collect.scenarioRowViewbyValues.get(0).toString() is:: "+viewerCollect.scenarioRowViewbyValues.get(0).toString());

        PbReturnObject retObj = pbDb.execSelectSQL(calcMeasureQuery);
        if (retObj != null && retObj.getRowCount() > 0) {
            reportQryElementIds.add(retObj.getFieldValueString(0, 0));
            reportQryAggregations.add(retObj.getFieldValueString(0, 1));

            // ////("scenarioRowViewbyValues" + viewerCollect.scenarioRowViewbyValues);
            // ////("reportQryElementIds" + reportQryElementIds);
            eleRepQuery.setQryColumns(reportQryElementIds);
            //  ////("reportQryAggregations" + reportQryAggregations);
            eleRepQuery.setColAggration(reportQryAggregations);
            //  ////("viewerCollect.scenarioParametersValue" + viewerCollect.scenarioParametersValues);
            eleRepQuery.setParamValue(viewerCollect.scenarioParametersValues);
            eleRepQuery.setRowViewbyCols(viewerCollect.scenarioRowViewbyValues);
            timeA.add(0, "Day");
            timeA.add(1, "PRG_YEAR_RANGE");
            timeA.add(2, viewerCollect.timeDetailsArray.get(0).toString());
            timeA.add(3, viewerCollect.timeDetailsArray.get(1).toString());
            timeA.add(4, viewerCollect.timeDetailsArray.get(2).toString());
            timeA.add(5, viewerCollect.timeDetailsArray.get(3).toString());
            eleRepQuery.setTimeDetails(timeA);
            PbReturnObject pbro3 = eleRepQuery.getPbReturnObject((String) viewerCollect.scenarioRowViewbyValues.get(0));

            if (pbro3 != null && pbro3.getRowCount() > 0) {
                //  ////("column list"+ pbro3.getColumnNames());
                for (int looper = 0; looper < pbro3.getRowCount(); looper++) {
                    //  ////("pbro3 0" + pbro3.getFieldValueString(looper, 0));
                    //  ////("pbro3 0" + pbro3.getFieldValueString(looper, 1));
                    calcMeasureQueryResult.put(pbro3.getFieldValueString(looper, "A_" + (String) viewerCollect.scenarioRowViewbyValues.get(0)), String.valueOf(pbro3.getFieldValueBigDecimal(looper, 1)));
                }
            }
            // ////("calcMeasureQueryResult " + calcMeasureQueryResult);
        }



        // Get All The Selected Models To Be Compared
        String[] modelKeys = (String[]) ModelsMap.keySet().toArray(new String[0]);

        ArrayList compareModelNamesArray = new ArrayList();
        ArrayList modelNamesArrayList = new ArrayList();
        for (int y = 1; y < modelKeys.length; y++) {
            modelNamesArrayList = (ArrayList) ModelsMap.get(modelKeys[y]);
            compareModelNamesArray.add(modelNamesArrayList.get(1));
        }

        // Get Historical and Scenario Months
        //String qry = getMonthsQry("AS_OF_YEAR", timeRangeDetails, "analyzeScenario");
        String qry = getYearsQry("AS_OF_YEAR", timeRangeDetails, "analyzeScenario");
        PbReturnObject pbro2 = pbDb.execSelectSQL(qry);
        for (int i = 0; i < pbro2.getRowCount(); i++) {
            monthsArray.add(pbro2.getFieldValueString(i, 0));
        }

        // Build PbReturnObject starting from first month to last month in ORDER
        for (int y = 0; y < colViewByVal.size(); y++) {
            String qName = colViewByVal.get(y).toString();
            if (crossTabNonViewByMap.containsKey(qName)) {
                colNew.add(crossTabNonViewByMap.get(qName).toString());
            }
            colRevMap.put(crossTabNonViewByMap.get(qName).toString(), qName);
        }

        PbReturnObject pbro = new PbReturnObject();
        String colNames[] = new String[monthsArray.size() + 1];
        String viewByName = getViewByName(viewBy, scenarioViewByMain);
        colNames[0] = viewByName;
        for (int y = 1; y < colNames.length; y++) {
            colNames[y] = (String) monthsArray.get(y - 1);
        }
        pbro.setColumnNames(colNames);

        //////////////////////////////.println("colNew is:: " + colNew);
        //////////////////////////////.println("colRevMap is:: " + colRevMap);

        for (int m = 0; m < dataObj.getRowCount(); m++) {
            pbro.setFieldValueString(viewByName, dataObj.getFieldValueString(m, 0));
            param.add(dataObj.getFieldValueString(m, 0));
            for (int y = 0; y < monthsArray.size(); y++) {
                String scMon = monthsArray.get(y).toString();
                dataValue = "0";
                if (colNew.contains(scMon)) {
                    //////////////////////////////.println("in if 1");
                    if (colRevMap.containsKey(scMon)) {
                        //////////////////////////////.println("in if 2");
                        String qName = "";
                        qName = (String) colRevMap.get(scMon);
                        dataValue = dataObj.getFieldValueString(m, qName);
                        //////////////////////////////.println("dataValue is:: " + dataValue);
                    }
                }
                pbro.setFieldValueString(scMon, dataValue);
            }
            pbro.addRow();
        }


        compareTableResult.append("<table id=\"tablesorter\" class=\"tablesorter\" width=\"100%\">");
        compareTableResult.append("<thead>");
        compareTableResult.append("<tr>");
        compareTableResult.append("<th style=\"font-weight:bold\">");
        compareTableResult.append(viewByName);
        compareTableResult.append("</th>");
        for (int i = 0; i < compareModelNamesArray.size(); i++) {
            compareTableResult.append("<th style=\"font-weight:bold\">");
            compareTableResult.append((String) compareModelNamesArray.get(i));
            compareTableResult.append("</th>");
            compareTableResult.append("<th style=\"font-weight:bold\">");
            compareTableResult.append("Calculated Value");
            compareTableResult.append("</th>");
        }
        compareTableResult.append("</tr>");
        compareTableResult.append("</thead>");

        compareTableResult.append("<tbody>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            //////("pbro view By "+ pbro.getFieldValueString(i, 0));
            if ((i % 2) == 0) {
                tempStr = "even";
            } else {
                tempStr = "odd";
            }
            compareTableResult.append("<tr class=\"" + tempStr + "\">");
            compareTableResult.append("<td>");
            compareTableResult.append((String) pbro.getFieldValueString(i, 0));
            compareTableResult.append("</td>");
            String mTest = String.valueOf(calcMeasureQueryResult.get((pbro.getFieldValueString(i, 0))));
            double mulip = 0.0;
            if (mTest == null || mTest.equalsIgnoreCase("NULL")) {
                mulip = 0.0;
            } else {
                mulip = Double.parseDouble(mTest);
            }

            for (int k = 0; k < compareModelNamesArray.size(); k++) {
                if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Two Years Average")) {

                    double newValue = 0.00;
                    if (monthsArray.size() >= 2) {
                        fieldGrandTotal = 0.00;
                        calcGrandTotal = 0.00;
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 2; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            //////////////////////////////.println(" q is:: " + q + " ::: " + value);
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        newValue = newValue / 2;
                        finalValue = sc.getModifiedNumber(newValue, "");

                    } else {
                        finalValue = "0";
                    }
                    fieldGrandTotal = fieldGrandTotal + Double.parseDouble(String.valueOf(newValue));
                    calcGrandTotal = calcGrandTotal + Double.parseDouble(String.valueOf(newValue * mulip));
                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");
                    compareTableResult.append("<td>");
                    compareTableResult.append(sc.getModifiedNumber(newValue * mulip, ""));
                    compareTableResult.append("</td>");
                    fieldGrandTotalList.add(fieldGrandTotal);
                    calcGrandTotalList.add(calcGrandTotal);
                } else if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Three Years Average")) {
                    fieldGrandTotal = 0.00;
                    calcGrandTotal = 0.00;
                    double newValue = 0.00;
                    if (monthsArray.size() >= 3) {
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 3; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        newValue = newValue / 3;
                        finalValue = sc.getModifiedNumber(newValue, "");
                        //////////////////////////////.println("value is:: " + value);
                        //////////////////////////////.println("newValue is:: " + newValue);
                        //////////////////////////////.println("finalValue is:: " + finalValue);
                    } else {
                        finalValue = "0";
                    }
                    fieldGrandTotal = fieldGrandTotal + Double.parseDouble(String.valueOf(newValue));
                    calcGrandTotal = calcGrandTotal + Double.parseDouble(String.valueOf(newValue * mulip));
                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");
                    compareTableResult.append("<td>");
                    compareTableResult.append(sc.getModifiedNumber(newValue * mulip, ""));
                    compareTableResult.append("</td>");
                    fieldGrandTotalList.add(fieldGrandTotal);
                    calcGrandTotalList.add(calcGrandTotal);
                } else if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Six Years Average")) {
                    fieldGrandTotal = 0.00;
                    calcGrandTotal = 0.00;
                    double newValue = 0.00;
                    if (monthsArray.size() >= 6) {
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 6; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        newValue = newValue / 6;
                        finalValue = sc.getModifiedNumber(newValue, "");
                        //////////////////////////////.println("value is:: " + value);
                        //////////////////////////////.println("newValue is:: " + newValue);
                        //////////////////////////////.println("finalValue is:: " + finalValue);
                    } else {
                        finalValue = "0";
                    }
                    fieldGrandTotal = fieldGrandTotal + Double.parseDouble(String.valueOf(newValue));
                    calcGrandTotal = calcGrandTotal + Double.parseDouble(String.valueOf(newValue * mulip));
                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");
                    compareTableResult.append("<td>");
                    compareTableResult.append(sc.getModifiedNumber(newValue * mulip, ""));
                    compareTableResult.append("</td>");
                    fieldGrandTotalList.add(fieldGrandTotal);
                    calcGrandTotalList.add(calcGrandTotal);
                } else if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Nine Years Average")) {
                    fieldGrandTotal = 0.00;
                    calcGrandTotal = 0.00;
                    double newValue = 0.00;
                    if (monthsArray.size() >= 9) {
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 9; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        newValue = newValue / 9;
                        finalValue = sc.getModifiedNumber(newValue, "");
                        //////////////////////////////.println("value is:: " + value);
                        //////////////////////////////.println("newValue is:: " + newValue);
                        //////////////////////////////.println("finalValue is:: " + finalValue);
                    } else {
                        finalValue = "0";
                    }
                    fieldGrandTotal = fieldGrandTotal + Double.parseDouble(String.valueOf(newValue));
                    calcGrandTotal = calcGrandTotal + Double.parseDouble(String.valueOf(newValue * mulip));
                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");
                    compareTableResult.append("<td>");
                    compareTableResult.append(sc.getModifiedNumber(newValue * mulip, ""));
                    compareTableResult.append("</td>");
                    fieldGrandTotalList.add(fieldGrandTotal);
                    calcGrandTotalList.add(calcGrandTotal);
                } else if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Twelve Years Average")) {
                    fieldGrandTotal = 0.00;
                    calcGrandTotal = 0.00;
                    double newValue = 0.00;
                    if (monthsArray.size() >= 12) {
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 12; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        newValue = newValue / 12;
                        finalValue = sc.getModifiedNumber(newValue, "");
                        //////////////////////////////.println("value is:: " + value);
                        //////////////////////////////.println("newValue is:: " + newValue);
                        //////////////////////////////.println("finalValue is:: " + finalValue);
                    } else {
                        finalValue = "0";
                    }
                    fieldGrandTotal = fieldGrandTotal + Double.parseDouble(String.valueOf(newValue));
                    calcGrandTotal = calcGrandTotal + Double.parseDouble(String.valueOf(newValue * mulip));
                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");
                    compareTableResult.append("<td>");
                    compareTableResult.append(sc.getModifiedNumber(newValue * mulip, ""));
                    compareTableResult.append("</td>");
                    fieldGrandTotalList.add(fieldGrandTotal);
                    calcGrandTotalList.add(calcGrandTotal);
                } else if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Two Years Average Growth")) {
                    fieldGrandTotal = 0.00;
                    calcGrandTotal = 0.00;
                    double newValue = 0.00;
                    double firstDouble = 0.00;
                    double secondDouble = 0.00;
                    double changeInFirstPair = 0.00;
                    double changeInSecondPair = 0.00;
                    double AvgChangeIn = 0.00;
                    double Avg = 0.00;
                    double finalDouble = 0.00;
                    if (monthsArray.size() >= 3) {
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 2; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        Avg = newValue / 2;

                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 1));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 2));
                        secondDouble = new Double(value);
                        changeInFirstPair = ((firstDouble - secondDouble) / secondDouble) * 100;

                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 2));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 3));
                        secondDouble = new Double(value);
                        changeInSecondPair = ((firstDouble - secondDouble) / secondDouble) * 100;

                        AvgChangeIn = (changeInFirstPair + changeInSecondPair) / 2;
                        finalDouble = ((100 + AvgChangeIn) * Avg) / 100;

                        finalValue = sc.getModifiedNumber(finalDouble, "");
                        //////////////////////////////.println("value is:: " + value);
                        //////////////////////////////.println("newValue is:: " + newValue);
                        //////////////////////////////.println("finalValue is:: " + finalValue);
                    } else {
                        finalValue = "0";
                    }
                    fieldGrandTotal = fieldGrandTotal + Double.parseDouble(String.valueOf(newValue));
                    calcGrandTotal = calcGrandTotal + Double.parseDouble(String.valueOf(newValue * mulip));

                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");
                    compareTableResult.append("<td>");
                    compareTableResult.append(sc.getModifiedNumber(newValue * mulip, ""));
                    compareTableResult.append("</td>");
                    fieldGrandTotalList.add(fieldGrandTotal);
                    calcGrandTotalList.add(calcGrandTotal);
                } else if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Three Years Average Growth")) {
                    fieldGrandTotal = 0.00;
                    calcGrandTotal = 0.00;
                    double newValue = 0.00;
                    double firstDouble = 0.00;
                    double secondDouble = 0.00;
                    double changeInFirstPair = 0.00;
                    double changeInSecondPair = 0.00;
                    double changeInThirdPair = 0.00;
                    double AvgChangeIn = 0.00;
                    double Avg = 0.00;
                    double finalDouble = 0.00;
                    if (monthsArray.size() >= 4) {
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 3; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        Avg = newValue / 3;

                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 1));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 2));
                        secondDouble = new Double(value);
                        changeInFirstPair = ((firstDouble - secondDouble) / secondDouble) * 100;


                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 2));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 3));
                        secondDouble = new Double(value);
                        changeInSecondPair = ((firstDouble - secondDouble) / secondDouble) * 100;

                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 3));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 4));
                        secondDouble = new Double(value);
                        changeInThirdPair = ((firstDouble - secondDouble) / secondDouble) * 100;

                        AvgChangeIn = (changeInFirstPair + changeInSecondPair + changeInThirdPair) / 3;
                        finalDouble = ((100 + AvgChangeIn) * Avg) / 100;

                        finalValue = sc.getModifiedNumber(finalDouble, "");

                    } else {
                        finalValue = "0";
                    }
                    fieldGrandTotal = fieldGrandTotal + Double.parseDouble(String.valueOf(newValue));
                    calcGrandTotal = calcGrandTotal + Double.parseDouble(String.valueOf(newValue * mulip));
                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");
                    compareTableResult.append("<td>");
                    compareTableResult.append(sc.getModifiedNumber(newValue * mulip, ""));
                    compareTableResult.append("</td>");
                    fieldGrandTotalList.add(fieldGrandTotal);
                    calcGrandTotalList.add(calcGrandTotal);
                } else if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Four Years Average Growth")) {
                    fieldGrandTotal = 0.00;
                    calcGrandTotal = 0.00;
                    double newValue = 0.00;
                    double firstDouble = 0.00;
                    double secondDouble = 0.00;
                    double changeInFirstPair = 0.00;
                    double changeInSecondPair = 0.00;
                    double changeInThirdPair = 0.00;
                    double changeInFourthPair = 0.00;
                    double AvgChangeIn = 0.00;
                    double Avg = 0.00;
                    double finalDouble = 0.00;
                    if (monthsArray.size() >= 5) {
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 4; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        Avg = newValue / 4;

                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 1));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 2));
                        secondDouble = new Double(value);
                        changeInFirstPair = ((firstDouble - secondDouble) / secondDouble) * 100;


                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 2));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 3));
                        secondDouble = new Double(value);
                        changeInSecondPair = ((firstDouble - secondDouble) / secondDouble) * 100;

                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 3));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 4));
                        secondDouble = new Double(value);
                        changeInThirdPair = ((firstDouble - secondDouble) / secondDouble) * 100;

                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 4));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 5));
                        secondDouble = new Double(value);
                        changeInFourthPair = ((firstDouble - secondDouble) / secondDouble) * 100;

                        AvgChangeIn = (changeInFirstPair + changeInSecondPair + changeInThirdPair + changeInFourthPair) / 4;
                        finalDouble = ((100 + AvgChangeIn) * Avg) / 100;

                        finalValue = sc.getModifiedNumber(finalDouble, "");
                        //////////////////////////////.println("value is:: " + value);
                        //////////////////////////////.println("newValue is:: " + newValue);
                        //////////////////////////////.println("finalValue is:: " + finalValue);
                    } else {
                        finalValue = "0";
                    }
                    fieldGrandTotal = fieldGrandTotal + Double.parseDouble(String.valueOf(newValue));
                    calcGrandTotal = calcGrandTotal + Double.parseDouble(String.valueOf(newValue * mulip));
                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");
                    compareTableResult.append("<td>");
                    compareTableResult.append(sc.getModifiedNumber(newValue * mulip, ""));
                    compareTableResult.append("</td>");
                    fieldGrandTotalList.add(fieldGrandTotal);
                    calcGrandTotalList.add(calcGrandTotal);
                } else {
                    ArrayList customModelDetails = getCustomModelBasis(scenarioId, (String) compareModelNamesArray.get(k));
                    String customModelId = (String) customModelDetails.get(0);
                    String customModelBasis = (String) customModelDetails.get(1);
                    customModelDetailsMap = getCustomModelDetails(customModelId);
                    ArrayList customModelMonthsArray = (ArrayList) customModelDetailsMap.get("customModelMonths");
                    ArrayList customModelWeights = (ArrayList) customModelDetailsMap.get("customModelWeights");

                    if (customModelBasis.equalsIgnoreCase("Maximum")) {
                        fieldGrandTotal = 0.00;
                        calcGrandTotal = 0.00;
                        maximumValue = pbro.getFieldValueString(i, (String) customModelMonthsArray.get(0));
                        maximumDoubleValue = new Double(maximumValue);
                        for (int m = 0; m < customModelMonthsArray.size(); m++) {
                            value = pbro.getFieldValueString(i, (String) customModelMonthsArray.get(m));
                            doubleValue = new Double(value);
                            if (doubleValue > maximumDoubleValue) {
                                maximumDoubleValue = doubleValue;
                            }
                        }
                        finalValue = sc.getModifiedNumber(maximumDoubleValue, "");
                        fieldGrandTotal = fieldGrandTotal + Double.parseDouble(String.valueOf(maximumDoubleValue));
                        calcGrandTotal = calcGrandTotal + Double.parseDouble(String.valueOf(maximumDoubleValue * mulip));
                        compareTableResult.append("<td>");
                        compareTableResult.append(finalValue);
                        compareTableResult.append("</td>");
                        compareTableResult.append("<td>");
                        compareTableResult.append(sc.getModifiedNumber(maximumDoubleValue * mulip, ""));
                        compareTableResult.append("</td>");
                        fieldGrandTotalList.add(fieldGrandTotal);
                        calcGrandTotalList.add(calcGrandTotal);
                    } else if (customModelBasis.equalsIgnoreCase("Minimum")) {
                        fieldGrandTotal = 0.00;
                        calcGrandTotal = 0.00;
                        minimumValue = pbro.getFieldValueString(i, (String) customModelMonthsArray.get(0));
                        minimumDoubleValue = new Double(minimumValue);
                        for (int m = 0; m < customModelMonthsArray.size(); m++) {
                            value = pbro.getFieldValueString(i, (String) customModelMonthsArray.get(m));
                            doubleValue = new Double(value);
                            if (doubleValue < minimumDoubleValue) {
                                minimumDoubleValue = doubleValue;
                            }
                        }
                        finalValue = sc.getModifiedNumber(minimumDoubleValue, "");
                        fieldGrandTotal = fieldGrandTotal + Double.parseDouble(String.valueOf(minimumDoubleValue));
                        calcGrandTotal = calcGrandTotal + Double.parseDouble(String.valueOf(minimumDoubleValue * mulip));
                        compareTableResult.append("<td>");
                        compareTableResult.append(finalValue);
                        compareTableResult.append("</td>");
                        compareTableResult.append("<td>");
                        compareTableResult.append(sc.getModifiedNumber(maximumDoubleValue * mulip, ""));
                        compareTableResult.append("</td>");
                        fieldGrandTotalList.add(fieldGrandTotal);
                        calcGrandTotalList.add(calcGrandTotal);
                    } else if (customModelBasis.equalsIgnoreCase("Sum")) {
                        fieldGrandTotal = 0.00;
                        calcGrandTotal = 0.00;
                        double sumDoubleValue = 0.00;
                        for (int m = 0; m < customModelMonthsArray.size(); m++) {
                            value = pbro.getFieldValueString(i, (String) customModelMonthsArray.get(m));
                            doubleValue = new Double(value);
                            sumDoubleValue += doubleValue;
                        }
                        finalValue = sc.getModifiedNumber(sumDoubleValue, "");
                        fieldGrandTotal = fieldGrandTotal + Double.parseDouble(String.valueOf(sumDoubleValue));
                        calcGrandTotal = calcGrandTotal + Double.parseDouble(String.valueOf(sumDoubleValue * mulip));
                        compareTableResult.append("<td>");
                        compareTableResult.append(finalValue);
                        compareTableResult.append("</td>");
                        compareTableResult.append("<td>");
                        compareTableResult.append(sc.getModifiedNumber(sumDoubleValue * mulip, ""));
                        compareTableResult.append("</td>");
                        fieldGrandTotalList.add(fieldGrandTotal);
                        calcGrandTotalList.add(calcGrandTotal);
                    } else if (customModelBasis.equalsIgnoreCase("Simple Average")) {
                        fieldGrandTotal = 0.00;
                        calcGrandTotal = 0.00;
                        double simpleAvgDoubleValue = 0.00;
                        for (int m = 0; m < customModelMonthsArray.size(); m++) {
                            value = pbro.getFieldValueString(i, (String) customModelMonthsArray.get(m));
                            doubleValue = new Double(value);
                            simpleAvgDoubleValue += doubleValue;
                        }
                        simpleAvgDoubleValue = simpleAvgDoubleValue / customModelMonthsArray.size();
                        finalValue = sc.getModifiedNumber(simpleAvgDoubleValue, "");
                        fieldGrandTotal = fieldGrandTotal + Double.parseDouble(String.valueOf(simpleAvgDoubleValue));
                        calcGrandTotal = calcGrandTotal + Double.parseDouble(String.valueOf(simpleAvgDoubleValue * mulip));
                        compareTableResult.append("<td>");
                        compareTableResult.append(finalValue);
                        compareTableResult.append("</td>");
                        compareTableResult.append("<td>");
                        compareTableResult.append(sc.getModifiedNumber(simpleAvgDoubleValue * mulip, ""));
                        compareTableResult.append("</td>");
                        fieldGrandTotalList.add(fieldGrandTotal);
                        calcGrandTotalList.add(calcGrandTotal);
                    } else if (customModelBasis.equalsIgnoreCase("Weighted Average")) {
                        fieldGrandTotal = 0.00;
                        calcGrandTotal = 0.00;
                        double weightedAvgDoubleValue = 0.00;
                        double weight;
                        for (int m = 0; m < customModelMonthsArray.size(); m++) {
                            value = pbro.getFieldValueString(i, (String) customModelMonthsArray.get(m));
                            doubleValue = new Double(value);
                            if (!((String) customModelWeights.get(m)).equalsIgnoreCase("")) {
                                weight = Double.parseDouble((String) customModelWeights.get(m));
                                weightedAvgDoubleValue = weightedAvgDoubleValue + ((weight / 100) * doubleValue);
                            }
                        }

                        finalValue = sc.getModifiedNumber(weightedAvgDoubleValue, "");
                        fieldGrandTotal = fieldGrandTotal + Double.parseDouble(String.valueOf(weightedAvgDoubleValue));
                        calcGrandTotal = calcGrandTotal + Double.parseDouble(String.valueOf(weightedAvgDoubleValue * mulip));
                        compareTableResult.append("<td>");
                        compareTableResult.append(finalValue);
                        compareTableResult.append("</td>");
                        compareTableResult.append("<td>");
                        compareTableResult.append(sc.getModifiedNumber(weightedAvgDoubleValue * mulip, ""));
                        compareTableResult.append("</td>");
                        fieldGrandTotalList.add(fieldGrandTotal);
                        calcGrandTotalList.add(calcGrandTotal);
                    }
                }
            }
            compareTableResult.append("</tr>");
//            ////("fieldGrandTotalList size is : " + fieldGrandTotalList);
//            ////("calcGrandTotalList size is : " + calcGrandTotalList);

            for (int gt = 0; gt < fieldGrandTotalList.size(); gt++) {
//                ////("fieldGrandTotalList1.get(gt) is : " + Double.parseDouble(String.valueOf(fieldGrandTotalList.get(gt))));
                fieldGrandTotal1 = fieldGrandTotal1 + Double.parseDouble(String.valueOf(fieldGrandTotalList.get(gt)));
                calcGrandTotal1 = calcGrandTotal1 + Double.parseDouble(String.valueOf(calcGrandTotalList.get(gt)));
                gt++;
            }
//            ////("fieldGrandTotal1 is : " + fieldGrandTotal1);
//            ////("calcGrandTotal1 is : " + calcGrandTotal1);
            finalField.add(fieldGrandTotal1);
            finalCalc.add(calcGrandTotal1);

            for (int gt = 1; gt < fieldGrandTotalList.size(); gt++) {
                fieldGrandTotal2 = fieldGrandTotal2 + Double.parseDouble(String.valueOf(fieldGrandTotalList.get(gt)));
                calcGrandTotal2 = calcGrandTotal2 + Double.parseDouble(String.valueOf(calcGrandTotalList.get(gt)));
                gt++;
            }

//            ////("fieldGrandTotal2 is : " + fieldGrandTotal2);
//            ////("calcGrandTotal2 is : " + calcGrandTotal2);
            finalField.add(fieldGrandTotal2);
            finalCalc.add(calcGrandTotal2);

//            ////("finalfield is : " + finalField);
//            ////("finalcalc is : " + finalCalc);
//            if (i == pbro.getRowCount() - 1) {
//                compareTableResult.append("<tr>");
//
//                compareTableResult.append("<td>");
//                compareTableResult.append("Grand Total");
//                compareTableResult.append("</td>");
//                ////("compareModelNamesArray.size() is : " + compareModelNamesArray.size());
//                for (int col = 0; col < compareModelNamesArray.size(); col++) {
////                    for (int row = 0; row < pbro.getRowCount(); row++) {
//                    compareTableResult.append("<td>");
//                    compareTableResult.append(sc.getScenarioValDouble(String.valueOf(finalField.get(col))));
//                    compareTableResult.append("</td>");
//                    compareTableResult.append("<td>");
//                    compareTableResult.append(sc.getScenarioValDouble(String.valueOf(finalCalc.get(col))));
//                    compareTableResult.append("</td>");
//                }
////                }
//                compareTableResult.append("</tr>");
//            }
        }

        compareTableResult.append("</tbody>");
        compareTableResult.append("</table>");

        compareTableResult.append("<table>");
        compareTableResult.append("<tr>");
        compareTableResult.append("<td>");
        compareTableResult.append("T=Trillion");
        compareTableResult.append("</td>");

        compareTableResult.append("<td>");
        compareTableResult.append("");
        compareTableResult.append("</td>");

        compareTableResult.append("<td>");
        compareTableResult.append("B=Billion");
        compareTableResult.append("</td>");

        compareTableResult.append("<td>");
        compareTableResult.append("");
        compareTableResult.append("</td>");

        compareTableResult.append("<td>");
        compareTableResult.append("M=Million");
        compareTableResult.append("</td>");

        compareTableResult.append("<td>");
        compareTableResult.append("");
        compareTableResult.append("</td>");

        compareTableResult.append("<td>");
        compareTableResult.append("K=Thousand");
        compareTableResult.append("</td>");
        compareTableResult.append("</tr>");
        compareTableResult.append("</table>");


        all.put("compareTableResult", compareTableResult.toString());
        return all;
    }

    public HashMap getCompareScenarioTableForNewMeasure(ArrayList colViewByVal, PbReturnObject dataObj, String scenarioId, ArrayList timeRangeDetails, String viewBy, HashMap crossTabNonViewByMap, LinkedHashMap scenarioViewByMain, LinkedHashMap ModelsMap, HashMap scenarioParameters, String drillUrl) throws Exception {
        ScenarioC sc = new ScenarioC();
        StringBuffer compareTableResult = new StringBuffer("");
        HashMap all = new HashMap();
        String tempStr = "";
        ArrayList param = new ArrayList();
        String scnMon = "";
        String dataValue = "";
        String columnName = "";
        ArrayList colNew = new ArrayList();
        HashMap colRevMap = new HashMap();
        ArrayList monthsArray = new ArrayList();
        String finalValue = "";
        String value = "";
        String value1 = "";
        String value2 = "";
        String value3 = "";
        double doubleValue = 0.00;
        String maximumValue = "";
        double maximumDoubleValue = 0.00;
        String minimumValue = "";
        double minimumDoubleValue = 0.00;
        HashMap customModelDetailsMap = null;
        HashMap newMeasureHM = new HashMap();
        newMeasureHM = collect.getNormalHm();


        // Get All The Selected Models To Be Compared
        String[] modelKeys = (String[]) ModelsMap.keySet().toArray(new String[0]);

        ArrayList compareModelNamesArray = new ArrayList();
        ArrayList modelNamesArrayList = new ArrayList();
        for (int y = 1; y < modelKeys.length; y++) {
            modelNamesArrayList = (ArrayList) ModelsMap.get(modelKeys[y]);
            compareModelNamesArray.add(modelNamesArrayList.get(1));
        }


        // Get Historical and Scenario Months
        //String qry = getMonthsQry("AS_OF_YEAR", timeRangeDetails, "analyzeScenario");
        String qry = getYearsQry("AS_OF_YEAR", timeRangeDetails, "analyzeScenario");
        PbReturnObject pbro2 = pbDb.execSelectSQL(qry);
        for (int i = 0; i < pbro2.getRowCount(); i++) {
            monthsArray.add(pbro2.getFieldValueString(i, 0));
        }


        // Build PbReturnObject starting from first month to last month in ORDER
        for (int y = 0; y < colViewByVal.size(); y++) {
            String qName = colViewByVal.get(y).toString();
            if (crossTabNonViewByMap.containsKey(qName)) {
                colNew.add(crossTabNonViewByMap.get(qName).toString());
            }
            colRevMap.put(crossTabNonViewByMap.get(qName).toString(), qName);
        }

        PbReturnObject pbro = new PbReturnObject();
        String colNames[] = new String[monthsArray.size() + 1];
        String viewByName = getViewByName(viewBy, scenarioViewByMain);
        colNames[0] = viewByName;
        for (int y = 1; y < colNames.length; y++) {
            colNames[y] = (String) monthsArray.get(y - 1);
        }
        pbro.setColumnNames(colNames);

        //////////////////////////////.println("colNew is:: " + colNew);
        //////////////////////////////.println("colRevMap is:: " + colRevMap);

        for (int m = 0; m < dataObj.getRowCount(); m++) {
            pbro.setFieldValueString(viewByName, dataObj.getFieldValueString(m, 0));
            param.add(dataObj.getFieldValueString(m, 0));
            for (int y = 0; y < monthsArray.size(); y++) {
                String scMon = monthsArray.get(y).toString();
                dataValue = "0";
                if (colNew.contains(scMon)) {
                    //////////////////////////////.println("in if 1");
                    if (colRevMap.containsKey(scMon)) {
                        //////////////////////////////.println("in if 2");
                        String qName = "";
                        qName = (String) colRevMap.get(scMon);
                        dataValue = dataObj.getFieldValueString(m, qName);
                        //////////////////////////////.println("dataValue is:: " + dataValue);
                    }
                }
                pbro.setFieldValueString(scMon, dataValue);
            }
            pbro.addRow();
        }


        compareTableResult.append("<table id=\"tablesorter\" class=\"tablesorter\" width=\"100%\">");
        compareTableResult.append("<thead>");
        compareTableResult.append("<tr>");
        compareTableResult.append("<th style=\"font-weight:bold\">");
        compareTableResult.append(viewByName);
        compareTableResult.append("</th>");
        for (int i = 0; i < compareModelNamesArray.size(); i++) {
            compareTableResult.append("<th style=\"font-weight:bold\">");
            compareTableResult.append((String) compareModelNamesArray.get(i));
            compareTableResult.append("</th>");
        }
        compareTableResult.append("</tr>");
        compareTableResult.append("</thead>");

        compareTableResult.append("<tbody>");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            // ////("pbro view By "+ pbro.getFieldValueString(i, 0));
            if ((i % 2) == 0) {
                tempStr = "even";
            } else {
                tempStr = "odd";
            }
            compareTableResult.append("<tr class=\"" + tempStr + "\">");
            compareTableResult.append("<td>");
            compareTableResult.append((String) pbro.getFieldValueString(i, 0));
            compareTableResult.append("</td>");

            for (int k = 0; k < compareModelNamesArray.size(); k++) {
                if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Two Years Average")) {
                    double newValue = 0.00;
                    if (monthsArray.size() >= 2) {
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 2; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            //////////////////////////////.println(" q is:: " + q + " ::: " + value);
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        newValue = newValue / 2;
                        finalValue = sc.getModifiedNumber(newValue, "");
                        //////////////////////////////.println("value is:: " + value);
                        //////////////////////////////.println("newValue is:: " + newValue);
                        //////////////////////////////.println("finalValue is:: " + finalValue);
                    } else {
                        finalValue = "0";
                    }

                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");
                } else if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Three Years Average")) {
                    double newValue = 0.00;
                    if (monthsArray.size() >= 3) {
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 3; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        newValue = newValue / 3;
                        finalValue = sc.getModifiedNumber(newValue, "");
                        //////////////////////////////.println("value is:: " + value);
                        //////////////////////////////.println("newValue is:: " + newValue);
                        //////////////////////////////.println("finalValue is:: " + finalValue);
                    } else {
                        finalValue = "0";
                    }

                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");
                } else if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Six Years Average")) {
                    double newValue = 0.00;
                    if (monthsArray.size() >= 6) {
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 6; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        newValue = newValue / 6;
                        finalValue = sc.getModifiedNumber(newValue, "");
                        //////////////////////////////.println("value is:: " + value);
                        //////////////////////////////.println("newValue is:: " + newValue);
                        //////////////////////////////.println("finalValue is:: " + finalValue);
                    } else {
                        finalValue = "0";
                    }

                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");
                } else if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Nine Years Average")) {
                    double newValue = 0.00;
                    if (monthsArray.size() >= 9) {
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 9; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        newValue = newValue / 9;
                        finalValue = sc.getModifiedNumber(newValue, "");
                        //////////////////////////////.println("value is:: " + value);
                        //////////////////////////////.println("newValue is:: " + newValue);
                        //////////////////////////////.println("finalValue is:: " + finalValue);
                    } else {
                        finalValue = "0";
                    }

                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");
                } else if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Twelve Years Average")) {
                    double newValue = 0.00;
                    if (monthsArray.size() >= 12) {
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 12; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        newValue = newValue / 12;
                        finalValue = sc.getModifiedNumber(newValue, "");
                        //////////////////////////////.println("value is:: " + value);
                        //////////////////////////////.println("newValue is:: " + newValue);
                        //////////////////////////////.println("finalValue is:: " + finalValue);
                    } else {
                        finalValue = "0";
                    }

                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");
                } else if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Two Years Average Growth")) {

                    double newValue = 0.00;
                    double firstDouble = 0.00;
                    double secondDouble = 0.00;
                    double changeInFirstPair = 0.00;
                    double changeInSecondPair = 0.00;
                    double AvgChangeIn = 0.00;
                    double Avg = 0.00;
                    double finalDouble = 0.00;
                    if (monthsArray.size() >= 3) {
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 2; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        Avg = newValue / 2;

                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 1));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 2));
                        secondDouble = new Double(value);
                        changeInFirstPair = ((firstDouble - secondDouble) / secondDouble) * 100;

                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 2));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 3));
                        secondDouble = new Double(value);
                        changeInSecondPair = ((firstDouble - secondDouble) / secondDouble) * 100;

                        AvgChangeIn = (changeInFirstPair + changeInSecondPair) / 2;
                        finalDouble = ((100 + AvgChangeIn) * Avg) / 100;

                        finalValue = sc.getModifiedNumber(finalDouble, "");
                        //////////////////////////////.println("value is:: " + value);
                        //////////////////////////////.println("newValue is:: " + newValue);
                        //////////////////////////////.println("finalValue is:: " + finalValue);
                    } else {
                        finalValue = "0";
                    }


                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");
                } else if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Three Years Average Growth")) {

                    double newValue = 0.00;
                    double firstDouble = 0.00;
                    double secondDouble = 0.00;
                    double changeInFirstPair = 0.00;
                    double changeInSecondPair = 0.00;
                    double changeInThirdPair = 0.00;
                    double AvgChangeIn = 0.00;
                    double Avg = 0.00;
                    double finalDouble = 0.00;
                    if (monthsArray.size() >= 4) {
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 3; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        Avg = newValue / 3;

                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 1));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 2));
                        secondDouble = new Double(value);
                        changeInFirstPair = ((firstDouble - secondDouble) / secondDouble) * 100;


                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 2));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 3));
                        secondDouble = new Double(value);
                        changeInSecondPair = ((firstDouble - secondDouble) / secondDouble) * 100;

                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 3));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 4));
                        secondDouble = new Double(value);
                        changeInThirdPair = ((firstDouble - secondDouble) / secondDouble) * 100;

                        AvgChangeIn = (changeInFirstPair + changeInSecondPair + changeInThirdPair) / 3;
                        finalDouble = ((100 + AvgChangeIn) * Avg) / 100;

                        finalValue = sc.getModifiedNumber(finalDouble, "");
                        //////////////////////////////.println("value is:: " + value);
                        //////////////////////////////.println("newValue is:: " + newValue);
                        //////////////////////////////.println("finalValue is:: " + finalValue);
                    } else {
                        finalValue = "0";
                    }

                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");
                } else if (((String) compareModelNamesArray.get(k)).equalsIgnoreCase("Last Four Years Average Growth")) {

                    double newValue = 0.00;
                    double firstDouble = 0.00;
                    double secondDouble = 0.00;
                    double changeInFirstPair = 0.00;
                    double changeInSecondPair = 0.00;
                    double changeInThirdPair = 0.00;
                    double changeInFourthPair = 0.00;
                    double AvgChangeIn = 0.00;
                    double Avg = 0.00;
                    double finalDouble = 0.00;
                    if (monthsArray.size() >= 5) {
                        for (int q = monthsArray.size() - 1; q >= monthsArray.size() - 4; q--) {
                            value = pbro.getFieldValueString(i, (String) monthsArray.get(q));
                            doubleValue = new Double(value);
                            newValue += doubleValue;
                        }
                        Avg = newValue / 4;

                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 1));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 2));
                        secondDouble = new Double(value);
                        changeInFirstPair = ((firstDouble - secondDouble) / secondDouble) * 100;


                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 2));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 3));
                        secondDouble = new Double(value);
                        changeInSecondPair = ((firstDouble - secondDouble) / secondDouble) * 100;

                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 3));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 4));
                        secondDouble = new Double(value);
                        changeInThirdPair = ((firstDouble - secondDouble) / secondDouble) * 100;

                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 4));
                        firstDouble = new Double(value);
                        value = pbro.getFieldValueString(i, (String) monthsArray.get(monthsArray.size() - 5));
                        secondDouble = new Double(value);
                        changeInFourthPair = ((firstDouble - secondDouble) / secondDouble) * 100;

                        AvgChangeIn = (changeInFirstPair + changeInSecondPair + changeInThirdPair + changeInFourthPair) / 4;
                        finalDouble = ((100 + AvgChangeIn) * Avg) / 100;

                        finalValue = sc.getModifiedNumber(finalDouble, "");

                    } else {
                        finalValue = "0";
                    }

                    compareTableResult.append("<td>");
                    compareTableResult.append(finalValue);
                    compareTableResult.append("</td>");

                } else {
                    ArrayList customModelDetails = getCustomModelBasis(scenarioId, (String) compareModelNamesArray.get(k));
                    String customModelId = (String) customModelDetails.get(0);
                    String customModelBasis = (String) customModelDetails.get(1);
                    customModelDetailsMap = getCustomModelDetails(customModelId);
                    ArrayList customModelMonthsArray = (ArrayList) customModelDetailsMap.get("customModelMonths");
                    ArrayList customModelWeights = (ArrayList) customModelDetailsMap.get("customModelWeights");

                    if (customModelBasis.equalsIgnoreCase("Maximum")) {
                        maximumValue = pbro.getFieldValueString(i, (String) customModelMonthsArray.get(0));
                        maximumDoubleValue = new Double(maximumValue);
                        for (int m = 0; m < customModelMonthsArray.size(); m++) {
                            value = pbro.getFieldValueString(i, (String) customModelMonthsArray.get(m));
                            doubleValue = new Double(value);
                            if (doubleValue > maximumDoubleValue) {
                                maximumDoubleValue = doubleValue;
                            }
                        }
                        finalValue = sc.getModifiedNumber(maximumDoubleValue, "");
                        compareTableResult.append("<td>");
                        compareTableResult.append(finalValue);
                        compareTableResult.append("</td>");
                    } else if (customModelBasis.equalsIgnoreCase("Minimum")) {
                        minimumValue = pbro.getFieldValueString(i, (String) customModelMonthsArray.get(0));
                        minimumDoubleValue = new Double(minimumValue);
                        for (int m = 0; m < customModelMonthsArray.size(); m++) {
                            value = pbro.getFieldValueString(i, (String) customModelMonthsArray.get(m));
                            doubleValue = new Double(value);
                            if (doubleValue < minimumDoubleValue) {
                                minimumDoubleValue = doubleValue;
                            }
                        }
                        finalValue = sc.getModifiedNumber(minimumDoubleValue, "");
                        compareTableResult.append("<td>");
                        compareTableResult.append(finalValue);
                        compareTableResult.append("</td>");
                    } else if (customModelBasis.equalsIgnoreCase("Sum")) {
                        double sumDoubleValue = 0.00;
                        for (int m = 0; m < customModelMonthsArray.size(); m++) {
                            value = pbro.getFieldValueString(i, (String) customModelMonthsArray.get(m));
                            doubleValue = new Double(value);
                            sumDoubleValue += doubleValue;
                        }
                        finalValue = sc.getModifiedNumber(sumDoubleValue, "");
                        compareTableResult.append("<td>");
                        compareTableResult.append(finalValue);
                        compareTableResult.append("</td>");
                    } else if (customModelBasis.equalsIgnoreCase("Simple Average")) {
                        double simpleAvgDoubleValue = 0.00;
                        for (int m = 0; m < customModelMonthsArray.size(); m++) {
                            value = pbro.getFieldValueString(i, (String) customModelMonthsArray.get(m));
                            doubleValue = new Double(value);
                            simpleAvgDoubleValue += doubleValue;
                        }
                        simpleAvgDoubleValue = simpleAvgDoubleValue / customModelMonthsArray.size();
                        finalValue = sc.getModifiedNumber(simpleAvgDoubleValue, "");
                        compareTableResult.append("<td>");
                        compareTableResult.append(finalValue);
                        compareTableResult.append("</td>");
                    } else if (customModelBasis.equalsIgnoreCase("Weighted Average")) {
                        double weightedAvgDoubleValue = 0.00;
                        double weight;
                        for (int m = 0; m < customModelMonthsArray.size(); m++) {
                            value = pbro.getFieldValueString(i, (String) customModelMonthsArray.get(m));
                            doubleValue = new Double(value);
                            if (!((String) customModelWeights.get(m)).equalsIgnoreCase("")) {
                                weight = Double.parseDouble((String) customModelWeights.get(m));
                                weightedAvgDoubleValue = weightedAvgDoubleValue + ((weight / 100) * doubleValue);
                            }
                        }

                        finalValue = sc.getModifiedNumber(weightedAvgDoubleValue, "");
                        compareTableResult.append("<td>");
                        compareTableResult.append(finalValue);
                        compareTableResult.append("</td>");
                    }

                }
            }
            compareTableResult.append("</tr>");
        }

        compareTableResult.append("</tbody>");
        compareTableResult.append("</table>");

        compareTableResult.append("<table>");
        compareTableResult.append("<tr>");
        compareTableResult.append("<td>");
        compareTableResult.append("T=Trillion");
        compareTableResult.append("</td>");

        compareTableResult.append("<td>");
        compareTableResult.append("");
        compareTableResult.append("</td>");

        compareTableResult.append("<td>");
        compareTableResult.append("B=Billion");
        compareTableResult.append("</td>");

        compareTableResult.append("<td>");
        compareTableResult.append("");
        compareTableResult.append("</td>");

        compareTableResult.append("<td>");
        compareTableResult.append("M=Million");
        compareTableResult.append("</td>");

        compareTableResult.append("<td>");
        compareTableResult.append("");
        compareTableResult.append("</td>");

        compareTableResult.append("<td>");
        compareTableResult.append("K=Thousand");
        compareTableResult.append("</td>");
        compareTableResult.append("</tr>");
        compareTableResult.append("</table>");


        all.put("compareTableResult", compareTableResult.toString());
        return all;
    }

    public String getViewByName(String viewBy, HashMap scenarioViewByMain) {
        String viewByName = "";
        String viewByKeys[] = (String[]) scenarioViewByMain.keySet().toArray(new String[0]);

        for (int i = 0; i < viewByKeys.length; i++) {
            ArrayList viewByArrayList = new ArrayList();
            viewByArrayList = (ArrayList) scenarioViewByMain.get(viewByKeys[i]);
            if (((String) viewByArrayList.get(3)).equalsIgnoreCase(viewBy)) {
                viewByName = (String) viewByArrayList.get(4);
            }
        }
        return viewByName;
    }

    public ArrayList getCustomModelBasis(String scenarioId, String custModelName) {
        ArrayList customModelDetails = new ArrayList();
        String customModelBasis = "";
        String customModelId = "";
        String qry = "";
        PbReturnObject pbro = new PbReturnObject();
        try {
            qry = "select * from prg_custom_model_master where scenario_id=" + scenarioId + " and custom_model_name='" + custModelName + "'";
            pbro = pbDb.execSelectSQL(qry);
            customModelBasis = pbro.getFieldValueString(0, "MODEL_BASIS");
            customModelId = pbro.getFieldValueString(0, "CUSTOM_MODEL_ID");

            customModelDetails.add(customModelId);
            customModelDetails.add(customModelBasis);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return customModelDetails;
    }

    public static HashMap getNormalHM() {
        return normalHM;
    }

    public static void setNormalHM(HashMap aNormalHM) {
        normalHM = aNormalHM;
    }

    public static PbReturnObject getNormalRetObj() {
        return normalRetObj;
    }

    public static void setNormalRetObj(PbReturnObject aNormalRetObj) {
        normalRetObj = aNormalRetObj;
    }

    public static HashMap getNonViewByMap() {
        return NonViewByMap;
    }

    public static void setNonViewByMap(HashMap aNonViewByMap) {
        NonViewByMap = aNonViewByMap;
    }

    public static ArrayList getNonViewByList() {
        return NonViewByList;
    }

    public static void setNonViewByList(ArrayList aNonViewByList) {
        NonViewByList = aNonViewByList;
    }

    public HashMap getCustomModelDetails(String custModelId) {
        ArrayList customModelMonths = new ArrayList();
        ArrayList customModelWeights = new ArrayList();
        HashMap customModelDetailsMap = new HashMap();
        String qry = "";
        PbReturnObject pbro = new PbReturnObject();
        try {
            qry = "select * from prg_custom_model_details where custom_model_id=" + custModelId + " order by 1";
            pbro = pbDb.execSelectSQL(qry);
            int count = pbro.getRowCount();
            for (int i = 0; i < count; i++) {
                customModelMonths.add(pbro.getFieldValueString(i, "CUSTOM_MODEL_MONTHS"));
                customModelWeights.add(pbro.getFieldValueString(i, "CUSTOM_MODEL_WEIGHTS"));
            }
            customModelDetailsMap.put("customModelMonths", customModelMonths);
            customModelDetailsMap.put("customModelWeights", customModelWeights);

            ////////////////////////////////////////.println("customModelDetailsMap is:: "+customModelDetailsMap);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return customModelDetailsMap;
    }
    /*
     * public HashMap getCompareScenarioTable(ArrayList colViewByVal,
     * PbReturnObject dataObj, String scenarioId, ArrayList timeRangeDetails,
     * String viewBy, HashMap crossTabNonViewByMap, LinkedHashMap
     * scenarioViewByMain, LinkedHashMap ModelsMap, HashMap scenarioParameters,
     * String drillUrl) throws Exception { ScenarioC sc = new ScenarioC();
     * StringBuffer compareTableResult = new StringBuffer(""); HashMap all = new
     * HashMap(); String tempStr = ""; ArrayList param = new ArrayList(); String
     * scnMon = ""; String dataValue = ""; String columnName = ""; ArrayList
     * colNew = new ArrayList(); HashMap colRevMap = new HashMap(); ArrayList
     * monthsArray = new ArrayList(); String finalValue = ""; String value = "";
     * String value1 = ""; String value2 = ""; String value3 = ""; double
     * doubleValue = 0; String maximumValue = ""; double maximumDoubleValue = 0;
     * String minimumValue = ""; double minimumDoubleValue = 0; HashMap
     * customModelDetailsMap = null;
     *
     *
     *
     * // Get All The Selected Models To Be Compared String[] modelKeys =
     * (String[]) ModelsMap.keySet().toArray(new String[0]); ArrayList
     * compareModelNamesArray = new ArrayList(); ArrayList modelNamesArrayList =
     * new ArrayList(); for (int y = 1; y < modelKeys.length; y++) {
     * modelNamesArrayList = (ArrayList) ModelsMap.get(modelKeys[y]);
     * compareModelNamesArray.add(modelNamesArrayList.get(1)); }
     *
     *
     * // Get Historical and Scenario Months String qry =
     * getMonthsQry("AS_OF_MONTH", timeRangeDetails,"analyzeScenario");
     * PbReturnObject pbro2 = pbDb.execSelectSQL(qry); for (int i = 0; i <
     * pbro2.getRowCount(); i++) { monthsArray.add(pbro2.getFieldValueString(i,
     * 0)); }
     *
     *
     * // Build PbReturnObject starting from first month to last month in ORDER
     * for (int y = 0; y < colViewByVal.size(); y++) { String qName =
     * colViewByVal.get(y).toString(); if
     * (crossTabNonViewByMap.containsKey(qName)) {
     * colNew.add(crossTabNonViewByMap.get(qName).toString()); }
     * colRevMap.put(crossTabNonViewByMap.get(qName).toString(), qName); }
     *
     * PbReturnObject pbro = new PbReturnObject(); String colNames[] = new
     * String[monthsArray.size() + 1]; String viewByName = getViewByName(viewBy,
     * scenarioViewByMain); colNames[0] = viewByName; for (int y = 1; y <
     * colNames.length; y++) { colNames[y] = (String) monthsArray.get(y - 1); }
     * pbro.setColumnNames(colNames);
     *
     * ////////////////////////////////////////.println("colNew is:: " +
     * colNew); ////////////////////////////////////////.println("colRevMap is::
     * " + colRevMap);
     *
     * for (int m = 0; m < dataObj.getRowCount(); m++) {
     * pbro.setFieldValueString(viewByName, dataObj.getFieldValueString(m, 0));
     * param.add(dataObj.getFieldValueString(m, 0)); for (int y = 0; y <
     * monthsArray.size(); y++) { String scMon = monthsArray.get(y).toString();
     * dataValue = "0"; if (colNew.contains(scMon)) {
     * ////////////////////////////////////////.println("in if 1"); if
     * (colRevMap.containsKey(scMon)) {
     * ////////////////////////////////////////.println("in if 2"); String qName
     * = ""; qName = (String) colRevMap.get(scMon); dataValue =
     * dataObj.getFieldValueString(m, qName);
     * ////////////////////////////////////////.println("dataValue is:: " +
     * dataValue); } } pbro.setFieldValueString(scMon, dataValue); }
     * pbro.addRow(); }
     *
     *
     * compareTableResult.append("<table id=\"tablesorter\"
     * class=\"tablesorter\" width=\"100%\">");
     * compareTableResult.append("<thead>"); compareTableResult.append("<tr>");
     * compareTableResult.append("<th style=\"font-weight:bold\">");
     * compareTableResult.append(viewByName);
     * compareTableResult.append("</th>"); for (int i = 0; i <
     * compareModelNamesArray.size(); i++) { compareTableResult.append("<th
     * style=\"font-weight:bold\">"); compareTableResult.append((String)
     * compareModelNamesArray.get(i)); compareTableResult.append("</th>"); }
     * compareTableResult.append("</tr>");
     * compareTableResult.append("</thead>");
     *
     * compareTableResult.append("<tbody>"); for (int i = 0; i <
     * pbro.getRowCount(); i++) { if ((i % 2) == 0) { tempStr = "even"; } else {
     * tempStr = "odd"; } compareTableResult.append("<tr class=\"" + tempStr +
     * "\">"); compareTableResult.append("<td>");
     * compareTableResult.append((String) pbro.getFieldValueString(i, 0));
     * compareTableResult.append("</td>");
     *
     * for (int k = 0; k < compareModelNamesArray.size(); k++) { if (((String)
     * compareModelNamesArray.get(k)).equalsIgnoreCase("Last Two Months
     * Average")) { double newValue = 0; if(monthsArray.size() >= 2) { for(int
     * q=monthsArray.size()-1;q>=monthsArray.size()-2;q--) { value =
     * pbro.getFieldValueString(i, (String) monthsArray.get(q));
     * ////////////////////////////////////////.println(" q is:: "+q+" :::
     * "+value); doubleValue = new Double(value); newValue += doubleValue; }
     * newValue = newValue / 2; finalValue =
     * sc.getScenarioValDouble(String.valueOf(newValue));
     * ////////////////////////////////////////.println("value is:: " + value);
     * ////////////////////////////////////////.println("newValue is:: " +
     * newValue); ////////////////////////////////////////.println("finalValue
     * is:: " + finalValue); }else { finalValue = "0"; }
     *
     * compareTableResult.append("<td>"); compareTableResult.append(finalValue);
     * compareTableResult.append("</td>"); }else if (((String)
     * compareModelNamesArray.get(k)).equalsIgnoreCase("Last Three Months
     * Average")) { double newValue = 0; if(monthsArray.size() >= 3) { for(int
     * q=monthsArray.size()-1;q>=monthsArray.size()-3;q--) { value =
     * pbro.getFieldValueString(i, (String) monthsArray.get(q)); doubleValue =
     * new Double(value); newValue += doubleValue; } newValue = newValue / 3;
     * finalValue = sc.getScenarioValDouble(String.valueOf(newValue));
     *
     * }else { finalValue = "0"; }
     *
     * compareTableResult.append("<td>"); compareTableResult.append(finalValue);
     * compareTableResult.append("</td>"); }else if (((String)
     * compareModelNamesArray.get(k)).equalsIgnoreCase("Last Six Months
     * Average")) { double newValue = 0; if(monthsArray.size() >= 6) { for(int
     * q=monthsArray.size()-1;q>=monthsArray.size()-6;q--) { value =
     * pbro.getFieldValueString(i, (String) monthsArray.get(q)); doubleValue =
     * new Double(value); newValue += doubleValue; } newValue = newValue / 6;
     * finalValue = sc.getScenarioValDouble(String.valueOf(newValue));
     *
     * }else { finalValue = "0"; }
     *
     * compareTableResult.append("<td>"); compareTableResult.append(finalValue);
     * compareTableResult.append("</td>"); }else if (((String)
     * compareModelNamesArray.get(k)).equalsIgnoreCase("Last Nine Months
     * Average")) { double newValue = 0; if(monthsArray.size() >= 9) { for(int
     * q=monthsArray.size()-1;q>=monthsArray.size()-9;q--) { value =
     * pbro.getFieldValueString(i, (String) monthsArray.get(q)); doubleValue =
     * new Double(value); newValue += doubleValue; } newValue = newValue / 9;
     * finalValue = sc.getScenarioValDouble(String.valueOf(newValue));
     * ////////////////////////////////////////.println("value is:: " + value);
     * ////////////////////////////////////////.println("newValue is:: " +
     * newValue); ////////////////////////////////////////.println("finalValue
     * is:: " + finalValue); }else { finalValue = "0"; }
     *
     * compareTableResult.append("<td>"); compareTableResult.append(finalValue);
     * compareTableResult.append("</td>"); }else if (((String)
     * compareModelNamesArray.get(k)).equalsIgnoreCase("Last Twelve Months
     * Average")) { double newValue = 0; if(monthsArray.size() >= 12) { for(int
     * q=monthsArray.size()-1;q>=monthsArray.size()-12;q--) { value =
     * pbro.getFieldValueString(i, (String) monthsArray.get(q)); doubleValue =
     * new Double(value); newValue += doubleValue; } newValue = newValue / 12;
     * finalValue = sc.getScenarioValDouble(String.valueOf(newValue));
     *
     * }else { finalValue = "0"; }
     *
     * compareTableResult.append("<td>"); compareTableResult.append(finalValue);
     * compareTableResult.append("</td>"); }else { ArrayList customModelDetails
     * = getCustomModelBasis(scenarioId,(String)compareModelNamesArray.get(k));
     * String customModelId = (String)customModelDetails.get(0); String
     * customModelBasis = (String)customModelDetails.get(1);
     * customModelDetailsMap = getCustomModelDetails(customModelId); ArrayList
     * customModelMonthsArray =
     * (ArrayList)customModelDetailsMap.get("customModelMonths"); ArrayList
     * customModelWeights =
     * (ArrayList)customModelDetailsMap.get("customModelWeights");
     *
     * if(customModelBasis.equalsIgnoreCase("Maximum")) { maximumValue =
     * pbro.getFieldValueString(i, (String) customModelMonthsArray.get(0));
     * maximumDoubleValue = new Double(maximumValue); for(int
     * m=0;m<customModelMonthsArray.size();m++) { value =
     * pbro.getFieldValueString(i, (String) customModelMonthsArray.get(m));
     * doubleValue = new Double(value); if(doubleValue > maximumDoubleValue) {
     * maximumDoubleValue = doubleValue; } } finalValue =
     * sc.getScenarioValDouble(String.valueOf(maximumDoubleValue));
     * compareTableResult.append("<td>"); compareTableResult.append(finalValue);
     * compareTableResult.append("</td>"); }else
     * if(customModelBasis.equalsIgnoreCase("Minimum")) { minimumValue =
     * pbro.getFieldValueString(i, (String) customModelMonthsArray.get(0));
     * minimumDoubleValue = new Double(minimumValue); for(int
     * m=0;m<customModelMonthsArray.size();m++) { value =
     * pbro.getFieldValueString(i, (String) customModelMonthsArray.get(m));
     * doubleValue = new Double(value); if(doubleValue < minimumDoubleValue) {
     * minimumDoubleValue = doubleValue; } } finalValue =
     * sc.getScenarioValDouble(String.valueOf(minimumDoubleValue));
     * compareTableResult.append("<td>"); compareTableResult.append(finalValue);
     * compareTableResult.append("</td>"); }else
     * if(customModelBasis.equalsIgnoreCase("Sum")) { double sumDoubleValue = 0;
     * for(int m=0;m<customModelMonthsArray.size();m++) { value =
     * pbro.getFieldValueString(i, (String) customModelMonthsArray.get(m));
     * doubleValue = new Double(value); sumDoubleValue += doubleValue; }
     * finalValue = sc.getScenarioValDouble(String.valueOf(sumDoubleValue));
     * compareTableResult.append("<td>"); compareTableResult.append(finalValue);
     * compareTableResult.append("</td>"); }else
     * if(customModelBasis.equalsIgnoreCase("Simple Average")) { double
     * simpleAvgDoubleValue = 0; for(int
     * m=0;m<customModelMonthsArray.size();m++) { value =
     * pbro.getFieldValueString(i, (String) customModelMonthsArray.get(m));
     * doubleValue = new Double(value); simpleAvgDoubleValue += doubleValue; }
     * simpleAvgDoubleValue = simpleAvgDoubleValue /
     * customModelMonthsArray.size(); finalValue =
     * sc.getScenarioValDouble(String.valueOf(simpleAvgDoubleValue));
     * compareTableResult.append("<td>"); compareTableResult.append(finalValue);
     * compareTableResult.append("</td>"); }else
     * if(customModelBasis.equalsIgnoreCase("Weighted Average")) { double
     * weightedAvgDoubleValue = 0; double weight; for(int
     * m=0;m<customModelMonthsArray.size();m++) { value =
     * pbro.getFieldValueString(i, (String) customModelMonthsArray.get(m));
     * doubleValue = new Double(value);
     * if(!((String)customModelWeights.get(m)).equalsIgnoreCase("")) { weight =
     * Double.parseDouble((String)customModelWeights.get(m));
     * weightedAvgDoubleValue = weightedAvgDoubleValue + ((weight / 100) *
     * doubleValue); } }
     *
     * finalValue =
     * sc.getScenarioValDouble(String.valueOf(weightedAvgDoubleValue));
     * compareTableResult.append("<td>"); compareTableResult.append(finalValue);
     * compareTableResult.append("</td>"); }
     *
     * }
     * }
     * compareTableResult.append("</tr>"); }
     *
     * compareTableResult.append("</tbody>");
     * compareTableResult.append("</table>");
     *
     * compareTableResult.append("<table>"); compareTableResult.append("<tr>");
     * compareTableResult.append("<td>");
     * compareTableResult.append("T=Trillion");
     * compareTableResult.append("</td>");
     *
     * compareTableResult.append("<td>"); compareTableResult.append("");
     * compareTableResult.append("</td>");
     *
     * compareTableResult.append("<td>");
     * compareTableResult.append("B=Billion");
     * compareTableResult.append("</td>");
     *
     * compareTableResult.append("<td>"); compareTableResult.append("");
     * compareTableResult.append("</td>");
     *
     * compareTableResult.append("<td>");
     * compareTableResult.append("M=Million");
     * compareTableResult.append("</td>");
     *
     * compareTableResult.append("<td>"); compareTableResult.append("");
     * compareTableResult.append("</td>");
     *
     * compareTableResult.append("<td>");
     * compareTableResult.append("K=Thousand");
     * compareTableResult.append("</td>"); compareTableResult.append("</tr>");
     * compareTableResult.append("</table>");
     *
     *
     * all.put("compareTableResult", compareTableResult.toString()); return all;
     * }
     *
     */
    public JFreeChart chart;
    public String mapname;
    public CategoryItemRenderer renderer;
    // private Writer out = null;
    private String ctxPath = null;
    public String chartDisplay = "";
    private Color[] colors = {
        new Color(100, 178, 255),
        new Color(181, 249, 80),
        new Color(51, 230, 250),
        new Color(239, 230, 98),
        new Color(225, 117, 95),
        new Color(170, 199, 73),
        new Color(134, 25, 86),
        new Color(201, 160, 220),
        new Color(0, 153, 204),
        new Color(142, 84, 84),
        new Color(255, 153, 0)
    };

    public String getGraph(Vector arl, Vector viewBy, HttpSession hs, HttpServletResponse response, Writer out) throws IOException {

        final DefaultCategoryDataset dataset1 = buildDataSet(arl, viewBy);
        CategoryPlot plot = null;
        chart = ChartFactory.createBarChart(
                "", // chart title
                "", // domain axis label
                "Y-axis", // range axis label
                dataset1, // data
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips?
                false // URL generator?  Not required...
                );
        renderer = new LineAndShapeRenderer();
        plot = chart.getCategoryPlot();
        LegendTitle legend = chart.getLegend();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);


        legend.setPosition(RectangleEdge.BOTTOM);
        plot.setBackgroundPaint(Color.white);//use setter and getter to change bk color later
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        plot.setBackgroundAlpha(0f);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.mapDatasetToRangeAxis(1, 1);

        for (int i = 0; i < colors.length; i++) {
            renderer.setSeriesPaint(i, colors[i]);//r
        }


        StandardCategoryToolTipGenerator Std = new ProGenStandardCategoryToolTipGenerator();
        StandardCategoryURLGenerator url1 = null;


        renderer.setToolTipGenerator(Std);


        renderer.setNegativeItemLabelPosition(new ItemLabelPosition());
        plot.setRenderer(renderer);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        chart.setBorderVisible(false);

        String path = getchartPath(hs, response, out);
        setChartDisplay(this.mapname, path);
        hs.setAttribute("graphdisplay", chartDisplay);

        return path;

    }

    public String getchartPath(HttpSession session, HttpServletResponse response, Writer out) throws IOException {

        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        String fileName = ServletUtilities.saveChartAsPNG(chart, 1350, 650, info, session);
        this.mapname = fileName;
        //tempImgFile=File.

        PrintWriter pw = new PrintWriter(out);
        //  Write the image map to the PrintWriter

        ChartUtilities.writeImageMap(pw, fileName, info, true);
        pw.flush();
        //String path = response.encodeURL("servlet/DisplayChart?filename=" + fileName);
        String path = "servlet/DisplayChart?filename=" + fileName;
        return path;

    }

    public HashMap getBudgetScenarioTable(ArrayList colViewByVal, PbReturnObject dataObj, String scenarioId, ArrayList scenarioViewByMonths, String viewBy, HashMap crossTabNonViewByMap, LinkedHashMap scenarioViewByMain, LinkedHashMap timeDetailsMap, HashMap scenarioParameters, String drillUrl, String expGrowth, String modlName, String newrowId, String expRowGrowth, ArrayList expGrowthArray, ArrayList GrowthapplyList, HashMap expGrowthHm, String anlyName, HashMap expFinalGrowthHm, HashMap existexpGrowthHm) throws Exception {
        String dimensionId = "";
        String viewByEle = viewBy;
        HashMap normalHM = new HashMap();
        double secondTrend = 0.00;
        double thirdTrend = 0.00;
        double fourthTrend = 0.00;
        double sixthTrend = 0.00;

        double secondTrend1 = 0.00;
        double thirdTrend2 = 0.00;
        double fourthTrend3 = 0.00;
        double sixthTrend4 = 0.00;

        double secondTrendg = 0.00;
        double thirdTrendg = 0.00;
        double sixthTrendg = 0.00;
        double ninthTrendg = 0.00;
        double twelveTrendg = 0.00;
        double lastMonthg = 0.00;
        double selectmonthg = 0.00;
        double grandTotal = 0.00;
        double msrGrandTotal = 0.00;
        double projGrandTotal = 0.00;
        double GrTot = 0.00;
        double fieldGrandTotal = 0.00;
        double percentGrandTotal = 0.00;

        double y1growth = 0.00;
        double y2growth = 0.00;
        double cumulgrowth = 0.00;
        double growthPercent = 0.00;
        double sumdouble1 = 0.00;
        double sumdoubledouB2 = 0.00;
        double sumdouble3 = 0.00;
        double sumdoubledouB4 = 0.00;
        double sumExpGrowth = 0.00;
        double sumExpFinalGrowth = 0.00;
        double sumforCastGrowth = 0.00;
        double sumforCastFinalGrowth = 0.00;

        LinkedHashMap GraphMap = new LinkedHashMap();
        Vector GraphVector = new Vector();
        Vector ViewbyVector = new Vector();
        ArrayList expGrowthList = new ArrayList();
        BigDecimal bd = null;
        ArrayList douArrayList = new ArrayList();
        ArrayList doubArrayList = new ArrayList();

//        normalHM = collect.getNormalHm();
        //    ////("normalHM-in getScenarioTable--" + normalHM);
        if (scenarioParameters.containsKey(viewBy)) {
            ArrayList det = new ArrayList();
            det = (ArrayList) scenarioParameters.get(viewBy);
            dimensionId = det.get(3).toString();
        }

        HashMap all = new HashMap();
        ArrayList histMonths = new ArrayList();
        histMonths = getHistMonths(scenarioId);
        //    ////("histMonths--" + histMonths);
        String nextMonth = "";
        nextMonth = histMonths.get(0).toString();
        ArrayList param = new ArrayList();
        String viewByName = "";
        String mainVal[] = (String[]) scenarioViewByMain.keySet().toArray(new String[0]);
        String modelId = "";
        String modelName = "";
        for (int t = 0; t < mainVal.length; t++) {
            ArrayList al = (ArrayList) scenarioViewByMain.get(mainVal[t]);
            if (al.get(2).toString().equalsIgnoreCase("Model")) {
                modelName = al.get(3).toString();
                modelId = al.get(4).toString();
            }
        }

        ////////////////////////////////////////.println(modelId + " modelName-= " + modelName);
        String dispName = "select * from prg_user_all_info_details where element_id=" + viewBy;
        PbReturnObject vNameObj = pbDb.execSelectSQL(dispName);
        viewBy = vNameObj.getFieldValueString(0, "DISP_NAME");
        ArrayList colNew = new ArrayList();
        HashMap colRevMap = new HashMap();
        for (int y = 0; y < colViewByVal.size(); y++) {
            String qName = colViewByVal.get(y).toString();
            String val = "";
            if (crossTabNonViewByMap.containsKey(qName)) {
                colNew.add(crossTabNonViewByMap.get(qName).toString());
            }
            colRevMap.put(crossTabNonViewByMap.get(qName).toString(), qName);
        }

        //String result = "";
        PbReturnObject pbro = new PbReturnObject();
        ArrayList newMonths = new ArrayList();
        newMonths = getProjectedMonths(scenarioId, timeDetailsMap);


        String colNames[] = new String[newMonths.size() + 1];
        colNames[0] = viewBy.toUpperCase().trim();
        for (int y = 1; y < colNames.length; y++) {
            colNames[y] = (String) newMonths.get(y - 1);

        }
        pbro.setColumnNames(colNames); //uday


        for (int m = 0; m < dataObj.getRowCount(); m++) {
            pbro.setFieldValueString(viewBy.toUpperCase().trim(), dataObj.getFieldValueString(m, 0));
            param.add(dataObj.getFieldValueString(m, 0));
            for (int y = 0; y < newMonths.size(); y++) {
                String scMon = newMonths.get(y).toString();
                //    ////("scMon---" + scMon);
                String dataValue = "0";
                if (colNew.contains(scMon)) {
                    if (colRevMap.containsKey(scMon)) {
                        String qName = "";
                        qName = (String) colRevMap.get(scMon);
                        dataValue = dataObj.getFieldValueString(m, qName);
                        //      ////("dataValue--" + dataValue);
                    }
                }
                pbro.setFieldValueString(scMon, dataValue);
            }
            pbro.addRow();
        }
        ////////////////////////////////////////.println(" before ---------");



        pbro.writeString();

        ArrayList datesVa = new ArrayList();
        String cols[] = pbro.getColumnNames();
        String fColV = "";
        int t = 0;
        for (int p = 0; p < cols.length; p++) {
            if (p == 0) {
                fColV = cols[p];
            }

        }
        ArrayList al2 = new ArrayList();
        al2 = newMonths;
        datesVa = al2;
        //  ////(" datesVa " + datesVa);
        viewByName = viewBy;

        ArrayList alRev = new ArrayList();
        for (int y = al2.size() - 1; y >= 0; y--) {
            alRev.add(al2.get(y));
        }


        LinkedHashMap totIssue = new LinkedHashMap();

        //Calculation For the custom Model
        PbScenarioParamVals customP = new PbScenarioParamVals();

        Session customSession = new Session();
        ScenarioDb scDb = new ScenarioDb();
        customP.setScenarioId(scenarioId);
        customSession.setObject(customP);
        PbReturnObject modelpbro = scDb.getCustModelForScenario(customSession);
        PbReturnObject custompbro = scDb.getCustModelDet(customSession);
        String custModName[] = new String[modelpbro.getRowCount()];
        String custModBasis[] = new String[modelpbro.getRowCount()];
        String custModWeightType[] = new String[modelpbro.getRowCount()];
        String custModBasis2 = "";
        String custModName2 = "";//modelId;
        if (modelId.equalsIgnoreCase("") || (modelId.equalsIgnoreCase("--Select--"))) {
            custModName2 = modelId;
        }
        String custModWeightType2 = "";
        HashMap custMapMod = new HashMap();
        ArrayList cMonths = new ArrayList();

        // to check the weights
        HashMap custWeights = new HashMap();
        if (modelpbro.getRowCount() > 0) {
            for (int y = 0; y < modelpbro.getRowCount(); y++) {
                custModName[y] = modelpbro.getFieldValueString(y, "CUSTOM_MODEL_NAME");
                custModBasis[y] = modelpbro.getFieldValueString(y, "MODEL_BASIS");
                // custModWeightType[y] = modelpbro.getFieldValueString(y, "WEIGHT_TYPE");
                if (modelId.equalsIgnoreCase(modelpbro.getFieldValueString(y, "CUSTOM_MODEL_NAME"))) {
                    custModName2 = modelpbro.getFieldValueString(y, "CUSTOM_MODEL_NAME");
                    // custModWeightType2 = modelpbro.getFieldValueString(y,"WEIGHT_TYPE");
                    custModBasis2 = modelpbro.getFieldValueString(y, "MODEL_BASIS");
                }
            }
            ArrayList custMonths = new ArrayList();
            // if(!custModName2.equalsIgnoreCase(""))
            // modelId = custModName2;

            for (int x = 0; x < custompbro.getRowCount(); x++) {


                String mName = custompbro.getFieldValueString(x, "CUSTOM_MODEL_NAME");
                String monName = custompbro.getFieldValueString(x, "CUSTOM_MODEL_MONTHS");
                //monName = monName.substring(0, 3) + monName.substring(4);
                String weight = custompbro.getFieldValueString(x, "CUSTOM_MODEL_WEIGHTS");
                if (modelId.equalsIgnoreCase(mName)) {
                    cMonths.add(monName);
                }

                if (custMapMod.containsKey(mName)) {
                    custMonths = (ArrayList) custMapMod.get(mName);
                    HashMap neMap = new HashMap();
                    neMap.put(monName, weight);
                    custMonths.add(neMap);
                    // custMapMod.put(mName,custMonths);
                } else {
                    HashMap neMap = new HashMap();
                    custMonths = new ArrayList();
                    neMap.put(monName, weight);
                    custMonths.add(neMap);
                    // custMapMod.put(mName,custMonths);

                }
                custMapMod.put(mName, custMonths);
            }
        }
        ////////////////////////////////////////.println(" custMapMod " + custMapMod);

        ArrayList graphArray = new ArrayList();
        for (int m = 0; m < pbro.getRowCount(); m++) {
            LinkedHashMap hm2 = new LinkedHashMap();
            String oldpVal = param.get(m).toString();
            //////////////////////////////////////////.println("oldpVal...- " + oldpVal);
            hm2.put(viewByName, oldpVal);
            for (int k = 0; k < datesVa.size(); k++) {
                String b = datesVa.get(k).toString();
                float newV = Float.parseFloat(pbro.getFieldValueString(m, b.trim().toUpperCase()));
                //////////////////////////////////////////.println(m + " b -- " + b);
                hm2.put(b.trim().toUpperCase(), new Float(newV));
                // graphArray.add(hm2);
            }
        }

        // to stotre the custom calculated values
        HashMap custHm = new HashMap();
        //////////////////////////////////////////.println("graphArray . // " + graphArray);
        String nextMonth2 = nextMonth;//.substring(0, 3) + nextMonth.substring(4);
        Vector allPa = new Vector();
        boolean addFlag = false;
        LinkedHashMap custMapIssue = new LinkedHashMap();
        ////////////////////////////////////////.println(" alRev " + alRev);
        for (int m = 0; m < pbro.getRowCount(); m++) {

            LinkedHashMap hm2 = new LinkedHashMap();
            String oldpVal = param.get(m).toString();

            if (!allPa.contains(oldpVal)) {
                hm2.put(viewByName, oldpVal);
                //graphArray.add(hm2);
                addFlag = true;
            }
            // hm2.put(viewByName, oldpVal);
            // graphArray.add(hm2);

            ////////////////////////////////////////.println("oldpVal - " + oldpVal);
            double rTot = 0.00;
            LinkedHashMap hMapIssue = new LinkedHashMap();


            double custTotal = 0.00; //  Set key5 = (Set)hm.keySet();
            double custTotal2 = 0.00;
            Set keys = (Set) custMapMod.keySet();
            String keysV[] = (String[]) keys.toArray(new String[0]);
            String mName = "";
            //if (keysV.length > 0) {
            mName = modelId;//keysV[0];
            // }
            HashMap values = new HashMap();
            ArrayList oldMon = new ArrayList();
            if (custMapMod.containsKey(mName)) {
                oldMon = (ArrayList) custMapMod.get(mName);
                //////////////////////////////////////////.println("oldMon - "+oldMon);
                for (int l = 0; l < oldMon.size(); l++) {
                    HashMap old = (HashMap) oldMon.get(l);
                    Set keys2 = (Set) old.keySet();
                    String keysV2[] = (String[]) keys2.toArray(new String[0]);
                    for (int o = 0; o < keysV2.length; o++) {
                        String mm = keysV2[o];
                        if (old.containsKey(mm)) {
                            String wt = old.get(mm).toString();
                            values.put(mm, wt);
                        }
                    }
                }
            }
            double lastMon = 0.00;
            double secondLastMon = 0.00;
            double thirdLastMon = 0.00;
            double perChangeFirst = 0.00; //perChangeFirst = (secondLastMon-lastMon)/100;
            double fourthLastMon = 0.00;
            double fifthLastMon = 0.00;
            double sixthLastMon = 0.00;
            double seventhLastMon = 0.00;

            double sumFirstMonth = 0.00;
            double sumTwoMonths = 0.00;
            double sumThreeMonths = 0.00;
            double sumFourMonths = 0.00;
            double sumFiveMonths = 0.00;
            double sumSixMonths = 0.00;

            double sumTot = 0.00;

            // for minimum and maximum
            double minValue = 0.00;
            double maxValue = 0.00;
            boolean minFlag = false;

            for (int k = 0; k < alRev.size(); k++) {

                //////////////////////////////////////////.println("custModWeightType2 ... " + custModWeightType2);
                String b = alRev.get(k).toString();
                ////////////////////////////////////////.println(" pbro.getFieldValueString(m,b.trim().-=-=-=-=- " + pbro.getFieldValueString(m, b.trim().toUpperCase()));
                double newV = Double.parseDouble(pbro.getFieldValueString(m, b.trim().toUpperCase()));
                ////////////////////////////////////////.println(b + " newV " + newV);
                sumTot = sumTot + newV;

                if (k == 1) {
                    sumTwoMonths = sumTot;
                }
                if (k == 2) {
                    sumThreeMonths = sumTot;
                }
                if (k == 3) {
                    sumFourMonths = sumTot;
                }
                if (k == 5) {
                    sumSixMonths = sumTot;
                }


                if (k == 0) {
                    lastMon = newV;
                }
                if (k == 1) {
                    secondLastMon = newV;
                }
                if (k == 2) {
                    thirdLastMon = newV;
                }
                if (k == 3) {
                    fourthLastMon = newV;
                }
                if (k == 4) {
                    fifthLastMon = newV;
                }
                if (k == 5) {
                    sixthLastMon = newV;
                }
                if (k == 6) {
                    seventhLastMon = newV;
                }



                // to calculate the weight. If month is there get its weight and multiply with total of that month and add to custTotal
                if (cMonths.contains(b)) {
                    if (values.containsKey(b)) {
                        if (custModBasis2.equalsIgnoreCase("Weighted Average")) {
                            //if (custModWeightType2.equalsIgnoreCase("Not Equal")) {
                            //////////////////////////////////////////.println(" in Not  equal--");
                            double wt = Double.parseDouble((String) values.get(b));
                            // float nW = wt/100;
                            double newW = newV * wt;// newV*(1+nW);
                            custTotal = custTotal + newW;

                            //////////////////////////////////////////.println(wt + " wt------custTotal.. " + custTotal + " newV== " + newV + " paper " + b + " newW== " + newW + " nW============");
                        } else if (custModBasis2.equalsIgnoreCase("Simple Average")) {
                            ////////////////////////////////////////.println(" in  Simple Average---------");
                            custTotal = custTotal + newV;
                            ////////////////////////////////////////.println(" custTotal ,./ " + custTotal);
                        } else if (custModBasis2.equalsIgnoreCase("Sum")) {
                            //////////////////////////////////////////.println(" in  Sum---------");
                            double sumV = 0.00;
                            ScenarioC sc2 = new ScenarioC();
                            sumV = newV;
                            custTotal2 = custTotal2 + newV;
                            custTotal = custTotal + newV;//sumV;//newV;
                            ////////////////////////////////////////.println("custTotal is:: sum " + custTotal);
                        } else if (custModBasis2.equalsIgnoreCase("Minimum")) {
                            //////////////////////////////////////////.println(" in  Minimum---------");
                            if (minFlag == false) {
                                if (minValue > newV || minFlag == false) {
                                    minValue = newV;
                                }
                                minFlag = true;
                            } else if (minFlag == true) {
                                if (minValue > newV) {
                                    minValue = newV;
                                }
                            }
                        } else if (custModBasis2.equalsIgnoreCase("Maximum")) {
                            //////////////////////////////////////////.println(" in  Maximum---------");
                            if (maxValue < newV) {
                                maxValue = newV;
                            }
                        }
                    }
                }
                //// for minimum and maximum


                hm2.put(b.trim().toUpperCase(), new Double(newV));
                graphArray.add(hm2);

                rTot = rTot + newV;
                if (k == 0) {
                    hMapIssue.put("Sce1", String.valueOf(rTot));
                    //////////////////////////////////////////.println("Sce1 --  .... " + rTot);
                    if (modelId.equalsIgnoreCase("Last Month")) {
                        hm2.put(nextMonth2, String.valueOf(rTot));
                        // graphArray.add(hm2);
                    }
                }
                if (k == 1) {
                    double av = (double) rTot / 2;
                    //////////////////////////////////////////.println("Sce2 -- " + av + " .... " + rTot);
                    hMapIssue.put("Sce2", String.valueOf(av));
//                    if (modelId.equalsIgnoreCase("Last Two Months Average")) {
//                        hm2.put(nextMonth2, String.valueOf(av));
//                        // graphArray.add(hm2);
//                    }
                    if (modelId.equalsIgnoreCase("Last Two Years Average")) {
                        hm2.put(nextMonth2, String.valueOf(av));
                        // graphArray.add(hm2);
                    }
                }
                if (k == 2) {
                    double av = (double) rTot / 3;
                    //////////////////////////////////////////.println("Sce3 -- " + av + " .... " + rTot);
                    hMapIssue.put("Sce3", String.valueOf(av));
//                    if (modelId.equalsIgnoreCase("Last Three Months Average")) {
//                        hm2.put(nextMonth2, String.valueOf(av));
//                        //graphArray.add(hm2);
//                    }
                    if (modelId.equalsIgnoreCase("Last Three Years Average")) {
                        hm2.put(nextMonth2, String.valueOf(av));
                        //graphArray.add(hm2);
                    }
                }
                if (k == 5) {
                    double av = (double) rTot / 6;
                    //////////////////////////////////////////.println("Sce6 -- " + av + " .... " + rTot);
                    hMapIssue.put("Sce6", String.valueOf(av));
//                    if (modelId.equalsIgnoreCase("Last Six Months Average")) {
//                        hm2.put(nextMonth2, String.valueOf(av));
//                        //graphArray.add(hm2);
//                    }
                    if (modelId.equalsIgnoreCase("Last Six Years Average")) {
                        hm2.put(nextMonth2, String.valueOf(av));
                        //graphArray.add(hm2);
                    }
                }
                if (k == 8) {
                    double av = (double) rTot / 9;
                    //////////////////////////////////////////.println("Sce9 -- " + av + " .... " + rTot);
                    hMapIssue.put("Sce9", String.valueOf(av));
//                    if (modelId.equalsIgnoreCase("Last Nine Months Average")) {
//                        hm2.put(nextMonth2, String.valueOf(av));
//                        graphArray.add(hm2);
//                    }
                    if (modelId.equalsIgnoreCase("Last Nine Years Average")) {
                        hm2.put(nextMonth2, String.valueOf(av));
                        graphArray.add(hm2);
                    }
                }
                if (k == 11) {
                    double av = (double) rTot / 12;
                    //////////////////////////////////////////.println("Sce12 -- " + av + " .... " + rTot);
                    hMapIssue.put("Sce12", String.valueOf(av));
//                    if (modelId.equalsIgnoreCase("Last Twelve Months Average")) {
//                        hm2.put(nextMonth2, String.valueOf(av));
//                        // graphArray.add(hm2);
//                    }
                    if (modelId.equalsIgnoreCase("Last Twelve Years Average")) {
                        hm2.put(nextMonth2, String.valueOf(av));
                        // graphArray.add(hm2);
                    }
                }
                if (custModBasis2.equalsIgnoreCase("Weighted Average")) {
                    //////////////////////////////////////////.println(custModBasis2+" in Not  equal");
                    double bb = (double) custTotal / 100;
                    ScenarioC sc = new ScenarioC();
                    String bb2 = sc.getModifiedNumber(bb, "");
                    custMapIssue.put(oldpVal, bb2);
                    if (modelId.equalsIgnoreCase(custModName2)) { //custModName2
                        hMapIssue.put(custModName2, bb2); // hMapIssue.put("Sce9", new Float(av));
                        if (!modelId.equalsIgnoreCase("--Select--")) {
                            hm2.put(nextMonth2, bb2);
                            //graphArray.add(hm2);
                        }
                    }
                } else if (custModBasis2.equalsIgnoreCase("Simple Average")) {
                    //////////////////////////////////////////.println(custModBasis2+" in equal");
                    int tVal = 1;
                    tVal = cMonths.size();
                    if (tVal == 0) {
                        tVal = 1;
                    }
                    double bb = (double) custTotal / tVal;
                    ScenarioC sc = new ScenarioC();
                    String bb2 = sc.getModifiedNumber(bb, "");
                    custMapIssue.put(oldpVal, bb2);
                    if (modelId.equalsIgnoreCase(custModName2)) { //custModName2
                        hMapIssue.put(custModName2, bb2); // hMapIssue.put("Sce9", new Float(av));
                        if (!modelId.equalsIgnoreCase("--Select--")) {
                            hm2.put(nextMonth2, bb2);
                            //graphArray.add(hm2);
                        }
                    }
                } else if (custModBasis2.equalsIgnoreCase("Sum")) {

                    // float bb = (float)custTotal;
                    double bb = (double) custTotal2;
                    ScenarioC sc = new ScenarioC();
                    String bb2 = sc.getModifiedNumber(bb, "");
                    //////////////////////////////////////////.println(custModBasis2+" Sum--./ "+custTotal+" bb2 "+bb2+" bb = "+bb);
                    custMapIssue.put(oldpVal, new Double(bb2));
                    if (modelId.equalsIgnoreCase(custModName2)) { //custModName2
                        hMapIssue.put(custModName2, bb2); // hMapIssue.put("Sce9", new Float(av));
                        if (!modelId.equalsIgnoreCase("--Select--")) {
                            hm2.put(nextMonth2, bb2);
                            //graphArray.add(hm2);
                        }
                    }
                }

            }
            // to put for the maximum and minimum

            if (custModBasis2.equalsIgnoreCase("Maximum")) {
                //////////////////////////////////////////.println(custModBasis2+" Maximum");
                double bb = (double) maxValue;
                ScenarioC sc = new ScenarioC();
                String bb2 = sc.getModifiedNumber(bb, "");
                custMapIssue.put(oldpVal, new Double(bb2));
                if (modelId.equalsIgnoreCase(custModName2)) { //custModName2
                    hMapIssue.put(custModName2, bb2); // hMapIssue.put("Sce9", new Float(av));
                    if (!modelId.equalsIgnoreCase("--Select--")) {
                        hm2.put(nextMonth2, bb2);
                        //graphArray.add(hm2);
                    }
                }
            } else if (custModBasis2.equalsIgnoreCase("Minimum")) {
                //////////////////////////////////////////.println(custModBasis2+" Minimum "+custModName2);
                double bb = (double) minValue;
                ScenarioC sc = new ScenarioC();
                String bb2 = sc.getModifiedNumber(bb, "");
                custMapIssue.put(oldpVal, bb2);
                if (modelId.equalsIgnoreCase(custModName2)) { //custModName2
                    hMapIssue.put(custModName2, bb2); // hMapIssue.put("Sce9", new Float(av));
                    if (!modelId.equalsIgnoreCase("--Select--")) {
                        hm2.put(nextMonth2, bb2);
                        //graphArray.add(hm2);
                    }
                }
            }


            // to find the average of historical data
            double avgSumTwo = 0.00;
            avgSumTwo = sumTwoMonths / 2;

            double avgSumThree = 0.00;
            avgSumThree = sumThreeMonths / 3;

            double avgSumFour = 0.00;
            avgSumFour = sumFourMonths / 4;

            double avgSumSix = 0.00;
            avgSumSix = sumSixMonths / 6;

            // to get the change in percentage
            ScenarioC c = new ScenarioC();
            if (secondLastMon != 0) {
                perChangeFirst = (double) (100 * ((lastMon - secondLastMon) / secondLastMon));//(100*((lastMon-secondLastMon)/lastMon));//(float)((secondLastMon-lastMon)/lastMon);
            }
            double perChangeSecond = 0.00;
            if (thirdLastMon != 0) {
                perChangeSecond = (double) (100 * ((secondLastMon - thirdLastMon) / thirdLastMon));//(100*((secondLastMon-thirdLastMon)/secondLastMon));
            }
            double perChangeThird = 0.00;
            if (fourthLastMon != 0) {
                perChangeThird = (double) (100 * ((thirdLastMon - fourthLastMon) / fourthLastMon));//(100*((thirdLastMon-fourthLastMon)/thirdLastMon));
            }
            double perChangeFourth = 0.00;
            if (fifthLastMon != 0) {
                perChangeFourth = (double) (100 * ((fourthLastMon - fifthLastMon) / fifthLastMon));//(100*((thirdLastMon-fourthLastMon)/thirdLastMon));
            }
            double perChangeFifth = 0.00;
            if (sixthLastMon != 0) {
                perChangeFifth = (double) (100 * ((fifthLastMon - sixthLastMon) / sixthLastMon));//(100*((thirdLastMon-fourthLastMon)/thirdLastMon));
            }
            double perChangeSixth = 0.00;
            if (seventhLastMon != 0) {
                perChangeSixth = (double) (100 * ((sixthLastMon - seventhLastMon) / seventhLastMon));//(100*((thirdLastMon-fourthLastMon)/thirdLastMon));
            }

            // to get the average change in percentage
            double firstAvg = 0.00;
            firstAvg = (perChangeFirst + perChangeSecond) / 2;
            double firstTrend = ((firstAvg) * avgSumTwo) / 100;//(1+perChangeFirst)*lastMon; //thirdLastMon

            double secondAvg = 0.00;
            secondAvg = (perChangeFirst + perChangeSecond) / 2;

            double thirdAvg = 0.00;
            thirdAvg = (perChangeFirst + perChangeSecond + perChangeThird) / 3;

            double fourthAvg = 0.00;
            fourthAvg = (perChangeFirst + perChangeSecond + perChangeThird + perChangeFourth) / 4;

            double sixthAvg = 0.00;
            sixthAvg = (perChangeFirst + perChangeSecond + perChangeThird + perChangeFourth + perChangeFifth + perChangeSixth) / 6;

            double avgsec = (perChangeFirst + perChangeSecond) / 2;

            secondTrend = ((100 + secondAvg) * avgSumTwo) / 100;//(perChangeFirst)*lastMon;//thirdLastMon;

            thirdTrend = ((100 + thirdAvg) * avgSumThree) / 100;
            fourthTrend = ((100 + fourthAvg) * avgSumFour) / 100;
            sixthTrend = ((100 + sixthAvg) * avgSumSix) / 100;

//            if (modelId.equalsIgnoreCase("Last Two Months Average Growth")) {
//                String ss = c.getScenarioValDouble(secondTrend);
//                hMapIssue.put("Last Two Months Average Growth", ss);
//                hm2.put(nextMonth2, ss);
//                //graphArray.add(hm2);
//            }
            if (modelId.equalsIgnoreCase("Last Two Years Average Growth")) {
                String ss = c.getModifiedNumber(secondTrend, "");
                hMapIssue.put("Last Two Years Average Growth", ss);
                hm2.put(nextMonth2, ss);

                //graphArray.add(hm2);
            }

//            if (modelId.equalsIgnoreCase("Last Three Months Average Growth")) {
//                String ss2 = c.getScenarioValDouble(thirdTrend);
//                hMapIssue.put("Last Three Months Average Growth", ss2);
//                hm2.put(nextMonth2, ss2);
//                // graphArray.add(hm2);
//            }
            if (modelId.equalsIgnoreCase("Last Three Years Average Growth")) {
                String ss2 = c.getModifiedNumber(thirdTrend, "");
                hMapIssue.put("Last Three Years Average Growth", ss2);
                hm2.put(nextMonth2, ss2);
                // graphArray.add(hm2);
            }
//            if (modelId.equalsIgnoreCase("Last Four Months Average Growth")) {
//                String ss4 = c.getScenarioValDouble(fourthTrend);
//                hMapIssue.put("Last Four Months Average Growth", ss4);
//                hm2.put(nextMonth2, ss4);
//                // graphArray.add(hm2);
//            }
            if (modelId.equalsIgnoreCase("Last Four Years Average Growth")) {
                String ss4 = c.getModifiedNumber(fourthTrend, "");
                hMapIssue.put("Last Four Years Average Growth", ss4);
                hm2.put(nextMonth2, ss4);
                // graphArray.add(hm2);
            }
//            if (modelId.equalsIgnoreCase("Last Six Months Average Growth")) {
//                String ss6 = c.getScenarioValDouble(sixthTrend);
//                hMapIssue.put("Last Six Months Average Growth", ss6);
//                hm2.put(nextMonth2, ss6);
//                //  graphArray.add(hm2);
//            }
            if (modelId.equalsIgnoreCase("Last Six Years Average Growth")) {
                String ss6 = c.getModifiedNumber(sixthTrend, "");
                hMapIssue.put("Last Six Years Average Growth", ss6);
                hm2.put(nextMonth2, ss6);
                //  graphArray.add(hm2);
            }

            graphArray.add(hm2);
            totIssue.put(oldpVal, hMapIssue);
            //////("Amit here is hm2" + hm2);
        }


        datesVa.add(nextMonth2);
        String result1 = "";
//        //(" totIssue " + totIssue);
//        //(" graphArray " + graphArray);
//        //("datesVa" + datesVa);
        result.append( "<table id=\"tablesorter\" class=\"tablesorter\" width=\"100%\">");
        result.append( "<thead>");
        result.append(result).append("<tr>");
        result.append(result).append("<th style=\"font-weight:bold\">").append(viewByName).append("</th>");

        for (int k = 0; k < datesVa.size(); k++) {
            String b = datesVa.get(k).toString();
            if (k == datesVa.size() - 1) {
//                result = result + "<th style=\"background-color:silver;font-weight:bold\">" + nextMonth + "</th>";
            } else {
                result.append(result).append("<th style=\"font-weight:bold\">").append(b).append("</th>");
            }
        }
        //  ////("getNormalHM().size() in displayscnrparms is : "+getNormalHM().size());
        if (getNormalHM().size() > 0) {
            if (getNormalRetObj().getColumnCount() > 0) {
                //  ////("collect.nonviewbymap is : "+getNonViewByMap());
                //   ////("collect.nonviewbylist is : "+getNonViewByList());

                for (int i = 0; i < getNormalRetObj().getColumnCount() - 1; i++) {
                    //    ////("collect.getNonViewByMap().get(collect.getNonViewByList().get(i))---"+getNonViewByMap().get("A_"+getNonViewByList().get(i)));
                    result.append(result).append("<th style=\"font-weight:bold\">").append(getNonViewByMap().get("A_" + getNonViewByList().get(i))).append( "</th>");
                }
            }
            result.append(result).append("<th style=\"font-weight:bold\">Projected Value</th>");
        }
//        if (modelId.equalsIgnoreCase("Budgeting")) {
        result.append(result).append( "<th style=\"font-weight:bold\">First Yr Growth</th>");
        result.append(result).append( "<th style=\"font-weight:bold\">Second Yr Growth</th>");

        result.append(result).append( "<th style=\"font-weight:bold\">Expected Growth(in %)</th>");
        result.append(result).append("<th style=\"font-weight:bold\">").append(nextMonth).append( "</th>");
        result.append(result).append( "<th style=\"font-weight:bold\">Forecast Final Growth</th>");
        result.append(result).append("<th style=\"font-weight:bold\">Forecast Final Growth(in %)</th>");
//        }
        result.append(result).append( "</tr>");
        result.append( "</thead>");
        result.append( "<tbody>");

        int rowSp = 0;
        int newRowSp = 0;
        Vector vvVal = new Vector();
        String temp = "";
        ////////////////////////////////////////.println(viewBy + " fColV " + fColV + " viewByName " + viewByName);

        String coNames[] = pbro.getColumnNames();
        ////////////////////////////////////////.println("coNames length is:: " + coNames.length);
        ////////////////////////////////////////.println(" pbro " + pbro.getRowCount());

        String childEleId = null;
        childEleId = collect.getChildElementId(viewByEle, scenarioId, scenarioParameters);

        if (childEleId == null) {
            childEleId = "-1";
        }

        if (!newrowId.equalsIgnoreCase(null) && !"".equalsIgnoreCase(newrowId)) {
        } else {
            for (int i = 0; i < pbro.getRowCount(); i++) {
                expGrowthList.add(i, expGrowth);
            }
        }
//        for (int fgt = 1; fgt < pbro.getColumnCount(); fgt++) {
//            for (int frgt = 0; frgt < pbro.getRowCount(); frgt++) {
//                String[] colname = pbro.getColumnNames();
////                        ////("pbro.getFieldValueString(frgt, colname[fgt]) is : " + pbro.getFieldValueString(frgt, colname[fgt]));
//                fieldGrandTotal = fieldGrandTotal + Double.parseDouble(pbro.getFieldValueString(frgt, colname[fgt]));
//            }
//            per
//            fieldGrandTotal = 0;
//        }

        for (int i = 0; i < pbro.getRowCount(); i++) {

            String oldP = pbro.getFieldValueString(i, viewByName.toUpperCase().trim());
            String par2 = pbro.getFieldValueString(i, viewByName.toUpperCase().trim());

            //////////////////////////////////.println(" oldP=-= " + oldP);
            ////("par2 string is : " + par2);

            if ((i % 2) == 0) {
                temp = "even";
            } else {
                temp = "odd";
            }
            result.append(result).append("<tr class=\"").append(temp).append("\">");
            if (!childEleId.equalsIgnoreCase("-1")) {
                result.append(result).append("<td><a href=\"javascript:submitDrill('").append(drillUrl).append(par2.toString()).append("')\">").append(par2).append("</td>");

            } else {
                result.append(result).append("<td>").append(par2).append("</td>");
            }
            ViewbyVector.add(par2);
            ////("ViewbyVector is : " + ViewbyVector);
            for (int k = 0; k < datesVa.size(); k++) {
                String b = datesVa.get(k).toString();
                ////(" b is : " + b);
                ////////////////////////////////////////.println(k + " datesVa " + datesVa.size());
                String vv = "";
                if (k < datesVa.size() - 1) {
                    vv = pbro.getFieldValueString(i, b.toUpperCase().trim());
//                    //("vv is : " + vv);
                }
                if (k == datesVa.size() - 1) {
                    //   ////(" in if--");
                    String nsce1Value = "0";
                    String nsce2Value = "0";
                    String nsce3Value = "0";
                    String nsce6Value = "0";
                    String nsce9Value = "0";
                    String nsce12Value = "0";

                    String par = oldP;
                    //   ////("par - " + par);
                    HashMap vals = (HashMap) totIssue.get(par);
                    //   ////("vals---" + vals);
                    ArrayList newV = new ArrayList();
                    LinkedHashMap hhV = new LinkedHashMap();

                    String sce1V = "0";
                    ScenarioC sc = new ScenarioC();
                    ////////////////////////////////////////.println(" vals " + vals);
                    if (vals.containsKey("Sce1")) {
                        sce1V = ((String) vals.get("Sce1"));
                        nsce1Value = sc.getModifiedNumber(Double.parseDouble(sce1V), "");
                        //   ////("nsce1Value--" + nsce1Value);
                        hhV.put("Sce1", nsce1Value);
                        lastMonthg = Double.parseDouble(sce1V);
                    }


                    //////////////////////////////////////////.println(par + " par- sce1V //..-- " + sce1V);

                    //newV.add(hhV);

                    String sce2Value = "0";
                    if (vals.containsKey("Sce2")) {
                        String sce2V = ((String) vals.get("Sce2"));
                        nsce2Value = sc.getModifiedNumber(Double.parseDouble(sce2V), "");
                        hhV.put("Sce2", nsce2Value);
                        secondTrendg = Double.parseDouble(sce2V);
                        //////////////////////////////////////////.println("nsce2Value .- " + nsce2Value);
                        //newV.add(hhV);
                    }

                    String sce3Value = "0";
                    if (vals.containsKey("Sce3")) {
                        String sce3V = ((String) vals.get("Sce3"));
                        nsce3Value = sc.getModifiedNumber(Double.parseDouble(sce3V), "");
                        hhV.put("Sce3", nsce3Value);
                        thirdTrendg = Double.parseDouble(sce3V);
                        //newV.add(hhV);
                    }

                    String sce6Value = "0";
                    if (vals.containsKey("Sce6")) {
                        String sce6V = ((String) vals.get("Sce6"));
                        nsce6Value = sc.getModifiedNumber(Double.parseDouble(sce6V), "");
                        hhV.put("Sce6", nsce6Value);
                        sixthTrendg = Double.parseDouble(sce6V);
                        //newV.add(hhV);
                    }

                    String sce9Value = "0";
                    if (vals.containsKey("Sce9")) {
                        String sce9V = ((String) vals.get("Sce9"));
                        nsce9Value = sc.getModifiedNumber(Double.parseDouble(sce9V), "");
                        hhV.put("Sce9", nsce9Value);
                        ninthTrendg = Double.parseDouble(sce9V);
                        //newV.add(hhV);
                    }

                    String sce12Value = "0";
                    if (vals.containsKey("Sce12")) {
                        String sce12V = ((String) vals.get("Sce12"));
                        nsce12Value = sce12V;// sc.getScenarioValDouble(sce12V);
                        hhV.put("Sce12", nsce12Value);
                        twelveTrendg = Double.parseDouble(sce12V);

                    }
                    String sceTrend2 = "0";
//                    if (vals.containsKey("Last Two Months Average Growth")) {
//                        String sceT2V = ((String) vals.get("Last Two Months Average Growth"));//.doubleValue();
//                        sceTrend2 = sc.getScenarioValDouble(sceT2V);
//                        hhV.put("Last Two Months Average Growth", sceTrend2);
//
//                    }
                    if (vals.containsKey("Last Two Years Average Growth")) {
                        String sceT2V = ((String) vals.get("Last Two Years Average Growth"));//.doubleValue();
                        sceTrend2 = sc.getModifiedNumber(Double.parseDouble(sceT2V), "");
                        hhV.put("Last Two Years Average Growth", sceTrend2);
                        secondTrend1 = Double.parseDouble(sceT2V);
                    }

                    String sceTrend3 = "0";
//                    if (vals.containsKey("Last Three Months Average Growth")) {
//                        String sceT3V = ((String) vals.get("Last Three Months Average Growth"));
//                        sceTrend3 = sc.getScenarioValDouble(sceT3V);
//                        hhV.put("Last Three Months Average Growth", sceTrend3);
//
//                    }
                    if (vals.containsKey("Last Three Years Average Growth")) {
                        String sceT3V = ((String) vals.get("Last Three Years Average Growth"));
                        sceTrend3 = sc.getModifiedNumber(Double.parseDouble(sceT3V), "");
                        hhV.put("Last Three Years Average Growth", sceTrend3);
                        thirdTrend2 = Double.parseDouble(sceT3V);
                    }
                    String sceTrend4 = "0";
//                    if (vals.containsKey("Last Four Months Average Growth")) {
//                        String sceT4V = ((String) vals.get("Last Four Months Average Growth"));
//                        sceTrend4 = sc.getScenarioValDouble(sceT4V);
//                        hhV.put("Last Four Months Average Growth", sceTrend4);
//
//                    }
                    if (vals.containsKey("Last Four Years Average Growth")) {
                        String sceT4V = ((String) vals.get("Last Four Years Average Growth"));
                        sceTrend4 = sc.getModifiedNumber(Double.parseDouble(sceT4V), "");
                        hhV.put("Last Four Years Average Growth", sceTrend4);
                        fourthTrend3 = Double.parseDouble(sceT4V);

                    }

                    String sceTrend6 = "0";
//                    if (vals.containsKey("Last Six Months Average Growth")) {
//                        String sceT6V = ((String) vals.get("Last Six Months Average Growth"));
//                        sceTrend6 = sc.getScenarioValDouble(sceT6V);
//                        hhV.put("Last Six Months Average Growth", sceTrend6);
//
//                    }
                    if (vals.containsKey("Last Six Years Average Growth")) {
                        String sceT6V = ((String) vals.get("Last Six Years Average Growth"));
                        sceTrend6 = sc.getModifiedNumber(Double.parseDouble(sceT6V), "");
                        hhV.put("Last Six Years Average Growth", sceTrend6);
                        sixthTrend4 = Double.parseDouble(sceT6V);

                    }

                    //  ////("sceTrend4" + sceTrend4);
                    //  ////("hhV" + hhV);

                    String scustValue = "0";
                    /*
                     * if (vals.containsKey("Custom Model")) { float custV =
                     * ((Float) vals.get("Custom Model")).floatValue();
                     * scustValue = sc.getScenarioVal(custV); hhV.put("Custom
                     * Model", new Float(scustValue));
                     *
                     * }
                     */
                    if (vals.containsKey(modelId)) {
                        String custV = ((String) vals.get(modelId));
                        if (custModBasis2.equalsIgnoreCase("Sum")) {
                            scustValue = sc.getModifiedNumber(Double.parseDouble(custV), "");//custV;

                        } else {
                            scustValue = sc.getModifiedNumber(Double.parseDouble(custV), "");
                        }
                        selectmonthg = Double.parseDouble(custV);
                        //////////////////////////////////////////.println(custV+" custV "+b + " b------------ in if nsce2Value " + scustValue);
                        //   hhV.put("Custom Model", new Float(scustValue));

                    }
                    // }
                    //////////////////////////////////////////.println(modelId+" modelId - "+b + " b in if nsce2Value " + nsce2Value+ " custModName2" +custModName2);
                    //////////////////////////////////////////.println("CustomS -- "+CustomS);
                    double multiplyValue = 0;

                    if (modelId.equalsIgnoreCase("--Select--") || modelId.equalsIgnoreCase("")) {
                       result.append(result + "<td style=\"background-color:silver\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>");
                    }
                    if (modelId.equalsIgnoreCase("Last Month")) {
//                        result = result + "<td style=\"background-color:silver\">" + nsce1Value + "</td>";
                        grandTotal = grandTotal + lastMonthg;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = lastMonthg * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
//                        GraphMap.put(nextMonth, String.valueOf(lastMonthg));

                    }
                    if (modelId.equalsIgnoreCase("Last Two Years Average")) {
//                        result = result + "<td style=\"background-color:silver\">" + nsce2Value + "</td>";
                        grandTotal = grandTotal + secondTrendg;
                        if (getNormalHM().size() > 0) {
                            //   ////("getNormalHM() is : "+getNormalHM());
                            //   ////("pbro.getFieldValueString(i, 0) is : "+pbro.getFieldValueString(i, 0));
                            multiplyValue = secondTrendg * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
//                        GraphMap.put(nextMonth, String.valueOf(secondTrendg));
                    }
                    if (modelId.equalsIgnoreCase("Last Three Years Average")) {
//                        result = result + "<td style=\"background-color:silver\">" + nsce3Value + "</td>";
                        grandTotal = grandTotal + thirdTrendg;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = thirdTrendg * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
//                        GraphMap.put(nextMonth, String.valueOf(thirdTrendg));
                    }
                    if (modelId.equalsIgnoreCase("Last Six Years Average")) {
//                        result = result + "<td style=\"background-color:silver\">" + nsce6Value + "</td>";
                        grandTotal = grandTotal + sixthTrendg;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = sixthTrendg * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
//                        GraphMap.put(nextMonth, String.valueOf(sixthTrendg));
                    }
                    if (modelId.equalsIgnoreCase("Last Nine Years Average")) {
                      result.append(result + "<td >" + nsce9Value + "</td>");
                        grandTotal = grandTotal + ninthTrendg;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = ninthTrendg * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
//                        GraphMap.put(nextMonth, String.valueOf(ninthTrendg));
                    }
                    if (modelId.equalsIgnoreCase("Last Twelve Years Average")) {
                        result.append(result).append("<td >").append(nsce12Value).append("</td>");
                        grandTotal = grandTotal + twelveTrendg;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = twelveTrendg * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
//                        GraphMap.put(nextMonth, String.valueOf(twelveTrendg));
                    }
                    if (modelId.equalsIgnoreCase("Last Two Years Average Growth")) {
                        result.append(result).append("<td >").append(sceTrend2).append( "</td>");
                        grandTotal = grandTotal + secondTrend1;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = secondTrend1 * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
//                        GraphMap.put(nextMonth, String.valueOf(secondTrend1));
                    }
                    if (modelId.equalsIgnoreCase("Last Three Years Average Growth")) {
                        result.append(result).append("<td >").append(sceTrend3).append("</td>");
                        grandTotal = grandTotal + thirdTrend2;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = thirdTrend2 * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
//                        GraphMap.put(nextMonth, String.valueOf(thirdTrend2));
                    }
                    if (modelId.equalsIgnoreCase("Last Four Years Average Growth")) {
                        result.append(result).append("<td >").append(sceTrend4).append("</td>");
                        grandTotal = grandTotal + fourthTrend3;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = fourthTrend3 * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
//                        GraphMap.put(nextMonth, String.valueOf(fourthTrend3));
                    }
                    if (modelId.equalsIgnoreCase("Last Six Years Average Growth")) {
                        result.append(result).append("<td >").append(sceTrend6).append("</td>");
                        grandTotal = grandTotal + sixthTrend4;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = sixthTrend4 * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
//                        GraphMap.put(nextMonth, String.valueOf(sixthTrend4));
                    }
//                    if (modelId.equalsIgnoreCase("Budgeting")) {
//                         y1growth=
                    String model = "17";
                    Double dou = y1growth;
                    // //("y2growth;\t" + y2growth);
                    Double doub = y2growth;
                    bd = new BigDecimal(dou);
                    bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                    dou = bd.doubleValue();
                    bd = new BigDecimal(doub);
                    bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                    doub = bd.doubleValue();
                    douArrayList.add(dou);
                    doubArrayList.add(doub);
                    result.append(result).append("<td >").append(dou).append( "</td>");
                    result.append(result).append("<td>").append(doub).append( "</td>");


                    if (expGrowthHm.size() > 0) {
//                        ////.println("in if expgrowthhm");
                        if (GrowthapplyList.contains("applytoAll")) {
                            result1 = "<td><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowthHm.get(par2) + "\"></td>";
//                            GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                            if (expGrowthHm.get(par2) != null) {
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowthHm.get(par2).toString());
                            }
                            GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                        }
                        if (GrowthapplyList.contains("applytoabove")) {
//                            ////.println("in if of applytoabove");
//                            ////.println("expGrowthHm.get(par2) is : "+expGrowthHm.get(par2));
                            if (y2growth >= 50) {
//                                ////.println("in growth>50");
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowthHm.get(par2) + "\"></td>";
                                GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowthHm.get(par2).toString());
                            } else {
//                                ////.println("in not growth>50");
//                                expGrowth="0";
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"0\"></td>";
                                GraphMap.put("Expected Growth", 0);
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                            }
                        }
                        if (GrowthapplyList.contains("applytonew")) {
                            //("in if of applytonew");
                            if (growthPercent == 1) {
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowthHm.get(par2) + "\"></td>";
                                GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowthHm.get(par2).toString());
                            } else {
                                //("in if else of applytonew");
                                expGrowth = "0";
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowth + "\"></td>";
                                GraphMap.put("Expected Growth", expGrowth);
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                            }

                        }
                        if (GrowthapplyList.contains("applynotonegative")) {
//                            ////.println("in if of applynotonegative");
//                            ////.println("existexpGrowthHm is : "+existexpGrowthHm);
                            if (y1growth < 0 || y2growth < 0) {
//                                expGrowth="0";
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + existexpGrowthHm.get(par2) + "\"></td>";
                                GraphMap.put("Expected Growth", existexpGrowthHm.get(par2));
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(existexpGrowthHm.get(par2).toString());
                            } else {
                                //("in if else of applynotonegative");
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowthHm.get(par2) + "\"></td>";
                                GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowthHm.get(par2).toString());
                            }

                        }
                        if (GrowthapplyList.contains("applynotonegativelast")) {
                            //("in if of applynotonegativelast");
                            if (y2growth < 0) {
                                expGrowth = "0";
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"0\"></td>";
                                GraphMap.put("Expected Growth", 0);
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                            } else {
                                //("in if else of applynotonegativelast");
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowthHm.get(par2) + "\"></td>";
                                GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowthHm.get(par2).toString());
                            }

                        }
                        if (GrowthapplyList.contains("applynotoabove")) {
                            //("in if of applynotoabove");
                            if (y2growth >= 50) {
                                expGrowth = "0";
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"0\"></td>";
                                GraphMap.put("Expected Growth", 0);
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                            } else {
                                //("in if else of applynotoabove");
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowthHm.get(par2) + "\"></td>";
                                GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowthHm.get(par2).toString());
                            }
                        }
                        if (GrowthapplyList.contains("applynotonew")) {
                            //("in if of applynotonew");
                            if (growthPercent == 1) {
                                expGrowth = "0";
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"0\"></td>";
                                GraphMap.put("Expected Growth", 0);
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                            } else {
                                //("in if else of applynotonew");
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowthHm.get(par2) + "\"></td>";
                                GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowthHm.get(par2).toString());
                            }

                        }
                        result.append(result).append(result1);
                    } else {

                        if (GrowthapplyList.contains("applytoAll")) {
                            //("in if of applytoAll");
                            result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowth + "\"></td>";
                            GraphMap.put("Expected Growth", expGrowth);
                            sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                        }
                        if (GrowthapplyList.contains("applytoabove")) {
//                            ////.println("in else if of applytoabove");
                            if (y2growth >= 50) {
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowth + "\"></td>";
                                GraphMap.put("Expected Growth", expGrowth);
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                            } else {
                                //("in if else of applytoabove");
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"0\"></td>";
                                GraphMap.put("Expected Growth", 0);
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                            }
                        }
                        if (GrowthapplyList.contains("applytonew")) {
                            //("in if of applytonew");
                            if (growthPercent == 1) {
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowth + "\"></td>";
                                GraphMap.put("Expected Growth", 0);
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                            } else {
                                //("in if else of applytonew");
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowth + "\"></td>";
                                GraphMap.put("Expected Growth", expGrowth);
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                            }

                        }
                        if (GrowthapplyList.contains("applynotonegative")) {
//                            ////.println("in if of applynotonegative");
//                            ////.println("existexpGrowthHm is : "+existexpGrowthHm);
                            if (y1growth < 0 || y2growth < 0) {
//                                expGrowth="0";
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + existexpGrowthHm.get(par2) + "\"></td>";
                                GraphMap.put("Expected Growth", existexpGrowthHm.get(par2));
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(existexpGrowthHm.get(par2).toString());
                            } else {
                                //("in if else of applynotonegative");
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowthHm.get(par2) + "\"></td>";
                                GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowthHm.get(par2).toString());
                            }

                        }
                        if (GrowthapplyList.contains("applynotonegativelast")) {
                            //("in if of applynotonegativelast");
                            if (y1growth > 0 && y2growth < 0) {
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"0\"></td>";
                                GraphMap.put("Expected Growth", 0);
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                            } else {
                                //("in if else of applynotonegativelast");
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowth + "\"></td>";
                                GraphMap.put("Expected Growth", expGrowth);
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                            }

                        }
                        if (GrowthapplyList.contains("applynotoabove")) {
                            //("in if of applynotoabove");
                            if (y2growth >= 50) {
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"0\"></td>";
                                GraphMap.put("Expected Growth", 0);
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                            } else {
                                //("in if else of applynotoabove");
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowth + "\"></td>";
                                GraphMap.put("Expected Growth", expGrowth);
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                            }
                        }
                        if (GrowthapplyList.contains("applynotonew")) {
                            //("in if of applynotonew");
                            if (growthPercent == 1) {
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"0\"></td>";
                                GraphMap.put("Expected Growth", 0);
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                            } else {
                                //("in if else of applynotonew");
                                result1 = "<td ><input type=\"text\" id=\"expGrowth" + i + "\" name=\"expGrowth" + i + "\" style=\"width:50px\" onchange=\"javascript:changeBudgetExpGrowth(this," + model + "," + expGrowthList + ")\" value=\"" + expGrowth + "\"></td>";
                                GraphMap.put("Expected Growth", expGrowth);
                                sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowth.toString());
                            }

                        }

                        result.append(result).append( result1);
                    }

                    result.append(result).append("<td id=\"finalYear_").append(i).append("\" >").append(sc.getModifiedNumber(cumulgrowth, "")).append( "</td>");
                    sumforCastGrowth = sumforCastGrowth + cumulgrowth;
                    if (expFinalGrowthHm.size() > 0) {
                        result.append(result).append("<td ><input type=\"text\" id=\"finalGrowth_").append(i).append("\" name=\"finalGrowth").append(i).append("\" style=\"width:100px\" value=\"").append(expFinalGrowthHm.get(par2)).append("\" onchange=\"totaSumFinalGrowth(this,'").append(expFinalGrowthHm.get(par2)).append( "')\"></td>");
                        sumforCastFinalGrowth = sumforCastFinalGrowth + Double.parseDouble(expFinalGrowthHm.get(par2).toString());
                    } else {
                        result.append(result).append("<td ><input type=\"text\" id=\"finalGrowth_").append(i).append("\" name=\"finalGrowth").append(i).append("\" style=\"width:100px\" value=\"").append(cumulgrowth).append("\" onchange=\"totaSumFinalGrowth(this,'").append(cumulgrowth).append("')\"></td>");
                        sumforCastFinalGrowth = sumforCastFinalGrowth + cumulgrowth;
                    }
                    if (modelId.equalsIgnoreCase(custModName2) && (!custModName2.equalsIgnoreCase("--Select--"))) {
                        result.append(result).append("<td >").append(scustValue).append("</td>");
                        grandTotal = grandTotal + selectmonthg;
                        if (getNormalHM().size() > 0) {
                            multiplyValue = selectmonthg * Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        }
                        GraphMap.put(nextMonth, String.valueOf(selectmonthg));
                    }


                    if (getNormalHM().size() > 0) {

                        String finalMsrVal = sc.getModifiedNumber(Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", "")), "");
                        String finalValue = sc.getModifiedNumber(multiplyValue, "");
                        msrGrandTotal = msrGrandTotal + Double.parseDouble(getNormalHM().get(pbro.getFieldValueString(i, 0)).toString().replace("[", "").replace("]", ""));
                        projGrandTotal = projGrandTotal + Double.parseDouble(String.valueOf(multiplyValue));

                        result.append(result).append("<td style=\"").append(temp).append("\">").append(finalMsrVal).append( " </td>");
                        result.append(result).append("<td >").append(finalValue).append("</td>");
                        for (int v = 0; v < getNormalRetObj().getColumnCount() - 1; v++) {
                            GraphMap.put(getNonViewByMap().get("A_" + getNonViewByList().get(v)), getNormalHM().get(pbro.getFieldValueString(v, 0)).toString().replace("[", "").replace("]", ""));
                        }
                        GraphMap.put("Projected Value", String.valueOf(multiplyValue));
                    }
                    if (!newrowId.equalsIgnoreCase(null) && !"".equalsIgnoreCase(newrowId)) {


                        if (i == Integer.parseInt(newrowId)) {

                            result.append(result).append("<td ><input type=\"text\" id=\"finalexpGrowth").append(i).append("\" name=\"finalexpGrowth").append(i).append("\" style=\"width:50px;background-color:silver\"  value=\"").append(expGrowthArray.get(i)).append( "\" readonly></td>");
                            sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowthArray.get(i).toString());
                        } else {

                            result.append(result).append("<td ><input type=\"text\" id=\"finalexpGrowth").append(i).append("\" name=\"finalexpGrowth").append(i).append("\" style=\"width:50px;background-color:silver\"  value=\"").append(expGrowthArray.get(i)).append("\" readonly></td>");

                            sumExpGrowth = sumExpGrowth + Double.parseDouble(expGrowthArray.get(i).toString());
                        }
                    } else {
                        if (expGrowthHm.size() > 0) {
//                        ////.println("in if expgrowthhm");
                            if (GrowthapplyList.contains("applytoAll")) {
                                result1 = "<td><input type=\"text\" id=\"finalexpGrowth" + i + "\" name=\"finalexpGrowth" + i + "\" style=\"width:50px;background-color:silver\" value=\"" + expGrowthHm.get(par2) + "\" readonly></td>";
//                            GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                                if (expGrowthHm.get(par2) != null) {
                                    sumExpFinalGrowth = sumExpFinalGrowth + Double.parseDouble(expGrowthHm.get(par2).toString());
                                }
                                GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                            }
                            if (GrowthapplyList.contains("applytoabove")) {
//                            ////.println("in if of applytoabove");
//                            ////.println("expGrowthHm.get(par2) is : "+expGrowthHm.get(par2));
                                if (y2growth >= 50) {
//                                ////.println("in growth>50");
                                    result1 = "<td ><input type=\"text\" id=\"finalexpGrowth" + i + "\" name=\"finalexpGrowth" + i + "\" style=\"width:50px;background-color:silver\"  value=\"" + expGrowthHm.get(par2) + "\" readonly></td>";
                                    GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                                    sumExpFinalGrowth = sumExpFinalGrowth + Double.parseDouble(expGrowthHm.get(par2).toString());
                                } else {
//                                ////.println("in not growth>50");
//                                expGrowth="0";
                                    result1 = "<td ><input type=\"text\" id=\"finalexpGrowth" + i + "\" name=\"finalexpGrowth" + i + "\" style=\"width:50px;background-color:silver\"  value=\"0\" readonly></td>";
                                    GraphMap.put("Expected Growth", 0);
                                    sumExpFinalGrowth = sumExpFinalGrowth + Double.parseDouble(expGrowth.toString());
                                }
                            }
                            if (GrowthapplyList.contains("applytonew")) {
                                //("in if of applytonew");
                                if (growthPercent == 1) {
                                    result1 = "<td ><input type=\"text\" id=\"finalexpGrowth" + i + "\" name=\"finalexpGrowth" + i + "\" style=\"width:50px;background-color:silver\"  value=\"" + expGrowthHm.get(par2) + "\" readonly></td>";
                                    GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                                    sumExpFinalGrowth = sumExpFinalGrowth + Double.parseDouble(expGrowthHm.get(par2).toString());
                                } else {
                                    //("in if else of applytonew");
                                    expGrowth = "0";
                                    result1 = "<td ><input type=\"text\" id=\"finalexpGrowth" + i + "\" name=\"finalexpGrowth" + i + "\" style=\"width:50px;background-color:silver\"  value=\"" + expGrowth + "\" readonly></td>";
                                    GraphMap.put("Expected Growth", expGrowth);
                                    sumExpFinalGrowth = sumExpFinalGrowth + Double.parseDouble(expGrowth.toString());
                                }

                            }
                            if (GrowthapplyList.contains("applynotonegative")) {
//                            ////.println("in if of applynotonegative");
//                            ////.println("existexpGrowthHm is : "+existexpGrowthHm);
                                if (y1growth < 0 || y2growth < 0) {
//                                expGrowth="0";
                                    result1 = "<td ><input type=\"text\" id=\"finalexpGrowth" + i + "\" name=\"finalexpGrowth" + i + "\" style=\"width:50px;background-color:silver\" value=\"" + existexpGrowthHm.get(par2) + "\"></td>";
                                    GraphMap.put("Expected Growth", existexpGrowthHm.get(par2));
                                    sumExpFinalGrowth = sumExpFinalGrowth + Double.parseDouble(existexpGrowthHm.get(par2).toString());
                                } else {
                                    //("in if else of applynotonegative");
                                    result1 = "<td ><input type=\"text\" id=\"finalexpGrowth" + i + "\" name=\"finalexpGrowth" + i + "\" style=\"width:50px;background-color:silver\" value=\"" + expGrowthHm.get(par2) + "\"></td>";
                                    GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                                    sumExpFinalGrowth = sumExpFinalGrowth + Double.parseDouble(expGrowthHm.get(par2).toString());
                                }

                            }
                            if (GrowthapplyList.contains("applynotonegativelast")) {
                                //("in if of applynotonegativelast");
                                if (y2growth < 0) {
                                    expGrowth = "0";
                                    result1 = "<td ><input type=\"text\" id=\"finalexpGrowth" + i + "\" name=\"finalexpGrowth" + i + "\" style=\"width:50px;background-color:silver\" value=\"0\" readonly></td>";
                                    GraphMap.put("Expected Growth", 0);
                                    sumExpFinalGrowth = sumExpFinalGrowth + Double.parseDouble(expGrowth.toString());
                                } else {
                                    //("in if else of applynotonegativelast");
                                    result1 = "<td ><input type=\"text\" id=\"finalexpGrowth" + i + "\" name=\"finalexpGrowth" + i + "\" style=\"width:50px;background-color:silver\"  value=\"" + expGrowthHm.get(par2) + "\" readonly></td>";
                                    GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                                    sumExpFinalGrowth = sumExpFinalGrowth + Double.parseDouble(expGrowthHm.get(par2).toString());
                                }

                            }
                            if (GrowthapplyList.contains("applynotoabove")) {
                                //("in if of applynotoabove");
                                if (y2growth >= 50) {
                                    expGrowth = "0";
                                    result1 = "<td ><input type=\"text\" id=\"finalexpGrowth" + i + "\" name=\"finalexpGrowth" + i + "\" style=\"width:50px;background-color:silver\"  value=\"0\" readonly></td>";
                                    GraphMap.put("Expected Growth", 0);
                                    sumExpFinalGrowth = sumExpFinalGrowth + Double.parseDouble(expGrowth.toString());
                                } else {
                                    //("in if else of applynotoabove");
                                    result1 = "<td ><input type=\"text\" id=\"finalexpGrowth" + i + "\" name=\"finalexpGrowth" + i + "\" style=\"width:50px;background-color:silver\" value=\"" + expGrowthHm.get(par2) + "\" readonly></td>";
                                    GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                                    sumExpFinalGrowth = sumExpFinalGrowth + Double.parseDouble(expGrowthHm.get(par2).toString());
                                }
                            }
                            if (GrowthapplyList.contains("applynotonew")) {
                                //("in if of applynotonew");
                                if (growthPercent == 1) {
                                    expGrowth = "0";
                                    result1 = "<td ><input type=\"text\" id=\"finalexpGrowth" + i + "\" name=\"finalexpGrowth" + i + "\" style=\"width:50px;background-color:silver\"  value=\"0\" readonly></td>";
                                    GraphMap.put("Expected Growth", 0);
                                    sumExpFinalGrowth = sumExpFinalGrowth + Double.parseDouble(expGrowth.toString());
                                } else {
                                    //("in if else of applynotonew");
                                    result1 = "<td ><input type=\"text\" id=\"finalexpGrowth" + i + "\" name=\"finalexpGrowth" + i + "\" style=\"width:50px;background-color:silver\"  value=\"" + expGrowthHm.get(par2) + "\" readonly></td>";
                                    GraphMap.put("Expected Growth", expGrowthHm.get(par2));
                                    sumExpFinalGrowth = sumExpFinalGrowth + Double.parseDouble(expGrowthHm.get(par2).toString());
                                }

                            }
                            result.append(result).append( result1);
                        } else {
//                            ////.println("in else of expgrowthhm finalexpgrowth");
                            result.append(result).append("<td ><input type=\"text\" id=\"finalexpGrowth").append(i).append("\" name=\"finalexpGrowth").append(i).append("\" style=\"width:50px;background-color:silver\"   value=\"").append(expGrowth).append("\" readonly ></td>");
                            sumExpFinalGrowth = sumExpFinalGrowth + Double.parseDouble(expGrowth.toString());
                        }
                    }

                    result.append(result).append( "</tr>");

                } else {

                    ScenarioC sc = new ScenarioC();
                    String nVal = pbro.getFieldValueString(i, b.toUpperCase().trim());
                    String v = sc.getModifiedNumber(Double.parseDouble(nVal), "");

                    result.append(result).append("<td id='tdId").append(k).append("_").append(i).append("'>").append(v).append("</td>");

                    if (k == 1) {
                        String nVal1 = pbro.getFieldValueString(i, datesVa.get(k - 1).toString());

                        if (nVal1.equalsIgnoreCase("0") && "0".equalsIgnoreCase(nVal1)) {

                            y1growth = 0.00;
                        } else {

                            y1growth = (Double.parseDouble(nVal) - Double.parseDouble(nVal1)) * 100 / Double.parseDouble(nVal1);
                        }

                    }
                    GraphMap.put("FirstyearGrowth", y1growth);
                    if (k == 2) {
                        String nVal1 = pbro.getFieldValueString(i, datesVa.get(k - 1).toString());

                        if (nVal1.equalsIgnoreCase("0") || "0".equalsIgnoreCase(nVal1)) {

                            y2growth = 0.00;
                        } else {

                            y2growth = (Double.parseDouble(nVal) - Double.parseDouble(nVal1)) * 100 / Double.parseDouble(nVal1);
                        }

                        if (expGrowthHm.size() > 0) {
                            if (nVal.equalsIgnoreCase("0") || "0".equalsIgnoreCase(nVal)) {
                                cumulgrowth = 0.00;
                            } else {
                                //("----"+par2+"----");
                                //("Sreekant exp"+expGrowthHm.get(par2));
                                if (expGrowthHm.get(par2) == null || expGrowthHm.get(par2).toString().equalsIgnoreCase("null")) {
                                    cumulgrowth = Double.parseDouble(nVal);
                                } else {
                                    cumulgrowth = ((100 + Double.parseDouble(String.valueOf(expGrowthHm.get(par2)))) / 100) * Double.parseDouble(nVal);
                                }

                            }
                        } else {
                            cumulgrowth = ((100 + Double.parseDouble(String.valueOf(expGrowth))) / 100) * Double.parseDouble(nVal);
                        }

                    }
                    GraphMap.put("SecondyearGrowth", y2growth);

                }

            }
            GraphVector.add(GraphMap);
            GraphMap = new LinkedHashMap();
            sumdouble1 = sumdouble1 + y1growth;
            sumdoubledouB2 = sumdoubledouB2 + y2growth;
            sumdouble3 = sumdouble1 / douArrayList.size();
            sumdoubledouB4 = sumdoubledouB2 / doubArrayList.size();
            if (i == pbro.getRowCount() - 1) {
                ScenarioC sc = new ScenarioC();
                result.append(result).append("<tr class=\"even\">");
                result.append(result).append( "<td style=\"font-weight:bold;font-color:blue\">Grand Total</td>");
                for (int fgt = 1; fgt < pbro.getColumnCount(); fgt++) {
                    for (int frgt = 0; frgt < pbro.getRowCount(); frgt++) {
                        String[] colname = pbro.getColumnNames();

                        fieldGrandTotal = fieldGrandTotal + Double.parseDouble(pbro.getFieldValueString(frgt, colname[fgt]));
                    }
                    result.append(result).append("<td style=\"font-weight:bold\">").append(sc.getModifiedNumber(fieldGrandTotal, "")).append("</td>");
                    fieldGrandTotal = 0;
                }

                if (getNormalHM().size() > 0) {
                    result.append(result).append("<td style=\"font-weight:bold\">").append(sc.getModifiedNumber(msrGrandTotal, "")).append( "</td>");
                    result.append(result).append("<td style=\"font-weight:bold\">").append(sc.getModifiedNumber(projGrandTotal, "")).append( "</td>");
                }
                bd = new BigDecimal(sumdouble3);
                bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                sumdouble3 = bd.doubleValue();
                bd = new BigDecimal(sumdoubledouB4);
                bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                sumdoubledouB4 = bd.doubleValue();
                bd = new BigDecimal(sumExpGrowth);
                bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                sumExpGrowth = bd.doubleValue();
                bd = new BigDecimal(sumforCastGrowth);
                bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                sumforCastGrowth = bd.doubleValue();
                bd = new BigDecimal(sumforCastFinalGrowth);
                bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                sumforCastFinalGrowth = bd.doubleValue();
                double resExpgro = sumExpGrowth / pbro.getRowCount();
                bd = new BigDecimal(resExpgro);
                bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                resExpgro = bd.doubleValue();
                result.append(result).append("<td style=\"font-weight:bold\">").append(sumdouble3).append("</td>");
                result.append(result).append("<td style=\"font-weight:bold\">").append(sumdoubledouB4).append( "</td>");
                result.append(result).append("<td id=\"expGrowthTotal\" style=\"font-weight:bold\">").append(resExpgro).append( "</td>");
                result.append(result).append("<td id=\"forecastTotal\" style=\"font-weight:bold\">").append(sumforCastGrowth).append( "</td>");
                result.append(result).append("<td style=\"font-weight:bold\" id=\"totalFinalGrowth\">").append(sumforCastFinalGrowth).append( "</td>");

                result.append(result).append( "</tr>");
            }

        }

        result.append(result).append("</table>");

        result.append(result).append( "<br>");

        result.append(result).append( "<table>");
        result.append(result).append( "<tr>");
        result.append(result).append( "<td>T=Trillion</td>");
        result.append(result).append( "<td>B=Billion</td>");
        result.append(result).append( "<td>M=Million</td>");
        result.append(result).append( "<td>K=Thousand</td>");
        result.append(result).append( "</tr>");
        result.append(result).append( "</table>");

        ////////////////////////////////////////.println(" graphArray--= -" + graphArray);
        //  ////(" result-========== " + result);
        all.put("graphArray", graphArray);
        all.put("result", result);
        all.put("datesVa", datesVa);


        all.put("viewByName", viewByName);
        modelId = "Budgeting";
        all.put("modelId", modelId);
        all.put("histMonths", histMonths);
        all.put("dimensionId", dimensionId);



        setViewBy(ViewbyVector);
        setDataset(GraphVector);

        String GraphPath = getGraph(GraphVector, ViewbyVector, getSession(), getResponse(), getResponse().getWriter());
        // session.removeAttribute("expGrowth");


        return all;
    }

    public void setChartDisplay(String map, String path) {
        chartDisplay = "<SCRIPT LANGUAGE=\"JavaScript\" SRC=\"overlib.js\">  </SCRIPT>";

        chartDisplay += "<div align='center' id=\"overDiv\" style=\"position:absolute;z-index:1000;\"></div>";
        chartDisplay += "<img align='top' usemap=\"#" + map + "\"  src=\"/pi/" + path + "\" border='0' > </img>";
    }

    public DefaultCategoryDataset buildDataSet(Vector alist, Vector viewby) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        ////("alist=" + alist);
//        ////("viewby=" + viewby);

        for (int i = 0; i <= alist.size() - 1; i++) {

            LinkedHashMap hm = (LinkedHashMap) alist.get(i);
            String[] hmkeyset = (String[]) hm.keySet().toArray(new String[0]);
//            ////("hm=" + hm);
            for (int j = 0; j < hmkeyset.length; j++) {
                double value = Double.parseDouble(String.valueOf(hm.get(hmkeyset[j])));
                dataset.setValue(value, String.valueOf(hmkeyset[j]), String.valueOf(viewby.get(i)));
//                ////("value=" + value + "String.valueOf(hmkeyset[j])=" + String.valueOf(hmkeyset[j]) + "String.valueOf(viewby.get(i))=" + String.valueOf(viewby.get(i)));
            }

        }


        return dataset;
    }

    public Vector getDataset() {
        return Dataset;
    }

    public void setDataset(Vector Dataset) {
        this.Dataset = Dataset;
    }

    public Vector getViewBy() {
        return ViewBy;
    }

    public void setViewBy(Vector ViewBy) {
        this.ViewBy = ViewBy;
    }
}
