package com.progen.dashboardView.bd;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.progen.charts.JqplotGraphProperty;
import com.progen.charts.PbFxXML;
import com.progen.charts.ProgenChartDisplay;
import com.progen.dashboard.DashboardConstants;
import com.progen.dashboard.DashboardTableColorGroupHelper;
import com.progen.dashboardView.db.DashboardViewerDAO;
import com.progen.db.ProgenDataSet;
import com.progen.db.SelectDbSpecificFunc;
import com.progen.graph.GraphBuilder;
import com.progen.report.charts.PbGraphDisplay;
import com.progen.report.data.DataFacade;
import com.progen.report.display.DisplayParameters;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.drill.DrillMaps;
import com.progen.report.entities.*;
import com.progen.report.kpi.DashletPropertiesHelper;
import com.progen.report.kpi.KPISingleGroupHelper;
import com.progen.report.map.MapBD;
import com.progen.report.map.MapConstants;
import com.progen.report.*;
import com.progen.report.query.PbReportQuery;
import com.progen.report.query.QueryExecutor;
import com.progen.report.query.TrendDataSet;
import com.progen.report.query.TrendDataSetHelper;
import com.progen.reportdesigner.action.GroupMeassureParams;
import com.progen.reportdesigner.db.DashboardTemplateDAO;
import com.progen.reportview.db.PbReportViewerDAO;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

/**
 * @filename PbReportViewerBD
 *
 * @author santhosh.kumar@progenbusiness.com @date Oct 5, 2009, 10:52:02 AM
 */
public class PbDashboardViewerBD extends PbDb {

    public static Logger logger = Logger.getLogger(PbDashboardViewerBD.class);
    private String dashboardId = null;
    private String userId = null;
    private HttpServletRequest servletRequest = null;
    private HttpServletResponse servletResponse = null;
    private HttpSession httpSession = null;
    private Container container = null;
    //variables used in buildFXGraphs added by santhosh.kumar@progenbusiness.com on 19-12-2009
    private String dispType = null;
    private String graphId = null;
    private String refReportId = null;
    private String dispSequence = null;
    private String dashletId = null;
    private boolean isFxCharts = false;
    private SelectDbSpecificFunc checknull = new SelectDbSpecificFunc();
    private String editDashboard = "";
    private KPI kpiDetail;
    private String sortType = "top";
    private int countVal = 0;
    //end of variables

    public Container displayDashboardBD(String userId) {

        PbReportRequestParameter reportReqParams = null;
        pbDashboardCollection collect = null;
        DisplayParameters disp = null;
        String ParamSectionDisplay = null;
        String[] repParamsKeys = null;
        ArrayList Parameters = null;

        try {
            reportReqParams = new PbReportRequestParameter(this.servletRequest);
            disp = new DisplayParameters();
            if (this.container.getReportCollect() != null) {
                collect = (pbDashboardCollection) container.getReportCollect();

            } else {
                collect = new pbDashboardCollection();
            }

            collect.reportId = this.dashboardId;//here reportId is DashBiooard Id
            collect.reportIncomingParameters = reportReqParams.requestParamValues;

            collect.getParamMetaData(null);
            collect.setCheckDashboardType("true");

            repParamsKeys = (String[]) (collect.reportParameters.keySet()).toArray(new String[0]);
            if (collect.reportParameters != null) {
                ArrayList a1 = new ArrayList();
                for (int i = 0; i < repParamsKeys.length; i++) {
                    ArrayList arl = (ArrayList) collect.reportParameters.get(repParamsKeys[i]);
                    if (Parameters == null) {
                        Parameters = new ArrayList();
                    } else {
                        Parameters.add(repParamsKeys[i]);
                    }
                    a1.add(arl.get(1));
                }
                this.container.setReportParameterNames(a1);
            }

            if (getEditDashboard().equals("true")) {
                disp.setEditDashboard(true);
            }

            disp.isReport = false;
            disp.completeUrl1 = collect.completeUrl;
            ParamSectionDisplay = disp.displayAllParams(collect.reportParameters, collect.timeDetailsMap, collect.reportViewByMain, collect.reportViewByOrder, repParamsKeys[0], collect.resetPath, null, null, null, null);
            buildDashBoard(collect, userId);
            this.container.setRepReqParamsHashMap(reportReqParams.requestParamValues);
            //collect.setReportReqParams(reportReqParams.requestParamValues);
            this.container.setReportCollect(collect);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return this.container;
    }
//added by Dinanath for generating container for scheduler

    public Container displayDashboardBDforScheduler(String userId) {

        PbReportRequestParameter reportReqParams = null;
        pbDashboardCollection collect = null;
        DisplayParameters disp = null;
        String ParamSectionDisplay = null;
        String[] repParamsKeys = null;
        ArrayList Parameters = null;

        try {
            reportReqParams = new PbReportRequestParameter(this.servletRequest);
            disp = new DisplayParameters();
            if (this.container.getReportCollect() != null) {
                collect = (pbDashboardCollection) container.getReportCollect();

            } else {
                collect = new pbDashboardCollection();
            }

            collect.reportId = this.dashboardId;//here reportId is DashBiooard Id
            collect.reportIncomingParameters = reportReqParams.requestParamValues;

            collect.getParamMetaData(null);
            collect.setCheckDashboardType("true");

            repParamsKeys = (String[]) (collect.reportParameters.keySet()).toArray(new String[0]);
            if (collect.reportParameters != null) {
                ArrayList a1 = new ArrayList();
                for (int i = 0; i < repParamsKeys.length; i++) {
                    ArrayList arl = (ArrayList) collect.reportParameters.get(repParamsKeys[i]);
                    if (Parameters == null) {
                        Parameters = new ArrayList();
                    } else {
                        Parameters.add(repParamsKeys[i]);
                    }
                    a1.add(arl.get(1));
                }
                this.container.setReportParameterNames(a1);
            }

            if (getEditDashboard().equals("true")) {
                disp.setEditDashboard(true);
            }

            disp.isReport = false;
            disp.completeUrl1 = collect.completeUrl;
            ParamSectionDisplay = disp.displayAllParams(collect.reportParameters, collect.timeDetailsMap, collect.reportViewByMain, collect.reportViewByOrder, repParamsKeys[0], collect.resetPath, null, null, null, null);
            buildDashBoard(collect, userId);
            this.container.setRepReqParamsHashMap(reportReqParams.requestParamValues);
            //collect.setReportReqParams(reportReqParams.requestParamValues);
            this.container.setReportCollect(collect);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return this.container;
    }

    public void displayDashboardBD(String action, String userId) {

        PbReportRequestParameter reportReqParams = null;
        pbDashboardCollection collect = null;
        DisplayParameters disp = null;
        String ParamSectionDisplay = null;
        String[] repParamsKeys = null;
        ArrayList Parameters = null;
        pbDashboardCollection dbrdcollect = null;

        try {
            reportReqParams = new PbReportRequestParameter(this.servletRequest);
            disp = new DisplayParameters();
            if (this.container.getReportCollect() != null) {
                collect = (pbDashboardCollection) container.getReportCollect();

            } else {
                collect = new pbDashboardCollection();
            }
            if (this.servletRequest != null) {
                reportReqParams.setParametersHashMap();
            }
            collect.reportId = this.dashboardId;//here reportId is DashBiooard Id
            if (this.servletRequest != null) {
                collect.ctxPath = this.servletRequest.getContextPath();
            }
            collect.reportIncomingParameters = reportReqParams.requestParamValues;
            if (this.servletRequest != null) {
                collect.setServletRequest(this.servletRequest);
            }
            collect.dateFormat = container.getDateFormat();
            collect.getParamMetaData(action);
            if (this.container.getDrillfromRepId() != null && !this.container.getDrillfromRepId().equalsIgnoreCase("0")) {
                Container dbrdcontainer = Container.getContainerFromSession(this.getServletRequest(), this.container.getDrillfromRepId());
                dbrdcollect = (pbDashboardCollection) dbrdcontainer.getReportCollect();
                collect.timeDetailsArray = dbrdcollect.timeDetailsArray;
                collect.timeDetailsMap = dbrdcollect.timeDetailsMap;
            }
            collect.setCheckDashboardType("true");

            repParamsKeys = (String[]) (collect.reportParameters.keySet()).toArray(new String[0]);
            if (collect.reportParameters != null) {
                ArrayList a1 = new ArrayList();
                for (int i = 0; i < repParamsKeys.length; i++) {
                    ArrayList arl = (ArrayList) collect.reportParameters.get(repParamsKeys[i]);
                    if (Parameters == null) {
                        Parameters = new ArrayList();
                    } else {
                        Parameters.add(repParamsKeys[i]);
                    }
                    a1.add(arl.get(1));
                }
                this.container.setReportParameterNames(a1);
            }
            if (this.servletRequest != null) {
                setEditDashboard(String.valueOf(this.servletRequest.getAttribute("edit")));
            }
            if (getEditDashboard().equals("true")) {
                disp.setEditDashboard(true);
            }

            disp.isReport = false;
            disp.completeUrl1 = collect.completeUrl;
            if (this.servletRequest != null) {
                if (Parameters != null) {
                    this.servletRequest.setAttribute("Parameters", Parameters);
                } else {
                    this.servletRequest.setAttribute("Parameters", new ArrayList());
                }
            }
            if (!collect.reportParameters.isEmpty()) {
                ParamSectionDisplay = disp.newDisplayAllParams(collect.reportParameters, collect.timeDetailsMap, collect.reportViewByMain, collect.reportViewByOrder, repParamsKeys[0], collect.resetPath, collect.ctxPath, null, null, null, container.getReportId(), false, collect, null, this.container);
            } else {
                ParamSectionDisplay = disp.displayTimeParamsForDashboard(collect.timeDetailsMap, collect.resetPath);
            }
            if (this.servletRequest != null) {
                this.servletRequest.setAttribute("ParamSectionDisplay", ParamSectionDisplay);
                this.servletRequest.setAttribute("buildDashBoard", buildDashBoard(collect, userId));
                this.servletRequest.setAttribute("DisplayParameters", disp);
                this.servletRequest.setAttribute("currentURL", collect.completeUrl);
            } else {
                buildDashBoard(collect, userId);
            }
            this.container.setRepReqParamsHashMap(reportReqParams.requestParamValues);
            //collect.setReportReqParams(reportReqParams.requestParamValues);
            this.container.setReportCollect(collect);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }

    /*
     * Function to build dynamic dashboard layout. Building dashboard using four
     * key variables row number,column number, row span and column span. Find
     * the number of dashlets in every row using ArrayListMultimap. Loop through
     * the number of dashlets in the dashboard. Check for the dashlets in the
     * particular row is spanning across the next row or not and also check for
     * the column span of the particular dashlet. If the table containes only
     * one row then the flag buildTable is set to 1 else flag is set to 2, then
     * the table has to be built. according to the flag buildTable. If the
     * buildTable is 1 then a simple table is built with one row and number of
     * columns is decided accordingly. If the buildTable is 2 then the number of
     * rows are decided after checking the all the dashlets which are spanning
     * across the next row, then the row from which the table should be started
     * and the row till which the table should be built is decided. then the
     * table is built according to the row number column number and rowSpan and
     * colSpan.
     */
    public StringBuilder buildDashBoard(pbDashboardCollection collect, String userId) throws Exception {

        int rowIndex = -1;

//        HttpSession hs = getServletRequest().getSession(false);

//        String dashletsColumnstatus = String.valueOf(hs.getAttribute("dashletsColumnstatus"));

        StringBuilder dashBuilder = new StringBuilder("");

        String dashBoardId = getDashBoardId();
        Object[] ObjArray = new Object[1];
        ObjArray[0] = dashBoardId;

        if (!(collect.isInitialized())) {
            createDashletDetail(collect);
            collect.initialized = true;
        }

        PbReturnObject retObj = getDashboardData(container, collect, userId);
        container.setRetObj(retObj);
        PbReturnObject kpiRetObj = getDashboardKPIData(container, collect, userId);
        container.setKpiRetObj(kpiRetObj);
        PbReturnObject timeSeriesRetObj = getTimeSeriesData(collect, userId);
        container.setTimeSeriesRetObj(timeSeriesRetObj);

        if (collect.getDrillMap() == null) {
            DrillMaps drillMapHelper = new DrillMaps();
            collect.setDrillMap(drillMapHelper.getDrillForReport(container.getReportId(), userId, collect.reportRowViewbyValues.get(0),
                    collect.reportParametersValues));
        }
        List<DashletDetail> dashletDetails = collect.dashletDetails;
        boolean flag = false;
//        int buildTable = 0;
        String kpiType = "";
        ArrayList<Integer> al = new ArrayList();

        if (dashletDetails != null && !dashletDetails.isEmpty()) {
            dashBuilder.append("<div id='DashletsColumn1_1' class=\"column1\" align=\"left\" style=\"width:100%\">");
            ArrayListMultimap<Integer, Integer> rowinfo = ArrayListMultimap.create();
            for (int i = 0; i < dashletDetails.size(); i++) {
                DashletDetail detail = dashletDetails.get(i);
                rowinfo.put(detail.getRow(), detail.getCol());
            }

            for (int count = 0; count < rowinfo.keySet().size(); count++) {
                int buildTable = 0;
//                if(rowIndex<rowinfo.keySet().size()-1)
                rowIndex++;
                int colNum = 0;
                int rowNum = 0;
                int rowSpanNum = 0;
                int colSpanNum = 0;
                List<Integer> dashlets = rowinfo.get(rowIndex);
//                while (dashlets.isEmpty()) {
//                    rowIndex++;
//                    dashlets = rowinfo.get(rowIndex);
//                }
                al.add(rowIndex);
                int numOfDashlets = dashlets.size();
                int numOfCols = numOfDashlets;
                for (int p = 0; p < dashletDetails.size(); p++) {
                    DashletDetail detail = dashletDetails.get(p);
                    int row = detail.getRow();
                    int col = detail.getCol();
                    int rowSpan = detail.getRowSpan();
                    int colSpan = detail.getColSpan();
                    if (row == rowIndex) {

                        if (rowSpan == 1) {
                            colNum = col;
                            rowNum = row;
                            rowSpanNum = rowSpan;
                            colSpanNum = colSpan;
                            if (colSpan > 1) {
                                numOfCols = numOfCols + (colSpan - 1);
                            }
                            buildTable = 1;
                            flag = false;
                        } else if (rowSpan > 1) {
                            colNum = col;
                            rowNum = row;
                            rowSpanNum = rowSpan;
                            colSpanNum = colSpan;
                            if (colSpan > 1) {
                                numOfCols = numOfCols + (colSpan - 1);
                            }

                            flag = true;
                        }
                        if (flag == true) {
                            buildTable = 2;
                            break;
                        }
                    }
                }

                if (buildTable == 1) {
                    dashBuilder.append("<table width=100% cellspacing=10>");
                    for (int i = 0; i < dashletDetails.size(); i++) {
                        DashletDetail detail = dashletDetails.get(i);
                        int row = detail.getRow();
                        int col = detail.getCol();
                        int rowSpan = detail.getRowSpan();
                        int colSpan = detail.getColSpan();
                        String dashletId = detail.getDashBoardDetailId();

                        // % wise width for dashlets
                        double width = 100;
                        if (numOfCols == 1) {
                            width = 100;
                        } else if (numOfCols == 2) {
                            width = 50;
                            width = width * colSpan;
                        } else if (numOfCols == 3) {
                            width = 33;
                            width = width * colSpan;
                        } else if (numOfCols == 4) {
                            width = 25;
                            width = width * colSpan;
                        }

                        if (row == rowNum) {
                            if (col == 0) {
                                dashBuilder.append("<tr width=100%>");
                                dashBuilder.append("<td id=\"" + dashletId + "\" width=" + width + "% colspan=" + colSpan + " rowspan=" + rowSpan + " valign=\"top\">");
                                buildDashlets(dashBuilder, detail, i, numOfCols, container);
                                dashBuilder.append("</td>");
                            } else {
                                dashBuilder.append("<td id=\"" + dashletId + "\" width=" + width + "% colspan=" + colSpan + " rowspan=" + rowSpan + " valign=\"top\">");
                                buildDashlets(dashBuilder, detail, i, numOfCols, container);
                                dashBuilder.append("</td>");
                            }

                        }
                    }
                    dashBuilder.append("</tr></table>");
                    al = new ArrayList();
                    if (this.servletRequest != null) {
                        this.servletRequest.setAttribute("rowIndex", rowIndex);
                    }
                } else if (buildTable == 2) {
                    int startTableRowNum = rowNum;
                    int maxRows = rowNum + rowSpanNum;
                    int endTableRowNum = maxRows;
                    int newRowNum = rowNum;
                    int newRowSpanNum = rowSpanNum;
                    int newColNum = colNum;
                    int newColSpanNum = colSpanNum;
                    int newMaxRows = maxRows;
                    for (int k = rowNum; k < maxRows; k++) {
                        for (int n = 0; n < dashletDetails.size(); n++) //loop-^295
                        {
                            DashletDetail detail = dashletDetails.get(n);
                            int row = detail.getRow();
                            int col = detail.getCol();
                            int rowSpan = detail.getRowSpan();
                            int colSpan = detail.getColSpan();

                            if (row >= rowNum && row < maxRows) {
                                if (row != rowNum || col != colNum) {
                                    if (rowSpan > 1) {
                                        if (colSpan > 1) {
                                            numOfCols = numOfCols + (colSpan - 1);
                                        }
                                        newRowNum = row;
                                        newColNum = col;
                                        newRowSpanNum = rowSpan;
                                        newColSpanNum = colSpan;
                                        newMaxRows = newRowNum + newRowSpanNum;
                                        //                                    flag=true;
                                    } else {
                                        if (colSpan > 1) {
                                            numOfCols = numOfCols + (colSpan - 1);
                                        }
                                    }

                                    if (newMaxRows > maxRows) {
                                        maxRows = newMaxRows;
                                        rowNum = newRowNum;
                                        colNum = newColNum;
                                        rowSpanNum = newRowSpanNum;
                                        colSpanNum = newColSpanNum;
                                        endTableRowNum = newMaxRows;
                                        flag = true;
                                    } else {
                                        flag = false;
                                    }
                                    if (flag == true) {
                                        break;
                                    }
                                }
                            }
                        }
                        if (flag == false) {
                            k++;
                        }
                    }

                    dashBuilder.append("<table width=100% cellspacing=10>");
                    int prevRow = 0;
                    for (int i = 0; i < dashletDetails.size(); i++) {
                        DashletDetail detail = dashletDetails.get(i);
                        int row = detail.getRow();
                        int col = detail.getCol();
                        int rowSpan = detail.getRowSpan();
                        int colSpan = detail.getColSpan();
                        String dashletId = detail.getDashBoardDetailId();

                        // % wise width for dashlets
                        double width = 100;
                        if (numOfCols == 1) {
                            width = 100;
                        } else if (numOfCols == 2) {
                            width = 50;
                            width = width * colSpan;
                        } else if (numOfCols == 3) {
                            width = 33;
                            width = width * colSpan;
                        } else if (numOfCols == 4) {
                            width = 25;
                            width = width * colSpan;
                        }
                        if (row >= startTableRowNum && row < endTableRowNum) {
                            if (row == startTableRowNum && col == 0) {
                                prevRow = row;
                                dashBuilder.append("<tr width=100%>");
                                dashBuilder.append("<td id=\"" + dashletId + "\" width=" + width + "% colspan=" + colSpan + " rowspan=" + rowSpan + " valign=\"top\">");
                                buildDashlets(dashBuilder, detail, i, numOfCols, container);
                                dashBuilder.append("</td>");
                            } else if (row == prevRow) {
                                dashBuilder.append("<td id=\"" + dashletId + "\" width=" + width + "% colspan=" + colSpan + " rowspan=" + rowSpan + " valign=\"top\">");
                                buildDashlets(dashBuilder, detail, i, numOfCols, container);
                                dashBuilder.append("</td>");
                            } else {
                                dashBuilder.append("</tr>");
                                prevRow = row;
                                dashBuilder.append("<tr width=100%>");
                                if (col == 0) {
                                    dashBuilder.append("<td id=\"" + dashletId + "\" width=" + width + "% colspan=" + colSpan + " rowspan=" + rowSpan + " valign=\"top\">");
                                    buildDashlets(dashBuilder, detail, i, numOfCols, container);
                                    dashBuilder.append("</td>");
                                    ;
                                } else {
                                    dashBuilder.append("<td id=\"" + dashletId + "\" width=" + width + "% colspan=" + colSpan + " rowspan=" + rowSpan + " valign=\"top\">");
                                    buildDashlets(dashBuilder, detail, i, numOfCols, container);
                                    dashBuilder.append("</td>");
                                }
                            }
                        }
                    }
                    dashBuilder.append("</tr></table>");
                    rowIndex = +(endTableRowNum - 1);
                    this.servletRequest.setAttribute("rowIndex", rowIndex);
                }
            }
        }
        return dashBuilder;
    }

    public StringBuilder buildDashlets(StringBuilder sbuffer, DashletDetail detail, int i, int noOfColumn, Container container) {

        HttpSession hs = null;
        if (getServletRequest() != null) {
            hs = getServletRequest().getSession(false);
        }
        StringBuilder mapSB = new StringBuilder("");
        String mapMenustatus = "none";//String.valueOf(hs.getAttribute("mapMenustatus"));
        String dashBoardId = getDashBoardId();
        String ctxPath = "";
        if (this.servletRequest != null) {
            this.servletRequest.getContextPath();
        }
        String reportId = container.getReportCollect().reportId;

        if (detail.getDisplayType().equalsIgnoreCase("MAP")) {

            MapBD mapbd = new MapBD();
            String TopBtmEnable = "none";
            String GeoViewSelection = "none";
            mapbd.getGeographyDimensionIds(container);

            if (!container.getGeographyDimensionIds().isEmpty()) {
                boolean enabletopbottomSelection = mapbd.isTopBottomEnabled(container);

                if (enabletopbottomSelection) {
                    TopBtmEnable = "block";
                }
                if (hs != null) {
                    hs.setAttribute("TopBtmEnable", TopBtmEnable);
                }
                boolean enableViewSelection = mapbd.isGeoDimSelectionEnabled(container);
                if (enabletopbottomSelection) {
                    if (container.getMapMainMeasure().size() > 1) {
                        String mainMeasure = container.getMapMainMeasure().get(0);
                        String mainMeasureLabel = container.getMapMainMeasureLabel().get(0);
                        container.resetMapMainMeasures();
                        container.resetMapMainMeasureLabels();
                        container.setMapMainMeasure(mainMeasure);
                        container.setMapMainMeasureLabel(mainMeasureLabel);
                    }
                }

                if (enableViewSelection) {
                    GeoViewSelection = "block";
                }
                if (hs != null) {
                    hs.setAttribute("GeoViewSelection", GeoViewSelection);
                }
            }

            String display = "";
            String geoDimensionSelectionStatus = "";
            String allowColorGroup = "none";
            if (TopBtmEnable.equalsIgnoreCase("none")) {
                display = "none";
                allowColorGroup = "";
            }
            if (GeoViewSelection.equalsIgnoreCase("none")) {
                geoDimensionSelectionStatus = "none";
            }

            ArrayList<String> geoDimensionIds = (ArrayList) container.getGeographyDimensionIds();
            ArrayList<String> geoDimensionLabels = new ArrayList<String>();
            for (int j = 0; j < geoDimensionIds.size(); j++) {
                geoDimensionLabels.add(container.getReportCollect().getParameterDispName(geoDimensionIds.get(j)));
            }


            StringBuilder optionhtml = new StringBuilder();
            for (int k = 0; k < geoDimensionIds.size(); k++) {
                optionhtml.append("<option value=\"").append(geoDimensionIds.get(k)).append("\">").append(geoDimensionLabels.get(k)).append("</option>");

            }

            mapSB.append("<div id=\"innerDivKpiId\"  class=\"portlet-header1 navtitle portletHeader ui-corner-all\">");
            mapSB.append("<table width=\"100%\"><tr>");
            mapSB.append("<td width=\"74%\" onclick=\"displayMapOrNot()\"><strong>" + detail.getDashletName() + "</strong></td>");

            mapSB.append("<td width=\"10%\" align=\"right\"><a class=\"ui-icon ui-icon-trash\" href=\"javascript:void(0)\"  onclick=\"closeOldPortlet('Dashlets-" + dashletId + "'," + dashletId + ")\" ></a></td>");
            mapSB.append("</tr></table>");
            mapSB.append("</div>");

            mapSB.append("<div id=\"mapMenu\"").append("style=\"display:").append(mapMenustatus).append("\">");
            mapSB.append("<table width=\"100%\"><tr>");
            mapSB.append("<td width=\"20%\" align=\"left\"><a href=\"javascript:void(0)\"  onclick=\"addMapMeasures('" + ctxPath + "','','" + reportId + "','Dashlets-"
                    + detail.getDashBoardDetailId() + "')\" >Edit Map</a></td>");


            mapSB.append("<td width=\"10%\" align=\"left\" style= \"display:").append(display).append("\">");
            mapSB.append("<select class=\"myTextbox5\" name=\"ViewSelect\" id=\"ViewSelect\">");
            mapSB.append("<option value=\"" + MapConstants.DISPLAY_OVERALL_VALUES + "\" >Over All</option>");
            mapSB.append("<option value=\"" + MapConstants.DISPLAY_LOCATION_WISE_VALUES + "\" >Location wise</option></select>").append("</td>");
            mapSB.append("<td width=\"10%\" align=\"left\" style= \"display:").append(display).append("\">");
            mapSB.append("<select class=\"myTextbox5\" name=\"sortValuesForMap\" id=\"sortValuesForMap\"><option value=\"0\" >Top3</option>");
            mapSB.append("<option value=\"1\" >Top5</option>");
            mapSB.append("<option value=\"2\">Bottom3</option>");
            mapSB.append("<option value=\"3\">Bottom5</option></select>").append("</td>");
            //dashBuffer.append("<td width=\"12%\"align=\"left\" class=\"wordStyle\" style= \"display:").append(geoDimensionSelectionStatus).append("\">Geography Dimension</td>");
            mapSB.append("<td width=\"10%\"align=\"left\" style= \"display:").append(geoDimensionSelectionStatus).append("\">");
            mapSB.append("<select class=\"myTextbox5\" name=\"GeoViewForMap\" id=\"GeoViewForMap\">").append(optionhtml.toString()).append("</select>").append("</td>");
            mapSB.append("<td  align=\"left\" style= \"display:").append(display).append("\">");
            mapSB.append("<input type=\"button\" align=\"left\" class=\"navtitle-hover\" style=\"width:auto\" value=\"Go\" onclick=\"addSortValueToMap()\" id=\"MapView\">").append("</td>");
            mapSB.append("<td align=\"left\" style=\"display:").append(allowColorGroup).append("\">");
            mapSB.append("<a href=\"javascript:void(0);\" style=\"text-decoration: none;\" name=\"setMapColorGrouping\" name=\"setMapColorGrouping\" onclick=\"setColourGroupForMap(\'").append(dashBoardId).append("\')\">Color Grouping").append("</a>").append("</td>");

            mapSB.append("</tr></table>");
            mapSB.append("</div>");
            mapSB.append("<br/>");



        }
        if (hs != null) {
            HashMap GraphTypesHashMap = (HashMap) hs.getAttribute("GraphTypesHashMap");
            HashMap GraphClassesHashMap = (HashMap) hs.getAttribute("GraphClassesHashMap");

            String[] GraphTypesHashMapKeySet = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);
            String[] GraphClassesHashMapKeySet = (String[]) GraphClassesHashMap.keySet().toArray(new String[0]);
        }
        String dashBoardDetailId = detail.getDashBoardDetailId();
        String refReportId = detail.getRefReportId();
        String graphId = detail.getGraphId();
        String kpiMasterId = detail.getKpiMasterId();
        int displaySequence = detail.getDisplaySequence();
        String displayType = detail.getDisplayType();

        String kpiType = detail.getKpiType();
        String graphName = detail.getDashletName();
        int graphNameLength = 0;
        if (graphName != null) {
            graphNameLength = graphName.length();
        }
        String dashletName = graphName;
        String dispDashletName = "";

        int rowSpan = detail.getRowSpan();
        int colSpan = detail.getColSpan();

        double height = 350;
        height = height * rowSpan;
        DashboardTemplateDAO templateDAO = new DashboardTemplateDAO();
        String jqgraphtype = null;
        try {
            jqgraphtype = templateDAO.getJqPlotKpiGraphType(dashBoardDetailId);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        if (displayType.equalsIgnoreCase("MAP")) {
//            sbuffer.append("<div \"class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\">");
//            sbuffer.append(mapSB);
            sbuffer.append("<div  id=\"Dashlets-" + dashBoardDetailId + "\" class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\" style=\"width:100%;height:" + height + "px\">");
        } else {
            sbuffer.append("<div  id=\"Dashlets-" + dashBoardDetailId + "\"  style=\"width:100%;height:100%;overflow-x: hidden;overflow-y: hidden;\" >");
        }

//        sbuffer.append("<div class=\"portlet-header portletHeader ui-corner-all\">");
//        sbuffer.append("<center><b style='font-weight:bold'>" + dispDashletName + "</b></center>");
//        sbuffer.append(" </div>");

        String editDbrd = this.editDashboard;
        if (displayType.equalsIgnoreCase("Graph")) {
            if (detail.getJqplotgrapprop() != null) {
                JqplotGraphProperty prop = detail.getJqplotgrapprop();
                sbuffer.append("<script type='text/javascript'>");
                sbuffer.append("buildDBJqplot('" + prop.getGraphTypename() + "','" + prop.getGraphTypeId() + "','" + dashBoardId + "','" + dashBoardDetailId + "','" + height + "','" + displayType + "')");
                sbuffer.append(" </script>");
            } else {
                sbuffer.append("<script type='text/javascript'>");
                sbuffer.append("displayGraph('" + dashBoardDetailId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + displaySequence + "','" + displayType + "','" + dashletName + "','','" + editDbrd + "');");
                sbuffer.append(" </script>");
            }
        } else if (displayType.equalsIgnoreCase("KPI")) {
            sbuffer.append("<script type='text/javascript'>");
            sbuffer.append("displayKPI('" + dashBoardDetailId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + displaySequence + "','" + displayType + "','" + dashletName + "',false,'" + editDbrd + "');");
            sbuffer.append(" </script>");
        } else if (displayType.equalsIgnoreCase("SCARD")) {
            sbuffer.append("<script type='text/javascript'>");
            sbuffer.append("displayScoreCard('" + dashBoardDetailId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + displaySequence + "','" + displayType + "','" + dashletName + "');");
            sbuffer.append(" </script>");
        } else if (displayType.equalsIgnoreCase("MAP")) {
            sbuffer.append("<script type='text/javascript'>");
            sbuffer.append("displayMap('" + dashBoardDetailId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + displaySequence + "','" + displayType + "','" + dashletName + "');");
            sbuffer.append(" </script>");
        } else if (displayType.equalsIgnoreCase("newGraph")) {
            if (detail.getJqplotgrapprop() != null) {
                JqplotGraphProperty prop = detail.getJqplotgrapprop();
                sbuffer.append("<script type='text/javascript'>");
                sbuffer.append("buildDBJqplot('" + prop.getGraphTypename() + "','" + prop.getGraphTypeId() + "','" + dashBoardId + "','" + dashBoardDetailId + "','" + height + "','" + displayType + "')");
                sbuffer.append(" </script>");
            } else {
                sbuffer.append("<script type='text/javascript'>");
                sbuffer.append("displayDashboardGraph('" + dashBoardDetailId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + displaySequence + "','" + displayType + "','" + dashletName + "','" + editDbrd + "');");
                sbuffer.append(" </script>");
            }
        } else if (displayType.equalsIgnoreCase("table")) {
            sbuffer.append("<script type='text/javascript'>");
            sbuffer.append("displayDashboardGraph('" + dashBoardDetailId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + displaySequence + "','" + displayType + "','" + dashletName + "','" + editDbrd + "');");
            sbuffer.append(" </script>");
        } else if (displayType.equalsIgnoreCase("KPIGraph")) {
            // 
            if (jqgraphtype != null && jqgraphtype.equalsIgnoreCase("metter")) {
                sbuffer.append("<script type='text/javascript'>");
                sbuffer.append("jqplotMeterchart('Dashlets-" + dashBoardDetailId + "','" + dashBoardDetailId + "','" + dashBoardId + "');");
                sbuffer.append(" </script>");
            } else {
                sbuffer.append("<script type='text/javascript'>");
                sbuffer.append("displayKPIDashGraph('").append(dashBoardDetailId).append("','").append(dashBoardId).append("','").append(refReportId).append("','").append(graphId).append("','").append(kpiMasterId).append("','").append(displaySequence).append("','").append(displayType).append("','").append(dashletName).append("','").append(false).append("','").append(editDbrd).append("');");
                sbuffer.append(" </script>");
            }
        } else if (displayType.equalsIgnoreCase("textKpi")) {
            sbuffer.append("<script type='text/javascript'>");
            sbuffer.append("displayDashboardGraph('" + dashBoardDetailId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + displaySequence + "','" + displayType + "','" + dashletName + "','" + editDbrd + "');");
            sbuffer.append(" </script>");
        } else if (displayType.equalsIgnoreCase("groupMeassure")) {
            sbuffer.append("<script type='text/javascript'>");
            sbuffer.append("displayDashboardGraph('" + dashBoardDetailId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + displaySequence + "','" + displayType + "','" + dashletName + "','" + editDbrd + "');");
            sbuffer.append(" </script>");
        } else if (displayType.equalsIgnoreCase("KpiWithGraph")) {
            sbuffer.append("<script type='text/javascript'>");
            sbuffer.append("displayKpiWithGraph('" + dashBoardDetailId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + displaySequence + "','" + displayType + "','" + dashletName + "','" + editDbrd + "');");
            sbuffer.append(" </script>");
        }

        sbuffer.append(" </div>");
        return sbuffer;
    }

    public String getDashBoardId() {
        return dashboardId;
    }

    public void setDashBoardId(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }

    public void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public HttpServletResponse getServletResponse() {
        return servletResponse;
    }

    public void setServletResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    //used in buildFXGraphs only
    public String getDispType() {
        return dispType;
    }

    public void setDispType(String dispType) {
        this.dispType = dispType;
    }

    public String getGraphId() {
        return graphId;
    }

    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }

    public String getRefReportId() {
        return refReportId;
    }

    public void setRefReportId(String refReportId) {
        this.refReportId = refReportId;
    }

    public String getDispSequence() {
        return dispSequence;
    }

    public void setDispSequence(String dispSequence) {
        this.dispSequence = dispSequence;
    }

    public String getDashletId() {
        return dashletId;
    }

    public void setDashletId(String dashletId) {
        this.dashletId = dashletId;
    }

    public ArrayList buildFXGraphs() throws Exception {
        StringBuffer sbuffer = new StringBuffer();
        pbDashboardCollection collect = null;
        String finalQuery = null;
        String[] qryColNames = null;
        String[] grpColNames = null;
        String[] kpidbColNames = null;
        HashMap GraphReport = new LinkedHashMap();
        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject pbretObj = new PbReturnObject();

        ArrayList xmlDetails = new ArrayList();
        HashMap singleRecord = new HashMap();


        if (container.getReportCollect() != null) {
            collect = (pbDashboardCollection) container.getReportCollect();
            if (getDispType().equalsIgnoreCase("KPIGraph")) {
                PbGraphDisplay GraphDisplay = new PbGraphDisplay();

                PbReturnObject kpipbro = new PbReturnObject();
                String singlekpiQuery = "SELECT KPI_GRP_ID, ELEMENT_ID, DASHBOARD_ID,";
                singlekpiQuery += "NEEDLE, KPINAME, KPIGRAPHTYPE FROM PRG_AR_KPI_GRAPH_DETAILS where DASHBOARD_ID=" + getDashBoardId() + "  and DASHBOARDDET_ID=" + getDashletId();
                kpipbro = execSelectSQL(singlekpiQuery);
                kpidbColNames = kpipbro.getColumnNames();
                String query = "SELECT " + checknull.ifnullthen() + "(AGGREGATION_TYPE,'SUM') AGGREGATION_TYPE FROM PRG_USER_ALL_INFO_DETAILS where element_id=" + kpipbro.getFieldValueInt(0, kpidbColNames[1]);
                PbReturnObject kpiretObj = execSelectSQL(query);

                ArrayList kpielearr = new ArrayList();
                kpielearr.add(kpipbro.getFieldValueInt(0, kpidbColNames[1]));
                ArrayList aggr = new ArrayList();
                aggr.add(kpiretObj.getFieldValueString(0, 0));

                repQuery.setRowViewbyCols(collect.reportRowViewbyValues);
                repQuery.setColViewbyCols(collect.reportColViewbyValues);
                repQuery.setQryColumns(kpielearr);
                repQuery.setColAggration(aggr);
                repQuery.setTimeDetails(collect.timeDetailsArray);

                repQuery.setDefaultMeasure(String.valueOf(kpipbro.getFieldValueInt(0, kpidbColNames[1])));
                repQuery.setDefaultMeasureSumm(String.valueOf(kpiretObj.getFieldValueString(0, 0)));
                repQuery.isKpi = true;
                PbReturnObject kpipbretObj = repQuery.getPbReturnObject(String.valueOf(kpipbro.getFieldValueInt(0, kpidbColNames[1])));
                repQuery.isKpi = false;

                String kpis = kpipbro.getFieldValueString(0, kpidbColNames[8]);


                ProgenChartDisplay pchart = new ProgenChartDisplay(400, 250);
                pchart.setCtxPath(getServletRequest().getContextPath());

                singleRecord.put("startrange", kpipbro.getFieldValueString(0, kpidbColNames[3]));
                singleRecord.put("endrange", kpipbro.getFieldValueString(0, kpidbColNames[4]));
                singleRecord.put("firstbreak", kpipbro.getFieldValueString(0, kpidbColNames[5]));
                singleRecord.put("secondbreak", kpipbro.getFieldValueString(0, kpidbColNames[6]));
                singleRecord.put("needlevalue", kpipbretObj.getFieldValueInt(0, 1));

                GraphDisplay.setCurrentDispRetObjRecords(kpipbro);
                GraphDisplay.graphTypeName = kpipbro.getFieldValueString(0, kpidbColNames[9]).trim();
                GraphDisplay.setGraphId(kpipbro.getFieldValueString(0, kpidbColNames[0]));

                GraphDisplay.graphHeight = "280";
                GraphDisplay.graphWidth = "480";

                xmlDetails.add(GraphDisplay.graphHeight);
                xmlDetails.add(GraphDisplay.graphWidth);
                xmlDetails.add(GraphDisplay.graphTypeName);
                GraphDisplay.getFxDataSet(singleRecord);
                xmlDetails.add(buildKPIXML(singleRecord, getRefReportId(), GraphDisplay));

            } else if (getDispType().equalsIgnoreCase("Graph") || getDispType().equalsIgnoreCase("newGraph")) {
                PbGraphDisplay GraphDisplay = new PbGraphDisplay();
                PbReturnObject retObjQry = null;
                PbReturnObject retObjGrp = null;
                ArrayList kpiDetails = new ArrayList();
                ArrayList qryColIds = new ArrayList();
                ArrayList qryAggrs = new ArrayList();

                kpiDetails.add(getGraphId());
                kpiDetails.add(getDashBoardId());
                kpiDetails.add(getDispSequence());

                GraphReport.put(kpiDetails.get(0).toString(), kpiDetails);

                String sqlstr1 = "";
                sqlstr1 = "select element_id, col_disp_name,aggregation_type from prg_ar_query_detail where report_id=" + getRefReportId() + " order by col_seq";
                finalQuery = sqlstr1;
                retObjQry = execSelectSQL(finalQuery);
                qryColIds = new ArrayList();
                qryAggrs = new ArrayList();
                if (retObjQry != null) {
                    qryColNames = retObjQry.getColumnNames();
                    for (int j = 0; j < retObjQry.getRowCount(); j++) {
                        qryColIds.add(retObjQry.getFieldValueString(j, qryColNames[0]));
                        qryAggrs.add(retObjQry.getFieldValueString(j, qryColNames[2]));
                    }

                    //added by k on 06-may-10
                    String ViewByType = "";
                    PbDb pbdb1 = new PbDb();
                    try {
                        String query = "select DEFAULT_VALUE from PRG_AR_REPORT_VIEW_BY_MASTER where REPORT_ID=" + refReportId;
                        PbReturnObject graphDetailReturnObj = pbdb1.execSelectSQL(query);
                        ViewByType = graphDetailReturnObj.getFieldValueString(0, "DEFAULT_VALUE");
                    } catch (SQLException e) {
                    }

                    if (ViewByType.equalsIgnoreCase("TIME")) {
                        collect.reportRowViewbyValues.set(0, "Time");
                    }

                    //added by k over

                    String sqlstr2 = "";
                    sqlstr2 = "select element_id, col_name from prg_ar_graph_details where graph_id= " + getGraphId();
                    finalQuery = sqlstr2;

                    retObjGrp = execSelectSQL(finalQuery);
                    grpColNames = retObjGrp.getColumnNames();
                    ArrayList grpElementIds = new ArrayList();

                    for (int j = 0; j < collect.reportRowViewbyValues.size(); j++) {
                        if (String.valueOf(collect.reportRowViewbyValues.get(j)).equalsIgnoreCase("Time")) {
                            if (!(grpElementIds.contains(String.valueOf(collect.reportRowViewbyValues.get(j))))) {
                                grpElementIds.add(String.valueOf(collect.reportRowViewbyValues.get(j)));
                            }
                        } else {
                            if (!(grpElementIds.contains("A_" + String.valueOf(collect.reportRowViewbyValues.get(j))))) {
                                grpElementIds.add("A_" + String.valueOf(collect.reportRowViewbyValues.get(j)));
                            }
                        }
                    }
                    for (int k = 0; k < retObjGrp.getRowCount(); k++) {
                        if (!(grpElementIds.contains("A_" + retObjGrp.getFieldValueInt(k, grpColNames[0])))) {
                            grpElementIds.add("A_" + retObjGrp.getFieldValueInt(k, grpColNames[0]));
                        }
                    }

                    repQuery.setRowViewbyCols(collect.reportRowViewbyValues);
                    repQuery.setColViewbyCols(collect.reportColViewbyValues);
                    repQuery.setParamValue(collect.reportParametersValues);//Added by k
                    repQuery.setQryColumns(qryColIds);
                    repQuery.setColAggration(qryAggrs);
                    repQuery.setTimeDetails(collect.timeDetailsArray);

                    pbretObj = repQuery.getPbReturnObject(String.valueOf(qryColIds.get(0)));
                    if (pbretObj != null) {
                        container.setDisplayedSet(pbretObj);
                        container.setOriginalColumns(grpElementIds);
                        ArrayList alist = container.getDisplayedSet();

                        String[] newStr = new String[collect.reportRowViewbyValues.size()];



                        for (int k = 0; k < collect.reportRowViewbyValues.size(); k++) {
                            if (String.valueOf(collect.reportRowViewbyValues.get(k)).equalsIgnoreCase("TIME")) {
                                if (ViewByType.equalsIgnoreCase("TIME")) {
                                    newStr[k] = "Time";
                                } else {
                                    newStr[k] = String.valueOf(collect.reportRowViewbyValues.get(k));
                                }

                            } else {
                                if (ViewByType.equalsIgnoreCase("TIME")) {
                                    newStr[k] = "Time";
                                } else {
                                    newStr[k] = "A_" + String.valueOf(collect.reportRowViewbyValues.get(k));
                                }

                            }
                        }
                        GraphDisplay.setViewByElementIds(newStr);
                        GraphDisplay.setViewByColNames(newStr);
                        GraphDisplay.setCtxPath(getServletRequest().getContextPath());
                        GraphDisplay.setCurrentDispRetObjRecords(pbretObj);
                        GraphDisplay.setCurrentDispRecordsRetObjWithGT(pbretObj);
                        GraphDisplay.setAllDispRecordsRetObj(pbretObj);

                        GraphDisplay.setJscal(null);
                        GraphDisplay.setSession(getServletRequest().getSession(false));
                        GraphDisplay.setResponse(getServletResponse());
                        GraphDisplay.setOut(getServletResponse().getWriter());
                        GraphDisplay.setReportId((String) kpiDetails.get(1));
                        GraphDisplay.setCollect(collect);
                        GraphDisplay.setMyGraphType(collect.getMyGraphType());
                        ////.println("collect.getMyGraphType()="+collect.getMyGraphType());


                        GraphDisplay.getDashboardGraphHeadersNewFx((String) kpiDetails.get(1), (String) kpiDetails.get(0));

                        GraphDisplay.getFxDataSet(singleRecord);


                        GraphDisplay.graphHeight = "280";
                        GraphDisplay.graphWidth = "480";

                        xmlDetails.add(GraphDisplay.graphHeight);
                        xmlDetails.add(GraphDisplay.graphWidth);
                        xmlDetails.add(GraphDisplay.graphTypeName);

                        GraphDisplay.getFxDataSet(singleRecord);


                        HashMap GraphDetails = new HashMap();

                        GraphDetails.put("graphId", GraphDisplay.graphId);
                        GraphDetails.put("graphName", GraphDisplay.graphName);
                        GraphDetails.put("graphWidth", GraphDisplay.graphWidth);
                        GraphDetails.put("graphHeight", GraphDisplay.graphHeight);
                        GraphDetails.put("graphClassName", GraphDisplay.graphClassName);
                        GraphDetails.put("graphTypeName", GraphDisplay.graphTypeName);
                        GraphDetails.put("SwapColumn", String.valueOf(GraphDisplay.SwapColumn));

                        GraphDetails.put("graphLegend", GraphDisplay.grplegend);
                        GraphDetails.put("graphLegendLoc", GraphDisplay.grplegendloc);
                        GraphDetails.put("graphshowX", GraphDisplay.grpshox);
                        GraphDetails.put("graphshowY", GraphDisplay.grpshoy);
                        GraphDetails.put("graphLYaxislabel", GraphDisplay.grpLYaxislabel);
                        GraphDetails.put("graphRYaxislabel", GraphDisplay.grpryaxislabel);
                        GraphDetails.put("graphDrill", GraphDisplay.grpdrill);
                        GraphDetails.put("graphBcolor", GraphDisplay.grpbcolor);
                        GraphDetails.put("graphFcolor", GraphDisplay.grpfcolor);
                        GraphDetails.put("graphData", GraphDisplay.grpdata);


                        //storing graph details info in HashMap
                        GraphDetails.put("barChartColumnNames", GraphDisplay.getBarChartColumnNames());
                        GraphDetails.put("viewByElementIds", GraphDisplay.viewByElementIds);
                        GraphDetails.put("barChartColumnTitles", GraphDisplay.getBarChartColumnTitles());
                        GraphDetails.put("pieChartColumns", GraphDisplay.getPieChartColumns());
                        GraphDetails.put("axis", GraphDisplay.axis);

                        //storing graph details info (Dual Axis ) in HashMap
                        GraphDetails.put("barChartColumnNames1", GraphDisplay.getBarChartColumnNames1());
                        GraphDetails.put("barChartColumnTitles1", GraphDisplay.getBarChartColumnTitles1());
                        GraphDetails.put("barChartColumnNames2", GraphDisplay.getBarChartColumnNames2());
                        GraphDetails.put("barChartColumnTitles2", GraphDisplay.getBarChartColumnTitles2());

                        GraphDetails.put("nbrFormat", GraphDisplay.nbrFormat);
                        GraphDetails.put("graphSymbol", GraphDisplay.graphSymbol);
                        GraphDetails.put("graphGridLines", GraphDisplay.getGraphGridLines());

                        PbFxXML FxXML = new PbFxXML();
                        FxXML.requestFrom = "DashBoard";
                        FxXML.getFxXML(singleRecord, GraphDetails, getUserId(), getRefReportId(), GraphDisplay.graphId, null);
                        //xmlDetails.add(buildXML(singleRecord, getRefReportId(), GraphDisplay, getDispType()));
                        xmlDetails.add(FxXML.getFxXML(singleRecord, GraphDetails, getUserId(), getRefReportId(), GraphDisplay.graphId, null));
                    }
                }
            }
        }
        return xmlDetails;
    }

    public StringBuffer buildKPIXML(HashMap singleRecord, String refReportId, PbGraphDisplay GraphDisplay) {
        StringBuffer sbuffer = new StringBuffer("");

        sbuffer.append("<Graphs>");
        sbuffer.append("<Graph>");
        sbuffer.append("<user_id>" + getUserId() + "</user_id>");
        sbuffer.append("<report_id>" + refReportId + "</report_id>");
        sbuffer.append("<graph_id>" + GraphDisplay.getGraphId() + "</graph_id>");
        sbuffer.append("<graph_type>" + GraphDisplay.graphTypeName + "</graph_type>");
        sbuffer.append("<x_label></x_label>");
        sbuffer.append("<l_y_label>" + GraphDisplay.getGrplyaxislabel() + "</l_y_label>");
        sbuffer.append("<r_y_label>" + GraphDisplay.getGrpryaxislabel() + "</r_y_label>");

        if (true) {
            sbuffer.append("<graph_width>450</graph_width>");
            sbuffer.append("<graph_height>270</graph_height>");
        } else {
            sbuffer.append("<graph_width>" + GraphDisplay.graphWidth + "</graph_width>");
            sbuffer.append("<graph_height>" + GraphDisplay.graphHeight + "</graph_height>");
        }
        sbuffer.append("<allow_legend>" + GraphDisplay.getGrplegend() + "</allow_legend>");
        sbuffer.append("<allow_tooltip></allow_tooltip>");
        sbuffer.append("<legend_location>" + GraphDisplay.getGrplegendloc() + "</legend_location>");
        sbuffer.append("<font_color>" + GraphDisplay.getGrpfcolor() + "</font_color>");
        sbuffer.append("<back_ground_color>" + GraphDisplay.getGrpbcolor() + "</back_ground_color>");

        sbuffer.append("<start_range>" + singleRecord.get("startrange") + "</start_range>");
        sbuffer.append("<first_break>" + singleRecord.get("firstbreak") + "</first_break>");
        sbuffer.append("<second_break>" + singleRecord.get("secondbreak") + "</second_break>");
        sbuffer.append("<end_range>" + singleRecord.get("endrange") + "</end_range>");
        sbuffer.append("<needle_value>" + singleRecord.get("needlevalue") + "</needle_value>");
        sbuffer.append("</Graph>");
        sbuffer.append("</Graphs>");

        return sbuffer;
    }

    public boolean isIsFxCharts() {
        return isFxCharts;
    }

    public void setIsFxCharts(boolean isFxCharts) {
        this.isFxCharts = isFxCharts;
    }

    public void updateKPICustomColorVals(DashletDetail dashlet, String elementId, String kpiMasterId, String hrOperator, String mrOperator, String lrOperator, String hr1, String hr2, String lr1, String lr2, String mr1, String mr2) {
        KPI kpiDetail = (KPI) dashlet.getReportDetails();

        KPIColorRange colorRange = new KPIColorRange();
        colorRange.setColor("Green");
        colorRange.setOperator(hrOperator);
        colorRange.setRangeStartValue(Double.parseDouble(hr1));
        colorRange.setRangeEndValue(Double.parseDouble(hr2));
        kpiDetail.addKPIColorRange(elementId, colorRange);

        colorRange = new KPIColorRange();
        colorRange.setColor("Yellow");
        colorRange.setOperator(mrOperator);
        colorRange.setRangeStartValue(Double.parseDouble(mr1));
        colorRange.setRangeEndValue(Double.parseDouble(mr2));
        kpiDetail.addKPIColorRange(elementId, colorRange);

        colorRange = new KPIColorRange();
        colorRange.setColor("Red");
        colorRange.setOperator(lrOperator);
        colorRange.setRangeStartValue(Double.parseDouble(lr1));
        colorRange.setRangeEndValue(Double.parseDouble(lr2));
        kpiDetail.addKPIColorRange(elementId, colorRange);

        if (kpiDetail.isPersisted()) {
            try {
                String updateKPIDetsQry = "update prg_ar_kpi_details set KPI_HRANGE_TYPE='" + hrOperator + "',KPI_HRANGE1='" + hr1 + "',KPI_HRANGE2='" + hr2 + "'"
                        + ",KPI_LRANGE_TYPE='" + lrOperator + "',KPI_LRANGE1='" + lr1 + "',KPI_LRANGE2='" + lr2 + "',KPI_MRANGE_TYPE='" + mrOperator + "',KPI_MRANGE1='" + mr1 + "'"
                        + ",KPI_MRANGE2='" + mr2 + "' where element_id='" + elementId + "' and kpi_master_id='" + kpiMasterId + "'";
//                
                execModifySQL(updateKPIDetsQry);
            } catch (Exception exp) {
                logger.error("Exception:", exp);
            }
        }
    }

    public void createDashletDetail(pbDashboardCollection collect) {
        String finalQuery = null;
        String buildDashBoardQuery = "";
        Object[] ObjArray = new Object[1];
        ArrayList<String> hidecolumns = new ArrayList<String>();//added by sruthi for hide columns
        ObjArray[0] = collect.reportId; //DashBoard Id
        List<KPISingleGroupHelper> singleGroupHelpers = null;
        HashMap<String, Double> targetmanuval = new HashMap<String, Double>();
        //changed by sruthifor hidecolumns
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            buildDashBoardQuery = "SELECT"
                    + /*
                     * 0
                     */ " a.DASHBOARD_DETAILS_ID, "
                    + /*
                     * 1
                     */ "a.DASHBOARD_ID,"
                    + /*
                     * 2
                     */ " a.REF_REPORT_ID,"
                    + /*
                     * 3
                     */ " a.GRAPH_ID,"
                    + /*
                     * 4
                     */ " a.KPI_MASTER_ID,"
                    + /*
                     * 5
                     */ " a.DISPLAY_SEQUENCE,"
                    + /*
                     * 6 all types of graph types
                     */ " a.DISPLAY_TYPE,"
                    + /*
                     * 7
                     */ " ISNULL(a.KPI_HEADING , b.graph_name),"
                    + /*
                     * 8
                     */ "c.report_name,"
                    + /*
                     * 9 (types of kpi standard r normal)
                     */ "a.kpi_type, " + " a.ROW_1," + "a.COL_1," + "a.ROW_SPAN," + "a.COL_SPAN," + "a.KPI_NAME ," + "a.KPI_SYMBOL," + "b.TIME_SERIES,a.DASHLET_KPIANDTABLE_PROP,a.KPI_HEADS,a.ROW_VIEW_BY,a.GROUPID,b.JQ_PROPERTIES,a.Hidecolumns,a.NO_DAYS,a.TARGET_MANUAL"
                    + " FROM PRG_AR_DASHBOARD_DETAILS as a left outer join PRG_AR_GRAPH_MASTER b on (a.graph_id = b.graph_id) inner join PRG_AR_REPORT_MASTER c on (a.dashboard_id =c.report_id) WHERE a.dashboard_id=& ORDER BY a.row_1, a.col_1";

        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            buildDashBoardQuery = "SELECT"
                    + /*
                     * 0
                     */ " a.DASHBOARD_DETAILS_ID, "
                    + /*
                     * 1
                     */ "a.DASHBOARD_ID,"
                    + /*
                     * 2
                     */ " a.REF_REPORT_ID,"
                    + /*
                     * 3
                     */ " a.GRAPH_ID,"
                    + /*
                     * 4
                     */ " a.KPI_MASTER_ID,"
                    + /*
                     * 5
                     */ " a.DISPLAY_SEQUENCE,"
                    + /*
                     * 6 all types of graph types
                     */ " a.DISPLAY_TYPE,"
                    + /*
                     * 7
                     */ " IFNULL(a.KPI_HEADING , b.graph_name),"
                    + /*
                     * 8
                     */ "c.report_name,"
                    + /*
                     * 9 (types of kpi standard r normal)
                     */ "a.kpi_type, " + " a.ROW_1," + "a.COL_1," + "a.ROW_SPAN," + "a.COL_SPAN," + "a.KPI_NAME ," + "a.KPI_SYMBOL," + "b.TIME_SERIES,a.DASHLET_KPIANDTABLE_PROP,a.KPI_HEADS,a.ROW_VIEW_BY,a.GROUPID,b.JQ_PROPERTIES,a.Hidecolumns,a.NO_DAYS,a.TARGET_MANUAL"
                    + " FROM PRG_AR_DASHBOARD_DETAILS as a left outer join PRG_AR_GRAPH_MASTER b on (a.graph_id = b.graph_id) inner join PRG_AR_REPORT_MASTER c on (a.dashboard_id =c.report_id) WHERE a.dashboard_id=& ORDER BY a.row_1, a.col_1";

        } else {

            buildDashBoardQuery = "SELECT"
                    + /*
                     * 0
                     */ " a.DASHBOARD_DETAILS_ID, "
                    + /*
                     * 1
                     */ "a.DASHBOARD_ID,"
                    + /*
                     * 2
                     */ " a.REF_REPORT_ID,"
                    + /*
                     * 3
                     */ " a.GRAPH_ID,"
                    + /*
                     * 4
                     */ " a.KPI_MASTER_ID,"
                    + /*
                     * 5
                     */ " a.DISPLAY_SEQUENCE,"
                    + /*
                     * 6 all types of graph types
                     */ " a.DISPLAY_TYPE,"
                    + /*
                     * 7
                     */ " nvl(a.KPI_HEADING , b.graph_name),"
                    + /*
                     * 8
                     */ "c.report_name,"
                    + /*
                     * 9 (types of kpi standard r normal)
                     */ "a.kpi_type," + "a.ROW_1," + "a.COL_1," + "a.ROW_SPAN," + "a.COL_SPAN," + "a.KPI_NAME ," + "a.KPI_SYMBOL," + "b.TIME_SERIES,a.DASHLET_KPIANDTABLE_PROP ,a.KPI_HEADS,a.ROW_VIEW_BY,a.GROUPID,b.JQ_PROPERTIES,a.Hidecolumns,a.NO_DAYS,a.TARGET_MANUAL"
                    + " FROM PRG_AR_DASHBOARD_DETAILS a,PRG_AR_GRAPH_MASTER b,PRG_AR_REPORT_MASTER c where a.dashboard_id=& and a.dashboard_id=c.report_id  and a.graph_id= b.graph_id(+) order by a.row_1, a.col_1";

        }//ended by sruthi
        if (collect.isFromOneView()) {
            if (collect.getDashletId() != null) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    buildDashBoardQuery = "SELECT"
                            + /*
                             * 0
                             */ " a.DASHBOARD_DETAILS_ID, "
                            + /*
                             * 1
                             */ "a.DASHBOARD_ID,"
                            + /*
                             * 2
                             */ " a.REF_REPORT_ID,"
                            + /*
                             * 3
                             */ " a.GRAPH_ID,"
                            + /*
                             * 4
                             */ " a.KPI_MASTER_ID,"
                            + /*
                             * 5
                             */ " a.DISPLAY_SEQUENCE,"
                            + /*
                             * 6 all types of graph types
                             */ " a.DISPLAY_TYPE,"
                            + /*
                             * 7
                             */ " ISNULL(a.KPI_HEADING , b.graph_name),"
                            + /*
                             * 8
                             */ "c.report_name,"
                            + /*
                             * 9 (types of kpi standard r normal)
                             */ "a.kpi_type, " + " a.ROW_1," + "a.COL_1," + "a.ROW_SPAN," + "a.COL_SPAN," + "a.KPI_NAME ," + "a.KPI_SYMBOL," + "b.TIME_SERIES,a.DASHLET_KPIANDTABLE_PROP,a.KPI_HEADS,a.ROW_VIEW_BY,a.GROUPID"
                            + " FROM PRG_AR_DASHBOARD_DETAILS as a left outer join PRG_AR_GRAPH_MASTER b on (a.graph_id = b.graph_id) inner join PRG_AR_REPORT_MASTER c on (a.dashboard_id =c.report_id) WHERE a.dashboard_id=& and a.DASHBOARD_DETAILS_ID=" + collect.getDashletId() + " ORDER BY a.row_1, a.col_1";

                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    buildDashBoardQuery = "SELECT"
                            + /*
                             * 0
                             */ " a.DASHBOARD_DETAILS_ID, "
                            + /*
                             * 1
                             */ "a.DASHBOARD_ID,"
                            + /*
                             * 2
                             */ " a.REF_REPORT_ID,"
                            + /*
                             * 3
                             */ " a.GRAPH_ID,"
                            + /*
                             * 4
                             */ " a.KPI_MASTER_ID,"
                            + /*
                             * 5
                             */ " a.DISPLAY_SEQUENCE,"
                            + /*
                             * 6 all types of graph types
                             */ " a.DISPLAY_TYPE,"
                            + /*
                             * 7
                             */ " IFNULL(a.KPI_HEADING , b.graph_name),"
                            + /*
                             * 8
                             */ "c.report_name,"
                            + /*
                             * 9 (types of kpi standard r normal)
                             */ "a.kpi_type, " + " a.ROW_1," + "a.COL_1," + "a.ROW_SPAN," + "a.COL_SPAN," + "a.KPI_NAME ," + "a.KPI_SYMBOL," + "b.TIME_SERIES,a.DASHLET_KPIANDTABLE_PROP,a.KPI_HEADS,a.ROW_VIEW_BY,a.GROUPID"
                            + " FROM PRG_AR_DASHBOARD_DETAILS as a left outer join PRG_AR_GRAPH_MASTER b on (a.graph_id = b.graph_id) inner join PRG_AR_REPORT_MASTER c on (a.dashboard_id =c.report_id) WHERE a.dashboard_id=& and a.DASHBOARD_DETAILS_ID=" + collect.getDashletId() + " ORDER BY a.row_1, a.col_1";

                } else {

                    buildDashBoardQuery = "SELECT"
                            + /*
                             * 0
                             */ " a.DASHBOARD_DETAILS_ID, "
                            + /*
                             * 1
                             */ "a.DASHBOARD_ID,"
                            + /*
                             * 2
                             */ " a.REF_REPORT_ID,"
                            + /*
                             * 3
                             */ " a.GRAPH_ID,"
                            + /*
                             * 4
                             */ " a.KPI_MASTER_ID,"
                            + /*
                             * 5
                             */ " a.DISPLAY_SEQUENCE,"
                            + /*
                             * 6 all types of graph types
                             */ " a.DISPLAY_TYPE,"
                            + /*
                             * 7
                             */ " nvl(a.KPI_HEADING , b.graph_name),"
                            + /*
                             * 8
                             */ "c.report_name,"
                            + /*
                             * 9 (types of kpi standard r normal)
                             */ "a.kpi_type," + "a.ROW_1," + "a.COL_1," + "a.ROW_SPAN," + "a.COL_SPAN," + "a.KPI_NAME ," + "a.KPI_SYMBOL," + "b.TIME_SERIES,a.DASHLET_KPIANDTABLE_PROP ,a.KPI_HEADS,a.ROW_VIEW_BY,a.GROUPID,b.JQ_PROPERTIES"
                            + " FROM PRG_AR_DASHBOARD_DETAILS a,PRG_AR_GRAPH_MASTER b,PRG_AR_REPORT_MASTER c where a.dashboard_id=& and a.dashboard_id=c.report_id  and a.graph_id= b.graph_id(+) and a.DASHBOARD_DETAILS_ID=" + collect.getDashletId() + " order by a.row_1, a.col_1";

                }
            }
        }

        finalQuery = buildQuery(buildDashBoardQuery, ObjArray);
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        if (retObj != null && retObj.getRowCount() != 0) {
            this.container.setReportName(retObj.getFieldValueString(0, 8));
            this.container.setReportDesc(retObj.getFieldValueString(0, 8));

            for (int i = 0; i < retObj.getRowCount(); i++) {
                DashletDetail dashletDetail = new DashletDetail();

                String dashBoardDetailId = retObj.getFieldValueString(i, 0);
                String refRptId = retObj.getFieldValueString(i, 2);
                String graphId = retObj.getFieldValueString(i, 3);
                String kpiMasterId = retObj.getFieldValueString(i, 4);
                String displaySequence = retObj.getFieldValueString(i, 5);
                String displayType = retObj.getFieldValueString(i, 6);
                String graphName = retObj.getFieldValueString(i, 7);
                String kpiType = retObj.getFieldValueString(i, 9);
                String rowNum = retObj.getFieldValueString(i, 10);
                String colNum = retObj.getFieldValueString(i, 11);
                String rowSpan = retObj.getFieldValueString(i, 12);
                String colSpan = retObj.getFieldValueString(i, 13);
                String kpiName = retObj.getFieldValueString(i, 14);
                String kpiSymbol = retObj.getFieldValueString(i, 15);
                String isTimeSeries = retObj.getFieldValueString(i, 16);
                String RowViewBy = retObj.getFieldValueString(i, "ROW_VIEW_BY");
                String groupIds = retObj.getFieldValueString(i, "GROUPID");
                JqplotGraphProperty jqProperty = null;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    if (retObj.getRowCount() > 0 && retObj.getFieldUnknown(i, 21) != null) {
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        jqProperty = gson.fromJson(retObj.getFieldUnknown(i, 21), JqplotGraphProperty.class);
                    }
                } else {
                    if (retObj.getRowCount() > 0 && retObj.getFieldValueClobString(i, "JQ_PROPERTIES") != null) {
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        jqProperty = gson.fromJson(retObj.getFieldValueClobString(i, "JQ_PROPERTIES"), JqplotGraphProperty.class);
                    }
                }

                List<DashboardTableColorGroupHelper> tableColorGroupHelpers = null;
                String DASHLET_KPIANDTABLE_PROP = null;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    DASHLET_KPIANDTABLE_PROP = retObj.getFieldUnknown(i, 17);
                } else {
                    DASHLET_KPIANDTABLE_PROP = retObj.getFieldValueClobString(i, "DASHLET_KPIANDTABLE_PROP");
                }
                if (DASHLET_KPIANDTABLE_PROP != null) {
//                 Gson gson = new GsonBuilder().serializeNulls().create();
                    if (displayType.equalsIgnoreCase("table") || displayType.equalsIgnoreCase("newGraph")) {
                        Type listType = new TypeToken<ArrayList<DashboardTableColorGroupHelper>>() {
                        }.getType();
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            if (retObj.getFieldUnknown(i, 17) != null && !retObj.getFieldValueString(i, 17).equalsIgnoreCase("")) {
                                tableColorGroupHelpers = new Gson().fromJson(retObj.getFieldUnknown(i, 17), listType);
                            }
                        } else if (retObj.getFieldValueClobString(i, "DASHLET_KPIANDTABLE_PROP") != null && !retObj.getFieldValueString(i, "DASHLET_KPIANDTABLE_PROP").equalsIgnoreCase("")) {
                            tableColorGroupHelpers = new Gson().fromJson(retObj.getFieldValueClobString(i, "DASHLET_KPIANDTABLE_PROP"), listType);
                        }
                    } else {
                        Type listType = new TypeToken<ArrayList<KPISingleGroupHelper>>() {
                        }.getType();
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            singleGroupHelpers = new Gson().fromJson(retObj.getFieldUnknown(i, 17), listType);
                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                singleGroupHelpers = new Gson().fromJson(retObj.getFieldUnknown(i, 17), listType);
                            } else {
                                singleGroupHelpers = new Gson().fromJson(retObj.getFieldValueClobString(i, "DASHLET_KPIANDTABLE_PROP"), listType);
                            }
                        }
//                  singleGroupHelper=gson.fromJson(retObj.getFieldValueClobString(i, "DASHLET_KPIANDTABLE_PROP"),KPISingleGroupHelper.class);
//                this.container.setkPISingleGroupHelper(singleGroupHelper);
                    }
                } else {
                    singleGroupHelpers = null;
                }
                String kpiheads = retObj.getFieldValueString(i, "KPI_HEADS");
                ArrayList<String> Newkpiheads = new ArrayList<String>();
                String[] kpiheadsarray = kpiheads.split(",");
                for (int j = 0; j < kpiheadsarray.length; j++) {
                    Newkpiheads.add(kpiheadsarray[j]);
                }
                //added by sruthi for hide columns
                String hidecolumnsdata = null;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    hidecolumnsdata = retObj.getFieldValueString(i, 22);
                } else {
                    hidecolumnsdata = retObj.getFieldValueString(i, "Hidecolumns");
                }
                if (hidecolumnsdata != null) {
                    hidecolumnsdata = hidecolumnsdata.replace("[", "").replace("]", "");
                    String[] data = hidecolumnsdata.split("[,]");
                    for (int k = 0; k < data.length; k++) {
                        hidecolumns.add(data[k]);
                    }
                } else {
                    hidecolumns = null;
                }
                dashletDetail.setHidecolumns(hidecolumns);
                //ended by sruthi
                //added by sruthi for manuval bud
                String numberofdays = null;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    numberofdays = retObj.getFieldValueString(i, 23);
                } else {
                    numberofdays = retObj.getFieldValueString(i, "NO_DAYS");
                }
                dashletDetail.setNumberOfDays(numberofdays);
                String targetvalues = null;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    targetvalues = retObj.getFieldValueString(i, 24);
                } else {
                    targetvalues = retObj.getFieldValueString(i, "TARGET_MANUAL");
                }
                if (targetvalues.replace("{", "").replace("}", "") != null && !targetvalues.replace("{", "").replace("}", "").equalsIgnoreCase("") && !targetvalues.replace("{", "").replace("}", "").equalsIgnoreCase("null")) {
                    String[] targetdata = targetvalues.split("[,]");
                    for (int m = 0; m < targetdata.length; m++) {
                        String[] entry = targetdata[m].split("[=]");
                        if (!entry[1].trim().replace("{", "").replace("}", "").equalsIgnoreCase("null")) {
                            targetmanuval.put(entry[0].replace("{", "").trim(), Double.parseDouble(entry[1].replace("}", "").trim()));
                        } else {
                            targetmanuval.put(entry[0].trim(), null);
                        }
                    }
                } else {
                    targetmanuval = null;
                }
                dashletDetail.setTargetMauval(targetmanuval);
                //ended by sruthi
                dashletDetail.setDashBoardDetailId(dashBoardDetailId);
                dashletDetail.setRefReportId(refRptId);
                dashletDetail.setGraphId(graphId);
                dashletDetail.setKpiMasterId(kpiMasterId);
                dashletDetail.setDisplaySequence(Integer.parseInt(displaySequence));
                dashletDetail.setDisplayType(displayType);
                dashletDetail.setDashletName(graphName);
                dashletDetail.setKpiType(kpiType);
                dashletDetail.setRow(Integer.parseInt(rowNum));
                dashletDetail.setCol(Integer.parseInt(colNum));
                dashletDetail.setRowSpan(Integer.parseInt(rowSpan));
                dashletDetail.setColSpan(Integer.parseInt(colSpan));
                dashletDetail.setkpiName(kpiName);
                dashletDetail.setKpiSymbol(kpiSymbol);
                dashletDetail.setIsTimeSeries(Boolean.parseBoolean(isTimeSeries));
                dashletDetail.setSingleGroupHelpers(singleGroupHelpers);
                dashletDetail.setDashbrdTableColor(tableColorGroupHelpers);
                dashletDetail.setKpiheads(Newkpiheads);
                dashletDetail.setRowViewBy(RowViewBy);
                dashletDetail.setGroupElements(groupIds);
                dashletDetail.setJqplotgrapprop(jqProperty);


//                dashletDetail.setRow(Integer.parseInt(retObj.getFieldValueString(i,"ROW_1")));
//                dashletDetail.setCol(Integer.parseInt(retObj.getFieldValueString(i,"COL_1")));
//                dashletDetail.setRowSpan(Integer.parseInt(retObj.getFieldValueString(i,"ROW_SPAN")));
//                dashletDetail.setColSpan(Integer.parseInt(retObj.getFieldValueString(i,"COL_SPAN")));

                collect.addDashletDetail(dashletDetail);
            }
        }

        //Create the report object for each dashlet
        DashboardViewerDAO viewerDao = new DashboardViewerDAO();
        List<DashletDetail> dashletDetails = collect.dashletDetails;
        for (DashletDetail detail : dashletDetails) {
            String dashletDispType = detail.getDisplayType();
            Report reportDetails = null;
            if (collect.isOneviewForTest()) {
                if (DashboardConstants.KPI_REPORT.equalsIgnoreCase(dashletDispType)) {
                    reportDetails = viewerDao.getKPIDetails(detail.getKpiMasterId(), userId);
                } else if (DashboardConstants.KPI_GRAPH_REPORT.equalsIgnoreCase(dashletDispType)) {
                    reportDetails = viewerDao.getKPIGraphDetails(detail.getGraphId());
                }
            } else {
                if (DashboardConstants.KPI_REPORT.equalsIgnoreCase(dashletDispType)) {
                    reportDetails = viewerDao.getKPIDetails(detail.getKpiMasterId(), userId);
                } else if (DashboardConstants.MAP_REPORT.equalsIgnoreCase(dashletDispType)) {
                    reportDetails = viewerDao.getMapDetails(collect.reportId);
                } else if (DashboardConstants.KPI_GRAPH_REPORT.equalsIgnoreCase(dashletDispType)) {
                    reportDetails = viewerDao.getKPIGraphDetails(detail.getGraphId());
                } else if (DashboardConstants.GRAPH_REPORT.equalsIgnoreCase(dashletDispType)
                        || DashboardConstants.DASHBOARD_GRAPH_REPORT.equalsIgnoreCase(dashletDispType)) {
                    reportDetails = viewerDao.getGraphDetails(collect.reportId, detail.getGraphId());
                } else if (DashboardConstants.TABLE_REPORT.equalsIgnoreCase(dashletDispType)
                        || DashboardConstants.DASHBOARD_GRAPH_REPORT.equalsIgnoreCase(dashletDispType)) {
                    reportDetails = viewerDao.getGraphDetails(collect.reportId, detail.getGraphId());
                } else if (DashboardConstants.TEXTKPI_REPORT.equalsIgnoreCase(dashletDispType)) {
                    reportDetails = viewerDao.getTextKpiDashletDetails(collect.reportId, detail.getGraphId(), detail);
                } else if (DashboardConstants.GROUP_MEASSURE_INSIGHTS.equalsIgnoreCase(dashletDispType)) {
                    reportDetails = viewerDao.getGroupMeassureDashletDetails(collect.reportId, detail.getGraphId(), detail);
                } else if (DashboardConstants.KPI_WITH_GRAPH.equalsIgnoreCase(dashletDispType)) {
                    reportDetails = viewerDao.getGraphDetails(collect.reportId, detail.getGraphId());
                    KPI kpiDetail = viewerDao.getKpiDetailsForKPIWithGRaph(detail);
                    detail.setKpiDetails(kpiDetail, dashletDispType);
                    // }

                }
            }
            detail.setReportDetails(reportDetails);
            if (!collect.isOneviewForTest()) {
                if (DashboardConstants.KPI_REPORT.equalsIgnoreCase(dashletDispType)) {
                    if (singleGroupHelpers != null && !singleGroupHelpers.isEmpty()) {
                        for (KPISingleGroupHelper singleGroupHelper : singleGroupHelpers) {
                            if (singleGroupHelper.getGroupName() != null) {
                                KPI kpiDetails = (KPI) detail.getReportDetails();
                                if (!kpiDetails.getKPIElementsMap().containsKey(singleGroupHelper.getGroupName())) {
                                    KPIElement element = new KPIElement();
                                    element.setElementId(singleGroupHelper.getGroupName());
                                    element.setElementName(singleGroupHelper.getGroupName());
                                    element.setIsGroupElement(true);
                                    element.setRefElementId(Joiner.on(",").join(singleGroupHelper.getElementIds()));

                                    kpiDetails.getElementIds().add(singleGroupHelper.getGroupName());
                                    kpiDetails.getKPIElementsMap().put(singleGroupHelper.getGroupName(), element);
                                    String drillVal = kpiDetails.getKPIDrill(singleGroupHelper.getGroupName());
                                    singleGroupHelper.addGroupKPIDrill(singleGroupHelper.getGroupName(), drillVal);
                                }
                            }
                        }
                    }

                }

            }
        }

    }

    // code to build the combined query for all the graphs in this dashboard and get the return object.
    // Then the region for the graphs will be created and passed to the frontend.
    // Each report then calls asynchronously and creates the respective report from the data.
    public PbReturnObject getDashboardData(Container container, pbDashboardCollection collect, String userId) {
        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject pbretObj = null;
        ArrayList<String> qryColIds = (ArrayList) collect.getQryColIds();
        ArrayList<String> qryAggrs = (ArrayList) collect.getQryColAggr();

        List<DashletDetail> dashlets = collect.dashletDetails;
        Set<QueryDetail> qdSet = new HashSet<QueryDetail>();
        for (DashletDetail detail : dashlets) {
            if (DashboardConstants.GRAPH_REPORT.equalsIgnoreCase(detail.getDisplayType())
                    || DashboardConstants.DASHBOARD_GRAPH_REPORT.equalsIgnoreCase(detail.getDisplayType()) || DashboardConstants.TABLE_REPORT.equalsIgnoreCase(detail.getDisplayType()) || DashboardConstants.GROUP_MEASSURE_INSIGHTS.equalsIgnoreCase(detail.getDisplayType())) {
                GraphReport graphDetails = (GraphReport) detail.getReportDetails();
                if (detail.getIsTimeSeries() != null && graphDetails != null) {
                    graphDetails.setTimeSeries(detail.getIsTimeSeries());
                    if (graphDetails.isTimeSeries()) {
                        continue;
                    }
                } else {
                    if (graphDetails != null) {
                        if (graphDetails.isTimeSeries()) {
                            continue;
                        }
                    }
                }
            }

            if (detail.getReportDetails() != null) {
                List<QueryDetail> qdList = detail.getReportDetails().getQueryDetails();
                if (qdList != null) {
                    qdSet.addAll(qdList);
                }
            }
        }
        if (!qdSet.isEmpty()) {
            Iterator<QueryDetail> iter = qdSet.iterator();
            while (iter.hasNext()) {
                QueryDetail qd = iter.next();
                String elemId = qd.getElementId();
                if (!(qryColIds.contains(elemId))) {
                    qryColIds.add(qd.getElementId());       //Get the element id
                    qryAggrs.add(qd.getAggregationType());        //Get the aggregamtion type
                }
            }
        }

        if (!qryColIds.isEmpty()) {
            repQuery.setRowViewbyCols(collect.reportRowViewbyValues);
            repQuery.setColViewbyCols(collect.reportColViewbyValues);
            repQuery.setParamValue(collect.reportParametersValues);
            HashMap<String, List> inMap = (HashMap<String, List>) collect.operatorFilters.get("IN");
            if (inMap != null) {
                repQuery.setInMap(inMap);
            }
            repQuery.setQryColumns(qryColIds);
            repQuery.setColAggration(qryAggrs);
            repQuery.setTimeDetails(collect.timeDetailsArray);
            repQuery.isKpi = false;
            repQuery.setReportId(collect.reportId);
            repQuery.setBizRoles(collect.reportBizRoles[0]);
            repQuery.setUserId(userId);

            pbretObj = repQuery.getPbReturnObjectWithFlag(String.valueOf(qryColIds.get(0)));
            HashMap vals111 = new HashMap();
            vals111 = repQuery.getTimememdetails();
            collect.setTimememdetails(vals111);
            container.setTimememdetails(vals111);

            ArrayList<String> dispColumns = new ArrayList<String>();
            ArrayList<String> dispLabels = new ArrayList<String>();
            ArrayList<String> tableMeasures = new ArrayList<String>();
            ArrayList<String> dataTypes = new ArrayList<String>();

            for (int i = 0; i < collect.reportRowViewbyValues.size(); i++) {
                String dimId = collect.reportRowViewbyValues.get(i);
                String dimName = collect.getParameterDispName(dimId);
                dispColumns.add("A_" + dimId);
                dispLabels.add(dimName);
                dataTypes.add("C");
            }
            for (int i = 0; i < qryColIds.size(); i++) {
                String measId = "A_" + qryColIds.get(i);
                dispColumns.add(measId);
                dispLabels.add((String) repQuery.NonViewByMap.get(measId));
                tableMeasures.add((String) repQuery.NonViewByMap.get(measId));
                dataTypes.add("N");
            }
            container.setRetObj(pbretObj);
            container.setDisplayColumns(dispColumns);
            container.setDisplayLabels(dispLabels);
            container.setDataTypes(dataTypes);
            container.getTableHashMap().put("Measures", tableMeasures);
        }
        return pbretObj;
    }

    // code to build the combined query for all the KPI and Scorecard in this dashboard and get the return object.
    // Each report then calls asynchronously and creates the respective report from the data.
    public PbReturnObject getDashboardKPIData(Container container, pbDashboardCollection collect, String userId) {

        PbReturnObject pbretObj = null;
        Boolean ismonthObject = false;
        Boolean isYrObject = false;
        Boolean isDayObject = false;
        Boolean isQtrObject = false;
        ArrayList<String> queryCols = new ArrayList<String>();
        ArrayList<String> queryAggs = new ArrayList<String>();
        collect.kpiElementList.clear();
        boolean isMultiPeriodKpiexists = false;
        boolean isolapgraph = false;
        String viewbyid = null;
        List<DashletDetail> dashlets = collect.dashletDetails;
        Set<KPIElement> elemSet = new LinkedHashSet<KPIElement>();
        for (DashletDetail detail : dashlets) {
            if (detail.getDisplayType() != null && detail.getDisplayType().equalsIgnoreCase("KPI")) {
                if ((detail.getKpiType().equalsIgnoreCase("MultiPeriod") || detail.getKpiType().equalsIgnoreCase("MultiPeriodCurrentPrior"))) {
                    KPI reportDetails = (KPI) detail.getReportDetails();
                    if (reportDetails != null && reportDetails.isMTDChecked()) {
                        ismonthObject = true;
                    }
                    if (reportDetails != null && reportDetails.isYTDChecked()) {
                        isYrObject = true;
                    }
                    if (reportDetails != null && reportDetails.isQTDChecked()) {
                        isQtrObject = true;
                    }
                    if (reportDetails != null && reportDetails.isCurrentChecked()) {
                        isDayObject = true;
                    }
                }
            }

        }
        for (DashletDetail detail : dashlets) {
            Report reportDetails = detail.getReportDetails();
            if (detail.getDisplayType() != null && detail.getDisplayType().equalsIgnoreCase("KpiWithGraph")) {
                reportDetails = detail.getKpiDetails();
            }
            if (detail.getGraphtype() != null && detail.getGraphtype().equalsIgnoreCase("olapgraph")) {
                viewbyid = detail.getRowViewBy();
                isolapgraph = true;
            }
            if (reportDetails != null) {
                List<KPIElement> elemList = reportDetails.getKPIElements();
                List<KPIElement> TargetElements = reportDetails.gettargetKpiElements();
                if (elemList != null) {
                    elemSet.addAll(elemList);

                }
                if (TargetElements != null) {
                    elemSet.addAll(TargetElements);
                }
                if (detail.getKpiType() != null) {
                    if ((detail.getKpiType().equalsIgnoreCase("MultiPeriod") || detail.getKpiType().equalsIgnoreCase("MultiPeriodCurrentPrior")) && !isMultiPeriodKpiexists) {
                        isMultiPeriodKpiexists = true;
                    }
                }

            }
        }
        if (elemSet != null && !elemSet.isEmpty()) {
            Iterator<KPIElement> iter = elemSet.iterator();
            while (iter.hasNext()) {
                KPIElement elem = iter.next();
                if (elem != null) {
                    String refElementId = elem.getRefElementId();
                    if (!elem.isIsGroupElement()) {
                        queryCols.add(elem.getElementId());       //Get the element id
                        queryAggs.add(elem.getAggregationType());        //Get the aggregation type
                        collect.kpiElementList.put(refElementId, elem);
                    }
                }
            }
        }

        if (!queryCols.isEmpty()) {
            try {
                PbReportQuery repQuery = new PbReportQuery();

//                a.add("111667");
//                repQuery.setRowViewbyCols(a);
                if (container.ViewBy.isEmpty()) //changed by mohit
                {
                    repQuery.setRowViewbyCols(new ArrayList());
                    repQuery.isKpi = true;
                } else {
//                       ArrayList a=new ArrayList();
//                    a.add(container.ViewBy);
                    repQuery.setRowViewbyCols(container.ViewBy);
                }
//                   repQuery.setRowViewbyCols(container.getViewByElementIds());
                repQuery.setParamValue(collect.reportParametersValues);//Added by k
                HashMap<String, List> inMap = (HashMap<String, List>) collect.operatorFilters.get("IN");
                if (inMap != null) {
                    repQuery.setInMap(inMap);
                }

                repQuery.setColViewbyCols(new ArrayList());
                repQuery.setQryColumns(queryCols);
                repQuery.setColAggration(queryAggs);
                repQuery.setTimeDetails(collect.timeDetailsArray);
                repQuery.setDefaultMeasure(String.valueOf(queryCols.get(0)));
                repQuery.setDefaultMeasureSumm(String.valueOf(queryAggs.get(0)));
                repQuery.setReportId(collect.reportId);
                repQuery.setBizRoles(collect.reportBizRoles[0]);
                repQuery.setUserId(userId);
                if (isolapgraph) {
                    ArrayList viewbyids = new ArrayList();
                    viewbyids.add(viewbyid);
                    repQuery.setRowViewbyCols(viewbyids);
                    repQuery.isKpi = false;
                }
                MultiPeriodKPI multiPeriodKPI = new MultiPeriodKPI();
                ArrayList<String> multiperiods = new ArrayList<String>();
                if (isYrObject) {
                    multiperiods.add("Year");
                }
                if (isQtrObject) {
                    multiperiods.add("Qtr");
                }
                if (ismonthObject) {
                    multiperiods.add("Month");
                }
                if (isDayObject) {
                    multiperiods.add("Day");
                }
//        if(collect.dashletDetails.get(0).getKpiType().equalsIgnoreCase("MultiPeriod")){
                PbReturnObject kpiMulitiObject = new PbReturnObject();
                if (isMultiPeriodKpiexists) {
                    ArrayList tempTimeDetailsArrayList = (ArrayList) collect.timeDetailsArray.clone();
                    for (String str : multiperiods) {
                        //tempTimeDetailsArrayList.set(3, str);   //commented By Bhargavi on 1st April 2015
                        repQuery.setTimeDetails(tempTimeDetailsArrayList);
                        kpiMulitiObject = repQuery.getPbReturnObjectWithFlag(String.valueOf(queryCols.get(0)));
                        HashMap vals111 = new HashMap();
                        vals111 = repQuery.getTimememdetails();
                        collect.setTimememdetails(vals111);
                        container.setTimememdetails(vals111);
                        if (str.equalsIgnoreCase("Year")) {
                            multiPeriodKPI.setYearObject(kpiMulitiObject);
                        } else if (str.equalsIgnoreCase("Qtr")) {
                            multiPeriodKPI.setQuarterObject(kpiMulitiObject);
                        } else if (str.equalsIgnoreCase("Month")) {
                            multiPeriodKPI.setMonthObject(kpiMulitiObject);
                        } else if (str.equalsIgnoreCase("Day")) {
                            multiPeriodKPI.setDayObject(kpiMulitiObject);
                        }
                    }

                    container.setMultiPeriodKPI(multiPeriodKPI);
                }
                repQuery.setTimeDetails(collect.timeDetailsArray);
                pbretObj = repQuery.getPbReturnObject(String.valueOf(queryCols.get(0)));
                container.setSqlStr(repQuery.getFinalNormalQuery());
                HashMap vals111 = new HashMap();
                vals111 = repQuery.getTimememdetails();
                collect.setTimememdetails(vals111);
                container.setTimememdetails(vals111);
//        }else{


//                }
                //For Debugging
                //                String[] cols = pbretObj.getColumnNames();
                //                
                //                for (int i=0;i<cols.length;i++){
                //                    
                //                }
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }
        return pbretObj;
    }

    public PbReturnObject getTimeSeriesData(pbDashboardCollection collect, String userId) {
        PbReturnObject pbretObj = null;
        ArrayList<String> qryColIds = new ArrayList<String>();
        String[] folderIds = collect.reportBizRoles;

        List<DashletDetail> dashlets = collect.dashletDetails;
        Set<QueryDetail> qdSet = new HashSet<QueryDetail>();
        for (DashletDetail detail : dashlets) {
            if (DashboardConstants.GRAPH_REPORT.equalsIgnoreCase(detail.getDisplayType())
                    || DashboardConstants.DASHBOARD_GRAPH_REPORT.equalsIgnoreCase(detail.getDisplayType())
                    || DashboardConstants.GROUP_MEASSURE_INSIGHTS.equalsIgnoreCase(detail.getDisplayType())) {

                GraphReport graphDetails = (GraphReport) detail.getReportDetails();
                if (graphDetails.isTimeSeries()) {
                    List<QueryDetail> qdList = detail.getReportDetails().getQueryDetails();
                    if (qdList != null) {
                        qdSet.addAll(qdList);
                    }
                }
            } else if (DashboardConstants.TABLE_REPORT.equalsIgnoreCase(detail.getDisplayType())
                    || DashboardConstants.TABLE_REPORT.equalsIgnoreCase(detail.getDisplayType())) {

                GraphReport graphDetails = (GraphReport) detail.getReportDetails();
                if (graphDetails.isTimeSeries()) {
                    List<QueryDetail> qdList = detail.getReportDetails().getQueryDetails();
                    if (qdList != null) {
                        qdSet.addAll(qdList);
                    }
                }
            }
        }
        if (!qdSet.isEmpty()) {
            Iterator<QueryDetail> iter = qdSet.iterator();
            String folderId = "";
            while (iter.hasNext()) {
                QueryDetail qd = iter.next();
                String elemId = qd.getElementId();
                if (!(qryColIds.contains(elemId))) {
                    qryColIds.add(qd.getElementId());       //Get the element id
                }
                folderId = String.valueOf(qd.getFolderId());
            }
            if (folderIds != null && folderIds.length > 0) {
                folderId = folderIds[0];
            }

            TrendDataSet dataSet = TrendDataSetHelper.buildTrendDataSet(qryColIds, (String) collect.timeDetailsArray.get(3), userId, folderId);
            if (dataSet != null) {
                pbretObj = dataSet.getReturnObject();
            }
        }

        return pbretObj;
    }

    public static void main(String args[]) {

        ArrayList masterarray = new ArrayList();
        masterarray.add(10);
        masterarray.add(20);
        masterarray.add(30);
        masterarray.add(40);
        masterarray.add(0);
        masterarray.add(0);
        masterarray.add(0);
        masterarray.add(0);
        masterarray.add(0);
        masterarray.add(0);
        masterarray.add(0);
        masterarray.add(0);
        masterarray.add(0);
        masterarray.add(0);
        masterarray.add(0);
        masterarray.add(0);
        ////////////.println("Before masterarray=" + masterarray);

        for (int p = 0; p < masterarray.size(); p++) {
            if (masterarray.get(p).equals(0)) {

                masterarray.remove(p);

                if (p > 0) {
                    p = 0;
                } else {
                    p = -1;
                }

                ////////////.println("p="+p);
            }

        }

        ////////////.println("After masterarray=" + masterarray);



    }

    public PbReturnObject processSearchKpi(Container container, String timeLevel, String userId) {
        ProgenParam pParam = new ProgenParam();
        PbDb pbDb = new PbDb();
        PbReportCollection kpiCollect = (PbReportCollection) container.getReportCollect().clone();

        QueryExecutor qryExec = new QueryExecutor();
        ArrayList<String> reportQryElementIds = new ArrayList<String>();
        ArrayList<String> reportQryColAggs = new ArrayList<String>();

        ArrayList<String> measIds = (ArrayList) container.getReportHashMap().get("reportQryElementIds");
        ArrayList<String> rowViewBys = (ArrayList) container.getReportHashMap().get("reportQryElementIds");
        PbReturnObject retObj;

        try {
            for (String measEleId : measIds) {
                String sqlstr = "select ELEMENT_ID , REF_ELEMENT_ID , REF_ELEMENT_TYPE , AGGREGATION_TYPE ";
                sqlstr += " from ";
                sqlstr += " PRG_USER_ALL_INFO_DETAILS ";
                sqlstr += " where ELEMENT_ID=" + measEleId + " ";
                sqlstr += " OR REF_ELEMENT_ID = " + measEleId + " ";
                sqlstr += " order by REF_ELEMENT_TYPE asc ";

                retObj = pbDb.execSelectSQL(sqlstr);
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    reportQryElementIds.add(retObj.getFieldValueString(i, 0));
                    reportQryColAggs.add(retObj.getFieldValueString(i, 3));
                }
            }
            kpiCollect.isKpi = true;
            kpiCollect.reportQryElementIds = reportQryElementIds;
            kpiCollect.reportQryAggregations = reportQryColAggs;
            kpiCollect.reportRowViewbyValues = new ArrayList<String>();
            kpiCollect.reportColViewbyValues = new ArrayList<String>();

            ArrayList timeDetails = new ArrayList();
            if ("MONTH".equalsIgnoreCase(timeLevel)) {
                timeDetails.add("Day");
                timeDetails.add("PRG_STD");
                timeDetails.add(pParam.getdateforpage().toString());
                timeDetails.add("Month");
                timeDetails.add("Last Period");
            } else {
                timeDetails.add("Day");
                timeDetails.add("PRG_STD");
                timeDetails.add(pParam.getdateforpage().toString());
                timeDetails.add("Qtr");
                timeDetails.add("Last Period");
            }
            kpiCollect.timeDetailsArray = timeDetails;

            PbReportQuery reportQuery = qryExec.formulateQuery(kpiCollect, userId);

            String query = null;
            query = reportQuery.generateViewByQry();

            retObj = qryExec.executeQuery(kpiCollect, query);
            return retObj;

        } catch (Exception ex) {
            retObj = new PbReturnObject();
        }
        return retObj;
    }

    //added for Table in Dashboard
    public String buildDbrdTable(Container container, String dashletId, boolean fromDesigner, String editDbrd, boolean flag) {
        DashboardViewerDAO viewerDao = new DashboardViewerDAO();
        PbReportViewerDAO dao = new PbReportViewerDAO();
        DashletPropertiesHelper dashletPropertiesHelper = null;
        if (container.getDashletPropertiesHelper() == null) {
            dashletPropertiesHelper = viewerDao.getDashletPropertiesHelperObject(dashletId);
        } else {
            if (container.getDashletPropertiesHelper().getDashletId() != null) {
                if (container.getDashletPropertiesHelper().getDashletId().equalsIgnoreCase(dashletId)) {
                    dashletPropertiesHelper = container.getDashletPropertiesHelper();
                }
            }
        }
        container.setDashletPropertiesHelper(dashletPropertiesHelper);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();

        if (dashletPropertiesHelper == null || !dashletPropertiesHelper.isIsTranspose()) {
            DashletDetail dashlet = collect.getDashletDetail(dashletId);
            Boolean sortFlag = false;
            if (dashletPropertiesHelper != null) {
                sortFlag = Boolean.parseBoolean(dashletPropertiesHelper.getSortFlag());
            }
            String Jqgraphtypename = "";
            String jqgraphtypeId = "";
            String jqtype = "false";
            graphId = dashlet.getGraphId();
            GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
            graphDetails.setDashletpropertieshelper(dashletPropertiesHelper);
            int colSpan = dashlet.getColSpan();
            int rowSpan = dashlet.getRowSpan();
            int rowNum = dashlet.getRow();
            int colNum = dashlet.getCol();
            int width = 350;
            int height = 350;
            height = height * rowSpan;
            width = width * colSpan;
            String dashId = dashlet.getDashBoardDetailId();
            List<DashletDetail> dashletDetails = collect.dashletDetails;
            ArrayListMultimap<Integer, Integer> rowinfo = ArrayListMultimap.create();
            for (int i = 0; i < dashletDetails.size(); i++) {
                DashletDetail detail = dashletDetails.get(i);
                rowinfo.put(detail.getRow(), detail.getCol());
            }
            List<Integer> dashlets = rowinfo.get(Integer.parseInt(dashId));
            int numOfDashlets = dashlets.size();

            PbReturnObject retObj = null;
            Set<String> elementIdSet = new LinkedHashSet<String>();

            if (editDbrd != null && "true".equalsIgnoreCase(editDbrd)) {
                fromDesigner = true;
            }

            if (graphDetails.isTimeSeries()) {
                retObj = container.getTimeSeriesRetObj();
            } else {
                retObj = (PbReturnObject) container.getRetObj();
            }
            StringBuilder tableBuffer = new StringBuilder();

            if (retObj != null) {
                ArrayList<String> rowViewBys = collect.reportRowViewbyValues;
                String dashletName = dashlet.getDashletName();
                String dashBoardId = container.getReportId();
                dashBoardId = container.getDashboardId();
                String refRepId = dashlet.getRefReportId();
//            String paramName1 = container.getParamSectionDisplay();
                if (dashBoardId == null) {
                    dashBoardId = collect.reportId;
                }
                String kpiMasterId = dashlet.getKpiMasterId();
                graphDetails.setShowAsTable(true);
                List<QueryDetail> queryDetails = graphDetails.getQueryDetails();
                List<String> elementIds = new ArrayList<String>();
                List<String> colTypes = new ArrayList<String>();
                int dashletNameLength = dashletName.length();
                String newDashletName = "";
                GraphBuilder gb = new GraphBuilder();
                int maxCharLength = gb.getGraphNameMaxCharacters(collect, dashletId);
                if (flag || (dispType == null && !dashlet.getDisplayType().equalsIgnoreCase("Table"))) {
                    newDashletName = dashletName;
                } else {
                    if (dashletNameLength > maxCharLength) {
                        newDashletName = dashletName.substring(0, maxCharLength);
                        newDashletName += "...";
                    } else {
                        if (dashlet.getkpiName().equalsIgnoreCase("KPI REGION")) {
                            newDashletName = "Table Region";
                        } else {
                            newDashletName = dashlet.getkpiName();
                        }

                    }
                }
//            String rowViewByMeasureNames=null;
                StringBuilder rowViewByMeasureNames = new StringBuilder(400);
//                String measuresForApplyColor = null;
                StringBuilder measuresForApplyColor = new StringBuilder();
                for (int k = 0; k < rowViewBys.size(); k++) {
                    String rowViewById = rowViewBys.get(k);
                    String paramName = collect.getParameterDispName(rowViewById);
//                 rowViewByMeasureNames=rowViewByMeasureNames+"&"+rowViewById+","+paramName;
                    rowViewByMeasureNames.append("&").append(rowViewById).append(",").append(paramName);
                }
                for (QueryDetail qd : queryDetails) {
//                    measuresForApplyColor = measuresForApplyColor + "&" + qd.getElementId() + "," + qd.getDisplayName();
                    measuresForApplyColor.append( "&").append( qd.getElementId() ).append( "," ).append( qd.getDisplayName());
                    rowViewByMeasureNames.append("&").append(qd.getElementId()).append(",").append(qd.getDisplayName());
//                 rowViewByMeasureNames=rowViewByMeasureNames+"&"+qd.getElementId()+","+qd.getDisplayName();
                }
                //Creating the dashlet header
                tableBuffer.append("<div id=\"DashletHeader-" + dashletId + "\" class=\"portlet-header1 navtitle portletHeader ui-corner-all\"  align=\"center\">");
                tableBuffer.append("<Table width=\"100%\">");
                if (dashlet.getDisplayType().equalsIgnoreCase("KpiWithGraph")) {
                    tableBuffer.append("<tr align=\"right\" width=\"100%\">");
                } else {
                    tableBuffer.append("<tr align=\"left\" width=\"100%\">");
                }

                if ("Graph".equalsIgnoreCase(dispType)) {
                    tableBuffer.append("<td><strong><a href=\"javascript:parent.submiturls1('reportViewer.do?reportBy=viewReport&REPORTID=").
                            append(refReportId).append("')\">").append(newDashletName).append("</a></strong></td>");
                } else {
                    if (!dashlet.getDisplayType().equalsIgnoreCase("KpiWithGraph")) {
                        tableBuffer.append("<td width=\"50%\" valign=\"top\">");
                        tableBuffer.append("<table align=\"left\">");
                        tableBuffer.append("<tr>");
                        tableBuffer.append("<td  valign=\"top\" ><a  title=\"Dashlet Rename\" onclick=\"TableRename('" + refRepId + "','" + newDashletName + "','" + dashletId + "','" + dashBoardId + "','" + flag + "')\"><strong>").append(newDashletName).append("</a> </strong></td>");
                        tableBuffer.append("</tr></table></td>");
                        if (!fromDesigner) {
                            tableBuffer.append("<td width=\"50%\">");
                            tableBuffer.append("<table align=\"right\">");
                            tableBuffer.append("<tr>");

                            tableBuffer.append("<td align=\"right\">");//Marker starts
                            tableBuffer.append("<a href='javascript:void(0)'style='text-decoration:none'  title=\"Click For Options\" class=\"calcTitle\" onClick=\"getDashletTableOptions('" + dashletId + "')\"><font size=\"1px\"><b>Table Properties</b></font></a>&nbsp;&nbsp;");
                            tableBuffer.append("<div  style='display:none;width:100px;height:100px;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width:0px;' id='DashletOptions-" + dashletId + "'>");
                            tableBuffer.append("<table>");
                            tableBuffer.append("<tr>");
                            tableBuffer.append("<td>"); //code for sort top/bottom
                            tableBuffer.append("<a href=\"javascript:void(0)\" class=\"ui-icon ui-icon-triangle-2-n-s\" onclick=\"sort_Top_Buttom('" + dashBoardId + "','" + kpiMasterId + "','" + dashletId + "','" + flag + "','" + rowViewByMeasureNames + "')\"   style=\"text-decoration:none\"  title=\"Top/Bottom\"><font size=\"1px\"><b></b></font></a>");

                            tableBuffer.append("</td>");//end of sort top/bottom
                            tableBuffer.append("<td>");
                            tableBuffer.append("<a href=\"javascript:void(0)\"  style=\"text-decoration:none\" onclick=\"sort_Top_Buttom('" + dashBoardId + "','" + kpiMasterId + "','" + dashletId + "','" + flag + "','" + rowViewByMeasureNames + "')\"   title=\"Top/Bottom\"><font size=\"1px\"><b>Top/Bottom</b></font></a>");

                            tableBuffer.append("</td>");
                            tableBuffer.append("</tr>");
                            tableBuffer.append("<tr>");
                            tableBuffer.append("<td>");
                            tableBuffer.append("<a href=\"javascript:void(0)\"  class=\"ui-icon ui-icon-arrowthick-2-n-s\" onclick=\"sortByNumber('" + dashletId + "','" + rowViewByMeasureNames + "','" + flag + "')\"   style=\"text-decoration:none\"  title=\"SortByNumbers\"><font size=\"1px\"><b></b></font></a>");
                            tableBuffer.append("</td>");
                            tableBuffer.append("<td>");
                            tableBuffer.append("<a href=\"javascript:void(0)\"  onclick=\"sortByNumber('" + dashletId + "','" + rowViewByMeasureNames + "')\"   style=\"text-decoration:none\"  title=\"SortByNumbers\"><font size=\"1px\"><b>Sort</b></font></a>");
                            tableBuffer.append("</td>");
                            tableBuffer.append("</tr>");
                            tableBuffer.append("<tr>");
                            tableBuffer.append("<td>");
                            tableBuffer.append("<a href=\"javascript:void(0)\" class=\"ui-icon ui-icon-pencil\" style=\"text-decoration:none\" onclick=\"applyColor('" + dashletId + "','" + measuresForApplyColor + "','" + flag + "','" + dashBoardId + "')\"  title=\"ApplyColor\"><font size=\"1px\"><b></b></font></a>");
                            tableBuffer.append("</td>");
                            tableBuffer.append("<td>");
                            tableBuffer.append("<a href=\"javascript:void(0)\"  style=\"text-decoration:none\" onclick=\"applyColor('" + dashletId + "','" + measuresForApplyColor + "','" + flag + "','" + dashBoardId + "')\"  title=\"ApplyColor\"><font size=\"1px\"><b>ApplyColor</b></font></a>");
                            tableBuffer.append("</td>");
                            tableBuffer.append("</tr>");
                            tableBuffer.append("<tr>");
                            tableBuffer.append("<td>");
                            tableBuffer.append("<a href=\"javascript:void(0)\" class=\"ui-icon ui-icon-transferthick-e-w\" onclick=\"tableTranspose('" + dashletId + "','" + flag + "')\"   style=\"text-decoration:none\"   title=\"Transpose\"><font size=\"1px\"><b></b></font></a>");
                            tableBuffer.append("</td>");
                            tableBuffer.append("<td align=\"left\">");
                            tableBuffer.append("<a href=\"javascript:void(0)\"  onclick=\"tableTranspose('" + dashletId + "','" + flag + "')\"   style=\"text-decoration:none\"   title=\"Transpose\"><font size=\"1px\"><b>Transpose</b></font></a>");
                            tableBuffer.append("</td>");
                            tableBuffer.append("</tr>");
                            tableBuffer.append("<tr>");
                            tableBuffer.append("<td>").append("<a href=\"javascript:void(0)\" class=\"ui-icon ui-icon-refresh\" onclick=\"resetTableData('" + dashletId + "','" + flag + "','true')\"   style=\"text-decoration:none\"  title=\"reset\"><font size=\"1px\"><b></b></font></a>").append("</td>");
                            tableBuffer.append("<td>").append("<a href=\"javascript:void(0)\"  onclick=\"resetTableData('" + dashletId + "','" + flag + "','true')\"   style=\"text-decoration:none\"  title=\"reset\"><font size=\"1px\"><b>Reset</b></font></a>").append("</td>");
                            tableBuffer.append("</tr>");
                            tableBuffer.append("</table>");
                            tableBuffer.append("</div>");
                            tableBuffer.append("</td>");//Marker ends
                        }
                        tableBuffer.append("<td>");
                        tableBuffer.append("<a href=\"javascript:void(0)\" class=\"ui-icon ui-icon-clock\" onclick=\"getDbrdTimeDisplay('" + dashBoardId + "')\"  title=\"Time Info\"></a>");
                        tableBuffer.append("</td>");
                        tableBuffer.append("</tr>");
                        tableBuffer.append("</table>");
                        tableBuffer.append("</td>");
                        tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;");
                        tableBuffer.append("</td>");
                    }
                }


                if (flag || (dispType == null && !dashlet.getDisplayType().equalsIgnoreCase("Table"))) {
                    if (dashlet.getDisplayType().equalsIgnoreCase("KpiWithGraph")) {
                        tableBuffer.append("<td align=\"\" valign=\"top\">");
                        tableBuffer.append("<table><tr>");
                        tableBuffer.append("<td id=\"DbrdTable-" + dashletId + "\" valign=\"top\" ><a class=\"ui-icon ui-icon-image\" href='javascript:void(0)' "
                                + "onclick=\"KpiGraphToTable(" + dashletId + "," + dashBoardId + ",'" + refReportId + "'," + graphId + ",'" + kpiMasterId + "','" + dispSequence + "','" + dispType + "','" + dashletName + "','graph','" + fromDesigner + "')\" "
                                + "style='text-decoration:none' class=\"calcTitle\" title=\"Switch to Graph\">Switch to Graph</a>");
                    } else {
                        tableBuffer.append("<td align=\"left\" valign=\"top\">");
                        tableBuffer.append("<table><tr>");

                        if (dashlet.getJqplotgrapprop() != null) {
                            JqplotGraphProperty jqprop = dashlet.getJqplotgrapprop();
                            Jqgraphtypename = jqprop.getGraphTypename();
                            jqgraphtypeId = jqprop.getGraphTypeId();
                            jqtype = "true";

                        }
                        tableBuffer.append("<td id=\"DbrdTable-" + dashletId + "\" valign=\"top\"><a class=\"ui-icon ui-icon-image\" href='javascript:void(0)' "
                                + "onclick=\"dbrdTable(" + dashletId + "," + dashBoardId + ",'" + refReportId + "'," + graphId + ",'" + kpiMasterId + "','" + dispSequence + "','" + dispType + "','" + dashletName + "','graph','" + fromDesigner + "','" + flag + "','" + Jqgraphtypename + "','" + jqgraphtypeId + "'," + jqtype + ",'" + height + "')\" "
                                + "style='text-decoration:none' class=\"calcTitle\" title=\"Switch to Graph\">Switch to Graph</a>");
                    }
                }

                tableBuffer.append("</td>");

                if (fromDesigner) {
                    tableBuffer.append("<td> <img style=\"width:20px;\" src='images/clear-icon.png' onclick=\"clearDashlet('" + dashletId + "','" + numOfDashlets + "','" + rowNum + "','" + colNum + "','" + rowSpan + "','" + colSpan + "')\" /></td>");
                    tableBuffer.append("<td align=\"right\"><a class=\"ui-icon ui-icon-trash\" onclick=\"closeOldPortlet('Dashlets-" + dashletId + "'," + dashletId + ")\" href=\"javascript:void(0)\" /></td>");
                }
//            tableBuffer.append("<td><a class=\"ui-icon ui-icon-trash\" onclick=\"closeOldPortlet('Dashlets-" + dashletId + "',"+dashletId+")\" href=\"javascript:void(0)\" /></td>");
                tableBuffer.append("</tr>");
                tableBuffer.append("</Table>");

                tableBuffer.append("</td></tr></table>");
                tableBuffer.append("</div>");

                //Creating the dashlet Table
                tableBuffer.append("<div  id='zoom" + graphId + "'  style=\"width:100%;height:" + height + "px;overflow-y:auto;overflow-x:auto\"  align=\"center\">");

                tableBuffer.append("<table cellspacing=\"1\" class=\"tablesorter\" id=\"tablesorter\" width=\"100px\">");
                tableBuffer.append("<thead>");
                tableBuffer.append("<tr valign=\"top\">");
                if (graphDetails.isTimeSeries()) {
                    tableBuffer.append("<th>Time</th> ");
                    colTypes.add("C");
                    elementIdSet.add("TIME");
                } else {
                    for (int k = 0; k < rowViewBys.size(); k++) {
                        String rowViewById = rowViewBys.get(k);
                        if ("TIME".equalsIgnoreCase(rowViewById)) {
                            tableBuffer.append("<th><strong>").append("Time").append("</strong></th> ");
                            elementIdSet.add("TIME");
                        } else {
                            if (container.getParametersHashMap().get("ParameterNames") != null) {
//                            ArrayList paramName = (ArrayList) container.getParametersHashMap().get("ParameterNames");
//                            tableBuffer.append("<th>").append(paramName.get(0)).append("</th> ");
                                if (container.getFixedparamhashmap() != null) {
                                    String paramName = container.getFixedparamhashmap().get(rowViewById).toString();
                                    tableBuffer.append("<th><strong>").append(paramName).append("</strong></th> ");
                                }
                                elementIdSet.add("A_" + rowViewById);
                            } else {
                                String paramName = collect.getParameterDispName(rowViewById);
                                tableBuffer.append("<th><strong>").append(paramName).append("</strong></th> ");
                                elementIdSet.add("A_" + rowViewById);
                            }
                        }
                        colTypes.add("C");
                    }
                }
                ArrayList<String> tempMeasure = new ArrayList<String>();

                for (QueryDetail qd : queryDetails) {
                    if (elementIdSet.add("A_" + qd.getElementId())) {
                        tableBuffer.append("<th><strong>").append(qd.getDisplayName()).append("</strong></th> ");
                        String colType = qd.getColumnType();
                        if ("NUMBER".equalsIgnoreCase(colType) || "CALCULATED".equalsIgnoreCase(colType)) {
                            tempMeasure.add("A_" + qd.getElementId());
                            colTypes.add("N");
                        } else {
                            colTypes.add("C");
                        }
                    }
                }

                tableBuffer.append("</tr>");
                tableBuffer.append("</thead>");
                tableBuffer.append("<tfoot>");
                tableBuffer.append("</tfoot>");
                tableBuffer.append("<tbody>");
                Set<Integer> tempSeq = new HashSet<Integer>();
                List<Integer> tempSeqlist = new ArrayList<Integer>();
                for (String str : elementIdSet) {
                    elementIds.add(str);
                }
                if (dashletPropertiesHelper != null) {
                    sortType = dashletPropertiesHelper.getDisplayOrder();
                    countVal = dashletPropertiesHelper.getCountForDisplay();
                    if (dashletPropertiesHelper.isSortAll()) {
                        graphDetails.setDisplayRows(String.valueOf(retObj.getRowCount()));
                    } else {
                        graphDetails.setDisplayRows(String.valueOf(dashletPropertiesHelper.getCountForDisplay()));
                    }
                } else {
                    if (graphDetails.getDisplayRows() != null && !graphDetails.getDisplayRows().equalsIgnoreCase("All")) {
                        countVal = Integer.parseInt(graphDetails.getDisplayRows());
                    } else {
                        countVal = retObj.getRowCount();
                    }
                }
                if (!tempMeasure.isEmpty()) {
                    //Modified by Veena for sorting and top/bottom issue
                    if (dashletPropertiesHelper != null) {
                        String type;
                        type = container.getDataTypes().get(container.getDisplayColumns().indexOf(dashletPropertiesHelper.getElement_IDforSort())).toString();
                        String elementId = (String) container.getDisplayColumns().get(0);
                        char[] sortTypes1 = new char[1];
                        char[] sortDataTypes1 = new char[1];
                        ArrayList<String> columnNames = new ArrayList<String>();
                        sortTypes1[0] = 'A';
                        sortDataTypes1[0] = 'C';
                        columnNames.add(elementId);
                        ProgenDataSet dataSet = container.getRetObj();
                        ArrayList<Integer> viewSequence = dataSet.getViewSequence();
                        retObj.setViewSequence(viewSequence);
                        ArrayList<Integer> rowSequence;
                        rowSequence = retObj.getViewSequence();
                        ArrayList<String> ElementId = new ArrayList<String>();
                        char[] sortTypes = new char[1];
                        char[] sortDataTypes = new char[1];
                        Boolean sortflag = Boolean.parseBoolean(dashletPropertiesHelper.getSortFlag());
                        if (sortflag != null && sortflag == true) {
                            if (dashletPropertiesHelper.getTypeForSort() == 0) {
                                sortTypes[0] = '0';
                            } else {
                                sortTypes[0] = '1';
                            }
                            sortDataTypes[0] = type.charAt(0);
                            if (dashletPropertiesHelper.getElement_IDforSort() != null) {
                                ElementId.add(dashletPropertiesHelper.getElement_IDforSort());
                            } else {
                                ElementId.add(dashletPropertiesHelper.getSortOnMeasure());
                            }

                            rowSequence = retObj.sortDataSet(ElementId, sortTypes, sortDataTypes);//dataTypes, container.getOriginalColumns());
                            retObj.setViewSequence(rowSequence);
                        }//end of sort
                        else if (dashletPropertiesHelper.isIsFromTopBottom()) {
                            dataSet = container.getRetObj();
                            dataSet.resetViewSequence();
                            viewSequence = dataSet.getViewSequence();
                            retObj.setViewSequence(viewSequence);
                            if (dashletPropertiesHelper.getElement_IDforSort() != null) {
                                ElementId.add(dashletPropertiesHelper.getElement_IDforSort());
                            } else {
                                ElementId.add(dashletPropertiesHelper.getSortOnMeasure());
                            }
                            if (dashletPropertiesHelper.isSortAll()) {
                                countVal = retObj.getRowCount();
                            } else {
                                countVal = dashletPropertiesHelper.getCountForDisplay();
                            }
                            String topbottom = dashletPropertiesHelper.getDisplayOrder();
                            if (!topbottom.equalsIgnoreCase("null") && topbottom.equalsIgnoreCase("top")) {
                                sortTypes[0] = '1';
                            } else {
                                sortTypes[0] = '0';
                            }
                            sortDataTypes[0] = type.charAt(0);
                            rowSequence = retObj.findTopBottom(ElementId, sortTypes, countVal);
                            retObj.setViewSequence(rowSequence);
                        }//end of top/bottom
                        tempSeqlist = new ArrayList<Integer>(rowSequence);
//                    dashletPropertiesHelper.setViewSequence(rowSequence);
                        if (countVal == 0) {
                            countVal = retObj.getRowCount();
                        }
//                    }
                    } else {
                        if (!graphDetails.isTimeSeries() && !("TIME".equalsIgnoreCase(rowViewBys.get(0)))) {
                            retObj = retObj.sort(1, tempMeasure.get(0), "N");
                        }
                    }
                }
                //Modifications over
                //old code written by keerthu for sorting and top/bottom
//                    if (sortType.equalsIgnoreCase("top")  && !sortFlag) {
////                    if(container.getDataTypes().get(container.getDisplayColumns().indexOf(dashletPropertiesHelper.getSortOnMeasure())).toString().equalsIgnoreCase("N"))
////                    retObj = retObj.sort(1, dashletPropertiesHelper.getSortOnMeasure(), type);
//                         sortTypes[0]='1';
//                         if(ElementId!=null)
//                    rowSequence = retObj.sortDataSet(ElementId, sortTypes,sortDataTypes);
//                    retObj.setViewSequence(rowSequence);
//                } else {
//                          sortTypes[0]='0';
//                    rowSequence = retObj.sortDataSet(ElementId, sortTypes,sortDataTypes);
//                    retObj.setViewSequence(rowSequence);
////                    retObj = retObj.sort(0, dashletPropertiesHelper.getSortOnMeasure(), type);
////                    container.setSortRetObj(retObj);
//                }
//                   tempSeqlist = new ArrayList<Integer>(rowSequence);

//                    if(dashletPropertiesHelper.getElement_IDforSort()!=null && dashletPropertiesHelper.getTypeForSort()!=-1 && !dashletPropertiesHelper.isIsFromTopBottom() ){
////                        retObj=retObj.sort(dashletPropertiesHelper.getTypeForSort(), dashletPropertiesHelper.getElement_IDforSort(), type);
//
//                        if(dashletPropertiesHelper.getCountForDisplay()!=0){
//                        for(int i=0;i<dashletPropertiesHelper.getCountForDisplay();i++){
//                        tempSeq.add(rowSequence.get(i));
//            }
//                        }
//                        
//                        rowSequence=retObj.sortDataSet(ElementId,sortTypes,sortDataTypes);
//                        retObj.setViewSequence(rowSequence);
//                        Set<Integer>localSeqSet=new HashSet<Integer>();
//                        for(int j=0;j<rowSequence.size();j++){
//                          if(tempSeq.contains(rowSequence.get(j))){
//                          localSeqSet.add(rowSequence.get(j));
//                          if(dashletPropertiesHelper.getCountForDisplay()==localSeqSet.size())
//                              break;
//                          }
//                        }
//                        localSeqSet.addAll(rowSequence);
//                          tempSeq.addAll(rowSequence);
//                          tempSeqlist = new ArrayList<Integer>(localSeqSet);
                //end of old code for sorting and top/bottom
                if (dashletPropertiesHelper != null && dashletPropertiesHelper.isSortAll()) {
                    countVal = retObj.getRowCount();
                }
                int count = countVal;
//            if(fromDesigner)

                if (countVal > retObj.getRowCount()) {
                    count = retObj.getRowCount();
                }

                for (int i = 0; i < count; i++) {//loop for building the values of top/bottom and sort
                    tableBuffer.append("<tr>");
                    for (int j = 0; j < elementIds.size(); j++) {
                        String colType = colTypes.get(j);
                        if ("C".equalsIgnoreCase(colType)) {
                            if (!tempSeqlist.isEmpty()) {
                                tableBuffer.append("<td align=\"left\">").append(retObj.getFieldValueString(tempSeqlist.get(i), elementIds.get(j))).append("</td>");
                            } else {
                                tableBuffer.append("<td align=\"left\">").append(retObj.getFieldValueString(i, elementIds.get(j))).append("</td>");
                            }
                        } else {
                            BigDecimal bd;
                            if (!tempSeqlist.isEmpty()) {
                                bd = retObj.getFieldValueBigDecimal(tempSeqlist.get(i), elementIds.get(j));
                            } else {
                                bd = retObj.getFieldValueBigDecimal(i, elementIds.get(j));
                            }
                            double value = bd.doubleValue();
                            if (!dashlet.getDashbrdTableColor().isEmpty() && !dashlet.getDashbrdTableColor().get(0).getColorCondOper().isEmpty()) {
                                List<DashboardTableColorGroupHelper> colorGroupHelpers = dashlet.getDashbrdTableColor();
                                DashboardTableColorGroupHelper colorGroupHelper = null;
                                for (DashboardTableColorGroupHelper helper : colorGroupHelpers) {
                                    if (helper.getElementId().equalsIgnoreCase(elementIds.get(j).replace("A_", ""))) {
                                        colorGroupHelper = helper;
                                    }
                                }

                                if (colorGroupHelper != null) {
                                    tableBuffer.append(buildTableTD(colorGroupHelper, elementIds.get(j), value));
                                } else {
                                    tableBuffer.append("<td align=\"right\">").append(NumberFormatter.getModifiedNumber(bd, "", -1)).append("</td>");
                                }
                            } else {
                                tableBuffer.append("<td align=\"right\">").append(NumberFormatter.getModifiedNumber(bd, "", -1)).append("</td>");
                            }
                        }

                    }
                    tableBuffer.append("</tr>");
                }//values buildee for top/bottm and sort

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
            tableBuffer.append(" </div>");
            return tableBuffer.toString();
        } else {
            return this.buildtableTranspose(container, dashletId, flag);
        }
    }

    /**
     * @return the editDashboard
     */
    public String getEditDashboard() {
        return editDashboard;
    }

    /**
     * @param editDashboard the editDashboard to set
     */
    public void setEditDashboard(String editDashboard) {
        this.editDashboard = editDashboard;
    }
//    public void runKpiScheduler(String elementId,String kpiMasterId,String dashletId,String reportId){
//        DashboardTemplateDAO dao = new DashboardTemplateDAO();
//        ReportSchedulerJob job = new ReportSchedulerJob();
//        ReportSchedule schedule=null;
////        schedule=dao.getKpiSchedulerDetails(String elementId,String kpiMasterId,String dashletId,String reportId);
//        job.sendSchedulerMail(schedule);
//    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public void setCountVal(int countVal) {
        this.countVal = countVal;
    }

    public void saveDashletProperties(String dashletId, String sortType, String countVal, String sortOnColumn, String checkval) {
        DashboardViewerDAO viewerDao = new DashboardViewerDAO();
        viewerDao.saveDashletProperties(dashletId, sortType, countVal, sortOnColumn, checkval);

    }

    public String buildtableTranspose(Container container, String dashletId, Boolean flag) {
        DashboardViewerDAO viewerDao = new DashboardViewerDAO();
        DashletPropertiesHelper dashletPropertiesHelper = null;
        if (container.getDashletPropertiesHelper() != null) {
            dashletPropertiesHelper = viewerDao.getDashletPropertiesHelperObject(dashletId);
        } else {
            dashletPropertiesHelper = container.getDashletPropertiesHelper();
        }
        if (dashletPropertiesHelper == null) {
            dashletPropertiesHelper = new DashletPropertiesHelper();
        }

        dashletPropertiesHelper.setIsTranspose(true);
        container.setDashletPropertiesHelper(dashletPropertiesHelper);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail dashlet = collect.getDashletDetail(dashletId);
        if (dashletPropertiesHelper.isIsTranspose() == true && dashletPropertiesHelper != null) {
            dashlet.setDashletPropertiesHelper(dashletPropertiesHelper);
        }
        GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
        int colSpan = dashlet.getColSpan();
        int rowSpan = dashlet.getRowSpan();
        int rowNum = dashlet.getRow();
        int colNum = dashlet.getCol();
        int height = 350;
        height = height * rowSpan;
        // int width = 550;
        //width = width * colSpan;
        List<DashletDetail> dashletDetails = collect.dashletDetails;
        ArrayListMultimap<Integer, Integer> rowinfo = ArrayListMultimap.create();
        for (int i = 0; i < dashletDetails.size(); i++) {
            DashletDetail detail = dashletDetails.get(i);
            rowinfo.put(detail.getRow(), detail.getCol());
        }
        List<Integer> dashlets;

        int rowIndex = -1;
        int colsnum = 0;
        for (int count = 0; count < rowinfo.keySet().size(); count++) {
            rowIndex++;
            dashlets = rowinfo.get(rowIndex);
            int numofdashlets = dashlets.size();
            if (rowIndex == rowNum) {
                colsnum = numofdashlets;
            }
        }

        int width = 1080;
        if (colsnum == 1) {
            width = 1080;
        } else if (colsnum == 2) {
            width = 540;

        } else if (colsnum == 3) {
            width = 360;

        } else if (colsnum == 4) {
            width = 250;

        }
        PbReturnObject retObj = (PbReturnObject) container.getRetObj();
        Set<String> elementIdSet = new LinkedHashSet<String>();
        if (graphDetails.isTimeSeries()) {
            retObj = container.getTimeSeriesRetObj();
        } else {
            retObj = (PbReturnObject) container.getRetObj();
        }
        StringBuilder tableBuffer = new StringBuilder();

        if (retObj != null) {
            ArrayList<String> rowViewBys = collect.reportRowViewbyValues;
            String dashletName = dashlet.getDashletName();
            String dashBoardId = container.getReportId();
            dashBoardId = container.getDashboardId();
            String refRepId = dashlet.getRefReportId();
            String paramName1 = container.getParamSectionDisplay();
            if (dashBoardId == null) {
                dashBoardId = collect.reportId;
            }
            String kpiMasterId = dashlet.getKpiMasterId();
            graphDetails.setShowAsTable(true);
            List<QueryDetail> queryDetails = graphDetails.getQueryDetails();

            String colTypes;
            int dashletNameLength = dashletName.length();
            String newDashletName = "";
            GraphBuilder gb = new GraphBuilder();
            int maxCharLength = gb.getGraphNameMaxCharacters(collect, dashletId);
            if (dashletNameLength > maxCharLength) {
                newDashletName = dashletName.substring(0, maxCharLength);
                newDashletName += "...";
            } else {
                if (dashlet.getkpiName().equalsIgnoreCase("KPI REGION")) {
                    newDashletName = "Table Region";
                } else {
                    newDashletName = dashlet.getkpiName();
                }

            }
            //Creating the dashlet header
            tableBuffer.append("<div id=\"DashletHeader-" + dashletId + "\" class=\"portlet-header1 navtitle portletHeader ui-corner-all\"  align=\"center\">");
            tableBuffer.append("<Table width=\"100%\">");
            tableBuffer.append("<tr>");
            tableBuffer.append("<td align=\"left\" width=\"65%\"><a  title=\"Dashlet Rename\" onclick=\"TableRename('" + refRepId + "','" + newDashletName + "')\"><strong>").append(newDashletName).append("</a> </strong></td>");
            tableBuffer.append("<td align=\"left\"><a href=\"javascript:void(0)\"  onclick=\"tableTranspose('" + dashletId + "')\"   style=\"text-decoration:none\"  title=\"Transpose\"><font size=\"1px\"><b>Transpose</b></font></a></td>");
            tableBuffer.append("<td align=\"left\"><a href=\"javascript:void(0)\" class=\"ui-icon ui-icon-refresh\" onclick=\"resetTableData('" + dashletId + "','" + flag + "')\"   style=\"text-decoration:none\"  title=\"reset\"><font size=\"1px\"><b></b></font></a></td>");
            tableBuffer.append("</tr></table>");
            tableBuffer.append("</div>");

            //Creating the dashlet Table
            //tableBuffer.append("<div id='zoom" +dashletId+ "'style=\"width:"+width+"px; height:" + height + "px;overflow-y:auto;overflow-x:auto\">");
            tableBuffer.append("<div id='zoom" + dashletId + "'style=\"width:" + width + "px; height:" + height + "px;overflow-y:auto;overflow-x:auto\">");
            tableBuffer.append("<table cellspacing=\"1\" class=\"tablesorter\"  id=\"tablesorter\" >");
            tableBuffer.append("<thead>");
            tableBuffer.append("<tbody>");
            String tempMeasure = "";
            if (graphDetails.isTimeSeries() || rowViewBys.get(0).equalsIgnoreCase("TIME")) {
                tableBuffer.append("<th>Time</th> ");
                colTypes = "C";
                elementIdSet.add("TIME");
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    tableBuffer.append("<td align=\"right\">").append(retObj.getFieldValueString(i, 0)).append("</td>");
                }

            } else {
                for (int k = 0; k < rowViewBys.size(); k++) {
                    String rowViewById = rowViewBys.get(k);

                    String paramName = collect.getParameterDispName(rowViewById);

                    // tableBuffer.append("<tr>").append("<th style=\"background-color:#E6E6E6\">").append(paramName).append("</th>");
                    tableBuffer.append("<tr>").append("<th>").append(paramName).append("</th>");
                    String eleIdSet = "A_" + rowViewById;
                    colTypes = "C";
                    tableBuffer.append(particaltableTranspose(container, eleIdSet, dashletPropertiesHelper, retObj, colTypes, tempMeasure, dashlet));
                    tableBuffer.append("</tr>");
                }
            }

            String eleIdSet = "";

            for (QueryDetail qd : queryDetails) {
                if (elementIdSet.add("A_" + qd.getElementId())) {
                    tableBuffer.append("<tr>").append("<th>").append(qd.getDisplayName());
                    String colType = qd.getColumnType();
                    if ("NUMBER".equalsIgnoreCase(colType) || "CALCULATED".equalsIgnoreCase(colType)) {
                        eleIdSet = "A_" + qd.getElementId();
                        colTypes = "N";
                        tempMeasure = "A_" + qd.getElementId();
                        tableBuffer.append(particaltableTranspose(container, eleIdSet, dashletPropertiesHelper, retObj, colTypes, tempMeasure, dashlet));

                    } else {
                        eleIdSet = "A_" + qd.getElementId();
                        colTypes = "C";
                        tableBuffer.append(particaltableTranspose(container, eleIdSet, dashletPropertiesHelper, retObj, colTypes, tempMeasure, dashlet));
                    }
                    tableBuffer.append("</th>").append("</tr>");
                    tableBuffer.append("</tbody>");

                }
            }


            tableBuffer.append("</thead>");
            tableBuffer.append("<tfoot>");
            tableBuffer.append("</tfoot>");


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

        tableBuffer.append(" </div>");

        return tableBuffer.toString();


    }

    public String particaltableTranspose(Container container, String eleIdSet, DashletPropertiesHelper dashletPropertiesHelper, PbReturnObject retObj, String colTypes, String tempMeasure, DashletDetail dashlet) {
        StringBuilder tableBuffer = new StringBuilder();
        int count = retObj.getRowCount();

        for (int i = 0; i < count; i++) {
            if ("C".equalsIgnoreCase(colTypes)) {
                tableBuffer.append("<td>").append(retObj.getFieldValueString(i, eleIdSet)).append("</td>");
            } else {
                BigDecimal bd = retObj.getFieldValueBigDecimal(i, eleIdSet);
                // tableBuffer.append("<td>").append(NumberFormatter.getModifiedNumber(bd, "", -1)).append("</td>");
                double value = bd.doubleValue();
                if (!dashlet.getDashbrdTableColor().isEmpty() && !dashlet.getDashbrdTableColor().get(0).getColorCondOper().isEmpty()) {
                    List<DashboardTableColorGroupHelper> colorGroupHelpers = dashlet.getDashbrdTableColor();
                    DashboardTableColorGroupHelper colorGroupHelper = null;
                    for (DashboardTableColorGroupHelper helper : colorGroupHelpers) {
                        if (helper.getElementId().equalsIgnoreCase(eleIdSet.replace("A_", ""))) {
                            colorGroupHelper = helper;
                        }
                    }

                    if (colorGroupHelper != null) {
                        tableBuffer.append(buildTableTD(colorGroupHelper, eleIdSet, value));
                    } else {
                        tableBuffer.append("<td align=\"right\">").append(NumberFormatter.getModifiedNumber(bd, "", -1)).append("</td>");
                    }
                } else {
                    tableBuffer.append("<td align=\"right\">").append(NumberFormatter.getModifiedNumber(bd, "", -1)).append("</td>");
                }

            }
        }

        return tableBuffer.toString();
    }

    public String buildTableTD(DashboardTableColorGroupHelper colorGroupHelper, String elementId, double modifiedNumber) {
        List<String> condStartValue = colorGroupHelper.getColorCondOper();
        List<String> startValues = colorGroupHelper.getCondStartValue();
        List<String> endValues = colorGroupHelper.getCondEndValue();
        List<String> colorValues = colorGroupHelper.getColorVal();
        StringBuilder tableBuffer = new StringBuilder();
        boolean conditionStatus = false;
        int condtionIndex = -1;
        for (int i = 0; i < condStartValue.size(); i++) {
            String operator = condStartValue.get(i);
            if (operator.equalsIgnoreCase("<")) {
//                if (modifiedNumber < Double.parseDouble(startValues.get(i).trim())) {
                if (modifiedNumber < Double.parseDouble(startValues.get(i).trim())) {
                    conditionStatus = true;
                    condtionIndex = i;
                }

            } else if (operator.equalsIgnoreCase(">")) {
                if (modifiedNumber > Double.parseDouble(startValues.get(i).trim())) {
                    conditionStatus = true;
                    condtionIndex = i;
                }
            } else if (operator.equalsIgnoreCase("<=")) {
                if (modifiedNumber <= Double.parseDouble(startValues.get(i).trim())) {
                    conditionStatus = true;
                    condtionIndex = i;
                }
            } else if (operator.equalsIgnoreCase(">=")) {
                if (modifiedNumber >= Double.parseDouble(startValues.get(i).trim())) {
                    conditionStatus = true;
                    condtionIndex = i;
                }
            } else if (operator.equalsIgnoreCase("=")) {
                if (modifiedNumber == Double.parseDouble(startValues.get(i).trim())) {
                    conditionStatus = true;
                    condtionIndex = i;
                }
            } else if (operator.equalsIgnoreCase("!=")) {
                if (modifiedNumber != Double.parseDouble(startValues.get(i).trim())) {
                    conditionStatus = true;
                    condtionIndex = i;
                }
            } else if (operator.equalsIgnoreCase("<>")) {
                if (modifiedNumber > Double.parseDouble(startValues.get(i).trim()) && modifiedNumber < Double.parseDouble(endValues.get(i).trim())) {
                    conditionStatus = true;
                    condtionIndex = i;
                }

            }
        }

        if (conditionStatus) {
            tableBuffer.append("<td align=\"right\" style=\"background-color:" + colorValues.get(condtionIndex) + " \">").append(NumberFormatter.getModifiedNumber(new BigDecimal(modifiedNumber), "", -1)).append("</td>");
        } else {
            tableBuffer.append("<td align=\"right\" >").append(NumberFormatter.getModifiedNumber(new BigDecimal(modifiedNumber), "", -1)).append("</td>");
        }

        return tableBuffer.toString();

    }

    public String builDbGroupMeassure(Container container, GroupMeassureParams meassureParams) {
        DashboardViewerDAO viewerDao = new DashboardViewerDAO();
        String pattern = "\\d\\.?";
        String letterPattern = "\\D-\\.";
        Pattern p = Pattern.compile(pattern);
        Pattern p1 = Pattern.compile(letterPattern);
        String groupId = meassureParams.getGroupId();
        String dashletId = meassureParams.getDahletId();
        StringBuilder output = new StringBuilder();
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail dashlet = collect.getDashletDetail(dashletId);
        GraphReport graphDetails = new GraphReport();
//        int colSpan = dashlet.getColSpan();
        int rowSpan = dashlet.getRowSpan();
//        int rowNum = dashlet.getRow();
//        int colNum = dashlet.getCol();
        int height = 350;
        height = height * rowSpan;
        String rowsToDisp = meassureParams.getDisplayType();
        int fieldsToshow;
        int toSelect = 0;
        int viewSelect = 0;
        String resultdata = "";
        String align = "left";
        ArrayList rootElementList = new ArrayList();
        String groupDisplayType = meassureParams.getGroupDisplayType();
        List<KPIElement> kpiElements = meassureParams.getkPIElements();
        ArrayList<HashMap<String, ArrayList>> groupMapList = meassureParams.getGroupMap();
        List<QueryDetail> queryDetails = null;
        String[] folderIds = collect.reportBizRoles;
        String dashBoardId = container.getReportId();
        HashMap<String, String[]> repDrilMap = new HashMap<String, String[]>();
        String[] repDetails = new String[2];

//        if(groupDisplayType.equalsIgnoreCase("")||groupDisplayType.equalsIgnoreCase("Simple"))
//        {
        graphDetails = (GraphReport) dashlet.getReportDetails();
        queryDetails = graphDetails.getQueryDetails();
        for (HashMap map : groupMapList) {
            rootElementList = (ArrayList) map.get(groupId);
        }
//        }

        if (groupDisplayType.equalsIgnoreCase("") || groupDisplayType.equalsIgnoreCase("Simple")) {
            for (QueryDetail qd : queryDetails) {
                rootElementList.add(qd.getElementId());
            }


        }
        repDrilMap = viewerDao.getAssignedReports(rootElementList, rootElementList, groupId, dashBoardId, dashletId);
        if (groupDisplayType.equalsIgnoreCase("SWP")) {
            viewSelect = 1;
        }
//        List<DashletDetail> dashletDetails = collect.dashletDetails;
        PbReturnObject retObj = (PbReturnObject) container.getRetObj();
//        Set<String> elementIdSet = new LinkedHashSet<String>();
        if (graphDetails.isTimeSeries()) {
            retObj = container.getTimeSeriesRetObj();

        } else {
            retObj = (PbReturnObject) container.getRetObj();
        }
//        retObj.setRowCount(rowsToDisp);
        if (rowsToDisp == null || rowsToDisp.equalsIgnoreCase("null")) {
            rowsToDisp = "5";
        }
        if (rowsToDisp.equalsIgnoreCase("all")) {
            fieldsToshow = retObj.getRowCount();

        } else {
            int temprows = Integer.parseInt(rowsToDisp);
            if (temprows <= retObj.getRowCount()) {
                fieldsToshow = Integer.parseInt(rowsToDisp);
            } else {
                fieldsToshow = retObj.getRowCount();
            }
            toSelect = Integer.parseInt(rowsToDisp);
        }
        String is5Selected = "";
        String is10Selected = "";
        String is15Selected = "";
        String isAllSelected = "";
        String isSimple = "";
        String isSWP = "";
        switch (toSelect) {
            case 5:
                is5Selected = "selected";
                break;
            case 10:
                is10Selected = "selected";
                break;
            case 15:
                is15Selected = "selected";
                break;
            default:
                isAllSelected = "selected";
                break;
        }
        switch (viewSelect) {
            case 1:
                isSWP = "checked";
                break;
            default:
                isSimple = "checked";
                break;

        }
        if (retObj != null) {
            ArrayList<String> rowViewBys = collect.reportRowViewbyValues;
//            String dashletName = dashlet.getDashletName();

            dashBoardId = container.getDashboardId();
//            String refRepId = dashlet.getRefReportId();
//            String paramName1 = container.getParamSectionDisplay();
            if (dashBoardId == null) {
                dashBoardId = collect.reportId;
            }
//            String kpiMasterId = dashlet.getKpiMasterId();
//            graphDetails.setShowAsTable(true);




            HashMap map = container.getParametersHashMap();
//            HashMap paramMap = new HashMap();
//            ArrayList paramIds = (ArrayList) map.get("Parameters");
//            ArrayList paramNames = (ArrayList) map.get("ParameterNames");

//             for(int i=0;i<paramIds.size();i++)
//             {
//                 paramMap.put(paramIds.get(i),paramNames.get(i));
//             }


            output.append("<div id=\"DashletHeader-" + dashletId + "\" class=\"portlet-header1 navtitle portletHeader ui-corner-all\"  align=\"center\">");
            output.append("<table width=\"100%\"><tbody><tr align=\"left\" width=\"100%\"><td valign=\"top\" width=\"50%\"><table align=\"left\"><tbody><tr><td valign=\"top\"><strong> GroupMeasureInsightView</strong></td></tr></tbody></table></td>");
            output.append("<td width=\"50%\"><table align=\"right\"><tbody><tr>");
            output.append("<td align=\"left\" width=\"25%\"><a class=\" ui-icon ui-icon-gear\" onclick=\"drillToReportForGroup('" + dashBoardId + "','" + dashletId + "','" + groupId + "','" + rootElementList + "','" + folderIds[0] + "')\" title=\"Drill To Report\"></a></td>");
            output.append("<td align=\"left\" width=\"50%\"><a class=\"ui-icon ui-icon-pencil\" onclick=\"ViewGroupInMultiple('GroupProperties-" + dashletId + "')\" title=\"Properties\"></a>");
            output.append(" <div id='GroupProperties-" + dashletId + "' style='display: none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;''  ><table align='center'><tbody><tr>");
            output.append("<td><input  type='radio' name='groupView-" + dashletId + "' value='Simple' align='left' " + isSimple + "/>&nbsp;&nbsp;&nbsp;Simple</td></tr>");
            output.append("<tr><td><input  type='radio' name='groupView-" + dashletId + "' value='SWP' align='left' " + isSWP + "/>&nbsp;&nbsp;&nbsp;Simple With  Prior</td></tr>");
            output.append("</tbody><tfoot ><tr><td align=\"center\"><input  type=\"button\" name=\"save\" value=\"save\" class=\"navtitle-hover\" onclick=\"loadGroupMeassure('" + dashBoardId + "','" + dashletId + "','" + groupId + "','" + rootElementList + "')\"/></td></tr>");
            output.append("</tfoot></table></div></body>");
            output.append("</td><td/>");
            output.append("<td valign=\"right\" width=\"25%\"><strong>View:</strong><select id=\"viewType\" onchange=\"loadGroupMeassure('" + dashBoardId + "','" + dashletId + "','" + groupId + "','" + rootElementList + "')\" class=\"navtitle-hover\" align=\"left\">");
            output.append("<option value=\"5\" " + is5Selected + " >5</option><option value=\"10\" " + is10Selected + " >10</option><option value=\"15\" " + is15Selected + " >15</option><option value=\"all\"  " + isAllSelected + " >all</option></select></td>");
            output.append("</tr></tbody></table></td></tr></tbody></table>");
            output.append("</div>");
            output.append("<div  id='Group" + dashletId + "'  style=\"width:100%;height:" + height + "px;overflow-y:auto;overflow-x:auto\"  align=\"center\">");
            output.append("<table border=\"1\" id=\"tablesorter\"  class=\"tablesorter\" style=\"border-collapse:collapse\">");
            output.append("<thead>");
            output.append("<tr>");


            for (int k = 0; k < rowViewBys.size(); k++) {
                String rowViewById = "A_" + rowViewBys.get(k);
                if (graphDetails.isTimeSeries()) {
                    rowViewById = "TIME";
                }

                if (groupDisplayType.equalsIgnoreCase("SWP")) {
                    output.append("<th rowspan=\"2\"/><th align='center' rowspan=\"2\">KPI Name</th>");
                    for (int temp = 0; temp < fieldsToshow; temp++) {
                        output.append("<th align=\"center\"  colspan=\"2\"> ").append(retObj.getFieldValueString(temp, rowViewById)).append("</th>");
                    }
                    output.append("</tr><tr></th></th></th>");
                    for (int temp = 0; temp < fieldsToshow; temp++) {
                        output.append("<th align='center'>Current</th><th align='center'>Prior</th>");
                    }
                    output.append("</tr>");

                } else {
//                    String paramName =paramMap.get(rowViewById).toString();

                    output.append("<th/><th align='center'>KPI Name</th>");

                    for (int i = 0; i < fieldsToshow; i++) //retObj.rowCount
                    {
//                        if(retObj.getFieldValueString(i,"A_" + rowViewById) != "")
                        output.append("<th align=\"center\"> ").append(retObj.getFieldValueString(i, rowViewById)).append("</th>");
                    }
                }
            }

            output.append("</thead><tbody>");

//            String eleIdSet = "";
            String divId = "";
            String childDivId = "";
            String id = "";
            HashMap<String, HashMap<String, String>> elementsMap = new HashMap<String, HashMap<String, String>>();


            if (groupDisplayType.equalsIgnoreCase("SWP")) {
                for (int i = 0; i < rootElementList.size(); i++) {

//                         ArrayList<HashMap<String,String>> childMapList=new ArrayList<HashMap<String,String>>();
                    HashMap<String, String> childMap = new HashMap<String, String>();
                    for (KPIElement kpiElem : kpiElements) {

                        if (kpiElem.getRefElementId().equals(rootElementList.get(i)) && !kpiElem.getElementId().equals(rootElementList.get(i))) {
                            childMap.put(kpiElem.getRefElementType(), kpiElem.getElementId());


                        }
                    }

                    elementsMap.put(String.valueOf(rootElementList.get(i)), childMap);
                }
            }
            for (QueryDetail qd : queryDetails) {
                id = (String) qd.getElementId();
                divId = id + "_" + dashletId + "Div";
                childDivId = id + "_" + dashletId + "ChildDiv";


                if (groupDisplayType.equalsIgnoreCase("SWP")) {
                    if (rootElementList.contains(qd.getElementId())) {
                        output.append("<tr id='").append(divId).append("'>");
                        output.append("<td class=\"collapsible\" rowspan=\"2\" width=\"20px\">");
                        output.append("<a class=\"collapsed\" onclick=\"loadChildData('" + divId + "','" + id + "','" + childDivId + "','" + dashletId + "','" + groupId + "','" + dashBoardId + "','true')\"></a></td>");
                        repDetails = repDrilMap.get(id);
                        if (repDetails != null) {
                            if (repDetails[0] != "" && repDetails[0] != null && repDetails[1].equalsIgnoreCase("D")) {
                                output.append("<td align='left'>").append("<a style='decoration:none' href=\"javascript:submitDbrdUrlforGroup('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + repDetails[0] + "')\"><strong>" + qd.getDisplayName() + "</strong></A>").append("</td>");
                            } else if (repDetails[0] != "" && repDetails[0] != null && repDetails[1].equalsIgnoreCase("R")) {
                                output.append("<td align='left'>").append("<a style='decoration:none' href=\"javascript:submiturls1forGroup('reportViewer.do?reportBy=viewReport&REPORTID=" + repDetails[0] + "&drillViewCheck=true')\"><strong>" + qd.getDisplayName() + "</strong></A>").append("</td>");
                            } else {
                                output.append("<td align='left'>").append(qd.getDisplayName()).append("</td>");
                            }
                        } else {
                            output.append("<td align='left'>").append(qd.getDisplayName()).append("</td>");

                        }

                        for (KPIElement kpiElem : kpiElements) {
                            if (rootElementList.contains(kpiElem.getElementId()) && kpiElem.getElementId().equals(qd.getElementId())) {

                                String priorVal = "";
                                String currVal = "";

                                if (!((HashMap) elementsMap.get(id)).isEmpty()) {
                                    HashMap<String, String> childMap = elementsMap.get(id);
                                    if (childMap.containsKey("2")) {
                                        for (int i = 0; i < fieldsToshow; i++) //retObj.rowCount fieldsToshow
                                        {
                                            String priorElementId = childMap.get("2");
                                            String currentElementId = id;
                                            priorVal = NumberFormatter.getModifiedNumber(Double.parseDouble(retObj.getFieldValueString(i, "A_" + priorElementId)), "", -1);
                                            currVal = NumberFormatter.getModifiedNumber(Double.parseDouble(retObj.getFieldValueString(i, "A_" + currentElementId)), "", -1);
                                            output.append("<td align='center'>").append(currVal).append("</td>");
                                            output.append("<td align='center'>").append(priorVal).append("</td>");
                                        }
                                        output.append("</tr>");
                                        output.append("<tr class=\"expand-child\"><td style=\"display: none;\" colspan='").append(2 + (2 * fieldsToshow)).append("'>");
                                        output.append("<div id='").append(childDivId).append("'></div>");
                                        output.append("<div id='").append(childDivId).append("prgBar").append("'></div>");
                                        output.append("</td></tr>");
                                    }

                                } else {
                                    if (((HashMap) elementsMap.get(id)).isEmpty()) {
                                        for (int i = 0; i < fieldsToshow; i++) //retObj.rowCount fieldsToshow
                                        {
                                            resultdata = NumberFormatter.getModifiedNumber(Double.parseDouble(retObj.getFieldValueString(i, "A_" + kpiElem.getElementId())), "", -1);
                                            output.append("<td align='center' colspan=\"2\">").append(resultdata).append("</td>");
                                        }
                                        output.append("</tr>");
                                        output.append("<tr class=\"expand-child\"><td style=\"display: none;\" colspan='").append(2 + (2 * fieldsToshow)).append("'>");
                                        output.append("<div id='").append(childDivId).append("'></div>");
                                        output.append("<div id='").append(childDivId).append("prgBar").append("'></div>");
                                        output.append("</td></tr>");
                                    }
                                }



                            }
                        }

                    }
                } else {
                    output.append("<tr id='").append(divId).append("'>");
                    output.append("<td class=\"collapsible\" rowspan=\"2\" width=\"20px\">");
                    output.append("<a class=\"collapsed\" onclick=\"loadChildData('" + divId + "','" + id + "','" + childDivId + "','" + dashletId + "','" + groupId + "','" + dashBoardId + "','true')\"></a></td>");

                    repDetails = repDrilMap.get(id);
                    if (repDetails != null) {
                        if (repDetails[0] != "" && repDetails[0] != null && repDetails[1].equalsIgnoreCase("D")) {
                            output.append("<td align='left'>").append("<a style='decoration:none' href=\"javascript:submitDbrdUrlforGroup('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + repDetails[0] + "')\"><strong>" + qd.getDisplayName() + "</strong></A>").append("</td>");
                        } else if (repDetails[0] != "" && repDetails[0] != null && repDetails[1].equalsIgnoreCase("R")) {
                            output.append("<td align='left'>").append("<a style='decoration:none' href=\"javascript:submiturls1forGroup('reportViewer.do?reportBy=viewReport&REPORTID=" + repDetails[0] + "&drillViewCheck=true')\"><strong>" + qd.getDisplayName() + "</strong></A>").append("</td>");
                        } else {
                            output.append("<td align='left'>").append(qd.getDisplayName()).append("</td>");
                        }
                    } else {
                        output.append("<td align='left'>").append(qd.getDisplayName()).append("</td>");

                    }


//                        String name = (String) paramNames.get(j);

                    for (int i = 0; i < fieldsToshow; i++) //retObj.rowCount fieldsToshow
                    {
                        resultdata = retObj.getFieldValueString(i, "A_" + id);
                        Matcher match = p.matcher(resultdata);
                        Matcher match1 = p1.matcher(resultdata);
                        boolean b = match.find();
                        boolean b1 = match1.find();

                        if (b) {
                            if (!b1) {
                                resultdata = NumberFormatter.getModifiedNumber(Double.parseDouble(resultdata), "", -1);
                                align = "center";
                            }
                        }

                        output.append("<td align='" + align + "'>").append(resultdata).append("</td>");
                    }
                    output.append("</tr>");
                    output.append("<tr class=\"expand-child\"><td style=\"display: none;\" colspan='").append(2 + fieldsToshow).append("'>");
                    output.append("<div id='").append(childDivId).append("'></div>");
                    output.append("<div id='").append(childDivId).append("prgBar").append("'></div>");
                    output.append("</td></tr>");
                }
            }



            output.append("</tbody>");
            output.append("</table>");
            output.append("</div>");


        }

        return output.toString();
    }

    public String generateMeassureTable(Container container, GroupMeassureParams meassureParams) {
        DashboardViewerDAO viewerDao = new DashboardViewerDAO();
        String pattern = "\\d\\.?";
        String letterPattern = "\\D-\\.";
        Pattern p = Pattern.compile(pattern);
        Pattern p1 = Pattern.compile(letterPattern);
//        StringBuilder pmFunction = new StringBuilder();
//        StringBuilder drillHtml = new StringBuilder();
//        StringBuilder output = new StringBuilder();
//        boolean drillAvailable = false;
        StringBuilder childTable = new StringBuilder();
        String rowsToDisp = meassureParams.getDisplayType();

        int fieldsToshow = 5;
//        rowsToDisp = "10";

        String dashletId = meassureParams.getDahletId();
        String dbrdId = meassureParams.getDbrdId();
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail dashlet = collect.getDashletDetail(meassureParams.getDahletId());
        GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
//        List<DashletDetail> dashletDetails = collect.dashletDetails;
        List<QueryDetail> queryDetails = graphDetails.getQueryDetails();
        PbReturnObject retObj = (PbReturnObject) container.getRetObj();
        String oddStyle = "";
        String nextLevel = "odd";
        String resultdata = "";
        String align = "left";
        String groupDisplayType = meassureParams.getGroupDisplayType();
        List<KPIElement> kpiElements = meassureParams.getkPIElements();
        ArrayList<HashMap<String, ArrayList>> groupMapList = meassureParams.getGroupMap();
        ArrayList childElementList = new ArrayList();
        String groupId = meassureParams.getGroupId();
        String parentId = meassureParams.getMeassureId();
        HashMap<String, String[]> repDrilMap = new HashMap<String, String[]>();
        String[] repDetails = new String[2];
        ArrayList parentList = new ArrayList();
        parentList.add(parentId);



        if (meassureParams.isIsOddLevel()) {
            nextLevel = "even";
        }
        if (graphDetails.isTimeSeries()) {
            retObj = container.getTimeSeriesRetObj();
        } else {
            retObj = (PbReturnObject) container.getRetObj();
        }

        if (retObj != null) {
            ArrayList<String> rowViewBys = collect.reportRowViewbyValues;


            if (rowsToDisp.equalsIgnoreCase("All") || rowsToDisp.equalsIgnoreCase("")) {
                fieldsToshow = retObj.getRowCount();
            } else {
                int temprows = Integer.parseInt(rowsToDisp);
                if (temprows <= retObj.getRowCount()) {
                    fieldsToshow = Integer.parseInt(rowsToDisp);
                } else {
                    fieldsToshow = retObj.getRowCount();
                }
            }
            for (HashMap map : groupMapList) {
                childElementList = (ArrayList) map.get(groupId);
            }
            repDrilMap = viewerDao.getAssignedReports(childElementList, parentList, groupId, dbrdId, dashletId);
            HashMap<String, HashMap<String, String>> elementsMap = new HashMap<String, HashMap<String, String>>();
            if (groupDisplayType.equalsIgnoreCase("SWP")) {
                for (int i = 0; i < childElementList.size(); i++) {

//                         ArrayList<HashMap<String,String>> childMapList=new ArrayList<HashMap<String,String>>();
                    HashMap<String, String> childMap = new HashMap<String, String>();
                    for (KPIElement kpiElem : kpiElements) {

                        if (kpiElem.getRefElementId().equals(childElementList.get(i)) && !kpiElem.getElementId().equals(childElementList.get(i))) {
                            childMap.put(kpiElem.getRefElementType(), kpiElem.getElementId());
                        }
                    }

                    elementsMap.put(String.valueOf(childElementList.get(i)), childMap);
                }
            }
//        if (meassureParams.isIsOddLevel())
//            oddStyle = " style=\"background-color: gainsboro\" ";
            childTable.append("<table border=\"1\" id=\"tablesorter\"  class=\"tablesorter\" style=\"border-collapse:collapse;border-left-style: hidden;border-right-style: hidden;\">");
            childTable.append("<thead>");
            childTable.append("<tr>");
//        if(meassureParams.isIsOddLevel())
//               childTable.append("<th style=\"background-color: gainsboro\"/>");
//         else

            for (int k = 0; k < rowViewBys.size(); k++) {
                String rowViewById = "A_" + rowViewBys.get(k);
                if (graphDetails.isTimeSeries()) {
                    rowViewById = "TIME";
//                    fieldsToshow = retObj.rowCount;
                }
                if (groupDisplayType.equalsIgnoreCase("SWP")) {
                    childTable.append("<th rowspan=\"2\"/><th align='center' rowspan=\"2\">Meassure</th>");
                    for (int temp = 0; temp < fieldsToshow; temp++) {
                        childTable.append("<th align=\"center\"  colspan=\"2\"> ").append(retObj.getFieldValueString(temp, rowViewById)).append("</th>");
                    }
                    childTable.append("</tr><tr></th></th></th>");
                    for (int temp = 0; temp < fieldsToshow; temp++) {
                        childTable.append("<th align='center'>Current</th><th align='center'>Prior</th>");
                    }
                    childTable.append("</tr>");

                } else {
                    childTable.append("<th />");
                    childTable.append("<th align='center'" + oddStyle + ">Meassure</th>");

                    for (int i = 0; i < fieldsToshow; i++) //retObj.rowCount
                    {
//                        if(!retObj.getFieldValueString(i,"A_" + rowViewById).equalsIgnoreCase(""))
                        childTable.append("<th align=\"center\"" + oddStyle + ">").append(retObj.getFieldValueString(i, rowViewById)).append("</th>");
                    }
                }
            }
            childTable.append("</tr>");
            childTable.append("</thead>");
            childTable.append("<tbody>");
            for (QueryDetail qd : queryDetails) {
                String id = (String) qd.getElementId();
                String str = UUID.randomUUID().toString();
                String divId = str + "Div";
                String childDivId = str + "ChildDiv";
//                        childTable.append("<td style=\"border-left-style: hidden;\">" + qd.getDisplayName() + "</td>");
                if (groupDisplayType.equalsIgnoreCase("SWP")) {
                    if (childElementList.contains(qd.getElementId())) {
                        childTable.append("<tr id='").append(divId).append("'>");
                        childTable.append("<td class=\"collapsible\" rowspan=\"2\" width=\"20px\" style=\"border-bottom-style:hidden;\">");
                        childTable.append("<a class=\"collapsed\" onclick=\"loadChildDrillData('").append(divId).append("','").append(meassureParams.getDahletId()).append("','").append(meassureParams.getDbrdId()).append("','").append(id).append("'").append(",'").append(childDivId).append("'").append(",'").append(nextLevel).append("','").append(meassureParams.getGroupId()).append("',false);\"></a></td>");
                        repDetails = repDrilMap.get(id);
                        if (repDetails != null) {
                            if (repDetails[1].equalsIgnoreCase("D")) {
                                childTable.append("<td td style=\"border-left-style: hidden;\">").append("<a style='decoration:none' href=\"javascript:submitDbrdUrlforGroup('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + repDetails[0] + "')\"><strong>" + qd.getDisplayName() + "</strong></A>").append("</td>");
                            } else {
                                if (repDetails[1].equalsIgnoreCase("R")) {
                                    childTable.append("<td td style=\"border-left-style: hidden;\">").append("<a style='decoration:none' href=\"javascript:submiturls1forGroup('reportViewer.do?reportBy=viewReport&REPORTID=" + repDetails[0] + "&drillViewCheck=true')\"><strong>" + qd.getDisplayName() + "</strong></A>").append("</td>");
                                } else {
                                    childTable.append("<td td style=\"border-left-style: hidden;\">").append(qd.getDisplayName()).append("</td>");
                                }
                            }
                        } else {
                            childTable.append("<td td style=\"border-left-style: hidden;\">").append(qd.getDisplayName()).append("</td>");
                        }

                        for (KPIElement kpiElem : kpiElements) {
                            if (childElementList.contains(kpiElem.getElementId()) && kpiElem.getElementId().equals(qd.getElementId())) {
                                String priorVal = "";
                                String currVal = "";
                                if (!((HashMap) elementsMap.get(id)).isEmpty()) {
                                    HashMap<String, String> childMap = elementsMap.get(id);
                                    if (childMap.containsKey("2")) {
                                        for (int i = 0; i < fieldsToshow; i++) //retObj.rowCount fieldsToshow
                                        {
                                            String priorElementId = childMap.get("2");
                                            String currentElementId = id;
                                            priorVal = NumberFormatter.getModifiedNumber(Double.parseDouble(retObj.getFieldValueString(i, "A_" + priorElementId)), "", -1);
                                            currVal = NumberFormatter.getModifiedNumber(Double.parseDouble(retObj.getFieldValueString(i, "A_" + currentElementId)), "", -1);
                                            childTable.append("<td align='center' >").append(currVal).append("</td>");
                                            childTable.append("<td align='center' >").append(priorVal).append("</td>");
                                        }
                                    }
                                } else {
                                    if (((HashMap) elementsMap.get(id)).isEmpty()) {
                                        for (int i = 0; i < fieldsToshow; i++) //retObj.rowCount fieldsToshow
                                        {
                                            resultdata = NumberFormatter.getModifiedNumber(Double.parseDouble(retObj.getFieldValueString(i, "A_" + kpiElem.getElementId())), "", -1);
                                            childTable.append("<td align='center' colspan=\"2\">").append(resultdata).append("</td>");
                                        }
                                    }
                                }
                            }
                        }
                        childTable.append("</tr>");
                        childTable.append("<tr class=\"expand-child\"><td style=\"display: none;border-style: hidden;\" colspan='").append(3 + (2 * fieldsToshow)).append("'>");
                        childTable.append("<div id='").append(childDivId).append("prgBar").append("'></div>");
                        childTable.append("<div id='").append(childDivId).append("'></div>");
                        childTable.append("</td></tr>");
                    }
                } else {
                    childTable.append("<tr id='").append(divId).append("'>");
                    childTable.append("<td class=\"collapsible\" rowspan=\"2\" width=\"20px\" style=\"border-bottom-style:hidden;\">");
                    childTable.append("<a class=\"collapsed\" onclick=\"loadChildDrillData('").append(divId).append("','").append(meassureParams.getDahletId()).append("','").append(meassureParams.getDbrdId()).append("','").append(id).append("'").append(",'").append(childDivId).append("'").append(",'").append(nextLevel).append("','").append(meassureParams.getGroupId()).append("',false);\"></a></td>");
                    repDetails = repDrilMap.get(id);
                    if (repDetails != null) {
                        if (repDetails[1].equalsIgnoreCase("D")) {
                            childTable.append("<td td style=\"border-left-style: hidden;\">").append("<a style='decoration:none' href=\"javascript:submitDbrdUrlforGroup('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + repDetails[0] + "')\"><strong>" + qd.getDisplayName() + "</strong></A>").append("</td>");
                        } else {
                            if (repDetails[1].equalsIgnoreCase("R")) {
                                childTable.append("<td td style=\"border-left-style: hidden;\">").append("<a style='decoration:none' href=\"javascript:submiturls1forGroup('reportViewer.do?reportBy=viewReport&REPORTID=" + repDetails[0] + "&drillViewCheck=true')\"><strong>" + qd.getDisplayName() + "</strong></A>").append("</td>");
                            } else {
                                childTable.append("<td td style=\"border-left-style: hidden;\">").append(qd.getDisplayName()).append("</td>");
                            }
                        }
                    } else {
                        childTable.append("<td td style=\"border-left-style: hidden;\">").append(qd.getDisplayName()).append("</td>");
                    }
                    for (int i = 0; i < fieldsToshow; i++) {
                        resultdata = retObj.getFieldValueString(i, "A_" + id);
                        Matcher match = p.matcher(resultdata);
                        Matcher match1 = p1.matcher(resultdata);
                        boolean b = match.find();
                        boolean b1 = match1.find();

                        if (b) {
                            if (!b1) {
                                resultdata = NumberFormatter.getModifiedNumber(Double.parseDouble(resultdata), "", -1);
                                align = "center";
                            }
                        }
                        childTable.append("<td style=\"border-left-style: hidden;\" align='" + align + "'>").append(resultdata).append("</td>");
                    }

                    childTable.append("</tr>");
                    childTable.append("<tr class=\"expand-child\"><td style=\"display: none;border-style: hidden;\" colspan='").append(3 + fieldsToshow).append("'>");
                    childTable.append("<div id='").append(childDivId).append("prgBar").append("'></div>");
                    childTable.append("<div id='").append(childDivId).append("'></div>");
                    childTable.append("</td></tr>");
                }
            }
            childTable.append("</tbody>");
        }
        childTable.append("</table>");


        return childTable.toString();
    }

    public PbReturnObject getDashboardTextKPIData(Container container, pbDashboardCollection collect, String userId, DashletDetail details) {
        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject pbretObj = null;
        ArrayList<String> qryColIds = new ArrayList<String>();
        ArrayList<String> qryAggrs = new ArrayList<String>();
//        if(collect.getQryColIds()!=null && !collect.getQryColIds().isEmpty())
//        qryColIds = (ArrayList) collect.getQryColIds();
//        else
//        qryColIds =  collect.reportQryElementIds;
//        if(collect.getQryColAggr()!=null && !collect.getQryColAggr().isEmpty())
//        qryAggrs = (ArrayList) collect.getQryColAggr();
//        else
//        qryAggrs = (ArrayList) collect.reportQryAggregations;
        QueryExecutor qryExec = new QueryExecutor();

        List<DashletDetail> dashlets = collect.dashletDetails;

        Set<QueryDetail> qdSet = new HashSet<QueryDetail>();
//        for (DashletDetail detail : dashlets) {
//            if (DashboardConstants.GRAPH_REPORT.equalsIgnoreCase(detail.getDisplayType())
//                    || DashboardConstants.DASHBOARD_GRAPH_REPORT.equalsIgnoreCase(detail.getDisplayType()) || DashboardConstants.TABLE_REPORT.equalsIgnoreCase(detail.getDisplayType())) {
//                GraphReport graphDetails = (GraphReport) detail.getReportDetails();
//                if(detail.getIsTimeSeries()!=null){
//                graphDetails.setTimeSeries(detail.getIsTimeSeries());
//                if (graphDetails.isTimeSeries()) {
//                    continue;
//                }
//            }
// else{
//      if (graphDetails.isTimeSeries()) {
//                    continue;
//                }
// }
//                }
//
//            if (detail.getReportDetails() != null) {
//                List<QueryDetail> qdList = detail.getReportDetails().getQueryDetails();
//                if (qdList != null) {
//                    qdSet.addAll(qdList);
//                }
//            }
//        }


//        if (!qdSet.isEmpty()) {
//            Iterator<QueryDetail> iter = qdSet.iterator();
//            while (iter.hasNext()) {
//                QueryDetail qd = iter.next();
//                String elemId = qd.getElementId();
//                if (!(qryColIds.contains(elemId))) {
//                    qryColIds.add(qd.getElementId());       //Get the element id
//                    qryAggrs.add(qd.getAggregationType());        //Get the aggregamtion type
//                }
//            }
//        }
//           collect.reportRowViewbyValues = new ArrayList<String>();
//          collect.reportRowViewbyValues.set(0, collect.getTextKPIRowViewBy());
        ArrayList<String> viewBys = new ArrayList<String>();
        if (details.getReportDetails() != null) {
            List<QueryDetail> qdList = details.getReportDetails().getQueryDetails();
            if (qdList != null && !qdList.isEmpty()) {
                for (QueryDetail qd : qdList) {
//                        qd.getElementId();
                    qryColIds.add(qd.getElementId());
                    qryAggrs.add(qd.getAggregationType());
                }
            }
        }

        viewBys.add(details.getRowViewBy());
        ArrayList timedetailsForTextKPI = collect.timeDetailsArray;
        if (timedetailsForTextKPI.get(2).toString().equalsIgnoreCase("09/30/2011")) {
            timedetailsForTextKPI.set(2, "05/30/2011");
        }
//          if(collect.timeDetailsArray.get(2).toString().equalsIgnoreCase("09/30/2011"))
//          collect.timeDetailsArray.set(2, "05/30/2011");
        if (!qryColIds.isEmpty()) {
            repQuery.setRowViewbyCols(viewBys);
            repQuery.setColViewbyCols(collect.reportColViewbyValues);
            repQuery.setParamValue(collect.reportParametersValues);
            repQuery.setQryColumns(qryColIds);
            repQuery.setColAggration(qryAggrs);

            repQuery.setTimeDetails(timedetailsForTextKPI);
            repQuery.isKpi = false;
            repQuery.setReportId(collect.reportId);
            repQuery.setBizRoles(collect.reportBizRoles[0]);
            repQuery.setUserId(userId);

//            repQuery = qryExec.formulateQuery(collect, userId);
            try {
                String query = repQuery.generateViewByQry();
                pbretObj = (PbReturnObject) qryExec.executeQuery(collect, query, false);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }

            HashMap vals111 = new HashMap();
            vals111 = repQuery.getTimememdetails();
            collect.setTimememdetails(vals111);
            container.setTimememdetails(vals111);

            ArrayList<String> dispColumns = new ArrayList<String>();
            ArrayList<String> dispLabels = new ArrayList<String>();
            ArrayList<String> tableMeasures = new ArrayList<String>();
            ArrayList<String> dataTypes = new ArrayList<String>();

//            for (int i = 0; i < collect.getTextKPIRowViewBy(); i++) {
//                String dimId = collect.reportRowViewbyValues.get(i);
//                String dimName = collect.getParameterDispName(dimId);
//                dispColumns.add("A_" + dimId);
//                if(dimId.equalsIgnoreCase("114319"))
//                dimName = "Department";
//                dispLabels.add(dimName);
//                dataTypes.add("C");
//            }
            String dimId = collect.getTextKPIRowViewBy();
            String dimName = collect.getParameterDispName(dimId);
            dispColumns.add("A_" + dimId);
            if (dimId.equalsIgnoreCase("114319")) {
                dimName = "Department";
            }
            dispLabels.add(dimName);
            dataTypes.add("C");
            for (int i = 0; i < qryColIds.size(); i++) {
                String measId = "A_" + qryColIds.get(i);
                dispColumns.add(measId);
                dispLabels.add((String) repQuery.NonViewByMap.get(measId));
                tableMeasures.add((String) repQuery.NonViewByMap.get(measId));
                dataTypes.add("N");
            }
            container.setRetObj(pbretObj);
            container.setDisplayColumns(dispColumns);
            container.setDisplayLabels(dispLabels);
            container.setDataTypes(dataTypes);
            container.getTableHashMap().put("Measures", tableMeasures);
        }
        return pbretObj;
    }

    public String buildTextKpiTable(Container container, String dashletId) {
        String finalresult = "";
        StringBuilder tableBuffer = new StringBuilder();
        String comment = null;
        String displayType = "textKpi";
        PbReturnObject retObject = null;
        retObject = (PbReturnObject) container.getRetObj();
        HashMap textKpiDrill = new HashMap();
        String drillReportId = "";

        ArrayList<String> tablecolumns = new ArrayList<String>();
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        if (collect.getTextKPIRowViewBy() == null) {
            DashboardTemplateDAO dao = new DashboardTemplateDAO();
            String rowViewBy = dao.getRowViewByforTextKpi(collect.reportId, dashletId);
            collect.setTextKPIRowViewBy(rowViewBy);
        }

        DashletDetail dashlet = collect.getDashletDetail(dashletId);

        dashlet.setRowViewBy(collect.getTextKPIRowViewBy());
        StringBuilder ElementNames = new StringBuilder();
        StringBuilder ElementStatus = new StringBuilder();
        retObject = getDashboardTextKPIData(container, collect, null, dashlet);


        ArrayList<String> textKpis = new ArrayList<String>();
        for (int i = 0; i < retObject.getRowCount(); i++) {
            textKpis.add(retObject.getFieldValueString(i, "A_" + collect.getTextKPIRowViewBy()));
            ElementNames.append(",").append(retObject.getFieldValueString(i, "A_" + collect.getTextKPIRowViewBy()));
            ElementStatus.append(",").append(true);
        }
        dashlet.setTextKpis(textKpis);
        if (dashlet.TextkpiDrill != null && !dashlet.TextkpiDrill.isEmpty()) {
            textKpiDrill = (HashMap) dashlet.TextkpiDrill;
        }
        int colSpan = dashlet.getColSpan();
        int rowSpan = dashlet.getRowSpan();
        int rowNum = dashlet.getRow();
        int colNum = dashlet.getCol();
        int width = 350;
        int height = 350;
        height = height * rowSpan;
        width = width * colSpan;
        String dashId = dashlet.getDashBoardDetailId();
        StringBuilder bizRolesString = new StringBuilder();
        if (collect.reportBizRoles != null) {
            for (String role : collect.reportBizRoles) {
                bizRolesString.append(",").append(role);
            }
        }
        List<DashletDetail> dashletDetails = collect.dashletDetails;

        ArrayListMultimap<Integer, Integer> rowinfo = ArrayListMultimap.create();
        for (int i = 0; i < dashletDetails.size(); i++) {
            DashletDetail detail = dashletDetails.get(i);
            rowinfo.put(detail.getRow(), detail.getCol());
        }
        List<Integer> dashlets = rowinfo.get(Integer.parseInt(dashId));
        int numOfDashlets = dashlets.size();
        tablecolumns = container.getDisplayColumns();

        tableBuffer.append("<div class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\" style=\"width:100%; height:100% \">");
        tableBuffer.append("<div id=\"innerDivKpiId\"  class=\"portlet-header1 navtitle portletHeader ui-corner-all\">");
        tableBuffer.append("<table width=\"100%\"><tr>");
        tableBuffer.append("<td align='left'>Text KPI</td>");
        tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td align=\"right\"><a  class=\"ui-icon ui-icon-triangle-2-n-s\" title=\"KPI Drill\" onclick=\"TextKpiDrilldown('").append(ElementNames.toString()).append("','").append(bizRolesString.toString()).append("','").append(collect.reportId).append("','").append(dashlet.getDashBoardDetailId()).append("','").append(dashlet.getKpiMasterId()).append("','").append(false).append("','" + ElementStatus.toString() + "','" + ElementNames + "','" + displayType + "')\"></a></td>");
        tableBuffer.append("</tr></table></div>");
        tableBuffer.append("<div style=\"width:99%;min-height:" + height + "px;overflow-y:auto;overflow-x:auto;padding:4px\"  align=\"left\">");
        tableBuffer.append("<Table style=\"width:99%;height:auto\"   cellpadding=\"0\" cellspacing=\"1\" id=\"tablesorter\"  class=\"tablesorter\" >");
        tableBuffer.append("<thead align=\"center\">");
        tableBuffer.append("<tr>");
        for (int i = 0; i < container.getDisplayLabels().size(); i++) {
            if (i == 0) {
                tableBuffer.append("<th width=\"70px\"><strong>" + container.getDisplayLabels().get(i) + "</strong></th>");
            } else {
                tableBuffer.append("<th width=\"300px\"><strong>" + container.getDisplayLabels().get(i) + "</strong></th>");
            }
        }
        tableBuffer.append("<th width=\"50px\" ><strong>Add Comment</strong></th>");
        tableBuffer.append("</tr>");
        tableBuffer.append("</thead>");
        tableBuffer.append("<tfoot></tfoot>");
        tableBuffer.append("<tbody>");
        for (int i = 0; i < retObject.getRowCount(); i++) {
            tableBuffer.append("<tr>");
            for (int j = 0; j < tablecolumns.size(); j++) {
                if (j == 0) {

                    if (textKpiDrill != null && !textKpiDrill.isEmpty()) {
                        if (textKpiDrill.get(retObject.getFieldValueString(i, tablecolumns.get(j))) != null) {
                            drillReportId = textKpiDrill.get(retObject.getFieldValueString(i, tablecolumns.get(j))).toString();
                        }
                    }
                    if (drillReportId != null && !drillReportId.equalsIgnoreCase("") && !drillReportId.equalsIgnoreCase("0")) {
//                      DashboardTemplateDAO dao = new DashboardTemplateDAO();
//                      String childViewBy = null;
//                      String[] childViewbyarr = null;
//                      String rowViewBy = null;
//                      childViewBy = dao.getCurrentViewbysofRep(drillReportId);//CBOVIEWBY values
//                      childViewbyarr = childViewBy.split(",");
//                      rowViewBy = dao.getRowViewBysForRep(drillReportId);
//                      
                        String url = drillReportId + "&CBOARP" + collect.getTextKPIRowViewBy().trim() + "=" + retObject.getFieldValueString(i, tablecolumns.get(j));
                        tableBuffer.append("<td><strong><a style='decoration:none' href=\"javascript:submiturls12('reportViewer.do?reportBy=viewReport&REPORTID=" + url + "')\">" + retObject.getFieldValueString(i, tablecolumns.get(j)) + "</a></strong></td>");
                        tableBuffer.append("<input type='hidden' name='' value='" + retObject.getFieldValueString(i, tablecolumns.get(j)) + "' readonly id='param" + i + "'/>");
                    } else {
                        tableBuffer.append("<td><strong>" + retObject.getFieldValueString(i, tablecolumns.get(j)) + "</strong></td>");
                        tableBuffer.append("<input type='hidden' name='' value='" + retObject.getFieldValueString(i, tablecolumns.get(j)) + "' readonly id='param" + i + "'/>");
                    }
                    //                 tableBuffer.append("<td>");
//              tableBuffer.append("<input type='hidden' name='' value='"+retObject.getFieldValueString(i, tablecolumns.get(j))+"' readonly id='param"+i+"'/>");
//              tableBuffer.append("</td>");
                    DashboardViewerDAO dao = new DashboardViewerDAO();
                    comment = dao.getTextKpiComment(retObject.getFieldValueString(i, tablecolumns.get(j)), dashId);
//                 if(dashlet.TextkpiComment!=null){
//                     if(dashlet.TextkpiComment.get(retObject.getFieldValueString(i, tablecolumns.get(j)))!=null){
//
//                 comment = dashlet.TextkpiComment.get(retObject.getFieldValueString(i, tablecolumns.get(j)));
//                     }
//                 }
                } else {
                    tableBuffer.append("<td>");
                    tableBuffer.append(retObject.getFieldValueString(i, tablecolumns.get(j)));
                    tableBuffer.append("</td>");
                }
            }
            if (comment != null) {
                tableBuffer.append("<Td align=\"center\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;cursor:pointer;\" onclick=\"showTextKpiCommentDialog('" + dashletId + "','" + i + "')\" title=\"Click to View Comment(s)\"><font size=\"10\">View Comment(s)</font></Td>");
                comment = null;
            } else {
                tableBuffer.append("<Td align=\"center\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;cursor:pointer;\" onclick=\"javascript:showTextKpiCommentDialog('" + dashletId + "','" + i + "')\" title=\"Click to Add Comment\"><font color=\"black\">Add Comment</font></Td>");
            }
            tableBuffer.append("</tr>");
        }
        tableBuffer.append("</tbody>");
        tableBuffer.append("</table>");
        tableBuffer.append("</div>");
        tableBuffer.append("</div>");

        return tableBuffer.toString();
    }

    /**
     * @author srikanth.p For Viewing the generated groups for GroupMeassure
     * Hirarchy
     */
    public String ViewGroup() {
        StringBuilder groupView = new StringBuilder();
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        PbReturnObject retObj = dao.getGroupRetObj();
        groupView.append("<ul id='groupList' class='filetree'>");
        for (int i = 0; i < retObj.getRowCount(); i++) {
            groupView.append("<li id='" + retObj.getFieldValueString(i, 0) + "' class='closed' ><div class=\"hitarea closed\"></div><img src='images/treeViewImages/folder-closed.gif'><span class='groupName' id=fid_" + retObj.getFieldValueString(i, 2) + ",gid_" + retObj.getFieldValueString(i, 0) + " onclick=\"showParents('" + retObj.getFieldValueString(i, 0) + "')\"><font size='1px' face='verdana' >&nbsp;" + retObj.getFieldValueString(i, 1) + "</font></span>");
            groupView.append("<ul id=\"grp_" + retObj.getFieldValueString(i, 0) + "\" ></ul></li>");//<img src='images/treeViewImages/folder-closed.gif'>

        }
        groupView.append("</ul>");
        return groupView.toString();
    }

    public void setInitialGroupMeasssureReport(Container container, DashletDetail dashlet) {
        Report reportDetails = null;
        DashboardViewerDAO viewerDao = new DashboardViewerDAO();
        PbReturnObject pbretObj = new PbReturnObject();
        String dashBoardId = container.getDashboardId();
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        List<QueryDetail> queryDetails = viewerDao.getGroupQueryDetailList(dashlet);
//        dashlet.setReportDetails(reportDetails);
        GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
        graphDetails.setQueryDetails(queryDetails);
        if (graphDetails.isTimeSeries()) {
            pbretObj = getTimeSeriesData(collect, userId);
            container.setTimeSeriesRetObj(pbretObj);
        } else {
            pbretObj = getDashboardData(container, collect, userId);
        }
    }

    public String GroupMeassureBuildParents(String groupId) {
        StringBuilder parentView = new StringBuilder();
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        PbReturnObject retObj = dao.getParentsRetObj(groupId);
//        parentView.append("<ul id='groupList'>");
        for (int i = 0; i < retObj.getRowCount(); i++) {
            parentView.append("<li id='" + retObj.getFieldValueString(i, 0) + "' class='closed' ><a href='#' onclick=\"showChilds('" + retObj.getFieldValueString(i, 0) + "','" + retObj.getFieldValueString(i, 2) + "','" + retObj.getFieldValueString(i, 3) + "')\"><div  id=\"div_" + retObj.getFieldValueString(i, 0) + "_" + retObj.getFieldValueString(i, 2) + "\" class=\"hitarea closed\"></div></a><span class='parentName' id=eid_" + retObj.getFieldValueString(i, 0) + ",gid_" + retObj.getFieldValueString(i, 2) + ",fid_" + retObj.getFieldValueString(i, 3) + ",lev_" + retObj.getFieldValueString(i, 5) + " onclick=\"showChilds('" + retObj.getFieldValueString(i, 0) + "','" + retObj.getFieldValueString(i, 2) + "','" + retObj.getFieldValueString(i, 3) + "')\"><font size='1px' face='verdana' >&nbsp;" + retObj.getFieldValueString(i, 1) + "</font></span>");
            parentView.append("<ul id=\"par_" + retObj.getFieldValueString(i, 0) + "_" + retObj.getFieldValueString(i, 2) + "\" ></ul></li>");//<img src='images/treeViewImages/plus.gif'>

        }
//        parentView.append("</ul>");

        return parentView.toString();
    }

    public String GroupMeassureBuildChilds(String groupId, String parentId, String folderId) {
        StringBuilder parentView = new StringBuilder();
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        PbReturnObject retObj = dao.getchildsRetObj(groupId, parentId, folderId);
//        parentView.append("<ul id='groupList'>");
        for (int i = 0; i < retObj.getRowCount(); i++) {
            parentView.append("<li id='" + retObj.getFieldValueString(i, 0) + "' class='closed' ><a href='#' onclick=\"showChilds('" + retObj.getFieldValueString(i, 0) + "','" + retObj.getFieldValueString(i, 2) + "','" + retObj.getFieldValueString(i, 3) + "')\"><div id=\"div_" + retObj.getFieldValueString(i, 0) + "_" + retObj.getFieldValueString(i, 2) + "\" class=\"hitarea closed\"></div> </a><span class='parentName' id=eid_" + retObj.getFieldValueString(i, 0) + ",gid_" + retObj.getFieldValueString(i, 2) + ",fid_" + retObj.getFieldValueString(i, 3) + ",lev_" + retObj.getFieldValueString(i, 6) + " onclick=\"showChilds('" + retObj.getFieldValueString(i, 0) + "','" + retObj.getFieldValueString(i, 2) + "','" + retObj.getFieldValueString(i, 3) + "')\"><font size='1px' face='verdana' >&nbsp;" + retObj.getFieldValueString(i, 1) + "</font></span>");
            parentView.append("<ul id=\"par_" + retObj.getFieldValueString(i, 0) + "_" + retObj.getFieldValueString(i, 2) + "\" ></ul></li>");//<img src='images/treeViewImages/plus.gif'>

        }
//        parentView.append("</ul>");

        return parentView.toString();
    }

    public void setGroupMeasureKPIData(Container container, DashletDetail dashlet, List<KPIElement> kpiElements, String userId) {

        PbReturnObject pbretObj = null;
        ArrayList<String> queryCols = new ArrayList<String>();
        ArrayList<String> queryAggs = new ArrayList<String>();
//        Report reportDetails = dashlet.getReportDetails();
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        List<KPIElement> elemList = kpiElements;
        List<QueryDetail> queryDetails = new ArrayList<QueryDetail>();
        if (elemList != null && !elemList.isEmpty()) {
            for (KPIElement kpiElement : elemList) {
                QueryDetail qd = new QueryDetail();
                qd.setElementId(kpiElement.getElementId());
                qd.setDisplayName(kpiElement.getElementName());
                qd.setRefElementId(kpiElement.getRefElementId());
//                qd.setFolderId(retObj.getFieldValueInt(i, 4));
//                qd.setSubFolderId(retObj.getFieldValueInt(i, 5));
                qd.setAggregationType(kpiElement.getAggregationType());
                qd.setColumnType(kpiElement.getRefElementType());
                queryCols.add(kpiElement.getElementId());
                queryAggs.add(kpiElement.getAggregationType());
                collect.kpiElementList.put(kpiElement.getRefElementId(), kpiElement);

                queryDetails.add(qd);

            }
            collect.setQryColIds(queryCols);
            collect.setQryColAggr(queryAggs);
        }


        try {

            GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
            graphDetails.setQueryDetails(queryDetails);
            if (graphDetails.isTimeSeries()) {
                pbretObj = getTimeSeriesData(collect, userId);
                container.setTimeSeriesRetObj(pbretObj);
            } else {
                pbretObj = getDashboardData(container, collect, userId);
                container.setRetObj(pbretObj);
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

    }

    /**
     * this method gets all the elements with respect to assigned Reports
     * rootElementList Contains Only parents and the single measures
 *
     */
    public String drillToReport(String groupId, String dbrdId, String dashletId, String folderId) {
        StringBuilder outPut = new StringBuilder();
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        String repQuery = "select a.REPORT_ID,a.REPORT_NAME, a.REPORT_TYPE  from PRG_AR_REPORT_MASTER a,PRG_USER_FOLDER b where REPORT_ID in (select c.REPORT_ID from PRG_AR_REPORT_DETAILS c,PRG_AR_USER_REPORTS d where c.FOLDER_ID in (" + folderId + ") and c.REPORT_ID= d.REPORT_ID and b.FOLDER_ID= c.FOLDER_ID) and b.FOLDER_ID in (" + folderId + ") order by upper(a.REPORT_NAME) asc";
        PbReturnObject measRetObj = null;
        PbReturnObject repRetObj = null;
        try {
            measRetObj = dao.getAllMeasures(dbrdId, dashletId);
            String[] measColNames = measRetObj.getColumnNames();
            repRetObj = execSelectSQL(repQuery);
            if (measRetObj != null) {
                outPut.append("<div id='drillDiv' >  <form action='' name='drilltoReportForm' id='drilltoReportForm' method='POST'>");

                outPut.append("<table align='center' border='0' CELLSPACING='10' ><thead ><th style='font-weight: bold;background-color:#87CEFA'>Measure</th><th style='font-weight: bold;background-color:#87CEFA'>Report</th></thead>");
                outPut.append("<tbody>");
                String elementId = "";
                String parentId = "";
                String refRepId = "";
                String isRepSelected = "";
                String selName = "";
                String value = "";
                String dispName = "";

                ArrayList<String> elemNameList = new ArrayList<String>();
                for (int i = 0; i < measRetObj.rowCount; i++) {
                    elementId = measRetObj.getFieldValueString(i, measColNames[0]);
                    parentId = measRetObj.getFieldValueString(i, measColNames[1]);
                    refRepId = measRetObj.getFieldValueString(i, measColNames[4]);
                    selName = "gid_" + groupId + "~pid_" + parentId + "~eId_" + elementId;
                    elemNameList.add(selName);
                    value = "None";
                    dispName = "None";
                    int selectCount = 0;
                    outPut.append("<tr>");
                    outPut.append("<td width='55%'>").append(measRetObj.getFieldValueString(i, measColNames[2])).append("</td>");
                    outPut.append("<td width='50%'> <select name='").append(selName).append("' align='center'>");
                    for (int j = 0; j < repRetObj.rowCount; j++) {
                        isRepSelected = "";
                        if (refRepId.equalsIgnoreCase(repRetObj.getFieldValueString(j, 0))) {
                            isRepSelected = "selected";
                            selectCount++;
                        }
                        value = repRetObj.getFieldValueString(j, 0) + "~" + repRetObj.getFieldValueString(j, 2);
                        dispName = repRetObj.getFieldValueString(j, 1);

                        outPut.append("<option value= '").append(value).append("' ").append(isRepSelected).append(">").append(dispName).append("</option>");

                    }
                    if (selectCount == 0) {
                        outPut.append("<option value= 'None' selected='selected'>None</option>");
                    }

                    outPut.append("</select></td></tr>");
                }
                outPut.append("</table>");
                outPut.append("<table align='center' border='0' width='100%'><tr><td align='center' ><input  type=\"button\" name=\"save\" value=\"save\" class=\"navtitle-hover\" onclick=\"saveReportAssignment('" + elemNameList + "','" + groupId + "','" + dbrdId + "','" + dashletId + "')\" align='center'/></td></tr></table>");
                outPut.append("</form></div>");
            }

        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return outPut.toString();
    }
    //Surender

    public void displayDataForOneviewKpis(HttpServletRequest request, String action, String userId) {

        PbReportRequestParameter reportReqParams = null;
        pbDashboardCollection collect = null;
        DisplayParameters disp = null;
        String ParamSectionDisplay = null;
        String[] repParamsKeys = null;
        ArrayList Parameters = null;
        String dashletId = request.getParameter("dashletId");
        if (dashletId == null) {
            dashletId = request.getAttribute("dashletId").toString();
        }
        try {
            reportReqParams = new PbReportRequestParameter(this.servletRequest);
            disp = new DisplayParameters();
            if (this.container.getReportCollect() != null) {
                collect = (pbDashboardCollection) container.getReportCollect();

            } else {
                collect = new pbDashboardCollection();
            }
            if (this.servletRequest != null) {
                reportReqParams.setParametersHashMap();
            }
            collect.reportId = this.dashboardId;//here reportId is DashBiooard Id

            if (this.servletRequest != null) {
                collect.ctxPath = this.servletRequest.getContextPath();
            }
            collect.reportIncomingParameters = reportReqParams.requestParamValues;
            if (this.servletRequest != null) {
                collect.setServletRequest(this.servletRequest);
            }
            collect.dateFormat = container.getDateFormat();
            collect.getParamMetaData(action);
            collect.setCheckDashboardType("true");

            repParamsKeys = (String[]) (collect.reportParameters.keySet()).toArray(new String[0]);
            if (collect.reportParameters != null) {
                ArrayList a1 = new ArrayList();
                for (int i = 0; i < repParamsKeys.length; i++) {
                    ArrayList arl = (ArrayList) collect.reportParameters.get(repParamsKeys[i]);
                    if (Parameters == null) {
                        Parameters = new ArrayList();
                    } else {
                        Parameters.add(repParamsKeys[i]);
                    }
                    a1.add(arl.get(1));
                }
                this.container.setReportParameterNames(a1);
            }
            if (this.servletRequest != null) {
                setEditDashboard(String.valueOf(this.servletRequest.getAttribute("edit")));
            }
            if (getEditDashboard().equals("true")) {
                disp.setEditDashboard(true);
            }

            disp.isReport = false;
            disp.completeUrl1 = collect.completeUrl;
            if (this.servletRequest != null) {
                if (Parameters != null) {
                    this.servletRequest.setAttribute("Parameters", Parameters);
                } else {
                    this.servletRequest.setAttribute("Parameters", new ArrayList());
                }
            }
            if (!collect.reportParameters.isEmpty()) {
                ParamSectionDisplay = disp.displayAllParams(collect.reportParameters, collect.timeDetailsMap, collect.reportViewByMain, collect.reportViewByOrder, repParamsKeys[0], collect.resetPath, null, null, null, null);
            } else {
                ParamSectionDisplay = disp.displayTimeParamsForDashboard(collect.timeDetailsMap, collect.resetPath);
            }
            if (this.servletRequest != null) {
                this.servletRequest.setAttribute("ParamSectionDisplay", ParamSectionDisplay);
//            if(request.getAttribute("OneviewTiemDetails")!=null)
//                collect.timeDetailsArray = (ArrayList) request.getAttribute("OneviewTiemDetails");
                if (request.getAttribute("dbrdkpiTimeDetails") != null) {
                    collect.timeDetailsArray = (ArrayList) request.getAttribute("dbrdkpiTimeDetails");
                }
                if (request.getAttribute("reportParameterVals") != null) {
                    collect.reportParametersValues = (LinkedHashMap) request.getAttribute("reportParameterVals");
                    collect.operatorFilters.put("IN", (LinkedHashMap) request.getAttribute("reportParameterVals"));
                }
                collect.setOneviewForTest(true);
                if (!(collect.isInitialized())) {
                    collect.setFromOneView(true);
                    collect.setDashletId(dashletId);
                    createDashletDetail(collect);
                    collect.setFromOneView(false);
//            collect.initialized = true;
                }
                PbReturnObject kpiRetObj = getDashboardKPIData(container, collect, userId);
                container.setKpiRetObj(kpiRetObj);
//            this.servletRequest.setAttribute("buildDashBoard", buildDashBoard(collect, userId));
                this.servletRequest.setAttribute("DisplayParameters", disp);
                this.servletRequest.setAttribute("currentURL", collect.completeUrl);
            } else {
                buildDashBoard(collect, userId);
            }

            this.container.setRepReqParamsHashMap(reportReqParams.requestParamValues);
            //collect.setReportReqParams(reportReqParams.requestParamValues);
            this.container.setReportCollect(collect);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }

    /**
     *
     * @param dashletId
     * @param seqMap
     * @return boolean status of updation
     */
    public boolean updateSequence(String dashletId, HashMap seqMap) {
        boolean status = false;
        String query = "";
        PbDb pbdb = new PbDb();
        ArrayList<String> updateQueryList = new ArrayList<String>();
        for (int i = 0; i < seqMap.size(); i++) {
            query = "UPDATE PRG_AR_KPIWITHGRAPH SET sequence_num=" + i + " WHERE DASHLET_ID=" + dashletId + " AND ELEMENT_IDS='" + seqMap.get(i) + "'";
            updateQueryList.add(query);
        }
        status = pbdb.executeMultiple(updateQueryList);
        return status;
    }

    public String drillToReportTemplate(KPI kpiDetail, String folderId, String dashboardId, String dashletId) {
        StringBuilder outPut = new StringBuilder();
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        String repQuery = "select a.REPORT_ID,a.REPORT_NAME, a.REPORT_TYPE  from PRG_AR_REPORT_MASTER a,PRG_USER_FOLDER b where REPORT_ID in (select c.REPORT_ID from PRG_AR_REPORT_DETAILS c,PRG_AR_USER_REPORTS d where c.FOLDER_ID in (" + folderId + ") and c.REPORT_ID= d.REPORT_ID and b.FOLDER_ID= c.FOLDER_ID) and b.FOLDER_ID in (" + folderId + ") order by upper(a.REPORT_NAME) asc";
        List<String> elementList = kpiDetail.getElementIds();
        List<KPIElement> kpiElements = kpiDetail.getKPIElements();
        PbReturnObject repRetObj = null;
        try {
            String elementId = "";
            String parentId = "";
            String refRepId = "";
            String isRepSelected = "";
            String selName = "";
            String value = "";
            String dispName = "";
            ArrayList<String> elemNameList = new ArrayList<String>();
            repRetObj = execSelectSQL(repQuery);
            outPut.append("<div id='drillDiv' >  <form action='' name='drilltoReportForm' id='drilltoReportForm' method='POST'>");
            outPut.append("<table border=\"0\" cellpadding=\"1\" cellspacing=\"1\" id=\"tablesorter\" class=\"tablesorter\" width=\"15%\"> ");
            outPut.append("<thead><tr><th>KPI Name</th><th>Drill Down Structure</th></tr></thead><tfoot></tfoot><tbody>");
            for (int i = 0; i < elementList.size(); i++) {
                elementId = elementList.get(i);
                refRepId = kpiDetail.getKPIDrill(elementId);
                elemNameList.add(elementId);
                value = "None";
                dispName = "None";
                int selectCount = 0;
                outPut.append("<tr>");
                for (KPIElement kpiElem : kpiElements) {
                    if (elementId.equalsIgnoreCase(kpiElem.getElementId())) {
                        outPut.append("<td width='55%'>").append(kpiElem.getElementName()).append("</td>");
                    }
                }
                outPut.append("<td width='50%'> <select name='").append(elementId).append("' align='center'>");
                for (int j = 0; j < repRetObj.rowCount; j++) {
                    isRepSelected = "";
                    if (refRepId != null && refRepId.equalsIgnoreCase(repRetObj.getFieldValueString(j, 0))) {
                        isRepSelected = "selected";
                        selectCount++;
                    }
                    value = repRetObj.getFieldValueString(j, 0) + "~" + repRetObj.getFieldValueString(j, 2);
                    dispName = repRetObj.getFieldValueString(j, 1);

                    outPut.append("<option value= '").append(value).append("' ").append(isRepSelected).append(">").append(dispName).append("</option>");

                }
                if (selectCount == 0) {
                    outPut.append("<option value= 'None' selected='selected'>None</option>");
                }

                outPut.append("</select></td></tr>");
            }
            outPut.append("</table>");
            outPut.append("<table align='center' border='0' width='100%'><tr><td align='center' ><input  type=\"button\" name=\"save\" value=\"save\" class=\"navtitle-hover\" onclick=\"saveAssignmentForKpi('" + dashboardId + "','" + dashletId + "')\" align='center'/></td></tr></table>");
            outPut.append("</form></div>");

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return outPut.toString();
    }

    public String OLAPGraphBuilder(String dashletId, String dashboardId, Container container, GraphBuilder graphBuilder, String contextPath) {
        StringBuilder resultGraph = new StringBuilder();
        HashMap ParametersHashMap = container.getParametersHashMap();
        ArrayList paramList = (ArrayList) ParametersHashMap.get("Parameters");
        ArrayList parametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail dashlet = collect.getDashletDetail(dashletId);
        GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
        graphDetails.setOLAPEnabled(true);
        ArrayList rowViewByIds = collect.reportRowViewbyValues;
        String graphId = dashlet.getGraphId();
        String prevViewBy = "";
        String nextViewBy = "";
        String classType = "";

        try {

            resultGraph.append("<div class=\"\" id=\"viewbyElementRegion\" > ");
            resultGraph.append("<table valign='top' width='100%' height='20%' border='0'><tr width='100%' class=\"spaceUnder\">");
            String viewById = "";

            for (int i = 0; i < paramList.size(); i++) {
                classType = "navtitle-hover";
                viewById = paramList.get(i).toString();
                if (rowViewByIds.get(0).toString().equalsIgnoreCase(viewById)) {
                    classType = "custom-button-color";
                    if (i > 0 && i < paramList.size() - 1) {
                        prevViewBy = paramList.get(i - 1).toString();
                        nextViewBy = paramList.get(i + 1).toString();

                    }
                    if (i == 0) {
                        prevViewBy = paramList.get(paramList.size() - 1).toString();
                        nextViewBy = paramList.get(1).toString();
                    }
                    if (i == paramList.size() - 1) {
                        prevViewBy = paramList.get(paramList.size() - 2).toString();
                        nextViewBy = paramList.get(0).toString();
                    }

                }
                resultGraph.append("<td align='center'><input type=\"button\" class=\"" + classType + "\" onclick=\"buildGraphOnViewby('" + dashboardId + "','" + dashletId + "','" + viewById + "','" + graphId + "')\" value=\"" + parametersNames.get(i) + "\" name=\"" + paramList.get(i) + "\" /></td>");
                if (paramList.size() > 8 && i == (paramList.size() / 2)) {
                    resultGraph.append("</tr><tr width='100%' class=\"spaceUnder\">");
                }

            }
            resultGraph.append("</tr></table></div><br>");
//         resultGraph.append("div id=\"OLAPChangableDiv\" style=\"over-flow='no';\"");
            resultGraph.append(graphBuilder.displayGraphs(container, dashletId, contextPath, false, ""));
            resultGraph.append("<div ><table width='100%' valign=''bottom' bolrder='0'><tr width=100%>");
            resultGraph.append("<td align=\"left\"><a href=\"#javascript.void(0)\" class=\"ui-icon ui-icon-circle-triangle-w\" onclick=\"buildGraphOnViewby('" + dashboardId + "','" + dashletId + "','" + prevViewBy + "','" + graphId + "')\" style></td>");
            resultGraph.append("<td align=\"center\"><input type=\"button\" class=\"navtitle-hover\" onclick=\"closeOLAPVew('" + dashboardId + "','" + dashletId + "')\" value=\"close\" name=\"\" /></td>");
            resultGraph.append("<td align='right'><a href= \"#javascript.void(0)\" class=\"ui-icon ui-icon-circle-triangle-e\" onclick=\"buildGraphOnViewby('" + dashboardId + "','" + dashletId + "','" + nextViewBy + "','" + graphId + "')\"></td>");
            resultGraph.append("</div>");
            resultGraph.append("</tr></table></div>");
            graphDetails.setOLAPEnabled(false);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }


        return resultGraph.toString();
    }

    public String OLAPGraphForOneView(Container container, String reportId, String graphId, String graphBuffer, String height, String width, String viewbyType, String drillType, String jqGraphId) {
        StringBuilder resultGraph = new StringBuilder();
        HashMap ParametersHashMap = container.getParametersHashMap();
        ArrayList paramList = (ArrayList) ParametersHashMap.get("Parameters");
        ArrayList parametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
        PbReportCollection collect = (PbReportCollection) container.getReportCollect();
        ArrayList rowViewByIds = collect.reportRowViewbyValues;
        String currViewById = rowViewByIds.get(0).toString();
        HashMap drillValueMap = collect.getDrillValuesMap();
        LinkedList drillValueList = new LinkedList();
        DataFacade facade = new DataFacade(container);
        HashMap timeMemDetails = container.getTimememdetails();
        ArrayList timeMemList = null;
        if (timeMemDetails != null && timeMemDetails.get("PR_DAY_DENOM") != null) {
            timeMemList = (ArrayList) timeMemDetails.get("PR_DAY_DENOM");
        }
        if (timeMemList == null) {
            timeMemList = (ArrayList) timeMemDetails.get("pr_day_denom");
        }


        String[] jqGraphArray = {"Area", "Area-Line", "Bar-Horizontal", "Bar-Vertical", "Bubble", "ColumnPie", "DualAxis(Bar-Line)", "Donut-Single", "Donut-Double", "Funnel", "Line", "Line(Smooth)", "Line(Dashed)", "Overlaid(Bar-Line)", "Pie", "Pie-Empty", "Scatter", "Scatter(Regression)", "StackedArea", "StackedBar(V)", "StackedBar(H)", "Waterfall", "Waterfall(GT)"};
        String[] jqGraphIds = {"5500", "5501", "5502", "5503", "5504", "5505", "5506", "5507", "5508", "5509", "5510", "5511", "5512", "5513", "5514", "5515", "5516", "5517", "5518", "5519", "5520", "5521", "5522"};


        if (drillValueMap.get(graphId) != null) {
            drillValueList = (LinkedList) drillValueMap.get(graphId);
        }
        String prevViewBy = "";
        String nextViewBy = "";
        String classType = "";
        String stndardSelected = "";
        String adHocSelected = "";
        if (drillType != null && drillType.equalsIgnoreCase("standardDrill")) {
            stndardSelected = "selected";
        } else {
            if (drillType != null && drillType.equalsIgnoreCase("adHocDrill")) {
                adHocSelected = "selected";
            }
        }
        String defaultViewId = facade.getViewbyId();
//         String paramDivHeight="100%";
//         if(paramList.size() >19){
//             paramDivHeight="85%";
//         }

        try {
//         resultGraph.append("<table vailgn='top' width='"+width+"px' height='10%' border='0'>");
//         resultGraph.append("<tr><td align='right'><a href=\"#javascript.void(0)\" class=\"ui-icon ui-icon-calculator\" onclick=\"buildTableORGraph('"+reportId+"','"+graphId+"','"+rowViewByIds.get(0).toString()+"','"+graphNum+"','table')\" title=\"Switch to Table\"></a></td></tr>");
//         resultGraph.append("</table>");
//         resultGraph.append("<div class=\"\" id=\"viewbyElementRegion\"> ");
//         resultGraph.append("<table valign='top' width='100%' height='20%' border='0'><tr width='100%' class=\"spaceUnder\">");
            resultGraph.append("<table vailgn='top' width='" + width + "px' height='" + height + "px' border='0'>");
            resultGraph.append("<tr><td valign='top' width='15%' height='95%' >"); //rospan='2'

            resultGraph.append("<table border=\"0\"><tr width=\"100%\"><td align=\"left\" height=\"100%\" >");
            resultGraph.append("<div style='width:auto;height:100%;overflow-y:auto;position:absolute;overflow-x: hidden;'>");
            resultGraph.append("<ol id=\"selectable\">");
            String viewById = "";

            for (int i = 0; i < paramList.size(); i++) {
                classType = "";
                viewById = paramList.get(i).toString();
                if (rowViewByIds.get(0).toString().equalsIgnoreCase(viewById)) {
                    classType = "ui-selected";
                    if (i > 0 && i < paramList.size() - 1) {
                        prevViewBy = paramList.get(i - 1).toString();
                        nextViewBy = paramList.get(i + 1).toString();

                    }
                    if (i == 0) {
                        prevViewBy = paramList.get(paramList.size() - 1).toString();
                        nextViewBy = paramList.get(1).toString();
                    }
                    if (i == paramList.size() - 1) {
                        prevViewBy = paramList.get(paramList.size() - 2).toString();
                        nextViewBy = paramList.get(0).toString();
                    }

                }
//             resultGraph.append("<td align='center'><input type=\"button\" class=\""+classType+"\" onclick=\"buildGraphOnViewbyForOneView('"+reportId+"','"+viewById+"','"+graphId+"')\" value=\""+parametersNames.get(i)+"\" name=\""+paramList.get(i)+"\" /></td>");
                resultGraph.append("<li class=\"" + classType + "\" onclick=\"buildGraphOnViewbyForOneView('" + reportId + "','" + viewById + "','" + graphId + "','" + defaultViewId + "')\">").append(parametersNames.get(i)).append("</li>");
//             if(paramList.size() >8 && i==(paramList.size()/2)){
//                 resultGraph.append("</tr><tr width='100%' class=\"spaceUnder\">");
//             }
            }
            String timeClass = "";
            if (rowViewByIds.get(0).toString().equalsIgnoreCase("TIME")) {
                timeClass = "ui-selected";
            }
            resultGraph.append("<li class=\"" + timeClass + "\" onclick=\"buildGraphOnTimeForOneView('" + reportId + "','TIME','" + graphId + "','" + defaultViewId + "')\">").append("Time").append("</li>");
            resultGraph.append("</ol></div></td></tr></table></td>");
            String dayLinkClass = "alinkclass";
            String weekLinkClass = "alinkclass";
            String monthLinkClass = "alinkclass";
            String qurteLinkClass = "alinkclass";
            String yearLinkClass = "alinkclass";
            if (viewbyType.equalsIgnoreCase("Day")) {
                dayLinkClass = "alinkclass-selected";
            }
            if (viewbyType.equalsIgnoreCase("Week")) {
                weekLinkClass = "alinkclass-selected";
            }
            if (viewbyType.equalsIgnoreCase("Quarter") || viewbyType.equalsIgnoreCase("Qtr")) {
                qurteLinkClass = "alinkclass-selected";
            }
            if (viewbyType.equalsIgnoreCase("Year")) {
                yearLinkClass = "alinkclass-selected";
            }
            if (viewbyType.equalsIgnoreCase("Month")) {
                monthLinkClass = "alinkclass-selected";
            }
            resultGraph.append("<td width='85%' height='95%' align='right'>");
            resultGraph.append("<table width='100%' height='100%' valign='top'>");
            //added after scrolling addition

            resultGraph.append("<tr class='trcls' id='olapHeaderRow'><td width='50%' height='1%' align='left' style='border-bottom-style:dashed;border-bottom-color:grey;border-width:1.8px;'>");
            resultGraph.append("<table width=\"50%\" border=\"0\" align='left'><tr>");
            if (!collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                resultGraph.append("<td align='right'><div class=\"" + dayLinkClass + "\"><a href= \"#javascript.void(0)\" onclick=\"buildTimeBaseLine('" + reportId + "','Day','" + graphId + "','" + rowViewByIds.get(0).toString() + "')\">Daily</a></div></td>");
                resultGraph.append("<td align='right'><div class=\"" + weekLinkClass + "\"><a href= \"#javascript.void(0)\" onclick=\"buildTimeBaseLine('" + reportId + "','Week','" + graphId + "','" + rowViewByIds.get(0).toString() + "')\">Weekly</a></div></td>");
                resultGraph.append("<td align='right'><div class=\"" + monthLinkClass + "\"><a href= \"#javascript.void(0)\" onclick=\"buildTimeBaseLine('" + reportId + "','Month','" + graphId + "','" + rowViewByIds.get(0).toString() + "')\">Monthly</a></div></td>");
                resultGraph.append("<td align='right'><div class=\"" + qurteLinkClass + "\"><a href= \"#javascript.void(0)\"  onclick=\"buildTimeBaseLine('" + reportId + "','Qtr','" + graphId + "','" + rowViewByIds.get(0).toString() + "')\">Qtrly</a></div></td>");
                resultGraph.append("<td align='right'><div class=\"" + yearLinkClass + "\"><a href= \"#javascript.void(0)\" onclick=\"buildTimeBaseLine('" + reportId + "','Year','" + graphId + "','" + rowViewByIds.get(0).toString() + "')\">Yearly</a></div></td>");
            }

            if (collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                resultGraph.append("<td align='left'><input id=\"OLAPFromDate\" type=\"text\" style=\"width:80px\" value=\"" + collect.timeDetailsArray.get(2) + "\" readonly />");
                resultGraph.append("&nbsp;&nbsp; TO &nbsp;&nbsp;<input id=\"OLAPFromDate\" type=\"text\" style=\"width:80px\" value=\"" + collect.timeDetailsArray.get(3) + "\" readonly /></td>");
            }
            //added for additional drill defining
//         if(rowViewByIds.get(0).toString().equalsIgnoreCase("TIME")){
//             resultGraph.append("<td align='left'>");
//
//             resultGraph.append("<div id='DrillDiv' class=\"overlapDiv\" style='display:none;width:auto;height:auto;position:absolute;'>");
//             resultGraph.append("<ul>");
////             resultGraph.append("<li style=\"font-family: verdana; font-size: 9px; margin: 1px 1px 0px 0px; padding: 0.4em; width: 80px; background:#64B2FF;cursor: pointer; color: white;\" onclick='localOLAPDrill()'><span >Local Drill</span></li>");
////             resultGraph.append("<li >");
////
////             resultGraph.append("<ul class=\"dropDownMenu\"><li style=\"font-family: verdana; font-size: 9px; margin: 1px 1px 0px 0px; padding: 0.4em; width: 80px; background:#64B2FF;cursor: pointer; color: white;\"><span title=\"Enable Adhoc Drill\">Adhoc Drill</span>");
////             resultGraph.append("<ul style=\"position: absolute; top: 0%; left: 100%;\">");
//             for(int i=0;i<paramList.size();i++){
//                 resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\""+paramList.get(i)+"\",\""+defaultViewId+"\")'>"+parametersNames.get(i)+"</li>");
//             }
////             resultGraph.append("<li class='customLi' onclick='adhocOLAPDrill(\""+currViewById+"\",\"TIME\")'>Time</li>");
//             resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\"Day\",\""+defaultViewId+"\")'>Daily</li>");
//             resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\"Week\",\""+defaultViewId+"\")'>Weekly</li>");
//             resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\"Month\",\""+defaultViewId+"\")'>Monthly</li>");
//             resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\"Qtr\",\""+defaultViewId+"\")'>Qtrly</li>");
//             resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\"Year\",\""+defaultViewId+"\")'>Yearly</li>");
//
//             resultGraph.append("</ul>");
////             resultGraph.append("</li>");
////             resultGraph.append("</ul>");
////             resultGraph.append("</li>");
////             resultGraph.append("</ul>");
//             resultGraph.append("</div></td>");
//

//         }

            resultGraph.append("</tr></table>");
            resultGraph.append("<table align='right'><tr>");

            if (!collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                resultGraph.append("<td align='left'><input id=\"OLAPFromDate\" type=\"text\" style=\"width:70px\" value=\"" + timeMemList.get(0).toString().substring(0, 10) + "\" readonly />");
                resultGraph.append("&nbsp; TO &nbsp;<input id=\"OLAPFromDate\" type=\"text\" style=\"width:70px\" value=\"" + collect.timeDetailsArray.get(2) + "\" readonly /></td>");
            }


            //this is for switch to table
            resultGraph.append("<td align='left'><a href=\"#javascript.void(0)\" class=\"ui-icon ui-icon-calculator\" onclick=\"buildTableORGraph('" + reportId + "','" + graphId + "','" + rowViewByIds.get(0).toString() + "','table')\" title=\"Switch to Table\"></a></td>");
            //this td is for changing the graphTypes
            resultGraph.append("<td align='left'><a title='GraphTypes' onclick='getOLAPGraphTypes()' class='ui-icon ui-icon-image' href='#javascript.void(0)'></a>");
            resultGraph.append("<div class='overlapDiv' id='olapGraphTypesDiv' style='display:none;width:auto;height:auto;position:absolute;'>");
            resultGraph.append("<ul class='graphTypesStyle'>");
            String onclickFunc = "";
            String id = "";
            for (int i = 0; i < jqGraphArray.length; i++) {
                id = "";
                if (jqGraphId.equalsIgnoreCase(jqGraphIds[i])) {
                    onclickFunc = "javascript:void(0)";
                    id = "selecteGraphLI";
                } else {
                    onclickFunc = "changeOLAPGraphType(\"" + jqGraphArray[i] + "\",\"" + jqGraphIds[i] + "\")";
                }
                resultGraph.append("<li id=\"" + id + "\" onclick='" + onclickFunc + "'>" + jqGraphArray[i] + "</li>");
            }
            resultGraph.append("</ul>");
            resultGraph.append("</div>");
            resultGraph.append("</td>");
            //below td builds select boc for adhoc drill
            resultGraph.append("<td align='left' style='float:left'>");
            resultGraph.append("<select id='olapDrillComboBox' onchange='updateDrillType()'>");
            resultGraph.append("<option id='standardDrill' value='standardDrill' " + stndardSelected + ">Std Drill</option>");
            resultGraph.append("<option id='adHocDrill' value='adHocDrill' " + adHocSelected + ">Adhoc Drill</option>");
            resultGraph.append("</select>");
            resultGraph.append("<div id='DrillDiv' class=\"overlapDiv\" style='display:none;width:100%;height:90%;overflow-y:auto;position:absolute;overflow-x: hidden;'>");
            resultGraph.append("<ul>");
//             resultGraph.append("<li style=\"font-family: verdana; font-size: 9px; margin: 1px 1px 0px 0px; padding: 0.4em; width: 80px; background:#64B2FF;cursor: pointer; color: white;\" onclick='localOLAPDrill()'><span >Local Drill</span></li>");
//             resultGraph.append("<li >");
//
//             resultGraph.append("<ul class=\"dropDownMenu\"><li style=\"font-family: verdana; font-size: 9px; margin: 1px 1px 0px 0px; padding: 0.4em; width: 80px; background:#64B2FF;cursor: pointer; color: white;\"><span title=\"Enable Adhoc Drill\">Adhoc Drill</span>");
//             resultGraph.append("<ul style=\"position: absolute; top: 0%; left: 100%;\">");
            for (int i = 0; i < paramList.size(); i++) {
                resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\"" + currViewById + "\",\"" + paramList.get(i) + "\",\"" + defaultViewId + "\")'>" + parametersNames.get(i) + "</li>");
            }
//             resultGraph.append("<li class='customLi' onclick='adhocOLAPDrill(\""+currViewById+"\",\"TIME\")'>Time</li>");
            if (!collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\"" + currViewById + "\",\"Day\",\"" + defaultViewId + "\")'>Daily</li>");
                resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\"" + currViewById + "\",\"Week\",\"" + defaultViewId + "\")'>Weekly</li>");
                resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\"" + currViewById + "\",\"Month\",\"" + defaultViewId + "\")'>Monthly</li>");
                resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\"" + currViewById + "\",\"Qtr\",\"" + defaultViewId + "\")'>Qtrly</li>");
                resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\"" + currViewById + "\",\"Year\",\"" + defaultViewId + "\")'>Yearly</li>");
            }

            resultGraph.append("</ul>");
//             resultGraph.append("</li>");
//             resultGraph.append("</ul>");
//             resultGraph.append("</li>");
//             resultGraph.append("</ul>");
            resultGraph.append("</div>");
            resultGraph.append("</td>");
            resultGraph.append("</tr></table>");
            resultGraph.append("</td>");
            resultGraph.append("</tr>");
//         resultGraph.append("<tr><td valign=\"top\" width='85%' height=\"5%\">");
//         resultGraph.append("<table width=\"100%\" border=\"0\"><tr>");
//         resultGraph.append("<td align=\"left\"><a href=\"#javascript.void(0)\" class=\"ui-icon ui-icon-circle-triangle-w\" onclick=\"buildGraphOnViewbyForOneView('"+reportId+"','"+prevViewBy+"','"+graphId+"')\" style></td>");
//         resultGraph.append("<td align='right'><a href= \"#javascript.void(0)\" class=\"ui-icon ui-icon-circle-triangle-e\" onclick=\"buildGraphOnViewbyForOneView('"+reportId+"','"+nextViewBy+"','"+graphId+"')\"></td>");
//         resultGraph.append("</tr></table>");
//         resultGraph.append("</td></tr>");




            resultGraph.append("<tr><td width='100%' height='99%' valign='top'>");
            resultGraph.append("<div style='overflow:auto;height=\"5%\"'><table border='0' widht='100%'><tr>");
            int endIndex = drillValueList.size();
            int startIndex = 0;
            if (endIndex > 5) {
                startIndex = endIndex - 5;
            }
            for (int i = startIndex; i < endIndex; i++) {
                resultGraph.append("<td class='ui-icon ui-icon-carat-1-e'></td><td><text style='font-family: verdana; color: grey; font-size: 11px;'>" + drillValueList.get(i) + "</text></td>");
            }



            resultGraph.append("</tr></tbody></table></div>");
            resultGraph.append("<div id=\"OLAPGraphRegion\" over-flow=\"no\" style=\"height:95%\" align='right'>");
            resultGraph.append(graphBuffer);
            resultGraph.append("</div>");

            resultGraph.append("</td></tr>");




//         resultGraph.append("<tr><td width='100%' height='3%' valign='top'>");
//         //to display the drill values in bottom
//         resultGraph.append("<div style='over-flow:auto'><table border='0'><tr>");
//         int endIndex=drillValueList.size();
//         int startIndex=0;
//         if(endIndex>5){
//            startIndex=endIndex-5;
//         }
//         for(int i=startIndex;i<endIndex;i++){
//             resultGraph.append("<td class='ui-icon ui-icon-carat-1-e'></td><td><text style='font-family: verdana; color: grey; font-size: 11px;'>"+drillValueList.get(i)+"</text></td>");
//             }
//         resultGraph.append("</tr></tbody></table></div>");
//         //eneded the bottom block
            resultGraph.append("</td></tr>");

            resultGraph.append("</table>");
            resultGraph.append("</td></tr>"); //<td></td>
            resultGraph.append("</table>");



//         resultGraph.append("</tr></table></div>");
//         resultGraph.append("<div id=\"OLAPGraphRegion\" over-flow=\"no\" style=\"height:400px\">");
//         resultGraph.append(graphBuffer);
//         resultGraph.append("</div>");
//         resultGraph.append("<div ><table width='100%' valign=''bottom' bolrder='0'><tr width=100%>");
//         resultGraph.append("<td align=\"left\"><a href=\"#javascript.void(0)\" class=\"ui-icon ui-icon-circle-triangle-w\" onclick=\"buildGraphOnViewbyForOneView('"+reportId+"','"+prevViewBy+"','"+graphId+"')\" style></td>");
//         resultGraph.append("<td align=\"center\"><input type=\"button\" class=\"navtitle-hover\" onclick=\"closeOLAPVew('"+reportId+"')\" value=\"close\" name=\"\" /></td>");
//         resultGraph.append("<td align='right'><a href= \"#javascript.void(0)\" class=\"ui-icon ui-icon-circle-triangle-e\" onclick=\"buildGraphOnViewbyForOneView('"+reportId+"','"+nextViewBy+"','"+graphId+"')\"></td>");
//         resultGraph.append("</div>");
//         resultGraph.append("</tr></table></div>");
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return resultGraph.toString();

    }

    public String OLAPGraphForOneViewd3(HttpServletRequest request, List<String> viewBysList, List<String> viewBysList1, ArrayList timedetails, String reportId, String graphId, String selectid, String height, String width, String viewbyType, String drillType, String jqGraphId) {
        StringBuilder resultGraph = new StringBuilder();
//         HashMap ParametersHashMap = container.getParametersHashMap();
        ArrayList parametersNames = new ArrayList();
        Gson gson = new Gson();
        LinkedList drillValueList = new LinkedList();
        String adhocdrills = request.getParameter("adhocdrills");
        Type tarType1 = new TypeToken<ArrayList>() {
        }.getType();
        if (adhocdrills != null) {
            String[] adhocdrillss = adhocdrills.split(",");
            for (int i = 0; i < adhocdrillss.length; i++) {
                drillValueList.add(adhocdrillss[i].replace("[", "").replace("]", ""));
            }
//          drillValueList = gson.fromJson(adhocdrills, tarType1);
        }
        for (int i = 0; i < viewBysList.size(); i++) {
            parametersNames.add(viewBysList.get(i));
        }
        ArrayList paramList = new ArrayList();
        for (int i = 0; i < viewBysList1.size(); i++) {
            paramList.add(viewBysList1.get(i));
        }
//         ArrayList paramList = (ArrayList)viewBysList;
//         ArrayList parametersNames = (ArrayList) viewBysList1;
//         PbReportCollection collect=(PbReportCollection) container.getReportCollect();
        ArrayList rowViewByIds = paramList;
        String currViewById = rowViewByIds.get(0).toString();
//         HashMap drillValueMap=collect.getDrillValuesMap();
//         LinkedList drillValueList=new LinkedList();
//         DataFacade facade = new DataFacade(container);
//         HashMap timeMemDetails=container.getTimememdetails();
//         ArrayList timeMemList=null;
//         if(timeMemDetails != null && timeMemDetails.get("PR_DAY_DENOM") != null){
//             timeMemList=(ArrayList)timeMemDetails.get("PR_DAY_DENOM");
//         }
//         if(timeMemList==null){
//         timeMemList=(ArrayList)timeMemDetails.get("pr_day_denom");
//         }


        String[] jqGraphArray = {"Area", "Area-Line", "Bar-Horizontal", "Bar-Vertical", "Bubble", "ColumnPie", "DualAxis(Bar-Line)", "Donut-Single", "Donut-Double", "Funnel", "Line", "Line(Smooth)", "Line(Dashed)", "Overlaid(Bar-Line)", "Pie", "Pie-Empty", "Scatter", "Scatter(Regression)", "StackedArea", "StackedBar(V)", "StackedBar(H)", "Waterfall", "Waterfall(GT)"};
        String[] jqGraphIds = {"5500", "5501", "5502", "5503", "5504", "5505", "5506", "5507", "5508", "5509", "5510", "5511", "5512", "5513", "5514", "5515", "5516", "5517", "5518", "5519", "5520", "5521", "5522"};


//         if(drillValueMap.get(graphId)!=null){
//             drillValueList=(LinkedList)drillValueMap.get(graphId);
//         }
        String prevViewBy = "";
        String nextViewBy = "";
        String classType = "";
        String stndardSelected = "";
        String adHocSelected = "";
        String selectedname = "";
        if (drillType != null && drillType.equalsIgnoreCase("standardDrill")) {
            adHocSelected = "selected";
//             stndardSelected="selected";
        } else {
            if (drillType != null && drillType.equalsIgnoreCase("adHocDrill")) {
                adHocSelected = "selected";
            }
        }
        String defaultViewId = selectid;
//         String paramDivHeight="100%";
//         if(paramList.size() >19){
//             paramDivHeight="85%";
//         }

        try {
//         resultGraph.append("<table vailgn='top' width='"+width+"px' height='10%' border='0'>");
//         resultGraph.append("<tr><td align='right'><a href=\"#javascript.void(0)\" class=\"ui-icon ui-icon-calculator\" onclick=\"buildTableORGraph('"+reportId+"','"+graphId+"','"+rowViewByIds.get(0).toString()+"','"+graphNum+"','table')\" title=\"Switch to Table\"></a></td></tr>");
//         resultGraph.append("</table>");
//         resultGraph.append("<div class=\"\" id=\"viewbyElementRegion\"> ");
//         resultGraph.append("<table valign='top' width='100%' height='20%' border='0'><tr width='100%' class=\"spaceUnder\">");
            resultGraph.append("<table vailgn='top' width='" + width + "px' height='" + height + "px' border='0'>");
            resultGraph.append("<tr><td valign='top' width='15%' height='95%' >"); //rospan='2'

            resultGraph.append("<table border=\"0\"><tr width=\"100%\"><td align=\"left\" height=\"100%\" >");
            resultGraph.append("<div style='width:auto;height:100%;overflow-y:auto;position:absolute;overflow-x: hidden;'>");
            resultGraph.append("<ol id=\"selectable\">");
            String viewById = "";

            for (int i = 0; i < paramList.size(); i++) {
                classType = "";
                viewById = paramList.get(i).toString();
                if (selectid.equalsIgnoreCase(viewById)) {
                    classType = "ui-selected";
                    if (i > 0 && i < paramList.size() - 1) {
                        prevViewBy = paramList.get(i - 1).toString();
                        nextViewBy = paramList.get(i + 1).toString();

                    }
                    if (i == 0) {
                        prevViewBy = paramList.get(paramList.size() - 1).toString();
                        nextViewBy = paramList.get(1).toString();
                    }
                    if (i == paramList.size() - 1) {
                        prevViewBy = paramList.get(paramList.size() - 2).toString();
                        nextViewBy = paramList.get(0).toString();
                    }
                    selectedname = (String) parametersNames.get(i);
                }
//             resultGraph.append("<td align='center'><input type=\"button\" class=\""+classType+"\" onclick=\"buildGraphOnViewbyForOneView('"+reportId+"','"+viewById+"','"+graphId+"')\" value=\""+parametersNames.get(i)+"\" name=\""+paramList.get(i)+"\" /></td>");
                resultGraph.append("<li class=\"" + classType + "\" onclick=\"buildGraphOnViewbyForOneView('" + reportId + "','" + viewById + "','" + graphId + "','" + defaultViewId + "','" + parametersNames.get(i) + "')\">").append(parametersNames.get(i)).append("</li>");
//             if(paramList.size() >8 && i==(paramList.size()/2)){
//                 resultGraph.append("</tr><tr width='100%' class=\"spaceUnder\">");
//             }
            }
            String timeClass = "";
            if (rowViewByIds.get(0).toString().equalsIgnoreCase("TIME")) {
                timeClass = "ui-selected";
            }
            resultGraph.append("<li class=\"" + timeClass + "\" onclick=\"buildGraphOnTimeForOneView('" + reportId + "','TIME','" + graphId + "','" + defaultViewId + "')\">").append("Time").append("</li>");
            resultGraph.append("</ol></div></td></tr></table></td>");
            String dayLinkClass = "alinkclass";
            String weekLinkClass = "alinkclass";
            String monthLinkClass = "alinkclass";
            String qurteLinkClass = "alinkclass";
            String yearLinkClass = "alinkclass";
            if (viewbyType.equalsIgnoreCase("Day")) {
                dayLinkClass = "alinkclass-selected";
            }
            if (viewbyType.equalsIgnoreCase("Week")) {
                weekLinkClass = "alinkclass-selected";
            }
            if (viewbyType.equalsIgnoreCase("Quarter") || viewbyType.equalsIgnoreCase("Qtr")) {
                qurteLinkClass = "alinkclass-selected";
            }
            if (viewbyType.equalsIgnoreCase("Year")) {
                yearLinkClass = "alinkclass-selected";
            }
            if (viewbyType.equalsIgnoreCase("Month")) {
                monthLinkClass = "alinkclass-selected";
            }
            resultGraph.append("<td width='85%' height='95%' align='right'>");
            resultGraph.append("<table width='100%' height='100%' valign='top'>");
            //added after scrolling addition

            resultGraph.append("<tr class='trcls' id='olapHeaderRow'><td width='50%' height='1%' align='left' style='border-bottom-style:dashed;border-bottom-color:grey;border-width:1.8px;'>");
            resultGraph.append("<table width=\"50%\" border=\"0\" align='left'><tr>");
//         if(!timedetails.toString().equalsIgnoreCase("PRG_DATE_RANGE")){
//         resultGraph.append("<td align='right'><div class=\""+dayLinkClass+"\"><a href= \"#javascript.void(0)\" onclick=\"buildTimeBaseLine('"+reportId+"','Day','"+graphId+"','"+rowViewByIds.get(0).toString()+"')\">Daily</a></div></td>");
//         resultGraph.append("<td align='right'><div class=\""+weekLinkClass+"\"><a href= \"#javascript.void(0)\" onclick=\"buildTimeBaseLine('"+reportId+"','Week','"+graphId+"','"+rowViewByIds.get(0).toString()+"')\">Weekly</a></div></td>");
//         resultGraph.append("<td align='right'><div class=\""+monthLinkClass+"\"><a href= \"#javascript.void(0)\" onclick=\"buildTimeBaseLine('"+reportId+"','Month','"+graphId+"','"+rowViewByIds.get(0).toString()+"')\">Monthly</a></div></td>");
//         resultGraph.append("<td align='right'><div class=\""+qurteLinkClass+"\"><a href= \"#javascript.void(0)\"  onclick=\"buildTimeBaseLine('"+reportId+"','Qtr','"+graphId+"','"+rowViewByIds.get(0).toString()+"')\">Qtrly</a></div></td>");
//         resultGraph.append("<td align='right'><div class=\""+yearLinkClass+"\"><a href= \"#javascript.void(0)\" onclick=\"buildTimeBaseLine('"+reportId+"','Year','"+graphId+"','"+rowViewByIds.get(0).toString()+"')\">Yearly</a></div></td>");
//             }
//
//         if(timedetails.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")){
//             resultGraph.append("<td align='left'><input id=\"OLAPFromDate\" type=\"text\" style=\"width:80px\" value=\""+timedetails.get(2)+"\" readonly />");
//             resultGraph.append("&nbsp;&nbsp; TO &nbsp;&nbsp;<input id=\"OLAPFromDate\" type=\"text\" style=\"width:80px\" value=\""+timedetails.get(3)+"\" readonly /></td>");
//         }
            //added for additional drill defining
//         if(rowViewByIds.get(0).toString().equalsIgnoreCase("TIME")){
//             resultGraph.append("<td align='left'>");
//
//             resultGraph.append("<div id='DrillDiv' class=\"overlapDiv\" style='display:none;width:auto;height:auto;position:absolute;'>");
//             resultGraph.append("<ul>");
////             resultGraph.append("<li style=\"font-family: verdana; font-size: 9px; margin: 1px 1px 0px 0px; padding: 0.4em; width: 80px; background:#64B2FF;cursor: pointer; color: white;\" onclick='localOLAPDrill()'><span >Local Drill</span></li>");
////             resultGraph.append("<li >");
////
////             resultGraph.append("<ul class=\"dropDownMenu\"><li style=\"font-family: verdana; font-size: 9px; margin: 1px 1px 0px 0px; padding: 0.4em; width: 80px; background:#64B2FF;cursor: pointer; color: white;\"><span title=\"Enable Adhoc Drill\">Adhoc Drill</span>");
////             resultGraph.append("<ul style=\"position: absolute; top: 0%; left: 100%;\">");
//             for(int i=0;i<paramList.size();i++){
//                 resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\""+paramList.get(i)+"\",\""+defaultViewId+"\")'>"+parametersNames.get(i)+"</li>");
//             }
////             resultGraph.append("<li class='customLi' onclick='adhocOLAPDrill(\""+currViewById+"\",\"TIME\")'>Time</li>");
//             resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\"Day\",\""+defaultViewId+"\")'>Daily</li>");
//             resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\"Week\",\""+defaultViewId+"\")'>Weekly</li>");
//             resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\"Month\",\""+defaultViewId+"\")'>Monthly</li>");
//             resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\"Qtr\",\""+defaultViewId+"\")'>Qtrly</li>");
//             resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\"Year\",\""+defaultViewId+"\")'>Yearly</li>");
//
//             resultGraph.append("</ul>");
////             resultGraph.append("</li>");
////             resultGraph.append("</ul>");
////             resultGraph.append("</li>");
////             resultGraph.append("</ul>");
//             resultGraph.append("</div></td>");
//

//         }

//          resultGraph.append("</tr></table>");
            resultGraph.append("<table align='right'><tr>");

//          if(!timedetails.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")){
//              resultGraph.append("<td align='left'><input id=\"OLAPFromDate\" type=\"text\" style=\"width:70px\" value=\""+timedetails.get(2)+"\" readonly />");
//              resultGraph.append("&nbsp; TO &nbsp;<input id=\"OLAPFromDate\" type=\"text\" style=\"width:70px\" value=\""+timedetails.get(2)+"\" readonly /></td>");
//          }


            //this is for switch to table
            resultGraph.append("<td align='left'><a href=\"#javascript.void(0)\" class=\"ui-icon ui-icon-calculator\" onclick=\"buildtableOnViewbyForOneView('" + reportId + "','" + selectid + "','" + graphId + "','" + selectid + "','" + selectedname + "')\" title=\"Switch to Table\"></a></td>");
            //this td is for changing the graphTypes
            resultGraph.append("<td align='left'><a title='GraphTypes' onclick='getOLAPGraphTypes()' class='ui-icon ui-icon-image' href='#javascript.void(0)'></a>");
            resultGraph.append("<div class='overlapDiv' id='olapGraphTypesDiv' style='display:none;width:auto;height:auto;position:absolute;'>");
            resultGraph.append("<ul class='graphTypesStyle'>");
            String onclickFunc = "";
            String id = "";
            for (int i = 0; i < jqGraphArray.length; i++) {
                id = "";
                if (jqGraphId.equalsIgnoreCase(jqGraphIds[i])) {
                    onclickFunc = "javascript:void(0)";
                    id = "selecteGraphLI";
                } else {
                    onclickFunc = "changeOLAPGraphType(\"" + jqGraphArray[i] + "\",\"" + jqGraphIds[i] + "\")";
                }
                resultGraph.append("<li id=\"" + id + "\" onclick='" + onclickFunc + "'>" + jqGraphArray[i] + "</li>");
            }
            resultGraph.append("</ul>");
            resultGraph.append("</div>");
            resultGraph.append("</td>");
            //below td builds select boc for adhoc drill
            resultGraph.append("<td align='left' style='float:left'>");
            resultGraph.append("<select id='olapDrillComboBox' onchange='updateDrillType()'>");
//             resultGraph.append("<option id='standardDrill' value='standardDrill' "+stndardSelected+">Std Drill</option>");
            resultGraph.append("<option id='adHocDrill' value='adHocDrill' " + adHocSelected + ">Adhoc Drill</option>");
            resultGraph.append("</select>");
            resultGraph.append("<div id='DrillDiv' class=\"overlapDiv\" style='display:none;width:100%;height:90%;overflow-y:auto;position:absolute;overflow-x: hidden;'>");
            resultGraph.append("<ul>");
//             resultGraph.append("<li style=\"font-family: verdana; font-size: 9px; margin: 1px 1px 0px 0px; padding: 0.4em; width: 80px; background:#64B2FF;cursor: pointer; color: white;\" onclick='localOLAPDrill()'><span >Local Drill</span></li>");
//             resultGraph.append("<li >");
//
//             resultGraph.append("<ul class=\"dropDownMenu\"><li style=\"font-family: verdana; font-size: 9px; margin: 1px 1px 0px 0px; padding: 0.4em; width: 80px; background:#64B2FF;cursor: pointer; color: white;\"><span title=\"Enable Adhoc Drill\">Adhoc Drill</span>");
//             resultGraph.append("<ul style=\"position: absolute; top: 0%; left: 100%;\">");
            for (int i = 0; i < paramList.size(); i++) {
                resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\"" + defaultViewId + "\",\"" + paramList.get(i) + "\",\"" + defaultViewId + "\",\"" + parametersNames.get(i) + "\",\"this.id\")'>" + parametersNames.get(i) + "</li>");
            }
//             resultGraph.append("<li class='customLi' onclick='adhocOLAPDrill(\""+currViewById+"\",\"TIME\")'>Time</li>");
//             if(!timedetails.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")){
//             resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\"Day\",\""+defaultViewId+"\")'>Daily</li>");
//             resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\"Week\",\""+defaultViewId+"\")'>Weekly</li>");
//             resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\"Month\",\""+defaultViewId+"\")'>Monthly</li>");
//             resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\"Qtr\",\""+defaultViewId+"\")'>Qtrly</li>");
//             resultGraph.append("<li class='customLi' onclick='viewAdhocDrillForOLAP(\""+currViewById+"\",\"Year\",\""+defaultViewId+"\")'>Yearly</li>");
//             }

            resultGraph.append("</ul>");
//             resultGraph.append("</li>");
//             resultGraph.append("</ul>");
//             resultGraph.append("</li>");
//             resultGraph.append("</ul>");
            resultGraph.append("</div>");
            resultGraph.append("</td>");
            resultGraph.append("</tr></table>");
            resultGraph.append("</td>");
            resultGraph.append("</tr>");
//         resultGraph.append("<tr><td valign=\"top\" width='85%' height=\"5%\">");
//         resultGraph.append("<table width=\"100%\" border=\"0\"><tr>");
//         resultGraph.append("<td align=\"left\"><a href=\"#javascript.void(0)\" class=\"ui-icon ui-icon-circle-triangle-w\" onclick=\"buildGraphOnViewbyForOneView('"+reportId+"','"+prevViewBy+"','"+graphId+"')\" style></td>");
//         resultGraph.append("<td align='right'><a href= \"#javascript.void(0)\" class=\"ui-icon ui-icon-circle-triangle-e\" onclick=\"buildGraphOnViewbyForOneView('"+reportId+"','"+nextViewBy+"','"+graphId+"')\"></td>");
//         resultGraph.append("</tr></table>");
//         resultGraph.append("</td></tr>");




            resultGraph.append("<tr><td width='100%' height='99%' valign='top'>");
            resultGraph.append("<div style='overflow:auto;height=\"5%\"'><table border='0' widht='100%'><tr>");
            int endIndex = drillValueList.size();
            int startIndex = 0;
            if (endIndex > 5) {
                startIndex = endIndex - 5;
            }
            for (int i = startIndex; i < endIndex; i++) {
                resultGraph.append("<td class='ui-icon ui-icon-carat-1-e'></td><td><text style='font-family: verdana; color: grey; font-size: 11px;'>" + drillValueList.get(i).toString().replace("\"", "") + "</text></td>");
            }



            resultGraph.append("</tr></tbody></table></div>");
            resultGraph.append("<div id=\"OLAPGraphRegion\" over-flow=\"no\" style=\"height:95%\" align='right'>");
//         resultGraph.append(graphBuffer);
            resultGraph.append("</div>");

            resultGraph.append("</td></tr>");




//         resultGraph.append("<tr><td width='100%' height='3%' valign='top'>");
//         //to display the drill values in bottom
//         resultGraph.append("<div style='over-flow:auto'><table border='0'><tr>");
//         int endIndex=drillValueList.size();
//         int startIndex=0;
//         if(endIndex>5){
//            startIndex=endIndex-5;
//         }
//         for(int i=startIndex;i<endIndex;i++){
//             resultGraph.append("<td class='ui-icon ui-icon-carat-1-e'></td><td><text style='font-family: verdana; color: grey; font-size: 11px;'>"+drillValueList.get(i)+"</text></td>");
//             }
//         resultGraph.append("</tr></tbody></table></div>");
//         //eneded the bottom block
            resultGraph.append("</td></tr>");

            resultGraph.append("</table>");
            resultGraph.append("</td></tr>"); //<td></td>
            resultGraph.append("</table>");



//         resultGraph.append("</tr></table></div>");
//         resultGraph.append("<div id=\"OLAPGraphRegion\" over-flow=\"no\" style=\"height:400px\">");
//         resultGraph.append(graphBuffer);
//         resultGraph.append("</div>");
//         resultGraph.append("<div ><table width='100%' valign=''bottom' bolrder='0'><tr width=100%>");
//         resultGraph.append("<td align=\"left\"><a href=\"#javascript.void(0)\" class=\"ui-icon ui-icon-circle-triangle-w\" onclick=\"buildGraphOnViewbyForOneView('"+reportId+"','"+prevViewBy+"','"+graphId+"')\" style></td>");
//         resultGraph.append("<td align=\"center\"><input type=\"button\" class=\"navtitle-hover\" onclick=\"closeOLAPVew('"+reportId+"')\" value=\"close\" name=\"\" /></td>");
//         resultGraph.append("<td align='right'><a href= \"#javascript.void(0)\" class=\"ui-icon ui-icon-circle-triangle-e\" onclick=\"buildGraphOnViewbyForOneView('"+reportId+"','"+nextViewBy+"','"+graphId+"')\"></td>");
//         resultGraph.append("</div>");
//         resultGraph.append("</tr></table></div>");
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return resultGraph.toString();

    }

    public String GetDefaultViewBy(String DashboardId) {
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        HashMap<String, String> map = new HashMap();
        String query = "select DEFAULT_VIEWBY from PRG_AR_DASHBOARD_DETAILS where DASHBOARD_ID= " + DashboardId;


        try {
            pbro = pbdb.execSelectSQL(query);
            if (pbro != null) {
                return pbro.getFieldValueString(0, 0);
            } else {
                return "";
            }
        } catch (Exception ex) {

            logger.error("Exception:", ex);


        }
        return "";

    }
}
