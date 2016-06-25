/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportdesigner.db;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.contypes.GetConnectionType;
import com.progen.dashboard.DashboardConstants;
import com.progen.oneView.bd.OneViewBD;
import com.progen.report.DashletDetail;
import com.progen.report.PbReportCollection;
import com.progen.report.bd.PbReportTableBD;
import com.progen.report.display.DisplayParameters;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.entities.KPI;
import com.progen.report.entities.KPITarget;
import com.progen.report.kpi.KPIBuilder;
import com.progen.report.pbDashboardCollection;
import com.progen.report.query.PbReportQuery;
import com.progen.report.query.QueryExecutor;
import com.progen.reportdesigner.action.GroupMeassureParams;
import com.progen.reportview.db.CreateKPIFromReport;
import com.progen.userlayer.action.GenerateDragAndDrophtml;
import java.awt.Color;
import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.*;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

/**
 *
 * @author mahesh.sanampudi@progenbusiness.com
 */
public class DashboardTemplateDAO extends PbDb {

    // DashBoardTemplateResourcBundle resourceBundle = new DashBoardTemplateResourcBundle();
    ResourceBundle resourceBundle;
    public static Logger logger = Logger.getLogger(DashboardTemplateDAO.class);

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new DashBoardTemplateResBunSqlserver();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new DashBoardTemplateResourcBundleMySql();
            } else {
                resourceBundle = new DashBoardTemplateResourcBundle();
            }
        }

        return resourceBundle;
    }

    public String getUserFolders(String grpIds) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = grpIds;
        String FolderId = "";
        String FolderName = "";
        String sql = getResourceBundle().getString("getUserFolders");

        StringBuffer outerBuffer = new StringBuffer();

        try {
//            new ProgenConnection().getConnection();
            finalQuery = buildQuery(sql, obj);

            //////////.println("in dashboard templateDAo sql=" + finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                FolderId = retObj.getFieldValueString(i, colNames[0]);
                FolderName = retObj.getFieldValueString(i, colNames[1]);
                outerBuffer.append("<li class='closed' id='" + FolderId + "'>");
                //outerBuffer.append("<img src='icons pinvoke/folder-horizontal.png'></img>");
                outerBuffer.append("<input type='checkbox' name='userfldsList' id='" + FolderId + "' onclick='javascript:getUserDims()' ><span><font size='1px' face='verdana'><b>" + FolderName + "</b></font></span>");
                outerBuffer.append("</li>");
            }
        }catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        logger.info("successful");

        return outerBuffer.toString();
    }

    public String getUserFoldersByUserId(String pbUserId) {

        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = pbUserId;
//        obj[1] = pbUserId;
//        obj[2] = pbUserId;

        String FolderId = "";
        String FolderName = "";
        String getUserFoldersByUserIdQuery = getResourceBundle().getString("getUserFoldersByUserId");

        StringBuffer outerBuffer = new StringBuffer();

        try {

            finalQuery = buildQuery(getUserFoldersByUserIdQuery, obj);
//            ProgenLog.log(ProgenLog.FINE,this,"getUserFoldersByUserId","finalQuery in getuserfoldersByUserId--"+finalQuery);
            logger.info("finalQuery in getuserfoldersByUserId--" + finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                FolderId = retObj.getFieldValueString(i, colNames[0]);
                FolderName = retObj.getFieldValueString(i, colNames[1]);
                outerBuffer.append("<li class='closed' id='" + FolderId + "'>");
                //outerBuffer.append("<img src='icons pinvoke/folder-horizontal.png'></img>");
                //outerBuffer.append("<input type='checkbox' name='userfldsList' id='" + FolderId + "' onclick='javascript:getUserDims();getFavParams();' ><span><font size='1px' face='verdana'><b>" + FolderName + "</b></font></span>");
                outerBuffer.append("<input type='radio' name='userfldsList' id='" + FolderId + "' onclick='javascript:getUserDims();' ><span><font size='1px' face='verdana'><b>" + FolderName + "</b></font></span>");
                outerBuffer.append("</li>");
            }
        }catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception ex) {
            logger.error("Exception: ", ex);
        } 
        logger.info("successful");
        return outerBuffer.toString();
    }

    public String getUserDims(String foldersIds, String userId) {
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
        String finalQuery = null;
        String[] colNames = null;
        String finalQuery1 = null;
        String[] colNames1 = null;
        Object obj[] = new Object[1];
        Object obj1[] = new Object[2];
        obj[0] = foldersIds;
        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
            obj1[0] = foldersIds;
        } else {
            obj1[0] = "''";
        }
        obj1[1] = userId;
        String dimId = "";
        String dimName = "";
        String subFolderId = "";
        String favName = "";
        String busFolderId = "";
        String elementId = "";
        String sql = getResourceBundle().getString("getUserDims");
        String sql1 = getResourceBundle().getString("getFavParams");

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);


            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
//added for Favourite parameters on 29-12-2009
            finalQuery1 = buildQuery(sql1, obj1);
            retObj1 = execSelectSQL(finalQuery1);
            colNames1 = retObj1.getColumnNames();

            // add for time dimension

            String minTimeLevel = getUserFolderMinTimeLevel(foldersIds);
            if (minTimeLevel.equals("5")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Period Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Period Basis' style='font-family:verdana;font-size:8pt'>Time-Period Basis</span>");
                outerBuffer.append("</li>");
                outerBuffer.append("<li class='closed' id='timeDimension-Range Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Range Basis' style='font-family:verdana;font-size:8pt'>Time-Range Basis</span>");
                outerBuffer.append("</li>");
                /*
                 * outerBuffer.append("<li class='closed'
                 * id='timeDimension-Rolling Period'>");
                 * outerBuffer.append("<img src='icons
                 * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
                 * id='elmnt-Time Dimension-Rolling Period'
                 * style='font-family:verdana;font-size:8pt'>Time-Rolling
                 * Period</span>"); outerBuffer.append("</li>"); //commented
                 * only for Nerolac /*outerBuffer.append("<li class='closed'
                 * id='timeDimension-Month Range Basis'>");
                 * outerBuffer.append("<img src='icons
                 * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
                 * id='elmnt-Time Dimension-Month Range Basis'
                 * style='font-family:verdana;font-size:8pt'>Time-Month Range
                 * Basis</span>"); outerBuffer.append("</li>");
                 * outerBuffer.append("<li class='closed'
                 * id='timeDimension-Quarter Range Basis'>");
                 * outerBuffer.append("<img src='icons
                 * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
                 * id='elmnt-Time Dimension-Quarter Range Basis'
                 * style='font-family:verdana;font-size:8pt'>Time-Quarter Range
                 * Basis</span>");
                 * outerBuffer.append("</li>");outerBuffer.append("<li
                 * class='closed' id='timeDimension-Year Range Basis'>");
                 * outerBuffer.append("<img src='icons
                 * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
                 * id='elmnt-Time Dimension-Year Range Basis'
                 * style='font-family:verdana;font-size:8pt'>Time-Year Range
                 * Basis</span>"); outerBuffer.append("</li>");
                 */

            } else if (minTimeLevel.equals("4")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Week Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Week Basis' style='font-family:verdana;font-size:8pt'>Time-Week Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("3")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Month Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Month Basis' style='font-family:verdana;font-size:8pt'>Time-Month Basis</span>");
                outerBuffer.append("</li>");
                outerBuffer.append("<li class='closed' id='timeDimension-Compare Month Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Compare Month Basis' style='font-family:verdana;font-size:8pt'>Time-Compare Month Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("2")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Quarter Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Quarter Basis' style='font-family:verdana;font-size:8pt'>Time-Quarter Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("1")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Year Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Year Basis' style='font-family:verdana;font-size:8pt'>Time-Year Basis</span>");
                outerBuffer.append("</li>");
            }
            // end of adding

            //added for favparams
            if (retObj1.getRowCount() > 0) {
                outerBuffer.append("<li class='closed'>");
                outerBuffer.append("<img src='icons pinvoke/folder-horizontal.png'>&nbsp;<span>Favourite Params</span>");
                outerBuffer.append("<ul id='favourParams' class='background'>");
                outerBuffer.append(getFavParams(foldersIds, userId));
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }

            for (int i = 0; i < retObj.getRowCount(); i++) {
                dimId = retObj.getFieldValueString(i, colNames[0]);
                dimName = retObj.getFieldValueString(i, colNames[1]);
                subFolderId = retObj.getFieldValueString(i, colNames[2]);

                outerBuffer.append("<li class='closed' id='" + dimId + "'>");
                outerBuffer.append("<img src='icons pinvoke/Dimension.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;font-size:8pt'>" + dimName + "</span>");
                outerBuffer.append("<ul id='dimName-" + dimName + "'>");

                outerBuffer.append(getUserDimsMbrs(subFolderId, dimId));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        }catch (SQLException ex) {
           logger.error("Exception: ", ex);
        }
        catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        logger.info("successful");
        return outerBuffer.toString();
    }

    public String getUserDimsMbrs(String subFolderId, String dimId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[2];
        obj[0] = subFolderId;
        obj[1] = dimId;
        String MbrId = "";
        String MbrName = "";
        String elementid = "";
        String sql = getResourceBundle().getString("getUserDimsMbrs");
        StringBuffer outerBuffer = new StringBuffer();
        try {
//            new ProgenConnection().getConnection();
            finalQuery = buildQuery(sql, obj);

            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                MbrId = retObj.getFieldValueString(i, colNames[2]);
                MbrName = retObj.getFieldValueString(i, colNames[1]);
                elementid = retObj.getFieldValueString(i, colNames[0]);
                outerBuffer.append("<li class='closed' id='" + MbrId + "'>");
                outerBuffer.append("<img src='icons pinvoke/Dimension.png'></img>");
                outerBuffer.append("<span id='elmnt-" + elementid + "' style='font-family:verdana;font-size:8pt'>" + MbrName + "</span>");

                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }   catch (Exception ex) {
            logger.error("Exception: ", ex);
        }       logger.info("successful");
        return outerBuffer.toString();
    }

    public String getKpis(String foldersIds) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        String complexKPI = "";

        String factName = "";
        String subFolderTabid = "";


        Object obj[] = new Object[1];

        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
            obj[0] = foldersIds;
        } else {
            obj[0] = "''";
        }

        String sql = getResourceBundle().getString("getKpis");
        StringBuffer outerBuffer = new StringBuffer();
        try {
            finalQuery = buildQuery(sql, obj);

            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                factName = retObj.getFieldValueString(i, colNames[1]);
                subFolderTabid = retObj.getFieldValueString(i, colNames[0]);
                outerBuffer.append("<li class='closed' id='" + factName + i + "'>");
                outerBuffer.append("<img src='icons pinvoke/table.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;font-size:8pt'>" + factName + "</span>");
                outerBuffer.append("<ul id='factName-" + factName + "'>");

                outerBuffer.append(getKpiElements(foldersIds, subFolderTabid));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
//            complexKPI=getComplexKPI();
//            outerBuffer.append(complexKPI);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        logger.info("successful");
        return outerBuffer.toString();
    }

    public String getKpiElements(String userFolderIds, String subFolderTabid) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[2];
        obj[0] = userFolderIds;
        obj[1] = subFolderTabid;
        String ElementId = "";
        String REFElementId = "";
        String ElementId1 = "";
        String REFElementId1 = "";
        String ElementName = "";
        String ElementName1 = "";
        String Formula = "";
        String colId = "";

        // String sql = getResourceBundle().getString("getFactElements"); qry 4 getting summerized &nonsummerized measures
        String sql = getResourceBundle().getString("getFactElementsForSummerizedMeasures");//qry 4 gettingonly summerized measure
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);


            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            ArrayList list = new ArrayList();
            String viewFormulaClass = "";
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
                        outerBuffer.append("<img src='icons pinvoke/document-attribute-f.png'></img>");
                        viewFormulaClass = "formulaViewMenu";
                    } else {
                        outerBuffer.append("<img src='icons pinvoke/report.png'></img>");
                    }
                    outerBuffer.append("<span class='" + viewFormulaClass + "' id='" + ElementId + "'  title='" + Formula + "' style='font-family:verdana;font-size:8pt'>" + ElementName + "</span>");

                    outerBuffer.append("<ul >");
                    for (int j = 0; j < retObj.getRowCount(); j++) {
                        ElementId1 = retObj.getFieldValueString(j, colNames[0]);
                        REFElementId1 = retObj.getFieldValueString(j, colNames[2]);
                        ElementName1 = retObj.getFieldValueString(j, colNames[1]);
                        Formula = retObj.getFieldValueString(j, colNames[7]);
                        if (ElementId.equalsIgnoreCase(REFElementId1) && !(ElementId.equalsIgnoreCase(ElementId1))) {
                            outerBuffer.append("<li class='closed' id='" + ElementId1 + "'>");
                            if (!Formula.equalsIgnoreCase("")) {
                                outerBuffer.append("<img src='icons pinvoke/document-attribute-f.png'></img>");
                            } else {
                                outerBuffer.append("<img src='icons pinvoke/report.png'></img>");
                            }
                            outerBuffer.append("<span id='" + ElementId1 + "'   title='" + Formula + "' style='font-family:verdana;font-size:8pt'>" + ElementName1 + "</span>");
                            outerBuffer.append("</li>");
                        }
                    }
                    outerBuffer.append("</ul>");
                    outerBuffer.append("</li>");
                }
            }
            //code to add custom measures

            String getCustomFactElements = getResourceBundle().getString("getCustomFactElements");
            Object objnew[] = new Object[2];
            objnew[0] = userFolderIds;
            finalQuery = buildQuery(getCustomFactElements, objnew);


            retObj = execSelectSQL(finalQuery);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                String testQuery = "select distinct sub_folder_tab_id  from prg_user_all_info_details where element_id in(" + removeLastCommas(retObj.getFieldValueString(i, 5)) + ") and sub_folder_tab_id=" + subFolderTabid;
                viewFormulaClass = "formulaViewMenu";
                PbReturnObject testpbro = execSelectSQL(testQuery);
                if (testpbro.getRowCount() > 0) {
                    ElementId = retObj.getFieldValueString(i, colNames[0]);
                    ElementName = retObj.getFieldValueString(i, colNames[1]);
                    REFElementId = retObj.getFieldValueString(i, colNames[2]);


                    Formula = retObj.getFieldValueString(i, colNames[7]);
                    if (ElementId.equalsIgnoreCase(REFElementId)) {
                        if (!(list.contains(ElementId))) {
                            outerBuffer.append("<li class='closed'>");
                            outerBuffer.append("<img src='icons pinvoke/document-attribute-f.png'></img>");
                            outerBuffer.append("<span   class='" + viewFormulaClass + "' id='" + ElementId + "'  title='" + Formula + "'  style='font-family:verdana;font-size:8pt'>" + ElementName + "</span>");

                            outerBuffer.append("<ul >");
                            for (int j = 0; j < retObj.getRowCount(); j++) {
                                ElementId1 = retObj.getFieldValueString(j, colNames[0]);
                                REFElementId1 = retObj.getFieldValueString(j, colNames[2]);
                                ElementName1 = retObj.getFieldValueString(j, colNames[1]);
                                Formula = retObj.getFieldValueString(j, colNames[7]);
                                if (ElementId.equalsIgnoreCase(REFElementId1) && !(ElementId.equalsIgnoreCase(ElementId1))) {
                                    outerBuffer.append("<li class='closed' id='" + ElementId1 + "'>");
                                    outerBuffer.append("<img src='icons pinvoke/document-attribute-f.png'></img>");
                                    outerBuffer.append("<span  id='" + ElementId1 + "'  title='" + Formula + "' style='font-family:verdana;font-size:8pt'>" + ElementName1 + "</span>");
                                    outerBuffer.append("</li>");
                                }
                            }
                            outerBuffer.append("</ul>");
                            outerBuffer.append("</li>");
                            list.add(ElementId);
                        }
                    }
                }
            }

        }  catch (SQLException ex) {
           logger.error("Exception: ", ex);
        }   catch (Exception ex) {
            logger.error("Exception: ", ex);
        }       logger.info("successful");
        return outerBuffer.toString();
    }

    public void displayDashBoardGraph(String repId) {
        String reportId = repId;

    }

    public String getGraphReports() {
        PbReturnObject retObj = null;

        String[] colNames = null;

        String repName = "";
        String repId = "";
        String getReportssql = getResourceBundle().getString("getReports");
        StringBuffer outerBuffer = new StringBuffer();
        try {

            retObj = execSelectSQL(getReportssql);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                repId = retObj.getFieldValueString(i, colNames[0]);
                repName = retObj.getFieldValueString(i, colNames[1]);
                outerBuffer.append("<li class='closed' id='" + repName + i + "'>");
                outerBuffer.append("<img src='icons pinvoke/report.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;font-size:8pt' onclick=\"dragGraph(" + repId + ")\">" + repName + "</span>");
                outerBuffer.append("<ul id='repName-" + repName + "'>");

                outerBuffer.append(getGraphs(repId));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }  catch (Exception ex) {
            logger.error("Exception: ", ex);
        }        logger.info("successful");
        return outerBuffer.toString();
    }

    public String getGraphs(String repId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = repId;
        String graphId = "";
        String graphName = "";
        String reportId = repId;
        String getGraphsbyReportssql = getResourceBundle().getString("getGraphsbyReports");
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(getGraphsbyReportssql, obj);

            retObj = execSelectSQL(finalQuery);
            if (retObj != null) {
                colNames = retObj.getColumnNames();
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    graphId = retObj.getFieldValueString(i, colNames[0]);
                    graphName = retObj.getFieldValueString(i, colNames[1]);

                    outerBuffer.append("<li class='closed' id='" + graphId + "'>");
                    outerBuffer.append("<img src='icons pinvoke/chart.png'></img>");
                    outerBuffer.append("<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href=javascript:buildDbrdGraph('" + reportId + "','" + graphId + "') style='text-Decoration:none'>" + graphName + "</a></span>");
                    outerBuffer.append("</li>");
                }
            }
        }  catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }  catch (Exception ex) {
            logger.error("Exception: ", ex);
        }        return outerBuffer.toString();
        }

    public String dispDbrdParameters(String parameters, HashMap ParametersHashMap) {
        DisplayParameters disp = new DisplayParameters();
        ArrayList dispParams = new ArrayList();
        String[] paramIds = null;
        HashMap TimeDimHashMap = null;
        ArrayList TimeDimList = new ArrayList();
        String temp = "";
        try {

            TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
            TimeDimList = (ArrayList) ParametersHashMap.get("timeParameters");


            paramIds = parameters.split(",");

            HashMap paramValues = new HashMap();
            if (paramIds != null || paramIds[0] != "") {
                for (int i = 0; i < paramIds.length; i++) {
                    dispParams.add(paramIds[i]);
                    paramValues.put(paramIds[i], "All");
                }
            }
            temp = disp.displayParamwithTime(dispParams, TimeDimHashMap);
            //temp = disp.displayParam(dispParams) + disp.displayTimeParams(TimeDimHashMap);//compemted by Amit
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return temp;
    }

    public ArrayList getReportQryAggregations(ArrayList reportQryElementIds) {
        String sqlQuery = getResourceBundle().getString("getReportQryAggregations");
        ArrayList reportQryAggregations = new ArrayList();
//        String StrReportQryElementIds = "";
        StringBuilder StrReportQryElementIds = new StringBuilder(200);
        String finalQuery = "";
        PbReturnObject retObj = null;
        Object[] Obj = new Object[1];
        String[] dbColumns = null;
        try {
            for (int i = 0; i < reportQryElementIds.size(); i++) {
                StrReportQryElementIds.append(",").append(String.valueOf(reportQryElementIds.get(i)));
//                StrReportQryElementIds += "," + String.valueOf(reportQryElementIds.get(i));
            }
//            if (!("".equalsIgnoreCase(StrReportQryElementIds))) {
            if (StrReportQryElementIds.length() > 0) {
                StrReportQryElementIds = new StringBuilder(StrReportQryElementIds.substring(1));
            }

            Obj[0] = StrReportQryElementIds;
            finalQuery = buildQuery(sqlQuery, Obj);

            retObj = execSelectSQL(finalQuery);
            dbColumns = retObj.getColumnNames();

            for (int j = 0; j < retObj.getRowCount(); j++) {
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));
            }
        }  catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }    catch (Exception ex) {
            logger.error("Exception: ", ex);
        }      return reportQryAggregations;
        }

    public String getKpiName(String kpielmntId) {

        //view_elements.add(getKpiNameQuery);
        String kpiNameQuery = getResourceBundle().getString("getKpiNameQuery");
        PbReturnObject retkpiObj = null;
        String finalQuery = "";
        Object[] seq = new Object[1];
        seq[0] = kpielmntId;
        try {
            finalQuery = buildQuery(kpiNameQuery, seq);

            retkpiObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }  catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return retkpiObj.getFieldValueString(0, 0);
    }

    public int getSequence(String sequence) {
        String sqlQuery = getResourceBundle().getString("getSequenceNumber");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] seq = new Object[1];
        seq[0] = sequence + ".nextval";
        try {
            finalQuery = buildQuery(sqlQuery, seq);
            //
            retObj = execSelectSQL(finalQuery);
        }catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }  catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return retObj.getFieldValueInt(0, 0);
    }

    public void insertDashboard(int reportId, String reportName, String reportDesc, String mapEnabled) {
        String query = getResourceBundle().getString("insertDashboard");
        String finalQuery = "";
        reportName = reportName.replace("'", "''");
        reportDesc = reportDesc.replace("'", "''");
        Object[] reportDetails = null;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            reportDetails = new Object[4];
            //reportDetails[0] = reportId;
            reportDetails[0] = reportName;
            reportDetails[1] = reportDesc;
            reportDetails[2] = reportDesc;
            reportDetails[3] = mapEnabled;
        } else {
            reportDetails = new Object[5];
            reportDetails[0] = reportId;
            reportDetails[1] = reportName;
            reportDetails[2] = reportDesc;
            reportDetails[3] = reportDesc;
            reportDetails[4] = mapEnabled;

        }

        try {
            finalQuery = buildQuery(query, reportDetails);
            ////.println("insertDashboard finalQuery="+finalQuery);
            execModifySQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    public void insertDashboardDetails(int reportId, String folderIds) {
        String query = getResourceBundle().getString("insertDashboardDetails");
        String finalQuery = "";
        Object[] reportDets = new Object[2];

        reportDets = new Object[2];
        reportDets[0] = reportId;
        reportDets[1] = folderIds;

        try {
            finalQuery = buildQuery(query, reportDets);
            ////.println("finalQuery=" + finalQuery);
            execModifySQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    public PbReturnObject getParameters(String str1, String str2) {
        String sqlQuery = getResourceBundle().getString("getParameters");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] params = new Object[2];
        Object[] params1 = new Object[1];
        ////.println("str1=" + str1);
        ////.println("str2=" + str2);
        params[0] = str1;
        params[1] = str2;

        params1[0] = str1;//sqlserver

        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                String kpiCase = " case ";
                StringBuilder kpiCase = new StringBuilder(100);
                kpiCase.append(" case ");
                String[] kpiCols = new String[str2.length()];
                kpiCols = str2.split(",");
                for (int i = 0; i < kpiCols.length - 1;) {
                    if (!kpiCols[i].equalsIgnoreCase("TIME")) {
//                    kpiCase += " when element_id =" + kpiCols[i] + " then " + kpiCols[i + 1] + " ";
                        kpiCase.append(" when element_id =").append(kpiCols[i]).append(" then ").append(kpiCols[i + 1]).append(" ");
                    }
                    i = i + 2;
                }
//                kpiCase += " else 10000 end ";
                kpiCase.append(" else 10000 end ");
                sqlQuery = buildQuery(sqlQuery, params1);
                finalQuery = sqlQuery + kpiCase;
            } else {

                finalQuery = buildQuery(sqlQuery, params);
            }
//            
            if (!str1.equalsIgnoreCase("") && !str1.equalsIgnoreCase("Time")) {
                retObj = execSelectSQL(finalQuery);
            }
        }catch (SQLException ex) {
            logger.error("Exception: ", ex);
            retObj = null;
        }catch (Exception ex) {
            logger.error("Exception: ", ex);
            retObj = null;
        }

        return retObj;
    }

    public void insertReportParamDetails(ArrayList queries) {
        try {
            executeMultiple(queries);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    public void insertKpiGraphs(ArrayList views) {
        try {
            for (int i = 0; i < views.size(); i++) {
                // 
                execModifySQL(views.get(i).toString());
            }
//            executeMultiple(views);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    public PbReturnObject getAggregation(String kpiElmnt) {
        String aggregationQry = getResourceBundle().getString("getAggregationType");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object obj[] = new Object[1];
        obj[0] = kpiElmnt;
        try {
            finalQuery = buildQuery(aggregationQry, obj);

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
           logger.error("Exception: ", ex);
            retObj = null;
        } catch (Exception ex) {
           logger.error("Exception: ", ex);
            retObj = null;
        }
        return retObj;
    }

    public PbReturnObject getParamDetails(int reportId) {
        String sqlQuery = getResourceBundle().getString("getParamDetails");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] params = new Object[1];
        params[0] = reportId;
        try {
            finalQuery = buildQuery(sqlQuery, params);

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        }catch (Exception ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        }
        return retObj;

    }

    public ArrayList insertReportTimeDimensions(ArrayList timeDetails, HashMap timeDimHashMap, int reportId, ArrayList queries) {
        String sqlQuery = getResourceBundle().getString("insertReportTimeDimensions");
        String finalQuery = "";
        Object[] obj = new Object[3];
        obj[0] = timeDetails.get(1);
        obj[1] = timeDetails.get(0);
        obj[2] = reportId;
        finalQuery = buildQuery(sqlQuery, obj);
        queries.add(finalQuery);
        //queries = insertReportTimeDimensionsDetails(timeDimHashMap, queries);
        return queries;
    }
    //19-Oct-09

    public PbReturnObject checkDashboardName() {
        String query = getResourceBundle().getString("getDashboardNames");
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(query);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

        return retObj;
    }

    public String getScoreCards(int userId) {
        PbReturnObject retObj = null;
        String[] colNames = null;
        String repName = "";
        String repId = "";
        String graphId = "";
        String graphName = "";
        String scardId = "";
        String scardName = "";
        Object[] dataValues = null;
        String getScardSQL = "select usr.user_id, usr.scard_id, scard.scard_name from prg_ar_scard_users usr"
                + ", prg_ar_scard_master scard where usr.scard_id = scard.scard_id and usr.user_id=" + userId;
        StringBuilder outerBuffer = new StringBuilder("");

        try {

            retObj = execSelectSQL(getScardSQL);

            if (retObj != null) {
                colNames = retObj.getColumnNames();
                String tempReportId = null;
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    scardId = retObj.getFieldValueString(i, colNames[1]);
                    scardName = retObj.getFieldValueString(i, colNames[2]);

                    outerBuffer.append("<li class='closed' id='" + scardName + "'>");
                    outerBuffer.append("<img src='icons pinvoke/report.png'></img>");
                    outerBuffer.append("<span style='font-family:verdana;font-size:8pt' id='" + scardId + "'>" + scardName + "</span>");
                    outerBuffer.append("<ul id='repName-" + scardName + "'>");
                    outerBuffer.append("</ul>");
                    outerBuffer.append("</li>");
                }

            }

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }  catch (Exception ex) {
            logger.error("Exception: ", ex);
        }       return outerBuffer.toString();
        }

    public String getGraphReportsByBuzRoles(String buzRoles) {
        PbReturnObject retObj = null;
        String[] colNames = null;
        String repName = "";
        String repId = "";
        String graphId = "";
        String graphName = "";
        Object[] dataValues = null;
        String getReportssql = getResourceBundle().getString("getReportsByBuzRoles");
        StringBuffer outerBuffer = new StringBuffer("");
        String finalQuery = "";
        try {

            dataValues = new Object[1];
            dataValues[0] = buzRoles;
            finalQuery = buildQuery(getReportssql, dataValues);
            retObj = execSelectSQL(finalQuery);

            if (retObj != null) {
                colNames = retObj.getColumnNames();
                String tempReportId = null;
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    repId = retObj.getFieldValueString(i, colNames[0]);
                    repName = retObj.getFieldValueString(i, colNames[1]);

                    graphId = retObj.getFieldValueString(i, colNames[2]);
                    graphName = retObj.getFieldValueString(i, colNames[3]);


                    if (tempReportId == null) {
                        tempReportId = repId;

                        outerBuffer.append("<li class='closed' id='" + repName + i + "'>");
                        outerBuffer.append("<img src='icons pinvoke/report.png'></img>");
                        outerBuffer.append("<span style='font-family:verdana;font-size:8pt' onclick=\"dragGraph(" + repId + ")\">" + repName + "</span>");
                        outerBuffer.append("<ul id='repName-" + repName + "'>");

                        outerBuffer.append("<li class='closed' id='" + graphId + "'>");
                        outerBuffer.append("<img src='icons pinvoke/chart.png'></img>");
                        outerBuffer.append("<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href=javascript:buildDbrdGraph('" + repId + "','" + graphId + "') style='text-Decoration:none'>" + graphName + "</a></span>");
                        outerBuffer.append("</li>");



                    } else {
                        if (tempReportId.equalsIgnoreCase(repId)) {
                            outerBuffer.append("<li class='closed' id='" + graphId + "'>");
                            outerBuffer.append("<img src='icons pinvoke/chart.png'></img>");
                            outerBuffer.append("<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href=javascript:buildDbrdGraph('" + repId + "','" + graphId + "') style='text-Decoration:none'>" + graphName + "</a></span>");
                            outerBuffer.append("</li>");
                        } else {
                            outerBuffer.append("</ul>");
                            outerBuffer.append("</li>");

                            outerBuffer.append("<li class='closed' id='" + repName + i + "'>");
                            outerBuffer.append("<img src='icons pinvoke/report.png'></img>");
                            outerBuffer.append("<span style='font-family:verdana;font-size:8pt' onclick=\"dragGraph(" + repId + ")\">" + repName + "</span>");
                            outerBuffer.append("<ul id='repName-" + repName + "'>");

                            outerBuffer.append("<li class='closed' id='" + graphId + "'>");
                            outerBuffer.append("<img src='icons pinvoke/chart.png'></img>");
                            outerBuffer.append("<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href=javascript:buildDbrdGraph('" + repId + "','" + graphId + "') style='text-Decoration:none'>" + graphName + "</a></span>");
                            outerBuffer.append("</li>");

                            tempReportId = repId;
                        }
                    }
                }
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");

            }

        } catch (SQLException ex) {
           logger.error("Exception: ", ex);
        }  catch (Exception ex) {
           logger.error("Exception: ", ex);
        }return outerBuffer.toString();
        }

    public String getUserDimsNew(String foldersIds) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = foldersIds;
        String dimId = "";
        String dimName = "";
        String subFolderId = "";
        String sql = getResourceBundle().getString("getUserDims");

        StringBuffer outerBuffer = new StringBuffer();
        try {
            finalQuery = buildQuery(sql, obj);


            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            // add for time dimension

            String minTimeLevel = getUserFolderMinTimeLevel(foldersIds);
            if (minTimeLevel.equals("5")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Period Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Period Basis' style='font-family:verdana;font-size:8pt'>Time-Period Basis</span>");
                outerBuffer.append("</li>");
                outerBuffer.append("<li class='closed' id='timeDimension-Range Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Range Basis' style='font-family:verdana;font-size:8pt'>Time-Range Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("4")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Week Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Week Basis' style='font-family:verdana;font-size:8pt'>Time-Week Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("3")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Month Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Month Basis' style='font-family:verdana;font-size:8pt'>Time-Month Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("2")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Quarter Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Quarter Basis' style='font-family:verdana;font-size:8pt'>Time-Quarter Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("1")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Year Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Year Basis' style='font-family:verdana;font-size:8pt'>Time-Year Basis</span>");
                outerBuffer.append("</li>");
            }
            // end of adding

            for (int i = 0; i < retObj.getRowCount(); i++) {
                dimId = retObj.getFieldValueString(i, colNames[0]);
                dimName = retObj.getFieldValueString(i, colNames[1]);
                subFolderId = retObj.getFieldValueString(i, colNames[2]);

                outerBuffer.append("<li class='closed' id='" + dimId + "'>");
                outerBuffer.append("<img src='icons pinvoke/Dimension.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;font-size:8pt'>" + dimName + "</span>");
                outerBuffer.append("<ul id='dimName-" + dimName + "'>");

                outerBuffer.append(getUserDimsMbrs(subFolderId, dimId));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return outerBuffer.toString();
    }

    public String getUserFolderMinTimeLevel(String str) throws Exception {
        String userFolderIdsStr = str;
        String minTimeLevel = "";

        String qry = getResourceBundle().getString("getUserFolderMinTimeLevel");

        Object obj[] = new Object[1];
        obj[0] = userFolderIdsStr;

        String finalQry = buildQuery(qry, obj);

        PbReturnObject pbro = execSelectSQL(finalQry);

        minTimeLevel = String.valueOf(pbro.getFieldValueInt(0, "MIN_LEVEL"));


        return minTimeLevel;
    }

    public PbReturnObject getKpiDrillReports(String elmntId, String foldersIds) {
        String getKpiDrillReportsQuery = getResourceBundle().getString("getKpiDrillReports");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] kpiReps = new Object[2];
        kpiReps[0] = elmntId;
        kpiReps[1] = foldersIds;
        try {
            finalQuery = buildQuery(getKpiDrillReportsQuery, kpiReps);

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
           logger.error("Exception: ", ex);
            retObj = null;
        }
         catch (Exception ex) {
           logger.error("Exception: ", ex);
            retObj = null;
        }
        return retObj;

    }

    public PbReturnObject getKpiDrillNames(String kpiId, String kpiMasterId) {
        String getKpiDrillNamesQuery = getResourceBundle().getString("getKpiDrillNames");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] kpiNames = new Object[2];
        kpiNames[0] = kpiId;
        kpiNames[1] = kpiMasterId;
        try {
            finalQuery = buildQuery(getKpiDrillNamesQuery, kpiNames);

            retObj = execSelectSQL(finalQuery);
        }  catch (SQLException ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        }catch (Exception ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        }
        return retObj;

    }

    public PbReturnObject getNewDbrdGrpDets(String elmntIds) {

        String getNewDbrdGrpDetsQuery = getResourceBundle().getString("getNewDbrdGrpDets");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] kpiNames = new Object[1];
        kpiNames[0] = elmntIds;
        try {
            finalQuery = buildQuery(getNewDbrdGrpDetsQuery, kpiNames);

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
            retObj = null;
        }  catch (Exception ex) {
            logger.error("Exception: ", ex);
            retObj = null;
        }       return retObj;
        }

    public PbReturnObject getNewDbrdGrpTypeId(String grpType) {

        String getNewDbrdGrpTypeIdQuery = getResourceBundle().getString("getNewDbrdGrpTypeId");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] kpiNames = new Object[1];
        kpiNames[0] = grpType;
        try {
            finalQuery = buildQuery(getNewDbrdGrpTypeIdQuery, kpiNames);

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        } catch (Exception ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        }        return retObj;
        }

    public PbReturnObject getNewDbrdGrpSizeId(String grpSize) {

        String getNewDbrdGrpSizeIdQuery = getResourceBundle().getString("getNewDbrdGrpSizeId");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] kpiNames = new Object[1];
        kpiNames[0] = grpSize;
        try {
            finalQuery = buildQuery(getNewDbrdGrpSizeIdQuery, kpiNames);

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        } catch (Exception ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        }       return retObj;
        }

    public PbReturnObject getDbrdViewOrder(String dashboardId) {
        String getDbrdViewOrderQuery = getResourceBundle().getString("getDbrdViewOrder");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] kpiNames = new Object[1];
        kpiNames[0] = dashboardId;
        try {
            finalQuery = buildQuery(getDbrdViewOrderQuery, kpiNames);

            retObj = execSelectSQL(finalQuery);
        }catch (SQLException ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        } catch (Exception ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        }       return retObj;
        }

    public PbReturnObject getKpiElmntAggr(String elmntId) {
        String getKpiElmntAggrQuery = getResourceBundle().getString("getKpiElmntAggr");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] kpiNames = new Object[1];
        kpiNames[0] = elmntId;
        try {
            finalQuery = buildQuery(getKpiElmntAggrQuery, kpiNames);

            retObj = execSelectSQL(finalQuery);
        }catch (SQLException ex) {
            logger.error("Exception: ", ex);
            retObj = null;
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
            retObj = null;
        }       return retObj;
        }

    public String getCustomDbrdId(String Query) throws Exception {
        String CustomDbrdId = String.valueOf(getSequenceNumber(Query));
        return CustomDbrdId;
    }

    public String getCurrentDbrdId() throws Exception {
        //String CurrentDbrdId = String.valueOf(getSequenceNumber("select PRG_AR_REPORT_MASTER_SEQ.currval from dual"));

        String CurrentDbrdIdQuery = ("select PRG_AR_REPORT_MASTER_SEQ.currval from dual");
        PbReturnObject retObj = null;
        String CurrentDbrdId = "";
        try {
            //finalQuery = buildQuery(getKpiElmntAggrQuery);

            retObj = execSelectSQL(CurrentDbrdIdQuery);
            CurrentDbrdId = String.valueOf(retObj.getFieldValueInt(0, 0));
        } catch (Exception e) {
            logger.error("Exception: ", e);
            retObj = null;
        }
        return CurrentDbrdId;
    }

    public String getFavParams(String foldersIds, String usrId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        PbReturnObject retObj1 = null;
        String finalQuery1 = null;
        String[] colNames1 = null;
        Object obj[] = new Object[2];
        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
            obj[0] = foldersIds;
        } else {
            obj[0] = "''";
        }
        obj[1] = usrId;
        String userId = "";
        String favName = "";
        String subFolderId = "";
        String elementId = "";
        String userId1 = "";
        String favName1 = "";
        String subFolderId1 = "";
        String sql = getResourceBundle().getString("getFavParams");
        String getFavParamNamessql = getResourceBundle().getString("getFavParamNames");

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            finalQuery1 = buildQuery(getFavParamNamessql, obj);
            retObj1 = execSelectSQL(finalQuery1);
            colNames1 = retObj1.getColumnNames();

            /*
             * String minTimeLevel = getUserFolderMinTimeLevel(foldersIds); if
             * (minTimeLevel.equals("5")) { outerBuffer.append("<li
             * class='closed' id='timeDimension-Period Basis'>");
             * outerBuffer.append("<img src='icons
             * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
             * id='elmnt-Time Dimension-Period Basis'
             * style='font-family:verdana;font-size:8pt'>Time-Period
             * Basis</span>"); outerBuffer.append("</li>");
             * outerBuffer.append("<li class='closed' id='timeDimension-Range
             * Basis'>"); outerBuffer.append("<img src='icons
             * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
             * id='elmnt-Time Dimension-Range Basis'
             * style='font-family:verdana;font-size:8pt'>Time-Range
             * Basis</span>"); outerBuffer.append("</li>"); } else if
             * (minTimeLevel.equals("4")) { outerBuffer.append("<li
             * class='closed' id='timeDimension-Week Basis'>");
             * outerBuffer.append("<img src='icons
             * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
             * id='elmnt-Time Dimension-Week Basis'
             * style='font-family:verdana;font-size:8pt'>Time-Week
             * Basis</span>"); outerBuffer.append("</li>"); } else if
             * (minTimeLevel.equals("3")) { outerBuffer.append("<li
             * class='closed' id='timeDimension-Month Basis'>");
             * outerBuffer.append("<img src='icons
             * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
             * id='elmnt-Time Dimension-Month Basis'
             * style='font-family:verdana;font-size:8pt'>Time-Month
             * Basis</span>"); outerBuffer.append("</li>");
             * outerBuffer.append("<li class='closed' id='timeDimension-Compare
             * Month Basis'>"); outerBuffer.append("<img src='icons
             * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
             * id='elmnt-Time Dimension-Compare Month Basis'
             * style='font-family:verdana;font-size:8pt'>Time-Compare Month
             * Basis</span>"); outerBuffer.append("</li>"); } else if
             * (minTimeLevel.equals("2")) { outerBuffer.append("<li
             * class='closed' id='timeDimension-Quarter Basis'>");
             * outerBuffer.append("<img src='icons
             * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
             * id='elmnt-Time Dimension-Quarter Basis'
             * style='font-family:verdana;font-size:8pt'>Time-Quarter
             * Basis</span>"); outerBuffer.append("</li>"); } else if
             * (minTimeLevel.equals("1")) { outerBuffer.append("<li
             * class='closed' id='timeDimension-Year Basis'>");
             * outerBuffer.append("<img src='icons
             * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
             * id='elmnt-Time Dimension-Year Basis'
             * style='font-family:verdana;font-size:8pt'>Time-Year
             * Basis</span>"); outerBuffer.append("</li>"); }
             */
            for (int j = 0; j < retObj1.getRowCount(); j++) {
                userId = retObj1.getFieldValueString(j, colNames1[0]);
                subFolderId = retObj1.getFieldValueString(j, colNames1[1]);
                favName = retObj1.getFieldValueString(j, colNames1[2]);

                outerBuffer.append("<li class='closed' id='" + favName + "' onmouseup=\"delfavparam('" + favName + "')\">");
                outerBuffer.append("<img src='icons pinvoke/Dimension.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;font-size:8pt'>" + favName + "</span>");
                outerBuffer.append("<ul id='dimName-" + favName + "'>");

                for (int i = 0; i < retObj.getRowCount(); i++) {
                    userId1 = retObj.getFieldValueString(i, colNames[0]);
                    subFolderId1 = retObj.getFieldValueString(i, colNames[1]);
                    favName1 = retObj.getFieldValueString(i, colNames[2]);
                    elementId = retObj.getFieldValueString(i, colNames[3]);

                    if (favName.equalsIgnoreCase(favName1)) {

                        /*
                         * if (elementId.equalsIgnoreCase("AS_OF_DATE")){
                         * outerBuffer.append("<li class='closed' id=" +
                         * elementId + ">"); outerBuffer.append("<img src='icons
                         * pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;font-size:8pt'>Date</span>");
                         * outerBuffer.append("</li>"); }else
                         * if(elementId.equalsIgnoreCase("PRG_PERIOD_TYPE")){
                         * outerBuffer.append("<li class='closed' id=" +
                         * elementId + ">"); outerBuffer.append("<img src='icons
                         * pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;font-size:8pt'>Aggregation</span>");
                         * outerBuffer.append("</li>"); }else
                         * if(elementId.equalsIgnoreCase("PRG_COMPARE")){
                         * outerBuffer.append("<li class='closed' id=" +
                         * elementId + ">"); outerBuffer.append("<img src='icons
                         * pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;font-size:8pt'>Compare
                         * With</span>"); outerBuffer.append("</li>"); }else
                         * if(elementId.equalsIgnoreCase("AS_OF_DATE1")){
                         * outerBuffer.append("<li class='closed' id=" +
                         * elementId + ">"); outerBuffer.append("<img src='icons
                         * pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;font-size:8pt'>From
                         * Date</span>"); outerBuffer.append("</li>"); }else
                         * if(elementId.equalsIgnoreCase("AS_OF_DATE2")){
                         * outerBuffer.append("<li class='closed' id='" +
                         * elementId + "'>"); outerBuffer.append("<img
                         * src='icons pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;font-size:8pt'>To
                         * Date</span>"); outerBuffer.append("</li>"); }else
                         * if(elementId.equalsIgnoreCase("CMP_AS_OF_DATE1")){
                         * outerBuffer.append("<li class='closed' id=" +
                         * elementId + ">"); outerBuffer.append("<img src='icons
                         * pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;font-size:8pt'>Compare
                         * From Date</span>"); outerBuffer.append("</li>");
                         * }else
                         * if(elementId.equalsIgnoreCase("CMP_AS_OF_DATE2")){
                         * outerBuffer.append("<li class='closed' id=" +
                         * elementId + ">"); outerBuffer.append("<img src='icons
                         * pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;font-size:8pt'>Compare To
                         * Date</span>"); outerBuffer.append("</li>"); }else
                         * if(elementId.equalsIgnoreCase("CMP_AS_OF_DATE2")){
                         * outerBuffer.append("<li class='closed' id=" +
                         * elementId + ">"); outerBuffer.append("<img src='icons
                         * pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;font-size:8pt'>Compare To
                         * Date</span>"); outerBuffer.append("</li>"); }
                         */

                        outerBuffer.append(getFavParamMbrs(subFolderId, elementId));
                    }
                }
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }       return outerBuffer.toString();
        }

    public String getFavParamMbrs(String subFolderId, String elementId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[2];
        obj[0] = subFolderId;
        obj[1] = elementId;
        String MbrId = "";
        String MbrName = "";
        String elementid = "";
        String sql = getResourceBundle().getString("getFavParamMbrs");
        StringBuffer outerBuffer = new StringBuffer("");
        try {

            finalQuery = buildQuery(sql, obj);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();


            for (int i = 0; i < retObj.getRowCount(); i++) {
                MbrId = retObj.getFieldValueString(i, colNames[2]);
                MbrName = retObj.getFieldValueString(i, colNames[1]);
                elementid = retObj.getFieldValueString(i, colNames[0]);
                outerBuffer.append("<li class='closed' id='" + MbrId + "'>");
                outerBuffer.append("<img src='icons pinvoke/hirarechy.png'></img>");
                outerBuffer.append("<span id='elmnt-" + elementid + "' style='font-family:verdana;font-size:8pt'>" + MbrName + "</span>");

                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }       return outerBuffer.toString();
        }

    public void updateDbrdComment(String elementId, String masterId, String comntText, String userId) {
        String query = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            query = "insert into prg_kpi_user_comments(USER_ID,KPI_MASTER_ID,ELEMENT_ID,KPI_COMMENT,COMMENT_DATE)"
                    + " values('&','&','&','&',getdate())";
        } else {
            query = "insert into prg_kpi_user_comments(COMMENT_ID,USER_ID,KPI_MASTER_ID,ELEMENT_ID,KPI_COMMENT,COMMENT_DATE)"
                    + " values(KPI_COMMENT_SEQ.nextval,'&','&','&','&',sysdate)";
        }

        String finalQuery = "";
        Object[] kpiComntDets = new Object[4];
        kpiComntDets[0] = userId;
        kpiComntDets[1] = masterId;
        kpiComntDets[2] = elementId;
        kpiComntDets[3] = comntText;

        try {
            finalQuery = buildQuery(query, kpiComntDets);
            execUpdateSQL(finalQuery);
        }  catch (Exception ex) {
            logger.error("Exception: ", ex);
        }    }

    public void deleteDbrdComment(String elementId, String masterId, String userId) {

        String delDbrdCommentquery = "delete from prg_kpi_user_comments where user_id=& and kpi_master_id=& and element_id=&";
        String finalQuery = "";
        Object[] delkpiComntDets = new Object[3];
        delkpiComntDets[0] = userId;
        delkpiComntDets[1] = masterId;
        delkpiComntDets[2] = elementId;

        try {
            finalQuery = buildQuery(delDbrdCommentquery, delkpiComntDets);

            execModifySQL(finalQuery);
        }catch (Exception ex) {
            logger.error("Exception: ", ex);
        }    }

    public void saveTargetForKpiTable(String elementId, String masterKpiId, double targetValue, String timeLevel, String reportId, DashletDetail dashlet) throws Exception {
        boolean newEntry = true;
        KPI kpiDetail = (KPI) dashlet.getReportDetails();
        List<KPITarget> kpiTargets = kpiDetail.getKPITargets(elementId);
        for (KPITarget target : kpiTargets) {
            if (timeLevel.equalsIgnoreCase(target.getTimeLevel())) {
                target.setTargetValue(targetValue);
                newEntry = false;
            }
        }

        if (newEntry) {
            KPITarget target = new KPITarget();
            target.setElementId(elementId);
            target.setTimeLevel(timeLevel);
            target.setTargetValue(targetValue);

            kpiDetail.addKPITarget(elementId, target);
        }

        if (kpiDetail.isPersisted()) {
            String query = "";
            if (newEntry) {  //Insert into the DB
                query = "insert into dashboard_target_kpi_value(ELEMENT_ID,KPI_MASTER_ID,TIME_LEVEL,TARGET_VALUE,DASHBOARD_ID) "
                        + "values(" + elementId + "," + masterKpiId + ",'" + timeLevel + "'," + targetValue + "," + reportId + ")";
            } else {   //Update the existing entry in the DB
                query = "update dashboard_target_kpi_value set target_value=" + targetValue + " where element_id=" + elementId
                        + " and kpi_master_id=" + masterKpiId + " and time_level='" + timeLevel + "' and dashboard_id=" + reportId;
            }

            ArrayList queries = new ArrayList();
            queries.add(query);
            try {
                executeMultiple(queries);
            } catch (Exception e) {
                logger.error("Exception: ", e);
            }
        }
    }

    public ArrayList getGrpMstrDetails(String grpId) {
        PbReturnObject GrpMstrDetsByIdObj = null;
        String finalQuery = null;
        Object obj[] = new Object[1];
        obj[0] = grpId;
        String getGrpMstrDetsByIdQry = getResourceBundle().getString("getGrpMstrDetailsById");
        ArrayList grpMstrDetsList = new ArrayList();
        try {
            finalQuery = buildQuery(getGrpMstrDetsByIdQry, obj);
            ////////.println("getGrpMstrDetsByIdQry in dao is : "+getGrpMstrDetsByIdQry);
            GrpMstrDetsByIdObj = execSelectSQL(finalQuery);
            if (GrpMstrDetsByIdObj.getRowCount() > 0) {
                for (int i = 0; i < GrpMstrDetsByIdObj.getRowCount(); i++) {
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "GRAPH_NAME"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "GRAPH_FAMILY"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueInt(i, "GRAPH_TYPE"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueInt(i, "GRAPH_ORDER"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueInt(i, "GRAPH_SIZE"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "GRAPH_HEIGHT"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "GRAPH_WIDTH"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "ALLOW_LINK"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "ALLOW_LABEL"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "ALLOW_LEGEND"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "ALLOW_TOOLTIP"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueInt(i, "GRAPH_CLASS"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "LEFT_Y_AXIS_LABEL"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "RIGHT_Y_AXIS_LABEL"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "X_AXIS_LABEL"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "FONT_NAME"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueInt(i, "FONT_SIZE"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "FONT_COLOR"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "LEGEND_LOC"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "SHOW_GRID_X_AXIS"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "SHOW_GRID_Y_AXIS"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "BACK_COLOR"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "SHOW_DATA"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueClobString(i, "ROW_VALUES"));
                    grpMstrDetsList.add(GrpMstrDetsByIdObj.getFieldValueString(i, "SHOW_GT"));
                }
            }
            ////////.println("grpMstrDetsList in dao is : "+grpMstrDetsList);

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }        return grpMstrDetsList;
        }

    public String removeLastCommas(String str) {

        //////.println("str"+str);
        if (str.length() > 0) {
            str = str.replaceAll(" ", "");
        }
        if (str.length() > 0) {
            while (str.charAt(str.length() - 1) == ',') {
                str = str.substring(0, str.length() - 1);
            }

        }

        return str;
    }

    public void editDashbdName(String dashId, String dashboardName, String dashboardDesc) {
        String updateDashbdNameQuery = getResourceBundle().getString("updateDashbdName");
        Object[] dashbdMaster = null;
        String finaldashbdQuery = "";

        try {
            dashbdMaster = new Object[4];
            dashbdMaster[0] = dashboardName;
            dashbdMaster[1] = dashboardDesc;
            dashbdMaster[2] = dashboardDesc;
            dashbdMaster[3] = dashId;
            finaldashbdQuery = buildQuery(updateDashbdNameQuery, dashbdMaster);
            //////.println("finaldashbdQuery-in DAO--"+finaldashbdQuery);
            execModifySQL(finaldashbdQuery);

        } catch (Exception ex) {
           logger.error("Exception: ", ex);
        }    }

    public String chckDashbdNameBfrUpdate(String dashDId, String gvnDashdNm, String gvnDashDesc) {
        PbDb pbdb = new PbDb();
        String finalQDashD = "";
        String status = "1";
        try {
            Object obj[] = new Object[1];
            obj[0] = dashDId;
            String chckdashbDduplicateRecd = getResourceBundle().getString("chckdashbDduplicateRecd");
            finalQDashD = buildQuery(chckdashbDduplicateRecd, obj);
            PbReturnObject alldashbdName = pbdb.execSelectSQL(finalQDashD);
            for (int m = 0; m < alldashbdName.getRowCount(); m++) {
                if (gvnDashdNm.equalsIgnoreCase(alldashbdName.getFieldValueString(m, "REPORT_NAME"))) {
                    status = "Dashboard Name Already Exists";
                }
                if (!status.equalsIgnoreCase("1")) {
                    break;
                }
            }
        }   catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }  catch (Exception ex) {
            logger.error("Exception: ", ex);
        }        return status;
        }

//    public PbReturnObject getKPIColorVals(String elementId, String kpiMasterId) {
//        PbReturnObject retObj=new PbReturnObject();
//        PbDb pbdb = new PbDb();
//        String finalKPIColorVals = "";
//        try {
//            String getKPIColorValsQry = getResourceBundle().getString("getKPIColorVals");
//            Object obj[] = new Object[2];
//            obj[0] = elementId;
//            obj[1] = kpiMasterId;
//
//            finalKPIColorVals = buildQuery(getKPIColorValsQry, obj);
//            
//            retObj = pbdb.execSelectSQL(finalKPIColorVals);
//        } catch (Exception exp) {
//            logger.error("Exception:", exp);
//        }
//        return retObj;
//    }
    public boolean purgeDashboard(String reportId) {
        ArrayList queries = new ArrayList();
        boolean result = false;
        String deleteReportGraphDetailsQuery = "delete  from PRG_AR_GRAPH_DETAILS  where graph_id in ( select graph_id from PRG_AR_GRAPH_MASTER where report_id =" + reportId + " ) ";
        String deleteReportGraphMasterQuery = "delete from PRG_AR_GRAPH_MASTER where report_id =" + reportId + " ";
        String deleteReportUserReportsQuery = "delete from PRG_AR_USER_REPORTS where report_id=" + reportId + "";
        String deleteReportQueryDetailsQuery = "delete from PRG_AR_QUERY_DETAIL where report_id =" + reportId + "";
        String deleteReportViewByDetailsQuery = "delete from PRG_AR_REPORT_VIEW_BY_DETAILS where VIEW_BY_ID in (select view_by_id from PRG_AR_REPORT_VIEW_BY_MASTER  where report_id=" + reportId + ")";
        String deleteReportViewByMasterQuery = "delete from PRG_AR_REPORT_VIEW_BY_MASTER where report_id =" + reportId + "";
        String deleteReportTimeDetailQuery = "delete from PRG_AR_REPORT_TIME_DETAIL where REP_TIME_ID in(select  REP_TIME_ID from PRG_AR_REPORT_TIME  where REPORT_ID=" + reportId + ")";
        String deleteReportTimeQuery = "delete from PRG_AR_REPORT_TIME where report_id=" + reportId + "";
        String deleteReportParamDetailsQuery = "delete from PRG_AR_REPORT_PARAM_DETAILS where report_id=" + reportId + "";
        String deleteKpiDetailsQuery = "delete from PRG_AR_KPI_DETAILS where KPI_MASTER_ID in(select KPI_MASTER_ID from PRG_AR_DASHBOARD_DETAILS where DISPLAY_TYPE='KPI' and DASHBOARD_ID=" + reportId + ")";
        String deleteDashboardDetailsQuery = "delete from PRG_AR_DASHBOARD_DETAILS where DASHBOARD_ID=" + reportId + "";
        String deleteReportDetailsQuery = "delete from PRG_AR_REPORT_DETAILS where report_id=" + reportId + "";
        String deleteReportMasterQuery = "delete from PRG_AR_REPORT_MASTER where report_id=" + reportId + "";



        try {
            queries.add(deleteReportGraphDetailsQuery);
            queries.add(deleteReportGraphMasterQuery);
            queries.add(deleteReportUserReportsQuery);
            queries.add(deleteReportQueryDetailsQuery);
            queries.add(deleteReportViewByDetailsQuery);
            queries.add(deleteReportViewByMasterQuery);
            queries.add(deleteReportTimeDetailQuery);
            queries.add(deleteReportTimeQuery);
            queries.add(deleteReportParamDetailsQuery);
            queries.add(deleteKpiDetailsQuery);
            queries.add(deleteDashboardDetailsQuery);
            queries.add(deleteReportDetailsQuery);
            queries.add(deleteReportMasterQuery);

            result = executeMultiple(queries);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

        return result;
    }

    public String getNewDashletMasterId() {
        String retVal = "";
        try {
            String query = "select prg_ar_dashlet_master_id_seq.nextval from dual";
            PbReturnObject retObj = execSelectSQL(query);
            if (retObj != null && retObj.getRowCount() > 0) {
                retVal = retObj.getFieldValueString(0, 0);
            }
        }
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }  catch (Exception ex) {
            logger.error("Exception: ", ex);
        }       return retVal;
    }

    public void deleteFromDashletDetail(String masterIds) {
        try {
            String whereCond = "(" + masterIds + ")";
            String query = "delete from prg_ar_dashlet_details where master_id in " + whereCond;
            execModifySQL(query);
        }  catch (Exception ex) {
            logger.error("Exception: ", ex);
        }    }

    public String mapToString(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String key : map.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append("\"" + key + "\":");
            if (!map.get(key).contains("[")) {
                stringBuilder.append("\"" + map.get(key) + "\"");
            } else {
                stringBuilder.append(map.get(key));
            }

        }


        return "{" + stringBuilder + "}";
    }

    public StringBuilder getKpiElementType(String kpiMasterId) {
        StringBuilder sb = new StringBuilder();
        String result = "";
        String type1 = "Standard";
        String type2 = "Non-Standard";
        String query = "select KPI_NAME , ELEMENT_ID ,KPI_ST_TYPE from PRG_AR_KPI_DETAILS  where KPI_MASTER_ID = " + kpiMasterId + " ORDER BY KPI_DETAILS_ID";
        try {

            PbReturnObject retObj = execSelectSQL(query);

            if (retObj != null && retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    sb.append("<tr>");
                    sb.append("<td>");
                    sb.append(retObj.getFieldValueString(i, 0));
                    sb.append("</td>");
                    sb.append("<td>");
                    sb.append("<select id='selectKPIType" + i + "' width='25%' name='selectKPIType" + i + "'> ");
                    if (retObj.getFieldValueString(i, "KPI_ST_TYPE").equalsIgnoreCase("Standard")) {
                        sb.append("<option  selected value='Standard~" + retObj.getFieldValueString(i, 1) + "'>");
                        sb.append(type1);
                        sb.append("</option >");
                        sb.append("<option value='Non-Standard~" + retObj.getFieldValueString(i, 1) + "'>");
                        sb.append(type2);
                        sb.append("</option >");
                    } else {
                        sb.append("<option selected value='Non-Standard~" + retObj.getFieldValueString(i, 1) + "'>");
                        sb.append(type2);
                        sb.append("</option >");
                        sb.append("<option   value='Standard~" + retObj.getFieldValueString(i, 1) + "'>");
                        sb.append(type1);
                        sb.append("</option >");
                    }

                    sb.append("</td>");
                    sb.append("</tr>");

                }


            }

        }catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return sb;
    }

    public boolean saveKpiTypes(String[] kpitypes, String kpiMasterId) {
        String updateKpiTypes = getResourceBundle().getString("updateKpiTypes");
        String finalQuery = "";
        ArrayList<String> queryList = new ArrayList<String>();
        Object object[] = new Object[3];
        for (String str : kpitypes) {
            String tempStr[] = str.split("~");
            object[0] = tempStr[0];
            object[1] = kpiMasterId;
            object[2] = tempStr[1];
            queryList.add(super.buildQuery(updateKpiTypes, object));
        }

        return super.executeMultiple(queryList);

    }

    public String getKpiElementNewName(String kpiMasterId) {
        StringBuilder sb = new StringBuilder();
        HashMap<String, String> tempHashMap = new HashMap<String, String>();
        ArrayList<String> elementIDList = new ArrayList<String>();
        String query = "select KPI_NAME , ELEMENT_ID from PRG_AR_KPI_DETAILS  where KPI_MASTER_ID = " + kpiMasterId + " ORDER BY KPI_DETAILS_ID";
        try {
            PbReturnObject retObj = execSelectSQL(query);
            if (retObj != null && retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    if (retObj.getFieldValueString(i, "ELEMENT_ID").equalsIgnoreCase("") || retObj.getFieldValueString(i, "ELEMENT_ID").equalsIgnoreCase("0")) {
                        elementIDList.add(retObj.getFieldValueString(i, 0));
                    } else {
                        elementIDList.add(retObj.getFieldValueString(i, "ELEMENT_ID"));
                    }
                    sb.append("<tr>");
                    sb.append("<td>");
                    sb.append(retObj.getFieldValueString(i, 0));
                    sb.append("</td>");
                    sb.append("<td>");
                    if (retObj.getFieldValueString(i, "ELEMENT_ID").equalsIgnoreCase("") || retObj.getFieldValueString(i, "ELEMENT_ID").equalsIgnoreCase("0")) {
                        sb.append("<input type='text' id='KpiRename" + retObj.getFieldValueString(i, 0) + "' value='" + retObj.getFieldValueString(i, 0) + "' name='KpiRename" + retObj.getFieldValueString(i, 0) + "'>");
                    } else {
                        sb.append("<input type='text' id='KpiRename" + retObj.getFieldValueString(i, "ELEMENT_ID") + "' value='" + retObj.getFieldValueString(i, 0) + "' name='KpiRename" + retObj.getFieldValueString(i, "ELEMENT_ID") + "'>");
                    }
                    sb.append("</td>");
                    sb.append("</tr>");

                }
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        tempHashMap.put("htmlVar", sb.toString());
        tempHashMap.put("ElementIds", "[\"" + Joiner.on("\",\"").join(elementIDList) + "\"]");
        return mapToString(tempHashMap);
    }

    public int saveDashletNewName(String kpiMasterId, String newDashletName) throws Exception {

        String saveDashletName = getResourceBundle().getString("saveNewDashletName");
        String finalQuery = "";
        Object obj[] = new Object[2];
        obj[0] = newDashletName;
        obj[1] = kpiMasterId;
        int i = super.execUpdateSQL(super.buildQuery(saveDashletName, obj));
        return super.execUpdateSQL(super.buildQuery(saveDashletName, obj));

    }

    public int saveTableNewName(String refRepId, String tableName) throws Exception {

        String saveTableName = getResourceBundle().getString("saveNewTableName");
        String finalQuery = "";
        Object obj[] = new Object[2];
        obj[0] = tableName;
        obj[1] = refRepId;
        int i = super.execUpdateSQL(super.buildQuery(saveTableName, obj));
        return super.execUpdateSQL(super.buildQuery(saveTableName, obj));

    }

    public static void main(String[] args) {
        DashboardTemplateDAO DAO = new DashboardTemplateDAO();
        String str = DAO.getGraphReportsByBuzRoles("221");


    }

    public boolean saveNewKPINames(String kpiMasterId, String[] newKPINamesArray) {
        String saveNewKPINames = getResourceBundle().getString("saveNewKPINames");
        String finalQuery = "";
        ArrayList<String> queryList = new ArrayList<String>();
        Object object[] = new Object[3];
        for (String str : newKPINamesArray) {
            String tempStr[] = str.split("~");
            object[0] = tempStr[1];
            object[1] = kpiMasterId;
            object[2] = tempStr[0];
            queryList.add(super.buildQuery(saveNewKPINames, object));
        }

        return super.executeMultiple(queryList);
    }

    public int saveKpiSymbol(String kpiSymbol, String kpiMasterId) throws Exception {


        String saveKpiSymbol = getResourceBundle().getString("saveSymbol");
        String finalQuery = "";
        Object[] obj = new Object[2];
        obj[0] = kpiSymbol;
        obj[1] = kpiMasterId;


        return super.execUpdateSQL(super.buildQuery(saveKpiSymbol, obj));
    }

    public int resetTableData(String dashletId) throws Exception {

        String resetTableData = getResourceBundle().getString("resetTableData");
        String finalQuery = "";
        Object[] obj = new Object[2];
        obj[0] = "";
        obj[1] = dashletId;
        return super.execUpdateSQL(super.buildQuery(resetTableData, obj));
    }

    public StringBuilder getKpiDrillToAnyReport(String elementId, String foldersIds, String elementname) {
        StringBuilder sb = new StringBuilder();
        String getKpiDrillReportsQuery = getResourceBundle().getString("getKpiCustomReports");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] kpiReps = new Object[2];
        kpiReps[0] = foldersIds;
//        kpiReps[0] = elementId;
        kpiReps[1] = foldersIds;
        String[] columnNames = null;
        try {
            finalQuery = buildQuery(getKpiDrillReportsQuery, kpiReps);
//            
            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
            retObj = null;
        }catch (Exception ex) {
            logger.error("Exception: ", ex);
            retObj = null;
        }        columnNames = retObj.getColumnNames();
        sb.append("<tr>");
        sb.append("<td class='myHead'>");
        sb.append(elementname);
        sb.append("</td>");
        sb.append("<td>");
        sb.append("<select id='selectReport" + elementId.replace(",", "_") + "' width='45%' name='selectReport'> ");
        for (int i = 0; i < retObj.getRowCount(); i++) {
            sb.append("<option value='" + retObj.getFieldValueString(i, columnNames[0]) + "'>");
            sb.append(retObj.getFieldValueString(i, columnNames[1]));
            sb.append("</option>");
        }
        sb.append("</select>");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("~" + elementId.replace(",", "_"));
        return sb;
    }
//    public ReportSchedule getKpiSchedulerDetails(String elementId,String kpiMasterId,String dashletId,String reportId){
//
//        ReportSchedule schedule = null;
//        String query = getResourceBundle().getString("getKpiSchedulerDetails");
//        return schedule;
//
//    }

    public float getDeviation(String actulaValue, String targetValue) {

        Double deviation = (((double) ((Double.parseDouble(actulaValue) - Double.parseDouble(targetValue))) / Double.parseDouble(targetValue)) * 100);

        double tempDev = deviation;

        deviation = Math.abs(deviation);

        double d = tempDev;//deviation[i];
        float p = (float) Math.pow(10, 2);
        double Rval2 = 0;
        Rval2 = d * p;
        float tmp = Math.round(Rval2);
        float finalDev = tmp / p;

        return finalDev;
    }

    public String getComplexKPI() {
        String sqlQuery = getResourceBundle().getString("getCreateKPIData");
        PbReturnObject retObj = new PbReturnObject();
        StringBuilder outerBuffer = new StringBuilder();
        try {
            retObj = execSelectSQL(sqlQuery);
            if (retObj.getRowCount() > 0) {
//            outerBuffer.append("<li class='closed' id='complexKPI'>");
//                outerBuffer.append("<img src='icons pinvoke/table.png'></img>");
//                outerBuffer.append("<span style='font-family:verdana;font-size:8pt'>COMPLEX KPI</span>");
//                outerBuffer.append("<ul id='factName-complexKPI'>");
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    outerBuffer.append("<li>");
                    outerBuffer.append("<img src='icons pinvoke/report.png'></img>");
                    outerBuffer.append("<span  id='" + retObj.getFieldValueString(i, "CREATE_KPI_ID") + "' class='myDragTabs ui-draggable'>" + retObj.getFieldValueString(i, "CREATE_KPI_NAME") + "</span></li>");
                    //outerBuffer.append(retObj.getFieldValueString(i, "CREATE_KPI_NAME"));
                }

//                outerBuffer.append("</ul>");
//                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception ex) {
            logger.error("Exception: ", ex);
        }       return outerBuffer.toString();
        }

    public PbReturnObject getCreateKPIReturnObject(CreateKPIFromReport kPIFromReport, String dashboardid, HttpServletRequest request) {

        PbReturnObject returnObject = new PbReturnObject();
//        Container container = Container.getContainerFromSession(request, dashboardid);
        HashMap map = null;
        HttpSession session = request.getSession(false);
        map = (HashMap) session.getAttribute("PROGENTABLES");
        Container container = (Container) map.get(dashboardid);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject newReturnObj = new PbReturnObject();
        PbReportTableBD reportTableBD = new PbReportTableBD();
        ArrayList rowViewBys = new ArrayList(Arrays.asList(kPIFromReport.getRowViewBys()));
        ArrayList reportQryCols = new ArrayList(Arrays.asList(kPIFromReport.getReportQryCols()));
        ArrayList qryAggregations = new ArrayList(Arrays.asList(kPIFromReport.getQryAggregations()));
        ArrayList timeDetailsArray = collect.timeDetailsArray;

//        rowViewBys.add(kPIFromReport.getRowViewBys());
//        reportQryCols.addAll(kPIFromReport.getReportQryCols());
//        qryAggregations.add(kPIFromReport.getQryAggregations());
//        timeDetailsArray.add(kPIFromReport.getTimeDetailsArray());
        ArrayList sortColumns = null;
        if (kPIFromReport.getSortColumns() != null) {
            sortColumns = new ArrayList(Arrays.asList(kPIFromReport.getSortColumns()));
        }
        //sortColumns.add(kPIFromReport.getSortColumns());
        ArrayList dataSeq = null;
        repQuery.setRowViewbyCols(rowViewBys);
        repQuery.setParamValue((HashMap) kPIFromReport.getReportParametersValues());//Added by k
        repQuery.setColViewbyCols(new ArrayList());
        repQuery.setQryColumns(reportQryCols);
        repQuery.setColAggration(qryAggregations);
        repQuery.setTimeDetails(timeDetailsArray);
        //repQuery.setDefaultMeasure(String.valueOf(kPIFromReport.getMeasureId()));
        //repQuery.setDefaultMeasureSumm(String.valueOf(kPIFromReport.getQryAggregations()));
//                repQuery.isKpi = true;
        //repQuery.setReportId(kPIFromReport.getReportId());
        //repQuery.setBizRoles(kPIFromReport.getBizRoles()[0]);
        //repQuery.setUserId(kPIFromReport.getUserId());
        returnObject = repQuery.getPbReturnObject(String.valueOf(reportQryCols.get(0)));
        returnObject.resetViewSequence();
        reportTableBD.searchDataSet(kPIFromReport, returnObject);
        if (kPIFromReport.isTopBottomSet()) {
            if (kPIFromReport.getTopBtmMode().equalsIgnoreCase("TopBottomAbsRows")) {
                dataSeq = returnObject.findTopBottom(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
            } else {
                dataSeq = returnObject.findTopBottomPercentWise(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
            }
            returnObject.setViewSequence(dataSeq);
        }

        returnObject.setProcessGT(true);
        returnObject.prepareObject(returnObject, dataSeq);
        return returnObject;
    }

    public GenerateDragAndDrophtml getCreateKPIValues(HttpServletRequest request) {
        String sqlQuery = getResourceBundle().getString("getCreateKPIData");
        PbReturnObject retObj = new PbReturnObject();
        StringBuilder outerBuffer = new StringBuilder();
        GenerateDragAndDrophtml dragAndDrophtml = null;
        ArrayList<String> droppableList = new ArrayList<String>();
        ArrayList<String> draggableList = new ArrayList<String>();
        ArrayList<String> droppableListnames = new ArrayList<String>();
        ArrayList<String> draggableListNames = new ArrayList<String>();
        try {
            retObj = execSelectSQL(sqlQuery);
            if (retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    draggableList.add(retObj.getFieldValueString(i, "CREATE_KPI_ID"));
                    draggableListNames.add(retObj.getFieldValueString(i, "CREATE_KPI_NAME"));
                }
                dragAndDrophtml = new GenerateDragAndDrophtml("Select Complex KPI from below", "Drag columns to here", droppableList, draggableList, request.getContextPath());
                dragAndDrophtml.setDragableListNames(draggableListNames);
            }
        }catch (SQLException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }       return dragAndDrophtml;
        }

    public String getBuildCreateKPI(String[] kpisArray, HttpServletRequest request, String dashboardid) {
        StringBuilder createKPIBuilder = new StringBuilder();
        String sqlQuery = getResourceBundle().getString("getCreateKPIDetails");
        Gson gson = new Gson();
        String[] kpiName = new String[kpisArray.length];
        String[] kpiId = new String[kpisArray.length];
        String[] kpiValue = new String[kpisArray.length];
        String[] aggType = new String[kpisArray.length];
        String ReportKpiId = "";
        Object[] obj = new Object[1];
        PbReturnObject retObj = new PbReturnObject();
        PbReturnObject kpiRetObj = null;
        CreateKPIFromReport kPIFromReport = new CreateKPIFromReport();
        String createKPIString = null;
        ArrayList<CreateKPIFromReport> createKPIList = new ArrayList<CreateKPIFromReport>();
        for (int i = 0; i < kpisArray.length; i++) {
            obj[0] = kpisArray[i];
            try {
                retObj = execSelectSQL(buildQuery(sqlQuery, obj));
                if (retObj.getRowCount() > 0) {
                    Type listType = new TypeToken<CreateKPIFromReport>() {
                    }.getType();
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        createKPIString = retObj.getFieldUnknown(0, 0);
                    } else {
                        createKPIString = retObj.getFieldValueClobString(0, "CREATE_KPI_DATA");
                    }
                    kPIFromReport = gson.fromJson(createKPIString, CreateKPIFromReport.class);
                    createKPIList.add(kPIFromReport);
                    kpiName[i] = kPIFromReport.getReportKPIName();
                    ReportKpiId = kPIFromReport.getMeasureId();
                    kpiRetObj = getCreateKPIReturnObject(kPIFromReport, dashboardid, request);
                    if (kPIFromReport.getAggType().equalsIgnoreCase("avg")) {
                        kpiValue[i] = NumberFormatter.getModifiedNumber(kpiRetObj.getColumnAverageValue(kPIFromReport.getMeasureId()), "");
                        // kpiValue[i]=String.valueOf(kpiRetObj.getColumnAverageValue(kPIFromReport.getMeasureId()));

                    } else if (kPIFromReport.getAggType().equalsIgnoreCase("sum")) {

                        kpiValue[i] = NumberFormatter.getModifiedNumber(kpiRetObj.getColumnGrandTotalValue(kPIFromReport.getMeasureId()), "");

                    } else if (kPIFromReport.getAggType().equalsIgnoreCase("min")) {

                        kpiValue[i] = NumberFormatter.getModifiedNumber(kpiRetObj.getColumnMinimumValue(kPIFromReport.getMeasureId()), "");
                    } else if (kPIFromReport.getAggType().equalsIgnoreCase("max")) {
                        kpiValue[i] = NumberFormatter.getModifiedNumber(kpiRetObj.getColumnMaximumValue(kPIFromReport.getMeasureId()), "");

                    } else {
                        kpiValue[i] = String.valueOf(kpiRetObj.getViewSequence().size());
                    }
                }
            } catch (SQLException ex) {
                logger.error("Exception", ex);
            } catch (Exception ex) {
                logger.error("Exception", ex);
            }
        }
        String dashletId = request.getParameter("divId");
        String dashboardId = request.getParameter("dashboardId");
        HttpSession session = request.getSession(false);
        HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
        Container container = (Container) map.get(dashboardId);
        //
        if (container != null) {
            pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
            DashletDetail dashlet = collect.getDashletDetail(dashletId);
            if (dashlet != null) {
                dashlet.setDashBoardDetailId(dashletId);
                dashlet.setKpiType("Complexkpi");
                dashlet.setDisplayType(DashboardConstants.KPI_REPORT);
                KPI kpidetails = new KPI();

                List<String> SelectedkpiIds = Arrays.asList(kpisArray);
                kpidetails.setElementIds(SelectedkpiIds);
                dashlet.setReportDetails(kpidetails);
                for (int i = 0; i < kpiName.length; i++) {
                    kpidetails.addelementNames(kpiId[i], kpiName[i]);
                }

            }
        }

        KPIBuilder kpibuilder = new KPIBuilder();
//        boolean testComplexKpis = Boolean.parseBoolean(request.getAttribute("oneviewTest").toString());
        String complexkpi = kpibuilder.processComplexKpi(kpiName, kpiValue, kpisArray);
        return complexkpi;

    }

    public PbReturnObject getGroupKpiDrillReports(String groupName, String foldersIds) {
        String getKpiDrillReportsQuery = getResourceBundle().getString("getKpiCustomReports");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] kpiReps = new Object[2];
        kpiReps[0] = foldersIds.replace(",", "").trim();
        kpiReps[1] = foldersIds.replace(",", "").trim();
        try {
            finalQuery = buildQuery(getKpiDrillReportsQuery, kpiReps);

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
            retObj = null;
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
            retObj = null;
        }
        return retObj;

    }

    public Boolean UpdateGraphNameDAO(String dashboard, String graphId, String NewGraphName) {
        Boolean update = false;
        String updateQuery = getResourceBundle().getString("updateGraphName");
        int i = 0;
        Object[] graphs = new Object[3];
        graphs[0] = NewGraphName;
        graphs[1] = graphId;
        graphs[2] = dashboard;
        String finalQuery = "";
        PbReturnObject retObj = null;
        try {
            finalQuery = buildQuery(updateQuery, graphs);
            i = execUpdateSQL(finalQuery);
        }  catch (Exception ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        }        return update;
        }

    public String getRelatedGraphs(String folderId) {
        String val = "";
        String relatedgraphsQuery = getResourceBundle().getString("getRelatedGraphs");
        Object[] graphs = new Object[2];
//            graphs[0] = elemntIds;
        graphs[0] = folderId;
        String finalQuery = "";
        PbReturnObject retObj = null;
        try {
            finalQuery = buildQuery(relatedgraphsQuery, graphs);
            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
            retObj = null;
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
            retObj = null;
        }
        return val;
    }

    public int updatekpiNewheads(String dbrdId, String dashletId, String kpiMasterId, String newKpiheads) {
        String updateQuery = getResourceBundle().getString("updateKpiHeads");
        int i = 0;
        Object[] kpidets = new Object[4];
        kpidets[0] = newKpiheads;
        kpidets[1] = dashletId;
        kpidets[2] = kpiMasterId;
        kpidets[3] = dbrdId;
        String finalQuery = "";
        PbReturnObject retObj = null;
        try {
            finalQuery = buildQuery(updateQuery, kpidets);
            i = execUpdateSQL(finalQuery);
        }catch (Exception ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        }        return i;
        }

    public String getUserName(String userId) {
        String getUserName = getResourceBundle().getString("getUserName");
        String userName = "";
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object obj[] = new Object[2];
        obj[0] = userId;
        try {
            finalQuery = buildQuery(getUserName, obj);
            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        }catch (Exception ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        }
        userName = retObj.getFieldValueString(0, "PU_LOGIN_ID");
        return userName;
    }

    public String getAuthorizedUserId(String elementId, String masterId, String dashletId) {
        String UserId = "";
        String getUserId = getResourceBundle().getString("getCommentedUserId");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object obj[] = new Object[3];
        obj[0] = masterId;
        obj[1] = elementId;
        try {
            finalQuery = buildQuery(getUserId, obj);
            retObj = execSelectSQL(finalQuery);
        }catch (SQLException ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        }catch (Exception ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        }
        //by gopesh
        UserId = retObj.getFieldValueString(0, 0);
        return UserId;
    }

    public ArrayList insertReportTimeDimensions(ArrayList timeDetails, HashMap timeDimHashMap, int reportId, ArrayList queries, ArrayList timeParams, String date, LinkedHashMap currentTimeDetails) {
        String sqlQuery = getResourceBundle().getString("insertReportTimeDimensions");
        String finalQuery = "";
        Object[] obj = null;
        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
            obj = new Object[2];
            obj[0] = timeDetails.get(1);
            obj[1] = timeDetails.get(0);
        } else {
            obj = new Object[3];
            obj[0] = timeDetails.get(1);
            obj[1] = timeDetails.get(0);
            obj[2] = reportId;
        }

        finalQuery = buildQuery(sqlQuery, obj);
        queries.add(finalQuery);
        if (timeDetails.get(1).equals("PRG_COHORT")) {
            queries = insertReportTimeDimensionsDetails(timeDimHashMap, queries, timeParams, null, timeDetails.get(1).toString(), date, currentTimeDetails);
        } else {
            queries = insertReportTimeDimensionsDetails(timeDimHashMap, queries, timeParams, timeDetails.get(3).toString(), timeDetails.get(1).toString(), date, currentTimeDetails);
        }
        return queries;
    }
    //added by dinanath for dashboard overwriting

    public ArrayList updateReportTimeDimensions(ArrayList timeDetails, HashMap timeDimHashMap, int reportId, ArrayList queries, ArrayList timeParams, String date, LinkedHashMap currentTimeDetails, int value) {
        //update PRG_AR_REPORT_TIME set TIME_TYPE='&',TIME_LEVEL='&' where REPORT_ID='&'
        String sqlQuery = getResourceBundle().getString("updateReportTimeDimensions");
        String finalQuery = "";
        Object[] obj = null;
        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
            obj = new Object[3];
            obj[0] = timeDetails.get(1);
            obj[1] = timeDetails.get(0);
            obj[2] = reportId;
        } else {
            obj = new Object[3];
            obj[0] = timeDetails.get(1);
            obj[1] = timeDetails.get(0);
            obj[2] = reportId;
        }

        finalQuery = buildQuery(sqlQuery, obj);
        queries.add(finalQuery);
        if (timeDetails.get(1).equals("PRG_COHORT")) {
            queries = updateReportTimeDimensionsDetails(timeDimHashMap, queries, timeParams, null, timeDetails.get(1).toString(), date, currentTimeDetails, value);
        } else {
            queries = updateReportTimeDimensionsDetails(timeDimHashMap, queries, timeParams, timeDetails.get(3).toString(), timeDetails.get(1).toString(), date, currentTimeDetails, value);
        }
        return queries;
    }

    public ArrayList insertReportTimeDimensionsDetails(HashMap timeDimHashMap, ArrayList queries, ArrayList timeParamsS, String duration, String timeType, String date, LinkedHashMap currentTimeDetails) {

        String sqlQuery = getResourceBundle().getString("insertReportTimeDimensionsDetails");
        String finalQuery = "";
        String column_type = "";
        String date1 = date;
        String date11 = date;
        String[] dateArray = null;
        if (date1 != null && date1.contains("@")) {
            dateArray = date1.split("@");
        }
        String currDetails = null;
        if (date11 != null && date11.contains("currdetails")) {
            currDetails = "currdetails";
        }
        int i = 0;


        Set details = timeDimHashMap.keySet();
        Iterator it = details.iterator();
        while (it.hasNext()) {
            if (dateArray != null && i < dateArray.length) {
                date11 = dateArray[i];
            }
            Object[] obj = new Object[6];
            String key = (String) it.next();
            ArrayList timeDetails = (ArrayList) timeDimHashMap.get(key);
            String column_name = (String) timeDetails.get(2);
            column_type = key;

            /*
             * for (int time = 0; time < timeParams.size(); time++) { if
             * (timeParams.get(time).toString().equalsIgnoreCase(key)) {
             * column_type = key;//String.valueOf(timeParams.get(time)); } }
             */
            int sequence = Integer.parseInt((String) timeDetails.get(3));
            int from_sequence = Integer.parseInt((String) timeDetails.get(4));
            // date = (Date) repDateFormat.parse(timeDetails.get(0).toString());
            if (key.equalsIgnoreCase("AS_OF_DATE")) {
                if (date != null) {
                    if (timeDetails.get(0) != null && date11 != null && date11.equals("reportDate")) {
                        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                            date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                            date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                        } else {
                            date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                        }

                    } else if (timeDetails.get(0) != null && date11 != null && date11.equals("yestrday")) {
//                    date = "YesterDay";
                        date = date11;
                    } else if (timeDetails.get(0) != null && date11 != null && date11.equals("systemDate")) {
                        date = date11;
                    } else if (timeDetails.get(0) != null && date11 != null && date11.equals("tomorow")) {
                        date = date11;
                    } else if (timeDetails.get(0) != null && date11 != null && date11.contains("newSysDate")) {
                        if (date11.contains(" ")) {
                            date = date11.replace(" ", "+");
                        } else {
                            date = date11;
                        }
                        date = date;
                    } else if (timeDetails.get(0) != null && date11 != null && date11.contains("globalDate")) {
                        if (date11.contains(" ")) {
                            date = date11.replace(" ", "+");
                        } else {
                            date = date11;
                        }
                        date = date;
                    } else if (timeDetails.get(0) != null && date11 != null && date11.contains("currdetails")) {
                        String[] currVal = currentTimeDetails.get(key).toString().split("#");
                        if (currVal != null && currVal[1].equalsIgnoreCase("null")) {
                            date11 = currVal[0];
                            date = "null";
                        } else {
                            if (timeDetails.get(0) != null) {
                                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                                    date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                                } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                                    date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                                } else {
                                    date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                                }
                            }
                        }
                    } else {
                        date = "null";
                    }
                } else {
                    if (timeDetails.get(0) != null) {
                        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                            date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                            date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                        } else {
                            date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                        }

                    } else {
                        date = "null";
                    }
                }
            } else if (key.equalsIgnoreCase("AS_OF_DATE1")) {
                if (timeDetails.get(0) != null && date11 != null && date11.equals("fromyestrday")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.equals("fromtomorow")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("fromSysDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("fromglobalDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && currDetails != null && currDetails.contains("currdetails")) {
                    String[] currVal = currentTimeDetails.get(key).toString().split("#");
                    if (currVal != null && currVal[1].equalsIgnoreCase("null")) {
                        date11 = currVal[0];
                        date = "null";
                    } else {
                        if (timeDetails.get(0) != null) {
                            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                                date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                            } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                                date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                            } else {
                                date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                            }
                        }
                    }
                } else if (timeDetails.get(0) != null) {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                    } else {
                        date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                    }

                } else {
                    date = "null";
                }
            } else if (key.equalsIgnoreCase("AS_OF_DATE2")) {
                if (timeDetails.get(0) != null && date11 != null && date11.equals("toyestrday")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.equals("totomorow")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("toSystDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("toglobalDdate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && currDetails != null && currDetails.contains("currdetails")) {
                    String[] currVal = currentTimeDetails.get(key).toString().split("#");
                    if (currVal != null && currVal[1].equalsIgnoreCase("null")) {
                        date11 = currVal[0];
                        date = "null";
                    } else {
                        if (timeDetails.get(0) != null) {
                            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                                date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                            } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                                date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                            } else {
                                date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                            }
                        }
                    }
                } else if (timeDetails.get(0) != null) {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                    } else {
                        date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                    }

                } else {
                    date = "null";
                }
            } else if (key.equalsIgnoreCase("CMP_AS_OF_DATE1")) {
                if (timeDetails.get(0) != null && date11 != null && date11.contains("CmpFrmyestrday")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("CmpFrmtomorow")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("CmpFrmSysDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("CmpFrmglobalDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && currDetails != null && currDetails.contains("currdetails")) {
                    String[] currVal = currentTimeDetails.get(key).toString().split("#");
                    if (currVal != null && currVal[1].equalsIgnoreCase("null")) {
                        date11 = currVal[0];
                        date = "null";
                    } else {
                        if (timeDetails.get(0) != null) {
                            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                                date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                            } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                                date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                            } else {
                                date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                            }
                        }
                    }
                } else if (timeDetails.get(0) != null) {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                    } else {
                        date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                    }

                } else {
                    date = "null";
                }
            } else if (key.equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                if (timeDetails.get(0) != null && date11 != null && date11.contains("cmptoyestrday")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("cmptotomorow")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("cmptoSysDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("cmptoglobalDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && currDetails != null && currDetails.contains("currdetails")) {
                    String[] currVal = currentTimeDetails.get(key).toString().split("#");
                    if (currVal != null && currVal[1].equalsIgnoreCase("null")) {
                        date11 = currVal[0];
                        date = "null";
                    } else {
                        if (timeDetails.get(0) != null) {
                            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                                date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                            } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                                date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                            } else {
                                date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                            }
                        }
                    }
                } else if (timeDetails.get(0) != null) {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                    } else {
                        date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                    }

                } else {
                    date = "null";
                }
            } else {
                date = "null";
            }
            obj[0] = column_name;
            obj[1] = column_type;
            obj[2] = sequence;
            obj[3] = from_sequence;
            obj[4] = date;
            if (timeType.equalsIgnoreCase("PRG_COHORT")) {
                if (column_type.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    obj[5] = "Month";
                } else {
                    obj[5] = null;
                }
            } else {
                if (column_type.equalsIgnoreCase("PRG_COMPARE")) {
                    String comparewith = timeDetails.get(0).toString();
                    if (comparewith.equalsIgnoreCase("Last Period") || comparewith.equalsIgnoreCase("Last Year") || comparewith.equalsIgnoreCase("Period Complete") || comparewith.equalsIgnoreCase("Year Complete")) {
                        obj[5] = timeDetails.get(0);
                    } else {
                        obj[5] = duration;
                    }
                }
            }
            if (key.equalsIgnoreCase("AS_OF_DATE")) {
                if (date11 != null && date11.equalsIgnoreCase("yestrday") || date11 != null && date11.equalsIgnoreCase("Yesterday")) {
                    obj[4] = "null";
                    obj[5] = "Yesterday";
                } else if (date11 != null && date11.equalsIgnoreCase("tomorow") || date11 != null && date11.equalsIgnoreCase("Tomorrow")) {
                    obj[4] = "null";
                    obj[5] = "Tomorrow";
                } else if (date11 != null && date11.equalsIgnoreCase("systemDate")) {
                    obj[4] = "null";
                    obj[5] = "systemDate";
                } else if (date11 != null && (date11.contains("newSysDate") || date11.contains("globalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                    obj[4] = "null";
                    obj[5] = date11;
                } else if (date11 != null && date11.contains("newSysDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                } else if (date11 != null && date11.contains("globalDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                }
            } else if (key.equalsIgnoreCase("AS_OF_DATE1")) {
                if (date11 != null && date11.equalsIgnoreCase("fromyestrday")) {
                    obj[4] = "null";
                    obj[5] = "fromyestrday";
                } else if (date11 != null && date11.equalsIgnoreCase("fromtomorow")) {
                    obj[4] = "null";
                    obj[5] = "fromtomorow";
                } else if (date11 != null && (date11.contains("fromSysDate") || date11.contains("fromglobalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                    obj[4] = "null";
                    obj[5] = date11;
                } else if (date11 != null && date11.contains("fromSysDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                } else if (date11 != null && date11.contains("fromglobalDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                }
            } else if (key.equalsIgnoreCase("AS_OF_DATE2")) {
                if (date11 != null && date11.equalsIgnoreCase("toyestrday")) {
                    obj[4] = "null";
                    obj[5] = "toyestrday";
                } else if (date11 != null && date11.equalsIgnoreCase("totomorow")) {
                    obj[4] = "null";
                    obj[5] = "totomorow";
                } else if (date11 != null && (date11.contains("toSystDate") || date11.contains("toglobalDdate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                    obj[4] = "null";
                    obj[5] = date11;
                } else if (date11 != null && date11.contains("toSystDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                } else if (date11 != null && date11.contains("toglobalDdate")) {
                    obj[4] = "null";
                    obj[5] = date;
                }
            } else if (key.equalsIgnoreCase("CMP_AS_OF_DATE1")) {
                if (date11 != null && date11.equalsIgnoreCase("CmpFrmyestrday")) {
                    obj[4] = "null";
                    obj[5] = "CmpFrmyestrday";
                } else if (date11 != null && date11.equalsIgnoreCase("CmpFrmtomorow")) {
                    obj[4] = "null";
                    obj[5] = "CmpFrmtomorow";
                } else if (date11 != null && (date11.contains("CmpFrmSysDate") || date11.contains("CmpFrmglobalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                    obj[4] = "null";
                    obj[5] = date11;
                } else if (date11 != null && date11.contains("CmpFrmSysDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                } else if (date11 != null && date11.contains("CmpFrmglobalDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                }
            } else if (key.equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                if (date11 != null && date11.equalsIgnoreCase("cmptoyestrday")) {
                    obj[4] = "null";
                    obj[5] = "cmptoyestrday";
                } else if (date11 != null && date11.equalsIgnoreCase("cmptotomorow")) {
                    obj[4] = "null";
                    obj[5] = "cmptotomorow";
                } else if (date11 != null && (date11.contains("cmptoSysDate") || date11.contains("cmptoglobalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                    obj[4] = "null";
                    obj[5] = date11;
                } else if (date11 != null && date11.contains("cmptoSysDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                } else if (date11 != null && date11.contains("cmptoglobalDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                }
            }
            finalQuery = buildQuery(sqlQuery, obj);

            queries.add(finalQuery);

            timeDetails = null;
            obj = null;
            i++;
        }
        return queries;
    }
    //added by dinanath for dashboard overwriting

    public ArrayList updateReportTimeDimensionsDetails(HashMap timeDimHashMap, ArrayList queries, ArrayList timeParamsS, String duration, String timeType, String date, LinkedHashMap currentTimeDetails, int repTimeId) {
//update PRG_AR_REPORT_TIME_DETAIL set COLUMN_NAME='&',COLUMN_TYPE='&',SEQUENCE='&',FORM_SEQUENCE='&',DEFAULT_DATE='&',DEFAULT_VALUE='&'  where REP_TIME_ID='&' and SEQUENCE='&'
        String sqlQuery = getResourceBundle().getString("updateReportTimeDimensionsDetails");
        String finalQuery = "";
        String column_type = "";
        String date1 = date;
        String date11 = date;
        String[] dateArray = null;
        if (date1 != null && date1.contains("@")) {
            dateArray = date1.split("@");
        }
        String currDetails = null;
        if (date11 != null && date11.contains("currdetails")) {
            currDetails = "currdetails";
        }
        int i = 0;


        Set details = timeDimHashMap.keySet();
        Iterator it = details.iterator();
        while (it.hasNext()) {
            if (dateArray != null && i < dateArray.length) {
                date11 = dateArray[i];
            }
            Object[] obj = new Object[8];
            String key = (String) it.next();
            ArrayList timeDetails = (ArrayList) timeDimHashMap.get(key);
            String column_name = (String) timeDetails.get(2);
            column_type = key;

            /*
             * for (int time = 0; time < timeParams.size(); time++) { if
             * (timeParams.get(time).toString().equalsIgnoreCase(key)) {
             * column_type = key;//String.valueOf(timeParams.get(time)); } }
             */
            int sequence = Integer.parseInt((String) timeDetails.get(3));
            int from_sequence = Integer.parseInt((String) timeDetails.get(4));
            // date = (Date) repDateFormat.parse(timeDetails.get(0).toString());
            if (key.equalsIgnoreCase("AS_OF_DATE")) {
                if (date != null) {
                    if (timeDetails.get(0) != null && date11 != null && date11.equals("reportDate")) {
                        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                            date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                            date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                        } else {
                            date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                        }

                    } else if (timeDetails.get(0) != null && date11 != null && date11.equals("yestrday")) {
//                    date = "YesterDay";
                        date = date11;
                    } else if (timeDetails.get(0) != null && date11 != null && date11.equals("systemDate")) {
                        date = date11;
                    } else if (timeDetails.get(0) != null && date11 != null && date11.equals("tomorow")) {
                        date = date11;
                    } else if (timeDetails.get(0) != null && date11 != null && date11.contains("newSysDate")) {
                        if (date11.contains(" ")) {
                            date = date11.replace(" ", "+");
                        } else {
                            date = date11;
                        }
                        date = date;
                    } else if (timeDetails.get(0) != null && date11 != null && date11.contains("globalDate")) {
                        if (date11.contains(" ")) {
                            date = date11.replace(" ", "+");
                        } else {
                            date = date11;
                        }
                        date = date;
                    } else if (timeDetails.get(0) != null && date11 != null && date11.contains("currdetails")) {
                        String[] currVal = currentTimeDetails.get(key).toString().split("#");
                        if (currVal != null && currVal[1].equalsIgnoreCase("null")) {
                            date11 = currVal[0];
                            date = "null";
                        } else {
                            if (timeDetails.get(0) != null) {
                                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                                    date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                                } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                                    date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                                } else {
                                    date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                                }
                            }
                        }
                    } else {
                        date = "null";
                    }
                } else {
                    if (timeDetails.get(0) != null) {
                        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                            date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                            date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                        } else {
                            date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                        }

                    } else {
                        date = "null";
                    }
                }
            } else if (key.equalsIgnoreCase("AS_OF_DATE1")) {
                if (timeDetails.get(0) != null && date11 != null && date11.equals("fromyestrday")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.equals("fromtomorow")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("fromSysDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("fromglobalDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && currDetails != null && currDetails.contains("currdetails")) {
                    String[] currVal = currentTimeDetails.get(key).toString().split("#");
                    if (currVal != null && currVal[1].equalsIgnoreCase("null")) {
                        date11 = currVal[0];
                        date = "null";
                    } else {
                        if (timeDetails.get(0) != null) {
                            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                                date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                            } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                                date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                            } else {
                                date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                            }
                        }
                    }
                } else if (timeDetails.get(0) != null) {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                    } else {
                        date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                    }

                } else {
                    date = "null";
                }
            } else if (key.equalsIgnoreCase("AS_OF_DATE2")) {
                if (timeDetails.get(0) != null && date11 != null && date11.equals("toyestrday")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.equals("totomorow")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("toSystDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("toglobalDdate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && currDetails != null && currDetails.contains("currdetails")) {
                    String[] currVal = currentTimeDetails.get(key).toString().split("#");
                    if (currVal != null && currVal[1].equalsIgnoreCase("null")) {
                        date11 = currVal[0];
                        date = "null";
                    } else {
                        if (timeDetails.get(0) != null) {
                            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                                date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                            } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                                date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                            } else {
                                date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                            }
                        }
                    }
                } else if (timeDetails.get(0) != null) {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                    } else {
                        date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                    }

                } else {
                    date = "null";
                }
            } else if (key.equalsIgnoreCase("CMP_AS_OF_DATE1")) {
                if (timeDetails.get(0) != null && date11 != null && date11.contains("CmpFrmyestrday")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("CmpFrmtomorow")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("CmpFrmSysDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("CmpFrmglobalDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && currDetails != null && currDetails.contains("currdetails")) {
                    String[] currVal = currentTimeDetails.get(key).toString().split("#");
                    if (currVal != null && currVal[1].equalsIgnoreCase("null")) {
                        date11 = currVal[0];
                        date = "null";
                    } else {
                        if (timeDetails.get(0) != null) {
                            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                                date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                            } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                                date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                            } else {
                                date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                            }
                        }
                    }
                } else if (timeDetails.get(0) != null) {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                    } else {
                        date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                    }

                } else {
                    date = "null";
                }
            } else if (key.equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                if (timeDetails.get(0) != null && date11 != null && date11.contains("cmptoyestrday")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("cmptotomorow")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("cmptoSysDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("cmptoglobalDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && currDetails != null && currDetails.contains("currdetails")) {
                    String[] currVal = currentTimeDetails.get(key).toString().split("#");
                    if (currVal != null && currVal[1].equalsIgnoreCase("null")) {
                        date11 = currVal[0];
                        date = "null";
                    } else {
                        if (timeDetails.get(0) != null) {
                            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                                date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                            } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                                date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                            } else {
                                date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                            }
                        }
                    }
                } else if (timeDetails.get(0) != null) {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        date = "str_to_date('" + ((String) timeDetails.get(0)) + "','%m/%d/%Y')";
                    } else {
                        date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                    }

                } else {
                    date = "null";
                }
            } else {
                date = "null";
            }
            obj[0] = column_name;
            obj[1] = column_type;
            obj[2] = sequence;
            obj[3] = from_sequence;
            obj[4] = date;
            if (timeType.equalsIgnoreCase("PRG_COHORT")) {
                if (column_type.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    obj[5] = "Month";
                } else {
                    obj[5] = null;
                }
            } else {
                if (column_type.equalsIgnoreCase("PRG_COMPARE")) {
                    String comparewith = timeDetails.get(0).toString();
                    if (comparewith.equalsIgnoreCase("Last Period") || comparewith.equalsIgnoreCase("Last Year") || comparewith.equalsIgnoreCase("Period Complete") || comparewith.equalsIgnoreCase("Year Complete")) {
                        obj[5] = timeDetails.get(0);
                    } else {
                        obj[5] = duration;
                    }
                }
            }
            if (key.equalsIgnoreCase("AS_OF_DATE")) {
                if (date11 != null && date11.equalsIgnoreCase("yestrday") || date11 != null && date11.equalsIgnoreCase("Yesterday")) {
                    obj[4] = "null";
                    obj[5] = "Yesterday";
                } else if (date11 != null && date11.equalsIgnoreCase("tomorow") || date11 != null && date11.equalsIgnoreCase("Tomorrow")) {
                    obj[4] = "null";
                    obj[5] = "Tomorrow";
                } else if (date11 != null && date11.equalsIgnoreCase("systemDate")) {
                    obj[4] = "null";
                    obj[5] = "systemDate";
                } else if (date11 != null && (date11.contains("newSysDate") || date11.contains("globalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                    obj[4] = "null";
                    obj[5] = date11;
                } else if (date11 != null && date11.contains("newSysDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                } else if (date11 != null && date11.contains("globalDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                }
            } else if (key.equalsIgnoreCase("AS_OF_DATE1")) {
                if (date11 != null && date11.equalsIgnoreCase("fromyestrday")) {
                    obj[4] = "null";
                    obj[5] = "fromyestrday";
                } else if (date11 != null && date11.equalsIgnoreCase("fromtomorow")) {
                    obj[4] = "null";
                    obj[5] = "fromtomorow";
                } else if (date11 != null && (date11.contains("fromSysDate") || date11.contains("fromglobalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                    obj[4] = "null";
                    obj[5] = date11;
                } else if (date11 != null && date11.contains("fromSysDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                } else if (date11 != null && date11.contains("fromglobalDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                }
            } else if (key.equalsIgnoreCase("AS_OF_DATE2")) {
                if (date11 != null && date11.equalsIgnoreCase("toyestrday")) {
                    obj[4] = "null";
                    obj[5] = "toyestrday";
                } else if (date11 != null && date11.equalsIgnoreCase("totomorow")) {
                    obj[4] = "null";
                    obj[5] = "totomorow";
                } else if (date11 != null && (date11.contains("toSystDate") || date11.contains("toglobalDdate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                    obj[4] = "null";
                    obj[5] = date11;
                } else if (date11 != null && date11.contains("toSystDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                } else if (date11 != null && date11.contains("toglobalDdate")) {
                    obj[4] = "null";
                    obj[5] = date;
                }
            } else if (key.equalsIgnoreCase("CMP_AS_OF_DATE1")) {
                if (date11 != null && date11.equalsIgnoreCase("CmpFrmyestrday")) {
                    obj[4] = "null";
                    obj[5] = "CmpFrmyestrday";
                } else if (date11 != null && date11.equalsIgnoreCase("CmpFrmtomorow")) {
                    obj[4] = "null";
                    obj[5] = "CmpFrmtomorow";
                } else if (date11 != null && (date11.contains("CmpFrmSysDate") || date11.contains("CmpFrmglobalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                    obj[4] = "null";
                    obj[5] = date11;
                } else if (date11 != null && date11.contains("CmpFrmSysDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                } else if (date11 != null && date11.contains("CmpFrmglobalDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                }
            } else if (key.equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                if (date11 != null && date11.equalsIgnoreCase("cmptoyestrday")) {
                    obj[4] = "null";
                    obj[5] = "cmptoyestrday";
                } else if (date11 != null && date11.equalsIgnoreCase("cmptotomorow")) {
                    obj[4] = "null";
                    obj[5] = "cmptotomorow";
                } else if (date11 != null && (date11.contains("cmptoSysDate") || date11.contains("cmptoglobalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                    obj[4] = "null";
                    obj[5] = date11;
                } else if (date11 != null && date11.contains("cmptoSysDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                } else if (date11 != null && date11.contains("cmptoglobalDate")) {
                    obj[4] = "null";
                    obj[5] = date;
                }
            }
            obj[6] = repTimeId;
            obj[7] = column_type;
            finalQuery = buildQuery(sqlQuery, obj);

            queries.add(finalQuery);

            timeDetails = null;
            obj = null;
            i++;
        }
        return queries;
    }

    public ArrayList insertNonStdReportTimeDimensions(int reportId, ArrayList queries) {
        String sqlQuery = getResourceBundle().getString("insertReportTimeDimensions");
        String finalQuery = "";
        Object[] obj = null;
        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
            obj = new Object[2];
            obj[0] = "Non_Standard";
            obj[1] = "Day";
        } else {
            obj = new Object[3];
            obj[0] = "Non_Standard";
            obj[1] = "Day";
            obj[2] = reportId;
        }

        finalQuery = buildQuery(sqlQuery, obj);
        queries.add(finalQuery);
        //for(int time=0;time<timeParams.size();time++){
//        queries = insertReportTimeDimensionsDetails(timeDimHashMap, queries, timeParams);
        //}
        return queries;
    }

    public String getTrendGraphReportsByBuzRoles(String buzRoles) {
        PbReturnObject retObj = null;
        String[] colNames = null;
        String repName = "";
        String repId = "";
        String graphId = "";
        String graphName = "";
        Object[] dataValues = null;
        String getReportssql = getResourceBundle().getString("getTrendReportsByBuzRoles");
        StringBuffer outerBuffer = new StringBuffer("");
        String finalQuery = "";
        try {

            dataValues = new Object[1];
            dataValues[0] = buzRoles;
            finalQuery = buildQuery(getReportssql, dataValues);
            retObj = execSelectSQL(finalQuery);

            if (retObj != null) {
                colNames = retObj.getColumnNames();
                String tempReportId = null;
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    repId = retObj.getFieldValueString(i, colNames[0]);
                    repName = retObj.getFieldValueString(i, colNames[1]);

                    graphId = retObj.getFieldValueString(i, colNames[2]);
                    graphName = retObj.getFieldValueString(i, colNames[3]);


                    if (tempReportId == null) {
                        tempReportId = repId;

                        outerBuffer.append("<li class='closed' id='" + repName + i + "'>");
                        outerBuffer.append("<img src='icons pinvoke/report.png'></img>");
                        outerBuffer.append("<span style='font-family:verdana;font-size:8pt' onclick=\"dragGraph(" + repId + ")\">" + repName + "</span>");
                        outerBuffer.append("<ul id='repName-" + repName + "'>");

                        outerBuffer.append("<li class='closed' id='" + graphId + "'>");
                        outerBuffer.append("<img src='icons pinvoke/chart.png'></img>");
                        outerBuffer.append("<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href=javascript:buildDbrdGraph('" + repId + "','" + graphId + "') style='text-Decoration:none'>" + graphName + "</a></span>");
                        outerBuffer.append("</li>");



                    } else {
                        if (tempReportId.equalsIgnoreCase(repId)) {
                            outerBuffer.append("<li class='closed' id='" + graphId + "'>");
                            outerBuffer.append("<img src='icons pinvoke/chart.png'></img>");
                            outerBuffer.append("<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href=javascript:buildDbrdGraph('" + repId + "','" + graphId + "') style='text-Decoration:none'>" + graphName + "</a></span>");
                            outerBuffer.append("</li>");
                        } else {
                            outerBuffer.append("</ul>");
                            outerBuffer.append("</li>");

                            outerBuffer.append("<li class='closed' id='" + repName + i + "'>");
                            outerBuffer.append("<img src='icons pinvoke/report.png'></img>");
                            outerBuffer.append("<span style='font-family:verdana;font-size:8pt' onclick=\"dragGraph(" + repId + ")\">" + repName + "</span>");
                            outerBuffer.append("<ul id='repName-" + repName + "'>");

                            outerBuffer.append("<li class='closed' id='" + graphId + "'>");
                            outerBuffer.append("<img src='icons pinvoke/chart.png'></img>");
                            outerBuffer.append("<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href=javascript:buildDbrdGraph('" + repId + "','" + graphId + "') style='text-Decoration:none'>" + graphName + "</a></span>");
                            outerBuffer.append("</li>");

                            tempReportId = repId;
                        }
                    }
                }
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");

            }

        }catch (SQLException ex) {
             logger.error("Exception: ", ex);
        } catch (Exception ex) {
             logger.error("Exception: ", ex);
        }       return outerBuffer.toString();
        }

    public boolean deleteFavoParam(String favName, String dashboardId) {
        String delFavparams = "delete from PRG_AR_FAV_PARAMS where FAV_PARAM_NAME='" + favName + "'";
        boolean status = false;
        try {
            execModifySQL(delFavparams);
            status = true;
        } catch (Exception ex) {
            status = false;
            logger.error("Exception", ex);
        }        return status;
        }

    public String resolveGroupTimeTableDash(String elementId) throws SQLException {
        PbReturnObject retObj;
        String[] colNames = null;
        String colName = null;
        String minLevel = null;
        String bussTimeTableName = "pr_day_denom";
        PbDb pbdb = new PbDb();

        String sqlstr = "";
        sqlstr += "select ref_table_name from prg_grp_buss_table where BUSS_TABLE_ID = "
                + " (  select business_table_id from PRG_TIME_DIM_INFO "
                + " where MAIN_FACT_ID in "
                + " (select BUSS_TABLE_ID "
                + " from PRG_USER_ALL_INFO_DETAILS "
                + " where element_id =  " + elementId + " ) )  "
                + " and ref_table_name is not NULL  ";

//        System.out.println("sqlstr " +sqlstr);
        logger.info("sqlstr " + sqlstr);
        retObj = pbdb.execSelectSQL(sqlstr);

        colNames = retObj.getColumnNames();

        //Vector facts = new Vector();
        // HashMap CurrCols = new HashMap();
        // HashMap PriorCols = new HashMap();

        int psize = retObj.getRowCount();

        if (psize > 0) {
            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query
            if (retObj.getFieldValue(0, colNames[0]) != null) {
                bussTimeTableName = colName = retObj.getFieldValueString(0, colNames[0]);

            }
        }
        //TimetableId = retObj.getFieldValueString(0, colNames[1]);
        return (bussTimeTableName);

    }

    public void updateHideLeftTdStatus(String repId, String status, String tdType) {
        PbReturnObject retObj = new PbReturnObject();
        String qry = null;
        if (tdType.equalsIgnoreCase("lefttd")) {
            qry = getResourceBundle().getString("updateDashLeftTdStatus");
        } else {
            qry = getResourceBundle().getString("updateDashParamTdStatus");
        }
        Object[] obj = new Object[2];
        obj[0] = status;
        obj[1] = repId;
        String finalqry = buildQuery(qry, obj);
        try {
            execModifySQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }

    public String getLeftTdStatus(String repId, String tdtype) {
        String repleftTdStatus = null;
        PbReturnObject retObj = new PbReturnObject();
        String qry = null;
        if (tdtype.equalsIgnoreCase("lefttd")) {
            qry = getResourceBundle().getString("getDashLeftTdStatus");
        } else {
            qry = getResourceBundle().getString("getDashParamTdStatus");
        }
        Object[] obj = new Object[1];
        obj[0] = repId;
        String finalqry = buildQuery(qry, obj);
        try {
            retObj = execSelectSQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        if (tdtype.equalsIgnoreCase("lefttd")) {
            repleftTdStatus = retObj.getFieldValueString(0, "LEFT_TD_STATUS");
        } else {
            repleftTdStatus = retObj.getFieldValueString(0, "PARAM_TD_STATUS");
        }
        return repleftTdStatus;
    }

    public void deleteDbrdTarget(String elementId, String dashletId, String reportId, String kpiMasterId) {
        String delDbrdTargetquery = "delete from dashboard_target_kpi_value where  kpi_master_id=& and element_id=&";
        String finalQuery = "";
        Object[] delkpiTargetDets = new Object[2];

        delkpiTargetDets[0] = kpiMasterId;
        delkpiTargetDets[1] = elementId;

        try {
            finalQuery = buildQuery(delDbrdTargetquery, delkpiTargetDets);

            execModifySQL(finalQuery);
        }  catch (Exception ex) {
            logger.error("Exception: ", ex);
        }    }

    public String getReportname(String reportId) {
        String repname = "";
        PbReturnObject dbrdNameObj = null;
        String DbrdNameQuery = "select REPORT_NAME from prg_ar_report_master where report_id=" + reportId;
        try {
            dbrdNameObj = execSelectSQL(DbrdNameQuery);
            repname = dbrdNameObj.getFieldValueString(0, "REPORT_NAME");
        }
        catch (SQLException ex) {
           logger.error("Exception: ", ex);
        }        return repname;
    }

    public HashMap getReportviewbys(String repId) {
        ArrayList<String> viewbynames = new ArrayList<String>();
        ArrayList<String> viewbyids = new ArrayList<String>();
        PbReturnObject retobj = null;
        String[] colNames;
        HashMap reportviewbys = new HashMap();
        Object[] Obj = null;
        String finalqry = "";
        String getReportviwbysql = getResourceBundle().getString("getReportViewbys");
        Obj = new Object[1];
        Obj[0] = repId;
        finalqry = buildQuery(getReportviwbysql, Obj);
        try {
            retobj = execSelectSQL(finalqry);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        colNames = retobj.getColumnNames();
        int psize = retobj.getRowCount();
        if (psize > 0) {
            for (int looper = 0; looper < psize; looper++) {
                viewbyids.add(retobj.getFieldValueString(looper, colNames[0]));
                viewbynames.add(retobj.getFieldValueString(looper, colNames[1]));
            }
            viewbyids.add("TIME");
            viewbynames.add("Time");
        }
        reportviewbys.put("viewbynames", viewbynames);
        reportviewbys.put("viewbyids", viewbyids);

        return reportviewbys;
    }

    public String getCurrentViewbysofRep(String repId) {
//           String currentviewby = "";
        StringBuilder currentviewby = new StringBuilder(200);
        String getReportCurrentViewby = getResourceBundle().getString("getReportCurrentViewby");
        PbReturnObject retObject = null;
        String finalqry = "";
        Object[] Obj = null;
        Obj = new Object[1];
        Obj[0] = repId;
        finalqry = buildQuery(getReportCurrentViewby, Obj);
        try {
            retObject = execSelectSQL(finalqry);
            if (retObject.getRowCount() > 0) {
//                currentviewby = retObject.getFieldValueString(0,"VIEW_BY_ID");
                currentviewby.append(retObject.getFieldValueString(0, "VIEW_BY_ID"));
                for (int i = 1; i < retObject.getRowCount(); i++) {
//                    currentviewby = currentviewby+","+retObject.getFieldValueString(i,"VIEW_BY_ID");
                    currentviewby.append(",").append(retObject.getFieldValueString(i, "VIEW_BY_ID"));
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return currentviewby.toString();
    }

    public String getViewbyvaluesorep(String repId) {
//           String rowviewbyvalues = "";
        StringBuilder rowviewbyvalues = new StringBuilder(200);
        String qry = getResourceBundle().getString("getrowviebyvalues");
        Object[] obj = new Object[1];
        PbReturnObject retObj = null;
        String[] colNames;
        obj[0] = repId;
        String finalqry = "";
        finalqry = buildQuery(qry, obj);
        try {
            retObj = execSelectSQL(finalqry);
            colNames = retObj.getColumnNames();
            int psize = retObj.getRowCount();
            String paramExcludedIncluded = "NOT_SELECTED";

            if (psize > 0) {
                ArrayList paramInfo1 = new ArrayList();
                paramInfo1.add(retObj.getFieldValueString(0, colNames[0]));
                paramInfo1.add(retObj.getFieldValueString(0, colNames[1]));
                paramInfo1.add(retObj.getFieldValueString(0, colNames[2]));
                paramInfo1.add(retObj.getFieldValueString(0, colNames[3]));
                paramInfo1.add(retObj.getFieldValueString(0, colNames[4]));
                paramInfo1.add(retObj.getFieldValueString(0, colNames[5]));
                paramInfo1.add(retObj.getFieldValueString(0, colNames[6]));
                paramInfo1.add(retObj.getFieldValueString(0, colNames[7]));

                paramInfo1.add(retObj.getFieldValueClobString(0, "DEFAULT_VALUE"));
                paramExcludedIncluded = retObj.getFieldValueString(0, colNames[9]);
                if (paramExcludedIncluded.equals("")) {
                    paramExcludedIncluded = "NOT_SELECTED";
                }

                paramInfo1.add("&CBOARP" + retObj.getFieldValueString(0, colNames[0]));
                paramInfo1.add(paramExcludedIncluded);
//                rowviewbyvalues=  String.valueOf(paramInfo1.get(9)) + "=" + String.valueOf(paramInfo1.get(8));
                rowviewbyvalues.append(String.valueOf(paramInfo1.get(9))).append("=").append(String.valueOf(paramInfo1.get(8)));
                for (int looper = 1; looper < psize; looper++) {
                    ArrayList paramInfo = new ArrayList();
                    paramInfo.add(retObj.getFieldValueString(looper, colNames[0]));
                    paramInfo.add(retObj.getFieldValueString(looper, colNames[1]));
                    paramInfo.add(retObj.getFieldValueString(looper, colNames[2]));
                    paramInfo.add(retObj.getFieldValueString(looper, colNames[3]));
                    paramInfo.add(retObj.getFieldValueString(looper, colNames[4]));
                    paramInfo.add(retObj.getFieldValueString(looper, colNames[5]));
                    paramInfo.add(retObj.getFieldValueString(looper, colNames[6]));
                    paramInfo.add(retObj.getFieldValueString(looper, colNames[7]));

                    paramInfo.add(retObj.getFieldValueClobString(looper, "DEFAULT_VALUE"));
                    paramExcludedIncluded = retObj.getFieldValueString(looper, colNames[9]);
                    if (paramExcludedIncluded.equals("")) {
                        paramExcludedIncluded = "NOT_SELECTED";
                    }

                    paramInfo.add("&CBOARP" + retObj.getFieldValueString(looper, colNames[0]));
                    paramInfo.add(paramExcludedIncluded);
//                rowviewbyvalues=  rowviewbyvalues +"&"+String.valueOf(paramInfo.get(9)) + "=" + String.valueOf(paramInfo.get(8));
                    rowviewbyvalues.append(rowviewbyvalues).append("&").append(String.valueOf(paramInfo.get(9))).append("=").append(String.valueOf(paramInfo.get(8)));

                }
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return rowviewbyvalues.toString();
    }

    public String getGroupMeasures(String folderId) {
        String result = "";

        return result;
    }

    /**
     * @author srikanth.p to get childrens of the parent for GrouMeasssure
     * Isights
     * @param parentId,groupId
     * @return returnObject
     */
    public PbReturnObject getChildIds(String parentId, String groupId) {
        PbReturnObject childRetObj = new PbReturnObject();
        String sqlQuery = getResourceBundle().getString("getChildsForParent");
        String finalQuery = "";
        Object[] obj = new Object[2];
        obj[0] = parentId;
        obj[1] = groupId;
        finalQuery = buildQuery(sqlQuery, obj);
        
        try {
            childRetObj = execSelectSQL(finalQuery);
            } catch (SQLException ex) {
                logger.error("Exception: ", ex);
        } catch (Exception ex) {
                logger.error("Exception: ", ex);
        }
        return childRetObj;
    }

    public String getRowViewByforTextKpi(String dashboardId, String dashletId) {
        String rowViewBy = null;
        PbReturnObject returnObject = new PbReturnObject();
        String sqlQuery = getResourceBundle().getString("getRowViewByForTextKpi");
        String finalQuery = "";
        Object[] obj = new Object[2];
        obj[0] = dashboardId;
        obj[1] = dashletId;
        finalQuery = buildQuery(sqlQuery, obj);
        try {
            returnObject = execSelectSQL(finalQuery);
            rowViewBy = returnObject.getFieldValueString(0, "ROW_VIEW_BY");
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return rowViewBy;
    }

    public String saveTextKpiComments(String paramvalue, String commentText, String reportid, String dashletId, String userid) {

        String query = "insert into prg_textkpi_user_comments(COMMENT_ID,DASHBOARD_ID,DASHLET_ID,KPI_VALUE,KPI_COMMENT,COMMENT_DATE,USER_ID) values(prg_textkpi_user_comments_seq.nextval,'&','&','&','&',sysdate,'&')";
        Object textkpiobj[] = new Object[5];
        textkpiobj[0] = reportid;
        textkpiobj[1] = dashletId;
        textkpiobj[2] = paramvalue;
        textkpiobj[3] = commentText;
        textkpiobj[4] = userid;
        String finalquery = super.buildQuery(query, textkpiobj);
        try {
            int inserttextkpi = super.execUpdateSQL(finalquery);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return null;
    }

    public int updateDrillViewBys(String drillrepId, String drillValues, String elementId) {
        String drillViewBy = null;
        PbReturnObject returnObject = new PbReturnObject();
        int flag = 0;
        String sqlQuery = getResourceBundle().getString("updateDrillViewBys");
        String finalQuery = "";
        Object[] obj = new Object[3];
        obj[0] = drillValues;
        obj[1] = drillrepId;
        obj[2] = elementId;
        finalQuery = buildQuery(sqlQuery, obj);
        try {
            flag = execUpdateSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return flag;
    }

    public String getRowViewBysForRep(String drillRepId) {

        String rowViewBy = null;

        String query = getResourceBundle().getString("getRowViewByOfRep");
        Object[] obj = new Object[1];
        obj[0] = drillRepId;
        String finalqry = null;
        PbReturnObject retObj = null;
        finalqry = buildQuery(query, obj);
        try {
            retObj = execSelectSQL(finalqry);
            String[] colNames = retObj.getColumnNames();
            rowViewBy = retObj.getFieldValueString(0, colNames[3]);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return rowViewBy;
    }

    public void saveKpiTargetMapping(String kpiId, String dashletId, String kpimasterId, String dashboardId, String elementId) {
        String updateTargetmapping = getResourceBundle().getString("updateTargetMapping");
        Object[] obj = new Object[3];
        obj[0] = kpiId;
        obj[1] = kpimasterId;
        obj[2] = elementId;
//         obj[3] = dashboardId;
//         obj[4] = elementId;

        String finalquery = null;
        finalquery = buildQuery(updateTargetmapping, obj);
        try {
            execUpdateSQL(finalquery);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }
//Surender

    public PbReturnObject getGroupKpiDrillDashBoards(String groupName, String foldersIds) {
        String getKpiDrillReportsQuery = getResourceBundle().getString("getKpiCustomDashBoard");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] kpiReps = new Object[2];
        kpiReps[0] = foldersIds.replace(",", "").trim();
        try {
            finalQuery = buildQuery(getKpiDrillReportsQuery, kpiReps);

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
              logger.error("Exception: ", ex);
            retObj = null;
        } catch (Exception ex) {
              logger.error("Exception: ", ex);
            retObj = null;
        }
        return retObj;
    }

    /**
     * @author srikanth.p Belowe Methods are added for groupMessure hirarchy
     * creation
     */
    public PbReturnObject getParents(String groupId) {
        PbReturnObject rootRetObj = new PbReturnObject();
        String sqlQuery = getResourceBundle().getString("getParents");
        String finalQuery = "";
        Object[] obj = new Object[1];
        obj[0] = groupId;
        finalQuery = buildQuery(sqlQuery, obj);
        try {
            rootRetObj = execSelectSQL(finalQuery);
            } catch (SQLException ex) {
                  logger.error("Exception: ", ex);
        }catch (Exception ex) {
                  logger.error("Exception: ", ex);
        }


        return rootRetObj;
    }

    public String saveGroup(String folderId, String groupName) {
        StringBuilder output = new StringBuilder();
        String query = getResourceBundle().getString("groupInsert");
        PbReturnObject retObj = null;
        String queryForGroupId = "SELECT GRP_ID FROM PRG_USER_FOLDER WHERE Folder_ID=" + folderId;
        Object[] obj = new Object[3];
        obj[0] = groupName;
        obj[2] = folderId;
        String finalQuery = "";
        String grp_Id = "";
        int insertNo = 0;

        try {
            retObj = execSelectSQL(queryForGroupId);
            grp_Id = retObj.getFieldValueString(0, 0);
            obj[1] = grp_Id;
            finalQuery = buildQuery(query, obj);
            insertNo = execUpdateSQL(finalQuery);
            logger.info("Inserted Groups:" + insertNo);
            if (insertNo == 1) {
                output.append("<script>alert('" + groupName + " sucessfully created..!!')</script>");
            } else {
                output.append("<script>alert( 'Group Not Created')</script>");
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception ex) { 
            logger.error("Exception: ", ex);
        }
        return output.toString();

    }

    public PbReturnObject getGroupRetObj() {
        PbReturnObject retObj = new PbReturnObject();
        String query = "SELECT GROUP_ID,GROUP_NAME,Folder_Id FROM PRG_GRP_GROUP_HIERARCHY_MASTER ORDER BY GROUP_NAME";
        try {
            retObj = execSelectSQL(query);
            } catch (SQLException ex) {
                logger.error("Exception: ", ex);
        } catch (Exception ex) {
                logger.error("Exception: ", ex);
        }

        return retObj;
    }

    public String insertParents(GroupMeassureParams grpParams, HashMap elemMap) {
        StringBuilder output = new StringBuilder();
        PbDb pbdb = new PbDb();
        String folderId = grpParams.getFolderId();
        String groupId = grpParams.getGroupId();
        String grpName = grpParams.getGroupName();
        Set elementSet = elemMap.keySet();
        Object[] elements = elementSet.toArray();
        ArrayList selectedElements = new ArrayList();
        boolean flag = false;
//         StringBuilder elems=new StringBuilder();
//         for(int i=0;i<elementSet.size();i++)
//         {
//             elems.append(",").append(elements[i]);
//
//         }
//         String elementString=elems.substring(1);
        String query = getResourceBundle().getString("parentInsert");
        String grpNamequery = "SELECT GROUP_NAME FROM PRG_GRP_GROUP_HIERARCHY_MASTER WHERE GROUP_ID=" + groupId;
        String selectQuery = "SELECT a.GROUP_NAME,b.PERENT_ELEMENT_ID,b.Group_Id FROM PRG_GRP_GROUP_HIERARCHY_MASTER a,PRG_USER_ELEMENTS_HIERARCHY b WHERE a.GROUP_ID=b.group_id AND b.GROUP_ID = " + groupId + " AND b.level_no=1";
        PbReturnObject retObj = new PbReturnObject();
        int updateCount = 0;
        Object[] obj = new Object[5];
//           Object[] grpObj=new Object[1];
//           grpObj[0]=groupId;
        String finalQuery = "";
//           String finalSelect=buildQuery(selectQuery,grpObj);
        String updateQuery = "";
        ArrayList finalExcecuteQueris = new ArrayList();
        try {
            retObj = execSelectSQL(selectQuery);
            if (retObj.rowCount == 0) {
                retObj = execSelectSQL(grpNamequery);
            }
            for (int i = 0; i < retObj.rowCount; i++) {
                selectedElements.add(retObj.getFieldValueString(i, 1));
            }
            grpName = retObj.getFieldValueString(0, 0);
            String seqNum = "";
            for (int j = 0; j < elementSet.size(); j++) {
                obj[0] = elements[j];
                obj[1] = groupId;
                obj[2] = grpName;
                obj[3] = folderId;
                obj[4] = elemMap.get(elements[j]);
                if (selectedElements.contains(elements[j])) {
                    updateQuery = "UPDATE PRG_USER_ELEMENTS_HIERARCHY SET SEQUENCE_NUMBER=" + obj[4] + "WHERE PERENT_ELEMENT_ID=" + elements[j] + " AND GROUP_ID=" + groupId;
                } else {
                    updateQuery = buildQuery(query, obj);
                }
                finalExcecuteQueris.add(updateQuery);

            }
            flag = pbdb.executeMultiple(finalExcecuteQueris);
//              for(int i=0;i<elemMap.size();i++)
//              {
//                  int count=0;
//                  obj[0]=elements[i];
//                  obj[1]=groupId;
//                  obj[2]=grpName;
//                  obj[3]=folderId;
//                  finalQuery=buildQuery(query, obj);
//                  count=execUpdateSQL(finalQuery);
//                  if(count==1)
//                  {
//                      updateCount++;
//                  }
//              }
            if (flag == true) {
                output.append("<script>alert('" + elemMap.size() + " Parents are sucessfully updated.')</script>");
            } else {
                output.append("<script>alert( 'Parents are Not updated..!!')</script>");
            }
//              


        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception ex) {
            logger.error("Exception: ", ex);
        }         return output.toString();

    }

    public PbReturnObject getParentsRetObj(String groupId) {
        PbReturnObject retObj = new PbReturnObject();
        String query = getResourceBundle().getString("getParentsForGHCreation");
        Object[] obj = new Object[1];
        obj[0] = groupId;
        String finalQuery = buildQuery(query, obj);
        try {
            retObj = execSelectSQL(finalQuery);
            } catch (SQLException ex) {
                logger.error("Exception: ", ex);
        }catch (Exception ex) {
                logger.error("Exception: ", ex);
        }

        return retObj;
    }

    public String insertChilds(GroupMeassureParams grpParams, HashMap elemMap) {
        StringBuilder output = new StringBuilder();
        String folderId = grpParams.getFolderId();
        String groupId = grpParams.getGroupId();
        String grpName = grpParams.getGroupName();
        String parentId = grpParams.getMeassureId();
        PbDb pbdb = new PbDb();
        Set elementSet = elemMap.keySet();
        Object[] elements = elementSet.toArray();
        ArrayList selectedElements = new ArrayList();
        ArrayList finalExcecuteQueris = new ArrayList();
        boolean flag = false;
        String query = getResourceBundle().getString("childInsert");

        String grpNamequery = "SELECT GROUP_NAME FROM PRG_GRP_GROUP_HIERARCHY_MASTER WHERE GROUP_ID=" + groupId;
        PbReturnObject retObj = new PbReturnObject();
        PbReturnObject selectedRetObj = new PbReturnObject();
        selectedRetObj = getchildsRetObj(groupId, parentId, folderId);
        int updateCount = 0;
        Object[] obj = new Object[6];
        String finalQuery = "";
        try {
//              retObj=execSelectSQL(grpNamequery);
            if (selectedRetObj.rowCount == 0) {
                selectedRetObj = execSelectSQL(grpNamequery);
                grpName = selectedRetObj.getFieldValueString(0, 0);
            } else {
                for (int i = 0; i < selectedRetObj.getRowCount(); i++) {
                    selectedElements.add(selectedRetObj.getFieldValueString(i, 0));
                }
                grpName = retObj.getFieldValueString(0, 5);
            }
            for (int j = 0; j < elementSet.size(); j++) {
                int count = 0;
                obj[0] = parentId;
                obj[1] = elements[j];
                obj[2] = groupId;
                obj[3] = grpName;
                obj[4] = folderId;
                obj[5] = elemMap.get(elements[j]);
                if (selectedElements.contains(elements[j])) {
                    finalQuery = "UPDATE PRG_USER_ELEMENTS_HIERARCHY SET SEQUENCE_NUMBER=" + obj[5] + "WHERE PERENT_ELEMENT_ID=" + parentId + " AND GROUP_ID=" + groupId + " AND CHILD_ELEMENT_ID=" + elements[j];
                } else {
                    finalQuery = buildQuery(query, obj);
                }
                finalExcecuteQueris.add(finalQuery);

            }
            //below code for deleting the elements which are present in list from database but they are delleted at the front-end which are not present in elementSet
            //on delleting parent, childs under the current parent are added as  childs of its parent's parent
            for (int k = 0; k < selectedElements.size(); k++) {
                if (!elementSet.contains(selectedElements.get(k))) {
                    finalQuery = "UPDATE PRG_USER_ELEMENTS_HIERARCHY SET PERENT_ELEMENT_ID=" + parentId + " WHERE group_id=" + groupId + " AND PERENT_ELEMENT_ID=" + selectedElements.get(k);
                    finalExcecuteQueris.add(finalQuery);
                    finalQuery = "DELETE FROM PRG_USER_ELEMENTS_HIERARCHY WHERE CHILD_ELEMENT_ID=" + selectedElements.get(k) + " AND PERENT_ELEMENT_ID=" + parentId + " AND group_id=" + groupId;
                    finalExcecuteQueris.add(finalQuery);
                }
            }

            flag = pbdb.executeMultiple(finalExcecuteQueris);
//              
            if (flag) {
                output.append("<script>alert('" + elemMap.size() + " Childs are sucessfully updated.')</script>");
            } else {
                output.append("<script>alert( 'Childs are Not updated some Problem Occured..!!')</script>");
            }

        }  catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception ex) {
                logger.error("Exception: ", ex);
        }        return output.toString();

    }

    public PbReturnObject getchildsRetObj(String groupId, String parentId, String folderId) {
        PbReturnObject retObj = new PbReturnObject();
        String query = getResourceBundle().getString("getChildsForGHCreation");
        Object[] obj = new Object[3];
        obj[0] = parentId;
        obj[1] = groupId;
        obj[2] = folderId;
        String finalQuery = buildQuery(query, obj);
        try {
            retObj = execSelectSQL(finalQuery);
            } catch (SQLException ex) {
                logger.error("Exception: ", ex);
        }catch (Exception ex) {
                logger.error("Exception: ", ex);
        }

        return retObj;
    }

    public String getPrevElements(String groupId, String parentId, String folderId) {
        StringBuilder elementList = new StringBuilder();
        String selectQuery = "";
        PbReturnObject retObj = new PbReturnObject();


        try {
            if (parentId == null || parentId.equalsIgnoreCase("null") || parentId.equalsIgnoreCase("")) {
                selectQuery = "SELECT DISTINCT P.PERENT_ELEMENT_ID, E.USER_COL_DESC, P.GROUP_ID, P.Folder_Id,P.SEQUENCE_NUMBER FROM PRG_USER_ALL_INFO_DETAILS E, PRG_USER_ELEMENTS_HIERARCHY P WHERE P.PERENT_ELEMENT_ID=E.element_id AND P.GROUP_ID= " + groupId + " AND P.Folder_Id= " + folderId + " AND P.level_no=1 ORDER BY P.SEQUENCE_NUMBER ";
                retObj = execSelectSQL(selectQuery);
            } else {
                retObj = getchildsRetObj(groupId, parentId, folderId);
            }
            if (retObj.getRowCount() == 0) {
                elementList.append(",");
            }

            for (int i = 0; i < retObj.getRowCount(); i++) {
                elementList.append(",");
                elementList.append(retObj.getFieldValueString(i, 0)).append("^").append(retObj.getFieldValueString(i, 1));
            }
        }  catch (SQLException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception ex) {
                logger.error("Exception: ", ex);
        }       return elementList.toString();
        }

//Surender
    public void deleteOneview(String oneviewId, String userType, String userId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        //modified by anitha for power analyzer: start
        if (userType != null && userType.equalsIgnoreCase("true")) {
            //
            String getKpiDrillReportsQuery = getResourceBundle().getString("deleteOneviewdata");
            String delDbrdCommentquery = getResourceBundle().getString("deleteOneviewd");
            String dltQry = getResourceBundle().getString("deleteRegOneviewd");
            String query = "Select INNER_REG_FILENAME FROM PRG_AR_ONEVIEW_VIEWLET_DETAILS WHERE ONEVIEW_ID=" + oneviewId;
            PbReturnObject retobj = null;
            PbReturnObject retobj1 = null;
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//        String delDbrdCommentquery = "delete from oneview_data where view_id='&'";
            String finalQuery = "";
            String finalQuery1 = "";
            Object[] delkpiComntDets = new Object[1];
            delkpiComntDets[0] = oneviewId;
            retobj1 = reportTemplateDAO.getOneviewFileNam(oneviewId);
            String fileName = retobj1.getFieldValueString(0, "ONEVIEW_FILE");
            String version = retobj1.getFieldValueString(0, "ONE_VERSION");
            String filePath = retobj1.getFieldValueString(0, "FILEPATH");
            String oldAdvHtmlFileProps = "";


            try {
//            finalQuery = buildQuery(getKpiDrillReportsQuery, delkpiComntDets);

                execModifySQL(getKpiDrillReportsQuery);

                finalQuery = buildQuery(delDbrdCommentquery, delkpiComntDets);

                execModifySQL(finalQuery);
                if (Float.parseFloat(version) > 2.0) {
                    oldAdvHtmlFileProps = filePath;
                    OneViewBD oneBd = new OneViewBD();
                    File delFile = new File(oldAdvHtmlFileProps);
                    if (delFile.exists()) {
                        if (delFile.isDirectory()) {
                            File[] innerFiles = delFile.listFiles();
                            for (int i = 0; i < innerFiles.length; i++) {
                                innerFiles[i].delete();
                            }
                        }
                        delFile.delete();

                    }
                } else {
                    InputStream servletStream = session.getServletContext().getResourceAsStream("/WEB-INF/classes/cache.ccf");
                    if (servletStream != null) {
                        try {
                            Properties fileProps = new Properties();
                            fileProps.load(servletStream);
                            oldAdvHtmlFileProps = fileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath");
                            session.setAttribute("oldAdvHtmlFileProps", oldAdvHtmlFileProps);
                        } catch (Exception e) {
                            logger.error("Exception: ", e);
                        }

                        retobj = execSelectSQL(query);
                        if (retobj.getRowCount() != 0) {
                            for (int i = 0; i < retobj.getRowCount(); i++) {
                                File file = new File(oldAdvHtmlFileProps + "/" + retobj.getFieldValueString(i, 0));
                                file.delete();
                            }
                        }
                    }
                }
                finalQuery1 = buildQuery(dltQry, delkpiComntDets);
                execModifySQL(finalQuery1);
                File file = new File(oldAdvHtmlFileProps + "/" + fileName);
                file.delete();
            } catch (SQLException ex) {
                logger.error("Exception: ", ex);
            }catch (Exception ex) { 
                logger.error("Exception: ", ex);
            }
        } else {
            String getdeleteOneviewAssignment = getResourceBundle().getString("deleteOneviewAssignment");
            Object[] delDets = new Object[2];
            delDets[0] = oneviewId;
            delDets[1] = userId;
            String finalQuery = "";
            finalQuery = buildQuery(getdeleteOneviewAssignment, delDets);
            try {
                execModifySQL(finalQuery);
            } catch (Exception ex) {
                logger.error("Exception", ex);
            }
        }
        //modified by anitha for power analyzer:end

    }

    public boolean insertIcalDetail(String icalName, String busroleId, String userId, String elementIds, String elementNames, String roleIds, String nodays, int imonth, String iyear, String checkDailyval, String compareVal, String formatList, String roundList, String primaryMeas, String dispNameList, String timeDetails, String monthlyView) {
        String seq1 = "";
        int seq = 0;
        int segAssisgn = 0;
        int measSeq = 1;
        PbDb pbdborder = new PbDb();
        Calendar cal;
        String s;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        String value = "";
        cal = Calendar.getInstance();
        date = cal.getTime();
        value = sdf.format(date);
        String[] dateArr = value.split("/");
        logger.info("monthlyView+++++" + monthlyView);
        if (monthlyView != null && monthlyView.equalsIgnoreCase("true")) {
            monthlyView = "true";
        } else {
            monthlyView = "false";
        }
        String orderQuery = "select max(ICAL_SEQ) from ICAL_MASTER";
        PbReturnObject pbroIcalOrderObject = new PbReturnObject();
        try {
            pbroIcalOrderObject = pbdborder.execSelectSQL(orderQuery);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }
        //String[] SeqList = null;
        Object[] reportDets;

        String insertTMDetQuery = getResourceBundle().getString("insertIcalDetail");
        if (!ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            try {
                seq = super.getSequenceNumber(" select ICAL_MASTER_SEQ.NEXTVAL FROM DUAL");
            } catch (Exception ex) {
                logger.error("Exception", ex);
            }
        }
        if (!ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            reportDets = new Object[9];
            reportDets[0] = seq;
            reportDets[1] = icalName;
            reportDets[2] = Integer.parseInt(busroleId);
            if (timeDetails != null && !timeDetails.equalsIgnoreCase("") && timeDetails.equalsIgnoreCase("currdetails")) {
                reportDets[3] = nodays + "/" + imonth + "/" + iyear;
            } else {
                reportDets[3] = nodays + "/" + (Integer.parseInt(dateArr[1]) - 1) + "/" + dateArr[2];
            }
            reportDets[4] = checkDailyval;
            reportDets[5] = pbroIcalOrderObject.getFieldValueInt(0, 0) + 1;
            reportDets[6] = compareVal;
            reportDets[7] = primaryMeas;
            reportDets[8] = monthlyView;
        } else {
            reportDets = new Object[8];
            reportDets[0] = icalName;
            reportDets[1] = Integer.parseInt(busroleId);
            if (timeDetails != null && !timeDetails.equalsIgnoreCase("") && timeDetails.equalsIgnoreCase("currdetails")) {
                reportDets[2] = nodays + "/" + imonth + "/" + iyear;
            } else {
                reportDets[2] = nodays + "/" + (Integer.parseInt(dateArr[1]) - 1) + "/" + dateArr[2];
            };
            reportDets[3] = checkDailyval;
            reportDets[4] = pbroIcalOrderObject.getFieldValueInt(0, 0) + 1;
            reportDets[5] = compareVal;
            reportDets[6] = primaryMeas;
            reportDets[7] = monthlyView;
        }
        String finalQuery = "";

        try {
            finalQuery = buildQuery(insertTMDetQuery, reportDets);
            // 
//            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {               
//                seq = insertAndGetSequenceInSQLSERVER(finalQuery, "ICAL_MASTER");
//            } else {
            execModifySQL(finalQuery);
            //           }
        }  catch (Exception ex) {
            logger.error("Exception: ", ex);
        }        String insertAssignquery = getResourceBundle().getString("insertAssignmentDetails");
        Object[] assignDets;

        if (!ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            try {
                segAssisgn = super.getSequenceNumber("select ICAL_ASSIGNMENT_SEQ.NEXTVAL FROM DUAL");
            } catch (Exception ex) {
                logger.error("Exception", ex);
            }
        }
        if (!ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            assignDets = new Object[3];
            assignDets[0] = segAssisgn;
            assignDets[1] = seq;
            assignDets[2] = Integer.parseInt(userId);
        } else {
            assignDets = new Object[2];
            assignDets[0] = "IDENT_CURRENT('ICAL_MASTER')";
            assignDets[1] = Integer.parseInt(userId);
        }
        String assignfinalQuery = "";
        try {
            assignfinalQuery = buildQuery(insertAssignquery, assignDets);
            //    
//            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                segAssisgn = insertAndGetSequenceInSQLSERVER(assignfinalQuery, "ICAL_ASSIGNMENT");
//            } else {
            execModifySQL(assignfinalQuery);
//            }
        }   catch (Exception ex) {
            logger.error("Exception: ", ex);
        }        String insertMeasuresQry = getResourceBundle().getString("insertmeasureDetails");
        String measFinalQry = "";
        ArrayList queries = new ArrayList();
        Object[] measDets;
        String[] eltIds = elementIds.split(",");
        String[] eltNames = elementNames.split(",");
        String[] rolIds = roleIds.split(",");
        String[] formatListVals = null;
        String[] roundListVals = null;
        String[] dispNameListVals = null;
        if (formatList != null && !formatList.equalsIgnoreCase("")) {
            formatListVals = formatList.split(",");
            roundListVals = roundList.split(",");
            dispNameListVals = dispNameList.split(",");
        }
        for (int i = 0; i < eltIds.length; i++) {
            if (!ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                measDets = new Object[9];
                measDets[0] = seq;
                measDets[1] = eltIds[i];
                measDets[2] = eltIds[i];
                measDets[3] = eltNames[i];
                if (dispNameListVals != null && (i < dispNameListVals.length) && dispNameListVals[i] != null && !dispNameListVals[i].equalsIgnoreCase("")) {
                    measDets[4] = dispNameListVals[i];
                } else {
                    measDets[4] = eltNames[i];
                }
                if (dispNameListVals != null && (i < dispNameListVals.length) && dispNameListVals[i] != null && !dispNameListVals[i].equalsIgnoreCase("")) {
                    measDets[5] = dispNameListVals[i];
                } else {
                    measDets[5] = eltNames[i];
                }

                measDets[6] = measSeq;

                if (formatListVals != null && (i < formatListVals.length) && formatListVals[i] != null && !formatListVals[i].equalsIgnoreCase("")) {
                    measDets[7] = formatListVals[i];
                } else {
                    measDets[7] = "A";
                }
                if (roundListVals != null && (i < roundListVals.length) && roundListVals[i] != null && !roundListVals[i].equalsIgnoreCase("")) {
                    measDets[8] = roundListVals[i];
                } else {
                    measDets[8] = "0";
                }
            } else {
                measDets = new Object[9];
                measDets[0] = "IDENT_CURRENT('ICAL_MASTER')";
                measDets[1] = eltIds[i];
                measDets[2] = eltIds[i];
                measDets[3] = eltNames[i];
                if (dispNameListVals != null && (i < dispNameListVals.length) && dispNameListVals[i] != null && !dispNameListVals[i].equalsIgnoreCase("")) {
                    measDets[4] = dispNameListVals[i];
                } else {
                    measDets[4] = eltNames[i];
                }
                if (dispNameListVals != null && (i < dispNameListVals.length) && dispNameListVals[i] != null && !dispNameListVals[i].equalsIgnoreCase("")) {
                    measDets[5] = dispNameListVals[i];
                } else {
                    measDets[5] = eltNames[i];
                }

                measDets[6] = measSeq;

                if (formatListVals != null && (i < formatListVals.length) && formatListVals[i] != null && !formatListVals[i].equalsIgnoreCase("")) {
                    measDets[7] = formatListVals[i];
                } else {
                    measDets[7] = "A";
                }
                if (roundListVals != null && (i < roundListVals.length) && roundListVals[i] != null && !roundListVals[i].equalsIgnoreCase("")) {
                    measDets[8] = roundListVals[i];
                } else {
                    measDets[8] = "0";
                }
            }
            measFinalQry = buildQuery(insertMeasuresQry, measDets);
            queries.add(measFinalQry);
            //
            measSeq++;
        }
        boolean flag = executeMultiple(queries);
        return flag;
    }

    public String getSavedMeasDetails(String icalId, HttpSession session) {
        String qry = getResourceBundle().getString("getMeasuresDetails");
        String roleIdqry = getResourceBundle().getString("getRoleId");
        PbReturnObject retObj = new PbReturnObject();
        PbReturnObject retObj1 = new PbReturnObject();
        List<String> idList = new ArrayList<String>();
        List<String> originNameList = new ArrayList<String>();
        String finalQuery = "";
        String finalQuery1 = "";
        List<String> nameList = new ArrayList<String>();
        List<String> formatList = new ArrayList<String>();
        List<String> roundList = new ArrayList<String>();
        String roleId = "";
        String viewType = "";
        String comparisionType = "";
        String primaryMeasure = "";
        String monthlyIcal = "";
        String dateDetails = "";
        String[] dateArray = null;
        String jsonString = "";
        String IdStr = "";
        String orgNameStr = "";
        String NameStr = "";
        String formatListStr = "";
        String roundListStr = "";
        Object[] obj = new Object[1];
        obj[0] = icalId;
        OnceViewContainer onecontainer = null;
        HashMap map2 = new HashMap();
        if (session.getAttribute("ICALDETAILS") != null) {
            map2 = (HashMap) session.getAttribute("ICALDETAILS");
            onecontainer = (OnceViewContainer) map2.get(icalId);
        } else {
            onecontainer = new OnceViewContainer();
        }
        LinkedHashMap map = new LinkedHashMap();
        try {
            finalQuery = buildQuery(qry, obj);
            finalQuery1 = buildQuery(roleIdqry, obj);
            retObj = execSelectSQL(finalQuery);
            retObj1 = execSelectSQL(finalQuery1);
            if (retObj != null && retObj.getRowCount() != 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    idList.add(retObj.getFieldValueString(i, "MEASURE_ID"));
                    originNameList.add(retObj.getFieldValueString(i, "ELEMENT_ID_NAME"));
                    nameList.add(retObj.getFieldValueString(i, "MEASURE_DISPLAY_NAME"));
                    formatList.add(retObj.getFieldValueString(i, "DISPLAY_FORMAT"));
                    roundList.add(retObj.getFieldValueString(i, "ROUND_VALUE"));
                }
            }
            if (retObj1 != null && retObj1.getFieldValueString(0, 0) != null) {
                roleId = retObj1.getFieldValueString(0, "ICAL_ROLE_ID");
                dateDetails = retObj1.getFieldValueString(0, "ICAL_DEFAULT_DATE_VARCHAR");
                viewType = retObj1.getFieldValueString(0, "VIEW_TYPE");
                comparisionType = retObj1.getFieldValueString(0, "COMPARE_WITH");
                primaryMeasure = retObj1.getFieldValueString(0, "PRIMARY_MEASURE");
                monthlyIcal = retObj1.getFieldValueString(0, "MONTHLY_CAL");
            }
            for (int i = 0; i < idList.size(); i++) {
                if (IdStr == "" && NameStr == "") {
                    IdStr = idList.get(i);
                    NameStr = nameList.get(i);
                    orgNameStr = originNameList.get(i);
                    formatListStr = formatList.get(i);
                    roundListStr = roundList.get(i);
                } else {
                    IdStr = IdStr + "," + idList.get(i);
                    NameStr = NameStr + "," + nameList.get(i);
                    orgNameStr = orgNameStr + "," + originNameList.get(i);
                    formatListStr = formatListStr + "," + formatList.get(i);
                    roundListStr = roundListStr + "," + roundList.get(i);
                }
            }
            onecontainer.setElementIds(IdStr);
            onecontainer.setElementNames(NameStr);
            onecontainer.setFormatList(formatListStr);
            onecontainer.setRoundList(roundListStr);
            onecontainer.setComparisionType(comparisionType);
            onecontainer.setPrimaryMeasure(primaryMeasure);
            onecontainer.setRoleId(roleId);
            onecontainer.setMonthlyCal(monthlyIcal);
            onecontainer.setViewType(viewType);

            dateArray = dateDetails.split("/");
            session.setAttribute("elementIds", IdStr);
            session.setAttribute("elementNames", NameStr);
            session.setAttribute("orgelementNames", orgNameStr);
            onecontainer.setNodays(dateArray[0]);
            map2.put(icalId, onecontainer);
            session.setAttribute("ICALDETAILS", map2);
            session.setAttribute("iMonth", dateArray[1]);
            session.setAttribute("iYear", dateArray[2]);
            map.put("idList", idList);
            map.put("nameList", nameList);
            map.put("roleId", roleId);
            map.put("dateDetails", dateDetails);
            map.put("viewType", viewType);
            map.put("comparisionType", comparisionType);
            map.put("primaryMeasure", primaryMeasure);
            map.put("monthlyIcal", monthlyIcal);
            Gson gson = new Gson();
            jsonString = gson.toJson(map);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return jsonString;
    }

    public boolean updateIcal(String overWriteId, String icalName, String busroleId, String userId, String elementIds, String elementNames, String roleIds, String nodays, int imonth, String iyear, String checkDailyval, String formatList, String roundList, String compareVal, String primaryMeas, String dispNameList, String timeDetails, String monthlyView) {
        String updateIcalQry = getResourceBundle().getString("insertmeasureDetails");
        String deleteQry = getResourceBundle().getString("deleteMeasQry");
        String updateMasterTabQry = getResourceBundle().getString("updatemasterTable");
        String measFinalQry = "";
        int measSeq = 1;
        logger.info("monthlyView+++++" + monthlyView);
        if (monthlyView != null && monthlyView.equalsIgnoreCase("true")) {
            monthlyView = "true";
        } else {
            monthlyView = "false";
        }
        Object[] icalId = new Object[1];
        Object[] compareDetails = new Object[5];
        ArrayList queries = new ArrayList();
        Object[] measDets = new Object[10];
        String[] eltIds = elementIds.split(",");
        String[] eltNames = elementNames.split(",");
        String[] formatListVals = null;
        String[] roundListVals = null;
        String[] dispNameListVals = null;
        Calendar cal;
        String s;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        String value = "";
        cal = Calendar.getInstance();
        date = cal.getTime();
        value = sdf.format(date);
        String[] dateArr = value.split("/");
        if (formatList != null && !formatList.equalsIgnoreCase("")) {
            formatListVals = formatList.split(",");
            roundListVals = roundList.split(",");
            if (dispNameList != null && !dispNameList.equalsIgnoreCase("")) {
                dispNameListVals = dispNameList.split(",");
                if (eltIds.length != dispNameListVals.length) {
                    dispNameListVals = null;
                }
            }
        }
        //String[] rolIds = roleIds.split(",");
        icalId[0] = overWriteId;
        try {
            execModifySQL(buildQuery(deleteQry, icalId));
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        for (int i = 0; i < eltIds.length; i++) {
            measDets[0] = overWriteId;
            measDets[1] = eltIds[i];
            measDets[2] = eltIds[i];
            measDets[3] = eltNames[i];
            if (dispNameListVals != null && (i < dispNameListVals.length) && dispNameListVals[i] != null && !dispNameListVals[i].equalsIgnoreCase("")) {
                measDets[4] = dispNameListVals[i];
            } else {
                measDets[4] = eltNames[i];
            }
            if (dispNameListVals != null && (i < dispNameListVals.length) && dispNameListVals[i] != null && !dispNameListVals[i].equalsIgnoreCase("")) {
                measDets[5] = dispNameListVals[i];
            } else {
                measDets[5] = eltNames[i];
            }

            measDets[6] = measSeq;

            if (formatListVals != null && (i < formatListVals.length) && formatListVals[i] != null && !formatListVals[i].equalsIgnoreCase("")) {
                measDets[7] = formatListVals[i];
            } else {
                measDets[7] = "A";
            }
            if (roundListVals != null && (i < roundListVals.length) && roundListVals[i] != null && !roundListVals[i].equalsIgnoreCase("")) {
                measDets[8] = roundListVals[i];
            } else {
                measDets[8] = "0";
            }
            measFinalQry = buildQuery(updateIcalQry, measDets);
            queries.add(measFinalQry);
            //
            measSeq++;
        }
        if (timeDetails != null && !timeDetails.equalsIgnoreCase("") && timeDetails.equalsIgnoreCase("currdetails")) {
            compareDetails[0] = nodays + "/" + imonth + "/" + iyear;
        } else {
            compareDetails[0] = nodays + "/" + (Integer.parseInt(dateArr[1]) - 1) + "/" + dateArr[2];
        }
        //
        //
        compareDetails[1] = compareVal;
        compareDetails[2] = primaryMeas;
        compareDetails[3] = monthlyView;
        compareDetails[4] = overWriteId;
        String masterQry = "";
        masterQry = buildQuery(updateMasterTabQry, compareDetails);
        try {
            //
            execModifySQL(masterQry);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        boolean flag = executeMultiple(queries);
        return flag;
    }

    public void deleteIcal(String icalId) {
        String deleteIcalQry = getResourceBundle().getString("deleteIcalQry");
        Object[] icalId1 = new Object[1];
        icalId1[0] = icalId;
        try {
            execModifySQL(buildQuery(deleteIcalQry, icalId1));
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }

    /**
     * @author srikanth.P to getAll Measures in a group
     */
    public PbReturnObject getAllMeasures(String dbrdId, String dashletId) {
        PbReturnObject retObj = new PbReturnObject();
        String query = getResourceBundle().getString("getAllMeasures");
        Object[] obj = new Object[2];
        obj[0] = dbrdId;
        obj[1] = dashletId;
        String finalQuery = buildQuery(query, obj);
        try {
            retObj = execSelectSQL(finalQuery);
            } catch (SQLException ex) {
                logger.error("Exception: ", ex);
        } catch (Exception ex) {
                logger.error("Exception: ", ex);
        }

        return retObj;

    }

    /**
     * @Author srikanth.P this mothod updates the reports assigned to measures
     */
    public boolean saveReportAssignment(HashMap reportMap, String dbrdId, String dashletId) {
        PbDb pbdb = new PbDb();
        boolean status = false;
        Set keySet = reportMap.keySet();
        Iterator iter = keySet.iterator();
        String groupId = "";
        String parentId = "";
        String elementId = "";
//            String reportId="";
        String combKey = "";
        String updateQuery = "";
        ArrayList<String> queryList = new ArrayList<String>();
        while (iter.hasNext()) {
            combKey = String.valueOf(iter.next());
            String[] key = combKey.split("~");
            groupId = key[0].trim().substring(key[0].trim().indexOf("_") + 1);
            parentId = key[1].trim().substring(key[1].trim().indexOf("_") + 1);
            elementId = key[2].trim().substring(key[2].trim().indexOf("_") + 1);
            String[] reportIds = String.valueOf(reportMap.get(combKey)).split("~");
            if (!reportIds[0].equalsIgnoreCase("None")) {
                if (parentId.equalsIgnoreCase("")) {
                    updateQuery = "UPDATE PRG_AR_GRP_DRILL SET REF_REPORT_ID=" + reportIds[0] + ",REF_REPORT_TYPE='" + reportIds[1] + "' WHERE GROUP_ID=" + groupId + " AND CHILD_ELEMENT_ID=" + elementId + " AND DASHBOARD_ID=" + dbrdId + " AND DASHLET_ID=" + dashletId;
                } else {
                    updateQuery = "UPDATE PRG_AR_GRP_DRILL SET REF_REPORT_ID=" + reportIds[0] + ", REF_REPORT_TYPE='" + reportIds[1] + "' WHERE GROUP_ID=" + groupId + " AND PARENT_ELEMENT_ID=" + parentId + " AND CHILD_ELEMENT_ID=" + elementId + " AND DASHBOARD_ID=" + dbrdId + " AND DASHLET_ID=" + dashletId;
                }
                queryList.add(updateQuery);
            }
        }
        status = pbdb.executeMultiple(queryList);

        return status;

    }
    //Surender

    public PbReturnObject getOneviewFOrkpis(String foldersIds) {
        String getKpiDrillReportsQuery = getResourceBundle().getString("getOneviewForReport");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] kpiReps = new Object[2];
        kpiReps[0] = foldersIds.replace(",", "").trim();
        kpiReps[1] = foldersIds.replace(",", "").trim();
        try {
            finalQuery = buildQuery(getKpiDrillReportsQuery, kpiReps);

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
            retObj = null;
        }catch (Exception ex) {
            logger.error("Exception: ", ex);
            retObj = null;
        }
        return retObj;

    }
    //Surender

    public PbReturnObject getKpiDashboardForOneview(String foldersIds) {
        String getKpiDrillReportsQuery = getResourceBundle().getString("getKpisForOneview");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] kpiReps = new Object[2];
        kpiReps[0] = foldersIds.replace(",", "").trim();
        kpiReps[1] = foldersIds.replace(",", "").trim();
        try {
            finalQuery = buildQuery(getKpiDrillReportsQuery, kpiReps);

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        } catch (Exception ex) {
             logger.error("Exception: ", ex);
            retObj = null;
        }
        return retObj;
    }

    public Map<String, String> getcolors() {

        Map<String, String> backgroundcolor = new HashMap<String, String>();
        backgroundcolor.put("", "");
        backgroundcolor.put("Red", "#f24040");
        backgroundcolor.put("Green", "#9ACD32");
        backgroundcolor.put("Orange", "#FF9933");
        backgroundcolor.put("Blue", "#0000FF");
        backgroundcolor.put("Aqua", "#00FFFF");
        backgroundcolor.put("Charteuse", "#7FFF00");
        backgroundcolor.put("Black", "#000000");
        backgroundcolor.put("White", "#FFFFFF");
        backgroundcolor.put("Yellow", "#FFFF00");
        backgroundcolor.put("Violet", "#EE82EE");
        backgroundcolor.put("Purple", "#800080");
        backgroundcolor.put("LightBlue", "#ADD8E6");
        backgroundcolor.put("Silver", "#C0C0C0");
        backgroundcolor.put("Olive", "#808000");
        backgroundcolor.put("Light Slate Gray", "#6D7B8D");
        backgroundcolor.put("Slate Blue", "#737CA1");
        backgroundcolor.put("Lavender", "#E3E4FA");
        backgroundcolor.put("Pink3", "#C48793");
        backgroundcolor.put("Lemon Chiffon3", "#C9C299");
        backgroundcolor.put("Khaki", "#ADA96E");
        backgroundcolor.put("Bisque", "#FFE4C4");
        backgroundcolor.put("DarkGray", "#A9A9A9");
        backgroundcolor.put("PaleGreen", "#98FB98");
        backgroundcolor.put("indian red", "#B0171F");
        backgroundcolor.put("crimson", "#DC143C");
        backgroundcolor.put("Dark Goldenrod", "#b8860b");
        backgroundcolor.put("Wheat", "#f5deb3");
        backgroundcolor.put("Dark Orchid", "#9932cc");
        backgroundcolor.put("Thistle", "#d8bfd8");
        backgroundcolor.put("Aquamarine", "#66cdaa");
        backgroundcolor.put("Magenta", "#FF00FF");
        backgroundcolor.put("lightpink", "#FFB6C1");
        backgroundcolor.put("palevioletred", "#DB7093");
        backgroundcolor.put("violetred", "#FF3E96");
        backgroundcolor.put("plum", "FFBBFF");
        backgroundcolor.put("seashell", "#EEE5DE");
        backgroundcolor.put("Rosy Brown", "#bc8f8f");
        backgroundcolor.put("Salmon", "#e9967a");
        backgroundcolor.put("Coral", "#ff7f50");
        backgroundcolor.put("Lavender Blush", "#fff0f5");
        backgroundcolor.put("Cadet Blue", "#5f9ea0");
        backgroundcolor.put("default1", "#64B2FF");
        backgroundcolor.put("default2", "#B5F950");
        backgroundcolor.put("default3", "#33E6FA");
        backgroundcolor.put("default4", "#EFE662");
        backgroundcolor.put("default5", "#E1755F");
        backgroundcolor.put("default6", "#AAC749");
        backgroundcolor.put("default7", "#867D56");
        backgroundcolor.put("default8", "#C9A0DC");
        backgroundcolor.put("default9", "#0099CC");
        backgroundcolor.put("default10", "#8E5454");
        backgroundcolor.put("default11", "#FF9900");


        return backgroundcolor;
    }

    public HashMap getColorCodesWithRGB(HashMap colorCodesMap) {
        HashMap<String, String> rgbWithCodes = new HashMap<String, String>();
//             HashMap colorCodesMap=(HashMap)getcolors();
        Set keySet = colorCodesMap.keySet();
        Iterator keySetIter = keySet.iterator();

        int i = 0;
        String value = "";
        Color clr = null;
        int red = 0;
        int green = 0;
        int blue = 0;
        String rgb = "";
        int hexInt = 0;
        while (keySetIter.hasNext()) {
            value = colorCodesMap.get(keySetIter.next()).toString();
            if (value != null && !value.isEmpty()) {
                hexInt = Integer.valueOf(value.substring(1), 16).intValue();
            }
            red = (hexInt & 0xFF0000) >> 16;
            green = (hexInt & 0xFF00) >> 8;
            blue = (hexInt & 0xFF);
//                 clr=hex2Rgb(value);

            //                 if(clr != null){
//                    red=clr.getRed();
//                    green=clr.getGreen();
//                    blue=clr.getBlue();
            rgb = "rgb(R,G,B)";
            rgb = rgb.replace("R", String.valueOf(red));
            rgb = rgb.replace("G", String.valueOf(green));
            rgb = rgb.replace("B", String.valueOf(blue));
            rgbWithCodes.put(rgb, value);
//                     i++;
//                 }
        }
        return rgbWithCodes;
    }

    public Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 2), 16),
                Integer.valueOf(colorStr.substring(3, 4), 16),
                Integer.valueOf(colorStr.substring(4, 6), 16));
    }

    public String getMeasuresForFormating(String icalId, HttpSession session) {
        String selectQry = getResourceBundle().getString("getMeasuresForFormating");
        String selectPrimQry = getResourceBundle().getString("getPrimaryMeasure");
        Object[] icalId1 = new Object[1];
        icalId1[0] = icalId;
        List<String> idList = new ArrayList<String>();
        LinkedHashMap map = new LinkedHashMap();
        List<String> nameList = new ArrayList<String>();
        List<String> displaynameList = new ArrayList<String>();
        List<String> formatList = new ArrayList<String>();
        List<String> roundingList = new ArrayList<String>();
        String primaryMeasure = "";
        PbReturnObject retObj = new PbReturnObject();
        PbReturnObject retObj1 = new PbReturnObject();
        String jsonString = "";
        OnceViewContainer container = null;
        if (icalId != null && !icalId.equalsIgnoreCase("")) {
            HashMap map2 = new HashMap();
            if (session.getAttribute("ICALDETAILS") != null) {
                map2 = (HashMap) session.getAttribute("ICALDETAILS");
                container = (OnceViewContainer) map2.get(icalId);
            }
        }
        String formatList1 = "";
        String roundList = "";
        String[] formatListVals = null;
        String[] roundListVals = null;
        formatList1 = (String) session.getAttribute("formatList");
        roundList = (String) session.getAttribute("roundList");
        if (formatList1 != null && !formatList1.equalsIgnoreCase("") && roundList != null && !roundList.equalsIgnoreCase("")) {
            formatListVals = formatList1.split(",");
            roundListVals = roundList.split(",");
        }
        try {
            retObj = execSelectSQL(buildQuery(selectQry, icalId1));
            retObj1 = execSelectSQL(buildQuery(selectPrimQry, icalId1));
            if (retObj != null) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    idList.add(retObj.getFieldValueString(i, "MEASURE_ID"));
                    nameList.add(retObj.getFieldValueString(i, "ELEMENT_ID_NAME"));
                    displaynameList.add(retObj.getFieldValueString(i, "MEASURE_DISPLAY_NAME"));
                    formatList.add(retObj.getFieldValueString(i, "DISPLAY_FORMAT"));
                    roundingList.add(retObj.getFieldValueString(i, "ROUND_VALUE"));
                }
                if (retObj1 != null) {
                    primaryMeasure = retObj1.getFieldValueString(0, "PRIMARY_MEASURE");
                }
                if (formatListVals != null && roundListVals != null) {
                    formatList = new ArrayList<String>();
                    roundingList = new ArrayList<String>();
                    for (int i = 0; i < formatListVals.length; i++) {
                        formatList.add(formatListVals[i]);
                        roundingList.add(roundListVals[i]);
                    }
                }
                map.put("idList", idList);
                map.put("nameList", nameList);
                map.put("displaynameList", displaynameList);
                map.put("formatList", formatList);
                map.put("roundingList", roundingList);
                map.put("primaryMeasure", primaryMeasure);
                Gson gson = new Gson();
                jsonString = gson.toJson(map);
            }
        }catch (SQLException ex) {
            logger.error("Exception", ex);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return jsonString;
        }

    public String getJqPlotKpiGraphType(String dashletid) throws Exception {
        String selectQuery = "select JQ_PROPERTIES from PRG_AR_KPI_GRAPH_DETAILS where dashboarddet_id=&";
        Object[] jqobj = new Object[1];
        jqobj[0] = dashletid;
        String jqgraphtype = null;
        String finalQuery = this.buildQuery(selectQuery, jqobj);
        PbReturnObject retObj = execSelectSQL(finalQuery);
        if (retObj.getRowCount() != 0) {
            jqgraphtype = retObj.getFieldValueString(0, "JQ_PROPERTIES");
        }

        return jqgraphtype;
    }

    public String getLastUpdatedDate(Connection con, String elementID) {
        String date = "";
        PbReturnObject returnObject = null;
//        if(con!=null){
        String query = "";
        GetConnectionType gettypeofconn = new GetConnectionType();
        String connType = gettypeofconn.getConTypeByElementId(elementID);
        if (connType.equalsIgnoreCase("sqlserver")) {
            query = "select Last_Update_Date from PRG_Last_Update_Table";
        } else {
            if (connType.equalsIgnoreCase("Mysql")) {
                query = "SELECT * FROM Prg_Last_Update_Table";
            } else {
                query = "SELECT TO_CHAR(Last_Update_Date, 'MM/DD/yyyy hh24:mi:ss') FROM Prg_Last_Update_Table";
            }
        }

        try {
            returnObject = execSelectSQL(query, con);
            if (returnObject != null && returnObject.getRowCount() > 0) {
                date = returnObject.getFieldValueString(0, 0);
            }
        } 
        catch (Exception ex) {
            logger.error("Exception", ex);
        }
//        }

        return date;
    }

    public String getBuildOneviewComplexKPI(String[] kpisArray, HttpServletRequest request, String oneviewId, String viewLetId, String height, String width) throws Exception {
        StringBuilder createKPIBuilder = new StringBuilder();
        String sqlQuery = getResourceBundle().getString("getCreateKPIDetails");
        Gson gson = new Gson();
        String[] kpiName = new String[kpisArray.length];
        String[] kpiId = new String[kpisArray.length];
        String[] kpiValue = new String[kpisArray.length];
        String[] aggType = new String[kpisArray.length];
        String ReportKpiId = "";
        Object[] obj = new Object[1];
        PbReturnObject retObj = new PbReturnObject();
        PbReturnObject kpiRetObj = null;
        CreateKPIFromReport kPIFromReport = new CreateKPIFromReport();
        String createKPIString = null;
        ArrayList<CreateKPIFromReport> createKPIList = new ArrayList<CreateKPIFromReport>();
        for (int i = 0; i < kpisArray.length; i++) {
            obj[0] = kpisArray[i];
            try {
                retObj = execSelectSQL(buildQuery(sqlQuery, obj));
                if (retObj.getRowCount() > 0) {
                    Type listType = new TypeToken<CreateKPIFromReport>() {
                    }.getType();
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        createKPIString = retObj.getFieldUnknown(0, 0);
                    } else {
                        createKPIString = retObj.getFieldValueClobString(0, "CREATE_KPI_DATA");
                    }
                    kPIFromReport = gson.fromJson(createKPIString, CreateKPIFromReport.class);
                    createKPIList.add(kPIFromReport);
                    kpiName[i] = kPIFromReport.getReportKPIName();
                    ReportKpiId = kPIFromReport.getMeasureId();
                    String totalvalue = kPIFromReport.getTotalValue();
                    String pagesperSlide = kPIFromReport.getPagesPerSlide();
                    // 
                    kpiRetObj = getOneViewComplexKPIReturnObject(kPIFromReport, oneviewId, request, viewLetId, height, width, kpisArray);
                    if (kPIFromReport.getDynamicTimeDetails() != null && kPIFromReport.getDynamicTimeDetails().equalsIgnoreCase("static") && kPIFromReport.getAggTypeValue() != null) {
                        kpiValue[i] = NumberFormatter.getModifiedNumber(kPIFromReport.getAggTypeValue(), "");
                    } else {
                        if (kPIFromReport.getAggType().equalsIgnoreCase("avg")) {
                            int rows = 0;
                            //kpiValue[i]= NumberFormatter.getModifiedNumber(kpiRetObj.getColumnAverageValue(kPIFromReport.getMeasureId()), "");
                            // kpiValue[i]=String.valueOf(kpiRetObj.getColumnAverageValue(kPIFromReport.getMeasureId()));
                            if (kPIFromReport.isTopBottomSet()) {
                                rows = kpiRetObj.getRowCount();
                            } else if (pagesperSlide != null && !pagesperSlide.equalsIgnoreCase("")) {
                                rows = Integer.parseInt(pagesperSlide);
                            } else {
                                rows = kpiRetObj.getRowCount();
                            }
                            if (totalvalue != null && totalvalue.equalsIgnoreCase("grandtotalDet")) {
                                rows = kpiRetObj.getRowCount();
                            }
//                     kpiValue[i]= NumberFormatter.getModifiedNumber(kpiRetObj.getColumnAverageValue(kPIFromReport.getMeasureId()), "");
                            // kpiValue[i]=String.valueOf(kpiRetObj.getColumnAverageValue(kPIFromReport.getMeasureId()));
                            double averageSum = 0.0;
                            for (int j = 0; j < kpiRetObj.getRowCount(); j++) {
                                averageSum = averageSum + Double.parseDouble(kpiRetObj.getFieldValueString(j, kPIFromReport.getMeasureId()));
                            }
                            BigDecimal curval = new BigDecimal(averageSum);
                            BigDecimal val = curval;
                            int decimalPlaces = 0;
                            curval = curval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                            NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                            ArrayList alist1 = new ArrayList();
                            ArrayList alist2 = new ArrayList();
                            Long val2 = new Long("0");
                            alist1 = kpiRetObj.retrieveDataBasedOnViewSeq(kPIFromReport.getMeasureId());
                            if (alist1.size() < rows) {
                                rows = alist1.size();
                            }
                            for (int j = 0; j < rows; j++) {
                                if (alist1 != null && alist1.get(j) != null) {
                                    String myval = NumberFormatter.getModifiedNumber(new BigDecimal(alist1.get(j).toString()), "", 0);
                                    val2 = val2 + Long.parseLong(myval.replace(",", ""));
                                }
                            }
                            String avgVal = NumberFormatter.getModifiedNumber(new BigDecimal(val2), "");
                            String avgVal1 = String.valueOf(Float.parseFloat(avgVal.replace(",", "")) / kpiRetObj.getViewSequence().size());
                            kpiValue[i] = (avgVal1);
                        } else if (kPIFromReport.getAggType().equalsIgnoreCase("sum")) {
                            ArrayList alist1 = new ArrayList();
                            ArrayList alist2 = new ArrayList();
                            Long val = new Long("0");
                            int rows = 0;
                            if (kPIFromReport.isTopBottomSet()) {
                                rows = kpiRetObj.getRowCount();
                            } else if (pagesperSlide != null && !pagesperSlide.equalsIgnoreCase("")) {
                                rows = Integer.parseInt(pagesperSlide);
                            } else {
                                rows = kpiRetObj.getRowCount();
                            }
                            if (totalvalue != null && totalvalue.equalsIgnoreCase("grandtotalDet")) {
                                rows = kpiRetObj.getRowCount();
                            }
                            alist1 = kpiRetObj.retrieveDataBasedOnViewSeq(kPIFromReport.getMeasureId());
                            if (alist1.size() < rows) {
                                rows = alist1.size();
                            }
                            for (int j = 0; j < rows; j++) {
                                if (alist1 != null && alist1.get(j) != null) {
                                    String myval = NumberFormatter.getModifiedNumber(new BigDecimal(alist1.get(j).toString()), "", 0);
                                    val = val + Long.parseLong(myval.replace(",", ""));
                                }
                            }
                            kpiValue[i] = NumberFormatter.getModifiedNumber(new BigDecimal(val), "");

                        } else if (kPIFromReport.getAggType().equalsIgnoreCase("min") && kpiRetObj.getColumnMinimumValue(kPIFromReport.getMeasureId()) != null && !kpiRetObj.getColumnMinimumValue(kPIFromReport.getMeasureId()).toString().equalsIgnoreCase("")) {
                            kpiValue[i] = NumberFormatter.getModifiedNumber(kpiRetObj.getColumnMinimumValue(kPIFromReport.getMeasureId()), "");
                        } else if (kPIFromReport.getAggType().equalsIgnoreCase("max") && kpiRetObj.getColumnMaximumValue(kPIFromReport.getMeasureId()) != null && !kpiRetObj.getColumnMaximumValue(kPIFromReport.getMeasureId()).toString().equalsIgnoreCase("")) {
                            kpiValue[i] = NumberFormatter.getModifiedNumber(kpiRetObj.getColumnMaximumValue(kPIFromReport.getMeasureId()), "");
                        } else if (kPIFromReport.getAggType().equalsIgnoreCase("count") && kpiRetObj.getRowCount() > 0) {
                            kpiValue[i] = String.valueOf(kpiRetObj.getViewSequence().size());
                        } else {
                            kpiValue[i] = "--";
                        }
                    }
                }
            } catch (SQLException ex) {
                logger.error("Exception", ex);
            }catch (Exception ex) {
                logger.error("Exception", ex);
            }
        }
        String dashletId = request.getParameter("divId");
        String dashboardId = request.getParameter("dashboardId");
        OneViewLetDetails detail = new OneViewLetDetails();
        HttpSession session = request.getSession(false);
        HashMap map = null;
        OnceViewContainer onecontainer = new OnceViewContainer();
        if (session != null) {

            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneviewId);
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(viewLetId));
            }
        }
        List<String> tiemdetails = new ArrayList<String>();
        tiemdetails = (List<String>) request.getAttribute("complexKPITimeDetails");

//       if(container!=null){
//       pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
//        DashletDetail dashlet = collect.getDashletDetail(dashletId);
//      if(dashlet!=null)
//      {
//            dashlet.setDashBoardDetailId(dashletId);
//            dashlet.setKpiType("Complexkpi");
//             dashlet.setDisplayType(DashboardConstants.KPI_REPORT);
//             KPI kpidetails=new KPI();
//
//             List<String> SelectedkpiIds =Arrays.asList(kpisArray);
//             kpidetails.setElementIds(SelectedkpiIds);
//             dashlet.setReportDetails(kpidetails);
//             for(int i=0;i<kpiName.length;i++){
//            kpidetails.addelementNames(kpiId[i],kpiName[i]);
//                }
//
//            }
//        }

        KPIBuilder kpibuilder = new KPIBuilder();
        String complexkpi = kpibuilder.processComplexKpiForOneview(kpiName, kpiValue, kpisArray, detail, "value", "value", kPIFromReport, viewLetId);
        return complexkpi;

    }

    public PbReturnObject getOneViewComplexKPIReturnObject(CreateKPIFromReport kPIFromReport, String oneviewId, HttpServletRequest request, String viewLetId, String height, String width, String[] kpisArray) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        HashMap map = null;
        ArrayList rowViewBys = new ArrayList(Arrays.asList(kPIFromReport.getRowViewBys()));
        ArrayList reportQryCols = new ArrayList(Arrays.asList(kPIFromReport.getReportQryCols()));
        ArrayList qryAggregations = new ArrayList(Arrays.asList(kPIFromReport.getQryAggregations()));
        PbReturnObject returnObject = new PbReturnObject();
        ArrayList timeDetailsArray = new ArrayList();
        OnceViewContainer onecontainer = new OnceViewContainer();
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        ArrayList alisty1 = new ArrayList();
        ArrayList alisty2 = new ArrayList();
        if (session != null) {

            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneviewId);


                OneViewLetDetails detail = new OneViewLetDetails();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(viewLetId));
                timeDetailsArray = (ArrayList) onecontainer.timedetails;
                alisty1.addAll(timeDetailsArray);
                Object[] timeDetaArr = kPIFromReport.getTimeDetailsArray();
                if (!alisty1.isEmpty() && alisty1.size() > 0 && kPIFromReport.getDynamicTimeDetails() != null && kPIFromReport.getDynamicTimeDetails().equalsIgnoreCase("dynamic")) {
                    if (kPIFromReport.getOtherTimeDetails() != null && kPIFromReport.getOtherTimeDetails().equalsIgnoreCase("repTimeDet")) {
                        alisty1.set(0, timeDetaArr[0]);
                        alisty1.set(1, timeDetaArr[1]);
                        alisty1.set(2, timeDetaArr[2]);
                        alisty1.set(3, timeDetaArr[3]);
                        alisty1.set(4, timeDetaArr[4]);
                    }
                }

                detail.setHeight(Integer.parseInt(height));
                detail.setWidth(Integer.parseInt(width));
                detail.setReptype("complexkpi");
                detail.setComplexKpiMeasureId(kpisArray);
                detail.setComplexMeasureName(kPIFromReport.getReportKPIName());
                detail.setComplexdispMeasures(kPIFromReport.getDispMeasures());
                detail.setRepId(kpisArray[0]);
                detail.setRepName(kPIFromReport.getReportKPIName());
                detail.setRoleId(kPIFromReport.getBizRoles()[0]);
                detail.setComplexrowviewbys(rowViewBys);
                detail.setComplexreportQryCols(reportQryCols);
                detail.setComplexqryAggregations(qryAggregations);
                detail.setComplexparamValue((HashMap) kPIFromReport.getReportParametersValues());
                detail.setKpiFromReport(kPIFromReport);
            } else {
                OneViewLetDetails detail = null;

                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
                String fileName = request.getSession(false).getAttribute("tempFileName").toString();
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();

                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(viewLetId));
                timeDetailsArray = (ArrayList) onecontainer.timedetails;
                alisty1.addAll(timeDetailsArray);
                Object[] timeDetaArr = kPIFromReport.getTimeDetailsArray();
                if (!alisty1.isEmpty() && alisty1.size() > 0 && kPIFromReport.getDynamicTimeDetails() != null && kPIFromReport.getDynamicTimeDetails().equalsIgnoreCase("dynamic")) {
                    if (kPIFromReport.getOtherTimeDetails() != null && kPIFromReport.getOtherTimeDetails().equalsIgnoreCase("repTimeDet")) {
                        alisty1.set(0, timeDetaArr[0]);
                        alisty1.set(1, timeDetaArr[1]);
                        alisty1.set(2, timeDetaArr[2]);
                        alisty1.set(3, timeDetaArr[3]);
                        alisty1.set(4, timeDetaArr[4]);
                    }
                }
                detail.setHeight(Integer.parseInt(height));
                detail.setWidth(Integer.parseInt(width));
                detail.setReptype("complexkpi");
                detail.setComplexKpiMeasureId(kpisArray);
                detail.setComplexdispMeasures(kPIFromReport.getDispMeasures());
                detail.setComplexMeasureName(kPIFromReport.getReportKPIName());
                detail.setRepId(kpisArray[0]);
                detail.setRepName(kPIFromReport.getReportKPIName());
                detail.setRoleId(kPIFromReport.getBizRoles()[0]);
                detail.setComplexrowviewbys(rowViewBys);
                detail.setComplexreportQryCols(reportQryCols);
                detail.setComplexqryAggregations(qryAggregations);
                detail.setComplexparamValue((HashMap) kPIFromReport.getReportParametersValues());
                detail.setKpiFromReport(kPIFromReport);

                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();

            }
        }
        ArrayList arl = new ArrayList();
        Object[] timeDetaArr = kPIFromReport.getTimeDetailsArray();
        arl.add("Day");
        arl.add("PRG_STD");
        ProgenParam pramnam = new ProgenParam();
        String date = pramnam.getdateforpage();
        arl.add(date);
        arl.add("Month");
        arl.add("Last Period");

        alisty2.addAll(arl);
        if (kPIFromReport.getDynamicTimeDetails() != null && kPIFromReport.getDynamicTimeDetails().equalsIgnoreCase("dynamic")) {
            if (kPIFromReport.getOtherTimeDetails() != null && kPIFromReport.getOtherTimeDetails().equalsIgnoreCase("repTimeDet")) {
                alisty2.set(0, timeDetaArr[0]);
                alisty2.set(1, timeDetaArr[1]);
                alisty2.set(2, timeDetaArr[2]);
                alisty2.set(3, timeDetaArr[3]);
                alisty2.set(4, timeDetaArr[4]);
            }
        }


        if (onecontainer.timedetails != null && !onecontainer.timedetails.isEmpty()) {
            request.setAttribute("OneviewTiemDetails", onecontainer.timedetails);
        } else {
            onecontainer.timedetails = arl;
        }

        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject newReturnObj = new PbReturnObject();
        PbReportTableBD reportTableBD = new PbReportTableBD();

        ArrayList sortColumns = null;
        if (kPIFromReport.getSortColumns() != null) {
            sortColumns = new ArrayList(Arrays.asList(kPIFromReport.getSortColumns()));
        }

        ArrayList dataSeq = null;
        repQuery.setRowViewbyCols(rowViewBys);
        repQuery.setParamValue((HashMap) kPIFromReport.getReportParametersValues());//Added by k
        repQuery.setColViewbyCols(new ArrayList());
        repQuery.setQryColumns(reportQryCols);
        repQuery.setColAggration(qryAggregations);
        repQuery.setBizRoles(kPIFromReport.getBizRoles()[0]);
        repQuery.setUserId(session.getAttribute("USERID").toString());
        //repQuery.setTimeDetails(timeDetailsArray);

        if ((List<String>) request.getAttribute("OneviewTiemDetails") != null) {
            repQuery.setTimeDetails(alisty1);
            request.setAttribute("complexKPITimeDetails", alisty1);
        } else {
            repQuery.setTimeDetails(alisty2);
            request.setAttribute("complexKPITimeDetails", alisty2);
        }
        PbReportCollection collect = new PbReportCollection();
        collect.reportQryElementIds = new ArrayList(Arrays.asList(kPIFromReport.getReportQryCols()));
        collect.reportQryAggregations = new ArrayList(Arrays.asList(kPIFromReport.getQryAggregations()));
        collect.reportRowViewbyValues = new ArrayList(Arrays.asList(kPIFromReport.getRowViewBys()));
        collect.reportColViewbyValues = new ArrayList(Arrays.asList(kPIFromReport.getColViewBys()));
//            collect.reportParametersValues = kPIFromReport.getReportParametersValues();
        if ((List<String>) request.getAttribute("OneviewTiemDetails") != null) {
            collect.timeDetailsArray = alisty1;
        } else {
            collect.timeDetailsArray = alisty2;
        }
        collect.reportId = kPIFromReport.getReportId();
        String totalvalue = kPIFromReport.getTotalValue();
        ArrayList innerViewBy = repQuery.getInnerViewBy(collect.reportId);
        if (innerViewBy != null && innerViewBy.size() > 0) {
            repQuery.isInnerViewBy = true;
            for (int iLoop = 0; iLoop < innerViewBy.size(); iLoop++) {
                if ((!collect.reportRowViewbyValues.contains(innerViewBy.get(iLoop))) && (!collect.reportColViewbyValues.contains(innerViewBy.get(iLoop)))) {
                    collect.reportRowViewbyValues.add((String) innerViewBy.get(iLoop));
                } else {
                    innerViewBy.remove(iLoop);
                }

            }

        }
        String query = "select  REF_ELEMENT_TYPE , AGGREGATION_TYPE from  PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + collect.reportQryElementIds.get(0);
        PbDb pbdb = new PbDb();
        PbReturnObject retobj = pbdb.execSelectSQL(query);
        if ((retobj.getFieldValueString(0, "REF_ELEMENT_TYPE").equalsIgnoreCase("4") || retobj.getFieldValueString(0, "AGGREGATION_TYPE").equalsIgnoreCase("avg")) && totalvalue != null && totalvalue.equalsIgnoreCase("grandtotalDet") && !totalvalue.equalsIgnoreCase("subtotalDet")) {
            returnObject = this.getKpiRetObjforgrandtotals(collect, session.getAttribute("USERID").toString(), kPIFromReport);
        } else {
            returnObject = repQuery.getPbReturnObject(String.valueOf(reportQryCols.get(0)));
        }

        reportTableBD.searchDataSet(kPIFromReport, returnObject);
        if (totalvalue != null && !totalvalue.equalsIgnoreCase("grandtotalDet") && totalvalue.equalsIgnoreCase("subtotalDet")) {
            if (kPIFromReport.isTopBottomSet()) {
                if (kPIFromReport.getTopBtmMode().equalsIgnoreCase("TopBottomAbsRows")) {
                    dataSeq = returnObject.findTopBottom(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
                } else {
                    dataSeq = returnObject.findTopBottomPercentWise(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
                }
                returnObject.setViewSequence(dataSeq);
            }
        } else if (totalvalue == null) {
            if (kPIFromReport.isTopBottomSet()) {
                if (kPIFromReport.getTopBtmMode().equalsIgnoreCase("TopBottomAbsRows")) {
                    dataSeq = returnObject.findTopBottom(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
                } else {
                    dataSeq = returnObject.findTopBottomPercentWise(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
                }
                returnObject.setViewSequence(dataSeq);
            }
        }

        returnObject.setProcessGT(true);
        returnObject.prepareObject(returnObject, dataSeq);
        return returnObject;
    }

    public String getBuildOneviewComplexKPIData(String[] kpisArray, HttpServletRequest request, String oneviewId, String viewLetId, String height, String width, OneViewLetDetails oneviewlet, String valu, String val1) throws Exception {
        StringBuilder createKPIBuilder = new StringBuilder();
        String sqlQuery = getResourceBundle().getString("getCreateKPIDetails");
        Gson gson = new Gson();
        String[] kpiName = new String[kpisArray.length];
        String[] kpiId = new String[kpisArray.length];
        String[] kpiValue = new String[kpisArray.length];
        String[] aggType = new String[kpisArray.length];
        String ReportKpiId = "";
        String dashletId = request.getParameter("divId");
        String dashboardId = request.getParameter("dashboardId");
        HttpSession session = request.getSession(false);
        HashMap map = null;
        OnceViewContainer onecontainer = new OnceViewContainer();
        if (session != null) {

            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneviewId);
            }
        }
        Object[] obj = new Object[1];
        PbReturnObject retObj = new PbReturnObject();
        PbReturnObject kpiRetObj = null;
        CreateKPIFromReport kPIFromReport = new CreateKPIFromReport();
        String createKPIString = null;
        ArrayList<CreateKPIFromReport> createKPIList = new ArrayList<CreateKPIFromReport>();
        for (int i = 0; i < kpisArray.length; i++) {
            obj[0] = kpisArray[i];
            try {
                retObj = execSelectSQL(buildQuery(sqlQuery, obj));
                if (retObj.getRowCount() > 0) {
                    Type listType = new TypeToken<CreateKPIFromReport>() {
                    }.getType();
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        createKPIString = retObj.getFieldUnknown(0, 0);
                    } else {
                        createKPIString = retObj.getFieldValueClobString(0, "CREATE_KPI_DATA");
                    }
                    kPIFromReport = gson.fromJson(createKPIString, CreateKPIFromReport.class);
                    createKPIList.add(kPIFromReport);
                    kpiName[i] = kPIFromReport.getReportKPIName();
                    ReportKpiId = kPIFromReport.getMeasureId();
                    String totalvalue = kPIFromReport.getTotalValue();
                    String pagesperSlide = kPIFromReport.getPagesPerSlide();
                    Object[] timeDetailsArray = kPIFromReport.getTimeDetailsArray();
                    kpiRetObj = getOneViewComplexKPIReturnObjectData(kPIFromReport, oneviewId, request, viewLetId, height, width, kpisArray);
                    if (kPIFromReport.getDynamicTimeDetails() != null && kPIFromReport.getDynamicTimeDetails().equalsIgnoreCase("static") && kPIFromReport.getAggTypeValue() != null) {
                        kpiValue[i] = NumberFormatter.getModifiedNumber(kPIFromReport.getAggTypeValue(), "");
                    } else {
                        if (kPIFromReport.getAggType().equalsIgnoreCase("avg")) {
                            int rows = 0;
                            //kpiValue[i]= NumberFormatter.getModifiedNumber(kpiRetObj.getColumnAverageValue(kPIFromReport.getMeasureId()), "");
                            // kpiValue[i]=String.valueOf(kpiRetObj.getColumnAverageValue(kPIFromReport.getMeasureId()));
                            if (kPIFromReport.isTopBottomSet()) {
                                rows = kpiRetObj.getRowCount();
                            } else if (pagesperSlide != null && !pagesperSlide.equalsIgnoreCase("")) {
                                rows = Integer.parseInt(pagesperSlide);
                            } else {
                                rows = kpiRetObj.getRowCount();
                            }
                            if (totalvalue != null && totalvalue.equalsIgnoreCase("grandtotalDet")) {
                                rows = kpiRetObj.getRowCount();
                            }
                            double averageSum = 0.0;
                            for (int j = 0; j < rows; j++) {
                                averageSum = averageSum + Double.parseDouble(kpiRetObj.getFieldValueString(j, kPIFromReport.getMeasureId()));
                            }
                            BigDecimal curval = new BigDecimal(averageSum);
                            BigDecimal val = curval;
                            int decimalPlaces = 0;
                            curval = curval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                            NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                            ArrayList alist1 = new ArrayList();
                            ArrayList alist2 = new ArrayList();
                            Long val12 = new Long("0");
                            if (totalvalue != null && totalvalue.equalsIgnoreCase("grandtotalDet")) {
                                rows = kpiRetObj.getRowCount();
                            }
                            alist1 = kpiRetObj.retrieveDataBasedOnViewSeq(kPIFromReport.getMeasureId());
                            if (alist1.size() < rows) {
                                rows = alist1.size();
                            }
                            for (int j = 0; j < rows; j++) {
                                if (alist1 != null && alist1.get(j) != null) {
                                    String myval = NumberFormatter.getModifiedNumber(new BigDecimal(alist1.get(j).toString()), "", 0);
                                    val12 = val12 + Long.parseLong(myval.replace(",", ""));
                                }
                            }
                            String avgVal = NumberFormatter.getModifiedNumber(new BigDecimal(val12), "");
                            avgVal = String.valueOf(Float.parseFloat(avgVal.replace(",", "")) / kpiRetObj.getViewSequence().size());
                            kpiValue[i] = (avgVal);
                        } else if (kPIFromReport.getAggType().equalsIgnoreCase("sum")) {
                            ArrayList alist1 = new ArrayList();
                            ArrayList alist2 = new ArrayList();
                            Long val2 = new Long("0");

                            int rows = 0;
                            if (kPIFromReport.isTopBottomSet()) {
                                rows = kpiRetObj.getRowCount();
                            } else if (pagesperSlide != null && !pagesperSlide.equalsIgnoreCase("")) {
                                rows = Integer.parseInt(pagesperSlide);
                            } else {
                                rows = kpiRetObj.getRowCount();
                            }
                            if (totalvalue != null && totalvalue.equalsIgnoreCase("grandtotalDet")) {
                                rows = kpiRetObj.getRowCount();
                            }
                            alist1 = kpiRetObj.retrieveDataBasedOnViewSeq(kPIFromReport.getMeasureId());
                            if (alist1.size() < rows) {
                                rows = alist1.size();
                            }
                            for (int j = 0; j < rows; j++) {
                                if (alist1 != null && alist1.get(j) != null) {
                                    String myval = NumberFormatter.getModifiedNumber(new BigDecimal(alist1.get(j).toString()), "", 0);
                                    val2 = val2 + Long.parseLong(myval.replace(",", ""));
                                }
                            }
                            kpiValue[i] = NumberFormatter.getModifiedNumber(new BigDecimal(val2), "");
                        } else if (kPIFromReport.getAggType().equalsIgnoreCase("min") && kpiRetObj.getColumnMinimumValue(kPIFromReport.getMeasureId()) != null && !kpiRetObj.getColumnMinimumValue(kPIFromReport.getMeasureId()).toString().equalsIgnoreCase("")) {
                            kpiValue[i] = NumberFormatter.getModifiedNumber(kpiRetObj.getColumnMinimumValue(kPIFromReport.getMeasureId()), "");
                        } else if (kPIFromReport.getAggType().equalsIgnoreCase("max") && kpiRetObj.getColumnMaximumValue(kPIFromReport.getMeasureId()) != null && !kpiRetObj.getColumnMaximumValue(kPIFromReport.getMeasureId()).toString().equalsIgnoreCase("")) {
                            kpiValue[i] = NumberFormatter.getModifiedNumber(kpiRetObj.getColumnMaximumValue(kPIFromReport.getMeasureId()), "");
                        } else if (kPIFromReport.getAggType().equalsIgnoreCase("count") && kpiRetObj.getRowCount() > 0) {
                            kpiValue[i] = String.valueOf(kpiRetObj.getViewSequence().size());
                        } else {
                            kpiValue[i] = "--";
                        }
                    }
                }
            } catch (SQLException ex) {
                logger.info("Exception: ", ex);
            } catch (Exception ex) {
                logger.info("Exception: ", ex);
            }
        }
        if (!kpiValue[0].contains("--")) {
            BigDecimal value = new BigDecimal(kpiValue[0].replace(",", "").replace("--", ""));
            if (oneviewlet.getFormatVal() != null && oneviewlet.getRoundVal() != null) {
                kpiValue[0] = NumberFormatter.getModifiedNumber(value, oneviewlet.getFormatVal(), Integer.parseInt(oneviewlet.getRoundVal()));
            } else {
                kpiValue[0] = NumberFormatter.getModifiedNumber(value, "", 0);
            }
        }
        oneviewlet.setCustomKpiVal(kpiValue[0]);
        List<String> tiemdetails = new ArrayList<String>();
        tiemdetails = (List<String>) request.getAttribute("complexKPITimeDetails");
        KPIBuilder kpibuilder = new KPIBuilder();
        String complexkpi = "";
        if (oneviewlet.ispdfEnabled) {
            complexkpi = kpiValue[0];
            oneviewlet.ispdfEnabled = false;
        } else {
            complexkpi = kpibuilder.processComplexKpiForOneviewData(kpiName, kpiValue, kpisArray, oneviewlet, valu, val1, kPIFromReport, viewLetId, tiemdetails);
        }
        //
        return complexkpi;
    }

    public PbReturnObject getOneViewComplexKPIReturnObjectData(CreateKPIFromReport kPIFromReport, String oneviewId, HttpServletRequest request, String viewLetId, String height, String width, String[] kpisArray) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        HashMap map = null;
        ArrayList rowViewBys = new ArrayList(Arrays.asList(kPIFromReport.getRowViewBys()));
        ArrayList reportQryCols = new ArrayList(Arrays.asList(kPIFromReport.getReportQryCols()));
        ArrayList qryAggregations = new ArrayList(Arrays.asList(kPIFromReport.getQryAggregations()));
        PbReturnObject returnObject = new PbReturnObject();
        ArrayList timeDetailsArray = new ArrayList();
        OnceViewContainer onecontainer = new OnceViewContainer();
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        ArrayList alisty1 = new ArrayList();
        ArrayList alisty2 = new ArrayList();
        if (session != null) {

            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneviewId);

                OneViewLetDetails detail = new OneViewLetDetails();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(viewLetId));
                timeDetailsArray = (ArrayList) onecontainer.timedetails;
                alisty1.addAll(timeDetailsArray);
                Object[] timeDetaArr = kPIFromReport.getTimeDetailsArray();
                if (!alisty1.isEmpty() && alisty1.size() > 0 && kPIFromReport.getDynamicTimeDetails() != null && kPIFromReport.getDynamicTimeDetails().equalsIgnoreCase("dynamic")) {
                    if (kPIFromReport.getOtherTimeDetails() != null && kPIFromReport.getOtherTimeDetails().equalsIgnoreCase("repTimeDet")) {
                        alisty1.set(0, timeDetaArr[0]);
                        alisty1.set(1, timeDetaArr[1]);
                        alisty1.set(2, timeDetaArr[2]);
                        alisty1.set(3, timeDetaArr[3]);
                        alisty1.set(4, timeDetaArr[4]);
                    }
                }
                detail.setHeight(Integer.parseInt(height));
                detail.setWidth(Integer.parseInt(width));
                detail.setReptype("complexkpi");
                detail.setComplexKpiMeasureId(kpisArray);
                detail.setComplexMeasureName(kPIFromReport.getReportKPIName());
                detail.setRepId(kpisArray[0]);
                detail.setComplexdispMeasures(kPIFromReport.getDispMeasures());
                detail.setRepName(kPIFromReport.getReportKPIName());
                detail.setRoleId(kPIFromReport.getBizRoles()[0]);
                detail.setComplexrowviewbys(rowViewBys);
                detail.setComplexreportQryCols(reportQryCols);
                detail.setComplexqryAggregations(qryAggregations);
                detail.setComplexparamValue((HashMap) kPIFromReport.getReportParametersValues());
                detail.setKpiFromReport(kPIFromReport);
            } else {
                OneViewLetDetails detail = null;

                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
                String fileName = request.getSession(false).getAttribute("tempFileName").toString();
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();

                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(viewLetId));
                timeDetailsArray = (ArrayList) onecontainer.timedetails;
                alisty1.addAll(timeDetailsArray);
                Object[] timeDetaArr = kPIFromReport.getTimeDetailsArray();
                if (!alisty1.isEmpty() && alisty1.size() > 0 && kPIFromReport.getDynamicTimeDetails() != null && kPIFromReport.getDynamicTimeDetails().equalsIgnoreCase("dynamic")) {
                    if (kPIFromReport.getOtherTimeDetails() != null && kPIFromReport.getOtherTimeDetails().equalsIgnoreCase("repTimeDet")) {
                        alisty1.set(0, timeDetaArr[0]);
                        alisty1.set(1, timeDetaArr[1]);
                        alisty1.set(2, timeDetaArr[2]);
                        alisty1.set(3, timeDetaArr[3]);
                        alisty1.set(4, timeDetaArr[4]);
                    }
                }
                detail.setHeight(Integer.parseInt(height));
                detail.setWidth(Integer.parseInt(width));
                detail.setReptype("complexkpi");
                detail.setComplexKpiMeasureId(kpisArray);
                detail.setComplexdispMeasures(kPIFromReport.getDispMeasures());
                detail.setComplexMeasureName(kPIFromReport.getReportKPIName());
                detail.setRepId(kpisArray[0]);
                detail.setRepName(kPIFromReport.getReportKPIName());
                detail.setRoleId(kPIFromReport.getBizRoles()[0]);
                detail.setComplexrowviewbys(rowViewBys);
                detail.setComplexreportQryCols(reportQryCols);
                detail.setComplexqryAggregations(qryAggregations);
                detail.setComplexparamValue((HashMap) kPIFromReport.getReportParametersValues());
                detail.setKpiFromReport(kPIFromReport);

                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();

            }
        }
        ArrayList arl = new ArrayList();
        Object[] timeDetaArr = kPIFromReport.getTimeDetailsArray();
        arl.add("Day");
        arl.add("PRG_STD");
        ProgenParam pramnam = new ProgenParam();
        String date = pramnam.getdateforpage();
        arl.add(date);
        arl.add("Month");
        arl.add("Last Period");

        alisty2.addAll(arl);
        if (kPIFromReport.getDynamicTimeDetails() != null && kPIFromReport.getDynamicTimeDetails().equalsIgnoreCase("dynamic")) {
            if (kPIFromReport.getOtherTimeDetails() != null && kPIFromReport.getOtherTimeDetails().equalsIgnoreCase("repTimeDet")) {
                alisty2.set(0, timeDetaArr[0]);
                alisty2.set(1, timeDetaArr[1]);
                alisty2.set(2, timeDetaArr[2]);
                alisty2.set(3, timeDetaArr[3]);
                alisty2.set(4, timeDetaArr[4]);
            }
        }


        if (onecontainer.timedetails != null && !onecontainer.timedetails.isEmpty()) {
            request.setAttribute("OneviewTiemDetails", onecontainer.timedetails);
        } else {
            onecontainer.timedetails = arl;
        }
        PbReportCollection collect = new PbReportCollection();
        collect.reportQryElementIds = new ArrayList(Arrays.asList(kPIFromReport.getReportQryCols()));
        collect.reportQryAggregations = new ArrayList(Arrays.asList(kPIFromReport.getQryAggregations()));
        collect.reportRowViewbyValues = new ArrayList(Arrays.asList(kPIFromReport.getRowViewBys()));
        collect.reportColViewbyValues = new ArrayList(Arrays.asList(kPIFromReport.getColViewBys()));
//            collect.reportParametersValues = (LinkedHashMap) kPIFromReport.getReportParametersValues();
        if ((List<String>) request.getAttribute("OneviewTiemDetails") != null) {
            collect.timeDetailsArray = alisty1;
        } else {
            collect.timeDetailsArray = alisty2;
        }
        collect.reportId = kPIFromReport.getReportId();

        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject newReturnObj = new PbReturnObject();
        PbReportTableBD reportTableBD = new PbReportTableBD();
        ArrayList sortColumns = null;
        if (kPIFromReport.getSortColumns() != null) {
            sortColumns = new ArrayList(Arrays.asList(kPIFromReport.getSortColumns()));
        }
        ArrayList dataSeq = null;
        repQuery.setRowViewbyCols(rowViewBys);
        repQuery.setParamValue((HashMap) kPIFromReport.getReportParametersValues());//Added by k
        repQuery.setColViewbyCols(new ArrayList());
        repQuery.setQryColumns(reportQryCols);
        repQuery.setBizRoles(kPIFromReport.getBizRoles()[0]);
        repQuery.setColAggration(qryAggregations);
        repQuery.setUserId(session.getAttribute("USERID").toString());
        //repQuery.setTimeDetails(timeDetailsArray);
        if (onecontainer.getFilterBusinessRole() != null && !onecontainer.getFilterBusinessRole().equalsIgnoreCase("") && kPIFromReport.getBizRoles()[0] != null && !kPIFromReport.getBizRoles()[0].equalsIgnoreCase("") && onecontainer.getFilterBusinessRole().equalsIgnoreCase(kPIFromReport.getBizRoles()[0])) {
            if (onecontainer.getReportParameterValues() != null && !onecontainer.getReportParameterValues().isEmpty()) {
                repQuery.setParamValue(onecontainer.getReportParameterValues());
            }
            repQuery.setInMap(onecontainer.getReportParameterValues());
        }

        if ((List<String>) request.getAttribute("OneviewTiemDetails") != null) {
            repQuery.setTimeDetails(alisty1);
            request.setAttribute("complexKPITimeDetails", alisty1);
        } else {
            repQuery.setTimeDetails(alisty2);
            request.setAttribute("complexKPITimeDetails", alisty2);
        }

        ArrayList innerViewBy = repQuery.getInnerViewBy(collect.reportId);
        if (innerViewBy != null && innerViewBy.size() > 0) {
            repQuery.isInnerViewBy = true;
            for (int iLoop = 0; iLoop < innerViewBy.size(); iLoop++) {
                if ((!collect.reportRowViewbyValues.contains(innerViewBy.get(iLoop))) && (!collect.reportColViewbyValues.contains(innerViewBy.get(iLoop)))) {
                    collect.reportRowViewbyValues.add((String) innerViewBy.get(iLoop));
                } else {
                    innerViewBy.remove(iLoop);
                }

            }

        }
        String totalvalue = kPIFromReport.getTotalValue();
        String query = "select  REF_ELEMENT_TYPE , AGGREGATION_TYPE from  PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + collect.reportQryElementIds.get(0);
        PbDb pbdb = new PbDb();
        PbReturnObject retobj = pbdb.execSelectSQL(query);
        if ((retobj.getFieldValueString(0, "REF_ELEMENT_TYPE").equalsIgnoreCase("4") || retobj.getFieldValueString(0, "AGGREGATION_TYPE").equalsIgnoreCase("avg")) && totalvalue != null && totalvalue.equalsIgnoreCase("grandtotalDet") && !totalvalue.equalsIgnoreCase("subtotalDet")) {
            returnObject = this.getKpiRetObjforgrandtotals(collect, session.getAttribute("USERID").toString(), kPIFromReport);
        } else {
            returnObject = repQuery.getPbReturnObject(String.valueOf(reportQryCols.get(0)));
        }
//                returnObject = repQuery.getPbReturnObject(String.valueOf(reportQryCols.get(0)));

        reportTableBD.searchDataSet(kPIFromReport, returnObject);
        if (totalvalue != null && !totalvalue.equalsIgnoreCase("grandtotalDet") && totalvalue.equalsIgnoreCase("subtotalDet")) {
            if (kPIFromReport.isTopBottomSet()) {
                if (kPIFromReport.getTopBtmMode().equalsIgnoreCase("TopBottomAbsRows")) {
                    dataSeq = returnObject.findTopBottom(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
                } else {
                    dataSeq = returnObject.findTopBottomPercentWise(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
                }
                returnObject.setViewSequence(dataSeq);
            }
        } else if (totalvalue == null) {
            if (kPIFromReport.isTopBottomSet()) {
                if (kPIFromReport.getTopBtmMode().equalsIgnoreCase("TopBottomAbsRows")) {
                    dataSeq = returnObject.findTopBottom(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
                } else {
                    dataSeq = returnObject.findTopBottomPercentWise(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
                }
                returnObject.setViewSequence(dataSeq);
            }
        }

        returnObject.setProcessGT(true);
        returnObject.prepareObject(returnObject, dataSeq);
        return returnObject;
    }

    public String getComplexKPIForOneView(String roleId) {
        String sqlQuery = getResourceBundle().getString("getCreateKPIData");
        PbReturnObject retObj = new PbReturnObject();
        StringBuilder outerBuffer = new StringBuilder();
        String createKPIString = "";
        Gson gson = new Gson();
        CreateKPIFromReport kPIFromReport = new CreateKPIFromReport();
        try {
            retObj = execSelectSQL(sqlQuery);
            if (retObj.getRowCount() > 0) {
//            outerBuffer.append("<li class='closed' id='complexKPI'>");
//                outerBuffer.append("<img src='icons pinvoke/table.png'></img>");
//                outerBuffer.append("<span style='font-family:verdana;font-size:8pt'>COMPLEX KPI</span>");
//                outerBuffer.append("<ul id='factName-complexKPI'>");
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        createKPIString = retObj.getFieldUnknown(i, 2);
                    } else {
                        createKPIString = retObj.getFieldValueClobString(i, "CREATE_KPI_DATA");
                    }
                    Type tarType = new TypeToken<Object>() {
                    }.getType();
                    kPIFromReport = gson.fromJson(createKPIString, CreateKPIFromReport.class);
                    if (kPIFromReport.getBizRoles() != null) {
                        createKPIString = kPIFromReport.getBizRoles()[0];
                        if (roleId != null && createKPIString != null && createKPIString.equalsIgnoreCase(roleId)) {
                            outerBuffer.append("<li>");
                            outerBuffer.append("<img src='icons pinvoke/report.png'></img>");
                            outerBuffer.append("<span  id='" + retObj.getFieldValueString(i, "CREATE_KPI_ID") + "' class='myDragTabs ui-draggable'>" + retObj.getFieldValueString(i, "CREATE_KPI_NAME") + "</span></li>");
                        }
                    }
                    //outerBuffer.append(retObj.getFieldValueString(i, "CREATE_KPI_NAME"));
                }

//                outerBuffer.append("</ul>");
//                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return outerBuffer.toString();
    }

    public int updateOneViewDetails(String oneViewId, String path, String version) {
        int flag = 0;
        String sql = getResourceBundle().getString("updateOneViewFile");
        Object[] obj = new Object[3];
        obj[0] = version;
        obj[1] = path;
        obj[2] = oneViewId;
        String finalQuery = buildQuery(sql, obj);
        try {
            flag = execUpdateSQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return flag;
    }

    public PbReturnObject getKpiRetObjforgrandtotals(PbReportCollection collect, String PbuserId, CreateKPIFromReport kpifromreport) throws Exception {
        PbReturnObject retObject = new PbReturnObject();
        PbReportQuery KpiQuery = new PbReportQuery();
        ArrayList<String> repqrycls = new ArrayList<String>();
        ArrayList<String> repqryaggs = new ArrayList<String>();
        QueryExecutor qryExec = new QueryExecutor();
        ArrayList<String> allqrycls = new ArrayList<String>();
        ArrayList<String> allqryAggs = new ArrayList<String>();

        for (int i = 0; i < collect.reportQryElementIds.size(); i++) {
            String query = "select  REF_ELEMENT_TYPE , AGGREGATION_TYPE from  PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + collect.reportQryElementIds.get(i);
            PbDb pbdb = new PbDb();
            PbReturnObject retobj = pbdb.execSelectSQL(query);
            if (retobj.getFieldValueString(0, "REF_ELEMENT_TYPE").equalsIgnoreCase("4") || retobj.getFieldValueString(0, "AGGREGATION_TYPE").equalsIgnoreCase("avg")) {
                repqrycls.add(collect.reportQryElementIds.get(i));
                repqryaggs.add(collect.reportQryAggregations.get(i));
            }
        }
        if (repqrycls != null && !repqrycls.isEmpty()) {
            for (int i = 0; i < repqrycls.size(); i++) {

                //SELECT element_id FROM prg_user_all_info_details WHERE ref_element_id IN (SELECT ref_element_id FROM prg_user_all_info_details WHERE element_id      =2784147) and ref_element_type in (1,2)
                String query = "SELECT ELEMENT_ID , AGGREGATION_TYPE FROM PRG_USER_ALL_INFO_DETAILS WHERE ref_element_id IN (SELECT ref_element_id FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id=" + repqrycls.get(i) + ") ";
                PbDb pbdb = new PbDb();
                PbReturnObject retobj = pbdb.execSelectSQL(query);
                if (retobj != null && retobj.getRowCount() > 0) {
                    for (int j = 0; j < retobj.getRowCount(); j++) {
                        allqrycls.add(retobj.getFieldValueString(j, 0));
                        allqryAggs.add(retobj.getFieldValueString(j, 1));
                    }
                }
            }
        }
//          container.setKpiQrycls(repqrycls);
        KpiQuery.setRowViewbyCols(collect.reportRowViewbyValues);
        KpiQuery.setColViewbyCols(collect.reportColViewbyValues);
        KpiQuery.setParamValue(collect.reportParametersValues);

        KpiQuery.setQryColumns(allqrycls);
        KpiQuery.setColAggration(allqryAggs);
        KpiQuery.setTimeDetails(collect.timeDetailsArray);
        KpiQuery.isKpi = true;
//        HashMap<String, List> inMap = (HashMap<String, List>) collect.operatorFilters.get("IN");
//        if (inMap != null) {
//        KpiQuery.setInMap(inMap);
//        }
        KpiQuery.setReportId(collect.reportId);
        KpiQuery.setBizRoles(kpifromreport.getBizRoles()[0]);
        KpiQuery.setUserId(PbuserId);

        if (allqrycls != null && !allqrycls.isEmpty()) {
            String query = KpiQuery.generateViewByQry();
            //
            retObject = (PbReturnObject) qryExec.executeQuery(collect, query, false);
        }
//            if(retObject!=null && retObject.getRowCount()>0){
//                for(int i=0;i<repqrycls.size();i++){
//                     
//                }
//
//            }

        return retObject;
    }

    public HashMap getModifyMeasureattr(String elementid) {




        PbReturnObject retObj = new PbReturnObject();
        PbReturnObject retObj2 = new PbReturnObject();
        PbReturnObject retObj3 = new PbReturnObject();
        String getReportMeasures = "select user_col_desc from PRG_USER_ALL_INFO_DETAILS Where ELEMENT_ID=" + elementid;
        String getMeasureProperties = "select symbols,no_format,round,user_col_type,suffix_symbol from prg_user_sub_folder_elements where element_id=" + elementid;
        String getaggregationtype = "select aggregation_type,measure_type from prg_user_all_info_details where element_id=" + elementid;

        HashMap<String, String> map2 = new HashMap();

        try {

            retObj = execSelectSQL(getReportMeasures);
            if (retObj != null && retObj.getRowCount() > 0) {
//            if(retObj.getRowCount()>0){
//                for(int i=0;i<retObj.getRowCount();i++){
//                   String elementid="";
                String elementname = "";
//                  elementid="A_";
//                  elementid+=retObj.getFieldValueString(i, 0);
                elementname = retObj.getFieldValueString(0, 0);
                retObj2 = execSelectSQL(getMeasureProperties);


//                for(int j=0;j<retObj2.getRowCount();j++){

                String symbols = retObj2.getFieldValueString(0, 0);
                String no_format = retObj2.getFieldValueString(0, 1);
                String round = retObj2.getFieldValueString(0, 2);
                String col_type = retObj2.getFieldValueString(0, 3);
                String suffix_symbol = retObj2.getFieldValueString(0, 4);

                map2.put("elementname", elementname);
                map2.put("no_format", no_format);
                map2.put("symbols", symbols);
                map2.put("round", round);
                map2.put("ncol_type", col_type);
                map2.put("suffix_symbol", suffix_symbol);
//                  measureName.add(retObj.getFieldValueString(i, 1));
                retObj3 = execSelectSQL(getaggregationtype);


//                for(int k=0;k<retObj3.getRowCount();k++){
                String aggregation = retObj3.getFieldValueString(0, 0);
                String measureType = retObj3.getFieldValueString(0, 1);
                map2.put("aggregation", aggregation);
                map2.put("measureType", measureType);
//                }

//                }

//                 map.put(elementid,map2);
//                }


//            }
            } else {
                map2 = null;
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
//         container.setmodifymeasure(map);
        return map2;
    }
}
