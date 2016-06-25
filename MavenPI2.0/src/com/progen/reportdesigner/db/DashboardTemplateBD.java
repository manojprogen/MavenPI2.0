
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportdesigner.db;

import com.progen.charts.ProgenChartDisplay;
import com.progen.report.PbReportCollection;
import com.progen.report.charts.PbGraphDisplay;
import com.progen.report.query.PbReportQuery;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.PbReturnObject;

/**
 *
 * @author mahesh.sanampudi@progenbusiness.com
 */
public class DashboardTemplateBD {

    DashboardTemplateDAO DAO = new DashboardTemplateDAO();
    public static Logger logger = Logger.getLogger(DashboardTemplateBD.class);

    public HashMap setDefaults(String grpId, String grpType, HashMap GraphHashMap, HashMap ParametersHashMap, HashMap TableHashMap, HashMap GraphSizesDtlsHashMap, HashMap GraphClassesHashMap) {
        HashMap GraphDetails = null;
        GraphDetails = new HashMap();
        ArrayList Parameters = (ArrayList) ParametersHashMap.get("Parameters");
        ArrayList REP = (ArrayList) TableHashMap.get("REP");
        ArrayList CEP = (ArrayList) TableHashMap.get("CEP");
        ArrayList Measures = (ArrayList) TableHashMap.get("Measures");

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("GraphSizesDtlsHashMap is " + GraphSizesDtlsHashMap);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("GraphClassesHashMap is " + GraphClassesHashMap);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("GraphSizesDtlsHashMap.get(Medium) is " + GraphSizesDtlsHashMap.get("Medium"));
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("GraphClassesHashMap.get(grpType) is " + GraphClassesHashMap.get(grpType));

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

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("grpId is " + grpId);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("grpIds is " + grpIds);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Measures is " + Measures);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("REP is " + REP);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("CEP is " + CEP);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Parameters is " + Parameters);

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

                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("barChartColumnNames[i] in first for loop is " + barChartColumnNames[i]);
                }
                for (int i = viewByElementIds.length; i < (graphCols.length + viewByElementIds.length); i++) {
                    barChartColumnNames[i] = graphCols[i - viewByElementIds.length];
                    pieChartColumns[i] = graphCols[i - viewByElementIds.length];
                    barChartColumnTitles[i] = graphCols[i - viewByElementIds.length];
                    axis[i] = "0";
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("barChartColumnNames[i]  in second for loop is " + barChartColumnNames[i]);
                }
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("viewByElementIds is " + viewByElementIds);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("viewByElementIds.length is " + viewByElementIds.length);
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

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("GraphDetails is " + GraphDetails);

            GraphHashMap.put(grpId, GraphDetails);
            GraphHashMap.put("graphIds", grpIds);
        } catch (Exception exception) {
            logger.error("Exception: ", exception);
        }
        logger.info("successful");
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

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Parameters is " + Parameters);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("REP is " + REP);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("grpColumns is " + grpColumns);

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
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("viewByElementIds is " + viewByElementIds);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("viewByElementIds.length is " + viewByElementIds.length);

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

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Before calling pbretObj");
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("GraphHashMap is " + GraphHashMap);
            pbretObj = getRecordSet(ParametersHashMap, TableHashMap, GraphHashMap);

            String[] dbColumns = null;
            String[] axis = null;

            String[] graphIds = grpIds.split(",");

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("graphIds is " + grpIds);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("graphIds.length  is " + graphIds.length);

            // HashMap[] graphMapDetails = new HashMap[GraphHashMap.size()];
            HashMap[] graphMapDetails = new HashMap[graphIds.length];

            if (pbretObj != null) {
                ArrayList AllGraphColumns = (ArrayList) GraphHashMap.get("AllGraphColumns");
                ArrayList originalColumns = new ArrayList();
                Container container = new Container();

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("AllGraphColumns is " + AllGraphColumns);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Measures is " + Measures);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("REP is " + REP);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("CEP is " + CEP);

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
                        //graphColcount = dbColumns.length;

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

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("originalColumns in buildGraph is " + originalColumns);

                container.setOriginalColumns(originalColumns);
                container.setDisplayedSet(pbretObj);
                //displayedSet = container.getDisplayedSet();

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("displayedSet is " + displayedSet);

                String[] viewBys = null;
                String[] viewBysDispNames = null;

                if (graphIds.length != 0) {
                    for (int i = 0; i < graphMapDetails.length; i++) {
                        graphMapDetails[i] = (HashMap) GraphHashMap.get(graphIds[i]);
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("graphMapDetails[i] is " + graphMapDetails[i]);
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("graphMapDetails[i].get(viewByElementIds)) is " + graphMapDetails[i].get("viewByElementIds"));

                        if (CEP != null && CEP.size() != 0) {
                            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("CEP is not null and its length is not Zero");
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
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" viewBys are : " + viewBys);

                        viewBysDispNames = viewBys;

                        for (int k = 0; k < viewBys.length; k++) {
                            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("viewBys[k] is " + viewBys[k]);
                        }

                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("originalColumns is " + originalColumns);
                    }
                    GraphDisplay.setViewByColNames(viewBysDispNames);
                    GraphDisplay.setViewByElementIds(viewBys);

                    GraphDisplay.setCurrentDispRetObjRecords(pbretObj);
                    //GraphDisplay.setCurrentDispRecords(displayedSet);
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
                            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("grpType is " + grpType);
                            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("GraphTypesHashMap.get(GraphSTypesKeySet[gtype]) is " + String.valueOf(GraphTypesHashMap.get(GraphSTypesKeySet[gtype])));

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
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("graphMapDetails[grpCnt] is " + graphMapDetails[grpCnt]);
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("graphMapDetails[grpCnt].get(viewByElementIds)) is " + graphMapDetails[grpCnt].get("viewByElementIds"));

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
                            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("grpType is " + grpType);
                            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("GraphTypesHashMap.get(GraphSTypesKeySet[gtype]) is " + String.valueOf(GraphTypesHashMap.get(GraphSTypesKeySet[gtype])));

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

                            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("grpSize is " + grpSize);
                            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("String.valueOf(GraphSizesHashMap.get(GraphSizesKeySet[gsize])) is " + String.valueOf(GraphSizesHashMap.get(GraphSizesKeySet[gsize])));

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
        } catch (IOException ex) { 
            logger.info("Exception: ", ex);
        } catch (Exception ex) { 
            logger.info("Exception: ", ex);
        } 
        logger.info("successful");
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

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("ParametersHashMap is " + ParametersHashMap);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("TableHashMap is " + TableHashMap);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("graphHashhMap is " + graphHashhMap);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("ParametersHashMap.get(Parameters) is " + ParametersHashMap.get("Parameters"));

            ArrayList paramList = (ArrayList) ParametersHashMap.get("Parameters");

            ArrayList REP = (ArrayList) TableHashMap.get("REP");
            ArrayList CEP = (ArrayList) TableHashMap.get("CEP");
            ArrayList Measures = (ArrayList) TableHashMap.get("Measures");
            ArrayList AllGraphColumns = (ArrayList) graphHashhMap.get("AllGraphColumns");

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" REP is " + REP);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" CEP is " + CEP);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" Measures is " + Measures);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" AllGraphColumns is " + AllGraphColumns);

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
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("reportQryElementIds is" + reportQryElementIds);

            if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
                // reportQryAggregations = DAO.getReportQryAggregations(reportQryElementIds);

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

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("pbretObj is " + pbretObj);

        } catch (Exception e) {
            logger.info("Exception: ", e);
        }
        logger.info("successful");
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

    /*
     * public String refreshGraphs(HashMap GraphHashMap, HttpServletRequest
     * request, HttpServletResponse response) { ArrayList grpDetails = new
     * ArrayList(); PbGraphDisplay GraphDisplay = new PbGraphDisplay();
     * GraphDisplay.setCtxPath(request.getContextPath()); StringBuffer
     * graphsBuffer = new StringBuffer("");
     *
     * ArrayList displayedSet = new ArrayList(); PbReturnObject pbretObj = null;
     * String grpIds="";
     *
     * try { HashMap ParametersHashMap = (HashMap)
     * request.getSession().getAttribute("ParametersHashMap"); HashMap
     * TableHashMap = (HashMap)
     * request.getSession().getAttribute("TableHashMap");
     *
     * ArrayList Measures = (ArrayList) TableHashMap.get("Measures"); ArrayList
     * REP = (ArrayList) TableHashMap.get("REP"); ArrayList CEP = (ArrayList)
     * TableHashMap.get("CEP"); ArrayList paramList = (ArrayList)
     * ParametersHashMap.get("Parameters");
     *
     * pbretObj = getRecordSet(ParametersHashMap, TableHashMap, GraphHashMap);
     *
     * String[] dbColumns = null; String[] axis = null;
     *
     * if (pbretObj != null) { ArrayList AllGraphColumns = (ArrayList)
     * GraphHashMap.get("AllGraphColumns"); ArrayList originalColumns = new
     * ArrayList(); Container container = new Container();
     *
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("AllGraphColumns
     * is " + AllGraphColumns);
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Measures
     * is " + Measures);
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("REP
     * is " + REP);
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("CEP
     * is " + CEP);
     *
     * if (CEP == null || CEP.size() == 0) { if (AllGraphColumns == null ||
     * AllGraphColumns.size() == 0) { for (int i = 0; i < REP.size(); i++) {
     * originalColumns.add(String.valueOf(REP.get(i))); }
     * originalColumns.add(String.valueOf(Measures.get(REP.size())));
     * container.setOriginalColumns(originalColumns);
     *
     * } else { originalColumns.add(String.valueOf(paramList.get(0))); for (int
     * i = 0; i < AllGraphColumns.size(); i++) {
     * originalColumns.add(String.valueOf(AllGraphColumns.get(i))); }
     * container.setOriginalColumns(originalColumns); } } else { if
     * (pbretObj.getRowCount() != 0) { dbColumns = pbretObj.getColumnNames();
     * axis = new String[dbColumns.length]; for (int colIndex = 0; colIndex <
     * dbColumns.length; colIndex++) {
     * originalColumns.add(String.valueOf(dbColumns[colIndex])); axis[colIndex]
     * = "0"; } container.setOriginalColumns(originalColumns); } }
     * container.setDisplayedSet(pbretObj); displayedSet =
     * container.getDisplayedSet();
     *
     * if(GraphHashMap.get("graphIds")!=null){
     * grpIds=(String)GraphHashMap.get("graphIds"); }
     *
     *
     * String[] graphIds = grpIds.split(",");
     *
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("graphIds
     * is " + graphIds);
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("graphIds.length
     * is " + graphIds.length);
     *
     * // HashMap[] graphMapDetails = new HashMap[GraphHashMap.size()];
     * HashMap[] graphMapDetails = new HashMap[graphIds.length];
     *
     * if (graphIds.length != 0) { for (int i = 0; i < graphMapDetails.length;
     * i++) { graphMapDetails[i] = (HashMap) GraphHashMap.get(graphIds[i]);
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("graphMapDetails[i]
     * is " + graphMapDetails[i]);
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("graphMapDetails[i].get(viewByElementIds))
     * is " + graphMapDetails[i].get("viewByElementIds"));
     *
     * GraphDisplay.setViewByColNames((String[])
     * graphMapDetails[i].get("viewByElementIds"));
     * GraphDisplay.setViewByElementIds((String[])
     * graphMapDetails[i].get("viewByElementIds"));
     *
     * if (CEP != null || CEP.size() != 0) {
     * graphMapDetails[i].put("barChartColumnNames", (String[])
     * originalColumns.toArray(new String[0]));
     * graphMapDetails[i].put("barChartColumnTitles", (String[])
     * originalColumns.toArray(new String[0]));
     * graphMapDetails[i].put("pieChartColumns", (String[])
     * originalColumns.toArray(new String[0])); graphMapDetails[i].put("axis",
     * axis); } } GraphDisplay.setCurrentDispRecords(displayedSet);
     * //GraphDisplay.setSwapGraphAnalysis(swapGraphAnalysis);
     * GraphDisplay.setSession(request.getSession(false));
     * GraphDisplay.setResponse(response);
     * GraphDisplay.setOut(response.getWriter()); GraphDisplay.setReportId("");
     * GraphDisplay.setGraphMapDetails(graphMapDetails);
     * GraphDisplay.setJscal("");
     *
     * grpDetails = GraphDisplay.getGraphHeadersByGraphMap();
     *
     * ProgenChartDisplay[] pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
     * ProgenChartDisplay[] pchartsZoom = (ProgenChartDisplay[])
     * grpDetails.get(4);
     *
     *
     *
     * String[] paths =
     * grpDetails.get(0).toString().split(";");//grpDetails[0].split(";");
     * String[] grpTitles = grpDetails.get(1).toString().split(";"); String[]
     * pathsZoom =
     * grpDetails.get(3).toString().split(";");//grpDetails[0].split(";");
     * graphMapDetails = (HashMap[]) grpDetails.get(5);//
     *
     *
     * graphsBuffer.append("<Table>"); graphsBuffer.append("<Tr>"); for (int
     * grpCnt = 0; grpCnt < pcharts.length; grpCnt++) {
     * pcharts[grpCnt].setCtxPath(request.getContextPath());
     * pchartsZoom[grpCnt].setCtxPath(request.getContextPath());
     * pcharts[grpCnt].setSwapColumn(true);
     *
     * graphsBuffer.append("<Td>"); graphsBuffer.append("<div
     * class=\"column\">");//column div starts here graphsBuffer.append("<div
     * class=\"portlet\">");//portlet div starts here
     *
     * graphsBuffer.append("<div class=\"portlet-header\">" + grpTitles[grpCnt]
     * + "</div>");//portlet-header div starts here
     *
     * graphsBuffer.append("<div style = \"width:auto;height:auto\" align =
     * \"center\" >");
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("pcharts[grpCnt].chartDisplay
     * is " + pcharts[grpCnt].chartDisplay); //graphImages/Pie3D.gif
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("pcharts[grpCnt].chartDisplay
     * is " + pcharts[grpCnt].chartDisplay); if
     * (pcharts[grpCnt].chartDisplay.equalsIgnoreCase("")) { String grpType =
     * String.valueOf(graphMapDetails[grpCnt].get("graphTypeName"));
     *
     * graphsBuffer.append("<SCRIPT LANGUAGE=\"JavaScript\" SRC=\"overlib.js\">
     * </SCRIPT>"); graphsBuffer.append("<div id=\"overDiv\"
     * style=\"position:absolute; visibility:hidden; z-index:1000;\"></div>");
     * graphsBuffer.append("<img src=\"" + request.getContextPath() +
     * "/graphImages/" + grpType + ".gif\" border='0' > </img>");
     * graphsBuffer.append(pcharts[grpCnt].chartDisplay); } else {
     * graphsBuffer.append(pcharts[grpCnt].chartDisplay); }
     *
     * graphsBuffer.append("</div>");//portlet-header div ends here
     * graphsBuffer.append("</div >");//portlet div ends here
     * graphsBuffer.append("</div >");//column div ends here
     *
     * graphsBuffer.append("</Td>"); } graphsBuffer.append("</Tr>");
     * graphsBuffer.append("</Table>"); } } } catch (Exception ex) {
     * logger.error("Exception:", ex); } return graphsBuffer.toString(); }
     *
     *
     * public ArrayList getRecordSet(HashMap incomingParameters, ArrayList
     * rowViewbys, ArrayList colViewbys, HashMap paramValues) { ArrayList
     * recordsList = new ArrayList(); PbReturnObject pbretObj = null;
     * PbReportCollection collect = new PbReportCollection(); DisplayParameters
     * disp = new DisplayParameters(); PbReportQuery repQuery = new
     * PbReportQuery();
     *
     * try { //collect.reportId = "1"; collect.reportIncomingParameters =
     * incomingParameters; collect.reportColViewbyValues = colViewbys;
     * collect.reportRowViewbys = rowViewbys;
     *
     * collect.getParamMetaData();
     *
     * repQuery.setRowViewbyCols(rowViewbys);
     * repQuery.setColViewbyCols(colViewbys);
     *
     * repQuery.setQryColumns(collect.reportQryElementIds);
     * repQuery.setColAggration(collect.reportQryAggregations);
     *
     * repQuery.setParamValue(paramValues);
     *
     * pbretObj = repQuery.getPbReturnObject();
     *
     * Container container = new Container(); ArrayList originalColumns =
     * collect.tableElementIds; container.setOriginalColumns(originalColumns);
     * container.setDisplayedSet(pbretObj); recordsList =
     * container.getDisplayedSet(); } catch (Exception e) {
     * logger.error("Exception:", ex); } return recordsList; }
     */
    public static void main(String[] args) {
    }
}
