
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportdesigner.bd;

import com.google.gson.Gson;
import com.lowagie.text.BadElementException;
import com.progen.bd.ProgenJqplotGraphBD;
import com.progen.charts.GraphProperty;
import com.progen.charts.JqplotGraphProperty;
import com.progen.charts.ProgenChartDisplay;
import com.progen.dashboard.DashboardConstants;
import com.progen.dashboardView.bd.PbDashboardViewerBD;
import com.progen.dashboardView.db.DashboardViewerDAO;
import com.progen.datasnapshots.DataSnapshotDAO;
import com.progen.datasnapshots.DataSnapshotGenerator;
import com.progen.db.ProgenDataSet;
import com.progen.graph.GraphBuilder;
import com.progen.graph.info.ProgenGraphInfo;
import com.progen.jqplot.ProGenJqPlotChartTypes;
import com.progen.oneView.bd.OneViewBD;
import com.progen.report.charts.PbGraphDisplay;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.entities.GraphReport;
import com.progen.report.entities.KPI;
import com.progen.report.entities.MapDetail;
import com.progen.report.entities.QueryDetail;
import com.progen.report.kpi.KPIBuilder;
import com.progen.report.map.MapBD;
import com.progen.report.*;
import com.progen.report.query.PbReportQuery;
import com.progen.reportdesigner.action.GroupMeassureParams;
import com.progen.reportdesigner.db.DashboardTemplateDAO;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.scheduler.ReportSchedule;
import com.progen.scheduler.SchedulerBD;
import com.progen.scheduler.db.SchedulerDAO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.*;

/**
 *
 * @author mahesh.sanampudi@progenbusiness.com
 */
public class DashboardTemplateBD {

    public static Logger logger = Logger.getLogger(DashboardTemplateBD.class);
    DashboardTemplateDAO DAO = new DashboardTemplateDAO();
    ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
    //added by susheela
    private String userId = null;
    //added by k
    private String viewBy = "";
    private HttpServletRequest servletRequest = null;
    private HttpSession session = null;
    private String displayType = "newGraph";

    public HashMap setDefaults(String grpId, String grpType, HashMap GraphHashMap, HashMap ParametersHashMap, HashMap TableHashMap, HashMap GraphSizesDtlsHashMap, HashMap GraphClassesHashMap) {
        HashMap GraphDetails = null;
        GraphDetails = new HashMap();
        ArrayList Parameters = (ArrayList) ParametersHashMap.get("Parameters");
        ArrayList REP = (ArrayList) TableHashMap.get("REP");
        ArrayList CEP = (ArrayList) TableHashMap.get("CEP");
        ArrayList Measures = (ArrayList) TableHashMap.get("Measures");

        ArrayList sizeDtls = (ArrayList) GraphSizesDtlsHashMap.get("Medium");
        String graphClass = String.valueOf(GraphClassesHashMap.get(grpType));
        String grpIds = "";
        String[] barChartColumnNames = null;
        String[] pieChartColumns = null;
        String[] barChartColumnTitles = null;
        String[] axis = null;

        try {
            if (GraphHashMap.get("graphIds") != null) {
                grpIds = (String) GraphHashMap.get("graphIds");
                grpIds = grpIds + "," + grpId;
            } else {
                grpIds = grpId;
            }



            String[] viewByElementIds = null;
            String[] graphCols = new String[1];

            if (Measures != null && Measures.size() != 0) {
                if (REP != null) {
                    viewByElementIds = (String[]) REP.toArray(new String[0]);
                    //graphCols[0] = (String) Measures.toArray(new String[0])[REP.size()];

                } else {
                    if (Parameters != null && Parameters.size() != 0) {
                        viewByElementIds = new String[1];
                        viewByElementIds[0] = String.valueOf(Parameters.get(0));
                    } else {
                    }
                    //graphCols[0] = (String) Measures.toArray(new String[0])[REP.size()];
                }
                graphCols[0] = (String) Measures.toArray(new String[0])[0];

                barChartColumnNames = new String[viewByElementIds.length + graphCols.length];
                pieChartColumns = new String[barChartColumnNames.length];
                barChartColumnTitles = new String[barChartColumnNames.length];
                axis = new String[barChartColumnNames.length];

                for (int i = 0; i < viewByElementIds.length; i++) {
                    barChartColumnNames[i] = viewByElementIds[i];
                    pieChartColumns[i] = viewByElementIds[i];
                    barChartColumnTitles[i] = viewByElementIds[i];
                    axis[i] = "0";


                }
                for (int i = viewByElementIds.length; i < (graphCols.length + viewByElementIds.length); i++) {
                    barChartColumnNames[i] = graphCols[i - viewByElementIds.length];
                    pieChartColumns[i] = graphCols[i - viewByElementIds.length];
                    barChartColumnTitles[i] = graphCols[i - viewByElementIds.length];
                    axis[i] = "0";

                }


            }

            GraphDetails.put("graphId", grpId);
            GraphDetails.put("graphName", "Graph " + grpId);
            //GraphDetails.put("graphClassName", DAO.getGraphClassName(grpType));
            GraphDetails.put("graphClassName", graphClass);
            GraphDetails.put("graphTypeName", grpType);
            GraphDetails.put("viewByElementIds", viewByElementIds);
            GraphDetails.put("grpSize", "Medium");
            GraphDetails.put("graphWidth", String.valueOf(sizeDtls.get(0)));
            GraphDetails.put("graphHeight", String.valueOf(sizeDtls.get(1)));
            GraphDetails.put("barChartColumnNames", barChartColumnNames);
            GraphDetails.put("pieChartColumns", pieChartColumns);
            GraphDetails.put("barChartColumnTitles", barChartColumnTitles);
            GraphDetails.put("axis", axis);



            GraphHashMap.put(grpId, GraphDetails);
            GraphHashMap.put("graphIds", grpIds);
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return GraphHashMap;
    }

    public HashMap changeGraphType(HashMap GraphHashMap, String grpType, String grpId, HashMap ParametersHashMap, HashMap TableHashMap, HashMap GraphClassesHashMap) {
        HashMap GraphDetails = null;
        String graphClass = String.valueOf(GraphClassesHashMap.get(grpType));
        GraphDetails = (HashMap) GraphHashMap.get(grpId);
        GraphDetails.put("graphTypeName", grpType);
        GraphDetails.put("graphClassName", graphClass);
        GraphHashMap.put(grpId, GraphDetails);
        return GraphHashMap;
    }

    public HashMap changeGraphSize(HashMap GraphHashMap, String grpSize, String grpId, HashMap ParametersHashMap, HashMap TableHashMap, HashMap GraphSizesDtlsHashMap) {
        HashMap GraphDetails = null;
        GraphDetails = (HashMap) GraphHashMap.get(grpId);
        GraphDetails.put("grpSize", grpSize);

        ArrayList sizeDtls = (ArrayList) GraphSizesDtlsHashMap.get(grpSize);

        GraphDetails.put("graphWidth", String.valueOf(sizeDtls.get(0)));
        GraphDetails.put("graphHeight", String.valueOf(sizeDtls.get(1)));

        //GraphDetails = DAO.getGrpDimensionsBySize(GraphDetails, grpSize);

        GraphHashMap.put(grpId, GraphDetails);
        return GraphHashMap;
    }

    public HashMap changeGraphColumns(HashMap GraphHashMap, String grpColumns, String grpId, HashMap ParametersHashMap, HashMap TableHashMap) {

        HashMap GraphDetails = null;
        ArrayList Parameters = (ArrayList) ParametersHashMap.get("Parameters");
        ArrayList REP = (ArrayList) TableHashMap.get("REP");
        //ArrayList CEP = (ArrayList) TableHashMap.get("CEP");
        //ArrayList Measures = (ArrayList) TableHashMap.get("Measures");





        String[] viewByElementIds = null;
        String[] axis = null;
        String[] barChartColumnNames = null;
        String[] pieChartColumns = null;
        String[] barChartColumnTitles = null;
        String[] graphCols = grpColumns.split(",");

        if (REP == null) {
            viewByElementIds = new String[1];
            viewByElementIds[0] = String.valueOf(Parameters.get(0));
        } else {
            viewByElementIds = (String[]) REP.toArray(new String[0]);
        }



        graphCols = grpColumns.split(",");
        barChartColumnNames = new String[viewByElementIds.length + graphCols.length];
        pieChartColumns = new String[barChartColumnNames.length];
        barChartColumnTitles = new String[barChartColumnNames.length];
        axis = new String[barChartColumnNames.length];

        for (int i = 0; i < viewByElementIds.length; i++) {
            barChartColumnNames[i] = viewByElementIds[i];
            pieChartColumns[i] = viewByElementIds[i];
            barChartColumnTitles[i] = viewByElementIds[i];
            axis[i] = "0";
        }
        for (int i = viewByElementIds.length; i < (graphCols.length + viewByElementIds.length); i++) {
            barChartColumnNames[i] = graphCols[i - viewByElementIds.length];
            pieChartColumns[i] = graphCols[i - viewByElementIds.length];
            barChartColumnTitles[i] = graphCols[i - viewByElementIds.length];
            axis[i] = "0";
        }

        GraphDetails = (HashMap) GraphHashMap.get(grpId);

        if (GraphDetails != null) {
            GraphDetails.put("barChartColumnNames", barChartColumnNames);
            GraphDetails.put("pieChartColumns", pieChartColumns);
            GraphDetails.put("barChartColumnTitles", barChartColumnTitles);
            GraphDetails.put("viewByElementIds", viewByElementIds);
            GraphDetails.put("axis", axis);

            GraphHashMap.put(grpId, GraphDetails);
        }
        return GraphHashMap;
    }

    public HashMap changeViewBys(HashMap GraphHashMap, String grpViewBys, String grpIds) {
        String[] graphIds = grpIds.split(",");
        String[] viewByElementIds = grpViewBys.split(",");
        HashMap GraphDetails = null;

        for (int i = 0; i < graphIds.length; i++) {
            GraphDetails = (HashMap) GraphHashMap.get(graphIds[i]);

            String[] prevBarChartColumnNames = (String[]) GraphDetails.get("barChartColumnNames");
            String[] prevPieChartColumns = (String[]) GraphDetails.get("pieChartColumns");
            String[] prevBarChartColumnTitles = (String[]) GraphDetails.get("barChartColumnTitles");
            String[] prevViewByElementIds = (String[]) GraphDetails.get("viewByElementIds");
            //String[] prevAxis = (String[]) GraphDetails.get("axis");

            String[] barChartColumnNames = new String[viewByElementIds.length + (prevBarChartColumnNames.length - prevViewByElementIds.length)];
            String[] pieChartColumns = new String[barChartColumnNames.length];
            String[] barChartColumnTitles = new String[barChartColumnNames.length];
            String[] axis = new String[barChartColumnNames.length];

            for (int j = 0; j < viewByElementIds.length; j++) {
                barChartColumnNames[j] = viewByElementIds[j];
                pieChartColumns[j] = viewByElementIds[j];
                barChartColumnTitles[j] = viewByElementIds[j];
                axis[j] = "0";
            }
            for (int j = prevViewByElementIds.length; j < prevBarChartColumnNames.length; j++) {
                barChartColumnNames[viewByElementIds.length + (j - prevViewByElementIds.length)] = prevBarChartColumnNames[j];
                pieChartColumns[viewByElementIds.length + (j - prevViewByElementIds.length)] = prevPieChartColumns[j];
                barChartColumnTitles[viewByElementIds.length + (j - prevViewByElementIds.length)] = prevBarChartColumnTitles[j];
                axis[viewByElementIds.length + (j - prevViewByElementIds.length)] = "0";
            }

            GraphDetails.put("viewByElementIds", viewByElementIds);
            GraphDetails.put("barChartColumnNames", barChartColumnNames);
            GraphDetails.put("pieChartColumns", pieChartColumns);
            GraphDetails.put("barChartColumnTitles", barChartColumnTitles);

            GraphHashMap.put(graphIds[i], GraphDetails);
        }
        return GraphHashMap;
    }

    public String buildGraph(HttpServletRequest request, HttpServletResponse response, String grpIds) {
        ArrayList grpDetails = new ArrayList();
        PbGraphDisplay GraphDisplay = new PbGraphDisplay();
        GraphDisplay.setCtxPath(request.getContextPath());
        StringBuffer graphsBuffer = new StringBuffer("");

        //ArrayList displayedSet = new ArrayList();
        PbReturnObject pbretObj = null;
        HttpSession session = null;
        String reportName = "";

        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        HashMap ReportHashMap = null;
        HashMap GraphHashMap = null;

        HashMap GraphSizesHashMap = null;
        HashMap GraphTypesHashMap = null;

        String[] GraphSizesKeySet = null;
        String[] GraphSTypesKeySet = null;

        try {
            session = request.getSession();
            ParametersHashMap = (HashMap) session.getAttribute("ParametersHashMap");
            TableHashMap = (HashMap) session.getAttribute("TableHashMap");
            ReportHashMap = (HashMap) session.getAttribute("ReportHashMap");
            GraphHashMap = (HashMap) session.getAttribute("GraphHashMap");

            if (session.getAttribute("GraphTypesHashMap") != null) {
                GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
                GraphSTypesKeySet = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);
            }

            if (session.getAttribute("GraphSizesHashMap") != null) {
                GraphSizesHashMap = (HashMap) session.getAttribute("GraphSizesHashMap");
                GraphSizesKeySet = (String[]) GraphSizesHashMap.keySet().toArray(new String[0]);
            }

            reportName = (String) ReportHashMap.get("ReportName");

            ArrayList Measures = (ArrayList) TableHashMap.get("Measures");
            ArrayList REP = (ArrayList) TableHashMap.get("REP");
            ArrayList CEP = (ArrayList) TableHashMap.get("CEP");
            ArrayList paramList = (ArrayList) ParametersHashMap.get("Parameters");
            pbretObj = getRecordSet(ParametersHashMap, TableHashMap, GraphHashMap);
            String[] dbColumns = null;
            String[] axis = null;
            String[] graphIds = grpIds.split(",");
            HashMap[] graphMapDetails = new HashMap[graphIds.length];

            if (pbretObj != null) {
                ArrayList AllGraphColumns = (ArrayList) GraphHashMap.get("AllGraphColumns");
                ArrayList originalColumns = new ArrayList();
                Container container = new Container();
                if (CEP != null && CEP.size() != 0) {
                    originalColumns = new ArrayList();
                    if (pbretObj.getRowCount() != 0) {
                        dbColumns = pbretObj.getColumnNames();
                        int graphColcount = 0;

                        if (dbColumns.length <= 10) {
                            graphColcount = dbColumns.length;
                        } else {
                            graphColcount = 10;
                        }
                        axis = new String[graphColcount];
                        for (int colIndex = 0; colIndex < graphColcount; colIndex++) {
                            originalColumns.add(String.valueOf(dbColumns[colIndex]));
                            axis[colIndex] = "0";
                        }
                    }
                } else {
                    originalColumns = new ArrayList();
                    if (REP != null && REP.size() != 0) {
                        for (int i = 0; i < REP.size(); i++) {
                            originalColumns.add(String.valueOf(REP.get(i)));
                        }
                    } else {
                        if (paramList != null && paramList.size() != 0) {
                            originalColumns.add(String.valueOf(paramList.get(0)));
                        }
                    }
                    if (AllGraphColumns != null && AllGraphColumns.size() != 0) {
                        for (int i = 0; i < AllGraphColumns.size(); i++) {
                            originalColumns.add(String.valueOf(AllGraphColumns.get(i)));
                        }
                    } else {
                        if (Measures != null && Measures.size() != 0) {
                            originalColumns.add(String.valueOf(Measures.get(0)));
                        }
                    }
                }
                container.setOriginalColumns(originalColumns);
                //container.setDisplayedSet(pbretObj);
                //displayedSet = container.getDisplayedSet();
                String[] viewBys = null;
                String[] viewBysDispNames = null;

                if (graphIds.length != 0) {
                    for (int i = 0; i < graphMapDetails.length; i++) {
                        graphMapDetails[i] = (HashMap) GraphHashMap.get(graphIds[i]);

                        if (CEP != null && CEP.size() != 0) {
                            viewBys = (String[]) graphMapDetails[i].get("viewByElementIds");

                            for (int index = 0; index < viewBys.length; index++) {
                                viewBys[index] = (String) originalColumns.get(index);
                            }
                            graphMapDetails[i].put("viewByElementIds", viewBys);
                            graphMapDetails[i].put("barChartColumnNames", (String[]) originalColumns.toArray(new String[0]));
                            graphMapDetails[i].put("barChartColumnTitles", (String[]) originalColumns.toArray(new String[0]));
                            graphMapDetails[i].put("pieChartColumns", (String[]) originalColumns.toArray(new String[0]));
                            graphMapDetails[i].put("axis", axis);

                        } else {
                            if (REP != null && REP.size() != 0) {
                                viewBys = (String[]) REP.toArray(new String[0]);
                            } else {
                                viewBys = new String[1];
                                viewBys[0] = String.valueOf(paramList.get(0));
                            }
                        }
                        viewBysDispNames = viewBys;

                    }
                    GraphDisplay.setViewByColNames(viewBysDispNames);
                    GraphDisplay.setViewByElementIds(viewBys);

                    //GraphDisplay.setCurrentDispRecords(displayedSet);
                    GraphDisplay.setCurrentDispRecordsRetObjWithGT(pbretObj);
                    GraphDisplay.setCurrentDispRetObjRecords(pbretObj);
                    GraphDisplay.setAllDispRecordsRetObj(pbretObj);
                    //GraphDisplay.setSwapGraphAnalysis(swapGraphAnalysis);
                    GraphDisplay.setSession(request.getSession(false));
                    GraphDisplay.setResponse(response);
                    GraphDisplay.setOut(response.getWriter());
                    GraphDisplay.setReportId("");
                    GraphDisplay.setGraphMapDetails(graphMapDetails);
                    GraphDisplay.setJscal("");

                    grpDetails = GraphDisplay.getGraphHeadersByGraphMap();

                    ProgenChartDisplay[] pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
                    ProgenChartDisplay[] pchartsZoom = (ProgenChartDisplay[]) grpDetails.get(4);

                    String[] paths = grpDetails.get(0).toString().split(";");//grpDetails[0].split(";");
                    String[] grpTitles = grpDetails.get(1).toString().split(";");
                    String[] pathsZoom = grpDetails.get(3).toString().split(";");//grpDetails[0].split(";");
                    graphMapDetails = (HashMap[]) grpDetails.get(5);//

                    //graphsBuffer.append("");
                    //graphsBuffer.append("");

                    //solid black 1px
                    graphsBuffer.append("<Table width=\"100%\" border='0'  >");
                    graphsBuffer.append("<Tr>");
                    for (int grpCnt = 0; grpCnt < pcharts.length; grpCnt++) {
                        pcharts[grpCnt].setCtxPath(request.getContextPath());
                        pchartsZoom[grpCnt].setCtxPath(request.getContextPath());
                        pcharts[grpCnt].setSwapColumn(true);

                        String grpType = "";
                        String grpSize = "";
                        if (graphMapDetails[grpCnt].get("graphTypeName") != null) {
                            grpType = String.valueOf(graphMapDetails[grpCnt].get("graphTypeName"));
                        }
                        if (graphMapDetails[grpCnt].get("grpSize") != null) {
                            grpSize = String.valueOf(graphMapDetails[grpCnt].get("grpSize"));
                        }

                        graphsBuffer.append("<Td valign='top'  width=\"" + (100 / pcharts.length) + "%\">");
                        graphsBuffer.append("<Table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                        graphsBuffer.append("<Tr>");//start of 1st row
                        graphsBuffer.append("<Td>");
                        //&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src='images/sign_cancel.png' aligm='middle' onClick=\"deleteGraph('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='cursor:pointer;cursor:hand'  title='Delete Chart'>
                        graphsBuffer.append("<div   class=\"portlet-header ui-widget-header ui-corner-all \">" + reportName + " " + grpTitles[grpCnt] + "</div>");//portlet-header div starts here
                        graphsBuffer.append("</Td>");
                        graphsBuffer.append("</Tr>");

                        graphsBuffer.append("<Tr>");//start of 2nd row
                        graphsBuffer.append("<Td>");//start of 2nd row 1st td

                        graphsBuffer.append("<Table class=\"progenTable\"  border='0' align=\"left\">");//start of outer table
                        graphsBuffer.append("<Tr>");

                        //start of graph types

                        graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                        graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphTypesDisp(document.getElementById('dispgrptypes" + grpCnt + "'))\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Types\"> Graph Types </a>");
                        graphsBuffer.append("<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position: absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;'  id='dispgrptypes" + grpCnt + "'>");

                        graphsBuffer.append("<Table>");//start of inner table
                        for (int gtype = 0; gtype < GraphSTypesKeySet.length; gtype++) {
                            if (grpType.equalsIgnoreCase(String.valueOf(GraphTypesHashMap.get(GraphSTypesKeySet[gtype])))) {
                                graphsBuffer.append("<Tr>");
                                graphsBuffer.append("<Td id='" + grpType + "'>");
                                graphsBuffer.append("<b>" + grpType + "</b>");
                                graphsBuffer.append("</Td>");
                                graphsBuffer.append("</Tr>");
                            } else {
                                graphsBuffer.append("<Tr>");
                                graphsBuffer.append("<Td id='" + grpType + "'>");
                                graphsBuffer.append("<a href='javascript:void(0)' title='" + String.valueOf(GraphTypesHashMap.get(GraphSTypesKeySet[gtype])) + "' onclick=\"changeGrpType('" + String.valueOf(GraphTypesHashMap.get(GraphSTypesKeySet[gtype])) + "','" + graphIds[grpCnt] + "','" + grpIds + "')\">" + String.valueOf(GraphTypesHashMap.get(GraphSTypesKeySet[gtype])) + "</a>");
                                graphsBuffer.append("</Td>");
                                graphsBuffer.append("</Tr>");
                            }
                        }
                        graphsBuffer.append("</Table>");//closing of inner table
                        graphsBuffer.append(" </div>");
                        graphsBuffer.append("</Td>");

                        //end of graph types

                        //start of graph sizes

                        graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                        graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphSizesDisp(document.getElementById('dispgrpsizes" + grpCnt + "'))\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Sizes\"> Graph Sizes </a>");
                        graphsBuffer.append("<div  style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position: absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;'  id='dispgrpsizes" + grpCnt + "'>");

                        graphsBuffer.append("<Table>");//start of inner table
                        for (int gsize = 0; gsize < GraphSizesKeySet.length; gsize++) {

                            if (grpSize.equalsIgnoreCase(String.valueOf(GraphSizesHashMap.get(GraphSizesKeySet[gsize])))) {
                                graphsBuffer.append("<Tr>");
                                graphsBuffer.append("<Td id='" + grpSize + "'>");
                                graphsBuffer.append("<b>" + grpSize + "</b>");
                                graphsBuffer.append("</Td>");
                                graphsBuffer.append("</Tr>");
                            } else {
                                graphsBuffer.append("<Tr>");
                                graphsBuffer.append("<Td id='" + grpSize + "'>");
                                graphsBuffer.append("<a href='javascript:void(0)' title='" + String.valueOf(GraphSizesHashMap.get(GraphSizesKeySet[gsize])) + "' onclick=\"changeGrpSize('" + String.valueOf(GraphSizesHashMap.get(GraphSizesKeySet[gsize])) + "','" + graphIds[grpCnt] + "','" + grpIds + "')\">" + String.valueOf(GraphSizesHashMap.get(GraphSizesKeySet[gsize])) + "</a>");
                                graphsBuffer.append("</Td>");
                                graphsBuffer.append("</Tr>");
                            }
                        }
                        graphsBuffer.append("</Table>");//closing of inner table
                        graphsBuffer.append(" </div>");
                        graphsBuffer.append("</Td>");

                        //end of graph sizes

                        //start of graph columns
                        graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                        graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphColumnsDisp('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Columns\"> Graph Columns </a>");
                        //end of graph columns

                        //start of delete graph
                        graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                        graphsBuffer.append("<Td><a  href='javascript:void(0)' onClick=\"deleteGraph('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Delete Chart\">Delete Chart </a>");
                        //end of delete graph

                        graphsBuffer.append("</Tr>");
                        graphsBuffer.append("</Table>");//closing of outer table

                        graphsBuffer.append("</Td>");//closing of 2nd row 1st td
                        graphsBuffer.append("</Tr>");//end of 2nd row

                        graphsBuffer.append("<Tr>");//start of 3rd row
                        graphsBuffer.append("<Td>");
                        graphsBuffer.append("<div  align='left'   class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\">");//portlet div starts here

                        graphsBuffer.append("<div align='left' style=\"height:auto;width:" + (90 / pcharts.length) + "%\"   >");

                        if (pcharts[grpCnt].chartDisplay.equalsIgnoreCase("")) {

                            graphsBuffer.append("<SCRIPT LANGUAGE=\"JavaScript\" SRC=\"" + request.getContextPath() + "/overlib.js\">  </SCRIPT>");
                            graphsBuffer.append("<div  align='left' id=\"overDiv\" style=\"position:absolute; visibility:hidden; z-index:1000;\"></div>");
                            graphsBuffer.append("<img  src=\"" + request.getContextPath() + "/images/" + grpType + ".gif\" border='0' align='top' > </img>");
                            graphsBuffer.append(pcharts[grpCnt].chartDisplay);
                        } else {
                            graphsBuffer.append(pcharts[grpCnt].chartDisplay);
                        }

                        //graphsBuffer.append("</div>");//
                        graphsBuffer.append("</div >");//portlet div ends here
                        graphsBuffer.append("</div >");//column div ends here

                        graphsBuffer.append("</Td>");
                        graphsBuffer.append("</Tr>");
                        graphsBuffer.append("</Table>");

                        graphsBuffer.append("</Td>");
                    }
                    graphsBuffer.append("</Tr>");
                    graphsBuffer.append("</Table>");

                    /*
                     * graphsBuffer.append("<script>");
                     * graphsBuffer.append("$(document).ready(function() {");
                     *
                     * graphsBuffer.append("$('.portlet').addClass('ui-widget
                     * ui-widget-content ui-helper-clearfix ui-corner-all')");
                     * graphsBuffer.append(".find('.portlet-header')");
                     * graphsBuffer.append(".addClass('ui-widget-header
                     * ui-corner-all')"); graphsBuffer.append(".prepend('<span
                     * class='ui-icon ui-icon-plusthick'></span>')");
                     * graphsBuffer.append(".end()");
                     * graphsBuffer.append(".find('.portlet-content');");
                     * graphsBuffer.append(" $('.portlet-header
                     * .ui-icon').click(function() {");
                     * graphsBuffer.append("$(this).toggleClass('ui-icon-minusthick');");
                     * graphsBuffer.append("$(this).parents('.portlet:first').find('.portlet-content').toggle();");
                     * graphsBuffer.append("});");
                     * graphsBuffer.append("$('.column').disableSelection();");
                     *
                     * graphsBuffer.append("});");
                     * graphsBuffer.append("</script>");
                     */
                }
            } else {
                if (graphIds.length != 0) {
                    graphMapDetails = new HashMap[graphIds.length];

                    //String[] grpTitles = grpDetails.get(1).toString().split(";");

                    graphsBuffer.append("<Table width=\"100%\" border='0'  >");
                    graphsBuffer.append("<Tr>");
                    for (int grpCnt = 0; grpCnt < graphMapDetails.length; grpCnt++) {
                        String graphTitle = "";
                        graphMapDetails[grpCnt] = (HashMap) GraphHashMap.get(graphIds[grpCnt]);



                        graphTitle = String.valueOf(graphMapDetails[grpCnt].get("graphName"));

                        String grpType = "";
                        String grpSize = "";
                        if (graphMapDetails[grpCnt].get("graphTypeName") != null) {
                            grpType = String.valueOf(graphMapDetails[grpCnt].get("graphTypeName"));
                        }
                        if (graphMapDetails[grpCnt].get("grpSize") != null) {
                            grpSize = String.valueOf(graphMapDetails[grpCnt].get("grpSize"));
                        }

                        graphsBuffer.append("<Td valign='top'  width=\"" + (100 / graphMapDetails.length) + "%\">");
                        graphsBuffer.append("<Table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                        graphsBuffer.append("<Tr>");//start of 1st row
                        graphsBuffer.append("<Td>");
                        //&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src='images/sign_cancel.png' aligm='middle' onClick=\"deleteGraph('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='cursor:pointer;cursor:hand'  title='Delete Chart'
                        graphsBuffer.append("<div   class=\"portlet-header ui-widget-header ui-corner-all \">" + reportName + " " + graphTitle + "</div>");//portlet-header div starts here
                        graphsBuffer.append("</Td>");
                        graphsBuffer.append("</Tr>");

                        graphsBuffer.append("<Tr>");//start of 2nd row
                        graphsBuffer.append("<Td>");//start of 2nd row 1st td

                        graphsBuffer.append("<Table class=\"progenTable\"  border='0' align=\"left\">");//start of outer table
                        graphsBuffer.append("<Tr>");

                        //start of graph types

                        graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                        graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphTypesDisp(document.getElementById('dispgrptypes" + grpCnt + "'))\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Types\"> Graph Types </a>");
                        graphsBuffer.append("<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position: absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;'  id='dispgrptypes" + grpCnt + "'>");

                        graphsBuffer.append("<Table>");//start of inner table
                        for (int gtype = 0; gtype < GraphSTypesKeySet.length; gtype++) {



                            if (grpType.equalsIgnoreCase(String.valueOf(GraphTypesHashMap.get(GraphSTypesKeySet[gtype])))) {
                                graphsBuffer.append("<Tr>");
                                graphsBuffer.append("<Td id='" + grpType + "'>");
                                graphsBuffer.append("<b>" + grpType + "</b>");
                                graphsBuffer.append("</Td>");
                                graphsBuffer.append("</Tr>");
                            } else {
                                graphsBuffer.append("<Tr>");
                                graphsBuffer.append("<Td id='" + grpType + "'>");
                                graphsBuffer.append("<a href='javascript:void(0)' title='" + String.valueOf(GraphTypesHashMap.get(GraphSTypesKeySet[gtype])) + "' onclick=\"changeGrpType('" + String.valueOf(GraphTypesHashMap.get(GraphSTypesKeySet[gtype])) + "','" + graphIds[grpCnt] + "','" + grpIds + "')\">" + String.valueOf(GraphTypesHashMap.get(GraphSTypesKeySet[gtype])) + "</a>");
                                graphsBuffer.append("</Td>");
                                graphsBuffer.append("</Tr>");
                            }
                        }
                        graphsBuffer.append("</Table>");//closing of inner table
                        graphsBuffer.append(" </div>");
                        graphsBuffer.append("</Td>");

                        //end of graph types

                        //start of graph sizes

                        graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                        graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphSizesDisp(document.getElementById('dispgrpsizes" + grpCnt + "'))\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Sizes\"> Graph Sizes </a>");
                        graphsBuffer.append("<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position: absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;'  id='dispgrpsizes" + grpCnt + "'>");

                        graphsBuffer.append("<Table>");//start of inner table
                        for (int gsize = 0; gsize < GraphSizesKeySet.length; gsize++) {




                            if (grpSize.equalsIgnoreCase(String.valueOf(GraphSizesHashMap.get(GraphSizesKeySet[gsize])))) {
                                graphsBuffer.append("<Tr>");
                                graphsBuffer.append("<Td id='" + grpSize + "'>");
                                graphsBuffer.append("<b>" + grpSize + "</b>");
                                graphsBuffer.append("</Td>");
                                graphsBuffer.append("</Tr>");
                            } else {
                                graphsBuffer.append("<Tr>");
                                graphsBuffer.append("<Td id='" + grpSize + "'>");
                                graphsBuffer.append("<a href='javascript:void(0)' title='" + String.valueOf(GraphSizesHashMap.get(GraphSizesKeySet[gsize])) + "' onclick=\"changeGrpSize('" + String.valueOf(GraphSizesHashMap.get(GraphSizesKeySet[gsize])) + "','" + graphIds[grpCnt] + "','" + grpIds + "')\">" + String.valueOf(GraphSizesHashMap.get(GraphSizesKeySet[gsize])) + "</a>");
                                graphsBuffer.append("</Td>");
                                graphsBuffer.append("</Tr>");
                            }

                        }
                        graphsBuffer.append("</Table>");//closing of inner table
                        graphsBuffer.append(" </div>");
                        graphsBuffer.append("</Td>");

                        //end of graph sizes

                        //start of graph columns
                        graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                        graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphColumnsDisp('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Columns\"> Graph Columns </a>");
                        //end of graph columns

                        //start of delete graph
                        graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                        graphsBuffer.append("<Td><a  href='javascript:void(0)' onClick=\"deleteGraph('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Delete Chart\">Delete Chart </a>");
                        //end of delete graph

                        graphsBuffer.append("</Tr>");
                        graphsBuffer.append("</Table>");//closing of outer table

                        graphsBuffer.append("</Td>");//closing of 2nd row 1st td
                        graphsBuffer.append("</Tr>");//end of 2nd row

                        graphsBuffer.append("<Tr>");//start of 3rd row
                        graphsBuffer.append("<Td align='left'>");
                        graphsBuffer.append("<div  align='left'   class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\">");//portlet div starts here

                        graphsBuffer.append("<div align='left' style=\"height:auto;width:" + (90 / graphMapDetails.length) + "%\"   >");
                        graphsBuffer.append("<SCRIPT LANGUAGE=\"JavaScript\" SRC=\"" + request.getContextPath() + "/overlib.js\">  </SCRIPT>");
                        graphsBuffer.append("<div align='left' id=\"overDiv\" style=\"position:absolute; visibility:hidden; z-index:1000;\"></div>");
                        graphsBuffer.append("<img height=\"172px\" width=\"372px\"  src=\"" + request.getContextPath() + "/images/" + grpType + ".gif\" border='0' align='top' > </img>");

                        //graphsBuffer.append("</div>");//
                        graphsBuffer.append("</div >");//portlet div ends here
                        graphsBuffer.append("</div >");//column div ends here

                        graphsBuffer.append("</Td>");
                        graphsBuffer.append("</Tr>");
                        graphsBuffer.append("</Table>");

                        graphsBuffer.append("</Td>");
                    }
                    graphsBuffer.append("</Tr>");
                    graphsBuffer.append("</Table>");
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return graphsBuffer.toString();
    }

    public PbReturnObject getRecordSet(HashMap ParametersHashMap, HashMap TableHashMap, HashMap graphHashhMap) {
        //ArrayList recordsList = new ArrayList();
        PbReturnObject pbretObj = null;
        PbReportCollection collect = new PbReportCollection();
        //DisplayParameters disp = new DisplayParameters();
        PbReportQuery repQuery = new PbReportQuery();

        try {
            //collect.reportId = "1";
            HashMap reportIncomingParameters = new HashMap();
            HashMap paramValues = new HashMap();




            ArrayList paramList = (ArrayList) ParametersHashMap.get("Parameters");

            ArrayList REP = (ArrayList) TableHashMap.get("REP");
            ArrayList CEP = (ArrayList) TableHashMap.get("CEP");
            ArrayList Measures = (ArrayList) TableHashMap.get("Measures");
            ArrayList AllGraphColumns = (ArrayList) graphHashhMap.get("AllGraphColumns");


            if (AllGraphColumns == null) {
                AllGraphColumns = new ArrayList();
            }
            if (REP == null) {
                REP = new ArrayList();
                REP.add(String.valueOf(paramList.get(0)));
            }
            if (CEP != null && CEP.size() != 0) {
                CEP = new ArrayList();
            }
            if (Measures == null) {
                Measures = new ArrayList();
            }

            ArrayList reportQryElementIds = new ArrayList();
            ArrayList reportQryAggregations = new ArrayList();

            for (int i = 0; i < paramList.size(); i++) {
                if (paramValues.get(String.valueOf(paramList.get(i))) == null) {
                    paramValues.put(String.valueOf(paramList.get(i)), "All");
                    reportIncomingParameters.put("CBOAPR" + String.valueOf(paramList.get(i)), null);
                }
            }
            collect.reportIncomingParameters = reportIncomingParameters;
            collect.reportColViewbyValues = CEP;
            collect.reportRowViewbyValues = REP;

            collect.getParamMetaDataForReportDesigner();

            for (int j = 0; j < Measures.size(); j++) {
                if (!reportQryElementIds.contains(String.valueOf(Measures.get(j)))) {
                    reportQryElementIds.add(String.valueOf(Measures.get(j)));
                }
            }
            for (int k = 0; k < AllGraphColumns.size(); k++) {
                if (!reportQryElementIds.contains(String.valueOf(AllGraphColumns.get(k)))) {
                    reportQryElementIds.add(String.valueOf(AllGraphColumns.get(k)));
                }
            }


            if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
                reportQryAggregations = DAO.getReportQryAggregations(reportQryElementIds);

                collect.reportQryElementIds = reportQryElementIds;
                collect.reportQryAggregations = reportQryAggregations;

                repQuery.setQryColumns(collect.reportQryElementIds);
                repQuery.setColAggration(collect.reportQryAggregations);

                repQuery.setDefaultMeasure(String.valueOf(reportQryElementIds.get(0)));
                repQuery.setDefaultMeasureSumm(String.valueOf(reportQryAggregations.get(0)));
            }

            repQuery.setRowViewbyCols(REP);
            repQuery.setColViewbyCols(CEP);
            repQuery.setParamValue(paramValues);

            if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
                pbretObj = repQuery.getPbReturnObject(String.valueOf(reportQryElementIds.get(0)));
            }



        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return pbretObj;
    }

    public String buildTable(HashMap ParametersHashMap, HashMap TableHashMap, HashMap GraphHashMap, String ctxPath) {
        PbReturnObject retObj = getRecordSet(ParametersHashMap, TableHashMap, GraphHashMap);
        StringBuffer tableBuffer = new StringBuffer();
        String[] dbColumns = retObj.getColumnNames();
        int endCount = 10;

        //tableBuffer.append("");
        //tableBuffer.append("");


        /*
         * tableBuffer.append("<div id=\"pager\" class=\"pager\" align=\"left\"
         * >");
         *
         * tableBuffer.append("<img src=\"" + ctxPath +
         * "/tablesorter/addons/pager/icons/first.png\" class=\"first\"/>");
         * tableBuffer.append("<img src=\"" + ctxPath +
         * "/tablesorter/addons/pager/icons/prev.png\" class=\"prev\"/>");
         * tableBuffer.append("<input type=\"text\" class=\"pagedisplay\"/>");
         * tableBuffer.append("<img src=\"" + ctxPath +
         * "/tablesorter/addons/pager/icons/next.png\" class=\"next\"/>");
         * tableBuffer.append("<img src=\"" + ctxPath +
         * "/tablesorter/addons/pager/icons/last.png\" class=\"last\"/>");
         * tableBuffer.append("<select class=\"pagesize\" >");
         *
         * tableBuffer.append("<option value=\"5\">5</option>");
         * tableBuffer.append("<option selected value=\"10\">10</option>");
         * tableBuffer.append("</select>"); tableBuffer.append("</div>");
         */

        tableBuffer.append("<table cellspacing=\"1\" class=\"tablesorter\" id=\"tablesorter\" width=\"100px\">");
        tableBuffer.append("<thead>");
        tableBuffer.append("<tr valign=\"top\">");

        for (int i = 0; i < dbColumns.length; i++) {
            tableBuffer.append("<th>" + dbColumns[i] + "</th> ");
            //tableBuffer.append("<br>");
        }

        tableBuffer.append("</tr>");
        tableBuffer.append("</thead>");
        tableBuffer.append("<tfoot>");
        tableBuffer.append("</tfoot>");
        tableBuffer.append("<tbody>");

        if (retObj.getRowCount() <= endCount) {
            endCount = retObj.getRowCount();
        }

        for (int rowId = 0; rowId < endCount; rowId++) {
            tableBuffer.append("<tr valign = \"top\">");
            //tableBuffer.append("<br>");

            for (int colId = 0; colId < dbColumns.length; colId++) {
                tableBuffer.append("<td>");
                tableBuffer.append(retObj.getFieldValueString(rowId, dbColumns[colId]));
                tableBuffer.append("</td>");
                //tableBuffer.append("<br>");
            }
            tableBuffer.append("</tr>");
        }
        //tableBuffer.append("<br>");

        tableBuffer.append("</tbody>");
        tableBuffer.append("</table>");

        tableBuffer.append("<script type='text/javascript'>");

        tableBuffer.append("$(document).ready(function() {");
        tableBuffer.append(" $('#tablesorter').columnFilters();");
        tableBuffer.append(" $('#tablesorter')");
        tableBuffer.append(".tablesorter({widthFixed: true, widgets: ['zebra']})");
        tableBuffer.append(" .tablesorterPager({container: $('#pager')});");
        tableBuffer.append("});");

        tableBuffer.append(" </script>");

        //tableBuffer.append("<script>");
        //tableBuffer.append("$(document).ready(function() {");
        //tableBuffer.append(" $('table#tablesorter').columnFilters();");
        // tableBuffer.append("});");
        //tableBuffer.append("</script>");

        return tableBuffer.toString();
    }

    public ArrayList getRecordSetForGraph(HashMap ParametersHashMap, ArrayList graphColumns, ArrayList AllColumns, String customDbrdId) {

        PbReturnObject pbretObj = null;
        PbReportCollection collect = new PbReportCollection();
        PbReportQuery repQuery = new PbReportQuery();
        HashMap reportIncomingParameters = null;
        HashMap paramValues = null;
        HashMap TimeDimHashMap = null;
        ArrayList paramList = null;
        ArrayList TimeDetailstList = null;
        ArrayList REP = null;
        ArrayList CEP = null;
        Container container = new Container();
        ArrayList recordSet = new ArrayList();
        try {
            reportIncomingParameters = new HashMap();
            paramValues = new HashMap();
            paramList = (ArrayList) ParametersHashMap.get("Parameters");
            TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
            TimeDetailstList = (ArrayList) ParametersHashMap.get("TimeDetailstList");



            //////////////.println("paramList="+paramList);

            //////////////.println("TimeDimHashMap="+TimeDimHashMap);
            //////////////.println("TimeDetailstList="+TimeDetailstList);






            ArrayList params = new ArrayList();
            ArrayList timeparams = new ArrayList();
            ArrayList timeDetails = new ArrayList();
            ArrayList paramNames = new ArrayList();






            if (paramList == null) {

                ////.println("entered inside paramList");
                if (getSession() != null) {
                    HashMap map = (HashMap) getSession().getAttribute("PROGENTABLES");
                    if (map != null) {

                        Container contain = (Container) map.get(customDbrdId);

                        // ////////////////.println("entered");

                        paramNames = contain.getReportParameterNames();
                        HashMap hm = contain.getTimeParameterHashMap();
                        timeDetails = (ArrayList) hm.get("TimeDetailstList");
                        HashMap hmobj = contain.getReportParameterHashMap();

                        // setReportParameterHashMap()

                        //HashMap reportParameters = (HashMap) hmobj.get("reportParameters");
                        HashMap reportParametersValues = (HashMap) hmobj.get("reportParametersValues");


                        HashMap timeDetsMap = (HashMap) hm.get("TimeDimHashMap");
                        ////////////////////.println("Dasboard Kpi hm="+hm);

                        String[] BuildedParamsWithTime = (String[]) timeDetsMap.keySet().toArray(new String[0]); //contains 31150,31149,31156,31153
                        String[] BuildedParams = (String[]) reportParametersValues.keySet().toArray(new String[0]);
                        // String[] kpiElmnts = (String[]) kpiHashMap.keySet().toArray(new String[0]);
                        String BuildedParamsWithTimeStr = "";
                        String BuildedParamsStr = "";
                        for (int i = 0; i < BuildedParamsWithTime.length; i++) {

                            timeparams.add(BuildedParamsWithTime[i]);
                            //BuildedParamsWithTimeStr += "," + BuildedParamsWithTime[i];
                            // //////////////////.println("BuildedParamsWithTime="+BuildedParamsWithTime[i]);
                        }
                        //  BuildedParamsWithTimeStr = BuildedParamsWithTimeStr.substring(1);

                        //////////////////.println("");

                        for (int j = 0; j < BuildedParams.length; j++) {
                            //BuildedParamsStr += "," + BuildedParams[j];
                            params.add(BuildedParams[j]);

                            ////////////////////.println("BuildedParams="+BuildedParams[j]);

                        }

                        // ////////////////.println("params="+params);
                        //  ////////////////.println("timeDetsMap="+timeDetsMap);
                        // // ////////////////.println("timeDetails="+timeDetails);
                        //  ////////////////.println("paramNames="+paramNames);


                        paramList = params;
                        TimeDimHashMap = timeDetsMap;
                        TimeDetailstList = timeDetails;
                        // ParametersNames=paramNames;
                    }
                }
            }

            //added by k over

            ////.println("TimeDetailstList="+TimeDetailstList);




            ArrayList reportQryElementIds = new ArrayList();
            ArrayList reportQryAggregations = new ArrayList();

            if (paramList != null) {

                for (int i = 0; i < paramList.size(); i++) {
                    if (paramValues.get(String.valueOf(paramList.get(i))) == null) {
                        paramValues.put(String.valueOf(paramList.get(i)), "All");
                        reportIncomingParameters.put("CBOAPR" + String.valueOf(paramList.get(i)), null);
                    }
                }

            }

            if (REP == null) {
                REP = new ArrayList();
                if (paramList != null) {
                    REP.add(String.valueOf(paramList.get(0)));
                }
            }
            if (CEP == null) {
                CEP = new ArrayList();
            }
            collect.reportIncomingParameters = reportIncomingParameters;
            collect.reportColViewbyValues = CEP;
            collect.reportRowViewbyValues = REP;
            collect.timeDetailsArray = TimeDetailstList;
            collect.timeDetailsMap = TimeDimHashMap;
            collect.getParamMetaDataForReportDesigner();
            for (int j = 0; j < graphColumns.size(); j++) {
                if (!reportQryElementIds.contains(String.valueOf(graphColumns.get(j)).replace("A_", ""))) {
                    reportQryElementIds.add(String.valueOf(graphColumns.get(j)).replace("A_", ""));
                }
            }

            //////////////.println("reportQryElementIds="+reportQryElementIds);
            if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
                reportQryAggregations = reportTemplateDAO.getReportQryAggregations(reportQryElementIds);
                collect.reportQryElementIds = reportQryElementIds;
                collect.reportQryAggregations = reportQryAggregations;
                repQuery.setQryColumns(collect.reportQryElementIds);
                repQuery.setColAggration(collect.reportQryAggregations);
                repQuery.setDefaultMeasure(String.valueOf(reportQryElementIds.get(0)));
                repQuery.setDefaultMeasureSumm(String.valueOf(reportQryAggregations.get(0)));
            }

            repQuery.setRowViewbyCols(REP);
            repQuery.setColViewbyCols(CEP);
            repQuery.setParamValue(paramValues);
            repQuery.setTimeDetails(TimeDetailstList); //assigning time details array to report query
            if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
                pbretObj = repQuery.getPbReturnObject(String.valueOf(reportQryElementIds.get(0)));
                container.setOriginalColumns(AllColumns);
                container.setDisplayedSet(pbretObj);

                //////////////.println("container.getDisplayedSet()"+container.getDisplayedSet());
                recordSet = container.getDisplayedSet();
            }

        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return recordSet;
    }

    public PbReturnObject getRecordSetForGraphFX(HashMap ParametersHashMap, ArrayList graphColumns, ArrayList AllColumns, String customDbrdId) {

        PbReturnObject pbretObj = null;
        PbReportCollection collect = new PbReportCollection();
        PbReportQuery repQuery = new PbReportQuery();
        HashMap reportIncomingParameters = null;
        HashMap paramValues = null;
        HashMap TimeDimHashMap = null;
        ArrayList paramList = null;
        ArrayList TimeDetailstList = null;
        ArrayList REP = null;
        ArrayList CEP = null;
        Container container = new Container();
        ArrayList recordSet = new ArrayList();
        try {
            reportIncomingParameters = new HashMap();
            paramValues = new HashMap();
            paramList = (ArrayList) ParametersHashMap.get("Parameters");
            TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
            TimeDetailstList = (ArrayList) ParametersHashMap.get("TimeDetailstList");



            //////////////.println("paramList="+paramList);

            //////////////.println("TimeDimHashMap="+TimeDimHashMap);
            //////////////.println("TimeDetailstList="+TimeDetailstList);






            ArrayList params = new ArrayList();
            ArrayList timeparams = new ArrayList();
            ArrayList timeDetails = new ArrayList();
            ArrayList paramNames = new ArrayList();






            if (paramList == null) {

                ////.println("entered inside paramList");
                if (getSession() != null) {
                    HashMap map = (HashMap) getSession().getAttribute("PROGENTABLES");
                    if (map != null) {

                        Container contain = (Container) map.get(customDbrdId);

                        // ////////////////.println("entered");

                        paramNames = contain.getReportParameterNames();
                        HashMap hm = contain.getTimeParameterHashMap();
                        timeDetails = (ArrayList) hm.get("TimeDetailstList");
                        HashMap hmobj = contain.getReportParameterHashMap();

                        // setReportParameterHashMap()

                        //HashMap reportParameters = (HashMap) hmobj.get("reportParameters");
                        HashMap reportParametersValues = (HashMap) hmobj.get("reportParametersValues");


                        HashMap timeDetsMap = (HashMap) hm.get("TimeDimHashMap");
                        ////////////////////.println("Dasboard Kpi hm="+hm);

                        String[] BuildedParamsWithTime = (String[]) timeDetsMap.keySet().toArray(new String[0]); //contains 31150,31149,31156,31153
                        String[] BuildedParams = (String[]) reportParametersValues.keySet().toArray(new String[0]);
                        // String[] kpiElmnts = (String[]) kpiHashMap.keySet().toArray(new String[0]);
                        String BuildedParamsWithTimeStr = "";
                        String BuildedParamsStr = "";
                        for (int i = 0; i < BuildedParamsWithTime.length; i++) {

                            timeparams.add(BuildedParamsWithTime[i]);
                            //BuildedParamsWithTimeStr += "," + BuildedParamsWithTime[i];
                            // //////////////////.println("BuildedParamsWithTime="+BuildedParamsWithTime[i]);
                        }
                        //  BuildedParamsWithTimeStr = BuildedParamsWithTimeStr.substring(1);

                        //////////////////.println("");

                        for (int j = 0; j < BuildedParams.length; j++) {
                            //BuildedParamsStr += "," + BuildedParams[j];
                            params.add(BuildedParams[j]);

                            ////////////////////.println("BuildedParams="+BuildedParams[j]);

                        }

                        // ////////////////.println("params="+params);
                        //  ////////////////.println("timeDetsMap="+timeDetsMap);
                        // // ////////////////.println("timeDetails="+timeDetails);
                        //  ////////////////.println("paramNames="+paramNames);


                        paramList = params;
                        TimeDimHashMap = timeDetsMap;
                        TimeDetailstList = timeDetails;
                        // ParametersNames=paramNames;
                    }
                }
            }

            //added by k over

            ////.println("TimeDetailstList="+TimeDetailstList);




            ArrayList reportQryElementIds = new ArrayList();
            ArrayList reportQryAggregations = new ArrayList();

            if (paramList != null) {

                for (int i = 0; i < paramList.size(); i++) {
                    if (paramValues.get(String.valueOf(paramList.get(i))) == null) {
                        paramValues.put(String.valueOf(paramList.get(i)), "All");
                        reportIncomingParameters.put("CBOAPR" + String.valueOf(paramList.get(i)), null);
                    }
                }

            }

            if (REP == null) {
                REP = new ArrayList();
                if (paramList != null) {
                    REP.add(String.valueOf(paramList.get(0)));
                }
            }
            if (CEP == null) {
                CEP = new ArrayList();
            }
            collect.reportIncomingParameters = reportIncomingParameters;
            collect.reportColViewbyValues = CEP;
            collect.reportRowViewbyValues = REP;
            collect.timeDetailsArray = TimeDetailstList;
            collect.timeDetailsMap = TimeDimHashMap;
            collect.getParamMetaDataForReportDesigner();
            for (int j = 0; j < graphColumns.size(); j++) {
                if (!reportQryElementIds.contains(String.valueOf(graphColumns.get(j)).replace("A_", ""))) {
                    reportQryElementIds.add(String.valueOf(graphColumns.get(j)).replace("A_", ""));
                }
            }

            //////////////.println("reportQryElementIds="+reportQryElementIds);
            if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
                reportQryAggregations = reportTemplateDAO.getReportQryAggregations(reportQryElementIds);
                collect.reportQryElementIds = reportQryElementIds;
                collect.reportQryAggregations = reportQryAggregations;
                repQuery.setQryColumns(collect.reportQryElementIds);
                repQuery.setColAggration(collect.reportQryAggregations);
                repQuery.setDefaultMeasure(String.valueOf(reportQryElementIds.get(0)));
                repQuery.setDefaultMeasureSumm(String.valueOf(reportQryAggregations.get(0)));
            }

            repQuery.setRowViewbyCols(REP);
            repQuery.setColViewbyCols(CEP);
            repQuery.setParamValue(paramValues);
            repQuery.setTimeDetails(TimeDetailstList); //assigning time details array to report query
            if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
                ////.println("00000000000000000000");
                pbretObj = repQuery.getPbReturnObject(String.valueOf(reportQryElementIds.get(0)));
                container.setOriginalColumns(AllColumns);
                container.setDisplayedSet(pbretObj);

                //////////////.println("container.getDisplayedSet()"+container.getDisplayedSet());
                recordSet = container.getDisplayedSet();
            }

        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return pbretObj;
    }

    public String getThumbNail(HttpServletRequest request, HttpServletResponse response, String path) {
        String userId = "10";
        String templId = "11";
        String targetPath = "";
        HttpSession session = request.getSession(false);
        //Process process = null;
        //targetPath=request.getRequestURL().toString();
        targetPath = " http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID=804";

        StringBuffer urlPath = new StringBuffer("");
        Runtime run = Runtime.getRuntime();
        String output = "";

        if (session != null) {
            urlPath.append(path + "IECapt.exe ");
            //urlPath.append(" http://www.gmail.com ");
            urlPath.append(" " + targetPath + " ");
            //urlPath.append(" http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" + targetPath);
            urlPath.append(" " + path + "Img" + userId + "_" + templId + ".png");


            try {
                //run.exec(urlPath.toString());
                //run.exec("F:\\QD-26-10-2009\\QueryDesigner\\IECapt.exe   http://localhost:8082/QueryDesigner/newjsp.jsp F:\\QD-26-10-2009\\QueryDesigner\\Img10_11.png");
                //process = run.exec(urlPath.toString());

                /*
                 * Thread.currentThread().sleep(10000);//if 10000 its working
                 * fine BufferedImage image = ImageIO.read(new File(path + "Img"
                 * + userId + "_" + templId + ".png")); image =
                 * getScaledInstance(image, 150, 150,
                 * RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                 * ImageIO.write(image, "PNG", new File(path + "Img" + userId +
                 * "_" + templId + "Thumb.png"));
                 *
                 * output = "<img src=" + path + "Img" + userId + "_" + templId
                 * + "Thumb.png>"; //output = "<img src=" +
                 * request.getContextPath() + "/Img" + userId + "_" + templId +
                 * "Thumb.png>";
                 */
            } catch (Exception e) {
                logger.error("Exception:", e);
            }
        } else {
        }
        return output;

    }

    public BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint) {
        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage) img;

        int w = img.getWidth();
        int h = img.getHeight();

        do {
            if (w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGraphByGraphId(String divId, String reportId, String graphId, HttpServletRequest request, HttpServletResponse response,
            HttpSession session, String dbrdId, boolean isFxCharts, boolean addedToDashboard) throws Exception {

        StringBuilder imageBuffer = new StringBuilder("");
        if (isFxCharts) {
            imageBuffer.append("<iframe frameborder='0' scrolling='No' NAME='iframe_" + graphId + "_" + dbrdId + "'  id='iframe_" + graphId + "_" + dbrdId + "' STYLE='width:100%;height:100%;overflow:auto;' src='" + request.getContextPath() + "/PbFXDashBoardReportGraphXMLBuild.jsp?dbrdId=" + dbrdId + "&graphId=" + graphId + "&reportId=" + reportId + "'></iframe>");
        } else {
            PbGraphDisplay pbDisplay = new PbGraphDisplay();
            HashMap ParametersMap = null;
            HashMap map = null;
            Container container = null;
            ArrayList Parameters = new ArrayList();
            ArrayList REP = new ArrayList();
            ArrayList ParameterNames = new ArrayList();
            String[] viewByElementIds = new String[0];
            String[] viewByDispNames = new String[0];
            PbReportViewerBD repViewBD = new PbReportViewerBD();
            ArrayList grpDetails = new ArrayList();
            ProgenChartDisplay[] pcharts = null;
            map = (HashMap) session.getAttribute("PROGENTABLES");
            String userId = String.valueOf(session.getAttribute("USERID"));
            container = (Container) map.get(dbrdId);
            ParametersMap = (HashMap) (container.getParametersHashMap()).clone();
            pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
            int graphWidth = 320;
            boolean fromDesigner = true;
            String editDbrd = null;

            DashboardViewerDAO dao = new DashboardViewerDAO();
            GraphReport graphDetails = (GraphReport) dao.getGraphDetails(reportId, graphId);
            DashletDetail dashlet = null;

            GraphBuilder grpBuilder = new GraphBuilder();

            if (addedToDashboard) {
                dashlet = collect.getDashletDetail(divId);
                int rowSpan = dashlet.getRowSpan();
                int colSpan = dashlet.getColSpan();
                int totalCols = grpBuilder.getTotalColumns(collect, divId);
                graphWidth = grpBuilder.getGraphWidth(dashlet.getRowSpan(), dashlet.getColSpan(), totalCols);
                int graphHeight = 320;
                graphHeight = graphHeight * rowSpan;
                graphWidth = graphWidth * colSpan;
//                graphDetails.setGraphSize(graphSize);
//                graphDetails.setGraphSizeName(ProgenGraphInfo.getGraphSizeName(graphSize));
//                List<Integer> graphDim=ProgenGraphInfo.getGraphDimensions(graphSize);
                graphDetails.setGraphHeight(String.valueOf(graphHeight));
                graphDetails.setGraphWidth(String.valueOf(graphWidth));
            } else {
                dashlet = new DashletDetail();
                dashlet.setDashBoardDetailId(divId);
            }
            dashlet.setRefReportId(reportId);
            dashlet.setGraphId(graphId);
            dashlet.setKpiMasterId("0");
            dashlet.setDisplaySequence(0);
            dashlet.setDisplayType(DashboardConstants.GRAPH_REPORT);
            dashlet.setDashletName(graphDetails.getGraphName());
            dashlet.setKpiType(null);
            dashlet.setReportDetails(graphDetails);

            String dashletName = dashlet.getDashletName();

            if (ParametersMap != null || ParametersMap.size() > 0) {
                Parameters = (ArrayList) ParametersMap.get("Parameters");
                ParameterNames = (ArrayList) ParametersMap.get("Parameters");
            }
            if (Parameters == null) {
                ArrayList params = new ArrayList();
                HashMap hmobj = container.getReportParameterHashMap();
                HashMap reportParametersValues = (HashMap) hmobj.get("reportParametersValues");
                String[] BuildedParams = (String[]) reportParametersValues.keySet().toArray(new String[0]);

                for (int j = 0; j < BuildedParams.length; j++) {
                    params.add(BuildedParams[j]);
                }
                Parameters = params;
                ParameterNames = params;
            }

            String ViewByType = "";
            PbDb pbdb1 = new PbDb();
            try {
                String query = "select DEFAULT_VALUE from PRG_AR_REPORT_VIEW_BY_MASTER where REPORT_ID=" + reportId;
                PbReturnObject graphDetailReturnObj = pbdb1.execSelectSQL(query);
                ViewByType = graphDetailReturnObj.getFieldValueString(0, "DEFAULT_VALUE");
            } catch (Exception e) {
            }

            // newparameters arraylist is added because of retain old hashmap persistant
            ArrayList newParameters = new ArrayList();
            if (Parameters != null) {
                for (int i = 0; i < Parameters.size(); i++) {
                    newParameters.add(Parameters.get(i));
                }
                if (ViewByType.equalsIgnoreCase("Time")) {
                    newParameters.set(0, "Time");
                    ParametersMap.put("Parameters", newParameters);
                } else {
                    ParametersMap.put("Parameters", newParameters);
                }
            }

            PbReturnObject graphDataSet = null;

            if (addedToDashboard) {
                ArrayList<String> reportQryAggregations = new ArrayList<String>();
                ArrayList<String> reportQryElementIds = new ArrayList<String>();

                List<QueryDetail> queryDetails = graphDetails.getQueryDetails();
                for (QueryDetail qd : queryDetails) {
                    reportQryAggregations.add(qd.getAggregationType());
                    reportQryElementIds.add(qd.getElementId());
                }

//                collect.addDashletDetail(dashlet);
                if ("Time".equalsIgnoreCase(ViewByType)) {
                    graphDetails.setTimeSeries(true);
                    String grpType = "Line";
                    Integer graphTypeId = ProgenGraphInfo.getGraphTypeId(grpType);
                    Integer graphClassId = ProgenGraphInfo.getGraphClassId(graphTypeId);
                    graphDetails.setGraphType(graphTypeId);
                    graphDetails.setGraphTypeName(grpType);
                    graphDetails.setGraphClass(graphClassId);
                    graphDetails.getGraphProperty().setSwapGraphColumns("true");
                    PbDashboardViewerBD viewerBD = new PbDashboardViewerBD();
                    graphDataSet = viewerBD.getTimeSeriesData(collect, userId);
                    container.setTimeSeriesRetObj(graphDataSet);
                } else {
                    collect.addQryColumns(reportQryElementIds, reportQryAggregations);
                    PbDashboardViewerBD viewerBD = new PbDashboardViewerBD();
                    graphDataSet = viewerBD.getDashboardData(container, collect, userId);
                }
            } else {
                if ("Time".equalsIgnoreCase(ViewByType)) {
                    REP.add("TIME");
                    String grpType = "Line";
                    Integer graphTypeId = ProgenGraphInfo.getGraphTypeId(grpType);
                    Integer graphClassId = ProgenGraphInfo.getGraphClassId(graphTypeId);
                    graphDetails.setGraphType(graphTypeId);
                    graphDetails.setGraphTypeName(grpType);
                    graphDetails.setGraphClass(graphClassId);
                    graphDetails.getGraphProperty().setSwapGraphColumns("true");
                } else {
                    if (collect.reportRowViewbyValues == null || collect.reportRowViewbyValues.isEmpty()) {
                        REP.add(((String[]) newParameters.toArray(new String[0]))[0]);
                    } else {
                        REP = collect.reportRowViewbyValues;
                    }
                }
                graphDataSet = repViewBD.getGraphsDataForDashBoard(reportId, graphId, request, response, session, REP);
            }

            if (newParameters != null && !newParameters.isEmpty()) {
                viewByElementIds = new String[1];
                viewByDispNames = new String[1];

                if (((String[]) newParameters.toArray(new String[0]))[0].equalsIgnoreCase("Time")) {
                    viewByElementIds[0] = "TIME";//((String[]) newParameters.toArray(new String[0]))[0];
                    viewByDispNames[0] = "TIME";//((String[]) newParameters.toArray(new String[0]))[0];
                } else {
                    if (ViewByType.equalsIgnoreCase("Time")) {
                        viewByElementIds[0] = "Time";
                        viewByDispNames[0] = "Time";
                    } else {
                        if (collect.reportRowViewbyValues == null || collect.reportRowViewbyValues.isEmpty()) {
                            viewByElementIds[0] = "A_" + ((String[]) newParameters.toArray(new String[0]))[0];
                            viewByDispNames[0] = "A_" + ((String[]) newParameters.toArray(new String[0]))[0];
                        } else {
                            viewByElementIds[0] = "A_" + collect.reportRowViewbyValues.get(0);
                            viewByDispNames[0] = "A_" + collect.reportRowViewbyValues.get(0);
                        }
                    }
                }
//                REP.add(viewByElementIds[0].replace("A_", ""));
            }

            if (addedToDashboard) {
//                GraphBuilder gb = new GraphBuilder();
                grpBuilder.setRequest(request);
                grpBuilder.setResponse(response);
                grpBuilder.setFxCharts(isFxCharts);
                String result = grpBuilder.displayGraphs(container, divId, request.getContextPath(), fromDesigner, editDbrd);
                imageBuffer.append(result);
            } else {
                pbDisplay.setReportId(reportId);
                pbDisplay.setCurrentDispRetObjRecords(graphDataSet);
                pbDisplay.setCurrentDispRecordsRetObjWithGT(graphDataSet);
                pbDisplay.setAllDispRecordsRetObj(graphDataSet);
                pbDisplay.setOut(response.getWriter());
                pbDisplay.setResponse(response);
                pbDisplay.setViewByElementIds(viewByElementIds);
                pbDisplay.setViewByColNames(viewByDispNames);
                pbDisplay.setJscal(null);
                pbDisplay.setCtxPath(request.getContextPath());
                pbDisplay.setShowGT("N");
                pbDisplay.setSession(session);
                grpDetails = pbDisplay.getDashboardGraphHeadersNew(reportId, graphId, dashlet);
                //            grpDetails = pbDisplay.getGraphByGraphId(reportId, graphId, ParametersMap);

                if (grpDetails != null && !grpDetails.isEmpty()) {
                    pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
                    if (pcharts != null && pcharts.length != 0) {
                        imageBuffer.append(pcharts[0].chartDisplay);
                    }
                }
            }
//            
        }
        return imageBuffer.toString();
    }

    public void deleteDbrdGraph(Container container, String dashletId) {
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        collect.removeDashletDetail(dashletId);
    }

    public StringBuilder buildDbrdGraphs(HttpServletRequest request, HttpServletResponse response, boolean isFxCharts) {
        ArrayList grpDetails = new ArrayList();
        PbGraphDisplay GraphDisplay = new PbGraphDisplay();
        GraphDisplay.setCtxPath(request.getContextPath());
        StringBuilder graphsBuffer = new StringBuilder("");
        Container container = new Container();
        PbReturnObject pbretObj = null;
        HttpSession session = request.getSession(false);
        HashMap map = null;
        String customDbrdId = "";
        HashMap GraphSizesHashMap = null;
        HashMap GraphTypesHashMap = null;
        HashMap newDbrdGraph = null;
        String[] GraphSizesKeySet = null;
        String[] GraphSTypesKeySet = null;
        String[] axis = null;
        ArrayList REP = new ArrayList();
        ArrayList CEP = new ArrayList();
        ArrayList paramList = null;
        ArrayList ParametersNames = null;
        HashMap GraphClassesHashMap;
        HashMap GraphSizesDtlsHashMap;
        String[] barChartColumnNames = null;
        String[] pieChartColumns = null;
        String[] barChartColumnTitles = null;
        String[] viewByElementIds = null;
        String[] viewBys = null;
        String[] viewByNames = null;
        String[] graphCols = new String[0];
        GraphTypesHashMap = new HashMap();
        GraphClassesHashMap = new HashMap();
        GraphSizesHashMap = new HashMap();
        GraphSizesDtlsHashMap = new HashMap();
        pbDashboardCollection collect = null;//new pbDashboardCollection();
        PbReportQuery repQuery = new PbReportQuery();
        HashMap reportIncomingParameters = null;
        HashMap paramValues = null;
        HashMap TimeDimHashMap = null;
        ArrayList TimeDetailstList = null;
        ArrayList AllGraphColumns = new ArrayList();
        HashMap grpColsbyDivMap = null;
        GraphReport graphDetails = new GraphReport();
        boolean newDbrd = false;
        boolean fromDesigner = true;
        String editDbrd = null;
        String tableString = "";
        boolean flag = false;
        LinkedHashMap<String, String> parammap = new LinkedHashMap<String, String>();
        String DashboardGraphName = "";
        try {
            String grpType = request.getParameter("grpType");
            String kpiType = request.getParameter("kpiType");
            String grpColumns = "";
            String grpColumnsNames = "";
            String groupId = "";
            if (grpType.equalsIgnoreCase("groupMeassure")) {
                groupId = request.getAttribute("groupId").toString();
                grpColumns = request.getAttribute("grpColumns").toString();
                grpColumnsNames = request.getAttribute("grpColumnsNames").toString();
            } else {
                grpColumns = request.getParameter("grpColumns");
                grpColumnsNames = request.getParameter("grpColumnsNames");
            }

            String graphSizeStr = request.getParameter("grpSize");
            String divId = request.getParameter("divId");
            String grpId = request.getParameter("grpId");
            String viewby = request.getParameter("viewby");
            String selectedDimId = request.getParameter("selectedDim");

            map = (HashMap) session.getAttribute("PROGENTABLES");
            customDbrdId = request.getParameter("dashboardId");
            DashboardGraphName = request.getParameter("DashboardGraphName");
            if (DashboardGraphName == null) {
                DashboardGraphName = "";
            }

            container = (Container) map.get(customDbrdId);
            collect = (pbDashboardCollection) container.getReportCollect();
            collect.setTextKPIRowViewBy(selectedDimId);
            if (collect == null) {
                collect = new pbDashboardCollection();
                newDbrd = true;
            }

            DashletDetail detail = collect.getDashletDetail(divId);

            if (detail.getDashletName() == null || detail.getDashletName().equals("")) {
                //detail = new DashletDetail();
                detail.setDashBoardDetailId(divId);
                detail.setRefReportId(customDbrdId);
                detail.setGraphId(grpId);
                detail.setKpiMasterId("0");
                detail.setDisplaySequence(-1);
                detail.setDisplayType(displayType);
                detail.setDashletName(DashboardGraphName);
                detail.setKpiType(null);
                detail.setReportDetails(graphDetails);
                detail.setRowViewBy(collect.getTextKPIRowViewBy());
                detail.setGroupId(groupId);
//                detail.setGraphtype(grpType);
                // collect.addDashletDetail(detail);
            }
            int graphWidth = 320;
            int rowSpan = detail.getRowSpan();
            int colSpan = detail.getColSpan();
            GraphBuilder grpBuilder = new GraphBuilder();
            int totalCols = grpBuilder.getTotalColumns(collect, divId);
            graphWidth = grpBuilder.getGraphWidth(rowSpan, colSpan, totalCols);
            graphDetails.setGraphSize(1);
//            graphDetails.setGraphSizeName(ProgenGraphInfo.getGraphSizeName(graphSize));
//            graphSizeStr = ProgenGraphInfo.getGraphSizeName(1);
            int graphHeight = 320;
            graphHeight = graphHeight * rowSpan;
            graphWidth = graphWidth * colSpan;
            if (kpiType != null && kpiType.equalsIgnoreCase("KpiWithGraph")) {
                detail.setKpiType(kpiType);
                detail.setDisplayType(kpiType);
                detail.setReportDetails(graphDetails);
                graphWidth = 3 * (graphWidth / 5);
                grpColumns = grpColumns.substring(0, grpColumns.indexOf(","));
                grpColumnsNames = grpColumnsNames.substring(0, grpColumnsNames.indexOf(","));

            }
//                graphDetails.setGraphSize(graphSize);
//                graphDetails.setGraphSizeName(ProgenGraphInfo.getGraphSizeName(graphSize));
//                List<Integer> graphDim=ProgenGraphInfo.getGraphDimensions(graphSize);
            graphDetails.setGraphHeight(String.valueOf(graphHeight));
            graphDetails.setGraphWidth(String.valueOf(graphWidth));
            HashMap ParametersHashMap = container.getParametersHashMap();//(HashMap) Session.getAttribute("ParametersHashMap");
            paramList = (ArrayList) ParametersHashMap.get("Parameters");
            ParametersNames = (ArrayList) ParametersHashMap.get("ParameterNames");
            reportIncomingParameters = new HashMap();
            paramValues = new HashMap();
            TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
            TimeDetailstList = (ArrayList) ParametersHashMap.get("TimeDetailstList");

            ArrayList params = new ArrayList();
            ArrayList timeparams = new ArrayList();
            ArrayList timeDetails = new ArrayList();
            ArrayList paramNames = new ArrayList();
            if (paramList.size() <= 1) {
                if ((paramList.get(0).equals("") || paramList.get(0).equals("TIME")) && (ParametersNames.get(0).equals("") || ParametersNames.get(0).equals("TIME"))) {
                    viewby = "TIME";
                }
            }
            if ("TIME".equalsIgnoreCase(viewby)) {
                if (!grpType.equalsIgnoreCase("table") && !grpType.equalsIgnoreCase("groupMeassure")) {
                    grpType = "Line";
                }

                graphDetails.setTimeSeries(true);
            }
            viewBy = String.valueOf(request.getParameter("viewby"));

            setViewBy(viewBy);


            if (container.getGrpColsbyDivMap() == null) {
                grpColsbyDivMap = new HashMap();
            } else {
                grpColsbyDivMap = container.getGrpColsbyDivMap();//(HashMap) Session.getAttribute("grpColsbyDivMap");
            }
            grpColsbyDivMap.put(divId, grpColumns + "^" + grpColumnsNames);
            container.setGrpColsbyDivMap(grpColsbyDivMap);

            GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");
            GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
            GraphSizesHashMap = (HashMap) session.getAttribute("GraphSizesHashMap");
            GraphSizesDtlsHashMap = (HashMap) session.getAttribute("GraphSizesDtlsHashMap");
            GraphSTypesKeySet = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);
            GraphSizesKeySet = (String[]) GraphSizesHashMap.keySet().toArray(new String[0]);

            ArrayList sizeDtls = (ArrayList) GraphSizesDtlsHashMap.get(graphSizeStr);
            String graphClass = String.valueOf(GraphClassesHashMap.get(grpType));
            String[] grpColIds = null;
            if (grpColumns != null && !"".equalsIgnoreCase(grpColumns)) {
                grpColIds = grpColumns.split(",");
                for (int i = 0; i < grpColIds.length; i++) {
                    AllGraphColumns.add("A_" + grpColIds[i]);
                }
            }


            map = (HashMap) session.getAttribute("PROGENTABLES");
            String userId = String.valueOf(session.getAttribute("USERID"));
            customDbrdId = request.getParameter("dashboardId");
            container = (Container) map.get(customDbrdId);

            if (ParametersNames == null) {
                paramNames = container.getReportParameterNames();
                HashMap hm = container.getTimeParameterHashMap();
                timeDetails = (ArrayList) hm.get("TimeDetailstList");
                HashMap hmobj = container.getReportParameterHashMap();
                HashMap reportParametersValues = (HashMap) hmobj.get("reportParametersValues");
                HashMap timeDetsMap = (HashMap) hm.get("TimeDimHashMap");

                String[] BuildedParamsWithTime = (String[]) timeDetsMap.keySet().toArray(new String[0]); //contains 31150,31149,31156,31153
                String[] BuildedParams = (String[]) reportParametersValues.keySet().toArray(new String[0]);
                // String[] kpiElmnts = (String[]) kpiHashMap.keySet().toArray(new String[0]);
                String BuildedParamsWithTimeStr = "";
                String BuildedParamsStr = "";
                for (int i = 0; i < BuildedParamsWithTime.length; i++) {
                    timeparams.add(BuildedParamsWithTime[i]);
                }

                for (int j = 0; j < BuildedParams.length; j++) {
                    //BuildedParamsStr += "," + BuildedParams[j];
                    params.add(BuildedParams[j]);
                }

                paramList = params;
                TimeDimHashMap = timeDetsMap;
                TimeDetailstList = timeDetails;
                ParametersNames = paramNames;
            }

            if (graphDetails.isTimeSeries()) {
                viewByElementIds = new String[1];
                viewByElementIds[0] = "TIME";
                viewBys = viewByElementIds;
                viewByNames = new String[1];
                viewByNames[0] = "TIME";
                //for setting time
                ArrayList arl = paramList;
                if (arl.isEmpty()) {
                    arl.add("TIME");
                } else {
                    arl.set(0, "TIME");
                }
                paramList = arl;
            } else {
                viewByElementIds = new String[1];
                if (collect.reportRowViewbyValues != null && !collect.reportRowViewbyValues.isEmpty()) {
                    viewByElementIds[0] = "A_" + collect.reportRowViewbyValues.get(0);
                } else {
                    viewByElementIds[0] = String.valueOf(paramList.get(0));
                }
                viewBys = viewByElementIds;
                viewByNames = new String[1];
                if (collect.reportRowViewbyValues != null && !collect.reportRowViewbyValues.isEmpty()) {
                    viewByNames[0] = collect.getParameterDispName(collect.reportRowViewbyValues.get(0));
                } else {
                    viewByNames[0] = String.valueOf(ParametersNames.get(0));
                }
            }

            if (paramList != null && !paramList.isEmpty()) {
                REP.add(String.valueOf(paramList.get(0)));
                for (int i = 0; i < paramList.size(); i++) {
                    if (paramValues.get(String.valueOf(paramList.get(i))) == null) {
                        paramValues.put(String.valueOf(paramList.get(i)), "All");
                        parammap.put(String.valueOf(paramList.get(i)), "All");
                        reportIncomingParameters.put("CBOAPR" + String.valueOf(paramList.get(i)), null);
                    }
                }
            }
            collect.reportParametersValues = parammap;
            container.setParametersHashMap(ParametersHashMap);
            ArrayList reportQryElementIds = new ArrayList();
            ArrayList reportQryAggregations = new ArrayList();

            if (newDbrd) {
                collect.reportIncomingParameters = reportIncomingParameters;
                collect.reportColViewbyValues = CEP;
                collect.reportRowViewbyValues = REP;
                collect.timeDetailsArray = TimeDetailstList;
                collect.timeDetailsMap = TimeDimHashMap;
                collect.reportId = customDbrdId;
                collect.reportParamIds = (ArrayList) ParametersHashMap.get("Parameters");
                collect.reportParamNames = (ArrayList) ParametersHashMap.get("ParameterNames");
                collect.reportParametersValues = new LinkedHashMap();
                collect.getParamMetaDataForReportDesigner();
            }

            PbDashboardViewerBD viewerBD = new PbDashboardViewerBD();
            PbReturnObject retObj = DAO.getNewDbrdGrpDets(grpColumns);

            List<QueryDetail> queryDetails = new ArrayList<QueryDetail>();
            if (retObj != null && retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    QueryDetail qd = new QueryDetail();
                    qd.setElementId(retObj.getFieldValueString(i, 1));
                    qd.setDisplayName(retObj.getFieldValueString(i, 0));
                    qd.setRefElementId(retObj.getFieldValueString(i, 2));
                    qd.setFolderId(retObj.getFieldValueInt(i, 3));
                    qd.setSubFolderId(retObj.getFieldValueInt(i, 4));
                    qd.setAggregationType(retObj.getFieldValueString(i, 5));
                    qd.setColumnType(retObj.getFieldValueString(i, 6));
                    queryDetails.add(qd);
                    reportQryAggregations.add(retObj.getFieldValueString(i, 5));
                    reportQryElementIds.add(retObj.getFieldValueString(i, 1));
                }
            }
            graphDetails.setQueryDetails(queryDetails);
            if (graphDetails.isTimeSeries()) {
                pbretObj = viewerBD.getTimeSeriesData(collect, userId);
                container.setTimeSeriesRetObj(pbretObj);
            } else {
                collect.addQryColumns(reportQryElementIds, reportQryAggregations);
                if (grpType.equalsIgnoreCase("textKpi")) {
                    pbretObj = viewerBD.getDashboardTextKPIData(container, collect, userId, detail);
                } else {
                    pbretObj = viewerBD.getDashboardData(container, collect, userId);
                }
            }

            graphCols = (String[]) AllGraphColumns.toArray(new String[0]);
            barChartColumnNames = new String[graphCols.length + viewByElementIds.length];
            pieChartColumns = new String[barChartColumnNames.length];
            barChartColumnTitles = new String[barChartColumnNames.length];
            axis = new String[barChartColumnNames.length];
            for (int i = 0; i < viewByElementIds.length; i++) {
                if (viewByElementIds[i].equalsIgnoreCase("Time")) {
                    viewBys[i] = viewByElementIds[i];
                    barChartColumnNames[i] = viewBys[i];
                    pieChartColumns[i] = viewBys[i];
                } else {
                    if (!(viewByElementIds[i].contains("A_"))) {
                        viewBys[i] = "A_" + viewByElementIds[i];
                        barChartColumnNames[i] = viewBys[i];
                        pieChartColumns[i] = viewBys[i];
                    } else {
                        viewBys[i] = viewByElementIds[i];
                        barChartColumnNames[i] = viewBys[i];
                        pieChartColumns[i] = viewBys[i];
                    }
                }
                barChartColumnTitles[i] = viewByNames[i];
                axis[i] = "0";
            }
            for (int i = viewByElementIds.length; i < (graphCols.length + viewByElementIds.length); i++) {
                if (!(graphCols[i - viewByElementIds.length].contains("A_"))) {
                    barChartColumnNames[i] = "A_" + graphCols[i - viewByElementIds.length];
                    pieChartColumns[i] = "A_" + graphCols[i - viewByElementIds.length];
                } else {
                    barChartColumnNames[i] = graphCols[i - viewByElementIds.length];
                    pieChartColumns[i] = graphCols[i - viewByElementIds.length];
                }
                barChartColumnTitles[i] = String.valueOf(repQuery.NonViewByMap.get(barChartColumnNames[i]));
                axis[i] = "0";
            }
            if (!grpType.equalsIgnoreCase("table") && !grpType.equalsIgnoreCase("groupMeassure") && !grpType.equalsIgnoreCase("textKpi")) {
                Integer graphTypeId = ProgenGraphInfo.getGraphTypeId(grpType);
                Integer graphClassId = ProgenGraphInfo.getGraphClassId(graphTypeId);

                graphDetails.setGraphWidth(String.valueOf(graphWidth));
                graphDetails.setGraphHeight(String.valueOf(graphHeight));
                graphDetails.setGraphClassName(graphClass);
                graphDetails.setGraphClass(graphClassId);
                graphDetails.setGraphTypeName(grpType);
                graphDetails.setGraphType(graphTypeId);
                graphDetails.setLegendAllowed(true);
                graphDetails.setLegendLocation("Bottom");
                graphDetails.setShowXAxisGrid(true);
                graphDetails.setShowYAxisGrid(true);
                graphDetails.setLeftYAxisLabel("");
                graphDetails.setRightYAxisLabel("");
                graphDetails.setLinkAllowed(true);
                graphDetails.setBackgroundColor("");
                graphDetails.setFontColor("");
                graphDetails.setShowData(true);
                graphDetails.setAxis(axis[0]);

                GraphProperty graphProp = new GraphProperty();
                graphProp.setEndValue(10);
                graphProp.setStartValue(0);
                graphProp.setNumberFormat("");
                graphProp.setSymbol("");
                graphProp.setSwapGraphColumns("true");
                graphDetails.setGraphProperty(graphProp);
                GraphBuilder gb = new GraphBuilder();
                gb.setRequest(request);
                gb.setResponse(response);
                gb.setFxCharts(isFxCharts);
                String contextPath = request.getContextPath();
                if (kpiType != null && kpiType.equalsIgnoreCase("KpiWithGraph")) //to set any defaults to kpiWithGraph graphDetails
                {
                    graphDetails.setLegendLocation("Right");

                }

                graphsBuffer.append(gb.displayGraphs(container, divId, contextPath, fromDesigner, editDbrd));
            } else {
                if (grpType.equalsIgnoreCase("groupMeassure")) {
                    GroupMeassureParams meassureParams = new GroupMeassureParams();
                    meassureParams.setDahletId(detail.getDashBoardDetailId());
                    meassureParams.setGroupId(detail.getGroupId());
                    tableString = viewerBD.builDbGroupMeassure(container, meassureParams);

                } else if (grpType.equalsIgnoreCase("textKpi")) {
                    detail.setDisplayType(grpType);
                    tableString = viewerBD.buildTextKpiTable(container, detail.getDashBoardDetailId());
                } else {
                    tableString = viewerBD.buildDbrdTable(container, detail.getDashBoardDetailId(), fromDesigner, editDbrd, flag);
                }
                graphsBuffer = new StringBuilder(tableString);
            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return graphsBuffer;
    }

    public String addScoreCard(Container container, String scoreCardIds) {

        if (scoreCardIds != null && !("".equalsIgnoreCase(scoreCardIds))) {
            DashboardTemplateDAO dao = new DashboardTemplateDAO();
            String kpiMasterId = dao.getNewDashletMasterId();
            pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
            DashletDetail detail = new DashletDetail();
            detail.setDashBoardDetailId("newScoreCard");
            detail.setRefReportId(collect.reportId);
            detail.setGraphId("0");
            detail.setKpiMasterId(kpiMasterId);
            detail.setDisplaySequence(-1);
            detail.setDisplayType("SCARD");
            detail.setDashletName("ScoreCard Region");
            detail.setKpiType(null);

            collect.addDashletDetail(detail);

            List<String> scardIds = new ArrayList<String>();
            String[] scardIdsArr = scoreCardIds.split(",");
            scardIds.addAll(Arrays.asList(scardIdsArr));

            for (String scard : scardIds) {
                collect.scoreCardDetails.put(kpiMasterId, scard);
            }

            return kpiMasterId;
        }
        return null;
    }

    public String addMap(Container container, List<String> mainMeasureList, List<String> suppMeasList, boolean isEdit, String divId, String userId) {
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail dashlet = null;//collect.getDashletDetail(divId);
        MapDetail mapDetail = null;//(MapDetail) dashlet.getReportDetails();
        ArrayList reportQryElementIds = new ArrayList();
        ArrayList reportQryAggregations = new ArrayList();
        StringBuilder measureIds = new StringBuilder(300);
//        String measureIds = "";
        for (int i = 0; i < mainMeasureList.size(); i++) {
            if (mainMeasureList.get(i).startsWith("A_")) {
//                measureIds = measureIds + "," + mainMeasureList.get(i).substring(2);
                measureIds.append(",").append(mainMeasureList.get(i).substring(2));
            } else {
//                measureIds = measureIds + "," + mainMeasureList.get(i);
                measureIds.append(",").append(mainMeasureList.get(i));
            }
        }
        if (!suppMeasList.isEmpty()) {
            for (int i = 0; i < suppMeasList.size(); i++) {
                if (suppMeasList.get(i).startsWith("A_")) {
//                    measureIds = measureIds + "," + suppMeasList.get(i).substring(2);
                    measureIds.append(",").append(suppMeasList.get(i).substring(2));
                } else {
//                    measureIds = measureIds + "," + suppMeasList.get(i);
                    measureIds.append(",").append(suppMeasList.get(i));
                }

            }
        }
//        measureIds = measureIds.substring(1);
        measureIds = new StringBuilder(measureIds.substring(1));
        if (!isEdit) {
            dashlet = new DashletDetail();
            mapDetail = new MapDetail();
            mapDetail.setPrimaryMeasure(mainMeasureList);
            mapDetail.setSupportingMeasures(suppMeasList);
            dashlet.setDashBoardDetailId(divId);
            dashlet.setRefReportId(collect.reportId);
            dashlet.setGraphId("0");
            dashlet.setKpiMasterId("0");
            dashlet.setDisplaySequence(-2);
            dashlet.setDisplayType(DashboardConstants.MAP_REPORT);
            dashlet.setDashletName("Map Region");
            dashlet.setKpiType(null);
            dashlet.setReportDetails(mapDetail);
            collect.addDashletDetail(dashlet);
        } else {
            dashlet = collect.getDashletDetail(divId);
            mapDetail = new MapDetail();
            mapDetail.setPrimaryMeasure(mainMeasureList);
            mapDetail.setSupportingMeasures(suppMeasList);
            dashlet.setRefReportId(collect.reportId);
            dashlet.setGraphId("0");
            dashlet.setKpiMasterId("0");
            dashlet.setDisplaySequence(-2);
            dashlet.setDisplayType(DashboardConstants.MAP_REPORT);
            dashlet.setDashletName("Map Region");
            dashlet.setKpiType(null);
            dashlet.setReportDetails(mapDetail);
        }

        PbDashboardViewerBD viewerBD = new PbDashboardViewerBD();
        PbReturnObject retObj = DAO.getNewDbrdGrpDets(measureIds.toString());

        List<QueryDetail> queryDetails = new ArrayList<QueryDetail>();
        if (retObj != null && retObj.getRowCount() > 0) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                QueryDetail qd = new QueryDetail();
                qd.setElementId(retObj.getFieldValueString(i, 1));
                qd.setDisplayName(retObj.getFieldValueString(i, 0));
                qd.setRefElementId(retObj.getFieldValueString(i, 2));
                qd.setFolderId(retObj.getFieldValueInt(i, 3));
                qd.setSubFolderId(retObj.getFieldValueInt(i, 4));
                qd.setAggregationType(retObj.getFieldValueString(i, 5));
                qd.setColumnType(retObj.getFieldValueString(i, 6));
                queryDetails.add(qd);
                reportQryAggregations.add(retObj.getFieldValueString(i, 5));
                reportQryElementIds.add(retObj.getFieldValueString(i, 1));
            }
        }
        mapDetail.setQueryDetails(queryDetails);
        collect.addQryColumns(reportQryElementIds, reportQryAggregations);
        PbReturnObject pbretObj = viewerBD.getDashboardData(container, collect, userId);

        MapBD mapBD = new MapBD();
        mapBD.getGeographyDimensionIds(container);

        return null;
    }

    public PbReturnObject buildDbrdGraphData(HttpServletRequest request, HttpServletResponse response, boolean isFxCharts) {
        ArrayList grpDetails = new ArrayList();
        PbGraphDisplay GraphDisplay = new PbGraphDisplay();
        GraphDisplay.setCtxPath(request.getContextPath());
        StringBuffer graphsBuffer = new StringBuffer("");
        Container container = new Container();
        PbReturnObject pbretObj = null;
        HttpSession session = request.getSession(false);
        HashMap map = null;
        String customDbrdId = "";
        HashMap GraphSizesHashMap = null;
        HashMap GraphTypesHashMap = null;
        HashMap newDbrdGraph = null;
        String[] GraphSizesKeySet = null;
        String[] GraphSTypesKeySet = null;
        String[] axis = null;
        ArrayList REP = new ArrayList();
        ArrayList CEP = new ArrayList();
        ArrayList paramList = null;
        ArrayList ParametersNames = null;
        HashMap GraphClassesHashMap;
        HashMap GraphSizesDtlsHashMap;
        String[] barChartColumnNames = null;
        String[] pieChartColumns = null;
        String[] barChartColumnTitles = null;
        String[] viewByElementIds = null;
        String[] viewBys = null;
        String[] viewByNames = null;
        String[] graphCols = new String[0];
        GraphTypesHashMap = new HashMap();
        GraphClassesHashMap = new HashMap();
        GraphSizesHashMap = new HashMap();
        GraphSizesDtlsHashMap = new HashMap();
        pbDashboardCollection collect = null;//new pbDashboardCollection();
        PbReportQuery repQuery = new PbReportQuery();
        HashMap reportIncomingParameters = null;
        HashMap paramValues = null;
        HashMap TimeDimHashMap = null;
        ArrayList TimeDetailstList = null;
        ArrayList AllGraphColumns = new ArrayList();
        HashMap grpColsbyDivMap = null;
        boolean newDbrd = false;
        try {
            String grpColumns = request.getParameter("grpColumns");
            String grpColumnsNames = request.getParameter("grpColumnsNames");
            String grpType = request.getParameter("grpType");

            String graphSize = request.getParameter("grpSize");
            String divId = request.getParameter("divId");
            String grpId = request.getParameter("grpId");
            String viewby = request.getParameter("viewby");

            map = (HashMap) session.getAttribute("PROGENTABLES");
            customDbrdId = request.getParameter("dashboardId");
            String DashboardGraphName = request.getParameter("DashboardGraphName");
            container = (Container) map.get(customDbrdId);
            collect = (pbDashboardCollection) container.getReportCollect();
            if (collect == null) {
                collect = new pbDashboardCollection();
                newDbrd = true;
            }

            DashletDetail detail = collect.getDashletDetail("newGraph" + grpId);
            GraphReport graphDetails = new GraphReport();
            if (detail == null) {
                detail = new DashletDetail();
                detail.setDashBoardDetailId("newGraph" + grpId);
                detail.setRefReportId(customDbrdId);
                detail.setGraphId(grpId);
                detail.setKpiMasterId("0");
                detail.setDisplaySequence(-1);
                detail.setDisplayType("newGraph");
                detail.setDashletName(DashboardGraphName);
                detail.setKpiType(null);
                detail.setReportDetails(graphDetails);
                collect.addDashletDetail(detail);
            }

            if ("TIME".equalsIgnoreCase(viewby)) {
                grpType = "Line";
                graphDetails.setTimeSeries(true);
            }
            viewBy = String.valueOf(request.getParameter("viewby"));

            setViewBy(viewBy);
            HashMap ParametersHashMap = container.getParametersHashMap();//(HashMap) Session.getAttribute("ParametersHashMap");


            if (container.getGrpColsbyDivMap() == null) {
                grpColsbyDivMap = new HashMap();
            } else {
                grpColsbyDivMap = container.getGrpColsbyDivMap();//(HashMap) Session.getAttribute("grpColsbyDivMap");
            }
            grpColsbyDivMap.put(divId, grpColumns + "^" + grpColumnsNames);
            container.setGrpColsbyDivMap(grpColsbyDivMap);

            GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");
            GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
            GraphSizesHashMap = (HashMap) session.getAttribute("GraphSizesHashMap");
            GraphSizesDtlsHashMap = (HashMap) session.getAttribute("GraphSizesDtlsHashMap");
            GraphSTypesKeySet = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);
            GraphSizesKeySet = (String[]) GraphSizesHashMap.keySet().toArray(new String[0]);

            ArrayList sizeDtls = (ArrayList) GraphSizesDtlsHashMap.get(graphSize);
            String graphClass = String.valueOf(GraphClassesHashMap.get(grpType));
            String[] grpColIds = null;
            if (grpColumns != null && !"".equalsIgnoreCase(grpColumns)) {
                grpColIds = grpColumns.split(",");
                for (int i = 0; i < grpColIds.length; i++) {
                    AllGraphColumns.add("A_" + grpColIds[i]);
                }
            }
            paramList = (ArrayList) ParametersHashMap.get("Parameters");
            ParametersNames = (ArrayList) ParametersHashMap.get("ParameterNames");
            reportIncomingParameters = new HashMap();
            paramValues = new HashMap();
            TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
            TimeDetailstList = (ArrayList) ParametersHashMap.get("TimeDetailstList");

            ArrayList params = new ArrayList();
            ArrayList timeparams = new ArrayList();
            ArrayList timeDetails = new ArrayList();
            ArrayList paramNames = new ArrayList();

            map = (HashMap) session.getAttribute("PROGENTABLES");
            String userId = String.valueOf(session.getAttribute("USERID"));
            customDbrdId = request.getParameter("dashboardId");
            container = (Container) map.get(customDbrdId);

            if (ParametersNames == null) {
                paramNames = container.getReportParameterNames();
                HashMap hm = container.getTimeParameterHashMap();
                timeDetails = (ArrayList) hm.get("TimeDetailstList");
                HashMap hmobj = container.getReportParameterHashMap();
                HashMap reportParametersValues = (HashMap) hmobj.get("reportParametersValues");
                HashMap timeDetsMap = (HashMap) hm.get("TimeDimHashMap");

                String[] BuildedParamsWithTime = (String[]) timeDetsMap.keySet().toArray(new String[0]); //contains 31150,31149,31156,31153
                String[] BuildedParams = (String[]) reportParametersValues.keySet().toArray(new String[0]);
                // String[] kpiElmnts = (String[]) kpiHashMap.keySet().toArray(new String[0]);
                String BuildedParamsWithTimeStr = "";
                String BuildedParamsStr = "";
                for (int i = 0; i < BuildedParamsWithTime.length; i++) {
                    timeparams.add(BuildedParamsWithTime[i]);
                }

                for (int j = 0; j < BuildedParams.length; j++) {
                    //BuildedParamsStr += "," + BuildedParams[j];
                    params.add(BuildedParams[j]);
                }

                paramList = params;
                TimeDimHashMap = timeDetsMap;
                TimeDetailstList = timeDetails;
                ParametersNames = paramNames;
            }

            if (graphDetails.isTimeSeries()) {
                viewByElementIds = new String[1];
                viewByElementIds[0] = "TIME";
                viewBys = viewByElementIds;
                viewByNames = new String[1];
                viewByNames[0] = "TIME";
                //for setting time
                ArrayList arl = paramList;
                arl.set(0, "TIME");
                paramList = arl;
            } else {
                viewByElementIds = new String[1];
                if (collect.reportRowViewbyValues != null && !collect.reportRowViewbyValues.isEmpty()) {
                    viewByElementIds[0] = "A_" + collect.reportRowViewbyValues.get(0);
                } else {
                    viewByElementIds[0] = String.valueOf(paramList.get(0));
                }
                viewBys = viewByElementIds;
                viewByNames = new String[1];
                if (collect.reportRowViewbyValues != null && !collect.reportRowViewbyValues.isEmpty()) {
                    viewByNames[0] = collect.getParameterDispName(collect.reportRowViewbyValues.get(0));
                } else {
                    viewByNames[0] = String.valueOf(ParametersNames.get(0));
                }
            }

            if (paramList != null) {
                REP.add(String.valueOf(paramList.get(0)));
                for (int i = 0; i < paramList.size(); i++) {
                    if (paramValues.get(String.valueOf(paramList.get(i))) == null) {
                        paramValues.put(String.valueOf(paramList.get(i)), "All");
                        reportIncomingParameters.put("CBOAPR" + String.valueOf(paramList.get(i)), null);
                    }
                }
            }
            ArrayList reportQryElementIds = new ArrayList();
            ArrayList reportQryAggregations = new ArrayList();

            if (newDbrd) {
                collect.reportIncomingParameters = reportIncomingParameters;
                collect.reportColViewbyValues = CEP;
                collect.reportRowViewbyValues = REP;
                collect.timeDetailsArray = TimeDetailstList;
                collect.timeDetailsMap = TimeDimHashMap;
                collect.reportId = customDbrdId;
                collect.reportParamIds = (ArrayList) ParametersHashMap.get("Parameters");
                collect.reportParamNames = (ArrayList) ParametersHashMap.get("ParameterNames");
                collect.reportParametersValues = new LinkedHashMap();
                collect.getParamMetaDataForReportDesigner();
            }

            PbDashboardViewerBD viewerBD = new PbDashboardViewerBD();
            PbReturnObject retObj = DAO.getNewDbrdGrpDets(grpColumns);

            List<QueryDetail> queryDetails = new ArrayList<QueryDetail>();
            if (retObj != null && retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    QueryDetail qd = new QueryDetail();
                    qd.setElementId(retObj.getFieldValueString(i, 1));
                    qd.setDisplayName(retObj.getFieldValueString(i, 0));
                    qd.setRefElementId(retObj.getFieldValueString(i, 2));
                    qd.setFolderId(retObj.getFieldValueInt(i, 3));
                    qd.setSubFolderId(retObj.getFieldValueInt(i, 4));
                    qd.setAggregationType(retObj.getFieldValueString(i, 5));
                    qd.setColumnType(retObj.getFieldValueString(i, 6));
                    queryDetails.add(qd);
                    reportQryAggregations.add(retObj.getFieldValueString(i, 5));
                    reportQryElementIds.add(retObj.getFieldValueString(i, 1));
                }
            }
            graphDetails.setQueryDetails(queryDetails);
            if (graphDetails.isTimeSeries()) {
                pbretObj = viewerBD.getTimeSeriesData(collect, userId);
                container.setTimeSeriesRetObj(pbretObj);
            } else {
                collect.addQryColumns(reportQryElementIds, reportQryAggregations);
                pbretObj = viewerBD.getDashboardData(container, collect, userId);
            }

            graphCols = (String[]) AllGraphColumns.toArray(new String[0]);
            barChartColumnNames = new String[graphCols.length + viewByElementIds.length];
            pieChartColumns = new String[barChartColumnNames.length];
            barChartColumnTitles = new String[barChartColumnNames.length];
            axis = new String[barChartColumnNames.length];
            for (int i = 0; i < viewByElementIds.length; i++) {
                if (viewByElementIds[i].equalsIgnoreCase("Time")) {
                    viewBys[i] = viewByElementIds[i];
                    barChartColumnNames[i] = viewBys[i];
                    pieChartColumns[i] = viewBys[i];
                } else {
                    if (!(viewByElementIds[i].contains("A_"))) {
                        viewBys[i] = "A_" + viewByElementIds[i];
                        barChartColumnNames[i] = viewBys[i];
                        pieChartColumns[i] = viewBys[i];
                    } else {
                        viewBys[i] = viewByElementIds[i];
                        barChartColumnNames[i] = viewBys[i];
                        pieChartColumns[i] = viewBys[i];
                    }
                }
                barChartColumnTitles[i] = viewByNames[i];
                axis[i] = "0";
            }
            for (int i = viewByElementIds.length; i < (graphCols.length + viewByElementIds.length); i++) {
                if (!(graphCols[i - viewByElementIds.length].contains("A_"))) {
                    barChartColumnNames[i] = "A_" + graphCols[i - viewByElementIds.length];
                    pieChartColumns[i] = "A_" + graphCols[i - viewByElementIds.length];
                } else {
                    barChartColumnNames[i] = graphCols[i - viewByElementIds.length];
                    pieChartColumns[i] = graphCols[i - viewByElementIds.length];
                }
                barChartColumnTitles[i] = String.valueOf(repQuery.NonViewByMap.get(barChartColumnNames[i]));
                axis[i] = "0";
            }

            Integer graphTypeId = ProgenGraphInfo.getGraphTypeId(grpType);
            Integer graphSizeId = ProgenGraphInfo.getGraphSizeId(graphSize);
            Integer graphClassId = ProgenGraphInfo.getGraphClassId(graphTypeId);

            graphDetails.setGraphWidth(String.valueOf(sizeDtls.get(0)));
            graphDetails.setGraphHeight(String.valueOf(sizeDtls.get(1)));
            graphDetails.setGraphClassName(graphClass);
            graphDetails.setGraphClass(graphClassId);
            graphDetails.setGraphTypeName(grpType);
            graphDetails.setGraphType(graphTypeId);
            graphDetails.setGraphSizeName(graphSize);
            graphDetails.setGraphSize(graphSizeId);
            graphDetails.setLegendAllowed(true);
            graphDetails.setLegendLocation("Bottom");
            graphDetails.setShowXAxisGrid(true);
            graphDetails.setShowYAxisGrid(true);
            graphDetails.setLeftYAxisLabel("");
            graphDetails.setRightYAxisLabel("");
            graphDetails.setLinkAllowed(true);
            graphDetails.setBackgroundColor("");
            graphDetails.setFontColor("");
            graphDetails.setShowData(true);
            graphDetails.setAxis(axis[0]);

            GraphProperty graphProp = new GraphProperty();
            graphProp.setEndValue(10);
            graphProp.setStartValue(0);
            graphProp.setNumberFormat("");
            graphProp.setSymbol("");
            graphProp.setSwapGraphColumns("true");
            graphDetails.setGraphProperty(graphProp);

            GraphDisplay.setViewByColNames(viewByNames);
            GraphDisplay.setViewByElementIds(viewBys);
            GraphDisplay.setSession(request.getSession(false));
            GraphDisplay.setResponse(response);
            GraphDisplay.setOut(response.getWriter());
            GraphDisplay.setReportId("");
            GraphDisplay.setJscal(null);

            ProgenChartDisplay[] pcharts = null;
            ProgenChartDisplay[] pchartsZoom = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return pbretObj;

    }

    public String getViewBy() {
        return viewBy;
    }

    public void setViewBy(String viewBy) {
        this.viewBy = viewBy;
    }

    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }

    public void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public void editDashbdName(String dashId, String dashbdname, String dashbddesc) {
        DAO.editDashbdName(dashId, dashbdname, dashbddesc);
    }

    public String chckDashbdNameBfrUpdate(String dashDId, String gvnDashdNm, String gvnDashDesc) {
        String status = DAO.chckDashbdNameBfrUpdate(dashDId, gvnDashdNm, gvnDashDesc);
        return status;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }
//Start of code by sandeep on 17/10/14 for schedule// update local files in oneview

    public String getGraphById1(Container container, int grpNo, boolean viewBy, OneViewLetDetails oneviewlet, OnceViewContainer onecontainer, ReportSchedule schedule) throws Exception {
        StringBuilder imageBuffer = new StringBuilder("");
        oneviewlet.setContainer(container);
        PbReportCollection collect = container.getReportCollect();
        ProgenChartDisplay[] pcharts = null;
        String reportId = oneviewlet.getRepId();
        String graphId = oneviewlet.getGrapId();
        String name = oneviewlet.getRepName();
//        String divId = oneviewlet.getNoOfViewLets();
        ArrayList grpDetails = new ArrayList();
        int height = oneviewlet.getHeight();
        int width = oneviewlet.getWidth();
        boolean oneviewReportTimeDetails = false;
        List<String> timeDetails = null;
        String repDate = null;
        boolean isOneViewTime = false;
        boolean isReportTime = false;

        isOneViewTime = true;

        String olapFunc = "";
        timeDetails = collect.timeDetailsArray;
//        session.setAttribute("timeDetails", timeDetails);

        olapFunc = "olapGraph('" + name + "','" + reportId + "','" + graphId + "','" + grpNo + "','" + timeDetails + "','false')";



        String olapRef = "javascript:olapGraph(\"" + name + "\",\"" + reportId + "\",\"" + graphId + "\",\"" + grpNo + "\")";
        ArrayList<String> sortCols = null;
        char[] sortTypes = null;//ArrayList sortTypes = null;
        char[] sortDataTypes = null;

        if (collect.reportRowViewbyValues.get(0) != null && collect.reportRowViewbyValues.get(0).equalsIgnoreCase("TIME")) {
        } else {
            sortCols = container.getSortColumns();
            String sort = "";
            if (sortCols != null && !sortCols.isEmpty()) {
                sortCols = container.getSortColumns();
                if (!sortCols.isEmpty()) {
                    sortTypes = container.getSortTypes();
                    sortDataTypes = container.getSortDataTypes();
                    ProgenDataSet retObj = container.getRetObj();
                    ArrayList rowSequence = new ArrayList();
                    if (container.isTopBottomSet()) {
                        int topbottomCount = container.getTopBottomCount();
                        if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS)) {
                            sort = "1";
//                         container.setSortColumn(sortCols, sort);
                            if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                            } else {
                                rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                            }
//                              retObj.setViewSequence(rowSequence);
                        } else if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS)) {
                            sort = "0";
//                            container.setSortColumn(sortColumn, sort);
                            if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                            } else {
                                rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                            }

                        }
                    } else {
                        rowSequence = container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes);
                    }
                    retObj.setViewSequence(rowSequence);




//                container.getRetObj().setViewSequence(container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes));
//                rowCount = rowSequence.size();
                } else {
                    ArrayList tableMeasure = container.getTableMeasure();
                    if (tableMeasure != null) {
                        ArrayList sortColumn = new ArrayList();
                        sortColumn.add("A_" + tableMeasure.get(0).toString());
                        char[] sortType = new char[1];//new String[1];
                        sortType[0] = ' ';
                        char[] sortdataType = new char[1];
                        sortdataType[0] = 'N';
                        container.getRetObj().setViewSequence(container.getRetObj().sortDataSet(sortColumn, sortType, sortdataType));
                    }
                }
            }
        }

//              if(container.getOneviewGraphTimedetails()!=null && !container.getOneviewGraphTimedetails().isEmpty()){
//                    container.getOneviewGraphTimedetails().clear();
//                }
        HashMap GraphTypesHashMap = null;
        HashMap GraphSizesDtlsHashMap = null;
//                             String ProGenImgPath = getServletContext().getRealPath("/") + "tempFolder/";
        String[] ViewBys = null;

        GraphSizesDtlsHashMap = schedule.getGraphSizesDtlsHashMap();
//        GraphSizesDtlsHashMap = (HashMap) session.getAttribute("GraphSizesDtlsHashMap");
        PbGraphDisplay GraphDisplay = new PbGraphDisplay();
        GraphDisplay.olapFun = olapRef;
        GraphDisplay.setGraphSizesDtlsHashMap(GraphSizesDtlsHashMap);
//                                GraphDisplay.setProGenImgPath(ProGenImgPath);
        ProgenDataSet recordsRetObj = container.getRetObj();
        GraphDisplay.setCurrentDispRetObjRecords(recordsRetObj);//works 4 fx
        // GraphDisplay.setCurrentDispRetObjRecords(container.getDisplayedSetRetObj());//works 4 jfree
        //   GraphDisplay.setCurrentDispRecordsRetObjWithGT(container.getDisplayedSetRetObjWithGT());
        GraphDisplay.setAllDispRecordsRetObj(recordsRetObj);
        GraphDisplay.setNoOfDays(container.getNoOfDays());
        //added by santhosh.k on 01-03-2010 for reading info of entire dataset
        GraphDisplay.setColumnAverages(recordsRetObj.getColumnAverages());
        GraphDisplay.setColumnGrandTotals(recordsRetObj.getColumnGrandTotals());
        GraphDisplay.setColumnOverAllMinimums(recordsRetObj.getColumnOverAllMinimums());
        GraphDisplay.setColumnOverAllMaximums(recordsRetObj.getColumnOverAllMaximums());
        ArrayList viewByColNames = container.getViewByColNames();
        ArrayList viewByElementId = container.getViewByElementIds();
        ViewBys = (String[]) viewByElementId.toArray(new String[0]);
        for (int viewIndex = 0; viewIndex < ViewBys.length; viewIndex++) {
            if (ViewBys[viewIndex].equalsIgnoreCase("Time")) {
                ViewBys[viewIndex] = ViewBys[viewIndex];
            } else {
                if (ViewBys[viewIndex].contains("A_")) {
                    ViewBys[viewIndex] = ViewBys[viewIndex];
                } else {
                    ViewBys[viewIndex] = "A_" + ViewBys[viewIndex];
                }
            }
        }
        GraphDisplay.setViewByColNames((String[]) viewByColNames.toArray(new String[0]));
        GraphDisplay.setViewByElementIds(ViewBys);
        GraphDisplay.setCtxPath(onecontainer.getContextPath());
        GraphDisplay.setTimelevel(container.getTimeLevel());

//                                ArrayList links = container.getLinks();
//                                if (links != null && links.size() != 0) {
//                                    GraphDisplay.setJscal(String.valueOf(links.get(0)));
//                                } else {
//                                    GraphDisplay.setJscal("reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&");
//                                }
//        GraphDisplay.setSession("false");
//        GraphDisplay.setResponse(response);
//        GraphDisplay.setOut(response.getWriter());
//        GraphDisplay.setReportId(reportId);

        HashMap[] graphMapDetails = container.getGraphMapDetails();

        GraphDisplay.setGraphHashMap(container.getGraphHashMap());
        GraphDisplay.setGraphMapDetails(graphMapDetails);
//                                GraphDisplay.setIsCrosstab(container.isReportCrosstab());
        GraphDisplay.setSortColumns(container.getSortColumns());
        GraphDisplay.fromOneview = true;
        if (!viewBy) {
            GraphDisplay.graphWidth = Integer.toString(oneviewlet.getWidth());
            GraphDisplay.graphHeight = Integer.toString(oneviewlet.getHeight());
        } else {
            GraphDisplay.graphWidth = Integer.toString(oneviewlet.getWidth());
            GraphDisplay.graphHeight = Integer.toString(oneviewlet.getHeight());
        }
        if (Integer.parseInt(container.getColumnViewByCount()) != 0) {
            grpDetails = GraphDisplay.get2dGraphHeaders(container);
        } else {
            grpDetails = GraphDisplay.getGraphHeaders(container.getNoOfDays(), container);
        }

        GraphDisplay.fromOneview = false;
        GraphDisplay.graphWidth = null;
        GraphDisplay.graphHeight = null;

        container.setGraphHashMap(GraphDisplay.getGraphHashMap());


        String graphTypeName = null;
        String jqgraphId = "";
//                                  if (grpDetails != null && !grpDetails.isEmpty()) {
//                                        pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
//                                        if (pcharts != null && pcharts.length != 0) {
//                                            if(grpNo==0)
//                                            graphTypeName=pcharts[0].getChartType();
//                                            else
//                                            graphTypeName=pcharts[1].getChartType();
//                                    }
//                                }
        if (oneviewlet.getGraphType().equalsIgnoreCase("JQPlot")) {
            JqplotGraphProperty graphproperty = new JqplotGraphProperty();
            PbReportViewerDAO reportViewerdao = new PbReportViewerDAO();
            graphproperty = reportViewerdao.getJqGraphDetails(graphId);
//            HashMap jqpToJfNameMap = (HashMap) session.getAttribute("JqpToJfNameMap");
//            HashMap jqpMap = (HashMap) session.getAttribute("JqpMap");
            graphTypeName = graphproperty.getGraphTypename();
            jqgraphId = graphproperty.getGraphTypeId();
//                                      if(jqpToJfNameMap != null && jqpMap!= null ){
//                                      graphTypeName=(String) jqpToJfNameMap.get(graphTypeName);
//                                      jqgraphId=(String)jqpMap.get(graphTypeName);

            if (graphTypeName == null || graphTypeName.equalsIgnoreCase("") || graphTypeName.equalsIgnoreCase("null")) {
                graphTypeName = "Bar-Vertical";
                jqgraphId = "5502";
            }

            ProGenJqPlotChartTypes jqplotcontainer = new ProGenJqPlotChartTypes();
            ProgenJqplotGraphBD jqplotgraphbd = new ProgenJqplotGraphBD();
            String selectgraph = graphproperty.getSlectedGraphType(graphId);
            jqplotgraphbd.JqplotGraphProperty = graphproperty;
            if (container.getRetObj() != null && container.getRetObj().rowCount > 0) {
                if (graphproperty.getGraphTypename().equalsIgnoreCase("ColumnPie")) {
//                    request.setAttribute("height", height);
                    ReportTemplateBD reporttemp = new ReportTemplateBD();
                    String viewletNum = oneviewlet.getNoOfViewLets();
//                    session.setAttribute("isineviewdivcolpie", viewletNum);
                    imageBuffer.append(reporttemp.builcolumnpie(graphproperty, jqplotgraphbd, null, null, null, container, jqplotcontainer));
                } else if (graphproperty.getGraphTypename().equalsIgnoreCase("Pie") && container.isReportCrosstab()) {
//                    request.setAttribute("height", height);
                    ReportTemplateBD reporttemp = new ReportTemplateBD();
                    imageBuffer.append(reporttemp.builcrosstabpie(graphproperty, jqplotgraphbd, null, null, null, container, jqplotcontainer, oneviewlet));
                } else {
                    String isineviewdiv = null;
                    String viewletNum = oneviewlet.getNoOfViewLets();
                    String chartId = "chart-" + viewletNum; //+grpNo+"_"+reportId+"_"
                    jqplotgraphbd.chartId = chartId;
                    jqplotgraphbd.tickId = viewletNum;
                    //
                    if (graphproperty.getGraphTypename().equalsIgnoreCase("Pie") || graphproperty.getGraphTypename().equalsIgnoreCase("Pie-Empty") || graphproperty.getGraphTypename().equalsIgnoreCase("Donut-Single") || graphproperty.getGraphTypename().equalsIgnoreCase("Donut-Double")) {

                        width = (int) (0.70 * width);
                        imageBuffer.append("<div id='" + chartId + "' style=\"width:" + width + "px;height:" + (height + (height / 7)) + "px; margin: auto\" align=\"center\" onclick=\"" + olapFunc + "\"></div><br>"); //olapGraph('"+name+"','"+reportId+"','"+graphId+"','"+grpNo+"')
//                            imageBuffer.append("<script>");

                    } else {
                        imageBuffer.append("<div id='" + chartId + "' style=\"width:" + width + "px;height:" + height + "px;\" align=\"center\" onmouseover=\"getdetailsfordrill('" + viewletNum + "','" + name + "','" + reportId + "','" + graphId + "','" + grpNo + "','" + timeDetails + "','" + chartId + "','" + width + "','" + height + "','false')\"  onclick=\"" + olapFunc + "\"></div><br>"); //olapGraph('"+name+"','"+reportId+"','"+graphId+"','"+grpNo+"')
                    }
                    imageBuffer.append("<script>");

                    if (graphproperty.getTableColumns() != null && graphproperty.getTableColumns().length > 0) {
                        jqplotgraphbd.tablecols = graphproperty.getTableColumns();
                    }
                    jqplotgraphbd.setContainer(container);
                    jqplotgraphbd.setisFromOneViewschedule(true);

                    imageBuffer.append(jqplotgraphbd.prepareJqplotGraph(reportId, jqgraphId, graphTypeName, jqplotcontainer, null, null, selectgraph, graphId));

                    imageBuffer.append("</script>");
                    jqplotgraphbd.setisFromOneViewschedule(true);

                }

            } else {
                imageBuffer.append("<div id='nodata-" + oneviewlet.getNoOfViewLets() + "' style=\"width:" + width + "px;height:" + height + "px;\" align=\"center\" >");
                imageBuffer.append("<font style='font-family:verdana;font-size:14pt'>No Data Found</font>");
                imageBuffer.append("</div><br>"); //olapGraph('"+name+"','"+reportId+"','"+graphId+"','"+grpNo+"')

            }

//                                      

        } else {
            if (grpDetails != null && !grpDetails.isEmpty()) {
                pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
                if (pcharts != null && pcharts.length != 0) {
                    if (grpNo == 0) {
                        imageBuffer.append(pcharts[0].chartDisplay);
                    } else {
                        imageBuffer.append(pcharts[1].chartDisplay);
                    }
                }
            }
        }

        return imageBuffer.toString();

//     return null;
    }

//Surender
//    public String getGraphById(String divId, String reportId, String graphId, HttpServletRequest request, HttpServletResponse response,
//            HttpSession session, String dbrdId, boolean isFxCharts, boolean addedToDashboard, boolean viewBy) throws Exception {
    public String getGraphById(String reportId, HttpServletRequest request, HttpServletResponse response, HttpSession session, int grpNo, boolean viewBy, OneViewLetDetails oneviewlet, OnceViewContainer onecontainer) throws Exception {

        StringBuilder imageBuffer = new StringBuilder("");
        PbGraphDisplay pbDisplay = new PbGraphDisplay();
        HashMap ParametersMap = null;
        HashMap map = null;
        Container container = null;
        ArrayList Parameters = new ArrayList();
        ArrayList REP = new ArrayList();
        ArrayList ParameterNames = new ArrayList();
        String[] viewByElementIds = new String[0];
        String[] viewByDispNames = new String[0];
        PbReportViewerBD repViewBD = new PbReportViewerBD();
        ArrayList grpDetails = new ArrayList();
        ProgenChartDisplay[] pcharts = null;
        String userId = String.valueOf(session.getAttribute("USERID"));
        PbReportViewerBD reportViewerBD = new PbReportViewerBD();
        String strURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        strURL = strURL + "reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&action=open";
        request.setAttribute("url", strURL);
        request.setAttribute("REPORTID", reportId);
        if (onecontainer.getFilterBusinessRole() != null && !onecontainer.getFilterBusinessRole().equalsIgnoreCase("") && oneviewlet != null && oneviewlet.getRoleId() != null && !oneviewlet.getRoleId().equalsIgnoreCase("") && onecontainer.getFilterBusinessRole().equalsIgnoreCase(oneviewlet.getRoleId()) && !oneviewlet.isOneviewReportTimeDetails()) {
            if (onecontainer.getReportParameterValues() != null && !onecontainer.getReportParameterValues().isEmpty()) {
                request.setAttribute("reportParameterVals", onecontainer.getReportParameterValues());
            }
        }
        reportViewerBD.prepareReport(reportId, userId, request, response,false);
        map = (HashMap) session.getAttribute("PROGENTABLES");

        container = (Container) map.get(reportId);
        oneviewlet.setContainer(container);
        PbReportCollection collect = container.getReportCollect();
        String name = request.getParameter("name");
        if (name == null || name.equalsIgnoreCase("null")) {
            if (request.getAttribute("graphName") != null) {
                name = request.getAttribute("graphName").toString();
            }
        }

        String graphId = request.getParameter("graphId");
        if (graphId == null || graphId.equalsIgnoreCase("null")) {
            if (request.getAttribute("graphId") != null) {
                graphId = request.getAttribute("graphId").toString();
            }
        }
        int height = oneviewlet.getHeight();
        int width = oneviewlet.getWidth();
        if (height == 0 || width == 0) {
            if (request.getAttribute("height") != null) {
                height = Integer.parseInt(request.getAttribute("height").toString());
            }
            if (request.getAttribute("width") != null) {
                width = Integer.parseInt(request.getAttribute("width").toString());
            }
        }
        boolean oneviewReportTimeDetails = false;
        List<String> timeDetails = null;
        String repDate = null;
        boolean isOneViewTime = false;
        boolean isReportTime = false;
        if (request.getAttribute("OneviewGraphTimeDetails") != null && !oneviewlet.isOneviewReportTimeDetails()) {
            timeDetails = (List) request.getAttribute("OneviewGraphTimeDetails");
            isOneViewTime = true;

        }
        if (request.getAttribute("OneviewgraphDate") != null && oneviewlet.isOneviewReportTimeDetails()) {
            repDate = request.getAttribute("OneviewgraphDate").toString();
            oneviewReportTimeDetails = oneviewlet.isOneviewReportTimeDetails();
            isReportTime = true;
        }
        String olapFunc = "";
        timeDetails = collect.timeDetailsArray;
        session.setAttribute("timeDetails", timeDetails);
        if (isOneViewTime) {
            olapFunc = "olapGraph('" + name + "','" + reportId + "','" + graphId + "','" + grpNo + "','" + timeDetails + "','false')";
        } else {
            olapFunc = "olapGraph('" + name + "','" + reportId + "','" + graphId + "','" + grpNo + "','" + repDate + "','true')";
        }


        String olapRef = "javascript:olapGraph(\"" + name + "\",\"" + reportId + "\",\"" + graphId + "\",\"" + grpNo + "\")";
        ArrayList<String> sortCols = null;
        char[] sortTypes = null;//ArrayList sortTypes = null;
        char[] sortDataTypes = null;

        if (collect.reportRowViewbyValues.get(0) != null && collect.reportRowViewbyValues.get(0).equalsIgnoreCase("TIME")) {
        } else {
            sortCols = container.getSortColumns();
            String sort = "";
            if (sortCols != null && !sortCols.isEmpty()) {
                sortCols = container.getSortColumns();
                if (!sortCols.isEmpty()) {
                    sortTypes = container.getSortTypes();
                    sortDataTypes = container.getSortDataTypes();
                    ProgenDataSet retObj = container.getRetObj();
                    ArrayList rowSequence = new ArrayList();
                    if (container.isTopBottomSet()) {
                        int topbottomCount = container.getTopBottomCount();
                        if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS)) {
                            sort = "1";
//                         container.setSortColumn(sortCols, sort);
                            if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                            } else {
                                rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                            }
//                              retObj.setViewSequence(rowSequence);
                        } else if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS)) {
                            sort = "0";
//                            container.setSortColumn(sortColumn, sort);
                            if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                            } else {
                                rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                            }

                        }
                    } else {
                        rowSequence = container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes);
                    }
                    retObj.setViewSequence(rowSequence);




//                container.getRetObj().setViewSequence(container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes));
//                rowCount = rowSequence.size();
                } else {
                    ArrayList tableMeasure = container.getTableMeasure();
                    if (tableMeasure != null) {
                        ArrayList sortColumn = new ArrayList();
                        sortColumn.add("A_" + tableMeasure.get(0).toString());
                        char[] sortType = new char[1];//new String[1];
                        sortType[0] = ' ';
                        char[] sortdataType = new char[1];
                        sortdataType[0] = 'N';
                        container.getRetObj().setViewSequence(container.getRetObj().sortDataSet(sortColumn, sortType, sortdataType));
                    }
                }
            }
        }

//              if(container.getOneviewGraphTimedetails()!=null && !container.getOneviewGraphTimedetails().isEmpty()){
//                    container.getOneviewGraphTimedetails().clear();
//                }
        HashMap GraphTypesHashMap = null;
        HashMap GraphSizesDtlsHashMap = null;
//                             String ProGenImgPath = getServletContext().getRealPath("/") + "tempFolder/";
        String[] ViewBys = null;

        GraphSizesDtlsHashMap = (HashMap) session.getAttribute("GraphSizesDtlsHashMap");
        PbGraphDisplay GraphDisplay = new PbGraphDisplay();
        GraphDisplay.olapFun = olapRef;
        GraphDisplay.setGraphSizesDtlsHashMap(GraphSizesDtlsHashMap);
//                                GraphDisplay.setProGenImgPath(ProGenImgPath);
        ProgenDataSet recordsRetObj = container.getRetObj();
        GraphDisplay.setCurrentDispRetObjRecords(recordsRetObj);//works 4 fx
        // GraphDisplay.setCurrentDispRetObjRecords(container.getDisplayedSetRetObj());//works 4 jfree
        //   GraphDisplay.setCurrentDispRecordsRetObjWithGT(container.getDisplayedSetRetObjWithGT());
        GraphDisplay.setAllDispRecordsRetObj(recordsRetObj);
        GraphDisplay.setNoOfDays(container.getNoOfDays());
        //added by santhosh.k on 01-03-2010 for reading info of entire dataset
        GraphDisplay.setColumnAverages(recordsRetObj.getColumnAverages());
        GraphDisplay.setColumnGrandTotals(recordsRetObj.getColumnGrandTotals());
        GraphDisplay.setColumnOverAllMinimums(recordsRetObj.getColumnOverAllMinimums());
        GraphDisplay.setColumnOverAllMaximums(recordsRetObj.getColumnOverAllMaximums());
        ArrayList viewByColNames = container.getViewByColNames();
        ArrayList viewByElementId = container.getViewByElementIds();
        ViewBys = (String[]) viewByElementId.toArray(new String[0]);
        for (int viewIndex = 0; viewIndex < ViewBys.length; viewIndex++) {
            if (ViewBys[viewIndex].equalsIgnoreCase("Time")) {
                ViewBys[viewIndex] = ViewBys[viewIndex];
            } else {
                if (ViewBys[viewIndex].contains("A_")) {
                    ViewBys[viewIndex] = ViewBys[viewIndex];
                } else {
                    ViewBys[viewIndex] = "A_" + ViewBys[viewIndex];
                }
            }
        }
        GraphDisplay.setViewByColNames((String[]) viewByColNames.toArray(new String[0]));
        GraphDisplay.setViewByElementIds(ViewBys);
        GraphDisplay.setCtxPath(request.getContextPath());
        GraphDisplay.setTimelevel(container.getTimeLevel());

//                                ArrayList links = container.getLinks();
//                                if (links != null && links.size() != 0) {
//                                    GraphDisplay.setJscal(String.valueOf(links.get(0)));
//                                } else {
//                                    GraphDisplay.setJscal("reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&");
//                                }
        GraphDisplay.setSession(request.getSession(false));
        GraphDisplay.setResponse(response);
        GraphDisplay.setOut(response.getWriter());
        GraphDisplay.setReportId(reportId);

        HashMap[] graphMapDetails = container.getGraphMapDetails();

        GraphDisplay.setGraphHashMap(container.getGraphHashMap());
        GraphDisplay.setGraphMapDetails(graphMapDetails);
//                                GraphDisplay.setIsCrosstab(container.isReportCrosstab());
        GraphDisplay.setSortColumns(container.getSortColumns());
        GraphDisplay.fromOneview = true;
        if (!viewBy) {
            GraphDisplay.graphWidth = request.getParameter("width").toString();
            GraphDisplay.graphHeight = request.getParameter("height").toString();
        } else {
            GraphDisplay.graphWidth = request.getAttribute("width").toString();
            GraphDisplay.graphHeight = request.getAttribute("height").toString();
        }
        if (Integer.parseInt(container.getColumnViewByCount()) != 0) {
            grpDetails = GraphDisplay.get2dGraphHeaders(container);
        } else {
            grpDetails = GraphDisplay.getGraphHeaders(container.getNoOfDays(), container);
        }

        GraphDisplay.fromOneview = false;
        GraphDisplay.graphWidth = null;
        GraphDisplay.graphHeight = null;

        container.setGraphHashMap(GraphDisplay.getGraphHashMap());


        String graphTypeName = null;
        String jqgraphId = "";
//                                  if (grpDetails != null && !grpDetails.isEmpty()) {
//                                        pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
//                                        if (pcharts != null && pcharts.length != 0) {
//                                            if(grpNo==0)
//                                            graphTypeName=pcharts[0].getChartType();
//                                            else
//                                            graphTypeName=pcharts[1].getChartType();
//                                    }
//                                }
        if (oneviewlet.getGraphType().equalsIgnoreCase("JQPlot")) {
            JqplotGraphProperty graphproperty = new JqplotGraphProperty();
            PbReportViewerDAO reportViewerdao = new PbReportViewerDAO();
            graphproperty = reportViewerdao.getJqGraphDetails(graphId);
            HashMap jqpToJfNameMap = (HashMap) session.getAttribute("JqpToJfNameMap");
            HashMap jqpMap = (HashMap) session.getAttribute("JqpMap");
            graphTypeName = graphproperty.getGraphTypename();
            jqgraphId = graphproperty.getGraphTypeId();
//                                      if(jqpToJfNameMap != null && jqpMap!= null ){
//                                      graphTypeName=(String) jqpToJfNameMap.get(graphTypeName);
//                                      jqgraphId=(String)jqpMap.get(graphTypeName);

            if (graphTypeName == null || graphTypeName.equalsIgnoreCase("") || graphTypeName.equalsIgnoreCase("null")) {
                graphTypeName = "Bar-Vertical";
                jqgraphId = "5502";
            }

            ProGenJqPlotChartTypes jqplotcontainer = new ProGenJqPlotChartTypes();
            ProgenJqplotGraphBD jqplotgraphbd = new ProgenJqplotGraphBD();
            String selectgraph = graphproperty.getSlectedGraphType(graphId);
            jqplotgraphbd.JqplotGraphProperty = graphproperty;
            if (container.getRetObj() != null && container.getRetObj().rowCount > 0) {
                if (graphproperty.getGraphTypename().equalsIgnoreCase("ColumnPie")) {
                    request.setAttribute("height", height);
                    ReportTemplateBD reporttemp = new ReportTemplateBD();
                    String viewletNum = oneviewlet.getNoOfViewLets();
                    session.setAttribute("isineviewdivcolpie", viewletNum);
                    imageBuffer.append(reporttemp.builcolumnpie(graphproperty, jqplotgraphbd, null, null, request, container, jqplotcontainer));
                } else if (graphproperty.getGraphTypename().equalsIgnoreCase("Pie") && container.isReportCrosstab()) {
                    request.setAttribute("height", height);
                    ReportTemplateBD reporttemp = new ReportTemplateBD();
                    imageBuffer.append(reporttemp.builcrosstabpie(graphproperty, jqplotgraphbd, null, null, request, container, jqplotcontainer, oneviewlet));
                } else {
                    String isineviewdiv = null;
                    String viewletNum = oneviewlet.getNoOfViewLets();
                    String chartId = "chart-" + viewletNum; //+grpNo+"_"+reportId+"_"
                    jqplotgraphbd.chartId = chartId;
                    jqplotgraphbd.tickId = viewletNum;
                    //
                    if (graphproperty.getGraphTypename().equalsIgnoreCase("Pie") || graphproperty.getGraphTypename().equalsIgnoreCase("Pie-Empty") || graphproperty.getGraphTypename().equalsIgnoreCase("Donut-Single") || graphproperty.getGraphTypename().equalsIgnoreCase("Donut-Double")) {

                        width = (int) (0.70 * width);
                        imageBuffer.append("<div id='" + chartId + "' style=\"width:" + width + "px;height:" + (height + (height / 7)) + "px; margin: auto\" align=\"center\" onclick=\"" + olapFunc + "\"></div><br>"); //olapGraph('"+name+"','"+reportId+"','"+graphId+"','"+grpNo+"')
//                            imageBuffer.append("<script>");

                    } else {
                        imageBuffer.append("<div id='" + chartId + "' style=\"width:" + width + "px;height:" + height + "px;\" align=\"center\" onmouseover=\"getdetailsfordrill('" + viewletNum + "','" + name + "','" + reportId + "','" + graphId + "','" + grpNo + "','" + timeDetails + "','" + chartId + "','" + width + "','" + height + "','false')\"  onclick=\"" + olapFunc + "\"></div><br>"); //olapGraph('"+name+"','"+reportId+"','"+graphId+"','"+grpNo+"')
                    }
                    imageBuffer.append("<script>");

                    if (graphproperty.getTableColumns() != null && graphproperty.getTableColumns().length > 0) {
                        jqplotgraphbd.tablecols = graphproperty.getTableColumns();
                    }

                    jqplotgraphbd.setIsFromOneView(true);

                    imageBuffer.append(jqplotgraphbd.prepareJqplotGraph(reportId, jqgraphId, graphTypeName, jqplotcontainer, request, null, selectgraph, graphId));

                    imageBuffer.append("</script>");
                    jqplotgraphbd.setIsFromOneView(false);

                }

            } else {
                imageBuffer.append("<div id='nodata-" + oneviewlet.getNoOfViewLets() + "' style=\"width:" + width + "px;height:" + height + "px;\" align=\"center\" >");
                imageBuffer.append("<font style='font-family:verdana;font-size:14pt'>No Data Found</font>");
                imageBuffer.append("</div><br>"); //olapGraph('"+name+"','"+reportId+"','"+graphId+"','"+grpNo+"')

            }

//                                      

        } else {
            if (grpDetails != null && !grpDetails.isEmpty()) {
                pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
                if (pcharts != null && pcharts.length != 0) {
                    if (grpNo == 0) {
                        imageBuffer.append(pcharts[0].chartDisplay);
                    } else {
                        imageBuffer.append(pcharts[1].chartDisplay);
                    }
                }
            }
        }

        return imageBuffer.toString();
    }
    //Surender

    public String getGraphDetailsData(HttpServletRequest request, HttpServletResponse response, HttpSession session, OneViewLetDetails oneviewlet, List<String> timedetails, OnceViewContainer onecontainer) throws Exception {
        StringBuilder finalStringVal = new StringBuilder();
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        String value = null;
        if (request.getAttribute("graphTest") == null) {
            if (session.getAttribute("MeasureDrillTest") != null) {
                HashMap<String, ArrayList<String>> drillMap = null;
                drillMap = (HashMap<String, ArrayList<String>>) session.getAttribute("MeasureDrillTest");
                Set<String> keys = drillMap.keySet();
                ArrayList<String> values = new ArrayList<String>();
                values.addAll(keys);
                if (values.contains(oneviewlet.getNoOfViewLets())) {
                    if (drillMap.get(oneviewlet.getNoOfViewLets()).get(0).equalsIgnoreCase(oneviewlet.getOneviewId())) {
                        value = drillMap.get(oneviewlet.getNoOfViewLets()).get(1);
                    }
                }
            }
            String ifpoweranalyer = session.getAttribute("isPowerAnalyserEnableforUser").toString();
            if (ifpoweranalyer != null && ifpoweranalyer.equalsIgnoreCase("true")) {
                oneviewlet.setUserStatus(true);
            } else {
                oneviewlet.setUserStatus(false);
            }
            if (oneviewlet.getMeasurDrill() != null) {
                value = oneviewlet.getMeasurDrill();
            }

//                     "<td id=\"Dashlets"+tdId+"\" style='font-size:12pt;color:#000000; '></td><td id='renameId"+tdId+"' style='display:none;width:3%;'><a class='ui-icon ui-icon-pencil' title='Rename' style='text-decoration: none;' onclick='' valign='top' href='javascript:void(0)'></a></td><td id='drillId"+tdId+"' style='display:none;width:3%;'><a  class='ui-icon ui-icon-triangle-2-n-s' title='Drill' style='text-decoration: none;' onclick='' valign='top' href='javascript:void(0)'></a></td><td id='readdId"+tdId+"' style='display:none;width:3%;'><a href='javascript:void(0)' class=\"ui-icon ui-icon-plusthick\" onclick=\"selectTypeDiv('Dashlets-"+tdId+"',"+tdId+","+width+","+height+",'Dashlets"+tdId+"')\"  style='text-decoration:none'  title=\"Re Add Onevielet\"></a></td></tr></table>";
            //finalStringVal.append("<td id='" + oneviewlet.getNoOfViewLets() + "' width='" + oneviewlet.getWidth() + "px'  style='height:" + oneviewlet.getHeight() + "px;' rowspan='" + oneviewlet.getRowSpan() + "' colspan='" + oneviewlet.getColSpan() + "'>");
            if (request.getParameter("downloaingfdf") == null) {
//                finalStringVal.append(new DashboardViewerDAO().getOneviewTableAndGraphHeader(oneviewlet, value,timedetails));
            }

            int height = oneviewlet.getHeight();
            int width = oneviewlet.getWidth();

            // finalStringVal.append("<div id='Dashlets-" + oneviewlet.getNoOfViewLets() + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 100%; margin-left: 10x; margin-right: 10px;'>");
            //Added By Ram
            finalStringVal.append("<div id='Dashlets-" + oneviewlet.getNoOfViewLets() + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: 1px solid ; border-left: 1px solid ; border-right: 1px solid ; border-color: LightGrey ; width: 100%; height: 100%; margin-left: 10x; margin-right: 10px;'>");
        }

        String result = null;

        request.setAttribute("width", Integer.toString(oneviewlet.getWidth()));
        request.setAttribute("height", Integer.toString(oneviewlet.getHeight()));
        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
//
        if (request.getAttribute("OneviewGraphTimeDetails") != null) {
            request.removeAttribute("OneviewGraphTimeDetails");
        }
        if (timedetails != null && !oneviewlet.isOneviewReportTimeDetails() && !timedetails.isEmpty()) {
            request.setAttribute("OneviewGraphTimeDetails", timedetails);

        }
        if (timedetails != null && oneviewlet.isoneviewcustomtimedetails() && !timedetails.isEmpty()) {
            request.setAttribute("OneviewGraphTimeDetails", timedetails);
        }

        if (request.getAttribute("OneviewTableTimeDetails") != null) {
            request.removeAttribute("OneviewTableTimeDetails");
        }

        if (request.getAttribute("OneviewgraphDate") != null) {
            request.removeAttribute("OneviewgraphDate");
        }
        if (timedetails != null && oneviewlet.isOneviewReportTimeDetails() && !timedetails.isEmpty()) {
            request.setAttribute("OneviewgraphDate", timedetails.get(2));

        }
        if (timedetails != null && !oneviewlet.isoneviewcustomtimedetails() && !timedetails.isEmpty()) {
            request.setAttribute("OneviewgraphDate", timedetails.get(2));
        }


        if (request.getAttribute("OneviewtableDate") != null) {
            request.removeAttribute("OneviewtableDate");
        }

        dashboardTemplateBD.setServletRequest(request);
        boolean viewBy = true;
        HashMap map = null;
        Container container = null;
        String customDbrdId = "";
        HashMap divGraphs = null;
        String reportId = oneviewlet.getRepId();
        String graphId = oneviewlet.getGrapId();
        String gaphName = oneviewlet.getRepName();
        String divId = oneviewlet.getNoOfViewLets();
        request.setAttribute("graphName", gaphName);
        request.setAttribute("graphId", graphId);
        String strURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        strURL = strURL + "reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&action=open";
        request.setAttribute("url", strURL);
        request.setAttribute("REPORTID", reportId);
        String userId = String.valueOf(session.getAttribute("USERID"));
        if (onecontainer.getFilterBusinessRole() != null && !onecontainer.getFilterBusinessRole().equalsIgnoreCase("") && oneviewlet != null && oneviewlet.getRoleId() != null && !oneviewlet.getRoleId().equalsIgnoreCase("") && onecontainer.getFilterBusinessRole().equalsIgnoreCase(oneviewlet.getRoleId()) && !oneviewlet.isOneviewReportTimeDetails()) {
            if (onecontainer.getReportParameterValues() != null && !onecontainer.getReportParameterValues().isEmpty()) {
                request.setAttribute("reportParameterVals", onecontainer.getReportParameterValues());
            }
        }
        if (oneviewlet.isoneviewcustomtimedetails() || !oneviewlet.isOneviewReportTimeDetails()) {
            request.setAttribute("isoneview", "true");
            PbReportViewerBD reportViewerBD = new PbReportViewerBD();
            reportViewerBD.prepareReport(reportId, userId, request, response,false);
            map = (HashMap) session.getAttribute("PROGENTABLES");

            container = (Container) map.get(reportId);
//        oneviewlet.setContainer(container);
        }
//            if(oneviewlet.isOneviewReportTimeDetails()){
//        result = dashboardTemplateBD.getGraphById(reportId, request, response, session, oneviewlet.getGrapNo(), viewBy, oneviewlet, onecontainer);
        String image = null;
//        if(result != null){
//            int va = result.indexOf("#");
//            int v = result.indexOf("png");
//            image= result.substring(va+1,v+3);
//        }
//
//        if(request.getAttribute("graphTest")!=null){
//         return image;
//        }
        if (result != null) {
            finalStringVal.append(result);
            finalStringVal.append("</div>");
            finalStringVal.append("</td>");
            return finalStringVal.toString();
        } else {
            if (result != null) {
                if (request.getParameter("downloaingfdf") != null) {
                    finalStringVal.append(result.replace("" + request.getContextPath() + "/servlet/DisplayChart?filename=", System.getProperty("java.io.tmpdir") + "/".toString()));
                } else {
                    finalStringVal.append(result);

                    onecontainer = new OnceViewContainer();
                    ReportTemplateDAO reportTemplateDA = new ReportTemplateDAO();
                    OneViewLetDetails detail = null;
                    String fileName = reportTemplateDA.getOneviewFileName(oneviewlet.getOneviewId());
                    FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                    ObjectInputStream ois2 = new ObjectInputStream(fis2);
                    onecontainer = (OnceViewContainer) ois2.readObject();
                    ois2.close();
                    detail = onecontainer.onviewLetdetails.get(Integer.parseInt(oneviewlet.getNoOfViewLets()));

                    detail.graphDetails.put(oneviewlet.getNoOfViewLets(), image);
//             reportTemplateDAO.updateOneviewData(onecontainer,oneViewIdValue);
                    FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(onecontainer);
                    oos.flush();
                    oos.close();
                }
            } else {
//                finalStringVal.append("No Data");
            }
            finalStringVal.append("</div>");
            //finalStringVal.append("</td>");
            return finalStringVal.toString();
        }

    }
//Start of code by sandeep on 17/10/14 for schedule// update local files in oneview

    public String getGraphDetailsData1(Container container, OneViewLetDetails oneviewlet, List<String> timedetails, OnceViewContainer onecontainer, ReportSchedule schedule) throws Exception {
        StringBuilder finalStringVal = new StringBuilder();
        String value = null;
        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
        boolean viewBy = true;
        String reprtId = oneviewlet.getRepId();
//    String repDate = request.getAttribute("OneviewgraphDate").toString();
        finalStringVal.append(new DashboardViewerDAO().getOneviewTableAndGraphHeader(oneviewlet, value, timedetails));
        finalStringVal.append("<div id='Dashlets-" + oneviewlet.getNoOfViewLets() + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 100%; margin-left: 10x; margin-right: 10px;'>");
        oneviewlet.setContainer(container);
        String result = dashboardTemplateBD.getGraphById1(container, oneviewlet.getGrapNo(), viewBy, oneviewlet, onecontainer, schedule);
        if (result != null) {
            finalStringVal.append(result);
            finalStringVal.append("</div>");
            finalStringVal.append("</td>");
            return finalStringVal.toString();
        }
        return null;
    }
    ////Start of code by sandeep on 17/10/14 for schedule// update local files in oneview

    public String getTableDetailsData1(Container container, OneViewLetDetails oneviewlet, List<String> timedetails, OnceViewContainer onecontainer, ReportSchedule schedule) throws Exception {
        StringBuilder finalStringVal = new StringBuilder();
        String value = null;
        StringBuilder result = new StringBuilder();
        DataSnapshotGenerator generateRepTable = new DataSnapshotGenerator();
        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
        boolean viewBy = true;
        String height = Integer.toString(oneviewlet.getHeight());
        String width = Integer.toString(oneviewlet.getWidth());
        String userId = onecontainer.getUserId();
        String reprtId = oneviewlet.getRepId();
        finalStringVal.append(new DashboardViewerDAO().getOneviewTableAndGraphHeader(oneviewlet, value, timedetails));
        finalStringVal.append("<div id='Dashlets-" + oneviewlet.getNoOfViewLets() + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 100%; margin-left: 10x; margin-right: 10px;overflow:auto;'>");

        oneviewlet.setContainer(container);
        if (container.isTopBottomTableEnable()) {
            result = generateRepTable.generateTopBottomTable(container, userId, width, width);
        } else {

            result = generateRepTable.generateReportTable(container, userId, height, width);
        }
        if (result != null) {
            finalStringVal.append(result.toString());
        } else {
            finalStringVal.append("No Data");
        }
        finalStringVal.append("</div>");
        //finalStringVal.append("</td>");

        return finalStringVal.toString();

//         return null;
    }
//Surender

    public String getTableDetailsData(HttpServletRequest request, HttpServletResponse response, HttpSession session, OneViewLetDetails oneviewlet, List<String> timedetails, OnceViewContainer onecontainer) throws Exception {
        StringBuilder finalStringVal = new StringBuilder();

        String value = null;
        if (session.getAttribute("MeasureDrillTest") != null) {
            HashMap<String, ArrayList<String>> drillMap = null;
            drillMap = (HashMap<String, ArrayList<String>>) session.getAttribute("MeasureDrillTest");
            Set<String> keys = drillMap.keySet();
            ArrayList<String> values = new ArrayList<String>();
            values.addAll(keys);
            if (values.contains(oneviewlet.getNoOfViewLets())) {
                if (drillMap.get(oneviewlet.getNoOfViewLets()).get(0).equalsIgnoreCase(oneviewlet.getOneviewId())) {
                    value = drillMap.get(oneviewlet.getNoOfViewLets()).get(1);
                }
            }
        }
        if (oneviewlet.getMeasurDrill() != null) {
            value = oneviewlet.getMeasurDrill();
        }
        String istranseposse = request.getParameter("istranseposse");
        String istranseposse1 = (String) request.getAttribute("transpose");
        if ((istranseposse != null && istranseposse.equalsIgnoreCase("true")) || (istranseposse1 != null && istranseposse1.equalsIgnoreCase("true"))) {
            onecontainer.setistrasnsposee(istranseposse);
            if (istranseposse1 != null && istranseposse1.equalsIgnoreCase("true")) {
                istranseposse = istranseposse1;
            }
        }
        //finalStringVal.append("<td id='" + oneviewlet.getNoOfViewLets() + "' width='" + oneviewlet.getWidth() + "px' style='height:" + oneviewlet.getHeight() + "px;' rowspan='" + oneviewlet.getRowSpan() + "' colspan='" + oneviewlet.getColSpan() + "'>");
        if (request.getParameter("downloaingfdf") == null) {

            finalStringVal.append(new DashboardViewerDAO().getOneviewTableAndGraphHeader(oneviewlet, value, timedetails));
        }
        finalStringVal.append("<div  id='Dashlets-" + oneviewlet.getNoOfViewLets() + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 95%; height: 100%; margin-left: 5px; margin-right: 5px;overflow:auto;'>");

        DataSnapshotGenerator generateRepTable = new DataSnapshotGenerator();
        StringBuilder result = new StringBuilder();
        HashMap map = null;
        Container container = null;
        String reportId = oneviewlet.getRepId();
        String height = Integer.toString(oneviewlet.getHeight());
        String width = Integer.toString(oneviewlet.getWidth());
        if (request.getAttribute("OneviewTableTimeDetails") != null) {
            request.removeAttribute("OneviewTableTimeDetails");
        }

        if (timedetails != null && !oneviewlet.isOneviewReportTimeDetails() && !timedetails.isEmpty()) {
            request.setAttribute("OneviewTableTimeDetails", timedetails);
        }
        if (request.getAttribute("OneviewGraphTimeDetails") != null) {
            request.removeAttribute("OneviewGraphTimeDetails");
        }


        if (request.getAttribute("OneviewtableDate") != null) {
            request.removeAttribute("OneviewtableDate");
        }

        if (timedetails != null && !timedetails.isEmpty() && oneviewlet.isOneviewReportTimeDetails()) {
            request.setAttribute("OneviewtableDate", timedetails.get(2));
        }
        if (request.getAttribute("OneviewgraphDate") != null) {
            request.removeAttribute("OneviewgraphDate");
        }

        String userId = String.valueOf(session.getAttribute("USERID"));
        PbReportViewerBD reportViewerBD = new PbReportViewerBD();
        String strURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        strURL = strURL + "reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&action=open";
        request.setAttribute("url", strURL);
        request.setAttribute("REPORTID", reportId);

        if (onecontainer.getFilterBusinessRole() != null && !onecontainer.getFilterBusinessRole().equalsIgnoreCase("") && oneviewlet != null && oneviewlet.getRoleId() != null && !oneviewlet.getRoleId().equalsIgnoreCase("") && onecontainer.getFilterBusinessRole().equalsIgnoreCase(oneviewlet.getRoleId()) && !oneviewlet.isOneviewReportTimeDetails()) {
            if (onecontainer.getReportParameterValues() != null && !onecontainer.getReportParameterValues().isEmpty()) {
                request.setAttribute("reportParameterVals", onecontainer.getReportParameterValues());
            }
        }
        reportViewerBD.prepareReport(reportId, userId, request, response,false);
        map = (HashMap) session.getAttribute("PROGENTABLES");
        istranseposse = request.getParameter("istranseposse");


        container = (Container) map.get(reportId);
        oneviewlet.setContainer(container);
        if (container.isTopBottomTableEnable()) {
            result = generateRepTable.generateTopBottomTable(container, userId, width, width);
        } else {
            if (istranseposse != null && istranseposse.equalsIgnoreCase("true")) {
                container.setfromBKP(istranseposse);
            }
            result = generateRepTable.generateReportTable(container, userId, height, width);
        }
        if (result != null) {
            finalStringVal.append(result.toString());
        } else {
            finalStringVal.append("No Data");
        }
        finalStringVal.append("</div>");
        //finalStringVal.append("</td>");

        return finalStringVal.toString();
    }
//Surender

    public String getMeasureDetailsData(HttpServletRequest request, HttpServletResponse response, HttpSession session, OnceViewContainer onecontainer, OneViewLetDetails oneviewlet) throws FileNotFoundException, IOException, BadElementException, SQLException {
        StringBuilder finalStringVal = new StringBuilder();

        String valu = null;
        //Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
        if (oneviewlet.getisschedule()) {
        } else {
            if (session.getAttribute("MeasureDrillTest") != null) {
                HashMap<String, ArrayList<String>> drillMap = null;
                drillMap = (HashMap<String, ArrayList<String>>) session.getAttribute("MeasureDrillTest");
                Set<String> keys = drillMap.keySet();
                ArrayList<String> values = new ArrayList<String>();
                values.addAll(keys);
                if (values.contains(oneviewlet.getNoOfViewLets())) {
                    if (drillMap.get(oneviewlet.getNoOfViewLets()).get(0).equalsIgnoreCase(oneviewlet.getOneviewId())) {
                        valu = drillMap.get(oneviewlet.getNoOfViewLets()).get(1);
                    }
                }
            }
        }
        //End of code by sandeep on 17/10/14 for schedule// update local files in oneview
        if (oneviewlet.getMeasurDrill() != null) {
            valu = oneviewlet.getMeasurDrill();
        }
        String result = "";
        String compare = "Prior";
        List<String> tiemdetails = new ArrayList<String>();
        if (oneviewlet.isOneviewReportTimeDetails()) {
            tiemdetails = oneviewlet.getMsrCustomTimeDetails();
        } else {
            tiemdetails = (List<String>) onecontainer.timedetails;
        }
        DashboardViewerDAO dao = new DashboardViewerDAO();
        PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();
        List<String> measureIdVal = new ArrayList<String>();
        measureIdVal.add(oneviewlet.getRepId());
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        PbReturnObject pbretObjTime = new PbReturnObject();
        PbReturnObject pbretObjTime1 = new PbReturnObject();

        double currVal = 0.0;
        double priorVal = 0.0;
        double changePer = 0.0;
        String measurecolor = "";
        String userId = "";
        String action = "";
        String measureType1 = "";
        if (oneviewlet.getMeasureColor() != null && !oneviewlet.getMeasureColor().isEmpty()) {
            measurecolor = oneviewlet.getMeasureColor();
        }
        StringBuilder measureText = new StringBuilder();
        //Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
        if (oneviewlet.getisschedule()) {
            userId = onecontainer.getUserId();
        } else {
            userId = String.valueOf(session.getAttribute("USERID"));
            action = request.getParameter("action");
            measureType1 = request.getParameter("measureType");
        }
        //End of code by sandeep on 17/10/14 for schedule// update local files in oneview
        List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal, new HashMap<String, String>());
        if (kpiElements != null) {
            for (KPIElement elem : kpiElements) {
                if (elem.getElementName() != null) {
                    QueryCols.add(elem.getElementId());
                }
                QueryAggs.add(elem.getAggregationType());
            }
        }
        String filePath = "/usr/local/cache/oneviewmesureGO";
        File file = new File(filePath);
        String path = file.getAbsolutePath();
        File f = new File(path);
        if (file.exists()) {
        } else {
            f.mkdirs();
        }
        filePath = path + File.separator + onecontainer.oneviewName + "_" + onecontainer.oneviewId;
        File file1 = new File(filePath);
        boolean x = oneviewlet.getMeasurOprFlag();
        String flag = "null";
//        String qry="select REF_ELEMENT_ID from prg_user_all_info_details where ELEMENT_ID="+oneviewlet.getRepId()+"";
        String ViewByType = "";
        PbDb pbdb1 = new PbDb();
//            try {
//                if((action!=null && action.equalsIgnoreCase("GO")) || (measureType1!=null && measureType1.equalsIgnoreCase("measures")) ){
//
//                }else{
//                String query = "select REF_ELEMENT_ID from prg_user_all_info_details where ELEMENT_ID="+oneviewlet.getRepId()+"";
//                PbReturnObject graphDetailReturnObj = pbdb1.execSelectSQL(query);
//                ViewByType = graphDetailReturnObj.getFieldValueString(0, "REF_ELEMENT_ID");
//                List<OneViewLetDetails> dashletDetails = onecontainer.onviewLetdetails;
//            for (int i = 0; i < dashletDetails.size(); i++) {
//                OneViewLetDetails detail = dashletDetails.get(i);
////                if(oneviewlet.getRepId() == null ? ViewByType != null : !oneviewlet.getRepId().equals(ViewByType)){
//                String filePath1 = filePath+File.separator+onecontainer.oneviewName+"_"+detail.getNoOfViewLets()+File.separator+onecontainer.oneviewName+"_"+detail.getNoOfViewLets()+"_"+userId+"_"+ViewByType;
//   File measureGO = new File(filePath1);
//      if(measureGO.exists()){
//pbretObjTime=detail.getPbReturnObject();
// flag="true";
//  HashMap retobjects1=detail.getretobjects();
//  pbretObjTime=(PbReturnObject) retobjects1.get(userId);
//oneviewlet.setPbReturnObject(pbretObjTime);
//
//break;
//      }
//                    }
////                }else{
////                    flag="null";
////                }
//                }
//            } catch (Exception e) {
//            }
        PbReturnObject securityfilters;
        String securityqry = "select * from PRG_USER_ROLE_MEMBER_FILTER";
        securityfilters = (PbReturnObject) session.getAttribute("securityfilters");
        if (securityfilters != null) {
        } else {
            securityfilters = pbdb1.execSelectSQL(securityqry);
        }
        session.setAttribute("securityfilters", securityfilters);
        if (action != null && action.equalsIgnoreCase("open")) {
            pbretObjTime = oneviewlet.getPbReturnObject();

        } else {
            if (String.valueOf(QueryAggs.get(0)) != null && x == false) {
                //Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
                if (oneviewlet.getisschedule()) {
                    pbretObjTime = dao.getReturnObjectForOneView(QueryCols, QueryAggs, userId, oneviewlet.getRoleId(), null, onecontainer, oneviewlet);
                } else {
                    pbretObjTime = dao.getReturnObjectForOneView(QueryCols, QueryAggs, userId, oneviewlet.getRoleId(), request, onecontainer, oneviewlet);
                }
                oneviewlet.setPbReturnObject(pbretObjTime);
            } else {
                pbretObjTime = oneviewlet.getPbReturnObject();
                if (pbretObjTime == null) {
                    //Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
                    if (oneviewlet.getisschedule()) {
                        pbretObjTime = dao.getReturnObjectForOneView(QueryCols, QueryAggs, userId, oneviewlet.getRoleId(), null, onecontainer, oneviewlet);
                    } else {
                        pbretObjTime = dao.getReturnObjectForOneView(QueryCols, QueryAggs, userId, oneviewlet.getRoleId(), request, onecontainer, oneviewlet);
                    }
                    oneviewlet.setPbReturnObject(pbretObjTime);
                }
                oneviewlet.setMeasurOprFlag(false);
            }
        }
        HashMap retobjects = new HashMap();
        retobjects.put(userId, pbretObjTime);

        if (file1.exists()) {
            filePath = filePath + File.separator + onecontainer.oneviewName + "_" + oneviewlet.getNoOfViewLets();
            File measureGO = new File(filePath);
            if (measureGO.exists()) {
                String[] folders = measureGO.list();
                for (int i = 0; i < folders.length; i++) {
                    String path1 = folders[i];
                    String filePath1 = filePath + File.separator + path1;
                    String comparepath = onecontainer.oneviewName + "_" + oneviewlet.getNoOfViewLets() + "_" + userId + "_" + oneviewlet.getRepId();
                    measureGO = new File(filePath1);
                    if ((action != null && action.equalsIgnoreCase("GO")) || (measureType1 != null && measureType1.equalsIgnoreCase("measures"))) {
                        measureGO.delete();
                    } else {
                        if (comparepath == null ? path1 == null : comparepath.equals(path1)) {
                            measureGO.delete();
                        }
                    }
                }
//  measureGO.mkdirs();
            } else {
                measureGO.mkdirs();
            }
        } else {
            file1.mkdirs();
            filePath = filePath + File.separator + onecontainer.oneviewName + "_" + oneviewlet.getNoOfViewLets();
            file1 = new File(filePath);
            if (file1.exists()) {
            } else {
                file1.mkdirs();
            }
        }
        filePath = filePath + File.separator + onecontainer.oneviewName + "_" + oneviewlet.getNoOfViewLets() + "_" + userId + "_" + oneviewlet.getRepId();
        file1 = new File(filePath);
        file1.createNewFile();
        HashMap<String, HashMap<String, String>> chartData12 = new HashMap<String, HashMap<String, String>>();
        HashMap<String, String> valuesmeasure = new HashMap<String, String>();
        ArrayList chartJson = new ArrayList();
//                         if (QueryCols.size() == 4) {
        if (pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString()) != null && pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString()) != "") {
            currVal = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())));
        } else {
            currVal = 0.0;
        }
        priorVal = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString())));
        if (QueryCols.size() == 4) {
            changePer = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString())));
        }
        chartJson.add(currVal);
        chartJson.add(priorVal);
        chartJson.add(changePer);

        valuesmeasure.put("currVal", String.valueOf(currVal));
        valuesmeasure.put("priorVal", String.valueOf(priorVal));
        valuesmeasure.put("changePer", String.valueOf(changePer));
//        }
        Gson gson = new Gson();
        FileReadWrite fileReadWrite = new FileReadWrite();
        chartData12.put("region" + oneviewlet.getNoOfViewLets(), valuesmeasure);
        String ab = gson.toJson(chartData12);

        fileReadWrite.writeToFile(filePath, ab);

        String FOLDER_ID = "";
        String MEMEBER_VALUE = "";
        String ELEMENT_ID = "";
        ArrayList viewbys = new ArrayList();

//if(action!=null && action.equalsIgnoreCase("save")){
//                         if(securityfilters.getRowCount()>0){
//                   for(int i=0;i<securityfilters.getRowCount();i++){
//                 FOLDER_ID = securityfilters.getFieldValueString(0, "USER_ID");
//                 ELEMENT_ID = securityfilters.getFieldValueString(i, "ELEMENT_ID");
//                         viewbys.add(ELEMENT_ID);
//                             }
//                   request.setAttribute("issecurity","true");
//                   request.setAttribute("viewbys", viewbys);
//if(viewbys!=null){
//        if(flag == null ? "null" == null : flag.equals("null")){
//
//        if (String.valueOf(QueryAggs.get(0)) != null && x==false ) {
//            //Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
//if(oneviewlet.getisschedule()){
//     pbretObjTime1 = dao.getReturnObjectForOneView(QueryCols, QueryAggs, userId, oneviewlet.getRoleId(), null,onecontainer,oneviewlet);
//}else{
//            pbretObjTime1 = dao.getReturnObjectForOneView(QueryCols, QueryAggs, userId, oneviewlet.getRoleId(), request,onecontainer,oneviewlet);
//            }
////                oneviewlet.setPbReturnObject(pbretObjTime);
//            }
//        else{
//            pbretObjTime1=oneviewlet.getPbReturnObject();
//            if(pbretObjTime1 ==null){
//                //Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
//                if(oneviewlet.getisschedule()){
//                   pbretObjTime1 = dao.getReturnObjectForOneView(QueryCols, QueryAggs, userId, oneviewlet.getRoleId(), null,onecontainer,oneviewlet);
//                }else{
//                pbretObjTime1 = dao.getReturnObjectForOneView(QueryCols, QueryAggs, userId, oneviewlet.getRoleId(), request,onecontainer,oneviewlet);
//                }
////                    oneviewlet.setPbReturnObject(pbretObjTime);
//                }
//            oneviewlet.setMeasurOprFlag(false);
//        }
////         for(int i=0;i<securityfilters.getRowCount();i++){
////                 FOLDER_ID = securityfilters.getFieldValueString(0, "USER_ID");
////                 ELEMENT_ID = securityfilters.getFieldValueString(i, "ELEMENT_ID");
////                 MEMEBER_VALUE = securityfilters.getFieldValueString(i, "ELEMENT_ID");
////                         viewbys.add(ELEMENT_ID);
////
////                         String filterqry="select * from R_GO_"+oneviewlet.getRepId();
////
////               filePath = "/usr/local/cache/oneviewmesureGO"+File.separator+onecontainer.oneviewName+"_"+onecontainer.oneviewId+File.separator+onecontainer.oneviewName+"_"+oneviewlet.getNoOfViewLets()+File.separator+onecontainer.oneviewName+"_"+oneviewlet.getNoOfViewLets()+"_"+FOLDER_ID+"_"+oneviewlet.getRepId();
////                     file1 = new File(filePath);
////                      file1.createNewFile();
//////                     HashMap<String,HashMap<String,String>> chartData12 = new HashMap<String,HashMap<String,String>>();
//////                     HashMap<String,String> valuesmeasure = new HashMap<String,String>();
//////                    ArrayList chartJson = new ArrayList();
//////                         if (QueryCols.size() == 4) {
//////                     if(pbretObjTime1.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())!=null && pbretObjTime1.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())!=""){
//////                     currVal = Double.parseDouble((pbretObjTime1.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())));
//////        }else{
//////                currVal=0.0;
//////        }
//////                       if (QueryCols.size() == 4) {
//////                      changePer = Double.parseDouble((pbretObjTime1.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString())));
//////                             }
////                       chartJson.add(currVal);
////                       chartJson.add(priorVal);
////                       chartJson.add(changePer);
////
////                       valuesmeasure.put("currVal", String.valueOf(currVal));
////                       valuesmeasure.put("priorVal", String.valueOf(priorVal));
////                       valuesmeasure.put("changePer", String.valueOf(changePer));
//////        }
////// Gson gson = new Gson();
//////  FileReadWrite fileReadWrite = new FileReadWrite();
////                      chartData12.put("region"+oneviewlet.getNoOfViewLets(),valuesmeasure);
////                       String  ab1 = gson.toJson(chartData12);
////
////        fileReadWrite.writeToFile(filePath,ab1);
////          retobjects.put(FOLDER_ID, pbretObjTime1);
////            }
//
//    }
//
//}
//        }
//
//                       }
        PbReturnObject securityobj = null;
        if (action != null && action.equalsIgnoreCase("open")) {
            securityobj = dao.getsecurityvalue(QueryCols, QueryAggs, userId, oneviewlet.getRoleId(), request, onecontainer, oneviewlet);
        }

// String sad =securityobj.getFieldValueString(0, 0);
        int heigth = 0;
        int width = 0;
        heigth = oneviewlet.getHeight();
        width = oneviewlet.getWidth();
        String measureType = "";
        String oneviewtypedate = "";
        ArrayList assignedGraphIds = oneviewlet.getAssignedGraphIds();
        ArrayList assignedGraphNames = oneviewlet.getAssignedGraphNames();
        //Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
        if (!oneviewlet.getisschedule()) {
            oneviewtypedate = (String) session.getAttribute("oneviewdatetype");
        }
        //End of code by sandeep on 17/10/14 for schedule// update local files in oneview
        if (oneviewtypedate != null && oneviewtypedate.equalsIgnoreCase("true")) {
            if (assignedGraphIds != null) {
                assignedGraphIds.clear();
            }
        } else {
            assignedGraphIds = oneviewlet.getAssignedGraphIds();
        }
        if (tiemdetails != null && !tiemdetails.isEmpty()) {
            if (tiemdetails.get(3).equalsIgnoreCase("Day")) {
                measureType = "DTD";
            } else if (tiemdetails.get(3).equalsIgnoreCase("Week")) {
                measureType = "WTD";
            } else if (tiemdetails.get(3).equalsIgnoreCase("Month")) {
                measureType = "MTD";
            } else if (tiemdetails.get(3).equalsIgnoreCase("Quarter")) {
                measureType = "QTD";
            } else if (tiemdetails.get(3).equalsIgnoreCase("Year")) {
                measureType = "YTD";
            }
        } else {
            measureType = "MTD";
        }

        String displayTest = "display:none; ";
        String newType = "display: none;";
        if (oneviewlet.isNewTypeMeasure()) {
            displayTest = "display: ;";
        } else {
            newType = "display: ;";
        }

        String formatType = "";
        String formatValue = "";
        String prefixValue = "";
        String suffixValue = "";
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("K")) {
            formatType = "K";
            formatValue = "K";
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("M")) {
            formatType = "M";
            formatValue = "Mn";
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("L")) {
            formatType = "L";
            formatValue = "Lkh";
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("Cr")) {
            formatType = "Cr";
            formatValue = "Crs";
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("%")) {
            formatType = "%";
            formatValue = "%";
        }
        if (oneviewlet.getPrefixValue() != null) {
            prefixValue = oneviewlet.getPrefixValue();
        }
        if (oneviewlet.getSuffixValue() != null) {
            suffixValue = oneviewlet.getSuffixValue();
        }
        if (oneviewlet.getSuffixValue() != null) {
            if (oneviewlet.getSuffixValue().equalsIgnoreCase("K")) {
                suffixValue = "K";
            } else if (oneviewlet.getSuffixValue().equalsIgnoreCase("L")) {
                suffixValue = "Lkh";
            } else if (oneviewlet.getSuffixValue().equalsIgnoreCase("M")) {
                suffixValue = "Mn";
            } else if (oneviewlet.getSuffixValue().equalsIgnoreCase("Cr")) {
                suffixValue = "Crs";
            }
        }
        String currFourthVal = "";
        String priorFourthVal = "";

        currFourthVal = oneviewlet.getCurrValue();
        priorFourthVal = oneviewlet.getPriorValue();
        if (QueryCols.size() == 4) {

            if (action != null && action.equalsIgnoreCase("open")) {
                int count = securityobj.getRowCount();
                int coountcol = securityobj.getColumnCount();
                if (!securityobj.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString()).equalsIgnoreCase("")) {
                    currVal = Double.parseDouble((securityobj.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())));
                }
                if (securityobj.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()) != null && !securityobj.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()).equalsIgnoreCase("")) {
                    priorVal = Double.parseDouble((securityobj.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString())));
                }
                if (securityobj.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString()) != null && !securityobj.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString()).equalsIgnoreCase("")) {
                    changePer = Double.parseDouble((securityobj.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString())));
                }
            } else {
                if (!pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString()).equalsIgnoreCase("")) {
                    currVal = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())));
                }
                if (pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()) != null && !pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()).equalsIgnoreCase("")) {
                    priorVal = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString())));
                }
                if (pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString()) != null && !pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString()).equalsIgnoreCase("")) {
                    changePer = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(3)).toString())));
                }
            }
            int decimalPlaces = 1;
            BigDecimal curval = new BigDecimal(currVal);
            BigDecimal prior = new BigDecimal(priorVal);
            BigDecimal chper = new BigDecimal(changePer);
            curval = curval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
            prior = prior.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
            chper = chper.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
            NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));

            String currWidth = "";
            String priorwidth = "";
            String firstTest = "display:none;";
            String secondTest = "display:none;";
            String thirdTest = "display:none;";
            String fourthTest = "display:none;";
            String fifthTest = "display:none;";

            if (oneviewlet.getDisplayType() == null || oneviewlet.getDisplayType().equalsIgnoreCase("fifth")) {
                firstTest = "display:;";
            } else if (oneviewlet.getDisplayType().equalsIgnoreCase("first")) {
                secondTest = "display:;";
            } else if (oneviewlet.getDisplayType().equalsIgnoreCase("second")) {
                thirdTest = "display:;";
            } else if (oneviewlet.getDisplayType().equalsIgnoreCase("third")) {
                fourthTest = "display:;";
            } else if (oneviewlet.getDisplayType().equalsIgnoreCase("fourth")) {
                fifthTest = "display:;";
            }
            String value = "";
            if (currVal > priorVal) {
                currWidth = "100";
                value = Integer.toString((int) currVal);
                if (!value.substring(0, 1).equalsIgnoreCase("-")) {
                    priorwidth = Integer.toString((int) (priorVal / currVal * 100));
                } else {
                    priorwidth = Integer.toString((int) (currVal / priorVal * 100)).replace("-", "");
                }
            } else if (currVal == 0.0 && priorVal == 0.0) {
                priorwidth = "0";
                currWidth = "0";
            } else if (currVal == priorVal) {
                priorwidth = "100";
                currWidth = "100";
            } else if (currVal < 0.0 && priorVal == 0.0) {
                priorwidth = "100";
                value = Integer.toString((int) currVal);
                if (!value.substring(0, 1).equalsIgnoreCase("-")) {
                    currWidth = Integer.toString((int) (currVal / priorVal * 100));
                } else {
                    currWidth = Integer.toString((int) (priorVal / currVal * 100)).replace("-", "");
                }
            } else {
                priorwidth = "100";
                value = Integer.toString((int) currVal);
                if (!value.substring(0, 1).equalsIgnoreCase("-")) {
                    currWidth = Integer.toString((int) (currVal / priorVal * 100));
                } else {
                    currWidth = Integer.toString((int) (priorVal / currVal * 100)).replace("-", "");
                }
            }

            if (formatType != null && formatValue != null && oneviewlet.getFormatVal() != null && oneviewlet.getRoundVal() != null && !oneviewlet.getRoundVal().equalsIgnoreCase("")) {
                String value1 = NumberFormatter.getModifiedNumber(curval, oneviewlet.getFormatVal(), Integer.parseInt(oneviewlet.getRoundVal()));
                if (action != null && action.equalsIgnoreCase("open")) {
                    if (securityobj.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()) != null && (!securityobj.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()).equalsIgnoreCase(""))) {
                        String priorValstr = securityobj.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString());
                        if (!priorValstr.equalsIgnoreCase("") && !priorValstr.equalsIgnoreCase("undefined") && priorValstr != null) {
                            BigDecimal priorVal1 = new BigDecimal(Double.parseDouble(priorValstr.replace(",", "")));
                            priorValstr = NumberFormatter.getModifiedNumber(priorVal1, oneviewlet.getFormatVal(), Integer.parseInt(oneviewlet.getRoundVal()));
                            oneviewlet.setPriorValue(priorValstr);
                        }
                    }
                } else {
                    if (pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()) != null && !pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()).equalsIgnoreCase("")) {
                        String priorValstr = pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString());
                        if (!priorValstr.equalsIgnoreCase("") && !priorValstr.equalsIgnoreCase("undefined") && priorValstr != null) {
                            BigDecimal priorVal1 = new BigDecimal(Double.parseDouble(priorValstr.replace(",", "")));
                            priorValstr = NumberFormatter.getModifiedNumber(priorVal1, oneviewlet.getFormatVal(), Integer.parseInt(oneviewlet.getRoundVal()));
                            oneviewlet.setPriorValue(priorValstr);
                        }
                    }
                }
                oneviewlet.setCurrValue(value1);
            }

            currFourthVal = oneviewlet.getCurrValue();
            priorFourthVal = oneviewlet.getPriorValue();
            int minRadious = 40;
            int minLength = (6 * minRadious) / 40;
            String lformatType = formatType;
            String lsuffixValue = suffixValue;

            if (oneviewlet.getDisplayType() != null && (currFourthVal.length() > minLength || priorFourthVal.length() > minLength)) { //&& !(formatType.trim().length() >0)
                OneViewBD viewBd = new OneViewBD();
                HashMap valMap = new HashMap();
                valMap.put("currVal", currVal);
                valMap.put("priorVal", priorVal);

                HashMap FormattedValMap = new HashMap();
                FormattedValMap = viewBd.getFormatedMesureValues(valMap, minRadious, formatType, Integer.parseInt(oneviewlet.getRoundVal()));
                currFourthVal = FormattedValMap.get("currVal").toString();
                priorFourthVal = FormattedValMap.get("priorVal").toString();
                lformatType = FormattedValMap.get("format").toString();
                lsuffixValue = lformatType;
            }

//Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
            if (oneviewlet.getisschedule()) {
                finalStringVal.append(new PbReportViewerDAO().getOneviewMeasureHeasder(oneviewlet, valu, currVal, priorVal, formatter.format(curval), formatter.format(chper), formatter.format(prior)));

            } else {
                if (request.getParameter("downloaingfdf") == null) {
                    finalStringVal.append(new PbReportViewerDAO().getOneviewMeasureHeasder(oneviewlet, valu, currVal, priorVal, formatter.format(curval), formatter.format(chper), formatter.format(prior)));

                }
            }
            //End of code by sandeep on 17/10/14 for schedule// update local files in oneview
            finalStringVal.append("<div id='Dashlets-" + oneviewlet.getNoOfViewLets() + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 100%; margin-left: 10px; margin-right: 10px;'>");
            //added by srikanth.p for trend graph
            if (oneviewlet.isDialChart()) {
                OneViewBD viewBd = new OneViewBD();
                StringBuilder dialScript = new StringBuilder();
                int days = viewBd.getOneViewDays(onecontainer);
                oneviewlet.setTargetValPerDay(currVal);
                dialScript.append(viewBd.getMeasureMeterGraph(changePer, heigth, width, oneviewlet.getDialMap(), oneviewlet.getNoOfViewLets(), oneviewlet, false, days, curval.doubleValue()));
                measureText.append(dialScript);
            } else if (oneviewlet.isTrendGraph()) {
                String measId = oneviewlet.getRepId();
                String trendDate = oneviewlet.getTrendDate();
                String viewLetNum = oneviewlet.getNoOfViewLets();
                int height = oneviewlet.getHeight();
                int divWidth = oneviewlet.getWidth();
                String buzRoleId = oneviewlet.getRoleId();

                measureText.append(dao.getOneViewRollingGraphJQ(measId, trendDate, userId, viewLetNum, height, divWidth, oneviewlet, "null"));
            } else {
                //Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
                String context = "";
                if (oneviewlet.getisschedule()) {
                    context = onecontainer.getContextPath();
                } else {
                    context = request.getContextPath();
                }
                //End of code by sandeep on 17/10/14 for schedule// update local files in oneview
                if (oneviewlet.getCurrValue() != null) {
                    if (changePer != 0.0) {
                        if (oneviewlet.getMeasType().equalsIgnoreCase("Standard")) {
                            if (changePer > 0.0 && currVal != priorVal) {
                                if (measurecolor.equalsIgnoreCase("")) {
                                    if (oneviewlet.getCurrValue().contains("-")) {
                                        measurecolor = "red";
                                    }
                                }
                                //Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
                                if (oneviewlet.getisschedule()) {
                                    measureText.append(new DashboardViewerDAO().getOneviewMeasures(oneviewlet, heigth, width, displayTest, measureType, currWidth, compare, priorwidth, curval, chper, prior, newType, context, tiemdetails, onecontainer, null));

                                } else {
                                    measureText.append(new DashboardViewerDAO().getOneviewMeasures(oneviewlet, heigth, width, displayTest, measureType, currWidth, compare, priorwidth, curval, chper, prior, newType, context, tiemdetails, onecontainer, session));
                                }
                                //End of code by sandeep on 17/10/14 for schedule// update local files in oneview

                            } else if (currVal == priorVal) {
                                if (oneviewlet.getFormatVal() != null && oneviewlet.getPrefixValue() == null && oneviewlet.getSuffixValue() == null && !oneviewlet.getFormatVal().equalsIgnoreCase("K") && !oneviewlet.getFormatVal().equalsIgnoreCase("M") && !oneviewlet.getFormatVal().equalsIgnoreCase("L") && !oneviewlet.getFormatVal().equalsIgnoreCase("Cr")) {
                                    measureText.append("<center><table align='' style='overflow:auto;' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' style='" + displayTest + "'><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getCurrValue() + "</td></tr></table></td></tr><tr><td><table><tr><td >" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getPriorValue() + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' style='" + newType + "'><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:25pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)'>" + oneviewlet.getCurrValue() + "</a></td><td colspan='1' align='right'></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td>");
                                    measureText.append("<td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr><tr><td colspan='2' height='" + oneviewlet.getHeight() / 2 + "px' width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'><tr><td width='" + oneviewlet.getWidth() + "px'></td><td id='addCommentsId' style='' width=''></td><td ><a   style='text-decoration: none;' onclick=\"measureOptions('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a   style='text-decoration: none;' onclick=\"taggleNewMeasures('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table></td></tr></table></center>");
                                } else {
                                    measureText.append("<center><table align='' style='overflow:auto;' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' style='" + displayTest + "'><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></td></tr></table></td></tr><tr><td><table><tr><td >" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getPriorValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' style='" + newType + "'><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:25pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></a></td><td colspan='1' align='right'></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info \" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr><tr><td colspan='2' height='" + oneviewlet.getHeight() + "px' width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'><tr><td width='" + oneviewlet.getWidth() + "px'></td><td id='addCommentsId' style='' width=''></td><td ><a   style='text-decoration: none;' onclick=\"measureOptions('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a   style='text-decoration: none;' onclick=\"taggleNewMeasures('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table></td></tr></table></center>");
                                }
//
                            } else {
                                //Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
                                if (oneviewlet.getisschedule()) {
                                    measureText.append(new DashboardViewerDAO().getOneviewMeasures1(oneviewlet, heigth, width, displayTest, measureType, currWidth, compare, priorwidth, curval, chper, prior, newType, context, pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()), tiemdetails, onecontainer, null));

                                } else {
                                    measureText.append(new DashboardViewerDAO().getOneviewMeasures1(oneviewlet, heigth, width, displayTest, measureType, currWidth, compare, priorwidth, curval, chper, prior, newType, context, pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(1)).toString()), tiemdetails, onecontainer, session));
                                }
                                //End of code by sandeep on 17/10/14 for schedule// update local files in oneview
                            }
                        } else if (oneviewlet.getMeasType().equalsIgnoreCase("Non-Standard")) {
                            if (measurecolor.equalsIgnoreCase("")) {
                                if (oneviewlet.getCurrValue().contains("-")) {
                                    measurecolor = "green";
                                }
                            }
                            if (changePer < 0.0 && currVal != priorVal) {

                                if (oneviewlet.getFormatVal() != null && oneviewlet.getPrefixValue() == null && oneviewlet.getSuffixValue() == null && !oneviewlet.getFormatVal().equalsIgnoreCase("K") && !oneviewlet.getFormatVal().equalsIgnoreCase("M") && !oneviewlet.getFormatVal().equalsIgnoreCase("L") && oneviewlet.getFormatVal().equalsIgnoreCase("%") && !oneviewlet.getFormatVal().equalsIgnoreCase("Cr")) {
                                    measureText.append("<center><table id='compareMeasIdfirst" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + firstTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td id='currValfirst" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' ><span style='color:" + measurecolor + ";font-size:18pt;'>" + oneviewlet.getCurrValue() + "</span></a></td><td colspan='1' align='right'></td></tr><tr height='60'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    measureText.append("<center><table id='compareMeasurIdsecond" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + secondTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getCurrValue() + "</td></tr></table></td></tr><tr><td><table><tr><td style='white-space:nowrap;'>" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "'>" + oneviewlet.getPriorValue() + "</td></tr></table></td></tr></table></td></tr></table></td></tr><tr height='40'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    measureText.append("<center><table id='compareMeasurIdthird" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + thirdTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "'><td><table><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' style='color:#008000;text-align:left;vertical-align:top;'>" + oneviewlet.getCurrValue() + "</a></td><td><img  style='border-left:medium hidden;border-top:medium hidden;' width='40px' height='40px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Green Down Arrow.jpg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('( Prior " + oneviewlet.getRepName() + "=" + formatter.format(prior) + " )')\"/><font style='color:#008000;'>(" + chper + "%)</font></td></tr></table></td></tr><tr><td colspan='2'  width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'><tr><td width='" + oneviewlet.getWidth() + "px'></td><td id='addCommentsId' style='' width=''></td><td width=''><a   style='text-decoration: none;' onclick=\"measureOptions('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a   style='text-decoration: none;' onclick=\"taggleNewMeasures('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + oneviewlet.getWidth() + "px' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/><td id='measureDescId" + oneviewlet.getNoOfViewLets() + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' height='" + oneviewlet.getHeight() + "px'></td>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td><td style='display:none'><input type='text' id='measureValue" + oneviewlet.getNoOfViewLets() + "' name='' value='' ></td></tr></table></td></tr></table></td></tr></table></center>");
                                    if (Float.parseFloat(currFourthVal.replace(lformatType, "").replace(",", "")) > Float.parseFloat(priorFourthVal.replace(lformatType, "").replace(",", ""))) {
                                        measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:15px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 50px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                        measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    } else {
                                        measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                        measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    }

                                } else {
                                    measureText.append("<center><table id='compareMeasIdfirst" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + firstTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td id='currValfirst" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' ><span style='color:" + measurecolor + ";font-size:18pt;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></span></a></td><td colspan='1' align='right'></td></tr><tr height='60'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    measureText.append("<center><table id='compareMeasurIdsecond" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + secondTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:6pt;'>" + suffixValue + "</span> </td></tr></table></td></tr><tr><td><table><tr><td style='white-space:nowrap;'>" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getPriorValue().replace(formatType, "") + "<span style='font-size:6pt;'>" + suffixValue + "</span></td></tr></table></td></tr></table></td></tr><tr  height='40'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    measureText.append("<center><table id='compareMeasurIdthird" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + thirdTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td><table><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' style='color:#008000;text-align:left;vertical-align:top;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></a></td><td><img  style='border-left:medium hidden;border-top:medium hidden;' width='40px' height='40px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Green Down Arrow.jpg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('(Prior " + oneviewlet.getRepName() + "=" + formatter.format(prior) + ")')\"/><font style='color:#008000;'>(" + chper + "%)</font></td></tr></table></td></tr><tr><td colspan='2'  width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'><tr><td width='" + oneviewlet.getWidth() + "px'></td><td id='addCommentsId' style='' width=''></td><td width=''><a  style='text-decoration: none;' onclick=\"measureOptions('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a   style='text-decoration: none;' onclick=\"taggleNewMeasures('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + oneviewlet.getWidth() + "px'  style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/><td id='measureDescId" + oneviewlet.getNoOfViewLets() + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' height='" + oneviewlet.getHeight() + "px'></td>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td><td style='display:none'><input type='text' id='measureValue" + oneviewlet.getNoOfViewLets() + "' name='' value='' ></td></tr></table></td></tr></table></td></tr></table></center>");
                                    if (Float.parseFloat(currFourthVal.replace(lformatType, "").replace(",", "")) > Float.parseFloat(priorFourthVal.replace(lformatType, "").replace(",", ""))) {
                                        measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:15px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 50px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                        measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    } else {
                                        measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                        measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 40px; height: 40px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 50px; height: 50px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    }

                                }
                            } else if (currVal == priorVal) {
                                if (oneviewlet.getFormatVal() != null && oneviewlet.getPrefixValue() == null && oneviewlet.getSuffixValue() == null && !oneviewlet.getFormatVal().equalsIgnoreCase("K") && !oneviewlet.getFormatVal().equalsIgnoreCase("M") && !oneviewlet.getFormatVal().equalsIgnoreCase("L") && !oneviewlet.getFormatVal().equalsIgnoreCase("Cr")) {
                                    measureText.append("<center><table align='' style='overflow:auto;' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' style='" + displayTest + "'><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getCurrValue() + "</td></tr></table></td></tr><tr><td><table><tr><td >" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getPriorValue() + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' style='" + newType + "'><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)'>" + oneviewlet.getCurrValue() + "</a></td><td colspan='1' align='right'></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                } else {
                                    measureText.append("<center><table align='' style='overflow:auto;' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' style='" + displayTest + "'><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getCurrValue().replace("K", "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></td></tr></table></td></tr><tr><td><table><tr><td >" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getPriorValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' style='" + newType + "'><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></a></td><td colspan='1' align='right'></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                }
                            } else {


                                if (oneviewlet.getFormatVal() != null && oneviewlet.getPrefixValue() == null && oneviewlet.getSuffixValue() == null && !oneviewlet.getFormatVal().equalsIgnoreCase("K") && !oneviewlet.getFormatVal().equalsIgnoreCase("M") && !oneviewlet.getFormatVal().equalsIgnoreCase("L") && !oneviewlet.getFormatVal().equalsIgnoreCase("Cr")) {
                                    measureText.append("<center><table id='compareMeasIdfirst" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + firstTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td id='currValfirst" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' ><span style='color:" + measurecolor + ";font-size:18pt;'>" + oneviewlet.getCurrValue() + "</span></a></td><td colspan='1' align='right'></td></tr><tr height='60'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    measureText.append("<center><table id='compareMeasurIdsecond" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + secondTest + ";' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getCurrValue() + "</td></tr></table></td></tr><tr><td><table><tr><td style='white-space:nowrap;'>" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getPriorValue() + "</td><tr></tr></table></td></tr></table></td></tr><tr height='40'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    measureText.append("<center><table id='compareMeasurIdthird" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + thirdTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td><table><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' style='color:#FF0000;text-align:left;vertical-align:top;'>" + oneviewlet.getCurrValue() + "</a></td><td><img  style='border-left:medium hidden;border-top:medium hidden;' width='40px' height='40px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Red Up Arrow.jpeg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('(Prior " + oneviewlet.getRepName() + "=" + formatter.format(prior) + ")')\"/><font style='color:#FF0000;'>(" + chper + "%)</font></td></tr></table></td></tr><tr><td colspan='2'  width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'><tr><td width='" + oneviewlet.getWidth() + "px'></td><td id='addCommentsId' style='' width=''></td><td ><a   style='text-decoration: none;' onclick=\"measureOptions('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a   style='text-decoration: none;' onclick=\"taggleNewMeasures('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + oneviewlet.getWidth() + "px' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/><td id='measureDescId" + oneviewlet.getNoOfViewLets() + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#FF0000;font-size:7pt;' height='" + oneviewlet.getHeight() + "px'></td>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td><td style='display:none'><input type='text' id='measureValue" + oneviewlet.getNoOfViewLets() + "' name='' value='' ></td></tr></table></td></tr></table></td></tr></table></center>");
                                    if (Float.parseFloat(currFourthVal.replace(lformatType, "").replace(",", "")) > Float.parseFloat(priorFourthVal.replace(lformatType, "").replace(",", ""))) {
                                        measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:15px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 50px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                        measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    } else {
                                        measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                        measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 40px; height: 40px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 50px; height: 50px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    }

                                } else {
                                    measureText.append("<center><table id='compareMeasIdfirst" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + firstTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td id='currValfirst" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' ><span style='color:" + measurecolor + ";font-size:18pt;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></span></a></td><td colspan='1' align='right'></td></tr><tr height='60'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    measureText.append("<center><table id='compareMeasurIdsecond" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + secondTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:6pt;'>" + suffixValue + "</span> </td></tr></table></td></tr><tr><td><table><tr><td style='white-space:nowrap;'>" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getPriorValue().replace(formatType, "") + "<span style='font-size:6pt;'>" + suffixValue + "</span></td></tr></table></td></tr></table></td></tr><tr height='40'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    measureText.append("<center><table id='compareMeasurIdthird" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + thirdTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td><table><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' style='color:#FF0000;text-align:left;vertical-align:top;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></a></td><td><img  style='border-left:medium hidden;border-top:medium hidden;' width='40px' height='40px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Red Up Arrow.jpeg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('(Prior " + oneviewlet.getRepName() + "=" + formatter.format(prior) + ")')\"/><font style='color:#FF0000;'>(" + chper + "%)</font></td></tr></table></td></tr><tr><td colspan='2'  width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'><tr><td width='" + oneviewlet.getWidth() + "px'></td><td id='addCommentsId' style='' width=''></td><td ><a   style='text-decoration: none;' onclick=\"measureOptions('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a   style='text-decoration: none;' onclick=\"taggleNewMeasures('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + oneviewlet.getWidth() + "px' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/><td id='measureDescId" + oneviewlet.getNoOfViewLets() + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#FF0000;font-size:7pt;' height='" + oneviewlet.getHeight() + "px'></td>");
                                    if (assignedGraphIds != null) {
                                        String tdWidth = "0";
                                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                                            if (i > 0) {
                                                tdWidth = "1%";
                                            }
                                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                        }
                                    }
                                    measureText.append("<td id='measureNavigateIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td><td style='display:none'><input type='text' id='measureValue" + oneviewlet.getNoOfViewLets() + "' name='' value='' ></td></tr></table></td></tr></table></td></tr></table></center>");
                                    if (Float.parseFloat(currFourthVal.replace(lformatType, "").replace(",", "")) > Float.parseFloat(priorFourthVal.replace(lformatType, "").replace(",", ""))) {
                                        measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:15px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 50px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                        measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    } else {
                                        measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                        measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 40px; height: 40px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 50px; height: 50px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                        if (assignedGraphIds != null) {
                                            String tdWidth = "0";
                                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                                if (i > 0) {
                                                    tdWidth = "1%";
                                                }
                                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                            }
                                        }
                                        measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                    }

                                }
                            }
                        }
                    } else {

                        if (oneviewlet.getFormatVal() != null && oneviewlet.getPrefixValue() == null && oneviewlet.getSuffixValue() == null && !oneviewlet.getFormatVal().equalsIgnoreCase("K") && !oneviewlet.getFormatVal().equalsIgnoreCase("M") && !oneviewlet.getFormatVal().equalsIgnoreCase("L") && !oneviewlet.getFormatVal().equalsIgnoreCase("Cr")) {
                            measureText.append("<center><table align='' style='overflow:auto;' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' style='" + displayTest + "'><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getCurrValue() + "</td></tr></table></td></tr><tr><td><table><tr><td >" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getPriorValue() + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' style='" + newType + "'><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)'>" + oneviewlet.getCurrValue() + "</a></td><td colspan='1' align='right'></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                            if (assignedGraphIds != null) {
                                String tdWidth = "0";
                                for (int i = 0; i < assignedGraphIds.size(); i++) {
                                    if (i > 0) {
                                        tdWidth = "1%";
                                    }
                                    measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                }
                            }
                            measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                        } else {
                            measureText.append("<center><table align='' style='overflow:auto;' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' style='" + displayTest + "'><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></td></tr></table></td></tr><tr><td><table><tr><td >" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getPriorValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' style='" + newType + "'><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></a></td><td colspan='1' align='right'></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                            if (assignedGraphIds != null) {
                                String tdWidth = "0";
                                for (int i = 0; i < assignedGraphIds.size(); i++) {
                                    if (i > 0) {
                                        tdWidth = "1%";
                                    }
                                    measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                }
                            }
                            measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                        }
                    }
                } else {
                    if (changePer != 0.0) {
                        if (changePer > 0.0 && currVal != priorVal) {
                            measureText.append("<center><table id='compareMeasIdfirst" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + firstTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td id='currValfirst" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' ><span style='color:" + measurecolor + ";font-size:18pt;'>" + formatter.format(curval) + "</span></a></td><td colspan='1' align='right'></td></tr><tr height='60'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                            if (assignedGraphIds != null) {
                                String tdWidth = "0";
                                for (int i = 0; i < assignedGraphIds.size(); i++) {
                                    if (i > 0) {
                                        tdWidth = "1%";
                                    }
                                    measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                }
                            }
                            measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                            measureText.append("<center><table id='compareMeasurIdsecond" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + secondTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td style='white-space:nowrap;'>" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' >" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr height='40'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                            if (assignedGraphIds != null) {
                                String tdWidth = "0";
                                for (int i = 0; i < assignedGraphIds.size(); i++) {
                                    if (i > 0) {
                                        tdWidth = "1%";
                                    }
                                    measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                }
                            }
                            measureText.append("<td id='measureNavigateIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                            measureText.append("<center><table id='compareMeasurIdthird" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + thirdTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td><table><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' style='color:#008000;text-align:left;vertical-align:top;'>" + formatter.format(curval) + "</a></td><td><img  style='border-left:medium hidden;border-top:medium hidden;' width='40px' height='40px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Green Arrow.jpg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('(Prior " + oneviewlet.getRepName() + "=" + formatter.format(prior) + ")')\"/><font style='color:#008000;'>(" + chper + "%)</font></td></tr></table></td></tr><tr><td colspan='2' ' width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'><tr><td  width='" + oneviewlet.getWidth() + "px'></td><td id='addCommentsId' style='' width=''></td><td width=''><a   style='text-decoration: none;' onclick=\"measureOptions('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a   style='text-decoration: none;' onclick=\"taggleNewMeasures('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + oneviewlet.getWidth() + "px'  style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + oneviewlet.getNoOfViewLets() + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' height='" + oneviewlet.getHeight() + "px'></td>");
                            if (assignedGraphIds != null) {
                                String tdWidth = "0";
                                for (int i = 0; i < assignedGraphIds.size(); i++) {
                                    if (i > 0) {
                                        tdWidth = "1%";
                                    }
                                    measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                }
                            }
                            measureText.append("<td id='measureNavigateIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td><td style='display:none'><input type='text' id='measureValue" + oneviewlet.getNoOfViewLets() + "' name='' value='' ></td></tr></table></td></tr></table></center>");
                            if (Float.parseFloat(currFourthVal.replace(lformatType, "").replace(",", "")) > Float.parseFloat(priorFourthVal.replace(lformatType, "").replace(",", ""))) {
                                measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:15px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 50px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                if (assignedGraphIds != null) {
                                    String tdWidth = "0";
                                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                                        if (i > 0) {
                                            tdWidth = "1%";
                                        }
                                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                    }
                                }
                                measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                if (assignedGraphIds != null) {
                                    String tdWidth = "0";
                                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                                        if (i > 0) {
                                            tdWidth = "1%";
                                        }
                                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                    }
                                }
                                measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                            } else {
                                measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                if (assignedGraphIds != null) {
                                    String tdWidth = "0";
                                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                                        if (i > 0) {
                                            tdWidth = "1%";
                                        }
                                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                    }
                                }
                                measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 40px; height: 40px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 50px; height: 50px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                if (assignedGraphIds != null) {
                                    String tdWidth = "0";
                                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                                        if (i > 0) {
                                            tdWidth = "1%";
                                        }
                                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                    }
                                }
                                measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                            }

                        } else if (currVal == priorVal) {
                            measureText.append("<center><table align='' style='overflow:auto;' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' style='" + displayTest + "'><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td >" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' >" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' style='" + newType + "'><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                            if (assignedGraphIds != null) {
                                String tdWidth = "0";
                                for (int i = 0; i < assignedGraphIds.size(); i++) {
                                    if (i > 0) {
                                        tdWidth = "1%";
                                    }
                                    measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                }
                            }
                            measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                        } else {
                            measureText.append("<center><table id='compareMeasIdfirst" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + firstTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td id='currValfirst" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' ><span style='color:" + measurecolor + ";font-size:18pt;'>" + formatter.format(curval) + "</span></a></td><td colspan='1' align='right'></td></tr><tr height='60'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                            if (assignedGraphIds != null) {
                                String tdWidth = "0";
                                for (int i = 0; i < assignedGraphIds.size(); i++) {
                                    if (i > 0) {
                                        tdWidth = "1%";
                                    }
                                    measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                }
                            }
                            measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                            measureText.append("<center><table id='compareMeasurIdsecond" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + secondTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td style='white-space:nowrap;'>" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' >" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr height='40'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                            if (assignedGraphIds != null) {
                                String tdWidth = "0";
                                for (int i = 0; i < assignedGraphIds.size(); i++) {
                                    if (i > 0) {
                                        tdWidth = "1%";
                                    }
                                    measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                }
                            }
                            measureText.append("<td id='measureNavigateIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                            measureText.append("<center><table id='compareMeasurIdthird" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + thirdTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' '><td><table><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' style='color:#FF0000;text-align:left;vertical-align:top;'>" + formatter.format(curval) + "</a></td><td><img  style='border-left:medium hidden;border-top:medium hidden;' width='40px' height='40px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Red Arrow.jpeg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('( Prior " + oneviewlet.getRepName() + "=" + formatter.format(prior) + ")')\"/><font style='color:#FF0000;'>(" + chper + "%)</font></td></tr></table></td></tr><tr><td colspan='2'  width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'><tr><td  width='" + oneviewlet.getWidth() + "px'></td><td id='addCommentsId' style='' width=''></td><td width=''><a   style='text-decoration: none;' onclick=\"measureOptions('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td><a   style='text-decoration: none;' onclick=\"taggleNewMeasures('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + oneviewlet.getWidth() + "px'  style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId" + oneviewlet.getNoOfViewLets() + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#FF0000;font-size:7pt;' height='" + oneviewlet.getHeight() + "px'></td>");
                            if (assignedGraphIds != null) {
                                String tdWidth = "0";
                                for (int i = 0; i < assignedGraphIds.size(); i++) {
                                    if (i > 0) {
                                        tdWidth = "1%";
                                    }
                                    measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                }
                            }
                            measureText.append("<td id='measureNavigateIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td><td style='display:none'><input type='text' id='measureValue" + oneviewlet.getNoOfViewLets() + "' name='' value='' ></td></tr></table></td></tr></table></center>");
                            if (Float.parseFloat(currFourthVal.replace(lformatType, "").replace(",", "")) > Float.parseFloat(priorFourthVal.replace(lformatType, "").replace(",", ""))) {
                                measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:15px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 50px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                if (assignedGraphIds != null) {
                                    String tdWidth = "0";
                                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                                        if (i > 0) {
                                            tdWidth = "1%";
                                        }
                                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                    }
                                }
                                measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                if (assignedGraphIds != null) {
                                    String tdWidth = "0";
                                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                                        if (i > 0) {
                                            tdWidth = "1%";
                                        }
                                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                    }
                                }
                                measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                            } else {
                                measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                if (assignedGraphIds != null) {
                                    String tdWidth = "0";
                                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                                        if (i > 0) {
                                            tdWidth = "1%";
                                        }
                                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                    }
                                }
                                measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                                measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 40px; height: 40px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 50px; height: 50px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                                if (assignedGraphIds != null) {
                                    String tdWidth = "0";
                                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                                        if (i > 0) {
                                            tdWidth = "1%";
                                        }
                                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                                    }
                                }
                                measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                            }

                        }
                    } else {

                        measureText.append("<center><table id='compareMeasId" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;display:' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' style='" + displayTest + "'><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + formatter.format(curval) + "</td></tr></table></td></tr><tr><td><table><tr><td >" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' >" + formatter.format(prior) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' style='" + newType + "'><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)'>" + formatter.format(curval) + "</a></td><td colspan='1' align='right'></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                        if (assignedGraphIds != null) {
                            String tdWidth = "0";
                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                if (i > 0) {
                                    tdWidth = "1%";
                                }
                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                            }
                        }
                        measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");

                    }
                }
            }




        } else if (pbretObjTime != null) {
            if (action != null && action.equalsIgnoreCase("open")) {
                currVal = Double.parseDouble((securityobj.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())));
            } else {
                currVal = Double.parseDouble((pbretObjTime.getFieldValueString(0, ("A_" + QueryCols.get(0)).toString())));
            }
            BigDecimal curval = new BigDecimal(currVal);
            int decimalPlaces = 1;
            curval = curval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
            NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
            //Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
            String context = "";
            if (oneviewlet.getisschedule()) {
                context = onecontainer.getContextPath();
            } else {
                context = request.getContextPath();
            }
            //End of code by sandeep on 17/10/14 for schedule// update local files in oneview
//            String context = request.getContextPath();
            String currWidth = "100";
            String measurecolor1 = "";
            if (oneviewlet.getMeasType().equalsIgnoreCase("Standard")) {
                if (measurecolor1.equalsIgnoreCase("")) {
                    if (oneviewlet.getCurrValue() != null && !oneviewlet.getCurrValue().equalsIgnoreCase("") && oneviewlet.getCurrValue().contains("-")) {
                        measurecolor1 = "red";
                    }
                }
            } else if (oneviewlet.getMeasType().equalsIgnoreCase("Non-Standard")) {
                if (measurecolor1.equalsIgnoreCase("")) {
                    if (oneviewlet.getCurrValue() != null && !oneviewlet.getCurrValue().equalsIgnoreCase("") && oneviewlet.getCurrValue().contains("-")) {
                        measurecolor1 = "green";
                    }
                }
            }
            if (formatType != null && formatValue != null && oneviewlet.getFormatVal() != null && oneviewlet.getRoundVal() != null && !oneviewlet.getRoundVal().equalsIgnoreCase("")) {
                String value1 = NumberFormatter.getModifiedNumber(curval, oneviewlet.getFormatVal(), Integer.parseInt(oneviewlet.getRoundVal()));
                oneviewlet.setCurrValue(value1);
            }
//            String context="";
            //Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
            if (oneviewlet.getisschedule()) {
                finalStringVal.append(new SchedulerBD().getOneviewNoCurrMeasureHeasder(oneviewlet, valu, currVal, priorVal, formatter.format(curval)));

            } else {
                if (request.getParameter("downloaingfdf") == null) {
                    finalStringVal.append(new SchedulerBD().getOneviewNoCurrMeasureHeasder(oneviewlet, valu, currVal, priorVal, formatter.format(curval)));

                }
            }

            finalStringVal.append("<div id='Dashlets-" + oneviewlet.getNoOfViewLets() + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGray ; border-left: medium hidden LightGray ; border-right: medium hidden LightGray ; border-color: LightGray ; width: 100%; height: 100%; margin-left: 10px; margin-right: 10px;'>");

            if (oneviewlet.isTrendGraph()) {
                String measId = oneviewlet.getRepId();
                String trendDate = oneviewlet.getTrendDate();
                String viewLetNum = oneviewlet.getNoOfViewLets();
                int height = oneviewlet.getHeight();
                int divWidth = oneviewlet.getWidth();
                String buzRoleId = oneviewlet.getRoleId();

                measureText.append(dao.getOneViewRollingGraphJQ(measId, trendDate, userId, viewLetNum, height, divWidth, oneviewlet, "null"));
            } else {

                if (oneviewlet.getCurrValue() != null) {


                    String lformatType = formatType;
                    String lsuffixValue = suffixValue;
                    currWidth = "100";

                    BigDecimal prior = new BigDecimal(priorVal);
                    BigDecimal chper = new BigDecimal(changePer);
                    curval = curval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                    prior = prior.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                    chper = chper.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                    String priorwidth = "";
                    String firstTest = "display:none;";
                    String secondTest = "display:none;";
                    String thirdTest = "display:none;";
                    String fourthTest = "display:none;";
                    String fifthTest = "display:none;";
                    if (oneviewlet.getDisplayType() == null || oneviewlet.getDisplayType().equalsIgnoreCase("fifth")) {
                        firstTest = "display:;";
                    } else if (oneviewlet.getDisplayType().equalsIgnoreCase("first")) {
                        secondTest = "display:;";
                    } else if (oneviewlet.getDisplayType().equalsIgnoreCase("second")) {
                        thirdTest = "display:;";
                    } else if (oneviewlet.getDisplayType().equalsIgnoreCase("third")) {
                        fourthTest = "display:;";
                    } else if (oneviewlet.getDisplayType().equalsIgnoreCase("fourth")) {
                        fifthTest = "display:;";
                    }
                    if (oneviewlet.getFormatVal() != null && oneviewlet.getPrefixValue() == null && oneviewlet.getSuffixValue() == null && !oneviewlet.getFormatVal().equalsIgnoreCase("K") && !oneviewlet.getFormatVal().equalsIgnoreCase("M") && !oneviewlet.getFormatVal().equalsIgnoreCase("L") && !oneviewlet.getFormatVal().equalsIgnoreCase("Cr")) {
                        measureText.append("<center><table id='compareMeasIdfirst" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + firstTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' style='" + displayTest + "'><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getCurrValue() + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' ><span style='color:" + measurecolor + ";font-size:18pt;'>" + oneviewlet.getCurrValue() + "<span style='font-size:9pt;'></span></span></a></td><td colspan='1' align='right'></td></tr><tr height='60'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                        if (assignedGraphIds != null) {
                            String tdWidth = "0";
                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                if (i > 0) {
                                    tdWidth = "1%";
                                }
                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                            }
                        }
                        measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                        measureText.append("<center><table id='compareMeasurIdsecond" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + secondTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + oneviewlet.getCurrValue() + "<span style='font-size:6pt;'></span> </td></tr></table></td></tr></table></td></tr><tr  height='40'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                        if (assignedGraphIds != null) {
                            String tdWidth = "0";
                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                if (i > 0) {
                                    tdWidth = "1%";
                                }
                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                            }
                        }
                        measureText.append("<td id='measureNavigateIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                        measureText.append("<center><table id='compareMeasurIdthird" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + thirdTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td><table><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' style='color:#008000;text-align:left;vertical-align:top;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></a></td><td><img  style='border-left:medium hidden;border-top:medium hidden;' width='40px' height='40px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Green Down Arrow.jpg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('(Prior " + oneviewlet.getRepName() + "=" + formatter.format(prior) + ")')\"/><font style='color:#008000;'>(" + chper + "%)</font></td></tr></table></td></tr><tr><td colspan='2'  width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'><tr><td width='" + oneviewlet.getWidth() + "px'></td><td id='addCommentsId' style='' width=''></td><td width='' style='display:none;'><a  style='text-decoration: none;' onclick=\"measureOptions('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td style='display:none;'><a   style='text-decoration: none;' onclick=\"taggleNewMeasures('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + oneviewlet.getWidth() + "px'  style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/><td id='measureDescId" + oneviewlet.getNoOfViewLets() + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' height='" + oneviewlet.getHeight() + "px'></td>");
                        if (assignedGraphIds != null) {
                            String tdWidth = "0";
                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                if (i > 0) {
                                    tdWidth = "1%";
                                }
                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                            }
                        }
                        measureText.append("<td id='measureNavigateIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td><td style='display:none'><input type='text' id='measureValue" + oneviewlet.getNoOfViewLets() + "' name='' value='' ></td></tr></table></td></tr></table></td></tr></table></center>");
                        measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 50px; height: 50px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "'><font style='color: white;'>" + currFourthVal + "<span style='font-size:6pt;'></span> </font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                        if (assignedGraphIds != null) {
                            String tdWidth = "0";
                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                if (i > 0) {
                                    tdWidth = "1%";
                                }
                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                            }
                        }
                        measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                        measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 50px; height: 50px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "'><font style='color: white;'>" + currFourthVal + "<span style='font-size:6pt;'></span> </font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                        if (assignedGraphIds != null) {
                            String tdWidth = "0";
                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                if (i > 0) {
                                    tdWidth = "1%";
                                }
                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                            }
                        }
                        measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                    } else {
                        measureText.append("<center><table id='compareMeasIdfirst" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + firstTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' style='" + displayTest + "'><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getCurrValue() + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' ><span style='color:" + measurecolor + ";font-size:18pt;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></span></a></td><td colspan='1' align='right'></td></tr><tr height='60'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                        if (assignedGraphIds != null) {
                            String tdWidth = "0";
                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                if (i > 0) {
                                    tdWidth = "1%";
                                }
                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                            }
                        }
                        measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                        measureText.append("<center><table id='compareMeasurIdsecond" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + secondTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span> </td></tr></table></td></tr></table></td></tr><tr  height='40'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                        if (assignedGraphIds != null) {
                            String tdWidth = "0";
                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                if (i > 0) {
                                    tdWidth = "1%";
                                }
                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                            }
                        }
                        measureText.append("<td id='measureNavigateIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                        measureText.append("<center><table id='compareMeasurIdthird" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + thirdTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td><table><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' style='color:#008000;text-align:left;vertical-align:top;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></a></td><td><img  style='border-left:medium hidden;border-top:medium hidden;' width='40px' height='40px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Green Down Arrow.jpg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('(Prior " + oneviewlet.getRepName() + "=" + formatter.format(prior) + ")')\"/><font style='color:#008000;'>(" + chper + "%)</font></td></tr></table></td></tr><tr><td colspan='2'  width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'><tr><td width='" + oneviewlet.getWidth() + "px'></td><td id='addCommentsId' style='' width=''></td><td width='' style='display:none;'><a  style='text-decoration: none;' onclick=\"measureOptions('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td><td style='display:none;'><a   style='text-decoration: none;' onclick=\"taggleNewMeasures('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(prior) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "')\" title='NewMeasureType' href='javascript:void(0)'></a></td></tr></table><table width='" + oneviewlet.getWidth() + "px'  style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/><td id='measureDescId" + oneviewlet.getNoOfViewLets() + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' height='" + oneviewlet.getHeight() + "px'></td>");
                        if (assignedGraphIds != null) {
                            String tdWidth = "0";
                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                if (i > 0) {
                                    tdWidth = "1%";
                                }
                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                            }
                        }
                        measureText.append("<table><tr><td id='measureNavigateIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td></tr><tr><td id='relatedMeasureInfoIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></tr></table></td></tr><tr><td height='" + oneviewlet.getHeight() + "px'></td></tr></table></center>");
                        measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table align='center'><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 50px; height: 50px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "'><font style='color: white;'>" + prefixValue + currFourthVal + "<span style='font-size:9pt;'>" + suffixValue + "</span> </font></td></tr></table></center></div></td></tr></table></td><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                        if (assignedGraphIds != null) {
                            String tdWidth = "0";
                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                if (i > 0) {
                                    tdWidth = "1%";
                                }
                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                            }
                        }
//                    measureText.append("<table><tr><td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='center' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td></tr><tr><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='center' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></tr></table></td></tr><tr><td height='" + oneviewlet.getHeight() + "px'></td></tr></table></center>");

                        measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='center' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                        measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 50px; height: 50px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "'><font style='color: white;'>" + prefixValue + currFourthVal + "<span style='font-size:9pt;'>" + suffixValue + "</span> </font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                        if (assignedGraphIds != null) {
                            String tdWidth = "0";
                            for (int i = 0; i < assignedGraphIds.size(); i++) {
                                if (i > 0) {
                                    tdWidth = "1%";
                                }
                                measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                            }
                        }
                        measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                    }



                    // }

                } else {

                    measureText.append("<center><table align='' style='overflow:auto;' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' style='" + displayTest + "'><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + formatter.format(curval) + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' style='" + newType + "'><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)'><span style='color:" + measurecolor1 + ";font-size:18pt;'>" + formatter.format(curval) + "</span></a></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                    if (assignedGraphIds != null) {
                        String tdWidth = "0";
                        for (int i = 0; i < assignedGraphIds.size(); i++) {
                            if (i > 0) {
                                tdWidth = "1%";
                            }
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }
                    measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='0%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "',''\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                }
            }
        } else {
            finalStringVal.append("No Data");
        }
        finalStringVal.append(measureText.toString());
        finalStringVal.append("</div>");
        //finalStringVal.append("</td>");

        return finalStringVal.toString();
    }
//Surender

    public String getPortalDetailsData(HttpServletRequest request, HttpServletResponse response, HttpSession session, OneViewLetDetails oneviewlet) {

        return null;
    }

    //Surender
    public String getHeadLinehDetailsData(HttpServletRequest request, HttpServletResponse response, HttpSession session, OneViewLetDetails oneviewlet) throws Exception {
        StringBuilder finalStringVal = new StringBuilder();
        String valu = null;
        if (session.getAttribute("MeasureDrillTest") != null) {
            HashMap<String, ArrayList<String>> drillMap = null;
            drillMap = (HashMap<String, ArrayList<String>>) session.getAttribute("MeasureDrillTest");
            Set<String> keys = drillMap.keySet();
            ArrayList<String> values = new ArrayList<String>();
            values.addAll(keys);
            if (values.contains(oneviewlet.getNoOfViewLets())) {
                if (drillMap.get(oneviewlet.getNoOfViewLets()).get(0).equalsIgnoreCase(oneviewlet.getOneviewId())) {
                    valu = drillMap.get(oneviewlet.getNoOfViewLets()).get(1);
                }
            }
        }
        if (oneviewlet.getMeasurDrill() != null) {
            valu = oneviewlet.getMeasurDrill();
        }

        //finalStringVal.append("<td id='" + oneviewlet.getNoOfViewLets() + "' width='" + oneviewlet.getWidth() + "px' style='height:" + oneviewlet.getHeight() + "px;' rowspan='" + oneviewlet.getRowSpan() + "' colspan='" + oneviewlet.getColSpan() + "'>");
        if (request.getParameter("downloaingfdf") == null) {
            finalStringVal.append(new SchedulerDAO().getOneviewHeadLinesHeader(oneviewlet, valu));

        }
        finalStringVal.append("<div id='Dashlets-" + oneviewlet.getNoOfViewLets() + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 100%; margin-left: 10px; margin-right: 10px;overflow:auto;'>");

//                     String result = "";
//                      String divid = "Dashlets-".concat(oneviewlet.getNoOfViewLets());
        String checkedHeadlines = null;
        checkedHeadlines = oneviewlet.getHealinesIds();
        request.setAttribute("checkedHeadlines", checkedHeadlines);
//                     request.setAttribute("divid", divid);
        DataSnapshotDAO snapshotDAO = new DataSnapshotDAO();
        String headlinedetails = snapshotDAO.getReportHeadlines("false", request);
//                     String headlinedetails=snapshotDAO.getReportHeadlines("false", request);
        finalStringVal.append(headlinedetails);
        finalStringVal.append("</div>");
        // finalStringVal.append("</td>");

        return finalStringVal.toString();
    }

    //added by srikanth.p on Jun15 2012
    public String groupMeasureInitialView(Container container, GroupMeassureParams meassureParams) {
        String childTable = "";
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
//        GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
        ArrayList reportQryElementIds = new ArrayList();
        ArrayList reportQryAggregations = new ArrayList();
        DashletDetail detail = collect.getDashletDetail(meassureParams.getDahletId());
        GraphReport graphDetails = (GraphReport) detail.getReportDetails();
        PbDashboardViewerBD viewerBD = new PbDashboardViewerBD();
        PbReturnObject pbretObj = null;
        String groupDispType = meassureParams.getGroupDisplayType();
        PbReturnObject childRetObj = DAO.getChildIds(meassureParams.getMeassureId(), meassureParams.getGroupId());
        List<QueryDetail> queryDetails = new ArrayList<QueryDetail>();
        ArrayList childElementList = new ArrayList();
        String groupId = meassureParams.getGroupId();
        HashMap<String, ArrayList> groupMap = new HashMap<String, ArrayList>();
        if (childRetObj.getRowCount() == 0 || childRetObj == null) {
            return "";
        } else {
            if (groupDispType.equalsIgnoreCase("SWP")) {
                DashboardViewerDAO DVdao = new DashboardViewerDAO();
                PbDashboardViewerBD bd = new PbDashboardViewerBD();
                for (int i = 0; i < childRetObj.getRowCount(); i++) {
                    childElementList.add(childRetObj.getFieldValueString(i, 0));
                }
                groupMap.put(groupId, childElementList);
                meassureParams.setGroupMap(groupMap);
                List<KPIElement> kpiElements = DVdao.getKPIElementsForGroups(childElementList, new HashMap<String, String>());
                meassureParams.setkPIElements(kpiElements);

                bd.setGroupMeasureKPIData(container, detail, kpiElements, userId);

            } else {
                if (childRetObj != null && childRetObj.getRowCount() > 0) {
                    for (int i = 0; i < childRetObj.getRowCount(); i++) {
                        QueryDetail qd = new QueryDetail();
                        qd.setElementId(childRetObj.getFieldValueString(i, 0));
                        qd.setDisplayName(childRetObj.getFieldValueString(i, 1));
                        qd.setRefElementId(childRetObj.getFieldValueString(i, 2));
                        qd.setFolderId(childRetObj.getFieldValueInt(i, 4));
                        qd.setSubFolderId(childRetObj.getFieldValueInt(i, 5));
                        qd.setAggregationType(childRetObj.getFieldValueString(i, 3));
                        qd.setColumnType(childRetObj.getFieldValueString(i, 6));
                        queryDetails.add(qd);
                        reportQryAggregations.add(childRetObj.getFieldValueString(i, 3));
                        reportQryElementIds.add(childRetObj.getFieldValueString(i, 0));
                    }
                }
                graphDetails.setQueryDetails(queryDetails);
                if (graphDetails.isTimeSeries()) {
                    pbretObj = viewerBD.getTimeSeriesData(collect, userId);
                    container.setTimeSeriesRetObj(pbretObj);
                } else {
                    collect.addQryColumns(reportQryElementIds, reportQryAggregations);
                    pbretObj = viewerBD.getDashboardData(container, collect, userId);
                }
            }
            childTable = viewerBD.generateMeassureTable(container, meassureParams);
            return childTable;
        }
    }

    /**
     * added by srikanth.p fro group meassure insights to get measures in the
     * group
     */
    public StringBuilder buildGroupMeassure(HttpServletRequest request, HttpServletResponse response, boolean isFxCharts) {
        StringBuilder initialView = new StringBuilder();
        HashMap<String, ArrayList> rootParentsMap = new HashMap<String, ArrayList>();
        ArrayList rootParentsList = new ArrayList();
        HttpSession session = request.getSession(false);
        GroupMeassureParams grpParams = (GroupMeassureParams) session.getAttribute("GroupParamsMap");
        if (grpParams == null) {
            grpParams = new GroupMeassureParams();
        }
        try {
            String groupId = "";
            String idsParam = request.getParameter("Ids");
            String NamesParam = request.getParameter("Names");
            String[] ids = idsParam.split(",");
            String[] Names = NamesParam.split(",");
            PbReturnObject rootRetObj = new PbReturnObject();
            StringBuilder grpColumns = new StringBuilder();
            StringBuilder grpColumnsNames = new StringBuilder();
            for (int i = 0; i < ids.length; i++) {
                if (ids[i].indexOf("G_") != -1) {
                    groupId = ids[i].substring(2);
                    rootRetObj = DAO.getParents(groupId);
                    for (int j = 0; j < rootRetObj.getRowCount(); j++) {
                        rootParentsList.add(rootRetObj.getFieldValueString(j, 0));
                        grpColumns.append(",").append(rootRetObj.getFieldValueString(j, 0));
                        grpColumnsNames.append(",").append(rootRetObj.getFieldValueString(j, 1));
                    }
                    rootParentsMap.put(groupId, rootParentsList);
                } else {
                    continue;

                }
                grpParams.setGroupMap(rootParentsMap);
            }
            for (int k = 0; k < ids.length; k++) {
                if (ids[k].indexOf("G_") != -1) {
                    continue;
                } else {
                    grpColumns.append(",").append(ids[k]);
                    grpColumnsNames.append(",").append(Names[k]);
                }
            }

            session.setAttribute("GroupParamsMap", grpParams);
            request.setAttribute("groupId", groupId);
            request.setAttribute("grpColumns", grpColumns.substring(1).toString());
            request.setAttribute("grpColumnsNames", grpColumnsNames.substring(1).toString());
            initialView = buildDbrdGraphs(request, response, isFxCharts);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return initialView;
    }
    //Surender

    public String getOneviewKpisData(HttpServletRequest request, HttpServletResponse response, HttpSession session, OneViewLetDetails oneviewlet, OnceViewContainer onecontainer) throws Exception {
        StringBuilder finalStringVal = new StringBuilder();
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        String valu = null;
        if (request.getAttribute("kpiGraph") == null) {
            if (session.getAttribute("MeasureDrillTest") != null) {
                HashMap<String, ArrayList<String>> drillMap = null;
                drillMap = (HashMap<String, ArrayList<String>>) session.getAttribute("MeasureDrillTest");
                Set<String> keys = drillMap.keySet();
                ArrayList<String> values = new ArrayList<String>();
                values.addAll(keys);
                if (values.contains(oneviewlet.getNoOfViewLets())) {
                    if (drillMap.get(oneviewlet.getNoOfViewLets()).get(0).equalsIgnoreCase(oneviewlet.getOneviewId())) {
                        valu = drillMap.get(oneviewlet.getNoOfViewLets()).get(1);
                    }
                }
            }
            if (oneviewlet.getMeasurDrill() != null) {
                valu = oneviewlet.getMeasurDrill();
            }

            //finalStringVal.append("<td id='" + oneviewlet.getNoOfViewLets() + "' width='" + oneviewlet.getWidth() + "px' style='height:" + oneviewlet.getHeight() + "px;' rowspan='" + oneviewlet.getRowSpan() + "' colspan='" + oneviewlet.getColSpan() + "'>");
            if (request.getParameter("downloaingfdf") == null) {
                finalStringVal.append(new SchedulerDAO().getOneviewMeasureHeader(oneviewlet, valu));

            }
            finalStringVal.append("<div id='Dashlets-" + oneviewlet.getNoOfViewLets() + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 100%; margin-left: 10px; margin-right: 10px;overflow:auto;'>");
        }
        HashMap map = null;
        Container container = null;
        String dashBoardId = null;
        String kpiMasterId = null;
        String kpiDrill = null;
        pbDashboardCollection collect = null;
        KPIBuilder kpibuilder = new KPIBuilder();
        String fromDesigner = null;
        String editDbrd = null;
        String result = "";
        PbReportRequestParameter reportReqParams = null;
        HashMap DBKPIHashMap = null;

        if (session != null && session.getAttribute("USERID") != null && session.getAttribute("PROGENTABLES") != null) {
            dashBoardId = oneviewlet.getRepId();
            kpiMasterId = oneviewlet.getKpiMasterId();
            kpiDrill = "N";
            map = (HashMap) session.getAttribute("PROGENTABLES");
            fromDesigner = request.getParameter("fromDesigner");
            editDbrd = request.getParameter("editDbrd");

            boolean createForDesigner = false;
            if (fromDesigner != null && "true".equalsIgnoreCase(fromDesigner)) {
                createForDesigner = true;
            }

            container = (Container) map.get(dashBoardId);
            collect = (pbDashboardCollection) container.getReportCollect();
//            collect.reportIncomingParameters = container.getRepReqParamsHashMap();
            if (oneviewlet.getKpiType() != null && (oneviewlet.getKpiType().equalsIgnoreCase("Basic") || oneviewlet.getKpiType().equalsIgnoreCase("Target") || oneviewlet.getKpiType().equalsIgnoreCase("Standard") || oneviewlet.getKpiType().equalsIgnoreCase("MultiPeriod") || oneviewlet.getKpiType().equalsIgnoreCase("MultiPeriodCurrentPrior") || oneviewlet.getKpiType().equalsIgnoreCase("BasicTarget"))) {
                String userId = (String) session.getAttribute("USERID");
                DBKPIHashMap = container.getDBKPIHashMap();


                collect.setDBKPIHashMap(DBKPIHashMap);
                collect.reportId = dashBoardId;//here reportId is DashBoard Id
                collect.ctxPath = request.getContextPath();
                collect.setServletRequest(request);
                collect.setServletResponse(response);
                collect.setSession(session);
                if (onecontainer.getFilterBusinessRole() != null && !onecontainer.getFilterBusinessRole().equalsIgnoreCase("") && oneviewlet != null && oneviewlet.getRoleId() != null && !oneviewlet.getRoleId().equalsIgnoreCase("") && onecontainer.getFilterBusinessRole().equalsIgnoreCase(oneviewlet.getRoleId())) {
                    if (onecontainer.getReportParameterValues() != null && !onecontainer.getReportParameterValues().isEmpty()) {
                        collect.reportParametersValues = onecontainer.getReportParameterValues();
                    }
                }

                DashletDetail detail = collect.getDashletDetail(oneviewlet.getNoOfViewLets());
                collect.setOneviewCheckForKpis(true);
                collect.setOneViewWidth(Integer.toString(oneviewlet.getWidth()));
                if (!oneviewlet.getKpiType().equalsIgnoreCase("Complexkpi")) {
                    result = kpibuilder.processSingleKpi(container, oneviewlet.getKpiMasterId(), collect.kpiQuery, kpiDrill, oneviewlet.getKpiDashLetNo(), dashBoardId, createForDesigner, collect, userId, editDbrd);
                } else {
                    KPI kpiDetails = (KPI) detail.getReportDetails();
                    List<String> a1 = kpiDetails.getElementIds();
                    String ElemntIds[] = new String[a1.size()];
                    for (int i = 0; i < a1.size(); i++) {
                        ElemntIds[i] = a1.get(i);
                    }
                    request.setAttribute("oneviewTest", true);
                    DashboardTemplateDAO dao = new DashboardTemplateDAO();
                    result = dao.getBuildCreateKPI(ElemntIds, request, dashBoardId);
                }
            } else {
                reportReqParams = new PbReportRequestParameter(request);
                reportReqParams.setParametersHashMap();
                GraphBuilder graphBuilder = new GraphBuilder();
                graphBuilder.setRequest(request);
                graphBuilder.setResponse(response);
                graphBuilder.setFxCharts(false);
                collect.setOneviewCheckForKpis(true);
                collect.setOneViewWidth(Integer.toString(oneviewlet.getWidth()));
                collect.setOneViewHeight(Integer.toString(oneviewlet.getHeight()));

                result = graphBuilder.displayGraphs(container, oneviewlet.getKpiDashLetNo(), request.getContextPath(), createForDesigner, editDbrd);
            }
        }
        String image = null;
//        if(result != ""){
//            int va = result.indexOf("#");
//            int v = result.indexOf("png");
//            image= result.substring(va+1,v+3);
//        }
//
//        if(request.getAttribute("kpiGraph")!=null){
//         return image;
//        }
        if (result != null) {
            finalStringVal.append(result);
            finalStringVal.append("</div>");
            finalStringVal.append("</td>");
            return finalStringVal.toString();
        } else {
            if (result != "") {
                finalStringVal.append(result);

                onecontainer = new OnceViewContainer();
                ReportTemplateDAO reportTemplateDA = new ReportTemplateDAO();
                OneViewLetDetails detail = null;
                String fileName = reportTemplateDA.getOneviewFileName(oneviewlet.getOneviewId());
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(oneviewlet.getNoOfViewLets()));

                detail.kpiGraphDetails.put(oneviewlet.getNoOfViewLets(), image);
//             reportTemplateDAO.updateOneviewData(onecontainer,oneViewIdValue);
                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();
            } else {
                finalStringVal.append("No Data");
            }
            finalStringVal.append("</div>");
            //finalStringVal.append("</td>");
//        finalStringVal.append(result);

            return finalStringVal.toString();
        }
    }

    public String getOLAPGraphForOneView(String reportId, HttpServletRequest request, HttpServletResponse response, HttpSession session, int graphNum, boolean viewBy) throws Exception {
        StringBuffer graphString = new StringBuffer();
        HashMap ParametersMap = null;
        HashMap map = null;
        Container container = null;
        PbReportCollection collect = null;
        ArrayList Parameters = new ArrayList();
        ArrayList REP = new ArrayList();
        ArrayList ParameterNames = new ArrayList();
        String[] viewByDispNames = new String[0];
        PbReportViewerBD repViewBD = new PbReportViewerBD();
        String[] ViewBys = null;
        ProgenChartDisplay[] pcharts = null;
        String userId = String.valueOf(session.getAttribute("USERID"));
        PbReportViewerBD reportViewerBD = new PbReportViewerBD();
        String strURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        strURL = strURL + "reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&action=open";
        request.setAttribute("url", strURL);
        request.setAttribute("REPORTID", reportId);
        String viewby = request.getParameter("viewById");
        String drillValue = request.getParameter("drillValue");
        String width = request.getParameter("width");
        String height = request.getParameter("height");
        String timeViewbyType = request.getParameter("viewbyType");
        String islocaldrill = request.getParameter("islocaldrill");
        map = (HashMap) session.getAttribute("PROGENTABLES");
        if (map != null) {
            container = (Container) map.get(reportId);
            if (container != null) {
                collect = container.getReportCollect();
                if (request.getParameter("viewById") != null) {
//                    String viewByid=request.getParameter("viewById");
                    ArrayList rowViewbyCols = new ArrayList();
                    rowViewbyCols.add(viewby);
                    /*
                     * collect.reportRowViewbyValues =rowViewbyCols;
                     */
                }
            }
        }
        String isRepDate = request.getParameter("isRepDate");
        String timeDetails = request.getParameter("timeDetails");
        ArrayList timeDeArrayList = new ArrayList();
        if (timeDetails != null) {
            if (isRepDate != null && isRepDate.equalsIgnoreCase("false")) {
                if (map != null) {
                    container = (Container) map.get(reportId);
                    if (container != null) {
                        collect = container.getReportCollect();
                        if (timeDetails != null) {
                            if (collect.reportRowViewbyValues.get(0).equalsIgnoreCase("TIME") && drillValue != null) {
                                String temp = timeDetails.substring(timeDetails.indexOf("[") + 1, timeDetails.indexOf("]"));
                                String[] details = temp.split(",");
//                    if(timeViewbyType!=null){
//                        details[3]=timeViewbyType.trim();
//                    }
                                if (details[3].trim().equalsIgnoreCase("Quarter")) {
                                    details[3] = "Qtr";
                                }

                                for (int i = 0; i < details.length; i++) {
                                    if (i == 3) {
                                        String drillto = request.getParameter("CBO_PRG_PERIOD_TYPE");
                                        if (drillto != null) {
                                            timeDeArrayList.add(drillto);
                                        } else {
                                            timeDeArrayList.add(details[i].trim());
                                        }
                                    } else {
                                        timeDeArrayList.add(details[i].trim());
                                    }
                                }
                                request.setAttribute("OneviewGraphTimeDetails", timeDeArrayList);
                            } else {
                                String temp = timeDetails.substring(timeDetails.indexOf("[") + 1, timeDetails.indexOf("]"));
                                String[] details = temp.split(",");
//                    if(timeViewbyType!=null){
//                        details[3]=timeViewbyType.trim();
//                    }
                                if (details[3].trim().equalsIgnoreCase("Quarter")) {
                                    details[3] = "Qtr";
                                }
                                for (int i = 0; i < details.length; i++) {
                                    timeDeArrayList.add(details[i].trim());
                                }
                                request.setAttribute("OneviewGraphTimeDetails", timeDeArrayList);
                            }
                        }
                    }
                } else {
                    String temp = timeDetails.substring(timeDetails.indexOf("[") + 1, timeDetails.indexOf("]"));
                    String[] details = temp.split(",");
                    if (details[3].trim().equalsIgnoreCase("Quarter")) {
                        details[3] = "Qtr";
                    }
                    for (int i = 0; i < details.length; i++) {
                        timeDeArrayList.add(details[i].trim());
                    }
                    request.setAttribute("OneviewGraphTimeDetails", timeDeArrayList);

                }

            } else {
                if (isRepDate != null && isRepDate.equalsIgnoreCase("true")) {
                    request.setAttribute("OneviewgraphDate", timeDetails);
                } else {
                    if (map != null) {
                        container = (Container) map.get(reportId);
                        if (container != null) {
                            collect = container.getReportCollect();
                            if (timeDetails != null) {
                                if (collect.reportRowViewbyValues.get(0).equalsIgnoreCase("TIME") && drillValue != null) {
                                    String temp = timeDetails.substring(timeDetails.indexOf("[") + 1, timeDetails.indexOf("]"));
                                    String[] details = temp.split(",");
//                  if(timeViewbyType!=null){
//                        details[3]=timeViewbyType.trim();
//                    }
                                    if (details[3].trim().equalsIgnoreCase("Quarter")) {
                                        details[3] = "Qtr";
                                    }

                                    for (int i = 0; i < details.length; i++) {
                                        if (i == 3) {
                                            String drillto = request.getParameter("CBO_PRG_PERIOD_TYPE");
                                            if (drillto != null) {
                                                timeDeArrayList.add(drillto);
                                            } else {
                                                timeDeArrayList.add(details[i].trim());
                                            }
                                        } else {
                                            timeDeArrayList.add(details[i].trim());
                                        }
                                    }
                                    request.setAttribute("OneviewGraphTimeDetails", timeDeArrayList);
                                } else {
                                    String temp = timeDetails.substring(timeDetails.indexOf("[") + 1, timeDetails.indexOf("]"));
                                    String[] details = temp.split(",");
//                    if(timeViewbyType!=null){
//                        details[3]=timeViewbyType.trim();
//                    }
                                    if (details[3].trim().equalsIgnoreCase("Quarter")) {
                                        details[3] = "Qtr";
                                    }
                                    for (int i = 0; i < details.length; i++) {
                                        timeDeArrayList.add(details[i].trim());
                                    }
                                    request.setAttribute("OneviewGraphTimeDetails", timeDeArrayList);
                                }
                            }
                        }
                    } else {
                        String temp = timeDetails.substring(timeDetails.indexOf("[") + 1, timeDetails.indexOf("]"));
                        String[] details = temp.split(",");
                        if (details[3].trim().equalsIgnoreCase("Quarter")) {
                            details[3] = "Qtr";
                        }
                        for (int i = 0; i < details.length; i++) {
                            timeDeArrayList.add(details[i].trim());
                        }
                        request.setAttribute("OneviewGraphTimeDetails", timeDeArrayList);

                    }
                }
            }
        }

        if (request.getParameter("viewById") != null || container == null || request.getParameter("drillValue") != null || request.getParameter("isOLAPInitial") != null && request.getParameter("isOLAPInitial").equalsIgnoreCase("true") || request.getParameter("viewbyType") != null) {
            reportViewerBD.prepareReport(reportId, userId, request, response,false);
        }
        map = (HashMap) session.getAttribute("PROGENTABLES");
        if (map != null) {
            container = (Container) map.get(reportId);
            collect = container.getReportCollect();
            viewby = collect.reportRowViewbyValues.get(0);

        }

//            PbGraphDisplay GraphDisplay = new PbGraphDisplay();

        ArrayList<String> sortCols = null;
        char[] sortTypes = null;//ArrayList sortTypes = null;
        char[] sortDataTypes = null;

        if (viewby != null && viewby.equalsIgnoreCase("TIME")) {
        } else {
            sortCols = container.getSortColumns();
            String sort = "";
            if (sortCols != null && !sortCols.isEmpty()) {
                sortCols = container.getSortColumns();
                if (!sortCols.isEmpty()) {
                    sortTypes = container.getSortTypes();
                    sortDataTypes = container.getSortDataTypes();
                    ProgenDataSet retObj = container.getRetObj();
                    ArrayList rowSequence = new ArrayList();
                    if (container.isTopBottomSet()) {
                        int topbottomCount = container.getTopBottomCount();
                        if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS)) {
                            sort = "1";
//                         container.setSortColumn(sortCols, sort);
                            if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                            } else {
                                rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                            }
//                              retObj.setViewSequence(rowSequence);
                        } else if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS)) {
                            sort = "0";
//                            container.setSortColumn(sortColumn, sort);
                            if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                            } else {
                                rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                            }

                        }
                    } else {
                        rowSequence = container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes);
                    }
                    retObj.setViewSequence(rowSequence);




//                container.getRetObj().setViewSequence(container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes));
//                rowCount = rowSequence.size();
                } else {
                    ArrayList tableMeasure = container.getTableMeasure();
                    if (tableMeasure != null) {
                        ArrayList sortColumn = new ArrayList();
                        sortColumn.add("A_" + tableMeasure.get(0).toString());
                        char[] sortType = new char[1];//new String[1];
                        sortType[0] = ' ';
                        char[] sortdataType = new char[1];
                        sortdataType[0] = 'N';
                        container.getRetObj().setViewSequence(container.getRetObj().sortDataSet(sortColumn, sortType, sortdataType));
                    }
                }
            }
        }

//              if(container.getOneviewGraphTimedetails()!=null && !container.getOneviewGraphTimedetails().isEmpty()){
//                    container.getOneviewGraphTimedetails().clear();
//                }
        HashMap GraphTypesHashMap = null;
        HashMap GraphSizesDtlsHashMap = null;
//                             String ProGenImgPath = getServletContext().getRealPath("/") + "tempFolder/";
//                             String[] ViewBys = null;

        GraphSizesDtlsHashMap = (HashMap) session.getAttribute("GraphSizesDtlsHashMap");
        PbGraphDisplay GraphDisplay = new PbGraphDisplay();
        ArrayList grpDetails = new ArrayList();
//                                GraphDisplay.olapFun=olapRef;
        GraphDisplay.setGraphSizesDtlsHashMap(GraphSizesDtlsHashMap);
//                                GraphDisplay.setProGenImgPath(ProGenImgPath);
        ProgenDataSet recordsRetObj = container.getRetObj();
        GraphDisplay.setCurrentDispRetObjRecords(recordsRetObj);//works 4 fx
        // GraphDisplay.setCurrentDispRetObjRecords(container.getDisplayedSetRetObj());//works 4 jfree
        //   GraphDisplay.setCurrentDispRecordsRetObjWithGT(container.getDisplayedSetRetObjWithGT());
        GraphDisplay.setAllDispRecordsRetObj(recordsRetObj);
        GraphDisplay.setNoOfDays(container.getNoOfDays());
        GraphDisplay.setColumnAverages(recordsRetObj.getColumnAverages());
        GraphDisplay.setColumnGrandTotals(recordsRetObj.getColumnGrandTotals());
        GraphDisplay.setColumnOverAllMinimums(recordsRetObj.getColumnOverAllMinimums());
        GraphDisplay.setColumnOverAllMaximums(recordsRetObj.getColumnOverAllMaximums());
        ArrayList viewByColNames = container.getViewByColNames();
        ArrayList viewByElementId = container.getViewByElementIds();
        ViewBys = (String[]) viewByElementId.toArray(new String[0]);
        for (int viewIndex = 0; viewIndex < ViewBys.length; viewIndex++) {
            if (ViewBys[viewIndex].equalsIgnoreCase("Time")) {
                ViewBys[viewIndex] = ViewBys[viewIndex];
            } else {
                if (ViewBys[viewIndex].contains("A_")) {
                    ViewBys[viewIndex] = ViewBys[viewIndex];
                } else {
                    ViewBys[viewIndex] = "A_" + ViewBys[viewIndex];
                }
            }
        }
        GraphDisplay.setViewByColNames((String[]) viewByColNames.toArray(new String[0]));
        GraphDisplay.setViewByElementIds(ViewBys);
        GraphDisplay.setCtxPath(request.getContextPath());
        GraphDisplay.setTimelevel(container.getTimeLevel());

        ArrayList links = container.getLinks();
        if (links != null && links.size() != 0) {
            GraphDisplay.setJscal(String.valueOf(links.get(0)));
        } else {
            GraphDisplay.setJscal("reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&");
        }
        GraphDisplay.setSession(request.getSession(false));
        GraphDisplay.setResponse(response);
        GraphDisplay.setOut(response.getWriter());
        GraphDisplay.setReportId(reportId);

        HashMap[] graphMapDetails = container.getGraphMapDetails();

        GraphDisplay.setGraphHashMap(container.getGraphHashMap());
        GraphDisplay.setGraphMapDetails(graphMapDetails);
        GraphDisplay.setIsCrosstab(container.isReportCrosstab());
        GraphDisplay.setSortColumns(container.getSortColumns());
        GraphDisplay.graphWidth = "700";
        GraphDisplay.graphHeight = "320";
        GraphDisplay.fromOneview = true;
//                                if (!viewBy) {
//                                   GraphDisplay.graphWidth=request.getParameter("width").toString();
//                                   GraphDisplay.graphHeight=request.getParameter("height").toString();
//                                 } else {
//                                       GraphDisplay.graphWidth=request.getAttribute("width").toString();
//                                       GraphDisplay.graphHeight=request.getAttribute("height").toString();
//                                     }
//                                 grpDetails = GraphDisplay.getGraphHeaders(container.getNoOfDays());

        if (Integer.parseInt(container.getColumnViewByCount()) != 0) {
            grpDetails = GraphDisplay.get2dGraphHeaders(container);
        } else {
            grpDetails = GraphDisplay.getGraphHeaders(container.getNoOfDays(), container);
        }

        GraphDisplay.fromOneview = false;
//                                  GraphDisplay.graphWidth=null;
//                                  GraphDisplay.graphHeight=null;
        container.setGraphHashMap(GraphDisplay.getGraphHashMap());




        String name = request.getParameter("name");
        if (name == null || name.equalsIgnoreCase("null")) {
            if (request.getAttribute("graphName") != null) {
                name = request.getAttribute("graphName").toString();
            }
        }
        String graphId = request.getParameter("graphId");
        if (graphId == null || graphId.equalsIgnoreCase("null")) {
            if (request.getAttribute("graphId") != null) {
                graphId = request.getAttribute("graphId").toString();
            }
        }
//        int height=oneviewlet.getHeight();
//        int width=oneviewlet.getWidth();
//        if(height ==0 || width ==0){
//            if(request.getAttribute("height")!= null ){
//                height=Integer.parseInt(request.getAttribute("height").toString());
//              }
//            if(request.getAttribute("width")!= null){
//                width=Integer.parseInt(request.getAttribute("width").toString());
//              }
//           }
//          grpDetails = GraphDisplay.getGraphHeaders(container.getNoOfDays());
//          container.setGraphHashMap(GraphDisplay.getGraphHashMap());
        String graphType = "JQPlot";
        String graphTypeName = null;
        String jqgraphId = "";
        if (graphType.equalsIgnoreCase("JQPlot")) {
//                                      if (grpDetails != null && !grpDetails.isEmpty()) {
//                                        pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
//                                        if (pcharts != null && pcharts.length != 0) {
//                                            if(graphNum==0)
//                                            graphTypeName=pcharts[0].getChartType();
//                                            else
//                                            graphTypeName=pcharts[1].getChartType();
//                                    }
//                                }
            JqplotGraphProperty graphproperty = new JqplotGraphProperty();
            PbReportViewerDAO reportViewerdao = new PbReportViewerDAO();
            graphproperty = reportViewerdao.getJqGraphDetails(graphId);
            HashMap jqpToJfNameMap = (HashMap) session.getAttribute("JqpToJfNameMap");
            HashMap jqpMap = (HashMap) session.getAttribute("JqpMap");
            graphTypeName = graphproperty.getGraphTypename();

            if (graphTypeName == null || graphTypeName.equalsIgnoreCase("") || graphTypeName.equalsIgnoreCase("null")) {
                graphTypeName = "Bar-Vertical";
                jqgraphId = "5502";
            }
//                                      }else{
//                                          graphTypeName="Bar-Vertical";
//                                          graphId="5502";
//
//                                      }

//                               String[] jqgrapharray={"Area","Bar-Horizontal","Bar-Vertical","DualAxis(Bar-Line)","Donut-Single","Donut-Double","Funnel","Line","Line(Smooth)","Line(Dashed)","Overlaid(Bar-Line)","Pie","Pie-Empty","StackedArea","StackedBar(V)","StackedBar(H)","Waterfall"};
//                              String[] jqgraphIds={"5500","5501","5502","5503","5504","5505","5506","5507","5508","5509","5510","5511","5512","5513","5514","5515","5516"};
//
//                              ArrayList graphlist=new ArrayList();
//                              int matchedCount=0;
//                                for(int i=0;i<jqgrapharray.length;i++){
//                                    if(graphTypeName.equalsIgnoreCase(jqgrapharray[i])){
//                                        graphTypeName=jqgrapharray[i];
//                                        jqgraphId=jqgraphIds[i];
//                                        matchedCount++;
//                                    }
//                                }
//                              if(matchedCount ==0){
//                                  graphTypeName="Bar-Vertical";
//                              }

            ProGenJqPlotChartTypes jqplotcontainer = new ProGenJqPlotChartTypes();
            ProgenJqplotGraphBD jqplotgraphbd = new ProgenJqplotGraphBD();
            String chartId = "chart_" + graphNum + "_" + reportId;
            jqplotgraphbd.chartId = chartId;
            float divWidth = Float.parseFloat(width) - (Float.parseFloat(width) / 100) * 20;
            float divHeight = Float.parseFloat(height) - (Float.parseFloat(height) / 100) * 10;
            graphString.append("<div id='" + chartId + "' style=\"width:" + divWidth + "px;height:" + divHeight + "px;\" align='center'></div>");
            graphString.append("<script>");
            String selectgraph = graphproperty.getSlectedGraphType(graphId);
            jqgraphId = graphproperty.getGraphTypeId();
            jqplotgraphbd.JqplotGraphProperty = graphproperty;
            if (graphproperty.getTableColumns() != null && graphproperty.getTableColumns().length > 0) {
                jqplotgraphbd.tablecols = graphproperty.getTableColumns();
            }

            jqplotgraphbd.setIsFromOneView(true);
            if (viewby != null && viewby.equalsIgnoreCase("TIME")) {
                graphString.append(jqplotgraphbd.prepareJqplotGraph(reportId, jqgraphId, "Line", jqplotcontainer, request, null, selectgraph, graphId));
            } else {
                graphString.append(jqplotgraphbd.prepareJqplotGraph(reportId, jqgraphId, graphTypeName, jqplotcontainer, request, null, selectgraph, graphId));
            }

//                                 graphString.append(jqplotgraphbd.prepareJqplotGraph(reportId,jqgraphId, graphTypeName, jqplotcontainer,request,null,"jq",graphId,null,null,null));
            graphString.append("</script>");
//                                      

        } else {

//                if (grpDetails != null && !grpDetails.isEmpty()) {
            pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
            if (pcharts != null && pcharts.length != 0) {
                if (graphNum == 0) {
                    graphString.append(pcharts[0].chartDisplay);
                } else {
                    graphString.append(pcharts[1].chartDisplay);
                }
            }
        }
//                                  return graphString.toString();

        GraphDisplay.fromOneview = false;
        return graphString.toString();
    }

    public String prepareMeterGraph(double sRange, double fB, double sB, double eRange, double deviation, String dashid) {
        StringBuffer metergraphbuffer = new StringBuffer();
        //Gson json = new Gson();
        // metergraphbuffer.append("<div id='"+dashid+"' style=\"width:600px; height:280px;\" align=\"left\" possition=\"\"></div><br>;");
        metergraphbuffer.append("<script >");
        metergraphbuffer.append(" var s1 =[" + deviation + "];");
        metergraphbuffer.append(" var plot4 = $.jqplot('chart-" + dashid + "',[s1],{");
        metergraphbuffer.append("seriesDefaults: {");
        metergraphbuffer.append(" renderer: $.jqplot.MeterGaugeRenderer,");
        metergraphbuffer.append("rendererOptions: {");
        metergraphbuffer.append("min:" + sRange + ",");
        metergraphbuffer.append("max:" + eRange + ",");
        metergraphbuffer.append("intervals:[" + fB + "," + sB + "," + eRange + "],");
        metergraphbuffer.append("intervalColors:['#FF0000', '#FFA500', '#006400']");
        metergraphbuffer.append(" }");
        metergraphbuffer.append(" }");
        //metergraphbuffer.append("}");
        metergraphbuffer.append("});");
        metergraphbuffer.append("</script >");
//       HashMap map=new HashMap();
//       map.put("meterchart", metergraphbuffer.toString());
//     String  jsonString = json.toJson(map);


        return metergraphbuffer.toString();
    }

    public void getOLAPTableForOneView(String reportId, HttpServletRequest request, HttpServletResponse response, HttpSession session, int graphNum, boolean viewBy) {
        StringBuffer graphString = new StringBuffer();
        HashMap ParametersMap = null;
        HashMap map = null;
        Container container = null;
        ArrayList Parameters = new ArrayList();
        ArrayList REP = new ArrayList();
        ArrayList ParameterNames = new ArrayList();
        String[] viewByDispNames = new String[0];
        PbReportViewerBD repViewBD = new PbReportViewerBD();
        String[] ViewBys = null;
        ProgenChartDisplay[] pcharts = null;
        String userId = String.valueOf(session.getAttribute("USERID"));
        PbReportViewerBD reportViewerBD = new PbReportViewerBD();
        String strURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        strURL = strURL + "reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&action=open";
        request.setAttribute("url", strURL);
        request.setAttribute("REPORTID", reportId);

        String width = request.getParameter("width");
        String height = request.getParameter("height");

        map = (HashMap) session.getAttribute("PROGENTABLES");
        if (map != null) {
            container = (Container) map.get(reportId);
            if (container != null) {
                PbReportCollection collect = container.getReportCollect();
                if (request.getParameter("viewById") != null) {
                    String viewByid = request.getParameter("viewById");
                    ArrayList rowViewbyCols = new ArrayList();
                    rowViewbyCols.add(viewByid);
                    collect.reportRowViewbyValues = rowViewbyCols;
                }
            }
        }
        String isRepDate = request.getParameter("isRepDate");
        String timeDetails = request.getParameter("timeDetails");
        ArrayList timeDeArrayList = new ArrayList();
        if (isRepDate != null && isRepDate.equalsIgnoreCase("true")) {
            request.setAttribute("OneviewgraphDate", timeDetails);

        } else {
            if (isRepDate.equalsIgnoreCase("false") && timeDetails != null) {
                String temp = timeDetails.substring(timeDetails.indexOf("[") + 1, timeDetails.indexOf("]"));
                String[] details = temp.split(",");
                for (int i = 0; i < details.length; i++) {
                    timeDeArrayList.add(details[i].trim());
                }
                request.setAttribute("OneviewGraphTimeDetails", timeDeArrayList);
            }
        }
        try {
            reportViewerBD.prepareReport(reportId, userId, request, response,false);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        map = (HashMap) session.getAttribute("PROGENTABLES");
        if (map != null) {
            container = (Container) map.get(reportId);
        }
        ArrayList<String> sortCols = null;
        char[] sortTypes = null;//ArrayList sortTypes = null;
        char[] sortDataTypes = null;
        sortCols = container.getSortColumns();
        if (sortCols != null) {
            sortCols = container.getSortColumns();
            if (!sortCols.isEmpty()) {
                sortTypes = container.getSortTypes();
                sortDataTypes = container.getSortDataTypes();
//                rowSequence = container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes);
                container.getRetObj().setViewSequence(container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes));
//                rowCount = rowSequence.size();
            }
        }
        container.setAdhocDrillType("drilldown");
//        int fromRow = 0;
//        int toRow = 0;
//        PbReportCollection collect = container.getReportCollect();
//        DataFacade facade = new DataFacade(container);
//        facade.setUserId(userId);
//        facade.setCtxPath(collect.ctxPath);
//        TableBuilder tableBldr = new RowViewTableBuilder(facade);
//        if (fromRow == 0 && toRow == 0) {
//            tableBldr.setFromAndToRow(0, container.getRetObj().getViewSequence().size());
//        } else {
//            tableBldr.setFromAndToRow(fromRow, toRow);
//        }
//         StringBuilder htmlBuffer = new StringBuilder();
//         TableDisplay displayHelper = null;
//         TableDisplay menuHelper = null;
//         TableDisplay searchHelper = null;
//         TableDisplay bodyHelper = null;
//         TableDisplay subTotalHelper = null;
//         displayHelper = new TableHeaderDisplay(tableBldr);
//         menuHelper = new TableMenuDisplay(tableBldr);
//         searchHelper = new TableSearchDisplay(tableBldr);
//         bodyHelper = new TableBodyDisplay(tableBldr);
//         subTotalHelper = new TableSubtotalDisplay(tableBldr);
//         displayHelper.setNext(menuHelper).setNext(searchHelper).setNext(bodyHelper).setNext(subTotalHelper);
//         htmlBuffer.append(displayHelper.generateOutputHTML());
//         htmlBuffer.append("</Table>");
//         htmlBuffer.append("</div>");
//
//         return htmlBuffer.toString();
    }

    public String getComplexKpisData(HttpServletRequest request, HttpServletResponse response, HttpSession session, OneViewLetDetails oneviewlet, String oneviewID, int viewLetId) throws FileNotFoundException, IOException, BadElementException, Exception {

        String valu = null;
        StringBuilder finalStringVal = new StringBuilder();
        if (session.getAttribute("MeasureDrillTest") != null) {
            HashMap<String, ArrayList<String>> drillMap = null;
            drillMap = (HashMap<String, ArrayList<String>>) session.getAttribute("MeasureDrillTest");
            Set<String> keys = drillMap.keySet();
            ArrayList<String> values = new ArrayList<String>();
            values.addAll(keys);
            if (values.contains(oneviewlet.getNoOfViewLets())) {
                if (drillMap.get(oneviewlet.getNoOfViewLets()).get(0).equalsIgnoreCase(oneviewlet.getOneviewId())) {
                    valu = drillMap.get(oneviewlet.getNoOfViewLets()).get(1);
                }
            }
        }
        if (oneviewlet.getMeasurDrill() != null) {
            valu = oneviewlet.getMeasurDrill();
        }
        String val1 = request.getParameter("downloaingfdf");

//        finalStringVal.append("<td id='" + oneviewlet.getNoOfViewLets() + "' width='" + oneviewlet.getWidth() + "px' style='height:" + oneviewlet.getHeight() + "px;' rowspan='" + oneviewlet.getRowSpan() + "' colspan='" + oneviewlet.getColSpan() + "'>");
//        if (request.getParameter("downloaingfdf") == null) {
//            finalStringVal.append(new SchedulerDAO().getOneviewComplexKpiHeader(oneviewlet,valu));
//
//        }
//        finalStringVal.append("<div id='Dashlets-" + oneviewlet.getNoOfViewLets() + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 100%; margin-left: 10px; margin-right: 10px;overflow:auto;'>");


        List<String> tiemdetails = new ArrayList<String>();
        tiemdetails = (List<String>) request.getAttribute("OneviewTiemDetails");
        String[] kpiArray = oneviewlet.getComplexKpiMeasureId();
        String kpiName = oneviewlet.getComplexMeasureName();
        DashboardTemplateDAO templateDAO = new DashboardTemplateDAO();
        finalStringVal.append(templateDAO.getBuildOneviewComplexKPIData(kpiArray, request, oneviewID, String.valueOf(viewLetId), String.valueOf(oneviewlet.getHeight()), String.valueOf(oneviewlet.getWidth()), oneviewlet, valu, val1));
        return finalStringVal.toString();
    }

    public String getOneviewRegDate(HttpServletRequest request, HttpServletResponse response, HttpSession session, OnceViewContainer onecontainer, OneViewLetDetails detail, String regDate) throws Exception {

        StringBuilder dateString = new StringBuilder();
        String frmDate = "";
        String toDate = "";
        String curval = "";
        PbReturnObject pbretObjTime = null;
        List<String> measureIdVal = new ArrayList<String>();
        DashboardViewerDAO dao = new DashboardViewerDAO();

        ArrayList dateArrList = new ArrayList();
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        String busrole = detail.getRoleId();
        String measueId = detail.getPrevMeasId();
        String userId = String.valueOf(session.getAttribute("USERID"));
        measureIdVal.add(measueId);
        List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal, new HashMap<String, String>());
        if (kpiElements != null) {
            for (KPIElement elem : kpiElements) {
                if (elem.getElementName() != null) {
                    QueryCols.add(elem.getElementId());
                }
                QueryAggs.add(elem.getAggregationType());
            }
        }
        if (!String.valueOf(QueryAggs.get(0)).equalsIgnoreCase("null")) {
            if (onecontainer.timedetails != null && !onecontainer.timedetails.isEmpty()) {
                request.setAttribute("OneviewTiemDetails", onecontainer.timedetails);
            }
            pbretObjTime = dao.getReturnObjectForOneView(QueryCols, QueryAggs, userId, busrole, request, onecontainer, detail);
        }
        if (onecontainer.timeHashMap.get("PR_DAY_DENOM") != null) {
            dateArrList = (ArrayList) onecontainer.timeHashMap.get("PR_DAY_DENOM");
            if (dateArrList.get(0) != "") {
                String[] frmDateArray = dateArrList.get(0).toString().split("/");

                String[] toDateArray = dateArrList.get(1).toString().split(" ")[0].split("/");
                frmDate = frmDateArray[1] + "/" + frmDateArray[0] + "/" + frmDateArray[2];
                toDate = toDateArray[1] + "/" + toDateArray[0] + "/" + toDateArray[2];
            }
        }
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yy");
        Date date = format1.parse(regDate);
        Date fromDate = format1.parse(frmDate);
        regDate = format2.format(date);
        frmDate = format2.format(fromDate);
        Date tooDate = format1.parse(toDate);
        toDate = format2.format(tooDate);
        // dateString.append("<td id='" + detail.getNoOfViewLets() + "' width='" + detail.getWidth() + "px' style='height:" + detail.getHeight() + "px;' rowspan='" + detail.getRowSpan() + "' colspan='" + detail.getColSpan() + "'>");

        dateString.append("<table style='margin-left: 10px;width:" + detail.getWidth() + "px;'>");
        dateString.append("<tr >");

        String reportName = detail.getRepName();
        dateString.append("<td id=\"Dashlets" + detail.getNoOfViewLets() + "\" style='font-size:12pt;color:#000000;white-space:nowrap;'>" + reportName + "</td>");



        dateString.append("<td id=\"optionId" + detail.getNoOfViewLets() + "\" style='width:1%;align:right;'>");
        dateString.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-triangle-2-n-s\" onclick=\"selectforReadd(" + detail.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Region Options\"></a>");

        dateString.append("<div id=\"reigonOptionsDivId" + detail.getNoOfViewLets() + "\" style='display:none;width:120px;height:auto;background-color:white;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;' calss=\"overlapDiv\">");
        dateString.append("<table border='0' align='left' >");
        dateString.append("<tr><td>");

        dateString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"renameRegion('Dashlets-" + detail.getNoOfViewLets() + "'," + detail.getNoOfViewLets() + ",'Dashlets" + detail.getNoOfViewLets() + "','" + detail.getOneviewId() + "')\" >Rename</a></td></tr></table>");
        dateString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"drillToReport('report','" + detail.getRepName() + "','" + detail.getRoleId() + "','" + detail.getRepId() + "','" + detail.getNoOfViewLets() + "','" + detail.getOneviewId() + "','" + detail.getReptype() + "')\"  >Drill To Report</a></td></tr></table>");
        dateString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"drillToReport('dashboard','" + detail.getRepName() + "','" + detail.getRoleId() + "','" + detail.getRepId() + "','" + detail.getNoOfViewLets() + "','" + detail.getOneviewId() + "','" + detail.getReptype() + "')\" >Drill To Dashboard</a></td></tr></table>");
        dateString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"measureOptions('" + detail.getNoOfViewLets() + "','" + detail.getOneviewId() + "','" + curval + "','','" + detail.getRepId() + "','" + detail.getRepName() + "','')\" title='MeasuresOptions' href='javascript:void(0)'>Measure Option</a></td></tr></table>");
        dateString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"taggleNewMeasures('" + detail.getNoOfViewLets() + "','" + detail.getOneviewId() + "','" + curval + "','','" + detail.getRepId() + "','" + detail.getRepName() + "')\" title='NewMeasureType' href='javascript:void(0)'>Toggle Display</a></td></tr></table>");
        dateString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"measureTrendGraph('" + detail.getRepId() + "','" + detail.getRepName() + "','" + detail.getRoleId() + "','" + detail.getHeight() + "','" + detail.getWidth() + "','" + detail.getNoOfViewLets() + "','" + detail.getOneviewId() + "')\" title='MeasureTrend' href='javascript:void(0)'>Trend</a></td></tr></table>");
//            dateString.append("<table><tr><td><a  style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;'  onclick=\"measureComments('" + detail.getNoOfViewLets() + "','" + detail.getOneviewId() + "')\" href='javascript:void(0)' title='Add/View Comments' >Comments</a></td></tr></table>");

        dateString.append("</td></tr>");
        dateString.append("</table>");
        dateString.append("</div>");
        dateString.append("</td>");
        dateString.append("<td id=\"optionIds" + detail.getNoOfViewLets() + "\" style='width:1%;align:right;'>");
        dateString.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-plusthick\" onclick=\"selectReadd(" + detail.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Re Add Onevielet\"></a>");
//display:none; width:100px; height:100px; background-color:#ffffff; overflow:auto;  position:absolute; text-align:left; border:1px solid #000000; border-top-width: 0px; z-index:1002;
        dateString.append("<div id=\"readdDivId" + detail.getNoOfViewLets() + "\" style='display:none; width:90px; height:100px; background-color:#ffffff; overflow:auto;  position:absolute; text-align:left; border:1px solid #000000; border-top-width: 0px; z-index:1002;'>");
        dateString.append("<table border='0' align='left' >");
        dateString.append("<tr><td>");

        dateString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('report','Dashlets-" + detail.getNoOfViewLets() + "'," + detail.getNoOfViewLets() + "," + detail.getWidth() + "," + detail.getHeight() + ",'Dashlets" + detail.getNoOfViewLets() + "','GrpTyp" + detail.getNoOfViewLets() + "','" + detail.getOneviewId() + "')\" >Reports</a></td></tr></table>");
        dateString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('measures','Dashlets-" + detail.getNoOfViewLets() + "'," + detail.getNoOfViewLets() + "," + detail.getWidth() + "," + detail.getHeight() + ",'Dashlets" + detail.getNoOfViewLets() + "','GrpTyp" + detail.getNoOfViewLets() + "','" + detail.getOneviewId() + "')\" >Measures</a></td></tr></table>");

        dateString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('dashboard','Dashlets-" + detail.getNoOfViewLets() + "'," + detail.getNoOfViewLets() + "," + detail.getWidth() + "," + detail.getHeight() + ",'Dashlets" + detail.getNoOfViewLets() + "','GrpTyp" + detail.getNoOfViewLets() + "','" + detail.getOneviewId() + "')\" >KPIs</a></td></tr></table>");
        dateString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('headline','Dashlets-" + detail.getNoOfViewLets() + "'," + detail.getNoOfViewLets() + "," + detail.getWidth() + "," + detail.getHeight() + ",'Dashlets" + detail.getNoOfViewLets() + "','GrpTyp" + detail.getNoOfViewLets() + "','" + detail.getOneviewId() + "')\" >Headlines</a></td></tr></table>");
        dateString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('complexkpi','Dashlets-" + detail.getNoOfViewLets() + "'," + detail.getNoOfViewLets() + "," + detail.getWidth() + "," + detail.getHeight() + ",'Dashlets" + detail.getNoOfViewLets() + "','GrpTyp" + detail.getNoOfViewLets() + "','" + detail.getOneviewId() + "')\" >Custom KPI</a></td></tr></table>");
        dateString.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('Date','Dashlets-" + detail.getNoOfViewLets() + "'," + detail.getNoOfViewLets() + "," + detail.getWidth() + "," + detail.getHeight() + ",'Dashlets" + detail.getNoOfViewLets() + "','GrpTyp" + detail.getNoOfViewLets() + "','" + detail.getOneviewId() + "')\" >Date</a></td></tr></table>");
        dateString.append("</td></tr>");
        dateString.append("</table>");
        dateString.append("</div>");
        dateString.append("</td></tr></table>");
        // dateString.append(new SchedulerDAO().getOneviewMeasureHeader(detail,"date"));
        dateString.append("<div id='Dashlets-" + detail.getNoOfViewLets() + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 100%; margin-left: 10px; margin-right: 10px;overflow:auto;'>");
        dateString.append("<center>");
        dateString.append("<table id='date" + detail.getNoOfViewLets() + "' align=\"left\" width=" + detail.getWidth() + " height=" + detail.getHeight() + "  style=\"overflow:auto;display:\">");
        dateString.append("<tr id=\"\">");
        dateString.append("<td><table><tr>");
        dateString.append("<td id='date1" + detail.getNoOfViewLets() + "' style=\"font-size:18pt;color:#2191C0;white-space:nowrap;\">");
        //dateString.append("<font color='#0000A0'>");
        dateString.append("<span style=\"font-size:18pt;color:#2191C0;white-space:nowrap;\">");
        dateString.append(regDate);
        dateString.append("</span>");
        //dateString.append("</font>");
        dateString.append("</td>");
        dateString.append("</tr>");
        dateString.append("<tr>");

        dateString.append("<td><span style=\"color:;font-size:10pt;\">(").append(frmDate + " ").append("TO").append(" " + toDate + ")");
        dateString.append("</span></td></tr></table></td></tr></table>");
        dateString.append("</center>");
        dateString.append("</div>");
        dateString.append("</td>");
        return dateString.toString();
    }
}
