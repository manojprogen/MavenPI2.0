/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.jqplot;

import java.io.Serializable;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import prg.db.Container;

public class ProGenJqPlotChartTypes implements Serializable {

    private static final long serialVersionUID = 2276668053087654909L;
    private String reportid = null;
    private String userid = null;
    private String graphtype = null;
    private String graphid = null;
    private String oldgraphtype = null;
    public String xaxisfontSize = "";
    public String yaxisfontSize = "";
    public static String xaxisfontcolor = "black";
    public static String yaxisfontcolor = "black";
    public static String fontFamily = "Verdana";
    public static String xaxisangle = "-30";
    public static String yaxisangle = "-30";
    public static String legendfontSize = "10pt";
    public static String[] seriescolors1 = {"#357EC7", "#667c26", "#C24641", "#A0C544", "#53B3FF", "#737CA1", "#7E354D", "#E66C2C", "#A74AC7", "#307D7E"};
    public static String[] seriescolors = {"#ADA96E", "#2F4F4F", "#A9A9A9", "#808000", "#B0171F", "#DC143C", "#b8860b", "#f5deb3", "#9932cc", "#d8bfd8", "#66cdaa", "#FF00FF", "#357EC7", "#667c26", "#C24641", "#A0C544", "#53B3FF", "#737CA1", "#7E354D", "#E66C2C", "#A74AC7", "#307D7E", "#357EC7", "#667c26", "#C24641", "#A0C544", "#53B3FF", "#737CA1", "#7E354D", "#E66C2C", "#A74AC7", "#307D7E", "#357EC7", "#667c26", "#C24641", "#A0C544", "#53B3FF", "#737CA1", "#7E354D", "#E66C2C", "#A74AC7", "#307D7E", "#357EC7", "#667c26", "#C24641", "#A0C544", "#53B3FF", "#737CA1", "#7E354D", "#E66C2C", "#A74AC7", "#307D7E"};
    public ArrayList data;
    public ArrayList tooltip;
    public ArrayList ticks;
    public ArrayList ticks12; //kruthika
    public String chartId;
    public String tickId;
    public ArrayList seriesColors;
    public String title;
    public ArrayList ticks122; //kruthika
    public boolean showlabels;
    public int measureRound = 0;
    public String nbrFormat = "";
    public boolean legenddisplay = false;
    public String graphLegendLoc;
    public ArrayList legendlabels;
    public int legendsPerRow = 8;
    public boolean griddisplay = false;
    public String showxAxis = "";
    public int labelDir = -30;
    public String yaxiscalibration = "";
    public int yAxisRounding = 0;
    public String yaxisstart = "";
    public String yaxisend = "";
    public String yaxisinterval = "";
    public String showlyAxis = "";
    public String targetValue = "";
    public String tickdisplay = ""; //kruthika
    public String targetcolor = "rgb(88,88,88)";
    public boolean varyBarColor = false;
    public String rgb = "transparent";
    public ArrayList drillmap;
    public String drilltype = "";
    public int minvalue = 0;
    public boolean showlocaldrill = false;
    public boolean isFromOneView;
    public boolean isFromOneViewschedule;
    public String nextviewbyid = null;
    public String viewById = null;
    public ArrayList viewbylist;
    public String rowviewby = "";
    public String columnviewby = "";
    public boolean isappendXaxis = false;
    public boolean isAdhocEnabled = false;
    public boolean iscolorGrouping = false;
    public String[] viewByColumns = null;
    public String[] markerOptions = {"circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare", "circle", "x", "dimaond", "Rectangle", "square", "filledCircle", "filledDiamond", "filledSquare"};
    public String xaxisstart = "";
    public String xaxisend = "";
    public String xaxisinterval = "";
    public int xAxisRounding = 0;
    public String graphtype1 = "Bar";
    public String graphtype2 = "Line";
    public ArrayList overlaid;
    public String showryAxis = "";
    public String y2axisstart = "";
    public String y2axisend = "";
    public String y2axisinterval = "";
    public int y2AxisRounding = 0;
    public String measureFormat = "";
    public ArrayList columnPieVewBy;
    public boolean legendDiv;
    public int barpadding = 1;
    public String datebyInterval = "default";
    public boolean ishomeTab;
    public ArrayList tickLenthComp = new ArrayList();
    public ArrayList tickLenthComp1 = new ArrayList(); //krk
    public boolean istooltipXaxis = true;
    public String ischeckedLA = "";
    public String ischeckedRA = "";
    public String ischeckedXA = "";
    public String ischeckedYA = "";
    public String Flag = "";
    public String viewbyLabels = "";
    public String viewOthers = "";
    public String isOthersFlag = "";
    public String contextpath = "";

    public void tickLength(Container container, String graphType) {
        int tickinterval = 0;
        if (datebyInterval != null && datebyInterval.equalsIgnoreCase("default")) {
            tickinterval = 0;
        } else {
            tickinterval = Integer.parseInt(datebyInterval);
        }
        for (int i = 0; i < this.ticks.size(); i++) {
            if (i == 0) {
                String tickString = "";
                if (viewByColumns.length >= 2 && !isappendXaxis && !this.ticks.get(i).toString().substring(0).contains("\"")) {
                    tickString = "\"" + this.ticks.get(i).toString().substring(this.ticks.get(i).toString().lastIndexOf(",") + 1);
                } else {
                    tickString = this.ticks.get(i).toString();
                }
                if (tickString.length() > 15) {
                    if (tickString.contains("\"")) {
                        tickLenthComp.add(tickString.substring(0, 15).concat("...") + "\"");
                    } else {
                        tickLenthComp.add(tickString.substring(0, 15).concat("...") + "\'");
                    }
                } else {
                    tickLenthComp.add(tickString);
                }
            } else if (i != 0 && (i % (tickinterval + 1)) == 0) {
                String tickString = "";
                if (viewByColumns.length >= 2 && !isappendXaxis && !this.ticks.get(i).toString().substring(0).contains("\"")) {
                    tickString = "\"" + this.ticks.get(i).toString().substring(this.ticks.get(i).toString().lastIndexOf(",") + 1);
                } else {
                    tickString = this.ticks.get(i).toString();
                }
                if (tickString.length() > 15) {
                    if (tickString.contains("\"")) {
                        tickLenthComp.add(tickString.substring(0, 15).concat("...") + "\"");
                    } else {
                        tickLenthComp.add(tickString.substring(0, 15).concat("...") + "\'");
                    }
                } else {
                    tickLenthComp.add(tickString);
                }
            } else {
                tickLenthComp.add("\"\"");
            }
        }
        // ArrayList ticks122;
//start of code by kruthika for tick interval
        if (this.tickdisplay.equalsIgnoreCase("1")) {  //kruthika
            ticks122 = this.ticks;
            for (int i = 0; i < ticks122.size(); i++) {
                if (i % 2 != 0) {

                    ticks122.set(i, " \" \" ");
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("2")) {
            ticks122 = this.ticks;
            for (int i = 0; i < ticks122.size(); i++) {
                if (i % 3 != 0) {
                    ticks122.set(i, " \" \" ");
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("3")) {
            ticks122 = this.ticks;
            for (int i = 0; i < ticks122.size(); i++) {
                if (i % 4 != 0) {
                    ticks122.set(i, " \" \" ");
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("4")) {
            ticks122 = this.ticks;
            for (int i = 0; i < ticks122.size(); i++) {
                if (i % 5 != 0) {
                    ticks122.set(i, " \" \" ");
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("5")) {
            ticks122 = this.ticks;
            for (int i = 0; i < ticks122.size(); i++) {
                if (i % 6 != 0) {
                    ticks122.set(i, " \" \" ");
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("6")) {
            ticks122 = this.ticks;
            for (int i = 0; i < ticks122.size(); i++) {
                if (i % 7 != 0) {
                    ticks122.set(i, " \" \" ");
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("7")) {
            ticks122 = this.ticks;
            for (int i = 0; i < ticks122.size(); i++) {
                if (i % 8 != 0) {
                    ticks122.set(i, " \" \" ");
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("8")) {
            ticks122 = this.ticks;
            for (int i = 0; i < ticks122.size(); i++) {
                if (i % 9 != 0) {
                    ticks122.set(i, " \" \" ");
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("9")) {
            ticks122 = this.ticks;
            for (int i = 0; i < ticks122.size(); i++) {
                if (i % 10 != 0) {
                    ticks122.set(i, " \" \" ");
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("10")) {
            ticks122 = this.ticks;
            for (int i = 0; i < ticks122.size(); i++) {
                if (i % 11 != 0) {
                    ticks122.set(i, " \" \" ");
                }
            }
        } else {
            ticks122 = this.ticks;
        }
        //end of code by kruthika
        if (graphType.equalsIgnoreCase("Waterfall(GT)")) {
            tickLenthComp.add("\"GT\"");
            this.ticks.add("\"GT\"");
        }

    }

    public void Areatick(Container container, String graphType) {
        int tickinterval = 0;
        if (datebyInterval != null && datebyInterval.equalsIgnoreCase("default")) {
            tickinterval = 0;
        } else {
            tickinterval = Integer.parseInt(datebyInterval);
        }
        // this.ticks =ticks122;
        if (this.tickdisplay.equalsIgnoreCase("1")) { //kruthika
            for (int i = 0; i < this.ticks.size(); i++) {
                if (i % 2 != 0) {
                    this.ticks.set(i, " \" \" ");
                } else {
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("2")) {
            for (int i = 0; i < this.ticks.size(); i++) {
                if (i % 3 != 0) {
                    this.ticks.set(i, " \" \" ");
                } else {
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("3")) {
            for (int i = 0; i < this.ticks.size(); i++) {
                if (i % 4 != 0) {
                    this.ticks.set(i, " \" \" ");
                } else {
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("4")) {
            for (int i = 0; i < this.ticks.size(); i++) {
                if (i % 5 != 0) {
                    this.ticks.set(i, " \" \" ");
                } else {
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("5")) {
            for (int i = 0; i < this.ticks.size(); i++) {
                if (i % 6 != 0) {
                    this.ticks.set(i, " \" \" ");
                } else {
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("6")) {
            for (int i = 0; i < this.ticks.size(); i++) {
                if (i % 7 != 0) {
                    this.ticks.set(i, " \" \" ");
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("7")) {
            for (int i = 0; i < this.ticks.size(); i++) {
                if (i % 8 != 0) {
                    this.ticks.set(i, " \" \" ");
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("8")) {
            for (int i = 0; i < this.ticks.size(); i++) {
                if (i % 9 != 0) {
                    this.ticks.set(i, " \" \" ");
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("9")) {
            for (int i = 0; i < this.ticks.size(); i++) {
                if (i % 10 != 0) {
                    this.ticks.set(i, " \" \" ");
                }
            }
        }
        if (this.tickdisplay.equalsIgnoreCase("10")) {
            for (int i = 0; i < this.ticks.size(); i++) {
                if (i % 11 != 0) {
                    this.ticks.set(i, " \" \" ");
                }
            }
        } //end of code by kruthika
        for (int i = 0; i < this.ticks.size(); i++) {
            if (i == 0) {
                String tickString = "";
                if (viewByColumns.length >= 2 && !isappendXaxis && !this.ticks.get(i).toString().substring(0).contains("\"")) {
                    tickString = "\"" + this.ticks.get(i).toString().substring(this.ticks.get(i).toString().lastIndexOf(",") + 1);
                } else {
                    tickString = this.ticks.get(i).toString();
                }
                if (tickString.length() > 15) {
                    if (tickString.contains("\"")) {
                        tickLenthComp.add("[" + (i + 1) + "," + tickString.substring(0, 15).concat("...") + "\"]");
                    } else {
                        tickLenthComp.add("[" + (i + 1) + "," + tickString.substring(0, 15).concat("...") + "\']");
                    }
                } else {
                    tickLenthComp.add("[" + (i + 1) + "," + tickString + "]");
                }
            } else if (i != 0 && (i % (tickinterval + 1)) == 0) {
                String tickString = "";
                if (viewByColumns.length >= 2 && !isappendXaxis && !this.ticks.get(i).toString().substring(0).contains("\"")) {
                    tickString = "\"" + this.ticks.get(i).toString().substring(this.ticks.get(i).toString().lastIndexOf(",") + 1);
                } else {
                    tickString = this.ticks.get(i).toString();
                }
                if (tickString.length() > 15) {
                    if (tickString.contains("\"")) {
                        tickLenthComp.add("[" + (i + 1) + "," + tickString.substring(0, 15).concat("...") + "\"]");
                    } else {
                        tickLenthComp.add("[" + (i + 1) + "," + tickString.substring(0, 15).concat("...") + "\']");
                    }
                } else {
                    tickLenthComp.add("[" + (i + 1) + "," + tickString + "]");
                }
            } else {
                tickLenthComp.add("\"\"");
            }
        }
    }

    public StringBuffer BarVertical(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
        this.tickLength(container, graphType);
        jqplotbuffer.append("(function($) {");
//            if (viewByColumns.length >= 2 && (graphType.equalsIgnoreCase("Waterfall") || graphType.equalsIgnoreCase("Waterfall(GT)"))) {
//                jqplotbuffer.append("alert(\"GraphType Not Supported\");");
//            }
        jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
        jqplotbuffer.append("if (!format) {");
        jqplotbuffer.append("format = '%.1f';");
        jqplotbuffer.append(" }");
        jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
        jqplotbuffer.append("};");
        jqplotbuffer.append(" function numberWithCommas(x) {");
        jqplotbuffer.append("  if(x==0 || x==0.0 || x==0.00) return '';");
        jqplotbuffer.append(" else return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
        jqplotbuffer.append(" }");
        jqplotbuffer.append("})(jQuery);");
        jqplotbuffer.append("var tooltip" + tickId + "=" + tooltip + ";");
        jqplotbuffer.append(" var ticks" + tickId + " =" + tickLenthComp + ";"); //kruthika
        //jqplotbuffer.append(" var ticks12" + tickId + " =" + ticks12 + ";");
        jqplotbuffer.append("var tooltipticks=" + tooltip + ";");
        jqplotbuffer.append("var plot1 = $.jqplot('" + chartId + "'," + data + ", {");
        jqplotbuffer.append(" animate: true,animateReplot: true,");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        if (!isFromOneView && !ishomeTab) {
            jqplotbuffer.append("title:{text: '" + title + "',fontSize: 12},");
        }
        jqplotbuffer.append(" seriesDefaults:{");
        jqplotbuffer.append(" renderer:$.jqplot.BarRenderer,");
        if (nbrFormat.equalsIgnoreCase("%")) {
            jqplotbuffer.append("pointLabels: { show: " + showlabels + ",formatString:'%." + measureRound + "f" + nbrFormat + ischeckedLA + "',location: 's',edgeTolerance: -15,formatter: $.jqplot.euroFormatter },");
        } else {
            jqplotbuffer.append("pointLabels: { show: " + showlabels + ",formatString:'%." + nbrFormat + measureRound + "f" + ischeckedLA + "',location: 's',edgeTolerance: -15,formatter: $.jqplot.euroFormatter },");
        }
        jqplotbuffer.append(" rendererOptions: {");
        if (graphType.equalsIgnoreCase("Waterfall") || graphType.equalsIgnoreCase("Waterfall(GT)")) {
            jqplotbuffer.append("waterfall:true,varyBarColor: true,");
        } else if (!container.getSortColumns().contains(viewByColumns[0]) || iscolorGrouping) {
            jqplotbuffer.append("varyBarColor: " + varyBarColor + ",");
        }
        jqplotbuffer.append("varyBarColor: " + varyBarColor + ",");
        jqplotbuffer.append(" showDataLabels: true,");
        jqplotbuffer.append(" barPadding: " + barpadding + ", fillToZero: true, useNegativeColors: false}  },");
        if (legenddisplay && (targetValue.isEmpty() || targetValue == null || data.size() > 1) && (!iscolorGrouping || viewByColumns.length < 2)) {
            jqplotbuffer.append(" legend: {");
            if (graphLegendLoc.equalsIgnoreCase("n") || graphLegendLoc.equalsIgnoreCase("s")) {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer,rendererOptions: {numberColumns: " + legendsPerRow + "}},");
            } else {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer,rendererOptions: {numberColumns: 1}},");
            }

        } else if ((iscolorGrouping || targetValue != null) && legenddisplay) {
            jqplotbuffer.append(" legend: {");
            jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "', showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer,rendererOptions: {numberColumns: " + legendsPerRow + "}},");
            jqplotbuffer.append("series:" + legendlabels + ",");
        }
        jqplotbuffer.append(" axes: {");
        jqplotbuffer.append(" xaxis: {");
        jqplotbuffer.append(" renderer: $.jqplot.CategoryAxisRenderer,");
        if (graphType.equalsIgnoreCase("Waterfall")) {
            jqplotbuffer.append(" ticks:ticks" + tickId + ",");
        }
        jqplotbuffer.append("label:\"" + showxAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        jqplotbuffer.append(" ticks:" + ticks122 + ",");
        jqplotbuffer.append(" tickOptions:{");
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append(" fontSize:'" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor:'" + this.xaxisfontcolor + "',");
        jqplotbuffer.append(" fontFamily:'" + this.fontFamily + "',");
        jqplotbuffer.append(" angle:" + labelDir + ",");
        jqplotbuffer.append(" },");

        jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" yaxis: {");
        if (yaxiscalibration.equalsIgnoreCase("Custom") && !yaxisstart.isEmpty() && !yaxisend.isEmpty() && !yaxisinterval.isEmpty()) {
            jqplotbuffer.append(" pad:0,autoscale:true, min:" + yaxisstart + ",max:" + yaxisend + ",tickInterval: " + yaxisinterval + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter},");
        } else if (yaxiscalibration.equalsIgnoreCase("default") || minvalue == -1) {
            jqplotbuffer.append(" tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter},");
        } else {
            jqplotbuffer.append(" pad:0, min:" + minvalue + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter},");
        }
        jqplotbuffer.append("label:\"" + showlyAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer");
        jqplotbuffer.append(" }");
        jqplotbuffer.append(" },");
        if (targetValue != "" && !targetValue.isEmpty()) {
            jqplotbuffer.append("canvasOverlay: { show: true,");
            jqplotbuffer.append("objects: [{horizontalLine: {name: 'pebbles',");
            jqplotbuffer.append(" y: " + targetValue + ",lineWidth: 2,color: \"" + targetcolor + "\",");
            jqplotbuffer.append("shadow: true,lineCap: 'butt',xOffset: 0, showTooltip: true,tooltipFormatString:\"" + targetValue + "\" }}, ] },");
        }
        jqplotbuffer.append(" cursor: {show:true,zoom:true,showTooltip: false,},");
        jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'ne',tooltipAxes: 'yref',formatString: '%d',tooltipContentEditor:tooltipContentEditor,useAxesFormatters:false },");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
        jqplotbuffer.append("});");
        jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot) ");
        jqplotbuffer.append("{");
        jqplotbuffer.append("var pointlables=ticks" + tickId + "[pointIndex];");
        jqplotbuffer.append("var roundvalue=Math.round(plot.data[seriesIndex][pointIndex]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + ");");
        if (container.isReportCrosstab()) {
            jqplotbuffer.append("var tooltip1=tooltip" + tickId + "[seriesIndex];");
            if (!istooltipXaxis) {
                jqplotbuffer.append(" return tooltip1.toString()+\":\"  +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + ischeckedLA + "\"");
            } else {
                jqplotbuffer.append(" return pointlables.toString()+\":\"+tooltip1.toString()+\":\"  +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + ischeckedLA + "\"");
            }
        } else {
            jqplotbuffer.append(" return pointlables.toString()+\":\" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + ischeckedLA + "\"");
        }
        jqplotbuffer.append("};");
        //mayank
        if (targetValue != "" && !targetValue.isEmpty() && !graphType.equalsIgnoreCase("Waterfall") || !graphType.equalsIgnoreCase("Waterfall(GT)")) {
            jqplotbuffer.append("$('#" + chartId + " .jqplot-point-label').css({color:'rgb(254,208,175)'});");
        }


//         if ((ischischeckedXAeckedXA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-xaxis-tick\").hide();");
//                 }
//         if ((ischeckedYA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-yaxis-tick\").hide();");
//                 }
//         jqplotbuffer.append("$(\".jqplot-xaxis-tick\").hide();");
//          jqplotbuffer.append("$(\".jqplot-yaxis-tick\").hide();");

        if (!isFromOneView) {
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, i, j, data) {");
            jqplotbuffer.append("var reportid=" + reportid + ";");
            jqplotbuffer.append(" var value=ticks" + tickId + "[j];");
            jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");
            if (showlocaldrill) {
                jqplotbuffer.append(" $.ajax({");
                if (isFromOneViewschedule) {
                    jqplotbuffer.append("url:'" + contextpath + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+value+'&type=drillgrph',");

                } else {
                    jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+value+'&type=drillgrph',");
                }
                jqplotbuffer.append(" success: function(data){");
                jqplotbuffer.append(" window.location.href=window.location.href;");
                jqplotbuffer.append("}");
                jqplotbuffer.append("});");
            } else if (drilltype.equalsIgnoreCase("report")) {
                ArrayList url = container.getLinks();
                String Url = url.get(0).toString();
                if (container.isReportCrosstab()) {
                    jqplotbuffer.append("submitformStandard('" + Url + "',ticks" + tickId + "[j],i,j,'" + drillmap.toString() + "','" + rowviewby + "','" + columnviewby + "',tooltip" + tickId + "[i]);");
                } else {
                    jqplotbuffer.append("submitformStandard('" + Url + "',ticks" + tickId + "[j],i,j,'" + drillmap.toString() + "');");
                }
            } else {
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    jqplotbuffer.append("submitform('" + oneUrl + "',ticks" + tickId + "[j],'" + isAdhocEnabled + "');");
                }

            }

            jqplotbuffer.append("}) ");
        }
        //added by srikanth.p for drilling in oneView
        if (isFromOneView) {
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, data) {");
            jqplotbuffer.append("var value=ticks" + tickId + "[pointIndex];");
            ArrayList url = container.getLinks();
            String oneUrl = "";
            if (url != null) {
                oneUrl = url.get(0).toString();
                oneUrl = oneUrl.replace("reportViewer.do?reportBy=viewReport", "dashboardViewer.do?reportBy=getOLAPGraphforOneView");
                jqplotbuffer.append("drillOLAPGraph('" + oneUrl + "',value);");
            }

            jqplotbuffer.append("});");
        }
        return jqplotbuffer;
    }

    public StringBuffer Line(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
        this.tickLength(container, graphType);
        jqplotbuffer.append("(function($) {");
        jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
        jqplotbuffer.append("if (!format) {");
        jqplotbuffer.append("format = '%.1f';");
        jqplotbuffer.append(" }");
        jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
        jqplotbuffer.append("};");
        jqplotbuffer.append(" function numberWithCommas(x) {");
        jqplotbuffer.append("  if(x==0 || x==0.0 || x==0.00) return '';");
        jqplotbuffer.append(" else return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
        jqplotbuffer.append(" }");
        jqplotbuffer.append("})(jQuery);");
        jqplotbuffer.append(" var ticks" + tickId + " =" + tickLenthComp + ";"); //kruthika
        jqplotbuffer.append("var tooltip" + tickId + "=" + tooltip + ";");
        jqplotbuffer.append("var tooltipticks=" + tooltip + ";");
        jqplotbuffer.append("var plot1 = $.jqplot('" + chartId + "'," + data + ", {");

        jqplotbuffer.append(" animate: true,animateReplot: true,");
        if (!graphType.equalsIgnoreCase("Line(Simple)") && !graphType.equalsIgnoreCase("Line(Std)")) {
            jqplotbuffer.append(" series:[");
            if (graphType.equalsIgnoreCase("Line(Simple-R)")) {
                jqplotbuffer.append("{trendline: { show:true,  color: '#006400'  }},");
            }
            if (graphType.equalsIgnoreCase("Dot-Graph")) {
                for (int i = 0; i < data.size(); i++) {
                    jqplotbuffer.append("{ showLine:false, markerOptions: { shadow: false,  style:\"filledDiamond\" } },");
                }
            } else {
                for (int i = 0; i < data.size(); i++) {
                    jqplotbuffer.append("{");
                    jqplotbuffer.append("lineWidth:2,");
                    jqplotbuffer.append(" markerOptions: { style:'" + markerOptions[i] + "' }");
                    jqplotbuffer.append("},");
                }
            }
            jqplotbuffer.append("],");
        }
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        if (!isFromOneView && !ishomeTab) {
            jqplotbuffer.append("title:{text: '" + title + "',fontSize: 12},");
        }
        jqplotbuffer.append("axesDefaults: { labelRenderer: $.jqplot.CanvasAxisLabelRenderer   },");
        jqplotbuffer.append("seriesDefaults: {lineWidth: 1,");
        if (graphType.equalsIgnoreCase("Line(Simple)") || graphType.equalsIgnoreCase("Line(Simple-R)")) {
            jqplotbuffer.append("markerOptions: { show: false},");
        } else {
            jqplotbuffer.append("markerOptions: { show: true},");
        }
        jqplotbuffer.append("pointLabels: { show: " + showlabels + ",formatString:'%." + measureRound + "f" + nbrFormat + ischeckedLA + "',location: 's',edgeTolerance: -15,formatter: $.jqplot.euroFormatter },");
        if (graphType.equalsIgnoreCase("Line(Dashed)")) {
            jqplotbuffer.append("linePattern:'dashed'},");
        } else if (graphType.equalsIgnoreCase("Line(Smooth)")) {
            jqplotbuffer.append("rendererOptions: { smooth:true}},");
        } else if (graphType.equalsIgnoreCase("Line") || graphType.equalsIgnoreCase("Line(Simple)") || graphType.equalsIgnoreCase("Line(Std)") || graphType.equalsIgnoreCase("Line(Simple-R)") || graphType.equalsIgnoreCase("Dot-Graph")) {
            jqplotbuffer.append("rendererOptions: { smooth:false}},");
        }

        if (legenddisplay) {
            jqplotbuffer.append(" legend: {");
            if (graphLegendLoc.equalsIgnoreCase("n") || graphLegendLoc.equalsIgnoreCase("s")) {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: " + legendsPerRow + "}},");
            } else {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer,rendererOptions: {numberColumns: 1}},");
            }
        }
        jqplotbuffer.append(" axes: {");
        jqplotbuffer.append(" xaxis: {");
        if (container.getDataTypes().get(0).equals("D")) {
            jqplotbuffer.append(" renderer: $.jqplot.DateAxisRenderer,");
        } else {
            jqplotbuffer.append(" renderer: $.jqplot.CategoryAxisRenderer,");
        }
        jqplotbuffer.append("label:\"" + showxAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        jqplotbuffer.append(" ticks:" + ticks122 + ","); //kruthika
        jqplotbuffer.append(" tickOptions:{");
        if (container.getDataTypes().get(0).equals("D")) {
            jqplotbuffer.append("formatString: '%e-%b-%y',");
        }
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append(" fontSize:'" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor:'" + this.xaxisfontcolor + "',");
        jqplotbuffer.append(" fontFamily:'" + this.fontFamily + "',");
        jqplotbuffer.append(" angle:" + labelDir + ",");
        jqplotbuffer.append(" },");
        if (container.getDataTypes().get(0).equals("D")) {
            if (datebyInterval.equalsIgnoreCase("Default")) {
                jqplotbuffer.append("min:" + ticks.get(0) + ",max:" + ticks.get(ticks.size() - 1) + ",");
            } else {
                jqplotbuffer.append("min:" + ticks.get(0) + ",max:" + ticks.get(ticks.size() - 1) + ",tickInterval:\"" + datebyInterval + " Days\",");
            }
        }
        jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" yaxis: {");
        jqplotbuffer.append("label:\"" + showlyAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        if (yaxiscalibration.equalsIgnoreCase("Custom") && !yaxisstart.isEmpty() && !yaxisend.isEmpty() && !yaxisinterval.isEmpty()) {
            jqplotbuffer.append(" pad:0,autoscale:true, min:" + yaxisstart + ",max:" + yaxisend + ",tickInterval: " + yaxisinterval + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter},");
        } else if (yaxiscalibration.equalsIgnoreCase("default") || minvalue == -1) {
            jqplotbuffer.append("tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        } else {
            jqplotbuffer.append(" pad:0, min:" + minvalue + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        }
        jqplotbuffer.append(" }");
        jqplotbuffer.append(" },");
        if (targetValue != "" && !targetValue.isEmpty()) {
            jqplotbuffer.append("canvasOverlay: { show: true,");
            jqplotbuffer.append("objects: [{horizontalLine: {name: 'pebbles',");
            jqplotbuffer.append(" y: " + targetValue + ",lineWidth: 2,color: \"" + targetcolor + "\",");
            jqplotbuffer.append("shadow: true,lineCap: 'butt',xOffset: 0,showTooltip: true,tooltipFormatString:\"" + targetValue + "\" }}, ] },");
        }
        jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'ne',tooltipAxes: 'yref',formatString: '%d" + nbrFormat + "',tooltipContentEditor:tooltipContentEditor,useAxesFormatters:false },");
        jqplotbuffer.append(" cursor: {show:true,zoom:true,showTooltip: false,},");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
        jqplotbuffer.append("});");
        jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot) ");
        jqplotbuffer.append("{");
        if (container.getDataTypes().get(0).equals("D")) {
            jqplotbuffer.append("var roundvalue=Math.round(plot.data[seriesIndex][pointIndex][1]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + ");");
            jqplotbuffer.append(" return plot.data[seriesIndex][pointIndex][0]+\":\" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + "\"");
        } else {
            jqplotbuffer.append("var pointlables=ticks" + tickId + "[pointIndex];");
            jqplotbuffer.append("var roundvalue=Math.round(plot.data[seriesIndex][pointIndex]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + ");");
            if (container.isReportCrosstab()) {
                jqplotbuffer.append("var tooltip1=tooltip" + tickId + "[seriesIndex];");
                jqplotbuffer.append(" return pointlables.toString()+\":\"+tooltip1.toString()+\":\"  +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + ischeckedLA + "\"");
            } else {
                jqplotbuffer.append(" return pointlables.toString()+\":\" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + ischeckedLA + "\"");
            }
        }
        jqplotbuffer.append("};");
//         if ((ischeckedXA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-xaxis-tick\").hide();");
//                 }
//        if ((ischeckedYA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-yaxis-tick\").hide();");
//                 }
        if (!isFromOneView) {
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, i, j, data) {");
            jqplotbuffer.append("var reportid=" + reportid + ";");
            jqplotbuffer.append(" var value=ticks" + tickId + "[j];");
            jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");

            if (showlocaldrill) {
                jqplotbuffer.append(" $.ajax({");
                jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+value+'&type=drillgrph',");
                jqplotbuffer.append(" success: function(data){");
                jqplotbuffer.append(" window.location.href=window.location.href;");
                jqplotbuffer.append("}");
                jqplotbuffer.append("});");
            } else if (drilltype.equalsIgnoreCase("report")) {
                ArrayList url = container.getLinks();
                String Url = url.get(0).toString();
                if (container.isReportCrosstab()) {
                    jqplotbuffer.append("submitformStandard('" + Url + "',value,i,j,'" + drillmap.toString() + "','" + rowviewby + "','" + columnviewby + "',tooltip" + tickId + "[i]);");
                } else {
                    jqplotbuffer.append("submitformStandard('" + Url + "',value,i,j,'" + drillmap.toString() + "');");
                }
            } else {
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    jqplotbuffer.append("submitform('" + oneUrl + "',value,'" + isAdhocEnabled + "');");
                }
            }

            jqplotbuffer.append("}) ");
        }
        //added by srikanth.p for drilling in oneView

        if (isFromOneView) {

            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, data) {");
            jqplotbuffer.append("var value=ticks" + tickId + "[pointIndex];");
            ArrayList url = container.getLinks();
            String oneUrl = "";
            if (url != null) {
                oneUrl = url.get(0).toString();
                oneUrl = oneUrl.replace("reportViewer.do?reportBy=viewReport", "dashboardViewer.do?reportBy=getOLAPGraphforOneView");
                jqplotbuffer.append("drillOLAPGraph('" + oneUrl + "',value);");
            }

            jqplotbuffer.append("});");
        }
        return jqplotbuffer;
    }

    public StringBuffer Area(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();

        //  this.tickLength(container,graphType);
        jqplotbuffer.append("(function($) {");
        jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
        jqplotbuffer.append("if (!format) {");
        jqplotbuffer.append("format = '%.1f';");
        jqplotbuffer.append(" }");
        jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
        jqplotbuffer.append("};");
        jqplotbuffer.append(" function numberWithCommas(x) {");
        jqplotbuffer.append("  if(x==0 || x==0.0 || x==0.00) return '';");
        jqplotbuffer.append(" else return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
        jqplotbuffer.append(" }");
        jqplotbuffer.append("})(jQuery);");
        jqplotbuffer.append(" var ticks" + tickId + " =" + ticks + ";"); //kruthika
        jqplotbuffer.append("var tooltip" + tickId + "=" + tooltip + ";");
        jqplotbuffer.append("var tooltipticks=" + tooltip + ";");
        jqplotbuffer.append("var plot1 = $.jqplot('" + chartId + "'," + data + ", {");
        jqplotbuffer.append(" animate: true,animateReplot: true,");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        if (!isFromOneView && !ishomeTab) {
            jqplotbuffer.append("title:{text: '" + title + "',fontSize: 12},");
        }
        jqplotbuffer.append("axesDefaults: { labelRenderer: $.jqplot.CanvasAxisLabelRenderer   },");
        jqplotbuffer.append("seriesDefaults: {fill:true,");
        jqplotbuffer.append("pointLabels: { show: " + showlabels + ",formatString:'%." + measureRound + "f" + nbrFormat + ischeckedLA + "',location: 's',edgeTolerance: -15,formatter: $.jqplot.euroFormatter },");
        jqplotbuffer.append("rendererOptions: { smooth:false}},");

        if (legenddisplay) {
            jqplotbuffer.append(" legend: {");
            if (graphLegendLoc.equalsIgnoreCase("n") || graphLegendLoc.equalsIgnoreCase("s")) {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: " + legendsPerRow + "}},");
            } else {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer,rendererOptions: {numberColumns: 1}},");
            }
        }
        this.Areatick(container, graphType);
        jqplotbuffer.append(" axes: {");
        jqplotbuffer.append(" xaxis: {");
        jqplotbuffer.append(" pad:0,");
        jqplotbuffer.append("label:\"" + showxAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        jqplotbuffer.append(" ticks:" + tickLenthComp + ",");
        jqplotbuffer.append(" tickOptions:{");
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append(" fontSize:'" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor:'" + this.xaxisfontcolor + "',");
        jqplotbuffer.append(" fontFamily:'" + this.fontFamily + "',");
        jqplotbuffer.append(" angle:" + labelDir + "");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" yaxis: {");
        jqplotbuffer.append("label:\"" + showlyAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        if (yaxiscalibration.equalsIgnoreCase("Custom") && !yaxisstart.isEmpty() && !yaxisend.isEmpty() && !yaxisinterval.isEmpty()) {
            jqplotbuffer.append(" pad:0,autoscale:true, min:" + yaxisstart + ",max:" + yaxisend + ",tickInterval: " + yaxisinterval + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter},");
        } else if (yaxiscalibration.equalsIgnoreCase("default") || minvalue == -1) {
            jqplotbuffer.append("tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        } else {
            jqplotbuffer.append(" pad:0, min:" + minvalue + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        }
        jqplotbuffer.append(" }");
        jqplotbuffer.append(" },");
        if (targetValue != "" && !targetValue.isEmpty()) {
            jqplotbuffer.append("canvasOverlay: { show: true,");
            jqplotbuffer.append("objects: [{horizontalLine: {name: 'pebbles',");
            jqplotbuffer.append(" y: " + targetValue + ",lineWidth: 2,color:  \"" + targetcolor + "\",");
            jqplotbuffer.append("shadow: true,lineCap: 'butt',xOffset: 0, showTooltip: true,tooltipFormatString:\"" + targetValue + "\" }}, ] },");
        }
        jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'ne',tooltipAxes: 'yref',formatString: '%d" + nbrFormat + ischeckedLA + "',tooltipContentEditor:tooltipContentEditor,useAxesFormatters:false },");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
        jqplotbuffer.append("});");
        jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot) ");
        jqplotbuffer.append("{");
        jqplotbuffer.append("var pointlables=ticks" + tickId + "[pointIndex];");
        jqplotbuffer.append("var roundvalue=Math.round(plot.data[seriesIndex][pointIndex]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + ");");
        if (container.isReportCrosstab()) {
            jqplotbuffer.append("var tooltip1=tooltip" + tickId + "[seriesIndex];");
            jqplotbuffer.append(" return pointlables.toString()+\":\"+tooltip1.toString()+\":\"  +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + ischeckedLA + "\"");
        } else {
            jqplotbuffer.append(" return pointlables.toString()+\":\" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + ischeckedLA + "\"");
        }
        jqplotbuffer.append("};");
//         if ((ischeckedXA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-xaxis-tick\").hide();");
//                 }
//if ((ischeckedYA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-yaxis-tick\").hide();");
//                 }
        if (!isFromOneView) {
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, i, j, data) {");
            jqplotbuffer.append("var reportid=" + reportid + ";");
            jqplotbuffer.append(" var value=ticks" + tickId + "[j];");
            jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");
            if (showlocaldrill) {
                jqplotbuffer.append(" $.ajax({");
                jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+value+'&type=drillgrph',");
                jqplotbuffer.append(" success: function(data){");
                jqplotbuffer.append(" window.location.href=window.location.href;");
                jqplotbuffer.append("}");
                jqplotbuffer.append("});");
            } else if (drilltype.equalsIgnoreCase("report")) {
                ArrayList url = container.getLinks();
                String Url = url.get(0).toString();
                if (container.isReportCrosstab()) {
                    jqplotbuffer.append("submitformStandard('" + Url + "',value,i,j,'" + drillmap.toString() + "','" + rowviewby + "','" + columnviewby + "',tooltip" + tickId + "[i]);");
                } else {
                    jqplotbuffer.append("submitformStandard('" + Url + "',value,i,j,'" + drillmap.toString() + "');");
                }

            } else {
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    jqplotbuffer.append("submitform('" + oneUrl + "',value,'" + isAdhocEnabled + "');");
                }
            }

            jqplotbuffer.append("}) ");
        }
        //added by srikanth.p for drilling in oneView

        if (isFromOneView) {
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, data) {");
            jqplotbuffer.append("var value=ticks" + tickId + "[pointIndex];");
            ArrayList url = container.getLinks();
            String oneUrl = "";
            if (url != null) {
                oneUrl = url.get(0).toString();
                oneUrl = oneUrl.replace("reportViewer.do?reportBy=viewReport", "dashboardViewer.do?reportBy=getOLAPGraphforOneView");
                jqplotbuffer.append("drillOLAPGraph('" + oneUrl + "',value);");
            }

            jqplotbuffer.append("});");

        }
        return jqplotbuffer;
    }

    public StringBuffer BarHorizontal(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
        this.tickLength(container, graphType);
        jqplotbuffer.append("(function($) {");
        jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
        jqplotbuffer.append("if (!format) {");
        jqplotbuffer.append("format = '%.1f';");
        jqplotbuffer.append(" }");
        jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
        jqplotbuffer.append("};");
        jqplotbuffer.append(" function numberWithCommas(x) {");
        jqplotbuffer.append("  if(x==0 || x==0.0 || x==0.00) return '';");
        jqplotbuffer.append(" else return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
        jqplotbuffer.append(" }");

        jqplotbuffer.append("})(jQuery);");
        jqplotbuffer.append(" var ticks" + tickId + " =" + tickLenthComp + ";"); //kruthika
        jqplotbuffer.append("var tooltip" + tickId + "=" + tooltip + ";");
        jqplotbuffer.append("var plot1 = $.jqplot('" + chartId + "'," + data + ", {");
        jqplotbuffer.append(" animate: true,");
        jqplotbuffer.append(" animateReplot: true,");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        if (!isFromOneView && !ishomeTab) {
            jqplotbuffer.append("title:{text: '" + title + "',fontSize: 12},");
        }
        jqplotbuffer.append(" seriesDefaults: {");
        jqplotbuffer.append(" renderer:$.jqplot.BarRenderer,");
        jqplotbuffer.append("pointLabels: { show: " + showlabels + ",formatString:'%." + measureRound + "f" + nbrFormat + ischeckedRA + "',location: 's',edgeTolerance: -15,formatter: $.jqplot.euroFormatter },");
        jqplotbuffer.append("shadowAngle: 135,");
        jqplotbuffer.append(" rendererOptions: {");
        if (!container.getSortColumns().contains(viewByColumns[0])) {
            jqplotbuffer.append("varyBarColor: " + varyBarColor + ",");
        }
        jqplotbuffer.append("fillToZero: true, useNegativeColors: false, ");
        jqplotbuffer.append("barDirection: 'horizontal' ");
        jqplotbuffer.append("}");
        jqplotbuffer.append("},");
        jqplotbuffer.append(" axes: {");
        jqplotbuffer.append(" xaxis: {");
        jqplotbuffer.append(" pad:0,");
        //  jqplotbuffer.append("renderer: $.jqplot.CategoryAxisRenderer,");

        jqplotbuffer.append("label:\"" + showxAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        jqplotbuffer.append(" tickOptions:{");
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append(" fontSize:'" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor:'" + this.xaxisfontcolor + "',");
        jqplotbuffer.append(" fontFamily:'" + this.fontFamily + "',");
        jqplotbuffer.append(" angle:" + this.xaxisangle + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedRA + "',formatter: $.jqplot.euroFormatter");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
        jqplotbuffer.append(" },");
        jqplotbuffer.append("yaxis: {");
        jqplotbuffer.append("label:\"" + showlyAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        jqplotbuffer.append("pad:0,");
        jqplotbuffer.append("renderer: $.jqplot.CategoryAxisRenderer,");
        jqplotbuffer.append("ticks:" + ticks122 + ",");//krk
        jqplotbuffer.append(" tickOptions:{");
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append(" fontSize:'" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor:'" + this.xaxisfontcolor + "',");
        jqplotbuffer.append(" fontFamily:'" + this.fontFamily + "',");
        jqplotbuffer.append(" angle:" + labelDir + ",");
//            jqplotbuffer.append(" formatter: function(format, value) {");
//            jqplotbuffer.append("var tick=ticks"+tickId+"[value-1];");
//            jqplotbuffer.append("if(tick===undefined){ return \"\"; } else{");
//               if (viewByColumns.length >= 2 && !isappendXaxis) {
//                jqplotbuffer.append("if(tick!=undefined) var tick=tick.toString().substring(tick.toString().indexOf(\",\")+1);");
//            }
//            jqplotbuffer.append("if(tick.length>15) return tick.substring(0,15)+\"...\";  else return tick;}} ");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");

        jqplotbuffer.append("}");
        jqplotbuffer.append("},");
        if (targetValue != "" && !targetValue.isEmpty()) {
            jqplotbuffer.append("canvasOverlay: { show: true,");
            jqplotbuffer.append("objects: [{verticalLine: {name: 'pebbles',");
            jqplotbuffer.append(" x: " + targetValue + ",lineWidth: 2,color: \"" + targetcolor + "\",");
            jqplotbuffer.append("shadow: true,lineCap: 'butt',xOffset: 0,showTooltip: true,tooltipFormatString:\"" + targetValue + "\" }}, ] },");
        }
        jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'n',tooltipAxes: 'yref',tooltipContentEditor:tooltipContentEditor,useAxesFormatters:false },");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0},");
        if (legenddisplay) {
            jqplotbuffer.append(" legend: {");
            if (graphLegendLoc.equalsIgnoreCase("n") || graphLegendLoc.equalsIgnoreCase("s")) {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: " + legendsPerRow + "}},");
            } else {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ",renderer:$.jqplot.EnhancedLegendRenderer,rendererOptions: {numberColumns: 1}},");
            }
        }
        jqplotbuffer.append("});");
        jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot) ");
        jqplotbuffer.append("{");
        jqplotbuffer.append("var pointlables=ticks" + tickId + "[pointIndex];");
        jqplotbuffer.append("var roundvalue=Math.round(plot.data[seriesIndex][pointIndex]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + ");");
        jqplotbuffer.append(" return pointlables.toString()+\":\" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + ischeckedRA + "\"");
        jqplotbuffer.append("};");
        if ((ischeckedXA).equalsIgnoreCase("true")) {
            jqplotbuffer.append("$('#" + chartId + " .jqplot-xaxis-tick').hide();");
//                  jqplotbuffer.append("$(\".jqplot-xaxis-tick\").hide();");
        }
//            if ((ischeckedYA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-yaxis-tick\").hide();");
//                 }
        if (!isFromOneView) {
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, i, j, data) {");
            jqplotbuffer.append("var reportid=" + reportid + ";");
            jqplotbuffer.append(" var value=ticks" + tickId + "[j];");
            jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");

            if (showlocaldrill) {
                jqplotbuffer.append(" $.ajax({");
                jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+value+'&type=drillgrph',");
                jqplotbuffer.append(" success: function(data){");
                jqplotbuffer.append(" window.location.href=window.location.href;");
                jqplotbuffer.append("}");
                jqplotbuffer.append("});");
            } else if (drilltype.equalsIgnoreCase("report")) {
                ArrayList url = container.getLinks();
                String Url = url.get(0).toString();
                if (container.isReportCrosstab()) {
                    jqplotbuffer.append("submitformStandard('" + Url + "',ticks" + tickId + "[j],i,j,'" + drillmap.toString() + "','" + rowviewby + "','" + columnviewby + "',tooltip" + tickId + "[i]);");
                } else {
                    jqplotbuffer.append("submitformStandard('" + Url + "',ticks" + tickId + "[j],i,j,'" + drillmap.toString() + "');");
                }
            } else {
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    jqplotbuffer.append("submitform('" + oneUrl + "',ticks" + tickId + "[j],'" + isAdhocEnabled + "');");
                }
            }
            jqplotbuffer.append("}) ");
        }
        //added by srikanth.p for drilling in oneView

        if (isFromOneView) {
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, data) {");
            jqplotbuffer.append("var value=ticks" + tickId + "[pointIndex];");
            ArrayList url = container.getLinks();
            String oneUrl = "";
            if (url != null) {
                oneUrl = url.get(0).toString();
                oneUrl = oneUrl.replace("reportViewer.do?reportBy=viewReport", "dashboardViewer.do?reportBy=getOLAPGraphforOneView");
                jqplotbuffer.append("drillOLAPGraph('" + oneUrl + "',value);");
            }
            jqplotbuffer.append("});");
        }

        return jqplotbuffer;
    }

    public StringBuffer AreaLine(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();

        jqplotbuffer.append("(function($) {");
        jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
        jqplotbuffer.append("if (!format) {");
        jqplotbuffer.append("format = '%.1f';");
        jqplotbuffer.append(" }");
        jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
        jqplotbuffer.append("};");
        jqplotbuffer.append(" function numberWithCommas(x) {");
        jqplotbuffer.append("  if(x==0 || x==0.0 || x==0.00) return '';");
        jqplotbuffer.append(" else return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
        jqplotbuffer.append(" }");

        jqplotbuffer.append("})(jQuery);");
        jqplotbuffer.append(" var ticks" + tickId + " =" + ticks + ";");//krk
        jqplotbuffer.append("var tooltip" + tickId + "=" + tooltip + ";");
        jqplotbuffer.append("var plot1 = $.jqplot('" + chartId + "'," + data + ", {");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        jqplotbuffer.append("highlighter: {");
        jqplotbuffer.append("show: true,");
        jqplotbuffer.append("sizeAdjust: 1,");
        jqplotbuffer.append("tooltipOffset: -9");
        jqplotbuffer.append("},");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0},");
        if (legenddisplay) {
            jqplotbuffer.append(" legend: {");
            if (graphLegendLoc.equalsIgnoreCase("n") || graphLegendLoc.equalsIgnoreCase("s")) {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: " + legendsPerRow + "}},");
            } else {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer,rendererOptions: {numberColumns: 1}},");
            }
        }
        this.Areatick(container, graphType);
        jqplotbuffer.append("seriesDefaults: {");
        jqplotbuffer.append("pointLabels: { show: " + showlabels + ",formatString:'%." + measureRound + "f" + nbrFormat + "',location: 's',edgeTolerance: -15,formatter: $.jqplot.euroFormatter },");
        jqplotbuffer.append("rendererOptions: {");
        jqplotbuffer.append("smooth: false,");
        jqplotbuffer.append("animation: {");
        jqplotbuffer.append("show: true");
        jqplotbuffer.append("}");
        jqplotbuffer.append("},");
        jqplotbuffer.append("showMarker: false");
        jqplotbuffer.append("},");
        //jqplotbuffer.append("series: [{fill: true,label: ''},{fill: true,label: ''}],");
        jqplotbuffer.append("series: [{fill: true,label: ''}],");
        jqplotbuffer.append("axesDefaults: {rendererOptions: { baselineWidth: 1.5,baselineColor: '#444444',drawBaseline:false}},");
        jqplotbuffer.append("axes: {");
        jqplotbuffer.append(" xaxis: {");
        jqplotbuffer.append(" pad:0,");
        jqplotbuffer.append("label:\"" + showxAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        jqplotbuffer.append(" ticks:" + tickLenthComp + ",");
        jqplotbuffer.append(" tickOptions:{");
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append(" fontSize:'" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor:'" + this.xaxisfontcolor + "',");
        jqplotbuffer.append(" fontFamily:'" + this.fontFamily + "',");
        jqplotbuffer.append(" angle:" + labelDir + "");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" yaxis: {");
        jqplotbuffer.append("label:\"" + showlyAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        if (yaxiscalibration.equalsIgnoreCase("Custom") && !yaxisstart.isEmpty() && !yaxisend.isEmpty() && !yaxisinterval.isEmpty()) {
            jqplotbuffer.append(" pad:0,autoscale:true, min:" + yaxisstart + ",max:" + yaxisend + ",tickInterval: " + yaxisinterval + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter},");
        } else if (yaxiscalibration.equalsIgnoreCase("default") || minvalue == -1) {
            jqplotbuffer.append(" pad:0, tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        } else {
            jqplotbuffer.append(" pad:0, min:" + minvalue + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        }
        jqplotbuffer.append(" }");
        jqplotbuffer.append("}");
//            if ((ischeckedXA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-xaxis-tick\").hide();");
//                 }
//            if ((ischeckedYA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-yaxis-tick\").hide();");
//                 }
        jqplotbuffer.append("});");
        // jqplotbuffer.append("$('.jqplot-highlighter-tooltip').addClass('ui-corner-all')");

        return jqplotbuffer;
    }

    public StringBuffer Bubble(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
        jqplotbuffer.append("(function($) {");
        jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
        jqplotbuffer.append("if (!format) {");
        jqplotbuffer.append("format = '%.1f';");
        jqplotbuffer.append(" }");
        jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
        jqplotbuffer.append("};");
        jqplotbuffer.append(" function numberWithCommas(x) {");
        jqplotbuffer.append("  if(x==0 || x==0.0 || x==0.00) return '';");
        jqplotbuffer.append(" else return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
        jqplotbuffer.append(" }");
        jqplotbuffer.append("})(jQuery);");
        jqplotbuffer.append("var data=" + data + ";");
        jqplotbuffer.append(" var ticks" + tickId + " =" + ticks + ";");
        jqplotbuffer.append("plot1 = $.jqplot('" + chartId + "',[data],{");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        if (!isFromOneView && !ishomeTab) {
            jqplotbuffer.append("title:{text: '" + title + "',fontSize: 12},");
        }
        jqplotbuffer.append("captureRightClick: true,seriesDefaults:{renderer: $.jqplot.BubbleRenderer,rendererOptions: {bubbleGradients: true}},");
        jqplotbuffer.append(" axes: {");
        jqplotbuffer.append(" xaxis: {");
        jqplotbuffer.append("label:\"" + showxAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        if (yaxiscalibration.equalsIgnoreCase("Custom") && !xaxisstart.isEmpty() && !xaxisend.isEmpty() && !xaxisinterval.isEmpty()) {
            jqplotbuffer.append("autoscale:true, min:" + xaxisstart + ",max:" + xaxisend + ",tickInterval:" + xaxisinterval + " ,tickOptions:{");
        } else {
            jqplotbuffer.append(" min:0,tickOptions:{");
        }
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append("formatString:'%." + xAxisRounding + "f" + nbrFormat + ischeckedRA + "',fontSize:'" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor:'" + this.xaxisfontcolor + "',");
        jqplotbuffer.append(" fontFamily:'" + this.fontFamily + "',");
        jqplotbuffer.append(" angle:" + labelDir + ",formatter: $.jqplot.euroFormatter");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" yaxis: {");
        jqplotbuffer.append("label:\"" + showlyAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        if (yaxiscalibration.equalsIgnoreCase("Custom") && !yaxisstart.isEmpty() && !yaxisend.isEmpty() && !yaxisinterval.isEmpty()) {
            jqplotbuffer.append(" pad:0,autoscale:true, min:" + yaxisstart + ",max:" + yaxisend + ",tickInterval: " + yaxisinterval + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter},");
        } else if (yaxiscalibration.equalsIgnoreCase("default") || minvalue == -1) {
            jqplotbuffer.append(" tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        } else {
            jqplotbuffer.append(" pad:0, min:0,tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        }
        jqplotbuffer.append(" }");
        jqplotbuffer.append(" },");
        if (targetValue != "" && !targetValue.isEmpty()) {
            jqplotbuffer.append("canvasOverlay: { show: true,");
            jqplotbuffer.append("objects: [{horizontalLine: {name: 'pebbles',");
            jqplotbuffer.append(" y: " + targetValue + ",lineWidth: 2,color: \"" + targetcolor + "\",");
            jqplotbuffer.append("shadow: true,lineCap: 'butt',xOffset: 0,showTooltip: true,tooltipFormatString:\"" + targetValue + "\" }}, ] },");
        }
        jqplotbuffer.append(" cursor: {show:true,zoom:true,showTooltip: true,showTooltipUnitPosition:false, useAxesFormatters:true, followMouse:true},");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
        jqplotbuffer.append("});");
//            if ((ischeckedXA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-xaxis-tick\").hide();");
//                 }
//            if ((ischeckedYA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-yaxis-tick\").hide();");
//                 }
        jqplotbuffer.append("$(\"#" + chartId + "\").bind('jqplotDataMouseOver',function (ev, seriesIndex, pointIndex, data){   ");
        jqplotbuffer.append(" var valueRound= Math.round(data[0]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + "); var valueRound1= Math.round(data[1]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + "); var valueRound2= Math.round(data[2]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + ");");
        jqplotbuffer.append("var value=data[3]+':  '+valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+ ':  ' + valueRound1.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+ ':  ' + valueRound2.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");");
        jqplotbuffer.append("$(\".jqplot-cursor-tooltip\").html(value +\"" + nbrFormat + "\"); });");
        jqplotbuffer.append("$(\"#" + chartId + "\").bind('jqplotDataRightClick',function (ev, seriesIndex, pointIndex, data, radius){ ");
        jqplotbuffer.append("var x= (Math.round(data[0]*Math.pow(10,0))/Math.pow(10,0)).toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");");
        jqplotbuffer.append("var y= (Math.round(data[1]*Math.pow(10,0))/Math.pow(10,0)).toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");");
        jqplotbuffer.append("var r= (Math.round(data[2]*Math.pow(10,0))/Math.pow(10,0)).toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");");
        jqplotbuffer.append(" var color = 'rgb(0,0,0)';");
        jqplotbuffer.append(" $('.jqplot-cursor-tooltip').html('<span style=\"font-size:10px;font-weight:bold;color:' +");
        jqplotbuffer.append("color + ';\">' + data[3] + '</span><br/>'+'<table><tr><td>' +'" + legendlabels.get(0).toString().replace("\"", "") + ": ' +x+");
        jqplotbuffer.append("'<br/>' + '" + legendlabels.get(1).toString().replace("\"", "") + ": ' + y+ '<br/>' + '" + legendlabels.get(2).toString().replace("\"", "") + ": ' + r)+'</td></tr></table>';});");
        if (!isFromOneView) {
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, i, j, data) {");
            jqplotbuffer.append("var reportid=" + reportid + ";");
            jqplotbuffer.append(" var value=ticks" + tickId + "[j];");
            jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");

            if (showlocaldrill) {
                jqplotbuffer.append(" $.ajax({");
                jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+value+'&type=drillgrph',");
                jqplotbuffer.append(" success: function(data){");
                jqplotbuffer.append(" window.location.href=window.location.href;");
                jqplotbuffer.append("}");
                jqplotbuffer.append("});");
            } else if (drilltype.equalsIgnoreCase("report")) {
                ArrayList url = container.getLinks();
                String Url = url.get(0).toString();
                jqplotbuffer.append("submitformStandard('" + Url + "',value,i,j,'" + drillmap + "');");
            } else {
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    jqplotbuffer.append("submitform('" + oneUrl + "',value,'" + isAdhocEnabled + "');");
                }
            }

            jqplotbuffer.append("}) ");
        }
        return jqplotbuffer;
    }

    public StringBuffer Scatter(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
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
        jqplotbuffer.append("var data=" + data + ";");
        jqplotbuffer.append(" var ticks" + tickId + " =" + ticks + ";");
        jqplotbuffer.append("plot1 = $.jqplot('" + chartId + "',[data],{");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        jqplotbuffer.append("  animate:true,seriesDefaults:{pointLabels:{ show:false, location:'s' }}, series:[   ");
        jqplotbuffer.append(" { showLine:false, markerOptions: { shadow: false, size: 2, style:\"circle\" } },],");
        jqplotbuffer.append(" axes:{");
        jqplotbuffer.append(" xaxis: {");
        jqplotbuffer.append("label:\"" + showxAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        if (yaxiscalibration.equalsIgnoreCase("Custom") && !xaxisstart.isEmpty() && !xaxisend.isEmpty() && !xaxisinterval.isEmpty()) {
            jqplotbuffer.append("autoscale:true, min:" + xaxisstart + ",max:" + xaxisend + ",tickInterval:" + xaxisinterval + " ,tickOptions:{");
        } else {
            jqplotbuffer.append(" min:0,tickOptions:{");
        }
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append(" formatString:'%." + xAxisRounding + "f" + nbrFormat + ischeckedRA + "',fontSize:'" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor:'" + this.xaxisfontcolor + "',");
        jqplotbuffer.append(" fontFamily:'" + this.fontFamily + "',");
        jqplotbuffer.append(" angle:" + labelDir + ",formatter: $.jqplot.euroFormatter");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" yaxis: {");
        jqplotbuffer.append("label:\"" + showlyAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        if (yaxiscalibration.equalsIgnoreCase("Custom") && !yaxisstart.isEmpty() && !yaxisend.isEmpty() && !yaxisinterval.isEmpty()) {
            jqplotbuffer.append(" pad:0,autoscale:true, min:" + yaxisstart + ",max:" + yaxisend + ",tickInterval: " + yaxisinterval + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter},");
        } else if (yaxiscalibration.equalsIgnoreCase("default") || minvalue == -1) {
            jqplotbuffer.append(" tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        } else {
            jqplotbuffer.append(" pad:0, numberTicks:16,min:5,tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        }
        jqplotbuffer.append(" }},");
        if (targetValue != "" && !targetValue.isEmpty()) {
            jqplotbuffer.append("canvasOverlay: { show: true,");
            jqplotbuffer.append("objects: [{horizontalLine: {name: 'pebbles',");
            jqplotbuffer.append(" y: " + targetValue + ",lineWidth: 2,color:  \"" + targetcolor + "\",");
            jqplotbuffer.append("shadow: true,lineCap: 'butt',xOffset: 0,showTooltip: true,tooltipFormatString:\"" + targetValue + "\" }}, ] },");
        }
        jqplotbuffer.append(" cursor: {show:true,zoom:true,showTooltip: true,showTooltipUnitPosition:false, useAxesFormatters:true, followMouse:true},");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
        jqplotbuffer.append("});");
//            if ((ischeckedXA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-xaxis-tick\").hide();");
//                 }
//            if ((ischeckedYA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-yaxis-tick\").hide();");
//                 }
        jqplotbuffer.append("$(\"#" + chartId + "\").bind('jqplotDataMouseOver',function (ev, seriesIndex, pointIndex, data){   ");
        if (legendlabels.size() > 1) {
            jqplotbuffer.append(" var valueRound= Math.round(data[1]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + "); var valueRound1= Math.round(data[0]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + ");");
            jqplotbuffer.append("var value=data[2]+':  '+valueRound1.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+ ':  ' + valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");");
            jqplotbuffer.append("$(\".jqplot-cursor-tooltip\").html(value +\"" + nbrFormat + ischeckedLA + "\"); });");
        } else if (legendlabels.size() == 1) {
            jqplotbuffer.append("var valueRound= Math.round(data[1]*Math.pow(10,0))/Math.pow(10,0); var value=ticks" + tickId + "[pointIndex]+':  ' + valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");");
            jqplotbuffer.append("$(\".jqplot-cursor-tooltip\").html(value +\"\"); });");
        }
        if (!isFromOneView) {
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, i, j, data) {");
            jqplotbuffer.append("var reportid=" + reportid + ";");
            jqplotbuffer.append(" var value=data[2];");
            jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");

            if (showlocaldrill) {
                jqplotbuffer.append(" $.ajax({");
                jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+value+'&type=drillgrph',");
                jqplotbuffer.append(" success: function(data){");
                jqplotbuffer.append(" window.location.href=window.location.href;");
                jqplotbuffer.append("}");
                jqplotbuffer.append("});");
            } else if (drilltype.equalsIgnoreCase("report")) {
                ArrayList url = container.getLinks();
                String Url = url.get(0).toString();
                jqplotbuffer.append("submitformStandard('" + Url + "',value,i,j,'" + drillmap + "');");
            } else {
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    jqplotbuffer.append("submitform('" + oneUrl + "',value,'" + isAdhocEnabled + "');");
                }
            }

            jqplotbuffer.append("}) ");
        }
        return jqplotbuffer;
    }

    public StringBuffer ScatterRegression(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
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
        jqplotbuffer.append("var data=" + data + ";");
        jqplotbuffer.append(" var ticks" + tickId + " =" + ticks + ";");
        jqplotbuffer.append("plot1 = $.jqplot('" + chartId + "',[data],{");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        jqplotbuffer.append("  animate:true,seriesDefaults:{pointLabels:{ show:false, location:'s' }}, series:[   ");
        jqplotbuffer.append(" { showLine:false,trendline: { show:true,  color: '#667c26'  }, markerOptions: { shadow: false, size: 2, style:\"circle\" } },],");
        jqplotbuffer.append(" axes:{");
        jqplotbuffer.append(" xaxis: {");
        jqplotbuffer.append("label:\"" + showxAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        if (yaxiscalibration.equalsIgnoreCase("Custom") && !xaxisstart.isEmpty() && !xaxisend.isEmpty() && !xaxisinterval.isEmpty()) {
            jqplotbuffer.append("autoscale:true, min:" + xaxisstart + ",max:" + xaxisend + ",tickInterval:" + xaxisinterval + " ,tickOptions:{");
        } else {
            jqplotbuffer.append(" min:0,tickOptions:{");
        }
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append(" formatString:'%." + xAxisRounding + "f" + nbrFormat + ischeckedRA + "',fontSize:'" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor:'" + this.xaxisfontcolor + "',");
        jqplotbuffer.append(" fontFamily:'" + this.fontFamily + "',");
        jqplotbuffer.append(" angle:" + labelDir + ",formatter: $.jqplot.euroFormatter");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" yaxis: {");
        jqplotbuffer.append("label:\"" + showlyAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        if (yaxiscalibration.equalsIgnoreCase("Custom") && !yaxisstart.isEmpty() && !yaxisend.isEmpty() && !yaxisinterval.isEmpty()) {
            jqplotbuffer.append(" pad:0,autoscale:true, min:" + yaxisstart + ",max:" + yaxisend + ",tickInterval: " + yaxisinterval + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter},");
        } else if (yaxiscalibration.equalsIgnoreCase("default") || minvalue == -1) {
            jqplotbuffer.append(" tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        } else {
            jqplotbuffer.append(" pad:0, numberTicks:16,min:5,tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        }
        jqplotbuffer.append(" }},");
        if (targetValue != "" && !targetValue.isEmpty()) {
            jqplotbuffer.append("canvasOverlay: { show: true,");
            jqplotbuffer.append("objects: [{horizontalLine: {name: 'pebbles',");
            jqplotbuffer.append(" y: " + targetValue + ",lineWidth: 2,color:  \"" + targetcolor + "\",");
            jqplotbuffer.append("shadow: true,lineCap: 'butt',xOffset: 0,showTooltip: true,tooltipFormatString:\"" + targetValue + "\" }}, ] },");
        }
        jqplotbuffer.append(" cursor: {show:true,zoom:true,showTooltip: true,showTooltipUnitPosition:false, useAxesFormatters:true, followMouse:true},");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
        jqplotbuffer.append("});");
//            if ((ischeckedXA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-xaxis-tick\").hide();");
//                 }
//            if ((ischeckedYA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-yaxis-tick\").hide();");
//                 }
        jqplotbuffer.append("$(\"#" + chartId + "\").bind('jqplotDataMouseOver',function (ev, seriesIndex, pointIndex, data){   ");
        if (legendlabels.size() > 1) {
            jqplotbuffer.append(" var valueRound= Math.round(data[1]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + "); var valueRound1= Math.round(data[0]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + ");");
            jqplotbuffer.append("var value=data[2]+':  '+valueRound1.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+ ':  ' + valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");");
            jqplotbuffer.append("$(\".jqplot-cursor-tooltip\").html(value +\"" + nbrFormat + ischeckedLA + "\"); });");
        } else if (legendlabels.size() == 1) {
            jqplotbuffer.append("var valueRound= Math.round(data[1]*Math.pow(10,0))/Math.pow(10,0); var value=ticks" + tickId + "[pointIndex]+':  ' + valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");");
            jqplotbuffer.append("$(\".jqplot-cursor-tooltip\").html(value +\"\"); });");
        }
        if (!isFromOneView) {
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, i, j, data) {");
            jqplotbuffer.append("var reportid=" + reportid + ";");
            jqplotbuffer.append(" var value=data[2];");
            jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");

            if (showlocaldrill) {
                jqplotbuffer.append(" $.ajax({");
                jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+value+'&type=drillgrph',");
                jqplotbuffer.append(" success: function(data){");
                jqplotbuffer.append(" window.location.href=window.location.href;");
                jqplotbuffer.append("}");
                jqplotbuffer.append("});");
            } else if (drilltype.equalsIgnoreCase("report")) {
                ArrayList url = container.getLinks();
                String Url = url.get(0).toString();
                jqplotbuffer.append("submitformStandard('" + Url + "',value,i,j,'" + drillmap + "');");
            } else {
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    jqplotbuffer.append("submitform('" + oneUrl + "',value,'" + isAdhocEnabled + "');");
                }
            }

            jqplotbuffer.append("}) ");
        }
        return jqplotbuffer;

    }

    public StringBuffer StackedBarHorizontal(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
        this.tickLength(container, graphType);
        jqplotbuffer.append("(function($) {");
        jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
        jqplotbuffer.append("if (!format) {");
        jqplotbuffer.append("format = '%.1f';");
        jqplotbuffer.append(" }");
        jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
        jqplotbuffer.append("};");
        jqplotbuffer.append(" function numberWithCommas(x) {");
        jqplotbuffer.append("  if(x==0 || x==0.0 || x==0.00) return '';");
        jqplotbuffer.append(" else return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
        jqplotbuffer.append(" }");

        jqplotbuffer.append("})(jQuery);");
        jqplotbuffer.append(" var ticks" + tickId + " =" + tickLenthComp + ";");//krk
        jqplotbuffer.append("var tooltip" + tickId + "=" + tooltip + ";");
        jqplotbuffer.append("var plot1 = $.jqplot('" + chartId + "'," + data + ", {");
        jqplotbuffer.append(" stackSeries: true,");
        jqplotbuffer.append(" animate: true,");
        jqplotbuffer.append(" animateReplot: true,");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        if (!isFromOneView && !ishomeTab) {
            jqplotbuffer.append("title:{text: '" + title + "',fontSize: 12},");
        }
        jqplotbuffer.append(" seriesDefaults: {");
        jqplotbuffer.append(" renderer:$.jqplot.BarRenderer,");
        jqplotbuffer.append("pointLabels: { show: " + showlabels + ",formatString:'%." + measureRound + "f" + nbrFormat + ischeckedRA + "',location: 's',edgeTolerance: -15,formatter: $.jqplot.euroFormatter },");
        jqplotbuffer.append("shadowAngle: 135,");
        jqplotbuffer.append(" rendererOptions: {");
        jqplotbuffer.append("fillToZero: true, useNegativeColors: false, ");
        jqplotbuffer.append("barDirection: 'horizontal'");
        jqplotbuffer.append("}");
        jqplotbuffer.append("},");
        jqplotbuffer.append(" axes: {");
        jqplotbuffer.append(" xaxis: {");
        jqplotbuffer.append("label:\"" + showxAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        //jqplotbuffer.append(" renderer: $.jqplot.CategoryAxisRenderer,");
//            jqplotbuffer.append(" ticks:ticks,");
        jqplotbuffer.append(" tickOptions:{");
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append(" fontSize:'" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor:'" + this.xaxisfontcolor + "',");
        jqplotbuffer.append(" fontFamily:'" + this.fontFamily + "',");
        jqplotbuffer.append(" angle:" + this.xaxisangle + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedRA + "',formatter: $.jqplot.euroFormatter");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
        jqplotbuffer.append(" },");
        jqplotbuffer.append("yaxis: {");
        jqplotbuffer.append("label:\"" + showlyAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        jqplotbuffer.append("renderer: $.jqplot.CategoryAxisRenderer,");
        jqplotbuffer.append(" ticks:" + ticks122 + ",");//krk
        jqplotbuffer.append(" tickOptions:{");
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append(" fontSize:'" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor:'" + this.xaxisfontcolor + "',");
        jqplotbuffer.append(" fontFamily:'" + this.fontFamily + "',");
        jqplotbuffer.append(" angle:" + labelDir + ",");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");

        jqplotbuffer.append("}");
        jqplotbuffer.append("},");
//            if ((ischeckedXA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-xaxis-tick\").hide();");
//                 }
//            if ((ischeckedYA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-yaxis-tick\").hide();");
//                 }
        if (targetValue != "" && !targetValue.isEmpty()) {
            jqplotbuffer.append("canvasOverlay: { show: true,");
            jqplotbuffer.append("objects: [{verticalLine: {name: 'pebbles',");
            jqplotbuffer.append(" x: " + targetValue + ",lineWidth: 2,color: \"" + targetcolor + "\",");
            jqplotbuffer.append("shadow: true,lineCap: 'butt',xOffset: 0,showTooltip: true,tooltipFormatString:\"" + targetValue + "\" }}, ] },");
        }
        jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'n',tooltipAxes: 'yref',tooltipContentEditor:tooltipContentEditor,useAxesFormatters:false },");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0},");
        if (legenddisplay) {
            jqplotbuffer.append(" legend: {");
            if (graphLegendLoc.equalsIgnoreCase("n") || graphLegendLoc.equalsIgnoreCase("s")) {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: " + legendsPerRow + "}},");
            } else {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ",renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: 1}},");
            }
        }
        jqplotbuffer.append("});");
        jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot) ");
        jqplotbuffer.append("{");
        jqplotbuffer.append("var pointlables=ticks" + tickId + "[pointIndex];");
        jqplotbuffer.append("var roundvalue=Math.round(plot.data[seriesIndex][pointIndex]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + ");");
        if (container.isReportCrosstab()) {
            jqplotbuffer.append("var tooltip1=tooltip" + tickId + "[seriesIndex];");
            jqplotbuffer.append(" return pointlables.toString()+\",\"+tooltip1.toString()+\":\"  +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + ischeckedRA + "\"");
        } else {
            jqplotbuffer.append(" return pointlables.toString()+\":\" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + ischeckedRA + "\"");
        }
        jqplotbuffer.append("};");
        if (!isFromOneView) {
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, i, j, data) {");
            jqplotbuffer.append("var reportid=" + reportid + ";");
            jqplotbuffer.append(" var value=ticks" + tickId + "[j];");
            jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");

            if (showlocaldrill) {

                jqplotbuffer.append(" $.ajax({");
                jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+value+'&type=drillgrph',");
                jqplotbuffer.append(" success: function(data){");
                jqplotbuffer.append(" window.location.href=window.location.href;");
                jqplotbuffer.append("}");
                jqplotbuffer.append("});");
            } else if (drilltype.equalsIgnoreCase("report")) {
                ArrayList url = container.getLinks();
                String Url = url.get(0).toString();
                if (container.isReportCrosstab()) {
                    jqplotbuffer.append("submitformStandard('" + Url + "',ticks" + tickId + "[j],i,j,'" + drillmap.toString() + "','" + rowviewby + "','" + columnviewby + "',tooltip" + tickId + "[i]);");
                } else {
                    jqplotbuffer.append("submitformStandard('" + Url + "',ticks" + tickId + "[j],i,j,'" + drillmap.toString() + "');");
                }
            } else {
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    jqplotbuffer.append("submitform('" + oneUrl + "',ticks" + tickId + "[j],'" + isAdhocEnabled + "');");
                }
            }

            jqplotbuffer.append("}) ");
        }
        //added by srikanth.p for drilling in oneView

        if (isFromOneView) {

            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, data) {");
//                   jqplotbuffer.append("alert('seriesIndex'+seriesIndex+' pointIndex'+pointIndex+' data'+data+' value:'+ticks[pointIndex]);");
            jqplotbuffer.append("var value=ticks" + tickId + "[pointIndex];");
            ArrayList url = container.getLinks();
            String oneUrl = "";
            if (url != null) {
                oneUrl = url.get(0).toString();
                oneUrl = oneUrl.replace("reportViewer.do?reportBy=viewReport", "dashboardViewer.do?reportBy=getOLAPGraphforOneView");
                jqplotbuffer.append("drillOLAPGraph('" + oneUrl + "',value);");
            }

            jqplotbuffer.append("});");
        }
        return jqplotbuffer;

    }

    public StringBuffer StackedArea(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();

        jqplotbuffer.append("(function($) {");
        jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
        jqplotbuffer.append("if (!format) {");
        jqplotbuffer.append("format = '%.1f';");
        jqplotbuffer.append(" }");
        jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
        jqplotbuffer.append("};");
        jqplotbuffer.append(" function numberWithCommas(x) {");
        jqplotbuffer.append("  if(x==0 || x==0.0 || x==0.00) return '';");
        jqplotbuffer.append(" else return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
        jqplotbuffer.append(" }");
        jqplotbuffer.append("})(jQuery);");
        jqplotbuffer.append(" var ticks" + tickId + " =" + ticks + ";");//krk
        jqplotbuffer.append("var tooltip" + tickId + "=" + tooltip + ";");
        jqplotbuffer.append("var tooltipticks=" + tooltip + ";");
        jqplotbuffer.append("var plot1 = $.jqplot('" + chartId + "'," + data + ", {");


        jqplotbuffer.append(" animate: true,animateReplot: true,");
        jqplotbuffer.append("stackSeries:true,");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        if (!isFromOneView && !ishomeTab) {
            jqplotbuffer.append("title:{text: '" + title + "',fontSize: 12},");
        }
        jqplotbuffer.append("axesDefaults: { labelRenderer: $.jqplot.CanvasAxisLabelRenderer   },");
        jqplotbuffer.append("seriesDefaults: {fill:true,");
        jqplotbuffer.append("pointLabels: {formatString:'%." + measureRound + "f" + nbrFormat + ischeckedLA + "',location: 's',edgeTolerance: -15,formatter: $.jqplot.euroFormatter },");
        jqplotbuffer.append("rendererOptions: { smooth:false}},");
        if (legenddisplay) {
            jqplotbuffer.append(" legend: {");
            if (graphLegendLoc.equalsIgnoreCase("n") || graphLegendLoc.equalsIgnoreCase("s")) {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: " + legendsPerRow + "}},");
            } else {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer,rendererOptions: {numberColumns: 1}},");
            }
        }
        this.Areatick(container, graphType);
        jqplotbuffer.append(" axes: {");
        jqplotbuffer.append(" xaxis: {");
        jqplotbuffer.append(" pad:0,");
        jqplotbuffer.append("label:\"" + showxAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        jqplotbuffer.append(" ticks:" + tickLenthComp + ",");
        jqplotbuffer.append(" tickOptions:{");
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append(" fontSize:'" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor:'" + this.xaxisfontcolor + "',");
        jqplotbuffer.append(" fontFamily:'" + this.fontFamily + "',");
        jqplotbuffer.append(" angle:" + labelDir + "");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" yaxis: {");
        jqplotbuffer.append("label:\"" + showlyAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        if (yaxiscalibration.equalsIgnoreCase("Custom") && !yaxisstart.isEmpty() && !yaxisend.isEmpty() && !yaxisinterval.isEmpty()) {
            jqplotbuffer.append(" pad:0,autoscale:true, min:" + yaxisstart + ",max:" + yaxisend + ",tickInterval: " + yaxisinterval + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter},");
        } else if (yaxiscalibration.equalsIgnoreCase("default") || minvalue == -1) {
            jqplotbuffer.append("pad:0,tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        } else {
            jqplotbuffer.append(" pad:0, min:" + minvalue + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        }
        jqplotbuffer.append(" }");
        jqplotbuffer.append(" },");
//            if ((ischeckedXA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-xaxis-tick\").hide();");
//                 }
//            if ((ischeckedYA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-yaxis-tick\").hide();");
//                 }
        if (targetValue != "" && !targetValue.isEmpty()) {
            jqplotbuffer.append("canvasOverlay: { show: true,");
            jqplotbuffer.append("objects: [{horizontalLine: {name: 'pebbles',");
            jqplotbuffer.append(" y: " + targetValue + ",lineWidth: 2,color: \"" + targetcolor + "\",");
            jqplotbuffer.append("shadow: true,lineCap: 'butt',xOffset: 0,showTooltip: true,tooltipFormatString:\"" + targetValue + "\" }}, ] },");
        }
        jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'n',tooltipAxes: 'yref',formatString: '%d" + nbrFormat + "',tooltipContentEditor:tooltipContentEditor,useAxesFormatters:false },");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
        jqplotbuffer.append("});");
        jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot) ");
        jqplotbuffer.append("{");
        jqplotbuffer.append("var pointlables=ticks" + tickId + "[pointIndex];");
        jqplotbuffer.append("var roundvalue=Math.round(plot.data[seriesIndex][pointIndex]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + ");");
        if (container.isReportCrosstab()) {
            jqplotbuffer.append("var tooltip1=tooltip" + tickId + "[seriesIndex];");
            jqplotbuffer.append(" return pointlables.toString()+\",\"+tooltip1.toString()+\":\"  +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + "\"");
        } else {
            jqplotbuffer.append(" return pointlables.toString()+\":\" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + ischeckedLA + "\"");
        }
        jqplotbuffer.append("};");
        if (!isFromOneView) {
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, i, j, data) {");
            jqplotbuffer.append("var reportid=" + reportid + ";");
            jqplotbuffer.append(" var value=ticks" + tickId + "[j];");
            jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");

            if (showlocaldrill) {
                jqplotbuffer.append(" $.ajax({");
                jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+value+'&type=drillgrph',");
                jqplotbuffer.append(" success: function(data){");
                jqplotbuffer.append(" window.location.href=window.location.href;");
                jqplotbuffer.append("}");
                jqplotbuffer.append("});");
            } else if (drilltype.equalsIgnoreCase("report")) {
                ArrayList url = container.getLinks();
                String Url = url.get(0).toString();
                if (container.isReportCrosstab()) {
                    jqplotbuffer.append("submitformStandard('" + Url + "',areavalue,i,j,'" + drillmap.toString() + "','" + rowviewby + "','" + columnviewby + "',tooltip" + tickId + "[i]);");
                } else {
                    jqplotbuffer.append("submitformStandard('" + Url + "',areavalue,i,j,'" + drillmap.toString() + "');");
                }
            } else {
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    jqplotbuffer.append("submitform('" + oneUrl + "',areavalue,'" + isAdhocEnabled + "');");
                }
            }

            jqplotbuffer.append("}) ");
        }
        //added by srikanth.p for drilling in oneView

        if (isFromOneView) {

            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, data) {");
            jqplotbuffer.append("var value=ticks" + tickId + "[pointIndex];");
            ArrayList url = container.getLinks();
            String oneUrl = "";
            if (url != null) {
                oneUrl = url.get(0).toString();
                oneUrl = oneUrl.replace("reportViewer.do?reportBy=viewReport", "dashboardViewer.do?reportBy=getOLAPGraphforOneView");
                jqplotbuffer.append("drillOLAPGraph('" + oneUrl + "',value);");
            }

            jqplotbuffer.append("});");
        }


        return jqplotbuffer;
    }

    public StringBuffer StackedBarVertical(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
        this.tickLength(container, graphType);
        jqplotbuffer.append("(function($) {");
        jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
        jqplotbuffer.append("if (!format) {");
        jqplotbuffer.append("format = '%.1f';");
        jqplotbuffer.append(" }");
        jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
        jqplotbuffer.append("};");
        jqplotbuffer.append(" function numberWithCommas(x) {");
        jqplotbuffer.append("  if(x==0 || x==0.0 || x==0.00) return '';");
        jqplotbuffer.append(" else return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
        jqplotbuffer.append(" }");

        jqplotbuffer.append("})(jQuery);");
        jqplotbuffer.append(" var ticks" + tickId + " =" + tickLenthComp + ";");//k
        jqplotbuffer.append("var tooltip" + tickId + "=" + tooltip + ";");
        jqplotbuffer.append("var plot1 = $.jqplot('" + chartId + "'," + data + ", {");

        jqplotbuffer.append(" stackSeries: true,");
        jqplotbuffer.append(" animate: true,animateReplot: true,");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        if (!isFromOneView && !ishomeTab) {
            jqplotbuffer.append("title:{text: '" + title + "',fontSize: 12},");
        }
        jqplotbuffer.append(" seriesDefaults:{");
        jqplotbuffer.append(" renderer:$.jqplot.BarRenderer,");
        jqplotbuffer.append("rendererOptions: {fillToZero: true, useNegativeColors: false}, ");
        jqplotbuffer.append("pointLabels: { show: " + showlabels + ",formatString:'%." + measureRound + "f" + nbrFormat + ischeckedLA + "',location: 's',edgeTolerance: -15,formatter: $.jqplot.euroFormatter }},");
        if (legenddisplay) {
            jqplotbuffer.append(" legend: {");
            if (graphLegendLoc.equalsIgnoreCase("n") || graphLegendLoc.equalsIgnoreCase("s")) {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: " + legendsPerRow + "}},");
            } else {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ",renderer:$.jqplot.EnhancedLegendRenderer,rendererOptions: {numberColumns: 1}},");
            }
        }
        jqplotbuffer.append(" axes: {");
        jqplotbuffer.append(" xaxis: {");
        jqplotbuffer.append(" renderer: $.jqplot.CategoryAxisRenderer,");
        jqplotbuffer.append("label:\"" + showxAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        jqplotbuffer.append(" ticks:" + ticks122 + ",");//krk
        jqplotbuffer.append(" tickOptions:{");
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append(" fontSize: '" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor: \"black\",");
        jqplotbuffer.append(" fontFamily:'\"Verdana\"',");
        jqplotbuffer.append(" angle: " + labelDir + ",");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" yaxis: {");
        jqplotbuffer.append("label:\"" + showlyAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        if (yaxiscalibration.equalsIgnoreCase("Custom") && !yaxisstart.isEmpty() && !yaxisend.isEmpty() && !yaxisinterval.isEmpty()) {
            jqplotbuffer.append(" pad:0,autoscale:true, min:" + yaxisstart + ",max:" + yaxisend + ",tickInterval: " + yaxisinterval + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter},");
        } else if (yaxiscalibration.equalsIgnoreCase("default") || minvalue == -1) {
            jqplotbuffer.append(" tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        } else {
            jqplotbuffer.append(" pad:0,min:0,tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        }
        jqplotbuffer.append(" }");
        jqplotbuffer.append(" },");
//            if ((ischeckedXA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-xaxis-tick\").hide();");
//                 }
//            if ((ischeckedYA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-yaxis-tick\").hide();");
//                 }
        if (targetValue != "" && !targetValue.isEmpty()) {
            jqplotbuffer.append("canvasOverlay: { show: true,");
            jqplotbuffer.append("objects: [{horizontalLine: {name: 'pebbles',");
            jqplotbuffer.append(" y: " + targetValue + ",lineWidth: 2,color: \"" + targetcolor + "\",");
            jqplotbuffer.append("shadow: true,lineCap: 'butt',xOffset: 0,showTooltip: true,tooltipFormatString:\"" + targetValue + "\" }}, ] },");
        }
        jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'n',tooltipAxes: 'yref',useAxesFormatters:true,tooltipContentEditor:tooltipContentEditor },");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
        jqplotbuffer.append("});");
        jqplotbuffer.append(" function tooltipContentEditor(str, seriesIndex, pointIndex, plot)");
        jqplotbuffer.append("{");
        jqplotbuffer.append(" var pointlables=ticks" + tickId + "[pointIndex];");
        jqplotbuffer.append("var roundvalue=Math.round(plot.data[seriesIndex][pointIndex]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + ");");
        if (container.isReportCrosstab()) {
            jqplotbuffer.append("var tooltip1=tooltip" + tickId + "[seriesIndex];");
            jqplotbuffer.append(" return pointlables.toString()+\",\"+tooltip1.toString()+\":\"  +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + ischeckedLA + "\"");
        } else {
            jqplotbuffer.append(" return pointlables.toString()+\": \" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + ischeckedLA + "\"");
        }

        jqplotbuffer.append("}");
        if (!isFromOneView) {
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, i, j, data) {");
            jqplotbuffer.append("var reportid=" + reportid + ";");
            jqplotbuffer.append(" var value=ticks" + tickId + "[j];");
            jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");

            if (showlocaldrill) {
                jqplotbuffer.append(" $.ajax({");
                jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+value+'&type=drillgrph',");
                jqplotbuffer.append(" success: function(data){");
                jqplotbuffer.append(" window.location.href=window.location.href;");
                jqplotbuffer.append("}");
                jqplotbuffer.append("});");
            } else if (drilltype.equalsIgnoreCase("report")) {
                ArrayList url = container.getLinks();
                String Url = url.get(0).toString();
                if (container.isReportCrosstab()) {
                    jqplotbuffer.append("submitformStandard('" + Url + "',ticks" + tickId + "[j],i,j,'" + drillmap.toString() + "','" + rowviewby + "','" + columnviewby + "',tooltip" + tickId + "[i]);");
                } else {
                    jqplotbuffer.append("submitformStandard('" + Url + "',ticks" + tickId + "[j],i,j,'" + drillmap.toString() + "');");
                }
            } else {
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    jqplotbuffer.append("submitform('" + oneUrl + "',ticks" + tickId + "[j],'" + isAdhocEnabled + "');");
                }
            }

            jqplotbuffer.append("}) ");
        }

        //added by srikanth.p for drill in oneView
        if (isFromOneView) {

            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, data) {");
            jqplotbuffer.append("var value=ticks" + tickId + "[pointIndex];");
            ArrayList url = container.getLinks();
            String oneUrl = "";
            if (url != null) {
                oneUrl = url.get(0).toString();
                oneUrl = oneUrl.replace("reportViewer.do?reportBy=viewReport", "dashboardViewer.do?reportBy=getOLAPGraphforOneView");
                jqplotbuffer.append("drillOLAPGraph('" + oneUrl + "',value);");
            }
            jqplotbuffer.append("});");
        }
        return jqplotbuffer;
    }

    public StringBuffer DualAxis(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
        this.tickLength(container, graphType);
        if ((graphType.equalsIgnoreCase("DualAxis(Bar-Line)") || graphType.equalsIgnoreCase("DualAxis(Area-Line)")) && graphType != null) {
            jqplotbuffer.append("(function($) {");
            jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
            jqplotbuffer.append("if (!format) {");
            jqplotbuffer.append("format = '%.1f';");
            jqplotbuffer.append(" }");
            jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
            jqplotbuffer.append("};");
            jqplotbuffer.append(" function numberWithCommas(x) {");
            jqplotbuffer.append("  if(x==0 || x==0.0 || x==0.00) return '';");
            jqplotbuffer.append(" else return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
            jqplotbuffer.append(" }");
            jqplotbuffer.append("})(jQuery);");
            jqplotbuffer.append("var ticks" + tickId + "=" + tickLenthComp + ";");
            jqplotbuffer.append("var tooltip" + tickId + "=" + tooltip + ";");
            jqplotbuffer.append("var plot2 = $.jqplot('" + chartId + "'," + data + ",{");

            jqplotbuffer.append("animate: true,");
            jqplotbuffer.append("seriesColors:" + seriesColors + ",");
            if (!isFromOneView && !ishomeTab) {
                jqplotbuffer.append("title:{text: '" + title + "',fontSize: 12},");
            }
            if ((graphtype1 != null && !graphtype1.isEmpty() && (graphtype1.equalsIgnoreCase("StackedBar") || graphtype1.equalsIgnoreCase("StackedArea"))) || (graphtype2 != null && !graphtype2.isEmpty() && (graphtype2.equalsIgnoreCase("StackedBar") || graphtype2.equalsIgnoreCase("StackedArea")))) {
                jqplotbuffer.append("stackSeries: true,");
            }
            jqplotbuffer.append("series:" + overlaid + ",");
            if (legenddisplay) {
                jqplotbuffer.append(" legend: {");
                if (graphLegendLoc.equalsIgnoreCase("n") || graphLegendLoc.equalsIgnoreCase("s")) {
                    jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: " + legendsPerRow + "}},");
                } else {
                    jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer,rendererOptions: {numberColumns: 1}},");
                }
            }
            jqplotbuffer.append(" seriesDefaults:{");
            jqplotbuffer.append("rendererOptions: {");
            jqplotbuffer.append("highlightMouseDown: true},");
            jqplotbuffer.append("pointLabels: { show: " + showlabels + ",formatString:'%." + measureRound + "f" + nbrFormat + ischeckedLA + "',location: 's',edgeTolerance: -15,formatter: $.jqplot.euroFormatter }");
            jqplotbuffer.append("},");
            jqplotbuffer.append(" axes: {");

            jqplotbuffer.append("xaxis: { renderer: $.jqplot.CategoryAxisRenderer,");
            jqplotbuffer.append("label:\"" + showxAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
            jqplotbuffer.append("ticks:" + ticks122 + ","); //krk
            jqplotbuffer.append("tickOptions:{showGridline:" + griddisplay + ",fontSize: '" + this.xaxisfontSize + "',fontFamily:'" + this.fontFamily + "',textColor: \"black\",angle:" + labelDir + ",");
            jqplotbuffer.append(" formatter: function(format, value) {");
            jqplotbuffer.append("var tick=ticks" + tickId + "[value-1];");
            jqplotbuffer.append("if(tick===undefined){ return \"\"; } else{");
            if (viewByColumns.length >= 2 && !isappendXaxis) {
                jqplotbuffer.append("if(tick!=undefined) var tick=tick.toString().substring(tick.toString().indexOf(\",\")+1);");
            }
            jqplotbuffer.append("if(tick.length>15) return tick.substring(0,15)+\"...\";  else return tick;}}}, ");
            jqplotbuffer.append("tickRenderer:$.jqplot.CanvasAxisTickRenderer },");
            jqplotbuffer.append(" yaxis: { ");
            jqplotbuffer.append("label:\"" + showlyAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
            if (yaxiscalibration.equalsIgnoreCase("Custom") && !yaxisstart.isEmpty() && !yaxisend.isEmpty() && !yaxisinterval.isEmpty()) {
                jqplotbuffer.append(" pad:0,autoscale:true, min:" + yaxisstart + ",max:" + yaxisend + ",tickInterval: " + yaxisinterval + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter},");
            } else if (yaxiscalibration.equalsIgnoreCase("default") || minvalue == -1) {
                jqplotbuffer.append(" tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
            } else {
                jqplotbuffer.append(" pad:0,min:0,tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
            }
            jqplotbuffer.append(" },");
            jqplotbuffer.append(" y2axis: { ");
            jqplotbuffer.append("label:\"" + showryAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
            if (yaxiscalibration.equalsIgnoreCase("default") || minvalue == -1) {
                jqplotbuffer.append(" tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + y2AxisRounding + "f" + nbrFormat + ischeckedRA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
            } else if (yaxiscalibration.equalsIgnoreCase("Custom") && !y2axisstart.isEmpty() && !y2axisend.isEmpty() && !y2axisinterval.isEmpty()) {
                jqplotbuffer.append(" pad:0,autoscale:true, min:" + y2axisstart + ",max:" + y2axisend + ",tickInterval: " + y2axisinterval + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + y2AxisRounding + "f" + nbrFormat + ischeckedRA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter},");
            } else {
                jqplotbuffer.append(" pad:0,min:0,tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + y2AxisRounding + "f" + nbrFormat + ischeckedRA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
            }
            jqplotbuffer.append(" },");
            jqplotbuffer.append("},");
//            if ((ischeckedXA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-xaxis-tick\").hide();");
//                 }
//            if ((ischeckedYA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-yaxis-tick\").hide();");
//                 }
            if (targetValue != "" && !targetValue.isEmpty()) {
                jqplotbuffer.append("canvasay: { show: true,");
                jqplotbuffer.append("objects: [{horizontalLine: {name: 'pebbles',");
                jqplotbuffer.append(" y: " + targetValue + ",lineWidth: 2,color: 'rgb(88,88,88)',");
                jqplotbuffer.append("shadow: true,lineCap: 'butt',xOffset: 0 }}, ] },");
            }
            jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'n',tooltipAxes: 'yref',useAxesFormatters:true,tooltipContentEditor:tooltipContentEditor },");
            jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            jqplotbuffer.append("});");
            jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot)");
            jqplotbuffer.append("{");
            jqplotbuffer.append("var pointlables=ticks" + tickId + "[pointIndex];");
            jqplotbuffer.append("var roundvalue=Math.round(plot.data[seriesIndex][pointIndex]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + ");");
            //jqplotbuffer.append(" return pointlables+\" \" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")");
            if (container.isReportCrosstab()) {
                jqplotbuffer.append("var tooltip1=tooltip" + tickId + "[seriesIndex];");
                jqplotbuffer.append(" return pointlables.toString()+\",\"+tooltip1.toString()+\":\"  +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + ischeckedLA + "\"");
            } else {
                jqplotbuffer.append(" return pointlables.toString()+\":\" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + ischeckedLA + "\"");
            }
            jqplotbuffer.append("}");
            if (!isFromOneView) {
                jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, i, j, data) {");
                jqplotbuffer.append("var reportid=" + reportid + ";");
                jqplotbuffer.append(" var value=ticks" + tickId + "[j];");
                jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");

                if (showlocaldrill) {
                    jqplotbuffer.append(" $.ajax({");
                    jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+value+'&type=drillgrph',");
                    jqplotbuffer.append(" success: function(data){");
                    jqplotbuffer.append(" window.location.href=window.location.href;");
                    jqplotbuffer.append("}");
                    jqplotbuffer.append("});");
                } else if (drilltype.equalsIgnoreCase("report")) {
                    ArrayList url = container.getLinks();
                    String Url = url.get(0).toString();
                    if (container.isReportCrosstab()) {
                        jqplotbuffer.append("submitformStandard('" + Url + "',ticks" + tickId + "[j],i,j,'" + drillmap.toString() + "','" + rowviewby + "','" + columnviewby + "',tooltip" + tickId + "[i]);");
                    } else {
                        jqplotbuffer.append("submitformStandard('" + Url + "',ticks" + tickId + "[j],i,j,'" + drillmap.toString() + "');");
                    }
                } else {
                    ArrayList url = container.getLinks();
                    String oneUrl = "";
                    if (url != null) {
                        oneUrl = url.get(0).toString();
                        jqplotbuffer.append("submitform('" + oneUrl + "',ticks" + tickId + "[j],'" + isAdhocEnabled + "');");
                    }

                }

                jqplotbuffer.append("}) ");
            }
            //added by srikanth.p for drilling in oneView

            if (isFromOneView) {
                jqplotbuffer.append("var i=0;");
                jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, data) {");
                jqplotbuffer.append("var value=ticks" + tickId + "[pointIndex];");
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    oneUrl = oneUrl.replace("reportViewer.do?reportBy=viewReport", "dashboardViewer.do?reportBy=getOLAPGraphforOneView");
                    jqplotbuffer.append("drilldualOLAPGraph('" + oneUrl + "',value,i);");
                    jqplotbuffer.append("i++;");
                }


                //jqplotbuffer.append("$(\"#" + chartId + "\").unbind('jqplotDataClick');");
                jqplotbuffer.append("});");
                jqplotbuffer.append("i=0;");
            }
        }
        return jqplotbuffer;
    }

    public StringBuffer Overlaid(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
        this.tickLength(container, graphType);
        jqplotbuffer.append("(function($) {");
        jqplotbuffer.append("$.jqplot.euroFormatter = function (format, val) {");
        jqplotbuffer.append("if (!format) {");
        jqplotbuffer.append("format = '%.1f';");
        jqplotbuffer.append(" }");
        jqplotbuffer.append("return numberWithCommas($.jqplot.sprintf(format, val));");
        jqplotbuffer.append("};");
        jqplotbuffer.append(" function numberWithCommas(x) {");
        jqplotbuffer.append("  if(x==0 || x==0.0 || x==0.00) return '';");
        jqplotbuffer.append(" else return x.toString().replace(/\\B(?=(?:\\d{3})+(?!\\d))/g, \",\");");
        jqplotbuffer.append(" }");

        jqplotbuffer.append("})(jQuery);");
        jqplotbuffer.append("var ticks" + tickId + "=" + tickLenthComp + ";");//k

        jqplotbuffer.append("$(function() {");
        jqplotbuffer.append("var plot1 = $.jqplot('" + chartId + "'," + data + ", BarChart());");


        jqplotbuffer.append(" });");
        jqplotbuffer.append("function BarChart(){");
        jqplotbuffer.append(" var optionsObj = {");
        jqplotbuffer.append("animate: true,");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        if (!isFromOneView && !ishomeTab) {
            jqplotbuffer.append("title:{text: '" + title + "',fontSize: 12},");
        }
        jqplotbuffer.append("axes: {");
        jqplotbuffer.append("xaxis: {");
        jqplotbuffer.append("renderer: $.jqplot.CategoryAxisRenderer,");
        jqplotbuffer.append("label:\"" + showxAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        jqplotbuffer.append("ticks:" + ticks122 + ","); //krk
        jqplotbuffer.append(" tickOptions:{");
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append(" fontSize: '" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor: \"black\",");
        jqplotbuffer.append(" fontFamily:'\"Verdana\"',");
        jqplotbuffer.append(" angle:" + labelDir + ",");
        jqplotbuffer.append(" },");
        jqplotbuffer.append("tickRenderer:$.jqplot.CanvasAxisTickRenderer");
        jqplotbuffer.append(" },");
        jqplotbuffer.append("yaxis: {");
        jqplotbuffer.append("label:\"" + showlyAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        if (yaxiscalibration.equalsIgnoreCase("Custom") && !yaxisstart.isEmpty() && !yaxisend.isEmpty() && !yaxisinterval.isEmpty()) {
            jqplotbuffer.append(" pad:0,autoscale:true, min:" + yaxisstart + ",max:" + yaxisend + ",tickInterval: " + yaxisinterval + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter},");
        } else if (yaxiscalibration.equalsIgnoreCase("default") || minvalue == -1) {
            jqplotbuffer.append(" tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        } else {
            jqplotbuffer.append(" pad:0,min:0,tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        }
        jqplotbuffer.append("}");
        jqplotbuffer.append("},");
//            if ((ischeckedXA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-xaxis-tick\").hide();");
//                 }
//            if ((ischeckedYA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-yaxis-tick\").hide();");
//                 }
        if (targetValue != "" && !targetValue.isEmpty()) {
            jqplotbuffer.append("canvasOverlay: { show: true,");
            jqplotbuffer.append("objects: [{horizontalLine: {name: 'pebbles',");
            jqplotbuffer.append(" y: " + targetValue + ",lineWidth: 2,color: \"" + targetcolor + "\",");
            jqplotbuffer.append("shadow: true,lineCap: 'butt',xOffset: 0,showTooltip: true,tooltipFormatString:\"" + targetValue + "\" }}, ] },");
        }
        jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'n',tooltipAxes: 'yref',useAxesFormatters:true,tooltipContentEditor:tooltipContentEditor },");
        jqplotbuffer.append("grid: {");
        jqplotbuffer.append("gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0},");
        if ((graphtype1 != null && !graphtype1.isEmpty() && (graphtype1.equalsIgnoreCase("StackedBar") || graphtype1.equalsIgnoreCase("StackedArea"))) || (graphtype2 != null && !graphtype2.isEmpty() && (graphtype2.equalsIgnoreCase("StackedBar") || graphtype2.equalsIgnoreCase("StackedArea")))) {
            jqplotbuffer.append("stackSeries: true,");
        }
        jqplotbuffer.append(" series:" + overlaid + ",");
        if (legenddisplay) {
            jqplotbuffer.append(" legend: {");
            if (graphLegendLoc.equalsIgnoreCase("n") || graphLegendLoc.equalsIgnoreCase("s")) {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ", showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: " + legendsPerRow + "}},");
            } else {
                jqplotbuffer.append(" show: " + legenddisplay + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "',labels:" + legendlabels + ",renderer:$.jqplot.EnhancedLegendRenderer,rendererOptions: {numberColumns: 1}},");
            }
        }
        jqplotbuffer.append(" seriesDefaults:{");
        jqplotbuffer.append("shadow: true,");
        jqplotbuffer.append("rendererOptions: {");
        jqplotbuffer.append("highlightMouseDown: true},");
        jqplotbuffer.append("pointLabels: { show: " + showlabels + ",formatString:'%." + measureRound + "f" + nbrFormat + ischeckedLA + "',location: 's',edgeTolerance: -15,formatter: $.jqplot.euroFormatter },");
        jqplotbuffer.append("}};");
        jqplotbuffer.append("return optionsObj;");
        jqplotbuffer.append("}");
        jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, data) {");
        jqplotbuffer.append("var areanumber=(data[0]-1);");
        jqplotbuffer.append("var areavalue=ticks" + tickId + "[areanumber];");
        jqplotbuffer.append("var reportid=" + reportid + ";");
        jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");
        if (isFromOneView) {
            jqplotbuffer.append("var i=0;");
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, data) {");
            jqplotbuffer.append("var value=ticks" + tickId + "[pointIndex];");
            ArrayList url = container.getLinks();
            String oneUrl = "";
            if (url != null) {
                oneUrl = url.get(0).toString();
                oneUrl = oneUrl.replace("reportViewer.do?reportBy=viewReport", "dashboardViewer.do?reportBy=getOLAPGraphforOneView");
                jqplotbuffer.append("drilldualOLAPGraph('" + oneUrl + "',value,i);");
                jqplotbuffer.append("i++;");
            }


            //jqplotbuffer.append("$(\"#" + chartId + "\").unbind('jqplotDataClick');");
            jqplotbuffer.append("});");
            jqplotbuffer.append("i=0;");
        } else {
            if (showlocaldrill) {
                jqplotbuffer.append(" $.ajax({");
                jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+areavalue+'&type=drillgrph',");
                jqplotbuffer.append(" success: function(data){");
                jqplotbuffer.append(" window.location.href=window.location.href;");
                jqplotbuffer.append("}");
                jqplotbuffer.append("});");
            } else if (drilltype.equalsIgnoreCase("report")) {
                ArrayList url = container.getLinks();
                String Url = url.get(0).toString();
                jqplotbuffer.append("submitformStandard('" + Url + "',areavalue,i,j,'" + drillmap + "');");
            } else {
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    jqplotbuffer.append("submitform('" + oneUrl + "',areavalue,'" + isAdhocEnabled + "');");
                }
            }
        }


        jqplotbuffer.append("});");
        jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot)");
        jqplotbuffer.append("{");
        jqplotbuffer.append("var pointlables=ticks" + tickId + "[pointIndex];");
        jqplotbuffer.append("var roundvalue=Math.round(plot.data[seriesIndex][pointIndex]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + ");");
        jqplotbuffer.append(" return pointlables.toString()+\": \" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + "\"");
        //jqplotbuffer.append("return pointlables+\",\"+ plot.data[seriesIndex][pointIndex];");
        jqplotbuffer.append("}");


        return jqplotbuffer;
    }

    public StringBuffer Funnel(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
        jqplotbuffer.append("var data =" + data + ";");
        jqplotbuffer.append("var plot1 = jQuery.jqplot ('" + chartId + "', [data],");
        jqplotbuffer.append(" {");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        if (!isFromOneView && !ishomeTab) {
            jqplotbuffer.append("title:{text: '" + title + "',fontSize: 12},");
        }
        jqplotbuffer.append(" seriesDefaults: {");
        jqplotbuffer.append("renderer: jQuery.jqplot.FunnelRenderer, ");

        jqplotbuffer.append("rendererOptions: {");
        jqplotbuffer.append(" dataLabelThreshold:0,shadowAlpha: 0,");
        if (graphType.equalsIgnoreCase("Funnel(INV)")) {
            jqplotbuffer.append(" widthRatio: 0,");
        }

        jqplotbuffer.append(" sectionMargin:0,");
        jqplotbuffer.append("showDataLabels: " + showlabels + ",");
        jqplotbuffer.append(" highlightMouseDown: true,");
        if (measureFormat.equalsIgnoreCase("value")) {
            jqplotbuffer.append("dataLabels: 'value',");
            jqplotbuffer.append(" dataLabelFormatString: \"%'." + measureRound + "f" + nbrFormat + ischeckedLA + "\",");
        } else {
            jqplotbuffer.append(" dataLabelFormatString: \"%'." + measureRound + "f%\",");
        }
        jqplotbuffer.append("},");
        jqplotbuffer.append("pointLabels: { show: " + showlabels + ",formatString:'%." + measureRound + "f',location: 's',edgeTolerance: -15 },");
        jqplotbuffer.append(" },");
        if (legenddisplay) {
            jqplotbuffer.append("legend: {");
            if (graphLegendLoc.equalsIgnoreCase("n") || graphLegendLoc.equalsIgnoreCase("s")) {
                jqplotbuffer.append(" show: " + legenddisplay + ",labels:" + legendlabels + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "', showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: " + legendsPerRow + "}},");
            } else {
                jqplotbuffer.append(" show: " + legenddisplay + ",labels:" + legendlabels + ",location: '" + graphLegendLoc + "',placement: 'insideGrid',xoffset: 0,fontSize: '10px',rowSpacing: '0px',textColor: 'balck', fontFamily: 'Lucida Grande, Lucida Sans, Arial, sans-serif' },");
            }
        }
        jqplotbuffer.append(" cursor: {show: true,showTooltip: true,showTooltipUnitPosition:false, useAxesFormatters:false, followMouse:true},");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
        jqplotbuffer.append(" });");
        jqplotbuffer.append("$(\"#" + chartId + "\").bind('jqplotDataMouseOver',function (ev, seriesIndex, pointIndex, data1) {");
        if (graphType.equalsIgnoreCase("Funnel(INV)")) {
            jqplotbuffer.append("var data2=data.sort(function (a, b) { return a[1] - b[1]; });var data3=data2[pointIndex];");
        } else {
            jqplotbuffer.append("var data3=data1;");
        }
        jqplotbuffer.append("var valueRound= Math.round(data3[1]*Math.pow(10," + measureRound + "))/Math.pow(10, " + measureRound + ");");
        jqplotbuffer.append("var value= data3[0] + ': ' + valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + "\";$(\".jqplot-cursor-tooltip\").html(value+\"\");});");
        return jqplotbuffer;
    }

    public StringBuffer Pie(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
        jqplotbuffer.append("var data =" + data + ";");
        jqplotbuffer.append("var labels=" + legendlabels + ";");
        jqplotbuffer.append("var plot1 = jQuery.jqplot ('" + chartId + "', [data],");
        jqplotbuffer.append(" {");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        if (!isFromOneView && !ishomeTab) {
            jqplotbuffer.append("title:{text: '" + title + "',fontSize: 12},");
        }
        jqplotbuffer.append(" seriesDefaults: {");

        jqplotbuffer.append("renderer: jQuery.jqplot.PieRenderer, ");
        jqplotbuffer.append("rendererOptions: {");
        if (graphType.equalsIgnoreCase("Pie-Empty")) {
            jqplotbuffer.append("fill: false,");
        }
        jqplotbuffer.append(" highlightMouseDown: true,");
        jqplotbuffer.append("showDataLabels: " + showlabels + ",");
        if (measureFormat.equalsIgnoreCase("value")) {
            jqplotbuffer.append("dataLabels: 'value',");
            jqplotbuffer.append(" dataLabelFormatString: \"%'." + measureRound + "f" + nbrFormat + ischeckedLA + "\",");
        } else if (measureFormat.equalsIgnoreCase("ST")) {
            jqplotbuffer.append("dataLabels: 'label',");
        } else if (measureFormat.equalsIgnoreCase("valueWithPercentage")) { //kruthika
            jqplotbuffer.append("dataLabels: 'custom',");
            jqplotbuffer.append(" dataLabelFormatString: \"%'." + measureRound + "f" + nbrFormat + ischeckedLA + "\",");
        } else {
            jqplotbuffer.append(" dataLabelFormatString: \"%'." + measureRound + "f%\",");
        }
        jqplotbuffer.append("fontSize: '20px'");
        jqplotbuffer.append("}");
        jqplotbuffer.append(" },");
        if (legenddisplay) {
            jqplotbuffer.append("legend: {");
            if (graphLegendLoc.equalsIgnoreCase("n") || graphLegendLoc.equalsIgnoreCase("s")) {
                jqplotbuffer.append(" show: " + legenddisplay + ", labels:" + legendlabels + ",placement: 'outsideGrid',location: '" + graphLegendLoc + "', showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: " + legendsPerRow + "}},");
            } else {
                jqplotbuffer.append("show: true,location: '" + graphLegendLoc + "',labels:" + legendlabels + ",placement: 'outsideGrid',xoffset: 0,fontSize: '10px',rowSpacing: '0px' },");
            }
        }
        jqplotbuffer.append(" cursor: {show: true,showTooltip: true,showTooltipUnitPosition:false, useAxesFormatters:false, followMouse:true},");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
        jqplotbuffer.append(" });");
        jqplotbuffer.append("$(\"#" + chartId + "\").bind('jqplotDataMouseOver',function (ev, seriesIndex, pointIndex, data) {var valueRound= Math.round(data[1]*Math.pow(10," + measureRound + "))/Math.pow(10, " + measureRound + ");");
        if (measureFormat.equalsIgnoreCase("ST")) {
            jqplotbuffer.append("var value= labels[pointIndex] + ': ' + valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + "\";$(\".jqplot-cursor-tooltip\").html(value+\"\");});");
        } else {
            if (legendlabels.size() > 0) {
                jqplotbuffer.append("var value= labels[pointIndex] + ': '");
            } else {
                jqplotbuffer.append("var value= data[0] + ': '");
            }
            jqplotbuffer.append(" + valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + "\";$(\".jqplot-cursor-tooltip\").html(value+\"\");});");
        }
        jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, data) {");
        if (measureFormat.equalsIgnoreCase("ST")) {
            jqplotbuffer.append("var pieslicevalue=labels[pointIndex];");
        } else {
            jqplotbuffer.append("var pieslicevalue=data[0];");
        }
        jqplotbuffer.append("var reportid=" + reportid + ";");
        jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");
        if (isFromOneView) {
            ArrayList url = container.getLinks();
            String oneUrl = "";
            if (url != null) {
                oneUrl = url.get(0).toString();
                oneUrl = oneUrl.replace("reportViewer.do?reportBy=viewReport", "dashboardViewer.do?reportBy=getOLAPGraphforOneView");
                jqplotbuffer.append("drillOLAPGraph('" + oneUrl + "',pieslicevalue);");


            }


        } else {
            if (showlocaldrill) {

                jqplotbuffer.append(" $.ajax({");
                jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+pieslicevalue+'&type=drillgrph',");
                jqplotbuffer.append(" success: function(data){");
                jqplotbuffer.append(" window.location.href=window.location.href;");
                jqplotbuffer.append("}");
                jqplotbuffer.append("});");
            } else if (drilltype.equalsIgnoreCase("report")) {
                ArrayList url = container.getLinks();
                String Url = url.get(0).toString();
                if (container.isReportCrosstab()) {
                    jqplotbuffer.append("submitformStandard('" + Url + "','" + title + "',pointIndex,seriesIndex,'" + drillmap.toString() + "','" + rowviewby + "','" + columnviewby + "',pieslicevalue);");
                } else {
                    jqplotbuffer.append("submitformStandard('" + Url + "',pieslicevalue,pointIndex,seriesIndex,'" + drillmap + "');");
                }
            } else {
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    jqplotbuffer.append("submitform('" + oneUrl + "',pieslicevalue,'" + isAdhocEnabled + "');");
                }
            }
        }
        jqplotbuffer.append("});");
        return jqplotbuffer;

    }

    public StringBuffer Donut(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
        jqplotbuffer.append("var labels=" + legendlabels + ";");
        jqplotbuffer.append("var plot3 = $.jqplot('" + chartId + "', " + data + ", {");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        if (!isFromOneView && !ishomeTab) {
            jqplotbuffer.append("title:{text: '" + title + "',fontSize: 12},");
        }
        jqplotbuffer.append("seriesDefaults: {");
        jqplotbuffer.append("renderer:$.jqplot.DonutRenderer,");
        jqplotbuffer.append("rendererOptions:{");
        jqplotbuffer.append("sliceMargin:1,");
        jqplotbuffer.append("startAngle:30,");
        jqplotbuffer.append("pointLabels: { show: " + showlabels + ",formatString:'%." + measureRound + "f',location: 's',edgeTolerance: -15 },");
        jqplotbuffer.append("showDataLabels: " + showlabels + ",");
        jqplotbuffer.append(" highlightMouseDown: true,");
        if (measureFormat.equalsIgnoreCase("value")) {
            jqplotbuffer.append("dataLabels: 'value',");
            jqplotbuffer.append(" dataLabelFormatString: \"%'." + measureRound + "f" + nbrFormat + "\",");
        } else {
            jqplotbuffer.append(" dataLabelFormatString: \"%'." + measureRound + "f%\",");
        }
        jqplotbuffer.append("}},");
        if (legenddisplay) {
            jqplotbuffer.append("legend: {");
            if (graphLegendLoc.equalsIgnoreCase("n") || graphLegendLoc.equalsIgnoreCase("s")) {
                jqplotbuffer.append(" show: " + legenddisplay + ",labels:" + legendlabels + ", placement: 'outsideGrid',location: '" + graphLegendLoc + "', showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: " + legendsPerRow + "}},");
            } else {
                jqplotbuffer.append("show: true,location: '" + graphLegendLoc + "',labels:" + legendlabels + ",placement: 'outsideGrid',xoffset: 0,fontSize: '10px',rowSpacing: '0px',textColor: 'balck', fontFamily: 'Lucida Grande, Lucida Sans, Arial, sans-serif' },");
            }

        }
        jqplotbuffer.append(" cursor: {show: true,showTooltip: true,showTooltipUnitPosition:false, useAxesFormatters:false, followMouse:true},");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
        jqplotbuffer.append("});");
        jqplotbuffer.append("$(\"#" + chartId + "\").bind('jqplotDataMouseOver',function (ev, seriesIndex, pointIndex, data) {var valueRound= Math.round(data[1]*Math.pow(10," + measureRound + "))/Math.pow(10, " + measureRound + ");");
        if (legendlabels.size() > 0) {
            jqplotbuffer.append("var value= labels[pointIndex] + ': '");
        } else {
            jqplotbuffer.append("var value= data[0] + ': '");
        }
        jqplotbuffer.append(" + valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + "\";$(\".jqplot-cursor-tooltip\").html(value+\"\");});");
        jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, data) {");
        jqplotbuffer.append("var pieslicevalue=data[0];");
        jqplotbuffer.append("var reportid=" + reportid + ";");
        jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");
        if (isFromOneView) {
            ArrayList url = container.getLinks();
            String oneUrl = "";
            if (url != null) {
                oneUrl = url.get(0).toString();
                oneUrl = oneUrl.replace("reportViewer.do?reportBy=viewReport", "dashboardViewer.do?reportBy=getOLAPGraphforOneView");
                jqplotbuffer.append("drillOLAPGraph('" + oneUrl + "',pieslicevalue);");


            }

        } else {
            if (showlocaldrill) {
                jqplotbuffer.append(" $.ajax({");
                jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+pieslicevalue+'&type=drillgrph',");
                jqplotbuffer.append(" success: function(data){");
                jqplotbuffer.append(" window.location.href=window.location.href;");
                jqplotbuffer.append("}");
                jqplotbuffer.append("});");
            } else if (drilltype.equalsIgnoreCase("report")) {
                ArrayList url = container.getLinks();
                String Url = url.get(0).toString();
                if (container.isReportCrosstab()) {
                    jqplotbuffer.append("submitformStandard('" + Url + "','" + title + "',pointIndex,seriesIndex,'" + drillmap.toString() + "','" + rowviewby + "','" + columnviewby + "',pieslicevalue);");
                } else {
                    jqplotbuffer.append("submitformStandard('" + Url + "',pieslicevalue,pointIndex,seriesIndex,'" + drillmap + "');");
                }
            } else {
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    jqplotbuffer.append("submitform('" + oneUrl + "',pieslicevalue,'" + isAdhocEnabled + "');");
                }
            }
        }

        jqplotbuffer.append("});");
        return jqplotbuffer;
    }

    public StringBuffer Columnpie(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
        for (int i = 0; i < columnPieVewBy.size(); i++) {
            jqplotbuffer.append("var data=" + data + ";");
            jqplotbuffer.append("plot1 = $.jqplot('" + chartId + "',[data],{");
            jqplotbuffer.append("seriesColors:" + seriesColors + ",");
            if (!isFromOneView && !ishomeTab) {
                jqplotbuffer.append("title:{text: '" + columnPieVewBy.get(i) + "',fontSize: 14},");
            }
            jqplotbuffer.append(" seriesDefaults: {");

            jqplotbuffer.append("renderer: jQuery.jqplot.PieRenderer, ");
            jqplotbuffer.append("rendererOptions: {");
            if (graphType.equalsIgnoreCase("Pie-Empty")) {
                jqplotbuffer.append("fill: false,");
            }
            jqplotbuffer.append(" highlightMouseDown: true,");
            jqplotbuffer.append("showDataLabels: " + showlabels + ",");
            if (measureFormat.equalsIgnoreCase("value")) {
                jqplotbuffer.append("dataLabels: 'value',");
                jqplotbuffer.append(" dataLabelFormatString: \"%'." + measureRound + "f" + nbrFormat + ischeckedLA + "\",");
            } else {
                jqplotbuffer.append(" dataLabelFormatString: \"%'." + measureRound + "f%\",");
            }
            jqplotbuffer.append("fontSize: '20px'");
            jqplotbuffer.append("}");
            jqplotbuffer.append(" },");
            if (legenddisplay) {
                jqplotbuffer.append("legend: {");
                if (graphLegendLoc.equalsIgnoreCase("n") || graphLegendLoc.equalsIgnoreCase("s")) {
                    jqplotbuffer.append(" show: " + legenddisplay + ", labels:" + legendlabels + ",placement: 'outsideGrid',location: '" + graphLegendLoc + "', showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: " + legendsPerRow + "}},");
                } else {
                    jqplotbuffer.append("show: true,location: '" + graphLegendLoc + "',labels:" + legendlabels + ",placement: 'outsideGrid',xoffset: 0,fontSize: '10px',rowSpacing: '0px' },");
                }
            }
            jqplotbuffer.append(" cursor: {show: true,showTooltip: true,showTooltipUnitPosition:false, useAxesFormatters:false, followMouse:true},");
            jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            jqplotbuffer.append(" });");
            jqplotbuffer.append("$(\"#" + chartId + "\").bind('jqplotDataMouseOver',function (ev, seriesIndex, pointIndex, data) {var valueRound= Math.round(data[1]*Math.pow(10," + measureRound + "))/Math.pow(10, " + measureRound + ");");
            jqplotbuffer.append("var value= data[0] + ': ' + valueRound.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + "\";$(\".jqplot-cursor-tooltip\").html(value+\"\");});");
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, seriesIndex, pointIndex, data) {");
            jqplotbuffer.append("var pieslicevalue=data[0];");
            jqplotbuffer.append("var reportid=" + reportid + ";");
            jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");
            if (isFromOneView) {
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    oneUrl = oneUrl.replace("reportViewer.do?reportBy=viewReport", "dashboardViewer.do?reportBy=getOLAPGraphforOneView");
                    jqplotbuffer.append("drillOLAPGraph('" + oneUrl + "',pieslicevalue);");


                }


            } else {
                if (showlocaldrill) {

                    jqplotbuffer.append(" $.ajax({");
                    jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+pieslicevalue+'&type=drillgrph',");
                    jqplotbuffer.append(" success: function(data){");
                    jqplotbuffer.append(" window.location.href=window.location.href;");
                    jqplotbuffer.append("}");
                    jqplotbuffer.append("});");
                } else if (drilltype.equalsIgnoreCase("report")) {
                    ArrayList url = container.getLinks();
                    String Url = url.get(0).toString();
                    if (container.isReportCrosstab()) {
                        jqplotbuffer.append("submitformStandard('" + Url + "','" + columnPieVewBy.get(i) + "',pointIndex,seriesIndex,'" + drillmap.toString() + "','" + rowviewby + "','" + columnviewby + "',pieslicevalue);");
                    } else {
                        jqplotbuffer.append("submitformStandard('" + Url + "',pieslicevalue,pointIndex,seriesIndex,'" + drillmap + "');");
                    }
                } else {
                    ArrayList url = container.getLinks();
                    String oneUrl = "";
                    if (url != null) {
                        oneUrl = url.get(0).toString();
                        jqplotbuffer.append("submitform('" + oneUrl + "',pieslicevalue,'" + isAdhocEnabled + "');");
                    }
                }
            }


            jqplotbuffer.append("});");
        }
        return jqplotbuffer;
    }

    public StringBuffer LegendDiv() {
        StringBuffer jqplotbuffer = new StringBuffer();
        if (legendDiv) {

            jqplotbuffer.append("var data =" + data + ";");
            jqplotbuffer.append("var plot1 = jQuery.jqplot ('" + chartId + "', [data],");
            jqplotbuffer.append(" {");
            jqplotbuffer.append("seriesColors:" + seriesColors + ",");

            jqplotbuffer.append(" seriesDefaults: {");

            jqplotbuffer.append("renderer: jQuery.jqplot.PieRenderer, ");
            jqplotbuffer.append(" },");
            jqplotbuffer.append("legend: {");
            jqplotbuffer.append(" show: true, labels:" + legendlabels + ",placement: 'outsideGrid',location: '" + graphLegendLoc + "', showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: 12}},");

            jqplotbuffer.append(" cursor: {show: true,showTooltip: true,showTooltipUnitPosition:false, useAxesFormatters:false, followMouse:true},");
            jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'#BBBBBB',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
            jqplotbuffer.append(" });");
        }
        return jqplotbuffer;
    }

    public StringBuffer Mekko(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
        jqplotbuffer.append(" tickFormatter = function (format, val) {");
        jqplotbuffer.append("if(isNaN(val.toFixed(" + measureRound + "))) return val;if(val < 9999) {return val.toFixed(" + measureRound + ");}");
        if (nbrFormat.equalsIgnoreCase("k")) {
            jqplotbuffer.append("else return (val/1000).toFixed(" + measureRound + ") + \"K\";");
        } else if (nbrFormat.equalsIgnoreCase("l")) {
            jqplotbuffer.append("else return (val/10000).toFixed(" + measureRound + ") + \"L\";");
        } else if (nbrFormat.equalsIgnoreCase("M")) {
            jqplotbuffer.append("else return ((val/1000000).toFixed(" + measureRound + ")) + \"M\";");
        } else if (nbrFormat.equalsIgnoreCase("Cr")) {
            jqplotbuffer.append("else return ((val/10000000).toFixed(" + measureRound + ")) + \"Cr\";");
        } else {
            jqplotbuffer.append("else return val.toFixed(" + measureRound + ")");
        }
        jqplotbuffer.append("};");

        jqplotbuffer.append("var data=" + data + ";");
        jqplotbuffer.append(" var ticks" + tickId + " =" + ticks + ";");
        jqplotbuffer.append("plot1 = $.jqplot('" + chartId + "',data,{");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
        if (!isFromOneView && !ishomeTab) {
            jqplotbuffer.append("title:{text: '" + title + "',fontSize: 14},");
        }
        jqplotbuffer.append(" seriesDefaults: {");

        jqplotbuffer.append("renderer:$.jqplot.MekkoRenderer, rendererOptions: {borderColor: '#dddddd'}},");
        if (legenddisplay) {
            jqplotbuffer.append("legend: {");
            if (graphLegendLoc.equalsIgnoreCase("n") || graphLegendLoc.equalsIgnoreCase("s")) {
                jqplotbuffer.append(" show: " + legenddisplay + ", labels:" + legendlabels + ",placement: 'outsideGrid',location: '" + graphLegendLoc + "', showLabels:true,renderer:$.jqplot.EnhancedLegendRenderer, rendererOptions: {numberColumns: " + legendsPerRow + "}},");
            } else {
                jqplotbuffer.append("show: true,location: '" + graphLegendLoc + "',labels:" + legendlabels + ",placement: 'outsideGrid',xoffset: 0,fontSize: '10px',rowSpacing: '0px' },");
            }
        }
        jqplotbuffer.append(" axesDefaults:{renderer:$.jqplot.MekkoAxisRenderer,tickOptions:{}},");
        jqplotbuffer.append("axes:{");
        jqplotbuffer.append(" xaxis:{barLabels:" + ticks + ",");
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append(" rendererOptions: {barLabelOptions: {");
        jqplotbuffer.append(" fontSize:'" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor:'" + this.xaxisfontcolor + "',");
        jqplotbuffer.append(" fontFamily:'" + this.fontFamily + "'");
        jqplotbuffer.append("},");
        jqplotbuffer.append("barLabelRenderer: $.jqplot.CanvasAxisTickRenderer },tickOptions:{formatter: tickFormatter} }");
        jqplotbuffer.append("},");
        jqplotbuffer.append(" highlighter:{ show:true,sizeAdjust:-8, tooltipLocation: 'e',tooltipAxes: 'yref',useAxesFormatters:true,tooltipContentEditor:tooltipContentEditor },");
        jqplotbuffer.append("});");
        jqplotbuffer.append("function tooltipContentEditor(str, seriesIndex, pointIndex, plot)");
        jqplotbuffer.append("{");
        jqplotbuffer.append("var pointlables=ticks" + tickId + "[pointIndex];");
        jqplotbuffer.append("var roundvalue=Math.round(plot.data[seriesIndex][pointIndex]*Math.pow(10," + measureRound + "))/Math.pow(10," + measureRound + ");");
        jqplotbuffer.append(" return pointlables.toString()+\": \" +roundvalue.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\")+\"" + nbrFormat + "\"");
        //jqplotbuffer.append("return pointlables+\",\"+ plot.data[seriesIndex][pointIndex];");
        jqplotbuffer.append("}");

//        
        return jqplotbuffer;
    }

    public StringBuffer Block(String graphType, Container container, HttpServletRequest request) {
        StringBuffer jqplotbuffer = new StringBuffer();
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
        jqplotbuffer.append("var data=" + data + ";");
        jqplotbuffer.append(" var ticks" + tickId + " =" + ticks + ";");
        jqplotbuffer.append("plot1 = $.jqplot('" + chartId + "',[data],{");
        jqplotbuffer.append("seriesDefaults:{captureRightClick: true,renderer:$.jqplot.BlockRenderer, rendererOptions: { varyBlockColors: true},pointLabels:{show: false}},");
        jqplotbuffer.append("seriesColors:" + seriesColors + ",");
//            jqplotbuffer.append("  animate:true,seriesDefaults:{pointLabels:{ show:false, location:'s' }}, series:[   ");
//            jqplotbuffer.append(" { showLine:false, markerOptions: { shadow: false, size: 2, style:\"circle\" } },],");
        jqplotbuffer.append(" axes:{");
        jqplotbuffer.append(" xaxis: {");
        jqplotbuffer.append("label:\"" + showxAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        if (yaxiscalibration.equalsIgnoreCase("Custom") && !xaxisstart.isEmpty() && !xaxisend.isEmpty() && !xaxisinterval.isEmpty()) {
            jqplotbuffer.append("autoscale:true, min:" + xaxisstart + ",max:" + xaxisend + ",tickInterval:" + xaxisinterval + " ,tickOptions:{");
        } else {
            jqplotbuffer.append(" min:0,tickOptions:{");
        }
        jqplotbuffer.append(" showGridline:" + griddisplay + ",");
        jqplotbuffer.append(" formatString:'%." + xAxisRounding + "f" + nbrFormat + ischeckedRA + "',fontSize:'" + this.xaxisfontSize + "' , ");
        jqplotbuffer.append(" textColor:'" + this.xaxisfontcolor + "',");
        jqplotbuffer.append(" fontFamily:'" + this.fontFamily + "',");
        jqplotbuffer.append(" angle:" + labelDir + ",formatter: $.jqplot.euroFormatter");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" tickRenderer:$.jqplot.CanvasAxisTickRenderer");
        jqplotbuffer.append(" },");
        jqplotbuffer.append(" yaxis: {");
        jqplotbuffer.append("label:\"" + showlyAxis + "\",labelRenderer: $.jqplot.CanvasAxisLabelRenderer,");
        if (yaxiscalibration.equalsIgnoreCase("default") || minvalue == -1) {
            jqplotbuffer.append(" tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        } else if (yaxiscalibration.equalsIgnoreCase("Custom") && !yaxisstart.isEmpty() && !yaxisend.isEmpty() && !yaxisinterval.isEmpty()) {
            jqplotbuffer.append(" pad:0,autoscale:true, min:" + yaxisstart + ",max:" + yaxisend + ",tickInterval: " + yaxisinterval + ",tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter},");
        } else {
            jqplotbuffer.append(" pad:0, numberTicks:16,min:5,tickOptions: { showGridline:" + griddisplay + ",formatString:'%." + yAxisRounding + "f" + nbrFormat + ischeckedLA + "',fontSize:'" + this.yaxisfontSize + "' , textColor:'" + this.yaxisfontcolor + "', fontFamily:'" + this.fontFamily + "',formatter: $.jqplot.euroFormatter}");
        }
        jqplotbuffer.append(" }},");
        if (targetValue != "" && !targetValue.isEmpty()) {
            jqplotbuffer.append("canvasOverlay: { show: true,");
            jqplotbuffer.append("objects: [{horizontalLine: {name: 'pebbles',");
            jqplotbuffer.append(" y: " + targetValue + ",lineWidth: 2,color:  \"" + targetcolor + "\",");
            jqplotbuffer.append("shadow: true,lineCap: 'butt',xOffset: 0,showTooltip: true,tooltipFormatString:\"" + targetValue + "\" }}, ] },");
        }
        jqplotbuffer.append(" cursor: {show:true,zoom:true,showTooltip: true, useAxesFormatters:true, followMouse:true},");
        jqplotbuffer.append("grid:{gridLineColor:'#F2F2F2',background:'" + rgb + "',borderWidth:0,borderColor:'black',shadow:false,shadowAmgle:0,shadowWidth:0,shadowOffset:0,shadowDepth:0}");
        jqplotbuffer.append("});");
//            if ((ischeckedXA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-xaxis-tick\").hide();");
//                 }
//            if ((ischeckedYA).equalsIgnoreCase("true")){
//                  jqplotbuffer.append("$(\".jqplot-yaxis-tick\").hide();");
//                 }
//            jqplotbuffer.append("$(\"#" + chartId + "\").bind('jqplotDataRightClick',function (ev, seriesIndex, pointIndex, data, radius){ ");
//            jqplotbuffer.append("var x= (Math.round(data[0]*Math.pow(10,0))/Math.pow(10,0)).toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");");
//            jqplotbuffer.append("var y= (Math.round(data[1]*Math.pow(10,0))/Math.pow(10,0)).toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");");
//            jqplotbuffer.append("var r= (Math.round(data[2]*Math.pow(10,0))/Math.pow(10,0)).toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");");
//            jqplotbuffer.append(" var color = 'rgb(0,0,0)';");
//            jqplotbuffer.append(" $('.jqplot-cursor-tooltip').html('<span style=\"font-size:10px;font-weight:bold;color:' +");
//            jqplotbuffer.append("color + ';\">' + data[3] + '</span><br/>'+'<table><tr><td>' +'"+legendlabels.get(0).toString().replace("\"", "")+": ' +x+");
//            jqplotbuffer.append("'<br/>' + '"+legendlabels.get(1).toString().replace("\"", "")+": ' + y+ '<br/>' + '"+legendlabels.get(0).toString().replace("\"", "")+": ' + r)+'</td></tr></table>';});");
        if (!isFromOneView) {
            jqplotbuffer.append("$(\"#" + chartId + "\").bind(\"jqplotDataClick\", function(ev, i, j, data) {");
            jqplotbuffer.append("var reportid=" + reportid + ";");
            jqplotbuffer.append(" var value=data[2];");
            jqplotbuffer.append(" var viewbyid=" + nextviewbyid + ";");

            if (showlocaldrill) {
                jqplotbuffer.append(" $.ajax({");
                jqplotbuffer.append("url:'" + request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID='+reportid+'&CBOVIEW_BY" + viewById + "='+viewbyid+'&CBOARP" + viewbylist.get(0) + "='+value+'&type=drillgrph',");
                jqplotbuffer.append(" success: function(data){");
                jqplotbuffer.append(" window.location.href=window.location.href;");
                jqplotbuffer.append("}");
                jqplotbuffer.append("});");
            } else if (drilltype.equalsIgnoreCase("report")) {
                ArrayList url = container.getLinks();
                String Url = url.get(0).toString();
                jqplotbuffer.append("submitformStandard('" + Url + "',value,i,j,'" + drillmap + "');");
            } else {
                ArrayList url = container.getLinks();
                String oneUrl = "";
                if (url != null) {
                    oneUrl = url.get(0).toString();
                    jqplotbuffer.append("submitform('" + oneUrl + "',value,'" + isAdhocEnabled + "');");
                }
            }

            jqplotbuffer.append("}) ");
        }
//             
        return jqplotbuffer;
    }
}
