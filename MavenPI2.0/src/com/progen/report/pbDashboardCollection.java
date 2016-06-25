/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import com.google.common.collect.ArrayListMultimap;
import com.progen.charts.GraphProperty;
import com.progen.dashboardView.db.DashboardViewerDAO;
import com.progen.graph.info.ProgenGraphInfo;
import com.progen.report.entities.GraphReport;
import com.progen.report.scorecard.bd.ScorecardViewerBD;
import java.sql.Connection;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class pbDashboardCollection extends PbReportCollection {

    public static Logger logger = Logger.getLogger(pbDashboardCollection.class);
    ResourceBundle resBndle = null;

    private ResourceBundle getResourceBundle() {
        if (resBndle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resBndle = new PbReportCollectionResBunSqlServer();
            } else {
                resBndle = new PbReportCollectionResourceBundle();
            }
        }
        return resBndle;
    }
    int colorCode = 0;
    int colorCode1 = 0;
    public String drillUrl = "";
    public HashMap kpiQuery = new LinkedHashMap();
    public HashMap kpiMasterQuery = new LinkedHashMap();
    public HashMap kpiMasterData = new LinkedHashMap();
    public HashMap kpiGraphData = new LinkedHashMap();
    public HashMap kpiGraphTitleData = new LinkedHashMap();
    public HashMap GraphReport = new LinkedHashMap();
    public List<DashletDetail> dashletDetails = new ArrayList<DashletDetail>();
    public List<String> qryColIds = new ArrayList<String>();
    public List<String> qryColAggr = new ArrayList<String>();
    public ArrayListMultimap<String, KPIElement> kpiElementList = ArrayListMultimap.create();
    public ArrayListMultimap<String, String> scoreCardDetails = ArrayListMultimap.create();
    private HashMap reportReqParams = null;
    private HttpServletRequest servletRequest = null;
    private HttpSession session = null;
    private HttpServletResponse servletResponse = null;
    private boolean isFxCharts = false;
    public Document document = null;
    public Element root = null;
    public HashMap kpiCommentHashMap = new HashMap();
    //public String userId = null;
    public String elementId = null;
    private String diffDays = null;
    private HashMap DBKPIHashMap = null;
    private String MyGraphType;
    private String MyGraphId;
    private HashMap graphIdDetails = new HashMap();
    private String OriginalgraphId;
    private String OriginalgraphName;
    private String OriginalgraphClassName;
    private String OriginalraphTypeName;
    public LinkedHashMap saveHashMapDetails = new LinkedHashMap();
    private String checkDashboardType = "N";
    public boolean initialized;
    private String textKPIRowViewBy;
    private String oneViewWidth;
    private String oneViewHeight;
    private boolean oneviewCheckForKpis;
    private boolean oneviewForTest;
    private ArrayList<String> initialReportVIewBys = new ArrayList<String>();
    private String dashletId;
    private boolean fromOneView;

    public void getParamMetaData(String action) throws Exception {

        if (action != null && "paramChange".equalsIgnoreCase(action)) {
            super.updateCollection(true);
        } else {
            super.getParamMetaData(true);
        }
        HashMap map = new HashMap();
        HashMap repHashMap = new HashMap();
        HashMap tabHashMap = new HashMap();

        Container container = null;
        HttpSession hs = null;
        if (getServletRequest() != null) {
            hs = getServletRequest().getSession();
        }

        if (hs != null && hs.getAttribute("PROGENTABLES") != null) {

            map = (HashMap) hs.getAttribute("PROGENTABLES");
            if (map.get(this.reportId) != null) {
                container = (Container) map.get(this.reportId);
            } else {
                container = new Container();
            }
        } else {
            container = new Container();
        }

        repHashMap.put("reportParameters", reportParameters);
        repHashMap.put("Parameters", reportParamIds);
        repHashMap.put("ParametersNames", reportParamNames);
        repHashMap.put("reportParametersValues", reportParametersValues);
        container.setReportParameterHashMap(repHashMap);


        this.resetPath = ctxPath + "/dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + this.reportId + "&pagename=" + reportIncomingParameters.get("pagename");
        this.completeUrl = this.completeUrl.replace("reportViewer", "dashboardViewer");
        this.completeUrl = this.completeUrl.replace("viewReport", "viewDashboard");
        this.completeUrl = this.completeUrl.replace(";", "&");
//        this.completeUrl = ctxPath + "/dashboardViewer.do?reportBy=viewDashboard;REPORTID=" + this.reportId + this.completeUrl;
        HashMap hm = new HashMap();
        hm.put("TimeDimHashMap", timeDetailsMap);
        hm.put("TimeDetailstList", timeDetailsArray);
        repHashMap.put("TimeDimHashMap", timeDetailsMap);
        repHashMap.put("TimeDetailstList", timeDetailsArray);
        container.setTimeParameterHashMap(hm);
        container.setParametersHashMap(repHashMap);
        container.setReportParameterHashMap(repHashMap);
        container.setTableHashMap(tabHashMap);
        map.put(this.reportId, container);
        if (hs != null && hs.getAttribute("PROGENTABLES") != null) {
            hs.setAttribute("PROGENTABLES", map);
//        hs.setAttribute("viewById",viewByID);
            hs.setAttribute("reportIncomingParameters", reportIncomingParameters);
        }
//        this.drillUrl = "&CBOVIEW_BY<VIEWBY_ID>="+reportRowViewbyValues.get(0)+"&CBOARP"+reportRowViewbyValues.get(0)+"=";
        this.drillUrl = "&CBOARP" + reportRowViewbyValues.get(0) + "=";
    }///end of public void getParamMetaData()


    /*
     * public ArrayList getDashKpiGraphinfo(String dashboardId,
     * HttpServletRequest request, HttpServletResponse response) throws
     * Exception { String finalQuery = ""; PbReturnObject retObj = null;
     * PbReportQuery repQuery = new PbReportQuery(); //PbReturnObject pbretObj =
     * new PbReturnObject(); PbDb db = new PbDb();
     *
     * PbReturnObject pbretObj = null; String[] dbColNames = null; String[]
     * qryColNames = null; String[] grpColNames = null; String[] graphsStrArray
     * = null; ArrayList grpsArray = new ArrayList();
     *
     * ArrayList grpsTitlesArray = new ArrayList(); ArrayList grpsImagesArray =
     * new ArrayList();
     *
     *
     * PrintWriter out = response.getWriter();
     *
     * String MasterId = ""; //code for create kpigaph viewer
     *
     *
     * //ends here String sqlstr = ""; sqlstr += "SELECT KPI_GRP_ID, ELEMENT_ID,
     * DASHBOARD_ID,"; sqlstr += " NEEDLE, KPINAME, KPIGRAPHTYPE FROM
     * PRG_AR_KPI_GRAPH_DETAILS";
     *
     * sqlstr += " where DASHBOARD_ID=" + dashboardId + " and
     * display_type='Graph' order by dash.DISPLAY_SEQUENCE ";
     *
     *
     * finalQuery = sqlstr; retObj = db.execSelectSQL(finalQuery);
     *
     * dbColNames = retObj.getColumnNames(); //MasterId =
     * (retObj.getFieldValueString(0, dbColNames[5])); PbDashboardGraphDisplay
     * pdgd = new PbDashboardGraphDisplay(); //pdgd.reportRowViewbyValues =
     * reportRowViewbyValues; String graphStr = "";
     *
     * for (int i = 0; i < retObj.getRowCount(); i++) {
     *
     * PbReturnObject retObjQry = null; PbReturnObject retObjGrp = null;
     *
     * ArrayList kpiDetails = new ArrayList(); ArrayList grpDetails = new
     * ArrayList(); ArrayList qryColIds = new ArrayList(); ArrayList qryAggrs =
     * new ArrayList();
     *
     * kpiDetails.add(retObj.getFieldValueString(i, dbColNames[0]));
     * kpiDetails.add(retObj.getFieldValueString(i, dbColNames[1]));
     * kpiDetails.add(retObj.getFieldValueString(i, dbColNames[2]));
     *
     * GraphReport.put(kpiDetails.get(0).toString(), kpiDetails);
     *
     *
     *
     * String sqlstr1 = ""; sqlstr1 = "select element_id,
     * col_disp_name,aggregation_type from prg_ar_query_detail where report_id="
     * + String.valueOf(kpiDetails.get(1)) + " order by col_seq"; finalQuery =
     * sqlstr1;
     *
     * retObjQry = db.execSelectSQL(finalQuery);
     *
     * qryColIds = new ArrayList(); qryAggrs = new ArrayList(); if (retObjQry !=
     * null) { qryColNames = retObjQry.getColumnNames(); for (int j = 0; j <
     * retObjQry.getRowCount(); j++) {
     * qryColIds.add(retObjQry.getFieldValueString(j, qryColNames[0]));
     * qryAggrs.add(retObjQry.getFieldValueString(j, qryColNames[2])); }
     *
     * String sqlstr2 = ""; sqlstr2 = "select element_id, col_name from
     * prg_ar_graph_details where graph_id= " + (String) kpiDetails.get(0);
     * finalQuery = sqlstr2;
     *
     *
     * retObjGrp = db.execSelectSQL(finalQuery); grpColNames =
     * retObjGrp.getColumnNames(); ArrayList grpElementIds = new ArrayList();
     *
     * for (int j = 0; j < reportRowViewbyValues.size(); j++) { if
     * (!(grpElementIds.contains(String.valueOf(reportRowViewbyValues.get(j)))))
     * { grpElementIds.add("A_" + String.valueOf(reportRowViewbyValues.get(j)));
     * } }
     *
     *
     * for (int k = 0; k < retObjGrp.getRowCount(); k++) {
     * grpElementIds.add("A_" + retObjGrp.getFieldValueInt(k, grpColNames[0]));
     * }
     *
     * repQuery.setRowViewbyCols(reportRowViewbyValues);
     * repQuery.setColViewbyCols(reportColViewbyValues);
     * repQuery.setQryColumns(qryColIds); repQuery.setColAggration(qryAggrs);
     * repQuery.setTimeDetails(timeDetailsArray);
     *
     * if (qryColIds.get(0) == null) { } pbretObj =
     * repQuery.getPbReturnObject(String.valueOf(qryColIds.get(0)));
     *
     *
     * if (pbretObj != null) { Container container = new Container();
     *
     *
     *
     * container.setDisplayedSet(pbretObj);
     * container.setOriginalColumns(grpElementIds); ArrayList alist =
     * container.getDisplayedSet();
     *
     * String[] newStr = new String[reportRowViewbyValues.size()]; for (int k =
     * 0; k < reportRowViewbyValues.size(); k++) { if
     * (!String.valueOf(reportRowViewbyValues.get(k)).equalsIgnoreCase("TIME"))
     * { newStr[k] = "A_" + String.valueOf(reportRowViewbyValues.get(k)); }
     *
     * }
     * pdgd.setViewByElementIds(newStr);
     *
     * pdgd.setCtxPath(request.getContextPath());
     * pdgd.setCurrentDispRecords(alist);
     *
     * pdgd.setJscal("reportViewer.do?reportBy=viewReport&REPORTID=" + (String)
     * kpiDetails.get(1) + this.drillUrl);
     *
     *
     * pdgd.setSession(request.getSession(false)); pdgd.setResponse(response);
     * pdgd.setOut(out); pdgd.setReportId((String) kpiDetails.get(1));
     * pdgd.setCollect(this);
     *
     * grpDetails = pdgd.getDashboardGraphHeadersNew((String)
     * kpiDetails.get(0));
     *
     * String[] paths =
     * grpDetails.get(0).toString().split(";");//grpDetails[0].split(";");
     *
     * String[] grpTitles = grpDetails.get(1).toString().split(";");
     *
     * ProgenChartDisplay[] pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
     *
     * String[] pathsZoom =
     * grpDetails.get(3).toString().split(";");//grpDetails[0].split(";");
     * ProgenChartDisplay[] pchartsZoom = (ProgenChartDisplay[])
     * grpDetails.get(4);
     *
     * for (int grpCnt = 0; grpCnt < pcharts.length; grpCnt++) {
     *
     * grpsImagesArray.add(pcharts[grpCnt].chartDisplay);
     * grpsTitlesArray.add(grpTitles[grpCnt]); } } } retObjQry = null; retObjGrp
     * = null;
     *
     * grpsArray.add(grpsImagesArray); grpsArray.add(grpsTitlesArray);
     *
     * }
     *
     * return grpsArray; }
     *
     * public String getDashKpiGraphValue(String dashboardId, HttpServletRequest
     * request, HttpServletResponse response) throws Exception {
     *
     * PbDb db = new PbDb(); PbReportQuery repQuery = new PbReportQuery();
     *
     *
     * String kpiQuery = "SELECT KPI_GRP_ID, ELEMENT_ID, DASHBOARD_ID,";
     * kpiQuery += "NEEDLE, KPINAME, KPIGRAPHTYPE FROM PRG_AR_KPI_GRAPH_DETAILS
     * where DASHBOARD_ID=" + dashboardId;
     *
     * PbReturnObject kpipbro = db.execSelectSQL(kpiQuery); String[]
     * kpidbColNames = kpipbro.getColumnNames();
     *
     * String query = "SELECT nvl(AGGREGATION_TYPE,'SUM') AGGREGATION_TYPE FROM
     * PRG_USER_ALL_INFO_DETAILS where element_id=" +
     * kpipbro.getFieldValueInt(0, kpidbColNames[1]);
     *
     * PbReturnObject kpiretObj = db.execSelectSQL(query);
     *
     * ArrayList kpielearr = new ArrayList();
     * kpielearr.add(kpipbro.getFieldValueInt(0, kpidbColNames[1])); ArrayList
     * aggr = new ArrayList(); aggr.add(kpiretObj.getFieldValueString(0, 0));
     *
     * repQuery.setRowViewbyCols(reportRowViewbyValues);
     *
     * repQuery.setColViewbyCols(reportColViewbyValues);
     * repQuery.setQryColumns(kpielearr); repQuery.setColAggration(aggr);
     * repQuery.setTimeDetails(timeDetailsArray);
     *
     * repQuery.setDefaultMeasure(String.valueOf(kpipbro.getFieldValueInt(0,
     * kpidbColNames[1])));
     * repQuery.setDefaultMeasureSumm(String.valueOf(kpiretObj.getFieldValueString(0,
     * 0))); repQuery.isKpi = true; PbReturnObject kpipbretObj =
     * repQuery.getPbReturnObject(String.valueOf(kpipbro.getFieldValueInt(0,
     * kpidbColNames[1])));
     *
     * String kpiVal = String.valueOf(kpipbretObj.getFieldValueInt(0, 1));
     * return kpiVal;
    }
     */
    public String processScoreCard(String scoreCardMasterId, String dashletId, PbReturnObject retObj, PbReturnObject priorRetObj, String userId) throws Exception {
        StringBuilder output = new StringBuilder();

        DashletDetail detail = getDashletDetail(dashletId);
        output.append("<div id=\"innerDivKpiId\"  class=\"portlet-header1 navtitle portletHeader ui-corner-all\">");
        output.append("<table width=\"100%\"><tr>");
        output.append("<td width=\"74%\"><strong>" + detail.getDashletName() + "</strong></td>");
        output.append("<td width=\"10%\" align=\"right\"><a class=\"ui-icon ui-icon-trash\" href=\"javascript:void(0)\"  onclick=\"closeOldPortlet('Dashlets-" + dashletId + "'," + dashletId + ")\" ></a></td>");
        output.append("</tr></table>");
        output.append("</div>");

        output.append(displayScoreCard(scoreCardMasterId, retObj, priorRetObj));
        return output.toString();
    }

    public String displayScoreCard(String scoreCardMasterId, PbReturnObject retObj, PbReturnObject priorRetObj) {
        StringBuilder output = new StringBuilder();
        DashletDetail detail = new DashletDetail();
        int rowSpan = detail.getRowSpan();
        int colSpan = detail.getColSpan();
        int width = 500;
        int height = 330;
        width = width * colSpan;
        height = height * rowSpan;
        output.append("<div id=" + scoreCardMasterId + " style=\"width:99%;height:" + height + "px;overflow:auto\"  align=\"left\">");
        DashboardViewerDAO dao = new DashboardViewerDAO();
        List<String> scardIds = this.scoreCardDetails.get(scoreCardMasterId);//dao.getScoreCardIds(scoreCardMasterId);

        for (String id : scardIds) {
            ScorecardViewerBD viewerBD = new ScorecardViewerBD();
            //String scardOutput = viewerBD.loadScorecard(id, userId);
            //output.append(scardOutput);
            output.append("<br>");
        }

        output.append("</div>");
        return output.toString();
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

    public boolean isIsFxCharts() {
        return isFxCharts;
    }

    public void setIsFxCharts(boolean isFxCharts) {
        this.isFxCharts = isFxCharts;
    }

    public void addDashletDetail(DashletDetail detail) {
        this.dashletDetails.add(detail);
    }

    public List<DashletDetail> getDashletDetails() {
        return this.dashletDetails;
    }

    public DashletDetail getDashletDetail(String dashletId) {
        for (DashletDetail detail : dashletDetails) {
            if (detail.getDashBoardDetailId().equalsIgnoreCase(dashletId)) {
                return detail;
            }
        }
        return null;
    }

    public DashletDetail getDashletFromKPIMaster(String kpiMasterId) {
        for (DashletDetail detail : dashletDetails) {
            if (kpiMasterId.equalsIgnoreCase(detail.getKpiMasterId())) {
                return detail;
            }
        }
        return null;
    }

    public DashletDetail getDashletFromGraphId(String graphId) {
        if (graphId != null) {
            for (DashletDetail detail : dashletDetails) {
                if (graphId.equalsIgnoreCase(detail.getGraphId())) {
                    return detail;
                }
            }
        }
        return null;
    }

    public void removeDashletDetail(String dashletId) {
        for (int i = 0; i < dashletDetails.size(); i++) {
            DashletDetail detail = dashletDetails.get(i);
            if (detail.getDashBoardDetailId().equalsIgnoreCase(dashletId)) {
                dashletDetails.remove(i);
                break;
            }
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public List<String> getQryColAggr() {
        return qryColAggr;
    }

    public void setQryColAggr(List<String> qryColAggr) {
        this.qryColAggr = qryColAggr;
    }

    public List<String> getQryColIds() {
        return qryColIds;
    }

    public void setQryColIds(List<String> qryColIds) {
        this.qryColIds = qryColIds;
    }

    public void addQryColumns(ArrayList<String> colIds, ArrayList<String> colAggrs) {
        if (colIds != null && colAggrs != null) {
            for (int i = 0; i < colIds.size(); i++) {
                if (!(this.qryColIds.contains(colIds.get(i)))) {
                    this.qryColIds.add(colIds.get(i));
                    this.qryColAggr.add(colAggrs.get(i));
                }
            }
        }
    }

    public HashMap getReportReqParams() {
        return reportReqParams;
    }

    public void setReportReqParams(HashMap reportReqParams) {
        this.reportReqParams = reportReqParams;
    }

//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
    public Connection getConnection(String elementId) {

        Connection connection = null;
        try {
            connection = ProgenConnection.getInstance().getConnectionForElement(elementId);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return connection;
    }

    public String getDiffDays() {
        return diffDays;
    }

    public void setDiffDays(String diffDays) {
        this.diffDays = diffDays;
    }

    public HashMap getDBKPIHashMap() {
        return DBKPIHashMap;
    }

    public void setDBKPIHashMap(HashMap DBKPIHashMap) {
        this.DBKPIHashMap = DBKPIHashMap;
    }

    public HashMap getGraphIdDetails() {
        return graphIdDetails;
    }

    public void setGraphIdDetails(HashMap graphIdDetails) {
        this.graphIdDetails = graphIdDetails;
    }

    public PbReturnObject getPbReturnObject(String sql) {
        PbReturnObject pbretObj3 = null;
        PbDb db = new PbDb();
        try {
            pbretObj3 = db.execSelectSQL(sql);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return pbretObj3;
    }

    /**
     * @return the OriginalgraphId
     */
    public String getOriginalgraphId() {
        return OriginalgraphId;
    }

    /**
     * @param OriginalgraphId the OriginalgraphId to set
     */
    public void setOriginalgraphId(String OriginalgraphId) {
        this.OriginalgraphId = OriginalgraphId;
    }

    /**
     * @return the OriginalgraphName
     */
    public String getOriginalgraphName() {
        return OriginalgraphName;
    }

    /**
     * @param OriginalgraphName the OriginalgraphName to set
     */
    public void setOriginalgraphName(String OriginalgraphName) {
        this.OriginalgraphName = OriginalgraphName;
    }

    /**
     * @return the OriginalgraphClassName
     */
    public String getOriginalgraphClassName() {
        return OriginalgraphClassName;
    }

    /**
     * @param OriginalgraphClassName the OriginalgraphClassName to set
     */
    public void setOriginalgraphClassName(String OriginalgraphClassName) {
        this.OriginalgraphClassName = OriginalgraphClassName;
    }

    /**
     * @return the OriginalraphTypeName
     */
    public String getOriginalraphTypeName() {
        return OriginalraphTypeName;
    }

    /**
     * @param OriginalraphTypeName the OriginalraphTypeName to set
     */
    public void setOriginalraphTypeName(String OriginalraphTypeName) {
        this.OriginalraphTypeName = OriginalraphTypeName;
    }

    public String getMyGraphType() {
        return MyGraphType;
    }

    public void setMyGraphType(String MyGraphType) {
        this.MyGraphType = MyGraphType;
    }

    public String getMyGraphId() {
        return MyGraphId;
    }

    public void setMyGraphId(String MyGraphId) {
        this.MyGraphId = MyGraphId;
    }

    public String getCheckDashboardType() {
        return checkDashboardType;
    }

    public void setCheckDashboardType(String aCheckDashboardType) {
        checkDashboardType = aCheckDashboardType;
    }

    public String getbusinessroles(String dashBoardId) {
        String folderdetailsqquery = " select distinct FOLDER_ID from PRG_AR_REPORT_DETAILS  where REPORT_ID =" + dashBoardId;
        StringBuffer foldernames = new StringBuffer();
        String folderDetails = "";
        PbDb reportDetail = new PbDb();
        PbReturnObject retObj = new PbReturnObject();

        try {
            retObj = reportDetail.execSelectSQL(folderdetailsqquery);
            for (int j = 0; j < retObj.getRowCount(); j++) {
                foldernames = foldernames.append(retObj.getFieldValueString(j, 0));
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

        folderDetails = foldernames.toString();
        return folderDetails;
    }

    public static void main(String args[]) {

        ArrayList masterarray = new ArrayList();

        masterarray.add("1124");
        masterarray.add("1125");
        //masterarray.add("1084");


        ArrayList arlist141 = new ArrayList();
        HashMap elementdetailHashmap = new HashMap();
        //getting data int he form of arraylist
        if (masterarray != null && masterarray.size() != 0) {
            for (int j = 0; j < masterarray.size(); j++) {
                PbDb PbDbobj141 = new PbDb();

                PbReturnObject retObj141 = new PbReturnObject();
                String query = "select ELEMENT_ID from PRG_AR_KPI_DETAILS where KPI_MASTER_ID=" + masterarray.get(j);

                try {

                    retObj141 = PbDbobj141.execSelectSQL(query);
                    for (int k = 0; k < retObj141.getRowCount(); k++) {
                        arlist141.add(retObj141.getFieldValueString(k, 0));

                    }
                } catch (Exception e) {
                    logger.error("Exception: ", e);
                }


                elementdetailHashmap.put(String.valueOf(masterarray.get(j)), arlist141);
                arlist141 = new ArrayList();

            }

        }

        ////////////////.println("elementdetailHashmap=" + elementdetailHashmap);


        HashMap hhm = new HashMap();
        HashMap maincommentshm = new HashMap();
        if (masterarray != null && masterarray.size() != 0) {
            for (int j = 0; j < masterarray.size(); j++) {
                PbDb PbDbobj141 = new PbDb();

                ArrayList arl = (ArrayList) elementdetailHashmap.get(masterarray.get(j));
                ////////////////.println("arl="+arl);
                if (arl != null && arl.size() != 0) {

                    for (int z = 0; z < arl.size(); z++) {
                        PbReturnObject retObj143 = new PbReturnObject();
                        String query = "select distinct  KPI_COMMENT from PRG_KPI_USER_COMMENTS where KPI_MASTER_ID=" + masterarray.get(j) + " and " + "ELEMENT_ID=" + arl.get(z);

                        try {

                            retObj143 = PbDbobj141.execSelectSQL(query);
                            for (int k = 0; k < retObj143.getRowCount(); k++) {
                                //////////////////.println(String.valueOf(retObj143.getFieldValueString(k, 0)));
                                String str = String.valueOf(retObj143.getFieldValueString(k, 0));
                                if (str.equalsIgnoreCase("")) {
                                    hhm.put(arl.get(z), "");
                                } else {
                                    hhm.put(arl.get(z), String.valueOf(retObj143.getFieldValueString(k, 0)));
                                }


                            }//for
                        } catch (Exception e) {
                            logger.error("Exception: ", e);
                        }//catch^
                    }//for


                }//if
                maincommentshm.put(masterarray.get(j), hhm);
                hhm = new HashMap();


            }//for

        }//if

        ////////////////.println("maincommentshm="+maincommentshm);







    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public String getDbrdTableData(String grpType, String dashletId, Container container, String dashBoardId, String refReportId, String graphId, String kpiMasterId, String dispSequence, String dispType, String dashletName) throws Exception {
        StringBuilder DbrdtableBuilder = new StringBuilder("");
        if (grpType.equalsIgnoreCase("Graph")) {
            if (container.getDbrdDispValue() != null) {
                if (container.getDbrdDispValue().get(dashletId) != null) {
                    if (container.getDbrdDispValue().get(dashletId).toString().equalsIgnoreCase("Table")) {
                        DbrdtableBuilder.append("<script type='text/javascript'>");
                        DbrdtableBuilder.append("dbrdViewerTable('" + dashletId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + dispSequence + "','" + dispType + "','" + dashletName + "');");
                        DbrdtableBuilder.append("</script>");
                    }
                } else {
                    DbrdtableBuilder.append("");
                }
            } else {
                DbrdtableBuilder.append("");
            }
        }
        return DbrdtableBuilder.toString();
    }

    public void setGraphType(String dashletId, String graphType) {
        DashletDetail dashlet = getDashletDetail(dashletId);

        if (dashlet != null && graphType != null && !("".equals(graphType))) {
            GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
            Integer graphTypeId = ProgenGraphInfo.getGraphTypeId(graphType);
            String className = ProgenGraphInfo.getGraphClassName(graphTypeId);
            Integer classId = ProgenGraphInfo.getGraphClassId(graphTypeId);
            graphDetails.setGraphTypeName(graphType);
            graphDetails.setGraphType(graphTypeId);
            graphDetails.setGraphClass(classId);
            graphDetails.setGraphClassName(className);
        }
    }

    private HashMap buildGraphMap(String graphId, PbReturnObject pbro, Container container) {
        HashMap graphMap;
        HashMap singleGraphMap;
        GraphProperty graphProperty;
        if (container.getGraphHashMap() != null) {
            graphMap = container.getGraphHashMap();
        } else {
            graphMap = new HashMap();
        }

        if (graphMap.get(graphId) != null) {
            singleGraphMap = (HashMap) graphMap.get(graphId);
        } else {
            singleGraphMap = new HashMap();
        }

        singleGraphMap.put("graphId", pbro.getFieldValueString(0, "GRAPH_ID"));
        singleGraphMap.put("graphName", pbro.getFieldValueString(0, "GRAPH_NAME"));
        singleGraphMap.put("graphClassName", pbro.getFieldValueString(0, "GRAPH_CLASS_NAME"));
        singleGraphMap.put("graphTypeName", pbro.getFieldValueString(0, "GRAPH_TYPE_NAME"));
        singleGraphMap.put("graphSize", pbro.getFieldValueString(0, "GRAPH_SIZE_NAME"));


        singleGraphMap.put("graphLegend", pbro.getFieldValueString(0, "ALLOW_LEGEND"));
        singleGraphMap.put("showGT", pbro.getFieldValueString(0, "SHOW_GT"));
        // singleGraphMap.put("nbrFormat", pbro.getFieldValueString(0, reportName));
        singleGraphMap.put("graphLegendLoc", pbro.getFieldValueString(0, "LEGEND_LOC"));
        // singleGraphMap.put("graphSymbol", pbro.getFieldValueString(0, reportName));
        // singleGraphMap.put("graphSize", pbro.getFieldValueString(0, "GRAPH_SIZE"));
        singleGraphMap.put("graphWidth", pbro.getFieldValueString(0, "GRAPH_WIDTH"));
        singleGraphMap.put("graphHeight", pbro.getFieldValueString(0, "GRAPH_HEIGHT"));
        singleGraphMap.put("graphDisplayRows", pbro.getFieldValueString(0, "GRAPH_DISPLAY_ROWS"));
//        singleGraphMap.put("targetRange", pbro.getFieldValueString(0, reportName));
//        singleGraphMap.put("startValue", pbro.getFieldValueString(0, reportName));
//        singleGraphMap.put("endValue", pbro.getFieldValueString(0, reportName));
//        singleGraphMap.put("SwapColumn", pbro.getFieldValueString(0, reportName));
//        singleGraphMap.put("showMinMaxRange", pbro.getFieldValueString(0, reportName));
        if (singleGraphMap.get("GraphProperty") != null) {
            graphProperty = (GraphProperty) singleGraphMap.get("GraphProperty");
        } else {
            graphProperty = new GraphProperty();
            graphProperty.setLabelsDisplayed(false);
        }
        singleGraphMap.put("GraphProperty", graphProperty);
        graphMap.put(graphId, singleGraphMap);
        return graphMap;
    }

    public String getTextKPIRowViewBy() {
        return textKPIRowViewBy;
    }

    public void setTextKPIRowViewBy(String textKPIRowViewBy) {
        this.textKPIRowViewBy = textKPIRowViewBy;
    }

    public boolean isOneviewCheckForKpis() {
        return oneviewCheckForKpis;
    }

    public void setOneviewCheckForKpis(boolean oneviewCheckForKpis) {
        this.oneviewCheckForKpis = oneviewCheckForKpis;
    }

    public String getOneViewWidth() {
        return oneViewWidth;
    }

    public void setOneViewWidth(String oneViewWidth) {
        this.oneViewWidth = oneViewWidth;
    }

    public boolean isOneviewForTest() {
        return oneviewForTest;
    }

    public void setOneviewForTest(boolean oneviewForTest) {
        this.oneviewForTest = oneviewForTest;
    }

    /**
     * @return the initialReportVIewBys
     */
    public ArrayList<String> getInitialReportVIewBys() {
        return initialReportVIewBys;
    }

    /**
     * @param initialReportVIewBys the initialReportVIewBys to set
     */
    public void setInitialReportVIewBys(ArrayList<String> initialReportVIewBys) {
        this.initialReportVIewBys = initialReportVIewBys;
    }

    /**
     * @return the initialReportVIewBys
     */
    public String getOneViewHeight() {
        return oneViewHeight;
    }

    public void setOneViewHeight(String oneViewHeight) {
        this.oneViewHeight = oneViewHeight;
    }

    /**
     * @return the dashletId
     */
    public String getDashletId() {
        if (fromOneView == true) {
            return dashletId;
        } else {
            return null;
        }
    }

    /**
     * @param dashletId the dashletId to set
     */
    public void setDashletId(String dashletId) {
        if (fromOneView == true) {
            this.dashletId = dashletId;
        } else {
            this.dashletId = null;
        }
    }

    /**
     * @return the fromOneView
     */
    public boolean isFromOneView() {
        return fromOneView;
    }

    /**
     * @param fromOneView the fromOneView to set
     */
    public void setFromOneView(boolean fromOneView) {
        this.fromOneView = fromOneView;
    }
}