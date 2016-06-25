/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.charts;

import com.progen.jqplot.ProGenJqPlotProperties;
import com.progen.report.DashletDetail;
import com.progen.report.entities.GraphReport;
import com.progen.report.pbDashboardCollection;
import java.util.*;
import prg.db.Container;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 *
 * This class is created to build Jqplot Graphs
 *
 */
public class ProgenJQPlotGraph {

    private String reportId;
    private String dashboardId;
    private String dashletId;
    private String graphId;
    private String graphType;
    private ArrayList rowViewByList = new ArrayList(); //it shuold be in the form of ['abc','xyz','123']
    private ArrayList rovViewBySimpleList; //it will be in the form of [abc,xyx,123]
    private HashMap<String, ArrayList> datasetMap; //it contains the datamap with Key=measure and Value=measure Values
    private ArrayList rowViewValueList;
    private ArrayList elementList; //
    private String chartId; //chart id can be reportId or dashletId
    private ArrayList elementNameList;
    private ArrayList lebelList; //it shuold be in the form of ['abc','xyz','123']
    private String contextPath;
    private String[] markerOptions = {"circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare"};
    private ArrayList orignallist = new ArrayList();
    private Container container = null;
    String graphDisplayRows = "";
    String graphLegend = null;
    String graphGridLines = null;
    String grpSize = null;
    String[] grprgbColorCode = null;
    boolean graphLabels = false;
    ArrayList Seriescolors = new ArrayList();
    private boolean showTickLabels = true;
    private String trendLegend;
    public String tickIntervels;
    public String trendType;
    private final ArrayList meterColorList = new ArrayList() {

        {
            add("#FF0000");
            add("#FFA500");
            add("#006400");
        }
    };
    private String measureType = "Standard";

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public String getDashletId() {
        return dashletId;
    }

    public void setDashletId(String dashletId) {
        this.dashletId = dashletId;
    }

    public String getGraphId() {
        return graphId;
    }

    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }

    public String getGraphType() {
        return graphType;
    }

    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }

    public ArrayList getRowViewByList() {
        return rowViewByList;
    }

    public void setRowViewByList(ArrayList rowViewByList) {
        this.rovViewBySimpleList = rowViewByList;
        for (Object viewByElement : rowViewByList) {
            this.rowViewByList.add("'" + viewByElement + "'");
        }
    }

    public HashMap<String, ArrayList> getDatasetMap() {
        return datasetMap;
    }

    public void setDatasetMap(HashMap<String, ArrayList> datasetMap) {
        this.datasetMap = datasetMap;
    }

    public ArrayList getElementList() {
        return elementList;
    }

    public void setElementList(ArrayList elementList) {
        this.elementList = elementList;
    }

    public String getChartId() {
        return chartId;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId;
    }

    public String createJqPlotGraph() {
        StringBuilder jqplotbuffer = new StringBuilder();
        Object[] keyArray = (Object[]) datasetMap.keySet().toArray();
        ArrayList pielist = new ArrayList();
        ArrayList arealist = new ArrayList();
        ArrayList firstElementList = (ArrayList) datasetMap.get(keyArray[0]);
        ArrayList varList = new ArrayList();
        ArrayList donutrowlist = new ArrayList();
        ArrayList donutrowlist1 = new ArrayList();

        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail dashlet = collect.getDashletDetail(this.dashletId);
        GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
        JqplotGraphProperty jqprop = new JqplotGraphProperty();
        jqprop = dashlet.getJqplotgrapprop();
        graphDisplayRows = graphDetails.getDisplayRows();
        graphLegend = "N";
        if (graphDetails.isLegendAllowed()) {
            graphLegend = "Y";
        }
        graphGridLines = "N";
        if (graphDetails.isShowYAxisGrid()) {
            graphGridLines = "Y";
        }
        grpSize = String.valueOf(graphDetails.getGraphSize());
        grprgbColorCode = graphDetails.getGraphProperty().getRgbColorArr();
        graphLabels = graphDetails.getGraphProperty().isLabelsDisplayed();
        String[] colors = (String[]) jqprop.getSeriescolors();
//         
        if (colors == null) {
            colors = ProGenJqPlotProperties.seriescolors1;
        }
        if (colors != null) {
            Seriescolors = new ArrayList();
            for (int i = 0; i < colors.length; i++) {
                Seriescolors.add("'" + colors[i] + "'");
            }
        }
//        
//         
        // 
//        if(grprgbColorCode!=null && grprgbColorCode.length>0)
//            
        if ((graphType.equalsIgnoreCase("VerticalBar") || graphType.equalsIgnoreCase("Waterfall")) && graphType != null) {
            if (keyArray != null) {
                for (int i = 0; i < datasetMap.size(); i++) {
                    jqplotbuffer.append(" var " + keyArray[i] + "=" + datasetMap.get(keyArray[i]) + ";");
                    varList.add(keyArray[i]);
                }

            }
            jqplotbuffer.append("(function($) {");
            jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
            jqplotbuffer.append("if (!format) {");
            jqplotbuffer.append("format = '%.1f';");
            jqplotbuffer.append(" }");
            jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
            jqplotbuffer.append("};");
            jqplotbuffer.append(" function numberWithCommas(x) {");
            jqplotbuffer.append(" return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
            jqplotbuffer.append(" }");

            jqplotbuffer.append("})(jQuery);");
            jqplotbuffer.append(" var ticks =" + rowViewByList + ";");
            jqplotbuffer.append("var plot1 = $.jqplot('chart-" + chartId + "'," + varList + ", {");
            jqplotbuffer.append(" animate: true,animateReplot: true,");
            jqplotbuffer.append("seriesColors:" + Seriescolors + ",");
            jqplotbuffer.append(" seriesDefaults:{");
            jqplotbuffer.append(" renderer:$.jqplot.BarRenderer,");
            jqplotbuffer.append("pointLabels: { show: " + graphLabels + ",location: 's',edgeTolerance: -15 },");
            jqplotbuffer.append(" rendererOptions: {");
            if (graphType.equalsIgnoreCase("Waterfall")) {
                jqplotbuffer.append("waterfall:true,varyBarColor: true,");
            }
            //jqplotbuffer.append("varyBarColor: true,");
            jqplotbuffer.append(" showDataLabels: true");
            jqplotbuffer.append(" }  },");


            jqplotbuffer.append("ClickableBars: {");
            jqplotbuffer.append("onClick: function(i, j, data){");
            // jqplotbuffer.append(" alert('hi ');");
            jqplotbuffer.append("var reportid=" + reportId + ";");
            // jqplotbuffer.append(" alert(reportid);");
            jqplotbuffer.append(" var value=ticks[j];");
            // jqplotbuffer.append(" alert(value);");
            jqplotbuffer.append(" var viewbyid=\"111591\";");
            jqplotbuffer.append(" $.ajax({");
            jqplotbuffer.append("url:'" + contextPath + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY9161='+viewbyid+'&CBOARP111613='+value+'&type=drillgrph',");
            jqplotbuffer.append(" success: function(data){");
            //jqplotbuffer.append(" alert('"+request.getContextPath()+"/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY9161='+viewbyid+'&CBOARP111613='+value+'&type=drillgrph');");
            jqplotbuffer.append(" window.location.href=window.location.href;");
            jqplotbuffer.append("}");
            jqplotbuffer.append("});");
            jqplotbuffer.append("} },");

//               jqplotbuffer.append(" series: [");
//               jqplotbuffer.append(" {label:''}, ],");
            if (graphDetails.isLegendAllowed()) {
                jqplotbuffer.append(" legend: {");
                jqplotbuffer.append(" show: " + graphDetails.isLegendAllowed() + ", placement: 'outsideGrid',location: 'e',labels:" + lebelList + ", showLabels:true,fontFamily: 'Times New Roman',textColor: 'black'},");
            }
            jqplotbuffer.append(" axes: {");
            jqplotbuffer.append(" xaxis: {");
            jqplotbuffer.append(" renderer: $.jqplot.CategoryAxisRenderer,");
            jqplotbuffer.append(" ticks:ticks,");
            jqplotbuffer.append(" tickOptions:{");
            jqplotbuffer.append(" showGridline:" + graphDetails.isShowYAxisGrid() + ",");
            jqplotbuffer.append(" fontSize:'" + ProGenJqPlotProperties.xaxisfontSize + "' , ");
            jqplotbuffer.append(" textColor:'" + ProGenJqPlotProperties.xaxisfontcolor + "',");
            jqplotbuffer.append(" fontFamily:'" + ProGenJqPlotProperties.fontFamily + "',");
            jqplotbuffer.append(" angle:" + ProGenJqPlotProperties.xaxisangle + "");
            jqplotbuffer.append(" },");
            jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
            jqplotbuffer.append(" },");
            jqplotbuffer.append(" yaxis: {");
            jqplotbuffer.append(" pad: 0,tickOptions: { showGridline:" + graphDetails.isShowYAxisGrid() + ",formatString: '%d',fontSize:'" + ProGenJqPlotProperties.yaxisfontSize + "' , textColor:'" + ProGenJqPlotProperties.yaxisfontcolor + "', fontFamily:'" + ProGenJqPlotProperties.fontFamily + "',formatter: $.jqplot.euroFormatter}");
            jqplotbuffer.append(" }");
            jqplotbuffer.append(" },");
            jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'n',tooltipAxes: 'yref',tooltipContentEditor:tooltipContentEditor,useAxesFormatters:false },");
            if (grprgbColorCode != null && grprgbColorCode.length > 0) {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'rgb(" + Integer.parseInt(grprgbColorCode[0].trim()) + "," + Integer.parseInt(grprgbColorCode[1].trim()) + "," + Integer.parseInt(grprgbColorCode[2].trim()) + ")',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            } else {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'#FFFFFF',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            }
            jqplotbuffer.append("});");
            jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot) ");
            jqplotbuffer.append("{");
            jqplotbuffer.append("var pointlables=ticks[pointIndex];");
            jqplotbuffer.append("var roundvalue=plot.data[seriesIndex][pointIndex];");
            jqplotbuffer.append(" return pointlables+\" \" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")");
            jqplotbuffer.append("};");

        }

        if ((graphType.equalsIgnoreCase("Pie") || graphType.equalsIgnoreCase("Pie-Empty")) && graphType != null) {
//                int max = Math.max(rowViewByList.size(), firstElementList.size());
            for (int i = 0; i < rowViewByList.size(); i++) {
//                if (i < rowViewByList.size()) {
                pielist.add("[" + rowViewByList.get(i) + "," + datasetMap.get(keyArray[0]).get(i) + "]");
//                }
            }
            jqplotbuffer.append("var data =" + pielist + ";");
            jqplotbuffer.append("var plot1 = jQuery.jqplot ('chart-" + chartId + "', [data],");
            jqplotbuffer.append(" {");
            jqplotbuffer.append("seriesColors:" + Seriescolors + ",");
            jqplotbuffer.append(" seriesDefaults: {");

            jqplotbuffer.append("renderer: jQuery.jqplot.PieRenderer, ");
            jqplotbuffer.append("rendererOptions: {");
            if (graphType.equalsIgnoreCase("Pie-Empty")) {
                jqplotbuffer.append("fill: false,");
            }
            jqplotbuffer.append("showDataLabels:" + graphLabels + ",");
            jqplotbuffer.append("dataLabelFormatString:'<font color=black>%d%</font>',");
            jqplotbuffer.append("fontSize: '20px'");
            jqplotbuffer.append("}");
            jqplotbuffer.append(" },");
            if (graphDetails.isLegendAllowed()) {
                jqplotbuffer.append("legend: { show:" + graphDetails.isLegendAllowed() + ",location: 'e',placement: 'outsideGrid',xoffset: 0,fontSize: '10px',rowSpacing: '0px',textColor: 'balck', fontFamily: 'Lucida Grande, Lucida Sans, Arial, sans-serif' },");
            }
            jqplotbuffer.append("highlighter: {show:true,sizeAdjust: 10,tooltipLocation: 's',tooltipAxes: 'yref',formatString:'%s,%#d',useAxesFormatters: false},");
            // jqplotbuffer.append("seriesStyles: {seriesColors: ['black', 'orange', 'yellow', 'green', 'blue', 'indigo'],highlightColors: ['lightpink', 'lightsalmon', 'lightyellow', 'lightgreen', 'lightblue', 'mediumslateblue']},");
            if (grprgbColorCode != null && grprgbColorCode.length > 0) {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'rgb(" + Integer.parseInt(grprgbColorCode[0].trim()) + "," + Integer.parseInt(grprgbColorCode[1].trim()) + "," + Integer.parseInt(grprgbColorCode[2].trim()) + ")',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            } else {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'#FFFFFF',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            }
            jqplotbuffer.append(" });");

        }

        if ((graphType.equalsIgnoreCase("Line") || graphType.equalsIgnoreCase("Line(Smooth)") || graphType.equalsIgnoreCase("Line(Dashed)")) && graphType != null) {
            //jqplotbuffer.append("var ticks =;");
//               if (keyArray != null) {
            for (int i = 0; i < rowViewByList.size(); i++) {
//                if (i < rowViewByList.size()) {
                pielist.add("[" + rowViewByList.get(i) + "," + datasetMap.get(keyArray[0]).get(i) + "]");
//                }
            }

//            }
//               jqplotbuffer.append("var data ="+firstElementList+";");
            jqplotbuffer.append("var data =" + pielist + ";");
            jqplotbuffer.append("var plot1 = jQuery.jqplot ('chart-" + chartId + "', [data],");
            jqplotbuffer.append(" {");
            jqplotbuffer.append("title: '',animate: true,animateReplot: true,");
            jqplotbuffer.append(" series:[");
            for (int i = 0; i < orignallist.size(); i++) {
                jqplotbuffer.append("{");
                jqplotbuffer.append("lineWidth:2,");
                jqplotbuffer.append(" markerOptions: { style:'" + markerOptions[i] + "' }");
                jqplotbuffer.append("},");
            }

            jqplotbuffer.append("],");
            jqplotbuffer.append("seriesColors:" + Seriescolors + ",");
            jqplotbuffer.append("axesDefaults: { labelRenderer: $.jqplot.CanvasAxisLabelRenderer   },");
            jqplotbuffer.append("seriesDefaults: {lineWidth: 1,markerOptions: { show: true},");
            jqplotbuffer.append("pointLabels: { show: " + graphLabels + ",location: 's',edgeTolerance: -15 },");
            if (graphType.equalsIgnoreCase("Line(Dashed)")) {
                jqplotbuffer.append("linePattern:'dashed'},");
            } else if (graphType.equalsIgnoreCase("Line(Smooth)")) {
                jqplotbuffer.append("rendererOptions: { smooth:true}},");
            } else if (graphType.equalsIgnoreCase("Line")) {
                jqplotbuffer.append("rendererOptions: { smooth:false}},");
            }
            if (graphDetails.isLegendAllowed()) {
                jqplotbuffer.append(" legend: {");
                jqplotbuffer.append(" show: " + graphDetails.isLegendAllowed() + ", placement: 'outsideGrid',location: 'e',labels:" + lebelList + ",showLabels:true,fontFamily: 'Times New Roman',textColor: 'black'},");
            }
            // jqplotbuffer.append("axes: { xaxis: { label: \"X Axis\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickRenderer: $.jqplot.CanvasAxisTickRenderer, },yaxis: { label: \"Y Axis\" }}});");
            jqplotbuffer.append("axes:{");
            jqplotbuffer.append("xaxis: {renderer: $.jqplot.CategoryAxisRenderer,labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: { showGridline:" + graphDetails.isShowYAxisGrid() + ",fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',angle: -30 } },");
            jqplotbuffer.append("yaxis: {pad: 0,autoscale:true,labelRenderer: $.jqplot.CanvasAxisLabelRenderer, tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: { showGridline:" + graphDetails.isShowYAxisGrid() + ",formatString: '%d',fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"'} },");
            jqplotbuffer.append("},");
            jqplotbuffer.append(" highlighter:{show:true,tooltipContentEditor:tooltipContentEditor },");
            if (grprgbColorCode != null && grprgbColorCode.length > 0) {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'rgb(" + Integer.parseInt(grprgbColorCode[0].trim()) + "," + Integer.parseInt(grprgbColorCode[1].trim()) + "," + Integer.parseInt(grprgbColorCode[2].trim()) + ")',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            } else {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'#FFFFFF',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            }
            jqplotbuffer.append("});");
            jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot) {");
            jqplotbuffer.append("var series=\"\";");
            jqplotbuffer.append("var roundvalue=plot.data[seriesIndex][pointIndex];");
            // jqplotbuffer.append("alert(roundvalue);");
            jqplotbuffer.append(" return series+\" \" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")");
            //jqplotbuffer.append("return series+\"\" + plot.data[seriesIndex][pointIndex];");
            jqplotbuffer.append("}");

        }

        if (graphType.equalsIgnoreCase("Area") && graphType != null) {
            int max = Math.max(rowViewByList.size(), firstElementList.size());
            for (int i = 0; i < max; i++) {
                if (i < rowViewByList.size()) {
                    arealist.add("[" + (i + 1));
                }
                if (i < rowViewByList.size()) {
                    arealist.add(rowViewByList.get(i) + "]");
                }

            }
            jqplotbuffer.append("var ticks=" + arealist + ";");
            jqplotbuffer.append(" var s1 =" + datasetMap.get(keyArray[0]) + ";");
            jqplotbuffer.append("var plot1b = $.jqplot('chart-" + chartId + "',[s1],{");
            jqplotbuffer.append(" animate: true,");
            jqplotbuffer.append(" animateReplot: true,");
            jqplotbuffer.append(" stackSeries:false,");
            jqplotbuffer.append(" showMarker: false,");
            jqplotbuffer.append("seriesColors:" + Seriescolors + ",");
            jqplotbuffer.append("seriesDefaults: { fill: true,rendererOptions: {highlightMouseDown: true},");
            jqplotbuffer.append("pointLabels: { show: " + graphLabels + ",location: 's',edgeTolerance: -15 }");
            jqplotbuffer.append("},");
            if (graphDetails.isLegendAllowed()) {
                jqplotbuffer.append(" legend: {");
                jqplotbuffer.append(" show: " + graphDetails.isLegendAllowed() + ", placement: 'outsideGrid',location: 'e',labels:" + lebelList + ", showLabels:true,fontFamily: 'Times New Roman',textColor: 'black'},");
            }
            jqplotbuffer.append(" axes: {");
            jqplotbuffer.append(" xaxis: {ticks: ticks,tickRenderer: $.jqplot.CanvasAxisTickRenderer, tickOptions: {showGridline:" + graphDetails.isShowYAxisGrid() + ",fontFamily:'\"Verdana\"',fontSize: '7pt',textColor: \"black\",angle: -30},drawMajorGridlines: false},");
            jqplotbuffer.append(" yaxis: {");
            jqplotbuffer.append(" pad: 0,tickOptions: { showGridline:" + graphDetails.isShowYAxisGrid() + ",formatString: '%d',fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',formatter: $.jqplot.euroFormatter}");
            jqplotbuffer.append(" }");
            jqplotbuffer.append("},");
            jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'n',tooltipAxes: 'yref',useAxesFormatters:true,tooltipContentEditor:tooltipContentEditor },");
            if (grprgbColorCode != null && grprgbColorCode.length > 0) {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'rgb(" + Integer.parseInt(grprgbColorCode[0].trim()) + "," + Integer.parseInt(grprgbColorCode[1].trim()) + "," + Integer.parseInt(grprgbColorCode[2].trim()) + ")',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            } else {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'#FFFFFF',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            }
            jqplotbuffer.append("});");
            jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot) ");
            jqplotbuffer.append("{");
            jqplotbuffer.append(" var pointlables=ticks[pointIndex];");
            jqplotbuffer.append("var roundvalue=plot.data[seriesIndex][pointIndex];");
            jqplotbuffer.append(" return pointlables.toString().substr(2)+\",\" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")");
            jqplotbuffer.append("};");
        }
        if ((graphType.equalsIgnoreCase("Donut-Double") || graphType.equalsIgnoreCase("Donut-Single")) && graphType != null) {
//             int max = Math.max(rowViewByList.size(),firstElementList.size());
//               for (int i=0; i < max; i++)
//                {
//               if (i < rowViewByList.size())
//                {
//               donutrowlist.add("["+rowViewByList.get(i));
//                }
//                if (i< keyArray.length&& datasetMap.get(keyArray[0])!= null && i < datasetMap.get(keyArray[0]).size())
//                {
//                donutrowlist.add(datasetMap.get(keyArray[0]).get(i)+"]");
//                }
//                 if (i < rowViewByList.size())
//                {
//               donutrowlist1.add("["+rowViewByList.get(i));
//                }
//                if (i< keyArray.length && datasetMap.get(keyArray[1]) != null&& i < datasetMap.get(keyArray[1]).size())
//                {
//                donutrowlist1.add(datasetMap.get(keyArray[1]).get(i)+"]");
//                }
//                }
//               
//               


            ArrayList<ArrayList> lines = new ArrayList<ArrayList>(); //each element in list is a line and each line will be in the form [['abc',123],['xyz',456]]
            ArrayList line1 = new ArrayList();
            for (int i = 0; i < datasetMap.size(); i++) {
                ArrayList line = new ArrayList();
                if (i == 0) {
                    line1 = line;
                }
                if (i == 2) {
                    break;
                }
                if (keyArray != null) {
//                       for(int j=0;j<keyArray.length;j++){
                    ArrayList rowViewValues = datasetMap.get(keyArray[i]);
                    for (int k = 0; k < rowViewByList.size(); k++) {
                        line.add("[" + rowViewByList.get(k) + "," + rowViewValues.get(k) + "]");
                    }

//                       }
                    lines.add(line);
                }
            }




//            jqplotbuffer.append(" var s1="+donutrowlist+";");
//            jqplotbuffer.append(" var s2="+donutrowlist1+";");
            if (graphType.equalsIgnoreCase("Donut-Single") && graphType != null) {
                jqplotbuffer.append("var plot3 = $.jqplot('chart-" + chartId + "', [" + line1 + "], {");
            } else {
                jqplotbuffer.append("var plot3 = $.jqplot('chart-" + chartId + "', " + lines + ", {");
            }
            jqplotbuffer.append("seriesColors:" + Seriescolors + ",");
            jqplotbuffer.append("seriesDefaults: {");
            jqplotbuffer.append("renderer:$.jqplot.DonutRenderer,");
            jqplotbuffer.append("rendererOptions:{");
            jqplotbuffer.append("sliceMargin:1,");
            jqplotbuffer.append("startAngle:30,");
            jqplotbuffer.append("showDataLabels: " + graphLabels + ",");
            //jqplotbuffer.append("dataLabels: 'value'");
            jqplotbuffer.append("}},");
            if (graphDetails.isLegendAllowed()) {
                jqplotbuffer.append(" legend: {");
                jqplotbuffer.append(" show: " + graphDetails.isLegendAllowed() + ", placement: 'outsideGrid',location: 'e', showLabels:true,fontSize: '10px',textColor: 'balck'},");
            }
            jqplotbuffer.append("highlighter: {show:true,sizeAdjust: 10,tooltipLocation: 's',tooltipAxes: 'yref',formatString:'%s,%#d',useAxesFormatters: false},");
            if (grprgbColorCode != null && grprgbColorCode.length > 0) {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'rgb(" + Integer.parseInt(grprgbColorCode[0].trim()) + "," + Integer.parseInt(grprgbColorCode[1].trim()) + "," + Integer.parseInt(grprgbColorCode[2].trim()) + ")',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            } else {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'#FFFFFF',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            }
            jqplotbuffer.append("});");
        }
        if (graphType.equalsIgnoreCase("StackedBarVertical") && graphType != null) {
            if (keyArray != null) {
                for (int i = 0; i < datasetMap.size(); i++) {
                    jqplotbuffer.append(" var " + keyArray[i] + "=" + datasetMap.get(keyArray[i]) + ";");
                    varList.add(keyArray[i]);
                }

            }
            jqplotbuffer.append("(function($) {");
            jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
            jqplotbuffer.append("if (!format) {");
            jqplotbuffer.append("format = '%.1f';");
            jqplotbuffer.append(" }");
            jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
            jqplotbuffer.append("};");
            jqplotbuffer.append(" function numberWithCommas(x) {");
            jqplotbuffer.append(" return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
            jqplotbuffer.append(" }");

            jqplotbuffer.append("})(jQuery);");
            jqplotbuffer.append(" var ticks =" + rowViewByList + ";");
            jqplotbuffer.append("var plot1 = $.jqplot('chart-" + chartId + "'," + varList + ", {");
            jqplotbuffer.append(" stackSeries: true,");
            jqplotbuffer.append(" animate: true,animateReplot: true,");
            jqplotbuffer.append("seriesColors:" + Seriescolors + ",");
            jqplotbuffer.append(" seriesDefaults:{");
            jqplotbuffer.append(" renderer:$.jqplot.BarRenderer,");
            jqplotbuffer.append("pointLabels: { show: " + graphLabels + ",location: 's',edgeTolerance: -15 },");
            jqplotbuffer.append(" rendererOptions: {");
            if (graphType.equalsIgnoreCase("Waterfall")) {
                jqplotbuffer.append("waterfall:true,varyBarColor: true,");
            }
            //jqplotbuffer.append("varyBarColor: true,");
            jqplotbuffer.append(" showDataLabels: true");
            jqplotbuffer.append(" }  },");
//               jqplotbuffer.append(" series: [");
//               jqplotbuffer.append(" {color: '#1E90FF',label:''}, ],");
            if (graphDetails.isLegendAllowed()) {
                jqplotbuffer.append(" legend: {");
                jqplotbuffer.append(" show: " + graphDetails.isLegendAllowed() + ", placement: 'outsideGrid',location: 'e',labels:" + lebelList + ", showLabels:true,fontFamily: 'Times New Roman',textColor: 'black'},");
            }
            jqplotbuffer.append(" axes: {");
            jqplotbuffer.append(" xaxis: {");
            jqplotbuffer.append(" renderer: $.jqplot.CategoryAxisRenderer,");
            jqplotbuffer.append(" ticks:ticks,");
            jqplotbuffer.append(" tickOptions:{");
            jqplotbuffer.append(" showGridline:" + graphDetails.isShowYAxisGrid() + ",");
            jqplotbuffer.append(" fontSize: '7pt' , ");
            jqplotbuffer.append(" textColor: \"black\",");
            jqplotbuffer.append(" fontFamily:'\"Verdana\"',");
            jqplotbuffer.append(" angle: -30");
            jqplotbuffer.append(" },");
            jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
            jqplotbuffer.append(" },");
            jqplotbuffer.append(" yaxis: {");
            jqplotbuffer.append(" pad: 0,tickOptions: {showGridline:" + graphDetails.isShowYAxisGrid() + ",formatString: '%d',fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',formatter: $.jqplot.euroFormatter}");
            jqplotbuffer.append(" }");
            jqplotbuffer.append(" },");
            jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'n',tooltipAxes: 'yref',useAxesFormatters:true,tooltipContentEditor:tooltipContentEditor },");
            if (grprgbColorCode != null && grprgbColorCode.length > 0) {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'rgb(" + Integer.parseInt(grprgbColorCode[0].trim()) + "," + Integer.parseInt(grprgbColorCode[1].trim()) + "," + Integer.parseInt(grprgbColorCode[2].trim()) + ")',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            } else {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'#FFFFFF',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            }
            jqplotbuffer.append("});");
            jqplotbuffer.append(" function tooltipContentEditor(str, seriesIndex, pointIndex, plot)");
            jqplotbuffer.append("{");
            jqplotbuffer.append(" var pointlables=ticks[pointIndex];");
            jqplotbuffer.append("var roundvalue=plot.data[seriesIndex][pointIndex];");
            jqplotbuffer.append(" return pointlables+\" \" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")");

            jqplotbuffer.append("}");;

        }
        if (graphType.equalsIgnoreCase("DualAxis(Bar-Line)") && graphType != null) {
            ArrayList<ArrayList> lines = new ArrayList<ArrayList>(); //each element in list is a line and each line will be in the form [['abc',123],['xyz',456]]
            for (int i = 0; i < datasetMap.size(); i++) {
                ArrayList line = new ArrayList();
                if (keyArray != null) {
//                       for(int j=0;j<keyArray.length;j++){
                    ArrayList rowViewValues = datasetMap.get(keyArray[i]);
                    String temp = "";
                    for (int k = 0; k < rowViewByList.size(); k++) {
                        temp = "[" + rowViewByList.get(k) + "," + rowViewValues.get(k) + "]";
                        line.add(temp);
                    }

//                       }
                    lines.add(line);
                }
            }

//               jqplotbuffer.append(" var line1="+donutrowlist+";");
//               jqplotbuffer.append(" var line2="+donutrowlist1+";");
            jqplotbuffer.append("(function($) {");
            jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
            jqplotbuffer.append("if (!format) {");
            jqplotbuffer.append("format = '%.1f';");
            jqplotbuffer.append(" }");
            jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
            jqplotbuffer.append("};");
            jqplotbuffer.append(" function numberWithCommas(x) {");
            jqplotbuffer.append(" return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
            jqplotbuffer.append(" }");

            jqplotbuffer.append("})(jQuery);");
            jqplotbuffer.append("var plot2 = $.jqplot('chart-" + chartId + "', " + lines + ",{");
            jqplotbuffer.append("animate: true,");
            jqplotbuffer.append("seriesColors:" + Seriescolors + ",");
            jqplotbuffer.append("series:[{renderer:$.jqplot.BarRenderer}, {yaxis:'y2axis'}],");
//               jqplotbuffer.append("axesDefaults: {");
//               jqplotbuffer.append("tickRenderer: $.jqplot.CanvasAxisTickRenderer ,");
//               jqplotbuffer.append("tickOptions: {");
//               jqplotbuffer.append("angle: -30");
//               jqplotbuffer.append("}},");
            if (graphDetails.isLegendAllowed()) {
                jqplotbuffer.append(" legend: {");
                jqplotbuffer.append(" show: " + graphDetails.isLegendAllowed() + ", placement: 'outsideGrid',location: 'e',labels:" + lebelList + ", showLabels:true, showLabels:true,fontFamily: 'Times New Roman',textColor: 'black'},");
            }
            jqplotbuffer.append(" axes: {");
            jqplotbuffer.append("xaxis: { renderer: $.jqplot.CategoryAxisRenderer,tickOptions:{fontSize: '7pt',textColor: \"black\",fontFamily:'\"Verdana\"',angle: -30},tickRenderer:$.jqplot.CanvasAxisTickRenderer },");
//               jqplotbuffer.append(" xaxis: {");
//               jqplotbuffer.append(" renderer: $.jqplot.CategoryAxisRenderer,");
//               jqplotbuffer.append(" ticks:ticks,");
//               jqplotbuffer.append(" tickOptions:{");
//               jqplotbuffer.append(" fontSize: '7pt' , ");
//               jqplotbuffer.append(" textColor: \"black\",");
//               jqplotbuffer.append(" fontFamily:'\"Verdana\"',");
//               jqplotbuffer.append(" angle: -30");
//               jqplotbuffer.append(" },");
//               jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
//               jqplotbuffer.append(" },");
            jqplotbuffer.append(" yaxis: { autoscale:false,tickOptions:{fontSize: '7pt',textColor: \"black\"},angle:-90 },");
            jqplotbuffer.append(" y2axis: {pad: 0, autoscale:false,tickOptions:{fontSize: '7pt',textColor: \"black\"},angle:-90 }");
            jqplotbuffer.append("},");
            jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'n',tooltipAxes: 'yref',useAxesFormatters:true,tooltipContentEditor:tooltipContentEditor },");
            if (grprgbColorCode != null && grprgbColorCode.length > 0) {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'rgb(" + Integer.parseInt(grprgbColorCode[0].trim()) + "," + Integer.parseInt(grprgbColorCode[1].trim()) + "," + Integer.parseInt(grprgbColorCode[2].trim()) + ")',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            } else {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'#FFFFFF',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            }
            jqplotbuffer.append("});");
            jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot)");
            jqplotbuffer.append("{");
            jqplotbuffer.append("var series=\"\";");
            jqplotbuffer.append("var roundvalue=plot.data[seriesIndex][pointIndex];");
            //jqplotbuffer.append(" return pointlables+\" \" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")");
            jqplotbuffer.append(" return series+\" \" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")");
            jqplotbuffer.append("}");
        }
        if (graphType.equalsIgnoreCase("Funnel") && graphType != null) {
            for (int i = 0; i < rowViewByList.size(); i++) {
//                if (i < rowViewByList.size()) {
                pielist.add("[" + rowViewByList.get(i) + "," + datasetMap.get(keyArray[0]).get(i) + "]");
//                }
            }
            jqplotbuffer.append("var data =" + pielist + ";");
            jqplotbuffer.append("var plot1 = $.jqplot ('chart-" + chartId + "', [data],");
            jqplotbuffer.append(" {");
            jqplotbuffer.append("seriesColors:" + Seriescolors + ",");
            jqplotbuffer.append(" seriesDefaults: {");
            jqplotbuffer.append("renderer: jQuery.jqplot.FunnelRenderer, ");

            jqplotbuffer.append("rendererOptions: {");

            jqplotbuffer.append("},");
            jqplotbuffer.append("pointLabels: { show: " + graphLabels + ",location: 's',edgeTolerance: -15 }");
            jqplotbuffer.append(" },");
            if (graphDetails.isLegendAllowed()) {
                jqplotbuffer.append(" legend: {");
                jqplotbuffer.append(" show: " + graphDetails.isLegendAllowed() + ", placement: 'outsideGrid',location: 'e',labels:" + lebelList + ",showLabels:true,fontFamily: 'Times New Roman',textColor: 'black'},");
            }
            if (grprgbColorCode != null && grprgbColorCode.length > 0) {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'rgb(" + Integer.parseInt(grprgbColorCode[0].trim()) + "," + Integer.parseInt(grprgbColorCode[1].trim()) + "," + Integer.parseInt(grprgbColorCode[2].trim()) + ")',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            } else {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'#FFFFFF',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            }
//               jqplotbuffer.append("highlighter: {show:true,sizeAdjust: 10,tooltipLocation: 's',tooltipAxes: 'yref',formatString:'%s,%#d',useAxesFormatters: false},");
            // jqplotbuffer.append("seriesStyles: {seriesColors: ['black', 'orange', 'yellow', 'green', 'blue', 'indigo'],highlightColors: ['lightpink', 'lightsalmon', 'lightyellow', 'lightgreen', 'lightblue', 'mediumslateblue']},");
//               jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'white',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            jqplotbuffer.append(" });");
        }
        if (graphType.equalsIgnoreCase("Overlaid(Bar-Line)") && graphType != null) {
            if (keyArray != null) {
                for (int i = 0; i < datasetMap.size(); i++) {
                    jqplotbuffer.append(" var " + keyArray[i] + "=" + datasetMap.get(keyArray[i]) + ";");
                    varList.add(keyArray[i]);
                }

            }
//             jqplotbuffer.append("var p1="+rowviewvaluelist+";");
//             jqplotbuffer.append("var p2="+valuelist1+";");
            jqplotbuffer.append("(function($) {");
            jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
            jqplotbuffer.append("if (!format) {");
            jqplotbuffer.append("format = '%.1f';");
            jqplotbuffer.append(" }");
            jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
            jqplotbuffer.append("};");
            jqplotbuffer.append(" function numberWithCommas(x) {");
            jqplotbuffer.append(" return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
            jqplotbuffer.append(" }");

            jqplotbuffer.append("})(jQuery);");
            jqplotbuffer.append("var p3=" + rowViewByList + ";");
            jqplotbuffer.append("$(function() {");
            jqplotbuffer.append("$.jqplot('chart-" + chartId + "', " + varList + ", BarChart());");
            jqplotbuffer.append(" });");
            jqplotbuffer.append("function BarChart(){");
            jqplotbuffer.append(" var optionsObj = {");
            jqplotbuffer.append("animate: true,");
            jqplotbuffer.append("seriesColors:" + Seriescolors + ",");
            jqplotbuffer.append("title: '',");
            jqplotbuffer.append("axes: {");
            jqplotbuffer.append("xaxis: {");
            jqplotbuffer.append("renderer: $.jqplot.CategoryAxisRenderer,");
            jqplotbuffer.append("ticks:p3,");
            jqplotbuffer.append(" tickOptions:{");
            jqplotbuffer.append(" showGridline:" + graphDetails.isShowYAxisGrid() + ",");
            jqplotbuffer.append(" fontSize: '7pt' , ");
            jqplotbuffer.append(" textColor: \"black\",");
            jqplotbuffer.append(" fontFamily:'\"Verdana\"',");
            jqplotbuffer.append(" angle:-30");
            jqplotbuffer.append(" },");
            jqplotbuffer.append("tickRenderer:$.jqplot.CanvasAxisTickRenderer");
            jqplotbuffer.append(" },");
            jqplotbuffer.append("yaxis: {pad: 0, tickOptions: {showGridline:" + graphDetails.isShowYAxisGrid() + ",showMark:false, formatString: \"%d\",textColor: \"black\",fontSize: '7pt',formatter: $.jqplot.euroFormatter} }");
            jqplotbuffer.append("},");
            jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'n',tooltipAxes: 'yref',useAxesFormatters:true,tooltipContentEditor:tooltipContentEditor },");
            jqplotbuffer.append("grid: {");
            if (grprgbColorCode != null && grprgbColorCode.length > 0) {
                jqplotbuffer.append("background: 'rgb(" + Integer.parseInt(grprgbColorCode[0].trim()) + "," + Integer.parseInt(grprgbColorCode[1].trim()) + "," + Integer.parseInt(grprgbColorCode[2].trim()) + ")',drawGridlines: true,shadow:true},");
            } else {
                jqplotbuffer.append("background: \"white\",drawGridlines: true,shadow:true},");
            }
            jqplotbuffer.append(" series: [ {renderer:$.jqplot.BarRenderer},{label:''}],");
            if (graphDetails.isLegendAllowed()) {
                jqplotbuffer.append(" legend: {");
                jqplotbuffer.append(" show:  " + graphDetails.isLegendAllowed() + ", placement: 'outsideGrid',location: 'e',labels:" + lebelList + ", showLabels:true,fontFamily: 'Times New Roman',textColor: 'black' },");
            }
            jqplotbuffer.append(" seriesDefaults:{");
            jqplotbuffer.append("shadow: true,rendererOptions:{ barPadding: 1, barMargin: 10,barWidth: 25},");
            jqplotbuffer.append("pointLabels: { show: " + graphLabels + ",location: 's',edgeTolerance: -15 },");
            jqplotbuffer.append("}};");
            jqplotbuffer.append("return optionsObj;");
            jqplotbuffer.append("}");
            jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot)");
            jqplotbuffer.append("{");
            jqplotbuffer.append("var pointlables=p3[pointIndex];");
            jqplotbuffer.append("var roundvalue=plot.data[seriesIndex][pointIndex];");
            jqplotbuffer.append(" return pointlables+\" \" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")");
            //jqplotbuffer.append("return pointlables+\",\"+ plot.data[seriesIndex][pointIndex];");
            jqplotbuffer.append("}");
        }
        if (graphType.equalsIgnoreCase("StackedArea") && graphType != null) {
            if (keyArray != null) {
                for (int i = 0; i < datasetMap.size(); i++) {
                    jqplotbuffer.append(" var " + keyArray[i] + "=" + datasetMap.get(keyArray[i]) + ";");
                    varList.add(keyArray[i]);
                }

            }
//               jqplotbuffer.append(" var s1 ="+rowviewvaluelist+";");
//               jqplotbuffer.append(" var s2 ="+valuelist1+";");
            jqplotbuffer.append("(function($) {");
            jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
            jqplotbuffer.append("if (!format) {");
            jqplotbuffer.append("format = '%.1f';");
            jqplotbuffer.append(" }");
            jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
            jqplotbuffer.append("};");
            jqplotbuffer.append(" function numberWithCommas(x) {");
            jqplotbuffer.append(" return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
            jqplotbuffer.append(" }");

            jqplotbuffer.append("})(jQuery);");
            jqplotbuffer.append(" var ticks =" + rowViewByList + ";");
            jqplotbuffer.append("var plot1 = $.jqplot('chart-" + chartId + "'," + varList + ", {");
            jqplotbuffer.append(" animate: true,");
            jqplotbuffer.append(" animateReplot: true,");
            jqplotbuffer.append(" stackSeries: true,");
            jqplotbuffer.append(" showMarker: false,");
            jqplotbuffer.append("seriesColors:" + Seriescolors + ",");
            jqplotbuffer.append("seriesDefaults: { fill: true ,");
            jqplotbuffer.append("pointLabels: { show: " + graphLabels + ",location: 's',edgeTolerance: -15 }},");
            if (graphDetails.isLegendAllowed()) {
                jqplotbuffer.append(" legend: {");
                jqplotbuffer.append(" show: " + graphDetails.isLegendAllowed() + ", placement: 'outsideGrid',location: 'e',labels:" + lebelList + ", showLabels:true,fontFamily: 'Times New Roman',textColor: 'black'},");
            }
            jqplotbuffer.append(" axes: {");
            jqplotbuffer.append(" xaxis: {");
            jqplotbuffer.append(" renderer: $.jqplot.CategoryAxisRenderer,");
            jqplotbuffer.append(" ticks:ticks,");
            jqplotbuffer.append(" tickOptions:{");
            jqplotbuffer.append(" showGridline:" + graphDetails.isShowYAxisGrid() + ",");
            jqplotbuffer.append(" fontSize: '7pt' , ");
            jqplotbuffer.append(" textColor: \"black\",");
            jqplotbuffer.append(" fontFamily:'\"Verdana\"',");
            jqplotbuffer.append(" angle: -30");
            jqplotbuffer.append(" },");
            jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
            jqplotbuffer.append(" },");
            jqplotbuffer.append(" yaxis: {");
            jqplotbuffer.append(" pad: 0,tickOptions: {showGridline:" + graphDetails.isShowYAxisGrid() + ", formatString: '%d',fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',formatter: $.jqplot.euroFormatter}");
            jqplotbuffer.append(" }");
            jqplotbuffer.append(" },");
            jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'n',tooltipAxes: 'yref',useAxesFormatters:true,tooltipContentEditor:tooltipContentEditor },");
            if (grprgbColorCode != null && grprgbColorCode.length > 0) {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'rgb(" + Integer.parseInt(grprgbColorCode[0].trim()) + "," + Integer.parseInt(grprgbColorCode[1].trim()) + "," + Integer.parseInt(grprgbColorCode[2].trim()) + ")',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            } else {
                jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'#FFFFFF',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            }
            jqplotbuffer.append("});");
            jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot)");
            jqplotbuffer.append("{");
            jqplotbuffer.append("var pointlables=ticks[pointIndex];");
            jqplotbuffer.append("var roundvalue=plot.data[seriesIndex][pointIndex];");
            jqplotbuffer.append(" return pointlables+\" \" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")");
            //jqplotbuffer.append(" return pointlables+\",\" + plot.data[seriesIndex][pointIndex]");
            jqplotbuffer.append("}");
        }

//        


        return jqplotbuffer.toString();

    }

    /**
     * @return the elementNameList
     */
    public ArrayList getElementNameList() {
        return elementNameList;
    }

    /**
     * @param elementNameList the elementNameList to set
     */
    public void setElementNameList(ArrayList elementNameList) {
        this.elementNameList = elementNameList;
        lebelList = new ArrayList();
        for (Object elementName : elementNameList) {
            this.lebelList.add("'" + elementName + "'");
        }

    }

    /**
     * @return the contextPath
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * @param contextPath the contextPath to set
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getTrendGraph(PbReturnObject returnObj, ArrayList measureList, ArrayList viewBys, String trendColor, String roleId, String measId) {
        StringBuffer jqplotbuffer = new StringBuffer();
        ArrayList datalist = new ArrayList();
        String min = "";
        String max = "";
        String trendColor1 = "";
        String labels = "";
        if (trendColor != null && trendColor.equalsIgnoreCase("true")) {
            labels = "true";
            trendColor = "";
        }
        if (returnObj != null) {
            String[] nameValuePair = null;
            String viewById = viewBys.get(0).toString();
            String xtick = "";
            if (trendColor == null || trendColor.isEmpty() || trendColor.equalsIgnoreCase("null") || trendColor.equalsIgnoreCase("false")) {
                trendColor = "#357EC7";
                trendColor1 = "#F87431";
            }
            for (Object measureId : measureList) {
                ArrayList datalist1 = new ArrayList();
                for (int i = 0; i < returnObj.rowCount; i++) {
                    xtick = returnObj.getFieldValueString(i, "TIME");
//                    xtick=xtick.replace("-","/");
//                    xtick="TEST"+i;
                    datalist1.add("['" + xtick + "'," + returnObj.getFieldValueString(i, "A_" + measureId.toString()) + "]");
                }
                datalist.add(datalist1);
                if (returnObj.rowCount > 0) {
                    min = returnObj.getFieldValueString(0, "TIME");
                    max = returnObj.getFieldValueString((returnObj.rowCount - 1), "TIME");
                }
            }

        }
//        for (int i = 0; i < rowViewByList.size(); i++) {
////                if (i < rowViewByList.size()) {
//                   pielist.add("["+rowViewByList.get(i)+","+datasetMap.get(keyArray[0]).get(i)+"]");
////                }
//            }

//            }
//               jqplotbuffer.append("var data ="+firstElementList+";");
        jqplotbuffer.append("var data =" + datalist + ";");
//               
        jqplotbuffer.append("var plot1 = jQuery.jqplot ('chart-" + chartId + "', data,");
        jqplotbuffer.append(" {");
        jqplotbuffer.append("title: '',animate: true,animateReplot: true,");
//               jqplotbuffer.append(" series:[");
//               for(int i=0;i<orignallist.size();i++)
//               {
//                 jqplotbuffer.append("{");
//                 jqplotbuffer.append("lineWidth:2,");
//                 jqplotbuffer.append(" markerOptions: { style:'"+markerOptions[i]+"' }");
//                 jqplotbuffer.append("},");
//               }
//
//               jqplotbuffer.append("],");
        //jqplotbuffer.append("seriesColors:"+Seriescolors+",");
        jqplotbuffer.append("seriesColors:[\"" + trendColor + "\",\"" + trendColor1 + "\"],");
        jqplotbuffer.append("axesDefaults: { labelRenderer: $.jqplot.CanvasAxisLabelRenderer   },");
        jqplotbuffer.append("seriesDefaults: {lineWidth: 1,markerOptions: { show: false},");
//               jqplotbuffer.append("pointLabels: { show: "+graphLabels+",location: 's',edgeTolerance: -15 },");
        jqplotbuffer.append("pointLabels: { show: false,location: 's',edgeTolerance: -15 },");
        if (graphType.equalsIgnoreCase("Line(Dashed)")) {
            jqplotbuffer.append("linePattern:'dashed'},");
        } else if (graphType.equalsIgnoreCase("Line(Smooth)")) {
            jqplotbuffer.append("rendererOptions: { smooth:true}},");
        } else if (graphType.equalsIgnoreCase("Line")) {
            jqplotbuffer.append("rendererOptions: { smooth:false}},");
        }

//                jqplotbuffer.append("linePattern:'dashed'},");
        jqplotbuffer.append("axes:{");
        if (trendType != null && trendType.equalsIgnoreCase("montFormat")) {
            jqplotbuffer.append("xaxis: {renderer: $.jqplot.CategoryAxisRenderer,labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: {fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',angle: -30 }},");
        } else {
            jqplotbuffer.append("xaxis: {renderer: $.jqplot.DateAxisRenderer,labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: {fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',angle: -30 },min:\"" + min + "\",max:\"" + max + "\",tickInterval:\"" + tickIntervels + "\" },");
        }
        jqplotbuffer.append("yaxis: {pad: 0,autoscale:true,labelRenderer: $.jqplot.CanvasAxisLabelRenderer, tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: { formatString: '%d',fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"'} },");
        jqplotbuffer.append("},");
        if (labels != null && labels.equalsIgnoreCase("true")) {
            jqplotbuffer.append(" legend: {");
            jqplotbuffer.append(" show:true,labels:['Current','Previous'] ,placement: 'outsideGrid',location: 's', showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberRows: 1}},");
        }
        jqplotbuffer.append(" highlighter:{show:true,tooltipContentEditor:tooltipContentEditor },");
//               if(grprgbColorCode!=null && grprgbColorCode.length>0){
//               jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'rgb("+Integer.parseInt(grprgbColorCode[0].trim())+","+Integer.parseInt(grprgbColorCode[1].trim())+","+Integer.parseInt(grprgbColorCode[2].trim())+")',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
//               }else{
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'#FFFFFF',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
//               }
        jqplotbuffer.append(" });");

        jqplotbuffer.append("$(\"#chart-" + chartId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, plot) {");
//          jqplotbuffer.append("alert(data[seriesIndex][pointIndex]);");
        jqplotbuffer.append("relatedParametersDrill('" + roleId + "','" + chartId + "','" + measId + "',data[seriesIndex][pointIndex]);");
        jqplotbuffer.append("});");

        jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot) {");

        jqplotbuffer.append("var value = plot.data[seriesIndex][pointIndex]; var valueRound= Math.round(value[1]*Math.pow(10,2))/Math.pow(10, 2); return value[0] + ': ' + valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");}");


        return jqplotbuffer.toString();
    }

    public String getTrendGraphs(PbReturnObject returnObj, ArrayList measureList, ArrayList viewBys, String trendColor) {
        StringBuffer jqplotbuffer = new StringBuffer();
        ArrayList datalist = new ArrayList();
        String min = "";
        String max = "";
        String trendColor1 = "";
        String labels = "";
        if (trendColor != null && trendColor.equalsIgnoreCase("true")) {
            labels = "true";
            trendColor = "";
        }
        if (returnObj != null) {
            String[] nameValuePair = null;
            String viewById = viewBys.get(0).toString();
            String xtick = "";
            if (trendColor == null || trendColor.isEmpty() || trendColor.equalsIgnoreCase("null")) {
                trendColor = "#357EC7";
                trendColor1 = "#F87431";
            }
            for (Object measureId : measureList) {
                ArrayList datalist1 = new ArrayList();
                for (int i = 0; i < returnObj.rowCount; i++) {
                    xtick = returnObj.getFieldValueString(i, "TIME");
//                    xtick=xtick.replace("-","/");
//                    xtick="TEST"+i;
                    datalist1.add("['" + xtick + "'," + returnObj.getFieldValueString(i, "A_" + measureId.toString()) + "]");
                }
                datalist.add(datalist1);
                if (returnObj.rowCount > 0) {
                    min = returnObj.getFieldValueString(0, "TIME");
                    max = returnObj.getFieldValueString((returnObj.rowCount - 1), "TIME");
                }
            }

        }
//        for (int i = 0; i < rowViewByList.size(); i++) {
////                if (i < rowViewByList.size()) {
//                   pielist.add("["+rowViewByList.get(i)+","+datasetMap.get(keyArray[0]).get(i)+"]");
////                }
//            }

//            }
//               jqplotbuffer.append("var data ="+firstElementList+";");
        jqplotbuffer.append("var data =" + datalist + ";");
//               
        jqplotbuffer.append("var plot1 = jQuery.jqplot ('chartA-" + chartId + "', data,");
        jqplotbuffer.append(" {");
        jqplotbuffer.append("title: '',animate: true,animateReplot: true,");
//               jqplotbuffer.append(" series:[");
//               for(int i=0;i<orignallist.size();i++)
//               {
//                 jqplotbuffer.append("{");
//                 jqplotbuffer.append("lineWidth:2,");
//                 jqplotbuffer.append(" markerOptions: { style:'"+markerOptions[i]+"' }");
//                 jqplotbuffer.append("},");
//               }
//
//               jqplotbuffer.append("],");
        //jqplotbuffer.append("seriesColors:"+Seriescolors+",");
        jqplotbuffer.append("seriesColors:[\"" + trendColor + "\",\"" + trendColor1 + "\"],");
        jqplotbuffer.append("axesDefaults: { labelRenderer: $.jqplot.CanvasAxisLabelRenderer   },");
        jqplotbuffer.append("seriesDefaults: {lineWidth: 1,markerOptions: { show: false},");
//               jqplotbuffer.append("pointLabels: { show: "+graphLabels+",location: 's',edgeTolerance: -15 },");
        jqplotbuffer.append("pointLabels: { show: false,location: 's',edgeTolerance: -15 },");
        if (graphType.equalsIgnoreCase("Line(Dashed)")) {
            jqplotbuffer.append("linePattern:'dashed'},");
        } else if (graphType.equalsIgnoreCase("Line(Smooth)")) {
            jqplotbuffer.append("rendererOptions: { smooth:true}},");
        } else if (graphType.equalsIgnoreCase("Line")) {
            jqplotbuffer.append("rendererOptions: { smooth:false}},");
        }

//                jqplotbuffer.append("linePattern:'dashed'},");
        jqplotbuffer.append("axes:{");
        if (trendType != null && trendType.equalsIgnoreCase("montFormat")) {
            jqplotbuffer.append("xaxis: {renderer: $.jqplot.CategoryAxisRenderer,labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: {fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',angle: -30 }},");
        } else {
            jqplotbuffer.append("xaxis: {renderer: $.jqplot.DateAxisRenderer,labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: {fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',angle: -30 },min:\"" + min + "\",max:\"" + max + "\",tickInterval:\"" + tickIntervels + "\" },");
        }
        jqplotbuffer.append("yaxis: {pad: 0,autoscale:true,labelRenderer: $.jqplot.CanvasAxisLabelRenderer, tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: { formatString: '%d',fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"'} },");
        jqplotbuffer.append("},");
        if (labels != null && labels.equalsIgnoreCase("true")) {
            jqplotbuffer.append(" legend: {");
            jqplotbuffer.append(" show:true,labels:['Current','Previous'] ,placement: 'outsideGrid',location: 's', showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberRows: 1}},");
        }
        jqplotbuffer.append(" highlighter:{show:true,tooltipContentEditor:tooltipContentEditor },");
//               if(grprgbColorCode!=null && grprgbColorCode.length>0){
//               jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'rgb("+Integer.parseInt(grprgbColorCode[0].trim())+","+Integer.parseInt(grprgbColorCode[1].trim())+","+Integer.parseInt(grprgbColorCode[2].trim())+")',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
//               }else{
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'#FFFFFF',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
//               }
        jqplotbuffer.append("});");

        jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot) {");
//                jqplotbuffer.append(" alert('hi');");
        jqplotbuffer.append("var value = plot.data[seriesIndex][pointIndex]; var valueRound= Math.round(value[1]*Math.pow(10,2))/Math.pow(10, 2); return value[0] + ': ' + valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");}");


        return jqplotbuffer.toString();
    }
    // added by srikanth.p for meter graph in oneView

    public String prepareMeterGraph(Object sRange, Object fB, Object sB, Object eRange, Object deviation, String dashid) {
        StringBuffer metergraphbuffer = new StringBuffer();
        //Gson json = new Gson();
        // metergraphbuffer.append("<div id='"+dashid+"' style=\"width:600px; height:280px;\" align=\"left\" possition=\"\"></div><br>;");
        metergraphbuffer.append("<script >");
        metergraphbuffer.append(" var s1 =[" + deviation + "];");
        metergraphbuffer.append(" var plot4 = $.jqplot('chart-" + dashid + "',[s1],{");
        metergraphbuffer.append("seriesDefaults: {");
        metergraphbuffer.append(" renderer: $.jqplot.MeterGaugeRenderer,");

        metergraphbuffer.append("rendererOptions: {");
//       metergraphbuffer.append("label: '").append(deviation).append("',");
//       metergraphbuffer.append("labelPosition: 'bottom',");
//       metergraphbuffer.append("labelHeightAdjust: -5,");
        metergraphbuffer.append("min:" + sRange + ",");
        metergraphbuffer.append("max:" + eRange + ",");
//       metergraphbuffer.append("ticks:["+sRange+","+fB+","+sB+","+eRange+"],");
        metergraphbuffer.append("intervals:[" + fB + "," + sB + "," + eRange + "],");
        if (getMeasureType().equalsIgnoreCase("Standard")) {
            metergraphbuffer.append("intervalColors:['" + meterColorList.get(0) + "', '" + meterColorList.get(1) + "', '" + meterColorList.get(2) + "'],");
        } else {
            metergraphbuffer.append("intervalColors:['" + meterColorList.get(2) + "', '" + meterColorList.get(1) + "', '" + meterColorList.get(0) + "'],");
        }
        metergraphbuffer.append("showTickLabels:" + showTickLabels + ", }");
        metergraphbuffer.append(" }");
        //metergraphbuffer.append("}");
        metergraphbuffer.append("});");
        metergraphbuffer.append("</script >");
//       HashMap map=new HashMap();
//       map.put("meterchart", metergraphbuffer.toString());
//     String  jsonString = json.toJson(map);
        //

        return metergraphbuffer.toString();
    }

    /**
     * @return the showTickLabels
     */
    public boolean isShowTickLabels() {
        return showTickLabels;
    }

    /**
     * @param showTickLabels the showTickLabels to set
     */
    public void setShowTickLabels(boolean showTickLabels) {
        this.showTickLabels = showTickLabels;
    }

    /**
     * @return the trendLegend
     */
    public String getTrendLegend() {
        return trendLegend;
    }

    /**
     * @param trendLegend the trendLegend to set
     */
    public void setTrendLegend(String trendLegend) {
        this.trendLegend = trendLegend;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public String getTrendGraphForParams(PbReturnObject returnObj, ArrayList measureList, ArrayList viewBys, String trendColor, int j, int counttb1, String seltbid1) {
        StringBuffer jqplotbuffer = new StringBuffer();
        ArrayList datalist = new ArrayList();
        String min = "";
        String max = "";
        String trendColor1 = "";
        String labels = "";
        ArrayList<String> sortCols = new ArrayList<String>();
        ArrayList<Integer> rowSequence;// = null;
        if (trendColor != null && trendColor.equalsIgnoreCase("true")) {
            labels = "true";
            trendColor = "";
        }
        if (returnObj != null) {
            String[] nameValuePair = null;
            String viewById = viewBys.get(0).toString();
            String xtick = "";
            if (trendColor == null || trendColor.isEmpty() || trendColor.equalsIgnoreCase("null")) {
                trendColor = "#357EC7";
                trendColor1 = "#F87431";
            }
            for (Object measureId : measureList) {
                ArrayList datalist1 = new ArrayList();
                int rowct = 0;
                if (returnObj.rowCount > 10) {
                    if (counttb1 > 0) {
                        rowct = counttb1;
                    } else {
                        rowct = 10;
                    }
                } else {
                    rowct = returnObj.rowCount;
                }
                char[] sortTypes = null;
                if (seltbid1.equalsIgnoreCase("bottom")) {
                    sortTypes = new char[]{'0'};
                } else {
                    sortTypes = new char[]{'1'};
                }

                sortCols.add("A_" + measureList.get(0).toString());
                returnObj.resetViewSequence();
                rowSequence = returnObj.findTopBottom(sortCols, sortTypes, rowct);
                returnObj.setViewSequence(rowSequence);

                for (int i = 0; i < rowct; i++) {
                    xtick = returnObj.getFieldValueString(returnObj.getViewSequence().get(i), "A_" + viewById);
                    datalist1.add("['" + xtick + "'," + returnObj.getFieldValueString(returnObj.getViewSequence().get(i), "A_" + measureId.toString()) + "]");
                }
                datalist.add(datalist1);
                if (rowct > 0) {
                    min = returnObj.getFieldValueString(0, "A_" + viewById);
                    max = returnObj.getFieldValueString((rowct - 1), "A_" + viewById);
                }
            }

        }
        jqplotbuffer.append("var dataB =" + datalist + ";");
        jqplotbuffer.append("$('#inputhidden" + j + "').val(dataB);");
        jqplotbuffer.append("var plot1 = jQuery.jqplot ('chart" + j + "-" + chartId + "', dataB,");
        jqplotbuffer.append(" {");
        jqplotbuffer.append("title: '',animate: true,animateReplot: true,");
        jqplotbuffer.append("seriesColors:[\"" + trendColor + "\",\"" + trendColor1 + "\"],");
        jqplotbuffer.append("axesDefaults: { labelRenderer: $.jqplot.CanvasAxisLabelRenderer   },");
        jqplotbuffer.append("seriesDefaults: {lineWidth: 1,markerOptions: { show: false},");
//               jqplotbuffer.append("pointLabels: { show: "+graphLabels+",location: 's',edgeTolerance: -15 },");
        jqplotbuffer.append("pointLabels: { show: false,location: 's',edgeTolerance: -15 },");
        if (graphType.equalsIgnoreCase("Line(Dashed)")) {
            jqplotbuffer.append("linePattern:'dashed'},");
        } else if (graphType.equalsIgnoreCase("Line(Smooth)")) {
            jqplotbuffer.append("rendererOptions: { smooth:true}},");
        } else if (graphType.equalsIgnoreCase("Line")) {
            jqplotbuffer.append("rendererOptions: { smooth:false}},");
        }
        jqplotbuffer.append("axes:{");
        if (trendType != null && trendType.equalsIgnoreCase("montFormat")) {
            jqplotbuffer.append("xaxis: {renderer: $.jqplot.CategoryAxisRenderer,labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: {fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',angle: -30 }},");
        } else {
            jqplotbuffer.append("xaxis: {renderer: $.jqplot.DateAxisRenderer,labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: {fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',angle: -30 },min:\"" + min + "\",max:\"" + max + "\",tickInterval:\"" + tickIntervels + "\" },");
        }
        jqplotbuffer.append("yaxis: {pad: 0,autoscale:true,labelRenderer: $.jqplot.CanvasAxisLabelRenderer, tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: { formatString: '%d',fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"'} },");
        jqplotbuffer.append("},");
        if (labels != null && labels.equalsIgnoreCase("true")) {
            jqplotbuffer.append(" legend: {");
            jqplotbuffer.append(" show:true,labels:['Current','Previous'] ,placement: 'outsideGrid',location: 's', showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberRows: 1}},");
        }
        jqplotbuffer.append(" highlighter:{show:true,tooltipContentEditor:tooltipContentEditor,tooltipLocation: 'ne', },");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'#FFFFFF',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
//               }
        jqplotbuffer.append("});");

        jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot) {");
        jqplotbuffer.append("var value = plot.data[seriesIndex][pointIndex]; var valueRound= Math.round(value[1]*Math.pow(10,2))/Math.pow(10, 2); return value[0] + ': ' + valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");}");


        return jqplotbuffer.toString();
    }

    public String getoverLayedGraph(PbReturnObject returnObj, ArrayList measureList, ArrayList viewBys, String trendColor, String viewId, ArrayList paramlist, int nuOfParams) {
        StringBuffer jqplotbuffer = new StringBuffer();
        ArrayList datalist = new ArrayList();
        String min = "";
        String max = "";
        String trendColor1 = "";
        String labels = "";
        int count = 0;
        ArrayList list5 = new ArrayList();
        ArrayList lengthof = new ArrayList();
        ArrayList lengthList = new ArrayList();
        ArrayList lableList = new ArrayList();
        Set lableSet = new LinkedHashSet();
        if (trendColor != null && trendColor.equalsIgnoreCase("true")) {
            labels = "true";
            trendColor = "";
        }
        JqplotGraphProperty jqprop = new JqplotGraphProperty();
        String[] colors = (String[]) jqprop.getSeriescolors();
//         
        if (colors == null) {
            colors = ProGenJqPlotProperties.seriescolors1;
        }
        if (colors != null) {
            Seriescolors = new ArrayList();
            for (int i = 0; i < colors.length; i++) {
                Seriescolors.add("'" + colors[i] + "'");
            }
        }
        if (returnObj != null) {
            String[] nameValuePair = null;
            String viewById = viewBys.get(0).toString();
            String xtick = "";
            ArrayList xtickfreq = new ArrayList();
            if (trendColor == null || trendColor.isEmpty() || trendColor.equalsIgnoreCase("null")) {
                trendColor = "#357EC7";
                trendColor1 = "#F87431";
            }
            for (Object measureId : measureList) {
                ArrayList datalist1 = new ArrayList();
                for (int i = 0; i < returnObj.rowCount; i++) {
//                    xtick=returnObj.getFieldValueString(i, "TIME");
                    lengthof.add(returnObj.getFieldValueString(i, "A_" + viewId));
//                    if(!xtickfreq.contains(xtick)){
//                    xtickfreq.add(xtick);
//                    }
//                    else{
//                    lengthof.add(xtickfreq.size());
//                    xtickfreq.clear();
//                    xtickfreq.add(xtick);
//                    }
//                    datalist1.add("['"+xtick+"',"+returnObj.getFieldValueString(i, "A_"+measureId.toString())+"]");
                }
//                datalist.add(datalist1);
                for (int k = 0; k < paramlist.size(); k++) {
                    lengthList.add(Collections.frequency(lengthof, paramlist.get(k)));

                }
                for (int g = 0; g < lengthof.size(); g++) {
                    lableSet.add("'" + lengthof.get(g) + "'");
                    // lableList.add("'"+lengthof.get(g)+"'");
                }
                if (returnObj.rowCount > 0) {
                    min = returnObj.getFieldValueString(0, "TIME");
                    max = returnObj.getFieldValueString((returnObj.rowCount - 1), "TIME");
                }


                for (int index1 = 0; index1 < paramlist.size(); index1++) {

                    ArrayList list4 = new ArrayList();
                    int actualRow = 0;
                    int size1 = Integer.parseInt(lengthList.get(index1).toString());
                    for (int i = 0; i < size1; i++) {
                        String temp = returnObj.getFieldValueString(count, "A_" + measureId.toString());
                        list4.add("['" + returnObj.getFieldValueString(i, "TIME") + "'," + returnObj.getFieldValueString(count, "A_" + measureId.toString()) + "]");
                        count++;
//                        if(temp.isEmpty()){
//                            temp="0";
//                        }
//                        double value = new Double(temp);
//                        if( value<=0.0){
//                            list4.add(null);
//                        }else
                        //list4.add(value);

                    }
                    if (list4.isEmpty()) {
                        list4.add("['',null]");
                    }
                    list5.add(list4);
                }
            }
        }


        jqplotbuffer.append("var data =" + list5 + ";");
        // 
        jqplotbuffer.append("var toolval =" + lableSet + ";");
        jqplotbuffer.append("var plot1 = jQuery.jqplot ('chartA" + nuOfParams + "-" + chartId + "', data,");
        jqplotbuffer.append(" {");
        jqplotbuffer.append("title: '',animate: true,animateReplot: true,");
        jqplotbuffer.append("seriesColors:" + Seriescolors + ",");
        jqplotbuffer.append("axesDefaults: { labelRenderer: $.jqplot.CanvasAxisLabelRenderer   },");
        jqplotbuffer.append("seriesDefaults: {lineWidth: 2,markerOptions: { show: false},");
        jqplotbuffer.append("pointLabels: { show: false,location: 's',edgeTolerance: -15 },");

        if (graphType.equalsIgnoreCase("Line(Dashed)")) {
            jqplotbuffer.append("linePattern:'dashed'},");
        } else if (graphType.equalsIgnoreCase("Line(Smooth)")) {
            jqplotbuffer.append("rendererOptions: { smooth:true}},");
        } else if (graphType.equalsIgnoreCase("Line")) {
            jqplotbuffer.append("rendererOptions: { smooth:false}},");
        }
        jqplotbuffer.append("axes:{");
        if (trendType != null && trendType.equalsIgnoreCase("montFormat")) {
            jqplotbuffer.append("xaxis: {renderer: $.jqplot.CategoryAxisRenderer,labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: {fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',angle: -30 }},");
        } else {
            jqplotbuffer.append("xaxis: {renderer: $.jqplot.DateAxisRenderer,labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: {fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',angle: -30 },min:\"" + min + "\",max:\"" + max + "\",tickInterval:\"" + tickIntervels + "\" },");
        }
        jqplotbuffer.append("yaxis: {pad: 0,autoscale:true,labelRenderer: $.jqplot.CanvasAxisLabelRenderer, tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: { formatString: '%d',fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"'} },");
        jqplotbuffer.append("},");
//               if(labels != null && labels.equalsIgnoreCase("true")){
        jqplotbuffer.append(" legend: {");
        jqplotbuffer.append(" show:true,labels:" + lableSet + " ,placement: 'outsideGrid',location: 's', showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberRows: 1}},");
//               }
        jqplotbuffer.append(" highlighter:{show:true,tooltipContentEditor:tooltipContentEditor,tooltipLocation: 'ne', },");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'#FFFFFF',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
        jqplotbuffer.append("});");
        jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot) {");
        jqplotbuffer.append(" var value = plot.data[seriesIndex][pointIndex]; var valueRound= Math.round(value[1]*Math.pow(10,2))/Math.pow(10, 2); return toolval[seriesIndex] +' : '+ value[0] + ': ' + valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");}");

        return jqplotbuffer.toString();
    }

    public String getoverLayedGraph(int j, ArrayList datalist) {
        StringBuffer jqplotbuffer = new StringBuffer();
        String trendColor = null;
        String trendColor1 = null;
        if (trendColor == null || trendColor.isEmpty() || trendColor.equalsIgnoreCase("null")) {
            trendColor = "#357EC7";
            trendColor1 = "#F87431";
        }
        jqplotbuffer.append("var dataB =" + datalist + ";");
        jqplotbuffer.append("var plot1 = jQuery.jqplot ('chart" + j + "-" + chartId + "', dataB,");
        jqplotbuffer.append(" {");
        jqplotbuffer.append("title: '',animate: true,animateReplot: true,");
        jqplotbuffer.append("seriesColors:[\"" + trendColor + "\",\"" + trendColor1 + "\"],");
        jqplotbuffer.append("axesDefaults: { labelRenderer: $.jqplot.CanvasAxisLabelRenderer   },");
        jqplotbuffer.append("seriesDefaults: {lineWidth: 1,markerOptions: { show: false},");
//               jqplotbuffer.append("pointLabels: { show: "+graphLabels+",location: 's',edgeTolerance: -15 },");
        jqplotbuffer.append("pointLabels: { show: false,location: 's',edgeTolerance: -15 },");
        if (graphType.equalsIgnoreCase("Line(Dashed)")) {
            jqplotbuffer.append("linePattern:'dashed'},");
        } else if (graphType.equalsIgnoreCase("Line(Smooth)")) {
            jqplotbuffer.append("rendererOptions: { smooth:true}},");
        } else if (graphType.equalsIgnoreCase("Line")) {
            jqplotbuffer.append("rendererOptions: { smooth:false}},");
        }
        jqplotbuffer.append("axes:{");
        if (trendType != null && trendType.equalsIgnoreCase("montFormat")) {
            jqplotbuffer.append("xaxis: {renderer: $.jqplot.CategoryAxisRenderer,labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: {fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',angle: -30 }},");
        } else {
            jqplotbuffer.append("xaxis: {renderer: $.jqplot.DateAxisRenderer,labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: {fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',angle: -30 } },");
        }
        jqplotbuffer.append("yaxis: {pad: 0,autoscale:true,labelRenderer: $.jqplot.CanvasAxisLabelRenderer, tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: { formatString: '%d',fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"'} },");
        jqplotbuffer.append("},");
//               if(labels != null && labels.equalsIgnoreCase("true")){
//               jqplotbuffer.append(" legend: {");
//               jqplotbuffer.append(" show:true,labels:['Current','Previous'] ,placement: 'outsideGrid',location: 's', showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberRows: 1}},");
//               }
        jqplotbuffer.append(" highlighter:{show:true,tooltipContentEditor:tooltipContentEditor,tooltipLocation: 'ne', },");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'#FFFFFF',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
//               }
        jqplotbuffer.append("});");

        jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot) {");
        jqplotbuffer.append("var value = plot.data[seriesIndex][pointIndex]; var valueRound= Math.round(value[1]*Math.pow(10,2))/Math.pow(10, 2); return value[0] + ': ' + valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");}");


        return jqplotbuffer.toString();
    }

    public String getSingleTrendGraph(ArrayList datalist, ArrayList vallist, int j) {
        StringBuffer jqplotbuffer = new StringBuffer();
        String labels = "";
        String trendColor = null;
        String trendColor1 = null;
        if (trendColor == null || trendColor.isEmpty() || trendColor.equalsIgnoreCase("null")) {
            trendColor = "#357EC7";
            trendColor1 = "#F87431";
        }
        if (trendColor != null && trendColor.equalsIgnoreCase("true")) {
            labels = "true";
            trendColor = "";
        }

        jqplotbuffer.append("var labelList=" + datalist + ";");
        jqplotbuffer.append("var dataList=" + vallist + ";");
        jqplotbuffer.append("var dataC =[" + vallist + "];");
        jqplotbuffer.append("var plot1 = jQuery.jqplot ('chartsingle-" + j + "', dataC,");
        jqplotbuffer.append(" {");
        jqplotbuffer.append("title: '',animate: true,animateReplot: true,");
        jqplotbuffer.append("seriesColors:[\"" + trendColor + "\",\"" + trendColor1 + "\"],");
        jqplotbuffer.append("axesDefaults: { labelRenderer: $.jqplot.CanvasAxisLabelRenderer   },");
        jqplotbuffer.append("seriesDefaults: {lineWidth: 1,markerOptions: { show: false},");
//               jqplotbuffer.append("pointLabels: { show: "+graphLabels+",location: 's',edgeTolerance: -15 },");
        jqplotbuffer.append("pointLabels: { show: false,location: 's',edgeTolerance: -15 },");
        graphType = "Line";
        if (graphType.equalsIgnoreCase("Line(Dashed)")) {
            jqplotbuffer.append("linePattern:'dashed'},");
        } else if (graphType.equalsIgnoreCase("Line(Smooth)")) {
            jqplotbuffer.append("rendererOptions: { smooth:true}},");
        } else if (graphType.equalsIgnoreCase("Line")) {
            jqplotbuffer.append("rendererOptions: { smooth:false}},");
        }
        jqplotbuffer.append("axes:{");
        if (trendType != null && trendType.equalsIgnoreCase("montFormat")) {
            jqplotbuffer.append("xaxis: {renderer: $.jqplot.CategoryAxisRenderer,labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickRenderer: $.jqplot.CanvasAxisTickRenderer,tickOptions: {fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',angle: -30 }},");
        } else {
            jqplotbuffer.append("xaxis: {renderer: $.jqplot.CategoryAxisRenderer,labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickRenderer: $.jqplot.CanvasAxisTickRenderer,ticks:labelList,tickOptions: {fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"',angle: -30 } },");
        }
        jqplotbuffer.append("yaxis: {pad: 0,autoscale:true,labelRenderer: $.jqplot.CanvasAxisLabelRenderer,tickOptions: { formatString: '%d',fontSize: '7pt' , textColor: \"black\", fontFamily:'\"Verdana\"'} },");
        jqplotbuffer.append("},");
//               if(labels != null && labels.equalsIgnoreCase("true")){
//               jqplotbuffer.append(" legend: {");
//               jqplotbuffer.append(" show:true,labels:['Current','Previous'] ,placement: 'outsideGrid',location: 's', showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberRows: 1}},");
//               }
        jqplotbuffer.append(" highlighter:{show:true,tooltipContentEditor:tooltipContentEditor,tooltipLocation: 'ne', },");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'#FFFFFF',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
//               }
        jqplotbuffer.append("});");

        jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot) {");
        jqplotbuffer.append("var value = dataList[pointIndex]; var valueRound= Math.round(value*Math.pow(10,2))/Math.pow(10, 2); return labelList[pointIndex] + ': ' + valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");}");


        return jqplotbuffer.toString();
    }
}
