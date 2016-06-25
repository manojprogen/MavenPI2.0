/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor
 */
package com.progen.dashboardView.action;

/**
 *
 * @author mahesh.sanampudi@progenbusiness.com
 */
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.Createtable.CreatetableDAO;
import com.progen.bd.ProgenJqplotGraphBD;
import com.progen.charts.JqplotGraphProperty;
import com.progen.dashboard.DashboardConstants;
import com.progen.dashboardView.bd.PbDashboardViewerBD;
import com.progen.dashboardView.db.DashboardViewerDAO;
import com.progen.graph.GraphBuilder;
import com.progen.jqplot.ProGenJqPlotChartTypes;
import com.progen.oneView.bd.OneViewBD;
import com.progen.report.entities.*;
import com.progen.report.insights.InsightBaseDetails;
import com.progen.report.insights.InsightsBD;
import com.progen.report.kpi.DashletPropertiesHelper;
import com.progen.report.kpi.KPIBuilder;
import com.progen.report.*;
import com.progen.report.query.PbReportQuery;
import com.progen.reportdesigner.action.GroupMeassureParams;
import com.progen.reportdesigner.bd.DashboardTemplateBD;
import com.progen.reportdesigner.db.DashboardTemplateDAO;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.db.PbReportViewerDAO;
import java.io.*;
import java.lang.reflect.Type;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.*;

/**
 *
 * @author Administrator
 */
public class PbDashboardViewerAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(PbDashboardViewerAction.class);
    //boolean isFxCharts = false;

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("viewDashboard", "viewDashboard");
        map.put("displayDashboard", "displayDashboard");
        map.put("displayGraph", "displayGraph");
        map.put("displayKPI", "displayKPI");
        map.put("displayScoreCard", "displayScoreCard");
        map.put("displayKPIDashGraph", "displayKPIDashGraph");
        map.put("forwardToDashBoard", "forwardToDashBoard");

        map.put("saveKPICustomColorVals", "saveKPICustomColorVals");
        map.put("getKPIInsightViewerPage", "getKPIInsightViewerPage");
        map.put("dispDbrdTable", "dispDbrdTable");
        map.put("buildKPI", "buildKPI");
        map.put("backToDashboard", "backToDashboard");
        map.put("buildKPIWithGraph", "buildKPIWithGraph");
        map.put("buildGraphOnKPI", "buildGraphOnKPI");
        map.put("excelDownload", "excelDownload");
        map.put("displayKpiWithGraph", "displayKpiWithGraph");
        map.put("reOrderKpi", "reOrderKpi");
        map.put("setKpiProperties", "setKpiProperties");
        map.put("drillToReportForKpiWithGraph", "drillToReportForKpiWithGraph");
        map.put("saveReportForKpiWithGraph", "saveReportForKpiWithGraph");
        map.put("buildGraphOnViewby", "buildGraphOnViewby");
        map.put("getInitialViewofOLAPGraph", "getInitialViewofOLAPGraph");
        map.put("closeOLAPView", "closeOLAPView");
        map.put("getOLAPGraphforOneView", "getOLAPGraphforOneView");
        map.put("buildDBJqplotGraph", "buildDBJqplotGraph");
        map.put("getOneViewRollingGraphJQ", "getOneViewRollingGraphJQ");
        map.put("assignGraphtoMeasure", "assignGraphtoMeasure");
        map.put("oneViewSettings", "oneViewSettings");
        map.put("renameDashBoard", "renameDashBoard");
        map.put("getOLAPGraphforOneViewd3", "getOLAPGraphforOneViewd3");
        map.put("buildqucktrend", "buildqucktrend");//sandeep
        map.put("showViewBydb", "showViewBydb");//sandeep
        map.put("getQueryReturnObject", "getQueryReturnObject");//mohit
        return map;
    }

    /*
     * This method is called when the user opens a dashboard or performs an
     * action on the existing dashboard. Functions Performed : Creates a
     * consolidated query for all the reports in the dashboard and stores the
     * return object in the conatiner
     */
    public ActionForward viewDashboard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        StringBuffer url = request.getRequestURL();
        HttpSession session = request.getSession(false);
        String action = request.getParameter("action");
        boolean isReset = Boolean.parseBoolean(request.getParameter("resetFlag"));
        boolean fromOneview = false;
        if (Boolean.parseBoolean(request.getParameter("fromOneview"))) {
            fromOneview = true;
        } else if (request.getAttribute("OneviewTest") != null) {
            fromOneview = true;
        }
        String rowViewByArray = request.getParameter("RowViewByArray");
        String DashboardId = null;
        String editDashboard = "false";
//        String DashboardName = null;
        String UserId = null;
        PbDashboardViewerBD dashboardViewerBD = new PbDashboardViewerBD();
        HashMap map = null;
        Container container = null;
        boolean isFxCharts = false;
        String drillfromRepId = "0";
        if (!fromOneview) {
            ServletContext context = this.getServlet().getServletContext();
            isFxCharts = Boolean.parseBoolean(context.getInitParameter("isFxCharts"));
        }
        if (session != null && session.getAttribute("USERID") != null) {
            try {
                if (request.getParameter("REPORTID") != null) {
                    DashboardId = request.getParameter("REPORTID");
                    editDashboard = request.getParameter("editDbrd");
//                    DashboardName = request.getParameter("pagename");
                } else if (request.getAttribute("REPORTID") != null) {
                    DashboardId = String.valueOf(request.getAttribute("REPORTID"));
                    editDashboard = String.valueOf(request.getAttribute("editDbrd"));
                    //DashboardName=String.valueOf(request.getAttribute("pagename"));
                }

                UserId = String.valueOf(session.getAttribute("USERID"));
                url.append("?reportBy=viewDashboard;REPORTID=").append(DashboardId).append(";action=paramChange");
                request.setAttribute("url", url.toString());
                if (request.getParameter("drillfromrepId") != null) {
                    drillfromRepId = request.getParameter("drillfromrepId").toString();
                }

                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(DashboardId) != null && !isReset) {
                        container = (Container) map.get(DashboardId);
                    } else {
                        container = new Container();
                        pbDashboardCollection collect = new pbDashboardCollection();
                        container.setReportCollect(collect);
                    }
                } else {
                    container = new Container();
                    pbDashboardCollection collect = new pbDashboardCollection();
                    container.setReportCollect(collect);
                }
                container.setReportId(DashboardId);
                container.setTableId(DashboardId);
                container.setSessionContext(session, container);
                container.setDrillfromRepId(drillfromRepId);
                ArrayList rowviewnames = new ArrayList<String>();

//                if(request.getParameter("RowViewByArray") != null && !request.getParameter("RowViewByArray").equalsIgnoreCase(""))
                if (request.getParameter("RowViewByArray") != null) {
//ArrayList rowViewByLst = new ArrayList<>();
                    if (!request.getParameter("RowViewByArray").equalsIgnoreCase("None") && !request.getParameter("RowViewByArray").equalsIgnoreCase("")) {
                        String[] rowIdArr = rowViewByArray.split(",");
                        rowviewnames = new ArrayList<>(Arrays.asList(rowIdArr));
                        pbDashboardCollection collect = new pbDashboardCollection();
                        collect = (pbDashboardCollection) container.getReportCollect();

                        HashMap reportParam = collect.reportParameters;
//   if(!rowViewByLst.isEmpty()){
//   for(int i=0;i<rowViewByLst.size();i++){
//         ArrayList al1 = null;
//       String elemntid=   (String) rowViewByLst.get(i);
//        if (reportParam != null && !reportParam.isEmpty()) {
//             al1 = (ArrayList) reportParam.get(elemntid);
//               rowviewnames.add(al1.get(1).toString());
//        }
//   }
//                    }
//     if(!rowViewByLst.isEmpty()){
                        container.setViewBy(rowviewnames);
//                }
//                    container.ViewBy=request.getParameter("ViewBy").split("_")[1];
                    } else {
                        container.setViewBy(rowviewnames);
                    }
                } else if (container.ViewBy.isEmpty() && request.getParameter("ViewBy") == null) {
                    String GetDefaultViewBy = dashboardViewerBD.GetDefaultViewBy(DashboardId);
                    if (GetDefaultViewBy != null && !GetDefaultViewBy.equalsIgnoreCase("") && !GetDefaultViewBy.equalsIgnoreCase("[]")) {
                        String[] split = GetDefaultViewBy.replaceAll("\\[", "").replaceAll("\\]", "").trim().split(",");
                        for (int i = 0; i < split.length; i++) {
                            split[i] = split[i].trim();
                        }
                        rowviewnames = new ArrayList<>(Arrays.asList(split));              //added by mohit
//                    rowviewnames.add(GetDefaultViewBy);
                        container.setViewBy(rowviewnames);
                    } else {
                        container.setViewBy(rowviewnames);
                    }
                }
//               else
//              {
//                       container.setViewBy(rowviewnames);
//              }


                /*
                 * Added by santhosh.k on 04-03-2010 for optimizing DashBoard
                 */
                HashMap DBKPIHashMap = container.getDBKPIHashMap();
                HashMap DBKPIGraphHashMap = container.getDBKPIGraphHashMap();
                HashMap DBGraphHashMap = container.getDBGraphHashMap();

                DBKPIHashMap = (DBKPIHashMap == null) ? new HashMap() : DBKPIHashMap;
                DBKPIGraphHashMap = (DBKPIGraphHashMap == null) ? new HashMap() : DBKPIGraphHashMap;
                DBGraphHashMap = (DBGraphHashMap == null) ? new HashMap() : DBGraphHashMap;

                container.setDBKPIHashMap(DBKPIHashMap);
                container.setDBKPIGraphHashMap(DBKPIGraphHashMap);
                container.setDBGraphHashMap(DBGraphHashMap);

                //for making static variables null for next dashboard

                container.setTimeParameterHashMap(new HashMap());
                container.setReportParameterHashMap(new HashMap());
                container.setReportParameterNames(new ArrayList());
                container.setMoreKpiDetails(new LinkedHashMap());
                container.setKpiQuery(new HashMap());

                container.setSavegraphchanges(new LinkedHashMap());


//            String dashbdid = request.getParameter("dashbdId");
                ////.println("dashbdid is in editdashboardname: " + dashbdid);
                String DashbdName = null;
                String Dashbddesc = null;
                PbDb pbdb = new PbDb();

                String dashNameQry = "select REPORT_NAME,REPORT_DESC from PRG_AR_REPORT_MASTER where REPORT_ID=" + DashboardId;
                // //.println("dashnameqry-in ---" + dashNameQry);
                PbReturnObject dashNameObj = pbdb.execSelectSQL(dashNameQry);

                if (dashNameObj.getRowCount() > 0) {
                    DashbdName = dashNameObj.getFieldValueString(0, 0);
                    Dashbddesc = dashNameObj.getFieldValueString(0, 1);
                }
                container.setDbrdName(DashbdName);
                container.setDbrdDesc(Dashbddesc);
                String ddform = null;
                if (session.getAttribute("dateFormat") != null) {
                    ddform = session.getAttribute("dateFormat").toString();
                }
                container.setDateFormat(ddform);
                if (session.getAttribute("MeasureDrillTest") != null) {
                    session.removeAttribute("MeasureDrillTest");
                }

                request.setAttribute("REPORTID", DashboardId);
                if (request.getParameter("editDbrd") != null) {
                    request.setAttribute("edit", editDashboard);
                }

                dashboardViewerBD.setIsFxCharts(isFxCharts);
                dashboardViewerBD.setHttpSession(session);
                dashboardViewerBD.setDashBoardId(DashboardId);
                dashboardViewerBD.setUserId(UserId);
                dashboardViewerBD.setServletRequest(request);
                dashboardViewerBD.setServletResponse(response);
                dashboardViewerBD.setContainer(container);
                if (fromOneview) {
                    dashboardViewerBD.displayDataForOneviewKpis(request, action, UserId);
                } else {
                    dashboardViewerBD.displayDashboardBD(action, UserId);
                }

                container.setSessionContext(session, container);
                if (fromOneview) {
//                    response.getWriter().print("Fine");
                    return null;
                } else {
                    if ("true".equals(editDashboard)) {
                        return mapping.findForward("editDashboard");
                    } else {
                        return mapping.findForward("displayDashboard");
                    }
                }
            } catch (Exception exp) {
                logger.error("Exception:", exp);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward displayKPI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = null;
        Container container = null;
        String dashBoardId = null;
        String kpiMasterId = null;
        String kpiDrill = null;
        pbDashboardCollection collect = null;
        KPIBuilder kpibuilder = new KPIBuilder();
        PrintWriter out = response.getWriter();
        String fromDesigner = null;
        String result = "";
        String editDbrd = null;

        String dashletId = null;
        HashMap DBKPIHashMap = null;

        if (session != null && session.getAttribute("USERID") != null && session.getAttribute("PROGENTABLES") != null) {
            dashBoardId = request.getParameter("dashBoardId");
            kpiMasterId = request.getParameter("kpiMasterId");
            kpiDrill = request.getParameter("kpiDrill");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            dashletId = request.getParameter("dashletId");
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
            String userId = (String) session.getAttribute("USERID");
            DashletDetail detail = collect.getDashletDetail(dashletId);
            if (!detail.getKpiType().equalsIgnoreCase("Complexkpi")) {
                result = kpibuilder.processSingleKpi(container, kpiMasterId, collect.kpiQuery, kpiDrill, dashletId, dashBoardId, createForDesigner, collect, userId, editDbrd);
            } else {
                KPI kpiDetails = (KPI) detail.getReportDetails();
                List<String> a1 = kpiDetails.getElementIds();
                String ElemntIds[] = new String[a1.size()];
                for (int i = 0; i < a1.size(); i++) {
                    ElemntIds[i] = a1.get(i);
                }
                DashboardTemplateDAO dao = new DashboardTemplateDAO();
                result = dao.getBuildCreateKPI(ElemntIds, request, dashBoardId);
            }
        }

        out.print(result);
        return null;
    }
    //sandeep

    public ActionForward showViewBydb(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        HashMap map = null;
        HashMap allViewBys = new HashMap();
        ArrayList<String> allViewIds = new ArrayList<String>();
        ArrayList<String> allViewNames = new ArrayList<String>();
        ArrayList<String> rowViewIdList = new ArrayList<String>();
        ArrayList<String> colViewIdList = new ArrayList<String>();
        ArrayList<String> rowNamesLst = new ArrayList<String>();
        ArrayList<String> colNamesLst = new ArrayList<String>();
        String rowName = "";
        String colName = "";
        String ViewFrom = String.valueOf(session.getAttribute("ViewFrom"));
        Container container = null;
        String reportId = request.getParameter("REPORTID");
        String fromdesigner = request.getParameter("fromdesigner");
        String globalfilter = request.getParameter("globalfilter");
        String fromdashboard = request.getParameter("fromdashboard");
        if (fromdesigner == null) {
            fromdesigner = "";
        }
        PbReportCollection collect = new PbReportCollection();
        HashMap ParameterMap = new HashMap();
        String selectedParams = "";

        if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportId);
            }
            if (container != null) {
                selectedParams = container.getSelectedParameterIds();
                ArrayList alist = new ArrayList();
                String[] selectedParamsArray = null;
                if (selectedParams != null && !selectedParams.equalsIgnoreCase("")) {
                    selectedParamsArray = selectedParams.split(",");
                }
                if (selectedParamsArray != null) {
                    for (int i = 0; i < selectedParamsArray.length; i++) {
                        alist.add(selectedParamsArray[i]);
                    }
                }
                if (ViewFrom.equalsIgnoreCase("Designer") && !fromdesigner.equalsIgnoreCase("fromdesigner")) {
                    ParameterMap = container.getParametersHashMap();
                    allViewIds = (ArrayList<String>) ParameterMap.get("Parameters");
                    allViewNames = (ArrayList<String>) ParameterMap.get("ParametersNames");
                } else {
                    collect = container.getReportCollect();
                    if (fromdashboard != null && fromdashboard.equalsIgnoreCase("true")) {
                        collect = new pbDashboardCollection();
                        collect = (pbDashboardCollection) container.getReportCollect();
                    }
                    allViewBys = collect.getReportParameters();
                    String[] allKeys = (String[]) (allViewBys.keySet()).toArray(new String[0]);
                    for (int i = 0; i < allViewBys.size(); i++) {
                        allViewIds.add(allKeys[i]);
                        allViewNames.add(collect.getElementName(allKeys[i]));
                    }
                    for (int i = 0; i < alist.size(); i++) {
                        allViewIds.remove(alist.get(i));
                        allViewNames.remove(collect.getElementName((String) alist.get(i)));

                    }
                    allViewIds.add("TIME");
                    allViewNames.add("Time");
                }
                rowViewIdList = container.getViewBy();
                colViewIdList = collect.reportColViewbyValues;
                if (globalfilter != null && globalfilter.equalsIgnoreCase("true")) {
                    rowViewIdList = container.getqfilters();
                }
                if (rowViewIdList != null && rowViewIdList.size() != 0) {
                    for (int i = 0; i < rowViewIdList.size(); i++) {
                        rowName = (String) rowViewIdList.get(i);
                        if (rowName.equalsIgnoreCase("Time")) {
                            rowNamesLst.add("Time");
                        } else {
                            rowNamesLst.add(collect.getParameterDispName(rowName));
                        }
                    }
                }
                if (colViewIdList != null && colViewIdList.size() != 0) {
                    for (int i = 0; i < colViewIdList.size(); i++) {
                        colName = (String) colViewIdList.get(i);
                        if (colName.equalsIgnoreCase("Time")) {
                            colNamesLst.add("Time");
                        } else {
                            colNamesLst.add(collect.getParameterDispName(colName));
                        }
                    }
                }
                //by gopesh for test add measures in change view by section
                if (container.getTableHashMap() != null && !container.getTableHashMap().isEmpty()) {
                    ArrayList aggregationType = new ArrayList();
                    ArrayList nameListName = (ArrayList) (container.getTableHashMap().get("MeasuresNames"));
//                ArrayList nameListName = collect.reportQryColNames;
                    ArrayList nameListIds = (ArrayList) (container.getTableHashMap().get("Measures"));
//                ArrayList nameListIds = collect.reportQryElementIds;

                    //for test hybrid summ measures
                    if (container.isSummarizedMeasuresEnabled()) {
                        HashMap summarizedmMesMap = container.getSummerizedTableHashMap();
                        if (summarizedmMesMap != null && !summarizedmMesMap.isEmpty()) {
                            nameListIds.addAll((List<String>) summarizedmMesMap.get("summerizedQryeIds"));
                            nameListName.addAll((List<String>) summarizedmMesMap.get("summerizedQryColNames"));
                        }
                    }

                    aggregationType = collect.reportQryAggregations;
                    for (int i = 0; i < nameListIds.size(); i++) {

                        if (nameListIds.get(i).toString() != null && (nameListIds.get(i).toString().contains("_percentwise") || nameListIds.get(i).toString().contains("_rank") || nameListIds.get(i).toString().contains("_wf") || nameListIds.get(i).toString().contains("_wtrg") || nameListIds.get(i).toString().contains("_rt") || nameListIds.get(i).toString().contains("_pwst") || nameListIds.get(i).toString().contains("_excel") || nameListIds.get(i).toString().contains("_excel_target") || nameListIds.get(i).toString().contains("_deviation_mean") || nameListIds.get(i).toString().contains("_gl") || nameListIds.get(i).toString().contains("_userGl") || nameListIds.get(i).toString().contains("_timeBased") || nameListIds.get(i).toString().contains("_changedPer") || nameListIds.get(i).toString().contains("_glPer"))) {
                            aggregationType.add("SUM");
                        }
                    }
                    if (request.getParameter("isGraphObject") != null && request.getParameter("isGraphObject").equalsIgnoreCase("y")) {
                        try {
                            for (Object nameList11 : nameListIds) {
                                allViewIds.add(nameList11.toString());
                            }
                            for (Object nameList12 : nameListName) {
                                allViewNames.add(nameList12.toString());
                            }
                        } catch (Exception e) {
                        }
                    }

                    if (container.isSummarizedMeasuresEnabled()) {
                        HashMap summarizedmMesMap = container.getSummerizedTableHashMap();
                        if (summarizedmMesMap != null && !summarizedmMesMap.isEmpty()) {
                            aggregationType.addAll((List<String>) summarizedmMesMap.get("summerizedQryAggregations"));
                        }
                    }
                    session.setAttribute("aggType", aggregationType);

                    if (fromdesigner == null || fromdesigner.equalsIgnoreCase("")) {
                        session.setAttribute("rowMeasIdList", nameListIds);
                        session.setAttribute("rowMeasNamesLst", nameListName);
                    }
                }

                session.setAttribute("allViewIds", allViewIds);
                session.setAttribute("allViewNames", allViewNames);

                if (fromdesigner == null || fromdesigner.equalsIgnoreCase("")) {
                    session.setAttribute("rowViewIdList", rowViewIdList);

                    session.setAttribute("colViewIdList", colViewIdList);
                    session.setAttribute("rowNamesLst", rowNamesLst);

                    session.setAttribute("colNamesLst", colNamesLst);
                }
                if (fromdesigner != null || !fromdesigner.equalsIgnoreCase("")) {
                    if (allViewIds == null || allViewIds.isEmpty() || allViewBys.size() == 0) {
                        PrintWriter out = null;
                        out = response.getWriter();
                        out.print("NoViewBys");

                    }

                }
            }
        }

        return null;
    }
    //sandeep

    public ActionForward buildqucktrend(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String dashboardId = request.getParameter("dashboardId");
        String kpiMasterId = request.getParameter("kpiMasterId");
        String dashletId = request.getParameter("dashletId");


        String kpis = request.getParameter("Kpis");
        String measurename = request.getParameter("measurename");
        String kpiNames = request.getParameter("KpiNames");
        String kpiType = request.getParameter("kpiType");
        String viewchange = request.getParameter("viewchange");
        //added by sruthi for viewby graph
        String viewbyid = request.getParameter("viewbyid");
        String viewbyname = request.getParameter("viewbyname");

        //ended by sruthi
        HttpSession session = request.getSession(false);
        //added by sruthi for viewby graph
        //ended by sruthi
        HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
        Container container = (Container) map.get(dashboardId);
        Container container1 = (Container) map.get(dashboardId);
        Collection<String> seqElments = null;
        ArrayList<String> datatypes = new ArrayList<String>();
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        List<DashletDetail> dashlets = collect.dashletDetails;
        DashletDetail dashlet = collect.getDashletDetail(dashletId);
        DashletDetail dashlet1 = collect.getDashletDetail(dashletId);
        HashMap parameterHashMap = container.getparameterHash();
        HashMap map1 = collect.reportParameters;
        ArrayList tempTimeDetailsArrayList = (ArrayList) collect.timeDetailsArray.clone();
        HashMap<String, List> inMapcopy = (HashMap<String, List>) collect.operatorFilters.get("IN");
        ArrayList timedetails = new ArrayList();

        ArrayList al11 = null;
        ArrayList<String> paramnames = new ArrayList<String>();
        ArrayList<String> paramids = new ArrayList<String>();
        //List al2=null;
        String[] a11 = null;               //  for(int i=0; i <map.size() ; i++){ ;
        if (map1 != null && (!map1.isEmpty())) {
            a11 = (String[]) (map1.keySet()).toArray(new String[map1.size()]);
            for (int j = 0; j < map1.size(); j++) {
                al11 = (ArrayList) map1.get(a11[j]);
                paramnames.add(al11.get(1).toString());
                paramids.add(a11[j]);
            }
        }
//            ArrayList paramids= (ArrayList) parameterHashMap.get("parameters");
        String rowviewbyid = null;
        String rowviewbyid1 = null;

        String rowviewbyname = null;
        String viewbychangerunid = null;
        String viewbychangerun = null;
//            paramnames= (ArrayList) parameterHashMap.get("parametersNames");

        if ((viewchange != null && viewchange.equalsIgnoreCase("TIME"))) {
            rowviewbyid = "";

        } else {
            if (paramnames != null && paramnames.size() > 0) {
                for (int i = 0; i < paramnames.size(); i++) {
                    if (paramnames.get(i).toString().equalsIgnoreCase(viewchange) || paramnames.get(i).toString().equalsIgnoreCase("Month - Year") || paramnames.get(i).toString().equalsIgnoreCase("Month-Year")) {
                        rowviewbyid = paramids.get(i).toString();
                        rowviewbyname = paramnames.get(i).toString();
                        viewbychangerunid = rowviewbyid;
                        viewbychangerun = rowviewbyname;
                    }

                }
            }
        }

        if (rowviewbyid == null || rowviewbyid == "") {
            rowviewbyid = "TIME";
            rowviewbyname = "TIME";
            datatypes.add("C");
            viewbychangerunid = rowviewbyid;
            viewbychangerun = rowviewbyname;
            rowviewbyid1 = rowviewbyid;
            timedetails.add("Day");
            timedetails.add("PRG_STD");
            timedetails.add(collect.timeDetailsArray.get(3));
            timedetails.add("Month");
            timedetails.add("Complete Same Month Last Year");
            collect.timeDetailsArray = timedetails;
        } else {

            rowviewbyid1 = "A_" + rowviewbyid;
            datatypes.add("C");
        }
        dashlet.setGraphtype("olapgraph");
        dashlet.setRowViewBy(rowviewbyid);
        //New KPI Region is added. A new dashlet is created for this region
        // if (dashlet == null||dashlet.getDashletName()==null){

        kpiMasterId = dashlet.getKpiMasterId();
        if (kpiMasterId == null) {
            kpiMasterId = request.getParameter("kpiMasterId");
        }
        KPI kpiDetail = (KPI) dashlet.getReportDetails();
        Report reportDetails1 = dashlet.getReportDetails();

        List<String> newKPIs = new ArrayList<String>();
        List<String> kpiIds = new ArrayList<String>();
        List<String> kpiIds1 = new ArrayList<String>();
        HashMap<String, String> kpidrill = (HashMap<String, String>) kpiDetail.getKPIDrill();
        HashMap<Integer, String> seqElmentsMap = (HashMap<Integer, String>) kpiDetail.getKPISequenceHashMap().clone();
        seqElments = seqElmentsMap.values();
        kpiDetail.getKPISequenceHashMap().clear();
        List<KPIElement> elemList12 = kpiDetail.getKPIElements();
        kpiIds1 = kpiDetail.getElementIds();
        if (kpis != null) {
            String[] selectedKPIs = kpis.split(",");
            for (int indx = 0; indx < selectedKPIs.length; indx++) {
//            for (String elementId:selectedKPIs){
                kpiIds.add(selectedKPIs[indx]);
                List<KPIElement> elements = kpiDetail.getKPIElements(selectedKPIs[indx]);
                if (elements == null || elements.isEmpty()) {
                    newKPIs.add(selectedKPIs[indx]);
                }
                kpiDetail.getKPISequenceHashMap().put(indx + 1, selectedKPIs[indx]);
            }
            if (seqElments != null && !seqElments.isEmpty()) {
                for (String string : seqElments) {
                    if (!kpiDetail.getKPISequenceHashMap().containsValue(string)) {
                        if (dashlet.isGroupElement(string)) {
                            dashlet.removeSingleGroup(string);
                        }
                    }
                }
            }
        }
        for (String kpiId : kpiIds) {
            if (!kpidrill.containsKey(kpiId)) {
                kpiDetail.addKPIDrill(kpiId, "0");
            }
        }

        kpiDetail.setElementIds(kpiIds);
        DashboardViewerDAO dao = new DashboardViewerDAO();
        PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();
        String userId = String.valueOf(session.getAttribute("USERID"));
        List<KPIElement> kpiElements = dao.getKPIElements(newKPIs, new HashMap<String, String>());
        for (KPIElement kpiElem : kpiElements) {
            kpiDetail.addKPIElement(kpiElem.getRefElementId(), kpiElem);
        }

        if (DashboardConstants.KPI_TYPE_TARGET.equalsIgnoreCase(dashlet.getKpiType())) {
            //Populate the target values from PRG_AR_TARGET_DATA
            if (!newKPIs.isEmpty()) {
                List<TargetData> targetList = dao.getTargetData(newKPIs);
                if (targetList != null) {
                    for (TargetData tgtData : targetList) {
                        kpiDetail.addTargetData(tgtData);
                    }
                }
            }
        }
        ArrayList<String> queryCols = new ArrayList<String>();
        ArrayList<String> queryCols1 = new ArrayList<String>();

        ArrayList<String> colNames = new ArrayList<String>();
        ArrayList<String> links = new ArrayList<String>();
        ArrayList<String> viewbyids = new ArrayList<String>();
        ArrayList<String> queryAggs = new ArrayList<String>();
        Set<KPIElement> elemSet = new LinkedHashSet<KPIElement>();

        queryCols.add(rowviewbyid);
        queryCols1.add(rowviewbyid1);

        List<KPIElement> elemList = kpiDetail.getKPIElements();
        elemSet.addAll(elemList);
        if (elemSet != null && !elemSet.isEmpty()) {
            Iterator<KPIElement> iter = elemSet.iterator();
            while (iter.hasNext()) {
                KPIElement elem = iter.next();
                if (elem != null) {
                    String refElementId = elem.getRefElementId();
                    if (!elem.isIsGroupElement()) {
                        queryCols.add("A_" + elem.getElementId());       //Get the element id
                        queryCols1.add("A_" + elem.getElementId());       //Get the element id
                        queryAggs.add(elem.getAggregationType());
                        datatypes.add("N");
                        //Get the aggregation type
//                collect.kpiElementList.put(refElementId, elem);
                    }
                }
            }
        }
//             List<String> colNames = new ArrayList();
        colNames.add(rowviewbyname);
        colNames.add(measurename);
        dashlet.setReportDetails(kpiDetail);
        //added by sruthi for viewby graph
        if (!container.getViewBy().isEmpty() && !viewbyid.equalsIgnoreCase("")) {
            //sandeep
            if (request.getParameter("viewbyid") != null && !request.getParameter("viewbyid").equalsIgnoreCase("")) {
                String[] rowIdArr = viewbyid.replaceAll("\\[", "").replaceAll("\\]", "").trim().split(",");
                for (int i = 0; i < rowIdArr.length; i++) {
                    rowIdArr[i] = rowIdArr[i].trim();
                }
                String[] rowArr = viewbyname.replaceAll("\\[", "").replaceAll("\\]", "").trim().split(",");
                for (int i = 0; i < rowIdArr.length; i++) {
                    rowArr[i] = rowArr[i].trim();
                }
//         String[] rowIdArr = viewbyid.split(",");
//         String[] rowArr = viewbyname.split(",");
                ArrayList rowViewByLst = new ArrayList<String>(Arrays.asList(rowIdArr));
                ArrayList rowViewBy = new ArrayList<String>(Arrays.asList(rowArr));
                HashMap<String, List> inMap = (HashMap<String, List>) collect.operatorFilters.get("IN");
                HashMap<String, List> inMap1 = new HashMap<String, List>();
                Set paramEleIds = collect.reportParameters.keySet();
                Iterator paramEleIter = paramEleIds.iterator();
                String paramElement;
                while (paramEleIter.hasNext()) {

                    paramElement = (String) paramEleIter.next();
                    List<String> savefilter = (List<String>) inMap.get(paramElement);
                    inMap1.put(paramElement, savefilter);
                }
                for (int i = 0; i < rowViewByLst.size(); i++) {
                    List<String> viewby = new ArrayList<String>();

                    viewby.add((String) rowViewBy.get(i).toString().replace("[", "").replace("]", ""));

                    inMap1.put((String) rowViewByLst.get(i).toString().replace("[", "").replace("]", ""), viewby);
                    collect.operatorFilters.put("IN", inMap1);
                }

            }
            //end of sandeep  code
        }
//             else if(!container.getViewBy().isEmpty())
//            {
//                 List<String> viewby1 = new ArrayList<String>();
//              viewby1.add("All");
//                  HashMap<String, List> inMap=(HashMap<String, List>)collect.operatorFilters.get("IN");  // added by mohit
//              inMap.put((String) container.getViewBy().get(1),viewby1);
//                 collect.operatorFilters.put("IN",inMap);
//
//            }
        //ended by sruthi
        PbReturnObject retObj = viewerBd.getDashboardKPIData(container, collect, userId);
//        for (KPIElement kpiElem1:elemList12){
//            kpiDetail.addKPIElement(kpiElem1.getRefElementId(), kpiElem1);
//        }
        collect.timeDetailsArray = tempTimeDetailsArrayList;
        collect.operatorFilters.put("IN", inMapcopy);
        kpiDetail.setElementIds(kpiIds1);
        dashlet.setReportDetails(kpiDetail);
        PbReturnObject retObj11 = (PbReturnObject) container.getRetObj();
        container.setdashretobj(retObj);
        viewbyids.add(rowviewbyid);
        container.setDisplayColumns(queryCols1);
        container.setOriginalColumns(queryCols1);
        container.setDisplayLabels(colNames);
        container.setViewByElementIds(viewbyids);
        container.setDataTypes(datatypes);
        dashlet.setGraphtype("");
        map.put(dashboardId, container1);
        session.setAttribute("PROGENTABLES", map);
        String strURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        strURL = strURL + "reportViewer.do?reportBy=viewReport&REPORTID=" + dashboardId + "&action=open";
        links.add(strURL);
        container.setLinks(links);
//          List<String> colNames = new ArrayList();
//            List<String> colIds = new ArrayList();
        List<String> viewbyids1 = new ArrayList();
        List<String> viewbynames = new ArrayList();
        List<String> measures = new ArrayList();
        List<String> measureids = new ArrayList();
        List<String> Aggregation = new ArrayList();
        Aggregation.add(queryAggs.get(0));
        measures.add(measurename);
        measureids.add(kpis);

        rowviewbyid = viewbychangerunid;
        rowviewbyname = viewbychangerun;

//          colIds.add(rowviewbyid);
//             colIds.add(kpis);
        viewbyids1.add(rowviewbyid);

//          colNames.add(rowviewbyname);
//            colNames.add(measurename);
        viewbynames.add(rowviewbyname);

        Gson gson = new Gson();
//          Map<String,List<Map<String, String>>> dataMap = new HashMap<String,List<Map<String, String>>>();
//List<Map<String, String>> list = new  ArrayList<Map<String, String>>();
////List<Map<String, String>> list = new  ArrayList<Map<String, String>>();
////List<Map<String, String>> list = new  ArrayList<Map<String, String>>();
////  ReportManagementDAO dao1=new ReportManagementDAO();
//         list= dao.generategraphJson(retObj,colNames,colIds,1);
//          dataMap.put("chart1", list);
//          String data=gson.toJson(dataMap);
        HashMap map11 = new HashMap();
//         map11.put("data",data);
        map11.put("viewbyids", viewbyids1);
        map11.put("viewbynames", viewbynames);
        map11.put("measures", measures);
        map11.put("measureids", measureids);
        map11.put("Aggregation", Aggregation);
        session.setAttribute("originalretobj", retObj11);
        String report = gson.toJson(map11);
        PrintWriter out = response.getWriter();
        out.print(report);
//        container.setKpiRetObj(retObj);
        return null;
    }

    public ActionForward displayScoreCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = null;
        Container container = null;
        String dashBoardId = null;
        String masterId = null;
        pbDashboardCollection collect = null;
        PrintWriter out = response.getWriter();
        String result = "";
        String dashletId = null;

        if (session != null && session.getAttribute("USERID") != null && session.getAttribute("PROGENTABLES") != null) {
            dashBoardId = request.getParameter("dashBoardId");
            masterId = request.getParameter("kpiMasterId");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            dashletId = request.getParameter("dashletId");
            dashBoardId = request.getParameter("dashBoardId");
            container = (Container) map.get(dashBoardId);
            collect = (pbDashboardCollection) container.getReportCollect();
            collect.setServletRequest(request);
            collect.setServletResponse(response);
            collect.setSession(session);

            String userId = (String) session.getAttribute("USERID");
            result = collect.processScoreCard(masterId, dashletId, container.getKpiRetObj(), container.getPriorRetObj(), userId);
        }

        out.print(result);
        return null;
    }

    /*
     * This method is called as the ajax request from individual portlets to
     * load the corresponding graph. Functions Performed : From the return
     * object available in the container, data set is created and PbGraphDisplay
     * is used to create the graph using JFree Charts library
     */
    public ActionForward displayGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath();
        HashMap map = null;
        Container container = null;
        String dashletId = null;
        String dashBoardId = null;
        String refReportId = null;
        String graphId = null;
        String kpiMasterId = null;
        String dispSequence = null;
        String dispType = null;
        String dashletName = null;
        String MyGraphType = null;
        String graphOrTable = null;
        String editDbrd = null;
        boolean fromDesigner = false;
        boolean flag = false;
        DashboardViewerDAO viewerDao = new DashboardViewerDAO();
        DashletPropertiesHelper dashletPropertiesHelper = null;
        pbDashboardCollection collect = null;
        PbReportRequestParameter reportReqParams = null;
        PrintWriter out = response.getWriter();
        String result = null;

        ServletContext context = this.getServlet().getServletContext();
        boolean isFxCharts = Boolean.parseBoolean(context.getInitParameter("isFxCharts"));

        if (session != null && session.getAttribute("USERID") != null) {
            dashletId = request.getParameter("dashletId");
            dashletPropertiesHelper = viewerDao.getDashletPropertiesHelperObject(dashletId);
            dashBoardId = request.getParameter("dashBoardId");
            refReportId = request.getParameter("refReportId");
            graphId = request.getParameter("graphId");
            kpiMasterId = request.getParameter("kpiMasterId");
            dispSequence = request.getParameter("dispSequence");
            dispType = request.getParameter("dispType");
            MyGraphType = request.getParameter("graphType");
            graphOrTable = request.getParameter("graphOrTable");
            editDbrd = request.getParameter("editDbrd");
            fromDesigner = Boolean.parseBoolean(request.getParameter("fromDesigner"));
            flag = Boolean.parseBoolean(request.getParameter("flag"));
            if (dispType.equalsIgnoreCase("table")) {
                graphOrTable = dispType;
            }
            dashletName = request.getParameter("dashletName");
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(dashBoardId) != null) {
                    container = (Container) map.get(dashBoardId);
                    collect = (pbDashboardCollection) container.getReportCollect();
                    reportReqParams = new PbReportRequestParameter(request);
                    reportReqParams.setParametersHashMap();
                    collect.setGraphType(dashletId, MyGraphType);
                    DashletDetail dashlet = collect.getDashletDetail(dashletId);
                    dashlet.setDashletName(dashletName);
                    GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
                    if (graphDetails != null) {
                        if (graphDetails.getDashletpropertieshelper() != null) {
                            dashletPropertiesHelper = graphDetails.getDashletpropertieshelper();
                        } else {
                            dashletPropertiesHelper = viewerDao.getDashletPropertiesHelperObject(dashletId);
                        }
                    }
                    if (dashletPropertiesHelper != null) {
                        if (!dashletPropertiesHelper.isSortAll()) {
                            graphDetails.setDisplayRows(Integer.toString(dashletPropertiesHelper.getCountForDisplay()));
                        } else {
                            graphDetails.setDisplayRows("All");
                        }
                    }

                    if (graphOrTable != null && !("".equals(graphOrTable))) {
                        if ("table".equalsIgnoreCase(graphOrTable)) {
                            graphDetails.setShowAsTable(true);
                        } else {
                            graphDetails.setShowAsTable(false);
                        }
                    } else if (dispType.equalsIgnoreCase("jq")) {
                        graphDetails.setShowAsTable(false);
                    }
                    if (dispType.equalsIgnoreCase("textKpi")) {
                        PbDashboardViewerBD bd = new PbDashboardViewerBD();
                        result = bd.buildTextKpiTable(container, dashletId);
                    } else {
                        if (dispType.equalsIgnoreCase("groupMeassure")) {
                            String userId = session.getAttribute("USERID").toString();
                            PbDashboardViewerBD bd = new PbDashboardViewerBD();
                            GroupMeassureParams meassureParams = new GroupMeassureParams();
                            meassureParams.setDahletId(dashletId);
                            meassureParams.setGroupId(dashlet.getGroupId());
                            bd.setUserId(userId);
                            bd.setInitialGroupMeasssureReport(container, dashlet);
                            result = bd.builDbGroupMeassure(container, meassureParams);
                        } else {
                            if (graphDetails.isShowAsTable()) {
                                PbDashboardViewerBD bd = new PbDashboardViewerBD();
                                result = bd.buildDbrdTable(container, dashletId, fromDesigner, editDbrd, flag);
                            } else {
                                GraphBuilder graphBuilder = new GraphBuilder();
                                graphBuilder.setRequest(request);
                                graphBuilder.setResponse(response);
                                graphBuilder.setFxCharts(isFxCharts);

                                result = graphBuilder.displayGraphs(container, dashletId, contextPath, fromDesigner, editDbrd);
                            }
                        }
                    }
                }
            }
        }
        if (result == null) {
            result = "";
        }
        out.print(result);
        return null;
    }

    public ActionForward displayKPIDashGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath();
        HashMap map = null;
        Container container = null;
        String dashletId = null;
        String dashBoardId = null;
        String refReportId = null;
        String graphId = null;
        String kpiMasterId = null;
        String dispSequence = null;
        String dispType = null;
        String dashletName = null;
        pbDashboardCollection collect = null;
        PbReportRequestParameter reportReqParams = null;
        PrintWriter out = response.getWriter();
        String result = null;
        boolean fromDesigner = false;
        String editDbrd = null;

        ServletContext context = this.getServlet().getServletContext();
        boolean isFxCharts = Boolean.parseBoolean(context.getInitParameter("isFxCharts"));

        if (session != null && session.getAttribute("USERID") != null) {
            dashletId = request.getParameter("dashletId");
            dashBoardId = request.getParameter("dashBoardId");
            refReportId = request.getParameter("refReportId");
            graphId = request.getParameter("graphId");
            kpiMasterId = request.getParameter("kpiMasterId");
            dispSequence = request.getParameter("dispSequence");
            dispType = request.getParameter("dispType");
            dashletName = request.getParameter("dashletName");
            editDbrd = request.getParameter("editDbrd");
            if (request.getParameter("fromDesigner") != null || !"".equals(request.getParameter("forDesigner"))) {
                fromDesigner = Boolean.parseBoolean(request.getParameter("forDesigner"));
            }
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(dashBoardId) != null) {
                    container = (Container) map.get(dashBoardId);
                    collect = (pbDashboardCollection) container.getReportCollect();
                    reportReqParams = new PbReportRequestParameter(request);
                    reportReqParams.setParametersHashMap();

                    GraphBuilder graphBuilder = new GraphBuilder();
                    graphBuilder.setRequest(request);
                    graphBuilder.setResponse(response);
                    graphBuilder.setFxCharts(isFxCharts);
                    graphBuilder.setDashboardid(dashBoardId);

                    result = graphBuilder.displayGraphs(container, dashletId, contextPath, fromDesigner, editDbrd);
                }
            }
        }
        if (result == null) {
            result = "";
        }
        out.println(result);
        return null;
    }

    //added for toggle between table and graph
    public ActionForward dispDbrdTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        Container container = null;
        String dbrdId = "";
        String dashletId = "";
        String result = "";
        boolean flag = false;
        if (session != null) {
            try {
                dbrdId = request.getParameter("dashBoardId");
                dashletId = request.getParameter("dashletId");
                PrintWriter out = response.getWriter();

                container = Container.getContainerFromSession(request, dbrdId);
                PbDashboardViewerBD bd = new PbDashboardViewerBD();
                result = bd.buildDbrdTable(container, dashletId, false, null, flag);
                out.print(result);
                return null;

            } catch (Exception e) {
                logger.error("Exception:", e);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward forwardToDashBoard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        return mapping.findForward("dashboardView");
    }

    public ActionForward saveKPICustomColorVals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String hr1 = request.getParameter("hr1");
        String hr2 = request.getParameter("hr2");
        String lr1 = request.getParameter("lr1");
        String lr2 = request.getParameter("lr2");
        String mr1 = request.getParameter("mr1");
        String mr2 = request.getParameter("mr2");
        String hrVal = request.getParameter("hrVal");
        String mrVal = request.getParameter("mrVal");
        String lrVal = request.getParameter("lrVal");
        String kpiName = request.getParameter("kpiName");
        String elementId = request.getParameter("elementId");
        String kpiMasterId = request.getParameter("kpiMasterId");
        String changePercentVal = request.getParameter("changePercentVal");

        String dashboardId = request.getParameter("reportId");
        String dashletId = request.getParameter("dashletId");

        HttpSession session = request.getSession(false);
        HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
        Container container = (Container) map.get(dashboardId);

        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail dashlet = collect.getDashletDetail(dashletId);

        PbDashboardViewerBD dbrdViewerBD = new PbDashboardViewerBD();
        dbrdViewerBD.updateKPICustomColorVals(dashlet, elementId, kpiMasterId, hrVal, mrVal, lrVal, hr1, hr2, lr1, lr2, mr1, mr2);
        return null;
    }

    public ActionForward getKPIInsightViewerPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String dashBoardId = request.getParameter("dashBoardId");
        String elementId = request.getParameter("elementId");
        String insightDimId = request.getParameter("insightDimId");
        String insightDimValue = request.getParameter("insightDimValue");
        InsightBaseDetails baseDetails = new InsightBaseDetails();
        LinkedHashMap reportParams;
        InsightsBD insightsBD = new InsightsBD();
        ArrayList<String> dispMeasures = new ArrayList<String>();
        ArrayList<String> dispMeasuresNames = new ArrayList<String>();

        ArrayList<String> dimensionIds = new ArrayList<String>();
        ArrayList<String> dimensionNames = new ArrayList<String>();



        String[] insightDimElementIds = null;
        String[] insightDimElemtValue = null;
        if (insightDimId != null && insightDimValue != null) {
            insightDimElementIds = insightDimId.split(",");
            insightDimElemtValue = insightDimValue.split(",");
        }
        int index = 0;

        HttpSession session = request.getSession(false);
        baseDetails.setUserId(String.valueOf(session.getAttribute("USERID")));


        Container container = Container.getContainerFromSession(request, dashBoardId);
        String url = ((pbDashboardCollection) container.getReportCollect()).completeUrl;

        HashMap insightParams = new HashMap();
        if (insightDimElementIds != null && insightDimElementIds.length > 0) {

            for (String dimelemtids : insightDimElementIds) {
                if (dimelemtids.startsWith("A_")) {
                    dimelemtids = dimelemtids.replace("A_", "");
                }
                insightParams.put(dimelemtids, insightDimElemtValue[index]);
                index++;
            }
        }

        reportParams = insightsBD.getParameters(container.getReportCollect().reportParametersValues, insightParams);
        baseDetails.setParameters(reportParams);
        baseDetails.setBizRoles(container.getReportCollect().reportBizRoles);
        for (int i = 0; i < container.getReportCollect().reportParamIds.size(); i++) {
            dimensionIds.add((String) container.getReportCollect().reportParamIds.get(i));
        }
        for (int i = 0; i < container.getReportCollect().reportParamNames.size(); i++) {
            dimensionNames.add((String) container.getReportCollect().reportParamNames.get(i));
        }
        baseDetails.setKpiElement(insightsBD.getKPIElements(elementId));
        if (!baseDetails.getKpiElement().isEmpty()) {
            baseDetails.setIndicatorMeasure(baseDetails.getKpiElement().get(baseDetails.getKpiElement().size() - 1).getElementId());
        }
        baseDetails.setDrillMap(container.getReportCollect().getDrillMap());
        baseDetails.setTimeDetailsArray(container.getReportCollect().timeDetailsArray);
        baseDetails.setCallBackUrl(url);
        baseDetails.setDimensionIds(dimensionIds);
        baseDetails.setDimensionNames(dimensionNames);
        for (int i = 0; i < baseDetails.getKpiElement().size(); i++) {
            dispMeasures.add(baseDetails.getKpiElement().get(i).getElementId());
            dispMeasuresNames.add(baseDetails.getKpiElement().get(i).getElementName());
        }
        baseDetails.setDispMeasures(dispMeasures);
        baseDetails.setDispMeasureNames(dispMeasuresNames);
        session.setAttribute("insightBaseDetails", baseDetails);

        return mapping.findForward("insightsPage");
    }

    public ActionForward buildKPI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String dashboardId = request.getParameter("dashboardId");
        String kpiMasterId = request.getParameter("kpiMasterId");
        String dashletId = request.getParameter("dashletId");


        String kpis = request.getParameter("Kpis");
        String kpiNames = request.getParameter("KpiNames");
        String kpiType = request.getParameter("kpiType");

        HttpSession session = request.getSession(false);
        HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
        Container container = (Container) map.get(dashboardId);
        Collection<String> seqElments = null;
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail dashlet = collect.getDashletDetail(dashletId);

        //New KPI Region is added. A new dashlet is created for this region
        // if (dashlet == null||dashlet.getDashletName()==null){
        if (dashlet.getDashletName() == null || dashlet.getDashletName().equals("")) {
            // dashlet = new DashletDetail();
            dashlet.setDashBoardDetailId(dashletId);
            dashlet.setKpiType(kpiNames);
            dashlet.setKpiType(kpiType);
            dashlet.setDisplayType(DashboardConstants.KPI_REPORT);
            dashlet.setDashletName(collect.reportName);

            KPI kpiDetail = new KPI();
            dashlet.setReportDetails(kpiDetail);
            kpiMasterId = dashlet.getKpiMasterId();
            // collect.addDashletDetail(dashlet);
        }
        kpiMasterId = dashlet.getKpiMasterId();
        if (kpiMasterId == null) {
            kpiMasterId = request.getParameter("kpiMasterId");
        }
        KPI kpiDetail = (KPI) dashlet.getReportDetails();

        List<String> newKPIs = new ArrayList<String>();
        List<String> kpiIds = new ArrayList<String>();
        HashMap<String, String> kpidrill = (HashMap<String, String>) kpiDetail.getKPIDrill();
        HashMap<Integer, String> seqElmentsMap = (HashMap<Integer, String>) kpiDetail.getKPISequenceHashMap().clone();
        seqElments = seqElmentsMap.values();
        kpiDetail.getKPISequenceHashMap().clear();

        if (kpis != null) {
            String[] selectedKPIs = kpis.split(",");
            for (int indx = 0; indx < selectedKPIs.length; indx++) {
//            for (String elementId:selectedKPIs){
                kpiIds.add(selectedKPIs[indx]);
                List<KPIElement> elements = kpiDetail.getKPIElements(selectedKPIs[indx]);
                if (elements == null || elements.isEmpty()) {
                    newKPIs.add(selectedKPIs[indx]);
                }
                kpiDetail.getKPISequenceHashMap().put(indx + 1, selectedKPIs[indx]);
            }
            if (seqElments != null && !seqElments.isEmpty()) {
                for (String string : seqElments) {
                    if (!kpiDetail.getKPISequenceHashMap().containsValue(string)) {
                        if (dashlet.isGroupElement(string)) {
                            dashlet.removeSingleGroup(string);
                        }
                    }
                }
            }
        }
        for (String kpiId : kpiIds) {
            if (!kpidrill.containsKey(kpiId)) {
                kpiDetail.addKPIDrill(kpiId, "0");
            }
        }

        kpiDetail.setElementIds(kpiIds);
        DashboardViewerDAO dao = new DashboardViewerDAO();
        PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();
        String userId = String.valueOf(session.getAttribute("USERID"));
        List<KPIElement> kpiElements = dao.getKPIElements(newKPIs, new HashMap<String, String>());
        for (KPIElement kpiElem : kpiElements) {
            kpiDetail.addKPIElement(kpiElem.getRefElementId(), kpiElem);
        }

        if (DashboardConstants.KPI_TYPE_TARGET.equalsIgnoreCase(dashlet.getKpiType())) {
            //Populate the target values from PRG_AR_TARGET_DATA
            if (!newKPIs.isEmpty()) {
                List<TargetData> targetList = dao.getTargetData(newKPIs);
                if (targetList != null) {
                    for (TargetData tgtData : targetList) {
                        kpiDetail.addTargetData(tgtData);
                    }
                }
            }
        }
        dashlet.setReportDetails(kpiDetail);
        PbReturnObject retObj = viewerBd.getDashboardKPIData(container, collect, userId);
        container.setKpiRetObj(retObj);
        return null;
    }

    /**
     * @Author Srikanth.P to build KPI With Graph
     */
    public ActionForward buildKPIWithGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        DashboardTemplateBD dashboardTempBD = new DashboardTemplateBD();
        String dashboardId = request.getParameter("dashboardId");
        String kpiMasterId = request.getParameter("kpiMasterId");
        String dashletId = request.getParameter("dashletId");


        String kpis = request.getParameter("grpColumns");
        String kpiNames = request.getParameter("grpColumnsNames");
        String kpiType = request.getParameter("kpiType");
        String grptype = request.getParameter("grpType");
        HttpSession session = request.getSession(false);
        HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
        Container container = (Container) map.get(dashboardId);
        Collection<String> seqElments = null;
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail dashlet = collect.getDashletDetail(dashletId);
        int rowSpan = dashlet.getRowSpan();
        int colSpan = dashlet.getColSpan();
        int height = 350;
        height = height * rowSpan;

        //New KPI Region is added. A new dashlet is created for this region
        // if (dashlet == null||dashlet.getDashletName()==null){
        if (dashlet.getDashletName() == null || dashlet.getDashletName().equals("")) {
            // dashlet = new DashletDetail();
            dashlet.setDashBoardDetailId(dashletId);
            dashlet.setKpiType(kpiNames);
            dashlet.setKpiType(kpiType);
            dashlet.setDisplayType(DashboardConstants.KPI_WITH_GRAPH);
            dashlet.setDashletName(collect.reportName);


            kpiMasterId = dashlet.getKpiMasterId();
            // collect.addDashletDetail(dashlet);
        }
        KPI kpiDetail = (KPI) dashlet.getKpiDetails();
        if (kpiDetail == null) {
            kpiDetail = new KPI();
            dashlet.setReportDetails(kpiDetail);
        }

        kpiMasterId = dashlet.getKpiMasterId();
        if (kpiMasterId == null) {
            kpiMasterId = request.getParameter("kpiMasterId");
        }
//        KPI kpiDetail = (KPI) dashlet.getReportDetails();

        List<String> newKPIs = new ArrayList<String>();
        List<String> kpiIds = new ArrayList<String>();
        HashMap<String, String> kpidrill = (HashMap<String, String>) kpiDetail.getKPIDrill();
        HashMap<Integer, String> seqElmentsMap = (HashMap<Integer, String>) kpiDetail.getKPISequenceHashMap().clone();
        seqElments = seqElmentsMap.values();
        kpiDetail.getKPISequenceHashMap().clear();

        if (kpis != null) {
            String[] selectedKPIs = kpis.split(",");
            for (int indx = 0; indx < selectedKPIs.length; indx++) {
//            for (String elementId:selectedKPIs){
                kpiIds.add(selectedKPIs[indx]);
                List<KPIElement> elements = kpiDetail.getKPIElements(selectedKPIs[indx]);
                if (elements == null || elements.isEmpty()) {
                    newKPIs.add(selectedKPIs[indx]);
                }
                kpiDetail.getKPISequenceHashMap().put(indx + 1, selectedKPIs[indx]);
            }
            if (seqElments != null && !seqElments.isEmpty()) {
                for (String string : seqElments) {
                    if (!kpiDetail.getKPISequenceHashMap().containsValue(string)) {
                        if (dashlet.isGroupElement(string)) {
                            dashlet.removeSingleGroup(string);
                        }
                    }
                }
            }
        }
        for (String kpiId : kpiIds) {
            if (!kpidrill.containsKey(kpiId)) {
                kpiDetail.addKPIDrill(kpiId, "0");
            }
        }

        kpiDetail.setElementIds(kpiIds);
        DashboardViewerDAO dao = new DashboardViewerDAO();
        PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();
        String userId = String.valueOf(session.getAttribute("USERID"));
        List<KPIElement> kpiElements = dao.getKPIElements(newKPIs, new HashMap<String, String>());
        for (KPIElement kpiElem : kpiElements) {
            kpiDetail.addKPIElement(kpiElem.getRefElementId(), kpiElem);
        }

        if (DashboardConstants.KPI_TYPE_TARGET.equalsIgnoreCase(dashlet.getKpiType())) {
            //Populate the target values from PRG_AR_TARGET_DATA
            if (!newKPIs.isEmpty()) {
                List<TargetData> targetList = dao.getTargetData(newKPIs);
                if (targetList != null) {
                    for (TargetData tgtData : targetList) {
                        kpiDetail.addTargetData(tgtData);
                    }
                }
            }
        }
        dashlet.setReportDetails(kpiDetail);
        dashlet.setKpiDetails(kpiDetail, "KpiWithGraph");
        PbReturnObject retObj = viewerBd.getDashboardKPIData(container, collect, userId);
        if (retObj != null) {
            container.setKpiRetObj(retObj);
        }
        KPIBuilder kpibuilder = new KPIBuilder();
        String kpiRegion = "";
        PrintWriter out = response.getWriter();
        //kpiRegion=kpibuilder.buildKPIRegion(container, kpiMasterId,dashletId, dashboardId,collect);
        kpiRegion = kpibuilder.buildKpiRegionForViewer(container, dashletId, userId, dashboardId);
        String graphRegion = dashboardTempBD.buildDbrdGraphs(request, response, false).toString();
        String kpiWithGraph = kpibuilder.buildKpiWithGraph(kpiRegion, graphRegion, dashletId, dashlet, height, dashboardId);
        out.print(kpiWithGraph);
        return null;
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return graph will be built on the clicked KPI of the KpiRegion
     * @throws java.lang.Exception
     */
    public ActionForward buildGraphOnKPI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath();
        HashMap map = null;
        Container container = null;
        String dashletId = null;
        String dashBoardId = null;
        String refReportId = null;
        String graphId = null;
        String kpiMasterId = null;
        String dispSequence = null;
        String dispType = null;
        String dashletName = null;
        String MyGraphType = null;
        String graphOrTable = null;
        String editDbrd = null;
        boolean fromDesigner = false;
        boolean flag = false;
        DashboardViewerDAO viewerDao = new DashboardViewerDAO();
        DashletPropertiesHelper dashletPropertiesHelper = null;
        pbDashboardCollection collect = null;
        PbReportRequestParameter reportReqParams = null;
        PrintWriter out = response.getWriter();
        String result = null;

        ServletContext context = this.getServlet().getServletContext();
        boolean isFxCharts = Boolean.parseBoolean(context.getInitParameter("isFxCharts"));

        if (session != null && session.getAttribute("USERID") != null) {
            dashletId = request.getParameter("dashletId");
            dashletPropertiesHelper = viewerDao.getDashletPropertiesHelperObject(dashletId);
            dashBoardId = request.getParameter("dashBoardId");
            refReportId = request.getParameter("refReportId");
            graphId = request.getParameter("graphId");
            kpiMasterId = request.getParameter("kpiMasterId");
            dispSequence = request.getParameter("dispSequence");
            dispType = request.getParameter("dispType");
            MyGraphType = request.getParameter("graphType");
            graphOrTable = request.getParameter("graphOrTable");
            editDbrd = request.getParameter("editDbrd");
            String buildElement = request.getParameter("elementId");
            fromDesigner = Boolean.parseBoolean(request.getParameter("fromDesigner"));
            flag = Boolean.parseBoolean(request.getParameter("flag"));
            HashMap GraphSizesHashMap = null;
            HashMap GraphTypesHashMap = null;
            HashMap GraphClassesHashMap = new HashMap();
            String graphClassName = null;
            String graphTypeName = null;
            dashletName = request.getParameter("dashletName");
            GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");
            GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
            GraphSizesHashMap = (HashMap) session.getAttribute("GraphSizesHashMap");
            graphTypeName = String.valueOf(GraphTypesHashMap.get(MyGraphType));
            graphClassName = String.valueOf(GraphClassesHashMap.get(graphTypeName));


            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(dashBoardId) != null) {
                    container = (Container) map.get(dashBoardId);
                    collect = (pbDashboardCollection) container.getReportCollect();
                    reportReqParams = new PbReportRequestParameter(request);
                    reportReqParams.setParametersHashMap();
//                    collect.setGraphType(dashletId,graphTypeName);
                    DashletDetail dashlet = collect.getDashletDetail(dashletId);
                    dashlet.setDashletName(dashletName);
                    GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
                    if (graphDetails != null) {
                        if (graphDetails.getDashletpropertieshelper() != null) {
                            dashletPropertiesHelper = graphDetails.getDashletpropertieshelper();
                        } else {
                            dashletPropertiesHelper = viewerDao.getDashletPropertiesHelperObject(dashletId);
                        }
                    }
                    if (dashletPropertiesHelper != null) {
                        if (!dashletPropertiesHelper.isSortAll()) {
                            graphDetails.setDisplayRows(Integer.toString(dashletPropertiesHelper.getCountForDisplay()));
                        } else {
                            graphDetails.setDisplayRows("All");
                        }
                    }
                    graphDetails.setBuildElement(buildElement);
                    DashboardTemplateDAO DAO = new DashboardTemplateDAO();
                    PbDashboardViewerBD viewerBD = new PbDashboardViewerBD();
                    PbReturnObject retObj = DAO.getNewDbrdGrpDets(buildElement);
                    PbReturnObject pbretObj = null;
                    String userId = String.valueOf(session.getAttribute("USERID"));
                    List<QueryDetail> queryDetails = new ArrayList<QueryDetail>();
                    if (retObj != null && retObj.getRowCount() > 0) {
                        for (int i = 0; i < retObj.getRowCount(); i++) {
                            QueryDetail qd = new QueryDetail();
                            qd.setElementId(retObj.getFieldValueString(i, 1));
                            qd.setDisplayName(retObj.getFieldValueString(i, 0));
                            qd.setRefElementId(retObj.getFieldValueString(i, 2));
                            qd.setFolderId(retObj.getFieldValueInt(i, 3));
                            qd.setSubFolderId(retObj.getFieldValueInt(i, 4));
                            qd.setAggregationType(retObj.getFieldValueString(i, 5));
                            qd.setColumnType(retObj.getFieldValueString(i, 6));
                            queryDetails.add(qd);

                        }
                    }
                    graphDetails.setQueryDetails(queryDetails);
                    if (graphDetails.isTimeSeries()) {
                        pbretObj = viewerBD.getTimeSeriesData(collect, userId);
                        container.setTimeSeriesRetObj(pbretObj);
                    } else {
                        pbretObj = viewerBD.getDashboardData(container, collect, userId);
                    }
                    GraphBuilder graphBuilder = new GraphBuilder();
                    graphBuilder.setRequest(request);
                    graphBuilder.setResponse(response);
                    graphBuilder.setFxCharts(isFxCharts);

                    result = graphBuilder.displayGraphs(container, dashletId, contextPath, fromDesigner, editDbrd);
                }
            }
        }
        if (result == null) {
            result = "";
        }
        out.print(result);
        return null;
    }

    public ActionForward displayKpiWithGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath();
        HashMap map = null;
        Container container = null;
        String dashletId = null;
        String dashBoardId = null;
        String refReportId = null;
        String graphId = null;
        String kpiMasterId = null;
        String dispSequence = null;
        String dispType = null;
        String dashletName = null;
        String MyGraphType = null;
        String graphOrTable = null;
        String editDbrd = null;
        boolean fromDesigner = false;
        boolean flag = false;
        DashboardViewerDAO viewerDao = new DashboardViewerDAO();
        DashletPropertiesHelper dashletPropertiesHelper = null;
        pbDashboardCollection collect = null;
        PbReportRequestParameter reportReqParams = null;
        PrintWriter out = response.getWriter();
        String result = null;
        ServletContext context = this.getServlet().getServletContext();
        boolean isFxCharts = Boolean.parseBoolean(context.getInitParameter("isFxCharts"));

        if (session != null && session.getAttribute("USERID") != null) {
            dashletId = request.getParameter("dashletId");
            dashletPropertiesHelper = viewerDao.getDashletPropertiesHelperObject(dashletId);
            dashBoardId = request.getParameter("dashBoardId");
            refReportId = request.getParameter("refReportId");
            graphId = request.getParameter("graphId");
            kpiMasterId = request.getParameter("kpiMasterId");
            dispSequence = request.getParameter("dispSequence");
            dispType = request.getParameter("dispType");
            MyGraphType = request.getParameter("graphType");
            graphOrTable = request.getParameter("graphOrTable");
            editDbrd = request.getParameter("editDbrd");
            fromDesigner = Boolean.parseBoolean(request.getParameter("fromDesigner"));
            flag = Boolean.parseBoolean(request.getParameter("flag"));
            if (dispType.equalsIgnoreCase("table")) {
                graphOrTable = dispType;
            }
            dashletName = request.getParameter("dashletName");
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(dashBoardId) != null) {
                    container = (Container) map.get(dashBoardId);
                    collect = (pbDashboardCollection) container.getReportCollect();
                    reportReqParams = new PbReportRequestParameter(request);
                    reportReqParams.setParametersHashMap();
                    collect.setGraphType(dashletId, MyGraphType);
                    DashletDetail dashlet = collect.getDashletDetail(dashletId);
                    int rowSpan = dashlet.getRowSpan();
                    int colSpan = dashlet.getColSpan();
                    int height = 350;
                    height = height * rowSpan;
                    dashlet.setDashletName(dashletName);
                    GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
                    if (graphDetails != null) {
                        if (graphDetails.getDashletpropertieshelper() != null) {
                            dashletPropertiesHelper = graphDetails.getDashletpropertieshelper();
                        } else {
                            dashletPropertiesHelper = viewerDao.getDashletPropertiesHelperObject(dashletId);
                        }
                    }
                    if (dashletPropertiesHelper != null) {
                        if (!dashletPropertiesHelper.isSortAll()) {
                            graphDetails.setDisplayRows(Integer.toString(dashletPropertiesHelper.getCountForDisplay()));
                        } else {
                            graphDetails.setDisplayRows("All");
                        }
                    }
                    KPIBuilder kpibuilder = new KPIBuilder();
                    GraphBuilder graphBuilder = new GraphBuilder();
                    graphBuilder.setRequest(request);
                    graphBuilder.setResponse(response);
                    graphBuilder.setFxCharts(isFxCharts);
                    String userId = session.getAttribute("USERID").toString();
                    String graphRegion = "";
                    if (dashlet.getJqplotgrapprop() != null) {
                        graphRegion = "";
                    } else {
                        graphRegion = graphBuilder.displayGraphs(container, dashletId, contextPath, fromDesigner, editDbrd);;
                    }

//                        result=graphBuilder.displayGraphs(container, dashletId,contextPath,fromDesigner,editDbrd);;
                    String KpiRegion = kpibuilder.buildKpiRegionForViewer(container, dashletId, userId, dashBoardId);
                    result = kpibuilder.buildKpiWithGraph(KpiRegion, graphRegion, dashletId, dashlet, height, dashBoardId);

                }
            }
        }
        if (result == null) {
            result = "";
        }
        out.print(result);

        return null;
    }

    public ActionForward reOrderKpi(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String dashboardId = request.getParameter("dashboardId");
        String kpiMasterId = request.getParameter("kpiMasterId");
        String dashletId = request.getParameter("dashletId");
        String kpis = request.getParameter("Kpis");
        String kpiNames = request.getParameter("KpiNames");
        String kpiType = request.getParameter("kpiType");
        boolean status = false;
        HttpSession session = request.getSession(false);
        HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
        HashMap seqMap = new HashMap();
        String result = "";
        if (session != null) {
            PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();
            KPIBuilder kpiBuilder = new KPIBuilder();
            String userId = session.getAttribute("USERID").toString();
            List<String> elementList = new ArrayList<String>();
            if (kpis != null) {
                String[] kpiList = kpis.split(",");
                for (int i = 0; i < kpiList.length; i++) {
                    seqMap.put(i, kpiList[i]);
                    elementList.add(kpiList[i]);
                }
            }
//            status=viewerBd.updateSequence(dashletId, seqMap);
//            if(status){
            map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(dashboardId) != null) {
                Container container = (Container) map.get(dashboardId);
                pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
                DashletDetail dashlet = collect.getDashletDetail(dashletId);
                KPI kpiDetails = dashlet.getKpiDetails();
                kpiDetails.setKPISequenceHashMap(seqMap);
                kpiDetails.setElementIds(elementList);
                result = kpiBuilder.buildKpiRegionForViewer(container, dashletId, userId, dashboardId);
            }

//            }
//            else{
//                result="alert('Sequence Did not Effected...Connection Problem Occcured...!! ')";
//            }
            response.getWriter().print(result);
        }
        return null;
    }

    public ActionForward setKpiProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String dashboardId = request.getParameter("dashBoardId");
        String kpiMasterId = request.getParameter("kpiMasterId");
        String dashletId = request.getParameter("dashletId");
        String kpiType = request.getParameter("kpiType");
        HttpSession session = request.getSession(false);
        HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
        HashMap seqMap = new HashMap();
        String result = "";
        if (session != null) {
            KPIBuilder kpiBuilder = new KPIBuilder();
            String userId = session.getAttribute("USERID").toString();
            map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(dashboardId) != null) {
                Container container = (Container) map.get(dashboardId);
                result = kpiBuilder.buildKpiRegionForViewer(container, dashletId, userId, dashboardId);
            }
            response.getWriter().print(result);
        }

        return null;
    }

    public ActionForward drillToReportForKpiWithGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String dashboardId = request.getParameter("dashboardId");
        String kpiMasterId = request.getParameter("kpiMasterId");
        String dashletId = request.getParameter("dashletId");
        String folderIds = request.getParameter("folderIds");
        HttpSession session = request.getSession(false);
        HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
        String result = "";
        if (session != null) {
//            KPIBuilder kpiBuilder=new KPIBuilder();
            String userId = session.getAttribute("USERID").toString();
            map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(dashboardId) != null) {
                Container container = (Container) map.get(dashboardId);
                pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
                DashletDetail dashlet = collect.getDashletDetail(dashletId);
                KPI kpiDetails = dashlet.getKpiDetails();
                PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();
                result = viewerBd.drillToReportTemplate(kpiDetails, folderIds, dashboardId, dashletId);
            }
            response.getWriter().print(result);
        }
        return null;
    }

    public ActionForward saveReportForKpiWithGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String dashboardId = request.getParameter("dashboardId");
        String kpiMasterId = request.getParameter("kpiMasterId");
        String dashletId = request.getParameter("dashletId");
        HttpSession session = request.getSession(false);
        HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
        String result = "";
        if (session != null) {
            KPIBuilder kpiBuilder = new KPIBuilder();
            String userId = session.getAttribute("USERID").toString();
            map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(dashboardId) != null) {
                Container container = (Container) map.get(dashboardId);
                pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
                DashletDetail dashlet = collect.getDashletDetail(dashletId);
                KPI kpiDetails = dashlet.getKpiDetails();
                List<String> elementList = kpiDetails.getElementIds();
                HashMap reportMap = new HashMap();
                String reportDetails = "";
                kpiDetails.clerKpiDrill();
                kpiDetails.clearKpiDrillType();
                for (int i = 0; i < elementList.size(); i++) {
                    reportDetails = request.getParameter(String.valueOf(elementList.get(i)));
                    String reportArray[] = reportDetails.split("~");
                    if (!reportArray[0].equalsIgnoreCase("None")) {
                        kpiDetails.addKPIDrill(elementList.get(i), reportArray[0]);
                        kpiDetails.addKPIDrillRepType(elementList.get(i), reportArray[1]);
                    }

                }
                result = kpiBuilder.buildKpiRegionForViewer(container, dashletId, userId, dashboardId);
            }
            response.getWriter().print(result);
        }
        return null;
    }

//     public ActionForward backToDashboard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
//     {
//        String dashboardId = request.getParameter("dashboardId");
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            request.setAttribute("REPORTID", dashboardId);
//            this.viewDashboard(mapping,form,request,response);
//            return mapping.findForward("dashboardDesigner");
//        } else {
//            return mapping.findForward("sessionExpired");
//        }
//
//     }
    public ActionForward excelDownload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String dahletid = request.getParameter("REPORTID");
        String dashboardId = request.getParameter("dashboardid");
        HttpSession session = request.getSession(false);
        HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
        Container container = (Container) map.get(dashboardId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        PbReturnObject pbretObj = new PbReturnObject();
        pbretObj = container.getKpiRetObj();
        CreatetableDAO viewerBd = new CreatetableDAO();
        //Added by ram
        Format formatter = new SimpleDateFormat("dd-MMM-yy");
        Date date = new Date();
        String s = formatter.format(date);
        //End Ram Code
        String kpiname = viewerBd.downloadexcel(dahletid, dashboardId, collect, pbretObj, container, s);

        session.setAttribute("KpiName", kpiname);


//

        return null;
    }

    /**
     * @author srikanth.p to build OLAP Graphs for a Graph in a dashlet
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward buildGraphOnViewby(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String dashBoardId = request.getParameter("dashBoardId");
        String dashletId = request.getParameter("dashletId");
        String viewById = request.getParameter("viewById");
        ArrayList reportrowViewBys = new ArrayList();
        String result = "";
        if (session != null) {
            Map map = null;
            String userId = session.getAttribute("USERID").toString();
            map = (HashMap) session.getAttribute("PROGENTABLES");
            String contextPath = request.getContextPath();
            if (map.get(dashBoardId) != null) {
                PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();
                Container container = (Container) map.get(dashBoardId);
                pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
                PbReturnObject retObj = null;
                reportrowViewBys.add(viewById);
                collect.reportRowViewbyValues = reportrowViewBys;
                retObj = viewerBd.getDashboardData(container, collect, userId);
                if (retObj != null) {
                    container.setRetObj(retObj);
                    GraphBuilder graphBuilder = new GraphBuilder();
                    graphBuilder.setRequest(request);
                    graphBuilder.setResponse(response);
                    graphBuilder.setFxCharts(false);
                    result = viewerBd.OLAPGraphBuilder(dashletId, dashBoardId, container, graphBuilder, contextPath);

                }

            }
            response.getWriter().print(result);
        }
        return null;
    }

    public ActionForward getInitialViewofOLAPGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String dashBoardId = request.getParameter("dashBoardId");
        String dashletId = request.getParameter("dashletId");
        String result = "";
        if (session != null) {
            Map map = null;
            String userId = session.getAttribute("USERID").toString();
            map = (HashMap) session.getAttribute("PROGENTABLES");
            String contextPath = request.getContextPath();
            if (map.get(dashBoardId) != null) {
                PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();
                Container container = (Container) map.get(dashBoardId);
                pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
                DashletDetail dashlet = collect.getDashletDetail(dashletId);
                collect.setInitialReportVIewBys(collect.reportRowViewbyValues);
                GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
                graphDetails.setInitialGraphHeight(graphDetails.getGraphHeight());
                graphDetails.setInitialGraphWidth(graphDetails.getGraphWidth());
                graphDetails.setGraphWidth("700");
                graphDetails.setGraphHeight("320");
                GraphBuilder graphBuilder = new GraphBuilder();
                graphBuilder.setRequest(request);
                graphBuilder.setResponse(response);
                graphBuilder.setFxCharts(false);
                result = viewerBd.OLAPGraphBuilder(dashletId, dashBoardId, container, graphBuilder, contextPath);
            }
            response.getWriter().print(result);
        }
        return null;
    }

    public ActionForward closeOLAPView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String dashBoardId = request.getParameter("dashBoardId");
        String dashletId = request.getParameter("dashletId");
        if (session != null) {
            Map map = null;
            String userId = session.getAttribute("USERID").toString();
            map = (HashMap) session.getAttribute("PROGENTABLES");
            String contextPath = request.getContextPath();
            if (map.get(dashBoardId) != null) {
                PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();
                Container container = (Container) map.get(dashBoardId);
                pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
                DashletDetail dashlet = collect.getDashletDetail(dashletId);
                collect.reportRowViewbyValues = collect.getInitialReportVIewBys();
                GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
                graphDetails.setGraphHeight(graphDetails.getInitialGraphHeight());
                graphDetails.setGraphWidth(graphDetails.getInitialGraphWidth());
                PbReturnObject retObj = viewerBd.getDashboardData(container, collect, userId);
//                
                if (retObj != null) {
                    container.setRetObj(retObj);
                }
            }
            response.getWriter().print("");
        }
        return null;
    }

    public ActionForward getOLAPGraphforOneView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String reportId = request.getParameter("REPORTID");
        String graphId = request.getParameter("graphId");
        String width = request.getParameter("width");
        String height = request.getParameter("height");
        int graphNum = Integer.parseInt(request.getParameter("graphNum"));
        String drillValue = request.getParameter("drillValue");
        String isOLAPInitial = request.getParameter("isOLAPInitial");
        String timeViewbyType = request.getParameter("viewbyType");
        String isRepDate = request.getParameter("isRepDate");
        String timeDetails = request.getParameter("timeDetails");
        ArrayList timeDeArrayList = new ArrayList();
        String dispType = request.getParameter("dispType");// written by swati
        String viewByid = request.getParameter("viewById");
        String drillType = request.getParameter("drillType");
        String graphChangeEvent = request.getParameter("isGraphTypeChanged");
        String changedGraphType = request.getParameter("changedGraphType");
        String changedGraphId = request.getParameter("changedGraphId");
        String graph = "";

        String OLAPGraph = "";
        if (session != null) {
            HashMap map = null;
            Container container = null;

            DashboardTemplateBD templateBd = new DashboardTemplateBD();
            PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();

            if (dispType != null && dispType.equalsIgnoreCase("table")) {
                StringBuilder sb = new StringBuilder();
                sb.append("<table vailgn='top' width='" + width + "px' height='" + height + "px' border='0' style='overflow: auto;'>");
                //sb.append("<table vailgn='top' width='"+width+"px' height='10%' border='0'>");
                sb.append("<tr><td align='right' width='100%' height='10px'><a href=\"#javascript.void(0)\" class=\"ui-icon ui-icon-image\" onclick=\"buildTableORGraph('" + reportId + "','" + graphId + "','" + viewByid + "','graph')\" title=\"Switch to Graph\"></a></td></tr>");
//                 sb.append("</table>");
                // sb.append("<table vailgn='top' width='"+width+"px' height='"+height+"px' border='0'>");
                sb.append("<tr><td valign='top' width='100%' height='" + (Integer.parseInt(height) - 10) + "px'>");
                templateBd.getOLAPTableForOneView(reportId, request, response, session, graphNum, true);
                sb.append("<div id='progenTable' name='progenTable' style=\"width='" + width + "px'; height='" + height + "px';\" align=\"left\" >");
                //sb.append(graph);
                sb.append("<script type='text/javascript'>");
                sb.append("buildProgenTable('" + reportId + "')");
                sb.append(" </script>");
                sb.append("</td></tr>");
                sb.append("</table>");
                OLAPGraph = sb.toString();
            } else {
                JqplotGraphProperty JqGraphproperty = new JqplotGraphProperty();
                PbReportViewerDAO reportViewerdao = new PbReportViewerDAO();
                JqGraphproperty = reportViewerdao.getJqGraphDetails(graphId);
                String jqGraphId = "";
                if (JqGraphproperty != null) {
                    jqGraphId = JqGraphproperty.getGraphTypeId();
                }
                if (graphChangeEvent != null && graphChangeEvent.equalsIgnoreCase("true")) {
                    jqGraphId = changedGraphId;
                    StringBuffer graphString = new StringBuffer();
//                         JqplotGraphProperty graphproperty=new JqplotGraphProperty();
                    ProGenJqPlotChartTypes jqplotcontainer = new ProGenJqPlotChartTypes();
                    ProgenJqplotGraphBD jqplotgraphbd = new ProgenJqplotGraphBD();
                    String chartId = "chart_" + graphNum + "_" + reportId;
                    jqplotgraphbd.chartId = chartId;
                    float divWidth = Float.parseFloat(width) - (Float.parseFloat(width) / 100) * 20;
                    float divHeight = Float.parseFloat(height) - (Float.parseFloat(height) / 100) * 10;
                    graphString.append("<div id='" + chartId + "' style=\"width:" + divWidth + "px;height:" + divHeight + "px;\" align='center'></div>");
                    graphString.append("<script>");
                    String selectgraph = JqGraphproperty.getSlectedGraphType(graphId);
//                                changedGraphId=graphproperty.getGraphTypeId();
                    jqplotgraphbd.JqplotGraphProperty = JqGraphproperty;
                    if (JqGraphproperty.getTableColumns() != null && JqGraphproperty.getTableColumns().length > 0) {
                        jqplotgraphbd.tablecols = JqGraphproperty.getTableColumns();
                    }
                    jqplotgraphbd.setIsFromOneView(true);
                    graphString.append(jqplotgraphbd.prepareJqplotGraph(reportId, changedGraphId, changedGraphType, jqplotcontainer, request, null, selectgraph, graphId));
                    graphString.append("</script>");
                    graph = graphString.toString();
                } else {
                    graph = templateBd.getOLAPGraphForOneView(reportId, request, response, session, graphNum, true);
                }

                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportId);
                PbReportCollection collect = container.getReportCollect();
                if (isOLAPInitial != null && isOLAPInitial.equalsIgnoreCase("true")) {
                    collect.setDrillValuesMap(new HashMap<String, LinkedList>());
                }
                if (drillValue != null) {
                    HashMap drillValuesMap = collect.getDrillValuesMap();
                    if (drillValuesMap != null && drillValuesMap.get(graphId) != null) {
                        LinkedList drilValList = (LinkedList) drillValuesMap.get(graphId);
                        drilValList.add(drillValue);
                    }
                    if (drillValuesMap.get(graphId) == null) {
                        LinkedList drilValList = new LinkedList();
                        drilValList.add(drillValue);
                        drillValuesMap.put(graphId, drilValList);
                    }
                }

                timeViewbyType = collect.timeDetailsArray.get(3).toString();
//                 
                OLAPGraph = viewerBd.OLAPGraphForOneView(container, reportId, graphId, graph, height, width, timeViewbyType, drillType, jqGraphId);
            }
        }
        response.getWriter().print(OLAPGraph);
        return null;
    }

    public ActionForward getOLAPGraphforOneViewd3(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String reportId = request.getParameter("REPORTID");
        String graphId = request.getParameter("graphId");
        String width = request.getParameter("width");
        String height = request.getParameter("height");
        int graphNum = 0;
        String drillValue = request.getParameter("drillValue");
        String isOLAPInitial = request.getParameter("isOLAPInitial");
        String timeViewbyType = request.getParameter("viewbyType");
        String isRepDate = request.getParameter("isRepDate");
        String timeDetails = request.getParameter("timeDetails");
        ArrayList timeDeArrayList = new ArrayList();
        String dispType = request.getParameter("dispType");// written by swati
        String viewByid = request.getParameter("viewById");
        String drillType = request.getParameter("drillType");
        String graphChangeEvent = request.getParameter("isGraphTypeChanged");
        String changedGraphType = request.getParameter("changedGraphType");
        String changedGraphId = request.getParameter("changedGraphId");
        String oneviewID = request.getParameter("oneviewID");
        String repname = request.getParameter("repname");
        String rolename = request.getParameter("rolename");
        String chartname = request.getParameter("chartname");
        String regid = request.getParameter("regid");
        String adhocdrills = request.getParameter("adhocdrills");
        String graph = "";
        ArrayList timedetails = new ArrayList();
        String OLAPGraph = "";
        if (session != null) {
            HashMap map = null;
            Container container = null;

            DashboardTemplateBD templateBd = new DashboardTemplateBD();
            PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();

            if (dispType != null && dispType.equalsIgnoreCase("table")) {
                StringBuilder sb = new StringBuilder();
                sb.append("<table vailgn='top' width='" + width + "px' height='" + height + "px' border='0' style='overflow: auto;'>");
                //sb.append("<table vailgn='top' width='"+width+"px' height='10%' border='0'>");
                sb.append("<tr><td align='right' width='100%' height='10px'><a href=\"#javascript.void(0)\" class=\"ui-icon ui-icon-image\" onclick=\"buildTableORGraph('" + reportId + "','" + graphId + "','" + viewByid + "','graph')\" title=\"Switch to Graph\"></a></td></tr>");
//                 sb.append("</table>");
                // sb.append("<table vailgn='top' width='"+width+"px' height='"+height+"px' border='0'>");
                sb.append("<tr><td valign='top' width='100%' height='" + (Integer.parseInt(height) - 10) + "px'>");
                templateBd.getOLAPTableForOneView(reportId, request, response, session, graphNum, true);
                sb.append("<div id='progenTable' name='progenTable' style=\"width='" + width + "px'; height='" + height + "px';\" align=\"left\" >");
                //sb.append(graph);
                sb.append("<script type='text/javascript'>");
                sb.append("buildProgenTable('" + reportId + "')");
                sb.append(" </script>");
                sb.append("</td></tr>");
                sb.append("</table>");
                OLAPGraph = sb.toString();
            } else {
                JqplotGraphProperty JqGraphproperty = new JqplotGraphProperty();
                PbReportViewerDAO reportViewerdao = new PbReportViewerDAO();
//                JqGraphproperty=reportViewerdao.getJqGraphDetails(graphId);
                String jqGraphId = "";
                if (JqGraphproperty != null) {
                    jqGraphId = JqGraphproperty.getGraphTypeId();
                }
                if (graphChangeEvent != null && graphChangeEvent.equalsIgnoreCase("true")) {
                    jqGraphId = changedGraphId;
                    StringBuffer graphString = new StringBuffer();
//                         JqplotGraphProperty graphproperty=new JqplotGraphProperty();
                    ProGenJqPlotChartTypes jqplotcontainer = new ProGenJqPlotChartTypes();
                    ProgenJqplotGraphBD jqplotgraphbd = new ProgenJqplotGraphBD();
                    String chartId = "chart_" + graphNum + "_" + reportId;
                    jqplotgraphbd.chartId = chartId;
                    float divWidth = Float.parseFloat(width) - (Float.parseFloat(width) / 100) * 20;
                    float divHeight = Float.parseFloat(height) - (Float.parseFloat(height) / 100) * 10;
                    graphString.append("<div id='" + chartId + "' style=\"width:" + divWidth + "px;height:" + divHeight + "px;\" align='center'></div>");
                    graphString.append("<script>");
                    String selectgraph = JqGraphproperty.getSlectedGraphType(graphId);
//                                changedGraphId=graphproperty.getGraphTypeId();
                    jqplotgraphbd.JqplotGraphProperty = JqGraphproperty;
                    if (JqGraphproperty.getTableColumns() != null && JqGraphproperty.getTableColumns().length > 0) {
                        jqplotgraphbd.tablecols = JqGraphproperty.getTableColumns();
                    }
                    jqplotgraphbd.setIsFromOneView(true);
                    graphString.append(jqplotgraphbd.prepareJqplotGraph(reportId, changedGraphId, changedGraphType, jqplotcontainer, request, null, selectgraph, graphId));
                    graphString.append("</script>");
                    graph = graphString.toString();
                } else {
//                 graph=templateBd.getOLAPGraphForOneView(reportId, request, response, session, graphNum, true);
                }
                OnceViewContainer onecontainer1 = null;
                String oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
                String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
                String fileName = reportTemplateDAO.getOneviewFileName(oneviewID);
                File file = null;
                String bizzRoleName = "";
                file = new File(oldAdvHtmlFileProps + "/" + fileName);
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(oldAdvHtmlFileProps + "/" + fileName);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    onecontainer1 = (OnceViewContainer) ois.readObject();
                    ois.close();
                }
                Gson gson = new Gson();
                FileReadWrite fileReadWrite = new FileReadWrite();
//String filePath = "/usr/local/cache/"+rolename.replaceAll("\\W", "").trim()+"/"+repname.replaceAll("\\W", "").trim()+"_"+reportId+"/"+repname.replaceAll("\\W", "").trim() +"_"+reportId+"_data.json";
                String filePath = "/usr/local/cache/OneviewGO/oneview_" + oneviewID + "/oneview_" + oneviewID + "_" + regid + "/" + repname.replaceAll("\\W", "").trim() + "_" + reportId + "_data.json";
                File metafile = new File(filePath);
                Type tarType1 = new TypeToken<Map<String, String>>() {
                }.getType();
                Type tarType11 = new TypeToken<Map<String, List<Map<String, String>>>>() {
                }.getType();
                if (metafile.exists()) {
                    String meta = fileReadWrite.loadJSON(filePath);
                    XtendReportMeta reportMeta = new XtendReportMeta();
                    Type metaType = new TypeToken<XtendReportMeta>() {
                    }.getType();
                    reportMeta = gson.fromJson(meta, metaType);
                    List<String> viewBysList = reportMeta.getViewbys();
                    List<String> viewBysList1 = reportMeta.getViewbysIds();
                    timedetails = (ArrayList) onecontainer1.timedetails;
                    DashboardChartData chartDetails = reportMeta.getChartData().get(chartname);
                    graph = viewByid;
//                  Map<String,String> mapData;
//                 List<Map<String, String>> mapData1;
//  mapData = gson.fromJson(meta, tarType1);
// String mapData11 = mapData.get("chartData");
//mapData1= gson.fromJson(mapData11, tarType11);
//             map = (HashMap) session.getAttribute("PROGENTABLES");
//             container = (Container) map.get(reportId);
//             PbReportCollection collect=container.getReportCollect();
//             if(isOLAPInitial!= null && isOLAPInitial.equalsIgnoreCase("true")){
//                 collect.setDrillValuesMap(new HashMap<String,LinkedList>());
//             }
//             if(drillValue != null){
//                 HashMap drillValuesMap=collect.getDrillValuesMap();
//                 if(drillValuesMap!=null && drillValuesMap.get(graphId)!=null){
//                     LinkedList drilValList=(LinkedList)drillValuesMap.get(graphId);
//                     drilValList.add(drillValue);
//                 }
//                 if(drillValuesMap.get(graphId)==null){
//                     LinkedList drilValList=new LinkedList();
//                     drilValList.add(drillValue);
//                     drillValuesMap.put(graphId,drilValList);
//                 }
//             }

//                 timeViewbyType=collect.timeDetailsArray.get(3).toString();
                    timeViewbyType = timedetails.get(3).toString();
//                 
                    OLAPGraph = viewerBd.OLAPGraphForOneViewd3(request, viewBysList, viewBysList1, timedetails, reportId, graphId, graph, height, width, timeViewbyType, drillType, jqGraphId);
//             OLAPGraph=viewerBd.OLAPGraphForOneView(container, reportId, graphId, graph,height,width,timeViewbyType,drillType,jqGraphId);
                }
            }
        }
        response.getWriter().print(OLAPGraph);
        return null;
    }

    public ActionForward buildGraphOnViewbyForOneView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String reportId = request.getParameter("reportId");
        String graphId = request.getParameter("graphId");
        String viewById = request.getParameter("grpNo");
        return null;
    }

    public ActionForward buildDBJqplotGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String graphType = request.getParameter("graphType");
        String graphId = request.getParameter("graphId");
        String dashboardId = request.getParameter("dashboardId");
        String dashletId = request.getParameter("dashletId");
        String contextPath = request.getContextPath();
        String jqGraphString = "";
        if (session != null) {
            ProGenJqPlotChartTypes jqplotcontainer = new ProGenJqPlotChartTypes();
            ProgenJqplotGraphBD jqplotgraphbd = new ProgenJqplotGraphBD();
            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
            Container container = (Container) map.get(dashboardId);
            pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
            DashletDetail dashlet = (DashletDetail) collect.getDashletDetail(dashletId);
            dashlet.setJqplotGraph(true);
            dashlet.setJqPlotGraphType(graphType);
            GraphBuilder graphBuilder = new GraphBuilder();
            graphBuilder.setRequest(request);
            graphBuilder.setResponse(response);
            graphBuilder.setFxCharts(false);
            String userId = session.getAttribute("USERID").toString();
            JqplotGraphProperty jqprop = dashlet.getJqplotgrapprop();
            //
            if (jqprop == null) {
                jqprop = new JqplotGraphProperty();
            }
            jqprop.setGraphId(graphId);
            jqprop.setGraphTypeId(graphId);
            jqprop.setGraphTypename(graphType);
            jqprop.isJqPlot = true;
            dashlet.setJqplotgrapprop(jqprop);
            jqGraphString = graphBuilder.displayGraphs(container, dashletId, contextPath, false, "false");
            dashlet.setJqplotGraph(false);
//             jqGraphString=jqplotgraphbd.JQPlotGraphBuilder(container, dashboardId, dashletId);






//            GraphReport graphDetails=(GraphReport)dashlet.getReportDetails();
//            ArrayList originalColumns=new ArrayList();
//            ArrayList rowViewBys=collect.reportRowViewbyValues;
//            if(rowViewBys != null){
//                String viewBy="A_"+rowViewBys.get(0).toString();
//                originalColumns.add(viewBy);
//            }
//            List<QueryDetail> queryDetails=(List<QueryDetail>) graphDetails.getQueryDetails();
//            String measId="";
//            for(QueryDetail qd:queryDetails){
//                measId="A_"+qd.getElementId();
//                originalColumns.add(measId);
//            }
////            container.setOriginalColumns(originalColumns);
//            jqplotcontainer.setUserid(String.valueOf(session.getAttribute("USERID")));
//            jqplotcontainer.setReportid(dashletId);
//            jqGraphString=jqplotgraphbd.prepareJqplotGraph(dashboardId, graphId, graphType, jqplotcontainer, request, graphId);
            response.getWriter().print(jqGraphString);
        }
        return null;
    }

    public ActionForward getOneViewRollingGraphJQ(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String measId = request.getParameter("measureId");
        String measName = request.getParameter("measName");
        String roleId = request.getParameter("roleId");
        int height = Integer.parseInt(request.getParameter("height"));
        int width = Integer.parseInt(request.getParameter("width"));
        String date = request.getParameter("date");
        String viewLetId = request.getParameter("viewLetId");
        String oneViewId = request.getParameter("oneViewId");
        String isZoomTrend = request.getParameter("isZoomTrend");
        String userId = String.valueOf(session.getAttribute("USERID"));
        DashboardViewerDAO dao = new DashboardViewerDAO();
//        HashMap map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
        OnceViewContainer onecontainer = null;
        OneViewLetDetails viewLet = null;
        String advHtmlFileProps = (String) session.getAttribute("advHtmlFileProps");
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        //String fileName = reportTemplateDAO.getOneviewFileName(oneViewId);
        String fileName = request.getSession(false).getAttribute("tempFileName").toString();
        FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        onecontainer = (OnceViewContainer) ois2.readObject();
        ois2.close();
        viewLet = onecontainer.onviewLetdetails.get(Integer.parseInt(viewLetId));
        String[] dateSplit = date.split("/");
        String MMDDYYYYForm = dateSplit[1] + "/" + dateSplit[0] + "/" + dateSplit[2];
        viewLet.setTrendGraph(true);
        viewLet.setTrendDate(MMDDYYYYForm);
        String trendJq = dao.getOneViewRollingGraphJQ(measId, MMDDYYYYForm, userId, viewLetId, height, width, viewLet, isZoomTrend);

        DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
        OneViewBD oneViewBD = new OneViewBD();
        String result = "";
        result = dashboardTemplateBD.getMeasureDetailsData(request, response, session, onecontainer, viewLet);
        oneViewBD.writeOneviewRegData(onecontainer, result, viewLet.getNoOfViewLets(), request);
        FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(onecontainer);
        oos.flush();
        oos.close();
        response.getWriter().print(trendJq);
        return null;
    }

    /**
     * added by srikanth.p to get the graph Details for particular reportId
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward assignGraphtoMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String reportId = request.getParameter("REPORTID");
        String viewletId = request.getParameter("viewletId");
        String oneViewId = request.getParameter("oneViewId");
        String result = "";
        if (session != null) {
            DashboardViewerDAO dao = new DashboardViewerDAO();
            String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");

            OnceViewContainer onecontainer = new OnceViewContainer();
            OneViewLetDetails detail = new OneViewLetDetails();
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
            //String fileName = reportTemplateDAO.getOneviewFileName(oneViewId);
            String fileName = request.getSession(false).getAttribute("tempFileName").toString();
            FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            onecontainer = (OnceViewContainer) ois2.readObject();
            ois2.close();
            detail = onecontainer.onviewLetdetails.get(Integer.parseInt(viewletId));
            detail.setAssignedReportId(reportId);
            result = dao.getGraphsOnReport(reportId, detail);
            FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(onecontainer);
            oos.flush();
            oos.close();
            response.getWriter().print(result);
        }
        return null;
    }

    public ActionForward oneViewSettings(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String oneViewId = request.getParameter("oneViewId");
        String settingType = request.getParameter("seType");
        String isEveryTime = request.getParameter("isEveryTime");
        OnceViewContainer onecontainer = new OnceViewContainer();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        String fileName = reportTemplateDAO.getOneviewFileName(oneViewId);
//    String fileName=request.getSession(false).getAttribute("tempFileName").toString();
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
        FileInputStream fis2 = new FileInputStream(advHtmlFileProps + "/" + fileName);
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        onecontainer = (OnceViewContainer) ois2.readObject();
        ois2.close();
        onecontainer.setSettingType(settingType);
        onecontainer.setEveryTimeUpdate(Boolean.parseBoolean(isEveryTime));
        FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(onecontainer);
        oos.flush();
        oos.close();
        response.getWriter().print("");
        return null;
    }

//rename DashBoard
//
    public ActionForward renameDashBoard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String DashboardId = request.getParameter("dashBoardId");
        String oldName = request.getParameter("oldName");
        String newName = request.getParameter("newName");
        String dashRENameQry = null;
        PbDb pbdb = new PbDb();
        dashRENameQry = "update  PRG_AR_REPORT_MASTER set REPORT_NAME='" + newName + "' where REPORT_ID=" + DashboardId;
        pbdb.execUpdateSQL(dashRENameQry);
        response.getWriter().print("success");
        return null;
    }
// added by mohit
    public ActionForward getQueryReturnObject(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PbReportQuery repQuery = new PbReportQuery();
        LinkedHashMap reportParametersValues = new LinkedHashMap();
        HashMap<String, List> inMap = new HashMap<String, List>();
        ArrayList<String> queryCols = new ArrayList<String>();
        ArrayList<String> queryAggs = new ArrayList<String>();
        ArrayList<String> timeDetailsArray = new ArrayList<String>();
        ArrayList<String> ParametersValues = new ArrayList<String>();
        ParametersValues.add("All");

        reportParametersValues.put("13998", ParametersValues);
        reportParametersValues.put("14003", ParametersValues);
        reportParametersValues.put("13972", ParametersValues);
        inMap.put("13998", ParametersValues);
        inMap.put("14003", ParametersValues);
        inMap.put("13972", ParametersValues);
        queryCols.add("14786");
        queryCols.add("14787");
        queryCols.add("14788");
        queryCols.add("14789");
        queryAggs.add("sum");
        queryAggs.add("sum");
        queryAggs.add("sum");
        queryAggs.add("sum");
        timeDetailsArray.add("Day");
        timeDetailsArray.add("PRG_DATE_RANGE");
        timeDetailsArray.add("09/01/2015");
        timeDetailsArray.add("09/30/2015");
        timeDetailsArray.add("08/01/2012");
        timeDetailsArray.add("08/31/2012");
        repQuery.isKpi = true;
        repQuery.setParamValue(reportParametersValues);
//                      HashMap<String, List> inMap=(HashMap<String, List>)collect.operatorFilters.get("IN");

        repQuery.setInMap(inMap);

        repQuery.setRowViewbyCols(new ArrayList());
        repQuery.setColViewbyCols(new ArrayList());
        repQuery.setQryColumns(queryCols);
        repQuery.setColAggration(queryAggs);
        repQuery.setTimeDetails(timeDetailsArray);
        repQuery.setDefaultMeasure("14786");
        repQuery.setDefaultMeasureSumm("sum");
        repQuery.setReportId("2335");
        repQuery.setBizRoles("6");
        repQuery.setUserId("5");
        PbReturnObject pbretObj = repQuery.getPbReturnObject("14786");

        response.getWriter().print("success");
        return null;
    }
}