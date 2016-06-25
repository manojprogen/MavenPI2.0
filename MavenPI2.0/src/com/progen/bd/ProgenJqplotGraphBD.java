/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.bd;

import com.progen.charts.GraphProperty;
import com.progen.charts.JqplotGraphProperty;
import com.progen.db.ProgenDataSet;
import com.progen.jqplot.ProGenJqPlotChartTypes;
import com.progen.query.RTMeasureElement;
import com.progen.report.PbReportCollection;
import com.progen.report.ReportParameter;
import com.progen.report.display.util.NumberFormatter;
import java.math.BigDecimal;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import prg.db.Container;

/**
 *
 * @author ramesh janakuttu
 */
public class ProgenJqplotGraphBD {

    StringBuffer jqpotbuffer = new StringBuffer();
    public String chartId; //it should be the target div id of jqplot
    private boolean isFromOneView;
    private boolean isFromOneViewschedule;
    public JqplotGraphProperty JqplotGraphProperty;
    public int rowindex;
    public String[] rowValues;
    public String[] tablecols;
    public boolean legendDiv;
    public String tickId;
    public boolean ishomeTab;
    private Container container;

    public String prepareJqplotGraph(String reportid, String graphid, String graphType, ProGenJqPlotChartTypes graphTypes, HttpServletRequest request, String grpahids, String selectgraph, String grpidfrmrep) {
        //HashMap ParametersHashMap = container.getParametersHashMap();
//Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
        HttpSession session = null;
        if (!isFromOneViewschedule) {
            session = request.getSession(false);
        }
        //End of code by sandeep on 17/10/14 for schedule// update local files in oneview
        String sort = "sort";
        PbReportCollection collect = new PbReportCollection();
        boolean refreshGraph = false;
        HashMap map = new HashMap();
        Container container = null;
        ArrayList graphlist = new ArrayList();
        ArrayList columnnames = new ArrayList();
        ArrayList orginalcolumns = new ArrayList();
        ArrayList tableColumns = new ArrayList();
        ArrayList tableCollabel = new ArrayList();
        ArrayList displaycollist = new ArrayList();
        ArrayList bubblelist = new ArrayList();
        ArrayList scatterlist = new ArrayList();
        ArrayList columnpieDataSet = new ArrayList();
        ArrayList graphcolumns1 = new ArrayList();
        ArrayList Seriescolors = new ArrayList();
        ArrayList measurelist = new ArrayList();
        ArrayList dualAxisLines = new ArrayList();
        ArrayList orignallist = new ArrayList();
        ArrayList tooltip = new ArrayList();
        ArrayList viewBy = new ArrayList();
        ArrayList overlaid = new ArrayList();
        ArrayList lists = new ArrayList();
        ArrayList lists1 = new ArrayList();
        ArrayList lists2 = new ArrayList();
        ArrayList legends = new ArrayList();
        ArrayList list5 = new ArrayList();
        HashMap TableHashMap = null;
        String Data = null;
        Double value = 0.0;
        int count = 0;
        int minvalue = 0;
        int rowCount;
        int actualrowcount = 0;
        int graphrowcount = 0;
        int numberRows = 1;
        int barpadding = 1;
        int drillmapLength = 0;
        String temp = null;
        String displayCols = "";
        String graphDisplay = "";
        String rowviewby = "";
        String columnviewby = "";
        String targetType = "standard";
        String rgb = "transparent";
        String[] PrevbarChartColumnNames = null;
        String[] PrevbarChartColumnTitles = null;
        String[] viewByColumns = null;
        String[] colornew = null;
        String[] colors = null;
        String[] background = null;
        String[] graphColumns = null;
        String nextviewbyid = null;
        HashMap graphtype = new HashMap();
        HashMap GraphHashMap = null;
        HashMap singleGraphDetails = null;
        double currVal = 0.0;
        ProgenDataSet retobj = null;
        boolean istranpose = false;
        boolean islegendAppend = false;
        boolean isSupriseZero = false;
        GraphProperty jfProperty = new GraphProperty();
        if (JqplotGraphProperty != null && JqplotGraphProperty.getSeriescolors() != null) {
            colornew = JqplotGraphProperty.getSeriescolors();
        }
        if (JqplotGraphProperty != null) {
            graphTypes.showlocaldrill = JqplotGraphProperty.isGraphDrill();
            graphTypes.yaxiscalibration = JqplotGraphProperty.getYaxiscalibration();
            graphTypes.yaxisstart = JqplotGraphProperty.getYaxisstart();
            graphTypes.yaxisend = JqplotGraphProperty.getYaxisend();
            graphTypes.yaxisinterval = JqplotGraphProperty.getYaxisinterval();
            graphTypes.xaxisstart = JqplotGraphProperty.getXaxisstart();
            graphTypes.xaxisend = JqplotGraphProperty.getXaxisend();
            graphTypes.xaxisinterval = JqplotGraphProperty.getXaxisinterval();
            graphTypes.y2axisstart = JqplotGraphProperty.getY2axisstart();
            graphTypes.y2axisend = JqplotGraphProperty.getY2axisend();
            graphTypes.y2axisinterval = JqplotGraphProperty.getY2axisinterval();
            graphTypes.yAxisRounding = JqplotGraphProperty.getyAxisRounding();
            graphTypes.xAxisRounding = JqplotGraphProperty.getxAxisRounding();
            graphTypes.y2AxisRounding = JqplotGraphProperty.getY2AxisRounding();
            graphTypes.targetValue = JqplotGraphProperty.getTargetValue();
            graphTypes.tickdisplay = JqplotGraphProperty.gettickdisplay(); //kruthika
            graphTypes.isAdhocEnabled = JqplotGraphProperty.isAdhocEnabled();
            graphTypes.iscolorGrouping = JqplotGraphProperty.isColorGrouping();
            graphTypes.labelDir = JqplotGraphProperty.getLabelDirection();
            graphTypes.legendsPerRow = JqplotGraphProperty.getLegendsPerRow();
            graphTypes.isappendXaxis = JqplotGraphProperty.isAppendXaxis();
            graphDisplay = JqplotGraphProperty.getGraphdisptype();
            graphColumns = JqplotGraphProperty.getGraphColumns();
            graphTypes.graphtype1 = JqplotGraphProperty.getGraphtype1();
            graphTypes.graphtype2 = JqplotGraphProperty.getGraphtype2();
            graphTypes.targetcolor = JqplotGraphProperty.getTargetColor();
            graphTypes.drilltype = JqplotGraphProperty.getDrillType();
            graphTypes.datebyInterval = JqplotGraphProperty.getDatebyInterval();
            targetType = JqplotGraphProperty.getTargetType();
            islegendAppend = JqplotGraphProperty.isLegenaAppend();
            istranpose = JqplotGraphProperty.isTranspose();
            isSupriseZero = JqplotGraphProperty.isSupriseZero();
            graphTypes.istooltipXaxis = JqplotGraphProperty.istooltipXaxis();
            graphTypes.ischeckedLA = JqplotGraphProperty.getischeckedLA();
            graphTypes.ischeckedRA = JqplotGraphProperty.getischeckedRA();
            graphTypes.ischeckedXA = JqplotGraphProperty.getischeckedXA();
            graphTypes.ischeckedYA = JqplotGraphProperty.getischeckedYA();
            graphTypes.Flag = JqplotGraphProperty.getFlag();
            graphTypes.viewbyLabels = JqplotGraphProperty.getviewbyLabels();

            graphTypes.isOthersFlag = JqplotGraphProperty.getisOthersFlag();
            graphTypes.viewOthers = JqplotGraphProperty.getviewOthers();

        }
        String grpTitle = "";
        String[] series = graphTypes.seriescolors1;
        if (session != null || isFromOneViewschedule) {
//Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
            if (!isFromOneViewschedule) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportid);

            } else {
                container = this.getContainer();
                graphTypes.contextpath = container.getContextPath();
            }
            //End of code by sandeep on 17/10/14 for schedule// update local files in oneview
            collect = container.getReportCollect();
            TableHashMap = container.getTableHashMap();

            HashMap ReportHashMap = container.getReportHashMap();
            HashMap GraphHashMap2 = container.getChangeGraphColumns();
            HashMap GraphHashMap1 = container.getGraphHashMap();

            retobj = container.getRetObj();
            rowCount = retobj.getViewSequence().size();//getRowCount();
            actualrowcount = rowCount;
            graphrowcount = 10;
            String graph = (String) GraphHashMap2.get("GraphColumns");
            container.setSlectedGraphType(grpidfrmrep, selectgraph);
            container.setSelectedgraph(selectgraph);
            if (!isFromOneViewschedule) {
                session.setAttribute("selectgraph", selectgraph);
            }
            GraphHashMap = container.getGraphHashMap();
            HashMap map1 = container.getChangeGraphColumns();
            orginalcolumns = container.getOriginalColumns();
            tableColumns = container.getDisplayColumns();
            container.getCrossTabGraphHashMap();
            displaycollist = container.getDisplayLabels();
            HashMap jqgraphdetails = new HashMap();
            HashMap jqdetailmap = new HashMap();
            jqgraphdetails.put("grpidfrmrep", grpidfrmrep);
            jqgraphdetails.put("graphTypeid", graphid);
            jqgraphdetails.put("graphTypename", graphType);
            jqgraphdetails.put("selectedgraphtype", selectgraph);
            jqdetailmap.put(grpidfrmrep, jqgraphdetails);
            GraphHashMap.put("jqdetailmap" + grpidfrmrep, jqdetailmap);
            JqplotGraphProperty graphproperty = new JqplotGraphProperty();
            singleGraphDetails = (HashMap) GraphHashMap.get(grpidfrmrep);
            if (container.isReportCrosstab()) {
                tablecols = null;
            }

            if (singleGraphDetails.get("graphName") != null) {
                grpTitle = singleGraphDetails.get("graphName").toString();
            }

            if (singleGraphDetails.get("graphGridLines") != null && !singleGraphDetails.get("graphGridLines").toString().equalsIgnoreCase("null") && singleGraphDetails.get("graphGridLines").toString().equalsIgnoreCase("y")) {
                graphTypes.griddisplay = true;
            }
            if (singleGraphDetails.get("graphLegend") != null && !singleGraphDetails.get("graphLegend").toString().equalsIgnoreCase("null") && singleGraphDetails.get("graphLegend").toString().equalsIgnoreCase("y")) {
                graphTypes.legenddisplay = true;
            }
            if (singleGraphDetails.get("showLabels") != null && !singleGraphDetails.get("showLabels").toString().equalsIgnoreCase("null") && singleGraphDetails.get("showLabels").toString().equalsIgnoreCase("true")) {
                graphTypes.showlabels = true;
            }
            if (singleGraphDetails.get("colorGrouping") != null && !singleGraphDetails.get("colorGrouping").toString().equalsIgnoreCase("null")) {
                graphTypes.iscolorGrouping = Boolean.parseBoolean(singleGraphDetails.get("colorGrouping").toString());
            }
            if (singleGraphDetails.get("istranspose") != null && !singleGraphDetails.get("istranspose").toString().equalsIgnoreCase("null")) {
                istranpose = Boolean.parseBoolean(singleGraphDetails.get("istranspose").toString());
            }
            if (singleGraphDetails.get("showlyAxis") != null && !singleGraphDetails.get("showlyAxis").toString().equalsIgnoreCase("null")) {
                graphTypes.showlyAxis = singleGraphDetails.get("showlyAxis").toString();
            }
            if (singleGraphDetails.get("showryAxis") != null && !singleGraphDetails.get("showryAxis").toString().equalsIgnoreCase("null")) {
                graphTypes.showryAxis = singleGraphDetails.get("showryAxis").toString();
            }
            if (singleGraphDetails.get("showxAxis") != null && !singleGraphDetails.get("showxAxis").toString().equalsIgnoreCase("null")) {
                graphTypes.showxAxis = singleGraphDetails.get("showxAxis").toString();
            }
            String rowcount = singleGraphDetails.get("graphDisplayRows").toString();

            if (rowcount != null && !rowcount.equalsIgnoreCase("null") && !rowcount.isEmpty() && !rowcount.equalsIgnoreCase("")) {
                if (rowcount.equalsIgnoreCase("All") || actualrowcount < Integer.parseInt(rowcount)) {
                    graphrowcount = actualrowcount;
                } else {
                    graphrowcount = Integer.parseInt(rowcount);
                }
            }
            if (singleGraphDetails.get("GraphLocalDrill") != null) {

                graphTypes.showlocaldrill = (Boolean) singleGraphDetails.get("GraphLocalDrill");
            }
            if (singleGraphDetails.get("y-axiscalibration") != null && !singleGraphDetails.get("y-axiscalibration").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("y-axiscalibration").toString().isEmpty()) {

                graphTypes.yaxiscalibration = (String) singleGraphDetails.get("y-axiscalibration");

            }
            if (singleGraphDetails.get("ischeckedLAxis") != null) {

                graphTypes.ischeckedLA = singleGraphDetails.get("ischeckedLAxis").toString();

            }
            if (singleGraphDetails.get("ischeckedRAxis") != null) {

                graphTypes.ischeckedRA = singleGraphDetails.get("ischeckedRAxis").toString();

            }
            if (singleGraphDetails.get("ischeckedXAxishide") != null) {

                graphTypes.ischeckedXA = singleGraphDetails.get("ischeckedXAxishide").toString();

            }
            if (singleGraphDetails.get("ischeckedYAxishide") != null) {

                graphTypes.ischeckedYA = singleGraphDetails.get("ischeckedYAxishide").toString();

            }

            if (singleGraphDetails.get("Flagg") != null) {

                graphTypes.Flag = singleGraphDetails.get("Flagg").toString();

            }
            if (singleGraphDetails.get("viewbyLabels") != null) {
                graphTypes.viewbyLabels = singleGraphDetails.get("viewbyLabels").toString();

            }
            if (singleGraphDetails.get("yaxisstart") != null && !singleGraphDetails.get("yaxisstart").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("yaxisstart").toString().isEmpty()) {

                graphTypes.yaxisstart = (String) singleGraphDetails.get("yaxisstart");
            }
            if (singleGraphDetails.get("yaxisend") != null && !singleGraphDetails.get("yaxisend").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("yaxisend").toString().isEmpty()) {

                graphTypes.yaxisend = (String) singleGraphDetails.get("yaxisend");

            }
            if (singleGraphDetails.get("yaxisinterval") != null && !singleGraphDetails.get("yaxisinterval").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("yaxisinterval").toString().isEmpty()) {

                graphTypes.yaxisinterval = (String) singleGraphDetails.get("yaxisinterval");

            }
            if (singleGraphDetails.get("xaxisstart") != null && !singleGraphDetails.get("xaxisstart").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("xaxisstart").toString().isEmpty()) {

                graphTypes.xaxisstart = (String) singleGraphDetails.get("xaxisstart");
            }
            if (singleGraphDetails.get("xaxisend") != null && !singleGraphDetails.get("xaxisend").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("xaxisend").toString().isEmpty()) {

                graphTypes.xaxisend = (String) singleGraphDetails.get("xaxisend");

            }
            if (singleGraphDetails.get("xaxisinterval") != null && !singleGraphDetails.get("xaxisinterval").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("xaxisinterval").toString().isEmpty()) {

                graphTypes.xaxisinterval = (String) singleGraphDetails.get("xaxisinterval");

            }
            if (singleGraphDetails.get("y2axisstart") != null && !singleGraphDetails.get("y2axisstart").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("y2axisstart").toString().isEmpty()) {

                graphTypes.y2axisstart = (String) singleGraphDetails.get("y2axisstart");
            }
            if (singleGraphDetails.get("y2axisend") != null && !singleGraphDetails.get("y2axisend").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("y2axisend").toString().isEmpty()) {

                graphTypes.y2axisend = (String) singleGraphDetails.get("y2axisend");

            }
            if (singleGraphDetails.get("y2axisinterval") != null && !singleGraphDetails.get("y2axisinterval").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("y2axisinterval").toString().isEmpty()) {

                graphTypes.y2axisinterval = (String) singleGraphDetails.get("y2axisinterval");

            }
            if (singleGraphDetails.get("yAxisRounding") != null && !singleGraphDetails.get("yAxisRounding").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("yAxisRounding").toString().isEmpty()) {

                graphTypes.yAxisRounding = Integer.parseInt(singleGraphDetails.get("yAxisRounding").toString());

            }
            if (singleGraphDetails.get("xAxisRounding") != null && !singleGraphDetails.get("xAxisRounding").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("xAxisRounding").toString().isEmpty()) {

                graphTypes.xAxisRounding = Integer.parseInt(singleGraphDetails.get("xAxisRounding").toString());

            }
            if (singleGraphDetails.get("y2AxisRounding") != null && !singleGraphDetails.get("y2AxisRounding").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("y2AxisRounding").toString().isEmpty()) {

                graphTypes.y2AxisRounding = Integer.parseInt(singleGraphDetails.get("y2AxisRounding").toString());

            }
            if (singleGraphDetails.get("targetValue") != null && !singleGraphDetails.get("targetValue").toString().equalsIgnoreCase("null")) {

                graphTypes.targetValue = singleGraphDetails.get("targetValue").toString();

            } //SOC by kruthika
            if (singleGraphDetails.get("tickdisplay") != null && !singleGraphDetails.get("tickdisplay").toString().equalsIgnoreCase("null")) {

                graphTypes.tickdisplay = singleGraphDetails.get("tickdisplay").toString();

            } //EOD by kruthika
            if (singleGraphDetails.get("GraphdisplayCols") != null && !singleGraphDetails.get("GraphdisplayCols").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("GraphdisplayCols").toString().isEmpty()) {

                displayCols = (String) singleGraphDetails.get("GraphdisplayCols");

            } else if (JqplotGraphProperty != null && JqplotGraphProperty.getGraphDisplayCols() != null) {
                displayCols = JqplotGraphProperty.getGraphDisplayCols();
            }

            if (singleGraphDetails.get("rgbColorArr") != null && singleGraphDetails.get("rgbColorArr").toString().length() != 0 && !singleGraphDetails.get("rgbColorArr").toString().isEmpty()) {
                String[] background1 = (String[]) singleGraphDetails.get("rgbColorArr");
                if (background1 != null && background1.length != 0) {
                    background = (String[]) singleGraphDetails.get("rgbColorArr");
                }
            }

            if (background != null && background.toString().isEmpty() && background.length != 0) {
                rgb = "rgb(R,G,B)";
                rgb = rgb.replace("R", background[0]);
                rgb = rgb.replace("G", background[1]);
                rgb = rgb.replace("B", background[2]);
            }
            if (singleGraphDetails.get("measureFormat") != null && !singleGraphDetails.get("measureFormat").toString().isEmpty() && !singleGraphDetails.get("measureFormat").toString().equalsIgnoreCase("null")) {
                graphTypes.measureFormat = singleGraphDetails.get("measureFormat").toString();
            }
            if (singleGraphDetails.get("nbrFormat") != null && !singleGraphDetails.get("nbrFormat").toString().isEmpty() && !singleGraphDetails.get("nbrFormat").toString().equalsIgnoreCase("null")) {
                graphTypes.nbrFormat = singleGraphDetails.get("nbrFormat").toString();
            }
            if (singleGraphDetails.get("measureValueRounding") != null && !singleGraphDetails.get("measureValueRounding").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("measureValueRounding").toString().isEmpty()) {
                graphTypes.measureRound = Integer.parseInt(singleGraphDetails.get("measureValueRounding").toString());
            }
            if (singleGraphDetails.get("graphLegendLoc") != null && !singleGraphDetails.get("graphLegendLoc").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("graphLegendLoc").toString().isEmpty()) {
                graphTypes.graphLegendLoc = singleGraphDetails.get("graphLegendLoc").toString();
                if (graphTypes.graphLegendLoc.equalsIgnoreCase("bottom")) {
                    graphTypes.graphLegendLoc = "s";
                } else if (graphTypes.graphLegendLoc.equalsIgnoreCase("top")) {
                    graphTypes.graphLegendLoc = "n";
                } else if (graphTypes.graphLegendLoc.equalsIgnoreCase("left")) {
                    graphTypes.graphLegendLoc = "w";
                } else if (graphTypes.graphLegendLoc.equalsIgnoreCase("right")) {
                    graphTypes.graphLegendLoc = "e";
                }
                if (graphType.equalsIgnoreCase("Funnel(INV)")) {
                    if (graphTypes.graphLegendLoc.equalsIgnoreCase("s")) {
                        graphTypes.graphLegendLoc = "n";
                    } else if (graphTypes.graphLegendLoc.equalsIgnoreCase("n")) {
                        graphTypes.graphLegendLoc = "s";
                    } else if (graphTypes.graphLegendLoc.equalsIgnoreCase("w")) {
                        graphTypes.graphLegendLoc = "e";
                    } else if (graphTypes.graphLegendLoc.equalsIgnoreCase("e")) {
                        graphTypes.graphLegendLoc = "w";
                    }
                }
            }
            String[] colorar = (String[]) singleGraphDetails.get("colorSeries");

            if (colorar != null && !colorar.toString().isEmpty()) {
                List<String> colorList = Arrays.asList(colorar);
                if (!colorList.contains("Default")) {
                    colors = colorar;
                } else {
                    colors = graphTypes.seriescolors1;
                }
            } else if (colornew != null && !colornew.toString().isEmpty()) {
                List<String> colorList = Arrays.asList(colornew);
                if (!colorList.contains("Default")) {
                    colors = colornew;
                } else {
                    colors = graphTypes.seriescolors1;
                }

            } else {
                colors = graphTypes.seriescolors1;
            }
            if (colors != null) {
                Seriescolors = new ArrayList();
                for (int i = 0; i < colors.length; i++) {
                    Seriescolors.add("'" + colors[i] + "'");
                }
            }
            if (singleGraphDetails.get("isAdhocEnabled") != null) {
                graphTypes.isAdhocEnabled = Boolean.parseBoolean(singleGraphDetails.get("isAdhocEnabled").toString());

            }
            if (singleGraphDetails.get("labeldir") != null && !singleGraphDetails.get("labeldir").toString().equalsIgnoreCase("null") && !singleGraphDetails.get("labeldir").toString().isEmpty()) {

                graphTypes.labelDir = Integer.parseInt(singleGraphDetails.get("labeldir").toString());

            }
            if (singleGraphDetails.get("legendsPerRow") != null && !singleGraphDetails.get("legendsPerRow").toString().equalsIgnoreCase("null")) {


                if (singleGraphDetails.get("legendsPerRow").toString().isEmpty()) {
                    graphTypes.legendsPerRow = 8;
                } else {
                    graphTypes.legendsPerRow = Integer.parseInt(singleGraphDetails.get("legendsPerRow").toString());
                }

            }
            if (singleGraphDetails.get("graphDisplay") != null && !singleGraphDetails.get("graphDisplay").toString().equalsIgnoreCase("null")) {
                graphDisplay = singleGraphDetails.get("graphDisplay").toString();
            }
            if (singleGraphDetails.get("graphtype1") != null && !singleGraphDetails.get("graphtype1").toString().isEmpty() && !singleGraphDetails.get("graphtype1").toString().equalsIgnoreCase("null")) {
                graphTypes.graphtype1 = singleGraphDetails.get("graphtype1").toString();
            }
            if (singleGraphDetails.get("graphtype2") != null && !singleGraphDetails.get("graphtype2").toString().isEmpty() && !singleGraphDetails.get("graphtype2").toString().equalsIgnoreCase("null")) {
                graphTypes.graphtype2 = singleGraphDetails.get("graphtype2").toString();
            }
            if (singleGraphDetails.get("targetcolor") != null && !singleGraphDetails.get("targetcolor").toString().isEmpty() && !singleGraphDetails.get("targetcolor").toString().equalsIgnoreCase("null")) {
                graphTypes.targetcolor = singleGraphDetails.get("targetcolor").toString();
            }
            if (singleGraphDetails.get("drilltype") != null && !singleGraphDetails.get("drilltype").toString().isEmpty() && !singleGraphDetails.get("drilltype").toString().equalsIgnoreCase("null")) {
                graphTypes.drilltype = singleGraphDetails.get("drilltype").toString();
            }
            if (graphTypes.drilltype.equalsIgnoreCase("adhoc")) {
                graphTypes.isAdhocEnabled = true;
            }
            if (singleGraphDetails.get("datebyInterval") != null && !singleGraphDetails.get("datebyInterval").toString().isEmpty() && !singleGraphDetails.get("datebyInterval").toString().equalsIgnoreCase("null")) {
                graphTypes.datebyInterval = singleGraphDetails.get("datebyInterval").toString();
            }
            if (singleGraphDetails.get("targetType") != null && !singleGraphDetails.get("targetType").toString().isEmpty() && !singleGraphDetails.get("targetType").toString().equalsIgnoreCase("null")) {
                targetType = singleGraphDetails.get("targetType").toString();
            }
            if (singleGraphDetails.get("appendXaxis") != null && !singleGraphDetails.get("appendXaxis").toString().isEmpty() && !singleGraphDetails.get("appendXaxis").toString().equalsIgnoreCase("null")) {
                graphTypes.isappendXaxis = Boolean.valueOf(singleGraphDetails.get("appendXaxis").toString());
            }
            if (singleGraphDetails.get("legendAppend") != null && !singleGraphDetails.get("legendAppend").toString().isEmpty() && !singleGraphDetails.get("legendAppend").toString().equalsIgnoreCase("null")) {
                islegendAppend = Boolean.valueOf(singleGraphDetails.get("legendAppend").toString());
            }
            if (singleGraphDetails.get("zeroValues") != null && !singleGraphDetails.get("zeroValues").toString().isEmpty() && !singleGraphDetails.get("zeroValues").toString().equalsIgnoreCase("null")) {
                isSupriseZero = Boolean.valueOf(singleGraphDetails.get("zeroValues").toString());
            }
            if (singleGraphDetails.get("tooltipXaxis") != null && !singleGraphDetails.get("tooltipXaxis").toString().isEmpty() && !singleGraphDetails.get("tooltipXaxis").toString().equalsIgnoreCase("null")) {
                graphTypes.istooltipXaxis = Boolean.valueOf(singleGraphDetails.get("tooltipXaxis").toString());
            }
            if (singleGraphDetails.get("isShowOthers") != null) {

                graphTypes.viewOthers = singleGraphDetails.get("isShowOthers").toString();

            }
            if (singleGraphDetails.get("OtherFlag") != null) {

                graphTypes.isOthersFlag = singleGraphDetails.get("OtherFlag").toString();

            }
            graphproperty.setGraphId(grpidfrmrep);
            graphproperty.setGraphTypename(graphType);
            graphproperty.setGraphTypeId(graphid);
            graphproperty.setSlectedGraphType(grpidfrmrep, selectgraph);
            graphproperty.setReportId(reportid);
            graphproperty.setRowValues(rowValues);
            graphproperty.setYaxiscalibration(graphTypes.yaxiscalibration);
            graphproperty.setYaxisstart(graphTypes.yaxisstart);
            graphproperty.setYaxisend(graphTypes.yaxisend);
            graphproperty.setischeckedLA(graphTypes.ischeckedLA);
            graphproperty.setischeckedRA(graphTypes.ischeckedRA);
            graphproperty.setischeckedXA(graphTypes.ischeckedXA);
            graphproperty.setischeckedYA(graphTypes.ischeckedYA);
            graphproperty.setFlag(graphTypes.Flag);
            graphproperty.setviewbyLabels(graphTypes.viewbyLabels);
            graphproperty.setYaxisinterval(graphTypes.yaxisinterval);
            graphproperty.setXaxisstart(graphTypes.xaxisstart);
            graphproperty.setXaxisend(graphTypes.xaxisend);
            graphproperty.setXaxisinterval(graphTypes.xaxisinterval);
            graphproperty.setY2axisstart(graphTypes.y2axisstart);
            graphproperty.setY2axisend(graphTypes.y2axisend);
            graphproperty.setY2axisinterval(graphTypes.y2axisinterval);
            graphproperty.setyAxisRounding(graphTypes.yAxisRounding);
            graphproperty.setxAxisRounding(graphTypes.xAxisRounding);
            graphproperty.setY2AxisRounding(graphTypes.y2AxisRounding);
            graphproperty.setTargetValue(graphTypes.targetValue); //kruthika
            graphproperty.settickdisplay(graphTypes.tickdisplay);
            graphproperty.setGraphDisplayCols(displayCols);
            graphproperty.setAdhocEnabled(graphTypes.isAdhocEnabled);
            graphproperty.setColorGrouping(graphTypes.iscolorGrouping);
            graphproperty.setLabelDirection(graphTypes.labelDir);
            graphproperty.setLegendsPerRow(graphTypes.legendsPerRow);
            graphproperty.setTableColumns(tablecols);
            graphproperty.setGraphdisptype(graphDisplay);
            graphproperty.setGraphtype1(graphTypes.graphtype1);
            graphproperty.setGraphtype2(graphTypes.graphtype2);
            graphproperty.setTargetColor(graphTypes.targetcolor);
            graphproperty.setDrillType(graphTypes.drilltype);
            graphproperty.setDatebyInterval(graphTypes.datebyInterval);
            graphproperty.setTargetType(targetType);
            graphproperty.setisOthersFlag(graphTypes.isOthersFlag);
            graphproperty.setviewOthers(graphTypes.viewOthers);
            container.getGraphMapDetails();
            container.initializeRuntimeMeasures();
            graphproperty.setAppendXaxis(graphTypes.isappendXaxis);
            graphproperty.setLegenaAppend(islegendAppend);
            graphproperty.setSupriseZero(isSupriseZero);
            graphproperty.settooltipXaxis(graphTypes.istooltipXaxis);
            graphTypes.rgb = rgb;
            graphTypes.isFromOneView = isFromOneView;
            graphTypes.isFromOneViewschedule = isFromOneViewschedule;
            graphTypes.tickId = tickId;
            graphTypes.chartId = chartId;
            graphTypes.ishomeTab = ishomeTab;
            if (ishomeTab) {
                graphTypes.xaxisfontSize = "6pt";
                graphTypes.yaxisfontSize = "5.5pt";
                graphTypes.legenddisplay = false;
            } else {
                graphTypes.xaxisfontSize = "7.35pt";
                graphTypes.yaxisfontSize = "7pt";
            }
            if (graphTypes.legendsPerRow == 0) {
                graphTypes.legendsPerRow = 8;
            }
            graphrowcount += container.getFromRow();
            if (actualrowcount < graphrowcount) {
                graphrowcount = actualrowcount;
            }
            if (ishomeTab && graphrowcount > 10) {
                graphrowcount = 10;
            }
            if (map1 != null && !map1.isEmpty()) {

                String graphmeasId = null;
                ArrayList graphmeasures = null;

                String graphId[] = null;

                HashMap graphcolumnHashMap = container.getChangeGraphColumns();
                if (graphcolumnHashMap.size() != 0) {
                    graphColumns = null;
                    if (graphcolumnHashMap.get("GraphColumns") != null && !graphcolumnHashMap.get("GraphColumns").toString().isEmpty()) {
                        if (graphcolumnHashMap.get("GraphColumns").toString().contains("ST") || graphcolumnHashMap.get("GraphColumns").toString().contains("GT") || graphcolumnHashMap.get("GraphColumns").toString().contains("AVG")) {
                            graphColumns = graphcolumnHashMap.get("GraphColumns").toString().split(",");
                        }
                        graphId = graphcolumnHashMap.get("grpIds").toString().split(",");
                    }

                    if (graphId != null && !graphId.toString().isEmpty()) {
                        singleGraphDetails = (HashMap) GraphHashMap.get(grpidfrmrep);
                        if (singleGraphDetails != null) {
                            PrevbarChartColumnNames = (String[]) singleGraphDetails.get("barChartColumnNames");
                            PrevbarChartColumnTitles = (String[]) singleGraphDetails.get("barChartColumnTitles");
                            viewByColumns = (String[]) singleGraphDetails.get("viewByElementIds");
                            for (int j = viewByColumns.length; j < PrevbarChartColumnNames.length; j++) {
                                graphlist.add(PrevbarChartColumnNames[j]);
                                columnnames.add(PrevbarChartColumnTitles[j]);
                            }
                        }

                    }
                }
                for (int i = 0; i < columnnames.size(); i++) {
                    legends.add("\"" + columnnames.get(i) + "\"");
                }
                numberRows = (legends.size()) / 5;
                if (tablecols != null) {
                    graphlist.clear();
                    legends.clear();
                    for (int j = 0; j < tablecols.length; j++) {
                        graphlist.add(tablecols[j]);
                        String columnname = container.getMeasureName(tablecols[j]);
                        if (columnname.contains("\"")) {
                            legends.add("\'" + columnname + "\'");
                        } else {
                            legends.add("\"" + columnname + "\"");
                        }
                    }
                }
            } else {
                singleGraphDetails = (HashMap) GraphHashMap.get(grpidfrmrep);
                if (singleGraphDetails != null) {
                    PrevbarChartColumnNames = (String[]) singleGraphDetails.get("barChartColumnNames");
                    PrevbarChartColumnTitles = (String[]) singleGraphDetails.get("barChartColumnTitles");
                    viewByColumns = (String[]) singleGraphDetails.get("viewByElementIds");
                    for (int j = viewByColumns.length; j < PrevbarChartColumnNames.length; j++) {

                        graphlist.add(PrevbarChartColumnNames[j]);
                        if (!container.isReportCrosstab()) {
                            columnnames.add(container.getMeasureName(PrevbarChartColumnNames[j]));
                        } else {
                            columnnames.add(PrevbarChartColumnTitles[j]);
                        }
                    }
                    for (int i = 0; i < columnnames.size(); i++) {
                        if (columnnames.get(i) != null) {
                            if (columnnames.get(i).toString().contains("\"")) {
                                legends.add("\'" + columnnames.get(i) + "\'");
                            } else {
                                legends.add("\"" + columnnames.get(i) + "\"");
                            }
                        }
                    }
                    numberRows = (legends.size()) / 5;
                }
                if (tablecols != null) {
                    graphlist.clear();
                    legends.clear();
                    for (int j = 0; j < tablecols.length; j++) {
                        graphlist.add(tablecols[j]);
                        String columnname = container.getMeasureName(tablecols[j]);
                        legends.add("\"" + columnname + "\"");
                    }
                }

            }
            tableCollabel = container.getDisplayLabels();
            if (graphrowcount > 10) {
                for (int i = 0; i < graphTypes.seriescolors.length; i++) {
                    Seriescolors.add("'" + graphTypes.seriescolors[i] + "'");
                }
            }

            graphproperty.setGraphColumns(graphColumns);
            graphproperty.setSeriescolors(colors);
            graphproperty.setGraphDrill(graphTypes.showlocaldrill);
            graphproperty.setIsTranspose(istranpose);
            GraphHashMap.put("jqgraphproperty" + grpidfrmrep, graphproperty);
        }

        ReportParameter repParam = container.getReportParameter();
        //GraphHashMap = container.getGraphHashMap();
        ArrayList<String> sortCols = null;
        String groupCols = "";
        char[] sortTypes = null;//ArrayList sortTypes = null;
        char[] sortDataTypes = null;
        ArrayList<Integer> rowSequence;
        sortCols = container.getSortColumns();
        sortTypes = container.getSortTypes();
        groupCols = container.getGroupColumns();

        String viewById = null;
        if (!container.getReportCollect().getReportViewByMain().keySet().isEmpty() && container.getReportCollect().getReportViewByMain().keySet() != null) {
            Set<String> viewByIdSet = container.getReportCollect().getReportViewByMain().keySet();
            for (String viewByIdKey : viewByIdSet) {
                viewById = viewByIdKey;
            }
        }

        int columnCount = container.getDisplayColumns().size();
        ArrayList viewbylist = (ArrayList) container.getViewByElementIds();

        ArrayList list = (ArrayList) container.getParametersHashMap().get("Parameters");

        ArrayList list1 = (ArrayList) container.getParametersHashMap().get("ParametersNames");
        int index = list.indexOf(viewbylist.get(0));
        if (list.contains(viewbylist.get(0))) {
            index++;
            if (index < list.size()) {
                nextviewbyid = (String) list.get(index);
            }
        }
        String presviewbyid = viewbylist.get(0).toString();
        HashMap<String, String> drillHelpMap = collect.getDrillMap();
        if (drillHelpMap != null && drillHelpMap.get(presviewbyid) != null && !drillHelpMap.get(presviewbyid).isEmpty()) {
            nextviewbyid = drillHelpMap.get(presviewbyid);
        }

        HashMap map1 = container.getChangeGraphColumns();
        String pagesPerSlide = container.getPagesPerSlide();
        if (sort != null) //user hasn't done any sort
        {

            if (sortCols != null && !presviewbyid.equalsIgnoreCase("TIME") && (groupCols == null || groupCols.isEmpty())) //check if any previous sort is present retain them
            {
                sortCols = container.getSortColumns();
                if (!sortCols.isEmpty()) {
                    refreshGraph = true;
                    sortTypes = container.getSortTypes();
                    sortDataTypes = container.getSortDataTypes();
                    rowSequence = retobj.sortDataSet(sortCols, sortTypes, sortDataTypes);//dataTypes, container.getOriginalColumns());
                    retobj.setViewSequence(rowSequence);
                    rowCount = rowSequence.size();
//                         if(presviewbyid.equalsIgnoreCase("TIME")){
//                retobj.resetViewSequence();
//            }
                }

            }
        }
        StringBuffer jqplotbuffer = new StringBuffer();

        ArrayList pielist = new ArrayList();
        ArrayList donutrowlist = new ArrayList();
        ArrayList donutrowlist1 = new ArrayList();
        ArrayList valuelist1 = new ArrayList();
        ArrayList rowviewlist = new ArrayList();


        ArrayList rowviewlistother = new ArrayList();
        ArrayList rowviewvaluelist = new ArrayList();
        ArrayList tworowviewlist = new ArrayList();
        ArrayList tworowtooltip = new ArrayList();
        String[] timeValues;
        ArrayList groupSeries = new ArrayList();
        // for summarizes measures
        HashMap summarizedmMesMap = container.getSummerizedTableHashMap();
        ArrayList summmeas = new ArrayList();
        ArrayList summmeasTitle = new ArrayList();
        if (summarizedmMesMap != null && !summarizedmMesMap.isEmpty()) {
            summmeas.addAll((List<String>) summarizedmMesMap.get("summerizedQryeIds"));
            summmeasTitle.addAll((List<String>) summarizedmMesMap.get("summerizedQryColNames"));
        }
        //  ends

        HashMap msrround = new HashMap();
        HashMap msrnbrformat = new HashMap();
        String msrid = "";
        String nbrSymbol = "";
        int precision = 0;
        String gpid = "";
        ArrayList<String> MeasureIdss = container.getTableDisplayMeasures();   //kruthika
        for (int ms = 0; ms < MeasureIdss.size(); ms++) {
            msrid = MeasureIdss.get(ms).toString();
            if (graphlist.contains(msrid)) {
                // nbrSymbol = container.getNumberSymbol(msrid);
                nbrSymbol = container.symbol.get(msrid);
                precision = container.getRoundPrecisionForMeasure(msrid);
                msrnbrformat.put(msrid, nbrSymbol);
                msrround.put(msrid, precision);
            }
        }

        if (retobj != null && graphrowcount != 0) {
//            for (int i = container.getFromRow(); i < graphrowcount; i++) {
            for (int i = container.getFromRow(); i < retobj.getRowCount(); i++) {
                graphTypes.nbrFormat = singleGraphDetails.get("nbrFormat").toString();
                String MeasureValue2 = "0.0";
                String MeasureValue1 = "0.0";
                if (graphlist.size() > 1) {
                }
                if (graphlist != null && !graphlist.isEmpty()) {
                    if (container.getDataTypes().get(0).equals("D")) {
//                            if(retobj.getFieldValueDateString(retobj.getViewSequence().get(i), (String) orginalcolumns.get(0)).contains("\"")){
//                                rowviewlist.add("\'" + retobj.getFieldValueDateString(retobj.getViewSequence().get(i), (String) orginalcolumns.get(0)) + "\'");
//                            }else
//                            rowviewlist.add("\"" + retobj.getFieldValueDateString(retobj.getViewSequence().get(i), (String) orginalcolumns.get(0)) + "\"");
                        if (retobj.getFieldValueDateString(retobj.getViewSequence().get(i), (String) orginalcolumns.get(0)).contains("\"")) {
                            rowviewlist.add("\'" + retobj.getFieldValueDateString(i, (String) orginalcolumns.get(0)) + "\'");
                        } else {
                            rowviewlist.add("\"" + retobj.getFieldValueDateString(i, (String) orginalcolumns.get(0)) + "\"");
                        }

                    } else {
//                             if(retobj.getFieldValueDateString(retobj.getViewSequence().get(i), (String) orginalcolumns.get(0)).contains("\"")){
//                                rowviewlist.add("\'" + retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) orginalcolumns.get(0)) + "\'");
//                            }else
//                            rowviewlist.add("\"" + retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) orginalcolumns.get(0)) + "\"");
                        if (retobj.getFieldValueDateString(retobj.getViewSequence().get(i), (String) orginalcolumns.get(0)).contains("\"")) {
                            rowviewlist.add("\'" + retobj.getFieldValueString(i, (String) orginalcolumns.get(0)) + "\'");
                        } else {
                            rowviewlist.add("\"" + retobj.getFieldValueString(i, (String) orginalcolumns.get(0)) + "\"");
                        }

                    }
//                        rowviewvaluelist.add(retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(0)));
                    BigDecimal bd = BigDecimal.ZERO;
                    if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(0))) {
//                            bd=retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(i), (String) graphlist.get(0));
                        bd = retobj.getFieldValueRuntimeMeasure(i, (String) graphlist.get(0));
                    } else {
                        gpid = (String) graphlist.get(0);
//                             MeasureValue1=retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(0));
                        MeasureValue1 = retobj.getFieldValueString(i, (String) graphlist.get(0));
                        if (MeasureValue1 == null || MeasureValue1.isEmpty() || MeasureValue1.equalsIgnoreCase("")) {
                            MeasureValue1 = "0.0";
                        }
                        bd = new BigDecimal(MeasureValue1);
                    }

                    if (graphTypes.nbrFormat.equalsIgnoreCase("null") || graphTypes.nbrFormat.isEmpty() || graphTypes.nbrFormat.equalsIgnoreCase("")) {
                        graphTypes.nbrFormat = ""; //kruthika
                        if (msrround.get(gpid) != null) {
                            graphTypes.measureRound = Integer.parseInt(msrround.get(gpid).toString());
                        }
                        if (msrnbrformat.get(gpid) != null) {
                            graphTypes.nbrFormat = msrnbrformat.get(gpid).toString();
                        }
                        if (graphTypes.measureRound == -999) {
                            //graphTypes.measureRound = Integer.parseInt(singleGraphDetails.get("measureValueRounding").toString());
                            graphTypes.measureRound = 2;
                        }
                    } else {
                        graphTypes.nbrFormat = singleGraphDetails.get("nbrFormat").toString();
                        graphTypes.measureRound = Integer.parseInt(singleGraphDetails.get("measureValueRounding").toString());

                    }
                    if (graphTypes.nbrFormat.equalsIgnoreCase("%")) {
                        graphTypes.nbrFormat = "";
                        rowviewvaluelist.add(NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                        graphTypes.nbrFormat = "%";
                    } else {
                        rowviewvaluelist.add(NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                    }
                    if (graphlist.size() > 1) {
//                            valuelist1.add(retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(1)));
                        if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(1))) {
//                                 bd=retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(i), (String) graphlist.get(1));
                            bd = retobj.getFieldValueRuntimeMeasure(i, (String) graphlist.get(1));
                        } else {
//                                  MeasureValue2=retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(1));
                            MeasureValue2 = retobj.getFieldValueString(i, (String) graphlist.get(1));
                            if (MeasureValue2 == null || MeasureValue2.isEmpty() || MeasureValue2.equalsIgnoreCase("")) {
                                MeasureValue2 = "0.0";
                            }
                            bd = new BigDecimal(MeasureValue2);
                        }
                        valuelist1.add(NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                    }
                }
                graphTypes.data = rowviewvaluelist;
            }
//            String othersval=""; //code commented by kruthika for others
//            double OG=0.0;
//            if( graphType.equalsIgnoreCase("DualAxis(Bar-Line)") || graphType.equalsIgnoreCase("DualAxis(Area-Line)")){
//
//            } else {
////            if (graphTypes.viewOthers.equalsIgnoreCase("true")) {
////                             for(int ss=0;ss<graphrowcount;ss++) {
////             othersval=rowviewvaluelist.get(ss).toString();
////                OG=OG+Double.parseDouble(othersval);
////
////               }
////             ArrayList<String> MeasureIds = container.getTableDisplayMeasures();
////          ArrayList<Object> othersData = container.getOthersData();
////             for (int s2 = 0; s2 < MeasureIds.size(); s2++)
////          {
////              String Othermeasure = MeasureIds.get(s2);
////              if (graphlist.contains(Othermeasure))
////              {
////                BigDecimal bdvalue =retobj.getColumnGrandTotalValue(MeasureIds.get(s2)).subtract(new BigDecimal(OG));
////                rowviewvaluelist.add(bdvalue);
////              }
////          }   rowviewlist.add("\" others \"");
////               rowviewlistother.addAll(rowviewlist);
//////         graphTypes.data = rowviewvaluelist;
////               }
//      } EODC by kruthika
            viewByColumns = (String[]) singleGraphDetails.get("viewByElementIds");
            if ((graphTypes.Flag).equalsIgnoreCase("true")) {
                if (viewByColumns.length >= 2) {
                    int s = 0;
                    String temp1 = "";
                    String[] viewbyelement;
//                  for (int i = container.getFromRow(); i < graphrowcount; i++) {
                    for (int i = container.getFromRow(); i < retobj.getRowCount(); i++) {
                        StringBuffer sb = new StringBuffer();
                        StringBuffer group = new StringBuffer();
                        String Viewbycolumnname = graphTypes.viewbyLabels;
                        String[] viewbys = Viewbycolumnname.split(",");
                        ArrayList viewbylistNames = (ArrayList) container.getViewByColNames();
                        for (s = 0; s < viewByColumns.length; s++) {
                            String elementid = viewByColumns[s];
                            String names = container.getMeasureName(elementid);
                            for (int n = 0; n < viewbys.length; n++) {
                                if (names.equalsIgnoreCase(viewbys[n])) {
                                    String measureelementid = elementid;
                                    if (n != 0) {
                                        sb.append(",");
                                    }
//                    sb.append(retobj.getFieldValueString(retobj.getViewSequence().get(i), measureelementid));
                                    sb.append(retobj.getFieldValueString(i, measureelementid));

                                }
                            }
                        }
                        tworowviewlist.add("\"" + sb.toString() + "\"");

                    }
                }
            } else if (viewByColumns.length >= 2) {
//                graphrowcount-=container.getFromRow();
                int k = 0;
                String temp1 = "";
//                for (int i = container.getFromRow(); i < graphrowcount; i++) {
                for (int i = container.getFromRow(); i < retobj.getRowCount(); i++) {
                    StringBuffer sb = new StringBuffer();
                    StringBuffer group = new StringBuffer();

                    for (int j = 0; j < viewByColumns.length; j++) {
//                        sb.append(retobj.getFieldValueString(retobj.getViewSequence().get(i), viewByColumns[j]));
                        sb.append(retobj.getFieldValueString(i, viewByColumns[j]));
                        if (j < viewByColumns.length - 1) {
                            sb.append(",");
                        }

                    }
                    for (int j = 0; j < viewByColumns.length - 1; j++) {
//                         group.append(retobj.getFieldValueString(retobj.getViewSequence().get(i), viewByColumns[j]));
                        group.append(retobj.getFieldValueString(i, viewByColumns[j]));
                        if (j < viewByColumns.length - 2) {
                            group.append(",");
                        }
                    }
                    if (temp1.equalsIgnoreCase(group.toString()) || temp1.isEmpty() || temp1.equalsIgnoreCase("")) {
                        groupSeries.add(Seriescolors.get(k));

                    } else {
                        k++;
                        groupSeries.add(Seriescolors.get(k));
                    }
                    temp1 = group.toString();
                    if (sb.toString().contains("\"")) {
                        tworowviewlist.add("\'" + sb.toString() + "\'");
                    } else {
                        tworowviewlist.add("\"" + sb.toString() + "\"");
                    }
                }
                numberRows = (tworowviewlist.size()) / 5;
                if (viewByColumns.length >= 2 && graphTypes.iscolorGrouping && !graphType.equalsIgnoreCase("Scatter") && graphType.equalsIgnoreCase("bar-vertical")) {
                    legends.clear();
                    ArrayList test = new ArrayList();
                    graphTypes.varyBarColor = true;
                    ArrayList testlist = new ArrayList();
                    for (int i = 0; i < tworowviewlist.size(); i++) {
                        testlist.add(retobj.getFieldValueString(i, PrevbarChartColumnNames[viewByColumns.length]) != null ? retobj.getFieldValueString(i, PrevbarChartColumnNames[viewByColumns.length]) : "0");
                        //
                        if (!test.contains(tworowviewlist.get(i).toString().substring(1, tworowviewlist.get(i).toString().lastIndexOf(",")))) {
                            test.add(tworowviewlist.get(i).toString().substring(1, tworowviewlist.get(i).toString().lastIndexOf(",")));
                            if (tworowviewlist.get(i).toString().substring(0, tworowviewlist.get(i).toString().lastIndexOf(",")).substring(0).contains("\"")) {
                                legends.add("{label:" + tworowviewlist.get(i).toString().substring(0, tworowviewlist.get(i).toString().lastIndexOf(",")) + "\",color:" + groupSeries.get(i) + "}");
                            } else {
                                legends.add("{label:" + tworowviewlist.get(i).toString().substring(0, tworowviewlist.get(i).toString().lastIndexOf(",")) + "\',color:" + groupSeries.get(i) + "}");
                            }
                        }
                    }
                }


            }
        }
        ArrayList lites = new ArrayList();
        if ((graphType.equalsIgnoreCase("StackedBar(V)") || graphType.equalsIgnoreCase("DualAxis(Bar-Line)") || graphType.equalsIgnoreCase("DualAxis(Area-Line)") || graphType.equalsIgnoreCase("Bar-Vertical") || graphType.equalsIgnoreCase("StackedArea") || graphType.equalsIgnoreCase("Waterfall") || graphType.equalsIgnoreCase("Waterfall(GT)") || graphType.equalsIgnoreCase("Area") || graphType.equalsIgnoreCase("Overlaid(Bar-Line)") || graphType.equalsIgnoreCase("Bar-Horizontal") || graphType.equalsIgnoreCase("StackedBar(H)")) || graphType.equalsIgnoreCase("Donut-Single") || graphType.equalsIgnoreCase("Donut-Double") || graphType.equalsIgnoreCase("Line") || graphType.equalsIgnoreCase("Line(Smooth)") || graphType.equalsIgnoreCase("Line(Dashed)") || graphType.equalsIgnoreCase("Area-Line") || graphType.equalsIgnoreCase("Line(Simple)") || graphType.equalsIgnoreCase("Line(Simple-R)") || graphType.equalsIgnoreCase("Line(Std)") || graphType.equalsIgnoreCase("Dot-Graph") || graphType.equalsIgnoreCase("mekko") || graphType.equalsIgnoreCase("Overlaid(Bar-Dot)") && graphType != null) {
            //For average,Grandtotal and subtotal values
            if (graphDisplay != null && !graphDisplay.equalsIgnoreCase("null") && (graphDisplay.equalsIgnoreCase("GT") || graphDisplay.equalsIgnoreCase("AVG") || graphDisplay.equalsIgnoreCase("ST"))) {
                graphcolumns1.clear();
                rowviewlist.clear();
                BigDecimal st = new BigDecimal(0);
                ArrayList graphcolumns = new ArrayList();
                if (graphColumns != null && !(graphlist.size() > 1)) {
                    for (int i = 0; i < graphlist.size(); i++) {
                        for (int k = 0; k < graphColumns.length; k++) {
                            BigDecimal bd = new BigDecimal(0);
                            if (graphColumns[k].toString().contains("ST")) {
                                for (int j = 0; j < Integer.parseInt(container.getPagesPerSlide().toString()); j++) {
                                    if (j < actualrowcount && j < Integer.parseInt(container.getPagesPerSlide().toString())) {
                                        st = new BigDecimal(retobj.getFieldValueString(retobj.getViewSequence().get(j), (String) graphlist.get(i)));
                                        bd = bd.add(st);
                                    }

                                }

                                graphcolumns.add(NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                                rowviewlist.add("\"" + "SubTotal" + "\"");
                            }
                            if (graphColumns[k].toString().contains("GT")) {
                                bd = retobj.getColumnGrandTotalValue((String) graphlist.get(i));
                                graphcolumns.add(NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                                rowviewlist.add("\"GrandTotal\"");
                            }
                            if (graphColumns[k].toString().contains("AVG")) {
                                bd = retobj.getColumnAverageValue((String) graphlist.get(i));
                                graphcolumns.add(NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                                rowviewlist.add("\"Average\"");
                            }
                        }
                    }
                    graphcolumns1.add(graphcolumns);

                } else {
                    legends.clear();
                    for (int i = viewByColumns.length; i < tableColumns.size(); i++) {
                        BigDecimal bd = new BigDecimal(0);

                        if (graphDisplay.equalsIgnoreCase("AVG")) {
                            bd = retobj.getColumnAverageValue((String) tableColumns.get(i));
                            legends.add("\"" + "Average" + "\"");
                        } else if (graphDisplay.equalsIgnoreCase("GT")) {
                            bd = retobj.getColumnGrandTotalValue((String) tableColumns.get(i));
                            legends.add("\"" + "GrandTotal" + "\"");
                        } else if (graphDisplay.equalsIgnoreCase("ST")) {
                            for (int j = 0; j < Integer.parseInt(container.getPagesPerSlide().toString()); j++) {
                                if (j < actualrowcount && j < Integer.parseInt(container.getPagesPerSlide().toString())) {
                                    st = new BigDecimal(retobj.getFieldValueString(retobj.getViewSequence().get(j), (String) tableColumns.get(i)));
                                    bd = bd.add(st);
                                }

                            }
                            legends.add("\"" + "SubTotal" + "\"");
                        }
                        graphcolumns.add(NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                        if (retobj.getColumnGrandTotalValue((String) tableColumns.get(i)).toString().contains("-")) {
                            minvalue = -1;
                        }

                        rowviewlist.add("\"" + tableCollabel.get(i) + "\"");
                    }
                    graphcolumns1.add(graphcolumns);

                }
            } else {
                viewByColumns = (String[]) singleGraphDetails.get("viewByElementIds");
                int Maxsize = 0;
                ArrayList viewvalues = new ArrayList();
                ArrayList legendvalues = new ArrayList();
                LinkedHashMap<String, Double> twoRowValuelist = new LinkedHashMap<String, Double>();
                if (PrevbarChartColumnNames.length < 1) {
                    PrevbarChartColumnNames = (String[]) singleGraphDetails.get("barChartColumnNames");
                }
                // For two Row ViewBy sort
                if (!container.isReportCrosstab() && !graphTypes.iscolorGrouping && container.getSortColumns().contains(viewByColumns[0]) && viewByColumns.length >= 2 && !graphType.equalsIgnoreCase("Waterfall") && !graphType.equalsIgnoreCase("Waterfall(GT)")) {
//               tworowviewlist.clear();
                    ArrayList<Integer> sortList = new ArrayList<Integer>();
                    sortList = retobj.getViewSequence();
                    int sindex = container.getFromRow();
                    int eindex = 0;
                    eindex = graphrowcount;

                    String[] currDimValue = null;
                    int seqNo = -1;
                    for (int i = sindex; i < eindex && i < actualrowcount; i++) {
                        ArrayList tworowlist = new ArrayList();
                        if (viewByColumns.length < PrevbarChartColumnNames.length) {
                            int loopCount = 0;
                            for (int n = container.getFromRow(); n < sortList.size(); n++) {
                                if (seqNo == sortList.size()) {
                                    break;
                                }
                                if (seqNo == -1) {
                                    seqNo = n;
                                }
                                int actualRow = sortList.get(seqNo);

                                for (int j = 1; j <= viewByColumns.length - 1; j++) {
                                    if (!legendvalues.contains(retobj.getFieldValueString(actualRow, viewByColumns[j]))) {
                                        legendvalues.add(retobj.getFieldValueString(actualRow, viewByColumns[j]));
                                    }
                                }
                                String[] tempArray = new String[viewByColumns.length - 1];
                                for (int k = 0; k < viewByColumns.length - 1; k++) {
                                    tempArray[k] = retobj.getFieldValueString(actualRow, viewByColumns[k]);
                                }
                                if (currDimValue == null) {
                                    currDimValue = new String[tempArray.length];
                                    for (int l = 0; l < tempArray.length; l++) {
                                        currDimValue[l] = tempArray[l];
                                    }
                                }
                                boolean flag = compareStringArray(tempArray, currDimValue);
                                String value1 = "";
                                String viewValue = "";

                                if (flag) {

                                    for (int l = 0; l < tempArray.length; l++) {

                                        currDimValue[l] = tempArray[l];
                                    }
                                    loopCount = loopCount + 1;
                                    seqNo = seqNo + 1;
                                    if (loopCount > eindex) {
                                        break;
                                    }
                                } else {
                                    for (int l = 0; l < tempArray.length; l++) {
                                        currDimValue[l] = tempArray[l];

                                    }
                                    seqNo = seqNo;
                                    break;
                                }
                            }
                        }

                    }
                    sindex = container.getFromRow();
                    eindex = 0;
                    eindex = graphrowcount;

                    currDimValue = null;
                    seqNo = -1;
                    for (int i = sindex; i < eindex; i++) {
                        twoRowValuelist = new LinkedHashMap<String, Double>();
                        ArrayList tworowlist = new ArrayList();
                        if (viewByColumns.length < PrevbarChartColumnNames.length) {
                            int loopCount = 0;
                            for (int n = container.getFromRow(); n < sortList.size(); n++) {
                                if (seqNo == sortList.size()) {
                                    break;
                                }
                                if (seqNo == -1) {
                                    seqNo = n;
                                }
                                int actualRow = sortList.get(seqNo);


                                String[] tempArray = new String[viewByColumns.length - 1];
                                for (int k = 0; k < viewByColumns.length - 1; k++) {
                                    tempArray[k] = retobj.getFieldValueString(actualRow, viewByColumns[k]);
                                }
//            for(int j=1;j<= viewByColumns.length-1;j++){
//                if(!legendvalues.contains(retobj.getFieldValueString(actualRow, viewByColumns[j])))
//                legendvalues.add(retobj.getFieldValueString(actualRow, viewByColumns[j]));
//            }

//            for(int m=0;m<viewByColumns.length-1;m++){
//               value1="\""+retobj.getFieldValueString(actualRow, viewByColumns[m])+","+retobj.getFieldValueString(actualRow, viewByColumns[m+1])+"\"";
//                if(!tworowtooltip.contains(value1));
//                   tworowtooltip.add(value1);
//            }


                                if (currDimValue == null) {
                                    currDimValue = new String[tempArray.length];
                                    for (int l = 0; l < tempArray.length; l++) {
                                        currDimValue[l] = tempArray[l];
                                    }
                                }
                                boolean flag = compareStringArray(tempArray, currDimValue);
                                String value1 = "";
                                String viewValue = "";

                                if (flag) {
                                    if (tablecols != null && tablecols.length > 0) {
                                        PrevbarChartColumnNames[2] = tablecols[0];
                                    }
                                    BigDecimal bd = BigDecimal.ZERO;
                                    if (RTMeasureElement.isRunTimeMeasure(PrevbarChartColumnNames[viewByColumns.length])) {
                                        bd = retobj.getFieldValueRuntimeMeasure(actualRow, PrevbarChartColumnNames[viewByColumns.length]);
                                        temp = bd.toString();
                                    } else {
                                        temp = retobj.getFieldValueString(actualRow, PrevbarChartColumnNames[viewByColumns.length]) != null ? retobj.getFieldValueString(actualRow, PrevbarChartColumnNames[viewByColumns.length]) : "0";
                                    }
                                    bd = new BigDecimal(temp);
                                    temp = NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", "");
                                    //value = Double.valueOf(temp);
                                    value = new Double(temp);
                                    String viewby = getViewBy(retobj, viewByColumns, actualRow);
                                    value1 = viewby.substring(0, viewby.lastIndexOf(",")) + "," + viewby.substring(viewby.lastIndexOf(",") + 1, viewby.length());
                                    viewValue = viewby.substring(viewby.lastIndexOf(",") + 1, viewby.length());
                                    if (value1.contains("\"")) {
                                        tworowtooltip.add("\'" + value1 + "\'");
                                    } else {
                                        tworowtooltip.add("\"" + value1 + "\"");
                                    }
                                    twoRowValuelist.put(viewValue, value);
                                    tworowlist.add(value);
                                    for (int l = 0; l < tempArray.length; l++) {

                                        currDimValue[l] = tempArray[l];
                                        if (!viewvalues.contains(currDimValue[l])) {
                                            viewvalues.add(currDimValue[l]);
                                        }
                                    }
                                    loopCount = loopCount + 1;
                                    seqNo = seqNo + 1;
                                    if (loopCount > eindex) {
                                        break;
                                    }
                                } else {
                                    for (int l = 0; l < tempArray.length; l++) {
                                        currDimValue[l] = tempArray[l];

                                    }
                                    seqNo = seqNo;
                                    tworowlist = this.getTwoRowViewLists(twoRowValuelist, legendvalues);
                                    break;
                                }

                            }
                            if (Maxsize < tworowlist.size()) {
                                Maxsize = tworowlist.size();
                            }

                            list5.add(tworowlist);
                        }

                    }

                    if (list5 != null && !list5.isEmpty()) {

                        ArrayList duplist5 = (ArrayList) list5.clone();
                        list5.clear();

                        for (int i = 0; i < eindex && i < legendvalues.size(); i++) {
                            ArrayList tworowlist = new ArrayList();
                            for (int j = 0; j < duplist5.size(); j++) {
                                ArrayList innerlistvals = (ArrayList) duplist5.get(j);
                                if (innerlistvals.size() > i) {
                                    tworowlist.add(innerlistvals.get(i));
                                }
                            }
                            list5.add(tworowlist);
                        }
                        tworowviewlist.clear();

                        for (int i = 0; i < viewvalues.size(); i++) {
                            if (viewvalues.get(i).toString().contains("\"")) {
                                tworowviewlist.add("\'" + viewvalues.get(i) + "\'");
                            } else {
                                tworowviewlist.add("\"" + viewvalues.get(i) + "\"");
                            }
                        }
                        legends.clear();
                        for (int i = 0; i < legendvalues.size(); i++) {
                            if (legendvalues.get(i).toString().contains("\"")) {
                                legends.add("\'" + legendvalues.get(i) + "\'");
                            } else {
                                legends.add("\"" + legendvalues.get(i) + "\"");
                            }
                        }

                    }
                    graphTypes.data = list5;
                } // For Two and greater than TwoRowViewBy
                else if (!container.isReportCrosstab() && viewByColumns.length >= 2) {
                    if (tablecols != null && tablecols.length > 0) {
                        String[] duplicate = new String[tablecols.length + viewByColumns.length];
                        for (int i = 0; i < viewByColumns.length; i++) {
                            duplicate[i] = viewByColumns[i];
                        }
                        for (int i = 0; i < tablecols.length; i++) {
                            duplicate[i + viewByColumns.length] = tablecols[i];
                        }
                        PrevbarChartColumnNames = duplicate.clone();
                    }

                    int sindex = 0;
                    ArrayList<Integer> graphViewSeq = retobj.getViewSequence();
                    int eindex = PrevbarChartColumnNames.length - viewByColumns.length;
                    int ColumnSize = PrevbarChartColumnNames.length - viewByColumns.length;
                    ArrayList<Integer> sortList = new ArrayList<Integer>();
                    ArrayList<String> barChartColumnNames = new ArrayList<String>();
                    ArrayList<String> viewByNames = new ArrayList<String>();
                    for (int i = 0; i < PrevbarChartColumnNames.length; i++) {
                        barChartColumnNames.add(PrevbarChartColumnNames[i]);
                    }
                    for (int i = 0; i < viewByColumns.length; i++) {
                        viewByNames.add(viewByColumns[i]);
                    }
                    barChartColumnNames.removeAll(viewByNames);
                    sortList = retobj.getViewSequence();
                    String[] currDimValue = null;
                    for (index = sindex; index < eindex; index++) {
                        ArrayList tworowlist = new ArrayList();
                        int loopCount = container.getFromRow();

                        for (int n = container.getFromRow(); n < sortList.size(); n++) {
                            int actualRow = sortList.get(n);
                            String[] tempArray = new String[viewByColumns.length - 1];
                            for (int k = 0; k < viewByColumns.length - 1; k++) {
                                tempArray[k] = retobj.getFieldValueString(actualRow, viewByColumns[k]);
                            }
                            if (currDimValue == null) {
                                currDimValue = new String[tempArray.length];
                                for (int l = 0; l < tempArray.length; l++) {
                                    currDimValue[l] = tempArray[l];
                                }
                            }
                            BigDecimal bd = BigDecimal.ZERO;
                            if (RTMeasureElement.isRunTimeMeasure(barChartColumnNames.get(index))) {
                                bd = retobj.getFieldValueRuntimeMeasure(actualRow, barChartColumnNames.get(index));
                                temp = bd.toString();
                            } else {
                                temp = retobj.getFieldValueString(actualRow, barChartColumnNames.get(index)) != null ? retobj.getFieldValueString(actualRow, barChartColumnNames.get(index)) : "0";
                            }
                            bd = new BigDecimal(temp);
                            temp = NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", "");
                            if (temp.isEmpty() || temp.equalsIgnoreCase("")) {
                                temp = "0.0";
                            }
                            value = new Double(temp);
                            tworowlist.add(value);
                            loopCount = loopCount + 1;
                            if (loopCount >= graphrowcount) {
                                break;
                            }


                        }
                        Double othersval;
                        Double OG = 0.0;
                        if (graphTypes.viewOthers.equalsIgnoreCase("true")) {
                            for (int ss = 0; ss < graphrowcount; ss++) {
                                othersval = new Double(tworowlist.get(ss).toString());
                                OG = OG + othersval;

                            }
                            ArrayList<String> MeasureIds = container.getTableDisplayMeasures();
                            ArrayList<Object> othersData = container.getOthersData();
                            for (int s2 = 0; s2 < MeasureIds.size(); s2++) {
                                String Othermeasure = MeasureIds.get(s2);
                                if (graphlist.contains(Othermeasure)) {
                                    BigDecimal bdvalue = retobj.getColumnGrandTotalValue(MeasureIds.get(s2)).subtract(new BigDecimal(OG));
                                    tworowlist.add(bdvalue);
                                }
                            }
//             rowviewlist.add("\" others \"");
//               rowviewlistother.addAll(rowviewlist);
//         graphTypes.data = rowviewvaluelist;
                        }

                        list5.add(tworowlist);
                    }

                    if (graphTypes.iscolorGrouping && !graphType.equalsIgnoreCase("StackedBar(V)")) {
                        for (int i = 0; i < legends.size(); i++) {
                            if (list5.size() <= i) {
                                list5.add("[]");
                            }
                        }
                        if (graphlist.size() == 1) {
                            graphTypes.barpadding = -25;
                        }
                    }

                }
                // For Cross tab
                if (container.isReportCrosstab()) {
                    String s = null;
                    String[] crossTabMeas = null;
                    ArrayList elemArray = new ArrayList();
                    ArrayList finalColViewSortedValues = new ArrayList();
                    ArrayList tempmeasurecol = (ArrayList) TableHashMap.get("REPNames");
                    ArrayList tempmeasurecol2 = (ArrayList) TableHashMap.get("CEP");
                    ArrayList tempmeasurecol3 = (ArrayList) TableHashMap.get("Measures");
                    rowviewby = tempmeasurecol.get(0).toString();
                    columnviewby = tempmeasurecol2.get(0).toString();
                    HashMap map2 = container.getCrossTabGraphHashMap();
                    String measurecol = "A_" + tempmeasurecol.get(0);
                    String measurecol2 = "A_" + tempmeasurecol2.get(0);

                    singleGraphDetails = (HashMap) GraphHashMap.get(grpidfrmrep);
                    int sindex = 0;
                    int eindex = 5;
                    ArrayList<Integer> graphViewSeq = retobj.getViewSequence();
                    PrevbarChartColumnNames = (String[]) singleGraphDetails.get("barChartColumnNames");
                    if (displayCols != null && !displayCols.isEmpty() && !displayCols.equalsIgnoreCase("null")) {
                        if (displayCols.equalsIgnoreCase("All")) {
                            eindex = PrevbarChartColumnTitles.length - viewByColumns.length;
                        } else {
                            eindex = Integer.parseInt(displayCols);
                        }
                    }
                    if (PrevbarChartColumnTitles.length <= eindex) {
                        eindex = PrevbarChartColumnTitles.length - viewByColumns.length;
                    }
                    if (container.getGraphCrossTabMeas() != null && !container.getGraphCrossTabMeas().isEmpty()) {
                        crossTabMeas = container.getGraphCrossTabMeas().split(",");
                        for (int i = 0; i < crossTabMeas.length; i++) {
                            if (i >= summmeas.size()) {
                                elemArray.add(":" + crossTabMeas[i]);
                            } else {
                                elemArray.add("");
                            }
                        }
                    }
                    for (int j = viewByColumns.length; j < PrevbarChartColumnTitles.length && j <= eindex + viewByColumns.length; j++) {
                        lites.add(PrevbarChartColumnTitles[j]);
                    }
                    if (crossTabMeas != null) {
                        for (int i = 0; i < lites.size(); i++) {
                            for (int j = 0; j < crossTabMeas.length; j++) {
                                elemArray.add(":" + crossTabMeas[j]);
                            }


                        }
                    }

                    for (int k = 0; k < lites.size(); k++) {
                        if (crossTabMeas != null) {
                            if (lites.get(k).toString().contains("\"")) {
                                tooltip.add("\'" + lites.get(k) + elemArray.get(k) + "\'");
                            } else {
                                tooltip.add("\"" + lites.get(k) + elemArray.get(k) + "\"");
                            }
                            if (islegendAppend) {
                                if (lites.get(k).toString().contains("\"")) {
                                    lists1.add("\'" + lites.get(k) + elemArray.get(k) + "\'");
                                } else {
                                    lists1.add("\"" + lites.get(k) + elemArray.get(k) + "\"");
                                }
                            } else {
                                if (lites.get(k).toString().contains("\"")) {
                                    lists1.add("\'" + lites.get(k) + "\'");
                                } else {
                                    lists1.add("\"" + lites.get(k) + "\"");
                                }
                            }


                        } else {
                            if (islegendAppend) {
                                if (lites.get(k).toString().contains("\"")) {
                                    lists1.add("\'" + lites.get(k) + ":" + container.getMeasureName(tempmeasurecol3.get(0).toString()) + "\'");
                                } else {
                                    lists1.add("\"" + lites.get(k) + ":" + container.getMeasureName(tempmeasurecol3.get(0).toString()) + "\"");
                                }
                            } else {
                                if (lites.get(k).toString().contains("\"")) {
                                    lists1.add("\'" + lites.get(k) + "\'");
                                } else {
                                    lists1.add("\"" + lites.get(k) + "\"");
                                }
                            }
                            if (lites.get(k).toString().contains("\"")) {
                                tooltip.add("\'" + lites.get(k) + ":" + container.getMeasureName(tempmeasurecol3.get(0).toString()) + "\'");
                            } else {
                                tooltip.add("\"" + lites.get(k) + ":" + container.getMeasureName(tempmeasurecol3.get(0).toString()) + "\"");
                            }
                        }
                    }
                    graphTypes.tooltip = tooltip;
                    int ColumnSize = PrevbarChartColumnNames.length - viewByColumns.length;
                    ArrayList<Integer> seqlist = new ArrayList<Integer>();
                    ArrayList<Integer> graphseqlist = new ArrayList<Integer>();
                    ArrayList<Integer> graphseqlist1 = new ArrayList<Integer>();
                    for (int k = 0; k < graphlist.size(); k++) {
                        seqlist.add(k);
                    }
                    for (int l = 0; l < PrevbarChartColumnNames.length; l++) {
                        graphseqlist.add(l);
                    }
                    for (int m = 0; m < graphseqlist.size() - 1; m++) {
                        graphseqlist1.add(m);
                    }


                    drillmapLength = eindex;

                    if (istranpose) {
                        rowviewlist.clear();
                        lists1.clear();
                        for (int i = container.getFromRow(); i < graphrowcount; i++) {

                            ArrayList list4 = new ArrayList();
                            int actualRow = 0;
                            for (int index1 = sindex; index1 < eindex; index1++) {
                                temp = retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(index1));

                                value = new Double(temp);
                                if (isSupriseZero && value <= 0.0) {
                                    list4.add(null);
                                } else {
                                    list4.add(value);
                                }

                            }
                            list5.add(list4);
                        }
                        for (int k = 0; k < eindex; k++) {
                            if (lites.get(k).toString().contains("\"")) {
                                rowviewlist.add("\'" + lites.get(k) + "\'");
                            } else {
                                rowviewlist.add("\"" + lites.get(k) + "\"");
                            }
                        }
                        for (int i = container.getFromRow(); i < graphrowcount; i++) {
                            StringBuffer sb = new StringBuffer();
                            for (int j = 0; j < viewByColumns.length; j++) {
                                sb.append(retobj.getFieldValueString(retobj.getViewSequence().get(i), viewByColumns[j]));
                                if (j < viewByColumns.length - 1) {
                                    sb.append(",");
                                }
                            }
                            if (sb.toString().contains("\"")) {
                                lists1.add("\'" + sb.toString() + "\'");
                            } else {
                                lists1.add("\"" + sb.toString() + "\"");
                            }
                        }
                    } else {
                        for (int index1 = sindex; index1 < eindex; index1++) {
                            if (index1 < graphlist.size()) {
                                ArrayList list4 = new ArrayList();
                                int actualRow = 0;
                                for (int i = container.getFromRow(); i < graphrowcount; i++) {
                                    temp = retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(index1));
                                    if (temp.isEmpty()) {
                                        temp = "0";
                                    }
                                    value = new Double(temp);
                                    if (isSupriseZero && value <= 0.0) {
                                        list4.add(null);
                                    } else {
                                        list4.add(value);
                                    }

                                }
                                list5.add(list4);
                            }
                        }

                    }
                    if (container.getViewByCount() >= 2 && graphTypes.isappendXaxis) {
                        graphTypes.ticks = tworowviewlist;

                    } else {
                        graphTypes.ticks = rowviewlist;

                    }
                    numberRows = eindex / 5;
                    if (graphType.equalsIgnoreCase("Mekko")) {
                        if (list5 != null && !list5.isEmpty()) {

                            ArrayList duplist5 = (ArrayList) list5.clone();
                            list5.clear();

                            for (int i = 0; i < rowviewlist.size(); i++) {
                                ArrayList tworowlist = new ArrayList();
                                for (int j = 0; j < duplist5.size(); j++) {
                                    ArrayList innerlistvals = (ArrayList) duplist5.get(j);
                                    if (innerlistvals.size() > i) {
                                        tworowlist.add(innerlistvals.get(i));
                                    }
                                }
                                list5.add(tworowlist);
                            }
                        }
                    }
                }
                if (!container.getSortColumns().contains(viewByColumns[0])) {
                    if (container.getDataTypes().get(0).equals("D") && (graphType.equalsIgnoreCase("Line") || graphType.equalsIgnoreCase("Line(Smooth)") || graphType.equalsIgnoreCase("Line(Dashed)") || graphType.equalsIgnoreCase("Line(Simple)") || graphType.equalsIgnoreCase("Line(Simple-R)") || graphType.equalsIgnoreCase("Line(Std)") || graphType.equalsIgnoreCase("Dot-Graph"))) {
                        for (Object columnsize : graphlist) {
                            ArrayList dateData = new ArrayList();
                            for (int i = container.getFromRow(); i < graphrowcount; i++) {
                                dateData.add("[" + rowviewlist.get(i) + "," + retobj.getFieldValueString(retobj.getViewSequence().get(i), columnsize.toString()) + "]");
                            }
                            graphcolumns1.add(dateData);
                        }
                    } else {
                        if (istranpose) {
                            for (int i = container.getFromRow(); i < graphrowcount; i++) {
                                ArrayList graphcolumns = new ArrayList();
                                for (int j = 0; j < graphlist.size(); j++) {
                                    String MeasureValue = retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(j));
                                    if (MeasureValue == null || MeasureValue.isEmpty() || MeasureValue.equalsIgnoreCase("")) {
                                        MeasureValue = "0.0";
                                    }
                                    BigDecimal bd = BigDecimal.ZERO;
                                    if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(j))) {
                                        bd = retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(i), (String) graphlist.get(j));
                                    } else {
                                        bd = new BigDecimal(MeasureValue);
                                    }
                                    if (graphType.equalsIgnoreCase("DualAxis(Area-Line)") && NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", "").toString().equalsIgnoreCase("0")) {
                                        graphcolumns.add("null");
                                    } else {
                                        graphcolumns.add(NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));

                                    }

                                    if (MeasureValue.contains("-")) {
                                        minvalue = -1;
                                    }

                                }
                                graphcolumns1.add(graphcolumns);

                            }
                            ArrayList transposeticks = legends;
                            legends = (ArrayList) rowviewlist.clone();
                            rowviewlist = (ArrayList) transposeticks.clone();

                        } else {
                            for (int i = 0; i < graphlist.size(); i++) {
                                ArrayList graphcolumns = new ArrayList();
//                     for (int j = container.getFromRow(); j < graphrowcount; j++) {
                                for (int j = container.getFromRow(); j < retobj.getRowCount(); j++) {
                                    String MeasureValue = "0.0";
                                    BigDecimal bd = BigDecimal.ZERO;
                                    if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(i))) //                            bd=retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(j), (String) graphlist.get(i));
                                    {
                                        bd = retobj.getFieldValueRuntimeMeasure(j, (String) graphlist.get(i));
                                    } else {
//                                 MeasureValue=retobj.getFieldValueString(retobj.getViewSequence().get(j), (String) graphlist.get(i));
                                        MeasureValue = retobj.getFieldValueString(j, (String) graphlist.get(i));
                                        if (MeasureValue == null || MeasureValue.isEmpty() || MeasureValue.equalsIgnoreCase("")) {
                                            MeasureValue = "0.0";
                                        }
                                        bd = new BigDecimal(MeasureValue);
                                    }

                                    if (graphType.equalsIgnoreCase("DualAxis(Area-Line)") && NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", "").toString().equalsIgnoreCase("0")) {
                                        graphcolumns.add("null");
                                    } else {
                                        if (graphTypes.nbrFormat.equalsIgnoreCase("%")) {
                                            graphTypes.nbrFormat = "";
                                            graphcolumns.add(NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                                            graphTypes.nbrFormat = "%";
                                        } else {
                                            graphcolumns.add(NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                                        }

                                    }

                                    if (MeasureValue.contains("-")) {
                                        minvalue = -1;
                                    }

                                }
                                //  graphcolumns1.add(graphcolumns);
                                String othersval = ""; //SOF for others by kruthika
                                double OG = 0.0;
                                if (graphTypes.viewOthers.equalsIgnoreCase("true")) {
                                    for (int ss = 0; ss < graphrowcount; ss++) {
                                        othersval = graphcolumns.get(ss).toString();
                                        OG += Double.parseDouble(othersval);
                                    }

                                    BigDecimal bdvalue = retobj.getColumnGrandTotalValue((String) graphlist.get(i)).subtract(new BigDecimal(OG));
                                    //  String bdvalue1 = bdvalue.toString();
                                    bdvalue = bdvalue.setScale(2, BigDecimal.ROUND_FLOOR);
                                    String bdvalue1 = bdvalue.toString();
                                    graphcolumns.add(bdvalue1);




                                } // EOC for others by kruthika
                                graphcolumns1.add(graphcolumns);

                            }
                        }
                    }
                    if (graphTypes.viewOthers.equalsIgnoreCase("true")) { //soc by krk
                        rowviewlist.add("\" others \"");
                        rowviewlistother.addAll(rowviewlist);
                        graphTypes.data = rowviewvaluelist;
                    } //eoc by krk
                } else {

                    for (int i = 0; i < graphlist.size(); i++) {
                        ArrayList graphcolumns = new ArrayList();
                        for (int j = container.getFromRow(); j < graphrowcount; j++) {
                            String MeasureValue = retobj.getFieldValueString(retobj.getViewSequence().get(j), (String) graphlist.get(i));
                            if (MeasureValue == null || MeasureValue.isEmpty() || MeasureValue.equalsIgnoreCase("")) {
                                MeasureValue = "0.0";
                            }
                            BigDecimal bd = BigDecimal.ZERO;
                            if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(i))) {
                                bd = retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(j), (String) graphlist.get(i));
                            } else {
                                bd = new BigDecimal(MeasureValue);
                            }
                            graphcolumns.add(NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));

                            if (MeasureValue.contains("-")) {
                                minvalue = -1;
                            }

                        }
                        graphcolumns1.add(graphcolumns);

                    }

                }
                if (graphTypes.targetValue != null && !graphTypes.targetValue.isEmpty() && graphcolumns1.size() == 1 && graphType.equalsIgnoreCase("Bar-Vertical") && !container.getSortColumns().contains(viewByColumns[0])) {
                    ArrayList targetlist = new ArrayList();
                    ArrayList targetlist1 = new ArrayList();
                    ArrayList targetlist2 = new ArrayList();
                    String legend = (String) legends.get(0);
                    legends.clear();
                    legends.add("{label:" + legend + ",color:" + Seriescolors.get(0) + "}");
                    Seriescolors.clear();
                    targetlist2 = (ArrayList) graphcolumns1.get(0);
                    if (viewByColumns.length >= 2) {
                        targetlist2 = (ArrayList) list5.get(0);
                    }
                    for (int i = 0; i < targetlist2.size(); i++) {
                        if ((int) Double.valueOf(targetlist2.get(i).toString()).doubleValue() < Integer.parseInt(graphTypes.targetValue)) {
                            if (targetType.equalsIgnoreCase("standard")) {
                                Seriescolors.add("'#E41B17'");
                            } else {
                                Seriescolors.add("'#008000'");
                            }
                        } else {
                            if (targetType.equalsIgnoreCase("standard")) {
                                Seriescolors.add("'#008000'");
                            } else {
                                Seriescolors.add("'#E41B17'");
                            }
                        }
                    }
                    graphTypes.varyBarColor = true;
//                 legends.add(legends.get(0));

                }

            }
        }
        if ((graphType.equalsIgnoreCase("Pie") || graphType.equalsIgnoreCase("Pie-Empty") || graphType.equalsIgnoreCase("Funnel") || graphType.equalsIgnoreCase("Funnel(INV)")) && graphType != null) {

            if (container.isReportCrosstab()) {
                ArrayList tempmeasurecol = (ArrayList) TableHashMap.get("REPNames");
                ArrayList tempmeasurecol2 = (ArrayList) TableHashMap.get("CEP");
                rowviewby = tempmeasurecol.get(0).toString();
                columnviewby = tempmeasurecol2.get(0).toString();
                if (istranpose) {
                    graphTypes.legenddisplay = false;
                    for (int j = 0; j < graphrowcount; j++) {
                        temp = retobj.getFieldValueString(retobj.getViewSequence().get(j), (String) graphlist.get(rowindex));
                        value = new Double(temp);
                        if (retobj.getFieldValueString(retobj.getViewSequence().get(j), viewByColumns[0]).contains("\"")) {
                            pielist.add("[\'" + retobj.getFieldValueString(retobj.getViewSequence().get(j), viewByColumns[0]) + "\'");
                        } else {
                            pielist.add("[\"" + retobj.getFieldValueString(retobj.getViewSequence().get(j), viewByColumns[0]) + "\"");
                        }
                        if (legendDiv) {
                            pielist.add(0 + "]");
                        } else {
                            pielist.add(value + "]");
                        }
                        grpTitle = PrevbarChartColumnTitles[rowindex + 1];
                    }
                } else {
                    int eindex;
                    String[] crossTabMeas = null;
                    ArrayList elemArray = new ArrayList();
                    ArrayList tempmeasurecol3 = (ArrayList) TableHashMap.get("Measures");
                    if (PrevbarChartColumnTitles.length < 10) {
                        eindex = PrevbarChartColumnTitles.length;
                    } else {
                        eindex = 10;
                    }
                    if (displayCols != null && !displayCols.equalsIgnoreCase("null") && !displayCols.isEmpty() && !displayCols.equalsIgnoreCase("")) {
                        if (displayCols.equalsIgnoreCase("All") || PrevbarChartColumnTitles.length < Integer.parseInt(displayCols)) {
                            eindex = PrevbarChartColumnTitles.length;
                        } else {
                            eindex = Integer.parseInt(displayCols);
                        }
                    }
                    graphTypes.legenddisplay = false;
                    int ColumnSize = PrevbarChartColumnNames.length - viewByColumns.length;
                    int actualRow = retobj.getViewSequence().get(rowindex);
                    drillmapLength = viewByColumns.length + ColumnSize;
                    if (container.getGraphCrossTabMeas() != null && !container.getGraphCrossTabMeas().isEmpty()) {
                        crossTabMeas = container.getGraphCrossTabMeas().split(",");
                        for (int i = 0; i < crossTabMeas.length; i++) {
                            if (i >= summmeas.size()) {
                                elemArray.add(":" + crossTabMeas[i]);
                            } else {
                                elemArray.add("");
                            }
                        }
                    }
                    for (int j = viewByColumns.length; j < PrevbarChartColumnTitles.length && j <= (viewByColumns.length + ColumnSize); j++) {
                        lites.add(PrevbarChartColumnTitles[j]);
                    }
                    if (crossTabMeas != null) {
                        for (int i = 0; i < lites.size(); i++) {
                            for (int j = summmeas.size(); j < crossTabMeas.length; j++) {

                                elemArray.add(crossTabMeas[j]);
                            }
                        }
                    }

                    if (summmeas != null && !summmeas.isEmpty()) {
                        for (int j = 0; j < summmeas.size(); j++) {
                            if (!retobj.getFieldValueString(retobj.getViewSequence().get(rowindex), "A_" + summmeas.get(j).toString()).isEmpty()) {
                                value = new Double(retobj.getFieldValueString(retobj.getViewSequence().get(rowindex), "A_" + summmeas.get(j).toString()));
                            }
                            if (value > 0) {
                                if (summmeasTitle.get(j).toString().contains("\"")) {
                                    pielist.add("[\'" + summmeasTitle.get(j) + "\'");
                                } else {
                                    pielist.add("[\"" + summmeasTitle.get(j) + "\"");
                                }
                                pielist.add(value + "]");
                            }
                        }
                    }
                    for (int i = viewByColumns.length + summmeas.size(); i < (viewByColumns.length + ColumnSize) && i < eindex + 1; i++) {

                        if (i > summmeas.size()) {
                            value = new Double(retobj.getFieldValueString(actualRow, PrevbarChartColumnNames[i]) == null && "".equalsIgnoreCase(retobj.getFieldValueString(actualRow, PrevbarChartColumnNames[i])) ? "0" : retobj.getFieldValueString(actualRow, PrevbarChartColumnNames[i]));
                            if (value > 0) {
                                if (crossTabMeas != null) {
                                    if (islegendAppend) {
                                        if (PrevbarChartColumnTitles[i].contains("\"")) {
                                            pielist.add("[\'" + PrevbarChartColumnTitles[i] + ":" + elemArray.get(i + viewByColumns.length) + "\'");
                                        } else {
                                            pielist.add("[\"" + PrevbarChartColumnTitles[i] + ":" + elemArray.get(i + viewByColumns.length) + "\"");
                                        }
                                    } else {
                                        if (PrevbarChartColumnTitles[i].contains("\"")) {
                                            pielist.add("[\'" + PrevbarChartColumnTitles[i] + "\'");
                                        } else {
                                            pielist.add("[\"" + PrevbarChartColumnTitles[i] + "\"");
                                        }
                                    }
                                } else {
                                    if (islegendAppend) {
                                        if (PrevbarChartColumnTitles[i].toString().contains("\"")) {
                                            pielist.add("[\'" + PrevbarChartColumnTitles[i] + ":" + container.getMeasureName(tempmeasurecol3.get(0).toString()) + "\'");
                                        } else {
                                            pielist.add("[\"" + PrevbarChartColumnTitles[i] + ":" + container.getMeasureName(tempmeasurecol3.get(0).toString()) + "\"");
                                        }
                                    } else {
                                        if (PrevbarChartColumnTitles[i].toString().contains("\"")) {
                                            pielist.add("[\'" + PrevbarChartColumnTitles[i] + "\'");
                                        } else {
                                            pielist.add("[\"" + PrevbarChartColumnTitles[i] + "\"");
                                        }
                                    }

                                }
                                if (legendDiv) {
                                    pielist.add(0 + "]");
                                } else {
                                    pielist.add(value + "]");
                                }
                            }
                        }
                        for (int k = 0; k < viewByColumns.length; k++) {
                            if (!viewBy.contains(retobj.getFieldValueString(actualRow, viewByColumns[k]))) {
                                viewBy.add(retobj.getFieldValueString(actualRow, viewByColumns[k]));
                                grpTitle = retobj.getFieldValueString(actualRow, viewByColumns[k]);
                            }
                        }

                    }
                }


            } else {
                int max = Math.max(rowviewlist.size(), rowviewvaluelist.size());
                if (graphTypes.measureFormat.equalsIgnoreCase("ST")) {
                    BigDecimal bd = new BigDecimal(0);
                    bd = retobj.getColumnGrandTotalValue((String) graphlist.get(0));
                    // 
                    for (int i = 0; i < max; i++) {
                        if (i < rowviewvaluelist.size()) {
                            BigDecimal va = new BigDecimal(rowviewvaluelist.get(i).toString());
                            float res = (va.floatValue() / bd.floatValue()) * (100);
//                        BigDecimal te=(((va.multiply(new BigDecimal(100))).divide(bd)));
                            pielist.add("[" + "\"" + NumberFormatter.getModifiedNumberFormat(new BigDecimal(res), "%", graphTypes.measureRound) + "\"");
                        }
                        if (i < rowviewvaluelist.size()) {
                            pielist.add(rowviewvaluelist.get(i) + "]");
                        }
                        if (i < rowviewlist.size()) {
                            tworowviewlist.add(rowviewlist.get(i));
                        }
                    }

                } else {
                    for (int i = 0; i < max; i++) {
                        if (i < rowviewlist.size()) {
                            pielist.add("[" + rowviewlist.get(i));
                        }
                        if (i < rowviewvaluelist.size()) {
                            pielist.add(rowviewvaluelist.get(i) + "]");
                        }
                    }
                }
                numberRows = (rowviewlist.size()) / 5;
            }
        }
        if ((graphType.equalsIgnoreCase("Donut-Double") || graphType.equalsIgnoreCase("Donut-Single")) && graphType != null) {
            int max = Math.max(rowviewlist.size(), rowviewvaluelist.size());
            for (int i = 0; i < max; i++) {
                if (i < rowviewlist.size()) {
                    donutrowlist.add("[" + rowviewlist.get(i));
                }
                if (i < rowviewvaluelist.size()) {
                    donutrowlist.add(rowviewvaluelist.get(i) + "]");
                }
                if (valuelist1 != null && !valuelist1.isEmpty()) {
                    if (i < rowviewlist.size()) {
                        donutrowlist1.add("[" + rowviewlist.get(i));
                    }
                    if (i < rowviewvaluelist.size()) {
                        donutrowlist1.add(valuelist1.get(i) + "]");
                    }
                }

            }
            numberRows = (rowviewlist.size()) / 5;
        }

        if (graphType.equalsIgnoreCase("Bubble") || graphType.equalsIgnoreCase("Bubble(log)")) {

            if (graphlist.size() >= 3) {
                if (graphTypes.showxAxis == null || graphTypes.showxAxis.equalsIgnoreCase("null") || graphTypes.showxAxis.equalsIgnoreCase(" ") || graphTypes.showxAxis.isEmpty()) {
                    graphTypes.showxAxis = legends.get(0).toString().replace("\"", "");
                    graphTypes.showlyAxis = legends.get(1).toString().replace("\"", "");
                }
                for (int i = container.getFromRow(); i < graphrowcount; i++) {
                    String MeasureValue1 = "0.0";
                    String MeasureValue2 = "0.0";
                    String MeasureValue3 = "0.0";
                    BigDecimal bd = BigDecimal.ZERO;
                    if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(0))) {
                        bd = retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(i), (String) graphlist.get(0));
                    } else {
                        MeasureValue1 = retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(0));
                        if (MeasureValue1 == null || MeasureValue1.isEmpty() || MeasureValue1.equalsIgnoreCase("")) {
                            MeasureValue1 = "0.0";
                        }
                        if (graphType.equalsIgnoreCase("Bubble(log)") && Math.log(Double.parseDouble(MeasureValue1)) != Double.POSITIVE_INFINITY && Math.log(Double.parseDouble(MeasureValue1)) != Double.NEGATIVE_INFINITY && Math.log(Double.parseDouble(MeasureValue1)) != Double.NaN) {
                            bd = new BigDecimal(Math.log(Double.parseDouble(MeasureValue1)));
                        } else {
                            bd = new BigDecimal(MeasureValue1);
                        }
                    }
                    bubblelist.add("[" + NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                    if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(0))) {
                        minvalue = 0;
                    } else {
                        if (MeasureValue1.contains("-")) {
                            minvalue = -1;
                        }
                    }
                    BigDecimal bd1 = BigDecimal.ZERO;
                    if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(1))) {
                        bd1 = retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(i), (String) graphlist.get(1));
                    } else {
                        MeasureValue2 = retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(1));
                        if (MeasureValue2 == null || MeasureValue2.isEmpty() || MeasureValue2.equalsIgnoreCase("")) {
                            MeasureValue2 = "0.0";
                        }
                        if (graphType.equalsIgnoreCase("Bubble(log)") && Math.log(Double.parseDouble(MeasureValue2)) != Double.POSITIVE_INFINITY && Math.log(Double.parseDouble(MeasureValue2)) != Double.NEGATIVE_INFINITY && Math.log(Double.parseDouble(MeasureValue2)) != Double.NaN) {
                            bd1 = new BigDecimal(Math.log(Double.parseDouble(MeasureValue2)));
                        } else {
                            bd1 = new BigDecimal(MeasureValue2);
                        }
                    }
                    bubblelist.add(NumberFormatter.getModifiedNumberFormat(bd1, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                    if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(1))) {
                        minvalue = 0;
                    } else {
                        if (MeasureValue2.contains("-")) {
                            minvalue = -1;
                        }
                    }
                    BigDecimal bd2 = BigDecimal.ZERO;
                    if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(2))) {
                        bd2 = retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(i), (String) graphlist.get(2));
                    } else {
                        MeasureValue3 = retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(2));
                        if (MeasureValue3 == null || MeasureValue3.isEmpty() || MeasureValue3.equalsIgnoreCase("")) {
                            MeasureValue3 = "0.0";
                        }
                        if (graphType.equalsIgnoreCase("Bubble(log)") && Math.log(Double.parseDouble(MeasureValue3)) != Double.POSITIVE_INFINITY && Math.log(Double.parseDouble(MeasureValue3)) != Double.NEGATIVE_INFINITY && Math.log(Double.parseDouble(MeasureValue3)) != Double.NaN) {
                            bd2 = new BigDecimal(Math.log(Double.parseDouble(MeasureValue3)));
                        }
                        bd2 = new BigDecimal(MeasureValue3);
                    }
                    bubblelist.add(NumberFormatter.getModifiedNumberFormat(bd2, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                    if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(2))) {
                        minvalue = 0;
                    } else {
                        if (MeasureValue3.contains("-")) {
                            minvalue = -1;
                        }
                    }
                    bubblelist.add(rowviewlist.get(i - container.getFromRow()) + "]");
                }

            }
        }
        if (graphType.equalsIgnoreCase("Scatter") || graphType.equalsIgnoreCase("Scatter(Regression)")) {
            if (graphTypes.showxAxis == null || graphTypes.showxAxis.equalsIgnoreCase("null") || graphTypes.showxAxis.equalsIgnoreCase(" ") || graphTypes.showxAxis.isEmpty()) {
                if (legends.size() > 1) {
                    graphTypes.showxAxis = legends.get(0).toString().replace("\"", "");
                    graphTypes.showlyAxis = legends.get(1).toString().replace("\"", "");
                } else if (legends.size() == 1) {
                    graphTypes.showlyAxis = legends.get(0).toString().replace("\"", "");
                }
            }
            if (graphlist.size() >= 1) {
                for (int i = 0; i < retobj.getViewSequence().size(); i++) {
                    String MeasureValue1 = "0.0";
                    String MeasureValue2 = "0.0";
                    if (graphlist.size() > 1) {

                        BigDecimal bd = BigDecimal.ZERO;
                        if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(0))) {
                            bd = retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(i), (String) graphlist.get(0));
                        } else {
                            MeasureValue1 = retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(0));
                            if (MeasureValue1 == null || MeasureValue1.isEmpty() || MeasureValue1.equalsIgnoreCase("")) {
                                MeasureValue1 = "0.0";
                            }

                            bd = new BigDecimal(MeasureValue1);
                        }
                        scatterlist.add("[" + NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                        if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(0))) {
                            minvalue = 0;
                        } else {
                            if (MeasureValue1.contains("-")) {
                                minvalue = -1;
                            } else {
                                minvalue = 0;
                            }
                        }
                        BigDecimal bd1 = BigDecimal.ZERO;
                        if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(1))) {
                            bd1 = retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(i), (String) graphlist.get(1));
                        } else {
                            MeasureValue2 = retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(1));
                            if (MeasureValue2 == null || MeasureValue2.isEmpty() || MeasureValue2.equalsIgnoreCase("")) {
                                MeasureValue2 = "0.0";
                            }
                            bd1 = new BigDecimal(MeasureValue2);
                        }
                        scatterlist.add(NumberFormatter.getModifiedNumberFormat(bd1, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                        if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(1))) {
                            minvalue = 0;
                        } else {
                            if (MeasureValue2.contains("-")) {
                                minvalue = -1;
                            } else {
                                minvalue = 0;
                            }
                        }
                        if (viewByColumns.length >= 2 && i < tworowviewlist.size()) {
                            scatterlist.add(tworowviewlist.get(i) + "]");
                        } else {
                            if (container.getDataTypes().equals("D")) {
                                scatterlist.add("\"" + retobj.getFieldValueDateString(retobj.getViewSequence().get(i), (String) orginalcolumns.get(0)) + "\"]");
                            } else {
                                scatterlist.add("\"" + retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) orginalcolumns.get(0)) + "\"]");
                            }
                        }
                    } else if (graphlist.size() == 1) {
                        BigDecimal bd = BigDecimal.ZERO;
                        if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(0))) {
                            bd = retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(i), (String) graphlist.get(0));
                        } else {
                            MeasureValue1 = retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(0));
                            if (MeasureValue1 == null || MeasureValue1.isEmpty() || MeasureValue1.equalsIgnoreCase("")) {
                                MeasureValue1 = "0.0";
                            }
                            bd = new BigDecimal(MeasureValue1);

                        }
                        scatterlist.add(NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                        if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(0))) {
                            minvalue = 0;
                        } else {
                            if (MeasureValue1.contains("-")) {
                                minvalue = -1;
                            } else {
                                minvalue = 0;
                            }
                        }
                    }
                }

            }
        }
        if (graphType.equalsIgnoreCase("StackedBar(Percent)") || graphType.equalsIgnoreCase("StackedH(Percent)")) {
            graphTypes.nbrFormat = "%";
            Double[] totalList = new Double[rowviewlist.size()];
            for (int k = 0; k < totalList.length; k++) {
                totalList[k] = 0.0;
            }
            for (int i = 0; i < graphlist.size(); i++) {

                for (int j = container.getFromRow(); j < graphrowcount; j++) {
                    BigDecimal bd = BigDecimal.ZERO;
                    if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(i))) {
                        if ((retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(j), (String) graphlist.get(i))).toString() != null) {
                            bd = retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(j), (String) graphlist.get(i));
                            totalList[j] = totalList[j] + Double.parseDouble(bd.toString());
                        } else {
                            totalList[j] = totalList[j] + 0.0;
                        }
                    } else {
                        if (retobj.getFieldValueString(retobj.getViewSequence().get(j), (String) graphlist.get(i)) != null) {
                            totalList[j] = totalList[j] + Double.parseDouble(retobj.getFieldValueString(retobj.getViewSequence().get(j), (String) graphlist.get(i)));
                        } else {
                            totalList[j] = totalList[j] + 0.0;
                        }
                    }
//                            else
//                            totalList[j]=totalList[j]+0.0;

                }
            }
            for (int i = 0; i < graphlist.size(); i++) {
                ArrayList graphcolumns = new ArrayList();
                for (int j = container.getFromRow(); j < graphrowcount; j++) {
                    double value12 = 0.0;
                    BigDecimal bd = BigDecimal.ZERO;
                    if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(i))) {
                        if ((retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(j), (String) graphlist.get(i))).toString() != null) {
                            bd = retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(j), (String) graphlist.get(i));
                            value12 = Double.parseDouble(bd.toString());
                        } else {
                            value12 = 0.0;
                        }
                    } else {
                        if (retobj.getFieldValueString(retobj.getViewSequence().get(j), (String) graphlist.get(i)) != null) {
                            value12 = Double.parseDouble(retobj.getFieldValueString(retobj.getViewSequence().get(j), (String) graphlist.get(i)));
                        } else {
                            value12 = 0.0;
                        }
                    }
                    graphcolumns.add((value12 / totalList[j]) * 100);
                    if (((value12 / totalList[j]) * 100) < 0) {
                        minvalue = -1;
                    } else {
                        minvalue = 0;
                    }

                }
                graphcolumns1.add(graphcolumns);

            }
        }
        // Foe graph type equals to Scatter(Partial)
        if (graphType.equalsIgnoreCase("Scatter(Partial)") || graphType.equalsIgnoreCase("Block")) {
            if (graphTypes.showxAxis == null || graphTypes.showxAxis.equalsIgnoreCase("null") || graphTypes.showxAxis.equalsIgnoreCase(" ") || graphTypes.showxAxis.isEmpty()) {
                if (legends.size() > 1) {
                    graphTypes.showxAxis = legends.get(0).toString().replace("\"", "");
                    graphTypes.showlyAxis = legends.get(1).toString().replace("\"", "");
                } else if (legends.size() == 1) {
                    graphTypes.showlyAxis = legends.get(0).toString().replace("\"", "");
                }
            }
            if (graphlist.size() >= 1) {
                for (int i = container.getFromRow(); i < graphrowcount; i++) {
                    if (graphlist.size() > 1) {
                        String MeasureValue1 = "0.0";
                        String MeasureValue2 = "0.0";
                        BigDecimal bd = BigDecimal.ZERO;
                        if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(0))) {
                            bd = retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(i), (String) graphlist.get(0));
                        } else {
                            MeasureValue1 = retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(0));
                            if (MeasureValue1 == null || MeasureValue1.isEmpty() || MeasureValue1.equalsIgnoreCase("")) {
                                MeasureValue1 = "0.0";
                            }
                            bd = new BigDecimal(MeasureValue1);
                        }
                        scatterlist.add("[" + NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                        if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(0))) {
                            minvalue = 0;
                        }
                        if (retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(0)).contains("-")) {
                            minvalue = -1;
                        } else {
                            minvalue = 0;
                        }
                        BigDecimal bd1 = BigDecimal.ZERO;
                        if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(1))) {
                            bd1 = retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(i), (String) graphlist.get(1));
                        } else {
                            MeasureValue2 = retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(1));
                            if (MeasureValue2 == null || MeasureValue2.isEmpty() || MeasureValue2.equalsIgnoreCase("")) {
                                MeasureValue2 = "0.0";
                            }
                            bd1 = new BigDecimal(MeasureValue2);
                        }
                        scatterlist.add(NumberFormatter.getModifiedNumberFormat(bd1, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                        if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(1))) {
                            minvalue = 0;
                        } else {
                            if (retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(1)).contains("-")) {
                                minvalue = -1;
                            } else {
                                minvalue = 0;
                            }
                        }
                        if (viewByColumns.length >= 2) {
                            scatterlist.add(tworowviewlist.get(i) + "]");
                        } else {
                            if (container.getDataTypes().equals("D")) {
                                scatterlist.add("\"" + retobj.getFieldValueDateString(retobj.getViewSequence().get(i), (String) orginalcolumns.get(0)) + "\"]");
                            } else {
                                scatterlist.add("\"" + retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) orginalcolumns.get(0)) + "\"]");
                            }
                        }
                    } else if (graphlist.size() == 1) {
                        String MeasureValue = "0.0";
                        BigDecimal bd = BigDecimal.ZERO;
                        if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(0))) {
                            bd = retobj.getFieldValueRuntimeMeasure(retobj.getViewSequence().get(i), (String) graphlist.get(0));
                        } else {
                            MeasureValue = retobj.getFieldValueString(retobj.getViewSequence().get(i), (String) graphlist.get(0));
                            if (MeasureValue == null || MeasureValue.isEmpty() || MeasureValue.equalsIgnoreCase("")) {
                                MeasureValue = "0.0";
                            }
                            bd = new BigDecimal(MeasureValue);
                        }
                        scatterlist.add(NumberFormatter.getModifiedNumberFormat(bd, graphTypes.nbrFormat, graphTypes.measureRound).replaceAll(",", ""));
                        if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(0))) {
                            minvalue = 0;
                        } else {
                            if (MeasureValue.contains("-")) {
                                minvalue = -1;
                            } else {
                                minvalue = 0;
                            }
                        }

                    }
                }

            }
        }
        // For Columnpie Graph
        if (graphType.equalsIgnoreCase("ColumnPie")) {
            String[] crossTabMeas = null;
            ArrayList elemArray = new ArrayList();
            ArrayList tempmeasurecol = (ArrayList) TableHashMap.get("REPNames");
            ArrayList tempmeasurecol2 = (ArrayList) TableHashMap.get("CEP");
            rowviewby = tempmeasurecol.get(0).toString();
            if (tempmeasurecol2 != null && !tempmeasurecol2.isEmpty()) {
                columnviewby = tempmeasurecol2.get(0).toString();
            }
            int ColumnSize = PrevbarChartColumnNames.length - viewByColumns.length;
            int actualRow = retobj.getViewSequence().get(rowindex);
            drillmapLength = viewByColumns.length + ColumnSize;
            if (container.getGraphCrossTabMeas() != null && !container.getGraphCrossTabMeas().isEmpty()) {
                crossTabMeas = container.getGraphCrossTabMeas().split(",");
                for (int i = 0; i < crossTabMeas.length; i++) {
                    if (i >= summmeas.size()) {
                        elemArray.add(":" + crossTabMeas[i]);
                    } else {
                        elemArray.add("");
                    }
                }
            }
            for (int j = viewByColumns.length; j < PrevbarChartColumnTitles.length && j <= (viewByColumns.length + ColumnSize); j++) {
                lites.add(PrevbarChartColumnTitles[j]);
            }
            if (crossTabMeas != null) {
                for (int i = 0; i < lites.size(); i++) {
                    for (int j = summmeas.size(); j < crossTabMeas.length; j++) {

                        elemArray.add(crossTabMeas[j]);
                    }
                }
            }
            if (summmeas != null && !summmeas.isEmpty()) {
                for (int j = 0; j < summmeas.size(); j++) {
                    if (!retobj.getFieldValueString(retobj.getViewSequence().get(rowindex), "A_" + summmeas.get(j).toString()).isEmpty()) {
                        value = new Double(retobj.getFieldValueString(retobj.getViewSequence().get(rowindex), "A_" + summmeas.get(j).toString()));
                    }
                    if (value > 0) {
                        if (summmeasTitle.get(j).toString().contains("\"")) {
                            columnpieDataSet.add("[\'" + summmeasTitle.get(j) + "\'");
                        } else {
                            columnpieDataSet.add("[\"" + summmeasTitle.get(j) + "\"");
                        }
                        columnpieDataSet.add(value + "]");
                    }
                }
            }
            for (int i = viewByColumns.length + summmeas.size(); i < (viewByColumns.length + ColumnSize); i++) {
                BigDecimal bd = BigDecimal.ZERO;
                if (RTMeasureElement.isRunTimeMeasure(PrevbarChartColumnNames[i])) {
                    bd = retobj.getFieldValueRuntimeMeasure(actualRow, PrevbarChartColumnNames[i]);
                    value = Double.valueOf(bd.toString());
                } else {
                    if (i > summmeas.size()) {
                        value = new Double(retobj.getFieldValueString(actualRow, PrevbarChartColumnNames[i]) == null && "".equalsIgnoreCase(retobj.getFieldValueString(actualRow, PrevbarChartColumnNames[i])) ? "0" : retobj.getFieldValueString(actualRow, PrevbarChartColumnNames[i]));

                        if (value > 0) {
                            if (crossTabMeas != null) {
                                if (islegendAppend) {
                                    if (PrevbarChartColumnTitles[i].contains("\"")) {
                                        columnpieDataSet.add("[\'" + PrevbarChartColumnTitles[i] + ":" + elemArray.get(i + viewByColumns.length) + "\'");
                                    }
                                    columnpieDataSet.add("[\"" + PrevbarChartColumnTitles[i] + ":" + elemArray.get(i + viewByColumns.length) + "\"");
                                } else {
                                    if (PrevbarChartColumnTitles[i].contains("\"")) {
                                        columnpieDataSet.add("[\'" + PrevbarChartColumnTitles[i] + "\'");
                                    } else {
                                        columnpieDataSet.add("[\"" + PrevbarChartColumnTitles[i] + "\"");
                                    }
                                }
                            } else {
                                if (PrevbarChartColumnTitles[i].contains("\"")) {
                                    columnpieDataSet.add("[\'" + PrevbarChartColumnTitles[i] + "\'");
                                } else {
                                    columnpieDataSet.add("[\"" + PrevbarChartColumnTitles[i] + "\"");
                                }
                            }

                            columnpieDataSet.add(value + "]");
                        }
                    }
                    for (int k = 0; k < viewByColumns.length; k++) {
                        if (!viewBy.contains(retobj.getFieldValueString(actualRow, viewByColumns[k]))) {
                            viewBy.add(retobj.getFieldValueString(actualRow, viewByColumns[k]));
                        }
                    }
                }
            }

        }

        ArrayList drillmap = new ArrayList();
        if (container.isReportCrosstab()) {
            graphlist.clear();
            ArrayList tempmeasurecol3 = (ArrayList) TableHashMap.get("Measures");
            for (int i = 0; i < tempmeasurecol3.size(); i++) {
                graphlist.add(tempmeasurecol3.get(i));
            }
        }
        if (summmeas != null && !summmeas.isEmpty()) {
            for (int i = 0; i < summmeas.size(); i++) {
                String drillRepId = container.getReportDrillMap("A_" + summmeas.get(i).toString());
                if (drillRepId != null) {
                    drillmap.add(drillRepId);
                } else {
                    drillmap.add("null");
                }

            }

        }
        if (graphType.equalsIgnoreCase("pie")) {
            for (int j = 0; j < drillmapLength; j++) {
                String drillRepId = container.getReportDrillMap(graphlist.get(0).toString());
                if (drillRepId != null) {
                    drillmap.add(drillRepId);
                } else {
                    drillmap.add("null");
                }
            }
        } else if (container.isReportCrosstab()) {
            for (int j = 0; j < drillmapLength; j++) {
                for (int i = 0; i < graphlist.size(); i++) {
                    String drillRepId = container.getReportDrillMap(graphlist.get(i).toString());
                    if (drillRepId != null) {
                        drillmap.add(drillRepId);
                    } else {
                        drillmap.add("null");
                    }
                }
            }
        } else {
            for (int i = 0; i < graphlist.size(); i++) {
                String drillRepId = container.getReportDrillMap(graphlist.get(i).toString());
                if (drillRepId != null) {
                    drillmap.add(drillRepId);
                } else {
                    drillmap.add("null");
                }
            }
        }
        //
        if (graphType.equalsIgnoreCase("Overlaid(Bar-Line)") || graphType.equalsIgnoreCase("DualAxis(Bar-Line)")) {
            if (singleGraphDetails.get("barChartColumnNames1") != null && !singleGraphDetails.get("barChartColumnNames1").toString().isEmpty()) {
                String[] overlaidaxis = (String[]) singleGraphDetails.get("barChartColumnNames1");
                for (int i = viewByColumns.length; i < overlaidaxis.length; i++) {
                    if (graphTypes.graphtype1.equalsIgnoreCase("Bar") || graphTypes.graphtype1.isEmpty()) {
                        overlaid.add("{disableStack : true,renderer:$.jqplot.BarRenderer}");
                    } else if (graphTypes.graphtype1.equalsIgnoreCase("Area")) {
                        overlaid.add("{disableStack : true,fill:true}");
                    } else if (graphTypes.graphtype1.equalsIgnoreCase("Stackedbar")) {
                        overlaid.add("{renderer:$.jqplot.BarRenderer}");
                    } else if (graphTypes.graphtype1.equalsIgnoreCase("StackedArea")) {
                        overlaid.add("{fill:true}");
                    } else if (graphTypes.graphtype1.equalsIgnoreCase("Line")) {
                        overlaid.add("{disableStack : true,renderer:$.jqplot.LineRenderer,lineWidth:2}");
                    } //added by shravani
                    else if (graphTypes.graphtype1.equalsIgnoreCase("Dot-Graph")) {
                        overlaid.add("{disableStack : true,renderer:$.jqplot.LineRenderer,showLine:false, markerOptions: { shadow: false,  style:\"filledDiamond\" }}");
                    }
                    //end of code
                }
            }
            if (singleGraphDetails.get("barChartColumnNames2") != null && !singleGraphDetails.get("barChartColumnNames2").toString().isEmpty()) {
                String[] overlaidaxis = (String[]) singleGraphDetails.get("barChartColumnNames2");
                for (int i = viewByColumns.length; i <= overlaidaxis.length; i++) {
                    if (graphType != null && graphType.equalsIgnoreCase("Overlaid(Bar-Line)")) {
                        if (graphTypes.graphtype2.equalsIgnoreCase("Bar") || graphTypes.graphtype1.isEmpty()) {
                            overlaid.add("{disableStack : true,renderer:$.jqplot.BarRenderer}");
                        } else if (graphTypes.graphtype2.equalsIgnoreCase("Area")) {
                            overlaid.add("{disableStack : true,fill:true}");
                        } else if (graphTypes.graphtype2.equalsIgnoreCase("Stackedbar")) {
                            overlaid.add("{renderer:$.jqplot.BarRenderer}");
                        } else if (graphTypes.graphtype2.equalsIgnoreCase("StackedArea")) {
                            overlaid.add("{fill:true}");
                        } else if (graphTypes.graphtype2.equalsIgnoreCase("Line")) {
                            overlaid.add("{disableStack : true,renderer:$.jqplot.LineRenderer,lineWidth:2}");
                        }
                    } else if (graphType != null && graphType.equalsIgnoreCase("DualAxis(Bar-Line)")) {
                        if (graphTypes.graphtype2.equalsIgnoreCase("Bar") || graphTypes.graphtype1.isEmpty()) {
                            overlaid.add("{disableStack : true,renderer:$.jqplot.BarRenderer,yaxis:'y2axis'}");
                        } else if (graphTypes.graphtype2.equalsIgnoreCase("Area")) {
                            overlaid.add("{disableStack : true,fill:true,yaxis:'y2axis'}");
                        } else if (graphTypes.graphtype2.equalsIgnoreCase("Stackedbar")) {
                            overlaid.add("{renderer:$.jqplot.BarRenderer,yaxis:'y2axis'}");
                        } else if (graphTypes.graphtype2.equalsIgnoreCase("StackedArea")) {
                            overlaid.add("{fill:true,yaxis:'y2axis'}");
                        } else if (graphTypes.graphtype2.equalsIgnoreCase("Line")) {
                            overlaid.add("{disableStack : true,renderer:$.jqplot.LineRenderer,lineWidth:2,yaxis:'y2axis'}");
                        } //added by shravani
                        else if (graphTypes.graphtype2.equalsIgnoreCase("Dot-Graph")) {
                            overlaid.add("{disableStack : true,renderer:$.jqplot.LineRenderer,showLine:false, markerOptions: { shadow: false,  style:\"filledDiamond\" },yaxis:'y2axis'}");
                        }
                        //end of code
                    }
                }
            }
            overlaid.add("{label:'',}");

        }
        if (graphType.equalsIgnoreCase("DualAxis(Area-Line)")) {
            overlaid.add("{fill:true}");
            for (int i = 1; i < columnnames.size(); i++) {
                overlaid.add("{disableStack : true,renderer:$.jqplot.LineRenderer,lineWidth:2}");
            }
        }
        if (graphType.equalsIgnoreCase("Overlaid(Bar-Dot)")) {
            overlaid.add("{disableStack : true,renderer:$.jqplot.BarRenderer}");
            for (int i = 1; i < graphlist.size(); i++) {
                overlaid.add("{disableStack : true,renderer:$.jqplot.LineRenderer,showLine:false, markerOptions: { shadow: false,  style:\"filledDiamond\" }}");
            }
        }
//if (graphTypes.viewOthers.equalsIgnoreCase("true")) { //dualothers ///commented by kruthika for others
//    graphcolumns1.clear();
//    graphcolumns1.add(rowviewvaluelist);
//    rowviewlist.clear();
//    rowviewlist.addAll(rowviewlistother);
////    tworowviewlist.add("\" others \"");
//}
        if (graphType.equalsIgnoreCase("Waterfall") || graphType.equalsIgnoreCase("Waterfall(GT)")) {
            if (viewByColumns.length >= 2 && !container.isReportCrosstab()) {
                graphTypes.data = (ArrayList) list5.get(0);

            }
            if (viewByColumns.length < 2) {
                graphTypes.data = rowviewvaluelist;
            }
        }
        if (container.isReportCrosstab()) {

            if (retobj.getViewSequence().size() <= 1) {
                if (graphType.equalsIgnoreCase("Waterfall") || graphType.equalsIgnoreCase("Waterfall(GT)")) {
                } else {
                    graphTypes.data = graphcolumns1;
                }
            } else {
                if (graphType.equalsIgnoreCase("Waterfall") || graphType.equalsIgnoreCase("Waterfall(GT)")) {
                } else {
                    graphTypes.data = list5;
                }
            }
            graphTypes.legendlabels = lists1;
        } else {
            if (viewByColumns.length >= 2) {
                graphTypes.tooltip = tworowtooltip;
                if (graphTypes.viewOthers.equalsIgnoreCase("true")) {
                    tworowviewlist.add("\" others \"");
                }
                graphTypes.ticks = tworowviewlist;

                graphTypes.data = list5;
            } else {

                graphTypes.ticks = rowviewlist;


                graphTypes.data = graphcolumns1;
            }
            graphTypes.legendlabels = legends;
        }

        if ((viewByColumns.length >= 2 && graphTypes.iscolorGrouping) && graphType.equalsIgnoreCase("Bar-vertical")) {
            graphTypes.seriesColors = groupSeries;
        } else {
            graphTypes.seriesColors = Seriescolors;
        }

        if (!isFromOneView) {
            graphTypes.title = grpTitle;
        }
        ArrayList<String> MeasureIds = container.getTableDisplayMeasures();
        ArrayList<String> MeasureNames = container.getReportMeasureNames();
        String measId = "";
        for (int i = 0; i < MeasureIds.size(); i++) {
            measId = MeasureIds.get(i);
        }
        if ((graphTypes.Flag).equalsIgnoreCase("true")) {
            if (RTMeasureElement.isRunTimeMeasure(measId)) {

                if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(0))) {
                    if ((graphTypes.ischeckedLA).equalsIgnoreCase("true")) {
//                 graphTypes.nbrFormat="%";
                        graphTypes.ischeckedLA = "%";
                        if ((graphTypes.ischeckedRA).equalsIgnoreCase("true")) {
                            graphTypes.ischeckedRA = "%";
                        } else {

                            graphTypes.ischeckedRA = "";

                        }
                    } else {
                        if ((graphTypes.ischeckedRA).equalsIgnoreCase("true")) {
//                graphTypes.nbrFormat="%";
                            graphTypes.ischeckedLA = "";
                            graphTypes.ischeckedRA = "%";


                        } else {

                            graphTypes.ischeckedLA = "";
                            graphTypes.ischeckedRA = "";
                        }
                    }

                } else if (graphlist.size() > 1) {
                    if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(1))) {
                        if ((graphTypes.ischeckedLA).equalsIgnoreCase("true")) {
//                 graphTypes.nbrFormat="%";
                            graphTypes.ischeckedLA = "%";
                            if ((graphTypes.ischeckedRA).equalsIgnoreCase("true")) {
                                graphTypes.ischeckedRA = "%";
                            } else {

                                graphTypes.ischeckedRA = "";

                            }
                        } else {
                            if ((graphTypes.ischeckedRA).equalsIgnoreCase("true")) {
//                graphTypes.nbrFormat="%";
                                graphTypes.ischeckedLA = "";
                                graphTypes.ischeckedRA = "%";
                            } else {

                                graphTypes.ischeckedLA = "";
                                graphTypes.ischeckedRA = "";
                            }
                        }
                    } else {
                        graphTypes.ischeckedLA = "";
                        graphTypes.ischeckedRA = "";
                    }
                } else {
                    graphTypes.ischeckedLA = "";
                    graphTypes.ischeckedRA = "";
                }
            }
        } else {
            if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(0))) {
                graphTypes.nbrFormat = "";
            } else {
                if (graphlist.size() > 1) {
                    if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(1))) {
                        graphTypes.nbrFormat = "";
                    }
                }
            }
        }
        if ((graphTypes.ischeckedRA).equalsIgnoreCase("true")) {
//                graphTypes.nbrFormat="%";
            graphTypes.ischeckedLA = "";
            graphTypes.ischeckedRA = "%";
        } else {

            graphTypes.ischeckedLA = "";
            graphTypes.ischeckedRA = "";
        }

        graphTypes.drillmap = drillmap;
        graphTypes.minvalue = minvalue;
        graphTypes.nextviewbyid = nextviewbyid;
        graphTypes.viewById = viewById;
        graphTypes.viewbylist = viewbylist;
        graphTypes.rowviewby = rowviewby;
        graphTypes.columnviewby = columnviewby;
        graphTypes.viewByColumns = viewByColumns;
        if (graphType.equalsIgnoreCase("bar-vertical") || graphType.equalsIgnoreCase("Waterfall") || graphType.equalsIgnoreCase("Waterfall(GT)")) {
            if (graphType.equalsIgnoreCase("Waterfall") || graphType.equalsIgnoreCase("Waterfall(GT)")) {
                graphTypes.varyBarColor = true;
            }
            jqplotbuffer = graphTypes.BarVertical(graphType, container, request);
        } else if (graphType.equalsIgnoreCase("Line") || graphType.equalsIgnoreCase("Line(Smooth)") || graphType.equalsIgnoreCase("Line(Dashed)") || graphType.equalsIgnoreCase("Line(Simple)") || graphType.equalsIgnoreCase("Line(Simple-R)") || graphType.equalsIgnoreCase("Line(Std)") || graphType.equalsIgnoreCase("Dot-Graph")) {
            jqplotbuffer = graphTypes.Line(graphType, container, request);
        } else if (graphType.equalsIgnoreCase("Area")) {
            jqplotbuffer = graphTypes.Area(graphType, container, request);
        } else if (graphType.equalsIgnoreCase("Bar-Horizontal")) {
            jqplotbuffer = graphTypes.BarHorizontal(graphType, container, request);
        } else if (graphType.equalsIgnoreCase("Area-Line")) {
            jqplotbuffer = graphTypes.AreaLine(graphType, container, request);
        } else if (graphType.equalsIgnoreCase("Bubble") || graphType.equalsIgnoreCase("Bubble(log)")) {
            graphTypes.data = bubblelist;
            graphTypes.ticks = rowviewlist;

            if (graphlist.size() >= 3) {
                jqplotbuffer = graphTypes.Bubble(graphType, container, request);
            } else {
                jqplotbuffer.append("alert(\"Please Select More Than 2 Measures\")");
            }
        } else if (graphType.equalsIgnoreCase("Scatter") || graphType.equalsIgnoreCase("Scatter(Partial)") || graphType.equalsIgnoreCase("Scatter(Regression)")) {
            graphTypes.data = scatterlist;
            if (viewByColumns.length >= 2) {
                graphTypes.ticks = tworowviewlist;

            } else {
                graphTypes.ticks = rowviewlist;

            }
            if (graphType.equalsIgnoreCase("Scatter(Regression)")) {
                jqplotbuffer = graphTypes.ScatterRegression(graphType, container, request);
            } else {
                jqplotbuffer = graphTypes.Scatter(graphType, container, request);
            }
        } else if (graphType.equalsIgnoreCase("StackedBar(H)") || graphType.equalsIgnoreCase("StackedH(Percent)")) {
            jqplotbuffer = graphTypes.StackedBarHorizontal(graphType, container, request);
        } else if (graphType.equalsIgnoreCase("StackedArea")) {
            jqplotbuffer = graphTypes.StackedArea(graphType, container, request);
        } else if (graphType.equalsIgnoreCase("StackedBar(V)") || graphType.equalsIgnoreCase("StackedBar(Percent)")) {
            jqplotbuffer = graphTypes.StackedBarVertical(graphType, container, request);
        } else if (graphType.equalsIgnoreCase("DualAxis(Bar-Line)") || graphType.equalsIgnoreCase("DualAxis(Area-Line)")) {
            graphTypes.overlaid = overlaid;
            jqplotbuffer = graphTypes.DualAxis(graphType, container, request);

        } else if (graphType.equalsIgnoreCase("Overlaid(Bar-Line)") || graphType.equalsIgnoreCase("Overlaid(Bar-Dot)")) {
            graphTypes.overlaid = overlaid;
            jqplotbuffer = graphTypes.Overlaid(graphType, container, request);

        } else if (graphType.equalsIgnoreCase("Funnel") || graphType.equalsIgnoreCase("Funnel(INV)")) {
            graphTypes.data = pielist;
            if (!container.isReportCrosstab()) {
                graphTypes.legendlabels = tworowviewlist;
            }
            jqplotbuffer = graphTypes.Funnel(graphType, container, request);

        } else if ((graphType.equalsIgnoreCase("Pie") || graphType.equalsIgnoreCase("Pie-Empty")) && !legendDiv) {
            graphTypes.data = pielist;
            if (!container.isReportCrosstab()) {
                graphTypes.legendlabels = tworowviewlist;
            }
            jqplotbuffer = graphTypes.Pie(graphType, container, request);

        } else if (graphType.equalsIgnoreCase("Donut-Double") || graphType.equalsIgnoreCase("Donut-Single")) {
            if (!container.isReportCrosstab()) {
                graphTypes.legendlabels = tworowviewlist;
            }
            ArrayList donutlist = new ArrayList();
            donutlist = (ArrayList) graphTypes.data.clone();
            graphTypes.data.clear();
            if (viewByColumns.length < 2) {
                graphTypes.data.add(donutrowlist);
                jqplotbuffer.append(" var s1=" + donutrowlist + ";");
                if (graphType.equalsIgnoreCase("Donut-Double")) {
                    if (donutrowlist1 != null && !donutrowlist1.isEmpty()) {
                        graphTypes.data.add(donutrowlist1);
                    }
                }

            }
            if (viewByColumns.length >= 2) {
                graphTypes.data.add(donutlist.get(0));
                if (graphType.equalsIgnoreCase("Donut-Double")) {
                    if (list5.size() > 1) {
                        graphTypes.data.add(donutlist.get(1));
                    }
                }
            }
            jqplotbuffer = graphTypes.Donut(graphType, container, request);

        } else if (graphType.equalsIgnoreCase("ColumnPie")) {
            graphTypes.data = columnpieDataSet;
            graphTypes.legendlabels = tworowviewlist;
            graphTypes.columnPieVewBy = viewBy;
            jqplotbuffer = graphTypes.Columnpie(graphType, container, request);
        } else if (legendDiv) {
            graphTypes.data = pielist;
            graphTypes.legendDiv = legendDiv;
            jqplotbuffer = graphTypes.LegendDiv();
        } else if (graphType.equalsIgnoreCase("Mekko")) {
            if (container.isReportCrosstab()) {
                jqplotbuffer = graphTypes.Mekko(graphType, container, request);
            } else {
                jqplotbuffer.append("alert(\"Works Only For CrossTab Report\")");
            }
        } else if (graphType.equalsIgnoreCase("Block")) {
            graphTypes.data = scatterlist;
            if (viewByColumns.length >= 2) {
                graphTypes.ticks = tworowviewlist;

            } else {
                graphTypes.ticks = rowviewlist;

            }
            jqplotbuffer = graphTypes.Block(graphType, container, request);
        }

        return jqplotbuffer.toString();

    }

    /**
     * @return the isFromOneView
     */
    public boolean isFromOneView() {
        return isFromOneView;
    }

    public boolean isFromOneViewschedule() {
        return isFromOneViewschedule;
    }

    /**
     * @param isFromOneView the isFromOneView to set
     */
    public void setIsFromOneView(boolean isFromOneView) {
        this.isFromOneView = isFromOneView;
    }

    public void setisFromOneViewschedule(boolean isFromOneViewschedule) {
        this.isFromOneViewschedule = isFromOneViewschedule;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public String getViewBy(ProgenDataSet retObj, String[] viewByColumns, int rowNum) {
        StringBuffer viewBy = new StringBuffer();
        for (int i = 0; i < viewByColumns.length; i++) {
            viewBy.append("," + retObj.getFieldValueString(rowNum, viewByColumns[i]));
        }

        return viewBy.substring(1);
    }

    private boolean compareStringArray(String[] temp, String[] currDimValue) {
        int count = 0;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equalsIgnoreCase(currDimValue[i])) {
                count = count + 1;
            }
        }
        if (count == temp.length) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList getTwoRowViewLists(LinkedHashMap twoRowValueMap, ArrayList legendvalues) {
        ArrayList tworowlList = new ArrayList();

        for (int i = 0; i < legendvalues.size(); i++) {
            if (twoRowValueMap.get(legendvalues.get(i)) != null && !twoRowValueMap.get(legendvalues.get(i)).toString().isEmpty()) {
                tworowlList.add(twoRowValueMap.get(legendvalues.get(i)));
            } else {
                tworowlList.add(0.0);
            }
        }

        return tworowlList;
    }

    public boolean checkViewByExists(String viewBy) {
        boolean exists = false;
        if (rowValues != null && rowValues.length > 0) {
            for (int j = 0; j < rowValues.length; j++) {
                if (viewBy.equalsIgnoreCase(rowValues[j].toString())) {
                    exists = true;
                }
            }
        }
        return exists;
    }
}