/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.graph;

import com.google.common.collect.ArrayListMultimap;
import com.progen.bd.ProgenJqplotGraphBD;
import com.progen.charts.JqplotGraphProperty;
import com.progen.charts.ProgenChartDisplay;
import com.progen.charts.ProgenJQPlotGraph;
import com.progen.dashboard.DashboardConstants;
import com.progen.graph.info.GraphTypeInfo;
import com.progen.graph.info.ProgenGraphInfo;
import com.progen.report.DashletDetail;
import com.progen.report.charts.PbGraphDisplay;
import com.progen.report.entities.GraphReport;
import com.progen.report.entities.KPIGraph;
import com.progen.report.entities.QueryDetail;
import com.progen.report.entities.Report;
import com.progen.report.pbDashboardCollection;
import com.progen.reportdesigner.action.DashboardTemplateAction;
import com.progen.reportdesigner.bd.DashboardTemplateBD;
import com.progen.reportdesigner.db.DashboardTemplateDAO;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class GraphBuilder {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private boolean fxCharts;
    private String dashboardid;
    private String graphType;

    public String displayGraphs(Container container, String dashletId, String contextPath, boolean fromDesigner, String editDbrd) throws Exception {

        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        container.getDashboardId();
        DashletDetail detail = collect.getDashletDetail(dashletId);
        String dispType = detail.getDisplayType();
        String graphHtml = "";

        if (dispType.equalsIgnoreCase(DashboardConstants.KPI_GRAPH_REPORT)) {
            graphHtml = createKPIGraph(container, dashletId, fromDesigner, editDbrd);
        } else if (DashboardConstants.GRAPH_REPORT.equalsIgnoreCase(dispType)
                || DashboardConstants.DASHBOARD_GRAPH_REPORT.equalsIgnoreCase(dispType) || DashboardConstants.KPI_WITH_GRAPH.equalsIgnoreCase(dispType)) {
            graphHtml = createGraph(container, dashletId, contextPath, fromDesigner, editDbrd);
        }

        return graphHtml;
    }

    private String createKPIGraph(Container container, String dashletId, boolean fromDesigner, String editDbrd) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        HttpSession hs = request.getSession();
        String measureName = "";
        String dashname = "";
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail detail = collect.getDashletDetail(dashletId);
        String folderId = collect.reportBizRoles[0];
        String graphId = detail.getGraphId();
        Report reportDetails = detail.getReportDetails();
        String aggType = reportDetails.getKPIElements().get(0).getAggregationType();
        String myGraphType = "";
        PrintWriter out = response.getWriter();
        Document document = null;
        Element root = null;
        StringBuilder graphHtml = new StringBuilder();
        KPIGraph kpiGrp = (KPIGraph) reportDetails;
        String dashboardid = container.getDashboardId();
        String dashBoardId = this.getDashboardid();
        measureName = kpiGrp.getKpiName();
        if (kpiGrp.getKpigrname() != null && !kpiGrp.getKpigrname().equalsIgnoreCase("")) {
            dashname = kpiGrp.getKpigrname();
        } else {
            dashname = measureName;
        }
        String elementId = "A_" + kpiGrp.getElementId();
        double kpiGrpHeight = kpiGrp.getGraphHeight();
        double kpiGrpWidth = kpiGrp.getGraphWidth();
        String dashId = detail.getDashBoardDetailId();
        DashboardTemplateDAO templateDAO = new DashboardTemplateDAO();
        DashboardTemplateBD dashboardbd = new DashboardTemplateBD();
        String jqgraphtype = templateDAO.getJqPlotKpiGraphType(dashletId);
        String dayTgtValue1 = null;
        String baseVal = "";

        if (!isFxCharts()) {

            document = builder.build(new ByteArrayInputStream(kpiGrp.getGraphXML().getBytes()));
            root = document.getRootElement();
            PbReturnObject kpipbretObj1 = container.getKpiRetObj();
            List measType1 = root.getChildren("MeasureType");
            List basis1 = root.getChildren("Basis");
            if (!basis1.isEmpty()) {
                Element basisValue1 = (Element) basis1.get(0);
                if (basisValue1.getText().equalsIgnoreCase("deviation")) {
                    baseVal = "deviation";
                    List row1 = root.getChildren("TargetValues");
                    for (int i = 0; i < row1.size(); i++) {
                        Element TargetValues = (Element) row1.get(i);
                        List dayTarget1 = TargetValues.getChildren("DayTarget");
                        for (int j = 0; j < dayTarget1.size(); j++) {
                            Element DayTargetValue = (Element) dayTarget1.get(j);
                            dayTgtValue1 = DayTargetValue.getText();
                        }
                    }
                }
            }
        }
        List<DashletDetail> dashletDetails = collect.dashletDetails;
        ArrayListMultimap<Integer, Integer> rowinfo = ArrayListMultimap.create();
        for (int i = 0; i < dashletDetails.size(); i++) {
            DashletDetail dashlet = dashletDetails.get(i);
            rowinfo.put(dashlet.getRow(), dashlet.getCol());
        }
        List<Integer> dashlets = rowinfo.get(Integer.parseInt(dashId));
        int numOfDashlets = dashlets.size();

        int rowNum = detail.getRow();
        int colNum = detail.getCol();
//        int graphWidth=320;
        int rowSpan = detail.getRowSpan();
        int colSpan = detail.getColSpan();
        //changes for kpidrill by anitha
        String dashletName = detail.getDashletName();
        String dashboardDashletID = detail.getDashBoardDetailId();
        int height = 350;
        int width = 500;
        height = height * rowSpan;
        width = width * colSpan;
        String[] KPIGraphTypes = {"Meter", "Thermometer"};
        //Create the dashlet header
        if (!collect.isOneviewCheckForKpis()) {
            graphHtml.append("<div id=\"DashletHeader-" + dashletId + "\" class=\"portlet-header1 navtitle portletHeader ui-corner-all\"  align=\"center\">");
            graphHtml.append("<table width=\"100%\"><tr>");
            graphHtml.append("<td><a href=\"javascript:void(0)\" onclick=\"editKPIName('" + dashletId + "','" + collect.reportId + "','" + folderId + "','" + graphId + "','" + dashname + "','" + collect.reportId + "','" + detail.getKpiMasterId() + "','" + detail.getDisplaySequence() + "','" + detail.getDisplayType() + "','" + measureName + "','" + fromDesigner + "','" + editDbrd + "')\"><strong>" + dashname + "</strong></a></td>");
            graphHtml.append("<td align='right'>");
            graphHtml.append("<table><tr>");
            graphHtml.append("<td><a href=\"javascript:void(0)\" class=\"ui-icon ui-icon-gear\" title=\"Drill\" onclick=\"KPIDrillindb('" + folderId + "','" + graphId + "','" + dashletName + "')\"></a></td>");
            graphHtml.append("<td><a href=\"javascript:void(0)\" class=\"ui-icon ui-icon-clock\" title=\"Drill\" onclick=\"getDbrdTimeDisplay('" + collect.reportId + "')\"></a></td>");
            if (baseVal.equalsIgnoreCase("deviation")) {
                graphHtml.append("<td><a href=\"javascript:void(0)\" onclick=\"editKPITarget('" + dashletId + "','" + elementId + "','" + measureName + "','" + "PRG_STD" + "','" + "proRated" + "','" + dashboardDashletID + "','" + folderId + "','" + dayTgtValue1 + "','" + aggType + "')\">");
                graphHtml.append("<td align=\"right\">");
                graphHtml.append("<a class=\"ui-icon ui-icon-pencil\" href=\"javascript:void(0)\"  onclick=\"editGraph(document.getElementById('GrpType" + dashletId + "-" + dashId + "'))\" style='text-decoration:none' class=\"calcTitle\" title=\"GraphTypes\"></a>");
                graphHtml.append("<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='GrpType" + dashletId + "-" + dashId + "'>");
                graphHtml.append("<Table>");
                for (int i = 0; i < KPIGraphTypes.length; i++) {
                    graphHtml.append("<Tr valign='top'>");
                    graphHtml.append("<Td valign='top'>");
                    graphHtml.append("<a href='javascript:void(0)' onclick=\"openkpiGrpType('" + KPIGraphTypes[i] + "','" + dashletId + "','" + collect.reportId + "','" + folderId + "','" + graphId + "','" + dashletName + "','" + detail.getKpiMasterId() + "','" + detail.getDisplaySequence() + "','" + detail.getDisplayType() + "','" + measureName + "','" + fromDesigner + "','" + editDbrd + "')\">" + KPIGraphTypes[i] + "</a>");
                    graphHtml.append("</Td>");
                    graphHtml.append("</Tr>");
                }
                graphHtml.append("</Table>");//closing of inner table
                graphHtml.append(" </div>");
                graphHtml.append("</td>");
                graphHtml.append("<td><a href=\"javascript:void(0)\" onclick=\"editKPITarget('" + dashletId + "','" + elementId + "','" + measureName + "','" + "PRG_STD" + "','" + "proRated" + "','" + dashboardDashletID + "','" + folderId + "')\">");

                graphHtml.append("Edit Target</a></td>");
            }
            graphHtml.append("<td align=\"right\"><a class=\"ui-icon ui-icon-image\" href=\"javascript:void(0)\"  onclick=\"jqplotMeterchart('Dashlets-" + dashletId + "','" + dashletId + "','" + dashBoardId + "')\" ></a></td>");
            if (fromDesigner) {
                graphHtml.append("<td> <img style=\"width:20px;\"  src='images/clear-icon.png' onclick=\"clearDashlet('" + dashId + "','" + numOfDashlets + "','" + rowNum + "','" + colNum + "','" + rowSpan + "','" + colSpan + "')\" /></td>");
                graphHtml.append("<td align=\"right\"><a class=\"ui-icon ui-icon-trash\" href=\"javascript:void(0)\"  onclick=\"closeOldPortlet('Dashlets-" + dashletId + "','" + dashletId + "')\" ></a></td>");
            } else if ("true".equals(editDbrd)) {
                graphHtml.append("<td> <img style=\"width:20px;\"  src='images/clear-icon.png' onclick=\"clearDashlet('" + dashId + "','" + numOfDashlets + "','" + rowNum + "','" + colNum + "','" + rowSpan + "','" + colSpan + "')\" /></td>");
                graphHtml.append("<td align=\"right\"><a class=\"ui-icon ui-icon-trash\" href=\"javascript:void(0)\"  onclick=\"closeOldPortlet('Dashlets-" + dashletId + "','" + dashletId + "')\" ></a></td>");
            }
            graphHtml.append("</tr></table>");
            graphHtml.append("</td>");
            graphHtml.append("</tr></table>");
            graphHtml.append("</div>");
        }
        //Create the dashlet graph
        if (!collect.isOneviewCheckForKpis()) {
            graphHtml.append("<div  id='zoom" + graphId + "'  style=\"width:100%;height:" + height + "px;overflow-y:auto;overflow-x:auto\"  align=\"center\">");
        }
//         if(collect.isOneviewCheckForKpis()){
//          graphHtml.append("<div  id='zoom" + graphId + "'  style=\"width:100%;height:"+(Integer.parseInt(collect.getOneViewHeight()))+"px;overflow-y:auto;overflow-x:auto\"  align=\"center\">");
//         }
//         else{
//             graphHtml.append("<div  id='zoom" + graphId + "'  style=\"width:100%;height:"+height+"px;overflow-y:auto;overflow-x:auto\"  align=\"center\">");
//         }

        String dayTgtValue = null;
        String weekTgtValue = null;
        String monthTgtValue = null;
        String qtrTgtValue = null;
        String yearTgtValue = null;

        DashboardTemplateAction dbrbtempAction = new DashboardTemplateAction();
        if (!isFxCharts()) {

            document = builder.build(new ByteArrayInputStream(kpiGrp.getGraphXML().getBytes()));
            root = document.getRootElement();
            PbReturnObject kpipbretObj = container.getKpiRetObj();
            String targetValue = "";
            float deviation = 0.0f;
            double needle = 0.0;
            double sR = kpiGrp.getStartRange();
            double eR = kpiGrp.getEndRange();
            double fB = kpiGrp.getFsplit();
            double sB = kpiGrp.getSsplit();
            String colorchange = "";
            double sRange = 0.0;
            double eRange = 0.0;
            double fBreak = 0.0;
            double sBreak = 0.0;

            /*
             * if(sR<fB && fB<sB && sB<eR ) { sRange = sR; eRange = eR ; fBreak
             * = fB; sBreak = sB ; colorchange="no"; } else if(sR>fB && fB>sB &&
             * sB>eR ) { sRange = eR; eRange = sR ; fBreak = sB; sBreak = fB ;
             * colorchange="yes"; } else if(fB>sB) { fBreak=sB; sBreak=fB; }
             */

            String actualValue = String.valueOf(kpipbretObj.getFieldValueInt(0, elementId));

            List measType = root.getChildren("MeasureType");
            Element type = (Element) measType.get(0);
            List basis = root.getChildren("Basis");
            Element basisValue = (Element) basis.get(0);

            if (basisValue.getText().equalsIgnoreCase("deviation")) {
                List row = root.getChildren("TargetValues");
                for (int i = 0; i < row.size(); i++) {
                    Element TargetValues = (Element) row.get(i);
                    List dayTarget = TargetValues.getChildren("DayTarget");
                    for (int j = 0; j < dayTarget.size(); j++) {
                        Element DayTargetValue = (Element) dayTarget.get(j);
                        dayTgtValue = DayTargetValue.getText();
                    }
                    List weekTarget = TargetValues.getChildren("WeekTarget");
                    for (int j = 0; j < weekTarget.size(); j++) {
                        Element weekTargetValue = (Element) weekTarget.get(j);
                        weekTgtValue = weekTargetValue.getText();
                    }
                    List monthTarget = TargetValues.getChildren("MonthTarget");
                    for (int j = 0; j < monthTarget.size(); j++) {
                        Element monthTargetValue = (Element) monthTarget.get(j);
                        monthTgtValue = monthTargetValue.getText();
                    }
                    List qtrTarget = TargetValues.getChildren("QtrTarget");
                    for (int j = 0; j < qtrTarget.size(); j++) {
                        Element qtrTargetValue = (Element) qtrTarget.get(j);
                        qtrTgtValue = qtrTargetValue.getText();
                    }
                    List yearTarget = TargetValues.getChildren("YearTarget");
                    for (int j = 0; j < yearTarget.size(); j++) {
                        Element yearTargetValue = (Element) yearTarget.get(j);
                        yearTgtValue = yearTargetValue.getText();
                    }
                }
                if (collect.timeDetailsArray.get(0).toString().equalsIgnoreCase("Day")) {
                    if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                        targetValue = dayTgtValue;
                        deviation = templateDAO.getDeviation(actualValue, dayTgtValue);
                        needle = Double.parseDouble(String.valueOf(deviation));
                    } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Day")) {
                        targetValue = dayTgtValue;
                        deviation = templateDAO.getDeviation(actualValue, dayTgtValue);
                        needle = Double.parseDouble(String.valueOf(deviation));
                    } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Week")) {
                        targetValue = weekTgtValue;
                        deviation = templateDAO.getDeviation(actualValue, weekTgtValue);
                        needle = Double.parseDouble(String.valueOf(deviation));
                    } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Month")) {
                        targetValue = monthTgtValue;
                        deviation = templateDAO.getDeviation(actualValue, monthTgtValue);
                        needle = Double.parseDouble(String.valueOf(deviation));
                    } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Qtr")) {
                        targetValue = qtrTgtValue;
                        deviation = templateDAO.getDeviation(actualValue, qtrTgtValue);
                        needle = Double.parseDouble(String.valueOf(deviation));
                    } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Year")) {
                        targetValue = yearTgtValue;
                        deviation = templateDAO.getDeviation(actualValue, yearTgtValue);
                        needle = Double.parseDouble(String.valueOf(deviation));
                    }
                } else if (collect.timeDetailsArray.get(0).toString().equalsIgnoreCase("Week")) {
                    if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Week")) {
                        targetValue = weekTgtValue;
                        deviation = templateDAO.getDeviation(actualValue, weekTgtValue);
                        needle = Double.parseDouble(String.valueOf(deviation));
                    } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Month")) {
                        targetValue = monthTgtValue;
                        deviation = templateDAO.getDeviation(actualValue, monthTgtValue);
                        needle = Double.parseDouble(String.valueOf(deviation));
                    } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Qtr")) {
                        targetValue = qtrTgtValue;
                        deviation = templateDAO.getDeviation(actualValue, qtrTgtValue);
                        needle = Double.parseDouble(String.valueOf(deviation));
                    } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Year")) {
                        targetValue = yearTgtValue;
                        deviation = templateDAO.getDeviation(actualValue, yearTgtValue);
                        needle = Double.parseDouble(String.valueOf(deviation));
                    }
                } else if (collect.timeDetailsArray.get(0).toString().equalsIgnoreCase("Month")) {
                    if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Month")) {
                        targetValue = monthTgtValue;
                        deviation = templateDAO.getDeviation(actualValue, monthTgtValue);
                        needle = Double.parseDouble(String.valueOf(deviation));
                    } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Qtr")) {
                        targetValue = qtrTgtValue;
                        deviation = templateDAO.getDeviation(actualValue, qtrTgtValue);
                        needle = Double.parseDouble(String.valueOf(deviation));
                    } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Year")) {
                        targetValue = yearTgtValue;
                        deviation = templateDAO.getDeviation(actualValue, yearTgtValue);
                        needle = Double.parseDouble(String.valueOf(deviation));
                    }
                } else if (collect.timeDetailsArray.get(0).toString().equalsIgnoreCase("Quarter")) {
                    if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Qtr")) {
                        targetValue = qtrTgtValue;
                        deviation = templateDAO.getDeviation(actualValue, qtrTgtValue);
                        needle = Double.parseDouble(String.valueOf(deviation));
                    } else if (collect.timeDetailsArray.get(3).toString().equalsIgnoreCase("Year")) {
                        targetValue = yearTgtValue;
                        deviation = templateDAO.getDeviation(actualValue, yearTgtValue);
                        needle = Double.parseDouble(String.valueOf(deviation));
                    }
                } else if (collect.timeDetailsArray.get(0).toString().equalsIgnoreCase("Year")) {
                    targetValue = yearTgtValue;
                    deviation = templateDAO.getDeviation(actualValue, yearTgtValue);
                    needle = Double.parseDouble(String.valueOf(deviation));
                }
            } else {
                needle = Double.parseDouble(String.valueOf(kpipbretObj.getFieldValueInt(0, elementId)));
            }
            ProgenChartDisplay pchart = null;
            if (!collect.isOneviewCheckForKpis()) {
                pchart = new ProgenChartDisplay((int) kpiGrpWidth, (int) kpiGrpHeight);
            } else {
                pchart = new ProgenChartDisplay(Integer.parseInt(collect.getOneViewWidth()), Integer.parseInt(collect.getOneViewHeight()));
            }
            pchart.setCtxPath(request.getContextPath());
            if (type.getText().equalsIgnoreCase("Standard")) {
                colorchange = "no";       //Need to check in which case value of yes should be passed
            }
            pchart.setColorchange(colorchange);
            if (sRange == 0.0) {
                sRange = sR;
            }
            if (eRange == 0.0) {
                eRange = eR;
            }
            if (fBreak == 0.0) {
                fBreak = fB;
            }
            if (sBreak == 0.0) {
                sBreak = sB;
            }
            if (basisValue.getText().equalsIgnoreCase("deviation")) {
                if (deviation < sRange) {
                    sRange = Math.round(deviation - 10);
                } else if (deviation > eRange) {
                    eRange = Math.round(deviation + 10);
                }
            } else {
                if (needle < sRange) {
                    sRange = Math.round(needle - 10);
                } else if (needle > eRange) {
                    eRange = Math.round(needle + 10);
                }
            }

            if (this.getGraphType() != null && this.getGraphType().equalsIgnoreCase("metter")) {
                DashboardTemplateBD dashboardbd1 = new DashboardTemplateBD();
                String metergraph = dashboardbd1.prepareMeterGraph(sRange, fB, sBreak, eRange, needle, dashletId);
                graphHtml.append(metergraph);
                //graphHtml.append(dashletId);
                graphHtml.append("<div id='chart-" + dashletId + "' style=\"width:500px; height:250px;\" align=\"left\" possition=\"\"></div>");
            } else {
                if (collect.isOneviewCheckForKpis()) {
                    String divWidth = collect.getOneViewWidth();
                    String divHeight = collect.getOneViewHeight();
                    graphHtml.append("<div id='chart-" + dashletId + "' style=\"width:" + divWidth + "px; height:" + divHeight + "px;\" align=\"\" possition=\"\"></div>");
//                    DashboardTemplateBD templateBd=new DashboardTemplateBD();
                    ProgenJQPlotGraph jqGraph = new ProgenJQPlotGraph();
                    if (Integer.parseInt(divHeight) <= 80) {
                        jqGraph.setShowTickLabels(false);
                    }
//                    String metergraph= templateBd.prepareMeterGraph(sRange,fB,sBreak, eRange, needle, dashletId);
                    graphHtml.append(jqGraph.prepareMeterGraph(sRange, fB, sBreak, eRange, needle, dashletId));

                } else {
                    if (kpiGrp.getKpiGraphType().trim().equalsIgnoreCase("meter")) {
                        pchart.GetMeterChart(sRange, eRange, fBreak, sBreak, needle, "", hs, response, out);
                    } else {
                        pchart.GetThermChart(sRange, eRange, fBreak, sBreak, needle, hs, response, out);
                    }
                    graphHtml = buildGraphs(pchart, graphHtml);
                }
            }
            if (!collect.isOneviewCheckForKpis()) {
                graphHtml.append("<Table  style=\"width:99%\"  cellpadding=\"0\" cellspacing=\"1\" id=\"tablesorter\"  class=\"tablesorter\" >");
                graphHtml.append("<thead><tr>");
                graphHtml.append("<th><strong>Measure</strong></th>");
                if (basisValue.getText().equalsIgnoreCase("deviation")) {
                    graphHtml.append("<th><strong>Target Value</strong></th>");
                }
                graphHtml.append("<th><strong>Actual Value</strong></th>");
                if (basisValue.getText().equalsIgnoreCase("deviation")) {
                    graphHtml.append("<th><strong>Deviation(%)</strong></th>");
                }
                graphHtml.append("</tr></thead>");
                graphHtml.append("<td><strong>" + measureName + "</strong></td>");
                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                if (basisValue.getText().equalsIgnoreCase("deviation")) {
                    graphHtml.append("<td>" + formatter.format(Double.parseDouble(targetValue)) + "</td>");
                }
                graphHtml.append("<td>" + formatter.format(Double.parseDouble(String.valueOf(kpipbretObj.getFieldValueInt(0, elementId)))) + "</td>");
                if (basisValue.getText().equalsIgnoreCase("deviation")) {
                    graphHtml.append("<td>" + deviation + "</td>");
                }
                graphHtml.append("<tfoot></tfoot><tbody>");
                graphHtml.append("</tbody></Table>");
            }
//                }
        } else {
            graphHtml = buildFXGraphs(collect, graphHtml, dashletId, myGraphType);
        }
        if (!collect.isOneviewCheckForKpis()) {
            graphHtml.append(" </div>");
        }
        return graphHtml.toString();
    }

    private String createGraph(Container container, String dashletId, String contextPath, boolean fromDesigner, String editDbrd) throws Exception {

//        String fromDesigner=request.getParameter("fromDesigner");
        PbDb db = new PbDb();

        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
//        collect.getdash
        String dashBoardId = container.getDashboardId();
        if (dashBoardId == null) {
            dashBoardId = collect.reportId;
        }
        if (editDbrd != null && "true".equalsIgnoreCase(editDbrd)) {
            fromDesigner = true;
        }
        DashletDetail detail = collect.getDashletDetail(dashletId);
        JqplotGraphProperty jqprop = new JqplotGraphProperty();
        jqprop = detail.getJqplotgrapprop();
        if (jqprop != null) {
            detail.setJqplotGraph(jqprop.isJqPlot);
            detail.setJqPlotGraphType(jqprop.getGraphTypename());
//            detail.
        }

        GraphReport graphDetails = (GraphReport) detail.getReportDetails();
        String refReportId = detail.getRefReportId();
        String graphId = detail.getGraphId();
        String kpiMasterId = detail.getKpiMasterId();
        String dispSequence = String.valueOf(detail.getDisplaySequence());
        String dispType = detail.getDisplayType();
        String dashletName = detail.getDashletName();
        Report reportDetails = detail.getReportDetails();
        int rowSpan = detail.getRowSpan();
        int colSpan = detail.getColSpan();
        int height = 350;
        height = height * rowSpan;
        String myGraphType = "";
        String isJqPlotSelected = "";
        Map<Integer, GraphTypeInfo> GraphTypesHashMap = null;
        boolean flag = true;
        GraphTypesHashMap = ProgenGraphInfo.getGraphTypes();
        Set<Integer> GraphTypesHashMapKeySet = GraphTypesHashMap.keySet();

        String dashId = detail.getDashBoardDetailId();

        List<DashletDetail> dashletDetails = collect.dashletDetails;
        ArrayListMultimap<Integer, Integer> rowinfo = ArrayListMultimap.create();
        for (int i = 0; i < dashletDetails.size(); i++) {
            DashletDetail dashlet = dashletDetails.get(i);
            rowinfo.put(dashlet.getRow(), dashlet.getCol());
        }
        List<Integer> dashlets = rowinfo.get(Integer.parseInt(dashId));
        int numOfDashlets = dashlets.size();

        int rowNum = detail.getRow();
        int colNum = detail.getCol();

        PrintWriter out = response.getWriter();
        StringBuilder graphHtml = new StringBuilder();
        int dashletNameLength = 0;
        if (dashletName != null) {
            dashletNameLength = dashletName.length();
        }
        if (detail.isJqplotGraph()) {
            isJqPlotSelected = "checked";
        }


        String folderId = collect.reportBizRoles[0];
        String newDashletName = "";
        int maxCharLength = getGraphNameMaxCharacters(collect, dashletId);
        if (dashletNameLength > maxCharLength) {
            newDashletName = detail.getDashletName().substring(0, maxCharLength);
            newDashletName += "...";
        } else {
            newDashletName = dashletName;
        }
        boolean isOLAPEnabled = graphDetails.isOLAPEnabled();
        if (!isOLAPEnabled) {
//      if(detail.getKpiType() != null && detail.getDisplayType().equalsIgnoreCase("KpiWithGraph"))
//      {
//          List<QueryDetail> queryDetail=graphDetails.getQueryDetails();
//          String kpiElementName=queryDetail.get(0).getDisplayName();
//          height=320;
//          height=height*rowSpan;
//          graphHtml.append("<div id=\"Dashlets-"+dashletId+"-graph\" class=\"\" style=\"width:100%; height:345px\">");
//          graphHtml.append("<div id=\"DashletHeader-"+dashletId+"\" class=\"portlet-header1 navtitle portletHeader ui-corner-all\"  align=\"center\">");
//          graphHtml.append("<Table width=\"100%\">");
//          graphHtml.append("<tr>");
//          graphHtml.append("<td align=\"left\" ><strong>"+kpiElementName+"</strong></td>");
//          graphHtml.append("<td align=\"right\">");
//          graphHtml.append("<table><tr>");
//          graphHtml.append("<td align=\"center\" ><a class=\"ui-icon ui-icon-image\" href='javascript:void(0)' title=\"GraphTypes\" onclick=\"graphTypesDisp(document.getElementById('dispgrptypes" + graphId + "'))\" ></a>");
//          graphHtml.append("<div style='display:none;width:100px;height:"+height+"px;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;'  id='dispgrptypes" + graphId + "'>");
//          graphHtml.append("<Table >");
//          Iterator<Integer> iter = GraphTypesHashMapKeySet.iterator();
//                while(iter.hasNext())
//                {
//                    String typeGraph=GraphTypesHashMap.get(iter.next()).getGraphTypeName();
//
//        //        }
//        //        Iterator<Integer> itertr = GraphTypesHashMapKeySet.iterator();
//        //        while(itertr.hasNext()) {
//                    graphHtml.append("<Tr>");
//        //            Array id=GraphTypesHashMap.get(GraphTypesHashMapKeySet).toString();
//                    graphHtml.append("<Td  id='" + typeGraph + "'>");
//                    graphHtml.append("<a href='javascript:void(0)'    onclick=\"changeGraphTypeOnKpi('" + dashletId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + dispSequence + "','" + dispType + "','" + dashletName + "',  '" +typeGraph + "', '"+fromDesigner+"')\"     title='" + typeGraph + "' > " +typeGraph + " </a>");
//                    graphHtml.append("</Td>");
//                    graphHtml.append("</Tr>");
//                }
//                graphHtml.append("</Table>");
//                graphHtml.append(" </div>");
//                graphHtml.append("</tr>");
//                graphHtml.append("</Table>");
//                graphHtml.append("<td align=\"center\" onclick=\"showGraphProperties('" + dashletId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + dispSequence + "','" + dispType + "','" + detail.getDashletName() + "','" + contextPath + "','" + fromDesigner + "')\"><a class=\"ui-icon ui-icon-pencil\" title=\"Properties\"></a></td>");
//                graphHtml.append("<td align=\"left\" id=\"DbrdTable-"+dashletId+"\" valign=\"top\"><a class=\"ui-icon ui-icon-calculator\" href='javascript:void(0)' onclick=\"KpiGraphToTable("+dashletId+","+dashBoardId+",'" + refReportId + "',"+graphId + ",'" + kpiMasterId + "','" + dispSequence + "','" + dispType + "','"+ detail.getDashletName() +"','table','"+fromDesigner+"','"+flag+"')\" style='text-decoration:none' class=\"calcTitle\" title=\"Switch to Table\">Switch to Table</a>");
//                graphHtml.append("</td>");
//
//
//                graphHtml.append("</td></tr></table>");
//                graphHtml.append("</div>");
//      }
// else
//      {
            //Creating the dashlet header
            // graphHtml.append("<div id=\"Dashlets-"+dashletId+"\"class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\" style=\"width:100%; height:100%\">");
            if (detail.getDisplayType().equalsIgnoreCase("KpiWithGraph")) {
                graphHtml.append("<div id=\"Dashlets-" + dashletId + "-graph\" class=\"\" style=\"width:100%; style=\"width:100%; height:" + (height - 30) + "px;\">");
            } else {
                graphHtml.append("<div id=\"Dashlets-" + dashletId + "\"  style=\"width:100%; height:" + (height - 30) + "px;\">");
            }

            graphHtml.append("<div id=\"DashletHeader-" + dashletId + "\" class=\"portlet-header1 navtitle portletHeader ui-corner-all\"  align=\"center\">");

            Object[] Obj = new Object[2];
            Obj[0] = refReportId;
            Obj[1] = graphId;
            myGraphType = graphDetails.getGraphTypeName();

            String DupGraphName = "";
            String DupGraphId = "";
            String DupGraphClassName = "";

            ArrayList alist = new ArrayList();
            alist.add(dashletId);
            alist.add(dashBoardId);
            alist.add(refReportId);
            alist.add(graphId);
            alist.add(kpiMasterId);
            alist.add(dispSequence);
            alist.add(dispType);
            alist.add(dashletName);

            if (myGraphType != null && !myGraphType.equalsIgnoreCase("")) {
                DupGraphName = myGraphType;
            } else {
                DupGraphName = graphDetails.getGraphTypeName();

            }
            List<QueryDetail> queryDetails = graphDetails.getQueryDetails();
            String elmntIdString = "";
            for (int j = 0; j < queryDetails.size(); j++) {
                elmntIdString = elmntIdString + "," + queryDetails.get(j).getElementId();
            }
            /*
             * if (DupGraphName.equalsIgnoreCase("Bar") ||
             * DupGraphName.equalsIgnoreCase("bar3d") ||
             * DupGraphName.equalsIgnoreCase("line") ||
             * DupGraphName.equalsIgnoreCase("line3d") ||
             * DupGraphName.equalsIgnoreCase("stacked") ||
             * DupGraphName.equalsIgnoreCase("stackedarea") ||
             * DupGraphName.equalsIgnoreCase("stacked3d") ||
             * DupGraphName.equalsIgnoreCase("column") ||
             * DupGraphName.equalsIgnoreCase("column3d") ||
             * DupGraphName.equalsIgnoreCase("waterfall") ||
             * DupGraphName.equalsIgnoreCase("Area") ||
             * DupGraphName.equalsIgnoreCase("HorizontalStacked") ||
             * DupGraphName.equalsIgnoreCase("HorizontalStacked3d")) {
             * DupGraphClassName = "1"; } else if
             * (DupGraphName.equalsIgnoreCase("pie") ||
             * DupGraphName.equalsIgnoreCase("pie3d") ||
             * DupGraphName.equalsIgnoreCase("ring") ||
             * DupGraphName.equalsIgnoreCase("ColumnPie") ||
             * DupGraphName.equalsIgnoreCase("ColumnPie3D")) { DupGraphClassName
             * = "2"; } else if (DupGraphName.equalsIgnoreCase("Dual Axis") ||
             * DupGraphName.equalsIgnoreCase("OverlaidBar") ||
             * DupGraphName.equalsIgnoreCase("OverlaidArea")||
             * DupGraphName.equalsIgnoreCase("Pareto")||
             * DupGraphName.equalsIgnoreCase("Spider")) { DupGraphClassName =
             * "6"; } else if (DupGraphName.equalsIgnoreCase("Dial")) {
             * DupGraphClassName = "3"; } else if
             * (DupGraphName.equalsIgnoreCase("Meter") ||
             * DupGraphName.equalsIgnoreCase("Thermometer")) { DupGraphClassName
             * = "4"; } else if (DupGraphName.equalsIgnoreCase("Bubble")) {
             * DupGraphClassName = "8"; } else if
             * (DupGraphName.equalsIgnoreCase("TimeSeries")) { DupGraphClassName
             * = "9"; } else if (DupGraphName.equalsIgnoreCase("HorizCone")) {
             * DupGraphClassName = "10"; } else if
             * (DupGraphName.equalsIgnoreCase("Pyramid")) { DupGraphClassName =
             * "11"; } else if (DupGraphName.equalsIgnoreCase("Cone")) {
             * DupGraphClassName = "7"; } else if
             * (DupGraphName.equalsIgnoreCase("PieRing")) { DupGraphClassName =
             * "2"; } else {
        }
             */

            graphHtml.append("<Table width=\"100%\">");
            graphHtml.append("<tr>");
//
//        if ("Graph".equalsIgnoreCase(dispType))
//            graphHtml.append("<td align=\"left\" title=\"").append(dashletName).append(" \"><strong><a href=\"javascript:parent.submiturls1('reportViewer.do?reportBy=viewReport&REPORTID=")
//                            .append(refReportId + "')\">"+ newDashletName + "</a></strong></td>");
//        else
            graphHtml.append("<td align=\"left\" title=\"").append(dashletName).append(" \"><strong>").append("<a href=\"javascript:GraphRename('" + newDashletName + "','" + dashBoardId + "','" + graphId + "','" + dashletId + "','" + refReportId + "','" + kpiMasterId + "','" + dispSequence + "','" + dispType + "','" + fromDesigner + "')\">").append(newDashletName).append("</a> </strong></td>");

            // graphHtml.append("<td align=\"left\" title=\"").append(dashletName).append(" \"><strong>").append("<a href=\"javascript:GraphRename('"+newDashletName+"','"+dashBoardId+"','"+graphId+"','"+dashletId+"','"+refReportId+"','"+kpiMasterId+"','"+dispSequence+"','"+dispType+"','"+fromDesigner+"')\">").append(newDashletName).append("</a> </strong></td>");
            graphHtml.append("<td align=\"right\">");
            graphHtml.append("<table><tr>");
            if (!fromDesigner) {
                if (!detail.getDisplayType().equalsIgnoreCase("KpiWithGraph")) {
                    graphHtml.append("<td><a class=\"ui-icon ui-icon-gear\" href='javascript:void(0)' onclick=\"drillToReports('" + dashletName + "','" + folderId + "')\"></a></td>");
                    graphHtml.append("<td><a class=\"ui-icon ui-icon-clock\" href='javascript:void(0)' onclick=\"getDbrdTimeDisplay('" + dashBoardId + "')\"></a></td>");
                }
            }

            graphHtml.append("<td align=\"left\"><a class=\"ui-icon ui-icon-cart\" onclick=\"chartType('" + dashletId + "')\" title=\"chartType\"></a>");
            graphHtml.append(" <div id='chartType-" + dashletId + "' class=\"overlap_div\" style='display:none;width:100px;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;'  >");
            graphHtml.append("<table align='center'><tbody><tr><td><input type='radio' name='chartTypeButton-" + graphId + "' value='Jfree' align='left' '" + isJqPlotSelected + "'/><a href='javascript:void(0)'>Server Side</a></td></tr>");
            graphHtml.append("<tr><td><input type='radio' name='chartTypeButton-" + graphId + "' value='JQPlot' align='left' '" + isJqPlotSelected + "'/><a href='javascript:void(0)'>Client Side</a></td></tr>");
            graphHtml.append("</tbody>");
//        graphHtml.append("<tfoot ><tr><td align=\"center\"><input  type=\"button\" name=\"ok\" value=\"ok\" class=\"navtitle-hover\" onclick=\"saveChartType('"+dashletId+"','"+graphId+"')\"/></td></tr></tfoot>");
            graphHtml.append("</table></div>");
            graphHtml.append("</td>");


            graphHtml.append("<td align=\"right\" ><a class=\"ui-icon ui-icon-image\" href='javascript:void(0)' title=\"GraphTypes\" onclick=\"graphTypesDispForDb('" + graphId + "','" + dashletId + "')\" ></a>");
            graphHtml.append("<div class=\"overlap_div\"  style='display:none;width:100px;height:" + height + "px;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;'  id='dispgrptypes" + graphId + "'>");
            graphHtml.append("<Table >");

            Iterator<Integer> iter = GraphTypesHashMapKeySet.iterator();
            while (iter.hasNext()) {
                String typeGraph = GraphTypesHashMap.get(iter.next()).getGraphTypeName();
                if (typeGraph.equalsIgnoreCase(DupGraphName)) {
                    DupGraphId = DupGraphName;
                }
//        }
//        Iterator<Integer> itertr = GraphTypesHashMapKeySet.iterator();
//        while(itertr.hasNext()) {
                graphHtml.append("<Tr>");
//            Array id=GraphTypesHashMap.get(GraphTypesHashMapKeySet).toString();
                graphHtml.append("<Td  id='" + typeGraph + "'>");
                if (detail.getKpiType() != null && detail.getDisplayType().equalsIgnoreCase("KpiWithGraph")) {
                    graphHtml.append("<a href='javascript:void(0)'    onclick=\"changeGraphTypeOnKpi('" + dashletId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + dispSequence + "','" + dispType + "','" + dashletName + "',  '" + typeGraph + "', '" + fromDesigner + "')\"     title='" + typeGraph + "' > " + typeGraph + " </a>");
                } else {
                    graphHtml.append("<a href='javascript:void(0)'    onclick=\"changeDashboardGraphType('" + dashletId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + dispSequence + "','" + dispType + "','" + dashletName + "',  '" + typeGraph + "', '" + fromDesigner + "')\"     title='" + typeGraph + "' > " + typeGraph + " </a>");
                }
                graphHtml.append("</Td>");
                graphHtml.append("</Tr>");
            }

            graphHtml.append("</Table>");
            graphHtml.append(" </div>");
            //belowe code is written by srikanth.p to show the graph types of jqplot
            graphHtml.append("<div class=\"overlap_div\" style='display:none;width:100px;height:" + height + "px;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;'  id='dispgrptypes" + graphId + "-jqplot'>");
            graphHtml.append("<Table >");
            String[] jqgrapharray = {"VerticalBar", "Area", "StackedArea", "Line", "Line(Smooth)", "Line(Dashed)", "StackedBarVertical", "DualAxis(Bar-Line)", "Pie", "Pie-Empty", "Donut-Single", "Donut-Double", "Waterfall", "Overlaid(Bar-Line)", "Funnel"};
            String[] jqgraphIds = {"5500", "5501", "5502", "5503", "5504", "5505", "5506", "5507", "5508", "5509", "5510", "5511", "5512", "5513", "5514"};
            for (int i = 0; i < jqgrapharray.length; i++) {
                graphHtml.append("<Tr>");
                graphHtml.append("<Td  id='" + jqgraphIds[i] + "'>");
                graphHtml.append("<a href='javascript:void(0)'    onclick=\"buildDBJqplot('" + jqgrapharray[i] + "','" + jqgraphIds[i] + "','" + dashBoardId + "','" + dashletId + "','" + height + "','" + dispType + "')\"     title='" + jqgrapharray[i] + "' > " + jqgrapharray[i] + " </a>");
                graphHtml.append("</Td>");
                graphHtml.append("</Tr>");
            }
            graphHtml.append("</Table>");
            graphHtml.append(" </div>");
            graphHtml.append("</td>");
            //ended by srikanth.p of jqplot graphtypes


            graphHtml.append("<td align=\"right\" onclick=\"showGraphProperties('" + dashletId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + dispSequence + "','" + dispType + "','" + detail.getDashletName() + "','" + contextPath + "','" + fromDesigner + "')\"><a class=\"ui-icon ui-icon-pencil\" title=\"Properties\"></a></td>");
            if (!detail.getDisplayType().equalsIgnoreCase("KpiWithGraph")) {
                graphHtml.append("<td align=\"right\"><a style='text-decoration:none' class=\"calcTitle\" onclick=\"zoomer('zoom" + graphId + "','" + detail.getDashletName() + "')\" href=\"javascript:void(0)\" ><span  title=\"ZOOM\"  class=\"ui-icon ui-icon-zoomin\"></span></a> </td>");
            }
            graphHtml.append("<td align=\"left\" id=\"DbrdTable-" + dashletId + "\" valign=\"top\"><a class=\"ui-icon ui-icon-calculator\" href='javascript:void(0)' onclick=\"dbrdTable(" + dashletId + "," + dashBoardId + ",'" + refReportId + "'," + graphId + ",'" + kpiMasterId + "','" + dispSequence + "','" + dispType + "','" + detail.getDashletName() + "','table','" + fromDesigner + "','" + flag + "')\" style='text-decoration:none' class=\"calcTitle\" title=\"Switch to Table\">Switch to Table</a>");
            graphHtml.append("</td>");
            if (!detail.getDisplayType().equalsIgnoreCase("KpiWithGraph")) {
                graphHtml.append("<td align=\"right\"><a style='text-decoration:none' class=\"ui-icon ui-icon-video\" onclick=\"OLAPGraph('" + dashBoardId + "','" + dashletId + "','" + dashletName + "')\" href=\"javascript:void(0)\" title=\"OLAP Graph\"></a> </td>");
            }
            if (fromDesigner) {
                graphHtml.append("<td> <img style=\"width:20px;\" src='images/clear-icon.png' onclick=\"clearDashlet('" + dashId + "','" + numOfDashlets + "','" + rowNum + "','" + colNum + "','" + rowSpan + "','" + colSpan + "')\" /></td>");
                graphHtml.append("<td align=\"right\"><a class=\"ui-icon ui-icon-trash\" onclick=\"closeOldPortlet('Dashlets-" + dashletId + "'," + dashletId + ")\" href=\"javascript:void(0)\" /></td>");
            }
//        if("true".equals(editDbrd)){
//            graphHtml.append("<td> <img style=\"width:20px;\" src='images/clear-icon.png' onclick=\"clearDashlet('"+dashId+"','"+numOfDashlets+"','"+rowNum+"','"+colNum+"','"+rowSpan+"','"+colSpan+"')\" /></td>");
//            graphHtml.append("<td align=\"right\"><a class=\"ui-icon ui-icon-trash\" onclick=\"closeOldPortlet('Dashlets-" + dashletId + "',"+dashletId+")\" href=\"javascript:void(0)\" /></td>");
//        }
            graphHtml.append("</tr>");
            graphHtml.append("</Table>");

            graphHtml.append("</td></tr></table>");
            graphHtml.append("</div>");
//            }
        }
        //Creating the dashlet Graph
        if (detail.isJqplotGraph()) {
            graphHtml.append("<div  id='zoom" + graphId + "'  style=\"width:100%;height:250px;\"  align=\"center\">");
        } else {
            graphHtml.append("<div  id='zoom" + graphId + "'  style=\"width:100%;height:" + height + "px;overflow-y:auto;overflow-x:auto\"  align=\"center\">");
        }
        //creates jqPlotGraph
        if (detail.isJqplotGraph()) {
            ProgenJqplotGraphBD jqPlotBuilder = new ProgenJqplotGraphBD();
            graphHtml.append("<div id='chart-" + dashletId + "' style=\"width:100%;height:" + (height - 30) + "px;\" align=\"left\" possition=\"\"><br>");
            graphHtml.append("<script >");
            graphHtml.append(JQPlotGraphBuilder(container, dashBoardId, dashletId));
            graphHtml.append("</script>");
            graphHtml.append("</div>");
        } else {
            PbReturnObject pbretObj = new PbReturnObject();
            PbGraphDisplay GraphDisplay = new PbGraphDisplay();

            if (!isFxCharts()) {
                ArrayList kpiDetails = new ArrayList();
                ArrayList grpDetails = new ArrayList();
                kpiDetails.add(graphId);
                kpiDetails.add(refReportId);
                kpiDetails.add(dispSequence);

                String ViewByType = "";
                String viewById = "";
                try {
                    String query = "select * from PRG_AR_REPORT_VIEW_BY_MASTER where REPORT_ID=" + refReportId;
                    PbReturnObject graphDetailReturnObj = db.execSelectSQL(query);
                    if (graphDetailReturnObj.getRowCount() > 0) {
                        ViewByType = graphDetailReturnObj.getFieldValueString(0, "DEFAULT_VALUE");
                        viewById = graphDetailReturnObj.getFieldValueString(0, "VIEW_BY_ID");
                    }
                } catch (Exception e) {
                }

                if (graphDetails.isTimeSeries()) {
                    pbretObj = container.getTimeSeriesRetObj();
                } else {
                    pbretObj = (PbReturnObject) container.getRetObj();
                }

                if (pbretObj != null) {
                    String[] newStr = null;

                    if (graphDetails.isTimeSeries()) {
                        newStr = new String[1];
                        newStr[0] = "TIME";
                    } else {
                        newStr = new String[collect.reportRowViewbyValues.size()];

                        for (int k = 0; k < collect.reportRowViewbyValues.size(); k++) {
                            if (String.valueOf(collect.reportRowViewbyValues.get(k)).equalsIgnoreCase("TIME")) {
                                if (ViewByType.equalsIgnoreCase("TIME")) {
                                    newStr[k] = "TIME";
                                } else {
                                    newStr[k] = String.valueOf(collect.reportRowViewbyValues.get(k));
                                }
                            } else {
                                if (ViewByType.equalsIgnoreCase("TIME")) {
                                    newStr[k] = "TIME";
                                } else {
                                    newStr[k] = "A_" + String.valueOf(collect.reportRowViewbyValues.get(k));
                                }

                            }
                        }
                    }

                    if (dispType.equalsIgnoreCase("Graph")) {
//                    String drillUrlStr = collect.drillUrl;
//                    drillUrlStr = drillUrlStr.replace("<VIEWBY_ID>", viewById);
                        GraphDisplay.setJscal("reportViewer.do?reportBy=viewReport&REPORTID=" + (String) kpiDetails.get(1) + collect.drillUrl);
                    } else {
                        GraphDisplay.setJscal(null);
                    }

                    GraphDisplay.setCurrentDispRecordsRetObjWithGT(pbretObj);
                    GraphDisplay.setCurrentDispRetObjRecords(pbretObj);
                    GraphDisplay.setAllDispRecordsRetObj(pbretObj);
                    GraphDisplay.setViewByElementIds(newStr);
                    GraphDisplay.setViewByColNames(newStr);
                    GraphDisplay.setCtxPath(request.getContextPath());

                    GraphDisplay.setSession(request.getSession(false));
                    GraphDisplay.setResponse(response);
                    GraphDisplay.setOut(out);
                    GraphDisplay.setReportId((String) kpiDetails.get(1));
                    GraphDisplay.setCollect(collect);
                    GraphDisplay.setMyGraphType(myGraphType);
                    grpDetails = GraphDisplay.getDashboardGraphHeadersNew(dashBoardId, (String) kpiDetails.get(0), detail);

                    ProgenChartDisplay[] pcharts = null;
                    if (grpDetails != null && !grpDetails.isEmpty()) {
                        pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
                        if (pcharts != null && pcharts.length != 0) {
                            buildGraphs(pcharts[0], graphHtml);
                        }
                    }
                }
            } else {
                graphHtml = buildFXGraphs(collect, graphHtml, dashletId, myGraphType);
            }
        }
        graphHtml.append(" </div>");

        graphHtml.append(" </div>");
        return graphHtml.toString();
    }

    public StringBuilder buildGraphs(ProgenChartDisplay pchart, StringBuilder sbuffer) {
        sbuffer.append(pchart.chartDisplay);
        return sbuffer;
    }

    public StringBuilder buildFXGraphs(pbDashboardCollection collect, StringBuilder sbuffer, String dashletId, String graphType) {
        DashletDetail detail = collect.getDashletDetail(dashletId);

        String dashBoardId = collect.reportId;
        String refReportId = detail.getRefReportId();
        String graphId = detail.getGraphId();
        String kpiMasterId = detail.getKpiMasterId();
        String dispSequence = String.valueOf(detail.getDisplaySequence());
        String dispType = detail.getDisplayType();
        String dashletName = detail.getDashletName();

        sbuffer.append("<iframe frameborder='0' style=\"width:500px;height:300px\" id=iFrame-'" + dashletId).append("' name=iFrame-'" + dashletId + "' src='" + request.getContextPath()).append("/PbDashboardGraphDisp.jsp?dashletId=" + dashletId + "&dashBoardId=" + dashBoardId).append("&refReportId=" + refReportId + "&kpiMasterId=" + kpiMasterId).append("&dispSequence=" + dispSequence + "&dispType=" + dispType).append("&myGraphType=" + graphType + "&dashletName=" + graphType).append("&graphId=" + graphId + "' ></iframe>");
        return sbuffer;
    }

    public boolean isFxCharts() {
        return fxCharts;
    }

    public void setFxCharts(boolean fxCharts) {
        this.fxCharts = fxCharts;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public int getGraphWidth(int rowSpan, int colSpan, int totalCols) {
        int graphWidth = 310;
        if (totalCols == 1) {
            graphWidth = 1000;
        } else if (totalCols == 2) {
            graphWidth = 500;
        } else if (totalCols == 3) {
            graphWidth = 330;
        } else if (totalCols == 4) {
            graphWidth = 250;
        }
        return graphWidth;
//        if (totalCols == 1){
//            if (rowSpan == 1){
//                return 13;  //  MediumWith2ColSpan
//            }
//            else if (rowSpan > 1){
//                return 13;  // To be changed
//            }
//        }
//        else if(totalCols == 2){
//            if (rowSpan == 1 && colSpan == 1){
//                return 1;  //  Medium
//            }
//            else if (rowSpan == 1 && colSpan == 2){
//                return 13;  //MediumWith2ColSpan
//            }
//            else if (rowSpan == 2 && colSpan == 1){
//                return 14; //MediumWith2RowSpan
//            }
//            else if (rowSpan == 2 && colSpan == 2){
//                return 13;  // To be changed
//            }
//        }
//        else if(totalCols == 3)
//        {
//            if (rowSpan == 1 && colSpan == 1){
//                return 2;  //  Small
//            }
//            else if (rowSpan == 1 && colSpan == 2){
//                return 10;  //smallWith2ColSpan
//            }
//            else if (rowSpan == 1 && colSpan == 3){
//                return 4; //smallWith3ColSpan
//            }
//            else if (rowSpan == 2 && colSpan == 1){
//                return 11;  // smallWith2RowSpan
//            }
//            else if (rowSpan == 2 && colSpan == 2){
//                return 12;  // smallWith2RSpanCSpan
//            }
//            else if (rowSpan == 2 && colSpan == 3){
//                return 13;  // To be changed
//            }
//        }
//        else if(totalCols == 4){
//            if (rowSpan == 1 && colSpan == 1){
//                return 6;  //  smallest
//            }
//            else if (rowSpan == 1 && colSpan == 2){
//                return 8;  //smallestWith2ColSpan
//            }
//            else if (rowSpan == 1 && colSpan == 3){
//                return 13; // To be Changed
//            }
//            else if (rowSpan == 1 && colSpan == 4){
//                return 13;  // MediumWith2ColSpan
//            }
//            else if (rowSpan == 2 && colSpan == 2){
//                return 1;  // Medium
//            }
//            else if (rowSpan == 2 && colSpan == 4){
//                return 13;  //  To be changed
//            }
//        }
//
//        return 1; //BY default return Medium Graph Size

        /*
         * pbDashboardCollection collect = (pbDashboardCollection)
         * container.getReportCollect(); List<DashletDetail> dashletDetails =
         * collect.dashletDetails; if (dashletDetails != null &&
         * !dashletDetails.isEmpty()) { ArrayListMultimap<Integer, Integer>
         * rowinfo = ArrayListMultimap.create(); for (int i = 0; i <
         * dashletDetails.size(); i++) { DashletDetail detail =
         * dashletDetails.get(i); rowinfo.put(detail.getRow(), detail.getCol());
         * } for (int j = 0; j < rowinfo.keySet().size(); j++) { List<Integer>
         * dashlets = rowinfo.get(j); int numOfDashlets = dashlets.size(); for
         * (int p = 0; p < dashletDetails.size(); p++) { DashletDetail detail =
         * dashletDetails.get(p); GraphReport graphDetails = (GraphReport)
         * detail.getReportDetails();
         * if(detail.getDashBoardDetailId().equals(dashletId)) { int row =
         * detail.getRow(); int col = detail.getCol(); int rowSpan =
         * detail.getRowSpan(); int colSpan = detail.getColSpan();
         * if(colSpan==1) { graphDetails.setGraphSize(); } } } }
         *
         * }
         */
    }

    public int getTotalColumns(pbDashboardCollection collect, String dashletId) {
        List<DashletDetail> dashletDetails = collect.dashletDetails;
        DashletDetail detail = collect.getDashletDetail(dashletId);

        int row = detail.getRow();
        int noOfCols = 0;

        for (DashletDetail temp : dashletDetails) {
            if (temp.getRow() == row) {
                noOfCols += temp.getColSpan();
            }
        }
        return noOfCols;
    }

    public int getGraphNameMaxCharacters(pbDashboardCollection collect, String dashletId) {
        int maxChars = DashboardConstants.NEW_GRAPHNAME_LENGTH_4;
        int totalCols = getTotalColumns(collect, dashletId);
        DashletDetail detail = collect.getDashletDetail(dashletId);

        int colSpan = detail.getColSpan();
        if (totalCols == 1) {
            maxChars = DashboardConstants.NEW_GRAPHNAME_LENGTH_1;
        } else if (totalCols == 2) {
            if (colSpan == 1) {
                maxChars = DashboardConstants.NEW_GRAPHNAME_LENGTH_2;
            } else {
                maxChars = DashboardConstants.NEW_GRAPHNAME_LENGTH_1;
            }
        } else if (totalCols == 3) {
            if (colSpan == 1) {
                maxChars = DashboardConstants.NEW_GRAPHNAME_LENGTH_3;
            } else if (colSpan == 2) {
                maxChars = DashboardConstants.NEW_GRAPHNAME_LENGTH_2;
            } else {
                maxChars = DashboardConstants.NEW_GRAPHNAME_LENGTH_1;
            }
        } else if (totalCols == 4) {
            if (colSpan == 1) {
                maxChars = DashboardConstants.NEW_GRAPHNAME_LENGTH_4;
            } else if (colSpan == 2) {
                maxChars = DashboardConstants.NEW_GRAPHNAME_LENGTH_2;
            } else if (colSpan == 3) {
                maxChars = DashboardConstants.NEW_GRAPHNAME_LENGTH_2;
            } else {
                maxChars = DashboardConstants.NEW_GRAPHNAME_LENGTH_1;
            }
        }
        return maxChars;
    }

    /**
     * added by srikanth.p to build jqplot graphs for dashboard
     *
     * @param container
     * @param dashbordId
     * @param dashletId
     * @return
     */
    public String JQPlotGraphBuilder(Container container, String dashbordId, String dashletId) {
        StringBuilder jqplot = new StringBuilder();
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail dashlet = (DashletDetail) collect.getDashletDetail(dashletId);
        GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
        String graphDisplayRows = graphDetails.getDisplayRows();

        List<QueryDetail> queryDetails = (List<QueryDetail>) graphDetails.getQueryDetails();
        PbReturnObject retObj = null;
        ProgenJQPlotGraph jqplotGraph = new ProgenJQPlotGraph();
        if (graphDetails.isTimeSeries()) {
            retObj = container.getTimeSeriesRetObj();
        } else {
            retObj = (PbReturnObject) container.getRetObj();
        }
        String graphType = dashlet.getJqPlotGraphType();
        ArrayList viewByList = new ArrayList();
        String rowViewBy = "A_" + collect.reportRowViewbyValues.get(0);
        if (graphDetails.isTimeSeries()) {
            rowViewBy = "TIME";
        }

        if (retObj != null) {

            int dispRows = 10;
            ArrayList<Integer> viewSequence = retObj.getViewSequence();
            if (!graphDetails.isTimeSeries() && queryDetails != null && !queryDetails.isEmpty()) {
                String sortElementId = "A_" + queryDetails.get(0).getElementId();
                char[] sortTypes = new char[1];
                ArrayList<String> sortElementsList = new ArrayList<String>();
                char[] sortDataTypes = new char[1];
                sortElementsList.add(sortElementId);
                sortTypes[0] = '1';
                sortDataTypes[0] = queryDetails.get(0).getColumnType().charAt(0);
//                retObj.resetViewSequence();
                viewSequence = retObj.sortDataSet(sortElementsList, sortTypes, sortDataTypes);
                retObj.setViewSequence(viewSequence);
            }
            if (graphDisplayRows != null && !graphDisplayRows.equalsIgnoreCase("null") && !graphDisplayRows.isEmpty() && !graphDisplayRows.equalsIgnoreCase("")) {
                if (graphDisplayRows.equalsIgnoreCase("All") || (Integer.parseInt(graphDisplayRows) > viewSequence.size())) {
                    dispRows = viewSequence.size();
                } else {
                    dispRows = Integer.parseInt(graphDisplayRows);
                }
            }
//            if(retObj.rowCount >10){
//            retObj.rowCount=10;
//        }
//            if(viewSequence.size() <10){
//                dispRows=viewSequence.size();
//                    }
            //String viewbyName = "";
            for (int i = 0; i < dispRows; i++) {
                //  viewbyName = "'" + retObj.getFieldValueString(i, rowViewBy) + "'";
                viewByList.add(retObj.getFieldValueString(viewSequence.get(i), rowViewBy));
            }
            HashMap<String, ArrayList> barDataset = new HashMap<String, ArrayList>();
            ArrayList elementNameList = new ArrayList();
            for (int i = 0; i < queryDetails.size(); i++) {
                QueryDetail qd = queryDetails.get(i);
                String elementId = "A_" + qd.getElementId();
                elementNameList.add(qd.getDisplayName());
                ArrayList ValuesList = new ArrayList();
                for (int j = 0; j < dispRows; j++) {
                    ValuesList.add(retObj.getFieldValueString(viewSequence.get(j), elementId));
                }
                barDataset.put(elementId, ValuesList);

            }

            jqplotGraph.setDashboardId(dashbordId);
            jqplotGraph.setChartId(dashletId);
            jqplotGraph.setDatasetMap(barDataset);
            jqplotGraph.setGraphType(graphType);
            jqplotGraph.setRowViewByList(viewByList);
            jqplotGraph.setElementNameList(elementNameList);
            jqplotGraph.setContainer(container);
            jqplotGraph.setDashletId(dashletId);
            jqplot.append(jqplotGraph.createJqPlotGraph());
            String url = "reportViewer.do?reportBy=viewReport&REPORTID=" + dashlet.getRefReportId() + collect.drillUrl;

            jqplot.append(" $('#chart-" + dashletId + "').bind('jqplotDataClick', function (ev, seriesIndex, pointIndex, data) {drilltoRepForJQPLot('" + url + "','" + viewByList + "',pointIndex,data);});");
            //String function="drilltoRepForJQPLot('"+url+"','"+viewByList+"','+pointIndex+','+data+')";

        }
        return jqplot.toString();
    }

    /**
     * @return the dashboardid
     */
    public String getDashboardid() {
        return dashboardid;
    }

    /**
     * @param dashboardid the dashboardid to set
     */
    public void setDashboardid(String dashboardid) {
        this.dashboardid = dashboardid;
    }

    /**
     * @return the graphType
     */
    public String getGraphType() {
        return graphType;
    }

    /**
     * @param graphType the graphType to set
     */
    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }
}
