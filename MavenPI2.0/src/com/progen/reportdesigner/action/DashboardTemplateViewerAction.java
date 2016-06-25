package com.progen.reportdesigner.action;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.charts.*;
import com.progen.dashboard.DashboardConstants;
import com.progen.dashboard.DashboardTableColorGroupHelper;
import com.progen.dashboardView.bd.PbDashboardViewerBD;
import com.progen.dashboardView.db.DashboardViewerDAO;
import com.progen.db.ProgenDataSet;
import com.progen.graph.GraphBuilder;
import com.progen.oneView.bd.OneViewBD;
import com.progen.query.RunTimeMeasure;
import com.progen.report.DashletDetail;
import com.progen.report.KPIElement;
import com.progen.report.PbReportCollection;
import com.progen.report.bd.PbReportTableBD;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.entities.*;
import com.progen.report.kpi.DashletPropertiesHelper;
import com.progen.report.kpi.KPIBuilder;
import com.progen.report.kpi.KPISingleGroupHelper;
import com.progen.report.pbDashboardCollection;
import com.progen.report.query.PbReportQuery;
import com.progen.report.query.PbTimeRanges;
import com.progen.report.util.stat.StatUtil;
import com.progen.reportdesigner.bd.DashboardTemplateBD;
import com.progen.reportdesigner.db.DashboardTemplateDAO;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.db.CreateKPIFromReport;
import com.progen.scheduler.ReportSchedule;
import com.progen.scheduler.ReportSchedulePreferences;
import com.progen.scheduler.SchedulerBD;
import com.progen.scheduler.UserDimensionMap;
import com.progen.scheduler.db.KPIScheduleHelper;
import com.progen.scheduler.db.SchedulerDAO;
import com.progen.userlayer.action.GenerateDragAndDrophtml;
import com.progen.userlayer.db.UserLayerDAO;
import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import oracle.jdbc.OraclePreparedStatement;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import prg.db.*;
import prg.reportscheduler.ReportSchedulerJob;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

/**
 *
 * @author mahesh.sanampudi@progenbusiness.com Altered by Kalyan....
 *
 */
public class DashboardTemplateViewerAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(DashboardTemplateViewerAction.class);
    private DashletPropertiesHelper dashletPropertiesHelper;

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("getKpis", "getKpis");
        map.put("getMoreKpis", "getMoreKpis");
        map.put("getGraphReports", "getGraphReports");
        map.put("saveDashboard", "saveDashboard");
        map.put("OverrideDashboard", "OverrideDashboard");
        map.put("getGraphByGraphId", "getGraphByGraphId");
        map.put("getKpisGraphs", "getKpisGraphs");
        map.put("getKpiGrpValue", "getKpiGrpValue");
        map.put("getKpiNeedleValue", "getKpiNeedleValue");
        map.put("buildDbrdGraphs", "buildDbrdGraphs");
        map.put("getKpiGraphTargets", "getKpiGraphTargets");
        map.put("getDeviation", "getDeviation");
        map.put("saveDbrdComments", "saveDbrdComments");
        map.put("delDbrdComments", "delDbrdComments");
        map.put("saveTargetForKpiTable", "saveTargetForKpiTable");
        map.put("ZoomTargetGraph", "ZoomTargetGraph");
        map.put("deleteDbrdGraphs", "deleteDbrdGraphs");
        map.put("getScoreCards", "getScoreCards");
        map.put("addScoreCard", "addScoreCard");
        map.put("addMap", "addMap");
        map.put("saveKPIDrill", "saveKPIDrill");
        map.put("updateGraphProperties", "updateGraphProperties");
        map.put("setInsightAndCommentViewStatus", "setInsightAndCommentViewStatus");
        map.put("getKPITargetDetails", "getKPITargetDetails");
        map.put("createRegionDashlet", "createRegionDashlet");
        map.put("mergeColumns", "mergeColumns");
        map.put("mergeRows", "mergeRows");
        map.put("delDashlet", "delDashlet");
        map.put("clearDashlet", "clearDashlet");
        map.put("getKpiType", "getKpiType");
        map.put("saveKpiTypes", "saveKpiTypes");
        map.put("getKpiNewName", "getKpiNewName");
        map.put("getUpdatedName", "getUpdatedName");
        map.put("saveDashletName", "saveDashletName");
        map.put("saveKpiSymbol", "saveKpiSymbol");
        map.put("saveTableName", "saveTableName");
        map.put("resetTableData", "resetTableData");
        map.put("runScheduler", "runScheduler");
        map.put("saveScheduler", "saveScheduler");
        map.put("groupKpis", "groupKpis");
        map.put("getAllKpiDetails", "getAllKpiDetails");
        map.put("buildKpiGrouping", "buildKpiGrouping");
        map.put("tableTranspose", "tableTranspose");
        map.put("saveDbrColors", "saveDbrColors");
        map.put("GetReportNames", "GetReportNames");
        map.put("getKpiAttributes", "getKpiAttributes");
        map.put("goButtonForSort", "goButtonForSort");
        map.put("updateGraphName", "updateGraphName");
        map.put("getRelatedGraphs", "getRelatedGraphs");
        map.put("updateKpiHeads", "updateKpiHeads");
        map.put("isAuthorizedUser", "isAuthorizedUser");
        map.put("editKPIName", "editKPIName");
        map.put("getAggregationType", "getAggregationType");
        map.put("getKpiGraphType", "getKpiGraphType");
        map.put("getDbrdTimeDisplay", "getDbrdTimeDisplay");
        map.put("hideTdDash", "hideTdDash");
        map.put("getLeftTdStatus", "getLeftTdStatus");
        map.put("clearTargetForKpiTable", "clearTargetForKpiTable");
        map.put("saveDbrdUrl", "saveDbrdUrl");
        map.put("getViewbysfromReport", "getViewbysfromReport");
        map.put("getViewbyURL", "getViewbyURL");
        map.put("setMtdQtdYtdStatus", "setMtdQtdYtdStatus");
        map.put("saveTextKPIDrill", "saveTextKPIDrill");
        map.put("saveTextKpiComments", "saveTextKpiComments");
        map.put("refreshTextKpiCommentsPage", "refreshTextKpiCommentsPage");
        map.put("getAllViewBysAfterDrill", "getAllViewBysAfterDrill");
        map.put("getSelectedDrillViewBys", "getSelectedDrillViewBys");
        map.put("getMoreKpisForTarget", "getMoreKpisForTarget");
        map.put("saveTargetMapping", "saveTargetMapping");
        map.put("measureDisplyinIcal", "measureDisplyinIcal");
        map.put("getMeasuresForIcal", "getMeasuresForIcal");
        map.put("deleteMeasures", "deleteMeasures");
        map.put("deleteIcal", "deleteIcal");
        map.put("getAllKpis", "getAllKpis");
        map.put("makeSessionVarNull", "makeSessionVarNull");
        map.put("setSessionVal", "setSessionVal");
        map.put("getSessionVal", "getSessionVal");
        map.put("getMeasuresForFormating", "getMeasuresForFormating");
        map.put("applyFormating", "applyFormating");
        map.put("getMonthlyDataForIcal", "getMonthlyDataForIcal");
        map.put("getRollingDataForIcal", "getRollingDataForIcal");
        map.put("saveValsForInitialOpen", "saveValsForInitialOpen");
        map.put("bulidJqMeterChart", "bulidJqMeterChart");
        map.put("getMeasureGraphtest", "getMeasureGraphtest");
        map.put("getDataOnComparisonClick", "getDataOnComparisonClick");
        map.put("getDataOnRankOrValClick", "getDataOnRankOrValClick");
        map.put("dashboardKpiAlerts", "dashboardKpiAlerts");
        map.put("saveTimeDetails", "saveTimeDetails");
        map.put("saveMeasure", "saveMeasure");
        map.put("removeOneviewMeasComments", "removeOneviewMeasComments");
        map.put("GetReportNamesforGraph", "GetReportNamesforGraph");
        map.put("localSaveInDbRegions", "localSaveInDbRegions");
        map.put("getKPIUpdateQueries", "getKPIUpdateQueries");
        return map;
    }
    private final static String SUCCESS = "success";

    public ActionForward getKpis(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String foldersIds = "";
        foldersIds = request.getParameter("foldersIds");
        String dbrdId = request.getParameter("dashboardId");
        String kpiType = request.getParameter("kpiType");
        DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
        String result = dashboardTemplateDAO.getKpis(foldersIds);
        request.setAttribute("Kpis", result);
        request.setAttribute("dbrdId", dbrdId);
        request.setAttribute("kpiType", kpiType);
        return mapping.findForward("Viewerkpis");
    }

    public ActionForward getMoreKpis(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String foldersIds = "";
        foldersIds = request.getParameter("foldersIds");
        String dbrdId = request.getParameter("dashboardId");
        String kpiType = request.getParameter("kpiType");
        String dashletId = request.getParameter("dashletId");
        String kpiMasterId = request.getParameter("kpiMasterId");
        Container container = new Container();
        String tablistFlag = (String) request.getParameter("tableList");
        if (!dashletId.equalsIgnoreCase("0") || !kpiMasterId.equalsIgnoreCase("0")) {
            container = Container.getContainerFromSession(request, dbrdId);
            pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
            List<DashletDetail> dashletList = collect.dashletDetails;
            DashletDetail dashlet = Iterables.find(dashletList, DashletDetail.getDashletDetailPredicate(kpiMasterId));
            dashlet.setEditFlag(true);
        }
        DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
        ReportTemplateDAO repdao = new ReportTemplateDAO();
        String result = "";
//        String result = dashboardTemplateDAO.getKpis(foldersIds);
        ArrayList Parameters = new ArrayList();
        if (container.getTableList() != null && !container.getTableList().isEmpty()) {
            ArrayList alist = new ArrayList();
            result = repdao.getMeasuresForReport(foldersIds, Parameters, request.getContextPath(), container.getTableList());
            container.setTableList(alist);
        } else if (tablistFlag != null && tablistFlag.equalsIgnoreCase("true")) {
            result = repdao.getMeasures(foldersIds, Parameters, request.getContextPath());
        }

        request.setAttribute("Kpis", result);
        request.setAttribute("dbrdId", dbrdId);
        request.setAttribute("kpiType", kpiType);
        request.setAttribute("kpiMasterId", kpiMasterId);
        request.setAttribute("divId", dashletId);

        return mapping.findForward("kpis");
    }

    public ActionForward getKpisGraphs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String customDbrdId = "";
        String foldersIds = "";
        if (session != null) {
            foldersIds = request.getParameter("foldersIds");
            customDbrdId = request.getParameter("dashboardId");
            if (foldersIds == null || "".equals(foldersIds)) {
                Container container = Container.getContainerFromSession(request, customDbrdId);
                PbReportCollection collect = container.getReportCollect();
                String[] ids = collect.reportBizRoles;
                if (ids != null && ids.length > 0) {
                    foldersIds = ids[0];
                }
            }
            DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
            String result = dashboardTemplateDAO.getKpis(foldersIds);
            request.setAttribute("KpisGraphs", result);
            request.setAttribute("DashboardId", customDbrdId);
            return mapping.findForward("ViewerKpisGraphs");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public PbReturnObject getKpiGrpValue(String kpiIds, HttpSession hs, String dbrdId) {
        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject pbretObj = new PbReturnObject();
        PbDb pbdb = new PbDb();

        HashMap map = new HashMap();
        Container container = null;
        String customDbrdId = "";
        String divId = "";
        if (hs != null) {
            try {
                String query = "";
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    query = "SELECT ISNULL(AGGREGATION_TYPE,'SUM') AGGREGATION_TYPE FROM PRG_USER_ALL_INFO_DETAILS where element_id=" + kpiIds;
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    query = "SELECT IFNULL(AGGREGATION_TYPE,'SUM') AGGREGATION_TYPE FROM PRG_USER_ALL_INFO_DETAILS where element_id=" + kpiIds;
                } else {
                    query = "SELECT nvl(AGGREGATION_TYPE,'SUM') AGGREGATION_TYPE FROM PRG_USER_ALL_INFO_DETAILS where element_id=" + kpiIds;
                }


                PbReturnObject retObj = pbdb.execSelectSQL(query);

                map = (HashMap) hs.getAttribute("PROGENTABLES");
                customDbrdId = dbrdId;

                // container = (Container) KPIAttributesMap.get(dbrdId);

                // HashMap ParametersMap = container.getParametersHashMap();//(HashMap) hs.getAttribute("ParametersHashMap");
                //ArrayList params = (ArrayList) ParametersMap.get("Parameters");
                //ArrayList timeDetails = (ArrayList) ParametersMap.get("TimeDetailstList");
                ArrayList params = (ArrayList) hs.getAttribute("MyParameters");
                if (params == null) {
                    params = (ArrayList) hs.getAttribute("params");
                }
                ArrayList timeDetails = (ArrayList) hs.getAttribute("MyTimeDetailstList");
                if (timeDetails == null) {
                    timeDetails = (ArrayList) hs.getAttribute("timeDetails");
                }



                //////////////////////////.println("----------getKpiGrpValue---------");
                //////////////////////////.println("params=" + params);
                //////////////////////////.println("timeDetails=" + timeDetails);
                ArrayList reportRowViewbyValues = new ArrayList();
//                reportRowViewbyValues.add(params.get(0));
                ArrayList reportColViewbyValues = new ArrayList();
                ArrayList kpielearr = new ArrayList();
                kpielearr.add(kpiIds);
                ArrayList aggr = new ArrayList();
                aggr.add(retObj.getFieldValueString(0, 0));
                repQuery.setRowViewbyCols(reportRowViewbyValues);
                repQuery.setColViewbyCols(reportColViewbyValues);
                repQuery.setQryColumns(kpielearr);
                repQuery.setColAggration(aggr);
                repQuery.setTimeDetails(timeDetails);

                repQuery.setDefaultMeasure(kpiIds);
                repQuery.setUserId((String) hs.getAttribute("USERID"));
                repQuery.setDefaultMeasureSumm(String.valueOf(retObj.getFieldValueString(0, 0)));
                repQuery.isKpi = true;
                pbretObj = repQuery.getPbReturnObject(kpiIds);



            } catch (Exception e) {
                logger.error("Exception:", e);
            }

        }
        return pbretObj;
    }

    public String getKpiAggtypeValue(String kpiIds, HttpSession hs, String dbrdId) {
        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject pbretObj = new PbReturnObject();
        PbDb pbdb = new PbDb();

        HashMap map = new HashMap();
        Container container = null;
        String aggType = "";
        String divId = "";
        map = (HashMap) hs.getAttribute("PROGENTABLES");
        if (map.get(dbrdId) != null) {
            container = (prg.db.Container) map.get(dbrdId);
            ////.println("after dbrdId="+dbrdId);

        }
        HashMap ParametersMap = container.getParametersHashMap();

        ArrayList timeArray = (ArrayList) ParametersMap.get("TimeDetailstList");
        hs.setAttribute("MyTimeDetailstList", timeArray);
        if (hs != null) {
            try {
                String query = "";
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    query = "SELECT ISNULL(AGGREGATION_TYPE,'SUM') AGGREGATION_TYPE FROM PRG_USER_ALL_INFO_DETAILS where element_id=" + kpiIds;
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    query = "SELECT IFNULL(AGGREGATION_TYPE,'SUM') AGGREGATION_TYPE FROM PRG_USER_ALL_INFO_DETAILS where element_id=" + kpiIds;
                } else {
                    query = "SELECT nvl(AGGREGATION_TYPE,'SUM') AGGREGATION_TYPE FROM PRG_USER_ALL_INFO_DETAILS where element_id=" + kpiIds;
                }


                PbReturnObject retObj = pbdb.execSelectSQL(query);

                aggType = retObj.getFieldValueString(0, 0);

            } catch (Exception e) {
                logger.error("Exception:", e);
            }

        }
        return aggType;
    }

    public ActionForward getKpiNeedleValue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession hs = request.getSession(false);
        PbReturnObject retObj = new PbReturnObject();
        String kpiIds = request.getParameter("kpiIds");
        String dbrdId = request.getParameter("dashboardId");
        retObj = getKpiGrpValue(kpiIds, hs, dbrdId);
        String result = String.valueOf(retObj.getFieldValueInt(0, 1));
        //////////////////////////.println("needlevalue=" + result);
        PrintWriter out = response.getWriter();
        out.print(result);
        return null;
    }

    public ActionForward getAggregationType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession hs = request.getSession(false);
        PbReturnObject retObj = new PbReturnObject();
        String kpiIds = request.getParameter("kpiIds");
        String dbrdId = request.getParameter("dashboardId");

        String aggType = getKpiAggtypeValue(kpiIds, hs, dbrdId);
        String result = aggType;
        //////////////////////////.println("needlevalue=" + result);
        PrintWriter out = response.getWriter();
        out.print(result);
        return null;
    }

    public ActionForward getGraphReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
        HttpSession session = request.getSession(false);
        String buzRoles = request.getParameter("buzRoles");
        String result = "";
        if (session != null) {
            result = dashboardTemplateDAO.getGraphReportsByBuzRoles(buzRoles);//DashBoardsGraphs);
            request.setAttribute("Graphs", result);
            return mapping.findForward("Viewer_graphs");
        } else {

            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getScoreCards(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            int userId = Integer.parseInt(String.valueOf(session.getAttribute("USERID")));
            DashboardTemplateDAO dao = new DashboardTemplateDAO();
            String result = dao.getScoreCards(userId);
            request.setAttribute("Scorecards", result);
            return mapping.findForward("Viewer_scorecards");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public void createDashboard(HttpServletRequest request, String dbrdId, String dashboardName, String dashboardDesc) throws Exception {

        String mapEnabled;
        Boolean SqlServer = false;
        Boolean MySql = false;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            SqlServer = true;
            MySql = false;
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            MySql = true;
            SqlServer = false;
        } else {
            SqlServer = false;
            MySql = false;
        }


        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        DashboardTemplateDAO paramdDAO = new DashboardTemplateDAO();
        String folderIds = "";
        String NewdashboardId = "";
        String dashletIds = request.getParameter("dashletIds");
        String[] dashletsArray = new String[0];
        Boolean isSaveAsNew = false;

        if (dashletIds != null && dashletIds.contains(",")) {
            dashletsArray = dashletIds.split(",");
        }

        String str;
        for (int i = 0; i < dashletsArray.length; i++) {
            str = dashletsArray[i].substring(9, dashletsArray[i].length());
            dashletsArray[i] = str.trim();
        }

        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(dbrdId);

        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();

        if (dashboardName == null) {
            isSaveAsNew = true;
            dashboardName = collect.reportName;
            dashboardDesc = collect.reportDesc;
        }

        //code to delete NEW  dashdetails from database and add new one
        String NewDashIds = request.getParameter("NewDashIds");
        String[] NewDashIdsArray = null;
        if (NewDashIds != null && NewDashIds.contains(",")) {
            NewDashIdsArray = NewDashIds.split(",");
        }

        String str1;
        if (NewDashIds != null && NewDashIds.contains(",")) {
            for (int i = 0; i < NewDashIdsArray.length; i++) {
                str1 = NewDashIdsArray[i];
                NewDashIdsArray[i] = str1.trim();
            }
        }

        folderIds = String.valueOf(session.getAttribute("folderDetails"));

        DashboardTemplateDAO dbrdTemplateDAO = new DashboardTemplateDAO();
        String finalBuildQuery = "";
        if (SqlServer) {
            finalBuildQuery = "select ident_current('PRG_AR_REPORT_MASTER') as NEXTVAL ";
            NewdashboardId = dbrdTemplateDAO.getCustomDbrdId(finalBuildQuery);
            NewdashboardId = String.valueOf((Integer.parseInt(NewdashboardId)) + 1);
        } else if (MySql) {
            finalBuildQuery = "select LAST_INSERT_ID(REPORT_ID) from PRG_AR_REPORT_MASTER order by 1 desc limit 1";
            NewdashboardId = dbrdTemplateDAO.getCustomDbrdId(finalBuildQuery);
            NewdashboardId = String.valueOf((Integer.parseInt(NewdashboardId)) + 1);
        } else {
            finalBuildQuery = "select PRG_AR_REPORT_MASTER_SEQ.nextval from dual";
            NewdashboardId = dbrdTemplateDAO.getCustomDbrdId(finalBuildQuery);
        }

        mapEnabled = container.isMapEnabled() ? "Y" : "N";
        new DashboardTemplateDAO().insertDashboard(Integer.parseInt(NewdashboardId), dashboardName, dashboardDesc, mapEnabled);
        paramdDAO.insertDashboardDetails(Integer.parseInt(NewdashboardId), folderIds);

        // getting from viewer page
        ArrayList params = new ArrayList();
        HashMap hmobj = container.getReportParameterHashMap();
        String[] BuildedParams = null;
        HashMap reportParametersValues = (HashMap) hmobj.get("reportParametersValues");
        if (reportParametersValues != null) {
            BuildedParams = (String[]) reportParametersValues.keySet().toArray(new String[0]);
            for (int j = 0; j < BuildedParams.length; j++) {
                params.add(BuildedParams[j]);
            }
        }

        HashMap timeDimMap = (HashMap) session.getAttribute("timeDetsMap");

//        String paramsString = "";
//        String timeString = "";
//        String paramString1 = "";
        StringBuilder paramsString = new StringBuilder();
        StringBuilder paramsString1 = new StringBuilder();
        if (params.size() > 0) {
            for (int i = 0; i < params.size(); i++) {
                paramsString.append(",").append(params.get(i));
//                paramsString = paramsString + "," + params.get(i);
            }
            paramsString = new StringBuilder(paramsString.substring(1));
//            paramsString = paramsString.substring(1);

            for (int i = 0; i < params.size(); i++) {
                paramsString1.append(",").append(params.get(i)).append(",").append((i + 1));
//                paramString1 = paramString1 + "," + params.get(i) + "," + (i + 1);
            }
//            paramString1 = paramString1.substring(1);
            paramsString1 = new StringBuilder(paramsString1.substring(1));
        }

        PbReturnObject pbro = paramdDAO.getParameters(paramsString.toString(), paramsString1.toString());

        ArrayList finalQueries = new ArrayList();
        Gson gson = new Gson();
        Type tarType = new TypeToken<List<String>>() {
        }.getType();

        for (int i = 0; i < pbro.getRowCount(); i++) {
            String element_id = pbro.getFieldValueString(i, 0);
            String disp_name = pbro.getFieldValueString(i, 1);
            String dim_id = pbro.getFieldValueString(i, 2);
            String dim_tab_id = pbro.getFieldValueString(i, 3);
            String buss_table = pbro.getFieldValueString(i, 4);
            String defaultVal = gson.toJson(reportParametersValues.get(BuildedParams[i]), tarType);

            String query = "";
            if (SqlServer || MySql) {
                query = "insert into PRG_AR_REPORT_PARAM_DETAILS (REPORT_ID,PARAM_DISP_NAME,ELEMENT_ID,DIM_ID,DIM_TAB_ID,BUSS_TABLE,DEFAULT_VALUE) values (" + NewdashboardId + ",'" + disp_name + "'," + element_id + "," + dim_id + "," + dim_tab_id + "," + buss_table + ",'" + defaultVal + "')";
            } else {
                query = "insert into PRG_AR_REPORT_PARAM_DETAILS (PARAM_DISP_ID,REPORT_ID,PARAM_DISP_NAME,ELEMENT_ID,DIM_ID,DIM_TAB_ID,BUSS_TABLE,DEFAULT_VALUE) values (PRG_AR_REPORT_PARAM_DETLS_SEQ.nextval," + NewdashboardId + ",'" + disp_name + "'," + element_id + "," + dim_id + "," + dim_tab_id + "," + buss_table + ",'" + defaultVal + "')";
            }
            finalQueries.add(query);
        }

        paramdDAO.insertReportParamDetails(finalQueries);
        String rowViewBy = collect.reportRowViewbyValues.get(0);
        if (rowViewBy.equalsIgnoreCase("")) {
            rowViewBy = "Time";
        }
        ArrayList view_elements = new ArrayList();
        if (params != null) {
            String viewElmntQuery = "";
            if (SqlServer || MySql) {
                viewElmntQuery = "insert into PRG_AR_REPORT_VIEW_BY_MASTER (REPORT_ID,VIEW_BY_SEQ,IS_ROW_EDGE,ROW_SEQ,COL_SEQ,DEFAULT_VALUE) values (" + NewdashboardId + ",1,'Y',1,-1,'" + rowViewBy + "')";
            } else {
                viewElmntQuery = "insert into PRG_AR_REPORT_VIEW_BY_MASTER (VIEW_BY_ID,REPORT_ID,VIEW_BY_SEQ,IS_ROW_EDGE,ROW_SEQ,COL_SEQ,DEFAULT_VALUE) values (PRG_AR_REP_VIEW_BY_MASTER_SEQ.nextval," + NewdashboardId + ",1,'Y',1,-1,'" + rowViewBy + "')";
            }

            PbReturnObject paramPbro = paramdDAO.getParamDetails(Integer.parseInt(dbrdId));
            view_elements.add(viewElmntQuery);

            for (int i = 0; i < paramPbro.getRowCount(); i++) {
                String param_disp_id = paramPbro.getFieldValueString(i, 0);
                String disp_seq_no = paramPbro.getFieldValueString(i, 1);

                String viewbyQuery = "";

                if (SqlServer) {
                    viewbyQuery = "insert into PRG_AR_REPORT_VIEW_BY_DETAILS (PARAM_DISP_ID,VIEW_BY_ID,DISP_SEQ_NO) values (" + param_disp_id + ",ident_current('PRG_AR_REPORT_VIEW_BY_MASTER')," + i + ")";
                    // ////.println("PRG_AR_REPORT_VIEW_BY_DETAILS="+viewbyQuery);
                } else if (MySql) {
                    viewbyQuery = "insert into PRG_AR_REPORT_VIEW_BY_DETAILS (PARAM_DISP_ID,VIEW_BY_ID,DISP_SEQ_NO) values (" + param_disp_id + ",(select LAST_INSERT_ID(VIEW_BY_ID) from PRG_AR_REPORT_VIEW_BY_MASTER order by 1 desc limit 1)," + i + ")";
                    // ////.println("PRG_AR_REPORT_VIEW_BY_DETAILS="+viewbyQuery);
                } else {
                    viewbyQuery = "insert into PRG_AR_REPORT_VIEW_BY_DETAILS (VIEW_BY_DTL_ID,PARAM_DISP_ID,VIEW_BY_ID,DISP_SEQ_NO) values (PRG_AR_REP_VIEW_BY_DETLS_SEQ.nextval," + param_disp_id + ",PRG_AR_REP_VIEW_BY_MASTER_SEQ.currval," + i + ")";
                }

                view_elements.add(viewbyQuery);
            }
        }

        HashMap timeDetailsHashmap = (HashMap) container.getTimeParameterHashMap().get("TimeDimHashMap");
        ArrayList timeDetails = (ArrayList) container.getTimeParameterHashMap().get("TimeDetailstList");
        ArrayList timeParams = new ArrayList();
        String[] BuildedParamsWithTime = null;

        if (timeDetailsHashmap != null) {
            BuildedParamsWithTime = (String[]) timeDetailsHashmap.keySet().toArray(new String[0]); //contains 31150,31149,31156,31153
            for (int i = 0; i < BuildedParamsWithTime.length; i++) {
                timeParams.add(BuildedParamsWithTime[i]);
            }
        }

        String timeQry = "";

        if (SqlServer || MySql) {
            timeQry = "insert into PRG_AR_REPORT_TIME (TIME_TYPE, TIME_LEVEL, REPORT_ID) values"
                    + "('" + timeDetails.get(1) + "','" + timeDetails.get(0) + "'," + NewdashboardId + ")";
        } else {
            timeQry = "insert into PRG_AR_REPORT_TIME (REP_TIME_ID, TIME_TYPE, TIME_LEVEL, REPORT_ID) values"
                    + "(PRG_AR_REPORT_TIME_SEQ.nextval,'" + timeDetails.get(1) + "','" + timeDetails.get(0) + "'," + NewdashboardId + ")";
        }

        String timeQRepry = "select REP_TIME_ID from  PRG_AR_REPORT_TIME where REPORT_ID=" + dbrdId;
        PbDb pbdb1 = new PbDb();
        PbReturnObject getRepTimeId = pbdb1.execSelectSQL(timeQRepry);
        int k = 0;
        if (getRepTimeId != null) {
            k = getRepTimeId.getRowCount();
        }
        if (k != 0) {
            k = k - 1;
        }
        if (getRepTimeId != null && getRepTimeId.getRowCount() > 1) {
            if (getRepTimeId.getFieldValueInt(0, 0) > getRepTimeId.getFieldValueInt(1, 0)) {
                k = 0;
            }
        }
        String timeDetRepQry = "";
        PbReturnObject getDefaultVals = new PbReturnObject();
        if (getRepTimeId != null && getRepTimeId.getFieldValueInt(k, 0) != 0 && !getRepTimeId.getFieldValueString(k, 0).equalsIgnoreCase("") && getRepTimeId.getFieldValueString(k, 0) != null) {
            timeDetRepQry = "select COLUMN_TYPE,DEFAULT_VALUE from PRG_AR_REPORT_TIME_DETAIL where REP_TIME_ID = " + getRepTimeId.getFieldValueInt(k, 0);
            getDefaultVals = pbdb1.execSelectSQL(timeDetRepQry);
        }
        //added by Dinanath for exact matching
        int psize = getDefaultVals.getRowCount();
        String[] colNames = getDefaultVals.getColumnNames();
        view_elements.add(timeQry);
        for (int time = 0; time < timeParams.size(); time++) {
            String[] keys = (String[]) timeDetailsHashmap.keySet().toArray(new String[0]);

            for (int i = 0; i < keys.length; i++) {
                ArrayList details = (ArrayList) timeDetailsHashmap.get(keys[i]);
                if (timeParams.get(time).toString().equalsIgnoreCase(details.get(6).toString())) {
                    String timeDetailsQry = "";
                    if (SqlServer) {
                        if (timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE") || timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE1") || timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE2") || timeParams.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE1") || timeParams.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                            //eddited by Dinanath for exact matching
                            if (getDefaultVals != null && !getDefaultVals.getFieldValueString(i, "DEFAULT_VALUE").equalsIgnoreCase("") && getDefaultVals.getFieldValueString(i, "DEFAULT_VALUE") != null && !getDefaultVals.getFieldValueString(i, "DEFAULT_VALUE").equalsIgnoreCase("Month")) {
                                for (int ii = 0; ii < psize; ii++) {//added by Dinanath for exact matching
                                    if (timeParams.get(time).toString().equalsIgnoreCase(getDefaultVals.getFieldValueString(ii, "COLUMN_TYPE").toString())) {
                                        timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE,DEFAULT_DATE,DEFAULT_VALUE) "
                                                + "values (ident_current('PRG_AR_REPORT_TIME'),'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ",null,'" + getDefaultVals.getFieldValueString(ii, "DEFAULT_VALUE") + "')";
                                    }
                                }
                            } else {
                                timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE,DEFAULT_DATE,DEFAULT_VALUE) "
                                        + "values (ident_current('PRG_AR_REPORT_TIME'),'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ",convert(datetime,'" + ((String) details.get(0)) + "',101),null)";
                            }
                        } else {
                            timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE,DEFAULT_DATE,DEFAULT_VALUE) "
                                    + "values (ident_current('PRG_AR_REPORT_TIME'),'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ",null,'" + details.get(0) + "')";
                            //////.println("PRG_AR_REPORT_TIME_DETAIL="+timeDetailsQry);
                        }
                    } else if (MySql) {
                        if (timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE") || timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE1") || timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE2") || timeParams.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE1") || timeParams.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                            if (getDefaultVals != null && !getDefaultVals.getFieldValueString(i, "DEFAULT_VALUE").equalsIgnoreCase("") && getDefaultVals.getFieldValueString(i, "DEFAULT_VALUE") != null && !getDefaultVals.getFieldValueString(i, "DEFAULT_VALUE").equalsIgnoreCase("Month")) {
                                for (int ii = 0; ii < psize; ii++) {//added by Dinanath for exact matching
                                    if (timeParams.get(time).toString().equalsIgnoreCase(getDefaultVals.getFieldValueString(ii, "COLUMN_TYPE").toString())) {
                                        timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE,DEFAULT_DATE,DEFAULT_VALUE) "
                                                + "values (select LAST_INSERT_ID(REP_TIME_ID) from PRG_AR_REPORT_TIME order by 1 desc limit 1),'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ",null,'" + getDefaultVals.getFieldValueString(ii, "DEFAULT_VALUE") + "')";
                                    }
                                }
                            } else {
                                timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE,DEFAULT_DATE,DEFAULT_VALUE) "
                                        + "values ((select cast(LAST_INSERT_ID(REP_TIME_ID) as decimal) from PRG_AR_REPORT_TIME order by 1 desc limit 1),'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ",str_to_date('" + ((String) details.get(0)) + "','%m/%d/%Y'),null)";
                            }
                        } else {
                            timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE,DEFAULT_DATE,DEFAULT_VALUE) "
                                    + "values ((select cast(LAST_INSERT_ID(REP_TIME_ID) as decimal) from PRG_AR_REPORT_TIME order by 1 desc limit 1),'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ",null,'" + details.get(0) + "')";
                            //////.println("PRG_AR_REPORT_TIME_DETAIL="+timeDetailsQry);
                        }
                    } else {
                        if (timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE") || timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE1") || timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE2") || timeParams.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE1") || timeParams.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                            if (getDefaultVals != null && !getDefaultVals.getFieldValueString(i, "DEFAULT_VALUE").equalsIgnoreCase("") && getDefaultVals.getFieldValueString(i, "DEFAULT_VALUE") != null && !getDefaultVals.getFieldValueString(i, "DEFAULT_VALUE").equalsIgnoreCase("Month")) {
                                for (int ii = 0; ii < psize; ii++) {//added by Dinanath for exact matching
                                    if (timeParams.get(time).toString().equalsIgnoreCase(getDefaultVals.getFieldValueString(ii, "COLUMN_TYPE").toString())) {
                                        timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_DTL_ID, REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE,DEFAULT_DATE,DEFAULT_VALUE) "
                                                + "values (PRG_AR_REPORT_TIME_DETAIL_SEQ.nextval,PRG_AR_REPORT_TIME_SEQ.currval,'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ",null,'" + getDefaultVals.getFieldValueString(ii, "DEFAULT_VALUE") + "')";
                                    }
                                }
                            } else {
                                timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_DTL_ID, REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE,DEFAULT_DATE,DEFAULT_VALUE) "
                                        + "values (PRG_AR_REPORT_TIME_DETAIL_SEQ.nextval,PRG_AR_REPORT_TIME_SEQ.currval,'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ",to_date('" + ((String) details.get(0)) + "','mm/dd/yyyy'),null)";
                            }
                        } else if (timeParams.get(time).toString().equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                            timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_DTL_ID, REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE,DEFAULT_DATE,DEFAULT_VALUE) "
                                    + "values (PRG_AR_REPORT_TIME_DETAIL_SEQ.nextval,PRG_AR_REPORT_TIME_SEQ.currval,'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ",null,'" + details.get(0) + "')";
                        } else if (timeParams.get(time).toString().equalsIgnoreCase("PRG_COMPARE")) {
                            timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_DTL_ID, REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE,DEFAULT_DATE,DEFAULT_VALUE) "
                                    + "values (PRG_AR_REPORT_TIME_DETAIL_SEQ.nextval,PRG_AR_REPORT_TIME_SEQ.currval,'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ",null,'" + details.get(0) + "')";
                        } else {
                            timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_DTL_ID, REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE) "
                                    + "values (PRG_AR_REPORT_TIME_DETAIL_SEQ.nextval,PRG_AR_REPORT_TIME_SEQ.currval,'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ")";

                        }
                    }

                    view_elements.add(timeDetailsQry);
                }
            }
        }


        List<DashletDetail> dashletList = collect.dashletDetails;
//         HashMap<String,KPIGroupingHelper> kpiGroupHelHashMap=(HashMap<String, KPIGroupingHelper>) container.getKPIGroupHashMap();
        //Inserting KPI Details into the respective KPI tables
        for (int i = 0; i < dashletList.size(); i++) {
            DashletDetail dashlet = dashletList.get(i);
            String reportType = dashlet.getDisplayType();
            int row = dashlet.getRow();
            int col = dashlet.getCol();
            int rowSpan = dashlet.getRowSpan();
            int colSpan = dashlet.getColSpan();
            if (DashboardConstants.KPI_REPORT.equalsIgnoreCase(reportType)) {
                List<String> kpiQueries = getKPIInsertQueries(NewdashboardId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan, container.ViewBy); // added by mohit for saving view by
                view_elements.addAll(kpiQueries);
            } //  Save the KPIAttributesMap details in the respective table
            else if (DashboardConstants.MAP_REPORT.equalsIgnoreCase(reportType)) {
                List<String> mapQueries = getMapInsertQueries(NewdashboardId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan);
                view_elements.addAll(mapQueries);
            } //  Save the KPI Graph details in the respective table
            else if (DashboardConstants.KPI_GRAPH_REPORT.equalsIgnoreCase(reportType)) {
                List<String> kpiGraphQueries = getKPIGraphInsertQueries(NewdashboardId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan);
                view_elements.addAll(kpiGraphQueries);
            } else if (DashboardConstants.DASHBOARD_GRAPH_REPORT.equalsIgnoreCase(reportType)
                    || DashboardConstants.GRAPH_REPORT.equalsIgnoreCase(reportType)) {
                dashletPropertiesHelper = dashlet.getDashletPropertiesHelper();
                List<String> graphQueries = getGraphInsertQueries(NewdashboardId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan);
                view_elements.addAll(graphQueries);
            } else if (DashboardConstants.TABLE_REPORT.equalsIgnoreCase(reportType)) {
                dashletPropertiesHelper = dashlet.getDashletPropertiesHelper();
                List<String> graphQueries = getGraphInsertQueries(NewdashboardId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan);
                view_elements.addAll(graphQueries);
            } else if (DashboardConstants.TEXTKPI_REPORT.equalsIgnoreCase(reportType)) {
                List<String> graphQueries = getTextKPIInsertQueries(NewdashboardId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan, collect.getTextKPIRowViewBy());
                view_elements.addAll(graphQueries);
            } else if (DashboardConstants.KPI_WITH_GRAPH.equalsIgnoreCase(reportType)) {
                dashlet.setRowViewBy(rowViewBy);
                List<String> graphQueries = getGraphInsertQueries(NewdashboardId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan);
                view_elements.addAll(graphQueries);
            }

        }

        //Store Scorecard Details
        ArrayListMultimap<String, String> scoreCardDetails = collect.scoreCardDetails;
        Set<String> keySet = scoreCardDetails.keySet();
        Iterator<String> keyIter = keySet.iterator();
        StringBuilder keyStr = new StringBuilder();

        if (scoreCardDetails != null && scoreCardDetails.size() != 0) {
            // First delete the entries from the dashlet detail table
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                keyStr.append("," + key);
            }
            DashboardTemplateDAO dao = new DashboardTemplateDAO();
            dao.deleteFromDashletDetail(keyStr.substring(1));

            for (DashletDetail detail : dashletList) {
                int dispSeq = detail.getDisplaySequence();
                String dashletName = detail.getDashletName();
                String masterId = detail.getKpiMasterId();
                if ("SCARD".equalsIgnoreCase(detail.getDisplayType())) {
                    String newDbrdgrp1Query = "";
                    if (SqlServer || MySql) {
                        newDbrdgrp1Query = "insert into PRG_AR_DASHBOARD_DETAILS (DASHBOARD_ID, REF_REPORT_ID, GRAPH_ID, KPI_MASTER_ID, DISPLAY_SEQUENCE, DISPLAY_TYPE, KPI_HEADING)  values (" + NewdashboardId + "," + NewdashboardId + ",0," + masterId + "," + dispSeq + ",'SCARD','" + dashletName + "')";
                    } else {
                        newDbrdgrp1Query = "insert into PRG_AR_DASHBOARD_DETAILS (DASHBOARD_DETAILS_ID, DASHBOARD_ID, REF_REPORT_ID, GRAPH_ID, KPI_MASTER_ID, DISPLAY_SEQUENCE, DISPLAY_TYPE, KPI_HEADING, KPI_NAME, KPI_SYMBOL)  values (PRG_AR_DASHBOARD_DETS_SEQ.nextval," + NewdashboardId + "," + NewdashboardId + ",0," + masterId + "," + dispSeq + ",'SCARD','" + dashletName + "')";
                    }

                    view_elements.add(newDbrdgrp1Query);
                }
            }

            keyIter = keySet.iterator();
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                List<String> scList = scoreCardDetails.get(key);
                for (String scId : scList) {
                    String qry = "";
                    if (SqlServer || MySql) {
                        qry = "insert into PRG_AR_DASHLET_DETAILS values (" + key + "," + scId + ")";
                    } else {
                        qry = "insert into PRG_AR_DASHLET_DETAILS values (prg_ar_dashlet_detail_id_seq.nextval," + key + "," + scId + ")";
                    }

                    view_elements.add(qry);
                }
            }
        }
        char favRepCheck;
        int repSequence;
        PbDb pbdb = new PbDb();
        String NewDbIdCheckQuery = null;
        String qury = "select PUR_FAV_REPORT,pur_report_sequence from PRG_AR_USER_REPORTS where REPORT_ID=" + dbrdId;
        PbReturnObject getFavRepCheck = pbdb.execSelectSQL(qury);
        if (getFavRepCheck.getFieldValueString(0, 0) != null && !getFavRepCheck.getFieldValueString(0, 0).equalsIgnoreCase("")) {
            favRepCheck = getFavRepCheck.getFieldValueString(0, 0).charAt(0);
            repSequence = getFavRepCheck.getFieldValueInt(0, 1);
            if (favRepCheck == 'Y' && isSaveAsNew) {
                NewDbIdCheckQuery = "update PRG_AR_USER_REPORTS set PUR_FAV_REPORT='" + favRepCheck + "', pur_report_sequence='" + repSequence + "' where REPORT_ID=" + NewdashboardId;
                view_elements.add(NewDbIdCheckQuery);
                if (!getFavRepCheck.getFieldValueString(0, 0).equalsIgnoreCase("") && getFavRepCheck.getFieldValueString(0, 0) != null) {
                    favRepCheck = getFavRepCheck.getFieldValueString(0, 0).charAt(0);
                    repSequence = getFavRepCheck.getFieldValueInt(0, 1);
                    if (favRepCheck == 'Y' && isSaveAsNew) {
                        NewDbIdCheckQuery = "update PRG_AR_USER_REPORTS set PUR_FAV_REPORT='" + favRepCheck + "', pur_report_sequence='" + repSequence + "' where REPORT_ID=" + NewdashboardId;
                        view_elements.add(NewDbIdCheckQuery);
                    }
                }
            }
        }
        //
        DashboardViewerDAO viewerDao = new DashboardViewerDAO();
        paramdDAO.insertKpiGraphs(view_elements);
        if (MySql) {
        } else if (SqlServer) {
        } else {
            for (DashletDetail detail : dashletList) {
                if (detail != null && detail.getUpdateClobQry() != null && !detail.getUpdateClobQry().isEmpty() && !detail.getUpdateClobQry().equalsIgnoreCase("")) {
                    viewerDao.procedureExecution(detail.getUpdateClobQry());
                }
            }
        }

        map.remove(dbrdId);
        request.setAttribute("REPORTID", NewdashboardId);
        request.setAttribute("ReportType", "D");
        request.setAttribute("UserFolderIds", folderIds);
        request.setAttribute("ReportName", dashboardName);
        if (NewDbIdCheckQuery != null) {
            request.setAttribute("FavQuery", NewDbIdCheckQuery);
        }
    }

    public List<String> getGraphInsertQueries(String dashboardId, String dashboardName, int dispSeq, DashletDetail dashlet, boolean sqlServer, boolean MySql, int row, int col, int rowSpan, int colSpan) {
        List<String> queries = new ArrayList<String>();

        GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
        JqplotGraphProperty jqprop = new JqplotGraphProperty();
        jqprop = dashlet.getJqplotgrapprop();
        PbDb pbdb = new PbDb();

        String graphId = null;

        try {
            if (sqlServer) {
                String queryForMasterSequence = "select ident_current('PRG_AR_GRAPH_MASTER') ";
                PbReturnObject getMasterSeqDetsObj = pbdb.execSelectSQL(queryForMasterSequence);
                graphId = getMasterSeqDetsObj.getFieldValueString(0, 0);
            } else if (MySql) {
                String queryForMasterSequence = "select LAST_INSERT_ID(GRAPH_ID) from PRG_AR_GRAPH_MASTER order by 1 desc limit 1 ";
                PbReturnObject getMasterSeqDetsObj = pbdb.execSelectSQL(queryForMasterSequence);
                graphId = getMasterSeqDetsObj.getFieldValueString(0, 0);
            } else {
                String queryForMasterSequence = "select PRG_AR_GRAPH_MASTER_SEQ.nextval from dual";
                PbReturnObject getMasterSeqDetsObj = pbdb.execSelectSQL(queryForMasterSequence);
                graphId = getMasterSeqDetsObj.getFieldValueString(0, 0);
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        String graphMasterQry = "";
        if (sqlServer || MySql) {

            graphMasterQry = "INSERT INTO PRG_AR_GRAPH_MASTER(REPORT_ID,GRAPH_NAME,GRAPH_FAMILY,GRAPH_TYPE,GRAPH_ORDER,GRAPH_SIZE,GRAPH_HEIGHT,GRAPH_WIDTH,ALLOW_LINK,ALLOW_LABEL,ALLOW_LEGEND,ALLOW_TOOLTIP,GRAPH_CLASS,"
                    + "LEFT_Y_AXIS_LABEL,RIGHT_Y_AXIS_LABEL,X_AXIS_LABEL,FONT_NAME,FONT_SIZE,FONT_COLOR,LEGEND_LOC,SHOW_GRID_X_AXIS,SHOW_GRID_Y_AXIS,BACK_COLOR,SHOW_DATA,ROW_VALUES,SHOW_GT,SHOW_TABLE,GRAPH_PROPERTY_XML,GRAPH_DISPLAY_ROWS,TIME_SERIES,JQ_PROPERTIES) "
                    + "values(" + "&,'&','&',&,&,&,'&','&','&','&','&','&',&,'&','&','&','&',&,'&','&','&','&','&','&','&','&','&','&','&','&','&')";
        } else {
            graphMasterQry = "INSERT INTO PRG_AR_GRAPH_MASTER(GRAPH_ID,REPORT_ID,GRAPH_NAME,GRAPH_FAMILY,GRAPH_TYPE,GRAPH_ORDER,GRAPH_SIZE,GRAPH_HEIGHT,GRAPH_WIDTH,ALLOW_LINK,ALLOW_LABEL,ALLOW_LEGEND,ALLOW_TOOLTIP,GRAPH_CLASS,"
                    + "LEFT_Y_AXIS_LABEL,RIGHT_Y_AXIS_LABEL,X_AXIS_LABEL,FONT_NAME,FONT_SIZE,FONT_COLOR,LEGEND_LOC,SHOW_GRID_X_AXIS,SHOW_GRID_Y_AXIS,BACK_COLOR,SHOW_DATA,ROW_VALUES,SHOW_GT,SHOW_TABLE,GRAPH_PROPERTY_XML,GRAPH_DISPLAY_ROWS,TIME_SERIES,JQ_PROPERTIES) "
                    + "values(" + graphId + ",&,'&','&',&,&,&,'&','&','&','&','&','&',&,'&','&','&','&',&,'&','&','&','&','&','&','&','&','&','&','&','&','&')";
        }

        Object[] objArr = new Object[31];
        objArr[0] = dashboardId;
        objArr[1] = dashlet.getDashletName();
        objArr[2] = graphDetails.getGraphFamily();
        objArr[3] = graphDetails.getGraphType();
        objArr[4] = graphDetails.getGraphOrder();
        objArr[5] = graphDetails.getGraphSize();
        objArr[6] = graphDetails.getGraphHeight();
        objArr[7] = graphDetails.getGraphWidth();
        if (graphDetails.isLinkAllowed()) {
            objArr[8] = "Y";
        } else {
            objArr[8] = "N";
        }
//            label, legen, tooltip, class
        if (graphDetails.isLabelAllowed()) {
            objArr[9] = "Y";
        } else {
            objArr[9] = "N";
        }
        if (graphDetails.isLegendAllowed()) {
            objArr[10] = "Y";
        } else {
            objArr[10] = "N";
        }
        if (graphDetails.isTooltipAllowed()) {
            objArr[11] = "Y";
        } else {
            objArr[11] = "N";
        }
        objArr[12] = graphDetails.getGraphClass();
        objArr[13] = graphDetails.getLeftYAxisLabel();
        objArr[14] = graphDetails.getRightYAxisLabel();
        objArr[15] = graphDetails.getXAxisLabel();
        objArr[16] = graphDetails.getFontName();
        objArr[17] = graphDetails.getFontSize();
        objArr[18] = graphDetails.getFontColor();
        objArr[19] = graphDetails.getLegendLocation();
        if (graphDetails.isShowXAxisGrid()) {
            objArr[20] = "Y";
        } else {
            objArr[20] = "N";
        }
        if (graphDetails.isShowYAxisGrid()) {
            objArr[21] = "Y";
        } else {
            objArr[21] = "N";
        }
        objArr[22] = graphDetails.getBackgroundColor();
        if (graphDetails.isShowData()) {
            objArr[23] = "Y";
        } else {
            objArr[23] = "N";
        }
        objArr[24] = graphDetails.getRowValues();
        if (graphDetails.isShowGT()) {
            objArr[25] = "Y";
        } else {
            objArr[25] = "N";
        }
        if (graphDetails.isShowAsTable()) {
            objArr[26] = "Y";
        } else {
            objArr[26] = "N";
        }
        objArr[27] = graphDetails.getGraphProperty().toXml();
        if (graphDetails.getDisplayRows() == null || graphDetails.getDisplayRows().equalsIgnoreCase("")) {
            objArr[28] = 10;
        } else {
            objArr[28] = graphDetails.getDisplayRows();
        }
        objArr[29] = String.valueOf(graphDetails.isTimeSeries());
        String jqpropertiesString = null;
        Gson gson1 = new Gson();
        jqpropertiesString = gson1.toJson(jqprop);
        objArr[30] = jqpropertiesString;

        String finalQuery = pbdb.buildQuery(graphMasterQry, objArr);
        queries.add(finalQuery);

        List<QueryDetail> queryDetails = graphDetails.getQueryDetails();
        queries.addAll(getQueryDetailsInsertQueries(dashboardId, queryDetails, sqlServer, MySql));
        String elementIds = "";
        String basicElement = "";
        String elementName = "";
        if (dashlet.getKpiType() != null && (dashlet.getKpiType().equalsIgnoreCase("KpiWithGraph") || dashlet.getDisplayType().equalsIgnoreCase("KpiWithGraph"))) {
            for (int i = 0; i < queryDetails.size(); i++) {
                String grpDetsQry = "";
                QueryDetail qd = queryDetails.get(i);
                String axisGr = graphDetails.getAxis();
                elementIds += "," + qd.getElementId();
                if (i == 0) {
                    basicElement = qd.getElementId();
                    elementName = qd.getDisplayName();
                    if (axisGr != null) {
                        if (axisGr.equalsIgnoreCase("")) {
                            axisGr = null;
                        }
                    }
                    if (sqlServer) {
                        grpDetsQry = "INSERT INTO PRG_AR_GRAPH_DETAILS(GRAPH_ID,COL_NAME,ELEMENT_ID,COLUMN_ORDER,QUERY_COL_ID,AXIS) "
                                + "values( ident_current('PRG_AR_GRAPH_MASTER'),'" + qd.getDisplayName() + "'," + qd.getElementId() + ","
                                + "" + 0 + ", null," + axisGr + ")";
                    } else if (MySql) {
                        grpDetsQry = "INSERT INTO PRG_AR_GRAPH_DETAILS(GRAPH_ID,COL_NAME,ELEMENT_ID,COLUMN_ORDER,QUERY_COL_ID,AXIS) "
                                + "values( (select LAST_INSERT_ID(GRAPH_ID) from PRG_AR_GRAPH_MASTER order by 1 desc limit 1 ),'" + qd.getDisplayName() + "'," + qd.getElementId() + ","
                                + "" + 0 + ", null," + axisGr + ")";
                    } else {
                        grpDetsQry = "INSERT INTO PRG_AR_GRAPH_DETAILS(GRAPH_COL_ID,GRAPH_ID,COL_NAME,ELEMENT_ID,COLUMN_ORDER,QUERY_COL_ID,AXIS) "
                                + "values(PRG_AR_GRAPH_DETAILS_SEQ.nextval," + graphId + ",'" + qd.getDisplayName() + "'," + qd.getElementId() + ","
                                + "" + 0 + ", null," + axisGr + ")";
                    }
                    queries.add(grpDetsQry);
                }

            }

        } else {
            for (int i = 0; i < queryDetails.size(); i++) {
                String grpDetsQry = "";
                QueryDetail qd = queryDetails.get(i);
                String axisGr = graphDetails.getAxis();
                elementIds += "," + qd.getElementId();
                if (i == 0) {
                    basicElement = qd.getElementId();
                    elementName = qd.getDisplayName();
                }
                if (axisGr != null) {
                    if (axisGr.equalsIgnoreCase("")) {
                        axisGr = null;
                    }
                }
                if (sqlServer) {
                    grpDetsQry = "INSERT INTO PRG_AR_GRAPH_DETAILS(GRAPH_ID,COL_NAME,ELEMENT_ID,COLUMN_ORDER,QUERY_COL_ID,AXIS) "
                            + "values( ident_current('PRG_AR_GRAPH_MASTER'),'" + qd.getDisplayName() + "'," + qd.getElementId() + ","
                            + "" + 0 + ", null," + axisGr + ")";
                } else if (MySql) {
                    grpDetsQry = "INSERT INTO PRG_AR_GRAPH_DETAILS(GRAPH_ID,COL_NAME,ELEMENT_ID,COLUMN_ORDER,QUERY_COL_ID,AXIS) "
                            + "values( (select LAST_INSERT_ID(GRAPH_ID) from PRG_AR_GRAPH_MASTER order by 1 desc limit 1),'" + qd.getDisplayName() + "'," + qd.getElementId() + ","
                            + "" + 0 + ", null," + axisGr + ")";
                } else {
                    grpDetsQry = "INSERT INTO PRG_AR_GRAPH_DETAILS(GRAPH_COL_ID,GRAPH_ID,COL_NAME,ELEMENT_ID,COLUMN_ORDER,QUERY_COL_ID,AXIS) "
                            + "values(PRG_AR_GRAPH_DETAILS_SEQ.nextval," + graphId + ",'" + qd.getDisplayName() + "'," + qd.getElementId() + ","
                            + "" + 0 + ", null," + axisGr + ")";
                }
                queries.add(grpDetsQry);
            }
        }

        String dispType = dashlet.getDisplayType();
        String tabName = dashlet.getkpiName();
        String dashletId = dashlet.getDashBoardDetailId();

        DashboardViewerDAO viewerDao = new DashboardViewerDAO();
        if (dashletPropertiesHelper == null) {
            dashletPropertiesHelper = viewerDao.getDashletPropertiesHelperObject(dashletId);
        }


        Gson gson = new Gson();
        String propertiesString = "";
        if (dashletPropertiesHelper != null) {
            propertiesString = gson.toJson(dashletPropertiesHelper);
        }
//         String[] tabProperties =

        String kpiTableColorGrp = "";
        if (dashlet.getDashbrdTableColor() != null) {
            List<DashboardTableColorGroupHelper> colorGroupHelperrs = dashlet.getDashbrdTableColor();

            if (!colorGroupHelperrs.isEmpty()) {
                kpiTableColorGrp = gson.toJson(colorGroupHelperrs);
            }
        }
        if (dispType.equalsIgnoreCase("KpiWithGraph")) {
            kpiTableColorGrp = gson.toJson(dashlet.getSingleGroupHelpers());
        }
        String grp1Query = "";
        if (sqlServer) {
            grp1Query = "insert into PRG_AR_DASHBOARD_DETAILS  (DASHBOARD_ID, REF_REPORT_ID, GRAPH_ID, KPI_MASTER_ID, DISPLAY_SEQUENCE, DISPLAY_TYPE, KPI_HEADING,ROW_1,COL_1,ROW_SPAN,COL_SPAN,KPI_NAME,DASHLET_PROPERTIES,DASHLET_KPIANDTABLE_PROP,ROW_VIEW_BY,GROUPID) values (" + dashboardId + "," + dashlet.getRefReportId() + ",ident_current('PRG_AR_GRAPH_MASTER'),0," + dispSeq + ",'" + dispType + "',null,'" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "','" + tabName + "','" + propertiesString + "','" + kpiTableColorGrp + "',null,null)";
        }
        if (MySql) {
            grp1Query = "insert into PRG_AR_DASHBOARD_DETAILS  (DASHBOARD_ID, REF_REPORT_ID, GRAPH_ID, KPI_MASTER_ID, DISPLAY_SEQUENCE, DISPLAY_TYPE, KPI_HEADING,ROW_1,COL_1,ROW_SPAN,COL_SPAN,KPI_NAME,DASHLET_PROPERTIES,DASHLET_KPIANDTABLE_PROP,ROW_VIEW_BY,GROUPID) values (" + dashboardId + "," + dashlet.getRefReportId() + ",(select LAST_INSERT_ID(GRAPH_ID) from PRG_AR_GRAPH_MASTER order by 1 desc limit 1),0," + dispSeq + ",'" + dispType + "',null,'" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "','" + tabName + "','" + propertiesString + "','" + kpiTableColorGrp + "',null,null)";
        } else {
            grp1Query = "insert into PRG_AR_DASHBOARD_DETAILS  (DASHBOARD_DETAILS_ID, DASHBOARD_ID, REF_REPORT_ID, GRAPH_ID, KPI_MASTER_ID, DISPLAY_SEQUENCE, DISPLAY_TYPE, KPI_HEADING,ROW_1,COL_1,ROW_SPAN,COL_SPAN,KPI_NAME,DASHLET_PROPERTIES,DASHLET_KPIANDTABLE_PROP,ROW_VIEW_BY,GROUPID) values (PRG_AR_DASHBOARD_DETS_SEQ.nextval," + dashboardId + "," + dashlet.getRefReportId() + "," + graphId + ",0," + dispSeq + ",'" + dispType + "','','" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "','" + tabName + "','" + propertiesString + "','" + kpiTableColorGrp + "',null,null)";
        }
        queries.add(grp1Query);

        if (dashlet.getKpiType() != null && (dashlet.getKpiType().equalsIgnoreCase("KpiWithGraph") || dashlet.getDisplayType().equalsIgnoreCase("KpiWithGraph"))) {
            String kpiGraphQuery = "";
            String viewBy = dashlet.getRowViewBy();
            if (viewBy.equalsIgnoreCase("TIME")) {
                viewBy = "0";
            }
            if (!elementIds.equalsIgnoreCase("")) {
                elementIds = elementIds.substring(1);
            }
            String elemId = "";
            KPI kpiDetail = dashlet.getKpiDetails();
            ArrayListMultimap<String, KPIElement> kpiElementMap = kpiDetail.getKPIElementsMap();
            List<String> elements = kpiDetail.getElementIds();
            if (elements != null) {
                for (int i = 0; i < elements.size(); i++) {


                    if (sqlServer) {
                        kpiGraphQuery = "INSERT INTO PRG_AR_KPIWITHGRAPH(DASHLET_ID,KPI_ID,GRAPH_ID,GRAPH_TYPE,VIEWBY,ELEMENT_IDS,GRAPH_MEASURE,SEQUENCE_NUM,Ref_Report_Id,Ref_Report_Type)"
                                + "VALUES(IDENT_CURRENT('PRG_AR_DASHBOARD_DETAILS')," + dashlet.getKpiMasterId() + "," + graphId + ",'" + graphDetails.getGraphType() + "'," + viewBy + ",'" + elements.get(i) + "'," + basicElement + "," + i + ",'" + kpiDetail.getKPIDrill(elements.get(i)) + "','" + kpiDetail.getKPIDrillRepType(elements.get(i)) + "')";
                    } else if (MySql) {
                        kpiGraphQuery = "INSERT INTO PRG_AR_KPIWITHGRAPH(DASHLET_ID,KPI_ID,GRAPH_ID,GRAPH_TYPE,VIEWBY,ELEMENT_IDS,GRAPH_MEASURE,SEQUENCE_NUM,Ref_Report_Id,Ref_Report_Type)"
                                + "VALUES((select LAST_INSERT_ID(DASHBOARD_DETAILS_ID) from PRG_AR_DASHBOARD_DETAILS order by 1 desc limit 1)," + dashlet.getKpiMasterId() + "," + graphId + ",'" + graphDetails.getGraphType() + "'," + viewBy + ",'" + elements.get(i) + "'," + basicElement + "," + i + ",'" + kpiDetail.getKPIDrill(elements.get(i)) + "','" + kpiDetail.getKPIDrillRepType(elements.get(i)) + "')";
                    } else {
                        kpiGraphQuery = "INSERT INTO PRG_AR_KPIWITHGRAPH(DASHLET_ID,KPI_ID,GRAPH_ID,GRAPH_TYPE,VIEWBY,ELEMENT_IDS,GRAPH_MEASURE,SEQUENCE_NUM,Ref_Report_Id,Ref_Report_Type)"
                                + "VALUES(PRG_AR_DASHBOARD_DETS_SEQ.currval," + dashlet.getKpiMasterId() + "," + graphId + ",'" + graphDetails.getGraphType() + "'," + viewBy + ",'" + elements.get(i) + "'," + basicElement + "," + i + ",'" + kpiDetail.getKPIDrill(elements.get(i)) + "','" + kpiDetail.getKPIDrillRepType(elements.get(i)) + "')";

                    }
                    queries.add(kpiGraphQuery);
                }
            }

        }

        return queries;
    }

    public List<String> getKPIInsertQueries(String dashboardId, String dashboardName, int dispSeq, DashletDetail dashlet, boolean sqlServer, boolean MySql, int row, int col, int rowSpan, int colSpan, ArrayList viewby) {
        List<String> queries = new ArrayList<String>();
        HashMap<String, Double> manuvaltarget = new HashMap<String, Double>();//added by sruthi for manuval bud based on date
        Double targetVal = null;
        KPI kpiDetail = (KPI) dashlet.getReportDetails();
        String kpiType = dashlet.getKpiType();
        String kpiDashName = dashlet.getkpiName();
        String kpiSymbol = dashlet.getKpiSymbol();
        ArrayList<String> kpiheads = dashlet.getKpiheads();
        StringBuilder kpiheadsbuilder = new StringBuilder();
        String kpiheadsstr = "";
        for (int i = 0; i < kpiheads.size(); i++) {
            kpiheadsbuilder.append(",").append(kpiheads.get(i));
        }
        if (0 < kpiheads.size()) {
            kpiheadsstr = kpiheadsbuilder.toString();
            kpiheadsstr = kpiheadsstr.substring(1);
        }
        List<String> elementids = kpiDetail.getElementIds();
        for (String elmntId : elementids) {
            if (dashlet.getSingleGroupHelpers() != null && !dashlet.getSingleGroupHelpers().isEmpty()) {
                List<KPISingleGroupHelper> kPISingleGroupHelpers = dashlet.getSingleGroupHelpers();
                for (KPISingleGroupHelper groupingHelper : kPISingleGroupHelpers) {
                    if (groupingHelper.getGroupName() != null) {
                        if (groupingHelper.getGroupName().equalsIgnoreCase(elmntId)) {
//                            String drillVal= groupingHelper.getGroupKPIDrill(elmntId);
                            String drillVal1 = kpiDetail.getKPIDrill(elmntId);
                            groupingHelper.addGroupKPIDrill(elmntId, drillVal1);
                        }
                    }
                }
            }
        }
        int kpiMasterId = -1;

        try {
            if (sqlServer) {
                String kpiMasterSeq = "insert into prg_kpi_master(kpiname) values('" + kpiType + "')";
                PbDb pbDb = new PbDb();
                kpiMasterId = pbDb.insertAndGetSequenceInSQLSERVER(kpiMasterSeq, "prg_kpi_master");
            } else if (MySql) {
                String kpiMasterSeq = "insert into prg_kpi_master(kpi_name) values('" + kpiType + "')";
                PbDb pbDb = new PbDb();
                kpiMasterId = pbDb.insertAndGetSequenceInMySql(kpiMasterSeq, "prg_kpi_master", "KPI_ID");
            } else {
                String kpiMasterSeq = "select PRG_AR_KPI_MASTER_SEQ.nextval from dual";
                PbDb pbDb = new PbDb();
                kpiMasterId = pbDb.getSequenceNumber(kpiMasterSeq);
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        //added by sruthi for manuval bud based on date
        String numberofdays = dashlet.getNumberOfDays();
        HashMap<String, Double> targetdata = dashlet.getTargetMauval();
        for (String ids : elementids) {
            if (targetdata != null && !targetdata.isEmpty()) {
                targetVal = targetdata.get(ids);
            }
            // if(targetVal!=null)
            manuvaltarget.put(ids, targetVal);
        }
        //ended by sruthi
        Gson gson = new Gson();
        String kpiGroupString = "";
        if (dashlet.getSingleGroupHelpers() != null) {
            List<KPISingleGroupHelper> kPISingleGroupHelpers = dashlet.getSingleGroupHelpers();

            if (!kPISingleGroupHelpers.isEmpty()) {
                kpiGroupString = gson.toJson(kPISingleGroupHelpers);
            }
        }
//         Gson gson=new Gson();
//        String kpiGroupString=gson.toJson(kpiGroupHelHashMap.get(dashlet.getKpiMasterId()));
        ArrayList<String> hidecolumns = new ArrayList<String>();
        hidecolumns = dashlet.getHidecolumns();//added by sruthi for hidecolumns
        if (kpiMasterId != -1) {
            //Inserting into dashboard details table. This can be moved to the caller function. Common function for all the dashlet types
            if (sqlServer || MySql) {
                String kpiQuery = "insert into PRG_AR_DASHBOARD_DETAILS (DASHBOARD_ID,REF_REPORT_ID,GRAPH_ID,KPI_MASTER_ID,DISPLAY_SEQUENCE,"
                        + "DISPLAY_TYPE,KPI_HEADING,KPI_TYPE,ROW_1,COL_1,ROW_SPAN,COL_SPAN,KPI_NAME,KPI_SYMBOL,DASHLET_KPIANDTABLE_PROP,KPI_HEADS,DEFAULT_VIEWBY,Hidecolumns,NO_DAYS,TARGET_MANUAL) values (" + dashboardId + ",0,0," + kpiMasterId + "," + dispSeq
                        + ",'KPI','" + dashboardName + " KPI','" + kpiType + "','" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "','" + kpiDashName + "','" + kpiSymbol + "','" + kpiGroupString + "','" + kpiheadsstr + "','" + viewby + "','" + hidecolumns + "','" + numberofdays + "','" + manuvaltarget + "')";

                queries.add(kpiQuery);
            } else {
                String kpiQuery = "insert into PRG_AR_DASHBOARD_DETAILS (DASHBOARD_DETAILS_ID,DASHBOARD_ID,REF_REPORT_ID,GRAPH_ID,KPI_MASTER_ID,"
                        + "DISPLAY_SEQUENCE,DISPLAY_TYPE,KPI_HEADING,KPI_TYPE,ROW_1,COL_1,ROW_SPAN,COL_SPAN,KPI_NAME,KPI_SYMBOL,KPI_HEADS,DEFAULT_VIEWBY,Hidecolumns,NO_DAYS,TARGET_MANUAL) values (PRG_AR_DASHBOARD_DETS_SEQ.nextval," + dashboardId
                        + ",0,0," + kpiMasterId + "," + dispSeq + ",'KPI','" + dashboardName + " KPI','" + kpiType
                        + "','" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "','" + kpiDashName + "','" + kpiSymbol + "','" + kpiheadsstr + "','" + viewby + "','" + hidecolumns + "','" + numberofdays + "','" + manuvaltarget + "')";

                queries.add(kpiQuery);

                DashboardViewerDAO dao = new DashboardViewerDAO();
                String queryStr = dao.prepareQueries(dashboardId, kpiMasterId, kpiGroupString);
                dashlet.setUpdateClobQry(queryStr);
            }



            List<String> kpiElementIds = kpiDetail.getElementIds();
            int sequence = 1;
            int increment = 0;
            boolean isGrpElement = false;
            for (String elementId : kpiElementIds) {
                List<KPIElement> kpiElements = kpiDetail.getKPIElements(elementId);
                KPIElement currKPIElement = null;
                for (KPIElement element : kpiElements) {
                    if (element.getElementId().equalsIgnoreCase(elementId)) {
                        currKPIElement = element;
                        isGrpElement = element.isIsGroupElement();
                        if (isGrpElement) {
                            elementId = null;
                        }
                        break;
                    }
                }
                if (!kpiType.equalsIgnoreCase("Complexkpi")) {
                    if (currKPIElement != null && currKPIElement.getElementName() != null) {
                        String kpiName = currKPIElement.getElementName();
                        String aggType = currKPIElement.getAggregationType();
                        //change by keerthi on sep-9-2011
                        String kpiStType = "Standard";
                        if (!isGrpElement) {
                            if (!(dashlet.getKpiType().equalsIgnoreCase("BasicTarget"))) {
                                if (!kpiDetail.getKpiStTypeHashMap().isEmpty() && kpiType.equalsIgnoreCase("BasicTarget")) {

                                    HashMap<String, String> KpiStTypeHashMap = kpiDetail.getKpiStTypeHashMap();

                                    Set<String> keyset = (Set<String>) KpiStTypeHashMap.keySet();
                                    Object[] obj = keyset.toArray();
                                    kpiStType = (String) KpiStTypeHashMap.get(obj[increment]);
                                    increment++;
                                }
                            }
                        }//end change
                        String drillVal = "0";
                        String drillRepType = "0";
                        String drillViewBys = "";
                        if (kpiDetail.getKPIDrill(elementId) != null) {
                            drillVal = kpiDetail.getKPIDrill(elementId);
                            drillRepType = kpiDetail.getKPIDrillRepType(elementId);
                            if (drillRepType == null) {
                                drillRepType = "0";
                            }
                            if (!drillVal.trim().equalsIgnoreCase("")) {
                                if (kpiDetail.getDrillViewBys() != null && !kpiDetail.getDrillViewBys().equalsIgnoreCase("") && !kpiDetail.getDrillViewBys().equalsIgnoreCase("(null)")) {
                                    drillViewBys = kpiDetail.getDrillViewBys();
                                } else {
                                    drillViewBys = "";
                                }
                            }
                        }
                        if (drillVal.trim().equalsIgnoreCase("")) {
                            drillVal = "0";
                        }
                        if (drillRepType.trim().equalsIgnoreCase("")) {
                            drillRepType = "0";
                        }
                        //Inserting into kpi details table
                        String kpiDetailsQuery = "";
                        if (sqlServer || MySql) {
                            kpiDetailsQuery = "insert into PRG_AR_KPI_DETAILS(KPI_MASTER_ID,KPI_NAME,ELEMENT_ID,REF_REPORT_ID,KPI_SEQ,AGGREGATION,InsightViewStatus,CommentViewStatus,GraphViewStatus,KPI_ST_TYPE,MtdViewStatus,QtdViewStatus,YtdViewStatus,CurrentViewStatus,DRILL_VIEW_BYS,TARGET_ELEMENT,DRILL_TYPE) "
                                    + "values (" + kpiMasterId + ",'" + kpiName + "'," + elementId + "," + drillVal + "," + sequence + ",'"
                                    + aggType + "','" + kpiDetail.isShowInsights() + "','" + kpiDetail.isShowComments() + "','" + kpiDetail.isShowGraphs() + "','" + kpiStType + "','" + kpiDetail.isMTDChecked() + "','" + kpiDetail.isQTDChecked() + "','" + kpiDetail.isYTDChecked() + "','" + kpiDetail.isCurrentChecked() + "','" + drillViewBys + "','" + kpiDetail.getKpiTragetMap(elementId) + "','" + drillRepType + "')";
                        } else {
                            kpiDetailsQuery = "insert into PRG_AR_KPI_DETAILS(KPI_DETAILS_ID,KPI_MASTER_ID,KPI_NAME,ELEMENT_ID,REF_REPORT_ID,KPI_SEQ,AGGREGATION,InsightViewStatus,CommentViewStatus,GraphViewStatus,KPI_ST_TYPE,MtdViewStatus,QtdViewStatus,YtdViewStatus,CurrentViewStatus,DRILL_VIEW_BYS,TARGET_ELEMENT,DRILL_TYPE) "
                                    + "values (PRG_AR_KPI_DETS_SEQ.nextval," + kpiMasterId + ",'" + kpiName + "'," + elementId + "," + drillVal
                                    + "," + sequence + ",'" + aggType + "','" + kpiDetail.isShowInsights() + "','" + kpiDetail.isShowComments() + "','" + kpiDetail.isShowGraphs() + "','" + kpiStType + "','" + kpiDetail.isMTDChecked() + "','" + kpiDetail.isQTDChecked() + "','" + kpiDetail.isYTDChecked() + "','" + kpiDetail.isCurrentChecked() + "','" + drillViewBys + "','" + kpiDetail.getKpiTragetMap(elementId) + "','" + drillRepType + "')";
                        }
                        queries.add(kpiDetailsQuery);
                        sequence++;

                        //Inserting the kpi comments
                        List<KPIComment> kpiComments = kpiDetail.getKPIComments(elementId);
                        if (kpiComments != null) {
                            for (KPIComment kpiComment : kpiComments) {
                                String comment = kpiComment.getComment();
                                String userId = kpiComment.getUserId();
                                String commentsQuery = "";

                                if (sqlServer) {
                                    commentsQuery = "insert into prg_kpi_user_comments(USER_ID,KPI_MASTER_ID,ELEMENT_ID,KPI_COMMENT,COMMENT_DATE) "
                                            + "values('" + userId + "'," + kpiMasterId + ",'" + elementId + "','" + comment + "',GETDATE())";
                                } else if (MySql) {
                                    commentsQuery = "insert into prg_kpi_user_comments(USER_ID,KPI_MASTER_ID,ELEMENT_ID,KPI_COMMENT,COMMENT_DATE) "
                                            + "values('" + userId + "'," + kpiMasterId + ",'" + elementId + "','" + comment + "',NOW())";
                                } else {
                                    commentsQuery = "insert into prg_kpi_user_comments(COMMENT_ID,USER_ID,KPI_MASTER_ID,ELEMENT_ID,KPI_COMMENT,COMMENT_DATE) "
                                            + "values(KPI_COMMENT_SEQ.nextval,'" + userId + "'," + kpiMasterId + ",'" + elementId
                                            + "','" + comment + "',sysdate)";
                                }
                                queries.add(commentsQuery);
                            }
                        }

                        //Inserting the kpi targets
                        List<KPITarget> kpiTargets = kpiDetail.getKPITargets(elementId);
                        if (kpiTargets != null) {
                            for (KPITarget kpiTarget : kpiTargets) {
                                String timeLevel = kpiTarget.getTimeLevel();
                                double targetValue = kpiTarget.getTargetValue();

                                String targetQuery = "";
                                if (sqlServer || MySql) {
                                    targetQuery = "insert into dashboard_target_kpi_value(ELEMENT_ID,KPI_MASTER_ID,TIME_LEVEL,TARGET_VALUE,DASHBOARD_ID) "
                                            + "values(" + elementId + "," + kpiMasterId + ",'" + timeLevel + "'," + targetValue + ","
                                            + dashboardId + ")";
                                } else {
                                    targetQuery = "insert into dashboard_target_kpi_value(ELEMENT_ID,KPI_MASTER_ID,TIME_LEVEL,TARGET_VALUE,DASHBOARD_ID) "
                                            + "values(" + elementId + "," + kpiMasterId + ",'" + timeLevel + "'," + targetValue + ","
                                            + dashboardId + ")";
                                }
                                queries.add(targetQuery);
                            }
                        }

                        //Updating kpi details table with the custom color values
                        List<KPIColorRange> kpiColorRanges = kpiDetail.getKPIColorRanges(elementId);
                        if (kpiColorRanges != null) {
                            String hrOperator = "";
                            double hr1 = 0;
                            double hr2 = 0;
                            String mrOperator = "";
                            double mr1 = 0;
                            double mr2 = 0;
                            String lrOperator = "";
                            double lr1 = 0;
                            double lr2 = 0;

                            for (KPIColorRange colorRange : kpiColorRanges) {
                                String color = colorRange.getColor();
                                if ("Green".equalsIgnoreCase(color)) {
                                    hrOperator = colorRange.getOperator();
                                    hr1 = colorRange.getRangeStartValue();
                                    hr2 = colorRange.getRangeEndValue();
                                } else if ("Yellow".equalsIgnoreCase(color)) {
                                    mrOperator = colorRange.getOperator();
                                    mr1 = colorRange.getRangeStartValue();
                                    mr2 = colorRange.getRangeEndValue();
                                }
                                if ("Red".equalsIgnoreCase(color)) {
                                    lrOperator = colorRange.getOperator();
                                    lr1 = colorRange.getRangeStartValue();
                                    lr2 = colorRange.getRangeEndValue();
                                }
                            }

                            String updateKPIDetsQry = "update prg_ar_kpi_details set KPI_HRANGE_TYPE='" + hrOperator + "',KPI_HRANGE1='" + hr1 + "',KPI_HRANGE2='" + hr2 + "'"
                                    + ",KPI_LRANGE_TYPE='" + lrOperator + "',KPI_LRANGE1='" + lr1 + "',KPI_LRANGE2='" + lr2 + "',KPI_MRANGE_TYPE='" + mrOperator + "',KPI_MRANGE1='" + mr1 + "'"
                                    + ",KPI_MRANGE2='" + mr2 + "' where element_id=" + elementId + " and kpi_master_id=" + kpiMasterId;

                            queries.add(updateKPIDetsQry);
                        }
                    }
                } //code for complex Kpi
                else {
                    String kpiDetailsQuery = "";
                    if (sqlServer || MySql) {
                        kpiDetailsQuery = "insert into PRG_AR_KPI_DETAILS(KPI_MASTER_ID,KPI_NAME,ELEMENT_ID,REF_REPORT_ID,KPI_SEQ,AGGREGATION,InsightViewStatus,CommentViewStatus,GraphViewStatus,KPI_ST_TYPE,MtdViewStatus,QtdViewStatus ,YtdViewStatus,CurrentViewStatus) "
                                + "values (" + kpiMasterId + ",'" + kpiDetail.getelementNames(elementId) + "'," + elementId + "," + null + "," + sequence + ",'"
                                + null + "','" + kpiDetail.isShowInsights() + "','" + kpiDetail.isShowComments() + "','" + kpiDetail.isShowGraphs() + "',null,'" + kpiDetail.isMTDChecked() + "','" + kpiDetail.isQTDChecked() + "','" + kpiDetail.isYTDChecked() + "','" + kpiDetail.isCurrentChecked() + "')";
                    } else {
                        kpiDetailsQuery = "insert into PRG_AR_KPI_DETAILS(KPI_DETAILS_ID,KPI_MASTER_ID,KPI_NAME,ELEMENT_ID,REF_REPORT_ID,KPI_SEQ,AGGREGATION,InsightViewStatus,CommentViewStatus,GraphViewStatus,KPI_ST_TYPE,MtdViewStatus,QtdViewStatus ,YtdViewStatus,CurrentViewStatus) "
                                + "values (PRG_AR_KPI_DETS_SEQ.nextval," + kpiMasterId + ",'" + kpiDetail.getelementNames(elementId) + "'," + elementId + "," + null
                                + "," + sequence + ",'" + null + "','" + kpiDetail.isShowInsights() + "','" + kpiDetail.isShowComments() + "','" + kpiDetail.isShowGraphs() + "',null,'" + kpiDetail.isMTDChecked() + "','" + kpiDetail.isQTDChecked() + "','" + kpiDetail.isYTDChecked() + "','" + kpiDetail.isCurrentChecked() + "')";
                    }
                    queries.add(kpiDetailsQuery);

                }
                //end of If
            }
        }
        return queries;
    }

    public List<String> getMapInsertQueries(String dashboardId, String dashboardName, int dispSeq, DashletDetail dashlet, boolean sqlServer, boolean MySql, int row, int col, int rowSpan, int colSpan) {
        MapDetail mapDetail = (MapDetail) dashlet.getReportDetails();
        List<String> queries = new ArrayList<String>();
        String insertMapQry;
        String dashletName = dashlet.getDashletName();
        if (sqlServer || MySql) {
            String mapQuery = "insert into PRG_AR_DASHBOARD_DETAILS (DASHBOARD_ID, REF_REPORT_ID, GRAPH_ID, KPI_MASTER_ID, "
                    + "DISPLAY_SEQUENCE, DISPLAY_TYPE, KPI_HEADING,ROW_1,COL_1,ROW_SPAN,COL_SPAN)  values (" + dashboardId + "," + dashboardId + ",0, 0," + dispSeq
                    + ",'MAP','" + dashletName + "','" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "')";
            queries.add(mapQuery);
        } else {
            String mapQuery = "insert into PRG_AR_DASHBOARD_DETAILS (DASHBOARD_DETAILS_ID, DASHBOARD_ID, REF_REPORT_ID, "
                    + "GRAPH_ID, KPI_MASTER_ID, DISPLAY_SEQUENCE, DISPLAY_TYPE, KPI_HEADING,ROW_1,COL_1,ROW_SPAN,COL_SPAN)  values (PRG_AR_DASHBOARD_DETS_SEQ.nextval,"
                    + dashboardId + "," + dashboardId + ",0, 0," + dispSeq + ",'MAP','" + dashletName + "','" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "')";
            queries.add(mapQuery);
        }
        StringBuilder mainMeasSB = new StringBuilder();
        List<String> mainMeasure = mapDetail.getPrimaryMeasure();
        for (String meas : mainMeasure) {
            if (meas.startsWith("A_")) {
                meas = meas.replace("A_", "");
            }
            mainMeasSB.append(",").append(meas);
        }
        mainMeasSB.replace(0, 1, "");
//        if (mainMeasure.startsWith("A_")) {
//            mainMeasure = mainMeasure.replace("A_", "");
//        }
        List<String> suppMeasures = mapDetail.getSupportingMeasures();
        StringBuilder suppMeasSB = new StringBuilder();
        if (suppMeasures != null) {
            for (String meas : suppMeasures) {
                if (meas.startsWith("A_")) {
                    meas = meas.replace("A_", "");
                }
                suppMeasSB.append(",").append(meas);
            }
            if (suppMeasSB.length() > 0) {
                suppMeasSB.replace(0, 1, "");
            }
        }

        if (sqlServer) {
            insertMapQry = "insert into prg_ar_report_map_details values(" + dashboardId + ",'" + mainMeasSB.toString() + "','" + suppMeasSB.toString() + "')";
            queries.add(insertMapQry);
        }
        if (MySql) {
            int seqOfDetailId = 0;

            try {
                PbReturnObject pbro = new PbReturnObject();
                PbDb pbdb = new PbDb();
                String queryForSeq = "select LAST_INSERT_ID(DETAIL_ID) from PRG_AR_REPORT_MAP_DETAILS order by 1 desc limit 1";
                pbro = pbdb.executeSelectSQL(queryForSeq);
                seqOfDetailId = pbro.getFieldValueInt(0, 0);
                seqOfDetailId = seqOfDetailId + 1;
            } catch (SQLException se) {
            }
            insertMapQry = "insert into prg_ar_report_map_details values('" + seqOfDetailId + "'," + dashboardId + ",'" + mainMeasSB.toString() + "','" + suppMeasSB.toString() + "')";
            queries.add(insertMapQry);
        } else {
            insertMapQry = "insert into prg_ar_report_map_details values(prg_ar_report_map_det_id_seq.nextval," + dashboardId + ",'" + mainMeasSB.toString() + "','" + suppMeasSB.toString() + "')";
            queries.add(insertMapQry);
        }
//        StringBuilder measVals = new StringBuilder();
//        measVals.append(mainMeasure);
//        if(suppMeasures!=null){
//            for (String measId:suppMeasures){
//                if (measId.startsWith("A_")){
//                    measId = measId.replace("A_", "");
//                }
//                measVals.append(",").append(measId);
//            }
//        }

        List<QueryDetail> mapQryDetails = mapDetail.getQueryDetails();
        queries.addAll(getQueryDetailsInsertQueries(dashboardId, mapQryDetails, sqlServer, MySql));

        return queries;
    }

    public List<String> getQueryDetailsInsertQueries(String dashboardId, List<QueryDetail> queryDetails, boolean sqlServer, boolean MySql) {
        List<String> queries = new ArrayList<String>();
        Set<QueryDetail> qdSet = new HashSet<QueryDetail>();
        if (queryDetails != null) {
            qdSet.addAll(queryDetails);
        }

        if (!qdSet.isEmpty()) {
            Iterator<QueryDetail> iter = qdSet.iterator();
            int j = 0;
            while (iter.hasNext()) {
                QueryDetail qd = iter.next();
                String insertStr;
                if (sqlServer || MySql) {
                    insertStr = "INSERT INTO  PRG_AR_QUERY_DETAIL( COL_SEQ, COL_DISP_NAME, ELEMENT_ID, REF_ELEMENT_ID, FOLDER_ID, SUB_FOLDER_ID, REPORT_ID, AGGREGATION_TYPE, COLUMN_TYPE) "
                            + " values(" + (j + 1) + ",'" + qd.getDisplayName() + "'," + qd.getElementId() + "," + qd.getRefElementId() + ","
                            + "" + qd.getFolderId() + "," + qd.getSubFolderId() + "," + dashboardId + ",'" + qd.getAggregationType() + "','" + qd.getColumnType() + "')";
                } else {
                    insertStr = "INSERT INTO  PRG_AR_QUERY_DETAIL(QRY_COL_ID, COL_SEQ, COL_DISP_NAME, ELEMENT_ID, REF_ELEMENT_ID, FOLDER_ID, SUB_FOLDER_ID, REPORT_ID, AGGREGATION_TYPE, COLUMN_TYPE) "
                            + " values(PRG_AR_QUERY_DETAIL_SEQ.nextval," + (j + 1) + ",'" + qd.getDisplayName() + "'," + qd.getElementId() + "," + qd.getRefElementId() + ","
                            + "" + qd.getFolderId() + "," + qd.getSubFolderId() + "," + dashboardId + ",'" + qd.getAggregationType() + "','" + qd.getColumnType() + "')";
                }
                j++;
                queries.add(insertStr);
            }
        }
        return queries;
    }

    public List<String> getKPIGraphInsertQueries(String dashboardId, String dashboardName, int dispSeq, DashletDetail dashlet, boolean sqlServer, boolean MySql, int row, int col, int rowSpan, int colSpan) {
        List<String> queries = new ArrayList<String>();
        KPIGraph kpiGrpDetail = (KPIGraph) dashlet.getReportDetails();
        PbDb pbdb = new PbDb();
        String KPIGrpDetSeqQry = "";
        PbReturnObject seqObj = null;
        Connection con;
        String dbrdGrpQuery = "";
        String KPIGrpQry = "";
        String KPIColorQry = "";
        String jqgraphtype = "";
        PreparedStatement opstmt = null;
        ArrayList queryList = new ArrayList();
        if (dashlet.getGraphtype() != null) {
            jqgraphtype = dashlet.getGraphtype();

        }
        try {
            if (sqlServer) {

                dbrdGrpQuery = "insert into PRG_AR_DASHBOARD_DETAILS (DASHBOARD_ID,REF_REPORT_ID,GRAPH_ID,KPI_MASTER_ID,DISPLAY_SEQUENCE,"
                        + "DISPLAY_TYPE,KPI_HEADING,ROW_1,COL_1,ROW_SPAN,COL_SPAN) values (" + dashboardId + ",0,  IDENT_CURRENT('PRG_AR_KPI_GRAPH_DETAILS') ,0," + dispSeq + ",'KPIGraph','" + dashboardName + " KPI','" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "')";
                queryList.add(dbrdGrpQuery);

            }
            if (MySql) {

                dbrdGrpQuery = "insert into PRG_AR_DASHBOARD_DETAILS (DASHBOARD_ID,REF_REPORT_ID,GRAPH_ID,KPI_MASTER_ID,DISPLAY_SEQUENCE,"
                        + "DISPLAY_TYPE,KPI_HEADING,ROW_1,COL_1,ROW_SPAN,COL_SPAN) values (" + dashboardId + ",0,  (select LAST_INSERT_ID(kpi_grp_id) from PRG_AR_KPI_GRAPH_DETAILS order by 1 desc limit 1 ),0," + dispSeq + ",'KPIGraph','" + dashboardName + " KPI','" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "')";
                queryList.add(dbrdGrpQuery);

            } else {
                KPIGrpDetSeqQry = "SELECT PR_AR_KPI_GRAPH_DET_SEQ.nextval FROM DUAL";
                seqObj = pbdb.execSelectSQL(KPIGrpDetSeqQry);
                dashlet.setGraphId(seqObj.getFieldValueString(0, 0));
                dbrdGrpQuery = "insert into PRG_AR_DASHBOARD_DETAILS (DASHBOARD_DETAILS_ID,DASHBOARD_ID,REF_REPORT_ID,GRAPH_ID,KPI_MASTER_ID,DISPLAY_SEQUENCE,"
                        + "DISPLAY_TYPE,KPI_HEADING,ROW_1,COL_1,ROW_SPAN,COL_SPAN) values (PRG_AR_DASHBOARD_DETS_SEQ.nextval," + dashboardId + ",0," + seqObj.getFieldValueString(0, 0) + ",0," + dispSeq + ",'KPIGraph','" + dashboardName + " KPI','" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "')";
                pbdb.execModifySQL(dbrdGrpQuery);
            }
            if (kpiGrpDetail.getKpigrname() == null) {
                kpiGrpDetail.setKpigrname(kpiGrpDetail.getKpiName());
            }
            con = ProgenConnection.getInstance().getConnection();

            if (sqlServer) {
                KPIGrpQry = "INSERT INTO PRG_AR_KPI_GRAPH_DETAILS(ELEMENT_ID, DASHBOARD_ID, NEEDLE, KPINAME,KPIGRAPHTYPE,DASHBOARDDET_ID,GRAPH_XML,KPI_GRP_SIZE,GRAPH_HEIGHT,GRAPH_WIDTH,KPI_DASHLET_NAME) values(" + kpiGrpDetail.getElementId() + "," + dashboardId + "," + kpiGrpDetail.getNeedle() + ",'" + kpiGrpDetail.getKpiName() + "','" + kpiGrpDetail.getKpiGraphType() + "',ident_current('PRG_AR_DASHBOARD_DETAILS'),'" + kpiGrpDetail.getGraphXML() + "',1," + kpiGrpDetail.getGraphHeight() + "," + kpiGrpDetail.getGraphWidth() + ",'" + kpiGrpDetail.getKpigrname() + "')";
                queryList.add(KPIGrpQry);
                queryList.add("UPDATE PRG_AR_DASHBOARD_DETAILS SET GRAPH_ID=IDENT_CURRENT('PRG_AR_KPI_GRAPH_DETAILS') WHERE DASHBOARD_DETAILS_ID=IDENT_CURRENT('PRG_AR_DASHBOARD_DETAILS')");
                pbdb.executeMultiple(queryList);
//                        KPIGrpDetSeqQry = "select IDENT_CURRENT('PRG_AR_DASHBOARD_DETAILS')";
//                  seqObj = pbdb.execSelectSQL(KPIGrpDetSeqQry);
//                  dashlet.setGraphId(seqObj.getFieldValueString(0,0));

//                      opstmt = con.prepareStatement(KPIGrpQry);
//                      opstmt.setString(1, kpiGrpDetail.getElementId());
//                      opstmt.setString(2, dashboardId);
////                      opstmt.setString(3, Double.toString(kpiGrpDetail.getStartRange()));
////                      opstmt.setString(4, Double.toString(kpiGrpDetail.getEndRange()));
////                      opstmt.setString(5, Double.toString(kpiGrpDetail.getFsplit()));
////                      opstmt.setString(6, Double.toString(kpiGrpDetail.getSsplit()));
//                      opstmt.setString(3, Double.toString(kpiGrpDetail.getNeedle()));
//                      opstmt.setString(4, kpiGrpDetail.getKpiName());
//                      opstmt.setString(5, kpiGrpDetail.getKpiGraphType());
//                      opstmt.setString(6, kpiGrpDetail.getGraphXML());
//                      int rows = opstmt.executeUpdate();

                KPIColorQry = "INSERT INTO PRG_AR_KPI_GRAPH_RANGE_DETAILS(KPI_GRAPH_ID,RISK,OPERATORS,START_VAL,END_VAL) values(IDENT_CURRENT('PRG_AR_KPI_GRAPH_DETAILS'),?,?,?,?)";
                opstmt = con.prepareStatement(KPIColorQry);
                HashMap<String, KPIColorRange> kpiColorRangeMap = kpiGrpDetail.getKpiGrphColorRangeHashMap();
                Set keySet = kpiColorRangeMap.keySet();
                Iterator<String> iter = keySet.iterator();
                while (iter.hasNext()) {
                    String risk = iter.next();
                    KPIColorRange kpiColorRange = kpiColorRangeMap.get(risk);
                    opstmt.setString(1, kpiColorRange.getColor());
                    opstmt.setString(2, kpiColorRange.getOperator());
                    opstmt.setString(3, Double.toString(kpiColorRange.getRangeStartValue()));
                    opstmt.setString(4, Double.toString(kpiColorRange.getRangeEndValue()));
                    int rows = opstmt.executeUpdate();
                }
                con.close();



            }
            if (MySql) {
                KPIGrpQry = "INSERT INTO PRG_AR_KPI_GRAPH_DETAILS(ELEMENT_ID, DASHBOARD_ID, NEEDLE, KPINAME,KPIGRAPHTYPE,DASHBOARDDET_ID,GRAPH_XML,KPI_GRP_SIZE,GRAPH_HEIGHT,GRAPH_WIDTH,KPI_DASHLET_NAME) values(" + kpiGrpDetail.getElementId() + "," + dashboardId + "," + kpiGrpDetail.getNeedle() + ",'" + kpiGrpDetail.getKpiName() + "','" + kpiGrpDetail.getKpiGraphType() + "',(select LAST_INSERT_ID(DASHBOARD_DETAILS_ID) from PRG_AR_DASHBOARD_DETAILS order by 1 desc limit 1),'" + kpiGrpDetail.getGraphXML() + "',1," + kpiGrpDetail.getGraphHeight() + "," + kpiGrpDetail.getGraphWidth() + ",'" + kpiGrpDetail.getKpigrname() + "')";
                queryList.add(KPIGrpQry);
                queryList.add("UPDATE PRG_AR_DASHBOARD_DETAILS SET GRAPH_ID=(select LAST_INSERT_ID(KPI_GRP_ID) from PRG_AR_KPI_GRAPH_DETAILS order by 1 desc limit 1) WHERE DASHBOARD_DETAILS_ID=(select LAST_INSERT_ID(KPI_GRP_ID) from PRG_AR_KPI_GRAPH_DETAILS order by 1 desc limit 1)");
                pbdb.executeMultiple(queryList);
                KPIColorQry = "INSERT INTO PRG_AR_KPI_GRAPH_RANGE_DETAILS(KPI_GRAPH_ID,RISK,OPERATORS,START_VAL,END_VAL) values((select LAST_INSERT_ID(KPI_GRP_ID) from PRG_AR_KPI_GRAPH_DETAILS order by 1 desc limit 1),?,?,?,?)";
                opstmt = con.prepareStatement(KPIColorQry);
                HashMap<String, KPIColorRange> kpiColorRangeMap = kpiGrpDetail.getKpiGrphColorRangeHashMap();
                Set keySet = kpiColorRangeMap.keySet();
                Iterator<String> iter = keySet.iterator();
                while (iter.hasNext()) {
                    String risk = iter.next();
                    KPIColorRange kpiColorRange = kpiColorRangeMap.get(risk);
                    opstmt.setString(1, kpiColorRange.getColor());
                    opstmt.setString(2, kpiColorRange.getOperator());
                    opstmt.setString(3, Double.toString(kpiColorRange.getRangeStartValue()));
                    opstmt.setString(4, Double.toString(kpiColorRange.getRangeEndValue()));
                    int rows = opstmt.executeUpdate();
                }
                con.close();
            } else {
                if (jqgraphtype.equalsIgnoreCase("metter")) {
                    KPIGrpQry = "INSERT INTO PRG_AR_KPI_GRAPH_DETAILS(KPI_GRP_ID, ELEMENT_ID, DASHBOARD_ID, NEEDLE, KPINAME,KPIGRAPHTYPE,DASHBOARDDET_ID,GRAPH_XML,KPI_GRP_SIZE,GRAPH_HEIGHT,GRAPH_WIDTH,KPI_DASHLET_NAME,JQ_PROPERTIES) values(" + seqObj.getFieldValueString(0, 0) + ",?,?,?,?,?,PRG_AR_DASHBOARD_DETS_SEQ.currval,?,1,?,?,?,?)";
                } else {
                    KPIGrpQry = "INSERT INTO PRG_AR_KPI_GRAPH_DETAILS(KPI_GRP_ID, ELEMENT_ID, DASHBOARD_ID, NEEDLE, KPINAME,KPIGRAPHTYPE,DASHBOARDDET_ID,GRAPH_XML,KPI_GRP_SIZE,GRAPH_HEIGHT,GRAPH_WIDTH,KPI_DASHLET_NAME) values(" + seqObj.getFieldValueString(0, 0) + ",?,?,?,?,?,PRG_AR_DASHBOARD_DETS_SEQ.currval,?,1,?,?,?)";
                }

//                KPIGrpQry = "INSERT INTO PRG_AR_KPI_GRAPH_DETAILS(KPI_GRP_ID, ELEMENT_ID, DASHBOARD_ID, NEEDLE, KPINAME,KPIGRAPHTYPE,DASHBOARDDET_ID,GRAPH_XML,KPI_GRP_SIZE,GRAPH_HEIGHT,GRAPH_WIDTH,KPI_DASHLET_NAME) values(" + seqObj.getFieldValueString(0, 0) + ",?,?,?,?,?,PRG_AR_DASHBOARD_DETS_SEQ.currval,?,1,?,?,?)";
                opstmt = con.prepareStatement(KPIGrpQry);

                opstmt.setString(1, kpiGrpDetail.getElementId());
                opstmt.setString(2, dashboardId);
//                      opstmt.setString(3, Double.toString(kpiGrpDetail.getStartRange()));
//                      opstmt.setString(4, Double.toString(kpiGrpDetail.getEndRange()));
//                      opstmt.setString(5, Double.toString(kpiGrpDetail.getFsplit()));
//                      opstmt.setString(6, Double.toString(kpiGrpDetail.getSsplit()));
                opstmt.setString(3, Double.toString(kpiGrpDetail.getNeedle()));
                opstmt.setString(4, kpiGrpDetail.getKpiName());
                opstmt.setString(5, kpiGrpDetail.getKpiGraphType());
                ((OraclePreparedStatement) opstmt).setStringForClob(6, kpiGrpDetail.getGraphXML());
                opstmt.setString(7, Double.toString(kpiGrpDetail.getGraphHeight()));
                opstmt.setString(8, Double.toString(kpiGrpDetail.getGraphWidth()));
                opstmt.setString(9, kpiGrpDetail.getKpigrname());
                if (jqgraphtype.equalsIgnoreCase("metter")) {
                    opstmt.setString(10, jqgraphtype);
                }

                int rows = opstmt.executeUpdate();

                KPIColorQry = "INSERT INTO PRG_AR_KPI_GRAPH_RANGE_DETAILS(RANGE_ID,KPI_GRAPH_ID,RISK,OPERATORS,START_VAL,END_VAL) values(PRG_AR_KPI_GRAPH_RANGE_SEQ.nextVal," + seqObj.getFieldValueString(0, 0) + ",?,?,?,?)";
                opstmt = con.prepareStatement(KPIColorQry);
                HashMap<String, KPIColorRange> kpiColorRangeMap = kpiGrpDetail.getKpiGrphColorRangeHashMap();
                Set keySet = kpiColorRangeMap.keySet();
                Iterator<String> iter = keySet.iterator();
                while (iter.hasNext()) {

                    String risk = iter.next();
                    KPIColorRange kpiColorRange = kpiColorRangeMap.get(risk);
                    opstmt.setString(1, kpiColorRange.getColor());
                    opstmt.setString(2, kpiColorRange.getOperator());
                    opstmt.setString(3, Double.toString(kpiColorRange.getRangeStartValue()));
                    opstmt.setString(4, Double.toString(kpiColorRange.getRangeEndValue()));
                    rows = opstmt.executeUpdate();
                }
                con.close();
            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }


        return queries;
    }

    public ActionForward saveDashboard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String dbrdId = request.getParameter("dashboardId");
        String dashboardName = String.valueOf(request.getParameter("dashboardName"));
        String dashboardDesc = String.valueOf(request.getParameter("dashboardDesc"));
        createDashboard(request, dbrdId, dashboardName, dashboardDesc);

        ServletContext context = this.getServlet().getServletContext();
        boolean isCompanyValid = Boolean.parseBoolean(context.getInitParameter("isCompanyValid"));

        if (isCompanyValid) {
            return mapping.findForward("dashboardAssignmentWithCompany");
        } else {
            return mapping.findForward("dashboardAssignment");
        }
    }

    public ActionForward OverrideDashboard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String dbrdId = request.getParameter("dashboardId");




        createDashboard(request, dbrdId, null, null);

        deleteDatabaseData(dbrdId);

        ServletContext context = this.getServlet().getServletContext();
        boolean isCompanyValid = Boolean.parseBoolean(context.getInitParameter("isCompanyValid"));

        if (isCompanyValid) {
            return mapping.findForward("dashboardAssignmentWithCompany");
        } else {
            return mapping.findForward("dashboardAssignment");
        }
    }

    public ActionForward getKpiGraphTargets(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            try {
                XMLOutputter serializer = null;
                Document document = null;
                HashMap map = new HashMap();
                Container container = null;
                String customReportId = "";
                Double dayDeviation = 0.0;
                Double weekDeviation = 0.0;
                Double monthDeviation = 0.0;
                Double qtrDeviation = 0.0;
                Double yrDeviation = 0.0;
                double daytempDev = 0.0;
                double weektempDev = 0.0;
                double monthtempDev = 0.0;
                double qtrtempDev = 0.0;
                double yrtempDev = 0.0;
                float finalDev = 0.0f;
                ArrayList dayDeviateList = new ArrayList();
                ArrayList dayDeviateValList = new ArrayList();
                PrintWriter out = response.getWriter();
                DashboardTemplateDAO templateDAO = new DashboardTemplateDAO();

                String dayTarget = request.getParameter("day");
                String weekTarget = request.getParameter("week");
                String monthTarget = request.getParameter("month");
                String qtrTarget = request.getParameter("qtr");
                String yrTarget = request.getParameter("year");
                customReportId = request.getParameter("dashboardId");
                String kpiIds = request.getParameter("kpiIds");
                String dayDiff = request.getParameter("dayDiff");
                String timeDim = request.getParameter("timeDim");
                String targetType = request.getParameter("targetType");
                String measType = request.getParameter("measType");
                String basis = request.getParameter("basis");

                ////////
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(customReportId);

                Element root = new Element("kpiGraph");
                root.setAttribute("version", "1.00001");
                root.setText("New Root");

                Element Target = new Element("TargetValues");

                Element dayTargetValue = new Element("DayTarget");
                dayTargetValue.setText(dayTarget);
                Target.addContent(dayTargetValue);

                Element weekTargetValue = new Element("WeekTarget");
                weekTargetValue.setText(weekTarget);
                Target.addContent(weekTargetValue);

                Element monthTargetValue = new Element("MonthTarget");
                monthTargetValue.setText(monthTarget);
                Target.addContent(monthTargetValue);

                Element qtrTargetValue = new Element("QtrTarget");
                qtrTargetValue.setText(qtrTarget);
                Target.addContent(qtrTargetValue);

                Element yrTargetValue = new Element("YearTarget");
                yrTargetValue.setText(yrTarget);
                Target.addContent(yrTargetValue);

                Element MeasureType = new Element("MeasureType");
                MeasureType.setText(measType);

                Element Basis = new Element("Basis");
                Basis.setText(basis);

                //Element deviationValue = new Element("Deviation");
                //deviationValue.setText(String.valueOf(finalDev));
                //Target.addContent(deviationValue);

                root.addContent(Target);
                root.addContent(MeasureType);
                root.addContent(Basis);
                //root.addContent(deviationValue);
                document = new Document(root);
                serializer = new XMLOutputter();
                ////////

                // HashMap ParametersHashMap = container.getParametersHashMap();
                //added by k
                // ArrayList params = (ArrayList) hs.getAttribute("MyParameters");
                ArrayList TimeDetailsList = (ArrayList) session.getAttribute("MyTimeDetailstList");



                //ArrayList TimeDetailsList = (ArrayList) ParametersHashMap.get("TimeDetailstList");


                PbReturnObject retObj = getKpiGrpValue(kpiIds, session, customReportId);
                String result = String.valueOf(retObj.getFieldValueInt(0, 1));
                ////////////////////////////////.println("timedim in action is: "+timeDim);
                ////////////////////////////////.println("daytarget in action is: "+dayTarget);
                ////////////////////////////////.println("daydiff in action is: "+dayDiff);
                double actualValperDay = 0.0;
                if (timeDim.equalsIgnoreCase("PRG_DATE_RANGE")) {
                    if (!dayTarget.equals("0")) {
                        actualValperDay = Double.parseDouble(result) / Double.parseDouble(dayDiff);
                        finalDev = templateDAO.getDeviation(String.valueOf(actualValperDay), dayTarget);
                        ////////////////////////////////.println("actulavalperday in action is: "+actualValperDay);
                    } else {
                        dayDeviation = 0.0;
                    }
                    out.print(finalDev + "~" + serializer.outputString(document));
                } else {
                    if (String.valueOf(TimeDetailsList.get(0)).equalsIgnoreCase("DAY")) {
                        if (!dayTarget.equals("0")) {
                            //dayDeviation = (((double) ((Double.parseDouble(result) - Double.parseDouble(dayTarget))) / Double.parseDouble(dayTarget)) * 100);
                            //weekDeviation = (((double) ((Double.parseDouble(result) - Double.parseDouble(weekTarget))) / Double.parseDouble(weekTarget)) * 100);
                            //monthDeviation = (((double) ((Double.parseDouble(result) - Double.parseDouble(monthTarget))) / Double.parseDouble(monthTarget)) * 100);
                            //qtrDeviation = (((double) ((Double.parseDouble(result) - Double.parseDouble(qtrTarget))) / Double.parseDouble(qtrTarget)) * 100);
                            //yrDeviation = (((double) ((Double.parseDouble(result) - Double.parseDouble(yrTarget))) / Double.parseDouble(yrTarget)) * 100);

                            finalDev = templateDAO.getDeviation(result, monthTarget);

                        } else {
                            dayDeviation = 0.0;
                        }

                        out.print(finalDev + "~" + serializer.outputString(document) + "~" + actualValperDay);
                    }
                    if (String.valueOf(TimeDetailsList.get(0)).equalsIgnoreCase("WEEK")) {
                        if (!weekTarget.equals("0")) {
                            //weekDeviation = (((double) ((Double.parseDouble(result) - Double.parseDouble(weekTarget))) / Double.parseDouble(weekTarget)) * 100);
                            finalDev = templateDAO.getDeviation(result, weekTarget);
                        } else {
                            weekDeviation = 0.0;
                        }
                        out.print(finalDev + "~" + serializer.outputString(document));
                    }
                    if (String.valueOf(TimeDetailsList.get(0)).equalsIgnoreCase("MONTH")) {
                        if (!monthTarget.equals("0")) {
                            //monthDeviation = (((double) ((Double.parseDouble(result) - Double.parseDouble(monthTarget))) / Double.parseDouble(monthTarget)) * 100);
                            finalDev = templateDAO.getDeviation(result, monthTarget);
                        } else {
                            monthDeviation = 0.0;
                        }
                        out.print(finalDev + "~" + serializer.outputString(document));
                    }
                    if (String.valueOf(TimeDetailsList.get(0)).equalsIgnoreCase("QUARTER")) {
                        if (!qtrTarget.equals("0")) {
                            //qtrDeviation = (((double) ((Double.parseDouble(result) - Double.parseDouble(qtrTarget))) / Double.parseDouble(qtrTarget)) * 100);
                            finalDev = templateDAO.getDeviation(result, qtrTarget);
                        } else {
                            qtrDeviation = 0.0;
                        }
                        out.print(finalDev + "~" + serializer.outputString(document));
                    }
                    if (String.valueOf(TimeDetailsList.get(0)).equalsIgnoreCase("YEAR")) {
                        if (!yrTarget.equals("0")) {
                            //yrDeviation = (((double) ((Double.parseDouble(result) - Double.parseDouble(yrTarget))) / Double.parseDouble(yrTarget)) * 100);
                            finalDev = templateDAO.getDeviation(result, yrTarget);
                        } else {
                            yrDeviation = 0.0;
                        }
                        out.print(finalDev + "~" + serializer.outputString(document));
                    }
                }
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }
        return null;
    }

    public ActionForward getKPITargetDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String dashletId = request.getParameter("dashletId");
        HttpSession session = request.getSession(false);
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        String dashboardId = request.getParameter("dashboardId");
        Container container = Container.getContainerFromSession(request, dashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail detail = collect.getDashletDetail(dashletId);
        Report reportDetails = detail.getReportDetails();
        KPIGraph kpiGrp = (KPIGraph) reportDetails;
        HashMap<String, KPIColorRange> kpiColorRangeHashMap = kpiGrp.getKpiGrphColorRangeHashMap();
        StringBuilder operatorsSB = new StringBuilder();
        StringBuilder startValuesSB = new StringBuilder();
        StringBuilder endValuesSB = new StringBuilder();
        StringBuilder json = new StringBuilder();
        String risk[] = {"high", "medium", "low"};
        for (String key : risk) {
            KPIColorRange kpicolorRangeObj = kpiColorRangeHashMap.get(key);
            operatorsSB.append(",\"").append(kpicolorRangeObj.getOperator()).append("\"");
            startValuesSB.append(",\"").append(kpicolorRangeObj.getRangeStartValue()).append("\"");
            endValuesSB.append(",\"").append(kpicolorRangeObj.getRangeEndValue()).append("\"");
        }
        operatorsSB.replace(0, 1, "");
        startValuesSB.replace(0, 1, "");
        endValuesSB.replace(0, 1, "");
        json.append("{");
        json.append("operators:[").append(operatorsSB).append("]").append(",");
        json.append("startValue:[").append(startValuesSB).append("]").append(",");
        json.append("endValues:[").append(endValuesSB).append("]").append(",");
        json.append("needleValue:").append(kpiGrp.getNeedle()).append("}");
        session.setAttribute("kpiXml", kpiGrp.getGraphXML());
        out.print(json.toString());

        return null;
    }

    public ActionForward saveDbrdComments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String elementId = null;
        String kpiMasterId = null;
        String commentText = null;
        String dashBoardId = "";
        String userId = "";
        String dashletId = "";
        HashMap map = new HashMap();
        Container container = null;
        StringBuilder sb = new StringBuilder();
        String userName = "";
        DashboardTemplateDAO dbrdDAO = new DashboardTemplateDAO();
        if (session != null) {
            try {
                elementId = request.getParameter("elementId");
                kpiMasterId = request.getParameter("masterId");
                commentText = request.getParameter("commentText");
                dashBoardId = request.getParameter("REPORTID");
                dashletId = request.getParameter("dashletId");
                userId = (String) session.getAttribute("USERID");
                userName = dbrdDAO.getUserName(userId);
                sb.append(userName).append(" : ").append(commentText);

                if (commentText != null || !("".equalsIgnoreCase(commentText))) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    container = (Container) map.get(dashBoardId);

                    pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
                    DashletDetail dashlet = collect.getDashletDetail(dashletId);
                    KPI kpiDetails = (KPI) dashlet.getReportDetails();

                    KPIComment comment = new KPIComment();
                    comment.setCommentDate(new Date());
                    comment.setElementId(elementId);
                    comment.setUserId(userId);
                    comment.setComment(sb.toString());
                    kpiDetails.addKPIComments(elementId, comment);

                    if (kpiDetails.isPersisted()) {
                        dbrdDAO.updateDbrdComment(elementId, kpiMasterId, sb.toString(), userId);
                    }
                }
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward saveKPIDrill(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String reportId = request.getParameter("REPORT_ID");
        String dashletId = request.getParameter("DASHLET_ID");
        String kpiMasterId = request.getParameter("KPI_MASTER_ID");

        HttpSession session = request.getSession(false);
        HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
        Container container = (Container) map.get(reportId);

        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail dashlet = collect.getDashletDetail(dashletId);

        KPI kpiDetails = (KPI) dashlet.getReportDetails();
        List<String> elementIds = kpiDetails.getElementIds();
        Set<String> GroupNames = new HashSet<String>();
        if (dashlet.getSingleGroupHelpers() != null && !dashlet.getSingleGroupHelpers().isEmpty()) {
            List<KPISingleGroupHelper> kPISingleGroupHelpers = dashlet.getSingleGroupHelpers();
            for (KPISingleGroupHelper groupingHelper : kPISingleGroupHelpers) {
                if (groupingHelper.getGroupName() != null && !groupingHelper.getGroupName().isEmpty()) {
                    GroupNames.add(groupingHelper.getGroupName());
                }
            }
        }
        for (String elementId : elementIds) {
            String targetReportId = request.getParameter(elementId + "_report");
            String[] drilldets = new String[2];
            String drillid = "";
            String drilltype = "";
            if (targetReportId != null && targetReportId.contains("~")) {
                drilldets = targetReportId.split("~");
                drillid = drilldets[0];
                drilltype = drilldets[1];
            } else {
                drillid = targetReportId;
            }
            kpiDetails.addKPIDrill(elementId, drillid);
            kpiDetails.addKPIDrillRepType(elementId, drilltype);
            if (GroupNames.contains(elementId)) {
                if (dashlet.getSingleGroupHelpers() != null && !dashlet.getSingleGroupHelpers().isEmpty()) {
                    List<KPISingleGroupHelper> kPISingleGroupHelpers = dashlet.getSingleGroupHelpers();
                    for (KPISingleGroupHelper groupingHelper : kPISingleGroupHelpers) {
                        if (groupingHelper.getGroupName() != null && groupingHelper.getGroupName().equalsIgnoreCase(elementId)) {
                            groupingHelper.addGroupKPIDrill(elementId, drillid);
                            groupingHelper.addGroupKPIDrillType(elementId, drilltype);
                        }
                    }
                }
            }
        }

        return null;
    }

    public ActionForward delDbrdComments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String elementId = null;
        String kpiMasterId = null;
        String dashBoardId = "";
        String userId = "";

        if (session != null) {
            try {
                elementId = request.getParameter("elementId");
                kpiMasterId = request.getParameter("masterId");
                userId = request.getParameter("userId");
                dashBoardId = request.getParameter("REPORTID");

                HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
                Container container = (Container) map.get(dashBoardId);

                pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
                DashletDetail dashlet = collect.getDashletFromKPIMaster(kpiMasterId);
                KPI kpiDetails = (KPI) dashlet.getReportDetails();
                kpiDetails.clearKPIComments(elementId);

                DashboardTemplateDAO dbrdDAO = new DashboardTemplateDAO();
                dbrdDAO.deleteDbrdComment(elementId, kpiMasterId, userId);

            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward saveTargetForKpiTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String elementId = null;
        String kpiMasterId = null;

        elementId = request.getParameter("elementId");
        kpiMasterId = request.getParameter("masterKpiId");
        String tVal = request.getParameter("tVal");
        String reportId = request.getParameter("reportId");
        String dashletId = request.getParameter("dashletId");
        String kpiType = request.getParameter("kpiType");

        if (session != null) {
            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
            Container container = (Container) map.get(reportId);
            pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
            DashletDetail dashlet = collect.getDashletDetail(dashletId);

            tVal = tVal.replaceAll(",", "");
            String timeLevel = "";
            timeLevel = request.getParameter("timeLevel");
            DashboardTemplateDAO dbrdDAO = new DashboardTemplateDAO();
            if (timeLevel.equalsIgnoreCase("Month")) {
                Double Target = Double.parseDouble(tVal);
                Double dayTarget = Target / 31;
                dbrdDAO.saveTargetForKpiTable(elementId, kpiMasterId, dayTarget, "Day", reportId, dashlet);
                Double weekTarget = dayTarget * 7;
                dbrdDAO.saveTargetForKpiTable(elementId, kpiMasterId, weekTarget, "Week", reportId, dashlet);
                Double qtrTarget = dayTarget * 90;
                dbrdDAO.saveTargetForKpiTable(elementId, kpiMasterId, qtrTarget, "Qtr", reportId, dashlet);
                Double yearTarget = dayTarget * 365;
                dbrdDAO.saveTargetForKpiTable(elementId, kpiMasterId, yearTarget, "Year", reportId, dashlet);
            }
            //Month,Day,Week,Qtr,Year
            //dbrdDAO.saveTargetForKpiTable(elementId, kpiMasterId, tVal, timeLevel);
            dbrdDAO.saveTargetForKpiTable(elementId, kpiMasterId, Double.parseDouble(tVal), timeLevel, reportId, dashlet);

            PrintWriter out = response.getWriter();
            out.println("1");
        }
        return null;

    }

    //methods used to build graphs in Dasdboard
    public ActionForward getGraphByGraphId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
        dashboardTemplateBD.setServletRequest(request);
        PrintWriter out = response.getWriter();
        ServletContext context = this.getServlet().getServletContext();
        boolean isFxCharts = Boolean.parseBoolean(context.getInitParameter("isFxCharts"));
        String addedToDBStr = request.getParameter("addedToDashBoard");
        boolean addedToDashboard = false;
        if (addedToDBStr != null && "true".equalsIgnoreCase(addedToDBStr)) {
            addedToDashboard = true;
        }
        HashMap map = null;
        Container container = null;
        String customDbrdId = "";
        HashMap divGraphs = null;
        String reportId = request.getParameter("reportId");
        String graphId = request.getParameter("graphId");
        String divId = request.getParameter("divId");

        String result = "";
        if (session != null) {
            map = (HashMap) session.getAttribute("PROGENTABLES");
            customDbrdId = request.getParameter("dashboardId");
            container = (Container) map.get(customDbrdId);
            if (container.getDivDetails() != null) {
                divGraphs = new HashMap();
            }
            if (container.getDivDetails() != null) {
                divGraphs = container.getDivGraphs();//(HashMap) session.getAttribute("divDetails");
            }
            divGraphs.put(divId, graphId + "," + reportId);
            container.setDivDetails(divGraphs);

            result = dashboardTemplateBD.getGraphByGraphId(divId, reportId, graphId, request, response, session, customDbrdId, isFxCharts, addedToDashboard);
            out.print(result);
            return null;
        } else {

            return null;
        }
    }

    public ActionForward buildDbrdGraphs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PrintWriter out = response.getWriter();
        ServletContext context = this.getServlet().getServletContext();
        HttpSession session = request.getSession(false);
        boolean isFxCharts = Boolean.parseBoolean(context.getInitParameter("isFxCharts"));
        if (session != null) {
            DashboardTemplateBD dashboardTempBD = new DashboardTemplateBD();
            dashboardTempBD.setServletRequest(request);
            StringBuilder graphsBuffer = dashboardTempBD.buildDbrdGraphs(request, response, isFxCharts);
            out.print(graphsBuffer);
        } else {
            out.print("Session Expired...Please logout and login");
        }
        return null;
    }

    public ActionForward deleteDbrdGraphs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        HashMap map = null;
        map = (HashMap) session.getAttribute("PROGENTABLES");
        String dashBoardId = request.getParameter("dashboardId");
        Container container = (Container) map.get(dashBoardId);
        String dashletId = request.getParameter("dashletId");

        if (session != null) {
            DashboardTemplateBD dashboardTempBD = new DashboardTemplateBD();
            dashboardTempBD.deleteDbrdGraph(container, dashletId);
            out.println("Success");
        } else {
            out.print("Session Expired...Please logout and login");
        }
        return null;
    }

    //end of methods used for building dashboard graphs
    public void deleteDatabaseData(String DashId) {
        ArrayList arl = new ArrayList();
        arl.add("delete from PRG_AR_KPI_GRAPH_DETAILS where DASHBOARD_ID=" + DashId);
        arl.add("delete from PRG_AR_KPI_DETAILS  where KPI_MASTER_ID in(select KPI_MASTER_ID from PRG_AR_DASHBOARD_DETAILS where dashboard_id=" + DashId + ")");
        arl.add("delete  from  PRG_AR_GRAPH_DETAILS where GRAPH_ID in (select GRAPH_ID  from PRG_AR_GRAPH_MASTER where REPORT_ID=" + DashId + ")");
        arl.add("delete from PRG_AR_GRAPH_MASTER where REPORT_ID=" + DashId);
        arl.add("delete from PRG_KPI_USER_COMMENTS where KPI_MASTER_ID in(select KPI_MASTER_ID from PRG_AR_DASHBOARD_DETAILS where dashboard_id=" + DashId + ")");
        arl.add("delete from PRG_AR_DASHBOARD_DETAILS where DASHBOARD_ID= " + DashId);
        arl.add("delete from PRG_AR_QUERY_DETAIL where REPORT_ID= " + DashId);
        arl.add("delete from DASHBOARD_TARGET_KPI_VALUE where DASHBOARD_ID= " + DashId);
        arl.add("delete from PRG_AR_REPORT_MASTER where REPORT_ID=" + DashId);
        arl.add("delete from PRG_AR_REPORT_MAP_DETAILS where REPORT_ID=" + DashId);
//        arl.add("delete from PRG_AR_REPORT_VIEW_BY_MASTER where REPORT_ID="+DashId);
//        arl.add("delete from PRG_AR_REPORT_PARAM_DETAILS where REPORT_ID="+DashId);
//        arl.add("delete from PRG_AR_REPORT_VIEW_BY_DETAILS where REPORT_ID="+DashId);
//        arl.add("delete from PRG_AR_REPORT_TIME where REPORT_ID="+DashId);
//        arl.add("delete from PRG_AR_REPORT_TIME_DETAIL where REPORT_ID="+DashId);
//        arl.add("delete from PRG_AR_KPIWITHGRAPH where REPORT_ID="+DashId);

        PbDb PbDbobj121 = new PbDb();
        try {
            PbDbobj121.executeMultiple(arl);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public ActionForward ZoomTargetGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession hs = request.getSession(false);
        String[] viewbycolumnsstr = (String.valueOf(request.getParameter("viewbycolumnsstr"))).split(",");
        String[] barchartcolumntitlesstr = (String.valueOf(request.getParameter("barchartcolumntitlesstr"))).split(",");
        String[] keyvaluesstr = (String.valueOf(request.getParameter("keyvaluesstr"))).split(",");
        String[] datavaluesstr = (String.valueOf(request.getParameter("datavaluesstr"))).split(",");
        ProgenChartDatasets graph = new ProgenChartDatasets();

        graph.setTimeLevel("DAY");
        ProgenChartDisplay pchart = new ProgenChartDisplay(600, 400);
        pchart.setCtxPath(request.getContextPath());
        pchart.setGraph(graph);
        pchart.setSession(hs);
        pchart.setResponse(response);
        pchart.setOut(response.getWriter());
        String dashBoardID = request.getParameter("dashBoardID");
        Container container = Container.getContainerFromSession(request, dashBoardID);
        pchart.setRetObj(container.getKpiRetObj());
        pchart.GetKPITimeSeriesChartZoom(viewbycolumnsstr, barchartcolumntitlesstr, keyvaluesstr, datavaluesstr);

        String imgdata = pchart.chartDisplay;
        PrintWriter out = response.getWriter();
        out.print(imgdata);
        return null;
    }

    public ActionForward addScoreCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String scoreCards = request.getParameter("scoreCardIds");
        String dashboardId = request.getParameter("dashboardId");

        Container container = null;
        HttpSession hs = request.getSession(false);
        HashMap map = (HashMap) hs.getAttribute("PROGENTABLES");
        String userId = String.valueOf(hs.getAttribute("USERID"));
        container = (Container) map.get(dashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();


        DashboardTemplateBD bd = new DashboardTemplateBD();
        String masterId = bd.addScoreCard(container, scoreCards);

        PbDashboardViewerBD viewerBD = new PbDashboardViewerBD();
        PbReturnObject retObj = viewerBD.getDashboardKPIData(container, collect, userId);
        container.setKpiRetObj(retObj);

        String result = collect.displayScoreCard(masterId, retObj, null);
        PrintWriter out = response.getWriter();
        out.println(result);
        return null;
    }

    public ActionForward addMap(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String measureIds = request.getParameter("measureIds");
        String dashboardId = request.getParameter("dashboardId");
        String editMap = request.getParameter("editMap");
        String[] dashletDivId = request.getParameter("divId").split("-");
        String divId = "newMap";
        boolean isEdit = false;
        if (editMap != null && "true".equalsIgnoreCase(editMap)) {
            isEdit = true;
            divId = dashletDivId[1];
        }

        Container container = null;
        HttpSession hs = request.getSession(false);
        HashMap map = (HashMap) hs.getAttribute("PROGENTABLES");
        String userId = String.valueOf(hs.getAttribute("USERID"));

        container = (Container) map.get(dashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();

        DashboardTemplateBD bd = new DashboardTemplateBD();
//        bd.addMap(container, measureIds, isEdit, divId, userId);
        PrintWriter out = response.getWriter();
        out.println("Success");
        return null;
    }

    public ActionForward updateGraphProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String dashboardId = request.getParameter("dashboardId");
        String graphId = request.getParameter("graphId");
        String dashletId = request.getParameter("dashletId");
        Container container = null;
        String grpType = null;
        String grpSize = null;
        String grpId = null;
        String TableId = null;
        String graphChange = null;
        String presCollist = null;
        String swapBy = null;
        String rowValues = null;
        String showLegends = null;
        String showGT = null;
        String graphLegendLoc = null;
        String nbrFormat = null;
        String graphSymbol = null;
        String graphGridLines = null;
        String targetRange = null;
        HashMap singleGraphDetails = null;
        boolean isShowLabels = false;
        String startValue = null;
        String endValue = null;
        String startindex = null;
        String endindex = null;
        String graphDisplayRows = null;

        String rgbColorCode = null;
        String[] rgbColorArr = null;
        GraphProperty graphProperty = null;
        DashletPropertiesHelper dashletPropertiesHelper = null;
        DashboardViewerDAO viewerDao = new DashboardViewerDAO();
        Boolean issortAll = true;
        String[] jqcolorSeries = new String[10];
//        dashletPropertiesHelper = viewerDao.getDashletPropertiesHelperObject(dashletId);
        String from = request.getParameter("from");
        if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {
            HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
            container = (Container) map.get(dashboardId);

            pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
            DashletDetail dashlet = collect.getDashletFromGraphId(graphId);
            GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
            graphProperty = graphDetails.getGraphProperty();
            JqplotGraphProperty jqprop = new JqplotGraphProperty();
            jqprop = dashlet.getJqplotgrapprop();


            grpType = request.getParameter("grptypid");
            grpSize = request.getParameter("grpsizeid");
            TableId = request.getParameter("TableId");
            graphChange = request.getParameter("graphChange");
            rowValues = request.getParameter("rowValues");
            nbrFormat = request.getParameter("nbrFormat");
            graphLegendLoc = request.getParameter("graphLegendLoc");
            showLegends = request.getParameter("showLegends");
            graphGridLines = request.getParameter("graphGridLines");
            showGT = request.getParameter("showGT");
            swapBy = request.getParameter("SwapColumn");
            targetRange = request.getParameter("targetRange");
            String[] colorSeries = new String[10];
            String paramName = "";
            for (int i = 0; i < 10; i++) {
                paramName = "colorSelect" + i;
                if (request.getParameter(paramName) != null && !request.getParameter(paramName).equalsIgnoreCase("null") && !request.getParameter(paramName).equalsIgnoreCase("")) {
                    colorSeries[i] = request.getParameter(paramName);
                }
            }
            if (targetRange != null && targetRange.equalsIgnoreCase("Discrete")) {
                startValue = request.getParameter("startValue1");
            } else {
                startValue = request.getParameter("startValue2");
            }
            endValue = request.getParameter("endValue");
            graphSymbol = request.getParameter("graphSymbol");
            rgbColorCode = request.getParameter("rgbColorCode");
            if (!(rgbColorCode == null || rgbColorCode == "")) {
                rgbColorArr = rgbColorCode.replace("rgb(", "").replace(")", "").split(",");
            }
            jqcolorSeries = request.getParameterValues("jqcolorCodeseries");
            if (jqprop != null) {
                jqprop.setSeriescolors(jqcolorSeries);
            }
//                if(dashletPropertiesHelper!=null){
//                  graphDisplayRows = Integer.toString(dashletPropertiesHelper.getCountForDisplay());
//                }else{
//                graphDisplayRows = request.getParameter("graphDisplayRows");
//                }
            graphDisplayRows = request.getParameter("graphDisplayRows");
            graphDisplayRows = graphDisplayRows != null ? graphDisplayRows : "";
            startindex = graphDisplayRows == null ? String.valueOf(graphProperty.getStartValue()) : "0";
            endindex = graphDisplayRows == null ? String.valueOf(graphProperty.getEndValue()) : graphDisplayRows;
            graphDetails.setDisplayRows(graphDisplayRows);
            if (graphDisplayRows != null && !graphDisplayRows.equalsIgnoreCase("0.0") && !graphDisplayRows.equalsIgnoreCase("All")) {
                if (graphDetails.getDashletpropertieshelper() != null) {
                    graphDetails.getDashletpropertieshelper().setCountForDisplay(Integer.parseInt(graphDisplayRows));
                }
            }
            if (graphDisplayRows.equalsIgnoreCase("All")) {
                graphDetails.getDashletpropertieshelper().setSortAll(true);
            }
            if (request.getParameter("showLabels") != null) {
                isShowLabels = Boolean.valueOf(request.getParameter("showLabels"));
            }


            if (graphProperty == null) {
                graphProperty = new GraphProperty();
                graphDetails.setGraphProperty(graphProperty);
            }
            graphProperty.setColorSeries(colorSeries);

            graphProperty.setLabelsDisplayed(isShowLabels);
            if ("Y".equalsIgnoreCase(showLegends)) {
                graphDetails.setLegendAllowed(true);
            } else {
                graphDetails.setLegendAllowed(false);
            }
            if ("Y".equalsIgnoreCase(graphGridLines)) {
                graphDetails.setShowXAxisGrid(true);
                graphDetails.setShowYAxisGrid(true);
            } else {
                graphDetails.setShowXAxisGrid(false);
                graphDetails.setShowYAxisGrid(false);
            }
            if ("Y".equalsIgnoreCase(showGT)) {
                graphDetails.setShowGT(true);
            } else {
                graphDetails.setShowGT(false);
            }
            graphProperty.setNumberFormat(nbrFormat);
            graphDetails.setLegendLocation(graphLegendLoc);
            graphProperty.setSymbol(graphSymbol);
            graphDetails.setGraphSizeName(grpSize);
            graphProperty.setSwapGraphColumns(swapBy);
            if (startValue != null && !"".equals(startValue)) {
                graphProperty.setStartValue(Double.parseDouble(startValue));
            }
            if (endValue != null && !"".equals(endValue)) {
                graphProperty.setEndValue(Double.parseDouble(endValue));
            }
            graphProperty.setRgbColorArr(rgbColorArr);
        }
        return null;
    }

    public ActionForward setInsightAndCommentViewStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        String dbrdId = request.getParameter("dashboardId");
        String dashletId = request.getParameter("dashletId");
        boolean showinsight = Boolean.parseBoolean(request.getParameter("insigtSelect"));
        boolean showcomment = Boolean.parseBoolean(request.getParameter("commentSelect"));
        boolean showgraph = Boolean.parseBoolean(request.getParameter("graphSelect"));

        Container container = null;
        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(dbrdId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail detail = collect.getDashletDetail(dashletId);
        KPI kpiDetails = (KPI) detail.getReportDetails();
        kpiDetails.setInsightAndCommentViewStaus(showinsight, showcomment, showgraph);

        return null;
    }

    public ActionForward createRegionDashlet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String dashboardId = request.getParameter("dashboardId");
        String dashletId = request.getParameter("dashletId");
        int row = Integer.parseInt(request.getParameter("row"));
        int col = Integer.parseInt(request.getParameter("col"));
//        int prevCol=0;
        int prevRow = Integer.parseInt(request.getParameter("prevRow"));
        int dashId = Integer.parseInt(dashletId);
        HashMap map = new HashMap();
        Container container = null;
        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(dashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        for (int i = prevRow; i < row; i++) {
            for (int j = 0; j < col; j++) {
                DashletDetail detail = new DashletDetail();
                detail.setDashBoardDetailId(dashletId);
                detail.setCol(j);
                detail.setRow(i);
                detail.setColSpan(1);
                detail.setRowSpan(1);
                collect.addDashletDetail(detail);
                dashId = dashId + 1;
                dashletId = String.valueOf(dashId);
            }
        }
//        prevCol=row;
        return null;
    }

    public ActionForward mergeColumns(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String dashboardId = request.getParameter("dashboardId");
        String dashletId = request.getParameter("dashlet");
        String colSpan = request.getParameter("colSpan");
        String delDashletId = request.getParameter("delDashletId");
        Container container = null;
        HashMap map = new HashMap();
        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(dashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();

        List<DashletDetail> dashletDetails = collect.dashletDetails;
        for (int i = 0; i < dashletDetails.size(); i++) {
            DashletDetail detail = dashletDetails.get(i);
            String dashlet = detail.getDashBoardDetailId();
            if (dashlet.equals(dashletId)) {
                detail.setColSpan(Integer.parseInt(colSpan));
            }
            if (dashlet.equals(delDashletId)) {
                dashletDetails.remove(i);
            }
        }
        return null;
    }

    public ActionForward mergeRows(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String dashboardId = request.getParameter("dashboardId");
        String dashletId = request.getParameter("dashlet");
        String rowSpan = request.getParameter("rowSpan");
//        String delDashletId=request.getParameter("delDashletId");
        Container container = null;
        HashMap map = new HashMap();
        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(dashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        List<DashletDetail> dashletDetails = collect.dashletDetails;
        for (int i = 0; i < dashletDetails.size(); i++) {
            DashletDetail detail = dashletDetails.get(i);
            String dashlet = detail.getDashBoardDetailId();
            if (dashlet.equals(dashletId)) {
                detail.setRowSpan(Integer.parseInt(rowSpan));
            }
//            if(dashlet.equals(delDashletId))
//            {
//                
//                dashletDetails.remove(i);
//            }
        }
        return null;
    }

//    public ActionForward delDashlet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception{
//        HttpSession session=request.getSession(false);
//        String dashboardId = request.getParameter("dashboardId");
//        String delDashletId = request.getParameter("dashlet");
//        Container container = null;
//        HashMap KPIAttributesMap = new HashMap();
//        KPIAttributesMap = (HashMap) session.getAttribute("PROGENTABLES");
//        container = (Container) KPIAttributesMap.get(dashboardId);
//        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
//
//        List<DashletDetail> dashletDetails = collect.dashletDetails;
//        for (int i = 0; i < dashletDetails.size(); i++)
//        {
//            DashletDetail detail = dashletDetails.get(i);
//            String dashlet = detail.getDashBoardDetailId();
//            if(dashlet.equals(delDashletId))
//            {
//                dashletDetails.remove(i);
//            }
//        }
//        return null;
//    }
    public ActionForward clearDashlet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession();
        String dashboardId = request.getParameter("dashboardId");
        String dashletId = request.getParameter("dashletId");
        String row = request.getParameter("row");
        String col = request.getParameter("col");
        String rowSpan = request.getParameter("rowSpan");
        String colSpan = request.getParameter("colSpan");
        Container container = null;
        HashMap map = new HashMap();
        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(dashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        int dashletIdVal = Integer.parseInt(dashletId);

        List<DashletDetail> dashletDetails = collect.dashletDetails;
//        for (int i = 0; i < dashletDetails.size(); i++)
//        {
//             if(i==(Integer.parseInt(dashletId)))
//            {
        //  DashletDetail detail = dashletDetails.get(dashletIdVal);
//                String dashlet = detail.getDashBoardDetailId();

        for (DashletDetail detail : dashletDetails) {
            if (detail.getDashBoardDetailId().equalsIgnoreCase(dashletId)) {
                detail.setRow(Integer.parseInt(row));
                detail.setCol(Integer.parseInt(col));
                detail.setRowSpan(Integer.parseInt(rowSpan));
                detail.setColSpan(Integer.parseInt(colSpan));
                detail.setDashletName(null);
                detail.setDisplaySequence(0);
                detail.setDisplayType(null);
                detail.setGraphId(null);
                detail.setKpiMasterId(null);
                detail.setKpiType(null);
                detail.setReportDetails(null);
                detail.setRefReportId(null);
            }
        }
//
//                detail.setRow(Integer.parseInt(row));
//                detail.setCol(Integer.parseInt(col));
//                detail.setRowSpan(Integer.parseInt(rowSpan));
//                detail.setColSpan(Integer.parseInt(colSpan));
//                detail.setDashletName("");
//                detail.setDisplaySequence(-1);
//                detail.setDisplayType("");
//                detail.setGraphId("");
//                detail.setKpiMasterId("");
//                detail.setKpiType("");
//                detail.setReportDetails(null);

//            }

//        }
        return null;
    }

    public static void main(String args[]) {
        ArrayList arl = new ArrayList();
        arl.add("2182");
        arl.add("2183");
        arl.add("2184");
        String DashboardId = "4957";
        LinkedHashMap TargetDetailsHM = new LinkedHashMap();
        PbReturnObject retObj1 = new PbReturnObject();
        PbDb pb1 = new PbDb();

        LinkedHashMap elementdatahm = new LinkedHashMap();
        ArrayList elementdata = new ArrayList();

        for (int i = 0; i <= arl.size() - 1; i++) {
            elementdatahm = new LinkedHashMap();
            //////.println("outer arl.get(i)="+arl.get(i));
            String query = " select ELEMENT_ID,KPI_MASTER_ID,TIME_LEVEL,TARGET_VALUE,DASHBOARD_ID from DASHBOARD_TARGET_KPI_VALUE where KPI_MASTER_ID=" + arl.get(i) + " and DASHBOARD_ID=" + DashboardId;
            try {
                retObj1 = pb1.execSelectSQL(query);

                if (retObj1.getRowCount() > 0) {
                    for (int j = 0; j < retObj1.getRowCount(); j++) {
                        elementdata.add(retObj1.getFieldValueString(j, 0));
                        elementdata.add(retObj1.getFieldValueString(j, 1));
                        elementdata.add(retObj1.getFieldValueString(j, 2));
                        elementdata.add(retObj1.getFieldValueString(j, 3));
                        elementdatahm.put(String.valueOf(retObj1.getFieldValueString(j, 0)), elementdata);
                        elementdata = new ArrayList();
                    }
                    TargetDetailsHM.put(arl.get(i), elementdatahm);
                    elementdatahm = new LinkedHashMap();
                } else {
                    elementdatahm = new LinkedHashMap();
                    TargetDetailsHM.put(arl.get(i), elementdatahm);
                }
            } catch (Exception e) {
            }
        }
    }

    public ActionForward getKpiType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String kpiMasterId = "";
        kpiMasterId = request.getParameter("kpiMasterId");
        DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();

        PrintWriter out = response.getWriter();
        out.print(dashboardTemplateDAO.getKpiElementType(kpiMasterId));

        return null;
    }

    public ActionForward saveKpiTypes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String kpiMasterId = request.getParameter("kpiMasterId");
        String kpiTupesdetails = request.getParameter("kpiTupesdetails");
        String kpitypes[] = kpiTupesdetails.split(",");
        DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
        boolean status = dashboardTemplateDAO.saveKpiTypes(kpitypes, kpiMasterId);
        response.getWriter().print(status);

        return null;
    }

    public ActionForward getKpiNewName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String kpiMasterId = "";
        kpiMasterId = request.getParameter("kpiMasterId");
        DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();

        PrintWriter out = response.getWriter();
        out.print(dashboardTemplateDAO.getKpiElementNewName(kpiMasterId));

        return null;
    }

    public ActionForward getUpdatedName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String kpiMasterId = "";
        String newNamesArray = "";
        String kpidashboardId = "";
        List<String> elementIdsList = new ArrayList<String>();
        List<String> elementNamesList = new ArrayList<String>();
        kpiMasterId = request.getParameter("kpiMasterId");
        newNamesArray = request.getParameter("newNamesArray");
        kpidashboardId = request.getParameter("kpidashboardId");
        String newKPINamesArray[] = newNamesArray.split(",");
        String IdsAndNamesArray[] = new String[newKPINamesArray.length];
        for (int i = 0; i < newKPINamesArray.length; i++) {
            IdsAndNamesArray = newKPINamesArray[i].split("~");
            elementIdsList.add(IdsAndNamesArray[0]);
            elementNamesList.add(IdsAndNamesArray[1]);

        }
        Map<String, String> IdAndNameMap = new HashMap<String, String>();
        if (!elementIdsList.isEmpty() && !elementNamesList.isEmpty()) {
            for (int id = 0; id < elementIdsList.size(); id++) {
                IdAndNameMap.put(elementIdsList.get(id), elementNamesList.get(id));
            }
        }

        Container container = Container.getContainerFromSession(request, kpidashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        List<DashletDetail> dashletList = collect.dashletDetails;
        DashletDetail dashlet = Iterables.find(dashletList, DashletDetail.getDashletDetailPredicate(kpiMasterId));
        KPI kpiDetails = (KPI) dashlet.getReportDetails();
        ArrayListMultimap<String, KPIElement> kpiElementMap = kpiDetails.getKPIElementsMap();
        for (int i = 0; i < elementIdsList.size(); i++) {
            List<KPIElement> elements = kpiElementMap.get(elementIdsList.get(i));
            for (KPIElement elem : elements) {
                if (elementIdsList.get(i).equalsIgnoreCase(elem.getElementId())) {
                    elem.setElementName(IdAndNameMap.get(elementIdsList.get(i)));
                }
            }
        }
        DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
        dashboardTemplateDAO.saveNewKPINames(kpiMasterId, newKPINamesArray);
//            response.getWriter().print(dashboardTemplateDAO.saveNewKPINames(kpiMasterId,newKPINamesArray));
        return null;
    }

    public ActionForward saveDashletName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String kpidashboardId = "";
        String kpiMasterId = "";
        String dashletName = "";
        Boolean fordesigner = false;
        String dashletId = "";
        kpiMasterId = request.getParameter("kpiMasterId");
        dashletName = request.getParameter("newDashletName");
        kpidashboardId = request.getParameter("kpidashboardId");
        fordesigner = Boolean.parseBoolean(request.getParameter("fordesigner"));
        dashletId = request.getParameter("dashId");
        Container container = Container.getContainerFromSession(request, kpidashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        if (!fordesigner) {
            List<DashletDetail> dashletList = collect.dashletDetails;
            DashletDetail dashlet = Iterables.find(dashletList, DashletDetail.getDashletDetailPredicate(kpiMasterId));
            dashlet.setkpiName(dashletName);

            DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
            dashboardTemplateDAO.saveDashletNewName(kpiMasterId, dashletName);
        } else {
            DashletDetail dashlet = collect.getDashletDetail("0");
            dashlet.setkpiName(dashletName);
        }

//        PrintWriter out = response.getWriter();
//        out.print(dashboardTemplateDAO.saveDashletNewName(kpiMasterId,dashletName));

        return null;
    }

    public ActionForward saveKpiSymbol(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String kpidashboardId = request.getParameter("kpidashboardId");
        String kpiMasterId = request.getParameter("kpiMasterId");
        String KpielementIds = request.getParameter("eids");
        String elementIds[] = KpielementIds.split(",");
        String kpiSymbols[] = request.getParameterValues("kpiSymbols");
        String kpiAlignment[] = request.getParameterValues("kpiAlignment");
        String kpiFont[] = request.getParameterValues("kpiFont");
        String NbrFormat[] = request.getParameterValues("NbrFormat");
        //  String gblFormat[]=request.getParameterValues("globalformat");
        String kpiRound[] = request.getParameterValues("kpiRound");
        String selectrepids = request.getParameter("selectrepids");
        String selectrepIds[] = selectrepids.split(",");
        String kpiBg[] = request.getParameterValues("kpiBg");
        String NegativeValues[] = request.getParameterValues("Negativevalue");
        boolean checkbox = false;
        boolean checkboxhead = false;

        if (request.getParameter("globalnumberformat") != null) {
            checkbox = true;
        }
        if (request.getParameter("checkformatselect") != null) {
            checkboxhead = true;
        }

        List<String> elementIdsList = new ArrayList<String>();
        List<String> kpiSymbolsList = new ArrayList<String>();
        List<String> kpiAlignmentList = new ArrayList<String>();
        List<String> kpiFontList = new ArrayList<String>();
        List<String> NbrFormatList = new ArrayList<String>();
        List<String> kpiRoundList = new ArrayList<String>();
        List<String> gblFormatList = new ArrayList<String>();
        List<String> repids = new ArrayList<String>();
        List<String> kpiBgList = new ArrayList<String>();
        List<String> NegativeValuesList = new ArrayList<String>();


        elementIdsList.addAll(Arrays.asList(elementIds));
        kpiSymbolsList.addAll(Arrays.asList(kpiSymbols));
        kpiAlignmentList.addAll(Arrays.asList(kpiAlignment));
        kpiFontList.addAll(Arrays.asList(kpiFont));
        NbrFormatList.addAll(Arrays.asList(NbrFormat));
        kpiRoundList.addAll(Arrays.asList(kpiRound));
        kpiBgList.addAll(Arrays.asList(kpiBg));
        gblFormatList.addAll(Arrays.asList(NbrFormat[0]));
        repids.addAll(Arrays.asList(selectrepIds));
        NegativeValuesList.addAll(Arrays.asList(NegativeValues));
        kpiSymbolsList.remove(0);
        kpiAlignmentList.remove(0);
        kpiFontList.remove(0);
        NbrFormatList.remove(0);
        kpiRoundList.remove(0);
        kpiBgList.remove(0);
        NegativeValuesList.remove(0);



        Container container = Container.getContainerFromSession(request, kpidashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        List<DashletDetail> dashletList = collect.dashletDetails;
        DashletDetail dashlet = Iterables.find(dashletList, DashletDetail.getDashletDetailPredicate(kpiMasterId));
        List<KPISingleGroupHelper> tempGroupHelpers = dashlet.getSingleGroupHelpers();
        KPISingleGroupHelper kpiGrouphelper = null;
        if (!tempGroupHelpers.isEmpty()) {
            kpiGrouphelper = tempGroupHelpers.get(0);
        } else {
            kpiGrouphelper = new KPISingleGroupHelper();
        }

        kpiGrouphelper.setAtrelementIds(elementIdsList);
        kpiGrouphelper.setSymbols(kpiSymbolsList);
        kpiGrouphelper.setAlignment(kpiAlignmentList);
        kpiGrouphelper.setFont(kpiFontList);
        kpiGrouphelper.setNumberFormat(NbrFormatList);
        kpiGrouphelper.setRound(kpiRoundList);
        kpiGrouphelper.setBackGround(kpiBgList);
        kpiGrouphelper.setcheckformat(checkbox);
        kpiGrouphelper.setcheckhead(checkbox);
        kpiGrouphelper.setgblFormatList(gblFormatList);
        kpiGrouphelper.setselectrepIds(repids);
        kpiGrouphelper.setNegativevalue(NegativeValuesList);
        if (tempGroupHelpers.isEmpty()) {
            tempGroupHelpers.add(kpiGrouphelper);
        }
        dashlet.setSingleGroupHelpers(tempGroupHelpers);

        //result = kpibuilder.processSingleKpi(container, kpiMasterId, collect.kpiQuery, kpiDrill, dashletId, dashBoardId, createForDesigner,collect,userId,editDbrd);

        /*
         * String kpiSymbol = request.getParameter("kpiSymbol");
         * DashboardTemplateDAO dashboardTemplateDAO = new
         * DashboardTemplateDAO(); PrintWriter out = response.getWriter();
         * out.print(dashboardTemplateDAO.saveKpiSymbol(kpiSymbol, kpiMasterId));
         */

        return null;
    }

    public ActionForward saveTableName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String refRepId = "";
        String tableName = "";
        String dashletId = "";
        String dashboardId = "";
        PbDashboardViewerBD dashboardViewerBD = new PbDashboardViewerBD();
        Boolean flag = Boolean.parseBoolean(request.getParameter("flag"));
        refRepId = request.getParameter("refReportId");
        tableName = request.getParameter("newTableName");
        dashletId = request.getParameter("dashletID");
        dashboardId = request.getParameter("dashboardId");
        DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
        dashboardTemplateDAO.saveTableNewName(refRepId, tableName);
        Container container = Container.getContainerFromSession(request, dashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail dashlet = collect.getDashletDetail(dashletId);
        dashlet.setDashletName(tableName);
        dashlet.setkpiName(tableName);
        String htmlTable = dashboardViewerBD.buildDbrdTable(container, dashletId, false, "false", flag);
        response.getWriter().print(htmlTable);
//        PrintWriter out = response.getWriter();
//        out.print(dashboardTemplateDAO.saveTableNewName(refRepId,tableName));

        return null;
    }

    public ActionForward resetTableData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String dashletId = request.getParameter("dashletId");
        HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
        String dashReportID = request.getParameter("dashReportID");
        Boolean flag = Boolean.parseBoolean(request.getParameter("flag"));
//        Container container = (Container) map.get(dashReportID);
        Container container = Container.getContainerFromSession(request, dashReportID);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        List<DashletDetail> dashletList = collect.dashletDetails;
        DashletDetail dashlet = Iterables.find(dashletList, DashletDetail.getDashletDetailPredicatBaseOnDashLetID(dashletId));
        dashlet.setDashletPropertiesHelper(null);
        PbDashboardViewerBD dashboardViewerBD = new PbDashboardViewerBD();
        DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
        collect = (pbDashboardCollection) container.getReportCollect();
        dashlet = collect.getDashletDetail(dashletId);
        GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
        graphDetails.setDisplayRows("10");

        try {
            dashboardTemplateDAO.resetTableData(dashletId);
            String htmlTable = dashboardViewerBD.buildDbrdTable(container, dashletId, false, "false", flag);
            response.getWriter().print(htmlTable);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward runScheduler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String elementId = "";
        String kpiMasterId = "";
        String dashletId = "";
        String reportId = "";
        //getting details of kpi Scheduler

        elementId = request.getParameter("elmntId");
        kpiMasterId = request.getParameter("KpimasterSc");
        dashletId = request.getParameter("dashId");
        reportId = request.getParameter("repIdSch");
        String schdulerName = request.getParameter("schdReportName");
        if (schdulerName == null || schdulerName.equalsIgnoreCase("null")) {
            schdulerName = "";
        }
        String startDate = request.getParameter("startdate");
        String endDate = request.getParameter("enddate");
        int frequency = Integer.parseInt(request.getParameter("frequency"));
        int hrs = 0;
        int mins = 0;

        if (frequency == 1) {
            hrs = Integer.parseInt(request.getParameter("hrs"));
            mins = Integer.parseInt(request.getParameter("mins"));
        } else if (frequency == 2) {
            String Day = request.getParameter("monthDate");
            hrs = Integer.parseInt(request.getParameter("hrs"));
            mins = Integer.parseInt(request.getParameter("mins"));
        } else {
            String dayOfWeek = request.getParameter("alertDay");
            hrs = Integer.parseInt(request.getParameter("hrs"));
            mins = Integer.parseInt(request.getParameter("mins"));
        }
//        String emailid = "";
        StringBuilder emailid = new StringBuilder(300);
        String emil_Id = request.getParameter("mail");
        String[] email_Ids = request.getParameterValues("mail");
        for (int j = 0; j < email_Ids.length; j++) {
            emailid.append(",").append(email_Ids[j]);
//            emailid = emailid + "," + email_Ids[j];
        }
        HttpSession session = request.getSession(false);
        HashMap map = null;
        Container container = null;
        String dashBoardId = reportId;
        String kpiDrill = null;
        pbDashboardCollection collect = null;
        KPIBuilder kpibuilder = new KPIBuilder();
        PrintWriter out = response.getWriter();
        String fromDesigner = null;
        String result = "";
        String editDbrd = null;
        String userId = "";

        HashMap DBKPIHashMap = null;
        if (session != null && session.getAttribute("USERID") != null && session.getAttribute("PROGENTABLES") != null) {
            kpiDrill = request.getParameter("kpiDrill");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            fromDesigner = request.getParameter("fromDesigner");
            editDbrd = request.getParameter("editDbrd");
            boolean createForDesigner = false;

            if (fromDesigner != null && "true".equalsIgnoreCase(fromDesigner)) {
                createForDesigner = true;
            }
            container = (Container) map.get(dashBoardId);
            DBKPIHashMap = container.getDBKPIHashMap();

            collect = (pbDashboardCollection) container.getReportCollect();
            collect.setDBKPIHashMap(DBKPIHashMap);
            collect.reportId = dashBoardId;//here reportId is DashBoard Id
            collect.ctxPath = request.getContextPath();
            collect.setServletRequest(request);
            collect.setServletResponse(response);
            collect.setSession(session);

            collect.reportIncomingParameters = container.getRepReqParamsHashMap();
            userId = (String) session.getAttribute("USERID");
            kpibuilder.setElemntIdForMail(elementId);
            kpibuilder.setSchedulerFlag(true);
            result = kpibuilder.processSingleKpi(container, kpiMasterId, collect.kpiQuery, kpiDrill, dashletId, dashBoardId, createForDesigner, collect, userId, editDbrd);
        }
        //sending kpi to mail using scheduled details
        ReportSchedule schedule = new ReportSchedule();
        schedule.setFrequency(request.getParameter("frequency"));
        int rep_Id = Integer.parseInt(dashBoardId);
        schedule.setReportId(rep_Id);
        String time = request.getParameter("hrs") + ":" + request.getParameter("mins");
        schedule.setScheduledTime(time);
        schedule.setViewBy("");
        schedule.setUserId(userId);
        schedule.setContentType("kpihtml");
        List<ReportSchedulePreferences> schdPreferenceList = new ArrayList<ReportSchedulePreferences>();
        ReportSchedulePreferences schedulePreferences = new ReportSchedulePreferences();
        schedulePreferences.setDimId(elementId);
        schedulePreferences.setMailIds(emailid.toString());
        schedulePreferences.setDimValues("All");
        schdPreferenceList.add(schedulePreferences);
        UserDimensionMap usermap = new UserDimensionMap();
        List<UserDimensionMap> userDimensionMaps = new ArrayList<UserDimensionMap>();
        userDimensionMaps.add(usermap);
        String dimId = collect.dashletDetails.get(0).getReportDetails().getKPIElements().get(0).getElementId();
        usermap.setDimensionId(dimId);
        usermap.setDimensionValue("All");
        usermap.setUserId(Integer.parseInt(userId));
        usermap.setMailId(emil_Id);
        schedule.setUsermap(userDimensionMaps);
        schedule.setIsAutoSplited(true);
        schedule.setReportSchedulePrefrences(schdPreferenceList);
        schedule.setSchedulerName(schdulerName);
        ReportSchedulerJob job = new ReportSchedulerJob();
        StringBuilder tempSb = new StringBuilder("");
        tempSb.append("<html><head></head><body>" + result + "</body></html>");
        job.setKPIHtml(tempSb.toString());
        job.sendSchedulerMail(schedule);
        //}
        return null;
    }

    public ActionForward saveScheduler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession();
        ReportSchedule schedule = new ReportSchedule();
        HashMap map = null;
        Container container = null;
        String kpiDrill = null;
        pbDashboardCollection collect = null;
        KPIBuilder kpibuilder = new KPIBuilder();
        PrintWriter out = response.getWriter();
        String fromDesigner = null;
        String result = "";
        String editDbrd = null;
        String userId = "";

        String elementId = "";
        String kpiMasterId = "";
        String dashletId = "";
        String reportId = "";
        elementId = request.getParameter("elmntId");
        kpiMasterId = request.getParameter("KpimasterSc");
        dashletId = request.getParameter("dashId");
        reportId = request.getParameter("repIdSch");
        String schedulerName = request.getParameter("schdReportName");
        if (schedulerName == null || schedulerName.equalsIgnoreCase("null")) {
            schedulerName = "";
        }
        String startDate = request.getParameter("startdate");
        String endDate = request.getParameter("enddate");
//        String emailid = "";
        StringBuilder emailid = new StringBuilder(300);
        String[] mailDetail = request.getParameterValues("mail");
        for (int i = 0; i < mailDetail.length; i++) {
            emailid.append(",").append(mailDetail[i]);
//            emailid = emailid + "," + mailDetail[i];
        }


        String mailId = request.getParameter("mail");
        String frequency = request.getParameter("frequency");
        String scheduledTime = "";
        String particularDate = "";
        String schedulerFrequency = "";
        String hrs = request.getParameter("hrs");
        String mins = request.getParameter("mins");
        scheduledTime = hrs.concat(":").concat(mins);
        String dataSelection = "";
        if (frequency.equalsIgnoreCase("1")) {
            schedulerFrequency = "Daily";
            particularDate = "";
            dataSelection = request.getParameter("dailyData");
        } else if (frequency.equalsIgnoreCase("2")) {
            schedulerFrequency = "Monthly";
            particularDate = request.getParameter("monthDate");
            if (!"L".equalsIgnoreCase(particularDate) && !"B".equalsIgnoreCase(particularDate)) {
                dataSelection = request.getParameter("monthlyData");
            }
        } else {
            schedulerFrequency = "Weekly";
            particularDate = request.getParameter("alertDay");
        }
        String dashBoardId = reportId;

        HashMap DBKPIHashMap = null;
        if (session != null && session.getAttribute("USERID") != null && session.getAttribute("PROGENTABLES") != null) {
            kpiDrill = request.getParameter("kpiDrill");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            fromDesigner = request.getParameter("fromDesigner");
            editDbrd = request.getParameter("editDbrd");
            boolean createForDesigner = false;

            if (fromDesigner != null && "true".equalsIgnoreCase(fromDesigner)) {
                createForDesigner = true;
            }
            container = (Container) map.get(dashBoardId);
            DBKPIHashMap = container.getDBKPIHashMap();

            collect = (pbDashboardCollection) container.getReportCollect();
            collect.setDBKPIHashMap(DBKPIHashMap);
            collect.reportId = dashBoardId;//here reportId is DashBoard Id
            collect.ctxPath = request.getContextPath();
            collect.setServletRequest(request);
            collect.setServletResponse(response);
            collect.setSession(session);

            collect.reportIncomingParameters = container.getRepReqParamsHashMap();
            userId = (String) session.getAttribute("USERID");
            kpibuilder.setElemntIdForMail(elementId);
            kpibuilder.setSchedulerFlag(true);
            result = kpibuilder.processSingleKpi(container, kpiMasterId, collect.kpiQuery, kpiDrill, dashletId, dashBoardId, createForDesigner, collect, userId, editDbrd);
        }


        Date sDate;
        Date eDate;
        DateFormat formatter;
        formatter = new SimpleDateFormat("MM/dd/yyyy");
        sDate = formatter.parse(startDate);
        eDate = formatter.parse(endDate);
        schedule.setFrequency(request.getParameter("frequency"));
        schedule.setReportId(Integer.parseInt(reportId));
        schedule.setContenType("kpihtml");
        schedule.setEndDate(eDate);
        schedule.setFrequency(schedulerFrequency);
        schedule.setStartDate(sDate);
        schedule.setViewBy("");
        schedule.setScheduledTime(scheduledTime);
        schedule.setUserId(userId);
        schedule.setSchedulerName(schedulerName);
        schedule.setIsAutoSplited(true);

        List<ReportSchedulePreferences> schdPreferenceList = new ArrayList<ReportSchedulePreferences>();
        ReportSchedulePreferences schedulePreferences = new ReportSchedulePreferences();
        schedulePreferences.setDimId(elementId);
        schedulePreferences.setMailIds(emailid.toString());
        schedulePreferences.setDimValues("All");
        schdPreferenceList.add(schedulePreferences);
        UserDimensionMap usermap = new UserDimensionMap();
        List<UserDimensionMap> userDimensionMaps = new ArrayList<UserDimensionMap>();
        userDimensionMaps.add(usermap);
        String dimId = collect.dashletDetails.get(0).getReportDetails().getKPIElements().get(0).getElementId();
        usermap.setDimensionId(dimId);
        usermap.setDimensionValue("All");
        usermap.setUserId(Integer.parseInt(userId));
        usermap.setMailId(mailId);
//        schedule.set
        schedule.setUsermap(userDimensionMaps);
        schedule.setParticularDay(particularDate);
        schedule.setDataSelection(dataSelection);
        schedule.setReportSchedulePrefrences(schdPreferenceList);
        SchedulerDAO dao = new SchedulerDAO();
//        ReportSchedulerJob job = new ReportSchedulerJob();
        StringBuilder tempSb = new StringBuilder("");
        tempSb.append("<html><head></head><body>" + result + "</body></html>");
        dao.setKPIHtml(tempSb.toString());
        schedule.setKpiMasterId(kpiMasterId);
        schedule.setDashLetId(dashletId);
        schedule.setElementID(elementId);
        KPIScheduleHelper kPIScheduleHelper = new KPIScheduleHelper();
        kPIScheduleHelper.setDashBoardID(dashBoardId);
        kPIScheduleHelper.setDashLetId(dashletId);
        kPIScheduleHelper.setElementId(elementId);
        kPIScheduleHelper.setKpiMasterId(kpiMasterId);
        schedule.setkPIScheduleHelper(kPIScheduleHelper);
        String schedId = dao.insertReportSchedule(schedule);
//         job.setKPIHtml(tempSb.toString());

        dao.insertReportSchedulePreferences(schedule);
        SchedulerBD bd = new SchedulerBD();

        bd.scheduleReport(schedule, false);
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.print(schedId);

        return null;
    }

    public ActionForward groupKpis(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String foldersIds = "";
        foldersIds = request.getParameter("foldersIds");
        String dbrdId = request.getParameter("dashboardId");
        String kpiType = request.getParameter("kpiType");
        String dashletId = request.getParameter("dashletId");
        String kpiMasterId = request.getParameter("kpiMasterId");
        String result = "";

//        DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
//        String result = dashboardTemplateDAO.getKpis(foldersIds);

        request.setAttribute("Kpis", result);
        request.setAttribute("dbrdId", dbrdId);
        request.setAttribute("kpiType", kpiType);
        request.setAttribute("kpiMasterId", kpiMasterId);
        request.setAttribute("dashletId", dashletId);

        return mapping.findForward("groupKpis");
    }

    public ActionForward getAllKpiDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String kpielements = request.getParameter("kpielements");
        String kpiIDs = request.getParameter("kpinames");
        String dragableList[] = kpiIDs.split(",");
        String KPInames[] = kpielements.split(",");
        ArrayList<String> dragableArrayList = new ArrayList<String>();
        ArrayList<String> KPINamesList = new ArrayList<String>();
        dragableArrayList.addAll(Arrays.asList(dragableList));
        KPINamesList.addAll(Arrays.asList(KPInames));
        GenerateDragAndDrophtml dragAndDrophtml = new GenerateDragAndDrophtml("Drag KPI from here", "Drop KPI here", null, dragableArrayList, request.getContextPath());
        dragAndDrophtml.setDragableListNames(KPINamesList);
        String htmlJson = dragAndDrophtml.getDragAndDropDiv();
        try {
            response.getWriter().print(htmlJson);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }


        return null;
    }

    public ActionForward buildKpiGrouping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String dashboardId = request.getParameter("dashboardId");
        String groupName = request.getParameter("groupName");
        String dashletId = request.getParameter("dashletId");
        String kpiMasterId = request.getParameter("kpimasterid");
        String calculationType = request.getParameter("calculationType");
        String eleIds = request.getParameter("EleIds");
        String kpiNames = request.getParameter("kpiNames");
        String elementIdsarray[] = eleIds.split(",");
        String kpiNamesarray[] = kpiNames.split(",");
        String kpiType = request.getParameter("kpiType");
        ArrayList<String> elementIdsArrayList = new ArrayList<String>();
        ArrayList<String> kpiNamesArrayList = new ArrayList<String>();
        elementIdsArrayList.addAll(Arrays.asList(elementIdsarray));
        kpiNamesArrayList.addAll(Arrays.asList(kpiNamesarray));
        KPISingleGroupHelper kpiSingleGroup = new KPISingleGroupHelper();
        kpiSingleGroup.setElementNameList(kpiNamesArrayList);
        kpiSingleGroup.setElementIds(elementIdsArrayList);
        kpiSingleGroup.setCalcType(calculationType);
        kpiSingleGroup.setGroupName(groupName);
        kpiSingleGroup.setKpiType(kpiType);

        Container container = Container.getContainerFromSession(request, dashboardId);

        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        List<DashletDetail> dashletList = collect.dashletDetails;
        DashletDetail dashlet = Iterables.find(dashletList, DashletDetail.getDashletDetailPredicate(kpiMasterId));
        List<KPISingleGroupHelper> tempISingleGroupHelpers = dashlet.getSingleGroupHelpers();

        tempISingleGroupHelpers.add(kpiSingleGroup);
        dashlet.setSingleGroupHelpers(tempISingleGroupHelpers);
        KPI kpiDetails = (KPI) dashlet.getReportDetails();
        kpiDetails.getElementIds().add(groupName);
        KPIElement element = new KPIElement();
        element.setElementId(groupName);
        element.setElementName(groupName);
        element.setIsGroupElement(true);
        element.setRefElementId(Joiner.on(",").join(elementIdsArrayList));
        kpiDetails.getKPIElementsMap().put(groupName, element);
        kpiDetails.getKPISequenceHashMap().put(kpiDetails.getKPISequenceHashMap().size() + 1, groupName);

        return null;
    }

    public ActionForward tableTranspose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String dashletId = request.getParameter("dashletId");
        String dashReportID = request.getParameter("dashReportID");
        Boolean flag = Boolean.parseBoolean(request.getParameter("flag"));
        HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
        Container container = (Container) map.get(dashReportID);
        PbDashboardViewerBD dashboardViewerBD = new PbDashboardViewerBD();
        try {
            String htmlTable = dashboardViewerBD.buildtableTranspose(container, dashletId, flag);
            response.getWriter().print(htmlTable);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward saveDbrColors(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);                         //fordashboardTableColor
        String elementClId = request.getParameter("colName");
        String elementId = elementClId.substring(2);
        String disColName = request.getParameter("disColName");
        String maxVal = request.getParameter("max");
        String minVal = request.getParameter("min");
        boolean isgradint = Boolean.parseBoolean(request.getParameter("gradientBased"));
        String elementName = request.getParameter("colmnlabelName");
        String dashletId = request.getParameter("dashletId");
        String reportid = request.getParameter("reportid");


        String colorCodes[] = request.getParameterValues("colorCodes");
        String operators[] = request.getParameterValues("operators");
        String sValues[] = request.getParameterValues("sValues");
        String eValues[] = request.getParameterValues("eValues");

        Container container = Container.getContainerFromSession(request, reportid);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();


        List<String> operator = new ArrayList<String>();
        List<String> colrcode = new ArrayList<String>();
        List<String> strtVal = new ArrayList<String>();
        List<String> endVal = new ArrayList<String>();
        colrcode.addAll(Arrays.asList(colorCodes));
        for (int i = 0; i < operators.length; i++) {
            operator.add(operators[i]);
            if (sValues[i] != null && !sValues[i].equals("")) {
                strtVal.add(sValues[i]);
            } else {
                strtVal.add(null);
            }
            if (operators[i].equalsIgnoreCase("<>")) {
                endVal.add(eValues[i]);
            } else {
                endVal.add(null);
            }
        }
        List<DashletDetail> dashletList = collect.dashletDetails;
        DashletDetail dashlet = Iterables.find(dashletList, DashletDetail.getDashletDetailPredicatBaseOnDashLetID(dashletId));
        List<DashboardTableColorGroupHelper> tableColorGrpList = dashlet.getDashbrdTableColor();

        DashboardTableColorGroupHelper dshbrdTblcolrhelper = null;
        boolean statusOfAdding = true;
        if (tableColorGrpList.isEmpty()) {
            dshbrdTblcolrhelper = new DashboardTableColorGroupHelper();
        } else {
//                             DashboardTableColorGroupHelper colorGroupHelper=null;
            for (DashboardTableColorGroupHelper helper : tableColorGrpList) {
                if (helper.getElementId().equalsIgnoreCase(elementId)) {
                    dshbrdTblcolrhelper = helper;

                    statusOfAdding = false;
                    break;

                }
            }
            if (dshbrdTblcolrhelper == null) {
                dshbrdTblcolrhelper = new DashboardTableColorGroupHelper();
            }
//              dshbrdTblcolrhelper=Iterables.find(tableColorGrpList, DashboardTableColorGroupHelper.getElementPredicatForColorGrp(elementId));
        }
        dshbrdTblcolrhelper.setElementId(elementId);
        dshbrdTblcolrhelper.setElementName(elementName);
        dshbrdTblcolrhelper.setMeasureType(disColName);
        dshbrdTblcolrhelper.setMeasureMaxValue(maxVal);
        dshbrdTblcolrhelper.setMeasureMinValue(minVal);
        dshbrdTblcolrhelper.setIsgradiantBase(isgradint);
        dshbrdTblcolrhelper.setColorVal(colrcode);
        dshbrdTblcolrhelper.setColorCondOper(operator);
        dshbrdTblcolrhelper.setCondStartValue(strtVal);
        dshbrdTblcolrhelper.setCondEndValue(endVal);

        if (statusOfAdding) {
            tableColorGrpList.add(dshbrdTblcolrhelper);
        }
        dashlet.setDashbrdTableColor(tableColorGrpList);
        return null;

    }

    public ActionForward GetReportNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String elementId = "";
        String foldersIds = "";
        String elementname = request.getParameter("elementname");
        elementId = request.getParameter("elmntId");
        foldersIds = request.getParameter("foldersIds");
        DashboardTemplateDAO dashdao = new DashboardTemplateDAO();
        PrintWriter out = response.getWriter();
        out.print(dashdao.getKpiDrillToAnyReport(elementId, foldersIds, elementname));
        return null;

    }

    public ActionForward getKpiAttributes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String kpidashboardId = request.getParameter("dashboardId");
        String kpiMasterId = request.getParameter("masterId");
        Container container = Container.getContainerFromSession(request, kpidashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        List<DashletDetail> dashletList = collect.dashletDetails;
        DashletDetail dashlet = Iterables.find(dashletList, DashletDetail.getDashletDetailPredicate(kpiMasterId));
        KPI kpiDetails = new KPI();
        if (dashlet.getDisplayType().equalsIgnoreCase("KpiWithGraph")) {
            kpiDetails = dashlet.getKpiDetails();
        } else {
            kpiDetails = (KPI) dashlet.getReportDetails();
        }
        ArrayListMultimap<String, KPIElement> kpiElementMap = kpiDetails.getKPIElementsMap();
        List<String> kpiEIds = kpiDetails.getElementIds();

        List<KPISingleGroupHelper> groupHelpers = dashlet.getSingleGroupHelpers();
        KPISingleGroupHelper helper = new KPISingleGroupHelper();
        List<String> helperEIds = new ArrayList<String>();
        List<String> helperENames = new ArrayList<String>();
        List<String> kpiENames = new ArrayList<String>();

        if (!groupHelpers.isEmpty()) {
            helper = groupHelpers.get(0);
            helperEIds = helper.getAtrelementIds();
        }

        for (int i = 0; i < kpiEIds.size(); i++) {
            String elementId = (String) kpiEIds.get(i);
            List<KPIElement> elements = kpiElementMap.get(elementId);
            for (KPIElement elem : elements) {
                if (elementId.equalsIgnoreCase(elem.getElementId())) {
                    kpiENames.add(elem.getElementName());
                }
            }
        }

        for (int i = 0; i < helperEIds.size(); i++) {
            String elementId = (String) helperEIds.get(i);
            List<KPIElement> elements = kpiElementMap.get(elementId);
            for (KPIElement elem : elements) {
                if (elementId.equalsIgnoreCase(elem.getElementId())) {
                    helperENames.add(elem.getElementName());
                }
            }
        }
        HashMap KPIAttributesMap = new HashMap();
        if (helper != null) {
            List<String> symbols = helper.getSymbols();
            List<String> alignment = helper.getAlignment();
            List<String> font = helper.getFont();
            List<String> nbrFormat = helper.getNumberFormat();;
            List<String> round = helper.getRound();
            List<String> selectedids = helper.getselectrepIds();
            List<String> background = helper.getBackGround();
            List<String> negativevalues = helper.getNegativevalue();
            KPIAttributesMap.put("EIdfrmMap", kpiEIds);
            KPIAttributesMap.put("ENamefrmMap", kpiENames);
            KPIAttributesMap.put("EIdfrmHelper", helperEIds);
            KPIAttributesMap.put("ENamefrmHelper", helperENames);
            KPIAttributesMap.put("symbols", symbols);
            KPIAttributesMap.put("alignment", alignment);
            KPIAttributesMap.put("font", font);
            KPIAttributesMap.put("nbrFormat", nbrFormat);
            KPIAttributesMap.put("round", round);
            KPIAttributesMap.put("selectedids", selectedids);
            KPIAttributesMap.put("background", background);
            KPIAttributesMap.put("negativevalue", negativevalues);
        }
        Gson gson = new Gson();
        String jsonString = gson.toJson(KPIAttributesMap);
        response.getWriter().print(jsonString);
        // 
        return null;
    }

    public ActionForward goButtonForSort(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {


        String dashletId = request.getParameter("dashletId");
        Boolean flag = Boolean.parseBoolean(request.getParameter("flag"));
        String element_IDforSort = request.getParameter("SortMeasure");
        String typeForSort = request.getParameter("SortOrder");
        String dashReportID = request.getParameter("PbReportId");

        Container container = Container.getContainerFromSession(request, dashReportID);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();

        List<DashletDetail> dashletList = collect.dashletDetails;
        DashletDetail dashlet = Iterables.find(dashletList, DashletDetail.getDashletDetailPredicatBaseOnDashLetID(dashletId));
        GraphReport details = (GraphReport) dashlet.getReportDetails();
//              details.getDashletpropertieshelper().getCountForDisplay();
        DashletPropertiesHelper dashletPropertiesHelper = dashlet.getDashletPropertiesHelper();

        PbDashboardViewerBD dashboardViewerBD = new PbDashboardViewerBD();
        if (dashlet.getDashletPropertiesHelper() != null) {
            dashletPropertiesHelper = dashlet.getDashletPropertiesHelper();
        } else {
            dashletPropertiesHelper = new DashletPropertiesHelper();
        }
        if (dashletPropertiesHelper != null) {
            dashletPropertiesHelper.setElement_IDforSort(element_IDforSort);
            dashletPropertiesHelper.setTypeForSort(Integer.parseInt(typeForSort));
//       dashletPropertiesHelper.setSortOnMeasure(element_IDforSort);
            dashletPropertiesHelper.setIsFromTopBottom(false);
            dashletPropertiesHelper.setSortFlag("true");
            if (details != null) {
                if (details.getDashletpropertieshelper() != null) {
                    if (details.getDashletpropertieshelper().getCountForDisplay() != 0) {
                        dashletPropertiesHelper.setCountForDisplay(details.getDashletpropertieshelper().getCountForDisplay());
                    }
                }
            }
            dashletPropertiesHelper.setDashletId(dashletId);
            dashlet.setDashletPropertiesHelper(dashletPropertiesHelper);
            container.setDashletPropertiesHelper(dashletPropertiesHelper);
        }
        try {
            String htmlTable = dashboardViewerBD.buildDbrdTable(container, dashletId, false, "false", flag);
            response.getWriter().print(htmlTable);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward updateGraphName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dashboardId = request.getParameter("dashboardid");
        String graphId = request.getParameter("graphId");
        String NewGraphName = request.getParameter("NewGraphName");
        String dashletId = request.getParameter("dashletid");
        Container container = Container.getContainerFromSession(request, dashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail detail = collect.getDashletDetail(dashletId);
        detail.setDashletName(NewGraphName);
        return null;

    }

    public ActionForward getRelatedGraphs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {

//      String dashletId = request.getParameter("dashletid");
        String folderId = request.getParameter("folderidGlobal");
        String elementIds = request.getParameter("elementids");
        String newElmntIds = elementIds.substring(1, elementIds.length());
        ArrayList<String> Elements = new ArrayList<String>();
        String[] Elmnts = newElmntIds.split(",");

        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        String RelgraphIdsAndNames = dao.getRelatedGraphs(folderId);

//      ArrayList<String>selectedRelgraphNames=new ArrayList<String>();
//      for(int i=0;i<Elmnts.length;i++){
//          selectedRelgraphIds.add(Elmnts[i]);
//
//      }
//      ArrayList<String> dragableArrayList = new ArrayList<String>();
//      for(String grId:selectedRelgraphIds){
//          dragableArrayList.add(grId);
//      }
//      GenerateDragAndDrophtml dragAndDrophtml=new GenerateDragAndDrophtml("Drag Graphs from here", "Drop Graphs here", selectedRelgraphIds,(ArrayList<String>)dragableArrayList, request.getContextPath());
//                       dragAndDrophtml.setDragableListNames(selectedRelgraphIds);
//                 dragAndDrophtml.setDropedmesNames(selectedRelgraphIds);
//                 String htmlJson=dragAndDrophtml.getDragAndDropDiv();
//                     try {
//                    response.getWriter().print(htmlJson);
//                } catch (IOException ex) {
//                    logger.error("Exception:",ex);
//                }
//      dao.getGraphNames(elementIds,folderId);
        return null;
    }

    public ActionForward updateKpiHeads(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dbrdId = request.getParameter("dbrdId");
        String dashletId = request.getParameter("dashletId");
        String kpiMasterId = request.getParameter("kpiMasterId");
        String kpiNewHeads = request.getParameter("kpiNewHeads");
        ArrayList<String> kpiheadarray = new ArrayList<String>();
        String newheads = "";
        if (kpiNewHeads.startsWith(",")) {
            newheads = kpiNewHeads.substring(1);
        } else {
            newheads = kpiNewHeads;
        }
        Container container = Container.getContainerFromSession(request, dbrdId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        List<DashletDetail> dashletList = collect.dashletDetails;
        DashletDetail dashlet = Iterables.find(dashletList, DashletDetail.getDashletDetailPredicate(kpiMasterId));
        String[] setKpiheads = newheads.split(",");
        for (int i = 0; i < setKpiheads.length; i++) {
            kpiheadarray.add(setKpiheads[i]);
        }
        dashlet.setKpiheads(kpiheadarray);
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
//       dao.updatekpiNewheads(dbrdId,dashletId,kpiMasterId,newheads);

        return null;
    }

    public ActionForward isAuthorizedUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String elementId = "";
        String masterId = "";
        String userId = "";
        String dashletId = "";
        elementId = request.getParameter("elementId");
        masterId = request.getParameter("masterId");
        userId = request.getParameter("userId");
        dashletId = request.getParameter("dashletId");
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        PrintWriter out = response.getWriter();
        String authorizedUserId = dao.getAuthorizedUserId(elementId, masterId, dashletId);
        if (userId.equalsIgnoreCase(authorizedUserId)) {
            out.print(true);
        } else {
            out.print(false);
        }

        return null;
    }

    public ActionForward editKPIName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dashletId = "";
        String dashboardId = "";
        String folderId = "";
        String graphId = "";
        String kpigrname = "";

        dashletId = request.getParameter("dashletid");
        dashboardId = request.getParameter("dashboardid");
        folderId = request.getParameter("folderid");
        graphId = request.getParameter("graphId");
        kpigrname = request.getParameter("kpiGrname");
        Container container = Container.getContainerFromSession(request, dashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail detail = collect.getDashletDetail(dashletId);
        Report reportDetails = detail.getReportDetails();
        KPIGraph kpiGrp = (KPIGraph) reportDetails;
        kpiGrp.setKpigrname(kpigrname);
        return null;
    }

    public ActionForward getKpiGraphType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dashboardId = "";
        String dashletId = "";
        String kpigrType = "";

        dashboardId = request.getParameter("dbrdId");
        dashletId = request.getParameter("dashletId");
        kpigrType = request.getParameter("grpType");
        Container container = Container.getContainerFromSession(request, dashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail detail = collect.getDashletDetail(dashletId);
        Report reportDetails = detail.getReportDetails();
        KPIGraph kpiGrp = (KPIGraph) reportDetails;
        kpiGrp.setKpiGraphType(kpigrType);
        return null;
    }

    public ActionForward getDbrdTimeDisplay(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        String conntype = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            conntype = "sqlserver";
        }
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            conntype = "MySql";
        }
        String repId = "";
        String from = "";
        repId = request.getParameter("repId");
        from = request.getParameter("from");
        StringBuilder sb = new StringBuilder();
        sb.append("");
        Container container = Container.getContainerFromSession(request, repId);
        if (container != null && container.getReportCollect() != null) {
            pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
            DashboardTemplateDAO dao = new DashboardTemplateDAO();
            ProgenParam connectionparam = new ProgenParam();
            String lastupdateedate = "";
            Connection con = null;
            ArrayList<String> qryelements = collect.reportQryElementIds;
            String elementId = "";
            if (qryelements != null && !qryelements.isEmpty()) {
                elementId = qryelements.get(0);
                con = connectionparam.getConnection(elementId);
                lastupdateedate = dao.getLastUpdatedDate(con, elementId);
            }
            ArrayList<String> timeinfo = collect.timeDetailsArray;

            String vals = container.getTimememdetails().get("PR_DAY_DENOM").toString();
            vals = vals.replace("[", "");
            vals = vals.replace("]", "");
            String[] vals1 = vals.split(",");
            DateFormat formatter;
            Date date12;
//        container.getTimememdetails().get("PR_DAY_DENOM");
            Date date = new Date();
            String[] dates1 = new String[10];
            String[] dates = new String[10];
            Calendar ca1123 = Calendar.getInstance();
            java.util.Date d1 = new java.util.Date(ca1123.getTimeInMillis());
            if (vals1[1].contains("/")) {
                dates = vals1[1].split("/");
            }
            if (vals1[1].contains("-")) {
                dates1 = vals1[1].split(" ");
                String values = dates1[1];
                String[] repdates = values.split("-");
                dates[2] = repdates[0];
                dates[0] = repdates[1];
                dates[1] = repdates[2];
            }
            //  dates[0];//month
            // dates[1];//day
            // dates[2];//year
            Calendar ca1 = Calendar.getInstance();

            // set(year, month, date) month 0-11
            ca1.set(Integer.parseInt(dates[2].substring(0, 4)), Integer.parseInt(dates[0].replace(" ", "")) - 1, Integer.parseInt(dates[1]));
            java.util.Date d = new java.util.Date(ca1.getTimeInMillis());
            String partialName = new SimpleDateFormat("MMM").format(d);
            String fullName = partialName + "'" + dates[2].substring(2, 4);
            String repUpdatedate = "";
            String nameToDisplay = timeinfo.get(3);
            Calendar ca112 = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            ca112.add(Calendar.DATE, -1);
            if (lastupdateedate != null && !lastupdateedate.equalsIgnoreCase("null") && !lastupdateedate.equalsIgnoreCase("")) {
                repUpdatedate = lastupdateedate;
            } else {
                repUpdatedate = dateFormat.format(ca112.getTime());
            }
            if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {
                nameToDisplay = "Custom";
            }
            if (timeinfo.get(3).equalsIgnoreCase("Month")) {
                nameToDisplay = "Month";
//          partialName= new SimpleDateFormat("MMM").format(d);
                fullName = partialName + "'" + dates[2].substring(2, 4);
            } else if (timeinfo.get(3).equalsIgnoreCase("Qtr")) {
                int month = Integer.parseInt(new SimpleDateFormat("M").format(d));
                nameToDisplay = "Quarter";
                if (month >= 1 && month <= 3) {
                    partialName = "Q4";
                } else if (month >= 4 && month <= 6) {
                    partialName = "Q1";
                } else if (month >= 7 && month <= 9) {
                    partialName = "Q2";
                } else if (month >= 10 && month <= 12) {
                    partialName = "Q3";
                }
                fullName = partialName + "-" + dates[2].substring(0, 4);
            } else {
            }
            if (!(from == null && from.equalsIgnoreCase("")) && !from.equalsIgnoreCase("fromtab")) {
                sb.append("<div id='timeinfo' name ='Time Info' title='Time Info'style='position:absolute;width:50px;left:600px;top:200px;'><table border='1' width='100px' class='mycls' cellspacing='0' cellpadding='0' ><tr class='navtitle-hover'align='right'onmousemove=divMove('timeinfo')  id='" + repId + "tr'><td align='right' id='titleBar'  style='cursor:move' width='80%' id='" + repId + "td'><a class='ui-icon ui-icon-close' onclick=closeDashSpan('" + repId + "')></a></td></tr><tr><td>");
            }
            sb.append("<table><tr style=\"width:100px\"><td align=\"left\" styel=\"width:85px\">Duration</td><td>:</td><td align=\"left\" style=\"color:red ;width:15px\">" + nameToDisplay + "</td></tr>");
            if (!timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {
                sb.append("<tr><td align=\"left\" style=\"color:red ;width:85px\">" + nameToDisplay + "</td><td>:</td><td align=\"left\" style=\"widht:15px\">" + fullName + "</td></tr>");
            }
            sb.append("<tr style=\"width:100px\"><td align=\"left\" styel=\"width:85px\">From Date </td><td>:</td><td align=\"left\" style=\"widht:15px\">" + vals1[0].substring(0, 10) + "</td></tr>");
            sb.append("<tr style=\"width:100px\" ><td align=\"left\" styel=\"width:85px\">To Date </td><td>:</td><td align=\"left\" style=\"widht:15px\">" + vals1[1].substring(0, 11) + "</td></tr>");

            String[] datevals1 = vals1[2].split(" ");
            String[] datevals = new String[10];
            int mymonth = 0;
            int decval = 0;
            if (datevals1[1].contains("/")) {
                datevals = datevals1[1].split("/");
                mymonth = Integer.parseInt(datevals[0]);
                decval = mymonth - 1;
            } else {
                String[] myvals = datevals1[1].split("-");

                datevals[2] = myvals[0];
                datevals[1] = myvals[2];
                mymonth = Integer.parseInt(datevals[1]);
                decval = mymonth - 1;
            }

            Calendar ca11 = Calendar.getInstance();
            ca11.set(Integer.parseInt(datevals[2].substring(0, 4)), decval, Integer.parseInt(datevals[1]));
//        java.util.Date d1 = new java.util.Date(ca11.getTimeInMillis());
            d = new java.util.Date(ca11.getTimeInMillis());
            partialName = new SimpleDateFormat("MMM").format(d);
            fullName = partialName + "'" + datevals[2].substring(2, 4);

            if (timeinfo.get(3).equalsIgnoreCase("Month")) {
                fullName = partialName + "'" + datevals[2].substring(2, 4);
            } else if (timeinfo.get(3).equalsIgnoreCase("Qtr")) {
                int month = Integer.parseInt(new SimpleDateFormat("M").format(d));
                nameToDisplay = "Quarter";
                if (month >= 1 && month <= 3) {
                    partialName = "Q4";
                } else if (month >= 4 && month <= 6) {
                    partialName = "Q1";
                } else if (month >= 7 && month <= 9) {
                    partialName = "Q2";
                } else if (month >= 10 && month <= 12) {
                    partialName = "Q3";
                }
                fullName = partialName + "-" + datevals[2].substring(0, 4);
            } else {
            }
            if (timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")) {
                sb.append("<tr style=\"width:100px\"><td align=\"left\" styel=\"width:85px\">Compare Dates</td>");
            } else {
                sb.append("<tr style=\"width:100px\"><td  align=\"left\" styel=\"width:85px\">Compare</td><td>:</td><td align=\"left\" style=\"widht:15px\">" + fullName + "</td></tr>");
            }
            sb.append("<tr style=\"width:100px\"><td align=\"left\" styel=\"width:85px\">From Date </td><td>:</td><td align=\"left\" style=\"widht:15px\">" + vals1[2].substring(0, 11) + "</td></tr>");
            sb.append("<tr style=\"width:100px\"><td align=\"left\" styel=\"width:85px\">To Date </td><td>:</td><td align=\"left\" style=\"widht:15px\">" + vals1[3].substring(0, 11) + "</td></tr>");
            sb.append("<tr style=\"width:100px\" ><td align=\"left\" styel=\"width:85px\">Updated On</td><td>:</td><td align=\"left\" style=\"widht:15px\">" + repUpdatedate + "</td></tr>");
            sb.append("</table>");
            sb.append("</td></tr></table></div>");
        }
        PrintWriter out = response.getWriter();
        out.print(sb);
        return null;
    }

    public ActionForward hideTdDash(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String reportId = request.getParameter("repId");
        String hidelefttd = request.getParameter("block");
        String tdtype = request.getParameter("tdType");
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        dao.updateHideLeftTdStatus(reportId, hidelefttd, tdtype);
        return null;

    }

    public ActionForward getLeftTdStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String reportId = request.getParameter("repId");
        String tdtype = request.getParameter("tdtype");
        String repLeftTdStatus = null;
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        repLeftTdStatus = dao.getLeftTdStatus(reportId, tdtype);
        PrintWriter out = response.getWriter();
        out.print(repLeftTdStatus);

        return null;

    }

    public ActionForward clearTargetForKpiTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String kpiMasterId = "";
        String elementId = "";
        String reportId = "";
        String dashletId = "";
        if (session != null) {
            try {
                kpiMasterId = request.getParameter("masterKpiId");
                elementId = request.getParameter("elementId");
                reportId = request.getParameter("reportId");
                dashletId = request.getParameter("dashletId");

                HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
                Container container = (Container) map.get(reportId);

                pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
                DashletDetail dashlet = collect.getDashletFromKPIMaster(kpiMasterId);
                KPI kpiDetails = (KPI) dashlet.getReportDetails();
                kpiDetails.clearKPITarget(elementId);

                DashboardTemplateDAO dbrdDAO = new DashboardTemplateDAO();
                dbrdDAO.deleteDbrdTarget(elementId, dashletId, reportId, kpiMasterId);

            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }

        } else {
            return mapping.findForward("sessionExpired");
        }

        return null;

    }

    public ActionForward saveDbrdUrl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String reportId = request.getParameter("repId");
        String repName = "";
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        repName = dao.getReportname(reportId);
        String url = "dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + reportId + "&pagename=" + repName + "&editDbrd=" + false;
        request.setAttribute("dashboardurl", url);
        return null;

    }

    public ActionForward getViewbysfromReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String drillrepId = request.getParameter("drillrepId");
        String elementId = request.getParameter("elementId");
        String drillreptype = request.getParameter("drillReptype");
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        HashMap vewbyhashmap = new HashMap();
        String currentviewby = "";
        String currentviewbyvalues = "";//CBOARP values
        vewbyhashmap = dao.getReportviewbys(drillrepId);
        currentviewby = dao.getCurrentViewbysofRep(drillrepId);//CBOVIEWBY values
        currentviewbyvalues = dao.getViewbyvaluesorep(drillrepId);//CBOARP values

        ArrayList<String> viewbynames = new ArrayList<String>();
        ArrayList<String> viewbyids = new ArrayList<String>();
        ArrayList<String> drillviewbynames = new ArrayList<String>();
        ArrayList<String> drillviewbyids = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();

        String viewbynamesstr = "";
        String viewbyidsstr = "";
        String[] viewbynamesstrarr = null;
        String[] viewbyidsstrarr = null;
        String Vals = null;
        String[] ValsList = null;
        String[] idValsList = null;
        String[] nameValsList = null;

        KPI kpiDetails = new KPI();
//        String drillViews = kpiDetails.getDrillViewBys();
//          
        viewbynamesstr = vewbyhashmap.get("viewbynames").toString();
        viewbyidsstr = vewbyhashmap.get("viewbyids").toString();
        viewbynamesstrarr = viewbynamesstr.split(",");
        viewbyidsstrarr = viewbyidsstr.split(",");
        for (int i = 0; i < viewbynamesstrarr.length; i++) {
            viewbynames.add(viewbynamesstrarr[i]);
            viewbyids.add(viewbyidsstrarr[i]);
        }
        drillviewbynames = viewbynames;
        drillviewbyids = viewbyids;
        String qry = "select DRILL_VIEW_BYS from PRG_AR_KPI_DETAILS where REF_REPORT_ID = " + drillrepId + " and ELEMENT_ID = " + elementId;
        PbDb pbdb = new PbDb();
        PbReturnObject drillViewBysRetObj = new PbReturnObject();
        try {
            drillViewBysRetObj = pbdb.execSelectSQL(qry);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (drillViewBysRetObj != null && drillViewBysRetObj.getFieldValueString(0, "DRILL_VIEW_BYS") != null && !drillViewBysRetObj.getFieldValueString(0, "DRILL_VIEW_BYS").equalsIgnoreCase("")) {
            Vals = drillViewBysRetObj.getFieldValueString(0, "DRILL_VIEW_BYS");
            if (!Vals.equalsIgnoreCase("(null)")) {
                if (Vals != null && !Vals.equalsIgnoreCase("")) {
                    ValsList = Vals.split(":");
                    idValsList = ValsList[0].split(",");
                    nameValsList = ValsList[1].split(",");
                    viewbynames = new ArrayList<String>();
                    viewbyids = new ArrayList<String>();
                    for (int i = 0; i < idValsList.length; i++) {
                        viewbynames.add(nameValsList[i].replace("{", "").replace("}", "").replace("\"", ""));
                        viewbyids.add(idValsList[i].replace("{", "").replace("}", "").replace("\"", ""));
                    }
                }
            }
        }
        sb.append("<table width=\"100%\">");

        for (int i = 0; i < viewbynames.size(); i++) {
            String viewby = viewbynames.get(i).replace("[", "");
            String viewby1 = viewby.replace("]", "");
            sb.append("<tr>");
            sb.append("<Td width=\"70%\" align=\"left\"><a href=\"javascript:drillToRepWithVieBy('" + viewbyids.get(i) + "','" + drillrepId + "','" + currentviewby + "','" + currentviewbyvalues + "','" + elementId + "','" + drillreptype + "')\">");
            sb.append("" + viewby1 + "");
            sb.append("</a></td>");
            sb.append("</tr>");
//            sb.append("<tr>");
//            sb.append("&nbsp;");
//            sb.append("</tr>");

        }
        sb.append("<tr>");
        sb.append("<td><input align=\"right\" onclick=\"getAllViewBysAfterDrill('" + drillrepId + "','" + drillviewbyids + "','" + drillviewbynames + "','" + elementId + "')\"  type=\"button\" value=\"Edit\" name=\"Edit\"></td>");
        sb.append("</tr>");
        sb.append("</table>");

        PrintWriter out = response.getWriter();
        out.print(sb);
        return null;

    }

    public ActionForward getViewbyURL(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String reportId = request.getParameter("repId");
        String viewbyId = request.getParameter("viewbyId");
        String currentviewbyid = request.getParameter("currentviewbyid");
        String[] currentViewByarr = currentviewbyid.split(",");
        String Cboarpvalues = request.getParameter("Cboarpvalues");
        String drillrepType = request.getParameter("drillreptype");
        String dashboardId = request.getParameter("dashboardId");

        viewbyId = viewbyId.replace("[", "");
        String tempURL = "";

        String url = "";
        if (drillrepType != null && drillrepType.equalsIgnoreCase("D")) {
            url = request.getContextPath() + "/dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + reportId + "&CBOVIEW_BY" + currentViewByarr[0].trim() + "=" + viewbyId.trim() + "&drillViewCheck=true" + "&drillfromrepId=" + dashboardId;
        } else {
            url = request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&CBOVIEW_BY" + currentViewByarr[0].trim() + "=" + viewbyId.trim() + "&drillViewCheck=true" + "&drillfromrepId=" + dashboardId;
        }
        PrintWriter out = response.getWriter();

        out.print(url);
        return null;

    }

    public ActionForward setMtdQtdYtdStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        String dbrdId = request.getParameter("dashboardId");
        String dashletId = request.getParameter("dashletId");
        boolean showMTD = Boolean.parseBoolean(request.getParameter("mtdSelect"));
        boolean showQTD = Boolean.parseBoolean(request.getParameter("qtdSelect"));
        boolean showYTD = Boolean.parseBoolean(request.getParameter("ytdSelect"));
        boolean showCURRENT = Boolean.parseBoolean(request.getParameter("currentSelect"));

        Container container = null;
        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(dbrdId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail detail = collect.getDashletDetail(dashletId);
        KPI kpiDetails = (KPI) detail.getReportDetails();
        kpiDetails.setMtdQtdYtdViewStatus(showMTD, showQTD, showYTD, showCURRENT);

        return null;
    }

    public List<String> getTextKPIInsertQueries(String dashboardId, String dashboardName, int dispSeq, DashletDetail dashlet, boolean sqlServer, boolean MySql, int row, int col, int rowSpan, int colSpan, String rowViewby) {
        List<String> queries = new ArrayList<String>();

        GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
        PbDb pbdb = new PbDb();

        String graphId = null;

        try {
            if (sqlServer) {
                String queryForMasterSequence = "select ident_current('PRG_AR_GRAPH_MASTER') ";
                PbReturnObject getMasterSeqDetsObj = pbdb.execSelectSQL(queryForMasterSequence);
                graphId = getMasterSeqDetsObj.getFieldValueString(0, 0);
            }
            if (MySql) {
                String queryForMasterSequence = "select LAST_INSERT_ID(GRAPH_ID) from PRG_AR_GRAPH_MASTER order by 1 desc limit 1 ";
                PbReturnObject getMasterSeqDetsObj = pbdb.execSelectSQL(queryForMasterSequence);
                graphId = getMasterSeqDetsObj.getFieldValueString(0, 0);
            } else {
                String queryForMasterSequence = "select PRG_AR_GRAPH_MASTER_SEQ.nextval from dual";
                PbReturnObject getMasterSeqDetsObj = pbdb.execSelectSQL(queryForMasterSequence);
                graphId = getMasterSeqDetsObj.getFieldValueString(0, 0);
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        String graphMasterQry = "";
        if (sqlServer || MySql) {

            graphMasterQry = "INSERT INTO PRG_AR_GRAPH_MASTER(REPORT_ID,GRAPH_NAME,GRAPH_FAMILY,GRAPH_TYPE,GRAPH_ORDER,GRAPH_SIZE,GRAPH_HEIGHT,GRAPH_WIDTH,ALLOW_LINK,ALLOW_LABEL,ALLOW_LEGEND,ALLOW_TOOLTIP,GRAPH_CLASS,"
                    + "LEFT_Y_AXIS_LABEL,RIGHT_Y_AXIS_LABEL,X_AXIS_LABEL,FONT_NAME,FONT_SIZE,FONT_COLOR,LEGEND_LOC,SHOW_GRID_X_AXIS,SHOW_GRID_Y_AXIS,BACK_COLOR,SHOW_DATA,ROW_VALUES,SHOW_GT,SHOW_TABLE,GRAPH_PROPERTY_XML,GRAPH_DISPLAY_ROWS,TIME_SERIES) "
                    + "values(" + "&,'&','&',&,&,&,'&','&','&','&','&','&',&,'&','&','&','&',&,'&','&','&','&','&','&','&','&','&','&','&','&')";
        } else {
            graphMasterQry = "INSERT INTO PRG_AR_GRAPH_MASTER(GRAPH_ID,REPORT_ID,GRAPH_NAME,GRAPH_FAMILY,GRAPH_TYPE,GRAPH_ORDER,GRAPH_SIZE,GRAPH_HEIGHT,GRAPH_WIDTH,ALLOW_LINK,ALLOW_LABEL,ALLOW_LEGEND,ALLOW_TOOLTIP,GRAPH_CLASS,"
                    + "LEFT_Y_AXIS_LABEL,RIGHT_Y_AXIS_LABEL,X_AXIS_LABEL,FONT_NAME,FONT_SIZE,FONT_COLOR,LEGEND_LOC,SHOW_GRID_X_AXIS,SHOW_GRID_Y_AXIS,BACK_COLOR,SHOW_DATA,ROW_VALUES,SHOW_GT,SHOW_TABLE,GRAPH_PROPERTY_XML,GRAPH_DISPLAY_ROWS,TIME_SERIES) "
                    + "values(" + graphId + ",&,'&','&',&,&,&,'&','&','&','&','&','&',&,'&','&','&','&',&,'&','&','&','&','&','&','&','&','&','&','&','&')";
        }

        Object[] objArr = new Object[30];
        objArr[0] = dashboardId;
        objArr[1] = dashlet.getDashletName();
        objArr[2] = graphDetails.getGraphFamily();
        objArr[3] = graphDetails.getGraphType();
        objArr[4] = graphDetails.getGraphOrder();
        objArr[5] = graphDetails.getGraphSize();
        objArr[6] = graphDetails.getGraphHeight();
        objArr[7] = graphDetails.getGraphWidth();
        if (graphDetails.isLinkAllowed()) {
            objArr[8] = "Y";
        } else {
            objArr[8] = "N";
        }
//            label, legen, tooltip, class
        if (graphDetails.isLabelAllowed()) {
            objArr[9] = "Y";
        } else {
            objArr[9] = "N";
        }
        if (graphDetails.isLegendAllowed()) {
            objArr[10] = "Y";
        } else {
            objArr[10] = "N";
        }
        if (graphDetails.isTooltipAllowed()) {
            objArr[11] = "Y";
        } else {
            objArr[11] = "N";
        }
        objArr[12] = graphDetails.getGraphClass();
        objArr[13] = graphDetails.getLeftYAxisLabel();
        objArr[14] = graphDetails.getRightYAxisLabel();
        objArr[15] = graphDetails.getXAxisLabel();
        objArr[16] = graphDetails.getFontName();
        objArr[17] = graphDetails.getFontSize();
        objArr[18] = graphDetails.getFontColor();
        objArr[19] = graphDetails.getLegendLocation();
        if (graphDetails.isShowXAxisGrid()) {
            objArr[20] = "Y";
        } else {
            objArr[20] = "N";
        }
        if (graphDetails.isShowYAxisGrid()) {
            objArr[21] = "Y";
        } else {
            objArr[21] = "N";
        }
        objArr[22] = graphDetails.getBackgroundColor();
        if (graphDetails.isShowData()) {
            objArr[23] = "Y";
        } else {
            objArr[23] = "N";
        }
        objArr[24] = graphDetails.getRowValues();
        if (graphDetails.isShowGT()) {
            objArr[25] = "Y";
        } else {
            objArr[25] = "N";
        }
        if (graphDetails.isShowAsTable()) {
            objArr[26] = "Y";
        } else {
            objArr[26] = "N";
        }
        objArr[27] = graphDetails.getGraphProperty().toXml();
        if (graphDetails.getDisplayRows() == null || graphDetails.getDisplayRows().equalsIgnoreCase("")) {
            objArr[28] = 10;
        } else {
            objArr[28] = graphDetails.getDisplayRows();
        }
        objArr[29] = String.valueOf(graphDetails.isTimeSeries());

        String finalQuery = pbdb.buildQuery(graphMasterQry, objArr);
        queries.add(finalQuery);

        List<QueryDetail> queryDetails = graphDetails.getQueryDetails();
        queries.addAll(getQueryDetailsInsertQueries(dashboardId, queryDetails, sqlServer, MySql));

        for (int i = 0; i < queryDetails.size(); i++) {
            String grpDetsQry = "";
            QueryDetail qd = queryDetails.get(i);
            String axisGr = graphDetails.getAxis();
            if (axisGr != null) {
                if (axisGr.equalsIgnoreCase("")) {
                    axisGr = null;
                }
            }
            if (sqlServer) {
                grpDetsQry = "INSERT INTO PRG_AR_GRAPH_DETAILS(GRAPH_ID,COL_NAME,ELEMENT_ID,COLUMN_ORDER,QUERY_COL_ID,AXIS) "
                        + "values( ident_current('PRG_AR_GRAPH_MASTER'),'" + qd.getDisplayName() + "'," + qd.getElementId() + ","
                        + "" + i + ", null," + axisGr + ")";
            }
            if (MySql) {
                grpDetsQry = "INSERT INTO PRG_AR_GRAPH_DETAILS(GRAPH_ID,COL_NAME,ELEMENT_ID,COLUMN_ORDER,QUERY_COL_ID,AXIS) "
                        + "values( (select LAST_INSERT_ID(GRAPH_ID) from PRG_AR_GRAPH_MASTER order by 1 desc limit 1),'" + qd.getDisplayName() + "'," + qd.getElementId() + ","
                        + "" + i + ", null," + axisGr + ")";
            } else {
                grpDetsQry = "INSERT INTO PRG_AR_GRAPH_DETAILS(GRAPH_COL_ID,GRAPH_ID,COL_NAME,ELEMENT_ID,COLUMN_ORDER,QUERY_COL_ID,AXIS) "
                        + "values(PRG_AR_GRAPH_DETAILS_SEQ.nextval," + graphId + ",'" + qd.getDisplayName() + "'," + qd.getElementId() + ","
                        + "" + i + ", null," + axisGr + ")";
            }
            queries.add(grpDetsQry);
        }

        String dispType = dashlet.getDisplayType();
        String tabName = dashlet.getkpiName();
        String dashletId = dashlet.getDashBoardDetailId();

        DashboardViewerDAO viewerDao = new DashboardViewerDAO();
        if (dashletPropertiesHelper == null) {
            dashletPropertiesHelper = viewerDao.getDashletPropertiesHelperObject(dashletId);
        }
        Gson gson = new Gson();
        String propertiesString = "";
        if (dashletPropertiesHelper != null) {
            propertiesString = gson.toJson(dashletPropertiesHelper);
        }
//         String[] tabProperties =

        String kpiTableColorGrp = "";
        if (dashlet.getDashbrdTableColor() != null) {
            List<DashboardTableColorGroupHelper> colorGroupHelperrs = dashlet.getDashbrdTableColor();

            if (!colorGroupHelperrs.isEmpty()) {
                kpiTableColorGrp = gson.toJson(colorGroupHelperrs);
            }
        }
        String grp1Query = "";
        if (sqlServer) {
            grp1Query = "insert into PRG_AR_DASHBOARD_DETAILS  (DASHBOARD_ID, REF_REPORT_ID, GRAPH_ID, KPI_MASTER_ID, DISPLAY_SEQUENCE, DISPLAY_TYPE, KPI_HEADING,ROW_1,COL_1,ROW_SPAN,COL_SPAN,KPI_NAME,DASHLET_PROPERTIES,DASHLET_KPIANDTABLE_PROP,ROW_VIEW_BY) values (" + dashboardId + "," + dashlet.getRefReportId() + ",ident_current('PRG_AR_GRAPH_MASTER'),0," + dispSeq + ",'" + dispType + "',null,'" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "','" + tabName + "','" + propertiesString + "','" + kpiTableColorGrp + "','" + dashlet.getRowViewBy() + "')";
        }
        if (MySql) {
            grp1Query = "insert into PRG_AR_DASHBOARD_DETAILS  (DASHBOARD_ID, REF_REPORT_ID, GRAPH_ID, KPI_MASTER_ID, DISPLAY_SEQUENCE, DISPLAY_TYPE, KPI_HEADING,ROW_1,COL_1,ROW_SPAN,COL_SPAN,KPI_NAME,DASHLET_PROPERTIES,DASHLET_KPIANDTABLE_PROP,ROW_VIEW_BY) values (" + dashboardId + "," + dashlet.getRefReportId() + ",(select LAST_INSERT_ID(GRAPH_ID) from PRG_AR_GRAPH_MASTER order by 1 desc limit 1),0," + dispSeq + ",'" + dispType + "',null,'" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "','" + tabName + "','" + propertiesString + "','" + kpiTableColorGrp + "','" + dashlet.getRowViewBy() + "')";
        } else {
            grp1Query = "insert into PRG_AR_DASHBOARD_DETAILS  (DASHBOARD_DETAILS_ID, DASHBOARD_ID, REF_REPORT_ID, GRAPH_ID, KPI_MASTER_ID, DISPLAY_SEQUENCE, DISPLAY_TYPE, KPI_HEADING,ROW_1,COL_1,ROW_SPAN,COL_SPAN,KPI_NAME,DASHLET_PROPERTIES,DASHLET_KPIANDTABLE_PROP,ROW_VIEW_BY) values (PRG_AR_DASHBOARD_DETS_SEQ.nextval," + dashboardId + "," + dashlet.getRefReportId() + "," + graphId + ",0," + dispSeq + ",'" + dispType + "','','" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "','" + tabName + "','" + propertiesString + "','" + kpiTableColorGrp + "','" + dashlet.getRowViewBy() + "')";
        }
        queries.add(grp1Query);

        ArrayList<String> textKpis = new ArrayList<String>();
        textKpis = dashlet.getTextKpis();
        String TextKpiQuery = "";
        String Comment = null;
        String TextkpiCommentqry = "";
        HashMap textkpidrill = (HashMap) dashlet.TextkpiDrill;
        ArrayList textkpidrillqueries = new ArrayList();
        for (String textkpi : textKpis) {
            String drillval = "0";
            String comment = null;
            if (textkpidrill != null && !textkpidrill.isEmpty()) {
                if (textkpidrill.get(textkpi) != null) {
                    drillval = (String) textkpidrill.get(textkpi);
                }
            }
            if (dashlet.TextkpiComment != null) {
                if (dashlet.TextkpiComment.get(textkpi) != null) {
                    comment = dashlet.TextkpiComment.get(textkpi).toString();
                }
            }
            if (sqlServer || MySql) {
                TextKpiQuery = "insert into PRG_TEXTKPI_DETAILS (DASHBOARD_ID,DASHLET_ID,TEXT_KPI_VALUE,DRILL_ID) values ('" + dashboardId + "',IDENT_CURRENT('PRG_AR_GRAPH_MASTER'),'" + textkpi + "','" + drillval + "')";
            } else {
                TextKpiQuery = "insert into PRG_TEXTKPI_DETAILS (DASHBOARD_ID,DASHLET_ID,TEXT_KPI_VALUE,DRILL_ID)values('" + dashboardId + "','" + graphId + "','" + textkpi + "','" + drillval + "')";
            }
            queries.add(TextKpiQuery);
//           if(sqlServer) {
//              // TextkpiCommentqry = "insert into prg_textkpi_user_comments(DASHBOARD_ID,DASHLET_ID,KPI_VALUE,KPI_COMMENT,COMMENT_DATE,USER_ID) values('"+dashboardId+"',ident_current(PRG_AR_DASHBOARD_DETS),'"+textkpi+"','"+comment+"',getDate(),41)";
//          }else{
//
//               TextkpiCommentqry = "insert into prg_textkpi_user_comments(COMMENT_ID,DASHBOARD_ID,DASHLET_ID,KPI_VALUE,KPI_COMMENT,COMMENT_DATE,USER_ID) values(PRG_TEXTKPI_USER_COMMENTS_SEQ.nextval,''"+dashboardId+"'','"+graphId+"','"+textkpi+"','"+comment+"',sysdate,'"+dashlet.Userid+"')";
//               queries.add(TextkpiCommentqry);
//           }

        }

        return queries;
    }

    public ActionForward saveTextKPIDrill(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String reportId = request.getParameter("REPORT_ID");
        String dashletId = request.getParameter("DASHLET_ID");
        String kpiMasterId = request.getParameter("KPI_MASTER_ID");
        String kpiType = request.getParameter("KPI_TYPE");
        Container container = Container.getContainerFromSession(request, reportId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail details = collect.getDashletDetail(dashletId);
        ArrayList<String> textkpis = details.getTextKpis();
        for (String textkpi : textkpis) {
            String targetReportId = request.getParameter(textkpi + "_report");
            details.addTextKPIDrill(textkpi, targetReportId);
        }
        PbDashboardViewerBD bd = new PbDashboardViewerBD();
        String result = bd.buildTextKpiTable(container, dashletId);

        PrintWriter out = response.getWriter();
        out.append(result);
        return null;
    }

    public ActionForward saveTextKpiComments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String paramvalue = request.getParameter("paramvalue");
        String commentText = request.getParameter("commentText");
        String reportid = request.getParameter("REPORTID");
        String dashletId = request.getParameter("dashletId");
        String userid = request.getParameter("userid");
        DashboardTemplateDAO textkpidao = new DashboardTemplateDAO();
        pbDashboardCollection collect = new pbDashboardCollection();


        textkpidao.saveTextKpiComments(paramvalue, commentText, reportid, dashletId, userid);
        Container container = Container.getContainerFromSession(request, reportid);
        collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail detail = collect.getDashletDetail(dashletId);
        detail.Userid = userid;
        detail.TextkpiComment.put(paramvalue, commentText);
        PbDashboardViewerBD viewerdao = new PbDashboardViewerBD();
        String result = viewerdao.buildTextKpiTable(container, dashletId);
        PrintWriter out = response.getWriter();
        out.print(result);


        return null;
    }

    public ActionForward refreshTextKpiCommentsPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String reportid = request.getParameter("REPORTID");
        String dashletId = request.getParameter("dashletId");
        Container container = Container.getContainerFromSession(request, reportid);
        PbDashboardViewerBD viewerdao = new PbDashboardViewerBD();
        viewerdao.buildTextKpiTable(container, dashletId);

        return null;
    }

    public ActionForward getAllViewBysAfterDrill(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String drillrepId = request.getParameter("repIdAfterDrill");
        String elementId = request.getParameter("elementId");
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        HashMap vewbyhashmap = new HashMap();
        String currentviewby = "";
        String currentviewbyvalues = "";//CBOARP values
        vewbyhashmap = dao.getReportviewbys(drillrepId);
        currentviewby = dao.getCurrentViewbysofRep(drillrepId);
        currentviewbyvalues = dao.getViewbyvaluesorep(drillrepId);//CBOARP values

        ArrayList<String> viewbynames = new ArrayList<String>();
        ArrayList<String> viewbyids = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();

        String viewbynamesstr = "";
        String viewbyidsstr = "";
        String[] viewbynamesstrarr;
        String[] viewbyidsstrarr;


        viewbynamesstr = vewbyhashmap.get("viewbynames").toString();
        viewbyidsstr = vewbyhashmap.get("viewbyids").toString();
        viewbynamesstrarr = viewbynamesstr.split(",");
        viewbyidsstrarr = viewbyidsstr.split(",");
        for (int i = 0; i < viewbynamesstrarr.length; i++) {
            viewbynames.add(viewbynamesstrarr[i]);
            viewbyids.add(viewbyidsstrarr[i]);
        }
        sb.append("<table width=\"100%\">");
        for (int i = 0; i < viewbynames.size(); i++) {
            String viewby = viewbynames.get(i).replace("[", "");
            String viewby1 = viewby.replace("]", "");
            String viewbyId = viewbyids.get(i).replace("[", "");
            String viewby1Id = viewbyId.replace("]", "");
            sb.append("<tr id=\"" + viewby1Id + "\" >");
            sb.append("<Td id=\"" + viewby1Id.concat(viewby1) + "\" width=\"70%\" align=\"left\"><img align=\"middle\" SRC=\"" + request.getContextPath() + "/icons pinvoke/cross-small.png\" onclick=\"deleteColumn1('" + viewby1Id + "','" + viewby1 + "')\" BORDER=\"0\" title=\"Close ViewBy\" />");
            sb.append("" + viewby1 + "");
            sb.append("</a></td>");
            sb.append("</tr>");
            sb.append("<tr>");
            sb.append("&nbsp;");
            sb.append("</tr>");

        }
        sb.append("<tr>");
        sb.append("<td><input align=\"right\" onclick=\"updateViewBys('" + drillrepId + "','" + elementId + "')\" class=\"navtitle-hover\" type=\"button\" value=\"Done\" name=\"Done\"></td>");
        sb.append("</tr>");
        sb.append("</table>");

        PrintWriter out = response.getWriter();
        out.print(sb);
        return null;
    }

    public ActionForward getSelectedDrillViewBys(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String drillrepId = request.getParameter("drillrepId");
        String drillviewbyids = request.getParameter("drillviewbyids");
        String drillviewbynames = request.getParameter("drillviewbynames");
        String elementId = request.getParameter("elementId");
        HashMap<String, String> hmap = new HashMap<String, String>();
        HashMap<String, String> hmap1 = new HashMap<String, String>();
        hmap.put(drillviewbyids, drillviewbynames);
        Gson gson = new Gson();
        String jsonString = gson.toJson(hmap);
        DashboardTemplateDAO ddao = new DashboardTemplateDAO();
        int result = 0;
        result = ddao.updateDrillViewBys(drillrepId, jsonString, elementId);
        PrintWriter out = response.getWriter();
        out.print(result);
        return null;
    }

    // added by veena on jul6 2012
    public ActionForward getMoreKpisForTarget(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String foldersIds = "";
        foldersIds = request.getParameter("foldersIds");
        String kpiType = request.getParameter("kpiType");
        String dbrdId = request.getParameter("dashboardId");
        String kpiMasterId = request.getParameter("kpiMasterId");
        String dashletId = request.getParameter("dashletId");
        String elementId = request.getParameter("elementId");
        String targetElem = request.getParameter("targetelem");
        String targetElemName = request.getParameter("targetElemName");
        String tablistFlag = (String) request.getParameter("tableList");
        Container container = null;
        ReportTemplateDAO repdao = new ReportTemplateDAO();

        if (!dashletId.equalsIgnoreCase("0") || !kpiMasterId.equalsIgnoreCase("0")) {
            container = Container.getContainerFromSession(request, dbrdId);
            pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
            List<DashletDetail> dashletList = collect.dashletDetails;
            DashletDetail dashlet = Iterables.find(dashletList, DashletDetail.getDashletDetailPredicate(kpiMasterId));
            dashlet.setEditFlag(true);
        }
        DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
        String result = "";

        ArrayList Parameters = new ArrayList();
        if (container.getTableList() != null && !container.getTableList().isEmpty()) {
            ArrayList alist = new ArrayList();
            result = repdao.getMeasuresForReport(foldersIds, Parameters, request.getContextPath(), container.getTableList());
            container.setTableList(alist);
        } else if (tablistFlag != null && tablistFlag.equalsIgnoreCase("true")) {
            result = dashboardTemplateDAO.getKpis(foldersIds);
        }

        request.setAttribute("targetKpis", result);
        request.setAttribute("dbrdId", dbrdId);
        request.setAttribute("kpiType", kpiType);
        request.setAttribute("kpiMasterId", kpiMasterId);
        request.setAttribute("divId", dashletId);
        request.setAttribute("elementId", elementId);
        request.setAttribute("targetElem", targetElem);
        request.setAttribute("targetElemName", targetElemName);

        return mapping.findForward("TargetMappingkpis");
    }

    public ActionForward saveTargetMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String kpiIdstr = request.getParameter("Kpis");
        String kpinamesstr = request.getParameter("KpiNames");
        String dashletId = request.getParameter("dashletId");
        String kpimasterId = request.getParameter("kpiMasterId");
        String dashboardId = request.getParameter("dbrdId");
        String elementId = request.getParameter("elementId");
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        dao.saveKpiTargetMapping(kpiIdstr.replace(",", "").trim(), dashletId, kpimasterId, dashboardId, elementId);


        return null;

    }

    /**
     * @author srikanth.p for groupMeassure Insights
     *
     */
    public List<String> getGroupMeassureInsertQueries(String dashboardId, String dashboardName, int dispSeq, DashletDetail dashlet, boolean sqlServer, boolean MySql, int row, int col, int rowSpan, int colSpan, ArrayList rootParentsMapList) {
        List<String> queries = new ArrayList<String>();
        GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
        String groupId = dashlet.getGroupId();
        ArrayList rootParentsList = new ArrayList();

        StringBuilder insertIds = new StringBuilder();
        insertIds.append("G_" + groupId);

        PbDb pbdb = new PbDb();

        String graphId = null;
        for (int i = 0; i < rootParentsMapList.size(); i++) {
            HashMap rootParentsMap = (HashMap) rootParentsMapList.get(i);
            if (rootParentsMap.containsKey(groupId)) {
                rootParentsList = (ArrayList) rootParentsMap.get(groupId);
            }
        }

        try {
            if (sqlServer) {
                String queryForMasterSequence = "select ident_current('PRG_AR_GRAPH_MASTER') ";
                PbReturnObject getMasterSeqDetsObj = pbdb.execSelectSQL(queryForMasterSequence);
                graphId = getMasterSeqDetsObj.getFieldValueString(0, 0);
            } else if (MySql) {
                String queryForMasterSequence = "select LAST_INSERT_ID(GRAPH_ID) from PRG_AR_GRAPH_MASTER order by 1 desc limit 1";
                PbReturnObject getMasterSeqDetsObj = pbdb.execSelectSQL(queryForMasterSequence);
                graphId = getMasterSeqDetsObj.getFieldValueString(0, 0);
            } else {
                String queryForMasterSequence = "select PRG_AR_GRAPH_MASTER_SEQ.nextval from dual";
                PbReturnObject getMasterSeqDetsObj = pbdb.execSelectSQL(queryForMasterSequence);
                graphId = getMasterSeqDetsObj.getFieldValueString(0, 0);
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        String graphMasterQry = "";
        if (sqlServer || MySql) {

            graphMasterQry = "INSERT INTO PRG_AR_GRAPH_MASTER(REPORT_ID,GRAPH_NAME,GRAPH_FAMILY,GRAPH_TYPE,GRAPH_ORDER,GRAPH_SIZE,GRAPH_HEIGHT,GRAPH_WIDTH,ALLOW_LINK,ALLOW_LABEL,ALLOW_LEGEND,ALLOW_TOOLTIP,GRAPH_CLASS,"
                    + "LEFT_Y_AXIS_LABEL,RIGHT_Y_AXIS_LABEL,X_AXIS_LABEL,FONT_NAME,FONT_SIZE,FONT_COLOR,LEGEND_LOC,SHOW_GRID_X_AXIS,SHOW_GRID_Y_AXIS,BACK_COLOR,SHOW_DATA,ROW_VALUES,SHOW_GT,SHOW_TABLE,GRAPH_PROPERTY_XML,GRAPH_DISPLAY_ROWS,TIME_SERIES) "
                    + "values(" + "&,'&','&',&,&,&,'&','&','&','&','&','&',&,'&','&','&','&',&,'&','&','&','&','&','&','&','&','&','&','&','&')";
        } else {
            graphMasterQry = "INSERT INTO PRG_AR_GRAPH_MASTER(GRAPH_ID,REPORT_ID,GRAPH_NAME,GRAPH_FAMILY,GRAPH_TYPE,GRAPH_ORDER,GRAPH_SIZE,GRAPH_HEIGHT,GRAPH_WIDTH,ALLOW_LINK,ALLOW_LABEL,ALLOW_LEGEND,ALLOW_TOOLTIP,GRAPH_CLASS,"
                    + "LEFT_Y_AXIS_LABEL,RIGHT_Y_AXIS_LABEL,X_AXIS_LABEL,FONT_NAME,FONT_SIZE,FONT_COLOR,LEGEND_LOC,SHOW_GRID_X_AXIS,SHOW_GRID_Y_AXIS,BACK_COLOR,SHOW_DATA,ROW_VALUES,SHOW_GT,SHOW_TABLE,GRAPH_PROPERTY_XML,GRAPH_DISPLAY_ROWS,TIME_SERIES) "
                    + "values(" + graphId + ",&,'&','&',&,&,&,'&','&','&','&','&','&',&,'&','&','&','&',&,'&','&','&','&','&','&','&','&','&','&','&','&')";
        }

        Object[] objArr = new Object[30];
        objArr[0] = dashboardId;
        objArr[1] = dashlet.getDashletName();
        objArr[2] = graphDetails.getGraphFamily();
        objArr[3] = graphDetails.getGraphType();
        objArr[4] = graphDetails.getGraphOrder();
        objArr[5] = graphDetails.getGraphSize();
        objArr[6] = graphDetails.getGraphHeight();
        objArr[7] = graphDetails.getGraphWidth();
        if (graphDetails.isLinkAllowed()) {
            objArr[8] = "Y";
        } else {
            objArr[8] = "N";
        }
//            label, legen, tooltip, class
        if (graphDetails.isLabelAllowed()) {
            objArr[9] = "Y";
        } else {
            objArr[9] = "N";
        }
        if (graphDetails.isLegendAllowed()) {
            objArr[10] = "Y";
        } else {
            objArr[10] = "N";
        }
        if (graphDetails.isTooltipAllowed()) {
            objArr[11] = "Y";
        } else {
            objArr[11] = "N";
        }
        objArr[12] = graphDetails.getGraphClass();
        objArr[13] = graphDetails.getLeftYAxisLabel();
        objArr[14] = graphDetails.getRightYAxisLabel();
        objArr[15] = graphDetails.getXAxisLabel();
        objArr[16] = graphDetails.getFontName();
        objArr[17] = graphDetails.getFontSize();
        objArr[18] = graphDetails.getFontColor();
        objArr[19] = graphDetails.getLegendLocation();
        if (graphDetails.isShowXAxisGrid()) {
            objArr[20] = "Y";
        } else {
            objArr[20] = "N";
        }
        if (graphDetails.isShowYAxisGrid()) {
            objArr[21] = "Y";
        } else {
            objArr[21] = "N";
        }
        objArr[22] = graphDetails.getBackgroundColor();
        if (graphDetails.isShowData()) {
            objArr[23] = "Y";
        } else {
            objArr[23] = "N";
        }
        objArr[24] = graphDetails.getRowValues();
        if (graphDetails.isShowGT()) {
            objArr[25] = "Y";
        } else {
            objArr[25] = "N";
        }
        if (graphDetails.isShowAsTable()) {
            objArr[26] = "Y";
        } else {
            objArr[26] = "N";
        }
        objArr[27] = graphDetails.getGraphProperty().toXml();
        if (graphDetails.getDisplayRows() == null || graphDetails.getDisplayRows().equalsIgnoreCase("")) {
            objArr[28] = 10;
        } else {
            objArr[28] = graphDetails.getDisplayRows();
        }
        objArr[29] = String.valueOf(graphDetails.isTimeSeries());

        String finalQuery = pbdb.buildQuery(graphMasterQry, objArr);
        queries.add(finalQuery);

        List<QueryDetail> queryDetails = graphDetails.getQueryDetails();
        ArrayList singleMeasures = new ArrayList();
//        queries.addAll(getQueryDetailsInsertQueries(dashboardId, queryDetails, sqlServer));

        for (int i = 0; i < queryDetails.size(); i++) {
            String grpDetsQry = "";
            QueryDetail qd = queryDetails.get(i);
            String axisGr = graphDetails.getAxis();
            if (axisGr != null) {
                if (axisGr.equalsIgnoreCase("")) {
                    axisGr = null;
                }
            }
            if (!rootParentsList.contains(qd.getElementId())) {
                insertIds.append(",E_").append(qd.getElementId());
                singleMeasures.add(qd.getElementId());



//            if (sqlServer) {
//                  grpDetsQry = "INSERT INTO PRG_USER_ELEMENTS_HIERARCHY(PERENT_ELEMENT_ID,GROUP_ID,LEVEL_NO,ELEMENT_TYPE) VALUES("+qd.getElementId()+","+groupId+",1,'SM')";
//            } else {
//                grpDetsQry = "INSERT INTO PRG_USER_ELEMENTS_HIERARCHY(ELEMENT_HIERARCHY_ID,PERENT_ELEMENT_ID,GROUP_ID,LEVEL_NO,ELEMENT_TYPE) VALUES(PRG_USER_ELEMENTS_HIERA_SEQ.NEXTVAL,"+qd.getElementId()+","+groupId+",1,'SM')";
//            }
//            queries.add(grpDetsQry);
            }
        }


        String dispType = dashlet.getDisplayType();
        String tabName = dashlet.getkpiName();
        String dashletId = dashlet.getDashBoardDetailId();

        DashboardViewerDAO viewerDao = new DashboardViewerDAO();
        if (dashletPropertiesHelper == null) {
            dashletPropertiesHelper = viewerDao.getDashletPropertiesHelperObject(dashletId);
        }


        Gson gson = new Gson();
        String propertiesString = "";
        if (dashletPropertiesHelper != null) {
            propertiesString = gson.toJson(dashletPropertiesHelper);
        }
//         String[] tabProperties =

        String kpiTableColorGrp = "";
        if (dashlet.getDashbrdTableColor() != null) {
            List<DashboardTableColorGroupHelper> colorGroupHelperrs = dashlet.getDashbrdTableColor();

            if (!colorGroupHelperrs.isEmpty()) {
                kpiTableColorGrp = gson.toJson(colorGroupHelperrs);
            }
        }
        String grp1Query = "";
        if (sqlServer) {
            grp1Query = "insert into PRG_AR_DASHBOARD_DETAILS  (DASHBOARD_ID, REF_REPORT_ID, GRAPH_ID, KPI_MASTER_ID, DISPLAY_SEQUENCE, DISPLAY_TYPE, KPI_HEADING,ROW_1,COL_1,ROW_SPAN,COL_SPAN,KPI_NAME,DASHLET_PROPERTIES,DASHLET_KPIANDTABLE_PROP,ROW_VIEW_BY,GROUPID) values (" + dashboardId + "," + dashlet.getRefReportId() + ",ident_current('PRG_AR_GRAPH_MASTER'),0," + dispSeq + ",'" + dispType + "',null,'" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "','" + tabName + "','" + propertiesString + "','" + kpiTableColorGrp + "',null,'" + insertIds.toString() + "')";
        } else if (MySql) {
            grp1Query = "insert into PRG_AR_DASHBOARD_DETAILS  (DASHBOARD_ID, REF_REPORT_ID, GRAPH_ID, KPI_MASTER_ID, DISPLAY_SEQUENCE, DISPLAY_TYPE, KPI_HEADING,ROW_1,COL_1,ROW_SPAN,COL_SPAN,KPI_NAME,DASHLET_PROPERTIES,DASHLET_KPIANDTABLE_PROP,ROW_VIEW_BY,GROUPID) values (" + dashboardId + "," + dashlet.getRefReportId() + ",(select LAST_INSERT_ID(GRAPH_ID) from PRG_AR_GRAPH_MASTER order by 1 desc limit 1),0," + dispSeq + ",'" + dispType + "',null,'" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "','" + tabName + "','" + propertiesString + "','" + kpiTableColorGrp + "',null,'" + insertIds.toString() + "')";
        } else {
            grp1Query = "insert into PRG_AR_DASHBOARD_DETAILS  (DASHBOARD_DETAILS_ID, DASHBOARD_ID, REF_REPORT_ID, GRAPH_ID, KPI_MASTER_ID, DISPLAY_SEQUENCE, DISPLAY_TYPE, KPI_HEADING,ROW_1,COL_1,ROW_SPAN,COL_SPAN,KPI_NAME,DASHLET_PROPERTIES,DASHLET_KPIANDTABLE_PROP,ROW_VIEW_BY,GROUPID) values (PRG_AR_DASHBOARD_DETS_SEQ.nextval," + dashboardId + "," + dashlet.getRefReportId() + "," + graphId + ",0," + dispSeq + ",'" + dispType + "','','" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "','" + tabName + "','" + propertiesString + "','" + kpiTableColorGrp + "',null,'" + insertIds.toString() + "')";
        }
        queries.add(grp1Query);
        //belowe code is written for drilling to report in groupmeasure
        String selectQuery = "";
        if (sqlServer) {
            selectQuery = "SELECT ISNULL(CHILD_ELEMENT_ID,PERENT_ELEMENT_ID) ,PERENT_ELEMENT_ID FROM PRG_USER_ELEMENTS_HIERARCHY WHERE GROUP_ID=" + groupId;
        } else if (MySql) {
            selectQuery = "SELECT IFNULL(CHILD_ELEMENT_ID,PERENT_ELEMENT_ID) ,PERENT_ELEMENT_ID FROM PRG_USER_ELEMENTS_HIERARCHY WHERE GROUP_ID=" + groupId;
        } else {

            selectQuery = "SELECT NVL(CHILD_ELEMENT_ID,PERENT_ELEMENT_ID) ,PERENT_ELEMENT_ID FROM PRG_USER_ELEMENTS_HIERARCHY WHERE GROUP_ID=" + groupId;
        }

        try {
            PbReturnObject selRetObj = null;
            selRetObj = pbdb.execSelectSQL(selectQuery);
            String insertQuery = "";
            if (selRetObj != null) {
                for (int i = 0; i < selRetObj.rowCount; i++) {
                    if (sqlServer) {
                        insertQuery = "INSERT INTO PRG_AR_GRP_DRILL(Child_Element_Id,Parent_Element_Id,Group_Id,DASHBOARD_Id,Dashlet_Id) "
                                + "VALUES(" + selRetObj.getFieldValueString(i, 0) + "," + selRetObj.getFieldValueString(i, 1) + "," + groupId + "," + dashboardId + ",IDENT_CURRENT('PRG_AR_DASHBOARD_DETAILS'))";

                    } else {
                        insertQuery = "INSERT INTO PRG_AR_GRP_DRILL(Drill_Id,Child_Element_Id,Parent_Element_Id,Group_Id,DASHBOARD_Id,Dashlet_Id) "
                                + "VALUES(PRG_AR_GRP_DRILL_SEQ.NEXTVAL," + selRetObj.getFieldValueString(i, 0) + "," + selRetObj.getFieldValueString(i, 1) + "," + groupId + "," + dashboardId + ",PRG_AR_DASHBOARD_DETS_SEQ.currval)";
                    }
                    queries.add(insertQuery);
                }
                for (int j = 0; j < singleMeasures.size(); j++) {
                    if (sqlServer) {
                        insertQuery = "INSERT INTO PRG_AR_GRP_DRILL(Child_Element_Id,Group_Id,DASHBOARD_Id,Dashlet_Id) "
                                + "VALUES(" + singleMeasures.get(j) + "," + groupId + "," + dashboardId + ",IDENT_CURRENT('PRG_AR_DASHBOARD_DETAILS'))";

                    } else {
                        insertQuery = "INSERT INTO PRG_AR_GRP_DRILL(Drill_Id,Child_Element_Id,Group_Id,DASHBOARD_Id,Dashlet_Id) "
                                + "VALUES(PRG_AR_GRP_DRILL_SEQ.NEXTVAL," + singleMeasures.get(j) + "," + groupId + "," + dashboardId + ",PRG_AR_DASHBOARD_DETS_SEQ.currval)";
                    }
                    queries.add(insertQuery);
                }
            }

        } catch (Exception e) {
            logger.error("Exception:", e);
        }


        return queries;
    }

    public ActionForward getAllKpis(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String foldersIds = "";
        foldersIds = request.getParameter("foldersIds");
        String dbrdId = request.getParameter("dashboardId");
        String kpiType = request.getParameter("kpiType");
        String dashletId = request.getParameter("dashletId");
        String kpiMasterId = request.getParameter("kpiMasterId");
        String kpiidsStr = request.getParameter("idsStr");
        String kpinamesStr = request.getParameter("namesStr");
        request.setAttribute("dbrdId", dbrdId);
        request.setAttribute("kpiType", kpiType);
        request.setAttribute("kpiMasterId", kpiMasterId);
        request.setAttribute("divId", dashletId);
        request.setAttribute("kpiidsStr", kpiidsStr);
        request.setAttribute("kpinamesStr", kpinamesStr);
        return mapping.findForward("addallkpis");

    }

    public ActionForward saveTimeDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String timeDetails = request.getParameter("timeDetails");
        String timeDetsMap = request.getParameter("timeDetsMap");
        String PbReportId = request.getParameter("PbReportId");
        String date = request.getParameter("Date");
        String[] timeDetailsArray = timeDetails.split(",");
        String[] timeDetsMapArray = timeDetsMap.split("@");
        String[] values = null;
        String[] values1 = null;
        ReportTemplateDAO DAO1 = new ReportTemplateDAO();
        ArrayList timeDetailsAlist = new ArrayList();
        ArrayList timeparams = new ArrayList();
        ArrayList queries = new ArrayList();
        LinkedHashMap timeDetsMapAlist = new LinkedHashMap();
        String qry = "select REP_TIME_ID from PRG_AR_REPORT_TIME where REPORT_ID = " + Integer.parseInt(PbReportId);
        PbDb pbdb = new PbDb();
        LinkedHashMap modulesList = new LinkedHashMap();
        PbReturnObject roleIdResult1 = new PbReturnObject();
        PbReturnObject roleIdResult2 = new PbReturnObject();
        int value = 0;
        try {
            PbReturnObject roleIdResult = pbdb.execSelectSQL(qry);

            int k = 0;
            k = roleIdResult.getRowCount();
            if (k != 0) {
                k = k - 1;
            }
            value = roleIdResult.getFieldValueInt(k, "REP_TIME_ID");
            String finalqry1 = "select COLUMN_TYPE, DEFAULT_VALUE from PRG_AR_REPORT_TIME_DETAIL where REP_TIME_ID = " + value;
            String finalqry2 = "select DEFAULT_DATE from PRG_AR_REPORT_TIME_DETAIL where REP_TIME_ID = " + value;
            roleIdResult1 = pbdb.execSelectSQL(finalqry1);
            roleIdResult2 = pbdb.execSelectSQL(finalqry2);
            for (int i = 0; i < roleIdResult1.getRowCount(); i++) {
                modulesList.put(roleIdResult1.getFieldValueString(i, "COLUMN_TYPE"), roleIdResult1.getFieldValueString(i, "DEFAULT_VALUE") + "#" + roleIdResult2.getFieldValueDate(i, "DEFAULT_DATE"));
            }

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        ArrayList Alist = new ArrayList();
        for (int i = 0; i < timeDetailsArray.length; i++) {
            timeDetailsAlist.add(timeDetailsArray[i].replace("[", "").replace("]", "").replace(" ", ""));
        }
        for (int j = 0; j < timeDetsMapArray.length; j++) {
            ArrayList valsArray = new ArrayList();
            values = timeDetsMapArray[j].split("~");
            values1 = values[1].replace("[", "").replace("]", "").split(",");
            for (int k = 0; k < values1.length; k++) {
                valsArray.add(values1[k].replace(" ", ""));
            }
            timeDetsMapAlist.put(values[0], valsArray);
        }
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        //modified by Dinanath for updating timeinfo
        queries = dao.updateReportTimeDimensions(timeDetailsAlist, timeDetsMapAlist, Integer.parseInt(PbReportId), timeparams, Alist, date, modulesList, value);
        boolean result = DAO1.saveReport(queries);
//        PrintWriter out = response.getWriter();
//        out.print(result);
//        return null;
//        request.setAttribute("REPORTID",PbReportId);
        return null;
    }

    public ActionForward getMeasureGraphtest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HttpSession session = request.getSession(false);
        String measId = request.getParameter("measureId");
        String icalId = request.getParameter("icalId");
        String compareVal = request.getParameter("compareVal");
        String measName = request.getParameter("measureName");
        String roleId = request.getParameter("busroleId");
        int iMonth = Integer.parseInt(request.getParameter("iMonth")) + 1;
        String iYear = request.getParameter("iYear");
        String nodays = request.getParameter("days");
        String primaryMeasure = request.getParameter("primaryMeasure");
        // 
        boolean testView = Boolean.parseBoolean(request.getParameter("checkval"));
        // 
        //
//        String dateFormat = new String(Integer.toString(iMonth).concat("/").concat(nodays).concat("/").concat(iYear)); by Prabal
        String dateFormat = Integer.toString(iMonth).concat("/").concat(nodays).concat("/").concat(iYear);
        //
        StringBuilder sb = new StringBuilder();
        DashboardViewerDAO dao = new DashboardViewerDAO();
        PbReturnObject pbretObjForTime = null;
        String[] formatListVals = null;
        String[] roundListVals = null;
        String formatList = (String) session.getAttribute("formatList");
        String roundList = (String) session.getAttribute("roundList");
        String applyValues = (String) session.getAttribute("applyValues");

        request.setAttribute("icalhomeFlag", "false");
        request.setAttribute("icalId", 0);
        request.setAttribute("icalName", "");


        String mesName = measName + testView;
        //
        OnceViewContainer container = null;


        ArrayList rowview = new ArrayList();
        rowview.add("TIME");
        PbReportQuery timequery = new PbReportQuery();
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        List<String> measureIdVal = new ArrayList<String>();
        //measureIdVal.add(measId);
        LinkedHashMap lMap = new LinkedHashMap();
        LinkedHashMap priorlMap = new LinkedHashMap();
        LinkedHashMap lMap1 = new LinkedHashMap();
        String eltIds = (String) session.getAttribute("elementIds");
        String[] eltIdsArray = eltIds.split(",");
        for (int i = 0; i < eltIdsArray.length; i++) {
            measureIdVal.add(eltIdsArray[i]);
            // 
        }
        // 
        //
        //
        if (icalId == null) {
            icalId = (String) session.getAttribute("icalId");
        }
        if (icalId != null && !icalId.equalsIgnoreCase("")) {
            HashMap map2 = new HashMap();
            if (session.getAttribute("ICALDETAILS") != null) {
                map2 = (HashMap) session.getAttribute("ICALDETAILS");
                container = (OnceViewContainer) map2.get(icalId);
            }
        }
        if (container != null && compareVal != null && !compareVal.equalsIgnoreCase("")) {
            container.setComparisionType(compareVal);
        }
        if (container != null) {
            formatList = container.getFormatList();
            roundList = container.getRoundList();
            applyValues = container.getApplyFormatValues();
            compareVal = container.getComparisionType();
            if (primaryMeasure != null && !primaryMeasure.equalsIgnoreCase("")) {
                container.setPrimaryMeasure(primaryMeasure);
            }
        }
        if (formatList != null) {
            formatListVals = formatList.split(",");
            roundListVals = roundList.split(",");
        }


        String userId = String.valueOf(session.getAttribute("USERID"));
        List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal, new HashMap<String, String>());
        if (kpiElements != null) {
            for (KPIElement elem : kpiElements) {
                if (elem.getElementName() != null) {
                    QueryCols.add(elem.getElementId());
                }
                QueryAggs.add(elem.getAggregationType());
            }
        }

//            timequery.setQryColumns(QueryCols);
//            timequery.setColAggration(QueryAggs);
//            timequery.setParamValue(dashboardcollect.reportParametersValues);
//            timequery.setParamValue(dashboardcollect.reportParametersValues);
//            OnceViewContainer onecontainer1 = null;
//            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//            onecontainer1 = reportTemplateDAO.getOneViewData(oneViewId);
        ArrayList arl = new ArrayList();

//            if(onecontainer1!=null && !onecontainer1.timedetails.isEmpty()){
//                arl = (ArrayList) onecontainer1.timedetails;
//            }
//            else{
        arl.add("Day");
        arl.add("PRG_STD");
//                ProgenParam pramnam=new ProgenParam();
//                String date=pramnam.getdateforpage();
//                arl.add(date);
        arl.add(dateFormat);
        arl.add("Day");
        if (compareVal == null) {
            // 
            arl.add("Last Day");
        } else if (compareVal != null && !compareVal.equalsIgnoreCase("") && compareVal.equalsIgnoreCase("No Comparision")) {
            //
            arl.add("Last Day");
        } else {
            // 
            arl.add(compareVal);
        }
//            }

//                timequery.setRowViewbyCols(new ArrayList());
//                repQuery.setParamValue(collect.reportParametersValues);
        timequery.setRowViewbyCols(rowview);
        timequery.setColViewbyCols(new ArrayList());
        timequery.setColViewbyCols(new ArrayList());
        timequery.setQryColumns(QueryCols);
        timequery.setColAggration(QueryAggs);
        timequery.setTimeDetails(arl);
        timequery.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
        timequery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
        timequery.isTimeSeries = false;
        timequery.isTimeDrill = true;
//                repQuery.setReportId(collect.reportId);
        timequery.setBizRoles(roleId);
        timequery.setUserId(userId);
        pbretObjForTime = timequery.getPbReturnObject1(String.valueOf(QueryCols.get(0)));


//                ArrayList a1 = new ArrayList();
//                ArrayList<String> oneKeyElements = new ArrayList<String>();
//                Map<String,ArrayList<String>> measureMap = new HashMap<String,ArrayList<String>>();
//                if(session.getAttribute("Keys")!=null && session.getAttribute("KeyValues")!=null){

        if (pbretObjForTime != null) {
            if (pbretObjForTime.getRowCount() > Integer.parseInt(nodays)) {
                for (int j = 0; j < measureIdVal.size(); j++) {
                    ArrayList calendardata = new ArrayList();
                    ArrayList calendardata2 = new ArrayList();
                    ArrayList calendardata1 = new ArrayList();
                    String qry = "select ELEMENT_ID from prg_user_all_info_details where ref_element_id = " + measureIdVal.get(j) + " and ref_element_type=3";
                    PbDb pbdb = new PbDb();
                    PbReturnObject retObj = new PbReturnObject();
                    retObj = pbdb.execSelectSQL(qry);
                    for (int i = 0; i < pbretObjForTime.getRowCount(); i++) {
                        ArrayList QueryCols1 = new ArrayList();
                        ArrayList QueryAggs1 = new ArrayList();
                        List test = new LinkedList();
                        test.add(measureIdVal.get(j));
                        kpiElements = dao.getKPIElements(test, new HashMap<String, String>());
                        if (kpiElements != null) {
                            for (KPIElement elem : kpiElements) {
                                if (elem.getElementName() != null) {
                                    QueryCols1.add(elem.getElementId());
                                }
                                QueryAggs1.add(elem.getAggregationType());
                            }
                        }
                        double currVal = 0.0;
                        double datavalue = 0.0;
                        double priorvalue = 0.0;
                        String changeVal = "";
                        String dateVal = "";
                        String dateVal1 = "";
                        String[] vals1 = null;
                        String[] vals2 = null;
//                     String datavalue = pbretObjForTime.getFieldValueString(0, "A_".concat(QueryCols.get(0).toString()));
                        dateVal = pbretObjForTime.getFieldValueString(i, ("TIME"));
                        if (dateVal != null && !dateVal.equalsIgnoreCase("")) {
                            if (dateVal.contains("/")) {
                                vals1 = dateVal.split("/");
                            } else {
                                vals1 = dateVal.split("-");
                            }
                            if (i < pbretObjForTime.getRowCount() - 1) {
                                dateVal1 = pbretObjForTime.getFieldValueString(i + 1, ("TIME"));
                                if (dateVal1.contains("/")) {
                                    vals2 = dateVal1.split("/");
                                } else {
                                    vals2 = dateVal1.split("-");
                                }
                            }
                        }
                        //                     String datavalue = pbretObjForTime.getFieldValueString(0, "A_".concat(QueryCols.get(0).toString()));
                        if (retObj != null && retObj.getRowCount() > 0 && retObj.getFieldValueString(0, "ELEMENT_ID") != "" && retObj.getFieldValueString(0, "ELEMENT_ID") != null && !retObj.getFieldValueString(0, "ELEMENT_ID").equalsIgnoreCase("")) {
                            datavalue = Double.parseDouble((pbretObjForTime.getFieldValueString(i, ("A_" + retObj.getFieldValueString(0, "ELEMENT_ID")).toString())));
                        }
                        priorvalue = Double.parseDouble((pbretObjForTime.getFieldValueString(i, ("A_" + QueryCols1.get(1)).toString())));
                        if (datavalue < 0) {
                            changeVal = "N";
                        } else if (datavalue > 0) {
                            changeVal = "P";
                        } else if (datavalue == 0) {
                            changeVal = "V";
                        }
                        currVal = Double.parseDouble((pbretObjForTime.getFieldValueString(i, ("A_" + measureIdVal.get(j)).toString())));
                        int decimalPlaces = 1;
                        BigDecimal curval = new BigDecimal(currVal);
                        BigDecimal priorvalue1 = new BigDecimal(priorvalue);
                        BigDecimal val = curval;
                        curval = curval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                        NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
//                         mesureValues.add(formatter.format(curval));
                        for (int k = 0; k < Integer.parseInt(nodays); k++) {
                            String checkDt = "";
                            String text = "";
                            String text1 = "";
                            if (k < 9) {
                                text = ((k + 1) < 10 ? "0" : "") + (k + 1);
                            } else {
                                text = Integer.toString(k + 1);
                            }
                            text1 = ((iMonth) < 10 ? "0" : "") + (iMonth);
                            if (dateVal != null && dateVal.contains("/")) {
                                checkDt = (text1).concat("/").concat(text).concat("/").concat(iYear);
                            } else {
                                checkDt = (text1).concat("-").concat(text).concat("-").concat(iYear);
                            }
                            if (dateVal != null && dateVal.equalsIgnoreCase(checkDt)) {
                                if (formatListVals != null && roundListVals != null) {
                                    if (j < formatListVals.length && formatListVals[j] != null && !formatListVals[j].equalsIgnoreCase("")) {
                                        if (formatListVals[j].equalsIgnoreCase("A")) {
                                            formatListVals[j] = "";
                                        }
                                        NumberFormatter nf = new NumberFormatter();
                                        String formattedvalue = "";
                                        if (currVal != Double.parseDouble("0.0")) {
                                            formattedvalue = nf.getModifiedNumber(val, formatListVals[j], Integer.parseInt(roundListVals[j]));
                                        } else {
                                            formattedvalue = "0";
                                        }
                                        if (compareVal != null && !compareVal.equalsIgnoreCase("") && !compareVal.equalsIgnoreCase("No Comparision")) {
                                            calendardata.add(formattedvalue + changeVal);
                                            calendardata1.add(formattedvalue);
                                            calendardata2.add(priorvalue1);
                                        } else if (primaryMeasure != null && !primaryMeasure.equalsIgnoreCase("")) {
                                            calendardata.add(formattedvalue + changeVal);
                                            calendardata1.add(formattedvalue);
                                            calendardata2.add(priorvalue1);
                                        } else {
                                            calendardata.add(formattedvalue);
                                            calendardata1.add(formattedvalue);
                                            calendardata2.add(priorvalue1);
                                        }
                                    } else if (compareVal != null && !compareVal.equalsIgnoreCase("") && !compareVal.equalsIgnoreCase("No Comparision")) {
                                        calendardata.add(formatter.format(curval) + changeVal);
                                        calendardata1.add(formatter.format(curval));
                                        calendardata2.add(priorvalue1);
                                    } else {
                                        calendardata.add(formatter.format(curval));
                                        calendardata1.add(formatter.format(curval));
                                        calendardata2.add(priorvalue1);
                                    }
                                } else if (primaryMeasure != null && !primaryMeasure.equalsIgnoreCase("")) {
                                    calendardata.add(formatter.format(curval));
                                    calendardata1.add(formatter.format(curval));
                                    calendardata2.add(priorvalue1);
                                } else if (compareVal != null && !compareVal.equalsIgnoreCase("") && compareVal.equalsIgnoreCase("No Comparision")) {
                                    calendardata.add(formatter.format(curval));
                                    calendardata1.add(formatter.format(curval));
                                    calendardata2.add(priorvalue1);
                                } else {
                                    calendardata.add(formatter.format(curval) + changeVal);
                                    calendardata1.add(formatter.format(curval));
                                    calendardata2.add(priorvalue1);
                                }
//                    if (i != Integer.parseInt(nodays) - 1) {
//                        calendardata.add(",");
////                            a1.add(curval);
//
//                    }
                            }
                        }
                        if (i < pbretObjForTime.getRowCount()) {
                            if (vals1 != null && !vals1[1].equalsIgnoreCase("") && vals2 != null && !vals2[1].equalsIgnoreCase("")) {
                                int k = Integer.parseInt(vals2[1]) - Integer.parseInt(vals1[1]);
                                for (int a = 1; a < k; a++) {
                                    calendardata.add("0");
                                    calendardata2.add("0");
                                }
                            }
                        }

                    }
//                    if (primaryMeasure != null && !primaryMeasure.equalsIgnoreCase("") && measureIdVal != null) {
//                        int maxindex = 0;
//                            int minindex = 0;
//                        if (primaryMeasure.equals(measureIdVal.get(j))) {
//                            double max = Double.parseDouble(calendardata1.get(0).toString().replaceAll(",", ""));
//                            double min = Double.parseDouble(calendardata1.get(0).toString().replaceAll(",", ""));
//                            for (int ktr = 0; ktr < calendardata1.size(); ktr++) {
//                                if (Double.parseDouble(calendardata1.get(ktr).toString().replaceAll(",", "")) > max) {
//                                    max = Double.parseDouble(calendardata1.get(ktr).toString().replaceAll(",", ""));
//                                    maxindex = ktr;
//                                }
//                                if (Double.parseDouble(calendardata1.get(ktr).toString().replaceAll(",", "")) < min) {
//                                    min = Double.parseDouble(calendardata1.get(ktr).toString().replaceAll(",", ""));
//                                    minindex = ktr;
//                                }
//                            }
//                            calendardata.set(maxindex, "X"+calendardata.get(maxindex));
//                            calendardata.set(minindex, "Z"+calendardata.get(minindex));
//                        }
//                    }
                    if (primaryMeasure != null && !primaryMeasure.equalsIgnoreCase("")) {
                        lMap.put(measureIdVal.get(j) + "I", calendardata);
                        priorlMap.put(measureIdVal.get(j) + "I", calendardata2);
                    } else {
                        lMap.put(measureIdVal.get(j), calendardata);
                        priorlMap.put(measureIdVal.get(j), calendardata2);
                    }
                }
            } else {
                for (int j = 0; j < measureIdVal.size(); j++) {
                    ArrayList QueryCols1 = new ArrayList();
                    ArrayList QueryAggs1 = new ArrayList();
                    List test = new LinkedList();
                    test.add(measureIdVal.get(j));
                    kpiElements = dao.getKPIElements(test, new HashMap<String, String>());
                    if (kpiElements != null) {
                        for (KPIElement elem : kpiElements) {
                            if (elem.getElementName() != null) {
                                QueryCols1.add(elem.getElementId());
                            }
                            QueryAggs1.add(elem.getAggregationType());
                        }
                    }
                    String qry = "select ELEMENT_ID from prg_user_all_info_details where ref_element_id = " + measureIdVal.get(j) + " and ref_element_type=3";
                    PbDb pbdb = new PbDb();
                    PbReturnObject retObj = new PbReturnObject();
                    retObj = pbdb.execSelectSQL(qry);
                    // 
                    ArrayList calendardata = new ArrayList();
                    ArrayList calendardata2 = new ArrayList();
                    ArrayList calendardata1 = new ArrayList();
                    ArrayList changedata = new ArrayList();
                    for (int i = 0; i < pbretObjForTime.getRowCount(); i++) {
                        double currVal = 0.0;
                        double datavalue = 0.0;
                        double priorvalue = 0.0;
                        String changeVal = "";
                        String dateVal = "";
                        String dateVal1 = "";
                        String[] vals1 = null;
                        String[] vals2 = null;
//                     String datavalue = pbretObjForTime.getFieldValueString(0, "A_".concat(QueryCols.get(0).toString()));
                        dateVal = pbretObjForTime.getFieldValueString(i, ("TIME"));
                        if (dateVal != null && !dateVal.equalsIgnoreCase("")) {
                            if (dateVal.contains("/")) {
                                vals1 = dateVal.split("/");
                            } else {
                                vals1 = dateVal.split("-");
                            }
                            if (i < pbretObjForTime.getRowCount() - 1) {
                                dateVal1 = pbretObjForTime.getFieldValueString(i + 1, ("TIME"));
                                if (dateVal1.contains("/")) {
                                    vals2 = dateVal1.split("/");
                                } else {
                                    vals2 = dateVal1.split("-");
                                }
                            }
                        }
                        if (retObj != null && retObj.getRowCount() > 0 && retObj.getFieldValueString(0, "ELEMENT_ID") != null && retObj.getFieldValueString(0, "ELEMENT_ID") != "" && !retObj.getFieldValueString(0, "ELEMENT_ID").equalsIgnoreCase("")) {
                            datavalue = Double.parseDouble((pbretObjForTime.getFieldValueString(i, ("A_" + retObj.getFieldValueString(0, "ELEMENT_ID")).toString())));
                        }
                        // 
                        if (datavalue < 0) {
                            changeVal = "N";
                        } else if (datavalue > 0) {
                            changeVal = "P";
                        } else if (datavalue == 0) {
                            changeVal = "V";
                        }
                        if ((pbretObjForTime.getFieldValueString(i, ("A_" + measureIdVal.get(j)).toString())) != null && !(pbretObjForTime.getFieldValueString(i, ("A_" + measureIdVal.get(j)).toString())).equalsIgnoreCase("")) {
                            currVal = Double.parseDouble((pbretObjForTime.getFieldValueString(i, ("A_" + measureIdVal.get(j)).toString())));
                        }
                        priorvalue = Double.parseDouble((pbretObjForTime.getFieldValueString(i, ("A_" + QueryCols1.get(1)).toString())));
                        int decimalPlaces = 1;
                        BigDecimal curval = new BigDecimal(currVal);
                        BigDecimal priorvalue1 = new BigDecimal(priorvalue);
                        BigDecimal val = curval;
                        curval = curval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                        NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
//                         mesureValues.add(formatter.format(curval));
                        if (formatListVals != null && roundListVals != null) {
                            if (j < formatListVals.length && formatListVals[j] != null && !formatListVals[j].equalsIgnoreCase("")) {
                                if (formatListVals[j].equalsIgnoreCase("A")) {
                                    formatListVals[j] = "";
                                }
                                NumberFormatter nf = new NumberFormatter();
                                String formattedvalue = "";
                                if (currVal != Double.parseDouble("0.0")) {
                                    formattedvalue = nf.getModifiedNumber(val, formatListVals[j], Integer.parseInt(roundListVals[j]));
                                } else {
                                    formattedvalue = "0";
                                }
                                if (compareVal != null && !compareVal.equalsIgnoreCase("") && !compareVal.equalsIgnoreCase("No Comparision")) {
                                    calendardata.add(formattedvalue + changeVal);
                                    calendardata1.add(formattedvalue);
                                    calendardata2.add(priorvalue1);
                                } else if (primaryMeasure != null && !primaryMeasure.equalsIgnoreCase("")) {
                                    calendardata.add(formattedvalue + changeVal);
                                    calendardata1.add(formattedvalue);
                                    calendardata2.add(priorvalue1);
                                } else {
                                    calendardata.add(formattedvalue);
                                    calendardata1.add(formattedvalue);
                                    calendardata2.add(priorvalue1);
                                }
                            } else if (compareVal != null && !compareVal.equalsIgnoreCase("") && !compareVal.equalsIgnoreCase("No Comparision")) {
                                calendardata.add(formatter.format(curval) + changeVal);
                                calendardata1.add(formatter.format(curval));
                                calendardata2.add(priorvalue1);
                            } else {
                                calendardata.add(formatter.format(curval));
                                calendardata1.add(formatter.format(curval));
                                calendardata2.add(priorvalue1);
                            }
                        } else if (primaryMeasure != null && !primaryMeasure.equalsIgnoreCase("")) {
                            calendardata.add(formatter.format(curval) + changeVal);
                            calendardata1.add(formatter.format(curval) + changeVal);
                            calendardata2.add(priorvalue1);
                        } else if (compareVal != null && !compareVal.equalsIgnoreCase("") && compareVal.equalsIgnoreCase("No Comparision")) {
                            calendardata.add(formatter.format(curval));
                            calendardata1.add(formatter.format(curval));
                            calendardata2.add(priorvalue1);
                        } else {
                            calendardata.add(formatter.format(curval) + changeVal);
                            calendardata1.add(formatter.format(curval));
                            calendardata2.add(priorvalue1);
                        }
//                    if (i != Integer.parseInt(nodays) - 1) {
//                        calendardata.add(",");
//                    }
                        if (i < pbretObjForTime.getRowCount()) {
                            if (vals1 != null && !vals1[1].equalsIgnoreCase("") && vals2 != null && !vals2[1].equalsIgnoreCase("")) {
                                int k = Integer.parseInt(vals2[1]) - Integer.parseInt(vals1[1]);
                                for (int a = 1; a < k; a++) {
                                    calendardata.add("0");
                                    calendardata2.add("0");
                                }
                            }
                        }
                    }
//                 if (primaryMeasure != null && !primaryMeasure.equalsIgnoreCase("") && measureIdVal != null) {
//                        int maxindex = 0;
//                            int minindex = 0;
//                        if (primaryMeasure.equals(measureIdVal.get(j))) {
//                            double max = Double.parseDouble(calendardata1.get(0).toString().replaceAll(",", ""));
//                            double min = Double.parseDouble(calendardata1.get(0).toString().replaceAll(",", ""));
//                            for (int ktr = 0; ktr < calendardata1.size(); ktr++) {
//                                if (Double.parseDouble(calendardata1.get(ktr).toString().replaceAll(",", "")) > max) {
//                                    max = Double.parseDouble(calendardata1.get(ktr).toString().replaceAll(",", ""));
//                                    maxindex = ktr;
//                                }
//                                if (Double.parseDouble(calendardata1.get(ktr).toString().replaceAll(",", "")) < min) {
//                                    min = Double.parseDouble(calendardata1.get(ktr).toString().replaceAll(",", ""));
//                                    minindex = ktr;
//                                }
//                            }
//                            calendardata.set(maxindex, "X"+calendardata.get(maxindex));
//                            calendardata.set(minindex, "Z"+calendardata.get(minindex));
//                        }
//                    }
                    if (primaryMeasure != null && !primaryMeasure.equalsIgnoreCase("")) {
                        lMap.put(measureIdVal.get(j) + "I", calendardata);
                        priorlMap.put(measureIdVal.get(j) + "I", calendardata2);
                    } else {
                        lMap.put(measureIdVal.get(j), calendardata);
                        priorlMap.put(measureIdVal.get(j), calendardata2);
                    }
                    //
                }
            }

        }
        if (container != null) {
            container.setIcalDetailsHashmap(lMap);
        }
        if (session.getAttribute("icalFlag") == "true") {
            session.setAttribute("icalFlag", "false");
        }
        LinkedHashMap hmap12 = new LinkedHashMap();
        hmap12.put("lmap", lMap);
        hmap12.put("lmap1", priorlMap);
        //String val = dao.displayIcal(container,lMap,session);
        Gson gson = new Gson();
        String jsonString = gson.toJson(hmap12);

        PrintWriter out = response.getWriter();
        out.print(jsonString);
        return null;

    }

    public ActionForward saveMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HttpSession session = request.getSession(false);
        String elementIds = (String) session.getAttribute("elementIds");
        String nodays = request.getParameter("nodays");
        int imonth = Integer.parseInt(request.getParameter("imonth"));
        String iyear = request.getParameter("iyear");
        String elementNames = (String) session.getAttribute("orgelementNames");
        String dispNameList = "";
        dispNameList = (String) session.getAttribute("elementNames");
        String roleIds = (String) session.getAttribute("roleIds");
        String busroleId = request.getParameter("busroleId");
        String checkDailyval = request.getParameter("checkDailyval");
        String overWriteId = request.getParameter("overWriteId");
        String primaryMeas = request.getParameter("primaryMeas");
        String icalName = request.getParameter("icalName");
        String timeDetails = request.getParameter("timeDetails");
        String compareVal = request.getParameter("compareVal");
        String monthlyView = request.getParameter("monthlyView");
        String userId = (String) session.getAttribute("USERID");
//                 String date = request.getParameter("date");
        HashMap map2 = new HashMap();
        OnceViewContainer container = null;
        if (session.getAttribute("ICALDETAILS") != null) {
            map2 = (HashMap) session.getAttribute("ICALDETAILS");
            container = (OnceViewContainer) map2.get(overWriteId);
        }
        String formatList = "";
        String roundList = "";
        if (container != null) {
            formatList = container.getFormatList();
            roundList = container.getRoundList();
            dispNameList = container.getDispNameList();
            if (container.getPrimaryMeasure() != null && !container.getPrimaryMeasure().equalsIgnoreCase("")) {
                primaryMeas = container.getPrimaryMeasure();
            }
        }
        int id = 0;
        boolean flag = false;
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        if (overWriteId != null && !overWriteId.equalsIgnoreCase("null") && !overWriteId.equalsIgnoreCase("")) {
            flag = dao.updateIcal(overWriteId, icalName, busroleId, userId, elementIds, elementNames, roleIds, nodays, imonth, iyear, checkDailyval, formatList, roundList, compareVal, primaryMeas, dispNameList, timeDetails, monthlyView);
        } else {
            flag = dao.insertIcalDetail(icalName, busroleId, userId, elementIds, elementNames, roleIds, nodays, imonth, iyear, checkDailyval, compareVal, formatList, roundList, primaryMeas, dispNameList, timeDetails, monthlyView);
        }
        String jstring = "";
        PrintWriter out = response.getWriter();
        out.print(flag);
        session.setAttribute("elementIds", null);
        session.setAttribute("elementNames", null);
        session.setAttribute("formatList", null);
        session.setAttribute("roundList", null);
        session.setAttribute("name", null);
        session.setAttribute("icalId", null);
        session.setAttribute("ICALDETAILS", null);
        return null;
    }

    public ActionForward measureDisplyinIcal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HttpSession session = request.getSession(false);
        String measId = request.getParameter("measId");
        String overWriteId = request.getParameter("overWriteId");
//               if(!overWriteId.equalsIgnoreCase("") && overWriteId != null && !overWriteId.equalsIgnoreCase("undefined")){
//                   session.setAttribute("elementIds", null);
//                   session.setAttribute("elementNames", null);
//               }
        String measName = request.getParameter("measName");
        String roleId = request.getParameter("roleId");
        String eltIds = (String) session.getAttribute("elementIds");
        String elementNames = (String) session.getAttribute("elementNames");
        String roleIds = (String) session.getAttribute("roleIds");
        if (measId != "" && measName != "") {
            eltIds = measId;
            elementNames = measName;
        }
        if (session.getAttribute("roleIds") != null) {
            roleIds = roleIds + "," + roleId;
        } else {
            roleIds = roleId;
        }
        session.setAttribute("elementIds", eltIds);
        session.setAttribute("elementNames", elementNames);
        session.setAttribute("orgelementNames", elementNames);
        session.setAttribute("roleIds", roleIds);
        return null;
    }

    public ActionForward getMeasuresForIcal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String icalId = request.getParameter("icalId");
        HttpSession session = request.getSession(false);
        session.setAttribute("elementIds", null);
        session.setAttribute("elementNames", null);
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        String val = dao.getSavedMeasDetails(icalId, session);
        PrintWriter out = response.getWriter();
        session.setAttribute("icalId", icalId);
        out.print(val);
        return null;
    }

    public ActionForward deleteMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String kpiArray = request.getParameter("kpiArray");
        HttpSession session = request.getSession(false);
        String[] array = kpiArray.split(",");
        String[] vals = null;
        String IdList = "";
        String NameList = "";
        // 
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].replace("^", "@");
            vals = array[i].split("@");
            if (!array[i].equalsIgnoreCase("")) {
                if ("".equals(IdList) && "".equals(NameList)) {
                    NameList = vals[0];
                    IdList = vals[1];
                } else {
                    NameList = NameList + "," + vals[0];
                    IdList = IdList + "," + vals[1];
                }
            }
        }
        session.setAttribute("elementIds", IdList);
        session.setAttribute("elementNames", NameList);
        return null;
    }

    public ActionForward deleteIcal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String icalId = request.getParameter("icalId");
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        dao.deleteIcal(icalId);
        return null;
    }

    public ActionForward makeSessionVarNull(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String fromOnchane = request.getParameter("fromOnchane");
        String name = request.getParameter("name");
        String reset = request.getParameter("reset");
        String ibusRoleId = request.getParameter("ibusRoleId");
        HttpSession session = request.getSession(false);
        String icalId = request.getParameter("icalId");
        OnceViewContainer onecontainer = null;
        HashMap map2 = new HashMap();
        if (reset != null && reset.equalsIgnoreCase("true")) {
            if (icalId != null && !icalId.equalsIgnoreCase("")) {
                if (session.getAttribute("ICALDETAILS") != null) {
                    map2 = (HashMap) session.getAttribute("ICALDETAILS");
                    onecontainer = (OnceViewContainer) map2.get(icalId);
                } else {
                    onecontainer = new OnceViewContainer();
                }
                onecontainer.setMonthlyCal("false");
            }
        }
        session.setAttribute("showVariables", fromOnchane);
        session.setAttribute("name", name);
        session.setAttribute("cumCheck", "cumCheck");
        if (ibusRoleId != null && !ibusRoleId.equalsIgnoreCase("select")) {
            session.setAttribute("ibusRoleId", ibusRoleId);
        }
        return null;
    }

    public ActionForward setSessionVal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HttpSession session = request.getSession(false);
        session.setAttribute("elementIds", null);
        session.setAttribute("elementNames", null);
        session.setAttribute("formatList", null);
        session.setAttribute("roundList", null);
        session.setAttribute("name", null);
        session.setAttribute("icalId", null);
        session.setAttribute("ICALDETAILS", null);
        return null;
    }

    public ActionForward getSessionVal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HttpSession session = request.getSession(false);
        String val = (String) session.getAttribute("elementIds");
        String elementNames = (String) session.getAttribute("elementNames");
        PrintWriter out = response.getWriter();
        out.print(val + "@" + elementNames);
        return null;
    }

    public ActionForward getMeasuresForFormating(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String icalId = request.getParameter("icalId");
        HttpSession session = request.getSession(false);
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        String result = dao.getMeasuresForFormating(icalId, session);
        PrintWriter out = response.getWriter();
        out.print(result);
        return null;
    }

    public ActionForward applyFormating(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String formatList = request.getParameter("formatList");
        String fromOnchane = request.getParameter("fromOnchane");
        String roundList = request.getParameter("roundList");
        String dispNames = request.getParameter("dispNames");
        HttpSession session = request.getSession(false);
        HashMap map2 = new HashMap();
        OnceViewContainer container = null;
        String icalId = request.getParameter("icalId");
        if (session.getAttribute("ICALDETAILS") != null) {
            map2 = (HashMap) session.getAttribute("ICALDETAILS");
            container = (OnceViewContainer) map2.get(icalId);
        }
        container.setFormatList(formatList);
        container.setRoundList(roundList);
        container.setApplyFormatValues("true");
        container.setDispNameList(dispNames);
        String eltIds = container.getElementIds();
        String eltNames = container.getElementNames();
        String name = request.getParameter("name");
        session.setAttribute("showVariables", fromOnchane);
        session.setAttribute("formatList", formatList);
        session.setAttribute("roundList", roundList);
        session.setAttribute("dispNames", dispNames);
        session.setAttribute("elementNames", dispNames);
        session.setAttribute("name", name);
        session.setAttribute("applyValues", "true");
        return null;
    }

    public ActionForward getMonthlyDataForIcal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HttpSession session = request.getSession(false);
        String measId = request.getParameter("measureId");
        String icalId = request.getParameter("icalId");
        String compareVal = request.getParameter("compareVal");
        String measName = request.getParameter("measureName");
        String roleId = request.getParameter("busroleId");
        int iMonth = Integer.parseInt(request.getParameter("iMonth")) + 1;
        String iYear = request.getParameter("iYear");
        String nodays = request.getParameter("days");
        boolean testView = Boolean.parseBoolean(request.getParameter("checkval"));
//            String dateFormat1 = new String(Integer.toString(iMonth).concat("/").concat(nodays).concat("/").concat(iYear));Prabal
        String dateFormat1 = Integer.toString(iMonth).concat("/").concat(nodays).concat("/").concat(iYear);
        String dateFormat = "";
        StringBuilder sb = new StringBuilder();
        DashboardViewerDAO dao = new DashboardViewerDAO();
        PbReturnObject pbretObjForTime = null;
        String[] formatListVals = null;
        String[] roundListVals = null;
        String formatList = (String) session.getAttribute("formatList");
        String roundList = (String) session.getAttribute("roundList");
        String applyValues = (String) session.getAttribute("applyValues");
        String[] monthName = {"JAN", "FEB",
            "MAR", "APR", "MAY", "JUN", "JUL",
            "AUG", "SEP", "OCT", "NOV",
            "DEC"};
        Calendar cal;
        String s;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date;
        cal = Calendar.getInstance();
        date = cal.getTime();
        dateFormat = sdf.format(date);
        //  

        int monthval = dateFormat.indexOf("/");
        String valu = "";
        valu = dateFormat.substring(0, monthval);
        int change = 12 - (iMonth + 1);
        //

        String mesName = measName + testView;
        OnceViewContainer container = null;


        ArrayList rowview = new ArrayList();
        rowview.add("TIME");
        PbReportQuery timequery = new PbReportQuery();
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        List<String> measureIdVal = new ArrayList<String>();
        LinkedHashMap lMap = new LinkedHashMap();
        LinkedHashMap lMap1 = new LinkedHashMap();
        String eltIds = (String) session.getAttribute("elementIds");
        String[] eltIdsArray = eltIds.split(",");
        for (int i = 0; i < eltIdsArray.length; i++) {
            measureIdVal.add(eltIdsArray[i]);
        }
        //  
        if (icalId != null && !icalId.equalsIgnoreCase("")) {
            HashMap map2 = new HashMap();
            if (session.getAttribute("ICALDETAILS") != null) {
                map2 = (HashMap) session.getAttribute("ICALDETAILS");
                container = (OnceViewContainer) map2.get(icalId);
            }
        }
        if (container != null) {
            formatList = container.getFormatList();
            roundList = container.getRoundList();
            applyValues = container.getApplyFormatValues();
            if (compareVal != null && !compareVal.equalsIgnoreCase("")) {
                compareVal = container.getComparisionType();
            }
        }
        if (formatList != null) {
            formatListVals = formatList.split(",");
            roundListVals = roundList.split(",");
        }


        String userId = String.valueOf(session.getAttribute("USERID"));
        List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal, new HashMap<String, String>());
        if (kpiElements != null) {
            for (KPIElement elem : kpiElements) {
                if (elem.getElementName() != null) {
                    QueryCols.add(elem.getElementId());
                }
                QueryAggs.add(elem.getAggregationType());
            }
        }
        ArrayList arl = new ArrayList();
        arl.add("Day");
        arl.add("PRG_STD");
        arl.add(dateFormat1);
        arl.add("Month");
        if (compareVal != null && !compareVal.equalsIgnoreCase("")) {
            arl.add(compareVal);
        } else {
            arl.add("Last Year");
        }

        timequery.setRowViewbyCols(rowview);
        timequery.setColViewbyCols(new ArrayList());
        timequery.setColViewbyCols(new ArrayList());
        timequery.setQryColumns(QueryCols);
        timequery.setColAggration(QueryAggs);
        timequery.setTimeDetails(arl);
        timequery.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
        timequery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
        timequery.isTimeSeries = false;
        timequery.isTimeDrill = false;
        timequery.setBizRoles(roleId);
        timequery.setUserId(userId);
        pbretObjForTime = timequery.getPbReturnObject1(String.valueOf(QueryCols.get(0)));


        if (pbretObjForTime != null) {
//            if (pbretObjForTime.getRowCount() > Integer.parseInt(nodays)) {
            for (int j = 0; j < measureIdVal.size(); j++) {
                String qry = "select element_id from prg_user_all_info_details where ref_element_id = " + measureIdVal.get(j) + " and ref_element_type=3";
                PbDb pbdb = new PbDb();
                PbReturnObject retObj = new PbReturnObject();
                retObj = pbdb.execSelectSQL(qry);
                ArrayList calendardata = new ArrayList();
                for (int i = 0; i < pbretObjForTime.getRowCount(); i++) {
                    double currVal = 0.0;
                    double datavalue = 0.0;
                    String changeVal = "";
                    String dateVal = "";
                    String dateVal1 = "";
                    String[] vals1 = null;
                    String[] vals2 = null;
//                     String datavalue = pbretObjForTime.getFieldValueString(0, "A_".concat(QueryCols.get(0).toString()));
                    dateVal = pbretObjForTime.getFieldValueString(i, ("TIME"));
                    if (dateVal != null && !dateVal.equalsIgnoreCase("")) {
                        vals1 = dateVal.split("-");
                        if (i < pbretObjForTime.getRowCount() - 1) {
                            dateVal1 = pbretObjForTime.getFieldValueString(i + 1, ("TIME"));
                            vals2 = dateVal1.split("-");
                        }
                    }
                    if (retObj != null && retObj.getRowCount() > 0 && retObj.getFieldValueString(0, "ELEMENT_ID") != "" && retObj.getFieldValueString(0, "ELEMENT_ID") != null && !retObj.getFieldValueString(0, "ELEMENT_ID").equalsIgnoreCase("")) {
                        datavalue = Double.parseDouble((pbretObjForTime.getFieldValueString(i, ("A_" + retObj.getFieldValueString(0, "ELEMENT_ID")).toString())));
                    }
                    // 
                    if (datavalue < 0) {
                        changeVal = "N";
                    } else if (datavalue > 0) {
                        changeVal = "P";
                    } else if (datavalue == 0) {
                        changeVal = "V";
                    }
                    currVal = Double.parseDouble((pbretObjForTime.getFieldValueString(i, ("A_" + measureIdVal.get(j)).toString())));
                    int decimalPlaces = 1;
                    BigDecimal curval = new BigDecimal(currVal);
                    BigDecimal val = curval;
                    curval = curval.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                    NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
//                    if(formatListVals != null && roundListVals != null ){
//                            if(formatListVals[j].equalsIgnoreCase("A") ){
//                                formatListVals[j] = "";
//                            }
//                            NumberFormatter nf=new NumberFormatter();
//                            String formattedvalue=nf.getModifiedNumber(val, formatListVals[j], Integer.parseInt(roundListVals[j]));
//                            calendardata.add(formattedvalue);
//                    }else{
                    if (i == 0) {
                        int l3 = 0;
                        for (int l = 0; l < monthName.length; l++) {
                            if (monthName[l].equalsIgnoreCase(vals1[0])) {
                                l3 = l + 1;
                            }
                        }
                        if (l3 - 1 > 0 && vals1[1].equalsIgnoreCase(iYear)) {
                            for (int m = 0; m < l3 - 1; m++) {
                                calendardata.add("0");
                            }
                        }
                    }
                    for (int l = 0; l < monthName.length; l++) {
                        if ((monthName[l] + "-" + iYear).equalsIgnoreCase(dateVal)) {
                            if (compareVal != null && !compareVal.equalsIgnoreCase("") && !compareVal.equalsIgnoreCase("No Comparision")) {
                                calendardata.add(formatter.format(curval) + changeVal);
                            } else {
                                calendardata.add(formatter.format(curval));
                            }
                        }
                    }
                    if (i > change) {
                        if (i < pbretObjForTime.getRowCount()) {
                            if (vals1 != null && !vals1[0].equalsIgnoreCase("") && vals2 != null && !vals2[0].equalsIgnoreCase("")) {
                                int l1 = 0, l2 = 0;
                                for (int l = 0; l < monthName.length; l++) {
                                    if (monthName[l].equalsIgnoreCase(vals1[0])) {
                                        l1 = l + 1;
                                    }
                                    if (monthName[l].equalsIgnoreCase(vals2[0])) {
                                        l2 = l + 1;
                                    }
                                }
                                int k = 0;
                                if (l2 > l1) {
                                    k = l2 - l1;
                                }
                                for (int a = 1; a < k; a++) {
                                    calendardata.add("0");
                                }
                            }
                        }
                    }
                }
                lMap.put(measureIdVal.get(j), calendardata);
            }
        }
        Gson gson = new Gson();
        String jsonString = gson.toJson(lMap);
        PrintWriter out = response.getWriter();
        out.print(jsonString);
        return null;
    }

    public ActionForward getRollingDataForIcal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HttpSession session = request.getSession(false);
        String measId = request.getParameter("measureId");
        String measName = request.getParameter("measureName");
        String roleId = request.getParameter("busroleId");
        int noofDays = Integer.parseInt(request.getParameter("noofDays")) + 1;
        int iMonth = Integer.parseInt(request.getParameter("iMonth")) + 1;
        String iyear = request.getParameter("iyear");
//        String dateFormat = new String(Integer.toString(iMonth).concat("/").concat(Integer.toString(noofDays)).concat("/").concat(iyear)); Prabal
        String dateFormat = Integer.toString(iMonth).concat("/").concat(Integer.toString(noofDays)).concat("/").concat(iyear);
        boolean testView = Boolean.parseBoolean(request.getParameter("checkval"));
//        String dateFormat = "";
        StringBuilder sb = new StringBuilder();
        DashboardViewerDAO dao = new DashboardViewerDAO();
        PbReturnObject pbretObjForTime = null;
        String imgdata = "";
        //roleId =  "1644";

//        Calendar cal;
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//        Date date;
//            cal = Calendar.getInstance();
//            date = cal.getTime();
//            dateFormat = sdf.format(date);

        ArrayList rowview = new ArrayList();
        rowview.add("TIME");
        PbReportQuery timequery = new PbReportQuery();
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        List<String> measureIdVal = new ArrayList<String>();
        LinkedHashMap lMap = new LinkedHashMap();
        LinkedHashMap lMap1 = new LinkedHashMap();
        measureIdVal.add(measId);

        String userId = String.valueOf(session.getAttribute("USERID"));
        List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal, new HashMap<String, String>());
        if (kpiElements != null) {
            for (KPIElement elem : kpiElements) {
                if (elem.getElementId().equalsIgnoreCase(measId)) {
                    if (elem.getElementName() != null) {
                        QueryCols.add(elem.getElementId());
                    }
                    QueryAggs.add(elem.getAggregationType());
                }
            }
        }
        ProgenParam pramnam = new ProgenParam();
        String date1 = pramnam.getdateforpage();

        ArrayList arl = new ArrayList();
        arl.add("Day");
        arl.add("PRG_DAY_ROLLING");
        arl.add(dateFormat);
        arl.add("Last 30 Days");

        timequery.setRowViewbyCols(rowview);
        timequery.setColViewbyCols(new ArrayList());
        timequery.setColViewbyCols(new ArrayList());
        timequery.setQryColumns(QueryCols);
        timequery.setColAggration(QueryAggs);
        timequery.setTimeDetails(arl);
        timequery.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
        timequery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
        timequery.isTimeSeries = false;
        timequery.isTimeDrill = false;
        timequery.setBizRoles(roleId);
        timequery.setUserId(userId);
        pbretObjForTime = timequery.getPbReturnObject(String.valueOf(QueryCols.get(0)));

        String datavaluesstr = "";
        String keyvaluesstr = "";
        String barchartcolumntitlesstr = "";
        String viewbycolumnsstr = "";
        StringBuilder imgtest = new StringBuilder();
        ProgenJQPlotGraph jqGraph = new ProgenJQPlotGraph();
        jqGraph.trendType = "montFormat";
        jqGraph.setChartId(measId);
        jqGraph.setChartId(measId);
        jqGraph.setGraphType("Line");
        imgtest.append("<div id='chart-" + measId + "' style='width:600px; height:400px;'></div>");


        imgtest.append("<script>$(\".jqplot-highlighter-tooltip, .jqplot-canvasOverlay-tooltip \").css('font-size', '1em');");
        imgtest.append(jqGraph.getTrendGraph(pbretObjForTime, (ArrayList) measureIdVal, rowview, "", null, null));
        imgtest.append("</script>");


//        if ((pbretObjForTime != null && pbretObjForTime.rowCount > 0)) {
//            ProgenChartDatasets graph = new ProgenChartDatasets();
//            String[] barchartcolumnnames = new String[2];
//            String[] barchartcolumntitles = new String[2];
//            String[] viewbycolumns = new String[1];
//            String[] viewbycolumnTies = new String[1];
//
//            barchartcolumnnames[0] = "Time";
//            barchartcolumnnames[1] = "A_" + measId;
//            barchartcolumntitles[0] = "Time";
//            barchartcolumntitles[1] = measName;
//            viewbycolumns[0] = "TIME";
//            viewbycolumnTies[0] = "Time";
//
//            for (int index = 0; index < pbretObjForTime.getRowCount(); index++) {
//                barchartcolumntitlesstr = barchartcolumntitlesstr + measName + ",";
//                datavaluesstr = datavaluesstr + pbretObjForTime.getFieldValueString(index, "A_" + measId) + ",";
//                String str = pbretObjForTime.getFieldValueString(index, viewbycolumns[0]);
//                str = str.replaceAll("-", "/");
//                keyvaluesstr = keyvaluesstr + str + ",";
//                viewbycolumnsstr = viewbycolumnsstr + "Time" + ",";
//            }
//
//            PbGraphDisplay pbDisplay = new PbGraphDisplay();
//            ArrayList grpDetails = new ArrayList();
//            ProgenChartDisplay[] pcharts = null;
//            String reportId = null;
//            String graphId = null;
//            DashletDetail dashlet = new DashletDetail();
//            GraphReport graphDetails = new GraphReport();
//            GraphProperty graphPro = new GraphProperty();
//            graphPro.setBarChartColumnNames1(barchartcolumnnames);
//            graphPro.setBarChartColumnNames2(viewbycolumns);
//            graphPro.setBarChartColumnTitles1(barchartcolumntitles);
//            graphPro.setBarChartColumnTitles2(viewbycolumnTies);
//            graphPro.setFromDate((String) arl.get(2));
//            graphPro.setStackedType("absStacked");
//            graphPro.setToDate("Day");
//
//
//
//            List<QueryDetail> queryDetail = new ArrayList<QueryDetail>();
//            QueryDetail queryDetais = new QueryDetail();
//            queryDetais.setElementId(String.valueOf(QueryCols.get(0)));
//            queryDetais.setAggregationType(String.valueOf(QueryAggs.get(0)));
//            queryDetais.setDisplayName(measName);
//            queryDetais.setColumnType("NUMBER");
//            queryDetail.add(queryDetais);
//            graphDetails.setQueryDetails(queryDetail);
//
//            graphDetails.setGraphWidth("600");
//            graphDetails.setGraphHeight("400");
//            graphDetails.setGraphClass(7);
//            graphDetails.setGraphSize(1);
//            graphDetails.setShowXAxisGrid(true);
//            graphDetails.setShowYAxisGrid(true);
//            graphDetails.setLeftYAxisLabel("");
//            graphDetails.setRightYAxisLabel("");
//            graphDetails.setBackgroundColor("");
//            graphDetails.setShowData(true);
//            graphDetails.setLinkAllowed(true);
//            graphDetails.setDisplayRows("30");
//            graphDetails.setLegendLocation("Bottom");
//            graphDetails.setGraphTypeName("Line");
//            graphDetails.setGraphClassName("Category");
//            graphDetails.setGraphSizeName("Medium");
//            graphDetails.setGraphName(measName);
//            graphDetails.setAxis("0");
//            graphDetails.setGraphProperty(graphPro);
//            graphDetails.setTimeSeries(true);
//            GraphProperty graphProp = new GraphProperty();
//                graphProp.setEndValue(10);
//                graphProp.setStartValue(0);
//                graphProp.setNumberFormat("");
//                graphProp.setSymbol("");
//                graphProp.setSwapGraphColumns("true");
//            graphDetails.setGraphProperty(graphProp);
//
//            dashlet.setRefReportId(reportId);
//            dashlet.setGraphId(graphId);
//            dashlet.setKpiMasterId("0");
//            dashlet.setDashBoardDetailId("0");
//            dashlet.setDisplaySequence(0);
//            dashlet.setDisplayType(DashboardConstants.GRAPH_REPORT);
//            dashlet.setDashletName(measName);
//            dashlet.setKpiType(null);
//            dashlet.setReportDetails(graphDetails);
//
//            pbDisplay.setCurrentDispRetObjRecords(pbretObjForTime);
//            pbDisplay.setCurrentDispRecordsRetObjWithGT(pbretObjForTime);
//            pbDisplay.setAllDispRecordsRetObj(pbretObjForTime);
//            pbDisplay.setOut(response.getWriter());
//            pbDisplay.setResponse(response);
//            pbDisplay.setViewByElementIds(viewbycolumns);
//            pbDisplay.setViewByColNames(viewbycolumns);
//            pbDisplay.setJscal(null);
//            pbDisplay.setCtxPath(request.getContextPath());
//            pbDisplay.setShowGT("N");
//            pbDisplay.setSession(session);
//            grpDetails = pbDisplay.getDashboardGraphHeadersNew(reportId, graphId, dashlet);
//
//            if (grpDetails != null && !grpDetails.isEmpty()) {
//                pcharts = (ProgenChartDisplay[]) grpDetails.get(2);
//                if (pcharts != null && pcharts.length != 0) {
//                    imgdata = pcharts[0].chartDisplay;
//                }
//            }
//        }
//        StringBuilder imgtest = new StringBuilder();
        PrintWriter out = response.getWriter();
        out.print(imgtest.toString());


        return null;
    }

    public ActionForward saveValsForInitialOpen(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HttpSession session = request.getSession(false);
        String name = request.getParameter("name");
        session.setAttribute("icalFlag", "true");
        session.setAttribute("name", name);
        return null;
    }

    public ActionForward bulidJqMeterChart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {


        HttpSession session = request.getSession(false);
        String dashboardid = request.getParameter("dashboardId");
        String dashletid = request.getParameter("dashletId");
        String dashid = request.getParameter("ids");
        String graphtype = request.getParameter("graphtype");
        String contextPath = request.getContextPath();
        HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
        Container container = (Container) map.get(dashboardid);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail dashlet = (DashletDetail) collect.getDashletDetail(dashletid);
        dashlet.setGraphtype(graphtype);
        //dashlet.setJqplotGraph(true);
        //dashlet.setJqPlotGraphType(graphType);
        GraphBuilder graphBuilder = new GraphBuilder();
        graphBuilder.setRequest(request);
        graphBuilder.setResponse(response);
        graphBuilder.setFxCharts(false);
        graphBuilder.setGraphType(graphtype);
        String userId = session.getAttribute("USERID").toString();
        String jqGraphString = graphBuilder.displayGraphs(container, dashletid, contextPath, false, "false");


//     DashboardTemplateBD dashtempbd=new DashboardTemplateBD();
//     String metergraph=dashtempbd.bulidJqMeterChart(dashboardid, dashletid,dashid,request);
        PrintWriter out = response.getWriter();
        out.print(jqGraphString);





        return null;
    }

    public ActionForward getDataOnComparisonClick(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String measId = request.getParameter("measId");
        //String measName = request.getParameter("measName");
        String oneViewId = request.getParameter("oneViewId");
        String roleId = request.getParameter("roleId");
        String viewLetId = request.getParameter("viewLetId");
        String compareCheck = request.getParameter("compareCheck");
        StringBuilder sb = new StringBuilder();
        ArrayList dataSeq = null;
        DashboardViewerDAO dao = new DashboardViewerDAO();
        PbReportTableBD reportTableBD = new PbReportTableBD();
        PbReturnObject pbretObjForTime = new PbReturnObject();
        CreateKPIFromReport kPIFromReport = new CreateKPIFromReport();
        String imgdata = "";
        String formatType = "";
        String formatValue = "";
        List<ProgenDataSet> retObjList = new ArrayList<ProgenDataSet>();
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");

        ArrayList rowview = new ArrayList();
        rowview.add("TIME");
        PbReportQuery timequery = new PbReportQuery();
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        List<String> measureIdVal = new ArrayList<String>();
        ArrayList measureIdVal1 = new ArrayList();
        measureIdVal.add(measId);


        String userId = String.valueOf(session.getAttribute("USERID"));
        OnceViewContainer onecontainer1 = null;
        String fileName = null;
        PbReturnObject retObj = null;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        retObj = reportTemplateDAO.testForExisting();
        ArrayList<String> va = new ArrayList<String>();
        OneViewLetDetails detail = null;
        for (int i = 0; i < retObj.getRowCount(); i++) {
            va.add(retObj.getFieldValueString(i, "ONEVIEWID"));
        }
        if (!va.isEmpty() && va.contains(oneViewId)) {
            retObj = reportTemplateDAO.getOneviewFileNam(oneViewId);
            fileName = request.getSession(false).getAttribute("tempFileName").toString();
            FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            onecontainer1 = (OnceViewContainer) ois.readObject();
            ois.close();
        } else {
            fileName = request.getSession(false).getAttribute("tempFileName").toString();
            FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            onecontainer1 = (OnceViewContainer) ois.readObject();
            ois.close();
        }
        detail = onecontainer1.onviewLetdetails.get(Integer.parseInt(viewLetId));
        kPIFromReport = detail.getKpiFromReport();
        ArrayList arl = new ArrayList();
        ArrayList arl1 = new ArrayList();
        String priorId = userLayerDAO.getPriorMeasure(kPIFromReport.getMeasureId().replace("A_", ""));

        if (priorId != "") {
            measureIdVal1.add(kPIFromReport.getMeasureId().replace("A_", ""));
            List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal1, new HashMap<String, String>());
            if (kpiElements != null) {
                for (KPIElement elem : kpiElements) {
                    if (elem.getElementName() != null) {
                        QueryCols.add(elem.getElementId());
                    }
                    QueryAggs.add(elem.getAggregationType());
                }
            }
            if (QueryCols != null && !QueryCols.isEmpty()) {
                for (int i = 0; i < QueryCols.size(); i++) {
                    if (priorId.equalsIgnoreCase(QueryCols.get(i).toString())) {
                        detail.getComplexreportQryCols().add(QueryCols.get(i));
                        detail.getComplexqryAggregations().add(QueryAggs.get(i));
                    }
                }
            }
        }
        if (onecontainer1 != null && !onecontainer1.timedetails.isEmpty()) {
            arl = (ArrayList) onecontainer1.timedetails;
        } else {
            arl.add("Day");
            arl.add("PRG_STD");
            ProgenParam pramnam = new ProgenParam();
            String date = pramnam.getdateforpage();
            arl.add(date);
            arl.add("Month");
            arl.add("Last Period");
        }
        arl1.addAll(arl);
        if (compareCheck == null || compareCheck.equalsIgnoreCase("qtr")) {
            arl1.set(3, "Qtr");
        } else if (compareCheck != null && compareCheck.equalsIgnoreCase("year")) {
            arl1.set(3, "Year");
        } else if (compareCheck != null && compareCheck.equalsIgnoreCase("lastmonth")) {
            //arl1.set(4, "Last Month");
            PbTimeRanges timeRanges = new PbTimeRanges();
            String priodType = arl.get(3).toString();
            String compareWith = arl.get(4).toString();
            String date = arl.get(2).toString();
            timeRanges.elementID = kPIFromReport.getMeasureId().replace("A_", "");
            timeRanges.setRange(priodType, compareWith, date);
            arl1.set(2, timeRanges.p_ed_d.split(" ")[0]);
            //timeRanges.ed_d.split(" ")[0];
        }
        String prefixValue = "";
        String suffixValue = "";
        if (detail.getPrefixValue() != null) {
            prefixValue = detail.getPrefixValue();
        }
        if (detail.getSuffixValue() != null) {
            suffixValue = detail.getSuffixValue();
        }
        if (detail.getSuffixValue() != null) {
            if (detail.getSuffixValue().equalsIgnoreCase("K")) {
                suffixValue = "K";
            } else if (detail.getSuffixValue().equalsIgnoreCase("L")) {
                suffixValue = "Lkh";
            } else if (detail.getSuffixValue().equalsIgnoreCase("M")) {
                suffixValue = "Mn";
            } else if (detail.getSuffixValue().equalsIgnoreCase("Cr")) {
                suffixValue = "Crs";
            }
        }
        if (detail.getFormatVal() != null && detail.getFormatVal().equalsIgnoreCase("K")) {
            formatType = "K";
            formatValue = "K";
        }
        if (detail.getFormatVal() != null && detail.getFormatVal().equalsIgnoreCase("M")) {
            formatType = "M";
            formatValue = "Mn";
        }
        if (detail.getFormatVal() != null && detail.getFormatVal().equalsIgnoreCase("L")) {
            formatType = "L";
            formatValue = "Lkh";
        }
        if (detail.getFormatVal() != null && detail.getFormatVal().equalsIgnoreCase("Cr")) {
            formatType = "Cr";
            formatValue = "Crs";
        }
        timequery.setRowViewbyCols(detail.getComplexrowviewbys());
        timequery.setColViewbyCols(new ArrayList());
        timequery.setParamValue((HashMap) kPIFromReport.getReportParametersValues());
        timequery.setQryColumns(detail.getComplexreportQryCols());
        timequery.setColAggration(detail.getComplexqryAggregations());
        timequery.setTimeDetails(arl1);
//        timequery.setDefaultMeasure(String.valueOf(detail.getComplexreportQryCols().get(0)));
//        timequery.setDefaultMeasureSumm(String.valueOf(detail.getComplexqryAggregations().get(0)));
//        timequery.isTimeSeries = false;
//                repQuery.setReportId(collect.reportId);
        timequery.setBizRoles(roleId);
        timequery.setUserId(userId);
        pbretObjForTime = timequery.getPbReturnObject(String.valueOf(detail.getComplexreportQryCols().get(0)));
        pbretObjForTime.resetViewSequence();
        ArrayList sortColumns = null;
        if (kPIFromReport.getSortColumns() != null) {
            sortColumns = new ArrayList(Arrays.asList(kPIFromReport.getSortColumns()));
        }

        reportTableBD.searchDataSet(kPIFromReport, pbretObjForTime);
        if (kPIFromReport.isTopBottomSet()) {
            if (kPIFromReport.getTopBtmMode().equalsIgnoreCase("TopBottomAbsRows")) {
                dataSeq = pbretObjForTime.findTopBottom(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
            } else {
                dataSeq = pbretObjForTime.findTopBottomPercentWise(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
            }
            pbretObjForTime.resetViewSequence();
            pbretObjForTime.setViewSequence(dataSeq);
        }
        retObjList.add(pbretObjForTime);
        StringBuilder imgtest = new StringBuilder();
        ArrayList alist1 = new ArrayList();
        ArrayList alist2 = new ArrayList();
        ArrayList alist3 = new ArrayList();
        ArrayList alist4 = new ArrayList();
        alist1 = pbretObjForTime.retrieveDataBasedOnViewSeq(kPIFromReport.getMeasureId());
        if (priorId != "") {
            alist3 = pbretObjForTime.retrieveDataBasedOnViewSeq("A_" + priorId);
        }
        if (formatType != null && formatValue != null && detail != null && detail.getFormatVal() != null && detail.getRoundVal() != null && !detail.getRoundVal().equalsIgnoreCase("")) {
            for (int i = 0; i < alist1.size(); i++) {
                String value1 = NumberFormatter.getModifiedNumber(new BigDecimal(alist1.get(i).toString()), detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
                alist2.add(value1);
            }
        } else {
            for (int i = 0; i < alist1.size(); i++) {
                String value1 = NumberFormatter.getModifiedNumber(new BigDecimal(alist1.get(i).toString()), "", 0);
                alist2.add(value1);
            }
        }
//                RunTimeMeasure rtm = new RunTimeMeasure(alist1);
//                ArrayList<BigDecimal> runTimeMeasData = null;
//                pbretObjForTime.setRuntimeMeasure(kPIFromReport.getMeasureId(), alist1);
//        runTimeMeasData = StatUtil.STAT_HELPER.Rank(pbretObjForTime.retrieveDataBasedOnViewSeq(kPIFromReport.getMeasureId()));
//        RunTimeMeasure rtMeasure = new RunTimeMeasure(runTimeMeasData);
//
//        RunTimeMeasure rtm1 = new RunTimeMeasure(alist3);
//                ArrayList<BigDecimal> runTimeMeasData1 = null;
//                pbretObjForTime.setRuntimeMeasure("A_"+priorId, alist3);
//        runTimeMeasData1 = StatUtil.STAT_HELPER.Rank(pbretObjForTime.retrieveDataBasedOnViewSeq("A_"+priorId));
//        String color = "";
//        for(int k=0;k<runTimeMeasData1.size();k++){
//        if (runTimeMeasData1.get(k).compareTo(runTimeMeasData.get(k)) > 0) {
//                   color =  "green";
//                } else if (runTimeMeasData1.get(k).compareTo(runTimeMeasData.get(k)) < 0) {
//                    color = "red";
//                }else{
//                    color = "";
//                }
//            alist4.add(color);
//        }
        if (prefixValue == null) {
            prefixValue = "";
        }
        if (suffixValue == null) {
            suffixValue = "";
        }
        imgtest.append("<table style='width:100%;height:100%;'>");
//        imgtest.append("<tr height='10'></tr>");
        for (int i = 0; i < alist2.size(); i++) {
            //
//                    String tooltipval = "<table><tr></tr><tr><td>Rank</td></tr><tr><td>Value</td></tr></table>";
//                    tooltipval+="<table><tr><td>Current:</td></tr><tr><td>"+runTimeMeasData.get(i)+"</td></tr><tr><td>"+alist1.get(i) +"</td></tr></table>";
//                    tooltipval+="<table><tr><td>Prior:</td></tr><tr><td>"+runTimeMeasData1.get(i) +"</td></tr><tr><td>"+alist3.get(i) +"</td></tr></table>";
            //String tooltipval = "Current Rank:"+runTimeMeasData.get(i)+", Prior Rank:"+runTimeMeasData1.get(i)+"";
            imgtest.append("<tr><td class='myhead' style='color:#369;font-size:11pt;background-color:white;width:100%'>" + pbretObjForTime.getFieldValueStringBasedOnViewSeq(i, "A_" + detail.getComplexrowviewbys().get(0)) + "</td><td width='2%'></td><td style='color:#369;font-size:12pt'>" + prefixValue + alist2.get(i).toString().replace(formatType, "") + "<span style='font-size:7pt;'>" + suffixValue + "</span></td></tr>");
            imgtest.append("<tr></tr><tr></tr>");
        }
        imgtest.append("</table>");
//        imgtest.append("</center>");



        PrintWriter out = response.getWriter();
        out.print(imgtest.toString());
        return null;
    }

    public ActionForward getDataOnRankOrValClick(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String measId = request.getParameter("measId");
        //String measName = request.getParameter("measName");
        String oneViewId = request.getParameter("oneViewId");
        String roleId = request.getParameter("roleId");
        String viewLetId = request.getParameter("viewLetId");
        String compareCheck = request.getParameter("compareCheck");
        StringBuilder sb = new StringBuilder();
        ArrayList dataSeq = null;
        DashboardViewerDAO dao = new DashboardViewerDAO();
        PbReportTableBD reportTableBD = new PbReportTableBD();
        PbReturnObject pbretObjForTime = new PbReturnObject();
        CreateKPIFromReport kPIFromReport = new CreateKPIFromReport();
        String imgdata = "";
        String formatType = "";
        String formatValue = "";
        List<ProgenDataSet> retObjList = new ArrayList<ProgenDataSet>();
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");

        ArrayList rowview = new ArrayList();
        rowview.add("TIME");
        PbReportQuery timequery = new PbReportQuery();
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        List<String> measureIdVal = new ArrayList<String>();
        ArrayList measureIdVal1 = new ArrayList();
        measureIdVal.add(measId);


        String userId = String.valueOf(session.getAttribute("USERID"));
        OnceViewContainer onecontainer1 = null;
        String fileName = null;
        PbReturnObject retObj = null;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        retObj = reportTemplateDAO.testForExisting();
        ArrayList<String> va = new ArrayList<String>();
        OneViewLetDetails detail = null;
        for (int i = 0; i < retObj.getRowCount(); i++) {
            va.add(retObj.getFieldValueString(i, "ONEVIEWID"));
        }
        if (!va.isEmpty() && va.contains(oneViewId)) {
            retObj = reportTemplateDAO.getOneviewFileNam(oneViewId);
            fileName = request.getSession(false).getAttribute("tempFileName").toString();
            FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            onecontainer1 = (OnceViewContainer) ois.readObject();
            ois.close();
        } else {
            fileName = request.getSession(false).getAttribute("tempFileName").toString();
            FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            onecontainer1 = (OnceViewContainer) ois.readObject();
            ois.close();
        }
        detail = onecontainer1.onviewLetdetails.get(Integer.parseInt(viewLetId));
        kPIFromReport = detail.getKpiFromReport();
        ArrayList arl = new ArrayList();
        ArrayList arl1 = new ArrayList();
        String priorId = userLayerDAO.getPriorMeasure(kPIFromReport.getMeasureId().replace("A_", ""));

        if (priorId != "") {
            measureIdVal1.add(kPIFromReport.getMeasureId().replace("A_", ""));
            List<KPIElement> kpiElements = dao.getKPIElements(measureIdVal1, new HashMap<String, String>());
            if (kpiElements != null) {
                for (KPIElement elem : kpiElements) {
                    if (elem.getElementName() != null) {
                        QueryCols.add(elem.getElementId());
                    }
                    QueryAggs.add(elem.getAggregationType());
                }
            }
            if (QueryCols != null && !QueryCols.isEmpty()) {
                for (int i = 0; i < QueryCols.size(); i++) {
                    if (priorId.equalsIgnoreCase(QueryCols.get(i).toString())) {
                        detail.getComplexreportQryCols().add(QueryCols.get(i));
                        detail.getComplexqryAggregations().add(QueryAggs.get(i));
                    }
                }
            }
        }
        if (onecontainer1 != null && !onecontainer1.timedetails.isEmpty()) {
            arl = (ArrayList) onecontainer1.timedetails;
        } else {
            arl.add("Day");
            arl.add("PRG_STD");
            ProgenParam pramnam = new ProgenParam();
            String date = pramnam.getdateforpage();
            arl.add(date);
            arl.add("Month");
            arl.add("Last Period");
        }
        String prefixValue = "";
        String suffixValue = "";
        if (detail.getPrefixValue() != null) {
            prefixValue = detail.getPrefixValue();
        }
        if (detail.getSuffixValue() != null) {
            suffixValue = detail.getSuffixValue();
        }
        if (detail.getSuffixValue() != null) {
            if (detail.getSuffixValue().equalsIgnoreCase("K")) {
                suffixValue = "K";
            } else if (detail.getSuffixValue().equalsIgnoreCase("L")) {
                suffixValue = "Lkh";
            } else if (detail.getSuffixValue().equalsIgnoreCase("M")) {
                suffixValue = "Mn";
            } else if (detail.getSuffixValue().equalsIgnoreCase("Cr")) {
                suffixValue = "Crs";
            }
        }
        if (detail.getFormatVal() != null && detail.getFormatVal().equalsIgnoreCase("K")) {
            formatType = "K";
            formatValue = "K";
        }
        if (detail.getFormatVal() != null && detail.getFormatVal().equalsIgnoreCase("M")) {
            formatType = "M";
            formatValue = "Mn";
        }
        if (detail.getFormatVal() != null && detail.getFormatVal().equalsIgnoreCase("L")) {
            formatType = "L";
            formatValue = "Lkh";
        }
        if (detail.getFormatVal() != null && detail.getFormatVal().equalsIgnoreCase("Cr")) {
            formatType = "Cr";
            formatValue = "Crs";
        }
        timequery.setRowViewbyCols(detail.getComplexrowviewbys());
        timequery.setColViewbyCols(new ArrayList());
        timequery.setParamValue((HashMap) kPIFromReport.getReportParametersValues());
        timequery.setQryColumns(detail.getComplexreportQryCols());
        timequery.setColAggration(detail.getComplexqryAggregations());
        timequery.setTimeDetails(arl);
//        timequery.setDefaultMeasure(String.valueOf(detail.getComplexreportQryCols().get(0)));
//        timequery.setDefaultMeasureSumm(String.valueOf(detail.getComplexqryAggregations().get(0)));
//        timequery.isTimeSeries = false;
//                repQuery.setReportId(collect.reportId);
        timequery.setBizRoles(roleId);
        timequery.setUserId(userId);
        pbretObjForTime = timequery.getPbReturnObject(String.valueOf(detail.getComplexreportQryCols().get(0)));
        pbretObjForTime.resetViewSequence();
        ArrayList sortColumns = null;
        if (kPIFromReport.getSortColumns() != null) {
            sortColumns = new ArrayList(Arrays.asList(kPIFromReport.getSortColumns()));
        }

        reportTableBD.searchDataSet(kPIFromReport, pbretObjForTime);
        if (kPIFromReport.isTopBottomSet()) {
            if (kPIFromReport.getTopBtmMode().equalsIgnoreCase("TopBottomAbsRows")) {
                dataSeq = pbretObjForTime.findTopBottom(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
            } else {
                dataSeq = pbretObjForTime.findTopBottomPercentWise(sortColumns, kPIFromReport.getSortTypes(), kPIFromReport.getTopBtmCount());
            }
            pbretObjForTime.resetViewSequence();
            pbretObjForTime.setViewSequence(dataSeq);
        }
        retObjList.add(pbretObjForTime);
        StringBuilder imgtest = new StringBuilder();
        ArrayList alist1 = new ArrayList();
        ArrayList alist2 = new ArrayList();
        ArrayList alist3 = new ArrayList();
        ArrayList alist4 = new ArrayList();
        ArrayList alist5 = new ArrayList();
        alist1 = pbretObjForTime.retrieveDataBasedOnViewSeq(kPIFromReport.getMeasureId());
        if (priorId != "") {
            alist3 = pbretObjForTime.retrieveDataBasedOnViewSeq("A_" + priorId);
        }
        if (formatType != null && formatValue != null && detail != null && detail.getFormatVal() != null && detail.getRoundVal() != null && !detail.getRoundVal().equalsIgnoreCase("")) {
            for (int i = 0; i < alist1.size(); i++) {
                String value1 = NumberFormatter.getModifiedNumber(new BigDecimal(alist1.get(i).toString()), detail.getFormatVal(), Integer.parseInt(detail.getRoundVal()));
                alist2.add(value1);
                String value2 = NumberFormatter.getModifiedNumber(new BigDecimal(alist3.get(i).toString()), "", 0);
                alist5.add(value2);
            }
        } else {
            for (int i = 0; i < alist1.size(); i++) {
                String value1 = NumberFormatter.getModifiedNumber(new BigDecimal(alist1.get(i).toString()), "", 0);
                alist2.add(value1);
                String value2 = NumberFormatter.getModifiedNumber(new BigDecimal(alist3.get(i).toString()), "", 0);
                alist5.add(value2);
            }
        }
        RunTimeMeasure rtm = new RunTimeMeasure(alist1);
        ArrayList<BigDecimal> runTimeMeasData = null;
        pbretObjForTime.setRuntimeMeasure(kPIFromReport.getMeasureId(), alist1);
        runTimeMeasData = StatUtil.STAT_HELPER.Rank(pbretObjForTime.retrieveDataBasedOnViewSeq(kPIFromReport.getMeasureId()));
        RunTimeMeasure rtMeasure = new RunTimeMeasure(runTimeMeasData);

        RunTimeMeasure rtm1 = new RunTimeMeasure(alist3);
        ArrayList<BigDecimal> runTimeMeasData1 = null;
        pbretObjForTime.setRuntimeMeasure("A_" + priorId, alist3);
        runTimeMeasData1 = StatUtil.STAT_HELPER.Rank(pbretObjForTime.retrieveDataBasedOnViewSeq("A_" + priorId));
        String color = "";
        if (compareCheck == null || compareCheck.equalsIgnoreCase("rankCheck")) {
            for (int k = 0; k < runTimeMeasData1.size(); k++) {
                if (runTimeMeasData1.get(k).compareTo(runTimeMeasData.get(k)) > 0) {
                    color = "green";
                } else if (runTimeMeasData1.get(k).compareTo(runTimeMeasData.get(k)) < 0) {
                    color = "red";
                } else {
                    color = "";
                }
                alist4.add(color);
            }
        } else if (compareCheck != null && compareCheck.equalsIgnoreCase("valCheck")) {
            for (int k = 0; k < alist5.size(); k++) {
                if (Long.parseLong(alist5.get(k).toString().replace(",", "").replace(formatType, "")) < (Long.parseLong(alist2.get(k).toString().replace(",", "").replace(formatType, "")))) {
                    color = "green";
                } else if (Long.parseLong(alist5.get(k).toString().replace(",", "").replace(formatType, "")) > (Long.parseLong(alist2.get(k).toString().replace(",", "").replace(formatType, "")))) {
                    color = "red";
                } else {
                    color = "";
                }
                alist4.add(color);
            }
        } else {
            for (int k = 0; k < runTimeMeasData1.size(); k++) {
                if (runTimeMeasData1.get(k).compareTo(runTimeMeasData.get(k)) > 0) {
                    color = "green";
                } else if (runTimeMeasData1.get(k).compareTo(runTimeMeasData.get(k)) < 0) {
                    color = "red";
                } else {
                    color = "";
                }
                alist4.add(color);
            }
        }
        if (prefixValue == null) {
            prefixValue = "";
        }
        if (suffixValue == null) {
            suffixValue = "";
        }
        imgtest.append("<table style='width:100%'>");
//        imgtest.append("<tr height='30'></tr>");
        for (int i = 0; i < alist2.size(); i++) {
            // 
//                    String tooltipval = "<table><tr></tr><tr><td>Rank</td></tr><tr><td>Value</td></tr></table>";
//                    tooltipval+="<table><tr><td>Current:</td></tr><tr><td>"+runTimeMeasData.get(i)+"</td></tr><tr><td>"+alist1.get(i) +"</td></tr></table>";
//                    tooltipval+="<table><tr><td>Prior:</td></tr><tr><td>"+runTimeMeasData1.get(i) +"</td></tr><tr><td>"+alist3.get(i) +"</td></tr></table>";
            String tooltipval = "Current Rank:" + runTimeMeasData.get(i) + ", Prior Rank:" + runTimeMeasData1.get(i) + "";
            if (compareCheck == null || compareCheck.equalsIgnoreCase("rankCheck")) {
                tooltipval = "Current Rank:" + runTimeMeasData.get(i) + ", Prior Rank:" + runTimeMeasData1.get(i) + "";
            } else if (compareCheck != null && compareCheck.equalsIgnoreCase("valCheck")) {
                tooltipval = "Current Value:" + alist1.get(i) + ", Prior Value:" + alist3.get(i) + "";
            }
            if (alist4 != null && !alist4.isEmpty() && String.valueOf(alist4.get(i)).equalsIgnoreCase("green")) {
                imgtest.append("<tr><td class='myhead' style='color:#369;font-size:11pt;background-color:white;width:100%;'>" + pbretObjForTime.getFieldValueStringBasedOnViewSeq(i, "A_" + detail.getComplexrowviewbys().get(0)) + "</td><td width='2%'></td><td style='color:#369;font-size:12pt'>" + prefixValue + alist2.get(i).toString().replace(formatType, "") + "<span style='font-size:7pt;'>" + suffixValue + "</span></td><td><img id='tdImage'  title='" + tooltipval + "' src='" + request.getContextPath() + "/stylesheets/images/positive.GIF'/></td></tr>");
            } else if (alist4 != null && !alist4.isEmpty() && String.valueOf(alist4.get(i)).equalsIgnoreCase("red")) {
                imgtest.append("<tr><td class='myhead' style='color:#369;font-size:11pt;background-color:white;width:100%;'>" + pbretObjForTime.getFieldValueStringBasedOnViewSeq(i, "A_" + detail.getComplexrowviewbys().get(0)) + "</td><td width='2%'></td><td style='color:#369;font-size:12pt'>" + prefixValue + alist2.get(i).toString().replace(formatType, "") + "<span style='font-size:7pt;'>" + suffixValue + "</span></td><td><img id='tdImage' title='" + tooltipval + "' src='" + request.getContextPath() + "/stylesheets/images/negative.gif'/></td></tr>");
            } else {
                imgtest.append("<tr><td class='myhead' style='color:#369;font-size:11pt;background-color:white;width:100%;'>" + pbretObjForTime.getFieldValueStringBasedOnViewSeq(i, "A_" + detail.getComplexrowviewbys().get(0)) + "</td><td width='2%'></td><td style='color:#369;font-size:12pt'>" + prefixValue + alist2.get(i).toString().replace(formatType, "") + "<span style='font-size:7pt;'>" + suffixValue + "</span></td></tr>");
            }
            imgtest.append("<tr></tr><tr></tr>");
        }
        imgtest.append("</table>");
//        imgtest.append("</center>");



        PrintWriter out = response.getWriter();
        out.print(imgtest.toString());
        return null;
    }

    public ActionForward dashboardKpiAlerts(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            HttpSession session = request.getSession();
            ReportSchedule schedule = new ReportSchedule();

            String userId = "";
            String elementId = "";
            String kpiMasterId = "";
            String dashletId = "";
            String reportId = "";
            String dataSelection = "";
            String alertDate = "";
            elementId = request.getParameter("elmntId");
            kpiMasterId = request.getParameter("KpimasterSc");
            dashletId = request.getParameter("dashId");
            reportId = request.getParameter("repIdSch");
            String schedulerName = request.getParameter("schdReportName");
            String mailIds = request.getParameter("usertextarea");
            String startDate = request.getParameter("startdate");
            String endDate = request.getParameter("enddate");
            String folderId = request.getParameter("folderId");
            String elemtName = request.getParameter("elemtName");
            String firstViewByName = request.getParameter("firstViewByName");
            String secondViewByName = request.getParameter("secondViewByName");
            String headerLogo = request.getParameter("headerLogo");
            String footerLogo = request.getParameter("footerLogo");
            String optionalHeader = request.getParameter("optionalHeader");
            String optionalFooter = request.getParameter("optionalFooter");
            String htmlSignature = request.getParameter("htmlSignature");

            String totalrows = request.getParameter("totalrows");
//          ArrayList<String> arrayofSvalues = new ArrayList<>();
//          ArrayList<String> arrayofEvalues = new ArrayList<>();
//          ArrayList<String> operatorSymbol = new ArrayList<>();
//          ArrayList<String> arrayAlertType = new ArrayList<>();
//          ArrayList<String> arrcolumnHeaderNames = new ArrayList<>();
            String arrayofSvalues[] = new String[Integer.parseInt(totalrows)];
            String arrayofEvalues[] = new String[Integer.parseInt(totalrows)];
            String operatorSymbol[] = new String[Integer.parseInt(totalrows)];
            String arrayAlertType[] = new String[Integer.parseInt(totalrows)];
            String arrcolumnHeaderNames[] = new String[Integer.parseInt(totalrows)];

            for (int i = 0; i < Integer.parseInt(totalrows); i++) {
                arrayofSvalues[i] = request.getParameter("sValues" + i);
                arrayofEvalues[i] = request.getParameter(i + "eValues");
                operatorSymbol[i] = request.getParameter(i + "operators");
                arrayAlertType[i] = request.getParameter("alertType" + i);
                arrcolumnHeaderNames[i] = request.getParameter("columnHeaderNames" + i);
            }

            String alertType = request.getParameter("alertType");

            String yearly = request.getParameter("Year");
            String quartly = request.getParameter("Qtr");
            String monthly = request.getParameter("Month");
            String weekly = request.getParameter("Week");
            String daily = request.getParameter("Day");

            List<String> attacheDetails = new ArrayList<String>();

            if (yearly != null && !yearly.equalsIgnoreCase("")) {
                attacheDetails.add(yearly);
            }
            if (quartly != null && !quartly.equalsIgnoreCase("")) {
                attacheDetails.add(quartly);
            }
            if (monthly != null && !monthly.equalsIgnoreCase("")) {
                attacheDetails.add(monthly);
            }
            if (weekly != null && !weekly.equalsIgnoreCase("")) {
                attacheDetails.add(weekly);
            }
            if (daily != null && !daily.equalsIgnoreCase("")) {
                attacheDetails.add(daily);
            }
            dataSelection = request.getParameter("dailyData");

            ProgenParam pParam = new ProgenParam();
            if (dataSelection.equalsIgnoreCase("last")) {
                alertDate = pParam.getcurrentdateforpage(1);
            } else if (dataSelection.equalsIgnoreCase("current")) {
                alertDate = pParam.getcurrentdateforpage(0);
            } else if (dataSelection.equalsIgnoreCase("globalDate")) {
                alertDate = pParam.getdateforpage();
            }
            String mailId = request.getParameter("mail");
            String frequency = request.getParameter("frequency");
            String particularDay = request.getParameter("particularDay");
            String particularHour = request.getParameter("particularHour");
            String monthParticularDay = request.getParameter("monthParticularDay");
            String scheduledTime = "";
            String schedulerFrequency = "";
            String hrs = request.getParameter("hrs");
            String mins = request.getParameter("mins");
            String contentType = "H";
            scheduledTime = hrs.concat(":").concat(mins);
            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));

            Date sDate;
            Date eDate;
            DateFormat formatter;
            String value = "";
            String valu = "";
            String mont = "";
            String CurrValue = "";
            String value1 = "";
            String valu1 = "";
            String mont1 = "";
            String CurrValue1 = "";

            value = startDate;
            int slashval = value.indexOf("/");
            int slashLast = value.lastIndexOf("/");
            valu = value.substring(0, slashval);
            mont = value.substring(slashval + 1, slashLast + 1);
            CurrValue = mont.concat(valu).concat(value.substring(slashLast));
            startDate = CurrValue;

            value1 = endDate;
            int slashval1 = value1.indexOf("/");
            int slashLast1 = value1.lastIndexOf("/");
            valu1 = value1.substring(0, slashval1);
            mont1 = value1.substring(slashval1 + 1, slashLast1 + 1);
            CurrValue1 = mont1.concat(valu1).concat(value1.substring(slashLast1));
            endDate = CurrValue1;

            formatter = new SimpleDateFormat("MM/dd/yyyy");
            sDate = formatter.parse(startDate);
            eDate = formatter.parse(endDate);
            schedule.setFrequency(request.getParameter("frequency"));
            schedule.setReportId(Integer.parseInt(reportId));
            schedule.setSchedulerName(schedulerName);
            schedule.setContenType(contentType);
            schedule.setEndDate(eDate);
            schedule.setFrequency(frequency);
            schedule.setStartDate(sDate);
            schedule.setViewBy("");
            schedule.setViewByName(elemtName);
            schedule.setScheduledTime(scheduledTime);
            schedule.setUserId(userId);
            schedule.setReportmailIds(mailIds);
            schedule.setSchedulerName(schedulerName);
//        schedule.setReportScheduledId(Integer.parseInt(elementId));
            schedule.setElementID(elementId);
            schedule.setFolderId(folderId);
            schedule.setAlertDate(alertDate);
            schedule.setFromdsrbKpi(true);
            schedule.setStartValue(arrayofSvalues);
            schedule.setEndValue(arrayofEvalues);
            schedule.setOperatorSymbol(operatorSymbol);
            schedule.setAlertType(arrayAlertType);
            schedule.setColumnHeaderNames(arrcolumnHeaderNames);
            schedule.setFirstViewByName(firstViewByName);
            schedule.setSecondViewByName(secondViewByName);

            KPIScheduleHelper kpiScheduleHelper = new KPIScheduleHelper();
            schedule.setkPIScheduleHelper(kpiScheduleHelper);
            schedule.getkPIScheduleHelper().setKpiMasterId(kpiMasterId);
            schedule.getkPIScheduleHelper().setDashLetId(dashletId);
            schedule.getkPIScheduleHelper().setDashBoardID(reportId);
            schedule.getkPIScheduleHelper().setElementId(elementId);

            schedule.setIsHeaderLogoOn(headerLogo);
            schedule.setIsFooterLogoOn(footerLogo);
            schedule.setIsOptionalHeaderTextOn(optionalHeader);
            schedule.setIsOptionalFooterTextOn(optionalFooter);
            schedule.setIsHtmlSignatureOn(htmlSignature);
            if (frequency.equalsIgnoreCase("Weekly")) {
                schedule.setParticularDay(particularDay);
            } else if (frequency.equalsIgnoreCase("Monthly")) {
                schedule.setParticularDay(monthParticularDay);
            } else if (frequency.equalsIgnoreCase("Hourly")) {
                schedule.setParticularDay(particularHour);
            }
            schedule.setDataSelectionTypes(attacheDetails);
            SchedulerBD bd = new SchedulerBD();
            OneViewBD oneviewBd = new OneViewBD();
            oneviewBd.insertOneviewMesureAlerts(reportId, userId, schedule, kpiMasterId);
            bd.scheduleReport(schedule, false);

            out.print("Alert Scheduler has created successfully");
        } catch (Exception e) {
            out.print("Alert Scheduler is failed to create!");
        }
        return null;
    }

    public ActionForward localSaveInDbRegions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession();
        String dashBoardId = request.getParameter("dashBoardId");
        String dashletId = request.getParameter("dashletId");
        String kpiMasterId = request.getParameter("kpiMasterId");
//        request.getParameter("ViewBy");
        HashMap map = new HashMap();
        Container container = null;
        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(dashBoardId);
        String dashboardName = "";
        ArrayList updateQuerylist = new ArrayList();

        Boolean SqlServer = false;
        Boolean MySql = false;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            SqlServer = true;
            MySql = false;
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            MySql = true;
            SqlServer = false;
        } else {
            SqlServer = false;
            MySql = false;
        }

        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        dashboardName = collect.reportName;

        List<DashletDetail> dashletList = collect.dashletDetails;

        for (int i = 0; i < dashletList.size(); i++) {
            DashletDetail dashlet = dashletList.get(i);
            String reportType = dashlet.getDisplayType();
            String dashletDetailId = dashlet.getDashBoardDetailId();
            int row = dashlet.getRow();
            int col = dashlet.getCol();
            int rowSpan = dashlet.getRowSpan();
            int colSpan = dashlet.getColSpan();
            if (Integer.parseInt(dashletDetailId) == Integer.parseInt(dashletId)) {
                if (DashboardConstants.KPI_REPORT.equalsIgnoreCase(reportType)) {
                    List<String> kpiQueries = getKPIUpdateQueries(container, dashBoardId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan, kpiMasterId, container.ViewBy);
                    updateQuerylist.addAll(kpiQueries);
                }
            }
        }
        DashboardTemplateDAO paramdDAO = new DashboardTemplateDAO();
        paramdDAO.insertKpiGraphs(updateQuerylist);
        //

        DashboardViewerDAO viewerDao = new DashboardViewerDAO();

        if (MySql) {
        } else if (SqlServer) {
        } else {
            for (DashletDetail detail : dashletList) {
                if (detail != null && detail.getUpdateClobQry() != null && !detail.getUpdateClobQry().isEmpty()) {
                    viewerDao.procedureExecution(detail.getUpdateClobQry());
                }
            }
        }
        PrintWriter out = response.getWriter();
        out.print("true");


        return null;
    }

    public String buildQuery(String query, Object params[]) {
//        String finalQuery = "";
        String var = null;
        StringBuilder finalQuery = new StringBuilder(300);
        //Split the String sql command on basis of & character
        String str[] = query.split("&");

        //Loop through the String sql command and iconcatenate the parameters in place of & to build query
        for (int counter = 0; counter < str.length; counter++) {
            var = "";
            if (counter < params.length) {
                if (params[counter] != null) {
                    var = (params[counter]).toString();
                } else {
                    var = "";
                }
            }
            finalQuery.append(str[counter]).append(var.toString());
//            finalQuery += str[counter] + var.toString();
        }
        return finalQuery.toString();
    }

    public List<String> getKPIUpdateQueries(Container container, String dashboardId, String dashboardName, int dispSeq, DashletDetail dashlet, boolean sqlServer, boolean MySql, int row, int col, int rowSpan, int colSpan, String kpiMasterIdstr, ArrayList ViewBy) {
        List<String> queries = new ArrayList<String>();
        ArrayList<String> hidecolumns = new ArrayList<String>();//added by sruthi for hidecolumns
        KPI kpiDetail = (KPI) dashlet.getReportDetails();
        String kpiType = dashlet.getKpiType();
        HashMap<String, Double> manuvaltarget = new HashMap<String, Double>();//added by sruthi for manuval bud based on date
        Double targetVal = null;
        hidecolumns = dashlet.getHidecolumns();//added by sruthi for hidecolumns
        String kpiDashName = dashlet.getkpiName();
        String kpiSymbol = dashlet.getKpiSymbol();
        ArrayList<String> kpiheads = dashlet.getKpiheads();
        StringBuilder kpiheadsbuilder = new StringBuilder();
        HashMap parametersMap = container.getParametersHashMap();
        PbReportCollection collect = container.getReportCollect();
        HashMap timeDimHashMap = (HashMap) parametersMap.get("TimeDimHashMap");
        ArrayList params = (ArrayList) parametersMap.get("Parameters");
        StringBuffer ParametersOrder = new StringBuffer();
        StringBuffer Parameters = new StringBuffer();
        String kpiheadsstr = "";
        for (int i = 0; i < kpiheads.size(); i++) {
            kpiheadsbuilder.append(",").append(kpiheads.get(i));
        }
        if (0 < kpiheads.size()) {
            kpiheadsstr = kpiheadsbuilder.toString();
            kpiheadsstr = kpiheadsstr.substring(1);
        }
        List<String> elementids = kpiDetail.getElementIds();
        for (String elmntId : elementids) {
            if (dashlet.getSingleGroupHelpers() != null && !dashlet.getSingleGroupHelpers().isEmpty()) {
                List<KPISingleGroupHelper> kPISingleGroupHelpers = dashlet.getSingleGroupHelpers();
                for (KPISingleGroupHelper groupingHelper : kPISingleGroupHelpers) {
                    if (groupingHelper.getGroupName() != null) {
                        if (groupingHelper.getGroupName().equalsIgnoreCase(elmntId)) {
                            String drillVal1 = kpiDetail.getKPIDrill(elmntId);
                            groupingHelper.addGroupKPIDrill(elmntId, drillVal1);
                        }
                    }
                }
            }
        }
        //added by sruthi for manuval bud based on dates
        String numberofdays = dashlet.getNumberOfDays();
        HashMap<String, Double> targetdata = dashlet.getTargetMauval();
        for (String ids : elementids) {
            if (targetdata != null && !targetdata.isEmpty()) {
                targetVal = targetdata.get(ids);
            }
            // if(targetVal!=null)
            manuvaltarget.put(ids, targetVal);
        }
        //ended by sruthi
        Gson gson = new Gson();
        String kpiGroupString = "";
        if (dashlet.getSingleGroupHelpers() != null) {
            List<KPISingleGroupHelper> kPISingleGroupHelpers = dashlet.getSingleGroupHelpers();

            if (!kPISingleGroupHelpers.isEmpty()) {
                kpiGroupString = gson.toJson(kPISingleGroupHelpers);
            }
        }
        int kpiMasterId = Integer.parseInt(kpiMasterIdstr);

        //updating dashboard details table. This can be moved to the caller function. Common function for all the dashlet types
        //Start of code by Bhargavi for date saving on 6th july 2015
        Set details = timeDimHashMap.keySet();
        Iterator it = details.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            ArrayList timeDetails = (ArrayList) timeDimHashMap.get(key);
            String date;
            String dateinmysql = "";
            String[] dateformysql = ((String) timeDetails.get(0)).split("/");
            if (key.equalsIgnoreCase("AS_OF_DATE") || key.equalsIgnoreCase("AS_OF_DATE1") || key.equalsIgnoreCase("AS_OF_DATE2") || key.equalsIgnoreCase("CMP_AS_OF_DATE1") || key.equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                dateinmysql = dateformysql[2].trim() + "/" + dateformysql[0].trim() + "/" + dateformysql[1].trim();
            }

            if (timeDetails.get(0) != null) {
                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                    date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                    date = "date_format('" + dateinmysql + "','%Y-%m-%d')";
                } else {
                    date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                }

            } else {
                date = "null";
            }
            String kpiQuery = "UPDATE PRG_AR_REPORT_TIME_DETAIL SET DEFAULT_DATE =" + date + " WHERE REP_TIME_ID=(select REP_TIME_ID from prg_ar_report_time where report_id=" + dashboardId + ") and COLUMN_TYPE='" + key + "'";

            queries.add(kpiQuery);
        }
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                Parameters.append("," + String.valueOf(params.get(i)).replace("A_", ""));
            }
            Type targetType = new TypeToken<List<String>>() {
            }.getType();
            String[] paramIds = Parameters.substring(1).split(",");
            for (int i = 0; i < paramIds.length; i++) {
                List<String> defultVlueList = collect.getInValList(paramIds[i]);
                String defaultVal = "";

                if (defultVlueList != null && !defultVlueList.isEmpty()) {

                    defaultVal = gson.toJson(defultVlueList, targetType);
                } else {
                    defaultVal = "[\"All\"]";
                }
                String kpiQuery = "UPDATE PRG_AR_REPORT_PARAM_DETAILS SET DEFAULT_VALUE ='" + defaultVal + "',PARAMETER_TYPE='IN' WHERE report_id=" + dashboardId + " and ELEMENT_ID=" + paramIds[i] + "";
                queries.add(kpiQuery);

            }
        }
        //end of code by Bhargavi
        //changed by sruthi for hide columns
        if (sqlServer) {
            String kpiQuery = "update PRG_AR_DASHBOARD_DETAILS set REF_REPORT_ID = 0,GRAPH_ID = 0, DISPLAY_SEQUENCE = " + dispSeq + ","
                    + "DISPLAY_TYPE = 'KPI',KPI_HEADING = '" + dashboardName + " KPI',KPI_TYPE = '" + kpiType + "',"
                    + "ROW_1 = '" + row + "',COL_1 = '" + col + "',ROW_SPAN = '" + rowSpan + "',COL_SPAN = '" + colSpan + "',"
                    + "KPI_NAME = '" + kpiDashName + "',KPI_SYMBOL = '" + kpiSymbol + "',KPI_HEADS = '" + kpiheadsstr + "',"
                    + "DASHLET_KPIANDTABLE_PROP = '" + kpiGroupString + "',DEFAULT_VIEWBY='" + ViewBy + "' ,Hidecolumns='" + hidecolumns + "',NO_DAYS='" + numberofdays + "',TARGET_MANUAL='" + manuvaltarget + "' where DASHBOARD_ID = " + dashboardId + " and DASHBOARD_DETAILS_ID = '" + dashlet.getDashBoardDetailId() + "' and "
                    + "KPI_MASTER_ID = " + kpiMasterId + "";

            queries.add(kpiQuery);
        } else if (MySql) {
            String kpiQuery = "update PRG_AR_DASHBOARD_DETAILS set REF_REPORT_ID = 0,GRAPH_ID = 0, DISPLAY_SEQUENCE = " + dispSeq + ","
                    + "DISPLAY_TYPE = 'KPI',KPI_HEADING = '" + dashboardName + " KPI',KPI_TYPE = '" + kpiType + "',ROW_1 = '" + row + "',COL_1 = '" + col + "',"
                    + "ROW_SPAN = '" + rowSpan + "',COL_SPAN = '" + colSpan + "',KPI_NAME = '" + kpiDashName + "',KPI_SYMBOL = '" + kpiSymbol + "',"
                    + "KPI_HEADS = '" + kpiheadsstr + "',DASHLET_KPIANDTABLE_PROP = '" + kpiGroupString + "',DEFAULT_VIEWBY='" + ViewBy + "' ,Hidecolumns='" + hidecolumns + "' ,NO_DAYS='" + numberofdays + "',TARGET_MANUAL='" + manuvaltarget + "' where DASHBOARD_ID = " + dashboardId + " and "
                    + "DASHBOARD_DETAILS_ID = '" + dashlet.getDashBoardDetailId() + "' and KPI_MASTER_ID = " + kpiMasterId + "";

            queries.add(kpiQuery);
        } else {
            String kpiQuery = "update PRG_AR_DASHBOARD_DETAILS set REF_REPORT_ID = 0,GRAPH_ID = 0, DISPLAY_SEQUENCE = " + dispSeq + ","
                    + "DISPLAY_TYPE = 'KPI',KPI_HEADING = '" + dashboardName + " KPI',KPI_TYPE = '" + kpiType + "',ROW_1 = '" + row + "',COL_1 = '" + col + "',"
                    + "ROW_SPAN = '" + rowSpan + "',COL_SPAN = '" + colSpan + "',KPI_NAME = '" + kpiDashName + "',KPI_SYMBOL = '" + kpiSymbol + "',"
                    + "KPI_HEADS = '" + kpiheadsstr + "',DEFAULT_VIEWBY='" + ViewBy + "',Hidecolumns='" + hidecolumns + "',NO_DAYS='" + numberofdays + "',TARGET_MANUAL='" + manuvaltarget + "' where DASHBOARD_ID = " + dashboardId + " and DASHBOARD_DETAILS_ID = '" + dashlet.getDashBoardDetailId() + "' and KPI_MASTER_ID = " + kpiMasterId + "";

            queries.add(kpiQuery);

            DashboardViewerDAO dao = new DashboardViewerDAO();
            String queryStr = dao.prepareQueries(dashboardId, kpiMasterId, kpiGroupString);
            dashlet.setUpdateClobQry(queryStr);
        }
//ended by sruthi
        List<String> kpiElementIds = kpiDetail.getElementIds();
        int sequence = 1;
        int increment = 0;
        boolean isGrpElement = false;
        String deletekpiDetailsQuery = "";
        deletekpiDetailsQuery = "delete from PRG_AR_KPI_DETAILS where KPI_MASTER_ID = " + kpiMasterId + "";
        queries.add(deletekpiDetailsQuery);
        for (String elementId : kpiElementIds) {
            List<KPIElement> kpiElements = kpiDetail.getKPIElements(elementId);
            KPIElement currKPIElement = null;
            for (KPIElement element : kpiElements) {
                if (element.getElementId().equalsIgnoreCase(elementId)) {
                    currKPIElement = element;
                    isGrpElement = element.isIsGroupElement();
                    if (isGrpElement) {
                        elementId = null;
                    }
                    break;
                }
            }
            if (!kpiType.equalsIgnoreCase("Complexkpi")) {
                if (currKPIElement != null && currKPIElement.getElementName() != null) {
                    String kpiName = currKPIElement.getElementName();
                    String aggType = currKPIElement.getAggregationType();
                    String kpiStType = "Standard";
                    if (!isGrpElement) {
                        if (!(dashlet.getKpiType().equalsIgnoreCase("BasicTarget"))) {
                            if (!kpiDetail.getKpiStTypeHashMap().isEmpty() && kpiType.equalsIgnoreCase("BasicTarget")) {

                                HashMap<String, String> KpiStTypeHashMap = kpiDetail.getKpiStTypeHashMap();

                                Set<String> keyset = (Set<String>) KpiStTypeHashMap.keySet();
                                Object[] obj = keyset.toArray();
                                kpiStType = (String) KpiStTypeHashMap.get(obj[increment]);
                                increment++;
                            }
                        }
                    }
                    String drillVal = "0";
                    String drillRepType = "0";
                    String drillViewBys = "";
                    if (kpiDetail.getKPIDrill(elementId) != null) {
                        drillVal = kpiDetail.getKPIDrill(elementId);
                        drillRepType = kpiDetail.getKPIDrillRepType(elementId);
                        if (drillRepType == null) {
                            drillRepType = "0";
                        }
                        if (!drillVal.trim().equalsIgnoreCase("")) {
                            if (kpiDetail.getDrillViewBys() != null && !kpiDetail.getDrillViewBys().equalsIgnoreCase("") && !kpiDetail.getDrillViewBys().equalsIgnoreCase("(null)")) {
                                drillViewBys = kpiDetail.getDrillViewBys();
                            } else {
                                drillViewBys = "";
                            }
                        }
                    }
                    if (drillVal.trim().equalsIgnoreCase("")) {
                        drillVal = "0";
                    }
                    if (drillRepType.trim().equalsIgnoreCase("")) {
                        drillRepType = "0";
                    }
                    //updating kpi details table

                    String kpiDetailsQuery = "";

                    if (sqlServer || MySql) {
                        kpiDetailsQuery = "insert into PRG_AR_KPI_DETAILS(KPI_MASTER_ID,KPI_NAME,ELEMENT_ID,REF_REPORT_ID,KPI_SEQ,AGGREGATION,InsightViewStatus,CommentViewStatus,GraphViewStatus,KPI_ST_TYPE,MtdViewStatus,QtdViewStatus,YtdViewStatus,CurrentViewStatus,DRILL_VIEW_BYS,TARGET_ELEMENT,DRILL_TYPE) "
                                + "values (" + kpiMasterId + ",'" + kpiName + "'," + elementId + "," + drillVal + "," + sequence + ",'"
                                + aggType + "','" + kpiDetail.isShowInsights() + "','" + kpiDetail.isShowComments() + "','" + kpiDetail.isShowGraphs() + "','" + kpiStType + "','" + kpiDetail.isMTDChecked() + "','" + kpiDetail.isQTDChecked() + "','" + kpiDetail.isYTDChecked() + "','" + kpiDetail.isCurrentChecked() + "','" + drillViewBys + "','" + kpiDetail.getKpiTragetMap(elementId) + "','" + drillRepType + "')";
                    } else {
                        kpiDetailsQuery = "insert into PRG_AR_KPI_DETAILS(KPI_DETAILS_ID,KPI_MASTER_ID,KPI_NAME,ELEMENT_ID,REF_REPORT_ID,KPI_SEQ,AGGREGATION,InsightViewStatus,CommentViewStatus,GraphViewStatus,KPI_ST_TYPE,MtdViewStatus,QtdViewStatus,YtdViewStatus,CurrentViewStatus,DRILL_VIEW_BYS,TARGET_ELEMENT,DRILL_TYPE) "
                                + "values (PRG_AR_KPI_DETS_SEQ.nextval," + kpiMasterId + ",'" + kpiName + "'," + elementId + "," + drillVal
                                + "," + sequence + ",'" + aggType + "','" + kpiDetail.isShowInsights() + "','" + kpiDetail.isShowComments() + "','" + kpiDetail.isShowGraphs() + "','" + kpiStType + "','" + kpiDetail.isMTDChecked() + "','" + kpiDetail.isQTDChecked() + "','" + kpiDetail.isYTDChecked() + "','" + kpiDetail.isCurrentChecked() + "','" + drillViewBys + "','" + kpiDetail.getKpiTragetMap(elementId) + "','" + drillRepType + "')";
                    }


                    queries.add(kpiDetailsQuery);
                    sequence++;

                    //updating the kpi comments
                    List<KPIComment> kpiComments = kpiDetail.getKPIComments(elementId);
                    if (kpiComments != null) {
                        for (KPIComment kpiComment : kpiComments) {
                            String comment = kpiComment.getComment();
                            String userId = kpiComment.getUserId();
                            String commentsQuery = "";

                            if (sqlServer) {
                                commentsQuery = "update prg_kpi_user_comments set KPI_COMMENT = '" + comment + "',COMMENT_DATE = GETDATE() where "
                                        + "KPI_MASTER_ID = '" + kpiMasterId + "' and USER_ID = '" + userId + "' and ELEMENT_ID = '" + elementId + "'";
                            } else if (MySql) {
                                commentsQuery = "update prg_kpi_user_comments set KPI_COMMENT = '" + comment + "',COMMENT_DATE = NOW() where "
                                        + "KPI_MASTER_ID = '" + kpiMasterId + "' and USER_ID = '" + userId + "' and ELEMENT_ID = '" + elementId + "'";
                            } else {
                                commentsQuery = "update prg_kpi_user_comments set KPI_COMMENT = '" + comment + "',COMMENT_DATE = sysdate where "
                                        + "KPI_MASTER_ID = '" + kpiMasterId + "' and USER_ID = '" + userId + "' and ELEMENT_ID = '" + elementId + "'";
                            }
                            queries.add(commentsQuery);
                        }
                    }

                    //updating the kpi targets
                    List<KPITarget> kpiTargets = kpiDetail.getKPITargets(elementId);
                    if (kpiTargets != null) {
                        for (KPITarget kpiTarget : kpiTargets) {
                            String timeLevel = kpiTarget.getTimeLevel();
                            double targetValue = kpiTarget.getTargetValue();

                            String targetQuery = "";
                            if (sqlServer) {
                                targetQuery = "update dashboard_target_kpi_value set TIME_LEVEL = '" + timeLevel + "',TARGET_VALUE = " + targetValue + " "
                                        + "where KPI_MASTER_ID = " + kpiMasterId + " and ELEMENT_ID = " + elementId + " and DASHBOARD_ID = " + dashboardId + "";
                            } else if (MySql) {
                                targetQuery = "update dashboard_target_kpi_value set TIME_LEVEL = '" + timeLevel + "',TARGET_VALUE = " + targetValue + " "
                                        + "where KPI_MASTER_ID = " + kpiMasterId + " and ELEMENT_ID = " + elementId + " and DASHBOARD_ID = " + dashboardId + "";
                            } else {
                                targetQuery = "update dashboard_target_kpi_value set TIME_LEVEL = '" + timeLevel + "',TARGET_VALUE = " + targetValue + " "
                                        + "where KPI_MASTER_ID = " + kpiMasterId + " and ELEMENT_ID = " + elementId + " and DASHBOARD_ID = " + dashboardId + "";
                            }
                            queries.add(targetQuery);
                        }
                    }

                    //Updating kpi details table with the custom color values
                    List<KPIColorRange> kpiColorRanges = kpiDetail.getKPIColorRanges(elementId);
                    if (kpiColorRanges != null) {
                        String hrOperator = "";
                        double hr1 = 0;
                        double hr2 = 0;
                        String mrOperator = "";
                        double mr1 = 0;
                        double mr2 = 0;
                        String lrOperator = "";
                        double lr1 = 0;
                        double lr2 = 0;

                        for (KPIColorRange colorRange : kpiColorRanges) {
                            String color = colorRange.getColor();
                            if ("Green".equalsIgnoreCase(color)) {
                                hrOperator = colorRange.getOperator();
                                hr1 = colorRange.getRangeStartValue();
                                hr2 = colorRange.getRangeEndValue();
                            } else if ("Yellow".equalsIgnoreCase(color)) {
                                mrOperator = colorRange.getOperator();
                                mr1 = colorRange.getRangeStartValue();
                                mr2 = colorRange.getRangeEndValue();
                            }
                            if ("Red".equalsIgnoreCase(color)) {
                                lrOperator = colorRange.getOperator();
                                lr1 = colorRange.getRangeStartValue();
                                lr2 = colorRange.getRangeEndValue();
                            }
                        }

                        String updateKPIDetsQry = "update prg_ar_kpi_details set KPI_HRANGE_TYPE='" + hrOperator + "',KPI_HRANGE1='" + hr1 + "',KPI_HRANGE2='" + hr2 + "'"
                                + ",KPI_LRANGE_TYPE='" + lrOperator + "',KPI_LRANGE1='" + lr1 + "',KPI_LRANGE2='" + lr2 + "',KPI_MRANGE_TYPE='" + mrOperator + "',KPI_MRANGE1='" + mr1 + "'"
                                + ",KPI_MRANGE2='" + mr2 + "' where element_id=" + elementId + " and kpi_master_id=" + kpiMasterId;

                        queries.add(updateKPIDetsQry);
                    }
                }
            } //code for complex Kpi
            else {
                String kpiDetailsQuery = "";
                if (sqlServer || MySql) {
                    kpiDetailsQuery = "update PRG_AR_KPI_DETAILS set KPI_NAME = '" + kpiDetail.getelementNames(elementId) + "',REF_REPORT_ID = " + null + ","
                            + "KPI_SEQ = " + sequence + ",AGGREGATION = '" + null + "',InsightViewStatus = '" + kpiDetail.isShowInsights() + "',"
                            + "CommentViewStatus = '" + kpiDetail.isShowComments() + "',GraphViewStatus = '" + kpiDetail.isShowGraphs() + "',KPI_ST_TYPE = null,"
                            + "MtdViewStatus = '" + kpiDetail.isMTDChecked() + "',QtdViewStatus = '" + kpiDetail.isQTDChecked() + "',YtdViewStatus = '" + kpiDetail.isYTDChecked() + "',"
                            + "CurrentViewStatus = '" + kpiDetail.isCurrentChecked() + "' where KPI_MASTER_ID = " + kpiMasterId + " and ELEMENT_ID = " + elementId + "";
                } else {
                    kpiDetailsQuery = "update PRG_AR_KPI_DETAILS set KPI_NAME = '" + kpiDetail.getelementNames(elementId) + "',REF_REPORT_ID = " + null + ","
                            + "KPI_SEQ = " + sequence + ",AGGREGATION = '" + null + "',InsightViewStatus = '" + kpiDetail.isShowInsights() + "',"
                            + "CommentViewStatus = '" + kpiDetail.isShowComments() + "',GraphViewStatus = '" + kpiDetail.isShowGraphs() + "',KPI_ST_TYPE = null,"
                            + "MtdViewStatus = '" + kpiDetail.isMTDChecked() + "',QtdViewStatus = '" + kpiDetail.isQTDChecked() + "',YtdViewStatus = '" + kpiDetail.isYTDChecked() + "',"
                            + "CurrentViewStatus = '" + kpiDetail.isCurrentChecked() + "' where KPI_MASTER_ID = " + kpiMasterId + " and ELEMENT_ID = " + elementId + "";
                }
                queries.add(kpiDetailsQuery);

            }
            //end of If
        }
        return queries;
    }

    public ActionForward removeOneviewMeasComments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException, ParseException {
        HttpSession session = request.getSession(false);
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        HashMap map = null;
        OnceViewContainer onecontainer = new OnceViewContainer();
        String oneViewIdValue = request.getParameter("oneViewIdValue");
        if (session != null) {
            map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
            if (map != null) {
                onecontainer = (OnceViewContainer) map.get(oneViewIdValue);
                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();

                OneViewLetDetails detail = new OneViewLetDetails();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("colNo")));

                detail.measureComments.clear();

            } else {
                OneViewLetDetails detail = new OneViewLetDetails();

                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//                onecontainer = reportTemplateDAO.getOneViewData(oneViewIdValue);
                // String fileName = reportTemplateDAO.getOneviewFileName(oneViewIdValue);
                String fileName = session.getAttribute("tempFileName").toString();
                FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois2 = new ObjectInputStream(fis2);
                onecontainer = (OnceViewContainer) ois2.readObject();
                ois2.close();

                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("colNo")));
                detail.measureComments.clear();
//                reportTemplateDAO.updateOneviewData(onecontainer, oneViewIdValue);
                FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(onecontainer);
                oos.flush();
                oos.close();
            }

        }
        return null;
    }

    public ActionForward GetReportNamesforGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String folderId = request.getParameter("foldersIds");
        String graphId = request.getParameter("graphid");
        String graphName = request.getParameter("newGraphName");
        boolean fromoneview = Boolean.parseBoolean(request.getParameter("fromOneview"));
        StringBuilder sb = new StringBuilder();
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        String[] columnNames = null;
        PbReturnObject retObj = null;
        if (fromoneview) {
            retObj = dao.getOneviewFOrkpis(folderId);
        } else {
            retObj = dao.getGroupKpiDrillReports(null, folderId);
        }
        columnNames = retObj.getColumnNames();
        sb.append("<tr>");
        sb.append("<td class='myHead'>");
        if (fromoneview) {
            sb.append("Report");
        } else {
            sb.append(graphName);
        }
        sb.append("</td>");
        sb.append("<td>");
        sb.append("<select id='selectReportgr" + graphId.replace(",", "_") + "' width='45%' name='selectReportforGraph'> ");
        for (int i = 0; i < retObj.getRowCount(); i++) {
            sb.append("<option value='" + retObj.getFieldValueString(i, columnNames[0]) + "'>");
            sb.append(retObj.getFieldValueString(i, columnNames[1]));
            sb.append("</option>");
        }
        sb.append("</select>");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("~" + graphId.replace(",", "_"));
        PrintWriter out = response.getWriter();
        out.print(sb);
        return null;

    }
}
