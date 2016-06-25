/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.charts;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Administrator
 */
public class PbFxXML {

    public String requestFrom = null;

    public StringBuffer getFxXML(HashMap singleRecord, HashMap GraphDetails, String userId, String reportId, String graphId, ArrayList links) {
        StringBuffer sbuffer = new StringBuffer("");
        ArrayList[] y_valueList = null;
        ArrayList x_valuesList = null;
        ArrayList series_namesList = null;
        ArrayList[] url_valueList = null;
        String graphType = null;
        String graphWidth = null;
        String graphHeight = null;
        String allow_legend = null;
        String allow_tooltip = null;
        String allow_gridlines = null;
        String legend_location = null;
        String font_color = null;
        String back_ground_color = null;
        String graphLYaxislabel = "";
        String graphRYaxislabel = "";
        String graphXaxislabel = "";
        boolean MultiAxisFlag = false;

        if (singleRecord != null && singleRecord.size() != 0) {
            y_valueList = (ArrayList[]) singleRecord.get("y_values");
            x_valuesList = (ArrayList) singleRecord.get("x_values");
            series_namesList = (ArrayList) singleRecord.get("series_names");
            url_valueList = (ArrayList[]) singleRecord.get("url_values");
            graphType = String.valueOf(GraphDetails.get("graphTypeName"));

            if (graphType.equalsIgnoreCase("Dual Axis") || graphType.equalsIgnoreCase("OverlaidBar") || graphType.equalsIgnoreCase("TargetBar") || graphType.equalsIgnoreCase("HorizontalTargetBar")) {
                MultiAxisFlag = true;
            }

            if (requestFrom != null && requestFrom.equalsIgnoreCase("DashBoard")) {
                graphWidth = "450";
                graphHeight = "270";
            } else {
                graphWidth = String.valueOf(GraphDetails.get("graphWidth"));
                graphHeight = String.valueOf(GraphDetails.get("graphHeight"));
            }
            allow_legend = GraphDetails.get("graphLegend") == null ? "Y" : String.valueOf(GraphDetails.get("graphLegend"));
            allow_tooltip = GraphDetails.get("graphToolTip") == null ? "Y" : String.valueOf(GraphDetails.get("graphToolTip"));
            allow_gridlines = GraphDetails.get("graphGridLines") == null ? "Y" : String.valueOf(GraphDetails.get("graphGridLines"));
            legend_location = String.valueOf(GraphDetails.get("graphLegendLoc"));
            font_color = String.valueOf(GraphDetails.get("graphFcolor"));
            back_ground_color = String.valueOf(GraphDetails.get("graphBcolor"));
            graphLYaxislabel = String.valueOf(GraphDetails.get("graphLYaxislabel"));
            graphRYaxislabel = String.valueOf(GraphDetails.get("graphRYaxislabel"));
            graphXaxislabel = singleRecord.get("x_label") == null ? "  " : singleRecord.get("x_label").toString();

            sbuffer.append("<Graphs>");
            sbuffer.append("<Graph>");
            sbuffer.append("<time_level>" + singleRecord.get("TimeLevel") + "</time_level>");
            sbuffer.append("<user_id>" + userId + "</user_id>");
            sbuffer.append("<report_id>" + reportId + "</report_id>");
            sbuffer.append("<graph_id>" + graphId + "</graph_id>");
            sbuffer.append("<img_file_name>" + (System.getProperty("java.io.tmpdir")).replace("\\", "/") + "/jfreechart-Fx-" + userId + "-" + reportId + "-" + graphId + ".gif</img_file_name>");

            sbuffer.append("<graph_type>" + graphType + "</graph_type>");
            sbuffer.append("<x_label>" + graphXaxislabel + "</x_label>");
            sbuffer.append("<l_y_label>" + graphLYaxislabel + "</l_y_label>");
            sbuffer.append("<r_y_label>" + graphRYaxislabel + "</r_y_label>");
            sbuffer.append("<graph_width>" + graphWidth + "</graph_width>");
            sbuffer.append("<graph_height>" + graphHeight + "</graph_height>");
            sbuffer.append("<allow_legend>" + allow_legend + "</allow_legend>");
            sbuffer.append("<allow_tooltip>" + allow_tooltip + "</allow_tooltip>");
            sbuffer.append("<allow_gridlines>" + allow_gridlines + "</allow_gridlines>");
            sbuffer.append("<legend_location>" + legend_location + "</legend_location>");
            sbuffer.append("<font_color>" + font_color + "</font_color>");
            sbuffer.append("<back_ground_color>" + back_ground_color + "</back_ground_color>");
            sbuffer.append("<max_Val>" + String.valueOf(singleRecord.get("maxVal")) + "</max_Val>");
            sbuffer.append("<min_Val>" + String.valueOf(singleRecord.get("minVal")) + "</min_Val>");
            sbuffer.append("<step_size>" + String.valueOf(singleRecord.get("stepSize")) + "</step_size>");
            sbuffer.append("<start_range>" + String.valueOf(singleRecord.get("minVal")) + "</start_range>");
            if (singleRecord.get("stepSize") != null) {
                sbuffer.append("<first_break>" + Double.parseDouble(String.valueOf(singleRecord.get("stepSize"))) * 2 + "</first_break>");
                sbuffer.append("<second_break>" + Double.parseDouble(String.valueOf(singleRecord.get("stepSize"))) * 4 + "</second_break>");
            } else {
                sbuffer.append("<first_break>" + String.valueOf(singleRecord.get("stepSize")) + "</first_break>");
                sbuffer.append("<second_break>" + String.valueOf(singleRecord.get("stepSize")) + "</second_break>");
            }
            sbuffer.append("<end_range>" + String.valueOf(singleRecord.get("maxVal")) + "</end_range>");
            sbuffer.append("<needle_value>" + String.valueOf(singleRecord.get("minVal")) + "</needle_value>");
            sbuffer.append("<series_list>");
            for (int j = 0; j < series_namesList.size(); j++) {
                sbuffer.append("<series_name>" + series_namesList.get(j) + "</series_name>");
            }
            sbuffer.append("</series_list>");
            sbuffer.append("<x_values>");
            for (int j = 0; j < x_valuesList.size(); j++) {
                sbuffer.append("<single_x_value>" + x_valuesList.get(j) + "</single_x_value>");
            }
            sbuffer.append("</x_values>");
            for (int k = 0; k < y_valueList.length; k++) {
                sbuffer.append("<y_values>");
                if (y_valueList[k] != null) {
                    for (int l = 0; l < y_valueList[k].size(); l++) {
                        sbuffer.append("<single_y_value>" + y_valueList[k].get(l) + "</single_y_value>");
                    }
                }
                sbuffer.append("</y_values>");
            }
            if (links != null && links.size() != 0) {
                for (int k = 0; k < url_valueList.length; k++) {
                    sbuffer.append("<url_values>");
                    if (url_valueList[k] != null) {
                        for (int l = 0; l < url_valueList[k].size(); l++) {
                            if (links != null && links.size() != 0) {
                                sbuffer.append("<single_url_value>" + links.get(0).toString().replaceAll("&", "~") + url_valueList[k].get(l) + "</single_url_value>");
                            } else {
                                sbuffer.append("<single_url_value>reportViewer.do?reportBy=viewReport~REPORTID=" + reportId + "</single_url_value>");
                            }
                        }
                    }
                    sbuffer.append("</url_values>");
                }
            }

            if (MultiAxisFlag) {

                ////////////////////////.println("singleRecord is "+singleRecord);
                ArrayList[] bary_valueList = (ArrayList[]) singleRecord.get("bary_values");//TargetBar
                ArrayList barseries_namesList = (ArrayList) singleRecord.get("barseries_names");
                ArrayList[] barurl_valueList = (ArrayList[]) singleRecord.get("barurl_values");
                ArrayList[] liney_valueList = (ArrayList[]) singleRecord.get("liney_values");
                ArrayList lineseries_namesList = (ArrayList) singleRecord.get("lineseries_names");
                ArrayList[] lineurl_valueList = (ArrayList[]) singleRecord.get("lineurl_values");

                sbuffer.append("<bar-data>");
                sbuffer.append("<series_list>");
                for (int j = 0; j < barseries_namesList.size(); j++) {
                    sbuffer.append("<series_name>" + barseries_namesList.get(j) + "</series_name>");
                }
                sbuffer.append("</series_list>");

                sbuffer.append("<x_values>");
                for (int j = 0; j < x_valuesList.size(); j++) {
                    sbuffer.append("<single_x_value>" + x_valuesList.get(j) + "</single_x_value>");
                }
                sbuffer.append("</x_values>");

                for (int k = 0; k < bary_valueList.length; k++) {
                    sbuffer.append("<y_values>");
                    if (bary_valueList[k] != null) {
                        for (int l = 0; l < bary_valueList[k].size(); l++) {
                            sbuffer.append("<single_y_value>" + bary_valueList[k].get(l) + "</single_y_value>");
                        }
                    }
                    sbuffer.append("</y_values>");
                }
                for (int k = 0; k < barurl_valueList.length; k++) {
                    sbuffer.append("<url_values>");
                    if (barurl_valueList[k] != null) {
                        for (int l = 0; l < barurl_valueList[k].size(); l++) {
                            if (links != null && links.size() != 0) {
                                sbuffer.append("<single_url_value>" + links.get(0).toString().replaceAll("&", "~") + barurl_valueList[k].get(l) + "</single_url_value>");
                            } else {
                                sbuffer.append("<single_url_value>reportViewer.do?reportBy=viewReport~REPORTID=" + reportId + "</single_url_value>");
                            }
                        }
                    }
                    sbuffer.append("</url_values>");
                }
                sbuffer.append("</bar-data>");

                sbuffer.append("<line-data>");
                sbuffer.append("<series_list>");
                for (int j = 0; j < lineseries_namesList.size(); j++) {
                    sbuffer.append("<series_name>" + lineseries_namesList.get(j) + "</series_name>");
                }
                sbuffer.append("</series_list>");

                sbuffer.append("<x_values>");
                for (int j = 0; j < x_valuesList.size(); j++) {
                    sbuffer.append("<single_x_value>" + x_valuesList.get(j) + "</single_x_value>");
                }
                sbuffer.append("</x_values>");

                for (int k = 0; k < liney_valueList.length; k++) {
                    sbuffer.append("<y_values>");
                    if (liney_valueList[k] != null) {
                        for (int l = 0; l < liney_valueList[k].size(); l++) {
                            sbuffer.append("<single_y_value>" + liney_valueList[k].get(l) + "</single_y_value>");
                        }
                    }
                    sbuffer.append("</y_values>");
                }
                for (int k = 0; k < lineurl_valueList.length; k++) {
                    sbuffer.append("<url_values>");
                    if (lineurl_valueList[k] != null) {
                        for (int l = 0; l < lineurl_valueList[k].size(); l++) {
                            if (links != null && links.size() != 0) {
                                sbuffer.append("<single_url_value>" + links.get(0).toString().replaceAll("&", "~") + lineurl_valueList[k].get(l) + "</single_url_value>");
                            } else {
                                sbuffer.append("<single_url_value>reportViewer.do?reportBy=viewReport~REPORTID=" + reportId + "</single_url_value>");
                            }
                        }
                    }
                    sbuffer.append("</url_values>");
                }
                sbuffer.append("</line-data>");
            }

            sbuffer.append("</Graph>");
            sbuffer.append("</Graphs>");
        }

        return sbuffer;
    }

    public String getFxChartsFunNames() {
        StringBuffer sbuffer = new StringBuffer();

        //sbuffer.append("");
        sbuffer.append("if (graphType == \"Bar\") {");
        sbuffer.append("myApp.script.barChart2D();");
        sbuffer.append("} else if (graphType == \"Bar3D\") {");
        sbuffer.append(" myApp.script.barChart3D();");
        sbuffer.append("}else if (graphType == \"Pareto\") {");
        sbuffer.append("myApp.script.paretochart();");
        sbuffer.append(" } else if (graphType == \"TargetBar\") {");
        sbuffer.append(" myApp.script.TargetBar();");
        sbuffer.append("} else if (graphType == \"Line\" || graphType == \"Line3D\") {");
        sbuffer.append(" myApp.script.lineChart2D();");
        sbuffer.append("} else if (graphType == \"Stacked\" || graphType == \"Stacked3D\" || graphType == \"HorizontalStacked3D\" || graphType == \"HorizontalStacked\") {");
        sbuffer.append(" myApp.script.stackbar();");
        sbuffer.append("} else if (graphType == \"Pie\") {");
        sbuffer.append("myApp.script.pieChart2D();");
        sbuffer.append("} else if (graphType == \"Pie3D\") {");
        sbuffer.append("myApp.script.pieChart3D();");
        sbuffer.append("} else if (graphType == \"Meter\" || graphType == \"meter\") {");
        sbuffer.append(" myApp.script.meter();");
        sbuffer.append("} else if (graphType == \"Dual Axis\") {");
        sbuffer.append(" myApp.script.dualAxis();");
        sbuffer.append("} else if (graphType == \"OverlaidBar\") {");
        sbuffer.append("myApp.script.overlaid();");
        sbuffer.append("} else if (graphType == \"Column\" || graphType == \"Column3D\") {");
        sbuffer.append(" myApp.script.column2D();");
        sbuffer.append("} else if (graphType == \"Cone\") {");
        sbuffer.append("myApp.script.cone();");
        sbuffer.append("} else if (graphType == \"Area\") {");
        sbuffer.append(" myApp.script.areaChart2D();");
        sbuffer.append("} else if (graphType == \"Pyramid\") {");
        sbuffer.append(" myApp.script.pyramid();");
        sbuffer.append(" } else if (graphType == \"HorizCone\") {");
        sbuffer.append("myApp.script.horizcone();");
        sbuffer.append("} else if (graphType == \"ColumnPie\" || graphType == \"ColumnPie3D\") {");
        sbuffer.append("myApp.script.columnpie();");
        sbuffer.append("} else if (graphType == \"Ring\") {");
        sbuffer.append(" myApp.script.ring2D();");
        sbuffer.append("} else if (graphType == \"PieRing\") {");
        sbuffer.append("myApp.script.pie_in_ring();");
        sbuffer.append("} else if (graphType == \"Bubble\") {");
        sbuffer.append(" myApp.script.bubblechart();");
        sbuffer.append("} else if (graphType == \"Thermometer\") {");
        sbuffer.append("myApp.script.thermometer();");
        sbuffer.append("} else if (graphType == \"Waterfall\") {");
        sbuffer.append("myApp.script.waterfallchart();");
        sbuffer.append("} else if (graphType == \"TimeSeries\") {");
        sbuffer.append("myApp.script.timeserieschart();");
        sbuffer.append("} else if (graphType == \"HorizontalTargetBar\") {");
        sbuffer.append("myApp.script.horizontaltargetbar();");
        sbuffer.append("}");

        return sbuffer.toString();

    }
}
