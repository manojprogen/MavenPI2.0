/**
 * @filename PbGraphDisplay
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 19, 2009, 1:23:07 PM
 */
package com.progen.report.charts;

import com.progen.charts.GraphProperty;
import com.progen.charts.JqplotGraphProperty;
import com.progen.charts.ProgenChartDatasets;
import com.progen.charts.ProgenChartDisplay;
import com.progen.dashboardView.db.DashboardViewerDAO;
import com.progen.db.ProgenDataSet;
import com.progen.query.RTMeasureElement;
import com.progen.report.DashletDetail;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.entities.GraphReport;
import com.progen.report.entities.QueryDetail;
import com.progen.report.kpi.DashletPropertiesHelper;
import com.progen.report.pbDashboardCollection;
import com.progen.reportdesigner.bd.DashboardTemplateBD;
import com.progen.reportview.db.PbReportViewerDAO;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Clob;
import java.text.NumberFormat;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class PbGraphDisplay extends PbDb implements Serializable {

    public static Logger logger = Logger.getLogger(PbGraphDisplay.class);
    private static final long serialVersionUID = 2279871274881909L;
    public PbGraphDisplayResourceBundle resBundle = new PbGraphDisplayResourceBundle();
    public String graphHdrsQuery = resBundle.getString("grpHdrsQuery");
    public String graphHdrsQueryFX = resBundle.getString("graphHdrsQueryFX");
    public String graphDtlsQuery = resBundle.getString("grpDtlsQuery");
    public String grpHdrsQueryByGraphIdQuery = resBundle.getString("grpHdrsQueryByGraphId");//@ dashboard designer
    public String dashboardGraphHdrsQueryNew = resBundle.getString("dashboardGraphHdrsQueryNew");//@ dashboard viewer
    String dashboardGraphDtlsQuery = resBundle.getString("dashboardGrpDtlsQuery");
    public String reportId;
    public String[] viewByElementIds;
    public String[] viewByColNames;
    public ArrayList currentDispRecords;
    public PbReturnObject pbretObj = null;
    public PbReturnObject pbretObj2 = null;
    public String[] dbColumns = null;
    private String[] pieChartColumns = null;
    private String[] barChartColumnNames = null;
    private String[] barChartColumnTitles = null;
    public ProgenChartDatasets graph = null;
    public ProgenChartDisplay[] pcharts = null;
//    public String path = "";
    public StringBuilder path = new StringBuilder();
//    public String graphTitle = "";
    public StringBuilder graphTitle = new StringBuilder();
    public String graphId = null;
    public String graphName = null;
    public String graphWidth = null;
    public String graphHeight = null;
    public String graphWidth1 = null;
    public String graphHeight1 = null;
    public String graphClassName = null;
    public String graphTypeName = null;
    public String jscal = "1";
    public HttpSession session = null;
    public HttpServletResponse response = null;
    public Writer out = null;
    public int graphsCnt = 0;
    public String ctxPath = null;
    private String startindex = "";
    private String endindex = "";
    public String[] axis = null;
    private String[] barChartColumnNames1 = null;
    private String[] barChartColumnTitles1 = null;
    private String[] barChartColumnNames2 = null;
    private String[] barChartColumnTitles2 = null;
    public ProgenChartDatasets graph1 = null;
    public ProgenChartDatasets graph2 = null;
    //public HashMap swapGraphAnalysis = null;
    public ProgenChartDisplay[] pchartsZoom = null;
//    public String pathZoom = "";
    public StringBuilder pathZoom = new StringBuilder();
    public Object[] Obj = null;
    public String finalQuery = "";
    public HashMap[] graphMapDetails = null;
    public HashMap GraphHashMap = null;
    public StringBuffer AllGraphIds = null;
    public ArrayList AllGraphColumns = null;
    //added by santhosh.kumar@progenbusiness.com on 25/11/2009
    private String myGraphType;
    public String graphSize = "Medium";
    public String grplegend = "Y";
    public String grplegendloc = "Bottom";
    public String grpshox = "Y";
    public String grpshoy = "Y";
    public String grpLYaxislabel = "Left";
    public String dispRowsAfterDrill = null;
    public boolean fromOneview = false;
    public String olapFun;
    public String selectedgraphtype = "jf";

//    public String getGrpbDomainaxislabel() {
//        return grpbDomainaxislabel;
//    }
//
//    public void setGrpbDomainaxislabel(String grpbDomainaxislabel) {
//        this.grpbDomainaxislabel = grpbDomainaxislabel;
//    }
    public String getGrplyaxislabel() {
        return grpLYaxislabel;
    }

    public void setGrplyaxislabel(String grplyaxislabel) {
        this.grpLYaxislabel = grplyaxislabel;
    }

    public String getGrpryaxislabel() {
        return grpryaxislabel;
    }

    public void setGrpryaxislabel(String grpryaxislabel) {
        this.grpryaxislabel = grpryaxislabel;
    }
    public String grpryaxislabel = "";
    public String grpbDomainaxislabel = "Bottom";
    public String grpdrill = "Y";
    public String grpbcolor = "";
    public String grpfcolor = "";
    public String grpdata = "Y";
    public boolean SwapColumn = true;
    public pbDashboardCollection collect = new pbDashboardCollection();
    //added by santhosh.kumar@progenbusiness.com on 24/12/2009 fro getting all record list
    private ArrayList allDispRecords = null;
    private ArrayList rowValuesList = null;//added on 24/12/2009 for column pie
    private String showGT = "N";
    private ArrayList currentDispRecordsWithGT;
    private String timelevel = null;
    private String scatterViewBy = null;
    private boolean timeSeries = false;
    public String nbrFormat = "";
    public String graphSymbol = null;
    private String showlyAxis = "";
    private String showryAxis = "";
    private String showxAxis = "";
    public String measNamePosition = "";
    private String piChartMeasurelabel = "";
    private String catChartMeasurelabel = "";

    public String getMeasNamePosition() {
        return measNamePosition;
    }

    public void setMeasNamePosition(String measNamePosition) {
        this.measNamePosition = measNamePosition;
    }
    private boolean checkingTimeSeries = false;
    private String graphGridLines = "Y";
    private String graphDisplayRows = "";
    private ProgenDataSet currentDispRecordsRetObjWithGT;
    private ProgenDataSet currentDispRetObjRecords;
    private ProgenDataSet allDispRecordsRetObj = null;
    private String targetRange = null;
    private String startValue = null;
    private String endValue = null;
    private String showMinMaxRange = "N";
    private String ProGenImgPath = "";
//added by santhosh on 01-03-2010 foir getting information for entire dataset
    private TreeMap columnOverAllMaximums = null;
    private TreeMap columnOverAllMinimums = null;
    private TreeMap columnAverages = null;
    private TreeMap columnGrandTotals = null;
    //
    private HashMap GraphSizesDtlsHashMap = null;
    private String ToolTipType = null;
    //added by uday on 24-feb-2010
    private HashMap whatIfRanges = null;
    private String whatIfScenarioId = null;
    private String uiTheme = null;
    private Integer screenResolution = null;
    private GraphProperty graphProperty;
    private ArrayList<Integer> graphViewSeq;
    private boolean isCrosstab;
    private ArrayList<String> sortColumns = new ArrayList<String>();
    private int noOfDays;
    private String stackedType = "absStacked";
    private String measureFormat = null;
    private String measureValueRounding = null;
    private String axisLabelPosition;
    public boolean showLabels;
    public String calibration = null;
    private String firstChartType;
    private String secondChartType;
    private String[] rgbColorArr;
//    private boolean stackedFlag;

    /**
     * Creates a new instance of ProgenGraphDisp
     */
    public PbGraphDisplay() {
    }

    public String getGraphId() {
        return graphId;
    }

    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }

    public ArrayList getCurrentDispRecords() {
        return currentDispRecords;
    }

    public String getJscal() {
        return jscal;
    }

    public void setJscal(String jscal) {
        this.jscal = jscal;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setCurrentDispRecords(ArrayList currentDispRecords) {
        this.currentDispRecords = currentDispRecords;
    }

    public Writer getOut() {
        return out;
    }

    public void setOut(Writer out) {
        this.out = out;
    }

    public String getGrpHdrsQuery(String reportId) {
        Obj = new Object[1];
        Obj[0] = reportId;
        finalQuery = buildQuery(graphHdrsQuery, Obj);
        return finalQuery;
    }

    public String getGrpHdrsQueryFX(String reportId) {
        Obj = new Object[1];
        Obj[0] = reportId;
        finalQuery = buildQuery(graphHdrsQueryFX, Obj);
        return finalQuery;
    }

    public String getGrpHdrsQueryByGraphIdQuery(String reportId, String graphId) {
        Obj = new Object[2];
        Obj[0] = reportId;
        Obj[1] = graphId;
        finalQuery = buildQuery(grpHdrsQueryByGraphIdQuery, Obj);
        return finalQuery;
    }

    public String getDashboardGrpHdrsQueryNew(String reportId, String graphId) {
        Obj = new Object[2];
        Obj[0] = reportId;
        Obj[1] = graphId;
        finalQuery = buildQuery(grpHdrsQueryByGraphIdQuery, Obj);
        //////////////////////.println("getDashboardGrpHdrsQueryNew->finalQuery="+finalQuery);
        return finalQuery;
    }

    public String getDashboardGrpDtlsQuery(String graphId) {
        Obj = new Object[1];
        Obj[0] = graphId;
        finalQuery = buildQuery(dashboardGraphDtlsQuery, Obj);
        return finalQuery;
    }

    public String getGrpDtlsQuery(String graphId) {
        Obj = new Object[1];
        Obj[0] = graphId;
        finalQuery = buildQuery(graphDtlsQuery, Obj);
        //////////////////////.println("getGrpDtlsQuery="+finalQuery);
        return finalQuery;
    }

    public PbReturnObject getPbReturnObject(String sql) {
        PbReturnObject pbretObj3 = null;
        try {
            pbretObj3 = execSelectSQL(sql);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return pbretObj3;
    }

    public ArrayList getGraphHeadersByGraphMap() {
        ArrayList grpDetails = new ArrayList();
        try {
            pcharts = new ProgenChartDisplay[graphMapDetails.length];
            pchartsZoom = new ProgenChartDisplay[graphMapDetails.length];
            for (int i = 0; i < graphMapDetails.length; i++) {
                getGraphWiseDetailsMap(graphMapDetails[i]);//getting graph details from hashmap
                setBarChartColumnTitles((String[]) graphMapDetails[i].get("barChartColumnTitles"));
                setBarChartColumnNames((String[]) graphMapDetails[i].get("barChartColumnNames"));
                setPieChartColumns((String[]) graphMapDetails[i].get("pieChartColumns"));
                axis = ((String[]) graphMapDetails[i].get("axis"));
                viewByElementIds = ((String[]) graphMapDetails[i].get("viewByElementIds"));
                setBarChartColumnNames1((String[]) graphMapDetails[i].get("barChartColumnNames1"));
                setBarChartColumnTitles1((String[]) graphMapDetails[i].get("barChartColumnTitles1"));
                setBarChartColumnNames2((String[]) graphMapDetails[i].get("barChartColumnNames2"));
                setBarChartColumnTitles2((String[]) graphMapDetails[i].get("barChartColumnTitles2"));

                ArrayList ProgenChartDisplayList = buildGraphBuildingInfo(pcharts[i], pchartsZoom[i]); //building graphs
                pcharts[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(0);
                pchartsZoom[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(1);
                graphMapDetails[i] = buildGraphWiseDetailsMap(graphMapDetails[i]);
            }
//            if (!path.equalsIgnoreCase("")) {
            if (path != null && path.length() > 0) {
                path = new StringBuilder(path.substring(1));
            }
//            if (!graphTitle.equalsIgnoreCase("")) {
            if (graphTitle != null && graphTitle.length() > 0) {
//                graphTitle = graphTitle.substring(1);
                graphTitle = new StringBuilder(graphTitle.substring(1));
            }
            grpDetails.add(path);
            grpDetails.add(graphTitle);
            grpDetails.add(pcharts);
            grpDetails.add(pathZoom);
            grpDetails.add(pchartsZoom);
            grpDetails.add(graphMapDetails);

        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return grpDetails;
    }

    //added for dashboard on 19/10/09
    public HashMap getGraphsForDashBoard(String repId, HashMap DashBoardsGraphs) {
        try {
            pbretObj = getPbReturnObject(getGrpHdrsQuery(repId));
            String[] tableDBColumns = pbretObj.getColumnNames();
            setGraphsCnt(pbretObj.getRowCount());
            pcharts = new ProgenChartDisplay[getGraphsCnt()];
            pchartsZoom = new ProgenChartDisplay[getGraphsCnt()];
            String[] grpIds = null;
            graphMapDetails = new HashMap[pbretObj.getRowCount()];
            grpIds = new String[pbretObj.getRowCount()];
            String[] viewBys = null;
            for (int i = 0; i < getGraphsCnt(); i++) {
                graphMapDetails[i] = new HashMap();
                grpIds[i] = pbretObj.getFieldValueString(i, tableDBColumns[0]);
                getGraphWiseDetailsMapFromPbReturnObject(pbretObj, i, tableDBColumns);
                graphWidth = "420";
                graphHeight = "290";
                setGraphId(graphId);
                pbretObj2 = getPbReturnObject(getGrpDtlsQuery(graphId));
                setBarChartColumnTitles(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                setBarChartColumnNames(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                setPieChartColumns(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                axis = new String[getViewByElementIds().length + pbretObj2.getRowCount()];
                String[] graphDBColumns = pbretObj2.getColumnNames();
                viewBys = viewByElementIds;
                for (int viewbyIndex = 0; viewbyIndex < viewByElementIds.length; viewbyIndex++) {
                    if (!(viewByElementIds[viewbyIndex].contains("A_"))) {
                        viewBys[viewbyIndex] = "A_" + viewByElementIds[viewbyIndex];
                    } else {
                        viewBys[viewbyIndex] = viewByElementIds[viewbyIndex];
                    }
                    getBarChartColumnTitles()[viewbyIndex] = viewByColNames[viewbyIndex];
                    getBarChartColumnNames()[viewbyIndex] = viewBys[viewbyIndex];
                    axis[viewbyIndex] = "0";
                }
                for (int j = 0; j < pbretObj2.getRowCount(); j++) {
                    getBarChartColumnTitles()[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[1]);
                    getBarChartColumnNames()[viewByElementIds.length + j] = "A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                    if (pbretObj2.getFieldValueString(j, graphDBColumns[2]).equalsIgnoreCase("") || pbretObj2.getFieldValueString(j, graphDBColumns[2]) == null) {
                        axis[viewByElementIds.length + j] = "0";
                    } else {
                        axis[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[2]);
                    }
                }
                setPieChartColumns(getBarChartColumnNames());
                ArrayList ProgenChartDisplayList = buildGraphBuildingInfo(pcharts[i], pchartsZoom[i]);
                pcharts[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(0);
                pchartsZoom[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(1);
                graphMapDetails[i] = buildGraphWiseDetailsMap(graphMapDetails[i]);//storing graph header info in HashMap

            }

//             if (!path.equalsIgnoreCase("")) {
            if (path != null && path.length() > 0) {
                path = new StringBuilder(path.substring(1));
            }
            //            if (!graphTitle.equalsIgnoreCase("")) {
            if (graphTitle != null && graphTitle.length() > 0) {
//                graphTitle = graphTitle.substring(1);
                graphTitle = new StringBuilder(graphTitle.substring(1));
            }

            String[] grpPaths = path.toString().split(";");
            HashMap graphPaths = new HashMap();

            for (int k = 0; k < grpPaths.length; k++) {
                graphPaths.put(grpIds[k], grpPaths[k]);
            }

            DashBoardsGraphs.put(repId, graphPaths);

        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return DashBoardsGraphs;
    }

    //added on 29-oct
    public String getCtxPath() {
        return ctxPath;
    }

    public void setCtxPath(String ctxPath) {
        this.ctxPath = ctxPath;
    }

    public int getGraphsCnt() {
        return graphsCnt;
    }

    public void setGraphsCnt(int graphsCnt) {
        this.graphsCnt = graphsCnt;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String[] getViewByElementIds() {
        return viewByElementIds;
    }

    public void setViewByElementIds(String[] viewByElementIds) {
        this.viewByElementIds = viewByElementIds;
    }

    public String[] getViewByColNames() {
        return viewByColNames;
    }

    public void setViewByColNames(String[] viewByColNames) {
        this.viewByColNames = viewByColNames;
    }

    public HashMap[] getGraphMapDetails() {
        return graphMapDetails;
    }

    public void setGraphMapDetails(HashMap[] graphMapDetails) {
        this.graphMapDetails = graphMapDetails;
    }

    public HashMap getGraphHashMap() {
        return GraphHashMap;
    }

    public void setGraphHashMap(HashMap GraphHashMap) {
        this.GraphHashMap = GraphHashMap;
    }

    //New Method created by santhosh.kumar@progenbusiness.com on 01/12/2009 for retreiving and modifying graph details at run time
    public ArrayList getGraphHeaders(int noOfDays, Container container) {

        //////.println("getGraphHeaders");
        ArrayList grpDetails = new ArrayList();
        //Measures of the grpah
        //LinkedHashMap stores the Element Id as Key and Col Name as Value
        //Introducing a new LinkedHashMap for storing individual Graphs Measure
        //BiMap will store the ElementId and Measure Col Name
        //like A_75755 - Sales Amount
        LinkedHashMap<String, String> grphMeasMap;
        String measId;
        String measDispName;

        try {
            if (getGraphHashMap() != null && getGraphHashMap().get("isGraphsExists") != null) {
                //////.println("1");
                grpDetails = getGraphHeadersMapAtRT(container);


            } else {
                ////////.println("2");


                if (GraphHashMap.get("isGraphsExists") == null) {
                    pbretObj = getPbReturnObject(getGrpHdrsQuery(getReportId()));
                    setGraphsCnt(pbretObj.getRowCount());
                    if (getGraphsCnt() != 0) {
                        String[] tableDBColumns = pbretObj.getColumnNames();
                        GraphHashMap.put("isGraphsExists", "true");
                        pcharts = new ProgenChartDisplay[getGraphsCnt()];
                        pchartsZoom = new ProgenChartDisplay[getGraphsCnt()];
                        graphMapDetails = new HashMap[pbretObj.getRowCount()];
                        if (GraphHashMap == null) {
                            GraphHashMap = new HashMap();
                        }
                        if (AllGraphIds == null) {
                            AllGraphIds = new StringBuffer("");
                        }
                        if (AllGraphColumns == null || AllGraphColumns.size() == 0) {
                            AllGraphColumns = new ArrayList();
                        }
                        for (int i = 0; i < getGraphsCnt(); i++) {

                            grphMeasMap = new LinkedHashMap<String, String>();
                            //////.println("3");
                            AllGraphIds.append("," + pbretObj.getFieldValueString(i, tableDBColumns[0]));
                            graphMapDetails[i] = new HashMap();
                            getGraphWiseDetailsMapFromPbReturnObject(pbretObj, i, tableDBColumns);//
                            if (!isCheckingTimeSeries()) {

                                ////////////////.println(" in for loop    !isCheckingTimeSeries()");

                                pbretObj2 = getPbReturnObject(getGrpDtlsQuery(graphId));
                                setBarChartColumnTitles(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                                setBarChartColumnNames(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                                setPieChartColumns(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                                axis = new String[getViewByElementIds().length + pbretObj2.getRowCount()];
                                String[] graphDBColumns = pbretObj2.getColumnNames();
                                for (int viewbyIndex = 0; viewbyIndex < viewByElementIds.length; viewbyIndex++) {
                                    getBarChartColumnTitles()[viewbyIndex] = viewByColNames[viewbyIndex];
                                    getBarChartColumnNames()[viewbyIndex] = viewByElementIds[viewbyIndex];
                                    getPieChartColumns()[viewbyIndex] = viewByElementIds[viewbyIndex];
                                    axis[viewbyIndex] = "0";
                                }
                                for (int j = 0; j < pbretObj2.getRowCount(); j++) {
                                    getBarChartColumnTitles()[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[1]);
                                    getBarChartColumnNames()[viewByElementIds.length + j] = "A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                                    getPieChartColumns()[viewByElementIds.length + j] = "A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                                    axis[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[2]);

                                    if (!AllGraphColumns.contains("A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]))) {
                                        AllGraphColumns.add("A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]));
                                    }
                                    measId = "A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                                    measDispName = pbretObj2.getFieldValueString(j, graphDBColumns[1]);
                                    grphMeasMap.put(measId, measDispName);
                                }

                                if (graphProperty.getBarChartColumnNames1() != null) {
                                    if (graphProperty.getBarChartColumnNames1().length != 0) {
                                        setBarChartColumnNames1(graphProperty.getBarChartColumnNames1());
                                        setBarChartColumnTitles1(graphProperty.getBarChartColumnTitles1());
                                        for (int k = viewByElementIds.length; k < barChartColumnNames1.length; k++) {
                                            if (axis.length >= k) {
                                                axis[k] = "0";
                                            }
                                        }

                                    }
                                }
                                if (graphProperty.getBarChartColumnNames2() != null) {
                                    if (graphProperty.getBarChartColumnNames2().length != 0) {
                                        setBarChartColumnNames2(graphProperty.getBarChartColumnNames2());
                                        setBarChartColumnTitles2(graphProperty.getBarChartColumnTitles2());
                                        if (barChartColumnNames2.length > viewByElementIds.length) {
                                            for (int m = barChartColumnNames1.length; m < barChartColumnNames.length; m++) {
                                                axis[m] = "1";
                                            }
                                        }
                                    }
                                }

                                JqplotGraphProperty graphproperty = new JqplotGraphProperty();
                                ;
                                HashMap singleGraphDetails = (HashMap) container.getGraphHashMap().get(graphId);
                                PbReportViewerDAO reportViewerdao = new PbReportViewerDAO();

                                if (singleGraphDetails != null && singleGraphDetails.get("jqgraphproperty" + graphId) != null) {
                                    graphproperty = (JqplotGraphProperty) singleGraphDetails.get("jqgraphproperty" + graphId);
                                    selectedgraphtype = graphproperty.getSlectedGraphType(graphId);

                                } else {
                                    graphproperty = reportViewerdao.getJqGraphDetails(graphId);
                                    if (graphproperty != null) {
                                        selectedgraphtype = graphproperty.getSlectedGraphType(graphId);
                                    }
                                }

                                ArrayList ProgenChartDisplayList = buildGraphBuildingInfo(pcharts[i], pchartsZoom[i]);
                                this.getGraphProperty().setStackedType(this.getGraphProperty().getStackedType());

                                ////////////////.println("ProgenChartDisplayList.get(0)="+ProgenChartDisplayList.get(0));
                                pcharts[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(0);
                                pchartsZoom[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(1);
                                graphMapDetails[i] = buildGraphWiseDetailsMap(graphMapDetails[i]);
                                graphMapDetails[i].put("graphMeasures", grphMeasMap);
                                GraphHashMap.put(graphId, graphMapDetails[i]);//which holds details of all graphs
                            }
                        }

                        if (!isCheckingTimeSeries()) {

                            ////////////////.println("!isCheckingTimeSeries()");
                            GraphHashMap.put("AllGraphColumns", AllGraphColumns);

                            if (AllGraphIds.toString().equalsIgnoreCase("")) {
                                GraphHashMap.put("graphIds", AllGraphIds.toString());
                            } else {
                                GraphHashMap.put("graphIds", AllGraphIds.substring(1));
                            }
//                              if (!path.equalsIgnoreCase("")) {
                            if (path != null && path.length() > 0) {
                                path = new StringBuilder(path.substring(1));
                            }
//            if (!graphTitle.equalsIgnoreCase("")) {
                            if (graphTitle != null && graphTitle.length() > 0) {
//                graphTitle = graphTitle.substring(1);
                                graphTitle = new StringBuilder(graphTitle.substring(1));
                            }

                            grpDetails.add(path);
                            grpDetails.add(graphTitle);
                            grpDetails.add(pcharts);

                            grpDetails.add(pathZoom);
                            grpDetails.add(pchartsZoom);
                            grpDetails.add(graphMapDetails);
                        }
                    } else {
                        GraphHashMap.put("isGraphsExists", "false");
                    }
                }
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }

        return grpDetails;
    }

    public ArrayList getGraphHeadersMapAtRT(Container container) {

        //////.println("getGraphHeadersMapAtRT");

        ArrayList grpDetails = new ArrayList();
        LinkedHashMap<String, String> grphMeasMap;
        String measId;
        String measDispName;
        if (getGraphHashMap().get("graphIds") != null) {
            ////////////////.println("get graph hashmapppppppppppppppp");
            String[] graphIds = String.valueOf(getGraphHashMap().get("graphIds")).split(",");
            ////////////////.println("graphIds="+graphIds.length);

            pcharts = new ProgenChartDisplay[graphIds.length];
            pchartsZoom = new ProgenChartDisplay[graphIds.length];
            graphMapDetails = new HashMap[graphIds.length];
            try {
                AllGraphColumns = new ArrayList();
                AllGraphIds = new StringBuffer("");
                for (int i = 0; i < graphIds.length; i++) {

                    AllGraphIds.append("," + graphIds[i]);
                    graphMapDetails[i] = (HashMap) getGraphHashMap().get(graphIds[i]);


                    //  //////////////.println("graphMapDetails[i]="+graphMapDetails[i]);



                    getGraphWiseDetailsMap(graphMapDetails[i]);
                    if (!isCheckingTimeSeries()) {

                        ////////////////.println("1111111111111111111111!isCheckingTimeSeries()");
                        grphMeasMap = new LinkedHashMap<String, String>();
                        setBarChartColumnTitles((String[]) graphMapDetails[i].get("barChartColumnTitles"));
                        setBarChartColumnNames((String[]) graphMapDetails[i].get("barChartColumnNames"));
                        setPieChartColumns((String[]) graphMapDetails[i].get("pieChartColumns"));
                        axis = ((String[]) graphMapDetails[i].get("axis"));
                        graphMapDetails[i].put("viewByElementIds", viewByElementIds);
                        for (int viewbyIndex = 0; viewbyIndex < viewByElementIds.length; viewbyIndex++) {

                            getBarChartColumnTitles()[viewbyIndex] = viewByColNames[viewbyIndex];
                            getBarChartColumnNames()[viewbyIndex] = viewByElementIds[viewbyIndex];
                            getPieChartColumns()[viewbyIndex] = viewByElementIds[viewbyIndex];
                            axis[viewbyIndex] = "0";
                        }
                        for (int j = viewByElementIds.length; j < getBarChartColumnNames().length; j++) {
                            if (!AllGraphColumns.contains(barChartColumnNames[j])) {
                                AllGraphColumns.add(getBarChartColumnNames()[j]);
                            }
                            measId = getBarChartColumnNames()[j];
                            measDispName = getBarChartColumnTitles()[j];
                            grphMeasMap.put(measId, measDispName);
                        }
                        this.noOfDays = noOfDays;
//                        this.graphProperty.setTargetPerDay(this.graphProperty.getStartValue()/30);
                        if (this.noOfDays != 0) {
                            this.graphProperty.setStartValue(this.graphProperty.getTargetPerDay() * this.noOfDays);
                        } else {
                            this.graphProperty.setStartValue(this.graphProperty.getTargetPerDay());
                        }
                        this.getGraphProperty().setStackedType(this.getGraphProperty().getStackedType());
                        JqplotGraphProperty graphproperty = new JqplotGraphProperty();
                        ;
                        HashMap singleGraphDetails = (HashMap) container.getGraphHashMap().get(graphId);
                        PbReportViewerDAO reportViewerdao = new PbReportViewerDAO();

                        if (singleGraphDetails != null && singleGraphDetails.get("jqgraphproperty" + graphId) != null) {
                            graphproperty = (JqplotGraphProperty) singleGraphDetails.get("jqgraphproperty" + graphId);
                            selectedgraphtype = graphproperty.getSlectedGraphType(graphId);

                        } else {
                            graphproperty = reportViewerdao.getJqGraphDetails(graphId);
                            if (graphproperty != null) {
                                selectedgraphtype = graphproperty.getSlectedGraphType(graphId);
                            }
                        }
                        ArrayList ProgenChartDisplayList = buildGraphBuildingInfo(pcharts[i], pchartsZoom[i]);
                        pcharts[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(0);
                        pchartsZoom[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(1);
                        graphMapDetails[i].put("graphMeasures", grphMeasMap);
                        graphMapDetails[i] = buildGraphWiseDetailsMap(graphMapDetails[i]);
                        //GraphHashMap.put(graphId, graphMapDetails[i]);//which holds details of all graphs

                    }
                }
                if (!isCheckingTimeSeries()) {

                    // GraphHashMap.put("AllGraphColumns", AllGraphColumns);
                    if (AllGraphIds.toString().equalsIgnoreCase("")) {
                        //   GraphHashMap.put("graphIds", AllGraphIds.toString());
                    } else {
                        // GraphHashMap.put("graphIds", AllGraphIds.substring(1));
                    }
//  if (!path.equalsIgnoreCase("")) {
                    if (path != null && path.length() > 0) {
                        path = new StringBuilder(path.substring(1));
                    }
                    //            if (!graphTitle.equalsIgnoreCase("")) {
                    if (graphTitle != null && graphTitle.length() > 0) {
//                graphTitle = graphTitle.substring(1);
                        graphTitle = new StringBuilder(graphTitle.substring(1));
                    }


                    grpDetails.add(path);
                    grpDetails.add(graphTitle);
                    grpDetails.add(pcharts);
                    grpDetails.add(pathZoom);
                    grpDetails.add(pchartsZoom);
                    grpDetails.add(graphMapDetails);
                }
            } catch (Exception exception) {
                logger.error("Exception:", exception);
            }

            //added by k
//            if(Container.getResetGraphfromchartdataset()!=null)
//                {
//                    setGraphHashMap(Container.getResetGraphfromchartdataset());
//                    //////.println("kk->"+Container.getResetGraphfromchartdataset());
//                }

            //////.println("final supercheck"+getGraphHashMap());




        }

        return grpDetails;
    }

//    public ArrayList get2dGraphHeaders(String[] CTbarChartColumnNames, String[] CTbarChartColumnTitles, String[] CTviewByColumns,Container container) {
    public ArrayList get2dGraphHeaders(Container container) {
        //////.println("get2dGraphHeaders");


        ArrayList grpDetails = new ArrayList();
        try {
            if (getGraphHashMap() != null && getGraphHashMap().get("isGraphsExists") != null) {

                //////.println("100");
//                grpDetails = get2dGraphHeadersMapAtRT(CTbarChartColumnNames, CTbarChartColumnTitles, CTviewByColumns,container);
                grpDetails = get2dGraphHeadersMapAtRT(container);
            } else {

                if (GraphHashMap.get("isGraphsExists") == null) {
                    pbretObj = getPbReturnObject(getGrpHdrsQuery(getReportId()));
                    graphMapDetails = new HashMap[pbretObj.getRowCount()];
                    setGraphsCnt(pbretObj.getRowCount());

                    if (getGraphsCnt() != 0) {
                        GraphHashMap.put("isGraphsExists", "true");
                        AllGraphIds = new StringBuffer("");
                        AllGraphColumns = new ArrayList();
                        dbColumns = pbretObj.getColumnNames();
                        pcharts = new ProgenChartDisplay[getGraphsCnt()];
                        pchartsZoom = new ProgenChartDisplay[getGraphsCnt()];
                        for (int i = 0; i < getGraphsCnt(); i++) {
                            String sqlQuery = "select GRAPH_ID,ELEMENT_ID from PRG_AR_GRAPH_DETAILS where GRAPH_ID=" + pbretObj.getFieldValueString(i, dbColumns[0]);
                            PbDb pbDb = new PbDb();
                            PbReturnObject retObj = new PbReturnObject();
                            retObj = pbDb.execSelectSQL(sqlQuery);
                            String crosstabGraphColumnMeasure = "";
                            if (retObj.getRowCount() > 0) {
                                crosstabGraphColumnMeasure = String.valueOf(container.getReportCollect().getNonViewByMap().get("A_" + retObj.getFieldValueString(0, "ELEMENT_ID")));

                            }

                            graphMapDetails[i] = new HashMap();
                            AllGraphIds.append("," + pbretObj.getFieldValueString(i, dbColumns[0]));
                            getGraphWiseDetailsMapFromPbReturnObject(pbretObj, i, dbColumns);

                            graphId = pbretObj.getFieldValueString(i, dbColumns[0]);
                            graphName = pbretObj.getFieldValueString(i, dbColumns[1]);
                            if (!fromOneview) {
                                graphWidth = pbretObj.getFieldValueString(i, dbColumns[2]);
                                graphHeight = pbretObj.getFieldValueString(i, dbColumns[3]);
                            }
                            if (fromOneview) {
                                graphWidth1 = pbretObj.getFieldValueString(i, dbColumns[2]);
                                graphHeight1 = pbretObj.getFieldValueString(i, dbColumns[3]);
                            }
                            graphClassName = pbretObj.getFieldValueString(i, dbColumns[4]);
                            graphTypeName = pbretObj.getFieldValueString(i, dbColumns[5]);
                            graphSize = pbretObj.getFieldValueString(i, dbColumns[6]);
                            grplegendloc = pbretObj.getFieldValueString(i, dbColumns[7]);
                            grplegend = pbretObj.getFieldValueString(i, dbColumns[8]);
                            grpshox = pbretObj.getFieldValueString(i, dbColumns[9]);
                            grpshoy = pbretObj.getFieldValueString(i, dbColumns[10]);
                            grpdrill = pbretObj.getFieldValueString(i, dbColumns[11]);
                            grpbcolor = pbretObj.getFieldValueString(i, dbColumns[12]);
                            grpfcolor = pbretObj.getFieldValueString(i, dbColumns[13]);
                            grpdata = pbretObj.getFieldValueString(i, dbColumns[14]);
                            grpLYaxislabel = pbretObj.getFieldValueString(i, dbColumns[15]);
                            grpryaxislabel = pbretObj.getFieldValueString(i, dbColumns[16]);
                            grpbDomainaxislabel = pbretObj.getFieldValueString(i, dbColumns[16]);
//                            graphDisplayRows = (graphDisplayRows == null && "".equalsIgnoreCase(graphDisplayRows)) ? "" : String.valueOf(graphMapDetails[i].get("graphDisplayRows"));
//                            startindex="0";
//                            endindex=graphDisplayRows;//"10";

                            if (graphTypeName.equalsIgnoreCase("columnPie") || graphTypeName.equalsIgnoreCase("columnPie3D") || graphTypeName.equalsIgnoreCase("TimeSeries") || graphTypeName.equalsIgnoreCase("Scatter")) {
                                rowValuesList = getRowValuesList(pbretObj.getFieldValueClobString(i, dbColumns[17]));
                                if (rowValuesList == null) {
                                    rowValuesList = new ArrayList();
                                }
                            } else {
                                rowValuesList = new ArrayList();
                            }
                            showGT = pbretObj.getFieldValueString(i, dbColumns[18]);

                            //showlyAxis = pbretObj.getFieldValueString(i, dbColumns[18]);
                            String grpaPropetyString = "";
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                if (pbretObj.getFieldUnknown(i, 21) != null && !pbretObj.getFieldUnknown(i, 21).equalsIgnoreCase("") && pbretObj.getFieldUnknown(i, 22) != null && !pbretObj.getFieldUnknown(i, 22).equalsIgnoreCase("")) {
                                    grpaPropetyString = pbretObj.getFieldUnknown(i, 20) + pbretObj.getFieldUnknown(i, 21) + pbretObj.getFieldUnknown(i, 22);
                                } else if (pbretObj.getFieldUnknown(i, 21) != null && !pbretObj.getFieldUnknown(i, 21).equalsIgnoreCase("")) {
                                    grpaPropetyString = pbretObj.getFieldUnknown(i, 20) + pbretObj.getFieldUnknown(i, 21);
                                } else {
                                    grpaPropetyString = pbretObj.getFieldUnknown(i, 20);
                                }
                            } else {
                                if (pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1") != null && !pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1").equalsIgnoreCase("") && pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML2") != null && !pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML2").equalsIgnoreCase("")) {
                                    grpaPropetyString = pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML") + pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1") + pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML2");
                                } else if (pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1") != null && !pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1").equalsIgnoreCase("")) {
                                    grpaPropetyString = pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML") + pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1");
                                } else {
                                    grpaPropetyString = pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML");
                                }
                            }
                            setGraphProperty(this.parseGraphPropertyXml(grpaPropetyString));
//                            if(pbretObj.getFieldValueString(i, "SHOW_LABEL")!=null && !"".equalsIgnoreCase(pbretObj.getFieldValueString(i, "SHOW_LABEL")))
//                               isShowLabels=Boolean.valueOf(pbretObj.getFieldValueString(i, "SHOW_LABEL"));
//                            else
//                                isShowLabels=false;

                            setGraphId(graphId);

                            if (graphTypeName.equalsIgnoreCase("TimeSeries")) {

                                ////////////////.println("time series here ");
                                setTimeSeries(true);
                            }

                            if (!isCheckingTimeSeries()) {

                                // ////////////////.println("1111111isCheckingTimeSeries");
                                int whichMeasure = container.getViewByCount();
                                String crossTabColumnName = "";
                                for (int k = whichMeasure; k < container.getReportMeasureCount() + container.getViewByCount(); k++) {
                                    ArrayList tempStr = (ArrayList) container.getDisplayLabels().get(k);
                                    if (crosstabGraphColumnMeasure.equalsIgnoreCase((String) tempStr.get(1))) {
                                        crossTabColumnName = container.getDisplayColumns().get(k);
                                        whichMeasure = container.getDisplayColumns().indexOf(crossTabColumnName);
                                        container.getGrpIdElementIdMap().put(graphId, crossTabColumnName);//@@@ added by swati
                                        break;
                                    }
                                }



                                if (container.getGrpIdElementIdMap().get(graphId) != null) {
                                    whichMeasure = container.getDisplayColumns().indexOf(container.getGrpIdElementIdMap().get(graphId));
                                }
                                container.getGrpIdElementIdMap().put(graphId, container.getGrpIdElementIdMap().get(graphId));

                                //getGraphProperty().setShowxAxis(LabelStr.get(1).toString());


                                //this.setBarChartColumnTitles(CTbarChartColumnTitles);
                                //this.setBarChartColumnNames(CTbarChartColumnNames);
                                //this.setPieChartColumns(CTbarChartColumnNames);
//                                this.viewByElementIds = CTviewByColumns;
                                setGraphColumnNamesAndTitles(container, graphId);
                                axis = new String[barChartColumnNames.length];
                                for (int j = 0; j < axis.length; j++) {
                                    axis[j] = "0";
                                }
                                JqplotGraphProperty graphproperty = new JqplotGraphProperty();
                                HashMap singleGraphDetails = (HashMap) container.getGraphHashMap().get(graphId);
                                PbReportViewerDAO reportViewerdao = new PbReportViewerDAO();

                                if (singleGraphDetails != null && singleGraphDetails.get("jqgraphproperty" + graphId) != null) {
                                    graphproperty = (JqplotGraphProperty) singleGraphDetails.get("jqgraphproperty" + graphId);
                                    selectedgraphtype = graphproperty.getSlectedGraphType(graphId);

                                } else {
                                    graphproperty = reportViewerdao.getJqGraphDetails(graphId);
                                    if (graphproperty != null) {
                                        selectedgraphtype = graphproperty.getSlectedGraphType(graphId);
                                    }
                                }
                                ArrayList ProgenChartDisplayList = buildGraphBuildingInfo(pcharts[i], pchartsZoom[i]);

                                pcharts[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(0);
                                pchartsZoom[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(1);
                                graphMapDetails[i] = buildGraphWiseDetailsMap(graphMapDetails[i]);
                                GraphHashMap.put(graphId, graphMapDetails[i]);//which holds details of all graphs

                                getGraphHashMap().put(graphId, graphMapDetails[i]);//which holds details of all graphs

                            }
                        }
                        if (!isCheckingTimeSeries()) {

                            //////////////////.println("22222222222isCheckingTimeSeries");


                            getGraphHashMap().put("AllGraphColumns", AllGraphColumns);

                            if (AllGraphIds.toString().equalsIgnoreCase("")) {
                                getGraphHashMap().put("graphIds", AllGraphIds.toString());

                            } else {
                                getGraphHashMap().put("graphIds", AllGraphIds.substring(1));
                            }

//                              if (!path.equalsIgnoreCase("")) {
                            if (path != null && path.length() > 0) {
                                path = new StringBuilder(path.substring(1));
                            }
                            //            if (!graphTitle.equalsIgnoreCase("")) {
                            if (graphTitle != null && graphTitle.length() > 0) {
//                graphTitle = graphTitle.substring(1);
                                graphTitle = new StringBuilder(graphTitle.substring(1));
                            }


                            grpDetails.add(path);
                            grpDetails.add(graphTitle);
                            grpDetails.add(pcharts);

                            grpDetails.add(pathZoom);
                            grpDetails.add(pchartsZoom);
                            grpDetails.add(graphMapDetails);
                        }

                    } else {
                        GraphHashMap.put("isGraphsExists", "false");
                    }
                } else {
                }
            }

            return grpDetails;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return grpDetails;
        }
    }

//    public ArrayList get2dGraphHeadersMapAtRT(String[] CTbarChartColumnNames, String[] CTbarChartColumnTitles, String[] CTviewByColumns,Container container) {
    public ArrayList get2dGraphHeadersMapAtRT(Container container) {
        //////.println("get2dGraphHeadersMapAtRT");

        ArrayList grpDetails = new ArrayList();
        if (getGraphHashMap().get("graphIds") != null) {

            String[] graphIds = String.valueOf(getGraphHashMap().get("graphIds")).split(",");
            pcharts = new ProgenChartDisplay[graphIds.length];
            pchartsZoom = new ProgenChartDisplay[graphIds.length];
            graphMapDetails = new HashMap[graphIds.length];
            try {
                pcharts = new ProgenChartDisplay[graphIds.length];
                pchartsZoom = new ProgenChartDisplay[graphIds.length];
                graphMapDetails = new HashMap[graphIds.length];
                AllGraphColumns = new ArrayList();
                AllGraphIds = new StringBuffer("");
                for (int i = 0; i < graphIds.length; i++) {
                    //////.println("444");
                    AllGraphIds.append("," + graphIds[i]);
                    graphMapDetails[i] = (HashMap) getGraphHashMap().get(graphIds[i]);
                    getGraphWiseDetailsMap(graphMapDetails[i]);
                    if (!isCheckingTimeSeries()) {
                        //this.setBarChartColumnTitles(CTbarChartColumnTitles);
                        //this.setBarChartColumnNames(CTbarChartColumnNames);
                        //this.setPieChartColumns(CTbarChartColumnNames);
                        //this.viewByElementIds = CTviewByColumns;
                        setGraphColumnNamesAndTitles(container, graphIds[i]);
                        axis = new String[this.barChartColumnNames.length];

                        for (int j = 0; j < axis.length; j++) {
                            axis[j] = "0";
                        }
                        for (int j = viewByElementIds.length; j < getBarChartColumnNames().length; j++) {
                            if (!AllGraphColumns.contains(barChartColumnNames[j])) {
                                AllGraphColumns.add(getBarChartColumnNames()[j]);
                            }
                        }
                        JqplotGraphProperty graphproperty = new JqplotGraphProperty();
                        ;
                        HashMap singleGraphDetails = (HashMap) container.getGraphHashMap().get(graphId);
                        PbReportViewerDAO reportViewerdao = new PbReportViewerDAO();

                        if (singleGraphDetails != null && singleGraphDetails.get("jqgraphproperty" + graphId) != null) {
                            graphproperty = (JqplotGraphProperty) singleGraphDetails.get("jqgraphproperty" + graphId);
                            selectedgraphtype = graphproperty.getSlectedGraphType(graphId);

                        } else {
                            graphproperty = reportViewerdao.getJqGraphDetails(graphId);
                            if (graphproperty != null) {
                                selectedgraphtype = graphproperty.getSlectedGraphType(graphId);
                            }
                        }
                        ArrayList ProgenChartDisplayList = buildGraphBuildingInfo(pcharts[i], pchartsZoom[i]);
                        pcharts[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(0);
                        pchartsZoom[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(1);
                        graphMapDetails[i] = buildGraphWiseDetailsMap(graphMapDetails[i]);
                        //getGraphHashMap().put(graphId, graphMapDetails[i]);//which holds details of all graphs//commented by k and added below
                        //////.println("supercheck"+getGraphHashMap());


                    }
                }
                if (!isCheckingTimeSeries()) {
                    getGraphHashMap().put("AllGraphColumns", AllGraphColumns);
                    if (AllGraphIds.toString().equalsIgnoreCase("")) {
                        getGraphHashMap().put("graphIds", AllGraphIds.toString());
                        //////.println("111 supercheck"+getGraphHashMap());
                    } else {
                        getGraphHashMap().put("graphIds", AllGraphIds.substring(1));
                        //////.println("222 supercheck"+getGraphHashMap());
                    }

//                      if (!path.equalsIgnoreCase("")) {
                    if (path != null && path.length() > 0) {
                        path = new StringBuilder(path.substring(1));
                    }
                    //            if (!graphTitle.equalsIgnoreCase("")) {
                    if (graphTitle != null && graphTitle.length() > 0) {
//                graphTitle = graphTitle.substring(1);
                        graphTitle = new StringBuilder(graphTitle.substring(1));
                    }

                    grpDetails.add(path);
                    grpDetails.add(graphTitle);
                    grpDetails.add(pcharts);
                    grpDetails.add(pathZoom);
                    grpDetails.add(pchartsZoom);
                    grpDetails.add(graphMapDetails);
                }

//                if(Container.getResetGraphfromchartdataset()!=null)
//                {
//                    setGraphHashMap(Container.getResetGraphfromchartdataset());
//                    //////.println("kk->"+Container.getResetGraphfromchartdataset());
//                }

                //////.println("final supercheck"+getGraphHashMap());

            } catch (Exception exception) {
                logger.error("Exception:", exception);

            }
        }

        return grpDetails;
    }

    //added by santhosh.kumar@progenbusiness.com on 14-12-2009 for FX Charts
    public String getMinMaxStepValues(ArrayList values) {
        MathContext mc = new MathContext(2);
        String str = "";


        double maxVal = Double.parseDouble((String) values.get(0));

        for (int i = 0; i < values.size(); i++) {
            if (Double.parseDouble((String) values.get(i)) > maxVal) {
                maxVal = Double.parseDouble((String) values.get(i));
            }
        }
        //get Minimum Value
        double minVal = Double.parseDouble((String) values.get(0));

        for (int i = 0; i < values.size(); i++) {
            if (Double.parseDouble((String) values.get(i)) < minVal) {
                minVal = Double.parseDouble((String) values.get(i));
            }
        }
        //get Step Value/Size
        double stepSize = maxVal / 10;

        str = "[" + new BigDecimal(minVal).round(mc) + "," + new BigDecimal(maxVal).round(mc) + "," + new BigDecimal(stepSize).round(mc) + "]";
        return str;
    }

    public String getViewBy(HashMap hmap, String[] viewByColumns) {
        int colCount = viewByColumns.length;
//        String viewBy = "";
        StringBuilder viewBy = new StringBuilder(300);
        Object obj = null;
        String temp = null;
        for (int i = 0; i < colCount; i++) {
            obj = hmap.get(viewByColumns[i]);
            if (obj == null) {
                obj = hmap.get("A_" + viewByColumns[i]);
            }
            temp = obj.toString();

            if (temp != null) {
                temp = temp.replace(",", "");//handle to remove ',' inside view bys
            }
            if (i == (colCount - 1)) {
//                viewBy = viewBy + temp;
                viewBy.append(temp);
            } else {
//                viewBy = viewBy + temp + " - ";
                viewBy.append(temp).append(" - ");
            }
        }
        return viewBy.toString();
    }

    public StringBuffer getFXChart(HashMap singleRecord) {
        StringBuffer sbuffer = null;
        sbuffer = getFxDataSet(singleRecord);
        return sbuffer;
    }

    public HashMap getMinMaxStepValues(ArrayList[] seriesList, HashMap singleRecord, NumberFormat nFormat) {
        MathContext mc = new MathContext(2);
        String str = "";
        if (seriesList != null && seriesList.length != 0 && seriesList[0] != null) {
            double maxVal = Double.parseDouble((String) seriesList[0].get(0));
            double minVal = Double.parseDouble((String) seriesList[0].get(0));

            for (int k = 0; k < seriesList.length; k++) {
                for (int i = 0; i < seriesList[k].size(); i++) {
                    if (Double.parseDouble((String) seriesList[k].get(i)) > maxVal) {
                        maxVal = Double.parseDouble((String) seriesList[k].get(i));
                    }
                    if (Double.parseDouble((String) seriesList[k].get(i)) < minVal) {
                        minVal = Double.parseDouble((String) seriesList[k].get(i));
                    }
                }
            }
            double stepSize = maxVal / 10;
            maxVal = Math.ceil(maxVal + stepSize);

            singleRecord.put("maxVal", new BigDecimal(maxVal).round(mc));// new BigDecimal(maxVal).round(mc)
            singleRecord.put("minVal", new BigDecimal(minVal).round(mc));//new BigDecimal(minVal).round(mc)
            singleRecord.put("stepSize", new BigDecimal(stepSize).round(mc));//new BigDecimal(stepSize).round(mc)
        }

        return singleRecord;
    }

    public void buildURLS(HashMap singleRecord, ArrayList[] seriesList) {
        ArrayList[] url_values = new ArrayList[seriesList.length];
    }

    public String[] getPieChartColumns() {
        return pieChartColumns;
    }

    public void setPieChartColumns(String[] pieChartColumns) {
        this.pieChartColumns = pieChartColumns;
    }

    public String[] getBarChartColumnNames() {
        return barChartColumnNames;
    }

    public void setBarChartColumnNames(String[] barChartColumnNames) {
        this.barChartColumnNames = barChartColumnNames;
    }

    public String[] getBarChartColumnTitles() {
        return barChartColumnTitles;
    }

    public void setBarChartColumnTitles(String[] barChartColumnTitles) {
        this.barChartColumnTitles = barChartColumnTitles;
    }

    public String[] getBarChartColumnNames1() {
        return barChartColumnNames1;
    }

    public String[] getBarChartColumnTitles1() {
        return barChartColumnTitles1;
    }

    public String[] getBarChartColumnNames2() {
        return barChartColumnNames2;
    }

    public String[] getBarChartColumnTitles2() {
        return barChartColumnTitles2;
    }

    public void setBarChartColumnNames1(String[] barChartColumnNames1) {
        this.barChartColumnNames1 = barChartColumnNames1;
    }

    public void setBarChartColumnTitles1(String[] barChartColumnTitles1) {
        this.barChartColumnTitles1 = barChartColumnTitles1;
    }

    public void setBarChartColumnNames2(String[] barChartColumnNames2) {
        this.barChartColumnNames2 = barChartColumnNames2;
    }

    public void setBarChartColumnTitles2(String[] barChartColumnTitles2) {
        this.barChartColumnTitles2 = barChartColumnTitles2;
    }

    public StringBuffer getFXChartStringBuffer(StringBuffer sbuffer) {
        return sbuffer;
    }

    public pbDashboardCollection getCollect() {
        return collect;
    }

    public void setCollect(pbDashboardCollection collect) {
        this.collect = collect;
    }

    public ArrayList getAllDispRecords() {
        return allDispRecords;
    }

    public void setAllDispRecords(ArrayList allDispRecords) {
        this.allDispRecords = allDispRecords;
    }

    public ArrayList getRowValuesList(String strXML) {
        //Reader characterStream = null;
        Clob clob = null;
        ArrayList alist = new ArrayList();
        Document document = null;
        Element root = null;
        Element Companyname = null;
        Element paramElement = null;
        SAXBuilder builder = new SAXBuilder();
        List singleSeriesList = null;
        List row = null;
        if (strXML != null) {
            try {
                //characterStream = clob.getCharacterStream();
                //document = builder.build(characterStream);
                document = builder.build(new ByteArrayInputStream(strXML.getBytes()));
                root = document.getRootElement();
                row = root.getChildren("row-values");
                for (int k = 0; k < row.size(); k++) {
                    Companyname = (Element) row.get(k);
                    singleSeriesList = Companyname.getChildren("row-value");

                    for (int l = 0; l < singleSeriesList.size(); l++) {
                        paramElement = (Element) singleSeriesList.get(l);
                        alist.add(paramElement.getText());
                    }
                }
            } catch (Exception ex) {
                alist = new ArrayList();
                logger.error("Exception:", ex);
            }
        }
        return alist;
    }

    public String getShowGT() {
        return showGT;
    }

    public void setShowGT(String showGT) {
        this.showGT = showGT;
    }

    public ArrayList getCurrentDispRecordsWithGT() {
        return currentDispRecordsWithGT;
    }

    public void setCurrentDispRecordsWithGT(ArrayList currentDispRecordsWithGT) {
        this.currentDispRecordsWithGT = currentDispRecordsWithGT;
    }

    public String getTimelevel() {
        return timelevel;
    }

    public void setTimelevel(String timelevel) {
        this.timelevel = timelevel;
    }

    public String getScatterViewBy() {
        return scatterViewBy;
    }

    public void setScatterViewBy(String scatterViewBy) {
        this.scatterViewBy = scatterViewBy;
    }

    public boolean getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(boolean timeSeries) {
        this.timeSeries = timeSeries;
    }

    public boolean isCheckingTimeSeries() {
        return checkingTimeSeries;
    }

    public void setCheckingTimeSeries(boolean checkingTimeSeries) {
        this.checkingTimeSeries = checkingTimeSeries;
    }

    public String getGraphGridLines() {
        return graphGridLines;
    }

    public void setGraphGridLines(String graphGridLines) {
        this.graphGridLines = graphGridLines;
    }

    public void checkGraphHeaders() {

        try {
            if (getGraphHashMap() != null && !getGraphHashMap().isEmpty()) {
                checkGraphHeadersMapAtRT();
            } else {
                pbretObj = getPbReturnObject(getGrpHdrsQuery(getReportId()));
                setGraphsCnt(pbretObj.getRowCount());
                String[] tableDBColumns = pbretObj.getColumnNames();
                pcharts = new ProgenChartDisplay[getGraphsCnt()];
                pchartsZoom = new ProgenChartDisplay[getGraphsCnt()];
                graphMapDetails = new HashMap[pbretObj.getRowCount()];
                AllGraphIds = new StringBuffer("");
                AllGraphColumns = new ArrayList();
                for (int i = 0; i < getGraphsCnt(); i++) {
                    AllGraphIds.append("," + pbretObj.getFieldValueString(i, tableDBColumns[0]));
                    graphMapDetails[i] = new HashMap();
                    getGraphWiseDetailsMapFromPbReturnObject(pbretObj, i, tableDBColumns);
                }

//                if (AllGraphIds.toString().equalsIgnoreCase("")) {
//                    getGraphHashMap().put("graphIds", AllGraphIds.toString());
//                } else {
//                    getGraphHashMap().put("graphIds", AllGraphIds.substring(1));
//                }
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
    }

    public void checkGraphHeadersMapAtRT() {

        String[] graphIds = String.valueOf(getGraphHashMap().get("graphIds")).split(",");
        pcharts = new ProgenChartDisplay[graphIds.length];
        pchartsZoom = new ProgenChartDisplay[graphIds.length];
        graphMapDetails = new HashMap[graphIds.length];

        try {
            AllGraphColumns = new ArrayList();
            AllGraphIds = new StringBuffer("");

            for (int i = 0; i < graphIds.length; i++) {
                AllGraphIds.append("," + graphIds[i]);
                graphMapDetails[i] = (HashMap) getGraphHashMap().get(graphIds[i]);
                if (graphMapDetails[i] != null && !graphMapDetails[i].isEmpty()) {
                    getGraphWiseDetailsMap(graphMapDetails[i]);
                }
            }
//            if (AllGraphIds.toString().equalsIgnoreCase("")) {
//                getGraphHashMap().put("graphIds", AllGraphIds.toString());
//            } else {
//                getGraphHashMap().put("graphIds", AllGraphIds.substring(1));
//            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }

    }

    public void check2dGraphHeaders(String[] CTbarChartColumnNames, String[] CTbarChartColumnTitles, String[] CTviewByColumns) {

        try {
            if (getGraphHashMap() != null && getGraphHashMap().size() != 0) {
                check2dGraphHeadersMapAtRT(CTbarChartColumnNames, CTbarChartColumnTitles, CTviewByColumns);
            } else {
                pbretObj = getPbReturnObject(getGrpHdrsQuery(getReportId()));
                setGraphsCnt(pbretObj.getRowCount());
                AllGraphIds = new StringBuffer("");
                AllGraphColumns = new ArrayList();
                dbColumns = pbretObj.getColumnNames();
                pcharts = new ProgenChartDisplay[getGraphsCnt()];
                pchartsZoom = new ProgenChartDisplay[getGraphsCnt()];
                for (int i = 0; i < getGraphsCnt(); i++) {
                    AllGraphIds.append("," + pbretObj.getFieldValueString(i, dbColumns[0]));
                    getGraphWiseDetailsMapFromPbReturnObject(pbretObj, i, dbColumns);
                }
//                if (AllGraphIds.toString().equalsIgnoreCase("")) {
//                    GraphHashMap.put("graphIds", AllGraphIds.toString());
//                } else {
//                    GraphHashMap.put("graphIds", AllGraphIds.substring(1));
//                }
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
    }

    public void check2dGraphHeadersMapAtRT(String[] CTbarChartColumnNames, String[] CTbarChartColumnTitles, String[] CTviewByColumns) {
        String[] graphIds = String.valueOf(getGraphHashMap().get("graphIds")).split(",");
        graphMapDetails = new HashMap[graphIds.length];
        try {
            graphMapDetails = new HashMap[graphIds.length];
            AllGraphIds = new StringBuffer("");
            AllGraphColumns = new ArrayList();
            for (int i = 0; i < graphIds.length; i++) {
                AllGraphIds.append("," + graphIds[i]);
                graphMapDetails[i] = (HashMap) getGraphHashMap().get(graphIds[i]);
                if (graphMapDetails[i] != null && !graphMapDetails[i].isEmpty()) {
                    getGraphWiseDetailsMap(graphMapDetails[i]);
                }
            }
//            if (AllGraphIds.toString().equalsIgnoreCase("")) {
//                getGraphHashMap().put("graphIds", AllGraphIds.toString());
//            } else {
//                getGraphHashMap().put("graphIds", AllGraphIds.substring(1));
//            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);

        }
    }

    public ArrayList getFxGraphHeadersByGraphMap(HashMap GraphDetails) {
        ArrayList grpDetails = new ArrayList();
        try {
            getGraphWiseDetailsMap(GraphDetails);
            setBarChartColumnTitles((String[]) GraphDetails.get("barChartColumnTitles"));
            setBarChartColumnNames((String[]) GraphDetails.get("barChartColumnNames"));
            setPieChartColumns((String[]) GraphDetails.get("pieChartColumns"));
            axis = ((String[]) GraphDetails.get("axis"));
            viewByElementIds = ((String[]) GraphDetails.get("viewByElementIds"));
            SwapColumn = (graphTypeName.equalsIgnoreCase("ColumnPie") || graphTypeName.equalsIgnoreCase("ColumnPie3D")) ? !SwapColumn : SwapColumn;

            setBarChartColumnNames1((String[]) GraphDetails.get("barChartColumnNames1"));
            setBarChartColumnTitles1((String[]) GraphDetails.get("barChartColumnTitles1"));
            setBarChartColumnNames2((String[]) GraphDetails.get("barChartColumnNames2"));
            setBarChartColumnTitles2((String[]) GraphDetails.get("barChartColumnTitles2"));




            if ((getBarChartColumnNames1() == null || getBarChartColumnNames1().length == 0) && (getBarChartColumnNames2() == null || getBarChartColumnNames2().length == 0)) {
                int allColumns = getBarChartColumnNames().length - viewByElementIds.length;
                int dividor = 2;
                int rightColumns = allColumns / dividor;
                int leftColumns = allColumns - rightColumns;
                setBarChartColumnTitles1(new String[leftColumns + viewByElementIds.length]);
                setBarChartColumnNames1(new String[getBarChartColumnTitles1().length]);
                setBarChartColumnTitles2(new String[viewByElementIds.length + rightColumns]);
                setBarChartColumnNames2(new String[getBarChartColumnTitles2().length]);

                for (int j = 0; j < viewByElementIds.length; j++) {
                    getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                    getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j];
                    getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                    getBarChartColumnNames2()[j] = getBarChartColumnNames()[j];
                }
                for (int j = viewByElementIds.length; j < getBarChartColumnTitles1().length; j++) {
                    getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                    getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                }
                for (int j = viewByElementIds.length; j < getBarChartColumnTitles2().length; j++) {
                    getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j + leftColumns];
                    getBarChartColumnNames2()[j] = getBarChartColumnNames()[j + leftColumns];
                }
            }
            buildGraphWiseDetailsMap(GraphDetails);

        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return grpDetails;
    }

    public String getGrplegend() {
        return grplegend;
    }

    public void setGrplegend(String grplegend) {
        this.grplegend = grplegend;
    }

    public String getGrplegendloc() {
        return grplegendloc;
    }

    public void setGrplegendloc(String grplegendloc) {
        this.grplegendloc = grplegendloc;
    }

    public String getGrpshox() {
        return grpshox;
    }

    public void setGrpshox(String grpshox) {
        this.grpshox = grpshox;
    }

    public String getGrpshoy() {
        return grpshoy;
    }

    public void setGrpshoy(String grpshoy) {
        this.grpshoy = grpshoy;
    }

    public String getGrpdrill() {
        return grpdrill;
    }

    public void setGrpdrill(String grpdrill) {
        this.grpdrill = grpdrill;
    }

    public String getGrpbcolor() {
        return grpbcolor;
    }

    public void setGrpbcolor(String grpbcolor) {
        this.grpbcolor = grpbcolor;
    }

    public String getGrpfcolor() {
        return grpfcolor;
    }

    public void setGrpfcolor(String grpfcolor) {
        this.grpfcolor = grpfcolor;
    }

    public String getGrpdata() {
        return grpdata;
    }

    public void setGrpdata(String grpdata) {
        this.grpdata = grpdata;
    }

    public String getGraphDisplayRows() {
        return graphDisplayRows;
    }

    public void setGraphDisplayRows(String graphDisplayRows) {
        this.graphDisplayRows = graphDisplayRows;
    }

    public ProgenDataSet getCurrentDispRecordsRetObjWithGT() {
        return currentDispRecordsRetObjWithGT;
    }

    public void setCurrentDispRecordsRetObjWithGT(PbReturnObject currentDispRecordsRetObjWithGT) {
        this.currentDispRecordsRetObjWithGT = currentDispRecordsRetObjWithGT;
    }

    public ProgenDataSet getCurrentDispRetObjRecords() {
        return currentDispRetObjRecords;
    }

    public void setCurrentDispRetObjRecords(ProgenDataSet currentDispRetObjRecords) {
        this.currentDispRetObjRecords = currentDispRetObjRecords;
    }

    public ProgenDataSet getAllDispRecordsRetObj() {
        return allDispRecordsRetObj;
    }

    public void setAllDispRecordsRetObj(ProgenDataSet allDispRecordsRetObj) {
        this.allDispRecordsRetObj = allDispRecordsRetObj;
    }

    public ArrayList getGraphHeadersFX() {
        ArrayList grpDetails = new ArrayList();
        LinkedHashMap<String, String> grphMeasMap;
        String measId;
        String measDispName;


        try {
            if (getGraphHashMap() != null && getGraphHashMap().get("isGraphsExists") != null) {
                grpDetails = getGraphHeadersMapAtRTFX();
            } else {

                if (GraphHashMap.get("isGraphsExists") == null) {
                    pbretObj = getPbReturnObject(getGrpHdrsQueryFX(getReportId()));
                    setGraphsCnt(pbretObj.getRowCount());

                    if (getGraphsCnt() != 0) {
                        String[] tableDBColumns = pbretObj.getColumnNames();
                        GraphHashMap.put("isGraphsExists", "true");
                        pcharts = new ProgenChartDisplay[getGraphsCnt()];
                        pchartsZoom = new ProgenChartDisplay[getGraphsCnt()];
                        graphMapDetails = new HashMap[pbretObj.getRowCount()];
                        if (GraphHashMap == null) {
                            GraphHashMap = new HashMap();
                        }
                        if (AllGraphIds == null) {
                            AllGraphIds = new StringBuffer("");
                        }
                        if (AllGraphColumns == null || AllGraphColumns.size() == 0) {
                            AllGraphColumns = new ArrayList();
                        }
                        for (int i = 0; i < getGraphsCnt(); i++) {
                            grphMeasMap = new LinkedHashMap<String, String>();
                            AllGraphIds.append("," + pbretObj.getFieldValueString(i, tableDBColumns[0]));
                            graphMapDetails[i] = new HashMap();

                            getGraphWiseDetailsMapFromPbReturnObject(pbretObj, i, tableDBColumns);
                            pbretObj2 = getPbReturnObject(getGrpDtlsQuery(graphId));
                            setBarChartColumnTitles(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                            setBarChartColumnNames(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                            setPieChartColumns(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                            axis = new String[getViewByElementIds().length + pbretObj2.getRowCount()];
                            String[] graphDBColumns = pbretObj2.getColumnNames();
                            for (int viewbyIndex = 0; viewbyIndex < viewByElementIds.length; viewbyIndex++) {
                                getBarChartColumnTitles()[viewbyIndex] = viewByColNames[viewbyIndex];
                                getBarChartColumnNames()[viewbyIndex] = viewByElementIds[viewbyIndex];
                                getPieChartColumns()[viewbyIndex] = viewByElementIds[viewbyIndex];
                                axis[viewbyIndex] = "0";
                            }
                            for (int j = 0; j < pbretObj2.getRowCount(); j++) {
                                getBarChartColumnTitles()[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[1]);
                                getBarChartColumnNames()[viewByElementIds.length + j] = "A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                                getPieChartColumns()[viewByElementIds.length + j] = "A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                                axis[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[2]);

                                if (!AllGraphColumns.contains("A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]))) {
                                    AllGraphColumns.add("A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]));
                                }
                                measId = "A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                                measDispName = pbretObj2.getFieldValueString(j, graphDBColumns[1]);
                                grphMeasMap.put(measId, measDispName);


                            }

//                            String axis1 = "";
//                            String axis2 = "";
                            StringBuilder axis1 = new StringBuilder(1000);
                            StringBuilder axis2 = new StringBuilder(1000);
                            if (axis != null) {
                                for (int ind = viewByElementIds.length; ind < axis.length; ind++) {
                                    //if (axis[ind] == null) {
                                    //axis[ind] = "0";
                                    //}
                                    if (axis[ind].equalsIgnoreCase("0")) {
//                                        axis1 = axis1 + "," + ind;
                                        axis1.append(",").append(ind);
                                    } else {
//                                        axis2 = axis2 + "," + ind;
                                        axis2.append(",").append(ind);
                                    }
                                }
                            }


//                            if ((!axis1.equalsIgnoreCase("")) && (!axis2.equalsIgnoreCase(""))) {
                            if ((axis1.length() > 0) && (axis2.length()) > 0) {
//                                axis1 = axis1.substring(1);
//                                axis2 = axis2.substring(1);
                                axis1 = new StringBuilder(axis1.substring(1));
                                axis2 = new StringBuilder(axis2.substring(1));
                                String[] temp1 = axis1.toString().split(",");
                                setBarChartColumnTitles1(new String[temp1.length + viewByElementIds.length]);
                                setBarChartColumnNames1(new String[getBarChartColumnTitles1().length]);
                                String[] temp2 = axis2.toString().split(",");
                                setBarChartColumnTitles2(new String[temp2.length + viewByElementIds.length]);
                                setBarChartColumnNames2(new String[getBarChartColumnTitles2().length]);
                                for (int j = 0; j < viewByElementIds.length; j++) {
                                    getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                                    getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j];

                                    getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                                    getBarChartColumnNames2()[j] = getBarChartColumnNames()[j];
                                }
                                for (int j = 0; j < temp1.length; j++) {
                                    getBarChartColumnTitles1()[viewByElementIds.length + j] = getBarChartColumnTitles()[Integer.parseInt(temp1[j])];
                                    getBarChartColumnNames1()[viewByElementIds.length + j] = getBarChartColumnNames()[Integer.parseInt(temp1[j])];
                                }
                                for (int j = 0; j < temp2.length; j++) {
                                    getBarChartColumnTitles2()[viewByElementIds.length + j] = getBarChartColumnTitles()[Integer.parseInt(temp2[j])];
                                    getBarChartColumnNames2()[viewByElementIds.length + j] = getBarChartColumnNames()[Integer.parseInt(temp2[j])];
                                }
                            } else {
                                int allColumns = getBarChartColumnNames().length - viewByElementIds.length;
                                int dividor = 2;
                                int rightColumns = allColumns / dividor;
                                int leftColumns = allColumns - rightColumns;
                                setBarChartColumnTitles1(new String[leftColumns + viewByElementIds.length]);
                                setBarChartColumnNames1(new String[getBarChartColumnTitles1().length]);
                                setBarChartColumnTitles2(new String[viewByElementIds.length + rightColumns]);
                                setBarChartColumnNames2(new String[getBarChartColumnTitles2().length]);

                                for (int j = 0; j < viewByElementIds.length; j++) {
                                    getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                                    getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j];
                                    getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                                    getBarChartColumnNames2()[j] = getBarChartColumnNames()[j];
                                }
                                for (int j = viewByElementIds.length; j < getBarChartColumnTitles1().length; j++) {
                                    getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                                    getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                                }
                                for (int j = viewByElementIds.length; j < getBarChartColumnTitles2().length; j++) {
                                    getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j + leftColumns];
                                    getBarChartColumnNames2()[j] = getBarChartColumnNames()[j + leftColumns];
                                }
                            }

//                            path = path + ";" + "";
                            path.append(";").append("");
//                            graphTitle = graphTitle + ";" + graphName;
                            graphTitle.append(";").append(graphName);
//                            pathZoom = pathZoom + ";" + "";
                            pathZoom.append(";").append("");

                            graphMapDetails[i] = buildGraphWiseDetailsMap(graphMapDetails[i]);
                            graphMapDetails[i].put("graphMeasures", grphMeasMap);
                            GraphHashMap.put(graphId, graphMapDetails[i]);//which holds details of all graphs
                        }

                        //if (!isCheckingTimeSeries()) {
                        GraphHashMap.put("AllGraphColumns", AllGraphColumns);

                        if (AllGraphIds.toString().equalsIgnoreCase("")) {
                            GraphHashMap.put("graphIds", AllGraphIds.toString());
                        } else {
                            GraphHashMap.put("graphIds", AllGraphIds.substring(1));
                        }

                        grpDetails.add(path);
                        grpDetails.add(graphTitle);
                        grpDetails.add(pcharts);

                        grpDetails.add(pathZoom);
                        grpDetails.add(pchartsZoom);
                        grpDetails.add(graphMapDetails);
                        //}
                    } else {
                        GraphHashMap.put("isGraphsExists", "false");
                    }
                }
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return grpDetails;
    }

    public ArrayList getGraphHeadersMapAtRTFX() {
        ArrayList grpDetails = new ArrayList();
        if (getGraphHashMap().get("graphIds") != null) {
            String[] graphIds = String.valueOf(getGraphHashMap().get("graphIds")).split(",");
            pcharts = new ProgenChartDisplay[graphIds.length];
            pchartsZoom = new ProgenChartDisplay[graphIds.length];
            graphMapDetails = new HashMap[graphIds.length];
            try {
                AllGraphColumns = new ArrayList();
                AllGraphIds = new StringBuffer("");

                for (int i = 0; i < graphIds.length; i++) {
                    AllGraphIds.append("," + graphIds[i]);
                    graphMapDetails[i] = (HashMap) getGraphHashMap().get(graphIds[i]);

                    getGraphWiseDetailsMap(graphMapDetails[i]);
                    //if (!isCheckingTimeSeries()) {
                    setBarChartColumnTitles((String[]) graphMapDetails[i].get("barChartColumnTitles"));
                    setBarChartColumnNames((String[]) graphMapDetails[i].get("barChartColumnNames"));
                    setPieChartColumns((String[]) graphMapDetails[i].get("pieChartColumns"));
                    axis = ((String[]) graphMapDetails[i].get("axis"));
                    graphMapDetails[i].put("viewByElementIds", viewByElementIds);

                    for (int viewbyIndex = 0; viewbyIndex < viewByElementIds.length; viewbyIndex++) {



                        getBarChartColumnTitles()[viewbyIndex] = viewByColNames[viewbyIndex];
                        getBarChartColumnNames()[viewbyIndex] = viewByElementIds[viewbyIndex];
                        getPieChartColumns()[viewbyIndex] = viewByElementIds[viewbyIndex];
                        axis[viewbyIndex] = "0";
                    }

                    for (int j = viewByElementIds.length; j < getBarChartColumnNames().length; j++) {
                        if (!AllGraphColumns.contains(barChartColumnNames[j])) {
                            AllGraphColumns.add(getBarChartColumnNames()[j]);
                        }
                    }
//                    path = path + ";" + "";
                    path.append(";").append("");
//                    graphTitle = graphTitle + ";" + graphName;
                    graphTitle.append(";").append(graphName);
//                    pathZoom = pathZoom + ";" + "";
                    pathZoom.append(";").append("");

                    graphMapDetails[i] = buildGraphWiseDetailsMap(graphMapDetails[i]);
                    GraphHashMap.put(graphId, graphMapDetails[i]);//which holds details of all graphs



                    //}
                }
                //if (!isCheckingTimeSeries()) {
                GraphHashMap.put("AllGraphColumns", AllGraphColumns);
                if (AllGraphIds.toString().equalsIgnoreCase("")) {
                    GraphHashMap.put("graphIds", AllGraphIds.toString());
                } else {
                    GraphHashMap.put("graphIds", AllGraphIds.substring(1));
                }

                grpDetails.add(path);
                grpDetails.add(graphTitle);
                grpDetails.add(pcharts);
                grpDetails.add(pathZoom);
                grpDetails.add(pchartsZoom);
                grpDetails.add(graphMapDetails);
                //}
            } catch (Exception exception) {
                logger.error("Exception:", exception);
            }
        }
        return grpDetails;
    }

    public ArrayList get2dGraphHeadersFX(String[] CTbarChartColumnNames, String[] CTbarChartColumnTitles, String[] CTviewByColumns) {

        ArrayList grpDetails = new ArrayList();
        String[] graphsXML = null;
        try {
            if (getGraphHashMap() != null && getGraphHashMap().get("isGraphsExists") != null) {
                grpDetails = get2dGraphHeadersMapAtRTFX(CTbarChartColumnNames, CTbarChartColumnTitles, CTviewByColumns);
            } else {
                if (GraphHashMap.get("isGraphsExists") == null) {
                    pbretObj = getPbReturnObject(getGrpHdrsQueryFX(getReportId()));
                    graphMapDetails = new HashMap[pbretObj.getRowCount()];
                    setGraphsCnt(pbretObj.getRowCount());

                    if (getGraphsCnt() != 0) {
                        GraphHashMap.put("isGraphsExists", "true");
                        AllGraphIds = new StringBuffer("");
                        AllGraphColumns = new ArrayList();
                        dbColumns = pbretObj.getColumnNames();
                        pcharts = new ProgenChartDisplay[getGraphsCnt()];
                        pchartsZoom = new ProgenChartDisplay[getGraphsCnt()];
                        graphsXML = new String[getGraphsCnt()];
                        for (int i = 0; i < getGraphsCnt(); i++) {
                            graphMapDetails[i] = new HashMap();
                            AllGraphIds.append("," + pbretObj.getFieldValueString(i, dbColumns[0]));
                            getGraphWiseDetailsMapFromPbReturnObject(pbretObj, i, dbColumns);
                            //if (!isCheckingTimeSeries()) {

                            setBarChartColumnTitles(CTbarChartColumnTitles);
                            setBarChartColumnNames(CTbarChartColumnNames);
                            setPieChartColumns(CTbarChartColumnNames);
                            viewByElementIds = CTviewByColumns;
                            axis = new String[CTbarChartColumnNames.length];
                            for (int j = 0; j < axis.length; j++) {
                                axis[j] = "0";
                            }
                            int allColumns = getBarChartColumnNames().length - viewByElementIds.length;
                            int dividor = 2;
                            int rightColumns = allColumns / dividor;
                            int leftColumns = allColumns - rightColumns;
                            setBarChartColumnTitles1(new String[leftColumns + viewByElementIds.length]);
                            setBarChartColumnNames1(new String[getBarChartColumnTitles1().length]);
                            setBarChartColumnTitles2(new String[viewByElementIds.length + rightColumns]);
                            setBarChartColumnNames2(new String[getBarChartColumnTitles2().length]);

                            for (int j = 0; j < viewByElementIds.length; j++) {
                                getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                                getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j];
                                getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                                getBarChartColumnNames2()[j] = getBarChartColumnNames()[j];
                            }
                            for (int j = viewByElementIds.length; j < getBarChartColumnTitles1().length; j++) {
                                getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                                getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                            }
                            for (int j = viewByElementIds.length; j < getBarChartColumnTitles2().length; j++) {
                                getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j + leftColumns];
                                getBarChartColumnNames2()[j] = getBarChartColumnNames()[j + leftColumns];
                            }

//                            path = path + ";" + "";
                            path.append(";").append("");
//                            graphTitle = graphTitle + ";" + graphName;
                            graphTitle.append(";").append(graphName);
//                            pathZoom = pathZoom + ";" + "";
                            pathZoom.append(";").append("");

                            graphMapDetails[i] = buildGraphWiseDetailsMap(graphMapDetails[i]);
                            GraphHashMap.put(graphId, graphMapDetails[i]);//which holds details of all graphs

                            //}
                        }
                        //if (!isCheckingTimeSeries()) {
                        getGraphHashMap().put("AllGraphColumns", AllGraphColumns);
                        if (AllGraphIds.toString().equalsIgnoreCase("")) {
                            getGraphHashMap().put("graphIds", AllGraphIds.toString());
                        } else {
                            getGraphHashMap().put("graphIds", AllGraphIds.substring(1));
                        }
                        grpDetails.add(path);
                        grpDetails.add(graphTitle);
                        grpDetails.add(pcharts);
                        grpDetails.add(pathZoom);
                        grpDetails.add(pchartsZoom);
                        grpDetails.add(graphMapDetails);
                        // }
                    } else {
                        GraphHashMap.put("isGraphsExists", "false");
                    }
                } else {
                }
            }
            return grpDetails;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return grpDetails;
        }
    }

    public ArrayList get2dGraphHeadersMapAtRTFX(String[] CTbarChartColumnNames, String[] CTbarChartColumnTitles, String[] CTviewByColumns) {


        ArrayList grpDetails = new ArrayList();
        if (getGraphHashMap().get("graphIds") != null) {
            String[] graphIds = String.valueOf(getGraphHashMap().get("graphIds")).split(",");
            pcharts = new ProgenChartDisplay[graphIds.length];
            pchartsZoom = new ProgenChartDisplay[graphIds.length];
            graphMapDetails = new HashMap[graphIds.length];
            try {
                pcharts = new ProgenChartDisplay[graphIds.length];
                pchartsZoom = new ProgenChartDisplay[graphIds.length];
                graphMapDetails = new HashMap[graphIds.length];
                AllGraphColumns = new ArrayList();
                AllGraphIds = new StringBuffer("");
                for (int i = 0; i < graphIds.length; i++) {
                    AllGraphIds.append("," + graphIds[i]);
                    graphMapDetails[i] = (HashMap) getGraphHashMap().get(graphIds[i]);
                    getGraphWiseDetailsMap(graphMapDetails[i]);
                    //if (!isCheckingTimeSeries()) {
                    this.setBarChartColumnTitles(CTbarChartColumnTitles);
                    this.setBarChartColumnNames(CTbarChartColumnNames);
                    this.setPieChartColumns(CTbarChartColumnNames);
                    this.viewByElementIds = CTviewByColumns;
                    axis = new String[CTbarChartColumnNames.length];
                    for (int j = 0; j < axis.length; j++) {
                        axis[j] = "0";
                    }
                    for (int j = viewByElementIds.length; j < getBarChartColumnNames().length; j++) {
                        if (!AllGraphColumns.contains(barChartColumnNames[j])) {
                            AllGraphColumns.add(getBarChartColumnNames()[j]);
                        }
                    }
                    int allColumns = getBarChartColumnNames().length - viewByElementIds.length;
                    int dividor = 2;
                    int rightColumns = allColumns / dividor;
                    int leftColumns = allColumns - rightColumns;
                    setBarChartColumnTitles1(new String[leftColumns + viewByElementIds.length]);
                    setBarChartColumnNames1(new String[getBarChartColumnTitles1().length]);
                    setBarChartColumnTitles2(new String[viewByElementIds.length + rightColumns]);
                    setBarChartColumnNames2(new String[getBarChartColumnTitles2().length]);

                    for (int j = 0; j < viewByElementIds.length; j++) {
                        getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                        getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j];
                        getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                        getBarChartColumnNames2()[j] = getBarChartColumnNames()[j];
                    }
                    for (int j = viewByElementIds.length; j < getBarChartColumnTitles1().length; j++) {
                        getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                        getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                    }
                    for (int j = viewByElementIds.length; j < getBarChartColumnTitles2().length; j++) {
                        getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j + leftColumns];
                        getBarChartColumnNames2()[j] = getBarChartColumnNames()[j + leftColumns];
                    }
//                    path = path + ";" + "";
                    path.append(";").append("");
//                    graphTitle = graphTitle + ";" + graphName;
                    graphTitle.append(";").append(graphName);
//                    pathZoom = pathZoom + ";" + "";
                    pathZoom.append(";").append("");

                    graphMapDetails[i] = buildGraphWiseDetailsMap(graphMapDetails[i]);
                    GraphHashMap.put(graphId, graphMapDetails[i]);//which holds details of all graphs

                    //}
                }
                //if (!isCheckingTimeSeries()) {
                getGraphHashMap().put("AllGraphColumns", AllGraphColumns);
                if (AllGraphIds.toString().equalsIgnoreCase("")) {
                    getGraphHashMap().put("graphIds", AllGraphIds.toString());

                } else {
                    getGraphHashMap().put("graphIds", AllGraphIds.substring(1));
                }
                grpDetails.add(path);
                grpDetails.add(graphTitle);
                grpDetails.add(pcharts);
                grpDetails.add(pathZoom);
                grpDetails.add(pchartsZoom);
                grpDetails.add(graphMapDetails);
                // }

            } catch (Exception exception) {
                logger.error("Exception:", exception);

            }
        }
        return grpDetails;
    }

    public void BuilFXDataset() {
    }

    public StringBuffer getFxDataSet(HashMap singleRecord) {


        String columnviewby = String.valueOf(singleRecord.get("columnviewby"));

        ////.println("columnviewby=" + columnviewby);
        ProgenDataSet localRetObj = null;
        //HashMap hashMap = null;
        String temp = null;
        String temp1 = null;
        StringBuffer sbuffer = new StringBuffer("");
        int tempCount = 0;
        int tempCount1 = 0;
        int tempCount2 = 0;
        ArrayList viewByValues = new ArrayList();
        ArrayList seriesNames = new ArrayList();
        NumberFormat nFormat = NumberFormat.getInstance();
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);
        Object obj = null;
        Object obj1 = null;
        Object barObj = null;
        Object lineObj = null;
        ArrayList[] seriesList = null;
        ArrayList[] urlValuesList = null;

        ArrayList[] lineseriesList = null;
        ArrayList[] lineurlValuesList = null;
        ArrayList[] barseriesList = null;
        ArrayList[] barurlValuesList = null;

        ArrayList barseriesNames = new ArrayList();
        ArrayList lineseriesNames = new ArrayList();

        ArrayList RowValuesList = (ArrayList) singleRecord.get("rowValuesList");
        ////.println("RowValuesList="+RowValuesList);

        //if (RowValuesList == null) {
        // RowValuesList = new ArrayList();
        //}
        boolean MultiAxisFlag = false;
        if (graphTypeName == null || graphTypeName.equalsIgnoreCase("null")) {
            graphTypeName = "Bar";
        }

        if (graphTypeName.equalsIgnoreCase("Dual Axis") || graphTypeName.equalsIgnoreCase("OverlaidBar") || graphTypeName.equalsIgnoreCase("TargetBar") || graphTypeName.equalsIgnoreCase("HorizontalTargetBar")) {
            MultiAxisFlag = true;
        }

        ////.println("graphTypeName=" + graphTypeName);
        if (graphTypeName.equalsIgnoreCase("ColumnPie") || graphTypeName.equalsIgnoreCase("TimeSeries ") || graphTypeName.equalsIgnoreCase("Bubble")) {
            if (getShowGT().equalsIgnoreCase("Y")) {
                ////.println("entered");
                ////.println("999");
                localRetObj = getAllDispRecordsRetObj();
            } else {
                ////.println("88888");
                localRetObj = getAllDispRecordsRetObj();
            }
        } else {

            if (getShowGT().equalsIgnoreCase("Y")) {

                localRetObj = getCurrentDispRecordsRetObjWithGT();
            } else {

                localRetObj = getCurrentDispRetObjRecords();
            }
        }

        ////.println("localRetObj.getRowCount()=" + localRetObj.getRowCount());
        //boolean exists = memberValueExists(localRetObj, RowValuesList);
        int graphDispRows = (singleRecord.get("graphDispRows") != null && !singleRecord.get("graphDispRows").equals("") && !"null".equalsIgnoreCase(singleRecord.get("graphDispRows").toString())) ? Integer.parseInt(singleRecord.get("graphDispRows").toString()) : 10;
        int size = (localRetObj.getRowCount() < graphDispRows) ? localRetObj.getRowCount() : graphDispRows;
        ////.println("graphDispRows=" + graphDispRows);

        if (graphTypeName.equalsIgnoreCase("ColumnPie")) {
            size = (size != 0) ? 1 : size;
        }
        try {
            if (getBarChartColumnNames() != null && getViewByElementIds() != null) {
                tempCount = getBarChartColumnNames().length - getViewByElementIds().length;
                if (tempCount > 10) {
                    tempCount = 10;//to restrict no fo graph columns max columns is 10(including view bys)
                }
                seriesList = new ArrayList[tempCount];
                urlValuesList = new ArrayList[tempCount];
                if (MultiAxisFlag) {
                    if (getBarChartColumnNames1() != null) {
                        tempCount1 = getBarChartColumnNames1().length - getViewByElementIds().length;

                        if (tempCount1 > 10) {
                            tempCount1 = 10;//to restrict no fo graph columns max columns is 10(including view bys)
                        }

                        barseriesList = new ArrayList[tempCount1];
                        barurlValuesList = new ArrayList[tempCount1];
                    }
                    if (getBarChartColumnNames2() != null) {
                        tempCount2 = getBarChartColumnNames2().length - getViewByElementIds().length;
                        if (tempCount2 > 10) {
                            tempCount2 = 10;//to restrict no fo graph columns max columns is 10(including view bys)
                        }
                        lineseriesList = new ArrayList[tempCount2];
                        lineurlValuesList = new ArrayList[tempCount2];
                    }


                }

                ////.println("size=" + size);
                if (localRetObj != null) {
                    for (int i = 0; i < localRetObj.getRowCount(); i++) {
                        ////.println("localRetObj" + localRetObj.getFieldValueString(i, 0));
                    }


                }


                for (int k = 0; k < getViewByElementIds().length; k++) {
                    ////.println("getViewByElementIds()=" + getViewByElementIds()[k]);
                }


                for (int k = 0; k < getBarChartColumnNames().length; k++) {
                    ////.println("getBarChartColumnNames()=" + getBarChartColumnNames()[k]);
                }
                for (int k = 0; k < getBarChartColumnNames1().length; k++) {
                    ////.println("getBarChartColumnNames1()=" + getBarChartColumnNames1()[k]);
                }
                for (int k = 0; k < getBarChartColumnNames2().length; k++) {
                    ////.println("getBarChartColumnNames2()=" + getBarChartColumnNames2()[k]);
                }








                for (int index = 0; index < size; index++) {

                    ////.println("@@@index=" + index);
                    // ////.println("@@@getBarChartColumnNames1()[i]="+getBarChartColumnNames1()[index]);
                    viewByValues.add(getViewBy(localRetObj, getViewByElementIds(), index));
                    for (int i = getViewByElementIds().length; i < (getViewByElementIds().length + tempCount); i++) {
                        String barchartcname = "";
                        if (columnviewby.equalsIgnoreCase("null") || columnviewby.equalsIgnoreCase("0")) {
                            if (!(getBarChartColumnNames()[i]).contains("A_")) {
                                barchartcname = "A_" + getBarChartColumnNames()[i];
                            } else {
                                barchartcname = getBarChartColumnNames()[i];
                            }
                        } else {
                            barchartcname = getBarChartColumnNames()[i];
                        }
                        ////.println("@@@barchartcname" + barchartcname);

                        obj = localRetObj.getFieldValue(index, barchartcname);
                        obj1 = localRetObj.getFieldValue(index, getViewByElementIds()[0]);
                        if (obj != null) {
                            temp = obj.toString();
                        } else {
                            temp = "0.0";
                        }
                        if (obj1 != null) {
                            temp1 = obj1.toString();
                        } else {
                            temp1 = "";
                        }
                        if (seriesList[i - getViewByElementIds().length] == null) {
                            seriesList[i - getViewByElementIds().length] = new ArrayList();
                            urlValuesList[i - getViewByElementIds().length] = new ArrayList();
                        }
                        seriesList[i - getViewByElementIds().length].add(temp);
                        urlValuesList[i - getViewByElementIds().length].add(temp1);
                    }
                    if (MultiAxisFlag) {
                        for (int i = getViewByElementIds().length; i < (getViewByElementIds().length + tempCount1); i++) {

                            String barchartcname1 = "";
                            if (columnviewby.equalsIgnoreCase("null") || columnviewby.equalsIgnoreCase("0")) {
                                if (!(getBarChartColumnNames1()[i]).contains("A_")) {
                                    barchartcname1 = "A_" + getBarChartColumnNames1()[i];
                                } else {
                                    barchartcname1 = getBarChartColumnNames1()[i];
                                }
                            } else {
                                barchartcname1 = getBarChartColumnNames1()[i];
                            }

                            ////.println("###index=" + index);
                            ////.println("####getBarChartColumnNames1()[i]=" + barchartcname1);



                            barObj = localRetObj.getFieldValue(index, barchartcname1);
                            obj1 = localRetObj.getFieldValue(index, getViewByElementIds()[0]);
                            if (obj != null) {
                                temp = barObj.toString();
                            } else {
                                temp = "0.0";
                            }
                            if (obj1 != null) {
                                temp1 = obj1.toString();
                            } else {
                                temp1 = "";
                            }

                            if (barseriesList != null) {
                                if (barseriesList[i - getViewByElementIds().length] == null) {
                                    barseriesList[i - getViewByElementIds().length] = new ArrayList();
                                    barurlValuesList[i - getViewByElementIds().length] = new ArrayList();
                                }
                                barseriesList[i - getViewByElementIds().length].add(temp);
                                barurlValuesList[i - getViewByElementIds().length].add(temp1);
                            }
                        }
                        for (int i = getViewByElementIds().length; i < (getViewByElementIds().length + tempCount2); i++) {

                            String barchartcname2 = "";
                            if (columnviewby.equalsIgnoreCase("null") || columnviewby.equalsIgnoreCase("0")) {
                                if (!(getBarChartColumnNames2()[i]).contains("A_")) {
                                    barchartcname2 = "A_" + getBarChartColumnNames2()[i];
                                } else {
                                    barchartcname2 = getBarChartColumnNames2()[i];
                                }
                            } else {
                                barchartcname2 = getBarChartColumnNames2()[i];
                            }
                            lineObj = localRetObj.getFieldValue(index, barchartcname2);
                            obj1 = localRetObj.getFieldValue(index, getViewByElementIds()[0]);

                            if (obj != null) {
                                temp = lineObj.toString();
                            } else {
                                temp = "0.0";
                            }
                            if (obj1 != null) {
                                temp1 = obj1.toString();
                            } else {
                                temp1 = "";
                            }
                            if (lineseriesList[i - getViewByElementIds().length] == null) {
                                lineseriesList[i - getViewByElementIds().length] = new ArrayList();
                                lineurlValuesList[i - getViewByElementIds().length] = new ArrayList();
                            }
                            lineseriesList[i - getViewByElementIds().length].add(temp);
                            lineurlValuesList[i - getViewByElementIds().length].add(temp1);
                        }
                    }
                }
                singleRecord = getMinMaxStepValues(seriesList, singleRecord, nFormat);
                for (int i = getViewByElementIds().length; i < getBarChartColumnTitles().length; i++) {
                    seriesNames.add(getBarChartColumnTitles()[i]);
                }

                if (MultiAxisFlag) {
                    for (int i = getViewByElementIds().length; i < getBarChartColumnTitles1().length; i++) {

                        barseriesNames.add(getBarChartColumnTitles1()[i]);
                    }
                    for (int i = getViewByElementIds().length; i < getBarChartColumnTitles2().length; i++) {

                        lineseriesNames.add(getBarChartColumnTitles2()[i]);
                    }
                    singleRecord.put("barseries_names", barseriesNames);
                    singleRecord.put("lineseries_names", lineseriesNames);
                    singleRecord.put("barurl_values", barurlValuesList);
                    singleRecord.put("lineurl_values", lineurlValuesList);
                    singleRecord.put("bary_values", barseriesList);
                    singleRecord.put("liney_values", lineseriesList);
                }
                singleRecord.put("series_names", seriesNames);
                singleRecord.put("x_values", viewByValues);
                singleRecord.put("y_values", seriesList);
                singleRecord.put("url_values", urlValuesList);



            }
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }


        return sbuffer;
    }

    //added by santhosh.kumar@progenbusiness.com on 15-12-2009 for swapping graph analysis to measure by
    public StringBuffer getFxDataSetForMeasureAnalysis(HashMap singleRecord) {
        ProgenDataSet localRetObj = null;
        String temp = null;
        String temp1 = null;
        StringBuffer sbuffer = new StringBuffer("");
        NumberFormat nFormat = NumberFormat.getInstance();
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);
        ArrayList[] seriesList = null;
        ArrayList viewByValues = null;
        ArrayList seriesNames = null;
        ArrayList[] urlValuesList = null;

        if (graphTypeName.equalsIgnoreCase("ColumnPie") || graphTypeName.equalsIgnoreCase("TimeSeries ") || graphTypeName.equalsIgnoreCase("Bubble")) {
            if (getShowGT().equalsIgnoreCase("Y")) {
                localRetObj = getAllDispRecordsRetObj();
            } else {
                localRetObj = getAllDispRecordsRetObj();
            }

        } else {
            if (getShowGT().equalsIgnoreCase("Y")) {
                localRetObj = getCurrentDispRecordsRetObjWithGT();
            } else {
                localRetObj = getCurrentDispRetObjRecords();
            }
        }
        int graphDispRows = (singleRecord.get("graphDispRows") != null && !"null".equalsIgnoreCase(singleRecord.get("graphDispRows").toString())) ? Integer.parseInt(singleRecord.get("graphDispRows").toString()) : 10;
        int size = (localRetObj.getRowCount() < graphDispRows) ? localRetObj.getRowCount() : graphDispRows;
        size = (graphTypeName.equalsIgnoreCase("ColumnPie") && size != 0) ? 1 : size;

        int tempCount = size;
        seriesList = new ArrayList[tempCount];
        viewByValues = new ArrayList();
        seriesNames = new ArrayList();
        urlValuesList = new ArrayList[tempCount];
        Object obj = null;
        Object obj1 = null;

        for (int index = 0; index < tempCount; index++) {
            seriesNames.add(getViewBy(localRetObj, viewByElementIds, index));

            for (int i = viewByElementIds.length; i < (getBarChartColumnNames().length); i++) {
                obj = localRetObj.getFieldValue(index, getBarChartColumnNames()[i]);
                obj1 = localRetObj.getFieldValue(index, getBarChartColumnNames()[0]);
                if (obj != null) {
                    temp = obj.toString();
                } else {
                    temp = "0.0";
                }
                if (obj1 != null) {
                    temp1 = obj1.toString();
                } else {
                    temp1 = "0.0";
                }
                if (seriesList[index] == null) {
                    seriesList[index] = new ArrayList();
                    urlValuesList[index] = new ArrayList();
                }
                seriesList[index].add(temp);
                urlValuesList[index].add(temp1);
            }
        }
        singleRecord = getMinMaxStepValues(seriesList, singleRecord, nFormat);
        for (int i = viewByElementIds.length; i < getBarChartColumnTitles().length; i++) {
            viewByValues.add(getBarChartColumnTitles()[i]);
        }
        singleRecord.put("x_label", "");
        singleRecord.put("l_y_label", "");
        singleRecord.put("r_y_label", "");
        singleRecord.put("series_names", seriesNames);
        singleRecord.put("x_values", viewByValues);
        singleRecord.put("y_values", seriesList);
        singleRecord.put("url_values", urlValuesList);




        return sbuffer;
    }

    public String getViewBy(ProgenDataSet retObj, String[] viewByColumns, int index) {
        int colCount = viewByColumns.length;
        StringBuffer viewBy = new StringBuffer();
        for (int i = 0; i < colCount; i++) {


            if (i == (colCount - 1)) {
                viewBy.append(retObj.getFieldValueString(index, viewByColumns[i]));
            } else {
                viewBy.append(retObj.getFieldValueString(index, viewByColumns[i]) + " - ");
            }
        }

        return viewBy.toString();
    }

    public ArrayList getDashboardGraphHeadersNewFx(String reportId, String graphId) {
        ArrayList grpDetails = new ArrayList();
        try {
            ////.println("reportId" + reportId);
            ////.println("graphId" + graphId);

            Object[] Obj1 = new Object[2];
            Obj1[0] = reportId;
            Obj1[1] = graphId;
            String FinalQuery = buildQuery(resBundle.getString("grpHdrsQueryByGraphIdFX"), Obj1);

            pbretObj = getPbReturnObject(FinalQuery);


            ////.println("row count" + pbretObj.getRowCount());
            ////.println("column count" + pbretObj.getColumnCount());

            String[] tableDBColumns = pbretObj.getColumnNames();
            setGraphsCnt(pbretObj.getRowCount());
            pcharts = new ProgenChartDisplay[getGraphsCnt()];
            pchartsZoom = new ProgenChartDisplay[getGraphsCnt()];
            graphMapDetails = new HashMap[pbretObj.getRowCount()];


            for (int i = 0; i < getGraphsCnt(); i++) {

                ////.println("inside for loop");
                graphMapDetails[i] = new HashMap();
                graphId = pbretObj.getFieldValueString(i, tableDBColumns[0]);
                graphName = pbretObj.getFieldValueString(i, tableDBColumns[1]);
                graphWidth = "420";
                graphHeight = "280";
                graphClassName = pbretObj.getFieldValueString(i, tableDBColumns[4]);
                graphTypeName = pbretObj.getFieldValueString(i, tableDBColumns[5]);

                if (getMyGraphType() != null) {
                    if (getMyGraphType() != null || getMyGraphType().equalsIgnoreCase("")) {
                        if (getMyGraphType().equalsIgnoreCase("Bar")
                                || getMyGraphType().equalsIgnoreCase("bar3d")
                                || getMyGraphType().equalsIgnoreCase("line")
                                || getMyGraphType().equalsIgnoreCase("line3d")
                                || getMyGraphType().equalsIgnoreCase("stacked")
                                || getMyGraphType().equalsIgnoreCase("stackedarea")
                                || getMyGraphType().equalsIgnoreCase("stacked3d")
                                || getMyGraphType().equalsIgnoreCase("column")
                                || getMyGraphType().equalsIgnoreCase("column3d")
                                || getMyGraphType().equalsIgnoreCase("waterfall")
                                || getMyGraphType().equalsIgnoreCase("Area")
                                || getMyGraphType().equalsIgnoreCase("HorizontalStacked")
                                || getMyGraphType().equalsIgnoreCase("HorizontalStacked3d")
                                || getMyGraphType().equalsIgnoreCase("Layered Bar")) {
                            graphClassName = "Category";
                            graphTypeName = getMyGraphType();
                        }

                        if (getMyGraphType().equalsIgnoreCase("pie") || getMyGraphType().equalsIgnoreCase("pie3d") || getMyGraphType().equalsIgnoreCase("ring") || getMyGraphType().equalsIgnoreCase("ColumnPie") || getMyGraphType().equalsIgnoreCase("ColumnPie3D")) {
                            graphClassName = "Pie";
                            graphTypeName = getMyGraphType();
                        }

                        if (getMyGraphType().equalsIgnoreCase("Dual Axis") || getMyGraphType().equalsIgnoreCase("OverlaidBar") || getMyGraphType().equalsIgnoreCase("OverlaidArea") || getMyGraphType().equalsIgnoreCase("Pareto") || getMyGraphType().equalsIgnoreCase("Spider") || getMyGraphType().equalsIgnoreCase("Layered Bar")) {
                            graphClassName = "Dual Axis";
                            graphTypeName = getMyGraphType();
                        }


                        if (getMyGraphType().equalsIgnoreCase("Meter") || getMyGraphType().equalsIgnoreCase("Thermometer")) {
                            graphClassName = "Meter";
                            graphTypeName = getMyGraphType();
                        }
                        if (getMyGraphType().equalsIgnoreCase("HorizCone")) {
                            graphClassName = "HorizCone";
                            graphTypeName = getMyGraphType();
                        }

                        if (getMyGraphType().equalsIgnoreCase("Pyramid")) {
                            graphClassName = "Pyramid";
                            graphTypeName = getMyGraphType();
                        }
                        if (getMyGraphType().equalsIgnoreCase("Cone")) {
                            graphClassName = "Cone";
                            graphTypeName = getMyGraphType();
                        }
                        if (getMyGraphType().equalsIgnoreCase("PieRing")) {
                            graphClassName = "Pie";
                            graphTypeName = getMyGraphType();
                        }

//Dual Axis
                    }


                }










                graphSize = pbretObj.getFieldValueString(i, tableDBColumns[6]);
                grplegendloc = pbretObj.getFieldValueString(i, tableDBColumns[7]);
                grplegend = pbretObj.getFieldValueString(i, tableDBColumns[8]);
                grpshox = pbretObj.getFieldValueString(i, tableDBColumns[9]);
                grpshoy = pbretObj.getFieldValueString(i, tableDBColumns[10]);
                grpdrill = pbretObj.getFieldValueString(i, tableDBColumns[11]);
                grpbcolor = pbretObj.getFieldValueString(i, tableDBColumns[12]);
                grpfcolor = pbretObj.getFieldValueString(i, tableDBColumns[13]);
                grpdata = pbretObj.getFieldValueString(i, tableDBColumns[14]);
                grpLYaxislabel = pbretObj.getFieldValueString(i, tableDBColumns[15]);
                grpryaxislabel = pbretObj.getFieldValueString(i, tableDBColumns[16]);
                grpbDomainaxislabel = pbretObj.getFieldValueString(i, tableDBColumns[16]);
                String grpaPropetyString = "";
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    if (pbretObj.getFieldUnknown(i, 21) != null && !pbretObj.getFieldUnknown(i, 21).equalsIgnoreCase("") && pbretObj.getFieldUnknown(i, 22) != null && !pbretObj.getFieldUnknown(i, 22).equalsIgnoreCase("")) {
                        grpaPropetyString = pbretObj.getFieldUnknown(i, 20) + pbretObj.getFieldUnknown(i, 21) + pbretObj.getFieldUnknown(i, 22);
                    } else if (pbretObj.getFieldUnknown(i, 21) != null && !pbretObj.getFieldUnknown(i, 21).equalsIgnoreCase("")) {
                        grpaPropetyString = pbretObj.getFieldUnknown(i, 20) + pbretObj.getFieldUnknown(i, 21);
                    } else {
                        grpaPropetyString = pbretObj.getFieldUnknown(i, 20);
                    }
                } else {
                    if (pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1") != null && !pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1").equalsIgnoreCase("") && pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML2") != null && !pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML2").equalsIgnoreCase("")) {
                        grpaPropetyString = pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML") + pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1") + pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML2");
                    } else if (pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1") != null && !pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1").equalsIgnoreCase("")) {
                        grpaPropetyString = pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML") + pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1");
                    } else {
                        grpaPropetyString = pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML");
                    }
                }
                setGraphProperty(this.parseGraphPropertyXml(grpaPropetyString));


//                if(pbretObj.getFieldValueString(i, "SHOW_LABEL")!=null && !"".equalsIgnoreCase(pbretObj.getFieldValueString(i, "SHOW_LABEL")))
//                      isShowLabels=Boolean.valueOf(pbretObj.getFieldValueString(i, "SHOW_LABEL"));
//                  else
//                      isShowLabels=false;
                setGraphId(graphId);

                pbretObj2 = getPbReturnObject(getGrpDtlsQuery(graphId));
                setBarChartColumnTitles(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                setBarChartColumnNames(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                setPieChartColumns(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                axis = new String[getViewByElementIds().length + pbretObj2.getRowCount()];
                String[] graphDBColumns = pbretObj2.getColumnNames();
                for (int viewbyIndex = 0; viewbyIndex < viewByElementIds.length; viewbyIndex++) {
                    getBarChartColumnTitles()[viewbyIndex] = viewByColNames[viewbyIndex];
                    getBarChartColumnNames()[viewbyIndex] = viewByElementIds[viewbyIndex];
                    getPieChartColumns()[viewbyIndex] = viewByElementIds[viewbyIndex];
                    axis[viewbyIndex] = "0";
                }
                for (int j = 0; j < pbretObj2.getRowCount(); j++) {
                    getBarChartColumnTitles()[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[1]);
                    getBarChartColumnNames()[viewByElementIds.length + j] = "A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                    getPieChartColumns()[viewByElementIds.length + j] = "A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                    axis[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[2]);
                }

//                            String axis1 = "";
//                            String axis2 = "";
                StringBuilder axis1 = new StringBuilder(1000);
                StringBuilder axis2 = new StringBuilder(1000);
                if (axis != null) {
                    for (int ind = viewByElementIds.length; ind < axis.length; ind++) {
                        //if (axis[ind] == null) {
                        //axis[ind] = "0";
                        //}
                        if (axis[ind].equalsIgnoreCase("0")) {
//                                        axis1 = axis1 + "," + ind;
                            axis1.append(",").append(ind);
                        } else {
//                                        axis2 = axis2 + "," + ind;
                            axis2.append(",").append(ind);
                        }
                    }
                }
//                            if ((!axis1.equalsIgnoreCase("")) && (!axis2.equalsIgnoreCase(""))) {
                if ((axis1.length() > 0) && (axis2.length()) > 0) {
//                                axis1 = axis1.substring(1);
//                                axis2 = axis2.substring(1);
                    axis1 = new StringBuilder(axis1.substring(1));
                    axis2 = new StringBuilder(axis2.substring(1));


                    String[] temp1 = axis1.toString().split(",");

                    setBarChartColumnTitles1(new String[temp1.length + viewByElementIds.length]);
                    setBarChartColumnNames1(new String[getBarChartColumnTitles1().length]);

                    String[] temp2 = axis2.toString().split(",");
                    setBarChartColumnTitles2(new String[temp2.length + viewByElementIds.length]);
                    setBarChartColumnNames2(new String[getBarChartColumnTitles2().length]);

                    for (int j = 0; j < viewByElementIds.length; j++) {
                        getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                        getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j];

                        getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                        getBarChartColumnNames2()[j] = getBarChartColumnNames()[j];
                    }

                    for (int j = 0; j < temp1.length; j++) {
                        getBarChartColumnTitles1()[viewByElementIds.length + j] = getBarChartColumnTitles()[Integer.parseInt(temp1[j])];
                        getBarChartColumnNames1()[viewByElementIds.length + j] = getBarChartColumnNames()[Integer.parseInt(temp1[j])];
                    }
                    for (int j = 0; j < temp2.length; j++) {
                        getBarChartColumnTitles2()[viewByElementIds.length + j] = getBarChartColumnTitles()[Integer.parseInt(temp2[j])];
                        getBarChartColumnNames2()[viewByElementIds.length + j] = getBarChartColumnNames()[Integer.parseInt(temp2[j])];
                    }



                } else {
                    int count = 1;
                    setBarChartColumnTitles1(new String[count + viewByElementIds.length]);
                    setBarChartColumnNames1(new String[getBarChartColumnTitles1().length]);

                    setBarChartColumnTitles2(new String[getBarChartColumnNames().length - count]);
                    setBarChartColumnNames2(new String[getBarChartColumnTitles2().length]);

                    for (int j = 0; j < viewByElementIds.length; j++) {
                        getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                        getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j];
                        getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                        getBarChartColumnNames2()[j] = getBarChartColumnNames()[j];
                    }
                    for (int j = viewByElementIds.length; j < getBarChartColumnTitles1().length; j++) {
                        getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                        getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                    }

                    for (int j = getBarChartColumnTitles1().length; j < getBarChartColumnTitles().length; j++) {
                        getBarChartColumnTitles2()[j - viewByElementIds.length] = getBarChartColumnTitles()[j];
                        getBarChartColumnNames2()[j - viewByElementIds.length] = getBarChartColumnNames()[j];
                    }
                }
                //storing graph header info in HashMap
                buildGraphWiseDetailsMap(graphMapDetails[i]);
                //GraphHashMap.put(graphId, graphMapDetails[i]);//which holds details of all graphs

            }

        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        ////.println("grpDetails=" + grpDetails);
        return grpDetails;



    }

    //DashBoard related methods
    public ArrayList getGraphByGraphId(String repId, String graphId, HashMap ParametersMap) {
        ArrayList grpDetails = new ArrayList();
        ArrayList graphColumns = new ArrayList();
        ArrayList AllColumns = new ArrayList();
        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
        try {
            pbretObj = getPbReturnObject(getGrpHdrsQueryByGraphIdQuery(repId, graphId));
            String[] tableDBColumns = pbretObj.getColumnNames();
            setGraphsCnt(pbretObj.getRowCount());
            pcharts = new ProgenChartDisplay[getGraphsCnt()];
            pchartsZoom = new ProgenChartDisplay[getGraphsCnt()];
            String[] grpIds = null;
            graphMapDetails = new HashMap[pbretObj.getRowCount()];

            //////////////////.println("graphMapDetails="+graphMapDetails);
            grpIds = new String[pbretObj.getRowCount()];
            String[] viewBys = null;
            //////////////////.println("getGraphsCnt()="+getGraphsCnt());

            for (int i = 0; i < getGraphsCnt(); i++) {
                graphMapDetails[i] = new HashMap();

                //////////////////.println("graphMapDetails11111="+graphMapDetails[i]);
                grpIds[i] = pbretObj.getFieldValueString(i, tableDBColumns[0]);
                getGraphWiseDetailsMapFromPbReturnObject(pbretObj, i, tableDBColumns);

                setGraphId(graphId);
                pbretObj2 = getPbReturnObject(getGrpDtlsQuery(graphId));
                setBarChartColumnTitles(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                setBarChartColumnNames(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                setPieChartColumns(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                axis = new String[getViewByElementIds().length + pbretObj2.getRowCount()];
                String[] graphDBColumns = pbretObj2.getColumnNames();
                viewBys = viewByElementIds;
                for (int viewbyIndex = 0; viewbyIndex < viewByElementIds.length; viewbyIndex++) {

                    //////////////////.println("aaaaaaa");
                    if (viewByElementIds[viewbyIndex].equalsIgnoreCase("Time")) {
                        viewBys[viewbyIndex] = "Time";
                        //  viewBys[viewbyIndex] = viewByElementIds[viewbyIndex];

                    } else {
                        if (!(viewByElementIds[viewbyIndex].contains("A_"))) {
                            viewBys[viewbyIndex] = "A_" + viewByElementIds[viewbyIndex];
                        } else {
                            viewBys[viewbyIndex] = viewByElementIds[viewbyIndex];
                        }
                    }
                    getBarChartColumnTitles()[viewbyIndex] = viewByColNames[viewbyIndex];
                    getBarChartColumnNames()[viewbyIndex] = viewBys[viewbyIndex];
                    getPieChartColumns()[viewbyIndex] = viewBys[viewbyIndex];
                    axis[viewbyIndex] = "0";
                }
                for (int j = 0; j < pbretObj2.getRowCount(); j++) {

                    //////////////////.println("bbbbbbbbbb");

                    getBarChartColumnTitles()[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[1]);
                    getBarChartColumnNames()[viewByElementIds.length + j] = "A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                    getPieChartColumns()[viewByElementIds.length + j] = "A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]);

                    if (pbretObj2.getFieldValueString(j, graphDBColumns[2]).equalsIgnoreCase("") || pbretObj2.getFieldValueString(j, graphDBColumns[2]) == null) {
                        axis[viewByElementIds.length + j] = "0";
                    } else {
                        axis[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[2]);
                    }
                }

                //////////////////.println("ccccccccc");

                ArrayList ProgenChartDisplayList = buildGraphBuildingInfoForSettingGraphSize(pcharts[i], pchartsZoom[i]);
                pcharts[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(0);
                pchartsZoom[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(1);

                for (int grpcolIndex = 0; grpcolIndex < viewBys.length; grpcolIndex++) {
                    if (!AllColumns.contains(barChartColumnNames[grpcolIndex])) {
                        AllColumns.add(getBarChartColumnNames()[grpcolIndex]);
                    }
                }
                for (int grpcolIndex = viewBys.length; grpcolIndex < getBarChartColumnNames().length; grpcolIndex++) {
                    if (!graphColumns.contains(barChartColumnNames[grpcolIndex])) {
                        graphColumns.add(getBarChartColumnNames()[grpcolIndex]);
                    }
                    if (!AllColumns.contains(barChartColumnNames[grpcolIndex])) {
                        AllColumns.add(getBarChartColumnNames()[grpcolIndex]);
                    }
                }

                //////////////////.println("ddddddd");
                dashboardTemplateBD.setSession(session);
                setCurrentDispRecords(dashboardTemplateBD.getRecordSetForGraph(ParametersMap, graphColumns, AllColumns, getReportId()));
                //storing graph header info in HashMap
                buildGraphWiseDetailsMap(graphMapDetails[i]);
            }

//             if (!path.equalsIgnoreCase("")) {
            if (path != null && path.length() > 0) {
                path = new StringBuilder(path.substring(1));
            }
            //            if (!graphTitle.equalsIgnoreCase("")) {
            if (graphTitle != null && graphTitle.length() > 0) {
//                graphTitle = graphTitle.substring(1);
                graphTitle = new StringBuilder(graphTitle.substring(1));
            }


            grpDetails.add(path);
            grpDetails.add(graphTitle);
            grpDetails.add(pcharts);

            grpDetails.add(pathZoom);
            grpDetails.add(pchartsZoom);


            graphMapDetails[0].put("graphHeight", "280");
            if (session.getAttribute("screenwidth") != null) {
                if (Integer.parseInt(String.valueOf(session.getAttribute("screenwidth"))) == 1024) {
                    graphMapDetails[0].put("graphWidth", "300");
                } else {
                    graphMapDetails[0].put("graphWidth", "420");
                }
            } else {
                graphMapDetails[0].put("graphWidth", "420");
            }


            grpDetails.add(graphMapDetails);


            //String[] keys= (String[])graphMapDetails[0].keySet().toArray(new String[0]);


        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return grpDetails;
    }

    public ArrayList getGraphByGraphIdFX(String repId, String graphId, HashMap ParametersMap) {
        ArrayList grpDetails = new ArrayList();
        ArrayList graphColumns = new ArrayList();
        ArrayList AllColumns = new ArrayList();
        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
        try {
            //pbretObj = getPbReturnObject(getGrpHdrsQueryByGraphIdQuery(repId, graphId));
            Object[] Obj1 = new Object[2];
            Obj1[0] = repId;
            Obj1[1] = graphId;
            String FinalQuery = buildQuery(resBundle.getString("grpHdrsQueryByGraphIdFX"), Obj1);

            pbretObj = getPbReturnObject(FinalQuery);

            String[] tableDBColumns = pbretObj.getColumnNames();
            setGraphsCnt(pbretObj.getRowCount());
            pcharts = new ProgenChartDisplay[getGraphsCnt()];
            pchartsZoom = new ProgenChartDisplay[getGraphsCnt()];
            String[] grpIds = null;
            graphMapDetails = new HashMap[pbretObj.getRowCount()];
            grpIds = new String[pbretObj.getRowCount()];
            String[] viewBys = null;
            for (int i = 0; i < getGraphsCnt(); i++) {
                graphMapDetails[i] = new HashMap();
                grpIds[i] = pbretObj.getFieldValueString(i, tableDBColumns[0]);
                getGraphWiseDetailsMapFromPbReturnObject(pbretObj, i, tableDBColumns);

                setGraphId(graphId);
                pbretObj2 = getPbReturnObject(getGrpDtlsQuery(graphId));
                setBarChartColumnTitles(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                setBarChartColumnNames(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                setPieChartColumns(new String[getViewByElementIds().length + pbretObj2.getRowCount()]);
                axis = new String[getViewByElementIds().length + pbretObj2.getRowCount()];
                String[] graphDBColumns = pbretObj2.getColumnNames();
                viewBys = viewByElementIds;
                for (int viewbyIndex = 0; viewbyIndex < viewByElementIds.length; viewbyIndex++) {
                    if (viewByElementIds[viewbyIndex].equalsIgnoreCase("Time")) {
                        viewBys[viewbyIndex] = viewByElementIds[viewbyIndex];
                    } else {
                        if (!(viewByElementIds[viewbyIndex].contains("A_"))) {
                            viewBys[viewbyIndex] = "A_" + viewByElementIds[viewbyIndex];
                        } else {
                            viewBys[viewbyIndex] = viewByElementIds[viewbyIndex];
                        }
                    }
                    getBarChartColumnTitles()[viewbyIndex] = viewByColNames[viewbyIndex];
                    getBarChartColumnNames()[viewbyIndex] = viewBys[viewbyIndex];
                    getPieChartColumns()[viewbyIndex] = viewBys[viewbyIndex];
                    axis[viewbyIndex] = "0";
                }
                for (int j = 0; j < pbretObj2.getRowCount(); j++) {
                    getBarChartColumnTitles()[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[1]);
                    getBarChartColumnNames()[viewByElementIds.length + j] = "A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                    getPieChartColumns()[viewByElementIds.length + j] = "A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                    if (pbretObj2.getFieldValueString(j, graphDBColumns[2]).equalsIgnoreCase("") || pbretObj2.getFieldValueString(j, graphDBColumns[2]) == null) {
                        axis[viewByElementIds.length + j] = "0";
                    } else {
                        axis[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[2]);
                    }
                }
//                String axis1 = "";
//                String axis2 = "";
                StringBuilder axis1 = new StringBuilder(1000);
                StringBuilder axis2 = new StringBuilder(1000);
                if (axis != null) {
                    for (int ind = viewByElementIds.length; ind < axis.length; ind++) {
                        //if (axis[ind] == null) {
                        //axis[ind] = "0";
                        //}
                        if (axis[ind].equalsIgnoreCase("0")) {
//                            axis1 = axis1 + "," + ind;
                            axis1.append(",").append(ind);
                        } else {
//                            axis2 = axis2 + "," + ind;
                            axis2.append(",").append(ind);
                        }
                    }
                }


//                            if ((!axis1.equalsIgnoreCase("")) && (!axis2.equalsIgnoreCase(""))) {
                if ((axis1.length() > 0) && (axis2.length()) > 0) {
//                                axis1 = axis1.substring(1);
//                                axis2 = axis2.substring(1);
                    axis1 = new StringBuilder(axis1.substring(1));
                    axis2 = new StringBuilder(axis2.substring(1));

                    String[] temp1 = axis1.toString().split(",");
                    setBarChartColumnTitles1(new String[temp1.length + viewByElementIds.length]);
                    setBarChartColumnNames1(new String[getBarChartColumnTitles1().length]);
                    String[] temp2 = axis2.toString().split(",");
                    setBarChartColumnTitles2(new String[temp2.length + viewByElementIds.length]);
                    setBarChartColumnNames2(new String[getBarChartColumnTitles2().length]);
                    for (int j = 0; j < viewByElementIds.length; j++) {
                        getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                        getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j];

                        getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                        getBarChartColumnNames2()[j] = getBarChartColumnNames()[j];
                    }
                    for (int j = 0; j < temp1.length; j++) {
                        getBarChartColumnTitles1()[viewByElementIds.length + j] = getBarChartColumnTitles()[Integer.parseInt(temp1[j])];
                        getBarChartColumnNames1()[viewByElementIds.length + j] = getBarChartColumnNames()[Integer.parseInt(temp1[j])];
                    }
                    for (int j = 0; j < temp2.length; j++) {
                        getBarChartColumnTitles2()[viewByElementIds.length + j] = getBarChartColumnTitles()[Integer.parseInt(temp2[j])];
                        getBarChartColumnNames2()[viewByElementIds.length + j] = getBarChartColumnNames()[Integer.parseInt(temp2[j])];
                    }
                } else {
                    int allColumns = getBarChartColumnNames().length - viewByElementIds.length;
                    int dividor = 2;
                    int rightColumns = allColumns / dividor;
                    int leftColumns = allColumns - rightColumns;
                    setBarChartColumnTitles1(new String[leftColumns + viewByElementIds.length]);
                    setBarChartColumnNames1(new String[getBarChartColumnTitles1().length]);
                    setBarChartColumnTitles2(new String[viewByElementIds.length + rightColumns]);
                    setBarChartColumnNames2(new String[getBarChartColumnTitles2().length]);

                    for (int j = 0; j < viewByElementIds.length; j++) {
                        getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                        getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j];
                        getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                        getBarChartColumnNames2()[j] = getBarChartColumnNames()[j];
                    }
                    for (int j = viewByElementIds.length; j < getBarChartColumnTitles1().length; j++) {
                        getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                        getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                    }
                    for (int j = viewByElementIds.length; j < getBarChartColumnTitles2().length; j++) {
                        getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j + leftColumns];
                        getBarChartColumnNames2()[j] = getBarChartColumnNames()[j + leftColumns];
                    }
                }

                for (int grpcolIndex = 0; grpcolIndex < viewBys.length; grpcolIndex++) {
                    if (!AllColumns.contains(barChartColumnNames[grpcolIndex])) {
                        AllColumns.add(getBarChartColumnNames()[grpcolIndex]);
                    }
                }
                for (int grpcolIndex = viewBys.length; grpcolIndex < getBarChartColumnNames().length; grpcolIndex++) {
                    if (!graphColumns.contains(barChartColumnNames[grpcolIndex])) {
                        graphColumns.add(getBarChartColumnNames()[grpcolIndex]);
                    }
                    if (!AllColumns.contains(barChartColumnNames[grpcolIndex])) {
                        AllColumns.add(getBarChartColumnNames()[grpcolIndex]);
                    }
                }

                viewByElementIds = viewBys;
                dashboardTemplateBD.setSession(session);
                setCurrentDispRecords(dashboardTemplateBD.getRecordSetForGraph(ParametersMap, graphColumns, AllColumns, getReportId()));

                setCurrentDispRetObjRecords(dashboardTemplateBD.getRecordSetForGraphFX(ParametersMap, graphColumns, AllColumns, getReportId()));
                setCurrentDispRecordsRetObjWithGT(dashboardTemplateBD.getRecordSetForGraphFX(ParametersMap, graphColumns, AllColumns, getReportId()));
                setAllDispRecordsRetObj(dashboardTemplateBD.getRecordSetForGraphFX(ParametersMap, graphColumns, AllColumns, getReportId()));




                buildGraphWiseDetailsMap(graphMapDetails[i]);
            }

//             if (!path.equalsIgnoreCase("")) {
            if (path != null && path.length() > 0) {
                path = new StringBuilder(path.substring(1));
            }
            //            if (!graphTitle.equalsIgnoreCase("")) {
            if (graphTitle != null && graphTitle.length() > 0) {
//                graphTitle = graphTitle.substring(1);
                graphTitle = new StringBuilder(graphTitle.substring(1));
            }


            grpDetails.add(path);
            grpDetails.add(graphTitle);
            grpDetails.add(pcharts);
            grpDetails.add(pathZoom);
            grpDetails.add(pchartsZoom);
            grpDetails.add(graphMapDetails);
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return grpDetails;
    }

    public ArrayList getDashboardGraphHeadersNew(String reportId, String graphId, DashletDetail dashlet) {

        ArrayList grpDetails = new ArrayList();
        GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
        boolean isTimeViewByPresent = false;

        try {
            setGraphsCnt(1);
            pcharts = new ProgenChartDisplay[1];
            pchartsZoom = new ProgenChartDisplay[1];
            graphMapDetails = new HashMap[1];

            graphMapDetails[0] = new HashMap();
            getGraphDetailsFromDashlet(dashlet);
            String dashletId = dashlet.getDashBoardDetailId();
            DashletPropertiesHelper dashletPropertiesHelper1 = null;
            DashboardViewerDAO viewerDao = new DashboardViewerDAO();
            if (graphDetails.getDashletpropertieshelper() != null) {
                dashletPropertiesHelper1 = graphDetails.getDashletpropertieshelper();
                if (dashletPropertiesHelper1.isSortAll()) {
                    graphDetails.setDisplayRows("All");
                    graphDisplayRows = "All";
                } else {
                    graphDetails.setDisplayRows(String.valueOf(dashletPropertiesHelper1.getCountForDisplay()));
                    graphDisplayRows = String.valueOf(dashletPropertiesHelper1.getCountForDisplay());
                }
            } else {
                dashletPropertiesHelper1 = viewerDao.getDashletPropertiesHelperObject(dashletId);
                graphDetails.setDashletpropertieshelper(dashletPropertiesHelper1);
            }
            ArrayList<Integer> viewSeq = new ArrayList<Integer>();
            graphHeight = graphDetails.getGraphHeight();
            graphWidth = graphDetails.getGraphWidth();

            int elemCount = graphDetails.getElementCount();
            List<QueryDetail> queryDetails = graphDetails.getQueryDetails();

            setBarChartColumnTitles(new String[getViewByElementIds().length + elemCount]);
            setBarChartColumnNames(new String[getViewByElementIds().length + elemCount]);
            setPieChartColumns(new String[getViewByElementIds().length + elemCount]);

            axis = new String[getViewByElementIds().length + elemCount];

            for (int viewbyIndex = 0; viewbyIndex < viewByElementIds.length; viewbyIndex++) {
                getBarChartColumnTitles()[viewbyIndex] = viewByColNames[viewbyIndex];
                getBarChartColumnNames()[viewbyIndex] = viewByElementIds[viewbyIndex];
                getPieChartColumns()[viewbyIndex] = viewByElementIds[viewbyIndex];
                axis[viewbyIndex] = "0";
                if ("TIME".equalsIgnoreCase(viewByElementIds[viewbyIndex]) && !isTimeViewByPresent) {
                    isTimeViewByPresent = true;
                }
            }

            for (int j = 0; j < queryDetails.size(); j++) {
                getBarChartColumnTitles()[viewByElementIds.length + j] = queryDetails.get(j).getDisplayName();
                getBarChartColumnNames()[viewByElementIds.length + j] = "A_" + queryDetails.get(j).getElementId();
                getPieChartColumns()[viewByElementIds.length + j] = "A_" + queryDetails.get(j).getElementId();
                axis[viewByElementIds.length + j] = graphDetails.getAxis();
            }


            if (!graphDetails.isTimeSeries() && queryDetails != null && !queryDetails.isEmpty() && !isTimeViewByPresent) {//start of initial view
                String sortElementId = "A_" + queryDetails.get(0).getElementId();
                ProgenDataSet retObj = getAllDispRecordsRetObj();
                ArrayList<String> sortElementsList = new ArrayList<String>();
                char[] sortTypes = new char[1];
                char[] sortDataTypes = new char[1];
                if (dashletPropertiesHelper1 != null) {
//                String sortType="top";
//                sortType=dashletPropertiesHelper1.getDisplayOrder();
//                for (QueryDetail qd : queryDetails) {
//                    if(dashletPropertiesHelper1.getSortOnMeasure().equalsIgnoreCase("A_" + qd.getElementId())){
//                      sortDataTypes[0] =  qd.getColumnType().charAt(0);
//                    }
//                }
//                if (sortType.equalsIgnoreCase("top") ) {
//                sortTypes[0] = '1';
//                sortElementsList.add( dashletPropertiesHelper1.getSortOnMeasure());
////                    if(container.getDataTypes().get(container.getDisplayColumns().indexOf(dashletPropertiesHelper.getSortOnMeasure())).toString().equalsIgnoreCase("N"))
//               viewSeq = retObj.sortDataSet(sortElementsList,sortTypes, sortDataTypes);
//               retObj.setViewSequence(viewSeq);
//                } else {
//                sortTypes[0] = '0';
//                sortElementsList.add( dashletPropertiesHelper1.getSortOnMeasure());
//                  viewSeq = retObj.sortDataSet(sortElementsList,sortTypes, sortDataTypes);
//                  retObj.setViewSequence(viewSeq);
//                }
//                sortTypes[0] = (char)dashletPropertiesHelper1.getTypeForSort();
//                sortElementsList.add( dashletPropertiesHelper1.getSortOnMeasure());
//                if(dashletPropertiesHelper1.getElement_IDforSort()!=null && dashletPropertiesHelper1.getTypeForSort()!=-1 && !dashletPropertiesHelper1.isIsFromTopBottom() ){
//                       viewSeq=retObj.sortDataSet( sortElementsList,sortTypes, sortDataTypes);
//                       retObj.setViewSequence(viewSeq);
//
//                }
                } else {
                    sortElementsList.add(sortElementId);
                    sortTypes[0] = '1';
                    sortDataTypes[0] = queryDetails.get(0).getColumnType().charAt(0);
                    retObj.resetViewSequence();
                    viewSeq = retObj.sortDataSet(sortElementsList, sortTypes, sortDataTypes);
                    retObj.setViewSequence(viewSeq);
                }
                graphViewSeq = viewSeq;
            }//end of Initial view

            ArrayList ProgenChartDisplayList = buildGraphBuildingInfo(pcharts[0], pchartsZoom[0]);
            pcharts[0] = (ProgenChartDisplay) ProgenChartDisplayList.get(0);
            pchartsZoom[0] = (ProgenChartDisplay) ProgenChartDisplayList.get(1);

            buildGraphWiseDetailsMap(graphMapDetails[0], graphDetails);

//             if (!path.equalsIgnoreCase("")) {
            if (path != null && path.length() > 0) {
                path = new StringBuilder(path.substring(1));
            }
            //            if (!graphTitle.equalsIgnoreCase("")) {
            if (graphTitle != null && graphTitle.length() > 0) {
//                graphTitle = graphTitle.substring(1);
                graphTitle = new StringBuilder(graphTitle.substring(1));
            }


            grpDetails.add(path);
            grpDetails.add(graphTitle);
            grpDetails.add(pcharts);
            grpDetails.add(pathZoom);
            grpDetails.add(pchartsZoom);
            grpDetails.add(graphMapDetails);
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return grpDetails;
    }

    public ArrayList getDbrdGraphHeadersByGraphMap() {
        ArrayList grpDetails = new ArrayList();
        try {
            pcharts = new ProgenChartDisplay[graphMapDetails.length];
            pchartsZoom = new ProgenChartDisplay[graphMapDetails.length];

            for (int i = 0; i < graphMapDetails.length; i++) {
                getGraphWiseDetailsMap(graphMapDetails[i]);
                graphWidth = "480";
                graphHeight = "280";
                setGraphId(graphId);
                setBarChartColumnTitles((String[]) graphMapDetails[i].get("barChartColumnTitles"));
                setBarChartColumnNames((String[]) graphMapDetails[i].get("barChartColumnNames"));
                setPieChartColumns((String[]) graphMapDetails[i].get("pieChartColumns"));
                axis = ((String[]) graphMapDetails[i].get("axis"));
                viewByElementIds = ((String[]) graphMapDetails[i].get("viewByElementIds"));
                setBarChartColumnNames1((String[]) graphMapDetails[i].get("barChartColumnNames1"));
                setBarChartColumnTitles1((String[]) graphMapDetails[i].get("barChartColumnTitles1"));
                setBarChartColumnNames2((String[]) graphMapDetails[i].get("barChartColumnNames2"));
                setBarChartColumnTitles2((String[]) graphMapDetails[i].get("barChartColumnTitles2"));
                ArrayList ProgenChartDisplayList = buildGraphBuildingInfo(pcharts[i], pchartsZoom[i]);
                pcharts[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(0);
                pchartsZoom[i] = (ProgenChartDisplay) ProgenChartDisplayList.get(1);
                buildGraphWiseDetailsMap(graphMapDetails[i]);
            }
//             if (!path.equalsIgnoreCase("")) {
            if (path != null && path.length() > 0) {
                path = new StringBuilder(path.substring(1));
            }
            //            if (!graphTitle.equalsIgnoreCase("")) {
            if (graphTitle != null && graphTitle.length() > 0) {
//                graphTitle = graphTitle.substring(1);
                graphTitle = new StringBuilder(graphTitle.substring(1));
            }


            grpDetails.add(path);
            grpDetails.add(graphTitle);
            grpDetails.add(pcharts);
            grpDetails.add(pathZoom);
            grpDetails.add(pchartsZoom);
            grpDetails.add(graphMapDetails);


        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }

        return grpDetails;
    }
//end of code related to Dashboard

    public String getTargetRange() {
        return targetRange;
    }

    public void setTargetRange(String targetRange) {
        this.targetRange = targetRange;
    }

    public String getStartValue() {
        return startValue;
    }

    public void setStartValue(String startValue) {
        this.startValue = startValue;
    }

    public String getEndValue() {
        return endValue;
    }

    public void setEndValue(String endValue) {
        this.endValue = endValue;
    }

    public String getShowMinMaxRange() {
        return showMinMaxRange;
    }

    public void setShowMinMaxRange(String showMinMaxRange) {
        this.showMinMaxRange = showMinMaxRange;
    }

    public void getGraphWiseDetailsMap(HashMap singleGraphMapDetails) {

        //////.println("getGraphWiseDetailsMap");
        graphId = String.valueOf(singleGraphMapDetails.get("graphId"));
        graphName = String.valueOf(singleGraphMapDetails.get("graphName"));
        graphClassName = String.valueOf(singleGraphMapDetails.get("graphClassName"));



        graphTypeName = String.valueOf(singleGraphMapDetails.get("graphTypeName"));
        ////////////////.println("graphTypeName="+graphTypeName);
        if (!fromOneview) {
            graphWidth = String.valueOf(singleGraphMapDetails.get("graphWidth"));
            graphHeight = String.valueOf(singleGraphMapDetails.get("graphHeight"));
        }
        graphSize = singleGraphMapDetails.get("graphSize") == null ? "Medium" : String.valueOf(singleGraphMapDetails.get("graphSize"));
        grplegend = singleGraphMapDetails.get("graphLegend") == null ? "Y" : String.valueOf(singleGraphMapDetails.get("graphLegend"));
        setGraphProperty(singleGraphMapDetails.get("GraphProperty") != null ? (GraphProperty) (singleGraphMapDetails.get("GraphProperty")) : new GraphProperty());

        grplegendloc = singleGraphMapDetails.get("graphLegendLoc") == null ? "Bottom" : String.valueOf(singleGraphMapDetails.get("graphLegendLoc"));
        grpshox = String.valueOf(singleGraphMapDetails.get("graphshowX"));
        grpshoy = String.valueOf(singleGraphMapDetails.get("graphshowY"));
        grpdrill = String.valueOf(singleGraphMapDetails.get("graphDrill"));
        grpbcolor = String.valueOf(singleGraphMapDetails.get("graphBcolor"));
        grpfcolor = String.valueOf(singleGraphMapDetails.get("graphFcolor"));
        grpdata = String.valueOf(singleGraphMapDetails.get("graphData"));
        grpLYaxislabel = String.valueOf(singleGraphMapDetails.get("grplyaxislabel")) == null ? "" : String.valueOf(singleGraphMapDetails.get("grplyaxislabel"));
        grpryaxislabel = String.valueOf(singleGraphMapDetails.get("grpryaxislabel")) == null ? "" : String.valueOf(singleGraphMapDetails.get("grpryaxislabel"));
        grpbDomainaxislabel = String.valueOf(singleGraphMapDetails.get("grpbDomainaxislabel")) == null ? "" : String.valueOf(singleGraphMapDetails.get("grpbDomainaxislabel"));
        SwapColumn = (singleGraphMapDetails.get("SwapColumn") == null) ? true : Boolean.valueOf(String.valueOf(singleGraphMapDetails.get("SwapColumn")));//by prabal
        rowValuesList = singleGraphMapDetails.get("RowValuesList") == null ? new ArrayList() : (ArrayList) singleGraphMapDetails.get("RowValuesList");
        showGT = String.valueOf(singleGraphMapDetails.get("showGT"));
        nbrFormat = String.valueOf(singleGraphMapDetails.get("nbrFormat"));
        graphSymbol = String.valueOf(singleGraphMapDetails.get("graphSymbol"));
        showLabels = (singleGraphMapDetails.get("showLabels") == null) ? false : Boolean.valueOf(String.valueOf(singleGraphMapDetails.get("showLabels")));
        measureFormat = (singleGraphMapDetails.get("measureFormat") == null) ? "" : String.valueOf(singleGraphMapDetails.get("measureFormat"));
        measureValueRounding = (singleGraphMapDetails.get("measureValueRounding") == null) ? "" : (String) singleGraphMapDetails.get("measureValueRounding");
        axisLabelPosition = (singleGraphMapDetails.get("axisLabelPosition") == null) ? "" : (String) singleGraphMapDetails.get("axisLabelPosition");
        calibration = (singleGraphMapDetails.get("calibration") == null) ? "" : (String) singleGraphMapDetails.get("calibration");
        //measNamePosition = String.valueOf(singleGraphMapDetails.get("measureNamePosition"))== null ? "": String.valueOf(singleGraphMapDetails.get("measureNamePosition"));;
        graphGridLines = singleGraphMapDetails.get("graphGridLines") == null ? "Y" : String.valueOf(singleGraphMapDetails.get("graphGridLines"));
        firstChartType = singleGraphMapDetails.get("firstChartType") == null ? "" : String.valueOf(singleGraphMapDetails.get("firstChartType"));
        secondChartType = singleGraphMapDetails.get("secondChartType") == null ? "" : String.valueOf(singleGraphMapDetails.get("secondChartType"));
        rgbColorArr = (String[]) singleGraphMapDetails.get("rgbColorArr");
        GraphProperty property = (GraphProperty) singleGraphMapDetails.get("GraphProperty");
        if (property != null) {
//        measNamePosition= singleGraphMapDetails.get("measureNamePosition").toString();
            if (property.getMeasureNamePosition() != null) {
                measNamePosition = property.getMeasureNamePosition();
            }
            if (property.getShowlyAxis() != null) {
                showlyAxis = property.getShowlyAxis();
            }
            if (property.getShowryAxis() != null) {
                showryAxis = property.getShowryAxis();
            }
            if (property.getShowxAxis() != null) {
                showxAxis = property.getShowxAxis();
            }
            stackedType = property.getStackedType();
        }
        graphDisplayRows = singleGraphMapDetails.get("graphDisplayRows") == null ? "" : String.valueOf(singleGraphMapDetails.get("graphDisplayRows"));
        ////////////////////////.println("graphDisplayRows==="+graphDisplayRows);

        targetRange = singleGraphMapDetails.get("targetRange") == null ? null : String.valueOf(singleGraphMapDetails.get("targetRange"));
        startValue = singleGraphMapDetails.get("startValue") == null ? null : String.valueOf(singleGraphMapDetails.get("startValue"));
        endValue = singleGraphMapDetails.get("endValue") == null ? null : String.valueOf(singleGraphMapDetails.get("endValue"));
        showMinMaxRange = singleGraphMapDetails.get("showMinMaxRange") == null ? "N" : String.valueOf(singleGraphMapDetails.get("showMinMaxRange"));

        //////.println("test-before startindex"+String.valueOf(singleGraphMapDetails.get("startindex")));
        //////.println("test before  -endindex "+String.valueOf(singleGraphMapDetails.get("endindex")));

        startindex = singleGraphMapDetails.get("startindex") == null ? "0" : String.valueOf(singleGraphMapDetails.get("startindex"));
        endindex = singleGraphMapDetails.get("endindex") == null ? graphDisplayRows : String.valueOf(singleGraphMapDetails.get("endindex"));

        //////.println("test-aftre startindex"+startindex);
        //////.println("test-aftre endindex"+endindex);

        ////////.println("@@@@@@@ startindex="+startindex);
        ////////.println("@@@@@@@ endindex="+endindex);

        setGraphId(graphId);
        if (graphTypeName.equalsIgnoreCase("TimeSeries")) {
            timeSeries = true;
            ////////////////.println("time series is true");
//            setTimeSeries(true);
//            checkingTimeSeries=true;
        }
        setBarChartColumnTitles((String[]) singleGraphMapDetails.get("barChartColumnTitles"));
        setBarChartColumnNames((String[]) singleGraphMapDetails.get("barChartColumnNames"));
        setPieChartColumns((String[]) singleGraphMapDetails.get("pieChartColumns"));
        axis = ((String[]) singleGraphMapDetails.get("axis"));
        setBarChartColumnNames1((String[]) singleGraphMapDetails.get("barChartColumnNames1"));
        setBarChartColumnTitles1((String[]) singleGraphMapDetails.get("barChartColumnTitles1"));
        setBarChartColumnNames2((String[]) singleGraphMapDetails.get("barChartColumnNames2"));
        setBarChartColumnTitles2((String[]) singleGraphMapDetails.get("barChartColumnTitles2"));






        if (getBarChartColumnNames1() != null && getBarChartColumnNames2() != null) {
        } else {
            int allColumns = getBarChartColumnNames().length - viewByElementIds.length;
            int dividor = 2;
            int rightColumns = allColumns / dividor;
            int leftColumns = allColumns - rightColumns;
            setBarChartColumnTitles1(new String[leftColumns + viewByElementIds.length]);
            setBarChartColumnNames1(new String[getBarChartColumnTitles1().length]);
            setBarChartColumnTitles2(new String[viewByElementIds.length + rightColumns]);
            setBarChartColumnNames2(new String[getBarChartColumnTitles2().length]);


            for (int j = 0; j < viewByElementIds.length; j++) {
                getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j];
                getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                getBarChartColumnNames2()[j] = getBarChartColumnNames()[j];
            }
            for (int j = viewByElementIds.length; j < getBarChartColumnTitles1().length; j++) {
                getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
            }


            for (int j = viewByElementIds.length; j < getBarChartColumnTitles2().length; j++) {
                getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j + leftColumns];
                getBarChartColumnNames2()[j] = getBarChartColumnNames()[j + leftColumns];
            }
        }
    }

    public void getGraphWiseDetailsMapFromPbReturnObject(PbReturnObject pbretObj, int i, String[] tableDBColumns) {

        ////////.println("5555");
        graphId = pbretObj.getFieldValueString(i, tableDBColumns[0]);
        graphName = pbretObj.getFieldValueString(i, tableDBColumns[1]);
        if (!fromOneview) {
            graphWidth = pbretObj.getFieldValueString(i, tableDBColumns[2]);
            graphHeight = pbretObj.getFieldValueString(i, tableDBColumns[3]);
        }
        if (fromOneview) {
            graphWidth1 = pbretObj.getFieldValueString(i, tableDBColumns[2]);
            graphHeight1 = pbretObj.getFieldValueString(i, tableDBColumns[3]);
        }
        if (fromOneview) {
            graphWidth1 = pbretObj.getFieldValueString(i, tableDBColumns[2]);
            graphHeight1 = pbretObj.getFieldValueString(i, tableDBColumns[3]);
        }
        graphClassName = pbretObj.getFieldValueString(i, tableDBColumns[4]);
        graphTypeName = pbretObj.getFieldValueString(i, tableDBColumns[5]);
        graphSize = pbretObj.getFieldValueString(i, tableDBColumns[6]);

        //////////////////////.println("graphId="+graphId);
        //////////////////////.println("graphName="+graphName);
        //////////////////////.println("graphWidth="+graphWidth);
        //////////////////////.println("graphHeight="+graphHeight);
        //////////////////////.println("graphClassName="+graphClassName);
        //////////////////////.println("graphTypeName="+graphTypeName);
        //////////////////////.println("graphSize="+graphSize);



        if (getGraphSizesDtlsHashMap() != null) {
            if (getGraphSizesDtlsHashMap().get(graphSize) != null) {
                ArrayList sizedetails = (ArrayList) getGraphSizesDtlsHashMap().get(graphSize);
                if (!fromOneview) {
                    graphWidth = String.valueOf(sizedetails.get(0));
                    graphHeight = String.valueOf(sizedetails.get(1));
                }
                ////////////////////.println("getGraphWiseDetailsMapFromPbReturnObject");
                ////////////////////.println("graphWidth="+graphWidth);
                ////////////////////.println("graphHeight="+graphHeight);

            }
        }

        grplegendloc = pbretObj.getFieldValueString(i, tableDBColumns[7]);
        grplegend = pbretObj.getFieldValueString(i, tableDBColumns[8]);
        grpshox = pbretObj.getFieldValueString(i, tableDBColumns[9]);
        grpshoy = pbretObj.getFieldValueString(i, tableDBColumns[10]);
        grpdrill = pbretObj.getFieldValueString(i, tableDBColumns[11]);
        grpbcolor = pbretObj.getFieldValueString(i, tableDBColumns[12]);
        grpfcolor = pbretObj.getFieldValueString(i, tableDBColumns[13]);
        grpdata = pbretObj.getFieldValueString(i, tableDBColumns[14]);
        grpLYaxislabel = pbretObj.getFieldValueString(i, tableDBColumns[15]);
        grpryaxislabel = pbretObj.getFieldValueString(i, tableDBColumns[16]);
        grpbDomainaxislabel = pbretObj.getFieldValueString(i, tableDBColumns[16]);

        String grpaPropetyString = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            if (pbretObj.getFieldUnknown(i, 21) != null && !pbretObj.getFieldUnknown(i, 21).equalsIgnoreCase("") && pbretObj.getFieldUnknown(i, 22) != null && !pbretObj.getFieldUnknown(i, 22).equalsIgnoreCase("")) {
                grpaPropetyString = pbretObj.getFieldUnknown(i, 20) + pbretObj.getFieldUnknown(i, 21) + pbretObj.getFieldUnknown(i, 22);
            } else if (pbretObj.getFieldUnknown(i, 21) != null && !pbretObj.getFieldUnknown(i, 21).equalsIgnoreCase("")) {
                grpaPropetyString = pbretObj.getFieldUnknown(i, 20) + pbretObj.getFieldUnknown(i, 21);
            } else {
                grpaPropetyString = pbretObj.getFieldUnknown(i, 20);
            }
        } else {
            if (pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1") != null && !pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1").equalsIgnoreCase("") && pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML2") != null && !pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML2").equalsIgnoreCase("")) {
                grpaPropetyString = pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML") + pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1") + pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML2");
            } else if (pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1") != null && !pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1").equalsIgnoreCase("")) {
                grpaPropetyString = pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML") + pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML1");
            } else {
                grpaPropetyString = pbretObj.getFieldValueClobString(i, "GRAPH_PROPERTY_XML");
            }
        }
        GraphProperty property = this.parseGraphPropertyXml(grpaPropetyString);

        // setGraphProperty(property);   //commented by swati
//        if(pbretObj.getFieldValueString(i, "SHOW_LABEL")!=null && !"".equalsIgnoreCase(pbretObj.getFieldValueString(i, "SHOW_LABEL")))
//               isShowLabels=Boolean.valueOf(pbretObj.getFieldValueString(i, "SHOW_LABEL"));
//         else
//              isShowLabels=false;
        //////////////////////.println("grplegendloc="+grplegendloc);
        //////////////////////.println("grplegend="+grplegend);
        //////////////////////.println("grpshox="+grpshox);
        //////////////////////.println("grpshoy="+grpshoy);
        //////////////////////.println("grpdrill="+grpdrill);
        //////////////////////.println("grpbcolor="+grpbcolor);
        //////////////////////.println("grpfcolor="+grpfcolor);
        //////////////////////.println("grpdata="+grpdata);
        //////////////////////.println("grplyaxislabel="+grplyaxislabel);
        //////////////////////.println("grpryaxislabel="+grpryaxislabel);





        if (graphTypeName.equalsIgnoreCase("columnPie") || graphTypeName.equalsIgnoreCase("columnPie3D")) {
            rowValuesList = getRowValuesList(pbretObj.getFieldValueClobString(i, tableDBColumns[17]));
            if (rowValuesList == null) {
                rowValuesList = new ArrayList();
            }

        } else {
            rowValuesList = new ArrayList();
        }

        showGT = pbretObj.getFieldValueString(i, tableDBColumns[18]);
        setGraphId(graphId);
        if (graphTypeName.equalsIgnoreCase("TimeSeries")) {
            setTimeSeries(true);
        }
        if (!property.getSwapGraphColumns().equalsIgnoreCase("")) {
            SwapColumn = Boolean.valueOf(property.getSwapGraphColumns());
        } else {
            SwapColumn = Boolean.valueOf(" ");
        }
        nbrFormat = property.getNumberFormat();
        // if(property.getSymbol().equalsIgnoreCase(""))
        graphSymbol = property.getSymbol();
        measNamePosition = property.getMeasureNamePosition();
        showlyAxis = property.getShowlyAxis();
        showryAxis = property.getShowryAxis();
        showxAxis = property.getShowxAxis();
        graphGridLines = property.getGraphGridLines();
        String values = this.getGraphDisplayRowsForDrill();
        if (values != null && !values.equalsIgnoreCase("")) {
            graphDisplayRows = values;
        } else {
            graphDisplayRows = pbretObj.getFieldValueString(i, tableDBColumns[19]);
        }

        this.setGraphdisplayRowsForDrill(graphDisplayRows);
//        
//        if(graphDisplayRows.equalsIgnoreCase("")){
//            graphDisplayRows = 
//        }
        targetRange = property.getTargetValueType();
        if (targetRange.equalsIgnoreCase("Discrete")) {
            startValue = Double.toString(property.getStartValue());
        } else {
            startValue = Double.toString(property.getStartValue());
        }
        endValue = Double.toString(property.getEndValue());
        if (property.getMinMaxRange()) {
            showMinMaxRange = "Y";
        } else {
            showMinMaxRange = "N";
        }
//        startindex="0";//added by k
//        endindex=graphDisplayRows;//"10";//added by k
        stackedType = property.getStackedType();;

        setGraphId(graphId);
        if (graphTypeName.equalsIgnoreCase("TimeSeries")) {
            timeSeries = true;
        }
        measureFormat = property.getMeasureFormat();
        measureValueRounding = property.getMeasureValueRounding();
        axisLabelPosition = property.getAxisLabelPosition();
        if (property.getGraphDisplayRows() != null) {
            graphDisplayRows = property.getGraphDisplayRows();
        }
        startindex = "0";//added by k
        endindex = graphDisplayRows;//"10";//added by k
        showLabels = property.isLabelsDisplayed();
        calibration = property.getCalibration();
        firstChartType = property.getFirstChartType();
        secondChartType = property.getSecondChartType();
        rgbColorArr = property.getRgbColorArr();
        setGraphProperty(property);

    }

    private Integer getActualWidthForResolution(int resolution, int width) {
        if (resolution <= 1024) {
            width = 300;
        }
        return width;
    }

    public ArrayList buildGraphBuildingInfo(ProgenChartDisplay pcharts, ProgenChartDisplay pchartsZoom) throws Exception {


        ArrayList ProgenChartDisplayList = new ArrayList();
        String graphTheme = this.getUITheme();
        //
        if (screenResolution != null && graphWidth != null && !graphWidth.equalsIgnoreCase("null")) {
            Integer grpWidPerResolution = getActualWidthForResolution(screenResolution, Integer.parseInt(graphWidth));
            graphWidth = String.valueOf(grpWidPerResolution);
        }
        if (graphWidth == null || graphWidth.equalsIgnoreCase("null") || "".equals(graphWidth)) {
            graphWidth = "400";
        }
        if (graphHeight == null || graphHeight.equalsIgnoreCase("null") || "".equals(graphHeight)) {
            graphHeight = "250";
        }
        pcharts = new ProgenChartDisplay(Integer.parseInt(graphWidth), Integer.parseInt(graphHeight), ctxPath);
        pchartsZoom = new ProgenChartDisplay(700, 300, ctxPath);
        graph = new ProgenChartDatasets();

        graph.setBarChartColumnNames(getBarChartColumnNames());
        graph.setViewByColumns(viewByElementIds);
        graph.setBarChartColumnTitles(getBarChartColumnTitles());
        graph.setPieChartColumns(getBarChartColumnNames());
        graph.setTimeLevel(getTimelevel());
        graph.setSuffixSymbol(nbrFormat);
        graph.setPrefixSymbol(graphSymbol);
        //graph.setPrefixSymbol(measurePosition);
        graph.setDisplayGraphRows(graphDisplayRows);
        graph.setGraphProperty(graphProperty);

        graph.setShowGT(showGT);
        graph.setIsCrosstab(this.isCrosstab);
        graph.setSortColumns(sortColumns);
//        graph.setShowlyAxis(showlyAxis);
//        graph.setShowryAxis(showryAxis);
//        graph.setShowxAxis(showxAxis);
//        pcharts.setGrpbDomainaxislabel(showxAxis);
//        pcharts.setGrplyaxislabel(showlyAxis);
        if (graphTypeName.equalsIgnoreCase("ColumnPie") || graphTypeName.equalsIgnoreCase("ColumnPie3D")) {
            graph.setSwapColumn(!SwapColumn);
        } else {
            graph.setSwapColumn(SwapColumn);
        }

        //code to change graph types at run time
        if (getGraphHashMap() != null && getGraphHashMap().get(graphId) != null) {
            graphTypeName = String.valueOf(((HashMap) GraphHashMap.get(graphId)).get("graphTypeName"));
            graphClassName = String.valueOf(((HashMap) GraphHashMap.get(graphId)).get("graphClassName"));

            if (graphTypeName.equalsIgnoreCase("HorizCone")) {
                graphClassName = "Pie";
                graphTypeName = "Pie";
            }

            if (graphTypeName.equalsIgnoreCase("Pyramid")) {
                graphClassName = "Pie";
                graphTypeName = "Pie3D";
            }
            if (graphTypeName.equalsIgnoreCase("Cone")) {
                graphClassName = "Pie";
                graphTypeName = "ring";
            }
            if (graphTypeName.equalsIgnoreCase("PieRing")) {
                graphClassName = "Pie";
                graphTypeName = "pie3D";
            }



            graphSize = String.valueOf(((HashMap) GraphHashMap.get(graphId)).get("graphSize"));
            if (!fromOneview) {
                graphWidth = String.valueOf(((HashMap) GraphHashMap.get(graphId)).get("graphWidth"));
                graphHeight = String.valueOf(((HashMap) GraphHashMap.get(graphId)).get("graphHeight"));
            }
            ////////////////////.println("graphWidth="+graphWidth);
            ////////////////////.println("graphHeight="+graphHeight);
            //added by k
            startindex = String.valueOf(((HashMap) GraphHashMap.get(graphId)).get("startindex"));
            endindex = String.valueOf(((HashMap) GraphHashMap.get(graphId)).get("endindex"));

            //////.println("in buildGraphBuildingInfo startindex= "+startindex);
            //////.println("in buildGraphBuildingInfo endindex= "+endindex);


        }


        //added by k on 16-06-2010
        //added by k

        //  //////////////.println("pb graph disply startindex="+startindex);
        // //////////////.println("pb graph disply endindex="+endindex);

        graph.setStartindex(startindex);
        graph.setEndindex(endindex);
        graph.setGraphid(graphId + "," + getReportId());

        ////////////////.println("graph.getStartindex="+graph.getStartindex());
        ////////////////.println("graph.getEndindex="+graph.getEndindex());




//end of code to change graph types

        graph.setColumnAverages(getColumnAverages());
        graph.setColumnGrandTotals(getColumnGrandTotals());
        graph.setColumnOverAllMaximums(getColumnOverAllMaximums());
        graph.setColumnOverAllMinimums(getColumnOverAllMinimums());

        if (getMyGraphType() != null) {
            if (getMyGraphType() != null || getMyGraphType().equalsIgnoreCase("")) {
                if (getMyGraphType().equalsIgnoreCase("Bar")
                        || getMyGraphType().equalsIgnoreCase("bar3d")
                        || getMyGraphType().equalsIgnoreCase("line")
                        || getMyGraphType().equalsIgnoreCase("line3d")
                        || getMyGraphType().equalsIgnoreCase("stacked")
                        || getMyGraphType().equalsIgnoreCase("stackedarea")
                        || getMyGraphType().equalsIgnoreCase("stacked3d")
                        || getMyGraphType().equalsIgnoreCase("column")
                        || getMyGraphType().equalsIgnoreCase("column3d")
                        || getMyGraphType().equalsIgnoreCase("waterfall")
                        || getMyGraphType().equalsIgnoreCase("Area")
                        || getMyGraphType().equalsIgnoreCase("HorizontalStacked")
                        || getMyGraphType().equalsIgnoreCase("HorizontalStacked3d")) {
                    graphClassName = "Category";
                    graphTypeName = getMyGraphType();
                }

                if (getMyGraphType().equalsIgnoreCase("pie") || getMyGraphType().equalsIgnoreCase("pie3d") || getMyGraphType().equalsIgnoreCase("ring") || getMyGraphType().equalsIgnoreCase("ColumnPie") || getMyGraphType().equalsIgnoreCase("ColumnPie3D")) {
                    graphClassName = "Pie";
                    graphTypeName = getMyGraphType();
                }

                if (getMyGraphType().equalsIgnoreCase("Dual Axis") || getMyGraphType().equalsIgnoreCase("OverlaidBar") || getMyGraphType().equalsIgnoreCase("FeverChart") || getMyGraphType().equalsIgnoreCase("OverlaidArea") || getMyGraphType().equalsIgnoreCase("Pareto") || getMyGraphType().equalsIgnoreCase("Spider")) {
                    graphClassName = "Dual Axis";
                    graphTypeName = getMyGraphType();
                }


                if (getMyGraphType().equalsIgnoreCase("Meter") || getMyGraphType().equalsIgnoreCase("Thermometer")) {
                    graphClassName = "Meter";
                    graphTypeName = getMyGraphType();
                }
                if (getMyGraphType().equalsIgnoreCase("Dial")) {
                    graphClassName = "Dial";
                    graphTypeName = getMyGraphType();
                }

                if (getMyGraphType().equalsIgnoreCase("Bubble")) {
                    graphClassName = "Bubble";
                    graphTypeName = getMyGraphType();
                }
                if (getMyGraphType().equalsIgnoreCase("TimeSeries")) {
                    graphClassName = "TimeSeries";
                    graphTypeName = getMyGraphType();
                }


//Dual Axis
            }


        }



        pcharts.setStackedType(graphProperty.getStackedType());
        pchartsZoom.setStackedType(graphProperty.getStackedType());


        // setMyGraphType("");

        ////////////////.println("in buildGraphBuildingInfo------------");
        ////////////////.println("graphClassName="+graphClassName);
        ////////////////.println("graphTypeName="+graphTypeName);

        pcharts.setGraph(graph);
        pcharts.setChartType(graphTypeName);
        pcharts.setGrplyaxislabel(grpLYaxislabel);
        pcharts.setGrpryaxislabel(grpryaxislabel);
        pcharts.setGrpbDomainaxislabel(grpbDomainaxislabel);
        pcharts.setSession(getSession());
        pcharts.setResponse(getResponse());
        pcharts.setOut(getOut());
        pcharts.setGrplegendloc(grplegendloc);
        //pcharts.setMeasureNamePosition(measNamePosition);
        pchartsZoom.setGraph(graph);
        pchartsZoom.setChartType(graphTypeName);
        //pchartsZoom.setGrplyaxislabel(grplyaxislabel);
        //pchartsZoom.setGrpryaxislabel(grpryaxislabel);
        //pchartsZoom.setGrpbDomainaxislabel(grpbDomainaxislabel);
        pchartsZoom.setSession(getSession());
        pchartsZoom.setResponse(getResponse());
        pchartsZoom.setOut(getOut());
        pchartsZoom.setGrplegendloc(grplegendloc);
        //pchartsZoom.setMeasureNamePosition(measNamePosition);
        pcharts.setSwapColumn(SwapColumn);
        pchartsZoom.setSwapColumn(SwapColumn);
        //graphGridLines
        pcharts.setGraphGridLines(getGraphGridLines());
        pchartsZoom.setGraphGridLines(getGraphGridLines());
        //added by k
        pcharts.setGrplegend(grplegend);
        pchartsZoom.setGrplegend(grplegend);


        pcharts.setGraphProperty(getGraphProperty());
        pchartsZoom.setGraphProperty(getGraphProperty());
        // for setting graph color as per the theme
        pcharts.initializeGraphColors(graphTheme);
        //added by srikanth.p for olap graph in oneView
        if (fromOneview) {
            pcharts.isOLAPGraph = true;
            pcharts.olapFunction = olapFun;

        }

        if (graph.getSwapColumn()) {
            pcharts.setFunName(getJscal());
            pchartsZoom.setFunName(getJscal());
        }

        if (graphTypeName.equalsIgnoreCase("columnPie") || graphTypeName.equalsIgnoreCase("columnPie3D") || graphTypeName.equalsIgnoreCase("TimeSeries") || graphTypeName.equalsIgnoreCase("Scatter")) {
            if (getShowGT().equalsIgnoreCase("Y")) {
                pcharts.setRetObj(getAllDispRecordsRetObj());
                pchartsZoom.setRetObj(getAllDispRecordsRetObj());
            } else {
                pcharts.setRetObj(getAllDispRecordsRetObj());
                pchartsZoom.setRetObj(getAllDispRecordsRetObj());
            }

        } else {
            if (getShowGT().equalsIgnoreCase("Y")) {
                pcharts.setRetObj(getCurrentDispRetObjRecords());
                pchartsZoom.setRetObj(getCurrentDispRetObjRecords());
            } else {
                if (graphTypeName.equalsIgnoreCase("Waterfall")) {
                    graph.setShowGT("Y");
                    pcharts.setRetObj(getCurrentDispRetObjRecords());
                    pchartsZoom.setRetObj(getCurrentDispRetObjRecords());
                } else {
                    pcharts.setRetObj(getCurrentDispRetObjRecords());
                    pchartsZoom.setRetObj(getCurrentDispRetObjRecords());
                }
            }
        }
        pcharts.setRowValuesList(rowValuesList);
        pchartsZoom.setRowValuesList(rowValuesList);

        pcharts.setGrplegend(grplegend);
        pchartsZoom.setGrplegend(grplegend);

        pcharts.setGraphProperty(getGraphProperty());
        pchartsZoom.setGraphProperty(getGraphProperty());
//        String axis1 = "";
//        String axis2 = "";
        StringBuilder axis1 = new StringBuilder(1000);
        StringBuilder axis2 = new StringBuilder(1000);
        if (axis != null) {
            for (int ind = viewByElementIds.length; ind < axis.length; ind++) {
                //if (axis[ind] == null) {
                //axis[ind] = "0";
                //}
                if (axis[ind].equalsIgnoreCase("0")) {
//                                        axis1 = axis1 + "," + ind;
                    axis1.append(",").append(ind);
                } else {
//                                        axis2 = axis2 + "," + ind;
                    axis2.append(",").append(ind);
                }
            }
        }
//      if ((!axis1.equalsIgnoreCase("")) && (!axis2.equalsIgnoreCase(""))) {
        if ((axis1.length() > 0) && (axis2.length()) > 0) {
//          axis1 = axis1.substring(1);
//          axis2 = axis2.substring(1);
            axis1 = new StringBuilder(axis1.substring(1));
            axis2 = new StringBuilder(axis2.substring(1));

            String[] temp1 = axis1.toString().split(",");
            setBarChartColumnTitles1(new String[temp1.length + viewByElementIds.length]);
            setBarChartColumnNames1(new String[getBarChartColumnTitles1().length]);
            String[] temp2 = axis2.toString().split(",");
            setBarChartColumnTitles2(new String[temp2.length + viewByElementIds.length]);
            setBarChartColumnNames2(new String[getBarChartColumnTitles2().length]);
            for (int j = 0; j < viewByElementIds.length; j++) {
                getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j];

                getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                getBarChartColumnNames2()[j] = getBarChartColumnNames()[j];
            }
            for (int j = 0; j < temp1.length; j++) {
                getBarChartColumnTitles1()[viewByElementIds.length + j] = getBarChartColumnTitles()[Integer.parseInt(temp1[j])];
                getBarChartColumnNames1()[viewByElementIds.length + j] = getBarChartColumnNames()[Integer.parseInt(temp1[j])];
            }
            for (int j = 0; j < temp2.length; j++) {
                getBarChartColumnTitles2()[viewByElementIds.length + j] = getBarChartColumnTitles()[Integer.parseInt(temp2[j])];
                getBarChartColumnNames2()[viewByElementIds.length + j] = getBarChartColumnNames()[Integer.parseInt(temp2[j])];
            }
        } else {
//            if(graphProperty.getBarChartColumnNames1()!=null&&graphProperty.getBarChartColumnNames2()!=null&&graphProperty.getBarChartColumnTitles1()!=null&&graphProperty.getBarChartColumnTitles2()!=null)
//            {
//                setBarChartColumnNames1(graphProperty.getBarChartColumnNames1());
//                setBarChartColumnNames2(graphProperty.getBarChartColumnNames2());
//                setBarChartColumnTitles1(graphProperty.getBarChartColumnTitles1());
//                setBarChartColumnTitles2(graphProperty.getBarChartColumnTitles2());
//            }
//            else{
            int allColumns = getBarChartColumnNames().length - viewByElementIds.length;
            int dividor = 2;
            int rightColumns = allColumns / dividor;
            int leftColumns = allColumns - rightColumns;
            setBarChartColumnTitles1(new String[leftColumns + viewByElementIds.length]);
            setBarChartColumnNames1(new String[getBarChartColumnTitles1().length]);
            setBarChartColumnTitles2(new String[viewByElementIds.length + rightColumns]);
            setBarChartColumnNames2(new String[getBarChartColumnTitles2().length]);

            for (int j = 0; j < viewByElementIds.length; j++) {
                getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j];
                getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                getBarChartColumnNames2()[j] = getBarChartColumnNames()[j];
            }
            for (int j = viewByElementIds.length; j < getBarChartColumnTitles1().length; j++) {
                getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
            }
            for (int j = viewByElementIds.length; j < getBarChartColumnTitles2().length; j++) {
                getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j + leftColumns];
                getBarChartColumnNames2()[j] = getBarChartColumnNames()[j + leftColumns];
            }
//        }
        }
        graph1 = new ProgenChartDatasets();
        graph2 = new ProgenChartDatasets();

        graph1.setBarChartColumnNames(getBarChartColumnNames1());
        graph1.setBarChartColumnTitles(getBarChartColumnTitles1());
        graph1.setPieChartColumns(getBarChartColumnNames1());
        graph1.setViewByColumns(viewByElementIds);
        graph1.setShowGT(showGT);
        graph1.setSuffixSymbol(nbrFormat);
        graph1.setPrefixSymbol(graphSymbol);
        graph1.setDisplayGraphRows(graphDisplayRows);
//        graph1.setShowlyAxis(showlyAxis);
//        graph1.setShowryAxis(showryAxis);
//        graph1.setShowxAxis(showxAxis);

        //added by k
        graph1.setStartindex(startindex);
        graph1.setEndindex(endindex);
        graph1.setGraphid(graphId + "," + getReportId());



        graph1.setColumnAverages(getColumnAverages());
        graph1.setColumnGrandTotals(getColumnGrandTotals());
        graph1.setColumnOverAllMaximums(getColumnOverAllMaximums());
        graph1.setColumnOverAllMinimums(getColumnOverAllMinimums());

        graph2.setBarChartColumnNames(getBarChartColumnNames2());
        graph2.setBarChartColumnTitles(getBarChartColumnTitles2());
        graph2.setPieChartColumns(getBarChartColumnNames2());
        graph2.setViewByColumns(viewByElementIds);
        graph2.setShowGT(showGT);
        graph2.setSuffixSymbol(nbrFormat);
        graph2.setPrefixSymbol(graphSymbol);
        graph2.setDisplayGraphRows(graphDisplayRows);
//        graph2.setShowlyAxis(showlyAxis);
//        graph2.setShowryAxis(showryAxis);
//        graph2.setShowxAxis(showxAxis);
        //added by k
        graph2.setStartindex(startindex);
        graph2.setEndindex(endindex);
        graph2.setGraphid(graphId + "," + getReportId());

        graph2.setColumnAverages(getColumnAverages());
        graph2.setColumnGrandTotals(getColumnGrandTotals());
        graph2.setColumnOverAllMaximums(getColumnOverAllMaximums());
        graph2.setColumnOverAllMinimums(getColumnOverAllMinimums());
        //need to commented later so as to optimize code
        graph1.setGraphProperty(graphProperty);
        graph2.setGraphProperty(graphProperty);
        graph1.setIsCrosstab(isCrosstab);
        graph2.setIsCrosstab(isCrosstab);
        graph1.setSortColumns(sortColumns);
        graph2.setSortColumns(sortColumns);

        pcharts.setGrpryaxislabel(grpryaxislabel);
        pcharts.setGraph1(graph1);
        pcharts.setGraph2(graph2);

        pchartsZoom.setGrpryaxislabel(grpryaxislabel);
        pchartsZoom.setGraph1(graph1);
        pchartsZoom.setGraph2(graph2);

        if (this.graphViewSeq != null) {
            graph1.setViewSequence(graphViewSeq);
            graph2.setViewSequence(graphViewSeq);
            graph.setViewSequence(graphViewSeq);
        }

        //added by santhosh.k on 16-02-2010 for target definded line on graph
        pcharts.setTargetRange(getTargetRange());
//        pcharts.setStartValue(getStartValue());
        if (this.noOfDays != 0) {
            pcharts.setStartValue(String.valueOf(getGraphProperty().getTargetPerDay() * this.noOfDays));
        } else {
            pcharts.setStartValue(String.valueOf(getGraphProperty().getTargetPerDay()));
        }
        pcharts.setEndValue(getEndValue());
        pcharts.setShowMinMaxRange(getShowMinMaxRange());
//        pcharts.setSwapColumn(SwapColumn);
        pchartsZoom.setTargetRange(getTargetRange());
//        pchartsZoom.setStartValue(getStartValue());
        if (this.noOfDays != 0) {
            pchartsZoom.setStartValue(String.valueOf(getGraphProperty().getTargetPerDay() * this.noOfDays));
        } else {
            pchartsZoom.setStartValue(String.valueOf(getGraphProperty().getTargetPerDay()));
        }
        pchartsZoom.setEndValue(getEndValue());
        pchartsZoom.setShowMinMaxRange(getShowMinMaxRange());
        pcharts.setGraphProperty(getGraphProperty());
        pchartsZoom.setGraphProperty(getGraphProperty());

        //end of code written by santhosh.k on 16-02-2010 for target definded line on graph
        //RowValuesList

//        pcharts.setGrpbDomainaxislabel(showxAxis);
//        pcharts.setGrplyaxislabel(showlyAxis);
//        pcharts.setGrpryaxislabel(showryAxis);


        pcharts.overWriteColorSeries(getGraphProperty().getColorSeries());

        if ((selectedgraphtype == null || selectedgraphtype.equalsIgnoreCase("jf")) && !selectedgraphtype.equalsIgnoreCase("jq")) {
            if (graphClassName.equalsIgnoreCase("Pie")) {
                // 
                pcharts.setMeasurePosition(measNamePosition);
//            
//            
//            
                for (int kk = 0; kk < getBarChartColumnTitles2().length; kk++) {
//                
//            
                }

                pcharts.setPieMeasureLabel(piChartMeasurelabel);
                //
                path.append(";").append(pcharts.GetPieAxisChart());
//            graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
//            
                // pathZoom = pathZoom + ";" + pchartsZoom.GetPieAxisChart();
            } else if (graphClassName.equalsIgnoreCase("Category")) {
                pcharts.setGrpbDomainaxislabel(showxAxis);
                pcharts.setGrplyaxislabel(showlyAxis);
                ////////////////.println("entered in category");
//            path = path + ";" + pcharts.GetCategoryAxisChart();
                path.append(";").append(pcharts.GetCategoryAxisChart());
//            graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
                //pathZoom = pathZoom + ";" + pchartsZoom.GetCategoryAxisChart();
            } else if (graphClassName.equalsIgnoreCase("Meter")) {
//            path = path + ";" + "#";
                path.append(";").append("#");
                //path = path + ";" + pcharts.GetCategoryAxisChart();
//            graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
                //pathZoom = pathZoom + ";" + pchartsZoom.GetCategoryAxisChart();
                //pathZoom = pathZoom + ";" + "#";
            } else if (graphClassName.equalsIgnoreCase("Candle Stick")) {
//            path = path + ";" + "#";
                path.append(";").append("#");
                //path = path + ";" + pcharts.GetCategoryAxisChart();
                graphTitle.append(";").append(graphName);
                //pathZoom = pathZoom + ";" + pchartsZoom.GetCategoryAxisChart();
                //pathZoom = pathZoom + ";" + "#";
            } else if (graphClassName.equalsIgnoreCase("Dial")) {
//            path = path + ";" + "#";
                path.append(";").append("#");
                //path = path + ";" + pcharts.GetCategoryAxisChart();
//            graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
                //pathZoom = pathZoom + ";" + pchartsZoom.GetCategoryAxisChart();
                //pathZoom = pathZoom + ";" + "#";
            } else if (graphClassName.equalsIgnoreCase("Cone")) {
//            path = path + ";" + "#";
                path.append(";").append("#");
                //path = path + ";" + pcharts.GetCategoryAxisChart();
//            graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
                //pathZoom = pathZoom + ";" + pchartsZoom.GetCategoryAxisChart();
                //pathZoom = pathZoom + ";" + "#";
            } else if (graphClassName.equalsIgnoreCase("Bubble")) {
                pcharts.setGrpbDomainaxislabel(showxAxis);
                pcharts.setGrplyaxislabel(showlyAxis);
//            path = path + ";" + pcharts.GetBubbleChart();
                path.append(";").append(pcharts.GetBubbleChart());
//            graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
                // pathZoom =pathZoom + ";" + pchartsZoom.GetBubbleChart();
            } else if (graphClassName.trim().equalsIgnoreCase("TimeSeries")) {
                pcharts.setGrpbDomainaxislabel(showxAxis);
                pcharts.setGrplyaxislabel(showlyAxis);

//            path = path + ";" + pcharts.GetTimeSeriesChart();
                path.append(";").append(pcharts.GetTimeSeriesChart());
//            graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
                // pathZoom = pathZoom + ";" + pchartsZoom.GetTimeSeriesChart();
            } else if (graphClassName.equalsIgnoreCase("Scatter")) {
                pcharts.setGrpbDomainaxislabel(showxAxis);
                pcharts.setGrplyaxislabel(showlyAxis);
//            path = path + ";" + pcharts.GetScatterChart();
                path.append(";").append(pcharts.GetScatterChart());
//            graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
//            pathZoom = pathZoom + ";" + pchartsZoom.GetScatterChart();
                pathZoom.append(";").append(pchartsZoom.GetScatterChart());
            } else if (graphClassName.equalsIgnoreCase("Area")) {
                pcharts.setGrpbDomainaxislabel(showxAxis);
                pcharts.setGrplyaxislabel(showlyAxis);
//            path = path + ";" + pcharts.GetCategoryAxisChart();
                path.append(";").append(pcharts.GetCategoryAxisChart());
//            graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
                //  pathZoom = pathZoom + ";" + pchartsZoom.GetCategoryAxisChart();
            } else if (graphClassName.equalsIgnoreCase("Dual Axis")) {//GetTimeSeriesChart
                if (graphTypeName.equalsIgnoreCase("Dual Axis")) {
                    pcharts.setGrpbDomainaxislabel(showxAxis);
                    pcharts.setGrplyaxislabel(showlyAxis);
                    pcharts.setGrpryaxislabel(showryAxis);
//                path = path + ";" + pcharts.GetDualAxisChart();
                    path.append(";").append(pcharts.GetDualAxisChart());
//                graphTitle = graphTitle + ";" + graphName;
                    graphTitle.append(";").append(graphName);
                    //    pathZoom = pathZoom + ";" + pchartsZoom.GetDualAxisChart();
                } else if (graphTypeName.equalsIgnoreCase("OverlaidBar")) {
                    pcharts.setGrpbDomainaxislabel(showxAxis);
                    pcharts.setGrplyaxislabel(showlyAxis);
                    //pcharts.setGrpryaxislabel(showryAxis);

//                path = path + ";" + pcharts.GetOverlaidBar();
                    path.append(";").append(pcharts.GetOverlaidBar());
//                graphTitle = graphTitle + ";" + graphName;
                    graphTitle.append(";").append(graphName);
                    //pathZoom = pathZoom + ";" + pchartsZoom.GetOverlaidBar();
                } else if (graphTypeName.equalsIgnoreCase("OverlaidArea")) {
                    pcharts.setGrpbDomainaxislabel(showxAxis);
                    pcharts.setGrplyaxislabel(showlyAxis);
                    //pcharts.setGrpryaxislabel(showryAxis);

//                path = path + ";" + pcharts.GetOverlaidArea();
                    path.append(";").append(pcharts.GetOverlaidArea());
//                graphTitle = graphTitle + ";" + graphName;
                    graphTitle.append(";").append(graphName);
                    // pathZoom = pathZoom + ";" + pchartsZoom.GetOverlaidArea();
                } else if (graphTypeName.equalsIgnoreCase("FeverChart")) {
//                path = path + ";" + pcharts.GetFeverChart();
                    path.append(";").append(pcharts.GetFeverChart());
//                graphTitle = graphTitle + ";" + graphName;
                    graphTitle.append(";").append(graphName);
                    //pathZoom = pathZoom + ";" + pchartsZoom.GetFeverChart();
                } else if (graphTypeName.equalsIgnoreCase("Pareto")) {
                    pcharts.setGrpbDomainaxislabel(showxAxis);
                    pcharts.setGrplyaxislabel(showlyAxis);
                    //pcharts.setGrpryaxislabel(showryAxis);

//                path = path + ";" + pcharts.GetParetoChart();
                    path.append(";").append(pcharts.GetParetoChart());
//                graphTitle = graphTitle + ";" + graphName;
                    graphTitle.append(";").append(graphName);
                    // pathZoom = pathZoom + ";" + pchartsZoom.GetParetoChart();
                } else if (graphTypeName.equalsIgnoreCase("Spider")) {
//                path = path + ";" + pcharts.GetSpiderChart();
                    path.append(";").append(pcharts.GetSpiderChart());
//                graphTitle = graphTitle + ";" + graphName;
                    graphTitle.append(";").append(graphName);
                    // pathZoom = pathZoom + ";" + pchartsZoom.GetSpiderChart();
                }



            } else if (graphClassName.equalsIgnoreCase("ProGenCharts")) {
//            path = path + ";" + pcharts.GetProGenChart(getProGenImgPath(), Integer.parseInt(graphWidth), Integer.parseInt(graphHeight));
                path.append(";").append(pcharts.GetProGenChart(getProGenImgPath(), Integer.parseInt(graphWidth), Integer.parseInt(graphHeight)));
                graphTitle.append(";").append(graphName);
                // pathZoom = pathZoom + ";" + pchartsZoom.GetProGenChart(getProGenImgPath(), Integer.parseInt(graphWidth), Integer.parseInt(graphHeight));
            } else {
//            path = path + ";" + "";
                path.append(";").append("");
//            graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
                // pathZoom = pathZoom + ";" + "";
            }
        }
        ProgenChartDisplayList.add(pcharts);
        ProgenChartDisplayList.add(pchartsZoom);
        return ProgenChartDisplayList;
    }

    public ArrayList buildGraphBuildingInfoForSettingGraphSize(ProgenChartDisplay pcharts, ProgenChartDisplay pchartsZoom) throws Exception {

        //////////////////////.println("abc");
        ArrayList ProgenChartDisplayList = new ArrayList();
        pcharts = new ProgenChartDisplay(480, 280, ctxPath);
        pchartsZoom = new ProgenChartDisplay(480, 280, ctxPath);
        graph = new ProgenChartDatasets();

        graph.setBarChartColumnNames(getBarChartColumnNames());
        graph.setViewByColumns(viewByElementIds);
        graph.setBarChartColumnTitles(getBarChartColumnTitles());
        graph.setPieChartColumns(getBarChartColumnNames());
        graph.setTimeLevel(getTimelevel());
        graph.setSuffixSymbol(nbrFormat);
        graph.setPrefixSymbol(graphSymbol);
//        graph.setPrefixSymbol(measurePosition);
        graph.setDisplayGraphRows(graphDisplayRows);
        graph.setShowGT(showGT);
        if (graphTypeName.equalsIgnoreCase("ColumnPie") || graphTypeName.equalsIgnoreCase("ColumnPie3D")) {
            graph.setSwapColumn(!SwapColumn);
        } else {
            graph.setSwapColumn(SwapColumn);
        }

//code to change graph types at run time
        if (getGraphHashMap() != null && getGraphHashMap().get(graphId) != null) {
            graphTypeName = String.valueOf(((HashMap) GraphHashMap.get(graphId)).get("graphTypeName"));
            graphClassName = String.valueOf(((HashMap) GraphHashMap.get(graphId)).get("graphClassName"));
            if (graphTypeName.equalsIgnoreCase("HorizCone")) {
                graphClassName = "Pie";
                graphTypeName = "Pie";
            }

            if (graphTypeName.equalsIgnoreCase("Pyramid")) {
                graphClassName = "Pie";
                graphTypeName = "Pie3D";
            }
            if (graphTypeName.equalsIgnoreCase("Cone")) {
                graphClassName = "Pie";
                graphTypeName = "ring";
            }
            if (graphTypeName.equalsIgnoreCase("PieRing")) {
                graphClassName = "Pie";
                graphTypeName = "pie3D";
            }




            graphSize = String.valueOf(((HashMap) GraphHashMap.get(graphId)).get("graphSize"));
            if (!fromOneview) {
                graphWidth = String.valueOf(((HashMap) GraphHashMap.get(graphId)).get("graphWidth"));
                graphHeight = String.valueOf(((HashMap) GraphHashMap.get(graphId)).get("graphHeight"));
            }

        }
//end of code to change graph types

        graph.setColumnAverages(getColumnAverages());
        graph.setColumnGrandTotals(getColumnGrandTotals());
        graph.setColumnOverAllMaximums(getColumnOverAllMaximums());
        graph.setColumnOverAllMinimums(getColumnOverAllMinimums());

        if (getMyGraphType() != null) {
            if (getMyGraphType() != null || getMyGraphType().equalsIgnoreCase("")) {
                if (getMyGraphType().equalsIgnoreCase("Bar")
                        || getMyGraphType().equalsIgnoreCase("bar3d")
                        || getMyGraphType().equalsIgnoreCase("line")
                        || getMyGraphType().equalsIgnoreCase("line3d")
                        || getMyGraphType().equalsIgnoreCase("layered")
                        || getMyGraphType().equalsIgnoreCase("stacked")
                        || getMyGraphType().equalsIgnoreCase("stackedarea")
                        || getMyGraphType().equalsIgnoreCase("stacked3d")
                        || getMyGraphType().equalsIgnoreCase("column")
                        || getMyGraphType().equalsIgnoreCase("column3d")
                        || getMyGraphType().equalsIgnoreCase("waterfall")
                        || getMyGraphType().equalsIgnoreCase("Area")
                        || getMyGraphType().equalsIgnoreCase("HorizontalStacked")
                        || getMyGraphType().equalsIgnoreCase("HorizontalStacked3d")) {
                    graphClassName = "Category";
                    graphTypeName = getMyGraphType();
                }

                if (getMyGraphType().equalsIgnoreCase("pie") || getMyGraphType().equalsIgnoreCase("pie3d") || getMyGraphType().equalsIgnoreCase("ring") || getMyGraphType().equalsIgnoreCase("ColumnPie") || getMyGraphType().equalsIgnoreCase("ColumnPie3D")) {
                    graphClassName = "Pie";
                    graphTypeName = getMyGraphType();
                }

                if (getMyGraphType().equalsIgnoreCase("Dual Axis") || getMyGraphType().equalsIgnoreCase("OverlaidBar") || getMyGraphType().equalsIgnoreCase("FeverChart") || getMyGraphType().equalsIgnoreCase("OverlaidArea") || getMyGraphType().equalsIgnoreCase("Pareto") || getMyGraphType().equalsIgnoreCase("Spider")) {
                    graphClassName = "Dual Axis";
                    graphTypeName = getMyGraphType();
                }


                if (getMyGraphType().equalsIgnoreCase("Meter") || getMyGraphType().equalsIgnoreCase("Thermometer")) {
                    graphClassName = "Meter";
                    graphTypeName = getMyGraphType();
                }
                if (getMyGraphType().equalsIgnoreCase("Dial")) {
                    graphClassName = "Dial";
                    graphTypeName = getMyGraphType();
                }

                if (getMyGraphType().equalsIgnoreCase("Bubble")) {
                    graphClassName = "Bubble";
                    graphTypeName = getMyGraphType();
                }
                if (getMyGraphType().equalsIgnoreCase("TimeSeries")) {
                    graphClassName = "TimeSeries";
                    graphTypeName = getMyGraphType();
                }


//Dual Axis
            }


        }

        // setMyGraphType("");

        graph.setGraphProperty(graphProperty);
        pcharts.setGraph(graph);
        pcharts.setChartType(graphTypeName);
        pcharts.setGrplyaxislabel(grpLYaxislabel);
        pcharts.setGrpryaxislabel(grpryaxislabel);
        pcharts.setGrpbDomainaxislabel(grpbDomainaxislabel);
        pcharts.setSession(getSession());
        pcharts.setResponse(getResponse());
        pcharts.setOut(getOut());
        pcharts.setGrplegendloc(grplegendloc);
        pchartsZoom.setGraph(graph);
        pchartsZoom.setChartType(graphTypeName);
        pchartsZoom.setGrplyaxislabel(grpLYaxislabel);
        pchartsZoom.setGrpryaxislabel(grpryaxislabel);
        pchartsZoom.setGrpbDomainaxislabel(grpbDomainaxislabel);
        pchartsZoom.setSession(getSession());
        pchartsZoom.setResponse(getResponse());
        pchartsZoom.setOut(getOut());
        pchartsZoom.setGrplegendloc(grplegendloc);
        pcharts.setSwapColumn(SwapColumn);
        pchartsZoom.setSwapColumn(SwapColumn);
        //graphGridLines
        pcharts.setGraphGridLines(getGraphGridLines());
        pchartsZoom.setGraphGridLines(getGraphGridLines());
        //added by k
        pcharts.setGrplegend(grplegend);
        pchartsZoom.setGrplegend(grplegend);
        //pcharts.setMeasureNamePosition(measNamePosition);
        // pchartsZoom.setMeasureNamePosition(measNamePosition);

        pcharts.setGraphProperty(getGraphProperty());
        pchartsZoom.setGraphProperty(getGraphProperty());

        if (graph.getSwapColumn()) {
            pcharts.setFunName(getJscal());
            pchartsZoom.setFunName(getJscal());
        }

        if (graphTypeName.equalsIgnoreCase("columnPie") || graphTypeName.equalsIgnoreCase("columnPie3D") || graphTypeName.equalsIgnoreCase("TimeSeries") || graphTypeName.equalsIgnoreCase("Scatter")) {
            if (getShowGT().equalsIgnoreCase("Y")) {
                pcharts.setRetObj(getAllDispRecordsRetObj());
                pchartsZoom.setRetObj(getAllDispRecordsRetObj());
            } else {
                pcharts.setRetObj(getAllDispRecordsRetObj());
                pchartsZoom.setRetObj(getAllDispRecordsRetObj());
            }

        } else {
            if (getShowGT().equalsIgnoreCase("Y")) {
                pcharts.setRetObj(getCurrentDispRetObjRecords());
                pchartsZoom.setRetObj(getCurrentDispRetObjRecords());
            } else {
                if (graphTypeName.equalsIgnoreCase("Waterfall")) {
                    graph.setShowGT("Y");
                    pcharts.setRetObj(getCurrentDispRetObjRecords());
                    pchartsZoom.setRetObj(getCurrentDispRetObjRecords());
                } else {
                    pcharts.setRetObj(getCurrentDispRetObjRecords());
                    pchartsZoom.setRetObj(getCurrentDispRetObjRecords());
                }
            }
        }
        pcharts.setRowValuesList(rowValuesList);
        pchartsZoom.setRowValuesList(rowValuesList);
        pcharts.setGrplegend(grplegend);
        pchartsZoom.setGrplegend(grplegend);

        pcharts.setGraphProperty(getGraphProperty());
        pchartsZoom.setGraphProperty(getGraphProperty());
//        String axis1 = "";
//        String axis2 = "";
        StringBuilder axis1 = new StringBuilder(1000);
        StringBuilder axis2 = new StringBuilder(1000);
        if (axis != null) {
            for (int ind = viewByElementIds.length; ind < axis.length; ind++) {
                //if (axis[ind] == null) {
                //axis[ind] = "0";
                //}
                if (axis[ind].equalsIgnoreCase("0")) {
//                                        axis1 = axis1 + "," + ind;
                    axis1.append(",").append(ind);
                } else {
//                                        axis2 = axis2 + "," + ind;
                    axis2.append(",").append(ind);
                }
            }
        }
//      if ((!axis1.equalsIgnoreCase("")) && (!axis2.equalsIgnoreCase(""))) {
        if ((axis1.length() > 0) && (axis2.length()) > 0) {
//          axis1 = axis1.substring(1);
//          axis2 = axis2.substring(1);
            axis1 = new StringBuilder(axis1.substring(1));
            axis2 = new StringBuilder(axis2.substring(1));

            String[] temp1 = axis1.toString().split(",");
            setBarChartColumnTitles1(new String[temp1.length + viewByElementIds.length]);
            setBarChartColumnNames1(new String[getBarChartColumnTitles1().length]);
            String[] temp2 = axis2.toString().split(",");
            setBarChartColumnTitles2(new String[temp2.length + viewByElementIds.length]);
            setBarChartColumnNames2(new String[getBarChartColumnTitles2().length]);
            for (int j = 0; j < viewByElementIds.length; j++) {
                getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j];

                getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                getBarChartColumnNames2()[j] = getBarChartColumnNames()[j];
            }
            for (int j = 0; j < temp1.length; j++) {
                getBarChartColumnTitles1()[viewByElementIds.length + j] = getBarChartColumnTitles()[Integer.parseInt(temp1[j])];
                getBarChartColumnNames1()[viewByElementIds.length + j] = getBarChartColumnNames()[Integer.parseInt(temp1[j])];
            }
            for (int j = 0; j < temp2.length; j++) {
                getBarChartColumnTitles2()[viewByElementIds.length + j] = getBarChartColumnTitles()[Integer.parseInt(temp2[j])];
                getBarChartColumnNames2()[viewByElementIds.length + j] = getBarChartColumnNames()[Integer.parseInt(temp2[j])];
            }
        } else {
            int allColumns = getBarChartColumnNames().length - viewByElementIds.length;
            int dividor = 2;
            int rightColumns = allColumns / dividor;
            int leftColumns = allColumns - rightColumns;
            setBarChartColumnTitles1(new String[leftColumns + viewByElementIds.length]);
            setBarChartColumnNames1(new String[getBarChartColumnTitles1().length]);
            setBarChartColumnTitles2(new String[viewByElementIds.length + rightColumns]);
            setBarChartColumnNames2(new String[getBarChartColumnTitles2().length]);

            for (int j = 0; j < viewByElementIds.length; j++) {
                getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j];
                getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
                getBarChartColumnNames2()[j] = getBarChartColumnNames()[j];
            }
            for (int j = viewByElementIds.length; j < getBarChartColumnTitles1().length; j++) {
                getBarChartColumnTitles1()[j] = getBarChartColumnTitles()[j];
                getBarChartColumnNames1()[j] = getBarChartColumnNames()[j];
            }
            for (int j = viewByElementIds.length; j < getBarChartColumnTitles2().length; j++) {
                getBarChartColumnTitles2()[j] = getBarChartColumnTitles()[j + leftColumns];
                getBarChartColumnNames2()[j] = getBarChartColumnNames()[j + leftColumns];
            }
        }
        graph1 = new ProgenChartDatasets();
        graph2 = new ProgenChartDatasets();

        graph1.setBarChartColumnNames(getBarChartColumnNames1());
        graph1.setBarChartColumnTitles(getBarChartColumnTitles1());
        graph1.setPieChartColumns(getBarChartColumnNames1());
        graph1.setViewByColumns(viewByElementIds);
        graph1.setShowGT(showGT);
        graph1.setSuffixSymbol(nbrFormat);
        graph1.setPrefixSymbol(graphSymbol);
        //graph1.setPrefixSymbol(measurePosition);
        graph1.setDisplayGraphRows(graphDisplayRows);
        graph1.setColumnAverages(getColumnAverages());
        graph1.setColumnGrandTotals(getColumnGrandTotals());
        graph1.setColumnOverAllMaximums(getColumnOverAllMaximums());
        graph1.setColumnOverAllMinimums(getColumnOverAllMinimums());

        graph2.setBarChartColumnNames(getBarChartColumnNames2());
        graph2.setBarChartColumnTitles(getBarChartColumnTitles2());
        graph2.setPieChartColumns(getBarChartColumnNames2());
        graph2.setViewByColumns(viewByElementIds);
        graph2.setShowGT(showGT);
        graph2.setSuffixSymbol(nbrFormat);
        graph2.setPrefixSymbol(graphSymbol);
        //graph2.setPrefixSymbol(measurePosition);
        graph2.setDisplayGraphRows(graphDisplayRows);
        graph2.setColumnAverages(getColumnAverages());
        graph2.setColumnGrandTotals(getColumnGrandTotals());
        graph2.setColumnOverAllMaximums(getColumnOverAllMaximums());
        graph2.setColumnOverAllMinimums(getColumnOverAllMinimums());
        //need to commented later so as to optimize code
        graph1.setGraphProperty(graphProperty);
        graph2.setGraphProperty(graphProperty);
        pcharts.setGrpryaxislabel(grpryaxislabel);
        pcharts.setGraph1(graph1);
        pcharts.setGraph2(graph2);

        pchartsZoom.setGrpryaxislabel(grpryaxislabel);
        pchartsZoom.setGraph1(graph1);
        pchartsZoom.setGraph2(graph2);


        //added by santhosh.k on 16-02-2010 for target definded line on graph
        pcharts.setTargetRange(getTargetRange());
        pcharts.setStartValue(getStartValue());
        pcharts.setEndValue(getEndValue());
        pcharts.setShowMinMaxRange(getShowMinMaxRange());
        pchartsZoom.setTargetRange(getTargetRange());
        pchartsZoom.setStartValue(getStartValue());
        pchartsZoom.setEndValue(getEndValue());
        pchartsZoom.setShowMinMaxRange(getShowMinMaxRange());
        pcharts.setGraphProperty(graphProperty);
        pchartsZoom.setGraphProperty(graphProperty);
        //end of code written by santhosh.k on 16-02-2010 for target definded line on graph
        //RowValuesList


        if (graphClassName.equalsIgnoreCase("Pie")) {
//            path = path + ";" + pcharts.GetPieAxisChart();
            path.append(";").append(pcharts.GetPieAxisChart());
//            graphTitle = graphTitle + ";" + graphName;
            graphTitle.append(";").append(graphName);
            // pathZoom = pathZoom + ";" + pchartsZoom.GetPieAxisChart();
        } else if (graphClassName.equalsIgnoreCase("Category")) {
//            path = path + ";" + pcharts.GetCategoryAxisChart();
            path.append(";").append(pcharts.GetCategoryAxisChart());
//            graphTitle = graphTitle + ";" + graphName;
            graphTitle.append(";").append(graphName);
            // pathZoom = pathZoom + ";" + pchartsZoom.GetCategoryAxisChart();
        } else if (graphClassName.equalsIgnoreCase("Meter")) {
//            path = path + ";" + "#";
            path.append(";").append("#");
            //path = path + ";" + pcharts.GetCategoryAxisChart();
//            graphTitle = graphTitle + ";" + graphName;
            graphTitle.append(";").append(graphName);
            //pathZoom = pathZoom + ";" + pchartsZoom.GetCategoryAxisChart();
            // pathZoom = pathZoom + ";" + "#";
        } else if (graphClassName.equalsIgnoreCase("Candle Stick")) {
//            path = path + ";" + "#";
            path.append(";").append("#");
            //path = path + ";" + pcharts.GetCategoryAxisChart();
//            graphTitle = graphTitle + ";" + graphName;
            graphTitle.append(";").append(graphName);
            //pathZoom = pathZoom + ";" + pchartsZoom.GetCategoryAxisChart();
            // pathZoom = pathZoom + ";" + "#";
        } else if (graphClassName.equalsIgnoreCase("Dial")) {
//            path = path + ";" + "#";
            path.append(";").append("#");
            //path = path + ";" + pcharts.GetCategoryAxisChart();
//            graphTitle = graphTitle + ";" + graphName;
            graphTitle.append(";").append(graphName);
            //pathZoom = pathZoom + ";" + pchartsZoom.GetCategoryAxisChart();
            //  pathZoom = pathZoom + ";" + "#";
        } else if (graphClassName.equalsIgnoreCase("Cone")) {
//            path = path + ";" + "#";
            path.append(";").append("#");
            //path = path + ";" + pcharts.GetCategoryAxisChart();
            graphTitle.append(";").append(graphName);
            //pathZoom = pathZoom + ";" + pchartsZoom.GetCategoryAxisChart();
            //   pathZoom = pathZoom + ";" + "#";
        } else if (graphClassName.equalsIgnoreCase("Bubble")) {
//            path = path + ";" + pcharts.GetBubbleChart();
            path.append(";").append(pcharts.GetBubbleChart());
//            graphTitle = graphTitle + ";" + graphName;
            graphTitle.append(";").append(graphName);
            //    pathZoom = pathZoom + ";" + pchartsZoom.GetBubbleChart();
        } else if (graphClassName.equalsIgnoreCase("TimeSeries")) {
//            path = path + ";" + pcharts.GetTimeSeriesChart();
            path.append(";").append(pcharts.GetTimeSeriesChart());
//            graphTitle = graphTitle + ";" + graphName;
            graphTitle.append(";").append(graphName);
            //  pathZoom = pathZoom + ";" + pchartsZoom.GetBubbleChart();
        } else if (graphClassName.equalsIgnoreCase("Scatter")) {
//            path = path + ";" + pcharts.GetScatterChart();
            path.append(";").append(pcharts.GetScatterChart());
//            graphTitle = graphTitle + ";" + graphName;
            graphTitle.append(";").append(graphName);
            // pathZoom = pathZoom + ";" + pchartsZoom.GetScatterChart();
        } else if (graphClassName.equalsIgnoreCase("Area")) {
//            path = path + ";" + pcharts.GetCategoryAxisChart();
            path.append(";").append(pcharts.GetCategoryAxisChart());
//            graphTitle = graphTitle + ";" + graphName;
            graphTitle.append(";").append(graphName);
            //  pathZoom = pathZoom + ";" + pchartsZoom.GetCategoryAxisChart();
        } else if (graphClassName.equalsIgnoreCase("Dual Axis")) {//GetTimeSeriesChart
            if (graphTypeName.equalsIgnoreCase("Dual Axis")) {
//                path = path + ";" + pcharts.GetDualAxisChart();
                path.append(";").append(pcharts.GetDualAxisChart());
//                graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
                //   pathZoom = pathZoom + ";" + pchartsZoom.GetDualAxisChart();
            } else if (graphTypeName.equalsIgnoreCase("OverlaidBar")) {
//                path = path + ";" + pcharts.GetOverlaidBar();
                path.append(";").append(pcharts.GetOverlaidBar());
//                graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
//                pathZoom = pathZoom + ";" + pchartsZoom.GetOverlaidBar();
                pathZoom.append(";").append(pchartsZoom.GetOverlaidBar());
            } else if (graphTypeName.equalsIgnoreCase("FeverChart")) {
                path.append(";").append(pcharts.GetFeverChart());
//                graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
                // pathZoom = pathZoom + ";" + pchartsZoom.GetFeverChart();
            } else if (graphTypeName.equalsIgnoreCase("OverlaidArea")) {
                path.append(";").append(pcharts.GetOverlaidArea());
//                graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
                //pathZoom = pathZoom + ";" + pchartsZoom.GetOverlaidArea();
            } else if (graphTypeName.equalsIgnoreCase("Pareto")) {
                path.append(";").append(pcharts.GetParetoChart());
//                graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
                // pathZoom = pathZoom + ";" + pchartsZoom.GetParetoChart();
            } else if (graphTypeName.equalsIgnoreCase("Spider")) {
//                path = path + ";" + pcharts.GetSpiderChart();
                path.append(";").append(pcharts.GetSpiderChart());

//                graphTitle = graphTitle + ";" + graphName;
                graphTitle.append(";").append(graphName);
                //pathZoom = pathZoom + ";" + pchartsZoom.GetSpiderChart();
            }




        } else if (graphClassName.equalsIgnoreCase("ProGenCharts")) {
//            path = path + ";" + pcharts.GetProGenChart(getProGenImgPath(), Integer.parseInt(graphWidth), Integer.parseInt(graphHeight));
            path.append(";").append(pcharts.GetProGenChart(getProGenImgPath(), Integer.parseInt(graphWidth), Integer.parseInt(graphHeight)));
//            graphTitle = graphTitle + ";" + graphName;
            graphTitle.append(";").append(graphName);
            // pathZoom = pathZoom + ";" + pchartsZoom.GetProGenChart(getProGenImgPath(), Integer.parseInt(graphWidth), Integer.parseInt(graphHeight));
        } else {
//            path = path + ";" + "";
            path.append(";").append("");
//            graphTitle = graphTitle + ";" + graphName;
            graphTitle.append(";").append(graphName);
            // pathZoom = pathZoom + ";" + "";
        }
        ProgenChartDisplayList.add(pcharts);
        ProgenChartDisplayList.add(pchartsZoom);
        return ProgenChartDisplayList;
    }

    public HashMap buildGraphWiseDetailsMap(HashMap singleGraphMapDetails) {
        singleGraphMapDetails.put("graphId", graphId);
        singleGraphMapDetails.put("graphName", graphName);
        if (fromOneview) {
            singleGraphMapDetails.put("graphWidth", graphWidth1);
            singleGraphMapDetails.put("graphHeight", graphHeight1);
        } else {
            singleGraphMapDetails.put("graphWidth", graphWidth);
            singleGraphMapDetails.put("graphHeight", graphHeight);
        }

        singleGraphMapDetails.put("graphClassName", graphClassName);
        singleGraphMapDetails.put("graphTypeName", graphTypeName);
        singleGraphMapDetails.put("graphSize", graphSize);
        singleGraphMapDetails.put("SwapColumn", String.valueOf(SwapColumn));
        singleGraphMapDetails.put("graphLegend", grplegend);
        singleGraphMapDetails.put("graphLegendLoc", grplegendloc);
        singleGraphMapDetails.put("graphshowX", grpshox);
        singleGraphMapDetails.put("graphshowY", grpshoy);
        singleGraphMapDetails.put("graphLYaxislabel", grpLYaxislabel);
        singleGraphMapDetails.put("graphRYaxislabel", grpryaxislabel);
        singleGraphMapDetails.put("grpbDomainaxislabel", grpbDomainaxislabel);
        singleGraphMapDetails.put("graphDrill", grpdrill);
        singleGraphMapDetails.put("graphBcolor", grpbcolor);
        singleGraphMapDetails.put("graphFcolor", grpfcolor);
        singleGraphMapDetails.put("graphData", grpdata);
        //storing graph details info in HashMap
        singleGraphMapDetails.put("barChartColumnNames", getBarChartColumnNames());
        singleGraphMapDetails.put("viewByElementIds", viewByElementIds);
        singleGraphMapDetails.put("viewByNames", viewByColNames);
        singleGraphMapDetails.put("barChartColumnTitles", getBarChartColumnTitles());
        singleGraphMapDetails.put("pieChartColumns", getPieChartColumns());
        singleGraphMapDetails.put("axis", axis);

        singleGraphMapDetails.put("GraphProperty", getGraphProperty());

        //storing graph details info (Dual Axis ) in HashMap
        singleGraphMapDetails.put("barChartColumnNames1", getBarChartColumnNames1());
        singleGraphMapDetails.put("barChartColumnTitles1", getBarChartColumnTitles1());
        singleGraphMapDetails.put("barChartColumnNames2", getBarChartColumnNames2());
        singleGraphMapDetails.put("barChartColumnTitles2", getBarChartColumnTitles2());
        singleGraphMapDetails.put("RowValuesList", rowValuesList);
        singleGraphMapDetails.put("showGT", showGT);
        singleGraphMapDetails.put("nbrFormat", nbrFormat);
        singleGraphMapDetails.put("graphSymbol", graphSymbol);
        singleGraphMapDetails.put("measureNamePosition", getGraphProperty().getMeasureNamePosition());
        singleGraphMapDetails.put("graphGridLines", getGraphGridLines());
        singleGraphMapDetails.put("graphDisplayRows", graphDisplayRows);
        singleGraphMapDetails.put("targetRange", getGraphProperty().getTargetValueType());
        //added by malli for target value.
        if (getGraphProperty().getTargetValueType().equalsIgnoreCase("Discrete")) {
            double currentStr = getGraphProperty().getStartValue();
            BigDecimal bd = new BigDecimal(currentStr);
            String output = NumberFormatter.getModifidNumber(bd);
            String value = output;
            singleGraphMapDetails.put("startValue", value);
        } else {
            singleGraphMapDetails.put("startValue", getGraphProperty().getStartValue());
        }
        singleGraphMapDetails.put("endValue", getEndValue());
        singleGraphMapDetails.put("showMinMaxRange", getShowMinMaxRange());
        singleGraphMapDetails.put("showlyAxis", getGraphProperty().getShowlyAxis());
        singleGraphMapDetails.put("showryAxis", getGraphProperty().getShowryAxis());
        singleGraphMapDetails.put("showxAxis", getGraphProperty().getShowxAxis());
        singleGraphMapDetails.put("stackedType", getGraphProperty().getStackedType());
        //added by k

        // startindex=(String.valueOf(singleGraphMapDetails.get("startindex")));
        // endindex=(String.valueOf(singleGraphMapDetails.get("endindex")));

        singleGraphMapDetails.put("startindex", startindex);
        singleGraphMapDetails.put("endindex", endindex);
        singleGraphMapDetails.put("showLabels", showLabels);
        singleGraphMapDetails.put("measureFormat", measureFormat);
        singleGraphMapDetails.put("measureValueRounding", measureValueRounding);
        singleGraphMapDetails.put("axisLabelPosition", axisLabelPosition);
        singleGraphMapDetails.put("calibration", getGraphProperty().getCalibration());
        singleGraphMapDetails.put("firstChartType", firstChartType);
        singleGraphMapDetails.put("secondChartType", secondChartType);
        singleGraphMapDetails.put("rgbColorArr", rgbColorArr);

        //////.println("buildGraphWiseDetailsMap");
        //////.println("startindex="+startindex);
        //////.println("endindex="+endindex);




        return singleGraphMapDetails;
    }

    public HashMap buildGraphWiseDetailsMap(HashMap singleGraphMapDetails, GraphReport graphDetails) {

        GraphProperty graphProp = graphDetails.getGraphProperty();
        singleGraphMapDetails.put("graphId", graphId);
        singleGraphMapDetails.put("graphName", graphName);
        if (fromOneview) {
            singleGraphMapDetails.put("graphWidth", graphWidth1);
            singleGraphMapDetails.put("graphHeight", graphHeight1);
        } else {
            singleGraphMapDetails.put("graphWidth", graphWidth);
            singleGraphMapDetails.put("graphHeight", graphHeight);
        }
        singleGraphMapDetails.put("graphClassName", graphClassName);
        singleGraphMapDetails.put("graphTypeName", graphTypeName);
        singleGraphMapDetails.put("graphSize", graphSize);
        singleGraphMapDetails.put("SwapColumn", graphProp.getSwapGraphColumns());
        grplegend = "N";
        if (graphDetails.isLegendAllowed()) {
            grplegend = "Y";
        }
        singleGraphMapDetails.put("graphLegend", grplegend);
        singleGraphMapDetails.put("graphLegendLoc", graphDetails.getLegendLocation());
        grpshox = "N";
        grpshoy = "N";
        if (graphDetails.isShowXAxisGrid()) {
            grpshox = "Y";
        }
        if (graphDetails.isShowYAxisGrid()) {
            grpshoy = "Y";
        }
        singleGraphMapDetails.put("graphshowX", grpshox);
        singleGraphMapDetails.put("graphshowY", grpshoy);
        singleGraphMapDetails.put("graphLYaxislabel", graphDetails.getLeftYAxisLabel());
        singleGraphMapDetails.put("graphRYaxislabel", graphDetails.getRightYAxisLabel());
        grpdrill = "N";
        if (graphDetails.isLinkAllowed()) {
            grpdrill = "Y";
        }
        singleGraphMapDetails.put("graphDrill", grpdrill);
        singleGraphMapDetails.put("graphBcolor", graphDetails.getBackgroundColor());
        singleGraphMapDetails.put("graphFcolor", graphDetails.getFontColor());
        grpdata = "N";
        if (graphDetails.isShowData()) {
            grpdata = "Y";
        }
        singleGraphMapDetails.put("graphData", grpdata);
        //storing graph details info in HashMap
        singleGraphMapDetails.put("barChartColumnNames", getBarChartColumnNames());
        singleGraphMapDetails.put("viewByElementIds", viewByElementIds);
        singleGraphMapDetails.put("viewByNames", viewByColNames);
        singleGraphMapDetails.put("barChartColumnTitles", getBarChartColumnTitles());
        singleGraphMapDetails.put("pieChartColumns", getPieChartColumns());
        singleGraphMapDetails.put("axis", axis);

        singleGraphMapDetails.put("GraphProperty", graphProp);

        //storing graph details info (Dual Axis ) in HashMap
        singleGraphMapDetails.put("barChartColumnNames1", getBarChartColumnNames1());
        singleGraphMapDetails.put("barChartColumnTitles1", getBarChartColumnTitles1());
        singleGraphMapDetails.put("barChartColumnNames2", getBarChartColumnNames2());
        singleGraphMapDetails.put("barChartColumnTitles2", getBarChartColumnTitles2());
        singleGraphMapDetails.put("RowValuesList", rowValuesList);
        if (graphDetails.isShowGT()) {
            showGT = "Y";
        }
        singleGraphMapDetails.put("showGT", showGT);
        singleGraphMapDetails.put("nbrFormat", graphProp.getNumberFormat());
        singleGraphMapDetails.put("graphSymbol", graphProp.getSymbol());
//        singleGraphMapDetails.put("measurePosition", graphProp.getMeasurePosition());
        singleGraphMapDetails.put("graphGridLines", "Y");
        singleGraphMapDetails.put("graphDisplayRows", graphDetails.getDisplayRows());
        singleGraphMapDetails.put("targetRange", getTargetRange());
        singleGraphMapDetails.put("startValue", graphProp.getStartValue());
        singleGraphMapDetails.put("endValue", graphProp.getEndValue());
        singleGraphMapDetails.put("showMinMaxRange", graphProp.getMinMaxRange());


        singleGraphMapDetails.put("startindex", graphProp.getStartValue());
        singleGraphMapDetails.put("endindex", graphProp.getEndValue());
        if (graphProp.isStackedFlag()) {
            singleGraphMapDetails.put("stackedType", graphProp.getStackedType());
        } else {
            singleGraphMapDetails.put("stackedType", stackedType);
        }
        return singleGraphMapDetails;
    }

    public String getProGenImgPath() {
        return ProGenImgPath;
    }

    public void setProGenImgPath(String ProGenImgPath) {
        this.ProGenImgPath = ProGenImgPath;
    }

    public boolean memberValueExists(PbReturnObject retObj, ArrayList RowValues) {
        String[] dbColumns2 = null;
        boolean exists = false;
        dbColumns2 = retObj.getColumnNames();
        for (int index = 0; index < retObj.getRowCount(); index++) {
            for (String str : dbColumns2) {
                if (RowValues.contains(retObj.getFieldValueString(index, str))) {
                    exists = true;
                }
            }
        }
        return exists;
    }

    public boolean checkViewByExists(String viewBy, ArrayList RowValues) {
        boolean exists = false;
        for (int j = 0; j < RowValues.size(); j++) {
            if (viewBy.contains(RowValues.get(j).toString())) {
                exists = true;
            }
        }
        return exists;
    }

    public TreeMap getColumnOverAllMaximums() {
        return columnOverAllMaximums;
    }

    public void setColumnOverAllMaximums(TreeMap columnOverAllMaximums) {
        this.columnOverAllMaximums = columnOverAllMaximums;
    }

    public TreeMap getColumnOverAllMinimums() {
        return columnOverAllMinimums;
    }

    public void setColumnOverAllMinimums(TreeMap columnOverAllMinimums) {
        this.columnOverAllMinimums = columnOverAllMinimums;
    }

    public TreeMap getColumnAverages() {
        return columnAverages;
    }

    public void setColumnAverages(TreeMap columnAverages) {
        this.columnAverages = columnAverages;
    }

    public TreeMap getColumnGrandTotals() {
        return columnGrandTotals;
    }

    public void setColumnGrandTotals(TreeMap columnGrandTotals) {
        this.columnGrandTotals = columnGrandTotals;
    }

    public static void main(String[] args) {
        int i = 1;
        int j = i / 2;
        int k = i - j;


    }

    public HashMap getGraphSizesDtlsHashMap() {
        return GraphSizesDtlsHashMap;
    }

    public void setGraphSizesDtlsHashMap(HashMap GraphSizesDtlsHashMap) {
        this.GraphSizesDtlsHashMap = GraphSizesDtlsHashMap;
    }

    //added by uday on 20-mar-2010
    public HashMap getWhatIfRanges() {
        return whatIfRanges;
    }

    public void setWhatIfRanges(HashMap whatIfRanges) {
        this.whatIfRanges = whatIfRanges;
    }

    public String getWhatIfScenarioId() {
        return whatIfScenarioId;
    }

    public void setWhatIfScenarioId(String whatIfScenarioId) {
        this.whatIfScenarioId = whatIfScenarioId;
    }

    public String getMyGraphType() {
        return myGraphType;
    }

    public void setMyGraphType(String myGraphType) {
        this.myGraphType = myGraphType;
    }

    public String getToolTipType() {
        return ToolTipType;
    }

    public void setToolTipType(String ToolTipType) {
        this.ToolTipType = ToolTipType;
    }

    public String getStartindex() {
        return startindex;
    }

    public void setStartindex(String startindex) {
        this.startindex = startindex;
    }

    public String getEndindex() {
        return endindex;
    }

    public void setEndindex(String endindex) {
        this.endindex = endindex;
    }

    public void setUITheme(String theme) {
        this.uiTheme = theme;
    }

    public String getUITheme() {
        return uiTheme;
    }

    public void setResolution(Integer resolution) {
        this.screenResolution = resolution;
    }

    public Integer getResolution() {
        return screenResolution;
    }

    public GraphProperty getGraphProperty() {
        return graphProperty;
    }

    public void setGraphProperty(GraphProperty graphProperty) {
        this.graphProperty = graphProperty;
    }

    public GraphProperty parseGraphPropertyXml(String xml) {
        GraphProperty property = new GraphProperty();
        Document document;
        Element root = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            if (xml != null && !"".equalsIgnoreCase(xml)) {
                //started by Nazneen on 24 May 2014 for values having > or < signs
//                
                xml = xml.toString().replace(" > ", "gt").replace(" < ", "lt").replace(">>", "> gt").replace("<<", "< lt").replace("< ", "gt").replace(" >", "lt");
//                
                //ended by Nazneen on 24 May 2014 for values having > or < signs
                document = builder.build(new ByteArrayInputStream(xml.toString().getBytes()));
                root = document.getRootElement();
                List labelsLst = root.getChildren("Show_Labels");
                if (labelsLst != null && !labelsLst.isEmpty()) {
                    Element labelEle = (Element) labelsLst.get(0);
                    property.setLabelsDisplayed(Boolean.parseBoolean(labelEle.getText()));
                } else {
                    property.setLabelsDisplayed(false);
                }

                List endValLst = root.getChildren("endValue");
                if (endValLst != null && !endValLst.isEmpty()) {
                    Element endValEle = (Element) endValLst.get(0);
                    property.setEndValue(Double.parseDouble(endValEle.getText()));
                } else {
                    property.setEndValue(10);
                }

                List startValLst = root.getChildren("startValue");
                if (startValLst != null && !startValLst.isEmpty()) {
                    Element stValEle = (Element) startValLst.get(0);
                    property.setStartValue(Double.parseDouble(stValEle.getText()));
                } else {
                    property.setStartValue(0);
                }

                List numFmtLst = root.getChildren("numberFormat");
                if (numFmtLst != null && !numFmtLst.isEmpty()) {
                    Element numFmtEle = (Element) numFmtLst.get(0);
                    property.setNumberFormat(numFmtEle.getText());
                } else {
                    property.setNumberFormat("");
                }

                List symbolLst = root.getChildren("symbol");
                if (symbolLst != null && !symbolLst.isEmpty()) {
                    Element symbolEle = (Element) symbolLst.get(0);
                    property.setSymbol(symbolEle.getText());
                } else {
                    property.setSymbol("");
                }

                List swapColsLst = root.getChildren("swapColumns");
                if (swapColsLst != null && !swapColsLst.isEmpty()) {
                    Element swapColsEle = (Element) swapColsLst.get(0);
                    property.setSwapGraphColumns(swapColsEle.getText());
                } else {
                    property.setSwapGraphColumns("");
                }

                List minMaxRangeLst = root.getChildren("minMaxRange");
                if (minMaxRangeLst != null && !minMaxRangeLst.isEmpty()) {
                    Element minMaxRangeEle = (Element) minMaxRangeLst.get(0);
                    property.setMinMaxRange(Boolean.parseBoolean(minMaxRangeEle.getText()));
                } else {
                    property.setMinMaxRange(false);
                }

                List showlyAxisLst = root.getChildren("showlyAxis");
                if (showlyAxisLst != null && !showlyAxisLst.isEmpty()) {
                    Element showlyAxisEle = (Element) showlyAxisLst.get(0);
                    property.setShowlyAxis(showlyAxisEle.getText());
                } else {
                    property.setShowlyAxis("");
                }

                List showryAxisLst = root.getChildren("showryAxis");
                if (showryAxisLst != null && !showryAxisLst.isEmpty()) {
                    Element showryAxisEle = (Element) showryAxisLst.get(0);
                    property.setShowryAxis(showryAxisEle.getText());
                } else {
                    property.setShowryAxis("");
                }

                List showxAxisLst = root.getChildren("showxAxis");
                if (showxAxisLst != null && !showxAxisLst.isEmpty()) {
                    Element showxAxisEle = (Element) showxAxisLst.get(0);
                    property.setShowxAxis(showxAxisEle.getText());
                } else {
                    property.setShowxAxis("");
                }

                List measureNamePositionLst = root.getChildren("measureNamePosition");
                if (measureNamePositionLst != null && !measureNamePositionLst.isEmpty()) {
                    Element measureNamePositionEle = (Element) measureNamePositionLst.get(0);
                    property.setMeasurePosition(measureNamePositionEle.getText());
                } else {
                    property.setMeasurePosition("");
                }

                List showOverAllMinLst = root.getChildren("showOverAllMin");
                if (showOverAllMinLst != null && !showOverAllMinLst.isEmpty()) {
                    Element showOverAllMinEle = (Element) showOverAllMinLst.get(0);
                    property.setShowOverAllMin(showOverAllMinEle.getText());
                } else {
                    property.setShowOverAllMin("");
                }

                List showOverAllMaxLst = root.getChildren("showOverAllMax");
                if (showOverAllMaxLst != null && !showOverAllMaxLst.isEmpty()) {
                    Element showOverAllMaxEle = (Element) showOverAllMaxLst.get(0);
                    property.setShowOverAllMax(showOverAllMaxEle.getText());
                } else {
                    property.setShowOverAllMax("");
                }

                List showCategoryMinLst = root.getChildren("showCategoryMin");
                if (showCategoryMinLst != null && !showCategoryMinLst.isEmpty()) {
                    Element showCategoryMinEle = (Element) showCategoryMinLst.get(0);
                    property.setShowCategoryMin(showCategoryMinEle.getText());
                } else {
                    property.setShowCategoryMin("");
                }

                List showCategoryMaxLst = root.getChildren("showCategoryMax");
                if (showCategoryMaxLst != null && !showCategoryMaxLst.isEmpty()) {
                    Element showCategoryMaxEle = (Element) showCategoryMaxLst.get(0);
                    property.setShowCategoryMax(showCategoryMaxEle.getText());
                } else {
                    property.setShowCategoryMax("");
                }

                String[] barChartDetails = null;
                List barChartColumnNames1Lst = root.getChildren("barChartColumnNames1");
                if (barChartColumnNames1Lst != null && !barChartColumnNames1Lst.isEmpty()) {
                    Element barChartColumnNames1Ele = (Element) barChartColumnNames1Lst.get(0);
                    List columnNameLst = barChartColumnNames1Ele.getChildren("barChartColumnName");
                    barChartDetails = new String[columnNameLst.size()];
                    for (int i = 0; i < columnNameLst.size(); i++) {
                        Element columnNameEle = (Element) columnNameLst.get(i);
                        barChartDetails[i] = columnNameEle.getText();
                    }
                    property.setBarChartColumnNames1(barChartDetails);
                } else {
                    barChartDetails = new String[barChartColumnNames1Lst.size()];
                    property.setBarChartColumnNames1(barChartDetails);
                }
                //added by srikanth.p to convert colorseriesxml to colorseries Array
                String[] colorSeries = null;
                String clr = "";
                List colorSeriesList = root.getChildren("colorSeries");
                if (colorSeriesList != null && !colorSeriesList.isEmpty()) {
                    Element colorSeriesEle = (Element) colorSeriesList.get(0);
                    List columnNameLst = colorSeriesEle.getChildren("clr");
                    colorSeries = new String[columnNameLst.size()];
                    for (int i = 0; i < columnNameLst.size(); i++) {
                        Element columnNameEle = (Element) columnNameLst.get(i);
                        colorSeries[i] = columnNameEle.getText();
                    }
                    property.setColorSeries(colorSeries);
                } else {
                    colorSeries = new String[barChartColumnNames1Lst.size()];
                    property.setColorSeries(colorSeries);
                }


                List barChartColumnNames2Lst = root.getChildren("barChartColumnNames2");
                if (barChartColumnNames2Lst != null && !barChartColumnNames2Lst.isEmpty()) {
                    Element barChartColumnNames2Ele = (Element) barChartColumnNames2Lst.get(0);
                    List columnNameLst = barChartColumnNames2Ele.getChildren("barChartColumnName");
                    barChartDetails = new String[columnNameLst.size()];
                    for (int i = 0; i < columnNameLst.size(); i++) {
                        Element columnNameEle = (Element) columnNameLst.get(i);
                        barChartDetails[i] = columnNameEle.getText();
                    }
                    property.setBarChartColumnNames2(barChartDetails);
                } else {
                    barChartDetails = new String[barChartColumnNames2Lst.size()];
                    property.setBarChartColumnNames2(barChartDetails);
                }


                List barChartColumnTitles1Lst = root.getChildren("barChartColumnTitles1");
                if (barChartColumnTitles1Lst != null && !barChartColumnTitles1Lst.isEmpty()) {
                    Element barChartColumnTitles1Ele = (Element) barChartColumnTitles1Lst.get(0);
                    List columnTitleLst = barChartColumnTitles1Ele.getChildren("barChartColumnTitle");
                    barChartDetails = new String[columnTitleLst.size()];
                    for (int i = 0; i < columnTitleLst.size(); i++) {
                        Element columnTitleEle = (Element) columnTitleLst.get(i);
                        barChartDetails[i] = columnTitleEle.getText();
                    }
                    property.setBarChartColumnTitles1(barChartDetails);
                } else {
                    barChartDetails = new String[barChartColumnNames2Lst.size()];
                    property.setBarChartColumnTitles1(barChartDetails);
                }

                List barChartColumnTitles2Lst = root.getChildren("barChartColumnTitles2");
                if (barChartColumnTitles2Lst != null && !barChartColumnTitles2Lst.isEmpty()) {
                    Element barChartColumnTitles2Ele = (Element) barChartColumnTitles2Lst.get(0);
                    List columnTitleLst = barChartColumnTitles2Ele.getChildren("barChartColumnTitle");
                    barChartDetails = new String[columnTitleLst.size()];
                    for (int i = 0; i < columnTitleLst.size(); i++) {
                        Element columnTitleEle = (Element) columnTitleLst.get(i);
                        barChartDetails[i] = columnTitleEle.getText();
                    }
                    property.setBarChartColumnTitles2(barChartDetails);
                } else {
                    barChartDetails = new String[barChartColumnNames2Lst.size()];
                    property.setBarChartColumnTitles2(barChartDetails);
                }

                List targetValLst = root.getChildren("targetValueType");
                if (targetValLst != null && !targetValLst.isEmpty()) {
                    Element tarValEle = (Element) targetValLst.get(0);
                    property.setTargetValueType(tarValEle.getText());
                } else {
                    property.setTargetValueType("");
                }
                List fromDateLst = root.getChildren("fromDate");
                if (fromDateLst != null && !fromDateLst.isEmpty()) {
                    Element fDateEle = (Element) fromDateLst.get(0);
                    property.setFromDate(fDateEle.getText());
                } else {
                    property.setFromDate("");
                }
                List toDateLst = root.getChildren("toDate");
                if (toDateLst != null && !toDateLst.isEmpty()) {
                    Element toDateEle = (Element) toDateLst.get(0);
                    property.setToDate(toDateEle.getText());
                } else {
                    property.setToDate("");
                }
                List trgRangeLst = root.getChildren("targetPerDay");
                if (trgRangeLst != null && !trgRangeLst.isEmpty()) {
                    Element trgRangeEle = (Element) trgRangeLst.get(0);
                    property.setTargetPerDay(Double.parseDouble(trgRangeEle.getText()));
                } else {
                    property.setTargetPerDay(0.0);
                }
                List stackedTypLst = root.getChildren("stackedType");
                if (stackedTypLst != null && !stackedTypLst.isEmpty()) {
                    Element stackTypetEle = (Element) stackedTypLst.get(0);
                    property.setStackedType(stackTypetEle.getText());
                } else {
                    property.setStackedType("");
                }
                List measureValueRounding = root.getChildren("measureValueRounding");
                if (measureValueRounding != null && !measureValueRounding.isEmpty()) {
                    Element Rounding = (Element) measureValueRounding.get(0);
                    property.setMeasureValueRounding(Rounding.getText());
                } else {
                    property.setMeasureValueRounding("");
                }

                List axisLabelPosition = root.getChildren("axisLabelPosition");
                if (axisLabelPosition != null && !axisLabelPosition.isEmpty()) {
                    Element labelPosition = (Element) axisLabelPosition.get(0);
                    property.setAxisLabelPosition(labelPosition.getText());
                } else {
                    property.setAxisLabelPosition("");
                }
                List graphDisplayRows = root.getChildren("graphDisplayRows");
                if (graphDisplayRows != null && !graphDisplayRows.isEmpty()) {
                    Element DisplayRows = (Element) graphDisplayRows.get(0);
                    property.setGraphDisplayRows(DisplayRows.getText());
                } else {
                    property.setGraphDisplayRows("");
                }

                List measureFormat = root.getChildren("measureFormat");
                if (measureFormat != null && !measureFormat.isEmpty()) {
                    Element measurefrmt = (Element) measureFormat.get(0);
                    property.setMeasureFormat(measurefrmt.getText());
                } else {
                    property.setMeasureFormat("");
                }
                List calibration = root.getChildren("calibration");
                if (calibration != null && !calibration.isEmpty()) {
                    Element axiscalibration = (Element) calibration.get(0);
                    property.setCalibration(axiscalibration.getText());
                } else {
                    property.setCalibration("");
                }
                List firstcharttype = root.getChildren("firstChartType");
                if (firstcharttype != null && !firstcharttype.isEmpty()) {
                    Element axisfirstChartType = (Element) firstcharttype.get(0);
                    property.setFirstChartType(axisfirstChartType.getText());
                } else {
                    property.setFirstChartType("");
                }
                List secondcharttype = root.getChildren("secondChartType");
                if (secondcharttype != null && !secondcharttype.isEmpty()) {
                    Element axissecondChartType = (Element) secondcharttype.get(0);
                    property.setSecondChartType(axissecondChartType.getText());
                } else {
                    property.setSecondChartType("");
                }
                String[] finalrgbColors = null;
                List rgbColorArraylist = root.getChildren("rgbColorArray");
                if (rgbColorArraylist != null && !rgbColorArraylist.isEmpty()) {
                    Element rgbColorArrayEle = (Element) rgbColorArraylist.get(0);
                    List rgbColorArrLst = rgbColorArrayEle.getChildren("rgbColorArr");
                    finalrgbColors = new String[rgbColorArrLst.size()];
                    for (int i = 0; i < rgbColorArrLst.size(); i++) {
                        Element rgbColorArrEle = (Element) rgbColorArrLst.get(i);
                        finalrgbColors[i] = rgbColorArrEle.getText();
                    }
                    property.setRgbColorArr(finalrgbColors);
                } else {
                    // finalrgbColors=new String[rgbColorArraylist.size()];
                    property.setRgbColorArr(null);
                }
                List gridLines = root.getChildren("Show_Grid");
                if (gridLines != null && !gridLines.isEmpty()) {
                    Element gridLinesEle = (Element) gridLines.get(0);
                    property.setGraphGridLines(gridLinesEle.getText());
                } else {
                    property.setGraphGridLines("Y");
                }

            } else {

                property.setLabelsDisplayed(false);
                property.setEndValue(10);
                property.setStartValue(0);
                property.setNumberFormat("");
                property.setSymbol("");
                property.setSwapGraphColumns("");
                property.setMinMaxRange(false);
                property.setMeasurePosition(measNamePosition);
                property.setShowlyAxis(showlyAxis);
                property.setShowryAxis(showryAxis);
                property.setShowxAxis(showxAxis);
                property.setTargetPerDay(0.0);
                property.setTargetValueType("");
                property.setToDate("");
                property.setFromDate("");
                property.setStackedType(stackedType);
                property.setMeasureValueRounding("");
                property.setAxisLabelPosition("");
                property.setMeasureFormat("");
                property.setCalibration("");
                property.setGraphDisplayRows("");
                property.setRgbColorArr(null);
                property.setGraphGridLines(graphGridLines);
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return property;
    }

    private void getGraphDetailsFromDashlet(DashletDetail dashlet) {

        GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();

        graphId = dashlet.getGraphId();
        graphName = dashlet.getDashletName();
        if (!fromOneview) {
            graphWidth = graphDetails.getGraphWidth();
            graphHeight = graphDetails.getGraphHeight();
        }
        graphClassName = graphDetails.getGraphClassName();
        graphTypeName = graphDetails.getGraphTypeName();
        graphSize = graphDetails.getGraphSizeName();

        if (getGraphSizesDtlsHashMap() != null) {
            if (getGraphSizesDtlsHashMap().get(graphSize) != null) {
                ArrayList sizedetails = (ArrayList) getGraphSizesDtlsHashMap().get(graphSize);
                if (!fromOneview) {
                    graphWidth = String.valueOf(sizedetails.get(0));
                    graphHeight = String.valueOf(sizedetails.get(1));
                }
            }
        }

        grplegendloc = graphDetails.getLegendLocation();
        grplegend = "N";
        grpshox = "N";
        grpshoy = "N";
        grpdrill = "N";
        grpdata = "N";

        if (graphDetails.isLegendAllowed()) {
            grplegend = "Y";
        }
        if (graphDetails.isShowXAxisGrid()) {
            grpshox = "Y";
        }
        if (graphDetails.isShowYAxisGrid()) {
            grpshoy = "Y";
        }
        if (graphDetails.isLinkAllowed()) {
            grpdrill = "Y";
        }

        grpbcolor = graphDetails.getBackgroundColor();
        grpfcolor = graphDetails.getFontColor();
        if (graphDetails.isShowData()) {
            grpdata = "Y";
        }


        grpLYaxislabel = graphDetails.getLeftYAxisLabel();
        grpryaxislabel = graphDetails.getRightYAxisLabel();
        grpbDomainaxislabel = graphDetails.getXAxisLabel();
        setGraphProperty(graphDetails.getGraphProperty());

        if (graphTypeName.equalsIgnoreCase("columnPie") || graphTypeName.equalsIgnoreCase("columnPie3D")) {
            rowValuesList = getRowValuesList(graphDetails.getRowValues());
            if (rowValuesList == null) {
                rowValuesList = new ArrayList();
            }
        } else {
            rowValuesList = new ArrayList();
        }

        showGT = "N";
        if (graphDetails.isShowGT()) {
            showGT = "Y";
        }

        GraphProperty graphProp = graphDetails.getGraphProperty();
        SwapColumn = Boolean.parseBoolean(graphProp.getSwapGraphColumns());
        nbrFormat = graphProp.getNumberFormat();
        graphSymbol = graphProp.getSymbol();
        measNamePosition = graphProp.getMeasureNamePosition();
        graphGridLines = "N";
        if (graphDetails.isShowXAxisGrid() && graphDetails.isShowYAxisGrid()) {
            graphGridLines = "Y";
        }
        graphDisplayRows = graphDetails.getDisplayRows();
        targetRange = null;
        startValue = String.valueOf(graphProp.getStartValue());
        endValue = String.valueOf(graphProp.getEndValue());
        showMinMaxRange = "N";
        if (graphProp.getMinMaxRange()) {
            showMinMaxRange = "Y";
        }
        startindex = "0";
        endindex = graphDisplayRows;

        setGraphId(graphId);
        if (graphTypeName.equalsIgnoreCase("TimeSeries")) {
            timeSeries = true;
        }
    }

    public void setPiChartMeasurelabel(String piChartMeasurelabel) {
        this.piChartMeasurelabel = piChartMeasurelabel;
    }
//    public void setCatChartMeasurelabel(String catChartMeasurelabel){
//            this.catChartMeasurelabel=catChartMeasurelabel;
//    }

    public void setShowlyAxis(String showlyAxis) {
        this.showlyAxis = showlyAxis;
    }

    public void setShowryAxis(String showryAxis) {
        this.showryAxis = showryAxis;
    }

    public void setShowxAxis(String showxAxis) {
        this.showxAxis = showxAxis;
    }

    public void setGraphColumnNamesAndTitles(Container container, String graphId) {
        String[] viewbysTemp = (String[]) container.getViewByElementIds().toArray(new String[0]);//new String[container.getViewByElementIds().size()];
        ArrayList<String> chartCols = new ArrayList<String>();
        ArrayList<String> chartLabels = new ArrayList<String>();
        PbReturnObject newCrossRetObj = new PbReturnObject();
        ArrayList finalColViewSortedValues = new ArrayList();
        ArrayList elemArray = new ArrayList();
        String[] barChartColumnNames = null;
        String[] barChartColumnTitles = null;
        String[] elemNames = null;
        newCrossRetObj = (PbReturnObject) container.getRetObj();
        if (container.getGraphCrossTabMeas() != null && !container.getGraphCrossTabMeas().isEmpty()) {
            elemNames = container.getGraphCrossTabMeas().split(",");
            for (int i = 0; i < elemNames.length; i++) {
                elemArray.add(elemNames[i]);
            }
        }

        int whichMeasure;
        int initValue = container.getViewByCount();
        if (container.getGrpIdElementIdMap().get(graphId) != null && container.getDisplayColumns().indexOf(container.getGrpIdElementIdMap().get(graphId)) > 0) {
            whichMeasure = container.getDisplayColumns().indexOf(container.getGrpIdElementIdMap().get(graphId));
        } else {
            whichMeasure = container.getViewByCount();
        }

        HashMap summarizedmMesMap = container.getSummerizedTableHashMap();
        ArrayList summmeas = new ArrayList();
        ArrayList summmeasTitle = new ArrayList();
        if (container.isSummarizedMeasuresEnabled() && summarizedmMesMap != null && !summarizedmMesMap.isEmpty()) {
            summmeas.addAll((List<String>) summarizedmMesMap.get("summerizedQryeIds"));
            summmeasTitle.addAll((List<String>) summarizedmMesMap.get("summerizedQryColNames"));
            whichMeasure += summmeas.size();
            initValue += summmeas.size();
        }

        ArrayList LabelStr = (ArrayList) container.getDisplayLabels().get(whichMeasure);
        container.getCrossTabGraphColMap().removeAll(graphId);
        container.getCrossTabGraphColMap().put(graphId, (String) container.getDisplayColumns().get(whichMeasure));
        container.getCrossTabGraphColMap().put(graphId, LabelStr.get(LabelStr.size() - 1).toString());
        this.grpbDomainaxislabel = LabelStr.get(LabelStr.size() - 1).toString();
        this.showxAxis = LabelStr.get(LabelStr.size() - 1).toString();
//

        if (newCrossRetObj.finalColViewSortedValues != null && newCrossRetObj.finalColViewSortedValues.length > 0) {
            finalColViewSortedValues = newCrossRetObj.finalColViewSortedValues[1];
        }
        for (int j = 0; j < container.getViewByCount(); j++) {
            chartCols.add((String) container.getDisplayColumns().get(j));
            chartLabels.add(String.valueOf(container.getDisplayLabels().get(j)));
        }
        if (container.isSummarizedMeasuresEnabled() && summmeas != null) {
            for (int i = 0; i < summmeas.size(); i++) {
                chartCols.add("A_" + summmeas.get(i));
                chartLabels.add(summmeasTitle.get(i).toString());
            }
        }
        int temp = initValue;
        if (elemArray != null && !elemArray.isEmpty() && finalColViewSortedValues != null && finalColViewSortedValues.size() > 0) {
            for (int i = 0; i < finalColViewSortedValues.size() + summmeas.size() && i < finalColViewSortedValues.size(); i++) {
                if (elemArray != null && !elemArray.isEmpty() && elemArray.contains(finalColViewSortedValues.get(i))) {
                    ArrayList tempStr = (ArrayList) container.getDisplayLabels().get(i + initValue);
                    if (!tempStr.contains("Grand Total") && !tempStr.contains("Sub Total")) {
                        if (!RTMeasureElement.isRunTimeMeasure((String) container.getDisplayColumns().get(i + initValue))) {
                            chartCols.add((String) container.getDisplayColumns().get(i + initValue));
                            chartLabels.add(container.getDisplayLabels().get(i + initValue).toString().substring(1, container.getDisplayLabels().get(i + initValue).toString().lastIndexOf(",")));
                        }
                    }
                }

            }
        } else {
            ArrayList displayColumns = container.getDisplayColumns();
            int size = displayColumns.size();
            int measCount = container.getReportMeasureCount();
            for (int m = whichMeasure; m < size;) {
                ArrayList tempStr = (ArrayList) container.getDisplayLabels().get(m);
                if (!tempStr.contains("Grand Total") && !tempStr.contains("Sub Total")) {
                    if (!RTMeasureElement.isRunTimeMeasure((String) container.getDisplayColumns().get(m))) {
                        chartCols.add((String) container.getDisplayColumns().get(m));
                        //chartLabels.add(String.valueOf(container.getDisplayLabels().get(i)));
                        //chartLabels.add(tempStr.get(0).toString());
                        chartLabels.add(container.getDisplayLabels().get(m).toString().substring(1, container.getDisplayLabels().get(m).toString().lastIndexOf(",")));
                    }
                }
                m += measCount;

            }
        }
        barChartColumnNames = new String[chartCols.size()];
        barChartColumnTitles = new String[chartLabels.size()];
        for (int n = 0; n < barChartColumnNames.length; n++) {
            barChartColumnNames[n] = String.valueOf(chartCols.get(n));
            barChartColumnTitles[n] = String.valueOf(chartLabels.get(n));
        }
        for (int h = 0; h < viewbysTemp.length; h++) {
            viewbysTemp[h] = String.valueOf(container.getDisplayColumns().get(h));
            barChartColumnNames[h] = String.valueOf(container.getDisplayColumns().get(h));
        }
        this.setBarChartColumnNames(barChartColumnNames);
        this.setBarChartColumnTitles(barChartColumnTitles);
        this.setPieChartColumns(barChartColumnNames);
        this.viewByElementIds = viewbysTemp;



    }

    public boolean isIsCrosstab() {
        return isCrosstab;
    }

    public void setIsCrosstab(boolean isCrosstab) {
        this.isCrosstab = isCrosstab;
    }

    public ArrayList<String> getSortColumns() {
        return sortColumns;
    }

    public void setSortColumns(ArrayList<String> sortColumns) {
        this.sortColumns = sortColumns;
    }

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getStackedType() {
        return stackedType;
    }

    public void setStackedType(String stackedType) {
        this.stackedType = stackedType;
    }

    public String getGraphDisplayRowsForDrill() {
        return dispRowsAfterDrill;
    }

    public void setGraphdisplayRowsForDrill(String dispRows) {
        this.dispRowsAfterDrill = dispRows;
    }
}