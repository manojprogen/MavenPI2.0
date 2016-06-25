/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportdesigner.action;

import com.progen.dashboard.DashboardConstants;
import com.progen.dashboardView.bd.PbDashboardViewerBD;
import com.progen.dashboardView.db.DashboardViewerDAO;
import com.progen.graph.GraphBuilder;
import com.progen.oneView.bd.OneViewBD;
import com.progen.report.DashletDetail;
import com.progen.report.KPIElement;
import com.progen.report.PbReportCollection;
import com.progen.report.entities.KPIColorRange;
import com.progen.report.entities.KPIGraph;
import com.progen.report.pbDashboardCollection;
import com.progen.report.query.PbReportQuery;
import com.progen.reportdesigner.bd.DashboardTemplateBD;
import com.progen.reportdesigner.db.DashBoardTemplateResourcBundle;
import com.progen.reportdesigner.db.DashboardTemplateDAO;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.db.PbReportViewerDAO;
import java.io.*;
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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import prg.db.*;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

/**
 *
 * @author mahesh.sanampudi@progenbusiness.com
 */
public class DashboardTemplateAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(DashboardTemplateAction.class);

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("getUserDims", "getUserDims");
        map.put("getKpis", "getKpis");
        map.put("getGraphReports", "getGraphReports");
        map.put("buildDbrdParams", "buildDbrdParams");
        map.put("buildDbrdGraph", "buildDbrdGraph");
        map.put("dispDbrdParams", "dispDbrdParams");
        map.put("saveDashboard", "saveDashboard");
        map.put("goToDashboardDesigner", "goToDashboardDesigner");
        map.put("checkDashboardName", "checkDashboardName");
        map.put("getKpisGraphs", "getKpisGraphs");
        map.put("displayKpiGraph", "displayKpiGraph");
        map.put("kpisBydivid", "kpisBydivid");
        map.put("grpColsBydivid", "grpColsBydivid");
        map.put("getKpiGrpValue", "getKpiGrpValue");
        map.put("buildDbrdGraphs", "buildDbrdGraphs");
        map.put("getTemplateDivs", "getTemplateDivs");
        map.put("getKpiGraphTargets", "getKpiGraphTargets");
        map.put("getDeviation", "getDeviation");
        map.put("resetParameters", "resetParameters");
        map.put("EditDashBdName", "EditDashBdName");
        map.put("chckDashbdNameBfrUpdate", "chckDashbdNameBfrUpdate");
        map.put("ChangeDashDName", "ChangeDashDName");
        map.put("restCurrDashBd", "restCurrDashBd");
        map.put("getCreateKPIs", "getCreateKPIs");
        map.put("getCreateKPIData", "getCreateKPIData");
        map.put("buildCreateKPI", "buildCreateKPI");
        map.put("deleteFavoParam", "deleteFavoParam");
        map.put("buildCreateKPI", "buildCreateKPI");
        map.put("setParamhashmap", "setParamhashmap");
        map.put("getGroupMeasures", "getGroupMeasures");
        map.put("groupMeasureInitialView", "groupMeasureInitialView");
        map.put("reloadGroupMeassures", "reloadGroupMeassures");
        map.put("getAllViewParams", "getAllViewParams");
        map.put("buildOneviewComplexKPI", "buildOneviewComplexKPI");
        map.put("goToKPIDashboardDesigner", "goToKPIDashboardDesigner");
        map.put("selectRoleGoToKPiDashbrdDesin", "selectRoleGoToKPiDashbrdDesin");
        map.put("buildDbrdParamsForKpiDashBrd", "buildDbrdParamsForKpiDashBrd");
        map.put("goToTimeDashboardDesigner", "goToTimeDashboardDesigner");
        map.put("selectRoleGoToTimeDashbrdDesin", "selectRoleGoToTimeDashbrdDesin");
        map.put("hideColumns", "hideColumns");//added by sruthi for hide columns


        return map;
    }
    private final static String SUCCESS = "success";

    public ActionForward getUserDims(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String foldersIds = "";
        String PbUserId = "";
        HashMap map = new HashMap();
        Container container = null;
        String customDbrdId = "";
        if (session != null) {
            foldersIds = request.getParameter("foldersIds");
            PbUserId = String.valueOf(session.getAttribute("USERID"));
            map = (HashMap) session.getAttribute("PROGENTABLES");
            customDbrdId = request.getParameter("dashboardId");
            container = (Container) map.get(customDbrdId);

            HashMap ParametersHashMap = container.getParametersHashMap();
            ParametersHashMap.put("UserFolderIds", foldersIds);
            DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
            String result = dashboardTemplateDAO.getUserDims(foldersIds, PbUserId);
            PrintWriter out = response.getWriter();
            out.print(result);
            container.setParametersHashMap(ParametersHashMap);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getKpis(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String foldersIds = "";
        String divId = "";
        foldersIds = request.getParameter("foldersIds");
        divId = request.getParameter("divId");
        String dbrdId = request.getParameter("dashboardId");
        String kpiType = request.getParameter("kpiType");
        String tableList = request.getParameter("tableList");
        Container container = Container.getContainerFromSession(request, dbrdId);
        if (foldersIds == null || "".equals(foldersIds)) {
            container = Container.getContainerFromSession(request, dbrdId);
            PbReportCollection collect = container.getReportCollect();
            String[] ids = collect.reportBizRoles;
            if (ids != null && ids.length > 0) {
                foldersIds = ids[0];
            }
        }
        DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
        String result = "";
        ReportTemplateDAO repdao = new ReportTemplateDAO();
        ArrayList Parameters = new ArrayList();
        if (container.getTableList() != null && !container.getTableList().isEmpty()) {
            ArrayList alist = new ArrayList();
            result = repdao.getMeasuresForReport(foldersIds, Parameters, request.getContextPath(), container.getTableList());
            container.setTableList(alist);
        } else if (tableList != null && tableList.equalsIgnoreCase("true")) {
            result = dashboardTemplateDAO.getKpis(foldersIds);
        }

        request.setAttribute("Kpis", result);
        request.setAttribute("dbrdId", dbrdId);
        request.setAttribute("kpiType", kpiType);
        request.setAttribute("divId", divId);
        return mapping.findForward("kpis");
    }

    public ActionForward kpisBydivid(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String customDbrdId = "";
        String divId = "";
        if (session != null) {
            divId = request.getParameter("divId");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            customDbrdId = request.getParameter("dashboardId");
            container = (Container) map.get(customDbrdId);
            HashMap kpibyDivMap = container.getKpiIdNamesMap();//(HashMap) session.getAttribute("kpiIdNamesMap");
            ArrayList kpiDivList = (ArrayList) kpibyDivMap.get(divId);
            PrintWriter out = response.getWriter();
            out.print(kpiDivList);

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward grpColsBydivid(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String customDbrdId = "";
        String divId = "";
        if (session != null) {
            divId = request.getParameter("divId");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            customDbrdId = request.getParameter("dashboardId");
            container = (Container) map.get(customDbrdId);
            HashMap grpColsDivMap = container.getGrpColsbyDivMap();//(HashMap) session.getAttribute("grpColsbyDivMap");

            String grpColsDivList = String.valueOf(grpColsDivMap.get(divId));
            PrintWriter out = response.getWriter();
            out.print(grpColsDivList);

            return null;
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
                } else {
                    query = "SELECT nvl(AGGREGATION_TYPE,'SUM') AGGREGATION_TYPE FROM PRG_USER_ALL_INFO_DETAILS where element_id=" + kpiIds;
                }

                PbReturnObject retObj = pbdb.execSelectSQL(query);

                map = (HashMap) hs.getAttribute("PROGENTABLES");
                customDbrdId = dbrdId;
                container = (Container) map.get(dbrdId);

                HashMap ParametersMap = container.getParametersHashMap();//(HashMap) hs.getAttribute("ParametersHashMap");
                ArrayList params = (ArrayList) ParametersMap.get("Parameters");
                ArrayList timeDetails = (ArrayList) ParametersMap.get("TimeDetailstList");
                ArrayList reportRowViewbyValues = new ArrayList();
                reportRowViewbyValues.add(params.get(0));
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
                repQuery.setDefaultMeasureSumm(String.valueOf(retObj.getFieldValueString(0, 0)));
                repQuery.isKpi = true;
                pbretObj = repQuery.getPbReturnObject(kpiIds);


            } catch (Exception e) {
                logger.error("Exception:", e);
            }

        }
        return pbretObj;
    }

    public ActionForward getGraphReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        //////.println("getGraphReports in templateaction ");

        DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
        HttpSession session = request.getSession(false);
        String buzRoles = request.getParameter("buzRoles");
        String dashboardId = request.getParameter("dashboardId");
        if (buzRoles == null || "".equals(buzRoles)) {
            Container container = Container.getContainerFromSession(request, dashboardId);
            PbReportCollection collect = container.getReportCollect();
            String[] ids = collect.reportBizRoles;
            if (ids != null && ids.length > 0) {
                buzRoles = ids[0];
            }
        }
        Container container = Container.getContainerFromSession(request, dashboardId);
        HashMap<String, ArrayList<String>> paramhashmap = container.getParametersHashMap();
        ArrayList<String> parameters = paramhashmap.get("Parameters");
        ArrayList<String> parameterNames = paramhashmap.get("ParameterNames");
        String result = "";
        if (session != null) {
            if (parameters.get(0).equalsIgnoreCase("") || parameters.isEmpty()) {

                result = dashboardTemplateDAO.getTrendGraphReportsByBuzRoles(buzRoles);//DashBoardsGraphs);
                request.setAttribute("Graphs", result);
                return mapping.findForward("graphs");
            } else {
                result = dashboardTemplateDAO.getGraphReportsByBuzRoles(buzRoles);//DashBoardsGraphs);
                request.setAttribute("Graphs", result);
                return mapping.findForward("graphs");
            }
        } else {

            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward buildDbrdParams(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        ProgenParam pParam = new ProgenParam();
        HttpSession session = request.getSession(false);

        String customReportId = null;
        HashMap map = null;
        Container container = null;
        pbDashboardCollection collect = null;
        HashMap ParametersMap;
        String customDbrdId = "";
        String foldersIds = "";
        String dimId = "";

        if (session != null) {
            String paramIds = request.getParameter("params");
            String timeparams = request.getParameter("timeparams");
            String paramNames = request.getParameter("paramArray");
            foldersIds = request.getParameter("foldersIds");
            dimId = request.getParameter("dimId");
            DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
            String minTimeLevel = dashboardTemplateDAO.getUserFolderMinTimeLevel(foldersIds);
            String[] parameterIds = null;
            String[] timeparameterIds = null;
            String[] parameterNames = null;
            ArrayList Parameters = new ArrayList();
            ArrayList ParameterNames = new ArrayList();
            ArrayList timeParameters = new ArrayList();

            map = (HashMap) session.getAttribute("PROGENTABLES");
            customDbrdId = request.getParameter("dashboardId");
            container = (Container) map.get(customDbrdId);
            collect = (pbDashboardCollection) container.getReportCollect();

            ParametersMap = container.getParametersHashMap();
            if (ParametersMap == null) {
                ParametersMap = new HashMap();
            }
            ParametersMap.put("UserFolderIds", foldersIds);

            if (paramIds != null) {
                parameterIds = paramIds.split(",");
                parameterNames = paramNames.split(",");
                for (int paramCount = 0; paramCount < parameterIds.length; paramCount++) {
                    Parameters.add(parameterIds[paramCount]);
                    ParameterNames.add(parameterNames[paramCount]);
                }
                if (parameterIds.length > 0) {
                    collect.reportRowViewbyValues = new ArrayList<String>();
                    collect.reportRowViewbyValues.add(parameterIds[0]);
                }
            }

            if (timeparams != null) {
                timeparameterIds = timeparams.split(",");
                for (int timeparamCount = 0; timeparamCount < timeparameterIds.length; timeparamCount++) {
                    timeParameters.add(timeparameterIds[timeparamCount]);
                }
            }

            HashMap hm = new HashMap();
            for (int i = 0; i < Parameters.size(); i++) {
                hm.put(Parameters.get(i), ParameterNames.get(i));
            }

            container.setParameterIds(hm);

            ParametersMap.put("Parameters", Parameters);
            ParametersMap.put("ParameterNames", ParameterNames);
            ParametersMap.put("timeParameters", timeParameters);

            Date date = new Date();
            String DATE_FORMAT = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

            HashMap timeDimMap = new HashMap();
            ArrayList timeDetails = new ArrayList();
            ArrayList dateArray = new ArrayList();
            ArrayList weekArray = new ArrayList();
            ArrayList rangeArray = new ArrayList();
            ArrayList rangeArray1 = new ArrayList();
            ArrayList rangeArray2 = new ArrayList();
            ArrayList rangeArray3 = new ArrayList();
            ArrayList asofWeekArray = new ArrayList();
            ArrayList monthArray = new ArrayList();
            ArrayList asofMonthArray = new ArrayList();
            ArrayList qtrArray = new ArrayList();
            ArrayList asofQtrArray = new ArrayList();
            ArrayList yearArray = new ArrayList();
            ArrayList asofYrArray = new ArrayList();
            ArrayList periodTypeArray = new ArrayList();
            ArrayList compareArray = new ArrayList();
            ArrayList collectTimeDetailsArray = new ArrayList();

            if (dimId.equalsIgnoreCase("Time-Period Basis") || dimId.equalsIgnoreCase("Time-Range Basis") || dimId.equalsIgnoreCase("Time-Rolling Period") || dimId.equalsIgnoreCase("Time-Month Range Basis")
                    || dimId.equalsIgnoreCase("Time-Quarter Range Basis") || dimId.equalsIgnoreCase("Time-Year Range Basis") || minTimeLevel.equals("4")
                    || (minTimeLevel.equals("3")) || (minTimeLevel.equals("2")) || (minTimeLevel.equals("1"))) {
                if (timeParameters != null) {
                    for (int time = 0; time < timeParameters.size(); time++) {
                        if (minTimeLevel.equals("5")) {
                            if (dimId.equalsIgnoreCase("Time-Period Basis")) {
                                if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DATE")) {
                                    dateArray.add(pParam.getdateforpage().toString());
                                    dateArray.add("CBO_AS_OF_DATE");
                                    dateArray.add("DATE");
                                    dateArray.add("1");
                                    dateArray.add("1");
                                    dateArray.add(null);
                                    dateArray.add("AS_OF_DATE");
                                    timeDimMap.put("AS_OF_DATE", dateArray);

                                    collectTimeDetailsArray.add("Day");
                                    collectTimeDetailsArray.add("PRG_STD");
                                    collectTimeDetailsArray.add(pParam.getdateforpage().toString());
                                    collectTimeDetailsArray.add("Month");
                                    collectTimeDetailsArray.add("Last Period");
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                                    periodTypeArray.add("MONTH");
                                    periodTypeArray.add("CBO_PRG_PERIOD_TYPE");
                                    periodTypeArray.add("AGGREGATION");
                                    periodTypeArray.add("2");
                                    periodTypeArray.add("2");
                                    periodTypeArray.add("MONTH");
                                    periodTypeArray.add("PRG_PERIOD_TYPE");
                                    timeDimMap.put("PRG_PERIOD_TYPE", periodTypeArray);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("PRG_COMPARE")) {
                                    compareArray.add("LAST PERIOD");
                                    compareArray.add("CBO_PRG_COMPARE");
                                    compareArray.add("COMPARE");
                                    compareArray.add("3");
                                    compareArray.add("1001");
                                    compareArray.add("LAST PERIOD");
                                    compareArray.add("PRG_COMPARE");
                                    timeDimMap.put("PRG_COMPARE", compareArray);
                                }
                                timeDetails.add("Day");
                                timeDetails.add("PRG_STD");
                                timeDetails.add(pParam.getdateforpage().toString());
                                timeDetails.add("Month");
                                timeDetails.add("Last Period");
                            }
                            if (dimId.equalsIgnoreCase("Time-Range Basis")) {
                                if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DATE1")) {
                                    rangeArray.add(String.valueOf(pParam.getdateforpage(30)));
                                    rangeArray.add("CBO_AS_OF_DATE1");
                                    rangeArray.add("From DATE");
                                    rangeArray.add("1");
                                    rangeArray.add("1");
                                    rangeArray.add(null);
                                    rangeArray.add("AS_OF_DATE1");
                                    timeDimMap.put("AS_OF_DATE1", rangeArray);

                                    collectTimeDetailsArray.add("Day");
                                    collectTimeDetailsArray.add("PRG_DATE_RANGE");
                                    //collectTimeDetailsArray.add(sdf.format(date));
                                    //collectTimeDetailsArray.add("05/31/2008");
                                    collectTimeDetailsArray.add(String.valueOf(pParam.getdateforpage(30)));
                                    collectTimeDetailsArray.add(String.valueOf(pParam.getdateforpage()));
                                    collectTimeDetailsArray.add(String.valueOf(pParam.getdateforpage(60)));
                                    collectTimeDetailsArray.add(String.valueOf(pParam.getdateforpage(31)));;
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DATE2")) {
                                    rangeArray1.add(String.valueOf(pParam.getdateforpage(1)));
                                    rangeArray1.add("CBO_AS_OF_DATE2");
                                    rangeArray1.add("To DATE");
                                    rangeArray1.add("2");
                                    rangeArray1.add("2");
                                    rangeArray1.add(null);
                                    rangeArray1.add("AS_OF_DATE2");
                                    timeDimMap.put("AS_OF_DATE2", rangeArray1);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE1")) {
                                    rangeArray2.add(String.valueOf(pParam.getdateforpage(31)));
                                    rangeArray2.add("CBO_CMP_AS_OF_DATE1");
                                    rangeArray2.add("Comp From DATE");
                                    rangeArray2.add("3");
                                    rangeArray2.add("3");
                                    rangeArray2.add(null);
                                    rangeArray2.add("CMP_AS_OF_DATE1");
                                    timeDimMap.put("CMP_AS_OF_DATE1", rangeArray2);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                                    rangeArray3.add(String.valueOf(pParam.getdateforpage(60)));
                                    rangeArray3.add("CBO_CMP_AS_OF_DATE2");
                                    rangeArray3.add("Comp To DATE");
                                    rangeArray3.add("4");
                                    rangeArray3.add("4");
                                    rangeArray3.add(null);
                                    rangeArray3.add("CMP_AS_OF_DATE2");
                                    timeDimMap.put("CMP_AS_OF_DATE2", rangeArray3);
                                }
                                timeDetails.add("Day");
                                timeDetails.add("PRG_DATE_RANGE");
                                timeDetails.add(pParam.getdateforpage().toString());
                                timeDetails.add(null);
                                timeDetails.add(null);
                                timeDetails.add(null);
                            }
                            if (dimId.equalsIgnoreCase("Time-Rolling Period")) {
                                if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DATE")) {
                                    rangeArray.add(String.valueOf(pParam.getdateforpage(30)));
                                    rangeArray.add("CBO_AS_OF_DATE");
                                    rangeArray.add("DATE");
                                    rangeArray.add("1");
                                    rangeArray.add("1");
                                    rangeArray.add(null);
                                    rangeArray.add("AS_OF_DATE");
                                    timeDimMap.put("AS_OF_DATE", rangeArray);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("PRG_DAY_ROLLING")) {
                                    rangeArray1.add("Last 30 Days");
                                    rangeArray1.add("CBO_PRG_DAY_ROLLING");
                                    rangeArray1.add("Rolling Period");
                                    rangeArray1.add("2");
                                    rangeArray1.add("2");
                                    rangeArray1.add("Last 30 Days");
                                    rangeArray1.add("PRG_DAY_ROLLING");
                                    timeDimMap.put("PRG_DAY_ROLLING", rangeArray1);
                                }
                                timeDetails.add("Day");
                                timeDetails.add("PRG_DAY_ROLLING");
                                timeDetails.add(pParam.getdateforpage().toString());
                                timeDetails.add("Last 30 Days");
                            }
                            if (dimId.equalsIgnoreCase("Time-Month Range Basis")) {
                                if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DMONTH1")) {
                                    rangeArray.add(pParam.getdateforpage().toString());
                                    rangeArray.add("CBO_AS_OF_DMONTH1");
                                    rangeArray.add("From MONTH");
                                    rangeArray.add("1");
                                    rangeArray.add("1");
                                    rangeArray.add(null);
                                    rangeArray.add("AS_OF_DMONTH1");
                                    timeDimMap.put("AS_OF_DMONTH1", rangeArray);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DMONTH2")) {
                                    rangeArray1.add(pParam.getdateforpage(30).toString());
                                    rangeArray1.add("CBO_AS_OF_DMONTH2");
                                    rangeArray1.add("To MONTH");
                                    rangeArray1.add("2");
                                    rangeArray1.add("2");
                                    rangeArray1.add(null);
                                    rangeArray1.add("AS_OF_DMONTH2");
                                    timeDimMap.put("AS_OF_DMONTH2", rangeArray1);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DMONTH1")) {
                                    rangeArray2.add(pParam.getdateforpage(31).toString());
                                    rangeArray2.add("CBO_CMP_AS_OF_DMONTH1");
                                    rangeArray2.add("Comp From MONTH");
                                    rangeArray2.add("3");
                                    rangeArray2.add("3");
                                    rangeArray2.add(null);
                                    rangeArray2.add("CMP_AS_OF_DMONTH1");
                                    timeDimMap.put("CMP_AS_OF_DMONTH1", rangeArray2);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DM0NTH2")) {
                                    rangeArray3.add(pParam.getdateforpage(60).toString());
                                    rangeArray3.add("CBO_CMP_AS_OF_DMONTH2");
                                    rangeArray3.add("Comp To MONTH");
                                    rangeArray3.add("4");
                                    rangeArray3.add("4");
                                    rangeArray3.add(null);
                                    rangeArray3.add("CMP_AS_OF_DM0NTH2");
                                    timeDimMap.put("CMP_AS_OF_DMONTH2", rangeArray3);
                                }
                                timeDetails.add("Day");
                                timeDetails.add("PRG_MONTH_RANGE");
                                timeDetails.add(pParam.getmonthforpage().toString());
                                timeDetails.add(null);
                                timeDetails.add(null);
                                timeDetails.add(null);
                            }
                            if (dimId.equalsIgnoreCase("Time-Quarter Range Basis")) {
                                if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DQUARTER1")) {
                                    rangeArray.add(pParam.getdateforpage().toString());
                                    rangeArray.add("CBO_AS_OF_DQUARTER1");
                                    rangeArray.add("From QUARTER");
                                    rangeArray.add("1");
                                    rangeArray.add("1");
                                    rangeArray.add(null);
                                    rangeArray.add("AS_OF_DQUARTER1");
                                    timeDimMap.put("AS_OF_DQUARTER1", rangeArray);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DQUARTER2")) {
                                    rangeArray1.add(pParam.getdateforpage(30).toString());
                                    rangeArray1.add("CBO_AS_OF_DQUARTER2");
                                    rangeArray1.add("To QUARTER");
                                    rangeArray1.add("2");
                                    rangeArray1.add("2");
                                    rangeArray1.add(null);
                                    rangeArray1.add("AS_OF_DQUARTER2");
                                    timeDimMap.put("AS_OF_DQUARTER2", rangeArray1);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DQUARTER1")) {
                                    rangeArray2.add(pParam.getdateforpage(31).toString());
                                    rangeArray2.add("CBO_CMP_AS_OF_DQUARTER1");
                                    rangeArray2.add("Comp From QUARTER");
                                    rangeArray2.add("3");
                                    rangeArray2.add("3");
                                    rangeArray2.add(null);
                                    rangeArray2.add("CMP_AS_OF_DQUARTER1");
                                    timeDimMap.put("CMP_AS_OF_DQUARTER1", rangeArray2);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DQUARTER2")) {
                                    rangeArray3.add(pParam.getdateforpage(60).toString());
                                    rangeArray3.add("CBO_CMP_AS_OF_DQUARTER2");
                                    rangeArray3.add("Comp To QUARTER");
                                    rangeArray3.add("4");
                                    rangeArray3.add("4");
                                    rangeArray3.add(null);
                                    rangeArray3.add("CMP_AS_OF_DQUARTER2");
                                    timeDimMap.put("CMP_AS_OF_DQUARTER2", rangeArray3);
                                }
                                timeDetails.add("Day");
                                timeDetails.add("PRG_QUARTER_RANGE");
                                timeDetails.add("Dquarter");
                                timeDetails.add(null);
                                timeDetails.add(null);
                                timeDetails.add(null);
                            }
                            if (dimId.equalsIgnoreCase("Time-Year Range Basis")) {
                                if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DYEAR1")) {
                                    rangeArray.add(pParam.getdateforpage().toString());
                                    rangeArray.add("CBO_AS_OF_DYEAR1");
                                    rangeArray.add("From YEAR");
                                    rangeArray.add("1");
                                    rangeArray.add("1");
                                    rangeArray.add(null);
                                    rangeArray.add("AS_OF_DYEAR1");
                                    timeDimMap.put("AS_OF_DYEAR1", rangeArray);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DATE")) {
                                    rangeArray1.add(pParam.getdateforpage(30).toString());
                                    rangeArray1.add("CBO_AS_OF_DYEAR2");
                                    rangeArray1.add("To YEAR");
                                    rangeArray1.add("2");
                                    rangeArray1.add("2");
                                    rangeArray1.add(null);
                                    rangeArray1.add("AS_OF_DYEAR2");
                                    timeDimMap.put("AS_OF_DYEAR2", rangeArray1);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DYEAR1")) {
                                    rangeArray2.add(pParam.getdateforpage(31).toString());
                                    rangeArray2.add("CBO_CMP_AS_OF_DYEAR1");
                                    rangeArray2.add("Comp From YEAR");
                                    rangeArray2.add("3");
                                    rangeArray2.add("3");
                                    rangeArray2.add(null);
                                    rangeArray2.add("CMP_AS_OF_DYEAR1");
                                    timeDimMap.put("CMP_AS_OF_DYEAR1", rangeArray2);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DYEAR2")) {
                                    rangeArray3.add(pParam.getdateforpage(60).toString());
                                    rangeArray3.add("CBO_CMP_AS_OF_DYEAR2");
                                    rangeArray3.add("Comp To YEAR");
                                    rangeArray3.add("4");
                                    rangeArray3.add("4");
                                    rangeArray3.add(null);
                                    rangeArray3.add("CMP_AS_OF_DYEAR2");
                                    timeDimMap.put("CMP_AS_OF_DYEAR2", rangeArray3);
                                }
                                timeDetails.add("Day");
                                timeDetails.add("PRG_YEAR_RANGE");
                                timeDetails.add(pParam.getYearforpage().toString());
                                timeDetails.add(null);
                                timeDetails.add(null);
                                timeDetails.add(null);
                            }
                        } else if (minTimeLevel.equals("4")) {
                            if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_WEEK")) {
                                weekArray.add(null);
                                weekArray.add("CBO_AS_OF_WEEK");
                                weekArray.add("WEEK");
                                weekArray.add("1");
                                weekArray.add("1");
                                weekArray.add(null);
                                weekArray.add("AS_OF_WEEK");
                                timeDimMap.put("AS_OF_WEEK", weekArray);
                            } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_WEEK1")) {
                                asofWeekArray.add(null);
                                asofWeekArray.add("CBO_AS_OF_WEEK1");
                                asofWeekArray.add("COMPAREWEEK");
                                asofWeekArray.add("2");
                                asofWeekArray.add("2");
                                asofWeekArray.add(null);
                                asofWeekArray.add("AS_OF_WEEK1");
                                timeDimMap.put("AS_OF_WEEK1", asofWeekArray);
                            }
                            timeDetails.add("WEEK");
                            timeDetails.add("PRG_WEEK_CMP");
                            timeDetails.add(null);
                            timeDetails.add(null);

                        } else if (minTimeLevel.equals("3")) {
                            if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_MONTH")) {
                                monthArray.add(null);
                                monthArray.add("CBO_AS_OF_MONTH");
                                monthArray.add("MONTH");
                                monthArray.add("1");
                                monthArray.add("1");
                                monthArray.add(null);
                                monthArray.add("AS_OF_MONTH");
                                timeDimMap.put("AS_OF_MONTH", monthArray);
                            } else if (timeParameters.get(time).toString().equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                                periodTypeArray.add("MONTH");
                                periodTypeArray.add("CBO_PRG_PERIOD_TYPE");
                                periodTypeArray.add("AGGREGATION");
                                periodTypeArray.add("2");
                                periodTypeArray.add("2");
                                periodTypeArray.add("MONTH");
                                periodTypeArray.add("PRG_PERIOD_TYPE");
                                timeDimMap.put("PRG_PERIOD_TYPE", periodTypeArray);
                            } else if (timeParameters.get(time).toString().equalsIgnoreCase("PRG_COMPARE")) {
                                compareArray.add("LAST PERIOD");
                                compareArray.add("CBO_PRG_COMPARE");
                                compareArray.add("COMPARE");
                                compareArray.add("3");
                                compareArray.add("1001");
                                compareArray.add("LAST PERIOD");
                                compareArray.add("PRG_COMPARE");
                                timeDimMap.put("PRG_COMPARE", compareArray);
                            }
                            timeDetails.add("Month");
                            timeDetails.add("PRG_STD");
                            timeDetails.add(null);
                            timeDetails.add("Month");
                            timeDetails.add("Last Period");

                        } else if (minTimeLevel.equals("2")) {
                            if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_QUARTER")) {
                                qtrArray.add(null);
                                qtrArray.add("CBO_AS_OF_QUARTER");
                                qtrArray.add("QUARTER");
                                qtrArray.add("1");
                                qtrArray.add("1");
                                qtrArray.add(null);
                                qtrArray.add("AS_OF_QUARTER");
                                timeDimMap.put("AS_OF_QUARTER", qtrArray);
                            } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_QUARTER1")) {
                                asofQtrArray.add(null);
                                asofQtrArray.add("CBO_AS_OF_QUARTER1");
                                asofQtrArray.add("COMPAREQUARTER");
                                asofQtrArray.add("2");
                                asofQtrArray.add("2");
                                asofQtrArray.add(null);
                                asofQtrArray.add("AS_OF_QUARTER1");
                                timeDimMap.put("AS_OF_QUARTER1", asofQtrArray);
                            }
                            timeDetails.add("QUARTER");
                            timeDetails.add("PRG_QUARTER_CMP");
                            timeDetails.add(null);
                            timeDetails.add(null);

                        } else if (minTimeLevel.equals("1")) {
                            if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_YEAR")) {
                                yearArray.add(null);
                                yearArray.add("CBO_AS_OF_YEAR");
                                yearArray.add("YEAR");
                                yearArray.add("1");
                                yearArray.add("1");
                                yearArray.add(null);
                                yearArray.add("AS_OF_YEAR");
                                timeDimMap.put("AS_OF_YEAR", yearArray);
                            } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_YEAR1")) {
                                asofYrArray.add(null);
                                asofYrArray.add("CBO_AS_OF_YEAR1");
                                asofYrArray.add("COMPAREYEAR");
                                asofYrArray.add("2");
                                asofYrArray.add("2");
                                asofYrArray.add(null);
                                asofYrArray.add("AS_OF_YEAR1");
                                timeDimMap.put("AS_OF_YEAR1", asofYrArray);
                            }
                            timeDetails.add("YEAR");
                            timeDetails.add("PRG_YEAR_CMP");
                            timeDetails.add(null);
                            timeDetails.add(null);
                        }
                    }
                }
                ParametersMap.put("TimeDimHashMap", timeDimMap);
                ParametersMap.put("TimeDetailstList", timeDetails);
                collect.timeDetailsArray = collectTimeDetailsArray;
                collect.reportParametersValues = new LinkedHashMap<String, String>();
                if (foldersIds != null) {
                    collect.reportBizRoles = foldersIds.split(",");
                }
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward dispDbrdParams(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        DashboardTemplateDAO dashboardDAO = new DashboardTemplateDAO();
        String params = "";
        HashMap map = new HashMap();
        Container container = null;
        HashMap ParametersHashMap = null;
        String result = null;
        PrintWriter out = null;
        String customDbrdId = "";
        ArrayList Parameters = new ArrayList();
        ArrayList ParameterNames = new ArrayList();
        String[] parameterIds = null;
        String[] parameterNames = null;
        if (session != null) {
            try {
                String paramIds = request.getParameter("params");
                String paramNames = request.getParameter("paramArray");
                map = (HashMap) session.getAttribute("PROGENTABLES");
                customDbrdId = request.getParameter("dashboardId");
                container = (Container) map.get(customDbrdId);
                params = request.getParameter("params");
                ParametersHashMap = container.getParametersHashMap();

                if (!params.equalsIgnoreCase("")) {
                    parameterIds = paramIds.split(",");
                    parameterNames = paramNames.split(",");
                    for (int paramCount = 0; paramCount < parameterIds.length; paramCount++) {
                        Parameters.add(parameterIds[paramCount]);
                        ParameterNames.add(parameterNames[paramCount]);
                    }
                }

                ArrayList newparamNames = new ArrayList();
                HashMap hmap = container.getParameterIds();
                if (hmap.get(0) != null) {
                    for (int i = 0; i < parameterIds.length; i++) {
                        String[] parameterKeySet = (String[]) hmap.keySet().toArray(new String[0]);
                        for (int j = 0; j < hmap.size(); j++) {
                            if (parameterIds[i].equals(parameterKeySet[j])) {
                                newparamNames.add(hmap.get(parameterKeySet[j]));
                            }
                        }
                    }
                }
                HashMap ParamHashMap = container.getParametersHashMap();
                ParamHashMap.remove("Parameters");
                ParamHashMap.remove("ParameterNames");

                ParamHashMap.put("Parameters", Parameters);
                ParamHashMap.put("ParameterNames", newparamNames);
                container.setParametersHashMap(ParamHashMap);

                result = dashboardDAO.dispDbrdParameters(params, ParametersHashMap);
                out = response.getWriter();
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

    public ActionForward saveDashboard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
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
        HashMap dashboardMap = null;
        DashBoardTemplateResourcBundle resBundle = new DashBoardTemplateResourcBundle();
        DashboardTemplateDAO paramdDAO = new DashboardTemplateDAO();
        String dashboardName = "";
        String dashboardDesc = "";
        String folderIds = "";
        String dbrdId = "";
        String mapEnabled;

        map = (HashMap) session.getAttribute("PROGENTABLES");
        dbrdId = request.getParameter("dashboardId");
        container = (Container) map.get(dbrdId);
        HashMap parametersMap = null;
        dashboardMap = container.getDashboardHashMap();
        dashboardName = String.valueOf(dashboardMap.get("DbrdName"));
        dashboardDesc = String.valueOf(dashboardMap.get("DbrdDesc"));
        parametersMap = container.getParametersHashMap();
        folderIds = String.valueOf(parametersMap.get("UserFolderIds"));
        mapEnabled = container.isMapEnabled() ? "Y" : "N";
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();


        if (folderIds == null || "null".equalsIgnoreCase(folderIds)) {
            folderIds = collect.reportBizRoles[0];
        }

        String edit = request.getParameter("edit");
        if (edit.equals("true")) {

            PbDb pbdb = new PbDb();

            String dashNameQry = "select REPORT_NAME,REPORT_DESC from PRG_AR_REPORT_MASTER where REPORT_ID=" + dbrdId;
            // //.println("dashnameqry-in ---" + dashNameQry);
            PbReturnObject dashNameObj = pbdb.execSelectSQL(dashNameQry);

            if (dashNameObj.getRowCount() > 0) {
                dashboardName = dashNameObj.getFieldValueString(0, 0);
                dashboardDesc = dashNameObj.getFieldValueString(0, 1);
            }

            DashboardTemplateViewerAction DashbrdAction = new DashboardTemplateViewerAction();
//            DashbrdAction.createDashboard(request, dbrdId, null, null);
            DashbrdAction.deleteDatabaseData(dbrdId);

            DashboardTemplateDAO dbrdTemplateDAO = new DashboardTemplateDAO();
            String finalBuildQuery = "";
            if (SqlServer) {
                finalBuildQuery = "select ident_current('PRG_AR_REPORT_MASTER') as NEXTVAL ";
                dbrdId = dbrdTemplateDAO.getCustomDbrdId(finalBuildQuery);
                dbrdId = String.valueOf((Integer.parseInt(dbrdId)) + 1);
            } else if (MySql) {
                finalBuildQuery = "select LAST_INSERT_ID(REPORT_ID) from PRG_AR_REPORT_MASTER order by 1 desc limit 1";
                dbrdId = dbrdTemplateDAO.getCustomDbrdId(finalBuildQuery);
                dbrdId = String.valueOf((Integer.parseInt(dbrdId)) + 1);
            } else {
                finalBuildQuery = "select PRG_AR_REPORT_MASTER_SEQ.nextval from dual";
                dbrdId = dbrdTemplateDAO.getCustomDbrdId(finalBuildQuery);
            }
        }

        new DashboardTemplateDAO().insertDashboard(Integer.parseInt(dbrdId), dashboardName, dashboardDesc, mapEnabled);
        paramdDAO.insertDashboardDetails(Integer.parseInt(dbrdId), folderIds);
        ArrayList params = (ArrayList) parametersMap.get("Parameters");

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
                //////////.println("NewDashIdsArray[i]=" + NewDashIdsArray[i]);

            }
        }




//         String paramsString = "";
        StringBuilder paramsString = new StringBuilder();
//        String timeString = "";
        /*
         * for (int i = 0; i < timeParams.size(); i++) {
         * timeString=timeString+"," +timeParams.get(i); }
         * timeString=timeString.substring(1);
         *
         */
        for (int i = 0; i < params.size(); i++) {
            paramsString.append(",").append(params.get(i));
//            paramsString = paramsString + "," + params.get(i);
        }
//        if(!paramsString.equalsIgnoreCase(""))
//        paramsString = paramsString.substring(1);
        if (paramsString.length() > 0) {
            paramsString = new StringBuilder(paramsString.substring(1));
        }
//        String paramString1 = "";
        StringBuilder paramString1 = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            paramString1.append(",").append(params.get(i)).append(",").append((i + 1));
//            paramString1 = paramString1 + "," + params.get(i) + "," + (i + 1);
        }
//        if(!paramString1.equalsIgnoreCase(""))
//        paramString1 = paramString1.substring(1);
        if (paramString1.length() > 0) {
            paramString1 = new StringBuilder(paramString1.substring(1));
        }

        PbReturnObject pbro = paramdDAO.getParameters(paramsString.toString(), paramString1.toString());

        ArrayList finalQueries = new ArrayList();
        if (pbro != null && pbro.getRowCount() > 0) {
            for (int i = 0; i < pbro.getRowCount(); i++) {
                String element_id = pbro.getFieldValueString(i, 0);
                String disp_name = pbro.getFieldValueString(i, 1);
                String dim_id = pbro.getFieldValueString(i, 2);
                String dim_tab_id = pbro.getFieldValueString(i, 3);
                String buss_table = pbro.getFieldValueString(i, 4);

                String query = "";
                if (SqlServer || MySql) {
                    query = "insert into PRG_AR_REPORT_PARAM_DETAILS (REPORT_ID,PARAM_DISP_NAME,ELEMENT_ID,DIM_ID,DIM_TAB_ID,BUSS_TABLE,DEFAULT_VALUE) values (" + dbrdId + ",'" + disp_name + "'," + element_id + "," + dim_id + "," + dim_tab_id + "," + buss_table + ",'All')";
                    //////.println("PRG_AR_REPORT_PARAM_DETAILS="+query);
                } else {
                    query = "insert into PRG_AR_REPORT_PARAM_DETAILS (PARAM_DISP_ID,REPORT_ID,PARAM_DISP_NAME,ELEMENT_ID,DIM_ID,DIM_TAB_ID,BUSS_TABLE,DEFAULT_VALUE) values (PRG_AR_REPORT_PARAM_DETLS_SEQ.nextval," + dbrdId + ",'" + disp_name + "'," + element_id + "," + dim_id + "," + dim_tab_id + "," + buss_table + ",'All')";
                }


                finalQueries.add(query);
            }
        }

        paramdDAO.insertReportParamDetails(finalQueries);

        /*
         * End of code To Insert Param Details
         */

        ArrayList view_elements = new ArrayList();
        String rowViewBy = collect.reportRowViewbyValues.get(0);
        if (params != null) {
            String viewElmntQuery = "";
            if (SqlServer || MySql) {
                viewElmntQuery = "insert into PRG_AR_REPORT_VIEW_BY_MASTER (REPORT_ID,VIEW_BY_SEQ,IS_ROW_EDGE,ROW_SEQ,COL_SEQ,DEFAULT_VALUE) values (" + dbrdId + ",1,'Y',1,-1," + rowViewBy + ")";
                //////.println("PRG_AR_REPORT_VIEW_BY_MASTER="+viewElmntQuery);
            } else {
                if (!rowViewBy.equalsIgnoreCase("")) {
                    viewElmntQuery = "insert into PRG_AR_REPORT_VIEW_BY_MASTER (VIEW_BY_ID,REPORT_ID,VIEW_BY_SEQ,IS_ROW_EDGE,ROW_SEQ,COL_SEQ,DEFAULT_VALUE) values (PRG_AR_REP_VIEW_BY_MASTER_SEQ.nextval," + dbrdId + ",1,'Y',1,-1," + rowViewBy + ")";
                } else {
                    viewElmntQuery = "insert into PRG_AR_REPORT_VIEW_BY_MASTER (VIEW_BY_ID,REPORT_ID,VIEW_BY_SEQ,IS_ROW_EDGE,ROW_SEQ,COL_SEQ,DEFAULT_VALUE) values (PRG_AR_REP_VIEW_BY_MASTER_SEQ.nextval," + dbrdId + ",1,'Y',1,-1,'Time')";
                }
            }



            PbReturnObject paramPbro = paramdDAO.getParamDetails(Integer.parseInt(dbrdId));
            view_elements.add(viewElmntQuery);

            for (int i = 0; i < paramPbro.getRowCount(); i++) {
                String param_disp_id = paramPbro.getFieldValueString(i, 0);
                String disp_seq_no = paramPbro.getFieldValueString(i, 1);
                String viewbyQuery = "";

                if (SqlServer) {
                    viewbyQuery = "insert into PRG_AR_REPORT_VIEW_BY_DETAILS (PARAM_DISP_ID,VIEW_BY_ID,DISP_SEQ_NO) values (" + param_disp_id + ",ident_current('PRG_AR_REPORT_VIEW_BY_MASTER')," + i + ")";
                } else if (MySql) {
                    viewbyQuery = "insert into PRG_AR_REPORT_VIEW_BY_DETAILS (PARAM_DISP_ID,VIEW_BY_ID,DISP_SEQ_NO) values (" + param_disp_id + ",(select LAST_INSERT_ID(VIEW_BY_ID) from PRG_AR_REPORT_VIEW_BY_MASTER order by 1 desc limit 1)," + i + ")";
                } else {
                    viewbyQuery = "insert into PRG_AR_REPORT_VIEW_BY_DETAILS (VIEW_BY_DTL_ID,PARAM_DISP_ID,VIEW_BY_ID,DISP_SEQ_NO) values (PRG_AR_REP_VIEW_BY_DETLS_SEQ.nextval," + param_disp_id + ",PRG_AR_REP_VIEW_BY_MASTER_SEQ.currval," + i + ")";
                }

                view_elements.add(viewbyQuery);
            }
        }

        //insert into Time tables
        parametersMap.put("TimeDetailstList", collect.timeDetailsArray);
        HashMap timeDetailsHashmap = (HashMap) parametersMap.get("TimeDimHashMap");
        ArrayList timeDetails = (ArrayList) parametersMap.get("TimeDetailstList");
        ArrayList timeParams = (ArrayList) parametersMap.get("timeParameters");

        if (timeParams == null) {
            timeParams = new ArrayList();
            String[] BuildedParamsWithTime = null;

            if (timeDetailsHashmap != null) {
                BuildedParamsWithTime = (String[]) timeDetailsHashmap.keySet().toArray(new String[0]); //contains 31150,31149,31156,31153
                for (int i = 0; i < BuildedParamsWithTime.length; i++) {
                    timeParams.add(BuildedParamsWithTime[i]);
                }
            }
            parametersMap.put("timeParameters", timeParams);
        }

        String timeQry = "";

        if (SqlServer || MySql) {
            timeQry = "insert into PRG_AR_REPORT_TIME (TIME_TYPE, TIME_LEVEL, REPORT_ID) values"
                    + "('" + timeDetails.get(1) + "','" + timeDetails.get(0) + "'," + dbrdId + ")";
            //////.println("PRG_AR_REPORT_TIME="+timeQry);
        } else {
            timeQry = "insert into PRG_AR_REPORT_TIME (REP_TIME_ID, TIME_TYPE, TIME_LEVEL, REPORT_ID) values"
                    + "(PRG_AR_REPORT_TIME_SEQ.nextval,'" + timeDetails.get(1) + "','" + timeDetails.get(0) + "'," + dbrdId + ")";
        }
        view_elements.add(timeQry);
        for (int time = 0; time < timeParams.size(); time++) {
            String[] keys = (String[]) timeDetailsHashmap.keySet().toArray(new String[0]);
            //ArrayList
            for (int i = 0; i < keys.length; i++) {
                ArrayList details = (ArrayList) timeDetailsHashmap.get(keys[i]);
                if (timeParams.get(time).toString().equalsIgnoreCase(details.get(6).toString())) {
                    String timeDetailsQry = "";
                    if (SqlServer) {
                        if (timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE") || timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE1") || timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE2") || timeParams.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE1") || timeParams.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                            timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE,DEFAULT_DATE,DEFAULT_VALUE) "
                                    + "values (ident_current('PRG_AR_REPORT_TIME'),'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ",convert(datetime,'" + ((String) details.get(0)) + "',101),null)";
                        } else {
                            timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE) "
                                    + "values (ident_current('PRG_AR_REPORT_TIME'),'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ")";
                            //////.println("PRG_AR_REPORT_TIME_DETAIL="+timeDetailsQry);
                        }
                    } else if (MySql) {
                        if (timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE") || timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE1") || timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE2") || timeParams.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE1") || timeParams.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                            timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE,DEFAULT_DATE,DEFAULT_VALUE) "
                                    + "values ((Select cast(LAST_INSERT_ID(rep_time_id) as decimal)from PRG_AR_REPORT_TIME order by 1 desc limit 1),'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ",str_to_date('" + ((String) details.get(0)) + "','%m/%d/%Y'),null)";
                        } else {
                            timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE) "
                                    + "values ((Select cast(LAST_INSERT_ID(rep_time_id) as decimal)from PRG_AR_REPORT_TIME order by 1 desc limit 1),'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ")";
                            //////.println("PRG_AR_REPORT_TIME_DETAIL="+timeDetailsQry);
                        }
                    } else {
                        if (timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE") || timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE1") || timeParams.get(time).toString().equalsIgnoreCase("AS_OF_DATE2") || timeParams.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE1") || timeParams.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                            timeDetailsQry = "insert into PRG_AR_REPORT_TIME_DETAIL (REP_TIME_DTL_ID, REP_TIME_ID, COLUMN_NAME, COLUMN_TYPE, SEQUENCE, FORM_SEQUENCE,DEFAULT_DATE,DEFAULT_VALUE) "
                                    + "values (PRG_AR_REPORT_TIME_DETAIL_SEQ.nextval,PRG_AR_REPORT_TIME_SEQ.currval,'" + details.get(2) + "','" + details.get(6) + "'," + details.get(3) + "," + details.get(4) + ",to_date('" + ((String) details.get(0)) + "','mm/dd/yyyy'),null)";
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

        //Inserting KPI Details into the respective KPI tables
        DashboardTemplateViewerAction action = new DashboardTemplateViewerAction();
        for (int i = 0; i < dashletList.size(); i++) {
            DashletDetail dashlet = dashletList.get(i);
            String reportType = dashlet.getDisplayType();
            int row = dashlet.getRow();
            int col = dashlet.getCol();
            int rowSpan = dashlet.getRowSpan();
            int colSpan = dashlet.getColSpan();
            if (DashboardConstants.KPI_REPORT.equalsIgnoreCase(reportType)) {
                List<String> kpiQueries = action.getKPIInsertQueries(dbrdId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan, container.ViewBy);
                view_elements.addAll(kpiQueries);
            } else if (DashboardConstants.TABLE_REPORT.equalsIgnoreCase(reportType)) {
                List<String> graphQueries = action.getGraphInsertQueries(dbrdId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan);
                view_elements.addAll(graphQueries);
            } else if (DashboardConstants.GRAPH_REPORT.equalsIgnoreCase(reportType)
                    || DashboardConstants.DASHBOARD_GRAPH_REPORT.equalsIgnoreCase(reportType)) {
                dashlet.setRowViewBy(rowViewBy);
                List<String> graphQueries = action.getGraphInsertQueries(dbrdId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan);
                view_elements.addAll(graphQueries);
            } else if (DashboardConstants.KPI_GRAPH_REPORT.equalsIgnoreCase(reportType)) {
                List<String> queries = action.getKPIGraphInsertQueries(dbrdId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan);
                view_elements.addAll(queries);
            } else if (DashboardConstants.MAP_REPORT.equalsIgnoreCase(reportType)) {
                List<String> queries = action.getMapInsertQueries(dbrdId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan);
                view_elements.addAll(queries);
            } else if (DashboardConstants.TEXTKPI_REPORT.equalsIgnoreCase(reportType)) {
                List<String> graphQueries = action.getTextKPIInsertQueries(dbrdId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan, collect.getTextKPIRowViewBy());
                view_elements.addAll(graphQueries);
            } else if (DashboardConstants.GROUP_MEASSURE_INSIGHTS.equalsIgnoreCase(reportType)) {
                GroupMeassureParams grpParams = (GroupMeassureParams) session.getAttribute("GroupParamsMap");
                ArrayList rootParentsMap = grpParams.getGroupMap();
                List<String> graphQueries = action.getGroupMeassureInsertQueries(dbrdId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan, rootParentsMap);
                view_elements.addAll(graphQueries);

            } else if (DashboardConstants.KPI_WITH_GRAPH.equalsIgnoreCase(reportType)) {
                dashlet.setRowViewBy(rowViewBy);
                List<String> graphQueries = action.getGraphInsertQueries(dbrdId, dashboardName, i + 1, dashlet, SqlServer, MySql, row, col, rowSpan, colSpan);
                view_elements.addAll(graphQueries);

            }

        }


        paramdDAO.insertKpiGraphs(view_elements);
        map.remove(dbrdId);
        request.setAttribute("REPORTID", dbrdId);
        request.setAttribute("ReportType", "D");
        request.setAttribute("UserFolderIds", String.valueOf(parametersMap.get("UserFolderIds")));
        request.setAttribute("ReportName", dashboardName);
        ServletContext context = this.getServlet().getServletContext();
        boolean isCompanyValid = Boolean.parseBoolean(context.getInitParameter("isCompanyValid"));

        if (isCompanyValid) {
            return mapping.findForward("dashboardAssignmentWithCompany");
        } else {
            return mapping.findForward("dashboardAssignment");
        }
    }

    public ActionForward goToDashboardDesigner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap ParametersHashMap = null;
        HashMap DashboardHashMap = null;
        String dashboardName = null;
        String dashboardDesc = null;
        HashMap map = null;
        String dbrdId = null;
        Container container = null;
        PbReturnObject types = null;
        PbReturnObject sizes = null;
        String PbUserId = "";
        String iskpidashboard = "";
        String url = request.getRequestURL().toString();
        String userfldrs = "";

        if (session != null) {
            try {
                dashboardName = request.getParameter("dashboardName");
                //dashboardName=dashboardName.replace('^', '&').replace('~','+').replace('`', '#').replace('_','%');
                dashboardDesc = request.getParameter("dashboardDesc");
                iskpidashboard = request.getParameter("iskpidashboard");
                // dashboardDesc=dashboardDesc.replace('^', '&').replace('~','+').replace('`', '#').replace('_','%');
                PbUserId = String.valueOf(session.getAttribute("USERID"));

                url = url + "?templateParam2=goToDashboardDesigner&dashboardName=" + dashboardName + "&dashboardDesc=" + dashboardDesc;
                request.setAttribute("dashdesignerurl", url);
                DashboardTemplateDAO dbrdTemplateDAO = new DashboardTemplateDAO();
                String finalBuildQuery = "";
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    //finalBuildQuery="select  ident_current ('PRG_AR_REPORT_MASTER') as NEXTVAL ";
                    finalBuildQuery = "select ident_current('PRG_AR_REPORT_MASTER') as NEXTVAL ";
                    dbrdId = dbrdTemplateDAO.getCustomDbrdId(finalBuildQuery);
                    dbrdId = String.valueOf((Integer.parseInt(dbrdId)) + 1);
                } // by gopesh for dbId in mysql
                else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    finalBuildQuery = "select LAST_INSERT_ID(REPORT_ID) from PRG_AR_REPORT_MASTER order by 1 desc limit 1 ";
                    dbrdId = dbrdTemplateDAO.getCustomDbrdId(finalBuildQuery);
                    dbrdId = String.valueOf((Integer.parseInt(dbrdId)) + 1);
                } else {
                    finalBuildQuery = "select PRG_AR_REPORT_MASTER_SEQ.nextval from dual";
                    dbrdId = dbrdTemplateDAO.getCustomDbrdId(finalBuildQuery);
                }

                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(dbrdId) != null) {
                        container = (Container) map.get(dbrdId);
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

                container.setDashboardId(dbrdId);
                container.setTableId(dbrdId);

                ParametersHashMap = container.getParametersHashMap();
                DashboardHashMap = container.getDashboardHashMap();

                if (ParametersHashMap == null) {
                    ParametersHashMap = new HashMap();
                }

                if (DashboardHashMap == null) {
                    DashboardHashMap = new HashMap();
                }
                DashboardHashMap.put("DbrdName", dashboardName);
                DashboardHashMap.put("DbrdDesc", dashboardDesc);
                String result = dbrdTemplateDAO.getUserFoldersByUserId(PbUserId);
                userfldrs = result;
                //
                request.setAttribute("DashboardId", dbrdId);
                request.setAttribute("UserFlds", result);

                container.setDbrdName(dashboardName);
                container.setDbrdDesc(dashboardDesc);
                container.setDashboardHashMap(DashboardHashMap);
                container.setSessionContext(session, container);
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }
            if (iskpidashboard != null && iskpidashboard != "" && iskpidashboard.equalsIgnoreCase("true")) {
                response.getWriter().print(userfldrs);
                return null;
            } else {
                return mapping.findForward(SUCCESS);
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward checkDashboardName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String dashboardName = request.getParameter("dashboardName");
        String status = "";
        DashboardTemplateDAO dtdao = new DashboardTemplateDAO();
        PbReturnObject pbro = dtdao.checkDashboardName();

        HttpSession session = request.getSession(false);
        if (session != null) {
            for (int i = 0; i < pbro.getRowCount(); i++) {
                if (pbro.getFieldValueString(i, 0).equalsIgnoreCase(dashboardName)) {
                    status = "This Dashboard Name Already Exists";
                    break;
                }
            }
            response.getWriter().print(status);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getTemplateDivs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String path = this.getServlet().getServletContext().getRealPath("/").replace("\\.\\", "\\").split("build")[0];

        PrintWriter out = response.getWriter();
        DashboardTemplateBD bd = new DashboardTemplateBD();
        bd.setServletRequest(request);
        String output = bd.getThumbNail(request, response, path);

        out.print(output);
        return null;
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

                //Element deviationValue = new Element("Deviation");
                //deviationValue.setText(String.valueOf(finalDev));
                //Target.addContent(deviationValue);

                root.addContent(Target);
                //root.addContent(deviationValue);
                document = new Document(root);
                serializer = new XMLOutputter();
                ////////

                HashMap ParametersHashMap = container.getParametersHashMap();
                ArrayList TimeDetailsList = (ArrayList) ParametersHashMap.get("TimeDetailstList");
                PbReturnObject retObj = getKpiGrpValue(kpiIds, session, customReportId);
                String result = String.valueOf(retObj.getFieldValueInt(0, 1));
                //////////////////////////////.println("timedim in action is: "+timeDim);
                //////////////////////////////.println("daytarget in action is: "+dayTarget);
                //////////////////////////////.println("daydiff in action is: "+dayDiff);
                if (timeDim.equalsIgnoreCase("PRG_DATE_RANGE")) {
                    if (!dayTarget.equals("0")) {
                        double actualValperDay = Double.parseDouble(result) / Double.parseDouble(dayDiff);
                        finalDev = templateDAO.getDeviation(String.valueOf(actualValperDay), dayTarget);
                        //////////////////////////////.println("actulavalperday in action is: "+actualValperDay);
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

                        out.print(finalDev + "~" + serializer.outputString(document));
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

//    public float getDeviation(String actulaValue, String targetValue) {
//
//        Double deviation = (((double) ((Double.parseDouble(actulaValue) - Double.parseDouble(targetValue))) / Double.parseDouble(targetValue)) * 100);
//
//        double tempDev = deviation;
//
//        deviation = Math.abs(deviation);
//
//        double d = tempDev;//deviation[i];
//        float p = (float) Math.pow(10, 2);
//        double Rval2 = 0;
//        Rval2 = d * p;
//        float tmp = Math.round(Rval2);
//        float finalDev = tmp / p;
//
//        return finalDev;
//    }
    public ActionForward buildDbrdGraphs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PrintWriter out = response.getWriter();
        ServletContext context = this.getServlet().getServletContext();
        HttpSession session = request.getSession(false);
        boolean isFxCharts = Boolean.parseBoolean(context.getInitParameter("isFxCharts"));
        String displayType = "";
        String grptype = request.getParameter("grpType");
        StringBuilder graphsBuffer = new StringBuilder();
        if (session != null) {

            DashboardTemplateBD dashboardTempBD = new DashboardTemplateBD();
            dashboardTempBD.setServletRequest(request);
            if (request.getParameter("displayType") != null) {
                displayType = request.getParameter("displayType");
                dashboardTempBD.setDisplayType(displayType);
            }
            if (grptype.equalsIgnoreCase("groupMeassure")) {
                graphsBuffer = dashboardTempBD.buildGroupMeassure(request, response, isFxCharts);
            } else {
                graphsBuffer = dashboardTempBD.buildDbrdGraphs(request, response, isFxCharts);

            }

            out.print(graphsBuffer);
        } else {
            out.print("Session Expired...Please logout and  login");
        }
        return null;
    }

    public ActionForward displayKpiGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PrintWriter out = response.getWriter();
        ServletContext context = this.getServlet().getServletContext();
        HttpSession session = request.getSession(false);
        boolean isFxCharts = Boolean.parseBoolean(context.getInitParameter("isFxCharts"));
        HashMap singleRecord = new HashMap();
        ArrayList xmlDetails = new ArrayList();
        PbDashboardViewerBD DashboardViewerBD = new PbDashboardViewerBD();
        StringBuffer SBFxKPI = new StringBuffer("");
        if (session != null) {
            String hr1 = request.getParameter("hr1");
            String hr2 = request.getParameter("hr2");
            String mr1 = request.getParameter("mr1");
            String mr2 = request.getParameter("mr2");
            String lr1 = request.getParameter("lr1");
            String lr2 = request.getParameter("lr2");
            boolean fromEditTarget = false;
            fromEditTarget = Boolean.parseBoolean(request.getParameter("fromEditTarget"));

            String hrVal = request.getParameter("hrVal");
            String mrVal = request.getParameter("mrVal");
            String lrVal = request.getParameter("lrVal");


            String kpigrpType = request.getParameter("kpigrpType");
            String kpis = request.getParameter("kpis");
            String divId = request.getParameter("divId");
            String kpiIds = request.getParameter("kpiIds");
            String needleValue = request.getParameter("needleVal");
            String dbrdId = request.getParameter("dashboardId");
            String kpiTgtXml = String.valueOf(session.getAttribute("kpiXml"));
            HashMap kpiGraphs = null;
            HashMap map = new HashMap();
            Container container = null;
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(dbrdId);

            // creating dashlet for KPI Graph
            pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
            DashletDetail detail = collect.getDashletDetail(divId);
            KPIGraph kpiGrph = null;
            if (detail.getReportDetails() == null) {
                detail.setRefReportId(collect.reportId);
                detail.setDisplayType(DashboardConstants.KPI_GRAPH_REPORT);
                detail.setDashletName(collect.reportName);
                detail.setKpiType(null);
                detail.setKpiMasterId("0");
                kpiGrph = new KPIGraph();
                detail.setReportDetails(kpiGrph);
                // collect.addDashletDetail(detail);

                int graphWidth = 320;
                int rowSpan = detail.getRowSpan();
                int colSpan = detail.getColSpan();
                GraphBuilder grpBuilder = new GraphBuilder();
                int totalCols = grpBuilder.getTotalColumns(collect, divId);
                graphWidth = grpBuilder.getGraphWidth(detail.getRowSpan(), detail.getColSpan(), totalCols);
                int graphHeight = 250;
                graphHeight = graphHeight * rowSpan;
                graphWidth = graphWidth * colSpan;
                kpiGrph.setGraphHeight(graphHeight);
                kpiGrph.setGraphWidth(graphWidth);

                double needle = Double.parseDouble(needleValue);//(double) retObj.getFieldValueInt(0, 1);
                kpiGrph.setNeedle(needle);
                kpiGrph.setGraphXML(kpiTgtXml);
                kpiGrph.setKpiGraphType(kpigrpType);

                List<String> ids = new ArrayList<String>();
                ids.add(kpiIds);
                DashboardViewerDAO dao = new DashboardViewerDAO();
                List<KPIElement> kpiElems = dao.getKPIElements(ids, new HashMap<String, String>());
                kpiGrph.setKpiElement(kpiElems.get(0));
            } else {
                kpiGrph = (KPIGraph) detail.getReportDetails();
            }

            KPIColorRange kpicolorObj = new KPIColorRange();
            kpicolorObj.setColor("high");
            kpicolorObj.setOperator(hrVal);
            kpicolorObj.setRangeStartValue(Double.parseDouble(hr1));
            kpicolorObj.setRangeEndValue(Double.parseDouble(hr2));

            kpiGrph.addkpiGrphColorRange("high", kpicolorObj);

            KPIColorRange kpicolorObj1 = new KPIColorRange();
            kpicolorObj1.setColor("medium");
            kpicolorObj1.setOperator(mrVal);
            kpicolorObj1.setRangeStartValue(Double.parseDouble(mr1));
            kpicolorObj1.setRangeEndValue(Double.parseDouble(mr2));

            kpiGrph.addkpiGrphColorRange("medium", kpicolorObj1);

            KPIColorRange kpicolorObj2 = new KPIColorRange();
            kpicolorObj2.setColor("low");
            kpicolorObj2.setOperator(lrVal);
            kpicolorObj2.setRangeStartValue(Double.parseDouble(lr1));
            kpicolorObj2.setRangeEndValue(Double.parseDouble(lr2));

            kpiGrph.addkpiGrphColorRange("low", kpicolorObj2);
            kpiGrph.setGraphXML(kpiTgtXml);
            kpiGrph.initializeRanges();

            PbDashboardViewerBD bd = new PbDashboardViewerBD();
            PbReturnObject retObj = bd.getDashboardKPIData(container, collect, dbrdId);
            container.setKpiRetObj(retObj);
            /*
             * if (isFxCharts) { SBFxKPI.append("<iframe frameborder='0'
             * scrolling='No'
             * NAME='iframe_").append(divId).append("_").append(dbrdId).append("'
             * id='iframe_").append(divId).append("_").append(dbrdId).append("'
             * "); SBFxKPI.append("
             * STYLE='width:100%;height:100%;overflow:auto;' ");
             * SBFxKPI.append("
             * src='").append(request.getContextPath()).append("/PbFxDashBoardKPIGraph.jsp?&startrange=").append(sRange).append("&endrange=").append(eRange).append("&firstbreak=").append(fBreak).append("&secondbreak=").append(sBreak).append("&needlevalue=").append(needle).append("&graphHeight=").append(graphHeight).append("&graphWidth=").append(graphWidth).append("&kpigrpType=").append(kpigrpType).append("&dashBoardId=").append(dbrdId).append("'>");
             * SBFxKPI.append(" </iframe>"); kpiGraphs = (container.getKpiGrps()
             * == null) ? new HashMap() : container.getKpiGrps(); if
             * (kpigrpType.equals("Meter")) { kpiGraphs.put(divId, "KPIGraph," +
             * C + "," + E + "," + A + "," + B + "," + needle + "," + kpis + ","
             * + kpiIds + "," + "meter," + kpiTgtXml);
             *
             * } else { kpiGraphs.put(divId, "KPIGraph," + sRange + "," + eRange
             * + "," + fBreak + "," + sBreak + "," + needle + "," + kpis + "," +
             * kpiIds + "," + "therm," + kpiTgtXml);
             *
             * }
             * container.setKpiGrps(kpiGraphs); out.print(SBFxKPI.toString()); }
             * else {
             *
             * ProgenChartDisplay pchart = new ProgenChartDisplay(400, 250);
             * pchart.setColorchange(colorchange);
             * pchart.setCtxPath(request.getContextPath()); kpiGraphs =
             * (container.getKpiGrps() == null) ? new HashMap() :
             * container.getKpiGrps(); if (kpigrpType.equals("Meter")) {
             * //kpiGraphs.put(divId, "KPIGraph," + sRange + "," + eRange + ","
             * + fBreak + "," + sBreak + "," + needle + "," + kpis + "," +
             * kpiIds + "," + "meter," + kpiTgtXml);
             *
             * kpiGraphs.put(divId, "KPIGraph," + C + "," + E + "," + A + "," +
             * B + "," + needle + "," + kpis + "," + kpiIds + "," + "meter," +
             * kpiTgtXml); pchart.GetMeterChart(sRange, eRange, fBreak, sBreak,
             * needle, "", session, response, out);
             *
             * } else { kpiGraphs.put(divId, "KPIGraph," + sRange + "," + eRange
             * + "," + fBreak + "," + sBreak + "," + needle + "," + kpis + "," +
             * kpiIds + "," + "therm," + kpiTgtXml);
             * pchart.GetThermChart(sRange, eRange, fBreak, sBreak, needle,
             * session, response, out); } container.setKpiGrps(kpiGraphs);
             * out.print(pchart.chartDisplay);
            }
             */
            out.println("\n\nsuccess\n\n");
        } else {
            out.print("Session Expired...Please logout and login");
        }
        return null;
    }
    //end of methods used for building dashboard graphs

    //added by k
    public ActionForward resetParameters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        //added by k
        HttpSession session = request.getSession(false);
        HashMap map = null;
        Container container = null;
        String customDbrdId = "";


        ArrayList Parameters = new ArrayList();
        ArrayList ParameterNames = new ArrayList();
        String[] parameterIds = null;

        if (session != null) {

            String paramIds = request.getParameter("params");//added by k
            map = (HashMap) session.getAttribute("PROGENTABLES");
            customDbrdId = request.getParameter("dashboardId");
            container = (Container) map.get(customDbrdId);


            //added by k
            if (!paramIds.equalsIgnoreCase("")) {


                parameterIds = paramIds.split(",");

                for (int paramCount = 0; paramCount < parameterIds.length; paramCount++) {
                    Parameters.add(parameterIds[paramCount]);

                }


            }

            ArrayList newparamNames = new ArrayList();
            HashMap hmap = container.getParameterIds();
            if (parameterIds != null) {
                for (int i = 0; i < parameterIds.length; i++) {
                    String[] parameterKeySet = (String[]) hmap.keySet().toArray(new String[0]);
                    for (int j = 0; j < hmap.size(); j++) {
                        if (parameterIds[i].equals(parameterKeySet[j])) {
                            newparamNames.add(hmap.get(parameterKeySet[j]));
                        }
                    }
                }
            }

            ////////////.println("newparamNames="+newparamNames);



            HashMap ParamHashMap = container.getParametersHashMap();

            ////////////.println("before");
            ////////////.println("ParamHashMap.get(Parameters)"+ParamHashMap.get("Parameters"));
            ////////////.println("ParamHashMap.get(ParameterNames)"+ParamHashMap.get("ParameterNames"));



            ParamHashMap.remove("Parameters");
            ParamHashMap.remove("ParameterNames");


            ParamHashMap.put("Parameters", Parameters);
            ParamHashMap.put("ParameterNames", newparamNames);

            container.setParametersHashMap(ParamHashMap);
            ////////////.println("after");


            HashMap ParamHashMap1 = container.getParametersHashMap();
            //ParamHashMap.remove("Parameters");
            //ParamHashMap.remove("ParameterNames");
            ////////////.println("after");
            ////////////.println("ParamHashMap.get(Parameters)"+ParamHashMap1.get("Parameters"));
            ////////////.println("ParamHashMap.get(ParameterNames)"+ParamHashMap1.get("ParameterNames"));


            //////////////.println("in disp paams");
            //////////////.println("Parameters=" + Parameters);
            //////////////.println("ParameterNames=" + ParameterNames);


        }
        return null;
    }

    public ActionForward EditDashBdName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String dashId = null;
        String editDashbdName = null;
        String dashbdname = null;
        String dashbddesc = null;
        DashboardTemplateBD dashbdTempBD = new DashboardTemplateBD();
        if (session != null) {
            if (request.getParameter("editDashbdName") != null) {
                dashId = request.getParameter("dashId");
                editDashbdName = request.getParameter("editDashbdName");
                dashbdname = request.getParameter("dashboardName");
                dashbddesc = request.getParameter("dashboardDesc");
                dashbdTempBD.editDashbdName(dashId, dashbdname, dashbddesc);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward chckDashbdNameBfrUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PrintWriter out = response.getWriter();
        String dashDId = request.getParameter("dashdID");
        String gvnDashdNm = request.getParameter("gvnDashdNm");
        String gvnDashDesc = request.getParameter("gvnDashdDesc");
        DashboardTemplateBD dashbdTempBD = new DashboardTemplateBD();
        String status = dashbdTempBD.chckDashbdNameBfrUpdate(dashDId, gvnDashdNm, gvnDashDesc);
        out.println(status);
        return null;
    }

    public ActionForward ChangeDashDName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HashMap map = new HashMap();
        Container container = null;
        String dashId = request.getParameter("dashDId");
        String DashdNm = request.getParameter("DashdName");
        String DashdDesc = request.getParameter("DashDdesc");

        HttpSession session = request.getSession(false);
        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(dashId);
        HashMap DashDbdMap = container.getDashboardHashMap();
        DashDbdMap.put("DbrdName", DashdNm);
        DashDbdMap.put("DbrdDesc", DashdDesc);
        container.setDbrdName(DashdNm);
        container.setDbrdDesc(DashdDesc);
        return null;
    }

    public ActionForward restCurrDashBd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        String dashbdId = "";
        HashMap map = null;
        Container container = null;
        if (session != null) {
            dashbdId = request.getParameter("dashbdId");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(dashbdId) != null) {
                container = (Container) map.get(dashbdId);
                //////.println("container values--" + container);
                container = new Container();
            }
            out.println("1");
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getCreateKPIs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String divId = request.getParameter("divId");
        String dashboardId = request.getParameter("dashboardId");
        request.setAttribute("divId", divId);
        request.setAttribute("dashboardId", dashboardId);
        return mapping.findForward("complexKPI");
    }

    public ActionForward getCreateKPIData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DashboardTemplateDAO dbTemplateDAO = new DashboardTemplateDAO();
        String from = request.getParameter("from");
        String oneRoleId = request.getParameter("oneRoleId");

        //GenerateDragAndDrophtml dragAndDrophtml=dbTemplateDAO.getCreateKPIValues(request);
        String createKPIData = "";
        if (from != null && from.equalsIgnoreCase("oneview")) {
            createKPIData = dbTemplateDAO.getComplexKPIForOneView(oneRoleId);
        } else {
            createKPIData = dbTemplateDAO.getComplexKPI();
        }
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(createKPIData);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward buildCreateKPI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String kpis = request.getParameter("kpis");
        String[] kpisArray = kpis.substring(1).split(",");
        DashboardTemplateDAO templateDAO = new DashboardTemplateDAO();
        String CreateKPIString = templateDAO.getBuildCreateKPI(kpisArray, request, request.getParameter("dashboardId").toString());
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(CreateKPIString);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward deleteFavoParam(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DashboardTemplateDAO dashdao = new DashboardTemplateDAO();
        String favName = request.getParameter("favName");
        boolean status = false;
        String DashboardId = request.getParameter("DashboardId");
        status = dashdao.deleteFavoParam(favName, DashboardId);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(status);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward setParamhashmap(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String paramids = null;
        String paramnames = null;
        paramids = request.getParameter("paramids");
        paramnames = request.getParameter("paramnames");
        HashMap hm = new HashMap();
        ArrayList paramidarr = null;
        ArrayList paramnamearr = null;
        String[] paramstrarr = paramids.split(",");
        String[] paramnamestrarr = paramnames.split(",");
        String reportId = request.getParameter("dashboardId");
        for (int i = 0; i < paramstrarr.length; i++) {
            hm.put(paramstrarr[i], paramnamestrarr[i]);
        }
        Container container = Container.getContainerFromSession(request, reportId);
        container.setFixedparamhashmap(hm);

        return null;
    }

    public ActionForward getGroupMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String foldersIds = "";
        String divId = "";
        foldersIds = request.getParameter("foldersIds");
        divId = request.getParameter("divId");
        String dbrdId = request.getParameter("dashboardId");
        if (foldersIds == null || "".equals(foldersIds)) {
            Container container = Container.getContainerFromSession(request, dbrdId);
            PbReportCollection collect = container.getReportCollect();
            String[] ids = collect.reportBizRoles;
            if (ids != null && ids.length > 0) {
                foldersIds = ids[0];
            }
        }
        DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
        //for getting the group measures only
        String result = dashboardTemplateDAO.getGroupMeasures(foldersIds);
        //sending all the details to DashboardGroupMeasure.jsp
        request.setAttribute("groupmeasures", result);
        request.setAttribute("dbrdId", dbrdId);
        request.setAttribute("divId", divId);

        return mapping.findForward("groupMeasures");
    }

    public ActionForward groupMeasureInitialView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String meassureID = request.getParameter("meassId");
        String DashbrdId = request.getParameter("dbrdId");
        String dashletId = request.getParameter("dashletId");
        String groupId = request.getParameter("groupId");
        Container container = Container.getContainerFromSession(request, DashbrdId);
        DashboardTemplateBD dashBoardTamplateBd = new DashboardTemplateBD();
        String childTable = "";
        GroupMeassureParams meassureParams = new GroupMeassureParams();
        HttpSession session = request.getSession(false);
//                   String viewById = request.getParameter("viewByDim");
//                   String dimValue = request.getParameter("dimValue");
//                   String paramVals = request.getParameter("paramVals");
        String rowsToDisp = request.getParameter("rowsToDisp");
        String nextLevel = request.getParameter("nextLevel");
        String groupDispType = request.getParameter("groupType");
//                   String masterMesId = request.getParameter("masterMeassure");
//                   String childDimId = request.getParameter("childDimId");
        boolean isOddLevel = false;
        if ("odd".equalsIgnoreCase(nextLevel)) {
            isOddLevel = true;
        }

        meassureParams.setIsOddLevel(isOddLevel);
//                   meassureParams.setChildMeassureId(childDimId);
//                   meassureParams.setMasterMeassureId(masterMesId);
//                   meassureParams.setParameter(paramVals);
//                   meassureParams.setMeassureValue(meassureID);
        meassureParams.setMeassureId(meassureID);
        meassureParams.setDahletId(dashletId);
        meassureParams.setDbrdId(DashbrdId);
        meassureParams.setDisplayType(rowsToDisp);
        meassureParams.setGroupId(groupId);
        meassureParams.setGroupDisplayType(groupDispType);
        if (session != null) {
            childTable = dashBoardTamplateBd.groupMeasureInitialView(container, meassureParams);
            PrintWriter out = response.getWriter();
            out.print(childTable);

            return null;
        } else {
            return mapping.findForward("sessionExpired");

        }


    }

    public ActionForward reloadGroupMeassures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        GroupMeassureParams meassureParams = new GroupMeassureParams();
        PbReturnObject retObj = new PbReturnObject();
        String DashbrdId = request.getParameter("dbrdId");
        String dashletId = request.getParameter("dashletId");
        String groupId = request.getParameter("groupId");
        String rowsToDisp = request.getParameter("rowsToDisp");
        String rootElements = request.getParameter("rootElements");
        String groupDispType = request.getParameter("groupType");
        HashMap<String, ArrayList> groupMap = new HashMap<String, ArrayList>();
        meassureParams.setDahletId(dashletId);
        meassureParams.setDbrdId(DashbrdId);
        meassureParams.setDisplayType(rowsToDisp);
        meassureParams.setGroupId(groupId);
        meassureParams.setGroupDisplayType(groupDispType);
        Container container = Container.getContainerFromSession(request, DashbrdId);
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        DashletDetail dashlet = collect.getDashletDetail(dashletId);
        HttpSession session = request.getSession(false);
        String table = "";
        if (session != null) {
            String userId = session.getAttribute("USERID").toString();
            PbDashboardViewerBD bd = new PbDashboardViewerBD();
            DashboardViewerDAO dao = new DashboardViewerDAO();
            if (groupDispType.equalsIgnoreCase("Simple")) {
                bd.setUserId(userId);
                bd.setInitialGroupMeasssureReport(container, dashlet);
            }
            if (groupDispType.equalsIgnoreCase("SWP")) {
                String temp = rootElements.substring(rootElements.indexOf('[') + 1, rootElements.lastIndexOf(']'));
                String[] elems = temp.split(",");
                ArrayList rootElementsList = new ArrayList();
                ArrayList elementList = new ArrayList();

                for (int i = 0; i < elems.length; i++) {
                    rootElementsList.add(elems[i].trim());
                }
                groupMap.put(groupId, rootElementsList);
                meassureParams.setGroupMap(groupMap);
                List<KPIElement> kpiElements = dao.getKPIElementsForGroups(rootElementsList, new HashMap<String, String>());
                meassureParams.setkPIElements(kpiElements);

                bd.setGroupMeasureKPIData(container, dashlet, kpiElements, userId);
            }
            table = bd.builDbGroupMeassure(container, meassureParams);
            PrintWriter out = response.getWriter();
            out.print(table);

            return null;
        } else {
            return mapping.findForward("sessionExpired");

        }
    }

    public ActionForward getAllViewParams(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String grptype = (String) session.getAttribute("grptype");
        String selectedparams = request.getParameter("selectedMeasuresliast");
        String tdid = request.getParameter("dashid");
        String customReportId = null;
        customReportId = request.getParameter("dashboardId");
        DashboardTemplateBD dashboardTempBD = new DashboardTemplateBD();

        HashMap map = null;
        Container container = null;
        pbDashboardCollection collect = null;
        String customDbrdId = "";
        //dashboardTempBD.getViewByParam(grptype, tdid, selectedparams);
        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(customDbrdId);
        container.setTextkpiViewBy(selectedparams.replace(",", "").trim());
        collect = (pbDashboardCollection) container.getReportCollect();
        collect.setTextKPIRowViewBy(selectedparams);
        return null;
    }

    public ActionForward buildOneviewComplexKPI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String kpis = request.getParameter("kpis");
        String viewLetId = request.getParameter("divId");
        String height = request.getParameter("height");
        String width = request.getParameter("width");
        String oneviewID = request.getParameter("oneViewId");
        String[] kpisArray = kpis.substring(1).split(",");
        DashboardTemplateDAO templateDAO = new DashboardTemplateDAO();
        OneViewBD oneViewBD = new OneViewBD();
        String CreateKPIString = templateDAO.getBuildOneviewComplexKPI(kpisArray, request, request.getParameter("dashboardId").toString(), viewLetId, height, width);
        String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
        HashMap oneviewHashMap = null;
        OnceViewContainer onecontainer = new OnceViewContainer();
        oneviewHashMap = (HashMap) session.getAttribute("ONEVIEWDETAILS");
        OneViewLetDetails detail = null;
        if (oneviewHashMap != null) {
            onecontainer = (OnceViewContainer) oneviewHashMap.get(oneviewID);
            detail = new OneViewLetDetails();
            detail = onecontainer.onviewLetdetails.get(Integer.parseInt(request.getParameter("divId")));
        } else {
            String fileName = request.getSession(false).getAttribute("tempFileName").toString();
            DashboardTemplateBD dashboardTemplateBD = new DashboardTemplateBD();
            File file = new File(advHtmlFileProps + "/" + fileName);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(advHtmlFileProps + "/" + fileName);
                ObjectInputStream ois = new ObjectInputStream(fis);
                onecontainer = (OnceViewContainer) ois.readObject();
                ois.close();
                detail = new OneViewLetDetails();
                detail = onecontainer.onviewLetdetails.get(Integer.parseInt(viewLetId));
                HashMap tempRegHashMap = onecontainer.getTempRegHashMap();
                String tempRegFileName = (String) tempRegHashMap.get(Integer.parseInt(detail.getNoOfViewLets()));
                if (tempRegFileName == null) {
                    tempRegFileName = "InnerRegionDetails" + onecontainer.oneviewId + "_" + detail.getNoOfViewLets() + "_" + session.getId() + "_" + System.currentTimeMillis() + ".txt";
                    tempRegHashMap.put(Integer.parseInt(detail.getNoOfViewLets()), tempRegFileName);
                }
            }

            String result = dashboardTemplateBD.getComplexKpisData(request, response, session, detail, oneviewID, Integer.parseInt(viewLetId));
            oneViewBD.writeOneviewRegData(onecontainer, result, viewLetId, request);

            FileOutputStream fos = new FileOutputStream(advHtmlFileProps + "/" + fileName);
            ObjectOutputStream oosR = new ObjectOutputStream(fos);
            oosR.writeObject(onecontainer);
            oosR.flush();
            oosR.close();
            fos.close();
        }
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(CreateKPIString);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward goToKPIDashboardDesigner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap ParametersHashMap = null;
        HashMap DashboardHashMap = null;
        String dashboardName = null;
        String dashboardDesc = null;
        HashMap map = null;
        String dbrdId = null;
        Container container = null;
        PbReturnObject types = null;
        PbReturnObject sizes = null;
        boolean isKpiDashboard = false;
        String PbUserId = "";
        String iskpidashboard = "";
        String url = request.getRequestURL().toString();
        String userfldrs = "";
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();

        if (session != null) {
            try {

                dashboardName = request.getParameter("dashboardName");
                //dashboardName=dashboardName.replace('^', '&').replace('~','+').replace('`', '#').replace('_','%');
                dashboardDesc = request.getParameter("dashboardDesc");
                if (dashboardDesc == null) {
                    dashboardDesc = dashboardName;
                }
                iskpidashboard = request.getParameter("iskpidashboard");
                // dashboardDesc=dashboardDesc.replace('^', '&').replace('~','+').replace('`', '#').replace('_','%');
                PbUserId = String.valueOf(session.getAttribute("USERID"));

                url = url + "?templateParam2=goToDashboardDesigner&dashboardName=" + dashboardName + "&dashboardDesc=" + dashboardDesc;
                request.setAttribute("dashdesignerurl", url);
                DashboardTemplateDAO dbrdTemplateDAO = new DashboardTemplateDAO();
                String finalBuildQuery = "";
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    //finalBuildQuery="select  ident_current ('PRG_AR_REPORT_MASTER') as NEXTVAL ";
                    finalBuildQuery = "select ident_current('PRG_AR_REPORT_MASTER') as NEXTVAL ";
                    dbrdId = dbrdTemplateDAO.getCustomDbrdId(finalBuildQuery);
                    dbrdId = String.valueOf((Integer.parseInt(dbrdId)) + 1);
                } // by gopesh for dbId in mysql
                else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    finalBuildQuery = "select LAST_INSERT_ID(REPORT_ID) from PRG_AR_REPORT_MASTER order by 1 desc limit 1 ";
                    dbrdId = dbrdTemplateDAO.getCustomDbrdId(finalBuildQuery);
                    dbrdId = String.valueOf((Integer.parseInt(dbrdId)) + 1);
                } else {
                    finalBuildQuery = "select PRG_AR_REPORT_MASTER_SEQ.nextval from dual";
                    dbrdId = dbrdTemplateDAO.getCustomDbrdId(finalBuildQuery);
                }

                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(dbrdId) != null) {
                        container = (Container) map.get(dbrdId);
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

                container.setDashboardId(dbrdId);
                container.setTableId(dbrdId);
                if (iskpidashboard != null && iskpidashboard != "" && iskpidashboard.equalsIgnoreCase("true")) {
                    PbReportCollection collect = container.getReportCollect();
                    if (collect == null) {
                        collect = new PbReportCollection();
                    }
                    container.setReportCollect(collect);
                    ////.println(container.getReportId());
                    container.setReportId(dbrdId);
                    container.setTableId(dbrdId);
                    container.setNetTotalReq(true);
                    container.setGrandTotalReq(true);
                }
                ParametersHashMap = container.getParametersHashMap();
                DashboardHashMap = container.getDashboardHashMap();

                if (ParametersHashMap == null) {
                    ParametersHashMap = new HashMap();
                }

                if (DashboardHashMap == null) {
                    DashboardHashMap = new HashMap();
                }
                DashboardHashMap.put("DbrdName", dashboardName);
                DashboardHashMap.put("DbrdDesc", dashboardDesc);
                String result = "";
                result = "<table><tr><td><ul>";
                result += reportTemplateDAO.getUserFoldersByUserIdInDesigner(PbUserId);
                result += "</ul></td></tr><tr><td><input type=\"button\" value=\"Done\" onclick=\"creationOfKPIDashboard(" + dbrdId + ",'" + request.getContextPath() + "')\" class=\"navtitle-hover\" name=\"Done\"></td></tr></table>";

                userfldrs = result;

                request.setAttribute("DashboardId", dbrdId);
                request.setAttribute("UserFlds", result);

                container.setDbrdName(dashboardName);
                container.setReportName(dashboardName);
                container.setReportDesc(dashboardDesc);
                container.setDbrdDesc(dashboardDesc);
                container.setDashboardHashMap(DashboardHashMap);
                container.setSessionContext(session, container);
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }
            if (iskpidashboard != null && iskpidashboard != "" && iskpidashboard.equalsIgnoreCase("true")) {
                response.getWriter().print(userfldrs);
                return null;
            } else {
                return mapping.findForward(SUCCESS);
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward goToTimeDashboardDesigner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap ParametersHashMap = null;
        HashMap DashboardHashMap = null;
        String dashboardName = null;
        String dashboardDesc = null;
        HashMap map = null;
        String dbrdId = null;
        Container container = null;
        PbReturnObject types = null;
        PbReturnObject sizes = null;
        boolean isKpiDashboard = false;
        String PbUserId = "";
        String iskpidashboard = "";
        String url = request.getRequestURL().toString();
        String userfldrs = "";
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();

        if (session != null) {
            try {

                dashboardName = request.getParameter("dashboardName");
                //dashboardName=dashboardName.replace('^', '&').replace('~','+').replace('`', '#').replace('_','%');
                dashboardDesc = request.getParameter("dashboardDesc");
                if (dashboardDesc == null) {
                    dashboardDesc = dashboardName;
                }
                iskpidashboard = request.getParameter("iskpidashboard");
                // dashboardDesc=dashboardDesc.replace('^', '&').replace('~','+').replace('`', '#').replace('_','%');
                PbUserId = String.valueOf(session.getAttribute("USERID"));

                url = url + "?templateParam2=goToDashboardDesigner&dashboardName=" + dashboardName + "&dashboardDesc=" + dashboardDesc;
                request.setAttribute("dashdesignerurl", url);
                DashboardTemplateDAO dbrdTemplateDAO = new DashboardTemplateDAO();
                String finalBuildQuery = "";
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    //finalBuildQuery="select  ident_current ('PRG_AR_REPORT_MASTER') as NEXTVAL ";
                    finalBuildQuery = "select ident_current('PRG_AR_REPORT_MASTER') as NEXTVAL ";
                    dbrdId = dbrdTemplateDAO.getCustomDbrdId(finalBuildQuery);
                    dbrdId = String.valueOf((Integer.parseInt(dbrdId)) + 1);
                } // by gopesh for dbId in mysql
                else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    finalBuildQuery = "select LAST_INSERT_ID(REPORT_ID) from PRG_AR_REPORT_MASTER order by 1 desc limit 1 ";
                    dbrdId = dbrdTemplateDAO.getCustomDbrdId(finalBuildQuery);
                    dbrdId = String.valueOf((Integer.parseInt(dbrdId)) + 1);
                } else {
                    finalBuildQuery = "select PRG_AR_REPORT_MASTER_SEQ.nextval from dual";
                    dbrdId = dbrdTemplateDAO.getCustomDbrdId(finalBuildQuery);
                }

                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(dbrdId) != null) {
                        container = (Container) map.get(dbrdId);
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

                container.setDashboardId(dbrdId);
                container.setTableId(dbrdId);

                ParametersHashMap = container.getParametersHashMap();
                DashboardHashMap = container.getDashboardHashMap();

                if (ParametersHashMap == null) {
                    ParametersHashMap = new HashMap();
                }

                if (DashboardHashMap == null) {
                    DashboardHashMap = new HashMap();
                }
                DashboardHashMap.put("DbrdName", dashboardName);
                DashboardHashMap.put("DbrdDesc", dashboardDesc);
                String result = "";
                result = "<table><tr><td><ul>";
                result += reportTemplateDAO.getUserFoldersByUserIdInDesigner(PbUserId);
                result += "</ul></td></tr><tr><td><input type=\"button\" value=\"Done\" onclick=\"creationOfTimeBasedDashboard(" + dbrdId + ",'" + request.getContextPath() + "')\" class=\"navtitle-hover\" name=\"Done\"></td></tr></table>";

                userfldrs = result;

                request.setAttribute("DashboardId", dbrdId);
                request.setAttribute("UserFlds", result);

                container.setReportName(dashboardName);
                container.setDbrdName(dashboardName);
                container.setDbrdDesc(dashboardDesc);
                container.setDashboardHashMap(DashboardHashMap);
                container.setSessionContext(session, container);
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }
            if (iskpidashboard != null && iskpidashboard != "" && iskpidashboard.equalsIgnoreCase("true")) {
                response.getWriter().print(userfldrs);
                return null;
            } else {
                return mapping.findForward(SUCCESS);
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward selectRoleGoToKPiDashbrdDesin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String dbrdId = request.getParameter("repId");
        String roleId = request.getParameter("roleId");
        String timedash = request.getParameter("timedash");
        HashMap map = new HashMap();
        Container container = null;

        request.setAttribute("ReportId", dbrdId);
        request.setAttribute("fromDesigner", "true");
        request.setAttribute("roleid", roleId);
        PbReportViewerDAO DAO = new PbReportViewerDAO();
        String settingvalue = DAO.getCustomSetting();          //added by Mohit for Custom setting
        session.setAttribute("settingvalue", settingvalue);

        if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(dbrdId) != null) {
                    container = (Container) map.get(dbrdId);
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

            HashMap ParametersHashMap = container.getParametersHashMap();
            ParametersHashMap.put("UserFolderIds", roleId);
            container.setParametersHashMap(ParametersHashMap);
            container.setSessionContext(session, container);
            container.setIskpidasboard(true);
            if (timedash != null && timedash.equalsIgnoreCase("timedash")) {
                container.setIsTimedasboard(true);
                return mapping.findForward("newtimedashboard");
            } else {
                container.setIsTimedasboard(false);
                return mapping.findForward("newkpidashboard");
            }

        } else {

            return mapping.findForward("sessionExpired");

        }

    }

    public ActionForward selectRoleGoToTimeDashbrdDesin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {


        HttpSession session = request.getSession(false);
        String dbrdId = request.getParameter("repId");
        String roleId = request.getParameter("roleId");
        HashMap map = new HashMap();
        Container container = null;

        request.setAttribute("ReportId", dbrdId);
        request.setAttribute("fromDesigner", "true");
        request.setAttribute("roleid", roleId);
        PbReportViewerDAO DAO = new PbReportViewerDAO();
        String settingvalue = DAO.getCustomSetting();          //added by Mohit for Custom setting
        session.setAttribute("settingvalue", settingvalue);

        if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(dbrdId) != null) {
                    container = (Container) map.get(dbrdId);
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

            HashMap ParametersHashMap = container.getParametersHashMap();
            ParametersHashMap.put("UserFolderIds", roleId);
            container.setParametersHashMap(ParametersHashMap);
            container.setSessionContext(session, container);
            container.setIskpidasboard(true);
            return mapping.findForward("timeBaseddashboard");
        } else {

            return mapping.findForward("sessionExpired");

        }
    }

    public ActionForward buildDbrdParamsForKpiDashBrd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ProgenParam pParam = new ProgenParam();
        HttpSession session = request.getSession(false);

        String customReportId = null;
        HashMap map = null;
        Container container = null;
        pbDashboardCollection collect = null;
        HashMap ParametersMap;
        String customDbrdId = "";
        String foldersIds = "";
        String dimId = "";

        if (session != null) {
            String paramIds = request.getParameter("params");
            String timeparams = request.getParameter("timeparams");
            String paramNames = request.getParameter("paramArray");
            foldersIds = request.getParameter("foldersIds");
            dimId = request.getParameter("dimId");
            DashboardTemplateDAO dashboardTemplateDAO = new DashboardTemplateDAO();
            String minTimeLevel = dashboardTemplateDAO.getUserFolderMinTimeLevel(foldersIds);
            String[] parameterIds = null;
            String[] timeparameterIds = null;
            String[] parameterNames = null;
            ArrayList Parameters = new ArrayList();
            ArrayList ParameterNames = new ArrayList();
            ArrayList timeParameters = new ArrayList();

            map = (HashMap) session.getAttribute("PROGENTABLES");
            customDbrdId = request.getParameter("dashboardId");
            container = (Container) map.get(customDbrdId);
            collect = (pbDashboardCollection) container.getReportCollect();

            DashletDetail detail = new DashletDetail();
            detail.setDashBoardDetailId("0");
            detail.setCol(0);
            detail.setRow(0);
            detail.setColSpan(1);
            detail.setRowSpan(1);
            collect.addDashletDetail(detail);


            ParametersMap = container.getParametersHashMap();
            if (ParametersMap == null) {
                ParametersMap = new HashMap();
            }
            ParametersMap.put("UserFolderIds", foldersIds);

            if (paramIds != null) {
                parameterIds = paramIds.split(",");
                parameterNames = paramNames.split(",");
                for (int paramCount = 0; paramCount < parameterIds.length; paramCount++) {
                    Parameters.add(parameterIds[paramCount]);
                    ParameterNames.add(parameterNames[paramCount]);
                }
                if (parameterIds.length > 0) {
                    collect.reportRowViewbyValues = new ArrayList<String>();
                    collect.reportRowViewbyValues.add(parameterIds[0]);
                }
            }

            if (timeparams != null) {
                timeparameterIds = timeparams.split(",");
                for (int timeparamCount = 0; timeparamCount < timeparameterIds.length; timeparamCount++) {
                    timeParameters.add(timeparameterIds[timeparamCount]);
                }
            }

            HashMap hm = new HashMap();
            for (int i = 0; i < Parameters.size(); i++) {
                hm.put(Parameters.get(i), ParameterNames.get(i));
            }

            container.setParameterIds(hm);

            ParametersMap.put("Parameters", Parameters);
            ParametersMap.put("ParameterNames", ParameterNames);
            ParametersMap.put("timeParameters", timeParameters);

            Date date = new Date();
            String DATE_FORMAT = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

            HashMap timeDimMap = new HashMap();
            ArrayList timeDetails = new ArrayList();
            ArrayList dateArray = new ArrayList();
            ArrayList weekArray = new ArrayList();
            ArrayList rangeArray = new ArrayList();
            ArrayList rangeArray1 = new ArrayList();
            ArrayList rangeArray2 = new ArrayList();
            ArrayList rangeArray3 = new ArrayList();
            ArrayList asofWeekArray = new ArrayList();
            ArrayList monthArray = new ArrayList();
            ArrayList asofMonthArray = new ArrayList();
            ArrayList qtrArray = new ArrayList();
            ArrayList asofQtrArray = new ArrayList();
            ArrayList yearArray = new ArrayList();
            ArrayList asofYrArray = new ArrayList();
            ArrayList periodTypeArray = new ArrayList();
            ArrayList compareArray = new ArrayList();
            ArrayList collectTimeDetailsArray = new ArrayList();

            if (dimId.equalsIgnoreCase("Time-Period Basis") || dimId.equalsIgnoreCase("Time-Range Basis") || dimId.equalsIgnoreCase("Time-Rolling Period") || dimId.equalsIgnoreCase("Time-Month Range Basis")
                    || dimId.equalsIgnoreCase("Time-Quarter Range Basis") || dimId.equalsIgnoreCase("Time-Year Range Basis") || minTimeLevel.equals("4")
                    || (minTimeLevel.equals("3")) || (minTimeLevel.equals("2")) || (minTimeLevel.equals("1"))) {
                if (timeParameters != null) {
                    for (int time = 0; time < timeParameters.size(); time++) {
                        if (minTimeLevel.equals("5")) {
                            if (dimId.equalsIgnoreCase("Time-Period Basis")) {
                                if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DATE")) {
                                    dateArray.add(pParam.getdateforpage().toString());
                                    dateArray.add("CBO_AS_OF_DATE");
                                    dateArray.add("DATE");
                                    dateArray.add("1");
                                    dateArray.add("1");
                                    dateArray.add(null);
                                    dateArray.add("AS_OF_DATE");
                                    timeDimMap.put("AS_OF_DATE", dateArray);

                                    collectTimeDetailsArray.add("Day");
                                    collectTimeDetailsArray.add("PRG_STD");
                                    collectTimeDetailsArray.add(pParam.getdateforpage().toString());
                                    collectTimeDetailsArray.add("Month");
                                    collectTimeDetailsArray.add("Last Period");
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                                    periodTypeArray.add("MONTH");
                                    periodTypeArray.add("CBO_PRG_PERIOD_TYPE");
                                    periodTypeArray.add("AGGREGATION");
                                    periodTypeArray.add("2");
                                    periodTypeArray.add("2");
                                    periodTypeArray.add("MONTH");
                                    periodTypeArray.add("PRG_PERIOD_TYPE");
                                    timeDimMap.put("PRG_PERIOD_TYPE", periodTypeArray);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("PRG_COMPARE")) {
                                    compareArray.add("LAST PERIOD");
                                    compareArray.add("CBO_PRG_COMPARE");
                                    compareArray.add("COMPARE");
                                    compareArray.add("3");
                                    compareArray.add("1001");
                                    compareArray.add("LAST PERIOD");
                                    compareArray.add("PRG_COMPARE");
                                    timeDimMap.put("PRG_COMPARE", compareArray);
                                }
                                timeDetails.add("Day");
                                timeDetails.add("PRG_STD");
                                timeDetails.add(pParam.getdateforpage().toString());
                                timeDetails.add("Month");
                                timeDetails.add("Last Period");
                            }
                            if (dimId.equalsIgnoreCase("Time-Range Basis")) {
                                if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DATE1")) {
                                    rangeArray.add(String.valueOf(pParam.getdateforpage(30)));
                                    rangeArray.add("CBO_AS_OF_DATE1");
                                    rangeArray.add("From DATE");
                                    rangeArray.add("1");
                                    rangeArray.add("1");
                                    rangeArray.add(null);
                                    rangeArray.add("AS_OF_DATE1");
                                    timeDimMap.put("AS_OF_DATE1", rangeArray);

                                    collectTimeDetailsArray.add("Day");
                                    collectTimeDetailsArray.add("PRG_DATE_RANGE");
                                    //collectTimeDetailsArray.add(sdf.format(date));
                                    //collectTimeDetailsArray.add("05/31/2008");
                                    collectTimeDetailsArray.add(String.valueOf(pParam.getdateforpage(30)));
                                    collectTimeDetailsArray.add(String.valueOf(pParam.getdateforpage()));
                                    collectTimeDetailsArray.add(String.valueOf(pParam.getdateforpage(60)));
                                    collectTimeDetailsArray.add(String.valueOf(pParam.getdateforpage(31)));;
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DATE2")) {
                                    rangeArray1.add(String.valueOf(pParam.getdateforpage(1)));
                                    rangeArray1.add("CBO_AS_OF_DATE2");
                                    rangeArray1.add("To DATE");
                                    rangeArray1.add("2");
                                    rangeArray1.add("2");
                                    rangeArray1.add(null);
                                    rangeArray1.add("AS_OF_DATE2");
                                    timeDimMap.put("AS_OF_DATE2", rangeArray1);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE1")) {
                                    rangeArray2.add(String.valueOf(pParam.getdateforpage(31)));
                                    rangeArray2.add("CBO_CMP_AS_OF_DATE1");
                                    rangeArray2.add("Comp From DATE");
                                    rangeArray2.add("3");
                                    rangeArray2.add("3");
                                    rangeArray2.add(null);
                                    rangeArray2.add("CMP_AS_OF_DATE1");
                                    timeDimMap.put("CMP_AS_OF_DATE1", rangeArray2);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                                    rangeArray3.add(String.valueOf(pParam.getdateforpage(60)));
                                    rangeArray3.add("CBO_CMP_AS_OF_DATE2");
                                    rangeArray3.add("Comp To DATE");
                                    rangeArray3.add("4");
                                    rangeArray3.add("4");
                                    rangeArray3.add(null);
                                    rangeArray3.add("CMP_AS_OF_DATE2");
                                    timeDimMap.put("CMP_AS_OF_DATE2", rangeArray3);
                                }
                                timeDetails.add("Day");
                                timeDetails.add("PRG_DATE_RANGE");
                                timeDetails.add(pParam.getdateforpage().toString());
                                timeDetails.add(null);
                                timeDetails.add(null);
                                timeDetails.add(null);
                            }
                            if (dimId.equalsIgnoreCase("Time-Rolling Period")) {
                                if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DATE")) {
                                    rangeArray.add(String.valueOf(pParam.getdateforpage(30)));
                                    rangeArray.add("CBO_AS_OF_DATE");
                                    rangeArray.add("DATE");
                                    rangeArray.add("1");
                                    rangeArray.add("1");
                                    rangeArray.add(null);
                                    rangeArray.add("AS_OF_DATE");
                                    timeDimMap.put("AS_OF_DATE", rangeArray);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("PRG_DAY_ROLLING")) {
                                    rangeArray1.add("Last 30 Days");
                                    rangeArray1.add("CBO_PRG_DAY_ROLLING");
                                    rangeArray1.add("Rolling Period");
                                    rangeArray1.add("2");
                                    rangeArray1.add("2");
                                    rangeArray1.add("Last 30 Days");
                                    rangeArray1.add("PRG_DAY_ROLLING");
                                    timeDimMap.put("PRG_DAY_ROLLING", rangeArray1);
                                }
                                timeDetails.add("Day");
                                timeDetails.add("PRG_DAY_ROLLING");
                                timeDetails.add(pParam.getdateforpage().toString());
                                timeDetails.add("Last 30 Days");
                            }
                            if (dimId.equalsIgnoreCase("Time-Month Range Basis")) {
                                if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DMONTH1")) {
                                    rangeArray.add(pParam.getdateforpage().toString());
                                    rangeArray.add("CBO_AS_OF_DMONTH1");
                                    rangeArray.add("From MONTH");
                                    rangeArray.add("1");
                                    rangeArray.add("1");
                                    rangeArray.add(null);
                                    rangeArray.add("AS_OF_DMONTH1");
                                    timeDimMap.put("AS_OF_DMONTH1", rangeArray);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DMONTH2")) {
                                    rangeArray1.add(pParam.getdateforpage(30).toString());
                                    rangeArray1.add("CBO_AS_OF_DMONTH2");
                                    rangeArray1.add("To MONTH");
                                    rangeArray1.add("2");
                                    rangeArray1.add("2");
                                    rangeArray1.add(null);
                                    rangeArray1.add("AS_OF_DMONTH2");
                                    timeDimMap.put("AS_OF_DMONTH2", rangeArray1);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DMONTH1")) {
                                    rangeArray2.add(pParam.getdateforpage(31).toString());
                                    rangeArray2.add("CBO_CMP_AS_OF_DMONTH1");
                                    rangeArray2.add("Comp From MONTH");
                                    rangeArray2.add("3");
                                    rangeArray2.add("3");
                                    rangeArray2.add(null);
                                    rangeArray2.add("CMP_AS_OF_DMONTH1");
                                    timeDimMap.put("CMP_AS_OF_DMONTH1", rangeArray2);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DM0NTH2")) {
                                    rangeArray3.add(pParam.getdateforpage(60).toString());
                                    rangeArray3.add("CBO_CMP_AS_OF_DMONTH2");
                                    rangeArray3.add("Comp To MONTH");
                                    rangeArray3.add("4");
                                    rangeArray3.add("4");
                                    rangeArray3.add(null);
                                    rangeArray3.add("CMP_AS_OF_DM0NTH2");
                                    timeDimMap.put("CMP_AS_OF_DMONTH2", rangeArray3);
                                }
                                timeDetails.add("Day");
                                timeDetails.add("PRG_MONTH_RANGE");
                                timeDetails.add(pParam.getmonthforpage().toString());
                                timeDetails.add(null);
                                timeDetails.add(null);
                                timeDetails.add(null);
                            }
                            if (dimId.equalsIgnoreCase("Time-Quarter Range Basis")) {
                                if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DQUARTER1")) {
                                    rangeArray.add(pParam.getdateforpage().toString());
                                    rangeArray.add("CBO_AS_OF_DQUARTER1");
                                    rangeArray.add("From QUARTER");
                                    rangeArray.add("1");
                                    rangeArray.add("1");
                                    rangeArray.add(null);
                                    rangeArray.add("AS_OF_DQUARTER1");
                                    timeDimMap.put("AS_OF_DQUARTER1", rangeArray);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DQUARTER2")) {
                                    rangeArray1.add(pParam.getdateforpage(30).toString());
                                    rangeArray1.add("CBO_AS_OF_DQUARTER2");
                                    rangeArray1.add("To QUARTER");
                                    rangeArray1.add("2");
                                    rangeArray1.add("2");
                                    rangeArray1.add(null);
                                    rangeArray1.add("AS_OF_DQUARTER2");
                                    timeDimMap.put("AS_OF_DQUARTER2", rangeArray1);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DQUARTER1")) {
                                    rangeArray2.add(pParam.getdateforpage(31).toString());
                                    rangeArray2.add("CBO_CMP_AS_OF_DQUARTER1");
                                    rangeArray2.add("Comp From QUARTER");
                                    rangeArray2.add("3");
                                    rangeArray2.add("3");
                                    rangeArray2.add(null);
                                    rangeArray2.add("CMP_AS_OF_DQUARTER1");
                                    timeDimMap.put("CMP_AS_OF_DQUARTER1", rangeArray2);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DQUARTER2")) {
                                    rangeArray3.add(pParam.getdateforpage(60).toString());
                                    rangeArray3.add("CBO_CMP_AS_OF_DQUARTER2");
                                    rangeArray3.add("Comp To QUARTER");
                                    rangeArray3.add("4");
                                    rangeArray3.add("4");
                                    rangeArray3.add(null);
                                    rangeArray3.add("CMP_AS_OF_DQUARTER2");
                                    timeDimMap.put("CMP_AS_OF_DQUARTER2", rangeArray3);
                                }
                                timeDetails.add("Day");
                                timeDetails.add("PRG_QUARTER_RANGE");
                                timeDetails.add("Dquarter");
                                timeDetails.add(null);
                                timeDetails.add(null);
                                timeDetails.add(null);
                            }
                            if (dimId.equalsIgnoreCase("Time-Year Range Basis")) {
                                if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DYEAR1")) {
                                    rangeArray.add(pParam.getdateforpage().toString());
                                    rangeArray.add("CBO_AS_OF_DYEAR1");
                                    rangeArray.add("From YEAR");
                                    rangeArray.add("1");
                                    rangeArray.add("1");
                                    rangeArray.add(null);
                                    rangeArray.add("AS_OF_DYEAR1");
                                    timeDimMap.put("AS_OF_DYEAR1", rangeArray);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_DATE")) {
                                    rangeArray1.add(pParam.getdateforpage(30).toString());
                                    rangeArray1.add("CBO_AS_OF_DYEAR2");
                                    rangeArray1.add("To YEAR");
                                    rangeArray1.add("2");
                                    rangeArray1.add("2");
                                    rangeArray1.add(null);
                                    rangeArray1.add("AS_OF_DYEAR2");
                                    timeDimMap.put("AS_OF_DYEAR2", rangeArray1);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DYEAR1")) {
                                    rangeArray2.add(pParam.getdateforpage(31).toString());
                                    rangeArray2.add("CBO_CMP_AS_OF_DYEAR1");
                                    rangeArray2.add("Comp From YEAR");
                                    rangeArray2.add("3");
                                    rangeArray2.add("3");
                                    rangeArray2.add(null);
                                    rangeArray2.add("CMP_AS_OF_DYEAR1");
                                    timeDimMap.put("CMP_AS_OF_DYEAR1", rangeArray2);
                                } else if (timeParameters.get(time).toString().equalsIgnoreCase("CMP_AS_OF_DYEAR2")) {
                                    rangeArray3.add(pParam.getdateforpage(60).toString());
                                    rangeArray3.add("CBO_CMP_AS_OF_DYEAR2");
                                    rangeArray3.add("Comp To YEAR");
                                    rangeArray3.add("4");
                                    rangeArray3.add("4");
                                    rangeArray3.add(null);
                                    rangeArray3.add("CMP_AS_OF_DYEAR2");
                                    timeDimMap.put("CMP_AS_OF_DYEAR2", rangeArray3);
                                }
                                timeDetails.add("Day");
                                timeDetails.add("PRG_YEAR_RANGE");
                                timeDetails.add(pParam.getYearforpage().toString());
                                timeDetails.add(null);
                                timeDetails.add(null);
                                timeDetails.add(null);
                            }
                        } else if (minTimeLevel.equals("4")) {
                            if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_WEEK")) {
                                weekArray.add(null);
                                weekArray.add("CBO_AS_OF_WEEK");
                                weekArray.add("WEEK");
                                weekArray.add("1");
                                weekArray.add("1");
                                weekArray.add(null);
                                weekArray.add("AS_OF_WEEK");
                                timeDimMap.put("AS_OF_WEEK", weekArray);
                            } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_WEEK1")) {
                                asofWeekArray.add(null);
                                asofWeekArray.add("CBO_AS_OF_WEEK1");
                                asofWeekArray.add("COMPAREWEEK");
                                asofWeekArray.add("2");
                                asofWeekArray.add("2");
                                asofWeekArray.add(null);
                                asofWeekArray.add("AS_OF_WEEK1");
                                timeDimMap.put("AS_OF_WEEK1", asofWeekArray);
                            }
                            timeDetails.add("WEEK");
                            timeDetails.add("PRG_WEEK_CMP");
                            timeDetails.add(null);
                            timeDetails.add(null);

                        } else if (minTimeLevel.equals("3")) {
                            if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_MONTH")) {
                                monthArray.add(null);
                                monthArray.add("CBO_AS_OF_MONTH");
                                monthArray.add("MONTH");
                                monthArray.add("1");
                                monthArray.add("1");
                                monthArray.add(null);
                                monthArray.add("AS_OF_MONTH");
                                timeDimMap.put("AS_OF_MONTH", monthArray);
                            } else if (timeParameters.get(time).toString().equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                                periodTypeArray.add("MONTH");
                                periodTypeArray.add("CBO_PRG_PERIOD_TYPE");
                                periodTypeArray.add("AGGREGATION");
                                periodTypeArray.add("2");
                                periodTypeArray.add("2");
                                periodTypeArray.add("MONTH");
                                periodTypeArray.add("PRG_PERIOD_TYPE");
                                timeDimMap.put("PRG_PERIOD_TYPE", periodTypeArray);
                            } else if (timeParameters.get(time).toString().equalsIgnoreCase("PRG_COMPARE")) {
                                compareArray.add("LAST PERIOD");
                                compareArray.add("CBO_PRG_COMPARE");
                                compareArray.add("COMPARE");
                                compareArray.add("3");
                                compareArray.add("1001");
                                compareArray.add("LAST PERIOD");
                                compareArray.add("PRG_COMPARE");
                                timeDimMap.put("PRG_COMPARE", compareArray);
                            }
                            timeDetails.add("Month");
                            timeDetails.add("PRG_STD");
                            timeDetails.add(null);
                            timeDetails.add("Month");
                            timeDetails.add("Last Period");

                        } else if (minTimeLevel.equals("2")) {
                            if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_QUARTER")) {
                                qtrArray.add(null);
                                qtrArray.add("CBO_AS_OF_QUARTER");
                                qtrArray.add("QUARTER");
                                qtrArray.add("1");
                                qtrArray.add("1");
                                qtrArray.add(null);
                                qtrArray.add("AS_OF_QUARTER");
                                timeDimMap.put("AS_OF_QUARTER", qtrArray);
                            } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_QUARTER1")) {
                                asofQtrArray.add(null);
                                asofQtrArray.add("CBO_AS_OF_QUARTER1");
                                asofQtrArray.add("COMPAREQUARTER");
                                asofQtrArray.add("2");
                                asofQtrArray.add("2");
                                asofQtrArray.add(null);
                                asofQtrArray.add("AS_OF_QUARTER1");
                                timeDimMap.put("AS_OF_QUARTER1", asofQtrArray);
                            }
                            timeDetails.add("QUARTER");
                            timeDetails.add("PRG_QUARTER_CMP");
                            timeDetails.add(null);
                            timeDetails.add(null);

                        } else if (minTimeLevel.equals("1")) {
                            if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_YEAR")) {
                                yearArray.add(null);
                                yearArray.add("CBO_AS_OF_YEAR");
                                yearArray.add("YEAR");
                                yearArray.add("1");
                                yearArray.add("1");
                                yearArray.add(null);
                                yearArray.add("AS_OF_YEAR");
                                timeDimMap.put("AS_OF_YEAR", yearArray);
                            } else if (timeParameters.get(time).toString().equalsIgnoreCase("AS_OF_YEAR1")) {
                                asofYrArray.add(null);
                                asofYrArray.add("CBO_AS_OF_YEAR1");
                                asofYrArray.add("COMPAREYEAR");
                                asofYrArray.add("2");
                                asofYrArray.add("2");
                                asofYrArray.add(null);
                                asofYrArray.add("AS_OF_YEAR1");
                                timeDimMap.put("AS_OF_YEAR1", asofYrArray);
                            }
                            timeDetails.add("YEAR");
                            timeDetails.add("PRG_YEAR_CMP");
                            timeDetails.add(null);
                            timeDetails.add(null);
                        }
                    }
                }
                ParametersMap.put("TimeDimHashMap", timeDimMap);
                ParametersMap.put("TimeDetailstList", timeDetails);
                collect.timeDetailsArray = collectTimeDetailsArray;
                collect.reportParametersValues = new LinkedHashMap<String, String>();
                if (foldersIds != null) {
                    collect.reportBizRoles = foldersIds.split(",");
                }
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }

    }
    //added by sruthi for hide columns
    public ActionForward hideColumns(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String selectedItems = request.getParameter("selectedItems");
        String kpidashid = request.getParameter("kpidashid");
        String dashBoardId = request.getParameter("dashBoardId");
        HashMap map = new HashMap();
        String[] parameters = null;
        Container container = null;
        DashletDetail detail = null;

        //  DashletDetail detail = dashboardcollect.getDashletDetail(kpidashid);
        ArrayList<String> selecteddata = new ArrayList<String>();
        if (selectedItems != null) {
            parameters = selectedItems.split(",");
            for (int i = 0; i < parameters.length; i++) {
                selecteddata.add(parameters[i]);
            }
        }
        if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(dashBoardId) != null) {
                    container = (Container) map.get(dashBoardId);
                    pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
                    detail = collect.getDashletDetail(kpidashid);
                }
            }
        }
        detail.setHidecolumns(selecteddata);
        return null;
    }
//ended by sruthi
}
