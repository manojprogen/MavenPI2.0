/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportdesigner.bd;

import com.progen.bd.ProgenJqplotGraphBD;
import com.progen.charts.GraphProperty;
import com.progen.charts.JqplotGraphProperty;
import com.progen.charts.ProgenChartDisplay;
import com.progen.db.ProgenDataSet;
import com.progen.jqplot.ProGenJqPlotChartTypes;
import com.progen.query.RTDimensionElement;
import com.progen.query.RTMeasureElement;
import com.progen.report.ImportExcelDetail;
import com.progen.report.MultiPeriodKPI;
import com.progen.report.PbReportCollection;
import com.progen.report.ReportParameter;
import com.progen.report.charts.PbGraphDisplay;
import com.progen.report.data.DataFacade;
import com.progen.report.display.DisplayParameters;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.query.PbReportQuery;
import com.progen.report.query.QueryExecutor;
import com.progen.reportdesigner.action.ReportMetadataUIHelper;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.bd.PbAOViewerBD;
import com.progen.reportview.bd.PbReportViewerBD;
import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import prg.db.Container;
import prg.db.OneViewLetDetails;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenParam;

/**
 * @filename ReportTemplateBD
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 22, 2009, 4:59:03 PM
 */
public class ReportTemplateBD {

    public static Logger logger = Logger.getLogger(ReportTemplateBD.class);
    ReportTemplateDAO DAO = new ReportTemplateDAO();
    private String userId = null;
    private String bizRoles = null;
    //int i;//added by susheela start
    private String reportId = null;
    private boolean isFxCharts = false;
    private boolean getGraphIdFromSequence = true;
    ///
    /*
     * private String graphSize = "Medium"; private String grplegend = "Y";
     * private String grplegendloc = "Bottom"; private String grpshox = "Y";
     * private String grpshoy = "Y"; private String grplyaxislabel = ""; private
     * String grpryaxislabel = ""; private String grpdrill = "Y"; private String
     * grpbcolor = ""; private String grpfcolor = ""; private String grpdata =
     * "Y"; private boolean SwapColumn = true; private String graphGridLines =
     * "Y"; private String graphDisplayRows = "10"; private String
     * showMinMaxRange = "N"; private String graphLegendLoc = "Bottom"; private
     * String graphLegend = "Y"; private String graphData = "Y"; private String
     * graphFcolor = ""; private String graphBcolor= ""; private String
     * graphLYaxislabel = ""; private String graphRYaxislabel = ""; private
     * String graphshowX = "Y"; private String graphshowY= "Y";
     */
    private String graphTableHidden;
    public HttpServletRequest request;

    //Is called when ever user clicks on Add graphs in report designing
    public HashMap setDefaults(String grpId, String grpType, Container container, HashMap GraphSizesDtlsHashMap, HashMap GraphClassesHashMap, String isdashboard, String measureid, String measurename) {
        HashMap ParametersHashMap = container.getParametersHashMap();
        HashMap TableHashMap = container.getTableHashMap();
        HashMap ReportHashMap = container.getReportHashMap();
        HashMap GraphHashMap = container.getGraphHashMap();
        HashMap GraphDetails = null;
        GraphDetails = new HashMap();
        ArrayList allGraphColumns = null;

        ArrayList Parameters = (ArrayList) ParametersHashMap.get("Parameters");
        ArrayList ParameterNames = (ArrayList) ParametersHashMap.get("ParametersNames");
        ArrayList REP = (ArrayList) TableHashMap.get("REP");
        ArrayList REPNames = (ArrayList) TableHashMap.get("REPNames");
        //ArrayList CEP = (ArrayList) TableHashMap.get("CEP");
        //ArrayList CEPNames = (ArrayList) TableHashMap.get("CEPNames");
        ArrayList Measures = (ArrayList) TableHashMap.get("Measures");
        ArrayList MeasuresNames = (ArrayList) TableHashMap.get("MeasuresNames");
        //sandeep
        if (isdashboard != null && isdashboard.equalsIgnoreCase("true")) {

            String rowviewbyid = null;
            String rowviewbyname = null;
//                  ArrayList paramnames= (ArrayList) parameterHashMap.get("parametersNames");
            if (ParameterNames != null && ParameterNames.size() > 0) {
                for (int i = 0; i < ParameterNames.size(); i++) {
                    if (ParameterNames.get(i).toString().replace("1q1", "").equalsIgnoreCase("Month Year")) {
                        rowviewbyid = Parameters.get(i).toString();
                        rowviewbyname = ParameterNames.get(i).toString().replace("1q1", " ");

                    }

                }
            }
            if (rowviewbyid == null || rowviewbyid == "") {
                rowviewbyid = "TIME";
                rowviewbyname = "TIME";
            }
            REP = new ArrayList();
            REPNames = new ArrayList();
            Measures = new ArrayList();
            MeasuresNames = new ArrayList();
            REP.add(rowviewbyid);
            REPNames.add(rowviewbyname);
            Measures.add(measureid);
            MeasuresNames.add(measurename);


        }
        //end of sandeep code for quick trend for dashboard
        ArrayList sizeDtls = (ArrayList) GraphSizesDtlsHashMap.get("Medium");
        String graphClass = String.valueOf(GraphClassesHashMap.get(grpType));
        String grpIds = "";
        String[] barChartColumnNames = null;
        String[] pieChartColumns = null;
        String[] barChartColumnTitles = null;
        String[] axis = null;
        String[] viewByElementIds = null;
        String[] viewBys = null;
        String[] viewByNames = null;
        String[] graphCols = new String[0];
        String[] graphColNames = new String[0];
        String ReportName = "";
        int count = 1;
        String[] barChartColumnNames1 = null;
        String[] barChartColumnTitles1 = null;
        String[] barChartColumnNames2 = null;
        String[] barChartColumnTitles2 = null;
        LinkedHashMap grphMeasMap = new LinkedHashMap<String, String>();
        try {
            if (GraphHashMap.get("graphIds") != null) {
                grpIds = (String) GraphHashMap.get("graphIds");
                grpIds = grpIds + "," + grpId;
            } else {
                grpIds = grpId;
            }
            if (REP != null) {
                viewByElementIds = (String[]) REP.toArray(new String[0]);
                viewBys = viewByElementIds;
                viewByNames = (String[]) REPNames.toArray(new String[0]);
            } else {
                if (Parameters != null && Parameters.size() != 0) {
                    viewByElementIds = new String[1];
                    viewByElementIds[0] = String.valueOf(Parameters.get(0));

                    viewBys = viewByElementIds;
                    viewByNames = new String[1];
                    viewByNames[0] = String.valueOf(ParameterNames.get(0));
                }
            }
            if (Measures != null && Measures.size() != 0) {
                if (grpType.equalsIgnoreCase("columnPie") || grpType.equalsIgnoreCase("columnPie3D") || grpType.equalsIgnoreCase("Spider")) {
                    graphCols = (String[]) Measures.toArray(new String[0]);
                    graphColNames = (String[]) MeasuresNames.toArray(new String[0]);
                } else {
                    graphCols = new String[1];
                    graphColNames = new String[1];
                    graphCols[0] = (String) Measures.toArray(new String[0])[0];
                    graphColNames[0] = (String) MeasuresNames.toArray(new String[0])[0];
                }

                barChartColumnNames = new String[viewByElementIds.length + graphCols.length];
                pieChartColumns = new String[barChartColumnNames.length];
                barChartColumnTitles = new String[barChartColumnNames.length];
                axis = new String[barChartColumnNames.length];

                for (int index = 0; index < viewByElementIds.length; index++) {
                    if (viewByElementIds[index].equalsIgnoreCase("Time")) {
                        viewBys[index] = viewByElementIds[index];
                        barChartColumnNames[index] = viewBys[index];
                        pieChartColumns[index] = viewBys[index];
                    } else {
                        if (!(viewByElementIds[index].contains("A_"))) {
                            viewBys[index] = "A_" + viewByElementIds[index];
                            barChartColumnNames[index] = viewBys[index];
                            pieChartColumns[index] = viewBys[index];
                        } else {
                            viewBys[index] = viewByElementIds[index];
                            barChartColumnNames[index] = viewBys[index];
                            pieChartColumns[index] = viewBys[index];
                        }
                    }
                    barChartColumnTitles[index] = viewByNames[index];
                    axis[index] = "0";

                }
                for (int index = viewByElementIds.length; index < (graphCols.length + viewByElementIds.length); index++) {

                    if (!(graphCols[index - viewByElementIds.length].contains("A_"))) {
                        barChartColumnNames[index] = "A_" + graphCols[index - viewByElementIds.length];
                        pieChartColumns[index] = "A_" + graphCols[index - viewByElementIds.length];
                    } else {
                        barChartColumnNames[index] = graphCols[index - viewByElementIds.length];
                        pieChartColumns[index] = graphCols[index - viewByElementIds.length];
                    }
                    barChartColumnTitles[index] = graphColNames[index - viewByElementIds.length];
                    axis[index] = "0";
                    grphMeasMap.put(graphCols[index - viewByElementIds.length], barChartColumnTitles[index]);
                }
//                GraphDetails.put("viewByElementIds", viewBys);
                GraphDetails.put("barChartColumnNames", barChartColumnNames);
                GraphDetails.put("pieChartColumns", pieChartColumns);
                GraphDetails.put("barChartColumnTitles", barChartColumnTitles);
                GraphDetails.put("axis", axis);

                if (grpType.contains("Dual Axis") || grpType.contains("Dual") || grpType.contains("OverlaidBar") || grpType.contains("FeverChart") || grpType.contains("OverlaidArea") || grpType.contains("Pareto")) {

                    barChartColumnTitles1 = new String[count + viewByElementIds.length];
                    barChartColumnNames1 = new String[barChartColumnTitles1.length];
                    barChartColumnTitles2 = new String[(barChartColumnNames.length - count)];
                    barChartColumnNames2 = new String[barChartColumnTitles2.length];

                    for (int j = 0; j < viewByElementIds.length; j++) {
                        barChartColumnTitles1[j] = barChartColumnTitles[j];
                        barChartColumnTitles2[j] = barChartColumnTitles[j];
                        barChartColumnNames1[j] = barChartColumnNames[j];
                        barChartColumnNames2[j] = barChartColumnNames[j];
                    }
                    for (int j = viewByElementIds.length; j < barChartColumnTitles1.length; j++) {
                        barChartColumnTitles1[j] = barChartColumnTitles[j];
                        barChartColumnNames1[j] = barChartColumnNames[j];
                    }
                    for (int j = barChartColumnTitles1.length; j < barChartColumnTitles.length; j++) {
                        barChartColumnTitles2[j - viewByElementIds.length] = barChartColumnTitles[j];
                        barChartColumnNames2[j - viewByElementIds.length] = barChartColumnNames[j];
                    }
                    GraphDetails.put("barChartColumnNames1", barChartColumnNames1);
                    GraphDetails.put("barChartColumnTitles1", barChartColumnTitles1);
                    GraphDetails.put("barChartColumnNames2", barChartColumnNames2);
                    GraphDetails.put("barChartColumnTitles2", barChartColumnTitles2);
                }
            }
            if (container.getReportName() != null) {
                ReportName = container.getReportName();
            }
            if (isdashboard != null && isdashboard.equalsIgnoreCase("true")) {
                ReportName = "";
            }
            GraphDetails.put("viewByElementIds", viewBys);
            GraphDetails.put("viewByNames", viewByNames);
            GraphDetails.put("graphId", grpId);
            GraphDetails.put("graphName", ReportName);
            GraphDetails.put("graphClassName", graphClass);
            GraphDetails.put("graphTypeName", grpType);
            GraphDetails.put("graphSize", "Medium");
            GraphDetails.put("graphWidth", String.valueOf(sizeDtls.get(0)));
            GraphDetails.put("graphHeight", String.valueOf(sizeDtls.get(1)));
            GraphDetails.put("graphLegend", "Y");
            GraphDetails.put("graphLegendLoc", "Bottom");
            GraphDetails.put("graphshowX", "Y");
            GraphDetails.put("graphshowY", "Y");
            GraphDetails.put("graphLYaxislabel", "");
            GraphDetails.put("graphRYaxislabel", "");
            GraphDetails.put("graphDrill", "Y");
            GraphDetails.put("graphBcolor", "fff");
            GraphDetails.put("graphFcolor", "fff");
            GraphDetails.put("graphData", "Y");
            GraphDetails.put("measurePosition", "Bottom");
            if (container.isBusTemplateFromOneview()) {
                GraphDetails.put("graphMeasures", grphMeasMap);
                HashMap jqpToJfNameMap = (HashMap) request.getSession(false).getAttribute("JqpToJfNameMap");
                HashMap jqpMap = (HashMap) request.getSession(false).getAttribute("JqpMap");
                String jqGraphTypeName = (String) jqpToJfNameMap.get(grpType);
                String jqgraphId = (String) jqpMap.get(jqGraphTypeName);
                HashMap jqgraphdetails = new HashMap();
                HashMap jqdetailmap = new HashMap();
                jqgraphdetails.put("grpidfrmrep", grpId);
                jqgraphdetails.put("graphTypeid", jqgraphId);
                jqgraphdetails.put("graphTypename", jqGraphTypeName);
                jqgraphdetails.put("selectedgraphtype", "jq");
                jqdetailmap.put(grpId, jqgraphdetails);
                GraphHashMap.put("jqdetailmap" + grpId, jqdetailmap);
            }
            if (GraphHashMap.get("AllGraphColumns") != null) {
                allGraphColumns = (ArrayList) GraphHashMap.get("AllGraphColumns");
            }
            if (allGraphColumns == null) {
                allGraphColumns = new ArrayList();
            }
            for (int index = 0; index < graphCols.length; index++) {
                if (!allGraphColumns.contains(graphCols[index])) {
                    allGraphColumns.add(graphCols[index]);
                }
            }
            GraphHashMap.put("AllGraphColumns", allGraphColumns);
            GraphHashMap.put(grpId, GraphDetails);
            GraphHashMap.put("graphIds", grpIds);

        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }

        return GraphHashMap;
    }

    // called when user changes the graph types in report designing
    public HashMap changeGraphType(HashMap GraphHashMap, String grpType, String grpId, HashMap ParametersHashMap, HashMap TableHashMap, HashMap GraphClassesHashMap) {
        HashMap GraphDetails = null;
        String graphClass = String.valueOf(GraphClassesHashMap.get(grpType));
        GraphDetails = (HashMap) GraphHashMap.get(grpId);
        GraphDetails.put("graphTypeName", grpType);
        GraphDetails.put("graphClassName", graphClass);
        GraphHashMap.put(grpId, GraphDetails);
        return GraphHashMap;
    }

    // called when user changes the graph size in report designing
    public HashMap changeGraphSize(HashMap GraphHashMap, String grpSize, String grpId, HashMap ParametersHashMap, HashMap TableHashMap, HashMap GraphSizesDtlsHashMap) {
        HashMap GraphDetails = null;
        GraphDetails = (HashMap) GraphHashMap.get(grpId);
        GraphDetails.put("graphSize", grpSize);

        ArrayList sizeDtls = (ArrayList) GraphSizesDtlsHashMap.get(grpSize);

        GraphDetails.put("graphWidth", String.valueOf(sizeDtls.get(0)));
        GraphDetails.put("graphHeight", String.valueOf(sizeDtls.get(1)));

        //GraphDetails = DAO.getGrpDimensionsBySize(GraphDetails, grpSize);

        GraphHashMap.put(grpId, GraphDetails);
        return GraphHashMap;
    }

    //called when user add/edit graph columns in report designing
    public HashMap changeGraphColumns(HashMap GraphHashMap, String grpColumns, String grpId, HashMap ParametersHashMap, HashMap TableHashMap, String grpColumnsNames) {

        HashMap GraphDetails = null;
        ArrayList Parameters = (ArrayList) ParametersHashMap.get("Parameters");
        ArrayList REP = (ArrayList) TableHashMap.get("REP");

        ArrayList ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
        ArrayList REPNames = (ArrayList) TableHashMap.get("REPNames");
        String[] viewByElementIds = null;
        String[] viewBys = null;
        String[] viewByDispNames = null;
        String[] axis = null;
        String[] barChartColumnNames = null;
        String[] pieChartColumns = null;
        String[] barChartColumnTitles = null;
        String[] graphCols = null;
        String[] graphColsNames = null;
        if (REP == null || REP.size() == 0) {
            viewByElementIds = new String[1];
            viewByDispNames = new String[1];
            viewByElementIds[0] = String.valueOf(Parameters.get(0));
            viewByDispNames[0] = String.valueOf(ParametersNames.get(0));

            viewBys = viewByElementIds;
        } else {
            viewByElementIds = (String[]) REP.toArray(new String[0]);
            viewByDispNames = (String[]) REPNames.toArray(new String[0]);

            viewBys = viewByElementIds;
        }
        graphCols = grpColumns.split(",");
        graphColsNames = grpColumnsNames.split(",");
        barChartColumnNames = new String[viewByElementIds.length + graphCols.length];
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
            barChartColumnTitles[i] = viewByDispNames[i];
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
            barChartColumnTitles[i] = graphColsNames[i - viewByElementIds.length];
            axis[i] = "0";
        }


        GraphDetails = (HashMap) GraphHashMap.get(grpId);

        if (GraphDetails != null) {
            GraphDetails.put("barChartColumnNames", barChartColumnNames);
            GraphDetails.put("pieChartColumns", pieChartColumns);
            GraphDetails.put("barChartColumnTitles", barChartColumnTitles);
            GraphDetails.put("viewByElementIds", viewBys);
            GraphDetails.put("axis", axis);

            GraphHashMap.put(grpId, GraphDetails);
        }
        return GraphHashMap;
    }

    //called when user add/edit graph columns only for multi axis charts in report designing
    public HashMap changeDualAxisColumns(HashMap GraphHashMap, String grpId, HashMap ParametersHashMap, HashMap TableHashMap, String leftColumns, String rightColumns) {

        HashMap GraphDetails = null;
        ArrayList Parameters = (ArrayList) ParametersHashMap.get("Parameters");
        ArrayList REP = (ArrayList) TableHashMap.get("REP");

        ArrayList ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
        ArrayList REPNames = (ArrayList) TableHashMap.get("REPNames");
        String[] viewByElementIds = null;
        String[] viewBys = null;
        String[] viewByDispNames = null;
        String[] axis = null;
        String[] barChartColumnNames = null;
        String[] barChartColumnTitles = null;
        String[] barChartColumnNames1 = null;
        String[] barChartColumnTitles1 = null;

        String[] barChartColumnNames2 = null;
        String[] barChartColumnTitles2 = null;
        String[] leftgraphCols = null;
        String[] rightgraphCols = null;
        String[] allgraphCols = null;
        String allColumns = null;

        //allColumns = leftColumns + "," + rightColumns;

        if (!leftColumns.equalsIgnoreCase("") && rightColumns.equalsIgnoreCase("")) {
            allColumns = leftColumns;
        } else if (leftColumns.equalsIgnoreCase("") && !rightColumns.equalsIgnoreCase("")) {
            allColumns = rightColumns;
        } else {
            allColumns = leftColumns + "," + rightColumns;
        }


        allgraphCols = allColumns.split(",");
        leftgraphCols = leftColumns.split(",");
        rightgraphCols = rightColumns.split(",");






        if (REP == null) {
            viewByElementIds = new String[1];
            viewByDispNames = new String[1];
            viewByElementIds[0] = String.valueOf(Parameters.get(0));
            viewByDispNames[0] = String.valueOf(ParametersNames.get(0));

            viewBys = viewByElementIds;
        } else {
            viewByElementIds = (String[]) REP.toArray(new String[0]);
            viewByDispNames = (String[]) REPNames.toArray(new String[0]);
            viewBys = viewByElementIds;
        }


        barChartColumnNames = new String[viewByElementIds.length + allgraphCols.length];
        barChartColumnTitles = new String[barChartColumnNames.length];
        axis = new String[barChartColumnNames.length];

        barChartColumnNames1 = new String[viewByElementIds.length + leftgraphCols.length];
        barChartColumnTitles1 = new String[barChartColumnNames1.length];

        barChartColumnNames2 = new String[viewByElementIds.length + rightgraphCols.length];
        barChartColumnTitles2 = new String[barChartColumnNames2.length];

        for (int i = 0; i < viewByElementIds.length; i++) {
            if (viewByElementIds[i].equalsIgnoreCase("Time")) {
                viewBys[i] = viewByElementIds[i];
                barChartColumnNames[i] = viewBys[i];
            } else {
                if (!(viewByElementIds[i].contains("A_"))) {
                    viewBys[i] = "A_" + viewByElementIds[i];
                    barChartColumnNames[i] = viewBys[i];
                } else {
                    viewBys[i] = viewByElementIds[i];
                    barChartColumnNames[i] = viewBys[i];
                }
            }
            barChartColumnTitles[i] = viewByDispNames[i];
            axis[i] = "0";
//building left and right graph columns view bys
            barChartColumnNames1[i] = viewBys[i];
            barChartColumnNames2[i] = viewBys[i];
            barChartColumnTitles1[i] = viewByDispNames[i];
            barChartColumnTitles2[i] = viewByDispNames[i];
        }
        //building all graph columns
        for (int i = viewByElementIds.length; i < (barChartColumnNames.length); i++) {
            if (!(allgraphCols[i - viewByElementIds.length].contains("A_"))) {
                barChartColumnNames[i] = "A_" + allgraphCols[i - viewByElementIds.length];
            } else {
                barChartColumnNames[i] = allgraphCols[i - viewByElementIds.length];
            }
            barChartColumnTitles[i] = allgraphCols[i - viewByElementIds.length];
            axis[i] = "0";
        }
        //building bar chart columns
        for (int i = viewByElementIds.length; i < (barChartColumnNames1.length); i++) {
            barChartColumnNames1[i] = leftgraphCols[i - viewByElementIds.length];
            barChartColumnTitles1[i] = leftgraphCols[i - viewByElementIds.length];
        }
        //building line chart columns
        for (int i = viewByElementIds.length; i < (barChartColumnNames2.length); i++) {
            barChartColumnNames2[i] = rightgraphCols[i - viewByElementIds.length];
            barChartColumnTitles2[i] = rightgraphCols[i - viewByElementIds.length];
        }

        for (int i = barChartColumnNames1.length; i < barChartColumnNames.length; i++) {
            axis[i] = "1";
        }


        GraphDetails = (HashMap) GraphHashMap.get(grpId);
        if (GraphDetails != null) {
            GraphDetails.put("barChartColumnNames", barChartColumnNames);
            GraphDetails.put("pieChartColumns", barChartColumnNames);
            GraphDetails.put("barChartColumnTitles", barChartColumnTitles);
            GraphDetails.put("viewByElementIds", viewBys);
            GraphDetails.put("axis", axis);
            GraphDetails.put("barChartColumnNames1", barChartColumnNames1);
            GraphDetails.put("barChartColumnTitles1", barChartColumnTitles1);
            GraphDetails.put("barChartColumnNames2", barChartColumnNames2);
            GraphDetails.put("barChartColumnTitles2", barChartColumnTitles2);
            GraphHashMap.put(grpId, GraphDetails);
        }
        return GraphHashMap;
    }

    //called to change view bys in bar chart columns,pie chart columns when user changes his default row edge parameters
    public HashMap changeViewBys(HashMap GraphHashMap, ArrayList REP, ArrayList REPNames, ArrayList Parameters, ArrayList ParametersNames) {
        String grpIds = "";
        String[] viewByElementIds = null;
        String[] viewBys = null;
        String[] viewByElementNames = null;
        String[] graphIds = null;
        String[] prevBarChartColumnNames = null;
        //String[] prevPieChartColumns = null;
        String[] prevBarChartColumnTitles = null;
        String[] prevViewByElementIds = null;
        String[] prevAxis = null;
        String[] prevBarChartColumnNames1 = null;
        String[] prevBarChartColumnNames2 = null;
        String[] barChartColumnNames = null;
        //String[] pieChartColumns = null;
        String[] barChartColumnTitles = null;
        String[] axis = null;
        String[] barChartColumnNames1 = null;
        String[] barChartColumnNames2 = null;
        String[] barChartColumnTitles1 = null;
        String[] barChartColumnTitles2 = null;
        String[] prevBarChartColumnTitles1 = null;
        String[] prevBarChartColumnTitles2 = null;

        if (GraphHashMap.get("graphIds") != null) {
            grpIds = (String) GraphHashMap.get("graphIds");
        }
        if (!"".equalsIgnoreCase(grpIds)) {
            graphIds = grpIds.split(",");
            if (REP != null && REP.size() != 0) {
                viewByElementIds = (String[]) REP.toArray(new String[0]);
                viewByElementNames = (String[]) REPNames.toArray(new String[0]);
            } else {
                viewByElementIds = new String[1];
                viewByElementNames = new String[1];
                viewByElementIds[0] = ((String[]) Parameters.toArray(new String[0]))[0];
                viewByElementNames[0] = ((String[]) ParametersNames.toArray(new String[0]))[0];
            }

            HashMap GraphDetails = null;
            for (int i = 0; i < graphIds.length; i++) {
                GraphDetails = (HashMap) GraphHashMap.get(graphIds[i]);
                if (GraphDetails.get("barChartColumnNames") != null) {
                    prevBarChartColumnNames = (String[]) GraphDetails.get("barChartColumnNames");
                    prevBarChartColumnTitles = (String[]) GraphDetails.get("barChartColumnTitles");
                    prevViewByElementIds = (String[]) GraphDetails.get("viewByElementIds");
                    prevAxis = (String[]) GraphDetails.get("axis");

                    if (GraphDetails.get("barChartColumnNames1") != null && GraphDetails.get("barChartColumnNames2") != null) {
                        prevBarChartColumnNames1 = (String[]) GraphDetails.get("barChartColumnNames1");
                        prevBarChartColumnNames2 = (String[]) GraphDetails.get("barChartColumnNames2");
                        prevBarChartColumnTitles1 = (String[]) GraphDetails.get("barChartColumnTitles1");
                        prevBarChartColumnTitles2 = (String[]) GraphDetails.get("barChartColumnTitles2");
                    }

                    viewBys = new String[viewByElementIds.length];
                    barChartColumnNames = new String[viewByElementIds.length + (prevBarChartColumnNames.length - prevViewByElementIds.length)];
                    barChartColumnTitles = new String[barChartColumnNames.length];
                    axis = new String[barChartColumnNames.length];

                    if (GraphDetails.get("barChartColumnNames1") != null && GraphDetails.get("barChartColumnNames2") != null) {
                        barChartColumnNames1 = new String[viewByElementIds.length + (prevBarChartColumnNames1.length - prevViewByElementIds.length)];
                        barChartColumnNames2 = new String[viewByElementIds.length + (prevBarChartColumnNames2.length - prevViewByElementIds.length)];
                        barChartColumnTitles1 = new String[barChartColumnNames1.length];
                        barChartColumnTitles2 = new String[barChartColumnNames2.length];
                    }
                    for (int j = 0; j < viewByElementIds.length; j++) {
                        if (viewByElementIds[j].equalsIgnoreCase("TIME")) {
                            barChartColumnNames[j] = viewByElementIds[j];
                            viewBys[j] = viewByElementIds[j];
                        } else {
                            if (!(viewByElementIds[j].contains("A_"))) {
                                barChartColumnNames[j] = "A_" + viewByElementIds[j];
                                viewBys[j] = "A_" + viewByElementIds[j];
                            } else {
                                barChartColumnNames[j] = viewByElementIds[j];
                                viewBys[j] = viewByElementIds[j];
                            }
                        }
                        barChartColumnTitles[j] = viewByElementNames[j];
                        axis[j] = "0";
                        if (GraphDetails.get("barChartColumnNames1") != null && GraphDetails.get("barChartColumnNames2") != null) {
                            barChartColumnNames1[j] = barChartColumnNames[j];
                            barChartColumnNames2[j] = barChartColumnNames[j];
                            barChartColumnTitles1[j] = viewByElementNames[j];
                            barChartColumnTitles2[j] = viewByElementNames[j];
                        }
                    }
                    for (int j = prevViewByElementIds.length; j < prevBarChartColumnNames.length; j++) {
                        if (!(prevBarChartColumnNames[j].contains("A_"))) {
                            barChartColumnNames[viewByElementIds.length + (j - prevViewByElementIds.length)] = "A_" + prevBarChartColumnNames[j];
                        } else {
                            barChartColumnNames[viewByElementIds.length + (j - prevViewByElementIds.length)] = prevBarChartColumnNames[j];
                        }
                        barChartColumnTitles[viewByElementIds.length + (j - prevViewByElementIds.length)] = prevBarChartColumnTitles[j];
                        if (j <= (prevAxis.length - 1)) {
                            axis[viewByElementIds.length + (j - prevViewByElementIds.length)] = prevAxis[j];
                        } else {
                            axis[viewByElementIds.length + (j - prevViewByElementIds.length)] = "0";
                        }
                    }
                    if (GraphDetails.get("barChartColumnNames1") != null && GraphDetails.get("barChartColumnNames2") != null) {
                        for (int j = prevViewByElementIds.length; j < prevBarChartColumnNames1.length; j++) {
                            if (prevBarChartColumnNames1[j] != null) {
                                if (!(prevBarChartColumnNames1[j].contains("A_"))) {
                                    barChartColumnNames1[viewByElementIds.length + (j - prevViewByElementIds.length)] = "A_" + prevBarChartColumnNames1[j];
                                } else {
                                    barChartColumnNames1[viewByElementIds.length + (j - prevViewByElementIds.length)] = prevBarChartColumnNames1[j];
                                }
                                barChartColumnTitles1[viewByElementIds.length + (j - prevViewByElementIds.length)] = prevBarChartColumnTitles1[j];
                            }
                        }
                        for (int j = prevViewByElementIds.length; j < prevBarChartColumnNames2.length; j++) {
                            if (prevBarChartColumnNames2[j] != null) {
                                if (!(prevBarChartColumnNames2[j].contains("A_"))) {
                                    barChartColumnNames2[viewByElementIds.length + (j - prevViewByElementIds.length)] = "A_" + prevBarChartColumnNames2[j];
                                } else {
                                    barChartColumnNames2[viewByElementIds.length + (j - prevViewByElementIds.length)] = prevBarChartColumnNames2[j];
                                }
                                barChartColumnTitles2[viewByElementIds.length + (j - prevViewByElementIds.length)] = prevBarChartColumnTitles2[j];
                            }
                        }
                        GraphDetails.put("barChartColumnNames1", barChartColumnNames1);
                        GraphDetails.put("barChartColumnNames2", barChartColumnNames2);
                        GraphDetails.put("barChartColumnTitles1", barChartColumnTitles1);
                        GraphDetails.put("barChartColumnTitles2", barChartColumnTitles2);
                    }
                    GraphDetails.put("viewByElementIds", viewBys);
                    GraphDetails.put("barChartColumnNames", barChartColumnNames);
                    GraphDetails.put("pieChartColumns", barChartColumnNames);
                    GraphDetails.put("barChartColumnTitles", barChartColumnTitles);
                    GraphDetails.put("axis", axis);
                    GraphHashMap.put(graphIds[i], GraphDetails);
                }
            }

        } else {
        }
        return GraphHashMap;
    }

    //for building graphs in report designing
    public String buildGraph(Container container, HttpServletRequest request, HttpServletResponse response, String grpIds) {
        ArrayList grpDetails = new ArrayList();
        PbGraphDisplay GraphDisplay = new PbGraphDisplay();
        GraphDisplay.setCtxPath(request.getContextPath());
        StringBuffer graphsBuffer = new StringBuffer("");
        PbReturnObject pbretObj = null;
        String reportName = "";
        String reportDesc = "";
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        HashMap ReportHashMap = null;
        HashMap GraphHashMap = null;
        HashMap CrossTabGraphHashMap = null;
        HashMap GraphSizesHashMap = null;
        HashMap GraphTypesHashMap = null;
        HashMap GraphClassesHashMap = null;
        //String[] GraphSizesKeySet = null;
        //String[] GraphTypesKeySet = null;
        String[] dbColumns = null;
        String[] axis = null;
        String[] graphIds = null;
        ArrayList Measures = null;
        ArrayList REP = null;
        ArrayList CEP = null;
        ArrayList Parameters = null;
        ArrayList REPNames = null;
        ArrayList ParametersNames = null;
        HttpSession session = request.getSession(false);
        PbReportCollection collect = new PbReportCollection();
        PbReportQuery repQuery = new PbReportQuery();
        HashMap paramValues = null;
        HashMap TimeDimHashMap = null;
        ArrayList TimeDetailstList = new ArrayList();
        ArrayList AllGraphColumns = null;
        HashMap reportIncomingParameters = new HashMap();
        ArrayList reportQryElementIds = new ArrayList();
        ArrayList reportQryAggregations = new ArrayList();
        boolean isTimeSeries = false;

        String[] GraphSizesValues = null;
        String[] GraphTypesValues = null;
        String[] GraphClassesValues = null;
        ProgenParam pParam = new ProgenParam();
        //addedby santhosh.k on 08-03-2010
        String[] TimeParametersStr = null;
        ArrayList ReportTimeParams = null;
        ArrayList ReportTimeParamsNames = null;
        //addedby santhosh.k on 08-03-2010
//        repQuery = container.getViewbyqry();
//        
//        repQuery = container.getViewbyqry();
        try {
            if (grpIds != null && (!("".equalsIgnoreCase(grpIds)))) {
                graphIds = grpIds.split(",");
                ParametersHashMap = container.getParametersHashMap();
                TableHashMap = container.getTableHashMap();
                ReportHashMap = container.getReportHashMap();
                GraphHashMap = container.getGraphHashMap();
                GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
                GraphSizesHashMap = (HashMap) session.getAttribute("GraphSizesHashMap");
                GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");
                //added on 01-03-2010 by santhosh.k for displaying graph types in sorted order
                GraphTypesValues = (String[]) (new TreeSet(GraphTypesHashMap.values())).toArray(new String[0]);
                GraphSizesValues = (String[]) (new TreeSet(GraphSizesHashMap.values())).toArray(new String[0]);
                GraphClassesValues = (String[]) (new TreeSet(GraphClassesHashMap.values())).toArray(new String[0]);


                reportName = container.getReportName();//(String) ReportHashMap.get("ReportName");

                Measures = (TableHashMap.get("Measures") != null) ? (ArrayList) TableHashMap.get("Measures") : new ArrayList();
                REP = (TableHashMap.get("REP") != null) ? (ArrayList) TableHashMap.get("REP") : new ArrayList();
                REPNames = (TableHashMap.get("REPNames") != null) ? (ArrayList) TableHashMap.get("REPNames") : new ArrayList();
                CEP = (TableHashMap.get("CEP") != null) ? (ArrayList) TableHashMap.get("CEP") : new ArrayList();
                Parameters = (ParametersHashMap.get("Parameters") != null) ? (ArrayList) ParametersHashMap.get("Parameters") : new ArrayList();
                ParametersNames = (ParametersHashMap.get("ParametersNames") != null) ? (ArrayList) ParametersHashMap.get("ParametersNames") : new ArrayList();
                AllGraphColumns = (GraphHashMap.get("AllGraphColumns") != null) ? (ArrayList) GraphHashMap.get("AllGraphColumns") : new ArrayList();


                paramValues = new HashMap();
                if (ParametersHashMap.get("TimeDetailstList") != null) {
                    TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
                    TimeDetailstList = (ArrayList) ParametersHashMap.get("TimeDetailstList");
                } else {
                    TimeDetailstList.add("Day");
                    TimeDetailstList.add("PRG_STD");
                    TimeDetailstList.add(pParam.getdateforpage().toString());
                    TimeDetailstList.add("Month");
                    TimeDetailstList.add("Last Period");
                    repQuery.avoidProGenTime = true;
                }

                if (REP == null || REP.size() == 0) {
                    REP = new ArrayList();
                    REPNames = new ArrayList();
                    if (Parameters != null && Parameters.size() != 0) {
                        REP.add(String.valueOf(Parameters.get(0)));
                        REPNames.add(String.valueOf(ParametersNames.get(0)));
                    }
                }
                if (REP == null || REP.isEmpty()) {
                    PbReportCollection repCollect = container.getReportCollect();
                    if (repCollect.reportRowViewbyValues != null && !repCollect.reportRowViewbyValues.isEmpty()) {
                        REP = repCollect.reportRowViewbyValues;
                        if (ReportHashMap.containsKey("reportQryElementIds")) {
                            Measures = (ArrayList) ReportHashMap.get("reportQryElementIds");
                        }

                    }

                }


                if (REP.size() != 0 && (Measures.size() != 0 || AllGraphColumns.size() != 0)) {
                    for (int index = 0; index < Parameters.size(); index++) {
                        if (paramValues.get(String.valueOf(Parameters.get(index))) == null) {
                            paramValues.put(String.valueOf(Parameters.get(index)), "All");
                            reportIncomingParameters.put("CBOAPR" + String.valueOf(Parameters.get(index)), null);
                        }
                    }
                    collect.reportIncomingParameters = reportIncomingParameters;
                    collect.reportColViewbyValues = CEP;
                    collect.reportRowViewbyValues = REP;
                    collect.timeDetailsArray = TimeDetailstList;
                    collect.timeDetailsMap = TimeDimHashMap;
                    collect.getParamMetaDataForReportDesigner();

                    //addedby santhosh.k on 08-03-2010
                    if (collect.timeDetailsMap != null) {
                        TimeParametersStr = (String[]) (collect.timeDetailsMap.keySet()).toArray(new String[0]);
                        for (int timeIndex = 0; timeIndex < TimeParametersStr.length; timeIndex++) {
                            ArrayList alist = (ArrayList) collect.timeDetailsMap.get(TimeParametersStr[timeIndex]);
                            if (ReportTimeParams == null) {
                                ReportTimeParams = new ArrayList();
                                ReportTimeParamsNames = new ArrayList();
                            }
                            ReportTimeParams.add(TimeParametersStr[timeIndex]);
                            ReportTimeParamsNames.add(String.valueOf(alist.get(2)));
                            alist = null;
                        }

                        ParametersHashMap.put("TimeParameters", ReportTimeParams);
                        ParametersHashMap.put("TimeParametersNames", ReportTimeParamsNames);
                    }
                    /*
                     * for (int j = 0; j < Measures.size(); j++) { if
                     * (!reportQryElementIds.contains(String.valueOf(Measures.get(j)).replace("A_",
                     * ""))) {
                     * reportQryElementIds.add(String.valueOf(Measures.get(j)).replace("A_",
                     * "")); } }
                     *
                     * if (CEP.size() == 0) { for (int k = 0; k <
                     * AllGraphColumns.size(); k++) { if
                     * (!reportQryElementIds.contains(String.valueOf(AllGraphColumns.get(k)).replace("A_",
                     * ""))) {
                     * reportQryElementIds.add(String.valueOf(AllGraphColumns.get(k)).replace("A_",
                     * "")); } }
                    }
                     */
                    reportQryElementIds = (ArrayList) ReportHashMap.get("reportQryElementIds");
                    reportQryAggregations = (ArrayList) ReportHashMap.get("reportQryAggregations");
                    if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
//                        reportQryAggregations = DAO.getReportQryAggregations(reportQryElementIds);
                        collect.reportQryElementIds = reportQryElementIds;
                        collect.reportQryAggregations = reportQryAggregations;
                        repQuery.setQryColumns(collect.reportQryElementIds);
                        repQuery.setColAggration(collect.reportQryAggregations);
                        repQuery.setDefaultMeasure(String.valueOf(reportQryElementIds.get(0)));
                        repQuery.setDefaultMeasureSumm(String.valueOf(reportQryAggregations.get(0)));
                    }

                    repQuery.setRowViewbyCols(REP);
                    repQuery.setColViewbyCols(CEP);
                    repQuery.setParamValue(container.getReportCollect().reportParametersValues);
                    repQuery.setTimeDetails(TimeDetailstList);
                    repQuery.setReportId(getReportId());
                    if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                        if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("year")) {
                            container.setTimeLevel("YEAR");
                        } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Qtr")) {
                            container.setTimeLevel("QUARTER");
                        } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Month")) {
                            container.setTimeLevel("MONTH");
                        } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Week")) {
                            container.setTimeLevel("WEEK");
                        } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Day")) {
                            container.setTimeLevel("DAY");
                        }
                    } else if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                        container.setTimeLevel("MONTH");
                    } else if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                        container.setTimeLevel("QUARTER");
                    } else if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_QTR_RANGE")) {
                        container.setTimeLevel("YEAR");
                    } else if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                        container.setTimeLevel("YEAR");
                    }
                    repQuery.setBizRoles(getBizRoles());
                    repQuery.setUserId(getUserId());
                    graphIds = grpIds.split(",");
                    for (int i = 0; i < graphIds.length; i++) {
                        HashMap graphDetails = (HashMap) GraphHashMap.get(graphIds[i]);
                        if (graphDetails.get("graphTypeName") != null && graphDetails.get("graphTypeName").toString().equalsIgnoreCase("TimeSeries")) {
                            isTimeSeries = true;
                        }

                    }
                    repQuery.isTimeSeries = isTimeSeries;
                    if (reportQryElementIds != null && reportQryElementIds.size() != 0 && !container.isBusTemplateFromOneview()) {
                        //
                        pbretObj = repQuery.getPbReturnObjectCrossChecked(String.valueOf(reportQryElementIds.get(0)), container);
                        // 
                    } else {
                        pbretObj = (PbReturnObject) container.getGrret();
                        repQuery = container.getViewbyqry();
                    }
                }

                HashMap[] graphMapDetails = new HashMap[graphIds.length];
                HashMap[] CrossTabGraphMapDetails = new HashMap[graphIds.length];

                if (pbretObj != null) {
                    if (CEP == null || CEP.isEmpty()) {
                        if (container.getGrret() != null) {
                            pbretObj = (PbReturnObject) container.getGrret();
                        }
                        if (container.getRefreshGr() != null && !container.equals("null")) {
                            if (!container.getRefreshGr().equalsIgnoreCase("true")) {
                                container.setRetObj(pbretObj);
                            }
                        }
                    }
                    AllGraphColumns = (ArrayList) GraphHashMap.get("AllGraphColumns");
                    ArrayList originalColumns = new ArrayList();
                    if (CEP != null && CEP.size() != 0) {
                        originalColumns = new ArrayList();

                        dbColumns = pbretObj.getColumnNames();
                        int graphColcount = 0;
                        if (dbColumns.length <= 10) {
                            graphColcount = dbColumns.length;
                        } else {
                            graphColcount = 10;
                        }
                        axis = new String[graphColcount];
                        for (int colIndex = 0; colIndex < graphColcount; colIndex++) {
                            originalColumns.add(dbColumns[colIndex]);
                            axis[colIndex] = "0";
                        }
                        //  
                    } else {
                        originalColumns = new ArrayList();
                        if (REP != null && REP.size() != 0) {
                            for (int i = 0; i < REP.size(); i++) {
                                if (String.valueOf(REP.get(i)).equalsIgnoreCase("Time")) {
                                    originalColumns.add(String.valueOf(REP.get(i)));
                                } else {
                                    originalColumns.add("A_" + String.valueOf(REP.get(i)));
                                }
                            }
                        } else {
                            if (Parameters != null && Parameters.size() != 0) {
                                if (String.valueOf(Parameters.get(0)).equalsIgnoreCase("Time")) {
                                    originalColumns.add(String.valueOf(Parameters.get(0)));
                                } else {
                                    originalColumns.add("A_" + String.valueOf(Parameters.get(0)));
                                }
                            }
                        }

                        if (AllGraphColumns != null && AllGraphColumns.size() != 0) {
                            for (int i = 0; i < AllGraphColumns.size(); i++) {
                                if (!originalColumns.contains("A_" + String.valueOf(AllGraphColumns.get(i)).replace("A_", ""))) {
                                    originalColumns.add("A_" + String.valueOf(AllGraphColumns.get(i)).replace("A_", ""));
                                }
                            }
                        } else {
                            if (Measures != null && Measures.size() != 0) {
                                if (!originalColumns.contains("A_" + String.valueOf(Measures.get(0)).replace("A_", ""))) {
                                    originalColumns.add("A_" + String.valueOf(Measures.get(0)).replace("A_", ""));
                                }
                            }
                        }
                    }


                    GraphDisplay.setTimelevel(container.getTimeLevel());
                    GraphDisplay.setCurrentDispRetObjRecords(pbretObj);
                    GraphDisplay.setCurrentDispRecordsRetObjWithGT(pbretObj);
                    GraphDisplay.setAllDispRecordsRetObj(pbretObj);
                    GraphDisplay.setSession(request.getSession(false));
                    GraphDisplay.setResponse(response);
                    GraphDisplay.setOut(response.getWriter());
                    GraphDisplay.setReportId("");
                    GraphDisplay.setJscal(null);

                    String[] viewBys = null;
                    String[] viewBysDispNames = null;
                    String[] barChartColumnTitles = null;
                    String[] barChartColumnNames = null;
                    String[] pieChartColumns = null;
                    String[] barChartColumnNames1 = null;
                    String[] barChartColumnNames2 = null;
                    String[] barChartColumnTitles1 = null;
                    String[] barChartColumnTitles2 = null;

                    if (graphIds.length != 0) {
                        CrossTabGraphHashMap = new HashMap();
                        // 
                        for (int i = 0; i < graphMapDetails.length; i++) {
                            graphMapDetails[i] = (HashMap) GraphHashMap.get(graphIds[i]);
                            HashMap graphDetailsforallRows = (HashMap) GraphHashMap.get(graphIds[i]);
//                            ////.println("graphDetailsforallRows.get(GraphDisplayRows)=== " + graphDetailsforallRows.get("graphDisplayRows"));
                            graphMapDetails[i].put("graphDisplayRows", graphDetailsforallRows.get("graphDisplayRows"));
                            GraphDisplay.setStartindex("0");
                            GraphDisplay.setEndindex(String.valueOf(graphDetailsforallRows.get("graphDisplayRows")));
                            if (CEP != null && CEP.size() != 0) {
                                CrossTabGraphMapDetails[i] = (HashMap) graphMapDetails[i].clone();
                                if (viewBys == null || viewBysDispNames == null) {
                                    viewBys = (String[]) REP.toArray(new String[0]);
                                    viewBysDispNames = (String[]) REPNames.toArray(new String[0]);

                                    if (CrossTabGraphMapDetails[i].get("viewByElementIds") != null) {
                                        for (int index = 0; index < viewBys.length; index++) {
                                            viewBys[index] = String.valueOf(originalColumns.get(index));
                                        }
                                    } else {
                                        viewBys = (String[]) REP.toArray(new String[0]);
                                    }
                                    viewBysDispNames = (String[]) REPNames.toArray(new String[0]);
                                }

                                barChartColumnNames = (String[]) originalColumns.toArray(new String[0]);
                                pieChartColumns = (String[]) originalColumns.toArray(new String[0]);
                                CrossTabGraphMapDetails[i].put("viewByElementIds", viewBys);
                                CrossTabGraphMapDetails[i].put("barChartColumnNames", barChartColumnNames);
                                CrossTabGraphMapDetails[i].put("pieChartColumns", pieChartColumns);
                                CrossTabGraphMapDetails[i].put("axis", axis);

//                                
//                                
//                                
                                barChartColumnTitles = (String[]) originalColumns.toArray(new String[0]);

                                for (int temp1 = 0; temp1 < viewBysDispNames.length; temp1++) {
                                    barChartColumnTitles[temp1] = viewBysDispNames[temp1];
                                }
                                // 
                                for (int temp2 = viewBysDispNames.length; temp2 < barChartColumnTitles.length; temp2++) {
                                    barChartColumnTitles[temp2] = String.valueOf(repQuery.crossTabNonViewByMap.get(String.valueOf(originalColumns.get(temp2))));

                                }
                                CrossTabGraphMapDetails[i].put("barChartColumnTitles", barChartColumnTitles);
                                CrossTabGraphHashMap.put(graphIds[i], CrossTabGraphMapDetails[i]);

                            } else {

                                if (REP != null && REP.size() != 0) {
                                    viewBys = (String[]) REP.toArray(new String[0]);
                                    viewBysDispNames = (String[]) REPNames.toArray(new String[0]);
                                } else {
                                    viewBys = new String[1];
                                    viewBysDispNames = new String[1];
                                    viewBys[0] = String.valueOf(Parameters.get(0));
                                    viewBysDispNames[0] = (String) ParametersNames.toArray(new String[0])[0];
                                }
                                if (graphMapDetails[i].get("barChartColumnNames") != null) {
                                    barChartColumnNames = (String[]) graphMapDetails[i].get("barChartColumnNames");
                                    barChartColumnTitles = new String[barChartColumnNames.length];
                                    axis = new String[barChartColumnNames.length];

                                    for (int temp1 = 0; temp1 < viewBysDispNames.length; temp1++) {
                                        barChartColumnTitles[temp1] = viewBysDispNames[temp1];
                                        axis[temp1] = axis[temp1] != null ? axis[temp1] : "0";
                                    }
                                    for (int temp2 = viewBys.length; temp2 < barChartColumnNames.length; temp2++) {
                                        barChartColumnTitles[temp2] = String.valueOf(repQuery.NonViewByMap.get(barChartColumnNames[temp2]));
                                        axis[temp2] = axis[temp2] != null ? axis[temp2] : "0";
                                    }
                                    graphMapDetails[i].put("barChartColumnTitles", barChartColumnTitles);
                                    if (graphMapDetails[i].get("viewByElementIds") == null) {
                                        String[] tempViewBys = new String[viewBys.length];
                                        for (int x = 0; x < viewBys.length; x++) {
                                            if (viewBys[x].equalsIgnoreCase("TIME")) {
                                                tempViewBys[x] = "TIME";
                                            } else if (viewBys[x].startsWith("A_")) {
                                                tempViewBys[x] = viewBys[x];
                                            } else {
                                                tempViewBys[x] = "A_" + viewBys[x];
                                            }
                                        }
                                        graphMapDetails[i].put("viewByElementIds", tempViewBys);
                                    }
                                } else {
                                    barChartColumnNames = new String[viewBys.length + 1];
                                    barChartColumnTitles = new String[viewBys.length + 1];
                                    for (int index = 0; index < viewBys.length; index++) {
                                        barChartColumnNames[index] = String.valueOf(originalColumns.get(index));
                                        barChartColumnTitles[index] = viewBysDispNames[index];
                                    }
                                    for (int index = viewBys.length; index < barChartColumnNames.length; index++) {
                                        barChartColumnNames[index] = String.valueOf(originalColumns.get(index));
                                        barChartColumnTitles[index] = String.valueOf(repQuery.NonViewByMap.get(barChartColumnNames[index]));
                                    }
                                    barChartColumnTitles1 = new String[1 + viewBys.length];
                                    barChartColumnNames1 = new String[barChartColumnTitles1.length];
                                    barChartColumnTitles2 = new String[(barChartColumnNames.length - 1)];
                                    barChartColumnNames2 = new String[barChartColumnTitles2.length];

                                    for (int j = 0; j < viewBys.length; j++) {
                                        barChartColumnTitles1[j] = barChartColumnTitles[j];
                                        barChartColumnTitles2[j] = barChartColumnTitles[j];
                                        barChartColumnNames1[j] = barChartColumnNames[j];
                                        barChartColumnNames2[j] = barChartColumnNames[j];
                                    }
                                    for (int j = viewBys.length; j < barChartColumnTitles1.length; j++) {
                                        barChartColumnTitles1[j] = barChartColumnTitles[j];
                                        barChartColumnNames1[j] = barChartColumnNames[j];
                                    }
                                    for (int j = barChartColumnTitles1.length; j < barChartColumnTitles.length; j++) {
                                        barChartColumnTitles2[j - viewBys.length] = barChartColumnTitles[j];
                                        barChartColumnNames2[j - viewBys.length] = barChartColumnNames[j];
                                    }
                                    graphMapDetails[i].put("viewByElementIds", viewBys);
                                    graphMapDetails[i].put("barChartColumnNames", barChartColumnNames);
                                    graphMapDetails[i].put("pieChartColumns", barChartColumnNames);
                                    graphMapDetails[i].put("barChartColumnTitles", barChartColumnTitles);
                                    graphMapDetails[i].put("barChartColumnNames1", barChartColumnNames1);
                                    graphMapDetails[i].put("barChartColumnTitles1", barChartColumnTitles1);
                                    graphMapDetails[i].put("barChartColumnNames2", barChartColumnNames2);
                                    graphMapDetails[i].put("barChartColumnTitles2", barChartColumnTitles2);
                                }
                                if (graphMapDetails[i].get("barChartColumnNames1") != null) {
                                    barChartColumnNames1 = (String[]) graphMapDetails[i].get("barChartColumnNames1");
                                    barChartColumnTitles1 = new String[barChartColumnNames1.length];
                                    for (int temp1 = 0; temp1 < viewBysDispNames.length; temp1++) {
                                        barChartColumnTitles1[temp1] = viewBysDispNames[temp1];
                                    }
                                    for (int temp2 = viewBys.length; temp2 < barChartColumnNames1.length; temp2++) {
                                        barChartColumnTitles1[temp2] = String.valueOf(repQuery.NonViewByMap.get(barChartColumnNames1[temp2]));
                                    }
                                    graphMapDetails[i].put("barChartColumnTitles1", barChartColumnTitles1);
                                }
                                if (graphMapDetails[i].get("barChartColumnNames2") != null) {
                                    barChartColumnNames2 = (String[]) graphMapDetails[i].get("barChartColumnNames2");
                                    barChartColumnTitles2 = new String[barChartColumnNames2.length];
                                    for (int temp1 = 0; temp1 < viewBysDispNames.length; temp1++) {
                                        barChartColumnTitles2[temp1] = viewBysDispNames[temp1];
                                    }
                                    for (int temp2 = viewBys.length; temp2 < barChartColumnNames2.length; temp2++) {
                                        barChartColumnTitles2[temp2] = String.valueOf(repQuery.NonViewByMap.get(barChartColumnNames2[temp2]));
                                    }
                                    graphMapDetails[i].put("barChartColumnTitles2", barChartColumnTitles2);
                                }

                            }
                        }
                        container.setCrossTabGraphHashMap(CrossTabGraphHashMap);
                        GraphDisplay.setViewByColNames(viewBysDispNames);
                        GraphDisplay.setViewByElementIds(viewBys);
                        if (CEP != null && CEP.size() != 0) {
                            GraphDisplay.setGraphMapDetails(CrossTabGraphMapDetails);
                            //

                        } else {
                            GraphDisplay.setGraphMapDetails(graphMapDetails);
                            //
                        }
                        ProgenChartDisplay[] pcharts = null;
                        if (getIsFxCharts()) {
                        } else {
                            grpDetails = GraphDisplay.getGraphHeadersByGraphMap();
                            pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
                        }
                        graphsBuffer.append("<Table width=\"100%\" border='0'  >");
                        graphsBuffer.append("<Tr>");
                        for (int grpCnt = 0; grpCnt < graphIds.length; grpCnt++) {
                            if (getIsFxCharts()) {
                            } else {
                                pcharts[grpCnt].setCtxPath(request.getContextPath());
                                pcharts[grpCnt].setSwapColumn(true);
                            }
                            String grpType = "";
                            String grpSize = "";
                            String graphName = "";
                            graphMapDetails[grpCnt] = (HashMap) GraphHashMap.get(graphIds[grpCnt]);
                            grpType = String.valueOf(graphMapDetails[grpCnt].get("graphTypeName"));
                            grpSize = String.valueOf(graphMapDetails[grpCnt].get("graphSize"));
                            graphName = String.valueOf(graphMapDetails[grpCnt].get("graphName"));

                            graphsBuffer.append("<Td valign='top'  width=\"" + (100 / graphIds.length) + "%\">");
                            graphsBuffer.append("<Table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                            graphsBuffer.append("<Tr>");//start of 1st row
                            graphsBuffer.append("<Td>");

                            graphsBuffer.append("<div   class=\"portlet-header ui-widget-header ui-corner-all \">" + graphName + "</div>");//portlet-header div starts here
                            graphsBuffer.append("</Td>");
                            graphsBuffer.append("</Tr>");

                            graphsBuffer.append("<Tr>");//start of 2nd row
                            graphsBuffer.append("<Td>");//start of 2nd row 1st td

                            graphsBuffer.append("<Table class=\"progenTable\"  border='0' align=\"left\">");//start of outer table
                            graphsBuffer.append("<Tr>");

                            //start of graph types

                            graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphTypesDisp(document.getElementById('dispgrptypes" + graphIds[grpCnt] + "'))\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Types\"> Graph Types </a>");
                            graphsBuffer.append("<div class=\"flagDiv\" style=\"display:none;height:200px\"  id='dispgrptypes" + graphIds[grpCnt] + "'>");

                            graphsBuffer.append("<Table>");//start of inner table
                            for (int gtype = 0; gtype < GraphTypesValues.length; gtype++) {
                                if (grpType.equalsIgnoreCase(GraphTypesValues[gtype])) {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpType + "'>");
                                    graphsBuffer.append("<b>" + grpType + "</b>");
                                    graphsBuffer.append("</Td>");
                                    graphsBuffer.append("</Tr>");
                                } else {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpType + "'>");
                                    graphsBuffer.append("<a href='javascript:void(0)' title='" + GraphTypesValues[gtype] + "' onclick=\"changeGrpType('" + GraphTypesValues[gtype] + "','" + graphIds[grpCnt] + "','" + grpIds + "')\">" + GraphTypesValues[gtype] + "</a>");
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
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphSizesDisp(document.getElementById('dispgrpsizes" + graphIds[grpCnt] + "'))\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Sizes\"> Graph Sizes </a>");
                            graphsBuffer.append("<div  class=\"flagDiv\" style=\"display:none\"   id='dispgrpsizes" + graphIds[grpCnt] + "'>");

                            graphsBuffer.append("<Table>");//start of inner table
                            for (int gsize = 0; gsize < GraphSizesValues.length; gsize++) {
                                if (grpSize.equalsIgnoreCase(GraphSizesValues[gsize])) {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpSize + "'>");
                                    graphsBuffer.append("<b>" + grpSize + "</b>");
                                    graphsBuffer.append("</Td>");
                                    graphsBuffer.append("</Tr>");
                                } else {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpSize + "'>");
                                    graphsBuffer.append("<a href='javascript:void(0)' title='" + GraphSizesValues[gsize] + "' onclick=\"changeGrpSize('" + GraphSizesValues[gsize] + "','" + graphIds[grpCnt] + "','" + grpIds + "')\">" + GraphSizesValues[gsize] + "</a>");
                                    graphsBuffer.append("</Td>");
                                    graphsBuffer.append("</Tr>");
                                }
                            }
                            graphsBuffer.append("</Table>");//closing of inner table
                            graphsBuffer.append(" </div>");
                            graphsBuffer.append("</Td>");
                            //end of graph sizes


                            //start of graph columns
                            if (CEP == null || CEP.size() == 0) {
                                graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                                graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphColumnsDisp('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Columns\"> Graph Columns </a>");

                            }
                            //end of graph columns

                            //start of delete .16
                            graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onClick=\"deleteGraph('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Delete Chart\">Delete Chart </a>");
                            //end of delete graph

                            //start of  graph details
                            graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onClick=\"dispGraphDetails('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Delete Chart\">Graph Details</a>");
                            //end of  graph details

                            //start of Row values added by santhosh.kumar@progenbusiness.com on 24/12/2009
                            if (grpType.equalsIgnoreCase("ColumnPie") || grpType.equalsIgnoreCase("ColumnPie3D") || grpType.equalsIgnoreCase("Spider")) {
                                graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                                graphsBuffer.append("<Td><a  href='javascript:void(0)' onClick=\"dispRowValues('dispRowValues" + grpCnt + "','rowValues" + grpCnt + "','" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Column Pie rows\">Rows</a>");
                                graphsBuffer.append("<div  class=\"flagDiv\" style=\"display:none;height:200px;\"    id='dispRowValues" + grpCnt + "'>");

                                graphsBuffer.append("<Table>");//start of inner table
                                StringBuffer sbuffer = null;
                                ArrayList RowValues = null;
                                RowValues = (ArrayList) graphMapDetails[grpCnt].get("RowValuesList");

                                if (RowValues == null) {
                                    RowValues = new ArrayList();
                                }
                                for (int rowCnt = 0; rowCnt < pbretObj.getRowCount(); rowCnt++) {
                                    sbuffer = new StringBuffer("");
                                    for (int j = 0; j < viewBysDispNames.length; j++) {
                                        sbuffer.append("-" + pbretObj.getFieldValueString(rowCnt, barChartColumnNames[j]));
                                    }
                                    if (RowValues.size() == 0) {
                                        RowValues.add(sbuffer.toString().substring(1));
                                    }
                                    graphsBuffer.append("<Tr>");
                                    if (RowValues.contains(sbuffer.toString().substring(1))) {
                                        graphsBuffer.append("<Td><input type=\"checkbox\" checked value='" + sbuffer.toString().substring(1) + "' id='rowValues" + rowCnt + "' name='rowValues" + grpCnt + "' >" + sbuffer.toString().substring(1) + "</Td>");
                                    } else {
                                        graphsBuffer.append("<Td><input type=\"checkbox\"  value='" + sbuffer.toString().substring(1) + "' id='rowValues" + rowCnt + "' name='rowValues" + grpCnt + "' >" + sbuffer.toString().substring(1) + "</Td>");
                                    }
                                    graphsBuffer.append("</Tr>");
                                }
                                graphsBuffer.append("</Table>");//closing of inner table
                                graphsBuffer.append(" </div>");
                                graphsBuffer.append("</Td>");
                            }

                            //end of row values

                            graphsBuffer.append("</Tr>");
                            graphsBuffer.append("</Table>");//closing of outer table

                            graphsBuffer.append("</Td>");//closing of 2nd row 1st td
                            graphsBuffer.append("</Tr>");//end of 2nd row

                            graphsBuffer.append("<Tr>");//start of 3rd row
                            graphsBuffer.append("<Td align='left'>");
                            graphsBuffer.append("<div  align='left'   class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\">");//portlet div starts here

                            graphsBuffer.append("<div align='left' style=\"width:100%\"   >");
                            if (getIsFxCharts()) {
                                graphsBuffer.append("<iframe frameborder='0' scrolling='No' NAME='iframe_" + graphIds[grpCnt] + "_" + request.getParameter("REPORTID") + "'  id='iframe_" + graphIds[grpCnt] + "_" + request.getParameter("REPORTID") + "' STYLE='width:100%;height:100%;overflow:auto;' src='" + request.getContextPath() + "/PbFXGraphXMLBuild.jsp?REPORTID=" + request.getParameter("REPORTID") + "&graphId=" + graphIds[grpCnt] + "'></iframe>");

                            } else {
                                if (pcharts[grpCnt].chartDisplay.equalsIgnoreCase("")) {
                                    graphsBuffer.append("<SCRIPT LANGUAGE=\"JavaScript\" SRC=\"" + request.getContextPath() + "/overlib.js\">  </SCRIPT>");
                                    graphsBuffer.append("<div  align='left' id=\"overDiv\" style=\"position:absolute; visibility:hidden; z-index:1000;\"></div>");
                                    graphsBuffer.append("<img  src=\"" + request.getContextPath() + "/images/" + grpType + ".gif\" border='0' align='top' > </img>");
                                    graphsBuffer.append(pcharts[grpCnt].chartDisplay);
                                } else {
                                    graphsBuffer.append(pcharts[grpCnt].chartDisplay);
                                }
                            }

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
                } else {

                    if (graphIds.length != 0) {
                        graphMapDetails = new HashMap[graphIds.length];

                        graphsBuffer.append("<Table width=\"100%\" border='0'  >");
                        graphsBuffer.append("<Tr>");
                        for (int grpCnt = 0; grpCnt < graphMapDetails.length; grpCnt++) {
                            String grpType = "";
                            String grpSize = "";
                            String graphName = "";
                            graphMapDetails[grpCnt] = (HashMap) GraphHashMap.get(graphIds[grpCnt]);
                            grpType = String.valueOf(graphMapDetails[grpCnt].get("graphTypeName"));
                            grpSize = String.valueOf(graphMapDetails[grpCnt].get("graphSize"));
                            graphName = String.valueOf(graphMapDetails[grpCnt].get("graphName"));

                            graphsBuffer.append("<Td valign='top'  width=\"" + (100 / graphMapDetails.length) + "%\">");
                            graphsBuffer.append("<Table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                            graphsBuffer.append("<Tr>");//start of 1st row
                            graphsBuffer.append("<Td>");
                            graphsBuffer.append("<div   class=\"portlet-header ui-widget-header ui-corner-all \">" + graphName + "</div>");//portlet-header div starts here
                            graphsBuffer.append("</Td>");
                            graphsBuffer.append("</Tr>");


                            graphsBuffer.append("<Tr>");//start of 2nd row
                            graphsBuffer.append("<Td>");//start of 2nd row 1st td

                            graphsBuffer.append("<Table class=\"progenTable\"  border='0' align=\"left\">");//start of outer table
                            graphsBuffer.append("<Tr>");


                            //start of graph types

                            graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphTypesDisp(document.getElementById('dispgrptypes" + grpCnt + "'))\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Types\"> Graph Types </a>");
                            graphsBuffer.append("<div class=\"flagDiv\" style=\"display:none;height:200px;\"     id='dispgrptypes" + grpCnt + "'>");

                            graphsBuffer.append("<Table>");//start of inner table
                            for (int gtype = 0; gtype < GraphTypesValues.length; gtype++) {

                                if (grpType.equalsIgnoreCase(GraphTypesValues[gtype])) {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpType + "'>");
                                    graphsBuffer.append("<b>" + grpType + "</b>");
                                    graphsBuffer.append("</Td>");
                                    graphsBuffer.append("</Tr>");
                                } else {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpType + "'>");
                                    graphsBuffer.append("<a href='javascript:void(0)' title='" + GraphTypesValues[gtype] + "' onclick=\"changeGrpType('" + GraphTypesValues[gtype] + "','" + graphIds[grpCnt] + "','" + grpIds + "')\">" + GraphTypesValues[gtype] + "</a>");
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
                            graphsBuffer.append("<div class=\"flagDiv\" style=\"display:none;\"   id='dispgrpsizes" + grpCnt + "'>");

                            graphsBuffer.append("<Table>");//start of inner table
                            for (int gsize = 0; gsize < GraphSizesValues.length; gsize++) {

                                if (grpSize.equalsIgnoreCase(GraphSizesValues[gsize])) {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpSize + "'>");
                                    graphsBuffer.append("<b>" + grpSize + "</b>");
                                    graphsBuffer.append("</Td>");
                                    graphsBuffer.append("</Tr>");
                                } else {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpSize + "'>");
                                    graphsBuffer.append("<a href='javascript:void(0)' title='" + GraphSizesValues[gsize] + "' onclick=\"changeGrpSize('" + GraphSizesValues[gsize] + "','" + graphIds[grpCnt] + "','" + grpIds + "')\">" + GraphSizesValues[gsize] + "</a>");
                                    graphsBuffer.append("</Td>");
                                    graphsBuffer.append("</Tr>");
                                }

                            }
                            graphsBuffer.append("</Table>");//closing of inner table
                            graphsBuffer.append(" </div>");
                            graphsBuffer.append("</Td>");

                            //end of graph sizes
                            //start of graph columns
                            if (CEP == null || CEP.size() == 0) {
                                graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                                graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphColumnsDisp('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Columns\"> Graph Columns </a>");

                            }
                            //end of graph columns

                            //start of delete graph
                            graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onClick=\"deleteGraph('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Delete Chart\">Delete Chart </a>");
                            //end of delete graph

                            //start filter
                            graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onClick=\"applyFilter()\" style='text-decoration:none' class=\"calcTitle\" title=\"Apply Filter \">Filter</a>");
                            //end filter
                            //start of  graph details
                            graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                            //graphsBuffer.append("<Td><a  href='javascript:void(0)' onClick=\"dispGraphDetails('" + graphIds[grpCnt] + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Delete Chart\">Graph Details</a>");
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onClick=\"dispGraphDetails('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Delete Chart\">Graph Details</a>");

                            //end of  graph details

                            graphsBuffer.append("</Tr>");
                            graphsBuffer.append("</Table>");//closing of outer table

                            graphsBuffer.append("</Td>");//closing of 2nd row 1st td
                            graphsBuffer.append("</Tr>");//end of 2nd row

                            graphsBuffer.append("<Tr>");//start of 3rd row
                            graphsBuffer.append("<Td align='left'>");
                            graphsBuffer.append("<div  align='left' style=\"height:300px\"   class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\">");//portlet div starts here

                            graphsBuffer.append("<table align=\"center\" style=\"width:100%;height:100%\">");
                            graphsBuffer.append("<tr align=\"center\" valign=\"middle\">");
                            graphsBuffer.append("<td align=\"center\" valign=\"middle\"> No data to display</td>");
                            graphsBuffer.append("</tr>");
                            graphsBuffer.append(" </table>");
                            /*
                             * graphsBuffer.append("<div align='left'
                             * style=\"height:auto;width:" + (90 /
                             * graphMapDetails.length) + "%\" >");
                             * graphsBuffer.append("<SCRIPT
                             * LANGUAGE=\"JavaScript\" SRC=\"" +
                             * request.getContextPath() + "/overlib.js\">
                             * </SCRIPT>"); graphsBuffer.append("<div
                             * align='left' id=\"overDiv\"
                             * style=\"position:absolute; visibility:hidden;
                             * z-index:1000;\"></div>");
                             * graphsBuffer.append("<img height=\"172px\"
                             * width=\"372px\" src=\"" +
                             * request.getContextPath() + "/images/" + grpType +
                             * ".gif\" border='0' align='top' > </img>");
                             * graphsBuffer.append("</div >");//portlet div ends
                             * here
                             */

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
            } else {
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return graphsBuffer.toString();
    }

    //for Graph and Table in RD
    public String buildGraphTable(Container container, HttpServletRequest request, HttpServletResponse response, String grpIds) {
        ArrayList grpDetails = new ArrayList();
        PbGraphDisplay GraphDisplay = new PbGraphDisplay();
        GraphDisplay.setCtxPath(request.getContextPath());
        StringBuffer graphsBuffer = new StringBuffer("");
        PbReturnObject pbretObj = null;
        String reportName = "";
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        HashMap ReportHashMap = null;
        HashMap GraphHashMap = null;
        HashMap CrossTabGraphHashMap = null;
        HashMap GraphSizesHashMap = null;
        HashMap GraphTypesHashMap = null;
        HashMap GraphClassesHashMap = null;
        //String[] GraphSizesKeySet = null;
        //String[] GraphTypesKeySet = null;
        String[] dbColumns = null;
        String[] axis = null;
        String[] graphIds = null;
        ArrayList Measures = null;
        ArrayList REP = null;
        ArrayList CEP = null;
        ArrayList Parameters = null;
        ArrayList REPNames = null;
        ArrayList ParametersNames = null;
        HttpSession session = request.getSession(false);
        PbReportCollection collect = new PbReportCollection();
        PbReportQuery repQuery = new PbReportQuery();
        HashMap paramValues = null;
        HashMap TimeDimHashMap = null;
        ArrayList TimeDetailstList = null;
        ArrayList AllGraphColumns = null;
        HashMap reportIncomingParameters = new HashMap();
        ArrayList reportQryElementIds = new ArrayList();
        ArrayList reportQryAggregations = new ArrayList();
        boolean isTimeSeries = false;

        String[] GraphSizesValues = null;
        String[] GraphTypesValues = null;
        String[] GraphClassesValues = null;

        //addedby santhosh.k on 08-03-2010
        String[] TimeParametersStr = null;
        ArrayList ReportTimeParams = null;
        ArrayList ReportTimeParamsNames = null;
        //addedby santhosh.k on 08-03-2010
        try {
            if (grpIds != null && (!("".equalsIgnoreCase(grpIds)))) {
                graphIds = grpIds.split(",");
                ParametersHashMap = container.getParametersHashMap();
                TableHashMap = container.getTableHashMap();
                ReportHashMap = container.getReportHashMap();
                GraphHashMap = container.getGraphHashMap();
                GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
                GraphSizesHashMap = (HashMap) session.getAttribute("GraphSizesHashMap");
                GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");
                //added on 01-03-2010 by santhosh.k for displaying graph types in sorted order
                GraphTypesValues = (String[]) (new TreeSet(GraphTypesHashMap.values())).toArray(new String[0]);
                GraphSizesValues = (String[]) (new TreeSet(GraphSizesHashMap.values())).toArray(new String[0]);
                GraphClassesValues = (String[]) (new TreeSet(GraphClassesHashMap.values())).toArray(new String[0]);


                reportName = container.getReportName();//(String) ReportHashMap.get("ReportName");

                Measures = (TableHashMap.get("Measures") != null) ? (ArrayList) TableHashMap.get("Measures") : new ArrayList();
                REP = (TableHashMap.get("REP") != null) ? (ArrayList) TableHashMap.get("REP") : new ArrayList();
                REPNames = (TableHashMap.get("REPNames") != null) ? (ArrayList) TableHashMap.get("REPNames") : new ArrayList();
                CEP = (TableHashMap.get("CEP") != null) ? (ArrayList) TableHashMap.get("CEP") : new ArrayList();
                Parameters = (ParametersHashMap.get("Parameters") != null) ? (ArrayList) ParametersHashMap.get("Parameters") : new ArrayList();
                ParametersNames = (ParametersHashMap.get("ParametersNames") != null) ? (ArrayList) ParametersHashMap.get("ParametersNames") : new ArrayList();
                AllGraphColumns = (GraphHashMap.get("AllGraphColumns") != null) ? (ArrayList) GraphHashMap.get("AllGraphColumns") : new ArrayList();


                paramValues = new HashMap();
                TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
                TimeDetailstList = (ArrayList) ParametersHashMap.get("TimeDetailstList");


                if (REP == null || REP.size() == 0) {
                    REP = new ArrayList();
                    REPNames = new ArrayList();
                    if (Parameters != null && Parameters.size() != 0) {
                        REP.add(String.valueOf(Parameters.get(0)));
                        REPNames.add(String.valueOf(ParametersNames.get(0)));
                    }
                }

                if (REP.size() != 0 && (Measures.size() != 0 || AllGraphColumns.size() != 0)) {
                    for (int index = 0; index < Parameters.size(); index++) {
                        if (paramValues.get(String.valueOf(Parameters.get(index))) == null) {
                            paramValues.put(String.valueOf(Parameters.get(index)), "All");
                            reportIncomingParameters.put("CBOAPR" + String.valueOf(Parameters.get(index)), null);
                        }
                    }
                    collect.reportIncomingParameters = reportIncomingParameters;
                    collect.reportColViewbyValues = CEP;
                    collect.reportRowViewbyValues = REP;
                    collect.timeDetailsArray = TimeDetailstList;
                    collect.timeDetailsMap = TimeDimHashMap;
                    collect.getParamMetaDataForReportDesigner();

                    //addedby santhosh.k on 08-03-2010
                    TimeParametersStr = (String[]) (collect.timeDetailsMap.keySet()).toArray(new String[0]);
                    for (int timeIndex = 0; timeIndex < TimeParametersStr.length; timeIndex++) {
                        ArrayList alist = (ArrayList) collect.timeDetailsMap.get(TimeParametersStr[timeIndex]);
                        if (ReportTimeParams == null) {
                            ReportTimeParams = new ArrayList();
                            ReportTimeParamsNames = new ArrayList();
                        }
                        ReportTimeParams.add(TimeParametersStr[timeIndex]);
                        ReportTimeParamsNames.add(String.valueOf(alist.get(2)));
                        alist = null;
                    }

                    ParametersHashMap.put("TimeParameters", ReportTimeParams);
                    ParametersHashMap.put("TimeParametersNames", ReportTimeParamsNames);

                    /*
                     * for (int j = 0; j < Measures.size(); j++) { if
                     * (!reportQryElementIds.contains(String.valueOf(Measures.get(j)).replace("A_",
                     * ""))) {
                     * reportQryElementIds.add(String.valueOf(Measures.get(j)).replace("A_",
                     * "")); } }
                     *
                     * if (CEP.size() == 0) { for (int k = 0; k <
                     * AllGraphColumns.size(); k++) { if
                     * (!reportQryElementIds.contains(String.valueOf(AllGraphColumns.get(k)).replace("A_",
                     * ""))) {
                     * reportQryElementIds.add(String.valueOf(AllGraphColumns.get(k)).replace("A_",
                     * "")); } }
                    }
                     */
                    reportQryElementIds = (ArrayList) ReportHashMap.get("reportQryElementIds");
                    reportQryAggregations = (ArrayList) ReportHashMap.get("reportQryAggregations");
                    if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
//                        reportQryAggregations = DAO.getReportQryAggregations(reportQryElementIds);
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
                    repQuery.setTimeDetails(TimeDetailstList);
                    repQuery.setReportId(getReportId());
                    if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                        if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("year")) {
                            container.setTimeLevel("YEAR");
                        } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Qtr")) {
                            container.setTimeLevel("QUARTER");
                        } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Month")) {
                            container.setTimeLevel("MONTH");
                        } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Week")) {
                            container.setTimeLevel("WEEK");
                        } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Day")) {
                            container.setTimeLevel("DAY");
                        }
                    } else if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                        container.setTimeLevel("MONTH");
                    } else if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                        container.setTimeLevel("QUARTER");
                    } else if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_QTR_RANGE")) {
                        container.setTimeLevel("YEAR");
                    } else if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                        container.setTimeLevel("YEAR");
                    }
                    repQuery.setBizRoles(getBizRoles());
                    repQuery.setUserId(getUserId());
                    graphIds = grpIds.split(",");
                    for (int i = 0; i < graphIds.length; i++) {
                        HashMap graphDetails = (HashMap) GraphHashMap.get(graphIds[i]);
                        if (graphDetails.get("graphTypeName") != null && graphDetails.get("graphTypeName").toString().equalsIgnoreCase("TimeSeries")) {
                            isTimeSeries = true;
                        }

                    }
                    repQuery.isTimeSeries = isTimeSeries;
                    if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
                        pbretObj = repQuery.getPbReturnObjectCrossChecked(String.valueOf(reportQryElementIds.get(0)), container);
                        //  
                    }
                }

                HashMap[] graphMapDetails = new HashMap[graphIds.length];
                HashMap[] CrossTabGraphMapDetails = new HashMap[graphIds.length];

                if (pbretObj != null) {
                    container.setRetObj(pbretObj);
                    AllGraphColumns = (ArrayList) GraphHashMap.get("AllGraphColumns");
                    ArrayList originalColumns = new ArrayList();
                    if (CEP != null && CEP.size() != 0) {
                        originalColumns = new ArrayList();
                        dbColumns = pbretObj.getColumnNames();
                        int graphColcount = 0;
                        if (dbColumns.length <= 10) {
                            graphColcount = dbColumns.length;
                        } else {
                            graphColcount = 10;
                        }
                        axis = new String[graphColcount];
                        for (int colIndex = 0; colIndex < graphColcount; colIndex++) {
                            originalColumns.add(dbColumns[colIndex]);
                            axis[colIndex] = "0";
                        }
                    } else {
                        originalColumns = new ArrayList();
                        if (REP != null && REP.size() != 0) {
                            for (int i = 0; i < REP.size(); i++) {
                                if (String.valueOf(REP.get(i)).equalsIgnoreCase("Time")) {
                                    originalColumns.add(String.valueOf(REP.get(i)));
                                } else {
                                    originalColumns.add("A_" + String.valueOf(REP.get(i)));
                                }
                            }
                        } else {
                            if (Parameters != null && Parameters.size() != 0) {
                                if (String.valueOf(Parameters.get(0)).equalsIgnoreCase("Time")) {
                                    originalColumns.add(String.valueOf(Parameters.get(0)));
                                } else {
                                    originalColumns.add("A_" + String.valueOf(Parameters.get(0)));
                                }
                            }
                        }

                        if (AllGraphColumns != null && AllGraphColumns.size() != 0) {
                            for (int i = 0; i < AllGraphColumns.size(); i++) {
                                if (!originalColumns.contains("A_" + String.valueOf(AllGraphColumns.get(i)).replace("A_", ""))) {
                                    originalColumns.add("A_" + String.valueOf(AllGraphColumns.get(i)).replace("A_", ""));
                                }
                            }
                        } else {
                            if (Measures != null && Measures.size() != 0) {
                                if (!originalColumns.contains("A_" + String.valueOf(Measures.get(0)).replace("A_", ""))) {
                                    originalColumns.add("A_" + String.valueOf(Measures.get(0)).replace("A_", ""));
                                }
                            }
                        }
                    }

                    GraphDisplay.setTimelevel(container.getTimeLevel());
                    GraphDisplay.setCurrentDispRetObjRecords(pbretObj);
                    GraphDisplay.setCurrentDispRecordsRetObjWithGT(pbretObj);
                    GraphDisplay.setAllDispRecordsRetObj(pbretObj);
                    GraphDisplay.setSession(request.getSession(false));
                    GraphDisplay.setResponse(response);
                    GraphDisplay.setOut(response.getWriter());
                    GraphDisplay.setReportId("");
                    GraphDisplay.setJscal(null);

                    String[] viewBys = null;
                    String[] viewBysDispNames = null;
                    String[] barChartColumnTitles = null;
                    String[] barChartColumnNames = null;
                    String[] pieChartColumns = null;
                    String[] barChartColumnNames1 = null;
                    String[] barChartColumnNames2 = null;
                    String[] barChartColumnTitles1 = null;
                    String[] barChartColumnTitles2 = null;
                    String dispRowGT = "";
                    Integer dispGTRows = 0;

                    if (graphIds.length != 0) {
                        CrossTabGraphHashMap = new HashMap();
                        for (int i = 0; i < graphMapDetails.length; i++) {
                            graphMapDetails[i] = (HashMap) GraphHashMap.get(graphIds[i]);
                            HashMap graphDetailsforallRows = (HashMap) GraphHashMap.get(graphIds[i]);

                            // Added for display table columns
                            if (graphDetailsforallRows.get("GraphDisplayRows") != null && graphDetailsforallRows.get("GraphDisplayRows") != "NULL") {
                                dispRowGT = graphDetailsforallRows.get("GraphDisplayRows").toString();
                            }
                            if (!dispRowGT.equalsIgnoreCase("") && !dispRowGT.equalsIgnoreCase("ALL")) {
                                dispGTRows = Integer.parseInt(dispRowGT);
                            } else if (dispRowGT.equalsIgnoreCase("ALL")) {
                                dispGTRows = pbretObj.getRowCount();
                            } else {
                                dispGTRows = 10;
                            }
                            // end

                            graphMapDetails[i].put("graphDisplayRows", graphDetailsforallRows.get("graphDisplayRows"));
                            if (CEP != null && CEP.size() != 0) {
                                CrossTabGraphMapDetails[i] = (HashMap) graphMapDetails[i].clone();
                                if (viewBys == null || viewBysDispNames == null) {
                                    viewBys = (String[]) REP.toArray(new String[0]);
                                    viewBysDispNames = (String[]) REPNames.toArray(new String[0]);

                                    if (CrossTabGraphMapDetails[i].get("viewByElementIds") != null) {
                                        for (int index = 0; index < viewBys.length; index++) {
                                            viewBys[index] = String.valueOf(originalColumns.get(index));
                                        }
                                    } else {
                                        viewBys = (String[]) REP.toArray(new String[0]);
                                    }
                                    viewBysDispNames = (String[]) REPNames.toArray(new String[0]);
                                }

                                barChartColumnNames = (String[]) originalColumns.toArray(new String[0]);
                                pieChartColumns = (String[]) originalColumns.toArray(new String[0]);
                                CrossTabGraphMapDetails[i].put("viewByElementIds", viewBys);
                                CrossTabGraphMapDetails[i].put("barChartColumnNames", barChartColumnNames);
                                CrossTabGraphMapDetails[i].put("pieChartColumns", pieChartColumns);
                                CrossTabGraphMapDetails[i].put("axis", axis);
                                barChartColumnTitles = (String[]) originalColumns.toArray(new String[0]);

                                for (int temp1 = 0; temp1 < viewBysDispNames.length; temp1++) {
                                    barChartColumnTitles[temp1] = viewBysDispNames[temp1];
                                }
                                for (int temp2 = viewBysDispNames.length; temp2 < barChartColumnTitles.length; temp2++) {
                                    barChartColumnTitles[temp2] = String.valueOf(repQuery.crossTabNonViewByMap.get(String.valueOf(originalColumns.get(temp2))));
                                }
                                CrossTabGraphMapDetails[i].put("barChartColumnTitles", barChartColumnTitles);
                                CrossTabGraphHashMap.put(graphIds[i], CrossTabGraphMapDetails[i]);

                            } else {

                                if (REP != null && !REP.isEmpty()) {
                                    viewBys = (String[]) REP.toArray(new String[0]);
                                    viewBysDispNames = (String[]) REPNames.toArray(new String[0]);
                                } else {
                                    viewBys = new String[1];
                                    viewBysDispNames = new String[1];
                                    viewBys[0] = String.valueOf(Parameters.get(0));
                                    viewBysDispNames[0] = (String) ParametersNames.toArray(new String[0])[0];
                                }
                                if (graphMapDetails[i].get("barChartColumnNames") != null) {
                                    barChartColumnNames = (String[]) graphMapDetails[i].get("barChartColumnNames");
                                    barChartColumnTitles = new String[barChartColumnNames.length];
                                    axis = new String[barChartColumnNames.length];

                                    for (int temp1 = 0; temp1 < viewBysDispNames.length; temp1++) {
                                        barChartColumnTitles[temp1] = viewBysDispNames[temp1];
                                        axis[temp1] = axis[temp1] != null ? axis[temp1] : "0";
                                    }
                                    for (int temp2 = viewBys.length; temp2 < barChartColumnNames.length; temp2++) {
                                        barChartColumnTitles[temp2] = String.valueOf(repQuery.NonViewByMap.get(barChartColumnNames[temp2]));
                                        axis[temp2] = axis[temp2] != null ? axis[temp2] : "0";
                                    }
                                    graphMapDetails[i].put("barChartColumnTitles", barChartColumnTitles);

                                } else {
                                    barChartColumnNames = new String[viewBys.length + 1];
                                    barChartColumnTitles = new String[viewBys.length + 1];
                                    for (int index = 0; index < viewBys.length; index++) {
                                        barChartColumnNames[index] = String.valueOf(originalColumns.get(index));
                                        barChartColumnTitles[index] = viewBysDispNames[index];
                                    }
                                    for (int index = viewBys.length; index < barChartColumnNames.length; index++) {
                                        barChartColumnNames[index] = String.valueOf(originalColumns.get(index));
                                        barChartColumnTitles[index] = String.valueOf(repQuery.NonViewByMap.get(barChartColumnNames[index]));
                                    }
                                    barChartColumnTitles1 = new String[1 + viewBys.length];
                                    barChartColumnNames1 = new String[barChartColumnTitles1.length];
                                    barChartColumnTitles2 = new String[(barChartColumnNames.length - 1)];
                                    barChartColumnNames2 = new String[barChartColumnTitles2.length];

                                    for (int j = 0; j < viewBys.length; j++) {
                                        barChartColumnTitles1[j] = barChartColumnTitles[j];
                                        barChartColumnTitles2[j] = barChartColumnTitles[j];
                                        barChartColumnNames1[j] = barChartColumnNames[j];
                                        barChartColumnNames2[j] = barChartColumnNames[j];
                                    }
                                    for (int j = viewBys.length; j < barChartColumnTitles1.length; j++) {
                                        barChartColumnTitles1[j] = barChartColumnTitles[j];
                                        barChartColumnNames1[j] = barChartColumnNames[j];
                                    }
                                    for (int j = barChartColumnTitles1.length; j < barChartColumnTitles.length; j++) {
                                        barChartColumnTitles2[j - viewBys.length] = barChartColumnTitles[j];
                                        barChartColumnNames2[j - viewBys.length] = barChartColumnNames[j];
                                    }
                                    graphMapDetails[i].put("viewByElementIds", viewBys);
                                    graphMapDetails[i].put("barChartColumnNames", barChartColumnNames);
                                    graphMapDetails[i].put("pieChartColumns", barChartColumnNames);
                                    graphMapDetails[i].put("barChartColumnTitles", barChartColumnTitles);
                                    graphMapDetails[i].put("barChartColumnNames1", barChartColumnNames1);
                                    graphMapDetails[i].put("barChartColumnTitles1", barChartColumnTitles1);
                                    graphMapDetails[i].put("barChartColumnNames2", barChartColumnNames2);
                                    graphMapDetails[i].put("barChartColumnTitles2", barChartColumnTitles2);

                                }
                                if (graphMapDetails[i].get("barChartColumnNames1") != null) {
                                    barChartColumnNames1 = (String[]) graphMapDetails[i].get("barChartColumnNames1");
                                    barChartColumnTitles1 = new String[barChartColumnNames1.length];
                                    for (int temp1 = 0; temp1 < viewBysDispNames.length; temp1++) {
                                        barChartColumnTitles1[temp1] = viewBysDispNames[temp1];
                                    }
                                    for (int temp2 = viewBys.length; temp2 < barChartColumnNames1.length; temp2++) {
                                        barChartColumnTitles1[temp2] = String.valueOf(repQuery.NonViewByMap.get(barChartColumnNames1[temp2]));
                                    }
                                    graphMapDetails[i].put("barChartColumnTitles1", barChartColumnTitles1);
                                }
                                if (graphMapDetails[i].get("barChartColumnNames2") != null) {
                                    barChartColumnNames2 = (String[]) graphMapDetails[i].get("barChartColumnNames2");
                                    barChartColumnTitles2 = new String[barChartColumnNames2.length];
                                    for (int temp1 = 0; temp1 < viewBysDispNames.length; temp1++) {
                                        barChartColumnTitles2[temp1] = viewBysDispNames[temp1];
                                    }
                                    for (int temp2 = viewBys.length; temp2 < barChartColumnNames2.length; temp2++) {
                                        barChartColumnTitles2[temp2] = String.valueOf(repQuery.NonViewByMap.get(barChartColumnNames2[temp2]));
                                    }
                                    graphMapDetails[i].put("barChartColumnTitles2", barChartColumnTitles2);
                                }

                            }
                        }

                        container.setCrossTabGraphHashMap(CrossTabGraphHashMap);
                        GraphDisplay.setViewByColNames(viewBysDispNames);
                        GraphDisplay.setViewByElementIds(viewBys);
                        if (CEP != null && CEP.size() != 0) {
                            GraphDisplay.setGraphMapDetails(CrossTabGraphMapDetails);

                        } else {
                            GraphDisplay.setGraphMapDetails(graphMapDetails);
                        }
                        ProgenChartDisplay[] pcharts = null;
                        if (getIsFxCharts()) {
                        } else {
                            grpDetails = GraphDisplay.getGraphHeadersByGraphMap();
                            pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
                        }
                        graphsBuffer.append("<Table width=\"100%\" border='0'  >");
                        graphsBuffer.append("<Tr>");
                        for (int grpCnt = 0; grpCnt < graphIds.length; grpCnt++) {
                            if (getIsFxCharts()) {
                            } else {
                                pcharts[grpCnt].setCtxPath(request.getContextPath());
                                pcharts[grpCnt].setSwapColumn(true);
                            }
                            String grpType = "";
                            String grpSize = "";
                            String graphName = "";
                            graphMapDetails[grpCnt] = (HashMap) GraphHashMap.get(graphIds[grpCnt]);
                            grpType = String.valueOf(graphMapDetails[grpCnt].get("graphTypeName"));
                            grpSize = String.valueOf(graphMapDetails[grpCnt].get("graphSize"));
                            graphName = String.valueOf(graphMapDetails[grpCnt].get("graphName"));

                            graphsBuffer.append("<Td valign='top'  width=\"50%\" >");
                            graphsBuffer.append("<div   class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all \">");//border
                            graphsBuffer.append("<div   class=\"portlet-header ui-widget-header ui-corner-all \">" + graphName + "</div>");//portlet-header div starts here
                            graphsBuffer.append("<div style=\"height:310px;width:99%;overflow:auto\">");//InnerDiv
                            graphsBuffer.append("<Table width=\"100%\" border=\"0\" class=\"tablesorter\" cellpadding=\"0\" cellspacing=\"1\">");
                            graphsBuffer.append("<Thead>");
                            graphsBuffer.append("<Tr>");//start of 1st row
                            for (int view = 0; view < barChartColumnNames.length; view++) {
                                graphsBuffer.append("<Th>" + barChartColumnTitles[view] + "</Th>");
                            }
                            graphsBuffer.append("</Tr>");//end of 1st row
                            graphsBuffer.append("</Thead>");
                            graphsBuffer.append("<Tbody>");
                            if (dispGTRows != null) {
                                dispGTRows = (pbretObj.getRowCount() > dispGTRows) ? dispGTRows : pbretObj.getRowCount();
                            }
                            for (int ret = 0; ret < dispGTRows; ret++) {
                                graphsBuffer.append("<Tr>");//start of 2nd row
                                for (int view = 0; view < barChartColumnNames.length; view++) {
                                    graphsBuffer.append("<Td>" + pbretObj.getFieldValueString(ret, barChartColumnNames[view]) + "</Td>");
                                }
                                graphsBuffer.append("</Tr>");
                            }

                            graphsBuffer.append("</Tbody>");
                            graphsBuffer.append("</Table>");//closing of inner table
                            graphsBuffer.append("</div>");//end innerDiv
                            graphsBuffer.append("</div>");//border
                            graphsBuffer.append("</Td>");

                            graphsBuffer.append("<Td valign='top'  width=\"50%\" >");
                            graphsBuffer.append("<Table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                            graphsBuffer.append("<Tr>");//start of 1st row
                            graphsBuffer.append("<Td>");
                            graphsBuffer.append("<div   class=\"portlet-header ui-widget-header ui-corner-all \">" + graphName + "</div>");//portlet-header div starts here
                            graphsBuffer.append("</Td>");
                            graphsBuffer.append("</Tr>");

                            graphsBuffer.append("<Tr>");//start of 2nd row
                            graphsBuffer.append("<Td>");//start of 2nd row 1st td

                            graphsBuffer.append("<Table class=\"progenTable\"  border='0' align=\"left\">");//start of outer table
                            graphsBuffer.append("<Tr>");

                            //start of graph types

                            graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphTableTypesDisp(document.getElementById('dispgrptypes" + graphIds[grpCnt] + "'))\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Types\"> Graph Types </a>");
                            graphsBuffer.append("<div class=\"flagDiv\" style=\"display:none;height:200px\"  id='dispgrptypes" + graphIds[grpCnt] + "'>");

                            graphsBuffer.append("<Table>");//start of inner table
                            for (int gtype = 0; gtype < GraphTypesValues.length; gtype++) {
                                if (grpType.equalsIgnoreCase(GraphTypesValues[gtype])) {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpType + "'>");
                                    graphsBuffer.append("<b>" + grpType + "</b>");
                                    graphsBuffer.append("</Td>");
                                    graphsBuffer.append("</Tr>");
                                } else {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpType + "'>");
                                    graphsBuffer.append("<a href='javascript:void(0)' title='" + GraphTypesValues[gtype] + "' onclick=\"changeGrpTableType('" + GraphTypesValues[gtype] + "','" + graphIds[grpCnt] + "','" + grpIds + "')\">" + GraphTypesValues[gtype] + "</a>");
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
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphTableSizesDisp(document.getElementById('dispgrpsizes" + graphIds[grpCnt] + "'))\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Sizes\"> Graph Sizes </a>");
                            graphsBuffer.append("<div  class=\"flagDiv\" style=\"display:none\"   id='dispgrpsizes" + graphIds[grpCnt] + "'>");

                            graphsBuffer.append("<Table>");//start of inner table
                            for (int gsize = 0; gsize < GraphSizesValues.length; gsize++) {
                                if (grpSize.equalsIgnoreCase(GraphSizesValues[gsize])) {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpSize + "'>");
                                    graphsBuffer.append("<b>" + grpSize + "</b>");
                                    graphsBuffer.append("</Td>");
                                    graphsBuffer.append("</Tr>");
                                } else {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpSize + "'>");
                                    graphsBuffer.append("<a href='javascript:void(0)' title='" + GraphSizesValues[gsize] + "' onclick=\"changeGrpTableSize('" + GraphSizesValues[gsize] + "','" + graphIds[grpCnt] + "','" + grpIds + "')\">" + GraphSizesValues[gsize] + "</a>");
                                    graphsBuffer.append("</Td>");
                                    graphsBuffer.append("</Tr>");
                                }
                            }
                            graphsBuffer.append("</Table>");//closing of inner table
                            graphsBuffer.append(" </div>");
                            graphsBuffer.append("</Td>");
                            //end of graph sizes


                            //start of graph columns
                            if (CEP == null || CEP.size() == 0) {
                                graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                                graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphTableColumnsDisp('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Columns\"> Graph Columns </a>");

                            }
                            //end of graph columns

                            //start of delete .16
                            graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onClick=\"deleteGraphTable('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Delete Chart\">Delete Chart </a>");
                            //end of delete graph

                            //start of  graph details
                            graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onClick=\"dispGraphTableDetails('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Delete Chart\">Graph Details</a>");
                            //end of  graph details

                            //start of Row values added by santhosh.kumar@progenbusiness.com on 24/12/2009
                            if (grpType.equalsIgnoreCase("ColumnPie") || grpType.equalsIgnoreCase("ColumnPie3D")) {
                                graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                                graphsBuffer.append("<Td><a  href='javascript:void(0)' onClick=\"dispRowTableValues('dispRowValues" + grpCnt + "','rowValues" + grpCnt + "','" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Column Pie rows\">Rows</a>");
                                graphsBuffer.append("<div  class=\"flagDiv\" style=\"display:none;height:200px;\"    id='dispRowValues" + grpCnt + "'>");

                                graphsBuffer.append("<Table>");//start of inner table
                                StringBuffer sbuffer = null;
                                ArrayList RowValues = null;
                                RowValues = (ArrayList) graphMapDetails[grpCnt].get("RowValuesList");

                                if (RowValues == null) {
                                    RowValues = new ArrayList();
                                }
                                for (int rowCnt = 0; rowCnt < pbretObj.getRowCount(); rowCnt++) {
                                    sbuffer = new StringBuffer("");
                                    for (int j = 0; j < viewBysDispNames.length; j++) {
                                        sbuffer.append("-" + pbretObj.getFieldValueString(rowCnt, barChartColumnNames[j]));
                                    }
                                    if (RowValues.size() == 0) {
                                        RowValues.add(sbuffer.toString().substring(1));
                                    }
                                    graphsBuffer.append("<Tr>");
                                    if (RowValues.contains(sbuffer.toString().substring(1))) {
                                        graphsBuffer.append("<Td><input type=\"checkbox\" checked value='" + sbuffer.toString().substring(1) + "' id='rowValues" + rowCnt + "' name='rowValues" + grpCnt + "' >" + sbuffer.toString().substring(1) + "</Td>");
                                    } else {
                                        graphsBuffer.append("<Td><input type=\"checkbox\"  value='" + sbuffer.toString().substring(1) + "' id='rowValues" + rowCnt + "' name='rowValues" + grpCnt + "' >" + sbuffer.toString().substring(1) + "</Td>");
                                    }
                                    graphsBuffer.append("</Tr>");
                                }
                                graphsBuffer.append("</Table>");//closing of inner table
                                graphsBuffer.append(" </div>");
                                graphsBuffer.append("</Td>");
                            }

                            //end of row values

                            graphsBuffer.append("</Tr>");
                            graphsBuffer.append("</Table>");//closing of outer table

                            graphsBuffer.append("</Td>");//closing of 2nd row 1st td
                            graphsBuffer.append("</Tr>");//end of 2nd row

                            graphsBuffer.append("<Tr>");//start of 3rd row
                            graphsBuffer.append("<Td align='left'>");
                            graphsBuffer.append("<div  align='left'   class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\">");//portlet div starts here

                            graphsBuffer.append("<div align='left' style=\"width:100%;height:auto\"   >");
                            if (getIsFxCharts()) {
                                graphsBuffer.append("<iframe frameborder='0' scrolling='No' NAME='iframe_" + graphIds[grpCnt] + "_" + request.getParameter("REPORTID") + "'  id='iframe_" + graphIds[grpCnt] + "_" + request.getParameter("REPORTID") + "' STYLE='width:100%;height:100%;overflow:auto;' src='" + request.getContextPath() + "/PbFXGraphXMLBuild.jsp?REPORTID=" + request.getParameter("REPORTID") + "&graphId=" + graphIds[grpCnt] + "'></iframe>");

                            } else {
                                if (pcharts[grpCnt].chartDisplay.equalsIgnoreCase("")) {
                                    graphsBuffer.append("<SCRIPT LANGUAGE=\"JavaScript\" SRC=\"" + request.getContextPath() + "/overlib.js\">  </SCRIPT>");
                                    graphsBuffer.append("<div  align='left' id=\"overDiv\" style=\"position:absolute; visibility:hidden; z-index:1000;\"></div>");
                                    graphsBuffer.append("<img  src=\"" + request.getContextPath() + "/images/" + grpType + ".gif\" border='0' align='top' > </img>");
                                    graphsBuffer.append(pcharts[grpCnt].chartDisplay);
                                } else {
                                    graphsBuffer.append(pcharts[grpCnt].chartDisplay);
                                }
                            }

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
                } else {

                    if (graphIds.length != 0) {
                        graphMapDetails = new HashMap[graphIds.length];

                        graphsBuffer.append("<Table width=\"100%\" border='0'  >");
                        graphsBuffer.append("<Tr>");
                        for (int grpCnt = 0; grpCnt < graphMapDetails.length; grpCnt++) {
                            String grpType = "";
                            String grpSize = "";
                            String graphName = "";
                            graphMapDetails[grpCnt] = (HashMap) GraphHashMap.get(graphIds[grpCnt]);
                            grpType = String.valueOf(graphMapDetails[grpCnt].get("graphTypeName"));
                            grpSize = String.valueOf(graphMapDetails[grpCnt].get("graphSize"));
                            graphName = String.valueOf(graphMapDetails[grpCnt].get("graphName"));
//                            //////.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

                            graphsBuffer.append("<Td valign='top'  width=\"50%\" >");
                            graphsBuffer.append("<div   class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all \">");//border
                            graphsBuffer.append("<div   class=\"portlet-header ui-widget-header ui-corner-all \">" + graphName + "</div>");//portlet-header div starts here
                            graphsBuffer.append("<div style=\"height:320px;overflow-y:auto\">");//InnerDiv
                            graphsBuffer.append("<div  align='left' style=\"height:300px\">");//portlet div starts here
                            graphsBuffer.append("<table align=\"center\" style=\"width:100%;height:100%\">");
                            graphsBuffer.append("<tr align=\"center\" valign=\"middle\">");
                            graphsBuffer.append("<td align=\"center\" valign=\"middle\"> No data to display</td>");
                            graphsBuffer.append("</tr>");
                            graphsBuffer.append(" </table>");
                            graphsBuffer.append("</div >");//column div ends here
                            graphsBuffer.append("</div>");//end innerDiv
                            graphsBuffer.append("</div>");//border
                            graphsBuffer.append("</Td>");


                            graphsBuffer.append("<Td valign='top'  width=\"50%\" >");
                            graphsBuffer.append("<Table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                            graphsBuffer.append("<Tr>");//start of 1st row
                            graphsBuffer.append("<Td>");
                            graphsBuffer.append("<div   class=\"portlet-header ui-widget-header ui-corner-all \">" + graphName + "</div>");//portlet-header div starts here
                            graphsBuffer.append("</Td>");
                            graphsBuffer.append("</Tr>");


                            graphsBuffer.append("<Tr>");//start of 2nd row
                            graphsBuffer.append("<Td>");//start of 2nd row 1st td

                            graphsBuffer.append("<Table class=\"progenTable\"  border='0' align=\"left\">");//start of outer table
                            graphsBuffer.append("<Tr>");


                            //start of graph types

                            graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphTableTypesDisp(document.getElementById('dispgrptypes" + grpCnt + "'))\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Types\"> Graph Types </a>");
                            graphsBuffer.append("<div class=\"flagDiv\" style=\"display:none;height:200px;\"     id='dispgrptypes" + grpCnt + "'>");

                            graphsBuffer.append("<Table>");//start of inner table
                            for (int gtype = 0; gtype < GraphTypesValues.length; gtype++) {

                                if (grpType.equalsIgnoreCase(GraphTypesValues[gtype])) {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpType + "'>");
                                    graphsBuffer.append("<b>" + grpType + "</b>");
                                    graphsBuffer.append("</Td>");
                                    graphsBuffer.append("</Tr>");
                                } else {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpType + "'>");
                                    graphsBuffer.append("<a href='javascript:void(0)' title='" + GraphTypesValues[gtype] + "' onclick=\"changeGrpTableType('" + GraphTypesValues[gtype] + "','" + graphIds[grpCnt] + "','" + grpIds + "')\">" + GraphTypesValues[gtype] + "</a>");
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
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphTableSizesDisp(document.getElementById('dispgrpsizes" + grpCnt + "'))\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Sizes\"> Graph Sizes </a>");
                            graphsBuffer.append("<div class=\"flagDiv\" style=\"display:none;\"   id='dispgrpsizes" + grpCnt + "'>");

                            graphsBuffer.append("<Table>");//start of inner table
                            for (int gsize = 0; gsize < GraphSizesValues.length; gsize++) {

                                if (grpSize.equalsIgnoreCase(GraphSizesValues[gsize])) {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpSize + "'>");
                                    graphsBuffer.append("<b>" + grpSize + "</b>");
                                    graphsBuffer.append("</Td>");
                                    graphsBuffer.append("</Tr>");
                                } else {
                                    graphsBuffer.append("<Tr>");
                                    graphsBuffer.append("<Td id='" + grpSize + "'>");
                                    graphsBuffer.append("<a href='javascript:void(0)' title='" + GraphSizesValues[gsize] + "' onclick=\"changeGrpTableSize('" + GraphSizesValues[gsize] + "','" + graphIds[grpCnt] + "','" + grpIds + "')\">" + GraphSizesValues[gsize] + "</a>");
                                    graphsBuffer.append("</Td>");
                                    graphsBuffer.append("</Tr>");
                                }

                            }
                            graphsBuffer.append("</Table>");//closing of inner table
                            graphsBuffer.append(" </div>");
                            graphsBuffer.append("</Td>");

                            //end of graph sizes
                            //start of graph columns
                            if (CEP == null || CEP.size() == 0) {
                                graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                                graphsBuffer.append("<Td><a  href='javascript:void(0)' onclick=\"graphTableColumnsDisp('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Graph Columns\"> Graph Columns </a>");

                            }
                            //end of graph columns

                            //start of delete graph
                            graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onClick=\"deleteGraphTable('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Delete Chart\">Delete Chart </a>");
                            //end of delete graph

                            //start of  graph details
                            graphsBuffer.append("<Td><img src='" + request.getContextPath() + "/images/separator.gif' style='border:0'  alt='Separator' /></Td>");
                            //graphsBuffer.append("<Td><a  href='javascript:void(0)' onClick=\"dispGraphDetails('" + graphIds[grpCnt] + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Delete Chart\">Graph Details</a>");
                            graphsBuffer.append("<Td><a  href='javascript:void(0)' onClick=\"dispGraphTableDetails('" + graphIds[grpCnt] + "','" + grpIds + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Delete Chart\">Graph Details</a>");

                            //end of  graph details

                            graphsBuffer.append("</Tr>");
                            graphsBuffer.append("</Table>");//closing of outer table

                            graphsBuffer.append("</Td>");//closing of 2nd row 1st td
                            graphsBuffer.append("</Tr>");//end of 2nd row

                            graphsBuffer.append("<Tr>");//start of 3rd row
                            graphsBuffer.append("<Td align='left'>");
                            graphsBuffer.append("<div  align='left' style=\"height:300px\"   class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\">");//portlet div starts here

                            graphsBuffer.append("<table align=\"center\" style=\"width:100%;height:100%\">");
                            graphsBuffer.append("<tr align=\"center\" valign=\"middle\">");
                            graphsBuffer.append("<td align=\"center\" valign=\"middle\"> No data to display</td>");
                            graphsBuffer.append("</tr>");
                            graphsBuffer.append(" </table>");
                            /*
                             * graphsBuffer.append("<div align='left'
                             * style=\"height:auto;width:" + (90 /
                             * graphMapDetails.length) + "%\" >");
                             * graphsBuffer.append("<SCRIPT
                             * LANGUAGE=\"JavaScript\" SRC=\"" +
                             * request.getContextPath() + "/overlib.js\">
                             * </SCRIPT>"); graphsBuffer.append("<div
                             * align='left' id=\"overDiv\"
                             * style=\"position:absolute; visibility:hidden;
                             * z-index:1000;\"></div>");
                             * graphsBuffer.append("<img height=\"172px\"
                             * width=\"372px\" src=\"" +
                             * request.getContextPath() + "/images/" + grpType +
                             * ".gif\" border='0' align='top' > </img>");
                             * graphsBuffer.append("</div >");//portlet div ends
                             * here
                             */

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
            } else {
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return graphsBuffer.toString();
    }

    public String buildTable(String action, Container container, String userId, HashMap reqParam) {
        PbReportViewerBD viewerBd = new PbReportViewerBD();
        StringBuilder tableBuffer = new StringBuilder();
        if (!"none".equalsIgnoreCase(action)) {
            if (action.equalsIgnoreCase("paramChange")) {
                viewerBd.prepareReport(action, container, container.getReportCollect().reportId, userId, reqParam);
            }
            viewerBd.prepareReport(action, container, container.getReportCollect().reportId, userId, new HashMap());

            ArrayList dispLabels = container.getDisplayLabels();
            ArrayList<String> dispCols = container.getDisplayColumns();
            ArrayList dispTypes = container.getDataTypes();//container.getDisplayTypes();
            ProgenDataSet retObj = container.getRetObj();

            tableBuffer.append("<div>");
            tableBuffer.append("<table cellspacing=\"1\" class=\"tablesorter\" id=\"tablesorter\" width=\"100px\">");
            tableBuffer.append("<thead>");
            tableBuffer.append("<tr valign=\"top\">");

            //for (int i=0;i<dispLabels.size();i++)
            // tableBuffer.append("<th>"+dispLabels.get(i)+"</th>");

            tableBuffer.append("</tr>");
            tableBuffer.append("</thead>");
            tableBuffer.append("<tfoot>");
            tableBuffer.append("</tfoot>");
            tableBuffer.append("<tbody>");

            for (int i = 0; i < retObj.getRowCount(); i++) {
                tableBuffer.append("<tr>");
                for (int j = 0; j < dispCols.size(); j++) {
                    String colType = (String) dispTypes.get(j);
                    if ("C".equalsIgnoreCase(colType)) {
                        tableBuffer.append("<td align=\"left\">").append(retObj.getFieldValueString(i, dispCols.get(j))).append("</td>");
                    } else {
                        BigDecimal bd = retObj.getFieldValueBigDecimal(i, dispCols.get(j));
                        tableBuffer.append("<td align=\"right\">").append(NumberFormatter.getModifiedNumber(bd, "", -1)).append("</td>");
                    }
                }
                tableBuffer.append("</tr>");
            }

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
            tableBuffer.append("</div>");
        }
        return tableBuffer.toString();
    }

    //for building report designing table
    //  public String buildTable(HashMap ParametersHashMap, HashMap TableHashMap, HashMap GraphHashMap, String ctxPath, HttpSession session) {
    public String buildTable(Container container, String ctxPath, HttpSession session) {

        HashMap TableHashMap = null;
        HashMap GraphHashMap = null;
        HashMap ParametersHashMap = null;
        HashMap ReportHashMap = null;
        TableHashMap = container.getTableHashMap();
        GraphHashMap = container.getGraphHashMap();
        ParametersHashMap = container.getParametersHashMap();
        ReportHashMap = container.getReportHashMap();
        PbReportCollection collect = new PbReportCollection();
        PbReportQuery repQuery = new PbReportQuery();
        ArrayList displayLabels = new ArrayList();
        ArrayList dataTypes = new ArrayList();
        ArrayList originalColumns = new ArrayList();
        ArrayList displayColumns = new ArrayList();
        ArrayList alignments = new ArrayList();
        ArrayList<String> displayTypes = new ArrayList<String>();
        ArrayList<String> links = new ArrayList<String>();
        ProgenParam pParam = new ProgenParam();
        String[] dbColumns = new String[0];

        ArrayList reportQryElementIds = new ArrayList();
        ArrayList reportQryAggregations = new ArrayList();
        ArrayList reportQryColNames = new ArrayList();
        HashMap reportIncomingParameters = new HashMap();
        HashMap paramValues = new HashMap();
        ArrayList REP = null;
        ArrayList CEP = null;
        ArrayList Measures = null;
        ArrayList MeasuresNames = null;
        ArrayList Parameters = null;

        ArrayList REPNames = null;
        ArrayList CEPNames = null;
        ArrayList ParametersNames = null;
        //ArrayList AllGraphColumns = null;



        HashMap TimeDimHashMap = new HashMap();
        ArrayList TimeDetailstList = new ArrayList();

        PbReturnObject retObj = null;


        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        if (ParametersHashMap.get("TimeDimHashMap") != null) {
            TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
            TimeDetailstList = (ArrayList) ParametersHashMap.get("TimeDetailstList");
        } else {
            TimeDetailstList.add("Day");
            TimeDetailstList.add("PRG_STD");
            TimeDetailstList.add(pParam.getdateforpage().toString());
            TimeDetailstList.add("Month");
            TimeDetailstList.add("Last Period");
            repQuery.avoidProGenTime = true;
        }

        String defaultSortedCol = null;

        String[] columnTypes = null;

        String[] TimeParametersStr = null;
        ArrayList ReportTimeParams = null;
        ArrayList ReportTimeParamsNames = null;

        if (TableHashMap.get("REP") != null) {
            REP = (ArrayList) TableHashMap.get("REP");
            REPNames = (ArrayList) TableHashMap.get("REPNames");
        }
        if (TableHashMap.get("CEP") != null) {
            CEP = (ArrayList) TableHashMap.get("CEP");
            CEPNames = (ArrayList) TableHashMap.get("CEPNames");
        }
        if (TableHashMap.get("Measures") != null) {
            Measures = (ArrayList) TableHashMap.get("Measures");
            MeasuresNames = (ArrayList) TableHashMap.get("MeasuresNames");
            MeasuresNames = new ArrayList();

        }
        if (ParametersHashMap.get("Parameters") != null) {
            Parameters = (ArrayList) ParametersHashMap.get("Parameters");
            ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
        }
        if (REP == null) {
            REP = new ArrayList();
            REPNames = new ArrayList();
            if (Parameters != null && Parameters.size() != 0) {
                REP.add(String.valueOf(Parameters.get(0)));
                REPNames.add(String.valueOf(ParametersNames.get(0)));
            }
        }
        if (CEP == null) {
            CEP = new ArrayList();
        }
        if (Measures == null) {
            Measures = new ArrayList();
        }

        if (REP.size() != 0 && Measures.size() != 0) {
            for (int i = 0; i < Parameters.size(); i++) {
                if (paramValues.get(String.valueOf(Parameters.get(i))) == null) {
                    paramValues.put(String.valueOf(Parameters.get(i)), "All");
                    reportIncomingParameters.put("CBOAPR" + String.valueOf(Parameters.get(i)), null);
                }
            }

            if (TableHashMap.get("repParamVals") != null) {
                paramValues = (HashMap) TableHashMap.get("repParamVals");
            }
            collect.reportIncomingParameters = reportIncomingParameters;
            collect.reportColViewbyValues = CEP;
            collect.reportRowViewbyValues = REP;
            collect.timeDetailsArray = TimeDetailstList;
            collect.timeDetailsMap = TimeDimHashMap;
            collect.defaultSortedColumn = defaultSortedCol;
            collect.reportId = reportId;

            collect.getParamMetaDataForReportDesigner();

            //addedby santhosh.k on 08-03-2010
            if (collect.timeDetailsMap != null) {
                TimeParametersStr = (String[]) (collect.timeDetailsMap.keySet()).toArray(new String[0]);
                for (int timeIndex = 0; timeIndex < TimeParametersStr.length; timeIndex++) {
                    ArrayList alist = (ArrayList) collect.timeDetailsMap.get(TimeParametersStr[timeIndex]);
                    if (ReportTimeParams == null) {
                        ReportTimeParams = new ArrayList();
                        ReportTimeParamsNames = new ArrayList();
                    }
                    ReportTimeParams.add(TimeParametersStr[timeIndex]);
                    ReportTimeParamsNames.add(String.valueOf(alist.get(2)));
                    alist = null;
                }

                ParametersHashMap.put("TimeParameters", ReportTimeParams);
                ParametersHashMap.put("TimeParametersNames", ReportTimeParamsNames);
            }
            for (int j = 0; j < Measures.size(); j++) {
                if (!reportQryElementIds.contains(String.valueOf(Measures.get(j)).replace("A_", ""))) {
                    if (!RTMeasureElement.isRunTimeMeasure(String.valueOf(Measures.get(j)).replace("A_", ""))) {
                        reportQryElementIds.add(String.valueOf(Measures.get(j)).replace("A_", ""));
                    }
                }
            }
            if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
                reportQryAggregations = DAO.getReportQryAggregations(reportQryElementIds);
                reportQryColNames = DAO.getReportQryColNames();
                collect.reportQryElementIds = reportQryElementIds;
                collect.reportQryAggregations = reportQryAggregations;
                collect.reportQryColNames = reportQryColNames;

                repQuery.setQryColumns(collect.reportQryElementIds);
                repQuery.setColAggration(collect.reportQryAggregations);

                repQuery.setDefaultMeasure(String.valueOf(reportQryElementIds.get(0)));
                repQuery.setDefaultMeasureSumm(String.valueOf(reportQryAggregations.get(0)));
            }
            repQuery.setRowViewbyCols(REP);
            repQuery.setColViewbyCols(CEP);
            repQuery.setParamValue(paramValues);
//            
            repQuery.setTimeDetails(TimeDetailstList); //assigning time details array to report query

            //added by susheela on 03-02-10
            repQuery.setReportId(getReportId());

            //added by santhosh.kumar@progenbusiness.com on 03/12/2009 fro setting user id and biz roles
            repQuery.setBizRoles(getBizRoles());
            repQuery.setUserId(getUserId());

            if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
                boolean defSortExists = false;
                if (collect.defaultSortedColumn != null) {
                    if (collect.reportRowViewbyValues.contains(collect.defaultSortedColumn)) {
                        for (int i = 0; i < collect.reportRowViewbyValues.size(); i++) {
                            if (collect.reportRowViewbyValues.get(i).toString().equalsIgnoreCase(collect.defaultSortedColumn)) {
                                collect.defaultSortedColumn = String.valueOf(i + 1);
                                defSortExists = true;
                            }
                        }

                    } else if (collect.reportColViewbyValues != null && collect.reportColViewbyValues.size() == 0) {
                        if (collect.reportQryElementIds.contains(collect.defaultSortedColumn)) {
                            defSortExists = true;
                            for (int i = 0; i < collect.reportQryElementIds.size(); i++) {
                                if (collect.reportQryElementIds.get(i).toString().equalsIgnoreCase(collect.defaultSortedColumn)) {
                                    defSortExists = true;
                                    collect.defaultSortedColumn = String.valueOf(i + collect.reportRowViewbyValues.size() + 1);
                                }
                            }
                        }
                    } else {
                    }
                }
                if (defSortExists) {
                    repQuery.setDefaultSortedColumn(collect.defaultSortedColumn);//need to be removed later
                } else {
                    //repQuery.setDefaultSortedColumn(collect.defaultSortedColumn);//need to be removed later
                }
                //default sorted column
//                
                repQuery.setGrandTotalSubTotalDisplayPosition(container.getCrosstabGrandTotalDisplayPosition(), container.getCrosstabSubTotalDisplayPosition());

                retObj = repQuery.getPbReturnObjectWithFlag(String.valueOf(reportQryElementIds.get(0)));
                //////.println("retobject sqlstr in 1 is : " + session.getAttribute("sqlStr"));

            }
            if (retObj != null) {
                dbColumns = retObj.getColumnNames();
                displayLabels = (ArrayList) REPNames.clone();
                if (CEP != null && CEP.size() != 0) {
                    for (int nonViewByIndex = collect.reportRowViewbyValues.size(); nonViewByIndex < (collect.reportRowViewbyValues.size() + repQuery.crossTabNonViewBy.size()); nonViewByIndex++) {
                        displayLabels.add(repQuery.crossTabNonViewByMap.get(dbColumns[nonViewByIndex]));
                        MeasuresNames = reportQryColNames;
                    }
                } else {
                    for (int nonViewByIndex = REP.size(); nonViewByIndex < (REP.size() + repQuery.NonViewByMap.size()); nonViewByIndex++) {
                        displayLabels.add(String.valueOf(repQuery.NonViewByMap.get(dbColumns[nonViewByIndex])));
                        MeasuresNames.add(String.valueOf(repQuery.NonViewByMap.get(dbColumns[nonViewByIndex])));
                    }
                }
                if (MeasuresNames.size() == 0) {
                    TableHashMap.put("MeasuresNames", Measures);
                } else {
                    TableHashMap.put("MeasuresNames", MeasuresNames);
                }

                columnTypes = retObj.getColumnTypes();
                for (int colNum = 0; colNum < dbColumns.length; colNum++) {
                    originalColumns.add(dbColumns[colNum]);
                    displayColumns.add(dbColumns[colNum]);
                    if (columnTypes[colNum].equalsIgnoreCase("VARCHAR2")) {
                        dataTypes.add("C");
                        alignments.add("LEFT");
                    } else if (columnTypes[colNum].equalsIgnoreCase("NUMBER")
                            || columnTypes[colNum].equalsIgnoreCase("NUMERIC")
                            || columnTypes[colNum].equalsIgnoreCase("FLOAT")
                            || columnTypes[colNum].equalsIgnoreCase("DOUBLE")
                            || columnTypes[colNum].equalsIgnoreCase("DECIMAL")
                            || columnTypes[colNum].equalsIgnoreCase("BIGINT")
                            || columnTypes[colNum].equalsIgnoreCase("INTEGER")
                            || columnTypes[colNum].equalsIgnoreCase("INT")) {
                        dataTypes.add("N");
                        alignments.add("RIGHT");
                    } else if (columnTypes[colNum].equalsIgnoreCase("CALCULATED")) {
                        dataTypes.add("N");
                        alignments.add("RIGHT");
                    } else if (columnTypes[colNum].equalsIgnoreCase("SUMMARISED")) {
                        dataTypes.add("N");
                        alignments.add("RIGHT");
                    } else {
                        dataTypes.add("C");
                        alignments.add("LEFT");
                    }
                    displayTypes.add("T");
                    links.add("");
                }


                ReportHashMap.put("reportQryElementIds", reportQryElementIds);
                ReportHashMap.put("reportQryAggregations", reportQryAggregations);
                ReportHashMap.put("reportQryColNames", reportQryColNames);
//            ReportHashMap.put("ReportName", collect.reportName);
//            ReportHashMap.put("ReportDesc", collect.reportId);
                ReportHashMap.put("ReportFolders", new String[]{getBizRoles()});

                container.setReportCollect(collect);
                container.setRetObj(retObj);
                container.setTableMeasure(reportQryElementIds);
                container.setTableMeasureNames(MeasuresNames);
                container.setViewByColNames(collect.reportRowViewbyValues);
                container.setViewByElementIds(collect.reportRowViewbyValues);
                container.setTimeDetailsArray(collect.timeDetailsArray);
                container.setSqlStr(repQuery.getGeneratedQuery());
                container.setDisplayColumns(displayColumns);
                container.setDisplayLabels(displayLabels);
                container.setCrosstabColumnSpan(repQuery.getCrosstabColumnSpan());

                container.setLinks(links);
                container.setViewByCount(collect.reportRowViewbyValues.size());

                if (collect.reportColViewbyValues != null && collect.reportColViewbyValues.size() != 0) {
                    container.setColumnViewByCount(String.valueOf(collect.reportColViewbyValues.size()));
                    container.setColumnViewByName(collect.getElementName(String.valueOf(collect.reportColViewbyValues.get(0))));
                    container.setColumnViewByElementIds(collect.reportColViewbyValues);
                    container.setReportCrosstab(true);
                } else {
                    container.setColumnViewByCount("0");
                }

                container.setTableHashMap(TableHashMap);
                container.setParametersHashMap(ParametersHashMap);
                container.setReportHashMap(ReportHashMap);

                container.setOriginalColumns(originalColumns);
                container.setDisplayColumns(displayColumns);
                container.setAlignments(alignments);
                container.setDisplayTypes(displayTypes);
                container.setDataTypes(dataTypes);

                if (collect.reportColViewbyValues != null && collect.reportColViewbyValues.size() > 0) {
                    container.setReportCrosstab(true);
                } else {
                    container.setReportCrosstab(false);
                }

                ReportParameter repParam = container.getReportParameter();
                if (repParam == null) {
                    repParam = new ReportParameter();
                }

                repParam.setReportParameters(paramValues);
                ArrayList<String> colViewBys = new ArrayList<String>();
                if (container.isReportCrosstab()) {
                    colViewBys = container.getColumnViewByElementIds();
                }

                ArrayList rowViewBys = container.getViewByElementIds();
                repParam.setViewBys(rowViewBys, colViewBys);

                container.setReportParameter(repParam);

                if (container.getPagesPerSlide() == null) {
                    container.setPagesPerSlide("10");
                }
            }
        } else {
        }

        return null;
        //return tableBuffer.toString() + "Ã‚Â¥" + session.getAttribute("sqlStr");
    }

    //called for changing graph title in report designing
    public HashMap changeGraphTitle(String grpId, String grpTitle, HashMap GraphHashMap) {
        HashMap GraphDetails = null;

        if (GraphHashMap != null && GraphHashMap.get(grpId) != null) {
            GraphDetails = (HashMap) GraphHashMap.get(grpId);
            GraphDetails.put("graphName", grpTitle);
            GraphHashMap.put(grpId, GraphDetails);
        }

        return GraphHashMap;
    }

    public String getCurrDate() {
        Date date = new Date();
        String DATE_FORMAT = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        return sdf.format(date);
    }

    // for saving of report designed  in report designer
    public String saveReport(Container container, String reportName, String reportDesc, int reportId, String userId) throws Exception {
        PbReportViewerBD repBd = new PbReportViewerBD();
        repBd.setShowGraphTable(this.graphTableHidden);
        String newReportId = repBd.saveReport(container, reportName, reportDesc, reportId, null, userId, null, null);
        return newReportId;
    }

    //added by Kalyan Krishna on 19/11/2009 for changing graph details
    public HashMap changeGraphDetails(String grpId, String grpTitle, String grpLegend, String grpLegendLoc, String grpshowX, String grpshowY, String grplyaxislabel, String grpryaxislabel, String grpdrill, String grpbcolor, String grpfcolor, String grpdata, HashMap GraphHashMap, String GraphDisplayRows, String startindex, String endindex, boolean isShowLabels) {
        HashMap GraphDetails = null;
        GraphProperty graphProperty = null;
        if (GraphHashMap != null && GraphHashMap.get(grpId) != null) {
            GraphDetails = (HashMap) GraphHashMap.get(grpId);
            if (grpTitle != null) {
                GraphDetails.put("graphName", grpTitle);
            }
            if (grpLegend != null) {
                GraphDetails.put("graphLegend", grpLegend);
            }
            if (grpLegendLoc != null) {
                GraphDetails.put("graphLegendLoc", grpLegendLoc);
            }
            if (grpshowX != null) {
                GraphDetails.put("graphshowX", grpshowX);
            }
            if (grpshowY != null) {
                GraphDetails.put("graphshowY", grpshowY);
            }
            if (grplyaxislabel != null) {
                GraphDetails.put("graphLYaxislabel", grplyaxislabel);
            }
            if (grpryaxislabel != null) {
                GraphDetails.put("graphRYaxislabel", grpryaxislabel);
            }
            if (grpdrill != null) {
                GraphDetails.put("graphDrill", grpdrill);
            }
            if (grpbcolor != null) {
                GraphDetails.put("graphBcolor", grpbcolor);
            }
            if (grpfcolor != null) {
                GraphDetails.put("graphFcolor", grpfcolor);
            }
            if (grpdata != null) {
                GraphDetails.put("graphData", grpdata);
            }
            if (GraphDisplayRows != null) {
                GraphDetails.put("graphDisplayRows", GraphDisplayRows);
            }
            if (GraphDisplayRows != null) {
                GraphDetails.put("startindex", startindex);
            }
            if (GraphDisplayRows != null) {
                GraphDetails.put("endindex", endindex);
            }
            if (GraphDetails.get("GraphProperty") != null) {
                graphProperty = (GraphProperty) GraphDetails.get("GraphProperty");
                graphProperty.setLabelsDisplayed(isShowLabels);
            } else {
                graphProperty = new GraphProperty();
                graphProperty.setLabelsDisplayed(isShowLabels);
            }
            GraphDetails.put("GraphProperty", graphProperty);
            GraphHashMap.put(grpId, GraphDetails);
        }



        return GraphHashMap;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBizRoles() {
        return bizRoles;
    }

    public void setBizRoles(String bizRoles) {
        this.bizRoles = bizRoles;
    }

    //for building row values exclusively for column pie type of charts
    public String buildRowValuesXML(ArrayList alist) {
        StringBuffer sbuffer = new StringBuffer("");
        sbuffer.append("<column-row-values>");
        sbuffer.append("<row-values>");
        if (alist != null) {
            for (int j = 0; j < alist.size(); j++) {
                sbuffer.append("<row-value>" + alist.get(j) + "</row-value>");
            }
        }
        sbuffer.append("</row-values>");
        sbuffer.append("</column-row-values>");

        return sbuffer.toString();
    }

    public HashMap changeRowValuesList(String grpId, String rowValues, HashMap GraphHashMap) {

        String[] RowvaluesStr = null;
        HashMap GraphDetails = null;
        ArrayList temp = new ArrayList();
        if (grpId != null) {
            //if (rowValues != null && !rowValues.equalsIgnoreCase("")) {
            if (rowValues != null) {
                RowvaluesStr = rowValues.split(",");
                for (String Str : RowvaluesStr) {
                    //Str = Str.replace("^", "&");
                    //Str = Str.replace("~", ",");
                    temp.add(Str);
                }
            }
            if (GraphHashMap != null && GraphHashMap.get(grpId) != null) {
                GraphDetails = (HashMap) GraphHashMap.get(grpId);
                GraphDetails.put("RowValuesList", temp);

                GraphHashMap.put(grpId, GraphDetails);
            }
        }
        return GraphHashMap;
    }

    //called from report viewer for adding new graphs in report vierer
    public HashMap addGraphs(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        HashMap GraphClassesHashMap = null;
        HashMap GraphSizesDtlsHashMap = null;
        HashMap GraphHashMap = null;
        Container container = null;
        HashMap map = null;
        String grpType = null;
        String grpId = null;
        String REPORTID = null;
        HashMap GraphDetails = new HashMap();
        String[] barChartColumnNames = null;
        String[] pieChartColumns = null;
        String[] barChartColumnTitles = null;
        String[] axis = null;
        String[] viewByElementIds = null;
        //String[] viewBys = null;
        String[] viewByNames = null;

        String[] barChartColumnNames1 = null;
        String[] barChartColumnTitles1 = null;
        String[] barChartColumnNames2 = null;
        String[] barChartColumnTitles2 = null;
        if (session != null) {
            map = (HashMap) session.getAttribute("PROGENTABLES");
            grpType = request.getParameter("grptypid");
            //DAO.getSequence("PRG_AR_GRAPH_MASTER_SEQ")
            grpId = request.getParameter("gid");

            REPORTID = request.getParameter("REPORTID");
            container = (Container) map.get(REPORTID);
            GraphHashMap = container.getGraphHashMap();

            GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");
            GraphSizesDtlsHashMap = (HashMap) session.getAttribute("GraphSizesDtlsHashMap");

            ArrayList allGraphColumns = null;
            ArrayList sizeDtls = (ArrayList) GraphSizesDtlsHashMap.get("Medium");
            String graphClass = String.valueOf(GraphClassesHashMap.get(grpType));
            String grpIds = "";
            String ReportName = container.getReportName();
            int count = 1;
            int viewbyCount = (container.getViewByCount());
            int colViewByCount = Integer.parseInt(container.getColumnViewByCount());
            ArrayList displayColumns = container.getDisplayColumns();
            ArrayList displayLabels = container.getDisplayLabels();

            try {
                if (GraphHashMap.get("graphIds") != null) {
                    grpIds = (String) GraphHashMap.get("graphIds");
                    grpIds = grpIds + "," + grpId;
                } else {
                    grpIds = grpId;
                }
                viewByElementIds = new String[viewbyCount];
                viewByNames = new String[viewbyCount];

                for (int i = 0; i < viewbyCount; i++) {
                    viewByElementIds[i] = String.valueOf(displayColumns.get(i));
                    viewByNames[i] = String.valueOf(displayLabels.get(i));
                }
                if (colViewByCount == 0) {// not cross tab report
                    if (grpType.equalsIgnoreCase("columnPie") || grpType.equalsIgnoreCase("columnPie3D") || grpType.equalsIgnoreCase("Spider")) {
                        barChartColumnNames = new String[displayColumns.size()];
                    } else {
                        barChartColumnNames = new String[viewbyCount + 1];
                    }
                    barChartColumnTitles = new String[barChartColumnNames.length];
                    axis = new String[barChartColumnNames.length];
                    for (int i = 0; i < barChartColumnNames.length; i++) {
                        barChartColumnNames[i] = String.valueOf(displayColumns.get(i));
                        barChartColumnTitles[i] = String.valueOf(displayLabels.get(i));
                        axis[i] = "0";
                    }
                } else {//cross tab report
                    barChartColumnNames = new String[displayColumns.size()];
                    barChartColumnTitles = new String[barChartColumnNames.length];
                    axis = new String[barChartColumnNames.length];
                    for (int i = 0; i < barChartColumnNames.length; i++) {
                        barChartColumnNames[i] = String.valueOf(displayColumns.get(i));
                        barChartColumnTitles[i] = String.valueOf(displayLabels.get(i));
                        axis[i] = "0";
                    }
                }
                pieChartColumns = barChartColumnNames;
                GraphDetails.put("graphId", grpId);
                GraphDetails.put("graphName", ReportName);
                GraphDetails.put("graphClassName", graphClass);
                GraphDetails.put("graphTypeName", grpType);
                GraphDetails.put("viewByElementIds", viewByElementIds);
                GraphDetails.put("graphSize", "Medium");
                GraphDetails.put("graphWidth", String.valueOf(sizeDtls.get(0)));
                GraphDetails.put("graphHeight", String.valueOf(sizeDtls.get(1)));
                GraphDetails.put("barChartColumnNames", barChartColumnNames);
                GraphDetails.put("pieChartColumns", pieChartColumns);
                GraphDetails.put("barChartColumnTitles", barChartColumnTitles);
                GraphDetails.put("axis", axis);
                GraphDetails.put("graphLegend", "Y");
                GraphDetails.put("graphLegendLoc", "Bottom");
                GraphDetails.put("graphshowX", "Y");
                GraphDetails.put("graphshowY", "Y");
                GraphDetails.put("graphLYaxislabel", "");
                GraphDetails.put("graphRYaxislabel", "");
                GraphDetails.put("graphDrill", "Y");
                GraphDetails.put("graphBcolor", "fff");
                GraphDetails.put("graphFcolor", "fff");
                GraphDetails.put("graphData", "Y");
                GraphDetails.put("showGT", "N");
                GraphDetails.put("nbrFormat", "");
                GraphDetails.put("SwapColumn", "true");
                GraphDetails.put("graphSymbol", "");
                GraphDetails.put("measurePosition", "Bottom");
                GraphDetails.put("graphGridLines", "Y");
                GraphDetails.put("showLabels", "");
                GraphDetails.put("measureFormat", "");
                GraphDetails.put("measureValueRounding", "");
                GraphDetails.put("axisLabelPosition", "");
                GraphDetails.put("graphDisplayRows", "");

                //storing graph details info (Dual Axis ) in HashMap
                if (grpType.contains("Dual Axis") || grpType.contains("Dual") || grpType.contains("OverlaidBar") || grpType.contains("OverlaidArea") || grpType.contains("FeverChart")) {

                    barChartColumnTitles1 = new String[count + viewByElementIds.length];
                    barChartColumnNames1 = new String[barChartColumnTitles1.length];
                    barChartColumnTitles2 = new String[(barChartColumnNames.length - count)];
                    barChartColumnNames2 = new String[barChartColumnTitles2.length];

                    for (int j = 0; j < viewByElementIds.length; j++) {
                        barChartColumnTitles1[j] = barChartColumnTitles[j];
                        barChartColumnTitles2[j] = barChartColumnTitles[j];
                        barChartColumnNames1[j] = barChartColumnNames[j];
                        barChartColumnNames2[j] = barChartColumnNames[j];
                    }
                    for (int j = viewByElementIds.length; j < barChartColumnTitles1.length; j++) {
                        barChartColumnTitles1[j] = barChartColumnTitles[j];
                        barChartColumnNames1[j] = barChartColumnNames[j];
                    }
                    for (int j = barChartColumnTitles1.length; j < barChartColumnTitles.length; j++) {
                        barChartColumnTitles2[j - viewByElementIds.length] = barChartColumnTitles[j];
                        barChartColumnNames2[j - viewByElementIds.length] = barChartColumnNames[j];
                    }
                    GraphDetails.put("barChartColumnNames1", barChartColumnNames1);
                    GraphDetails.put("barChartColumnTitles1", barChartColumnTitles1);
                    GraphDetails.put("barChartColumnNames2", barChartColumnNames2);
                    GraphDetails.put("barChartColumnTitles2", barChartColumnTitles2);
                }


                if (GraphHashMap.get("AllGraphColumns") != null) {
                    allGraphColumns = (ArrayList) GraphHashMap.get("AllGraphColumns");
                }
                if (allGraphColumns == null) {
                    allGraphColumns = new ArrayList();
                }

                for (int i = viewbyCount; i < barChartColumnNames.length; i++) {
                    if (!allGraphColumns.contains(barChartColumnNames[i])) {
                        allGraphColumns.add(barChartColumnNames[i]);
                    }
                }
                GraphHashMap.put("AllGraphColumns", allGraphColumns);
                GraphHashMap.put(grpId, GraphDetails);
                GraphHashMap.put("graphIds", grpIds);

            } catch (Exception exception) {
                logger.error("Exception:", exception);
            }
        }
        return GraphHashMap;
    }

    //for building parameter section when user clicks on cancel button in report de3signing and also used in report customization
    public String buildParameters(HashMap ParametersHashMap) {
        StringBuffer tableBuffer = new StringBuffer("");
        ArrayList Parameters = (ArrayList) ParametersHashMap.get("Parameters");
        ArrayList ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");

        ArrayList TimeParameters = (ArrayList) ParametersHashMap.get("TimeParameters");
        ArrayList TimeParametersNames = (ArrayList) ParametersHashMap.get("TimeParametersNames");

        if (TimeParameters != null) {
            for (int i = 0; i < TimeParameters.size(); i++) {
                tableBuffer.append(" <li id='" + String.valueOf(TimeParameters.get(i)) + "' style='width:180px;color:white' class='navtitle-hover'>");
                tableBuffer.append("<table id='" + String.valueOf(TimeParametersNames.get(i)) + i + "'>");
                tableBuffer.append(" <tr>");
                tableBuffer.append(" <td >");
                tableBuffer.append("<a href=\"javascript:deleteTimeDimParam('" + String.valueOf(TimeParameters.get(i)) + "')\" class=\"ui-icon ui-icon-close\"></a>");
                tableBuffer.append("</td>");
                tableBuffer.append("<td style=\";color:black\">" + String.valueOf(TimeParametersNames.get(i)) + "</td>");
                tableBuffer.append("</tr>");
                tableBuffer.append("</table>");
                tableBuffer.append("</li>");
            }
        }
        if (Parameters != null) {
            for (int i = 0; i < Parameters.size(); i++) {
                tableBuffer.append(" <li id='param-" + String.valueOf(Parameters.get(i)) + "' style='width:180px;color:white' class='navtitle-hover'>");
                tableBuffer.append("<table id='" + String.valueOf(ParametersNames.get(i)) + i + "'>");
                tableBuffer.append(" <tr>");
                tableBuffer.append(" <td >");
                tableBuffer.append("  <a href=\"javascript:deleteParam('" + String.valueOf(Parameters.get(i)) + "')\" class=\"ui-icon ui-icon-close\"></a>");
                tableBuffer.append("</td>");
                tableBuffer.append("<td style=\"color:black\">" + String.valueOf(ParametersNames.get(i)) + "</td>");
                tableBuffer.append("</tr>");
                tableBuffer.append("</table>");
                tableBuffer.append("</li>");
            }
        }

        return tableBuffer.toString();
    }

    public String dispParameters(ArrayList paramIds, HashMap ParametersHashMap) {
        DisplayParameters disp = new DisplayParameters();
        HashMap TimeDimHashMap = null;
        String temp = "";
        try {
            TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");

            HashMap paramValues = new HashMap();
            for (int i = 0; i < paramIds.size(); i++) {
                paramValues.put(String.valueOf(paramIds.get(i)), "All");
            }
            temp = disp.displayParamwithTime(paramIds, TimeDimHashMap);
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return temp;
    }

    public static void main(String[] args) {
        int j;
        ReportTemplateBD td = new ReportTemplateBD();


    }

    public boolean getIsFxCharts() {
        return isFxCharts;
    }

    public void setIsFxCharts(boolean isFxCharts) {
        this.isFxCharts = isFxCharts;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public boolean isGetGraphIdFromSequence() {
        return getGraphIdFromSequence;
    }

    public void setGetGraphIdFromSequence(boolean getGraphIdFromSequence) {
        this.getGraphIdFromSequence = getGraphIdFromSequence;
    }

    //added by uday on 20-mar-2010
    public HashMap setWhatIfDefaults(String grpId, String grpType, HashMap GraphHashMap, HashMap ParametersHashMap, HashMap TableHashMap, HashMap GraphSizesDtlsHashMap, HashMap GraphClassesHashMap, HashMap ReportHashMap) {
        HashMap GraphDetails = null;
        GraphDetails = new HashMap();

        ArrayList allGraphColumns = null;

        ArrayList Parameters = (ArrayList) ParametersHashMap.get("Parameters");
        ArrayList ParameterNames = (ArrayList) ParametersHashMap.get("ParametersNames");

        ArrayList REP = (ArrayList) TableHashMap.get("REP");
        ArrayList REPNames = (ArrayList) TableHashMap.get("REPNames");

        ArrayList CEP = (ArrayList) TableHashMap.get("CEP");
        ArrayList CEPNames = (ArrayList) TableHashMap.get("CEPNames");

        ArrayList Measures = (ArrayList) TableHashMap.get("Measures");
        ArrayList MeasuresNames = (ArrayList) TableHashMap.get("MeasuresNames");

        ArrayList sizeDtls = (ArrayList) GraphSizesDtlsHashMap.get("Medium");
        String graphClass = String.valueOf(GraphClassesHashMap.get(grpType));
        String grpIds = "";
        String[] barChartColumnNames = null;
        String[] pieChartColumns = null;
        String[] barChartColumnTitles = null;
        String[] axis = null;

        String[] viewByElementIds = null;
        String[] viewBys = null;
        String[] viewByNames = null;
        String[] graphCols = new String[0];
        String[] graphColNames = new String[0];

        String whatIfScenarioName = "";
        int count = 1;

        String[] barChartColumnNames1 = null;
        String[] barChartColumnTitles1 = null;

        String[] barChartColumnNames2 = null;
        String[] barChartColumnTitles2 = null;

        try {
            if (GraphHashMap.get("graphIds") != null) {
                grpIds = (String) GraphHashMap.get("graphIds");
                grpIds = grpIds + "," + grpId;
            } else {
                grpIds = grpId;
            }
            if (REP != null) {
                viewByElementIds = (String[]) REP.toArray(new String[0]);
                viewBys = viewByElementIds;
                viewByNames = (String[]) REPNames.toArray(new String[0]);
            } else {
                if (Parameters != null && Parameters.size() != 0) {
                    viewByElementIds = new String[1];
                    viewByElementIds[0] = String.valueOf(Parameters.get(0));

                    viewBys = viewByElementIds;
                    viewByNames = new String[1];
                    viewByNames[0] = String.valueOf(ParameterNames.get(0));
                }
            }

            if (Measures != null && Measures.size() != 0) {
                if (grpType.equalsIgnoreCase("columnPie") || grpType.equalsIgnoreCase("columnPie3D") || grpType.equalsIgnoreCase("Spider")) {
                    graphCols = (String[]) Measures.toArray(new String[0]);
                    graphColNames = (String[]) MeasuresNames.toArray(new String[0]);
                } else {
                    graphCols = new String[1];
                    graphColNames = new String[1];
                    graphCols[0] = (String) Measures.toArray(new String[0])[0];
                    graphColNames[0] = (String) MeasuresNames.toArray(new String[0])[0];
                }

                barChartColumnNames = new String[viewByElementIds.length + graphCols.length];
                pieChartColumns = new String[barChartColumnNames.length];
                barChartColumnTitles = new String[barChartColumnNames.length];
                axis = new String[barChartColumnNames.length];

                for (int j = 0; j < viewByElementIds.length; j++) {
                    if (viewByElementIds[j].equalsIgnoreCase("Time")) {
                        viewBys[j] = viewByElementIds[j];
                        barChartColumnNames[j] = viewBys[j];
                        pieChartColumns[j] = viewBys[j];
                    } else {
                        if (!(viewByElementIds[j].contains("A_"))) {
                            viewBys[j] = "A_" + viewByElementIds[j];
                            barChartColumnNames[j] = viewBys[j];
                            pieChartColumns[j] = viewBys[j];
                        } else {
                            viewBys[j] = viewByElementIds[j];
                            barChartColumnNames[j] = viewBys[j];
                            pieChartColumns[j] = viewBys[j];
                        }
                    }
                    barChartColumnTitles[j] = viewByNames[j];
                    axis[j] = "0";
                }
                for (int j = viewByElementIds.length; j < (graphCols.length + viewByElementIds.length); j++) {
                    if (!(graphCols[j - viewByElementIds.length].contains("A_"))) {
                        barChartColumnNames[j] = "A_" + graphCols[j - viewByElementIds.length];
                        pieChartColumns[j] = "A_" + graphCols[j - viewByElementIds.length];
                    } else {
                        barChartColumnNames[j] = graphCols[j - viewByElementIds.length];
                        pieChartColumns[j] = graphCols[j - viewByElementIds.length];
                    }
                    barChartColumnTitles[j] = graphColNames[j - viewByElementIds.length];
                    axis[j] = "0";
                }

                if (grpType.contains("Dual Axis") || grpType.contains("Dual") || grpType.contains("OverlaidBar") || grpType.contains("OverlaidArea") || grpType.contains("FeverChart")) {

                    barChartColumnTitles1 = new String[count + viewByElementIds.length];
                    barChartColumnNames1 = new String[barChartColumnTitles1.length];
                    barChartColumnTitles2 = new String[(barChartColumnNames.length - count)];
                    barChartColumnNames2 = new String[barChartColumnTitles2.length];

                    for (int j = 0; j < viewByElementIds.length; j++) {
                        barChartColumnTitles1[j] = barChartColumnTitles[j];
                        barChartColumnTitles2[j] = barChartColumnTitles[j];
                        barChartColumnNames1[j] = barChartColumnNames[j];
                        barChartColumnNames2[j] = barChartColumnNames[j];
                    }
                    for (int j = viewByElementIds.length; j < barChartColumnTitles1.length; j++) {
                        barChartColumnTitles1[j] = barChartColumnTitles[j];
                        barChartColumnNames1[j] = barChartColumnNames[j];
                    }
                    for (int j = barChartColumnTitles1.length; j < barChartColumnTitles.length; j++) {
                        barChartColumnTitles2[j - viewByElementIds.length] = barChartColumnTitles[j];
                        barChartColumnNames2[j - viewByElementIds.length] = barChartColumnNames[j];
                    }
                    GraphDetails.put("barChartColumnNames1", barChartColumnNames1);
                    GraphDetails.put("barChartColumnTitles1", barChartColumnTitles1);
                    GraphDetails.put("barChartColumnNames2", barChartColumnNames2);
                    GraphDetails.put("barChartColumnTitles2", barChartColumnTitles2);
                }
            }
            if (ReportHashMap.get("whatIfScenarioName") != null) {
                whatIfScenarioName = String.valueOf(ReportHashMap.get("whatIfScenarioName"));
            }

            GraphDetails.put("graphId", grpId);
            GraphDetails.put("graphName", whatIfScenarioName);
            GraphDetails.put("graphClassName", graphClass);
            GraphDetails.put("graphTypeName", grpType);
            GraphDetails.put("viewByElementIds", viewBys);
            GraphDetails.put("grpSize", "Medium");
            GraphDetails.put("graphWidth", String.valueOf(sizeDtls.get(0)));
            GraphDetails.put("graphHeight", String.valueOf(sizeDtls.get(1)));
            GraphDetails.put("barChartColumnNames", barChartColumnNames);
            GraphDetails.put("pieChartColumns", pieChartColumns);
            GraphDetails.put("barChartColumnTitles", barChartColumnTitles);
            GraphDetails.put("axis", axis);
            GraphDetails.put("graphLegend", "Y");
            GraphDetails.put("graphLegendLoc", "Bottom");
            GraphDetails.put("graphshowX", "Y");
            GraphDetails.put("graphshowY", "Y");
            GraphDetails.put("graphLYaxislabel", "");
            GraphDetails.put("graphRYaxislabel", "");
            GraphDetails.put("graphDrill", "Y");
            GraphDetails.put("graphBcolor", "fff");
            GraphDetails.put("graphFcolor", "fff");
            GraphDetails.put("graphData", "Y");
            GraphDetails.put("SwapColumn", "true");
            GraphDetails.put("measurePosition", "Bottom");
            //storing graph details info (Dual Axis ) in HashMap



            if (GraphHashMap.get("AllGraphColumns") != null) {
                allGraphColumns = (ArrayList) GraphHashMap.get("AllGraphColumns");
            }
            if (allGraphColumns == null) {
                allGraphColumns = new ArrayList();
            }

            for (int j = 0; j < graphCols.length; j++) {
                if (!allGraphColumns.contains(graphCols[j])) {
                    allGraphColumns.add(graphCols[j]);
                }
            }
            GraphHashMap.put("AllGraphColumns", allGraphColumns);
            GraphHashMap.put(grpId, GraphDetails);
            GraphHashMap.put("graphIds", grpIds);

            //////.println("after adding defaults is:: " + GraphHashMap);
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }

        return GraphHashMap;
    }

    public String buildWhatIfScenarioGraph(Container container, HttpServletRequest request, HttpServletResponse response, String grpIds) {

        return null;
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
            String[] prevAxis = (String[]) GraphDetails.get("axis");


            String[] barChartColumnNames = new String[viewByElementIds.length + (prevBarChartColumnNames.length - prevViewByElementIds.length)];
            String[] pieChartColumns = new String[barChartColumnNames.length];
            String[] barChartColumnTitles = new String[barChartColumnNames.length];
            String[] axis = new String[barChartColumnNames.length];

            for (int j = 0; j < viewByElementIds.length; j++) {
                //barChartColumnNames[j] = viewByElementIds[j];
                //pieChartColumns[j] = viewByElementIds[j];



                if (!(viewByElementIds[j].contains("A_"))) {
                    barChartColumnNames[j] = "A_" + viewByElementIds[j];
                    pieChartColumns[j] = "A_" + viewByElementIds[j];
                } else {
                    barChartColumnNames[j] = viewByElementIds[j];
                    pieChartColumns[j] = viewByElementIds[j];
                }
                barChartColumnTitles[j] = viewByElementIds[j];
                axis[j] = "0";
            }
            for (int j = prevViewByElementIds.length; j < prevBarChartColumnNames.length; j++) {
                //barChartColumnNames[viewByElementIds.length + (j - prevViewByElementIds.length)] = prevBarChartColumnNames[j];
                //pieChartColumns[viewByElementIds.length + (j - prevViewByElementIds.length)] = prevPieChartColumns[j];


                if (!(prevBarChartColumnNames[j].contains("A_"))) {
                    barChartColumnNames[viewByElementIds.length + (j - prevViewByElementIds.length)] = "A_" + prevBarChartColumnNames[j];
                    pieChartColumns[viewByElementIds.length + (j - prevViewByElementIds.length)] = "A_" + prevPieChartColumns[j];
                } else {
                    barChartColumnNames[viewByElementIds.length + (j - prevViewByElementIds.length)] = prevBarChartColumnNames[j];
                    pieChartColumns[viewByElementIds.length + (j - prevViewByElementIds.length)] = prevPieChartColumns[j];
                }
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

    public String buildWhatIfTable(HashMap ParametersHashMap, HashMap TableHashMap, HashMap GraphHashMap, String ctxPath) {

        PbReportCollection collect = new PbReportCollection();
        PbReportQuery repQuery = new PbReportQuery();
        ArrayList displayLabels = new ArrayList();
        String[] dbColumns = new String[0];

        ArrayList reportQryElementIds = new ArrayList();
        ArrayList reportQryAggregations = new ArrayList();
        ArrayList reportQryColNames = new ArrayList();
        HashMap reportIncomingParameters = new HashMap();
        HashMap paramValues = new HashMap();
        ArrayList REP = null;
        ArrayList CEP = null;
        ArrayList Measures = null;
        ArrayList MeasuresNames = null;
        ArrayList Parameters = null;

        ArrayList REPNames = null;
        ArrayList CEPNames = null;
        ArrayList ParametersNames = null;

        HashMap TimeDimHashMap = new HashMap();
        ArrayList TimeDetailstList = new ArrayList();

        PbReturnObject retObj = null;
        StringBuffer tableBuffer = new StringBuffer("");
        int endCount = 10;
        int viewByCount = 0;

        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
        TimeDetailstList = (ArrayList) ParametersHashMap.get("TimeDetailstList");

        //added by santhosh.kumar on 04/01/2009 for table Properties
        HashMap TableProperties = null;
        String defaultSortedCol = null;
        String chkTtlValues = null;
        String chkSubTtlValues = null;
        String chkAvgValues = null;
        String selSymbolsValues = null;
        String chkOvrAllMaxValues = null;
        String chkOvrAllMinValues = null;
        String chkCatMaxValues = null;
        String chkCatMinValues = null;
        String nbrFormat = "";

        String[] columnTypes = null;

        ArrayList tempList = null;
        HashMap columnProperties = null;
        //end of variables

        if (TableHashMap.get("REP") != null) {
            REP = (ArrayList) TableHashMap.get("REP");
            REPNames = (ArrayList) TableHashMap.get("REPNames");
        }
        if (TableHashMap.get("CEP") != null) {
            CEP = (ArrayList) TableHashMap.get("CEP");
            CEPNames = (ArrayList) TableHashMap.get("CEPNames");
        }
        if (TableHashMap.get("Measures") != null) {
            Measures = (ArrayList) TableHashMap.get("Measures");
            MeasuresNames = (ArrayList) TableHashMap.get("MeasuresNames");
            MeasuresNames = new ArrayList();

        }
        if (ParametersHashMap.get("Parameters") != null) {
            Parameters = (ArrayList) ParametersHashMap.get("Parameters");
            ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
        }
        if (REP == null) {
            REP = new ArrayList();
            REPNames = new ArrayList();
            if (Parameters != null && Parameters.size() != 0) {
                REP.add(String.valueOf(Parameters.get(0)));
                REPNames.add(String.valueOf(ParametersNames.get(0)));
            }
        }
        if (CEP == null) {
            CEP = new ArrayList();
        }
        if (Measures == null) {
            Measures = new ArrayList();
        }

        if (TableHashMap.get("TableProperties") != null) {
            TableProperties = (HashMap) TableHashMap.get("TableProperties");
            defaultSortedCol = String.valueOf(TableProperties.get("DefaultSortedColumn"));
            chkTtlValues = (String) TableProperties.get("ShowTotalValues");
            chkSubTtlValues = (String) TableProperties.get("ShowSubTotalValues");
            chkAvgValues = (String) TableProperties.get("ShowAvgValues");
            selSymbolsValues = (String) TableProperties.get("ColumnSymbols");
            chkOvrAllMinValues = (String) TableProperties.get("ShowOvrAllMinValues");
            chkOvrAllMaxValues = (String) TableProperties.get("ShowOvrAllMaxValues");
            chkCatMinValues = (String) TableProperties.get("ShowCatMinValues");
            chkCatMaxValues = (String) TableProperties.get("ShowCatMaxValues");
            //code to get column properties for normar report (other than cross tab report)
            columnProperties = (HashMap) TableProperties.get("ColumnProperties");
        }

        if (REP.size() != 0 && Measures.size() != 0) {
            for (int i = 0; i < Parameters.size(); i++) {
                if (paramValues.get(String.valueOf(Parameters.get(i))) == null) {
                    paramValues.put(String.valueOf(Parameters.get(i)), "All");
                    reportIncomingParameters.put("CBOAPR" + String.valueOf(Parameters.get(i)), null);
                }
            }

            collect.reportIncomingParameters = reportIncomingParameters;
            collect.reportColViewbyValues = CEP;
            collect.reportRowViewbyValues = REP;
            collect.timeDetailsArray = TimeDetailstList;
            collect.timeDetailsMap = TimeDimHashMap;
            collect.defaultSortedColumn = defaultSortedCol;

            collect.getParamMetaDataForReportDesigner();


            for (int j = 0; j < Measures.size(); j++) {
                if (!reportQryElementIds.contains(String.valueOf(Measures.get(j)).replace("A_", ""))) {
                    reportQryElementIds.add(String.valueOf(Measures.get(j)).replace("A_", ""));
                }
            }
            if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
                reportQryAggregations = DAO.getReportQryAggregations(reportQryElementIds);
                reportQryColNames = DAO.getReportQryColNames();

                collect.reportQryElementIds = reportQryElementIds;
                collect.reportQryAggregations = reportQryAggregations;
                collect.reportQryColNames = reportQryColNames;

                repQuery.setQryColumns(collect.reportQryElementIds);
                repQuery.setColAggration(collect.reportQryAggregations);

                repQuery.setDefaultMeasure(String.valueOf(reportQryElementIds.get(0)));
                repQuery.setDefaultMeasureSumm(String.valueOf(reportQryAggregations.get(0)));
            }
            repQuery.setRowViewbyCols(REP);
            repQuery.setColViewbyCols(CEP);
            repQuery.setParamValue(paramValues);

            repQuery.setTimeDetails(TimeDetailstList); //assigning time details array to report query

            //added by santhosh.kumar@progenbusiness.com on 03/12/2009 fro setting user id and biz roles
            repQuery.setBizRoles(getBizRoles());
            repQuery.setUserId(getUserId());

            if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
                ///////////////default sorted column
                boolean defSortExists = false;

                if (collect.defaultSortedColumn != null) {
                    if (collect.reportRowViewbyValues.contains(collect.defaultSortedColumn)) {
                        for (int i = 0; i < collect.reportRowViewbyValues.size(); i++) {
                            if (collect.reportRowViewbyValues.get(i).toString().equalsIgnoreCase(collect.defaultSortedColumn)) {
                                collect.defaultSortedColumn = String.valueOf(i + 1);
                                defSortExists = true;
                            }
                        }

                    } else if (collect.reportColViewbyValues != null && collect.reportColViewbyValues.size() == 0) {
                        if (collect.reportQryElementIds.contains(collect.defaultSortedColumn)) {
                            defSortExists = true;
                            for (int i = 0; i < collect.reportQryElementIds.size(); i++) {
                                if (collect.reportQryElementIds.get(i).toString().equalsIgnoreCase(collect.defaultSortedColumn)) {
                                    defSortExists = true;
                                    collect.defaultSortedColumn = String.valueOf(i + collect.reportRowViewbyValues.size() + 1);
                                }
                            }
                        }
                    } else {
                        //
                    }
                }
                if (defSortExists) {
                    repQuery.setDefaultSortedColumn(collect.defaultSortedColumn);//need to be removed later
                } else {
                    //repQuery.setDefaultSortedColumn(collect.defaultSortedColumn);//need to be removed later
                }
                //default sorted column


                retObj = repQuery.getPbReturnObject(String.valueOf(reportQryElementIds.get(0)));

            }
            if (retObj != null) {
                dbColumns = retObj.getColumnNames();
                displayLabels = (ArrayList) REPNames.clone();
                if (CEP != null && CEP.size() != 0) {
                    for (int nonViewByIndex = collect.reportRowViewbyValues.size(); nonViewByIndex < (collect.reportRowViewbyValues.size() + repQuery.crossTabNonViewBy.size()); nonViewByIndex++) {
                        displayLabels.add(String.valueOf(repQuery.crossTabNonViewByMap.get(dbColumns[nonViewByIndex])));
                        MeasuresNames = reportQryColNames;
                    }
                } else {
                    for (int nonViewByIndex = collect.reportRowViewbyValues.size(); nonViewByIndex < (collect.reportRowViewbyValues.size() + repQuery.NonViewByMap.size()); nonViewByIndex++) {
                        displayLabels.add(String.valueOf(repQuery.NonViewByMap.get(dbColumns[nonViewByIndex])));
                        MeasuresNames.add(String.valueOf(repQuery.NonViewByMap.get(dbColumns[nonViewByIndex])));
                    }
                }
                viewByCount = collect.reportRowViewbyValues.size();
                if (MeasuresNames.size() == 0) {
                    TableHashMap.put("MeasuresNames", Measures);
                } else {
                    TableHashMap.put("MeasuresNames", MeasuresNames);
                }


                ////////.println("TableHashMap udayyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy is:: " + TableHashMap);
                dbColumns = retObj.getColumnNames();
                columnTypes = retObj.getColumnTypes();
                BigDecimal subTotals[] = new BigDecimal[displayLabels.size()];
                BigDecimal grandTotals[] = new BigDecimal[displayLabels.size()];

                BigDecimal avgwhatifTotals[] = new BigDecimal[displayLabels.size()];

                BigDecimal max[] = new BigDecimal[displayLabels.size()];
                BigDecimal min[] = new BigDecimal[displayLabels.size()];
                BigDecimal catMaxwhatif[] = new BigDecimal[displayLabels.size()];
                BigDecimal catMin[] = new BigDecimal[displayLabels.size()];

                for (int i = 0; i < subTotals.length; i++) {
                    subTotals[i] = new BigDecimal("0");
                    grandTotals[i] = new BigDecimal("0");
                    avgwhatifTotals[i] = new BigDecimal("0");
                    max[i] = new BigDecimal("0");
                    min[i] = new BigDecimal("0");
                    catMaxwhatif[i] = new BigDecimal("0");
                    catMin[i] = new BigDecimal("0");
                }

                tableBuffer.append("<table cellspacing=\"1\" class=\"tablesorter\" id=\"tablesorter\" width=\"100px\">");
                tableBuffer.append("<thead>");
                tableBuffer.append("<tr valign=\"top\">");
                for (int k = 0; k < displayLabels.size(); k++) {
                    tableBuffer.append("<th>" + displayLabels.get(k) + "</th> ");
                }
                tableBuffer.append("</tr>");
                tableBuffer.append("</thead>");
                tableBuffer.append("<tfoot>");
                tableBuffer.append("</tfoot>");
                tableBuffer.append("<tbody>");

                if (retObj.getRowCount() <= endCount) {
                    endCount = retObj.getRowCount();
                }
                ArrayList singleColPropList = null;
                for (int rowId = 0; rowId < endCount; rowId++) {
                    for (int j = 0; j < displayLabels.size(); j++) {
                        if (columnTypes[j].toUpperCase().equalsIgnoreCase("NUMBER")) {
                            subTotals[j] = subTotals[j].add(retObj.getFieldValueBigDecimal(rowId, dbColumns[j]));
                            grandTotals[j] = grandTotals[j].add(retObj.getFieldValueBigDecimal(rowId, dbColumns[j]));
                            avgwhatifTotals[j] = avgwhatifTotals[j].add(retObj.getFieldValueBigDecimal(rowId, dbColumns[j]));

                            if (rowId == 0) {
                                max[j] = retObj.getFieldValueBigDecimal(rowId, dbColumns[j]);
                                min[j] = retObj.getFieldValueBigDecimal(rowId, dbColumns[j]);
                                catMaxwhatif[j] = retObj.getFieldValueBigDecimal(rowId, dbColumns[j]);
                                catMin[j] = retObj.getFieldValueBigDecimal(rowId, dbColumns[j]);
                            }

                            if (retObj.getFieldValueBigDecimal(rowId, dbColumns[j]).doubleValue() > max[j].doubleValue()) {
                                max[j] = retObj.getFieldValueBigDecimal(rowId, dbColumns[j]);
                            }
                            if (retObj.getFieldValueBigDecimal(rowId, dbColumns[j]).doubleValue() < min[j].doubleValue()) {
                                min[j] = retObj.getFieldValueBigDecimal(rowId, dbColumns[j]);
                            }

                            if (retObj.getFieldValueBigDecimal(rowId, dbColumns[j]).doubleValue() > catMaxwhatif[j].doubleValue()) {
                                catMaxwhatif[j] = retObj.getFieldValueBigDecimal(rowId, dbColumns[j]);
                            }
                            if (retObj.getFieldValueBigDecimal(rowId, dbColumns[j]).doubleValue() < catMin[j].doubleValue()) {
                                catMin[j] = retObj.getFieldValueBigDecimal(rowId, dbColumns[j]);
                            }
                        }
                    }

                    tableBuffer.append("<tr valign = \"top\">");

                    for (int colId = 0; colId < viewByCount; colId++) {
                        tableBuffer.append("<td align='left'>");
                        tableBuffer.append(retObj.getFieldValueString(rowId, dbColumns[colId]));
                        tableBuffer.append("</td>");
                    }

                    if (CEP.size() == 0) {
                        for (int colId = viewByCount; colId < dbColumns.length; colId++) {
                            singleColPropList = (ArrayList) columnProperties.get(dbColumns[colId]);
                            if (columnTypes[colId].toUpperCase().equalsIgnoreCase("NUMBER")) {
                                tableBuffer.append("<td align='right'>");

                                tableBuffer.append(singleColPropList.get(7));
                                tableBuffer.append("" + retObj.getModifiedNumber(retObj.getFieldValueBigDecimal(rowId, dbColumns[colId]), nbrFormat));
                            } else {
                                tableBuffer.append("<td align='left'>");
                                tableBuffer.append(retObj.getFieldValueString(rowId, dbColumns[colId]));
                            }
                            tableBuffer.append("</td>");
                        }
                        tableBuffer.append("</tr>");
                    } else {
                        singleColPropList = (ArrayList) columnProperties.get("A_" + reportQryElementIds.get(0));
                        for (int colId = viewByCount; colId < dbColumns.length; colId++) {
                            if (columnTypes[colId].toUpperCase().equalsIgnoreCase("NUMBER")) {
                                tableBuffer.append("<td align='right'>");
                                tableBuffer.append(singleColPropList.get(7));
                                tableBuffer.append("" + retObj.getModifiedNumber(retObj.getFieldValueBigDecimal(rowId, dbColumns[colId]), nbrFormat));
                            } else {
                                tableBuffer.append("<td align='left'>");
                                tableBuffer.append(retObj.getFieldValueString(rowId, dbColumns[colId]));
                            }
                            tableBuffer.append("</td>");
                        }
                        tableBuffer.append("</tr>");
                    }
                }

                //end of code for displaying table content(excluding extra properties)

                if (retObj.getRowCount() != 0) {
                    //start of displaying extra rows like max,min,avg,total,sub total etc
                    //displaying  total values
                    if (chkTtlValues != null && chkTtlValues.equalsIgnoreCase("Y")) {
                        tableBuffer.append("<Tr valign = \"top\">");
                        for (int j = 0; j < REP.size(); j++) {
                            if (j == 0) {
                                tableBuffer.append("<th align='left'>");
                                tableBuffer.append("&nbsp;<strong>Total</strong>");
                                tableBuffer.append("</th>");
                            } else {
                                if (columnTypes[j].equalsIgnoreCase("NUMBER")) {
                                    tableBuffer.append("<td align='right'>");
                                    if (singleColPropList.get(0).toString().equalsIgnoreCase("Y")) {
                                        tableBuffer.append(retObj.getModifiedNumber(grandTotals[j], nbrFormat));
                                    }
                                } else {
                                    tableBuffer.append("<td align='left'>&nbsp;");
                                    tableBuffer.append("");
                                }
                                tableBuffer.append("</td>");
                            }
                        }
                        for (int j = REP.size(); j < dbColumns.length; j++) {
                            if (CEP.size() == 0) {
                                singleColPropList = (ArrayList) columnProperties.get(dbColumns[j]);
                            } else {
                                singleColPropList = (ArrayList) columnProperties.get("A_" + reportQryElementIds.get(0));
                            }
                            if (columnTypes[j].toUpperCase().equalsIgnoreCase("NUMBER")) {
                                tableBuffer.append("<td align='right'>");
                                if (singleColPropList.get(0).toString().equalsIgnoreCase("Y")) {
                                    tableBuffer.append(singleColPropList.get(7));
                                    tableBuffer.append(retObj.getModifiedNumber(grandTotals[j], nbrFormat));
                                }
                            } else {
                                tableBuffer.append("<td align='left'>&nbsp;");
                                tableBuffer.append("");
                            }
                            tableBuffer.append("</td>");
                        }
                        tableBuffer.append("</Tr>");
                    }
                    //displaying sub total values
                    if (chkSubTtlValues != null && chkSubTtlValues.equalsIgnoreCase("Y")) {
                        tableBuffer.append("<Tr valign = \"top\">");
                        for (int j = 0; j < REP.size(); j++) {
                            if (j == 0) {
                                tableBuffer.append("<th align='left'>");
                                tableBuffer.append("&nbsp;<strong>Sub Total</strong>");
                                tableBuffer.append("</th>");
                            } else {
                                if (columnTypes[j].equalsIgnoreCase("NUMBER")) {
                                    tableBuffer.append("<td align='right'>");
                                    if (singleColPropList.get(1).toString().equalsIgnoreCase("Y")) {
                                        tableBuffer.append(retObj.getModifiedNumber(subTotals[j], nbrFormat));
                                    }
                                } else {
                                    tableBuffer.append("<td align='left'>");
                                    tableBuffer.append("");
                                }
                                tableBuffer.append("</td>");
                            }
                        }
                        for (int j = REP.size(); j < dbColumns.length; j++) {
                            if (CEP.size() == 0) {
                                singleColPropList = (ArrayList) columnProperties.get(dbColumns[j]);
                            } else {
                                singleColPropList = (ArrayList) columnProperties.get("A_" + reportQryElementIds.get(0));
                            }
                            if (columnTypes[j].toUpperCase().equalsIgnoreCase("NUMBER")) {
                                tableBuffer.append("<td align='right'>");
                                if (singleColPropList.get(1).toString().equalsIgnoreCase("Y")) {
                                    tableBuffer.append(singleColPropList.get(7));
                                    tableBuffer.append(retObj.getModifiedNumber(subTotals[j], nbrFormat));
                                }
                            } else {
                                tableBuffer.append("<td align='left'>&nbsp;");
                                tableBuffer.append("");
                            }
                            tableBuffer.append("</td>");
                        }
                        tableBuffer.append("</Tr>");
                    }

                    //displaying average values
                    if (chkAvgValues != null && chkAvgValues.equalsIgnoreCase("Y")) {
                        tableBuffer.append("<Tr valign = \"top\">");
                        for (int j = 0; j < REP.size(); j++) {
                            if (j == 0) {
                                tableBuffer.append("<th align='left'>");
                                tableBuffer.append("&nbsp;Average");
                                tableBuffer.append("</th>");
                            } else {
                                if (columnTypes[j].equalsIgnoreCase("NUMBER")) {
                                    tableBuffer.append("<td align='right'>");
                                    if (singleColPropList.get(2).toString().equalsIgnoreCase("Y")) {
                                        //  ////.println("tableBuffer in reptempbd at 4868----"+tableBuffer);
                                        tableBuffer.append(retObj.getModifiedNumber(avgwhatifTotals[j], nbrFormat));
                                        // ////.println("tableBuffer in reptempbd at 4869---"+tableBuffer);
                                    }
                                } else {
                                    tableBuffer.append("<td align='left'>");
                                    tableBuffer.append("");
                                }
                                tableBuffer.append("</td>");
                            }
                        }
                        for (int j = REP.size(); j < dbColumns.length; j++) {
                            if (CEP.size() == 0) {
                                singleColPropList = (ArrayList) columnProperties.get(dbColumns[j]);
                            } else {
                                singleColPropList = (ArrayList) columnProperties.get("A_" + reportQryElementIds.get(0));
                            }
                            if (columnTypes[j].toUpperCase().equalsIgnoreCase("NUMBER")) {
                                tableBuffer.append("<td align='right'>");
                                if (singleColPropList.get(2).toString().equalsIgnoreCase("Y")) {
                                    tableBuffer.append(singleColPropList.get(7));
                                    tableBuffer.append(retObj.getModifiedNumber(avgwhatifTotals[j], nbrFormat));
                                }
                            } else {
                                tableBuffer.append("<td align='left'>&nbsp;");
                                tableBuffer.append("");
                            }
                            tableBuffer.append("</td>");
                        }
                        tableBuffer.append("</Tr>");
                    }

                    //displaying overall  maximum values
                    if (chkOvrAllMinValues != null && chkOvrAllMinValues.equalsIgnoreCase("Y")) {
                        tableBuffer.append("<Tr valign = \"top\">");
                        for (int j = 0; j < REP.size(); j++) {
                            if (j == 0) {
                                tableBuffer.append("<th align='left'>");
                                tableBuffer.append("&nbsp; Max");
                                tableBuffer.append("</th>");
                            } else {
                                if (columnTypes[j].equalsIgnoreCase("NUMBER")) {
                                    tableBuffer.append("<td align='right'>");
                                    if (singleColPropList.get(4).toString().equalsIgnoreCase("Y")) {
                                        tableBuffer.append(retObj.getModifiedNumber(min[j], nbrFormat));
                                    }
                                } else {
                                    tableBuffer.append("<td align='left'>");
                                    tableBuffer.append("");
                                }
                                tableBuffer.append("</td>");
                            }
                        }
                        for (int j = REP.size(); j < dbColumns.length; j++) {
                            if (CEP.size() == 0) {
                                singleColPropList = (ArrayList) columnProperties.get(dbColumns[j]);
                            } else {
                                singleColPropList = (ArrayList) columnProperties.get("A_" + reportQryElementIds.get(0));
                            }
                            if (columnTypes[j].toUpperCase().equalsIgnoreCase("NUMBER")) {
                                tableBuffer.append("<td align='right'>");
                                if (singleColPropList.get(4).toString().equalsIgnoreCase("Y")) {
                                    tableBuffer.append(singleColPropList.get(7));
                                    tableBuffer.append(retObj.getModifiedNumber(min[j], nbrFormat));
                                }
                            } else {
                                tableBuffer.append("<td align='left'>&nbsp;");
                                tableBuffer.append("");
                            }
                            tableBuffer.append("</td>");
                        }
                        tableBuffer.append("</Tr>");
                    }
                    //displaying overall  minimum values
                    if (chkOvrAllMaxValues != null && chkOvrAllMaxValues.equalsIgnoreCase("Y")) {
                        tableBuffer.append("<Tr valign = \"top\">");
                        for (int j = 0; j < REP.size(); j++) {
                            if (j == 0) {
                                tableBuffer.append("<th align='left'>");
                                tableBuffer.append("&nbsp; Min");
                                tableBuffer.append("</th>");
                            } else {
                                if (columnTypes[j].equalsIgnoreCase("NUMBER")) {
                                    tableBuffer.append("<td align='right'>");//
                                    if (singleColPropList.get(3).toString().equalsIgnoreCase("Y")) {
                                        tableBuffer.append(retObj.getModifiedNumber(max[j], nbrFormat));
                                    }
                                } else {
                                    tableBuffer.append("<td align='left'>");
                                    tableBuffer.append("");
                                }
                                tableBuffer.append("</td>");
                            }
                        }
                        for (int j = REP.size(); j < dbColumns.length; j++) {

                            if (CEP.size() == 0) {
                                singleColPropList = (ArrayList) columnProperties.get(dbColumns[j]);
                            } else {
                                singleColPropList = (ArrayList) columnProperties.get("A_" + reportQryElementIds.get(0));
                            }
                            if (columnTypes[j].toUpperCase().equalsIgnoreCase("NUMBER")) {
                                tableBuffer.append("<td align='right'>");
                                if (singleColPropList.get(3).toString().equalsIgnoreCase("Y")) {
                                    tableBuffer.append(singleColPropList.get(7));
                                    tableBuffer.append(retObj.getModifiedNumber(max[j], nbrFormat));
                                }
                            } else {
                                tableBuffer.append("<td align='left'>&nbsp;");
                                tableBuffer.append("");
                            }
                            tableBuffer.append("</td>");
                        }
                        tableBuffer.append("</Tr>");
                    }
                    //displaying category minimum values
                    if (chkCatMinValues != null && chkCatMinValues.equalsIgnoreCase("Y")) {
                        tableBuffer.append("<Tr valign = \"top\">");
                        for (int j = 0; j < REP.size(); j++) {
                            if (j == 0) {
                                tableBuffer.append("<th align='left'>");
                                tableBuffer.append("&nbsp;Category Min");
                                tableBuffer.append("</th>");
                            } else {
                                if (columnTypes[j].equalsIgnoreCase("NUMBER")) {
                                    tableBuffer.append("<td align='right'>");
                                    if (singleColPropList.get(6).toString().equalsIgnoreCase("Y")) {
                                        tableBuffer.append(retObj.getModifiedNumber(catMin[j], nbrFormat));
                                    }
                                } else {
                                    tableBuffer.append("<td align='left'>");
                                    tableBuffer.append("");
                                }
                                tableBuffer.append("</td>");
                            }
                        }
                        for (int j = REP.size(); j < dbColumns.length; j++) {
                            if (CEP.size() == 0) {
                                singleColPropList = (ArrayList) columnProperties.get(dbColumns[j]);
                            } else {
                                singleColPropList = (ArrayList) columnProperties.get("A_" + reportQryElementIds.get(0));
                            }
                            if (columnTypes[j].toUpperCase().equalsIgnoreCase("NUMBER")) {
                                tableBuffer.append("<td align='right'>");
                                if (singleColPropList.get(6).toString().equalsIgnoreCase("Y")) {
                                    tableBuffer.append(singleColPropList.get(7));
                                    tableBuffer.append(retObj.getModifiedNumber(catMin[j], nbrFormat));
                                }
                            } else {
                                tableBuffer.append("<td align='left'>&nbsp;");
                                tableBuffer.append("");
                            }
                            tableBuffer.append("</td>");
                        }
                        tableBuffer.append("</Tr>");
                    }

                    //displaying category maximum values
                    if (chkCatMaxValues != null && chkCatMaxValues.equalsIgnoreCase("Y")) {
                        tableBuffer.append("<Tr valign = \"top\">");
                        for (int j = 0; j < REP.size(); j++) {
                            if (j == 0) {
                                tableBuffer.append("<th align='left'>");
                                tableBuffer.append("&nbsp;Category Max");
                                tableBuffer.append("</th>");
                            } else {
                                if (columnTypes[j].equalsIgnoreCase("NUMBER")) {
                                    tableBuffer.append("<td align='right'>");
                                    if (singleColPropList.get(5).toString().equalsIgnoreCase("Y")) {
                                        tableBuffer.append(retObj.getModifiedNumber(catMaxwhatif[j], nbrFormat));
                                    }
                                } else {
                                    tableBuffer.append("<td align='left'>");
                                    tableBuffer.append("");
                                }
                                tableBuffer.append("</td>");
                            }
                        }
                        for (int j = REP.size(); j < dbColumns.length; j++) {
                            if (CEP.size() == 0) {
                                singleColPropList = (ArrayList) columnProperties.get(dbColumns[j]);
                            } else {
                                singleColPropList = (ArrayList) columnProperties.get("A_" + reportQryElementIds.get(0));
                            }
                            if (columnTypes[j].toUpperCase().equalsIgnoreCase("NUMBER")) {
                                tableBuffer.append("<td align='right'>");
                                if (singleColPropList.get(5).toString().equalsIgnoreCase("Y")) {
                                    tableBuffer.append(singleColPropList.get(7));
                                    tableBuffer.append(retObj.getModifiedNumber(catMaxwhatif[j], nbrFormat));
                                }
                            } else {
                                tableBuffer.append("<td align='left'>&nbsp;");
                                tableBuffer.append("");
                            }
                            tableBuffer.append("</td>");
                        }
                        tableBuffer.append("</Tr>");
                    }
                }

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
            }
        } else {
        }

        return tableBuffer.toString();
    }

    public String getGraphTableHidden() {
        return graphTableHidden;
    }

    public void setGraphTableHidden(String graphTableHidden) {
        this.graphTableHidden = graphTableHidden;
    }

    public String showMetaData(String reportId) {

        PbReportCollection collect = new PbReportCollection();
        String tableBuild = "";
        // StringBuilder tableBuild=new StringBuilder();
        collect.reportId = reportId;
        ReportMetadataUIHelper metadataUIHelper = new ReportMetadataUIHelper();
        try {
            collect.getParamMetaData(true);
            tableBuild = metadataUIHelper.showMetaData(collect);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return tableBuild;
    }

    public String builcolumnpie(JqplotGraphProperty graphproperty, ProgenJqplotGraphBD jqplotgraphbd, ArrayList alist, String[] rowValues, HttpServletRequest request, Container container, ProGenJqPlotChartTypes jqplotcontainer) {
        HttpSession session = request.getSession(false);
        int rowCount = 0;
        int row;
        int height1 = 440;
        String[] viewByColumns = null;
        HashMap GraphHashMap = null;
        HashMap map = new HashMap();
        String ReportId = "";
        ProgenDataSet retobj = null;
        ReportId = request.getParameter("REPORTID");
        retobj = container.getRetObj();
        String graphid = request.getParameter("gid");
        String graphtype = request.getParameter("grptypid");
        String graphids = request.getParameter("grpIds");
        String selectedgraph = request.getParameter("selectedgraph");
        String grpidfrmrep = request.getParameter("grpidfrmrep");
        if (ReportId == null || ReportId.isEmpty()) {
            ReportId = request.getAttribute("REPORTID").toString();
        }
        if (graphid == null || graphid.equalsIgnoreCase("") || graphtype.isEmpty()) {
            graphid = graphproperty.getGraphId();
        }
        if (graphtype == null || graphtype.equalsIgnoreCase("") || graphtype.isEmpty()) {
            graphtype = graphproperty.getGraphTypename();
        }
        if (selectedgraph == null || selectedgraph.equalsIgnoreCase("") || graphtype.isEmpty()) {
            selectedgraph = graphproperty.getSlectedGraphType(graphid);
        }
        if (grpidfrmrep == null || grpidfrmrep.equalsIgnoreCase("") || graphtype.isEmpty()) {
            grpidfrmrep = request.getParameter("graphId");
        }
        if (grpidfrmrep == null || grpidfrmrep.equalsIgnoreCase("null")) {
            if (request.getAttribute("graphId") != null) {
                grpidfrmrep = request.getAttribute("graphId").toString();
            }
        }
        if (request.getAttribute("height") != null) {
            height1 = Integer.parseInt(request.getAttribute("height").toString());
        }
        if (graphproperty != null && (rowValues == null || rowValues.length == 0)) {
            rowValues = graphproperty.getRowValues();
            jqplotgraphbd.rowValues = rowValues;
        }
        GraphHashMap = container.getGraphHashMap();
        HashMap singleGraphDetails = (HashMap) GraphHashMap.get(grpidfrmrep);
        viewByColumns = (String[]) singleGraphDetails.get("viewByElementIds");

        StringBuilder graph = new StringBuilder();
        if (rowValues != null && rowValues.length > 0) {
            rowCount = rowValues.length;
        } else if (alist != null && alist.size() > 0) {
            rowCount = alist.size();
        }

        if (rowCount == 0) {
            rowCount = 1;
        }
        if (rowCount > 5) {
            row = 2;
        } else {
            row = 1;
        }
        int temp = 0;
        int temp1 = 0;
        if (rowValues == null) {
            graph.append("<div id='chart' style='width:100%; height:" + 440 + "px;'>");
            jqplotgraphbd.chartId = "chart";
            jqplotgraphbd.rowindex = 0;
            graph.append("<script>");
            graph.append(jqplotgraphbd.prepareJqplotGraph(ReportId, graphid, graphtype, jqplotcontainer, request, graphids, selectedgraph, grpidfrmrep));
            graph.append("</script>");
            graph.append("</div>");
        } else {
            for (int j = 1; j <= row; j++) {
                graph.append("<table width='100%'>");
                graph.append("<tr>");
                for (int k = temp; k < retobj.getViewSequence().size(); k++) {
                    int actualRow = retobj.getViewSequence().get(k);
                    temp++;
                    if (jqplotgraphbd.checkViewByExists(jqplotgraphbd.getViewBy(retobj, viewByColumns, actualRow))) {
                        int height = height1 / row;
                        jqplotgraphbd.chartId = "chart" + k + j;
                        jqplotgraphbd.rowindex = k;
                        graph.append("<td>");
                        graph.append("<div id='chart" + k + "" + j + "' style='width:100%; height:" + height + "px;'>");
                        graph.append("<script>");
                        graph.append(jqplotgraphbd.prepareJqplotGraph(ReportId, graphid, graphtype, jqplotcontainer, request, graphids, selectedgraph, grpidfrmrep));
                        graph.append("</script>");
                        graph.append("</div>");
                        graph.append("</td>");
                        temp1++;
                        if (temp1 % 5 == 0) {

                            break;
                        }

                    }
                }
                graph.append("</tr>");
                graph.append("</table>");
            }
        }
        return graph.toString();
    }

    public String builcrosstabpie(JqplotGraphProperty graphproperty, ProgenJqplotGraphBD jqplotgraphbd, ArrayList alist, String[] rowValues, HttpServletRequest request, Container container, ProGenJqPlotChartTypes jqplotcontainer, OneViewLetDetails oneviewlet) {
        int actualrowsize = 10;
        StringBuilder graph = new StringBuilder();
        HttpSession session = request.getSession(false);
        int rowCount = 0;
        int row;
        int height1 = 440;
        String[] viewByColumns = null;
        HashMap GraphHashMap = null;
        HashMap map = new HashMap();
        String ReportId = "";
        ProgenDataSet retobj = null;
        ReportId = request.getParameter("REPORTID");
        retobj = container.getRetObj();
        String graphid = request.getParameter("gid");
        String graphtype = request.getParameter("grptypid");
        String graphids = request.getParameter("grpIds");
        String selectedgraph = request.getParameter("selectedgraph");
        String grpidfrmrep = request.getParameter("grpidfrmrep");
        boolean istranspose = false;
        if (ReportId == null || ReportId.isEmpty()) {
            ReportId = request.getAttribute("REPORTID").toString();
        }
        if (graphid == null || graphid.equalsIgnoreCase("") || graphtype.isEmpty()) {
            graphid = graphproperty.getGraphId();
        }
        if (graphtype == null || graphtype.equalsIgnoreCase("") || graphtype.isEmpty()) {
            graphtype = graphproperty.getGraphTypename();
        }
        if (selectedgraph == null || selectedgraph.equalsIgnoreCase("") || graphtype.isEmpty()) {
            selectedgraph = graphproperty.getSlectedGraphType(graphid);
        }
        if (grpidfrmrep == null || grpidfrmrep.equalsIgnoreCase("") || graphtype.isEmpty()) {
            grpidfrmrep = request.getParameter("graphId");
        }
        if (grpidfrmrep == null || grpidfrmrep.equalsIgnoreCase("null")) {
            if (request.getAttribute("graphId") != null) {
                grpidfrmrep = request.getAttribute("graphId").toString();
            }
        }
        if (request.getAttribute("height") != null) {
            height1 = Integer.parseInt(request.getAttribute("height").toString());
        }
        GraphHashMap = container.getGraphHashMap();
        HashMap singleGraphDetails = (HashMap) GraphHashMap.get(grpidfrmrep);
        viewByColumns = (String[]) singleGraphDetails.get("viewByElementIds");
        String[] PrevbarChartColumnNames = (String[]) singleGraphDetails.get("barChartColumnNames");
        if (singleGraphDetails.get("istranspose") != null && !singleGraphDetails.get("istranspose").toString().equalsIgnoreCase("null") && singleGraphDetails.get("istranspose").toString().equalsIgnoreCase("true")) {
            istranspose = true;
        } else if (graphproperty != null) {
            istranspose = graphproperty.isTranspose();
        }
        if (istranspose) {
            if (singleGraphDetails.get("GraphdisplayCols") != null && !singleGraphDetails.get("GraphdisplayCols").toString().equalsIgnoreCase("null") && singleGraphDetails.get("GraphdisplayCols").toString() != "") {
                rowCount = Integer.parseInt(singleGraphDetails.get("GraphdisplayCols").toString());
            } else {
                rowCount = PrevbarChartColumnNames.length;
            }
            actualrowsize = PrevbarChartColumnNames.length - viewByColumns.length;
        } else {
            actualrowsize = retobj.getViewSequence().size();
            if (retobj.getViewSequence().size() < 10) {
                rowCount = retobj.getViewSequence().size();
            } else {
                rowCount = 10;
            }
            if (singleGraphDetails.get("graphDisplayRows") != null && !singleGraphDetails.get("graphDisplayRows").toString().equalsIgnoreCase("null") && singleGraphDetails.get("graphDisplayRows").toString() != "") {
                rowCount = Integer.parseInt(singleGraphDetails.get("graphDisplayRows").toString());
                if (rowCount < retobj.getViewSequence().size()) {
                    rowCount = retobj.getViewSequence().size();
                }

            }
        }


        if (rowCount / 5 == 0) {
            row = 1;
        } else {
            row = rowCount / 5;
        }
        int temp = 0;
        int temp1 = 0;
        for (int j = 1; j <= row; j++) {
            graph.append("<table width='100%'>");
            graph.append("<tr>");
            for (int k = temp; k < actualrowsize && k < rowCount; k++) {
                temp++;
                int height = height1 / row;
                if (oneviewlet != null) {
                    jqplotgraphbd.chartId = "Dashlets-" + oneviewlet.getNoOfViewLets() + "chart" + k + j;
                } else {
                    jqplotgraphbd.chartId = "chart" + k + j;
                }
                jqplotgraphbd.rowindex = k;
                graph.append("<td>");
                if (oneviewlet != null) {
                    graph.append("<div id='Dashlets-" + oneviewlet.getNoOfViewLets() + "chart" + k + "" + j + "' style='width:100%; height:" + height + "px;'>");
                } else {
                    graph.append("<div id='chart" + k + "" + j + "' style='width:100%; height:" + height + "px;'>");
                }
                graph.append("<script>");
                graph.append(jqplotgraphbd.prepareJqplotGraph(ReportId, graphid, graphtype, jqplotcontainer, request, graphids, selectedgraph, grpidfrmrep));
                graph.append("</script>");
                graph.append("</div>");
                graph.append("</td>");
                temp1++;
                if (temp1 % 5 == 0) {

                    break;
                }


            }
            graph.append("</tr>");
            graph.append("</table>");
        }
        jqplotgraphbd.chartId = "chartlegend";
        jqplotgraphbd.legendDiv = true;
        graph.append("<div id='chartlegend' style='width:100%; height:10px;'>");
        graph.append("<script>");
        graph.append(jqplotgraphbd.prepareJqplotGraph(ReportId, graphid, graphtype, jqplotcontainer, request, graphids, selectedgraph, grpidfrmrep));
        graph.append("</script>");
        graph.append("</div>");

        return graph.toString();
    }

    public String TopBottom(String REPORTID, PbReturnObject pbretobj, Container container, boolean isTop, int endCount) {
//          String REPORTID = request.getParameter("REPORTID");
//                String perBy = request.getParameter("perBy");
//                String measureBy = request.getParameter("msrBy");
        String perBy = container.getTopBottomTableHashMap().get("TopBottomVal");
        String measureBy = container.getTopBottomTableHashMap().get("TopBottomMsr");
        PbReturnObject sortPbretObj = null;
        ArrayList<String> sortColumns = new ArrayList<String>();
        String name = "";

        ArrayList displayCols = container.getDisplayColumns();
        ArrayList displayLabels = container.getDisplayLabels();
        HashMap ColumnsVisibility = container.getColumnsVisibility();
        MultiPeriodKPI pbretObj1 = container.getMultiPeriodKPI();
        int start = container.getViewByCount();
        ArrayList<Integer> topBttmList = new ArrayList<Integer>();
        char[] sortTypes = new char[1];
        char[] sortDataTypes = new char[1];
        int noOfRows = 0;
        container.setTopBottomDispaly(perBy);
        ArrayList<BigDecimal> signList = new ArrayList<BigDecimal>();
        if (pbretobj.getRowCount() < endCount) {
            noOfRows = pbretobj.getRowCount();
        } else {
            noOfRows = endCount;
        }

        if (measureBy != null && (!"".equalsIgnoreCase(measureBy))) {
            if (perBy != null && (!"".equalsIgnoreCase(perBy))) {
                if (perBy.equalsIgnoreCase("top5") || perBy.equalsIgnoreCase("top10") || perBy.equalsIgnoreCase("top25")) {
                    //sortPbretObj = pbretObj.sort(1, measureBy, "N");
                    sortColumns.add(measureBy);
                    sortTypes[0] = '1';
                    sortDataTypes[0] = 'N';
                    topBttmList = pbretobj.findTopBottom(sortColumns, sortTypes, noOfRows);
                    //                          pbretObj.setViewSequence(topBttmList);
                } else if (perBy.equalsIgnoreCase("bottom5") || perBy.equalsIgnoreCase("bottom10") || perBy.equalsIgnoreCase("bottom25")) {
                    //sortPbretObj = pbretObj.sort(0, measureBy, "N");
                    sortColumns.add(measureBy);
                    sortTypes[0] = '0';
                    sortDataTypes[0] = 'N';
                    topBttmList = pbretobj.findTopBottom(sortColumns, sortTypes, noOfRows);

                }
            }
        } else {
            if (isTop == true) {
                sortColumns.add((String) displayCols.get(start));
                sortTypes[0] = '1';
                sortDataTypes[0] = 'N';
                topBttmList = pbretobj.findTopBottom(sortColumns, sortTypes, noOfRows);
            } else {
                sortColumns.add((String) displayCols.get(start));
                sortTypes[0] = '0';
                sortDataTypes[0] = 'N';
                topBttmList = pbretobj.findTopBottom(sortColumns, sortTypes, noOfRows);
            }
        }

        if (topBttmList.size() < endCount) {
            endCount = topBttmList.size();
        }
        BigDecimal TotalValue = new BigDecimal("0");
        StringBuffer disptopBottomChart = new StringBuffer();
        BigDecimal[] valArray = new BigDecimal[endCount];
        BigDecimal MaxValue = new BigDecimal("0");
        String dimension = container.getDisplayColumns().get(0);
        for (int i = 0; i < endCount; i++) {


            if (measureBy != null && (!"".equalsIgnoreCase(measureBy))) {
                valArray[i] = pbretobj.getFieldValueBigDecimal(topBttmList.get(i), measureBy);
                signList.add(valArray[i]);
                TotalValue = pbretobj.getColumnGrandTotalValue(measureBy);
            } else {

                valArray[i] = pbretobj.getFieldValueBigDecimal(topBttmList.get(i), String.valueOf(displayCols.get(start)));
                signList.add(valArray[i]);
                TotalValue = pbretobj.getColumnGrandTotalValue(String.valueOf(displayCols.get(start)));
            }
            MaxValue = MaxValue.max(valArray[i]);
            if (valArray[i].longValue() < 0) {
                MaxValue = MaxValue.max(valArray[i].negate());
            } else if (MaxValue.intValue() == 0) {
                MaxValue = new BigDecimal("1");
            }
        }
        ArrayList<Boolean> checkList = new ArrayList<Boolean>();

        for (int j = 0; j < signList.size(); j++) {
            if (signList.get(j).intValue() < 0) {
                checkList.add(false);
            } else {
                checkList.add(true);
            }
        }

        BigDecimal[] percentageVal = new BigDecimal[valArray.length];
        NumberFormat nformat = NumberFormat.getInstance(Locale.US);
        nformat.setMaximumFractionDigits(2);
        nformat.setMinimumFractionDigits(2);
        if (TotalValue == null) {
            TotalValue = new BigDecimal("0");
        }
        TotalValue = (TotalValue.doubleValue() == 0.0) ? new BigDecimal("1") : TotalValue;
        BigDecimal pixelSize = new BigDecimal("50");

        //  MaxValue = new BigDecimal(MaxValue.intValue());
        for (int i = 0; i < endCount; i++) {
            valArray[i] = valArray[i].divide(new BigDecimal("1"), MathContext.DECIMAL32);
            percentageVal[i] = valArray[i].multiply(new BigDecimal("100"));
            percentageVal[i] = percentageVal[i].divide(TotalValue, MathContext.DECIMAL32);
            disptopBottomChart.append("<tr>");
            if (measureBy != null && (!"".equalsIgnoreCase(measureBy))) {

                if (RTDimensionElement.getDimensionType(dimension) == RTDimensionElement.SEGMENT) {
                    name = pbretobj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(1))).toString().toUpperCase();
                } else {
                    name = pbretobj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(0)).toUpperCase());
                }
                if (name.length() > 10) {
                    name = name.substring(0, 9) + "..";
                }
                if (RTDimensionElement.getDimensionType(dimension) == RTDimensionElement.SEGMENT) {
                    disptopBottomChart.append("<td style='float:none;margin:0 8px 1px 4px;padding-bottom: 0.5em' title=\"" + pbretobj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(1)).toUpperCase()) + "\">" + name + "</td>");
                } else {
                    disptopBottomChart.append("<td style='float:none;margin:0 8px 1px 4px;padding-bottom: 0.5em' title=\"" + pbretobj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(0)).toUpperCase()) + "\">" + name + "</td>");
                }
            } else {
                if (RTDimensionElement.getDimensionType(dimension) == RTDimensionElement.SEGMENT) {
                    name = pbretobj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(1))).toString().toUpperCase();
                } else {
                    name = pbretobj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(0)).toUpperCase());
                }
                name = (name.length() > 10) ? name = name.substring(0, 9) + ".." : name;
                if (RTDimensionElement.getDimensionType(dimension) == RTDimensionElement.SEGMENT) {
                    disptopBottomChart.append("<td style='float:none;margin:0 8px 1px 4px;padding-bottom: 0.5em' title=\"" + pbretobj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(1)).toUpperCase()) + "\">" + name + "</td>");

                } else {
                    disptopBottomChart.append("<td style='float:none;margin:0 8px 1px 4px;padding-bottom: 0.5em' title=\"" + pbretobj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(0)).toUpperCase()) + "\">" + name + "</td>");
                }
            }
            int size = valArray[i].divide(MaxValue, MathContext.DECIMAL32).multiply(pixelSize).intValue();
            // ////.println("percentageVal[i]--" + percentageVal[i] + "---nformat.format(percentageVal[i])---" + nformat.format(percentageVal[i]));
            if (nformat.format(percentageVal[i]) != "0" && size == 0) {
                size = 1;
            }


            if (!checkList.contains(true)) {
                //size=(100+size)/100;
                size = 100 + size;
                //size= Math.abs(size);


            } else {
                if (checkList.get(i) == false) {
                    size = 0;
                }
            }
            if (isTop) {
                disptopBottomChart.append("<td style='float:left;clear:both;margin:0 8px 0 0;font-weight:normal;text-align:left;'><img style=\"width:" + size + "px;height:10px;\" src='images/greenbar.png'  title='" + valArray[i] + "'/>&nbsp;<font style='font-family:verdana;font-size:9px'>" + nformat.format(percentageVal[i]) + "%</font></td>");
            } else {
                disptopBottomChart.append("<td style='float:left;clear:both;margin:0 8px 0 0;font-weight:normal;text-align:left;'><img style=\"width:" + size + "px;height:10px;\" src='images/barchart.gif'  title='" + valArray[i] + "'/>&nbsp;<font style='font-family:verdana;font-size:9px'>" + nformat.format(percentageVal[i]) + "%</font></td>");
            }
            disptopBottomChart.append("</tr>");
        }
        return disptopBottomChart.toString();
    }

    public void writeBackUpFile(Container container, String filepath) throws FileNotFoundException, IOException {
        File file = new File(filepath);
        FileOutputStream fos1 = new FileOutputStream(file);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(container);
        oos1.flush();
        oos1.close();
    }

    public void getContainerFromDb(String reportId, String userId, HttpSession session) {
        String qry = "SELECT FILEPATH FROM PRG_AR_INSIGHT_WORKBENCH WHERE INSIGHT_ID=" + reportId;
        PbDb db = new PbDb();
        PbReturnObject retObj = null;
        Container container = null;
        try {
            retObj = db.execSelectSQL(qry);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        String filePath = "";
        if (retObj.getRowCount() >= 0) {
            filePath = retObj.getFieldValueString(0, 0);
        }
        PbReportViewerBD viewerbd = new PbReportViewerBD();
        try {
            container = (Container) viewerbd.readFileDetails(filePath);

            container.setSessionContext(session, container);
        } catch (FileNotFoundException ex) {
            logger.error("Exception:", ex);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } catch (ClassNotFoundException ex) {
            logger.error("Exception:", ex);
        }

    }

    public StringBuilder generateInsightHtmlData(Container container, String reportId, String userId, String action) {

        ArrayList<String> displayCols = container.getDisplayColumns();
        StringBuilder header = new StringBuilder();
        header.append("<div class=\"scrollable\" id=\"progenTableDiv\">");
        header.append("<Table ID=\"progenTable\" class=\"prgtable\" CELLPADDING=\"0\" CELLSPACING=\"1\" style=\"border-collapse:collapse\" width=\"100%\">");
        header.append("<thead id=\"theaddiv\">");
        header.append("<tr width=\"100%\"  height=\"20px\">");
        header.append("<th class=\"tableHeaderStyle\"/>");
        header.append("<th class=\"tableHeaderStyle\" align=\"center\" ><font color=\"#000000\">Parameter</font></th>");
        int colCount = container.getDisplayColumns().size();
        for (int i = 1; i < colCount; i++) {
            header.append("<th class=\"tableHeaderStyle\" align=\"center\">").append("<font color=\"#000000\">").append(container.getDisplayLabels().get(i)).append("</font>").append("</th>");
        }
        header.append("</tr>");
        header.append("</thead>");
        header.append("<tbody>");
//        for(int j=0; j<colCount; j++)
//         {
        String id = container.getDisplayColumns().get(0).replace("A_", "");
        String divId = id + "Div";
        String childDivId = id + "ChildDiv";
        header.append("<tr id='").append(divId).append("'>");
        header.append("<td class=\"collapsible\" >");
        header.append("<a class=\"collapsed\" onclick=\"loadChildData('" + divId + "','" + id + "','" + childDivId + "','" + container.getReportCollect().reportParametersValues.toString() + "');\"></a></td>");
        header.append("<td align=\"center\" >").append("<font color=\"#000000\">").append(container.getDisplayLabels().get(0)).append("</font>").append("</td>");
        PbReturnObject retObj;
        if (action != null && action.equalsIgnoreCase("insightOpen")) {
            retObj = container.getInsightRetObj();
        } else {
            retObj = genereateGrandTotal(container, userId);
        }

        if (retObj.getRowCount() > 0) {
            StringBuffer elementId = new StringBuffer();
            PbDb pbdb = new PbDb();
            HashMap<String, String> nfrmat = new HashMap<String, String>();
            HashMap<String, String> round = new HashMap<String, String>();
            NumberFormatter nf = new NumberFormatter();
            for (int i = 0; i < container.getReportCollect().reportQryElementIds.size(); i++) {
                elementId.append(",'").append(container.getReportCollect().reportQryElementIds.get(i)).append("'");
            }
            String qry = "SELECT ELEMENT_ID,NO_FORMAT,ROUND FROM PRG_USER_SUB_FOLDER_ELEMENTS WHERE ELEMENT_ID in (" + elementId.substring(1) + ")";
            try {
                PbReturnObject retObj1 = pbdb.execSelectSQL(qry);
                if (retObj1.getRowCount() > 0) {
                    for (int i = 0; i < retObj1.getRowCount(); i++) {
                        nfrmat.put(retObj1.getFieldValueString(i, 0), retObj1.getFieldValueString(i, 1));
                        round.put(retObj1.getFieldValueString(i, 0), retObj1.getFieldValueString(i, 2));
                    }
                }
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            // for(int i=1;i<retObj.getColumnCount();i++){
//                    header.append("<td align=\"center\">").append("<font color=\"#000000\">").append(retObj.getModifiedNumber(new BigDecimal(retObj.getFieldValueString(0, i)))).append("</font>").append("</td>");
//                    }
            for (int i = container.getViewByCount(); i < colCount; i++) {
//                     
//                     
                if (retObj.getFieldValueString(0, displayCols.get(i)) != "" && retObj.getFieldValueString(0, displayCols.get(i)) != null && !retObj.getFieldValueString(0, displayCols.get(i)).equalsIgnoreCase("null")) {
                    if (nfrmat.get(displayCols.get(i).replace("A_", "")) != null && nfrmat.get(displayCols.get(i).replace("A_", "")) != "" && !nfrmat.get(displayCols.get(i).replace("A_", "")).equalsIgnoreCase("") && round.get(displayCols.get(i).replace("A_", "")) != null && round.get(displayCols.get(i).replace("A_", "")) != "" && !round.get(displayCols.get(i).replace("A_", "")).equalsIgnoreCase("")) {
                        BigDecimal currVal = new BigDecimal(retObj.getFieldValueString(0, displayCols.get(i)));
                        String frmtVal = nf.getModifiedNumber(currVal, String.valueOf(nfrmat.get(displayCols.get(i).replace("A_", ""))), Integer.parseInt(String.valueOf(round.get(displayCols.get(i).replace("A_", "")))));
                        //
                        header.append("<td align=\"center\">").append("<font color=\"#000000\">").append(frmtVal).append("</font>").append("</td>");
                    } else if (nfrmat.get(displayCols.get(i).replace("A_", "")) != null && nfrmat.get(displayCols.get(i).replace("A_", "")).toString() != "" && !nfrmat.get(displayCols.get(i).replace("A_", "")).equalsIgnoreCase("")) {
                        BigDecimal currVal = new BigDecimal(retObj.getFieldValueString(0, displayCols.get(i)));
                        String frmtVal = nf.getModifiedNumber(currVal, String.valueOf(nfrmat.get(displayCols.get(i).replace("A_", ""))));
                        //
                        header.append("<td align=\"center\">").append("<font color=\"#000000\">").append(frmtVal).append("</font>").append("</td>");
                    } else {
                        header.append("<td align=\"center\">").append("<font color=\"#000000\">").append(retObj.getModifiedNumber(new BigDecimal(retObj.getFieldValueString(0, displayCols.get(i))))).append("</font>").append("</td>");
                    }
                } else {
                    header.append("<td align=\"center\">").append("<font color=\"#000000\">").append("").append("</font>").append("</td>");
                }
            }
        } else {
            for (int i = 1; i < colCount; i++) {
                header.append("<td>");
                header.append("</td>");
            }
        }
        header.append("</tr>");
        header.append("<tr class=\"expand-child\"><td style=\"display: none;\" colspan='").append(2 + colCount).append("'>");
        header.append("<div style=\"display: ;\" id='").append(childDivId).append("'>");
        header.append("</div>");
        header.append("<div id='").append(childDivId).append("prgBar").append("'></div>");
//         }
        header.append("</tbody>");
        return header;
    }

    public PbReturnObject genereateGrandTotal(Container container, String userId) {
        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject retObj = null;
        QueryExecutor qryExec = new QueryExecutor();
        PbReportCollection collect = container.getReportCollect();
        // 
        repQuery.setRowViewbyCols(collect.reportRowViewbyValues);
        repQuery.setColViewbyCols(collect.reportColViewbyValues);
        repQuery.setParamValue(container.getReportCollect().reportParametersValues);
        repQuery.setQryColumns(collect.reportQryElementIds);
        repQuery.setColAggration(collect.reportQryAggregations);

        repQuery.setTimeDetails(collect.timeDetailsArray);
        repQuery.isKpi = true;
        repQuery.setReportId(collect.reportId);
        repQuery.setBizRoles(collect.reportBizRoles[0]);
        repQuery.setUserId(userId);
        try {
            String query = repQuery.generateViewByQry();
            //
            retObj = (PbReturnObject) qryExec.executeQuery(collect, query, false);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        container.setInsightRetObj(retObj);
        return retObj;
    }

    public DataFacade generateReportQrys(String ReportId, DataFacade facade, String userId) throws Exception {
        Container container = facade.container;
        ArrayList fileParameterIds = (ArrayList) container.getParametersHashMap().get("Parameters");
        PbReportViewerBD viewerBd = new PbReportViewerBD();
        PbReportCollection collect = container.getReportCollect();
        ArrayList<String> reportRowViewbyValues = new ArrayList<String>();
        reportRowViewbyValues = collect.reportRowViewbyValues;
//        facade.setRefreshconatinerMap((String)reportRowViewby((StringVal)reportRowViewbyues.get(0), container);
        facade.refreshconatinerMap.put(((String) reportRowViewbyValues.get(0)).trim(), container);
        for (int i = 0; i < fileParameterIds.size(); i++) {
            String paramId = ((String) fileParameterIds.get(i)).trim();
            if (!paramId.equalsIgnoreCase(((String) reportRowViewbyValues.get(0)).trim())) {
                facade.setRefreshconatinerMap((String) fileParameterIds.get(i), container);
            }
        }
        if (facade.isMsrbasedBusTemplate) {
            facade.setRefreshconatinerMap("TIME", container);
        }
//         for(int i=0;i<fileParameterIds.size();i++)
//             
        return facade;
    }

    public String generateTemplateGraphHeader(String grpId, String reportId, OneViewLetDetails detail, String viewbyId) {
        StringBuilder htmlVar = new StringBuilder();
        String[] jqgrapharray = {"Area", "Area-Line", "Bar-Horizontal", "Bar-Vertical", "Block", "Bubble", "Bubble(log)", "Dot-Graph", "DualAxis(Bar-Line)", "DualAxis(Area-Line)", "Donut-Single", "Donut-Double", "Funnel", "Funnel(INV)", "Line", "Line(Dashed)", "Line(Simple)", "Line(Simple-R)", "Line(Smooth)", "Line(Std)", "Mekko", "Overlaid(Bar-Line)", "Overlaid(Bar-Dot)", "Pie", "Pie-Empty", "Scatter", "Scatter(Partial)", "Scatter(Regression)", "StackedArea", "StackedBar(V)", "StackedBar(H)", "StackedBar(Percent)", "StackedH(Percent)", "Waterfall", "Waterfall(GT)"};
        String[] jqgraphIds = {"5500", "5501", "5502", "5503", "5504", "5505", "5506", "5508", "5509", "5510", "5511", "5512", "5513", "5514", "5515", "5516", "5517", "5518", "5519", "5520", "5521", "5522", "5523", "5524", "5525", "5526", "5527", "5528", "5529", "5530", "5531", "5532", "5533", "5534", "5535"};
        htmlVar.append("<table style='margin-left: 10px;width:100%;'><tr>");
        if (detail.getReptype().toString().equalsIgnoreCase("MsrTemplateGraph")) {
            htmlVar.append("<td style='font-size:10pt;width:80%;'>").append(detail.getParamName()).append("</td>");
        } else {
            htmlVar.append("<td style='width:80%;'></td>");
        }
        htmlVar.append("<td style='font-size:12pt;width:10%;'>").append("<a class='ui-icon ui-icon-image' style='text-decoration:none' onclick=\"changeGraphTypes('" + detail.getNoOfViewLets() + "');\" title='graphTypes' href='javascript:void(0)'></a>");
        htmlVar.append("<div id=\'grphTypes" + detail.getNoOfViewLets() + "\' class='overlapDiv' style='width:120px;height:140px;display:none;overflow:auto;position: absolute; text-align: left; border: 1px solid rgb(0, 0, 0);'><table>");
        for (int gqtype = 0; gqtype < jqgrapharray.length; gqtype++) {
            htmlVar.append("<tr><td><a href='javascript:void(0)' onclick=\"buildJqGraph('" + jqgrapharray[gqtype] + "','" + grpId + "','" + jqgraphIds[gqtype] + "','" + reportId + "','" + viewbyId + "','" + detail.getNoOfViewLets() + "')\">" + jqgrapharray[gqtype] + "</a></td></tr>");
        }
        htmlVar.append("</table></div></td></tr></table>");

        return htmlVar.toString();

    }

    public void generateExcelreturnObject(String reportId, Container container, File tempFile) {
        HSSFWorkbook wb = null;
        HSSFSheet sheet = null;
        PbReturnObject excelreturnObj = new PbReturnObject();
        int size = 0;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(tempFile);
            wb = new HSSFWorkbook(fis);
            sheet = wb.getSheetAt(0);
            int colCount = sheet.getRow(0).getLastCellNum();

            excelreturnObj.colCount = colCount;
            excelreturnObj.cols = new String[colCount];
            excelreturnObj.columnTypes = new String[colCount];
            excelreturnObj.columnTypesInt = new Integer[colCount];
            excelreturnObj.columnSizes = new int[colCount];
            excelreturnObj.grandTotals = new BigDecimal[colCount];

            for (int i = 0; i < colCount; i++) {
                excelreturnObj.hMap.put(sheet.getRow(0).getCell(i).getStringCellValue(), new ArrayList());
                excelreturnObj.cols[i] = sheet.getRow(0).getCell(i).getStringCellValue();
                Cell typeCell = sheet.getRow(1).getCell(i);
                if (typeCell.getCellType() == Cell.CELL_TYPE_STRING || typeCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                    excelreturnObj.columnTypes[i] = "VARCHAR2";
                    excelreturnObj.columnTypesInt[i] = 12;
                    excelreturnObj.columnSizes[i] = sheet.getDefaultColumnWidth();
                } else if (typeCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    excelreturnObj.columnTypes[i] = "NUMBER";
                    excelreturnObj.columnTypesInt[i] = 2;
                    excelreturnObj.columnSizes[i] = sheet.getDefaultColumnWidth();
                } else {
                    excelreturnObj.columnTypes[i] = "VARCHAR2";
                    excelreturnObj.columnTypesInt[i] = 12;
                }
                excelreturnObj.grandTotals[i] = BigDecimal.ZERO;
            }

//            sheetgetColumnWidth
            Iterator<Row> rowIterator = sheet.iterator();
            int excelRowCnt = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                int i = 0;

                while (excelRowCnt != 0 && cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_BOOLEAN:
                            ((ArrayList) (excelreturnObj.hMap.get(excelreturnObj.cols[i]))).add(cell.getBooleanCellValue());
//                            System.out.print(cell.getBooleanCellValue() + "\t\t");
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            ((ArrayList) (excelreturnObj.hMap.get(excelreturnObj.cols[i]))).add(cell.getNumericCellValue());
//                            System.out.print(cell.getNumericCellValue() + "\t\t");
                            break;
                        case Cell.CELL_TYPE_STRING:
                            ((ArrayList) (excelreturnObj.hMap.get(excelreturnObj.cols[i]))).add(cell.getStringCellValue());
//                            System.out.print(cell.getStringCellValue() + "\t\t");
                            break;
                    }
                    i++;
                }

                if (excelRowCnt != 0) {
                    excelreturnObj.rowCount++;
                }
                excelRowCnt++;
//                

            }
            if (container.importExcelDetails != null) {
                container.importExcelDetails.getReturnObject().add(excelreturnObj);
//           for(int i=1;i<colCount;i++)
//             container.importExcelDetails.getExcelMeasures().add(excelreturnObj.cols[i]);
            } else {
                container.importExcelDetails = new ImportExcelDetail();
                container.importExcelDetails.getReturnObject().add(excelreturnObj);
//          for(int i=1;i<colCount;i++)
//             container.importExcelDetails.getExcelMeasures().add(excelreturnObj.cols[i]);
            }
            container.getReportCollect().setIsExcelimportEnable(true);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

    }

    public HashMap ReadExcelDetails(String reportId, File tempFile) {
        HSSFWorkbook wb = null;
        HSSFSheet sheet = null;
        FileInputStream fis = null;
        HashMap<String, ArrayList> excelMap = new HashMap<String, ArrayList>();
        ArrayList excelNamelist = new ArrayList();
        ArrayList excelColTypelist = new ArrayList();
        try {
            fis = new FileInputStream(tempFile);
            wb = new HSSFWorkbook(fis);
            sheet = wb.getSheetAt(0);
            int colCount = sheet.getRow(0).getLastCellNum();
            for (int i = 0; i < colCount; i++) {
                excelNamelist.add(sheet.getRow(0).getCell(i).getStringCellValue());
                Cell typeCell = sheet.getRow(1).getCell(i);
                excelColTypelist.add(typeCell.getCellType());
            }
            excelMap.put("excelNames", excelNamelist);
            excelMap.put("excelcolType", excelColTypelist);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return excelMap;
    }

    public boolean buildAO(String action, Container container, String userId, HashMap reqParam) {
        PbAOViewerBD viewerBd = new PbAOViewerBD();
//        StringBuilder tableBuffer = new StringBuilder();
        boolean prepareAO =false;
        if (!"none".equalsIgnoreCase(action)) {
            if (action.equalsIgnoreCase("paramChange")) {
               prepareAO = viewerBd.prepareAO(action, container, container.getReportCollect().reportId, userId, reqParam);
            }
           prepareAO= viewerBd.prepareAO(action, container, container.getReportCollect().reportId, userId, new HashMap());

           //comment by Mohit
//            ArrayList dispLabels = container.getDisplayLabels();
//            ArrayList<String> dispCols = container.getDisplayColumns();
//            ArrayList dispTypes = container.getDataTypes();//container.getDisplayTypes();
//            ProgenDataSet retObj = container.getRetObj();
//
//            tableBuffer.append("<div>");
//            tableBuffer.append("<table cellspacing=\"1\" class=\"tablesorter\" id=\"tablesorter\" width=\"100px\">");
//            tableBuffer.append("<thead>");
//            tableBuffer.append("<tr valign=\"top\">");
//
//            //for (int i=0;i<dispLabels.size();i++)
//            // tableBuffer.append("<th>"+dispLabels.get(i)+"</th>");
//
//            tableBuffer.append("</tr>");
//            tableBuffer.append("</thead>");
//            tableBuffer.append("<tfoot>");
//            tableBuffer.append("</tfoot>");
//            tableBuffer.append("<tbody>");
//            if (retObj != null) {
//                for (int i = 0; i < retObj.getRowCount(); i++) {
//                    tableBuffer.append("<tr>");
//                    for (int j = 0; j < dispCols.size(); j++) {
//                        String colType = (String) dispTypes.get(j);
//                        if ("C".equalsIgnoreCase(colType)) {
//                            tableBuffer.append("<td align=\"left\">").append(retObj.getFieldValueString(i, dispCols.get(j))).append("</td>");
//                        } else {
//                            BigDecimal bd = retObj.getFieldValueBigDecimal(i, dispCols.get(j));
//                            tableBuffer.append("<td align=\"right\">").append(NumberFormatter.getModifiedNumber(bd, "", -1)).append("</td>");
//                        }
//                    }
//                    tableBuffer.append("</tr>");
//                }
//            }
//            tableBuffer.append("</tbody>");
//            tableBuffer.append("</table>");
//
//            tableBuffer.append("<script type='text/javascript'>");
//            tableBuffer.append("$(document).ready(function() {");
//            tableBuffer.append(" $('#tablesorter').columnFilters();");
//            tableBuffer.append(" $('#tablesorter')");
//            tableBuffer.append(".tablesorter({widthFixed: true, widgets: ['zebra']})");
//            tableBuffer.append(" .tablesorterPager({container: $('#pager')});");
//            tableBuffer.append("});");
//
//            tableBuffer.append(" </script>");
//            tableBuffer.append("</div>");
                        }
        return prepareAO;
                    }
                }
